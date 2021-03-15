package com.fasset.excel;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.fasset.entities.DebitNote;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;
import com.fasset.form.GSTReport2Form;
import com.fasset.form.HSNReportForm;
import com.fasset.service.CommonServiceImpl;

public class GSTReport2ExcelBuilder extends AbstractXlsxView {

	
	private CommonServiceImpl commonService = new CommonServiceImpl();
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setHeader("Content-Disposition", "attachment; filename=\"gstReport2.xls\"");

		GSTReport2Form form = (GSTReport2Form) model.get("form");

		int rowCount = 1;

		// create excel xls sheet
		Sheet sheetb2b = workbook.createSheet("b2b");

		// create header row
		Row headerb2b = sheetb2b.createRow(0);
		headerb2b.createCell(0).setCellValue("GSTIN/UIN of Recipient");
		headerb2b.createCell(1).setCellValue("Invoice Number");
		headerb2b.createCell(2).setCellValue("Invoice Date");
		headerb2b.createCell(3).setCellValue("Invoice Value");
		headerb2b.createCell(4).setCellValue("Place of Supply");
		headerb2b.createCell(5).setCellValue("Reverse Charge");
		headerb2b.createCell(6).setCellValue("Invoice Type");
		headerb2b.createCell(7).setCellValue("E-Commerce GSTIN");
		headerb2b.createCell(8).setCellValue("Rate");
		headerb2b.createCell(9).setCellValue("Taxable Value");
		headerb2b.createCell(10).setCellValue("Cess Amount");

		Double Total_rateb2b = (double)0;
		Double Total_taxb2b = (double)0;
		Double Total_cessb2b = (double)0;

		// Create data cells
		for (PurchaseEntry purchaseEntry : form.getGetB2BList()) {
			Row courseRow = sheetb2b.createRow(rowCount++);

			if (purchaseEntry.getSupplier() != null) {
				if (purchaseEntry.getSupplier().getGst_no() != null) {
					courseRow.createCell(0).setCellValue(purchaseEntry.getSupplier().getGst_no());
				} else {
					courseRow.createCell(0).setCellValue("");
				}
			} else {
				courseRow.createCell(0).setCellValue("");
			}

			if (purchaseEntry.getVoucher_no() != null) {
				courseRow.createCell(1).setCellValue(purchaseEntry.getVoucher_no());
			} else {
				courseRow.createCell(1).setCellValue("");
			}

			if (purchaseEntry.getSupplier_bill_date() != null) {
				courseRow.createCell(2).setCellValue(purchaseEntry.getSupplier_bill_date().toString());
			} else {
				courseRow.createCell(2).setCellValue("");
			}
			if (purchaseEntry.getRound_off() != null) {
				courseRow.createCell(3).setCellValue(purchaseEntry.getRound_off().toString());
			} else {
				courseRow.createCell(3).setCellValue("");
			}

			if (purchaseEntry.getSupplier() != null) {
				if (purchaseEntry.getSupplier().getState() != null) {
					courseRow.createCell(4).setCellValue(purchaseEntry.getSupplier().getState().getState_name());
				} else {
					courseRow.createCell(4).setCellValue("");
				}
			} else {
				courseRow.createCell(4).setCellValue("");
			}

			if (purchaseEntry.getSupplier() != null) {
				if (purchaseEntry.getSupplier().getReverse_mecha() != null) {
					courseRow.createCell(5).setCellValue(purchaseEntry.getSupplier().getReverse_mecha());
				} else {
					courseRow.createCell(5).setCellValue("");
				}
			} else {
				courseRow.createCell(5).setCellValue("");
			}

			courseRow.createCell(6).setCellValue("");
			courseRow.createCell(7).setCellValue("");

			Double total = (double)0;
			if (purchaseEntry.getCgst() != null && purchaseEntry.getSgst() != null && purchaseEntry.getIgst() != null) {
				total = purchaseEntry.getCgst() + purchaseEntry.getSgst() + purchaseEntry.getIgst();
				Total_rateb2b = Total_rateb2b + total;
			}

			courseRow.createCell(8).setCellValue(round(total, 2).toString());

			if (purchaseEntry.getTransaction_value() != null) {
				courseRow.createCell(9).setCellValue(purchaseEntry.getTransaction_value().toString());
				Total_taxb2b = Total_taxb2b + purchaseEntry.getTransaction_value();
			} else {
				courseRow.createCell(9).setCellValue("");
			}
			if (purchaseEntry.getState_compansation_tax() != null) {
				courseRow.createCell(10).setCellValue(purchaseEntry.getState_compansation_tax().toString());
				Total_cessb2b = Total_cessb2b + purchaseEntry.getState_compansation_tax();
			} else {
				courseRow.createCell(10).setCellValue("");
			}

		}

