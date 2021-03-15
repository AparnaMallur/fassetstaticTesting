package com.fasset.service.interfaces;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;

import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PayrollEmployeeDetails;
import com.fasset.entities.PayrollSubledgerDetails;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IManualJournalVoucherService extends IGenericService<ManualJournalVoucher>{

	CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate);
	CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate);
	CopyOnWriteArrayList<GSTAutoJV>findAllGstJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate);
	CopyOnWriteArrayList<PayrollAutoJV>findAllPayrollJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate);
	public String deleteByIdValue(Long entityId);
	public void saveMJV(ManualJournalVoucher entity);
	public List<ManualJVDetails> getAllMJVDetails(Long entityId) ;

	public String deleteByMJVIdValue(Long entityId) ;
	CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany();
	CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany(Long company_id);
}
