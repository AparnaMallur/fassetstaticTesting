/**
 * mayur suramwar
 */
package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name = "service_frequency")
public class ServiceFrequency extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
     
	@Id
	@Column(name = "frequency_id", unique = true, nullable = false)
	private Long frequency_id;
	
	@Column(name = "frequency_name", nullable = true, length = MyAbstractController.SIZE_TWO_HUNDRED)
	private String frequency_name ;

	public Long getFrequency_id() {
		return frequency_id;
	}

	public void setFrequency_id(Long frequency_id) {
		this.frequency_id = frequency_id;
	}

	public String getFrequency_name() {
		return frequency_name;
	}

	public void setFrequency_name(String frequency_name) {
		this.frequency_name = frequency_name;
	}
	
}
