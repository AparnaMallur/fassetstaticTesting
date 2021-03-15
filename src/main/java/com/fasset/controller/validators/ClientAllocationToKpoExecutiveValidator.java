package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.fasset.entities.ClientAllocationToKpoExecutive;
@Component
public class ClientAllocationToKpoExecutiveValidator extends MyValidator  implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ClientAllocationToKpoExecutive.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ClientAllocationToKpoExecutive clientAllocationToKpoExecutive = (ClientAllocationToKpoExecutive)target;
		Long company_id = clientAllocationToKpoExecutive.getCompany_id();
		Long user_id = clientAllocationToKpoExecutive.getUser_id();
		
		if(user_id == 0){
			errors.rejectValue("user_id","error.user_id", "Please select user");
		}
		
		if(company_id == 0){
			errors.rejectValue("company_id","error.company_id", "Please select company");
		}
		
	}

}
