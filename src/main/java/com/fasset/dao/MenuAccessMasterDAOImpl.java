package com.fasset.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IMenuAccessMasterDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.MenuAccessMaster;
import com.fasset.entities.MenuMaster;
import com.fasset.entities.Role;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
@Repository
public class MenuAccessMasterDAOImpl extends AbstractHibernateDao<MenuAccessMaster> implements  IMenuAccessMasterDAO{

		@Override
		public Long SaveMenuAccessMaster(MenuAccessMaster menuAccessMaster) {
			
			Session session = getCurrentSession();
			menuAccessMaster.setCreated_Date(new LocalDateTime());
			Long id = (Long) session.save(menuAccessMaster);
			
			return id;
		}
		
	@Override
	public void update(MenuAccessMaster menuAccessMaster) throws MyWebException {		
		Session session = getCurrentSession();
		session.update(menuAccessMaster);		
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long,String> getRoleList(Long role_id) {
		  Map<Long, String> roleList = new HashMap<Long, String>();
		 
		  Session session=getCurrentSession();
			Query qry=session.createQuery("from Role r ");
			List<Role> list=qry.list();
			Iterator itr=list.iterator();
			if(role_id == MyAbstractController.ROLE_SUPERUSER) {
				while (itr.hasNext())
				{
					Object ob=itr.next();
					Role r=(Role)ob;
					
					if(r.getRole_id()==MyAbstractController.ROLE_SUPERUSER)
					{
						roleList.put(r.getRole_id(),r.getRole_name());
					}
					else if(r.getRole_id()==MyAbstractController.ROLE_EXECUTIVE)
					{
						roleList.put(r.getRole_id(),r.getRole_name());
					}
					else if(r.getRole_id()==MyAbstractController.ROLE_MANAGER)
					{
						roleList.put(r.getRole_id(), r.getRole_name());
					}
					else if(r.getRole_id()==MyAbstractController.ROLE_AUDITOR)
					{
						roleList.put(r.getRole_id(), r.getRole_name());
					}
					else if(r.getRole_id()==MyAbstractController.ROLE_CLIENT)
					{
						roleList.put(r.getRole_id(), r.getRole_name());
					}
					else if(r.getRole_id()==MyAbstractController.ROLE_TRIAL_USER)
					{
						roleList.put(r.getRole_id(), r.getRole_name());
					}
					
				}
				
			}
			if(role_id == MyAbstractController.ROLE_CLIENT) {
				
				while (itr.hasNext())
				{
					Object ob=itr.next();
					Role r=(Role)ob;
					
					
					 if(r.getRole_id()==MyAbstractController.ROLE_AUDITOR)
					{
						roleList.put(r.getRole_id(), r.getRole_name());
					}
					else if(r.getRole_id()==MyAbstractController.ROLE_EMPLOYEE)
					{
						roleList.put(r.getRole_id(), r.getRole_name());
					}
				}	 
					
			}
			if(role_id == MyAbstractController.ROLE_MANAGER) {				
				while (itr.hasNext())
				{
					Object ob=itr.next();
					Role r=(Role)ob;				
					if(r.getRole_id()==MyAbstractController.ROLE_EXECUTIVE)
					{
						roleList.put(r.getRole_id(), r.getRole_name());
					}					
				}	 
					
			}
			
		/*Query qry=session.createQuery("from Role r where r.role_id=:role_id");
		qry.setParameter("role_id",role_id);*/
		
		
		return roleList;
	}
	
	@Override
	public MenuAccessMaster findOne(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MenuAccessMaster findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MenuAccessMaster findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<MenuAccessMaster> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void create(MenuAccessMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void merge(MenuAccessMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete(MenuAccessMaster entity) throws MyWebException {
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
	
		return 0;
	}
	@Override
	public boolean validatePreTransaction(MenuAccessMaster entity) throws MyWebException {
		
		return false;
	}
	@Override
	public void refresh(MenuAccessMaster entity) {
		// TODO Auto-generated method stub
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getnameList(long role_id, long userrole_id,long company_id) {
		Session session = getCurrentSession();
		Query query;
		if(userrole_id==MyAbstractController.ROLE_CLIENT)
		{
		query = session.createQuery("from User where role.role_id =:role_id and company.company_id=:company_id");
		query.setParameter("company_id", company_id);
		query.setParameter("role_id", role_id);
		}
		else
		{
			query = session.createQuery("from User where role.role_id =:role_id");
			query.setParameter("role_id", role_id);
		}
		return query.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MenuAccessMaster> getAccess(Long user_Id , Long role_Id) {
		Session session=getCurrentSession();
		System.out.println("getAccess DAOImpl========:>"+user_Id+""+role_Id);
		List<Long> Ulist=new ArrayList<Long>();
		Ulist.add((long)0);
		Ulist.add((long)user_Id);
		List<Boolean> Blist=new ArrayList<Boolean>();
		Blist.add(true);
		Blist.add(false);
		List<MenuAccessMaster> results = new ArrayList<MenuAccessMaster>();		 
			Criteria crit=session.createCriteria(MenuAccessMaster.class);
			if(user_Id==0)
			{
				
				crit.add(Restrictions.eq("user_Id", user_Id));
			}
			else
			{
				
				crit.add(Restrictions.in("user_Id", Ulist));
			}
			
			crit.add(Restrictions.eq("role_Id", role_Id));
			crit.add(Restrictions.in("flag", Blist));
			 results=crit.list();
			 System.out.println("Result Size=====:>"+results.size());
			return results;
			
		  
		}
	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public MenuAccessMaster isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List getDynamicMenuAccess(Long user_Id, Long role_Id) {
		
		//System.out.println("Inside getDynamicMenuAccess DAOImpl========		"+user_Id);
		 List dynamicList = new ArrayList();
		 
		 List<MenuAccessMaster> menuAccess = new ArrayList<MenuAccessMaster>();
		 
		 List<MenuMaster> menuMaster = new ArrayList<MenuMaster>();
		 List<MenuMaster> subMenu=new ArrayList<MenuMaster>();		 
		 Session session = getCurrentSession();
		 
		Query query = session.createQuery("FROM MenuMaster WHERE parent_id IS NULL and status=1 order by sequence_no asc");
		menuMaster=query.list();
		
		Query subquery = session.createQuery("select menu_Id FROM MenuAccessMaster WHERE user_Id = :user_Id ");
		subquery.setParameter("user_Id", user_Id);
		List roleAccess = subquery.list();
		
		Query querymenu;
		if(roleAccess==null || roleAccess.isEmpty())
		{
			System.out.println("The null");
			querymenu = session.createQuery("FROM MenuAccessMaster WHERE (user_Id = :user_Id and access_List = true and role_Id= :role_Id) Or (role_Id= :role_Id and  access_List = true and user_Id=0)");
		}
		else
		{
			System.out.println("The not null");
			querymenu = session.createQuery("FROM MenuAccessMaster WHERE (user_Id = :user_Id and access_List = true and role_Id= :role_Id) Or (role_Id= :role_Id  and user_Id=0 and  access_List = true and menu_Id not in(:roleAccess))");
		    querymenu.setParameterList("roleAccess", roleAccess);
		}
		
		querymenu.setParameter("user_Id", user_Id);
		querymenu.setParameter("role_Id", role_Id);
	
		menuAccess=querymenu.list();
		
		/*Criteria criteria=session.createCriteria(MenuAccessMaster.class);
		criteria.add(Restrictions.eq("user_Id", user_Id));		
		criteria.add(Restrictions.eq("access_List", true));
		
		menuAccess=criteria.list();*/
		//System.out.println("List Size menuAccess========"+menuAccess.size());
		
		Query qry=session.createSQLQuery("SELECT *  FROM menu_master WHERE parent_id IS NOT NULL  and status=1 order by sequence_no asc").addEntity(MenuMaster.class);
		List<MenuMaster> subMenuList=qry.list();
		//Query qry=session.createQuery("FROM MenuMaster ");
		//subMenu=qry.list();
		//System.out.println("List Size subMenu========"+subMenuList.size());

		dynamicList.add(menuMaster);
		dynamicList.add(subMenuList);
		dynamicList.add(menuAccess);
		return dynamicList;
		
	}
	@Override
	public MenuAccessMaster findmenuexist(Long uid,int mid,Long rid) 
	{
		
		 MenuAccessMaster menuexists = new MenuAccessMaster();
		Session session = getCurrentSession();
		Query menuquery = session.createQuery("FROM MenuAccessMaster WHERE user_Id = :user_Id and role_Id= :role_Id and menu_Id= :menu_Id");
		menuquery.setParameter("user_Id", uid);
		menuquery.setParameter("menu_Id", mid);
		menuquery.setParameter("role_Id", rid);
		try
		{
		menuexists = (MenuAccessMaster) menuquery.uniqueResult();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return menuexists;
	}

	@Override
	public List<MenuAccessMaster> findmenuacccess(Long loginId, int menuId, Long role_Id) {
		// TODO Auto-generated method stub
		List<MenuAccessMaster> list=new ArrayList<MenuAccessMaster>();
		Session session = getCurrentSession();		
		List<Long> Ulist=new ArrayList<Long>();
		Ulist.add((long)loginId);
		/*Ulist.add((long)0);*/
		Query menuquery = session.createQuery("FROM MenuAccessMaster WHERE user_Id in (:user_Id) and role_Id= :role_Id ORDER BY user_Id DESC ");
			menuquery.setParameterList("user_Id", Ulist);
			menuquery.setParameter("role_Id", role_Id);
			list = menuquery.list();
		return list;
	}

}