package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.CreditNote;

@Component
public class CreditNoteValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return CreditNote.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CreditNote creditNote = (CreditNote) target;
		Long cId = creditNote.getCustomerId();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateString", "error.dateString", "Date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "round_off", "error.round_off", "Amount is required");
		
		if(cId == 0){
			errors.rejectValue("customerId", "error.customerId", "Select customer");
		}
		
		if(creditNote.getSalesEntryId()==null || creditNote.getSalesEntryId()== 0){
			errors.rejectValue("salesEntryId", "error.salesEntryId", "Select sales bill no");
		}
		
		if(creditNote.getDescription() == 0){
			errors.rejectValue("description", "error.description", "Select description");
		}
		else if(creditNote.getDescription() == 7)
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "remark", "error.remark", "Remark is required");
		}
		
		if(creditNote.getSubledgerId() == 0){
			errors.rejectValue("subledgerId", "error.subledgerId", "Select SubLedger");
		}
	}

}
