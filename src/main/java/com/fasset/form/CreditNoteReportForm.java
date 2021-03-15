package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;

public class CreditNoteReportForm {

	private LocalDate fromDate;
	private LocalDate toDate;
	private Long customerId;
	private Long option;
	private Long subledgerId;
	private Long reportAgainst;
	private Long ledgerId;
	private Long companyId;
	private List<CreditNote> creditNoteList = new ArrayList<CreditNote>();
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
	public Long getOption() {
		return option;
	}
	public void setOption(Long option) {
		this.option = option;
	}
	public Long getSubledgerId() {
		return subledgerId;
	}
	public void setSubledgerId(Long subledgerId) {
		this.subledgerId = subledgerId;
	}
	public Long getReportAgainst() {
		return reportAgainst;
	}
	public void setReportAgainst(Long reportAgainst) {
		this.reportAgainst = reportAgainst;
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
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public List<CreditNote> getCreditNoteList() {
		return creditNoteList;
	}
	public void setCreditNoteList(List<CreditNote> creditNoteList) {
		this.creditNoteList = creditNoteList;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
}
