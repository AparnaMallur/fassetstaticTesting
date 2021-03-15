package com.fasset.dao;

import java.util.ArrayList;
import java.util.HashSet;
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
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IManualJournalVoucherDAO;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Contra;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PayrollEmployeeDetails;
import com.fasset.entities.PayrollSubledgerDetails;
import com.fasset.exceptions.MyWebException;


@Repository
public class ManualJournalVoucherDAOImpl extends AbstractHibernateDao<ManualJournalVoucher> implements IManualJournalVoucherDAO{
	@Autowired
	private IOpeningBalancesDAO balanceDao;
	

	@Override
	public ManualJournalVoucher isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
	/*
	 * @Override public CopyOnWriteArrayList<PayrollAutoJV>
	 * findAllPayrollJournalVoucherReletedToCompany(Long company_id) {
	 * 
	 * CopyOnWriteArrayList<PayrollAutoJV> list = new
	 * CopyOnWriteArrayList<PayrollAutoJV>(); Session session = getCurrentSession();
	 * Query query =
	 * session.createQuery("SELECT journal from PayrollAutoJV journal "
	 * +"WHERE journal.company.company_id=:company_id  ORDER by journal.date DESC,journal.payroll_id DESC"
	 * ); query.setMaxResults(500); query.setParameter("company_id", company_id);
	 * 
	 * ScrollableResults scrollableResults =
	 * query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY); while
	 * (scrollableResults.next()) {
	 * list.add((PayrollAutoJV)scrollableResults.get()[0]);
	 * session.evict(scrollableResults.get()[0]); } return list;
	 * 
	 * }
	 */

