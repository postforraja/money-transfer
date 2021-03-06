package com.revolut.moneytransfer.util;

public class ConstantUtil {
	public static final String CONTENT_TYPE = "content-type";
	public static final String CONTENT_TYPE_VALUE = "application/json; charset=utf-8";
	
	public final static String GET_ALL_ACCOUNT = "SELECT ACCOUNT_ID, CLIENT_ID, ACCOUNT_BALANCE, ACCOUNT_CURRENCY_CODE FROM RMT.MT_ACCOUNT";
	public final static String GET_ACCOUNT_BY_ID = "SELECT CLIENT_ID, ACCOUNT_BALANCE, ACCOUNT_CURRENCY_CODE FROM RMT.MT_ACCOUNT WHERE ACCOUNT_ID = ? ";
	public final static String CREATE_ACCOUNT_SEQUENCE = "SELECT RMT.MT_ACCOUNT_SEQ.NEXTVAL AS SEQ_ID FROM DUAL";
	public final static String CREATE_ACCOUNT = "INSERT INTO RMT.MT_ACCOUNT(ACCOUNT_ID, CLIENT_ID, ACCOUNT_BALANCE, ACCOUNT_CURRENCY_CODE, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
	public final static String LOCK_ACCOUNT_BY_ID = "SELECT ACCOUNT_ID, CLIENT_ID, ACCOUNT_BALANCE, ACCOUNT_CURRENCY_CODE FROM RMT.MT_ACCOUNT WHERE ACCOUNT_ID = ? FOR UPDATE";
	public final static String UPDATE_ACCOUNT_BALANCE = "UPDATE RMT.MT_ACCOUNT SET ACCOUNT_BALANCE = ? WHERE ACCOUNT_ID = ?";
	public final static String DELETE_ACCOUNT_BY_ID = "DELETE FROM RMT.MT_ACCOUNT WHERE ACCOUNT_ID = ?";
	public final static String GET_CURRENCY_COUNT = "SELECT COUNT(1) AS CNT FROM RMT.MT_CURRENCY WHERE CURRENCY_ACTIVE = 'Y' AND CURRENCY_CODE = ?";
	public final static String CREATE_TRANSACTION_SEQUENCE = "SELECT RMT.MT_TRANSACTION_SEQ.NEXTVAL AS SEQ_ID FROM DUAL";
	public final static String CREATE_TRANSACTION = "INSERT INTO RMT.MT_TRANSACTION(TRANSACTION_ID, SOURCE_ACCOUNT_ID, DESTINATION_ACCOUNT_ID, AMOUNT, ACCOUNT_CURRENCY_CODE, COMMENT, STATUS, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
	public final static String GET_ALL_TRANSACTION = "SELECT TRANSACTION_ID, SOURCE_ACCOUNT_ID, DESTINATION_ACCOUNT_ID, AMOUNT, ACCOUNT_CURRENCY_CODE, COMMENT, STATUS FROM RMT.MT_TRANSACTION";
}
