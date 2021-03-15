/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.Ledger;
import com.fasset.service.interfaces.ILedgerService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class LedgerValidator extends MyValidator implements Validator{

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Autowired
	private ILedgerService ledgerService ;
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return Ledger.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		Ledger ledger = (Ledger)target;
		Long sId = ledger.getSubgroup_Id();
		Long company_id=ledger.getCompany_id();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ledger_name","error.ledger_name", "Ledger name is required");
	/*	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "credit_opening_balance1", "error.credit_opening_balance1", "Credit opening balance is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "debit_opening_balance1", "error.debit_opening_balance1", "Debit opening balance is required");
	*/	
		if(ledger.getLedger_id()==null)
		{
		if(ledger.getLedger_name()!="")
		{
			int exist =ledgerService.isExistsledger(ledger.getLedger_name(),company_id);
		if(exist==1)
		{
			errors.rejectValue("ledger_name","error.ledger_name", "Ledger name is already exists");
		}
		}
		}
		if(sId == 0) {
			errors.rejectValue("subgroup_Id","error.subgroup_Id", "Select Sub Group");
		}
		if(ledger.getCompany_id()!=null && ledger.getCompany_id()==0 )
		{
			errors.rejectValue("company_id","error.company_id", "Select Company Name");
		}
		
		/*if (ledger.getCredit_opening_balance1() != null && ledger.getCredit_opening_balance1() != "") {
			if (!ledger.getCredit_opening_balance1().matches("^([+-]?\\d*\\.?\\d*)$")) {
				errors.rejectValue("credit_opening_balance1", "error.credit_opening_balance1", "Enter valid credit opening balance");
			}
		}
		
		if (ledger.getDebit_opening_balance1() != null && ledger.getDebit_opening_balance1() != "") {
			if (!ledger.getDebit_opening_balance1().matches("^([+-]?\\d*\\.?\\d*)$")) {
				errors.rejectValue("debit_opening_balance1", "error.debit_opening_balance1", "Enter valid debit opening balance");
			}
		}*/
		
	}

	
}