	@Override
	public ManualJournalVoucher findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(ManualJournalVoucher.class);
		criteria.add(Restrictions.eq("journal_id", id));
		criteria.setFetchMode("mjvdetails", FetchMode.JOIN);
		return (ManualJournalVoucher) criteria.uniqueResult();
	}
	
	@Override
	public ManualJVDetails findOnemjvdetail(Long id){
		Criteria criteria = getCurrentSession().createCriteria(ManualJVDetails.class);
		criteria.add(Restrictions.eq("detailjv_id", id));
		criteria.setFetchMode("manualjournalvoucher", FetchMode.JOIN);
		return (ManualJVDetails) criteria.uniqueResult();
	}
	
	
	
	@Override
	public void create(ManualJournalVoucher entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		
	}

	@Override
	public void update(ManualJournalVoucher entity) throws MyWebException {
		Session session = getCurrentSession();
		session.update(entity);
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update ManualJournalVoucher set entry_status=:entry_status where journal_id =:id");
		query.setParameter("id", entityId);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		String msg="";
		try{
			query.executeUpdate();
			msg= "Manual Journal Voucher Deleted Successfully";
		}
		catch(Exception e){
			msg= "You can't delete manual journal voucher";
		
		}
		return msg;
	}
	
	
	@Override
	public String deleteByMJVIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from ManualJVDetails where detailjv_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "Subledger deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete subledger";
			
		}
		return msg;
	}

	@Override
	public Long saveMJV(ManualJournalVoucher entity) {
		Session session = getCurrentSession();
		Long id = (long)session.save(entity);
		return id;
	}

	@Override
	public List<ManualJVDetails> getAllMJVDetails(Long entityId) {
		/*Session session = getCurrentSession();
		Query query = session.createQuery("from ManualJVDetails where detailjv_id=:entryId");
		query.setParameter("entryId", entityId);
		return (Set<ManualJVDetails>) query.list();*/
		System.out.println("entityId "+entityId);
		List<ManualJVDetails> mjvList = new ArrayList<>();
		Criteria criteria = getCurrentSession().createCriteria(ManualJVDetails.class);
		criteria.add(Restrictions.eq("manualjournalvoucher.journal_id", entityId));
	
		mjvList= criteria.list();
		return mjvList;
		
	}
	
	
	@Override
	public int getCountByDate(Long companyId, String range, LocalDate date) {
		Session session = getCurrentSession();
		String[] datestring = date.toString().split("-");
		String moth = datestring[1];
		Integer countOfMonth = Integer.parseInt(moth);
		Criteria criteria = session.createCriteria(ManualJournalVoucher.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.sqlRestriction("MONTH(this_.date) = "+countOfMonth));
		criteria.addOrder(Order.desc("journal_id"));
		criteria.setMaxResults(1);
		ManualJournalVoucher entry = (ManualJournalVoucher) criteria.uniqueResult();
		Integer i= 0;
		if(entry!=null)
		{
			
			String vocher_no = entry.getVoucher_no().trim();
	    	String[] vochers = vocher_no.split("/");
			String vocher = vochers[vochers.length-1];
			i = Integer.parseInt(vocher.trim());
		}
		else
		{
			i=0;
			
		}
		return i;
		/*Query query = session.createSQLQuery("select count(transaction_id)from contra where company_id =:company_id and date ='" + date + "'");
		query.setParameter("company_id", companyId);
		System.out.println(query.list());
		if (query.list() == null || query.list().isEmpty()) {
			return 0;
		} else {
			System.out.println(query.list().get(0));
			if (query.list().get(0) == null)
				return 0;
			else {
				Integer vid = ((BigInteger) query.list().get(0)).intValue();
				System.out.println(vid);
				return vid;
			}
		}*/
	}

	@Override
	public CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany() {
		CopyOnWriteArrayList<ManualJournalVoucher> list =  new CopyOnWriteArrayList<ManualJournalVoucher>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT journal from ManualJournalVoucher journal "
				+"WHERE entry_status!=:entry_status ORDER by journal.date DESC,journal.journal_id DESC");
		query.setMaxResults(500);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((ManualJournalVoucher)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
	}

	@Override
	public void saveOnemjvdetail(ManualJVDetails mjd) {
		Session session = getCurrentSession();
		session.save(mjd);
	}


	@Override
	public CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany(Long company_id) {
		CopyOnWriteArrayList<ManualJournalVoucher> list =  new CopyOnWriteArrayList<ManualJournalVoucher>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT journal from ManualJournalVoucher journal "
				+"WHERE journal.company.company_id=:company_id and entry_status!=:entry_status ORDER by journal.date DESC,journal.journal_id DESC");
		query.setMaxResults(500);
		query.setParameter("company_id", company_id);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((ManualJournalVoucher)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
	}

	@Override
	public CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate) {
		
		CopyOnWriteArrayList<ManualJournalVoucher> list =  new CopyOnWriteArrayList<ManualJournalVoucher>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT journal from ManualJournalVoucher journal LEFT JOIN FETCH journal.mjvdetails mjvdetails "
				+"WHERE journal.date>=:fromDate and journal.date<=:toDate and journal.company.company_id=:company_id and journal.entry_status!=:entry_status");
		
		query.setParameter("company_id", company_id);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((ManualJournalVoucher)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
		
	}
	
	@Override
	public CopyOnWriteArrayList<PayrollAutoJV> findAllPayrollJournalVoucherReletedToCompany(Long company_id,
			LocalDate fromDate, LocalDate toDate) {
		
		CopyOnWriteArrayList<PayrollAutoJV> list =  new CopyOnWriteArrayList<PayrollAutoJV>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT journal from PayrollAutoJV journal LEFT JOIN FETCH journal.payrollSubledgerDetails payrollSubledgerDetails "
				+"WHERE journal.date>=:fromDate and journal.date<=:toDate and journal.company.company_id=:company_id");
		
		query.setParameter("company_id", company_id);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((PayrollAutoJV)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
	}

	@Override
	public CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationJournalVoucherReletedToCompany(Long company_id,
			LocalDate fromDate, LocalDate toDate) {
		CopyOnWriteArrayList<DepreciationAutoJV> list =  new CopyOnWriteArrayList<DepreciationAutoJV>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT journal from DepreciationAutoJV journal LEFT JOIN FETCH journal.depriciationSubledgerDetails depriciationSubledgerDetails "
				+"WHERE journal.date>=:fromDate and journal.date<=:toDate and journal.company.company_id=:company_id");
		
		query.setParameter("company_id", company_id);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((DepreciationAutoJV)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
	}

	@Override
	public CopyOnWriteArrayList<GSTAutoJV> findAllGstJournalVoucherReletedToCompany(Long company_id, LocalDate fromDate,
			LocalDate toDate) {
		CopyOnWriteArrayList<GSTAutoJV> list =  new CopyOnWriteArrayList<GSTAutoJV>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT journal from GSTAutoJV journal "
				+"WHERE journal.created_date>=:fromDate and journal.created_date<=:toDate and journal.company.company_id=:company_id");
	
		query.setParameter("company_id", company_id);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((GSTAutoJV)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
	}

	

	


}
