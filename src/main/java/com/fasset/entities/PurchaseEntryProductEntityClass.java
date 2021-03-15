/**
 * mayur suramwar
 */
package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Entity
@Table(name = "purchase_Entry_product")
public class PurchaseEntryProductEntityClass {

	@Id
	@GeneratedValue
	@Column(name = "purchase_detail_id", unique = true, nullable = false)
	private Long purchase_detail_id;

	@Column(name = "products_product_id", nullable = false)
	private Long product_id;

	@Column(name = "purchase_Entry_purchase_id", nullable = false)
	private Long purchase_id;

	@Column(name = "product_name")
	private String product_name;

	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "rate")
	private Double rate;

	@Column(name = "discount")
	private Double discount;

	@Column(name = "CGST")
	private Double CGST;

	@Column(name = "IGST")
	private Double IGST;

	@Column(name = "SGST")
	private Double SGST;

	@Column(name = "state_com_tax")
	private Double state_com_tax;

	@Column(name = "labour_charges")
	private Double labour_charges;

	@Column(name = "freight")
	private Double freight;

	@Column(name = "Others")
	private Double Others;

	@Column(name = "UOM")
	private String UOM;

	@Column(name = "HSNCode")
	private String HSNCode;

	@Column(name = "transaction_amount")
	private Double transaction_amount;

	@Column(name = "VAT")
	private Double VAT;

	@Column(name = "VATCST")
	private Double VATCST;

	@Column(name = "Excise")
	private Double Excise;

	@Column(name = "is_gst")
	private Long is_gst;


	@Override
	public String toString() {
		return "PurchaseEntryProductEntityClass [purchase_detail_id=" + purchase_detail_id + ", product_id="
				+ product_id + ", purchase_id=" + purchase_id + ", product_name=" + product_name + ", quantity="
				+ quantity + ", rate=" + rate + ", discount=" + discount + ", CGST=" + CGST + ", IGST=" + IGST
				+ ", SGST=" + SGST + ", state_com_tax=" + state_com_tax + ", labour_charges=" + labour_charges
				+ ", freight=" + freight + ", Others=" + Others + ", UOM=" + UOM + ", HSNCode=" + HSNCode + ", VAT="+ VAT + ", "+ "VATCST="+ VATCST + ", Excise="+ Excise + ", is_gst="+ is_gst +"]";
	}


	public Long getPurchase_detail_id() {
		return purchase_detail_id;
	}


	public void setPurchase_detail_id(Long purchase_detail_id) {
		this.purchase_detail_id = purchase_detail_id;
	}


	public Long getProduct_id() {
		return product_id;
	}


	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}


	public Long getPurchase_id() {
		return purchase_id;
	}


	public void setPurchase_id(Long purchase_id) {
		this.purchase_id = purchase_id;
	}


	public String getProduct_name() {
		return product_name;
	}


	public void setProduct_name(String product_name) {
		this.product_name = product_name;
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


	public Double getCGST() {
		return CGST;
	}


	public void setCGST(Double cGST) {
		CGST = cGST;
	}


	public Double getIGST() {
		return IGST;
	}


	public void setIGST(Double iGST) {
		IGST = iGST;
	}


	public Double getSGST() {
		return SGST;
	}


	public void setSGST(Double sGST) {
		SGST = sGST;
	}


	public Double getState_com_tax() {
		return state_com_tax;
	}


	public void setState_com_tax(Double state_com_tax) {
		this.state_com_tax = state_com_tax;
	}


	public Double getLabour_charges() {
		return labour_charges;
	}


	public void setLabour_charges(Double labour_charges) {
		this.labour_charges = labour_charges;
	}


	public Double getFreight() {
		return freight;
	}


	public void setFreight(Double freight) {
		this.freight = freight;
	}


	public Double getOthers() {
		return Others;
	}


	public void setOthers(Double others) {
		Others = others;
	}


	public String getUOM() {
		return UOM;
	}


	public void setUOM(String uOM) {
		UOM = uOM;
	}


	public String getHSNCode() {
		return HSNCode;
	}


	public void setHSNCode(String hSNCode) {
		HSNCode = hSNCode;
	}


	public Double getTransaction_amount() {
		return transaction_amount;
	}


	public void setTransaction_amount(Double transaction_amount) {
		this.transaction_amount = transaction_amount;
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

}
