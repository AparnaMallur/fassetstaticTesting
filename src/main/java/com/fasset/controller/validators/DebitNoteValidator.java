package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.DebitNote;

@Component
public class DebitNoteValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return DebitNote.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		DebitNote debitNote = (DebitNote) target;
		Long supplierId = debitNote.getSupplierId();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateString", "error.dateString", "Date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "round_off", "error.round_off", "Amount is required");
		
		if(supplierId == 0){
			errors.rejectValue("supplierId", "error.supplierId", "Select supplier");
		}
		
		if(debitNote.getPurchaseEntryId()==null || debitNote.getPurchaseEntryId() == 0){
			errors.rejectValue("purchaseEntryId", "error.purchaseEntryId", "Select purchase bill no");
		}
		if(debitNote.getDescription() == 0){
			errors.rejectValue("description", "error.description", "Select description");
		}
		else if(debitNote.getDescription() == 7)
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "remark", "error.remark", "Remark is required");
		}
		
		if(debitNote.getSubledgerId() == 0){
			errors.rejectValue("subledgerId", "error.subledgerId", "Select SubLedger");
		}
	}

}