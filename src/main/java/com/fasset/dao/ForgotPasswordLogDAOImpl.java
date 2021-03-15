package com.fasset.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IForgotPasswordLogDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Customer;
import com.fasset.entities.ForgotPasswordLog;
import com.fasset.entities.GstTaxMaster;
import com.fasset.exceptions.MyWebException;

@Repository
public class ForgotPasswordLogDAOImpl extends AbstractHibernateDao<ForgotPasswordLog> implements IForgotPasswordLogDAO {

	@Override
	public ForgotPasswordLog findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(ForgotPasswordLog.class);
		criteria.add(Restrictions.eq("trasaction_id", id));
		return (ForgotPasswordLog) criteria.uniqueResult();
	}

	@Override
	public ForgotPasswordLog findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ForgotPasswordLog findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ForgotPasswordLog> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(ForgotPasswordLog.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	@Override
	public void create(ForgotPasswordLog entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);	
		session.flush();
	    session.clear();
	}

	@Override
	public void update(ForgotPasswordLog entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public void merge(ForgotPasswordLog entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(ForgotPasswordLog entity) throws MyWebException {
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
	public boolean validatePreTransaction(ForgotPasswordLog entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(ForgotPasswordLog entity) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ForgotPasswordLog isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ForgotPasswordLog getLastPasswordUpdateDate(Long userId) {
		Criteria criteria = getCurrentSession().createCriteria(ForgotPasswordLog.class);
		criteria.add(Restrictions.eq("user.user_id", userId));
		criteria.addOrder(Order.desc("trasaction_id"));
		criteria.setMaxResults(1);
		return (ForgotPasswordLog) criteria.uniqueResult();
	}

}