		Row courseRow1 = sheetb2b.createRow(rowCount++);

		courseRow1.createCell(0).setCellValue("");
		courseRow1.createCell(1).setCellValue("");
		courseRow1.createCell(2).setCellValue("");
		courseRow1.createCell(3).setCellValue("");
		courseRow1.createCell(4).setCellValue("");
		courseRow1.createCell(5).setCellValue("");
		courseRow1.createCell(6).setCellValue("");
		courseRow1.createCell(7).setCellValue("");
		courseRow1.createCell(8).setCellValue(commonService.round((double)Total_rateb2b,2).toString());
		courseRow1.createCell(9).setCellValue(commonService.round((double)Total_taxb2b,2).toString());
		courseRow1.createCell(10).setCellValue(commonService.round((double)Total_cessb2b,2).toString());
	

		rowCount = 1;
		// create excel xls sheet
		Sheet sheetb2cl = workbook.createSheet("b2cl");

		// create header row
		Row headerb2cl = sheetb2cl.createRow(0);
		headerb2cl.createCell(0).setCellValue("Invoice Number");
		headerb2cl.createCell(1).setCellValue("Invoice Date");
		headerb2cl.createCell(2).setCellValue("Invoice Value");
		headerb2cl.createCell(3).setCellValue("Place of Supply");
		headerb2cl.createCell(4).setCellValue("Rate");
		headerb2cl.createCell(5).setCellValue("Taxable Value");
		headerb2cl.createCell(6).setCellValue("Cess Amount");
		headerb2cl.createCell(7).setCellValue("E-Commerce GSTIN");

		Double Total_rateb2cl = (double)0;
		Double Total_taxb2cl = (double)0;
		Double Total_cessb2cl = (double)0;

		// Create data cells
		for (PurchaseEntry purchaseEntry : form.getGetB2CLList()) {
			Row courseRow = sheetb2cl.createRow(rowCount++);

			if (purchaseEntry.getVoucher_no() != null) {
				courseRow.createCell(0).setCellValue(purchaseEntry.getVoucher_no());
			} else {
				courseRow.createCell(0).setCellValue("");
			}
			if (purchaseEntry.getSupplier_bill_date() != null) {
				courseRow.createCell(1).setCellValue(purchaseEntry.getSupplier_bill_date().toString());
			} else {
				courseRow.createCell(1).setCellValue("");
			}

			if (purchaseEntry.getRound_off() != null) {
				courseRow.createCell(2).setCellValue(purchaseEntry.getRound_off().toString());
			} else {
				courseRow.createCell(2).setCellValue("");
			}

			if (purchaseEntry.getSupplier() != null) {
				if (purchaseEntry.getSupplier().getState() != null) {
					courseRow.createCell(3).setCellValue(purchaseEntry.getSupplier().getState().getState_name());
				} else {
					courseRow.createCell(3).setCellValue("");
				}
			} else {
				courseRow.createCell(3).setCellValue("");
			}

			Double total = (double)0;
			if (purchaseEntry.getCgst() != null && purchaseEntry.getSgst() != null && purchaseEntry.getIgst() != null) {
				total = purchaseEntry.getCgst() + purchaseEntry.getSgst() + purchaseEntry.getIgst();
				Total_rateb2cl = Total_rateb2cl + total;
			}

			courseRow.createCell(4).setCellValue(round(total, 2).toString());

			if (purchaseEntry.getTransaction_value() != null) {
				courseRow.createCell(5).setCellValue(purchaseEntry.getTransaction_value().toString());
				Total_taxb2cl = Total_taxb2cl + purchaseEntry.getTransaction_value();
			} else {
				courseRow.createCell(5).setCellValue("");
			}

			if (purchaseEntry.getState_compansation_tax() != null) {
				courseRow.createCell(6).setCellValue(purchaseEntry.getState_compansation_tax().toString());
				Total_cessb2cl = Total_cessb2cl + purchaseEntry.getState_compansation_tax();
			} else {
				courseRow.createCell(6).setCellValue("");
			}

			courseRow.createCell(7).setCellValue("");
		}

		Row courseRow2 = sheetb2cl.createRow(rowCount++);

