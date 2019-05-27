package com.revolut.moneytransfer.dao;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revolut.moneytransfer.exception.CustomException;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class CurrencyDAOTest {
	
	private static final DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

	@BeforeClass
	public static void setup() {
		h2DaoFactory.populateTestData();
	}
	
	@Test
	public void testValidateCurrencyCode() throws CustomException {
		assertFalse(h2DaoFactory.getCurrencyDAO().validateCurrencyCode("USD"));
	}
	
	@Test
	public void testValidateInactiveCurrencyCode() throws CustomException {
		assertTrue(h2DaoFactory.getCurrencyDAO().validateCurrencyCode("IND"));
	}
}
