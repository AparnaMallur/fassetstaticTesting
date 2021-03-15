package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.ManualJournalVoucher;

@Component
public class ManualJournalVoucherValidator  extends MyValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return ManualJournalVoucher.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		ManualJournalVoucher voucher =(ManualJournalVoucher)target;
		LocalDate date=voucher.getDate();
		
		
		if(voucher.getYear_id()!=null && voucher.getYear_id()==0)
		{
			errors.rejectValue("year_id","error.year_id", "Select Accounting Year");
		}
		if(date==null){
			errors.rejectValue("date","error.date", "Date cannot be blank");
		}
	}

}
