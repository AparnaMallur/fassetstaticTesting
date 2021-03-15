package com.fasset.form;

public class SubledgerDetailsForm {

	private Double creditBalance;
	private Double debitBalance;
	private String subledgerName;

	public Double getCreditBalance() {
		return creditBalance;
	}
	public void setCreditBalance(Double creditBalance) {
		this.creditBalance = creditBalance;
	}
	public Double getDebitBalance() {
		return debitBalance;
	}
	public void setDebitBalance(Double debitBalance) {
		this.debitBalance = debitBalance;
	}
	public String getSubledgerName() {
		return subledgerName;
	}
	public void setSubledgerName(String subledgerName) {
		this.subledgerName = subledgerName;
	}
}
