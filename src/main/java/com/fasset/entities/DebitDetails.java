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
@Table(name = "debit_details")
public class DebitDetails {

	@Id
	@GeneratedValue
	@Column(name = "debit_detail_id")
	private Long debit_detail_id;
	
	@ManyToOne()
	@JoinColumn(name = "debit_id")
	private DebitNote debit_id;
	
	@Transient
	private Long debitId;

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

	public Long getDebit_detail_id() {
		return debit_detail_id;
	}

	public void setDebit_detail_id(Long debit_detail_id) {
		this.debit_detail_id = debit_detail_id;
	}

	public DebitNote getDebit_id() {
		return debit_id;
	}

	public void setDebit_id(DebitNote debit_id) {
		this.debit_id = debit_id;
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

	public Long getDebitId() {
		return debitId;
	}

	public void setDebitId(Long debitId) {
		this.debitId = debitId;
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

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

}
