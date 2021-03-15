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
import com.fasset.entities.abstracts.AbstractEntity;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name = "account_group_type")
public class AccountGroupType  extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "account_group_id", unique = true, nullable = false)
	private Long account_group_id;
	
	@Column(name = "account_group_name", nullable = true, length = MyAbstractController.SIZE_TWO_HUNDRED)
	private String  account_group_name;

	public String getAccount_group_name() {
		return account_group_name;
	}

	public void setAccount_group_name(String account_group_name) {
		this.account_group_name = account_group_name;
	}

	public Long getAccount_group_id() {
		return account_group_id;
	}

	public void setAccount_group_id(Long account_group_id) {
		this.account_group_id = account_group_id;
	}
}
