/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ICustomerService extends IGenericService<Customer>{

	String saveCustomers(Customer cus);
	String updateByApproval(Long supplierId, int status);
	String deleteByIdValue(Long entityId);
	String deleteCustomerSubLedger(Long cId, Long subId);
	String deleteCustomerProduct(Long cId, Long pId);
	Customer findOneWithAll(Long cusId);
	Customer isExistsCustomer(String companypan, String firm_name, long company_id, String email);
	List<Customer> findAll();
	List<Customer> findByStatus(int status);
	List<Customer> findByStatus(Long role_id,int status, Long userId);
	List<Customer> findAllCustomersOfCompany(Long CompanyId);
	List<Customer> findAllCustomersOfCompanyInclPending(Long CompanyId);
	List<Customer> findAllCustomerListing(Long flag);
	List<Customer> findAllCustomerListing(Boolean flag, Long userId);
	List<Customer> findAllCustomerListingOfCompany(Long CompanyId, Long flag);
	List<Customer> findAllCustomersOnlyOFCompany(Long CompanyId);
	void addcustomeropeningbalance(Long company_id, Long sids, Long type, Double creditval, Double debitval);
	Boolean approvedByBatch(List<String> customerList,Boolean primaryApproval);
	Boolean rejectByBatch(List<String> customerList);
	int isExistsPan(String companypan, String companyname, Long company_id, String email);
	void updateExcel(Customer customer);
	void addcustomeropeningbalancenew(Long company_id, Long sids, Long type, Double creditval, Double debitval,
			String date, Long year);
	
	void addcustomertransactionbalance(Long year, LocalDate date, Long company_id, Long sids, Long type, Double creditval,
			Double debitval, Long flag);
	List<Customer> findAllOPbalanceofcustomer(long company_id, long flag);
	List<Customer> findAllCustomersOfCompanyDashboard(long companyId);
	List<Customer> findByStatusDashboard(int approvalStatusPending, Long user_id);
	List<Customer> findAllDashboard();
	Integer findByStatusCustomerCountDashboard(Long role_id, int approvalStatus, Long user_id);
	Integer findAllCustomerCountDashboard();
	Integer findAllCustomerCountOfCompanyDashboard(Long CompanyId);
	List<ViewApprovalsForm> viewApprovals(Long role_id,Long userId);

	/* added by shrinivas */
	public Customer findGstNoForCustomerByCompany(long companyid, long custid);

}
