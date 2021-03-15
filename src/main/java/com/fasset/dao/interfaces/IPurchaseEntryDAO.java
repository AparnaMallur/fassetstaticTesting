/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Company;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.PurchaseEntryProductEntityClass;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Suppliers;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */

public interface IPurchaseEntryDAO extends IGenericDao<PurchaseEntry>{
	Company findCompanyDao(Long user_id);
	List<Suppliers> findAllSuppliers(Long company_id) ;
	Long savePurchaseEntryDao(PurchaseEntry sup);
	String deleteByIdValue(Long entityId);
	String deletePurchaseEntryProduct(Long PEId,Long purId, Long Company_id);
	List<PurchaseEntryProductEntityClass> findAllPurchaseEntryProductEntityList(Long entryId);
	CopyOnWriteArrayList<PurchaseEntry> getReport(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	List<PurchaseEntry> findAllPurchaseEntryOfCompany(Long CompanyId,Boolean flag);
	List<PurchaseEntry> findAllPurchaseEntriesOfCompany(Long CompanyId,Boolean importFunction);
	List<PurchaseEntry> findAllBySuppliers(Long supplierId, Long companyId);
	List<PurchaseEntry> getBillsPayable(Long supplierId, LocalDate fromDate, LocalDate toDate, Long companyId);
	List<PurchaseEntry> getBillsPayableForPurchase(Long supplierId, LocalDate fromDate, LocalDate toDate, Long companyId);
	List<PurchaseEntry> getB2BList(Integer month, Long yearId, Long companyId);
	List<PurchaseEntry> getB2CLList(Integer month, Long yearId, Long companyId, Long stateId);
	List<PurchaseEntry> getB2CSList(Integer month, Long yearId, Long companyId, Long stateId);
	List<PurchaseEntry> getExpList(Integer month, Long yearId, Long companyId, Long countryId);
	List<PurchaseEntry> getHsnList(Integer month, Long yearId, Long companyId);
	List<PurchaseEntry> getExempList(Integer month, Long yearId, Long companyId);
	List<PurchaseEntry> getNilList(Integer month, Long yearId, Long companyId);
	List<PurchaseEntry> getNonGstList(Integer month, Long yearId, Long companyId);
	Integer getCountByYear(Long yearId, Long companyId, String range);
	PurchaseEntry findOneWithAll(Long purId);
	PurchaseEntry findOneWithSuppilerAndSublegder(Long purId);
	Long savePurchaseEntryThroughExcel(PurchaseEntry entry);
	Long savePurchaseEntryProductEntityClassThroughExcel(PurchaseEntryProductEntityClass entry);
	void updatePurchaseEntryThroughExcel(PurchaseEntry entry);
	void updatePurchaseEntryProductEntityClassThroughExcel(PurchaseEntryProductEntityClass entry);
	void ChangeStatusOfPurchaseEntryThroughExcel(PurchaseEntry entry);
	int getCountByDate(Long companyId, String range, LocalDate date);
	PurchaseEntryProductEntityClass editproductdetailforPurchaseEntry(Long entryId);
	void updatePurchaseEntry(PurchaseEntry entry);
	void updatePurchaseEntryProductEntityClass(PurchaseEntryProductEntityClass entry);
	List<PurchaseEntry> getDayBookReport(LocalDate fromDate, LocalDate toDate,Long companyId);
	List<PurchaseEntry> findAllactivePurchaseEntryOfCompany(long company_id, boolean b);
	List<PurchaseEntry> supplirPurchaseHavingGST0(LocalDate fromDate, LocalDate toDate, Long company_id);
	List<PurchaseEntry> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate fromDate, LocalDate toDate, Long company_id);
	List<PurchaseEntry> supplierPurchaseWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<PurchaseEntry> getPurchaseForLedgerReport(LocalDate from_date,LocalDate to_date,Long suppilerId,Long companyId);
	List<PurchaseEntry> getPurchaseForLedgerReport1(LocalDate to_date,Long suppilerId,Long companyId);
	List<PurchaseEntry> getInwardSupplyList(LocalDate from_date, LocalDate to_date, Long companyId);
	Integer getCancelInwardSupplyList(LocalDate from_date, LocalDate to_date, Long companyId);
	public PurchaseEntry isExcelVocherNumberExist(String vocherNo,Long companyId);
	public List<PurchaseEntry>  getAllOpeningBalanceAgainstPurchase(Long supplierId,Long companyId);
	public List<PurchaseEntry> getAllPurchaseAmount(Long supplierId,Long companyId,LocalDate toDate);
	List<PurchaseEntry> findAllPurchaseEntryOfCompanyForMobile(Long CompanyId,Boolean flag,Long yearID);
}