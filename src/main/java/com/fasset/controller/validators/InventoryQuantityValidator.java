package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.InventoryQuantityAdjustment;

@Component
public class InventoryQuantityValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return InventoryQuantityAdjustment.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		InventoryQuantityAdjustment adjustment = (InventoryQuantityAdjustment) target;
		Long productId = adjustment.getProductId();
		Long yearId = adjustment.getYearId();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "error.dateString", "Please enter date");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity", "error.quantity", "Please enter quantity");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value", "error.value", "Please enter value");
		
		if(productId == 0){
			errors.rejectValue("productId", "error.productId", "Please select product");
		}
		
		if(yearId == 0){
			errors.rejectValue("yearId", "error.yearId", "Please select accounting year");
		}
	}

}
