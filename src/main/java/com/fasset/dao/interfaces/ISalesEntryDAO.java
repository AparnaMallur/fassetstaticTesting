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
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.form.HSNReportForm;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ISalesEntryDAO extends IGenericDao<SalesEntry>{

	Company findCompanyDao(Long user_id);
	Long saveSalesEntry(SalesEntry sup);
	List<SalesEntryProductEntityClass> findAllSalesEntryProductEntityList(Long entryId);
	String deleteSalesEntryProduct(Long seId,Long sale_id,Long company_id);
	CopyOnWriteArrayList<SalesEntry> getReport(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	List<SalesEntry> findAllSalesEntryOfCompany(Long companyId,Boolean flag);
	List<SalesEntry> findAllDisableSalesEntryOfCompanyAfterImport(Long CompanyId,Boolean flag);
	String deleteByIdValue(Long entityId,Boolean isDelete);
	List<SalesEntry> findAllByCustomers(Long customerId, Long companyId);
	List<SalesEntry> getBillsReceivable(Long customerId, LocalDate fromDate, LocalDate toDate, Long companyId);	
	List<SalesEntry> getB2BList(LocalDate from_date, LocalDate to_date, Long companyId);
	List<SalesEntry[]> getIntraRegisterList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	List<SalesEntry[]> getIntraNonRegisterList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	List<SalesEntry[]> getInterRegisterList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	List<SalesEntry[]> getInterNonRegisterList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	
	List<SalesEntry[]> getIntraRegisterVATList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	List<SalesEntry[]> getIntraNonRegisterVATList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	List<SalesEntry[]> getInterRegisterVATList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	List<SalesEntry[]> getInterNonRegisterVATList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	
	List<SalesEntry> getB2CLList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	List<SalesEntry> getB2CSList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	List<SalesEntry> getCashSalesList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId);
	List<SalesEntry> getExpList(LocalDate from_date, LocalDate to_date, Long companyId, Long countryId);
	List<SalesEntry> getHsnList(LocalDate from_date, LocalDate to_date, Long companyId);
	
	List<SalesEntry> getOutwardSupplyListForGivenVocherRange(LocalDate from_date, LocalDate to_date, Long companyId,String vocherRange);
	Integer getCancelOutwardSupplyListForGivenVocherRange(LocalDate from_date, LocalDate to_date, Long companyId,String vocherRange);
	
	void updateSalesEntry(SalesEntry sup,SalesEntry entity);
	//List<SalesEntry> getAtadjList(Integer month, Long yearId, Long companyId, List<Long> receiptIds);
	Integer getCountByYear(Long yearId, Long companyId, String range);
	SalesEntry findOneWithAll(Long salId);
	SalesEntry findOneWithCustomerAndSublegder(Long salId);
	Long saveSalesEntryThroughExcel(SalesEntry entry);
	Long saveSalesEntryProductEntityClassThroughExcel(SalesEntryProductEntityClass entry);
	void updateSalesEntryThroughExcel(SalesEntry entry);
	void updateSalesEntryProductEntityClassThroughExcel(SalesEntryProductEntityClass entry);
	void ChangeStatusofSalesEntryThroughExcel(SalesEntry entry);
	List<SalesEntry> findAllSalesEntriesOfCompany(Long CompanyId,Boolean importFunction);
	SalesEntryProductEntityClass editproductdetailforSalesEntry(Long entryId);
	void updateSalesEntry(SalesEntry entry);
	void updateSalesEntryProductEntityClass(SalesEntryProductEntityClass entry);
	List<SalesEntry> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	String activateByIdValue(long id);
	List<SalesEntry> findAllactiveSalesEntryOfCompany(Long CompanyId, Boolean flag);
	List<SalesEntry> getCashBookBankBookReport(LocalDate from_date,LocalDate to_date,Long companyId,Integer type);
	List<SalesEntry> customerSalesHavingGST0(LocalDate from_date,LocalDate to_date,Long companyId);
	List<SalesEntry> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate from_date,LocalDate to_date,Long companyId);
	List<SalesEntry> salesEntryWithEditedGSTvalues(LocalDate from_date,LocalDate to_date,Long companyId);
	List<SalesEntry> customerSalesWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<SalesEntry> getSalesForLedgerReport(LocalDate from_date,LocalDate to_date,Long customerId,Long companyId);
	List<SalesEntry> getCardSalesForLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId,Long bank_id);
	public SalesEntry isExcelVocherNumberExist(String vocherNo,Long companyId);
	List<SalesEntry> findAllSalesEntryOfCompanyForMobile(Long companyId,Boolean flag,Long yaer_id);
	public List<SalesEntry>  getAllOpeningBalanceAgainstSales(Long customerId,Long companyId);
	public List<SalesEntry>  getAllSalesAmount(Long customerId,Long companyId,LocalDate toDate);
	public List<SalesEntry> findExcelVoucherNumber(long companyid) ;
	
}