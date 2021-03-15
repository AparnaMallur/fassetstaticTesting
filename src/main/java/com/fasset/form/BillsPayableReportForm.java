package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;

public class BillsPayableReportForm {

	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long clientId;
	private Long supplierId;
	private List<BillsReceivableForm> billsPayable = new ArrayList<BillsReceivableForm>();
	private Company company ;
	private Long option;
	
	
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
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
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
	public Long getOption() {
		return option;
	}
	public void setOption(Long option) {
		this.option = option;
	}
}
