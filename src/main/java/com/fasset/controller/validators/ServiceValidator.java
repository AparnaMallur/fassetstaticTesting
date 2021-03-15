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
import com.fasset.entities.Service;
import com.fasset.service.interfaces.IFrequencyService;
import com.fasset.service.interfaces.IServiceMaster;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class ServiceValidator extends MyValidator implements Validator{

	@Autowired
	private IServiceMaster serviceMaster ;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Service.class.equals(clazz);
	}

	
	@Override
	public void validate(Object target, Errors errors) {
		Service service = (Service)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "service_name","error.service_name", "Service name is required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "service_charge","error.service_charge", "Service charge is required");
			
		if(service.getId()==null)
		{
			if(service.getService_name()!="")
			{
				Service type1 =serviceMaster.isExists(service.getService_name());
			if(type1!=null)
			{
				errors.rejectValue("service_name","error.service_name", "Service name is already exists");
			}
			}
		}
		
	}

	
}
