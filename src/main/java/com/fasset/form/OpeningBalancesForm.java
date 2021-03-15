package com.fasset.form;

import com.fasset.entities.SubLedger;

public class OpeningBalancesForm {

	private String customerName;
	private String supplierName;
	private String bankName;
	private String ledgerName;
	private String subledgerName;
	private Double debit_balance;
	private Double credit_balance;
	private Long customer_id;
	private Long supplier_id;
	private Long bank_id;
	private Long sub_id;
	private SubLedger subledger;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public String getSubledgerName() {
		return subledgerName;
	}
	public void setSubledgerName(String subledgerName) {
		this.subledgerName = subledgerName;
	}
	public Double getDebit_balance() {
		return debit_balance;
	}
	public void setDebit_balance(Double debit_balance) {
		this.debit_balance = debit_balance;
	}
	public Double getCredit_balance() {
		return credit_balance;
	}
	public void setCredit_balance(Double credit_balance) {
		this.credit_balance = credit_balance;
	}
	public SubLedger getSubledger() {
		return subledger;
	}
	public void setSubledger(SubLedger subledger) {
		this.subledger = subledger;
	}
	public Long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}
	public Long getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(Long supplier_id) {
		this.supplier_id = supplier_id;
	}
	public Long getBank_id() {
		return bank_id;
	}
	public void setBank_id(Long bank_id) {
		this.bank_id = bank_id;
	}
	public Long getSub_id() {
		return sub_id;
	}
	public void setSub_id(Long sub_id) {
		this.sub_id = sub_id;
	}
	@Override
	public String toString() {
		return "OpeningBalancesForm [customerName=" + customerName + ", supplierName=" + supplierName + ", bankName="
				+ bankName + ", ledgerName=" + ledgerName + ", subledgerName=" + subledgerName + ", debit_balance="
				+ debit_balance + ", credit_balance=" + credit_balance + ", customer_id=" + customer_id
				+ ", supplier_id=" + supplier_id + ", bank_id=" + bank_id + ", sub_id=" + sub_id + ", subledger="
				+ subledger + "]";
	}
	
}