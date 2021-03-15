package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Payment;

@Component
public class PaymentValidator extends MyValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Payment.class.equals(clazz);
	}
	
	private static final String icon_PATTERN = "^0$";

	@Override
	public void validate(Object target, Errors errors) {
		Payment payment = (Payment) target;
		Long supplierId = payment.getSupplierId();
		Long subLedgerId = payment.getSubLedgerId();
		Long purchaseEntryId = payment.getPurchaseEntryId();
		//Integer group = payment.getGroup();
		Integer paymentType = payment.getPayment_type();
		Long bankName=payment.getBankId();
		String voucherrange=payment.getVoucher_range();
		Long sid=payment.getPayment_id();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateString","error.dateString", "Date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount","error.amount", "Amount is required");
		
		if(supplierId == 0){
			errors.rejectValue("supplierId", "error.supplierId", "Suplier cannot be blank");
		}
		else if(supplierId == -1){
			if(subLedgerId == 0){
				errors.rejectValue("subLedgerId", "error.subLedgerId", "Expense Type cannot be blank");
			}
		}
		else if(supplierId > 0){
			if(purchaseEntryId == null){
				errors.rejectValue("purchaseEntryId", "error.purchaseEntryId", "Supplier bill number cannot be blank");
			}
			else if(purchaseEntryId == 0){
				errors.rejectValue("purchaseEntryId", "error.purchaseEntryId", "Supplier bill number cannot be blank");
			}
			
		}		
		
		if(paymentType == 0){
			errors.rejectValue("payment_type", "error.payment_type", "Payment type cannot be blank" );
		}
		else if(paymentType == MyAbstractController.PAYMENT_TYPE_CHEQUE || paymentType == MyAbstractController.PAYMENT_TYPE_DD || paymentType == MyAbstractController.PAYMENT_TYPE_NEFT){
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cheque_dd_no","error.cheque_dd_no", "Cheque  number is required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "chequeDateString","error.chequeDateString", "Cheque date is required");
			if(bankName==0){
				errors.rejectValue("bankId","error.bankId", "Bank Name cannot be blank");
			}
		}
		if((payment.getSupplierId()!=-1) && (payment.getSupplierId()!=null))
		{
			if(payment.getPurchaseEntryId()==-1)
			{
					boolean tdspaid=payment.getTds_paid();
			
					if(tdspaid==true)
					{
							ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tds_amount","error.tds_amount", "TDS Amount is required");
					}
			}
		}
		/*if(sid==null)
		{
			if (voucherrange.matches(icon_PATTERN)) {
				errors.rejectValue("voucher_range","error.voucher_range", "Select Voucher Range");
			}	
		}*/
		
	}
}
