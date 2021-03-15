/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.IindustryTypeDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.entities.IndustryType;
import com.fasset.entities.SubLedger;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ISubLedgerService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class IndustryTypeDAOImpl extends AbstractHibernateDao<IndustryType> implements IindustryTypeDAO{

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IindustryTypeDAO#saveindustryTypedao(com.fasset.entities.IndustryType)
	 */
	@Autowired
	private ISubLedgerDAO subLedgerDao;
	
	
	@Override
	public Long saveindustryTypedao(IndustryType type) {
	
		Session session = getCurrentSession();
		type.setCreated_date(new LocalDateTime());
		List<String> subLedgerList = type.getSubLedgerList();
		Set<SubLedger> subLedgers = new HashSet<SubLedger>();
		for(String id :subLedgerList)
		{
			Long sid = Long.parseLong(id);
			try {
				SubLedger subledger = subLedgerDao.findOne(sid);
				subLedgers.add(subledger);
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		type.setSubLedgers(subLedgers);
		Long id = (Long) session.save(type);
		session.flush();
	    session.clear();
		return id;
	}

	@Override
	public IndustryType findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(IndustryType.class);
		criteria.add(Restrictions.eq("industry_id", id));
		return (IndustryType) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IndustryType> findAllListing() {		
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select industry_id, industry_name, status from industry_type order by industry_id desc");
		return query.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<IndustryType> findAll() {		
		List<IndustryType> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("from IndustryType where status=:status order by industry_name ASC");
		query.setParameter("status", true);
		list = query.list();
		session.clear();
		return list;
	}

	
	@Override
	public void update(IndustryType entity) throws MyWebException {
		Session session = getCurrentSession();
		Criteria criteria = getCurrentSession().createCriteria(IndustryType.class);
		criteria.add(Restrictions.eq("industry_id", entity.getIndustry_id()));
		IndustryType newEntity = (IndustryType) criteria.uniqueResult();
		Set<SubLedger> subLedgers = newEntity.getSubLedgers();
		newEntity.setUpdate_date(new LocalDateTime());
		newEntity.setUpdated_by(entity.getUpdated_by());
		newEntity.setCreated_by(entity.getCreated_by());
		newEntity.setCreated_date(entity.getCreated_date());
		newEntity.setIndustry_name(entity.getIndustry_name());
		newEntity.setIp_address(entity.getIp_address());
		newEntity.setStatus(entity.getStatus());
		List<String> subLedgerList = entity.getSubLedgerList();
	
		
		
		for(String id :subLedgerList)
		{
			Long sid = Long.parseLong(id);
			try {
				SubLedger subledger = subLedgerDao.findOne(sid);
				subLedgers.add(subledger);
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		newEntity.setSubLedgers(subLedgers);
		session.merge(newEntity);
		session.flush();
	    session.clear();
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IindustryTypeDAO#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from IndustryType where industry_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "IndustryType Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete IndustryType";
			
		}
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IindustryTypeDAO#deleteSubLedger(java.lang.Long, java.lang.Long)
	 */
	@Override
	public String deleteSubLedger(Long induId, Long subId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from IndustrySubLedgerEntityClass where industry_id =:induId and sub_ledger_Id=:subId");
		query.setParameter("induId", induId);
		query.setParameter("subId", subId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "Sub Ledger Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete SubLedger";
			
		}
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public IndustryType isExists(String name) {
		Criteria criteria = getCurrentSession().createCriteria(IndustryType.class);
		criteria.add(Restrictions.eq("industry_name", name));
		return (IndustryType) criteria.uniqueResult();
	}

	@Override
	public List<IndustryType> findAllactive() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from IndustryType where status=:status order by industry_id desc");
		query.setParameter("status", true);
		return query.list();
	}

	@Override
	public IndustryType findOneWithAll(Long Id) {
		Criteria criteria = getCurrentSession().createCriteria(IndustryType.class);
		criteria.add(Restrictions.eq("industry_id", Id));
		criteria.setFetchMode("subLedgers", FetchMode.JOIN);
		criteria.setFetchMode("ledger", FetchMode.JOIN);
		return (IndustryType) criteria.uniqueResult();		
	}
	

	
	
}
