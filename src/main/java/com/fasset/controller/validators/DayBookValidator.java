package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.DayBookReportForm;


@Component
public class DayBookValidator extends MyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return DayBookReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		/*DayBookReportForm form = (DayBookReportForm)target;
		Long companyID = form.getCompanyId();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date","error.date", "Date is required");
		
		
		if(companyID != null && companyID == 0) {
			errors.rejectValue("companyId","error.companyId", "Select Company Name");
		}*/
		
	}

}
