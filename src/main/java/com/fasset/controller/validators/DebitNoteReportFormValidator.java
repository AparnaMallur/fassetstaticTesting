package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.fasset.form.DebitNoteReportForm;

@Component
public class DebitNoteReportFormValidator extends MyValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return DebitNoteReportForm.class.equals(clazz);
	}
	@Override
	public void validate(Object target, Errors errors) {
		
		DebitNoteReportForm form = (DebitNoteReportForm)target;
		LocalDate from_date = form.getFromDate();
		LocalDate to_date = form.getToDate();

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate","error.fromDate", "From date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDate","error.toDate", "To date is required");
		
		if(from_date != null && to_date != null)
		{
			if(from_date.compareTo(to_date) > 0){
				errors.rejectValue("fromDate","error.fromDate", "From date should be less than to date");
			}
		}
	
		if((form.getCompanyId()!=null)&&(form.getCompanyId()==0))
		{
			errors.rejectValue("companyId","error.companyId", "Select Company");
		}
		
		if(form.getOption()==0)
		{
			errors.rejectValue("option","error.option", "Select Type");
		}
		
	}

	
	
	
}
