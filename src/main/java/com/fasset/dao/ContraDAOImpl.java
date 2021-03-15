package com.fasset.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IContraDAO;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.entities.Contra;
import com.fasset.entities.DebitNote;
import com.fasset.entities.PurchaseEntry;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.exceptions.MyWebException;
import com.fasset.entities.SubLedger;

@Transactional
@Repository
public class ContraDAOImpl extends AbstractHibernateDao<Contra> implements IContraDAO {

	@Autowired
	private IOpeningBalancesDAO balanceDao;
	
	@Override
	public Long saveContradao(Contra contra) {
		Session session = getCurrentSession();
		Long id= (Long) session.save(contra);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public Contra findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Contra.class);
		criteria.add(Restrictions.eq("transaction_id", id));
		return (Contra) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Contra> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(Contra.class);
		criteria.addOrder(Order.desc("transaction_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Contra> findAllContraEntry(Long CompanyId, Boolean flag) {
		
		
		List<Contra> list =  new ArrayList<Contra>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT contra from Contra contra LEFT JOIN FETCH contra.company company "
				+"LEFT JOIN FETCH contra.withdraw_from withdraw_from " +"LEFT JOIN FETCH contra.deposite_to deposite_to "
				+"WHERE contra.company.company_id =:company_id and contra.flag =:flag ORDER by contra.date DESC,contra.transaction_id DESC");
		query.setParameter("company_id", CompanyId);
	//	query.setParameter("yearID",yearID); //  and contra.accounting_year_id.year_id=:yearID
		query.setParameter("flag", flag);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Contra)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		
		  return list;
	}


	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<Contra> findAllContraEntry(Long CompanyId, Boolean
	 * flag,Long yearID) {
	 * 
	 * 
	 * List<Contra> list = new ArrayList<Contra>(); Session session =
	 * getCurrentSession(); Query query = session.
	 * createQuery("SELECT contra from Contra contra LEFT JOIN FETCH contra.company company "
	 * +"LEFT JOIN FETCH contra.withdraw_from withdraw_from "
	 * +"LEFT JOIN FETCH contra.deposite_to deposite_to "
	 * +"WHERE contra.company.company_id =:company_id and contra.flag =:flag and contra.accounting_year_id.year_id=:yearID ORDER by contra.date DESC,contra.transaction_id DESC"
	 * ); query.setParameter("company_id", CompanyId);
	 * query.setParameter("yearID",yearID); query.setParameter("flag", flag);
	 * ScrollableResults scrollableResults =
	 * query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY); while
	 * (scrollableResults.next()) { list.add((Contra)scrollableResults.get()[0]);
	 * session.evict(scrollableResults.get()[0]); } session.clear();
	 * 
	 * return list; }
	 */
	@Override
	public void create(Contra entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
		session.clear();
	}

	@Override
	public void update(Contra entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
		session.clear();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from Contra where transaction_id =:id");
		query.setParameter("id", entityId);
		String msg = "";
		try {
			balanceDao.deleteOpeningBalance(null, null,null, null, null, null,entityId,null, null,null,null,null);
			query.executeUpdate();
			msg = "Contra Deleted successfully";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "You can't delete Contra";
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubLedger> getsubledger() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from SubLedger where ledger.accsubgroup.accountGroup.group_name=:group_name");
		query.setParameter("group_name", "Bank");
		List<SubLedger> result = (List<SubLedger>) query.list();
		return result;
	}

	@Override
	public Contra isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contra findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contra findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void merge(Contra entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Contra entity) throws MyWebException {
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
	public boolean validatePreTransaction(Contra entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(Contra entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer getCountByYear(Long yearId, Long companyId, String range) {
		/*
		 * Criteria criteria = getCurrentSession().createCriteria(Contra.class);
		 * criteria.add(Restrictions.eq("company.company_id", companyId));
		 * criteria.add(Restrictions.eq("accounting_year_id.year_id", yearId));
		 * criteria.setProjection(Projections.rowCount()); return
		 * Integer.parseInt(criteria.uniqueResult().toString());
		 */
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select count(transaction_id)from contra where company_id =:company_id and accounting_year_id =:year_id and voucher_no LIKE :term");
		query.setParameter("company_id", companyId);
		query.setParameter("year_id", yearId);
		query.setParameter("term", range + "%");
		
		if (query.list() == null || query.list().isEmpty()) {
			return 0;
		} else {
		
			if (query.list().get(0) == null)
				return 0;
			else {
				Integer vid = ((BigInteger) query.list().get(0)).intValue();
				System.out.println(vid);
				return vid;
			}
		}
	}

	@Override
	public Contra findOneWithAll(Long conId) {
		Criteria criteria = getCurrentSession().createCriteria(Contra.class);
		criteria.add(Restrictions.eq("transaction_id", conId));
		criteria.setFetchMode("withdraw_from", FetchMode.JOIN);
		criteria.setFetchMode("deposite_to", FetchMode.JOIN);
		criteria.setFetchMode("cash", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		return (Contra) criteria.uniqueResult();
	}

	@Override
	public Long saveContraThroughExcel(Contra contra) {
		Session session = getCurrentSession();
		Long id  = (Long) session.save(contra);
		session.flush();
		session.clear();
		return id ;
	}

	@Override
	public List<Contra> findAllContraEntries(Long CompanyId) {
		List<Contra> list =  new ArrayList<Contra>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT contra from Contra contra LEFT JOIN FETCH contra.company company "
				+"WHERE contra.company.company_id =:company_id and excel_voucher_no IS NOT NULL");
		query.setParameter("company_id", CompanyId);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Contra)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  Collections.sort(list, new Comparator<Contra>() {
	            public int compare(Contra pay1, Contra pay2) {
	                Long transaction_id1 = new Long(pay1.getTransaction_id());
	                Long transaction_id2 = new Long(pay2.getTransaction_id());
	                return transaction_id2.compareTo(transaction_id1);
	            }
	        });
		  return list;
	}

	@Override
	public List<Contra> getCashBookBankBookReport(LocalDate from_date, LocalDate to_date, Long companyId, Integer type) {
		/*Criteria criteria = getCurrentSession().createCriteria(Contra.class);

		criteria.add(Restrictions.ge("date", from_date));
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.desc("date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<Contra> list =  new ArrayList<Contra>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT contra from Contra contra "
				+"WHERE contra.company.company_id =:company_id and contra.flag =:flag and contra.date>=:from_date and contra.date<=:to_date order by contra.date desc");
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Contra)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		
		  return list;
	}

	@Override
	public int getCountByDate(Long companyId, String range, LocalDate date) {
		Session session = getCurrentSession();
		
		Criteria criteria = session.createCriteria(Contra.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("date", date));
		criteria.addOrder(Order.desc("transaction_id"));
		criteria.setMaxResults(1);
		Contra entry = (Contra) criteria.uniqueResult();
		Integer i= null;
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
	public List<Contra> getDayBookReport(LocalDate from_date,LocalDate to_date, Long companyId) {
	/*	Criteria criteria = getCurrentSession().createCriteria(Contra.class);
		criteria.add(Restrictions.ge("date", from_date));
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.desc("date"));
		criteria.setFetchMode("withdraw_from", FetchMode.JOIN);
		criteria.setFetchMode("deposite_to", FetchMode.JOIN);
		criteria.setFetchMode("cash", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<Contra> list =  new ArrayList<Contra>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT contra from Contra contra "
				+"WHERE contra.company.company_id =:company_id and contra.flag =:flag and contra.date>=:from_date and contra.date<=:to_date order by contra.date desc");
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Contra)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		
		  return list;
		
	}

	@Override
	public List<Contra> getContraListForLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId,Long bank_id) {
		List<Contra> list =  new ArrayList<Contra>();
		Session session = getCurrentSession();
		Query query =null;
		
		if(bank_id.equals((long)-4))
		{
			query = session.createQuery("SELECT contra from Contra contra LEFT JOIN FETCH contra.company company "
				+"LEFT JOIN FETCH contra.withdraw_from withdraw_from " +"LEFT JOIN FETCH contra.deposite_to deposite_to "
				+"WHERE contra.company.company_id =:company_id and contra.flag =:flag and contra.date>=:from_date and contra.date<=:to_date");
		}
		else
		{
		    query = session.createQuery("SELECT contra from Contra contra LEFT JOIN FETCH contra.company company "
					+"LEFT JOIN FETCH contra.withdraw_from withdraw_from " +"LEFT JOIN FETCH contra.deposite_to deposite_to "
					+"WHERE contra.company.company_id =:company_id and contra.flag =:flag and contra.date>=:from_date and contra.date<=:to_date and ((contra.deposite_to.bank_id=:bank_id) or (contra.withdraw_from.bank_id=:bank_id))");
			query.setParameter("bank_id", bank_id);
		}
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Contra)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public Contra isExcelVocherNumberExist(String vocherNo, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Contra.class);
		criteria.add(Restrictions.eq("excel_voucher_no", vocherNo));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Contra)criteria.uniqueResult();
	}

}