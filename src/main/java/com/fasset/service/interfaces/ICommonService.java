package com.fasset.service.interfaces;

import org.joda.time.LocalDate;

public interface ICommonService {
	String getVoucherNumber(String range, Integer type, LocalDate date, Long yearId, Long companyId);
	String convertNumberToWord(Double num);
	String convertFloatNumberToWord(Float num);
	String convertDoubleNumberToWord(Double num);
	String round(Double debit_balance, int decimalPlace);
	Float round(float d, int decimalPlace);
	LocalDate getApril1StDate(LocalDate date);
	 String getVoucherNumberForAUtoJV(LocalDate date, Long companyId) ;
		
	
}
