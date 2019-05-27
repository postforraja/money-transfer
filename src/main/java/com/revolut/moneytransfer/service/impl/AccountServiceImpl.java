package com.revolut.moneytransfer.service.impl;

import java.util.List;

import com.revolut.moneytransfer.dao.DAOFactory;
import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.service.AccountService;

public class AccountServiceImpl implements AccountService {
	private final DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

	@Override
	public List<Account> getAllAccounts() throws CustomException {
		return daoFactory.getAccountDAO().getAllAccounts();
	}

	@Override
	public Account getAccount(long accountId) throws CustomException {
		return daoFactory.getAccountDAO().getAccountById(accountId);
	}

	@Override
	public Long createAccount(Account account) throws CustomException {
		return daoFactory.getAccountDAO().createAccount(account);
	}

	@Override
	public int updateAccount(Account account) throws CustomException {
		return daoFactory.getAccountDAO().updateAccountBalance(account);
	}

	@Override
	public int deleteAccount(long accountId) throws CustomException {
		return daoFactory.getAccountDAO().deleteAccountById(accountId);
	}

}
