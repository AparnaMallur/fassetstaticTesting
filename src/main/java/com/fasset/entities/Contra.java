package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "contra")
public class Contra extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "transaction_id", unique = true, nullable = false)
    private Long transaction_id ;

    @Column(name = "voucher_no")
	private String voucher_no ;
   
    @Column(name = "excel_voucher_no")
	private String excel_voucher_no ;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "withdraw_from")
    private Bank withdraw_from;	  
  
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deposite_to")
    private Bank deposite_to;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cash")
    private SubLedger cash;
	 
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accounting_year_id;
	
	@Transient
	private Long accountYearId;
     
	@Transient
	private Long depositeTo;
     
	@Transient
	private Long withdrawFrom;
     
	@Column(name = "date", nullable = true)
	private LocalDate date ;
	  
	@Column(name ="amount", nullable = true)
	private Double amount;		
  
	@Column(name = "type")
	private Integer type;
	
	 @Transient
	 private String voucher_range;
	 
	@Column(name = "other_remark", nullable = true ,length = MyAbstractController.SIZE_THOUSAND)
	private String other_remark ;
  			 
	@Column(name = "from_mobile", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean from_mobile ;
	  
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;
	  
	@Column(name = "status", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean status ;
	  
	@Column(name = "created_date", nullable = true)
	private LocalDate created_date; 
	  
	@Column(name = "created_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private  Long created_by ; 
	  
	@Column(name = "updated_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private  Long updated_by ; 
	  
	@Column(name = "update_date", nullable = true)
	private   LocalDate  update_date;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	 @Column(name = "flag")
	 private Boolean flag;
	 
	 @Column(name = "local_time", nullable = true)
		private LocalTime local_time;
	
	public Long getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(Long transaction_id) {
		this.transaction_id = transaction_id;
	}

	public Bank getWithdraw_from() {
		return withdraw_from;
	}

	public void setWithdraw_from(Bank withdraw_from) {
		this.withdraw_from = withdraw_from;
	}

	public Bank getDeposite_to() {
		return deposite_to;
	}

	public void setDeposite_to(Bank deposite_to) {
		this.deposite_to = deposite_to;
	}

	public SubLedger getCash() {
		return cash;
	}

	public void setCash(SubLedger cash) {
		this.cash = cash;
	}

	public AccountingYear getAccounting_year_id() {
		return accounting_year_id;
	}

	public void setAccounting_year_id(AccountingYear accounting_year_id) {
		this.accounting_year_id = accounting_year_id;
	}

	public Long getAccountYearId() {
		return accountYearId;
	}

	public void setAccountYearId(Long accountYearId) {
		this.accountYearId = accountYearId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOther_remark() {
		return other_remark;
	}

	public void setOther_remark(String other_remark) {
		this.other_remark = other_remark;
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

	public LocalDate getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(LocalDate update_date) {
		this.update_date = update_date;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Long getDepositeTo() {
		return depositeTo;
	}

	public void setDepositeTo(Long depositeTo) {
		this.depositeTo = depositeTo;
	}

	public Long getWithdrawFrom() {
		return withdrawFrom;
	}

	public void setWithdrawFrom(Long withdrawFrom) {
		this.withdrawFrom = withdrawFrom;
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
		 
}
