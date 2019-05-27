package com.revolut.moneytransfer.dao;

import com.revolut.moneytransfer.exception.CustomException;

public interface CurrencyDAO {
	boolean validateCurrencyCode(String currencyCode) throws CustomException;
}
