package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.ExecutiveTimesheet;


@Component
public class ExecutiveTimesheetValidator extends MyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return ExecutiveTimesheet.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ExecutiveTimesheet executiveTimesheet = (ExecutiveTimesheet)target;
		
		if(executiveTimesheet.getTimesheetDetails()==null || executiveTimesheet.getTimesheetDetails()=="")
		{
		Long company_id = executiveTimesheet.getCompany_id();
		Long service_id = executiveTimesheet.getService_id();
		/*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "from_date","error.from_date", "From date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "to_date","error.to_date", "To date is required");*/
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date","error.date", "Date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "total_time","error.total_time", "Total time is required");
		if(company_id == 0){
			errors.rejectValue("company_id","error.company_id", "Please select company");
		}
		if(service_id == 0){
			errors.rejectValue("service_id","error.service_id", "Please select service");
		}
		}
		
	}

}
