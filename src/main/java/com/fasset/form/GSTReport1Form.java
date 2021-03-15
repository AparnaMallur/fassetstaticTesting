package com.fasset.form;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;

public class GSTReport1Form {
	private List<SalesEntry> b2bList;
	private List<SalesEntry> b2clList;
	private List<SalesEntry> b2csList;
	private List<Receipt> atAdjList;
	private List<CreditNote> cdnrList;
	private List<CreditNote> cdnurList;
	private List<SalesEntry> expList;
	private List<Receipt> atList;
	private List<SalesEntry> cashSalesList;
	private List<HSNReportForm> hsnReportList;
	private List<SalesEntry[]> intraRegisterList;
	private List<SalesEntry[]> intraNonRegisterList;
	private List<SalesEntry[]> interRegisterList ;
	private List<SalesEntry[]>  interNonRegisterList;
	private List<SalesEntry[]> intraRegisterVATList;
	private List<SalesEntry[]> intraNonRegisterVATList ;
	private List<SalesEntry[]>  interRegisterVATList;
	private List<SalesEntry[]> interNonRegisterVATList;
	private List<GSTdocsForm> outwardSupplyList;
	private List<GSTdocsForm> inwardSupplyList;
	private List<GSTdocsForm> creditNoteList;
	private List<GSTdocsForm> debitNoteList;
	private Integer noOfRecipientsForB2bList;
	private Integer noOfRecipientsForCdnrList;
	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long clientId;
	private Long supplierId;
	private Company company;
	private Long companyId;
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public List<SalesEntry> getB2bList() {
		return b2bList;
	}

	public void setB2bList(List<SalesEntry> b2bList) {
		this.b2bList = b2bList;
	}

	public List<SalesEntry> getB2clList() {
		return b2clList;
	}

	public void setB2clList(List<SalesEntry> b2clList) {
		this.b2clList = b2clList;
	}

	public List<SalesEntry> getB2csList() {
		return b2csList;
	}

	public void setB2csList(List<SalesEntry> b2csList) {
		this.b2csList = b2csList;
	}

	public List<Receipt> getAtAdjList() {
		return atAdjList;
	}

	public void setAtAdjList(List<Receipt> atAdjList) {
		this.atAdjList = atAdjList;
	}

	public List<CreditNote> getCdnrList() {
		return cdnrList;
	}

	public void setCdnrList(List<CreditNote> cdnrList) {
		this.cdnrList = cdnrList;
	}

	public List<CreditNote> getCdnurList() {
		return cdnurList;
	}

	public void setCdnurList(List<CreditNote> cdnurList) {
		this.cdnurList = cdnurList;
	}

	public List<SalesEntry> getExpList() {
		return expList;
	}

	public void setExpList(List<SalesEntry> expList) {
		this.expList = expList;
	}

	public List<Receipt> getAtList() {
		return atList;
	}

	public void setAtList(List<Receipt> atList) {
		this.atList = atList;
	}

	public List<SalesEntry> getCashSalesList() {
		return cashSalesList;
	}

	public void setCashSalesList(List<SalesEntry> cashSalesList) {
		this.cashSalesList = cashSalesList;
	}

	public List<HSNReportForm> getHsnReportList() {
		return hsnReportList;
	}

	public void setHsnReportList(List<HSNReportForm> hsnReportList) {
		this.hsnReportList = hsnReportList;
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

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	public List<SalesEntry[]> getIntraRegisterList() {
		return intraRegisterList;
	}
	public void setIntraRegisterList(List<SalesEntry[]> intraRegisterList) {
		this.intraRegisterList = intraRegisterList;
	}
	public List<SalesEntry[]> getIntraNonRegisterList() {
		return intraNonRegisterList;
	}
	public void setIntraNonRegisterList(List<SalesEntry[]> intraNonRegisterList) {
		this.intraNonRegisterList = intraNonRegisterList;
	}
	public List<SalesEntry[]> getInterRegisterList() {
		return interRegisterList;
	}
	public void setInterRegisterList(List<SalesEntry[]> interRegisterList) {
		this.interRegisterList = interRegisterList;
	}
	public List<SalesEntry[]> getInterNonRegisterList() {
		return interNonRegisterList;
	}
	public void setInterNonRegisterList(List<SalesEntry[]> interNonRegisterList) {
		this.interNonRegisterList = interNonRegisterList;
	}
	public List<SalesEntry[]> getIntraRegisterVATList() {
		return intraRegisterVATList;
	}
	public void setIntraRegisterVATList(List<SalesEntry[]> intraRegisterVATList) {
		this.intraRegisterVATList = intraRegisterVATList;
	}
	public List<SalesEntry[]> getIntraNonRegisterVATList() {
		return intraNonRegisterVATList;
	}
	public void setIntraNonRegisterVATList(List<SalesEntry[]> intraNonRegisterVATList) {
		this.intraNonRegisterVATList = intraNonRegisterVATList;
	}
	public List<SalesEntry[]> getInterRegisterVATList() {
		return interRegisterVATList;
	}
	public void setInterRegisterVATList(List<SalesEntry[]> interRegisterVATList) {
		this.interRegisterVATList = interRegisterVATList;
	}
	public List<SalesEntry[]> getInterNonRegisterVATList() {
		return interNonRegisterVATList;
	}
	public void setInterNonRegisterVATList(List<SalesEntry[]> interNonRegisterVATList) {
		this.interNonRegisterVATList = interNonRegisterVATList;
	}
	public List<GSTdocsForm> getOutwardSupplyList() {
		return outwardSupplyList;
	}
	public void setOutwardSupplyList(List<GSTdocsForm> outwardSupplyList) {
		this.outwardSupplyList = outwardSupplyList;
	}
	public List<GSTdocsForm> getInwardSupplyList() {
		return inwardSupplyList;
	}
	public void setInwardSupplyList(List<GSTdocsForm> inwardSupplyList) {
		this.inwardSupplyList = inwardSupplyList;
	}
	public List<GSTdocsForm> getCreditNoteList() {
		return creditNoteList;
	}
	public void setCreditNoteList(List<GSTdocsForm> creditNoteList) {
		this.creditNoteList = creditNoteList;
	}
	public List<GSTdocsForm> getDebitNoteList() {
		return debitNoteList;
	}
	public void setDebitNoteList(List<GSTdocsForm> debitNoteList) {
		this.debitNoteList = debitNoteList;
	}
	public Integer getNoOfRecipientsForB2bList() {
		return noOfRecipientsForB2bList;
	}
	public void setNoOfRecipientsForB2bList(Integer noOfRecipientsForB2bList) {
		this.noOfRecipientsForB2bList = noOfRecipientsForB2bList;
	}
	public Integer getNoOfRecipientsForCdnrList() {
		return noOfRecipientsForCdnrList;
	}
	public void setNoOfRecipientsForCdnrList(Integer noOfRecipientsForCdnrList) {
		this.noOfRecipientsForCdnrList = noOfRecipientsForCdnrList;
	}

}
