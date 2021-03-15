package com.fasset.form;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.Ledger;

public class CashFlowSummaryForm {

	private Long subgroupId;
	private Long groupId;
	private Long companyId;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long option;
	private List<Ledger> ledgerList ;
	private Company company ;
	
	public Long getSubgroupId() {
		return subgroupId;
	}
	public void setSubgroupId(Long subgroupId) {
		this.subgroupId = subgroupId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
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
	public Long getOption() {
		return option;
	}
	public void setOption(Long option) {
		this.option = option;
	}
	public List<Ledger> getLedgerList() {
		return ledgerList;
	}
	public void setLedgerList(List<Ledger> ledgerList) {
		this.ledgerList = ledgerList;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
}
