package com.fasset.form;

import com.fasset.entities.Suppliers;

public class SupplierBalanceForm {

	private Double creditBalance;
	private Double debitBalance;
	private Suppliers Supplier ;
	
	
	public Suppliers getSupplier() {
		return Supplier;
	}
	public void setSupplier(Suppliers supplier) {
		Supplier = supplier;
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
