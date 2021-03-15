/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Country;
import com.fasset.entities.Product;
import com.fasset.entities.VoucherSeries;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Transactional
@Repository
public class AccountingYearDAOImpl extends AbstractHibernateDao<AccountingYear> implements IAccountingYearDAO{

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IAccountingYearDAO#saveAccountingYearDao(com.fasset.entities.AccountingYear)
	 */
	@Override
	public Long saveAccountingYearDao(AccountingYear year) {
		Session session = getCurrentSession();
		year.setCreated_date(new LocalDateTime());
		Long id = (Long) session.save(year);
		session.flush();
		session.clear();
		return id;
	}

	
	@Transactional
	@Override
	public AccountingYear findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(AccountingYear.class);
		criteria.add(Restrictions.eq("year_id", id));
		return (AccountingYear) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AccountingYear> findAll() {
		
		Session session = getCurrentSession();
		Query query = session.createQuery("from AccountingYear where status=:status order by year_id desc");
		query.setParameter("status", true);
		return query.list();
	}
	
	@Override
	public void update(AccountingYear entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setUpdate_date(new LocalDateTime());
		session.merge(entity);
		session.flush();
		session.clear();
		
	}
	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from AccountingYear where year_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "Accounting Year Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete Accounting Year ";			
		}
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public AccountingYear isExists(String name) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(AccountingYear.class);
		criteria.add(Restrictions.eq("year_range", name));
		criteria.add(Restrictions.eq("status", true));
		return (AccountingYear) criteria.uniqueResult();
		
	}

	@Override
	public List<AccountingYear> findAccountRange(Long userId, String yearId,Long Comapny_id) {
		
		List<AccountingYear> list = new ArrayList<>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(YearEnding.class);
		criteria.add(Restrictions.eq("company.company_id", Comapny_id));
		
		//criteria.add(Restrictions.eq("yearEndingstatus", (long)1));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<YearEnding> yearList  =  criteria.list();

		session.clear();
		for(YearEnding year : yearList)
		{
			list.add(year.getAccountingYear());
		}
		return list;
		
	}

	@Override
	public int findactiveyear(Long yid) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query querymenu;
		if(yid!=null)
		{
			querymenu = session.createQuery("FROM AccountingYear WHERE status = :status and year_id!= :year_id");
		    querymenu.setParameter("status", true);
		    querymenu.setParameter("year_id", yid);

		}
		else
		{
		querymenu = session.createQuery("FROM AccountingYear WHERE status = :status");
	    querymenu.setParameter("status", true);
		}
	    int count=querymenu.list().size();
		return count;
	}

	@Transactional
	@Override
	public Long findcurrentyear() {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query querymenu;	
		querymenu = session.createSQLQuery("select year_id FROM accounting_year WHERE status = :status");
	    querymenu.setParameter("status", true);		
		if(querymenu.list()==null || querymenu.list().isEmpty())
			return (long) 0;
		else
	    return new Long(querymenu.list().get(0).toString()) ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountingYear> findAccountRangeOfCompany(String yearId) {
		List<AccountingYear> yearList = new ArrayList<AccountingYear>();
		if(yearId!=null)
		{
		String[] years=yearId.split(",");
		List<Long> yearIds = new ArrayList<Long>();
		for (String yId : years) {
			yearIds.add(new Long(yId));
		}
		Session session = getCurrentSession();
		
		Query querymenu;
		querymenu = session.createQuery("FROM AccountingYear WHERE status=:status and year_id in (:year_id)");
		querymenu.setParameter("status", true);
	    querymenu.setParameterList("year_id", yearIds);
	    yearList= querymenu.list();
		
		}
		return yearList;
	}

	@Override
	public List<AccountingYear> findAllListing() {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select year_id, year_range, start_date, end_date,status from accounting_year order by year_id desc");
		return query.list();
	}


	@Override
	public List<AccountingYear> findAccountRangeOfYearId(Long userId, Long yearId, Long Comapny_id) {
		List<AccountingYear> list = new ArrayList<>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(YearEnding.class);
		criteria.add(Restrictions.eq("company.company_id", Comapny_id));
		criteria.add(Restrictions.eq("accountingYear.year_id", yearId));
		//criteria.add(Restrictions.eq("yearEndingstatus", (long)1));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<YearEnding> yearList  =  criteria.list();

		session.clear();
		for(YearEnding year : yearList)
		{
			list.add(year.getAccountingYear());
		}
		return list;
	}


	@Override
	public AccountingYear findAccountRange(Long YearId) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(AccountingYear.class);
		criteria.add(Restrictions.eq("year_id", YearId));
		criteria.add(Restrictions.eq("status", true));
		return (AccountingYear) criteria.uniqueResult();
	}

}
