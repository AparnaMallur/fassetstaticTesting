/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.ICityDAO;
import com.fasset.dao.interfaces.ICompanyStatutoryTypeDAO;
import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.dao.interfaces.ICustomerDAO;
import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.dao.interfaces.IindustryTypeDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.Product;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.SubNature;


/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Transactional
@Repository
public class CustomerDAOImpl extends AbstractHibernateDao<Customer> implements ICustomerDAO {

	@Autowired
	private ICountryDAO countryDao;

	@Autowired
	private IStateDAO stateDAO;

	@Autowired
	private ICityDAO cityDao;
	
	@Autowired
	private ICompanyStatutoryTypeDAO typedao;

	@Autowired
	private IindustryTypeDAO industryTypeDAO;

	@Transactional
	@Override
	public Customer findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		criteria.add(Restrictions.eq("customer_id", id));
		criteria.setFetchMode("company", FetchMode.JOIN);
		return (Customer) criteria.uniqueResult();
	}

	@Override
	public void create(Customer entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDateTime());
		try {
			entity.setCompStatType(typedao.findOne(entity.getCompany_statutory_id()));
			entity.setCountry(countryDao.findOne(entity.getCountry_id()));
			entity.setState(stateDAO.findOne(entity.getState_id()));
			entity.setIndustryType(industryTypeDAO.findOne(entity.getIndustry_id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		session.save(entity);
		session.flush();
		session.clear();
	}

	@Override
	public Long createcust(Customer entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDateTime());
		try {
			entity.setCompStatType(typedao.findOne(entity.getCompany_statutory_id()));
			entity.setCountry(countryDao.findOne(entity.getCountry_id()));
			entity.setState(stateDAO.findOne(entity.getState_id()));
			entity.setCity(cityDao.findOne(entity.getCity_id()));
			entity.setIndustryType(industryTypeDAO.findOne(entity.getIndustry_id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		Long id=(Long) session.save(entity);
		session.flush();
	    session.clear();
	    return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Customer where status=:status and flag=:flag and customer_approval=:customer_approval order by customer_id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("customer_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		return query.list();
	}

	@Override
	public void update(Customer sup) throws MyWebException {
		Session session = getCurrentSession();
		List<SubNature> subPurposeList = sup.getSubPurposeList();

		Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		criteria.add(Restrictions.eq("customer_id", sup.getCustomer_id()));
		Customer newEntity = (Customer) criteria.uniqueResult();
			newEntity.setCustomer_approval(sup.getCustomer_approval());
		newEntity.setUpdate_date(new LocalDateTime());
		newEntity.setCreated_date(sup.getCreated_date());
		newEntity.setCompStatType(sup.getCompStatType());
		newEntity.setIndustryType(sup.getIndustryType());
		newEntity.setCity(sup.getCity());
		newEntity.setCountry(sup.getCountry());
		newEntity.setState(sup.getState());
		newEntity.setContact_name(sup.getContact_name());
		newEntity.setMobile(sup.getMobile());
		newEntity.setLandline_no(sup.getLandline_no());
		newEntity.setEmail_id(sup.getEmail_id());
		newEntity.setOwner_pan_no(sup.getOwner_pan_no());
		newEntity.setAdhaar_no(sup.getAdhaar_no());
		newEntity.setCurrent_address(sup.getCurrent_address());
		newEntity.setPermenant_address(sup.getPermenant_address());
		newEntity.setFirm_name(sup.getFirm_name());
		newEntity.setPincode(sup.getPincode());
		newEntity.setGst_applicable(sup.getGst_applicable());
		newEntity.setGst_no(sup.getGst_no());
		newEntity.setOther_tax_no(sup.getOther_tax_no());
		newEntity.setCompany_pan_no(sup.getCompany_pan_no());
		newEntity.setTan_no(sup.getTan_no());
		newEntity.setTds_applicable(sup.getTds_applicable());
		newEntity.setTds_rate(sup.getTds_rate());
		newEntity.setCredit_opening_balance(sup.getCredit_opening_balance());
		newEntity.setDebit_opening_balance(sup.getDebit_opening_balance());
		newEntity.setFrom_mobile(sup.getFrom_mobile());
		newEntity.setStatus(sup.getStatus());
		newEntity.setCreated_by(sup.getCreated_by());
		newEntity.setUpdated_by(sup.getUpdated_by());
		newEntity.setIp_address(sup.getIp_address());
		newEntity.setFlag(sup.getFlag());
		newEntity.setDeductee(sup.getDeductee());
		newEntity.setTdstype(sup.getTdstype());
		if (sup.getCompany_id() != null && sup.getCompany_id() > 0) {
			newEntity.setCompany(sup.getCompany());
		}

		Set<SubLedger> subLedgers = newEntity.getSubLedgers();
		Set<Product> products = newEntity.getProduct();

		if (sup.getSubLedgers() != null && sup.getSubLedgers().size() != 0) {
			Set<SubLedger> subLedgerList = sup.getSubLedgers();
			for (SubLedger subLedger : subLedgerList) {
				subLedgers.add(subLedger);
			}
		}

		if (sup.getProduct() != null && sup.getProduct().size() != 0) {
			Set<Product> productList = sup.getProduct();
			for (Product product : productList) {
				products.add(product);
			}
		}
		newEntity.setSubLedgers(subLedgers);
		newEntity.setProduct(products);
		session.update(newEntity);
		session.flush();
	    session.clear();

		if (subPurposeList != null && subPurposeList.size() != 0) {
			for (int i = 0; i < subPurposeList.size(); i++) {
				SubNature subNature = new SubNature();
				subNature = subPurposeList.get(i);
				String str = subNature.getPurpose();
				if (str != "" && str != null) {
					Query query = session.createQuery("update CustomerSubledgerEntityClass set nature_of_purpose=:purpose where customer_id =:cid and sub_ledger_Id=:suid");
					query.setParameter("purpose", subNature.getPurpose());
					query.setParameter("cid", sup.getCustomer_id());
					query.setParameter("suid", Long.parseLong(subNature.getSubId()));
					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();

					}
				}
			}

		}

	}

	@Override
	public Long saveCustomersDao(Customer sup) {

		Session session = getCurrentSession();
		
		
		List<SubNature> subPurposeList = sup.getSubPurposeList();
		Long id = (Long) session.save(sup);
		session.flush();
		session.clear();
		for (int i = 0; i < subPurposeList.size(); i++) {
			SubNature subNature = new SubNature();
			subNature = subPurposeList.get(i);
			String str = subNature.getPurpose();
			if (str != "" && str != null) {
				Query query = session.createQuery("update CustomerSubledgerEntityClass set nature_of_purpose=:purpose where customer_id =:cid and sub_ledger_Id=:suid");
				query.setParameter("purpose", subNature.getPurpose());
				query.setParameter("cid", id);
				query.setParameter("suid", Long.parseLong(subNature.getSubId()));
				try {
					query.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		}
		return id;

	}

	@Override
	public String deleteCustomerSubLedger(Long cId, Long subId) {

		Session session = getCurrentSession();
		Query query = session.createQuery("delete from CustomerSubledgerEntityClass where customer_id =:cId and sub_ledger_Id=:subId");
		query.setParameter("cId", cId);
		query.setParameter("subId", subId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "SubLedger Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete SubLedger";

		}
		return msg;

	}

	@Override
	public String deleteCustomerProduct(Long cId, Long pId) {

		Session session = getCurrentSession();
		Query query = session.createQuery("delete from CustomerProductEntityClass where customer_id =:cId and product_id=:pId");
		query.setParameter("cId", cId);
		query.setParameter("pId", pId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "Product Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete Product";

		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> findByStatus(int status) {
		/*Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		criteria.add(Restrictions.eq("customer_approval", status));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.addOrder(Order.desc("customer_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		return criteria.list();*/
		List<Customer> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT cus from Customer cus WHERE cus.customer_approval=:customer_approval and cus.flag=:flag and cus.status=:status order by cus.customer_id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("customer_approval", status);
	ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
	
	  while (scrollableResults.next()) {
		  list.add((Customer)scrollableResults.get()[0]);
		  session.evict(scrollableResults.get()[0]);
         }
	  session.clear();
	  return list;
	}

	@Override
	public String updateApprovalStatus(Long customerId, int status) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update Customer set customer_approval =:status where customer_id =:customerId");
		query.setParameter("customerId", customerId);
		query.setParameter("status", status);
		String msg = "";
		try {
			query.executeUpdate();
			if (status == 1) {
				msg = "Rejected successfully";
			} else {
				if (status == 2) {
					msg = "Customer Primary Approval Done, Sent For Secondary Approval ";
				} else {
					msg = "Customer Secondary Approval Done";

				}
			}
		} catch (Exception e) {
			msg = "You can't change status";
		}
		return msg;
	}

	@Override
	public Customer isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override 
	public List<Customer> findAllCustomersOfCompany(Long CompanyId) {	
		List<Customer> list =  new ArrayList<Customer>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT customer from Customer customer LEFT JOIN FETCH customer.product product "
				+"LEFT JOIN FETCH customer.subLedgers subLedgers "+"LEFT JOIN FETCH customer.state state "
				+ "WHERE customer.company.company_id =:company_id and customer.customer_approval=:customer_approval and customer.flag=:flag and customer.status=:status");
		query.setParameter("company_id", CompanyId);
		query.setParameter("customer_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("flag", true);
		query.setParameter("status", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
		   list.add((Customer)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  
		  Collections.sort(list, 
				  new Comparator<Customer>() {
	            public int compare(Customer cus1, Customer cus2) {
	                String firm_name1 = cus1.getFirm_name().trim();
	                String firm_name2 = cus2.getFirm_name().trim();
	                return firm_name1.compareTo(firm_name2);
	            }
	        });
		  
		  return list;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> findAllCustomersOnlyOFCompany(Long CompanyId) {
		/*Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Customer.class);
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		criteria.add(Restrictions.eq("customer_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.addOrder(Order.asc("contact_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<Customer> list =  new ArrayList<Customer>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT customer from Customer customer WHERE customer.company.company_id =:company_id and customer_approval=:customer_approval and status=:status and flag=:flag");
		query.setParameter("company_id", CompanyId);
		query.setParameter("customer_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   session.evict(scrollableResults.get()[0]);
		   list.add((Customer)scrollableResults.get()[0]);
	         }
		  /*Collections.sort(list,new comparator);*/
		  return list;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		
		Session session = getCurrentSession();
		Query query1 = session.createQuery("delete from CustomerProductEntityClass where customer_id =:cId");
		query1.setParameter("cId", entityId);
		try {
			query1.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Query query2 = session.createQuery("delete from CustomerSubledgerEntityClass where customer_id =:cId");
		query2.setParameter("cId", entityId);
		try {
			query2.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Query queryop = session.createQuery("UPDATE Customer SET opening_id=null where customer_id =:cId");
		queryop.setParameter("cId", entityId);
		try {
			queryop.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Query query3 = session.createQuery("delete from OpeningBalances where customer_id =:cId");
		query3.setParameter("cId", entityId);
		try {
			query3.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Query query = session.createQuery("delete from Customer where customer_id =:id");
		query.setParameter("id", entityId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "Customer Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete Customer";

		}
		return msg;
	}

	@Override
	public List<Customer> findAllCustomerListing(Long flag) {

		/*Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		if (flag == 1) {
			criteria.add(Restrictions.eq("flag", true));
		} else {
			criteria.add(Restrictions.eq("flag", false));
		}
		criteria.addOrder(Order.desc("customer_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("industryType", FetchMode.JOIN);
		criteria.setFetchMode("city", FetchMode.JOIN);
		return criteria.list();*/
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select m.customer_id, c.company_name,m.contact_name,m.firm_name,m.mobile,m.status,m.customer_approval from customer_master m,company c where  m.company_id=c.company_id and m.flag=:flag order by m.customer_id desc");
		if (flag == 1) {
			query.setParameter("flag", true);
		} else {
			query.setParameter("flag", false);
		}
		return query.list();
	}

	@Override
	public List<Customer> findAllCustomerListingOfCompany(Long CompanyId, Long flag) {	
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select m.customer_id, c.company_name,m.contact_name,m.firm_name,m.mobile,m.status,m.customer_approval from customer_master m,company c where  m.company_id=c.company_id and m.flag=:flag and m.company_id =:companyId order by m.customer_id desc");
		query.setParameter("companyId",CompanyId);
		query.setParameter("flag", flag);
		return query.list();
		
	/*	List<Customer> list =  new ArrayList<Customer>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT customer from Customer customer LEFT JOIN FETCH customer.company company "
				+"LEFT JOIN FETCH customer.industryType industryType " + "LEFT JOIN FETCH customer.city city "
				+ "where customer.company.company_id =:company_id and customer.flag=:flag");
		query.setParameter("company_id", CompanyId);
		if (flag == 1) {
			query.setParameter("flag", true);
		} else {
			query.setParameter("flag", false);
		}
		//query.setParameter("customer_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		//query.setParameter("status", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		 int count = 0;
		  while (scrollableResults.next()) {
		   if (++count > 0 && count % 100 == 0) {
		    System.out.println("Fetched " + count + " entities");
		   }
		   session.evict(scrollableResults.get()[0]);
		   list.add((Customer)scrollableResults.get()[0]);
	         }
		  return list;*/
	}
	@Transactional
	@Override
	public void updateCustomer(Customer customer) {
		Session session = getCurrentSession();
		session.merge(customer);
		session.flush();
		session.clear();
	}

	@Override
	public Customer findOneWithAll(Long cusId) {
		Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		criteria.add(Restrictions.eq("customer_id", cusId));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("compStatType", FetchMode.JOIN);
		criteria.setFetchMode("industryType", FetchMode.JOIN);
		criteria.setFetchMode("deductee", FetchMode.JOIN);
		criteria.setFetchMode("city", FetchMode.JOIN);
		criteria.setFetchMode("country", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subLedgers", FetchMode.JOIN);
		criteria.setFetchMode("product", FetchMode.JOIN);
		criteria.setFetchMode("ledger", FetchMode.JOIN);
		return (Customer) criteria.uniqueResult();
	}

	@Override
	public Boolean approvedByBatch(List<String> customerList, Boolean primaryApproval) {

		Session session = getCurrentSession();

		if (customerList.isEmpty()) {
			return false;
		} else {
			if (primaryApproval == true) {
				for (String id : customerList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery("update Customer set customer_approval=:customer_approval where customer_id =:customer_id");
					query.setParameter("customer_approval", 2);
					query.setParameter("customer_id", pid);
					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();

					}
				}
				return true;
			} else if (primaryApproval == false) {
				for (String id : customerList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery("update Customer set customer_approval=:customer_approval where customer_id =:customer_id");
					query.setParameter("customer_approval", 3);
					query.setParameter("customer_id", pid);
					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();

					}
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public Boolean rejectByBatch(List<String> customerList) {

		Session session = getCurrentSession();
		if (customerList.isEmpty()) {
			return false;
		} else {
			for (String id : customerList) {
				Long pid = Long.parseLong(id);
				Query query = session.createQuery("update Customer set customer_approval=:customer_approval where customer_id =:customer_id");
				query.setParameter("customer_approval", 1);
				query.setParameter("customer_id", pid);
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
	public int isExistsPan(String companypan, String companyname, Long company_id, String email) {
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"from Customer where company.company_id=:company_id and company_pan_no=:company_pan_no");
		query.setParameter("company_id", company_id);
		query.setParameter("company_pan_no", companypan);
		query.list();
		if (query.list() == null || query.list().isEmpty())
			return 0;
		else
			return 1;
	}

	@Override
	public Customer isExistsCustomer(String companypan, String firm_name, long company_id, String contact_name) {
		Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		System.out.println("Firm Name "+ firm_name);
		System.out.println("company_id "+ company_id);
		System.out.println("contact_name "+ contact_name);
		criteria.add(Restrictions.eq("firm_name", firm_name));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		criteria.add(Restrictions.eq("contact_name", contact_name));
		return (Customer) criteria.uniqueResult();
	}

	@Override
	public List<Customer> findByStatus(int status, List<Long> companyIds) {
		/*Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		criteria.add(Restrictions.eq("customer_approval", status));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.addOrder(Order.desc("customer_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		return criteria.list();*/
		
		List<Customer> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT cus from Customer cus WHERE cus.customer_approval=:customer_approval and cus.flag=:flag and cus.status=:status and cus.company.company_id in (:company_id) order by cus.customer_id desc");
		query.setParameterList("company_id", companyIds);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("customer_approval", status);
	ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
	
	  while (scrollableResults.next()) {
		  list.add((Customer)scrollableResults.get()[0]);
		  session.evict(scrollableResults.get()[0]);
         }
	  session.clear();
	  return list;
	  
	}

	@Override
	public List<Customer> findAllCustomerListing(Boolean flag, List<Long> companyIds) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select m.customer_id, c.company_name,m.contact_name,m.firm_name,m.mobile,m.status,m.customer_approval from customer_master m,company c where  m.company_id=c.company_id and m.flag=:flag and m.company_id in(:companyId) order by m.customer_id desc");
		query.setParameterList("companyId",companyIds);
		query.setParameter("flag", flag);
		return query.list();
		/*
		Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		criteria.add(Restrictions.eq("flag", flag));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.addOrder(Order.desc("customer_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("industryType", FetchMode.JOIN);
		criteria.setFetchMode("city", FetchMode.JOIN);
		return criteria.list();*/
	}

	@Override
	public List<Customer> findAllOPbalanceofcustomer(long company_id, long flag) {
		// TODO Auto-generated method stub		
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select sum( `credit_balance` ) , sum( `debit_balance` ) , `customer_id`  from opening_balances where company_id=:company_id and balanceType=:balanceType group by customer_id");
		query.setParameter("company_id", company_id);
		query.setParameter("balanceType", 5);
		return query.list();
	}

	@Override
	public List<Customer> findAllCustomersOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createQuery("from Customer where company.company_id=:company_id and status=:status and flag=:flag and customer_approval=:customer_approval order by customer_id desc");
		query.setParameter("status", true);
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("customer_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setMaxResults(10);
		return query.list();
		
	}

	@Override
	public List<Customer> findByStatusDashboard(int approvalStatusPending, List<Long> companyIds) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Customer where company.company_id in(:company_id) and status=:status and flag=:flag and customer_approval=:customer_approval order by customer_id desc");
		query.setParameter("status", true);
		query.setParameterList("company_id", companyIds);
		query.setParameter("flag", true);
		query.setParameter("customer_approval", approvalStatusPending);
		query.setMaxResults(10);
		return query.list();
	}

	@Override
	public List<Customer> findAllDashboard() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Customer where status=:status and flag=:flag and customer_approval=:customer_approval order by customer_id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("customer_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setMaxResults(10);
		return query.list();
	}

	@Override
	public Integer findAllCustomerCountOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("customer_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public Integer findByStatusCustomerCountDashboard(int approvalStatus, List<Long> companyIds) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		criteria.add(Restrictions.eq("customer_approval", approvalStatus));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public Integer findAllCustomerCountDashboard() {
		
		List <Integer> list = new ArrayList<Integer>();
		list.add(MyAbstractController.APPROVAL_STATUS_PENDING);
		list.add(MyAbstractController.APPROVAL_STATUS_PRIMARY);
		
		Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.in("customer_approval", list));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public Customer findGstNoForCustomerByCompany(long companyid, long custid) {

		Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		         criteria.add(Restrictions.eq("company.company_id", companyid));
		         criteria.add(Restrictions.eq("customer_id", custid));
		
		return (Customer) criteria.uniqueResult();
	}

	@Override
	public Integer updateTaxRate(long deducteeId,Float rate) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		if (deducteeId != 0 ) {
			Query query = session.createQuery("update Customer set tds_rate=:rate where deductee_id =:cid ");
			
			query.setParameter("cid", deducteeId);
			query.setParameter("rate", rate);
			try {
				query.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();

			}
			return 1;
		}else{
			return 0;
		}
		
	}

	@Override
	public List<Customer> findAllCustomersOfCompanyInclPending(Long CompanyId) {
		List<Customer> list =  new ArrayList<Customer>();
		Session session = getCurrentSession();
	//	LocalDate tomorrow = LocalDate.now().plusDays(1);
		LocalDateTime dt1=LocalDateTime.now().plusDays(-8);
		
		LocalDate dt = LocalDate.now().plusDays(-8);
		System.out.println("time  is " +LocalDate.now());
		System.out.println("dt1 is " +dt1);
		Query query = session.createQuery("SELECT customer from Customer customer LEFT JOIN FETCH customer.product product "
				+"LEFT JOIN FETCH customer.subLedgers subLedgers "+"LEFT JOIN FETCH customer.state state "
				+ "WHERE customer.company.company_id =:company_id and (customer.customer_approval=:customer_approval  or (  "
              +"customer.created_date >=:dt and customer.customer_approval=:customer_pending)) "
				
				+ "and customer.flag=:flag and customer.status=:status");
		System.out.println("query is " +dt);
		query.setParameter("company_id", CompanyId);
		query.setParameter("customer_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("customer_pending", MyAbstractController.APPROVAL_STATUS_PENDING);
		query.setParameter("dt", dt1);
		query.setParameter("flag", true);
		query.setParameter("status", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
		   list.add((Customer)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  
		  Collections.sort(list, 
				  new Comparator<Customer>() {
	            public int compare(Customer cus1, Customer cus2) {
	                String firm_name1 = cus1.getFirm_name().trim();
	                String firm_name2 = cus2.getFirm_name().trim();
	                return firm_name1.compareTo(firm_name2);
	            }
	        });
		  
		  return list;
	}

	/*@Override
	public Company getCompanyForGstCheck(Long companyId) {
		
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
				 criteria.add(Restrictions.eq("company_id", companyId));
		
		return (Company) criteria.uniqueResult();
	}*/

	

	
	
}