package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.Contra;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;

public class LedgerReportForm {

	private LocalDate fromDate;
	private LocalDate toDate;
	private Long ledgerId;
	private Long subledgerId;
	private Long companyId;
	private Long customerId;
	private Long supplierId;
	private Long bankId;
	private Long reportAgainst;
	private List<LedgerReport> ledgerReport;
	private List<LedgerReport> ledgerReport1;
	private Company company ;
	private List<SubLedger> subledgerList;
	private List<Contra> contraList ;
	private List<OpeningBalances>  openingBalancesList;
	
	private List<Receipt> receiptList;
    private List<Receipt> receiptListForOpenBalance;
	private List<Payment> paymenttList;
	private List<Payment> paymentListForOpenBalance;
	
	List<PurchaseEntry> purchaseEntries;
	List<SalesEntry> salesEntries;
	List<DebitNote> debitNotes;
	List<CreditNote> creditNotes;
	
	
	
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
	public Long getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}
	public Long getSubledgerId() {
		return subledgerId;
	}
	public void setSubledgerId(Long subledgerId) {
		this.subledgerId = subledgerId;
	}
	public List<PurchaseEntry> getPurchaseEntries() {
		return purchaseEntries;
	}
	public void setPurchaseEntries(List<PurchaseEntry> purchaseEntries) {
		this.purchaseEntries = purchaseEntries;
	}
	public List<SalesEntry> getSalesEntries() {
		return salesEntries;
	}
	public void setSalesEntries(List<SalesEntry> salesEntries) {
		this.salesEntries = salesEntries;
	}
	public List<DebitNote> getDebitNotes() {
		return debitNotes;
	}
	public void setDebitNotes(List<DebitNote> debitNotes) {
		this.debitNotes = debitNotes;
	}
	public List<CreditNote> getCreditNotes() {
		return creditNotes;
	}
	public void setCreditNotes(List<CreditNote> creditNotes) {
		this.creditNotes = creditNotes;
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
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public Long getBankId() {
		return bankId;
	}
	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
	public Long getReportAgainst() {
		return reportAgainst;
	}
	public void setReportAgainst(Long reportAgainst) {
		this.reportAgainst = reportAgainst;
	}
	public List<LedgerReport> getLedgerReport() {
		return ledgerReport;
	}
	public void setLedgerReport(List<LedgerReport> ledgerReport) {
		this.ledgerReport = ledgerReport;
	}
	public List<SubLedger> getSubledgerList() {
		return subledgerList;
	}
	public void setSubledgerList(List<SubLedger> subledgerList) {
		this.subledgerList = subledgerList;
	}
	public List<Contra> getContraList() {
		return contraList;
	}
	public void setContraList(List<Contra> contraList) {
		this.contraList = contraList;
	}
	public List<OpeningBalances> getOpeningBalancesList() {
		return openingBalancesList;
	}
	public void setOpeningBalancesList(List<OpeningBalances> openingBalancesList) {
		this.openingBalancesList = openingBalancesList;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public List<LedgerReport> getLedgerReport1() {
		return ledgerReport1;
	}
	public void setLedgerReport1(List<LedgerReport> ledgerReport1) {
		this.ledgerReport1 = ledgerReport1;
	}
	public List<Receipt> getReceiptList() {
		return receiptList;
	}
	public void setReceiptList(List<Receipt> receiptList) {
		this.receiptList = receiptList;
	}
	public List<Receipt> getReceiptListForOpenBalance() {
		return receiptListForOpenBalance;
	}
	public void setReceiptListForOpenBalance(List<Receipt> receiptListForOpenBalance) {
		this.receiptListForOpenBalance = receiptListForOpenBalance;
	}
	public List<Payment> getPaymenttList() {
		return paymenttList;
	}
	public void setPaymenttList(List<Payment> paymenttList) {
		this.paymenttList = paymenttList;
	}
	public List<Payment> getPaymentListForOpenBalance() {
		return paymentListForOpenBalance;
	}
	public void setPaymentListForOpenBalance(List<Payment> paymentListForOpenBalance) {
		this.paymentListForOpenBalance = paymentListForOpenBalance;
	}
	
}
