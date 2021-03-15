package com.fasset.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.ICreditNoteDAO;
import com.fasset.dao.interfaces.IGstTaxMasterDAO;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.ISalesEntryDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.CreditDetails;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.exceptions.MyWebException;

@Repository
@Transactional
public class CreditNoteDAOImpl extends AbstractHibernateDao<CreditNote> implements ICreditNoteDAO {

	@Autowired
	private IOpeningBalancesDAO balanceDao;

	@Autowired
	private IGstTaxMasterDAO GstTaxDao;

	@Autowired
	private ISalesEntryDAO salesEntryDAO;

	@Override
	public CreditNote findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		criteria.add(Restrictions.eq("credit_no_id", id));
		return (CreditNote) criteria.uniqueResult();
	}

	@Override
	public CreditNote findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreditNote findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CreditNote> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("credit_no_id"));
		return criteria.list();
	}

	@Override
	public void create(CreditNote entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
		session.clear();
	}

	@Override
	public void update(CreditNote entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
		session.clear();
	}

	@Override
	public void merge(CreditNote entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(CreditNote entity) throws MyWebException {
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
	public boolean validatePreTransaction(CreditNote entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(CreditNote entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public CreditNote isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CreditNote> getReport(Long id, LocalDate from_date, LocalDate to_date, Long companyId) {

		/*
		 * List<CreditNote> list = new ArrayList<>(); Session session =
		 * getCurrentSession(); Criteria criteria =
		 * session.createCriteria(CreditNote.class);
		 * criteria.add(Restrictions.ge("date", from_date));
		 * criteria.add(Restrictions.le("date", to_date));
		 * criteria.add(Restrictions.ne("entry_status",
		 * MyAbstractController.ENTRY_CANCEL));
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 * criteria.add(Restrictions.eq("company.company_id", companyId));
		 * criteria.add(Restrictions.eq("flag", true)); if(id > 0){
		 * criteria.add(Restrictions.eq("customer.customer_id", id)); }
		 * criteria.setFetchMode("customer", FetchMode.JOIN);
		 * criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		 * criteria.setFetchMode("creditDetails", FetchMode.JOIN); list =
		 * criteria.list(); session.clear(); return list;
		 */

		List<CreditNote> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = null;
		if (id != null) {
			if (id > 0) {
				query = session.createQuery("SELECT note from CreditNote note "
						+ "WHERE note.flag=:flag and note.company.company_id =:company_id and note.customer.customer_id =:customer_id and note.entry_status !=:entry_status and note.date >=:date1 and note.date <=:date2 ORDER by note.date DESC,note.credit_no_id DESC");
				query.setParameter("customer_id", id);

			} else {
				query = session.createQuery("SELECT note from CreditNote note "
						+ "WHERE note.flag=:flag and note.company.company_id =:company_id and note.entry_status !=:entry_status and note.date >=:date1 and note.date <=:date2 ORDER by note.date DESC,note.credit_no_id DESC");
			}
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			query.setParameter("date1", from_date);
			query.setParameter("date2", to_date);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);

			while (scrollableResults.next()) {
				list.add((CreditNote) scrollableResults.get()[0]);
				session.evict(scrollableResults.get()[0]);
			}
			session.clear();
		}
		return list;

	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query1 = session.createQuery("delete from CreditDetails where credit_id.credit_no_id =:id");
		query1.setParameter("id", entityId);
		query1.executeUpdate();

		balanceDao.deleteOpeningBalance(null, null, entityId, null, null, null, null, null, null, null, null, null);
		Query query = session.createQuery("delete from CreditNote where credit_no_id =:id");
		query.setParameter("id", entityId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "Credit Note Deleted successfully";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "You can't delete Credit Note";
		}
		return msg;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CreditNote> findAllCreditNoteOfCompany(Long CompanyId,Boolean flag) {
	/*	Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		if(flag==true)
			criteria.add(Restrictions.eq("flag", true));
		else
			criteria.add(Restrictions.eq("flag", false));
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("credit_no_id"));
		return criteria.list();*/
		
		List<CreditNote> list =  new ArrayList<CreditNote>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT note from CreditNote note LEFT JOIN FETCH note.company company "
				+"LEFT JOIN FETCH note.sales_bill_id sales_bill_id "
				+"WHERE note.company.company_id =:company_id and note.flag =:flag ORDER by note.date DESC,note.credit_no_id DESC");
		query.setParameter("company_id", CompanyId);
		//query.setParameter("yearID",yearID); //and note.accounting_year_id.year_id=:yearID 
		query.setParameter("flag", flag);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((CreditNote)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  /*Collections.sort(list, new Comparator<CreditNote>() {
	            public int compare(CreditNote pay1, CreditNote pay2) {
	                Long credit_no_id1 = new Long(pay1.getCredit_no_id());
	                Long credit_no_id2 = new Long(pay2.getCredit_no_id());
	                return credit_no_id2.compareTo(credit_no_id1);
	            }
	        });*/
		  return list;
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<CreditNote> findAllCreditNoteOfCompany(Long
	 * CompanyId,Boolean flag,Long yearID) { Criteria criteria =
	 * getCurrentSession().createCriteria(CreditNote.class);
	 * criteria.add(Restrictions.eq("company.company_id", CompanyId));
	 * if(flag==true) criteria.add(Restrictions.eq("flag", true)); else
	 * criteria.add(Restrictions.eq("flag", false));
	 * criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
	 * criteria.setFetchMode("customer", FetchMode.JOIN);
	 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	 * criteria.addOrder(Order.desc("credit_no_id")); return criteria.list();
	 * 
	 * List<CreditNote> list = new ArrayList<CreditNote>(); Session session =
	 * getCurrentSession(); Query query = session.
	 * createQuery("SELECT note from CreditNote note LEFT JOIN FETCH note.company company "
	 * +"LEFT JOIN FETCH note.sales_bill_id sales_bill_id "
	 * +"WHERE note.company.company_id =:company_id and note.flag =:flag and note.accounting_year_id.year_id=:yearID ORDER by note.date DESC,note.credit_no_id DESC"
	 * ); query.setParameter("company_id", CompanyId);
	 * query.setParameter("yearID",yearID); query.setParameter("flag", flag);
	 * ScrollableResults scrollableResults =
	 * query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY); while
	 * (scrollableResults.next()) {
	 * list.add((CreditNote)scrollableResults.get()[0]);
	 * session.evict(scrollableResults.get()[0]); } session.clear();
	 * Collections.sort(list, new Comparator<CreditNote>() { public int
	 * compare(CreditNote pay1, CreditNote pay2) { Long credit_no_id1 = new
	 * Long(pay1.getCredit_no_id()); Long credit_no_id2 = new
	 * Long(pay2.getCredit_no_id()); return credit_no_id2.compareTo(credit_no_id1);
	 * } }); return list; }
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<CreditNote> findBySalesId(long salesId) {
		Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		criteria.add(Restrictions.eq("sales_bill_id.sales_id", salesId));
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<CreditNote> getCdnrList(LocalDate from_date, LocalDate to_date, Long companyId) {

		/*
		 * Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		 * criteria.createAlias("customer", "cust");
		 * criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId)
		 * , Restrictions.eq("accounting_year_id.year_id", yearId),
		 * Restrictions.sqlRestriction("MONTH(this_.date) = "+month),
		 * Restrictions.eq("cust.gst_applicable",true), Restrictions.ne("entry_status",
		 * MyAbstractController.ENTRY_CANCEL) ));
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 * criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		 * criteria.setFetchMode("customer", FetchMode.JOIN);
		 * criteria.setFetchMode("creditDetails", FetchMode.JOIN); return
		 * criteria.list();
		 */

		List<CreditNote> cdnrList = new ArrayList<>();
		List<CreditNote> cdnrWithGSTrateList = new ArrayList<>();

		Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		criteria.createAlias("customer", "cust");
		criteria.add(
				Restrictions.and(Restrictions.eq("company.company_id", companyId), Restrictions.ge("date", from_date),
						Restrictions.le("date", to_date), Restrictions.eq("cust.gst_applicable", true),
						Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.addOrder(Order.asc("date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("creditDetails", FetchMode.JOIN);
		cdnrList = criteria.list();

		for (CreditNote entry : cdnrList) {
			SalesEntry salesentry = null;
			if (entry.getSales_bill_id() != null) {
				salesentry = entry.getSales_bill_id();
			}
			Set<CreditDetails> productinfoList = new HashSet<>();
			for (CreditDetails entity : entry.getCreditDetails()) {

				if (salesentry != null) {

					for (SalesEntryProductEntityClass salesentity : salesEntryDAO
							.findAllSalesEntryProductEntityList(salesentry.getSales_id())) {

						if (entity.getProduct_id() != null
								&& entity.getProduct_id().getProduct_id().equals(salesentity.getProduct_id())) {
							GstTaxMaster tax = GstTaxDao.getHSNbyDate(salesentry.getCreated_date(),
									salesentity.getHSNCode());
							entity.setGstRate(tax.getIgst());
							productinfoList.add(entity);
						}

					}
				}

			}
			entry.setCreditDetails(productinfoList);
			cdnrWithGSTrateList.add(entry);
		}
		return cdnrWithGSTrateList;

	}

	@Override
	public List<CreditNote> getCdnurList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {

		List<CreditNote> cdnurList = new ArrayList<>();
		List<CreditNote> cdnurgstList = new ArrayList<>();
		List<CreditNote> cdnurWithGSTrateList = new ArrayList<>();

		Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		criteria.createAlias("customer", "cust");
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.ge("date", from_date), Restrictions.le("date", to_date),
				Restrictions.not(Restrictions.eq("cust.state.state_id", stateId)),
				Restrictions.eq("cust.gst_applicable", false),

				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.addOrder(Order.asc("date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("creditDetails", FetchMode.JOIN);
		cdnurgstList = criteria.list();

		for (CreditNote note : cdnurgstList) {
			if (note.getSales_bill_id() != null
					&& (note.getSales_bill_id().getRound_off() + note.getSales_bill_id().getTds_amount()) > 250000) {
				cdnurList.add(note);
			}
		}

		for (CreditNote entry : cdnurList) {
			SalesEntry salesentry = null;
			if (entry.getSales_bill_id() != null) {
				salesentry = entry.getSales_bill_id();
			}

			Set<CreditDetails> productinfoList = new HashSet<>();
			for (CreditDetails entity : entry.getCreditDetails()) {

				if (salesentry != null) {

					for (SalesEntryProductEntityClass salesentity : salesEntryDAO
							.findAllSalesEntryProductEntityList(salesentry.getSales_id())) {

						if (entity.getProduct_id() != null
								&& entity.getProduct_id().getProduct_id().equals(salesentity.getProduct_id())) {
							GstTaxMaster tax = GstTaxDao.getHSNbyDate(salesentry.getCreated_date(),
									salesentity.getHSNCode());
							entity.setGstRate(tax.getIgst());
							productinfoList.add(entity);
						}

					}
				}

			}
			entry.setCreditDetails(productinfoList);
			cdnurWithGSTrateList.add(entry);
		}

		return cdnurWithGSTrateList;

	}

	@Override
	public String deleteByCebitNoteDetailsId(Long entityId) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(CreditDetails.class);
		criteria.add(Restrictions.eq("credit_detail_id", entityId));
		Query query = session.createQuery("delete from CreditDetails where credit_detail_id =:id");
		query.setParameter("id", entityId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "Credit Note Detail Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete Credit Note Detail";
		}
		return msg;
	}

	@Override
	public Integer getCountByYear(Long yearId, Long companyId, String range) {
		/*
		 * Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		 * criteria.add(Restrictions.eq("company.company_id", companyId));
		 * criteria.add(Restrictions.eq("accounting_year_id.year_id", yearId));
		 * criteria.setProjection(Projections.rowCount()); return
		 * Integer.parseInt(criteria.uniqueResult().toString());
		 */
		Session session = getCurrentSession();
		Query query = session.createSQLQuery(
				"select count(credit_no_id)from credit_note where company_id =:company_id and accounting_year_id =:year_id and voucher_no LIKE :term");
		query.setParameter("company_id", companyId);
		query.setParameter("year_id", yearId);
		query.setParameter("term", range + "%");
		System.out.println(query.list());
		if (query.list() == null || query.list().isEmpty())
			return 0;
		else {
			System.out.println(query.list().get(0));
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
	public CreditNote findOneView(Long creditNoteId) {
		Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		criteria.add(Restrictions.eq("credit_no_id", creditNoteId));
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("creditDetails", FetchMode.JOIN);
		return (CreditNote) criteria.uniqueResult();
	}

	@Override
	public List<CreditNote> getReportAgainstSubledger(Long id, LocalDate from_date, LocalDate to_date, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		criteria.add(Restrictions.ge("date", from_date));
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("flag", true));
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		return criteria.list();
	}

	@Override
	public Long saveCreditNoteTroughExcel(CreditNote note) {
		Session session = getCurrentSession();
		Long id = (Long) session.save(note);
		session.flush();
		session.clear();
		return id;

	}

	@Override
	public void saveCreditDetailsTroughExcel(CreditDetails entity, Long credit_id) {

		Session session = getCurrentSession();

		Criteria criteria = session.createCriteria(CreditNote.class);
		criteria.add(Restrictions.eq("credit_no_id", credit_id));
		CreditNote note = (CreditNote) criteria.uniqueResult();
		entity.setCredit_id(note);

		session.save(entity);
		session.flush();
		session.clear();
	}

	@Override
	public int getCountByDate(Long companyId, String range, LocalDate date) {
		Session session = getCurrentSession();

		Criteria criteria = session.createCriteria(CreditNote.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("date", date));
		criteria.addOrder(Order.desc("credit_no_id"));
		criteria.setMaxResults(1);
		CreditNote entry = (CreditNote) criteria.uniqueResult();
		Integer i = null;
		if (entry != null) {

			String vocher_no = entry.getVoucher_no().trim();
			String[] vochers = vocher_no.split("/");
			String vocher = vochers[vochers.length - 1];
			i = Integer.parseInt(vocher.trim());
		} else {
			i = 0;

		}
		return i;

		/*
		 * Query query = session.
		 * createSQLQuery("select count(credit_no_id)from credit_note where company_id =:company_id and date ='"
		 * +date+"'"); query.setParameter("company_id", companyId);
		 * 
		 * System.out.println(query.list()); if(query.list()==null ||
		 * query.list().isEmpty()){ return 0; } else{
		 * System.out.println(query.list().get(0)); if(query.list().get(0)==null) return
		 * 0; else { Integer vid=((BigInteger) query.list().get(0)).intValue();
		 * System.out.println(vid); return vid; } }
		 */
	}

	@Override
	public List<CreditNote> getDayBookReport(LocalDate from_date, LocalDate to_date, Long companyId) {
		/*
		 * Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		 * criteria.add(Restrictions.ge("date", from_date));
		 * criteria.add(Restrictions.le("date", to_date));
		 * criteria.add(Restrictions.eq("company.company_id", companyId));
		 * criteria.add(Restrictions.ne("entry_status",
		 * MyAbstractController.ENTRY_CANCEL));
		 * criteria.add(Restrictions.ne("entry_status",
		 * MyAbstractController.ENTRY_CANCEL)); criteria.add(Restrictions.eq("flag",
		 * true)); criteria.addOrder(Order.desc("date"));
		 * criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		 * criteria.setFetchMode("customer", FetchMode.JOIN);
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); return
		 * criteria.list();
		 */
		List<CreditNote> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT note from CreditNote note "
				+ "WHERE note.flag=:flag and note.company.company_id =:company_id and note.entry_status !=:entry_status and note.date >=:date1 and note.date <=:date2 order by note.date desc");
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		query.setParameter("date1", from_date);
		query.setParameter("date2", to_date);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);

		while (scrollableResults.next()) {
			list.add((CreditNote) scrollableResults.get()[0]);
			session.evict(scrollableResults.get()[0]);
		}
		session.clear();

		return list;
	}

	@Override
	public List<CreditNote> findAllCreditNotesOfCompany(Long CompanyId) {

		List<CreditNote> list = new ArrayList<CreditNote>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT credit from CreditNote credit LEFT JOIN FETCH credit.company company "
				+ "WHERE credit.company.company_id =:company_id and excel_voucher_no IS NOT NULL");
		query.setParameter("company_id", CompanyId);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		while (scrollableResults.next()) {
			list.add((CreditNote) scrollableResults.get()[0]);
			session.evict(scrollableResults.get()[0]);
		}
		Collections.sort(list, new Comparator<CreditNote>() {
			public int compare(CreditNote pay1, CreditNote pay2) {
				Long credit_no_id1 = new Long(pay1.getCredit_no_id());
				Long credit_no_id2 = new Long(pay2.getCredit_no_id());
				return credit_no_id2.compareTo(credit_no_id1);
			}
		});
		return list;
	}

	@Override
	public void updateCreditNoteThroughExcel(CreditNote note) {
		Session session = getCurrentSession();
		session.merge(note);
		session.flush();
		session.clear();
	}

	@Override
	public void updateCreditDetailsThroughExcel(CreditDetails entity, Long credit_id) {

		Session session = getCurrentSession();

		Criteria criteria = session.createCriteria(CreditNote.class);
		criteria.add(Restrictions.eq("credit_no_id", credit_id));

		CreditNote note = (CreditNote) criteria.uniqueResult();

		entity.setCredit_id(note);
		session.save(entity);
		session.flush();
		session.clear();
	}

	@Override
	public Long saveCreditNoteThroughExcel(CreditNote note) {
		Session session = getCurrentSession();
		Long id = (Long) session.save(note);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public CreditDetails getCreditDetailsById(Long creditDetailsId) {
		Criteria criteria = getCurrentSession().createCriteria(CreditDetails.class);
		criteria.add(Restrictions.eq("credit_detail_id", creditDetailsId));
		criteria.setFetchMode("credit_id", FetchMode.JOIN);
		return (CreditDetails) criteria.uniqueResult();
	}

	@Override
	public void updateCreditDetail(CreditDetails creditDetails) {
		Session session = getCurrentSession();
		session.merge(creditDetails);
		session.flush();
		session.clear();
	}

	@Override
	public void changeStatusOfCreditNoteThroughExcel(CreditNote note) {
		Session session = getCurrentSession();
		note.setFlag(false);
		session.merge(note);
		session.flush();
		session.clear();
	}

	@Override
	public void diactivateByIdValue(Long credit_no_id) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update CreditNote set entry_status=:entry_status where credit_no_id =:id");
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		query.setParameter("id", credit_no_id);
		try {
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void activateByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update CreditNote set entry_status=:entry_status where credit_no_id =:id");
		query.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
		query.setParameter("id", entityId);
		try {
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<CreditNote> getCreditNoteForLedgerReport(LocalDate from_date, LocalDate to_date, Long customerId,
			Long companyId) {
		List<CreditNote> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = null;
		if (customerId != null) {

			if (customerId.equals((long) -1)) {
				query = session.createQuery("SELECT note from CreditNote note "
						+ "WHERE note.flag=:flag and note.company.company_id =:company_id and note.entry_status !=:entry_status and note.date >=:date1 and note.date <=:date2");
			} else {
				query = session.createQuery("SELECT note from CreditNote note "
						+ "WHERE note.flag=:flag and note.company.company_id =:company_id and note.customer.customer_id =:customer_id and note.entry_status !=:entry_status and note.date >=:date1 and note.date <=:date2");
				query.setParameter("customer_id", customerId);

			}
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			query.setParameter("date1", from_date);
			query.setParameter("date2", to_date);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			while (scrollableResults.next()) {
				list.add((CreditNote) scrollableResults.get()[0]);
				session.evict(scrollableResults.get()[0]);
			}
			session.clear();
		}
		return list;
	}

	@Override
	public List<CreditNote> getCreditNoteGstList(LocalDate from_date, LocalDate to_date, Long companyId) {
		List<CreditNote> list = new ArrayList<CreditNote>();
		Session session = getCurrentSession();

		try {
			Query query = session.createQuery("SELECT note from CreditNote note "
					+ "WHERE note.company.company_id =:company_id and note.flag =:flag and note.date>=:from_date and note.date<=:to_date "
					+ "order by note.credit_no_id asc");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			while (scrollableResults.next()) {
				list.add((CreditNote) scrollableResults.get()[0]);
				session.evict(scrollableResults.get()[0]);
			}
			session.clear();

		}

		catch (Exception e) {
			e.printStackTrace();

		}

		return list;
	}

	@Override
	public Integer getCancelCreditNoteGstList(LocalDate from_date, LocalDate to_date, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		criteria.add(Restrictions.ge("date", from_date));
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("flag", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.rowCount());
		return (int) (long) criteria.uniqueResult();
	}

	@Override
	public CreditNote isExcelVocherNumberExist(String vocherNo, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		criteria.add(Restrictions.eq("excel_voucher_no", vocherNo));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (CreditNote) criteria.uniqueResult();
	}
	
	@Override
	public List<CreditNote> getCreditNoteForSale(Long companyId,LocalDate toDate) {
		// TODO Auto-generated method stub
		
Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);


		Criterion criterion3 = Restrictions.eq("company.company_id", companyId);	
		Criterion criterion4 = Restrictions.le("date", toDate);
		criteria.add(Restrictions.and(criterion3,criterion4));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();

	}

	@Override
	public List<CreditNote> getAllOpeningBalanceAgainstcreditnote(Long customerId, Long companyId) {
		// TODO Auto-generated method stub
Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		
		Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		Criterion criterion2 = Restrictions.eq("customer.customer_id",customerId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	
		criteria.add(Restrictions.and(criterion1,criterion2,criterion3));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<CreditNote> getAllOpeningBalanceAgainstCreditNoteForPeriod(Long customerId, Long companyId,
			LocalDate toDate) {
		Criteria criteria = getCurrentSession().createCriteria(CreditNote.class);
		Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		Criterion criterion2=Restrictions.eq("flag", true);
		Criterion criterion3 = Restrictions.eq("customer.customer_id",customerId);
		Criterion criterion4 = Restrictions.eq("company.company_id",companyId);	
		Criterion criterion5=Restrictions.neOrIsNotNull("entry_status", MyAbstractController.ENTRY_CANCEL);
		Criterion criterion6 = Restrictions.le("date",toDate);	
		criteria.add(Restrictions.and(criterion1,criterion2,criterion3,criterion4,criterion5,criterion6));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();

	}

}
