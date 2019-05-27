package com.revolut.moneytransfer.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.revolut.moneytransfer.dao.DAOFactory;
import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.model.Transaction;
import com.revolut.moneytransfer.model.TransactionStatus;
import com.revolut.moneytransfer.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {
	private final DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

	@Override
	public int transferFund(Transaction transaction) throws CustomException {
		int rows = 0;
		
		if(daoFactory.getCurrencyDAO().validateCurrencyCode(transaction.getCurrencyCode())) {
			transaction.setStatus(TransactionStatus.FAILED.toString());
			
			CompletableFuture.runAsync(() -> daoFactory.getTransferDAO().createTransaction(transaction));
			
			return rows;
		}
		
		synchronized (this) {
			rows = daoFactory.getTransferDAO().transferFund(transaction);			
		}
		
		transaction.setStatus(TransactionStatus.COMPLETED.toString());
		
		CompletableFuture.runAsync(() -> daoFactory.getTransferDAO().createTransaction(transaction));
		
		return rows;
	}
	
	@Override
	public List<Transaction> getAllTransactions() throws CustomException {
		return daoFactory.getTransferDAO().getAllTransactions();
	}
}
