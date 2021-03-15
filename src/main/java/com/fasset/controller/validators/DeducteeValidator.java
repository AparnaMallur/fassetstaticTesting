package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.AccountGroup;
import com.fasset.entities.Deductee;
import com.fasset.service.interfaces.IDeducteeService;

@Component
public class DeducteeValidator extends MyValidator implements Validator{

	@Autowired
	private IDeducteeService deducteeService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AccountGroup.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Deductee deductee = (Deductee) target ;
		
		Long industryId = deductee.getIndustry_id();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deductee_title","error.deductee_title", "TDS Type is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value1", "error.value1", "Rate is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "effective_date", "error.effective_date", "Effective Date is required");
		if (deductee.getValue1() != null && deductee.getValue1() != "") {
			if (!deductee.getValue1().matches("^([+-]?\\d*\\.?\\d*)$")) {
				errors.rejectValue("value1", "error.value1", "Enter valid Rate");
			}
		}
		
		if(deductee.getDeductee_id()==null)
		{
			if(deductee.getDeductee_title()!=""){
				Deductee deductee1 =deducteeService.isExists1(deductee.getDeductee_title(),deductee.getEffective_date());
				if(deductee1 != null ){
					errors.rejectValue("deductee_title","error.deductee_title", "TDS Type for this Effective Date already exists");
				}
			}
		}
		/*if(industryId == 0) {
			errors.rejectValue("industry_id","error.industry_id", "Select Industry Type");
		}*/
		
		
	}

}
