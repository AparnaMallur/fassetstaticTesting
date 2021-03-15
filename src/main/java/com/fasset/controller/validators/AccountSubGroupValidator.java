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
import com.fasset.entities.AccountSubGroup;
import com.fasset.service.interfaces.IAccountSubGroupService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class AccountSubGroupValidator extends MyValidator implements Validator{

	@Autowired
	private IAccountSubGroupService accountSubGroupService ;
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return AccountSubGroup.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		AccountSubGroup AccountSubGroup = (AccountSubGroup) target ;
		Long pId = AccountSubGroup.getGroup_Id();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subgroup_name","error.subgroup_name", "Sub Group name is required");
		
		if(AccountSubGroup.getSubgroup_Id()==null)
		{
			if(AccountSubGroup.getSubgroup_name()!="")
			{
				AccountSubGroup type1 =accountSubGroupService.isExists(AccountSubGroup.getSubgroup_name());
			if(type1!=null)
			{
				errors.rejectValue("subgroup_name","error.subgroup_name", " AccountSubGroup name is already exists");
			}
			}
		}
		
		if(pId == 0) {
			errors.rejectValue("group_Id","error.group_Id", "Select Group");
		}
	}

	
}
