package com.fasset.pdf;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasset.entities.Company;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.entities.User;
import com.fasset.form.GSTDetails;
import com.fasset.form.SalesForm;
import com.fasset.service.CommonServiceImpl;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;

@SessionAttributes("user")
public class PDFBuilderForSales extends AbstractITextPdfView {

	static CommonServiceImpl commonService = new CommonServiceImpl();

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"Sales Invoice.pdf\"");
		HttpSession session = request.getSession(true);
		User user = new User();
		user = (User) session.getAttribute("user");

		SalesForm form = (SalesForm) model.get("form");
		SalesEntry entry = new SalesEntry();
		List<SalesEntryProductEntityClass> customerProductList = new ArrayList<SalesEntryProductEntityClass>();
		entry = form.getSalesEntry();
		customerProductList = form.getCustomerProductList();

		String compAddress = "";
		String customerAddress = "";
		String pinCode = "";

		String invoiceNo = "";
		String Date = "";

		Integer count = 1;

		Company comp = new Company();
		comp = user.getCompany();
		pinCode = (comp.getPincode() != null) ? comp.getPincode().toString() : "";
		compAddress = "Sellers details : \n" + comp.getCompany_name() + "\n" + comp.getCurrent_address()
				+ "\nGSTIN/UIN:" + entry.getCompany().getGst_no();
		if(entry.getCompany().getRegistration_no()!=null)
		{
			if(entry.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| entry.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				compAddress +="\nCIN NO:" + entry.getCompany().getRegistration_no();
			}
		}	
		compAddress +="\nState Name :" + comp.getState().getState_name()
				+ " : "+entry.getCustomer().getState().getState_code() + "\nPin Code:" + pinCode;
		if ((entry.getCustomer() != null) || (entry.getCustomer_id() != null)) {
			pinCode = (entry.getCustomer().getPincode() != null) ? entry.getCustomer().getPincode().toString() : "";
			customerAddress = "Receiver's details : \n" + entry.getCustomer().getFirm_name() + "\n"
					+ entry.getCustomer().getCurrent_address() + "\nGSTIN/UIN:" + entry.getCustomer().getGst_no()
					+ "\nState Name :" + entry.getCustomer().getState().getState_name() +" : "+entry.getCustomer().getState().getState_code() + "\nPin Code:" + pinCode;
		}
		
		invoiceNo = "Invoice No.\n" + entry.getVoucher_no();
		Date = "Date:\n" + ClasstoConvertDateformat.dateFormat(entry.getCreated_date().toString());

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
		Font regularFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
		String filePath = request.getServletContext().getRealPath("resources");
		filePath += "/images/logo/";
		System.out.println("the file path of logo is" +filePath);
		String Logo=filePath+comp.getLogo();
		 
		 File tempFile = new File(Logo);
		 boolean exists = tempFile.exists();
		 Paragraph par = new Paragraph("Tax Invoice", boldFont);
			par.setAlignment(Paragraph.ALIGN_CENTER);

			doc.setPageSize(PageSize.A4);
			doc.open();
		 if(exists){
		
			 Image img = Image.getInstance(Logo);
		
		img.scaleAbsoluteHeight(90);
		img.scaleAbsoluteWidth((img.getWidth() * 60) / img.getHeight());
		img.setAlignment(Paragraph.ALIGN_RIGHT);
		//img.setAbsolutePosition(10, 50);
		//img.setAbsolutePosition(450f, 10f);
		doc.add(img);
		 }
		doc.add(par);
		PdfPTable mainTable = new PdfPTable(9);
		mainTable.setWidthPercentage(100.0f);
		mainTable.setWidths(new float[] { 3.0f, 15.0f, 7.0f, 6.0f, 5.0f, 4.0f, 4.0f, 8.0f, 10.0f });
		mainTable.setSpacingBefore(10);
		mainTable.getDefaultCell().setMinimumHeight(25);
		mainTable.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setPadding(5);

		PdfPCell cellRight = new PdfPCell();
		cellRight.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

		PdfPCell compAdd = new PdfPCell(new Phrase(compAddress, regularFont));
		compAdd.setColspan(3);
		compAdd.setRowspan(3);
		mainTable.addCell(compAdd);

		PdfPCell invoiceNum = new PdfPCell(new Phrase(invoiceNo, regularFont));
		invoiceNum.setColspan(3);
		invoiceNum.setMinimumHeight(30);
		mainTable.addCell(invoiceNum);

		PdfPCell date = new PdfPCell(new Phrase(Date, regularFont));
		date.setColspan(3);
		date.setMinimumHeight(30);
		mainTable.addCell(date);

		PdfPCell deliveryNote = new PdfPCell(new Phrase("Delivery Note", regularFont));
		deliveryNote.setColspan(3);
		deliveryNote.setMinimumHeight(30);
		mainTable.addCell(deliveryNote);

		PdfPCell termsOfPayment = new PdfPCell(new Phrase("Mode/Terms of Payment", regularFont));
		termsOfPayment.setColspan(3);
		termsOfPayment.setMinimumHeight(30);
		mainTable.addCell(termsOfPayment);

		//PdfPCell supRef = new PdfPCell(new Phrase("Supplier’s Ref.", regularFont));
		PdfPCell supRef = new PdfPCell(new Phrase("Suppliers Ref.", regularFont));
		supRef.setColspan(3);
		supRef.setMinimumHeight(30);
		mainTable.addCell(supRef);

		PdfPCell otherRef = new PdfPCell(new Phrase("Other Reference(s)", regularFont));
		otherRef.setColspan(3);
		otherRef.setMinimumHeight(30);
		mainTable.addCell(otherRef);
		System.out.println(entry.getCustomer());
		System.out.println(entry.getCustomer_id());
		if ((entry.getCustomer() != null) || (entry.getCustomer_id() != null)) {
			PdfPCell custAdd = new PdfPCell(new Phrase(customerAddress, regularFont));
			custAdd.setColspan(3);
			custAdd.setRowspan(6);
			mainTable.addCell(custAdd);
		} else {
			String customerAddress1 = "";
			if (entry.getSale_type() == 1) {
				customerAddress1 += "Receiver's details : \n" + "Cash Sales";
			} else {
				customerAddress1 += "Receiver's details : \n" + "Card Sales " + entry.getBank().getBank_name()
						+ " - " + entry.getBank().getAccount_no();
			}
			PdfPCell custAdd = new PdfPCell(new Phrase(customerAddress1, regularFont));
			custAdd.setColspan(3);
			custAdd.setRowspan(6);
			mainTable.addCell(custAdd);
		}

	//	PdfPCell orderNum = new PdfPCell(new Phrase("Buyer’s Order No.", regularFont));
		PdfPCell orderNum = new PdfPCell(new Phrase("Buyers Order No.", regularFont));
		orderNum.setColspan(3);
		orderNum.setMinimumHeight(30);
		mainTable.addCell(orderNum);

		PdfPCell dated = new PdfPCell(new Phrase("Dated", regularFont));
		dated.setColspan(3);
		dated.setMinimumHeight(30);
		mainTable.addCell(dated);

		PdfPCell docNum = new PdfPCell(new Phrase("Dispatch Document No.", regularFont));
		docNum.setColspan(3);
		invoiceNum.setMinimumHeight(30);
		mainTable.addCell(docNum);

		PdfPCell noteDate = new PdfPCell(new Phrase("Delivery Note Date", regularFont));
		noteDate.setColspan(3);
		noteDate.setMinimumHeight(30);
		mainTable.addCell(noteDate);

		PdfPCell despatched = new PdfPCell(new Phrase("Dispatched through", regularFont));
		despatched.setColspan(3);
		despatched.setMinimumHeight(30);
		mainTable.addCell(despatched);

		PdfPCell dest = new PdfPCell(new Phrase("Destination", regularFont));
		dest.setColspan(3);
		dest.setMinimumHeight(30);
		mainTable.addCell(dest);

		PdfPCell terms = new PdfPCell(new Phrase("Terms of Delivery", regularFont));
		terms.setColspan(6);
		terms.setRowspan(3);
		terms.setMinimumHeight(50);
		mainTable.addCell(terms);

		PdfPCell blank = new PdfPCell(new Phrase(""));
		blank.setColspan(9);
		mainTable.addCell(blank);

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

		cell.setPhrase(new Phrase("Disc. %", boldFont));
		mainTable.addCell(cell);

		cell.setPhrase(new Phrase("Other Charges", boldFont));
		mainTable.addCell(cell);

		cell.setPhrase(new Phrase("Amount", boldFont));
		mainTable.addCell(cell);

		mainTable.setHeaderRows(10);

		Double total = 0.0;
		List<GSTDetails> gstDetails = new ArrayList<GSTDetails>();
		Double rate = 0.0;

		for (SalesEntryProductEntityClass obj : customerProductList) {
			mainTable.addCell(new Phrase(count.toString(), regularFont));
			mainTable.addCell(new Phrase(obj.getProduct_name(), regularFont));
			mainTable.addCell(new Phrase(obj.getHSNCode(), regularFont));
			mainTable.addCell(new Phrase(calculate(obj.getQuantity()).toString(), regularFont));
			cellRight.setPhrase(new Phrase(calculate(obj.getRate()).toString(), regularFont));
			mainTable.addCell(cellRight);
			mainTable.addCell(new Phrase(obj.getUOM(), regularFont));
			cellRight.setPhrase(new Phrase(obj.getDiscount().toString(), regularFont));
			mainTable.addCell(cellRight);
			Double other = obj.getFreight() + obj.getOthers() + obj.getLabour_charges();
			cellRight.setPhrase(new Phrase(other.toString(), regularFont));
			mainTable.addCell(cellRight);
			cellRight.setPhrase(new Phrase(calculate(obj.getTransaction_amount()).toString(), regularFont));
			mainTable.addCell(cellRight);
			total += obj.getTransaction_amount();
		
			count++;
			
				GSTDetails gstDetail = new GSTDetails();
				gstDetail.setHsnCode(obj.getHSNCode());
				if (obj.getCGST() != null) {
					rate = (obj.getCGST() * 100) / obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setCgstAmount(obj.getCGST());
					gstDetail.setCgstRate(rate);
				} else {
					gstDetail.setCgstAmount((double)0);
				}
				if (obj.getSGST() != null) {
					rate = (obj.getSGST() * 100) / obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setSgstAmount(obj.getSGST());
					gstDetail.setSgstRate(rate);
				} else {
					gstDetail.setSgstAmount((double)0);
				}
				if (obj.getIGST() != null) {
					rate = (obj.getIGST() * 100) / obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setIgstAmount(obj.getIGST());
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
		cellRight.setPhrase(new Phrase(calculate(total).toString(), boldFont));
		mainTable.addCell(cellRight);

		for (GSTDetails gstDetail : gstDetails) {
			if (gstDetail.getCgstAmount() > 0) {
				mainTable.addCell("");
				mainTable.addCell(new Phrase("CGST OUTPUT", regularFont));
				mainTable.addCell("");
				mainTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(gstDetail.getCgstRate()).toString(), regularFont));
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

		total = total + entry.getCgst() + entry.getSgst()+ entry.getIgst()+entry.getState_compansation_tax()+entry.getTotal_vat()+entry.getTotal_vatcst()+entry.getTotal_excise();
		
		mainTable.addCell("");
		mainTable.addCell(new Phrase("Total", regularFont));
		mainTable.addCell("");
		mainTable.addCell("");
		mainTable.addCell("");
		mainTable.addCell("");
		mainTable.addCell("");
		mainTable.addCell("");
		cellRight.setPhrase(new Phrase(calculate(total).toString(), boldFont));
		mainTable.addCell(cellRight);

		PdfPCell totalAmount = new PdfPCell(new Phrase(
				"Amount Chargable(In Words):\n"
						+ commonService.convertDoubleNumberToWord((Double) (Math.round(total * 100.0) / 100.0)),
				regularFont));
		totalAmount.setColspan(9);
		totalAmount.setMinimumHeight(50);
		totalAmount.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		mainTable.addCell(totalAmount);

		if (entry.getTotal_vat() > 0) {
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

				cellRight.setPhrase(new Phrase(calculate(gstDetail.getVatRate()).toString(10), regularFont));
				taxTable.addCell(cellRight);

				cellRight.setPhrase(new Phrase(calculate(gstDetail.getVatAmount()).toString(), regularFont));
				taxTable.addCell(cellRight);

				vatTotal += gstDetail.getVatAmount();

				cellRight.setPhrase(new Phrase(gstDetail.getCstRate().toString(), regularFont));
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
			
		} else if (entry.getIgst() > 0) {
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
		} else if (entry.getCgst() > 0 && entry.getSgst() > 0) {
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

		if (entry.getCompany().getBank() != null) {
			PdfPCell bank = new PdfPCell(
					new Phrase("Bank Details:\n\nBank Name :" + entry.getCompany().getBank().getBank_name() + "\nAc/No. : "
							+ entry.getCompany().getBank().getAccount_no()+ "\nBranch Name. : "+entry.getCompany().getBank().getBranch()+ "\nIFSC Code. : "+entry.getCompany().getBank().getIfsc_no(), regularFont));
			bank.setColspan(9);
			mainTable.addCell(bank);
		}
		if ((entry.getCustomer() != null) || (entry.getCustomer_id() != null)) {
			PdfPCell remark = new PdfPCell(
					new Phrase("Remark:\n\nBeing supply effected to " + entry.getCustomer().getFirm_name() + " through "
							+ entry.getVoucher_no() + " dated " + entry.getCreated_date() + ".", regularFont));
			remark.setColspan(9);
			mainTable.addCell(remark);
		} else {
			if (entry.getSale_type() == 1) {
				PdfPCell remark = new PdfPCell(new Phrase("Remark:\n\nBeing supply effected to Cash Sales through "
						+ entry.getVoucher_no() + " dated " + entry.getCreated_date() + ".", regularFont));
				remark.setColspan(9);
				mainTable.addCell(remark);
			} else {
				PdfPCell remark = new PdfPCell(new Phrase("Remark:\n\nBeing supply effected to Card Sales "
						+ entry.getBank().getBank_name() + " - " + entry.getBank().getAccount_no() + " through "
						+ entry.getVoucher_no() + " dated " + entry.getCreated_date() + ".", regularFont));
				remark.setColspan(9);
				mainTable.addCell(remark);
			}

		}

		PdfPCell blank1 = new PdfPCell(new Phrase(""));
		blank1.setColspan(4);
		mainTable.addCell(blank1);

		PdfPCell sign = new PdfPCell(
				new Phrase("for " + comp.getCompany_name() + "\n\n\nAuthorised Signatory", regularFont));
		sign.setColspan(5);
		mainTable.addCell(sign);

		PdfPCell footer = new PdfPCell(new Phrase("This is computer generated invoice", regularFont));
		footer.setColspan(9);
		mainTable.addCell(footer);

		mainTable.setFooterRows(1);

		doc.add(mainTable);

	}
	public BigInteger calculate(Double num) {
		BigInteger value=	new BigDecimal(num).toBigInteger();
		return value;
		
	}
	
}