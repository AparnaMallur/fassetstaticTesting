/**
 * mayur suramwar
 */
package com.fasset.entities;

import java.util.List;
import java.util.Set;

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
import org.joda.time.LocalDateTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name = "company_client_validation_checklist")
public class ClientValidationChecklistStatus extends AbstractEntity{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

@Id
@GeneratedValue
@Column(name = "checklist_status_id", unique = true, nullable = false)
private Long checklist_status_id ;

/*@Column(name = "company_company_id",nullable = false)
private long company_id;

@Column(name = "checklist_checklist_id",nullable = false)
private long checklist_id;*/

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "checklist_checklist_id", referencedColumnName = "checklist_id", insertable = true, updatable = true)
private ClientValidationChecklist checklist;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "company_company_id", referencedColumnName = "company_id", insertable = true, updatable = true)
private Company company;

@Transient
private Integer status ;

@Transient
private Long updatedby ;

@Transient
private Integer emplimit ;
 
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

 @Transient
 private List<String> checkList ;
 
 @Transient
 private Set<ClientValidationChecklist> newcheckList ;
 
 @Transient
 private String yearRange;
 
 @Transient
 private LocalDate fromDate;
 
 @Transient
 private LocalDate toDate;
 
 @Column(name = "ip_address", nullable = true)
	private String ip_address ;
 
public Long getChecklist_status_id() {
	return checklist_status_id;
}

public void setChecklist_status_id(Long checklist_status_id) {
	this.checklist_status_id = checklist_status_id;
}

public ClientValidationChecklist getChecklist() {
	return checklist;
}

public void setChecklist(ClientValidationChecklist checklist) {
	this.checklist = checklist;
}

public Company getCompany() {
	return company;
}

public void setCompany(Company company) {
	this.company = company;
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

public List<String> getCheckList() {
	return checkList;
}

public void setCheckList(List<String> checkList) {
	this.checkList = checkList;
}

public Set<ClientValidationChecklist> getNewcheckList() {
	return newcheckList;
}

public void setNewcheckList(Set<ClientValidationChecklist> newcheckList) {
	this.newcheckList = newcheckList;
}


public Integer getStatus() {
	return status;
}

public void setStatus(Integer status) {
	this.status = status;
}

public Integer getEmplimit() {
	return emplimit;
}

public void setEmplimit(Integer emplimit) {
	this.emplimit = emplimit;
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

public String getYearRange() {
	return yearRange;
}

public void setYearRange(String yearRange) {
	this.yearRange = yearRange;
}

public Long getUpdatedby() {
	return updatedby;
}

public void setUpdatedby(Long updatedby) {
	this.updatedby = updatedby;
}

public String getIp_address() {
	return ip_address;
}

public void setIp_address(String ip_address) {
	this.ip_address = ip_address;
}


	
}
