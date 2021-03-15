/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.Customer;
import com.fasset.service.interfaces.ICustomerService;
/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Component
public class CustomerValidator extends MyValidator implements Validator {

	@Autowired
	private ICustomerService customerService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public boolean supports(Class<?> clazz) {
		return Customer.class.equals(clazz);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {

		Customer customer = (Customer) target;
		Long cId = customer.getCompany_statutory_id();
		Long iId = customer.getIndustry_id();
		Long coId = customer.getCountry_id();
		Long sId = customer.getState_id();
		Long ciId = customer.getCity_id();
		String companypan = customer.getCompany_pan_no();
		String gstno = customer.getGst_no();
		Long customer_id=customer.getCustomer_id();
		Long company_id=customer.getCompany_id();
		String ownerpan=customer.getOwner_pan_no();
		String Companyname=customer.getFirm_name();
		String email=customer.getEmail_id();
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contact_name", "error.contact_name", "Contact name is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobile", "error.mobile", "Mobile number is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "landline_no", "error.landline_no", "Landline number is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email_id", "error.email_id", "Email ID is required");
/*		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "owner_pan_no", "error.owner_pan_no", "Owner PAN number is required");
*/		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "adhaar_no", "error.adhaar_no", "Adhaar number is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "current_address", "error.current_address", "Current address is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "permenant_address", "error.permenant_address", "Permenant address is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tan_no", "error.tan_no", "TAN number is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company_pan_no", "error.company_pan_no", "Company PAN is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firm_name", "error.firm_name", "Firm name is required");
		/*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "credit_opening_balance1", "error.credit_opening_balance1", "Credit balance is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "debit_opening_balance1", "error.debit_opening_balance1", "Debit balance is required");
*/
		if (customer.getAdhaar_no() != null && customer.getAdhaar_no() != "" && customer.getAdhaar_no().length() < 12) {
			errors.rejectValue("adhaar_no", "error.adhaar_no", "Enter valid Aadhar no");
		}

		if (customer.getOwner_pan_no() != null && customer.getOwner_pan_no() != ""
				&& customer.getOwner_pan_no().length() < 10) {
			errors.rejectValue("owner_pan_no", "error.owner_pan_no", "Enter valid PAN no");
		}

		if (customer.getCompany_pan_no() != null && customer.getCompany_pan_no() != ""
				&& customer.getCompany_pan_no().length() < 10) {
			errors.rejectValue("company_pan_no", "error.company_pan_no", "Enter valid PAN no");
		}

		/*if (customer.getCredit_opening_balance1() != null && customer.getCredit_opening_balance1() != "") {
			if (!customer.getCredit_opening_balance1().matches("^([+-]?\\d*\\.?\\d*)$")) {
				errors.rejectValue("credit_opening_balance1", "error.credit_opening_balance1", "Enter valid Credit balance");
			}
		}
		if (customer.getDebit_opening_balance1() != null && customer.getDebit_opening_balance1() != "") {
			if (!customer.getDebit_opening_balance1().matches("^([+-]?\\d*\\.?\\d*)$")) {
				errors.rejectValue("debit_opening_balance1", "error.debit_opening_balance1", "Enter valid Debit balance");
			}
		}*/
		if (customer.getTds_applicable() == 1) {
			if(customer.getDeductee_id() == 0) {
				errors.rejectValue("deductee_id","error.deductee_id", "Select TDS Type");
			}
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tds_rate1", "error.tds_rate1", " Tds rate is required");
			if (customer.getTds_rate1() != null && customer.getTds_rate1() != "") {
				if((!customer.getTds_rate1().matches("^([+-]?\\d*\\.?\\d*)$"))){					
					errors.rejectValue("tds_rate1", "error.tds_rate1", "Enter valid tds rate");
				}
				else if(Float.parseFloat((customer.getTds_rate1())) >= 100){
					errors.rejectValue("tds_rate1", "error.tds_rate1", "Enter valid tds rate");
				}
			}

		}
		if (customer.getGst_applicable() == true) {
			if(gstno!=""&& gstno!=null) { if(gstno.length()!=15) {
				 errors.rejectValue("gst_no","error.gst_no",
				"Enter 15 digit GST No");				
				 } else {				  
				 if(!gstno.matches("[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}[0-9A-Z]{1}[0-9A-Z]{1}")
				  )		 
				  { errors.rejectValue("gst_no","error.gst_no","GST Identification Number is not valid. It should be in this '11AAAAA1111Z1A1' format"); } 
				 int gpan=gstno.indexOf(companypan); // prints "4"  
					if(gpan!=2)
					{
						errors.rejectValue("gst_no","error.gst_no","Third to twelth digit of Company GST no must be similar to Company PAN No ");
					}
					
				 }
			}			
	//	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gst_no", "error.gst_no", "GST number is required");
		}
		//if (cId == 0) {
		//	errors.rejectValue("company_statutory_id", "error.company_statutory_id", "Select Company Statutory Type");
		//}
		/*if (iId == 0) {
			errors.rejectValue("industry_id", "error.industry_id", "Select Industry type");
		}*/
		//if (coId == 0) {
		//	errors.rejectValue("country_id", "error.country_id", "Select Country Name");
		//}
		//if(customer.getState_id() == null)
		//{
		//	errors.rejectValue("state_id", "error.state_id", "Select State Name");
		//}
		//else if (sId == 0) {
		//	errors.rejectValue("state_id", "error.state_id", "Select State Name");
		//}
		//if(customer.getCity_id() == null)
		//{
		//	errors.rejectValue("city_id", "error.city_id", "Select City Name");
		//}
		//else if (ciId == 0) {
		//	errors.rejectValue("city_id", "error.city_id", "Select City Name");
		//}

		if (customer.getEmail_id() != null && customer.getEmail_id() != "") {
			if (!customer.getEmail_id().matches(EMAIL_PATTERN)) {
				errors.rejectValue("email_id", "email_id", "Enter valid mail id");
			}
			
			if(customer.getCompany_id()!=null && customer.getCompany_id()==0 )
			{
				errors.rejectValue("company_id","error.company_id", "Select Company Name");
			}

		}
		/*if(customer_id==null  && customer.getCompany_pan_no() != null && customer.getCompany_pan_no() != "" && customer.getFirm_name() != null && customer.getFirm_name() != "" && customer.getEmail_id() != "")
		{
			int exist=customerService.isExistsPan(companypan,Companyname,company_id, email);
			
			if(exist==1)
			{
				errors.rejectValue("company_pan_no","error.company_pan_no", "Customer Is Already Exists Against This PAN Number");
			}
		}*/ //Multiple customer can have same company pan no.
		
		if(customer_id==null && customer.getFirm_name()!=null && customer.getContact_name()!=null && customer.getCompany_pan_no()!=null && company_id!=null) 
		{
			Customer cr = customerService.isExistsCustomer(customer.getCompany_pan_no(),
					customer.getFirm_name(), company_id, customer.getContact_name());
			if(cr!=null)
			{
				errors.rejectValue("firm_name","error.firm_name", "Customer Already Exists With This Comapny Name And Contact Name. Please Use Different Names.");
			}
		}

	}

}
