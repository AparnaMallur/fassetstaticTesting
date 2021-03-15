package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.CashFlowSummaryForm;

@Component
public class CashFlowSummaryReportValidator extends MyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return CashFlowSummaryForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CashFlowSummaryForm cashFlowSummaryForm = (CashFlowSummaryForm) target;
		LocalDate from_date = cashFlowSummaryForm.getFromDate();
		LocalDate to_date = cashFlowSummaryForm.getToDate();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate","error.fromDate", "From date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDate","error.toDate", "To date is required");
		
		if(from_date != null && to_date != null)
		{
			if(from_date.compareTo(to_date) > 0){
				errors.rejectValue("fromDate","error.fromDate", "From date should be less than to date");
			}
		}
		
		if(cashFlowSummaryForm.getCompanyId()!=null && cashFlowSummaryForm.getCompanyId()==0)
		{
			errors.rejectValue("companyId","error.companyId", "Select Company Name");
			
		}
	}

}
