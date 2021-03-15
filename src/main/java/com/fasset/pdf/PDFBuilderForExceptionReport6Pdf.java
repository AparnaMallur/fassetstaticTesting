package com.fasset.pdf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.QuotationForm;
import com.fasset.service.CommonServiceImpl;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFBuilderForExceptionReport6Pdf extends PDFBuilderForExceptionReport6PdfView{
	private CommonServiceImpl commonService = new CommonServiceImpl();
	public void buildPdfDocumentForQuotation(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response, List<OpeningBalances> subledgerOPBalanceList,List<OpeningBalancesForm> subledgerOpenBalanceList,Long subId,LocalDate fromdate,LocalDate todate,Company company) throws Exception {

		document.setPageSize(PageSize.A4);
		document.open();
		if(company!=null)
		{
			Paragraph companyName = new Paragraph(new Phrase("Company Name:"+" "+company.getCompany_name()));
			companyName.setAlignment(Paragraph.ALIGN_LEFT);
			document.add(companyName);
			
			
		if(company.getPermenant_address()!=null)
		{
			
			Paragraph address = new Paragraph(new Phrase("Address:"+" "+company.getPermenant_address()));
			address.setAlignment(Paragraph.ALIGN_LEFT);
			document.add(address);
			
			
		}
		if(company.getRegistration_no()!=null)
		{
			if(company.getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| company.getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Paragraph cinNo = new Paragraph(new Phrase("CIN NO:"+" "+company.getRegistration_no()));
				cinNo.setAlignment(Paragraph.ALIGN_LEFT);
				document.add(cinNo);
			}
		}
		}
		
		Paragraph para = new Paragraph(new Phrase("Cash in hand account should technically have debit or zero balance"));
		para.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(para);
		
		
		if(fromdate!=null && todate!=null)
		{
			Paragraph date = new Paragraph(new Phrase("From"+" "+ClasstoConvertDateformat.dateFormat(fromdate.toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(todate.toString())));
			date.setAlignment(Paragraph.ALIGN_LEFT);
			document.add(date);
		}
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100.0f);
		table.setSpacingBefore(10);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);

		Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLACK);
		cell.setPadding(5);

		cell.setPhrase(new Phrase("Date", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Voucher Number", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Voucher Type", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Amount", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Payment Type", font));
		table.addCell(cell);
	
		cell.setPhrase(new Phrase("Balance", font));
		table.addCell(cell);
		
		Double credit = 0.0d;
		Double debit = 0.0d;
		Double row_running = 0.0d;
		for(OpeningBalancesForm form :subledgerOpenBalanceList)
		{
			if(form.getSub_id().equals(subId))
			{
				credit=credit+form.getCredit_balance();
				debit=debit+form.getDebit_balance();
			}
		}
		row_running=row_running+(debit-credit);
		
		for(OpeningBalances form :subledgerOPBalanceList)
		{
			if(form.getSales()!=null)
			{
				row_running=row_running+(form.getDebit_balance()-form.getCredit_balance());
				if(row_running<0)
				{
					if (form.getSales().getCreated_date() != null) {
						table.addCell(ClasstoConvertDateformat.dateFormat(form.getSales().getCreated_date() .toString()));
					} else {
						table.addCell("");
					}
					
					if(form.getSales().getVoucher_no()!=null) {
						table.addCell(form.getSales().getVoucher_no());
					}
					else {
						table.addCell("");
					}
					table.addCell("Sales");
					if(form.getCredit_balance()!=0)
					{
						table.addCell((commonService.round(form.getCredit_balance(), 2).toString()));
					}
					if(form.getDebit_balance()!=0)
					{
						table.addCell((commonService.round(form.getDebit_balance(), 2).toString()));
					}
					table.addCell((commonService.round(row_running, 2).toString()));
				}
			}
			if(form.getReceipt()!=null)
			{
				row_running=row_running+(form.getDebit_balance()-form.getCredit_balance());
				if(row_running<0)
				{
					if (form.getReceipt().getDate() != null) {
						table.addCell(ClasstoConvertDateformat.dateFormat(form.getReceipt().getDate().toString()));
					} else {
						table.addCell("");
					}
					
					if(form.getReceipt().getVoucher_no()!=null) {
						table.addCell(form.getReceipt().getVoucher_no());
					}
					else {
						table.addCell("");
					}
					table.addCell("Receipt");
					if(form.getCredit_balance()!=0)
					{
						table.addCell((commonService.round(form.getCredit_balance(), 2).toString()));
					}
					if(form.getDebit_balance()!=0)
					{
						table.addCell((commonService.round(form.getDebit_balance(), 2).toString()));
					}
					table.addCell((commonService.round(row_running, 2).toString()));
				}
			}
			if(form.getCredit()!=null)
			{
				row_running=row_running+(form.getDebit_balance()-form.getCredit_balance());
				if(row_running<0)
				{
					if (form.getCredit().getDate() != null) {
						table.addCell(ClasstoConvertDateformat.dateFormat(form.getCredit().getDate().toString()));
					} else {
						table.addCell("");
					}
					
					if(form.getCredit().getVoucher_no()!=null) {
						table.addCell(form.getCredit().getVoucher_no());
					}
					else {
						table.addCell("");
					}
					table.addCell("Credit Note");
					if(form.getCredit_balance()!=0)
					{
						table.addCell((commonService.round(form.getCredit_balance(), 2).toString()));
					}
					if(form.getDebit_balance()!=0)
					{
						table.addCell((commonService.round(form.getDebit_balance(), 2).toString()));
					}
					table.addCell((commonService.round(row_running, 2).toString()));
				}
			}
			if(form.getPurchase()!=null)
			{
				row_running=row_running+(form.getDebit_balance()-form.getCredit_balance());
				if(row_running<0)
				{
					if (form.getPurchase().getSupplier_bill_date() != null) {
						table.addCell(ClasstoConvertDateformat.dateFormat(form.getPurchase().getSupplier_bill_date().toString()));
					} else {
						table.addCell("");
					}
					
					if(form.getPurchase().getVoucher_no()!=null) {
						table.addCell(form.getPurchase().getVoucher_no());
					}
					else {
						table.addCell("");
					}
					table.addCell("Purchase");
					if(form.getCredit_balance()!=0)
					{
						table.addCell((commonService.round(form.getCredit_balance(), 2).toString()));
					}
					if(form.getDebit_balance()!=0)
					{
						table.addCell((commonService.round(form.getDebit_balance(), 2).toString()));
					}
					table.addCell((commonService.round(row_running, 2).toString()));
				}
			}
			
			if(form.getPayment()!=null)
			{
				row_running=row_running+(form.getDebit_balance()-form.getCredit_balance());
				if(row_running<0)
				{
					if (form.getPayment().getDate() != null) {
						table.addCell(ClasstoConvertDateformat.dateFormat(form.getPayment().getDate().toString()));
					} else {
						table.addCell("");
					}
					
					if(form.getPayment().getVoucher_no()!=null) {
						table.addCell(form.getPayment().getVoucher_no());
					}
					else {
						table.addCell("");
					}
					table.addCell("Payment");
					if(form.getCredit_balance()!=0)
					{
						table.addCell((commonService.round(form.getCredit_balance(), 2).toString()));
					}
					if(form.getDebit_balance()!=0)
					{
						table.addCell((commonService.round(form.getDebit_balance(), 2).toString()));
					}
					table.addCell((commonService.round(row_running, 2).toString()));
				}
			}
			
			if(form.getDebit()!=null)
			{
				row_running=row_running+(form.getDebit_balance()-form.getCredit_balance());
				if(row_running<0)
				{
					if (form.getDebit().getDate() != null) {
						table.addCell(ClasstoConvertDateformat.dateFormat(form.getDebit().getDate().toString()));
					} else {
						table.addCell("");
					}
					
					if(form.getDebit().getVoucher_no()!=null) {
						table.addCell(form.getDebit().getVoucher_no());
					}
					else {
						table.addCell("");
					}
					table.addCell("Debit");
					if(form.getCredit_balance()!=0)
					{
						table.addCell((commonService.round(form.getCredit_balance(), 2).toString()));
					}
					if(form.getDebit_balance()!=0)
					{
						table.addCell((commonService.round(form.getDebit_balance(), 2).toString()));
					}
					table.addCell((commonService.round(row_running, 2).toString()));
				}
			}
			if(form.getContra()!=null)
			{
				row_running=row_running+(form.getDebit_balance()-form.getCredit_balance());
				if(row_running<0)
				{
					if (form.getContra().getDate() != null) {
						table.addCell(ClasstoConvertDateformat.dateFormat(form.getContra().getDate().toString()));
					} else {
						table.addCell("");
					}
					
					if(form.getContra().getVoucher_no()!=null) {
						table.addCell(form.getContra().getVoucher_no());
					}
					else {
						table.addCell("");
					}
					table.addCell("Contra");
					if(form.getCredit_balance()!=0)
					{
						table.addCell((commonService.round(form.getCredit_balance(), 2).toString()));
					}
					if(form.getDebit_balance()!=0)
					{
						table.addCell((commonService.round(form.getDebit_balance(), 2).toString()));
					}
					table.addCell((commonService.round(row_running, 2).toString()));
				}
			}
		}
		document.add(table);
	}
}
