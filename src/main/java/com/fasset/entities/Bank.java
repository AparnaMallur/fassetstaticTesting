package com.fasset.entities;

import java.util.List;

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
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bank")
public class Bank extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "bank_id")
	private Long bank_id;
		
	@Column(name = "bank_name")
	private String bank_name;
	
	@Column(name = "account_no")
	private Long account_no;
	
	@Column(name = "branch")
	private String branch;
	
	@Column(name = "ifsc_no")
	private String ifsc_no;
	
	@Transient
	private Long company_id ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_sub_group_id")
	@JsonIgnore
	private AccountSubGroup account_sub_group_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	@JsonIgnore
	private Company company ;
	
	@Column(name = "bank_approval", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Integer bank_approval ;
	
	@Transient
	private Long subGroupId;		
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "from_mobile")
	private Boolean from_mobile;		
	
	@Column(name = "created_date")
	private LocalDate created_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	@JsonIgnore
	private User created_by;		
	
	@Column(name = "updated_date")
	private LocalDate updated_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	@JsonIgnore
	private User updated_by;
	
	@Column(name = "ip_address")
	private String ip_address;
	
	@Column(name = "credit_opening_balance")
	private Float credit_opening_balance;

	@Column(name = "debit_opening_balance")
	private Float debit_opening_balance;
	
	@Transient
	private String credit_opening_balance1;
	

	@Transient
	private String debit_opening_balance1;	
	
	@Transient
	private String other_bank_name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "opening_id")
	@JsonIgnore
	private OpeningBalances openingbalances;
	
	@Transient
	private Long openingId ;
	
	@Transient
	private List<String> bankList ;
	
	@Transient
	private Boolean primaryApproval;
	
	@Transient
	private Boolean rejectApproval;
	
	@Column(name = "flag")
	private Boolean flag;
	
	public Long getBank_id() {
		return bank_id;
	}

	public void setBank_id(Long bank_id) {
		this.bank_id = bank_id;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public Long getAccount_no() {
		return account_no;
	}

	public void setAccount_no(Long account_no) {
		this.account_no = account_no;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getIfsc_no() {
		return ifsc_no;
	}

	public void setIfsc_no(String ifsc_no) {
		this.ifsc_no = ifsc_no;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public AccountSubGroup getAccount_sub_group_id() {
		return account_sub_group_id;
	}

	public void setAccount_sub_group_id(AccountSubGroup account_sub_group_id) {
		this.account_sub_group_id = account_sub_group_id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Integer getBank_approval() {
		return bank_approval;
	}

	public void setBank_approval(Integer bank_approval) {
		this.bank_approval = bank_approval;
	}

	public Long getSubGroupId() {
		return subGroupId;
	}

	public void setSubGroupId(Long subGroupId) {
		this.subGroupId = subGroupId;
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

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public User getCreated_by() {
		return created_by;
	}

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
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

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public OpeningBalances getOpeningbalances() {
		return openingbalances;
	}

	public void setOpeningbalances(OpeningBalances openingbalances) {
		this.openingbalances = openingbalances;
	}

	public Long getOpeningId() {
		return openingId;
	}

	public void setOpeningId(Long openingId) {
		this.openingId = openingId;
	}

	public String getOther_bank_name() {
		return other_bank_name;
	}

	public void setOther_bank_name(String other_bank_name) {
		this.other_bank_name = other_bank_name;
	}

	/**
	 * @return the bankList
	 */
	public List<String> getBankList() {
		return bankList;
	}

	/**
	 * @param bankList the bankList to set
	 */
	public void setBankList(List<String> bankList) {
		this.bankList = bankList;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
