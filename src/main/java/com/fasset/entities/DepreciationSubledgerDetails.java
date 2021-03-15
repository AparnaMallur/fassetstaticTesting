
package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity

@Table(name = "DepreciationSubledgerDetails")
public class DepreciationSubledgerDetails {

	@Id
	@GeneratedValue
	@Column(name = "depreciationsubledgerdetails_id")
	private Long depreciationsubledgerdetails_id;

	@Column(name = "subLedgerAmount")
	private Double subLedgerAmount;

	@ManyToOne(fetch = FetchType.EAGER)

	@JoinColumn(name = "depreciation_id")
	private DepreciationAutoJV depreciationAutoJV;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subledger")
	private SubLedger subledger;

	public Long getDepreciationsubledgerdetails_id() {
		return depreciationsubledgerdetails_id;
	}

	public void setDepreciationsubledgerdetails_id(Long depreciationsubledgerdetails_id) {
		this.depreciationsubledgerdetails_id = depreciationsubledgerdetails_id;
	}

	public Double getSubLedgerAmount() {
		return subLedgerAmount;
	}

	public void setSubLedgerAmount(Double subLedgerAmount) {
		this.subLedgerAmount = subLedgerAmount;
	}

	public DepreciationAutoJV getDepreciationAutoJV() {
		return depreciationAutoJV;
	}

	public void setDepreciationAutoJV(DepreciationAutoJV depreciationAutoJV) {
		this.depreciationAutoJV = depreciationAutoJV;
	}

	public SubLedger getSubledger() {
		return subledger;
	}

	public void setSubledger(SubLedger subledger) {
		this.subledger = subledger;
	}

}