package com.fasset.excel;

import java.text.DecimalFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.fasset.entities.CreditDetails;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.form.GSTReport1Form;
import com.fasset.form.GSTdocsForm;
import com.fasset.form.HSNReportForm;
import com.fasset.pdf.ClasstoConvertDateformat;
import com.fasset.service.CommonServiceImpl;

public class GSTReport1ExcelBuilder extends AbstractXlsxView {

	private CommonServiceImpl commonService = new CommonServiceImpl();
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		 
		response.setHeader("Content-Disposition", "attachment; filename=\"gstReport1.xls\"");

		GSTReport1Form gstReport1Form = (GSTReport1Form) model.get("gstReport1Form");		

		int rowCount = 1;
		
		// create style for header cells
		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.BLACK.index);
        style.setFont(font);
       
        
        XSSFCellStyle style1 = (XSSFCellStyle) workbook.createCellStyle();
        style1.setAlignment(HorizontalAlignment.RIGHT);
/******************************************* Sheet for B2B *********************************************/
         // create excel xls sheet
		Sheet sheetb2b = workbook.createSheet("b2b");
         // create header row
		if(gstReport1Form.getCompany()!=null)
		{
			Row compname = sheetb2b.createRow(0);
			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
			
		if(gstReport1Form.getCompany().getPermenant_address()!=null)
		{
			Row address = sheetb2b.createRow(rowCount++);
			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
		
		}
		if(gstReport1Form.getCompany().getRegistration_no()!=null)
		{
			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Row cin = sheetb2b.createRow(rowCount++);
				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
			}
		}
		}
		
		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
		{
			Row dates = sheetb2b.createRow(rowCount++);
			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
		
		}
		
		
		
		Row emptyrow = sheetb2b.createRow(rowCount++);
		emptyrow.createCell(0).setCellValue("");
		
		
		Row headerb2b1 = sheetb2b.createRow(rowCount++);
		headerb2b1.createCell(0).setCellValue("No. of Recipients");
		headerb2b1.createCell(1).setCellValue("No. of Invoices");
		headerb2b1.createCell(2).setCellValue("");
		headerb2b1.createCell(3).setCellValue("Total Invoice Value");
		headerb2b1.createCell(4).setCellValue("");
		headerb2b1.createCell(5).setCellValue("");
		headerb2b1.createCell(6).setCellValue("");
		headerb2b1.createCell(7).setCellValue("");
		headerb2b1.createCell(8).setCellValue("");
		headerb2b1.createCell(9).setCellValue("Total Taxable Value");
		headerb2b1.createCell(10).setCellValue("Total Cess");
		
		for(int i=0;i<=10;i++)
		{
			headerb2b1.getCell(i).setCellStyle(style);
		}
		
		
		Row headerb2b2 = sheetb2b.createRow(rowCount++);

		Double Total_taxb2b = (double)0;
		Double Total_cessb2b = (double)0;
		Double Total_invoiceb2b = (double)0;
		
		
		for (SalesEntry salesEntry : gstReport1Form.getB2bList()){
			
			  if(salesEntry.getCustomer()!=null) // for restrict ---entries against cash sales
			  {
					if(salesEntry.getTransaction_value()!=null)
					{
					
					Total_taxb2b=Total_taxb2b+salesEntry.getTransaction_value();
					}
				
					if(salesEntry.getState_compansation_tax()!=null)
					{
						
						Total_cessb2b=Total_cessb2b+salesEntry.getState_compansation_tax();
					}
					if(salesEntry.getTds_amount()!=null)
					{
						Total_invoiceb2b=Total_invoiceb2b+salesEntry.getRound_off()+salesEntry.getTds_amount();
						
					}
					else if(salesEntry.getRound_off()!=null)
					{
						Total_invoiceb2b=Total_invoiceb2b+salesEntry.getRound_off();
						
					}
					
					
			  }
		}
		
		headerb2b2.createCell(0).setCellValue(gstReport1Form.getNoOfRecipientsForB2bList());
		headerb2b2.createCell(1).setCellValue(gstReport1Form.getB2bList().size());
		headerb2b2.createCell(2).setCellValue("");
		headerb2b2.createCell(3).setCellValue(round(Total_invoiceb2b,2).toString());
		headerb2b2.createCell(4).setCellValue("");
		headerb2b2.createCell(5).setCellValue("");
		headerb2b2.createCell(6).setCellValue("");
		headerb2b2.createCell(7).setCellValue("");
		headerb2b2.createCell(8).setCellValue("");
		headerb2b2.createCell(9).setCellValue(round(Total_taxb2b,2).toString());
		headerb2b2.createCell(10).setCellValue(round(Total_cessb2b,2).toString());
		
		for(int i=0;i<=10;i++)
		{
			headerb2b2.getCell(i).setCellStyle(style1);
		}
		
		Row headerb2b = sheetb2b.createRow(rowCount++);
		headerb2b.createCell(0).setCellValue("GSTIN/UIN of Recipient");
		headerb2b.createCell(1).setCellValue("Invoice Number");
		headerb2b.createCell(2).setCellValue("Invoice Date");
		headerb2b.createCell(3).setCellValue("Invoice Value");
		headerb2b.createCell(4).setCellValue("Place of Supply");
		headerb2b.createCell(5).setCellValue("Reverse Charge");
		headerb2b.createCell(6).setCellValue("Invoice Type");
		headerb2b.createCell(7).setCellValue("E-Commerce GSTIN");
		headerb2b.createCell(8).setCellValue("Rate");
		headerb2b.createCell(9).setCellValue("Taxable Value");
		headerb2b.createCell(10).setCellValue("Cess Amount");

		for(int i=0;i<=10;i++)
		{
			headerb2b.getCell(i).setCellStyle(style);
		}
	
		
         // Create data cells
		for (SalesEntry salesEntry : gstReport1Form.getB2bList())
		{
		
		 for (SalesEntryProductEntityClass product : salesEntry.getProductinfoList())
		 {	
		  if(salesEntry.getCustomer()!=null) // for restrict ---entries against cash sales
		  {
			  Row courseRow = sheetb2b.createRow(rowCount++);
			  
				if(salesEntry.getCustomer().getGst_no()!=null)
				{
					courseRow.createCell(0).setCellValue(salesEntry.getCustomer().getGst_no());
				}
				else
				{
					courseRow.createCell(0).setCellValue("");
				}
		
		
			
			if(salesEntry.getVoucher_no()!=null)
			{
				courseRow.createCell(1).setCellValue(salesEntry.getVoucher_no());
			}
			else
			{
				courseRow.createCell(1).setCellValue("");
			}
			
			
			if(salesEntry.getCreated_date()!=null)
			{
				courseRow.createCell(2).setCellValue(ClasstoConvertDateformat.dateFormatForGSTR(salesEntry.getCreated_date().toString()));
			}
			else
			{
				courseRow.createCell(2).setCellValue("");
			}
			
			if(salesEntry.getTds_amount()!=null)
			{
				
				courseRow.createCell(3).setCellValue(round(salesEntry.getRound_off()+salesEntry.getTds_amount(),2).toString());
			}
			else if(salesEntry.getRound_off()!=null)
			{
				courseRow.createCell(3).setCellValue(round(salesEntry.getRound_off(),2).toString());
				
			}
			else
			{
				courseRow.createCell(3).setCellValue("");
			}
			
			if(salesEntry.getCustomer()!=null)
			{
				if(salesEntry.getCustomer().getState()!=null)
				{
				courseRow.createCell(4).setCellValue(salesEntry.getCustomer().getState().getState_name()+"-"+salesEntry.getCustomer().getState().getState_code());
				}
				else
				{
					courseRow.createCell(4).setCellValue("");
				}
			}
			else
			{
				courseRow.createCell(4).setCellValue("");
			}
			
			
			courseRow.createCell(5).setCellValue("N");
			courseRow.createCell(6).setCellValue("--");
			courseRow.createCell(7).setCellValue("--");
			courseRow.createCell(8).setCellValue(round(new Double(product.getGstRate()),2).toString());
			
			if(product.getTransaction_amount()!=null)
			{
			courseRow.createCell(9).setCellValue(round(product.getTransaction_amount(),2).toString());
			
			}
			else
			{
				courseRow.createCell(9).setCellValue("");
			}
			if(product.getState_com_tax()!=null)
			{
				courseRow.createCell(10).setCellValue(round(product.getState_com_tax(),2).toString());
			
			}
			else
			{
				courseRow.createCell(10).setCellValue("");
			}
		
			for(int i=0;i<=10;i++)
			{
				if(i==3||i==8||i==9||i==10)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
			
	     	}
		}
		  
		  
		}	
		

/******************************************* Sheet for B2Cl *********************************************/		
		// create excel xls sheet
		Sheet sheetb2cl = workbook.createSheet("b2cl");
		rowCount = 1;
		
		if(gstReport1Form.getCompany()!=null)
		{
			Row compname = sheetb2cl.createRow(0);
			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
			
		if(gstReport1Form.getCompany().getPermenant_address()!=null)
		{
			Row address = sheetb2cl.createRow(rowCount++);
			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
		
		}
		if(gstReport1Form.getCompany().getRegistration_no()!=null)
		{
			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Row cin = sheetb2cl.createRow(rowCount++);
				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
			}
		}
		}
		
		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
		{
			Row dates = sheetb2cl.createRow(rowCount++);
			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
		
		}
		
		Row emptyrow1 = sheetb2cl.createRow(rowCount++);
		emptyrow1.createCell(0).setCellValue("");
		
		
		Row headerb2cl1 = sheetb2cl.createRow(rowCount++);
		headerb2cl1.createCell(0).setCellValue("No. of Invoices");
		headerb2cl1.createCell(1).setCellValue("");
		headerb2cl1.createCell(2).setCellValue("Total Invoice Value");
		headerb2cl1.createCell(3).setCellValue("");
		headerb2cl1.createCell(4).setCellValue("");
		headerb2cl1.createCell(5).setCellValue("Total Taxable Value");
		headerb2cl1.createCell(6).setCellValue("Total Cess");
		headerb2cl1.createCell(7).setCellValue("");
		
		for(int i=0;i<=7;i++)
		{
			headerb2cl1.getCell(i).setCellStyle(style);
		}
		
		Row headerb2cl2 = sheetb2cl.createRow(rowCount++);
		
		Double Total_taxb2cl = (double)0;
		Double Total_cessb2cl = (double)0;
		Double Total_invoiceb2cl = (double)0;
		
		
		for (SalesEntry salesEntry : gstReport1Form.getB2clList()){
			
			  if(salesEntry.getCustomer()!=null) // for restrict ---entries against cash sales
			  {
					if(salesEntry.getTransaction_value()!=null)
					{
					
						Total_taxb2cl=Total_taxb2cl+salesEntry.getTransaction_value();
					}
				
					if(salesEntry.getState_compansation_tax()!=null)
					{
						
						Total_cessb2cl=Total_cessb2cl+salesEntry.getState_compansation_tax();
					}
					if(salesEntry.getTds_amount()!=null)
					{
						Total_invoiceb2cl=Total_invoiceb2cl+salesEntry.getRound_off()+salesEntry.getTds_amount();
						
					}
					else if(salesEntry.getRound_off()!=null)
					{
						Total_invoiceb2cl=Total_invoiceb2cl+salesEntry.getRound_off();
						
					}
					
					
			  }
		}
		
		headerb2cl2.createCell(0).setCellValue(gstReport1Form.getB2clList().size());
		headerb2cl2.createCell(1).setCellValue("");
		headerb2cl2.createCell(2).setCellValue(round(Total_invoiceb2cl,2).toString());
		headerb2cl2.createCell(3).setCellValue("");
		headerb2cl2.createCell(4).setCellValue("");
		headerb2cl2.createCell(5).setCellValue(round(Total_taxb2cl,2).toString());
		headerb2cl2.createCell(6).setCellValue(round(Total_cessb2cl,2).toString());
		headerb2cl2.createCell(7).setCellValue("");
		
		for(int i=0;i<=7;i++)
		{
			headerb2cl2.getCell(i).setCellStyle(style1);
		}
		
         // create header row
		Row headerb2cl = sheetb2cl.createRow(rowCount++);
		headerb2cl.createCell(0).setCellValue("Invoice Number");
		headerb2cl.createCell(1).setCellValue("Invoice Date");
		headerb2cl.createCell(2).setCellValue("Invoice Value");
		headerb2cl.createCell(3).setCellValue("Place of Supply");
		headerb2cl.createCell(4).setCellValue("Rate");
		headerb2cl.createCell(5).setCellValue("Taxable Value");
		headerb2cl.createCell(6).setCellValue("Cess Amount");
		headerb2cl.createCell(7).setCellValue("E-Commerce GSTIN");
		
		for(int i=0;i<=7;i++)
		{
			headerb2cl.getCell(i).setCellStyle(style);
		}
	
		
		
         // Create data cells
		   for (SalesEntry salesEntry : gstReport1Form.getB2clList())
		   {
			
			 for (SalesEntryProductEntityClass product : salesEntry.getProductinfoList())
	        {	
			if(salesEntry.getCustomer()!=null) // for restrict ---entries against cash sales
		    {
				Row courseRow = sheetb2cl.createRow(rowCount++);
			if(salesEntry.getVoucher_no()!=null)
			{
				courseRow.createCell(0).setCellValue(salesEntry.getVoucher_no());
			}
			else
			{
				courseRow.createCell(0).setCellValue("");
			}
			
			
			if(salesEntry.getCreated_date()!=null)
			{
				courseRow.createCell(1).setCellValue(ClasstoConvertDateformat.dateFormatForGSTR(salesEntry.getCreated_date().toString()));
			}
			else
			{
				courseRow.createCell(1).setCellValue("");
			}
			
			if(salesEntry.getTds_amount()!=null)
			{
				courseRow.createCell(2).setCellValue(round(salesEntry.getRound_off()+salesEntry.getTds_amount(),2).toString());
			}
			else if(salesEntry.getRound_off()!=null)
			{
				courseRow.createCell(2).setCellValue(round(salesEntry.getRound_off(),2).toString());
				
			}
			else
			{
				courseRow.createCell(2).setCellValue("");
			}
			
			if(salesEntry.getCustomer()!=null)
			{
				if(salesEntry.getCustomer().getState()!=null)
				{
					courseRow.createCell(3).setCellValue(salesEntry.getCustomer().getState().getState_name()+"-"+salesEntry.getCustomer().getState().getState_code());
				}
				else
				{
					courseRow.createCell(3).setCellValue("");
				}
			}
			else
			{
				courseRow.createCell(3).setCellValue("");
			}
			
			courseRow.createCell(4).setCellValue(round(new Double(product.getGstRate()),2).toString());
			
			if(product.getTransaction_amount()!=null)
			{
				courseRow.createCell(5).setCellValue(round(product.getTransaction_amount(),2).toString());
				
			}
			else
			{
				courseRow.createCell(5).setCellValue("");
			}
			
			
			if(product.getState_com_tax()!=null)
			{
				courseRow.createCell(6).setCellValue(round(product.getState_com_tax(),2).toString());
			
			}
			else
			{
				courseRow.createCell(6).setCellValue("");
			}
			courseRow.createCell(7).setCellValue("--");
			
			for(int i=0;i<=7;i++)
			{
				if(i==2||i==4||i==5||i==6)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		  }
			
		}
			
		}	
		
      
		
/******************************************* Sheet for B2Cs *********************************************/
		// create excel xls sheet
		Sheet sheetb2cs = workbook.createSheet("b2cs");
		
         rowCount = 1;
		
         if(gstReport1Form.getCompany()!=null)
 		{
 			Row compname = sheetb2cs.createRow(0);
 			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
 			
 		if(gstReport1Form.getCompany().getPermenant_address()!=null)
 		{
 			Row address = sheetb2cs.createRow(rowCount++);
 			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
 		
 		}
 		if(gstReport1Form.getCompany().getRegistration_no()!=null)
 		{
 			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
 					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
 			{
 				Row cin = sheetb2cs.createRow(rowCount++);
 				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
 			}
 		}
 		}
 		
 		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
 		{
 			Row dates = sheetb2cs.createRow(rowCount++);
 			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
 		
 		}
 		
 		Row emptyrow2 = sheetb2cs.createRow(rowCount++);
 		emptyrow2.createCell(0).setCellValue("");
 		
 		Row headerb2cs1 = sheetb2cs.createRow(rowCount++);
 		headerb2cs1.createCell(0).setCellValue("");
 		headerb2cs1.createCell(1).setCellValue("");
 		headerb2cs1.createCell(2).setCellValue("");
 		headerb2cs1.createCell(3).setCellValue("");
 		headerb2cs1.createCell(4).setCellValue("Total Taxable  Value");
 		headerb2cs1.createCell(5).setCellValue("Total Cess");
 		headerb2cs1.createCell(6).setCellValue("");
		
 		for(int i=0;i<=6;i++)
		{
 			headerb2cs1.getCell(i).setCellStyle(style);
		}
 		
 		Row headerb2cs2 = sheetb2cs.createRow(rowCount++);
 		
		Double Total_taxb2cs = (double)0;
		Double Total_cessb2cs = (double)0;
		
		for (SalesEntry salesEntry : gstReport1Form.getB2csList()){
			
			 
					if(salesEntry.getTransaction_value()!=null)
					{
					
						Total_taxb2cs=Total_taxb2cs+salesEntry.getTransaction_value();
					}
				
					if(salesEntry.getState_compansation_tax()!=null)
					{
						
						Total_cessb2cs=Total_cessb2cs+salesEntry.getState_compansation_tax();
					}
					
					
					
			
		}
		
		headerb2cs2.createCell(0).setCellValue("");
		headerb2cs2.createCell(1).setCellValue("");
		headerb2cs2.createCell(2).setCellValue("");
		headerb2cs2.createCell(3).setCellValue("");
		headerb2cs2.createCell(4).setCellValue(round(Total_taxb2cs,2).toString());
		headerb2cs2.createCell(5).setCellValue(round(Total_cessb2cs,2).toString());
		headerb2cs2.createCell(6).setCellValue("");
 		
		for(int i=0;i<=6;i++)
		{
			headerb2cs2.getCell(i).setCellStyle(style1);
		}
		
		// create header row
		Row headerb2cs = sheetb2cs.createRow(rowCount++);
		headerb2cs.createCell(0).setCellValue("Type");
		headerb2cs.createCell(1).setCellValue("Customer Company Name");
		headerb2cs.createCell(2).setCellValue("Place Of Supply");
		headerb2cs.createCell(3).setCellValue("Rate");
		headerb2cs.createCell(4).setCellValue("Taxable Value");
		headerb2cs.createCell(5).setCellValue("Cess Amount");
		headerb2cs.createCell(6).setCellValue("E-Commerce GSTIN");

		for(int i=0;i<=6;i++)
		{
			headerb2cs.getCell(i).setCellStyle(style);
		}
		
         // Create data cells
		for (SalesEntry salesEntry : gstReport1Form.getB2csList())
		 {
			for (SalesEntryProductEntityClass product : salesEntry.getProductinfoList())
	        {	
			 Row courseRow = sheetb2cs.createRow(rowCount++);
			 courseRow.createCell(0).setCellValue("--");
						
				if(salesEntry.getCustomer()!=null)
				{
					courseRow.createCell(1).setCellValue(salesEntry.getCustomer().getFirm_name());
				}
				else
				{
					 if(salesEntry.getSale_type()==1)
					 {
					courseRow.createCell(1).setCellValue("Cash Sales");
					 }
					 else if(salesEntry.getSale_type()==2)
					 {
						 courseRow.createCell(1).setCellValue("Card Sales"+salesEntry.getBank().getBank_name()+"-"+salesEntry.getBank().getAccount_no());
					 }
				}
				if(salesEntry.getCustomer()!=null)
				{
				if(salesEntry.getCustomer().getState()!=null)
				{
					courseRow.createCell(2).setCellValue(salesEntry.getCustomer().getState().getState_name()+"-"+salesEntry.getCustomer().getState().getState_code());
				}
				else
				{
					courseRow.createCell(2).setCellValue("");
				}
				}
				else
				{
					courseRow.createCell(2).setCellValue("");
				}
				
			courseRow.createCell(3).setCellValue(round(new Double(product.getGstRate()),2).toString());
			if(product.getTransaction_amount()!=null)
			{
				courseRow.createCell(4).setCellValue(round(product.getTransaction_amount(),2).toString());
				
			}
			else
			{
				courseRow.createCell(4).setCellValue("");
			}
			
			if(product.getState_com_tax()!=null)
			{
				courseRow.createCell(5).setCellValue(round(product.getState_com_tax(),2).toString());
			
			}
			else
			{
				courseRow.createCell(5).setCellValue("");
			}
			courseRow.createCell(6).setCellValue("--");
			
			for(int i=0;i<=6;i++)
			{
				if(i==3||i==4||i==5)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
			
		 }
		  
		}	
		
/******************************************* Sheet for CDNR *********************************************/
		// create excel xls sheet
		Sheet sheetcdnr = workbook.createSheet("cdnr");

        rowCount = 1;
		
        if(gstReport1Form.getCompany()!=null)
		{
			Row compname = sheetcdnr.createRow(0);
			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
			
		if(gstReport1Form.getCompany().getPermenant_address()!=null)
		{
			Row address = sheetcdnr.createRow(rowCount++);
			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
		
		}
		if(gstReport1Form.getCompany().getRegistration_no()!=null)
		{
			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Row cin = sheetcdnr.createRow(rowCount++);
				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
			}
		}
		}
		
		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
		{
			Row dates = sheetcdnr.createRow(rowCount++);
			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
		
		}
		
		Row emptyrow3 = sheetcdnr.createRow(rowCount++);
		emptyrow3.createCell(0).setCellValue("");
		
		Row headercdnr1 = sheetcdnr.createRow(rowCount++);
		headercdnr1.createCell(0).setCellValue("No. of Recipients");
		headercdnr1.createCell(1).setCellValue("No. of Invoices");
		headercdnr1.createCell(2).setCellValue("");
		headercdnr1.createCell(3).setCellValue("No. of Notes/Vouchers");
		headercdnr1.createCell(4).setCellValue("");
		headercdnr1.createCell(5).setCellValue("");
		headercdnr1.createCell(6).setCellValue("");
		headercdnr1.createCell(7).setCellValue("");
		headercdnr1.createCell(8).setCellValue("Total Note/Refund Voucher Value");
		headercdnr1.createCell(9).setCellValue("");
		headercdnr1.createCell(10).setCellValue("Total Taxable Value");
		headercdnr1.createCell(11).setCellValue("Total Cess");
		headercdnr1.createCell(12).setCellValue("");
		
		for(int i=0;i<=12;i++)
		{
			headercdnr1.getCell(i).setCellStyle(style);
		}
		
		
		Row headercdnr2 = sheetcdnr.createRow(rowCount++);
		Double Total_invoicecdnr = (double)0;
		Double Total_taxcdnr = (double)0;
		Double Total_cesscdnr = (double)0;
		
for (CreditNote creditNote : gstReport1Form.getCdnrList()){
			
			if(creditNote.getCustomer()!=null)
			{
				
				if(creditNote.getTds_amount()!=null)
				{
					Total_invoicecdnr=Total_invoicecdnr+creditNote.getRound_off()+creditNote.getTds_amount();
					
				}
				else
				{
					Total_invoicecdnr=Total_invoicecdnr+creditNote.getRound_off();
				}
				
				if(creditNote.getTransaction_value_head()!=null)
				{
			
				Total_taxcdnr=Total_taxcdnr+creditNote.getTransaction_value_head();
				}
				
				if(creditNote.getSCT_head()!=null)
				{
				Total_cesscdnr=Total_cesscdnr+creditNote.getSCT_head();
			     }
				
			}
}
		

headercdnr2.createCell(0).setCellValue(gstReport1Form.getNoOfRecipientsForCdnrList());
headercdnr2.createCell(1).setCellValue(gstReport1Form.getCdnrList().size());
headercdnr2.createCell(2).setCellValue("");
headercdnr2.createCell(3).setCellValue(gstReport1Form.getCdnrList().size());
headercdnr2.createCell(4).setCellValue("");
headercdnr2.createCell(5).setCellValue("");
headercdnr2.createCell(6).setCellValue("");
headercdnr2.createCell(7).setCellValue("");
headercdnr2.createCell(8).setCellValue(round(Total_invoicecdnr,2).toString());
headercdnr2.createCell(9).setCellValue("");
headercdnr2.createCell(10).setCellValue(round(Total_taxcdnr,2).toString());
headercdnr2.createCell(11).setCellValue(round(Total_cesscdnr,2).toString());
headercdnr2.createCell(12).setCellValue("");

for(int i=0;i<=12;i++)
{
	headercdnr2.getCell(i).setCellStyle(style1);
}
         // create header row
		Row headercdnr = sheetcdnr.createRow(rowCount++);
		headercdnr.createCell(0).setCellValue("GSTIN/UIN of Recipient");
		headercdnr.createCell(1).setCellValue("Invoice/Advance Receipt Number");
		headercdnr.createCell(2).setCellValue("Invoice/Advance Receipt date");
		headercdnr.createCell(3).setCellValue("Note/Refund Voucher Number");
		headercdnr.createCell(4).setCellValue("Note/Refund Voucher date");
		headercdnr.createCell(5).setCellValue("Document Type");
		headercdnr.createCell(6).setCellValue("Reason For Issuing document");
		headercdnr.createCell(7).setCellValue("Place Of Supply");
		headercdnr.createCell(8).setCellValue("Note/Refund Voucher Value");
		headercdnr.createCell(9).setCellValue("Rate");
		headercdnr.createCell(10).setCellValue("Taxable Value");
		headercdnr.createCell(11).setCellValue("Cess Amount");
		headercdnr.createCell(12).setCellValue("Pre GST");

		for(int i=0;i<=12;i++)
		{
			headercdnr.getCell(i).setCellStyle(style);
		}
		

		// Create data cells
		for (CreditNote creditNote : gstReport1Form.getCdnrList())
		 {
		   for (CreditDetails product : creditNote.getCreditDetails())
		   {	
			if(creditNote.getCustomer()!=null)
			{
			Row courseRow = sheetcdnr.createRow(rowCount++);
			
			
				if(creditNote.getCustomer().getGst_no()!=null)
				{
				courseRow.createCell(0).setCellValue(creditNote.getCustomer().getGst_no());
				}
				else
				{
					courseRow.createCell(0).setCellValue("");
				}
			
			if(creditNote.getSales_bill_id()!=null)
			{
				if(creditNote.getSales_bill_id().getVoucher_no()!=null)
				{
					courseRow.createCell(1).setCellValue(creditNote.getSales_bill_id().getVoucher_no());
				}
				else
				{
					courseRow.createCell(1).setCellValue("");
				}
			}
			else
			{
				courseRow.createCell(1).setCellValue("");
			}
			
			if(creditNote.getSales_bill_id()!=null)
			{
				if(creditNote.getSales_bill_id().getCreated_date()!=null)
				{
				courseRow.createCell(2).setCellValue(ClasstoConvertDateformat.dateFormatForGSTR(creditNote.getSales_bill_id().getCreated_date().toString()));
				}
				else
				{
					courseRow.createCell(2).setCellValue("");
				}
			}
			else
			{
				courseRow.createCell(2).setCellValue("");
			}
			
			if(creditNote.getVoucher_no()!=null)
			{
				courseRow.createCell(3).setCellValue(creditNote.getVoucher_no());
			}
			else
			{
				courseRow.createCell(3).setCellValue("");
			}
			
			if(creditNote.getDate()!=null)
			{
				courseRow.createCell(4).setCellValue(ClasstoConvertDateformat.dateFormatForGSTR(creditNote.getDate().toString()));
			}
			else
			{
				courseRow.createCell(4).setCellValue("");
			}
			
			
			courseRow.createCell(5).setCellValue("C");
			String remarkString = "";
			switch(creditNote.getDescription()) {
			case 1: remarkString = "Sales return";
					break;
			case 2: remarkString = "Post sales discount";
					break;
			case 3: remarkString = "Deficiency in services ";
					break;
			case 4: remarkString = "Correction in Invoice";
					break;
			case 5: remarkString = "Change in POS";
					break;
			case 6: remarkString = "Finalization of Provisional Assessment";
					break;
			case 7: remarkString = creditNote.getRemark();
					break;
		}
			
			courseRow.createCell(6).setCellValue(remarkString);
			
			if(creditNote.getCustomer()!=null)
			{
				if(creditNote.getCustomer().getState()!=null)
				{
				courseRow.createCell(7).setCellValue(creditNote.getCustomer().getState().getState_name()+"-"+creditNote.getCustomer().getState().getState_code());
				}
				else
				{
					courseRow.createCell(7).setCellValue("");
				}
			}
			else
			{
				courseRow.createCell(7).setCellValue("");
			}
			
			if(creditNote.getTds_amount()!=null)
			{
				courseRow.createCell(8).setCellValue(round(creditNote.getRound_off()+creditNote.getTds_amount(),2).toString());
			}
			else
			{
				courseRow.createCell(8).setCellValue(round(creditNote.getRound_off(),2).toString());
			}
			
			
			
			courseRow.createCell(9).setCellValue(round(new Double(product.getGstRate()),2).toString());
			
			if(product.getTransaction_amount()!=null)
			{
			courseRow.createCell(10).setCellValue(round(product.getTransaction_amount(),2).toString().toString());
			
			}
			else
			{
				courseRow.createCell(10).setCellValue("");
			}
	
		
		
			if(product.getState_com_tax()!=null)
			{
			courseRow.createCell(11).setCellValue(round(product.getState_com_tax(),2).toString().toString());
		
		     }
			else
			{
				courseRow.createCell(11).setCellValue("");
			}
			
			
			courseRow.createCell(12).setCellValue("--");
			for(int i=0;i<=12;i++)
			{
				if(i==11||i==8||i==9||i==10)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		   }
			
		 }
		}	

/******************************************* Sheet for CDNUR *********************************************/		
		// create excel xls sheet
		Sheet sheetcdnur = workbook.createSheet("cdnur");

        rowCount = 1;
		
        if(gstReport1Form.getCompany()!=null)
		{
			Row compname = sheetcdnur.createRow(0);
			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
			
		if(gstReport1Form.getCompany().getPermenant_address()!=null)
		{
			Row address = sheetcdnur.createRow(rowCount++);
			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
		
		}
		if(gstReport1Form.getCompany().getRegistration_no()!=null)
		{
			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Row cin = sheetcdnur.createRow(rowCount++);
				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
			}
		}
		}
		
		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
		{
			Row dates = sheetcdnur.createRow(rowCount++);
			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
		
		}
		
		Row emptyrow4 = sheetcdnur.createRow(rowCount++);
		emptyrow4.createCell(0).setCellValue("");
         // create header row
		
		Row headercdnur1 = sheetcdnur.createRow(rowCount++);
		headercdnur1.createCell(0).setCellValue("");
		headercdnur1.createCell(1).setCellValue("No. of Notes/Vouchers");
		headercdnur1.createCell(2).setCellValue("");
		headercdnur1.createCell(3).setCellValue("");
		headercdnur1.createCell(4).setCellValue("No. of Invoices");
		headercdnur1.createCell(5).setCellValue("");
		headercdnur1.createCell(6).setCellValue("");
		headercdnur1.createCell(7).setCellValue("");
		headercdnur1.createCell(8).setCellValue("Total Note Value");
		headercdnur1.createCell(9).setCellValue("");
		headercdnur1.createCell(10).setCellValue("Total Taxable Value");
		headercdnur1.createCell(11).setCellValue("Total Cess");
		headercdnur1.createCell(12).setCellValue("");
		
		for(int i=0;i<=12;i++)
		{
			headercdnur1.getCell(i).setCellStyle(style);
		}
		
		Row headercdnur2 = sheetcdnur.createRow(rowCount++);
		
		Double Total_invoicecdnur = (double)0;
		Double Total_taxcdnur = (double)0;
		Double Total_cesscdnur = (double)0;
		
		
for (CreditNote creditNote : gstReport1Form.getCdnurList()){
			
			if(creditNote.getCustomer()!=null)
			{
				
				if(creditNote.getTds_amount()!=null)
				{
					Total_invoicecdnur=Total_invoicecdnur+creditNote.getRound_off()+creditNote.getTds_amount();
					
				}
				else
				{
					Total_invoicecdnur=Total_invoicecdnur+creditNote.getRound_off();
				}
				
				if(creditNote.getTransaction_value_head()!=null)
				{
			
					Total_taxcdnur=Total_taxcdnur+creditNote.getTransaction_value_head();
				}
				
				if(creditNote.getSCT_head()!=null)
				{
					Total_cesscdnur=Total_cesscdnur+creditNote.getSCT_head();
			     }
				
			}
}
		
headercdnur2.createCell(0).setCellValue("");
headercdnur2.createCell(1).setCellValue(gstReport1Form.getCdnurList().size());
headercdnur2.createCell(2).setCellValue("");
headercdnur2.createCell(3).setCellValue("");
headercdnur2.createCell(4).setCellValue(gstReport1Form.getCdnurList().size());
headercdnur2.createCell(5).setCellValue("");
headercdnur2.createCell(6).setCellValue("");
headercdnur2.createCell(7).setCellValue("");
headercdnur2.createCell(8).setCellValue(round(Total_invoicecdnur,2).toString());
headercdnur2.createCell(9).setCellValue("");
headercdnur2.createCell(10).setCellValue(round(Total_taxcdnur,2).toString());
headercdnur2.createCell(11).setCellValue(round(Total_cesscdnur,2).toString());
headercdnur2.createCell(12).setCellValue("");

for(int i=0;i<=12;i++)
{
	headercdnur2.getCell(i).setCellStyle(style1);
}
		
		Row headercdnur = sheetcdnur.createRow(rowCount++);
		headercdnur.createCell(0).setCellValue("UR Type");
		headercdnur.createCell(1).setCellValue("Note/Refund Voucher Number");
		headercdnur.createCell(2).setCellValue("Note/Refund Voucher date");
		headercdnur.createCell(3).setCellValue("Document Type");
		headercdnur.createCell(4).setCellValue("Invoice/Advance Receipt Number");
		headercdnur.createCell(5).setCellValue("Invoice/Advance Receipt date");
		headercdnur.createCell(6).setCellValue("Reason For Issuing document");
		headercdnur.createCell(7).setCellValue("Place Of Supply");
		headercdnur.createCell(8).setCellValue("Note/Refund Voucher Value");
		headercdnur.createCell(9).setCellValue("Rate");
		headercdnur.createCell(10).setCellValue("Taxable Value");
		headercdnur.createCell(11).setCellValue("Cess Amount");
		headercdnur.createCell(12).setCellValue("Pre GST");

		for(int i=0;i<=12;i++)
		{
			headercdnur.getCell(i).setCellStyle(style);
		}
		
		// Create data cells
		
		
		
		for (CreditNote creditNote : gstReport1Form.getCdnurList())
		  {
			 for (CreditDetails product : creditNote.getCreditDetails())
	        {	
			if(creditNote.getCustomer()!=null)
			{
			Row courseRow = sheetcdnur.createRow(rowCount++);
			
			
			courseRow.createCell(0).setCellValue("--");
			
			if(creditNote.getVoucher_no()!=null)
			{
				courseRow.createCell(1).setCellValue(creditNote.getVoucher_no());
			}
			else
			{
				courseRow.createCell(1).setCellValue("");
			}
			
			if(creditNote.getDate()!=null)
			{
				courseRow.createCell(2).setCellValue(ClasstoConvertDateformat.dateFormatForGSTR(creditNote.getDate().toString()));
			}
			else
			{
				courseRow.createCell(2).setCellValue("");
			}
			
			
			courseRow.createCell(3).setCellValue("C");
			
			if(creditNote.getSales_bill_id()!=null)
			{
				if(creditNote.getSales_bill_id().getCreated_date()!=null)
				{
				courseRow.createCell(4).setCellValue(ClasstoConvertDateformat.dateFormatForGSTR(creditNote.getSales_bill_id().getCreated_date().toString()));
				}
				else
				{
					courseRow.createCell(4).setCellValue("");
				}
			}
			else
			{
				courseRow.createCell(4).setCellValue("");
			}
			
			if(creditNote.getVoucher_no()!=null)
			{
				courseRow.createCell(5).setCellValue(creditNote.getVoucher_no());
			}
			else
			{
				courseRow.createCell(5).setCellValue("");
			}
			
			String remarkString = "";
			switch(creditNote.getDescription()) {
			case 1: remarkString = "Sales return";
					break;
			case 2: remarkString = "Post sales discount";
					break;
			case 3: remarkString = "Deficiency in services ";
					break;
			case 4: remarkString = "Correction in Invoice";
					break;
			case 5: remarkString = "Change in POS";
					break;
			case 6: remarkString = "Finalization of Provisional Assessment";
					break;
			case 7: remarkString = creditNote.getRemark();
					break;
		}
			
			courseRow.createCell(6).setCellValue(remarkString);
			
			if(creditNote.getCustomer()!=null)
			{
				if(creditNote.getCustomer().getState()!=null)
				{
					courseRow.createCell(7).setCellValue(creditNote.getCustomer().getState().getState_name()+"-"+creditNote.getCustomer().getState().getState_code());
				}
				else
				{
					courseRow.createCell(7).setCellValue("");
				}
			}
			else
			{
				courseRow.createCell(7).setCellValue("");
			}
			if(creditNote.getTds_amount()!=null)
			{
				courseRow.createCell(8).setCellValue(round(creditNote.getRound_off()+creditNote.getTds_amount(),2).toString());
			}
			else
			{
				courseRow.createCell(8).setCellValue(round(creditNote.getRound_off(),2).toString());
			}
			
			courseRow.createCell(9).setCellValue(round(new Double(product.getGstRate()),2).toString());
	
			if(product.getTransaction_amount()!=null)
			{
			courseRow.createCell(10).setCellValue(round(product.getTransaction_amount(),2).toString().toString());
			
			}
			else
			{
				courseRow.createCell(10).setCellValue("");
			}

			if(product.getState_com_tax()!=null)
			{
			courseRow.createCell(11).setCellValue(round(product.getState_com_tax(),2).toString().toString());
			
		     }
			else
			{
				courseRow.createCell(11).setCellValue("");
			}
			
			courseRow.createCell(12).setCellValue("--");
			for(int i=0;i<=12;i++)
			{
				if(i==11||i==8||i==9||i==10)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		   }
		  }
		}	
		
     
/******************************************* Sheet for Exp *********************************************/
		// create excel xls sheet
		Sheet sheetexp = workbook.createSheet("exp");

         // create header row

        rowCount = 1;
		
        if(gstReport1Form.getCompany()!=null)
		{
			Row compname = sheetexp.createRow(0);
			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
			
		if(gstReport1Form.getCompany().getPermenant_address()!=null)
		{
			Row address = sheetexp.createRow(rowCount++);
			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
		
		}
		if(gstReport1Form.getCompany().getRegistration_no()!=null)
		{
			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Row cin = sheetexp.createRow(rowCount++);
				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
			}
		}
		}
		
		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
		{
			Row dates = sheetexp.createRow(rowCount++);
			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
		
		}
		
		Row emptyrow5 = sheetexp.createRow(rowCount++);
		emptyrow5.createCell(0).setCellValue("");
		
		Row headerexp1= sheetexp.createRow(rowCount++);
		headerexp1.createCell(0).setCellValue("");
		headerexp1.createCell(1).setCellValue("No. of Invoices");
		headerexp1.createCell(2).setCellValue("");
		headerexp1.createCell(3).setCellValue("Total Invoice Value");
		headerexp1.createCell(4).setCellValue("");
		headerexp1.createCell(5).setCellValue("No. of Shipping Bill");
		headerexp1.createCell(6).setCellValue("");
		headerexp1.createCell(7).setCellValue("");
		headerexp1.createCell(8).setCellValue("Total Taxable Value");
		
		for(int i=0;i<=8;i++)
		{
			headerexp1.getCell(i).setCellStyle(style);
		}
		
		Row headerexp2= sheetexp.createRow(rowCount++);
		
		Double Total_taxexp = (double)0;
		Double Total_invoiceexp = (double)0;
		
		for (SalesEntry salesEntry : gstReport1Form.getExpList()){
			
			  if(salesEntry.getCustomer()!=null) // for restrict ---entries against cash sales
			  {
					if(salesEntry.getTransaction_value()!=null)
					{
					
						Total_taxexp=Total_taxexp+salesEntry.getTransaction_value();
					}
					if(salesEntry.getTds_amount()!=null)
					{
						Total_invoiceexp=Total_invoiceexp+salesEntry.getRound_off()+salesEntry.getTds_amount();
						
					}
					else if(salesEntry.getRound_off()!=null)
					{
						Total_invoiceexp=Total_invoiceexp+salesEntry.getRound_off();
						
					}
					
					
			  }
		}
		
		headerexp2.createCell(0).setCellValue("");
		headerexp2.createCell(1).setCellValue(gstReport1Form.getExpList().size());
		headerexp2.createCell(2).setCellValue("");
		headerexp2.createCell(3).setCellValue(round(Total_invoiceexp,2).toString());
		headerexp2.createCell(4).setCellValue("");
		headerexp2.createCell(5).setCellValue(gstReport1Form.getExpList().size());
		headerexp2.createCell(6).setCellValue("");
		headerexp2.createCell(7).setCellValue("");
		headerexp2.createCell(8).setCellValue(round(Total_taxexp,2).toString());
		
		for(int i=0;i<=8;i++)
		{
			headerexp2.getCell(i).setCellStyle(style1);
		}
		
		
		Row headerexp= sheetexp.createRow(rowCount++);
		headerexp.createCell(0).setCellValue("Export Type");
		headerexp.createCell(1).setCellValue("Invoice Number");
		headerexp.createCell(2).setCellValue("Invoice date");
		headerexp.createCell(3).setCellValue("Invoice Value");
		headerexp.createCell(4).setCellValue("Port Code");
		headerexp.createCell(5).setCellValue("Shipping Bill Number");
		headerexp.createCell(6).setCellValue("Shipping Bill Date");
		headerexp.createCell(7).setCellValue("Rate");
		headerexp.createCell(8).setCellValue("Taxable Value");
		
		for(int i=0;i<=8;i++)
		{
			headerexp.getCell(i).setCellStyle(style);
		}
		


		// Create data cells
		for (SalesEntry salesEntry : gstReport1Form.getExpList())
		  {
			 for (SalesEntryProductEntityClass product : salesEntry.getProductinfoList())
			 {	
			if(salesEntry.getCustomer()!=null)
			{
			Row courseRow = sheetexp.createRow(rowCount++);
			if(salesEntry.getExport_type()!=null && salesEntry.getExport_type().equals(1))
			{
			courseRow.createCell(0).setCellValue("WPAY");
			}
			else
			{
				courseRow.createCell(0).setCellValue("WOPAY");
			}
			
			if(salesEntry.getVoucher_no()!=null)
			{
				courseRow.createCell(1).setCellValue(salesEntry.getVoucher_no());
			}
			else
			{
				courseRow.createCell(1).setCellValue("");
			}
			
			if(salesEntry.getCreated_date()!=null)
			{
			courseRow.createCell(2).setCellValue(ClasstoConvertDateformat.dateFormatForGSTR(salesEntry.getCreated_date().toString()));
			}
			else
			{
				courseRow.createCell(2).setCellValue("");
			}
			
			if(salesEntry.getTds_amount()!=null)
			{
				courseRow.createCell(2).setCellValue(round(salesEntry.getRound_off()+salesEntry.getTds_amount(),2).toString());
			}
			else if(salesEntry.getRound_off()!=null)
			{
				courseRow.createCell(2).setCellValue(round(salesEntry.getRound_off(),2).toString());
				
			}
			else
			{
				courseRow.createCell(2).setCellValue("");
			}
			
			if(salesEntry.getPort_code()!=null)
			{
			courseRow.createCell(4).setCellValue(salesEntry.getPort_code());
			}
			else
			{
				courseRow.createCell(4).setCellValue("");
			}
			
			if(salesEntry.getShipping_bill_no()!=null)
			{
				courseRow.createCell(5).setCellValue(salesEntry.getShipping_bill_no());
			}
			else
			{
				courseRow.createCell(5).setCellValue("");
			}
			
			if(salesEntry.getShipping_bill_date()!=null)
			{
				courseRow.createCell(6).setCellValue(ClasstoConvertDateformat.dateFormatForGSTR(salesEntry.getShipping_bill_date().toString()));
			}
			else
			{
				courseRow.createCell(6).setCellValue("");
			}
		
			courseRow.createCell(7).setCellValue(round(new Double(product.getGstRate()),2).toString());
			if(product.getTransaction_amount()!=null)
			{
			courseRow.createCell(8).setCellValue(round(product.getTransaction_amount(),2).toString());
			
			}
			else
			{
				courseRow.createCell(8).setCellValue("");
			}
			
			 for(int i=0;i<=8;i++)
				{
					if(i==3||i==8||i==7)
					{
						if(courseRow.getCell(i)!=null){
					courseRow.getCell(i).setCellStyle(style1);}
					}
				}
	     	}
		  }
		}	
		
/******************************************* Sheet for AT *********************************************/
		// create excel xls sheet
		Sheet sheetat = workbook.createSheet("at");

        rowCount = 1;
		
        if(gstReport1Form.getCompany()!=null)
		{
			Row compname = sheetat.createRow(0);
			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
			
		if(gstReport1Form.getCompany().getPermenant_address()!=null)
		{
			Row address = sheetat.createRow(rowCount++);
			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
		
		}
		if(gstReport1Form.getCompany().getRegistration_no()!=null)
		{
			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Row cin = sheetat.createRow(rowCount++);
				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
			}
		}
		}
		
		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
		{
			Row dates = sheetat.createRow(rowCount++);
			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
		
		}
		
		Row emptyrow6 = sheetat.createRow(rowCount++);
		emptyrow6.createCell(0).setCellValue("");
		
		Row headerat1= sheetat.createRow(rowCount++);
		headerat1.createCell(0).setCellValue("");
		headerat1.createCell(1).setCellValue("");
		headerat1.createCell(2).setCellValue("Total Advance Received");
		headerat1.createCell(3).setCellValue("Total Cess");
		
		for(int i=0;i<=3;i++)
		{
			headerat1.getCell(i).setCellStyle(style);
		}
		
		Row headerat2= sheetat.createRow(rowCount++);
		
		Double Total_cessat= (double)0;
		Double Total_grossat= (double)0;
		
