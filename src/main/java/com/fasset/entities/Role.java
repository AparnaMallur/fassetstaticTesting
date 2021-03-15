package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.joda.time.LocalDate;

import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "role")
public class Role extends AbstractEntity{
	
	@Id
	@GeneratedValue
	@Column(name = "role_id", unique = true, nullable = false)
	private Long role_id;
	
	@Column(name = "role_name")
	private String role_name;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "from_mobile")
	private Boolean from_mobile;
	
	@Column(name = "created_date")
	private LocalDate created_date;	
	
	@JoinColumn(name = "created_by")
	private User created_by;
	
	@Column(name = "updated_date")
	private LocalDate updated_date;	
	
	@JoinColumn(name = "updated_by")
	private User updated_by;

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
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

}