package com.fasset.pdf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasset.entities.Company;
import com.fasset.entities.CreditDetails;
import com.fasset.entities.CreditNote;
import com.fasset.entities.User;
import com.fasset.form.GSTDetails;
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

public class PDFBuilderForCreditNote extends AbstractITextPdfView{

	private CommonServiceImpl commonService = new CommonServiceImpl();

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "attachment; filename=\"Credit Voucher.pdf\"");
		
		HttpSession session = request.getSession(true);
		User user = new User();
		user = (User)session.getAttribute("user");
		
		CreditNote creditNote = (CreditNote) model.get("creditNote");
		
		String compAddress= "";
		String custAddress= "";
		
		String invoiceNo= "";
		String Date= "";
		
		Integer count = 1;
		
		Company comp = new Company();
		comp = user.getCompany();
		
		compAddress=comp.getCompany_name()+"\n"+comp.getCurrent_address()+"\nGSTIN/UIN:"+comp.getGst_no()+"\nState Name :"+comp.getState().getState_name()+"\nPin Code:"+comp.getPincode();
		
		custAddress=creditNote.getCustomer().getContact_name()+"\n"+creditNote.getCustomer().getCurrent_address()+"\nGSTIN/UIN:"+creditNote.getCustomer().getGst_no()
				+"\nState Name :"+creditNote.getCustomer().getState().getState_name()+"\nPin Code:"+creditNote.getCustomer().getPincode();
		
		invoiceNo="Credit Note No.\n"+creditNote.getVoucher_no();
		Date="Dated:\n"+creditNote.getDate().toString();
				
		Paragraph par = new Paragraph("Revised Invoice");
		par.setAlignment(Paragraph.ALIGN_CENTER);
		
		doc.setPageSize(PageSize.A4);
		doc.open();
		doc.add(par);

		PdfPTable mainTable = new PdfPTable(9);
		mainTable.setWidthPercentage(100.0f);
		mainTable.setWidths(new float[] { 3.0f,13.0f,6.0f,6.0f,7.0f,4.0f,4.0f,7.0f,7.0f });
		mainTable.setSpacingBefore(10);
		mainTable.getDefaultCell().setMinimumHeight(25);
		mainTable.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);

		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(BaseColor.BLACK);

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
		Font regularFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setPadding(5);
		
		PdfPCell cellRight = new PdfPCell();
		cellRight.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		
		PdfPCell compAdd = new PdfPCell(new Phrase(compAddress, regularFont));
		compAdd.setColspan(4);
		compAdd.setRowspan(3);
		mainTable.addCell(compAdd);
		
		PdfPCell invoiceNum = new PdfPCell(new Phrase(invoiceNo, regularFont));
		invoiceNum.setColspan(2);
		invoiceNum.setMinimumHeight(30);
		mainTable.addCell(invoiceNum);
		
		PdfPCell date = new PdfPCell(new Phrase(Date, regularFont));
		date.setColspan(3);
		date.setMinimumHeight(30);
		mainTable.addCell(date);
		
		PdfPCell deliveryNote = new PdfPCell(new Phrase(""));
		deliveryNote.setColspan(2);
		deliveryNote.setMinimumHeight(30);
		mainTable.addCell(deliveryNote);
		
		PdfPCell termsOfPayment = new PdfPCell(new Phrase("Mode/Terms of Payment", regularFont));
		termsOfPayment.setColspan(3);
		termsOfPayment.setMinimumHeight(30);
		mainTable.addCell(termsOfPayment);
		
		PdfPCell supRef = new PdfPCell(new Phrase("Buyer’s Order No.", regularFont));
		supRef.setColspan(2);
		supRef.setMinimumHeight(30);
		mainTable.addCell(supRef);
		
		PdfPCell otherRef = new PdfPCell(new Phrase("Dated", regularFont));
		otherRef.setColspan(3);
		otherRef.setMinimumHeight(30);
		mainTable.addCell(otherRef);
		
		PdfPCell supAdd = new PdfPCell(new Phrase(custAddress, regularFont));
		supAdd.setColspan(4);
		supAdd.setRowspan(6);
		supAdd.setMinimumHeight(30);
		mainTable.addCell(supAdd);
		
		PdfPCell orderNum = new PdfPCell(new Phrase("Despatch Document No.", regularFont));
		orderNum.setColspan(2);
		orderNum.setMinimumHeight(30);
		mainTable.addCell(orderNum);
		
		PdfPCell dated = new PdfPCell(new Phrase(""));
		dated.setColspan(3);
		dated.setMinimumHeight(30);
		mainTable.addCell(dated);
		
		PdfPCell docNum = new PdfPCell(new Phrase("Despatch through", regularFont));
		docNum.setColspan(2);
		docNum.setMinimumHeight(30);
		mainTable.addCell(docNum);
		
		PdfPCell noteDate = new PdfPCell(new Phrase("Destination", regularFont));
		noteDate.setColspan(3);
		noteDate.setMinimumHeight(30);
		mainTable.addCell(noteDate);
		
		PdfPCell terms = new PdfPCell(new Phrase("Terms of Delivery", regularFont));
		terms.setColspan(5);
		terms.setRowspan(4);
		terms.setMinimumHeight(50);
		mainTable.addCell(terms);
		
		PdfPCell blank = new PdfPCell(new Phrase(""));
		blank.setColspan(9);
		mainTable.addCell(blank);
		
		if(creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
			
			cell.setPhrase(new Phrase("Sr. No.", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Description of Goods", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("HSN/SAC", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Quantity", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Rate", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Per", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Disc.", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Other Charges", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Amount", boldFont));
			mainTable.addCell(cell);
			
			mainTable.setHeaderRows(10);
			
			Double total = new Double(0);
			List<GSTDetails> gstDetails = new ArrayList<GSTDetails>();	
			Double rate = 0.0;
			if(creditNote.getCreditDetails() != null) {
				
			}
			for(CreditDetails obj:creditNote.getCreditDetails()){			
				mainTable.addCell(new Phrase(count.toString(),regularFont));
				mainTable.addCell(new Phrase(obj.getProduct_id().getProduct_name(),regularFont));
				if(obj.getHSNCode()!=null)
				{
					mainTable.addCell(new Phrase(obj.getHSNCode(),regularFont));
				}
				else if(obj.getProduct_id().getHsn_san_no()!=null)
				{
					mainTable.addCell(new Phrase(obj.getProduct_id().getHsn_san_no(),regularFont));
				}
				else
				{
					mainTable.addCell(new Phrase(" ",regularFont));
				}
				mainTable.addCell(new Phrase(calculate(obj.getQuantity()).toString(),regularFont));
				cellRight.setPhrase(new Phrase(calculate(obj.getRate()).toString(),regularFont));
				mainTable.addCell(cellRight);
				mainTable.addCell(new Phrase(obj.getProduct_id().getUom().getUnit(),regularFont));
				cellRight.setPhrase(new Phrase(calculate(obj.getDiscount()).toString(),regularFont));
				mainTable.addCell(cellRight);
				Double other = obj.getFrieght()+obj.getOthers()+obj.getLabour_charges();
				cellRight.setPhrase(new Phrase(calculate(other).toString(),regularFont));
				mainTable.addCell(cellRight);
				cellRight.setPhrase(new Phrase(calculate(obj.getTransaction_amount()).toString(),regularFont));
				mainTable.addCell(cellRight);
				total += obj.getTransaction_amount();
				count++;
				
				GSTDetails gstDetail = new GSTDetails();
	
				if(obj.getHSNCode()!=null)
				{
					gstDetail.setHsnCode(obj.getHSNCode());
				}
				else if(obj.getProduct_id().getHsn_san_no()!=null)
				{
					
					gstDetail.setHsnCode(obj.getProduct_id().getHsn_san_no());
				}
				else
				{
					gstDetail.setHsnCode("");
				}
				if (obj.getCgst() != null) {
					rate = (obj.getCgst() * 100) / obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setCgstAmount(obj.getCgst());
					gstDetail.setCgstRate(rate);
				} else {
					gstDetail.setCgstAmount((double)0);
				}
				if (obj.getSgst() != null) {
					rate = (obj.getSgst() * 100) / obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setSgstAmount(obj.getSgst());
					gstDetail.setSgstRate(rate);
				} else {
					gstDetail.setSgstAmount((double)0);
				}
				if (obj.getIgst() != null) {
					rate = (obj.getIgst() * 100) / obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setIgstAmount(obj.getIgst());
					gstDetail.setIgstRate(rate);
				} else {
					gstDetail.setIgstAmount((double)0);
				}
				if (obj.getState_com_tax() != null) {
					rate = (obj.getState_com_tax() * 100) / obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setCessAmount(obj.getState_com_tax());
					gstDetail.setCessRate(rate);
				} else {
					gstDetail.setCessAmount((double)0);
				}
				
				if(obj.getVAT() != null){
					rate = (obj.getVAT()*100)/obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setVatAmount(obj.getVAT());
					gstDetail.setVatRate(rate);
				
				}
				else{
					gstDetail.setVatAmount((double)0);
					
				}
				
				if(obj.getVATCST() != null){
					rate = (obj.getVATCST()*100)/obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setCstAmount(obj.getVATCST());
					gstDetail.setCstRate(rate);
					
				}
				else{
					gstDetail.setCstAmount((double)0);
					
				}
				if(obj.getExcise() != null){
					rate = (obj.getExcise()*100)/obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setExciseAmount(obj.getExcise());
					gstDetail.setExciseRate(rate);
						
				}
				else{
					gstDetail.setExciseAmount((double)0);
					
				}
				gstDetail.setTaxableAmount(obj.getTransaction_amount());

				gstDetails.add(gstDetail);
				
			}
			
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			cellRight.setPhrase(new Phrase(calculate(total).toString(),boldFont));
			mainTable.addCell(cellRight);
			
			for (GSTDetails gstDetail : gstDetails) {
				if (gstDetail.getCgstAmount() > 0) {
					mainTable.addCell("");
					mainTable.addCell(new Phrase("CGST OUTPUT", regularFont));
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(gstDetail.getCgstRate().toString(), regularFont));
					mainTable.addCell(cellRight);
					mainTable.addCell("%");
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCgstAmount()).toString(), regularFont));
					mainTable.addCell(cellRight);
				}
				if (gstDetail.getSgstAmount() > 0) {
					mainTable.addCell("");
					mainTable.addCell(new Phrase("SGST OUTPUT", regularFont));
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getSgstRate()).toString(), regularFont));
					mainTable.addCell(cellRight);
					mainTable.addCell("%");
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getSgstAmount()).toString(), regularFont));
					mainTable.addCell(cellRight);
				}
				if (gstDetail.getIgstAmount() > 0) {
					mainTable.addCell("");
					mainTable.addCell(new Phrase("IGST OUTPUT", regularFont));
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getIgstRate()).toString(), regularFont));
					mainTable.addCell(cellRight);
					mainTable.addCell("%");
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getIgstAmount()).toString(), regularFont));
					mainTable.addCell(cellRight);
				}
				if (gstDetail.getCessAmount() > 0) {
					mainTable.addCell("");
					mainTable.addCell(new Phrase("CESS OUTPUT", regularFont));
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCessRate()).toString(), regularFont));
					mainTable.addCell(cellRight);
					mainTable.addCell("%");
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCessAmount()).toString(), regularFont));
					mainTable.addCell(cellRight);
				}
				if(gstDetail.getVatAmount() > 0){
					mainTable.addCell("");
					mainTable.addCell(new Phrase("VAT OUTPUT",regularFont));
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getVatRate()).toString(),regularFont));
					mainTable.addCell(cellRight);
					mainTable.addCell("%");
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getVatAmount()).toString(),regularFont));
					mainTable.addCell(cellRight);
				}
				if(gstDetail.getCstAmount() > 0){
					mainTable.addCell("");
					mainTable.addCell(new Phrase("CST OUTPUT",regularFont));
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCstRate()).toString(),regularFont));
					mainTable.addCell(cellRight);
					mainTable.addCell("%");
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCstAmount()).toString(),regularFont));
					mainTable.addCell(cellRight);
				}
				if(gstDetail.getExciseAmount() > 0){
					mainTable.addCell("");
					mainTable.addCell(new Phrase("EXCISE OUTPUT",regularFont));
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getExciseRate()).toString(),regularFont));
					mainTable.addCell(cellRight);
					mainTable.addCell("%");
					mainTable.addCell("");
					mainTable.addCell("");
					cellRight.setPhrase(new Phrase(calculate(gstDetail.getExciseAmount()).toString(),regularFont));
					mainTable.addCell(cellRight);
				}
			}
			
			
			
			/*if(creditNote.getIGST_head() != null && creditNote.getIGST_head() > 0){
				total = total + creditNote.getIGST_head();
			}
			else{
				total = total + creditNote.getCGST_head() + creditNote.getSGST_head();
			}*/
			
			total = total + creditNote.getCGST_head() + creditNote.getSGST_head()+ creditNote.getIGST_head()+creditNote.getSCT_head()+creditNote.getTotal_vat()+creditNote.getTotal_vatcst()+creditNote.getTotal_excise();

			mainTable.addCell("");
			mainTable.addCell(new Phrase("Total", regularFont));
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			//cellRight.setPhrase(new Phrase(Double.toString((Double) (Math.round(total * 100.0) / 100.0)), boldFont));
			cellRight.setPhrase(new Phrase(calculate((Math.round(total * 100.0) / 100.0)).toString()));
			mainTable.addCell(cellRight);

			PdfPCell totalAmount = new PdfPCell(new Phrase(
					"Amount Chargable(In Words):\n"
							+ commonService.convertDoubleNumberToWord((Double) (Math.round(total * 100.0) / 100.0)),
					regularFont));
			totalAmount.setColspan(9);
			totalAmount.setMinimumHeight(50);
			totalAmount.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
			mainTable.addCell(totalAmount);

			if (creditNote.getTotal_vat() > 0) {
				PdfPTable taxTable = new PdfPTable(9);
				taxTable.setWidthPercentage(100.0f);
				taxTable.setWidths(new float[] { 20.0f, 15.0f, 10.0f, 15.0f, 10.0f, 15.0f, 10.0f, 15.0f, 15.0f });
				taxTable.setSpacingBefore(10);
				taxTable.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);

				PdfPCell hsn = new PdfPCell(new Phrase("HSN/SAC", boldFont));
				hsn.setRowspan(2);
				hsn.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(hsn);

				PdfPCell taxableValue = new PdfPCell(new Phrase("Taxable Value", boldFont));
				taxableValue.setRowspan(2);
				taxableValue.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(taxableValue);

				PdfPCell centralTax = new PdfPCell(new Phrase("VAT", boldFont));
				centralTax.setColspan(2);
				centralTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(centralTax);

				PdfPCell stateTax = new PdfPCell(new Phrase("CST", boldFont));
				stateTax.setColspan(2);
				stateTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(stateTax);

				PdfPCell cessTax = new PdfPCell(new Phrase("EXCISE", boldFont));
				cessTax.setColspan(2);
				cessTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(cessTax);

				PdfPCell totalTax = new PdfPCell(new Phrase("Total Tax Amount", boldFont));
				totalTax.setRowspan(2);
				totalTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(totalTax);

				PdfPCell rateCell = new PdfPCell(new Phrase("Rate", boldFont));
				rateCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

				PdfPCell amountCell = new PdfPCell(new Phrase("Amount", boldFont));
				amountCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

				taxTable.addCell(rateCell);
				taxTable.addCell(amountCell);

				taxTable.addCell(rateCell);
				taxTable.addCell(amountCell);

				taxTable.addCell(rateCell);
				taxTable.addCell(amountCell);
				
				Double taxableTotal = new Double(0);
				Double vatTotal = new Double(0);
				Double vatcstTotal = new Double(0);
				Double exciseTotal = new Double(0);
				for (GSTDetails gstDetail : gstDetails) {
					taxTable.addCell(new Phrase(gstDetail.getHsnCode(), regularFont));

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getTaxableAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);
					taxableTotal += gstDetail.getTaxableAmount();

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getVatRate()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getVatAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);

					vatTotal += gstDetail.getVatAmount();

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCstRate()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCstAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);

					vatcstTotal += gstDetail.getCstAmount();

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getExciseRate()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getExciseAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);

					exciseTotal += gstDetail.getExciseAmount();

					Double taxTotal = gstDetail.getVatAmount() + gstDetail.getCstAmount() + gstDetail.getExciseAmount();
					cellRight.setPhrase(new Phrase(calculate(taxTotal).toString(), regularFont));
					taxTable.addCell(cellRight);
				}
				taxTable.addCell(new Phrase("Total", regularFont));
				cellRight.setPhrase(new Phrase(calculate(taxableTotal).toString(), regularFont));
				taxTable.addCell(cellRight);
				taxTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(vatTotal).toString(), regularFont));
				taxTable.addCell(cellRight);
				taxTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(vatcstTotal).toString(), regularFont));
				taxTable.addCell(cellRight);
				taxTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(exciseTotal).toString(), regularFont));
				taxTable.addCell(cellRight);
				Double taxTotal = vatTotal + vatcstTotal+exciseTotal;
				cellRight.setPhrase(new Phrase(calculate(taxTotal).toString(), regularFont));
				taxTable.addCell(cellRight);

				PdfPCell taxTbl = new PdfPCell(taxTable);
				taxTbl.setColspan(9);
				mainTable.addCell(taxTbl);

				PdfPCell totalTaxAmt = new PdfPCell(new Phrase(
						"Total Tax Amount(In Words):\n"
								+ commonService.convertDoubleNumberToWord((Double) (Math.round(taxTotal * 100.0) / 100.0)),
						regularFont));
				totalTaxAmt.setColspan(9);
				totalTaxAmt.setMinimumHeight(50);
				totalTaxAmt.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
				mainTable.addCell(totalTaxAmt);
				
			} else if (creditNote.getIGST_head() > 0) {
				PdfPTable taxTable = new PdfPTable(7);
				taxTable.setWidthPercentage(100.0f);
				taxTable.setWidths(new float[] { 20.0f, 15.0f, 10.0f, 15.0f, 10.0f, 15.0f, 15.0f });
				taxTable.setSpacingBefore(10);
				taxTable.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);

				PdfPCell hsn = new PdfPCell(new Phrase("HSN/SAC", boldFont));
				hsn.setRowspan(2);
				hsn.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(hsn);

				PdfPCell taxableValue = new PdfPCell(new Phrase("Taxable Value", boldFont));
				taxableValue.setRowspan(2);
				taxableValue.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(taxableValue);

				PdfPCell centralTax = new PdfPCell(new Phrase("Integrated Tax", boldFont));
				centralTax.setColspan(2);
				centralTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(centralTax);

				PdfPCell cessTax = new PdfPCell(new Phrase("CESS Tax", boldFont));
				cessTax.setColspan(2);
				cessTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(cessTax);

				PdfPCell totalTax = new PdfPCell(new Phrase("Total Tax Amount", boldFont));
				totalTax.setRowspan(2);
				totalTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(totalTax);

				PdfPCell rateCell = new PdfPCell(new Phrase("Rate", boldFont));
				rateCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

				PdfPCell amountCell = new PdfPCell(new Phrase("Amount", boldFont));
				amountCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

				taxTable.addCell(rateCell);
				taxTable.addCell(amountCell);

				taxTable.addCell(rateCell);
				taxTable.addCell(amountCell);

				Double taxableTotal = new Double(0);
				Double igstTotal = new Double(0);
				Double cessTotal = new Double(0);
				for (GSTDetails gstDetail : gstDetails) {
					taxTable.addCell(new Phrase(gstDetail.getHsnCode(), regularFont));

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getTaxableAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);

					taxableTotal += gstDetail.getTaxableAmount();

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getIgstRate()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getIgstAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);

					igstTotal += gstDetail.getIgstAmount();

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCessRate()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCessAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cessTotal += gstDetail.getCessAmount();

					Double taxTotal = gstDetail.getIgstAmount() + gstDetail.getCessAmount();
					cellRight.setPhrase(new Phrase(calculate(taxTotal).toString(), regularFont));
					taxTable.addCell(cellRight);
				}
				taxTable.addCell(new Phrase("Total", regularFont));
				cellRight.setPhrase(new Phrase(calculate(taxableTotal).toString(), regularFont));
				taxTable.addCell(cellRight);
				taxTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(igstTotal).toString(), regularFont));
				taxTable.addCell(cellRight);
				taxTable.addCell(cellRight);

				PdfPCell taxTbl = new PdfPCell(taxTable);
				taxTbl.setColspan(9);
				mainTable.addCell(taxTbl);

				PdfPCell totalTaxAmt = new PdfPCell(new Phrase(
						"Total Tax Amount(In Words):\n"
								+ commonService.convertDoubleNumberToWord((Double) (Math.round(igstTotal * 100.0) / 100.0)),
						regularFont));
				totalTaxAmt.setColspan(9);
				totalTaxAmt.setMinimumHeight(50);
				totalTaxAmt.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
				mainTable.addCell(totalTaxAmt);
			} else if (creditNote.getCGST_head() > 0 && creditNote.getSGST_head() > 0) {
				PdfPTable taxTable = new PdfPTable(9);
				taxTable.setWidthPercentage(100.0f);
				taxTable.setWidths(new float[] { 20.0f, 15.0f, 10.0f, 15.0f, 10.0f, 15.0f, 10.0f, 15.0f, 15.0f });
				taxTable.setSpacingBefore(10);
				taxTable.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);

				PdfPCell hsn = new PdfPCell(new Phrase("HSN/SAC", boldFont));
				hsn.setRowspan(2);
				hsn.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(hsn);

				PdfPCell taxableValue = new PdfPCell(new Phrase("Taxable Value", boldFont));
				taxableValue.setRowspan(2);
				taxableValue.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(taxableValue);

				PdfPCell centralTax = new PdfPCell(new Phrase("Central Tax", boldFont));
				centralTax.setColspan(2);
				centralTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(centralTax);

				PdfPCell stateTax = new PdfPCell(new Phrase("State Tax", boldFont));
				stateTax.setColspan(2);
				stateTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(stateTax);

				PdfPCell cessTax = new PdfPCell(new Phrase("CESS Tax", boldFont));
				cessTax.setColspan(2);
				cessTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(cessTax);

				PdfPCell totalTax = new PdfPCell(new Phrase("Total Tax Amount", boldFont));
				totalTax.setRowspan(2);
				totalTax.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				taxTable.addCell(totalTax);

				PdfPCell rateCell = new PdfPCell(new Phrase("Rate", boldFont));
				rateCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

				PdfPCell amountCell = new PdfPCell(new Phrase("Amount", boldFont));
				amountCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

				taxTable.addCell(rateCell);
				taxTable.addCell(amountCell);

				taxTable.addCell(rateCell);
				taxTable.addCell(amountCell);

				taxTable.addCell(rateCell);
				taxTable.addCell(amountCell);

				Double taxableTotal = new Double(0);
				Double cgstTotal = new Double(0);
				Double sgstTotal = new Double(0);
				Double cessTotal = new Double(0);
				for (GSTDetails gstDetail : gstDetails) {
					taxTable.addCell(new Phrase(gstDetail.getHsnCode(), regularFont));

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getTaxableAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);
					taxableTotal += gstDetail.getTaxableAmount();

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCgstRate()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCgstAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cgstTotal += gstDetail.getCgstAmount();

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getSgstRate()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getSgstAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);

					sgstTotal += gstDetail.getSgstAmount();

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCessRate()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cellRight.setPhrase(new Phrase(calculate(gstDetail.getCessAmount()).toString(), regularFont));
					taxTable.addCell(cellRight);

					cessTotal += gstDetail.getCessAmount();

					Double taxTotal = gstDetail.getCgstAmount() + gstDetail.getSgstAmount() + gstDetail.getCessAmount();
					cellRight.setPhrase(new Phrase(calculate(taxTotal).toString(), regularFont));
					taxTable.addCell(cellRight);
				}
				taxTable.addCell(new Phrase("Total", regularFont));
				cellRight.setPhrase(new Phrase(calculate(taxableTotal).toString(), regularFont));
				taxTable.addCell(cellRight);
				taxTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(cgstTotal).toString(), regularFont));
				taxTable.addCell(cellRight);
				taxTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(sgstTotal).toString(), regularFont));
				taxTable.addCell(cellRight);
				taxTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(cessTotal).toString(), regularFont));
				taxTable.addCell(cellRight);
				Double taxTotal = cgstTotal + sgstTotal+cessTotal;
				cellRight.setPhrase(new Phrase(calculate(taxTotal).toString(), regularFont));
				taxTable.addCell(cellRight);

				PdfPCell taxTbl = new PdfPCell(taxTable);
				taxTbl.setColspan(9);
				mainTable.addCell(taxTbl);

				PdfPCell totalTaxAmt = new PdfPCell(new Phrase(
						"Total Tax Amount(In Words):\n"
								+ commonService.convertDoubleNumberToWord((Double) (Math.round(taxTotal * 100.0) / 100.0)),
						regularFont));
				totalTaxAmt.setColspan(9);
				totalTaxAmt.setMinimumHeight(50);
				totalTaxAmt.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
				mainTable.addCell(totalTaxAmt);
			}
			
		    
		}
		else
		{
			cell.setPhrase(new Phrase("Sr. No.", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Description of Goods", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("HSN/SAC", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Quantity", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Rate", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Per", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Disc.", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Other Charges", boldFont));
			mainTable.addCell(cell);

			cell.setPhrase(new Phrase("Amount", boldFont));
			mainTable.addCell(cell);
			mainTable.setHeaderRows(10);
			
			
			mainTable.addCell("1");
			if(creditNote.getSubledger()!=null) {
			mainTable.addCell(creditNote.getSubledger().getSubledger_name());
			}
			else
			{
				mainTable.addCell("");
			}
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
		    mainTable.addCell(creditNote.getRound_off().toString());
			
			mainTable.addCell("");
			mainTable.addCell(new Phrase("Total",regularFont));
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
			mainTable.addCell("");
		//	cellRight.setPhrase(new Phrase(Double.toString((Double) (Math.round(creditNote.getRound_off() * 100.0) / 100.0)),boldFont));
			cellRight.setPhrase(new Phrase(calculate((Math.round(creditNote.getRound_off() * 100.0) / 100.0)).toString()));
			
			
			mainTable.addCell(cellRight);
			
			PdfPCell totalAmount = new PdfPCell(new Phrase("Amount Chargable(In Words):\n"+commonService.convertDoubleNumberToWord((Double) (Math.round(creditNote.getRound_off() * 100.0) / 100.0)),regularFont));
			totalAmount.setColspan(9);
			totalAmount.setMinimumHeight(50);
			totalAmount.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
			mainTable.addCell(totalAmount);
		}

		if(comp.getBank() != null) {
			PdfPCell bank = new PdfPCell(new Phrase("Bank Details :\n\nBank Name :"+comp.getBank().getBank_name()+"\nAc/No. : "+comp.getBank().getAccount_no()+"\nBranch : "+comp.getBank().getBank_name()+"\nIFSC Code : "+comp.getBank().getIfsc_no(), regularFont));
			bank.setColspan(9);
			mainTable.addCell(bank);
		}
		
		String remarkString = "Remark:\n\nBeing credit note issued to "+creditNote.getCustomer().getFirm_name()+" for ";
		switch(creditNote.getDescription()) {
			case 1: remarkString += "Sales return";
					break;
			case 2: remarkString += "Post sales discount";
					break;
			case 3: remarkString += "Deficiency in services ";
					break;
			case 4: remarkString += "Correction in Invoice";
					break;
			case 5: remarkString += "Change in POS";
					break;
			case 6: remarkString += "Finalization of Provisional Assessment";
					break;
			case 7: remarkString += creditNote.getRemark();
					break;
		}
		
		PdfPCell remark = new PdfPCell(new Phrase(remarkString, regularFont));
		remark.setColspan(9);
		mainTable.addCell(remark);
		
		doc.add(mainTable);		
	}
	
	public BigInteger calculate(Double num) {
		BigInteger value=	new BigDecimal(num).toBigInteger();
		return value;
		
	}

}