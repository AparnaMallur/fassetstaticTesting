package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.form.GSTReport1Form;
import com.fasset.form.GSTdocsForm;
import com.fasset.form.HSNReportForm;
import com.fasset.form.IncomeAndExpenditureForm;

public interface IReportService {
	IncomeAndExpenditureForm getIncomeAndExpenditure(Long yearId, Long comapnyId);
	GSTReport1Form getGSTReport1(LocalDate from_date, LocalDate to_date, Company long1);
	List<HSNReportForm> getHsnList(LocalDate from_date, LocalDate to_date, Long companyId);
	List<GSTdocsForm> getOutwardSupplyList(LocalDate from_date, LocalDate to_date, Company company);
	List<GSTdocsForm> getInwardSupplyList(LocalDate from_date, LocalDate to_date, Company company);
	List<GSTdocsForm> getCreditNoteGstList(LocalDate from_date, LocalDate to_date, Company company);
	List<GSTdocsForm> getDebitNoteGstList(LocalDate from_date, LocalDate to_date, Company company);
}
