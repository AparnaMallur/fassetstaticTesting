package com.fasset.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.jfree.data.time.Year;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IContraDAO;
import com.fasset.dao.interfaces.ICreditNoteDAO;
import com.fasset.dao.interfaces.IDebitNoteDAO;
import com.fasset.dao.interfaces.IDepreciationAUtoJVDAO;
import com.fasset.dao.interfaces.IManualJournalVoucherDAO;
import com.fasset.dao.interfaces.IPaymentDAO;
import com.fasset.dao.interfaces.IPayrollAutoJVServiceDAO;
import com.fasset.dao.interfaces.IPurchaseEntryDAO;
import com.fasset.dao.interfaces.IReceiptDAO;
import com.fasset.dao.interfaces.ISalesEntryDAO;
import com.fasset.service.interfaces.ICommonService;

@Transactional
@Service
public class CommonServiceImpl implements ICommonService{
	
	@Autowired
	private IContraDAO contraDao;
	
	@Autowired
	private ISalesEntryDAO salesEntryDao;
	
	@Autowired
	private IPurchaseEntryDAO purchaseEntryDao;
	
	@Autowired
	private IPaymentDAO paymentDao;
	
	@Autowired
	private IManualJournalVoucherDAO manualJVDAO;

	@Autowired
	private IReceiptDAO receiptDao;
	
	@Autowired
	private IPayrollAutoJVServiceDAO payrollDAO;
	

	@Autowired
	private IDepreciationAUtoJVDAO depreciationDAO;
	
	
	
	
	@Autowired
	private ICreditNoteDAO creditNoteDao;
	
	@Autowired
	private IDebitNoteDAO debitNoteDao;
	

 	final String[] units = { "", "One", "Two", "Three", "Four",
 			"Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
 			"Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
 			"Eighteen", "Nineteen" };
 			
