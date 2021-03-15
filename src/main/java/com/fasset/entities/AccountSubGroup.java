/**
 * mayur suramwar
 */
package com.fasset.entities;

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
import org.joda.time.LocalDateTime;
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
@Table(name = "account_sub_group_master")
@Transactional
public class AccountSubGroup extends AbstractEntity{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "subgroup_Id", unique = true, nullable = false)
	 private Long subgroup_Id ;
	 
	@Transient
	private Long group_Id ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_Id", referencedColumnName = "group_Id", insertable = true, updatable = true)
	@JsonIgnore
	private AccountGroup accountGroup; 
	
	 @Column(name = "subgroup_name", nullable = true ,length = MyAbstractController.SIZE_THREE_HUNDRED)
	 private String subgroup_name ;
	 
	 @Column(name = "from_mobile", nullable = true)
	 private Boolean from_mobile ;
	 
	  @Column(name = "status", nullable = true)
	  private Boolean status ;
	  
	  @Column(name = "created_date", nullable = true)
	  private LocalDateTime created_date; 
	  
	  
	  
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "created_by")
		@JsonIgnore
		private User created_by;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "updated_by")
		@JsonIgnore
		private User updated_by;
		
	  @Column(name = "update_date", nullable = true)
	  private LocalDateTime  update_date;
	  
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = true, updatable = true)
		@JsonIgnore
		private Company company ;
		
		@OneToMany(fetch = FetchType.EAGER, mappedBy = "accsubgroup")
		@JsonIgnore
		private Set<Ledger> ledger ;
		
		@Column(name = "ip_address", nullable = true)
		private String ip_address ;

	public Long getGroup_Id() {
		return group_Id;
	}

	public void setGroup_Id(Long group_Id) {
		this.group_Id = group_Id;
	}

	public Long getSubgroup_Id() {
		return subgroup_Id;
	}

	public void setSubgroup_Id(Long subgroup_Id) {
		this.subgroup_Id = subgroup_Id;
	}

	public AccountGroup getAccountGroup() {
		return accountGroup;
	}

	public void setAccountGroup(AccountGroup accountGroup) {
		this.accountGroup = accountGroup;
	}

	public String getSubgroup_name() {
		return subgroup_name;
	}

	public void setSubgroup_name(String subgroup_name) {
		this.subgroup_name = subgroup_name;
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

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public LocalDateTime getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(LocalDateTime update_date) {
		this.update_date = update_date;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<Ledger> getLedger() {
		return ledger;
	}

	public void setLedger(Set<Ledger> ledger) {
		this.ledger = ledger;
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

	
}
