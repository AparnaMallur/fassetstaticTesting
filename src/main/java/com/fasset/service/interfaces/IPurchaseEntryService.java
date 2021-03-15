/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.PurchaseEntryProductEntityClass;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Suppliers;
import com.fasset.form.BillsReceivableForm;
import com.fasset.form.ExempListForm;
import com.fasset.form.HSNReportForm;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IPurchaseEntryService extends IGenericService<PurchaseEntry>{

	Company findCompany(Long user_id);
	List<Suppliers> findAllSuppliers(Long company_id);
	Long savePurchaseEntry(PurchaseEntry sup);
	List<PurchaseEntry> findAll();
	List<PurchaseEntryProductEntityClass> findAllPurchaseEntryProductEntityList(Long entryId);
	String deletePurchaseEntryProduct(Long PEId,Long purId, Long Company_id);
	String deleteByIdValue(Long entityId);
	CopyOnWriteArrayList<PurchaseEntry> getReport(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	List<PurchaseEntry> findAllPurchaseEntryOfCompany(Long CompanyId,Boolean flag);
	
	List<BillsReceivableForm> getBillsPayable(Long supplierId, LocalDate fromDate, LocalDate toDate,Long companyId);	
	List<BillsReceivableForm> getBillsPayableForPurchase(Long supplierId, LocalDate fromDate, LocalDate toDate,Long companyId);
	List<PurchaseEntry> getB2BList(Integer month, Long yearId, Long companyId);
	List<PurchaseEntry> getB2CLList(Integer month, Long yearId, Long companyId, Long stateId);
	List<PurchaseEntry> getB2CSList(Integer month, Long yearId, Long companyId, Long stateId);
	List<PurchaseEntry> getExpList(Integer month, Long yearId, Long companyId, Long countryId);
	List<HSNReportForm> getHsnList(Integer month, Long yearId, Long companyId);
	List<ExempListForm> getExempList(Integer month, Long yearId, Long companyId);
	public PurchaseEntry findOneWithAll(Long purId);
	public Long savePurchaseEntryThroughExcel(PurchaseEntry entry);
	public Long savePurchaseEntryProductEntityClassThroughExcel(PurchaseEntryProductEntityClass entry);
	public void updatePurchaseEntryThroughExcel(PurchaseEntry entry);
	public void updatePurchaseEntryProductEntityClassThroughExcel(PurchaseEntryProductEntityClass entry);
	public void ChangeStatusOfPurchaseEntryThroughExcel(PurchaseEntry entry);
	List<PurchaseEntry> findAllPurchaseEntriesOfCompany(Long CompanyId,Boolean importFunction);
	PurchaseEntryProductEntityClass editproductdetailforPurchaseEntry(Long entryId);
	public void updatePurchaseEntry(PurchaseEntry entry);
	public void updatePurchaseEntryProductEntityClass(PurchaseEntryProductEntityClass entry);
	List<PurchaseEntry> getDayBookReport(LocalDate fromDate, LocalDate toDate,Long companyId);
	List<PurchaseEntry> findAllactivePurchaseEntryOfCompany(long company_id, boolean b);
	List<PurchaseEntry> supplirPurchaseHavingGST0(LocalDate fromDate, LocalDate toDate, Long company_id);
	List<PurchaseEntry>supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate fromDate, LocalDate toDate, Long company_id);
	List<PurchaseEntry> supplierPurchaseWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<PurchaseEntry> getPurchaseForLedgerReport(LocalDate from_date,LocalDate to_date,Long suppilerId,Long companyId);
	List<PurchaseEntry> getPurchaseForLedgerReport1(LocalDate to_date,Long suppilerId,Long companyId);
	public PurchaseEntry isExcelVocherNumberExist(String vocherNo,Long companyId);
	Double getTotalBillsPayable(Long companyId);
	List<PurchaseEntry> getAllOpeningBalanceAgainstPurchase(Long suppilerId,Long companyId);
	List<PurchaseEntry> findAllPurchaseEntryOfCompanyForMobile(Long CompanyId,Boolean flag,Long yearID);

}