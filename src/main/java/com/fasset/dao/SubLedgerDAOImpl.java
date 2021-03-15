/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Bank;
import com.fasset.entities.Ledger;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Transactional
@Repository
public class SubLedgerDAOImpl extends AbstractHibernateDao<SubLedger> implements ISubLedgerDAO {

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query queryop = session.createQuery("UPDATE SubLedger SET opening_id=null where subledger_Id =:id");
		queryop.setParameter("id", entityId);
		try {
			queryop.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Query queryb = session.createSQLQuery("delete from opening_balances where subLedger=:id");
		queryb.setParameter("id", entityId);
		try {
			queryb.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Query query = session.createQuery("delete from SubLedger where subledger_Id =:id");
		query.setParameter("id", entityId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "Sub Ledger Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete subLedger because it is releted to industry type";
		}
		return msg;
	}

	@Override
	public Long saveSubLedgerDao(SubLedger group) {

		Session session = getCurrentSession();
		group.setCreated_date(new LocalDate());
		Long ledgerid = group.getLedger_id();
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_id", ledgerid));
		Ledger ledger = (Ledger) criteria.uniqueResult();
		group.setLedger(ledger);
		Long id = (Long) session.save(group);
		session.flush();
		session.clear();
		return id;
	}

	@Transactional
	@Override
	public SubLedger findOne(Long id) throws MyWebException {
		
		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("subledger_Id", id));
		criteria.setFetchMode("ledger", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		return (SubLedger) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubLedger> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"from SubLedger where status=:status and flag=:flag and subledger_approval=:subledger_approval order by subledger_Id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		System.out.println(query.list());
		return query.list();
	}

	@Override
	public void update(SubLedger entity) throws MyWebException {

		Session session = getCurrentSession();
		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("subledger_Id", entity.getSubledger_Id()));
		SubLedger newEntity = (SubLedger) criteria.uniqueResult();
		/*
		 * if((newEntity.getSubledger_approval()==1) ||
		 * (newEntity.getSubledger_approval()==3))
		 */ newEntity.setSubledger_approval(entity.getSubledger_approval());
		newEntity.setUpdate_date(new LocalDate());
		newEntity.setSubledger_name(entity.getSubledger_name());
		newEntity.setCredit_opening_balance(entity.getCredit_opening_balance());
		newEntity.setDebit_opening_balance(entity.getDebit_opening_balance());
		newEntity.setStatus(entity.getStatus());
		newEntity.setFlag(true);
		newEntity.setSetDefault(entity.getSetDefault());
		newEntity.setUpdated_by(entity.getUpdated_by());
		Long ledgerid = entity.getLedger_id();
		Criteria criteria1 = getCurrentSession().createCriteria(Ledger.class);
		criteria1.add(Restrictions.eq("ledger_id", ledgerid));
		Ledger ledger = (Ledger) criteria1.uniqueResult();
		newEntity.setLedger(ledger);

		session.merge(newEntity);
		session.flush();
		session.clear();

	}

