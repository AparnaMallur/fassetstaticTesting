/**
 * mayur suramwar
 */
package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name = "supplier_master_product")
public class SupplierProductEntityClass {

	@Id
	@GeneratedValue
	@Column(name = "supplier_product_id", unique = true, nullable = false)
	private Long supplier_product_id ;
	
	@Column(name = "product_product_id",nullable = false)
	private Long product_id ;
	
	@Column(name = "supplier_master_supplier_id",nullable = false)
	private Long supplier_id ;

	public Long getSupplier_product_id() {
		return supplier_product_id;
	}

	public void setSupplier_product_id(Long supplier_product_id) {
		this.supplier_product_id = supplier_product_id;
	}

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public Long getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(Long supplier_id) {
		this.supplier_id = supplier_id;
	}
	
}
