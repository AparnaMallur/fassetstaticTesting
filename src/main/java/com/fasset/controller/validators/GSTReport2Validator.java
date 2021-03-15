package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.GSTReport2Form;
import com.fasset.form.PurchaseReportForm;
import com.fasset.form.ProfitAndLossReportForm;

@Component
public class GSTReport2Validator extends MyValidator implements Validator{

	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return GSTReport2Form.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		GSTReport2Form  form = (GSTReport2Form)target;
		Integer month = form.getMonth();
		Long yearId = form.getYearId();
		if(form.getCompanyId()!=null && form.getCompanyId()==0)
		{
			errors.rejectValue("companyId","error.companyId", "Select company name");
			
		}
		if(month == 0) {
			errors.rejectValue("month","error.month", "Select month");
		}
		
		if(yearId == 0) {
			errors.rejectValue("yearId","error.yearId", "Select year");
		}
	}
	
}
