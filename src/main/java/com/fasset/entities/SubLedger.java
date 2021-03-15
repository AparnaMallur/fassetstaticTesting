/**
 * mayur suramwar
 */
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name = "subledger_master")
public class SubLedger  extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "subledger_Id", unique = true, nullable = false)
	private Long subledger_Id ;
	
	@Column(name = "subledger_name", nullable = true, length = MyAbstractController.SIZE_THREE_HUNDRED)
	private String subledger_name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ledger_id")
	@JsonIgnore
	private Ledger ledger ;
	
	@Column(name = "credit_opening_balance", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Float credit_opening_balance;
	
	@Column(name = "debit_opening_balance", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Float debit_opening_balance;
	
	@Transient
	private String credit_opening_balance1;
	
	@Transient
	private String debit_opening_balance1;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="industry_type_subledger_master",joinColumns=@JoinColumn(name="subLedgers_subledger_Id",referencedColumnName="subledger_Id"),
	inverseJoinColumns=@JoinColumn(name="industry_type_industry_id",referencedColumnName="industry_id"))
	@JsonIgnore
	private Set<IndustryType> type = new HashSet<IndustryType>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="supplier_master_subledger_master",joinColumns=@JoinColumn(name="subLedgers_subledger_Id",referencedColumnName="subledger_Id"),
	inverseJoinColumns=@JoinColumn(name="supplier_master_supplier_id",referencedColumnName="supplier_id"))
	@JsonIgnore
	private Set<Suppliers> supplier = new HashSet<Suppliers>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="customer_master_subledger_master",joinColumns=@JoinColumn(name="subLedgers_subledger_Id",referencedColumnName="subledger_Id"),
	inverseJoinColumns=@JoinColumn(name="customer_master_customer_id",referencedColumnName="customer_id"))
	@JsonIgnore
	private Set<Customer> customer = new HashSet<Customer>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	@JsonIgnore
	private Company company ;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "subledger")
	@JsonIgnore
	private Set<PurchaseEntry> purchaseEntry = new HashSet<PurchaseEntry>();

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "subledger")
	@JsonIgnore
	private Set<SalesEntry> salesEntry = new HashSet<SalesEntry>();
	 
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "subLedger")
	@JsonIgnore
	private Set<Payment> payment = new HashSet<Payment>();
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "subLedger")
	@JsonIgnore
	private Set<Receipt> receipt = new HashSet<Receipt>();
	
	@Transient
	private Long ledger_id ; 
	
	@Transient
	private Long company_id ;

	@Column(name = "from_mobile", nullable = true)
	private Boolean from_mobile ;
 
	@Column(name = "status", nullable = true)
	private Boolean status ;
  
	@Column(name = "allocated", nullable = true)
	private Boolean allocated ;
	
	@Column(name = "created_date", nullable = true)
	private LocalDate created_date; 
  
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	@JsonIgnore
	private User created_by;
  
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	@JsonIgnore
	private User updated_by;
  
    @Column(name = "update_date", nullable = true)
    private LocalDate  update_date;

    @Column(name = "subledger_approval", nullable = true, length = MyAbstractController.SIZE_TWENTY)
    private Integer subledger_approval ;

	@Column(name = "flag")
	private Boolean flag;
	
	@Column(name = "setDefault")
	private Boolean setDefault;	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "opening_id", referencedColumnName = "opening_id", insertable = true, updatable = true)
	@JsonIgnore
	private OpeningBalances openingbalances;
	
	@Transient
	private Long openingId ;
	
	@Transient
	private List<String> subledgerList ;
	
	@Transient
	private Boolean primaryApproval;
	
	@Transient
	private Boolean rejectApproval;
	
	@Transient
	private Double depSubLedgerAmount;
	
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subledger_Id == null) ? 0 : subledger_Id.hashCode());
		result = prime * result + ((subledger_name == null) ? 0 : subledger_name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubLedger other = (SubLedger) obj;
		if (subledger_Id == null) {
			if (other.subledger_Id != null)
				return false;
		} else if (!subledger_Id.equals(other.subledger_Id))
			return false;
		if (subledger_name == null) {
			if (other.subledger_name != null)
				return false;
		} else if (!subledger_name.equals(other.subledger_name))
			return false;
		return true;
	}
	
	public Set<Suppliers> getSupplier() {
		return supplier;
	}

	public void setSupplier(Set<Suppliers> supplier) {
		this.supplier = supplier;
	}
	public Set<IndustryType> getType() {
		return type;
	}

	public void setType(Set<IndustryType> type) {
		this.type = type;
	}

	public Long getSubledger_Id() {
		return subledger_Id;
	}

	public void setSubledger_Id(Long subledger_Id) {
		this.subledger_Id = subledger_Id;
	}

	public Ledger getLedger() {
		return ledger;
	}

	public void setLedger(Ledger ledger) {
		this.ledger = ledger;
	}

	public Long getLedger_id() {
		return ledger_id;
	}

	public void setLedger_id(Long ledger_id) {
		this.ledger_id = ledger_id;
	}

	public String getSubledger_name() {
		return subledger_name;
	}

	public void setSubledger_name(String subledger_name) {
		this.subledger_name = subledger_name;
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

	public Set<Customer> getCustomer() {
		return customer;
	}

	public void setCustomer(Set<Customer> customer) {
		this.customer = customer;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public LocalDate getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(LocalDate update_date) {
		this.update_date = update_date;
	}

	public Set<PurchaseEntry> getPurchaseEntry() {
		return purchaseEntry;
	}

	public void setPurchaseEntry(Set<PurchaseEntry> purchaseEntry) {
		this.purchaseEntry = purchaseEntry;
	}

	public Set<SalesEntry> getSalesEntry() {
		return salesEntry;
	}

	public void setSalesEntry(Set<SalesEntry> salesEntry) {
		this.salesEntry = salesEntry;
	}

	public Set<Payment> getPayment() {
		return payment;
	}

	public void setPayment(Set<Payment> payment) {
		this.payment = payment;
	}

	public Set<Receipt> getReceipt() {
		return receipt;
	}

	public void setReceipt(Set<Receipt> receipt) {
		this.receipt = receipt;
	}

	public Integer getSubledger_approval() {
		return subledger_approval;
	}

	public void setSubledger_approval(Integer subledger_approval) {
		this.subledger_approval = subledger_approval;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public User getCreated_by() {
		return created_by;
	}

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	public User getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(User updated_by) {
		this.updated_by = updated_by;
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

	public Boolean getSetDefault() {
		return setDefault;
	}

	public void setSetDefault(Boolean setDefault) {
		this.setDefault = setDefault;
	}

	public Boolean getAllocated() {
		return allocated;
	}

	public void setAllocated(Boolean allocated) {
		this.allocated = allocated;
	}

	public List<String> getSubledgerList() {
		return subledgerList;
	}

	public void setSubledgerList(List<String> subledgerList) {
		this.subledgerList = subledgerList;
	}

	public Boolean getPrimaryApproval() {
		return primaryApproval;
	}

	public void setPrimaryApproval(Boolean primaryApproval) {
		this.primaryApproval = primaryApproval;
	}

	public Boolean getRejectApproval() {
		return rejectApproval;
	}

	public void setRejectApproval(Boolean rejectApproval) {
		this.rejectApproval = rejectApproval;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public Double getDepSubLedgerAmount() {
		return depSubLedgerAmount;
	}

	public void setDepSubLedgerAmount(Double depSubLedgerAmount) {
		this.depSubLedgerAmount = depSubLedgerAmount;
	}

	
	
}
