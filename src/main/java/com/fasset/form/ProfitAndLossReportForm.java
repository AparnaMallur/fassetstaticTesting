package com.fasset.form;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.YearEndJV;

public class ProfitAndLossReportForm {

	private YearEndJV yearEnd;
	private Long companyId;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company ;
	private Long option ;
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesOfsubledger;
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesbeforestartDate;
	public YearEndJV getYearEnd() {
		return yearEnd;
	}
	public void setYearEnd(YearEndJV yearEnd) {
		this.yearEnd = yearEnd;
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
	public List<OpeningBalancesOfSubedgerForm> getListForOpeningbalancesbeforestartDate() {
		return ListForOpeningbalancesbeforestartDate;
	}
	public void setListForOpeningbalancesbeforestartDate(
			List<OpeningBalancesOfSubedgerForm> listForOpeningbalancesbeforestartDate) {
		ListForOpeningbalancesbeforestartDate = listForOpeningbalancesbeforestartDate;
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
	public List<OpeningBalancesOfSubedgerForm> getListForOpeningbalancesOfsubledger() {
		return ListForOpeningbalancesOfsubledger;
	}
	public void setListForOpeningbalancesOfsubledger(
			List<OpeningBalancesOfSubedgerForm> listForOpeningbalancesOfsubledger) {
		ListForOpeningbalancesOfsubledger = listForOpeningbalancesOfsubledger;
	}
}
