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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

import sun.print.resources.serviceui;

@Entity
@Table(name = "company")
public class Company extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "company_id", unique = true, nullable = false)
	private Long company_id;

	@Column(name = "company_name", length = 300)
	private String company_name;

	@Column(name = "emp_Limit", length = 300)
	private Integer empLimit;

	@Column(name = "subscription_range", length = 300)
	private String yearRange;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_statutory_type")
	private CompanyStatutoryType company_statutory_type;

	@Transient
	private Long companyTypeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "industry_type")
	private IndustryType industry_type;

	@Transient
	private Long industryTypeId;

	@Column(name = "mobile", length = 20)
	private String mobile;

	@Column(name = "iec_no", length = 20)
	private String iec_no;

	@Column(name = "landline_no", length = 20)
	private Long landline_no;

	@Column(name = "email_id", length = 100)
	private String email_id;

	@Column(name = "logo", length = 300)
	private String logo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country")
	private Country country;

	@Transient
	private Long countryId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "state")
	private State state;

	@Transient
	private Long stateId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city")
	private City city;

	@Transient
	private Long cityId;

	@Column(name = "pincode", length = 10)
	private Integer pincode;

	@Column(name = "from_mobile")
	private Boolean from_mobile;

	@Column(name = "current_address")
	private String current_address;

	@Column(name = "permenant_address")
	private String permenant_address;

	@Column(name = "registration_no")
	private String registration_no;

	@Column(name = "rcm") // Reverse Charge Mechanism
	private Boolean rcm;

	@Column(name = "pan_no")
	private String pan_no;

	@Column(name = "gst_no")
	private String gst_no;

	@Column(name = "pte_no")
	private String pte_no;

	@Column(name = "ptr_no")
	private String ptr_no;

	@Column(name = "eway_bill_no")
	private String eway_bill_no;

	@Column(name = "other_tax_1")
	private String other_tax_1;

	@Column(name = "other_tax_2")
	private String other_tax_2;

	@Column(name = "is_client")
	private Integer is_client;

	@Column(name = "status")
	private int status;

	@Column(name = "created_date")
	private LocalDate created_date;

	@Column(name = "email_date")
	private LocalDate email_date;

	@Column(name = "email_sent")
	private Integer email_sent;
	
	@Column(name = "istrialClient")
	private Boolean istrialClient;

	@Transient
	private String serviceFreq;
	
	@Column(name = "isImport")
	private Boolean isImport;
	
	public LocalDate getEmail_date() {
		return email_date;
	}

	public void setEmail_date(LocalDate email_date) {
		this.email_date = email_date;
	}

	public Integer getEmail_sent() {
		return email_sent;
	}

	public void setEmail_sent(Integer email_sent) {
		this.email_sent = email_sent;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createdby")
	private User created_by;

	@Column(name = "updated_date")
	private LocalDate updated_date;

	@Column(name = "updated_by")
	private User updated_by;

	@Column(name = "approved_date")
	private LocalDate approved_date;

	@Column(name = "approved_by")
	private User approved_by;

	@Column(name = "business_nature")
	private String business_nature;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "year_range")
	private AccountingYear year_range;

	@Transient
	private Long yearRangeId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bank")
	private Bank bank;

	@Transient
	private Long bankId;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
	private Set<User> user = new HashSet<User>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
	private Set<Ledger> ledgerList = new HashSet<Ledger>();

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<ClientValidationChecklist> checklist = new HashSet<ClientValidationChecklist>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company", cascade = CascadeType.ALL)
	private Set<ClientSubscriptionMaster> clientSubscriptionMasters = new HashSet<ClientSubscriptionMaster>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
	private Set<Product> product = new HashSet<Product>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
	private Set<AccountGroup> accountGroup = new HashSet<AccountGroup>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
	private Set<Customer> customerList = new HashSet<Customer>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
	private Set<Suppliers> suppliersList = new HashSet<Suppliers>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
	private Set<Bank> bankList = new HashSet<Bank>();

	@Column(name = "subscription_from")
	private LocalDate subscription_from;

	@Column(name = "subscription_to")
	private LocalDate subscription_to;
	
	@Column(name = "voucher_range")
	private String voucher_range;

	@Column(name = "trial_balance", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean trial_balance;

	@Column(name = "openingbalance_date")
	private LocalDate openingbalance_date;
	
	@Transient
	private long frequencyServiceList;

	@Transient
	private long serviceList;

	public Set<ClientValidationChecklist> getChecklist() {
		return checklist;
	}

	public void setChecklist(Set<ClientValidationChecklist> checklist) {
		this.checklist = checklist;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public CompanyStatutoryType getCompany_statutory_type() {
		return company_statutory_type;
	}

	public void setCompany_statutory_type(CompanyStatutoryType company_statutory_type) {
		this.company_statutory_type = company_statutory_type;
	}

	public Long getCompanyTypeId() {
		return companyTypeId;
	}

	public void setCompanyTypeId(Long companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public IndustryType getIndustry_type() {
		return industry_type;
	}

	public void setIndustry_type(IndustryType industry_type) {
		this.industry_type = industry_type;
	}

	public Long getIndustryTypeId() {
		return industryTypeId;
	}

	public void setIndustryTypeId(Long industryTypeId) {
		this.industryTypeId = industryTypeId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIec_no() {
		return iec_no;
	}

	public void setIec_no(String iec_no) {
		this.iec_no = iec_no;
	}

	public Long getLandline_no() {
		return landline_no;
	}

	public void setLandline_no(Long landline_no) {
		this.landline_no = landline_no;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public Boolean getFrom_mobile() {
		return from_mobile;
	}

	public void setFrom_mobile(Boolean from_mobile) {
		this.from_mobile = from_mobile;
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

	public String getRegistration_no() {
		return registration_no;
	}

	public void setRegistration_no(String registration_no) {
		this.registration_no = registration_no;
	}

	public Boolean getRcm() {
		return rcm;
	}

	public void setRcm(Boolean rcm) {
		this.rcm = rcm;
	}

	public String getPan_no() {
		return pan_no;
	}

	public void setPan_no(String pan_no) {
		this.pan_no = pan_no;
	}

	public String getGst_no() {
		return gst_no;
	}

	public void setGst_no(String gst_no) {
		this.gst_no = gst_no;
	}

	public String getPte_no() {
		return pte_no;
	}

	public void setPte_no(String pte_no) {
		this.pte_no = pte_no;
	}

	public String getPtr_no() {
		return ptr_no;
	}

	public void setPtr_no(String ptr_no) {
		this.ptr_no = ptr_no;
	}

	public String getEway_bill_no() {
		return eway_bill_no;
	}

	public void setEway_bill_no(String eway_bill_no) {
		this.eway_bill_no = eway_bill_no;
	}

	public String getOther_tax_1() {
		return other_tax_1;
	}

	public void setOther_tax_1(String other_tax_1) {
		this.other_tax_1 = other_tax_1;
	}

	public String getOther_tax_2() {
		return other_tax_2;
	}

	public void setOther_tax_2(String other_tax_2) {
		this.other_tax_2 = other_tax_2;
	}

	public Integer getIs_client() {
		return is_client;
	}

	public void setIs_client(Integer is_client) {
		this.is_client = is_client;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public User getCreated_by() {
		return created_by;
	}

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
	}

	public User getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(User updated_by) {
		this.updated_by = updated_by;
	}

	public LocalDate getApproved_date() {
		return approved_date;
	}

	public void setApproved_date(LocalDate approved_date) {
		this.approved_date = approved_date;
	}

	public User getApproved_by() {
		return approved_by;
	}

	public void setApproved_by(User approved_by) {
		this.approved_by = approved_by;
	}

	public Set<User> getUser() {
		return user;
	}

	public void setUser(Set<User> user) {
		this.user = user;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getBusiness_nature() {
		return business_nature;
	}

	public void setBusiness_nature(String business_nature) {
		this.business_nature = business_nature;
	}

	public AccountingYear getYear_range() {
		return year_range;
	}

	public void setYear_range(AccountingYear year_range) {
		this.year_range = year_range;
	}

	public Long getYearRangeId() {
		return yearRangeId;
	}

	public void setYearRangeId(Long yearRangeId) {
		this.yearRangeId = yearRangeId;
	}

	public Set<ClientSubscriptionMaster> getClientSubscriptionMasters() {
		return clientSubscriptionMasters;
	}

	public void setClientSubscriptionMasters(Set<ClientSubscriptionMaster> clientSubscriptionMasters) {
		this.clientSubscriptionMasters = clientSubscriptionMasters;
	}

	public LocalDate getSubscription_from() {
		return subscription_from;
	}

	public void setSubscription_from(LocalDate subscription_from) {
		this.subscription_from = subscription_from;
	}

	public LocalDate getSubscription_to() {
		return subscription_to;
	}

	public void setSubscription_to(LocalDate subscription_to) {
		this.subscription_to = subscription_to;
	}

	public Set<Product> getProduct() {
		return product;
	}

	public void setProduct(Set<Product> product) {
		this.product = product;
	}

	public Integer getEmpLimit() {
		return empLimit;
	}

	public void setEmpLimit(Integer empLimit) {
		this.empLimit = empLimit;
	}

	public Set<AccountGroup> getAccountGroup() {
		return accountGroup;
	}

	public void setAccountGroup(Set<AccountGroup> accountGroup) {
		this.accountGroup = accountGroup;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getYearRange() {
		return yearRange;
	}

	public void setYearRange(String yearRange) {
		this.yearRange = yearRange;
	}

	public Set<Ledger> getLedgerList() {
		return ledgerList;
	}

	public void setLedgerList(Set<Ledger> ledgerList) {
		this.ledgerList = ledgerList;
	}

	public String getVoucher_range() {
		return voucher_range;
	}

	public void setVoucher_range(String voucher_range) {
		this.voucher_range = voucher_range;
	}

	public Set<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(Set<Customer> customerList) {
		this.customerList = customerList;
	}

	public Set<Suppliers> getSuppliersList() {
		return suppliersList;
	}

	public void setSuppliersList(Set<Suppliers> suppliersList) {
		this.suppliersList = suppliersList;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Boolean getTrial_balance() {
		return trial_balance;
	}

	public void setTrial_balance(Boolean trial_balance) {
		this.trial_balance = trial_balance;
	}

	public Set<Bank> getBankList() {
		return bankList;
	}

	public void setBankList(Set<Bank> bankList) {
		this.bankList = bankList;
	}

	public String getServiceFreq() {
		return serviceFreq;
	}

	public void setServiceFreq(String serviceFreq) {
		this.serviceFreq = serviceFreq;
	}

	public long getFrequencyServiceList() {
		return frequencyServiceList;
	}

	public void setFrequencyServiceList(long frequencyServiceList) {
		this.frequencyServiceList = frequencyServiceList;
	}

	public long getServiceList() {
		return serviceList;
	}

	public void setServiceList(long serviceList) {
		this.serviceList = serviceList;
	}

	public Boolean getIstrialClient() {
		return istrialClient;
	}

	public void setIstrialClient(Boolean istrialClient) {
		this.istrialClient = istrialClient;
	}

	public LocalDate getOpeningbalance_date() {
		return openingbalance_date;
	}

	public void setOpeningbalance_date(LocalDate openingbalance_date) {
		this.openingbalance_date = openingbalance_date;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsImport() {
		return isImport;
	}

	public void setIsImport(Boolean isImport) {
		this.isImport = isImport;
	}



}