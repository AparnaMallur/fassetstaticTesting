package com.fasset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.fasset.dao.interfaces.IEmployeeDAO;
import com.fasset.entities.Employee;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IEmployeeService;

@Service
public class EmployeeServiceImpl implements IEmployeeService{

	@Autowired
	private IEmployeeDAO employeeDao;
	
	@Override
	public void add(Employee entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Employee entity) {
		Employee emp= new Employee();
		try {
			emp=employeeDao.findOne(entity.getEmployee_id());
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		employeeDao.update(entity);
		
	}

	@Override
	public List<Employee> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee getById(Long id) {
		// TODO Auto-generated method stub
		return employeeDao.getById(id);
	}

	@Override
	public Employee getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Employee entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Employee entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Employee isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Employee> findAllEmployeeReletedToCompany(Long company_id) {
		// TODO Auto-generated method stub
		return employeeDao.findAllEmployeeReletedToCompany(company_id);
	}

	@Override
	public String saveEmployee(Employee employee) {
		
		/*
		 * String m=employeeMasterDao.saveEmployee(employeeMaster); return m;
		 */
		String id=employeeDao.saveEmployee(employee);
		if(id!=null)
		{
			return "Employee saved succsfully";
		}
		else
		{
			return "Please try again ";
		}
		
	}

	@Override
	public List<Employee> findAll() {
		// TODO Auto-generated method stub
		return employeeDao.findAll();
	}

	@Override
	public String deleteByIdValue(Long employee_Id) {
		// TODO Auto-generated method stub
		return employeeDao.deleteByIdValue(employee_Id);
	}

	@Override
	public List<Employee> findAllListing() {
		// TODO Auto-generated method stub
		return employeeDao.findAll();
	}

	@Override
	public void create(Employee entity) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Employee loadEmployeeByEmployeename(String employeeName,Long company_id) {

		return employeeDao.loadEmployeeByEmployeename(employeeName,company_id);
	}

}