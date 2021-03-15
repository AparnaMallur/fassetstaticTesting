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
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.ICityDAO;
import com.fasset.dao.interfaces.ICompanyStatutoryTypeDAO;
import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.dao.interfaces.ISupplierDAO;
import com.fasset.dao.interfaces.IindustryTypeDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Customer;
import com.fasset.entities.Product;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.SubNature;

@Transactional
@Repository
public class SupplierDAOImpl extends AbstractHibernateDao<Suppliers> implements ISupplierDAO {

	@Autowired
	private ICityDAO cityDAO;

	@Autowired
	private ICountryDAO countryDao;

	@Autowired
	private IStateDAO stateDAO;

	@Autowired
	private ICompanyStatutoryTypeDAO typedao;

	@Autowired
	private IindustryTypeDAO industryTypeDAO;

	@Transactional
	@Override
	public Suppliers findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("supplier_id", id));
		criteria.setFetchMode("company", FetchMode.JOIN);
		return (Suppliers) criteria.uniqueResult();
	}

	@Override
	public Suppliers findOne(Criteria crt) throws MyWebException {
		return null;
	}

	@Override
	public Suppliers findOne(String id) throws MyWebException {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Suppliers> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Suppliers where status=:status and flag=:flag and supplier_approval=:supplier_approval order by supplier_id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("supplier_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		return query.list();
	}

	@Override
	public void create(Suppliers entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDateTime());
		try {
			entity.setCompStatType(typedao.findOne(entity.getCompany_statutory_id()));
			entity.setCountry(countryDao.findOne(entity.getCountry_id()));
			entity.setState(stateDAO.findOne(entity.getState_id()));
			entity.setCity(cityDAO.findOne(entity.getCity_id()));
			entity.setIndustryType(industryTypeDAO.findOne(entity.getIndustry_id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		session.save(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public Long createsup(Suppliers entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDateTime());
		try {
			entity.setCompStatType(typedao.findOne(entity.getCompany_statutory_id()));
			entity.setCountry(countryDao.findOne(entity.getCountry_id()));
			entity.setState(stateDAO.findOne(entity.getState_id()));
			entity.setCity(cityDAO.findOne(entity.getCity_id()));
			entity.setIndustryType(industryTypeDAO.findOne(entity.getIndustry_id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		Long id= (Long) session.save(entity);
		session.flush();
	    session.clear();
	    return id;
	}

	@Override
	public void update(Suppliers sup) throws MyWebException {

		Session session = getCurrentSession();
		List<SubNature> subPurposeList = sup.getSubPurposeList();

		Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("supplier_id", sup.getSupplier_id()));
		Suppliers newEntity = (Suppliers) criteria.uniqueResult();
		//if ((newEntity.getSupplier_approval() == 1) || (newEntity.getSupplier_approval() == 3))
			newEntity.setSupplier_approval(sup.getSupplier_approval());
		newEntity.setUpdate_date(new LocalDateTime());
		newEntity.setCompStatType(sup.getCompStatType());
		newEntity.setIndustryType(sup.getIndustryType());
		newEntity.setCity(sup.getCity());
		newEntity.setCountry(sup.getCountry());
		newEntity.setState(sup.getState());
		newEntity.setDeductee(sup.getDeductee());
		newEntity.setContact_name(sup.getContact_name());
		newEntity.setMobile(sup.getMobile());
		newEntity.setLandline_no(sup.getLandline_no());
		newEntity.setEmail_id(sup.getEmail_id());
		newEntity.setOwner_pan_no(sup.getOwner_pan_no());
		newEntity.setAdhaar_no(sup.getAdhaar_no());
		newEntity.setCurrent_address(sup.getCurrent_address());
		newEntity.setPermenant_address(sup.getPermenant_address());
		newEntity.setCompany_name(sup.getCompany_name());
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
		newEntity.setReverse_mecha(sup.getReverse_mecha());
		newEntity.setSuppiler_category(sup.getSuppiler_category());
		newEntity.setFrom_mobile(sup.getFrom_mobile());
		newEntity.setStatus(sup.getStatus());
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
		session.merge(newEntity);
		session.flush();
	    session.clear();
		if (subPurposeList != null && subPurposeList.size() != 0) {
			for (int i = 0; i < subPurposeList.size(); i++) {
				SubNature subNature = new SubNature();
				subNature = subPurposeList.get(i);
				String str = subNature.getPurpose();
				if (str != "" && str != null) {
					Query query = session.createQuery("update SupplierSubledgerEntityClass set nature_of_purpose=:purpose where supplier_id =:sid and sub_ledger_Id=:suid");
					query.setParameter("purpose", subNature.getPurpose());
					query.setParameter("sid", sup.getSupplier_id());
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
	public void merge(Suppliers entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Suppliers entity) throws MyWebException {
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
	public Long saveSuppliersdao(Suppliers sup) {
		Session session = getCurrentSession();
		List<SubNature> subPurposeList = sup.getSubPurposeList();
		Long id = (Long) session.save(sup);

		for (int i = 0; i < subPurposeList.size(); i++) {
			SubNature subNature = new SubNature();
			subNature = subPurposeList.get(i);
			String str = subNature.getPurpose();
			if (str != "" && str != null) {
				Query query = session.createQuery("update SupplierSubledgerEntityClass set nature_of_purpose=:purpose where supplier_id =:sid and sub_ledger_Id=:suid");
				query.setParameter("purpose", subNature.getPurpose());
				query.setParameter("sid", id);
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
	public String deleteSupplierSubLedger(Long suId, Long subId) {

		Session session = getCurrentSession();
		Query query = session.createQuery("delete from SupplierSubledgerEntityClass where supplier_id =:suId and sub_ledger_Id=:subId");
		query.setParameter("suId", suId);
		query.setParameter("subId", subId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "SubLedger Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete SubLedger Avinash supplier";

		}
		return msg;
	}

	@Override
	public String deleteSupplierProduct(Long suId, Long pId) {

		Session session = getCurrentSession();
		Query query = session.createQuery("delete from SupplierProductEntityClass where supplier_id =:suId and product_id=:pId");
		query.setParameter("suId", suId);
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
	public List<Suppliers> findByStatus(int status) {
		/*Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("supplier_approval", status));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.addOrder(Order.desc("supplier_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		return criteria.list();*/
		List<Suppliers> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT sup from Suppliers sup WHERE sup.supplier_approval=:supplier_approval and sup.flag=:flag and sup.status=:status order by sup.supplier_id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("supplier_approval", status);
	ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
	
	  while (scrollableResults.next()) {
		  list.add((Suppliers)scrollableResults.get()[0]);
		  session.evict(scrollableResults.get()[0]);
         }
	  session.clear();
	  return list;
	}

	@Override
	public String updateApprovalStatus(Long supplierId, int status) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update Suppliers set supplier_approval =:status where supplier_id =:supplierId");
		query.setParameter("supplierId", supplierId);
		query.setParameter("status", status);
		String msg = "";
		try {
			query.executeUpdate();
			if (status == 1) {
				msg = "Rejected successfully";
			} else {
				if (status == 2) {
					msg = "Supplier Primary Approval Done, Sent For Secondary Approval ";
				} else {
					msg = "Supplier Secondary Approval Done";

				}
			}
		} catch (Exception e) {
			msg = "You can't change status";
		}
		return msg;
	}

	@Override
	public Suppliers isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Suppliers> findAllSuppliersOfCompany(Long CompanyId) {

		/*Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.asc("contact_name"));
		criteria.add(Restrictions.eq("supplier_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("subLedgers", FetchMode.JOIN);
		criteria.setFetchMode("product", FetchMode.JOIN);
		return criteria.list();*/
		
		List<Suppliers> list =  new ArrayList<Suppliers>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT supplier from Suppliers supplier LEFT JOIN FETCH supplier.product product "
				+"LEFT JOIN FETCH supplier.subLedgers subLedgers "+"LEFT JOIN FETCH supplier.state state "
				+ "WHERE supplier.company.company_id =:company_id and supplier_approval=:supplier_approval and supplier.flag=:flag and supplier.status=:status");
		query.setParameter("company_id", CompanyId);
		query.setParameter("supplier_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("flag", true);
		query.setParameter("status", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
		   list.add((Suppliers)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  Collections.sort(list, new Comparator<Suppliers>() {
	            public int compare(Suppliers sup1, Suppliers sup2) {
	                String company_name1 = sup1.getCompany_name().trim();
	                String company_name2 = sup2.getCompany_name().trim();
	                return company_name1.compareTo(company_name2);
	            }
	        });
		  return list;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		
		Session session = getCurrentSession();
		Query query1 = session.createSQLQuery("delete from supplier_master_subledger_master where supplier_id =:suId");
		query1.setParameter("suId", entityId);
		try {
			query1.executeUpdate();
		} catch (Exception e) {

		}
		
		Query query2 = session.createSQLQuery("delete from supplier_master_product where supplier_id =:suId");
		query2.setParameter("suId", entityId);
		try {
			query2.executeUpdate();
		} catch (Exception e) {

		}
		Query queryop = session.createQuery("UPDATE Suppliers SET opening_id=null where supplier_id =:suId");
		queryop.setParameter("suId", entityId);
		try {
			queryop.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Query query3 = session.createQuery("delete from OpeningBalances where supplier_id =:suId");
		query3.setParameter("suId", entityId);
		try {
			query3.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Query query = session.createQuery("delete from Suppliers where supplier_id =:id");
		query.setParameter("id", entityId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "Supplier Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete Supplier";

		}
		return msg;
	}

	@Override
	public List<Suppliers> findAllSuppliersListing(Long flag) {
		Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		if (flag == 1) {
			criteria.add(Restrictions.eq("flag", true));
		} else {
			criteria.add(Restrictions.eq("flag", false));
		}
		criteria.addOrder(Order.desc("supplier_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("industryType", FetchMode.JOIN);
		criteria.setFetchMode("city", FetchMode.JOIN);
		return criteria.list();
	}

	@Override
	public List<Suppliers> findAllSuppliersListingOfCompany(Long CompanyId, Long flag) {
		Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		if (flag == 1) {
			criteria.add(Restrictions.eq("flag", true));
		} else {
			criteria.add(Restrictions.eq("flag", false));
		}
		criteria.addOrder(Order.desc("supplier_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("industryType", FetchMode.JOIN);
		criteria.setFetchMode("city", FetchMode.JOIN);
		return criteria.list();
	}

	@Transactional
	@Override
	public void updateSupplier(Suppliers suppliers) {
		Session session = getCurrentSession();
		session.merge(suppliers);
		session.flush();
	    session.clear();
	}

	@Override
	public Suppliers findOneWithAll(Long supId) {
		Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("supplier_id", supId));
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
		return (Suppliers) criteria.uniqueResult();
	}

	@Override
	public int isExistsPan(String companypan, String companyname, Long company_id, String email) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Suppliers where company.company_id=:company_id and company_pan_no=:company_pan_no");
		query.setParameter("company_id", company_id);
		query.setParameter("company_pan_no", companypan);
		query.list();
		if (query.list() == null || query.list().isEmpty())
			return 0;
		else
			return 1;
	}

	@Override
	public Boolean approvedByBatch(List<String> supplierList, Boolean primaryApproval) {
		Session session = getCurrentSession();
		if (supplierList.isEmpty()) {
			return false;
		} else {
			if (primaryApproval == true) {
				for (String id : supplierList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery(
							"update Suppliers set supplier_approval=:supplier_approval where supplier_id =:supplier_id");
					query.setParameter("supplier_approval", 2);
					query.setParameter("supplier_id", pid);
					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			} else if (primaryApproval == false) {
				for (String id : supplierList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery(
							"update Suppliers set supplier_approval=:supplier_approval where supplier_id =:supplier_id");
					query.setParameter("supplier_approval", 3);
					query.setParameter("supplier_id", pid);
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
	public Boolean rejectByBatch(List<String> supplierList) {
		Session session = getCurrentSession();
		if (supplierList.isEmpty()) {
			return false;
		} else {
			for (String id : supplierList) {
				Long pid = Long.parseLong(id);
				Query query = session.createQuery(
						"update Suppliers set supplier_approval=:supplier_approval where supplier_id =:supplier_id");
				query.setParameter("supplier_approval", 1);
				query.setParameter("supplier_id", pid);
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
	public Suppliers isExistsSupplier(String companypan, String company_name, long company_id, String supplier_name) {
		Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("company_name", company_name));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		criteria.add(Restrictions.eq("contact_name", supplier_name));
		return (Suppliers) criteria.uniqueResult();
	}

	@Override
	public List<Suppliers> findAllSuppliersOnlyOfCompany(Long CompanyId) {
		/*Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.asc("contact_name"));
		criteria.add(Restrictions.eq("supplier_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<Suppliers> list =  new ArrayList<Suppliers>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT supplier from Suppliers supplier WHERE supplier.company.company_id =:company_id and supplier_approval=:supplier_approval and status=:status and flag=:flag");
		query.setParameter("company_id", CompanyId);
		query.setParameter("supplier_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Suppliers)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  /*Collections.sort(list,new comparator);*/
		  return list;
	}

	@Override
	public List<Suppliers> findByStatus(int status, List<Long> companyIds) {
		/*Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("supplier_approval", status));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.addOrder(Order.desc("supplier_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		return criteria.list();*/
		List<Suppliers> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT sup from Suppliers sup WHERE sup.supplier_approval=:supplier_approval and sup.flag=:flag and sup.status=:status and sup.company.company_id in (:company_id) order by sup.supplier_id desc");
		query.setParameterList("company_id", companyIds);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("supplier_approval", status);
	ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
	
	  while (scrollableResults.next()) {
		  list.add((Suppliers)scrollableResults.get()[0]);
		  session.evict(scrollableResults.get()[0]);
         }
	  session.clear();
	  return list;
	  
	}

	@Override
	public List<Suppliers> findAllSuppliersListing(Boolean flag, List<Long> companyIds) {
		Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("flag", flag));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.addOrder(Order.desc("supplier_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("industryType", FetchMode.JOIN);
		criteria.setFetchMode("city", FetchMode.JOIN);
		return criteria.list();
	}

	@Override
	public List<Suppliers> findAllOPbalanceofsupplier(long company_id, long flag) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select sum( `credit_balance` ) , sum( `debit_balance` ) , `supplier_id`  from opening_balances where company_id=:company_id and balanceType=:balanceType group by supplier_id");
		query.setParameter("company_id", company_id);
		query.setParameter("balanceType", 4);
		System.out.println(query.list());
		return query.list();
	}

	@Override
	public List<Suppliers> findAllSuppliersOfCompanyDashboard(long companyId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Suppliers where company.company_id=:company_id and status=:status and flag=:flag and supplier_approval=:supplier_approval order by supplier_id desc");
		query.setParameter("status", true);
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("supplier_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setMaxResults(10);
		return query.list();
		
	}

	@Override
	public List<Suppliers> findByStatusDashboard(int approvalStatusPending, List<Long> companyIds) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Suppliers where company.company_id in(:company_id) and status=:status and flag=:flag and supplier_approval=:supplier_approval order by supplier_id desc");
		query.setParameter("status", true);
		query.setParameterList("company_id", companyIds);
		query.setParameter("flag", true);
		query.setParameter("supplier_approval", approvalStatusPending);
		query.setMaxResults(10);
		return query.list();
		
		/*Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("supplier_approval", approvalStatusPending));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.addOrder(Order.desc("supplier_id"));
		criteria.setFetchMode("company", FetchMode.JOIN);
		//criteria.setMaxResults(10);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
	}

	@Override
	public List<Suppliers> findAllDashboard() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Suppliers where status=:status and flag=:flag and supplier_approval=:supplier_approval order by supplier_id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("supplier_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setMaxResults(10);
		return query.list();
	}

	@Override
	public Integer findAllSuppliersCountOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("supplier_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public Integer findByStatusSuppliersCountDashboard(int approvalStatus, List<Long> companyIds) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("supplier_approval", approvalStatus));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public Integer findAllSuppliersCountDashboard() {
		
		List <Integer> list = new ArrayList<Integer>();
		list.add(MyAbstractController.APPROVAL_STATUS_PENDING);
		list.add(MyAbstractController.APPROVAL_STATUS_PRIMARY);
		
		Criteria criteria = getCurrentSession().createCriteria(Suppliers.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.in("supplier_approval", list));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public Integer updateTaxRate(long deducteeId, Float rate) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		if (deducteeId != 0 ) {
			Query query = session.createQuery("update Suppliers set tds_rate=:rate where deductee_id =:cid ");
			
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
}
