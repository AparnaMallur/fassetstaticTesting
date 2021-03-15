package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Receipt;

@Component
public class ReceiptValidator extends MyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		
		return Receipt.class.equals(clazz);
	}
	
	private static final String icon_PATTERN = "^0$";

	@SuppressWarnings("null")
	@Override
	public void validate(Object target, Errors errors) {
		Receipt receipt = (Receipt) target;
		LocalDate date=receipt.getDate();
		Long customer_id=receipt.getCustomer_id();
		Integer gst_applied=receipt.getGst_applied();
		Long subledger_id = receipt.getSubledgerId();
		Integer payment_type= receipt.getPayment_type();
		Long bankName=receipt.getBankId();
		
		
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount","error.amount", "Please enter amount");
		if(date==null){
			errors.rejectValue("date","error.date", "Date cannot be blank");
		}	
		
		if (customer_id == -1) {
			if (subledger_id == 0) {
				errors.rejectValue("subledgerId", "error.subledgerId", "Expense Type cannot be blank");
			}
		}
		else{
			if (customer_id == 0) {
				errors.rejectValue("customer_id", "error.customer_id", "Select customer name from customer list");
			}
			else if(receipt.getSalesEntryId()==null)
			{
				errors.rejectValue("salesEntryId", "error.salesEntryId", "Select bill number from list");
			}
			else if(receipt.getSalesEntryId()==0)
			{
				errors.rejectValue("salesEntryId", "error.salesEntryId", "Select bill number from list");
			}
		}
		
		
		if(gst_applied==0){
			errors.rejectValue("gst_applied","error.gst_applied", "GST applied cannot be blank");
		}
		
		if(payment_type==0){
			errors.rejectValue("payment_type","error.payment_type", "Select Receipt type");
		}
		/*
		 * if(bankName==0){ errors.rejectValue("bankId","error.bankId",
		 * "Select Bank Name");
		 * 
		 * }
		 */
		else if(payment_type == MyAbstractController.PAYMENT_TYPE_CHEQUE || payment_type == MyAbstractController.PAYMENT_TYPE_DD || payment_type == MyAbstractController.PAYMENT_TYPE_NEFT){
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cheque_no","error.cheque_no", "Enter Cheque / DD  number");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cheque_date","error.cheque_date", "Enter Cheque / DD date");
			
			/*
			 * if(bankName==0){ errors.rejectValue("bankId","error.bankId",
			 * "Select Bank Name");
			 * 
			 * }
			 */		
			}
		
		if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment()==true)
		{
			
			if(receipt.getTds_paid()==true)
			{
				
				if(receipt.getTds_amount()==null || receipt.getTds_amount()==0)
				{
					errors.rejectValue("tds_amount","error.tds_amount", "TDS Amount can not be blank or zero.");
				}
				
			}
		
		}
		
	}

	
	
	
}
