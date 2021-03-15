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

import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "forgot_password_log")
public class ForgotPasswordLog extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "trasaction_id", unique = true, nullable = false)
	private Long trasaction_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "from_mobile")
	private Boolean from_mobile;
	
	@Column(name = "date")
	private LocalDate date;
	
	@Column(name = "ip_address")
	private String ip_address;
	
	@Column(name = "varification_code")
	private String varification_code;
	
	@Column(name = "type")
	private Integer type;

	public Long getTrasaction_id() {
		return trasaction_id;
	}

	public void setTrasaction_id(Long trasaction_id) {
		this.trasaction_id = trasaction_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getFrom_mobile() {
		return from_mobile;
	}

	public void setFrom_mobile(Boolean from_mobile) {
		this.from_mobile = from_mobile;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getVarification_code() {
		return varification_code;
	}

	public void setVarification_code(String varification_code) {
		this.varification_code = varification_code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}	
	
}
