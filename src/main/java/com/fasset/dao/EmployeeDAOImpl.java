package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IEmployeeDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Employee;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;

@Repository
@Transactional
public class EmployeeDAOImpl extends AbstractHibernateDao<Employee> implements IEmployeeDAO{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Employee findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.eq("employee_id", id));
		return (Employee) criteria.uniqueResult();
	}

	@Override
	public Employee findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> findAll() {
		
		List<Employee> employeeList = new ArrayList<Employee>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		employeeList = criteria.list();
		
		return employeeList;
	}

	@Override
	public void create(Employee entity) {
		Session session = sessionFactory.getCurrentSession();
		        session.save(entity);
		        session.clear();
		
	}

	@Override
	public void update(Employee entity)  {
		Session session = sessionFactory.getCurrentSession();
		        session.merge(entity);
		        session.flush();
		        session.clear();
		
	}

	@Override
	public void merge(Employee entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Employee entity) throws MyWebException {
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
	public boolean validatePreTransaction(Employee entity) throws MyWebException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh(Employee entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Employee isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Employee> findAllEmployeeReletedToCompany(Long company_id) {
		List<Employee> list =  new ArrayList<Employee>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT employee from Employee employee WHERE employee.company.company_id =:company_id");
			query.setParameter("company_id", company_id);
			
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Employee)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
              session.clear();
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		return list;
	}

	@Override
	public String saveEmployee(Employee employee) {
		Session session = sessionFactory.getCurrentSession();
		
		String successMsg="";
		try {
			session.save(employee);
		    successMsg= "Employee saved successfully";
		} catch (Exception e) {
			successMsg = "Employee is not saved";

		}
		
		return successMsg;
	}

	@Override
	public String deleteByIdValue(Long employee_Id) {
		Session session = sessionFactory.getCurrentSession();
		String successMsg="";
		Query query = session.createQuery("delete from Employee where employee_id =:employee_id");
			  query.setParameter("employee_id", employee_Id);
			 
		try {
			query.executeUpdate();
			successMsg = "Employee Deleted Successfully";
		} catch (Exception e) {
			successMsg = "Employee is not deleted";

		}
	return successMsg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> findAllListing() {
		
		List<Employee> list = new ArrayList<Employee>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
				 criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		list = criteria.list();
		
		return list;
	}
	
	@Override
	public Employee getById(Long employee_Id)
	{
		
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("employee_id", employee_Id));
		return (Employee) criteria.uniqueResult();
			
	}
	@Override
	public Employee loadEmployeeByEmployeename(String employeeName,Long company_id) {
		Criteria criteria = getCurrentSession().createCriteria(Employee.class);
		criteria.add(Restrictions.eq("code", employeeName));
		criteria.add(Restrictions.eq("company.company_id", company_id));
		return (Employee) criteria.uniqueResult();
	}

}
