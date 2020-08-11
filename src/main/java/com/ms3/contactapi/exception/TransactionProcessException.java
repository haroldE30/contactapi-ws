package com.ms3.contactapi.exception;


public class TransactionProcessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TransactionProcessException(String message) {
		super(message);
	}
	
}
