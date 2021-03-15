package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.StockReportForm;

@Component
public class StockReportValidator extends MyValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return StockReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		StockReportForm stockReportForm = (StockReportForm) target;
		/*LocalDate from_date = stockReportForm.getFromDate();
		LocalDate to_date = stockReportForm.getToDate();		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate","error.fromDate", "From date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDate","error.toDate", "To date is required");
		
		if(from_date != null && to_date != null)
		{
			if(from_date.compareTo(to_date) > 0){
				errors.rejectValue("fromDate","error.fromDate", "From date should be less than to date");
			}
		}*/
		
		if(stockReportForm.getClientId()!=null && stockReportForm.getClientId()==0)
		{
			errors.rejectValue("clientId","error.clientId", "Select Company Name");
			
		}
		
	/*	if(stockReportForm.getProductId()!=null && stockReportForm.getProductId()==0)
		{
			errors.rejectValue("productId","error.productId", "Select Product Name");
			
		}*/
	}

}
