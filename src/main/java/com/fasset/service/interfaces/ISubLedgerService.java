/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import com.fasset.entities.Contra;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.Ledger;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.Payment;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.entities.YearEndJV;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ISubLedgerService extends IGenericService<SubLedger> {

	String saveSubLedger(SubLedger group);
	String saveExcelSubLedger(SubLedger group);
	String deleteByIdValue(Long entityId);
	String updateByApproval(Long subLedgerId, int status);
	Set<SubLedger> findAllSubLedgerWithRespectToLedger(Long ledgerId);
	List<SubLedger> findAll();
	List<SubLedger> findAllApprovedByCompanywithdefault(Long CompanyId);
	List<SubLedger> getSubledgerByGroup(String groupName, Long companyId);
	List<SubLedger> findByStatus(int status);
	List<SubLedger> findByStatus(Long role_id,int status, Long userId);
	List<SubLedger> findAllSubLedgersListing(Long flag);
	List<SubLedger> findAllSubLedgersListing(Boolean flag, Long userId);
	List<SubLedger> findAllSubLedgersListingOfCompany(Long CompanyId,Long flag);
	List<SubLedger> findAllApprovedByCompany(Long CompanyId);
	List<SubLedger> findAllApprovedByCompanyForCustomer(Long CompanyId);//new requirement get income and discount subledger only for customer
	List<SubLedger> findAllApprovedByCompanyForSupplier(Long CompanyId);//new requirement get expenses and discount subledger only for supplier
	List<SubLedger> findAllSubLedgerWithLedger();
	List<SubLedger> findAllSubLedgerWithLedger(Long userId);
	List<SubLedger> findAllSubLedgerWithLedgerForCustSupplier(Long userId,boolean IsCustomer,boolean IsSuperUser);
	SubLedger findOneWithAll(Long subId);
	void setdefaultdata(Long company_id);
	void createsubledgerwithopening(Long company_id, Long id, Long type, Double credit, Double debit);
	void addsubledgeropeningbalance(Long company_id, Long sids, Long type, Double creditval, Double debitval);
	void addsubledgertransactionbalance(Long year_id,LocalDate date,Long company_id, Long sids, Long type, Double creditval, Double debitval, Long flag,SalesEntry sales,Receipt receipt,PurchaseEntry purchase,Payment payment,CreditNote credit,DebitNote debit,Contra contra,ManualJournalVoucher mjv,PayrollAutoJV payAutoJv,GSTAutoJV gstAutoJV,DepreciationAutoJV depriAutoJV,YearEndJV yearendAutoJV);
	Boolean approvedByBatch(List<String> subledgerList,Boolean primaryApproval);//test
	Boolean rejectByBatch(List<String> subledgerList);
	int isExistssubledger(String subledger_name, Long company_id);
	SubLedger isExists(String name, Long company_id);
	void setindustrydata(long company_id);
	List<SubLedger> findAllSubLedgersOnlyOfCompany(Long CompanyId);
	List<SubLedger> findAllSubLedgersofSuppliers(Long CompanyId); // Added to get subledgers of suplliers
	void addsubledgeropeningbalancenew(Long company_id, Long sids, Long type, Double creditval, Double debitval,
			String date, Long year);
	List<SubLedger> findAllOPbalanceofsubledger(long company_id, long l);
	List<SubLedger> findAllSubLedgersListingOfCompany1(Long CompanyId,Long flag);
	void updateExcelSubLedger(SubLedger subledger);
	Integer findByStatusDashboard(Long role_id, int approvalStatus, Long user_id);
	Integer findAllDashboard();
	Integer findAllSubLedgerOfCompanyDashboard(Long CompanyId);
	List<ViewApprovalsForm> viewApprovals(Long role_id,Long userId);
	Double getClosingBalanceOfCashInHand(Long company_Id);
	public Set<SubLedger> findAllSubLedgersForDepreciation(Long company_id);
	public Ledger subLedgerOfNameDepreciaition(Long company_id);
	
}