for (Receipt receipt : gstReport1Form.getAtList()){
			
			if(receipt.getCustomer()!=null)// for restrict entries against subledger
			  {
				if(receipt.getTds_amount()!=null)
				{
			
				Total_grossat=Total_grossat+receipt.getAmount()+receipt.getTds_amount();
				}
				else if(receipt.getAmount()!=null)
				{
					Total_grossat=Total_grossat+receipt.getAmount();
				}
				if(receipt.getState_compansation_tax()!=null)
				{
					
					Total_cessat=Total_cessat+receipt.getState_compansation_tax();
					
				}
			  }
}

headerat2.createCell(0).setCellValue("");
headerat2.createCell(1).setCellValue("");
headerat2.createCell(2).setCellValue(round(Total_grossat,2).toString());
headerat2.createCell(3).setCellValue(round(Total_cessat,2).toString());
for(int i=0;i<=3;i++)
{
	headerat2.getCell(i).setCellStyle(style1);
}

         // create header row
		Row headerat= sheetat.createRow(rowCount++);
		headerat.createCell(0).setCellValue("Place Of Supply");
		headerat.createCell(1).setCellValue("Rate");
		headerat.createCell(2).setCellValue("Gross Advance Received");
		headerat.createCell(3).setCellValue("Cess Amount");

		for(int i=0;i<=3;i++)
		{
			headerat.getCell(i).setCellStyle(style);
		}
		
		

		// Create data cells
		for (Receipt receipt : gstReport1Form.getAtList()){
			
			if(receipt.getCustomer()!=null)// for restrict entries against subledger
			  {
			
			Row courseRow = sheetat.createRow(rowCount++);
			
			if(receipt.getCustomer()!=null)
			{
				if(receipt.getCustomer().getState()!=null)
				{
				courseRow.createCell(0).setCellValue(receipt.getCustomer().getState().getState_name()+"-"+receipt.getCustomer().getState().getState_code());
			    }
				else
				{
					courseRow.createCell(0).setCellValue("");
				}
			}
			else
			{
				courseRow.createCell(0).setCellValue("");
			}
			
			
			Double  total = (double)0;
			if(receipt.getCgst()!=null && receipt.getSgst()!=null && receipt.getIgst()!=null)
			{
				total =receipt.getCgst()+receipt.getSgst()+receipt.getIgst();
			courseRow.createCell(1).setCellValue(round(total,2).toString());
			
			}
			else
			{
				courseRow.createCell(1).setCellValue(round(total,2).toString());
			}
			if(receipt.getTds_amount()!=null)
			{
			courseRow.createCell(2).setCellValue(round(receipt.getAmount()+receipt.getTds_amount(),2).toString());
			Total_grossat=Total_grossat+receipt.getAmount()+receipt.getTds_amount();
			}
			else if(receipt.getAmount()!=null)
			{
				
				courseRow.createCell(2).setCellValue(round(receipt.getAmount(),2).toString());
				Total_grossat=Total_grossat+receipt.getAmount();
			}
			else
			{
				courseRow.createCell(2).setCellValue("0");
			}
			
			if(receipt.getState_compansation_tax()!=null)
			{
				courseRow.createCell(3).setCellValue(round(receipt.getState_compansation_tax(),2).toString());
				Total_cessat=Total_cessat+receipt.getState_compansation_tax();
				
			}
			else
			{
				courseRow.createCell(3).setCellValue("0");
			}
			for(int i=0;i<=3;i++)
			{
				if(i==3||i==2||i==1)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			
		   }
			
		   }
		}	
		
