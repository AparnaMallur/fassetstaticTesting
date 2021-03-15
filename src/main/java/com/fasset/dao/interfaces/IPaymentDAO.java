package com.fasset.dao.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PaymentDetails;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.exceptions.MyWebException;

public interface IPaymentDAO extends IGenericDao<Payment>{
	Long getCount();
	List<Payment> getReport(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	List<PurchaseEntry> findAllPurchaseEntryWithDate(LocalDate from_date,LocalDate to_date,Long company_id);
	List<Payment> findAllPaymentOfCompany(Long company_id,Boolean flag);
	String deleteByIdValue(Long entityId, long purchase_id, int status);
	List<Payment> getAdvancePaymentList(List<Long> paymentIds, Long supplierId, Long companyId, Long yid);
	List<Payment> getExpenditureByYearId(Long yeaId, Long companyId);
	List<Payment> getATList(Integer month, Long yearId, Long companyId);
	List<Payment> getATAdjList(Integer month, Long yearId, Long companyId);
	Payment findOneWithAll(Long paymentId);
	Integer getCountByYear(Long yearId, Long companyId, String range);
	Long savePaymentThroughExcel(Payment entry);
	void savePaymentDetailsThroughExcel(PaymentDetails details,Long payment_id);
	void updatePaymentThroughExcel(Payment entry);
	void updatePaymentDetailsThroughExcel(PaymentDetails details,Long payment_id);
	void cahngeStatusOfPaymentThroughExcel(Payment entry);
	List<Payment> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Payment> getCashBookBankBookReport(LocalDate from_date,LocalDate to_date,Long companyId,Integer type);
	List<Payment> findAllPaymentsOfCompany(Long comapny_id);
	int getCountByDate(Long companyId, String range, LocalDate date);
	PaymentDetails getPaymentDetailById(Long paymentDetailId);
	void updatePaymentDetail(PaymentDetails paymentDetails);
	void deletePaymentDetail(Long id);
	List<Payment> findallpaymententryofsales(long id);
	void diactivateByIdValue(Long payment_id, int status);
	void activateByIdValue(Long entityId);
	Double findpaidtds(Long purchase_id);
	void updateadvpaymentamount(Long payment_id, Double ramount, Double transactionvalue, Double tdsamount);
	/*List<Payment> getAdvancePaymentListForLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId);*/
	List<Payment> getPaymentListForLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId,Long bank_id);
	Long createforbill(Payment newpayment);
	void deletePayment(Long entityId);
	List<Payment> CashPaymentOfMoreThanRS10000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Payment> supplierPaymentHavingUnadjusted(LocalDate from_date,LocalDate to_date,Long companyId);
	void deletePaymentAginstadvance(Long entityId);
	List<Payment> getPaymentForLedgerReport(LocalDate from_date,LocalDate to_date,Long suppilerId,Long companyId);
	List<Payment> getPaymentForLedgerReport1(LocalDate to_date,Long suppilerId,Long companyId);
	List<Payment> supplirPaymentHavingGST0(LocalDate fromDate, LocalDate toDate, Long company_id);
	List<Payment> supplierPaymentWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Payment> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate fromDate, LocalDate toDate, Long company_id);
	public Payment isExcelVocherNumberExist(String vocherNo,Long companyId);
	List<Payment> getAllPaymentsAgainstAdvancePayment(Long advancePaymentId);
	List<Payment> getAllPaymentsAgainstOpeningBalance(Long openingBalanceId);
	List<Payment>  getAllOpeningBalanceAgainstPayment(Long supplierId,Long companyId);
	public List<Payment>  getAllOpeningBalanceAgainstPaymentForPeriod(Long supplierId, Long companyId,LocalDate todate);
	public Payment findOneopeningBalance(Long id) throws MyWebException;
	public List<Payment> getAllAdvancePaymentsAgainstSupplier(Long supplierId,Long companyId);
	public List<Payment> getPaymentForPurchase(Long supplierId, Long companyId,LocalDate toDate);
	public List<Payment> getAllAdvancePaymentsAgainstSupplierForPeriod(Long supplierId,Long companyId,LocalDate todate);
}