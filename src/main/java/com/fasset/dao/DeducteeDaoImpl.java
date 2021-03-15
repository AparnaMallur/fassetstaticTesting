package com.fasset.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import com.fasset.dao.interfaces.IDeducteeDao;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Country;
import com.fasset.entities.Deductee;
import com.fasset.entities.TDS_Type;
import com.fasset.exceptions.MyWebException;
@Repository
public class DeducteeDaoImpl extends AbstractHibernateDao<Deductee> implements IDeducteeDao{

	@Override
	public Deductee isExists(String name) {
		Criteria criteria = getCurrentSession().createCriteria(Deductee.class);
		criteria.add(Restrictions.eq("deductee_title", name));
		return (Deductee) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deductee> findAllDeducteeOfCompany(Long CompanyId) {
		
		Session session = getCurrentSession();
		Query query = session.createQuery("from Deductee where company.company_id=:company_id");
		query.setParameter("company_id", CompanyId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deductee> findAllDeductee() {
		List<Deductee> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("from Deductee where status=:status order by deductee_title ASC");
		query.setParameter("status", true);
		list = query.list();
		session.clear();
		return list;
	}

	@Override
	public Deductee findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Deductee.class);
		criteria.add(Restrictions.eq("deductee_id", id));
		return (Deductee) criteria.uniqueResult();
	}

	@Override
	public Long saveDeductee(Deductee deductee) {
		
		Session session = getCurrentSession();
		
		Long tdstypeid = deductee.getTds_type_id();
		Criteria criteria = getCurrentSession().createCriteria(TDS_Type.class);
		criteria.add(Restrictions.eq("tdsType_id", tdstypeid));
		
		TDS_Type tdstype = (TDS_Type) criteria.uniqueResult();
		deductee.setTds_type(tdstype);
		Long id= (Long) session.save(deductee);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public List<Deductee> findAllDeducteeListing() {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select deductee_id,deductee_title,value,status from deductee_master order by deductee_id desc");
		return query.list();
	}
	
	@Override
	public void update(Deductee entity) throws MyWebException
	{
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Deductee.class);
		criteria.add(Restrictions.eq("deductee_id", entity.getDeductee_id()));
		Deductee deductee= (Deductee) criteria.uniqueResult();
		deductee.setUpdate_date(new LocalDateTime());
		deductee.setUpdated_by(entity.getUpdated_by());
		deductee.setIndustryType(entity.getIndustryType());
		deductee.setValue(entity.getValue());
		deductee.setStatus(entity.getStatus());
		deductee.setDeductee_title(entity.getDeductee_title());
		session.merge(deductee);
		session.flush();
		session.clear();
	}
		
	@Override
	public String deleteDeducteeByIdValue(Long id) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from Deductee where deductee_id =:id");
		query.setParameter("id", id);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "TDS Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete TDS";

		}
		return msg;
	}

	@Override
	public Float getTDSRate(org.joda.time.LocalDate effectiveDate, Long tdsTypeId) {
		Session session = getCurrentSession();
		Query query = session.createQuery(" from Deductee where tds_type.tdsType_id =:id and Effective_date <=:dt order by Effective_date desc");
		query.setParameter("id", tdsTypeId);
		query.setParameter("dt", effectiveDate);
		query.setMaxResults(1);
		Deductee deductee=(Deductee) query.uniqueResult();
		Float rate =deductee.getValue();
		System.out.println("Th rate is "+rate);
		return rate;
	}

	@Override
	public Float getTDSRateByTdsType(Long tdsTypeId) {
		Session session = getCurrentSession();
		Query query = session.createQuery(" from Deductee where tds_type.tdsType_id =:id  order by Effective_date desc ");
		query.setParameter("id", tdsTypeId);
		query.setMaxResults(1);
		Deductee deductee=(Deductee) query.uniqueResult();
		Float rate =deductee.getValue();
		return rate;
	}

	@Override
	public Deductee isExists1(String name, org.joda.time.LocalDate effectiveDate) {
		Criteria criteria = getCurrentSession().createCriteria(Deductee.class);
		criteria.add(Restrictions.eq("deductee_title", name));
		criteria.add(Restrictions.eq("Effective_date", effectiveDate));
		return (Deductee) criteria.uniqueResult();
	}

	
	
	
}
