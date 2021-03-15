package com.fasset.pdf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasset.entities.SubLedger;
import com.fasset.form.HorizontalBalanceSheetReportForm;
import com.fasset.form.LedgerForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.OpeningBalancesOfSubedgerForm;
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

public class PDFBuilderForHorizontalBalanceSheet extends AbstractITextPdfView{

	 CommonServiceImpl commonService = new CommonServiceImpl();

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"Horizontal Balance Sheet Report.pdf\"");

		 HorizontalBalanceSheetReportForm form = (HorizontalBalanceSheetReportForm) model.get("form");

		 List<OpeningBalancesOfSubedgerForm> subOpeningList=form.getListForOpeningbalancesOfsubledger();
		 List<OpeningBalancesOfSubedgerForm> subOpeningListBeforeStartDate=form.getListForOpeningbalancesbeforestartDate();
		 
		 List<OpeningBalancesForm>bankOpeningBalanceList= form.getBankOpeningBalanceList();
		 List<OpeningBalancesForm>supplierOpeningBalanceList=form.getSupplierOpeningBalanceList();
		 List<OpeningBalancesForm>customerOpeningBalanceList=form.getCustomerOpeningBalanceList();
		 List<OpeningBalancesForm>supplierOpeningBalanceBeforeStartDate=form.getSupplierOpeningBalanceBeforeStartDate();
		 List<OpeningBalancesForm>customerOpeningBalanceBeforeStartDate=form.getCustomerOpeningBalanceBeforeStartDate();
		 List<OpeningBalancesForm>bankOpeningBalanceBeforeStartDate=form.getBankOpeningBalanceBeforeStartDate();
		 int debitCountforAdujustingRow = 0;
		 int creditCountforAdujustingRow = 0;
			
		 double totalDebitSideAmount = (double) 0;
		 double totalCreditSideAmount = (double) 0;
		 double totalDebitSideAmount1 = (double) 0;
	     double totalCreditSideAmount1 = (double) 0;	
	     
	     double total_credit_Supplier_beforeStartDate = (double) 0;
	     double total_debit_Supplier_beforeStartDate = (double) 0;
	     double total_credit_Supplier = (double) 0;
	     double total_debit_Supplier = (double) 0;
	     
	     double total_credit_Customer_beforeStartDate = (double) 0;
	     double total_debit_Customer_beforeStartDate = (double) 0;
	     double total_credit_Customer = (double) 0;
	     double total_debit_Customer = (double) 0;
	     
	     double total_credit_Bank_beforeStartDate = (double) 0;
	     double total_debit_Bank_beforeStartDate = (double) 0;
	     double total_credit_Bank = (double) 0;
	     double total_debit_Bank = (double) 0;
	     
	     for(OpeningBalancesForm obalance:supplierOpeningBalanceBeforeStartDate)
	     {
	    	 total_credit_Supplier_beforeStartDate=total_credit_Supplier_beforeStartDate+obalance.getCredit_balance();
	    	 total_debit_Supplier_beforeStartDate=total_debit_Supplier_beforeStartDate+obalance.getDebit_balance();
	     }
	     for(OpeningBalancesForm obalance:supplierOpeningBalanceList)
	     {
	    	 total_credit_Supplier=total_credit_Supplier+obalance.getCredit_balance();
	    	 total_debit_Supplier=total_debit_Supplier+obalance.getDebit_balance();
	     }
	     
	     for(OpeningBalancesForm obalance:customerOpeningBalanceBeforeStartDate)
	     {
	    	 total_credit_Customer_beforeStartDate=total_credit_Customer_beforeStartDate+obalance.getCredit_balance();
	    	 total_debit_Customer_beforeStartDate=total_debit_Customer_beforeStartDate+obalance.getDebit_balance();
	     }
	     for(OpeningBalancesForm obalance:customerOpeningBalanceList)
	     {
	    	 total_credit_Customer=total_credit_Customer+obalance.getCredit_balance();
	    	 total_debit_Customer=total_debit_Customer+obalance.getDebit_balance();
	     }
	     
	     for(OpeningBalancesForm obalance:bankOpeningBalanceBeforeStartDate)
	     {
	    	 total_credit_Bank_beforeStartDate=total_credit_Bank_beforeStartDate+obalance.getCredit_balance();
	    	 total_debit_Bank_beforeStartDate=total_debit_Bank_beforeStartDate+obalance.getDebit_balance();
	     }
	     for(OpeningBalancesForm obalance:bankOpeningBalanceList)
	     {
	    	 total_credit_Bank=total_credit_Bank+obalance.getCredit_balance();
	    	 total_debit_Bank=total_debit_Bank+obalance.getDebit_balance();
	     }
	 	for(OpeningBalancesOfSubedgerForm obalance : subOpeningList)
		{
			if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)3))
			{
				if(obalance.getAccountGroupName().equals("Current Assets"))
				{
					double openingBalanceforGroup = (double)0;
					for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
					{
						if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
						{
							 openingBalanceforGroup = openingBalanceforGroup+(obalanceBeStartDate.getTotaldebit_balance()+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate)-(obalanceBeStartDate.getTotalcredit_balance()+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate);
						}
					}
					
				     double total_debitGroup = (obalance.getTotaldebit_balance()+total_debit_Customer+total_debit_Bank);
				     double total_creditGroup = (obalance.getTotalcredit_balance()+total_credit_Customer+total_credit_Bank);
				     totalDebitSideAmount=totalDebitSideAmount+((openingBalanceforGroup)+(total_debitGroup-total_creditGroup));
				}
				else
				{
					for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
					{
						if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
						{
							totalDebitSideAmount = totalDebitSideAmount+((obalanceBeStartDate.getTotaldebit_balance()-obalanceBeStartDate.getTotalcredit_balance())+(obalance.getTotaldebit_balance()-obalance.getTotalcredit_balance()));
						}
					}
				}
			}
		}
	 	
	 	
	 	for(OpeningBalancesOfSubedgerForm obalance : subOpeningList)
		{
			if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)4))
			{
				if(obalance.getAccountGroupName().equals("Current Liabilities"))
				{
					double openingBalanceforGroup = (double)0;
					for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
					{
						if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
						{
							 openingBalanceforGroup = openingBalanceforGroup+(obalanceBeStartDate.getTotalcredit_balance()+total_credit_Supplier_beforeStartDate)-(obalanceBeStartDate.getTotaldebit_balance()+total_debit_Supplier_beforeStartDate);
						}
					}
					
				     double total_debitGroup = (obalance.getTotaldebit_balance()+total_debit_Supplier);
				     double total_creditGroup = (obalance.getTotalcredit_balance()+total_credit_Supplier);
				     totalCreditSideAmount=totalCreditSideAmount+((openingBalanceforGroup)+(total_creditGroup-total_debitGroup));
				}
				else
				{
					for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
					{
						if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
						{
							totalCreditSideAmount = totalCreditSideAmount+((obalanceBeStartDate.getTotalcredit_balance()-obalanceBeStartDate.getTotaldebit_balance())+(obalance.getTotalcredit_balance()-obalance.getTotaldebit_balance()));
						}
					}
				}
			}
		}
	     
	 	for(OpeningBalancesOfSubedgerForm obalance : subOpeningList)
		{
			if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)1))
			{
				for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
				{
					if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
					{
						totalDebitSideAmount1=totalDebitSideAmount1+((obalanceBeStartDate.getTotaldebit_balance()-obalanceBeStartDate.getTotalcredit_balance())+(obalance.getTotaldebit_balance()-obalance.getTotalcredit_balance()));
					}
				}
			}
			if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)2))
			{
				for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
				{
					if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
					{
						totalCreditSideAmount1=totalCreditSideAmount1+((obalanceBeStartDate.getTotalcredit_balance()-obalanceBeStartDate.getTotaldebit_balance())+(obalance.getTotalcredit_balance()-obalance.getTotaldebit_balance()));
					}
				}
			}
		}
	 	if (subOpeningList.size() > 0) 
	 	{
         for (OpeningBalancesOfSubedgerForm obalance : subOpeningList) 
             {
				
				if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)4))
				{
					double closingBalance = (double) 0;
					
					if(obalance.getAccountGroupName().equals("Current Liabilities"))
					{
						double openingBalanceforGroup = (double)0;
						for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
						{
							if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
							{
								 openingBalanceforGroup = openingBalanceforGroup+(obalanceBeStartDate.getTotalcredit_balance()+total_credit_Supplier_beforeStartDate)-(obalanceBeStartDate.getTotaldebit_balance()+total_debit_Supplier_beforeStartDate);
							}
							
						}
						double total_debitGroup = (obalance.getTotaldebit_balance()+total_debit_Supplier);
					    double total_creditGroup = (obalance.getTotalcredit_balance()+total_credit_Supplier);
					    closingBalance=closingBalance+((openingBalanceforGroup)+(total_creditGroup-total_debitGroup));
					}
					else
					{
						for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
						{
							if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
							{
								closingBalance = closingBalance+((obalanceBeStartDate.getTotalcredit_balance()-obalanceBeStartDate.getTotaldebit_balance())+(obalance.getTotalcredit_balance()-obalance.getTotaldebit_balance()));
							}
							
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
								if(accSubGroup.equals("Trade Payables"))
								{
									double openingBalanceSubgroup = (total_credit_Subgroup_beforeStartDate+total_credit_Supplier_beforeStartDate)-(total_debit_Subgroup_beforeStartDate+total_debit_Supplier_beforeStartDate);
									double total_debitSubgroup =(total_debit_Subgroup+total_debit_Supplier);
									double total_creditSubgroup = (total_credit_Subgroup+total_credit_Supplier);
									closingBalanceOfSubgroup=closingBalanceOfSubgroup+(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup);
								}
								else
								{
									closingBalanceOfSubgroup=closingBalanceOfSubgroup+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup);
								}
								
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
									
									if(accSubGroup.equals("Trade Payables"))
									{
										for(OpeningBalancesForm supOpbalance:supplierOpeningBalanceList) 
										{
											double supOpeningbalance = (double) 0;
											double supDebitbalance = (double) 0;
											double supCreditbalance = (double) 0;
											
											for(OpeningBalancesForm supOpbalance1:supplierOpeningBalanceBeforeStartDate) 
											{
												if(supOpbalance1.getSupplierName().equals(supOpbalance.getSupplierName()))
												{
													supOpeningbalance=supOpeningbalance+supOpbalance1.getCredit_balance()-supOpbalance1.getDebit_balance();
												}
											}
											supDebitbalance=supDebitbalance+supOpbalance.getDebit_balance();
											supCreditbalance=supCreditbalance+supOpbalance.getCredit_balance();
											if(supOpeningbalance!=0 || supDebitbalance!=0 || supCreditbalance!=0)
											{
												creditCountforAdujustingRow=creditCountforAdujustingRow+1;
											}
										}
										
										for(OpeningBalancesForm supOpbalance1:supplierOpeningBalanceBeforeStartDate) 
										{
											double openiBalanceOfsupplier = (double) 0;
											int issupplierPresent = 0;
											for(OpeningBalancesForm supOpbalance:supplierOpeningBalanceList) 
											{
												if(supOpbalance1.getSupplierName().equals(supOpbalance.getSupplierName()))
												{
													issupplierPresent=issupplierPresent+1;
												}
												
											}
											if(issupplierPresent==0)
											{
												openiBalanceOfsupplier=openiBalanceOfsupplier+supOpbalance1.getCredit_balance()-supOpbalance1.getDebit_balance();
												if(openiBalanceOfsupplier!=0)
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
			if(totalCreditSideAmount1!=totalDebitSideAmount1)
			{
				creditCountforAdujustingRow=creditCountforAdujustingRow+1;
			}
			
			 for (OpeningBalancesOfSubedgerForm obalance : subOpeningList) {
					
					if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)3))
					{
						double closingBalance = (double) 0;
						
						if(obalance.getAccountGroupName().equals("Current Assets"))
						{
							double openingBalanceforGroup = (double)0;
							for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
							{
								if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
								{
									 openingBalanceforGroup = openingBalanceforGroup+(obalanceBeStartDate.getTotaldebit_balance()+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate)-(obalanceBeStartDate.getTotalcredit_balance()+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate);
								}
								
							}
							double total_debitGroup = (obalance.getTotaldebit_balance()+total_debit_Customer+total_debit_Bank);
						    double total_creditGroup = (obalance.getTotalcredit_balance()+total_credit_Customer+total_credit_Bank);
						    closingBalance=closingBalance+(openingBalanceforGroup)+(total_debitGroup-total_creditGroup);
						}
						else
						{
							for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
							{
								if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
								{
									closingBalance = closingBalance+(obalanceBeStartDate.getTotaldebit_balance()-obalanceBeStartDate.getTotalcredit_balance())+(obalance.getTotaldebit_balance()-obalance.getTotalcredit_balance());
								}
								
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
									if(accSubGroup.equals("Trade Receivables")||accSubGroup.equals("Cash & Bank Balances"))
									{
										if(accSubGroup.equals("Trade Receivables"))
										{
											double openingBalanceSubgroup = (total_debit_Subgroup_beforeStartDate+total_debit_Customer_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Customer_beforeStartDate);
											double total_debitSubgroup =(total_debit_Subgroup+total_debit_Customer);
											double total_creditSubgroup = (total_credit_Subgroup+total_credit_Customer);
											closingBalanceOfSubgroup=closingBalanceOfSubgroup+(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup);
										}
										else if(accSubGroup.equals("Cash & Bank Balances"))
										{
											double openingBalanceSubgroup = (total_debit_Subgroup_beforeStartDate+total_debit_Bank_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Bank_beforeStartDate);
											double total_debitSubgroup =(total_debit_Subgroup+total_debit_Bank);
											double total_creditSubgroup = (total_credit_Subgroup+total_credit_Bank);
											closingBalanceOfSubgroup=closingBalanceOfSubgroup+(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup);
										}
										
									}
									else
									{
										closingBalanceOfSubgroup=closingBalanceOfSubgroup+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup);
									}
									
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
										
										if(accSubGroup.equals("Cash & Bank Balances"))
										{
											for(OpeningBalancesForm bankOpbalance:bankOpeningBalanceList) 
											{
												double bankOpeningbalance = (double) 0;
												double bankDebitbalance = (double) 0;
												double bankCreditbalance = (double) 0;
												
												for(OpeningBalancesForm bankOpbalance1:bankOpeningBalanceBeforeStartDate) 
												{
													if(bankOpbalance1.getBankName().equals(bankOpbalance.getBankName()))
													{
														bankOpeningbalance=bankOpeningbalance+bankOpbalance1.getDebit_balance()-bankOpbalance1.getCredit_balance();
													}
												}
												bankDebitbalance=bankDebitbalance+bankOpbalance.getDebit_balance();
												bankCreditbalance=bankCreditbalance+bankOpbalance.getCredit_balance();
												if(bankOpeningbalance!=0 || bankDebitbalance!=0 || bankCreditbalance!=0)
												{
													debitCountforAdujustingRow=debitCountforAdujustingRow+1;
												}
											}
											
											for(OpeningBalancesForm bankOpbalance1:bankOpeningBalanceBeforeStartDate) 
											{
												double openiBalanceOfbank = (double) 0;
												int isBankPresent = 0;
												for(OpeningBalancesForm bankOpbalance:bankOpeningBalanceList) 
												{
													if(bankOpbalance1.getBankName().equals(bankOpbalance.getBankName()))
													{
														isBankPresent=isBankPresent+1;
													}
													
												}
												if(isBankPresent==0)
												{
													openiBalanceOfbank=openiBalanceOfbank+bankOpbalance1.getDebit_balance()-bankOpbalance1.getCredit_balance();
													if(openiBalanceOfbank!=0)
													{
														debitCountforAdujustingRow=debitCountforAdujustingRow+1;
													}
												}
											}
										}
										
										if(accSubGroup.equals("Trade Receivables"))
										{
											for(OpeningBalancesForm cusoPbalance:customerOpeningBalanceList) 
											{
												double cusOpeningbalance = (double) 0;
												double cusDebitbalance = (double) 0;
												double cusCreditbalance = (double) 0;
												
												for(OpeningBalancesForm cusoPbalance1:customerOpeningBalanceBeforeStartDate) 
												{
													if(cusoPbalance1.getCustomerName().equals(cusoPbalance.getCustomerName()))
													{
														cusOpeningbalance=cusOpeningbalance+cusoPbalance1.getDebit_balance()-cusoPbalance1.getCredit_balance();
													}
												}
												cusDebitbalance=cusDebitbalance+cusoPbalance.getDebit_balance();
												cusCreditbalance=cusCreditbalance+cusoPbalance.getCredit_balance();
												if(cusOpeningbalance!=0 || cusDebitbalance!=0 || cusCreditbalance!=0)
												{
													debitCountforAdujustingRow=debitCountforAdujustingRow+1;
												}
											}
											
											for(OpeningBalancesForm cusoPbalance1:customerOpeningBalanceBeforeStartDate) 
											{
												double openiBalanceOfcustomer = (double) 0;
												int iscustomerPresent = 0;
												for(OpeningBalancesForm cusoPbalance:customerOpeningBalanceList) 
												{
													if(cusoPbalance1.getCustomerName().equals(cusoPbalance.getCustomerName()))
													{
														iscustomerPresent=iscustomerPresent+1;
													}
													
												}
												if(iscustomerPresent==0)
												{
													openiBalanceOfcustomer=openiBalanceOfcustomer+cusoPbalance1.getDebit_balance()-cusoPbalance1.getCredit_balance();
													if(openiBalanceOfcustomer!=0)
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
		
		Paragraph para = new Paragraph(new Phrase("Horizontal Balance Sheet Report"));
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
			
			PdfPTable liabilitiesTable = new PdfPTable(3);
			liabilitiesTable.setWidthPercentage(100.0f);
			liabilitiesTable.setWidths(new float[] { 10.0f, 5.0f, 5.0f  });
			//liabilitiesTable.setSpacingBefore(10);
			
			PdfPCell header1 = new PdfPCell(new Phrase("Liabilities",boldFont));
			header1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			header1.setColspan(3);
			liabilitiesTable.addCell(header1);
			
			cell.setPhrase(new Phrase("Particulars", font));
			cell.setHorizontalAlignment(PdfCell.ALIGN_CENTER);
			liabilitiesTable.addCell(cell);

			cell.setPhrase(new Phrase("", font));
			liabilitiesTable.addCell(cell);
			
			cell.setPhrase(new Phrase("", font));
			liabilitiesTable.addCell(cell);

			PdfPTable assetsTable = new PdfPTable(3);
			assetsTable.setWidthPercentage(100.0f);
			assetsTable.setWidths(new float[] {10.0f, 5.0f, 5.0f });
			//assetsTable.setSpacingBefore(10); 
			

			PdfPCell header2 = new PdfPCell(new Phrase("Assets",boldFont));
			header2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			header2.setColspan(3);
			assetsTable.addCell(header2);
			
			cell.setPhrase(new Phrase("Particulars", font));
			cell.setHorizontalAlignment(PdfCell.ALIGN_CENTER);
			assetsTable.addCell(cell);

			cell.setPhrase(new Phrase("", font));
			assetsTable.addCell(cell);
			
			cell.setPhrase(new Phrase("", font));
			assetsTable.addCell(cell);
			
			
			for (OpeningBalancesOfSubedgerForm obalance : subOpeningList) {
				
				if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)4))
				{
					double closingBalance = (double) 0;
					
					if(obalance.getAccountGroupName().equals("Current Liabilities"))
					{
						double openingBalanceforGroup = (double)0;
						for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
						{
							if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
							{
								 openingBalanceforGroup = openingBalanceforGroup+(obalanceBeStartDate.getTotalcredit_balance()+total_credit_Supplier_beforeStartDate)-(obalanceBeStartDate.getTotaldebit_balance()+total_debit_Supplier_beforeStartDate);
							}
							
						}
						double total_debitGroup = (obalance.getTotaldebit_balance()+total_debit_Supplier);
					    double total_creditGroup = (obalance.getTotalcredit_balance()+total_credit_Supplier);
					    closingBalance=closingBalance+((openingBalanceforGroup)+(total_creditGroup-total_debitGroup));
					}
					else
					{
						for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
						{
							if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
							{
								closingBalance = closingBalance+((obalanceBeStartDate.getTotalcredit_balance()-obalanceBeStartDate.getTotaldebit_balance())+(obalance.getTotalcredit_balance()-obalance.getTotaldebit_balance()));
							}
							
						}
					}
					if(closingBalance!=0)
					{
						liabilitiesTable.addCell(new Phrase(obalance.getAccountGroupName(), boldFont));
						liabilitiesTable.addCell("");
						liabilitiesTable.addCell(commonService.round(closingBalance, 2).toString());
						
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
							if(accSubGroup.equals("Trade Payables"))
							{
								double openingBalanceSubgroup = (total_credit_Subgroup_beforeStartDate+total_credit_Supplier_beforeStartDate)-(total_debit_Subgroup_beforeStartDate+total_debit_Supplier_beforeStartDate);
								double total_debitSubgroup =(total_debit_Subgroup+total_debit_Supplier);
								double total_creditSubgroup = (total_credit_Subgroup+total_credit_Supplier);
								closingBalanceOfSubgroup=closingBalanceOfSubgroup+(openingBalanceSubgroup)+(total_creditSubgroup-total_debitSubgroup);
							}
							else
							{
								closingBalanceOfSubgroup=closingBalanceOfSubgroup+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup);
							}
							
							if(closingBalanceOfSubgroup!=0)
							{
								liabilitiesTable.addCell(new Phrase("     "+accSubGroup));
								liabilitiesTable.addCell(commonService.round(closingBalanceOfSubgroup, 2).toString());
								liabilitiesTable.addCell("");
								
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
												liabilitiesTable.addCell(new Phrase("       "+ledgerform3.getLedgerName()));
												liabilitiesTable.addCell(commonService.round((openiBalanceOfLedger+total_creditOfLedger-total_debitOfLedger), 2).toString());
												liabilitiesTable.addCell("");
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
														liabilitiesTable.addCell(new Phrase("         "+subledger.getSubledgerName()));
														liabilitiesTable.addCell(commonService.round((openiBalanceOfSubLedger+total_creditOfSubLedger-total_debitOfSubLedger), 2).toString());
														liabilitiesTable.addCell("");
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
																				liabilitiesTable.addCell(new Phrase("       "+ledgerform6.getLedgerName()));
																				liabilitiesTable.addCell(commonService.round((openiBalanceOfLedger), 2).toString());
																				liabilitiesTable.addCell("");
																				
																				if(ledgerform6.getSubledgerList().size()>0)
																				{
																					for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																					{
																						double openiBalanceOfSubLedger = (double) 0;
																						openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																						if(openiBalanceOfSubLedger!=0)
																						{
																							liabilitiesTable.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																							liabilitiesTable.addCell(commonService.round((openiBalanceOfSubLedger), 2).toString());
																							liabilitiesTable.addCell("");
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
															liabilitiesTable.addCell(new Phrase("       "+ledgerform6.getLedgerName()));
															liabilitiesTable.addCell(commonService.round((openiBalanceOfLedger), 2).toString());
															liabilitiesTable.addCell("");
															if(ledgerform6.getSubledgerList().size()>0)
															{
																for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																{
																	double openiBalanceOfSubLedger = (double) 0;
																	openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																	if(openiBalanceOfSubLedger!=0)
																	{
																		liabilitiesTable.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																		liabilitiesTable.addCell(commonService.round((openiBalanceOfSubLedger), 2).toString());
																		liabilitiesTable.addCell("");
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
								
								if(accSubGroup.equals("Trade Payables"))
								{
									for(OpeningBalancesForm supOpbalance:supplierOpeningBalanceList) 
									{
										double supOpeningbalance = (double) 0;
										double supDebitbalance = (double) 0;
										double supCreditbalance = (double) 0;
										
										for(OpeningBalancesForm supOpbalance1:supplierOpeningBalanceBeforeStartDate) 
										{
											if(supOpbalance1.getSupplierName().equals(supOpbalance.getSupplierName()))
											{
												supOpeningbalance=supOpeningbalance+supOpbalance1.getCredit_balance()-supOpbalance1.getDebit_balance();
											}
										}
										supDebitbalance=supDebitbalance+supOpbalance.getDebit_balance();
										supCreditbalance=supCreditbalance+supOpbalance.getCredit_balance();
										if(supOpeningbalance!=0 || supDebitbalance!=0 || supCreditbalance!=0)
										{
											liabilitiesTable.addCell(new Phrase("         "+supOpbalance.getSupplierName()));
											liabilitiesTable.addCell(commonService.round(((supOpeningbalance)+(supCreditbalance-supDebitbalance)), 2).toString());
											liabilitiesTable.addCell("");
										}
									}
									
									for(OpeningBalancesForm supOpbalance1:supplierOpeningBalanceBeforeStartDate) 
									{
										double openiBalanceOfsupplier = (double) 0;
										int issupplierPresent = 0;
										for(OpeningBalancesForm supOpbalance:supplierOpeningBalanceList) 
										{
											if(supOpbalance1.getSupplierName().equals(supOpbalance.getSupplierName()))
											{
												issupplierPresent=issupplierPresent+1;
											}
											
										}
										if(issupplierPresent==0)
										{
											openiBalanceOfsupplier=openiBalanceOfsupplier+supOpbalance1.getCredit_balance()-supOpbalance1.getDebit_balance();
											if(openiBalanceOfsupplier!=0)
											{
												liabilitiesTable.addCell(new Phrase("         "+supOpbalance1.getSupplierName()));
												liabilitiesTable.addCell(commonService.round((openiBalanceOfsupplier), 2).toString());
												liabilitiesTable.addCell("");
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
			if(totalCreditSideAmount1>totalDebitSideAmount1)
			{
				liabilitiesTable.addCell(new Phrase("Profit & Loss A/c", boldFont));
				liabilitiesTable.addCell("");
				liabilitiesTable.addCell(commonService.round(totalCreditSideAmount1-totalDebitSideAmount1, 2).toString());
				totalNetProfit=totalNetProfit+(totalCreditSideAmount1-totalDebitSideAmount1);
				
			}
			if(totalDebitSideAmount1>totalCreditSideAmount1)
			{
				liabilitiesTable.addCell(new Phrase("Profit & Loss A/c", boldFont));
				liabilitiesTable.addCell("");
				liabilitiesTable.addCell(commonService.round(totalCreditSideAmount1-totalDebitSideAmount1, 2).toString());
				totalNetProfit=totalNetProfit+(totalCreditSideAmount1-totalDebitSideAmount1);
				
			}
			
			if(debitCountforAdujustingRow>creditCountforAdujustingRow)
			{
				if(debitCountforAdujustingRow-creditCountforAdujustingRow!=0)
				{
					for(int i=1;i<=debitCountforAdujustingRow-creditCountforAdujustingRow;i++)
					{
						liabilitiesTable.addCell("-");
						liabilitiesTable.addCell("-");
						liabilitiesTable.addCell("-");
					}
				}
			}
			
			liabilitiesTable.addCell(new Phrase("Total", boldFont));
			liabilitiesTable.addCell("");
			liabilitiesTable.addCell(new Phrase(commonService.round(totalCreditSideAmount+totalNetProfit, 2).toString(), boldFont));
	
             for (OpeningBalancesOfSubedgerForm obalance : subOpeningList) {
				
				if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)3))
				{
					double closingBalance = (double) 0;
					
					if(obalance.getAccountGroupName().equals("Current Assets"))
					{
						double openingBalanceforGroup = (double)0;
						for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
						{
							if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
							{
								 openingBalanceforGroup = openingBalanceforGroup+(obalanceBeStartDate.getTotaldebit_balance()+total_debit_Customer_beforeStartDate+total_debit_Bank_beforeStartDate)-(obalanceBeStartDate.getTotalcredit_balance()+total_credit_Customer_beforeStartDate+total_credit_Bank_beforeStartDate);
							}
							
						}
						double total_debitGroup = (obalance.getTotaldebit_balance()+total_debit_Customer+total_debit_Bank);
					    double total_creditGroup = (obalance.getTotalcredit_balance()+total_credit_Customer+total_credit_Bank);
					    closingBalance=closingBalance+(openingBalanceforGroup)+(total_debitGroup-total_creditGroup);
					}
					else
					{
						for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
						{
							if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
							{
								closingBalance = closingBalance+(obalanceBeStartDate.getTotaldebit_balance()-obalanceBeStartDate.getTotalcredit_balance())+(obalance.getTotaldebit_balance()-obalance.getTotalcredit_balance());
							}
							
						}
					}
					if(closingBalance!=0)
					{
						assetsTable.addCell(new Phrase(obalance.getAccountGroupName(), boldFont));
						assetsTable.addCell("");
						assetsTable.addCell(commonService.round(closingBalance, 2).toString());
						
		
				
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
							if(accSubGroup.equals("Trade Receivables")||accSubGroup.equals("Cash & Bank Balances"))
							{
								if(accSubGroup.equals("Trade Receivables"))
								{
									double openingBalanceSubgroup = (total_debit_Subgroup_beforeStartDate+total_debit_Customer_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Customer_beforeStartDate);
									double total_debitSubgroup =(total_debit_Subgroup+total_debit_Customer);
									double total_creditSubgroup = (total_credit_Subgroup+total_credit_Customer);
									closingBalanceOfSubgroup=closingBalanceOfSubgroup+(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup);
								}
								else if(accSubGroup.equals("Cash & Bank Balances"))
								{
									double openingBalanceSubgroup = (total_debit_Subgroup_beforeStartDate+total_debit_Bank_beforeStartDate)-(total_credit_Subgroup_beforeStartDate+total_credit_Bank_beforeStartDate);
									double total_debitSubgroup =(total_debit_Subgroup+total_debit_Bank);
									double total_creditSubgroup = (total_credit_Subgroup+total_credit_Bank);
									closingBalanceOfSubgroup=closingBalanceOfSubgroup+(openingBalanceSubgroup)+(total_debitSubgroup-total_creditSubgroup);
								}
								
							}
							else
							{
								closingBalanceOfSubgroup=closingBalanceOfSubgroup+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup);
							}
							
							if(closingBalanceOfSubgroup!=0)
							{
								assetsTable.addCell(new Phrase("       "+accSubGroup));
								assetsTable.addCell(commonService.round(closingBalanceOfSubgroup, 2).toString());
								assetsTable.addCell("");
								
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
												assetsTable.addCell(new Phrase("       "+ledgerform3.getLedgerName()));
												assetsTable.addCell(commonService.round((openiBalanceOfLedger+total_debitOfLedger-total_creditOfLedger), 2).toString());
												assetsTable.addCell("");
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
														assetsTable.addCell(new Phrase("         "+subledger.getSubledgerName()));
														assetsTable.addCell(commonService.round((openiBalanceOfSubLedger+total_debitOfSubLedger-total_creditOfSubLedger), 2).toString());
														assetsTable.addCell("");
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
																				assetsTable.addCell(new Phrase("       "+ledgerform6.getLedgerName()));
																				assetsTable.addCell(commonService.round((openiBalanceOfLedger), 2).toString());
																				assetsTable.addCell("");
																				
																				if(ledgerform6.getSubledgerList().size()>0)
																				{
																					for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																					{
																						double openiBalanceOfSubLedger = (double) 0;
																						openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																						if(openiBalanceOfSubLedger!=0)
																						{
																							assetsTable.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																							assetsTable.addCell(commonService.round((openiBalanceOfSubLedger), 2).toString());
																							assetsTable.addCell("");
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
															assetsTable.addCell(new Phrase("       "+ledgerform6.getLedgerName()));
															assetsTable.addCell(commonService.round((openiBalanceOfLedger), 2).toString());
															assetsTable.addCell("");
															if(ledgerform6.getSubledgerList().size()>0)
															{
																for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																{
																	double openiBalanceOfSubLedger = (double) 0;
																	openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																	if(openiBalanceOfSubLedger!=0)
																	{
																		assetsTable.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																		assetsTable.addCell(commonService.round((openiBalanceOfSubLedger), 2).toString());
																		assetsTable.addCell("");
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
								
								if(accSubGroup.equals("Cash & Bank Balances"))
								{
									for(OpeningBalancesForm bankOpbalance:bankOpeningBalanceList) 
									{
										double bankOpeningbalance = (double) 0;
										double bankDebitbalance = (double) 0;
										double bankCreditbalance = (double) 0;
										
										for(OpeningBalancesForm bankOpbalance1:bankOpeningBalanceBeforeStartDate) 
										{
											if(bankOpbalance1.getBankName().equals(bankOpbalance.getBankName()))
											{
												bankOpeningbalance=bankOpeningbalance+bankOpbalance1.getDebit_balance()-bankOpbalance1.getCredit_balance();
											}
										}
										bankDebitbalance=bankDebitbalance+bankOpbalance.getDebit_balance();
										bankCreditbalance=bankCreditbalance+bankOpbalance.getCredit_balance();
										if(bankOpeningbalance!=0 || bankDebitbalance!=0 || bankCreditbalance!=0)
										{
											assetsTable.addCell(new Phrase("         "+bankOpbalance.getBankName()));
											assetsTable.addCell(commonService.round(((bankOpeningbalance)+(bankCreditbalance-bankDebitbalance)), 2).toString());
											assetsTable.addCell("");
										}
									}
									
									for(OpeningBalancesForm bankOpbalance1:bankOpeningBalanceBeforeStartDate) 
									{
										double openiBalanceOfbank = (double) 0;
										int isBankPresent = 0;
										for(OpeningBalancesForm bankOpbalance:bankOpeningBalanceList) 
										{
											if(bankOpbalance1.getBankName().equals(bankOpbalance.getBankName()))
											{
												isBankPresent=isBankPresent+1;
											}
											
										}
										if(isBankPresent==0)
										{
											openiBalanceOfbank=openiBalanceOfbank+bankOpbalance1.getDebit_balance()-bankOpbalance1.getCredit_balance();
											if(openiBalanceOfbank!=0)
											{
												assetsTable.addCell(new Phrase("         "+bankOpbalance1.getBankName()));
												assetsTable.addCell(commonService.round((openiBalanceOfbank), 2).toString());
												assetsTable.addCell("");
											}
										}
									}
								}
								
								if(accSubGroup.equals("Trade Receivables"))
								{
									for(OpeningBalancesForm cusoPbalance:customerOpeningBalanceList) 
									{
										double cusOpeningbalance = (double) 0;
										double cusDebitbalance = (double) 0;
										double cusCreditbalance = (double) 0;
										
										for(OpeningBalancesForm cusoPbalance1:customerOpeningBalanceBeforeStartDate) 
										{
											if(cusoPbalance1.getCustomerName().equals(cusoPbalance.getCustomerName()))
											{
												cusOpeningbalance=cusOpeningbalance+cusoPbalance1.getDebit_balance()-cusoPbalance1.getCredit_balance();
											}
										}
										cusDebitbalance=cusDebitbalance+cusoPbalance.getDebit_balance();
										cusCreditbalance=cusCreditbalance+cusoPbalance.getCredit_balance();
										if(cusOpeningbalance!=0 || cusDebitbalance!=0 || cusCreditbalance!=0)
										{
											assetsTable.addCell(new Phrase("         "+cusoPbalance.getCustomerName()));
											assetsTable.addCell(commonService.round(((cusOpeningbalance)+(cusCreditbalance-cusDebitbalance)), 2).toString());
											assetsTable.addCell("");
										}
									}
									
									for(OpeningBalancesForm cusoPbalance1:customerOpeningBalanceBeforeStartDate) 
									{
										double openiBalanceOfcustomer = (double) 0;
										int iscustomerPresent = 0;
										for(OpeningBalancesForm cusoPbalance:customerOpeningBalanceList) 
										{
											if(cusoPbalance1.getCustomerName().equals(cusoPbalance.getCustomerName()))
											{
												iscustomerPresent=iscustomerPresent+1;
											}
											
										}
										if(iscustomerPresent==0)
										{
											openiBalanceOfcustomer=openiBalanceOfcustomer+cusoPbalance1.getDebit_balance()-cusoPbalance1.getCredit_balance();
											if(openiBalanceOfcustomer!=0)
											{
												assetsTable.addCell(new Phrase("         "+cusoPbalance1.getCustomerName()));
												assetsTable.addCell(commonService.round((openiBalanceOfcustomer), 2).toString());
												assetsTable.addCell("");
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

             if(creditCountforAdujustingRow>debitCountforAdujustingRow)
 			{
 				if(creditCountforAdujustingRow-debitCountforAdujustingRow!=0)
 				{
 					for(int i=1;i<=creditCountforAdujustingRow-debitCountforAdujustingRow;i++)
 					{
 						assetsTable.addCell("-");
 						assetsTable.addCell("-");
 						assetsTable.addCell("-");
 					}
 				}
 			}
			
             assetsTable.addCell(new Phrase("Total", boldFont));
             assetsTable.addCell("");
             assetsTable.addCell(new Phrase(commonService.round(totalDebitSideAmount, 2).toString(), boldFont));
             Paragraph paragraph = new Paragraph(new Phrase("    "));
             paragraph.setAlignment(Paragraph.ALIGN_LEFT);
     		 document.add(paragraph);
             mainTable.addCell(liabilitiesTable);
             mainTable.addCell(assetsTable);
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
