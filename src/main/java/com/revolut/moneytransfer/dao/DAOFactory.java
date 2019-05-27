package com.revolut.moneytransfer.dao;

public abstract class DAOFactory {

	public static final int H2 = 1;

	public abstract AccountDAO getAccountDAO();
	
	public abstract TransactionDAO getTransferDAO();
	
	public abstract CurrencyDAO getCurrencyDAO();

	public abstract void populateTestData();

	public static DAOFactory getDAOFactory(int factoryCode) {

		switch (factoryCode) {
		case H2:
			return new H2DAOFactory();
		default:
			// by default using H2 in memory database
			return new H2DAOFactory();
		}
	}

}