		courseRow2.createCell(0).setCellValue("");
		courseRow2.createCell(1).setCellValue("");
		courseRow2.createCell(2).setCellValue("");
		courseRow2.createCell(3).setCellValue("");
		courseRow2.createCell(4).setCellValue(commonService.round((double)Total_rateb2cl,2).toString());
		courseRow2.createCell(5).setCellValue(commonService.round((double)Total_taxb2cl,2).toString());
		courseRow2.createCell(6).setCellValue(commonService.round((double)Total_cessb2cl,2).toString());
		courseRow2.createCell(7).setCellValue("");

		rowCount = 1;
		// create excel xls sheet
		Sheet sheetb2cs = workbook.createSheet("b2cs");

		// create header row
		Row headerb2cs = sheetb2cs.createRow(0);
		headerb2cs.createCell(0).setCellValue("Type");
		headerb2cs.createCell(1).setCellValue("Place Of Supply");
		headerb2cs.createCell(2).setCellValue("Rate");
		headerb2cs.createCell(3).setCellValue("Taxable Value");
		headerb2cs.createCell(4).setCellValue("Cess Amount");
		headerb2cs.createCell(5).setCellValue("E-Commerce GSTIN");

		Double Total_rateb2cs = (double)0;
		Double Total_taxb2cs = (double)0;
		Double Total_cessb2cs = (double)0;

		// Create data cells
		for (PurchaseEntry purchaseEntry : form.getGetB2CSList()) {
			Row courseRow = sheetb2cs.createRow(rowCount++);
			courseRow.createCell(0).setCellValue("");

			if (purchaseEntry.getSupplier() != null) {
				if (purchaseEntry.getSupplier().getState() != null) {
					courseRow.createCell(1).setCellValue(purchaseEntry.getSupplier().getState().getState_name());
				} else {
					courseRow.createCell(1).setCellValue("");
				}
			} else {
				courseRow.createCell(1).setCellValue("");
			}

			Double total = (double)0;
			if (purchaseEntry.getCgst() != null && purchaseEntry.getSgst() != null && purchaseEntry.getIgst() != null) {
				total = purchaseEntry.getCgst() + purchaseEntry.getSgst() + purchaseEntry.getIgst();
				Total_rateb2cs = Total_rateb2cs + total;
			}

			courseRow.createCell(2).setCellValue(round(total, 2).toString());
			if (purchaseEntry.getTransaction_value() != null) {
				courseRow.createCell(3).setCellValue(purchaseEntry.getTransaction_value().toString());
				Total_taxb2cs = Total_taxb2cs + purchaseEntry.getTransaction_value();
			} else {
				courseRow.createCell(3).setCellValue("");
			}

			if (purchaseEntry.getState_compansation_tax() != null) {
				courseRow.createCell(4).setCellValue(purchaseEntry.getState_compansation_tax().toString());
				Total_cessb2cs = Total_cessb2cs + purchaseEntry.getState_compansation_tax();
			} else {
				courseRow.createCell(4).setCellValue("");
			}

			courseRow.createCell(5).setCellValue("");
		}

		Row courseRow3 = sheetb2cs.createRow(rowCount++);

		courseRow3.createCell(0).setCellValue("");
		courseRow3.createCell(1).setCellValue("");
		courseRow3.createCell(2).setCellValue(commonService.round((double)Total_rateb2cs,2).toString());
	    courseRow3.createCell(3).setCellValue(commonService.round((double)Total_taxb2cs,2).toString());
	    courseRow3.createCell(4).setCellValue(commonService.round((double)Total_cessb2cs,2).toString());
		courseRow3.createCell(5).setCellValue("");

		rowCount = 1;

		Sheet sheetcdnr = workbook.createSheet("cdnr");

		Row headercdnr = sheetcdnr.createRow(0);
		headercdnr.createCell(0).setCellValue("GSTIN/UIN of Recipient");
		headercdnr.createCell(1).setCellValue("Invoice Receipt Number");
		headercdnr.createCell(2).setCellValue("Invoice Receipt date");
		headercdnr.createCell(3).setCellValue("Refund Voucher Number");
		headercdnr.createCell(4).setCellValue("Refund Voucher date");
		headercdnr.createCell(5).setCellValue("Document Type");
		headercdnr.createCell(6).setCellValue("Reason For Issuing document");
		headercdnr.createCell(7).setCellValue("Place Of Supply");
		headercdnr.createCell(8).setCellValue("Refund Voucher Value");
		headercdnr.createCell(9).setCellValue("Rate");
		headercdnr.createCell(10).setCellValue("Taxable Value");
		headercdnr.createCell(11).setCellValue("Cess Amount");
		headercdnr.createCell(12).setCellValue("Pre GST");

