package com.fasset.pdf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasset.entities.PurchaseEntryProductEntityClass;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.form.SalesReportForm;
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

public class PDFBuilderForSalesEntry extends AbstractITextPdfView {

	private CommonServiceImpl commonService = new CommonServiceImpl();

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document doc, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		response.setHeader("Content-Disposition", "attachment; filename=\"Sales Entry Report.pdf\"");
		SalesReportForm form = (SalesReportForm) model.get("form");
		List<SalesEntry> salesEntryList = form.getSalesEntryList();
		Integer option = form.getOption();

		if (option == 2) {
			doc.setPageSize(PageSize.A3);
		} else {
			doc.setPageSize(PageSize.A4);
		}
		doc.open();
		if(form.getCompany()!=null)
		{
		doc.add(new Paragraph("Company Name:"+" "+form.getCompany().getCompany_name()));
		if(form.getCompany().getPermenant_address()!=null)
		{
		doc.add(new Paragraph("Address:"+" "+form.getCompany().getPermenant_address()));
		}
		if(form.getCompany().getRegistration_no()!=null)
		{
			if(form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
					|| form.getCompany().getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("14"))
			{
				doc.add(new Paragraph("CIN NO:"+" "+form.getCompany().getRegistration_no()));
			}
		}
		}
		doc.add(new Paragraph("Sales Entry Report"));

