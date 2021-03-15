package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitDetails;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Payment;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IDebitNoteService extends IGenericService<DebitNote>{

	List<DebitNote> getReport(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	String deleteByIdValue(Long entityId);
	List<DebitNote> findAllDebitNoteOfCompany(Long CompanyId);
	List<DebitNote> cdnrList(Integer month, Long yearId, Long companyId);
	List<DebitNote> cdnurList(Integer month, Long yearId, Long companyId);
	Long deleteByDebitNoteDetailsId(Long entityId, Long companyId);
	DebitNote findOneView(Long id);
	List<DebitNote> getReportAgainstSubledger(Long Id,LocalDate from_date,LocalDate to_date,Long comId);
	void saveDebitNote(DebitNote note);
	List<DebitNote> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	void updateDebitNoteThroughExcel(DebitNote note);
	void updateDebitDetailsThroughExcel(Long year_id, LocalDate date,DebitDetails entity,Long debit_id, Long companyId);
	Long saveDebitNoteThroughExcel(DebitNote note);
	List<DebitNote> findAllDebitNotesOfCompany(Long CompanyId,Boolean flag);
	List<DebitNote> findAllDebitNotesOnlyOfCompany(Long CompanyId);
	void changeStatusOfDebitNoteThroughExcel(DebitNote note);
	DebitDetails getDebitDetails(Long id);
	void updateDebitDetails(DebitDetails debitDetails, long company_id);
	List<DebitNote> findByPurchaseId(long id);
	void diactivateByIdValue(Long debit_no_id);
	List<DebitNote> getDebitNoteForLedgerReport(LocalDate from_date,LocalDate to_date,Long suppilerId,Long companyId);
	public DebitNote isExcelVocherNumberExist(String vocherNo,Long companyId);
	public List<DebitNote>  getAllOpeningBalanceAgainstDebitNote(Long supplierId, Long companyId);//to include opening balance
	public List<DebitNote>  getAllOpeningBalanceAgainstDebitNoteForPeriod(Long supplierId, Long companyId,LocalDate todate);//to include opening balance
}
