/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.State;
import com.fasset.service.interfaces.IStateService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class StateValidator extends MyValidator implements Validator{

	
	
	
	
	@Autowired
	private IStateService stateService ;
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return State.class.equals(clazz);
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		State state = (State)target;
		Long pId = state.getCountry_id();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state_name","error.state_name", "State name is required");
		if(state.getState_id()==null)
		{
			if(state.getState_name()!="")
			{
				State type1 =stateService.isExists(state.getState_name());
				if(type1!=null)
				{
					errors.rejectValue("state_name","error.state_name", "State name is already exists");
				}
			}
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state_code","error.state_code", "State Code is required");
		if(state.getState_id()==null)
		{
			if(state.getState_code()!=null)
			{
				State user=stateService.loadStateCode(state.getState_code());
				if(user!=null)
				{
					errors.rejectValue("state_code","error.state_code", "State code is already exists");
				}
			}
		}
		if(pId == 0) 
		{
			errors.rejectValue("country_id","error.country_id", "Select Country");
		}
		
	}
	

	
	
}
