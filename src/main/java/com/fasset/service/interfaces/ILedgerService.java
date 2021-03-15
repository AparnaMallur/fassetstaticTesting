package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Ledger;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.LedgerReport;
import com.fasset.form.LedgerReportForm;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.generic.IGenericService;
/** * @author mayur suramwar
 * deven infotech pvt ltd. */

public interface ILedgerService extends IGenericService<Ledger> {

	String saveLedger(Ledger ledger);

	Ledger findOneWithAll(Long leId);

	Ledger addledger(Long companyId, Ledger ledger);

	Ledger addledgerwithopening(Long companyId, Ledger ledger);
	
	Ledger isExists(String name, Long company_id);

	int isExistsledger(String ledger_name, Long company_id);

	List<Ledger> findAll();

	List<Ledger> findAllLedgersWithSubledger(Long company_id);
	
	List<Ledger> findAllLedgersWithSubledger();
	
	List<Ledger> findLedgersOnlyOfComapany(Long company_id);

	List<Ledger> findByStatus(int status);

	List<Ledger> findByStatus(Long role_id,int status, Long userId);

	List<Ledger> findAllLedgersOfCompany(Long CompanyId);

	List<Ledger> findAllListing(Long flag);
	
	List<Ledger> findAllListingExe(Boolean flag, Long userId);

	List<Ledger> findAllListingLedgersOfCompany(Long CompanyId, Long flag);

	/*List<LedgerReport> getLedgerReport(LedgerReportForm ledgerReportForm);
	
	List<LedgerReport> getLedgerReportforOpeningBalance(LedgerReportForm ledgerReportForm);*/

	String deleteByIdValue(Long entityId);

	String updateByApproval(Long ledgerId, int status);
	
	void updateExcel(Ledger entity,Long id);

	void addledgeropeningbalance(Long company_id, Long sids, Long type, Double creditval, Double debitval);

	void addledgertransactionbalance(Long year_id,LocalDate date,Long company_id, Long sids, Long type, Double creditval, Double debitval, Long flag);

	Boolean approvedByBatch(List<String> ledgerList,Boolean primaryApproval);
	
	Boolean rejectByBatch(List<String> ledgerList);

	Ledger findOne(Long ledger_id) throws MyWebException;

	void addledgeropeningbalancenew(Long company_id, Long ledger_id, long l, Double creditval, Double debitval,
			Long year, String date);

	Integer findAllLedgersOfCompanyDashboard(long companyId);

	Integer findByStatusDashboard(Long role_id,int approvalStatusPrimary, Long user_id);

	Integer findAllDashboard();
	
	List<ViewApprovalsForm> viewApprovals(Long role_id,Long userId);
}
