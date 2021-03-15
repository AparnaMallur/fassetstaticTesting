package com.fasset.controller.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.MenuMaster;

@Component
public class MenuMasterValidator extends MyValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return MenuMaster.class.equals(clazz);
	}
	private static final String icon_PATTERN = "^N/A$";
	@Override
	public void validate(Object target, Errors errors) {
		MenuMaster menuMaster=(MenuMaster) target;
		Long parent=menuMaster.getParent();
		String micon=menuMaster.getMenu_icon();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "menu_name","error.menu_name", "Menu  name is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "menu_url","error.menu_url", "Menu Url required");
		if(parent==0){
			//errors.rejectValue("menu_icon","error.menu_icon", "Menu Icon "+micon+"cannot be blank");
			if (menuMaster.getMenu_icon().matches(icon_PATTERN)) {
				errors.rejectValue("menu_icon","error.menu_icon", "Menu Icon cannot be blank");
			}			
		}
		
	}
}
