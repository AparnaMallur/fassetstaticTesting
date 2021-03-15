/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.AccountGroup;
import com.fasset.entities.TaxMaster;
import com.fasset.service.interfaces.ITaxMasterService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class TaxMasterValidator extends MyValidator implements Validator{

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Autowired
	private ITaxMasterService ITaxMasterService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return TaxMaster.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		TaxMaster taxMaster = (TaxMaster)target;
		 String vat1 = taxMaster.getVat1();
		 String cst1 = taxMaster.getCst1();
		 String excise1 = taxMaster.getExcise1();
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tax_name","error.tax_name", "Tax Name is required");
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "vat1","error.vat1", " Vat is required");
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cst1","error.cst1", " Cst is required");
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "excise1","error.excise1", "Excise is required");
		 
		 if(taxMaster.getTax_id()==null)
			{
				if(taxMaster.getTax_name()!="")
				{
					TaxMaster taxMaster1 =ITaxMasterService.isExists(taxMaster.getTax_name());
					if(taxMaster1!=null)
					{
						errors.rejectValue("tax_name","error.tax_name", "Tax name is already exists.");
					}
				}
			}
		 
		 if(vat1 != null && vat1 != ""){
				if (!vat1.matches("^([+-]?\\d*\\.?\\d*)$")) {
					errors.rejectValue("vat1","error.vat1", "Enter valid Vat");
				}
			}		   
		 if(cst1 != null && cst1 != ""){
				if (!cst1.matches("^([+-]?\\d*\\.?\\d*)$")) {
					errors.rejectValue("cst1","error.cst1", "Enter valid Cst");
				}
			}		 
		 if(excise1 != null && excise1 != ""){
				if (!excise1.matches("^([+-]?\\d*\\.?\\d*)$")) {
					errors.rejectValue("excise1","error.excise1", "Enter valid Excise");
				}
			}
	}
	

}
