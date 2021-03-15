package com.fasset.entities;

import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.Transient;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "payment")
public class Payment extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "payment_id", unique = true, nullable = false)
	private Long payment_id;

	@Column(name = "voucher_no")
	private String voucher_no;
	
	@Column(name = "excel_voucher_no")
	private String excel_voucher_no ;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplier_id")
	private Suppliers supplier;

	@Transient
	private Long supplierId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplier_bill_no")
	private PurchaseEntry supplier_bill_no;

	@Transient
	private Long purchaseEntryId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subLedger")
	private SubLedger subLedger;

	@Transient
	private Long subLedgerId;

	@Transient
	private Integer group = null;

	@Column(name = "date")
	private LocalDate date;

	@Transient
	private String dateString;

	@Column(name = "payment_type")
	private Integer payment_type;

	@Column(name = "cheque_dd_no", nullable = true, length = MyAbstractController.SIZE_HUNDRED)
	private String cheque_dd_no;

	@Column(name = "cheque_date")
	private LocalDate cheque_date;

	@Transient
	private String chequeDateString;

	@Column(name = "amount", nullable = true)
	private Double amount;

	@Column(name = "other_remark", nullable = true, length = MyAbstractController.SIZE_THOUSAND)
	private String other_remark;

	@Column(name = "advance_payment")
	private Boolean advance_payment;
	
	@Column(name = "againstOpeningBalnce")
	private Boolean againstOpeningBalnce;

	

	public Boolean getAgainstOpeningBalnce() {
		return againstOpeningBalnce;
	}

	public void setAgainstOpeningBalnce(Boolean againstOpeningBalnce) {
		this.againstOpeningBalnce = againstOpeningBalnce;
	}

	@Column(name = "gst_applied", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Boolean gst_applied;

	@Column(name = "status", nullable = true)
	private Boolean status;

	@Column(name = "from_mobile", nullable = true)
	private Boolean from_mobile;

	
	@Column(name = "created_by")
	private Long created_by;

	@Column(name = "created_date", nullable = true)
	private LocalDate created_date;

	
	@Column(name = "updated_by")
	private Long updated_by;

	@Column(name = "update_date", nullable = true)
	private LocalDate update_date;

	@Column(name = "ip_address", nullable = true)
	private String ip_address;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accountingYear;

	@Transient
	private Long accountYearId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bank")
	private Bank bank;

	@Transient
	private Long bankId;

	@Transient
	private String voucher_range;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "payment_id", cascade = CascadeType.ALL)
	private Set<PaymentDetails> paymentDetails;

	@Transient
	private String payDetails;
	
	// this will be used when purchase is generated against advance payment and if purchase amount is less than actual advance payment voucher  then new payment voucher is created and original payment reference is added to the new generated payment voucher.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "advpayment_id")
	private Payment advpayment;
	

	// this will be used when purchase is generated against OPening balance  payment and if purchase amount is less than actual OPening balance payment voucher  then new payment voucher is created and original payment reference is added to the new generated payment voucher.

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "openingbalance_id")
	private Payment openingbalance;
	
	
	
	
	/*@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "advpayment")
	private Set<Payment> multiplePaymentsAgainstAdvance = new HashSet<Payment>();*/
	
	public Payment getOpeningbalance() {
		return openingbalance;
	}

	public void setOpeningbalance(Payment openingbalance) {
		this.openingbalance = openingbalance;
	}

	@Column(name = "CGST_head", nullable = true)
	private Double CGST_head;

	@Column(name = "SGST_head", nullable = true)
	private Double SGST_head;

	@Column(name = "IGST_head", nullable = true)
	private Double IGST_head;

	@Column(name = "transaction_value_head", nullable = true)
	private Double transaction_value_head;

	@Column(name = "round_off", nullable = true)
	private Double round_off;

	@Column(name = "SCT_head", nullable = true)
	private Double SCT_head;
	
	@Column(name = "tds_amount", nullable = true)
	private Double tds_amount;

	@Column(name = "tds_paid", nullable = true)
	private Boolean tds_paid ;
	
	@Column(name = "total_vat", nullable = true)
	private Double total_vat;

	@Column(name = "total_vatcst", nullable = true)
	private Double total_vatcst;

	@Column(name = "total_excise", nullable = true)
	private Double total_excise;

	@Column(name = "gst_type", nullable = true)
	private Boolean gst_type;

	@Column(name = "flag")
	private Boolean flag;

	@Column(name = "entry_status")
	private Integer entry_status;

	@Column(name = "local_time", nullable = true)
	private LocalTime local_time;
	
    //set as true if purchase entry is generated against payment
	@Column(name = "is_purchasegenrated", nullable = true)
	private Boolean is_purchasegenrated;
	
	// set as true if payment is generated against purchase voucher and payment amount is greater than purchase amount
	@Column(name = "is_extraAdvanceReceived", nullable = true)
	private Boolean is_extraAdvanceReceived;
	
	@Transient
	private Integer	 agingDays;
	
	public Long getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(Long payment_id) {
		this.payment_id = payment_id;
	}

	public Suppliers getSupplier() {
		return supplier;
	}

	public void setSupplier(Suppliers supplier) {
		this.supplier = supplier;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getCheque_date() {
		return cheque_date;
	}

	public void setCheque_date(LocalDate cheque_date) {
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

	public Boolean getAdvance_payment() {
		return advance_payment;
	}

	public void setAdvance_payment(Boolean advance_payment) {
		this.advance_payment = advance_payment;
	}

	public Boolean getGst_applied() {
		return gst_applied;
	}

	public void setGst_applied(Boolean gst_applied) {
		this.gst_applied = gst_applied;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getFrom_mobile() {
		return from_mobile;
	}

	public void setFrom_mobile(Boolean from_mobile) {
		this.from_mobile = from_mobile;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public AccountingYear getAccountingYear() {
		return accountingYear;
	}

	public void setAccountingYear(AccountingYear accountingYear) {
		this.accountingYear = accountingYear;
	}

	public Set<PaymentDetails> getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(Set<PaymentDetails> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getChequeDateString() {
		return chequeDateString;
	}

	public void setChequeDateString(String chequeDateString) {
		this.chequeDateString = chequeDateString;
	}

	public String getPayDetails() {
		return payDetails;
	}

	public void setPayDetails(String payDetails) {
		this.payDetails = payDetails;
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

	public PurchaseEntry getSupplier_bill_no() {
		return supplier_bill_no;
	}

	public void setSupplier_bill_no(PurchaseEntry supplier_bill_no) {
		this.supplier_bill_no = supplier_bill_no;
	}

	public Long getPurchaseEntryId() {
		return purchaseEntryId;
	}

	public void setPurchaseEntryId(Long purchaseEntryId) {
		this.purchaseEntryId = purchaseEntryId;
	}

	public LocalDate getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(LocalDate update_date) {
		this.update_date = update_date;
	}

	public Double getCGST_head() {
		return CGST_head;
	}

	public void setCGST_head(Double cGST_head) {
		CGST_head = cGST_head;
	}

	public Double getSGST_head() {
		return SGST_head;
	}

	public void setSGST_head(Double sGST_head) {
		SGST_head = sGST_head;
	}

	public Double getIGST_head() {
		return IGST_head;
	}

	public void setIGST_head(Double iGST_head) {
		IGST_head = iGST_head;
	}

	public Double getTransaction_value_head() {
		return transaction_value_head;
	}

	public void setTransaction_value_head(Double transaction_value_head) {
		this.transaction_value_head = transaction_value_head;
	}

	public Double getSCT_head() {
		return SCT_head;
	}

	public void setSCT_head(Double sCT_head) {
		SCT_head = sCT_head;
	}

	public Double getRound_off() {
		return round_off;
	}

	public void setRound_off(Double round_off) {
		this.round_off = round_off;
	}

	public Long getAccountYearId() {
		return accountYearId;
	}

	public void setAccountYearId(Long accountYearId) {
		this.accountYearId = accountYearId;
	}

	public SubLedger getSubLedger() {
		return subLedger;
	}

	public void setSubLedger(SubLedger subLedger) {
		this.subLedger = subLedger;
	}

	public Long getSubLedgerId() {
		return subLedgerId;
	}

	public void setSubLedgerId(Long subLedgerId) {
		this.subLedgerId = subLedgerId;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public String getCheque_dd_no() {
		return cheque_dd_no;
	}

	public void setCheque_dd_no(String cheque_dd_no) {
		this.cheque_dd_no = cheque_dd_no;
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

	public Payment getAdvpayment() {
		return advpayment;
	}

	public void setAdvpayment(Payment advpayment) {
		this.advpayment = advpayment;
	}

	public String getExcel_voucher_no() {
		return excel_voucher_no;
	}

	public void setExcel_voucher_no(String excel_voucher_no) {
		this.excel_voucher_no = excel_voucher_no;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIs_purchasegenrated() {
		return is_purchasegenrated;
	}

	public void setIs_purchasegenrated(Boolean is_purchasegenrated) {
		this.is_purchasegenrated = is_purchasegenrated;
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

/*	public Set<Payment> getMultiplePaymentsAgainstAdvance() {
		return multiplePaymentsAgainstAdvance;
	}

	public void setMultiplePaymentsAgainstAdvance(Set<Payment> multiplePaymentsAgainstAdvance) {
		this.multiplePaymentsAgainstAdvance = multiplePaymentsAgainstAdvance;
	}
*/


}