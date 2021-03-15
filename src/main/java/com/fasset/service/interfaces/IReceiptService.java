package com.fasset.service.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;

import com.fasset.entities.Receipt;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.service.interfaces.generic.IGenericService;
import com.fasset.entities.Receipt_product_details;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Suppliers;

public interface IReceiptService extends IGenericService<Receipt>{
	Company findCompany(Long user_id);
	Long saveReceipt(Receipt sup);
	List<Receipt> findAll();
	List<Customer> findAllCustomer(Long company_id);
	List<Receipt_product_details> findAllReceiptProductEntityList(Long entryId);
	String deleteReceiptProduct(Long REId,Long receipt_id,Long company_id);	
	CopyOnWriteArrayList<Receipt> getReport(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	List<SalesEntry> findAllCustomerWithDate(LocalDate from_date, LocalDate to_date, Long company_id);
	String deleteByIdValue(Long entityId);
	List<Receipt> findAllRecepitOfCompany(Long company_id,Boolean flag);
	List<Receipt> getAdvancePaymentList(Long customerId, Long companyId, Long yid);
	List<Receipt> getIncome(Long yearId, Long companyId);
	public Receipt findOneWithAll(Long rId);
	Long saveReceiptThroughExcel(Receipt receipt);
	void saveReceipt_product_detailsThroughExcel(Receipt_product_details entity);
	void updateReceiptThroughExcel(Receipt receipt);
	void updateReceipt_product_detailsThroughExcel(Receipt_product_details entity);
	void changeStatusOfReceiptThroughExcel(Receipt receipt);
	public List<Receipt> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Receipt> findAllRecepitsOfCompany(Long company_id);
	List<Receipt> getCashBookBankBookReport(LocalDate from_date,LocalDate to_date,Long companyId,Integer type);
	Receipt_product_details editproductdetailforReceipt(Long entryId);
	public void updateReceipt(Receipt entry);
	public void updateReceipt_product_details(Receipt_product_details entry);
	double findpaidtds(Long sales_id);
	List<Receipt> findallreceiptentryofsales(long id);
	void diactivateByIdValue(Long entityId,Boolean isDelete);
	void activateByIdValue(Long entityId);
	List<Receipt> getReceiptListForLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId,Long bank_id);
	Receipt findOne(Long receipt_id);
	List<Receipt> CashReceiptOfMoreThanRS10000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Receipt> customerReceiptHavingUnadjustedUnadjusted(LocalDate from_date,LocalDate to_date,Long companyId);
	public List<Receipt> getReceiptForLedgerReport(LocalDate from_date,LocalDate to_date,Long customerId, Long companyId);
	List<Receipt> customerReceiptHavingGST0(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Receipt> customerReceiptsWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Receipt> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate from_date,LocalDate to_date,Long companyId);
	public Receipt isExcelVocherNumberExist(String vocherNo,Long companyId);
	List<Receipt> getAllReceiptsAgainstAdvanceReceipt(Long advanceReceiptId);
	 List<Receipt> getAllOpeningBalanceAgainstReceipt(Long customerId, Long companyId) ;
		public List<Receipt> getAllOpeningBalanceAgainstReceiptForPeriod(Long customerId, Long companyId,LocalDate toDate);
	 List<Receipt> getReceiptForSales(Long customerId, Long companyId,LocalDate toDate) ;
	 List<Receipt> getAllAdvanceReceiptsAgainstCustomer(Long companyId,Long customer_id);
}