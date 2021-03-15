package com.fasset.pdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Contra;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFBuilderForPayrollAutoJV extends AbstractITextPdfView {
	
	@Override
    protected void buildPdfDocument(Map<String, Object> model, Document doc,
        PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
      	//doc.setPageSize(new Rectangle(3440, 3440));
        doc.setPageSize(PageSize.A4);
        doc.open();
    	response.setHeader("Content-Disposition", "attachment; filename=\"Payroll Autop JV.pdf\"");		
		Contra contra = (Contra) model.get("contra");
		
		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
		Font regularFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);	
		
		Paragraph compName = new Paragraph(new Phrase(contra.getCompany().getCompany_name(),boldFont));
		compName.setAlignment(Paragraph.ALIGN_CENTER);
		doc.add(compName);
		
		Paragraph address = new Paragraph(new Phrase(contra.getCompany().getCurrent_address(),regularFont));
		address.setAlignment(Paragraph.ALIGN_CENTER);
		doc.add(address);
		
		Paragraph state = new Paragraph(new Phrase("State : "+contra.getCompany().getState().getState_name(),regularFont));
		state.setAlignment(Paragraph.ALIGN_CENTER);
		doc.add(state);
		
		Paragraph para = new Paragraph("Contra Voucher", regularFont);
		para.setAlignment(Paragraph.ALIGN_CENTER);
		doc.add(para);
		
		PdfPTable mainTable = new PdfPTable(3);
		mainTable.setWidthPercentage(70.0f);
		mainTable.setWidths(new float[] { 40.0f, 15.0f, 15.0f });
		mainTable.setSpacingBefore(10);	
		
		PdfPCell voucherNo = new PdfPCell(new Phrase("No. "+contra.getVoucher_no(), boldFont));
		voucherNo.setBorder(0);
		mainTable.addCell(voucherNo);
		
		PdfPCell date = new PdfPCell(new Phrase("Date : "+ClasstoConvertDateformat.dateFormat(contra.getDate().toString()), regularFont));
		date.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		date.setBorder(0);
		date.setColspan(2);
		mainTable.addCell(date);
		String narrationtext="";

		PdfPCell header = new PdfPCell();
		header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		
		header.setPhrase(new Phrase("Particulars", regularFont));
		mainTable.addCell(header);
		
		header.setPhrase(new Phrase("Debit", regularFont));
		mainTable.addCell(header);
		
		header.setPhrase(new Phrase("Credit", regularFont));
		mainTable.addCell(header);
		
		PdfPTable particulars = new PdfPTable(3);
		particulars.setWidthPercentage(100.0f);
		particulars.setWidths(new float[] { 20.0f, 70.0f, 10.0f });
		particulars.getDefaultCell().setBorder(0);
		
		if(contra.getType() == MyAbstractController.CONTRA_DEPOSITE) {
			particulars.addCell("To");
			particulars.addCell(contra.getDeposite_to().getBank_name()+" "+contra.getDeposite_to().getAccount_no());
			particulars.completeRow();
			
			particulars.addCell(" ");
			particulars.addCell("Cash In Hand");
			
			PdfPCell dr = new PdfPCell(new Phrase("Cr"));
			dr.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			dr.setBorder(0);
			particulars.addCell(dr);
			
			mainTable.addCell(particulars);
			
			PdfPCell debit = new PdfPCell(new Phrase(contra.getAmount().toString()));
			debit.setVerticalAlignment(PdfPCell.ALIGN_TOP);
			debit.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			mainTable.addCell(debit);
			
			PdfPCell credit = new PdfPCell(new Phrase(contra.getAmount().toString()));
			credit.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
			credit.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			mainTable.addCell(credit);
			narrationtext+="Being cash Deposited into "+contra.getDeposite_to().getBank_name()+" "+contra.getDeposite_to().getAccount_no()+".";
			
		}
		
		if(contra.getType() == MyAbstractController.CONTRA_WITHDRAW) {
			particulars.addCell("From");
			particulars.addCell(contra.getWithdraw_from().getBank_name()+" - "+contra.getWithdraw_from().getAccount_no());
			particulars.completeRow();
			
			particulars.addCell(" ");
			particulars.addCell("Cash In Hand");
			
			PdfPCell dr = new PdfPCell(new Phrase("Dr"));
			dr.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			dr.setBorder(0);
			particulars.addCell(dr);
			
			mainTable.addCell(particulars);
			PdfPCell debit = new PdfPCell(new Phrase(contra.getAmount().toString()));
			debit.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
			debit.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			mainTable.addCell(debit);
			PdfPCell credit = new PdfPCell(new Phrase(contra.getAmount().toString()));
			credit.setVerticalAlignment(PdfPCell.ALIGN_TOP);
			credit.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			mainTable.addCell(credit);	
			narrationtext+="Being cash Withdrawn into "+contra.getWithdraw_from().getBank_name()+" - "+contra.getWithdraw_from().getAccount_no()+".";

		}
		
		if(contra.getType() == MyAbstractController.CONTRA_TRANSFER) {
			particulars.addCell("To");
			particulars.addCell(contra.getDeposite_to().getBank_name()+" "+contra.getDeposite_to().getAccount_no());
			particulars.completeRow();
			
			particulars.addCell("From ");
			particulars.addCell(contra.getWithdraw_from().getBank_name()+" "+contra.getWithdraw_from().getAccount_no());
			particulars.completeRow();

			mainTable.addCell(particulars);
			mainTable.addCell(contra.getAmount().toString());
			PdfPCell credit = new PdfPCell(new Phrase(contra.getAmount().toString()));
			credit.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
			credit.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			mainTable.addCell(credit);		
			narrationtext+="Being amount transferred to "+contra.getDeposite_to().getBank_name()+" - "+contra.getDeposite_to().getAccount_no()+" from "+contra.getWithdraw_from().getBank_name()+" - "+contra.getWithdraw_from().getAccount_no()+".";
	
		}
		
		particulars.deleteBodyRows();
		
		PdfPCell account = new PdfPCell(/*new Phrase("On Account of :", boldFont)*/);
		account.setColspan(3);
		account.setBorder(0);
		particulars.addCell(account);
		
		PdfPCell no = new PdfPCell(/*new Phrase("CH. NO. 450179")*/);
		no.setColspan(3);
		no.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		no.setBorder(0);
		particulars.addCell(no);
		
		particulars.setSpacingBefore(150);
		mainTable.addCell(particulars);
		mainTable.completeRow();
		
		mainTable.addCell("");
		PdfPCell amount = new PdfPCell();
		amount.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		
		amount.setPhrase(new Phrase(contra.getAmount().toString()));
		mainTable.addCell(amount);
		mainTable.addCell(amount);
		
		PdfPTable signTable = new PdfPTable(2);
		signTable.setWidthPercentage(60.0f);
		signTable.setWidths(new float[] { 50.0f,50.0f});
		signTable.setSpacingBefore(15);
		signTable.getDefaultCell().setBorder(0);
		
		PdfPCell narration = new PdfPCell(new Phrase("Narration: "+ narrationtext,regularFont));
		narration.setColspan(2);
		narration.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		narration.setBorder(0);
		narration.setPaddingBottom(15);
		signTable.addCell(narration);				
		
			
		PdfPCell receiver = new PdfPCell(new Phrase(" ", regularFont));
		receiver.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		receiver.setBorder(0);
		signTable.addCell(receiver);				
		
		PdfPCell author = new PdfPCell(new Phrase("Authorised Signatory :", regularFont));
		author.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		author.setBorder(0);
		signTable.addCell(author);
			
		doc.add(mainTable);
		doc.add(signTable);
	}
}
