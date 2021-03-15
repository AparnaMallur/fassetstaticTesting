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
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IYearEndingDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.ActivityLog;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Company;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;


@Transactional
@Repository
public class CompanyDAOImpl extends AbstractHibernateDao<Company> implements ICompanyDAO{
	
	@Autowired
	private IYearEndingDAO dao ;
	
	@Transactional
	@Override
	public Company findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("company_id", id));
		return (Company) criteria.uniqueResult();
	}

	@Override
	public Company findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Company findOne(String id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("email_id", id));
		return (Company) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Company> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Company order by company_name ASC");
		return query.list();
	}

	@Override
	public void create(Company entity) throws MyWebException {
		
		
		
	}

	@Override
	public void update(Company entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);	
		session.flush();
		session.clear();
	}

	@Override
	public void merge(Company entity) throws MyWebException {
		// TODO Auto-generated method stub		
	}

	@Override
	public void delete(Company entity) throws MyWebException {
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
	public Long saveCompany(Company entity) {
		Session session = getCurrentSession();
		Long id = (Long) session.save(entity);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public Company isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Company> getCompanyByStatus(Integer status) {		
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("status", status));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("company_id"));
		criteria.setFetchMode("user", FetchMode.JOIN);
		criteria.setFetchMode("company_statutory_type", FetchMode.JOIN);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Company> getApprovedCompanies() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Company where status =:status order by company_name ASC");
		query.setParameter("status", MyAbstractController.STATUS_SUBSCRIBED_USER);	
		return query.list();
	}

	@Override
	public Company getCompanyWithAllUsers(Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("company_id", companyId));
		criteria.setFetchMode("user", FetchMode.JOIN);
		criteria.setFetchMode("company_statutory_type", FetchMode.JOIN);
		criteria.setFetchMode("industry_type", FetchMode.JOIN);
		criteria.setFetchMode("country", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("city", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		return (Company) criteria.uniqueResult();
	}

	@Override
	public Company getCompanyWithIndustrytype(Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("company_id", companyId));
		criteria.setFetchMode("industry_type", FetchMode.JOIN);
		criteria.setFetchMode("subLedgers", FetchMode.JOIN);
		criteria.setFetchMode("ledger", FetchMode.JOIN);
		return (Company) criteria.uniqueResult();
	}

	@Override
	public List<Company> getAllCompaniesWithLedgerlist() {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.setFetchMode("ledgerList", FetchMode.JOIN);
		criteria.setFetchMode("suppliersList", FetchMode.JOIN);
		criteria.setFetchMode("customerList", FetchMode.JOIN);
		criteria.setFetchMode("bankList", FetchMode.JOIN);
		criteria.addOrder(Order.asc("company_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Company findOneWithAll(Long compId) {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("company_id", compId));
		criteria.setFetchMode("company_statutory_type", FetchMode.JOIN);
		criteria.setFetchMode("industry_type", FetchMode.JOIN);
		criteria.setFetchMode("country", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("city", FetchMode.JOIN);
		criteria.setFetchMode("year_range", FetchMode.JOIN);
		criteria.setFetchMode("user", FetchMode.JOIN);
		return (Company) criteria.uniqueResult();
	}

	@Override
	public List<String> getVoucherRange(Long companyId, String VoucherRange) {
		String[] range=VoucherRange.split(",");
		List<String> rangeIds = new ArrayList<String>();
		for (String yId : range) {
			rangeIds.add(new String(yId));
		}
		return rangeIds;

	}

	@Override
	public List<Company> getAllCompaniesWithLedgerAndCustomerlist() {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.setFetchMode("ledgerList", FetchMode.JOIN);
		criteria.setFetchMode("customerList", FetchMode.JOIN);
		criteria.addOrder(Order.asc("company_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Company> getAllCompaniesWithLedgerAndSuppilerlist() {
		
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.setFetchMode("ledgerList", FetchMode.JOIN);
		criteria.setFetchMode("suppliersList", FetchMode.JOIN);
		criteria.addOrder(Order.asc("company_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Company> getAllCompaniesWithAccountGrouplist() {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.setFetchMode("accountGroup", FetchMode.JOIN);
		criteria.addOrder(Order.asc("company_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public String deleteByIdValue(long id) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from Company where company_id =:id");
		query.setParameter("id", id);
		String msg="";
		try{
			query.executeUpdate();
			msg= "Company Deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete Company";			
		}
		return msg;
	}

	@Override
	public int isExistscompanyname(String company_name) {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("company_name", company_name));
		if(criteria.list()==null || criteria.list().isEmpty())
			return 0;
		else
			return 1;
	}

	@Override
	public List<Company> getAllCompaniesWithProducts() {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.setFetchMode("product", FetchMode.JOIN);
		criteria.addOrder(Order.asc("company_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Company> getAllCompaniesOnly() {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("status", MyAbstractController.STATUS_SUBSCRIBED_USER));
		criteria.addOrder(Order.asc("company_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Company> getAllCompaniesWithLedgerlist(List<Long> companyIds) {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.in("company_id", companyIds));
		criteria.setFetchMode("ledgerList", FetchMode.JOIN);
		criteria.setFetchMode("suppliersList", FetchMode.JOIN);
		criteria.setFetchMode("customerList", FetchMode.JOIN);
		criteria.setFetchMode("bankList", FetchMode.JOIN);
		criteria.addOrder(Order.asc("company_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Company> getApprovedCompanies(List<Long> companyIds) {		
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("status", MyAbstractController.STATUS_SUBSCRIBED_USER));
		criteria.add(Restrictions.in("company_id", companyIds));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("company_name"));
		return criteria.list();
	}

	@Override
	public Company getCompanyWithCompanyStautarType(Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("company_id", companyId));
		criteria.setFetchMode("company_statutory_type", FetchMode.JOIN);
		return (Company) criteria.uniqueResult();
	}

	public int ismailsent(Long company_id, LocalDate date) {
		// TODO Auto-generated method stub
		Company com=new Company();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.add(Restrictions.eq("company_id", company_id));
		com=(Company) criteria.uniqueResult();
		if(com.getEmail_date()==null)
			return 0;
		else if(com.getEmail_date().equals(date))
			return 1;
		else
			return 0;
	}

	@Override
	public void updatemailsent(Long company_id, LocalDate date) {
		/*Session session = getCurrentSession();
		Query query = session.createQuery("update Company set email_date=:email_date,email_sent=:email_sent where company_id =:company_id");
		query.setParameter("company_id", company_id);
		query.setParameter("email_date", date);
		query.setParameter("email_sent", 1);
		query.executeUpdate();*/
		Session session = getCurrentSession();
		Criteria criteria1 = session.createCriteria(Company.class);
		criteria1.add(Restrictions.eq("company_id", company_id));
		Company company =(Company) criteria1.uniqueResult();
		company.setEmail_date(date);
		company.setEmail_sent(1);
		session.merge(company);
		session.flush();
		session.clear();
	}

	@Override
	public List<Company> FindAlllist() {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select company_id, company_name,permenant_address, email_id,mobile,status from company order by company_id desc");
		return query.list();
	}
	@Override
	public List<Company> FindAlllistexe(List<Long> companyIds) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select company_id, company_name,permenant_address, email_id,mobile,status from company where company_id in(:company_id) order by company_id desc");
		query.setParameterList("company_id", companyIds);
		return query.list();
	}

	@Override
	public Long createComapny(Company comapny) {
		Session session = getCurrentSession();
		long company_id=(long) session.save(comapny);
		session.flush();
		session.clear();
		return company_id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Company> FindAllInactiveCompanies(Long role, Long user_id) {
		
		List<Company> inactiveCompanyList = new ArrayList<>();
		if(role.equals(MyAbstractController.ROLE_SUPERUSER))
		{
			List<Company> companylist = new ArrayList<>();
			Session session = getCurrentSession();
			Query query = session.createQuery("SELECT company from Company company LEFT JOIN FETCH company.user user "
					+ "WHERE company.status=:status");
			query.setParameter("status", MyAbstractController.STATUS_SUBSCRIBED_USER);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			
			  while (scrollableResults.next()) {
				  companylist.add((Company)scrollableResults.get()[0]);
			     session.evict(scrollableResults.get()[0]);
		         }
			  session.clear();
			  Collections.sort(companylist, new Comparator<Company>() {
		            public int compare(Company com1, Company com2) {
		                String company_name1 = com1.getCompany_name().trim();
		                String company_name2 = com2.getCompany_name().trim();
		                return company_name1.compareTo(company_name2);
		            }
		        });
			 
		for(Company comp : companylist)
		{
			Integer inactiveCount=0;
			
			for(User user : comp.getUser())
			{
				Integer count = dao.findAllLogInLogBefore3DaysAgo(user.getUser_id());// If user is inactive from last 3 days then it will return count = 0
			
				inactiveCount=inactiveCount+count;
			}
			if(inactiveCount.equals(0))
			{
				inactiveCompanyList.add(comp);
			}
		}
		
		}
		else if(role.equals(MyAbstractController.ROLE_MANAGER))
		{
			
			List<ClientAllocationToKpoExecutive> clientAllcationlist = new ArrayList<>();
			
			List<User> userlist = new ArrayList<>();
			Session session = getCurrentSession();
			Query query = session.createQuery("SELECT user from User user WHERE user.manager_id.user_id=:user_id");
			query.setParameter("user_id", user_id);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			
			  while (scrollableResults.next()) {
				  userlist.add((User)scrollableResults.get()[0]);
			     session.evict(scrollableResults.get()[0]);
		         }
			  session.clear();
	
			List<Long>list = new ArrayList<>();
			for(User user : userlist)
			{
				list.add(user.getUser_id());
			}
			if(list.size()>0)
			{
				
				Session session1 = getCurrentSession();
				Query query1 = session1.createQuery("SELECT clientAllo from ClientAllocationToKpoExecutive clientAllo WHERE clientAllo.user.user_id in (:user_id)");
				query1.setParameterList("user_id", list);
				ScrollableResults scrollableResults1 = query1.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults1.next()) {
					  clientAllcationlist.add((ClientAllocationToKpoExecutive)scrollableResults1.get()[0]);
					  session1.evict(scrollableResults1.get()[0]);
			         }
				  session1.clear();
			
			for(ClientAllocationToKpoExecutive obj : clientAllcationlist)
			{
			        if(obj.getCompany()!=null)
			        {
			        	
					Integer inactiveCount=0;
					Company comp = findOneWithAll(obj.getCompany().getCompany_id());
					for(User user : comp.getUser())
					{
						Integer count = dao.findAllLogInLogBefore3DaysAgo(user.getUser_id());// If user is inactive from last 3 days then it will return count = 0
					
						inactiveCount=inactiveCount+count;
					}
					if(inactiveCount.equals(0))
					{
						inactiveCompanyList.add(comp);
					}
					
			        }
			}
			}
			
		}
		else if(role.equals(MyAbstractController.ROLE_EXECUTIVE))
		{
			
			List<ClientAllocationToKpoExecutive> clientAllcationlist = new ArrayList<>();

			Session session = getCurrentSession();
			Query query = session.createQuery("SELECT clientAllo from ClientAllocationToKpoExecutive clientAllo WHERE clientAllo.user.user_id=:user_id");
			query.setParameter("user_id", user_id);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			
			  while (scrollableResults.next()) {
				  clientAllcationlist.add((ClientAllocationToKpoExecutive)scrollableResults.get()[0]);
			     session.evict(scrollableResults.get()[0]);
		         }
			  session.clear();
			
			for(ClientAllocationToKpoExecutive obj : clientAllcationlist)
			{
				  if(obj.getCompany()!=null)
			        {
			        	
					Integer inactiveCount=0;
					Company comp = findOneWithAll(obj.getCompany().getCompany_id());
					for(User user : comp.getUser())
					{
						Integer count = dao.findAllLogInLogBefore3DaysAgo(user.getUser_id());// If user is inactive from last 3 days then it will return count = 0
					
						inactiveCount=inactiveCount+count;
					}
					if(inactiveCount.equals(0))
					{
						inactiveCompanyList.add(comp);
					}
					
			        }
			}
			
		}
			return inactiveCompanyList;
	}

	@Override
	public Company getCompanyById(Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Company.class);
						 criteria.add(Restrictions.eq("company_id", companyId));
				
	return (Company) criteria.uniqueResult();
	}
	
}