package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.LocalDate;

import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "stock_Info_Of_Product")
public class StockInfoOfProduct extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "stockinfo_id", unique = true, nullable = false)
	private Long stockinfo_id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_id")
	private Stock stock;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
	@Column(name = "product_name")
	private String product_name;
	
	@Column(name = "rate")
	private Double rate;
	
	@Column(name = "quantity")
	private Double quantity;
	
	@Column(name = "quantityDispatch")
	private Double dispatch;
	
	@Column(name = "purchase_date", nullable = true)
	private LocalDate purchase_date;

	@Column(name = "status")
    private Boolean status;
	
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public LocalDate getPurchase_date() {
		return purchase_date;
	}

	public void setPurchase_date(LocalDate purchase_date) {
		this.purchase_date = purchase_date;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getStockinfo_id() {
		return stockinfo_id;
	}

	public void setStockinfo_id(Long stockinfo_id) {
		this.stockinfo_id = stockinfo_id;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Double getDispatch() {
		return dispatch;
	}

	public void setDispatch(Double dispatch) {
		this.dispatch = dispatch;
	}
}
