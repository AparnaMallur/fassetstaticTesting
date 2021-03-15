package com.fasset.form;

import com.fasset.entities.Bank;

public class BankBalanceForm {

	
	private Double creditBalance;
	private Double debitBalance;
	private Bank bank ;
	
	
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
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
