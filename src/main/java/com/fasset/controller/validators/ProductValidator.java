package com.fasset.controller.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.AccountGroup;
import com.fasset.entities.Product;
import com.fasset.service.interfaces.IProductService;

@Component
public class ProductValidator extends MyValidator implements Validator {
	
	@Autowired
	private IProductService productService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Product.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Product product = (Product) target;
		Integer tid = product.getType();
		Long uid = product.getUnit();
		Long tax_type= product.getTax_type();
		Long company_id=product.getCompany_id();

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "product_name", "error.product_name", "Product name is required");
		
		
		if(tid == 0) {
			errors.rejectValue("type","error.type", "Select product type");
		}
		if(uid == 0) {
			errors.rejectValue("unit","error.unit", "Select unit of measurement");
		}
		
		if(tax_type==0)
		{
			errors.rejectValue("tax_type","error.tax_type", "Select tax type");
		}
		
		if(product.getProduct_id()==null)
		{
			if(product.getProduct_name()!="")
			{
				int exist =productService.isExistsProduct(product.getProduct_name(),company_id);
				if(exist==1)
				{
					errors.rejectValue("product_name","error.product_name", "Product name is already exists");
				}
			}
		}
		
		if(tax_type==2)
		{
			if(product.getVat_id()==0)
			{
				errors.rejectValue("vat_id","error.vat_id", "Select tax");
			}
			
		}
		if(tax_type==1){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hsn_san_no", "error.hsn_san_no",
					"HSC/SAC number is required");
		}
		
		if(product.getCompany_id()!=null && product.getCompany_id()==0 )
		{
			errors.rejectValue("company_id","error.company_id", "Select Company Name");
		}
	}

}
