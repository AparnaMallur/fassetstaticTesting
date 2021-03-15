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
@Table(name = "industry_type_subledger_master")
public class IndustrySubLedgerEntityClass {

	@Id
	@GeneratedValue
	@Column(name = "industry_subledger_id", unique = true, nullable = false)
	private Long industry_subledger_id ;
	
	@Column(name = "subLedgers_subledger_Id",nullable = false)
	private Long sub_ledger_Id ;
	
	@Column(name = "industry_type_industry_id",nullable = false)
	private Long industry_id ;

	public Long getIndustry_subledger_id() {
		return industry_subledger_id;
	}

	public void setIndustry_subledger_id(Long industry_subledger_id) {
		this.industry_subledger_id = industry_subledger_id;
	}

	public Long getSub_ledger_Id() {
		return sub_ledger_Id;
	}

	public void setSub_ledger_Id(Long sub_ledger_Id) {
		this.sub_ledger_Id = sub_ledger_Id;
	}

	public Long getIndustry_id() {
		return industry_id;
	}

	public void setIndustry_id(Long industry_id) {
		this.industry_id = industry_id;
	}
	
	
}