		Double Total_ratecdnr = (double)0;
		Double Total_taxcdnr = (double)0;
		Double Total_cesscdnr = (double)0;

		for (DebitNote debitNote : form.getCdnrList()) {
			
			if(debitNote.getSupplier()!=null)
			{
			Row courseRow = sheetcdnr.createRow(rowCount++);

			if (debitNote.getSupplier() != null) {
				if (debitNote.getSupplier().getGst_no() != null) {
					courseRow.createCell(0).setCellValue(debitNote.getSupplier().getGst_no());
				} else {
					courseRow.createCell(0).setCellValue("");
				}
			} else {
				courseRow.createCell(0).setCellValue("");
			}

			if (debitNote.getPurchase_bill_id() != null) {
				if (debitNote.getPurchase_bill_id().getSupplier_bill_no() != null) {
					courseRow.createCell(1).setCellValue(debitNote.getPurchase_bill_id().getVoucher_no());
				} else {
					courseRow.createCell(1).setCellValue("");
				}
			} else {
				courseRow.createCell(1).setCellValue("");
			}

			if (debitNote.getPurchase_bill_id() != null) {
				if (debitNote.getPurchase_bill_id().getSupplier_bill_date() != null) {
					courseRow.createCell(2)
							.setCellValue(debitNote.getPurchase_bill_id().getSupplier_bill_date().toString());
				} else {
					courseRow.createCell(2).setCellValue("");
				}
			} else {
				courseRow.createCell(2).setCellValue("");
			}

			if (debitNote.getVoucher_no() != null) {
				courseRow.createCell(3).setCellValue(debitNote.getVoucher_no());
			} else {
				courseRow.createCell(3).setCellValue("");
			}

			if (debitNote.getDate() != null) {
				courseRow.createCell(4).setCellValue(debitNote.getDate().toString());
			} else {
				courseRow.createCell(4).setCellValue("");
			}
			courseRow.createCell(5).setCellValue("");
			courseRow.createCell(6).setCellValue("");

			if (debitNote.getSupplier() != null) {
				if (debitNote.getSupplier().getState() != null) {
					courseRow.createCell(7).setCellValue(debitNote.getSupplier().getState().getState_name());
				} else {
					courseRow.createCell(7).setCellValue("");
				}
			} else {
				courseRow.createCell(7).setCellValue("");
			}

			if (debitNote.getRound_off() != null) {
				courseRow.createCell(8).setCellValue(debitNote.getRound_off().toString());
			} else {
				courseRow.createCell(8).setCellValue("");
			}

			if (debitNote.getCGST_head() != null && debitNote.getSGST_head() != null
					&& debitNote.getIGST_head() != null) {
				Double total = debitNote.getCGST_head() + debitNote.getSGST_head() + debitNote.getIGST_head();
				courseRow.createCell(9).setCellValue(round(total, 2).toString());
				Total_ratecdnr = Total_ratecdnr + total;
			} else {
				courseRow.createCell(9).setCellValue("");
			}

			if (debitNote.getTransaction_value_head() != null) {
				courseRow.createCell(10).setCellValue(debitNote.getTransaction_value_head().toString());
				Total_taxcdnr = Total_taxcdnr + debitNote.getTransaction_value_head();
			} else {
				courseRow.createCell(10).setCellValue("");
			}

			if (debitNote.getSCT_head() != null) {
				courseRow.createCell(11).setCellValue(debitNote.getSCT_head().toString());
				Total_cesscdnr = Total_cesscdnr + debitNote.getSCT_head();
			} else {
				courseRow.createCell(11).setCellValue("");
			}

			courseRow.createCell(12).setCellValue("");
			
		}

		}

		Row courseRow4 = sheetcdnr.createRow(rowCount++);

		courseRow4.createCell(0).setCellValue("");
		courseRow4.createCell(1).setCellValue("");
		courseRow4.createCell(2).setCellValue("");
		courseRow4.createCell(3).setCellValue("");
		courseRow4.createCell(4).setCellValue("");
		courseRow4.createCell(5).setCellValue("");
		courseRow4.createCell(6).setCellValue("");
		courseRow4.createCell(7).setCellValue("");
		courseRow4.createCell(8).setCellValue("");
		courseRow4.createCell(9).setCellValue(commonService.round((double)Total_ratecdnr,2).toString());
		courseRow4.createCell(10).setCellValue(commonService.round((double)Total_taxcdnr,2).toString());
		courseRow4.createCell(11).setCellValue(commonService.round((double)Total_cesscdnr,2).toString());
		courseRow4.createCell(12).setCellValue("");

