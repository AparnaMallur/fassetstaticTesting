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
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.ICompanyStatutoryTypeDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.entities.Country;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class CompanyStatutoryTypeDAOImpl extends AbstractHibernateDao<CompanyStatutoryType> implements ICompanyStatutoryTypeDAO{

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.ICompanyStatutoryTypeDAO#saveCompanyStatutoryTypeDao(com.fasset.entities.CompanyStatutoryType)
	 */
	@Override
	public Long saveCompanyStatutoryTypeDao(CompanyStatutoryType type) {
		
		Session session = getCurrentSession();
		type.setCreated_date(new LocalDateTime());
		Long id = (Long) session.save(type);
		session.flush();
		session.clear();
		return id;
	}		
	@Override
	public CompanyStatutoryType findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(CompanyStatutoryType.class);
		criteria.add(Restrictions.eq("company_statutory_id", id));
		return (CompanyStatutoryType) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CompanyStatutoryType> findAll() {
		List<CompanyStatutoryType> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("from CompanyStatutoryType where status=:status order by company_statutory_name ASC");
		query.setParameter("status", true);
		list = query.list();
		session.clear();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CompanyStatutoryType> findAllListing() {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select company_statutory_id, company_statutory_name, status from company_statutory_type order by company_statutory_id desc");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CompanyStatutoryType> findAllactive() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from CompanyStatutoryType where status=:status order by company_statutory_name ASC");
		query.setParameter("status", true);
		return query.list();
	}
	@Override
	public void create(CompanyStatutoryType entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDateTime());
		session.save(entity);	
		session.flush();
		session.clear();
	}

	@Override
	public void update(CompanyStatutoryType entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setUpdate_date(new LocalDateTime());
		session.merge(entity);
		session.flush();
		session.clear();
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.ICompanyStatutoryTypeDAO#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from CompanyStatutoryType where company_statutory_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "CompanyStatutoryType Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete CompanyStatutoryType";
			
		}
		return msg;
	}
	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public CompanyStatutoryType isExists(String name) {
		Criteria criteria = getCurrentSession().createCriteria(CompanyStatutoryType.class);
		criteria.add(Restrictions.eq("company_statutory_name", name));
		return (CompanyStatutoryType) criteria.uniqueResult();
	}

}
