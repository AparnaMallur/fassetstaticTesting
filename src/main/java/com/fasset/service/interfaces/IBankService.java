package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Bank;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IBankService extends IGenericService<Bank>{
	String updateByApproval(Long bankId, int status);
	String deleteByIdValue(Long entityId);
	String addExcel(Bank bank);
	Bank findOneView(Long id);
	Bank isExists(Long account_no, Long company_id);
	List<Bank> findByStatus(int status);
	List<Bank> findByStatus(Long role_id,int status, Long userId);
	List<Bank> findAllBanksOfCompany(Long CompanyId);
	List<Bank> findAllListing(Boolean flag);
	List<Bank> findAllListing(Boolean flag, Long userId);
	List<Bank> findAllListingBanksOfCompany(Long CompanyId, Boolean flag);
	void addbankopeningbalance(Long company_id, Long sids, Long type, Double creditval, Double debitval);
	void addbanktransactionbalance(Long year_id,LocalDate date,Long company_id, Long sids, Long type, Double creditval, Double debitval, Long flag);
	Boolean approvedByBatch(List<String> bankList,Boolean primaryApproval);
	Boolean rejectByBatch(List<String> bankList);
	int isExistsAccount(Long account_no, Long company_id);
	void addbankopeningbalancenew(Long company_id, Long sids, Long type, Double creditval, Double debitval, String date,
			Long year);
	List<Bank> findAllOPbalanceofbank(long company_id, long l);
	Integer findAllBanksOfCompanyDashboard(long companyId);
	Integer findByStatusDashboard(Long role_Id,int approvalStatusPending, Long user_id);
	Integer findAllDashboard();
	List<Bank> findAllListingBanksOfCompany1(Long CompanyId, Boolean flag);
	List<Bank> findAllListing1(boolean flag);
	List<ViewApprovalsForm> viewApprovals(Long role_id,Long userId);
	List<Bank> findAllListing2(boolean flag, Long company_id);
	Double getClosingBalanceOfAllbanks(Long company_Id);

	
}