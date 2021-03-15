package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Deductee;
import com.fasset.entities.SubLedger;
import org.joda.time.LocalDate;

public interface IDeducteeDao extends IGenericDao<Deductee>{
	public List<Deductee> findAllDeducteeOfCompany(Long CompanyId);
	public List<Deductee> findAllDeductee();
	public Long saveDeductee(Deductee deductee); 
	List<Deductee> findAllDeducteeListing();
	public String deleteDeducteeByIdValue(Long id);
	Float getTDSRate(LocalDate effectiveDate,Long tdsTypeId);
	Float getTDSRateByTdsType(Long tdsTypeId);
	Deductee isExists1(String name,LocalDate effectiveDate);
}
