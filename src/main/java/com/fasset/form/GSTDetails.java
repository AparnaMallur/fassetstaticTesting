package com.fasset.form;

public class GSTDetails {
	private String hsnCode;
	private Double igstRate;
	private Double sgstRate;
	private Double cgstRate;
	private Double cessRate;
	private Double vatRate;
	private Double cstRate;
	private Double exciseRate;
	private Double igstAmount;
	private Double sgstAmount;
	private Double cgstAmount;
	private Double cessAmount;
	private Double vatAmount;
	private Double exciseAmount;
	private Double cstAmount;
	
	public Double getCessRate() {
		return cessRate;
	}
	public void setCessRate(Double cessRate) {
		this.cessRate = cessRate;
	}
	public Double getVatRate() {
		return vatRate;
	}
	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}
	public Double getCessAmount() {
		return cessAmount;
	}
	public void setCessAmount(Double cessAmount) {
		this.cessAmount = cessAmount;
	}
	public Double getVatAmount() {
		return vatAmount;
	}
	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
	}
	private Double taxableAmount;
	
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public Double getIgstRate() {
		return igstRate;
	}
	public void setIgstRate(Double igstRate) {
		this.igstRate = igstRate;
	}
	public Double getSgstRate() {
		return sgstRate;
	}
	public void setSgstRate(Double sgstRate) {
		this.sgstRate = sgstRate;
	}
	public Double getCgstRate() {
		return cgstRate;
	}
	public void setCgstRate(Double cgstRate) {
		this.cgstRate = cgstRate;
	}
	public Double getIgstAmount() {
		return igstAmount;
	}
	public void setIgstAmount(Double igstAmount) {
		this.igstAmount = igstAmount;
	}
	public Double getSgstAmount() {
		return sgstAmount;
	}
	public void setSgstAmount(Double sgstAmount) {
		this.sgstAmount = sgstAmount;
	}
	public Double getCgstAmount() {
		return cgstAmount;
	}
	public void setCgstAmount(Double cgstAmount) {
		this.cgstAmount = cgstAmount;
	}
	public Double getTaxableAmount() {
		return taxableAmount;
	}
	public void setTaxableAmount(Double taxableAmount) {
		this.taxableAmount = taxableAmount;
	}
	public Double getCstRate() {
		return cstRate;
	}
	public void setCstRate(Double cstRate) {
		this.cstRate = cstRate;
	}
	public Double getExciseRate() {
		return exciseRate;
	}
	public void setExciseRate(Double exciseRate) {
		this.exciseRate = exciseRate;
	}
	public Double getExciseAmount() {
		return exciseAmount;
	}
	public void setExciseAmount(Double exciseAmount) {
		this.exciseAmount = exciseAmount;
	}
	public Double getCstAmount() {
		return cstAmount;
	}
	public void setCstAmount(Double cstAmount) {
		this.cstAmount = cstAmount;
	}			
}
