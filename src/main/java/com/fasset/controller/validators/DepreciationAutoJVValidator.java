
package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.fasset.entities.DepreciationAutoJV;



@Component
public class DepreciationAutoJVValidator extends MyValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return DepreciationAutoJV.class.equals(clazz);
	}

	
	@Override
	public void validate(Object target, Errors errors) {
		
		DepreciationAutoJV depreciationAutoJV = (DepreciationAutoJV) target;

	}

}