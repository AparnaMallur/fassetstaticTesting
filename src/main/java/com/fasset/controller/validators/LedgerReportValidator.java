package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.form.LedgerReportForm;
import com.fasset.form.SalesReportForm;

@Component
public class LedgerReportValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return LedgerReportForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LedgerReportForm ledgerReportForm = (LedgerReportForm) target;
		
		LocalDate from_date = ledgerReportForm.getFromDate();
		LocalDate to_date = ledgerReportForm.getToDate();
		
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fromDate","error.fromDate", "From date is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "toDate","error.toDate", "To date is required");
		
		if(from_date != null && to_date != null)
		{
			if(from_date.compareTo(to_date) > 0){
				errors.rejectValue("fromDate","error.fromDate", "From date should be less than to date");
			}
		}
		
		/*if(ledid == 0) {
			errors.rejectValue("ledgerId","error.ledgerId", "Select ledger name");
		}*/
		
		if(ledgerReportForm.getCompanyId()!=null && ledgerReportForm.getCompanyId()==0)
		{
			errors.rejectValue("companyId","error.companyId", "Select company name");
			
		}
		
		if(ledgerReportForm.getReportAgainst()!=null && ledgerReportForm.getReportAgainst()==0)
		{
			errors.rejectValue("reportAgainst","error.reportAgainst", "Select report against");
			
		}
		if(ledgerReportForm.getReportAgainst()!=null && ledgerReportForm.getReportAgainst()>0)
		{
			if(ledgerReportForm.getReportAgainst()!=null && ledgerReportForm.getReportAgainst()==1)
			{
				
				if(ledgerReportForm.getCustomerId()==null)
				{
					
					errors.rejectValue("customerId","error.customerId", "Select customer");
				}
			}
			if(ledgerReportForm.getReportAgainst()!=null && ledgerReportForm.getReportAgainst()==2)
			{
				
				if(ledgerReportForm.getSupplierId()==null)
				{
					
					errors.rejectValue("supplierId","error.supplierId", "Select supplier");
				}
				
			}
			if(ledgerReportForm.getReportAgainst()!=null && ledgerReportForm.getReportAgainst()==3)
			{
				if(ledgerReportForm.getLedgerId()==null)
				{
					
					errors.rejectValue("ledgerId","error.ledgerId", "Select ledger");
				}
				if(ledgerReportForm.getSubledgerId()==null)
				{
					
					errors.rejectValue("subledgerId","error.subledgerId", "Select subledger");
				}
				
			}
			if(ledgerReportForm.getReportAgainst()!=null && ledgerReportForm.getReportAgainst()==4)
			{
				if(ledgerReportForm.getBankId()==null)
				{
					
					errors.rejectValue("bankId","error.bankId", "Select bank");
				}
				
			}
		
			
		}
		
	}

}
