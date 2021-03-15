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
@Table(name = "tax_master")
public class TaxMaster extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "tax_id", unique = true, nullable = false)
    private Long tax_id ;
	
	@Column(name = "tax_name", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private String tax_name;
	
	@Column(name = "vat", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Float vat;
	
	@Column(name = "cst", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Float cst ;
	
	@Column(name = "excise", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Float excise ;
	
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

	@Transient
	private String vat1 ;
  
	@Transient
	private String cst1 ;
  
	@Transient
	private String excise1 ;
  
	@Column(name = "update_date", nullable = true)
	private LocalDateTime update_date;
	
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;
	  
	public String getVat1() {
		return vat1;
	}

	public void setVat1(String vat1) {
		this.vat1 = vat1;
	}

	public String getCst1() {
		return cst1;
	}

	public void setCst1(String cst1) {
		this.cst1 = cst1;
	}

	public String getExcise1() {
		return excise1;
	}

	public void setExcise1(String excise1) {
		this.excise1 = excise1;
	}

	public Long getTax_id() {
		return tax_id;
	}

	public void setTax_id(Long tax_id) {
		this.tax_id = tax_id;
	}

	public Float getVat() {
		return vat;
	}

	public void setVat(Float vat) {
		this.vat = vat;
	}

	public Float getCst() {
		return cst;
	}

	public void setCst(Float cst) {
		this.cst = cst;
	}

	public Float getExcise() {
		return excise;
	}

	public void setExcise(Float excise) {
		this.excise = excise;
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

	public String getTax_name() {
		return tax_name;
	}

	public void setTax_name(String tax_name) {
		this.tax_name = tax_name;
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
