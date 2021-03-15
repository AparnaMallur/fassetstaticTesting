package com.fasset.form;

import java.util.List;

import org.joda.time.LocalDate;

public class JournalRegisterDetails {

	private LocalDate date;
	private String voucherType;
	private String voucherNumber;
	private Double creditBalance;
	private Double debittBalance;
	private List<SubledgerDetailsForm>subList;
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public Double getCreditBalance() {
		return creditBalance;
	}
	public void setCreditBalance(Double creditBalance) {
		this.creditBalance = creditBalance;
	}
	public Double getDebittBalance() {
		return debittBalance;
	}
	public void setDebittBalance(Double debittBalance) {
		this.debittBalance = debittBalance;
	}
	public List<SubledgerDetailsForm> getSubList() {
		return subList;
	}
	public void setSubList(List<SubledgerDetailsForm> subList) {
		this.subList = subList;
	}
}
