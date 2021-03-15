package com.fasset.utils;

import java.util.HashMap;

public class JsonResponse {
	private String status = "";
	private String errorMessage = "";
	private HashMap<String, String> flds = new HashMap<String, String>();

	public JsonResponse(String status, String errorMessage) {
		this.status = status;
		this.errorMessage = errorMessage;
	}

	public JsonResponse(String status, String errorMessage, HashMap<String, String> h) {
		this.status = status;
		this.errorMessage = errorMessage;
		this.flds = h;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public HashMap<String, String> getFlds() {
		return flds;
	}

	public void setFlds(HashMap<String, String> flds) {
		this.flds = flds;
	}

	public void addFieldError(String divID, String error) {
		this.flds.put(divID, error);
	}

}
