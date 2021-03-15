/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IKPOExecutiveDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Country;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class KPOExecutiveDAOImpl extends AbstractHibernateDao<User> implements IKPOExecutiveDAO{

	
	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#findOne(java.lang.Long)
	 */
	@Override
	public User findOne(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#findOne(org.hibernate.Criteria)
	 */
	@Override
	public User findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#findOne(java.lang.String)
	 */
	@Override
	public User findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#findAll()
	 */
	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#create(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void create(User entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#update(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void update(User entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#merge(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void merge(User entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#delete(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void delete(User entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#deleteById(java.lang.Long)
	 */
	@Override
	public void deleteById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#deleteById(java.lang.String)
	 */
	@Override
	public void deleteById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#count()
	 */
	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#validatePreTransaction(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public boolean validatePreTransaction(User entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#refresh(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void refresh(User entity) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IKPOExecutiveDAO#findAllKPOExecutive()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllKPOExecutive() {
		List<Long> list = new ArrayList<Long>();
		list.add((long)3);
		list.add((long)4);
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.in("role_id2", list));
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public User isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
