/**
 * mayur suramwar
 */
package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasset.controller.abstracts.MyAbstractController;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name ="supplier_master_subledger_master")
public class SupplierSubledgerEntityClass {

	@Id
	@GeneratedValue
	@Column(name = "supplier_subledger_id", unique = true, nullable = false)
	private Long supplier_subledger_id ;
	
	@Column(name = "subLedgers_subledger_Id",nullable = false)
	private Long sub_ledger_Id ;
	
	@Column(name = "supplier_master_supplier_id",nullable = false)
	private Long supplier_id ;
	
	@Column(name = "nature_of_purpose", nullable = true, length = MyAbstractController.SIZE_THREE_HUNDRED)
	private String nature_of_purpose ;
	
	 @Column(name = "status", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	  private Boolean status ;

	public Long getSupplier_subledger_id() {
		return supplier_subledger_id;
	}

	public void setSupplier_subledger_id(Long supplier_subledger_id) {
		this.supplier_subledger_id = supplier_subledger_id;
	}

	public Long getSub_ledger_Id() {
		return sub_ledger_Id;
	}

	public void setSub_ledger_Id(Long sub_ledger_Id) {
		this.sub_ledger_Id = sub_ledger_Id;
	}

	public Long getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(Long supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getNature_of_purpose() {
		return nature_of_purpose;
	}

	public void setNature_of_purpose(String nature_of_purpose) {
		this.nature_of_purpose = nature_of_purpose;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
}
