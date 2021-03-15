/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

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
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IDebitNoteDAO;
import com.fasset.dao.interfaces.IPaymentDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.IPurchaseEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.ISupplierDAO;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.Product;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.PurchaseEntryProductEntityClass;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.BillsReceivableForm;
import com.fasset.form.ExempListForm;
import com.fasset.form.HSNReportForm;
import com.fasset.form.ProductInformation;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */

@Service
@Transactional
public class PurchaseEntryServiceImpl implements IPurchaseEntryService {

	@Autowired
	private IPurchaseEntryDAO pururchaseEntryDAO;

	@Autowired
	private IProductDAO productDao;

	@Autowired
	private ISupplierDAO supplierDao;

	@Autowired
	private ISubLedgerDAO subLedgerDAO;

	@Autowired
	private IPaymentDAO paymentDao;
	@Autowired
	private IDebitNoteDAO debitNoteDao;
	@Autowired
	private IAccountingYearDAO accountYearDAO;

	@Autowired
	ISubLedgerService subledgerService;

	@Autowired
	ISuplliersService supplierService;

	@Autowired
	private IBankDAO bankDao;
	
	@Autowired
	private ICommonService commonService;

	@Autowired
	private ICompanyDAO companyDao;
	
	@Autowired
	private IOpeningBalancesService openingbalances;
	
	@Override
	public void add(PurchaseEntry entity) throws MyWebException {

	}

