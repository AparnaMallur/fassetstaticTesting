
package com.fasset.dao;




import java.util.ArrayList;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IDepreciationAUtoJVDAO;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.DepreciationSubledgerDetails;
import com.fasset.entities.Ledger;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ProductInformation;
@Transactional
@Repository
public class DepreciationAUtoJVDAOImpl extends AbstractHibernateDao<DepreciationAutoJV>
		implements IDepreciationAUtoJVDAO {
	@Autowired
	private IOpeningBalancesDAO balanceDao;

	@Override public DepreciationAutoJV isExists(String name) {
		return null; 
		
		  
	}

	// d,ledger_master l where d.ledger_id=l.depreciation_id

	@SuppressWarnings("unchecked")

	@Override
	public List<Ledger> findAllSubledgerForDepreciation() {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select * from depreciation_autoJV order by d.depreciation_id desc");
		return query.list();
	}
	
	
	
	@Override
	public Long saveDepreciationAutoJV(DepreciationAutoJV entity) {
		
		/*
		 * List<DepreciationSubledgerDetails> depriciationList=new
		 * ArrayList<DepreciationSubledgerDetails>();
		 * depriciationList=entity.getDepriciationSubledgerDetailsList();
		 */
		
		Session session = getCurrentSession();
		Long id = (long)session.save(entity);
		return id;
	}
	
	@Override
	public void saveOnedjvdetail(DepreciationSubledgerDetails djv) {
		Session session = getCurrentSession();
		session.save(djv);
	}
	
	
	@Override
	public DepreciationAutoJV findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(DepreciationAutoJV.class);
		criteria.add(Restrictions.eq("depreciation_id", id));
		criteria.setFetchMode("depriciationSubledgerDetails", FetchMode.JOIN);
		return (DepreciationAutoJV) criteria.uniqueResult();
	}
	
	@Override
	public void update(DepreciationAutoJV entity) throws MyWebException {
		Session session = getCurrentSession();
		session.update(entity);
	}
	
	@Override
	public CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationAutoJVReletedToCompany() {
		CopyOnWriteArrayList<DepreciationAutoJV> list =  new CopyOnWriteArrayList<DepreciationAutoJV>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT depreciation from DepreciationAutoJV depreciation  ORDER by depreciation.date DESC,depreciation.depreciation_id DESC");
		query.setMaxResults(500);
		
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((DepreciationAutoJV)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
	}
	
	@Override
	public CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationAutoJVReletedToCompany(Long company_id) {
		
		CopyOnWriteArrayList<DepreciationAutoJV> list =  new CopyOnWriteArrayList<DepreciationAutoJV>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT depreciation from DepreciationAutoJV depreciation "
				+"WHERE depreciation.company.company_id=:company_id  ORDER by depreciation.date DESC,depreciation.depreciation_id DESC");
		query.setMaxResults(500);
		query.setParameter("company_id", company_id);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((DepreciationAutoJV)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
		
	}
	
	public List<DepreciationSubledgerDetails> getAllDepreciationSubledgerDetails(Long entityId){
		/*Session session = getCurrentSession();
		Query query = session.createQuery("from ManualJVDetails where detailjv_id=:entryId");
		query.setParameter("entryId", entityId);
		return (Set<ManualJVDetails>) query.list();*/
		List<DepreciationSubledgerDetails> depriList = new ArrayList<>();
		Criteria criteria = getCurrentSession().createCriteria(DepreciationSubledgerDetails.class);
		criteria.add(Restrictions.eq("depreciationAutoJV.depreciation_id", entityId));
	
		depriList= criteria.list();
		return depriList;
		
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query1 = session.createQuery("delete from DepreciationSubledgerDetails where depreciation_id =:depreciation_id");
		query1.setParameter("depreciation_id", entityId);
		query1.executeUpdate();

		balanceDao.deleteOpeningBalance(null,null, null, null, null, null,null,null, null,null,entityId,null);
		Query query = session.createQuery("delete from  DepreciationAutoJV   where depreciation_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
			query.executeUpdate();
			msg= "Depreciation Journal Voucher Deleted Successfully";
		}
		catch(Exception e){
			msg= "You can't delete Depreciation journal voucher";
		
		}
		return msg;
	}

	public int getCountByDate(Long companyId, String range, LocalDate date)
	{
		Session session = getCurrentSession();
		String[] datestring = date.toString().split("-");
		String moth = datestring[1];
		Integer countOfMonth = Integer.parseInt(moth);
		Criteria criteria = session.createCriteria(DepreciationAutoJV.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.sqlRestriction("MONTH(this_.date) = "+countOfMonth));
		criteria.addOrder(Order.desc("depreciation_id"));
		criteria.setMaxResults(1);
		DepreciationAutoJV entry = (DepreciationAutoJV) criteria.uniqueResult();
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
	}

	@Override
	public String deleteDetails(Long entityId) {
		Session session = getCurrentSession();
		Query query1 = session.createQuery("delete from DepreciationSubledgerDetails where depreciation_id =:depreciation_id");
		query1.setParameter("depreciation_id", entityId);
		query1.executeUpdate();
		return null;
	}

	

}