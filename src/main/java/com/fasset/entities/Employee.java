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

import org.joda.time.LocalDate;

import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "employee")
public class Employee  extends AbstractEntity{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "employee_id")
	private Long employee_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;
	
	@Transient
	private Long company_id ;
	
	@Column
	private Boolean status;
	
	@Column
	private String code;
	
	@Column
	private String name;
	
	@Column
	private String mobile;
	
	@Column
	private LocalDate doj;
	
	@Column
	private String pan;
	
	@Column
	private String adharNo;
	
	@Column
	private Double basicSalary;
	
	@Column
	private Double DA;
	
	@Column
	private String current_address;
	
	@Column
	private Double conveyanceAllowance;
	
	@Column
	private Double otherAllowances;
	
	@Column
	private Double grossSalary;
	
	@Column
	private Double pfEmployeeContribution;
	
	@Column
	private Double eSICEmployeeContribution;
	
	@Column
	private Double professionTax;
	
	@Column
	private Double lWF;
	
	@Column
	private Double tDS;
	
	@Column
	private Double otherDeductions;
	
	@Column
	private Double advanceAdjustment;
	
	@Column
	private Double totalDeductions;
	
	@Column
	private Double netSalary;
	
	@Column
	private Double pfEmployerContribution;
	
	@Column
	private Double eSICEmployerContribution;
	
	@Column
	private Double pFAdminCharges;

	public Long getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(Long employee_id) {
		this.employee_id = employee_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getAdharNo() {
		return adharNo;
	}

	public void setAdharNo(String adharNo) {
		this.adharNo = adharNo;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Double getDA() {
		return DA;
	}

	public void setDA(Double dA) {
		DA = dA;
	}

	public Double getConveyanceAllowance() {
		return conveyanceAllowance;
	}

	public void setConveyanceAllowance(Double conveyanceAllowance) {
		this.conveyanceAllowance = conveyanceAllowance;
	}

	public Double getOtherAllowances() {
		return otherAllowances;
	}

	public void setOtherAllowances(Double otherAllowances) {
		this.otherAllowances = otherAllowances;
	}

	public Double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(Double grossSalary) {
		this.grossSalary = grossSalary;
	}

	public Double getPfEmployeeContribution() {
		return pfEmployeeContribution;
	}

	public void setPfEmployeeContribution(Double pfEmployeeContribution) {
		this.pfEmployeeContribution = pfEmployeeContribution;
	}



	public Double getOtherDeductions() {
		return otherDeductions;
	}

	public void setOtherDeductions(Double otherDeductions) {
		this.otherDeductions = otherDeductions;
	}

	public Double getAdvanceAdjustment() {
		return advanceAdjustment;
	}

	public void setAdvanceAdjustment(Double advanceAdjustment) {
		this.advanceAdjustment = advanceAdjustment;
	}

	public Double getTotalDeductions() {
		return totalDeductions;
	}

	public void setTotalDeductions(Double totalDeductions) {
		this.totalDeductions = totalDeductions;
	}

	public Double getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(Double netSalary) {
		this.netSalary = netSalary;
	}

	public Double getPfEmployerContribution() {
		return pfEmployerContribution;
	}

	public void setPfEmployerContribution(Double pfEmployerContribution) {
		this.pfEmployerContribution = pfEmployerContribution;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Double geteSICEmployeeContribution() {
		return eSICEmployeeContribution;
	}

	public void seteSICEmployeeContribution(Double eSICEmployeeContribution) {
		this.eSICEmployeeContribution = eSICEmployeeContribution;
	}

	public Double getlWF() {
		return lWF;
	}

	public void setlWF(Double lWF) {
		this.lWF = lWF;
	}

	public Double gettDS() {
		return tDS;
	}

	public void settDS(Double tDS) {
		this.tDS = tDS;
	}

	public Double geteSICEmployerContribution() {
		return eSICEmployerContribution;
	}

	public void seteSICEmployerContribution(Double eSICEmployerContribution) {
		this.eSICEmployerContribution = eSICEmployerContribution;
	}

	public Double getpFAdminCharges() {
		return pFAdminCharges;
	}

	public void setpFAdminCharges(Double pFAdminCharges) {
		this.pFAdminCharges = pFAdminCharges;
	}

	public Double getProfessionTax() {
		return professionTax;
	}

	public void setProfessionTax(Double professionTax) {
		this.professionTax = professionTax;
	}

	public LocalDate getDoj() {
		return doj;
	}

	public void setDoj(LocalDate doj) {
		this.doj = doj;
	}



	public String getCurrent_address() {
		return current_address;
	}

	public void setCurrent_address(String current_address) {
		this.current_address = current_address;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
	
}