package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Product;

public interface IProductDAO extends IGenericDao<Product>{
	String updateByApproval(Long productId, int status);
	String deleteByIdValue(Long entityId);
	//Long saveProduct(Product product);
	Product findOneView(Long productId);
	Product isExists(String name, Long company_id);
	int isExistsProduct(String product_name, Long company_id);
	Integer checktype(Long company_id, long product_id);
	List<Product> findByStatus(int status);
	List<Product> findByStatus(int status, List<Long> companyIds);
	List<Product> findAllListing(Boolean flag);
	List<Product> findAllListing(Boolean flag, List<Long> companyIds);
	List<Product> findAllProductsOfCompany(Long CompanyId);
	List<Product> findAllListingProductsOfCompany(Long CompanyId, Boolean flag);
	Boolean approvedByBatch(List<String> productList,Boolean primaryApproval);
	Boolean rejectByBatch(List<String> productList);
	List<Product> findAllProductsOnlyOfCompany(Long CompanyId);
	Integer findAllProductsOfCompanyDashboard(long companyId);
	Integer findByStatusDashboard(int approvalStatusPrimary, List<Long> companyIds);
	Integer findAllDashboard();
	
}
