package com.fasset.controller.validators;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Bank;
import com.fasset.service.interfaces.IBankService;

/**
 * @author "Vishwajeet"
 *
 */
@Component
public class BankValidator implements Validator{
	
	@Autowired
	private IBankService bankService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Bank.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Bank bank = (Bank) target;
		String bankName = bank.getBank_name();
		Long subGroupId = bank.getSubGroupId();
		String ifsc = bank.getIfsc_no();
		Long accountNo = bank.getAccount_no();
		List<Bank> bankList2 = bankService.findAllListing2(true, bank.getCompany_id());
		
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "branch","error.branch", "Branch name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "account_no","error.account_no", "Account number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ifsc_no","error.ifsc_no", "IFSC code is required");
		
		if(bankName.equalsIgnoreCase("NONE")){
			errors.rejectValue("bank_name", "error.bank_name", "Select bank name");
		}
		if(bankName.equalsIgnoreCase("OTH")){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "other_bank_name","error.other_bank_name", "Bank name can not be blank!");
		}
		
		if(subGroupId == 0){
			errors.rejectValue("subGroupId", "error.subGroupId", "Select account sub group");
		}
		if(ifsc != null && ifsc != ""){
			if(!ifsc.matches("^[A-Za-z]{4}\\d{7}$")){
				errors.rejectValue("ifsc_no", "error.ifsc_no", "Enter valid IFSC code");
			}
		}
		
		
		
		if(bank.getBank_id() == null || bank.getBank_id() == 0){
			if(accountNo != null){
				for(Bank bankObj : bankList2){
					if(bankObj.getAccount_no().equals(accountNo)){
						if(bankObj.getBank_approval() != MyAbstractController.APPROVAL_STATUS_REJECT) {
							if(bankObj.getBank_approval() == MyAbstractController.APPROVAL_STATUS_PENDING || bankObj.getBank_approval() == MyAbstractController.APPROVAL_STATUS_PRIMARY) {
								errors.rejectValue("account_no", "error.account_no", "Account number is already exists and pending for approval");							
								break;
							}
							else if(bankObj.getBank_approval() == MyAbstractController.APPROVAL_STATUS_SECONDARY) {
								errors.rejectValue("account_no", "error.account_no", "Account number is already exists");							
								break;
							}
						}
						break;
					}
				}
			}
		}
		
		if(bank.getCompany_id()!=null && bank.getCompany_id()==0 ){
			errors.rejectValue("company_id","error.company_id", "Select Company Name");
		}
	}

}