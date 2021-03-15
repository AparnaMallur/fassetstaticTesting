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

@Entity
@Table(name = "activity_log")
public class ActivityLog {

	
	@Id
	@GeneratedValue
	@Column(name = "log_id")
	private Long log_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplier_id")
	private Suppliers supplier;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ledger_id")
	private Ledger ledger;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subLedger_id")
	private SubLedger subLedger;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bank_id")
	private Bank bank;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subscription_id")
	private ClientSubscriptionMaster subscription;
	
	@Column(name = "created_date")
	private LocalDate created_date;

	@Column(name = "primary_approval")//2
	private Long primary_approval;
	
	@Column(name = "secondary_approval")//3
	private Long secondary_approval;
	
	@Column(name = "rejection")//1
	private Long rejection;
	
	public Long getLog_id() {
		return log_id;
	}

	public void setLog_id(Long log_id) {
		this.log_id = log_id;
	}
	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Suppliers getSupplier() {
		return supplier;
	}

	public void setSupplier(Suppliers supplier) {
		this.supplier = supplier;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Ledger getLedger() {
		return ledger;
	}

	public void setLedger(Ledger ledger) {
		this.ledger = ledger;
	}

	public SubLedger getSubLedger() {
		return subLedger;
	}

	public void setSubLedger(SubLedger subLedger) {
		this.subLedger = subLedger;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getPrimary_approval() {
		return primary_approval;
	}

	public void setPrimary_approval(Long primary_approval) {
		this.primary_approval = primary_approval;
	}

	public Long getSecondary_approval() {
		return secondary_approval;
	}

	public void setSecondary_approval(Long secondary_approval) {
		this.secondary_approval = secondary_approval;
	}

	public Long getRejection() {
		return rejection;
	}

	public void setRejection(Long rejection) {
		this.rejection = rejection;
	}

	public ClientSubscriptionMaster getSubscription() {
		return subscription;
	}

	public void setSubscription(ClientSubscriptionMaster subscription) {
		this.subscription = subscription;
	}
	
}
