package com.fasset.excel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.fasset.entities.Bank;
import com.fasset.entities.Customer;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.form.GSTReport1Form;
import com.fasset.form.SubledgerExportForm;

public class OpeningBalancesExcelViewBuilder extends AbstractXlsxView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		 
		response.setHeader("Content-Disposition", "attachment; filename=\"OpeningBalancesExcel.xlsx\"");

		SubledgerExportForm form = (SubledgerExportForm) model.get("form");		
		int rowCount = 1;
		
		    CellStyle style = workbook.createCellStyle();
	        Font font = workbook.createFont();
	        font.setFontName("Arial");
	        style.setFillForegroundColor(HSSFColor.BLUE.index);
	        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        font.setColor(HSSFColor.WHITE.index);
	        style.setFont(font);
	        
	        Float total_credit = (float)0;
	        Float total_debit = (float)0;
	        
	        Sheet sheetb2b = workbook.createSheet("OpeningBalancesExcel");
	        SubLedger subledger1 = null;
	        List<SubLedger> subledgerList =form.getSubledgerList();
	        List<Bank> bankList=form.getBankList();
	        List<Suppliers> supplierList =form.getSupplierList();
	        List<Customer> customerList =form.getCustomerList();
	      
	           Row sub2 = sheetb2b.createRow(0);
	   	   	   sub2.createCell(0).setCellValue("Ledger Name");
	   	       sub2.getCell(0).setCellStyle(style);
	   	       sub2.createCell(1).setCellValue("Subledger Name");
	   	       sub2.getCell(1).setCellStyle(style);
	   	       sub2.createCell(2).setCellValue("Credit Balance");
	   	   	   sub2.getCell(2).setCellStyle(style);
	   	       sub2.createCell(3).setCellValue("Debit Balance");
	   	       sub2.getCell(3).setCellStyle(style);
	   	       
				sub2.createCell(4).setCellValue("Bank Name");
				sub2.getCell(4).setCellStyle(style);
				sub2.createCell(5).setCellValue("Branch Name/Account No");
				sub2.getCell(5).setCellStyle(style);
				sub2.createCell(6).setCellValue("Credit Balance");
				sub2.getCell(6).setCellStyle(style);
				sub2.createCell(7).setCellValue("Debit Balance");
				sub2.getCell(7).setCellStyle(style);
		   	   		
				
				
				sub2.createCell(8).setCellValue("Supplier Company Name");
				sub2.getCell(8).setCellStyle(style);
				sub2.createCell(9).setCellValue("Supplier Contact Name");
				sub2.getCell(9).setCellStyle(style);
				sub2.createCell(10).setCellValue("Credit Balance");
				sub2.getCell(10).setCellStyle(style);
				sub2.createCell(11).setCellValue("Debit Balance");
				sub2.getCell(11).setCellStyle(style);
	   			
				
				sub2.createCell(12).setCellValue(" Customer Company Name");
				sub2.getCell(12).setCellStyle(style);
				sub2.createCell(13).setCellValue("Customer Contact Name");
				sub2.getCell(13).setCellStyle(style);
				sub2.createCell(14).setCellValue("Credit Balance");
				sub2.getCell(14).setCellStyle(style);
	   			sub2.createCell(15).setCellValue("Debit Balance");
	   			sub2.getCell(15).setCellStyle(style);
	   			sub2.createCell(16).setCellValue("Accounting Year");
	   			sub2.getCell(16).setCellStyle(style);
	   			sub2.createCell(17).setCellValue("Date(DD/MM/YYYY)");
	   			sub2.getCell(17).setCellStyle(style);
	  
		        
	   	     while((subledgerList.size()!=0)||(bankList.size()!=0)||(supplierList.size()!=0)||(customerList.size()!=0)) 
	   	     {
	   	    	Row courseRow = sheetb2b.createRow(rowCount++);
	   	    	
	   			if(subledgerList.size()!=0)
	   			{
	   				
	   				for (SubLedger subledger: subledgerList){
		   	   			
	   					if(subledger1==null)
	   					{
	   					subledger1=subledger;
	   					if(subledger1.getOpeningbalances()!=null)
	   	 				{
	   	 				courseRow.createCell(16).setCellValue(subledger1.getOpeningbalances().getAccountingYear().getYear_range());
	   	 				}
	   					}
		   	   			if(subledger.getLedger()!=null)
		   	   			{
		   	   			courseRow.createCell(0).setCellValue(subledger.getLedger().getLedger_name());
		   	   			}
		   	   			courseRow.createCell(1).setCellValue(subledger.getSubledger_name());
		   	   			if(subledger.getOpeningbalances()!=null)
		   	   			{
		   	   			courseRow.createCell(2).setCellValue(subledger.getOpeningbalances().getCredit_balance());
		   	   		    total_credit=(float) (total_credit+subledger.getOpeningbalances().getCredit_balance());
		   	   			courseRow.createCell(3).setCellValue(subledger.getOpeningbalances().getDebit_balance());
		   	   		    total_debit=(float) (total_debit+subledger.getOpeningbalances().getDebit_balance());
		   	   			}
		   	   		subledgerList.remove(subledger);
		   	   		break;
		   	   		}	
	   			}
	   			else
	   			{
				courseRow.createCell(0).setCellValue("");
				courseRow.createCell(1).setCellValue("");

				courseRow.createCell(2).setCellValue("");

				courseRow.createCell(3).setCellValue("");
   	   		    
   	   			}
	   			
	   			
	   			if(bankList.size()!=0)
	   			{
	   				for (Bank bank: bankList){
		   	   			
		   	   			
		   	   			courseRow.createCell(4).setCellValue(bank.getBank_name());
		   	   			courseRow.createCell(5).setCellValue(bank.getBranch()+"/"+bank.getAccount_no());
		   	   			if(bank.getOpeningbalances()!=null)
		   	   			{
		   	   			courseRow.createCell(6).setCellValue(bank.getOpeningbalances().getCredit_balance());
		   	   		    total_credit=(float) (total_credit+bank.getOpeningbalances().getCredit_balance());
		   	   			courseRow.createCell(7).setCellValue(bank.getOpeningbalances().getDebit_balance());
		   	   		    total_debit=(float) (total_debit+bank.getOpeningbalances().getDebit_balance());
		   	   			}
		   	   			
		   	   		bankList.remove(bank);
		   	   	    break;
		   	   		}	
	   			}
	   			else
	   			{
				courseRow.createCell(4).setCellValue("");
				courseRow.createCell(5).setCellValue("");

				courseRow.createCell(6).setCellValue("");

				courseRow.createCell(7).setCellValue("");
   	   		    
   	   			}
	   			
	   			
	   			if(supplierList.size()!=0)
	   			{
	   				for (Suppliers supplier: supplierList){
		   	   			
		   	   			
		   	   			courseRow.createCell(8).setCellValue(supplier.getCompany_name());
		   	   			courseRow.createCell(9).setCellValue(supplier.getContact_name());
		   	   			if(supplier.getOpeningbalances()!=null)
		   	   			{
		   	   			courseRow.createCell(10).setCellValue(supplier.getOpeningbalances().getCredit_balance());
		   	   		    total_credit=(float) (total_credit+supplier.getOpeningbalances().getCredit_balance());
		   	   			courseRow.createCell(11).setCellValue(supplier.getOpeningbalances().getDebit_balance());
		   	   		    total_debit=(float) (total_debit+supplier.getOpeningbalances().getDebit_balance());
		   	   			}
		   	   			
		   	   		supplierList.remove(supplier);
		   	   	   break;
		   	   		}	
	   			}
	   			else
	   			{
				courseRow.createCell(8).setCellValue("");
				courseRow.createCell(9).setCellValue("");

				courseRow.createCell(10).setCellValue("");

				courseRow.createCell(11).setCellValue("");
   	   		    
   	   			}
	   			
	   			if(customerList.size()!=0)
	   			{
	   				for (Customer customer: customerList){
		   	   			
		   	   			
		   	   			courseRow.createCell(12).setCellValue(customer.getFirm_name());
		   	   			courseRow.createCell(13).setCellValue(customer.getContact_name());
		   	   			if(customer.getOpeningbalances()!=null)
		   	   			{
		   	   			courseRow.createCell(14).setCellValue(customer.getOpeningbalances().getCredit_balance());
		   	   	        total_credit=(float) (total_credit+customer.getOpeningbalances().getCredit_balance());
		   	   			courseRow.createCell(15).setCellValue(customer.getOpeningbalances().getDebit_balance());
		   	   		    total_debit=(float) (total_debit+customer.getOpeningbalances().getDebit_balance());
		   	   			}
		   	   			
		   	   		customerList.remove(customer);
		   	   	 break;
		   	   		}
	   			}
	   			else
	   			{
				courseRow.createCell(12).setCellValue("");
				courseRow.createCell(13).setCellValue("");

				courseRow.createCell(14).setCellValue("");

				courseRow.createCell(15).setCellValue("");
   	   		    
   	   			}
	   		
	   	     }
	        }
	
	public static Float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}