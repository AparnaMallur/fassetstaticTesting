/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IClientValidationChecklistDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.Country;
import com.fasset.entities.IndustryType;
import com.fasset.entities.SubLedger;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class ClientValidationChecklistDAOImpl extends AbstractHibernateDao<ClientValidationChecklist> implements IClientValidationChecklistDAO{

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IClientValidationChecklistDAO#saveClientValidationChecklistDao(com.fasset.entities.ClientValidationChecklist)
	 */
	@Override
	public Long saveClientValidationChecklistDao(ClientValidationChecklist validation) {
		Session session = getCurrentSession();
		validation.setCreated_date(new LocalDateTime());
		Long id = (Long) session.save(validation);
		session.flush();
		session.clear();
		return id;
	}
	
	@Override
	public void update(ClientValidationChecklist entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setUpdate_date(new LocalDateTime());
		session.merge(entity);
		session.flush();
		session.clear();
	}

	@Override
	public ClientValidationChecklist findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(ClientValidationChecklist.class);
		criteria.add(Restrictions.eq("checklist_id", id));
		return (ClientValidationChecklist) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClientValidationChecklist> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from ClientValidationChecklist");
		return query.list();
	}
	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IClientValidationChecklistDAO#deleteCompany(java.lang.Long, java.lang.Long)
	 */
	@Override
	public String deleteCompany(Long chekId, Long comId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public ClientValidationChecklist isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
