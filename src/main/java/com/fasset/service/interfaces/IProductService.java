package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.Product;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IProductService extends IGenericService<Product>{	 
	String addProductExcel(Product product);
	String updateByApproval(Long productId, int status);
	String deleteByIdValue(Long entityId);
	Product findOneView(Long productId);
	Product isExists(String product_name, Long company_id); 
	int isExistsProduct(String product_name, Long company_id);
	void updateExcel(Product entity) throws MyWebException;
	void setdefaultdata(long company_id, User user);
	Integer checktype(long company_id, Long product_id);
	List<Product> findByStatus(int status);
	List<Product> findByStatus(Long role_id,int status, Long userId);
	List<Product> findAllListing(Boolean flag);
	List<Product> findAllListing(Boolean flag, Long userId);
	List<Product> findAllProductsOfCompany(Long CompanyId);
	List<Product> findAllListingProductsOfCompany(long company_id, Boolean flag); 
	Boolean approvedByBatch(List<String> productList,Boolean primaryApproval);
	Boolean rejectByBatch(List<String> productList);
	List<Product> findAllProductsOnlyOfCompany(Long CompanyId);
	Integer findAllProductsOfCompanyDashboard(long companyId);
	Integer findByStatusDashboard(Long role_id, int approvalStatus, Long user_id);
	Integer findAllDashboard();
	List<ViewApprovalsForm> viewApprovals(Long role_id,Long userId);
}
