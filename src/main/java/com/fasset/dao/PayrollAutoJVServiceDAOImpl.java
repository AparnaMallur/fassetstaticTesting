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
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.IPayrollAutoJVServiceDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.DebitNote;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.Employee;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PayrollEmployeeDetails;
import com.fasset.entities.PayrollSubledgerDetails;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.entities.VoucherSeries;
import com.fasset.entities.YearEndJV;
import com.fasset.entities.YearEndJvSubledgerDetails;
import com.fasset.exceptions.MyWebException;


@Repository
@Transactional
public class PayrollAutoJVServiceDAOImpl extends AbstractHibernateDao<PayrollAutoJV> implements IPayrollAutoJVServiceDAO{
	@Autowired
	private IOpeningBalancesDAO balanceDao; 
	
	@Autowired
	private IAccountingYearDAO dao;
	
	@Override
	public PayrollAutoJV findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(PayrollAutoJV.class);
		criteria.add(Restrictions.eq("payroll_id", id));
		criteria.setFetchMode("payrollSubledgerDetails", FetchMode.JOIN);
		criteria.setFetchMode("payrollEmployeeDetails", FetchMode.JOIN);
		
		return (PayrollAutoJV) criteria.uniqueResult();
		
		
		/*
		 * Criteria criteria =
		 * getCurrentSession().createCriteria(ManualJournalVoucher.class);
		 * criteria.add(Restrictions.eq("journal_id", id));
		 * criteria.setFetchMode("mjvdetails", FetchMode.JOIN); return
		 * (ManualJournalVoucher) criteria.uniqueResult();
		 */
		
	}

	@Override
	public PayrollAutoJV findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PayrollAutoJV findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PayrollAutoJV> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(PayrollAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PayrollAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public void merge(PayrollAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(PayrollAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByPayrollSubledgerDetails(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from PayrollSubledgerDetails   where subledgerdetails_id =:id");
		query.setParameter("id", entityId);
		query.executeUpdate();
	}

	
	@Override
	public void deletePayrollEmployeeId(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from PayrollEmployeeDetails   where employeeDetails_id =:id");
		query.setParameter("id", entityId);
		query.executeUpdate();
		
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
	public boolean validatePreTransaction(PayrollAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(PayrollAutoJV entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PayrollAutoJV isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long savePayrollAutoJv(PayrollAutoJV autojv) {
		Session session = getCurrentSession();
		Long id= (Long) session.save(autojv);
		session.clear();
		return id;
	
	}

	@Override
	public List<PayrollAutoJV> getAllPayrollJVReletedToCompany(Long company_id) {
		
		List<PayrollAutoJV> list =  new ArrayList<PayrollAutoJV>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT payroll from PayrollAutoJV payroll WHERE payroll.company.company_id =:company_id");
			query.setParameter("company_id", company_id);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((PayrollAutoJV)scrollableResults.get()[0]);
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
	
	@Override
	public PayrollAutoJV getById(Long payroll_id)
	{
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(PayrollAutoJV.class);
		criteria.add(Restrictions.eq("payroll_id", payroll_id));
		return (PayrollAutoJV) criteria.uniqueResult();
		
	}
	
	public int getCountByDateYearAuto(Long companyId, String range, LocalDate date)
	{
		Session session = getCurrentSession();
		String[] datestring = date.toString().split("-");
		String moth = datestring[1];
		Integer countOfMonth = Integer.parseInt(moth);
		Criteria criteria = session.createCriteria(YearEndJV.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.sqlRestriction("MONTH(this_.date) = "+countOfMonth));
		criteria.addOrder(Order.desc("year_end_jVId"));
		criteria.setMaxResults(1);
		YearEndJV entry = (YearEndJV) criteria.uniqueResult();
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

	
	public int getVoucherS(Long companyId, String range, LocalDate date)
	{
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(VoucherSeries.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("voucher_date", date));
		criteria.addOrder(Order.desc("voucher_id"));
		criteria.setMaxResults(1);
		VoucherSeries entry = (VoucherSeries) criteria.uniqueResult();
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
	}

	
	@Override
	public int getCountByDate(Long companyId, String range, LocalDate date) {
		Session session = getCurrentSession();
		String[] datestring = date.toString().split("-");
		String moth = datestring[1];
		Integer countOfMonth = Integer.parseInt(moth);
		Criteria criteria = session.createCriteria(PayrollAutoJV.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.sqlRestriction("MONTH(this_.date) = "+countOfMonth));
		criteria.addOrder(Order.desc("payroll_id"));
		criteria.setMaxResults(1);
		PayrollAutoJV entry = (PayrollAutoJV) criteria.uniqueResult();
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
	public int getCountByDategst(Long companyId, String range, LocalDate date) {
		Session session = getCurrentSession();
		String[] datestring = date.toString().split("-");
		String moth = datestring[1];
		Integer countOfMonth = Integer.parseInt(moth);
		Criteria criteria = session.createCriteria(GSTAutoJV.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.sqlRestriction("MONTH(this_.created_date) = "+countOfMonth));
		criteria.addOrder(Order.desc("gst_id"));
		criteria.setMaxResults(1);
		GSTAutoJV entry = (GSTAutoJV) criteria.uniqueResult();
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
	public Long saveGstAutoJv(GSTAutoJV autojv) {
		Session session = getCurrentSession();
		Long id= (Long) session.save(autojv);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public GSTAutoJV findGstAutojv(Long gstId) {
		Criteria criteria = getCurrentSession().createCriteria(GSTAutoJV.class);
		criteria.add(Restrictions.eq("gst_id", gstId));
		return (GSTAutoJV) criteria.uniqueResult();
	}
	
	@Override
	public String deletePayroll(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from PayrollAutoJV where payroll_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
		balanceDao.deleteOpeningBalance(null, null,null, null, null, null,null,null, entityId,null,null,null);
		query.executeUpdate();
		msg= "Payroll deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete Payroll";
			
		}
		return msg;
	}
	

	@Override
	public PayrollAutoJV findOneView(long payroll_id) {
		
		Criteria criteria = getCurrentSession().createCriteria(PayrollAutoJV.class);
		criteria.add(Restrictions.eq("payroll_id", payroll_id));
		criteria.setFetchMode("payrollSubledgerDetails", FetchMode.JOIN);
		criteria.setFetchMode("payrollEmployeeDetails", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		
		return (PayrollAutoJV) criteria.uniqueResult();
	
	
}
	
	
	@Transactional
    @SuppressWarnings("unchecked")
    @Override
    public List<PayrollEmployeeDetails> findAllPayrollEmployeeList(Long entryId) {
    Session session = getCurrentSession();
    //SELECT sales from SalesEntry sales LEFT JOIN FETCH sales.company company "
    Query query = session.createQuery("select empdetails from PayrollEmployeeDetails empdetails  LEFT JOIN FETCH empdetails.employee employee "
            + " where empdetails.payrollAutoJV.payroll_id=:entryId order by empdetails.name" );
    query.setParameter("entryId", entryId);
    return query.list();
    }

	@Override
	public List<GSTAutoJV> getAllGstAutoJVReletedToCompany(Long company_id) {
		
		List<GSTAutoJV> list =  new ArrayList<GSTAutoJV>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT gstautojv from GSTAutoJV gstautojv WHERE gstautojv.company.company_id =:company_id");
			query.setParameter("company_id", company_id);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((GSTAutoJV)scrollableResults.get()[0]);
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

	@Override
	public String deleteGSTAutoJVByIdValue(Long gstauto_id) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from GSTAutoJV where gst_id =:id");
		query.setParameter("id", gstauto_id);
		String msg="";
		try{
		balanceDao.deleteOpeningBalance(null, null,null, null, null, null,null,null, null,gstauto_id,null,null);
		query.executeUpdate();
		msg= "GST auto jv deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete GST auto jv";
			
		}
		return msg;
	}

	@Override
	public GSTAutoJV getGstAutoJvById(Long id) {
		Criteria criteria = getCurrentSession().createCriteria(GSTAutoJV.class);
		criteria.add(Restrictions.eq("gst_id", id));
		return (GSTAutoJV) criteria.uniqueResult();
	}

	@Override
	public PayrollAutoJV findOneWithAll(Long payroll_id) {
		Criteria criteria = getCurrentSession().createCriteria(PayrollAutoJV.class);
		criteria.add(Restrictions.eq("payroll_id", payroll_id));
		criteria.setFetchMode("payrollEmployeeDetails", FetchMode.JOIN);
		criteria.setFetchMode("payrollSubledgerDetails", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("accounting_year_id", FetchMode.JOIN);
		return (PayrollAutoJV) criteria.uniqueResult();
	}

	@Override
	public PayrollEmployeeDetails editEmployeeForPayroll(Long entryId) {
		Criteria criteria = getCurrentSession().createCriteria(PayrollEmployeeDetails.class);
		criteria.add(Restrictions.eq("employeeDetails_id", entryId));
		
		return (PayrollEmployeeDetails) criteria.uniqueResult();
	}

	@Override
	public void updateSalesEntry(PayrollAutoJV entry) {
		Session session = getCurrentSession();
		session.merge(entry);
		session.flush();
	    session.clear();
		
	}
	
	@Override
	public void deleteDetails(Long entityId) {
		Session session = getCurrentSession();
		Query query1 = session.createQuery("delete from PayrollSubledgerDetails where payrollAutoJV.payroll_id =:payroll_id");
		query1.setParameter("payroll_id", entityId);
		
		
		Query query2= session.createQuery("delete from PayrollEmployeeDetails where payrollAutoJV.payroll_id =:payroll_id");
		query2.setParameter("payroll_id", entityId);
		
		query1.executeUpdate();
		query2.executeUpdate();
	
	}

	@Override
	@Transactional
	public Long saveYearEndAutoJv(YearEndJV autojv) {
		Session session = getCurrentSession();
		Long id= (Long) session.save(autojv);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	@Transactional
	public YearEndJV getYearEndJVById(Long id) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(YearEndJV.class);
		criteria.add(Restrictions.eq("year_end_jVId", id));
		return (YearEndJV) criteria.uniqueResult();
	}
	
	@Override
	public CopyOnWriteArrayList<YearEndJV> findAllYearEndVoucherReletedToCompany() {
		CopyOnWriteArrayList<YearEndJV> list =  new CopyOnWriteArrayList<YearEndJV>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT yearendjv from YearEndJV yearendjv   ORDER by yearendjv.date DESC,yearendjv.year_end_jVId DESC");
		query.setMaxResults(500);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((YearEndJV)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
	}

	
	@Override
	public List<YearEndJvSubledgerDetails> getAllYearEndJvSubledgerDetails(Long entityId) {
		/*Session session = getCurrentSession();
		Query query = session.createQuery("from ManualJVDetails where detailjv_id=:entryId");
		query.setParameter("entryId", entityId);
		return (Set<ManualJVDetails>) query.list();*/
		List<YearEndJvSubledgerDetails> yearjvList = new ArrayList<>();
		Criteria criteria = getCurrentSession().createCriteria(YearEndJvSubledgerDetails.class);
		criteria.add(Restrictions.eq("yearEndJV.year_end_jVId", entityId));
	
		yearjvList= criteria.list();
		return yearjvList;
		
	}
	@Override
	public String deleteByYearEndIdValue(Long entityId) {
		
		
		Session session = getCurrentSession();
		Query query1 = session.createQuery("delete from YearEndJvSubledgerDetails where year_end_Id =:year_end_jVId");
		query1.setParameter("year_end_jVId", entityId);
		query1.executeUpdate();

		balanceDao.deleteOpeningBalance(null,null, null, null, null, null,null,null, null,null,null,entityId);
		Query query = session.createQuery("delete from  YearEndJV   where year_end_jVId =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
			query.executeUpdate();
			msg= "YearEnd Journal Voucher Deleted Successfully";
		}
		catch(Exception e){
			msg= "You can't delete YearEnd journal voucher";
		
		}
		return msg;
		
		
	}

	@Override
	public int getCountByDateAutoJV(Long companyId, String range, LocalDate date) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<PayrollSubledgerDetails> findAllPayrollSubledgerList(Long entryId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
