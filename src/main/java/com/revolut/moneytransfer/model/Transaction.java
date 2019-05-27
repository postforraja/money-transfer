package com.revolut.moneytransfer.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transaction implements Serializable {
	private static final long serialVersionUID = -5259573084545547137L;
	private long transactionId;
	private long sourceAccountId;
	private long destinationAccountId;
	private BigDecimal amount;
	private String currencyCode;
	private String comment;
	private String status;
	private String user;

	public Transaction(long transactionId, long sourceAccountId, long destinationAccountId, BigDecimal amount,
			String currencyCode, String comment, String status) {
		this.transactionId = transactionId;
		this.sourceAccountId = sourceAccountId;
		this.destinationAccountId = destinationAccountId;
		this.amount = amount;
		this.currencyCode = currencyCode;
		this.comment = comment;
		this.status = status;
	}
	
	public Transaction(long sourceAccountId, long destinationAccountId, BigDecimal amount,
			String currencyCode, String comment, String user) {
		this.sourceAccountId = sourceAccountId;
		this.destinationAccountId = destinationAccountId;
		this.amount = amount;
		this.currencyCode = currencyCode;
		this.comment = comment;
		this.user = user;
	}
	
	public Transaction() {
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getSourceAccountId() {
		return sourceAccountId;
	}

	public void setSourceAccountId(long sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}

	public long getDestinationAccountId() {
		return destinationAccountId;
	}

	public void setDestinationAccountId(long destinationAccountId) {
		this.destinationAccountId = destinationAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		StringBuilder transaction = new StringBuilder("transaction[transactionId=");
		transaction.append(Long.toString(transactionId)).append(", sourceAccountId=")
				.append(Long.toString(sourceAccountId));
		transaction.append(", destinationAccountId=").append(Long.toString(destinationAccountId));
		transaction.append(", amount=").append(amount.toString()).append(", currencyCode=").append(currencyCode);
		transaction.append(", comment=").append(comment).append(", status=").append(status);

		return transaction.toString();
	}

}