/******************************************* Sheet for ATAdj *********************************************/
		// create excel xls sheet
		Sheet sheetatadj = workbook.createSheet("atadj");

        rowCount = 1;
		
        if(gstReport1Form.getCompany()!=null)
		{
			Row compname = sheetatadj.createRow(0);
			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
			
		if(gstReport1Form.getCompany().getPermenant_address()!=null)
		{
			Row address = sheetatadj.createRow(rowCount++);
			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
		
		}
		if(gstReport1Form.getCompany().getRegistration_no()!=null)
		{
			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Row cin = sheetatadj.createRow(rowCount++);
				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
			}
		}
		}
		
		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
		{
			Row dates = sheetatadj.createRow(rowCount++);
			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
		
		}
		
		Row emptyrow7 = sheetatadj.createRow(rowCount++);
		emptyrow7.createCell(0).setCellValue("");
         // create header row
		Row headeratadj= sheetatadj.createRow(rowCount++);
		headeratadj.createCell(0).setCellValue("Place Of Supply");
		headeratadj.createCell(1).setCellValue("Rate");
		headeratadj.createCell(2).setCellValue("Gross Advance Received");
		headeratadj.createCell(3).setCellValue("Cess Amount");

		for(int i=0;i<=3;i++)
		{
			headeratadj.getCell(i).setCellStyle(style);
		}
		
		/*Double Total_rateatadj = (double)0;
		Double Total_cessatadj= (double)0;
		Double Total_grossatadj= (double)0;*/
		
		// Create data cells
		/*for (Receipt receipt : gstReport1Form.getAtAdjList()){
			
			if(receipt.getCustomer()!=null)// for restrict entries against subledger
			  {
			Row courseRow = sheetatadj.createRow(rowCount++);
			
			if(receipt.getCustomer()!=null)
			{
				if(receipt.getCustomer().getState()!=null)
				{
					courseRow.createCell(0).setCellValue(receipt.getCustomer().getState().getState_name()+"-"+receipt.getCustomer().getState().getState_code());
			    }
				else
				{
					courseRow.createCell(0).setCellValue("");
				}
			}
			else
			{
				courseRow.createCell(0).setCellValue("");
			}
			
			Double  total = (double)0;
			if(receipt.getCgst()!=null && receipt.getSgst()!=null && receipt.getIgst()!=null)
			{
				total =receipt.getCgst()+receipt.getSgst()+receipt.getIgst();
			courseRow.createCell(1).setCellValue(round(total,2).toString());
			Total_rateatadj=Total_rateatadj+total;
			}
			else
			{
				courseRow.createCell(1).setCellValue(round(total,2).toString());
			}
			
			if(receipt.getTds_amount()!=null)
			{
			courseRow.createCell(2).setCellValue(round(receipt.getAmount()+receipt.getTds_amount(),2).toString());
			Total_grossat=Total_grossat+receipt.getAmount()+receipt.getTds_amount();
			}
			else if(receipt.getAmount()!=null)
			{
				
				courseRow.createCell(2).setCellValue(round(receipt.getAmount(),2).toString());
				Total_grossat=Total_grossat+receipt.getAmount();
			}
			else
			{
				courseRow.createCell(2).setCellValue("");
			}
			
			if(receipt.getState_compansation_tax()!=null) 
			{
				courseRow.createCell(3).setCellValue(round(receipt.getState_compansation_tax(),2).toString());
				Total_cessatadj=Total_cessatadj+receipt.getState_compansation_tax();
			}
			else
			{
				courseRow.createCell(3).setCellValue("");
			}
		   }
		}*/	
		
