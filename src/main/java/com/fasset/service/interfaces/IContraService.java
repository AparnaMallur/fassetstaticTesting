package com.fasset.service.interfaces;


import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.SubLedger;
import com.fasset.entities.Contra;
import com.fasset.entities.DebitNote;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IContraService extends IGenericService<Contra>{
	public String saveContra(Contra contra);
	public List <Contra> findAll();
	public String deleteByIdValue(Long entityId);
	public List<SubLedger> getsubledger();
	List<Contra> findAllContraEntry(Long CompanyId,Boolean flag);
	List<Contra> getContraListForLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId,Long bank_id);
	public Contra findOneWithAll(Long conId);
	public Long saveContraThroughExcel(Contra contra);
	List<Contra> findAllContraEntries(Long CompanyId);
	List<Contra> getCashBookBankBookReport(LocalDate from_date,LocalDate to_date,Long companyId,Integer type);
	List<Contra> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId);
	public Contra isExcelVocherNumberExist(String vocherNo,Long companyId);
}
