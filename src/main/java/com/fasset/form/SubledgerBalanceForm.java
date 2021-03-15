package com.fasset.form;

import com.fasset.entities.SubLedger;

public class SubledgerBalanceForm {

	
	private Double creditBalance;
	private Double debitBalance;
	private SubLedger subledger;
	
		
	public SubLedger getSubledger() {
		return subledger;
	}
	public void setSubledger(SubLedger subledger) {
		this.subledger = subledger;
	}
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
	 
	
}
