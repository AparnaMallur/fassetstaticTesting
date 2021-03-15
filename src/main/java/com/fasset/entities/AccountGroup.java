/**
 * mayur suramwar
 */
package com.fasset.entities;

import java.util.HashSet;
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
@Table(name = "account_group_master")
@Transactional
public class AccountGroup extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
     
	@Id
	@GeneratedValue
	@Column(name = "group_Id", unique = true, nullable = false)
	private Long group_Id ;
	
	@Transient
	private Long account_group_id;
	
	@Transient
	private Long posting_id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_type")
	@JsonIgnore
	private AccountGroupType grouptype;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "posting_side")
	@JsonIgnore
	private LedgerPostingSide postingSide;
	
	@Column(name = "group_name", nullable = true ,length = MyAbstractController.SIZE_THREE_HUNDRED)
	private String group_name;
	
	@Column(name = "sequence_no", nullable = true)
	private Long sequence_no;

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
	@JoinColumn(name = "company_id")
	@JsonIgnore
	private Company company ;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountGroup")
	@JsonIgnore
	private Set<AccountSubGroup> account_sub_group = new HashSet<AccountSubGroup>();
	
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;

	public Long getGroup_Id() {
		return group_Id;
	}

	public void setGroup_Id(Long group_Id) {
		this.group_Id = group_Id;
	}

	public Long getAccount_group_id() {
		return account_group_id;
	}

	public void setAccount_group_id(Long account_group_id) {
		this.account_group_id = account_group_id;
	}

	public Long getPosting_id() {
		return posting_id;
	}

	public void setPosting_id(Long posting_id) {
		this.posting_id = posting_id;
	}

	public AccountGroupType getGrouptype() {
		return grouptype;
	}

	public void setGrouptype(AccountGroupType grouptype) {
		this.grouptype = grouptype;
	}

	public LedgerPostingSide getPostingSide() {
		return postingSide;
	}

	public void setPostingSide(LedgerPostingSide postingSide) {
		this.postingSide = postingSide;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public Long getSequence_no() {
		return sequence_no;
	}

	public void setSequence_no(Long sequence_no) {
		this.sequence_no = sequence_no;
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

	public Set<AccountSubGroup> getAccount_sub_group() {
		return account_sub_group;
	}
	public void setAccount_sub_group(Set<AccountSubGroup> account_sub_group) {
		this.account_sub_group = account_sub_group;
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
