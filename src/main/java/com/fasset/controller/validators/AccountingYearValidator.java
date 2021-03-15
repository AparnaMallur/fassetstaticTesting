/**
 * mayur suramwar
 */
package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.AccountingYear;
import com.fasset.entities.Country;
import com.fasset.service.interfaces.IAccountingYearService;
/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Component
public class AccountingYearValidator extends MyValidator implements Validator{

	@Autowired
	private IAccountingYearService yearService;
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return AccountingYear.class.equals(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		AccountingYear AccountingYear=(AccountingYear) target;
		LocalDate startdate=AccountingYear.getStart_date();
		LocalDate enddate=AccountingYear.getEnd_date();
		Boolean status=AccountingYear.getStatus();	
		Long year_id=AccountingYear.getYear_id();
		String yearrange=AccountingYear.getYear_range();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "year_range","error.year_range", "Year range is required");
		Long yid=(long) 0;
		AccountingYear YearExist=yearService.isExists(yearrange);
		if(AccountingYear.getYear_id()==null)
		{
		if(YearExist!=null)
		{
			errors.rejectValue("year_range","error.year_range", "Year Range Already Exists");
		}
		}
/*		if(status==true)
		{
			if(year_id!=null)
			{
				int count=yearService.findactiveyear(year_id);
					if(count!=0)
					{
						errors.rejectValue("status","error.status", "Only one Financial Year can be active at a time!");
					}			
			}
			else
			{
				int count=yearService.findactiveyear(yid);
				if(count!=0)
				{
					errors.rejectValue("status","error.status", "Only one Financial Year can be active at a time!");
				}		
			}
		}
		
		else if(status==true)
		{
			
		}
*/
		if(startdate==null){
			errors.rejectValue("start_date","error.start_date", "Start Date cannot be blank");
		}
		if(enddate==null){
			errors.rejectValue("end_date","error.end_date", "End Date cannot be blank");
		}

	}

	
}
