package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.CreditNoteReportForm;

@Component
public class CreditNoteReportFormValidator extends MyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return CreditNoteReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CreditNoteReportForm creditNoteReportForm = (CreditNoteReportForm) target;
		LocalDate from_date = creditNoteReportForm.getFromDate();
		LocalDate to_date = creditNoteReportForm.getToDate();
		
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate","error.fromDate", "From date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDate","error.toDate", "To date is required");
		
		if(from_date != null && to_date != null)
		{
			if(from_date.compareTo(to_date) > 0){
				errors.rejectValue("fromDate","error.fromDate", "From date should be less than to date");
			}
		}
		
		if((creditNoteReportForm.getCompanyId()!=null)&&creditNoteReportForm.getCompanyId()==0)
		{
			errors.rejectValue("companyId","error.companyId", "Select Company");
		}
		
		if(creditNoteReportForm.getOption()==0)
		{
			errors.rejectValue("option","error.option", "Select Type");
		}
	}

}
