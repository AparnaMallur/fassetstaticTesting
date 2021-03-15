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

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "opening_balances")
public class OpeningBalances extends AbstractEntity {

	public YearEndJV getYearEndJV() {
		return yearEndJV;
	}

	public void setYearEndJV(YearEndJV yearEndJV) {
		this.yearEndJV = yearEndJV;
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "opening_id", unique = true, nullable = false)
	private Long opening_id;	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accountingYear ;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subLedger")
	private SubLedger subLedger ;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ledger_id")
	private Ledger ledger ;		


	@Column(name = "updated_date")
	private LocalDate updated_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User created_by;
	
	@Column(name = "created_date")
	private LocalDate created_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	private User updated_by;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bank")
	private Bank bank ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplier_id")
	private Suppliers supplier;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sales_id")
	private SalesEntry sales;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receipt_id")
	private Receipt receipt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id")
	private Payment payment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_id")
	private PurchaseEntry purchase;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "debit_no_id")
	private DebitNote debit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "credit_no_id")
	private CreditNote credit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contra_id")
	private Contra contra;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mjv_id")
	private ManualJournalVoucher mjv;
		
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payAutoJv_id")
	private PayrollAutoJV payAutoJv;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gstAutoJV_id")
	private GSTAutoJV gstAutoJV;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depriAutoJV_id")
	private DepreciationAutoJV depriAutoJV;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "year_end_jVId")
	private YearEndJV yearEndJV;
		
	@Column(name = "balanceType", nullable = true)
	private Long balanceType ;
	//1:ledger,2:Sub ledger,3:bank,4:supplier,5:customer
	
	@Column(name = "openingType", nullable = true)
	private Long openingType ;
	//1:opening current year,2:transactions,3:closing last year

	
	@Column(name = "credit_balance", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Double credit_balance;
	
	public Long getOpeningType() {
		return openingType;
	}

	public void setOpeningType(Long openingType) {
		this.openingType = openingType;
	}

	@Column(name = "debit_balance", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Double debit_balance;
	
	@Column(name = "credit_opening_balance", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Double credit_opening_balance;
	
	@Column(name = "debit_opening_balance", nullable = true, length = MyAbstractController.SIZE_TWENTY)
	private Double debit_opening_balance;	
		
	@Transient
	private String credit_opening_balance1;
	
	@Transient
	private String debit_opening_balance1;
	
	
	@Transient
	private String sublegderCr;
	
	@Transient
	private String sublegderdr;
	

	public Long getOpening_id() {
		return opening_id;
	}

	public void setOpening_id(Long opening_id) {
		this.opening_id = opening_id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public AccountingYear getAccountingYear() {
		return accountingYear;
	}

	public void setAccountingYear(AccountingYear accountingYear) {
		this.accountingYear = accountingYear;
	}

	public SubLedger getSubLedger() {
		return subLedger;
	}

	public void setSubLedger(SubLedger subLedger) {
		this.subLedger = subLedger;
	}

	public Ledger getLedger() {
		return ledger;
	}

	public void setLedger(Ledger ledger) {
		this.ledger = ledger;
	}

	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
	}

	public User getCreated_by() {
		return created_by;
	}

	public void setCreated_by(User created_by) {
		this.created_by = created_by;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public User getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(User updated_by) {
		this.updated_by = updated_by;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Suppliers getSupplier() {
		return supplier;
	}

	public void setSupplier(Suppliers supplier) {
		this.supplier = supplier;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(Long balanceType) {
		this.balanceType = balanceType;
	}

	public Double getCredit_balance() {
		return credit_balance;
	}

	public void setCredit_balance(Double credit_balance) {
		this.credit_balance = credit_balance;
	}

	public Double getDebit_balance() {
		return debit_balance;
	}

	public void setDebit_balance(Double debit_balance) {
		this.debit_balance = debit_balance;
	}

	public Double getCredit_opening_balance() {
		return credit_opening_balance;
	}

	public void setCredit_opening_balance(Double credit_opening_balance) {
		this.credit_opening_balance = credit_opening_balance;
	}

	public Double getDebit_opening_balance() {
		return debit_opening_balance;
	}

	public void setDebit_opening_balance(Double debit_opening_balance) {
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

	public SalesEntry getSales() {
		return sales;
	}

	public void setSales(SalesEntry sales) {
		this.sales = sales;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public PurchaseEntry getPurchase() {
		return purchase;
	}

	public void setPurchase(PurchaseEntry purchase) {
		this.purchase = purchase;
	}

	public DebitNote getDebit() {
		return debit;
	}

	public void setDebit(DebitNote debit) {
		this.debit = debit;
	}

	public CreditNote getCredit() {
		return credit;
	}

	public void setCredit(CreditNote credit) {
		this.credit = credit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Contra getContra() {
		return contra;
	}

	public void setContra(Contra contra) {
		this.contra = contra;
	}

	
	public ManualJournalVoucher getMjv() {
		return mjv;
	}

	public void setMjv(ManualJournalVoucher mjv) {
		this.mjv = mjv;
	}

	public String getSublegderCr() {
		return sublegderCr;
	}

	public void setSublegderCr(String sublegderCr) {
		this.sublegderCr = sublegderCr;
	}

	public String getSublegderdr() {
		return sublegderdr;
	}

	public void setSublegderdr(String sublegderdr) {
		this.sublegderdr = sublegderdr;
	}

	public PayrollAutoJV getPayAutoJv() {
		return payAutoJv;
	}

	public void setPayAutoJv(PayrollAutoJV payAutoJv) {
		this.payAutoJv = payAutoJv;
	}

	public GSTAutoJV getGstAutoJV() {
		return gstAutoJV;
	}

	public void setGstAutoJV(GSTAutoJV gstAutoJV) {
		this.gstAutoJV = gstAutoJV;
	}

	public DepreciationAutoJV getDepriAutoJV() {
		return depriAutoJV;
	}

	public void setDepriAutoJV(DepreciationAutoJV depriAutoJV) {
		this.depriAutoJV = depriAutoJV;
	}
	
	

}
