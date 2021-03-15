/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Country;
import com.fasset.entities.IndustryType;
import com.fasset.entities.Suppliers;
import com.fasset.exceptions.MyWebException;
import com.google.common.base.Preconditions;


/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class CountryDAOImpl extends AbstractHibernateDao<Country> implements ICountryDAO{

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.ICountryDAO#saveSuppliersdao(com.fasset.entities.Country)
	 */
	@Override
	public Long saveCountrydao(Country country) {
		
		Session session = getCurrentSession();
		country.setCreated_date(new LocalDateTime());
		Long id = (Long) session.save(country);
		session.flush();
		session.clear();
		return id;
	}
	@Override
	public Country findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Country.class);
		criteria.add(Restrictions.eq("country_id", id));
		return (Country) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Country> findAll() {
		List<Country> list = new ArrayList<Country>();
		Session session = getCurrentSession();
		Query query = session.createQuery("from Country where status=true order by country_name ASC "); 
		 list = query.list();
		 session.clear();
		 return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Country> findAllListing() {
	
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select country_id, country_name, status from country_master order by country_id desc");
		return query.list();
	}
	
	
	@Override
	public void create(Country entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDateTime());
		session.save(entity);	
		session.flush();
		session.clear();
	}

	@Override
	public void update(Country entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setUpdate_date(new LocalDateTime());
		session.merge(entity);
		session.flush();
		session.clear();
		
	}
	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.ICountryDAO#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		String msg="";
		String res="You can't delete country because it is releted to other states";
		if(res != null)
		{
			msg= "You can't delete country because it is releted to other states";
		}
		else 
		{

		Query query = session.createQuery("delete from Country where country_id =:id");
		query.setParameter("id", entityId);
		try{
		query.executeUpdate();
		msg= "Country Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete country because it is releted to other states";
			
		}
		}
		
		return msg;
	}
	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public Country isExists(String name) {
		Criteria criteria = getCurrentSession().createCriteria(Country.class);
		criteria.add(Restrictions.eq("country_name", name));
		return (Country) criteria.uniqueResult();
	}
	
	
	
}
