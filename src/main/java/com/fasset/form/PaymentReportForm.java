package com.fasset.form;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.Payment;

public class PaymentReportForm {

	private LocalDate fromDate;
	private LocalDate toDate;
	private Long subledgerId;
	private Long ledgerId;
	private Long companyId;
	private Long option;
	private List<Payment> paymentList;
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
	public List<Payment> getPaymentList() {
		return paymentList;
	}
	public void setPaymentList(List<Payment> paymentList) {
		this.paymentList = paymentList;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
}
