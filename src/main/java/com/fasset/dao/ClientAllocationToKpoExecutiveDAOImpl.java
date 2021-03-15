package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IClientAllocationToKpoExecutiveDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Company;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;

@Repository
public class ClientAllocationToKpoExecutiveDAOImpl extends AbstractHibernateDao<ClientAllocationToKpoExecutive> implements IClientAllocationToKpoExecutiveDAO{

	@Override
	public ClientAllocationToKpoExecutive isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long saveClientAllocationToKpoExecutive(ClientAllocationToKpoExecutive clientAllocationToKpoExecutive) {
		Session session = getCurrentSession();
		clientAllocationToKpoExecutive.setCreated_date(new LocalDate());
		Long id  = (Long) session.save(clientAllocationToKpoExecutive);	
		session.flush();
		session.clear();
		return id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClientAllocationToKpoExecutive> findAll() {
		
        List<ClientAllocationToKpoExecutive> list = new ArrayList<ClientAllocationToKpoExecutive>();
			Session session = getCurrentSession();
			Query query = session.createQuery("SELECT client from ClientAllocationToKpoExecutive client order by client.allocation_Id desc");
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
			  list.add((ClientAllocationToKpoExecutive)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
					  
		/*Criteria criteria = getCurrentSession().createCriteria(ClientAllocationToKpoExecutive.class);
		criteria.addOrder(Order.desc("allocation_Id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);*/
		return list;
	}

	@Override
	public ClientAllocationToKpoExecutive findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(ClientAllocationToKpoExecutive.class);
		criteria.add(Restrictions.eq("allocation_Id", id));
		return (ClientAllocationToKpoExecutive) criteria.uniqueResult();
	}

	@Override
	public ClientAllocationToKpoExecutive findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientAllocationToKpoExecutive findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(ClientAllocationToKpoExecutive entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ClientAllocationToKpoExecutive entity) throws MyWebException {
		
		Session session = getCurrentSession();
		entity.setUpdate_date(new LocalDateTime());
		session.merge(entity);
		session.flush();
		session.clear();
	}

	@Override
	public void merge(ClientAllocationToKpoExecutive entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(ClientAllocationToKpoExecutive entity) throws MyWebException {
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
	public boolean validatePreTransaction(ClientAllocationToKpoExecutive entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(ClientAllocationToKpoExecutive entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from ClientAllocationToKpoExecutive where allocation_Id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "Allocation Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete Allocation";
			
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientAllocationToKpoExecutive> findCompanyByExecutiveId(Long user_id) {
		   List<ClientAllocationToKpoExecutive> list = new ArrayList<ClientAllocationToKpoExecutive>();
					Session session = getCurrentSession();
					Query query = session.createQuery("SELECT client from ClientAllocationToKpoExecutive client WHERE client.user.user_id=:user_id");
					query.setParameter("user_id", user_id);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  list.add((ClientAllocationToKpoExecutive)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				  session.clear();
				  
	/*	Criteria criteria = getCurrentSession().createCriteria(ClientAllocationToKpoExecutive.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("user.user_id", user_id));*/
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientAllocationToKpoExecutive> findAllCompaniesUnderExecutive(Long user_id) {
		List<ClientAllocationToKpoExecutive> exelist = new ArrayList<>();
		List<User> userlist = new ArrayList<>();
		/*Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("manager_id.user_id", user_id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		= criteria.list();*/
		
		
			Session session = getCurrentSession();
			Query query = session.createQuery("SELECT user from User user WHERE user.manager_id.user_id=:user_id");
			query.setParameter("user_id", user_id);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
			  userlist.add((User)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		
		List<Long>list = new ArrayList<>();
		for(User user : userlist)
		{
			list.add(user.getUser_id());
		}
		if(list.size()>0)
		{
			Session session1 = getCurrentSession();
			Query query1 = session1.createQuery("SELECT client from ClientAllocationToKpoExecutive client WHERE client.user.user_id in (:user_id)");
			query1.setParameterList("user_id", list);
		ScrollableResults scrollableResults1 = query1.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults1.next()) {
			  exelist.add((ClientAllocationToKpoExecutive)scrollableResults1.get()[0]);
		   session1.evict(scrollableResults1.get()[0]);
	         }
		  session1.clear();
		  
		/*Criteria criteria1 = getCurrentSession().createCriteria(ClientAllocationToKpoExecutive.class);
		criteria1.add(Restrictions.in("user.user_id", list));
		criteria1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		exelist = criteria1.list();*/
		}
		return exelist;
	}

	@Override
	public int isCompanyAllocated(Long user_id, Long company_id) {
		Criteria criteria = getCurrentSession().createCriteria(ClientAllocationToKpoExecutive.class);
		criteria.add(Restrictions.eq("user.user_id", user_id));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Company> findAllCompaniesUnderManager(Long user_id) {
		List<ClientAllocationToKpoExecutive> clientList = new ArrayList<>();
		List<Company>companyList=new ArrayList<>();
		/*Criteria criteria1 = getCurrentSession().createCriteria(ClientAllocationToKpoExecutive.class);
		criteria1.add(Restrictions.eq("user.user_id", user_id));
		criteria1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		clientList=criteria1.list();*/
			Session session = getCurrentSession();
			Query query = session.createQuery("SELECT client from ClientAllocationToKpoExecutive client WHERE client.user.user_id=:user_id");
			query.setParameter("user_id", user_id);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
			  clientList.add((ClientAllocationToKpoExecutive)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  
		  
		
     	for(ClientAllocationToKpoExecutive client : clientList) {
     		companyList.add(client.getCompany());
	       }
		return companyList;
	}

	@Override
	public List<User> findAllExcecutiveAndManagerOfCompany(Long company_id) {
		List<ClientAllocationToKpoExecutive> clientList = new ArrayList<>();
		List<User>userList=new ArrayList<>();
			Session session = getCurrentSession();
			Query query = session.createQuery("SELECT client from ClientAllocationToKpoExecutive client WHERE client.company.company_id=:company_id");
			query.setParameter("company_id", company_id);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
			  clientList.add((ClientAllocationToKpoExecutive)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  
		  
		
     	for(ClientAllocationToKpoExecutive client : clientList) {
     		userList.add(client.getUser());
	       }
		return userList;
	}

}





