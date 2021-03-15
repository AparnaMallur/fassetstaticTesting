/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.AccountGroup;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.ClientValidationChecklistStatus;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ClientValidationChecklistForm;
import com.fasset.service.interfaces.IClientValidationChecklistService;
import com.fasset.service.interfaces.IClientValidationChecklistStatusService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class ClientValidationChecklistStatusValidator extends MyValidator implements Validator{
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		
		return ClientValidationChecklistForm.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		ClientValidationChecklistForm form = (ClientValidationChecklistForm)target;
		LocalDate from_date = form.getStatus().getFromDate();
		LocalDate to_date = form.getStatus().getToDate();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "status.fromDate","error.status.fromDate", "From date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "status.toDate","error.status.toDate", "To date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "status.emplimit","error.status.emplimit", "Employee Limit is required");
		if(form.getStatus().getYearRange()==null) {
			errors.rejectValue("status.yearRange","error.status.yearRange", "Select Financial Year");
		}
		if(from_date != null && to_date != null)
		{
			if(from_date.compareTo(to_date) > 0){
				errors.rejectValue("status.fromDate","error.status.fromDate", "From date should be less than to date");
			}
		}
		
	}

}
