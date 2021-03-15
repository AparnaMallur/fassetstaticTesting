/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.User;
import com.fasset.service.interfaces.IUserService;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Component
public class KPOExecutiveValidator extends MyValidator implements Validator {

	@Autowired
	private IUserService userService;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		String email = user.getEmail();
		Long country_id = user.getCountry_id();
		Long state_id = user.getState_id();
		Long city_id = user.getCity_id();
		Long role_id = user.getRole_id();
		Long managerId = user.getManager();
		String adhaar = user.getAdhaar_no();
		String panno = user.getPan_no();
		String pwd=user.getPassword();
		Long companyId=user.getCompany_id();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "first_name", "error.first_name", "First name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "last_name", "error.last_name", "Last name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobile_no", "error.mobile_no", "Mobile number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.email", "Email ID is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pan_no", "error.pan_no", "PAN number is required");

		if (user.getPassword() != null && user.getPassword() != "" && user.getPassword().length() < 8) {
			errors.rejectValue("password", "error.password", "Password should contain at least 8 character");
		}

		if (email != null && email != "") {
			if (!email.matches(EMAIL_PATTERN)) {
				errors.rejectValue("email", "error.email", "Enter valid mail id");
			} else {
				if (user.getUser_id() == null) {
					User user1 = userService.loadUserByUsername(email,pwd);
					if (user1 != null) {
						errors.rejectValue("email", "error.email", "Email id already exists.");
					}
				}

			}
		}
		if (panno != "" && panno != null) {
			if (!panno.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
				errors.rejectValue("pan_no", "error.user.pan_no", "Enter valid Pan No");
			}
		}
		if (adhaar != null && adhaar != "") {
			if (adhaar.length() != 12) {
				errors.rejectValue("adhaar_no", "error.user.adhaar_no", "Enter 12 digit Adhaar No");

			} else if (!adhaar.matches("^[0-9]+\\.?[0-9]+$")) {
				errors.rejectValue("adhaar_no", "error.user.adhaar_no", "Enter valid Adhaar No");
			}
		}
		if (country_id == 0) {
			errors.rejectValue("country_id", "error.country_id", "Select Country");
		}

		if (state_id == 0) {
			errors.rejectValue("state_id", "error.state_id", "Select State");
		}

		if (city_id == 0) {
			errors.rejectValue("city_id", "error.city_id", "Select City");
		}

		if (role_id == 0) {
			errors.rejectValue("role_id", "error.role_id", "Select Role");
		}

		if (managerId == null || managerId == 0) {
			errors.rejectValue("manager", "error.manager", "Select Manager");
		}
	}

}
