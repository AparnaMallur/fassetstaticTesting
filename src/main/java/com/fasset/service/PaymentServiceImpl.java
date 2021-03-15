package com.fasset.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IBankDAO;
import com.fasset.dao.interfaces.IPaymentDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.IPurchaseEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.ISupplierDAO;
import com.fasset.dao.interfaces.IUnitOfMeasurementDAO;
import com.fasset.entities.DebitNote;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PaymentDetails;
import com.fasset.entities.Product;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.UnitOfMeasurement;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;

@Service
@Transactional
public class PaymentServiceImpl implements IPaymentService {

	@Autowired
	private IPaymentDAO paymentDao;

	@Autowired
	private IAccountingYearDAO accountYearDAO;

	@Autowired
	private ISupplierDAO supplierDao;

	@Autowired
	private IProductDAO productDao;

	@Autowired
	private IUnitOfMeasurementDAO uomDao;

	@Autowired
	private IPurchaseEntryDAO purchaseEntryDao;

	@Autowired
	private ISubLedgerDAO subledgerDao;

	@Autowired
	private IBankDAO bankDao;

	@Autowired
	ISubLedgerService subledgerService;

	@Autowired
	ISuplliersService supplierService;

	@Autowired
	IBankService bankService;

	@Override
	public void add(Payment entity) throws MyWebException {

		try {
			
			entity.setAccountYearId(entity.getAccountYearId());
			entity.setAccountingYear(accountYearDAO.findOne(entity.getAccountYearId()));
			entity.setEntry_status(MyAbstractController.ENTRY_PENDING);
			
			int payType = entity.getPayment_type();
			if (payType == MyAbstractController.PAYMENT_TYPE_CHEQUE || payType == MyAbstractController.PAYMENT_TYPE_DD || payType==MyAbstractController.PAYMENT_TYPE_NEFT) {
				entity.setCheque_date(new LocalDate(entity.getChequeDateString()));
			}
			entity.setCreated_date(new LocalDate());
			if (entity.getPurchaseEntryId() > 0) {
				entity.setSupplier_bill_no(purchaseEntryDao.findOne(entity.getPurchaseEntryId()));
			}
			if (entity.getSupplierId() > 0) {
				Suppliers suppliers = supplierDao.findOne(entity.getSupplierId());
				entity.setSupplier(suppliers);
			}
			if (entity.getSubLedgerId() > 0) {
				SubLedger subLedger = subledgerDao.findOne(entity.getSubLedgerId());
				entity.setSubLedger(subLedger);
			}
			if (entity.getBankId() > 0) {
				entity.setBank(bankDao.findOne(entity.getBankId()));
			}
			if (entity.getPayDetails() != "") {
				JSONArray jsonArray = new JSONArray(entity.getPayDetails());
				Set<PaymentDetails> paymentDetailsList = new HashSet<PaymentDetails>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					PaymentDetails payDetails = new PaymentDetails();
					payDetails.setCgst(Double.parseDouble(json.getString("cgst")));
					payDetails.setIgst(Double.parseDouble(json.getString("igst")));
					payDetails.setSgst(Double.parseDouble(json.getString("sgst")));
					payDetails.setState_com_tax(Double.parseDouble(json.getString("state_com_tax")));
					payDetails.setDiscount(Double.parseDouble(json.getString("discount")));
					payDetails.setFrieght(Double.parseDouble(json.getString("freight")));
					payDetails.setLabour_charges(Double.parseDouble(json.getString("labour_charges")));
					payDetails.setOthers(Double.parseDouble(json.getString("others")));
					payDetails.setQuantity(Integer.parseInt(json.getString("quantity")));
					payDetails.setRate(Double.parseDouble(json.getString("rate")));
					payDetails.setTransaction_amount(Double.parseDouble(json.getString("transactionAmount")));
					Product product = productDao.findOne(Long.parseLong(json.getString("productId")));
					payDetails.setProduct_id(product);
					UnitOfMeasurement uom = uomDao.findOne(Long.parseLong(json.getString("uomId")));
					payDetails.setUom_id(uom);
					payDetails.setPayment_id(entity);
					paymentDetailsList.add(payDetails);
				}
				entity.setPaymentDetails(paymentDetailsList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setTds_paid(entity.getTds_paid());
		/** tds calculation for advance payment if user selected tds paid = yes for supplier then that amount will be consider as tds amount and if user selected tds paid = "no" but supplier is having gst applicable as YES then tds is deducted on that basis ie on % basis */
			if(entity.getAdvance_payment().equals(true) && entity.getGst_applied().equals(false))
			{
					if(entity.getTds_paid()==true)
					{
						entity.setTds_amount(entity.getTds_amount());
						entity.setAmount(entity.getAmount()-entity.getTds_amount());
						entity.setRound_off(entity.getAmount()-entity.getTds_amount());
						entity.setTds_paid(true);
					}
					else
					{
						if(entity.getSupplierId() > 0)
						{
							if(entity.getSupplier().getTds_applicable()==1)
							{
								if (entity.getGst_applied() == true) {
									entity.setTds_amount((entity.getTransaction_value_head()*entity.getSupplier().getTds_rate())/100);
								}
								else
								{
									entity.setTds_amount((entity.getAmount()*entity.getSupplier().getTds_rate())/100);
								}
								entity.setAmount(entity.getAmount()-entity.getTds_amount());
								entity.setRound_off(entity.getAmount()-entity.getTds_amount());
								entity.setTds_paid(true);
							}
							else
							{
								entity.setTds_paid(false);
								entity.setTds_amount((double) 0);				
							}
						}
						else
						{
							entity.setTds_paid(false);
							entity.setTds_amount((double) 0);				
						}
					}
			}
			else if(entity.getAdvance_payment().equals(true) && entity.getGst_applied().equals(true))
			{
				entity.setAmount(entity.getAmount());
				entity.setRound_off(entity.getAmount());
				entity.setTds_amount(entity.getTds_amount());
			}
			
		/*   */
			
			
			
			
			
			else
			{   
				/**IF tds is already cut for purchase entry then we are adding payment against that purchase entry then tds value for particular payment is storing in database for showing tds against this payment in payment report & ledger report */
				Double tds=(double) 0;
				if(entity.getSupplier_bill_no() != null && entity.getSupplier_bill_no().getTds_amount()!=null){	
				tds=(entity.getAmount()*entity.getSupplier_bill_no().getTds_amount())/entity.getSupplier_bill_no().getRound_off();
				}
				entity.setTds_amount(tds);	
				entity.setTds_paid(false); /**here we are giving tds paid = false because tds is already deducted for purchase entry. this tds amount will required for report  payment report & ledger report .*/
				entity.setAdvance_payment(false);
			}
		
	
			entity.setLocal_time(new LocalTime());
			
			if(entity.getPurchaseEntryId().equals((long)-2))
			{
				entity.setAgainstOpeningBalnce(true);
			}
			else
			{
				entity.setAgainstOpeningBalnce(false);
			}
			Long id = paymentDao.savePaymentThroughExcel(entity);		
		
			Payment entry = paymentDao.findOne(id);
			 
		if(entity.getSupplier_bill_no() != null){				
			PurchaseEntry purchaseEntry = purchaseEntryDao.findOneWithAll(entity.getSupplier_bill_no().getPurchase_id());
			Double total =(double) 0;
			for (Payment payment : purchaseEntry.getPayments()) {
					total += payment.getAmount();
			}
			for (DebitNote note : purchaseEntry.getDebitNote()) {
				total += note.getRound_off();
		    }
			if(purchaseEntry.getRound_off().equals(total)) {
				purchaseEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
			}
			else if(total>=purchaseEntry.getRound_off())
			{
				purchaseEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
			}
			else{
				purchaseEntry.setEntry_status(MyAbstractController.ENTRY_PENDING);			
			}
			purchaseEntryDao.update(purchaseEntry);
		}	
		
		
		if (entity.getSupplierId() > 0) {
			if(entity.getAdvance_payment()!=null && entity.getAdvance_payment()==true) {
				supplierService.addsuppliertrsansactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getSupplierId(), (long) 4, (double) 0, ((double)entity.getAmount()+(double)entity.getTds_amount()), (long) 1);
			
		    }
			else {
				
				supplierService.addsuppliertrsansactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getSupplierId(), (long) 4, (double) 0, ((double)entity.getAmount()), (long) 1);
			}
		}
		
		if (entity.getSubLedgerId() > 0) {
			subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getSubLedgerId(), (long) 2, (double) 0, ((double)entity.getAmount()+(double)entity.getTds_amount()), (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
		}
		if(entity.getTds_paid()!=null && entity.getTds_paid()==true)
		{
			SubLedger subledgertds = subledgerDao.findOne("TDS Payable", entity.getCompany().getCompany_id());
			subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)entity.getTds_amount(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
		}
		if (entity.getGst_applied() == false) {
			if (entity.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {
				SubLedger subledgercgst = subledgerDao.findOne("Cash In Hand", entity.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)entity.getAmount(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
			} 
			else {
				bankService.addbanktransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getBank().getBank_id(), (long) 3, (double)entity.getAmount(), (double) 0, (long) 1);
			}
		} 
		else {
			if (entity.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {
				SubLedger subledgercash = subledgerDao.findOne("Cash In Hand", entity.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, (double)(entity.getTransaction_value_head()), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
			}
			else {
				bankService.addbanktransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getBank().getBank_id(), (long) 3, (double)(entity.getTransaction_value_head()), (double) 0, (long) 1);
			}
			
			if (entity.getCGST_head() != null && entity.getCGST_head() > 0) {
				SubLedger subledgercgst = subledgerDao.findOne("Input CGST", entity.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)entity.getCGST_head(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
			}
			if (entity.getSGST_head() != null && entity.getSGST_head() > 0) {
				SubLedger subledgersgst = subledgerDao.findOne("Input SGST", entity.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)entity.getSGST_head(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
			}
			if (entity.getIGST_head() != null && entity.getIGST_head() > 0) {
				SubLedger subledgerigst = subledgerDao.findOne("Input IGST", entity.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)entity.getIGST_head(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
			}
			if (entity.getSCT_head() != null && entity.getSCT_head() > 0) {
				SubLedger subledgercess = subledgerDao.findOne("Input CESS", entity.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)entity.getSCT_head(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
			}
			if (entity.getTotal_vat() != null && entity.getTotal_vat() > 0) {
				SubLedger subledgervat = subledgerDao.findOne("Input VAT", entity.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)entity.getTotal_vat(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
			}
			if (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() > 0) {
				SubLedger subledgercst = subledgerDao.findOne("Input CST", entity.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)entity.getTotal_vatcst(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
			}
			if (entity.getTotal_excise() != null && entity.getTotal_excise() > 0) {
				SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", entity.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)entity.getTotal_excise(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
			}
		}
	}

	@Override
	public void update(Payment entity) throws MyWebException {
		Boolean flag = false;
		Payment payment = paymentDao.findOneWithAll(entity.getPayment_id());

		Payment oldpayment = new Payment();

		if(payment.getAmount()!=null)
		{
		oldpayment.setAmount(payment.getAmount());
		}
		if(payment.getRound_off()!=null)
		{
		oldpayment.setRound_off(payment.getRound_off());
		}
		if (payment.getTds_amount() != null) {
			oldpayment.setTds_amount(payment.getTds_amount());
		}
		if(payment.getTds_paid()!=null)
		{
			oldpayment.setTds_paid(payment.getTds_paid());
		}
		if(payment.getAdvance_payment()!=null)
		{
			oldpayment.setAdvance_payment(payment.getAdvance_payment());
		}
		if(payment.getTransaction_value_head()!=null)
		{
		oldpayment.setTransaction_value_head(payment.getTransaction_value_head());
		}		
		if (payment.getSupplier() != null) {
			oldpayment.setSupplier(payment.getSupplier());
		}
		if (payment.getSupplier_bill_no() != null) {
			oldpayment.setSupplier_bill_no(payment.getSupplier_bill_no());
		}
		if (payment.getSubLedger() != null) {
			oldpayment.setSubLedger(payment.getSubLedger());
		}
		if (payment.getBank() != null) {
			oldpayment.setBank(payment.getBank());
		}
		if (payment.getCGST_head() != null) {
			oldpayment.setCGST_head(payment.getCGST_head());
		}
		if (payment.getSGST_head() != null) {
			oldpayment.setSGST_head(payment.getSGST_head());
		}
		if (payment.getIGST_head() != null) {
			oldpayment.setIGST_head(payment.getIGST_head());
		}
		if (payment.getSCT_head() != null) {
			oldpayment.setSCT_head(payment.getSCT_head());
		}
		if (payment.getTotal_vat() != null) {
			oldpayment.setTotal_vat(payment.getTotal_vat());
		}
		if (payment.getTotal_vatcst() != null) {
			oldpayment.setTotal_vatcst(payment.getTotal_vatcst());
		}
		if (payment.getTotal_excise() != null) {
			oldpayment.setTotal_excise(payment.getTotal_excise());
		}
		

		try {
			
			/*if(entity.getAdvance_payment()!=null)
			{
			payment.setAdvance_payment(entity.getAdvance_payment());
			}*/
			/*if(entity.getAmount()!=null)
			{
			payment.setAmount(entity.getAmount());
			}
			if(entity.getRound_off()!=null)
			{
				payment.setRound_off(entity.getRound_off());
			}
			if(entity.getTds_amount()!=null)
			{
				payment.setTds_amount(entity.getTds_amount());
			}*/
			
			if(entity.getCheque_dd_no()!=null)
			{
				payment.setCheque_dd_no(entity.getCheque_dd_no());
			}
			if(entity.getGst_applied()!=null)
			{
				payment.setGst_applied(entity.getGst_applied());
			}
			
			if(entity.getOther_remark()!=null)
			{
				payment.setOther_remark(entity.getOther_remark());
			}
			
			if(entity.getStatus()!=null)
			{
				payment.setStatus(entity.getStatus());
			}
			
			payment.setDate(entity.getDate());			
			
			if(entity.getPayment_type()!=null)
			{
				payment.setPayment_type(entity.getPayment_type());
			}
			
			if(entity.getCGST_head()!=null)
			{
				payment.setCGST_head(entity.getCGST_head());
			}
			if(entity.getSGST_head()!=null)
			{
				payment.setSGST_head(entity.getSGST_head());
			}
			if(entity.getIGST_head()!=null)
			{
				payment.setIGST_head(entity.getIGST_head());
			}
			if(entity.getSCT_head()!=null)
			{
				payment.setSCT_head(entity.getSCT_head());
			}
			if(entity.getTotal_vat()!=null)
			{
				payment.setTotal_vat(entity.getTotal_vat());
			}
			if(entity.getTotal_vatcst()!=null)
			{
				payment.setTotal_vatcst(entity.getTotal_vatcst());
			}
			if(entity.getTransaction_value_head()!=null)
			{
				payment.setTransaction_value_head(entity.getTransaction_value_head());
			}
			
			
			
			if((entity.getPayment_type()!=null)&&(entity.getPayment_type() != MyAbstractController.PAYMENT_TYPE_CASH)){
				if(entity.getChequeDateString()!=null)
				{
				payment.setCheque_date(new LocalDate(entity.getChequeDateString()));
				}
				if ((entity.getBankId()!=null)&& (entity.getBankId() > 0)) {
					payment.setBank(bankDao.findOne(entity.getBankId()));
				}
			}
			
			payment.setUpdate_date(new LocalDate());
			
			if (entity.getPurchaseEntryId() != null && entity.getPurchaseEntryId() > 0) {
				payment.setSupplier_bill_no(purchaseEntryDao.findOne(entity.getPurchaseEntryId()));
			} else {
				payment.setSupplier_bill_no(null);
			}
			
			if (entity.getSupplierId() != null && entity.getSupplierId() > 0) {
				Suppliers suppliers = supplierDao.findOne(entity.getSupplierId());
				payment.setSupplier(suppliers);
			} else {
				payment.setSupplier(null);
			}
			if (entity.getSubLedgerId() != null && entity.getSubLedgerId() > 0) {
				SubLedger subLedger = subledgerDao.findOne(entity.getSubLedgerId());
				payment.setSubLedger(subLedger);
			} else {
				payment.setSubLedger(null);
			}
			payment.setTds_paid(entity.getTds_paid());
			
				if(entity.getAdvance_payment()==true  && entity.getGst_applied().equals(false))
				{
						if(entity.getTds_paid()==true)
						{
							if(payment.getTds_amount()!=null && payment.getAmount()!=null && !(payment.getTds_amount().equals(entity.getTds_amount())) && !(payment.getAmount().equals(entity.getAmount()))) {
								
								payment.setTds_amount(entity.getTds_amount());
								payment.setAmount(entity.getAmount()-entity.getTds_amount());
								payment.setRound_off(entity.getAmount()-entity.getTds_amount());
								payment.setTds_paid(true);
										}
							if(payment.getAmount()!=null && !(payment.getAmount().equals(entity.getAmount()))) {
								payment.setTds_amount(entity.getTds_amount());
								payment.setAmount(entity.getAmount()-entity.getTds_amount());
								payment.setRound_off(entity.getAmount()-entity.getTds_amount());
								payment.setTds_paid(true);
							}
							
						}
						else
						{
							if(entity.getSupplierId() > 0)
							{
								if(payment.getSupplier().getTds_applicable()==1)
								{
									if(payment.getTds_amount()!=null && payment.getAmount()!=null && !(payment.getTds_amount().equals(entity.getTds_amount())) && !(payment.getAmount().equals(entity.getAmount()))) {
									if (payment.getGst_applied() == true) {
										payment.setTds_amount((entity.getTransaction_value_head()*payment.getSupplier().getTds_rate())/100);
									}
									else
									{
									payment.setTds_amount((entity.getAmount()*payment.getSupplier().getTds_rate())/100);
									}
									payment.setAmount(entity.getAmount()-payment.getTds_amount());
									payment.setRound_off(entity.getAmount()-payment.getTds_amount());
									payment.setTds_paid(true);
									}
									if(payment.getAmount()!=null && !(payment.getAmount().equals(entity.getAmount()))) {
										if (payment.getGst_applied() == true) {
											payment.setTds_amount((entity.getTransaction_value_head()*payment.getSupplier().getTds_rate())/100);
										}
										else
										{
										payment.setTds_amount((entity.getAmount()*payment.getSupplier().getTds_rate())/100);
										}
										payment.setAmount(entity.getAmount()-payment.getTds_amount());
										payment.setRound_off(entity.getAmount()-payment.getTds_amount());
										payment.setTds_paid(true);
									}

								}
								else
								{
									payment.setTds_paid(false);
									payment.setTds_amount((double) 0);
									payment.setAmount(entity.getAmount());
									payment.setRound_off(entity.getAmount());
								}
							}
							else
							{
								payment.setTds_paid(false);
								payment.setTds_amount((double) 0);
								payment.setAmount(entity.getAmount());
								payment.setRound_off(entity.getAmount());
							}
						}
				}
				else if(entity.getAdvance_payment().equals(true) && entity.getGst_applied().equals(true))
				{
					payment.setAmount(entity.getAmount());
					payment.setRound_off(entity.getAmount());
					payment.setTds_amount(entity.getTds_amount());
				}
				else
				{
					/**IF tds is already cut for purchase entry then we are adding payment against that purchase entry then tds value for particular payment is storing in database for showing tds against this payment in payment report & ledger report */
					Double tds=(double) 0;
					if(payment.getSupplier_bill_no() != null && payment.getSupplier_bill_no().getTds_amount()!=null){	
					tds=(entity.getAmount()*payment.getSupplier_bill_no().getTds_amount())/payment.getSupplier_bill_no().getRound_off();
					}
					payment.setAmount(entity.getAmount());
					payment.setTds_amount(tds);	
					payment.setTds_paid(false); /**here we are giving tds paid = false because tds is already deducted for purchase entry. this tds amount will required for report  payment report & ledger report .*/
					payment.setAdvance_payment(false);
				}
			
			
			
			if ((entity.getPayDetails()!=null)&&(entity.getPayDetails() != "")) {
				JSONArray jsonArray = new JSONArray(entity.getPayDetails());
				Set<PaymentDetails> paymentDetailsList = payment.getPaymentDetails();// payment.getPaymentDetails();
				for (int i = 0; i < jsonArray.length(); i++) {
					System.out.println(jsonArray.length());
					JSONObject json = jsonArray.getJSONObject(i);
					PaymentDetails payDetails = new PaymentDetails();
					if (json.getInt("id") == 0) {
						flag = true;
						payDetails.setCgst(Double.parseDouble(json.getString("cgst")));
						payDetails.setIgst(Double.parseDouble(json.getString("igst")));
						payDetails.setSgst(Double.parseDouble(json.getString("sgst")));
						payDetails.setState_com_tax(Double.parseDouble(json.getString("state_com_tax")));
						payDetails.setDiscount(Double.parseDouble(json.getString("discount")));
						payDetails.setFrieght(Double.parseDouble(json.getString("freight")));
						payDetails.setLabour_charges(Double.parseDouble(json.getString("labour_charges")));
						payDetails.setOthers(Double.parseDouble(json.getString("others")));
						payDetails.setQuantity(Integer.parseInt(json.getString("quantity")));
						payDetails.setTransaction_amount(Double.parseDouble(json.getString("transactionAmount")));
						payDetails.setRate(Double.parseDouble(json.getString("rate")));
						Product product = productDao.findOne(Long.parseLong(json.getString("productId")));
						payDetails.setProduct_id(product);
						UnitOfMeasurement uom = uomDao.findOne(Long.parseLong(json.getString("uomId")));
						payDetails.setUom_id(uom);
						payDetails.setPayment_id(payment);
						paymentDetailsList.add(payDetails);
					}
				}
				if (flag)
					payment.setPaymentDetails(paymentDetailsList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		payment.setFlag(true);
		if(entity.getUpdated_by()!=null)
		{
			payment.setUpdated_by(entity.getUpdated_by());
		}
		try {
			
			paymentDao.update(payment);

			if(oldpayment.getSupplier_bill_no() != null){				
				PurchaseEntry purchaseEntry = purchaseEntryDao.findOneWithAll(oldpayment.getSupplier_bill_no().getPurchase_id());
				Double total =(double) 0;
				for (Payment payment1 : purchaseEntry.getPayments()) {
						total += payment1.getAmount();
				}
				for (DebitNote note : purchaseEntry.getDebitNote()) {
					total += note.getRound_off();
			    }
				if(purchaseEntry.getRound_off().equals(total)) {
					purchaseEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
				}
				else if(total>=purchaseEntry.getRound_off())
				{
					purchaseEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
				}
				else{
					purchaseEntry.setEntry_status(MyAbstractController.ENTRY_PENDING);			
				}
				purchaseEntryDao.update(purchaseEntry);
			}	
						
			if (oldpayment.getSupplier() != null) {
				if(oldpayment.getAdvance_payment()!=null && oldpayment.getAdvance_payment()==true) {
					supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), oldpayment.getSupplier().getSupplier_id(), (long) 4, (double) 0, ((double)oldpayment.getAmount()+(double)oldpayment.getTds_amount()), (long) 0);
				}
				else
				{
					supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), oldpayment.getSupplier().getSupplier_id(), (long) 4, (double) 0, ((double)oldpayment.getAmount()), (long) 0);
				}
			}
			if (entity.getSupplierId() != null && entity.getSupplierId() > 0) {
				if(oldpayment.getAdvance_payment()!=null && oldpayment.getAdvance_payment()==true) {
					supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), entity.getSupplierId(), (long) 4, (double) 0, ((double)payment.getAmount()+(double)payment.getTds_amount()), (long) 1);
				
				}
				else
				{
					supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), entity.getSupplierId(), (long) 4, (double) 0, ((double)payment.getAmount()), (long) 1);
				}
			}
			
			if (oldpayment.getSubLedger() != null) {
				subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), oldpayment.getSubLedger().getSubledger_Id(), (long) 2, (double) 0, ((double)oldpayment.getAmount()+(double)oldpayment.getTds_amount()), (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
			}
			if (entity.getSubLedgerId() != null && entity.getSubLedgerId() > 0) {
				subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), entity.getSubLedgerId(), (long) 2, (double) 0, ((double)payment.getAmount()+(double)payment.getTds_amount()), (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
			}
			
			if ((oldpayment.getAmount() != entity.getAmount())) {									
				if (entity.getGst_applied() == false) {
					if (entity.getPayment_type() == 1) {
						SubLedger subledgercash = subledgerDao.findOne("Cash In Hand", payment.getCompany().getCompany_id());
						if (subledgercash != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, (double)oldpayment.getAmount(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, (double)payment.getAmount(), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					} 
					else {
						bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), entity.getBankId(), (long) 3, (double)oldpayment.getAmount(), (double) 0, (long) 0);
						bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), entity.getBankId(), (long) 3, (double)payment.getAmount(), (double) 0, (long) 1);
					}
				}
			}
			if(payment.getTds_paid()!=null && payment.getTds_paid()==true && oldpayment.getTds_amount()!=payment.getTds_amount())
			{
				  SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", payment.getCompany().getCompany_id());
				  if (subledgerTds != null) {
					  subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2,  (double)oldpayment.getTds_amount(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					  subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2,  (double)payment.getTds_amount(), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
				  }
			}
			
			if ((oldpayment.getTransaction_value_head() != null && entity.getTransaction_value_head()!=null)&&(oldpayment.getTransaction_value_head() != entity.getTransaction_value_head())) {
				
				if(entity.getGst_applied()!=null && entity.getPayment_type()!=null)
				{
				if (entity.getGst_applied() == true) {
					if (entity.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {
						SubLedger subledgercash = subledgerDao.findOne("Cash In Hand", payment.getCompany().getCompany_id());

						if (subledgercash != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, (double)(oldpayment.getTransaction_value_head()), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, (double)(entity.getTransaction_value_head()), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					else {
						bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), oldpayment.getBank().getBank_id(), (long) 3, (double)(oldpayment.getTransaction_value_head()), (double) 0, (long) 0);
						bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), entity.getBankId(), (long) 3, (double)(entity.getTransaction_value_head()), (double) 0, (long) 1);
					}
					
					SubLedger subledgercgst = subledgerDao.findOne("Input CGST", payment.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)oldpayment.getCGST_head(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)entity.getCGST_head(), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
					}

					SubLedger subledgersgst = subledgerDao.findOne("Input SGST", payment.getCompany().getCompany_id());
					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)oldpayment.getSGST_head(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)entity.getSGST_head(), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
					}

					SubLedger subledgerigst = subledgerDao.findOne("Input IGST", payment.getCompany().getCompany_id());
					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)oldpayment.getIGST_head(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)entity.getIGST_head(), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
					}

					SubLedger subledgercess = subledgerDao.findOne("Input CESS", payment.getCompany().getCompany_id());
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)oldpayment.getSCT_head(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)entity.getSCT_head(), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
					}

