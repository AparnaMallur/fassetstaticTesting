/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.CompanyStatutoryType;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class ClientValidationChecklistValidator extends MyValidator implements Validator{

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return ClientValidationChecklist.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "checklist_name","error.checklist_name", "Checklist name is required");
	}

}