		rowCount = 1;

		Sheet sheetcdnur = workbook.createSheet("cdnur");

		Row headercdnur = sheetcdnur.createRow(0);
		headercdnur.createCell(0).setCellValue("UR Type");
		headercdnur.createCell(1).setCellValue("Refund Voucher Number");
		headercdnur.createCell(2).setCellValue("Refund Voucher date");
		headercdnur.createCell(3).setCellValue("Document Type");
		headercdnur.createCell(4).setCellValue("Advance Payment date");
		headercdnur.createCell(5).setCellValue("Reason For Issuing document");
		headercdnur.createCell(6).setCellValue("Place Of Supply");
		headercdnur.createCell(7).setCellValue("Refund Voucher Value");
		headercdnur.createCell(8).setCellValue("Rate");
		headercdnur.createCell(9).setCellValue("Taxable Value");
		headercdnur.createCell(10).setCellValue("Cess Amount");
		headercdnur.createCell(11).setCellValue("Pre GST");

		Double Total_ratecdnur = (double)0;
		Double Total_taxcdnur = (double)0;
		Double Total_cesscdnur = (double)0;

		for (DebitNote debitNote : form.getCdnurList()) {
			
			if(debitNote.getSupplier()!=null)
			{
			Row courseRow = sheetcdnur.createRow(rowCount++);

			courseRow.createCell(0).setCellValue("");

			if (debitNote.getVoucher_no() != null) {
				courseRow.createCell(1).setCellValue(debitNote.getVoucher_no());
			} else {
				courseRow.createCell(1).setCellValue("");
			}

			if (debitNote.getDate() != null) {
				courseRow.createCell(2).setCellValue(debitNote.getDate().toString());
			} else {
				courseRow.createCell(2).setCellValue("");
			}

			courseRow.createCell(3).setCellValue("");
			courseRow.createCell(4).setCellValue("");
			courseRow.createCell(5).setCellValue("");

			if (debitNote.getSupplier() != null) {
				if (debitNote.getSupplier().getState() != null) {
					courseRow.createCell(6).setCellValue(debitNote.getSupplier().getState().getState_name());
				} else {
					courseRow.createCell(6).setCellValue("");
				}
			} else {
				courseRow.createCell(6).setCellValue("");
			}

			if (debitNote.getRound_off() != null) {
				courseRow.createCell(7).setCellValue(debitNote.getRound_off().toString());
			} else {
				courseRow.createCell(7).setCellValue("");
			}

			if (debitNote.getCGST_head() != null && debitNote.getSGST_head() != null
					&& debitNote.getIGST_head() != null) {
				Double total = debitNote.getCGST_head() + debitNote.getSGST_head() + debitNote.getIGST_head();
				courseRow.createCell(8).setCellValue(round(total, 2).toString());
				Total_ratecdnr = Total_ratecdnr + total;
			} else {
				courseRow.createCell(8).setCellValue("");
			}

			if (debitNote.getTransaction_value_head() != null) {
				courseRow.createCell(9).setCellValue(debitNote.getTransaction_value_head().toString());
				Total_taxcdnr = Total_taxcdnr + debitNote.getTransaction_value_head();
			} else {
				courseRow.createCell(9).setCellValue("");
			}

			if (debitNote.getSCT_head() != null) {
				courseRow.createCell(10).setCellValue(debitNote.getSCT_head().toString());
				Total_cesscdnr = Total_cesscdnr + debitNote.getSCT_head();
			} else {
				courseRow.createCell(10).setCellValue("");
			}

			courseRow.createCell(11).setCellValue("");
			
		}

		}

		Row courseRow5 = sheetcdnur.createRow(rowCount++);

		courseRow5.createCell(0).setCellValue("");
		courseRow5.createCell(1).setCellValue("");
		courseRow5.createCell(2).setCellValue("");
		courseRow5.createCell(3).setCellValue("");
		courseRow5.createCell(4).setCellValue("");
		courseRow5.createCell(5).setCellValue("");
		courseRow5.createCell(6).setCellValue("");
		courseRow5.createCell(7).setCellValue("");
		courseRow5.createCell(8).setCellValue(commonService.round((double)Total_ratecdnur,2).toString());
		courseRow5.createCell(9).setCellValue(commonService.round((double)Total_taxcdnur,2).toString());
		courseRow5.createCell(10).setCellValue(commonService.round((double)Total_cesscdnur,2).toString());
		courseRow5.createCell(11).setCellValue("");

