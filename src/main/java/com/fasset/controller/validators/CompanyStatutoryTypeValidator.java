/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.CompanyStatutoryType;
import com.fasset.service.interfaces.ICompanyStatutoryTypeService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class CompanyStatutoryTypeValidator extends MyValidator implements Validator{

	@Autowired
	private ICompanyStatutoryTypeService  typeService ;
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return CompanyStatutoryType.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		CompanyStatutoryType type = (CompanyStatutoryType)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company_statutory_name","error.company_statutory_name", "Company statutory name is required");
		
		if(type.getCompany_statutory_id()==null)
		{
		if(type.getCompany_statutory_name()!="")
		{
		CompanyStatutoryType type1 =typeService.isExists(type.getCompany_statutory_name());
		if(type1!=null)
		{
			errors.rejectValue("company_statutory_name","error.company_statutory_name", "Company statutory name is already exists");
		}
		}
		}
	}

	
	
}
