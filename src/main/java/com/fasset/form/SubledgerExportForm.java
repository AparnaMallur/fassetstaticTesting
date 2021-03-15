package com.fasset.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasset.entities.Bank;
import com.fasset.entities.Customer;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;

public class SubledgerExportForm {

	
	private List<SubLedger> subledgerList = new ArrayList<>();
	private List<Bank> bankList  =  new ArrayList<>() ;
	private List<Suppliers> supplierList = new ArrayList<>() ;
	private List<Customer> customerList = new ArrayList<>() ;
	private Set<SubLedger> subLedgerListIndustry = new HashSet<>();
	
	private Integer opening_flag;
	
	public List<SubLedger> getSubledgerList() {
		return subledgerList;
	}
	public void setSubledgerList(List<SubLedger> subledgerList) {
		this.subledgerList = subledgerList;
	}
	public List<Bank> getBankList() {
		return bankList;
	}
	public void setBankList(List<Bank> bankList) {
		this.bankList = bankList;
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
	public Set<SubLedger> getSubLedgerListIndustry() {
		return subLedgerListIndustry;
	}
	public void setSubLedgerListIndustry(Set<SubLedger> subLedgerListIndustry) {
		this.subLedgerListIndustry = subLedgerListIndustry;
	}
	public Integer getOpening_flag() {
		return opening_flag;
	}
	public void setOpening_flag(Integer opening_flag) {
		this.opening_flag = opening_flag;
	}
	
	
	
}
