package com.fasset.form;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;

public class DebitNoteReportForm {

	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long supplierId;
	private Long subledgerId;
	private Long ledgerId;
	private Long companyId;
	private Long option;
	private Long reportAgainst;
	private List<DebitNote> debitNoteList = new ArrayList<DebitNote>();
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
	public Long getReportAgainst() {
		return reportAgainst;
	}
	public void setReportAgainst(Long reportAgainst) {
		this.reportAgainst = reportAgainst;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public List<DebitNote> getDebitNoteList() {
		return debitNoteList;
	}
	public void setDebitNoteList(List<DebitNote> debitNoteList) {
		this.debitNoteList = debitNoteList;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	
}
