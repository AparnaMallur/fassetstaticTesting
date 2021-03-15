/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IAccountGroupTypeDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountGroupType;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class AccountGroupTypeDAOImpl extends AbstractHibernateDao<AccountGroupType> implements IAccountGroupTypeDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountGroupType> findAll() {
		List<AccountGroupType> list = new ArrayList<AccountGroupType>();
		Session session =  getCurrentSession();
		Criteria criteria =session.createCriteria(AccountGroupType.class);
		criteria.addOrder(Order.asc("account_group_name"));
		list = criteria.list();
		session.clear();
		return list;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public AccountGroupType isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AccountGroupType findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(AccountGroupType.class);
		criteria.add(Restrictions.eq("account_group_id", id));
		return (AccountGroupType) criteria.uniqueResult();
	}

	@Override
	public AccountGroupType findOne(Criteria crt) throws MyWebException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountGroupType findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(AccountGroupType entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(AccountGroupType entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(AccountGroupType entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(AccountGroupType entity) throws MyWebException {
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
	public boolean validatePreTransaction(AccountGroupType entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(AccountGroupType entity) {
		// TODO Auto-generated method stub
		
	}
	
}
