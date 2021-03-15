package com.fasset.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class PasswdCredentialsException extends BadCredentialsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PasswdCredentialsException(String msg) {
		super(msg);
	}

}
