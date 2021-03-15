package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.BillsReceivableReportForm;
import com.fasset.form.DebitNoteReportForm;

@Component
public class BillsReceivableReportFormValidator  extends MyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return BillsReceivableReportForm.class.equals(clazz);
	}
	@Override
	public void validate(Object target, Errors errors) {
		
		BillsReceivableReportForm form = (BillsReceivableReportForm)target;
		LocalDate to_date = form.getToDate();

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDate","error.toDate", "To date is required");
		
	
		
		if(form.getClientId() !=null && form.getClientId() == 0)
		{
			errors.rejectValue("clientId","error.clientId", "Select Client Name");
			
		}
	}
}
