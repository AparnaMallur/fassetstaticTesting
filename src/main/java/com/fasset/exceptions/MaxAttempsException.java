package com.fasset.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class MaxAttempsException extends BadCredentialsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MaxAttempsException(String msg) {
		super(msg);
	}

}
