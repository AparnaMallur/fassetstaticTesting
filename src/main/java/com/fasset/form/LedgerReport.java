package com.fasset.form;

import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import com.fasset.entities.Bank;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Customer;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Payment;
import com.fasset.entities.Receipt;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.google.common.base.Supplier;

public class LedgerReport {
	private String type;
	private String transaction_value;
	private String voucher_no;
	private LocalDate created_date;
	private String cgst;
	private String igst;
	private String sgst;
	private String round_off;
	private String primary_id;
	private String customer_name_id;
	private String sct;
	private String total_excise;
	private String total_vat;
	private String total_vatcst;
	private String entry_status;
	private String flag;
    private String sale_type;
	private String bank_id;
	private String subledger_id;
	private String tds_amount;
	
	private Customer customer;
	private Suppliers supplier;
	private Bank bank;
	private SubLedger subledger;
	private Set<Payment> payments;
	private Set<Receipt> receipts;
	
	private Set<DebitNote> debitNotes;
	private Set<CreditNote> creditNotes;

	public String getTransaction_value() {
		return transaction_value;
	}

	public void setTransaction_value(String transaction_value) {
		this.transaction_value = transaction_value;
	}

	public String getVoucher_no() {
		return voucher_no;
	}

	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}

	

	public String getCgst() {
		return cgst;
	}

	public void setCgst(String cgst) {
		this.cgst = cgst;
	}

	public String getIgst() {
		return igst;
	}

	public void setIgst(String igst) {
		this.igst = igst;
	}

	public String getSgst() {
		return sgst;
	}

	public void setSgst(String sgst) {
		this.sgst = sgst;
	}

	public String getRound_off() {
		return round_off;
	}

	public void setRound_off(String round_off) {
		this.round_off = round_off;
	}

	public String getPrimary_id() {
		return primary_id;
	}

	public void setPrimary_id(String primary_id) {
		this.primary_id = primary_id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public Set<Receipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(Set<Receipt> receipts) {
		this.receipts = receipts;
	}

	public String getCustomer_name_id() {
		return customer_name_id;
	}

	public void setCustomer_name_id(String customer_name_id) {
		this.customer_name_id = customer_name_id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Suppliers getSupplier() {
		return supplier;
	}

	public void setSupplier(Suppliers supplier) {
		this.supplier = supplier;
	}

	public String getSct() {
		return sct;
	}

	public void setSct(String sct) {
		this.sct = sct;
	}

	public String getTotal_excise() {
		return total_excise;
	}

	public void setTotal_excise(String total_excise) {
		this.total_excise = total_excise;
	}

	public String getTotal_vat() {
		return total_vat;
	}

	public void setTotal_vat(String total_vat) {
		this.total_vat = total_vat;
	}

	public String getTotal_vatcst() {
		return total_vatcst;
	}

	public void setTotal_vatcst(String total_vatcst) {
		this.total_vatcst = total_vatcst;
	}

	public String getEntry_status() {
		return entry_status;
	}

	public void setEntry_status(String entry_status) {
		this.entry_status = entry_status;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSale_type() {
		return sale_type;
	}

	public void setSale_type(String sale_type) {
		this.sale_type = sale_type;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getTds_amount() {
		return tds_amount;
	}

	public void setTds_amount(String tds_amount) {
		this.tds_amount = tds_amount;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public Set<DebitNote> getDebitNotes() {
		return debitNotes;
	}

	public void setDebitNotes(Set<DebitNote> debitNotes) {
		this.debitNotes = debitNotes;
	}

	public Set<CreditNote> getCreditNotes() {
		return creditNotes;
	}

	public void setCreditNotes(Set<CreditNote> creditNotes) {
		this.creditNotes = creditNotes;
	}

	public SubLedger getSubledger() {
		return subledger;
	}

	public void setSubledger(SubLedger subledger) {
		this.subledger = subledger;
	}

	public String getSubledger_id() {
		return subledger_id;
	}

	public void setSubledger_id(String subledger_id) {
		this.subledger_id = subledger_id;
	}



}
