package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.SalesEntry;

public class SalesReportForm {

	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long subledgerId;
	private Long ledgerId;
	private Long companyId;
	private Integer option;
	private List<SalesEntry> salesEntryList = new ArrayList<SalesEntry>();
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
	public Integer getOption() {
		return option;
	}
	public void setOption(Integer option) {
		this.option = option;
	}
	public List<SalesEntry> getSalesEntryList() {
		return salesEntryList;
	}
	public void setSalesEntryList(List<SalesEntry> salesEntryList) {
		this.salesEntryList = salesEntryList;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
}
