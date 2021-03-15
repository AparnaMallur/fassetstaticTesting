package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Quotation;
import com.fasset.entities.User;
import com.fasset.form.UserForm;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IUserService;

@Component
public class SignUpValidator implements Validator {
	
	@Autowired
	private IUserService userService;
	
	@Autowired	
	private IQuotationService quoteService;
	
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public boolean supports(Class<?> clazz) {
		return UserForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserForm signUpDetails = (UserForm) target;
		String email = signUpDetails.getCompany().getEmail_id();
		String mobile = signUpDetails.getCompany().getMobile().toString();
		Integer signupType = signUpDetails.getSignUpType();	
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.email_id","error.company.email_id", "Email ID is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.mobile","error.company.mobile", "Mobile number is required");
		
		
		if(signupType == MyAbstractController.STATUS_TRIAL_LOGIN){
			String firstName = signUpDetails.getUser().getFirst_name();
			String lastName = signUpDetails.getUser().getLast_name();
			Long industryType = signUpDetails.getIndustryTypeId();
			Long companyType = signUpDetails.getCompanyTypeId();
			Long accYearId=signUpDetails.getCompany().getYearRangeId();
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.first_name","error.user.first_name", "First name is required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.last_name","error.user.last_name", "Last name is required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company.company_name","error.company.company_name", "Company name is required");
			
			if(firstName != null && firstName != ""){
				if(!firstName.matches("^[\\p{L} .'-]+$")){
					errors.rejectValue("user.first_name","error.user.first_name", "Enter only character");
				}
			}
			
			if(lastName != null && lastName != ""){
				if(!lastName.matches("^[\\p{L} .'-]+$")){
					errors.rejectValue("user.last_name","error.user.last_name", "Enter only character");
				}
			}
			
			if(industryType == 0 || industryType == null){
				errors.rejectValue("industryTypeId","error.industryTypeId", "Please select industry type");
			}
			
			if(companyType == 0 || companyType == null){
				errors.rejectValue("companyTypeId","error.companyTypeId", "Please select company type");
			}
			if(accYearId == 0 || accYearId == null){
				errors.rejectValue("company.YearRangeId","error.company.YearRangeId", "Please select Account Year");
			}
		}
		else{
			String qoutNo = signUpDetails.getQuotationNo();
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "quotationNo","error.quotationNo", "Quotation number is required");
			if(qoutNo != null && qoutNo != ""){
				Quotation quot = quoteService.getQuotation(email, qoutNo);
				if(quot == null){
					errors.rejectValue("quotationNo","error.quotationNo", "You entered wrong quotation number");
				}
			}
		}
		

		if(signUpDetails.getCompanyId() == null || signUpDetails.getCompanyId() == 0) {
			if(email != null && email != ""){
				if(!email.matches(EMAIL_PATTERN)){
					errors.rejectValue("company.email_id","error.company.email_id", "Enter valid mail id");
				}
				else{
					User user = userService.loadUserByUsername(email,"");
					if(user != null){
						if(user.getRole().getRole_id() != MyAbstractController.ROLE_TRIAL_USER || (user.getRole().getRole_id() == MyAbstractController.ROLE_TRIAL_USER && signUpDetails.getSignUpType() != MyAbstractController.STATUS_TRIAL_LOGIN)) {
							errors.rejectValue("company.email_id","error.company.email_id", "Email id already exists.");
						}
					}				
				}
			}
			
			if(mobile != null && mobile != ""){
				/*User checkMobileNo = userService.isExists(signUpDetails.getCompany().getMobile());
				if(checkMobileNo!=null)
				{
					errors.rejectValue("company.mobile","error.company.mobile", "Mobile number is already exists");
				}*/
				
				if(!mobile.matches("\\d{10}")){
					errors.rejectValue("company.mobile","error.company.mobile", "Enter valid mobile number");
				}
			}			
		}
	}

}
