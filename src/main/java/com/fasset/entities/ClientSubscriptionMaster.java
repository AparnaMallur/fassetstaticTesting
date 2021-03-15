package com.fasset.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

/**
 * @author vijay ghodake
 *
 *         deven infotech pvt ltd.
 */
@Entity
@Table(name = "client_subscription_master")
public class ClientSubscriptionMaster extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "subscription_id", unique = true, nullable = false)
	private Long subscription_id;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "quotation_id")
	private Quotation quotation_id;
	
	@Transient
	private Long quotationId;

	@Column(name = "subscription_amount")
	private Float subscription_amount;

	@Column(name = "payment_mode")
	private Integer payment_mode;

	@Column(name = "payment_reference_number")
	private String payment_reference_number;

	@Column(name = "subscription_from")
	private LocalDate subscription_from;
	
	@Column(name = "subscription_to")
	private LocalDate subscription_to;

	@Column(name = "payment_date")
	private LocalDate payment_date;

	@Column(name = "status")
	private Boolean status;

	@Column(name = "from_mobile", nullable = true)
	private Boolean from_mobile;

	@Column(name = "created_date", nullable = true)
	private LocalDate created_date;

	@Column(name = "created_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Long created_by;

	@Column(name = "approved_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Long approved_by;

	@Column(name = "approved_date", nullable = true)
	private LocalDate approved_date;

	@Column(name = "ip_address", nullable = true)
	private String ip_address;

	public Long getSubscription_id() {
		return subscription_id;
	}

	public void setSubscription_id(Long subscription_id) {
		this.subscription_id = subscription_id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Float getSubscription_amount() {
		return subscription_amount;
	}

	public void setSubscription_amount(Float subscription_amount) {
		this.subscription_amount = subscription_amount;
	}

	public Integer getPayment_mode() {
		return payment_mode;
	}

	public void setPayment_mode(Integer payment_mode) {
		this.payment_mode = payment_mode;
	}

	public String getPayment_reference_number() {
		return payment_reference_number;
	}

	public void setPayment_reference_number(String payment_reference_number) {
		this.payment_reference_number = payment_reference_number;
	}

	public LocalDate getPayment_date() {
		return payment_date;
	}

	public void setPayment_date(LocalDate payment_date) {
		this.payment_date = payment_date;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getFrom_mobile() {
		return from_mobile;
	}

	public void setFrom_mobile(Boolean from_mobile) {
		this.from_mobile = from_mobile;
	}


	public Long getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Long created_by) {
		this.created_by = created_by;
	}

	public Long getApproved_by() {
		return approved_by;
	}

	public void setApproved_by(Long approved_by) {
		this.approved_by = approved_by;
	}


	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public LocalDate getApproved_date() {
		return approved_date;
	}

	public void setApproved_date(LocalDate approved_date) {
		this.approved_date = approved_date;
	}

	public Quotation getQuotation_id() {
		return quotation_id;
	}

	public void setQuotation_id(Quotation quotation_id) {
		this.quotation_id = quotation_id;
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

	public Long getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(Long quotationId) {
		this.quotationId = quotationId;
	}
	
}