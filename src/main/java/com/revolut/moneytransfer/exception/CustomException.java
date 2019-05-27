package com.revolut.moneytransfer.exception;

public class CustomException extends Exception {
	private static final long serialVersionUID = -7553373586326024033L;

	public CustomException(String msg) {
		super(msg);
	}

	public CustomException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
