
package com.fasset.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.entities.abstracts.AbstractEntity;
import com.fasset.form.ProductInformation;

@Entity

@Table(name = "depreciation_autoJV")
public class DepreciationAutoJV extends AbstractEntity {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id

	@GeneratedValue

	@Column(name = "depreciation_id")
	private Long depreciation_id;

	@ManyToOne(fetch = FetchType.EAGER)

	@JoinColumn(name = "company_id")
	private Company company;

	@Column(name = "date", nullable = true)
	private LocalDate date;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by")
	private User created_by;
	
	@OneToOne(targetEntity=VoucherSeries.class,cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name = "voucher_id",referencedColumnName="voucher_id")
	private VoucherSeries voucherSeries;
	
	@Column(name = "voucher_no")
	private String voucher_no;

	@Column(name = "ip_address", nullable = true)
	private String ip_address;

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

	@Column(name = "subLedgerAmount", nullable = true)
	private Double subLedgerAmount;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accountingYear;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(name = "amount", nullable = true)
	private Double amount;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "depreciationAutoJV", cascade = CascadeType.ALL)
	private Set<DepreciationSubledgerDetails> depriciationSubledgerDetails = new HashSet<DepreciationSubledgerDetails>();

	@Transient
	List<DepreciationSubledgerDetails> depriciationSubledgerDetailsList;

	@Transient
	private Long subledger_id1;

	@Transient
	private LocalDate date1;

	@Transient
	private String dr_ledgerlist;

	public String getDr_ledgerlist() {
		return dr_ledgerlist;
	}

	public void setDr_ledgerlist(String dr_ledgerlist) {
		this.dr_ledgerlist = dr_ledgerlist;
	}

	@Transient
	private Long amount1;

	public Long getDepreciation_id() {
		return depreciation_id;
	}

	public void setDepreciation_id(Long depreciation_id) {
		this.depreciation_id = depreciation_id;
	}

	public Long getSubledger_id1() {
		return subledger_id1;
	}

	public void setSubledger_id1(Long subledger_id1) {
		this.subledger_id1 = subledger_id1;
	}

	public Long getAmount1() {
		return amount1;
	}

	public void setAmount1(Long amount1) {
		this.amount1 = amount1;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getVoucher_no() {
		return voucher_no;
	}

	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
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

	public Set<DepreciationSubledgerDetails> getDepriciationSubledgerDetails() {
		return depriciationSubledgerDetails;
	}

	public void setDepriciationSubledgerDetails(Set<DepreciationSubledgerDetails> depriciationSubledgerDetails) {
		this.depriciationSubledgerDetails = depriciationSubledgerDetails;
	}

	public Double getSubLedgerAmount() {
		return subLedgerAmount;
	}

	public void setSubLedgerAmount(Double subLedgerAmount) {
		this.subLedgerAmount = subLedgerAmount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getDate1() {
		return date1;
	}

	public void setDate1(LocalDate date1) {
		this.date1 = date1;
	}

	public List<DepreciationSubledgerDetails> getDepriciationSubledgerDetailsList() {
		return depriciationSubledgerDetailsList;
	}

	public void setDepriciationSubledgerDetailsList(
			List<DepreciationSubledgerDetails> depriciationSubledgerDetailsList) {
		this.depriciationSubledgerDetailsList = depriciationSubledgerDetailsList;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public User getCreated_by() {
		return created_by;
	}

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	public VoucherSeries getVoucherSeries() {
		return voucherSeries;
	}

	public void setVoucherSeries(VoucherSeries voucherSeries) {
		this.voucherSeries = voucherSeries;
	}

}