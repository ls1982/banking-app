package com.revolut.task.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/** Fires when account balance value is too low to perform an operation */
public class InsufficientFundsException extends WebApplicationException {
	private static final String DEFAULT_MESSAGE = "Insufficient funds on account %d";

	public InsufficientFundsException(long accountNumber) {
		this(String.format(DEFAULT_MESSAGE, accountNumber));
	}

	public InsufficientFundsException(String message) {
		super(Response.status(Response.Status.BAD_REQUEST).
				entity(message).type("text/plain").build());
	}

}
