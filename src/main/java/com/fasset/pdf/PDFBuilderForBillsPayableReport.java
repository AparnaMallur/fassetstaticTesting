package com.fasset.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasset.entities.Company;
import com.fasset.form.BillsPayableReportForm;
import com.fasset.form.BillsReceivableForm;
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
import com.lowagie.text.pdf.PdfCell;

public class PDFBuilderForBillsPayableReport extends AbstractITextPdfView{

	
	private CommonServiceImpl commonService = new CommonServiceImpl();
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		response.setHeader("Content-Disposition", "attachment; filename=\"Bills Payable Report.pdf\"");
		BillsPayableReportForm form =(BillsPayableReportForm) model.get("form");
		
		List<BillsReceivableForm> billsPayable = form.getBillsPayable();
		List<Company> companyList = new ArrayList<Company>();

		long option=form.getOption();
		document.setPageSize(PageSize.A3);
		document.open();
		if(form.getCompany()!=null)
		{
			Paragraph companyName = new Paragraph(new Phrase("Company Name:"+" "+form.getCompany().getCompany_name()));
			companyName.setAlignment(Paragraph.ALIGN_LEFT);
			document.add(companyName);
			
			
		if(form.getCompany().getPermenant_address()!=null)
		{
			
			Paragraph address = new Paragraph(new Phrase("Address:"+" "+form.getCompany().getPermenant_address()));
			address.setAlignment(Paragraph.ALIGN_LEFT);
			document.add(address);
			
			
		}
		if(form.getCompany().getRegistration_no()!=null)
		{
			if(form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				Paragraph cinNo = new Paragraph(new Phrase("CIN NO:"+" "+form.getCompany().getRegistration_no()));
				cinNo.setAlignment(Paragraph.ALIGN_LEFT);
				document.add(cinNo);
			}
		}
		}
		
		Paragraph para = new Paragraph(new Phrase("Bills Payable Report"));
		para.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(para);
		
		
		if(form.getFromDate()!=null && form.getToDate()!=null)
		{
			
			Paragraph date = new Paragraph(new Phrase("From"+" "+ClasstoConvertDateformat.dateFormat(form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(form.getToDate().toString())));
			date.setAlignment(Paragraph.ALIGN_LEFT);
			document.add(date);
		}

		if(option>0)
		{
			// define font for table header row
			//for single entry 5 column
			//column size is 5
			PdfPTable table = new PdfPTable(5);
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

			cell.setPhrase(new Phrase("Particulars", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Party Name", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Pending Amount", font));
			table.addCell(cell);

			
			double Tamount=(double)0;
			for(BillsReceivableForm entry:billsPayable) {
				if(entry!=null)
				{
					if (entry.getCreated_date() != null) {
						table.addCell(ClasstoConvertDateformat.dateFormat(entry.getCreated_date().toString()));
					} else {
						table.addCell("");
					}
					table.addCell(entry.getVoucher_no());
					table.addCell(entry.getParticulars());
					table.addCell(entry.getSupplier().getCompany_name());
					table.addCell((commonService.round(entry.getRound_off(), 2).toString()));
					Tamount=Tamount+entry.getRound_off();
					
				}
				
			}
			table.addCell("");
			table.addCell("");
			table.addCell("");
			
			table.addCell(new Phrase("Total",boldFont));
			table.addCell((commonService.round(Tamount, 2).toString()));
			document.add(table);
	}
	else if(option== 0)
		{
			//column size 2
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100.0f);
			table.setWidths(new float[] { 5.0f, 5.0f });
			table.setSpacingBefore(10);

			Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

			// define font for table header row
			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(BaseColor.WHITE);

			// define table header cell
			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(BaseColor.BLACK);
			cell.setPadding(0);

			PdfPCell header1 = new PdfPCell(new Phrase("Bills Payable Report",boldFont));
			header1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			header1.setColspan(3);
			table.addCell(header1);
			
			cell.setPhrase(new Phrase("Party Name", font));
			cell.setHorizontalAlignment(PdfCell.ALIGN_CENTER);
			table.addCell(cell);

			cell.setPhrase(new Phrase("Pending Amount", font));
			cell.setHorizontalAlignment(PdfCell.ALIGN_CENTER);
			table.addCell(cell);

			double Tamount=(double)0;
			for(BillsReceivableForm entry:billsPayable) {
					table.addCell(entry.getSupplier().getCompany_name());
					table.addCell((commonService.round(entry.getRound_off(), 2).toString()));
					Tamount=Tamount+entry.getRound_off();
				
			}
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell(new Phrase("Total",boldFont));
			table.addCell((commonService.round(Tamount, 2).toString()));
			document.add(table);

		}
	}

}
