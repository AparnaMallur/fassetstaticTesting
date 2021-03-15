package com.fasset.dao.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Company;
import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;
import com.fasset.entities.SalesEntry;


public interface IReceiptDAO extends IGenericDao<Receipt>{	
	Company findCompany(Long user_id);
	Long saveReceiptDao(Receipt sup);
	List<Receipt> findAll();
	List<Receipt_product_details> findAllReceiptProductEntityList(Long entryId);
	String deleteReceiptProduct(Long REId,Long receipt_id,Long company_id);		
	CopyOnWriteArrayList<Receipt>  getReport(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	List<SalesEntry> findAllSalesEntryWithDate(LocalDate from_date,LocalDate to_date,Long company_id);
	List<Receipt> findAllRecepitOfCompany(Long company_id,Boolean flag);
	String deleteByIdValue(Long entityId);
	List<Receipt> getAdvanceReceiptList(List<Long> receiptIds, Long customerId, Long companyId, Long yid);	
	void updateAdvanceReceipt(Receipt receipt);
	List<Receipt> getIncomeByYearId(Long yearId, Long companyId);
	List<Receipt> getATList(LocalDate from_date, LocalDate to_date, Long companyId);
	List<Receipt> getATAdjList(LocalDate from_date, LocalDate to_date, Long companyId);
	void updateReceipt(Receipt sup, Receipt entity);
	public Receipt findOneWithAll(Long rId);
	Integer getCountByYear(Long yearId, Long companyId, String range);
	Long saveReceiptThroughExcel(Receipt receipt);
	void saveReceipt_product_detailsThroughExcel(Receipt_product_details entity);
	void updateReceiptThroughExcel(Receipt receipt);
	void updateReceipt_product_detailsThroughExcel(Receipt_product_details entity);
	void changeStatusOfReceiptThroughExcel(Receipt receipt);
	public List<Receipt> getDayBookReport(LocalDate from_date,LocalDate to_date, Long companyId);
	List<Receipt> findAllRecepitsOfCompany(Long company_id);
	List<Receipt> getCashBookBankBookReport(LocalDate from_date,LocalDate to_date,Long companyId,Integer type);
	int getCountByDate(Long companyId, String range, LocalDate date);
	Receipt_product_details editproductdetailforReceipt(Long entryId);
	public void updateReceipt(Receipt entry);
	public void updateReceipt_product_details(Receipt_product_details entry);
	Double findpaidtds(Long sales_id);
	void diactivateByIdValue(Long entityId,Boolean isDelete);
	List<Receipt> findallreceiptentryofsales(long id);
	void activateByIdValue(Long entityId);
	/*List<Receipt> getAdvanceReceiptListForLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId);*/
	List<Receipt> getReceiptListForLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId,Long bank_id);
	Long saveReceiptDaoagainstbill(Receipt newreceipt);
	List<Receipt> CashReceiptOfMoreThanRS10000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Receipt> customerReceiptHavingUnadjustedUnadjusted(LocalDate from_date,LocalDate to_date,Long companyId);
	void deleteReceipt(Long entityId);
	public List<Receipt> getReceiptForLedgerReport(LocalDate from_date,LocalDate to_date,Long customerId, Long companyId);
	List<Receipt> customerReceiptHavingGST0(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Receipt> customerReceiptWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Receipt> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate from_date,LocalDate to_date,Long companyId);
	public Receipt isExcelVocherNumberExist(String vocherNo,Long companyId);
	List<Receipt> getAllReceiptsAgainstAdvanceReceipt(Long advanceReceiptId);
	public List<Receipt> getAllOpeningBalanceAgainstReceipt(Long customerId, Long companyId);
	public List<Receipt> getAllOpeningBalanceAgainstReceiptForPeriod(Long customerId, Long companyId,LocalDate toDate);
	public List<Receipt> getReceiptForSales(Long customerId, Long companyId,LocalDate toDate);
	List<Receipt> getAllAdvanceReceiptsAgainstCustomer(Long companyId, Long customer_id);
}