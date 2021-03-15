
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

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd..
 * 
 */
@Entity
@Table(name = "state_master")
public class State extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "country_id", referencedColumnName = "country_id", insertable = true, updatable = true)
	private Country cntry;

	@Id
	@GeneratedValue
	@Column(name = "state_id", unique = true, nullable = false)
	private Long state_id;

	@Transient
	private Long country_id;

	@Column(name = "state_name", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private String state_name;

	@Column(name = "status", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean status;
	
	@Column(name = "state_code", nullable = true, length = MyAbstractController.SIZE_TEN, columnDefinition = "int default 0")
    private Long state_code ;

	public Long getState_code() {
		return state_code;
	}

	public void setState_code(Long state_code) {
		this.state_code = state_code;
	}

	@Column(name = "created_date", nullable = true)
	private LocalDateTime created_date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User created_by;		
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	private User updated_by;

	@Column(name = "update_date", nullable = true)
	private LocalDateTime update_date;

	@Column(name = "ip_address", nullable = true)
	private String ip_address;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "state")
	private Set<City> city = new HashSet<City>();

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getCountry_id() {
		return country_id;
	}

	public void setCountry_id(Long country_id) {
		this.country_id = country_id;
	}

	public Country getCntry() {
		return cntry;
	}

	public void setCntry(Country cntry) {
		this.cntry = cntry;
	}

	public Long getState_id() {
		return state_id;
	}

	public void setState_id(Long state_id) {
		this.state_id = state_id;
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
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

	public Set<City> getCity() {
		return city;
	}

	public void setCity(Set<City> city) {
		this.city = city;
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
