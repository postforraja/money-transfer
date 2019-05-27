package com.revolut.moneytransfer.dao;

import com.revolut.moneytransfer.dao.impl.AccountDAOImpl;
import com.revolut.moneytransfer.dao.impl.CurrencyDAOImpl;
import com.revolut.moneytransfer.dao.impl.TransactionDAOImpl;
import com.revolut.moneytransfer.util.PropertyUtil;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DAOFactory extends DAOFactory {
	private static final String h2_driver = PropertyUtil.getStringProperty("h2_driver");
	private static final String h2_connection_url = PropertyUtil.getStringProperty("h2_connection_url");
	private static final String h2_user = PropertyUtil.getStringProperty("h2_user");
	private static final String h2_password = PropertyUtil.getStringProperty("h2_password");
	private static Logger logger = Logger.getLogger(H2DAOFactory.class);

	private final AccountDAO accountDAO = new AccountDAOImpl();
	private final TransactionDAO transferDAO = new TransactionDAOImpl();
	private final CurrencyDAO currencyDAO = new CurrencyDAOImpl();

	H2DAOFactory() {
		DbUtils.loadDriver(h2_driver);
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(h2_connection_url, h2_user, h2_password);

	}

	public AccountDAO getAccountDAO() {
		return accountDAO;
	}
	
	public TransactionDAO getTransferDAO() {
		return transferDAO;
	}
	
	public CurrencyDAO getCurrencyDAO() {
		return currencyDAO;
	}

	@Override
	public void populateTestData() {
		logger.info("Populating Account, Transaction Tables and data ..... ");
		Connection conn = null;
		try {
			conn = H2DAOFactory.getConnection();
			RunScript.execute(conn, new FileReader("src/test/resources/DB_SCHEMA.sql"));
			RunScript.execute(conn, new FileReader("src/test/resources/DB_DATA.sql"));
		} catch (SQLException e) {
			logger.error("populateTestData(): Error populating user data: ", e);
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			logger.error("populateTestData(): Error finding test script file ", e);
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

}
