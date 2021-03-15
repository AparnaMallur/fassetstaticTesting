package com.fasset.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.IPaymentDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PaymentDetails;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.exceptions.MyWebException;


@Repository
@Transactional
public class PaymentDAOImpl extends AbstractHibernateDao<Payment> implements IPaymentDAO{

	@Autowired
	private IOpeningBalancesDAO balanceDao;
	
	@Override
	public Payment findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.eq("payment_id", id));
		criteria.setFetchMode("advpayment", FetchMode.JOIN);
		return (Payment) criteria.uniqueResult();
	}
	
	
	@Override
	public Payment findOneopeningBalance(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.eq("payment_id", id));
		criteria.setFetchMode("openingbalance", FetchMode.JOIN);
		return (Payment) criteria.uniqueResult();
	}
	
	
	@Override
	public Payment findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Payment findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Payment> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.setFetchMode("supplier_bill_no", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("payment_id"));
		return criteria.list();
	}
	
	@Override
	public void create(Payment entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
		session.clear();
	}
	@Override
	public Long createforbill(Payment entity) {

	Session session = getCurrentSession();
	Long id= (Long) session.save(entity);
	session.flush();
	session.clear();
	return id;
	}
	@Override
	public void update(Payment entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
		session.clear();
	}
	
	@Override
	public void merge(Payment entity) throws MyWebException {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void delete(Payment entity) throws MyWebException {
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
	public boolean validatePreTransaction(Payment entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void refresh(Payment entity) {
		// TODO Auto-generated method stub
	}
	
	
	@Override
	public Payment isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Long getCount() {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Payment> getReport(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {
		/*Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.ge("date", from_date)); 
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("company.company_id", comId));
		criteria.setFetchMode("supplier",  FetchMode.JOIN);
		criteria.setFetchMode("subLedger",  FetchMode.JOIN);
		criteria.setFetchMode("supplier_bill_no",  FetchMode.JOIN);
		criteria.setFetchMode("paymentDetails",  FetchMode.JOIN);
		return criteria.list();*/
		
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.paymentDetails paymentDetails "
				+"WHERE payment.company.company_id =:company_id and payment.flag =:flag and payment.date >=:from_date and payment.date <=:to_date and payment.entry_status !=:entry_status ORDER by payment.date DESC,payment.payment_id DESC");
		query.setParameter("company_id", comId);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Payment)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseEntry> findAllPurchaseEntryWithDate(LocalDate from_date, LocalDate to_date, Long company_id) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.ge("date", from_date)); 
		criteria.add(Restrictions.le("date", to_date));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("company.company_id", company_id));
		return criteria.list();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override 
	public List<Payment> findAllPaymentOfCompany(Long company_id,Boolean flag) {
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.company company "
				+"WHERE payment.company.company_id =:company_id and payment.flag =:flag ORDER by payment.date DESC,payment.payment_id DESC");
		query.setParameter("company_id", company_id);
	//	query.setParameter("yearID",yearID); //and payment.accountingYear.year_id=:yearID 
		query.setParameter("flag", flag);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Payment)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
		
	}
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<Payment> findAllPaymentOfCompany(Long
	 * company_id,Boolean flag,Long yearID) { List<Payment> list = new
	 * ArrayList<Payment>(); Session session = getCurrentSession(); Query query =
	 * session.
	 * createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.company company "
	 * +"WHERE payment.company.company_id =:company_id and payment.flag =:flag and payment.accountingYear.year_id=:yearID ORDER by payment.date DESC,payment.payment_id DESC"
	 * ); query.setParameter("company_id", company_id);
	 * query.setParameter("yearID",yearID); query.setParameter("flag", flag);
	 * ScrollableResults scrollableResults =
	 * query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY); while
	 * (scrollableResults.next()) { list.add((Payment)scrollableResults.get()[0]);
	 * session.evict(scrollableResults.get()[0]); } session.clear(); return list;
	 * 
	 * }
	 */

	@Override
	public String deleteByIdValue(Long entityId, long purchase_id, int status) {
		Session session = getCurrentSession();
		String msg="";
		if(purchase_id!=0)
		{			
			Query querysales = session.createQuery("update PurchaseEntry set entry_status=:entry_status where purchase_id=:purchase_id");
			querysales.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
			querysales.setParameter("purchase_id",purchase_id);
			querysales.executeUpdate();
		}
	    Query query1 = session.createSQLQuery("delete from payment_details where payment_id =:payment_id");
		query1.setParameter("payment_id", entityId);
		query1.executeUpdate();
		if(status==4)
		{
			Query query = session.createQuery("update Payment set entry_status=:entry_status,supplier_bill_no= null where payment_id =:id");
			query.setParameter("payment_id", entityId);
			query.setParameter("entry_status",status);
			try{
				query.executeUpdate();
				msg= "Payment deleted successfully";
			}
			catch(Exception e){
				msg= "You can't delete Payment";			
			}
		}
		else
		{
		balanceDao.deleteOpeningBalance(null,null, null, null, entityId, null,null,null, null,null,null,null);
		Query query = session.createQuery("delete from Payment where payment_id =:id");
		query.setParameter("id", entityId);
			try{
				query.executeUpdate();
				msg= "Payment deleted successfully";
			}
			catch(Exception e){
				msg= "You can't delete Payment";			
			}
		}
		
		return msg;
		}

	@Override
	public List<Payment> getAdvancePaymentList(List<Long> paymentIds,Long supplierId,Long companyId, Long yid) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		Criterion criterion1 = Restrictions.eq("advance_payment", true);
		Criterion criterion2 = Restrictions.eq("supplier.supplier_id",supplierId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	
		
		criteria.add(Restrictions.le("accountingYear.year_id",yid));
		criteria.add(Restrictions.eq("entry_status", MyAbstractController.ENTRY_PENDING));
		/*if(paymentIds.size() > 0){
			Criterion criterion4 = Restrictions.not(Restrictions.in("payment_id", paymentIds));
			criteria.add(Restrictions.and(criterion1,criterion2,criterion3,criterion4));
		}*/
		criteria.add(Restrictions.and(criterion1,criterion2,criterion3));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	
	@Override
	public List<Payment>  getAllOpeningBalanceAgainstPayment(Long supplierId,Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		Criterion criterion2 = Restrictions.eq("supplier.supplier_id",supplierId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	

		criteria.add(Restrictions.and(criterion1,criterion2,criterion3));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	@Override
	public List<Payment> getExpenditureByYearId(Long yeaId, Long companyId) {
	/*	Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.eq("advance_payment", false));
		criteria.add(Restrictions.eq("company.company_id",companyId));
		criteria.add(Restrictions.eq("accountingYear.year_id",yeaId));
		criteria.setFetchMode("supplier_bill_no", FetchMode.JOIN);
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT payment from Payment payment "
				+"WHERE payment.accountingYear.year_id =:year_id and payment.advance_payment =:advance_payment and payment.company.company_id =:company_id and payment.flag =:flag and payment.entry_status !=:entry_status");
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("advance_payment", false);
		query.setParameter("year_id", yeaId);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Payment)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public List<Payment> getATList(Integer month, Long yearId, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.eq("advance_payment", true));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("accountingYear.year_id", yearId));
		criteria.add(Restrictions.sqlRestriction("MONTH(this_.date) = " + month));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("supplier_bill_no", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		return criteria.list();
	}

	@Override
	public List<Payment> getATAdjList(Integer month, Long yearId, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.eq("advance_payment", true));
		criteria.add(Restrictions.eq("company.company_id",companyId));
		criteria.add(Restrictions.eq("accountingYear.year_id",yearId));
		criteria.add(Restrictions.sqlRestriction("MONTH(this_.date) ="+(month-1)));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("supplier_bill_no", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		return criteria.list();
	}

	@Override
	public Integer getCountByYear(Long yearId, Long companyId, String range) {
		/*Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("accountingYear.year_id", yearId));
		criteria.setProjection(Projections.rowCount());
		return Integer.parseInt(criteria.uniqueResult().toString());*/
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select count(payment_id)from payment where company_id =:company_id and accounting_year_id =:year_id and voucher_no LIKE :term");
		query.setParameter("company_id", companyId);
		query.setParameter("year_id", yearId);
		query.setParameter("term",  range + "%");
		System.out.println(query.list());
		if(query.list()==null || query.list().isEmpty()){
			return 0;
		}
		else{
			System.out.println(query.list().get(0));
			if(query.list().get(0)==null)
				return 0;
			else{
				Integer vid=((BigInteger) query.list().get(0)).intValue();
				System.out.println(vid);
				return vid;
			}
		}
	}

	@Override
	public Payment findOneWithAll(Long paymentId) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.eq("payment_id", paymentId));
		criteria.setFetchMode("supplier_bill_no", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		criteria.setFetchMode("paymentDetails", FetchMode.JOIN);
		criteria.setFetchMode("advpayment", FetchMode.JOIN);

		return (Payment) criteria.uniqueResult();
	}

	@Override
	public Long savePaymentThroughExcel(Payment entry) {
		Session session = getCurrentSession();
		Long id= (Long) session.save(entry);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public void savePaymentDetailsThroughExcel(PaymentDetails details, Long payment_id) {
        Session session = getCurrentSession();
		
		Criteria criteria =session.createCriteria(Payment.class);
		criteria.add(Restrictions.eq("payment_id", payment_id));
		Payment payment=(Payment) criteria.uniqueResult();
		details.setPayment_id(payment);
		
		session.save(details);
		session.flush();
		session.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Payment> getDayBookReport(LocalDate from_date,LocalDate to_date,Long companyId) {
		/*Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.ge("date", from_date));
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.desc("date"));
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
	    	List<Payment> list =  new ArrayList<Payment>();
			Session session = getCurrentSession();
			Query query = session.createQuery("SELECT payment from Payment payment "
					+"WHERE payment.company.company_id =:company_id and payment.flag =:flag and payment.date >=:from_date and payment.date <=:to_date and payment.entry_status !=:entry_status order by payment.date desc");
			query.setParameter("company_id", companyId);
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("flag", true);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Payment)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
			  session.clear();
			  return list;
		
			
	}

	@Override
	public void updatePaymentThroughExcel(Payment entry) {
		Session session = getCurrentSession();
		session.merge(entry);
		session.flush();
		session.clear();
	}
	@Override
	public void updatePaymentDetailsThroughExcel(PaymentDetails details, Long payment_id) {
		
		    Session session = getCurrentSession();
			
			Criteria criteria =session.createCriteria(Payment.class);
			criteria.add(Restrictions.eq("payment_id", payment_id));
			Payment payment=(Payment) criteria.uniqueResult();
			details.setPayment_id(payment);
			
			session.save(details);
			session.flush();
			session.clear();
	}

	@Override
	public List<Payment> getCashBookBankBookReport(LocalDate from_date, LocalDate to_date, Long companyId,
			Integer type) {
	/*	Criteria criteria = getCurrentSession().createCriteria(Payment.class);
        criteria.add(Restrictions.ge("date", from_date));
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.desc("date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT payment from Payment payment "
				+"WHERE payment.company.company_id =:company_id and payment.flag =:flag and payment.date >=:from_date and payment.date <=:to_date and payment.entry_status !=:entry_status order by payment.date desc");
		query.setParameter("company_id", companyId);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Payment)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	
	}

	@Override
	public List<Payment> findAllPaymentsOfCompany(Long comapny_id) {
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.company company "
				+"WHERE payment.company.company_id =:company_id and excel_voucher_no IS NOT NULL");
		query.setParameter("company_id", comapny_id);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Payment)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  Collections.sort(list, new Comparator<Payment>() {
	            public int compare(Payment pay1, Payment pay2) {
	                Long payment_id1 = new Long(pay1.getPayment_id());
	                Long payment_id2 = new Long(pay2.getPayment_id());
	                return payment_id2.compareTo(payment_id1);
	            }
	        });
		  return list;
	}

	@Override
	public int getCountByDate(Long companyId, String range, LocalDate date) {
		Session session = getCurrentSession();
		
		Criteria criteria = session.createCriteria(Payment.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("date", date));
		criteria.addOrder(Order.desc("payment_id"));
		criteria.setMaxResults(1);
		Payment entry = (Payment) criteria.uniqueResult();
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
		
	/*	Query query = session.createSQLQuery("select count(payment_id)from payment where company_id =:company_id and date = '"+date+"'");
		query.setParameter("company_id", companyId);		
		System.out.println(query.list());
		if(query.list()==null || query.list().isEmpty()){
			return 0;
		}
		else{
			System.out.println(query.list().get(0));
			if(query.list().get(0)==null)
				return 0;
			else {
				Integer vid=((BigInteger) query.list().get(0)).intValue();
				System.out.println(vid);
				return vid;
			}
		}*/
	}

	@Override
	public PaymentDetails getPaymentDetailById(Long paymentDetailId) {
		Criteria criteria = getCurrentSession().createCriteria(PaymentDetails.class);
		criteria.add(Restrictions.eq("id", paymentDetailId));
		criteria.setFetchMode("payment_id", FetchMode.JOIN);
		return (PaymentDetails) criteria.uniqueResult();
	}

	@Override
	public void updatePaymentDetail(PaymentDetails paymentDetails) {
		Session session = getCurrentSession();
		session.merge(paymentDetails);
		session.flush();
		session.clear();
	}

	@Override
	public void deletePaymentDetail(Long id) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from PaymentDetails where id =:id");
		query.setParameter("id", id);
		try{
			query.executeUpdate();	
		}
		catch(Exception e){
			e.printStackTrace();	
		}
		
	}

	@Override
	public List<Payment> findallpaymententryofsales(long id) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Payment where supplier_bill_no.purchase_id =:purchase_id");
		query.setParameter("purchase_id", id);
		System.out.println(query.list());		
		return query.list();
	}

	@Override
	public void diactivateByIdValue(Long payment_id, int status) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update Payment set entry_status=:entry_status where payment_id =:id");
		query.setParameter("id", payment_id);
		query.setParameter("entry_status", status);
		query.executeUpdate();
	}

	@Override
	public void activateByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update Payment set entry_status=:entry_status where payment_id =:id");
		query.setParameter("id", entityId);
		query.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
		query.executeUpdate();		
	}

	@Override
	public Double findpaidtds(Long purchase_id) {
		Session session = getCurrentSession();
		Double tds=0.0;
		Query query = session.createSQLQuery("select ROUND(SUM(tds_amount), 2) as tds_amount from payment where supplier_bill_no =:purchase_id and ((entry_status = 0) || (entry_status = 1) || (entry_status IS NULL))");
		query.setParameter("purchase_id", purchase_id);
		if(query.list()==null || query.list().isEmpty())
		{
			tds=(double) 0;
		}
		else
		{
			if(query.list().get(0)==null || query.list().isEmpty())
				tds=(double)0;
			else
				tds= Double.parseDouble(query.list().get(0).toString());	
		}
		return tds;
	}

	@Override
	public void cahngeStatusOfPaymentThroughExcel(Payment entry) {
		Session session = getCurrentSession();
		entry.setFlag(false);
		session.merge(entry);
		session.flush();
		session.clear();
		
	}

	@Override
	public void updateadvpaymentamount(Long payment_id, Double ramount, Double transactionvalue, Double tdsamount) {
		Session session = getCurrentSession();
		Query queryadv = session.createQuery("update Payment set amount=:amount where payment_id =:id");
		queryadv.setParameter("id", payment_id);
		queryadv.setParameter("amount", ramount);
		queryadv.setParameter("transaction_value_head", transactionvalue);
		queryadv.setParameter("tds_amount", tdsamount);
		queryadv.executeUpdate();		
	}

	/*@Override
	public List<Payment> getAdvancePaymentListForLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId) {
		
		List<Payment> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.supplier_bill_no billno "
				+ "WHERE payment.advance_payment=:advance_payment and payment.flag=:flag and payment.company.company_id =:company_id and payment.entry_status !=:entry_status and payment.date >=:date1 and payment.date <=:date2 and payment.supplier_bill_no.purchase_id IS NULL order by payment.date ASC,payment.payment_id ASC");
		query.setParameter("advance_payment", true);
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		query.setParameter("date1", from_date);
		query.setParameter("date2", to_date);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
			  list.add((Payment)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  return list;
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.ge("date", from_date)); 
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("advance_payment", true));
		criteria.add(Restrictions.eq("supplier_bill_no.purchase_id", null));
		criteria.setFetchMode("supplier_bill_no", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}*/

	@Override
	public List<Payment> getPaymentListForLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId,Long bank_id) {
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		Query query = null;
		
		if(bank_id.equals((long)-4))
		{
		query = session.createQuery("SELECT payment from Payment payment "
				+"WHERE payment.company.company_id =:company_id and payment.flag =:flag and payment.date >=:from_date and payment.date <=:to_date and payment.entry_status !=:entry_status and payment.bank.bank_id IS NOT NULL order by payment.date ASC,payment.payment_id ASC");
		}
		else
		{
  		query = session.createQuery("SELECT payment from Payment payment "
					+"WHERE payment.company.company_id =:company_id and payment.flag =:flag and payment.date >=:from_date and payment.date <=:to_date and payment.entry_status !=:entry_status and payment.bank.bank_id =:bank_id order by payment.date ASC,payment.payment_id ASC");
			query.setParameter("bank_id",bank_id);
			
		}
		query.setParameter("company_id", companyId);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Payment)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
		 	}

	@Override
	public void deletePayment(Long entityId) {
		  Query query1 =  getCurrentSession().createSQLQuery("delete from payment_details where payment_id =:payment_id");
			query1.setParameter("payment_id", entityId);
			query1.executeUpdate();
			balanceDao.deleteOpeningBalance(null,null, null, null, entityId, null,null,null, null,null,null,null);
			Query query = getCurrentSession().createQuery("delete from Payment where payment_id =:id");
			query.setParameter("id", entityId);
				try{
					query.executeUpdate();
				}
				catch(Exception e){			
				}
			}

	@Override
	public List<Payment> CashPaymentOfMoreThanRS10000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId) {
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT payment from Payment payment "
				+"WHERE payment.flag =:flag and payment.entry_status !=:entry_status and payment.company.company_id =:company_id and amount+tds_amount>=10000 and payment_type=1 and date>=:from_date and date<=:to_date");
		query.setParameter("company_id", companyId);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Payment)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
		 	}

	@Override
	public List<Payment> supplierPaymentHavingUnadjusted(LocalDate from_date, LocalDate to_date, Long companyId) {
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.company company "
				+"WHERE payment.company.company_id =:company_id "
				+"and payment.date<:from_date and payment.supplier_bill_no.purchase_id IS NULL and payment.flag =:flag and payment.advance_payment=:advance_payment");
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("from_date", from_date);
		query.setParameter("advance_payment", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
			  Payment payment = (Payment)scrollableResults.get()[0];
			  payment.setAgingDays(Days.daysBetween(payment.getDate(), from_date).getDays());
		   list.add(payment);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public void deletePaymentAginstadvance(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from Payment where payment_id =:id");
		query.setParameter("id", entityId);
			try{
				query.executeUpdate();
			
			}
			catch(Exception e){
				
			}
		
	}

	@Override
	public List<Payment> getPaymentForLedgerReport(LocalDate from_date, LocalDate to_date, Long suppilerId,
			Long companyId) {
		List<Payment> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query =null;
		if(suppilerId!=null) {
			
		    if(suppilerId.equals((long)-2)|| suppilerId==0)
			{
		    	query = session.createQuery("SELECT payment from Payment payment "
				+ "WHERE payment.flag=:flag and payment.company.company_id =:company_id and payment.subLedger.subledger_Id IS NULL and payment.entry_status !=:entry_status and payment.date >=:date1 and payment.date <=:date2");
		
				}
				else
				{
					
				 query = session.createQuery("SELECT payment from Payment payment "
							+ "WHERE payment.flag=:flag and payment.company.company_id =:company_id and payment.supplier.supplier_id =:supplier_id and payment.subLedger.subledger_Id IS NULL and payment.entry_status !=:entry_status and payment.date >=:date1 and payment.date <=:date2");
					query.setParameter("supplier_id", suppilerId);
				
				}
		    query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			query.setParameter("date1", from_date);
			query.setParameter("date2", to_date);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			
			  while (scrollableResults.next()) {
				  list.add((Payment)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
			  session.clear();
				
		 
	       }
		
		  return list;
	}

	@Override
	public List<Payment> supplirPaymentHavingGST0(LocalDate fromDate, LocalDate toDate, Long company_id) {
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.company company "
					+"WHERE payment.company.company_id =:company_id and payment.flag =:flag and payment.date>=:from_date and payment.date<=:to_date "
					+ "and payment.gst_applied=:gst_applied and payment.entry_status !=:entry_status and payment.supplier.gst_applicable=:gst_applicable and payment.CGST_head=:cgst and payment.IGST_head=:igst and payment.SGST_head=:sgst");
			query.setParameter("from_date", fromDate);
			query.setParameter("to_date", toDate);
			query.setParameter("company_id", company_id);
			query.setParameter("flag", true);
			query.setParameter("gst_applicable", true);
			query.setParameter("gst_applied", true);
			query.setParameter("cgst", (double)0);
			query.setParameter("igst", (double)0);
			query.setParameter("sgst", (double)0);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Payment)scrollableResults.get()[0]);
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
	public List<Payment> supplierPaymentWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,
			LocalDate to_date, Long companyId) {
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.company company "
					+"WHERE payment.company.company_id =:company_id and payment.flag =:flag and payment.date>=:from_date and payment.date<=:to_date and payment.supplier IS NULL "
					+ "and payment.amount>=:amount and payment.tds_amount=:tds_amount and (payment.subLedger.subledger_name LIKE '%Legal%' or payment.subLedger.subledger_name LIKE '%Professional%' or payment.subLedger.subledger_name LIKE '%Repairs & Maintenance%')");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("amount",(double)30000);
			query.setParameter("tds_amount",(double)0);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Payment)scrollableResults.get()[0]);
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
	public List<Payment> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate fromDate, LocalDate toDate,
			Long company_id) {
		
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.paymentDetails paymentDetails "
					+"WHERE payment.company.company_id =:company_id and payment.flag =:flag and payment.date>=:from_date and payment.date<=:to_date "
					+ "and payment.supplier.gst_applicable=:gst_applicable and (payment.CGST_head>0 or payment.IGST_head>0 or payment.SGST_head>0)");
			query.setParameter("from_date", fromDate);
			query.setParameter("to_date", toDate);
			query.setParameter("company_id", company_id);
			query.setParameter("flag", true);
			query.setParameter("gst_applicable", false);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Payment)scrollableResults.get()[0]);
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
	public Payment isExcelVocherNumberExist(String vocherNo, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.eq("excel_voucher_no", vocherNo));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setFetchMode("paymentDetails", FetchMode.SELECT);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Payment)criteria.uniqueResult();
	}

	@Override
	public List<Payment> getAllPaymentsAgainstAdvancePayment(Long advancePaymentId) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.eq("advpayment.payment_id", advancePaymentId));
		return criteria.list();
	}
	
	
	@Override
	public List<Payment> getAllPaymentsAgainstOpeningBalance(Long openingbalId) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		criteria.add(Restrictions.eq("againstOpeningBalnce.payment_id", openingbalId));
		return criteria.list();
	}


	@Override
	public List<Payment> getAllAdvancePaymentsAgainstSupplier(Long supplierId, Long companyId) {
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.company company "
				+"WHERE payment.company.company_id =:company_id and payment.supplier.supplier_id=:supplier_id  and payment.flag =:flag and payment.advance_payment=:advance_payment and payment.supplier_bill_no.purchase_id is NULL ORDER by payment.date DESC,payment.payment_id DESC");
		query.setParameter("company_id", companyId);
		query.setParameter("supplier_id", supplierId);
		query.setParameter("flag", true);
		query.setParameter("advance_payment", true);
		
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Payment)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}


	@Override
	public List<Payment> getAllOpeningBalanceAgainstPaymentForPeriod(Long supplierId, Long companyId,
			LocalDate todate) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		Criterion criterion2 = Restrictions.eq("supplier.supplier_id",supplierId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	
		Criterion criterion4 = Restrictions.le("date",todate);
		criteria.add(Restrictions.and(criterion1,criterion2,criterion3,criterion4));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}


	@Override
	public List<Payment> getAllAdvancePaymentsAgainstSupplierForPeriod(Long supplierId, Long companyId,
			LocalDate todate) {
		List<Payment> list =  new ArrayList<Payment>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT payment from Payment payment LEFT JOIN FETCH payment.company company "
				+"WHERE payment.company.company_id =:company_id and payment.supplier.supplier_id=:supplier_id  and payment.flag =:flag and payment.advance_payment=:advance_payment and payment.date<=:todate and payment.supplier_bill_no.purchase_id is NULL ORDER by payment.date DESC,payment.payment_id DESC");
		query.setParameter("company_id", companyId);
		query.setParameter("supplier_id", supplierId);
		query.setParameter("flag", true);
		query.setParameter("advance_payment", true);
		query.setParameter("todate", todate);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Payment)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}


	@Override
	public List<Payment> getPaymentForPurchase(Long supplierId, Long companyId, LocalDate toDate) {
	Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		
		Criterion criterion2 = Restrictions.eq("supplier.supplier_id",supplierId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	
		Criterion criterion4=Restrictions.ne("againstOpeningBalnce", true);
		Criterion criterion5=Restrictions.le("date", toDate);
		criteria.add(Restrictions.and(criterion2,criterion3,criterion5));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}


	@Override
	public List<Payment> getPaymentForLedgerReport1(LocalDate to_date, Long suppilerId, Long companyId) {
		// TODO Auto-generated method stub
		List<Payment> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query =null;
		if(suppilerId!=null) {
			
		    if(suppilerId.equals((long)-2)|| suppilerId==0)
			{
		    	query = session.createQuery("SELECT payment from Payment payment "
				+ "WHERE payment.flag=:flag and payment.company.company_id =:company_id and payment.subLedger.subledger_Id IS NULL and payment.entry_status !=:entry_status  and payment.date <=:date2");
		
				}
				else
				{
					
				 query = session.createQuery("SELECT payment from Payment payment "
							+ "WHERE payment.flag=:flag and payment.company.company_id =:company_id and payment.supplier.supplier_id =:supplier_id and payment.subLedger.subledger_Id IS NULL and payment.entry_status !=:entry_status  and payment.date <=:date2");
					query.setParameter("supplier_id", suppilerId);
				
				}
		    query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			//query.setParameter("date1", from_date);
			query.setParameter("date2", to_date);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			
			  while (scrollableResults.next()) {
				  list.add((Payment)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
			  session.clear();
				
		 
	       }
		
		  return list;
	}

	/*
	 * @Override public List<OpeningBalances>
	 * getAllBalancesAgainstOpeningBalance(Long advancePaymentId) { Criteria
	 * criteria = getCurrentSession().createCriteria(Payment.class);
	 * criteria.add(Restrictions.eq("advpayment.payment_id", advancePaymentId));
	 * return criteria.list(); }
	 */
		
	}
	