package com.fasset.entities;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name = "payrollEmployeeDetails")
public class PayrollEmployeeDetails implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "employeeDetails_id")
	private Long employeeDetails_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "payroll_id")
	private PayrollAutoJV payrollAutoJV;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="employee_id")
	private Employee employee;
	
	@Transient
	private long employee_id;
	
	@Transient
	private long wdays;
	
	public long getWdays() {
		return wdays;
	}
  
	public void setWdays(long wdays) {
		this.wdays = wdays;
	}

	@Column
	private String code;
	
	@Column
	private String name;
	
	@Column
	private String pan;
	
	@Column
	private Integer totaldays;
	
	@Column
	private Double basicSalary;
	
	@Column
	private Double DA;
	
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

	public Long getEmployeeDetails_id() {
		return employeeDetails_id;
	}

	public void setEmployeeDetails_id(Long employeeDetails_id) {
		this.employeeDetails_id = employeeDetails_id;
	}

	public PayrollAutoJV getPayrollAutoJV() {
		return payrollAutoJV;
	}

	public void setPayrollAutoJV(PayrollAutoJV payrollAutoJV) {
		this.payrollAutoJV = payrollAutoJV;
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

	public Integer getTotaldays() {
		return totaldays;
	}

	public void setTotaldays(Integer totaldays) {
		this.totaldays = totaldays;
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

	public Double geteSICEmployeeContribution() {
		return eSICEmployeeContribution;
	}

	public void seteSICEmployeeContribution(Double eSICEmployeeContribution) {
		this.eSICEmployeeContribution = eSICEmployeeContribution;
	}

	public Double getProfessionTax() {
		return professionTax;
	}

	public void setProfessionTax(Double professionTax) {
		this.professionTax = professionTax;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public long getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(long employee_id) {
		this.employee_id = employee_id;
	}
}