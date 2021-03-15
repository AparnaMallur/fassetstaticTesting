package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.UserForm;
import com.fasset.service.interfaces.generic.IGenericService;

public interface ICompanyService extends IGenericService<Company> {
	
	Long saveSignUpDetails(Company company, User user);
	void addCompany(UserForm userForm) throws MyWebException;
	void updatebycomp(Company entity,String amount) throws MyWebException;
	void companyDeactivate(Company company);
	int isExistscompanyname(String company_name);
	String deleteByIdValue(long id);
	Company getCompanyWithAllUsers(Long companyId);
	Company getCompanyWithIndustrytype(Long companyId);
	Company findOneWithAll(Long compId);
	List<Company> findAll();
	List<Company> getCompanyByStatus(Integer status);
	List<Company> getAllCompaniesExe(Long userId);
	List<Company> getApprovedCompanies();
	List<Company> getApprovedCompanies(Long userId);
	List<Company> getAllCompaniesWithLedgerlist();
	List<Company> getAllCompaniesWithLedgerlist(Long userId);
	List<Company> getAllCompaniesWithLedgerAndCustomerlist();
	List<Company> getAllCompaniesWithLedgerAndSuppilerlist();
	List<Company> getAllCompaniesWithProducts();
	List<Company> getAllCompaniesWithAccountGrouplist();
	List<Company> getAllCompaniesOnly();
	List<String> getVoucherRange(Long companyId);
	Boolean setDefaultBank(Long bankId, Long compId, Long flag);
	Boolean setTrialBalance(Long compId,LocalDate openingBalancedate,Long YearId);
	Company getCompanyWithCompanyStautarType(Long companyId);
	long getdefaultbank(long company_id);
	int ismailsent(Long company_id, LocalDate date);
	void updatemailsent(Long company_id, LocalDate date);
	List<Company> FindAlllist();
	List<Company> FindAlllistexe(Long user_id);
	List<Company> FindAllInactiveCompanies(Long role, Long user_id);
	void updateCompany(Company company);
	
	Company getCompanyById(Long companyId);
}