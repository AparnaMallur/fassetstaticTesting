/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.AccountGroup;
import com.fasset.service.interfaces.IAccountGroupService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class AccountGroupValidator extends MyValidator implements Validator{

	@Autowired
	private IAccountGroupService  accGpService ;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AccountGroup.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AccountGroup accountGroup = (AccountGroup) target ;
		Long gId = accountGroup.getAccount_group_id();
		Long pId = accountGroup.getPosting_id();
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "group_name","error.group_name", "Group name is required");
		
		if(accountGroup.getGroup_Id()==null)
		{
			if(accountGroup.getGroup_name()!=""){
				AccountGroup type1 =accGpService.isExists(accountGroup.getGroup_name());
				if(type1 != null){
					errors.rejectValue("group_name","error.group_name", "Group name already exists");
				}
			}
		}
		if(gId == 0) {
			errors.rejectValue("account_group_id","error.account_group_id", "Select Group Type");
		}
		
		if(pId == 0) {
			errors.rejectValue("posting_id","error.posting_id", "Select Posting");
		}
	}

	
}
