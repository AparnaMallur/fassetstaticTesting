/**
 * mayur suramwar
 */
package com.fasset.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "client_validation_checklist")
public class ClientValidationChecklist extends AbstractEntity {
	
private static final long serialVersionUID = 1L;

@Id
@GeneratedValue
@Column(name = "checklist_id", unique = true, nullable = false)
private Long checklist_id ;
	
 @Column(name = "checklist_name", nullable = true, length = MyAbstractController.SIZE_FIFTY)
 private String checklist_name ;

 @Column(name = "is_mandatory", nullable = true)
 private Boolean is_mandatory ;
	
 
 @Column(name = "status", nullable = true)
 private Boolean status ;
 
 @Column(name = "created_date", nullable = true)
 private LocalDateTime created_date; 
 
 @Column(name = "created_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
 private Long created_by ; 
 
 @Column(name = "updated_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
 private Long updated_by ; 
 
 @Column(name = "update_date", nullable = true)
 private LocalDateTime update_date;
	
 @Column(name = "from_mobile", nullable = true)
 private Boolean from_mobile ;
 
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(name="company_client_validation_checklist",joinColumns=@JoinColumn(name="checklist_checklist_id",referencedColumnName="checklist_id"),
inverseJoinColumns=@JoinColumn(name="company_company_id",referencedColumnName="company_id"))
private Set<Company> company = new HashSet<Company>();

@Column(name = "ip_address", nullable = true)
private String ip_address ;
 
public Long getChecklist_id() {
	return checklist_id;
}

public void setChecklist_id(Long checklist_id) {
	this.checklist_id = checklist_id;
}

public Set<Company> getCompany() {
	return company;
}

public void setCompany(Set<Company> company) {
	this.company = company;
}

public String getChecklist_name() {
	return checklist_name;
}

public void setChecklist_name(String checklist_name) {
	this.checklist_name = checklist_name;
}

public Boolean getIs_mandatory() {
	return is_mandatory;
}

public void setIs_mandatory(Boolean is_mandatory) {
	this.is_mandatory = is_mandatory;
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

public Long getCreated_by() {
	return created_by;
}

public void setCreated_by(Long created_by) {
	this.created_by = created_by;
}

public Long getUpdated_by() {
	return updated_by;
}

public void setUpdated_by(Long updated_by) {
	this.updated_by = updated_by;
}

public LocalDateTime getUpdate_date() {
	return update_date;
}

public void setUpdate_date(LocalDateTime update_date) {
	this.update_date = update_date;
}

public Boolean getFrom_mobile() {
	return from_mobile;
}

public void setFrom_mobile(Boolean from_mobile) {
	this.from_mobile = from_mobile;
}

public String getIp_address() {
	return ip_address;
}

public void setIp_address(String ip_address) {
	this.ip_address = ip_address;
}	
}
