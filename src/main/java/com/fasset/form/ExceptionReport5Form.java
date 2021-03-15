package com.fasset.form;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class ExceptionReport5Form {
	private LocalDate date;
	private LocalTime localtime;
	private String voucherNumber;
	private String voucherType;
	private String invocieNumber;
	private String cusSupplierName;
	private Double sgst;
	private Double igst;
	private Double cgst;
	private Double labourcharges;
	private Double freight;
	private Double others;
	private Double invoiceValue;
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public LocalTime getLocaltime() {
		return localtime;
	}
	public void setLocaltime(LocalTime localtime) {
		this.localtime = localtime;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public String getInvocieNumber() {
		return invocieNumber;
	}
	public void setInvocieNumber(String invocieNumber) {
		this.invocieNumber = invocieNumber;
	}
	public String getCusSupplierName() {
		return cusSupplierName;
	}
	public void setCusSupplierName(String cusSupplierName) {
		this.cusSupplierName = cusSupplierName;
	}
	public Double getSgst() {
		return sgst;
	}
	public void setSgst(Double sgst) {
		this.sgst = sgst;
	}
	public Double getIgst() {
		return igst;
	}
	public void setIgst(Double igst) {
		this.igst = igst;
	}
	public Double getCgst() {
		return cgst;
	}
	public void setCgst(Double cgst) {
		this.cgst = cgst;
	}
	public Double getLabourcharges() {
		return labourcharges;
	}
	public void setLabourcharges(Double labourcharges) {
		this.labourcharges = labourcharges;
	}
	public Double getFreight() {
		return freight;
	}
	public void setFreight(Double freight) {
		this.freight = freight;
	}
	public Double getOthers() {
		return others;
	}
	public void setOthers(Double others) {
		this.others = others;
	}
	public Double getInvoiceValue() {
		return invoiceValue;
	}
	public void setInvoiceValue(Double invoiceValue) {
		this.invoiceValue = invoiceValue;
	}
	
}
