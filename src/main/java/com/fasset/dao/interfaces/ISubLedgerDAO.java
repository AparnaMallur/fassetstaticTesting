/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;
import java.util.Set;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Ledger;
import com.fasset.entities.SubLedger;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ISubLedgerDAO  extends IGenericDao<SubLedger>{

	String deleteByIdValue(Long entityId);
	String updateApprovalStatus(Long subLedgerId, int status);
	void updateSubledger(SubLedger subLedger);
	Long saveSubLedgerDao (SubLedger group);
	Long createSubLedger(SubLedger entity) throws MyWebException;
	SubLedger findOneWithAll(Long subId);
	SubLedger isExists(String name,Long company_id);
	SubLedger findOne(String string, Long company_id);
	List<SubLedger> getSubledgerByGroup(String groupName, Long companyId);
	List<SubLedger> findByStatus(int status);
	List<SubLedger> findByStatus(int status, List<Long> companyIds);
	List<SubLedger> findAllSubLedgersOfCompany(Long company_id);
	List<SubLedger> findAllApprovedByCompanyForCustomer(Long CompanyId);//new requirement get income and discount subledger only for customer
	List<SubLedger> findAllApprovedByCompanyForSupplier(Long CompanyId);//new requirement get expenses and discount subledger only for supplier
	List<SubLedger> findAllSubLedgersListing(Long flag);
	List<SubLedger> findAllSubLedgersListing(Boolean flag, List<Long> companyIds);
	List<SubLedger> findAllSubLedgersListingOfCompany(Long CompanyId, Long flag);
	List<SubLedger> findAllSubLedgerWithLedger();
	List<SubLedger> findAllSubLedgerWithLedger(List<Long> companyIds);
	List<SubLedger> findAllSubLedgerWithLedgerForCust(List<Long> companyIds,boolean IsSuperUser);
	List<SubLedger> findAllSubLedgerWithLedgerForSuppl(List<Long> companyIds,boolean IsSuperUser);
	List<SubLedger> setdefaultdata();
	Set<SubLedger> findAllSubLedgerWithRespectToLedger(Long ledgerId);
	List<SubLedger> findAllSubLedgersOfCompanywithdefault(Long company_id);
	Boolean approvedByBatch(List<String> subledgerList,Boolean primaryApproval);
	Boolean rejectByBatch(List<String> subledgerList);
	int isExistssubledger(String subledger_name, Long company_id);
	List<SubLedger> findAllSubLedgersOnlyOfCompany(Long CompanyId);
	List<SubLedger> findAllSubLedgersofSuppliers(Long CompanyId);// Added to get subledgers of suplliers
	List<SubLedger> findAllOPbalanceofsubledger(long company_id, long flag);
	List<SubLedger> findAllSubLedgersListingOfCompany1(Long companyId, Long flag);
	void updateExcelSubLedger(SubLedger subledger);
	Integer findByStatusDashboard(int approvalstatus, List<Long> companyIds);
	Integer findAllDashboard();
	Integer findAllSubLedgerOfCompanyDashboard(Long CompanyId);
	
	Set<SubLedger> findAllSubLedgersForDepreciation(Long company_id); 
	
	Ledger subLedgerOfNameDepreciaition(Long company_id);
	
	
}
