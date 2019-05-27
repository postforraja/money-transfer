package com.revolut.moneytransfer.dao;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.model.Account;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class AccountDAOTest {
	
	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

	@BeforeClass
	public static void setup() {
		h2DaoFactory.populateTestData();
	}
	
	@Test
	public void testGetAllAccounts() throws CustomException {
		List<Account> allAccounts = h2DaoFactory.getAccountDAO().getAllAccounts();
		assertTrue(allAccounts.size() > 1);
	}

	@Test
	public void testGetAccountById() throws CustomException {
		Account account = h2DaoFactory.getAccountDAO().getAccountById(1000);
		assertTrue(account.getCurrencyCode().equals("USD"));
	}

	@Test
	public void testGetNonExistingAccById() throws CustomException {
		Account account = h2DaoFactory.getAccountDAO().getAccountById(1000000);
		assertTrue(account == null);
	}

	@Test
	public void testCreateAccount() throws CustomException {
		Account account = new Account(100, BigDecimal.TEN, "EUR", "test");
		Long clientId = 100L;
		long aid = h2DaoFactory.getAccountDAO().createAccount(account);
		Account afterCreation = h2DaoFactory.getAccountDAO().getAccountById(aid);
		assertTrue(clientId.equals(afterCreation.getClientId()));
		assertTrue(afterCreation.getCurrencyCode().equals("EUR"));
		assertTrue(afterCreation.getBalance().compareTo(BigDecimal.TEN)==0);
	}
	
	@Test
	public void testUpdateAccountBalance() throws CustomException {
		Account account = new Account(5000, 4, BigDecimal.valueOf(600), "USD");
		account.setDeltaAmount(BigDecimal.TEN);
		assertTrue(h2DaoFactory.getAccountDAO().updateAccountBalance(account) == 1);
	}
	
	@Test(expected = CustomException.class)
	public void testUpdateAccountBalanceException() throws CustomException {
		Account account = new Account(5000, 4, BigDecimal.valueOf(600), "EUR");
		account.setDeltaAmount(BigDecimal.TEN);
		assertTrue(h2DaoFactory.getAccountDAO().updateAccountBalance(account) == 0);
	}

	@Test
	public void testDeleteAccountById() throws CustomException {
		int rowCount = h2DaoFactory.getAccountDAO().deleteAccountById(4000);
		assertTrue(rowCount == 1);
		assertTrue(h2DaoFactory.getAccountDAO().getAccountById(4000) == null);
	}

	@Test
	public void testDeleteNonExistingAccount() throws CustomException {
		int rowCount = h2DaoFactory.getAccountDAO().deleteAccountById(500000);
		assertTrue(rowCount == 0);
	}

}
