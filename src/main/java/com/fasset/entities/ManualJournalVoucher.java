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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;


import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "journal_voucher")
public class ManualJournalVoucher extends AbstractEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "journal_id", unique = true, nullable = false)
    private Long journal_id ;
	
	@Column(name = "voucher_no")
	private String voucher_no ;
    
    @Column(name = "date", nullable = true)
	private LocalDate date ;
    
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accounting_year_id;
    
    @ManyToOne(fetch = FetchType.EAGER)
	 @JoinColumn(name = "company_id")
	 private Company company ;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "manualjournalvoucher")
	private Set<ManualJVDetails> mjvdetails = new HashSet<ManualJVDetails>();
     
     @ManyToOne(fetch = FetchType.EAGER)
 	@JoinColumn(name = "created_by")
 	private User created_by;		
 	
 	@ManyToOne(fetch = FetchType.EAGER)
 	@JoinColumn(name = "updated_by")
 	private User updated_by;
 	
 	@Column(name = "created_date", nullable = true)
	private LocalDate created_date;

	@Column(name = "update_date", nullable = true)
	private LocalDate update_date;
    
    @Column(name ="amount", nullable = true)
	private Double amount;
    
    @Column(name = "remark", nullable = true, length = MyAbstractController.SIZE_THOUSAND)
	private String remark ;
    
    @Column(name = "ip_address", nullable = true)
	  private String ip_address ;

    @Column(name = "entry_status")
	 private Integer entry_status;
    
    @Transient
	private Long year_id;
    
    @Transient
  	private Long subledger_id1;
    
    @Transient
  	private Long amount1;
    
    @Transient
  	private String  dr_ledgerlist;
    
    @Transient
  	private String cr_ledgerlist;
    
    @Transient
  	private Long subledger_id2;
    
    @Transient
	private Long company_id ;
    
    @Transient
    private List<ManualJVDetails> detailList ;
    
	public Long getJournal_id() {
		return journal_id;
	}

	public void setJournal_id(Long journal_id) {
		this.journal_id = journal_id;
	}

	public String getVoucher_no() {
		return voucher_no;
	}

	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public AccountingYear getAccounting_year_id() {
		return accounting_year_id;
	}

	public void setAccounting_year_id(AccountingYear accounting_year_id) {
		this.accounting_year_id = accounting_year_id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	

	public Long getYear_id() {
		return year_id;
	}

	public void setYear_id(Long year_id) {
		this.year_id = year_id;
	}

	public Long getSubledger_id1() {
		return subledger_id1;
	}

	public void setSubledger_id1(Long subledger_id1) {
		this.subledger_id1 = subledger_id1;
	}

	public Long getSubledger_id2() {
		return subledger_id2;
	}

	public void setSubledger_id2(Long subledger_id2) {
		this.subledger_id2 = subledger_id2;
	}

	public User getCreated_by() {
		return created_by;
	}

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	public User getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(User updated_by) {
		this.updated_by = updated_by;
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

	public LocalDate getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(LocalDate update_date) {
		this.update_date = update_date;
	}

	

	public String getDr_ledgerlist() {
		return dr_ledgerlist;
	}

	public void setDr_ledgerlist(String dr_ledgerlist) {
		this.dr_ledgerlist = dr_ledgerlist;
	}

	public String getCr_ledgerlist() {
		return cr_ledgerlist;
	}

	public void setCr_ledgerlist(String cr_ledgerlist) {
		this.cr_ledgerlist = cr_ledgerlist;
	}

	public Set<ManualJVDetails> getMjvdetails() {
		return mjvdetails;
	}

	public void setMjvdetails(Set<ManualJVDetails> mjvdetails) {
		this.mjvdetails = mjvdetails;
	}

	public Integer getEntry_status() {
		return entry_status;
	}

	public void setEntry_status(Integer entry_status) {
		this.entry_status = entry_status;
	}

	public Long getAmount1() {
		return amount1;
	}

	public void setAmount1(Long amount1) {
		this.amount1 = amount1;
	}

	public List<ManualJVDetails> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<ManualJVDetails> detailList) {
		this.detailList = detailList;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	

	
}