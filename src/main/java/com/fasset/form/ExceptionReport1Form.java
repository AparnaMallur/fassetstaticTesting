package com.fasset.form;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class ExceptionReport1Form {
	
	private LocalDate Date;
	private LocalTime local_time;
	private String voucher_Number;
	private String voucher_Type;
	private String customer;
	private String suppliers;
	private String paymentType;
	private Double totalAmount;
	public LocalDate getDate() {
		return Date;
	}
	public void setDate(LocalDate date) {
		Date = date;
	}
	public LocalTime getLocal_time() {
		return local_time;
	}
	public void setLocal_time(LocalTime local_time) {
		this.local_time = local_time;
	}
	public String getVoucher_Number() {
		return voucher_Number;
	}
	public void setVoucher_Number(String voucher_Number) {
		this.voucher_Number = voucher_Number;
	}
	public String getVoucher_Type() {
		return voucher_Type;
	}
	public void setVoucher_Type(String voucher_Type) {
		this.voucher_Type = voucher_Type;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getSuppliers() {
		return suppliers;
	}
	public void setSuppliers(String suppliers) {
		this.suppliers = suppliers;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
}
