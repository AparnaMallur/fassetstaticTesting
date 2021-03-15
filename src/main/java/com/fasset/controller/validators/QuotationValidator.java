package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.fasset.entities.Quotation;
import com.fasset.service.interfaces.IQuotationService;

@Component
public class QuotationValidator extends MyValidator implements Validator {

	@Autowired
	private IQuotationService quoteService;

	
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Quotation.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Quotation quotation= (Quotation)target;
		
		/*Long gId=quotation.getFrequency_id();
		Long pId=quotation.getService_id();
		if(quotation.getSave_id()==0)
		{
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount","error.amount", "Amount can not be empty");
		if(gId == 0) {
			errors.rejectValue("frequency_id","error.frequency_id", "Select Frequency");
		}
		
		if(pId == 0) {
			errors.rejectValue("service_id","error.service_id", "Select Service");
		}
		}*/
	}
}
