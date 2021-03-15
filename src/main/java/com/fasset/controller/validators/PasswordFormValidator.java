package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.User;
import com.fasset.form.ChangePassword;
import com.fasset.service.interfaces.IUserService;

@Component
public class PasswordFormValidator implements Validator {
	@Autowired
	private IUserService userService;
	@Override
	public boolean supports(Class<?> clazz) {
		return ChangePassword.class.equals(clazz);
	}

	
	@Override
	public void validate(Object target, Errors errors) {
		ChangePassword changePass = (ChangePassword)target;
		String pass = changePass.getPass(), cpass = changePass.getConfPass();
		Long CompanyId=changePass.getCompany_id();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pass","error.pass", "Required field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confPass","error.confPass", "Required field");
	//	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company_id","error.company_id", "Required field");
		if(CompanyId == 0 || CompanyId == null){
			errors.rejectValue("company_id","error.company_id", "Please select Company");
		}
		if (pass != null && pass != "" && cpass != null && cpass != "") {
			if (!pass.equals(cpass)) {
				errors.rejectValue("confPass", "error.confPass", "Password does not match");
			}else {
				User user = userService.loadUserByUsername(changePass.getUserName(),changePass.getPass());
				if(user != null){
					errors.rejectValue("confPass","error.confPass", "please change password already exists.");
				}
			}
			
		} 
	}

}