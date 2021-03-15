/**
0 * mayur suramwar
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
@Table(name = "sales_Entry")
public class SalesEntry extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "sales_id", unique = true, nullable = false)
	private Long sales_id;

	@Column(name = "voucher_no")
	private String voucher_no;

	@Column(name = "excel_voucher_no")
	private String excel_voucher_no;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subledger_Id")
	private SubLedger subledger;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accountingYear;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bank")
	private Bank bank;

	@Transient
	private Long bankId;

	@Transient
	private List<SalesEntryProductEntityClass> productinfoList;

	@Column(name = "sale_type")
	private Integer sale_type;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Product> products = new HashSet<Product>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receipt_id")
	private Receipt advreceipt;

	@Column(name = "againstOpeningBalnce")
	private Boolean againstOpeningBalnce;

	public Boolean getAgainstOpeningBalnce() {
		return againstOpeningBalnce;
	}

	public void setAgainstOpeningBalnce(Boolean againstOpeningBalnce) {
		this.againstOpeningBalnce = againstOpeningBalnce;
	}

	public Long getAgainstOpeningbalanceId() {
		return againstOpeningbalanceId;
	}

	public void setAgainstOpeningbalanceId(Long againstOpeningbalanceId) {
		this.againstOpeningbalanceId = againstOpeningbalanceId;
	}

	@Transient

	private Long againstOpeningbalanceId;

	@Transient
	private Long save_id;

	@Transient
	private Long customer_id;

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

	@Column(name = "customer_bill_date", nullable = true)
	private LocalDate customer_bill_date;

	@Column(name = "customer_bill_no", nullable = true, length = MyAbstractController.SIZE_HUNDRED)
	private String customer_bill_no;

	@Transient
	private String voucher_range;

	@Column(name = "transaction_value", nullable = true)
	private Double transaction_value;

	@Column(name = "cgst", nullable = true)
	private Double cgst;

	@Column(name = "igst", nullable = true)
	private Double igst;

	@Column(name = "sgst", nullable = true)
	private Double sgst;

	@Column(name = "total_vat", nullable = true)
	private Double total_vat;

	@Column(name = "total_vatcst", nullable = true)
	private Double total_vatcst;

	@Column(name = "total_excise", nullable = true)
	private Double total_excise;

	@Column(name = "state_compansation_tax", nullable = true)
	private Double state_compansation_tax;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "sales_bill_id")
	private Set<Receipt> receipts = new HashSet<Receipt>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "sales_bill_id")
	private Set<CreditNote> creditNote = new HashSet<CreditNote>();

	@Transient
	private Long receiptId;

	@Column(name = "tds_amount", nullable = true)
	private Double tds_amount;

	public Receipt getAdvreceipt() {
		return advreceipt;
	}

	public void setAdvreceipt(Receipt advreceipt) {
		this.advreceipt = advreceipt;
	}

	public Set<CreditNote> getCreditNote() {
		return creditNote;
	}

	public void setCreditNote(Set<CreditNote> creditNote) {
		this.creditNote = creditNote;
	}

	@Column(name = "against_advance_receipt", nullable = true)
	private Boolean against_advance_receipt;

	@Column(name = "round_off", nullable = true)
	private Double round_off;

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

	@Column(name = "flag")
	private Boolean flag;

	@Column(name = "local_time", nullable = true)
	private LocalTime local_time;

	@Column(name = "entry_status")
	private Integer entry_status;

	public Long getSales_id() {
		return sales_id;
	}

	public void setSales_id(Long sales_id) {
		this.sales_id = sales_id;
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public LocalDate getCustomer_bill_date() {
		return customer_bill_date;
	}

	public void setCustomer_bill_date(LocalDate customer_bill_date) {
		this.customer_bill_date = customer_bill_date;
	}

	public String getCustomer_bill_no() {
		return customer_bill_no;
	}

	public void setCustomer_bill_no(String customer_bill_no) {
		this.customer_bill_no = customer_bill_no;
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

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
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

	public Long getSave_id() {
		return save_id;
	}

	public void setSave_id(Long save_id) {
		this.save_id = save_id;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public Boolean getAgainst_advance_receipt() {
		return against_advance_receipt;
	}

	public void setAgainst_advance_receipt(Boolean against_advance_receipt) {
		this.against_advance_receipt = against_advance_receipt;
	}

	public Long getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}

	public Set<Receipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(Set<Receipt> receipts) {
		this.receipts = receipts;
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

	@Override
	public String toString() {
		return "SalesEntry [sales_id=" + sales_id + ", round_off=" + round_off + "]";
	}

	public String getVoucher_no() {
		return voucher_no;
	}

	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
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

	public Integer getSale_type() {
		return sale_type;
	}

	public void setSale_type(Integer sale_type) {
		this.sale_type = sale_type;
	}

	public String getExcel_voucher_no() {
		return excel_voucher_no;
	}

	public void setExcel_voucher_no(String excel_voucher_no) {
		this.excel_voucher_no = excel_voucher_no;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SalesEntryProductEntityClass> getProductinfoList() {
		return productinfoList;
	}

	public void setProductinfoList(List<SalesEntryProductEntityClass> productinfoList) {
		this.productinfoList = productinfoList;
	}

	public LocalTime getLocal_time() {
		return local_time;
	}

	public void setLocal_time(LocalTime local_time) {
		this.local_time = local_time;
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

	public Double getState_compansation_tax() {
		return state_compansation_tax;
	}

	public void setState_compansation_tax(Double state_compansation_tax) {
		this.state_compansation_tax = state_compansation_tax;
	}

	public Double getTds_amount() {
		return tds_amount;
	}

	public void setTds_amount(Double tds_amount) {
		this.tds_amount = tds_amount;
	}

	public Double getRound_off() {
		return round_off;
	}

	public void setRound_off(Double round_off) {
		this.round_off = round_off;
	}

}