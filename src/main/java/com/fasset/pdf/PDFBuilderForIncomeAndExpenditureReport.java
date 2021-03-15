package com.fasset.pdf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasset.form.LedgerForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.OpeningBalancesOfSubedgerForm;
import com.fasset.form.ProfitAndLossReportForm;
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

public class PDFBuilderForIncomeAndExpenditureReport extends AbstractITextPdfView{

	
	private CommonServiceImpl commonService = new CommonServiceImpl();

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"Income and Expenditure Report.pdf\"");

		ProfitAndLossReportForm form = (ProfitAndLossReportForm) model.get("form");
		double totalDebitSideAmount = (double) 0;
		double totalCreditSideAmount = (double) 0;
		int debitCountforAdujustingRow = 0;
		int creditCountforAdujustingRow = 0;
		
		List<OpeningBalancesOfSubedgerForm> subOpeningList=form.getListForOpeningbalancesOfsubledger();
		List<OpeningBalancesOfSubedgerForm> subOpeningListBeforeStartDate = form.getListForOpeningbalancesbeforestartDate();
		for(OpeningBalancesOfSubedgerForm obalance : subOpeningList)
		{
			if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)1))
			{
				for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
				{
					if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
					{
						totalDebitSideAmount=totalDebitSideAmount+((obalanceBeStartDate.getTotaldebit_balance()-obalanceBeStartDate.getTotalcredit_balance())+(obalance.getTotaldebit_balance()-obalance.getTotalcredit_balance()));
					}
				}
			}
			if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)2))
			{
				for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
				{
					if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
					{
						totalCreditSideAmount=totalCreditSideAmount+((obalanceBeStartDate.getTotalcredit_balance()-obalanceBeStartDate.getTotaldebit_balance())+(obalance.getTotalcredit_balance()-obalance.getTotaldebit_balance()));
					}
				}
			}
		}
		if (subOpeningList.size() > 0) {
			
              for (OpeningBalancesOfSubedgerForm obalance : subOpeningList) {
				
				if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)1))
				{
					double closingBalance = (double) 0;
				
					for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
					{
						if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
						{
							closingBalance=closingBalance+((obalanceBeStartDate.getTotaldebit_balance()-obalanceBeStartDate.getTotalcredit_balance())+(obalance.getTotaldebit_balance()-obalance.getTotalcredit_balance()));
						}
					}
					
					if(closingBalance!=0)
					{
						debitCountforAdujustingRow=debitCountforAdujustingRow+1;
						if(obalance.getAccountSubGroupNameList().size()>0)
						{
							for(String accSubGroup : obalance.getAccountSubGroupNameList())
							{
								double closingBalanceOfSubgroup = (double) 0;
								double total_credit_Subgroup = (double) 0;
								double total_debit_Subgroup = (double) 0;
								double total_credit_Subgroup_beforeStartDate = (double) 0;
								double total_debit_Subgroup_beforeStartDate = (double) 0;
								
								for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
								{
									if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
									{
										for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
										{
											if(subGroupName.equals(accSubGroup))
											{
												if(obalanceBeStartDate.getLedgerformlist().size()>0)
												{
													for(LedgerForm ledgerform1 : obalanceBeStartDate.getLedgerformlist())
													{
														if(ledgerform1.getSubgroupName().equals(accSubGroup))
														{
															total_credit_Subgroup_beforeStartDate=total_credit_Subgroup_beforeStartDate+ledgerform1.getLedgercredit_balance();
															total_debit_Subgroup_beforeStartDate=total_debit_Subgroup_beforeStartDate+ledgerform1.getLedgerdebit_balance();
														}
													}
												}
											}
										}
									}
								}
								if(obalance.getLedgerformlist().size()>0)
								{
									for(LedgerForm ledgerform2 : obalance.getLedgerformlist())
									{
										if(ledgerform2.getSubgroupName().equals(accSubGroup))
										{
											total_credit_Subgroup=total_credit_Subgroup+ledgerform2.getLedgercredit_balance();
											total_debit_Subgroup=total_debit_Subgroup+ledgerform2.getLedgerdebit_balance();
										}
									}
								}
								
								closingBalanceOfSubgroup=closingBalanceOfSubgroup+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup);
								if(closingBalanceOfSubgroup!=0)
								{
									debitCountforAdujustingRow=debitCountforAdujustingRow+1;
									if(obalance.getLedgerformlist().size()>0)
									{
										for(LedgerForm ledgerform3 : obalance.getLedgerformlist())
										{
											if(ledgerform3.getSubgroupName().equals(accSubGroup))
											{
												double openiBalanceOfLedger = (double) 0;
												double total_debitOfLedger = (double) 0;
												double total_creditOfLedger = (double) 0;
												for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
												{
													if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
													{
														for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
														{
															if(subGroupName.equals(accSubGroup))
															{
																if(obalanceBeStartDate.getLedgerformlist().size()>0)
																{
																	for(LedgerForm ledgerform4 : obalanceBeStartDate.getLedgerformlist())
																	{
																		if(ledgerform4.getLedgerName().equals(ledgerform3.getLedgerName()))
																		{
																			if(ledgerform4.getSubgroupName().equals(accSubGroup))
																			{
																				openiBalanceOfLedger=openiBalanceOfLedger+(ledgerform4.getLedgerdebit_balance()-ledgerform4.getLedgercredit_balance());
																			}
																		}
																	}
																}
															}
														}
													}
												}
												
												total_debitOfLedger=total_debitOfLedger+ledgerform3.getLedgerdebit_balance();
												total_creditOfLedger=total_creditOfLedger+ledgerform3.getLedgercredit_balance();
												
												if(openiBalanceOfLedger!=0 || total_debitOfLedger!=0 || total_creditOfLedger!=0)
												{
													debitCountforAdujustingRow=debitCountforAdujustingRow+1;
												}
											}
											
											if(ledgerform3.getSubledgerList().size()>0)
											{
												if(ledgerform3.getSubgroupName().equals(accSubGroup))
												{
													for(OpeningBalancesForm subledger : ledgerform3.getSubledgerList())
													{
														double openiBalanceOfSubLedger = (double) 0;
														double total_debitOfSubLedger = (double) 0;
														double total_creditOfSubLedger = (double) 0;
														for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
														{
															if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
															{
																for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
																{
																	if(subGroupName.equals(accSubGroup))
																	{
																		for(LedgerForm ledgerform5 : obalanceBeStartDate.getLedgerformlist())
																		{
																			if(ledgerform5.getSubgroupName().equals(accSubGroup))
																			{
																				for(OpeningBalancesForm subledger1 : ledgerform5.getSubledgerList())
																				{
																					if(subledger1.getSubledgerName().equals(subledger.getSubledgerName()))
																					{
																						openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
														
														total_debitOfSubLedger=total_debitOfSubLedger+subledger.getDebit_balance();
														total_creditOfSubLedger=total_creditOfSubLedger+subledger.getCredit_balance();
														
														if(openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0)
														{
															debitCountforAdujustingRow=debitCountforAdujustingRow+1;
														}
													}
												}
											}
										}
									}
									
									for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
									{
										if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
										{
											for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
											{
												if(subGroupName.equals(accSubGroup))
												{
													
													for(LedgerForm ledgerform6 : obalanceBeStartDate.getLedgerformlist())
													{
														int isNotPresent = 0;
														int isPresent = 0;
														
														if(ledgerform6.getSubgroupName().equals(accSubGroup))
														{
															if(obalance.getLedgerformlist().size()>0)
															{
																for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
																{
																	if(ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																	{
																		isPresent=1;
																	}
																}
																
																if(isPresent==0) 
																{
																	if(obalance.getLedgerformlist().size()>0)
																	{
																		for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
																		{
																			if(!ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																			{
																				if(isNotPresent==0)
																				{
																					debitCountforAdujustingRow=debitCountforAdujustingRow+1;
																					
																					if(ledgerform6.getSubledgerList().size()>0)
																					{
																						for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																						{
																							double openiBalanceOfSubLedger = (double) 0;
																							openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																							if(openiBalanceOfSubLedger!=0)
																							{
																								debitCountforAdujustingRow=debitCountforAdujustingRow+1;
																							}
																						}
																					}
																					isNotPresent=1;
																				}
																			}
																		}
																	}
																}
															}
															if(obalance.getLedgerformlist().size()==0)
															{
																debitCountforAdujustingRow=debitCountforAdujustingRow+1;
																if(ledgerform6.getSubledgerList().size()>0)
																{
																	for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																	{
																		double openiBalanceOfSubLedger = (double) 0;
																		openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																		if(openiBalanceOfSubLedger!=0)
																		{
																			debitCountforAdujustingRow=debitCountforAdujustingRow+1;
																		}
																	}
																}
															}
															
															
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				
				}
			}
			if(totalCreditSideAmount>totalDebitSideAmount)
			{
				debitCountforAdujustingRow=debitCountforAdujustingRow+1;
			}

			for (OpeningBalancesOfSubedgerForm obalance : subOpeningList) {
				
				if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)2))
				{
					double closingBalance = (double) 0;
				
					for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
					{
						if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
						{
							closingBalance=closingBalance+((obalanceBeStartDate.getTotalcredit_balance()-obalanceBeStartDate.getTotaldebit_balance())+(obalance.getTotalcredit_balance()-obalance.getTotaldebit_balance()));
						}
					}
					
					if(closingBalance!=0)
					{
						creditCountforAdujustingRow=creditCountforAdujustingRow+1;
						if(obalance.getAccountSubGroupNameList().size()>0)
						{
							for(String accSubGroup : obalance.getAccountSubGroupNameList())
							{
								double closingBalanceOfSubgroup = (double) 0;
								double total_credit_Subgroup = (double) 0;
								double total_debit_Subgroup = (double) 0;
								double total_credit_Subgroup_beforeStartDate = (double) 0;
								double total_debit_Subgroup_beforeStartDate = (double) 0;
								
								for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
								{
									if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
									{
										for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
										{
											if(subGroupName.equals(accSubGroup))
											{
												if(obalanceBeStartDate.getLedgerformlist().size()>0)
												{
													for(LedgerForm ledgerform1 : obalanceBeStartDate.getLedgerformlist())
													{
														if(ledgerform1.getSubgroupName().equals(accSubGroup))
														{
															total_credit_Subgroup_beforeStartDate=total_credit_Subgroup_beforeStartDate+ledgerform1.getLedgercredit_balance();
															total_debit_Subgroup_beforeStartDate=total_debit_Subgroup_beforeStartDate+ledgerform1.getLedgerdebit_balance();
														}
													}
												}
											}
										}
									}
								}
								if(obalance.getLedgerformlist().size()>0)
								{
									for(LedgerForm ledgerform2 : obalance.getLedgerformlist())
									{
										if(ledgerform2.getSubgroupName().equals(accSubGroup))
										{
											total_credit_Subgroup=total_credit_Subgroup+ledgerform2.getLedgercredit_balance();
											total_debit_Subgroup=total_debit_Subgroup+ledgerform2.getLedgerdebit_balance();
										}
									}
								}
								
								closingBalanceOfSubgroup=closingBalanceOfSubgroup+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup);
								if(closingBalanceOfSubgroup!=0)
								{
									creditCountforAdujustingRow=creditCountforAdujustingRow+1;
									if(obalance.getLedgerformlist().size()>0)
									{
										for(LedgerForm ledgerform3 : obalance.getLedgerformlist())
										{
											if(ledgerform3.getSubgroupName().equals(accSubGroup))
											{
												double openiBalanceOfLedger = (double) 0;
												double total_debitOfLedger = (double) 0;
												double total_creditOfLedger = (double) 0;
												for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
												{
													if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
													{
														for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
														{
															if(subGroupName.equals(accSubGroup))
															{
																if(obalanceBeStartDate.getLedgerformlist().size()>0)
																{
																	for(LedgerForm ledgerform4 : obalanceBeStartDate.getLedgerformlist())
																	{
																		if(ledgerform4.getLedgerName().equals(ledgerform3.getLedgerName()))
																		{
																			if(ledgerform4.getSubgroupName().equals(accSubGroup))
																			{
																				openiBalanceOfLedger=openiBalanceOfLedger+(ledgerform4.getLedgercredit_balance()-ledgerform4.getLedgerdebit_balance());
																			}
																		}
																	}
																}
															}
														}
													}
												}
												
												total_debitOfLedger=total_debitOfLedger+ledgerform3.getLedgerdebit_balance();
												total_creditOfLedger=total_creditOfLedger+ledgerform3.getLedgercredit_balance();
												
												if(openiBalanceOfLedger!=0 || total_debitOfLedger!=0 || total_creditOfLedger!=0)
												{
													creditCountforAdujustingRow=creditCountforAdujustingRow+1;
												}
											}
											
											if(ledgerform3.getSubledgerList().size()>0)
											{
												if(ledgerform3.getSubgroupName().equals(accSubGroup))
												{
													for(OpeningBalancesForm subledger : ledgerform3.getSubledgerList())
													{
														double openiBalanceOfSubLedger = (double) 0;
														double total_debitOfSubLedger = (double) 0;
														double total_creditOfSubLedger = (double) 0;
														for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
														{
															if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
															{
																for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
																{
																	if(subGroupName.equals(accSubGroup))
																	{
																		for(LedgerForm ledgerform5 : obalanceBeStartDate.getLedgerformlist())
																		{
																			if(ledgerform5.getSubgroupName().equals(accSubGroup))
																			{
																				for(OpeningBalancesForm subledger1 : ledgerform5.getSubledgerList())
																				{
																					if(subledger1.getSubledgerName().equals(subledger.getSubledgerName()))
																					{
																						openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
														
														total_debitOfSubLedger=total_debitOfSubLedger+subledger.getDebit_balance();
														total_creditOfSubLedger=total_creditOfSubLedger+subledger.getCredit_balance();
														
														if(openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0)
														{
															creditCountforAdujustingRow=creditCountforAdujustingRow+1;
														}
													}
												}
											}
										}
									}
									
									for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
									{
										if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
										{
											for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
											{
												if(subGroupName.equals(accSubGroup))
												{
													
													for(LedgerForm ledgerform6 : obalanceBeStartDate.getLedgerformlist())
													{
														int isNotPresent = 0;
														int isPresent = 0;
														
														if(ledgerform6.getSubgroupName().equals(accSubGroup))
														{
															if(obalance.getLedgerformlist().size()>0)
															{
																for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
																{
																	if(ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																	{
																		isPresent=1;
																	}
																}
																
																if(isPresent==0) 
																{
																	if(obalance.getLedgerformlist().size()>0)
																	{
																		for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
																		{
																			if(!ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																			{
																				if(isNotPresent==0)
																				{
																					creditCountforAdujustingRow=creditCountforAdujustingRow+1;
																					
																					if(ledgerform6.getSubledgerList().size()>0)
																					{
																						for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																						{
																							double openiBalanceOfSubLedger = (double) 0;
																							openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																							if(openiBalanceOfSubLedger!=0)
																							{
																								creditCountforAdujustingRow=creditCountforAdujustingRow+1;
																							}
																						}
																					}
																					isNotPresent=1;
																				}
																			}
																		}
																	}
																}
															}
															if(obalance.getLedgerformlist().size()==0)
															{
																creditCountforAdujustingRow=creditCountforAdujustingRow+1;
																if(ledgerform6.getSubledgerList().size()>0)
																{
																	for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																	{
																		double openiBalanceOfSubLedger = (double) 0;
																		openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																		if(openiBalanceOfSubLedger!=0)
																		{
																			creditCountforAdujustingRow=creditCountforAdujustingRow+1;
																		}
																	}
																}
															}
															
															
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				
				}
			}

		if(totalDebitSideAmount>totalCreditSideAmount)
		{
			creditCountforAdujustingRow=creditCountforAdujustingRow+1;
		}
		}
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
		
		Paragraph para = new Paragraph(new Phrase("Horizontal Profit And Loss Report"));
		para.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(para);
		
		
		if(form.getFromDate()!=null && form.getToDate()!=null)
		{
			
			Paragraph date = new Paragraph(new Phrase("From"+" "+ClasstoConvertDateformat.dateFormat(form.getFromDate().toString())+" "+"To"+" "+ClasstoConvertDateformat.dateFormat(form.getToDate().toString())));
			date.setAlignment(Paragraph.ALIGN_LEFT);
			document.add(date);
		}
		
		if (subOpeningList.size() > 0) {

			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(BaseColor.WHITE);

			Font fontblack = FontFactory.getFont(FontFactory.HELVETICA);
			fontblack.setColor(BaseColor.BLACK);

			Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(BaseColor.BLACK);
			cell.setPadding(0);

			PdfPCell cell1 = new PdfPCell();
			cell1.setBackgroundColor(BaseColor.WHITE);
			cell1.setPadding(0);

			
			PdfPTable mainTable = new PdfPTable(2);
			mainTable.setWidthPercentage(100.0f);
			mainTable.setWidths(new float[] { 50.0f, 50.0f });
			mainTable.getDefaultCell().setBorder(0);
			
			PdfPTable debitTable = new PdfPTable(3);
			debitTable.setWidthPercentage(100.0f);
			debitTable.setWidths(new float[] { 10.0f, 5.0f, 5.0f  });
			//debitTable.setSpacingBefore(10);
			
			PdfPCell header1 = new PdfPCell(new Phrase("Expense",boldFont));
			header1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			header1.setColspan(3);
			debitTable.addCell(header1);
			
			cell.setPhrase(new Phrase("Particulars", font));
			cell.setHorizontalAlignment(PdfCell.ALIGN_CENTER);
			debitTable.addCell(cell);

			cell.setPhrase(new Phrase("", font));
			debitTable.addCell(cell);
			
			cell.setPhrase(new Phrase("", font));
			debitTable.addCell(cell);

			PdfPTable creditTable = new PdfPTable(3);
			creditTable.setWidthPercentage(100.0f);
			creditTable.setWidths(new float[] {10.0f, 5.0f, 5.0f });
			//assetsTable.setSpacingBefore(10); 
			

			PdfPCell header2 = new PdfPCell(new Phrase("Income",boldFont));
			header2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			header2.setColspan(3);
			creditTable.addCell(header2);
			
			cell.setPhrase(new Phrase("Particulars", font));
			cell.setHorizontalAlignment(PdfCell.ALIGN_CENTER);
			creditTable.addCell(cell);

			cell.setPhrase(new Phrase("", font));
			creditTable.addCell(cell);
			
			cell.setPhrase(new Phrase("", font));
			creditTable.addCell(cell);
			
			
			for (OpeningBalancesOfSubedgerForm obalance : subOpeningList) {
				
				if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)1))
				{
					double closingBalance = (double) 0;
				
					for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
					{
						if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
						{
							closingBalance=closingBalance+((obalanceBeStartDate.getTotaldebit_balance()-obalanceBeStartDate.getTotalcredit_balance())+(obalance.getTotaldebit_balance()-obalance.getTotalcredit_balance()));
						}
					}
					
					if(closingBalance!=0)
					{
						debitTable.addCell(new Phrase(obalance.getAccountGroupName(), boldFont));
						debitTable.addCell("");
						debitTable.addCell(commonService.round(closingBalance, 2).toString());
						
						if(obalance.getAccountSubGroupNameList().size()>0)
						{
							for(String accSubGroup : obalance.getAccountSubGroupNameList())
							{
								double closingBalanceOfSubgroup = (double) 0;
								double total_credit_Subgroup = (double) 0;
								double total_debit_Subgroup = (double) 0;
								double total_credit_Subgroup_beforeStartDate = (double) 0;
								double total_debit_Subgroup_beforeStartDate = (double) 0;
								
								for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
								{
									if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
									{
										for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
										{
											if(subGroupName.equals(accSubGroup))
											{
												if(obalanceBeStartDate.getLedgerformlist().size()>0)
												{
													for(LedgerForm ledgerform1 : obalanceBeStartDate.getLedgerformlist())
													{
														if(ledgerform1.getSubgroupName().equals(accSubGroup))
														{
															total_credit_Subgroup_beforeStartDate=total_credit_Subgroup_beforeStartDate+ledgerform1.getLedgercredit_balance();
															total_debit_Subgroup_beforeStartDate=total_debit_Subgroup_beforeStartDate+ledgerform1.getLedgerdebit_balance();
														}
													}
												}
											}
										}
									}
								}
								if(obalance.getLedgerformlist().size()>0)
								{
									for(LedgerForm ledgerform2 : obalance.getLedgerformlist())
									{
										if(ledgerform2.getSubgroupName().equals(accSubGroup))
										{
											total_credit_Subgroup=total_credit_Subgroup+ledgerform2.getLedgercredit_balance();
											total_debit_Subgroup=total_debit_Subgroup+ledgerform2.getLedgerdebit_balance();
										}
									}
								}
								
								closingBalanceOfSubgroup=closingBalanceOfSubgroup+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup);
								if(closingBalanceOfSubgroup!=0)
								{
									debitTable.addCell("     "+accSubGroup);
									debitTable.addCell(commonService.round(closingBalanceOfSubgroup, 2).toString());
									debitTable.addCell("");
									if(obalance.getLedgerformlist().size()>0)
									{
										for(LedgerForm ledgerform3 : obalance.getLedgerformlist())
										{
											if(ledgerform3.getSubgroupName().equals(accSubGroup))
											{
												double openiBalanceOfLedger = (double) 0;
												double total_debitOfLedger = (double) 0;
												double total_creditOfLedger = (double) 0;
												for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
												{
													if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
													{
														for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
														{
															if(subGroupName.equals(accSubGroup))
															{
																if(obalanceBeStartDate.getLedgerformlist().size()>0)
																{
																	for(LedgerForm ledgerform4 : obalanceBeStartDate.getLedgerformlist())
																	{
																		if(ledgerform4.getLedgerName().equals(ledgerform3.getLedgerName()))
																		{
																			if(ledgerform4.getSubgroupName().equals(accSubGroup))
																			{
																				openiBalanceOfLedger=openiBalanceOfLedger+(ledgerform4.getLedgerdebit_balance()-ledgerform4.getLedgercredit_balance());
																			}
																		}
																	}
																}
															}
														}
													}
												}
												
												total_debitOfLedger=total_debitOfLedger+ledgerform3.getLedgerdebit_balance();
												total_creditOfLedger=total_creditOfLedger+ledgerform3.getLedgercredit_balance();
												
												if(openiBalanceOfLedger!=0 || total_debitOfLedger!=0 || total_creditOfLedger!=0)
												{
													debitTable.addCell(new Phrase("       "+ledgerform3.getLedgerName()));
													debitTable.addCell(commonService.round((openiBalanceOfLedger+total_debitOfLedger-total_creditOfLedger), 2).toString());
													debitTable.addCell("");
												}
											}
											
											if(ledgerform3.getSubledgerList().size()>0)
											{
												if(ledgerform3.getSubgroupName().equals(accSubGroup))
												{
													for(OpeningBalancesForm subledger : ledgerform3.getSubledgerList())
													{
														double openiBalanceOfSubLedger = (double) 0;
														double total_debitOfSubLedger = (double) 0;
														double total_creditOfSubLedger = (double) 0;
														for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
														{
															if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
															{
																for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
																{
																	if(subGroupName.equals(accSubGroup))
																	{
																		for(LedgerForm ledgerform5 : obalanceBeStartDate.getLedgerformlist())
																		{
																			if(ledgerform5.getSubgroupName().equals(accSubGroup))
																			{
																				for(OpeningBalancesForm subledger1 : ledgerform5.getSubledgerList())
																				{
																					if(subledger1.getSubledgerName().equals(subledger.getSubledgerName()))
																					{
																						openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
														
														total_debitOfSubLedger=total_debitOfSubLedger+subledger.getDebit_balance();
														total_creditOfSubLedger=total_creditOfSubLedger+subledger.getCredit_balance();
														
														if(openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0)
														{
															debitTable.addCell(new Phrase("         "+subledger.getSubledgerName()));
															debitTable.addCell(commonService.round((openiBalanceOfSubLedger+total_debitOfSubLedger-total_creditOfSubLedger), 2).toString());
															debitTable.addCell("");
														}
													}
												}
											}
										}
									}
									
									for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
									{
										if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
										{
											for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
											{
												if(subGroupName.equals(accSubGroup))
												{
													
													for(LedgerForm ledgerform6 : obalanceBeStartDate.getLedgerformlist())
													{
														int isNotPresent = 0;
														int isPresent = 0;
														
														if(ledgerform6.getSubgroupName().equals(accSubGroup))
														{
															if(obalance.getLedgerformlist().size()>0)
															{
																for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
																{
																	if(ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																	{
																		isPresent=1;
																	}
																}
																
																if(isPresent==0) 
																{
																	if(obalance.getLedgerformlist().size()>0)
																	{
																		for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
																		{
																			if(!ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																			{
																				if(isNotPresent==0)
																				{
																					double openiBalanceOfLedger = (double) 0;
																					openiBalanceOfLedger =openiBalanceOfLedger+(ledgerform6.getLedgerdebit_balance()-ledgerform6.getLedgercredit_balance());
																					debitTable.addCell(new Phrase("       "+ledgerform6.getLedgerName()));
																					debitTable.addCell(commonService.round((openiBalanceOfLedger), 2).toString());
																					debitTable.addCell("");
																					
																					if(ledgerform6.getSubledgerList().size()>0)
																					{
																						for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																						{
																							double openiBalanceOfSubLedger = (double) 0;
																							openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																							if(openiBalanceOfSubLedger!=0)
																							{
																								debitTable.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																								debitTable.addCell(commonService.round((openiBalanceOfSubLedger), 2).toString());
																								debitTable.addCell("");
																							}
																						}
																					}
																					isNotPresent=1;
																				}
																			}
																		}
																	}
																}
															}
															if(obalance.getLedgerformlist().size()==0)
															{
																double openiBalanceOfLedger = (double) 0;
																openiBalanceOfLedger =openiBalanceOfLedger+(ledgerform6.getLedgerdebit_balance()-ledgerform6.getLedgercredit_balance());
																debitTable.addCell(new Phrase("       "+ledgerform6.getLedgerName()));
																debitTable.addCell(commonService.round((openiBalanceOfLedger), 2).toString());
																debitTable.addCell("");
																if(ledgerform6.getSubledgerList().size()>0)
																{
																	for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																	{
																		double openiBalanceOfSubLedger = (double) 0;
																		openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																		if(openiBalanceOfSubLedger!=0)
																		{
																			debitTable.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																			debitTable.addCell(commonService.round((openiBalanceOfSubLedger), 2).toString());
																			debitTable.addCell("");
																		}
																	}
																}
															}
															
															
														}
													}
												}
											}
										}
									}
									
								}
							}
						}
					}
				
				}
			}
			double totalNetProfit = (double) 0;
			if(totalCreditSideAmount>totalDebitSideAmount)
			{
				debitTable.addCell(new Phrase("Net Profit", boldFont));
				debitTable.addCell("");
				debitTable.addCell(new Phrase(commonService.round(totalCreditSideAmount-totalDebitSideAmount, 2).toString(), boldFont));
				totalNetProfit=totalNetProfit+(totalCreditSideAmount-totalDebitSideAmount);
				
			}
			if(creditCountforAdujustingRow>debitCountforAdujustingRow)
			{
				if(creditCountforAdujustingRow-debitCountforAdujustingRow!=0)
				{
					for(int i=1;i<=creditCountforAdujustingRow-debitCountforAdujustingRow;i++)
					{
						debitTable.addCell("-");
						debitTable.addCell("-");
						debitTable.addCell("-");
					}
				}
			}
			debitTable.addCell(new Phrase("Total", boldFont));
			debitTable.addCell("");
			debitTable.addCell(new Phrase(commonService.round(totalDebitSideAmount+totalNetProfit, 2).toString(), boldFont));
		
			for (OpeningBalancesOfSubedgerForm obalance : subOpeningList) {
				
				if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)2))
				{
					double closingBalance = (double) 0;
				
					for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
					{
						if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
						{
							closingBalance=closingBalance+((obalanceBeStartDate.getTotalcredit_balance()-obalanceBeStartDate.getTotaldebit_balance())+(obalance.getTotalcredit_balance()-obalance.getTotaldebit_balance()));
						}
					}
					
					if(closingBalance!=0)
					{
						creditTable.addCell(new Phrase(obalance.getAccountGroupName(), boldFont));
						creditTable.addCell("");
						creditTable.addCell(commonService.round(closingBalance, 2).toString());
						
						if(obalance.getAccountSubGroupNameList().size()>0)
						{
							for(String accSubGroup : obalance.getAccountSubGroupNameList())
							{
								double closingBalanceOfSubgroup = (double) 0;
								double total_credit_Subgroup = (double) 0;
								double total_debit_Subgroup = (double) 0;
								double total_credit_Subgroup_beforeStartDate = (double) 0;
								double total_debit_Subgroup_beforeStartDate = (double) 0;
								
								for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
								{
									if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
									{
										for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
										{
											if(subGroupName.equals(accSubGroup))
											{
												if(obalanceBeStartDate.getLedgerformlist().size()>0)
												{
													for(LedgerForm ledgerform1 : obalanceBeStartDate.getLedgerformlist())
													{
														if(ledgerform1.getSubgroupName().equals(accSubGroup))
														{
															total_credit_Subgroup_beforeStartDate=total_credit_Subgroup_beforeStartDate+ledgerform1.getLedgercredit_balance();
															total_debit_Subgroup_beforeStartDate=total_debit_Subgroup_beforeStartDate+ledgerform1.getLedgerdebit_balance();
														}
													}
												}
											}
										}
									}
								}
								if(obalance.getLedgerformlist().size()>0)
								{
									for(LedgerForm ledgerform2 : obalance.getLedgerformlist())
									{
										if(ledgerform2.getSubgroupName().equals(accSubGroup))
										{
											total_credit_Subgroup=total_credit_Subgroup+ledgerform2.getLedgercredit_balance();
											total_debit_Subgroup=total_debit_Subgroup+ledgerform2.getLedgerdebit_balance();
										}
									}
								}
								
								closingBalanceOfSubgroup=closingBalanceOfSubgroup+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup);
								if(closingBalanceOfSubgroup!=0)
								{
									creditTable.addCell("     "+accSubGroup);
									creditTable.addCell(commonService.round(closingBalanceOfSubgroup, 2).toString());
									creditTable.addCell("");
									
									if(obalance.getLedgerformlist().size()>0)
									{
										for(LedgerForm ledgerform3 : obalance.getLedgerformlist())
										{
											if(ledgerform3.getSubgroupName().equals(accSubGroup))
											{
												double openiBalanceOfLedger = (double) 0;
												double total_debitOfLedger = (double) 0;
												double total_creditOfLedger = (double) 0;
												for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
												{
													if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
													{
														for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
														{
															if(subGroupName.equals(accSubGroup))
															{
																if(obalanceBeStartDate.getLedgerformlist().size()>0)
																{
																	for(LedgerForm ledgerform4 : obalanceBeStartDate.getLedgerformlist())
																	{
																		if(ledgerform4.getLedgerName().equals(ledgerform3.getLedgerName()))
																		{
																			if(ledgerform4.getSubgroupName().equals(accSubGroup))
																			{
																				openiBalanceOfLedger=openiBalanceOfLedger+(ledgerform4.getLedgercredit_balance()-ledgerform4.getLedgerdebit_balance());
																			}
																		}
																	}
																}
															}
														}
													}
												}
												
												total_debitOfLedger=total_debitOfLedger+ledgerform3.getLedgerdebit_balance();
												total_creditOfLedger=total_creditOfLedger+ledgerform3.getLedgercredit_balance();
												
												if(openiBalanceOfLedger!=0 || total_debitOfLedger!=0 || total_creditOfLedger!=0)
												{
													creditTable.addCell(new Phrase("       "+ledgerform3.getLedgerName()));
													creditTable.addCell(commonService.round((openiBalanceOfLedger+total_creditOfLedger-total_debitOfLedger), 2).toString());
													creditTable.addCell("");
												}
											}
											
											if(ledgerform3.getSubledgerList().size()>0)
											{
												if(ledgerform3.getSubgroupName().equals(accSubGroup))
												{
													for(OpeningBalancesForm subledger : ledgerform3.getSubledgerList())
													{
														double openiBalanceOfSubLedger = (double) 0;
														double total_debitOfSubLedger = (double) 0;
														double total_creditOfSubLedger = (double) 0;
														for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
														{
															if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
															{
																for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
																{
																	if(subGroupName.equals(accSubGroup))
																	{
																		for(LedgerForm ledgerform5 : obalanceBeStartDate.getLedgerformlist())
																		{
																			if(ledgerform5.getSubgroupName().equals(accSubGroup))
																			{
																				for(OpeningBalancesForm subledger1 : ledgerform5.getSubledgerList())
																				{
																					if(subledger1.getSubledgerName().equals(subledger.getSubledgerName()))
																					{
																						openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
														
														total_debitOfSubLedger=total_debitOfSubLedger+subledger.getDebit_balance();
														total_creditOfSubLedger=total_creditOfSubLedger+subledger.getCredit_balance();
														
														if(openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0)
														{
															creditTable.addCell(new Phrase("         "+subledger.getSubledgerName()));
															creditTable.addCell(commonService.round((openiBalanceOfSubLedger+total_creditOfSubLedger-total_debitOfSubLedger), 2).toString());
															creditTable.addCell("");
														}
													}
												}
											}
										}
									}
									
									for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
									{
										if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
										{
											for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
											{
												if(subGroupName.equals(accSubGroup))
												{
													
													for(LedgerForm ledgerform6 : obalanceBeStartDate.getLedgerformlist())
													{
														int isNotPresent = 0;
														int isPresent = 0;
														
														if(ledgerform6.getSubgroupName().equals(accSubGroup))
														{
															if(obalance.getLedgerformlist().size()>0)
															{
																for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
																{
																	if(ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																	{
																		isPresent=1;
																	}
																}
																
																if(isPresent==0) 
																{
																	if(obalance.getLedgerformlist().size()>0)
																	{
																		for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
																		{
																			if(!ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																			{
																				if(isNotPresent==0)
																				{
																					double openiBalanceOfLedger = (double) 0;
																					openiBalanceOfLedger =openiBalanceOfLedger+(ledgerform6.getLedgercredit_balance()-ledgerform6.getLedgerdebit_balance());
																					creditTable.addCell(new Phrase("       "+ledgerform6.getLedgerName()));
																					creditTable.addCell(commonService.round((openiBalanceOfLedger), 2).toString());
																					creditTable.addCell("");
																					
																					if(ledgerform6.getSubledgerList().size()>0)
																					{
																						for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																						{
																							double openiBalanceOfSubLedger = (double) 0;
																							openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																							if(openiBalanceOfSubLedger!=0)
																							{
																								creditTable.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																								creditTable.addCell(commonService.round((openiBalanceOfSubLedger), 2).toString());
																								creditTable.addCell("");
																							}
																						}
																					}
																					isNotPresent=1;
																				}
																			}
																		}
																	}
																}
															}
															if(obalance.getLedgerformlist().size()==0)
															{
																double openiBalanceOfLedger = (double) 0;
																openiBalanceOfLedger =openiBalanceOfLedger+(ledgerform6.getLedgercredit_balance()-ledgerform6.getLedgerdebit_balance());
																creditTable.addCell(new Phrase("       "+ledgerform6.getLedgerName()));
																creditTable.addCell(commonService.round((openiBalanceOfLedger), 2).toString());
																creditTable.addCell("");
																if(ledgerform6.getSubledgerList().size()>0)
																{
																	for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																	{
																		double openiBalanceOfSubLedger = (double) 0;
																		openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																		if(openiBalanceOfSubLedger!=0)
																		{
																			creditTable.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																			creditTable.addCell(commonService.round((openiBalanceOfSubLedger), 2).toString());
																			creditTable.addCell("");
																		}
																	}
																}
															}
															
															
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				
				}
			}
			double totalNetLoss = (double) 0;
			if(totalDebitSideAmount>totalCreditSideAmount)
			{
				creditTable.addCell(new Phrase("Excess of Income Over Expenditure", boldFont));
				creditTable.addCell("");
				creditTable.addCell(new Phrase(commonService.round(totalDebitSideAmount-totalCreditSideAmount, 2).toString(), boldFont));
				totalNetLoss=totalNetLoss+(totalDebitSideAmount-totalCreditSideAmount);
				
			}
			
			if(debitCountforAdujustingRow>creditCountforAdujustingRow)
			{
				if(debitCountforAdujustingRow-creditCountforAdujustingRow!=0)
				{
					for(int i=1;i<=debitCountforAdujustingRow-creditCountforAdujustingRow;i++)
					{
						creditTable.addCell("-");
						creditTable.addCell("-");
						creditTable.addCell("-");
					}
				}
			}
			
			creditTable.addCell(new Phrase("Total", boldFont));
			creditTable.addCell("");
			creditTable.addCell(new Phrase(commonService.round(totalCreditSideAmount+totalNetLoss, 2).toString(), boldFont));
         

             Paragraph paragraph = new Paragraph(new Phrase("    "));
             paragraph.setAlignment(Paragraph.ALIGN_LEFT);
     		 document.add(paragraph);
     		 
             mainTable.addCell(debitTable);
             mainTable.addCell(creditTable);
             document.add(mainTable);

		} else {
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100.0f);
			table.setWidths(new float[] { 5.0f, 5.0f });
			table.setSpacingBefore(10);

			// define font for table header row
			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(BaseColor.WHITE);

			// define table header cell
			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(BaseColor.BLACK);
			cell.setPadding(0);

			cell.setPhrase(new Phrase("Liabilities", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Assets", font));
			table.addCell(cell);

			document.add(table);
		}
	}
	
}


