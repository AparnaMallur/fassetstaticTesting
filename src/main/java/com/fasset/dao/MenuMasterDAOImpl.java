package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IMenuMasterDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.MenuAccessMaster;
import com.fasset.entities.MenuMaster;
import com.fasset.exceptions.MyWebException;
import com.itextpdf.text.log.SysoCounter;
@Repository
public class MenuMasterDAOImpl extends AbstractHibernateDao<MenuMaster> implements IMenuMasterDAO{

		@Override
		public Long saveMenuMaster(MenuMaster menuMaster) {
			long id=0;
			try
			{
				Session session=getCurrentSession();
				id=(long)session.save(menuMaster);
				
			}
			catch(Exception ex)
			{
				System.out.println("Error in saveMenuMaster");
			}
			return id;
		}

	 @SuppressWarnings("unchecked")
	@Override
	public List<MenuMaster> findAll(Long loinId) {
		 Session session = getCurrentSession();

		 if(loinId == MyAbstractController.ROLE_CLIENT)
		 {	
			 Query subquery = session.createQuery("select menu_Id FROM MenuAccessMaster WHERE role_Id = :user_Id");
			 subquery.setParameter("user_Id", loinId);
			 List roleAccess = subquery.list();
			 Query querymenu = session.createQuery("FROM MenuMaster WHERE menu_Id in (:roleAccess)");
			 querymenu.setParameterList("roleAccess", roleAccess);					
			 return querymenu.list();			 
		 }
		 else if(loinId==MyAbstractController.ROLE_MANAGER)
		 {
			 Query subquery = session.createQuery("select menu_Id FROM MenuAccessMaster WHERE role_Id = :user_Id");
			 subquery.setParameter("user_Id", loinId);
			 List roleAccess = subquery.list();
			 Query querymenu = session.createQuery("FROM MenuMaster WHERE menu_Id in (:roleAccess)");
			 querymenu.setParameterList("roleAccess", roleAccess);					
			 return querymenu.list();	
		 }
		 else if(loinId==MyAbstractController.ROLE_SUPERUSER)
		 {
			 Criteria criteria = getCurrentSession().createCriteria(MenuMaster.class);
			 criteria.addOrder(Order.desc("menu_id"));
			 return criteria.list();
		 }
		return null;
		
	}
	
