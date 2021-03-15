package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.User;
import com.fasset.form.Ask4Mail;
import com.fasset.service.interfaces.IUserService;

@Component
public class Ask4MailValidator implements Validator{
	
	@Autowired
	private IUserService userService;
	
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Ask4Mail.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Ask4Mail ask4Mail = (Ask4Mail) target;
		String email = ask4Mail.getLogin();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login","error.login", "Email id is required");
		
		if(email != null && email != ""){
			if(!email.matches(EMAIL_PATTERN)){
				errors.rejectValue("login","error.login", "Enter valid mail id");
			}
			else{
				User user = userService.loadUserByUsername(email,"");
				if(user == null){
					errors.rejectValue("login","error.login", "This user is not registered");
				}
				
			}
		}
	}

}
