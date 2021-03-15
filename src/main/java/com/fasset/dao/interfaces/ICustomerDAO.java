/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.exceptions.MyWebException;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMAny;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ICustomerDAO extends IGenericDao<Customer>{
	Long saveCustomersDao(Customer cus);
	Long createcust(Customer entity) throws MyWebException;
	void updateCustomer(Customer customer);
	String updateApprovalStatus(Long supplierId, int status);
	String deleteCustomerSubLedger(Long cId, Long subId);
	String deleteCustomerProduct(Long cId, Long pId);
	String deleteByIdValue(Long entityId);
	Customer findOneWithAll(Long cusId);
	Customer isExistsCustomer(String company_pan_no, String firm_name, long company_id, String email);
	List<Customer> findByStatus(int status);
	List<Customer> findByStatus(int status, List<Long> companyIds);
	List<Customer> findAllCustomerListing(Long flag);
	List<Customer> findAllCustomerListing(Boolean flag, List<Long> companyIds);
	List<Customer> findAllCustomerListingOfCompany(Long CompanyId,Long flag);
	List<Customer> findAllCustomersOfCompany(Long CompanyId);
	List<Customer> findAllCustomersOfCompanyInclPending(Long CompanyId);
	List<Customer> findAllCustomersOnlyOFCompany(Long CompanyId);
	Boolean approvedByBatch(List<String> customerList,Boolean primaryApproval);
	Boolean rejectByBatch(List<String> customerList);
	int isExistsPan(String companypan, String companyname, Long company_id, String email);
	List<Customer> findAllOPbalanceofcustomer(long company_id, long flag);
	List<Customer> findAllCustomersOfCompanyDashboard(long companyId);
	List<Customer> findByStatusDashboard(int approvalStatusPending, List<Long> companyIds);
	List<Customer> findAllDashboard();
	Integer findAllCustomerCountOfCompanyDashboard(long companyId);
	Integer findByStatusCustomerCountDashboard(int approvalStatus, List<Long> companyIds);
	Integer findAllCustomerCountDashboard();
	/*added to update customer tax rate field when master tax rate is updated */
	Integer updateTaxRate(long deducteeId,Float rate);
	/* added by shrinivas */
	public Customer findGstNoForCustomerByCompany(long companyid, long custid);
}
