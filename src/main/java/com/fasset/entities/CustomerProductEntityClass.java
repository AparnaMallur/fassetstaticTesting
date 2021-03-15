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
@Table(name = "customer_master_product")
public class CustomerProductEntityClass {

	@Id
	@GeneratedValue
	@Column(name = "customer_product_id", unique = true, nullable = false)
	private Long customer_product_id ;
	
	@Column(name = "product_product_id",nullable = false)
	private Long product_id ;
	
	@Column(name = "customer_master_customer_id",nullable = false)
	private Long customer_id ;

	public Long getCustomer_product_id() {
		return customer_product_id;
	}

	public void setCustomer_product_id(Long customer_product_id) {
		this.customer_product_id = customer_product_id;
	}

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}
}
