package com.fasset.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.SubLedger;
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

public class PDFBuilderForTrialBalancePdf extends PDFBuilderForTrialBalancePdfView {

	private CommonServiceImpl commonService = new CommonServiceImpl();

	public void buildPdfDocumentForQuotation(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response,
			List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesbeforestartDate,
			List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesOfsubledger,
			List<OpeningBalancesForm> bankOpeningBalanceList, List<OpeningBalancesForm> supplierOpeningBalanceList,
			List<OpeningBalancesForm> customerOpeningBalanceList,
			List<OpeningBalancesForm> bankOpeningBalanceBeforeStartDate,
			List<OpeningBalancesForm> supplierOpeningBalanceBeforeStartDate,
			List<OpeningBalancesForm> customerOpeningBalanceBeforeStartDate, LocalDate fromdate, LocalDate todate,
			Company company) throws Exception
	 {
		List<OpeningBalancesOfSubedgerForm> subOpeningList = ListForOpeningbalancesOfsubledger;
		List<OpeningBalancesOfSubedgerForm> subOpeningListBeforeStartDate = ListForOpeningbalancesbeforestartDate;

		document.setPageSize(PageSize.A2);
		document.open();
		if (company != null) {
			Paragraph companyName = new Paragraph(new Phrase("Company Name:" + " " + company.getCompany_name()));
			companyName.setAlignment(Paragraph.ALIGN_LEFT);
			document.add(companyName);

			if (company.getPermenant_address() != null) {

				Paragraph address = new Paragraph(new Phrase("Address:" + " " + company.getPermenant_address()));
				address.setAlignment(Paragraph.ALIGN_LEFT);
				document.add(address);

			}
			if (company.getRegistration_no() != null) {
				if (company.getCompany_statutory_type().getCompany_statutory_id().toString().equalsIgnoreCase("10")
						|| company.getCompany_statutory_type().getCompany_statutory_id().toString()
								.equalsIgnoreCase("14")) {
					Paragraph cinNo = new Paragraph(new Phrase("CIN NO:" + " " + company.getRegistration_no()));
					cinNo.setAlignment(Paragraph.ALIGN_LEFT);
					document.add(cinNo);
				}
			}
		}

		Paragraph para = new Paragraph(new Phrase("Trial Balance Report"));
		para.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(para);

		if (fromdate != null && todate != null) {

			Paragraph date = new Paragraph(
					new Phrase("From" + " " + ClasstoConvertDateformat.dateFormat(fromdate.toString()) + " " + "To"
							+ " " + ClasstoConvertDateformat.dateFormat(todate.toString())));
			date.setAlignment(Paragraph.ALIGN_LEFT);
			document.add(date);
		}

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100.0f);
		table.setSpacingBefore(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);

		Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLACK);
		cell.setPadding(5);

		cell.setPhrase(new Phrase("Particulars", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Opening Balance", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Debit", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Credit", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Closing Balance", font));
		table.addCell(cell);

		double row_count_credit = (double) 0;
		double row_count_debit = (double) 0;

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
		for (OpeningBalancesForm obalance : supplierOpeningBalanceBeforeStartDate) {
			total_credit_Supplier_beforeStartDate = total_credit_Supplier_beforeStartDate
					+ obalance.getCredit_balance();
			total_debit_Supplier_beforeStartDate = total_debit_Supplier_beforeStartDate + obalance.getDebit_balance();
		}

		for (OpeningBalancesForm obalance : supplierOpeningBalanceList) {
			total_credit_Supplier = total_credit_Supplier + obalance.getCredit_balance();
			total_debit_Supplier = total_debit_Supplier + obalance.getDebit_balance();
		}

		for (OpeningBalancesForm obalance : customerOpeningBalanceBeforeStartDate) {
			total_credit_Customer_beforeStartDate = total_credit_Customer_beforeStartDate
					+ obalance.getCredit_balance();
			total_debit_Customer_beforeStartDate = total_debit_Customer_beforeStartDate + obalance.getDebit_balance();
		}
		for (OpeningBalancesForm obalance : customerOpeningBalanceList) {
			total_credit_Customer = total_credit_Customer + obalance.getCredit_balance();
			total_debit_Customer = total_debit_Customer + obalance.getDebit_balance();
		}

		for (OpeningBalancesForm obalance : bankOpeningBalanceBeforeStartDate) {
			total_credit_Bank_beforeStartDate = total_credit_Bank_beforeStartDate + obalance.getCredit_balance();
			total_debit_Bank_beforeStartDate = total_debit_Bank_beforeStartDate + obalance.getDebit_balance();
		}
		for (OpeningBalancesForm obalance : bankOpeningBalanceList) {
			total_credit_Bank = total_credit_Bank + obalance.getCredit_balance();
			total_debit_Bank = total_debit_Bank + obalance.getDebit_balance();
		}

		for (OpeningBalancesOfSubedgerForm obalance : subOpeningList)
		{
			if (obalance.getGroup().getPostingSide().getPosting_id().equals((long) 3)) {
				double openingBalanceforGroup = (double) 0;
				double total_debitGroup = (double) 0;
				double total_creditGroup = (double) 0;

				for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
					if (obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName())) {
						if (obalance.getAccountGroupName().equals("Current Assets")) {
							openingBalanceforGroup = openingBalanceforGroup
									+ (obalanceBeStartDate.getTotaldebit_balance()
											+ total_debit_Customer_beforeStartDate + total_debit_Bank_beforeStartDate)
									- (obalanceBeStartDate.getTotalcredit_balance()
											+ total_credit_Customer_beforeStartDate
											+ total_credit_Bank_beforeStartDate);
							total_debitGroup = (obalance.getTotaldebit_balance() + total_debit_Customer
									+ total_debit_Bank);
							total_creditGroup = (obalance.getTotalcredit_balance() + total_credit_Customer
									+ total_credit_Bank);
						} else {
							openingBalanceforGroup = openingBalanceforGroup
									+ (obalanceBeStartDate.getTotaldebit_balance())
									- (obalanceBeStartDate.getTotalcredit_balance());
							total_debitGroup = (obalance.getTotaldebit_balance());
							total_creditGroup = (obalance.getTotalcredit_balance());
						}
					}
				}

				if (openingBalanceforGroup != 0 || total_debitGroup != 0 || total_creditGroup != 0) {
					table.addCell(obalance.getAccountGroupName());
					table.addCell((commonService.round(openingBalanceforGroup, 2).toString()));
					table.addCell((commonService.round(total_debitGroup, 2).toString()));
					table.addCell((commonService.round(total_creditGroup, 2).toString()));
					table.addCell((commonService
							.round((openingBalanceforGroup) + (total_debitGroup - total_creditGroup), 2).toString()));
				}

				// start of SubGroup
				for (String accSubGroup : obalance.getAccountSubGroupNameList()) 
				{
					double total_credit_Subgroup_beforeStartDate = (double) 0;
					double total_debit_Subgroup_beforeStartDate = (double) 0;
					double total_credit_Subgroup = (double) 0;
					double total_debit_Subgroup = (double) 0;

					for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
						for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
							if (subGroupName.equalsIgnoreCase(accSubGroup)) {
								for (LedgerForm ledgerform1 : obalanceBeStartDate.getLedgerformlist()) {
									if (ledgerform1.getSubgroupName().equals(accSubGroup)) {
										total_credit_Subgroup_beforeStartDate = total_credit_Subgroup_beforeStartDate
												+ ledgerform1.getLedgercredit_balance();
										total_debit_Subgroup_beforeStartDate = total_debit_Subgroup_beforeStartDate
												+ ledgerform1.getLedgerdebit_balance();
									}
								}
							}
						}
					}

					if (obalance.getLedgerformlist().size() > 0) {
						for (LedgerForm ledgerform2 : obalance.getLedgerformlist()) {
							if (ledgerform2.getSubgroupName().equals(accSubGroup)) {
								total_credit_Subgroup = total_credit_Subgroup + ledgerform2.getLedgercredit_balance();
								total_debit_Subgroup = total_debit_Subgroup + ledgerform2.getLedgerdebit_balance();
							}
						}
					}

					double openingBalanceSubgroup = (double) 0;
					double total_debitSubgroup = (double) 0;
					double total_creditSubgroup = (double) 0;

					if (accSubGroup.equalsIgnoreCase("Trade Receivables")
							|| accSubGroup.equalsIgnoreCase("Cash & Bank Balances")) {
						if (accSubGroup.equalsIgnoreCase("Trade Receivables")) {
							openingBalanceSubgroup = (total_debit_Subgroup_beforeStartDate
									+ total_debit_Customer_beforeStartDate)
									- (total_credit_Subgroup_beforeStartDate + total_credit_Customer_beforeStartDate);
							total_debitSubgroup = total_debitSubgroup + total_debit_Customer;
							total_credit_Subgroup = total_credit_Subgroup + total_credit_Customer;
						} else if (accSubGroup.equalsIgnoreCase("Cash & Bank Balances")) {
							openingBalanceSubgroup = (total_debit_Subgroup_beforeStartDate
									+ total_debit_Bank_beforeStartDate)
									- (total_credit_Subgroup_beforeStartDate + total_credit_Bank_beforeStartDate);
							total_debitSubgroup = total_debitSubgroup + total_debit_Bank;
							total_credit_Subgroup = total_credit_Subgroup + total_credit_Bank;
						}
					} else {
						openingBalanceSubgroup = total_debit_Subgroup_beforeStartDate
								- total_credit_Subgroup_beforeStartDate;
						total_debitSubgroup = total_debitSubgroup;
						total_credit_Subgroup = total_credit_Subgroup;
					}

					if (openingBalanceSubgroup != 0 || total_debitSubgroup != 0 || total_creditSubgroup != 0) {
						table.addCell(new Phrase("    "+accSubGroup));
						table.addCell((commonService.round(openingBalanceSubgroup, 2).toString()));
						table.addCell((commonService.round(total_debitSubgroup, 2).toString()));
						table.addCell((commonService.round(total_creditSubgroup, 2).toString()));
						table.addCell((commonService
								.round((openingBalanceSubgroup) + (total_debitSubgroup - total_creditSubgroup), 2)
								.toString()));

						// start of ledger
						for (LedgerForm ledgerform3 : obalance.getLedgerformlist()) {
							if (ledgerform3 != null) {
								if (ledgerform3.getSubgroupName().equals(accSubGroup)) {
									double openiBalanceOfLedger = (double) 0;
									double total_debitOfLedger = (double) 0;
									double total_creditOfLedger = (double) 0;

									for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
										for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
											if (subGroupName.equalsIgnoreCase(accSubGroup)) {
												for (LedgerForm ledgerform4 : obalanceBeStartDate.getLedgerformlist()) {
													if (ledgerform4.getLedgerName()
															.equalsIgnoreCase(ledgerform3.getLedgerName())) {
														if (ledgerform4.getSubgroupName().equals(accSubGroup)) {
															openiBalanceOfLedger = openiBalanceOfLedger
																	+ (ledgerform4.getLedgerdebit_balance()
																			- ledgerform4.getLedgercredit_balance());
														}
													}
												}
											}
										}
									}

									total_debitOfLedger = ledgerform3.getLedgerdebit_balance();
									total_creditOfLedger = ledgerform3.getLedgercredit_balance();

									if (openiBalanceOfLedger != 0 || total_debitOfLedger != 0
											|| total_creditOfLedger != 0) {
										
										table.addCell((new Phrase("       "+ledgerform3.getLedgerName())));
										table.addCell((commonService.round(openiBalanceOfLedger, 2).toString()));
										table.addCell((commonService.round(total_debitOfLedger, 2).toString()));
										table.addCell((commonService.round(total_creditOfLedger, 2).toString()));
										table.addCell(
												(commonService
														.round((openiBalanceOfLedger)
																+ (total_debitOfLedger - total_creditOfLedger), 2)
														.toString()));
									}

								}
								// start of subledger

								if (ledgerform3.getSubgroupName().equals(accSubGroup)) {
									for (OpeningBalancesForm subledger : ledgerform3.getSubledgerList()) {
										if (subledger != null) {
											double openiBalanceOfSubLedger = (double) 0;
											double total_debitOfSubLedger = (double) 0;
											double total_creditOfSubLedger = (double) 0;
											for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
												for (String subGroupName : obalanceBeStartDate
														.getAccountSubGroupNameList()) {
													if (subGroupName.equalsIgnoreCase(accSubGroup)) {
														for (LedgerForm ledgerform5 : obalanceBeStartDate
																.getLedgerformlist()) {
															if (ledgerform5.getSubgroupName().equals(accSubGroup)) {
																for (OpeningBalancesForm subledger1 : ledgerform5
																		.getSubledgerList()) {
																	if (subledger1 != null) {
//						    			    									 /	test="${subledger1.subledgerName==subledger.subledgerName}">
																		if (subledger1.getSubledgerName()
																				.equalsIgnoreCase(
																						subledger.getSubledgerName())) {
																			openiBalanceOfSubLedger = openiBalanceOfSubLedger
																					+ (subledger1.getDebit_balance()-subledger1.getCredit_balance());
																		}
																	}
																}
															}
														}

													}
												}
											}

											total_debitOfSubLedger = subledger.getDebit_balance();
											total_creditOfSubLedger = subledger.getCredit_balance();
											
											if (openiBalanceOfSubLedger != 0 || total_debitOfSubLedger != 0
													|| total_creditOfSubLedger != 0) {
												table.addCell(new Phrase("         "+subledger.getSubledgerName())); 
												
												table.addCell(
														(commonService.round(openiBalanceOfSubLedger, 2).toString()));
												table.addCell(
														(commonService.round(total_debitOfSubLedger, 2).toString()));
												row_count_debit=row_count_debit+total_debitOfSubLedger;
												table.addCell(
														(commonService.round(total_creditOfSubLedger, 2).toString()));
												row_count_credit=row_count_credit+total_creditOfSubLedger;
												table.addCell((commonService
														.round((openiBalanceOfSubLedger)
																+ (total_debitOfSubLedger - total_creditOfSubLedger), 2)
														.toString()));
											}

										}
									}
								}
							}
						}

						// opening balance start Date
						for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) 
						{
							if (obalanceBeStartDate.getAccountSubGroupNameList().size() > 0) {
								for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
									if (subGroupName.equals(accSubGroup)) {
										if (obalanceBeStartDate.getLedgerformlist().size() > 0) {
											for (LedgerForm ledgerform6 : obalanceBeStartDate.getLedgerformlist()) {
												double isNotPresent = (double) 0;
												double isPresent = (double) 0;
												if (ledgerform6.getSubgroupName().equalsIgnoreCase(accSubGroup)) {
													if (obalance.getLedgerformlist().size() > 0) {
														for (LedgerForm ledgerform7 : obalance.getLedgerformlist()) {
															if (ledgerform7.getLedgerName()
																	.equals(ledgerform6.getLedgerName())) {
																isPresent = 1;
															}

														}
														if (isPresent == 0) {
															// obalance.ledgerformlist.size()>0
															if (obalance.getLedgerformlist().size() > 0) {
																for (LedgerForm ledgerform7 : obalance
																		.getLedgerformlist()) {
//					    			    			 						/ledgerform7.ledgerName!=ledgerform6.ledgerName
																	if (ledgerform7.getLedgerName() != ledgerform6
																			.getLedgerName()) {
																		if (isNotPresent == 0) {
																			double openiBalanceOfLedger = (double) 0;

																			table.addCell(new Phrase("       "+ledgerform6.getLedgerName()));

																			openiBalanceOfLedger=openiBalanceOfLedger+(ledgerform6.getLedgerdebit_balance()-ledgerform6.getLedgercredit_balance());
																			table.addCell((commonService
																					.round(openiBalanceOfLedger, 2)
																					.toString()));
																			table.addCell("0");
																			table.addCell("0");
																			table.addCell((commonService
																					.round((openiBalanceOfLedger), 2)
																					.toString()));

																			if (ledgerform6.getSubledgerList()
																					.size() > 0) {
																				for (OpeningBalancesForm subledger1 : ledgerform6
																						.getSubledgerList()) {
																					if (subledger1 != null) {
																						double openiBalanceOfSubLedger = (double) 0;
																						// ${openiBalanceOfSubLedger +
																						// (subledger1.debit_balance-subledger1.credit_balance)}
																						openiBalanceOfSubLedger = openiBalanceOfSubLedger
																								+ (subledger1
																										.getDebit_balance()
																								- subledger1
																										.getCredit_balance());
																						if (openiBalanceOfSubLedger != 0) {
																							table.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																							
																							table.addCell((commonService
																									.round(openiBalanceOfSubLedger,
																											2)
																									.toString()));
																							table.addCell("0");
																							table.addCell("0");
																							table.addCell((commonService
																									.round((openiBalanceOfSubLedger),
																											2)
																									.toString()));

																						}
																					}
																				}
																			}
																			isNotPresent = 1;
																		}
																	}
																}

															}
														}

													}

													if (obalance.getLedgerformlist().size() == 0)
													{
														double openiBalanceOfLedger = (double) 0;
														table.addCell(new Phrase("       "+ledgerform6.getLedgerName())); 
														
														openiBalanceOfLedger = openiBalanceOfLedger
																+ (ledgerform6.getLedgerdebit_balance()
																- ledgerform6.getLedgercredit_balance());
														table.addCell((commonService.round(openiBalanceOfLedger, 2)
																.toString()));
														table.addCell("0");
														table.addCell("0");
														table.addCell((commonService.round((openiBalanceOfLedger), 2)
																.toString()));

														if (ledgerform6.getSubledgerList().size() > 0) {
															for (OpeningBalancesForm subledger1 : ledgerform6
																	.getSubledgerList()) {
																if (subledger1 != null) {
																	double openiBalanceOfSubLedger = 0;
																	openiBalanceOfSubLedger = openiBalanceOfSubLedger
																			+ (subledger1.getDebit_balance()
																			- subledger1.getCredit_balance());
																	if (openiBalanceOfSubLedger != 0) {
																		 table.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																		table.addCell((commonService
																				.round(openiBalanceOfSubLedger, 2)
																				.toString()));
																		table.addCell("0");
																		table.addCell("0");
																		table.addCell((commonService
																				.round((openiBalanceOfSubLedger), 2)
																				.toString()));
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
						} // opening start date end

						if (accSubGroup.equalsIgnoreCase("Cash & Bank Balances")) 
						{
							for (OpeningBalancesForm bankOpbalance : bankOpeningBalanceList) {
								double bankOpeningbalance = (double) 0;
								double bankDebitbalance = (double) 0;
								double bankCreditbalance = (double) 0;
								for (OpeningBalancesForm bankOpbalance1 : bankOpeningBalanceBeforeStartDate) {
									// bankOpbalance1.bankName == bankOpbalance.bankName
									if (bankOpbalance1.getBankName().equals(bankOpbalance.getBankName())) {
										bankOpeningbalance = bankOpeningbalance + (bankOpbalance1.getDebit_balance() - bankOpbalance1.getCredit_balance());
									}
								}

								bankDebitbalance = bankOpbalance.getDebit_balance();
								bankCreditbalance = bankOpbalance.getCredit_balance();
								
								if (bankOpeningbalance != 0 || bankDebitbalance != 0 || bankCreditbalance != 0) {
									table.addCell(new Phrase("         "+bankOpbalance.getBankName()));
									table.addCell((commonService.round(bankOpeningbalance, 2).toString()));
									table.addCell(
											(commonService.round(bankDebitbalance, 2).toString()));
									row_count_debit = row_count_debit + bankDebitbalance;
									table.addCell((commonService.round(bankCreditbalance, 2).toString()));
									row_count_credit = row_count_credit + bankCreditbalance;
									table.addCell((commonService
											.round((bankOpeningbalance) + (bankDebitbalance - bankCreditbalance), 2)
											.toString()));

								}
							}

							for (OpeningBalancesForm bankOpbalance1 : bankOpeningBalanceBeforeStartDate) {
								double isBankPresent = 0;
								double openiBalanceOfbank = 0;

								for (OpeningBalancesForm bankOpbalance : bankOpeningBalanceList) {
									// bankOpbalance1.bankName == bankOpbalance.bankName
									if (bankOpbalance1.getBankName().equals(bankOpbalance.getBankName())) {
										isBankPresent = 1;
									}
								}
								if (isBankPresent == 0) {
									openiBalanceOfbank = openiBalanceOfbank
											+ (bankOpbalance1.getDebit_balance() - bankOpbalance1.getCredit_balance());
									if (openiBalanceOfbank != 0) {
										table.addCell(new Phrase("         "+bankOpbalance1.getBankName()));
										table.addCell((commonService.round(openiBalanceOfbank, 2).toString()));
										table.addCell("0");
										table.addCell("0");
										table.addCell((commonService.round(openiBalanceOfbank, 2).toString()));
									}
								}

							}

						} // closed for subgroup3 (Cash & Bank Balances)

						if (accSubGroup.equalsIgnoreCase("Trade Receivables")) 
						{
							for (OpeningBalancesForm cusoPbalance : customerOpeningBalanceList) 
							{
								double cusOpeningbalance = (double) 0;
								double cusDebitbalance = (double) 0;
								double cusCreditbalance = (double) 0;

								for (OpeningBalancesForm cusoPbalance1 : customerOpeningBalanceBeforeStartDate) {
									// cusoPbalance1.customerName == cusoPbalance.customerName
									if (cusoPbalance1.getCustomerName().equals(cusoPbalance.getCustomerName())) {
										cusOpeningbalance = cusOpeningbalance + cusoPbalance1.getDebit_balance()
												- cusoPbalance1.getCredit_balance();
									}
								}

								//
								cusDebitbalance = cusoPbalance.getDebit_balance();
								cusCreditbalance = cusoPbalance.getCredit_balance();
								
								if (cusOpeningbalance != 0 || cusDebitbalance != 0 || cusCreditbalance != 0) {
									table.addCell(new Phrase("         "+cusoPbalance.getCustomerName()));
									table.addCell((commonService.round(cusOpeningbalance, 2).toString()));
									table.addCell((commonService.round(cusDebitbalance, 2).toString()));
									row_count_debit = row_count_debit + cusDebitbalance;
									table.addCell((commonService.round(+cusCreditbalance, 2).toString()));
									row_count_credit = row_count_credit + cusCreditbalance;
									table.addCell((commonService
											.round((cusOpeningbalance) + (cusDebitbalance - cusCreditbalance), 2)
											.toString()));
								}
							}

							for (OpeningBalancesForm cusoPbalance1 : customerOpeningBalanceBeforeStartDate) {
								double iscustomerPresent = 0;
								double openiBalanceOfcustomer = 0;
								
								for (OpeningBalancesForm cusoPbalance : customerOpeningBalanceList) 
								{
									if (cusoPbalance1.getCustomerName().equals(cusoPbalance.getCustomerName())) {
										iscustomerPresent = 1;
									}
								}
								
								if (iscustomerPresent == 0) 
								{
									openiBalanceOfcustomer = openiBalanceOfcustomer
											+ (cusoPbalance1.getDebit_balance() - cusoPbalance1.getCredit_balance());
									if (openiBalanceOfcustomer != 0)
									{
										table.addCell(new Phrase("         "+cusoPbalance1.getCustomerName()));
										table.addCell((commonService.round(openiBalanceOfcustomer, 2).toString()));
										table.addCell("0");
										table.addCell("0");
										table.addCell((commonService.round(openiBalanceOfcustomer, 2).toString()));
									}
								}

							}

						}
					}

				}

			}
		}
		
		
		for (OpeningBalancesOfSubedgerForm obalance : subOpeningList)
		{
			if (obalance.getGroup().getPostingSide().getPosting_id().equals((long) 4)) {
				double openingBalanceforGroup = (double) 0;
				double total_debitGroup = (double) 0;
				double total_creditGroup = (double) 0;

				for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
					if (obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName())) {
						if (obalance.getAccountGroupName().equals("Current Liabilities")) {
							openingBalanceforGroup = openingBalanceforGroup +(obalanceBeStartDate.getTotalcredit_balance()
									+ total_credit_Supplier_beforeStartDate)
									- (obalanceBeStartDate.getTotaldebit_balance()
											+ total_debit_Supplier_beforeStartDate);
									 
							total_debitGroup = (obalance.getTotaldebit_balance() + total_debit_Supplier);
							total_creditGroup = (obalance.getTotalcredit_balance() + total_credit_Supplier);
						} else {
							openingBalanceforGroup = openingBalanceforGroup + (obalanceBeStartDate.getTotalcredit_balance())
									- (obalanceBeStartDate.getTotaldebit_balance());
							total_debitGroup = (obalance.getTotaldebit_balance());
							total_creditGroup = (obalance.getTotalcredit_balance());
						}
					}
				}

				if (openingBalanceforGroup != 0 || total_debitGroup != 0 || total_creditGroup != 0) {
					table.addCell(obalance.getAccountGroupName());
					table.addCell((commonService.round(openingBalanceforGroup, 2).toString()));
					table.addCell((commonService.round(total_debitGroup, 2).toString()));
					table.addCell((commonService.round(total_creditGroup, 2).toString()));
					table.addCell((commonService
							.round((openingBalanceforGroup) + (total_creditGroup-total_debitGroup), 2).toString()));
				}

				// start of SubGroup
				for (String accSubGroup : obalance.getAccountSubGroupNameList()) 
				{
					double total_credit_Subgroup_beforeStartDate = (double) 0;
					double total_debit_Subgroup_beforeStartDate = (double) 0;
					double total_credit_Subgroup = (double) 0;
					double total_debit_Subgroup = (double) 0;

					for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
						for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
							if (subGroupName.equalsIgnoreCase(accSubGroup)) {
								for (LedgerForm ledgerform1 : obalanceBeStartDate.getLedgerformlist()) {
									if (ledgerform1.getSubgroupName().equals(accSubGroup)) {
										total_credit_Subgroup_beforeStartDate = total_credit_Subgroup_beforeStartDate
												+ ledgerform1.getLedgercredit_balance();
										total_debit_Subgroup_beforeStartDate = total_debit_Subgroup_beforeStartDate
												+ ledgerform1.getLedgerdebit_balance();
									}
								}
							}
						}
					}

					if (obalance.getLedgerformlist().size() > 0) {
						for (LedgerForm ledgerform2 : obalance.getLedgerformlist()) {
							if (ledgerform2.getSubgroupName().equals(accSubGroup)) {
								total_credit_Subgroup = total_credit_Subgroup + ledgerform2.getLedgercredit_balance();
								total_debit_Subgroup = total_debit_Subgroup + ledgerform2.getLedgerdebit_balance();
							}
						}
					}

					double openingBalanceSubgroup = (double) 0;
					double total_debitSubgroup = (double) 0;
					double total_creditSubgroup = (double) 0;

					
						if (accSubGroup.equalsIgnoreCase("Trade Payables")) {
							openingBalanceSubgroup = (total_credit_Subgroup_beforeStartDate + total_credit_Supplier_beforeStartDate)-(total_debit_Subgroup_beforeStartDate
									+ total_debit_Supplier_beforeStartDate);
							
							total_debitSubgroup = total_debitSubgroup + total_debit_Supplier;
							total_credit_Subgroup = total_credit_Subgroup + total_credit_Supplier;
						} 
				     else {
						openingBalanceSubgroup = total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate;
							
						total_debitSubgroup = total_debitSubgroup;
						total_credit_Subgroup = total_credit_Subgroup;
					}

					if (openingBalanceSubgroup != 0 || total_debitSubgroup != 0 || total_creditSubgroup != 0) {
						table.addCell(new Phrase("    "+accSubGroup));
						table.addCell((commonService.round(openingBalanceSubgroup, 2).toString()));
						table.addCell((commonService.round(total_debitSubgroup, 2).toString()));
						table.addCell((commonService.round(total_creditSubgroup, 2).toString()));
						table.addCell((commonService
								.round((openingBalanceSubgroup) + (total_creditSubgroup-total_debitSubgroup), 2)
								.toString()));

						// start of ledger
						for (LedgerForm ledgerform3 : obalance.getLedgerformlist()) {
							if (ledgerform3 != null) {
								if (ledgerform3.getSubgroupName().equals(accSubGroup)) {
									double openiBalanceOfLedger = (double) 0;
									double total_debitOfLedger = (double) 0;
									double total_creditOfLedger = (double) 0;

									for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
										for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
											if (subGroupName.equalsIgnoreCase(accSubGroup)) {
												for (LedgerForm ledgerform4 : obalanceBeStartDate.getLedgerformlist()) {
													if (ledgerform4.getLedgerName()
															.equalsIgnoreCase(ledgerform3.getLedgerName())) {
														if (ledgerform4.getSubgroupName().equals(accSubGroup)) {
															openiBalanceOfLedger = openiBalanceOfLedger
																	+ (ledgerform4.getLedgercredit_balance()-ledgerform4.getLedgerdebit_balance()
																			);
														}
													}
												}
											}
										}
									}

									total_debitOfLedger = ledgerform3.getLedgerdebit_balance();
									total_creditOfLedger = ledgerform3.getLedgercredit_balance();

									if (openiBalanceOfLedger != 0 || total_debitOfLedger != 0
											|| total_creditOfLedger != 0) {
										table.addCell(new Phrase("       "+ledgerform3.getLedgerName()));  
										table.addCell((commonService.round(openiBalanceOfLedger, 2).toString()));
										table.addCell((commonService.round(total_debitOfLedger, 2).toString()));
										table.addCell((commonService.round(total_creditOfLedger, 2).toString()));
										table.addCell(
												(commonService
														.round((openiBalanceOfLedger)
																+ (total_creditOfLedger-total_debitOfLedger), 2)
														.toString()));
									}

								}
								// start of subledger

								if (ledgerform3.getSubgroupName().equals(accSubGroup)) {
									for (OpeningBalancesForm subledger : ledgerform3.getSubledgerList()) {
										if (subledger != null) {
											double openiBalanceOfSubLedger = (double) 0;
											double total_debitOfSubLedger = (double) 0;
											double total_creditOfSubLedger = (double) 0;
											for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
												for (String subGroupName : obalanceBeStartDate
														.getAccountSubGroupNameList()) {
													if (subGroupName.equalsIgnoreCase(accSubGroup)) {
														for (LedgerForm ledgerform5 : obalanceBeStartDate
																.getLedgerformlist()) {
															if (ledgerform5.getSubgroupName().equals(accSubGroup)) {
																for (OpeningBalancesForm subledger1 : ledgerform5
																		.getSubledgerList()) {
																	if (subledger1 != null) {
//						    			    									 /	test="${subledger1.subledgerName==subledger.subledgerName}">
																		if (subledger1.getSubledgerName()
																				.equalsIgnoreCase(
																						subledger.getSubledgerName())) {
																			openiBalanceOfSubLedger = openiBalanceOfSubLedger
																					+ (subledger1.getCredit_balance()-subledger1.getDebit_balance());
																		}
																	}
																}
															}
														}

													}
												}
											}

											total_debitOfSubLedger = subledger.getDebit_balance();
											total_creditOfSubLedger = subledger.getCredit_balance();
											
											if (openiBalanceOfSubLedger != 0 || total_debitOfSubLedger != 0
													|| total_creditOfSubLedger != 0) {
												table.addCell(new Phrase("         "+subledger.getSubledgerName()));
												table.addCell(
														(commonService.round(openiBalanceOfSubLedger, 2).toString()));
												table.addCell(
														(commonService.round(total_debitOfSubLedger, 2).toString()));
												row_count_debit=row_count_debit+total_debitOfSubLedger;
												table.addCell(
														(commonService.round(total_creditOfSubLedger, 2).toString()));
												row_count_credit=row_count_credit+total_creditOfSubLedger;
												table.addCell((commonService
														.round((openiBalanceOfSubLedger)
																+ (total_creditOfSubLedger-total_debitOfSubLedger), 2)
														.toString()));
											}

										}
									}
								}
							}
						}

						// opening balance start Date
						for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) 
						{
							if (obalanceBeStartDate.getAccountSubGroupNameList().size() > 0) {
								for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
									if (subGroupName.equals(accSubGroup)) {
										if (obalanceBeStartDate.getLedgerformlist().size() > 0) {
											for (LedgerForm ledgerform6 : obalanceBeStartDate.getLedgerformlist()) {
												double isNotPresent = (double) 0;
												double isPresent = (double) 0;
												if (ledgerform6.getSubgroupName().equalsIgnoreCase(accSubGroup)) {
													if (obalance.getLedgerformlist().size() > 0) {
														for (LedgerForm ledgerform7 : obalance.getLedgerformlist()) {
															if (ledgerform7.getLedgerName()
																	.equals(ledgerform6.getLedgerName())) {
																isPresent = 1;
															}

														}
														if (isPresent == 0) {
															// obalance.ledgerformlist.size()>0
															if (obalance.getLedgerformlist().size() > 0) {
																for (LedgerForm ledgerform7 : obalance
																		.getLedgerformlist()) {
//					    			    			 						/ledgerform7.ledgerName!=ledgerform6.ledgerName
																	if (ledgerform7.getLedgerName() != ledgerform6
																			.getLedgerName()) {
																		if (isNotPresent == 0) {
																			double openiBalanceOfLedger = (double) 0;

																			table.addCell(new Phrase("       "+ledgerform6.getLedgerName()));	
																			openiBalanceOfLedger=openiBalanceOfLedger+(ledgerform6.getLedgercredit_balance()-ledgerform6.getLedgerdebit_balance());
																			table.addCell((commonService
																					.round(openiBalanceOfLedger, 2)
																					.toString()));
																			table.addCell("0");
																			table.addCell("0");
																			table.addCell((commonService
																					.round((openiBalanceOfLedger), 2)
																					.toString()));

																			if (ledgerform6.getSubledgerList()
																					.size() > 0) {
																				for (OpeningBalancesForm subledger1 : ledgerform6
																						.getSubledgerList()) {
																					if (subledger1 != null) {
																						double openiBalanceOfSubLedger = (double) 0;
																						// ${openiBalanceOfSubLedger +
																						// (subledger1.debit_balance-subledger1.credit_balance)}
																						openiBalanceOfSubLedger = openiBalanceOfSubLedger
																								+ (subledger1
																										.getCredit_balance()-subledger1
																										.getDebit_balance()
																								 );
																						if (openiBalanceOfSubLedger != 0) {
																							table.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																						
																							table.addCell((commonService
																									.round(openiBalanceOfSubLedger,
																											2)
																									.toString()));
																							table.addCell("0");
																							table.addCell("0");
																							table.addCell((commonService
																									.round((openiBalanceOfSubLedger),
																											2)
																									.toString()));

																						}
																					}
																				}
																			}
																			isNotPresent = 1;
																		}
																	}
																}

															}
														}

													}

													if (obalance.getLedgerformlist().size() == 0)
													{
														double openiBalanceOfLedger = (double) 0;
														table.addCell(new Phrase("       "+ledgerform6.getLedgerName()));
														openiBalanceOfLedger = openiBalanceOfLedger
																+ (ledgerform6.getLedgercredit_balance()-ledgerform6.getLedgerdebit_balance()
																   );
														table.addCell((commonService.round(openiBalanceOfLedger, 2)
																.toString()));
														table.addCell("0");
														table.addCell("0");
														table.addCell((commonService.round((openiBalanceOfLedger), 2)
																.toString()));

														if (ledgerform6.getSubledgerList().size() > 0) {
															for (OpeningBalancesForm subledger1 : ledgerform6
																	.getSubledgerList()) {
																if (subledger1 != null) {
																	double openiBalanceOfSubLedger = 0;
																	openiBalanceOfSubLedger = openiBalanceOfSubLedger
																			+ (subledger1.getCredit_balance()-subledger1.getDebit_balance()
																			  );
																	if (openiBalanceOfSubLedger != 0) {
																		table.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																		table.addCell((commonService
																				.round(openiBalanceOfSubLedger, 2)
																				.toString()));
																		table.addCell("0");
																		table.addCell("0");
																		table.addCell((commonService
																				.round((openiBalanceOfSubLedger), 2)
																				.toString()));
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
						} // opening start date end

						if (accSubGroup.equalsIgnoreCase("Trade Payables")) 
						{
							for (OpeningBalancesForm supOpbalance : supplierOpeningBalanceList) {
								double supOpeningbalance = (double) 0;
								double supDebitbalance = (double) 0;
								double supCreditbalance = (double) 0;
								for (OpeningBalancesForm supOpbalance1 : supplierOpeningBalanceBeforeStartDate) {
									// bankOpbalance1.bankName == bankOpbalance.bankName
									if (supOpbalance1.getSupplierName().equals(supOpbalance.getSupplierName())) {
										supOpeningbalance = supOpeningbalance + (supOpbalance1.getCredit_balance()-supOpbalance1.getDebit_balance());
									}
								}

								supDebitbalance = supOpbalance.getDebit_balance();
								supCreditbalance = supOpbalance.getCredit_balance();
								
								if (supOpeningbalance != 0 || supDebitbalance != 0 || supCreditbalance != 0) {
									table.addCell(new Phrase("         "+supOpbalance.getSupplierName()));
								
									table.addCell((commonService.round(supOpeningbalance, 2).toString()));
									table.addCell(
											(commonService.round(supDebitbalance, 2).toString()));
									row_count_debit = row_count_debit + supDebitbalance;
									table.addCell((commonService.round(supCreditbalance, 2).toString()));
									row_count_credit = row_count_credit + supCreditbalance;
									table.addCell((commonService
											.round((supOpeningbalance) + (supCreditbalance-supDebitbalance), 2)
											.toString()));

								}
							}

							for (OpeningBalancesForm supOpbalance1 : supplierOpeningBalanceBeforeStartDate) {
								double issupplierPresent = 0;
								double openiBalanceOfsupplier = 0;

								for (OpeningBalancesForm supOpbalance : supplierOpeningBalanceList) {
									// bankOpbalance1.bankName == bankOpbalance.bankName
									if (supOpbalance1.getSupplierName().equals(supOpbalance.getSupplierName())) {
										issupplierPresent = 1;
									}
								}
								if (issupplierPresent == 0) {
									openiBalanceOfsupplier = openiBalanceOfsupplier
											+ (supOpbalance1.getCredit_balance()-supOpbalance1.getDebit_balance());
									if (openiBalanceOfsupplier != 0) {
										table.addCell(new Phrase("         "+supOpbalance1.getSupplierName()));
									
										table.addCell((commonService.round(openiBalanceOfsupplier, 2).toString()));
										table.addCell("0");
										table.addCell("0");
										table.addCell((commonService.round(openiBalanceOfsupplier, 2).toString()));
									}
								}

							}

						} // closed for subgroup3 (Cash & Bank Balances)

						
					}

				}

			}
		}
		
		for (OpeningBalancesOfSubedgerForm obalance : subOpeningList)
		{
			if (obalance.getGroup().getPostingSide().getPosting_id().equals((long) 2)) {
				double openingBalanceforGroup = (double) 0;
				double total_debitGroup = (double) 0;
				double total_creditGroup = (double) 0;

				for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
					if (obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName())) {
						
							openingBalanceforGroup = openingBalanceforGroup + (obalanceBeStartDate.getTotalcredit_balance())
									- (obalanceBeStartDate.getTotaldebit_balance());
							total_debitGroup = (obalance.getTotaldebit_balance());
							total_creditGroup = (obalance.getTotalcredit_balance());
						
					}
				}

				if (openingBalanceforGroup != 0 || total_debitGroup != 0 || total_creditGroup != 0) {
					table.addCell(obalance.getAccountGroupName());
					table.addCell((commonService.round(openingBalanceforGroup, 2).toString()));
					table.addCell((commonService.round(total_debitGroup, 2).toString()));
					table.addCell((commonService.round(total_creditGroup, 2).toString()));
					table.addCell((commonService
							.round((openingBalanceforGroup) + (total_creditGroup-total_debitGroup), 2).toString()));
				}

				// start of SubGroup
				for (String accSubGroup : obalance.getAccountSubGroupNameList()) 
				{
					double total_credit_Subgroup_beforeStartDate = (double) 0;
					double total_debit_Subgroup_beforeStartDate = (double) 0;
					double total_credit_Subgroup = (double) 0;
					double total_debit_Subgroup = (double) 0;

					for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
						for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
							if (subGroupName.equalsIgnoreCase(accSubGroup)) {
								for (LedgerForm ledgerform1 : obalanceBeStartDate.getLedgerformlist()) {
									if (ledgerform1.getSubgroupName().equals(accSubGroup)) {
										total_credit_Subgroup_beforeStartDate = total_credit_Subgroup_beforeStartDate
												+ ledgerform1.getLedgercredit_balance();
										total_debit_Subgroup_beforeStartDate = total_debit_Subgroup_beforeStartDate
												+ ledgerform1.getLedgerdebit_balance();
									}
								}
							}
						}
					}

					if (obalance.getLedgerformlist().size() > 0) {
						for (LedgerForm ledgerform2 : obalance.getLedgerformlist()) {
							if (ledgerform2.getSubgroupName().equals(accSubGroup)) {
								total_credit_Subgroup = total_credit_Subgroup + ledgerform2.getLedgercredit_balance();
								total_debit_Subgroup = total_debit_Subgroup + ledgerform2.getLedgerdebit_balance();
							}
						}
					}

					double openingBalanceSubgroup = (double) 0;
					double total_debitSubgroup = (double) 0;
					double total_creditSubgroup = (double) 0;
					
						openingBalanceSubgroup = total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate;
						total_debitSubgroup = total_debitSubgroup;
						total_credit_Subgroup = total_credit_Subgroup;
					

					if (openingBalanceSubgroup != 0 || total_debitSubgroup != 0 || total_creditSubgroup != 0) {
						table.addCell(new Phrase("    "+accSubGroup));
						table.addCell((commonService.round(openingBalanceSubgroup, 2).toString()));
						table.addCell((commonService.round(total_debitSubgroup, 2).toString()));
						table.addCell((commonService.round(total_creditSubgroup, 2).toString()));
						table.addCell((commonService
								.round((openingBalanceSubgroup) + (total_creditSubgroup-total_debitSubgroup), 2)
								.toString()));

						// start of ledger
						for (LedgerForm ledgerform3 : obalance.getLedgerformlist()) {
							if (ledgerform3 != null) {
								if (ledgerform3.getSubgroupName().equals(accSubGroup)) {
									double openiBalanceOfLedger = (double) 0;
									double total_debitOfLedger = (double) 0;
									double total_creditOfLedger = (double) 0;

									for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
										for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
											if (subGroupName.equalsIgnoreCase(accSubGroup)) {
												for (LedgerForm ledgerform4 : obalanceBeStartDate.getLedgerformlist()) {
													if (ledgerform4.getLedgerName()
															.equalsIgnoreCase(ledgerform3.getLedgerName())) {
														if (ledgerform4.getSubgroupName().equals(accSubGroup)) {
															openiBalanceOfLedger = openiBalanceOfLedger
																	+ (ledgerform4.getLedgercredit_balance()-ledgerform4.getLedgerdebit_balance()
																			);
														}
													}
												}
											}
										}
									}

									total_debitOfLedger = ledgerform3.getLedgerdebit_balance();
									total_creditOfLedger = ledgerform3.getLedgercredit_balance();

									if (openiBalanceOfLedger != 0 || total_debitOfLedger != 0
											|| total_creditOfLedger != 0) {
										table.addCell(new Phrase("       "+ledgerform3.getLedgerName())); 
										table.addCell((commonService.round(openiBalanceOfLedger, 2).toString()));
										table.addCell((commonService.round(total_debitOfLedger, 2).toString()));
										table.addCell((commonService.round(total_creditOfLedger, 2).toString()));
										table.addCell(
												(commonService
														.round((openiBalanceOfLedger)
																+ (total_creditOfLedger-total_debitOfLedger), 2)
														.toString()));
									}

								}
								// start of subledger

								if (ledgerform3.getSubgroupName().equals(accSubGroup)) {
									for (OpeningBalancesForm subledger : ledgerform3.getSubledgerList()) {
										if (subledger != null) {
											double openiBalanceOfSubLedger = (double) 0;
											double total_debitOfSubLedger = (double) 0;
											double total_creditOfSubLedger = (double) 0;
											for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
												for (String subGroupName : obalanceBeStartDate
														.getAccountSubGroupNameList()) {
													if (subGroupName.equalsIgnoreCase(accSubGroup)) {
														for (LedgerForm ledgerform5 : obalanceBeStartDate
																.getLedgerformlist()) {
															if (ledgerform5.getSubgroupName().equals(accSubGroup)) {
																for (OpeningBalancesForm subledger1 : ledgerform5
																		.getSubledgerList()) {
																	if (subledger1 != null) {
//						    			    									 /	test="${subledger1.subledgerName==subledger.subledgerName}">
																		if (subledger1.getSubledgerName()
																				.equalsIgnoreCase(
																						subledger.getSubledgerName())) {
																			openiBalanceOfSubLedger = openiBalanceOfSubLedger
																					+ (subledger1.getCredit_balance()-subledger1.getDebit_balance());
																		}
																	}
																}
															}
														}

													}
												}
											}

											total_debitOfSubLedger = subledger.getDebit_balance();
											total_creditOfSubLedger = subledger.getCredit_balance();
											
											if (openiBalanceOfSubLedger != 0 || total_debitOfSubLedger != 0
													|| total_creditOfSubLedger != 0) {
												table.addCell(new Phrase("         "+subledger.getSubledgerName()));
												table.addCell(
														(commonService.round(openiBalanceOfSubLedger, 2).toString()));
												table.addCell(
														(commonService.round(total_debitOfSubLedger, 2).toString()));
												row_count_debit=row_count_debit+total_debitOfSubLedger;
												table.addCell(
														(commonService.round(total_creditOfSubLedger, 2).toString()));
												row_count_credit=row_count_credit+total_creditOfSubLedger;
												table.addCell((commonService
														.round((openiBalanceOfSubLedger)
																+ (total_creditOfSubLedger-total_debitOfSubLedger), 2)
														.toString()));
											}

										}
									}
								}
							}
						}

						// opening balance start Date
						for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) 
						{
							if (obalanceBeStartDate.getAccountSubGroupNameList().size() > 0) {
								for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
									if (subGroupName.equals(accSubGroup)) {
										if (obalanceBeStartDate.getLedgerformlist().size() > 0) {
											for (LedgerForm ledgerform6 : obalanceBeStartDate.getLedgerformlist()) {
												double isNotPresent = (double) 0;
												double isPresent = (double) 0;
												if (ledgerform6.getSubgroupName().equalsIgnoreCase(accSubGroup)) {
													if (obalance.getLedgerformlist().size() > 0) {
														for (LedgerForm ledgerform7 : obalance.getLedgerformlist()) {
															if (ledgerform7.getLedgerName()
																	.equals(ledgerform6.getLedgerName())) {
																isPresent = 1;
															}

														}
														if (isPresent == 0) 
														{
															// obalance.ledgerformlist.size()>0
															if (obalance.getLedgerformlist().size() > 0) {
																for (LedgerForm ledgerform7 : obalance
																		.getLedgerformlist()) {
//					    			    			 						/ledgerform7.ledgerName!=ledgerform6.ledgerName
																	if (ledgerform7.getLedgerName() != ledgerform6
																			.getLedgerName()) {
																		if (isNotPresent == 0) {
																			double openiBalanceOfLedger = (double) 0;

																			table.addCell(new Phrase("       "+ledgerform6.getLedgerName()));  
																			openiBalanceOfLedger=openiBalanceOfLedger+(ledgerform6.getLedgercredit_balance()-ledgerform6.getLedgerdebit_balance());
																			table.addCell((commonService
																					.round(openiBalanceOfLedger, 2)
																					.toString()));
																			table.addCell("0");
																			table.addCell("0");
																			table.addCell((commonService
																					.round((openiBalanceOfLedger), 2)
																					.toString()));

																			if (ledgerform6.getSubledgerList()
																					.size() > 0) {
																				for (OpeningBalancesForm subledger1 : ledgerform6
																						.getSubledgerList()) {
																					if (subledger1 != null) {
																						double openiBalanceOfSubLedger = (double) 0;
																						// ${openiBalanceOfSubLedger +
																						// (subledger1.debit_balance-subledger1.credit_balance)}
																						openiBalanceOfSubLedger = openiBalanceOfSubLedger
																								+ (subledger1
																										.getCredit_balance()-subledger1
																										.getDebit_balance()
																								 );
																						if (openiBalanceOfSubLedger != 0) {
																							table.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																							table.addCell((commonService
																									.round(openiBalanceOfSubLedger,
																											2)
																									.toString()));
																							table.addCell("0");
																							table.addCell("0");
																							table.addCell((commonService
																									.round((openiBalanceOfSubLedger),
																											2)
																									.toString()));

																						}
																					}
																				}
																			}
																			isNotPresent = 1;
																		}
																	}
																}

															}
														}

													}

													if (obalance.getLedgerformlist().size() == 0)
													{
														double openiBalanceOfLedger = (double) 0;
														table.addCell(new Phrase("       "+ledgerform6.getLedgerName()));  
														
														openiBalanceOfLedger = openiBalanceOfLedger
																+ (ledgerform6.getLedgercredit_balance()-ledgerform6.getLedgerdebit_balance()
																   );
														table.addCell((commonService.round(openiBalanceOfLedger, 2)
																.toString()));
														table.addCell("0");
														table.addCell("0");
														table.addCell((commonService.round((openiBalanceOfLedger), 2)
																.toString()));

														if (ledgerform6.getSubledgerList().size() > 0) {
															for (OpeningBalancesForm subledger1 : ledgerform6
																	.getSubledgerList()) {
																if (subledger1 != null) {
																	double openiBalanceOfSubLedger = 0;
																	openiBalanceOfSubLedger = openiBalanceOfSubLedger
																			+ (subledger1.getCredit_balance()-subledger1.getDebit_balance()
																			  );
																	if (openiBalanceOfSubLedger != 0) {
																		table.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																		table.addCell((commonService
																				.round(openiBalanceOfSubLedger, 2)
																				.toString()));
																		table.addCell("0");
																		table.addCell("0");
																		table.addCell((commonService
																				.round((openiBalanceOfSubLedger), 2)
																				.toString()));
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
						} // opening start date end
						
					}

				}

			}
		}
		
		for (OpeningBalancesOfSubedgerForm obalance : subOpeningList)
		{
			if (obalance.getGroup().getPostingSide().getPosting_id().equals((long) 1)) 
			{
				double openingBalanceforGroup = (double) 0;
				double total_debitGroup = (double) 0;
				double total_creditGroup = (double) 0;

				for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
					if (obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName())) {
						
							openingBalanceforGroup = openingBalanceforGroup
									+ (obalanceBeStartDate.getTotaldebit_balance())
									- (obalanceBeStartDate.getTotalcredit_balance());
							total_debitGroup = (obalance.getTotaldebit_balance());
							total_creditGroup = (obalance.getTotalcredit_balance());
						
					}
				}

				if (openingBalanceforGroup != 0 || total_debitGroup != 0 || total_creditGroup != 0) {
					table.addCell(obalance.getAccountGroupName());
					table.addCell((commonService.round(openingBalanceforGroup, 2).toString()));
					table.addCell((commonService.round(total_debitGroup, 2).toString()));
					table.addCell((commonService.round(total_creditGroup, 2).toString()));
					table.addCell((commonService
							.round((openingBalanceforGroup) + (total_debitGroup - total_creditGroup), 2).toString()));
				}

				// start of SubGroup
				for (String accSubGroup : obalance.getAccountSubGroupNameList()) 
				{
					double total_credit_Subgroup_beforeStartDate = (double) 0;
					double total_debit_Subgroup_beforeStartDate = (double) 0;
					double total_credit_Subgroup = (double) 0;
					double total_debit_Subgroup = (double) 0;

					for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
						for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
							if (subGroupName.equalsIgnoreCase(accSubGroup)) {
								for (LedgerForm ledgerform1 : obalanceBeStartDate.getLedgerformlist()) {
									if (ledgerform1.getSubgroupName().equals(accSubGroup)) {
										total_credit_Subgroup_beforeStartDate = total_credit_Subgroup_beforeStartDate
												+ ledgerform1.getLedgercredit_balance();
										total_debit_Subgroup_beforeStartDate = total_debit_Subgroup_beforeStartDate
												+ ledgerform1.getLedgerdebit_balance();
									}
								}
							}
						}
					}

					if (obalance.getLedgerformlist().size() > 0) {
						for (LedgerForm ledgerform2 : obalance.getLedgerformlist()) {
							if (ledgerform2.getSubgroupName().equals(accSubGroup)) {
								total_credit_Subgroup = total_credit_Subgroup + ledgerform2.getLedgercredit_balance();
								total_debit_Subgroup = total_debit_Subgroup + ledgerform2.getLedgerdebit_balance();
							}
						}
					}

					double openingBalanceSubgroup = (double) 0;
					double total_debitSubgroup = (double) 0;
					double total_creditSubgroup = (double) 0;

					
						openingBalanceSubgroup = total_debit_Subgroup_beforeStartDate
								- total_credit_Subgroup_beforeStartDate;
						total_debitSubgroup = total_debitSubgroup;
						total_credit_Subgroup = total_credit_Subgroup;
				

					if (openingBalanceSubgroup != 0 || total_debitSubgroup != 0 || total_creditSubgroup != 0) {
						table.addCell(new Phrase("    "+accSubGroup));
						table.addCell((commonService.round(openingBalanceSubgroup, 2).toString()));
						table.addCell((commonService.round(total_debitSubgroup, 2).toString()));
						table.addCell((commonService.round(total_creditSubgroup, 2).toString()));
						table.addCell((commonService
								.round((openingBalanceSubgroup) + (total_debitSubgroup - total_creditSubgroup), 2)
								.toString()));

						// start of ledger
						for (LedgerForm ledgerform3 : obalance.getLedgerformlist()) {
							if (ledgerform3 != null) {
								if (ledgerform3.getSubgroupName().equals(accSubGroup)) {
									double openiBalanceOfLedger = (double) 0;
									double total_debitOfLedger = (double) 0;
									double total_creditOfLedger = (double) 0;

									for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
										for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
											if (subGroupName.equalsIgnoreCase(accSubGroup)) {
												for (LedgerForm ledgerform4 : obalanceBeStartDate.getLedgerformlist()) {
													if (ledgerform4.getLedgerName()
															.equalsIgnoreCase(ledgerform3.getLedgerName())) {
														if (ledgerform4.getSubgroupName().equals(accSubGroup)) {
															openiBalanceOfLedger = openiBalanceOfLedger
																	+ (ledgerform4.getLedgerdebit_balance()
																			- ledgerform4.getLedgercredit_balance());
														}
													}
												}
											}
										}
									}

									total_debitOfLedger = ledgerform3.getLedgerdebit_balance();
									total_creditOfLedger = ledgerform3.getLedgercredit_balance();

									if (openiBalanceOfLedger != 0 || total_debitOfLedger != 0
											|| total_creditOfLedger != 0) {
										table.addCell(new Phrase("       "+ledgerform3.getLedgerName()));  
										table.addCell((commonService.round(openiBalanceOfLedger, 2).toString()));
										table.addCell((commonService.round(total_debitOfLedger, 2).toString()));
										table.addCell((commonService.round(total_creditOfLedger, 2).toString()));
										table.addCell(
												(commonService
														.round((openiBalanceOfLedger)
																+ (total_debitOfLedger - total_creditOfLedger), 2)
														.toString()));
									}

								}
								// start of subledger

								if (ledgerform3.getSubgroupName().equals(accSubGroup)) {
									for (OpeningBalancesForm subledger : ledgerform3.getSubledgerList()) {
										if (subledger != null) {
											double openiBalanceOfSubLedger = (double) 0;
											double total_debitOfSubLedger = (double) 0;
											double total_creditOfSubLedger = (double) 0;
											for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) {
												for (String subGroupName : obalanceBeStartDate
														.getAccountSubGroupNameList()) {
													if (subGroupName.equalsIgnoreCase(accSubGroup)) {
														for (LedgerForm ledgerform5 : obalanceBeStartDate
																.getLedgerformlist()) {
															if (ledgerform5.getSubgroupName().equals(accSubGroup)) {
																for (OpeningBalancesForm subledger1 : ledgerform5
																		.getSubledgerList()) {
																	if (subledger1 != null) {
//						    			    									 /	test="${subledger1.subledgerName==subledger.subledgerName}">
																		if (subledger1.getSubledgerName()
																				.equalsIgnoreCase(
																						subledger.getSubledgerName())) {
																			openiBalanceOfSubLedger = openiBalanceOfSubLedger
																					+ (subledger1.getDebit_balance()-subledger1.getCredit_balance());
																		}
																	}
																}
															}
														}

													}
												}
											}

											total_debitOfSubLedger = subledger.getDebit_balance();
											total_creditOfSubLedger = subledger.getCredit_balance();
											
											if (openiBalanceOfSubLedger != 0 || total_debitOfSubLedger != 0
													|| total_creditOfSubLedger != 0) {
												table.addCell(new Phrase("         "+subledger.getSubledgerName()));
												table.addCell(
														(commonService.round(openiBalanceOfSubLedger, 2).toString()));
												table.addCell(
														(commonService.round(total_debitOfSubLedger, 2).toString()));
												row_count_debit=row_count_debit+total_debitOfSubLedger;
												table.addCell(
														(commonService.round(total_creditOfSubLedger, 2).toString()));
												row_count_credit=row_count_credit+total_creditOfSubLedger;
												table.addCell((commonService
														.round((openiBalanceOfSubLedger)
																+ (total_debitOfSubLedger - total_creditOfSubLedger), 2)
														.toString()));
											}

										}
									}
								}
							}
						}

						// opening balance start Date
						for (OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate) 
						{
							if (obalanceBeStartDate.getAccountSubGroupNameList().size() > 0) {
								for (String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList()) {
									if (subGroupName.equals(accSubGroup)) {
										if (obalanceBeStartDate.getLedgerformlist().size() > 0) {
											for (LedgerForm ledgerform6 : obalanceBeStartDate.getLedgerformlist()) {
												double isNotPresent = (double) 0;
												double isPresent = (double) 0;
												if (ledgerform6.getSubgroupName().equalsIgnoreCase(accSubGroup)) {
													if (obalance.getLedgerformlist().size() > 0) {
														for (LedgerForm ledgerform7 : obalance.getLedgerformlist()) {
															if (ledgerform7.getLedgerName()
																	.equals(ledgerform6.getLedgerName())) {
																isPresent = 1;
															}

														}
														if (isPresent == 0) {
															// obalance.ledgerformlist.size()>0
															if (obalance.getLedgerformlist().size() > 0) {
																for (LedgerForm ledgerform7 : obalance
																		.getLedgerformlist()) {
//					    			    			 						/ledgerform7.ledgerName!=ledgerform6.ledgerName
																	if (ledgerform7.getLedgerName() != ledgerform6
																			.getLedgerName()) {
																		if (isNotPresent == 0) {
																			double openiBalanceOfLedger = (double) 0;

																			table.addCell(new Phrase("       "+ledgerform6.getLedgerName()));  
																			openiBalanceOfLedger=openiBalanceOfLedger+(ledgerform6.getLedgerdebit_balance()-ledgerform6.getLedgercredit_balance());
																			table.addCell((commonService
																					.round(openiBalanceOfLedger, 2)
																					.toString()));
																			table.addCell("0");
																			table.addCell("0");
																			table.addCell((commonService
																					.round((openiBalanceOfLedger), 2)
																					.toString()));

																			if (ledgerform6.getSubledgerList()
																					.size() > 0) {
																				for (OpeningBalancesForm subledger1 : ledgerform6
																						.getSubledgerList()) {
																					if (subledger1 != null) {
																						double openiBalanceOfSubLedger = (double) 0;
																						// ${openiBalanceOfSubLedger +
																						// (subledger1.debit_balance-subledger1.credit_balance)}
																						openiBalanceOfSubLedger = openiBalanceOfSubLedger
																								+ (subledger1
																										.getDebit_balance()
																								- subledger1
																										.getCredit_balance());
																						if (openiBalanceOfSubLedger != 0) {
																							table.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																							
																							table.addCell((commonService
																									.round(openiBalanceOfSubLedger,
																											2)
																									.toString()));
																							table.addCell("0");
																							table.addCell("0");
																							table.addCell((commonService
																									.round((openiBalanceOfSubLedger),
																											2)
																									.toString()));

																						}
																					}
																				}
																			}
																			isNotPresent = 1;
																		}
																	}
																}

															}
														}

													}

													if (obalance.getLedgerformlist().size() == 0)
													{
														double openiBalanceOfLedger = (double) 0;
														table.addCell(new Phrase("       "+ledgerform6.getLedgerName()));
													
														openiBalanceOfLedger = openiBalanceOfLedger
																+ (ledgerform6.getLedgerdebit_balance()
																- ledgerform6.getLedgercredit_balance());
														table.addCell((commonService.round(openiBalanceOfLedger, 2)
																.toString()));
														table.addCell("0");
														table.addCell("0");
														table.addCell((commonService.round((openiBalanceOfLedger), 2)
																.toString()));

														if (ledgerform6.getSubledgerList().size() > 0) {
															for (OpeningBalancesForm subledger1 : ledgerform6
																	.getSubledgerList()) {
																if (subledger1 != null) {
																	double openiBalanceOfSubLedger = 0;
																	openiBalanceOfSubLedger = openiBalanceOfSubLedger
																			+ (subledger1.getDebit_balance()
																			- subledger1.getCredit_balance());
																	if (openiBalanceOfSubLedger != 0) {
																		table.addCell(new Phrase("         "+subledger1.getSubledgerName()));
																		table.addCell((commonService
																				.round(openiBalanceOfSubLedger, 2)
																				.toString()));
																		table.addCell("0");
																		table.addCell("0");
																		table.addCell((commonService
																				.round((openiBalanceOfSubLedger), 2)
																				.toString()));
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
		table.addCell(new Phrase("Total"));
		table.addCell(new Phrase(" "));
		table.addCell((commonService.round(row_count_debit, 2).toString()));
		table.addCell((commonService.round(row_count_credit, 2).toString()));
		table.addCell(new Phrase(" "));
		document.add(table);
	}
}
