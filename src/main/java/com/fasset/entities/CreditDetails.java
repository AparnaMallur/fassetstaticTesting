package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author "Vijay"
 *
 */

@Entity
@Table(name = "credit_details")
public class CreditDetails {
	@Id
	@GeneratedValue
	@Column(name = "credit_detail_id")
	private Long credit_detail_id;
	
	@ManyToOne()
	@JoinColumn(name = "credit_id")
	private CreditNote credit_id;
	
	@Transient
	private Long creditId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private Product product_id;

	@Column(name = "product_name")
	private String product_name;
	
	@Column(name = "HSNCode")
	private String HSNCode ;
	
	@Transient
	private Long productId;
	
	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "rate")
	private Double rate;

	@Column(name = "discount")
	private Double discount;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "uom_id")
	private UnitOfMeasurement uom_id;

	@Transient
	private Long uomId;
	
	@Column(name = "cgst")
	private Double cgst;

	@Column(name = "igst")
	private Double igst;

	@Column(name = "sgst")
	private Double sgst;

	@Column(name = "state_com_tax")
	private Double state_com_tax;

	@Column(name = "transaction_amount")
	private Double transaction_amount;

	@Column(name = "labour_charges")
	private Double labour_charges;

	@Column(name = "frieght")
	private Double frieght;

	@Column(name = "others")
	private Double others;

	@Column(name = "VAT")
	private Double VAT ;	
	
	@Column(name = "VATCST")
	private Double VATCST ;	
	
	@Column(name = "Excise")
	private Double Excise ;
	
	@Column(name = "is_gst")
	private Long is_gst ;
	
	
	 
	 @Transient
	 private Float gstRate;
	 
	
	public Long getCredit_detail_id() {
		return credit_detail_id;
	}

	public void setCredit_detail_id(Long credit_detail_id) {
		this.credit_detail_id = credit_detail_id;
	}

	public CreditNote getCredit_id() {
		return credit_id;
	}

	public void setCredit_id(CreditNote credit_id) {
		this.credit_id = credit_id;
	}

	public Product getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Product product_id) {
		this.product_id = product_id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public UnitOfMeasurement getUom_id() {
		return uom_id;
	}

	public void setUom_id(UnitOfMeasurement uom_id) {
		this.uom_id = uom_id;
	}

	public Long getUomId() {
		return uomId;
	}

	public void setUomId(Long uomId) {
		this.uomId = uomId;
	}

	public Double getCgst() {
		return cgst;
	}

	public void setCgst(Double cgst) {
		this.cgst = cgst;
	}

	public Double getIgst() {
		return igst;
	}

	public void setIgst(Double igst) {
		this.igst = igst;
	}

	public Double getSgst() {
		return sgst;
	}

	public void setSgst(Double sgst) {
		this.sgst = sgst;
	}

	public Double getState_com_tax() {
		return state_com_tax;
	}

	public void setState_com_tax(Double state_com_tax) {
		this.state_com_tax = state_com_tax;
	}

	public Double getTransaction_amount() {
		return transaction_amount;
	}

	public void setTransaction_amount(Double transaction_amount) {
		this.transaction_amount = transaction_amount;
	}

	public Double getLabour_charges() {
		return labour_charges;
	}

	public void setLabour_charges(Double labour_charges) {
		this.labour_charges = labour_charges;
	}

	public Double getFrieght() {
		return frieght;
	}

	public void setFrieght(Double frieght) {
		this.frieght = frieght;
	}

	public Double getOthers() {
		return others;
	}

	public void setOthers(Double others) {
		this.others = others;
	}

	public Double getVAT() {
		return VAT;
	}

	public void setVAT(Double vAT) {
		VAT = vAT;
	}

	public Double getVATCST() {
		return VATCST;
	}

	public void setVATCST(Double vATCST) {
		VATCST = vATCST;
	}

	public Double getExcise() {
		return Excise;
	}

	public void setExcise(Double excise) {
		Excise = excise;
	}

	public Long getIs_gst() {
		return is_gst;
	}

	public void setIs_gst(Long is_gst) {
		this.is_gst = is_gst;
	}

	public Long getCreditId() {
		return creditId;
	}

	public void setCreditId(Long creditId) {
		this.creditId = creditId;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getHSNCode() {
		return HSNCode;
	}

	public void setHSNCode(String hSNCode) {
		HSNCode = hSNCode;
	}

	public Float getGstRate() {
		return gstRate;
	}

	public void setGstRate(Float gstRate) {
		this.gstRate = gstRate;
	}


	
}
