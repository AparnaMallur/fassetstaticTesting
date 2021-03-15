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
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;
import com.fasset.form.ProductInformation;

@Entity
@Table(name = "receipt")
public class Receipt extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "receipt_id", unique = true, nullable = false)
    private Long receipt_id ;
	
	@Column(name = "voucher_no")
	private String voucher_no ;
	
	@Column(name = "excel_voucher_no")
	private String excel_voucher_no ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer customer ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accountingYear;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subledger_id")
	private SubLedger subLedger;
	
	@Transient
	private Long customer_id ;
	
	@Transient
	private Long product_id ;
	
	@Transient
	private Long year_id ;

	@Transient
	private List<Receipt_product_details> productinfoList;
	
	// this will be used when sales is generated against advance receipt and if sales amount is less than actual advance receipt voucher then new receipt voucher is created and original receipt reference is added to the new generated receipt voucher.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "advreceipt_id")
	private Receipt advreceipt;
	
	/*@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "advreceipt")
	private Set<Receipt> multipleReceiptsAgainstAdvance = new HashSet<Receipt>();*/
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sales_bill_id")
	private SalesEntry sales_bill_id;
	
	@Transient
	private Long salesEntryId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bank")
	private Bank bank;
	
	@Transient
	private Long bankId;
	
	@Column(name = "date", nullable = true)
	//@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate date ;
	
	@Column(name = "cheque_no", nullable = true, length = MyAbstractController.SIZE_HUNDRED)
	private String cheque_no ;
	
	@Column(name = "cheque_date", nullable = true, length = MyAbstractController.SIZE_HUNDRED)
	private String cheque_date ;
	
	@Column(name = "amount", nullable = true)
	private Double amount ;
	
	@Column(name = "other_remark", nullable = true, length = MyAbstractController.SIZE_THOUSAND)
	private String other_remark ;
	
	@Column(name = "advance_payment", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Boolean advance_payment ;
	
	@Column(name = "gst_applied", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Integer gst_applied ;
	
	@Column(name = "payment_type", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Integer payment_type ;
	
	@Column(name = "hsn_code", nullable = true, length = MyAbstractController.SIZE_HUNDRED)
	private String hsn_code ;
		
	@Column(name = "cgst", nullable = true)
	private Double cgst ;
	
	@Column(name = "sgst", nullable = true)
	private Double sgst ;
	
	@Column(name = "igst", nullable = true)
	private Double igst ;
	
	@Column(name = "tds_amount", nullable = true)
	private Double tds_amount ;

	@Column(name = "tds_paid", nullable = true)
	private Boolean tds_paid ;
	
	@Column(name = "from_mobile", nullable = true)
	private Boolean from_mobile ;
	 
	@Column(name = "status", nullable = true)
	private Boolean status ;
	 
	@Column(name = "gst_type", nullable = true)
	private Boolean gst_type ;
	  
	@Column(name = "created_date", nullable = true)
	private LocalDate created_date; 
  
	@Column(name = "created_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Long created_by ; 
	
	@Column(name = "updated_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Long updated_by ; 
	  
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<Product> products = new HashSet<Product>();
		 
	@Column(name = "update_date", nullable = true)
	private LocalDateTime update_date;
	  
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;
  
	@Column(name ="round_off", nullable = true)
	private Double round_off;
	
	@Column(name="transaction_value", nullable = true)
	private Double transaction_value ;
  		 
	@Column(name = "state_compansation_tax", nullable = true)
	private Double state_compansation_tax ;
	
	//new
	@Column(name = "againstOpeningBalnce", nullable = true)
	private Boolean againstOpeningBalnce ;
  
	//new
	@Transient
	private Long receiptagainstOpeningBalnce ;
	
	@Transient
	private Long subledgerId ;
  		
	@Transient
	private String productInformationList ;	

	@Transient
	private List<ProductInformation> informationList;

	@Transient
	private Long save_id;  
	  
	@Column(name = "total_vat", nullable = true)
	private Double total_vat;

	@Column(name = "total_vatcst", nullable = true)
	private Double total_vatcst;

	@Column(name = "total_excise", nullable = true)
	private Double total_excise;
	
	@Transient
	private String voucher_range;

	@Column(name = "flag")
	private Boolean flag;
	 
	@Column(name = "entry_status")
	private Integer entry_status;
	 
	@Column(name = "local_time", nullable = true)
	private LocalTime local_time;
	
	//set as true if sales entry is generated against receipt
	@Column(name = "is_salegenrated", nullable = true)
	private Boolean is_salegenrated ;
	
	// set as true if receipt is generated against sales voucher and receipt amount is greater than sales amount
	@Column(name = "is_extraAdvanceReceived", nullable = true)
	private Boolean is_extraAdvanceReceived;
	
	@Transient
	private Integer agingDays;
	
	public AccountingYear getAccountingYear() {
		return accountingYear;
	}

	public void setAccountingYear(AccountingYear accountingYear) {
		this.accountingYear = accountingYear;
	}

	public Long getYear_id() {
		return year_id;
	}

	public void setYear_id(Long year_id) {
		this.year_id = year_id;
	}

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public Long getReceipt_id() {
		return receipt_id;
	}

	public void setReceipt_id(Long receipt_id) {
		this.receipt_id = receipt_id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}	

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getCheque_no() {
		return cheque_no;
	}

	public void setCheque_no(String cheque_no) {
		this.cheque_no = cheque_no;
	}

	public String getCheque_date() {
		return cheque_date;
	}

	public void setCheque_date(String cheque_date) {
		this.cheque_date = cheque_date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getOther_remark() {
		return other_remark;
	}

	public void setOther_remark(String other_remark) {
		this.other_remark = other_remark;
	}

	public Integer getGst_applied() {
		return gst_applied;
	}

	public void setGst_applied(Integer gst_applied) {
		this.gst_applied = gst_applied;
	}

	public String getHsn_code() {
		return hsn_code;
	}

	public void setHsn_code(String hsn_code) {
		this.hsn_code = hsn_code;
	}

	
	public Double getCgst() {
		return cgst;
	}

	public void setCgst(Double cgst) {
		this.cgst = cgst;
	}

	public Double getSgst() {
		return sgst;
	}

	public void setSgst(Double sgst) {
		this.sgst = sgst;
	}

	public Double getIgst() {
		return igst;
	}

	public void setIgst(Double igst) {
		this.igst = igst;
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

	public Double getRound_off() {
		return round_off;
	}

	public void setRound_off(Double round_off) {
		this.round_off = round_off;
	}

	public Double getTransaction_value() {
		return transaction_value;
	}

	public void setTransaction_value(Double transaction_value) {
		this.transaction_value = transaction_value;
	}

	public Double getState_compansation_tax() {
		return state_compansation_tax;
	}

	public void setState_compansation_tax(Double state_compansation_tax) {
		this.state_compansation_tax = state_compansation_tax;
	}

	public String getProductInformationList() {
		return productInformationList;
	}

	public void setProductInformationList(String productInformationList) {
		this.productInformationList = productInformationList;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	public void setInformationList(List<ProductInformation> informationList) {
		this.informationList = informationList;
	}
	public List<ProductInformation> getInformationList() {
		return informationList;
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

	public Long getSave_id() {
		return save_id;
	}

	public void setSave_id(Long save_id) {
		this.save_id = save_id;
	}

	public Boolean getAdvance_payment() {
		return advance_payment;
	}

	public void setAdvance_payment(Boolean advance_payment) {
		this.advance_payment = advance_payment;
	}

	public SalesEntry getSales_bill_id() {
		return sales_bill_id;
	}

	public void setSales_bill_id(SalesEntry sales_bill_id) {
		this.sales_bill_id = sales_bill_id;
	}

	public Long getSalesEntryId() {
		return salesEntryId;
	}

	public void setSalesEntryId(Long salesEntryId) {
		this.salesEntryId = salesEntryId;
	}

	public SubLedger getSubLedger() {
		return subLedger;
	}

	public void setSubLedger(SubLedger subLedger) {
		this.subLedger = subLedger;
	}

	public Long getSubledgerId() {
		return subledgerId;
	}

	public void setSubledgerId(Long subledgerId) {
		this.subledgerId = subledgerId;
	}

	public Integer getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(Integer payment_type) {
		this.payment_type = payment_type;
	}

	public String getVoucher_no() {
		return voucher_no;
	}

	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}

	public Double getTotal_vat() {
		return total_vat;
	}

	public void setTotal_vat(Double total_vat) {
		this.total_vat = total_vat;
	}

	public Double getTotal_vatcst() {
		return total_vatcst;
	}

	public void setTotal_vatcst(Double total_vatcst) {
		this.total_vatcst = total_vatcst;
	}

	public Double getTotal_excise() {
		return total_excise;
	}

	public void setTotal_excise(Double total_excise) {
		this.total_excise = total_excise;
	}

	public String getVoucher_range() {
		return voucher_range;
	}

	public void setVoucher_range(String voucher_range) {
		this.voucher_range = voucher_range;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Boolean getGst_type() {
		return gst_type;
	}

	public void setGst_type(Boolean gst_type) {
		this.gst_type = gst_type;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Integer getEntry_status() {
		return entry_status;
	}

	public void setEntry_status(Integer entry_status) {
		this.entry_status = entry_status;
	}

	public Double getTds_amount() {
		return tds_amount;
	}

	public void setTds_amount(Double tds_amount) {
		this.tds_amount = tds_amount;
	}

	public Receipt getAdvreceipt() {
		return advreceipt;
	}

	public void setAdvreceipt(Receipt advreceipt) {
		this.advreceipt = advreceipt;
	}

	public String getExcel_voucher_no() {
		return excel_voucher_no;
	}

	public void setExcel_voucher_no(String excel_voucher_no) {
		this.excel_voucher_no = excel_voucher_no;
	}

	public List<Receipt_product_details> getProductinfoList() {
		return productinfoList;
	}

	public void setProductinfoList(List<Receipt_product_details> productinfoList) {
		this.productinfoList = productinfoList;
	}

	public Boolean getTds_paid() {
		return tds_paid;
	}

	public void setTds_paid(Boolean tds_paid) {
		this.tds_paid = tds_paid;
	}

	public LocalTime getLocal_time() {
		return local_time;
	}

	public void setLocal_time(LocalTime local_time) {
		this.local_time = local_time;
	}

	public Boolean getIs_salegenrated() {
		return is_salegenrated;
	}

	public void setIs_salegenrated(Boolean is_salegenrated) {
		this.is_salegenrated = is_salegenrated;
	}

	public Integer getAgingDays() {
		return agingDays;
	}

	public void setAgingDays(Integer agingDays) {
		this.agingDays = agingDays;
	}

	public Boolean getIs_extraAdvanceReceived() {
		return is_extraAdvanceReceived;
	}

	public void setIs_extraAdvanceReceived(Boolean is_extraAdvanceReceived) {
		this.is_extraAdvanceReceived = is_extraAdvanceReceived;
	}

	public Boolean getAgainstOpeningBalnce() {
		return againstOpeningBalnce;
	}

	public void setAgainstOpeningBalnce(Boolean againstOpeningBalnce) {
		this.againstOpeningBalnce = againstOpeningBalnce;
	}

	public Long getReceiptagainstOpeningBalnce() {
		return receiptagainstOpeningBalnce;
	}

	public void setReceiptagainstOpeningBalnce(Long receiptagainstOpeningBalnce) {
		this.receiptagainstOpeningBalnce = receiptagainstOpeningBalnce;
	}

/*	public Set<Receipt> getMultipleReceiptsAgainstAdvance() {
		return multipleReceiptsAgainstAdvance;
	}

	public void setMultipleReceiptsAgainstAdvance(Set<Receipt> multipleReceiptsAgainstAdvance) {
		this.multipleReceiptsAgainstAdvance = multipleReceiptsAgainstAdvance;
	}*/



}
