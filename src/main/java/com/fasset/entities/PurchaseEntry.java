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

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Entity
@Table(name = "purchase_Entry")
public class PurchaseEntry extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "purchase_id", unique = true, nullable = false)
	private Long purchase_id;

	@Column(name = "voucher_no")
	private String voucher_no;
	
	@Column(name = "excel_voucher_no")
	private String excel_voucher_no ;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplier_id")
	private Suppliers supplier;

	@Transient
	private Long save_id;

	@Transient
	private Long supplier_id;

	@Transient
	private Long subledger_Id;

	@Transient
	private Long product_id;

	@Transient
	private Long accounting_year_id;

	@Transient
	private String productInformationList;

	@Transient
	List<ProductInformation> informationList;
	
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "payment_id")
	 private Payment advpayment;
	 
	 
	public Payment getAdvpayment() {
		return advpayment;
	}

	public void setAdvpayment(Payment advpayment) {
		this.advpayment = advpayment;
	}

	@Transient
	private List<PurchaseEntryProductEntityClass> productinfoList;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subledger_Id")
	private SubLedger subledger;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accountingYear;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Product> products = new HashSet<Product>();

	@Column(name = "supplier_bill_no", nullable = true, length = MyAbstractController.SIZE_HUNDRED)
	private String supplier_bill_no;

	@Column(name = "voucher_date", nullable = true)
	private LocalDate voucher_date;

	@Column(name = "grn_date", nullable = true)
	private LocalDate grn_date;

	@Column(name = "supplier_bill_date", nullable = true)
	private LocalDate supplier_bill_date;

	@Column(name = "round_off", nullable = true)
	private Double round_off;

	@Column(name = "transaction_value", nullable = true)
	private Double transaction_value;

	@Column(name = "cgst", nullable = true)
	private Double cgst;

	@Column(name = "igst", nullable = true)
	private Double igst;

	@Column(name = "sgst", nullable = true)
	private Double sgst;

	@Column(name = "state_compansation_tax", nullable = true)
	private Double state_compansation_tax;

	@Transient
	private Long paymentId;

	@Column(name = "against_advance_Payment")
	private Boolean against_advance_Payment;
	
	

	@Column(name = "againstOpeningBalnce")
	private Boolean againstOpeningBalnce;

	@Transient
	private Long againstOpeningbalanceId;

	public Long getAgainstOpeningbalanceId() {
		return againstOpeningbalanceId;
	}

	public void setAgainstOpeningbalanceId(Long againstOpeningbalanceId) {
		this.againstOpeningbalanceId = againstOpeningbalanceId;
	}

	public Boolean getAgainstOpeningBalnce() {
		return againstOpeningBalnce;
	}

	public void setAgainstOpeningBalnce(Boolean againstOpeningBalnce) {
		this.againstOpeningBalnce = againstOpeningBalnce;
	}

	@Column(name = "remark", nullable = true, length = MyAbstractController.SIZE_THOUSAND)
	private String remark;

	@Column(name = "entry_type", nullable = true, length = MyAbstractController.SIZE_THOUSAND)
	private Integer entrytype;

	@Column(name = "shipping_bill_no", nullable = true, length = MyAbstractController.SIZE_THOUSAND)
	private String shipping_bill_no;

	@Column(name = "shipping_bill_date", nullable = true)
	private LocalDate shipping_bill_date;

	@Column(name = "export_type", nullable = true, length = MyAbstractController.SIZE_THOUSAND)
	private Integer export_type;

	@Column(name = "port_code", nullable = true, length = MyAbstractController.SIZE_THOUSAND)
	private String port_code;

	@Column(name = "status", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean status;

	@Column(name = "created_date", nullable = true)
	private LocalDate created_date;

	@Column(name = "created_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Long created_by;

	@Column(name = "updated_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Long updated_by;

	@Column(name = "update_date", nullable = true)
	private LocalDateTime update_date;

	@Column(name = "ip_address", nullable = true)
	private String ip_address;

	@Column(name = "from_mobile", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean from_mobile;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "supplier_bill_no")
	private Set<Payment> payments = new HashSet<Payment>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "purchase_bill_id")
	private Set<DebitNote> debitNote = new HashSet<DebitNote>();	

	@Column(name = "local_time", nullable = true)
	private LocalTime local_time;
	
	public Set<DebitNote> getDebitNote() {
		return debitNote;
	}

	public void setDebitNote(Set<DebitNote> debitNote) {
		this.debitNote = debitNote;
	}

	@Column(name = "total_vat", nullable = true)
	private Double total_vat;

	@Column(name = "total_vatcst", nullable = true)
	private Double total_vatcst;

	@Column(name = "total_excise", nullable = true)
	private Double total_excise;

	@Transient
	private String voucher_range;
	
	@Column(name = "tds_amount", nullable = true)
	private Double tds_amount;
	
	@Column(name = "flag")
	private Boolean flag;

	@Column(name = "entry_status")
	private Integer entry_status;

	public Long getPurchase_id() {
		return purchase_id;
	}

	public void setPurchase_id(Long purchase_id) {
		this.purchase_id = purchase_id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Suppliers getSupplier() {
		return supplier;
	}

	public void setSupplier(Suppliers supplier) {
		this.supplier = supplier;
	}

	public Long getSave_id() {
		return save_id;
	}

	public void setSave_id(Long save_id) {
		this.save_id = save_id;
	}

	public Long getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(Long supplier_id) {
		this.supplier_id = supplier_id;
	}

	public Long getSubledger_Id() {
		return subledger_Id;
	}

	public void setSubledger_Id(Long subledger_Id) {
		this.subledger_Id = subledger_Id;
	}

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public Long getAccounting_year_id() {
		return accounting_year_id;
	}

	public void setAccounting_year_id(Long accounting_year_id) {
		this.accounting_year_id = accounting_year_id;
	}

	public String getProductInformationList() {
		return productInformationList;
	}

	public void setProductInformationList(String productInformationList) {
		this.productInformationList = productInformationList;
	}

	public List<ProductInformation> getInformationList() {
		return informationList;
	}

	public void setInformationList(List<ProductInformation> informationList) {
		this.informationList = informationList;
	}

	public SubLedger getSubledger() {
		return subledger;
	}

	public void setSubledger(SubLedger subledger) {
		this.subledger = subledger;
	}

	public AccountingYear getAccountingYear() {
		return accountingYear;
	}

	public void setAccountingYear(AccountingYear accountingYear) {
		this.accountingYear = accountingYear;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public String getSupplier_bill_no() {
		return supplier_bill_no;
	}

	public void setSupplier_bill_no(String supplier_bill_no) {
		this.supplier_bill_no = supplier_bill_no;
	}

	public LocalDate getVoucher_date() {
		return voucher_date;
	}

	public void setVoucher_date(LocalDate voucher_date) {
		this.voucher_date = voucher_date;
	}

	public LocalDate getGrn_date() {
		return grn_date;
	}

	public void setGrn_date(LocalDate grn_date) {
		this.grn_date = grn_date;
	}

	public LocalDate getSupplier_bill_date() {
		return supplier_bill_date;
	}

	public void setSupplier_bill_date(LocalDate supplier_bill_date) {
		this.supplier_bill_date = supplier_bill_date;
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

	public Double getCgst() {
		return cgst;
	}

	public void setCgst(Double cgst) {
		this.cgst = cgst;
	}

	public Double getIgst() {
		return igst;
	}

	public void setIgst(Double igst) {
		this.igst = igst;
	}

	public Double getSgst() {
		return sgst;
	}

	public void setSgst(Double sgst) {
		this.sgst = sgst;
	}

	public Double getState_compansation_tax() {
		return state_compansation_tax;
	}

	public void setState_compansation_tax(Double state_compansation_tax) {
		this.state_compansation_tax = state_compansation_tax;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
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

	public Boolean getFrom_mobile() {
		return from_mobile;
	}

	public void setFrom_mobile(Boolean from_mobile) {
		this.from_mobile = from_mobile;
	}

	public Boolean getAgainst_advance_Payment() {
		return against_advance_Payment;
	}

	public void setAgainst_advance_Payment(Boolean against_advance_Payment) {
		this.against_advance_Payment = against_advance_Payment;
	}

	/*
	 * @Override public String toString() { return "PurchaseEntry [purchase_id=" +
	 * purchase_id + ", voucher_no=" + voucher_no + "]"; }
	 */

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public Integer getEntrytype() {
		return entrytype;
	}

	public void setEntrytype(Integer entrytype) {
		this.entrytype = entrytype;
	}

	public String getShipping_bill_no() {
		return shipping_bill_no;
	}

	public void setShipping_bill_no(String shipping_bill_no) {
		this.shipping_bill_no = shipping_bill_no;
	}

	public LocalDate getShipping_bill_date() {
		return shipping_bill_date;
	}

	public void setShipping_bill_date(LocalDate shipping_bill_date) {
		this.shipping_bill_date = shipping_bill_date;
	}

	public Integer getExport_type() {
		return export_type;
	}

	public void setExport_type(Integer export_type) {
		this.export_type = export_type;
	}

	public String getPort_code() {
		return port_code;
	}

	public void setPort_code(String port_code) {
		this.port_code = port_code;
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

	public String getExcel_voucher_no() {
		return excel_voucher_no;
	}

	public void setExcel_voucher_no(String excel_voucher_no) {
		this.excel_voucher_no = excel_voucher_no;
	}

	public List<PurchaseEntryProductEntityClass> getProductinfoList() {
		return productinfoList;
	}

	public void setProductinfoList(List<PurchaseEntryProductEntityClass> productinfoList) {
		this.productinfoList = productinfoList;
	}

	public Double getTds_amount() {
		return tds_amount;
	}

	public void setTds_amount(Double tds_amount) {
		this.tds_amount = tds_amount;
	}

	public LocalTime getLocal_time() {
		return local_time;
	}

	public void setLocal_time(LocalTime local_time) {
		this.local_time = local_time;
	}

}