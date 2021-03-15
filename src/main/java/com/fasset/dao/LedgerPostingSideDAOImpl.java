/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.ILedgerPostingSideDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.LedgerPostingSide;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class LedgerPostingSideDAOImpl extends AbstractHibernateDao<LedgerPostingSide> implements ILedgerPostingSideDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<LedgerPostingSide> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(LedgerPostingSide.class);
		criteria.addOrder(Order.asc("posting_title"));
		return criteria.list();
	}

	@Override
	public LedgerPostingSide isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LedgerPostingSide findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(LedgerPostingSide.class);
		criteria.add(Restrictions.eq("posting_id", id));
		return (LedgerPostingSide) criteria.uniqueResult();
	}

	@Override
	public LedgerPostingSide findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LedgerPostingSide findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(LedgerPostingSide entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(LedgerPostingSide entity) throws MyWebException {
		Session session = getCurrentSession();
		session.update(entity);		
	}

	@Override
	public void merge(LedgerPostingSide entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(LedgerPostingSide entity) throws MyWebException {
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
	public boolean validatePreTransaction(LedgerPostingSide entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(LedgerPostingSide entity) {
		// TODO Auto-generated method stub
		
	}
}
