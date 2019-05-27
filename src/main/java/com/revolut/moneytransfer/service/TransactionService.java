package com.revolut.moneytransfer.service;

import java.util.List;

import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.model.Transaction;

public interface TransactionService {
	int transferFund(Transaction transaction) throws CustomException;
	List<Transaction> getAllTransactions() throws CustomException;
}
