package com.fasset.form;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.fasset.entities.Company;
import com.fasset.entities.SubLedger;

public class DayBookReportForm {
	private LocalDate fromDate;
	private LocalDate toDate;
	
	private Long companyId;
	private Company company;
	private LocalDate Date;
	private LocalTime local_time;
	private String voucher_Number;
	private String voucher_Type;
	private String particulars;
	private Double debit;
	private Double credit;
	private Integer type; // 1 for payment,2 for receipt,3 for purchase,4 for Sales,5 for credit,6 for debit,7 for contra.
	private SubLedger subLedger;
	
	private String withdraw_from;
	private String deposit_to;
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
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
	public LocalDate getDate() {
		return Date;
	}
	public void setDate(LocalDate date) {
		Date = date;
	}
	public LocalTime getLocal_time() {
		return local_time;
	}
	public void setLocal_time(LocalTime local_time) {
		this.local_time = local_time;
	}
	public String getVoucher_Number() {
		return voucher_Number;
	}
	public void setVoucher_Number(String voucher_Number) {
		this.voucher_Number = voucher_Number;
	}
	public String getVoucher_Type() {
		return voucher_Type;
	}
	public void setVoucher_Type(String voucher_Type) {
		this.voucher_Type = voucher_Type;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	public Double getDebit() {
		return debit;
	}
	public void setDebit(Double debit) {
		this.debit = debit;
	}
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getWithdraw_from() {
		return withdraw_from;
	}
	public void setWithdraw_from(String withdraw_from) {
		this.withdraw_from = withdraw_from;
	}
	public String getDeposit_to() {
		return deposit_to;
	}
	public void setDeposit_to(String deposit_to) {
		this.deposit_to = deposit_to;
	}
	public SubLedger getSubLedger() {
		return subLedger;
	}
	public void setSubLedger(SubLedger subLedger) {
		this.subLedger = subLedger;
	}
}