		rowCount = 1;

		Sheet sheetexp = workbook.createSheet("exp");

		Row headerexp = sheetexp.createRow(0);
		headerexp.createCell(0).setCellValue("Export Type");
		headerexp.createCell(1).setCellValue("Invoice Number");
		headerexp.createCell(2).setCellValue("Invoice date");
		headerexp.createCell(3).setCellValue("Invoice Value");
		headerexp.createCell(4).setCellValue("Port Code");
		headerexp.createCell(5).setCellValue("Shipping Bill Number");
		headerexp.createCell(6).setCellValue("Shipping Bill Date");
		headerexp.createCell(7).setCellValue("Rate");
		headerexp.createCell(8).setCellValue("Taxable Value");

		Double Total_rateexp = (double)0;
		Double Total_taxexp = (double)0;

		for (PurchaseEntry purchaseEntry : form.getGetExpList()) {
			Row courseRow = sheetexp.createRow(rowCount++);

			courseRow.createCell(0).setCellValue("");

			if (purchaseEntry.getVoucher_no() != null) {
				courseRow.createCell(1).setCellValue(purchaseEntry.getVoucher_no());
			} else {
				courseRow.createCell(1).setCellValue("");
			}

			if (purchaseEntry.getSupplier_bill_date() != null) {
				courseRow.createCell(2).setCellValue(purchaseEntry.getSupplier_bill_date().toString());
			} else {
				courseRow.createCell(2).setCellValue("");
			}
			if (purchaseEntry.getRound_off() != null) {
				courseRow.createCell(3).setCellValue(purchaseEntry.getRound_off().toString());
			} else {
				courseRow.createCell(3).setCellValue("");
			}

			courseRow.createCell(4).setCellValue("");

			if (purchaseEntry.getShipping_bill_no() != null) {
				courseRow.createCell(5).setCellValue(purchaseEntry.getShipping_bill_no());
			} else {
				courseRow.createCell(5).setCellValue("");
			}

			if (purchaseEntry.getShipping_bill_date() != null) {
				courseRow.createCell(6).setCellValue(purchaseEntry.getShipping_bill_date().toString());
			} else {
				courseRow.createCell(6).setCellValue("");
			}

			if (purchaseEntry.getCgst() != null && purchaseEntry.getSgst() != null && purchaseEntry.getIgst() != null) {

				Double total = purchaseEntry.getCgst() + purchaseEntry.getSgst() + purchaseEntry.getIgst();

				courseRow.createCell(7).setCellValue(round(total, 2).toString());
				Total_rateexp = Total_rateexp + total;
			} else {
				courseRow.createCell(7).setCellValue("");
			}

			if (purchaseEntry.getTransaction_value() != null) {
				courseRow.createCell(8).setCellValue(purchaseEntry.getTransaction_value().toString());
				Total_taxexp = Total_taxexp + purchaseEntry.getTransaction_value();
			} else {
				courseRow.createCell(8).setCellValue("");
			}
		}

		Row courseRow6 = sheetexp.createRow(rowCount++);
		courseRow6.createCell(0).setCellValue("");
		courseRow6.createCell(1).setCellValue("");
		courseRow6.createCell(2).setCellValue("");
		courseRow6.createCell(3).setCellValue("");
		courseRow6.createCell(4).setCellValue("");
		courseRow6.createCell(5).setCellValue("");
		courseRow6.createCell(6).setCellValue("");
		courseRow6.createCell(7).setCellValue(commonService.round((double)Total_rateexp,2).toString());
		courseRow6.createCell(8).setCellValue(commonService.round((double)Total_taxexp,2).toString());
		

		rowCount = 1;

		Sheet sheetat = workbook.createSheet("at");

		Row headerat = sheetat.createRow(0);
		headerat.createCell(0).setCellValue("Place Of Supply");
		headerat.createCell(1).setCellValue("Rate");
		headerat.createCell(2).setCellValue("Gross Advance Received");
		headerat.createCell(3).setCellValue("Cess Amount");

		Double Total_rateat = (double)0;
		Double Total_cessat = (double)0;
		Double Total_grossat = (double)0;

