package com.fasset.entities;

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

import com.fasset.entities.abstracts.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "login_log")
public class LoginLog extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "log_id", unique = true, nullable = false)
	private Long log_id ;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;
	
	@Transient
	private Long user_id;
	
	@Column(name = "type")//(1:Login,2:logout)
	private Integer type;
	
	@Column(name = "created_date", nullable = true)
	private LocalDateTime created_date; 
	
	@Column(name = "login_date", nullable = true) // Added for tracking the Company who's user are not logged in system for  last 3 days should be visible on Dashboard.
	private LocalDate login_date; 
	
	@Column(name = "ip_address")
	private String ip_address;

	public Long getLog_id() {
		return log_id;
	}

	public void setLog_id(Long log_id) {
		this.log_id = log_id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public LocalDate getLogin_date() {
		return login_date;
	}

	public void setLogin_date(LocalDate login_date) {
		this.login_date = login_date;
	}
	
}
