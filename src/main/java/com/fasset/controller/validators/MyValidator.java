package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Errors;

import com.fasset.utils.StringUtil;
import com.google.common.base.Strings;

public class MyValidator {

	private ReloadableResourceBundleMessageSource mess;

	public MyValidator() {
		mess = null;
	}

	public MyValidator(ReloadableResourceBundleMessageSource mess) {
		setMess(mess);
	}

	protected ReloadableResourceBundleMessageSource getMess() {
		return mess;
	}

	public String getMess(String s) {
		try {
			return getMess().getMessage(s, null, null);
		} catch (Exception e) {

		}
		return s;
	}

	public void setMess(ReloadableResourceBundleMessageSource mess) {
		this.mess = mess;
	}

	protected LocalDate convertStr2LD(String date) {
		String pattern = getMess().getMessage("pattern.date", null, null);
		if ("pattern.date".equals(pattern)) {
			pattern = "";
		}
		return StringUtil.convertStr2LD(date, pattern);
	}

	/**
	 * This method check only Future date & Current date not accept.
	 * @param date
	 * @return
	 */
	protected boolean correctDOB(String date)
	{
		boolean flag=false;
		String pattern = null;
		LocalDate dtdob = null;
		LocalDate dtToday = null;
		
		if(StringUtil.isEmptyOrNullValue(date) == false)
		{
			pattern = getMess().getMessage("pattern.date", null, null);
			dtdob=StringUtil.convertStr2LD(date, pattern);
			if(dtdob != null)
			{
				dtToday= LocalDate.now();
				if(dtdob.toDate().compareTo(dtToday.toDate())>=0)
					flag=true;
			}
		}
		
		return flag;
	}
	/**
	 * This method check only Future date not accept but Current date will accept.
	 * @param date
	 * @return
	 */
	protected boolean checkFutureDate(String date)
	{
		boolean flag=false;
		String pattern = null;
		LocalDate dtdob = null;
		LocalDate dtToday = null;
		
		if(StringUtil.isEmptyOrNullValue(date) == false)
		{
			pattern = getMess().getMessage("pattern.date", null, null);
			dtdob=StringUtil.convertStr2LD(date, pattern);
			if(dtdob != null)
			{
				dtToday= LocalDate.now();
				if(dtdob.toDate().compareTo(dtToday.toDate())> 0)
					flag=true;
			}
		}
		
		return flag;
	}
	
	protected String checkAlien(String alie, String alieID, Errors errors) {
		if (!StringUtil.isEmptyOrNullValue(alie)) {
			alie = alie.trim();
			try {
				if (alie.length() < 7) {
					errors.rejectValue(alieID, "error.alie.num_s9");
				} else if (alie.length() > 9) {
					errors.rejectValue(alieID, "error.alie.num_s9");
				} else {
					String newv = Strings.padStart(alie, 9, '0');
					Long l = Long.valueOf(newv);
					if (l == 0) {
						errors.rejectValue(alieID, "error.alie.num");
					} else {
						alie = newv;
					}
				}
			} catch (Exception e) {
				errors.rejectValue(alieID, "error.alie.num");
			}
		}
		return (alie);
	}

	protected String checkAlienForm(String alie, String alieID, Errors errors) {
		if (!StringUtil.isEmptyOrNullValue(alie)) {
			alie = alie.trim();
			try {
				if (alie.length() < 7) {
					errors.reject(alieID, getMess("error.alie.num_s9"));
				} else if (alie.length() > 9) {
					errors.reject(alieID, getMess("error.alie.num_s9"));
				} else {
					String newv = Strings.padStart(alie, 9, '0');
					Long l = Long.valueOf(newv);
					if (l == 0) {
						errors.reject(alieID, getMess("error.alie.num"));
					} else {
						alie = newv;
					}
				}
			} catch (Exception e) {
				errors.reject(alieID, getMess("error.alie.num"));
			}
		}
		return (alie);
	}
	protected void checkUSCIS(String uscis, String uscisID, Errors errors) {
		if (!StringUtil.isEmptyOrNullValue(uscis)) {
			uscis = uscis.trim();
			try {
				if (uscis.trim().length() != 12) {
					errors.rejectValue(uscisID, "error.uscis.num");
				} else {
					Long l = Long.valueOf(uscis);
					if (l == 0) {
						errors.rejectValue(uscisID, "error.uscis");
					}
				}
			} catch (Exception e) {
				errors.rejectValue(uscisID, "error.uscis");
			}
		}
	}
	
