package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Suppliers;
import com.fasset.exceptions.MyWebException;

public interface ISupplierDAO extends IGenericDao<Suppliers>{
	Long saveSuppliersdao(Suppliers sup);
	Long createsup(Suppliers entity) throws MyWebException;
	String deleteByIdValue(Long entityId);
	String deleteSupplierSubLedger(Long suId, Long subId);
	String deleteSupplierProduct(Long suId, Long pId);
	String updateApprovalStatus(Long supplierId, int status);
	void updateSupplier(Suppliers suppliers);
	Suppliers findOneWithAll(Long supId);
	Suppliers isExistsSupplier(String owner_pan_no, String company_name, long company_id,String email);
	List<Suppliers> findByStatus(int status);
	List<Suppliers> findByStatus(int status, List<Long> companyIds);
	List<Suppliers> findAllSuppliersOfCompany(Long CompanyId);
	List<Suppliers> findAllSuppliersListing(Long flag);
	List<Suppliers> findAllSuppliersListing(Boolean flag, List<Long> companyIds);
	List<Suppliers> findAllSuppliersListingOfCompany(Long CompanyId, Long flag);
	List<Suppliers> findAllSuppliersOnlyOfCompany(Long CompanyId);
	int isExistsPan(String companypan, String companyname, Long company_id, String email);
	Boolean approvedByBatch(List<String> supplierList,Boolean primaryApproval);
	Boolean rejectByBatch(List<String> supplierList);
	List<Suppliers> findAllOPbalanceofsupplier(long company_id, long flag);
	List<Suppliers> findAllSuppliersOfCompanyDashboard(long companyId);
	List<Suppliers> findByStatusDashboard(int approvalStatusPending, List<Long> companyIds);
	List<Suppliers> findAllDashboard();
	Integer findAllSuppliersCountOfCompanyDashboard(long companyId);
	Integer findByStatusSuppliersCountDashboard(int approvalStatus, List<Long> companyIds);
	Integer findAllSuppliersCountDashboard();
	/*added to update customer tax rate field when master tax rate is updated */
	Integer updateTaxRate(long deducteeId,Float rate);
}
