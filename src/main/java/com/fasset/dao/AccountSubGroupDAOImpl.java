/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IAccountSubGroupDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.AccountSubGroup;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Transactional
@Repository
public class AccountSubGroupDAOImpl extends AbstractHibernateDao<AccountSubGroup> implements IAccountSubGroupDAO{

	@Override
	public Long saveAccountSubGroupDao(AccountSubGroup group) {		
		Session session = getCurrentSession();
		Long id= (Long) session.save(group);
		session.flush();
		session.clear();
		return id;
	}
	
	@Transactional
	@Override
	public AccountSubGroup findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(AccountSubGroup.class);
		criteria.add(Restrictions.eq("subgroup_Id", id));
		return (AccountSubGroup) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AccountSubGroup> findAll() {
		List<AccountSubGroup> list = new ArrayList<AccountSubGroup>();
		Session session =  getCurrentSession();
		Criteria criteria = session.createCriteria(AccountSubGroup.class);
		criteria.addOrder(Order.asc("subgroup_name"));
		criteria.add(Restrictions.eq("status", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		list = criteria.list();
		session.clear();
		return list; 
	}
    
	@Override
	public void update(AccountSubGroup entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);		
		session.flush();
		session.clear();
	}
	
	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from AccountSubGroup where subgroup_Id =:id");
		query.setParameter("id", entityId);
		String msg="";
		msg= "You can't delete Account Sub Group";
		/*try{
			query.executeUpdate();
			msg= "Account Sub Group Deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete Account Sub Group";			
		}*/
		return msg;
	}

	@Override
	public AccountSubGroup isExists(String name) {
		Criteria criteria = getCurrentSession().createCriteria(AccountSubGroup.class);
		criteria.add(Restrictions.eq("subgroup_name", name));
		return (AccountSubGroup) criteria.uniqueResult();
	}

	@Override
	public List<AccountSubGroup> findAllListing() {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select s.subgroup_Id, s.subgroup_name, a.group_name, s.status from account_sub_group_master s, account_group_master a where s.group_Id= a.group_Id order by s.subgroup_Id desc");
		return query.list();
		
	/*	Criteria criteria = getCurrentSession().createCriteria(AccountSubGroup.class);
		criteria.addOrder(Order.asc("subgroup_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("accountGroup",  FetchMode.JOIN);
		return criteria.list();	*/
	}

	@Override
	public List<AccountSubGroup> findSubgroup() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from AccountSubGroup where subgroup_name like :subgroup_cash OR subgroup_name like :subgroup_loan order by subgroup_name ASC");
		query.setParameter("subgroup_cash", "%cash%");
		query.setParameter("subgroup_loan", "%loan%");	
		return query.list();
	}

	@Override
	public AccountSubGroup findOneWithAll(Long accsId) {
		Criteria criteria = getCurrentSession().createCriteria(AccountSubGroup.class);
		criteria.add(Restrictions.eq("subgroup_Id", accsId));
		criteria.setFetchMode("accountGroup",  FetchMode.JOIN);
		criteria.setFetchMode("grouptype",  FetchMode.JOIN);
		return (AccountSubGroup) criteria.uniqueResult();
	}

	@Override
	public List<AccountSubGroup> findSubGroupForProfitAndLossReport() {
		Criteria criteria = getCurrentSession().createCriteria(AccountSubGroup.class);
		criteria.createAlias("accountGroup", "accGrp");
		criteria.add(Restrictions.or(Restrictions.eq("accGrp.postingSide", "Profit and loss account debit side"), Restrictions.eq("accGrp.postingSide", "Profit and loss account credit side")));
		criteria.setFetchMode("accountGroup",  FetchMode.JOIN);
		return criteria.list();
	}
	
}