		if(form.getFromDate()!=null && form.getToDate()!=null)
		{
		doc.add(new Paragraph("From"+" "+ClasstoConvertDateformat.dateFormat(form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(form.getToDate().toString())));
		}
		
		if (salesEntryList != null && salesEntryList.size() > 0) {
			Double TotalCgst = (double)0;
			Double Totalsgst = (double)0;
			Double Totaligst = (double)0;
			Double Totalsct = (double)0;
			Double Totalvat = (double)0;
			Double Totalvatcst = (double)0;
			Double Totalexcise = (double)0;
			Double Totallabourcharges = (double)0;
			Double Totalfreight = (double)0;
			Double TotalOthers = (double)0;
			Double BasicAmount = (double)0;
			Double Taxablevalue = (double)0;
			Double InvoiceValue = (double)0;

			int newcount = 7;

			for (SalesEntry entry : salesEntryList) {
				
				if(entry.getProductinfoList()!=null && entry.getProductinfoList().size()>0)
				{
					for(SalesEntryProductEntityClass product : entry.getProductinfoList())
					{
						Double rate = (double)0;
						Double quantity = (double)0;
						Double discount = (double)0;
						Double freight = (double)0;
						Double others = (double)0;
						Double labourcharges = (double)0;
						Double basicAmount = (double)0;
						Double taxablevalue = (double)0;
						Double invoiceValue = (double)0;
						
						if(product.getRate()!=null && product.getRate()>0)
						{
							rate=product.getRate();
						}
						if(product.getQuantity()!=null && product.getQuantity()>0)
						{
							quantity=product.getQuantity();
						}
										
						if(product.getLabour_charges()!=null && product.getLabour_charges()>0)
						{
							if (Totallabourcharges == 0) {
								newcount = newcount + 1;
							}
							labourcharges=product.getLabour_charges();
							Totallabourcharges=Totallabourcharges+product.getLabour_charges();
						}
						
						if(product.getFreight()!=null && product.getFreight()>0)
						{
							if (Totalfreight == 0) {
								newcount = newcount + 1;
							}
							freight=product.getFreight();
							Totalfreight=Totalfreight+product.getFreight();
						}
						if(product.getOthers()!=null && product.getOthers()>0)
						{
						
								if (TotalOthers == 0) {
									newcount = newcount + 1;
								}
								others=others+product.getOthers();
								TotalOthers=TotalOthers+product.getOthers();
							
							
						}
						if(product.getDiscount()!=null && product.getDiscount()>0)
						{
							discount=product.getDiscount();
						}
						
						Double CGST= (double)0;
						Double SGST= (double)0;
						Double IGST= (double)0;
						Double VAT= (double)0;
						Double VATCST= (double)0;
						Double Excise= (double)0;
						
						if(product.getCGST()!=null && product.getCGST()>0)
						{
							CGST=product.getCGST();
						}
						
						if(product.getSGST()!=null && product.getSGST()>0)
						{
							SGST=product.getSGST();
						}
						if(product.getIGST()!=null && product.getIGST()>0)
						{
							IGST=product.getIGST();
						}
				
						if(product.getVAT()!=null && product.getVAT()>0)
						{
							VAT=product.getVAT();
						}
						if(product.getVATCST()!=null && product.getVATCST()>0)
						{
							VATCST=product.getVATCST();
						}
						if(product.getExcise()!=null && product.getExcise()>0)
						{
							Excise=product.getExcise();
						}
						
						
						basicAmount= ((rate*quantity)-(rate*quantity*discount)/100);
						
						taxablevalue= basicAmount+freight+labourcharges+others;
						
						invoiceValue= taxablevalue+CGST+SGST+IGST+VAT+VATCST+Excise;
						
												
						BasicAmount=BasicAmount+basicAmount;

						Taxablevalue=Taxablevalue+taxablevalue;
						
						InvoiceValue=InvoiceValue+invoiceValue;
					}
				}

				if ((entry.getCgst() != null) && (entry.getCgst() != 0.0)) {
					if (TotalCgst == 0) {
						newcount = newcount + 1;
					}
					TotalCgst = TotalCgst + entry.getCgst();

				}
				if ((entry.getSgst() != null) && (entry.getSgst() != 0.0)) {
					if (Totalsgst == 0) {
						newcount = newcount + 1;
					}
					Totalsgst = Totalsgst + entry.getSgst();

				}
				if ((entry.getIgst() != null) && (entry.getIgst() != 0.0)) {
					if (Totaligst == 0) {
						newcount = newcount + 1;
					}
					Totaligst = Totaligst + entry.getIgst();

				}
				if ((entry.getState_compansation_tax() != null) && (entry.getState_compansation_tax() != 0.0)) {
					if (Totalsct == 0) {
						newcount = newcount + 1;
					}
					Totalsct = Totalsct + entry.getState_compansation_tax();

				}
				if ((entry.getTotal_vat() != null) && (entry.getTotal_vat() != 0.0)) {
					if (Totalvat == 0) {
						newcount = newcount + 1;
					}
					Totalvat = Totalvat + entry.getTotal_vat();

				}
				if ((entry.getTotal_vatcst() != null) && (entry.getTotal_vatcst() != 0.0)) {
					if (Totalvatcst == 0) {
						newcount = newcount + 1;
					}
					Totalvatcst = Totalvatcst + entry.getTotal_vatcst();

				}
				if ((entry.getTotal_excise() != null) && (entry.getTotal_excise() != 0.0)) {
					if (Totalexcise == 0) {
						newcount = newcount + 1;
					}
					Totalexcise = Totalexcise + entry.getTotal_excise();

				}
			}

			if (option == 2) {

				PdfPTable table = new PdfPTable(newcount);
				table.setWidthPercentage(100.0f);
				table.setSpacingBefore(10);

				Font font = FontFactory.getFont(FontFactory.HELVETICA);
				font.setColor(BaseColor.WHITE);

				Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

				PdfPCell cell = new PdfPCell();
				cell.setBackgroundColor(BaseColor.BLACK);
				cell.setPadding(5);

				cell.setPhrase(new Phrase("Created Date", font));
				table.addCell(cell);

				cell.setPhrase(new Phrase("Voucher Number", font));
				table.addCell(cell);
				
				cell.setPhrase(new Phrase("Particulars", font));
				table.addCell(cell);

				cell.setPhrase(new Phrase("Income Type", font));
				table.addCell(cell);
				
				cell.setPhrase(new Phrase("Basic Amount", font));
				table.addCell(cell);
				
				if ((Totalfreight != 0.0) && (Totalfreight != 0) && (Totalfreight != null)) {
					cell.setPhrase(new Phrase("Freight", font));
					table.addCell(cell);
				}
				
				if ((Totallabourcharges != 0.0) && (Totallabourcharges != 0) && (Totallabourcharges != null)) {
					cell.setPhrase(new Phrase("Labour Charges", font));
					table.addCell(cell);
				}

				if ((TotalOthers != 0.0) && (TotalOthers != 0) && (TotalOthers != null)) {
					cell.setPhrase(new Phrase("Others", font));
					table.addCell(cell);
				}
				
				cell.setPhrase(new Phrase("Taxable value", font));
				table.addCell(cell);
				
				if ((TotalCgst != 0.0) && (TotalCgst != 0) && (TotalCgst != null)) {
					cell.setPhrase(new Phrase("CGST", font));
					table.addCell(cell);
				}
				if ((Totalsgst != 0.0) && (Totalsgst != 0) && (Totalsgst != null)) {
					cell.setPhrase(new Phrase("SGST", font));
					table.addCell(cell);
				}
				if ((Totaligst != 0.0) && (Totaligst != 0) && (Totaligst != null)) {
					cell.setPhrase(new Phrase("IGST", font));
					table.addCell(cell);
				}
				if ((Totalsct != 0.0) && (Totalsct != 0) && (Totalsct != null)) {
					cell.setPhrase(new Phrase("SCT", font));
					table.addCell(cell);
				}
				if ((Totalvat != 0.0) && (Totalvat != 0) && (Totalvat != null)) {
					cell.setPhrase(new Phrase("VAT", font));
					table.addCell(cell);
				}
				if ((Totalvatcst != 0.0) && (Totalvatcst != 0) && (Totalvatcst != null)) {
					cell.setPhrase(new Phrase("VATCST", font));
					table.addCell(cell);
				}
				if ((Totalexcise != 0.0) && (Totalexcise != 0) && (Totalexcise != null)) {
					cell.setPhrase(new Phrase("Excise", font));
					table.addCell(cell);
				}

				cell.setPhrase(new Phrase("Invoice Value", font));
				table.addCell(cell);

				for (SalesEntry entry : salesEntryList) {

					if(entry.getSale_type()!=null)
					{
					if (entry.getCreated_date() != null) {
						table.addCell(ClasstoConvertDateformat.dateFormat(entry.getCreated_date().toString()));
					} else {
						table.addCell("");
					}
					
					if (entry.getVoucher_no() != null) {
						table.addCell(entry.getVoucher_no());
					} else {
						table.addCell("");
					}

					if (entry.getSale_type() == 1) {
						table.addCell("Cash Sales");
					}else if(entry.getSale_type() == 2) {
						if(entry.getBank()!=null)
						{
						table.addCell("Card Sales"/*+entry.getBank().getBank_name()+" - "+entry.getBank().getAccount_no()*/);
						}
						else
						{
							table.addCell("Card Sales");
						}
						
					}else if (entry.getCustomer() != null) {
						table.addCell(entry.getCustomer().getFirm_name());
					}
					else
					{
						table.addCell(" ");
					}

					if (entry.getSubledger() != null) {
						table.addCell(entry.getSubledger().getSubledger_name());
					} else {
						table.addCell("");
					}

					//----------------------------- basic amount
					 Double basicamount = (double)0;
						if(entry.getProductinfoList()!=null && entry.getProductinfoList().size()>0)
						{
							for(SalesEntryProductEntityClass product : entry.getProductinfoList())
							{
								
								Double rate = (double)0;
								Double quantity = (double)0;
								Double discount = (double)0;
								
								if(product.getRate()!=null && product.getRate()>0)
								{
									rate=rate+product.getRate();
								}
								if(product.getQuantity()!=null && product.getQuantity()>0)
								{
									quantity=quantity+product.getQuantity();
								}
							
								if(product.getDiscount()!=null && product.getDiscount()>0)
								{
									discount=discount+product.getDiscount();
								}	
								
								basicamount = basicamount + ((rate*quantity)-(rate*quantity*discount)/100);
							}
							table.addCell(basicamount.toString());
						}
						else
						{
							table.addCell(basicamount.toString());
						}

						//--------------------------------------------freight
						Double freight = (double)0;
						if(Totalfreight>0) {
					    if(entry.getProductinfoList()!=null && entry.getProductinfoList().size()>0)
						{
							for(SalesEntryProductEntityClass product : entry.getProductinfoList())
							{
								
								
								if(product.getFreight()!=null && product.getFreight()>0)
								{
									freight=freight+product.getFreight();
								}
								
							}
							table.addCell(freight.toString());
						}
						else
						{
							table.addCell(freight.toString());
						}
					   }	
						
						//--------------------------Totallabourcharges
						
						
						Double labourcharges = (double)0;
						if(Totallabourcharges>0) {
							if(entry.getProductinfoList()!=null && entry.getProductinfoList().size()>0)
							{
								for(SalesEntryProductEntityClass product : entry.getProductinfoList())
								{
									if(product.getLabour_charges()!=null && product.getLabour_charges()>0)
									{
										labourcharges=labourcharges+product.getLabour_charges();
									}
									
							     }
								table.addCell(labourcharges.toString());
							}
							else
							{
								table.addCell(labourcharges.toString());
							}

						}
							
							//-------------------------------------total others
						Double others = (double)0;
	                   if(TotalOthers>0) {
							if(entry.getProductinfoList()!=null && entry.getProductinfoList().size()>0)
							{
								for(SalesEntryProductEntityClass product : entry.getProductinfoList())
								{
									if(product.getOthers()!=null && product.getOthers()>0)
									{
										others=others+product.getOthers();
									}
									
								}
								table.addCell(others.toString());
							}
							else
							{
								table.addCell(others.toString());
							}

						}
						
						//----------------------------taxable value
	                   
	                   Double taxablevalue = (double)0;
	                  
							if(entry.getProductinfoList()!=null && entry.getProductinfoList().size()>0)
							{
									taxablevalue = basicamount + freight +labourcharges+others ;
								
								table.addCell(taxablevalue.toString());
							}
							else
							{
								table.addCell(taxablevalue.toString());
							}

						   
	                   
	                  
							
						if ((TotalCgst != 0.0) && (TotalCgst != 0) && (TotalCgst != null)) {

							if (entry.getCgst() != null && entry.getCgst() > 0) {
								table.addCell(entry.getCgst().toString());
							} else {
								table.addCell("0");
							}
						}
						if ((Totalsgst != 0.0) && (Totalsgst != 0) && (Totalsgst != null)) {
							if (entry.getSgst() != null && entry.getSgst() > 0) {
								table.addCell(entry.getSgst().toString());
							} else {
								table.addCell("0");
							}
						}
						if ((Totaligst != 0.0) && (Totaligst != 0) && (Totaligst != null)) {
							if (entry.getIgst() != null && entry.getIgst() > 0) {
								table.addCell(entry.getIgst().toString());
							} else {
								table.addCell("0");
							}
						}
						if ((Totalsct != 0.0) && (Totalsct != 0) && (Totalsct != null)) {
							if (entry.getState_compansation_tax() != null && entry.getState_compansation_tax() > 0) {
								table.addCell(entry.getState_compansation_tax().toString());
							} else {
								table.addCell("0");
							}
						}
						if ((Totalvat != 0.0) && (Totalvat != 0) && (Totalvat != null)) {
							if (entry.getTotal_vat() != null && entry.getTotal_vat() > 0) {
								table.addCell(entry.getTotal_vat().toString());
							} else {
								table.addCell("0");
							}
						}
						if ((Totalvatcst != 0.0) && (Totalvatcst != 0) && (Totalvatcst != null)) {
							if (entry.getTotal_vatcst() != null && entry.getTotal_vatcst() > 0) {
								table.addCell(entry.getTotal_vatcst().toString());
							} else {
								table.addCell("0");
							}
						}
						if ((Totalexcise != 0.0) && (Totalexcise != 0) && (Totalexcise != null)) {
							if (entry.getTotal_excise() != null && entry.getTotal_excise() > 0) {
								table.addCell(entry.getTotal_excise().toString());
							} else {
								table.addCell("0");
							}
						}
						//------------------InvoiceValue------------
						
						Double invoiceValue = (double)0;
		                  
								if(entry.getProductinfoList()!=null && entry.getProductinfoList().size()>0)
								{
									Double CGST= (double)0;
									Double SGST= (double)0;
									Double IGST= (double)0;
									Double VAT= (double)0;
									Double VATCST= (double)0;
									Double Excise= (double)0;
									
									for(SalesEntryProductEntityClass product : entry.getProductinfoList())
									{
										if(product.getCGST()!=null && product.getCGST()>0)
										{
											CGST=CGST+product.getCGST();
										}
										
										if(product.getSGST()!=null && product.getSGST()>0)
										{
											SGST=SGST+product.getSGST();
										}
										if(product.getIGST()!=null && product.getIGST()>0)
										{
											IGST=IGST+product.getIGST();
										}
								
										if(product.getVAT()!=null && product.getVAT()>0)
										{
											VAT=VAT+product.getVAT();
										}
										if(product.getVATCST()!=null && product.getVATCST()>0)
										{
											VATCST=VATCST+product.getVATCST();
										}
										if(product.getExcise()!=null && product.getExcise()>0)
										{
											Excise=Excise+product.getExcise();
										}
										
										
										
									}
									invoiceValue=taxablevalue+CGST+SGST+IGST+VAT+VATCST+Excise;
									table.addCell(invoiceValue.toString());
								}
								else
								{
									table.addCell(invoiceValue.toString());
								}
				
		        	
				}

				}
				
				if (newcount == 7) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}
				if (newcount == 8) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					if (Totalfreight > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalfreight, 3).toString(), boldFont));
					}
					if (Totallabourcharges > 0) {

						table.addCell(new Phrase(commonService.round((double)Totallabourcharges, 3).toString(), boldFont));
					}

					if (TotalOthers > 0) {

						table.addCell(new Phrase(commonService.round((double)TotalOthers, 3).toString(), boldFont));
					}
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					if (TotalCgst > 0) {
						table.addCell(new Phrase(commonService.round((double)TotalCgst, 3).toString(), boldFont));
					}
					if (Totalsgst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsgst, 3).toString(), boldFont));
					}
					if (Totaligst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totaligst, 3).toString(), boldFont));
					}
					if (Totalsct > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsct, 3).toString(), boldFont));
					}
					if (Totalvat > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvat, 3).toString(), boldFont));
					}
					if (Totalvatcst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvatcst, 3).toString(), boldFont));
					}
					if (Totalexcise > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalexcise, 3).toString(), boldFont));
					}
					
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}

				if (newcount == 9) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					if (Totalfreight > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalfreight, 3).toString(), boldFont));
					}
					if (Totallabourcharges > 0) {

						table.addCell(new Phrase(commonService.round((double)Totallabourcharges, 3).toString(), boldFont));
					}

					if (TotalOthers > 0) {

						table.addCell(new Phrase(commonService.round((double)TotalOthers, 3).toString(), boldFont));
					}
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					if (TotalCgst > 0) {
						table.addCell(new Phrase(commonService.round((double)TotalCgst, 3).toString(), boldFont));
					}
					if (Totalsgst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsgst, 3).toString(), boldFont));
					}
					if (Totaligst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totaligst, 3).toString(), boldFont));
					}
					if (Totalsct > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsct, 3).toString(), boldFont));
					}
					if (Totalvat > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvat, 3).toString(), boldFont));
					}
					if (Totalvatcst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvatcst, 3).toString(), boldFont));
					}
					if (Totalexcise > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalexcise, 3).toString(), boldFont));
					}
					
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}

				if (newcount == 10) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					if (Totalfreight > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalfreight, 3).toString(), boldFont));
					}
					if (Totallabourcharges > 0) {

						table.addCell(new Phrase(commonService.round((double)Totallabourcharges, 3).toString(), boldFont));
					}

					if (TotalOthers > 0) {

						table.addCell(new Phrase(commonService.round((double)TotalOthers, 3).toString(), boldFont));
					}
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					if (TotalCgst > 0) {
						table.addCell(new Phrase(commonService.round((double)TotalCgst, 3).toString(), boldFont));
					}
					if (Totalsgst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsgst, 3).toString(), boldFont));
					}
					if (Totaligst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totaligst, 3).toString(), boldFont));
					}
					if (Totalsct > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsct, 3).toString(), boldFont));
					}
					if (Totalvat > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvat, 3).toString(), boldFont));
					}
					if (Totalvatcst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvatcst, 3).toString(), boldFont));
					}
					if (Totalexcise > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalexcise, 3).toString(), boldFont));
					}
					
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}
				if (newcount == 11) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					if (Totalfreight > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalfreight, 3).toString(), boldFont));
					}
					if (Totallabourcharges > 0) {

						table.addCell(new Phrase(commonService.round((double)Totallabourcharges, 3).toString(), boldFont));
					}

					if (TotalOthers > 0) {

						table.addCell(new Phrase(commonService.round((double)TotalOthers, 3).toString(), boldFont));
					}
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					if (TotalCgst > 0) {
						table.addCell(new Phrase(commonService.round((double)TotalCgst, 3).toString(), boldFont));
					}
					if (Totalsgst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsgst, 3).toString(), boldFont));
					}
					if (Totaligst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totaligst, 3).toString(), boldFont));
					}
					if (Totalsct > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsct, 3).toString(), boldFont));
					}
					if (Totalvat > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvat, 3).toString(), boldFont));
					}
					if (Totalvatcst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvatcst, 3).toString(), boldFont));
					}
					if (Totalexcise > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalexcise, 3).toString(), boldFont));
					}
					
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}
				if (newcount == 12) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					if (Totalfreight > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalfreight, 3).toString(), boldFont));
					}
					if (Totallabourcharges > 0) {

						table.addCell(new Phrase(commonService.round((double)Totallabourcharges, 3).toString(), boldFont));
					}

					if (TotalOthers > 0) {

						table.addCell(new Phrase(commonService.round((double)TotalOthers, 3).toString(), boldFont));
					}
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					if (TotalCgst > 0) {
						table.addCell(new Phrase(commonService.round((double)TotalCgst, 3).toString(), boldFont));
					}
					if (Totalsgst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsgst, 3).toString(), boldFont));
					}
					if (Totaligst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totaligst, 3).toString(), boldFont));
					}
					if (Totalsct > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsct, 3).toString(), boldFont));
					}
					if (Totalvat > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvat, 3).toString(), boldFont));
					}
					if (Totalvatcst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvatcst, 3).toString(), boldFont));
					}
					if (Totalexcise > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalexcise, 3).toString(), boldFont));
					}
					
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}
				if (newcount == 13) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					if (Totalfreight > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalfreight, 3).toString(), boldFont));
					}
					if (Totallabourcharges > 0) {

						table.addCell(new Phrase(commonService.round((double)Totallabourcharges, 3).toString(), boldFont));
					}

					if (TotalOthers > 0) {

						table.addCell(new Phrase(commonService.round((double)TotalOthers, 3).toString(), boldFont));
					}
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					if (TotalCgst > 0) {
						table.addCell(new Phrase(commonService.round((double)TotalCgst, 3).toString(), boldFont));
					}
					if (Totalsgst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsgst, 3).toString(), boldFont));
					}
					if (Totaligst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totaligst, 3).toString(), boldFont));
					}
					if (Totalsct > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsct, 3).toString(), boldFont));
					}
					if (Totalvat > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvat, 3).toString(), boldFont));
					}
					if (Totalvatcst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvatcst, 3).toString(), boldFont));
					}
					if (Totalexcise > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalexcise, 3).toString(), boldFont));
					}
					
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}
				if (newcount == 14) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					if (Totalfreight > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalfreight, 3).toString(), boldFont));
					}
					if (Totallabourcharges > 0) {

						table.addCell(new Phrase(commonService.round((double)Totallabourcharges, 3).toString(), boldFont));
					}

					if (TotalOthers > 0) {

						table.addCell(new Phrase(commonService.round((double)TotalOthers, 3).toString(), boldFont));
					}
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					if (TotalCgst > 0) {
						table.addCell(new Phrase(commonService.round((double)TotalCgst, 3).toString(), boldFont));
					}
					if (Totalsgst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsgst, 3).toString(), boldFont));
					}
					if (Totaligst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totaligst, 3).toString(), boldFont));
					}
					if (Totalsct > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsct, 3).toString(), boldFont));
					}
					if (Totalvat > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvat, 3).toString(), boldFont));
					}
					if (Totalvatcst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvatcst, 3).toString(), boldFont));
					}
					if (Totalexcise > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalexcise, 3).toString(), boldFont));
					}
					
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}

				if (newcount == 15) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					if (Totalfreight > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalfreight, 3).toString(), boldFont));
					}
					if (Totallabourcharges > 0) {

						table.addCell(new Phrase(commonService.round((double)Totallabourcharges, 3).toString(), boldFont));
					}

					if (TotalOthers > 0) {

						table.addCell(new Phrase(commonService.round((double)TotalOthers, 3).toString(), boldFont));
					}
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					if (TotalCgst > 0) {
						table.addCell(new Phrase(commonService.round((double)TotalCgst, 3).toString(), boldFont));
					}
					if (Totalsgst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsgst, 3).toString(), boldFont));
					}
					if (Totaligst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totaligst, 3).toString(), boldFont));
					}
					if (Totalsct > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsct, 3).toString(), boldFont));
					}
					if (Totalvat > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvat, 3).toString(), boldFont));
					}
					if (Totalvatcst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvatcst, 3).toString(), boldFont));
					}
					if (Totalexcise > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalexcise, 3).toString(), boldFont));
					}
					
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}
				
				if (newcount == 16) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					if (Totalfreight > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalfreight, 3).toString(), boldFont));
					}
					if (Totallabourcharges > 0) {

						table.addCell(new Phrase(commonService.round((double)Totallabourcharges, 3).toString(), boldFont));
					}

					if (TotalOthers > 0) {

						table.addCell(new Phrase(commonService.round((double)TotalOthers, 3).toString(), boldFont));
					}
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					if (TotalCgst > 0) {
						table.addCell(new Phrase(commonService.round((double)TotalCgst, 3).toString(), boldFont));
					}
					if (Totalsgst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsgst, 3).toString(), boldFont));
					}
					if (Totaligst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totaligst, 3).toString(), boldFont));
					}
					if (Totalsct > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsct, 3).toString(), boldFont));
					}
					if (Totalvat > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvat, 3).toString(), boldFont));
					}
					if (Totalvatcst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvatcst, 3).toString(), boldFont));
					}
					if (Totalexcise > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalexcise, 3).toString(), boldFont));
					}
					
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}
				
				if (newcount == 17) {
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell("");
					table.addCell(new Phrase(commonService.round((double)BasicAmount, 3).toString(), boldFont));
					if (Totalfreight > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalfreight, 3).toString(), boldFont));
					}
					if (Totallabourcharges > 0) {

						table.addCell(new Phrase(commonService.round((double)Totallabourcharges, 3).toString(), boldFont));
					}

					if (TotalOthers > 0) {

						table.addCell(new Phrase(commonService.round((double)TotalOthers, 3).toString(), boldFont));
					}
					table.addCell(new Phrase(commonService.round((double)Taxablevalue, 3).toString(), boldFont));
					if (TotalCgst > 0) {
						table.addCell(new Phrase(commonService.round((double)TotalCgst, 3).toString(), boldFont));
					}
					if (Totalsgst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsgst, 3).toString(), boldFont));
					}
					if (Totaligst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totaligst, 3).toString(), boldFont));
					}
					if (Totalsct > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalsct, 3).toString(), boldFont));
					}
					if (Totalvat > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvat, 3).toString(), boldFont));
					}
					if (Totalvatcst > 0) {
						table.addCell(new Phrase(commonService.round((double)Totalvatcst, 3).toString(), boldFont));
					}
					if (Totalexcise > 0) {

						table.addCell(new Phrase(commonService.round((double)Totalexcise, 3).toString(), boldFont));
					}
					
					table.addCell(new Phrase(commonService.round((double)InvoiceValue, 3).toString(), boldFont));
				}
				doc.add(table);
			}

			else {

				PdfPTable table = new PdfPTable(5);
				table.setWidthPercentage(100.0f);
				table.setWidths(new float[] { 2.0f, 2.0f, 2.0f, 2.0f,2.0f });
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

				cell.setPhrase(new Phrase("Voucher Type", font));
				table.addCell(cell);
				
				cell.setPhrase(new Phrase("Credit", font));
				table.addCell(cell);

				
				Double Totalcredit = (double)0;
				
				for (SalesEntry entry : salesEntryList) {

					if(entry.getSale_type()!=null)
					{
					
					if (entry.getCreated_date() != null) {
						table.addCell(ClasstoConvertDateformat.dateFormat(entry.getCreated_date().toString()));
					} else {
						table.addCell("");
					}
					
					if (entry.getVoucher_no() != null) {
						table.addCell(entry.getVoucher_no());
					} else {
						table.addCell("");
					}
					
					if (entry.getSale_type() == 1) {
						table.addCell("Cash Sales");
					}else if(entry.getSale_type() == 2) {
						if(entry.getBank()!=null)
						{
						table.addCell("Card Sales - "+entry.getBank().getBank_name()+" - "+entry.getBank().getAccount_no());
						}
						else
						{
							table.addCell("Card Sales");
						}
						
					}else if (entry.getCustomer() != null) {
						table.addCell(entry.getCustomer().getFirm_name());
					}
					
					table.addCell("Sales");

					if (entry.getRound_off() != null) {
						table.addCell(entry.getRound_off().toString());
						Totalcredit=Totalcredit+entry.getRound_off();
					} else {
						table.addCell("");
					}
					
					}
					

				}
				table.addCell("");
				table.addCell("");
				table.addCell("");
				table.addCell("");


				if (Totalcredit > 0) {
					table.addCell(new Phrase(commonService.round((double)Totalcredit, 3).toString(), boldFont));
				} else {
					table.addCell("");
				}
				doc.add(table);
			}
		}

		else {
			doc.setPageSize(PageSize.A3);
			PdfPTable table = new PdfPTable(14);
			table.setWidthPercentage(100.0f);
			table.setWidths(new float[] { 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f,
					20.0f, 20.0f, 20.0f });
			table.setSpacingBefore(10);

			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(BaseColor.WHITE);

			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(BaseColor.BLACK);
			cell.setPadding(5);

			cell.setPhrase(new Phrase("Date", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Particulars", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Voucher Type", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Voucher Number", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Credit", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("CGST", font));

			cell.setPhrase(new Phrase("SGST", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("IGST", font));

			cell.setPhrase(new Phrase("SCT", font));

			cell.setPhrase(new Phrase("Total VAT", font));

			cell.setPhrase(new Phrase("Total VATCST", font));

			cell.setPhrase(new Phrase("Total Excise", font));

			cell.setPhrase(new Phrase("Total TDS", font));

			cell.setPhrase(new Phrase("Debit", font));
			table.addCell(cell);

			doc.add(table);
		}
	}
}