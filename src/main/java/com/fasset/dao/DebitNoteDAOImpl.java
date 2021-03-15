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
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IDebitNoteDAO;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitDetails;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Payment;
import com.fasset.exceptions.MyWebException;

@Repository
@Transactional
public class DebitNoteDAOImpl extends AbstractHibernateDao<DebitNote> implements IDebitNoteDAO {

	@Autowired
	private IOpeningBalancesDAO balanceDao;

	@Override
	public DebitNote findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		criteria.add(Restrictions.eq("debit_no_id", id));
		return (DebitNote) criteria.uniqueResult();
	}

	@Override
	public DebitNote findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DebitNote findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DebitNote> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("debit_no_id"));
		return criteria.list();
	}

	@Override
	public void create(DebitNote entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
		session.clear();
	}

	@Override
	public void update(DebitNote entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
		session.clear();
	}

	@Override
	public void merge(DebitNote entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(DebitNote entity) throws MyWebException {
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
	public boolean validatePreTransaction(DebitNote entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(DebitNote entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public DebitNote isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DebitNote> getReport(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {

		/*
		 * Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		 * criteria.add(Restrictions.ge("date", from_date));
		 * criteria.add(Restrictions.le("date", to_date));
		 * criteria.add(Restrictions.ne("entry_status",
		 * MyAbstractController.ENTRY_CANCEL));
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 * criteria.add(Restrictions.eq("company.company_id", comId));
		 * criteria.add(Restrictions.eq("flag", true)); if(Id > 0){
		 * criteria.add(Restrictions.eq("supplier.supplier_id", Id)); }
		 * criteria.setFetchMode("supplier", FetchMode.JOIN);
		 * criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
		 * criteria.setFetchMode("subledger", FetchMode.JOIN);
		 * criteria.setFetchMode("debitDetails", FetchMode.JOIN); return
		 * criteria.list();
		 */
		List<DebitNote> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = null;
		if (Id != null) {

			if (Id > 0) {
				query = session.createQuery("SELECT note from DebitNote note "
						+ "WHERE note.flag=:flag and note.company.company_id =:company_id and note.supplier.supplier_id =:supplier_id and note.entry_status !=:entry_status and note.date >=:date1 and note.date <=:date2 ORDER by note.date DESC,note.debit_no_id DESC");
				query.setParameter("supplier_id", Id);
			} else {
				query = session.createQuery("SELECT note from DebitNote note "
						+ "WHERE note.flag=:flag and note.company.company_id =:company_id and note.entry_status !=:entry_status and note.date >=:date1 and note.date <=:date2 ORDER by note.date DESC,note.debit_no_id DESC");

			}
			query.setParameter("company_id", comId);
			query.setParameter("flag", true);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			query.setParameter("date1", from_date);
			query.setParameter("date2", to_date);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);

			while (scrollableResults.next()) {
				list.add((DebitNote) scrollableResults.get()[0]);
				session.evict(scrollableResults.get()[0]);
			}
			session.clear();
		}
		return list;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query1 = session.createQuery("delete from DebitDetails where debit_id.debit_no_id =:id");
		query1.setParameter("id", entityId);
		query1.executeUpdate();
		balanceDao.deleteOpeningBalance(null, null, null, null, null, entityId, null, null, null, null, null, null);
		Query query = session.createQuery("delete from DebitNote where debit_no_id =:id");
		query.setParameter("id", entityId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "Debit Note Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete Debit Note";
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DebitNote> findAllDebitNoteOfCompany(Long CompanyId) {
		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("debit_no_id"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DebitNote> findByPurchaseId(Long purchaseId) {
		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		criteria.add(Restrictions.eq("purchase_bill_id.purchase_id", purchaseId));
		criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DebitNote> cdnrList(Integer month, Long yearId, Long companyId) {

		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		criteria.createAlias("supplier", "sup");
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.sqlRestriction("MONTH(this_.date) = " + month),
				Restrictions.eq("accounting_year_id.year_id", yearId), Restrictions.eq("sup.gst_applicable", true),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("debitDetails", FetchMode.JOIN);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DebitNote> cdnurList(Integer month, Long yearId, Long companyId) {

		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		criteria.createAlias("supplier", "sup");
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.sqlRestriction("MONTH(this_.date) = " + month),
				Restrictions.eq("accounting_year_id.year_id", yearId), Restrictions.eq("sup.gst_applicable", false),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("debitDetails", FetchMode.JOIN);
		return criteria.list();
	}

	@Override
	public String deleteByDebitNoteDetailsId(Long entityId) {
		Session session = getCurrentSession();

		// Criteria criteria = session.createCriteria(DebitDetails.class);
		// criteria.add(Restrictions.eq("debit_detail_id", entityId));
		Query query = session.createQuery("delete from DebitDetails where debit_detail_id =:id");
		query.setParameter("id", entityId);

		String msg = "";
		try {
			query.executeUpdate();
			msg = "Debit Note Detail Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete Debit Note Detail";
		}
		return msg;
	}

	@Override
	public Integer getCountByYear(Long yearId, Long companyId, String range) {
		/*
		 * Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		 * criteria.add(Restrictions.eq("company.company_id", companyId));
		 * criteria.add(Restrictions.eq("accounting_year_id.year_id", yearId));
		 * criteria.setProjection(Projections.rowCount()); return
		 * Integer.parseInt(criteria.uniqueResult().toString());
		 */
		Session session = getCurrentSession();
		Query query = session.createSQLQuery(
				"select count(debit_no_id)from debit_note where company_id =:company_id and accounting_year_id =:year_id and voucher_no LIKE :term");
		query.setParameter("company_id", companyId);
		query.setParameter("year_id", yearId);
		query.setParameter("term", range + "%");
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
		}
	}

	@Override
	public DebitNote findOneView(Long id) {
		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		criteria.add(Restrictions.eq("debit_no_id", id));
		criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("debitDetails", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		return (DebitNote) criteria.uniqueResult();
	}

	@Override
	public List<DebitNote> getReportAgainstSubledger(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {

		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		criteria.add(Restrictions.ge("date", from_date));
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("company.company_id", comId));
		criteria.add(Restrictions.eq("flag", true));
		criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
		return criteria.list();
	}

	@Override
	public void saveDebitNote(DebitNote note) {
		Session session = getCurrentSession();
		session.save(note);
		session.flush();
		session.clear();
	}

	@Override
	public int getCountByDate(Long companyId, String range, LocalDate date) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();

		Criteria criteria = session.createCriteria(DebitNote.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("date", date));
		criteria.addOrder(Order.desc("debit_no_id"));
		criteria.setMaxResults(1);
		DebitNote entry = (DebitNote) criteria.uniqueResult();
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
		 * createSQLQuery("select count(debit_no_id)from debit_note where company_id =:company_id and date ='"
		 * +date+"'"); query.setParameter("company_id", companyId);
		 * System.out.println(query.list()); if(query.list()==null ||
		 * query.list().isEmpty()) { return 0; } else {
		 * System.out.println(query.list().get(0)); if(query.list().get(0)==null) return
		 * 0; else { Integer vid=((BigInteger) query.list().get(0)).intValue();
		 * System.out.println(vid); return vid; } }
		 */
	}

	@Override
	public List<DebitNote> getDayBookReport(LocalDate from_date, LocalDate to_date, Long companyId) {
		/*
		 * Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		 * criteria.add(Restrictions.ge("date", from_date));
		 * criteria.add(Restrictions.le("date", to_date));
		 * criteria.add(Restrictions.eq("company.company_id", companyId));
		 * criteria.add(Restrictions.ne("entry_status",
		 * MyAbstractController.ENTRY_CANCEL)); criteria.add(Restrictions.eq("flag",
		 * true)); criteria.addOrder(Order.desc("date"));
		 * criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
		 * criteria.setFetchMode("supplier", FetchMode.JOIN);
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); return
		 * criteria.list();
		 */
		List<DebitNote> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT note from DebitNote note "
				+ "WHERE note.flag=:flag and note.company.company_id =:company_id and note.entry_status !=:entry_status and note.date >=:date1 and note.date <=:date2 order by note.date desc");
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		query.setParameter("date1", from_date);
		query.setParameter("date2", to_date);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);

		while (scrollableResults.next()) {
			list.add((DebitNote) scrollableResults.get()[0]);
			session.evict(scrollableResults.get()[0]);
		}
		session.clear();

		return list;
	}

	@Override
	public void updateDebitNoteThroughExcel(DebitNote note) {
		Session session = getCurrentSession();
		session.merge(note);
		session.flush();
		session.clear();
	}

	@Override
	public void updateDebitDetailsThroughExcel(DebitDetails entity, Long debit_id) {
		Session session = getCurrentSession();

		Criteria criteria = session.createCriteria(DebitNote.class);
		criteria.add(Restrictions.eq("debit_no_id", debit_id));

		DebitNote note = (DebitNote) criteria.uniqueResult();

		entity.setDebit_id(note);
		session.save(entity);
		session.flush();
		session.clear();
	}

	@Override
	public Long saveDebitNoteThroughExcel(DebitNote note) {
		Session session = getCurrentSession();
		Long id = (Long) session.save(note);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public List<DebitNote> findAllDebitNotesOfCompany(Long CompanyId, Boolean flag) {
		/*Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		criteria.add(Restrictions.eq("flag", flag));
		criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("debit_no_id"));
		return criteria.list();*/
		
		List<DebitNote> list =  new ArrayList<DebitNote>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT note from DebitNote note LEFT JOIN FETCH note.company company "
				+"LEFT JOIN FETCH note.purchase_bill_id purchase_bill_id "
				+"WHERE note.company.company_id =:company_id and note.flag =:flag  ORDER by note.date DESC,note.debit_no_id DESC");
		query.setParameter("company_id", CompanyId);
		//query.setParameter("yearID",yearID);// and note.accounting_year_id.year_id=:yearID
		query.setParameter("flag", flag);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((DebitNote)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  /*Collections.sort(list, new Comparator<DebitNote>() {
	            public int compare(DebitNote pay1, DebitNote pay2) {
	                Long debit_no_id1 = new Long(pay1.getDebit_no_id());
	                Long debit_no_id2 = new Long(pay2.getDebit_no_id());
	                return debit_no_id2.compareTo(debit_no_id1);
	            }
	        });*/
		  return list;
	}
	/*
	 * @Override public List<DebitNote> findAllDebitNotesOfCompany(Long CompanyId,
	 * Boolean flag,Long yearID) { Criteria criteria =
	 * getCurrentSession().createCriteria(DebitNote.class);
	 * criteria.add(Restrictions.eq("company.company_id", CompanyId));
	 * criteria.add(Restrictions.eq("flag", flag));
	 * criteria.setFetchMode("purchase_bill_id", FetchMode.JOIN);
	 * criteria.setFetchMode("supplier", FetchMode.JOIN);
	 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	 * criteria.addOrder(Order.desc("debit_no_id")); return criteria.list();
	 * 
	 * List<DebitNote> list = new ArrayList<DebitNote>(); Session session =
	 * getCurrentSession(); Query query = session.
	 * createQuery("SELECT note from DebitNote note LEFT JOIN FETCH note.company company "
	 * +"LEFT JOIN FETCH note.purchase_bill_id purchase_bill_id "
	 * +"WHERE note.company.company_id =:company_id and note.flag =:flag and note.accounting_year_id.year_id=:yearID ORDER by note.date DESC,note.debit_no_id DESC"
	 * ); query.setParameter("company_id", CompanyId);
	 * query.setParameter("yearID",yearID); query.setParameter("flag", flag);
	 * ScrollableResults scrollableResults =
	 * query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY); while
	 * (scrollableResults.next()) { list.add((DebitNote)scrollableResults.get()[0]);
	 * session.evict(scrollableResults.get()[0]); } session.clear();
	 * Collections.sort(list, new Comparator<DebitNote>() { public int
	 * compare(DebitNote pay1, DebitNote pay2) { Long debit_no_id1 = new
	 * Long(pay1.getDebit_no_id()); Long debit_no_id2 = new
	 * Long(pay2.getDebit_no_id()); return debit_no_id2.compareTo(debit_no_id1); }
	 * }); return list; }
	 */

	@Override
	public DebitDetails getDebitDetailsById(Long debitDetailId) {
		Criteria criteria = getCurrentSession().createCriteria(DebitDetails.class);
		criteria.add(Restrictions.eq("debit_detail_id", debitDetailId));
		return (DebitDetails) criteria.uniqueResult();
	}

	@Override
	public void updateDebitDetail(DebitDetails debitDetails) {
		Session session = getCurrentSession();
		session.merge(debitDetails);
		session.flush();
		session.clear();
	}

	@Override
	public List<DebitNote> findAllDebitNotesOnlyOfCompany(Long CompanyId) {
		List<DebitNote> list = new ArrayList<DebitNote>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT debit from DebitNote debit LEFT JOIN FETCH debit.company company "
				+ "WHERE debit.company.company_id =:company_id and excel_voucher_no IS NOT NULL");
		query.setParameter("company_id", CompanyId);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		while (scrollableResults.next()) {
			list.add((DebitNote) scrollableResults.get()[0]);
			session.evict(scrollableResults.get()[0]);
		}
		Collections.sort(list, new Comparator<DebitNote>() {
			public int compare(DebitNote pay1, DebitNote pay2) {
				Long debit_no_id1 = new Long(pay1.getDebit_no_id());
				Long debit_no_id2 = new Long(pay2.getDebit_no_id());
				return debit_no_id2.compareTo(debit_no_id1);
			}
		});
		return list;
	}

	@Override
	public void changeStatusOfDebitNoteThroughExcel(DebitNote note) {
		Session session = getCurrentSession();
		note.setFlag(false);
		session.merge(note);
		session.flush();
		session.clear();
	}

	@Override
	public void diactivateByIdValue(Long debit_no_id) {
		// Session session = getCurrentSession();
		Session session = getCurrentSession();
		Query query = session.createQuery("update DebitNote set entry_status=:entry_status where debit_no_id =:id");
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		query.setParameter("id", debit_no_id);
		try {
			query.executeUpdate();
		} catch (Exception e) {
		}
	}

	@Override
	public List<DebitNote> getDebitNoteForLedgerReport(LocalDate from_date, LocalDate to_date, Long suppilerId,
			Long companyId) {
		List<DebitNote> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = null;
		if (suppilerId != null) {

			if (suppilerId.equals((long) -2)|| suppilerId==0) {
				query = session.createQuery("SELECT note from DebitNote note "
						+ "WHERE note.flag=:flag and note.company.company_id =:company_id and note.entry_status !=:entry_status  and note.date <=:date2");
			} else {
				query = session.createQuery("SELECT note from DebitNote note "
						+ "WHERE note.flag=:flag and note.company.company_id =:company_id and note.supplier.supplier_id =:supplier_id and note.entry_status !=:entry_status  and note.date <=:date2");
				query.setParameter("supplier_id", suppilerId);
			}
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			//query.setParameter("date1", from_date);
			query.setParameter("date2", to_date);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);

			while (scrollableResults.next()) {
				list.add((DebitNote) scrollableResults.get()[0]);
				session.evict(scrollableResults.get()[0]);
			}
			session.clear();
		}
		return list;
	}

	@Override
	public List<DebitNote> getDebitNoteGstList(LocalDate from_date, LocalDate to_date, Long companyId) {
		List<DebitNote> list = new ArrayList<DebitNote>();
		Session session = getCurrentSession();

		try {
			Query query = session.createQuery("SELECT note from DebitNote note "
					+ "WHERE note.company.company_id =:company_id and note.flag =:flag and note.date>=:from_date and note.date<=:to_date "
					+ "order by note.debit_no_id asc");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			while (scrollableResults.next()) {
				list.add((DebitNote) scrollableResults.get()[0]);
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
	public Integer getCancelDebitNoteGstList(LocalDate from_date, LocalDate to_date, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
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
	public DebitNote isExcelVocherNumberExist(String vocherNo, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		criteria.add(Restrictions.eq("excel_voucher_no", vocherNo));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (DebitNote) criteria.uniqueResult();
	}

	@Override
	public List<DebitNote> getDebitNoteNoteForPurchase(Long companyId, LocalDate toDate) {
Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		
		
		Criterion criterion3 = Restrictions.eq("company.company_id", companyId);	
		Criterion criterion4 = Restrictions.le("date", toDate);
		criteria.add(Restrictions.and(criterion3,criterion4));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

	@Override
	public List<DebitNote> getAllOpeningBalanceAgainstDebitNote(Long supplierId, Long companyId) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
		Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		Criterion criterion2 = Restrictions.eq("supplier.supplier_id",supplierId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	

		criteria.add(Restrictions.and(criterion1,criterion2,criterion3));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<DebitNote> getAllOpeningBalanceAgainstDebitNoteForPeriod(Long supplierId, Long companyId,
			LocalDate todate) {
		// TODO Auto-generated method stub
				Criteria criteria = getCurrentSession().createCriteria(DebitNote.class);
				Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
				Criterion criterion2 = Restrictions.eq("supplier.supplier_id",supplierId);
				Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	
				Criterion criterion4 = Restrictions.le("date",todate);
				criteria.add(Restrictions.and(criterion1,criterion2,criterion3,criterion4));
				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				return criteria.list();
	}

}
