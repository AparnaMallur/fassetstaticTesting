package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PaymentDetails;
import com.fasset.entities.PurchaseEntry;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IPaymentService extends IGenericService<Payment>{
	Long getCount();
	List<Payment> getReport(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	//List<PurchaseEntry> findAllSuppliersWithDate(LocalDate from_date, LocalDate to_date, Long company_id);
	String deleteByIdValue(Long entityId);
	List<Payment> getAdvancePaymentList(Long supplierId, Long companyId, Long yid);
	List<Payment> getExpenditure(Long yearId, Long companyId);
	List<Payment> getATList(Integer month, Long yearId, Long companyId);
	List<Payment> getATAdjList(Integer month, Long yearId, Long companyId);
	Payment findOneWithAll(Long paymentId);
	Long savePaymentThroughExcel(Payment entry);
	void savePaymentDetailsThroughExcel(PaymentDetails details,Long payment_id);
	List<Payment> findAllPaymentOfCompany(Long comapny_id, Boolean flag );
	List<Payment> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	void updatePaymentThroughExcel(Payment entry);
	void cahngeStatusOfPaymentThroughExcel(Payment entry);
	void updatePaymentDetailsThroughExcel(PaymentDetails details,Long payment_id);
	List<Payment> getCashBookBankBookReport(LocalDate from_date,LocalDate to_date,Long companyId,Integer type);
	List<Payment> findAllPaymentsOfCompany(Long comapny_id);
	PaymentDetails getPaymentDetailById(Long paymentDetailId);
	void updatePaymentDetail(PaymentDetails paymentDetails, long company_id);
	Long deletePaymentDetail(Long id, long company_id);
	List<Payment> findallpaymententryofsales(long id);
	void diactivateByIdValue(Long payment_id);
	void activateByIdValue(Long entityId);
    Double findpaidtds(Long purchase_id);
	List<Payment> getPaymentListForLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId,Long bank_id);
	void deletePayment(Long entityId);
	List<Payment> CashPaymentOfMoreThanRS10000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Payment> supplierPaymentHavingUnadjusted(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Payment> getPaymentForLedgerReport(LocalDate from_date,LocalDate to_date,Long suppilerId,Long companyId);
	List<Payment> getPaymentForLedgerReport1(LocalDate to_date,Long suppilerId,Long companyId);
	List<Payment> supplirPaymentHavingGST0(LocalDate fromDate, LocalDate toDate, Long company_id);
	List<Payment> supplierPaymentWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId);
	List<Payment> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate fromDate, LocalDate toDate, Long company_id);
	public Payment isExcelVocherNumberExist(String vocherNo,Long companyId);
	List<Payment> getAllPaymentsAgainstAdvancePayment(Long advancePaymentId);
	public List<Payment>  getAllOpeningBalanceAgainstPayment(Long supplierId, Long companyId);
	public List<Payment>  getAllOpeningBalanceAgainstPaymentForPeriod(Long supplierId, Long companyId,LocalDate todate);
	public List<Payment> getAllAdvancePaymentsAgainstSupplier(Long supplierId,Long companyId);
	public List<Payment> getAllAdvancePaymentsAgainstSupplierForPeriod(Long supplierId,Long companyId,LocalDate todate);
}