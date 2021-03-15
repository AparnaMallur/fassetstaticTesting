package com.fasset.utils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.groups.Default;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Errors;

public class MyOwnValidator {

	public static String execValidatorByClass(String prefix, Object o, Errors errors,
			ReloadableResourceBundleMessageSource messages, Class<?>... classes) {
		StringBuilder errorStr = new StringBuilder();
		String e = "";
		if (classes == null || classes.length == 0 || classes[0] == null) {
			classes = new Class<?>[] { Default.class };
		}
		javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(o, classes);
		for (ConstraintViolation<Object> v : violations) {
			Path path = v.getPropertyPath();
			String propertyName = "";
			if (path != null) {
				for (Node n : path) {
					propertyName += n.getName() + ".";
				}
				propertyName = propertyName.substring(0, propertyName.length() - 1);
			}
			String constraintName = v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
			e = v.getMessage();
			if (propertyName == null || "".equals(propertyName)) {
				errors.reject(constraintName, v.getMessage());
			} else {
				try {
					if ("Length".equals(constraintName)) {
						String str = "0 " + v.getMessage();
						str = str.replaceAll("[^-?0-9]+", " ");
						List<String> ar = Arrays.asList(str.trim().split(" "));
						if (ar.size() >= 2) {
							MessageFormat mf = new MessageFormat(messages.getMessage("Length", null, null));
							e = mf.format(new Object[] { ar.get(0), ar.get(2), ar.get(1) });
						}
						errors.rejectValue(prefix + propertyName, "", e);
					} else if ("NotBlank".equals(constraintName)) {
						e = messages.getMessage("NotBlank", null, null);
						errors.rejectValue(prefix + propertyName, "", e);
					} else {
						errors.rejectValue(prefix + propertyName, "", v.getMessage());
					}
				} catch (Exception e1) {
					errors.reject(propertyName, v.getMessage());
				}
			}
			if (errorStr.length() == 0) {
				errorStr.append(e);
			} else {
				errorStr.append(", ").append(e);
			}
		}
		return errorStr.toString();
	}

}
