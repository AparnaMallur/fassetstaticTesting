package com.fasset.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class LockedCredentialsException extends BadCredentialsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LockedCredentialsException(String msg) {
		super(msg);
	}

}
