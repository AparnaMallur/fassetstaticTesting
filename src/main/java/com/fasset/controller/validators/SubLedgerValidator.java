/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.SubLedger;
import com.fasset.service.interfaces.ISubLedgerService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class SubLedgerValidator extends MyValidator implements Validator{

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Autowired
	private ISubLedgerService subLedgerService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return SubLedger.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		SubLedger subLedger =(SubLedger)target;
		Long sId = subLedger.getLedger_id();
		Long company_id=subLedger.getCompany_id();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subledger_name","error.subledger_name", "Subledger name is required");
		/*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "credit_opening_balance1", "error.credit_opening_balance1", "Credit opening balance is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "debit_opening_balance1", "error.debit_opening_balance1", "Debit opening balance is required");
		*/ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ledger_id", "error.ledger_id", "Ledger cannot be blank!");

		if(subLedger.getSubledger_Id()==null)
		{
		if(subLedger.getSubledger_name()!="")
		{
			int exist =subLedgerService.isExistssubledger(subLedger.getSubledger_name(),company_id);
			if(exist==1)
			{
				errors.rejectValue("subledger_name","error.subledger_name", "Subledger name is already exists");
			}
		}
		}
		
		if(sId == 0) {
			errors.rejectValue("ledger_id","error.ledger_id", "Select Ledger");
		}
		if(subLedger.getCompany_id()!=null)
		{
			if(subLedger.getCompany_id()==0)
			{
				errors.rejectValue("company_id","error.company_id", "Select Company Name");
			}
		}
	}

	
}
