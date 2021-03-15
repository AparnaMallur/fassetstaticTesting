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
@Table(name = "credit_note")
public class CreditNote extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "credit_no_id")
	private Long credit_no_id;

	@Column(name = "voucher_no")
	private String voucher_no;
	
	@Column(name = "excel_voucher_no")
	private String excel_voucher_no ;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subledger")
	private SubLedger subledger;

	@Transient
	private Long subledgerId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	@Transient
	private Long customerId;

	@Column(name = "date")
	private LocalDate date;

	@Transient
	private String dateString;

	@Column(name = "remark")
	private String remark;

	@Column(name = "description")
	private Integer description;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sales_bill_id")
	private SalesEntry sales_bill_id;

	@Transient
	private Long salesEntryId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accounting_year_id;

	@Transient
	private Long accountYearId;

	@Column(name = "status")
	private Boolean status;

	@Column(name = "updated_date")
	private LocalDate updated_date;

	@Column(name = "created_by")
	private Long created_by;

	@Column(name = "created_date")
	private LocalDate created_date;

	
	@Column(name = "updated_by")
	private Long updated_by;

	@Column(name = "from_mobile")
	private Boolean from_mobile;

	@Column(name = "ip_address")
	private String ip_address;

	@Transient
	private String creditNoteDetails;

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

	@Column(name = "total_vat", nullable = true)
	private Double total_vat;

	@Column(name = "total_vatcst", nullable = true)
	private Double total_vatcst;

	@Column(name = "total_excise", nullable = true)
	private Double total_excise;

	@Column(name = "flag")
	private Boolean flag;

	@Column(name = "entry_status")
	private Integer entry_status;

	@Transient
	private String voucher_range;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "credit_id", cascade = CascadeType.ALL)
	private Set<CreditDetails> creditDetails;

	@Column(name = "local_time", nullable = true)
	private LocalTime local_time;
	
	@Column(name = "tds_amount", nullable = true)
	private Double tds_amount;
	
	@Column(name = "againstOpeningBalnce", nullable = true)
	private Boolean againstOpeningBalnce ;
	public Double getTds_amount() {
		return tds_amount;
	}

	public void setTds_amount(Double tds_amount) {
		this.tds_amount = tds_amount;
	}

	public Long getCredit_no_id() {
		return credit_no_id;
	}

	public void setCredit_no_id(Long credit_no_id) {
		this.credit_no_id = credit_no_id;
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

	public Long getAccountYearId() {
		return accountYearId;
	}

	public void setAccountYearId(Long accountYearId) {
		this.accountYearId = accountYearId;
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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

	public Integer getDescription() {
		return description;
	}

	public void setDescription(Integer description) {
		this.description = description;
	}

	public String getCreditNoteDetails() {
		return creditNoteDetails;
	}

	public void setCreditNoteDetails(String creditNoteDetails) {
		this.creditNoteDetails = creditNoteDetails;
	}

	public Set<CreditDetails> getCreditDetails() {
		return creditDetails;
	}

	public void setCreditDetails(Set<CreditDetails> creditDetails) {
		this.creditDetails = creditDetails;
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
