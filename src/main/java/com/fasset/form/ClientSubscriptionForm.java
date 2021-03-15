package com.fasset.form;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;

public class ClientSubscriptionForm {
	private Company company;
	private Float amount;
	private Integer payment_mode;
	private LocalDate subscription_from;
	private LocalDate subscription_to;
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public Integer getPayment_mode() {
		return payment_mode;
	}
	public void setPayment_mode(Integer payment_mode) {
		this.payment_mode = payment_mode;
	}
	public LocalDate getSubscription_from() {
		return subscription_from;
	}
	public void setSubscription_from(LocalDate subscription_from) {
		this.subscription_from = subscription_from;
	}
	public LocalDate getSubscription_to() {
		return subscription_to;
	}
	public void setSubscription_to(LocalDate subscription_to) {
		this.subscription_to = subscription_to;
	}

	
}
