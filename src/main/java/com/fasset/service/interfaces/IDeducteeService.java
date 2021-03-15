package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.DebitNote;
import com.fasset.entities.Deductee;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.generic.IGenericService;
import org.joda.time.LocalDate;
public interface IDeducteeService extends IGenericService<Deductee>{

	public List<Deductee> findAllDeducteeOfCompany(Long CompanyId);
	public String addDeductee(Deductee entity);
	List<Deductee> findAllDeducteeListing();
	public String deleteDeducteeByIdValue(Long id);
	Float getTDSRate(LocalDate effectiveDate,Long tdsTypeId);
	Float getTDSRateByTdsType(Long tdsTypeId);
	Deductee isExists1(String name,LocalDate effectiveDate);
}
