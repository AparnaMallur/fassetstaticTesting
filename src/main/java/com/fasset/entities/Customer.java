package com.fasset.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDateTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;
import com.fasset.form.SubNature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
/**
 * @author "Vishwajeet"
 *
 */
@Entity
@Table(name = "customer_master")
@JsonIgnoreProperties({"compStatType", "industryType","city","country","state","company","product","subLedgers","deductee","openingbalances"})
public class Customer extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "customer_id", unique = true, nullable = false)
	private Long customer_id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_statutory_id")
	private CompanyStatutoryType compStatType;

	@Transient
	private Long company_statutory_id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "industry_id")
	private IndustryType industryType;

	@Transient
	private Long industry_id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	private City city;

	@Transient
	private Long city_id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private Country country;

	@Transient
	private Long country_id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id")
	private State state;

	@Transient
	private Long state_id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	@Transient
	private Long company_id ;
	
	@Transient
	private List<String> customerList ;
	
	@Transient
	private Boolean primaryApproval;
	
	@Transient
	private Boolean rejectApproval;
	
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private Set<Product> product = new HashSet<Product>();

	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private Set<SubLedger> subLedgers = new HashSet<SubLedger>();

	@Column(name = "contact_name", nullable = true, length = MyAbstractController.SIZE_THREE_HUNDRED)
	private String contact_name;

	@Column(name = "mobile", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private String mobile;

	@Column(name = "landline_no", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private String landline_no;

	@Column(name = "email_id", nullable = true, length = MyAbstractController.SIZE_HUNDRED)
	private String email_id;

	@Column(name = "adhaar_no", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private String adhaar_no;

	@Column(name = "owner_pan_no", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private String owner_pan_no;

	@Column(name = "firm_name", nullable = true, length = MyAbstractController.SIZE_FIVE_HUNDRED)
	private String firm_name;

	@Column(name = "current_address", nullable = true, length = MyAbstractController.SIZE_FIVE_HUNDRED)
	private String current_address;

	@Column(name = "permenant_address", nullable = true, length = MyAbstractController.SIZE_FIVE_HUNDRED)
	private String permenant_address;

	@Column(name = "pincode", nullable = true, length = MyAbstractController.SIZE_TEN)
	private Long pincode;

	@Column(name = "gst_no", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private String gst_no;

	@Column(name = "gst_applicable")
	private Boolean gst_applicable;

	@Column(name = "company_pan_no", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private String company_pan_no;

	@Column(name = "tan_no", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private String tan_no;

	@Column(name = "other_tax_no", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private String other_tax_no;

	@Column(name = "tds_applicable", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Integer tds_applicable;

	@Column(name = "tds_rate", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Float tds_rate;

	@Column(name = "from_mobile", nullable = true)
	private Boolean from_mobile;

	@Column(name = "status", nullable = true)
	private Boolean status;

	@Column(name = "created_date", nullable = true)
	private LocalDateTime created_date;

	@Column(name = "created_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Long created_by;

	@Column(name = "updated_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Long updated_by;

	@Column(name = "update_date", nullable = true)
	private LocalDateTime update_date;

	@Column(name = "ip_address", nullable = true)
	private String ip_address;

	@Column(name = "customer_approval", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Integer customer_approval;

	@Transient
	private List<String> productList;

	@Transient
	private String subNatureList;

	@Transient
	List<SubNature> subPurposeList;

	@Transient
	private String tds_rate1;
	
	@Column(name = "credit_opening_balance")
	private Float credit_opening_balance;

	@Column(name = "debit_opening_balance")
	private Float debit_opening_balance;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tdsType_id")
	private TDS_Type tdstype ;

	
	public synchronized TDS_Type getTdstype() {
		return tdstype;
	}

	public synchronized void setTdstype(TDS_Type tdstype) {
		this.tdstype = tdstype;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deductee_id")
	private Deductee deductee ;
	@Transient
	private Long deductee_id ;
	
	
	
	
	@Transient
	private String credit_opening_balance1;

	

	@Transient
	private String debit_opening_balance1;
	
	@Column(name = "flag")
	private Boolean flag;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "opening_id")
	private OpeningBalances openingbalances;
	
	@Transient
	private Long openingId ;
	
	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}

	public CompanyStatutoryType getCompStatType() {
		return compStatType;
	}

	public void setCompStatType(CompanyStatutoryType compStatType) {
		this.compStatType = compStatType;
	}

	public Long getCompany_statutory_id() {
		return company_statutory_id;
	}

	public void setCompany_statutory_id(Long company_statutory_id) {
		this.company_statutory_id = company_statutory_id;
	}

	public IndustryType getIndustryType() {
		return industryType;
	}

	public void setIndustryType(IndustryType industryType) {
		this.industryType = industryType;
	}

	public Long getIndustry_id() {
		return industry_id;
	}

	public void setIndustry_id(Long industry_id) {
		this.industry_id = industry_id;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Long getCity_id() {
		return city_id;
	}

	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Long getCountry_id() {
		return country_id;
	}

	public void setCountry_id(Long country_id) {
		this.country_id = country_id;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Long getState_id() {
		return state_id;
	}

	public void setState_id(Long state_id) {
		this.state_id = state_id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public Set<Product> getProduct() {
		return product;
	}

	public void setProduct(Set<Product> product) {
		this.product = product;
	}

	public Set<SubLedger> getSubLedgers() {
		return subLedgers;
	}

	public void setSubLedgers(Set<SubLedger> subLedgers) {
		this.subLedgers = subLedgers;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLandline_no() {
		return landline_no;
	}

	public void setLandline_no(String landline_no) {
		this.landline_no = landline_no;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getAdhaar_no() {
		return adhaar_no;
	}

	public void setAdhaar_no(String adhaar_no) {
		this.adhaar_no = adhaar_no;
	}

	public String getOwner_pan_no() {
		return owner_pan_no;
	}

	public void setOwner_pan_no(String owner_pan_no) {
		this.owner_pan_no = owner_pan_no;
	}

	public String getFirm_name() {
		return firm_name;
	}

	public void setFirm_name(String firm_name) {
		this.firm_name = firm_name;
	}

	public String getCurrent_address() {
		return current_address;
	}

	public void setCurrent_address(String current_address) {
		this.current_address = current_address;
	}

	public String getPermenant_address() {
		return permenant_address;
	}

	public void setPermenant_address(String permenant_address) {
		this.permenant_address = permenant_address;
	}

	public Long getPincode() {
		return pincode;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public String getGst_no() {
		return gst_no;
	}

	public void setGst_no(String gst_no) {
		this.gst_no = gst_no;
	}

	public Boolean getGst_applicable() {
		return gst_applicable;
	}

	public void setGst_applicable(Boolean gst_applicable) {
		this.gst_applicable = gst_applicable;
	}

	public String getCompany_pan_no() {
		return company_pan_no;
	}

	public void setCompany_pan_no(String company_pan_no) {
		this.company_pan_no = company_pan_no;
	}

	public String getTan_no() {
		return tan_no;
	}

	public void setTan_no(String tan_no) {
		this.tan_no = tan_no;
	}

	public String getOther_tax_no() {
		return other_tax_no;
	}

	public void setOther_tax_no(String other_tax_no) {
		this.other_tax_no = other_tax_no;
	}

	public Integer getTds_applicable() {
		return tds_applicable;
	}

	public void setTds_applicable(Integer tds_applicable) {
		this.tds_applicable = tds_applicable;
	}

	public Float getTds_rate() {
		return tds_rate;
	}

	public void setTds_rate(Float tds_rate) {
		this.tds_rate = tds_rate;
	}

	public Boolean getFrom_mobile() {
		return from_mobile;
	}

	public void setFrom_mobile(Boolean from_mobile) {
		this.from_mobile = from_mobile;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public Long getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Long created_by) {
		this.created_by = created_by;
	}

	public Long getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Long updated_by) {
		this.updated_by = updated_by;
	}

	public LocalDateTime getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(LocalDateTime update_date) {
		this.update_date = update_date;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public Integer getCustomer_approval() {
		return customer_approval;
	}

	public void setCustomer_approval(Integer customer_approval) {
		this.customer_approval = customer_approval;
	}

	public List<String> getProductList() {
		return productList;
	}

	public void setProductList(List<String> productList) {
		this.productList = productList;
	}

	public String getSubNatureList() {
		return subNatureList;
	}

	public void setSubNatureList(String subNatureList) {
		this.subNatureList = subNatureList;
	}

	public List<SubNature> getSubPurposeList() {
		return subPurposeList;
	}

	public void setSubPurposeList(List<SubNature> subPurposeList) {
		this.subPurposeList = subPurposeList;
	}

	public String getTds_rate1() {
		return tds_rate1;
	}

	public void setTds_rate1(String tds_rate1) {
		this.tds_rate1 = tds_rate1;
	}

	public Float getCredit_opening_balance() {
		return credit_opening_balance;
	}

	public void setCredit_opening_balance(Float credit_opening_balance) {
		this.credit_opening_balance = credit_opening_balance;
	}

	public Float getDebit_opening_balance() {
		return debit_opening_balance;
	}

	public void setDebit_opening_balance(Float debit_opening_balance) {
		this.debit_opening_balance = debit_opening_balance;
	}

	public String getCredit_opening_balance1() {
		return credit_opening_balance1;
	}

	public void setCredit_opening_balance1(String credit_opening_balance1) {
		this.credit_opening_balance1 = credit_opening_balance1;
	}

	public String getDebit_opening_balance1() {
		return debit_opening_balance1;
	}

	public void setDebit_opening_balance1(String debit_opening_balance1) {
		this.debit_opening_balance1 = debit_opening_balance1;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public OpeningBalances getOpeningbalances() {
		return openingbalances;
	}

	public void setOpeningbalances(OpeningBalances openingbalances) {
		this.openingbalances = openingbalances;
	}

	public Long getOpeningId() {
		return openingId;
	}

	public void setOpeningId(Long openingId) {
		this.openingId = openingId;
	}

	public Deductee getDeductee() {
		return deductee;
	}

	public void setDeductee(Deductee deductee) {
		this.deductee = deductee;
	}

	public Long getDeductee_id() {
		return deductee_id;
	}

	public void setDeductee_id(Long deductee_id) {
		this.deductee_id = deductee_id;
	}

	/**
	 * @return the customerList
	 */
	public List<String> getCustomerList() {
		return customerList;
	}

	/**
	 * @param customerList the customerList to set
	 */
	public void setCustomerList(List<String> customerList) {
		this.customerList = customerList;
	}

	/**
	 * @return the primaryApproval
	 */
	public Boolean getPrimaryApproval() {
		return primaryApproval;
	}

	/**
	 * @param primaryApproval the primaryApproval to set
	 */
	public void setPrimaryApproval(Boolean primaryApproval) {
		this.primaryApproval = primaryApproval;
	}

	/**
	 * @return the rejectApproval
	 */
	public Boolean getRejectApproval() {
		return rejectApproval;
	}

	/**
	 * @param rejectApproval the rejectApproval to set
	 */
	public void setRejectApproval(Boolean rejectApproval) {
		this.rejectApproval = rejectApproval;
	}

}