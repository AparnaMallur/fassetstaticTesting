/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IAccountGroupDAO;
import com.fasset.dao.interfaces.IPurchaseEntryDAO;
import com.fasset.dao.interfaces.ISalesEntryDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.Company;
import com.fasset.entities.Ledger;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class AccountGroupDAOImpl extends AbstractHibernateDao<AccountGroup> implements IAccountGroupDAO{


	
	@Override
	public Long saveAccountGroupDao(AccountGroup group) {		
		Session session = getCurrentSession();
		Long id= (Long) session.save(group);
		session.flush();
	    session.clear();
	    return id;
	}
	
	
	@Override
	public AccountGroup findOne(Long id) throws MyWebException {
		    Session session = getCurrentSession();
			Criteria criteria = session.createCriteria(AccountGroup.class);
			criteria.add(Restrictions.eq("group_Id", id));
			AccountGroup group=  (AccountGroup) criteria.uniqueResult();
			session.clear();
			return group;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AccountGroup> findAll() {
		List<AccountGroup> list = new ArrayList<AccountGroup>();
		Session session =  getCurrentSession();
		Criteria criteria = session.createCriteria(AccountGroup.class);
		criteria.addOrder(Order.asc("group_name"));
		criteria.add(Restrictions.eq("status", true));
		criteria.setFetchMode("account_sub_group", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		list = criteria.list();
		session.clear();
		return list; 
	}
	
	@Override
	public void update(AccountGroup entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);		
		session.flush();
	    session.clear();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from AccountGroup where group_Id =:id");
		query.setParameter("id", entityId);
		String msg="";
	/*	try{
			query.executeUpdate();
			msg= "AccountGroup Deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete AccountGroup.";			
		}*/
		msg= "You can't delete AccountGroup.";
		return msg;
	}

	@Override
	public AccountGroup isExists(String name) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(AccountGroup.class);
		criteria.add(Restrictions.eq("group_name", name));
		AccountGroup group= (AccountGroup) criteria.uniqueResult();
		session.clear();
		return group;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountGroup> findByCompanyId(Long companyId) {
		List<AccountGroup> list = new ArrayList<>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(AccountGroup.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.addOrder(Order.asc("group_name"));
		criteria.setFetchMode("account_sub_group", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		list = criteria.list();
		session.clear();
		return list;
	}

	@Override
	public List<AccountGroup> findAllListing() {	
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select g.group_Id, g.group_name, t.account_group_name, l.posting_title, g.status from account_group_master g, account_group_type t, ledger_posting_side l where g.group_type=t.account_group_id and g.posting_side=l.posting_id order by g.group_Id desc");
		return query.list();
		/*Criteria criteria = getCurrentSession().createCriteria(AccountGroup.class);
		criteria.addOrder(Order.asc("group_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("grouptype", FetchMode.JOIN);
		criteria.setFetchMode("postingSide", FetchMode.JOIN);
		return criteria.list();*/
	}


	@Override
	public AccountGroup findOneWithAll(Long accId) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(AccountGroup.class);
		criteria.add(Restrictions.eq("group_Id", accId));
		criteria.setFetchMode("grouptype", FetchMode.JOIN);
		criteria.setFetchMode("postingSide", FetchMode.JOIN);
		AccountGroup group=  (AccountGroup) criteria.uniqueResult();
		session.clear();
		return group;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<AccountGroup> getGroupsWithSubGrouplist() {
		List<AccountGroup> list = new ArrayList<>();
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(AccountGroup.class);
		criteria.setFetchMode("account_sub_group", FetchMode.JOIN);
		criteria.addOrder(Order.asc("group_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		list =  criteria.list();
		session.clear();
		return list;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<AccountGroup> findhorizontalAndVerticalBalanceSheetReport(Long comapany_id) {
		
		Session session = getCurrentSession();
		AccountGroupDAOImpl obj = new AccountGroupDAOImpl();
		List<AccountGroup> grouplist = new ArrayList<>();
		List<AccountGroup> newgrouplist = new ArrayList<>();
		Set<AccountSubGroup> account_sub_group = new HashSet<AccountSubGroup>();
		Set<Ledger> ledger = new HashSet<Ledger>();
		
		
		Criteria criteria = session.createCriteria(AccountGroup.class);
		criteria.add(Restrictions.eq("company.company_id", comapany_id));
		criteria.setFetchMode("account_sub_group", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		grouplist = criteria.list();
		
		for(AccountGroup group:grouplist) 
		{
			AccountGroup group2 = new AccountGroup();
			group2=group;
			for(AccountSubGroup subgrp : group.getAccount_sub_group() ) 
			{
				AccountSubGroup subgroup = new AccountSubGroup();
				subgroup = subgrp;
				for(Ledger newledger : subgrp.getLedger() ) 
				{
					ledger.add(obj.getLedger(newledger, session));
				}
				subgroup.getLedger().clear();
				subgroup.setLedger(ledger);
				
				account_sub_group.add(subgroup);
			}
			group2.getAccount_sub_group().clear();
			group2.setAccount_sub_group(account_sub_group);
			newgrouplist.add(group2);
		}
		
		
		return newgrouplist;
		
		
		
	}

	@Transactional
	public  Ledger getLedger(Ledger newledger ,Session session)
	{
		 Ledger ledger = new Ledger();
		 Set<SubLedger> subLedger = new HashSet<>();
		 AccountGroupDAOImpl obj = new AccountGroupDAOImpl();
		Criteria criteria = session.createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_id", newledger.getLedger_id()));
		criteria.setFetchMode("subLedger",  FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		ledger =(Ledger) criteria.uniqueResult();
         for(SubLedger subledger : ledger.getSubLedger())
          {
        	 subLedger.add(obj.getSubledger(subledger, session));
          }
         ledger.getSubLedger().clear();
         ledger.setSubLedger(subLedger);
		return ledger;
		
	}
	
	@Transactional
	public  SubLedger getSubledger(SubLedger newsubledger ,Session session){
		Criteria criteria = session.createCriteria(SubLedger.class);
		criteria.add(Restrictions.eq("subledger_Id", newsubledger.getSubledger_Id()));
		criteria.setFetchMode("purchaseEntry",  FetchMode.JOIN);
		criteria.setFetchMode("salesEntry",  FetchMode.JOIN);
		criteria.setFetchMode("payment",  FetchMode.JOIN);
		criteria.setFetchMode("receipt",  FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (SubLedger)criteria.uniqueResult();
		
	}

	@Override
	public Set<AccountSubGroup> getSubgroupListForCashFlow(Long groupId) {
		Criteria criteria = getCurrentSession().createCriteria(AccountGroup.class);
		criteria.add(Restrictions.eq("group_Id",groupId));
		criteria.setFetchMode("account_sub_group", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		AccountGroup group =  (AccountGroup) criteria.uniqueResult();
		return group.getAccount_sub_group();
	}
}
