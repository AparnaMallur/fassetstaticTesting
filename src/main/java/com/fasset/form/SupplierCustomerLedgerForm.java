package com.fasset.form;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.fasset.entities.Bank;
import com.fasset.entities.Customer;
import com.fasset.entities.Suppliers;

public class SupplierCustomerLedgerForm {
	
	private LocalDate Date;
	private String voucher_Number;
	private String particulars;
	private Customer customer ;
	private Suppliers supplier;
	private String voucher_Type;
	private Double debit;
	private Double credit;
	private LocalTime local_time;
	private String suppCustName;
	private Integer type; // 1 for payment,2 for receipt,3 for purchase,4 for Sales,5 for credit,6 for debit,7 for contra.
	private Integer contratType; 
	private Bank bank ;
	private Bank withdraw_from;	
	private Bank deposite_to;
	private Double transferAmount;
	public Double getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(Double transferAmount) {
		this.transferAmount = transferAmount;
	}
	public Bank getWithdraw_from() {
		return withdraw_from;
	}
	public void setWithdraw_from(Bank withdraw_from) {
		this.withdraw_from = withdraw_from;
	}
	public Bank getDeposite_to() {
		return deposite_to;
	}
	public void setDeposite_to(Bank deposite_to) {
		this.deposite_to = deposite_to;
	}
	public LocalDate getDate() {
		return Date;
	}
	public void setDate(LocalDate date) {
		Date = date;
	}
	public String getVoucher_Number() {
		return voucher_Number;
	}
	public void setVoucher_Number(String voucher_Number) {
		this.voucher_Number = voucher_Number;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	
	public String getVoucher_Type() {
		return voucher_Type;
	}
	public void setVoucher_Type(String voucher_Type) {
		this.voucher_Type = voucher_Type;
	}
	public Double getDebit() {
		return debit;
	}
	public void setDebit(Double debit) {
		this.debit = debit;
	}
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public LocalTime getLocal_time() {
		return local_time;
	}
	public void setLocal_time(LocalTime local_time) {
		this.local_time = local_time;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Suppliers getSupplier() {
		return supplier;
	}
	public void setSupplier(Suppliers supplier) {
		this.supplier = supplier;
	}
	public String getSuppCustName() {
		return suppCustName;
	}
	public void setSuppCustName(String suppCustName) {
		this.suppCustName = suppCustName;
	}
	public Integer getContratType() {
		return contratType;
	}
	public void setContratType(Integer contratType) {
		this.contratType = contratType;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
}