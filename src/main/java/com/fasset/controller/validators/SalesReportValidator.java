package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.DebitNoteReportForm;
import com.fasset.form.SalesReportForm;

@Component
public class SalesReportValidator extends MyValidator implements Validator {

	
	@Override
	public boolean supports(Class<?> clazz) {
		return SalesReportForm.class.equals(clazz);
	}
	@Override
	public void validate(Object target, Errors errors) {
		
		SalesReportForm form = (SalesReportForm)target;
		LocalDate from_date = form.getFromDate();
		LocalDate to_date = form.getToDate();
		Integer option = form.getOption();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate","error.fromDate", "From date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDate","error.toDate", "To date is required");
		
		if(from_date != null && to_date != null)
		{
			if(from_date.compareTo(to_date) > 0){
				errors.rejectValue("fromDate","error.fromDate", "From date should be less than to date");
			}
		}
		
		/*if(ledid == 0) {
			errors.rejectValue("ledgerId","error.ledgerId", "Select ledger name");
		}
		*/
		if(form.getCompanyId()!=null && form.getCompanyId()==0)
		{
			errors.rejectValue("companyId","error.companyId", "Select company name");
			
		}
		
		if(option <= 0){
			errors.rejectValue("option","error.option", "Select option");
		}
	}
}
