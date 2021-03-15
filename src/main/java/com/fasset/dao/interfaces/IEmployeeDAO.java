package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Employee;

public interface IEmployeeDAO extends IGenericDao<Employee>{

	List<Employee> findAllEmployeeReletedToCompany(Long company_id);
	public String saveEmployee(Employee employee);
	public List<Employee> findAll();
	public String deleteByIdValue(Long employee_Id);
	public Employee getById(Long employee_Id);
	List<Employee> findAllListing();
	public void create(Employee entity);
	public void update(Employee entity);
	Employee loadEmployeeByEmployeename(String userName,Long company_id);
}