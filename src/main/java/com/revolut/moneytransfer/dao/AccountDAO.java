package com.revolut.moneytransfer.dao;

import java.util.List;

import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.model.Account;

public interface AccountDAO {
    List<Account> getAllAccounts() throws CustomException;
    Account getAccountById(long accountId) throws CustomException;
    Long createAccount(Account account) throws CustomException;
    int updateAccountBalance(Account account) throws CustomException;
    int deleteAccountById(long accountId) throws CustomException;
}
