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
import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "year_ending")
public class YearEnding extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue
	@Column(name = "year_ending_id")
	private Long year_ending_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accountingYear;
	
	@Column(name = "fromDate", nullable = true)
	private LocalDate fromDate;
	
	@Column(name = "toDate", nullable = true)
	private LocalDate toDate;
	
	@Column(name = "YearEndingstatus", nullable = true)
	private Long yearEndingstatus;
	
	@Column(name = "isMailSent", nullable = true)
	private Boolean isMailSent;
	
	@Column(name = "isApprovedForEditingAccYr", nullable = true)
	private Boolean isApprovedForEditingAccYr;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public AccountingYear getAccountingYear() {
		return accountingYear;
	}

	public void setAccountingYear(AccountingYear accountingYear) {
		this.accountingYear = accountingYear;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public Long getYear_ending_id() {
		return year_ending_id;
	}

	public void setYear_ending_id(Long year_ending_id) {
		this.year_ending_id = year_ending_id;
	}

	public Long getYearEndingstatus() {
		return yearEndingstatus;
	}

	public void setYearEndingstatus(Long yearEndingstatus) {
		this.yearEndingstatus = yearEndingstatus;
	}

	public Boolean getIsMailSent() {
		return isMailSent;
	}

	public void setIsMailSent(Boolean isMailSent) {
		this.isMailSent = isMailSent;
	}

	public Boolean getIsApprovedForEditingAccYr() {
		return isApprovedForEditingAccYr;
	}

	public void setIsApprovedForEditingAccYr(Boolean isApprovedForEditingAccYr) {
		this.isApprovedForEditingAccYr = isApprovedForEditingAccYr;
	}

}
