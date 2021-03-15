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
@Table(name = "ledger_posting_side")
public class LedgerPostingSide extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "posting_id", unique = true, nullable = false)
	private Long posting_id;
	
	@Column(name = "posting_title", nullable = true, length = MyAbstractController.SIZE_TWO_HUNDRED)
	private String posting_title ;

	public Long getPosting_id() {
		return posting_id;
	}

	public void setPosting_id(Long posting_id) {
		this.posting_id = posting_id;
	}

	public String getPosting_title() {
		return posting_title;
	}

	public void setPosting_title(String posting_title) {
		this.posting_title = posting_title;
	}
}
