package com.fasset.dao.interfaces;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Contra;

public interface IContraDAO extends IGenericDao<Contra>{
	Long saveContradao(Contra contra);
	List<SubLedger> getsubledger();
	String deleteByIdValue(Long entityId);
	List<Contra> findAllContraEntry(Long CompanyId,Boolean flag);
	List<Contra> getContraListForLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId,Long bank_id);
	public Contra findOneWithAll(Long conId);
	Integer getCountByYear(Long yearId, Long companyId, String range);
	public Long saveContraThroughExcel(Contra contra);
	List<Contra> findAllContraEntries(Long CompanyId);
	List<Contra> getCashBookBankBookReport(LocalDate from_date,LocalDate to_date,Long companyId,Integer type);
	int getCountByDate(Long companyId, String range, LocalDate date);
	List<Contra> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	public Contra isExcelVocherNumberExist(String vocherNo,Long companyId);
}


