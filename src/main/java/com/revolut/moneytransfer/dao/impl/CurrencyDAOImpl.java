package com.revolut.moneytransfer.dao.impl;

import com.revolut.moneytransfer.dao.CurrencyDAO;
import com.revolut.moneytransfer.dao.H2DAOFactory;
import com.revolut.moneytransfer.exception.CustomException;
import com.revolut.moneytransfer.util.ConstantUtil;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyDAOImpl implements CurrencyDAO {
	private static Logger logger = Logger.getLogger(CurrencyDAOImpl.class);
	
	@Override
	public boolean validateCurrencyCode(String currencyCode) throws CustomException {
		int count = 0;

		try (Connection connection = H2DAOFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(ConstantUtil.GET_CURRENCY_COUNT)) {

			statement.setString(1, currencyCode);
			
			try(ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					count = resultSet.getInt("CNT");
					
					if (logger.isDebugEnabled())
						logger.debug("validateCurrencyCode() valid currency: " + count);
				}
			}

			return count == 0 ? true : false;
		} catch (SQLException e) {
			throw new CustomException("validateCurrencyCode(): Error reading Currency data", e);
		}
	}
}