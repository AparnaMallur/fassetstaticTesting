package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IGstTaxMasterDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Chapter;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Schedule;
import com.fasset.entities.State;
import com.fasset.exceptions.MyWebException;

@Repository
@Transactional
public class GstTaxMasterDAOImpl extends AbstractHibernateDao<GstTaxMaster> implements IGstTaxMasterDAO{

	@Override
	public GstTaxMaster findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(GstTaxMaster.class);
		criteria.add(Restrictions.eq("tax_id", id));
		return (GstTaxMaster) criteria.uniqueResult();
	}

	@Override
	public GstTaxMaster findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GstTaxMaster findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GstTaxMaster> findAll() {
		List<GstTaxMaster> list =  new ArrayList<GstTaxMaster>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT tax from GstTaxMaster tax where tax.status=:status order by tax.tax_id desc");
			query.setParameter("status", true);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((GstTaxMaster)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
              session.clear();
			 
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		 
		 return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GstTaxMaster> findAllsearch(String term) {		
		Session session = getCurrentSession();
		LocalDate cdate=new LocalDate();		
		Query query = session.createQuery("from GstTaxMaster where status=:status and hsc_sac_code LIKE :term and start_date <= :cdate and (end_date>=:cdate or end_date IS NULL) order by tax_id desc");
		query.setParameter("status", true);
		query.setParameter("cdate", cdate);
		query.setParameter("term",  term + "%");		
		return query.list();
	}

	@Override
	public void create(GstTaxMaster entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDate());
		session.save(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public void update(GstTaxMaster entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setUpdated_date(new LocalDate());
		session.merge(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public void merge(GstTaxMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(GstTaxMaster entity) throws MyWebException {
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
	public boolean validatePreTransaction(GstTaxMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(GstTaxMaster entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GstTaxMaster isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from GstTaxMaster where tax_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "GST Record Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete GST Record";
			
		}
		return msg;	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GstTaxMaster> findAllListing() {
		
		Session session = getCurrentSession();
		/*Query query = session.createSQLQuery("select tax_id, hsc_sac_code,description,start_date,end_date,status from gst_tax_master order by tax_id desc");
		return query.list();*/
		List<GstTaxMaster> list =  new ArrayList<GstTaxMaster>();
		Query query = session.createQuery("SELECT tax from GstTaxMaster tax");
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((GstTaxMaster)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	
	}

	@Override
	public GstTaxMaster getHSNbyDate(LocalDate date, String hsn) {
		GstTaxMaster gst = new GstTaxMaster();
		Session session = getCurrentSession();
		
		Query query = session.createQuery("from GstTaxMaster where status=:status and hsc_sac_code =:hsn and start_date <= :cdate and (end_date>=:cdate or end_date IS NULL) order by tax_id desc");
		query.setParameter("status", true);
		query.setParameter("cdate", date);
		query.setParameter("hsn",  hsn );	
		query.setMaxResults(1);
		gst=(GstTaxMaster) query.uniqueResult();
	
		return gst;	
	}

	@Override
	public GstTaxMaster getGSTbyHSN(String hsn,LocalDate startdate) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from GstTaxMaster where hsc_sac_code = :hsn and start_date <= :cdate and end_date IS NULL order by tax_id desc");
		query.setParameter("hsn",  hsn );
		query.setParameter("cdate", startdate);
		query.setMaxResults(1);
		GstTaxMaster gst =(GstTaxMaster) query.uniqueResult();
		return gst;
	}

	@Override
	public Chapter getChapter(Integer chapterNo) {
		Criteria criteria = getCurrentSession().createCriteria(Chapter.class);
		criteria.add(Restrictions.eq("chapterNo", chapterNo));
		return (Chapter) criteria.uniqueResult();
	}

	@Override
	public Schedule getSchedule(String scheduleName) {
		Criteria criteria = getCurrentSession().createCriteria(Schedule.class);
		criteria.add(Restrictions.eq("scheduleName", scheduleName));
		return (Schedule) criteria.uniqueResult();
	}

	@Override
	public GstTaxMaster getGstforBackdatedEntry(String hsn,LocalDate startdate) {
		GstTaxMaster gst = new GstTaxMaster();
		Session session = getCurrentSession();
		Query query = session.createQuery("from GstTaxMaster where status=:status and start_date>=:start_date and hsc_sac_code=:hsc_sac_code order by start_date asc");
		query.setParameter("status", true);
		query.setParameter("hsc_sac_code", hsn);
		query.setParameter("start_date", startdate);
		query.setMaxResults(1);
		gst=(GstTaxMaster) query.uniqueResult();
		return gst;
	}

	@Override
	public Integer checkExcelRowNo(String rowno) {
		
		Criteria criteria = getCurrentSession().createCriteria(GstTaxMaster.class);
		criteria.add(Restrictions.eq("excel_row_no", rowno));
		if((GstTaxMaster) criteria.uniqueResult()!=null)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public List<Chapter> findAllChapters() {
		List<Chapter> list =  new ArrayList<Chapter>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT chapter from Chapter chapter");
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Chapter)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public List<Schedule> findAllSchedules() {
		// TODO Auto-generated method stub
		List<Schedule> list =  new ArrayList<Schedule>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT schedule from Schedule schedule");
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Schedule)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public Chapter getChapter(Long chapterid) {
		Criteria criteria = getCurrentSession().createCriteria(Chapter.class);
		criteria.add(Restrictions.eq("chapter_id", chapterid));
		return (Chapter) criteria.uniqueResult();
	}

	@Override
	public Schedule getSchedule(Long scheduleid) {
		Criteria criteria = getCurrentSession().createCriteria(Schedule.class);
		criteria.add(Restrictions.eq("schedule_id", scheduleid));
		return (Schedule) criteria.uniqueResult();
	}

	@Override
	public List<GstTaxMaster> getHSNSACNoForNonInventory(String term, LocalDate date) {
		Session session = getCurrentSession();		
		Query query = session.createQuery("from GstTaxMaster where status=:status and hsc_sac_code LIKE :term and start_date <= :cdate and (end_date>=:cdate or end_date IS NULL) order by tax_id desc");
		query.setParameter("status", true);
		query.setParameter("cdate", date);
		query.setParameter("term",  term + "%");		
		return query.list();
	}

}