	@Override
	public MenuMaster findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(MenuMaster.class);
		criteria.add(Restrictions.eq("menu_id", id));
		return (MenuMaster) criteria.uniqueResult();

	}
	
	
	 @Override
	public void update(MenuMaster entity) throws MyWebException {
		Session session = getCurrentSession();
		session.update(entity);
		
	}

	
	@Override
	public MenuMaster isExists(String name) {
		
		return null;
	}

	@Override
	public List<MenuMaster> getSubMenuList(Long parent_id) {
		System.out.println("getSubMenuList DAOImpl========"+parent_id);
		Session session=getCurrentSession();
		Query qry=session.createSQLQuery("select * from menu_master where parent_id=:parent_id").addEntity(MenuMaster.class).setParameter("parent_id", parent_id);
		//qry.setParameter("parent_id", parent_id);
		List<MenuMaster> subMenuList=qry.list();
		System.out.println("subMenuList Size========"+subMenuList.size());
		return subMenuList;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("delete from menu_master where menu_id=:menu_id");
		query.setParameter("menu_id", entityId);
		String msg="";
			try{
				query.executeUpdate();
				msg= "Menu Deleted successfully";
			}
			catch(Exception e)
			{
				msg= "You can't delete Menu";				
			}
		return msg;	
		}

	@Override
	public List<MenuMaster> findAllbyrole(long roleId, long user_Id) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();

		 if(roleId == MyAbstractController.ROLE_CLIENT)
		 {	
			    List<MenuAccessMaster> menuAccess = new ArrayList<MenuAccessMaster>();
			    Query subquery = session.createQuery("select menu_Id FROM MenuAccessMaster WHERE user_Id = :user_Id ");
				subquery.setParameter("user_Id", user_Id);
				List roleAccess = subquery.list();
				
				Query querymenu;
				if(roleAccess==null || roleAccess.isEmpty())
				{
					querymenu = session.createQuery("FROM MenuAccessMaster WHERE (user_Id = :user_Id and access_List = true and role_Id= :role_Id) Or (role_Id= :role_Id and  access_List = true and user_Id=0)");
				}
				else
				{
					querymenu = session.createQuery("FROM MenuAccessMaster WHERE (user_Id = :user_Id and access_List = true and role_Id= :role_Id) Or (role_Id= :role_Id  and user_Id=0 and  access_List = true and menu_Id not in(:roleAccess))");
				    querymenu.setParameterList("roleAccess", roleAccess);
				}

				querymenu.setParameter("user_Id", user_Id);
				querymenu.setParameter("role_Id", roleId);
			
				menuAccess=querymenu.list();
			    List<Long>list = new ArrayList<>();
			for(MenuAccessMaster menmaster: menuAccess)
			{
				list.add((long) menmaster.getMenu_Id());
			}
			 Query querymenu1 = session.createQuery("FROM MenuMaster WHERE menu_Id in (:roleAccess)");
			 querymenu1.setParameterList("roleAccess", list);					
			 return querymenu1.list();			 
		 }
		 else if(roleId==MyAbstractController.ROLE_MANAGER)
		 {
			/* Query subquery = session.createQuery("select menu_Id FROM MenuAccessMaster WHERE ((role_Id = :role_Id) Or (user_Id= :user_Id))");
			 subquery.setParameter("role_Id", roleId);
			 subquery.setParameter("user_Id", user_Id);
			 List roleAccess = subquery.list();
			 Query querymenu = session.createQuery("FROM MenuMaster WHERE menu_Id in (:roleAccess)");
			 querymenu.setParameterList("roleAccess", roleAccess);					
			 return querymenu.list();*/	
			 List<MenuAccessMaster> menuAccess = new ArrayList<MenuAccessMaster>();
			    Query subquery = session.createQuery("select menu_Id FROM MenuAccessMaster WHERE user_Id = :user_Id ");
				subquery.setParameter("user_Id", user_Id);
				List roleAccess = subquery.list();
				
				Query querymenu;
				if(roleAccess==null || roleAccess.isEmpty())
				{
					querymenu = session.createQuery("FROM MenuAccessMaster WHERE (user_Id = :user_Id and access_List = true and role_Id= :role_Id) Or (role_Id= :role_Id and  access_List = true and user_Id=0)");
				}
				else
				{
					querymenu = session.createQuery("FROM MenuAccessMaster WHERE (user_Id = :user_Id and access_List = true and role_Id= :role_Id) Or (role_Id= :role_Id  and user_Id=0 and  access_List = true and menu_Id not in(:roleAccess))");
				    querymenu.setParameterList("roleAccess", roleAccess);
				}

				querymenu.setParameter("user_Id", user_Id);
				querymenu.setParameter("role_Id", roleId);
			
				menuAccess=querymenu.list();
			    List<Long>list = new ArrayList<>();
			for(MenuAccessMaster menmaster: menuAccess)
			{
				list.add((long) menmaster.getMenu_Id());
			}
			 Query querymenu1 = session.createQuery("FROM MenuMaster WHERE menu_Id in (:roleAccess)");
			 querymenu1.setParameterList("roleAccess", list);					
			 return querymenu1.list();
		 }
		 else if(roleId==MyAbstractController.ROLE_SUPERUSER)
		 {
			 Criteria criteria = getCurrentSession().createCriteria(MenuMaster.class);
			 criteria.addOrder(Order.desc("menu_id"));
			 return criteria.list();
		 }
		return null;
	}
}
