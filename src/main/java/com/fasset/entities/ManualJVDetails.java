package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "manualjv_details")
public class ManualJVDetails  extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "detailjv_id", unique = true, nullable = false)
	private Long detailjv_id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subledgerdr_Id")
	private SubLedger subledgerdr;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subledgercr_Id")
	private SubLedger subledgercr;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplierdr_Id")
	private Suppliers supplierdr;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "suppliercr_Id")
	private Suppliers suppliercr;

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customercr_Id")
	private Customer customercr;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customerdr_Id")
	private Customer customerdr;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "journal_id")          
	private ManualJournalVoucher manualjournalvoucher;
	
	@Column(name ="dramount", nullable = true)
	private Double  dramount;
	
	@Column(name ="cramount", nullable = true)
	private Double  cramount;
    
	public SubLedger getSubledgerdr() {
		return subledgerdr;
	}

	public void setSubledgerdr(SubLedger subledgerdr) {
		this.subledgerdr = subledgerdr;
	}

	public SubLedger getSubledgercr() {
		return subledgercr;
	}

	public void setSubledgercr(SubLedger subledgercr) {
		this.subledgercr = subledgercr;
	}

	public ManualJournalVoucher getManualjournalvoucher() {
		return manualjournalvoucher;
	}

	
	
	
	public Suppliers getSupplierdr() {
		return supplierdr;
	}

	public void setSupplierdr(Suppliers supplierdr) {
		this.supplierdr = supplierdr;
	}

	public Suppliers getSuppliercr() {
		return suppliercr;
	}

	public void setSuppliercr(Suppliers suppliercr) {
		this.suppliercr = suppliercr;
	}

	public Customer getCustomercr() {
		return customercr;
	}

	public void setCustomercr(Customer customercr) {
		this.customercr = customercr;
	}

	public Customer getCustomerdr() {
		return customerdr;
	}

	public void setCustomerdr(Customer customerdr) {
		this.customerdr = customerdr;
	}

	public void setManualjournalvoucher(ManualJournalVoucher manualjournalvoucher) {
		this.manualjournalvoucher = manualjournalvoucher;
	}

	public Long getDetailjv_id() {
		return detailjv_id;
	}

	public void setDetailjv_id(Long detailjv_id) {
		this.detailjv_id = detailjv_id;
	}

	public Double getDramount() {
		return dramount;
	}

	public void setDramount(Double dramount) {
		this.dramount = dramount;
	}

	public Double getCramount() {
		return cramount;
	}

	public void setCramount(Double cramount) {
		this.cramount = cramount;
	}

}
