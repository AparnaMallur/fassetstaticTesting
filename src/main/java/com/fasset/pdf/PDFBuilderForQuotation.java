package com.fasset.pdf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.form.QuotationForm;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@SessionAttributes("user")
public class PDFBuilderForQuotation extends QuotationPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

	}

	public void buildPdfDocumentForQuotation(Map<String, Object> model, Document doc, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response, QuotationForm form) throws Exception {

		List<QuotationDetails> quoteDetails = form.getQuoteDetails();
		Quotation quotation = form.getQuotation();

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] { 3.0f, 5.0f, 5.0f, 3.0f, 5.0f });
		table.setSpacingBefore(10);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLACK);
		cell.setPadding(5);

		cell.setPhrase(new Phrase("Quotation No:", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Name:", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Email:", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Mobile:", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Company Name:", font));
		table.addCell(cell);

		if (quotation != null) {
			table.addCell(quotation.getQuotation_no());
			table.addCell(quotation.getFirst_name() + quotation.getLast_name());
			table.addCell(quotation.getEmail());
			table.addCell(quotation.getMobile_no());
			table.addCell(quotation.getCompany_name());
			table.addCell(quotation.getStatus().toString());
		}

		PdfPTable table1 = new PdfPTable(3);
		table1.setWidthPercentage(100.0f);
		table1.setWidths(new float[] { 3.0f, 3.0f, 3.0f });
		table1.setSpacingBefore(10);

		cell.setPhrase(new Phrase("Service", font));
		table1.addCell(cell);

		cell.setPhrase(new Phrase("Frequency", font));
		table1.addCell(cell);

		cell.setPhrase(new Phrase("Amount", font));
		table1.addCell(cell);

		if (quoteDetails != null && quoteDetails.size() != 0) {
			for (QuotationDetails detail : quoteDetails) {
				table1.addCell(detail.getService_id().getService_name());
				table1.addCell(detail.getFrequency_id().getFrequency_name());
				table1.addCell(detail.getAmount().toString());
			}
		}
		doc.add(table);
		doc.add(table1);

	}
}