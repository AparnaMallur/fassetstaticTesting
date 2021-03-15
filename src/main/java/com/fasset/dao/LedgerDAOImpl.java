/**
 * mayur suramwar
 */
package com.fasset.dao;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.ILedgerDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Ledger;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.LedgerReport;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Transactional
@Repository
public class LedgerDAOImpl extends AbstractHibernateDao<Ledger> implements ILedgerDAO {

	@Override
	public Long saveLedgerDao(Ledger ledger) {
		Session session = getCurrentSession();
		Long id =(Long) session.save(ledger);
		session.flush();
	    session.clear();
	    return id;
	}

	@Transactional
	@Override
	public Ledger findOne(Long id) throws MyWebException {
		Session session = getCurrentSession();
		Ledger ledger = new Ledger();
		Criteria criteria = session.createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_id", id));
		criteria.setFetchMode("company", FetchMode.JOIN);
		ledger= (Ledger) criteria.uniqueResult();
		session.clear();
		return ledger;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.asc("ledger_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		return criteria.list();
	}

	@Transactional
	@Override
	public void update(Ledger entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query queryop = session.createQuery("UPDATE Ledger SET opening_id=null where ledger_id =:id");
		queryop.setParameter("id", entityId);
		try {
			queryop.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Query queryb = session.createQuery("delete from OpeningBalances where ledger_id=:id");
		queryb.setParameter("id", entityId);
		try {
			queryb.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Query query = session.createQuery("delete from Ledger where ledger_id =:id");
		query.setParameter("id", entityId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "Ledger Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete ledger because it is releted to other subledgers";
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> findByStatus(int status) {
		/*Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_approval", status));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.addOrder(Order.desc("ledger_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("accsubgroup", FetchMode.JOIN);
		return criteria.list();*/
		List<Ledger> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT ledger from Ledger ledger WHERE ledger.ledger_approval=:ledger_approval and ledger.flag=:flag and ledger.status=:status order by ledger.ledger_id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("ledger_approval", status);
	ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
	
	  while (scrollableResults.next()) {
		  list.add((Ledger)scrollableResults.get()[0]);
		  session.evict(scrollableResults.get()[0]);
         }
	  session.clear();
	  return list;
	}

	@Override
	public String updateApprovalStatus(Long ledgerId, int status) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update Ledger set ledger_approval =:status where ledger_id =:ledgerId");
		query.setParameter("ledgerId", ledgerId);
		query.setParameter("status", status);
		String msg = "";
		try {
			query.executeUpdate();
			if (status == 1)
				msg = "Rejected successfully";
			else if (status == 2)
				msg = "Ledger Primary Approval Done, Sent For Secondary Approval ";
			else
				msg = "Ledger Secondary Approval Done";
		} catch (Exception e) {
			msg = "You can't change status";
		}
		return msg;
	}

	@Override
	public Ledger isExists(String name) {
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_name", name));
		return (Ledger) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> findAllLedgersOfCompany(Long CompanyId) {
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.addOrder(Order.asc("ledger_name"));
		criteria.add(Restrictions.eq("ledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public List<LedgerReport> getLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId,
			Long customerId,Long supplierId) {
		List<LedgerReport> ledgerReports = new ArrayList<LedgerReport>();
		List<SalesEntry> saleslist = new ArrayList<SalesEntry>();
		List<PurchaseEntry> purchaselist = new ArrayList<PurchaseEntry>();
        if(customerId!=null) {
			
			if(customerId.equals((long)-1))
			{
				saleslist.clear();
				Session session = getCurrentSession();
				Query query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
						+ "WHERE salesEntry.flag=:flag and salesEntry.company.company_id =:company_id and salesEntry.entry_status !=:entry_status and salesEntry.created_date >=:created_date1 and salesEntry.created_date <=:created_date2 order by salesEntry.created_date ASC,salesEntry.sales_id ASC ");
				query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("created_date1", from_date);
				query.setParameter("created_date2", to_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  saleslist.add((SalesEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				  
			}
			else
			{
				saleslist.clear();
				Session session = getCurrentSession();
				Query query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
						+ "WHERE salesEntry.flag=:flag and salesEntry.company.company_id =:company_id and salesEntry.customer.customer_id =:customer_id and salesEntry.entry_status !=:entry_status and salesEntry.created_date >=:created_date1 and salesEntry.created_date <=:created_date2 order by salesEntry.created_date ASC,salesEntry.sales_id ASC");
				query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("customer_id", customerId);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("created_date1", from_date);
				query.setParameter("created_date2", to_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  saleslist.add((SalesEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
			}
			
			if(saleslist.size()>0)
			{
			for(SalesEntry entry :saleslist)
			{
				LedgerReport ledgerReport = new LedgerReport();
				ledgerReport.setType("Sales");
				if(entry.getTransaction_value()!=null)
				{
				ledgerReport.setTransaction_value(entry.getTransaction_value().toString());
				}
				if(entry.getVoucher_no()!=null)
				{
				ledgerReport.setVoucher_no(entry.getVoucher_no());
				}
				if(entry.getCreated_date()!=null)
				{
					ledgerReport.setCreated_date(entry.getCreated_date());
				}
				if(entry.getCgst()!=null)
				{
				ledgerReport.setCgst(entry.getCgst().toString());
				}
				if(entry.getIgst()!=null)
				{
				ledgerReport.setIgst(entry.getIgst().toString());
				}
				if(entry.getSgst()!=null)
				{
				ledgerReport.setSgst(entry.getSgst().toString());
				}
				if(entry.getRound_off()!=null)
				{
				ledgerReport.setRound_off(entry.getRound_off().toString());
				}
				ledgerReport.setPrimary_id(entry.getSales_id().toString());
				if(entry.getCustomer()!=null)
				{
				ledgerReport.setCustomer_name_id(entry.getCustomer().getCustomer_id().toString());
				}
				else
				{
					ledgerReport.setCustomer_name_id("0");
				}
				if(entry.getState_compansation_tax()!=null)
				{
				ledgerReport.setSct(entry.getState_compansation_tax().toString());
				}
				if(entry.getTotal_excise()!=null)
				{
				ledgerReport.setTotal_excise(entry.getTotal_excise().toString());
				}
				if(entry.getTotal_vat()!=null)
				{
				ledgerReport.setTotal_vat(entry.getTotal_vat().toString());
				}
				if(entry.getTotal_vatcst()!=null)
				{
				ledgerReport.setTotal_vatcst(entry.getTotal_vatcst().toString());
				}
				if(entry.getEntry_status()!=null)
				{
				ledgerReport.setEntry_status(entry.getEntry_status().toString());
				}
				if(entry.getFlag()!=null)
				{
			    ledgerReport.setFlag(entry.getFlag().toString());
				}
				if(entry.getSale_type()!=null)
				{
				ledgerReport.setSale_type(entry.getSale_type().toString());
				}
				if(entry.getBank()!=null)
				{
			    ledgerReport.setBank_id(entry.getBank().getBank_id().toString());
				}
				if(entry.getSubledger()!=null)
				{
			    ledgerReport.setSubledger_id(entry.getSubledger().getSubledger_Id().toString());
				}
				ledgerReports.add(ledgerReport);
			}
			}
			else // for advance receipt with salesid= null
			{
				LedgerReport ledgerReport = new LedgerReport();
				ledgerReport.setType("Sales");
				ledgerReports.add(ledgerReport);
			}
	}
		
           if(supplierId!=null) {
			
			if(supplierId.equals((long)-2))
			{
				purchaselist.clear();
				Session session = getCurrentSession();
				Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
						+ "WHERE purchase.flag=:flag and purchase.company.company_id =:company_id and purchase.entry_status !=:entry_status and purchase.supplier_bill_date >=:created_date1 and purchase.supplier_bill_date <=:created_date2 order by purchase.supplier_bill_date ASC,purchase.purchase_id ASC");
				query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("created_date1", from_date);
				query.setParameter("created_date2", to_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  purchaselist.add((PurchaseEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
			}
			else
			{
				purchaselist.clear();
				Session session = getCurrentSession();
				Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
						+ "WHERE purchase.flag=:flag and purchase.company.company_id =:company_id and purchase.supplier.supplier_id =:supplier_id and purchase.entry_status !=:entry_status and purchase.supplier_bill_date >=:created_date1 and purchase.supplier_bill_date <=:created_date2 order by purchase.supplier_bill_date ASC,purchase.purchase_id ASC");
				query.setParameter("flag", true);
				query.setParameter("company_id", companyId);
				query.setParameter("supplier_id", supplierId);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("created_date1", from_date);
				query.setParameter("created_date2", to_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  purchaselist.add((PurchaseEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
			}
			
			if(purchaselist.size()>0)
			{
			for(PurchaseEntry entry :purchaselist)
			{
				LedgerReport ledgerReport = new LedgerReport();
				ledgerReport.setType("Purchase");
				if(entry.getTransaction_value()!=null)
				{
				ledgerReport.setTransaction_value(entry.getTransaction_value().toString());
				}
				if(entry.getVoucher_no()!=null)
				{
				ledgerReport.setVoucher_no(entry.getVoucher_no());
				}
				if(entry.getSupplier_bill_date()!=null)
				{
				ledgerReport.setCreated_date(entry.getSupplier_bill_date());
				}
				if(entry.getCgst()!=null)
				{
				ledgerReport.setCgst(entry.getCgst().toString());
				}
				if(entry.getIgst()!=null)
				{
				ledgerReport.setIgst(entry.getIgst().toString());
				}
				if(entry.getSgst()!=null)
				{
				ledgerReport.setSgst(entry.getSgst().toString());
				}
				if(entry.getRound_off()!=null)
				{
				ledgerReport.setRound_off(entry.getRound_off().toString());
				}
				ledgerReport.setPrimary_id(entry.getPurchase_id().toString());
				if(entry.getSupplier()!=null)
				{
				ledgerReport.setCustomer_name_id(entry.getSupplier().getSupplier_id().toString());
				}
				else
				{
					ledgerReport.setCustomer_name_id("0");
				}
				if(entry.getState_compansation_tax()!=null)
				{
				ledgerReport.setSct(entry.getState_compansation_tax().toString());
				}
				if(entry.getTotal_excise()!=null)
				{
				ledgerReport.setTotal_excise(entry.getTotal_excise().toString());
				}
				if(entry.getTotal_vat()!=null)
				{
				ledgerReport.setTotal_vat(entry.getTotal_vat().toString());
				}
				if(entry.getTotal_vatcst()!=null)
				{
				ledgerReport.setTotal_vatcst(entry.getTotal_vatcst().toString());
				}
				if(entry.getEntry_status()!=null)
				{
				ledgerReport.setEntry_status(entry.getEntry_status().toString());
				}
				if(entry.getFlag()!=null)
				{
			    ledgerReport.setFlag(entry.getFlag().toString());
				}
				if(entry.getSubledger()!=null)
				{
			    ledgerReport.setSubledger_id(entry.getSubledger().getSubledger_Id().toString());
				}
				ledgerReports.add(ledgerReport);
			}
			}
			else
			{
				LedgerReport ledgerReport = new LedgerReport();
				ledgerReport.setType("Purchase");
				ledgerReports.add(ledgerReport);
			}
		}
		return ledgerReports;
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> findAllListing(Long flag) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select l.ledger_id, c.company_name,g.subgroup_name,l.ledger_name,l.as_subledger,l.status,l.ledger_approval,l.allocated from account_sub_group_master g,company c,ledger_master l where  l.company_id=c.company_id and l.acc_sub_group_Id=g.subgroup_Id and l.flag=:flag\n" + 
				"union\n" + 
				"select l.ledger_id, c.company_name,'' as subgroup_name,l.ledger_name,l.as_subledger,l.status,l.ledger_approval,l.allocated from company c,ledger_master l where  l.company_id=c.company_id and l.flag=:flag and l.acc_sub_group_Id IS NULL\n" + 
				" order by ledger_id desc");
		if(flag==1)
		query.setParameter("flag", true);
		else
			query.setParameter("flag", false);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> findAllListingLedgersOfCompany(Long CompanyId, Long flag) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select l.ledger_id, c.company_name,g.subgroup_name,l.ledger_name,l.as_subledger,l.status,l.ledger_approval,l.allocated from account_sub_group_master g,company c,ledger_master l where  l.company_id=c.company_id and l.acc_sub_group_Id=g.subgroup_Id and l.flag=:flag and l.company_id= :company_id \n" + 
				"union\n" + 
				"select l.ledger_id, c.company_name,'' as subgroup_name,l.ledger_name,l.as_subledger,l.status,l.ledger_approval,l.allocated from account_sub_group_master g,company c,ledger_master l where  l.company_id=c.company_id and l.flag=:flag and l.company_id= :company_id and l.acc_sub_group_Id IS NULL\n" + 
				"order by ledger_id desc");
		if(flag==1)
		query.setParameter("flag", true);
		else
			query.setParameter("flag", false);
		query.setParameter("company_id", CompanyId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> findAllLedgersWithSubledger(Long CompanyId) {
		List<Ledger> list = new ArrayList<>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("ledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("ledger_id"));
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		list =  criteria.list();
		session.clear();
		return list;
	} 

	@Override
	public Ledger findOneWithAll(Long leId) {
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_id", leId));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("accsubgroup", FetchMode.JOIN);
		criteria.setFetchMode("accountGroup", FetchMode.JOIN);
		criteria.setFetchMode("grouptype", FetchMode.JOIN);
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		return (Ledger) criteria.uniqueResult();
	}

	@Transactional
	@Override
	public Long isExistsforcompany(String ledger_name, Long companyId) {
		Session session = getCurrentSession();
		Query query = session.createQuery( "select ledger_id from Ledger where company.company_id=:companyId and ledger_name =:ledger_name");
		query.setParameter("companyId", companyId);
		query.setParameter("ledger_name", ledger_name);
		if (query.list() == null || query.list().isEmpty())
			return (long) 0;
		else
			return (Long) query.list().get(0);

	}

	@Override
	public Boolean approvedByBatch(List<String> ledgerList, Boolean primaryApproval) {
		Session session = getCurrentSession();

		if (ledgerList.isEmpty()) {
			return false;
		} else {
			if (primaryApproval == true) {
				for (String id : ledgerList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery("update Ledger set ledger_approval=:ledger_approval where ledger_id =:sid");
					query.setParameter("ledger_approval", 2);
					query.setParameter("sid", pid);
					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			} else if (primaryApproval == false) {
				for (String id : ledgerList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery("update Ledger set ledger_approval=:ledger_approval where ledger_id =:sid");
					query.setParameter("ledger_approval", 3);
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
		return true;

	}

	@Override
	public Boolean rejectByBatch(List<String> ledgerList) {
		Session session = getCurrentSession();
		if (ledgerList.isEmpty()) {
			return false;
		} else {
			for (String id : ledgerList) {
				Long pid = Long.parseLong(id);
				Query query = session.createQuery("update Ledger set ledger_approval=:ledger_approval where ledger_id =:sid");
				query.setParameter("ledger_approval", 1);
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
	public int isExistsledger(String ledger_name, Long company_id) {
		Session sesssion = getCurrentSession();
		Ledger legder = new Ledger();
		Criteria criteria = sesssion.createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_name", ledger_name));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		legder = (Ledger) criteria.uniqueResult();
		sesssion.clear();
		if(legder==null)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}

	@Override
	public Ledger isExists(String name, Long company_id) {
		Session sesssion = getCurrentSession();
		Ledger legder = new Ledger();
		Criteria criteria = sesssion.createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_name", name));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		legder = (Ledger) criteria.uniqueResult();
		sesssion.clear();
		return legder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> findAllLedgersWithSubledger() {
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("ledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("ledger_id"));
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> findLedgersOnlyOfComapany(Long company_id) {
		/*Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("ledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("ledger_id"));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		return criteria.list();*/
		List<Ledger> list =  new ArrayList<Ledger>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT ledger from Ledger ledger WHERE ledger.company.company_id =:company_id and ledger.ledger_approval=:ledger_approval and ledger.status=:status and ledger.flag=:flag ORDER by ledger.ledger_id DESC");
		query.setParameter("company_id", company_id);
		query.setParameter("ledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Ledger)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> findAllListingExe(Boolean flag, List<Long> companyIds) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select l.ledger_id, c.company_name,g.subgroup_name,l.ledger_name,l.as_subledger,l.status,l.ledger_approval,l.allocated from account_sub_group_master g,company c,ledger_master l where  l.company_id=c.company_id and l.acc_sub_group_Id=g.subgroup_Id and l.flag=:flag and l.company_id in(:company_id) \n" + 
				"union\n" + 
				"select l.ledger_id, c.company_name,'' as subgroup_name,l.ledger_name,l.as_subledger,l.status,l.ledger_approval,l.allocated from company c,ledger_master l where  l.company_id=c.company_id and l.flag=:flag and l.company_id in(:company_id) and l.acc_sub_group_Id IS NULL\n" + 
				"order by ledger_id desc");
		query.setParameter("flag", flag);
		query.setParameterList("company_id", companyIds);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> findByStatus(int status, List<Long> companyIds) {
	/*	Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_approval", status));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.addOrder(Order.desc("ledger_id"));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("accsubgroup", FetchMode.JOIN);
		return criteria.list();*/
		List<Ledger> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT ledger from Ledger ledger WHERE ledger.ledger_approval=:ledger_approval and ledger.flag=:flag and ledger.status=:status and ledger.company.company_id in (:company_id) order by ledger.ledger_id desc");
		query.setParameterList("company_id", companyIds);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("ledger_approval", status);
	ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
	
	  while (scrollableResults.next()) {
		  list.add((Ledger)scrollableResults.get()[0]);
		  session.evict(scrollableResults.get()[0]);
         }
	  session.clear();
	  return list;
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<LedgerReport> getLedgerReportforOpeningBalance(LocalDate from_date, LocalDate to_date, Long companyId,
			Long customerId, Long supplierId) {
		
		List<LedgerReport> ledgerReports = new ArrayList<LedgerReport>();
		List<SalesEntry> saleslist = new ArrayList<SalesEntry>();
		List<PurchaseEntry> purchaselist = new ArrayList<PurchaseEntry>();
		
        if(customerId!=null) {
			
			if(customerId==-1)
			{
				saleslist.clear();
				Session session = getCurrentSession();
				Query query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
						+ "WHERE salesEntry.company.company_id =:company_id and salesEntry.entry_status !=:entry_status and salesEntry.created_date <=:created_date2");
				query.setParameter("company_id", companyId);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("created_date2", from_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  saleslist.add((SalesEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
			}
			if(customerId!=-1)
			{
				saleslist.clear();	
				Session session = getCurrentSession();
				Query query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
						+ "WHERE salesEntry.company.company_id =:company_id and salesEntry.customer.customer_id =:customer_id and salesEntry.entry_status !=:entry_status and salesEntry.created_date <=:created_date2");
				query.setParameter("company_id", companyId);
				query.setParameter("customer_id", customerId);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("created_date2", from_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  saleslist.add((SalesEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				
			}
			
			if(saleslist.size()>0)
			{
			for(SalesEntry entry :saleslist)
			{
				LedgerReport ledgerReport = new LedgerReport();
				ledgerReport.setType("Sales");
				if(entry.getTransaction_value()!=null)
				{
				ledgerReport.setTransaction_value(entry.getTransaction_value().toString());
				}
				if(entry.getVoucher_no()!=null)
				{
				ledgerReport.setVoucher_no(entry.getVoucher_no());
				}
				if(entry.getCreated_date()!=null)
				{
				ledgerReport.setCreated_date(entry.getCreated_date());
				}
				if(entry.getCgst()!=null)
				{
				ledgerReport.setCgst(entry.getCgst().toString());
				}
				if(entry.getIgst()!=null)
				{
				ledgerReport.setIgst(entry.getIgst().toString());
				}
				if(entry.getSgst()!=null)
				{
				ledgerReport.setSgst(entry.getSgst().toString());
				}
				if(entry.getRound_off()!=null)
				{
				ledgerReport.setRound_off(entry.getRound_off().toString());
				}
				ledgerReport.setPrimary_id(entry.getSales_id().toString());
				if(entry.getCustomer()!=null)
				{
				ledgerReport.setCustomer_name_id(entry.getCustomer().getCustomer_id().toString());
				}
				else
				{
					ledgerReport.setCustomer_name_id("0");
				}
				if(entry.getState_compansation_tax()!=null)
				{
				ledgerReport.setSct(entry.getState_compansation_tax().toString());
				}
				if(entry.getTotal_excise()!=null)
				{
				ledgerReport.setTotal_excise(entry.getTotal_excise().toString());
				}
				if(entry.getTotal_vat()!=null)
				{
				ledgerReport.setTotal_vat(entry.getTotal_vat().toString());
				}
				if(entry.getTotal_vatcst()!=null)
				{
				ledgerReport.setTotal_vatcst(entry.getTotal_vatcst().toString());
				}
				if(entry.getEntry_status()!=null)
				{
				ledgerReport.setEntry_status(entry.getEntry_status().toString());
				}
				if(entry.getFlag()!=null)
				{
			    ledgerReport.setFlag(entry.getFlag().toString());
				}
				if(entry.getSale_type()!=null)
				{
				ledgerReport.setSale_type(entry.getSale_type().toString());
				}
				if(entry.getBank()!=null)
				{
			    ledgerReport.setBank_id(entry.getBank().getBank_id().toString());
				}
				ledgerReports.add(ledgerReport);
			}
			}
			else
			{
				LedgerReport ledgerReport = new LedgerReport();
				ledgerReport.setType("Sales");
				ledgerReports.add(ledgerReport);
			}
		}
		
           if(supplierId!=null) {
			
			if(supplierId==-2)
			{
				purchaselist.clear();
			
				Session session = getCurrentSession();
				Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
						+ "WHERE purchase.company.company_id =:company_id and purchase.entry_status !=:entry_status and purchase.supplier_bill_date <=:created_date2");
				query.setParameter("company_id", companyId);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("created_date2", from_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  purchaselist.add((PurchaseEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
			}
			if(supplierId!=-2)
			{
				purchaselist.clear();
			
				Session session = getCurrentSession();
				Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
						+ "WHERE purchase.company.company_id =:company_id and purchase.supplier.supplier_id =:supplier_id and purchase.entry_status !=:entry_status and purchase.supplier_bill_date <=:created_date2");
				query.setParameter("company_id", companyId);
				query.setParameter("supplier_id", supplierId);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("created_date2", from_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  purchaselist.add((PurchaseEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
			}
			
			if(purchaselist.size()>0)
			{
			for(PurchaseEntry entry :purchaselist)
			{
				LedgerReport ledgerReport = new LedgerReport();
				ledgerReport.setType("Purchase");
				if(entry.getTransaction_value()!=null)
				{
				ledgerReport.setTransaction_value(entry.getTransaction_value().toString());
				}
				if(entry.getVoucher_no()!=null)
				{
				ledgerReport.setVoucher_no(entry.getVoucher_no());
				}
				if(entry.getCreated_date()!=null)
				{
				ledgerReport.setCreated_date(entry.getSupplier_bill_date());
				}
				if(entry.getCgst()!=null)
				{
				ledgerReport.setCgst(entry.getCgst().toString());
				}
				if(entry.getIgst()!=null)
				{
				ledgerReport.setIgst(entry.getIgst().toString());
				}
				if(entry.getSgst()!=null)
				{
				ledgerReport.setSgst(entry.getSgst().toString());
				}
				if(entry.getRound_off()!=null)
				{
				ledgerReport.setRound_off(entry.getRound_off().toString());
				}
				ledgerReport.setPrimary_id(entry.getPurchase_id().toString());
				if(entry.getSupplier()!=null)
				{
				ledgerReport.setCustomer_name_id(entry.getSupplier().getSupplier_id().toString());
				}
				else
				{
					ledgerReport.setCustomer_name_id("0");
				}
				if(entry.getState_compansation_tax()!=null)
				{
				ledgerReport.setSct(entry.getState_compansation_tax().toString());
				}
				if(entry.getTotal_excise()!=null)
				{
				ledgerReport.setTotal_excise(entry.getTotal_excise().toString());
				}
				if(entry.getTotal_vat()!=null)
				{
				ledgerReport.setTotal_vat(entry.getTotal_vat().toString());
				}
				if(entry.getTotal_vatcst()!=null)
				{
				ledgerReport.setTotal_vatcst(entry.getTotal_vatcst().toString());
				}
				if(entry.getEntry_status()!=null)
				{
				ledgerReport.setEntry_status(entry.getEntry_status().toString());
				}
				if(entry.getFlag()!=null)
				{
			    ledgerReport.setFlag(entry.getFlag().toString());
				}
				ledgerReports.add(ledgerReport);
			}
			}
			else
			{
				LedgerReport ledgerReport = new LedgerReport();
				ledgerReport.setType("Purchase");
				ledgerReports.add(ledgerReport);
			}
		}
		return ledgerReports;
	}*/

	@Override
	public Integer findAllLedgersOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("ledger_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public Integer findByStatusDashboard(int approvalStatus, List<Long> companyIds) {
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_approval", approvalStatus));
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("status", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public Integer findAllDashboard() {
		
		List <Integer> list = new ArrayList<Integer>();
		list.add(MyAbstractController.APPROVAL_STATUS_PENDING);
		list.add(MyAbstractController.APPROVAL_STATUS_PRIMARY);
		
		Criteria criteria = getCurrentSession().createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.in("ledger_approval", list));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}


}