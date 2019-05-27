package com.revolut.moneytransfer.service;

import com.revolut.moneytransfer.service.impl.AccountServiceImpl;
import com.revolut.moneytransfer.service.impl.TransactionServiceImpl;

public class ServiceFactory {
	private final AccountService accountService = new AccountServiceImpl();
	private final TransactionService transactionService = new TransactionServiceImpl();
	
	public AccountService getAccountService() {
		return accountService;
	}
	
	public TransactionService getTransactionService() {
		return transactionService;
	}
	
	public static ServiceFactory getServiceFactory() {
		return new ServiceFactory();
	}

}
