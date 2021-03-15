/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Ledger;
import com.fasset.form.LedgerReport;


/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ILedgerDAO extends IGenericDao<Ledger> {

	Long saveLedgerDao(Ledger ledger);
	
	String deleteByIdValue(Long entityId);
	
	Ledger isExists(String name, Long company_id);
	
	int isExistsledger(String ledger_name, Long company_id);
	
	List<Ledger> findByStatus(int status);
	
	List<Ledger> findByStatus(int status, List<Long> companyIds);
	
	List<Ledger> findAllLedgersOfCompany(Long CompanyId);
	
	List<Ledger> findAllListing(Long flag);

	List<Ledger> findAllListingLedgersOfCompany(Long CompanyId,Long flag);
	
	List<Ledger> findAllLedgersWithSubledger(Long CompanyId);
	
	List<Ledger> findAllLedgersWithSubledger();
	
	List<Ledger> findLedgersOnlyOfComapany(Long company_id);
	
	List<Ledger> findAllListingExe(Boolean flag, List<Long> companyIds);
	
	/*List<LedgerReport> getLedgerReport(LocalDate from_date,LocalDate to_date,Long companyId,Long customerId,Long supplierId);
	
	List<LedgerReport> getLedgerReportforOpeningBalance(LocalDate from_date,LocalDate to_date,Long companyId,Long customerId,Long supplierId);*/
	
	String updateApprovalStatus(Long ledgerId, int status);
	
	Ledger findOneWithAll(Long leId);
	
	Long isExistsforcompany(String ledger_name, Long companyId);
	
	Boolean approvedByBatch(List<String> ledgerList,Boolean primaryApproval);
	
	Boolean rejectByBatch(List<String> ledgerList);

	Integer findAllLedgersOfCompanyDashboard(long companyId);

	Integer findByStatusDashboard(int approvalStatusPrimary, List<Long> companyIds);

	Integer findAllDashboard();
	
}