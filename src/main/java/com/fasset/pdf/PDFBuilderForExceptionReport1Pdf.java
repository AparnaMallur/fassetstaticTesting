package com.fasset.pdf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.form.ExceptionReport1Form;
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

public class PDFBuilderForExceptionReport1Pdf extends PDFBuilderForExceptionReport1PdfView{

	private CommonServiceImpl commonService = new CommonServiceImpl();
	
	public void buildPdfDocumentForQuotation(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response, List<ExceptionReport1Form> exceptionReport1FormsList,LocalDate fromdate,LocalDate todate,Company company) throws Exception {

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
		
		Paragraph para = new Paragraph(new Phrase("Cash payment and receipt of more Rs.10000/- and above."));
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

		cell.setPhrase(new Phrase("Customer Name/Supplier Name", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Voucher No", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Voucher Type", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Payment Type", font));
		table.addCell(cell);
	
		cell.setPhrase(new Phrase("Total Amount", font));
		table.addCell(cell);
		
		for (ExceptionReport1Form entry : exceptionReport1FormsList) 
		{
			if (entry.getDate() != null) {
				table.addCell(ClasstoConvertDateformat.dateFormat(entry.getDate() .toString()));
			} else {
				table.addCell("");
			}
			if (entry.getCustomer() != null) {
				table.addCell(entry.getCustomer());
			}
			else if (entry.getSuppliers() != null) {
				table.addCell(entry.getSuppliers());
			}
			else {
				table.addCell("");
			}
			
			if(entry.getVoucher_Number()!=null) {
				table.addCell(entry.getVoucher_Number());
			}
			else {
				table.addCell("");
			}
			if(entry.getVoucher_Type()!=null) {
				table.addCell(entry.getVoucher_Type());
			}
			else {
				table.addCell("");
			}
			if(entry.getPaymentType()!=null) {
				table.addCell(entry.getPaymentType());
			}
			else {
				table.addCell("");
			}
			if(entry.getTotalAmount()!=null) {
				table.addCell((commonService.round(entry.getTotalAmount(), 2).toString()));
			}
			else {
				table.addCell("");
			}
			
		}
		document.add(table);
	}
}
