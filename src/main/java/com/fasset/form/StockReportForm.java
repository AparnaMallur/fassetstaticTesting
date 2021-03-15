package com.fasset.form;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;

public class StockReportForm {
	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long productId;
	private Long clientId;
	private String productName ;
	private Double quantity;
	private Company company ;
	private Double amount;
	
	private List<StockReportForm> stockList;
	
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
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public List<StockReportForm> getStockList() {
		return stockList;
	}
	public void setStockList(List<StockReportForm> stockList) {
		this.stockList = stockList;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