		for (Payment payment : form.getGetATList()) {
			
			
			if (payment.getSupplier() != null) { //for restrict entries against subledger
				
				Row courseRow = sheetat.createRow(rowCount++);
		
			if (payment.getSupplier() != null) {
				if (payment.getSupplier().getState() != null) {
					courseRow.createCell(0).setCellValue(payment.getSupplier().getState().getState_name());
				} else {
					courseRow.createCell(0).setCellValue("");
				}
			} else {
				courseRow.createCell(0).setCellValue("");
			}

			Double total = (double)0;
			if (payment.getCGST_head() != null && payment.getSGST_head() != null && payment.getIGST_head() != null) {
				total = payment.getCGST_head() + payment.getSGST_head() + payment.getIGST_head();
				courseRow.createCell(1).setCellValue(round(total, 2).toString());
				Total_rateat = Total_rateat + total;
			} else {
				courseRow.createCell(1).setCellValue(round(total, 2).toString());
			}
			if (payment.getAmount() != null) {
				courseRow.createCell(2).setCellValue(payment.getAmount().toString());
				Total_grossat = Total_grossat + payment.getAmount();
			} else {
				courseRow.createCell(2).setCellValue("");
			}
			if (payment.getSCT_head() != null) {
				courseRow.createCell(3).setCellValue(payment.getSCT_head().toString());
				Total_cessat = Total_cessat + payment.getSCT_head();

			} else {
				courseRow.createCell(3).setCellValue("");
			}
			
		}

		}

		Row courseRow7 = sheetat.createRow(rowCount++);
		courseRow7.createCell(0).setCellValue("");
		courseRow7.createCell(1).setCellValue(commonService.round((double)Total_rateat,2).toString());
		courseRow7.createCell(2).setCellValue(commonService.round((double)Total_grossat,2).toString());
		courseRow7.createCell(3).setCellValue(commonService.round((double)Total_cessat,2).toString());
		

		rowCount = 1;
		Sheet sheetatadj = workbook.createSheet("atadj");

		Row headeratadj = sheetatadj.createRow(0);
		headeratadj.createCell(0).setCellValue("Place Of Supply");
		headeratadj.createCell(1).setCellValue("Rate");
		headeratadj.createCell(2).setCellValue("Gross Advance Received");
		headeratadj.createCell(3).setCellValue("Cess Amount");

		Double Total_rateatadj = (double)0;
		Double Total_cessatadj = (double)0;
		Double Total_grossatadj = (double)0;

		for (Payment payment : form.getGetATAdjList()) {
			
			if (payment.getSupplier() != null) //for restrict entries against subledger	
		   { 
			Row courseRow = sheetatadj.createRow(rowCount++);

			if (payment.getSupplier() != null) {
				if (payment.getSupplier().getState() != null) {
					courseRow.createCell(0).setCellValue(payment.getSupplier().getState().getState_name());
				} else {
					courseRow.createCell(0).setCellValue("");
				}
			} else {
				courseRow.createCell(0).setCellValue("");
			}

			Double total = (double)0;
			if (payment.getCGST_head() != null && payment.getSGST_head() != null && payment.getIGST_head() != null) {
				total = payment.getCGST_head() + payment.getSGST_head() + payment.getIGST_head();
				courseRow.createCell(1).setCellValue(round(total, 2).toString());
				Total_rateatadj = Total_rateatadj + total;
			} else {
				courseRow.createCell(1).setCellValue(round(total, 2).toString());
			}

			if (payment.getRound_off() != null) {
				courseRow.createCell(2).setCellValue(payment.getRound_off().toString());
				Total_grossatadj = Total_grossatadj + payment.getRound_off();
			} else {
				courseRow.createCell(2).setCellValue("");
			}
			if (payment.getSCT_head() != null) {
				courseRow.createCell(3).setCellValue(payment.getSCT_head().toString());
				Total_cessatadj = Total_cessatadj + payment.getSCT_head();
			} else {
				courseRow.createCell(3).setCellValue("");
			}
			
		}

		}

		Row courseRow8 = sheetatadj.createRow(rowCount++);
		courseRow8.createCell(0).setCellValue("");
		courseRow8.createCell(1).setCellValue(commonService.round((double)Total_rateatadj,2).toString());
		courseRow8.createCell(2).setCellValue(commonService.round((double)Total_grossatadj,2).toString());
		courseRow8.createCell(3).setCellValue(commonService.round((double)Total_cessatadj,2).toString());
		
		
		/******************************************* Sheet for HSN *********************************************/	
		rowCount = 1;

		Sheet sheethsn = workbook.createSheet("HSN");

