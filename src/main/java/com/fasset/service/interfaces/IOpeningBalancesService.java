package com.fasset.service.interfaces;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;

import com.fasset.entities.Contra;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.Ledger;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.YearEndJV;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IOpeningBalancesService extends IGenericService<OpeningBalances>{
	void updateStock(OpeningBalances opbalance);
	Long findledgerbalance(Long company_Id, Long year_Id,Long ledger_Id,Long balanceType);
	Long findsubledgerbalance(Long company_Id, Long year_Id,Long subledger_Id,Long balanceType);
	Long findbankbalance(Long company_Id, Long year_Id,Long subledger_Id,Long balanceType);
	Long findsupplierbalance(Long company_Id, Long year_Id,Long subledger_Id,Long balanceType);
	Long findcustomerbalance(Long company_Id, Long year_Id,Long subledger_Id,Long balanceType);
	//Long saveOpeningBalances(Long company_Id, Long year_Id, Long ledger_Id, Long balanceType);
	Long saveOpeningBalances(Long companyId, Long year_id, Long id, Long type, Double credit, Double debit);
	Long updatepeningbalance(Long existid, Double creditval, Double debitval);
	Long updateCDbalance(Long existid, Double creditval, Double debitval, Long flag);
	List<OpeningBalances> getOpeningBalances(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Ledger> getOpeningBalancesOfLedger(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances> getOpeningBalances(Long companyId,Long customerId,Long supplierId,Long bankId);
	Long saveOpeningBalancesnew(Long company_id, Long year, Long sids, long l, Double creditval, Double debitval,
			String date);
	Long updatepeningbalancenew(Long existid, Double creditval, Double debitval, String date, Long year);
	Long findcustomerbalancebydate(Long company_id, Long year_id, Long sids, long l, LocalDate date);
	Long saveOpeningBalancesbydate(LocalDate date, Long company_id, Long year_id, Long sids, long l, Double creditval,
			Double debitval,SalesEntry sales,Receipt receipt,PurchaseEntry purchase,Payment payment,CreditNote credit,DebitNote debit,Contra contra,ManualJournalVoucher mjv,PayrollAutoJV payAutoJv,GSTAutoJV gstAutoJV,DepreciationAutoJV depriAutoJV,YearEndJV yearEndJV);
	Long findsupplierbalancebydate(Long company_id, Long year_id, Long sids, long l, LocalDate date);
	Long findbankbalancebydate(Long company_id, long year_id, Long sids, long l, LocalDate date);
	Long findsubledgerbalancebydate(Long company_id, Long year_id, Long sids, long l, LocalDate date,SalesEntry sales,Receipt receipt,PurchaseEntry purchase,Payment payment,CreditNote credit,DebitNote debit,Contra contra,ManualJournalVoucher mjv,PayrollAutoJV payAutoJv,GSTAutoJV gstAutoJV,DepreciationAutoJV depriAutoJV,YearEndJV yearEndJV);
	double getclosingbalance(Long company_id,Long year_id, LocalDate created_date, Long subledger_Id, long i);
	List<OpeningBalances> findAllOPbalancesofdata(long company_id, long l,long year, long openingtype);
	List<OpeningBalances[]> findAllOPbalancesforSubledger(LocalDate from_date,LocalDate to_date,Long companyId,Boolean flag);
	List<OpeningBalances[]> findAllOPbalancesforLedger(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforCustomer(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforSupplier(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforBank(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances[]> tdsReceivableAndTdsPayableReports(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforCustomerBeforeFromDate(LocalDate from_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforSupplierBeforeFromDate(LocalDate from_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforBankBeforeFromDate(LocalDate from_date,Long companyId);
	CopyOnWriteArrayList<OpeningBalances> findAllOPbalancesforSubledger(LocalDate from_date,LocalDate to_date,Long subledgerId,Long companyId);
	OpeningBalances isOpeningBalancePresentPayment(LocalDate date, Long companyId,Long supplier_id);
	OpeningBalances isOpeningBalancePresentReceipt(LocalDate date, Long companyId,Long customer_id);
	public OpeningBalances isOpeningBalancePresentPurchase(LocalDate date, Long companyId,Long supplier_id);
	public OpeningBalances isOpeningBalancePresentSales(LocalDate date, Long companyId,Long customerId);
	public OpeningBalances isOpeningBalancePresentSalesForPeriod(LocalDate fromdate, Long companyId,Long customerId,LocalDate toDate);
	List<OpeningBalances[]> findAllOPbalancesforSubLedger(LocalDate from_date,LocalDate to_date,Long companyId);

}