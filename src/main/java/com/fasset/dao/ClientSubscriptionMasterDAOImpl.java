package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IClientSubscriptionMasterDao;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.City;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Company;
import com.fasset.exceptions.MyWebException;

@Repository
public class ClientSubscriptionMasterDAOImpl extends AbstractHibernateDao<ClientSubscriptionMaster> implements IClientSubscriptionMasterDao{

	@Override
	public ClientSubscriptionMaster findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(ClientSubscriptionMaster.class);
		criteria.add(Restrictions.eq("subscription_id", id));
		return (ClientSubscriptionMaster) criteria.uniqueResult();
	}

	@Override
	public ClientSubscriptionMaster findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientSubscriptionMaster findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientSubscriptionMaster> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(ClientSubscriptionMaster entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
		session.clear();
		
	}

	@Override
	public void update(ClientSubscriptionMaster entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);	
		session.flush();
		session.clear();
	}

	@Override
	public void merge(ClientSubscriptionMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(ClientSubscriptionMaster entity) throws MyWebException {
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
	public boolean validatePreTransaction(ClientSubscriptionMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(ClientSubscriptionMaster entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ClientSubscriptionMaster isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientSubscriptionMaster getClientSubscriptionByCompanyId(Long CompanyId) {
		Criteria criteria = getCurrentSession().createCriteria(ClientSubscriptionMaster.class);
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		return (ClientSubscriptionMaster) criteria.uniqueResult();
	}

	@Override
	public Long getquoteofcompany(Long company_id) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();		
		Long service_id;
		Query subquery = session.createSQLQuery("select quotation_id FROM client_subscription_master WHERE company_id = :company_id limit 0,1");
		subquery.setParameter("company_id", company_id);
		if(subquery.list()==null || subquery.list().isEmpty())
		{
			service_id= new Long(0);
		}
		else
		{
			if(subquery.list().get(0)==null)
				service_id=(long) 0;
			else
			service_id=Long.parseLong(subquery.list().get(0).toString());			
		}
		
		return service_id;
	}

	@Override
	public List<ClientSubscriptionMaster> getSubscriptionReport() {
		
		List<ClientSubscriptionMaster> subscriptionlist = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT subscri from ClientSubscriptionMaster subscri");
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
			  subscriptionlist.add((ClientSubscriptionMaster)scrollableResults.get()[0]);
		     session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		return subscriptionlist;
	}

	@Override
	public ClientSubscriptionMaster getClientSubscriptionByQuotationId(Long quotationId) {
		Criteria criteria = getCurrentSession().createCriteria(ClientSubscriptionMaster.class);
		criteria.add(Restrictions.eq("quotation_id.quotation_id", quotationId));
		return (ClientSubscriptionMaster) criteria.uniqueResult();
	}

}