		Row headerhsn = sheethsn.createRow(0);
		headerhsn.createCell(0).setCellValue("HSN");
		headerhsn.createCell(1).setCellValue("Description");
		headerhsn.createCell(2).setCellValue("UQC");
		headerhsn.createCell(3).setCellValue("Total Quantity");
		headerhsn.createCell(4).setCellValue("Total Value");
		headerhsn.createCell(5).setCellValue("Taxable Value");
		headerhsn.createCell(6).setCellValue("Integrated Tax Amount");
		headerhsn.createCell(7).setCellValue("Central Tax Amount");
		headerhsn.createCell(8).setCellValue("State/UT Tax Amount");
		headerhsn.createCell(9).setCellValue("Cess Amount");
		
		Double TotalQuantity = (double)0;
		Double TotalValue= (double)0;
		Double TaxableValue= (double)0;
		Double IgstAmount = (double)0;
		Double CgstAmount= (double)0;
		Double SgstAmount= (double)0;
		Double CessAmount= (double)0;
		
		
		for(HSNReportForm reportForm : form.getHsnReportList()) {
			Row courseRow = sheethsn.createRow(rowCount++);
			if(reportForm.getHsnCode()!=null)
			{
			courseRow.createCell(0).setCellValue(reportForm.getHsnCode());
			}
			else
			{
				courseRow.createCell(0).setCellValue("");
			}
			courseRow.createCell(1).setCellValue("");
			
			if(reportForm.getUqc()!=null)
			{
				courseRow.createCell(2).setCellValue(reportForm.getUqc());
			}
			else
			{
				courseRow.createCell(2).setCellValue("");
			}
			
			if(reportForm.getTotalQuantity()!=null)
			{
				courseRow.createCell(3).setCellValue(round(reportForm.getTotalQuantity(), 2).toString());
				TotalQuantity=TotalQuantity+reportForm.getTotalQuantity();
			}
			else
			{
				courseRow.createCell(3).setCellValue("");
			}
			
			if(reportForm.getTotalValue()!=null)
			{
				courseRow.createCell(4).setCellValue(round(reportForm.getTotalValue(), 2).toString());
				TotalValue=TotalValue+reportForm.getTotalValue();
			}
			else
			{
				courseRow.createCell(4).setCellValue("");
			}
			
			if(reportForm.getTaxableValue()!=null)
			{
				courseRow.createCell(5).setCellValue(round(reportForm.getTaxableValue(), 2).toString());
				TaxableValue=TaxableValue+reportForm.getTaxableValue();
			}
			else
			{
				courseRow.createCell(5).setCellValue("");
			}
			
			if(reportForm.getIgstAmount()!=null)
			{
				courseRow.createCell(6).setCellValue(round(reportForm.getIgstAmount(), 2).toString());
				IgstAmount=IgstAmount+reportForm.getIgstAmount();
			}
			else
			{
				courseRow.createCell(6).setCellValue("");
			}
			if(reportForm.getCgstAmount()!=null)
			{
				courseRow.createCell(7).setCellValue(round(reportForm.getCgstAmount(), 2).toString());
				CgstAmount=CgstAmount+reportForm.getCgstAmount();
			}
			else
			{
				courseRow.createCell(7).setCellValue("");
			}
			if(reportForm.getSgstAmount()!=null)
			{
				courseRow.createCell(8).setCellValue(round(reportForm.getSgstAmount(), 2).toString());
				SgstAmount=SgstAmount+reportForm.getSgstAmount();
			}
			else
			{
				courseRow.createCell(8).setCellValue("");
			}
			
			if(reportForm.getCessAmount()!=null)
			{
				courseRow.createCell(9).setCellValue(round(reportForm.getCessAmount(), 2).toString());
				CessAmount=CessAmount+reportForm.getCessAmount();
			}
			else
			{
				courseRow.createCell(9).setCellValue("");
			}
		}
		
		Row courseRow9= sheethsn.createRow(rowCount++);
		courseRow9.createCell(0).setCellValue("");
		courseRow9.createCell(1).setCellValue("");
		courseRow9.createCell(2).setCellValue("");
		courseRow9.createCell(3).setCellValue(commonService.round((double)TotalQuantity,2).toString());
		courseRow9.createCell(4).setCellValue(commonService.round((double)TotalValue,2).toString());
		courseRow9.createCell(5).setCellValue(commonService.round((double)TaxableValue,2).toString());
		courseRow9.createCell(6).setCellValue(commonService.round((double)IgstAmount,2).toString());
		courseRow9.createCell(7).setCellValue(commonService.round((double)CgstAmount,2).toString());
		courseRow9.createCell(8).setCellValue(commonService.round((double)SgstAmount,2).toString());
		courseRow9.createCell(9).setCellValue(commonService.round((double)CessAmount,2).toString());
		
		
	}

	public static Double round(Double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
}