	protected void checkUssn(String ussn, String ussnID, Errors errors) {
		if (!StringUtil.isEmptyOrNullValue(ussn)) {
			ussn = ussn.trim();
			try {
				if (ussn.trim().length() != 9) {
					errors.rejectValue(ussnID, "error.scal_scty_s9");
				} else {
					Long l = Long.valueOf(ussn);
					if (l == 0) {
						errors.rejectValue(ussnID, "error.scal_scty");
					}
				}
			} catch (Exception e) {
				errors.rejectValue(ussnID, "error.scal_scty");
			}
		}
	}

	public boolean checkBooleanExpln(Boolean opt, String optX, Errors errors, String fld, int maxSize, String idFld) {
		if (null == opt) {
			errors.rejectValue(idFld + fld, "required.field");
			return false;
		} else {
			if (opt) {
				if (StringUtil.isEmptyOrNullValue(optX)) {
					errors.rejectValue(idFld + fld, "required.expl");
				} else {
					if (optX.length() > maxSize) {
						errors.rejectValue(idFld + fld, "Length", new Object[] { 0, maxSize, 1 }, "");
					}
				}
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean checkExpln(String optX, Errors errors, String fld, int maxSize, String idFld) {
		if (StringUtil.isEmptyOrNullValue(optX)) {
			errors.rejectValue(idFld + fld, "required.expl");
		} else {
			if (optX.length() > maxSize) {
				errors.rejectValue(idFld + fld, "Length", new Object[] { 0, maxSize, 1 }, "");
			}
		}
		return true;
	}
	protected boolean acceptFutureAndCurntDate(String date)
	 {
	  boolean flag=false;
	  String pattern = null;
	  LocalDate dtdob = null;
	  LocalDate dtToday = null;
	  
	  if(StringUtil.isEmptyOrNullValue(date) == false)
	  {
	   pattern = getMess().getMessage("pattern.date", null, null);
	   dtdob=StringUtil.convertStr2LD(date, pattern);
	   if(dtdob != null)
	   {
		   dtToday= LocalDate.now();
		   if(dtdob.toDate().compareTo(dtToday.toDate())< 0)
		   		flag=true;
	   }
	  }
	  
	  return flag;
	 }
	protected boolean acceptFutureDate(String date)
	 {
	  boolean flag=false;
	  String pattern = null;
	  LocalDate dtdob = null;
	  LocalDate dtToday = null;
	  
	  if(StringUtil.isEmptyOrNullValue(date) == false)
	  {
	   pattern = getMess().getMessage("pattern.date", null, null);
	   dtdob=StringUtil.convertStr2LD(date, pattern);
	   if(dtdob != null)
	   {
		   dtToday= LocalDate.now();
		   if(dtdob.toDate().compareTo(dtToday.toDate())<= 0)
		   		flag=true;
	   }
	  }
	  
	  return flag;
	 }
	protected boolean checkPastDate(String date)
	 {
	  boolean flag=false;
	  String pattern = null;
	  LocalDate dtdob = null;
	  LocalDate dtToday = null;
	  
	  if(StringUtil.isEmptyOrNullValue(date) == false)
	  {
		  pattern = getMess().getMessage("pattern.date", null, null);
		  dtdob=StringUtil.convertStr2LD(date, pattern);
		  if(dtdob != null)
		  {
			  dtToday= LocalDate.now();
			  if(dtToday.getYear() - dtdob.getYear() > 5)
				  flag=true;
		  }
	  }
	  
	  return flag;
	 }
}
