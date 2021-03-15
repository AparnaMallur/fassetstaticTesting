/**
 * 
 */
package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.UnitOfMeasurement;

/**
 * @author "Vishwajeet"
 *
 */

@Component
public class UOMValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return UnitOfMeasurement.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "unit", "error.unit","Unitof measurement is required");
	}

}
