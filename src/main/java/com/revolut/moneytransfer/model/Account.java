package com.revolut.moneytransfer.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Account implements Serializable {
	private static final long serialVersionUID = -3284622384873407374L;
	private long accountId;
	private long clientId;
	private BigDecimal balance;
	private BigDecimal deltaAmount;
	private String currencyCode;
	private String user;

	public Account(long accountId, long clientId, BigDecimal balance, String currencyCode) {
		this.accountId = accountId;
		this.clientId = clientId;
		this.balance = balance;
		this.currencyCode = currencyCode;
	}
	
	public Account(long clientId, BigDecimal balance, String currencyCode, String user) {
		this.clientId = clientId;
		this.balance = balance;
		this.currencyCode = currencyCode;
		this.user = user;
	}
	
	public Account() {
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getDeltaAmount() {
		return deltaAmount;
	}

	public void setDeltaAmount(BigDecimal deltaAmount) {
		this.deltaAmount = deltaAmount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		StringBuilder account = new StringBuilder("Account[accountId=");
		account.append(Long.toString(accountId)).append(", clientId=").append(Long.toString(clientId));
		account.append(", balance=").append(balance.toString()).append(", currencyCode=").append(currencyCode)
				.append("]");
		return account.toString();
	}
}
