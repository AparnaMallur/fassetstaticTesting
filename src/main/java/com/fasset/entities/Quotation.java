package com.fasset.entities;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;
import com.fasset.form.QuotationDetailsForm;

@Entity
@Table(name = "quotation")
public class Quotation extends AbstractEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "quotation_id", unique = true, nullable = false)
	private Long quotation_id;
	
	@Column(name = "quotation_no")
	private String quotation_no;
	
	@Column(name = "first_name")
	private String first_name;
	
	@Column(name = "last_name")
	private String last_name;	

	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile_no")
	private String mobile_no;
	
	@Column(name = "status", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean status ;
	
	@Column(name = "company_name", length = 300)
	private String company_name;
	
	@Column(name = "flag")
	private Boolean flag;
	
	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_statutory_id")
	private CompanyStatutoryType companystatutorytype;
	
	@Transient
	private Long company_statutory_id;	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "industry_id")
	private IndustryType industrytype;	
	
	@Transient
	private Long industry_id;
		
	@Transient
	private Float amount;
	
	@Transient
	private Boolean service_status;
	
	@Transient
	private Long frequency_id;
	
	@Transient
	private Long service_id;
	
	@Transient
	private Long companyId;
	
	@Transient
	private Integer paymentType;

	@Transient
	private Long save_id; 
	
	@Transient
	private String QuoteDetails ;	
	 
	@Transient
	List<QuotationDetailsForm> QuoteDetailsList;	 
	 
	@Column(name = "created_by")
	private Integer created_by;
	
	@Column(name = "created_date")
	private LocalDate created_date;
	
	@Column(name = "updated_by")
	private Integer updated_by;
	
	@Column(name = "updated_date")
	private LocalDate updated_date;
	
	@Basic
    private java.sql.Time time;
	
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "quotation_id", cascade = CascadeType.ALL)
	private Set<QuotationDetails> quotationDetails;
	
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
	}

	public Long getQuotation_id() {
		return quotation_id;
	}

	public void setQuotation_id(Long quotation_id) {
		this.quotation_id = quotation_id;
	}

	public String getQuotation_no() {
		return quotation_no;
	}

	public void setQuotation_no(String quotation_no) {
		this.quotation_no = quotation_no;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public Set<QuotationDetails> getQuotationDetails() {
		return quotationDetails;
	}

	public void setQuotationDetails(Set<QuotationDetails> quotationDetails) {
		this.quotationDetails = quotationDetails;
	}

	public CompanyStatutoryType getCompanystatutorytype() {
		return companystatutorytype;
	}

	public void setCompanystatutorytype(CompanyStatutoryType companystatutorytype) {
		this.companystatutorytype = companystatutorytype;
	}

	public IndustryType getIndustrytype() {
		return industrytype;
	}

	public void setIndustrytype(IndustryType industrytype) {
		this.industrytype = industrytype;
	}

	public Long getCompany_statutory_id() {
		return company_statutory_id;
	}

	public void setCompany_statutory_id(Long company_statutory_id) {
		this.company_statutory_id = company_statutory_id;
	}

	public Long getIndustry_id() {
		return industry_id;
	}

	public void setIndustry_id(Long industry_id) {
		this.industry_id = industry_id;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Long getFrequency_id() {
		return frequency_id;
	}

	public void setFrequency_id(Long frequency_id) {
		this.frequency_id = frequency_id;
	}

	public Long getService_id() {
		return service_id;
	}

	public void setService_id(Long service_id) {
		this.service_id = service_id;
	}

	public Long getSave_id() {
		return save_id;
	}

	public void setSave_id(Long save_id) {
		this.save_id = save_id;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	public String getQuoteDetails() {
		return QuoteDetails;
	}

	public void setQuoteDetails(String quoteDetails) {
		QuoteDetails = quoteDetails;
	}

	public List<QuotationDetailsForm> getQuoteDetailsList() {
		return QuoteDetailsList;
	}

	public void setQuoteDetailsList(List<QuotationDetailsForm> quoteDetailsList) {
		QuoteDetailsList = quoteDetailsList;
	}

	public Boolean getService_status() {
		return service_status;
	}

	public void setService_status(Boolean service_status) {
		this.service_status = service_status;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public java.sql.Time getTime() {
		return time;
	}

	public void setTime(java.sql.Time time) {
		this.time = time;
	}

	

	

}
