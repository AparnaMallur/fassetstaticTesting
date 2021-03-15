package com.fasset.form;

import org.springframework.stereotype.Component;

import com.fasset.entities.Company;
import com.fasset.entities.User;

@Component
public class UserForm {
	
	private Company company;
	private User user;
	private Long companyId;
	private Long industryTypeId;
	private Long companyTypeId;
	private Integer signUpType;
	private Integer paymentType;
	private String quotationNo;
	private Float amount;
	private Long role_id;
	
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Long getIndustryTypeId() {
		return industryTypeId;
	}
	public void setIndustryTypeId(Long industryTypeId) {
		this.industryTypeId = industryTypeId;
	}
	public Long getCompanyTypeId() {
		return companyTypeId;
	}
	public void setCompanyTypeId(Long companyTypeId) {
		this.companyTypeId = companyTypeId;
	}
	public String getQuotationNo() {
		return quotationNo;
	}
	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}
	public Integer getSignUpType() {
		return signUpType;
	}
	public void setSignUpType(Integer signUpType) {
		this.signUpType = signUpType;
	}
	public Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	/**
	 * @return the role_id
	 */
	public Long getRole_id() {
		return role_id;
	}
	/**
	 * @param role_id the role_id to set
	 */
	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	
	
}
