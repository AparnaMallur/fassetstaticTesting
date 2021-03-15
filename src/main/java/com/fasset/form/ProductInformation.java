/**
 * mayur suramwar
 */
package com.fasset.form;

import javax.persistence.Column;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public class ProductInformation {

	private String productId;
	
	private String productname;
	
	private String quantity;
	
	private String unit;
	
	private String hsncode;
	
	private String rate;
	
	private String labourCharge;
	
	private String freightCharges;
	
	private String others;
	
	private String discount;
	
	private String CGST;
	private String SGST;
	private String IGST;
	private String SCT;
	private String transaction_amount;
	private String VAT ;	
	private String VATCST ;		
	private String Excise ;
	private String is_gst ;
	
	public ProductInformation() {
		super();
	}

	public ProductInformation(String productId, String quantity, String unit, String hsncode, String rate,
			String labourCharge, String freightCharges, String others, String cGST, String sGST, String iGST,
			String sCT,String transaction_amount) {
		super();
		this.productId = productId;
		this.quantity = quantity;
		this.unit = unit;
		this.hsncode = hsncode;
		this.rate = rate;
		this.labourCharge = labourCharge;
		this.freightCharges = freightCharges;
		this.others = others;
		this.transaction_amount=transaction_amount;
		CGST = cGST;
		SGST = sGST;
		IGST = iGST;
		SCT = sCT;
		this.is_gst=is_gst;
		this.VAT=VAT;
		this.VATCST=VATCST;
		this.Excise=Excise;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getHsncode() {
		return hsncode;
	}

	public void setHsncode(String hsncode) {
		this.hsncode = hsncode;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getLabourCharge() {
		return labourCharge;
	}

	public void setLabourCharge(String labourCharge) {
		this.labourCharge = labourCharge;
	}

	public String getFreightCharges() {
		return freightCharges;
	}

	public void setFreightCharges(String freightCharges) {
		this.freightCharges = freightCharges;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getCGST() {
		return CGST;
	}

	public void setCGST(String cGST) {
		CGST = cGST;
	}

	public String getSGST() {
		return SGST;
	}

	public void setSGST(String sGST) {
		SGST = sGST;
	}

	public String getIGST() {
		return IGST;
	}

	public void setIGST(String iGST) {
		IGST = iGST;
	}

	public String getSCT() {
		return SCT;
	}

	public void setSCT(String sCT) {
		SCT = sCT;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getVAT() {
		return VAT;
	}

	public void setVAT(String vAT) {
		VAT = vAT;
	}

	public String getVATCST() {
		return VATCST;
	}

	public void setVATCST(String vATCST) {
		VATCST = vATCST;
	}

	public String getExcise() {
		return Excise;
	}

	public void setExcise(String excise) {
		Excise = excise;
	}

	public String getIs_gst() {
		return is_gst;
	}

	public void setIs_gst(String is_gst) {
		this.is_gst = is_gst;
	}

	public String getTransaction_amount() {
		return transaction_amount;
	}

	public void setTransaction_amount(String transaction_amount) {
		this.transaction_amount = transaction_amount;
	}

	@Override
	public String toString() {
		return "ProductInformation [productId=" + productId + ", productname=" + productname + ", quantity=" + quantity
				+ ", unit=" + unit + ", hsncode=" + hsncode + ", rate=" + rate + ", labourCharge=" + labourCharge
				+ ", freightCharges=" + freightCharges + ", others=" + others + ", discount=" + discount + ", CGST="
				+ CGST + ", SGST=" + SGST + ", IGST=" + IGST + ", SCT=" + SCT + ", transaction_amount="
				+ transaction_amount + ", VAT="+ VAT + ", "+ "VATCST="+ VATCST + ", Excise="+ Excise + ", is_gst="+ is_gst +"]";
	}



	
	
	
	
}
