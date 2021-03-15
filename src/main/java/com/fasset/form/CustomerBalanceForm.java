package com.fasset.form;

import com.fasset.entities.Customer;

public class CustomerBalanceForm {

	
	private Double creditBalance;
	private Double debitBalance;
	private Customer customer ;
	
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
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
