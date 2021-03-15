/**
 * mayur suramwar
 */
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDateTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name = "industry_type")
public class IndustryType extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "industry_id", unique = true, nullable = false)
	private Long industry_id;
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Set<SubLedger> subLedgers = new HashSet<SubLedger>();
	
	@Transient
	private List<String> subLedgerList ;

	@Column(name = "industry_name", nullable = true)
	private String industry_name;
	
	@Column(name = "status", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean status ;
	  
	@Column(name = "created_date", nullable = true)
	private LocalDateTime created_date; 
	  
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User created_by;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	private User updated_by;
	  
	@Column(name = "update_date", nullable = true)
	private LocalDateTime  update_date;
	  
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;

	@Override
	public String toString() {
		return "IndustryType [industry_id=" + industry_id + ", industry_name=" + industry_name + "]";
	}

	public Set<SubLedger> getSubLedgers() {
		return subLedgers;
	}

	public void setSubLedgers(Set<SubLedger> subLedgers) {
		this.subLedgers = subLedgers;
	}

	public Long getIndustry_id() {
		return industry_id;
	}

	public void setIndustry_id(Long industry_id) {
		this.industry_id = industry_id;
	}

	public String getIndustry_name() {
		return industry_name;
	}

	public void setIndustry_name(String industry_name) {
		this.industry_name = industry_name;
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

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public List<String> getSubLedgerList() {
		return subLedgerList;
	}

	public void setSubLedgerList(List<String> subLedgerList) {
		this.subLedgerList = subLedgerList;
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
	  

}
