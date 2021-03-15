package com.fasset.dao.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitDetails;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Payment;

public interface IDebitNoteDAO extends IGenericDao<DebitNote>{

	List<DebitNote> getReport(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	String deleteByIdValue(Long entityId);
	List<DebitNote> findAllDebitNoteOfCompany(Long CompanyId);
	List<DebitNote> findByPurchaseId(Long purchaseId);
	List<DebitNote> cdnrList(Integer month, Long yearId, Long companyId);
	List<DebitNote> cdnurList(Integer month, Long yearId, Long companyId);
	String deleteByDebitNoteDetailsId(Long entityId);
	DebitNote findOneView(Long id);
	List<DebitNote> getReportAgainstSubledger(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	void saveDebitNote(DebitNote note);
	Integer getCountByYear(Long yearId, Long companyId, String range);
	int getCountByDate(Long companyId, String range, LocalDate date);
	List<DebitNote> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	void updateDebitNoteThroughExcel(DebitNote note);
	void updateDebitDetailsThroughExcel(DebitDetails entity,Long debit_id);
	Long saveDebitNoteThroughExcel(DebitNote note);
	List<DebitNote> findAllDebitNotesOfCompany(Long CompanyId,Boolean flag);
	DebitDetails getDebitDetailsById(Long debitDetailId);
	void updateDebitDetail(DebitDetails debitDetails);
	List<DebitNote> findAllDebitNotesOnlyOfCompany(Long CompanyId);
	void changeStatusOfDebitNoteThroughExcel(DebitNote note);
	void diactivateByIdValue(Long debit_no_id);
	List<DebitNote> getDebitNoteForLedgerReport(LocalDate from_date,LocalDate to_date,Long suppilerId,Long companyId);
	List<DebitNote> getDebitNoteGstList(LocalDate from_date, LocalDate to_date, Long companyId);
	Integer getCancelDebitNoteGstList(LocalDate from_date, LocalDate to_date, Long companyId);
	public DebitNote isExcelVocherNumberExist(String vocherNo,Long companyId);
	List<DebitNote> getDebitNoteNoteForPurchase(Long companyId,LocalDate toDate);
	List<DebitNote>  getAllOpeningBalanceAgainstDebitNote(Long supplierId, Long companyId);//to include opening balance
	public List<DebitNote>  getAllOpeningBalanceAgainstDebitNoteForPeriod(Long supplierId, Long companyId,LocalDate todate);//to include opening balance
}
