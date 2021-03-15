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

import org.joda.time.LocalDateTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name = "company_statutory_type")
public class CompanyStatutoryType  extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue
	@Column(name = "company_statutory_id", unique = true, nullable = false)
	private Long company_statutory_id ;
	
	@Column(name = "company_statutory_name", nullable = true)
	private String company_statutory_name;

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
	  private   LocalDateTime  update_date;
	  
	  @Column(name = "ip_address", nullable = true)
	  private String ip_address ;

	public Long getCompany_statutory_id() {
		return company_statutory_id;
	}

	public void setCompany_statutory_id(Long company_statutory_id) {
		this.company_statutory_id = company_statutory_id;
	}

	public String getCompany_statutory_name() {
		return company_statutory_name;
	}

	public void setCompany_statutory_name(String company_statutory_name) {
		this.company_statutory_name = company_statutory_name;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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
