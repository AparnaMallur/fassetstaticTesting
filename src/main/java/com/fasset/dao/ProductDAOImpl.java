package com.fasset.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Bank;
import com.fasset.entities.Customer;
import com.fasset.entities.Product;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Stock;
import com.fasset.entities.Suppliers;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IStockService;

@Repository
@Transactional
public class ProductDAOImpl extends AbstractHibernateDao<Product> implements IProductDAO {

	@Autowired
	private IStockService stockService;

	@Override
	public Product findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("product_id", id));
		criteria.setFetchMode("company", FetchMode.JOIN);
		return (Product) criteria.uniqueResult();
	}
	
	@Override
	public Product findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("product_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.addOrder(Order.asc("product_name"));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findAllListing(Boolean flag) {	
		
		List<Product> list =  new ArrayList<Product>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT pro from Product pro LEFT JOIN FETCH pro.company company "
					+"WHERE pro.flag =:flag order by pro.product_id desc");
			query.setParameter("flag", flag);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Product)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
              session.clear();
			 
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		 
		 return list;
	
	}

	@Override
	public void create(Product entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public void update(Product entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public void merge(Product entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Product entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Long id) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(String id) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean validatePreTransaction(Product entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(Product entity) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findByStatus(int status) {
		/*Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("product_approval", status));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.addOrder(Order.desc("product_id"));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<Product> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT pro from Product pro WHERE pro.product_approval=:product_approval and pro.flag=:flag and pro.status=:status order by pro.product_id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("product_approval", status);
	ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
	
	  while (scrollableResults.next()) {
		  list.add((Product)scrollableResults.get()[0]);
		  session.evict(scrollableResults.get()[0]);
         }
	  session.clear();
	  return list;
	}

	@Override
	public String updateByApproval(Long productId, int status) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update Product set product_approval =:status where product_id =:productId");
		query.setParameter("productId", productId);
		query.setParameter("status", status);
		String msg = "";
		try {
			query.executeUpdate();
			if (status == 1) {
				msg = "Rejected successfully";
			} else {
				if (status == 2) {
					msg = "Product Primary Approval Done, Sent For Secondary Approval ";
				} else {
					msg = "Product Secondary Approval Done";
				}
			}
		} catch (Exception e) {
			msg = "You can't change status";
		}
		return msg;
	}

	@Override
	public Product isExists(String name) {
		Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("product_name", name));
		return (Product) criteria.uniqueResult();
	}

	@Override
	public Product isExists(String name, Long company_id) {
		Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("product_name", name));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		return (Product) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findAllProductsOfCompany(Long CompanyId) {

		List<Product> list = new ArrayList<Product>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT product from Product product WHERE product.company.company_id =:company_id and product.product_approval=:product_approval and product.status=:status and product.flag=:flag");
		query.setParameter("company_id", CompanyId);
		query.setParameter("product_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		while (scrollableResults.next()) {
		list.add((Product)scrollableResults.get()[0]);
		session.evict(scrollableResults.get()[0]);
		}
		session.clear();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findAllListingProductsOfCompany(Long CompanyId, Boolean flag) {
		
		
		List<Product> list =  new ArrayList<Product>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT pro from Product pro LEFT JOIN FETCH pro.company company "
					+"WHERE pro.company.company_id =:company_id and pro.flag =:flag order by pro.product_id desc");
			query.setParameter("company_id", CompanyId);
			query.setParameter("flag", flag);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Product)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
              session.clear();
			 
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		 
		 return list;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		
		Query query = session.createQuery("delete from Product where product_id =:id");
		query.setParameter("id", entityId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "Product Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete Product";
		}
		Query querystock = session.createSQLQuery("delete from stock where product_id=:id");
		querystock.setParameter("id", entityId);
		try {
			querystock.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public Product findOneView(Long productId) {
		Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("product_id", productId));
		criteria.setFetchMode("company", FetchMode.JOIN);
		return (Product) criteria.uniqueResult();
	}

	@Override
	@Transactional
	public Integer checktype(Long company_id, long product_id) {
		Session session = getCurrentSession();
		Integer flag = 0;
		Query subquery = session.createQuery("select type FROM Product WHERE product_id = :product_id and company.company_id = :company_id");
		subquery.setParameter("product_id", product_id);
		subquery.setParameter("company_id", company_id);

		if (subquery.list() == null || subquery.list().isEmpty()) {
			return flag;
		} 
		else {
			flag = (Integer) subquery.list().get(0);
			return flag;
		}
	}

	@Override
	public Boolean approvedByBatch(List<String> productList, Boolean primaryApproval) {
		Session session = getCurrentSession();
		if (productList.isEmpty()) {
			return false;
		} else {
			if (primaryApproval == true) {
				for (String id : productList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery("update Product set product_approval=:product_approval where product_id =:pid");
					query.setParameter("product_approval", 2);
					query.setParameter("pid", pid);
					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			} else if (primaryApproval == false) {
				for (String id : productList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery("update Product set product_approval=:product_approval where product_id =:pid");
					query.setParameter("product_approval", 3);
					query.setParameter("pid", pid);
					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			}
			return true;
		}
	}

	@Override
	public Boolean rejectByBatch(List<String> productList) {
		Session session = getCurrentSession();
		if (productList.isEmpty()) {
			return false;
		} else {
			for (String id : productList) {
				Long pid = Long.parseLong(id);
				Query query = session.createQuery("update Product set product_approval=:product_approval where product_id =:pid");
				query.setParameter("product_approval", 1);
				query.setParameter("pid", pid);
				try {
					query.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return true;
		}
	}

	@Override
	public int isExistsProduct(String product_name, Long company_id) {
		Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("product_name", product_name));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		if (criteria.list() == null || criteria.list().isEmpty())
			return 0;
		else
			return 1;
	}

	@Override
	public List<Product> findByStatus(int status, List<Long> companyIds) {
		/*Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("product_approval", status));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.addOrder(Order.desc("product_id"));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<Product> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT pro from Product pro WHERE pro.product_approval=:product_approval and pro.flag=:flag and pro.status=:status and pro.company.company_id in (:company_id) order by pro.product_id desc");
		query.setParameterList("company_id", companyIds);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("product_approval", status);
	ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
	
	  while (scrollableResults.next()) {
		  list.add((Product)scrollableResults.get()[0]);
		  session.evict(scrollableResults.get()[0]);
         }
	  session.clear();
	  return list;
	}

	@Override
	public List<Product> findAllListing(Boolean flag, List<Long> companyIds) {
		
		List<Product> list =  new ArrayList<Product>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT pro from Product pro LEFT JOIN FETCH pro.company company "
					+"WHERE pro.company.company_id in (:company_id) and pro.flag =:flag order by pro.product_id desc");
			query.setParameterList("company_id", companyIds);
			query.setParameter("flag", flag);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Product)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
              session.clear();
			 
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		 
		 return list;
		
	}

	@Override
	public List<Product> findAllProductsOnlyOfCompany(Long CompanyId) {	
		List<Product> list =  new ArrayList<Product>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT product from Product product WHERE product.company.company_id =:company_id and product_approval=:product_approval and status=:status and flag=:flag and type=:type and hsn_san_no IS NOT NULL");
		query.setParameter("company_id", CompanyId);
		query.setParameter("product_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("type", 1);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Product)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  /*Collections.sort(list,new comparator);*/
		  return list;
	}

	@Override
	public Integer findAllProductsOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("product_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
		}

	@Override
	public Integer findByStatusDashboard(int Approvalstatus, List<Long> companyIds) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("product_approval", Approvalstatus));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public Integer findAllDashboard() {
		List <Integer> list = new ArrayList<Integer>();
		list.add(MyAbstractController.APPROVAL_STATUS_PENDING);
		list.add(MyAbstractController.APPROVAL_STATUS_PRIMARY);
		
		Criteria criteria = getCurrentSession().createCriteria(Product.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.in("product_approval",list));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

}