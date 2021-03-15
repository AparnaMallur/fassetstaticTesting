package com.fasset.pdf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasset.entities.Company;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.PurchaseEntryProductEntityClass;
import com.fasset.entities.User;
import com.fasset.form.GSTDetails;
import com.fasset.form.PurchaseForm;
import com.fasset.service.CommonServiceImpl;
import com.fasset.service.interfaces.ICommonService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@SessionAttributes("user")
public class PDFBuilderForPurchase extends AbstractITextPdfView {

	private CommonServiceImpl commonService = new CommonServiceImpl();
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"Purchase Invoice.pdf\"");
		
		HttpSession session = request.getSession(true);
		User user = new User();
		user = (User)session.getAttribute("user");
		
		PurchaseForm form = (PurchaseForm) model.get("form");
		PurchaseEntry entry = new PurchaseEntry();
		List<PurchaseEntryProductEntityClass> suppilerproductList= new ArrayList<PurchaseEntryProductEntityClass>();
		entry = form.getPurchaseEntry();
		suppilerproductList=form.getSuppilerproductList();
				
		Integer count = 1;
		
		Company comp = new Company();
		comp = user.getCompany();

		Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
		Font regularFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
				
		Paragraph par = new Paragraph(comp.getCompany_name(),boldFont);
		par.setAlignment(Paragraph.ALIGN_CENTER);
		
		doc.setPageSize(PageSize.A4);
		doc.open();
		
		doc.add(par); 

		PdfPTable mainTable = new PdfPTable(9);
		mainTable.setWidthPercentage(100.0f);
		mainTable.setWidths(new float[] { 3.0f,15.0f,7.0f,6.0f,5.0f,4.0f,4.0f,8.0f,10.0f });
		mainTable.setSpacingBefore(10);
		//mainTable.getDefaultCell().setBorder(0);
		mainTable.getDefaultCell().setMinimumHeight(25);
		mainTable.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setPadding(5);
		
		PdfPCell cellRight = new PdfPCell();
		cellRight.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		
		PdfPCell date = new PdfPCell(new Phrase("Date : ",regularFont));
		date.setColspan(2);
		date.setBorder(0);
		mainTable.addCell(date);
		
		PdfPCell dateValue = new PdfPCell(new Phrase(ClasstoConvertDateformat.dateFormat(entry.getCreated_date().toString()),boldFont));
		dateValue.setColspan(7);
		dateValue.setBorder(0);
		mainTable.addCell(dateValue);
		
		PdfPCell purchaseNum = new PdfPCell(new Phrase("Purchase voucher no. : ",regularFont));
		purchaseNum.setColspan(2);
		purchaseNum.setBorder(0);
		mainTable.addCell(purchaseNum);
		
		PdfPCell purchaseNumValue = new PdfPCell(new Phrase(entry.getVoucher_no(),boldFont));
		purchaseNumValue.setColspan(7);
		purchaseNumValue.setBorder(0);
		mainTable.addCell(purchaseNumValue);
		
		PdfPCell invoiceNum = new PdfPCell(new Phrase("Supplier invoice no. : ",regularFont));
		invoiceNum.setColspan(2);
		invoiceNum.setBorder(0);
		mainTable.addCell(invoiceNum);
		
		PdfPCell invoiceNumValue = new PdfPCell(new Phrase(entry.getSupplier_bill_no(),boldFont));
		invoiceNumValue.setColspan(7);
		invoiceNumValue.setBorder(0);
		mainTable.addCell(invoiceNumValue);
		
		PdfPCell partyName = new PdfPCell(new Phrase("Party A/c name : ",regularFont));
		partyName.setColspan(2);
		partyName.setBorder(0);
		mainTable.addCell(partyName);
		
		PdfPCell partyNameValue = new PdfPCell(new Phrase(entry.getSupplier().getCompany_name(),boldFont));
		partyNameValue.setColspan(7);
		partyNameValue.setBorder(0);
		mainTable.addCell(partyNameValue);
		
		/*PdfPCell currentBal = new PdfPCell(new Phrase("Current Balance : ",regularFont));
		currentBal.setColspan(2);
		currentBal.setBorder(0);
		mainTable.addCell(currentBal);
		
		Double currentBalance = entry.getSupplier().getOpeningbalances().getDebit_balance()-entry.getSupplier().getOpeningbalances().getCredit_balance();
		
		PdfPCell currentBalValue = new PdfPCell(new Phrase(currentBalance.toString() ,boldFont));
		currentBalValue.setColspan(7);
		currentBalValue.setBorder(0);
		mainTable.addCell(currentBalValue);*/
		
		PdfPCell purchaseLedger = new PdfPCell(new Phrase("Purchase ledger : ",regularFont));
		purchaseLedger.setColspan(2);
		purchaseLedger.setBorder(0);
		mainTable.addCell(purchaseLedger);
		
		PdfPCell purchaseLedgerValue = new PdfPCell(new Phrase(entry.getSubledger().getSubledger_name(),boldFont));
		purchaseLedgerValue.setColspan(7);
		purchaseLedgerValue.setBorder(0);
		mainTable.addCell(purchaseLedgerValue);		
		
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
		
		for(PurchaseEntryProductEntityClass obj:suppilerproductList){			
			mainTable.addCell(new Phrase(count.toString(),regularFont));
			mainTable.addCell(new Phrase(obj.getProduct_name(),regularFont));
			mainTable.addCell(new Phrase(obj.getHSNCode(),regularFont));
			mainTable.addCell(new Phrase(calculate(obj.getQuantity()).toString(),regularFont));
			cellRight.setPhrase(new Phrase(calculate(obj.getRate()).toString(),regularFont));
			mainTable.addCell(cellRight);
			mainTable.addCell(new Phrase(obj.getUOM(),regularFont));
			cellRight.setPhrase(new Phrase(obj.getDiscount().toString(),regularFont));
			mainTable.addCell(cellRight);
			Double other = obj.getFreight()+obj.getOthers()+obj.getLabour_charges();
			cellRight.setPhrase(new Phrase(calculate(other).toString(),regularFont));
			mainTable.addCell(cellRight);
			cellRight.setPhrase(new Phrase(calculate(obj.getTransaction_amount()).toString(),regularFont));
			mainTable.addCell(cellRight);
			total += obj.getTransaction_amount();
			count++;
			
				GSTDetails gstDetail = new GSTDetails();
				gstDetail.setHsnCode(obj.getHSNCode());
				if(obj.getCGST() != null){
					rate = (obj.getCGST()*100)/obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setCgstAmount(obj.getCGST());
					gstDetail.setCgstRate(rate);
				}
				else{
					gstDetail.setCgstAmount((double)0);
				}
				
				if(obj.getSGST() != null){
					rate = (obj.getSGST()*100)/obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setSgstAmount(obj.getSGST());
					gstDetail.setSgstRate(rate);
				}
				else{
					gstDetail.setSgstAmount((double)0);
				}
				if(obj.getIGST() != null){
					rate = (obj.getIGST()*100)/obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setIgstAmount(obj.getIGST());
					gstDetail.setIgstRate(rate);
				}
				else{
					gstDetail.setIgstAmount((double)0);
				}
				if(obj.getState_com_tax() != null){
					rate = (obj.getState_com_tax()*100)/obj.getTransaction_amount();
					rate = (Double) (Math.round(rate * 100.0) / 100.0);
					gstDetail.setCessAmount(obj.getState_com_tax());
					gstDetail.setCessRate(rate);
				}
				else{
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
			if(gstDetail.getCgstAmount() > 0){
				mainTable.addCell("");
				mainTable.addCell(new Phrase("CGST INPUT",regularFont));
				mainTable.addCell("");
				mainTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(gstDetail.getCgstRate()).toString(),regularFont));
				mainTable.addCell(cellRight);
				mainTable.addCell("%");
				mainTable.addCell("");
				mainTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(gstDetail.getCgstAmount()).toString(),regularFont));
				mainTable.addCell(cellRight);
			}
			if(gstDetail.getSgstAmount() > 0){
				mainTable.addCell("");
				mainTable.addCell(new Phrase("SGST INPUT",regularFont));
				mainTable.addCell("");
				mainTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(gstDetail.getSgstRate()).toString(),regularFont));
				mainTable.addCell(cellRight);
				mainTable.addCell("%");
				mainTable.addCell("");
				mainTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(gstDetail.getSgstAmount()).toString(),regularFont));
				mainTable.addCell(cellRight);
			}
			if(gstDetail.getIgstAmount() > 0){
				mainTable.addCell("");
				mainTable.addCell(new Phrase("IGST INPUT",regularFont));
				mainTable.addCell("");
				mainTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(gstDetail.getIgstRate()).toString(),regularFont));
				mainTable.addCell(cellRight);
				mainTable.addCell("%");
				mainTable.addCell("");
				mainTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(gstDetail.getIgstAmount()).toString(),regularFont));
				mainTable.addCell(cellRight);
			}
			if(gstDetail.getCessAmount() > 0){
				mainTable.addCell("");
				mainTable.addCell(new Phrase("CESS INPUT",regularFont));
				mainTable.addCell("");
				mainTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(gstDetail.getCessRate()).toString(),regularFont));
				mainTable.addCell(cellRight);
				mainTable.addCell("%");
				mainTable.addCell("");
				mainTable.addCell("");
				cellRight.setPhrase(new Phrase(calculate(gstDetail.getCessAmount()).toString(),regularFont));
				mainTable.addCell(cellRight);
			}
			if(gstDetail.getVatAmount() > 0){
				mainTable.addCell("");
				mainTable.addCell(new Phrase("VAT INPUT",regularFont));
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
				mainTable.addCell(new Phrase("CST INPUT",regularFont));
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
				mainTable.addCell(new Phrase("EXCISE INPUT",regularFont));
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
		mainTable.addCell(new Phrase("Total",regularFont));
		mainTable.addCell("");
		mainTable.addCell("");
		mainTable.addCell("");
		mainTable.addCell("");
		mainTable.addCell("");
		mainTable.addCell("");
		//cellRight.setPhrase(new Phrase(Double.toString((Double) (Math.round(total * 100.0) / 100.0)),boldFont));
		cellRight.setPhrase(new Phrase(calculate((Math.round(total * 100.0) / 100.0)).toString()));
		mainTable.addCell(cellRight);	
		
		PdfPCell totalAmount = new PdfPCell(new Phrase("Amount Chargable(In Words):\n"+commonService.convertDoubleNumberToWord((Double) (Math.round(total * 100.0) / 100.0)),regularFont));
		totalAmount.setColspan(9);
		totalAmount.setMinimumHeight(50);
		totalAmount.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		mainTable.addCell(totalAmount);			
		
		PdfPCell remark = new PdfPCell(new Phrase("Remark:\n\nBeing purchase made from "+entry.getSupplier().getCompany_name()+" against "+entry.getVoucher_no()+" dated "+ClasstoConvertDateformat.dateFormat(entry.getSupplier_bill_date().toString()),regularFont));
		remark.setColspan(9);
		mainTable.addCell(remark);
		
		/*PdfPCell footer = new PdfPCell(new Phrase("This is computer generated invoice",regularFont));
		footer.setColspan(9);
		mainTable.addCell(footer);	*/
		
		//mainTable.setFooterRows(1);
		
		doc.add(mainTable);
	}
	
	public BigInteger calculate(Double num) {
		BigInteger value=	new BigDecimal(num).toBigInteger();
		return value;
		
	}
}
