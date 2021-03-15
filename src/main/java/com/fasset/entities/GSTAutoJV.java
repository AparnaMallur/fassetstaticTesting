package com.fasset.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "gst_autoJV")
public class GSTAutoJV extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "gst_id")
	private Long gst_id;

	@Column(name = "created_date", nullable = true)
	private LocalDate created_date;
	
	@Column(name = "voucher_no")
	private String voucher_no;
	@Transient
	private Long accountYearId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accounting_year_id")
	private AccountingYear accountingYear;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;
	
	@OneToOne(targetEntity=VoucherSeries.class,cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name = "voucher_id",referencedColumnName="voucher_id")
	private VoucherSeries voucherSeries;
	
	@Column
	private String outputCgst;
	@Column
	private String outputSgst;
	@Column
	private String outputIgst;
	@Column
	private String inputCgst;
	@Column
	private String inputSgst;
	@Column
	private String inputIgst;
	@Column
	private String gstPayble;
	// balanceCase is used for GSTPayable Ledger Affection 1 is for output value zero for that amount goes to Debit side of GSTPayable and viceVersa
	@Transient
	private Integer balanceCase;
	//final values
	@Column
	private Double outputCgstbalance;
	@Column
	private Double outputSgstBalance;
	@Column
	private Double outputIgstBalance;
	@Column
	private Double inputCgstBalance;
	@Column
	private Double inputSgstBalance;
	@Column
	private Double inputIgstBalance;
	@Column
	private Double gstPaybleBalance;
	
	//initial values
	@Column
	private Double initialOutputCgstbalance;
	@Column
	private Double initialOutputSgstBalance;
	@Column
	private Double initialOutputIgstBalance;
	@Column
	private Double initialInputCgstBalance;
	@Column
	private Double initialInputSgstBalance;
	@Column
	private Double initialInputIgstBalance;
	
	//calculated values
	@Column
	private Double totalOutputCgstCrbalance;// it is actually dr balance to add on dr side
	@Column
	private Double totalOutputSgstCrBalance;// it is actually dr balance to add on dr side
	@Column
	private Double totalOutputIgstCrBalance;// it is actually dr balance to add on dr side
	@Column
	private Double totalInputCgstDrBalance;// it is actually cr balance to add on cr side
	@Column
	private Double totalInputSgstDrBalance;// it is actually cr balance to add on cr side
	@Column
	private Double totalInputIgstDrBalance;// it is actually cr balance to add on cr side
	@Column
	private Double totalgstPaybleDrBalance;
	
	
	public Long getGst_id() {
		return gst_id;
	}
	public void setGst_id(Long gst_id) {
		this.gst_id = gst_id;
	}
	public String getOutputCgst() {
		return outputCgst;
	}
	public void setOutputCgst(String outputCgst) {
		this.outputCgst = outputCgst;
	}
	public String getOutputSgst() {
		return outputSgst;
	}
	public void setOutputSgst(String outputSgst) {
		this.outputSgst = outputSgst;
	}
	public String getOutputIgst() {
		return outputIgst;
	}
	public void setOutputIgst(String outputIgst) {
		this.outputIgst = outputIgst;
	}
	public String getInputCgst() {
		return inputCgst;
	}
	public void setInputCgst(String inputCgst) {
		this.inputCgst = inputCgst;
	}
	public String getInputSgst() {
		return inputSgst;
	}
	public void setInputSgst(String inputSgst) {
		this.inputSgst = inputSgst;
	}
	public String getInputIgst() {
		return inputIgst;
	}
	public void setInputIgst(String inputIgst) {
		this.inputIgst = inputIgst;
	}
	public String getGstPayble() {
		return gstPayble;
	}
	public void setGstPayble(String gstPayble) {
		this.gstPayble = gstPayble;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public LocalDate getCreated_date() {
		return created_date;
	}
	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}
	public String getVoucher_no() {
		return voucher_no;
	}
	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}
	
	
	public Double getTotalgstPaybleDrBalance() {
		return totalgstPaybleDrBalance;
	}
	public void setTotalgstPaybleDrBalance(Double totalgstPaybleDrBalance) {
		this.totalgstPaybleDrBalance = totalgstPaybleDrBalance;
	}
	public Double getOutputCgstbalance() {
		return outputCgstbalance;
	}
	public void setOutputCgstbalance(Double outputCgstbalance) {
		this.outputCgstbalance = outputCgstbalance;
	}
	public Double getOutputSgstBalance() {
		return outputSgstBalance;
	}
	public void setOutputSgstBalance(Double outputSgstBalance) {
		this.outputSgstBalance = outputSgstBalance;
	}
	public Double getOutputIgstBalance() {
		return outputIgstBalance;
	}
	public void setOutputIgstBalance(Double outputIgstBalance) {
		this.outputIgstBalance = outputIgstBalance;
	}
	public Double getInputCgstBalance() {
		return inputCgstBalance;
	}
	public void setInputCgstBalance(Double inputCgstBalance) {
		this.inputCgstBalance = inputCgstBalance;
	}
	public Double getInputSgstBalance() {
		return inputSgstBalance;
	}
	public void setInputSgstBalance(Double inputSgstBalance) {
		this.inputSgstBalance = inputSgstBalance;
	}
	public Double getInputIgstBalance() {
		return inputIgstBalance;
	}
	public void setInputIgstBalance(Double inputIgstBalance) {
		this.inputIgstBalance = inputIgstBalance;
	}
	public Double getGstPaybleBalance() {
		return gstPaybleBalance;
	}
	public void setGstPaybleBalance(Double gstPaybleBalance) {
		this.gstPaybleBalance = gstPaybleBalance;
	}
	public Double getInitialOutputCgstbalance() {
		return initialOutputCgstbalance;
	}
	public void setInitialOutputCgstbalance(Double initialOutputCgstbalance) {
		this.initialOutputCgstbalance = initialOutputCgstbalance;
	}
	public Double getInitialOutputSgstBalance() {
		return initialOutputSgstBalance;
	}
	public void setInitialOutputSgstBalance(Double initialOutputSgstBalance) {
		this.initialOutputSgstBalance = initialOutputSgstBalance;
	}
	public Double getInitialOutputIgstBalance() {
		return initialOutputIgstBalance;
	}
	public void setInitialOutputIgstBalance(Double initialOutputIgstBalance) {
		this.initialOutputIgstBalance = initialOutputIgstBalance;
	}
	public Double getInitialInputCgstBalance() {
		return initialInputCgstBalance;
	}
	public void setInitialInputCgstBalance(Double initialInputCgstBalance) {
		this.initialInputCgstBalance = initialInputCgstBalance;
	}
	public Double getInitialInputSgstBalance() {
		return initialInputSgstBalance;
	}
	public void setInitialInputSgstBalance(Double initialInputSgstBalance) {
		this.initialInputSgstBalance = initialInputSgstBalance;
	}
	public Double getInitialInputIgstBalance() {
		return initialInputIgstBalance;
	}
	public void setInitialInputIgstBalance(Double initialInputIgstBalance) {
		this.initialInputIgstBalance = initialInputIgstBalance;
	}
	public Double getTotalOutputCgstCrbalance() {
		return totalOutputCgstCrbalance;
	}
	public void setTotalOutputCgstCrbalance(Double totalOutputCgstCrbalance) {
		this.totalOutputCgstCrbalance = totalOutputCgstCrbalance;
	}
	public Double getTotalOutputSgstCrBalance() {
		return totalOutputSgstCrBalance;
	}
	public void setTotalOutputSgstCrBalance(Double totalOutputSgstCrBalance) {
		this.totalOutputSgstCrBalance = totalOutputSgstCrBalance;
	}
	public Double getTotalOutputIgstCrBalance() {
		return totalOutputIgstCrBalance;
	}
	public void setTotalOutputIgstCrBalance(Double totalOutputIgstCrBalance) {
		this.totalOutputIgstCrBalance = totalOutputIgstCrBalance;
	}
	public Double getTotalInputCgstDrBalance() {
		return totalInputCgstDrBalance;
	}
	public void setTotalInputCgstDrBalance(Double totalInputCgstDrBalance) {
		this.totalInputCgstDrBalance = totalInputCgstDrBalance;
	}
	public Double getTotalInputSgstDrBalance() {
		return totalInputSgstDrBalance;
	}
	public void setTotalInputSgstDrBalance(Double totalInputSgstDrBalance) {
		this.totalInputSgstDrBalance = totalInputSgstDrBalance;
	}
	public Double getTotalInputIgstDrBalance() {
		return totalInputIgstDrBalance;
	}
	public void setTotalInputIgstDrBalance(Double totalInputIgstDrBalance) {
		this.totalInputIgstDrBalance = totalInputIgstDrBalance;
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
	public Long getAccountYearId() {
		return accountYearId;
	}
	public void setAccountYearId(Long accountYearId) {
		this.accountYearId = accountYearId;
	}
	public Integer getBalanceCase() {
		return balanceCase;
	}
	public void setBalanceCase(Integer balanceCase) {
		this.balanceCase = balanceCase;
	}
	public VoucherSeries getVoucherSeries() {
		return voucherSeries;
	}
	public void setVoucherSeries(VoucherSeries voucherSeries) {
		this.voucherSeries = voucherSeries;
	}
	
	
}