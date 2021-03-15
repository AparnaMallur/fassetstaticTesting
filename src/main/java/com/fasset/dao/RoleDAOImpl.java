package com.fasset.dao;

import java.util.HashMap;
import org.hibernate.cfg.*;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IRoleDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Role;
import com.fasset.entities.RoleMenuMaster;
import com.fasset.exceptions.MyWebException;


import java.util.*;
import org.hibernate.*;
@Repository
public class RoleDAOImpl extends AbstractHibernateDao<Role> implements IRoleDAO{

	@Override
	public Role findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Role.class);
		criteria.add(Restrictions.eq("role_id", id));
		return (Role) criteria.uniqueResult();
	}

	@Override
	public Role findOne(Criteria crt ) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Role order by role_name ASC");
		return query.list();
		
	}

	@Override
	public void  create(Role entity)  throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDate());
		session.save(entity);	
		session.flush();
	    session.clear();
		
	}

	@Override
	public void update (Role entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setUpdated_date(new LocalDate());
		session.merge(entity);
		session.flush();
	    session.clear();
		
	}

	@Override
	public void merge(Role entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Role entity) throws MyWebException {
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
	public boolean validatePreTransaction(Role entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(Role entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RoleMenuMaster getRole() {
		RoleMenuMaster roleMenuMaster = new RoleMenuMaster();
		
		Session session = getCurrentSession();
		 Query qry = session.createQuery("from Role r");
		 List l =qry.list();
		 System.out.println("Total Number Of Records : "+l.size());
		java.util.Iterator itr= l.iterator();
		
		while(itr.hasNext())
		{
			 	Object o = itr.next();
			 	Role r = (Role)o;
			 	if(r.getRole_name().equals(MyAbstractController.SuperUserRole))
			 	{
			 		roleMenuMaster.setSuperUserName(r.getRole_name());
			 		roleMenuMaster.setSuperUserId(r.getRole_id());
			 	}
			 	else if(r.getRole_name().equals(MyAbstractController.ClientRole))
			 	{
			 		roleMenuMaster.setClientName(r.getRole_name());
			 		roleMenuMaster.setClientId(r.getRole_id());
			 	}
			 	else if(r.getRole_name().equals(MyAbstractController.Auditor))
			 	{
			 		roleMenuMaster.setAuditorName(r.getRole_name());
			 		roleMenuMaster.setAuditorId(r.getRole_id());
			 	}
			 	
		}
		return roleMenuMaster;
	}

	@Override
	public List<Role> getRoleListById(List<Long> roleIds) {
		Criteria criteria = getCurrentSession().createCriteria(Role.class);
		criteria.add(Restrictions.in("role_id", roleIds));
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public Role isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}