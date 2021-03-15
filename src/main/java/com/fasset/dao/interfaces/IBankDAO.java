package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Bank;
import com.fasset.exceptions.MyWebException;

public interface IBankDAO extends IGenericDao<Bank>{

	Long createBank(Bank entity) throws MyWebException;	
	String updateApprovalStatus(Long bankId, int status);
	String deleteByIdValue(Long entityId);
	Bank findOneView(Long id);
	Bank isExists(Long account_no, Long company_id);
	List<Bank> findByStatus(int status);
	List<Bank> findByStatus(int status, List<Long> companyIds);
	List<Bank> findAllBanksOfCompany(Long CompanyId);
	List<Bank> findAllListingBanksOfCompany(Long CompanyId, Boolean flag);
	List<Bank> findAllListing(Boolean flag);
	List<Bank> findAllListing(Boolean flag, List<Long> companyIds);
	Boolean approvedByBatch(List<String> bankList,Boolean primaryApproval);
	Boolean rejectByBatch(List<String> bankList);
	int isExistsAccount(Long account_no, Long company_id);
	List<Bank> findAllOPbalanceofbank(long company_id, long flag);
	Integer findAllBanksOfCompanyDashboard(long companyId);
	Integer findByStatusDashboard(int approvalStatusPending, List<Long> companyIds);
	Integer findAllDashboard();
	List<Bank> findAllListingBanksOfCompany1(Long companyId, Boolean flag);
	List<Bank> findAllListing1(boolean flag);
	
	List<Bank> findAllListing2(boolean flag, Long company_id);
	
	
	
}