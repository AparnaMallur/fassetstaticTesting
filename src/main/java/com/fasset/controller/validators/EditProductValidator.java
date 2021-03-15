package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.PurchaseEntryProductEntityClass;

@Component
public class EditProductValidator extends MyValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> clazz) {
		return PurchaseEntryProductEntityClass.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PurchaseEntryProductEntityClass productpurchase=(PurchaseEntryProductEntityClass) target;
		long is_gst=productpurchase.getIs_gst();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quantity","error.quantity", "Quantity is required. Put 0 if it is not required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rate","error.rate", "Rate name is required. Put 0 if it is not required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "discount","error.discount", "Discount is required. Put 0 if it is not required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "labour_charges","error.labour_charges", "Labour charge is required. Put 0 if it is not required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "freight","error.freight", "Freight charge is required. Put 0 if it is not required");
		if(is_gst==2)
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "VAT","error.VAT", "VAT is required. Put 0 if it is not required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "VATCST","error.VATCST", "CST is required. Put 0 if it is not required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "Excise","error.Excise", "Excise is required. Put 0 if it is not required");
	    }
		else
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "CGST","error.CGST", "CGST is required. Put 0 if it is not required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "SGST","error.SGST", "SGST is required. Put 0 if it is not required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "IGST","error.IGST", "IGST is required. Put 0 if it is not required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state_com_tax","error.state_com_tax", "SCT is required. Put 0 if it is not required");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "UOM","error.UOM", "UOM is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "Others","error.Others", "Other is required. Put 0 if it is not required");
	
		
		
		
	}
}