	@Override
	public SubLedger isExists(String name) {

		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("subledger_name", name));
		return (SubLedger) criteria.uniqueResult();
	}

	@Override
	public List<SubLedger> getSubledgerByGroup(String groupName, Long companyId) {

		Session session = getCurrentSession();
		Query query = session.createQuery(
				"from SubLedger where ledger.accsubgroup.accountGroup.group_name=:group_name and company.company_id=:companyId");
		query.setParameter("group_name", groupName);
		query.setParameter("companyId", companyId);
		List<SubLedger> result = (List<SubLedger>) query.list();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubLedger> findByStatus(int status) {
		/*
		 * Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		 * criteria.add(Restrictions.eq("subledger_approval", status));
		 * criteria.add(Restrictions.eq("flag", true));
		 * criteria.add(Restrictions.eq("status", true));
		 * criteria.addOrder(Order.desc("subledger_Id"));
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 * criteria.setFetchMode("company", FetchMode.JOIN);
		 * criteria.setFetchMode("ledger", FetchMode.JOIN);
		 * criteria.setFetchMode("accsubgroup", FetchMode.JOIN); return criteria.list();
		 */
		List<SubLedger> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"SELECT sub from SubLedger sub WHERE sub.subledger_approval=:subledger_approval and sub.flag=:flag and sub.status=:status order by sub.subledger_Id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("subledger_approval", status);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);

		while (scrollableResults.next()) {
			list.add((SubLedger) scrollableResults.get()[0]);
			session.evict(scrollableResults.get()[0]);
		}
		session.clear();
		return list;
	}

	@Override
	public String updateApprovalStatus(Long subLedgerId, int status) {
		Session session = getCurrentSession();
		Query query = session
				.createQuery("update SubLedger set subledger_approval =:status where subledger_Id =:subLedgerId");
		query.setParameter("subLedgerId", subLedgerId);
		query.setParameter("status", status);
		String msg = "";
		try {
			query.executeUpdate();
			if (status == 1) {
				msg = "Rejected successfully";
			} else {
				if (status == 2) {
					msg = "Sub Ledger Primary Approval Done, Sent For Secondary Approval ";
				} else {
					msg = "Sub Ledger Secondary Approval Done";

				}
			}
		} catch (Exception e) {
			msg = "You can't change status";
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubLedger> findAllSubLedgersOfCompany(Long company_id) {
		List<SubLedger> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"from SubLedger where status=:status and flag=:flag and subledger_approval=:subledger_approval and company.company_id=:company_id and setDefault IS NULL order by subledger_name asc");
		System.out.println(query);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		// query.setParameter("setDefault", IS NULL);
		query.setParameter("company_id", company_id);
		query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		list = query.list();
		session.clear();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubLedger> findAllSubLedgersOfCompanywithdefault(Long company_id) {
		/*
		 * Session session = getCurrentSession(); Query query = session.
		 * createQuery("from SubLedger where status=:status and flag=:flag and subledger_approval=:subledger_approval and company.company_id=:company_id order by subledger_Id desc"
		 * ); System.out.println(query); query.setParameter("status", true);
		 * query.setParameter("flag", true); //query.setParameter("setDefault", IS
		 * NULL); query.setParameter("company_id", company_id);
		 * query.setParameter("subledger_approval",
		 * MyAbstractController.APPROVAL_STATUS_SECONDARY); return query.list();
		 */
		List<SubLedger> list = new ArrayList<SubLedger>();
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"SELECT sub from SubLedger sub WHERE sub.company.company_id =:company_id and subledger_approval=:subledger_approval and status=:status and flag=:flag order by subledger_name");
		query.setParameter("company_id", company_id);
		query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		while (scrollableResults.next()) {
			list.add((SubLedger) scrollableResults.get()[0]);
			session.evict(scrollableResults.get()[0]);
		}
		/* Collections.sort(list,new comparator); */
		session.clear();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubLedger> findAllSubLedgersListing(Long flag) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery(
				"select s.subledger_Id, c.company_name,l.ledger_name,s.subledger_name,s.status,s.subledger_approval from subledger_master s,company c,ledger_master l where  s.company_id=c.company_id and s.ledger_id=l.ledger_id and s.flag=:flag \n"
						+ "union\n"
						+ "select s.subledger_Id, c.company_name,''  as ledger_name,s.subledger_name,s.status,s.subledger_approval from subledger_master s,company c  where  s.company_id=c.company_id and s.flag=:flag AND s.ledger_id IS NULL order by subledger_Id desc");
		if (flag == 1)
			query.setParameter("flag", true);
		else
			query.setParameter("flag", false);
		return query.list();
		/*
		 * Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		 * criteria.addOrder(Order.desc("subledger_Id")); if(flag==1)
		 * criteria.add(Restrictions.eq("flag", true)); else
		 * criteria.add(Restrictions.eq("flag", false));
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 * criteria.setFetchMode("company", FetchMode.JOIN);
		 * criteria.setFetchMode("ledger", FetchMode.JOIN); return criteria.list();
		 */
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubLedger> findAllSubLedgersListingOfCompany(Long CompanyId, Long flag) {
		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.addOrder(Order.desc("subledger_Id"));
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		if (flag == 1)
			criteria.add(Restrictions.eq("flag", true));
		else
			criteria.add(Restrictions.eq("flag", false));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("ledger", FetchMode.JOIN);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubLedger> findAllSubLedgersListingOfCompany1(Long CompanyId, Long flag) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery(
				"select s.subledger_Id, c.company_name,l.ledger_name,s.subledger_name,s.status,s.subledger_approval,s.allocated from subledger_master s,company c,ledger_master l where  s.company_id=c.company_id and s.ledger_id=l.ledger_id and s.flag=:flag and c.company_id=:company_id\n"
						+ "union\n"
						+ "select s.subledger_Id, c.company_name,'' as ledger_name,s.subledger_name,s.status,s.subledger_approval,s.allocated from subledger_master s,company c where  s.company_id=c.company_id and  s.flag=:flag and c.company_id=:company_id AND s.ledger_id IS NULL order by subledger_Id desc");
		if (flag == 1)
			query.setParameter("flag", true);
		else
			query.setParameter("flag", false);
		query.setParameter("company_id", CompanyId);
		return query.list();
	}

	@Override
	public SubLedger findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public SubLedger findOne(String id, Long company_id) {
		/*
		 * Session session = getCurrentSession(); Query query = session.
		 * createQuery("from SubLedger where subledger_name LIKE :term and company.company_id=:company_id"
		 * ); query.setParameter("term", id + "%"); query.setParameter("company_id",
		 * company_id); if(query.list()!=null) return (SubLedger) query.list().get(0);
		 * else return null;
		 */
		List result = null;
		//System.out.println("id and co id");
		result = getCurrentSession().createCriteria(SubLedger.class)
				.add(Restrictions.eq("company.company_id", company_id)).add(Restrictions.eq("subledger_name", id))
				.setMaxResults(1)
				.setProjection(Projections.projectionList().add(Projections.groupProperty("subledger_Id"))).list();

		if (result.size() > 0) {
			SubLedger sub = new SubLedger();
			sub.setSubledger_Id((long) result.get(0));
			/* return (SubLedger) criteria.uniqueResult(); */
			return sub;
		} else {
			return null;
		}

	}

	@Override
	public void create(SubLedger entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
		session.clear();
	}

	@Transactional
	@Override
	public Long createSubLedger(SubLedger entity) throws MyWebException {
		Session session = getCurrentSession();
		// session.save(entity);
		Long id = (Long) session.save(entity);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public void merge(SubLedger entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(SubLedger entity) throws MyWebException {
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
	public boolean validatePreTransaction(SubLedger entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(SubLedger entity) {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public void updateSubledger(SubLedger subLedger) {
		Session session = getCurrentSession();
		session.merge(subLedger);
		session.flush();
		session.clear();
	}

	@Override
	public List<SubLedger> findAllSubLedgerWithLedger() {

		List<SubLedger> list = new ArrayList<>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.addOrder(Order.asc("subledger_name"));
		criteria.add(Restrictions.isNull("setDefault"));
		criteria.add(Restrictions.eq("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("ledger", FetchMode.JOIN);
		list = criteria.list();
		session.clear();
		return list;
	}

	@Override
	public SubLedger findOneWithAll(Long subId) {
		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("subledger_Id", subId));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("ledger", FetchMode.JOIN);
		criteria.setFetchMode("accsubgroup", FetchMode.JOIN);
		criteria.setFetchMode("accountGroup", FetchMode.JOIN);
		criteria.setFetchMode("grouptype", FetchMode.JOIN);
		return (SubLedger) criteria.uniqueResult();
	}

	@Transactional
	@Override
	public SubLedger isExists(String name, Long company_id) {
		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("subledger_name", name));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		return (SubLedger) criteria.uniqueResult();
	}

	@Override
	public List<SubLedger> setdefaultdata() {
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"from SubLedger where status=:status and flag=:flag and setDefault=:setDefault and subledger_approval=:subledger_approval and company.company_id=:company_id order by subledger_Id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("setDefault", true);
		query.setParameter("company_id", MyAbstractController.SUPERUSER_COMPANY);
		query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		return query.list();
	}

	@Override
	public Boolean approvedByBatch(List<String> subledgerList, Boolean primaryApproval) {
		Session session = getCurrentSession();
		if (subledgerList.isEmpty()) {
			return false;
		} else {
			if (primaryApproval == true) {
				for (String id : subledgerList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery(
							"update SubLedger set subledger_approval=:subledger_approval where subledger_Id =:sid");
					query.setParameter("subledger_approval", 2);
					query.setParameter("sid", pid);
					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			} else if (primaryApproval == false) {
				for (String id : subledgerList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery(
							"update SubLedger set subledger_approval=:subledger_approval where subledger_Id =:sid");
					query.setParameter("subledger_approval", 3);
					query.setParameter("sid", pid);
					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			}
			return true;
		}
	}

	@Override
	public Boolean rejectByBatch(List<String> subledgerList) {
		Session session = getCurrentSession();
		if (subledgerList.isEmpty()) {
			return false;
		} else {
			for (String id : subledgerList) {
				Long pid = Long.parseLong(id);
				Query query = session.createQuery(
						"update SubLedger set subledger_approval=:subledger_approval where subledger_Id =:sid");
				query.setParameter("subledger_approval", 1);
				query.setParameter("sid", pid);
				try {
					query.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return true;
		}
	}

	@Override
	public int isExistssubledger(String subledger_name, Long company_id) {
		// TODO Auto-generated method stub

		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("subledger_name", subledger_name));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		if (criteria.list() == null || criteria.list().isEmpty())
			return 0;
		else
			return 1;
	}

	@Override
	public Set<SubLedger> findAllSubLedgerWithRespectToLedger(Long ledgerId) {
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_id", ledgerId));
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		Ledger ledger = (Ledger) criteria.uniqueResult();
		return ledger.getSubLedger();
	}

	@Override
	public List<SubLedger> findAllSubLedgersListing(Boolean flag, List<Long> companyIds) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery(
				"select s.subledger_Id, c.company_name,l.ledger_name,s.subledger_name,s.status,s.subledger_approval from subledger_master s,company c,ledger_master l where  s.company_id=c.company_id and s.ledger_id=l.ledger_id and s.flag=:flag and s.company_id in(:company_id) \n"
						+ "union\n"
						+ "select s.subledger_Id, c.company_name,'' as ledger_name,s.subledger_name,s.status,s.subledger_approval from subledger_master s,company c where  s.company_id=c.company_id and s.flag=:flag and s.company_id in(:company_id) AND s.ledger_id IS NULL order by subledger_Id desc");
		query.setParameterList("company_id", companyIds);
		query.setParameter("flag", true);

		return query.list();
		/*
		 * Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		 * criteria.addOrder(Order.desc("subledger_Id"));
		 * criteria.add(Restrictions.eq("flag", flag));
		 * criteria.add(Restrictions.in("company.company_id", companyIds));
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 * criteria.setFetchMode("company", FetchMode.JOIN);
		 * criteria.setFetchMode("ledger", FetchMode.JOIN); return criteria.list();
		 */
	}

	@Override
	public List<SubLedger> findByStatus(int status, List<Long> companyIds) {
		/*
		 * Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		 * criteria.add(Restrictions.eq("subledger_approval", status));
		 * criteria.add(Restrictions.eq("flag", true));
		 * criteria.add(Restrictions.eq("status", true));
		 * criteria.add(Restrictions.in("company.company_id", companyIds));
		 * criteria.addOrder(Order.desc("subledger_Id"));
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 * criteria.setFetchMode("company", FetchMode.JOIN);
		 * criteria.setFetchMode("ledger", FetchMode.JOIN);
		 * criteria.setFetchMode("accsubgroup", FetchMode.JOIN); return criteria.list();
		 */
		List<SubLedger> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"SELECT sub from SubLedger sub WHERE sub.subledger_approval=:subledger_approval and sub.flag=:flag and sub.status=:status and sub.company.company_id in (:company_id) order by sub.subledger_Id desc");
		query.setParameterList("company_id", companyIds);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("subledger_approval", status);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);

		while (scrollableResults.next()) {
			list.add((SubLedger) scrollableResults.get()[0]);
			session.evict(scrollableResults.get()[0]);
		}
		session.clear();
		return list;

	}

	@Override
	public List<SubLedger> findAllSubLedgerWithLedger(List<Long> companyIds) {

		List<SubLedger> list = new ArrayList<SubLedger>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.add(Restrictions.isNull("setDefault"));
		criteria.addOrder(Order.asc("subledger_name"));
		criteria.add(Restrictions.eq("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("ledger", FetchMode.JOIN);
		list = criteria.list();
		session.clear();
		return list;
	}

	@Override
	public List<SubLedger> findAllSubLedgersOnlyOfCompany(Long CompanyId) {
		/*
		 * Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		 * criteria.add(Restrictions.eq("company.company_id", CompanyId));
		 * criteria.add(Restrictions.eq("status", true));
		 * criteria.add(Restrictions.eq("flag", true));
		 * criteria.addOrder(Order.desc("subledger_Id"));
		 * criteria.add(Restrictions.eq("subledger_approval",
		 * MyAbstractController.APPROVAL_STATUS_SECONDARY));
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); return
		 * criteria.list();
		 */
		System.out.println("The company id is " +CompanyId );
		List<SubLedger> list = new ArrayList<SubLedger>();
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"SELECT subLedger from SubLedger subLedger WHERE subLedger.company.company_id =:company_id and subLedger.subledger_approval=:subledger_approval and subLedger.status=:status and subLedger.flag=:flag");
		query.setParameter("company_id", CompanyId);
		query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		while (scrollableResults.next()) {
			list.add((SubLedger) scrollableResults.get()[0]);
			session.evict(scrollableResults.get()[0]);
		}
		session.clear();
		
		/* Collections.sort(list,new comparator); */
		return list;
	}

	@Override
	public List<SubLedger> findAllOPbalanceofsubledger(long company_id, long flag) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createSQLQuery(
				"select sum( `credit_balance` ) , sum( `debit_balance` ) , `subLedger`  from opening_balances where company_id=:company_id and balanceType=:balanceType group by subLedger");
		query.setParameter("company_id", company_id);
		query.setParameter("balanceType", 2);
		return query.list();
	}

	@Override
	public void updateExcelSubLedger(SubLedger entity) {

		Session session = getCurrentSession();
		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("subledger_Id", entity.getSubledger_Id()));
		SubLedger newEntity = (SubLedger) criteria.uniqueResult();
		/*
		 * if((newEntity.getSubledger_approval()==1) ||
		 * (newEntity.getSubledger_approval()==3))
		 */ newEntity.setSubledger_approval(entity.getSubledger_approval());
		newEntity.setUpdate_date(new LocalDate());
		newEntity.setSubledger_name(entity.getSubledger_name());
		newEntity.setCredit_opening_balance(entity.getCredit_opening_balance());
		newEntity.setDebit_opening_balance(entity.getDebit_opening_balance());
		newEntity.setStatus(entity.getStatus());
		newEntity.setFlag(entity.getFlag());
		newEntity.setSetDefault(entity.getSetDefault());
		newEntity.setUpdated_by(entity.getUpdated_by());
		Long ledgerid = entity.getLedger_id();
		Criteria criteria1 = getCurrentSession().createCriteria(Ledger.class);
		criteria1.add(Restrictions.eq("ledger_id", ledgerid));
		Ledger ledger = (Ledger) criteria1.uniqueResult();
		newEntity.setLedger(ledger);

		session.merge(newEntity);
		session.flush();
		session.clear();
	}

	@Override
	public Integer findAllDashboard() {

		List<Integer> list = new ArrayList<Integer>();
		list.add(MyAbstractController.APPROVAL_STATUS_PENDING);
		list.add(MyAbstractController.APPROVAL_STATUS_PRIMARY);

		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.in("subledger_approval", list));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.rowCount());
		return (int) (long) criteria.uniqueResult();
	}

	@Override
	public Integer findAllSubLedgerOfCompanyDashboard(Long CompanyId) {

		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		criteria.add(Restrictions.eq("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.rowCount());
		return (int) (long) criteria.uniqueResult();
	}

	@Override
	public Integer findByStatusDashboard(int approvalstatus, List<Long> companyIds) {
		Criteria criteria = getCurrentSession().createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("subledger_approval", approvalstatus));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setProjection(Projections.rowCount());
		return (int) (long) criteria.uniqueResult();
	}

	@Override
	public Set<SubLedger> findAllSubLedgersForDepreciation(Long company_id) {

		Set <SubLedger> list = new HashSet<SubLedger>();
		
		List<Ledger> ledgerlist = new ArrayList<Ledger>();
		Session session = getCurrentSession();
		Query query = session.createQuery(
					"SELECT ledger  FROM  Ledger ledger LEFT JOIN FETCH ledger.subLedger subLedger  WHERE ledger.company.company_id=:company_id"
					+ " and ledger.accsubgroup.subgroup_name IN('Fixed Assets - Tangible Assets','Fixed Assets - Intangible Assets')");
			query.setParameter("company_id", company_id);
		
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
			  ledgerlist.add((Ledger)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
        session.clear();
    
		for(Ledger ledger :ledgerlist )
		{
			for(SubLedger sub :ledger.getSubLedger()) 
			{
				list.add(sub);
			}
		}
		return list;
	}

	@Override
	public Ledger subLedgerOfNameDepreciaition(Long company_id) {
		Session session = getCurrentSession();
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("company.company_id", company_id));
		criteria.add(Restrictions.eq("subLedger.subledger_name", "Depreciation"));
		return (Ledger) criteria.uniqueResult();
		
		
	}

	@Override
	public List<SubLedger> findAllSubLedgersofSuppliers(Long CompanyId) {
		System.out.println("new query for suppliers");
		List<SubLedger> list = new ArrayList<SubLedger>();
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"SELECT subLedger from SubLedger subLedger inner join fetch subLedger.supplier su "
+" WHERE subLedger.company.company_id =:company_id and subLedger.subledger_approval=:subledger_approval and subLedger.status=:status and subLedger.flag=:flag");
		query.setParameter("company_id", CompanyId);
		query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		while (scrollableResults.next()) {
			list.add((SubLedger) scrollableResults.get()[0]);
			session.evict(scrollableResults.get()[0]);
		}
		System.out.println("thecount is "+list.size());
		
		session.clear();
		
		/* Collections.sort(list,new comparator); */
		return list;
	}

	@Override
	public List<SubLedger> findAllApprovedByCompanyForCustomer(Long CompanyId) {
		// TODO Auto-generated method stub
		
		List<SubLedger> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"from SubLedger where status=:status and flag=:flag and subledger_approval=:subledger_approval and company.company_id=:company_id and ledger.accsubgroup.accountGroup.postingSide.posting_id in(2,3) and setDefault IS NULL order by subledger_name asc");
		System.out.println(query);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		// query.setParameter("setDefault", IS NULL);
		query.setParameter("company_id", CompanyId);
		query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		list = query.list();
		session.clear();
		return list;
		
	}

	@Override
	public List<SubLedger> findAllApprovedByCompanyForSupplier(Long CompanyId) {
		// TODO Auto-generated method stub
		List<SubLedger> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery(
				"from SubLedger where status=:status and flag=:flag and subledger_approval=:subledger_approval and company.company_id=:company_id and ledger.accsubgroup.accountGroup.postingSide.posting_id in(1,3) and setDefault IS NULL order by subledger_name asc");
		System.out.println(query);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		// query.setParameter("setDefault", IS NULL);
		query.setParameter("company_id", CompanyId);
		query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		list = query.list();
		session.clear();
		return list;
	}

	@Override
	public List<SubLedger> findAllSubLedgerWithLedgerForCust(List<Long> companyIds,boolean IsSuperUser) {
		// TODO Auto-generated method stub
		List<SubLedger> list = new ArrayList<>();
		Session session = getCurrentSession();
		System.out.println("companyIds are "+companyIds);
		if(IsSuperUser){
			System.out.println("super");
		Query query = session.createQuery(
				"from SubLedger where status=:status and flag=:flag and subledger_approval=:subledger_approval  and ledger.accsubgroup in(15,16,17,26,27,28,94,95,99,52,53,82) and setDefault IS NULL order by subledger_name asc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		
		// query.setParameter("setDefault", IS NULL);
		//query.setParameter("company_id", companyIds);
		query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		list = query.list();
		}else
		{
			System.out.println("not super");
			Query query = session.createQuery(
					"from SubLedger where status=:status and flag=:flag and subledger_approval=:subledger_approval and company.company_id in (:company_id) and ledger.accsubgroup in(15,16,17,26,27,28,94,95,99,52,53,82) and setDefault IS NULL order by subledger_name asc");
			query.setParameter("status", true);
			query.setParameter("flag", true);
			// query.setParameter("setDefault", IS NULL);
			query.setParameterList("company_id", companyIds);
			query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
			list = query.list();
		}
		
		session.clear();
		return list;
	}
	
	@Override
	public List<SubLedger> findAllSubLedgerWithLedgerForSuppl(List<Long> companyIds, boolean IsSuperUser) {
		// TODO Auto-generated method stub
		List<SubLedger> list = new ArrayList<>();
		Session session = getCurrentSession();
		if(IsSuperUser){
			System.out.println(" super");
		Query query = session.createQuery(
				"from SubLedger where status=:status and flag=:flag and subledger_approval=:subledger_approval  and ledger.accsubgroup in(15,16,17,26,31,32,34,35,36,37,50,52,53,61,82,86,87,88,89,92) and setDefault IS NULL order by subledger_name asc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		// query.setParameter("setDefault", IS NULL);
		//query.setParameter("company_id", companyIds);
		query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		list = query.list();
		}else
		{
			System.out.println("non super");
			Query query = session.createQuery(
					"from SubLedger where status=:status and flag=:flag and subledger_approval=:subledger_approval and company.company_id in (:company_id) and ledger.accsubgroup in(15,16,17,26,31,32,34,35,36,37,50,52,53,61,82,86,87,88,89,92) and setDefault IS NULL order by subledger_name asc");
			query.setParameter("status", true);
			query.setParameter("flag", true);
			// query.setParameter("setDefault", IS NULL);
			if(!companyIds.isEmpty()){
			query.setParameterList("company_id", companyIds);}else{
				
			}
			query.setParameter("subledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
			list = query.list();
		}
		System.out.println("the list size is " +list.size());
		session.clear();
		return list;
	}

}