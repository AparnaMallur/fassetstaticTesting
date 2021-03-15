package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;

public class BillsReceivableReportForm {

	private LocalDate fromDate;
	private LocalDate toDate;
	private Long clientId;
	private Long customerId;
	private List<BillsReceivableForm> billsReceivable = new ArrayList<BillsReceivableForm>();
	private Company company ;
	private Long option ;
	private OpeningBalancesOfSubedgerForm ListForOpeningbalancesOfsubledger;
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesbeforestartDate;
	private List<OpeningBalancesForm>subledgerListbeforestartDate;
	private List<OpeningBalancesForm>bankOpeningBalanceList;
	private List<OpeningBalancesForm>supplierOpeningBalanceList;
	private List<OpeningBalancesForm>customerOpeningBalanceList;
	private List<OpeningBalancesForm>supplierOpeningBalanceBeforeStartDate;
	private List<OpeningBalancesForm>customerOpeningBalanceBeforeStartDate;
	private List<OpeningBalancesForm>bankOpeningBalanceBeforeStartDate;
	
	public Long getOption() {
		return option;
	}
	public void setOption(Long option) {
		this.option = option;
	}
	
	
	public List<OpeningBalancesOfSubedgerForm> getListForOpeningbalancesbeforestartDate() {
		return ListForOpeningbalancesbeforestartDate;
	}
	public void setListForOpeningbalancesbeforestartDate(
			List<OpeningBalancesOfSubedgerForm> listForOpeningbalancesbeforestartDate) {
		ListForOpeningbalancesbeforestartDate = listForOpeningbalancesbeforestartDate;
	}
	public List<OpeningBalancesForm> getSubledgerListbeforestartDate() {
		return subledgerListbeforestartDate;
	}
	public void setSubledgerListbeforestartDate(List<OpeningBalancesForm> subledgerListbeforestartDate) {
		this.subledgerListbeforestartDate = subledgerListbeforestartDate;
	}
	public List<OpeningBalancesForm> getBankOpeningBalanceList() {
		return bankOpeningBalanceList;
	}
	public void setBankOpeningBalanceList(List<OpeningBalancesForm> bankOpeningBalanceList) {
		this.bankOpeningBalanceList = bankOpeningBalanceList;
	}
	public List<OpeningBalancesForm> getSupplierOpeningBalanceList() {
		return supplierOpeningBalanceList;
	}
	public void setSupplierOpeningBalanceList(List<OpeningBalancesForm> supplierOpeningBalanceList) {
		this.supplierOpeningBalanceList = supplierOpeningBalanceList;
	}
	public List<OpeningBalancesForm> getCustomerOpeningBalanceList() {
		return customerOpeningBalanceList;
	}
	public void setCustomerOpeningBalanceList(List<OpeningBalancesForm> customerOpeningBalanceList) {
		this.customerOpeningBalanceList = customerOpeningBalanceList;
	}
	public List<OpeningBalancesForm> getSupplierOpeningBalanceBeforeStartDate() {
		return supplierOpeningBalanceBeforeStartDate;
	}
	public void setSupplierOpeningBalanceBeforeStartDate(List<OpeningBalancesForm> supplierOpeningBalanceBeforeStartDate) {
		this.supplierOpeningBalanceBeforeStartDate = supplierOpeningBalanceBeforeStartDate;
	}
	public List<OpeningBalancesForm> getCustomerOpeningBalanceBeforeStartDate() {
		return customerOpeningBalanceBeforeStartDate;
	}
	public void setCustomerOpeningBalanceBeforeStartDate(List<OpeningBalancesForm> customerOpeningBalanceBeforeStartDate) {
		this.customerOpeningBalanceBeforeStartDate = customerOpeningBalanceBeforeStartDate;
	}
	public List<OpeningBalancesForm> getBankOpeningBalanceBeforeStartDate() {
		return bankOpeningBalanceBeforeStartDate;
	}
	public void setBankOpeningBalanceBeforeStartDate(List<OpeningBalancesForm> bankOpeningBalanceBeforeStartDate) {
		this.bankOpeningBalanceBeforeStartDate = bankOpeningBalanceBeforeStartDate;
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
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public List<BillsReceivableForm> getBillsReceivable() {
		return billsReceivable;
	}
	public void setBillsReceivable(List<BillsReceivableForm> billsReceivable) {
		this.billsReceivable = billsReceivable;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public OpeningBalancesOfSubedgerForm getListForOpeningbalancesOfsubledger() {
		return ListForOpeningbalancesOfsubledger;
	}
	public void setListForOpeningbalancesOfsubledger(OpeningBalancesOfSubedgerForm listForOpeningbalancesOfsubledger) {
		ListForOpeningbalancesOfsubledger = listForOpeningbalancesOfsubledger;
	}
}
