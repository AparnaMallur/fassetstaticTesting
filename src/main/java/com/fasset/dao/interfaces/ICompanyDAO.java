package com.fasset.dao.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Company;

public interface ICompanyDAO extends IGenericDao<Company>{
	Long saveCompany(Company entity);
	String deleteByIdValue(long id);
	int isExistscompanyname(String company_name);
	Company getCompanyWithAllUsers(Long companyId);
	Company getCompanyWithIndustrytype(Long companyId);
	Company findOneWithAll(Long compId);
	List<String> getVoucherRange(Long companyId,String VoucherRange);
	List<Company> getCompanyByStatus(Integer status);
	List<Company> getApprovedCompanies();
	List<Company> getApprovedCompanies(List<Long> companyIds);
	List<Company> getAllCompaniesWithLedgerlist();
	List<Company> getAllCompaniesWithLedgerlist(List<Long> companyIds);
	List<Company> getAllCompaniesWithLedgerAndCustomerlist();
	List<Company> getAllCompaniesWithLedgerAndSuppilerlist();
	List<Company> getAllCompaniesWithAccountGrouplist();
	List<Company> getAllCompaniesWithProducts();
	List<Company> getAllCompaniesOnly();
	Company getCompanyWithCompanyStautarType(Long companyId);
	int ismailsent(Long company_id, LocalDate date);
	void updatemailsent(Long company_id, LocalDate date);
	List<Company> FindAlllist();
	List<Company> FindAlllistexe(List<Long> companyIds);
	Long createComapny(Company comapny);
	List<Company> FindAllInactiveCompanies(Long role, Long user_id);
	
	Company getCompanyById(Long companyId);
	
	}