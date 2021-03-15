package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Suppliers;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.generic.IGenericService;

public interface ISuplliersService extends IGenericService<Suppliers>{

	String saveSuppliers(Suppliers sup);
	String deleteByIdValue(Long entityId);
	String deleteSupplierSubLedger(Long suId, Long subId);
	String deleteSupplierProduct(Long suId, Long pId);
	String updateByApproval(Long supplierId, int status);
	Suppliers findOneWithAll(Long supId);
	Suppliers isExistsSupplier(String owner_pan_no, String company_name, long company_id,String email);
	List<Suppliers> findAll();
	List<Suppliers> findByStatus(int status);
	List<Suppliers> findByStatus(Long role_id,int status, Long userId);
	List<Suppliers> findAllSuppliersOfCompany(Long CompanyId);
	List<Suppliers> findAllSuppliersListing(Long flag);
	List<Suppliers> findAllSuppliersListing(Boolean flag, Long userId);
	List<Suppliers> findAllSuppliersListingOfCompany(Long CompanyId, Long flag);
	List<Suppliers> findAllSuppliersOnlyOfCompany(Long CompanyId);
	void addsupplieropeningbalance(Long company_id, Long sids, Long type, Double creditval, Double debitval);
	void addsuppliertrsansactionbalance(Long year, LocalDate date,Long company_id, Long sids, Long type, Double creditval, Double debitval, Long flag);
	int isExistsPan(String companypan, String companyname, Long company_id, String email);
	Boolean approvedByBatch(List<String> supplierList,Boolean primaryApproval);
	Boolean rejectByBatch(List<String> supplierList);
	void updateExcel(Suppliers entity);
	void addsupplieropeningbalancenew(Long company_id, Long sids, Long type, Double creditval, Double debitval,
			String date, Long year);
	List<Suppliers> findAllOPbalanceofsupplier(long company_id, long l);
	List<Suppliers> findAllSuppliersOfCompanyDashboard(long companyId);
	List<Suppliers> findByStatusDashboard(int approvalStatusPending, Long user_id);
	List<Suppliers> findAllDashboard();
	Integer findByStatusSupplierCountDashboard(Long role_id, int approvalStatus, Long user_id);
	Integer findAllSupplierCountDashboard();
	Integer findAllSupplierCountOfCompanyDashboard(Long CompanyId);
	List<ViewApprovalsForm> viewApprovals(Long role_id,Long userId);
	

}
