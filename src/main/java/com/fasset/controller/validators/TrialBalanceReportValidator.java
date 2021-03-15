package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.TrialBalanceReportForm;

@Component
public class TrialBalanceReportValidator extends MyValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return TrialBalanceReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TrialBalanceReportForm trialBalanceReportForm = (TrialBalanceReportForm) target;
		LocalDate from_date = trialBalanceReportForm.getFromDate();
		LocalDate to_date = trialBalanceReportForm.getToDate();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate","error.fromDate", "From date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDate","error.toDate", "To date is required");
		
		if(from_date != null && to_date != null)
		{
			if(from_date.compareTo(to_date) > 0){
				errors.rejectValue("fromDate","error.fromDate", "From date should be less than to date");
			}
		}
		
		
		if(trialBalanceReportForm.getCompanyId()!=null && trialBalanceReportForm.getCompanyId()==0)
		{
			errors.rejectValue("companyId","error.companyId", "Select Company Name");
			
		}
	}

}