	@Override
	public void update(PurchaseEntry sup) throws MyWebException {

		if (sup.getProductInformationList() != null && sup.getProductInformationList() != "") {
			List<ProductInformation> informationList = new ArrayList<ProductInformation>();
			Set<Product> products = new HashSet<Product>();
			Product product = new Product();
			JSONArray jsonArray = new JSONArray(sup.getProductInformationList());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				ProductInformation information = new ProductInformation();
				information.setProductId(jsonObject.getString("productId"));

				try {
					product = productDao.findOne(Long.parseLong(information.getProductId()));
				} catch (NumberFormatException | MyWebException e) {

					e.printStackTrace();
				}
				information.setProductname(product.getProduct_name());
				information.setQuantity(jsonObject.getString("quantity"));
				information.setUnit(jsonObject.getString("unit"));
				information.setHsncode(jsonObject.getString("hsncode"));
				information.setRate(jsonObject.getString("rate"));
				information.setLabourCharge(jsonObject.getString("labourCharge"));
				information.setFreightCharges(jsonObject.getString("freightCharges"));
				information.setOthers(jsonObject.getString("others"));
				information.setCGST(jsonObject.getString("CGST"));
				information.setSGST(jsonObject.getString("SGST"));
				information.setIGST(jsonObject.getString("IGST"));
				information.setSCT(jsonObject.getString("SCT"));
				information.setDiscount(jsonObject.getString("discount"));
				//vat
				 information.setIs_gst(jsonObject.getString("is_gst"));
				 information.setVAT(jsonObject.getString("VAT"));
				 information.setVATCST(jsonObject.getString("VATCST"));
				 information.setExcise(jsonObject.getString("Excise"));
				information.setTransaction_amount(jsonObject.getString("transaction_amount"));
				informationList.add(information);
			}
			sup.setInformationList(informationList);
			for (int i = 0; i < informationList.size(); i++) {
				ProductInformation information = new ProductInformation();
				information = informationList.get(i);
				try {
					Product product1 = productDao.findOne(Long.parseLong(information.getProductId()));
					products.add(product1);
				} catch (NumberFormatException | MyWebException e) {
					e.printStackTrace();
				}
			}
			sup.setProducts(products);

		}
		try {
			if (sup.getSubledger_Id() != null && sup.getSubledger_Id() > 0) {
				sup.setSubledger(subLedgerDAO.findOne(sup.getSubledger_Id()));
			}
			if (sup.getSupplier_id() != null && sup.getSupplier_id() > 0) {
				sup.setSupplier(supplierDao.findOne(sup.getSupplier_id()));
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}

		PurchaseEntry entity = pururchaseEntryDAO.findOneWithSuppilerAndSublegder(sup.getPurchase_id());
		if(entity.getAdvpayment()!=null)
		{
			sup.setAdvpayment(entity.getAdvpayment());
			
		}
		if (sup.getAdvpayment() != null) {
			try {
				Payment newpayment = paymentDao.findOne(sup.getAdvpayment().getPayment_id());
				double tds=0;
				if(sup.getRound_off()>newpayment.getAmount())
				{
					
					if(newpayment.getAdvpayment()!=null)
					{
						Payment orignalpayment = newpayment.getAdvpayment();
						
                         double originalAmount= 0.0d;
						
                         if(orignalpayment.getIs_extraAdvanceReceived()!=null && orignalpayment.getIs_extraAdvanceReceived().equals(true) && orignalpayment.getSupplier_bill_no()!=null)
						{
							double amount = 0.0d;
							amount = orignalpayment.getAmount();
							originalAmount = orignalpayment.getAmount();
							List<Payment> paymentList = paymentDao.getAllPaymentsAgainstAdvancePayment(orignalpayment.getPayment_id());
							for(Payment paymentAgainstAdvance : paymentList)
							{
								amount=amount-paymentAgainstAdvance.getAmount();
							}
							orignalpayment.setAmount(amount-orignalpayment.getSupplier_bill_no().getRound_off());
							
							
						}
						
												
						orignalpayment.setAmount(orignalpayment.getAmount()+newpayment.getAmount());
						orignalpayment.setTds_amount(orignalpayment.getTds_amount()+newpayment.getTds_amount());
						
						newpayment.setAmount(sup.getRound_off());
						if(newpayment.getTds_paid()==true)
						{
							tds=(newpayment.getAmount()*orignalpayment.getTds_amount())/orignalpayment.getAmount();
							newpayment.setTds_amount(tds);
							
						}
						else
						{
							newpayment.setTds_paid(false);
							newpayment.setTds_amount((double) 0);
						}
						
						
						 if(orignalpayment.getAmount().equals(sup.getRound_off()))//both payment are equal so delete previous payment. and add reference of original payment to purchase entry.
						    {
							  if(orignalpayment.getIs_extraAdvanceReceived()!=null && orignalpayment.getIs_extraAdvanceReceived().equals(true) && orignalpayment.getSupplier_bill_no()!=null)
								{
								  paymentDao.update(newpayment);
								  orignalpayment.setAmount(originalAmount);
								  orignalpayment.setEntry_status(MyAbstractController.ENTRY_NIL);
								  paymentDao.update(orignalpayment);
								}
							   if(orignalpayment.getIs_extraAdvanceReceived()==null)
								{
							    paymentDao.deletePaymentAginstadvance(newpayment.getPayment_id());
							    sup.setEntry_status(MyAbstractController.ENTRY_NIL);
							    sup.setAdvpayment(orignalpayment);
							    orignalpayment.setSupplier_bill_no(entity);
							    orignalpayment.setEntry_status(MyAbstractController.ENTRY_NIL);
								paymentDao.update(orignalpayment);
								}
							  
							}
						 else if(orignalpayment.getAmount()<sup.getRound_off())//amount of purchase entry exceeding than original payment so delete previous payment.and add reference of original payment to purchase entry.
							{
							  if(orignalpayment.getIs_extraAdvanceReceived()!=null && orignalpayment.getIs_extraAdvanceReceived().equals(true) && orignalpayment.getSupplier_bill_no()!=null)
								{
								  paymentDao.update(newpayment);
								  orignalpayment.setAmount(originalAmount);
								  orignalpayment.setEntry_status(MyAbstractController.ENTRY_NIL);
								  paymentDao.update(orignalpayment);
								  sup.setEntry_status(MyAbstractController.ENTRY_PENDING);
								}
							  if(orignalpayment.getIs_extraAdvanceReceived()==null)
								{
							    paymentDao.deletePaymentAginstadvance(newpayment.getPayment_id());
							    sup.setEntry_status(MyAbstractController.ENTRY_PENDING);
							    sup.setAdvpayment(orignalpayment);
							    orignalpayment.setSupplier_bill_no(entity);
							    orignalpayment.setEntry_status(MyAbstractController.ENTRY_NIL);
								paymentDao.update(orignalpayment);
								}
							  
							}
						    else if(orignalpayment.getAmount()>sup.getRound_off())
						     {
						    	if(orignalpayment.getIs_extraAdvanceReceived()==null)
								{
						    	orignalpayment.setAmount(orignalpayment.getAmount()-newpayment.getAmount());
								orignalpayment.setTds_amount(orignalpayment.getTds_amount()-newpayment.getTds_amount());
						    	paymentDao.update(orignalpayment);
								}
						    	paymentDao.update(newpayment);
					    	    if(orignalpayment.getIs_extraAdvanceReceived()!=null && orignalpayment.getIs_extraAdvanceReceived().equals(true) && orignalpayment.getSupplier_bill_no()!=null)
								{
				    	    	  orignalpayment.setAmount(originalAmount);
								  orignalpayment.setEntry_status(MyAbstractController.ENTRY_PENDING);
								  paymentDao.update(orignalpayment);
								}
						     }
					}
				}	
							

			} catch (MyWebException e) {
				e.printStackTrace();
			}
		}
		
		if(entity.getSupplier()!=null && entity.getSubledger()!=null && sup.getSupplier_id()!=null && sup.getSubledger_Id()!=null)
		{
		if ((entity.getRound_off() != sup.getRound_off())
				|| ((long)entity.getSupplier().getSupplier_id() != (long)sup.getSupplier_id())
				|| ((long)entity.getSubledger().getSubledger_Id() != (long)sup.getSubledger_Id())) {

			if (((long)entity.getSupplier().getSupplier_id() != (long)sup.getSupplier_id())&&(entity.getRound_off() != sup.getRound_off())) {

				if (entity.getSupplier()!= null) {
					supplierService.addsuppliertrsansactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							entity.getSupplier().getSupplier_id(), (long) 4, (double)entity.getRound_off(), (double) 0,
							(long) 0);
				}
				if (entity.getSubledger()!= null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							entity.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTransaction_value(),
							(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
				}
				if (sup.getSupplier_id() != null) {
					supplierService.addsuppliertrsansactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							sup.getSupplier_id(), (long) 4, (double)sup.getRound_off(), (double) 0, (long) 1);
				}
				if (sup.getSubledger_Id() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							sup.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTransaction_value(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
				}
				try {
					if (sup.getAdvpayment()==null && sup.getTds_amount() != null) {
						SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", sup.getCompany().getCompany_id());
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getSupplier_bill_date(),entity.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)entity.getTds_amount(), (double) 0, (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)sup.getTds_amount(), (double) 0, (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
					}
					
					if ((sup.getCgst() != null && entity.getCgst() != null)
							&& (sup.getCgst() > 0 && entity.getCgst() > 0) && (sup.getCgst() != entity.getCgst())) {
						
						SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",entity.getCompany().getCompany_id());
						
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getCgst(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getCgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getCgst() != null && sup.getCgst() > 0)
							&& (entity.getCgst() != null && entity.getCgst() == 0)) 
					{
						SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",
								entity.getCompany().getCompany_id());
						
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getCgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}

					if ((sup.getSgst() != null && entity.getSgst() != null)
							&& (sup.getSgst() > 0 && entity.getSgst() > 0) && (sup.getSgst() != entity.getSgst())) {
						
						SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
								entity.getCompany().getCompany_id());
						
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getSgst(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getSgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getSgst() != null && sup.getSgst() > 0)
							&& (entity.getSgst() != null && entity.getSgst() == 0)) {
						
						SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
								entity.getCompany().getCompany_id());
						
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getSgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((sup.getIgst() != null && entity.getIgst() != null)
							&& (sup.getIgst() > 0 && entity.getIgst() > 0) && (sup.getIgst() != entity.getIgst())) {
						
						SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
								entity.getCompany().getCompany_id());
						
						if (subledgerigst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getIgst(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double) 0,(double)sup.getIgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getIgst() != null && sup.getIgst() > 0)
							&& (entity.getIgst() != null && entity.getIgst() == 0)) {
						
						SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
								entity.getCompany().getCompany_id());
						
						if (subledgerigst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getIgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((sup.getState_compansation_tax() != null && entity.getState_compansation_tax() != null)
							&& (sup.getState_compansation_tax() > 0 && entity.getState_compansation_tax() > 0)
							&& (sup.getState_compansation_tax() != entity.getState_compansation_tax())) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
								entity.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double) 0,
									(double)entity.getState_compansation_tax(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double) 0,
									(double)sup.getState_compansation_tax(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getState_compansation_tax() != null && sup.getState_compansation_tax() > 0)
							&& (entity.getState_compansation_tax() != null
									&& entity.getState_compansation_tax() == 0)) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
								entity.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double) 0,
									(double)sup.getState_compansation_tax(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((sup.getTotal_vat() != null && entity.getTotal_vat() != null)
							&& (sup.getTotal_vat() > 0 && entity.getTotal_vat() > 0)
							&& (sup.getTotal_vat() != entity.getTotal_vat())) {
						
						SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", entity.getCompany().getCompany_id());
						if (subledgervat != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_vat(),
									(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vat(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} 
					else if ((sup.getTotal_vat() != null && sup.getTotal_vat() > 0)
							&& (entity.getTotal_vat() != null && entity.getTotal_vat() == 0)) {
						
						SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", entity.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vat(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						
						}
					}
					if ((sup.getTotal_vatcst() != null && entity.getTotal_vatcst() != null)
							&& (sup.getTotal_vatcst() > 0 && entity.getTotal_vatcst() > 0)
							&& (sup.getTotal_vatcst() != entity.getTotal_vatcst())) {
						
						SubLedger subledgercst = subLedgerDAO.findOne("Input CST", entity.getCompany().getCompany_id());
						
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_vatcst(),
									(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vatcst(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getTotal_vatcst() != null && sup.getTotal_vatcst() > 0)
							&& (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() == 0)) {
						
						SubLedger subledgercst = subLedgerDAO.findOne("Input CST", entity.getCompany().getCompany_id());
						
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vatcst(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((sup.getTotal_excise() != null && entity.getTotal_excise() != null)
							&& (sup.getTotal_excise() > 0 && entity.getTotal_excise() > 0)
							&& (sup.getTotal_excise() != entity.getTotal_excise())) {
						
						SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
								entity.getCompany().getCompany_id());
						
						if (subledgerexcise != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_excise(),
									(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_excise(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getTotal_excise() != null && sup.getTotal_excise() > 0)
							&& (entity.getTotal_excise() != null && entity.getTotal_excise() == 0)) {
						
						SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
								entity.getCompany().getCompany_id());
						
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_excise(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (((long)entity.getSubledger().getSubledger_Id() != (long)sup.getSubledger_Id())&&(entity.getRound_off() != sup.getRound_off())) {
				
				if (entity.getSubledger()!= null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							entity.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTransaction_value(),
							(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
				}

				if (sup.getSubledger_Id() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							sup.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTransaction_value(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
				}
				try {
					if (sup.getAdvpayment()==null && sup.getTds_amount() != null) {
						SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", sup.getCompany().getCompany_id());
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getSupplier_bill_date(),entity.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)entity.getTds_amount(), (double) 0, (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)sup.getTds_amount(), (double) 0, (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
					}
					if ((sup.getCgst() != null && entity.getCgst() != null)
							&& (sup.getCgst() > 0 && entity.getCgst() > 0) && (sup.getCgst() != entity.getCgst())) {
						
						SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",
								entity.getCompany().getCompany_id());
						
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getCgst(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getCgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getCgst() != null && sup.getCgst() > 0)
							&& (entity.getCgst() != null && entity.getCgst() == 0)) 
					{
						SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",
								entity.getCompany().getCompany_id());
						
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getCgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}

					if ((sup.getSgst() != null && entity.getSgst() != null)
							&& (sup.getSgst() > 0 && entity.getSgst() > 0) && (sup.getSgst() != entity.getSgst())) {
						
						SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
								entity.getCompany().getCompany_id());
						
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getSgst(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getSgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getSgst() != null && sup.getSgst() > 0)
							&& (entity.getSgst() != null && entity.getSgst() == 0)) {
						
						SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
								entity.getCompany().getCompany_id());
						
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getSgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((sup.getIgst() != null && entity.getIgst() != null)
							&& (sup.getIgst() > 0 && entity.getIgst() > 0) && (sup.getIgst() != entity.getIgst())) {
						
						SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
								entity.getCompany().getCompany_id());
						
						if (subledgerigst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getIgst(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double) 0,(double)sup.getIgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getIgst() != null && sup.getIgst() > 0)
							&& (entity.getIgst() != null && entity.getIgst() == 0)) {
						
						SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
								entity.getCompany().getCompany_id());
						
						if (subledgerigst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getIgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((sup.getState_compansation_tax() != null && entity.getState_compansation_tax() != null)
							&& (sup.getState_compansation_tax() > 0 && entity.getState_compansation_tax() > 0)
							&& (sup.getState_compansation_tax() != entity.getState_compansation_tax())) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
								entity.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double) 0,
									(double)entity.getState_compansation_tax(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double) 0,
									(double)sup.getState_compansation_tax(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getState_compansation_tax() != null && sup.getState_compansation_tax() > 0)
							&& (entity.getState_compansation_tax() != null
									&& entity.getState_compansation_tax() == 0)) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
								entity.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double) 0,
									(double)sup.getState_compansation_tax(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((sup.getTotal_vat() != null && entity.getTotal_vat() != null)
							&& (sup.getTotal_vat() > 0 && entity.getTotal_vat() > 0)
							&& (sup.getTotal_vat() != entity.getTotal_vat())) {
						
						SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", entity.getCompany().getCompany_id());
						if (subledgervat != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_vat(),
									(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vat(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} 
					else if ((sup.getTotal_vat() != null && sup.getTotal_vat() > 0)
							&& (entity.getTotal_vat() != null && entity.getTotal_vat() == 0)) {
						
						SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", entity.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vat(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						
						}
					}
					if ((sup.getTotal_vatcst() != null && entity.getTotal_vatcst() != null)
							&& (sup.getTotal_vatcst() > 0 && entity.getTotal_vatcst() > 0)
							&& (sup.getTotal_vatcst() != entity.getTotal_vatcst())) {
						
						SubLedger subledgercst = subLedgerDAO.findOne("Input CST", entity.getCompany().getCompany_id());
						
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_vatcst(),
									(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vatcst(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getTotal_vatcst() != null && sup.getTotal_vatcst() > 0)
							&& (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() == 0)) {
						
						SubLedger subledgercst = subLedgerDAO.findOne("Input CST", entity.getCompany().getCompany_id());
						
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vatcst(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((sup.getTotal_excise() != null && entity.getTotal_excise() != null)
							&& (sup.getTotal_excise() > 0 && entity.getTotal_excise() > 0)
							&& (sup.getTotal_excise() != entity.getTotal_excise())) {
						
						SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
								entity.getCompany().getCompany_id());
						
						if (subledgerexcise != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_excise(),
									(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_excise(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getTotal_excise() != null && sup.getTotal_excise() > 0)
							&& (entity.getTotal_excise() != null && entity.getTotal_excise() == 0)) {
						
						SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
								entity.getCompany().getCompany_id());
						
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_excise(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (((long)entity.getSupplier().getSupplier_id() != (long)sup.getSupplier_id())) {
				
				if (entity.getSupplier() != null) {
					supplierService.addsuppliertrsansactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							entity.getSupplier().getSupplier_id(), (long) 4, (double)entity.getRound_off(), (double) 0,
							(long) 0);
				}
				if (entity.getSubledger() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							entity.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTransaction_value(),
							(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
				}
				if (sup.getSupplier_id() != null) {
					supplierService.addsuppliertrsansactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							sup.getSupplier_id(), (long) 4, (double)sup.getRound_off(), (double) 0, (long) 1);
				}

				if (sup.getSubledger_Id() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							sup.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTransaction_value(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
				}
			} else if (((long)entity.getSubledger().getSubledger_Id() != (long)sup.getSubledger_Id())) {
				if (entity.getSubledger() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							entity.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTransaction_value(),
							(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
				}

				if (sup.getSubledger_Id() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							sup.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTransaction_value(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
				}
			} else {
				if (entity.getSupplier().getSupplier_id() != null) {
					supplierService.addsuppliertrsansactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							sup.getSupplier_id(), (long) 4, (double)entity.getRound_off(), (double) 0, (long) 0);
					supplierService.addsuppliertrsansactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							sup.getSupplier_id(), (long) 4, (double)sup.getRound_off(), (double) 0, (long) 1);
				}
				if (entity.getSubledger().getSubledger_Id() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							sup.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTransaction_value(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
							sup.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTransaction_value(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
				}
				try {
					if (sup.getAdvpayment()==null && sup.getTds_amount() != null) {
						SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", sup.getCompany().getCompany_id());
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getSupplier_bill_date(),entity.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)entity.getTds_amount(), (double) 0, (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)sup.getTds_amount(), (double) 0, (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
					}
					if ((sup.getCgst() != null && entity.getCgst() != null)
							&& (sup.getCgst() > 0 && entity.getCgst() > 0) && (sup.getCgst() != entity.getCgst())) {
						
						SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",
								entity.getCompany().getCompany_id());
						
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getCgst(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getCgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getCgst() != null && sup.getCgst() > 0)
							&& (entity.getCgst() != null && entity.getCgst() == 0)) 
					{
						SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",
								entity.getCompany().getCompany_id());
						
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getCgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}

					if ((sup.getSgst() != null && entity.getSgst() != null)
							&& (sup.getSgst() > 0 && entity.getSgst() > 0) && (sup.getSgst() != entity.getSgst())) {
						
						SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
								entity.getCompany().getCompany_id());
						
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getSgst(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getSgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getSgst() != null && sup.getSgst() > 0)
							&& (entity.getSgst() != null && entity.getSgst() == 0)) {
						
						SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
								entity.getCompany().getCompany_id());
						
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getSgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((sup.getIgst() != null && entity.getIgst() != null)
							&& (sup.getIgst() > 0 && entity.getIgst() > 0) && (sup.getIgst() != entity.getIgst())) {
						
						SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
								entity.getCompany().getCompany_id());
						
						if (subledgerigst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getIgst(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double) 0,(double)sup.getIgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getIgst() != null && sup.getIgst() > 0)
							&& (entity.getIgst() != null && entity.getIgst() == 0)) {
						
						SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
								entity.getCompany().getCompany_id());
						
						if (subledgerigst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getIgst(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((sup.getState_compansation_tax() != null && entity.getState_compansation_tax() != null)
							&& (sup.getState_compansation_tax() > 0 && entity.getState_compansation_tax() > 0)
							&& (sup.getState_compansation_tax() != entity.getState_compansation_tax())) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
								entity.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double) 0,
									(double)entity.getState_compansation_tax(), (long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double) 0,
									(double)sup.getState_compansation_tax(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getState_compansation_tax() != null && sup.getState_compansation_tax() > 0)
							&& (entity.getState_compansation_tax() != null
									&& entity.getState_compansation_tax() == 0)) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
								entity.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double) 0,
									(double)sup.getState_compansation_tax(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((sup.getTotal_vat() != null && entity.getTotal_vat() != null)
							&& (sup.getTotal_vat() > 0 && entity.getTotal_vat() > 0)
							&& (sup.getTotal_vat() != entity.getTotal_vat())) {
						
						SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", entity.getCompany().getCompany_id());
						if (subledgervat != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_vat(),
									(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vat(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} 
					else if ((sup.getTotal_vat() != null && sup.getTotal_vat() > 0)
							&& (entity.getTotal_vat() != null && entity.getTotal_vat() == 0)) {
						
						SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", entity.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vat(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						
						}
					}
					if ((sup.getTotal_vatcst() != null && entity.getTotal_vatcst() != null)
							&& (sup.getTotal_vatcst() > 0 && entity.getTotal_vatcst() > 0)
							&& (sup.getTotal_vatcst() != entity.getTotal_vatcst())) {
						
						SubLedger subledgercst = subLedgerDAO.findOne("Input CST", entity.getCompany().getCompany_id());
						
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_vatcst(),
									(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vatcst(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getTotal_vatcst() != null && sup.getTotal_vatcst() > 0)
							&& (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() == 0)) {
						
						SubLedger subledgercst = subLedgerDAO.findOne("Input CST", entity.getCompany().getCompany_id());
						
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vatcst(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((sup.getTotal_excise() != null && entity.getTotal_excise() != null)
							&& (sup.getTotal_excise() > 0 && entity.getTotal_excise() > 0)
							&& (sup.getTotal_excise() != entity.getTotal_excise())) {
						
						SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
								entity.getCompany().getCompany_id());
						
						if (subledgerexcise != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_excise(),
									(long) 0,null,null,entity,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_excise(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					} else if ((sup.getTotal_excise() != null && sup.getTotal_excise() > 0)
							&& (entity.getTotal_excise() != null && entity.getTotal_excise() == 0)) {
						
						SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
								entity.getCompany().getCompany_id());
						
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_excise(),
									(long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
		if(entity.getSubledger()==null)
		{
			if (sup.getSubledger_Id() != null) {
				try {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(), sup.getSubledger_Id(),
							(long) 2, (double) 0,(double) sup.getTransaction_value(), (long) 1,null,null,entity,null,null,null,null,null,null,null,null,null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}		
		pururchaseEntryDAO.update(sup);
	}

	@Override
	public List<PurchaseEntry> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PurchaseEntry getById(Long id) throws MyWebException {
		return pururchaseEntryDAO.findOne(id);
	}

	@Override
	public PurchaseEntry getById(String id) throws MyWebException {
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
	public void remove(PurchaseEntry entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(PurchaseEntry entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public Company findCompany(Long user_id) {
		return pururchaseEntryDAO.findCompanyDao(user_id);
	}

	@Override
	public List<Suppliers> findAllSuppliers(Long company_id) {

		return null;
	}

	@Override
	public Long savePurchaseEntry(PurchaseEntry sup) {

		List<ProductInformation> informationList = new ArrayList<ProductInformation>();
		Set<Product> products = new HashSet<Product>();
		Product product = new Product();
		JSONArray jsonArray = new JSONArray(sup.getProductInformationList());
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ProductInformation information = new ProductInformation();
			information.setProductId(jsonObject.getString("productId"));
			try {
				product = productDao.findOne(Long.parseLong(information.getProductId()));
			} catch (NumberFormatException | MyWebException e) {
				e.printStackTrace();
			}
			information.setProductname(product.getProduct_name());
			information.setQuantity(jsonObject.getString("quantity"));
			information.setUnit(jsonObject.getString("unit"));
			information.setHsncode(jsonObject.getString("hsncode"));
			information.setRate(jsonObject.getString("rate"));
			information.setLabourCharge(jsonObject.getString("labourCharge"));
			information.setFreightCharges(jsonObject.getString("freightCharges"));
			information.setOthers(jsonObject.getString("others"));
			information.setCGST(jsonObject.getString("CGST"));
			information.setSGST(jsonObject.getString("SGST"));
			information.setIGST(jsonObject.getString("IGST"));
			information.setSCT(jsonObject.getString("SCT"));
			information.setDiscount(jsonObject.getString("discount"));
			//vat
			 information.setIs_gst(jsonObject.getString("is_gst"));
			 information.setVAT(jsonObject.getString("VAT"));
			 information.setVATCST(jsonObject.getString("VATCST"));
			 information.setExcise(jsonObject.getString("Excise"));
			information.setTransaction_amount(jsonObject.getString("transaction_amount"));
			informationList.add(information);
		}
		sup.setInformationList(informationList);

		for (int i = 0; i < informationList.size(); i++) {
			ProductInformation information = new ProductInformation();
			information = informationList.get(i);
			try {
				Product product1 = productDao.findOne(Long.parseLong(information.getProductId()));
				products.add(product1);
			} catch (NumberFormatException | MyWebException e) {
				e.printStackTrace();
			}
		}
		sup.setProducts(products);
		sup.setEntry_status(MyAbstractController.ENTRY_PENDING);

		try {
			sup.setAccountingYear(accountYearDAO.findOne(sup.getAccounting_year_id()));
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		sup.setCreated_date(new LocalDate());
		try {
			sup.setSubledger(subLedgerDAO.findOne(sup.getSubledger_Id()));
			sup.setSupplier(supplierDao.findOne(sup.getSupplier_id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}

		if (sup.getExport_type() == 0) {
			sup.setExport_type(null);
		}
		if (sup.getPaymentId() == null) {
			sup.setAgainst_advance_Payment(false);
		}
		if(sup.getAgainstOpeningbalanceId()!=null && sup.getAgainstOpeningbalanceId().equals((long)1))
		{
			sup.setAgainstOpeningBalnce(true);
		}
		else
		{
			sup.setAgainstOpeningBalnce(false);
		}
		Long id = pururchaseEntryDAO.savePurchaseEntryDao(sup);
		
		
		
		if (sup.getPaymentId() != null) {
			try {
				
				Payment payment = paymentDao.findOne(sup.getPaymentId());
				double originalAmount= 0.0d;
				if(payment.getIs_extraAdvanceReceived()!=null && payment.getIs_extraAdvanceReceived().equals(true) && payment.getSupplier_bill_no()!=null)
				{
                    double amount = 0.0d;
                   
					amount = payment.getAmount();
					originalAmount = payment.getAmount();
					List<Payment> paymentList = paymentDao.getAllPaymentsAgainstAdvancePayment(payment.getPayment_id());
					for(Payment paymentAgainstAdvance : paymentList)
					{
						amount=amount-paymentAgainstAdvance.getAmount();
					}
					payment.setAmount(amount-payment.getSupplier_bill_no().getRound_off());
					
					
				}
	
				if(payment.getAmount()>(sup.getRound_off()))
				{
					 
					Payment newpayment=new Payment();
					newpayment.setCompany(payment.getCompany());
					newpayment.setVoucher_no(commonService.getVoucherNumber("PV", MyAbstractController.VOUCHER_PAYMENT, payment.getDate(),payment.getAccountYearId(), payment.getCompany().getCompany_id()));						
					newpayment.setFlag(true);
					newpayment.setSupplier_bill_no(pururchaseEntryDAO.findOne(id));
					try {
						if (payment.getAccountYearId() != null && payment.getAccountYearId() > 0) {
							newpayment.setAccountingYear(accountYearDAO.findOne(payment.getAccountYearId()));
						}						
					} catch (MyWebException e1) {
						e1.printStackTrace();
					}
					newpayment.setPayment_type(payment.getPayment_type());
					if(payment.getPayment_type()!=1)
					{
						
						newpayment.setCheque_dd_no(payment.getCheque_dd_no());
						newpayment.setCheque_date(payment.getCheque_date());
						if (payment.getBank().getBank_id() > 0) {
							 
							newpayment.setBank(bankDao.findOne(payment.getBank().getBank_id()));
							}
					}
					newpayment.setGst_applied(false);
					newpayment.setCreated_date(new LocalDate());
					newpayment.setLocal_time(new LocalTime());
					newpayment.setDate(payment.getDate());
					newpayment.setTransaction_value_head((double) 0);
					newpayment.setCGST_head((double) 0);
					newpayment.setSGST_head((double) 0);
					newpayment.setIGST_head((double) 0);
					newpayment.setSCT_head((double) 0);
					newpayment.setTotal_vat((double) 0);
					newpayment.setTotal_vatcst((double) 0);
					newpayment.setTotal_excise((double) 0);
					newpayment.setEntry_status(MyAbstractController.ENTRY_NIL);
					newpayment.setAdvance_payment(true);
					newpayment.setAmount(sup.getRound_off());
					newpayment.setAdvpayment(payment);
					newpayment.setSupplier(supplierDao.findOne(sup.getSupplier_id()));	
					
					double tds=0;
					if(payment.getTds_paid()==true)
					{
						 
						newpayment.setTds_paid(true);
						tds=(newpayment.getAmount()*payment.getTds_amount())/payment.getAmount();
						newpayment.setTds_amount(tds);
					}
					else
					{
						
						newpayment.setTds_paid(false);
						newpayment.setTds_amount((double) 0);
					}
					
					Long pId= paymentDao.createforbill(newpayment);
					
					if(payment.getIs_extraAdvanceReceived()==null)
					{
						 
					payment.setAmount(payment.getAmount()-sup.getRound_off());	
				/*	if(payment.getGst_applied()==true)
					{
					payment.setTransaction_value_head(payment.getTransaction_value_head()-sup.getRound_off());
					}*/
					payment.setTds_amount(payment.getTds_amount()-tds);		
					}
					PurchaseEntry entry = pururchaseEntryDAO.findOne(id);
					entry.setAgainst_advance_Payment(true);
					entry.setEntry_status(MyAbstractController.ENTRY_NIL);
					entry.setAdvpayment(paymentDao.findOne(pId));
					pururchaseEntryDAO.updatePurchaseEntry(entry);
					
				}
				if(payment.getAmount().equals(sup.getRound_off()) || payment.getAmount()<(sup.getRound_off()))
				{
					if(payment.getIs_extraAdvanceReceived()!=null && payment.getIs_extraAdvanceReceived().equals(true) && payment.getSupplier_bill_no()!=null)
					{
						 
						Payment newpayment=new Payment();
						newpayment.setCompany(payment.getCompany());
						newpayment.setVoucher_no(commonService.getVoucherNumber("PV", MyAbstractController.VOUCHER_PAYMENT, payment.getDate(),payment.getAccountYearId(), payment.getCompany().getCompany_id()));						
						newpayment.setFlag(true);
						newpayment.setSupplier_bill_no(pururchaseEntryDAO.findOne(id));
						try {
							if (payment.getAccountYearId() != null && payment.getAccountYearId() > 0) {
								newpayment.setAccountingYear(accountYearDAO.findOne(payment.getAccountYearId()));
							}						
						} catch (MyWebException e1) {
							e1.printStackTrace();
						}
						newpayment.setPayment_type(payment.getPayment_type());
						if(payment.getPayment_type()!=1)
						{
							newpayment.setCheque_dd_no(payment.getCheque_dd_no());
							newpayment.setCheque_date(payment.getCheque_date());
							if (payment.getBank().getBank_id() > 0) {
								newpayment.setBank(bankDao.findOne(payment.getBank().getBank_id()));
								}
						}
						newpayment.setGst_applied(false);
						newpayment.setCreated_date(new LocalDate());
						newpayment.setLocal_time(new LocalTime());
						newpayment.setDate(payment.getDate());
						newpayment.setTransaction_value_head((double) 0);
						newpayment.setCGST_head((double) 0);
						newpayment.setSGST_head((double) 0);
						newpayment.setIGST_head((double) 0);
						newpayment.setSCT_head((double) 0);
						newpayment.setTotal_vat((double) 0);
						newpayment.setTotal_vatcst((double) 0);
						newpayment.setTotal_excise((double) 0);
						newpayment.setEntry_status(MyAbstractController.ENTRY_NIL);
						newpayment.setAdvance_payment(true);
						newpayment.setAmount(sup.getRound_off());
						newpayment.setAdvpayment(payment);
						newpayment.setSupplier(supplierDao.findOne(sup.getSupplier_id()));	
						
						double tds=0;
						if(payment.getTds_paid()==true)
						{
							newpayment.setTds_paid(true);
							tds=(newpayment.getAmount()*payment.getTds_amount())/payment.getAmount();
							newpayment.setTds_amount(tds);
						}
						else
						{
							newpayment.setTds_paid(false);
							newpayment.setTds_amount((double) 0);
						}
						
						Long pId= paymentDao.createforbill(newpayment);
						
						if(payment.getIs_extraAdvanceReceived()==null)
						{
						payment.setAmount(payment.getAmount()-sup.getRound_off());	
					/*	if(payment.getGst_applied()==true)
						{
						payment.setTransaction_value_head(payment.getTransaction_value_head()-sup.getRound_off());
						}*/
						payment.setTds_amount(payment.getTds_amount()-tds);		
						}
						PurchaseEntry entry = pururchaseEntryDAO.findOne(id);
						entry.setAgainst_advance_Payment(true);
						entry.setEntry_status(MyAbstractController.ENTRY_NIL);
						entry.setAdvpayment(paymentDao.findOne(pId));
						pururchaseEntryDAO.updatePurchaseEntry(entry);
						payment.setEntry_status(MyAbstractController.ENTRY_NIL);
					}
					if(payment.getIs_extraAdvanceReceived()==null)
					{
						PurchaseEntry entry = pururchaseEntryDAO.findOne(id);
						if(payment.getAmount().equals(sup.getRound_off()))
						{
						entry.setEntry_status(MyAbstractController.ENTRY_NIL);
						}
						if(payment.getAmount()<(sup.getRound_off()))
						{
						entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
						}
						entry.setAdvpayment(payment);
						entry.setAgainst_advance_Payment(true);
						pururchaseEntryDAO.updatePurchaseEntry(entry);
					    payment.setSupplier_bill_no(pururchaseEntryDAO.findOne(id));
					    payment.setEntry_status(MyAbstractController.ENTRY_NIL);
					}
				
				}
				if(payment.getIs_extraAdvanceReceived()!=null && payment.getIs_extraAdvanceReceived().equals(true) && payment.getSupplier_bill_no()!=null)
				{
					payment.setAmount(originalAmount);
				}
				payment.setIs_purchasegenrated(true);
				paymentDao.update(payment);

			} catch (MyWebException e) {
				e.printStackTrace();
			}
		}
		
		PurchaseEntry purchase=null;
		try {
			purchase = pururchaseEntryDAO.findOne(id);
		} catch (MyWebException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (sup.getSupplier_id() != null) {
			try {
				
				supplierService.addsuppliertrsansactionbalance(sup.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
						sup.getSupplier_id(), (long) 4, (double)sup.getRound_off(), (double) 0, (long) 1);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (sup.getSubledger_Id() != null) {
			try {
				
				subledgerService.addsubledgertransactionbalance(sup.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(), sup.getSubledger_Id(),
						(long) 2, (double) 0,(double) sup.getTransaction_value(), (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			if(sup.getPaymentId() == null)
			{
				if (sup.getTds_amount() != null && sup.getTds_amount() != 0) {
					
					SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", sup.getCompany().getCompany_id());
					subledgerService.addsubledgertransactionbalance(sup.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)sup.getTds_amount(), (double) 0, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}
			if (sup.getCgst() != null && sup.getCgst() > 0) {
				
				SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST", sup.getCompany().getCompany_id());
				if(subledgercgst!=null)
				{
					
				subledgerService.addsubledgertransactionbalance(sup.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
						subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getCgst(), (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}
			if (sup.getSgst() != null && sup.getSgst() > 0) {
				
				SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST", sup.getCompany().getCompany_id());
				if(subledgersgst!=null)
				{
				subledgerService.addsubledgertransactionbalance(sup.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
						subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getSgst(), (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}
			if (sup.getIgst() != null && sup.getIgst() > 0) {
				
				SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST", sup.getCompany().getCompany_id());
				if(subledgerigst!=null)
				{
				subledgerService.addsubledgertransactionbalance(sup.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
						subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getIgst(), (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}
			
			if (sup.getState_compansation_tax() != null && sup.getState_compansation_tax() > 0) {
				SubLedger subledgercess = subLedgerDAO.findOne("Input CESS", sup.getCompany().getCompany_id());
				if(subledgercess!=null)
				{
				subledgerService.addsubledgertransactionbalance(sup.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
						subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getState_compansation_tax(),
						(long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}
			if (sup.getTotal_vat() != null && sup.getTotal_vat() > 0) {
				
				SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", sup.getCompany().getCompany_id());
				if(subledgervat!=null)
				{
				subledgerService.addsubledgertransactionbalance(sup.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
						subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vat(), (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}
			if (sup.getTotal_vatcst() != null && sup.getTotal_vatcst() > 0) {
				
				SubLedger subledgercst = subLedgerDAO.findOne("Input CST", sup.getCompany().getCompany_id());
				if(subledgercst!=null)
				{
				subledgerService.addsubledgertransactionbalance(sup.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
						subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_vatcst(), (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}
			if (sup.getTotal_excise() != null && sup.getTotal_excise() > 0) {
				
				SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE", sup.getCompany().getCompany_id());
				if(subledgerexcise!=null)
				{
				subledgerService.addsubledgertransactionbalance(sup.getAccountingYear().getYear_id(),sup.getSupplier_bill_date(),sup.getCompany().getCompany_id(),
						subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)sup.getTotal_excise(), (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public List<PurchaseEntry> findAll() {
		return pururchaseEntryDAO.findAll();
	}

	@Override
	public List<PurchaseEntryProductEntityClass> findAllPurchaseEntryProductEntityList(Long entryId) {

		return pururchaseEntryDAO.findAllPurchaseEntryProductEntityList(entryId);
	}

	@Override
	public String deletePurchaseEntryProduct(Long PEId, Long purId, Long Company_id) {

		return pururchaseEntryDAO.deletePurchaseEntryProduct(PEId, purId, Company_id);
	}

	@Override
	public PurchaseEntry isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CopyOnWriteArrayList<PurchaseEntry> getReport(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {
		return pururchaseEntryDAO.getReport(Id, from_date, to_date, comId);
	}

	@Override
	public List<PurchaseEntry> findAllPurchaseEntryOfCompany(Long CompanyId, Boolean flag) {
		return pururchaseEntryDAO.findAllPurchaseEntryOfCompany(CompanyId, flag);
	}
	
	
	@Override
	public List<PurchaseEntry> findAllPurchaseEntryOfCompanyForMobile(Long CompanyId, Boolean flag,Long yearID ) {
		return pururchaseEntryDAO.findAllPurchaseEntryOfCompanyForMobile(CompanyId, flag,yearID);
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		return pururchaseEntryDAO.deleteByIdValue(entityId);
	}

	@Override
	public List<BillsReceivableForm> getBillsPayable(Long supplierId, LocalDate fromDate, LocalDate toDate, Long companyId) {
		List<PurchaseEntry> purchaseEntries = pururchaseEntryDAO.getBillsPayable(supplierId, fromDate, toDate,
				companyId);
	
		List<BillsReceivableForm> billsPayable = new ArrayList<BillsReceivableForm>();
		for (PurchaseEntry purchaseEntry : purchaseEntries) {
			
			if (purchaseEntry.getPayments().size()>0 || purchaseEntry.getDebitNote().size()>0) {
				double total = 0;
				for (Payment payment : purchaseEntry.getPayments()) {
					
					if(payment.getTds_amount()!= null && payment.getTds_paid()!=null && payment.getTds_paid()==true)
						total += payment.getAmount()+payment.getTds_amount(); 
						else
							total += payment.getAmount();
				
				}
				for (DebitNote note : purchaseEntry.getDebitNote()) {
					
							total += note.getRound_off();
				}
				if (total < purchaseEntry.getRound_off()) {
					
		        	BillsReceivableForm entry = new BillsReceivableForm();
					entry.setSupplier_bill_date(purchaseEntry.getSupplier_bill_date());
					entry.setVoucher_no(purchaseEntry.getVoucher_no());
					entry.setRound_off(purchaseEntry.getRound_off() - total);
					entry.setSupplier(purchaseEntry.getSupplier());
					entry.setParticulars("Purchase");
					billsPayable.add(entry);
				}
			} else {
				BillsReceivableForm entry = new BillsReceivableForm();
				entry.setSupplier_bill_date(purchaseEntry.getSupplier_bill_date());
				entry.setVoucher_no(purchaseEntry.getVoucher_no());
				entry.setRound_off(purchaseEntry.getRound_off());
				entry.setSupplier(purchaseEntry.getSupplier());
				entry.setParticulars("Purchase");
				billsPayable.add(entry);
			}
		}
		return billsPayable;
	}

	@Override
	public List<PurchaseEntry> getB2BList(Integer month, Long yearId, Long companyId) {
		return pururchaseEntryDAO.getB2BList(month, yearId, companyId);
	}

	@Override
	public List<PurchaseEntry> getB2CLList(Integer month, Long yearId, Long companyId, Long stateId) {
		return pururchaseEntryDAO.getB2CLList(month, yearId, companyId, stateId);
	}

	@Override
	public List<PurchaseEntry> getB2CSList(Integer month, Long yearId, Long companyId, Long stateId) {
		return pururchaseEntryDAO.getB2CSList(month, yearId, companyId, stateId);
	}

	@Override
	public List<PurchaseEntry> getExpList(Integer month, Long yearId, Long companyId, Long countryId) {
		return pururchaseEntryDAO.getExpList(month, yearId, companyId, countryId);
	}

	@Override
	public PurchaseEntry findOneWithAll(Long purId) {
		return pururchaseEntryDAO.findOneWithAll(purId);
	}

	@Override
	public Long savePurchaseEntryThroughExcel(PurchaseEntry entry) {
		return pururchaseEntryDAO.savePurchaseEntryThroughExcel(entry);
	}

	@Override
	public Long savePurchaseEntryProductEntityClassThroughExcel(PurchaseEntryProductEntityClass entry) {
		return pururchaseEntryDAO.savePurchaseEntryProductEntityClassThroughExcel(entry);
	}

	@Override
	public void updatePurchaseEntryThroughExcel(PurchaseEntry entry) {
		pururchaseEntryDAO.updatePurchaseEntryThroughExcel(entry);

	}

	@Override
	public void updatePurchaseEntryProductEntityClassThroughExcel(PurchaseEntryProductEntityClass entry) {
		pururchaseEntryDAO.updatePurchaseEntryProductEntityClassThroughExcel(entry);

	}

	@Override
	public List<PurchaseEntry> findAllPurchaseEntriesOfCompany(Long CompanyId,Boolean importFunction) {
		return pururchaseEntryDAO.findAllPurchaseEntriesOfCompany(CompanyId,importFunction);
	}

	@Override
	public PurchaseEntryProductEntityClass editproductdetailforPurchaseEntry(Long entryId) {
		return pururchaseEntryDAO.editproductdetailforPurchaseEntry(entryId);
	}

	@Override
	public void updatePurchaseEntry(PurchaseEntry entry) {
		pururchaseEntryDAO.updatePurchaseEntry(entry);
	}

	@Override
	public void updatePurchaseEntryProductEntityClass(PurchaseEntryProductEntityClass entry) {
		pururchaseEntryDAO.updatePurchaseEntryProductEntityClass(entry);
	}

	@Override
	public List<PurchaseEntry> getDayBookReport(LocalDate fromDate, LocalDate toDate, Long companyId) {
		return pururchaseEntryDAO.getDayBookReport(fromDate, toDate,companyId);
	}

	@Override
	public List<PurchaseEntry> findAllactivePurchaseEntryOfCompany(long company_id, boolean b) {
		return pururchaseEntryDAO.findAllactivePurchaseEntryOfCompany(company_id, b);
	}

	@Override
	public void ChangeStatusOfPurchaseEntryThroughExcel(PurchaseEntry entry) {
		pururchaseEntryDAO.ChangeStatusOfPurchaseEntryThroughExcel(entry);
		
	}
	@Override
	public List<PurchaseEntry> supplirPurchaseHavingGST0(LocalDate fromDate, LocalDate toDate, Long company_id) {
		// TODO Auto-generated method stub
		return pururchaseEntryDAO.supplirPurchaseHavingGST0(fromDate, toDate, company_id);
	}

	@Override
	public List<PurchaseEntry> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate fromDate, LocalDate toDate,
			Long company_id) {
		// TODO Auto-generated method stub
		return pururchaseEntryDAO.supplierHavingGSTApplicbleAsNOAndExceedingZERO(fromDate, toDate, company_id);
	}
	@Override
	public List<HSNReportForm> getHsnList(Integer month, Long yearId, Long companyId) {
		List<HSNReportForm> hsnReportForms = new ArrayList<HSNReportForm>();
		List<PurchaseEntry> purchaseEntries = pururchaseEntryDAO.getHsnList(month, yearId, companyId);
		for (PurchaseEntry purchaseEntry : purchaseEntries) {
			List<PurchaseEntryProductEntityClass> productDetails = pururchaseEntryDAO.findAllPurchaseEntryProductEntityList(purchaseEntry.getPurchase_id());
			if(productDetails != null && productDetails.size() > 0) {
				for (PurchaseEntryProductEntityClass detail : productDetails) {
					Boolean flag = true;

					if(hsnReportForms.size() > 0) {
						if(detail.getHSNCode() != null && !detail.getHSNCode().trim().equals("")) {
							for(HSNReportForm hsnReport : hsnReportForms) {
								if(detail.getHSNCode().equals(hsnReport.getHsnCode())) {
									flag = false;									
									double total = 0;
									
									if(detail.getTransaction_amount() != null && detail.getTransaction_amount() > 0) {
										hsnReport.setTaxableValue(hsnReport.getTaxableValue() + detail.getTransaction_amount());
										total += detail.getTransaction_amount();
									}
									if(detail.getCGST() != null && detail.getCGST() > 0) {
										hsnReport.setCgstAmount(hsnReport.getCgstAmount() + detail.getCGST());
										total += detail.getCGST();
									}
									if(detail.getSGST() != null && detail.getSGST() > 0) {
										hsnReport.setSgstAmount(hsnReport.getSgstAmount() + detail.getSGST());
										total += detail.getSGST();
									}
									if(detail.getIGST() != null && detail.getIGST() > 0) {
										hsnReport.setIgstAmount(hsnReport.getIgstAmount() + detail.getIGST());
										total += detail.getIGST();
									}
									if(detail.getState_com_tax() != null && detail.getState_com_tax() > 0) {
										hsnReport.setCessAmount(hsnReport.getCessAmount() + detail.getState_com_tax());
										total += detail.getState_com_tax();
									}
									if(detail.getQuantity() != null && detail.getQuantity() > 0) {
										hsnReport.setTotalQuantity(hsnReport.getTotalQuantity() + detail.getQuantity());
									}
									hsnReport.setTotalValue(hsnReport.getTotalValue() + total);
								}
							}
						}
						
					}
					if(flag) {
						if(detail.getHSNCode() != null && !detail.getHSNCode().trim().equals("")) {

							HSNReportForm hsnReport = new HSNReportForm();
							double total = 0;
							
							hsnReport.setHsnCode(detail.getHSNCode());
							
							if(detail.getTransaction_amount() != null && detail.getTransaction_amount() > 0) {
								hsnReport.setTaxableValue(detail.getTransaction_amount());
								total += detail.getTransaction_amount();
							}
							else {
								hsnReport.setTaxableValue((double) 0);
							}
							
							if(detail.getCGST() != null && detail.getCGST() > 0) {
								hsnReport.setCgstAmount(detail.getCGST());
								total += detail.getCGST();
							}
							else {
								hsnReport.setCgstAmount((double) 0);
							}
							
							if(detail.getSGST() != null && detail.getSGST() > 0) {
								hsnReport.setSgstAmount(detail.getSGST());
								total += detail.getSGST();
							}
							else {
								hsnReport.setSgstAmount((double) 0);
							}
							if(detail.getIGST() != null && detail.getIGST() > 0) {
								hsnReport.setIgstAmount(detail.getIGST());
								total += detail.getIGST();
							}
							else {
								hsnReport.setIgstAmount((double) 0);
							}
							
							if(detail.getState_com_tax() != null && detail.getState_com_tax() > 0) {
								hsnReport.setCessAmount(detail.getState_com_tax());
								total += detail.getState_com_tax();
							}
							else {
								hsnReport.setCessAmount((double) 0);
							}
							
							if(detail.getQuantity() != null && detail.getQuantity() > 0) {
								hsnReport.setTotalQuantity(detail.getQuantity());
							}
							else {
								hsnReport.setTotalQuantity((double) 0);
							}
							
							hsnReport.setTotalValue(total);
							hsnReport.setUqc(detail.getUOM());
							
							hsnReportForms.add(hsnReport);
						}
					}
				}
			}
		}
		return hsnReportForms;
	}

	@Override
	public List<ExempListForm> getExempList(Integer month, Long yearId, Long companyId) {
		List<ExempListForm> exempListForms = new ArrayList<ExempListForm>();
		List<PurchaseEntry> exempList = pururchaseEntryDAO.getExempList(month, yearId, companyId);
		List<PurchaseEntry> nilList = pururchaseEntryDAO.getNilList(month, yearId, companyId);
		List<PurchaseEntry> nonGstList = pururchaseEntryDAO.getNonGstList(month, yearId, companyId);
		
		for (PurchaseEntry exemp : exempList) {
			Boolean flag = true;
			if(exempListForms.size() > 0) {
				for(ExempListForm exempForm : exempListForms) {
					if(exempForm.getSupId() == exemp.getSupplier().getSupplier_id()) {
						flag = false;
						exempForm.setExempted(exempForm.getExempted() + exemp.getRound_off());
					}
				}
			}
			if(flag) {
				ExempListForm form = new ExempListForm();
				form.setDesc(exemp.getSupplier().getCompany_name());
				form.setExempted(exemp.getRound_off());
				form.setNillRated((double) 0);
				form.setNonGst((double) 0);
				form.setSupId(exemp.getSupplier().getSupplier_id());
				exempListForms.add(form);
			}
		}
		for (PurchaseEntry nil : nilList) {
			Boolean flag = true;
			if(exempListForms.size() > 0) {
				for(ExempListForm exempForm : exempListForms) {
					if(exempForm.getSupId() == nil.getSupplier().getSupplier_id()) {
						flag = false;
						exempForm.setNillRated(exempForm.getExempted() + nil.getRound_off());
					}
				}
			}
			if(flag) {
				ExempListForm form = new ExempListForm();
				form.setDesc(nil.getSupplier().getCompany_name());
				form.setExempted((double) 0);
				form.setNillRated(nil.getRound_off());
				form.setNonGst((double) 0);
				form.setSupId(nil.getSupplier().getSupplier_id());
				exempListForms.add(form);
			}
		}
		for (PurchaseEntry nongst : nonGstList) {
			Boolean flag = true;
			if(exempListForms.size() > 0) {
				for(ExempListForm exempForm : exempListForms) {
					if(exempForm.getSupId() == nongst.getSupplier().getSupplier_id()) {
						flag = false;
						exempForm.setNonGst(exempForm.getExempted() + nongst.getRound_off());
					}
				}
			}
			if(flag) {
				ExempListForm form = new ExempListForm();
				form.setDesc(nongst.getSupplier().getCompany_name());
				form.setExempted((double) 0);
				form.setNillRated((double) 0);
				form.setNonGst(nongst.getRound_off());
				form.setSupId(nongst.getSupplier().getSupplier_id());
				exempListForms.add(form);
			}
		}
		
		return exempListForms;
	}

	@Override
	public List<PurchaseEntry> supplierPurchaseWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,
			LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		return pururchaseEntryDAO.supplierPurchaseWithSubledgerTransactionsRs300000AndAbove(from_date, to_date, companyId);
	}

	@Override
	public List<PurchaseEntry> getPurchaseForLedgerReport(LocalDate from_date, LocalDate to_date, Long suppilerId,
			Long companyId) {
		// TODO Auto-generated method stub
		return pururchaseEntryDAO.getPurchaseForLedgerReport(from_date, to_date, suppilerId, companyId);
	}

	@Override
	public PurchaseEntry isExcelVocherNumberExist(String vocherNo, Long companyId) {
		// TODO Auto-generated method stub
		return pururchaseEntryDAO.isExcelVocherNumberExist(vocherNo, companyId);
	}

	@Override
	public Double getTotalBillsPayable(Long companyId) {
     	Double amount=0.0;
     	Double totalAmount = 0.0;
     	LocalDate fromDate = null;
     	LocalDate toDate=LocalDate.now();
		Map<String, Double> billsPayable1 = new HashMap<String, Double>();
		//List<BillsReceivableForm> billsPayable = new ArrayList<BillsReceivableForm>();
		List<PurchaseEntry> billsPayable = null;
		List<DebitNote> debitNoteforPurchase=null;
		List<Payment> paymentagainstPurchase=null;
		List<Suppliers> suppliersList= supplierService.findAllSuppliersOfCompany(companyId);
		
		   try {
		    	fromDate=companyDao.findOne(companyId).getOpeningbalance_date();
		    	
		    	 billsPayable=getPurchaseForLedgerReport1( toDate, (long)0, companyId);
				 debitNoteforPurchase =debitNoteDao.getDebitNoteForLedgerReport(fromDate, toDate, (long)0, companyId);
		    	//billsPayable= getBillsPayable((long)0, fromDate,new LocalDate(),companyId);
			 paymentagainstPurchase=paymentDao.getPaymentForLedgerReport1( toDate, (long)0, companyId);
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		String companyName;
		Long debitNoteId;
		Long payment_purchaseId;
		Double billAmt=0.0;
		for (Suppliers supplier : suppliersList) {
			
			for (PurchaseEntry br : billsPayable) {

				if (br.getSupplier().getSupplier_id().equals(supplier.getSupplier_id())) {
					
					billAmt=br.getRound_off();
					for(Payment payment:paymentagainstPurchase){
						
						if(payment.getSupplier_bill_no()!=null){
							payment_purchaseId=payment.getSupplier_bill_no().getPurchase_id();
							
							//payment_purchaseId=Long.parseLong(paymentPurchaseId);
						if(br.getPurchase_id().equals(payment_purchaseId)==true){
							if(payment.getTds_amount()!= null && payment.getTds_paid()!=null && payment.getTds_paid()==true)
								
							{
								billAmt=billAmt-(payment.getAmount()+payment.getTds_amount());
							}else{
								billAmt=billAmt-payment.getAmount();
							}
							
						}
						}
					}
					for(DebitNote debitnote:debitNoteforPurchase){
						if(debitnote.getPurchase_bill_id()!=null){
		               debitNoteId=debitnote.getPurchase_bill_id().getPurchase_id();
	            	if(br.getPurchase_id().equals(debitNoteId)==true){
	            		
						billAmt=billAmt-debitnote.getRound_off();
		               }
	                  }
                   }
					companyName = br.getSupplier().getCompany_name();
					if(billsPayable1.containsKey(companyName))
					{
						
						amount = billsPayable1.get(companyName);
						amount += billAmt;
						billsPayable1.put(companyName, amount);
					}
					else
					{
						double advancePaymentTotalAmountAgainstSupplier = 0.0 ;
						Double openingBalance = 0.0;
						Double purchaseAmount= 0.0;
						
						OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(fromDate,companyId, supplier.getSupplier_id()); 
						
						
						if(purchaseopening!=null && purchaseopening.getDebit_balance()!=null && purchaseopening.getCredit_balance()!=null)
						{
							openingBalance =(-purchaseopening.getDebit_balance())+purchaseopening.getCredit_balance();
						}
					
						
							
						List<PurchaseEntry> purchaseaginstopening =	getAllOpeningBalanceAgainstPurchase(supplier.getSupplier_id(), companyId);
						
						for(PurchaseEntry purchase:purchaseaginstopening) 
						{
							purchaseAmount=purchaseAmount+purchase.getRound_off();
						}
						openingBalance=openingBalance+purchaseAmount;
						
						
						//**** commented for Bills Payable not matching
					//	List<Payment> paymentaginstopening=	paymentDao.getAllOpeningBalanceAgainstPayment(supplier.getSupplier_id(), companyId); 
						List<Payment> paymentaginstopening=	paymentDao.getAllOpeningBalanceAgainstPaymentForPeriod(supplier.getSupplier_id(), companyId,toDate);
						
						for(Payment payment:paymentaginstopening)
						{
							openingBalance=openingBalance-payment.getAmount();
						}
						
						//List<Payment> paymentlist = paymentDao.getAllAdvancePaymentsAgainstSupplier(supplier.getSupplier_id(), companyId);
						List<Payment> paymentlist = paymentDao.getAllAdvancePaymentsAgainstSupplierForPeriod(supplier.getSupplier_id(), companyId,toDate);
						for(Payment payment : paymentlist)
						{
							if(payment.getTds_amount()!=null)
								advancePaymentTotalAmountAgainstSupplier += payment.getAmount()+payment.getTds_amount(); 
								else
									advancePaymentTotalAmountAgainstSupplier += payment.getAmount();
						}
						
						//billsPayable1.put(companyName, (billAmt+(-advancePaymentTotalAmountAgainstSupplier)+openingBalance));
					}
				}
			}
			if(!billsPayable1.containsKey(supplier.getCompany_name()))
			{

				double advancePaymentTotalAmountAgainstSupplier = 0.0 ;
				List<Payment> paymentlist = paymentDao.getAllAdvancePaymentsAgainstSupplier(supplier.getSupplier_id(), companyId);
			                                                        
				for(Payment payment : paymentlist)
				{
					
					if(payment.getTds_amount()!=null)
						advancePaymentTotalAmountAgainstSupplier += payment.getAmount()+payment.getTds_amount(); 
						else
							advancePaymentTotalAmountAgainstSupplier += payment.getAmount();
				}
				
				advancePaymentTotalAmountAgainstSupplier=-advancePaymentTotalAmountAgainstSupplier;
				
				OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(fromDate, companyId, supplier.getSupplier_id()); 
			
				if(purchaseopening!=null && purchaseopening.getDebit_balance()!=null && purchaseopening.getCredit_balance()!=null)
				{
					
					advancePaymentTotalAmountAgainstSupplier =advancePaymentTotalAmountAgainstSupplier+(-purchaseopening.getDebit_balance())+purchaseopening.getCredit_balance();
				}
				
				Double paymentAmount= 0.0;
				List<Payment> paymentaginstopening=	paymentDao.getAllOpeningBalanceAgainstPayment(supplier.getSupplier_id(), companyId);
				
				for(Payment payment:paymentaginstopening)
				{
					paymentAmount=paymentAmount+payment.getAmount();
				}
				
				advancePaymentTotalAmountAgainstSupplier=advancePaymentTotalAmountAgainstSupplier-paymentAmount;
				
				if(advancePaymentTotalAmountAgainstSupplier!=0)
				{
					
					billsPayable1.put(supplier.getCompany_name(), (advancePaymentTotalAmountAgainstSupplier));
				}
				
			}
		}
		
		List<Double> AmountList = new ArrayList<Double>(billsPayable1.values());
		for(Double amount1 :AmountList)
		{
			totalAmount=totalAmount+amount1;
		}
		return totalAmount;
	}

	public List<PurchaseEntry> getAllOpeningBalanceAgainstPurchase(Long suppilerId,Long companyId){
		return pururchaseEntryDAO.getAllOpeningBalanceAgainstPurchase( suppilerId, companyId);
	}

	@Override
	public List<BillsReceivableForm> getBillsPayableForPurchase(Long supplierId, LocalDate fromDate, LocalDate toDate,
			Long companyId) {
		List<PurchaseEntry> purchaseEntries = pururchaseEntryDAO.getBillsPayableForPurchase(supplierId, fromDate, toDate, companyId);
		
		
		List<BillsReceivableForm> billsPayable = new ArrayList<BillsReceivableForm>();
		for (PurchaseEntry purchaseEntry : purchaseEntries) {
		
			if (purchaseEntry.getPayments().size()>0 || purchaseEntry.getDebitNote().size()>0) {
				double total = 0;
				for (Payment payment : purchaseEntry.getPayments()) {
					LocalDate dt=payment.getDate();
					if ((dt.isBefore(toDate))||( dt.equals(toDate))){
					if(payment.getTds_amount()!= null && payment.getTds_paid()!=null && payment.getTds_paid()==true)
						total += payment.getAmount()+payment.getTds_amount(); 
						else
							total += payment.getAmount();
					}
				}
				for (DebitNote note : purchaseEntry.getDebitNote()) {
					LocalDate dt=note.getDate();
					if ((dt.isBefore(toDate))||( dt.equals(toDate))){
							total += note.getRound_off();}
				}
				if (total < purchaseEntry.getRound_off()) {
					
		        	BillsReceivableForm entry = new BillsReceivableForm();
					entry.setSupplier_bill_date(purchaseEntry.getSupplier_bill_date());
					entry.setVoucher_no(purchaseEntry.getVoucher_no());
					entry.setRound_off(purchaseEntry.getRound_off() - total);
					entry.setSupplier(purchaseEntry.getSupplier());
					entry.setParticulars("Purchase");
					billsPayable.add(entry);
				}
			} else {
				BillsReceivableForm entry = new BillsReceivableForm();
				entry.setSupplier_bill_date(purchaseEntry.getSupplier_bill_date());
				entry.setVoucher_no(purchaseEntry.getVoucher_no());
				entry.setRound_off(purchaseEntry.getRound_off());
				entry.setSupplier(purchaseEntry.getSupplier());
				entry.setParticulars("Purchase");
				billsPayable.add(entry);
			}
		}
		return billsPayable;

	}

	@Override
	public List<PurchaseEntry> getPurchaseForLedgerReport1(LocalDate to_date, Long suppilerId, Long companyId) {
		// TODO Auto-generated method stub
		return pururchaseEntryDAO.getPurchaseForLedgerReport1( to_date, suppilerId, companyId);
	}

	

}