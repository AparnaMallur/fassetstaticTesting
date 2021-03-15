package com.fasset.report.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClassToConvertDateForExcell {

	
	
	public static String dateConvert(String str)
	{
		Date  dates = new Date();
		DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        try {
          dates = (Date)formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        String finalString = newFormat.format(dates);
        
        return finalString;
	}
	public static String dateConvertNew(String str){
		Date date = null;
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
	    String dateString = null;
	    try {
	        // convert to Date Format From "dd/mm/yy" to Date
	        date = formatter.parse(str);
	        System.out.println("firnat date "+ date);
	        // from the Converted date to the required format eg : "yyyy-MM-dd"
	        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
	        dateString = formatter1.format(date);

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return dateString;
		
	}
}
