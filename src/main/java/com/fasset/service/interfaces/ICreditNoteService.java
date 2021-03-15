package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.CreditDetails;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Receipt;
import com.fasset.service.interfaces.generic.IGenericService;

public interface ICreditNoteService extends IGenericService<CreditNote>{
	List<CreditNote> getReport(Long id,LocalDate from_date,LocalDate to_date,Long companyId);
	String deleteByIdValue(Long entityId);
	List<CreditNote> findAllCreditNoteOfCompany(Long CompanyId,Boolean flag);
	Long deleteByCebitNoteDetailsId(Long entityId, Long companyId);
	CreditNote findOneView(Long creditNoteId);
	List<CreditNote> getReportAgainstSubledger(Long id,LocalDate from_date,LocalDate to_date,Long companyId);	
	List<CreditNote> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	List<CreditNote> findAllCreditNotesOfCompany(Long CompanyId);
	void updateCreditNoteThroughExcel(CreditNote note);
	void updateCreditDetailsThroughExcel(Long year_id, LocalDate date,CreditDetails entity,Long credit_id, Long companyId);
	Long saveCreditNoteThroughExcel(CreditNote note);
	void changeStatusOfCreditNoteThroughExcel(CreditNote note);
	
	CreditDetails getCreditDetails(Long id);
	void updateCreditDetails(CreditDetails creditDetails, long company_id);
	List<CreditNote> findBySalesId(long id);
	void diactivateByIdValue(Long credit_no_id);
	void activateByIdValue(Long credit_no_id);
	List<CreditNote> getCreditNoteForLedgerReport(LocalDate from_date,LocalDate to_date,Long customerId,Long companyId);
	public CreditNote isExcelVocherNumberExist(String vocherNo,Long companyId);
	List<CreditNote> getCreditNoteForSale(Long companyId,LocalDate toDate);
	List<CreditNote> getAllOpeningBalanceAgainstcreditnote(Long customerId, Long companyId) ; // added to include opening balance option
	public List<CreditNote> getAllOpeningBalanceAgainstCreditNoteForPeriod(Long customerId, Long companyId,LocalDate toDate);
}
