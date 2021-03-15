package com.fasset.pdf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClasstoConvertDateformat 
{
	
	public static String dateFormat(String date)
	{
		Date dates = null;		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			dates = (Date)formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
		String finalString = newFormat.format(dates);				
		return finalString;
	}	
	
	public static String dateFormatForGSTR(String date)
	{
		Date dates = null;		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			dates = (Date)formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String finalString = newFormat.format(dates);				
		return finalString;
	}	

}
