package com.fasset.exceptions;

public class MyWebException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private Exception exception;

	public MyWebException(String message) {
		this.message = message;

	}

	public MyWebException(Exception e) {
		this.setException(e);
		this.message = e.getMessage();

	}

	public MyWebException(String message, Exception e) {
		this.setException(e);
		this.setMessage(message);
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
