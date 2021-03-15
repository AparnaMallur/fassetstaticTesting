package com.fasset.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "payroll_autoJV")
public class PayrollAutoJV extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "payroll_id")
	private Long payroll_id;
	
	
	
	
	@Column(name = "date", nullable = true)
	private LocalDate date ;

	@Column(name = "voucher_no")
	private String voucher_no;
	
	@Column(name = "excel_voucher_no")
	private String excel_voucher_no ;
	
	@Column(name = "other_remark", nullable = true, length = MyAbstractController.SIZE_THOUSAND)
	private String other_remark;
	
	@Transient
	private String employeeDetails;

	@Transient
	private String subledgerDetails;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "payrollAutoJV", cascade = CascadeType.ALL)
	private Set<PayrollSubledgerDetails> payrollSubledgerDetails;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "payrollAutoJV", cascade = CascadeType.ALL)
	private Set<PayrollEmployeeDetails> payrollEmployeeDetails;
	
	@Transient
    private List<PayrollSubledgerDetails> payrollSubledgerdetailList ;
	

	@Transient
    private List<PayrollEmployeeDetails> payrollEmployeedetailList ;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accounting_year_id;
	
	@OneToOne(targetEntity=VoucherSeries.class,cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name = "voucher_id",referencedColumnName="voucher_id")
	private VoucherSeries voucherSeries;
    
	
	@Transient
	private Long accountYearId;
	
	@Column(name = "created_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private  Long created_by ; 
	
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;
	
	@Column(name = "working_days")
	private Long days;
	
	
	
    @Transient
  	private Long days1;
	
	public Long getPayroll_id() {
		return payroll_id;
	}

	public void setPayroll_id(Long payroll_id) {
		this.payroll_id = payroll_id;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(String employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	public Set<PayrollSubledgerDetails> getPayrollSubledgerDetails() {
		return payrollSubledgerDetails;
	}

	public void setPayrollSubledgerDetails(Set<PayrollSubledgerDetails> payrollSubledgerDetails) {
		this.payrollSubledgerDetails = payrollSubledgerDetails;
	}

	public Set<PayrollEmployeeDetails> getPayrollEmployeeDetails() {
		return payrollEmployeeDetails;
	}

	public void setPayrollEmployeeDetails(Set<PayrollEmployeeDetails> payrollEmployeeDetails) {
		this.payrollEmployeeDetails = payrollEmployeeDetails;
	}

	public String getSubledgerDetails() {
		return subledgerDetails;
	}

	public void setSubledgerDetails(String subledgerDetails) {
		this.subledgerDetails = subledgerDetails;
	}

	public String getVoucher_no() {
		return voucher_no;
	}

	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}

	public String getExcel_voucher_no() {
		return excel_voucher_no;
	}

	public void setExcel_voucher_no(String excel_voucher_no) {
		this.excel_voucher_no = excel_voucher_no;
	}

	public String getOther_remark() {
		return other_remark;
	}

	public void setOther_remark(String other_remark) {
		this.other_remark = other_remark;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Long getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Long created_by) {
		this.created_by = created_by;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public AccountingYear getAccounting_year_id() {
		return accounting_year_id;
	}

	public void setAccounting_year_id(AccountingYear accounting_year_id) {
		this.accounting_year_id = accounting_year_id;
	}

	public Long getAccountYearId() {
		return accountYearId;
	}

	public void setAccountYearId(Long accountYearId) {
		this.accountYearId = accountYearId;
	}

	public Long getDays() {
		return days;
	}

	public void setDays(Long days) {
		this.days = days;
	}

	public Long getDays1() {
		return days1;
	}

	public void setDays1(Long days1) {
		this.days1 = days1;
	}

	public VoucherSeries getVoucherSeries() {
		return voucherSeries;
	}

	public void setVoucherSeries(VoucherSeries voucherSeries) {
		this.voucherSeries = voucherSeries;
	}

	public List<PayrollSubledgerDetails> getPayrollSubledgerdetailList() {
		return payrollSubledgerdetailList;
	}

	public void setPayrollSubledgerdetailList(List<PayrollSubledgerDetails> payrollSubledgerdetailList) {
		this.payrollSubledgerdetailList = payrollSubledgerdetailList;
	}

	public List<PayrollEmployeeDetails> getPayrollEmployeedetailList() {
		return payrollEmployeedetailList;
	}

	public void setPayrollEmployeedetailList(List<PayrollEmployeeDetails> payrollEmployeedetailList) {
		this.payrollEmployeedetailList = payrollEmployeedetailList;
	}

}