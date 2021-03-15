package com.fasset.dao.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PayrollEmployeeDetails;
import com.fasset.entities.PayrollSubledgerDetails;

public interface IManualJournalVoucherDAO extends IGenericDao<ManualJournalVoucher>{

	
	CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate);
	CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate);
	CopyOnWriteArrayList<GSTAutoJV>findAllGstJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate);
	CopyOnWriteArrayList<PayrollAutoJV>findAllPayrollJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate);
	public String deleteByIdValue(Long entityId);
	public Long saveMJV(ManualJournalVoucher entity);
	public List<ManualJVDetails> getAllMJVDetails(Long entityId);
//	/
	
	
	public String deleteByMJVIdValue(Long entityId);
	ManualJVDetails findOnemjvdetail(Long id);
	int getCountByDate(Long companyId, String range, LocalDate date);
	CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany();
	void saveOnemjvdetail(ManualJVDetails mjd);
	CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany(Long company_id);
	
}
