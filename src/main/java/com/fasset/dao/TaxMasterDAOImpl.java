/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.ITaxMasterDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Country;
import com.fasset.entities.Product;
import com.fasset.entities.TaxMaster;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class TaxMasterDAOImpl extends AbstractHibernateDao<TaxMaster> implements ITaxMasterDAO{

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.ITaxMasterDAO#saveTaxMasterDao(com.fasset.entities.TaxMaster)
	 */
	@Override
	public Long saveTaxMasterDao(TaxMaster taxMaster) {
		Session session = getCurrentSession();
		taxMaster.setCreated_date(new LocalDateTime());
		Long id = (Long) session.save(taxMaster);
		session.flush();
	    session.clear();
		return id;
	}

	@Override
	public TaxMaster findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(TaxMaster.class);
		criteria.add(Restrictions.eq("tax_id", id));
		return (TaxMaster) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaxMaster> findAll() {
		
		Session session = getCurrentSession();
		Query query = session.createQuery("from TaxMaster where status=:status order by tax_name ASC");
		query.setParameter("status", true);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaxMaster> findAllListing() {		
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select tax_id, tax_name, vat, cst, excise, status from tax_master order by tax_id desc");
		return query.list();
	}
	
	@Override
	public void update(TaxMaster entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setUpdate_date(new LocalDateTime());
		session.merge(entity);
		session.flush();
	    session.clear();
		
	}
	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.ITaxMasterDAO#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from TaxMaster where tax_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "Tax Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete Tax";
			
		}
		return msg;	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public TaxMaster isExists(String name) {
		Criteria criteria = getCurrentSession().createCriteria(TaxMaster.class);
		criteria.add(Restrictions.eq("tax_name", name));
		return (TaxMaster) criteria.uniqueResult();
	}

	
	
}