/**
 * mayur suramwar
 */
package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name = "accounting_year")
public class AccountingYear extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "year_id", unique = true, nullable = false)
	private Long year_id ;
	
	@Column(name = "year_range", nullable = true ,length = MyAbstractController.SIZE_THREE_HUNDRED)
	private String year_range ;

	@Column(name = "start_date", nullable = true)
	private LocalDate start_date ;
 
	@Column(name = "end_date", nullable = true)
	private LocalDate end_date ;
 
	@Column(name = "from_mobile", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean from_mobile ;
  
	@Column(name = "status", nullable = true, length = MyAbstractController.SIZE_THIRTY)
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
  
	@Column(name = "update_date")
	private LocalDateTime  update_date;
	
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;

	  
	public String getYear_range() {
		return year_range;
	}

	public void setYear_range(String year_range) {
		this.year_range = year_range;
	}

	public Long getYear_id() {
		return year_id;
	}

	public void setYear_id(Long year_id) {
		this.year_id = year_id;
	}

	

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	public LocalDate getEnd_date() {
		return end_date;
	}

	public void setEnd_date(LocalDate end_date) {
		this.end_date = end_date;
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
