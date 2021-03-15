package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.joda.time.LocalDate;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Contra;

@Component
public class ContraValidator extends MyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Contra.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		Contra contra=(Contra) target;
		
		LocalDate date=contra.getDate();
		Long depositeTo=contra.getDepositeTo();
		Long withdrawFrom=contra.getWithdrawFrom();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount","error.amount", "Amount is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type","error.type", "Type cannot be blank");
		if(contra.getType()!=null&&contra.getType()==0){
			errors.rejectValue("type","error.type", "Type cannot be blank");
		}
		else if((contra.getType()!=null)&&(contra.getType()==MyAbstractController.CONTRA_DEPOSITE)){
			if(depositeTo == 0){
				errors.rejectValue("depositeTo","error.depositeTo", "Deposite to cannot be blank");
			}		
		}
		else if((contra.getType()!=null)&&(contra.getType()==MyAbstractController.CONTRA_WITHDRAW)){
			if(withdrawFrom==0){
				errors.rejectValue("withdrawFrom","error.withdrawFrom", "Withdraw from cannot be blank");
			}
		}
		else if((contra.getType()!=null)&&(contra.getType()==MyAbstractController.CONTRA_TRANSFER)){
			if(depositeTo==0){
				errors.rejectValue("depositeTo","error.depositeTo", "Transfer to cannot be blank");
			}
			else if(withdrawFrom==0){
				errors.rejectValue("withdrawFrom","error.withdrawFrom", "Transfer from cannot be blank");
			}
			else if(withdrawFrom == depositeTo){
				errors.rejectValue("withdrawFrom","error.withdrawFrom", "You have selected same account for transfer");
			}
		}
		
		if(date==null){
			errors.rejectValue("date","error.date", "Date cannot be blank");
		}
	}

}
