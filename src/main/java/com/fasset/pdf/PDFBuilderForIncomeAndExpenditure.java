package com.fasset.pdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasset.form.IncomeAndExpenditureForm;
import com.fasset.form.IncomeAndExpenditureForm.IncomeOrExpense;
import com.fasset.form.IncomeAndExpenditureForm.IncomeOrExpense.Details;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFBuilderForIncomeAndExpenditure extends AbstractITextPdfView {
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"Income And Expenditure Report.pdf\"");
		IncomeAndExpenditureForm incomeExpenditureReport = (IncomeAndExpenditureForm) model.get("form");
		Paragraph par = new Paragraph("Income And Expenditure Report");
		par.setAlignment(Paragraph.ALIGN_CENTER);

		document.setPageSize(PageSize.A4);
		document.open();
		if(incomeExpenditureReport.getCompany()!=null)
		{
			document.add(new Paragraph("Company Name:"+" "+incomeExpenditureReport.getCompany().getCompany_name()));
		if(incomeExpenditureReport.getCompany().getPermenant_address()!=null)
		{
			document.add(new Paragraph("Address:"+" "+incomeExpenditureReport.getCompany().getPermenant_address()));
		}
		if(incomeExpenditureReport.getCompany().getRegistration_no()!=null)
		{
			if(incomeExpenditureReport.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| incomeExpenditureReport.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				document.add(new Paragraph("CIN NO:"+" "+incomeExpenditureReport.getCompany().getRegistration_no()));
			}
		}
		}
		document.add(par);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100.0f);
		table.setWidths(new float[] { 6.0f, 6.0f });
		table.setSpacingBefore(10);

		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		font.setSize(12);

		Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLACK);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell dataCell = new PdfPCell();
		dataCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

		cell.setPhrase(new Phrase("Expenditure", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Income", font));
		table.addCell(cell);

		PdfPTable tblExpense = new PdfPTable(3);
		tblExpense.setWidthPercentage(100.0f);
		tblExpense.setWidths(new float[] { 3.0f, 3.0f, 3.0f });
		tblExpense.setSpacingBefore(0);
		tblExpense.getDefaultCell().setBorder(1);

		cell.setPhrase(new Phrase("Particular", font));
		tblExpense.addCell(cell);

		cell.setPhrase(new Phrase("", font));
		tblExpense.addCell(cell);

		cell.setPhrase(new Phrase("Amount", font));
		tblExpense.addCell(cell);

		for (IncomeOrExpense expense : incomeExpenditureReport.getExpenses()) {

			if (expense.getGroup() != null) {
				tblExpense.addCell(expense.getGroup().getGroup_name());
			} else {
				tblExpense.addCell("");
			}

			tblExpense.addCell("");

			if (expense.getTotalAmount() != null) {
				dataCell.setPhrase(new Phrase(expense.getTotalAmount().toString()));
				tblExpense.addCell(dataCell);
			} else {
				tblExpense.addCell("");
			}

			for (Details detail : expense.getList()) {

				if (detail.getSubLedger() != null) {
					tblExpense.addCell(detail.getSubLedger().getSubledger_name());
				} else {
					tblExpense.addCell("");
				}

				if (detail.getAmount() != null) {
					dataCell.setPhrase(new Phrase(detail.getAmount().toString()));
					tblExpense.addCell(dataCell);
				} else {
					tblExpense.addCell("");
				}

				tblExpense.addCell("");
			}
			tblExpense.addCell("");
			tblExpense.addCell("");
			tblExpense.addCell("");
		}
		if (incomeExpenditureReport.getSurplus() != null) {
			tblExpense.addCell("Surplus");
			tblExpense.addCell("");
			if (incomeExpenditureReport.getSurplus() != null) {
				dataCell.setPhrase(new Phrase(incomeExpenditureReport.getSurplus().toString()));
				tblExpense.addCell(dataCell);
			} else {
				tblExpense.addCell("");
			}

		}

		PdfPTable tblIncome = new PdfPTable(3);
		tblIncome.setWidthPercentage(100.0f);
		tblIncome.setWidths(new float[] { 3.0f, 3.0f, 3.0f });
		tblIncome.setSpacingBefore(0);
		tblIncome.getDefaultCell().setBorder(1);

		cell.setPhrase(new Phrase("Particular", font));
		tblIncome.addCell(cell);

		cell.setPhrase(new Phrase("", font));
		tblIncome.addCell(cell);

		cell.setPhrase(new Phrase("Amount", font));
		tblIncome.addCell(cell);

		for (IncomeOrExpense income : incomeExpenditureReport.getIncomes()) {

			if (income.getGroup() != null) {
				tblIncome.addCell(income.getGroup().getGroup_name());
			} else {
				tblIncome.addCell("");
			}

			tblIncome.addCell("");

			if (income.getTotalAmount() != null) {
				dataCell.setPhrase(new Phrase(income.getTotalAmount().toString()));
				tblIncome.addCell(dataCell);
			} else {
				tblIncome.addCell("");
			}

			for (Details detail : income.getList()) {

				if (detail.getSubLedger() != null) {
					tblIncome.addCell(detail.getSubLedger().getSubledger_name());
				} else {
					tblIncome.addCell("");
				}

				if (detail.getAmount() != null) {
					dataCell.setPhrase(new Phrase(detail.getAmount().toString()));
					tblIncome.addCell(dataCell);
				} else {
					tblIncome.addCell("");
				}

				tblIncome.addCell("");
			}
			tblIncome.addCell("");
			tblIncome.addCell("");
			tblIncome.addCell("");
		}
		if (incomeExpenditureReport.getDeficit() != null) {
			tblIncome.addCell("Deficit");
			tblIncome.addCell("");
			dataCell.setPhrase(new Phrase(incomeExpenditureReport.getDeficit().toString()));
			tblIncome.addCell(dataCell);
		}

		table.addCell(tblExpense);
		table.addCell(tblIncome);

		PdfPTable tblExpensetotal = new PdfPTable(3);
		tblExpensetotal.setWidthPercentage(100.0f);
		tblExpensetotal.setWidths(new float[] { 3.0f, 3.0f, 3.0f });
		tblExpensetotal.setSpacingBefore(0);
		tblExpensetotal.getDefaultCell().setBorder(1);
		tblExpensetotal.addCell("Total : ");
		tblExpensetotal.addCell("");

		if (incomeExpenditureReport.getTotalExpense() != null) {
			dataCell.setPhrase(new Phrase(incomeExpenditureReport.getTotalExpense().toString(), boldFont));
			tblExpensetotal.addCell(dataCell);
		} else {
			tblExpensetotal.addCell("");
		}

		PdfPTable tblIncometotal = new PdfPTable(3);
		tblIncometotal.setWidthPercentage(100.0f);
		tblIncometotal.setWidths(new float[] { 3.0f, 3.0f, 3.0f });
		tblIncometotal.setSpacingBefore(0);
		tblIncometotal.getDefaultCell().setBorder(1);
		tblIncometotal.addCell("Total : ");
		tblIncometotal.addCell("");

		if (incomeExpenditureReport.getTotalIncome() != null) {
			dataCell.setPhrase(new Phrase(incomeExpenditureReport.getTotalIncome().toString(), boldFont));
			tblIncometotal.addCell(dataCell);
		} else {
			tblExpensetotal.addCell("");
		}

		table.addCell(tblExpensetotal);
		table.addCell(tblIncometotal);

		document.add(table);

	}

}