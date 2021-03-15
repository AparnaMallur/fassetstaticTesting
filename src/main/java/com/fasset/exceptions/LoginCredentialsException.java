package com.fasset.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class LoginCredentialsException extends BadCredentialsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginCredentialsException(String msg) {
		super(msg);
	}

}
