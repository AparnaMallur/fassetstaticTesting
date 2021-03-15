package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.joda.time.LocalDate;

import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "voucher_series")
public class VoucherSeries extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "voucher_id")
	private Long voucher_id;
	
	@Column(name = "voucher_date", nullable = true)
	private LocalDate voucher_date ;

	@Column(name = "voucher_no")
	private String voucher_no;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	public LocalDate getVoucher_date() {
		return voucher_date;
	}

	public void setVoucher_date(LocalDate voucher_date) {
		this.voucher_date = voucher_date;
	}

	public String getVoucher_no() {
		return voucher_no;
	}

	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getVoucher_id() {
		return voucher_id;
	}

	public void setVoucher_id(Long voucher_id) {
		this.voucher_id = voucher_id;
	}
}
