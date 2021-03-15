package com.fasset.dao.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.CreditDetails;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Receipt;

public interface ICreditNoteDAO extends IGenericDao<CreditNote>{
	List<CreditNote> getReport(Long id,LocalDate from_date,LocalDate to_date, Long companyId);
	String deleteByIdValue(Long entityId);
	List<CreditNote> findAllCreditNoteOfCompany(Long CompanyId,Boolean flag);
	List<CreditNote> findBySalesId(long salesId);
	List<CreditNote> getCdnrList(LocalDate from_date,LocalDate to_date, Long companyId);
	List<CreditNote> getCdnurList(LocalDate from_date,LocalDate to_date, Long companyId,Long stateId);
	String deleteByCebitNoteDetailsId(Long entityId);
	CreditNote findOneView(Long creditNoteId);
	List<CreditNote> getReportAgainstSubledger(Long id,LocalDate from_date,LocalDate to_date,Long companyId);
	Long saveCreditNoteTroughExcel(CreditNote note);
	Integer getCountByYear(Long yearId, Long companyId, String range);
	void saveCreditDetailsTroughExcel(CreditDetails entity,Long credit_id);
	int getCountByDate(Long companyId, String range, LocalDate date);
	List<CreditNote> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	List<CreditNote> findAllCreditNotesOfCompany(Long CompanyId);
	void updateCreditNoteThroughExcel(CreditNote note);
	void updateCreditDetailsThroughExcel(CreditDetails entity,Long credit_id);
	Long saveCreditNoteThroughExcel(CreditNote note);
	CreditDetails getCreditDetailsById(Long creditDetailsId);
	void updateCreditDetail(CreditDetails creditDetails);
	void changeStatusOfCreditNoteThroughExcel(CreditNote note);
	void diactivateByIdValue(Long credit_no_id);
	void activateByIdValue(Long entityId);
	List<CreditNote> getCreditNoteForLedgerReport(LocalDate from_date,LocalDate to_date,Long customerId,Long companyId);
	List<CreditNote> getCreditNoteGstList(LocalDate from_date, LocalDate to_date, Long companyId);
	Integer getCancelCreditNoteGstList(LocalDate from_date, LocalDate to_date, Long companyId);
	public CreditNote isExcelVocherNumberExist(String vocherNo,Long companyId);
	List<CreditNote> getCreditNoteForSale(Long companyId,LocalDate toDate);
	public List<CreditNote> getAllOpeningBalanceAgainstcreditnote(Long customerId, Long companyId);//to include opening balance option
	public List<CreditNote> getAllOpeningBalanceAgainstCreditNoteForPeriod(Long customerId, Long companyId,LocalDate toDate);
}
