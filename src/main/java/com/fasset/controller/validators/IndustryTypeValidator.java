/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.IndustryType;
import com.fasset.service.interfaces.IindustryTypeService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class IndustryTypeValidator extends MyValidator implements Validator{

	@Autowired
	private IindustryTypeService typeService ;
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return IndustryType.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		IndustryType type = (IndustryType)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "industry_name","error.industry_name", "Industry name is required");
		if(type.getIndustry_id()==null)
		{
		if(type.getIndustry_name()!="")
		{
			IndustryType type1 =typeService.isExists(type.getIndustry_name());
		if(type1!=null)
		{
			errors.rejectValue("industry_name","error.industry_name", "Industry name is already exists");
		}
		}
		}
	}

	
	
}