					SubLedger subledgervat = subledgerDao.findOne("Input VAT", payment.getCompany().getCompany_id());
					if (subledgervat != null) {
						if(oldpayment.getTotal_vat()!=null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)oldpayment.getTotal_vat(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
						if(entity.getTotal_vat()!=null){
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)entity.getTotal_vat(), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
						
					SubLedger subledgercst = subledgerDao.findOne("Input CST", payment.getCompany().getCompany_id());
					if (subledgercst != null) {
						if(oldpayment.getTotal_vatcst()!=null)
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)oldpayment.getTotal_vatcst(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						
						if(entity.getTotal_vatcst()!=null)
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)entity.getTotal_vatcst(), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
					
					SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", payment.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						if(oldpayment.getTotal_excise()!=null)
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)oldpayment.getTotal_excise(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						
						if(entity.getTotal_excise()!=null)
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)entity.getTotal_excise(), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
					}

				}
				
			    }
			}
/*
			if ((oldpayment.getSupplier() != null) && (entity.getSupplierId() == -1) && (entity.getSubLedgerId() > 0)) {

				if (oldpayment.getSupplier().getSupplier_id() != null) {
					supplierService.addsuppliertrsansactionbalance(payment.getCompany().getCompany_id(), oldpayment.getSupplier().getSupplier_id(), (long) 4, (double) 0, (double)oldpayment.getAmount(), (long) 0);
				}
				subledgerService.addsubledgertransactionbalance(payment.getCompany().getCompany_id(), entity.getSubLedgerId(), (long) 2, (double) 0, (double)entity.getAmount(), (long) 1);
			}
			if ((oldpayment.getSubLedger() != null) && (entity.getSupplierId() > 0)) {
				if (oldpayment.getSubLedger() != null) {
					subledgerService.addsubledgertransactionbalance(payment.getCompany().getCompany_id(), oldpayment.getSubLedger().getSubledger_Id(), (long) 2, (double) 0, (double)oldpayment.getAmount(), (long) 0);
				}

				if (entity.getSupplierId() != null) {
					supplierService.addsuppliertrsansactionbalance(payment.getCompany().getCompany_id(), entity.getSupplierId(), (long) 4, (double) 0, (double)entity.getAmount(), (long) 1);
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Payment> list() {
		return paymentDao.findAll();
	}

	@Override
	public Payment getById(Long id) throws MyWebException {
		return paymentDao.findOne(id);
	}

	@Override
	public Payment getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Payment entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Payment entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public Payment isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getCount() {
		return paymentDao.getCount();
	}

	@Override
	public List<Payment> getReport(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {
		return paymentDao.getReport(Id, from_date, to_date, comId);
	}
	

	/*
	 * @Override public List<PurchaseEntry> findAllSuppliersWithDate(LocalDate
	 * from_date, LocalDate to_date, Long company_id) { List<PurchaseEntry>
	 * suppliersListWithBillsPayable = new ArrayList<PurchaseEntry>();
	 * 
	 * List<PurchaseEntry> purchaseEntryList =
	 * paymentDao.findAllPurchaseEntryWithDate(from_date, to_date, company_id);
	 * List<Payment> paymentList = paymentDao.findAllPaymentOfCompany(company_id,
	 * true,yearID);
	 * 
	 * for (PurchaseEntry paymentDetails : purchaseEntryList) { Boolean flag =
	 * false; String bill_no = paymentDetails.getSupplier_bill_no(); for (Payment
	 * payment : paymentList) { if (bill_no.equals(payment.getSupplier_bill_no())) {
	 * flag = true; } } if (flag == false) {
	 * suppliersListWithBillsPayable.add(paymentDetails); } } return
	 * suppliersListWithBillsPayable; }
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		long purchase_id=0;
		int status= MyAbstractController.ENTRY_CANCEL;

		try {
			Double transaction_value = (double) 0;
			Double cgst = (double) 0;
			Double igst = (double) 0;
			Double sgst = (double) 0;
			Double vat = (double) 0;
			Double vatcst = (double) 0;
			Double excise = (double) 0;
			Double state_comp_tax = (double) 0;
			Double amount = (double) 0;
			Double tds = (double) 0;
			Payment payment = paymentDao.findOneWithAll(entityId);
			if (payment.getAmount() != null) {
				amount = payment.getAmount();
			}
			if (payment.getTds_amount() != null) {
				tds = payment.getTds_amount();
			}
			if (payment.getTransaction_value_head() != null && payment.getTransaction_value_head() > 0) {
				transaction_value = payment.getTransaction_value_head();
			}
			if (payment.getCGST_head() != null && payment.getCGST_head() > 0) {
				cgst = payment.getCGST_head();
			}
			if (payment.getIGST_head() != null && payment.getIGST_head() > 0) {
				igst = payment.getIGST_head();
			}
			if (payment.getSGST_head() != null && payment.getSGST_head() > 0) {
				sgst = payment.getSGST_head();
			}
			if (payment.getSCT_head() != null && payment.getSCT_head() > 0) {
				state_comp_tax = payment.getSCT_head();
			}
			if (payment.getTotal_vat() != null && payment.getTotal_vat() > 0) {
				vat = payment.getTotal_vat();
			}
			if (payment.getTotal_vatcst() != null && payment.getTotal_vatcst() > 0) {
				vatcst = payment.getTotal_vatcst();
			}
			if (payment.getTotal_excise() != null && payment.getTotal_excise() > 0) {
				excise = payment.getTotal_excise();
			}

			try {
				if (payment.getSupplier() != null) {
					if(payment.getAdvance_payment()!=null && payment.getAdvance_payment()==true) {
					supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSupplier().getSupplier_id(), (long) 4, (double) 0, ((double)amount +(double) payment.getTds_amount()), (long) 0);
					}
					else
					{
						supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSupplier().getSupplier_id(), (long) 4, (double) 0, (double)amount, (long) 0);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (payment.getSubLedger() != null) {
				try {
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSubLedger().getSubledger_Id(), (long) 2, (double) 0, ((double)amount + (double)payment.getTds_amount()), (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}		
			
			if(payment.getTds_paid()!=null && payment.getTds_paid()==true && payment.getTds_amount()!=null)
			{
				SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", payment.getCompany().getCompany_id());
			    if (subledgerTds != null) {
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2, (double)payment.getTds_amount(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
			    }
			}
			
			if(payment.getGst_applied()!=null && payment.getPayment_type() !=null)
			{
			if (payment.getGst_applied() == false) {
				if (payment.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {
					SubLedger subledgercgst = subledgerDao.findOne("Cash In Hand", payment.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)amount, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				} else {
					if (payment.getBank() != null) {
						bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getBank().getBank_id(), (long) 3, (double)amount, (double) 0, (long) 0);
					}
				}
			} else if (payment.getGst_applied() == true) {
				if (payment.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {

					if (transaction_value > 0) {
						SubLedger subledgercash = subledgerDao.findOne("Cash In Hand", payment.getCompany().getCompany_id());
						if (subledgercash != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, (double)(transaction_value), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
				} else {
					if (payment.getBank() != null) {
						bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(),
								payment.getBank().getBank_id(), (long) 3, (double)(transaction_value), (double) 0, (long) 0);
					}
				}
				if (cgst != null && cgst > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Input CGST", payment.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (sgst != null && sgst > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Input SGST", payment.getCompany().getCompany_id());
					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (igst != null && igst > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Input IGST", payment.getCompany().getCompany_id());
					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (state_comp_tax != null && state_comp_tax > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Input CESS", payment.getCompany().getCompany_id());
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (vat != null && vat > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Input VAT", payment.getCompany().getCompany_id());
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (vatcst != null && vatcst > 0) {
					SubLedger subledgercst = subledgerDao.findOne("Input CST", payment.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (excise != null && excise > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", payment.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
			}
			if(payment.getSupplier_bill_no()!=null)
			{
				purchase_id=payment.getSupplier_bill_no().getPurchase_id();				
			}
		}
			if(payment.getAdvance_payment()!=null && payment.getAdvance_payment()==true)
			{
				if(payment.getAdvpayment()!=null)
				{
					Payment advpayment = paymentDao.findOneWithAll(payment.getAdvpayment().getPayment_id());
				
					if(advpayment!=null)
					{
						if(advpayment.getSupplier_bill_no()==null)
						{
							if(payment.getAmount()>advpayment.getRound_off())
							{
								status= MyAbstractController.ENTRY_REVERT;
							}
							else
							{
								Double ramount=payment.getAmount()+advpayment.getAmount();
								Double transactionvalue=payment.getTransaction_value_head()+advpayment.getTransaction_value_head();
								Double tdsamount=payment.getTds_amount()+advpayment.getTds_amount();
								paymentDao.updateadvpaymentamount(payment.getAdvpayment().getPayment_id(),ramount,transactionvalue,tdsamount);
								
							}
						}
						else
						{
							//change status
							status= MyAbstractController.ENTRY_REVERT;
						}
					}
					else
					{
						status= MyAbstractController.ENTRY_REVERT;
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return paymentDao.deleteByIdValue(entityId,purchase_id,status);
	}

	@Override
	public List<Payment> getAdvancePaymentList(Long supplierId, Long companyId, Long yid) {
		List<Long> paymentIds = new ArrayList<Long>();
		/*List<PurchaseEntry> purchaseEntryList = purchaseEntryDao.findAllBySuppliers(supplierId, companyId);*/
		/*if (purchaseEntryList != null) {
			for (PurchaseEntry purchaseEntry : purchaseEntryList) {
				if (purchaseEntry.getPayments() != null) {
					for (Payment payment : purchaseEntry.getPayments()) {
						paymentIds.add(payment.getPayment_id());
					}
				}

			}
		}*/
		return paymentDao.getAdvancePaymentList(paymentIds, supplierId, companyId,yid);
	}

	
	@Override
	public List<Payment> getAllOpeningBalanceAgainstPayment(Long supplierId, Long companyId) {
		
		return paymentDao.getAllOpeningBalanceAgainstPayment( supplierId, companyId);
	}

	@Override
	public List<Payment> getExpenditure(Long yearId, Long companyId) {
		List<Payment> expenditure = new ArrayList<Payment>();
		List<Payment> payments = paymentDao.getExpenditureByYearId(yearId, companyId);
		for (Payment payment : payments) {
			boolean isExists = false;
			if (expenditure.size() > 0) {
				for (Payment expanse : expenditure) {
					if (payment.getSupplier_bill_no() != null) {
						if (expanse.getSubLedger().getSubledger_Id() == payment.getSupplier_bill_no().getSubledger()
								.getSubledger_Id()) {
							isExists = true;
							expanse.setAmount(expanse.getAmount() + payment.getAmount());
						}
					} else if (payment.getSubLedger() != null) {
						if (expanse.getSubLedger().getSubledger_Id() == payment.getSubLedger().getSubledger_Id()) {
							isExists = true;
							expanse.setAmount(expanse.getAmount() + payment.getAmount());
						}
					}
				}
			} else if (!isExists) {
				expenditure.add(payment);
			}
		}
		return expenditure;
	}

	@Override
	public List<Payment> getATList(Integer month, Long yearId, Long companyId) {
		return paymentDao.getATList(month, yearId, companyId);
	}

	@Override
	public List<Payment> getATAdjList(Integer month, Long yearId, Long companyId) {
		return paymentDao.getATAdjList(month, yearId, companyId);
	}

	@Override
	public Payment findOneWithAll(Long paymentId) {
		return paymentDao.findOneWithAll(paymentId);
	}

	@Override
	public Long savePaymentThroughExcel(Payment payment) {
		return paymentDao.savePaymentThroughExcel(payment);
	}

	@Override
	public void savePaymentDetailsThroughExcel(PaymentDetails details, Long payment_id) {
		paymentDao.savePaymentDetailsThroughExcel(details, payment_id);

	}

	@Override
	public List<Payment> findAllPaymentOfCompany(Long comapny_id, Boolean flag ) {
		return paymentDao.findAllPaymentOfCompany(comapny_id, flag);
	}

	@Override
	public List<Payment> getDayBookReport(LocalDate from_date,LocalDate to_date, Long companyId) {
		return paymentDao.getDayBookReport(from_date,to_date, companyId);
	}

	@Override
	public void updatePaymentThroughExcel(Payment paymentDetails) {
		paymentDao.updatePaymentThroughExcel(paymentDetails);
	}

	@Override
	public void updatePaymentDetailsThroughExcel(PaymentDetails details, Long payment_id) {
		paymentDao.updatePaymentDetailsThroughExcel(details, payment_id);
	}

	@Override
	public List<Payment> getCashBookBankBookReport(LocalDate from_date, LocalDate to_date, Long companyId, Integer type) {
		return paymentDao.getCashBookBankBookReport(from_date, to_date, companyId, type);
	}

	@Override
	public List<Payment> findAllPaymentsOfCompany(Long comapny_id) {
		return paymentDao.findAllPaymentsOfCompany(comapny_id);
	}

	@Override
	public PaymentDetails getPaymentDetailById(Long paymentDetailId) {
		return paymentDao.getPaymentDetailById(paymentDetailId);
	}

	@Override
	public void updatePaymentDetail(PaymentDetails paymentDetails, long company_id) {
		PaymentDetails payDetails = paymentDao.getPaymentDetailById(paymentDetails.getId());
		Payment payment= paymentDao.findOneWithAll(payDetails.getPayment_id().getPayment_id());
		
		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double state_comp_tax = (double)0;
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double excise = (double)0;
		Double transaction_value = (double)0;
		Double round_off = (double)0;
		
		Double preCGST = (double)0, newCGST = (double)0;
		Double preIGST = (double)0, newIGST = (double)0;
		Double preSGST = (double)0, newSGST = (double)0;
		Double preSCT = (double)0, newSCT = (double)0;
		Double preVAT = (double)0, newVAT = (double)0;
		Double preVATCST = (double)0, newVATCST = (double)0;
		Double preExcise = (double)0, newExcise = (double)0;
		Double pretransaction_amount=(double)0, newtransaction_amount=(double)0;
		Double preRound_off = (double)0, newRound_off = (double)0;
		Double tds=(double) 0;
		// Set previous payment detail values
		if(payment.getTds_amount()!=null)
		{
			tds=payment.getTds_amount();
		}
		if(payDetails.getCgst()!=null){
			preCGST=payDetails.getCgst();
		}
		if(payDetails.getIgst()!=null){
			preIGST=payDetails.getIgst();
		}
		if(payDetails.getSgst()!=null){
			preSGST=payDetails.getSgst();
		}
		if(payDetails.getState_com_tax()!=null){
			preSCT=payDetails.getState_com_tax();
		}
		if(payDetails.getTransaction_amount()!=null){
			pretransaction_amount=payDetails.getTransaction_amount();
		}
		if(payDetails.getVAT()!=null){
			preVAT=payDetails.getVAT();
		}
		if(payDetails.getVATCST()!=null){
			preVATCST=payDetails.getVATCST();
		}
		if(payDetails.getExcise()!=null) {
			preExcise=payDetails.getExcise();
		}
		
		// Set new payment detail values
		if(payDetails.getCgst()!=null){
			newCGST=paymentDetails.getCgst();
		}
		if(paymentDetails.getIgst()!=null){
			newIGST=paymentDetails.getIgst();
		}
		if(paymentDetails.getSgst()!=null){
			newSGST=paymentDetails.getSgst();
		}
		if(paymentDetails.getState_com_tax()!=null){
			newSCT=paymentDetails.getState_com_tax();
		}
		if(paymentDetails.getTransaction_amount()!=null){
			newtransaction_amount=paymentDetails.getTransaction_amount();
		}
		if(paymentDetails.getVAT()!=null){
			newVAT=paymentDetails.getVAT();
		}
		if(paymentDetails.getVATCST()!=null){
			newVATCST=paymentDetails.getVATCST();
		}
		if(paymentDetails.getExcise()!=null) {
			newExcise=paymentDetails.getExcise();
		}
		
		
		// Set payment values
		if(payment.getTransaction_value_head()!=null) {
			transaction_value= payment.getTransaction_value_head();
		}
		if(payment.getCGST_head()!=null){
			cgst=payment.getCGST_head();
		}
		if(payment.getIGST_head()!=null) {
			igst=payment.getIGST_head();
		}
		if(payment.getSGST_head()!=null){
			sgst=payment.getSGST_head();
		}
		if(payment.getSCT_head()!=null){
			state_comp_tax=payment.getSCT_head();
		}
		if(payment.getRound_off()!=null){
			round_off=payment.getRound_off();
		}
		if(payment.getTotal_vat()!=null){
			vat=payment.getTotal_vat();
		}
		if(payment.getTotal_vatcst()!=null){
			vatcst=payment.getTotal_vatcst();
		}
		if(payment.getTotal_excise()!=null) {
			excise=payment.getTotal_excise();
		}
		
		cgst = cgst-preCGST + newCGST;
		igst = igst-preIGST + newIGST;
		sgst = sgst-preSGST + newSGST;
		state_comp_tax = state_comp_tax - preSCT + newSCT;
		vat = vat - preVAT + newVAT;
		vatcst = vatcst - preVATCST + newVATCST;
		excise = excise - preExcise + newExcise;
		transaction_value=transaction_value - pretransaction_amount + newtransaction_amount;
		
		preRound_off = pretransaction_amount + preCGST + preIGST + preSGST + preSCT + preVAT + preVATCST + preExcise;
		newRound_off = newtransaction_amount + newCGST + newIGST + newSGST + newSCT + newVAT + newVATCST + newExcise;
		
		round_off = round_off - preRound_off + newRound_off;		
		
		/*if(payment.getTds_paid()==false)
		{
			if(payment.getSupplier()!=null)
			{
				if(payment.getSupplier().getTds_applicable()==1)
				{
					if (payment.getGst_applied() == true) {
					payment.setTds_amount((transaction_value*payment.getSupplier().getTds_rate())/100);
					}
					else
					{					
						payment.setTds_amount((round_off*payment.getSupplier().getTds_rate())/100);
					}
					//payment.setAmount(payment.getAmount()-payment.getTds_amount());
					payment.setTds_paid(true);
					round_off=round_off;
				}
				else
				{
					payment.setTds_amount((Double) 0);	
				}
			}		
		}*/
		/*if(tds!=payment.getTds_amount() && payment.getTds_paid()!=null && payment.getTds_paid()==true)
		{
			SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", payment.getCompany().getCompany_id());
		    if (subledgerTds != null) {
			subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2, (double)tds, (double) 0, (long) 0,null,null,null,payment,null,null,null);
			subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2, (double)payment.getTds_amount(), (double) 0, (long) 1,null,null,null,payment,null,null,null);
		    }
		}*/
		if ((payment.getAmount() != null) && (payment.getAmount() != round_off)) {
			if ((payment.getSupplier() != null) && (payment.getAmount() != round_off)) {
				
				if(payment.getAdvance_payment()!=null && payment.getAdvance_payment()==true) {
				supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id, payment.getSupplier().getSupplier_id(), (long) 5, (double) 0, ((double)payment.getAmount()), (long) 0);
				supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id, payment.getSupplier().getSupplier_id(), (long) 5, (double) 0, ((double)round_off), (long) 1);
				
				}
				else
				{
					supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id, payment.getSupplier().getSupplier_id(), (long) 5, (double) 0, ((double)payment.getAmount()), (long) 0);
					supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id, payment.getSupplier().getSupplier_id(), (long) 5, (double) 0, ((double)round_off), (long) 1);
				}
			}
		}
		
		if(payment.getGst_applied()!=null && payment.getPayment_type()!=null )
		{
		if (payment.getGst_applied()==true) {
			 if(payment.getPayment_type()==MyAbstractController.PAYMENT_TYPE_CASH) {
				 if((payment.getTransaction_value_head()!=null) && (payment.getTransaction_value_head()>0) && (transaction_value>0) && (payment.getTransaction_value_head()!=transaction_value)) {
					if(transaction_value>0) {
						SubLedger subledgercash = subledgerDao.findOne("Cash In Hand",company_id);
							
						if(subledgercash!=null){
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)(payment.getTransaction_value_head()),(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2, (double)(transaction_value),(double) 0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}	
					 }
				 }
			 }
			 else {
				 if((payment.getTransaction_value_head()!=null) && (payment.getTransaction_value_head()>0)&& (transaction_value>0) && (payment.getTransaction_value_head()!=transaction_value)){
					 if(payment.getBank()!=null){
						 bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,payment.getBank().getBank_id(),(long) 3,(double)(payment.getTransaction_value_head()),(double) 0,(long) 0);
						 bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,payment.getBank().getBank_id(),(long) 3, (double)(transaction_value),(double) 0,(long) 1);
					 }
				 }
			 }
			 
			 
			
				 SubLedger subledgercgst = subledgerDao.findOne("Input CGST",company_id);
				 if(subledgercgst!=null){
					 subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)payment.getCGST_head(),(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					 subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)cgst,(double) 0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
				 }
			
				 SubLedger subledgersgst = subledgerDao.findOne("Input SGST",company_id);
					
				 if(subledgersgst!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)payment.getSGST_head(),(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)sgst,(double) 0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
				 }
			
				 SubLedger subledgerigst = subledgerDao.findOne("Input IGST",company_id);
				
				 if(subledgerigst!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)payment.getIGST_head(),(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)igst,(double) 0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
				 }
			
				SubLedger subledgercess = subledgerDao.findOne("Input CESS",company_id);
				
				if(subledgercess!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)payment.getSCT_head(),(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)state_comp_tax,(double) 0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
		
				SubLedger subledgervat = subledgerDao.findOne("Input VAT",company_id);
				
				if(subledgervat!=null){
					if(payment.getTotal_vat()!=null)
					{
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)payment.getTotal_vat(),(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)vat,(double) 0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
		
				SubLedger subledgercst = subledgerDao.findOne("Input CST",company_id);
				
				if(subledgercst!=null){
					if(payment.getTotal_vatcst()!=null)
					{
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)payment.getTotal_vatcst(),(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)vatcst,(double) 0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
			
				SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE",company_id);
				
				if(subledgerexcise!=null){
					if(payment.getTotal_excise()!=null)
					{
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)payment.getTotal_excise(),(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)excise,(double) 0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
		
						
		 }
		
	}
		try {
			payDetails.setCgst(paymentDetails.getCgst());
			payDetails.setIgst(paymentDetails.getIgst());
			payDetails.setSgst(paymentDetails.getSgst());
			payDetails.setState_com_tax(paymentDetails.getState_com_tax());
			payDetails.setDiscount(paymentDetails.getDiscount());
			payDetails.setFrieght(paymentDetails.getFrieght());
			payDetails.setLabour_charges(paymentDetails.getLabour_charges());
			payDetails.setOthers(paymentDetails.getOthers());
			payDetails.setQuantity(paymentDetails.getQuantity());
			payDetails.setRate(paymentDetails.getRate());
			payDetails.setTransaction_amount(paymentDetails.getTransaction_amount());
			paymentDao.updatePaymentDetail(payDetails);
			
			payment.setCGST_head(round(cgst,2));
			payment.setIGST_head(round(igst,2));
			payment.setSGST_head(round(sgst,2));
			payment.setSCT_head(round(state_comp_tax,2));
			payment.setTotal_vat(vat);
			payment.setTotal_vatcst(vatcst);
			payment.setTotal_excise(excise);
			payment.setTransaction_value_head(transaction_value);
			payment.setRound_off(round_off);
			payment.setAmount(round_off);
			paymentDao.update(payment);
			
		} catch (MyWebException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Long deletePaymentDetail(Long id, long company_id) {		
		PaymentDetails payDetails = paymentDao.getPaymentDetailById(id);
		Long paymentId = payDetails.getPayment_id().getPayment_id();
		Payment payment = paymentDao.findOneWithAll(paymentId);		

		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double state_comp_tax = (double)0;
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double excise = (double)0;
		Double transaction_value = (double)0;
		Double round_off = (double)0;
		Double tds=(double)0;
		Double preCGST = (double)0;
		Double preIGST = (double)0;
		Double preSGST = (double)0;
		Double preSCT = (double)0;
		Double preVAT = (double)0;
		Double preVATCST = (double)0;
		Double preExcise = (double)0;
		Double pretransaction_amount=(double)0;
		Double preRound_off = (double)0;
		
		// Set previous payment detail values
		if(payment.getTds_amount()!=null)
		{
			tds =payment.getTds_amount();
		}
		if(payDetails.getCgst()!=null){
			preCGST=payDetails.getCgst();
		}
		if(payDetails.getIgst()!=null){
			preIGST=payDetails.getIgst();
		}
		if(payDetails.getSgst()!=null){
			preSGST=payDetails.getSgst();
		}
		if(payDetails.getState_com_tax()!=null){
			preSCT=payDetails.getState_com_tax();
		}
		if(payDetails.getVAT()!=null){
			preVAT=payDetails.getVAT();
		}
		if(payDetails.getVATCST()!=null){
			preVATCST=payDetails.getVATCST();
		}
		if(payDetails.getExcise()!=null) {
			preExcise=payDetails.getExcise();
		}
		if(payDetails.getTransaction_amount()!=null){
			pretransaction_amount=payDetails.getTransaction_amount();
		}
			
		// Set payment values
		if(payment.getTransaction_value_head()!=null) {
			transaction_value= payment.getTransaction_value_head();
		}
		if(payment.getCGST_head()!=null){
			cgst=payment.getCGST_head();
		}
		if(payment.getIGST_head()!=null) {
			igst=payment.getIGST_head();
		}
		if(payment.getSGST_head()!=null){
			sgst=payment.getSGST_head();
		}
		if(payment.getSCT_head()!=null){
			state_comp_tax=payment.getSCT_head();
		}
		if(payment.getTotal_vat()!=null){
			vat=payment.getTotal_vat();
		}
		if(payment.getTotal_vatcst()!=null){
			vatcst=payment.getTotal_vatcst();
		}
		if(payment.getTotal_excise()!=null) {
			excise=payment.getTotal_excise();
		}
		if(payment.getRound_off()!=null){
			round_off=payment.getRound_off();
		}
		
		cgst = cgst-preCGST;
		igst = igst-preIGST;
		sgst = sgst-preSGST;
		state_comp_tax = state_comp_tax - preSCT;
		vat = vat - preVAT;
		vatcst = vatcst - preVATCST;
		excise = excise - preExcise;
		transaction_value=transaction_value - pretransaction_amount;
		
		preRound_off = pretransaction_amount + preCGST + preIGST + preSGST + preSCT + preVAT + preVATCST + preExcise;
		round_off = round_off - preRound_off;
	
	/*	if(payment.getTds_paid()!=null && payment.getTds_paid()==false)
		{
			if(payment.getSupplier()!=null)
			{
				if(payment.getSupplier().getTds_applicable()==1)
				{
					if (payment.getGst_applied() == true) {
					payment.setTds_amount((transaction_value*payment.getSupplier().getTds_rate())/100);
					}
					else
					{
						payment.setTds_amount((round_off*payment.getSupplier().getTds_rate())/100);						
					}
					//payment.setAmount(payment.getAmount()-payment.getTds_amount());
					payment.setTds_paid(true);
					round_off=round_off;
				}
				else
				{
					payment.setTds_amount((Double) 0);	
				}
			}		
		}*/
	
		/*if((payment.getTds_amount()!=null)&&(tds!=payment.getTds_amount()))
		{
			SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", payment.getCompany().getCompany_id());
		    if (subledgerTds != null) {
			subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2, (double)tds, (double) 0, (long) 0,null,null,null,payment,null,null,null);
			subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2, (double)payment.getTds_amount(), (double) 0, (long) 1,null,null,null,payment,null,null,null);
		    }
		}	*/		
		if ((payment.getSupplier() != null)) {
			supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id, payment.getSupplier().getSupplier_id(), (long) 5, (double) 0, ((double)preRound_off), (long) 0);
		}
		
		if(payment.getGst_applied()!=null && payment.getPayment_type()!=null)
		{
		if (payment.getGst_applied()==true){
			if(payment.getPayment_type()== MyAbstractController.PAYMENT_TYPE_CASH){
				SubLedger subledgercash = subledgerDao.findOne("Cash In Hand",company_id);
			 
				if(subledgercash!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2, (double)pretransaction_amount, (double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
			}
			else {
				if(payment.getBank()!=null){
					 bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,payment.getBank().getBank_id(),(long) 3, (double)pretransaction_amount, (double) 0,(long) 0);
				}
			}
		 			
			if(preCGST!=null && preCGST>0){
				SubLedger subledgercgst = subledgerDao.findOne("Input CGST",company_id);
				
				if(subledgercgst!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)preCGST,(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
			}
			if(preSGST!=null && preSGST>0){
				SubLedger subledgersgst = subledgerDao.findOne("Input SGST",company_id);
				
				if(subledgersgst!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)preSGST,(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
			}
			if(preIGST!=null && preIGST>0){
				SubLedger subledgerigst = subledgerDao.findOne("Input IGST",company_id);
				
				if(subledgerigst!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)preIGST,(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
			}
			if(preSCT != null && preSCT > 0){
				SubLedger subledgercess = subledgerDao.findOne("Input CESS",company_id);
				
				if(subledgercess!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)preSCT,(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				}	
			}
			if(preVAT!=null && preVAT>0){
				SubLedger subledgervat = subledgerDao.findOne("Input VAT",company_id);
				
				if(subledgervat!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)preVAT,(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
			}
			if(preVATCST!=null && preVATCST>0){
				SubLedger subledgercst = subledgerDao.findOne("Input CST",company_id);
				
				if(subledgercst!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)preVATCST,(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
			}
			if(preExcise!=null && preExcise>0){
				SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE",company_id);
				
				if(subledgerexcise!=null){
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)preExcise,(double) 0,(long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				}
			}
		}
		
	}

		try {			
			payment.setCGST_head(round(cgst,2));
			payment.setIGST_head(round(igst,2));
			payment.setSGST_head(round(sgst,2));
			payment.setSCT_head(round(state_comp_tax,2));
			payment.setTotal_vat(vat);
			payment.setTotal_vatcst(vatcst);
			payment.setTotal_excise(excise);
			payment.setTransaction_value_head(transaction_value);
			payment.setRound_off(round_off);
			payment.setAmount(round_off);
			paymentDao.update(payment);
			paymentDao.deletePaymentDetail(id);
			
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		return paymentId;
	}
	
	public static Double round(Double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public List<Payment> findallpaymententryofsales(long id) {
		return paymentDao.findallpaymententryofsales(id);
	}

	@Override
	public void diactivateByIdValue(Long payment_id) {
		int status= MyAbstractController.ENTRY_CANCEL;

		try {
			Double transaction_value = (double) 0;
			Double cgst = (double) 0;
			Double igst = (double) 0;
			Double sgst = (double) 0;
			Double vat = (double) 0;
			Double vatcst = (double) 0;
			Double excise = (double) 0;
			Double state_comp_tax = (double) 0;
			Double amount = (double) 0;
			Double tds=(double)0;
			Payment payment = paymentDao.findOneWithAll(payment_id);
			if (payment.getAmount() != null) {
				amount = payment.getAmount();
			}
			if (payment.getTds_amount() != null) {
				tds = payment.getTds_amount();
			}
			if (payment.getTransaction_value_head() != null && payment.getTransaction_value_head() > 0) {
				transaction_value = payment.getTransaction_value_head();
			}

			if (payment.getCGST_head() != null && payment.getCGST_head() > 0) {
				cgst = payment.getCGST_head();
			}
			if (payment.getIGST_head() != null && payment.getIGST_head() > 0) {
				igst = payment.getIGST_head();
			}
			if (payment.getSGST_head() != null && payment.getSGST_head() > 0) {
				sgst = payment.getSGST_head();
			}
			if (payment.getSCT_head() != null && payment.getSCT_head() > 0) {
				state_comp_tax = payment.getSCT_head();
			}
			if (payment.getTotal_vat() != null && payment.getTotal_vat() > 0) {
				vat = payment.getTotal_vat();
			}
			if (payment.getTotal_vatcst() != null && payment.getTotal_vatcst() > 0) {
				vatcst = payment.getTotal_vatcst();
			}
			if (payment.getTotal_excise() != null && payment.getTotal_excise() > 0) {
				excise = payment.getTotal_excise();
			}

			try {
				if(payment.getAdvpayment()!=null)
				{
					/*Double ramount=payment.getAdvpayment().getAmount()+amount;
					paymentDao.updateadvpaymentamount(payment.getAdvpayment().getPayment_id(),ramount);*/
					
				}
				else
				{
						if (payment.getSupplier() != null) {
							supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSupplier().getSupplier_id(), (long) 4, (double) 0, ((double)amount+(double)tds), (long) 0);
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (payment.getSubLedger() != null) {
				try {
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSubLedger().getSubledger_Id(), (long) 2, (double) 0, ((double)amount+(double)tds), (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(payment.getTds_amount()!=null)
			{
				SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", payment.getCompany().getCompany_id());
			    if (subledgerTds != null) {
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2, (double)payment.getTds_amount(), (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
			    }
			}
			/*if(payment.getSupplier_bill_no()!=null){
				PurchaseEntry purchaseEntry = purchaseEntryDao.findOneWithAll(payment.getSupplier_bill_no().getPurchase_id());
			
				if(purchaseEntry.getPayments().size() <= 1) {
					SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", payment.getCompany().getCompany_id());
				    if (subledgerTds != null) {
				    	
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2, (double)payment.getTds_amount(), (double) 0, (long) 0);
						
						if (payment.getSupplier() != null) {
							supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSupplier().getSupplier_id(), (long) 4, (double)payment.getTds_amount(),(double) 0,  (long) 1);
						}
				    }
				}
		   }*/
			

			if(payment.getGst_applied()!=null && payment.getPayment_type()!=null)
			{
			if (payment.getGst_applied() == false) {
				if (payment.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {
					SubLedger subledgercgst = subledgerDao.findOne("Cash In Hand", payment.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)amount, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				} else {
					if (payment.getBank() != null) {
						bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getBank().getBank_id(), (long) 3, (double)amount, (double) 0, (long) 0);
					}
				}
			} else if (payment.getGst_applied() == true) {
				if (payment.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {

					if (transaction_value > 0) {
						SubLedger subledgercash = subledgerDao.findOne("Cash In Hand", payment.getCompany().getCompany_id());
						if (subledgercash != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, (double)transaction_value, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
				} else {
					if (payment.getBank() != null) {
						bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(),
								payment.getBank().getBank_id(), (long) 3, (double)transaction_value, (double) 0, (long) 0);
					}
				}
				if (cgst != null && cgst > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Input CGST", payment.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (sgst != null && sgst > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Input SGST", payment.getCompany().getCompany_id());
					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (igst != null && igst > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Input IGST", payment.getCompany().getCompany_id());
					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (state_comp_tax != null && state_comp_tax > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Input CESS", payment.getCompany().getCompany_id());
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (vat != null && vat > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Input VAT", payment.getCompany().getCompany_id());
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (vatcst != null && vatcst > 0) {
					SubLedger subledgercst = subledgerDao.findOne("Input CST", payment.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
				if (excise != null && excise > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", payment.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				}
			}
			
		}
			if(payment.getAdvance_payment()==true)
			{
				if(payment.getAdvpayment()!=null)
				{
					Payment advpayment = paymentDao.findOneWithAll(payment.getAdvpayment().getPayment_id());
				
					if(advpayment!=null)
					{
						if(advpayment.getSupplier_bill_no()==null)
						{
							if(payment.getAmount()>advpayment.getRound_off())
							{
								status= MyAbstractController.ENTRY_REVERT;
							}
							else
							{
								Double ramount=payment.getAmount()+advpayment.getAmount();
								Double transactionvalue=payment.getTransaction_value_head()+advpayment.getTransaction_value_head();
								Double tdsamount=payment.getTds_amount()+advpayment.getTds_amount();
								paymentDao.updateadvpaymentamount(payment.getAdvpayment().getPayment_id(),ramount,transactionvalue,tdsamount);
								
							}
						}
						else
						{
							//change status
							status= MyAbstractController.ENTRY_REVERT;
						}
					}
					else
					{
						status= MyAbstractController.ENTRY_REVERT;
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		paymentDao.diactivateByIdValue(payment_id,status);
		
	}

	@Override
	public void activateByIdValue(Long entityId) {
		try {
			Double transaction_value = (double) 0;
			Double cgst = (double) 0;
			Double igst = (double) 0;
			Double sgst = (double) 0;
			Double vat = (double) 0;
			Double vatcst = (double) 0;
			Double excise = (double) 0;
			Double state_comp_tax = (double) 0;
			Double amount = (double) 0;
			Double tds = (double) 0;			
			Payment payment = paymentDao.findOneWithAll(entityId);
			if (payment.getTds_amount() != null) {
				tds = payment.getTds_amount();
			}
			if (payment.getAmount() != null) {
				amount = payment.getAmount();
			}

			if (payment.getTransaction_value_head() != null && payment.getTransaction_value_head() > 0) {
				transaction_value = payment.getTransaction_value_head();
			}

			if (payment.getCGST_head() != null && payment.getCGST_head() > 0) {
				cgst = payment.getCGST_head();
			}
			if (payment.getIGST_head() != null && payment.getIGST_head() > 0) {
				igst = payment.getIGST_head();
			}
			if (payment.getSGST_head() != null && payment.getSGST_head() > 0) {
				sgst = payment.getSGST_head();
			}
			if (payment.getSCT_head() != null && payment.getSCT_head() > 0) {
				state_comp_tax = payment.getSCT_head();
			}
			if (payment.getTotal_vat() != null && payment.getTotal_vat() > 0) {
				vat = payment.getTotal_vat();
			}
			if (payment.getTotal_vatcst() != null && payment.getTotal_vatcst() > 0) {
				vatcst = payment.getTotal_vatcst();
			}
			if (payment.getTotal_excise() != null && payment.getTotal_excise() > 0) {
				excise = payment.getTotal_excise();
			}

			try {
				if (payment.getSupplier() != null) {
					supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSupplier().getSupplier_id(), (long) 4, (double) 0, ((double)amount +(double)tds), (long) 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (payment.getSubLedger() != null) {
				try {
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSubLedger().getSubledger_Id(), (long) 2, (double) 0, ((double)amount +(double)tds), (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(payment.getTds_amount()!=null)
			{
				SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", payment.getCompany().getCompany_id());
			    if (subledgerTds != null) {
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2, (double)payment.getTds_amount(), (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
			    }
			}
			
			/*if(payment.getSupplier_bill_no()!=null)
			{
				PurchaseEntry purchaseEntry = purchaseEntryDao.findOneWithAll(payment.getSupplier_bill_no().getPurchase_id());
			
			if(purchaseEntry.getPayments().size() <= 1) {
				SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", payment.getCompany().getCompany_id());
			    if (subledgerTds != null) {
					subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2, (double)payment.getTds_amount(), (double) 0, (long) 1);
					if (payment.getSupplier() != null) {
						supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSupplier().getSupplier_id(), (long) 4,  (double)payment.getTds_amount(), (double) 0,(long) 0);
					}		
			    }
			}
		   }*/

			if (payment.getGst_applied() == false) {
				if (payment.getPayment_type() == 1) {

					SubLedger subledgercgst = subledgerDao.findOne("Cash In Hand", payment.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)amount, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
					}
				} else {
					if (payment.getBank() != null) {
						bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getBank().getBank_id(), (long) 3, (double)amount, (double) 0, (long) 1);
					}
				}
			} else if (payment.getGst_applied() == true) {
				if (payment.getPayment_type() == 1) {

					if (transaction_value > 0) {
						SubLedger subledgercash = subledgerDao.findOne("Cash In Hand", payment.getCompany().getCompany_id());
						if (subledgercash != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, (double)amount, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (cgst != null && cgst > 0) {

						SubLedger subledgercgst = subledgerDao.findOne("Input CGST", payment.getCompany().getCompany_id());
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (sgst != null && sgst > 0) {

						SubLedger subledgersgst = subledgerDao.findOne("Input SGST", payment.getCompany().getCompany_id());
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (igst != null && igst > 0) {

						SubLedger subledgerigst = subledgerDao.findOne("Input IGST", payment.getCompany().getCompany_id());
						if (subledgerigst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}

					if (state_comp_tax != null && state_comp_tax > 0) {

						SubLedger subledgercess = subledgerDao.findOne("Input CESS", payment.getCompany().getCompany_id());
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (vat != null && vat > 0) {

						SubLedger subledgervat = subledgerDao.findOne("Input VAT", payment.getCompany().getCompany_id());
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (vatcst != null && vatcst > 0) {

						SubLedger subledgercst = subledgerDao.findOne("Input CST", payment.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (excise != null && excise > 0) {

						SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", payment.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
				} else {

					if (payment.getBank() != null) {
						bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getBank().getBank_id(), (long) 3, (double)transaction_value, (double) 0, (long) 1);
					}

					if (cgst != null && cgst > 0) {

						SubLedger subledgercgst = subledgerDao.findOne("Input CGST", payment.getCompany().getCompany_id());
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (sgst != null && sgst > 0) {

						SubLedger subledgersgst = subledgerDao.findOne("Input SGST", payment.getCompany().getCompany_id());
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (igst != null && igst > 0) {

						SubLedger subledgerigst = subledgerDao.findOne("Input IGST", payment.getCompany().getCompany_id());
						if (subledgerigst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}

					if (state_comp_tax != null && state_comp_tax > 0) {

						SubLedger subledgercess = subledgerDao.findOne("Input CESS", payment.getCompany().getCompany_id());
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (vat != null && vat > 0) {

						SubLedger subledgervat = subledgerDao.findOne("Input VAT", payment.getCompany().getCompany_id());
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (vatcst != null && vatcst > 0) {

						SubLedger subledgercst = subledgerDao.findOne("Input CST", payment.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (excise != null && excise > 0) {

						SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", payment.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double) 0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		paymentDao.activateByIdValue(entityId);
	}

	@Override
	public Double findpaidtds(Long purchase_id) {
		return paymentDao.findpaidtds(purchase_id);
	}

	@Override
	public void cahngeStatusOfPaymentThroughExcel(Payment entry) {
		paymentDao.cahngeStatusOfPaymentThroughExcel(entry);		
	}

	@Override
	public List<Payment> getPaymentListForLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId,Long bank_id) {
		// TODO Auto-generated method stub
		return paymentDao.getPaymentListForLedgerReport(from_date, to_date, companyId,bank_id);
	}

	@Override
	public void deletePayment(Long entityId) {
	
			try {
				Double transaction_value = (double) 0;
				Double cgst = (double) 0;
				Double igst = (double) 0;
				Double sgst = (double) 0;
				Double vat = (double) 0;
				Double vatcst = (double) 0;
				Double excise = (double) 0;
				Double state_comp_tax = (double) 0;
				Double amount = (double) 0;
				Double tds = (double) 0;
				Payment payment = paymentDao.findOneWithAll(entityId);
				if (payment.getAmount() != null) {
					amount = payment.getAmount();
				}
				if (payment.getTds_amount() != null) {
					tds = payment.getTds_amount();
				}
				if (payment.getTransaction_value_head() != null && payment.getTransaction_value_head() > 0) {
					transaction_value = payment.getTransaction_value_head();
				}
				if (payment.getCGST_head() != null && payment.getCGST_head() > 0) {
					cgst = payment.getCGST_head();
				}
				if (payment.getIGST_head() != null && payment.getIGST_head() > 0) {
					igst = payment.getIGST_head();
				}
				if (payment.getSGST_head() != null && payment.getSGST_head() > 0) {
					sgst = payment.getSGST_head();
				}
				if (payment.getSCT_head() != null && payment.getSCT_head() > 0) {
					state_comp_tax = payment.getSCT_head();
				}
				if (payment.getTotal_vat() != null && payment.getTotal_vat() > 0) {
					vat = payment.getTotal_vat();
				}
				if (payment.getTotal_vatcst() != null && payment.getTotal_vatcst() > 0) {
					vatcst = payment.getTotal_vatcst();
				}
				if (payment.getTotal_excise() != null && payment.getTotal_excise() > 0) {
					excise = payment.getTotal_excise();
				}

				try {
					if (payment.getSupplier() != null) {
						if(payment.getAdvance_payment()!=null && payment.getAdvance_payment()==true) {
						supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSupplier().getSupplier_id(), (long) 4, (double) 0, ((double)amount + tds), (long) 0);
						}
						else
						{
							supplierService.addsuppliertrsansactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSupplier().getSupplier_id(), (long) 4, (double) 0, (double)amount, (long) 0);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (payment.getSubLedger() != null) {
					try {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getSubLedger().getSubledger_Id(), (long) 2, (double) 0, ((double)amount + tds), (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}		
			
				if(payment.getTds_paid()!=null && payment.getTds_paid()==true && payment.getTds_amount()!=null)
				{
					SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", payment.getCompany().getCompany_id());
				    if (subledgerTds != null) {
						subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerTds.getSubledger_Id(), (long) 2, (double)tds, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
				    }
				}
				
				if(payment.getGst_applied()!=null && payment.getPayment_type() !=null)
				{
				if (payment.getGst_applied() == false) {
					if (payment.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {
						SubLedger subledgercgst = subledgerDao.findOne("Cash In Hand", payment.getCompany().getCompany_id());
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)amount, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					} else {
						if (payment.getBank() != null) {
							bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), payment.getBank().getBank_id(), (long) 3, (double)amount, (double) 0, (long) 0);
						}
					}
				} else if (payment.getGst_applied() == true) {
					if (payment.getPayment_type() == MyAbstractController.PAYMENT_TYPE_CASH) {

						if (transaction_value > 0) {
							SubLedger subledgercash = subledgerDao.findOne("Cash In Hand", payment.getCompany().getCompany_id());
							if (subledgercash != null) {
								subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, (double)transaction_value, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
							}
						}
					} else {
						if (payment.getBank() != null) {
							bankService.addbanktransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(),
									payment.getBank().getBank_id(), (long) 3, (double)transaction_value, (double) 0, (long) 0);
						}
					}
					if (cgst != null && cgst > 0) {
						SubLedger subledgercgst = subledgerDao.findOne("Input CGST", payment.getCompany().getCompany_id());
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (sgst != null && sgst > 0) {
						SubLedger subledgersgst = subledgerDao.findOne("Input SGST", payment.getCompany().getCompany_id());
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (igst != null && igst > 0) {
						SubLedger subledgerigst = subledgerDao.findOne("Input IGST", payment.getCompany().getCompany_id());
						if (subledgerigst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (state_comp_tax != null && state_comp_tax > 0) {
						SubLedger subledgercess = subledgerDao.findOne("Input CESS", payment.getCompany().getCompany_id());
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (vat != null && vat > 0) {
						SubLedger subledgervat = subledgerDao.findOne("Input VAT", payment.getCompany().getCompany_id());
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (vatcst != null && vatcst > 0) {
						SubLedger subledgercst = subledgerDao.findOne("Input CST", payment.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
					if (excise != null && excise > 0) {
						SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", payment.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double) 0, (long) 0,null,null,null,payment,null,null,null,null,null,null,null,null);
						}
					}
				}
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		 paymentDao.deletePayment(entityId);
	}

	@Override
	public List<Payment> CashPaymentOfMoreThanRS10000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId) {
		// TODO Auto-generated method stub
		return paymentDao.CashPaymentOfMoreThanRS10000AndAbove(from_date,to_date,companyId);
	}

	@Override
	public List<Payment> supplierPaymentHavingUnadjusted(LocalDate from_date, LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		return paymentDao.supplierPaymentHavingUnadjusted(from_date, to_date, companyId);
	}

	@Override
	public List<Payment> getPaymentForLedgerReport(LocalDate from_date, LocalDate to_date, Long suppilerId,
			Long companyId) {
		// TODO Auto-generated method stub
		return paymentDao.getPaymentForLedgerReport(from_date, to_date, suppilerId, companyId);
	}

	@Override
	public List<Payment> supplirPaymentHavingGST0(LocalDate fromDate, LocalDate toDate, Long company_id) {
		// TODO Auto-generated method stub
		return paymentDao.supplirPaymentHavingGST0(fromDate, toDate, company_id);
	}
	@Override
	public List<Payment> supplierPaymentWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,
			LocalDate to_date, Long companyId) {
	
		return paymentDao.supplierPaymentWithSubledgerTransactionsRs300000AndAbove(from_date, to_date, companyId);
	}
	@Override
	public List<Payment> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate fromDate, LocalDate toDate,
			Long company_id) {
	
		return paymentDao.supplierHavingGSTApplicbleAsNOAndExceedingZERO(fromDate, toDate, company_id);
	}

	@Override
	public Payment isExcelVocherNumberExist(String vocherNo, Long companyId) {
		// TODO Auto-generated method stub
		return paymentDao.isExcelVocherNumberExist(vocherNo, companyId);
	}

	@Override
	public List<Payment> getAllPaymentsAgainstAdvancePayment(Long advancePaymentId) {
		// TODO Auto-generated method stub.
		return paymentDao.getAllPaymentsAgainstAdvancePayment(advancePaymentId);
	}
	
	@Override
	public List<Payment> getAllAdvancePaymentsAgainstSupplier(Long supplierId, Long companyId) {
		// TODO Auto-generated method stub
		return paymentDao.getAllAdvancePaymentsAgainstSupplier(supplierId, companyId);
	}

	@Override
	public List<Payment> getAllOpeningBalanceAgainstPaymentForPeriod(Long supplierId, Long companyId,
			LocalDate todate) {
		// TODO Auto-generated method stub
		return paymentDao.getAllOpeningBalanceAgainstPaymentForPeriod( supplierId, companyId,todate);
	}

	@Override
	public List<Payment> getAllAdvancePaymentsAgainstSupplierForPeriod(Long supplierId, Long companyId,
			LocalDate todate) {
		// TODO Auto-generated method stub
	return paymentDao.getAllAdvancePaymentsAgainstSupplierForPeriod(supplierId, companyId, todate);
	}

	@Override
	public List<Payment> getPaymentForLedgerReport1(LocalDate to_date, Long suppilerId, Long companyId) {
		// TODO Auto-generated method stub
		return paymentDao.getPaymentForLedgerReport1( to_date, suppilerId, companyId);
	}
}