 	final String[] tens = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", 
 			"Seventy", "Eighty", "Ninety"}; 

	@Override
	public String getVoucherNumber(String range, Integer type, LocalDate date,Long yearId, Long companyId) {
		String prefix =range;
		int count = 0;
		String currentDate = (date.getDayOfMonth()<10)? "0"+Integer.toString(date.getDayOfMonth()):Integer.toString(date.getDayOfMonth());
		String currentMonth = (date.getMonthOfYear()<10)? "0"+Integer.toString(date.getMonthOfYear()):Integer.toString(date.getMonthOfYear());
		String voucherNo;

		switch(type){
			case MyAbstractController.VOUCHER_PURCHASE_ENTRY : 	
				count = purchaseEntryDao.getCountByDate(companyId, range, date);
				//count = purchaseEntryDao.getCountByYear(yearId,companyId, range);
				break;
											
			case MyAbstractController.VOUCHER_SALES_ENTRY 	:	
				count = salesEntryDao.getCountByYear(yearId, companyId, range);
				break; 
				
			case MyAbstractController.VOUCHER_PAYMENT 		:	
				count = paymentDao.getCountByDate(companyId, range, date);
				break; 
											
			case MyAbstractController.VOUCHER_RECEIPT 		: 	
				count = receiptDao.getCountByDate(companyId, range, date);
				
				break; 
											
			case MyAbstractController.VOUCHER_CREDIT_NOTE 	:	
				count = creditNoteDao.getCountByDate(companyId, range, date);
				break;
											
			case MyAbstractController.VOUCHER_DEBIT_NOTE 	:	
				count = debitNoteDao.getCountByDate(companyId, range, date);
				break;
											
			case MyAbstractController.VOUCHER_CONTRA 		:	
				count = contraDao.getCountByDate(companyId, range, date);
				break; 
				

			case MyAbstractController.AutoJV 		:	
				count = payrollDAO.getVoucherS(companyId, range, date);
				break; 
				
		/*
		 * case MyAbstractController.VOUCHER_PAYROLL : count =
		 * payrollDAO.getVoucherS(companyId, range, date); break; case
		 * MyAbstractController.DEPRECIATIONAUTOJV : count =
		 * payrollDAO.getVoucherS(companyId, range, date); break; case
		 * MyAbstractController.YEAR_AUTOJV : count =payrollDAO.getVoucherS(companyId,
		 * range, date); break;
		 * 
		 * case MyAbstractController.VOUCHER_GSTAJ : count
		 * =payrollDAO.getVoucherS(companyId, range, date); break;
		 * 
		 * 
		 */
				
		}
		
		if(type==MyAbstractController.VOUCHER_SALES_ENTRY)
		{
			if (count > 0) {
				if (count < 9) {
					voucherNo = prefix +"/"+ currentDate+ currentMonth + date.getYear() +"/"+ "00"+(count + 1);
				}
				else if (count >= 9 && count < 99) {
					voucherNo = prefix +"/"+ currentDate+ currentMonth + date.getYear() +"/"+ "0"+(count + 1);
				} 
				else {
					voucherNo = prefix +"/"+ currentDate+ currentMonth + date.getYear() +"/"+(count + 1);
				}
			} else {
				voucherNo = prefix +"/"+ currentDate+ currentMonth + date.getYear() +"/"+"001";
			}
			
		}
		else if(type==MyAbstractController.AutoJV)
		{
			if (count > 0) {
				if (count < 9) {
					voucherNo = prefix +"/"+ currentMonth + date.getYear() +"/"+ "00"+(count + 1);
				}
				else if (count >= 9 && count < 99) {
					voucherNo = prefix +"/"+ currentMonth + date.getYear() +"/"+ "0"+(count + 1);
				} 
				else {
					voucherNo = prefix +"/"+ currentMonth + date.getYear() +"/"+(count + 1);
				}
			} else {
				voucherNo = prefix +"/"+  currentMonth + date.getYear() +"/"+"001";
			}	
			
		}
		/*
		 * else if(type==MyAbstractController.VOUCHER_PAYROLL) { if (count > 0) { if
		 * (count < 9) { voucherNo = prefix +"/"+ currentMonth + date.getYear() +"/"+
		 * "00"+(count + 1); } else if (count >= 9 && count < 99) { voucherNo = prefix
		 * +"/"+ currentMonth + date.getYear() +"/"+ "0"+(count + 1); } else { voucherNo
		 * = prefix +"/"+ currentMonth + date.getYear() +"/"+(count + 1); } } else {
		 * voucherNo = prefix +"/"+ currentMonth + date.getYear() +"/"+"001"; }
		 * 
		 * }
		 */
		/*
		 * else if(type==MyAbstractController.DEPRECIATIONAUTOJV) { if (count > 0) { if
		 * (count < 9) { voucherNo = prefix +"/"+ currentMonth + date.getYear() +"/"+
		 * "00"+(count + 1); } else if (count >= 9 && count < 99) { voucherNo = prefix
		 * +"/"+ currentMonth + date.getYear() +"/"+ "0"+(count + 1); } else { voucherNo
		 * = prefix +"/"+ currentMonth + date.getYear() +"/"+(count + 1); } } else {
		 * voucherNo = prefix +"/"+ currentMonth + date.getYear() +"/"+"001"; }
		 * 
		 * }
		 */
		/*
		 * else if(type==MyAbstractController.VOUCHER_GSTAJ) { if (count > 0) { if
		 * (count < 9) { voucherNo = prefix +"/"+ currentMonth + date.getYear() +"/"+
		 * "00"+(count + 1); } else if (count >= 9 && count < 99) { voucherNo = prefix
		 * +"/"+ currentMonth + date.getYear() +"/"+ "0"+(count + 1); } else { voucherNo
		 * = prefix +"/"+ currentMonth + date.getYear() +"/"+(count + 1); } } else {
		 * voucherNo = prefix +"/"+ currentMonth + date.getYear() +"/"+"001"; }
		 * 
		 * }
		 */
		else{
			
			
			if (count > 0) {
				if (count < 9) {
					voucherNo = prefix + "/" + currentDate + currentMonth + date.getYear() + "/" + "00" + (count + 1);
				} else if (count >= 9 && count < 99) {
					voucherNo = prefix + "/" + currentDate + currentMonth + date.getYear() + "/" + "0" + (count + 1);
				} else {
					voucherNo = prefix + "/" + currentDate + currentMonth + date.getYear() + "/" + (count + 1);
				}
			} else {
				voucherNo = prefix + "/" + currentDate + currentMonth + date.getYear() + "/" + "001";
			}

		}

		return voucherNo;
	}

	@Override
	public String convertNumberToWord(Double n) {	
 		
 		if (n < 20) {
 			return units[(int) (n+0)];
 		}
 		
 		else if (n < 100) {
 			return tens[(int) (n / 10)] + " " + units[(int) (n % 10)];
 		}
 		
 		else if (n < 1000) {
 			return units[(int) (n / 100)] + " Hundred " + convertNumberToWord(n % 100);
 		}
 		
 		else if (n < 100000) {
 			return convertNumberToWord(n / 1000) + " Thousand " + convertNumberToWord(n % 1000);
 		}
 		
 		else if (n < 10000000) {
 			return convertNumberToWord(n / 100000) + " Lakh " + convertNumberToWord(n % 100000);
 		}
 		else {
 			return convertNumberToWord(n / 10000000) + " Crore " + convertNumberToWord(n % 10000000);
 		}
	}

	@Override
	public String convertFloatNumberToWord(Float num) {
		String number = Float.toString(num);
		String[] words=number.split("\\.");
		String amountInWord = "Rs. "+convertNumberToWord(new Double(words[0]));
		if(new Double(words[1]) > 0) {
			amountInWord = amountInWord + " and " + convertNumberToWord(new Double (words[1]))+" Paise";
		}
		amountInWord = amountInWord+" Only";
		return amountInWord;
	}

	@Override
	public String convertDoubleNumberToWord(Double num) {
		String number = Double.toString(num);
		String[] words=number.split("\\.");
		String amountInWord = "Rs. "+convertNumberToWord(new Double(words[0])).toString();
		if(new Double(words[1]) > 0) {
			amountInWord = amountInWord + " and " + convertNumberToWord(new Double(words[1]))+" Paise";
		}
		amountInWord = amountInWord+" Only";
		return amountInWord;
	}	
	
	@Override
	public String round(Double debit_balance, int decimalPlace) {
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(decimalPlace);
		return df.format(debit_balance);
	}

	@Override
	public Float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	@Override
	public LocalDate getApril1StDate(LocalDate date) {
		String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018
		String april1stDate=null;
		String fromDate= null;

		april1stDate=currentyear+"-"+"04"+"-"+"01";
		LocalDate fromDateLocaldate =null;
		LocalDate april1stLocaldate = new LocalDate(april1stDate);
		
		if(date.isBefore(april1stLocaldate)) 
		{
			Integer year = Integer.parseInt(currentyear);
			year=year-1;
			String lastYear =year.toString();
			fromDate=lastYear+"-"+"04"+"-"+"01";
			fromDateLocaldate = new LocalDate(fromDate);
		}
		else if(date.isAfter(april1stLocaldate) || date.equals(april1stLocaldate))
		{
			fromDateLocaldate=april1stLocaldate;
		}
		return fromDateLocaldate;
	}

	@Override
	public String getVoucherNumberForAUtoJV(LocalDate date,Long companyId) {
		String prefix ="AutoJV";
		int count = 0;
		
		String currentDate = (date.getDayOfMonth()<10)? "0"+Integer.toString(date.getDayOfMonth()):Integer.toString(date.getDayOfMonth());
		String currentMonth = (date.getMonthOfYear()<10)? "0"+Integer.toString(date.getMonthOfYear()):Integer.toString(date.getMonthOfYear());
		String voucherNo;
		
		count=payrollDAO.getVoucherS(companyId, prefix, date);
	
		if (count > 0) {
			if (count < 9) {
				voucherNo = prefix + "/" + currentDate + currentMonth + date.getYear() + "/" + "00" + (count + 1);
			} else if (count >= 9 && count < 99) {
				voucherNo = prefix + "/" + currentDate + currentMonth + date.getYear() + "/" + "0" + (count + 1);
			} else {
				voucherNo = prefix + "/" + currentDate + currentMonth + date.getYear() + "/" + (count + 1);
			}
		} else {
			voucherNo = prefix + "/" + currentDate + currentMonth + date.getYear() + "/" + "001";
		}
		return voucherNo;
	}

}