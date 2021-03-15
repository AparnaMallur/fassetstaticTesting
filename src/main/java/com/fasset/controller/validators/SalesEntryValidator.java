/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.SalesEntry;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Component
public class SalesEntryValidator extends MyValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return SalesEntry.class.equals(clazz);
	}

	private static final String icon_PATTERN = "^0$";

	@Override
	public void validate(Object target, Errors errors) {
		SalesEntry entry = (SalesEntry) target;
		Long eId = entry.getCustomer_id();
		String voucherrange = entry.getVoucher_range();
		Integer entry_type = entry.getEntrytype();
		Long sid = entry.getSales_id();
		Integer sale_type = entry.getSale_type();
		Long bankName = entry.getBankId();
		Long subledgerId = entry.getSubledger_Id();
		/*
		 * ValidationUtils.rejectIfEmptyOrWhitespace(errors,
		 * "customer_bill_no","error.customer_bill_no", "Invoice number is required");
		 *//*
			 * ValidationUtils.rejectIfEmptyOrWhitespace(errors,
			 * "customer_bill_date","error.customer_bill_date",
			 * "Customer bill date is required");
			 */
		if (eId == 0) {
			errors.rejectValue("customer_id", "error.customer_id", "Select Customer Name");
		}
		
		if (sid == null) {
			if (voucherrange.matches(icon_PATTERN)) {
				errors.rejectValue("voucher_range", "error.voucher_range", "Select Voucher Range");
			}
		}

		if (subledgerId == null || subledgerId == 0) {
			errors.rejectValue("subledger_Id", "error.subledger_Id", "Select Income Type");
		}
		
		if (sale_type == 2) {
			if (bankName == 0) {
				errors.rejectValue("bankId", "error.bankId", "Bank Name cannot be blank");
			}
		}
		
		if (entry_type == 0) {
			errors.rejectValue("entrytype", "error.entrytype", "Select Entry Type");
		}

		if (entry_type == 2) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shipping_bill_no", "error.shipping_bill_no",
					"Shipping bill number is required");
			if (entry.getExport_type() == 0) {
				errors.rejectValue("export_type", "error.export_type", "Export type is required");
			}

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shipping_bill_date", "error.shipping_bill_date", "Shipping bill date is required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "port_code", "error.port_code", "Port code is required");
		}
	}

}
