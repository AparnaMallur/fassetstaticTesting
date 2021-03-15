package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Company;
import com.fasset.entities.User;
import com.fasset.form.UserForm;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.ICompanyService;

@Component
public class UserFormValidator implements org.springframework.validation.Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserForm.class.equals(clazz);
	}
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICompanyService companyService;
	
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public void validate(Object target, Errors errors) {
		UserForm userForm = (UserForm) target;
		User user = userForm.getUser();
		Company company = userForm.getCompany();
		String email = user.getEmail();
		Long companyTypeId = company.getCompanyTypeId();
		Long industryId=company.getIndustryTypeId();
		Long countryId = company.getCountryId();
		Long stateId = company.getStateId();
		Long cityId = company.getCityId();
		String panno = user.getPan_no();
		String companypan = company.getPan_no();
		String adhaar = user.getAdhaar_no();
		String gstno = company.getGst_no();
		String pass=user.getPassword();
		/*Integer status = company.getStatus();*/
		Long userId = user.getUser_id();
		String company_name=company.getCompany_name();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.first_name", "error.user.first_name", "First name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.last_name", "error.user.last_name", "Last name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.mobile_no", "error.user.mobile_no", "Mobile number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.pan_no", "error.user.pan_no", "PAN number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.company_name", "error.company.company_name", "Company name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.current_address", "error.company.current_address", "Current address is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.permenant_address", "error.company.permenant_address", "Permenant address is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.pincode", "error.company.pincode", "Pincode is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.pan_no", "error.company.pan_no", "PAN number is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.gst_no", "error.company.gst_no", "GST number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.voucher_range", "error.company.voucher_range", "Vocuher Number Range  is required");
		
		if(email != null && email != ""){
			if(userForm.getRole_id()==MyAbstractController.ROLE_CLIENT)
			{				
				
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.empLimit", "error.company.empLimit", "Employee limit is required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.yearRange", "error.company.yearRange", "Select financial year");
			/*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.amount", "error.user.amount", "Amount  is required");*/

			}
			if(!email.matches(EMAIL_PATTERN)){
				errors.rejectValue("user.email","error.user.email", "Enter valid mail id");
			}
			else{
				if(userId == null){
					User user1 = userService.loadUserByUsername(email,pass);
					if(user1 != null){
						errors.rejectValue("user.email","error.user.email", "Email id already exists.");
					}
				}
			}
		}	
		
		
		if(userId==null)
		{
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.empLimit", "error.company.empLimit", "Employee limit is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.yearRange", "error.company.yearRange", "Select financial year");
			if(company_name!=null && company_name!="")
			{
				int compexist=companyService.isExistscompanyname(company_name);	
					if(compexist==1)
					{
						errors.rejectValue("company.company_name", "error.company.company_name", "Company name already Registered");
					}
			}
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.amount", "error.user.amount", "Amount is required");

			
//			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.password","error.user.password", "Password is required");
			if(pass.equals(""))
			{
				errors.rejectValue("user.password","error.user.password", "Password is required");
			}
			else
			{
				if(user.getPassword().length()<8)
				errors.rejectValue("user.password","error.user.password", "Password should contain at least 8 character");
			}			
			
		}
		if (companyTypeId !=null && companyTypeId == 0) {
			errors.rejectValue("company.companyTypeId", "error.company.companyTypeId", "Select company type");
		}
		if(industryId==0)
		{
			errors.rejectValue("company.industryTypeId", "error.company.industryTypeId", "Select Industry type");
		}
		if (countryId == 0) {
			errors.rejectValue("company.countryId", "error.company.countryId", "Select country");
		}

		if (stateId == 0) {
			errors.rejectValue("company.stateId", "error.company.stateId", "Select state");
		}

		if (cityId == 0) {
			errors.rejectValue("company.cityId", "error.company.cityId", "Select city");
		}

		if(panno.equals(""))
		{
			errors.rejectValue("user.pan_no", "error.user.pan_no", "PAN No is required");
		}
		else  
		{
			if (!panno.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
				errors.rejectValue("user.pan_no", "error.user.pan_no", "Enter valid PAN No");
			}
		}

		if(companypan.equals(""))
		{
			errors.rejectValue("company.pan_no", "error.company.pan_no", "PAN No is required");
		}
		
		else  
		{
			if (!companypan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
				errors.rejectValue("company.pan_no", "error.company.pan_no", "Enter valid PAN No of company");
			}
		}

	/*	if (adhaar != null && adhaar != "") {
			if (adhaar.length() != 12) {
				errors.rejectValue("user.adhaar_no", "error.user.adhaar_no", "Enter 12 digit Aadhaar No");

			} else if (!adhaar.matches("^[0-9]+\\.?[0-9]+$")) {
				errors.rejectValue("user.adhaar_no", "error.user.adhaar_no", "Enter valid Aadhaar No");
			}
		}
	*/	
		/*if(status == 0){
			errors.rejectValue("company.status", "error.company.status", "Select Status");
		}*/

		if(company.getGst_no().isEmpty()) { 
		}
		else { 
			if(gstno.length()!=15) {
		 errors.rejectValue("company.gst_no","error.company.gst_no",
		"Enter 15 digit GST No");
		
		 } else {
		  
		 if(!gstno.matches("[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}[0-9A-Z]{1}[0-9A-Z]{1}")
		  )		 
		  { errors.rejectValue("company.gst_no","error.company.gst_no","GST Identification Number is not valid. It should be in this '11AAAAA1111Z1A1' format"); } 
		 int gpan=gstno.indexOf(companypan); // prints "4"  
			if(gpan!=2)
			{
				errors.rejectValue("company.gst_no","error.company.gst_no","Third to twelth digit of Company GST no must be similar to Company PAN No ");
			}
			
		 } 
	   }		 
	}

}