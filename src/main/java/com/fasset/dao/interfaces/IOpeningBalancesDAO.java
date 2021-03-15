package com.fasset.dao.interfaces;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
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

public interface IOpeningBalancesDAO extends IGenericDao<OpeningBalances>{
	Long saveOpeningBalances(OpeningBalances opbalance);
	void updateStock(OpeningBalances opbalance);
	Long findledgerbalance(Long company_Id, Long year_Id,Long ledger_Id,Long balanceType);
	Long findsubledgerbalance(Long company_Id, Long year_Id,Long subledger_Id,Long balanceType);
	Long findbankbalance(Long company_Id, Long year_Id,Long subledger_Id,Long balanceType);
	Long findsupplierbalance(Long company_Id, Long year_Id,Long subledger_Id,Long balanceType);
	Long findcustomerbalance(Long company_Id, Long year_Id,Long subledger_Id,Long balanceType);
	OpeningBalances findexists(Long id);
	Long updateOpeningBalances(Long existid, Double creditval, Double debitval, Long flag);
	OpeningBalances findifexist(Long company_Id, Long year_Id, Long subledger_Id, Long balanceType);
	List<OpeningBalances> getOpeningBalances(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Ledger> getOpeningBalancesOfLedger(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances> getOpeningBalances(Long companyId,Long customerId,Long supplierId,Long bankId);
	OpeningBalances findifexistbydate(Long company_id, Long year_id, Long sids, long balanceType, LocalDate date,SalesEntry sales,Receipt receipt,PurchaseEntry purchase,Payment payment,CreditNote credit,DebitNote debit,Contra contra,ManualJournalVoucher mjv,PayrollAutoJV payrollav,GSTAutoJV gstav, DepreciationAutoJV djv,YearEndJV yearEndJV);/*PayrollAutoJV payrollav,GSTAutoJV gstav, DepreciationAutoJV djv,YearEndJV yearEndJV)*/;
	double getclosingbalance(Long company_id,Long year_id, LocalDate created_date, Long subledger_Id, long type);
	List<OpeningBalances> findAllOPbalancesofdata(long company_id, long flag,long year,long openingtype);
	List<OpeningBalances[]> findAllOPbalancesforSubledger(LocalDate from_date,LocalDate to_date,Long companyId,Boolean flag);
	List<OpeningBalances[]>findAllOPbalancesforLedger(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforCustomer(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances[]>findAllOPbalancesforSupplier(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforBank(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances[]> tdsReceivableAndTdsPayableReports(LocalDate from_date,LocalDate to_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforCustomerBeforeFromDate(LocalDate from_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforSupplierBeforeFromDate(LocalDate from_date,Long companyId);
	List<OpeningBalances[]> findAllOPbalancesforBankBeforeFromDate(LocalDate from_date,Long companyId);
	CopyOnWriteArrayList<OpeningBalances>   findAllOPbalancesforSubledger(LocalDate from_date,LocalDate to_date,Long subledgerId,Long companyId);
	void deleteOpeningBalance(Long salesId,Long receiptId,Long creditId,Long purchaseId,Long paymentId,Long debitId,Long contra_id,Long mj_id,Long payroll_id,Long gst_id,Long depri_id,Long yearId);
	Long updateOpeningBalances(OpeningBalances balance);
	OpeningBalances isOpeningBalancePresentPayment(LocalDate date, Long companyId,Long supplier_id);
	OpeningBalances isOpeningBalancePresentReceipt(LocalDate date, Long companyId,Long customer_id);
	 OpeningBalances isOpeningBalancePresentPurchase(LocalDate date, Long companyId,Long supplier_id);
	 public OpeningBalances isOpeningBalancePresentSales(LocalDate date, Long companyId,Long customer_id);
		public OpeningBalances isOpeningBalancePresentSalesForPeriod(LocalDate fromdate, Long companyId,Long customerId,LocalDate toDate);
	  List<OpeningBalances[]> findAllOPbalancesforSubLedger(LocalDate from_date, LocalDate to_date, Long companyId);


}