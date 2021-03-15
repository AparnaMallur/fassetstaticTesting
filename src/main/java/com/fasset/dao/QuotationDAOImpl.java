package com.fasset.dao;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Repository;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IQuotationDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.entities.IndustryType;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.Service;
import com.fasset.entities.ServiceFrequency;
import com.fasset.entities.Suppliers;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.QuotationDetailsForm;

@Repository
public class QuotationDAOImpl extends AbstractHibernateDao<Quotation>  implements IQuotationDAO{
	
	@Override
	public void create(Quotation quotation) throws MyWebException {
		Session session = getCurrentSession();
		quotation.setCreated_date(new LocalDate());
		session.save(quotation);	
		session.flush();
	    session.clear();
	}

	@Override
	public void update(Quotation quotation) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(quotation);	
		session.flush();
	    session.clear();
	}
	
	
		
	@Override
	public Quotation isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Quotation> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Quotation order by quotation_id desc");
		return query.list();
	}
	
	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Criteria criteria = getCurrentSession().createCriteria(Quotation.class);
		criteria.add(Restrictions.eq("quotation_id", entityId));
		Quotation quotation = (Quotation) criteria.uniqueResult();
		
		for(QuotationDetails details :quotation.getQuotationDetails())
		{
			Query query1 = session.createSQLQuery("delete from quotation_details where quotation_id =:quId");
			query1.setParameter("quId", entityId);
			query1.executeUpdate();
		}
		Query query = session.createQuery("delete from Quotation where quotation_id=:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
			query.executeUpdate();
			msg= "Quotation Deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete Quotation";
		}	
		return msg;		
	}

	@Override
	public Quotation getQuotation(String mailId, String quotationNumber) {
		Criteria criteria = getCurrentSession().createCriteria(Quotation.class);
		criteria.add(Restrictions.eq("email", mailId));
		criteria.add(Restrictions.eq("quotation_no", quotationNumber));
		return (Quotation) criteria.uniqueResult();
	}

	@Override
	public Long savequote(Quotation quotation) {
		Session session = getCurrentSession();
		quotation.setCreated_date(new LocalDate());
		quotation.setTime(new Time(1000));
		Long statid=quotation.getCompany_statutory_id();
		Criteria criteria = getCurrentSession().createCriteria(CompanyStatutoryType.class);
		criteria.add(Restrictions.eq("company_statutory_id",statid));
		CompanyStatutoryType companytype=(CompanyStatutoryType) criteria.uniqueResult();
		
		Long indid=quotation.getIndustry_id();
		Criteria criteria1 = getCurrentSession().createCriteria(IndustryType.class);
		criteria1.add(Restrictions.eq("industry_id",indid));
		IndustryType industrytype=(IndustryType) criteria1.uniqueResult();
		quotation.setCompanystatutorytype(companytype);
		quotation.setIndustrytype(industrytype);
		
		Long id = (Long) session.save(quotation);
		session.flush();
	    session.clear();
		return id;
	}

	@Override
	public List<QuotationDetails> findAllDetailsList(Long entryId) {
		Criteria criteria = getCurrentSession().createCriteria(QuotationDetails.class);
		criteria.add(Restrictions.eq("quotation_id.quotation_id", entryId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
		
	}
	
	@Override
	public Quotation findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Quotation.class);
		criteria.add(Restrictions.eq("quotation_id", id));
		criteria.setFetchMode("quotationDetails", FetchMode.JOIN);
		return (Quotation) criteria.uniqueResult();
	}

	@Override
	public Quotation findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Quotation findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void merge(Quotation entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Quotation entity) throws MyWebException {
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
	public boolean validatePreTransaction(Quotation entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(Quotation entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String updateQuatationDetails(Quotation quotation) {
		
        Session session = getCurrentSession();
		String msg= null;
		Criteria criteria = getCurrentSession().createCriteria(Quotation.class);
		criteria.add(Restrictions.eq("quotation_id", quotation.getQuotation_id()));
		Quotation newquotation = (Quotation) criteria.uniqueResult();
		
		newquotation.setStatus(quotation.getStatus());
		
		newquotation.setCreated_date(new LocalDate());
		newquotation.setTime(new Time(1000));
		
		Criteria criteria1 = session.createCriteria(Service.class);
		criteria1.add(Restrictions.eq("id", quotation.getService_id()));
		Service Service = (Service) criteria1.uniqueResult();
		
		Criteria criteria2 = session.createCriteria(ServiceFrequency.class);
		criteria2.add(Restrictions.eq("frequency_id", quotation.getFrequency_id()));
		ServiceFrequency frequency = (ServiceFrequency) criteria2.uniqueResult();
		
		QuotationDetails details = new QuotationDetails();
		details.setAmount(quotation.getAmount());
		details.setQuotation_id(quotation);
		details.setFrequency_id(frequency);
		details.setService_id(Service);
		details.setService_status(quotation.getService_status());	
		
		Set<QuotationDetails> quotationDetails= newquotation.getQuotationDetails();
		if(quotationDetails!=null)
		{
		Boolean flag = false;
		for(QuotationDetails olddetails: quotationDetails)
		{
			if(details.equals(olddetails))
			{
				flag=true;
			}
		}
		if(flag==false)
		{
		session.save(details);
		if(quotation.getQuoteDetailsList()!=null)
		{
			 List<QuotationDetailsForm> quoteDetailsList =quotation.getQuoteDetailsList();
			for(QuotationDetailsForm quot : quoteDetailsList )
			{
			Query query = session.createQuery("update QuotationDetails set amount=:amount where quotation_detail_id=:quotation_detail_id");
			query.setParameter("amount",Float.parseFloat(quot.getAmount()));
			query.setParameter("quotation_detail_id",Long.parseLong(quot.getQuotation_detail_id()));
			query.executeUpdate();
			}
		}
		msg= "Quotation updated successfully";
		}
		else {
		msg="Quotation details are alredy exists";
		}
		}
		else
		{
			session.save(details);
			msg= "Quotation updated successfully";
		}
		session.merge(newquotation);
		session.flush();
	    session.clear();
		return msg;
	}

	@Override
	public String deleteQuotationDetails(Long quoatId) {
		
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from QuotationDetails where quotation_detail_id =:quId");
		query.setParameter("quId", quoatId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "Quotation detail deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete Quotation Detail";
			
		}
		return msg;
	}

	@Override
	public Quotation getQuotation(String mailId) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Quotation.class);
		criteria.add(Restrictions.eq("email", mailId));
		return (Quotation) criteria.uniqueResult();
	}

	@Override
	public void updateQuatationDetailsAfterSaveAndBack(Quotation quotation) {
		   Session session = getCurrentSession();
		if(quotation.getQuoteDetailsList()!=null)
		{
			 List<QuotationDetailsForm> quoteDetailsList =quotation.getQuoteDetailsList();
			for(QuotationDetailsForm quot : quoteDetailsList )
			{
			Query query = session.createQuery("update QuotationDetails set amount=:amount where quotation_detail_id=:quotation_detail_id");
			query.setParameter("amount",Float.parseFloat(quot.getAmount()));
			query.setParameter("quotation_detail_id",Long.parseLong(quot.getQuotation_detail_id()));
			query.executeUpdate();
			}
		}
		
	}

	@Override
	public List<QuotationDetails> findAllimportDetailsList() {
		Session session = getCurrentSession();
		Query subquery = session.createQuery("select id FROM Service WHERE service_name = :service_name");
		subquery.setParameter("service_name", "Master Imports");
		
		if(subquery.list()==null || subquery.list().isEmpty())
		{
			return null;
		}
		else
		{
			Long service_id=(Long) subquery.list().get(0);
			Query query = session.createQuery("from QuotationDetails where service_id.id=:serviceId");
			query.setParameter("serviceId", service_id);
			List<QuotationDetails> details= query.list();
			return details;				
		}

	}

	@Override
	public void saveupdatedservices(Long entryId, Boolean status) {
		Session session = getCurrentSession();	
		QuotationDetails quote = new QuotationDetails();
		Criteria criteria = getCurrentSession().createCriteria(QuotationDetails.class);
		criteria.add(Restrictions.eq("quotation_detail_id", entryId));
		quote=(QuotationDetails) criteria.uniqueResult();
		quote.setService_status(status);
		session.save(quote);
		session.flush();
	    session.clear();
	}

	@Override
	public List<QuotationDetails> findAllimportDetailsUser(String email, Long quote_id) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query subquery = session.createQuery("select id FROM Service WHERE service_name = :service_name");
		subquery.setParameter("service_name", "Master Imports");
		
		if(subquery.list()==null || subquery.list().isEmpty())
		{
			return null;
		}
		else
		{
			Long service_id=(Long) subquery.list().get(0);
			Query query = session.createQuery("from QuotationDetails where service_id.id=:serviceId and service_status=:status and quotation_id.quotation_id=:quote_id");
			query.setParameter("serviceId", service_id);
			query.setParameter("status", true);
			query.setParameter("quote_id", quote_id);
			List<QuotationDetails> details= query.list();
			return details;				
		}	
	}

	@Override
	public Integer findAllDashboard() {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Quotation.class);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public List<Quotation> findAllListing() {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select quotation_id, first_name, last_name, quotation_no, company_name, email, mobile_no, status, flag, created_date, time from quotation order by quotation_id desc");
		return query.list();
	}

	@Override
	public List<ClientSubscriptionMaster> findAllSubscriptionList() {
		List<ClientSubscriptionMaster> list =  new ArrayList<ClientSubscriptionMaster>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT client from ClientSubscriptionMaster client");
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
		   list.add((ClientSubscriptionMaster)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public CopyOnWriteArrayList<Quotation> findAllQuotations() {
		CopyOnWriteArrayList<Quotation> list =  new CopyOnWriteArrayList<Quotation>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT quotation from Quotation quotation");
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
		   list.add((Quotation)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public Long getLastQuotationId() {
		Criteria criteria = getCurrentSession().createCriteria(Quotation.class);
		criteria.addOrder(Order.desc("quotation_id"));
		criteria.setMaxResults(1);
		Quotation quot =  (Quotation)criteria.uniqueResult();
		if(quot!=null)
		{
		return quot.getQuotation_id()+(long)1;
		}
		else
		{
			return (long)1;
		}
	}

	@Override
	public String deleteService(long serviceid) {
		
			Session session = getCurrentSession();
			Query query = session.createSQLQuery("delete from quotation_details where service_id=:serviceid");
			query.setParameter("serviceid", serviceid);
			String msg="";
			try{
				query.executeUpdate();
				msg= "Service Deleted successfully";
			}
			catch(Exception e){
				msg= "You can't delete Service";
			}
			return msg;
			
		
	}

	@Override
	public List<QuotationDetails> findAllTransactioimportDetailsList() {
		Session session = getCurrentSession();
		Query subquery = session.createQuery("select id FROM Service WHERE service_name = :service_name");
		subquery.setParameter("service_name", "Data Migration");
		
		if(subquery.list()==null || subquery.list().isEmpty())
		{
			return null;
		}
		else
		{
			Long service_id=(Long) subquery.list().get(0);
			Query query = session.createQuery("from QuotationDetails where service_id.id=:serviceId");
			query.setParameter("serviceId", service_id);
			List<QuotationDetails> details= query.list();
			return details;				
		}
	}
	@Override
	public List<QuotationDetails> findAllTransactionimportDetailsUser(String email, Long quote_id) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query subquery = session.createQuery("select id FROM Service WHERE service_name = :service_name");
		subquery.setParameter("service_name", "Data Migration");
		
		if(subquery.list()==null || subquery.list().isEmpty())
		{
			
			return null;
		}
		else
		{
		
			
			Long service_id=(Long) subquery.list().get(0);
			
			Query query = session.createQuery("from QuotationDetails where service_id.id=:serviceId and service_status=:status and quotation_id.quotation_id=:quote_id");
			query.setParameter("serviceId", service_id);
			query.setParameter("status", true);
			query.setParameter("quote_id", quote_id);
			List<QuotationDetails> details= query.list();
			return details;				
		}	
	}

}

