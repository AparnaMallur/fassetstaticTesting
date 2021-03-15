package com.fasset.pdf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Receipt;
import com.fasset.form.ReceiptForm;
import com.fasset.service.CommonServiceImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@SessionAttributes("user")
public class PDFBuilderForReceipt extends AbstractITextPdfView {

	CommonServiceImpl commenService = new CommonServiceImpl();

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"Receipt Voucher.pdf\"");

		ReceiptForm form = (ReceiptForm) model.get("form");
		Receipt receipt = form.getReceipt();

		String coustomerName = "";
		if (receipt.getCustomer() != null) {
			coustomerName = receipt.getCustomer().getFirm_name();
		} else {
			coustomerName = receipt.getSubLedger().getSubledger_name();
		}

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
		Font regularFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

		doc.setPageSize(PageSize.A4);
		doc.open();

		Paragraph compName = new Paragraph(new Phrase(receipt.getCompany().getCompany_name(), boldFont));
		compName.setAlignment(Paragraph.ALIGN_CENTER);
		doc.add(compName);

		Paragraph address = new Paragraph(new Phrase(receipt.getCompany().getCurrent_address(), regularFont));
		address.setAlignment(Paragraph.ALIGN_CENTER);
		doc.add(address);

		Paragraph state = new Paragraph(
				new Phrase("State : " + receipt.getCompany().getState().getState_name(), regularFont));
		state.setAlignment(Paragraph.ALIGN_CENTER);
		doc.add(state);

		Paragraph para = new Paragraph("Receipt Voucher", regularFont);
		para.setAlignment(Paragraph.ALIGN_CENTER);
		doc.add(para);
		
		PdfPTable mainTable = new PdfPTable(2);
		mainTable.setWidthPercentage(60.0f);
		mainTable.setWidths(new float[] { 70.0f, 30.0f });
		mainTable.setSpacingBefore(10);

		PdfPCell voucherNo = new PdfPCell(new Phrase("No. " + receipt.getVoucher_no(), boldFont));
		voucherNo.setBorder(0);
		mainTable.addCell(voucherNo);

		PdfPCell date = new PdfPCell(
				new Phrase("Date : " + ClasstoConvertDateformat.dateFormat(receipt.getDate().toString()), boldFont));
		date.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		date.setBorder(0);
		mainTable.addCell(date);

		PdfPCell header = new PdfPCell();
		header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

		header.setPhrase(new Phrase("Particulars", regularFont));
		mainTable.addCell(header);

		header.setPhrase(new Phrase("Amount", regularFont));
		mainTable.addCell(header);

		PdfPTable particulars = new PdfPTable(3);
		particulars.setWidthPercentage(100.0f);
		particulars.setWidths(new float[] { 20.0f, 70.0f, 10.0f });
		particulars.getDefaultCell().setBorder(0);

		PdfPCell account = new PdfPCell(new Phrase("Account", boldFont));
		account.setColspan(3);
		account.setBorder(0);
		particulars.addCell(account);
		particulars.completeRow();

		particulars.addCell(" ");
		particulars.addCell(new Phrase(coustomerName, regularFont));
		particulars.completeRow();

		particulars.addCell(" ");
		particulars.addCell(new Phrase(calculate(receipt.getAmount()).toString() + " Dr", boldFont));
		particulars.completeRow();

		PdfPCell through = new PdfPCell(new Phrase("Through", boldFont));
		through.setColspan(3);
		through.setBorder(0);
		through.setPaddingTop(80);
		particulars.addCell(through);
		particulars.completeRow();

		if (receipt.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {
			particulars.addCell(" ");
			particulars.addCell(new Phrase("Cash", regularFont));
			particulars.completeRow();
		} else if (receipt.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CHEQUE) {
			particulars.addCell(" ");
			particulars.addCell(new Phrase("Cheque", regularFont));
			particulars.completeRow();
		} else if (receipt.getPayment_type() == MyAbstractController.PAYMENT_TYPE_DD) {
			particulars.addCell(" ");
			particulars.addCell(new Phrase("DD", regularFont));
			particulars.completeRow();
		} else if (receipt.getPayment_type() == MyAbstractController.PAYMENT_TYPE_NEFT) {
			particulars.addCell(" ");
			particulars.addCell(new Phrase("NEFT", regularFont));
			particulars.completeRow();
		}

		PdfPCell accountof = new PdfPCell(new Phrase("On Account Of :", boldFont));
		accountof.setColspan(3);
		accountof.setBorder(0);
		particulars.addCell(accountof);
		particulars.completeRow();

		if (receipt.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {
			particulars.addCell(" ");
			particulars.addCell(new Phrase("being cash received", regularFont));
			particulars.completeRow();
		} else if (receipt.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CHEQUE) {
			particulars.addCell(" ");
			particulars.addCell(new Phrase("Received through Cheque", regularFont));
			particulars.completeRow();
		} else if (receipt.getPayment_type() == MyAbstractController.PAYMENT_TYPE_DD) {
			particulars.addCell(" ");
			particulars.addCell(new Phrase("Received through DD", regularFont));
			particulars.completeRow();
		} else if (receipt.getPayment_type() == MyAbstractController.PAYMENT_TYPE_NEFT) {
			particulars.addCell(" ");
			particulars.addCell(new Phrase("Received through NEFT", regularFont));
			particulars.completeRow();
		}

		PdfPCell amountInWord = new PdfPCell(new Phrase("Amount (in word) :", boldFont));
		amountInWord.setColspan(3);
		amountInWord.setBorder(0);
		particulars.addCell(amountInWord);
		particulars.completeRow();

		particulars.addCell(" ");
		particulars.addCell(new Phrase(
				commenService.convertFloatNumberToWord((float) (Math.round(receipt.getAmount() * 100.0) / 100.0)),
				regularFont));
		particulars.completeRow();

		mainTable.addCell(particulars);

		PdfPCell amount = new PdfPCell(new Phrase("\n" + calculate(receipt.getAmount()).toString(), boldFont));
		amount.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		mainTable.addCell(amount);

		mainTable.addCell(" ");

		amount = new PdfPCell(new Phrase(calculate(receipt.getAmount()).toString(), boldFont));
		amount.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		mainTable.addCell(amount);

		PdfPTable signTable = new PdfPTable(2);
		signTable.setWidthPercentage(60.0f);
		signTable.setWidths(new float[] { 50.0f, 50.0f });
		signTable.setSpacingBefore(40);
		signTable.getDefaultCell().setBorder(0);

		PdfPCell receiver = new PdfPCell(new Phrase("Receiver’s Signature :", regularFont));
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
	public BigInteger calculate(Double num) {
		BigInteger value=	new BigDecimal(num).toBigInteger();
		return value;
		
	}
}
