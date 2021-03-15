package com.fasset.entities;

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

import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "debit_note")
public class DebitNote extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "debit_no_id")
	private Long debit_no_id;

	@Column(name = "voucher_no")
	private String voucher_no;
	
	@Column(name = "excel_voucher_no")
	private String excel_voucher_no ;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplier")
	private Suppliers supplier;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "debit_id", cascade = CascadeType.ALL)
	private Set<DebitDetails> debitDetails;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subledger")
	private SubLedger subledger;

	@Transient
	private Long subledgerId;

	@Transient
	private Long supplierId;

	@Column(name = "date")
	private LocalDate date;

	@Transient
	private String dateString;

	@Column(name = "remark")
	private String remark;

	@Column(name = "description")
	private Integer description;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "purchase_bill_id")
	private PurchaseEntry purchase_bill_id;

	@Transient
	private Long purchaseEntryId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accounting_year_id;

	@Transient
	private Long yearId;

	@Column(name = "status")
	private Boolean status;

	@Column(name = "updated_date")
	private LocalDate updated_date;

	@Column(name = "created_by")
	private Long created_by;

	@Column(name = "updated_by")
	private Long updated_by;
	
	@Column(name = "created_date")
	private LocalDate created_date;

	

	@Column(name = "from_mobile")
	private Boolean from_mobile;

	@Column(name = "ip_address")
	private String ip_address;

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

	@Transient
	private String debitNoteDetails;

	@Column(name = "total_vat", nullable = true)
	private Double total_vat;

	@Column(name = "total_vatcst", nullable = true)
	private Double total_vatcst;

	@Column(name = "total_excise", nullable = true)
	private Double total_excise;

	@Column(name = "entry_status")
	private Integer entry_status;

	@Transient
	private String voucher_range;

	@Column(name = "tds_amount", nullable = true)
	private Double tds_amount;
	

	@Column(name = "flag")
	private Boolean flag;
	@Column(name = "againstOpeningBalnce", nullable = true)
	private Boolean againstOpeningBalnce ;
	
	@Column(name = "local_time", nullable = true)
	private LocalTime local_time;
	
	
	public Double getTds_amount() {
		return tds_amount;
	}

	public void setTds_amount(Double tds_amount) {
		this.tds_amount = tds_amount;
	}
	
	public Long getDebit_no_id() {
		return debit_no_id;
	}

	public void setDebit_no_id(Long debit_no_id) {
		this.debit_no_id = debit_no_id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public AccountingYear getAccounting_year_id() {
		return accounting_year_id;
	}

	public void setAccounting_year_id(AccountingYear accounting_year_id) {
		this.accounting_year_id = accounting_year_id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
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

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
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

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public PurchaseEntry getPurchase_bill_id() {
		return purchase_bill_id;
	}

	public void setPurchase_bill_id(PurchaseEntry purchase_bill_id) {
		this.purchase_bill_id = purchase_bill_id;
	}

	public Long getPurchaseEntryId() {
		return purchaseEntryId;
	}

	public void setPurchaseEntryId(Long purchaseEntryId) {
		this.purchaseEntryId = purchaseEntryId;
	}

	public Integer getDescription() {
		return description;
	}

	public void setDescription(Integer description) {
		this.description = description;
	}

	public Set<DebitDetails> getDebitDetails() {
		return debitDetails;
	}

	public void setDebitDetails(Set<DebitDetails> debitDetails) {
		this.debitDetails = debitDetails;
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

	public Double getRound_off() {
		return round_off;
	}

	public void setRound_off(Double round_off) {
		this.round_off = round_off;
	}

	public Double getSCT_head() {
		return SCT_head;
	}

	public void setSCT_head(Double sCT_head) {
		SCT_head = sCT_head;
	}

	public String getDebitNoteDetails() {
		return debitNoteDetails;
	}

	public void setDebitNoteDetails(String debitNoteDetails) {
		this.debitNoteDetails = debitNoteDetails;
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

	public SubLedger getSubledger() {
		return subledger;
	}

	public void setSubledger(SubLedger subledger) {
		this.subledger = subledger;
	}

	public Long getSubledgerId() {
		return subledgerId;
	}

	public void setSubledgerId(Long subledgerId) {
		this.subledgerId = subledgerId;
	}

	public String getExcel_voucher_no() {
		return excel_voucher_no;
	}

	public void setExcel_voucher_no(String excel_voucher_no) {
		this.excel_voucher_no = excel_voucher_no;
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
	
	public Boolean getAgainstOpeningBalnce() {
		return againstOpeningBalnce;
	}

	public void setAgainstOpeningBalnce(Boolean againstOpeningBalnce) {
		this.againstOpeningBalnce = againstOpeningBalnce;
	}

}