/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.City;
import com.fasset.service.interfaces.ICityService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class CityValidator extends MyValidator implements Validator{

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Autowired
	private ICityService cityService ;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return City.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		City city =(City)target;
		Long cId = city.getCountry_id();
		Long sId = city.getState_id();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city_name","error.city_name", "City name is required");
		if(city.getCity_id()==null)
		{
		if(city.getCity_name()!="")
		{
			City type1 =cityService.isExists(city.getCity_name());
		if(type1!=null)
		{
			errors.rejectValue("city_name","error.city_name", "City name is already exists");
		}
		}
		}
		if(cId == 0) {
			errors.rejectValue("country_id","error.country_id", "Select Country");
		}
		if(sId == 0) {
			errors.rejectValue("state_id","error.state_id", "Select State");
		}
	}

	
}
