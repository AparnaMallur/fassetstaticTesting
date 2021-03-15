/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IServiceFrequencyDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.City;
import com.fasset.entities.ServiceFrequency;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class ServiceFrequencyDAOImpl extends AbstractHibernateDao<ServiceFrequency> implements IServiceFrequencyDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceFrequency> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(ServiceFrequency.class);
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public ServiceFrequency isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceFrequency findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(ServiceFrequency.class);
		criteria.add(Restrictions.eq("frequency_id", id));
		return (ServiceFrequency) criteria.uniqueResult();
	}
}
