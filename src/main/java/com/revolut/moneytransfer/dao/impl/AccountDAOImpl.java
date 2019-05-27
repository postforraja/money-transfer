package com.revolut.moneytransfer.dao.impl;

import com.revolut.moneytransfer.dao.AccountDAO;
import com.revolut.moneytransfer.dao.H2DAOFactory;
import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.model.Account;
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

public class AccountDAOImpl implements AccountDAO {
	private static Logger logger = Logger.getLogger(AccountDAOImpl.class);

	/**
	 * Get all accounts
	 */
	@Override
	public List<Account> getAllAccounts() throws CustomException {
		List<Account> accountList = new ArrayList<>();

		try (Connection connection = H2DAOFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(ConstantUtil.GET_ALL_ACCOUNT);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				Account account = new Account(resultSet.getLong("ACCOUNT_ID"), resultSet.getLong("CLIENT_ID"),
						resultSet.getBigDecimal("ACCOUNT_BALANCE"), resultSet.getString("ACCOUNT_CURRENCY_CODE"));

				if (logger.isDebugEnabled())
					logger.debug("getAllAccounts(): Get Account " + account);

				accountList.add(account);
			}

			return accountList;
		} catch (SQLException e) {
			throw new CustomException("getAllAccounts(): Error reading account data", e);
		}
	}

	/**
	 * Get account by id
	 */
	@Override
	public Account getAccountById(long accountId) throws CustomException {
		Account account = null;

		try (Connection connection = H2DAOFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(ConstantUtil.GET_ACCOUNT_BY_ID)) {

			statement.setLong(1, accountId);
			
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					account = new Account(accountId, resultSet.getLong("CLIENT_ID"),
							resultSet.getBigDecimal("ACCOUNT_BALANCE"), resultSet.getString("ACCOUNT_CURRENCY_CODE"));

					if (logger.isDebugEnabled())
						logger.debug("getAccountById(): Get Account By Id: " + account);
				}
			}

			return account;
		} catch (SQLException e) {
			throw new CustomException("getAccountById(): Error reading account data", e);
		}
	}

	/**
	 * Create account
	 */
	@Override
	public Long createAccount(Account account) throws CustomException {
		Long id = null;

		try (Connection connection = H2DAOFactory.getConnection();
				PreparedStatement sequenceStatement = connection.prepareStatement(ConstantUtil.CREATE_ACCOUNT_SEQUENCE);
				ResultSet resultSet = sequenceStatement.executeQuery();
				PreparedStatement createStatement = connection.prepareStatement(ConstantUtil.CREATE_ACCOUNT)) {

			while (resultSet.next()) {
				id = resultSet.getLong("SEQ_ID");
			}

			createStatement.setLong(1, id);
			createStatement.setLong(2, account.getClientId());
			createStatement.setBigDecimal(3, account.getBalance());
			createStatement.setString(4, account.getCurrencyCode());
			createStatement.setString(5, account.getUser());
			createStatement.setString(6, account.getUser());
			int affectedRows = createStatement.executeUpdate();

			if (affectedRows == 0) {
				logger.error("createAccount(): Create account failed, no rows inserted");
				throw new CustomException("Account Cannot be created");
			}

			return id;
		} catch (SQLException e) {
			logger.error("Error Inserting Account  " + account);
			throw new CustomException("createAccount(): Error creating user account " + account, e);
		}
	}

	/**
	 * Update account
	 */
	@Override
	public int updateAccountBalance(Account account) throws CustomException {
		Connection connection = null;
		PreparedStatement selectStatement = null;
		PreparedStatement updateStatement = null;
		ResultSet resultSet = null;
		Account accountDB = null;
		int updateCount = 0;
		try {
			connection = H2DAOFactory.getConnection();
			connection.setAutoCommit(false);
			selectStatement = connection.prepareStatement(ConstantUtil.LOCK_ACCOUNT_BY_ID);
			selectStatement.setLong(1, account.getAccountId());
			resultSet = selectStatement.executeQuery();

			while (resultSet.next()) {
				accountDB = new Account(resultSet.getLong("ACCOUNT_ID"), resultSet.getLong("CLIENT_ID"),
						resultSet.getBigDecimal("ACCOUNT_BALANCE"), resultSet.getString("ACCOUNT_CURRENCY_CODE"));

				if (logger.isDebugEnabled())
					logger.debug("updateAccountBalance() from Account: " + accountDB);
			}

			if (accountDB == null) {
				throw new CustomException("updateAccountBalance(): fail to lock account : " + account.getAccountId());
			}
			
			if (!account.getCurrencyCode().equals(accountDB.getCurrencyCode())) {
				throw new CustomException(
						"Failed to update Account Balance, currencies are different between account and deposit/withdraw");
			}

			BigDecimal balance = accountDB.getBalance().add(account.getDeltaAmount());

			if (balance.compareTo(BigDecimal.ZERO) < 0) {
				throw new CustomException("Not sufficient Fund for account: " + account.getAccountId());
			}

			updateStatement = connection.prepareStatement(ConstantUtil.UPDATE_ACCOUNT_BALANCE);
			updateStatement.setBigDecimal(1, balance);
			updateStatement.setLong(2, account.getAccountId());
			updateCount = updateStatement.executeUpdate();
			connection.commit();

			if (logger.isDebugEnabled())
				logger.debug("New Balance after Update: " + balance);

			return updateCount;
		} catch (SQLException se) {
			logger.error("updateAccountBalance(): User Transaction Failed, rollback initiated for: "
					+ account.getAccountId(), se);

			try {
				if (connection != null)
					connection.rollback();
			} catch (SQLException re) {
				throw new CustomException("Fail to rollback transaction", re);
			}
		} finally {
			DbUtils.closeQuietly(connection, selectStatement, resultSet);
			DbUtils.closeQuietly(updateStatement);
		}

		return updateCount;
	}

	/**
	 * Delete account by id
	 */
	@Override
	public int deleteAccountById(long accountId) throws CustomException {
		try (Connection connection = H2DAOFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(ConstantUtil.DELETE_ACCOUNT_BY_ID)) {
			statement.setLong(1, accountId);
			return statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("deleteAccountById(): Error deleting user account Id " + accountId, e);
		}
	}

}
