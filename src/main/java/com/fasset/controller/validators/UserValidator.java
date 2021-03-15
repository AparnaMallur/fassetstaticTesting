package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.User;
import com.fasset.service.interfaces.IUserService;

@Component
public class UserValidator implements Validator{
	
	@Autowired
	private IUserService userService;
	
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User)target;
		
		String firstName = user.getFirst_name();
		String lastName = user.getLast_name();
		String mobile = user.getMobile_no();
		String email = user.getEmail();
		Long userId = user.getUser_id(); 
		String pass=user.getPassword();
		Long companyId=user.getCompany_id();
		if (user.getRole_id()!=1){
			 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "joinDate","error.joinDate", "Joining date is required");
			    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "adhaar_no","error.adhaar_no", "Aadhar number is required");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "first_name","error.first_name", "First name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "last_name","error.last_name", "Last name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email","error.email", "Email ID is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobile_no","error.mobile_no", "Mobile number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pan_no","error.pan_no", "PAN number is required");
	    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","error.password", "Password is required");
	    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "current_address","error.current_address", "Current address is required");
	    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "permenant_address","error.permenant_address", "Permanant address is required");
	    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pin_code","error.pin_code", "Pincode is required");
	   
	    //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate","error.fromDate", "From date is required");
	    //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDate","error.toDate", "To Date is required");
	    
	   if(user.getRole_id()!=null && user.getRole_id() == 0)
	   {
			errors.rejectValue("role_id","error.role_id", "Select Role");
	   }
	    if(user.getPassword()!=null && user.getPassword()!="")
		{
	    	if(user.getPassword().length()<8)
	    	{
			errors.rejectValue("password","error.password", "Password should contain at least 8 character");
	    	}
		}
		
	
		if(firstName != null && firstName != ""){
			if(!firstName.matches("^[\\p{L} .'-]+$")){
				errors.rejectValue("first_name","error.first_name", "Enter only character");
			}
		}
		
		if(lastName != null && lastName != ""){
			if(!lastName.matches("^[\\p{L} .'-]+$")){
				errors.rejectValue("last_name","error.last_name", "Enter only character");
			}
		}
		
		if(email != null && email != ""){
			if(!email.matches(EMAIL_PATTERN)){
				errors.rejectValue("email","error.email", "Enter valid mail id");
			}
			else{
				if(userId == null){
					//if (user.getRole_id()!=1){
					User user2=userService.getUserByUserName(companyId, email);
					
					if(user2!=null){
						errors.rejectValue("email","error.email", "Email id already exists.");
					}else{
					User user1 = userService.loadUserByUsername(email,pass);
					if(user1 != null){
						errors.rejectValue("email","error.email", "Email id already exists.");
					}
					}
				}
			}
		}
		
		if(mobile != null && mobile != ""){
			if(!mobile.matches("\\d{10}")){
				errors.rejectValue("mobile_no","error.mobile_no", "Enter valid mobile number");
			}
		}
		
		if (user.getCountry_id() == 0) {
			errors.rejectValue("country_id", "error.country_id", "Select Country Name");
		}
		
		if (user.getState_id() == 0) {
			errors.rejectValue("state_id", "error.state_id", "Select State Name");
		}
		
		if (user.getCity_id() == 0) {
			errors.rejectValue("city_id", "error.city_id", "Select City Name");
		}
		
		if(user.getCompany_id()!=null && user.getCompany_id()==0 )
		{
			errors.rejectValue("company_id","error.company_id", "Select Company Name");
		}

	}

}
