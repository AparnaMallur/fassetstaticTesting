package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.ChangePassword;
import com.fasset.service.interfaces.IUserService;

@Component
public class ChangePassValidator implements Validator {

	@Autowired
	private IUserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return ChangePassword.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ChangePassword changePass = (ChangePassword) target;
		String pass = changePass.getPass(), cpass = changePass.getConfPass(), userName = changePass.getUserName(),
				oldPass = changePass.getOldPass();

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldPass", "error.oldPass", "Old password is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pass", "error.pass", "New password is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confPass", "error.confPass", "Confirm password is required");

		if (oldPass != null && oldPass != "") {
			if (userService.authenticate(userName, oldPass)!=1) {
				errors.rejectValue("oldPass", "error.oldPass", "You entered wrong password");
			}
			
			else if (pass != null && pass != "" && cpass != null && cpass != "") {
				if (!pass.equals(cpass)) {
					errors.rejectValue("confPass", "error.confPass", "Password does not match");
				}
			}
		} 
		
	}

}