/******************************************* Sheet for EXEMP *********************************************/
		Sheet sheetexemp = workbook.createSheet("exemp");

		rowCount = 1;
		if(gstReport1Form.getCompany()!=null)
		{
			Row compname = sheetexemp.createRow(0);
			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
			
		if(gstReport1Form.getCompany().getPermenant_address()!=null)
		{
			Row address = sheetexemp.createRow(rowCount++);
			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
		
		}
		if(gstReport1Form.getCompany().getRegistration_no()!=null)
		{
			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Row cin = sheetexemp.createRow(rowCount++);
				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
			}
		}
		}
		
		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
		{
			Row dates = sheetexemp.createRow(rowCount++);
			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
		
		}
		
		Row emptyrow8 = sheetexemp.createRow(rowCount++);
		emptyrow8.createCell(0).setCellValue("");
		
		Row headerexemp1= sheetexemp.createRow(rowCount++);
		headerexemp1.createCell(0).setCellValue("");
		headerexemp1.createCell(1).setCellValue("Total Nil Rated Supplies");
		headerexemp1.createCell(2).setCellValue("Total Non-GST Supplies");
		for(int i=0;i<=2;i++)
		{
			headerexemp1.getCell(i).setCellStyle(style);
		}
		
		Row headerexemp2= sheetexemp.createRow(rowCount++);
		
		Double Total_nil = (double)0;
		Double Total_nongst = (double)0;
		
		for(Object form[] : gstReport1Form.getInterRegisterList())
		{
			if(form[0]!=null && form[1]!=null)
			{
				Total_nil=Total_nil+(double)form[0]+(double)form[1];
			}
		}
		
		for(Object form[] : gstReport1Form.getIntraRegisterList())
		{
			if(form[0]!=null && form[1]!=null)
			{
				Total_nil=Total_nil+(double)form[0]+(double)form[1];
			}
		}
		for(Object form[] : gstReport1Form.getInterNonRegisterList())
		{
			if(form[0]!=null && form[1]!=null)
			{
				Total_nil=Total_nil+(double)form[0]+(double)form[1];
			}
		}
		for(Object form[] : gstReport1Form.getIntraNonRegisterList())
		{
			if(form[0]!=null && form[1]!=null)
			{
				Total_nil=Total_nil+(double)form[0]+(double)form[1];
			}
		}
		
		for(Object form[] : gstReport1Form.getInterRegisterVATList())
		{
			if(form[0]!=null && form[1]!=null)
			{
				Total_nongst=Total_nongst+(double)form[0]+(double)form[1];
			}
		}
		for(Object form[] : gstReport1Form.getIntraRegisterVATList())
		{
			if(form[0]!=null && form[1]!=null)
			{
				Total_nongst=Total_nongst+(double)form[0]+(double)form[1];
			}
		}
		for(Object form[] : gstReport1Form.getInterNonRegisterVATList())
		{
			if(form[0]!=null && form[1]!=null)
			{
				Total_nongst=Total_nongst+(double)form[0]+(double)form[1];
			}
		}
		for(Object form[] : gstReport1Form.getIntraNonRegisterVATList())
		{
			if(form[0]!=null && form[1]!=null)
			{
				Total_nongst=Total_nongst+(double)form[0]+(double)form[1];
			}
		}
		
		headerexemp2.createCell(0).setCellValue("");
		headerexemp2.createCell(1).setCellValue(round(Total_nil,2).toString());
		headerexemp2.createCell(2).setCellValue(round(Total_nongst,2).toString());
		
		for(int i=0;i<=2;i++)
		{
			headerexemp2.getCell(i).setCellStyle(style1);
		}
		
        // create header row
		Row headerexemp= sheetexemp.createRow(rowCount++);
		headerexemp.createCell(0).setCellValue("Description");
		headerexemp.createCell(1).setCellValue("Nil Rated Supplies");
		headerexemp.createCell(2).setCellValue("Non GST Supplies");
		
		for(int i=0;i<=2;i++)
		{
			headerexemp.getCell(i).setCellStyle(style);
		}
		
		for(Object form[] : gstReport1Form.getInterRegisterList())
		{
			
			Row courseRow = sheetexemp.createRow(rowCount++);
			courseRow.createCell(0).setCellValue("Inter-State supplies to registered persons");
			if(form[0]!=null && form[1]!=null)
			{
			courseRow.createCell(1).setCellValue(commonService.round((double)form[0]+(double)form[1],2).toString());
			}
			else
			{
				courseRow.createCell(1).setCellValue("0");
			}
			courseRow.createCell(2).setCellValue("0");
			for(int i=0;i<=2;i++)
			{
				if(i==1||i==2)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		}
		
		for(Object form[] : gstReport1Form.getIntraRegisterList())
		{
			
			
			Row courseRow = sheetexemp.createRow(rowCount++);
			courseRow.createCell(0).setCellValue("Intra-State supplies to registered persons");
			if(form[0]!=null && form[1]!=null)
			{
			courseRow.createCell(1).setCellValue(commonService.round((double)form[0]+(double)form[1],2).toString());
			}
			else
			{
				courseRow.createCell(1).setCellValue("0");
			}
			courseRow.createCell(2).setCellValue("0");
		
			for(int i=0;i<=2;i++)
			{
				if(i==1||i==2)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		}
		
		for(Object form[] : gstReport1Form.getInterNonRegisterList())
		{
		
			Row courseRow = sheetexemp.createRow(rowCount++);
			courseRow.createCell(0).setCellValue("Inter-State supplies to unregistered persons");
			if(form[0]!=null && form[1]!=null)
			{
			courseRow.createCell(1).setCellValue(commonService.round((double)form[0]+(double)form[1],2).toString());
			}
			else
			{
				courseRow.createCell(1).setCellValue("0");
			}
			courseRow.createCell(2).setCellValue("0");
			for(int i=0;i<=2;i++)
			{
				if(i==1||i==2)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		}
		
		for(Object form[] : gstReport1Form.getIntraNonRegisterList())
		{
			Row courseRow = sheetexemp.createRow(rowCount++);
			courseRow.createCell(0).setCellValue("Intra-State supplies to unregistered persons");
			if(form[0]!=null && form[1]!=null)
			{
			courseRow.createCell(1).setCellValue(commonService.round((double)form[0]+(double)form[1],2).toString());
			}
			else
			{
				courseRow.createCell(1).setCellValue("0");
			}
			courseRow.createCell(2).setCellValue("0");
			for(int i=0;i<=2;i++)
			{
				if(i==1||i==2)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		}
	
		for(Object form[] : gstReport1Form.getInterRegisterVATList())
		{
			
			Row courseRow = sheetexemp.createRow(rowCount++);
			courseRow.createCell(0).setCellValue("Inter-State supplies to registered persons(Non-GST)");
			courseRow.createCell(1).setCellValue("0");
			if(form[0]!=null && form[1]!=null)
			{
			courseRow.createCell(2).setCellValue(commonService.round((double)form[0]+(double)form[1],2).toString());
			}
			else
			{
				courseRow.createCell(2).setCellValue("0");
			}
			for(int i=0;i<=2;i++)
			{
				if(i==1||i==2)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		}
		for(Object form[] : gstReport1Form.getIntraRegisterVATList())
		{
			
			Row courseRow = sheetexemp.createRow(rowCount++);
			courseRow.createCell(0).setCellValue("Intra-State supplies to registered persons(Non-GST)");
			courseRow.createCell(1).setCellValue("0");
			if(form[0]!=null && form[1]!=null)
			{
			courseRow.createCell(2).setCellValue(commonService.round((double)form[0]+(double)form[1],2).toString());
			}
			else
			{
				courseRow.createCell(2).setCellValue("0");
			}
			for(int i=0;i<=2;i++)
			{
				if(i==1||i==2)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		}
		for(Object form[] : gstReport1Form.getInterNonRegisterVATList())
				{
			Row courseRow = sheetexemp.createRow(rowCount++);
			courseRow.createCell(0).setCellValue("Inter-State supplies to unregistered persons(Non-GST)");
			courseRow.createCell(1).setCellValue("0");
			if(form[0]!=null && form[1]!=null)
			{
			courseRow.createCell(2).setCellValue(commonService.round((double)form[0]+(double)form[1],2).toString());
			}
			else
			{
				courseRow.createCell(2).setCellValue("0");
			}
			for(int i=0;i<=2;i++)
			{
				if(i==1||i==2)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
				}
		
		for(Object form[] : gstReport1Form.getIntraNonRegisterVATList())
		{
			Row courseRow = sheetexemp.createRow(rowCount++);
			courseRow.createCell(0).setCellValue("Intra-State supplies to unregistered persons(Non-GST)");
			courseRow.createCell(1).setCellValue("0");
			if(form[0]!=null && form[1]!=null)
			{
			courseRow.createCell(2).setCellValue(commonService.round((double)form[0]+(double)form[1],2).toString());
			}
			else
			{
				courseRow.createCell(2).setCellValue("0");
			}
			for(int i=0;i<=2;i++)
			{
				if(i==1||i==2)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		}
/******************************************* Sheet for HSN *********************************************/	
		

		Sheet sheethsn = workbook.createSheet("HSN");

		rowCount = 1;
		if(gstReport1Form.getCompany()!=null)
		{
			Row compname = sheethsn.createRow(0);
			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
			
		if(gstReport1Form.getCompany().getPermenant_address()!=null)
		{
			Row address = sheethsn.createRow(rowCount++);
			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
		
		}
		if(gstReport1Form.getCompany().getRegistration_no()!=null)
		{
			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Row cin = sheethsn.createRow(rowCount++);
				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
			}
		}
		}
		
		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
		{
			Row dates = sheethsn.createRow(rowCount++);
			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
		
		}
		
		Row emptyrow9 = sheethsn.createRow(rowCount++);
		emptyrow9.createCell(0).setCellValue("");
		
		Row headerhsn1 = sheethsn.createRow(rowCount++);
		headerhsn1.createCell(0).setCellValue("No. of HSN");
		headerhsn1.createCell(1).setCellValue("");
		headerhsn1.createCell(2).setCellValue("");
		headerhsn1.createCell(3).setCellValue("");
		headerhsn1.createCell(4).setCellValue("Total Value");
		headerhsn1.createCell(5).setCellValue("Total Taxable Value");
		headerhsn1.createCell(6).setCellValue("Total Integrated Tax");
		headerhsn1.createCell(7).setCellValue("Total Central Tax");
		headerhsn1.createCell(8).setCellValue("Total State/UT Tax");
		headerhsn1.createCell(9).setCellValue("Total Cess");
		
		for(int i=0;i<=9;i++)
		{
			headerhsn1.getCell(i).setCellStyle(style);
		}
		
		
		Row headerhsn2 = sheethsn.createRow(rowCount++);
		Double TotalValue= (double)0;
		Double TaxableValue= (double)0;
		Double IgstAmount = (double)0;
		Double CgstAmount= (double)0;
		Double SgstAmount= (double)0;
		Double CessAmount= (double)0;
		for(HSNReportForm reportForm : gstReport1Form.getHsnReportList()) {
			
			if(reportForm.getTotalValue()!=null)
			{
				TotalValue=TotalValue+reportForm.getTotalValue();
			}
	
			if(reportForm.getTaxableValue()!=null)
			{
				
				TaxableValue=TaxableValue+reportForm.getTaxableValue();
			}
			
			
			if(reportForm.getIgstAmount()!=null)
			{
				
				IgstAmount=IgstAmount+reportForm.getIgstAmount();
			}
			
			if(reportForm.getCgstAmount()!=null)
			{
				
				CgstAmount=CgstAmount+reportForm.getCgstAmount();
			}
		
			if(reportForm.getSgstAmount()!=null)
			{
				
				SgstAmount=SgstAmount+reportForm.getSgstAmount();
			}
			if(reportForm.getCessAmount()!=null)
			{
				CessAmount=CessAmount+reportForm.getCessAmount();
			}
		
		}
		
		headerhsn2.createCell(0).setCellValue(gstReport1Form.getHsnReportList().size());
		headerhsn2.createCell(1).setCellValue("");
		headerhsn2.createCell(2).setCellValue("");
		headerhsn2.createCell(3).setCellValue("");
		headerhsn2.createCell(4).setCellValue(round(TotalValue,2).toString());
		headerhsn2.createCell(5).setCellValue(round(TaxableValue,2).toString());
		headerhsn2.createCell(6).setCellValue(round(IgstAmount,2).toString());
		headerhsn2.createCell(7).setCellValue(round(CgstAmount,2).toString());
		headerhsn2.createCell(8).setCellValue(round(SgstAmount,2).toString());
		headerhsn2.createCell(9).setCellValue(round(CessAmount,2).toString());
		
		for(int i=0;i<=9;i++)
		{
			headerhsn2.getCell(i).setCellStyle(style1);
		}
		
		Row headerhsn = sheethsn.createRow(rowCount++);
		headerhsn.createCell(0).setCellValue("HSN");
		headerhsn.createCell(1).setCellValue("Description");
		headerhsn.createCell(2).setCellValue("UQC");
		headerhsn.createCell(3).setCellValue("Total Quantity");
		headerhsn.createCell(4).setCellValue("Total Value");
		headerhsn.createCell(5).setCellValue("Taxable Value");
		headerhsn.createCell(6).setCellValue("Integrated Tax Amount");
		headerhsn.createCell(7).setCellValue("Central Tax Amount");
		headerhsn.createCell(8).setCellValue("State/UT Tax Amount");
		headerhsn.createCell(9).setCellValue("Cess Amount");
		
		
		for(int i=0;i<=9;i++)
		{
			headerhsn.getCell(i).setCellStyle(style);
		}
		
		
		for(HSNReportForm reportForm : gstReport1Form.getHsnReportList()) {
			Row courseRow = sheethsn.createRow(rowCount++);
			if(reportForm.getHsnCode()!=null)
			{
			courseRow.createCell(0).setCellValue(reportForm.getHsnCode());
			}
			else
			{
				courseRow.createCell(0).setCellValue("");
			}
			courseRow.createCell(1).setCellValue("");
			
			if(reportForm.getUqc()!=null)
			{
				courseRow.createCell(2).setCellValue(reportForm.getUqc());
			}
			else
			{
				courseRow.createCell(2).setCellValue("");
			}
			
			if(reportForm.getTotalQuantity()!=null)
			{
				courseRow.createCell(3).setCellValue(round(reportForm.getTotalQuantity(), 2).toString());
			
			}
			else
			{
				courseRow.createCell(3).setCellValue("");
			}
			
			if(reportForm.getTotalValue()!=null)
			{
				courseRow.createCell(4).setCellValue(round(reportForm.getTotalValue(), 2).toString());
				
			}
			else
			{
				courseRow.createCell(4).setCellValue("");
			}
			
			if(reportForm.getTaxableValue()!=null)
			{
				courseRow.createCell(5).setCellValue(round(reportForm.getTaxableValue(), 2).toString());
				
			}
			else
			{
				courseRow.createCell(5).setCellValue("");
			}
			
			if(reportForm.getIgstAmount()!=null)
			{
				courseRow.createCell(6).setCellValue(round(reportForm.getIgstAmount(), 2).toString());
				
			}
			else
			{
				courseRow.createCell(6).setCellValue("");
			}
			if(reportForm.getCgstAmount()!=null)
			{
				courseRow.createCell(7).setCellValue(round(reportForm.getCgstAmount(), 2).toString());
				
			}
			else
			{
				courseRow.createCell(7).setCellValue("");
			}
			if(reportForm.getSgstAmount()!=null)
			{
				courseRow.createCell(8).setCellValue(round(reportForm.getSgstAmount(), 2).toString());
				
			}
			else
			{
				courseRow.createCell(8).setCellValue("");
			}
			
			if(reportForm.getCessAmount()!=null)
			{
				courseRow.createCell(9).setCellValue(round(reportForm.getCessAmount(), 2).toString());
				
			}
			else
			{
				courseRow.createCell(9).setCellValue("");
			}
			
			for(int i=0;i<=9;i++)
			{
				if(i==4||i==5||i==6||i==7||i==8||i==9)
				{
				courseRow.getCell(i).setCellStyle(style1);
				}
			}
		}
		
	
		
/******************************************* Sheet for docs *********************************************/
		Sheet sheetdocs = workbook.createSheet("docs");

		rowCount = 1;
		if(gstReport1Form.getCompany()!=null)
		{
			Row compname = sheetdocs.createRow(0);
			compname.createCell(0).setCellValue("Company Name:"+" "+gstReport1Form.getCompany().getCompany_name());
			
		if(gstReport1Form.getCompany().getPermenant_address()!=null)
		{
			Row address = sheetdocs.createRow(rowCount++);
			address.createCell(0).setCellValue("Address:"+" "+gstReport1Form.getCompany().getPermenant_address());
		
		}
		if(gstReport1Form.getCompany().getRegistration_no()!=null)
		{
			if(gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| gstReport1Form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Row cin = sheetdocs.createRow(rowCount++);
				cin.createCell(0).setCellValue("CIN NO:"+" "+gstReport1Form.getCompany().getRegistration_no());
			}
		}
		}
		
		if(gstReport1Form.getFromDate()!=null && gstReport1Form.getToDate()!=null)
		{
			Row dates = sheetdocs.createRow(rowCount++);
			dates.createCell(0).setCellValue("From"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(gstReport1Form.getToDate().toString()));
		
		}
		
		Row emptyrow10 = sheetdocs.createRow(rowCount++);
		emptyrow10.createCell(0).setCellValue("");
		
	    // create header row
		Row headerdocs1= sheetdocs.createRow(rowCount++);
		headerdocs1.createCell(0).setCellValue("");
		headerdocs1.createCell(1).setCellValue("");
		headerdocs1.createCell(2).setCellValue("");
		headerdocs1.createCell(3).setCellValue("Total Number");
		headerdocs1.createCell(4).setCellValue("Total Cancelled");
		
		for(int i=0;i<=4;i++)
		{
			headerdocs1.getCell(i).setCellStyle(style);
		}
		
		Row headerdocs2= sheetdocs.createRow(rowCount++);
		Integer totalnumber = 0 ;
		Integer totalcancel = 0 ;
		
		for(GSTdocsForm form : gstReport1Form.getOutwardSupplyList())
		{			
			totalnumber=totalnumber+form.getTotalNumber();
			totalcancel=totalcancel+form.getTotalCancel();
			
		}
		for(GSTdocsForm form : gstReport1Form.getInwardSupplyList())
		{			
			totalnumber=totalnumber+form.getTotalNumber();
			totalcancel=totalcancel+form.getTotalCancel();
		}
		
		for(GSTdocsForm form : gstReport1Form.getCreditNoteList())
		{			
			totalnumber=totalnumber+form.getTotalNumber();
			totalcancel=totalcancel+form.getTotalCancel();
		}
		
		for(GSTdocsForm form : gstReport1Form.getDebitNoteList())
		{			
			totalnumber=totalnumber+form.getTotalNumber();
			totalcancel=totalcancel+form.getTotalCancel();
		}
		
		headerdocs2.createCell(0).setCellValue("");
		headerdocs2.createCell(1).setCellValue("");
		headerdocs2.createCell(2).setCellValue("");
		headerdocs2.createCell(3).setCellValue(totalnumber);
		headerdocs2.createCell(4).setCellValue(totalcancel);
		
		
		Row headerdocs= sheetdocs.createRow(rowCount++);
		headerdocs.createCell(0).setCellValue("Nature  of Document");
		headerdocs.createCell(1).setCellValue("Sr. No. From");
		headerdocs.createCell(2).setCellValue("Sr. No. To");
		headerdocs.createCell(3).setCellValue("Total Number");
		headerdocs.createCell(4).setCellValue("Cancelled");
		
		for(int i=0;i<=4;i++)
		{
			headerdocs.getCell(i).setCellStyle(style);
		}
		
		for(GSTdocsForm form : gstReport1Form.getOutwardSupplyList())
		{			
			Row courseRow = sheetdocs.createRow(rowCount++);
			courseRow.createCell(0).setCellValue(form.getNatureOfdocument());
			courseRow.createCell(1).setCellValue(form.getSrnofrom());
			courseRow.createCell(2).setCellValue(form.getSrnoto());
			courseRow.createCell(3).setCellValue(form.getTotalNumber().toString());
			courseRow.createCell(4).setCellValue(form.getTotalCancel().toString());
		}
		for(GSTdocsForm form : gstReport1Form.getInwardSupplyList())
		{			
			Row courseRow = sheetdocs.createRow(rowCount++);
			courseRow.createCell(0).setCellValue(form.getNatureOfdocument());
			courseRow.createCell(1).setCellValue(form.getSrnofrom());
			courseRow.createCell(2).setCellValue(form.getSrnoto());
			courseRow.createCell(3).setCellValue(form.getTotalNumber().toString());
			courseRow.createCell(4).setCellValue(form.getTotalCancel().toString());
		}
		for(GSTdocsForm form : gstReport1Form.getCreditNoteList())
		{			
			Row courseRow = sheetdocs.createRow(rowCount++);
			courseRow.createCell(0).setCellValue(form.getNatureOfdocument());
			courseRow.createCell(1).setCellValue(form.getSrnofrom());
			courseRow.createCell(2).setCellValue(form.getSrnoto());
			courseRow.createCell(3).setCellValue(form.getTotalNumber().toString());
			courseRow.createCell(4).setCellValue(form.getTotalCancel().toString());
		}
		for(GSTdocsForm form : gstReport1Form.getDebitNoteList())
		{			
			Row courseRow = sheetdocs.createRow(rowCount++);
			courseRow.createCell(0).setCellValue(form.getNatureOfdocument());
			courseRow.createCell(1).setCellValue(form.getSrnofrom());
			courseRow.createCell(2).setCellValue(form.getSrnoto());
			courseRow.createCell(3).setCellValue(form.getTotalNumber().toString());
			courseRow.createCell(4).setCellValue(form.getTotalCancel().toString());
		}
	
	}
	
	
	
	
	public String round(Double debit_balance, int decimalPlace) {
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(decimalPlace);
		return df.format(debit_balance);
	}
}
