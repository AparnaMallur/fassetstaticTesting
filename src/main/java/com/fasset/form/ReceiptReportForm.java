package com.fasset.form;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;

public class ReceiptReportForm {
	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long subledgerId;
	private Long ledgerId;
	private Long companyId;
	private Long option;
	private List<Receipt> receiptList ;
	private Company company ;
	private List<List<Receipt_product_details>> productinfoList;
	
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
	public List<Receipt> getReceiptList() {
		return receiptList;
	}
	public void setReceiptList(List<Receipt> receiptList) {
		this.receiptList = receiptList;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public List<List<Receipt_product_details>> getProductinfoList() {
		return productinfoList;
	}
	public void setProductinfoList(List<List<Receipt_product_details>> productinfoList) {
		this.productinfoList = productinfoList;
	}
}
