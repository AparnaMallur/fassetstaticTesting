/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.Country;
import com.fasset.service.interfaces.ICountryService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class CountryValidator extends MyValidator implements Validator{

	@Autowired
	private ICountryService countryService ;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Country.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Country country =(Country)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "country_name","error.country_name", "Country name is required");
		
		if(country.getCountry_id()==null)
		{
		if(country.getCountry_name()!="")
		{
			Country type1 =countryService.isExists(country.getCountry_name());
		if(type1!=null)
		{
			errors.rejectValue("country_name","error.country_name", "Country name  is already exists");
		}
		}
		}
	}
}
