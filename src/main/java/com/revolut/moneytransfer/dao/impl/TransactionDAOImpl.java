package com.revolut.moneytransfer.dao.impl;

import com.revolut.moneytransfer.dao.H2DAOFactory;
import com.revolut.moneytransfer.dao.TransactionDAO;
import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.Transaction;
import com.revolut.moneytransfer.model.TransactionStatus;
import com.revolut.moneytransfer.util.ConstantUtil;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {
	private static Logger logger = Logger.getLogger(TransactionDAOImpl.class);
	
	/**
	 * Money transfer between accounts
	 */
	@Override
	public int transferFund(Transaction transaction) throws CustomException {
		int result = 0;
		Connection connection = null;
		PreparedStatement sourceSelectStatement = null;
		PreparedStatement destinationSelectStatement = null;
		PreparedStatement updateStatement = null;
		ResultSet sourceResultSet = null;
		ResultSet destinationResultSet = null;
		Account sourceAccount = null;
		Account destinationAccount = null;

		try {
			connection = H2DAOFactory.getConnection();
			connection.setAutoCommit(false);
			
			sourceAccount = getSourceAccount(connection, sourceSelectStatement, sourceResultSet, sourceAccount, transaction);
			destinationAccount = getDestinationAccount(connection, destinationSelectStatement, destinationResultSet, destinationAccount, transaction);
			BigDecimal sourceAccountBalance = validateTransfer(sourceAccount, destinationAccount, transaction);
			result = updateAccounts(connection, updateStatement, sourceAccountBalance, destinationAccount, transaction);

			connection.commit();
		} catch (SQLException se) {
			logger.error("transferFund(): User Transaction Failed, rollback initiated for: " + transaction,
					se);
			try {
				if (connection != null)
					connection.rollback();
			} catch (SQLException re) {
				throw new CustomException("Failed to rollback transaction", re);
			}
			
			transaction.setStatus(TransactionStatus.FAILED.toString());
			createTransaction(transaction);
		} finally {
			DbUtils.closeQuietly(connection, sourceSelectStatement, sourceResultSet);
			DbUtils.closeQuietly(destinationSelectStatement);
			DbUtils.closeQuietly(updateStatement);
			DbUtils.closeQuietly(destinationResultSet);
		}
		return result;
	}
	
	private Account getSourceAccount(Connection connection, PreparedStatement sourceSelectStatement, 
			ResultSet sourceResultSet, Account sourceAccount, Transaction transaction) throws SQLException {
		sourceSelectStatement = connection.prepareStatement(ConstantUtil.LOCK_ACCOUNT_BY_ID);
		sourceSelectStatement.setLong(1, transaction.getSourceAccountId());
		sourceResultSet = sourceSelectStatement.executeQuery();

		while (sourceResultSet.next()) {
			sourceAccount = new Account(sourceResultSet.getLong("ACCOUNT_ID"), sourceResultSet.getLong("CLIENT_ID"),
					sourceResultSet.getBigDecimal("ACCOUNT_BALANCE"), sourceResultSet.getString("ACCOUNT_CURRENCY_CODE"));

			if (logger.isDebugEnabled())
				logger.debug("transferFund() source Account: " + sourceAccount);
		}
		
		return sourceAccount;
	}
	
	private Account getDestinationAccount(Connection connection, PreparedStatement destinationSelectStatement, 
			ResultSet destinationResultSet, Account destinationAccount, Transaction transaction) throws SQLException {
		destinationSelectStatement = connection.prepareStatement(ConstantUtil.LOCK_ACCOUNT_BY_ID);
		destinationSelectStatement.setLong(1, transaction.getDestinationAccountId());
		destinationResultSet = destinationSelectStatement.executeQuery();

		while (destinationResultSet.next()) {
			destinationAccount = new Account(destinationResultSet.getLong("ACCOUNT_ID"), destinationResultSet.getLong("CLIENT_ID"),
					destinationResultSet.getBigDecimal("ACCOUNT_BALANCE"), destinationResultSet.getString("ACCOUNT_CURRENCY_CODE"));

			if (logger.isDebugEnabled())
				logger.debug("transferFund() destination Account: " + destinationAccount);
		}
		
		return destinationAccount;
	}
	
	private BigDecimal validateTransfer(Account sourceAccount, Account destinationAccount, Transaction transaction) throws CustomException {
		if (sourceAccount == null || destinationAccount == null) {
			throw new CustomException("Failed to lock both accounts");
		}
		
		if (!sourceAccount.getCurrencyCode().equals(destinationAccount.getCurrencyCode())) {
			throw new CustomException(
					"Failed to transfer Fund, transaction currencies are different between source and destination");
		}

		if (!sourceAccount.getCurrencyCode().equals(transaction.getCurrencyCode())) {
			throw new CustomException(
					"Failed to transfer Fund, transaction currencies are different between source/destination and transaction");
		}

		BigDecimal sourceAccountBalance = sourceAccount.getBalance().subtract(transaction.getAmount());
		if (sourceAccountBalance.compareTo(BigDecimal.ZERO) <= 0) {
			throw new CustomException("Not enough Fund from source Account ");
		}
		
		return sourceAccountBalance;
	}
	
	private int updateAccounts(Connection connection, PreparedStatement updateStatement, 
			BigDecimal sourceAccountBalance, Account destinationAccount, Transaction transaction) throws SQLException {
		int result = 0;
		updateStatement = connection.prepareStatement(ConstantUtil.UPDATE_ACCOUNT_BALANCE);
		updateStatement.setBigDecimal(1, sourceAccountBalance);
		updateStatement.setLong(2, transaction.getSourceAccountId());
		updateStatement.addBatch();
		updateStatement.setBigDecimal(1, destinationAccount.getBalance().add(transaction.getAmount()));
		updateStatement.setLong(2, transaction.getDestinationAccountId());
		updateStatement.addBatch();
		int[] rowsUpdated = updateStatement.executeBatch();
		result = rowsUpdated[0] + rowsUpdated[1];
		
		if (logger.isDebugEnabled()) {
			logger.debug("Number of rows updated for the transfer : " + result);
		}
		
		return result;
	}
	
	@Override
	public void createTransaction(Transaction transaction) {
		Long id = null;
		
		try (Connection connection = H2DAOFactory.getConnection();
				PreparedStatement sequenceStatement = connection.prepareStatement(ConstantUtil.CREATE_TRANSACTION_SEQUENCE);
				ResultSet resultSet = sequenceStatement.executeQuery();
				PreparedStatement createStatement = connection.prepareStatement(ConstantUtil.CREATE_TRANSACTION)) {

			while (resultSet.next()) {
				id = resultSet.getLong("SEQ_ID");
			}

			createStatement.setLong(1, id);
			createStatement.setLong(2, transaction.getSourceAccountId());
			createStatement.setLong(3, transaction.getDestinationAccountId());
			createStatement.setBigDecimal(4, transaction.getAmount());
			createStatement.setString(5, transaction.getCurrencyCode());
			createStatement.setString(6, transaction.getComment());
			createStatement.setString(7, transaction.getStatus().toString());
			createStatement.setString(8, transaction.getUser());
			createStatement.setString(9, transaction.getUser());
			int affectedRows = createStatement.executeUpdate();

			if (affectedRows == 0) {
				logger.error("createTransaction(): Create Transaction failed, no rows inserted");
			}

		} catch (SQLException e) {
			logger.error("Error Inserting Transaction  " + transaction);
		}
	}
	
	/**
	 * Get all Transactions
	 */
	@Override
	public List<Transaction> getAllTransactions() throws CustomException {
		List<Transaction> transactionList = new ArrayList<>();

		try (Connection connection = H2DAOFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(ConstantUtil.GET_ALL_TRANSACTION);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				Transaction transaction = new Transaction(resultSet.getLong("TRANSACTION_ID"), resultSet.getLong("SOURCE_ACCOUNT_ID"),
						resultSet.getLong("DESTINATION_ACCOUNT_ID"), resultSet.getBigDecimal("AMOUNT"), resultSet.getString("ACCOUNT_CURRENCY_CODE"), 
						resultSet.getString("COMMENT"), resultSet.getString("STATUS"));

				if (logger.isDebugEnabled())
					logger.debug("getAllTransactions(): Get transaction " + transaction);

				transactionList.add(transaction);
			}

			return transactionList;
		} catch (SQLException e) {
			throw new CustomException("getAllTransactions(): Error reading Transaction data", e);
		}
	}
}