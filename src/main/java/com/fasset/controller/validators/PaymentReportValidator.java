package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.PaymentReportForm;
import com.fasset.form.PurchaseReportForm;

@Component
public class PaymentReportValidator extends MyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return PaymentReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PaymentReportForm  form = (PaymentReportForm)target;
		LocalDate from_date = form.getFromDate();
		LocalDate to_date = form.getToDate();
        Long option=form.getOption();
        
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate","error.fromDate", "From date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDate","error.toDate", "To date is required");
		
		if(from_date != null && to_date != null)
		{
			if(from_date.compareTo(to_date) > 0){
				errors.rejectValue("fromDate","error.fromDate", "From date should be less than to date");
			}
		}
		
		if(form.getCompanyId()!=null && form.getCompanyId()==0)
		{
			errors.rejectValue("companyId","error.companyId", "Select company name");
			
		}
		if(option == 0) {
			errors.rejectValue("option","error.option", "Select option");
		}
		
	}
	
}
