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
@Table(name = "payrollSubledgerDetails")
public class PayrollSubledgerDetails {

	@Id
	@GeneratedValue
	@Column(name = "subledgerdetails_id")
	private Long subledgerdetails_id;
	
	@Column
	private String subledgerName;
	
	@Column
	private Double crAmount;
	
	@Column
	private Double drAmount;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payroll_id")
	private PayrollAutoJV payrollAutoJV;
	
	public Long getSubledgerdetails_id() {
		return subledgerdetails_id;
	}

	public void setSubledgerdetails_id(Long subledgerdetails_id) {
		this.subledgerdetails_id = subledgerdetails_id;
	}

	public String getSubledgerName() {
		return subledgerName;
	}

	public void setSubledgerName(String subledgerName) {
		this.subledgerName = subledgerName;
	}

	public Double getCrAmount() {
		return crAmount;
	}

	public void setCrAmount(Double crAmount) {
		this.crAmount = crAmount;
	}

	public Double getDrAmount() {
		return drAmount;
	}

	public void setDrAmount(Double drAmount) {
		this.drAmount = drAmount;
	}

	public PayrollAutoJV getPayrollAutoJV() {
		return payrollAutoJV;
	}

	public void setPayrollAutoJV(PayrollAutoJV payrollAutoJV) {
		this.payrollAutoJV = payrollAutoJV;
	}
}
