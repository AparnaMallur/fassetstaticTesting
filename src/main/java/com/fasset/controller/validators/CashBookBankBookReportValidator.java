package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.CashBookBankBookReportForm;

@Component
public class CashBookBankBookReportValidator extends MyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return CashBookBankBookReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CashBookBankBookReportForm form = (CashBookBankBookReportForm)target;
		Long companyID = form.getCompanyId();
		Integer type = form.getType();
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
		
		if(companyID != null && companyID == 0) {
			errors.rejectValue("companyId","error.companyId", "Select Company Name");
		}
		
		if(type == 0) {
			errors.rejectValue("type","error.type", "Select Payment Type");
		}
	}

}
