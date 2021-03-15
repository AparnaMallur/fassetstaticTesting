package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IUnitOfMeasurementDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.UnitOfMeasurement;
import com.fasset.exceptions.MyWebException;

@Repository
public class UnitOfMeasurementDAOImpl extends AbstractHibernateDao<UnitOfMeasurement> implements IUnitOfMeasurementDAO{

	@Override
	public UnitOfMeasurement findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(UnitOfMeasurement.class);
		criteria.add(Restrictions.eq("uom_id", id));
		return (UnitOfMeasurement) criteria.uniqueResult();
	}

	@Override
	public UnitOfMeasurement findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UnitOfMeasurement findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnitOfMeasurement> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from UnitOfMeasurement where status=:status order by unit ASC");
		query.setParameter("status", true);
		return query.list();
	}

	@Override
	public List<UnitOfMeasurement> findAllListing() {
		/*Session session = getCurrentSession();
		Query query = session.createSQLQuery("select uom_id,unit,status from unit_of_measurement order by uom_id desc");
		return query.list();*/
		List<UnitOfMeasurement> list =  new ArrayList<UnitOfMeasurement>();
		Session session = getCurrentSession();
		ScrollableResults scrollableResults = session.createQuery("from UnitOfMeasurement").setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((UnitOfMeasurement)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
	}
	
	@Override
	public void create(UnitOfMeasurement entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);	
		session.flush();
	    session.clear();
		
	}

	@Override
	public void update(UnitOfMeasurement entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public void merge(UnitOfMeasurement entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(UnitOfMeasurement entity) throws MyWebException {
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
	public boolean validatePreTransaction(UnitOfMeasurement entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(UnitOfMeasurement entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UnitOfMeasurement isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from UnitOfMeasurement where uom_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
			query.executeUpdate();
			msg= "Unit Of Measurement Deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete Unit Of Measurement";
		}
		return msg;
	}

}
