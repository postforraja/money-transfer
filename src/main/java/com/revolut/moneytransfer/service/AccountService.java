package com.revolut.moneytransfer.service;

import java.util.List;

import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.model.Account;

public interface AccountService {
	List<Account> getAllAccounts() throws CustomException;

	Account getAccount(long accountId) throws CustomException;

	Long createAccount(Account account) throws CustomException;

	int updateAccount(Account account) throws CustomException;

	int deleteAccount(long accountId) throws CustomException;
}
