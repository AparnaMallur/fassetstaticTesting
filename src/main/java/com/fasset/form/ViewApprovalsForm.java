package com.fasset.form;

import java.util.List;

import com.fasset.entities.Bank;
import com.fasset.entities.Customer;
import com.fasset.entities.Ledger;
import com.fasset.entities.Product;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;

public class ViewApprovalsForm {

	private String first_name;
	private String last_name;
	private List<Ledger> ledgerList ;
	private List<Bank> bankList ;
	private List<Product> productList ;
	private List<SubLedger> subledgerList ;
	private List<Suppliers> supplierList ;
	private List<Customer> customerList ;
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public List<Ledger> getLedgerList() {
		return ledgerList;
	}
	public void setLedgerList(List<Ledger> ledgerList) {
		this.ledgerList = ledgerList;
	}
	public List<Bank> getBankList() {
		return bankList;
	}
	public void setBankList(List<Bank> bankList) {
		this.bankList = bankList;
	}
	public List<Product> getProductList() {
		return productList;
	}
	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	public List<SubLedger> getSubledgerList() {
		return subledgerList;
	}
	public void setSubledgerList(List<SubLedger> subledgerList) {
		this.subledgerList = subledgerList;
	}
	public List<Suppliers> getSupplierList() {
		return supplierList;
	}
	public void setSupplierList(List<Suppliers> supplierList) {
		this.supplierList = supplierList;
	}
	public List<Customer> getCustomerList() {
		return customerList;
	}
	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}
}
