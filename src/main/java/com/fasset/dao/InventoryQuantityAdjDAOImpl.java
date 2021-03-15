package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IInventoryQuantityAdjDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.InventoryQuantityAdjustment;
import com.fasset.exceptions.MyWebException;

@Repository
public class InventoryQuantityAdjDAOImpl extends AbstractHibernateDao<InventoryQuantityAdjustment> implements IInventoryQuantityAdjDAO{

	@Override
	public InventoryQuantityAdjustment findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(InventoryQuantityAdjustment.class);
		criteria.add(Restrictions.eq("inventory_adj_id", id));
		return (InventoryQuantityAdjustment) criteria.uniqueResult();
	}

	@Override
	public InventoryQuantityAdjustment findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InventoryQuantityAdjustment findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InventoryQuantityAdjustment> findAll() {
		Criteria criteria = getCurrentSession().createCriteria(InventoryQuantityAdjustment.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("inventory_adj_id"));
		return criteria.list();
	}

	@Override
	public void create(InventoryQuantityAdjustment entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
	    session.clear();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<InventoryQuantityAdjustment> findAllInventoryOfCompany(Long CompanyId) {
		List<InventoryQuantityAdjustment> list = new ArrayList<>();
		Session session =  getCurrentSession();
		Criteria criteria = session.createCriteria(InventoryQuantityAdjustment.class);
		criteria.add(Restrictions.eq("company.company_id", CompanyId));
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("inventory_adj_id"));
		list = criteria.list();
		session.clear();
		return list; 
	}
	@Override
	public void update(InventoryQuantityAdjustment entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
	    session.clear();
	}

	@Override
	public void merge(InventoryQuantityAdjustment entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(InventoryQuantityAdjustment entity) throws MyWebException {
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
	public boolean validatePreTransaction(InventoryQuantityAdjustment entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(InventoryQuantityAdjustment entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InventoryQuantityAdjustment isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from InventoryQuantityAdjustment where inventory_adj_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
			query.executeUpdate();
			msg= "Inventory Record Deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete Inventory Record";			
		}
		return msg;
	}

}
