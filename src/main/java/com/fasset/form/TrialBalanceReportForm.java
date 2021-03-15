package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.Ledger;
import com.fasset.entities.OpeningBalances;

public class TrialBalanceReportForm {

	
	private Long companyId;
	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long acc_yearId;
	private Company company ;
	
	private List<OpeningBalancesForm>  bankList;
	private List<OpeningBalancesForm>  supplierList;
	private List<OpeningBalancesForm>  customerList;
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesOfsubledger;
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesbeforestartDate;
	
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
	
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	public List<OpeningBalancesForm> getBankList() {
		return bankList;
	}
	public void setBankList(List<OpeningBalancesForm> bankList) {
		this.bankList = bankList;
	}
	public List<OpeningBalancesForm> getSupplierList() {
		return supplierList;
	}
	public void setSupplierList(List<OpeningBalancesForm> supplierList) {
		this.supplierList = supplierList;
	}
	public List<OpeningBalancesForm> getCustomerList() {
		return customerList;
	}
	public void setCustomerList(List<OpeningBalancesForm> customerList) {
		this.customerList = customerList;
	}
	public List<OpeningBalancesOfSubedgerForm> getListForOpeningbalancesOfsubledger() {
		return ListForOpeningbalancesOfsubledger;
	}
	public void setListForOpeningbalancesOfsubledger(
			List<OpeningBalancesOfSubedgerForm> listForOpeningbalancesOfsubledger) {
		ListForOpeningbalancesOfsubledger = listForOpeningbalancesOfsubledger;
	}
	public List<OpeningBalancesOfSubedgerForm> getListForOpeningbalancesbeforestartDate() {
		return ListForOpeningbalancesbeforestartDate;
	}
	public void setListForOpeningbalancesbeforestartDate(
			List<OpeningBalancesOfSubedgerForm> listForOpeningbalancesbeforestartDate) {
		ListForOpeningbalancesbeforestartDate = listForOpeningbalancesbeforestartDate;
	}
	public Long getAcc_yearId() {
		return acc_yearId;
	}
	public void setAcc_yearId(Long acc_yearId) {
		this.acc_yearId = acc_yearId;
	}
}