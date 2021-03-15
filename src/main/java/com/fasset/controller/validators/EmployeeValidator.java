package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.fasset.entities.Employee;
import com.fasset.service.interfaces.IEmployeeService;

@Component
public class EmployeeValidator extends MyValidator implements Validator
{
	@Autowired
	private IEmployeeService employeeService;
	
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return Employee.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Employee employee=(Employee)target;
		
		String code = employee.getCode();
		Long employee_id=employee.getEmployee_id();
		Long company_id=employee.getCompany().getCompany_id();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code","error.code", "Employee ID is required");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name","error.name", "Name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "doj","error.doj", "Date of Joining is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobile","error.mobile", "Mobile number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "current_address","error.current_address", "Enter Address");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pan","error.pan", "Pan number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "adharNo","error.adharNo", "Aadhaar 12  number is required");
		
		if(code == null)
		{
				errors.rejectValue("code","error.code", "Enter Employee ID");
		}
			
		else
		{
			if(employee_id == null)
			{
				Employee user1 =employeeService.loadEmployeeByEmployeename(code,company_id);
				if(user1 != null)
				{
					errors.rejectValue("code","error.code", "Employee ID already exists.");
				}
			}
			}
		
	}

}
