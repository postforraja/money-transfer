package com.revolut.moneytransfer.dao;

import java.util.List;

import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.model.Transaction;

public interface TransactionDAO {
	int transferFund(Transaction transaction) throws CustomException;
	void createTransaction(Transaction transaction);
	List<Transaction> getAllTransactions() throws CustomException;
}
