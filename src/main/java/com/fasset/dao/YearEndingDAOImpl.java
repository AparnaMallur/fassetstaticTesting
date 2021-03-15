package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IBankDAO;
import com.fasset.dao.interfaces.IClientSubscriptionMasterDao;
import com.fasset.dao.interfaces.ICustomerDAO;
import com.fasset.dao.interfaces.ILedgerDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.ISupplierDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.dao.interfaces.IYearEndingDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.ActivityLog;
import com.fasset.entities.LoginLog;
import com.fasset.entities.YearEndJvSubledgerDetails;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;

@Repository
public class YearEndingDAOImpl extends AbstractHibernateDao<YearEnding> implements IYearEndingDAO{

	@Autowired	
	private IBankDAO bankDao;
	@Autowired	
	private IUserDAO userDao;
	@Autowired	
	private ICustomerDAO cusDao;
	@Autowired	
	private ISupplierDAO supDao;
	@Autowired	
	private IProductDAO productDao;
	@Autowired	
	private ILedgerDAO ledgrDao;
	@Autowired	
	private ISubLedgerDAO subledgerDao;
	@Autowired	
	private IClientSubscriptionMasterDao subscriptionDao;
	
	@Override
	public YearEnding isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public YearEnding findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(YearEnding.class);
		criteria.add(Restrictions.eq("year_ending_id", id));
		return (YearEnding) criteria.uniqueResult();
	}
	
	@Override
	public void create(YearEnding yearend) throws MyWebException {
		Session session = getCurrentSession();
		session.save(yearend);
		session.flush();
	    session.clear();
	}

	@Override
	public void update(YearEnding yearend) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(yearend);
		session.flush();
	    session.clear();
		
	}

	@Override
	public List<YearEnding> findAllYearEnding(Long comapany_id) {
		List<YearEnding> list = new ArrayList<>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(YearEnding.class);
		criteria.add(Restrictions.eq("company.company_id", comapany_id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		list = criteria.list();
		session.clear();
		return list;
	}

	@Override
	public List<YearEnding> findAllYearEnding() {
		Criteria criteria = getCurrentSession().createCriteria(YearEnding.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<ActivityLog> findAllActivityLog(Long user_id,LocalDate from_date, LocalDate to_date) {
		List<ActivityLog> list = new ArrayList<>(); 
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT log from ActivityLog log WHERE log.user.user_id =:user_id and log.created_date>=:from_date and log.created_date<=:to_date");
		query.setParameter("user_id", user_id);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
		   list.add((ActivityLog)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		return list;
	}

	@Override
	public void saveActivityLogForm(Long user_id, Long customer_id, Long supplier_id, Long product_id, Long ledger_id,
			Long subLedger_id, Long bank_id, Long quotation_id,Long primary_approval,Long secondary_approval,Long rejection) {
		try
		{
		ActivityLog log = new ActivityLog();
		log.setUser(userDao.findOne(user_id));
		log.setCreated_date(new LocalDate());
		if(primary_approval!=null)
		{
			log.setPrimary_approval(primary_approval);
		}
		if(secondary_approval!=null)
		{
			log.setSecondary_approval(secondary_approval);
		}
		if(rejection!=null)
		{
			log.setRejection(rejection);
		}
		if(customer_id!=null)
		{
		log.setCustomer(cusDao.findOne(customer_id));
		}
		if(supplier_id!=null)
		{
		log.setSupplier(supDao.findOne(supplier_id));
		}
		if(bank_id!=null)
		{
		log.setBank(bankDao.findOne(bank_id));
		}
		if(product_id!=null)
		{
		log.setProduct(productDao.findOne(product_id));
		}
		if(ledger_id!=null)
		{
		log.setLedger(ledgrDao.findOne(ledger_id));
		}
		if(subLedger_id!=null)
		{
		log.setSubLedger(subledgerDao.findOne(subLedger_id));
		}
		if(quotation_id!=null)
		{
		log.setSubscription(subscriptionDao.getClientSubscriptionByQuotationId(quotation_id));
		}
		Session session = getCurrentSession();
		session.save(log);
		session.flush();
	    session.clear();
		}
		catch(Exception e)
      {
			e.printStackTrace();
      }
	}

	@Override
	public Integer findAllLogInLogBefore3DaysAgo(Long user_id) {
		List<LocalDate>list = new ArrayList<>();
		list.add(new LocalDate());
		list.add(new LocalDate().minusDays(1));
		list.add(new LocalDate().minusDays(2));
		list.add(new LocalDate().minusDays(3));
		Criteria criteria = getCurrentSession().createCriteria(LoginLog.class);
		criteria.add(Restrictions.eq("user.user_id", user_id));
		criteria.add(Restrictions.in("login_date",  list));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public YearEnding findYearEnd(Long yearId,Long company_id) {
		Criteria criteria = getCurrentSession().createCriteria(YearEnding.class);
		criteria.add(Restrictions.eq("accountingYear.year_id", yearId));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		return (YearEnding) criteria.uniqueResult();
	}

	@Override
	public List<YearEndJvSubledgerDetails> findAllYearEndJVdtls(Long id) {
		// TODO Auto-generated method stub
		List<YearEndJvSubledgerDetails> list = new ArrayList<>(); 
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT log from YearEndJvSubledgerDetails log WHERE log.yearEndJV.year_end_jVId =:id");
		query.setParameter("id", id);
		
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
		   list.add((YearEndJvSubledgerDetails)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		return list;
		//return null;
	}

}
