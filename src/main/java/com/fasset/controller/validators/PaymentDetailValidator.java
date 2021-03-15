package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.PaymentDetails;

@Component
public class PaymentDetailValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return PaymentDetails.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity","error.quantity", "Quantity is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rate","error.rate", "Rate is required");		
	}

}
