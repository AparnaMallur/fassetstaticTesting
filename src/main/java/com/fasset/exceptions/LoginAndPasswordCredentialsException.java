package com.fasset.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class LoginAndPasswordCredentialsException extends BadCredentialsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BadCredentialsException login, passwd;

	public LoginAndPasswordCredentialsException(String msg) {
		super(msg);
	}

	public boolean isNoLogin() {
		return (null != login);
	}

	public boolean isNoPasswd() {
		return (null != passwd);
	}

	public String getNoLogin() {
		return login.getMessage();
	}

	public void setNoLogin(String mess) {
		this.login = new BadCredentialsException(mess);
	}

	public String getNoPasswd() {
		return passwd.getMessage();
	}

	public void setNoPasswd(String mess) {
		this.passwd = new BadCredentialsException(mess);
	}

}
