/**
 * mayur suramwar
 */
package com.fasset.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "service_master")
public class Service extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Long  id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "service_frequency")
	private ServiceFrequency serviceFrequency ;
	
	@Column(name = "service_name", nullable = true ,length = MyAbstractController.SIZE_THREE_HUNDRED)
	private String service_name;
	
	@Column(name = "service_charge", nullable = true, length = MyAbstractController.SIZE_TWO_HUNDRED)
	private String service_charge;
	
	@Transient
	private Long service_frequency;	
	
	@Column(name = "status", nullable = true)
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

	public ServiceFrequency getServiceFrequency() {
		return serviceFrequency;
	}

	public void setServiceFrequency(ServiceFrequency serviceFrequency) {
		this.serviceFrequency = serviceFrequency;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getService_name() {
		return service_name;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public String getService_charge() {
		return service_charge;
	}

	public void setService_charge(String service_charge) {
		this.service_charge = service_charge;
	}

	public Long getService_frequency() {
		return service_frequency;
	}

	public void setService_frequency(Long service_frequency) {
		this.service_frequency = service_frequency;
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

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	public void setUpdated_by(User updated_by) {
		this.updated_by = updated_by;
	}
	
}
