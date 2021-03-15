package com.fasset.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.entities.abstracts.AbstractEntity;
import com.fasset.form.OpeningBalancesOfSubedgerForm;

@Entity
@Table(name = "year_end_jV")
public class YearEndJV  extends AbstractEntity
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "year_end_jVId", unique = true, nullable = false)
	private Long year_end_jVId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "date", nullable = true)
	private LocalDate date;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by")
	private User created_by;

	@Column(name = "voucher_no")
	private String voucher_no;

	@Column(name = "ip_address", nullable = true)
	private String ip_address;
	
	@Column(name = "netProfit")
	private Double netProfit;
	
	@Column(name = "netLoss")
	private Double netLoss;
	
	@Transient
	private Long year_id;

	public Long getYear_id() {
		return year_id;
	}

	public void setYear_id(Long year_id) {
		this.year_id = year_id;
	}

	@Transient
	private Long accountYearId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accountingYear;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "yearEndJV" , cascade = CascadeType.ALL)
	private Set<YearEndJvSubledgerDetails> yearEndJvSubledgerDetails = new HashSet<YearEndJvSubledgerDetails>();

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public User getCreated_by() {
		return created_by;
	}

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	public String getVoucher_no() {
		return voucher_no;
	}

	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public Long getAccountYearId() {
		return accountYearId;
	}

	public void setAccountYearId(Long accountYearId) {
		this.accountYearId = accountYearId;
	}

	public AccountingYear getAccountingYear() {
		return accountingYear;
	}

	public void setAccountingYear(AccountingYear accountingYear) {
		this.accountingYear = accountingYear;
	}

	public Set<YearEndJvSubledgerDetails> getYearEndJvSubledgerDetails() {
		return yearEndJvSubledgerDetails;
	}

	public void setYearEndJvSubledgerDetails(Set<YearEndJvSubledgerDetails> yearEndJvSubledgerDetails) {
		this.yearEndJvSubledgerDetails = yearEndJvSubledgerDetails;
	}

	public Double getNetProfit() {
		return netProfit;
	}

	public void setNetProfit(Double netProfit) {
		this.netProfit = netProfit;
	}

	public Double getNetLoss() {
		return netLoss;
	}

	public void setNetLoss(Double netLoss) {
		this.netLoss = netLoss;
	}

	public Long getYear_end_jVId() {
		return year_end_jVId;
	}

	public void setYear_end_jVId(Long year_end_jVId) {
		this.year_end_jVId = year_end_jVId;
	}
	
}
