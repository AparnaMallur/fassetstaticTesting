package com.fasset.controller.validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.Suppliers;
import com.fasset.service.interfaces.ISuplliersService;
@Component
public class SupplierValidator extends MyValidator implements Validator {

	@Autowired
	private ISuplliersService supplierService;
	
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Suppliers.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Suppliers supplier = (Suppliers) target ;		
		Long cId = supplier.getCompany_statutory_id();
		Long iId = supplier.getIndustry_id();
		Long company_id=supplier.getCompany_id();
		Long coId = supplier.getCountry_id();
		Long sId = supplier.getState_id();
		Long ciId = supplier.getCity_id();
		Long supplier_id=supplier.getSupplier_id();
		String companypan = supplier.getCompany_pan_no();
		String ownerpan = supplier.getOwner_pan_no();
		String email=supplier.getEmail_id();
		String gstno = supplier.getGst_no();
		String Companyname=supplier.getCompany_name();
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contact_name","error.contact_name", "Contact name is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobile","error.mobile", "Mobile number is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email_id","error.email_id", "Email ID is required");
/*		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "owner_pan_no","error.owner_pan_no", "Owner PAN number is required");
*/		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "adhaar_no","error.adhaar_no", "Adhaar number is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "current_address","error.current_address", "Current address is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "permenant_address","error.permenant_address", "Permenant address is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tan_no","error.tan_no", "TAN number is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company_pan_no","error.company_pan_no", " Company PAN number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company_name","error.company_name", " Company name is required");
		
		if(supplier.getTds_applicable()==1)
		{
			if(supplier.getDeductee_id() == 0) {
				errors.rejectValue("deductee_id","error.deductee_id", "Select TDS Type");
			}
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tds_rate", "error.tds_rate", "TDS Rate cannot be blank");
	    }		
		if(supplier.getAdhaar_no() != null && supplier.getAdhaar_no() != "" && supplier.getAdhaar_no().length()<12)
		{
			errors.rejectValue("adhaar_no","error.adhaar_no", "Enter valid Aadhar no");
		}
		
		if(supplier.getOwner_pan_no() != null && supplier.getOwner_pan_no() != "" && supplier.getOwner_pan_no().length()<10)
		{
			errors.rejectValue("owner_pan_no","error.owner_pan_no", "Enter valid PAN no");
		}
		
		if(supplier.getCompany_pan_no() != null && supplier.getCompany_pan_no() != ""&&supplier.getCompany_pan_no().length()<10)
		{
			errors.rejectValue("company_pan_no","error.company_pan_no", "Enter valid PAN no");
		}
		
		if(supplier.getGst_applicable()==true)
		{
	//	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gst_no","error.gst_no", "GST number is required");
		if(gstno!=""&& gstno!=null) { if(gstno.length()!=15) {
			 errors.rejectValue("gst_no","error.gst_no", "Enter 15 digit GST No");			
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
		
		
		}
		
		//if(cId == 0) {
		//	errors.rejectValue("company_statutory_id","error.company_statutory_id", "Select Company Statutory Type");
		//}
		/*if(iId == 0) {
			errors.rejectValue("industry_id","error.industry_id", "Select Industry type");
		}*/
		//if(coId == 0) {
		//	errors.rejectValue("country_id","error.country_id", "Select Country Name");
		//}
		//if(supplier.getState_id() == null)
		//{
		//	errors.rejectValue("state_id","error.state_id", "Select State Name");
		//}
		//else if(sId == 0) {
		//	errors.rejectValue("state_id","error.state_id", "Select State Name");
		//}
		
		//if(supplier.getCity_id() == null) {
			//errors.rejectValue("city_id","error.city_id", "Select City Name");
		//}
		//else if(ciId == 0)
		//{
		//	errors.rejectValue("city_id","error.city_id", "Select City Name");
		//}
		if(supplier.getEmail_id() != null && supplier.getEmail_id() != ""){
			if(!supplier.getEmail_id().matches(EMAIL_PATTERN)){
				errors.rejectValue("email_id","email_id", "Enter valid mail id");
			}			
	    }
		if(supplier.getCompany_id()!=null && supplier.getCompany_id()==0 )
		{
			errors.rejectValue("company_id","error.company_id", "Select Company Name");
		}
		if(supplier_id==null  && supplier.getCompany_pan_no() != null && supplier.getCompany_pan_no() != "" && supplier.getCompany_name() != null && supplier.getCompany_name() != "" && supplier.getEmail_id() != "" && supplier.getEmail_id() != null)
		{
			int exist=supplierService.isExistsPan(companypan,Companyname,company_id,email);
			if(exist==1)
			{
				errors.rejectValue("company_pan_no","error.company_pan_no", "Supplier Is Already Exists Against This PAN Number");

			}
		}
		if(supplier_id==null && supplier.getCompany_pan_no()!=null && supplier.getCompany_name()!=null && company_id!=null && supplier.getContact_name()!=null) 
		{
			Suppliers sr = supplierService.isExistsSupplier(supplier.getCompany_pan_no(),
					supplier.getCompany_name(), company_id, supplier.getContact_name());
			
			if(sr!=null)
			{
				errors.rejectValue("company_name","error.company_name", "Supplier Already Exists With This Comapny Name And Contact Name. Please Use Different Names.");
			}
			
		}
	}
}