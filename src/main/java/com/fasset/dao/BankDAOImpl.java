package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IBankDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Bank;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.exceptions.MyWebException;

@Transactional
@Repository
public class BankDAOImpl extends AbstractHibernateDao<Bank> implements IBankDAO{

	@Transactional
	@Override
	public Bank findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.add(Restrictions.eq("bank_id", id));
		criteria.setFetchMode("company", FetchMode.JOIN);
		return (Bank) criteria.uniqueResult();
	}

	@Override
	public Bank findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bank findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.addOrder(Order.desc("bank_id"));
		criteria.add(Restrictions.eq("bank_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		return criteria.list();
	}

	@Override
	public void create(Bank entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
		session.clear();
	}
	@Override
	public Long createBank(Bank entity) throws MyWebException {
		Session session = getCurrentSession();
		Long id=(Long) session.save(entity);
		session.flush();
	    session.clear();
		return id;
	}
	
	@Transactional
	@Override
	public void update(Bank entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public void merge(Bank entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Bank entity) throws MyWebException {
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
	public boolean validatePreTransaction(Bank entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(Bank entity) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> findByStatus(int status) {
		/*Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.addOrder(Order.desc("bank_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("bank_approval", status));
		return criteria.list();*/
		
		List<Bank> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT bank from Bank bank WHERE bank.bank_approval=:bank_approval and bank.flag=:flag and bank.status=:status order by bank.bank_id desc");
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("bank_approval", status);
	ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
	
	  while (scrollableResults.next()) {
		  list.add((Bank)scrollableResults.get()[0]);
		  session.evict(scrollableResults.get()[0]);
         }
	  session.clear();
	  return list;
	  
	}

	@Override
	public String updateApprovalStatus(Long bankId, int status) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update Bank set bank_approval =:status where bank_id =:bankId");
		query.setParameter("bankId", bankId);
		query.setParameter("status", status);
		String msg="";
		try{
			query.executeUpdate();
			if(status == 1)
				msg= "Rejected successfully";
			else if(status == 2)
				msg= "Bank Primary Approval Done, Sent For Secondary Approval ";
			else
				msg= "Bank Secondary Approval Done";
		}
		catch(Exception e)
		{
			msg= "You can't change status";
		}
		return msg;
	}

	@Override
	public Bank isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> findAllBanksOfCompany(Long CompanyId) {
	
		List<Bank> list = new ArrayList<Bank>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT bank from Bank bank WHERE bank.company.company_id =:company_id and bank.bank_approval=:bank_approval and bank.status=:status and bank.flag=:flag");
		query.setParameter("company_id", CompanyId);
		query.setParameter("bank_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		while (scrollableResults.next()) {
		list.add((Bank)scrollableResults.get()[0]);
		session.evict(scrollableResults.get()[0]);
		}
		session.clear();
		return list;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query queryop = session.createQuery("UPDATE Bank SET opening_id=null where bank_id =:id");
		queryop.setParameter("id", entityId);
		try {
			queryop.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Query queryb = session.createSQLQuery("delete from opening_balances where bank=:id");
		queryb.setParameter("id", entityId);
		try {
			queryb.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Query query = session.createQuery("delete from Bank where bank_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
			query.executeUpdate();
			msg= "Bank Deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete Bank";			
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> findAllListingBanksOfCompany(Long CompanyId,Boolean flag) {
		/*Session session = getCurrentSession();
		Query query = session.createSQLQuery("select b.bank_id, c.company_name,b.bank_name,b.branch,a.subgroup_name,b.account_no,b.status,b.bank_approval from bank b,company c,account_sub_group_master a where  b.company_id=c.company_id and b.account_sub_group_id=a.subgroup_Id and b.flag=:flag  and c.company_id =:companyId order by b.bank_id desc");
		query.setParameter("flag", flag);
		query.setParameter("companyId",CompanyId);
		return query.list();*/
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.add(Restrictions.eq("flag", flag));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("account_sub_group_id", FetchMode.JOIN);
		criteria.addOrder(Order.asc("bank_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> findAllListing(Boolean flag) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select b.bank_id, c.company_name,b.bank_name,b.branch,a.subgroup_name,b.account_no,b.status,b.bank_approval from bank b,company c,account_sub_group_master a where  b.company_id=c.company_id and b.account_sub_group_id=a.subgroup_Id and b.flag=:flag \n" + 
				"union\n" + 
				"select b.bank_id, c.company_name,b.bank_name,b.branch,'' as subgroup_name,b.account_no,b.status,b.bank_approval from bank b,company c where  b.company_id=c.company_id and b.flag=:flag and b.account_sub_group_id IS NULL\n" + 
				"order by bank_id desc");
		query.setParameter("flag", flag);
		return query.list();
	}

	@Override
	public Bank findOneView(Long id) {
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.add(Restrictions.eq("bank_id", id));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("account_sub_group_id", FetchMode.JOIN);
		return (Bank) criteria.uniqueResult();
	}

	@Override
	public Boolean approvedByBatch(List<String> bankList, Boolean primaryApproval) {		
		Session session = getCurrentSession();
		if (bankList.isEmpty()) {
			return false;
		} else {
			if (primaryApproval == true) {
				for (String id : bankList) {
					Long pid = Long.parseLong(id);
					Query query = session.createQuery("update Bank set bank_approval=:bank_approval where bank_id =:bank_id");
					query.setParameter("bank_approval", 2);
					query.setParameter("bank_id", pid);
					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return true;
			} else if (primaryApproval == false) {
				for (String id : bankList) {
					Long pid = Long.parseLong(id);
					Query query = session
							.createQuery("update Bank set bank_approval=:bank_approval where bank_id =:bank_id");
					query.setParameter("bank_approval", 3);
					query.setParameter("bank_id", pid);
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
	public Boolean rejectByBatch(List<String> bankList) {
		Session session = getCurrentSession();
		if (bankList.isEmpty()) {
			return false;
		} else {
			for (String id : bankList) {
				Long pid = Long.parseLong(id);
				Query query = session
						.createQuery("update Bank set bank_approval=:bank_approval where bank_id =:bank_id");
				query.setParameter("bank_approval", 1);
				query.setParameter("bank_id", pid);
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
	public int isExistsAccount(Long account_no, Long company_id) {
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.add(Restrictions.eq("account_no", account_no));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		if(criteria.list()==null || criteria.list().isEmpty())
			return 0;
		else
			return 1;	
	}

	@Override
	public Bank isExists(Long account_no, Long company_id) {
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.add(Restrictions.eq("account_no", account_no));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		return (Bank) criteria.uniqueResult();
	}

	@Override
	public List<Bank> findByStatus(int status, List<Long> companyIds) {
		
	/*	Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("bank_approval", status));
		criteria.addOrder(Order.desc("bank_id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<Bank> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT bank from Bank bank WHERE bank.bank_approval=:bank_approval and bank.flag=:flag and bank.status=:status and bank.company.company_id in (:company_id) order by bank.bank_id desc");
		query.setParameterList("company_id", companyIds);
		query.setParameter("status", true);
		query.setParameter("flag", true);
		query.setParameter("bank_approval", status);
	ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
	
	  while (scrollableResults.next()) {
		  list.add((Bank)scrollableResults.get()[0]);
		  session.evict(scrollableResults.get()[0]);
         }
	  session.clear();
	  return list;
	  
	}

	@Override
	public List<Bank> findAllListing(Boolean flag, List<Long> companyIds) {	
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select b.bank_id, c.company_name,b.bank_name,b.branch,a.subgroup_name,b.account_no,b.status,b.bank_approval from bank b,company c,account_sub_group_master a where  b.company_id=c.company_id and b.account_sub_group_id=a.subgroup_Id and b.flag=:flag  and c.company_id in(:companyId) \n" + 
				"union\n" + 
				"select b.bank_id, c.company_name,b.bank_name,b.branch,'' as subgroup_name,b.account_no,b.status,b.bank_approval from bank b,company c  where  b.company_id=c.company_id and b.flag=:flag  and c.company_id in(:companyId) and b.account_sub_group_id IS NULL\n" + 
				"order by bank_id desc");
		query.setParameter("flag", flag);
		query.setParameterList("companyId",companyIds);
		return query.list();
	}

	@Override
	public List<Bank> findAllOPbalanceofbank(long company_id, long flag) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select sum( `credit_balance` ) , sum( `debit_balance` ) , `bank`  from opening_balances where company_id=:company_id and balanceType=:balanceType group by bank");
		query.setParameter("company_id", company_id);
		query.setParameter("balanceType", 3);
		return query.list();
	}

	@Override
	public Integer findAllBanksOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.add(Restrictions.eq("bank_approval", MyAbstractController.APPROVAL_STATUS_SECONDARY));
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public Integer findByStatusDashboard(int approvalStatus, List<Long> companyIds) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.add(Restrictions.in("company.company_id", companyIds));
		criteria.add(Restrictions.eq("bank_approval", approvalStatus));
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
		
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.add(Restrictions.eq("status", true));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.in("bank_approval", list));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> findAllListingBanksOfCompany1(Long CompanyId,Boolean flag) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select b.bank_id, c.company_name,b.bank_name,b.branch,a.subgroup_name,b.account_no,b.status,b.bank_approval from bank b,company c,account_sub_group_master a where  b.company_id=c.company_id and b.account_sub_group_id=a.subgroup_Id and b.flag=:flag  and c.company_id =:companyId \n" + 
				"union\n" + 
				"select b.bank_id, c.company_name,b.bank_name,b.branch,'' as subgroup_name,b.account_no,b.status,b.bank_approval from bank b,company c where  b.company_id=c.company_id and b.flag=:flag  and c.company_id =:companyId and b.account_sub_group_id IS NULL\n" + 
				"order by bank_id desc");
		query.setParameter("flag", flag);
		query.setParameter("companyId",CompanyId);
		return query.list();
	}

	@Override
	public List<Bank> findAllListing1(boolean flag) {
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("flag", flag));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> findAllListing2(boolean flag, Long company_id) {
		Criteria criteria = getCurrentSession().createCriteria(Bank.class);
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("flag", flag));
		criteria.add(Restrictions.eq("company.company_id",company_id ));
		//criteria.add(Restrictions.eq("company_id", ));
		return criteria.list();
	}

	
}