package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.entities.Suppliers;

public class BillsReceivableForm {

	private LocalDate created_date;
	private LocalDate supplier_bill_date;
	private String voucher_no ;
	private Customer customer ;
	private Suppliers supplier;
	private Double round_off;
	private List<BillsReceivableForm> billsPayable = new ArrayList<BillsReceivableForm>();
	private Company company ;
	private String particulars ;
	
	public LocalDate getCreated_date() {
		return created_date;
	}
	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}
	public String getVoucher_no() {
		return voucher_no;
	}
	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Double getRound_off() {
		return round_off;
	}
	public void setRound_off(Double round_off) {
		
		this.round_off = round_off;
	}
	public Suppliers getSupplier() {
		return supplier;
	}
	public void setSupplier(Suppliers supplier) {
		this.supplier = supplier;
	}
	public LocalDate getSupplier_bill_date() {
		return supplier_bill_date;
	}
	public void setSupplier_bill_date(LocalDate supplier_bill_date) {
		this.supplier_bill_date = supplier_bill_date;
	}
	public List<BillsReceivableForm> getBillsPayable() {
		return billsPayable;
	}
	public void setBillsPayable(List<BillsReceivableForm> billsPayable) {
		this.billsPayable = billsPayable;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	
}
