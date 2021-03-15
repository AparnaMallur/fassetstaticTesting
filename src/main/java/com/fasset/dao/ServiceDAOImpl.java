/**
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

import com.fasset.dao.interfaces.IServiceDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Service;
import com.fasset.entities.ServiceFrequency;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class ServiceDAOImpl extends AbstractHibernateDao<Service> implements IServiceDAO{


	@Override
	public Long saveServiceDao(Service service) {
		Session session = getCurrentSession();
		service.setCreated_date(new LocalDateTime());
		Long frequency_id= service.getService_frequency();
		//if(frequency_id > 0) {
			Criteria criteria = getCurrentSession().createCriteria(ServiceFrequency.class);
			criteria.add(Restrictions.eq("frequency_id", frequency_id));
			
			ServiceFrequency frequency = (ServiceFrequency) criteria.uniqueResult();
			service.setServiceFrequency(frequency);
		//}
		Long id = (Long) session.save(service);
		session.flush();
	    session.clear();
		return id;
	}
	
	@Override
	public Service findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Service.class);
		criteria.add(Restrictions.eq("id", id));
		return (Service) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Service> findAll() {
		List<Service> list = new ArrayList<Service>();
		Session  session = getCurrentSession();
		Criteria criteria = session.createCriteria(Service.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.addOrder(Order.asc("service_name"));
		list = criteria.list();
		session.clear();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Service> findAllListing() {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select id, service_name, status from service_master order by id desc");
		return query.list();
	}
	
	
	@Override
	public void create(Service entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDateTime());
		session.save(entity);		
		session.flush();
	    session.clear();
	}

	@Override
	public void update(Service entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setUpdate_date(new LocalDateTime());
         Long frequency_id= entity.getService_frequency();
		
		Criteria criteria = getCurrentSession().createCriteria(ServiceFrequency.class);
		criteria.add(Restrictions.eq("frequency_id", frequency_id));
		
		ServiceFrequency frequency = (ServiceFrequency) criteria.uniqueResult();
		entity.setServiceFrequency(frequency);
		session.merge(entity);
		session.flush();
	    session.clear();
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IServiceDAO#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from Service where id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "Service Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete Service";
			
		}
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public Service isExists(String name) {
	
		Criteria criteria = getCurrentSession().createCriteria(Service.class);
		criteria.add(Restrictions.eq("service_name", name));
		return (Service) criteria.uniqueResult();
	}

}