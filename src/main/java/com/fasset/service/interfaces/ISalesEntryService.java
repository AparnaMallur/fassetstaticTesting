/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.entities.Suppliers;
import com.fasset.form.BillsReceivableForm;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ISalesEntryService extends IGenericService<SalesEntry>{

	Long saveSalesEntry(SalesEntry sup);
	Long saveSalesEntryThroughExcel(SalesEntry entry);
	Long saveSalesEntryProductEntityClassThroughExcel(SalesEntryProductEntityClass entry);
	Company findCompany(Long user_id);
	String deleteSalesEntryProduct(Long SEId,Long sales_id,Long company_id);
	String deleteByIdValue(Long entityId,Boolean isDelete);
	String activateByIdValue(long id);
	SalesEntry findOneWithAll(Long salId);
	SalesEntryProductEntityClass editproductdetailforSalesEntry(Long entryId);
	void updateSalesEntryThroughExcel(SalesEntry entry);
	void updateSalesEntryProductEntityClassThroughExcel(SalesEntryProductEntityClass entry);
	void ChangeStatusofSalesEntryThroughExcel(SalesEntry entry);
	void updateSalesEntry(SalesEntry entry);
	void updateSalesEntryProductEntityClass(SalesEntryProductEntityClass entry);
	List<Suppliers> findAllSuppliers(Long company_id);
	List<SalesEntry> findAll();
	List<SalesEntryProductEntityClass> findAllSalesEntryProductEntityList(Long entryId);
	CopyOnWriteArrayList<SalesEntry> getReport(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	List<SalesEntry> findAllSalesEntryOfCompany(Long CompanyId,Boolean flag);
	List<SalesEntry> findAllDisableSalesEntryOfCompanyAfterImport(Long CompanyId,Boolean flag);
	List<BillsReceivableForm> getBillsReceivable(Long customerId, LocalDate fromDate, LocalDate toDate, Long companyId);
	List<SalesEntry> findAllSalesEntriesOfCompany(Long CompanyId,Boolean importFunction);
	List<SalesEntry> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	List<SalesEntry> findAllactiveSalesEntryOfCompany(long company_id, boolean b);
	List<SalesEntry> getCashBookBankBookReport(LocalDate from_date,LocalDate to_date,Long companyId,Integer type);
	List<SalesEntry> customerSalesHavingGST0(LocalDate from_date,LocalDate to_date,Long companyId);
	List<SalesEntry> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate from_date,LocalDate to_date,Long companyId);
	List<SalesEntry> salesEntryWithEditedGSTvalues(LocalDate from_date,LocalDate to_date,Long companyId);
	List<SalesEntry> customerSalesWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<SalesEntry> getSalesForLedgerReport(LocalDate from_date,LocalDate to_date,Long customerId,Long companyId);
	List<SalesEntry> getCardSalesForLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId,Long bank_id);
	public SalesEntry isExcelVocherNumberExist(String vocherNo,Long companyId);
	List<SalesEntry> findAllSalesEntryOfCompanyForMobile(Long companyId,Boolean flag,Long yearId);
	Double getTotalbillsBillsReceivable(Long company_Id);
	public List<SalesEntry> getAllOpeningBalanceAgainstSales(Long customerid,Long companyId);
	public List<SalesEntry> getAllSalesAmount(Long customerid,Long companyId,LocalDate toDate);
	 public List<SalesEntry> findExcelVoucherNumber(long companyid) ;
}