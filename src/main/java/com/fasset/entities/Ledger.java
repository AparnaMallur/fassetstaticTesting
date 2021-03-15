/**
 * mayur suramwar
 */
package com.fasset.entities;

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
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name = "ledger_master")
@Transactional
public class Ledger extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ledger_id", unique = true, nullable = false)
    private Long ledger_id ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "acc_sub_group_Id")
	@JsonIgnore
	private AccountSubGroup accsubgroup ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	@JsonIgnore
	private Company company ;
	
	@Transient
	private Long company_id ;
	
	@Transient
	private Long subgroup_Id;
	
	@Transient
	private List<String> ledgerList ;
	
	@Transient
	private Boolean primaryApproval;
	
	@Transient
	private Boolean rejectApproval;
	
	@Column(name = "ledger_name", nullable = true, length = MyAbstractController.SIZE_THREE_HUNDRED)
	 private String ledger_name ;  
	
	@Column(name = "credit_opening_balance", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Float credit_opening_balance;
	
	@Column(name = "debit_opening_balance", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Float debit_opening_balance;
	
	@Transient
	private String credit_opening_balance1;
	
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;
	
	public OpeningBalances getOpeningbalances() {
		return openingbalances;
	}


	public void setOpeningbalances(OpeningBalances openingbalances) {
		this.openingbalances = openingbalances;
	}


	@Transient
	private String debit_opening_balance1;
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "ledger")
	@JsonIgnore
	private Set<SubLedger> subLedger ;

	@Column(name = "as_subledger", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean as_subledger;

	@Column(name = "from_mobile", nullable = true)
	private Boolean from_mobile ;
 
	@Column(name = "status", nullable = true)
	private Boolean status ;
  
	@Column(name = "created_date", nullable = true)
	private LocalDate created_date; 
  
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	@JsonIgnore
	private User created_by;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	@JsonIgnore
	private User updated_by;
  
    @Column(name = "update_date", nullable = true)
    private LocalDate  update_date;
  
    @Column(name = "ledger_approval", nullable = true, length = MyAbstractController.SIZE_TWENTY)
    private Integer ledger_approval ;

	@Column(name = "flag")
	private Boolean flag;
	
	@Column(name = "allocated", nullable = true)
	private Boolean allocated ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "opening_id", referencedColumnName = "opening_id", insertable = true, updatable = true)
	@JsonIgnore
	private OpeningBalances openingbalances;
	
	@Transient
	private Long openingId ;
	
	public Long getOpeningId() {
		return openingId;
	}


	public void setOpeningId(Long openingId) {
		this.openingId = openingId;
	}


	public Long getLedger_id() {
		return ledger_id;
	}

	
	public void setLedger_id(Long ledger_id) {
		this.ledger_id = ledger_id;
	}

	public AccountSubGroup getAccsubgroup() {
		return accsubgroup;
	}

	public void setAccsubgroup(AccountSubGroup accsubgroup) {
		this.accsubgroup = accsubgroup;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public Long getSubgroup_Id() {
		return subgroup_Id;
	}

	public void setSubgroup_Id(Long subgroup_Id) {
		this.subgroup_Id = subgroup_Id;
	}

	public String getLedger_name() {
		return ledger_name;
	}

	public void setLedger_name(String ledger_name) {
		this.ledger_name = ledger_name;
	}

	public Float getCredit_opening_balance() {
		return credit_opening_balance;
	}

	public void setCredit_opening_balance(Float credit_opening_balance) {
		this.credit_opening_balance = credit_opening_balance;
	}

	public Float getDebit_opening_balance() {
		return debit_opening_balance;
	}

	public void setDebit_opening_balance(Float debit_opening_balance) {
		this.debit_opening_balance = debit_opening_balance;
	}

	public String getCredit_opening_balance1() {
		return credit_opening_balance1;
	}

	public void setCredit_opening_balance1(String credit_opening_balance1) {
		this.credit_opening_balance1 = credit_opening_balance1;
	}

	public String getDebit_opening_balance1() {
		return debit_opening_balance1;
	}

	public void setDebit_opening_balance1(String debit_opening_balance1) {
		this.debit_opening_balance1 = debit_opening_balance1;
	}

	public Set<SubLedger> getSubLedger() {
		return subLedger;
	}

	public void setSubLedger(Set<SubLedger> subLedger) {
		this.subLedger = subLedger;
	}

	public Boolean getAs_subledger() {
		return as_subledger;
	}

	public void setAs_subledger(Boolean as_subledger) {
		this.as_subledger = as_subledger;
	}

	public Boolean getFrom_mobile() {
		return from_mobile;
	}

	public void setFrom_mobile(Boolean from_mobile) {
		this.from_mobile = from_mobile;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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

	public Integer getLedger_approval() {
		return ledger_approval;
	}

	public void setLedger_approval(Integer ledger_approval) {
		this.ledger_approval = ledger_approval;
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

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}


	public Boolean getAllocated() {
		return allocated;
	}


	public void setAllocated(Boolean allocated) {
		this.allocated = allocated;
	}


	/**
	 * @return the ledgerList
	 */
	public List<String> getLedgerList() {
		return ledgerList;
	}


	/**
	 * @param ledgerList the ledgerList to set
	 */
	public void setLedgerList(List<String> ledgerList) {
		this.ledgerList = ledgerList;
	}


	/**
	 * @return the primaryApproval
	 */
	public Boolean getPrimaryApproval() {
		return primaryApproval;
	}


	/**
	 * @param primaryApproval the primaryApproval to set
	 */
	public void setPrimaryApproval(Boolean primaryApproval) {
		this.primaryApproval = primaryApproval;
	}


	/**
	 * @return the rejectApproval
	 */
	public Boolean getRejectApproval() {
		return rejectApproval;
	}


	/**
	 * @param rejectApproval the rejectApproval to set
	 */
	public void setRejectApproval(Boolean rejectApproval) {
		this.rejectApproval = rejectApproval;
	}


	public String getIp_address() {
		return ip_address;
	}


	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	
}