/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.PurchaseEntry;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class PurchaseEntryValidator extends MyValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return PurchaseEntry.class.equals(clazz);
		
	}
	//private static final String icon_PATTERN = "^0$";

	@Override
	public void validate(Object target, Errors errors) {
		PurchaseEntry entry = (PurchaseEntry)target;
		Long eId = entry.getSupplier_id();
		Integer entry_type = entry.getEntrytype();
		Long subledgerId = entry.getSubledger_Id();

        
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "supplier_bill_date","error.supplier_bill_date", "Supplier bill date is required");
		/*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "grn_date","error.grn_date", "GRN date is required");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "voucher_date","error.voucher_date", "Voucher date is required");*/
				
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "supplier_bill_no","error.supplier_bill_no", "Bill number is required");
		if(eId == 0) {
			errors.rejectValue("supplier_id","error.supplier_id", "Select Supplier Name");
		}		
		
		if(subledgerId == null || subledgerId == 0){
			errors.rejectValue("subledger_Id","error.subledger_Id", "Select Expense Type");
		}
		
		if(entry_type==0){
			errors.rejectValue("entrytype","error.entrytype", "Select Entry Type");
		}
		
		if(entry_type==2){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shipping_bill_no","error.shipping_bill_no", "Shipping bill number is required");
			if(entry.getExport_type()==0){
				errors.rejectValue("export_type","error.export_type", "Export type is required");
			}
		
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shipping_bill_date","error.shipping_bill_date", "Shipping bill date is required");
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "port_code","error.port_code", "Port code is required");
		}
		/*if(sid==null)
		{
			if (voucherrange.matches(icon_PATTERN)) {
				errors.rejectValue("voucher_range","error.voucher_range", "Select Voucher Range");
			}	
		}*/
	}

	
}
