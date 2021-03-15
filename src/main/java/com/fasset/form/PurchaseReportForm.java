package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.PurchaseEntry;

public class PurchaseReportForm {

	private LocalDate fromDate;
	private LocalDate transactionDate;
	private LocalDate toDate;
	private Long subledgerId;
	private Long ledgerId;
	private Long companyId;
	private Long option;
	private List<PurchaseEntry> purchaseEntryList= new ArrayList<PurchaseEntry>();
	private Company company ;
	
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public LocalDate getToDate() {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	public Long getSubledgerId() {
		return subledgerId;
	}
	public void setSubledgerId(Long subledgerId) {
		this.subledgerId = subledgerId;
	}
	public Long getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Long getOption() {
		return option;
	}
	public void setOption(Long option) {
		this.option = option;
	}
	public List<PurchaseEntry> getPurchaseEntryList() {
		return purchaseEntryList;
	}
	public void setPurchaseEntryList(List<PurchaseEntry> purchaseEntryList) {
		this.purchaseEntryList = purchaseEntryList;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
	
}
