package com.revolut.moneytransfer.dao;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.model.Transaction;
import com.revolut.moneytransfer.model.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

public class TransactionDAOTest {
	
	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

	@BeforeClass
	public static void setup() {
		h2DaoFactory.populateTestData();
	}
	
	@Test
	public void testTransferFund() throws CustomException {
		Transaction transaction = new Transaction(1000, 2000, BigDecimal.TEN, "USD", "test transfer", "test");
		assertTrue(h2DaoFactory.getTransferDAO().transferFund(transaction) == 2);
	}
	
	@Test(expected = CustomException.class)
	public void testTransferFundException() throws CustomException {
		Transaction transaction = new Transaction(1000, 2000, BigDecimal.TEN, "IND", "test transfer", "test");
		assertTrue(h2DaoFactory.getTransferDAO().transferFund(transaction) == 0);
	}

	@Test
	public void testCreateTransaction() throws CustomException {
		Transaction transaction = new Transaction(1000, 2000, BigDecimal.TEN, "USD", "test transfer", "test");
		transaction.setStatus(TransactionStatus.COMPLETED.toString());
		h2DaoFactory.getTransferDAO().createTransaction(transaction);
		List<Transaction> transactionList = h2DaoFactory.getTransferDAO().getAllTransactions();
		assertNotNull(transactionList);
		assertTrue(transactionList.size() == 1);
	}

}
