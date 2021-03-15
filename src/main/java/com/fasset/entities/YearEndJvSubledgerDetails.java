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

@Entity
@Table(name = "year_endJvSubledgerDetails")
public class YearEndJvSubledgerDetails
{
	
	@Id
	@GeneratedValue
	@Column(name = "year_endJvSubledgerDetailsId", unique = true, nullable = false)
	private Long year_endJvSubledgerDetailsId;

	@Column(name = "subLedgerAmount")
	private Double subLedgerAmount;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "year_end_Id")
	private YearEndJV yearEndJV;
	
	@Column(name = "DrSide")
	private Integer DrSide;
	
	@Column(name = "CrSide")
	private Integer CrSide;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subledger")
	private SubLedger subledger;

	@Transient
	private Long subId;
	
	


	public Long getSubId() {
		return subId;
	}

	public void setSubId(Long subId) {
		this.subId = subId;
	}

	public Double getSubLedgerAmount() {
		return subLedgerAmount;
	}

	public void setSubLedgerAmount(Double subLedgerAmount) {
		this.subLedgerAmount = subLedgerAmount;
	}

	public YearEndJV getYearEndJV() {
		return yearEndJV;
	}

	public void setYearEndJV(YearEndJV yearEndJV) {
		this.yearEndJV = yearEndJV;
	}

	public Integer getDrSide() {
		return DrSide;
	}

	public void setDrSide(Integer drSide) {
		DrSide = drSide;
	}

	public Integer getCrSide() {
		if(CrSide==null)
		{
			return new Integer(-1);
		}
		return CrSide;
	}

	public void setCrSide(Integer crSide) {
		CrSide = crSide;
	}

	public SubLedger getSubledger() {
		return subledger;
	}

	public void setSubledger(SubLedger subledger) {
		this.subledger = subledger;
	}

	public Long getYear_endJvSubledgerDetailsId() {
		return year_endJvSubledgerDetailsId;
	}

	public void setYear_endJvSubledgerDetailsId(Long year_endJvSubledgerDetailsId) {
		this.year_endJvSubledgerDetailsId = year_endJvSubledgerDetailsId;
	}
}
