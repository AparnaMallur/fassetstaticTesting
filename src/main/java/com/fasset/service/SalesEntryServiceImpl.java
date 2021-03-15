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

import org.jfree.data.time.Year;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
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
import com.fasset.dao.interfaces.ICreditNoteDAO;
import com.fasset.dao.interfaces.ICustomerDAO;
import com.fasset.dao.interfaces.ILedgerDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.IReceiptDAO;
import com.fasset.dao.interfaces.ISalesEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Customer;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Ledger;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Product;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.BillsReceivableForm;
import com.fasset.form.ProductInformation;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.ICustomerService;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */

@Service
@Transactional
public class SalesEntryServiceImpl implements ISalesEntryService {

	@Autowired
	private ISalesEntryDAO salesEntryDAO;

	@Autowired
	private IProductDAO productDao;

	@Autowired
	private ISubLedgerDAO subLedgerDAO;

	@Autowired
	private ICustomerDAO customerDAO;

	@Autowired
	private IReceiptDAO receiptDao;

	@Autowired
	private IAccountingYearDAO accountYearDAO;

	@Autowired
	ISubLedgerService subledgerService;
	
	@Autowired
	private ICommonService commonService;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private IBankDAO bankDao;
	
	@Autowired
	IBankService bankService;
	
	@Autowired
	private ICompanyDAO companyDao;
	@Autowired	
	private ICreditNoteDAO creditDao;
	
	@Autowired
	private IOpeningBalancesService openingbalances;
	
	@Override
	public void add(SalesEntry entity) throws MyWebException {

	}

	@Override
	public void update(SalesEntry salesEntry) throws MyWebException {
		if (salesEntry.getProductInformationList() != null && salesEntry.getProductInformationList() != "") {
			List<ProductInformation> informationList = new ArrayList<ProductInformation>();
			Set<Product> products = new HashSet<Product>();
			Product product = new Product();
			salesEntry.setUpdate_date(new LocalDateTime());
			JSONArray jsonArray = new JSONArray(salesEntry.getProductInformationList());
			
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
				information.setTransaction_amount(jsonObject.getString("transaction_amount"));
				information.setOthers(jsonObject.getString("others"));
				information.setCGST(jsonObject.getString("CGST"));
				information.setSGST(jsonObject.getString("SGST"));
				information.setIGST(jsonObject.getString("IGST"));
				information.setSCT(jsonObject.getString("SCT"));
				information.setDiscount(jsonObject.getString("discount"));
				// vat
				information.setIs_gst(jsonObject.getString("is_gst"));
				information.setVAT(jsonObject.getString("VAT"));
				information.setVATCST(jsonObject.getString("VATCST"));
				information.setExcise(jsonObject.getString("Excise"));
				informationList.add(information);
			}
			salesEntry.setInformationList(informationList);
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
			salesEntry.setProducts(products);
		}
		if(salesEntry.getSale_type()!=null)
		{
		salesEntry.setSale_type(salesEntry.getSale_type());
		}
		try {
			if (salesEntry.getSubledger_Id() != null && salesEntry.getSubledger_Id() > 0) {
				salesEntry.setSubledger(subLedgerDAO.findOne(salesEntry.getSubledger_Id()));
			}

			if (salesEntry.getCustomer_id() != null && salesEntry.getCustomer_id() > 0) {
				salesEntry.setCustomer(customerDAO.findOne(salesEntry.getCustomer_id()));
				//salesEntry.setCustomer_id(salesEntry.getCustomer().getCustomer_id());
			}
			if(salesEntry.getSale_type()!=null)
			{
			if(salesEntry.getSale_type()==2)
			{
				if (salesEntry.getBankId() != null) {				
						salesEntry.setBank(bankDao.findOne(salesEntry.getBankId()));
						salesEntry.setBankId(salesEntry.getBank().getBank_id());				
				}
			}
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		SalesEntry entity = salesEntryDAO.findOneWithCustomerAndSublegder(salesEntry.getSales_id());
		if(entity.getAdvreceipt()!=null)
		{
			salesEntry.setAdvreceipt(entity.getAdvreceipt());
			
		}
		if (salesEntry.getAdvreceipt() != null) {
			try {
				Receipt newreceipt = receiptDao.findOne(salesEntry.getAdvreceipt().getReceipt_id());
				double tds=0;
				if(salesEntry.getRound_off()>newreceipt.getAmount())
				{
					
					if(newreceipt.getAdvreceipt()!=null)
					{
						Receipt orignalReceipt = newreceipt.getAdvreceipt();
						double originalAmount= 0.0d;
						
						if(orignalReceipt.getIs_extraAdvanceReceived()!=null && orignalReceipt.getIs_extraAdvanceReceived().equals(true) && orignalReceipt.getSales_bill_id()!=null)
						{
							double amount = 0.0d;
							amount = orignalReceipt.getAmount();
							originalAmount = orignalReceipt.getAmount();
							List<Receipt> receiptList = receiptDao.getAllReceiptsAgainstAdvanceReceipt(orignalReceipt.getReceipt_id());
							for(Receipt receiptAgainstAdvance : receiptList)
							{
								amount=amount-receiptAgainstAdvance.getAmount();
							}
							orignalReceipt.setAmount(amount-orignalReceipt.getSales_bill_id().getRound_off());
							
							
						}
							orignalReceipt.setAmount(orignalReceipt.getAmount()+newreceipt.getAmount());
							orignalReceipt.setTds_amount(orignalReceipt.getTds_amount()+newreceipt.getTds_amount());
							
							newreceipt.setAmount(salesEntry.getRound_off());
							if(newreceipt.getTds_paid()==true)
							{
								tds=(newreceipt.getAmount()*orignalReceipt.getTds_amount())/orignalReceipt.getAmount();
								newreceipt.setTds_amount(tds);
								
							}
							else
							{
								newreceipt.setTds_paid(false);
								newreceipt.setTds_amount((double) 0);
							}
							
							
							
							 if(orignalReceipt.getAmount().equals(salesEntry.getRound_off()))//both receipt are equal so delete previous receipt. and add reference of original receipt to sales entry.
							    {
								    
								 if(orignalReceipt.getIs_extraAdvanceReceived()!=null && orignalReceipt.getIs_extraAdvanceReceived().equals(true) && orignalReceipt.getSales_bill_id()!=null)
									{
									    receiptDao.updateAdvanceReceipt(newreceipt);
										orignalReceipt.setAmount(originalAmount);
										orignalReceipt.setEntry_status(MyAbstractController.ENTRY_NIL);
										receiptDao.updateAdvanceReceipt(orignalReceipt);
									}
								 if(orignalReceipt.getIs_extraAdvanceReceived()==null)
									{
								    receiptDao.deleteReceipt(newreceipt.getReceipt_id());
									salesEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
									salesEntry.setAdvreceipt(orignalReceipt);
									orignalReceipt.setSales_bill_id(entity);
									orignalReceipt.setEntry_status(MyAbstractController.ENTRY_NIL);
									receiptDao.updateAdvanceReceipt(orignalReceipt);
									}
								
								}
							 else if(orignalReceipt.getAmount()<salesEntry.getRound_off())//amount of sales entry exceeding than original receipt so delete previous receipt.and add reference of original receipt to sales entry.
								{
								 if(orignalReceipt.getIs_extraAdvanceReceived()!=null && orignalReceipt.getIs_extraAdvanceReceived().equals(true) && orignalReceipt.getSales_bill_id()!=null)
									{
									    receiptDao.updateAdvanceReceipt(newreceipt);
										orignalReceipt.setAmount(originalAmount);
										orignalReceipt.setEntry_status(MyAbstractController.ENTRY_NIL);
										receiptDao.updateAdvanceReceipt(orignalReceipt);
										salesEntry.setEntry_status(MyAbstractController.ENTRY_PENDING);
									}
								 if(orignalReceipt.getIs_extraAdvanceReceived()==null)
									{
								    receiptDao.deleteReceipt(newreceipt.getReceipt_id());
				    				salesEntry.setEntry_status(MyAbstractController.ENTRY_PENDING);
									salesEntry.setAdvreceipt(orignalReceipt);
									orignalReceipt.setSales_bill_id(entity);
									orignalReceipt.setEntry_status(MyAbstractController.ENTRY_NIL);
									receiptDao.updateAdvanceReceipt(orignalReceipt);
									}
								}
							    else if (orignalReceipt.getAmount()>salesEntry.getRound_off())
							     {
							    	if(orignalReceipt.getIs_extraAdvanceReceived()==null)
									{
							    	orignalReceipt.setAmount(orignalReceipt.getAmount()-newreceipt.getAmount());
									orignalReceipt.setTds_amount(orignalReceipt.getTds_amount()-newreceipt.getTds_amount());
									 receiptDao.updateAdvanceReceipt(orignalReceipt);
									}
							    	receiptDao.updateAdvanceReceipt(newreceipt);
						    	   if(orignalReceipt.getIs_extraAdvanceReceived()!=null && orignalReceipt.getIs_extraAdvanceReceived().equals(true) && orignalReceipt.getSales_bill_id()!=null)
									{
						    	    	orignalReceipt.setAmount(originalAmount);
										orignalReceipt.setEntry_status(MyAbstractController.ENTRY_PENDING);
										receiptDao.updateAdvanceReceipt(orignalReceipt);
									}
								
							     }
						
					}
				}	
							

			} catch (MyWebException e) {
				e.printStackTrace();
			}
		}
		
		if(salesEntry.getAdvreceipt() == null) {
		if(salesEntry.getTds_amount()!=null && entity.getTds_amount() !=null && salesEntry.getTds_amount()!=entity.getTds_amount())
		{
			SubLedger subledgerTds = subLedgerDAO.findOne("TDS Receivable", entity.getCompany().getCompany_id());
			if (subledgerTds != null) {
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getCreated_date(),entity.getCompany().getCompany_id(),
						subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTds_amount(), (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getCreated_date(),entity.getCompany().getCompany_id(),
						subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)salesEntry.getTds_amount(), (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
			}
		}
		}
		if(entity.getCustomer()==null && entity.getSubledger()!=null && (entity.getRound_off() != salesEntry.getRound_off()) && salesEntry.getSubledger_Id()!=null) 
		{
			if((entity.getSale_type()!=null)&&(entity.getSale_type()==1))
			{
			SubLedger subledgercgst = subLedgerDAO.findOne("Cash In Hand", entity.getCompany().getCompany_id());
			if(subledgercgst!=null)
			{
			subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2,(double) 0,(double)entity.getRound_off(), (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
			subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2,(double) 0,(double)salesEntry.getRound_off(), (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
			}
			}
			else if((entity.getSale_type()!=null)&&(entity.getSale_type()==2))
			{
				if(entity.getBank()!=null)
				{
				bankService.addbanktransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(), entity.getBank().getBank_id(), (long) 3, (double) 0,(double)entity.getRound_off(),(long) 0);
				bankService.addbanktransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(), salesEntry.getBankId(), (long) 3, (double) 0,(double)salesEntry.getRound_off(),(long) 1);
				}
			}
				
			if (entity.getSubledger() != null) {
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
						entity.getSubledger().getSubledger_Id(), (long) 2, (double)entity.getTransaction_value(), (double) 0,
						(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
			}
			
			if (salesEntry.getSubledger_Id() != null) {
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
						salesEntry.getSubledger_Id(), (long) 2, (double)salesEntry.getTransaction_value(), (double) 0,
						(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
			}

			try {
				if ((salesEntry.getCgst() != null && entity.getCgst() != null)
						&& ((salesEntry.getCgst() > 0 && entity.getCgst() > 0))
						&& (salesEntry.getCgst() != entity.getCgst())) {
					
					SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST",
							salesEntry.getCompany().getCompany_id());
					if (subledgercgst != null) {
						
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
								subledgercgst.getSubledger_Id(), (long) 2, (double)entity.getCgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
						
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
								subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				} else if ((salesEntry.getCgst() != null && salesEntry.getCgst() > 0)
						&& (entity.getCgst() != null && entity.getCgst() == 0)) {
					
					SubLedger subledgercgst = subLedgerDAO.isExists("Output CGST", entity.getCompany().getCompany_id());
					
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
								subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0,
								(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				}

				if ((salesEntry.getSgst() != null && entity.getSgst() != null)
						&& (salesEntry.getSgst() > 0 && entity.getSgst() > 0)
						&& (salesEntry.getSgst() != entity.getSgst())) {
					
					SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
							salesEntry.getCompany().getCompany_id());
					
					if (subledgersgst != null) {
						
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
								subledgersgst.getSubledger_Id(), (long) 2, (double)entity.getSgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
						
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
								subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						
					}
				} else if ((salesEntry.getSgst() != null && salesEntry.getSgst() > 0)
						&& (entity.getSgst() != null && entity.getSgst() == 0)) {
					SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
							entity.getCompany().getCompany_id());
					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
								subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0,
								(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if ((salesEntry.getIgst() != null && entity.getIgst() != null)
						&& (salesEntry.getIgst() > 0 && entity.getIgst() > 0)
						&& (salesEntry.getIgst() != entity.getIgst())) {
					SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
							entity.getCompany().getCompany_id());
					if (subledgerigst != null) {
						
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgerigst.getSubledger_Id(), (long) 2, (double)entity.getIgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
						
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				} else if ((salesEntry.getIgst() != null && salesEntry.getIgst() > 0)
						&& (entity.getIgst() != null && entity.getIgst() == 0)) {
					
					SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
							salesEntry.getCompany().getCompany_id());
					if(subledgerigst!=null)
					{
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if ((salesEntry.getState_compansation_tax() != null && entity.getState_compansation_tax() != null)
						&& (salesEntry.getState_compansation_tax() > 0 && entity.getState_compansation_tax() > 0)
						&& (salesEntry.getState_compansation_tax() != entity.getState_compansation_tax())) {
					
					SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
							salesEntry.getCompany().getCompany_id());
					
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgercess.getSubledger_Id(), (long) 2, (double)entity.getState_compansation_tax(),
								(double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
								(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				} else if ((salesEntry.getState_compansation_tax() != null
						&& salesEntry.getState_compansation_tax() > 0)
						&& (entity.getState_compansation_tax() != null
								&& entity.getState_compansation_tax() == 0)) {
					
					SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
							salesEntry.getCompany().getCompany_id());
					
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
								(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if ((salesEntry.getTotal_vat() != null && entity.getTotal_vat() != null)
						&& (salesEntry.getTotal_vat() > 0 && entity.getTotal_vat() > 0)
						&& (salesEntry.getTotal_vat() != entity.getTotal_vat())) {
					
					SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
							salesEntry.getCompany().getCompany_id());
					
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgervat.getSubledger_Id(), (long) 2, (double)entity.getTotal_vat(), (double) 0,
								(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
						
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0,
								(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				} else if ((salesEntry.getTotal_vat() != null && salesEntry.getTotal_vat() > 0)
						&& (entity.getTotal_vat() != null && entity.getTotal_vat() == 0)) {
					SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
							salesEntry.getCompany().getCompany_id());
					
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0,
								(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if ((salesEntry.getTotal_vatcst() != null && entity.getTotal_vatcst() != null)
						&& (salesEntry.getTotal_vatcst() > 0 && entity.getTotal_vatcst() > 0)
						&& (salesEntry.getTotal_vatcst() != entity.getTotal_vatcst())) {
					SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
							salesEntry.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgercst.getSubledger_Id(), (long) 2, (double)entity.getTotal_vatcst(), (double) 0,
								(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0,
								(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				} else if ((salesEntry.getTotal_vatcst() != null && salesEntry.getTotal_vatcst() > 0)
						&& (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() == 0)) {
					SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
							salesEntry.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0,
								(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				if ((salesEntry.getTotal_excise() != null && entity.getTotal_excise() != null)
						&& (salesEntry.getTotal_excise() > 0 && entity.getTotal_excise() > 0)
						&& (salesEntry.getTotal_excise() != entity.getTotal_excise())) {
					SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
							salesEntry.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgerexcise.getSubledger_Id(), (long) 2, (double)entity.getTotal_excise(), (double) 0,
								(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(), (double) 0,
								(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				} else if ((salesEntry.getTotal_excise() != null && salesEntry.getTotal_excise() > 0)
						&& (entity.getTotal_excise() != null && entity.getTotal_excise() == 0)) {
					SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
							salesEntry.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(),
								(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		if(entity.getCustomer()!=null && entity.getSubledger()!=null && salesEntry.getSubledger_Id()!=null && salesEntry.getCustomer_id()!=null)
		{
		
		if ((entity.getRound_off() != salesEntry.getRound_off())
				|| ((long)entity.getCustomer().getCustomer_id() != (long)salesEntry.getCustomer_id())
				|| ((long)entity.getSubledger().getSubledger_Id() != (long)salesEntry.getSubledger_Id())) {

			if (((long)entity.getCustomer().getCustomer_id() != (long)salesEntry.getCustomer_id()) && ((long)entity.getSubledger().getSubledger_Id() != (long)salesEntry.getSubledger_Id()) && (entity.getRound_off() != salesEntry.getRound_off())) {	
				
				customerService.addcustomertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						entity.getCustomer().getCustomer_id(), (long) 5, (double) 0, (double)entity.getRound_off(),
						(long) 0);
				
				customerService.addcustomertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						salesEntry.getCustomer_id(), (long) 5, (double) 0, (double)salesEntry.getRound_off(), (long) 1);
				
				
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						entity.getSubledger().getSubledger_Id(), (long) 2, (double)entity.getTransaction_value(), (double) 0,
						(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
				
				
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						salesEntry.getSubledger_Id(), (long) 2, (double)salesEntry.getTransaction_value(), (double) 0,
						(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
				
				try {
					if ((salesEntry.getCgst() != null && entity.getCgst() != null)
							&& ((salesEntry.getCgst() > 0 && entity.getCgst() > 0))
							&& (salesEntry.getCgst() != entity.getCgst())) {
						
						SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercgst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)entity.getCgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getCgst() != null && salesEntry.getCgst() > 0)
							&& (entity.getCgst() != null && entity.getCgst() == 0)) {
						
						SubLedger subledgercgst = subLedgerDAO.isExists("Output CGST", entity.getCompany().getCompany_id());
						
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}

					if ((salesEntry.getSgst() != null && entity.getSgst() != null)
							&& (salesEntry.getSgst() > 0 && entity.getSgst() > 0)
							&& (salesEntry.getSgst() != entity.getSgst())) {
						
						SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgersgst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)entity.getSgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
							
						}
					} else if ((salesEntry.getSgst() != null && salesEntry.getSgst() > 0)
							&& (entity.getSgst() != null && entity.getSgst() == 0)) {
						SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
								entity.getCompany().getCompany_id());
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getIgst() != null && entity.getIgst() != null)
							&& (salesEntry.getIgst() > 0 && entity.getIgst() > 0)
							&& (salesEntry.getIgst() != entity.getIgst())) {
						SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
								entity.getCompany().getCompany_id());
						if (subledgerigst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double)entity.getIgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getIgst() != null && salesEntry.getIgst() > 0)
							&& (entity.getIgst() != null && entity.getIgst() == 0)) {
						
						SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
								salesEntry.getCompany().getCompany_id());
						if(subledgerigst!=null)
						{
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getState_compansation_tax() != null && entity.getState_compansation_tax() != null)
							&& (salesEntry.getState_compansation_tax() > 0 && entity.getState_compansation_tax() > 0)
							&& (salesEntry.getState_compansation_tax() != entity.getState_compansation_tax())) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)entity.getState_compansation_tax(),
									(double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getState_compansation_tax() != null
							&& salesEntry.getState_compansation_tax() > 0)
							&& (entity.getState_compansation_tax() != null
									&& entity.getState_compansation_tax() == 0)) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getTotal_vat() != null && entity.getTotal_vat() != null)
							&& (salesEntry.getTotal_vat() > 0 && entity.getTotal_vat() > 0)
							&& (salesEntry.getTotal_vat() != entity.getTotal_vat())) {
						
						SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)entity.getTotal_vat(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_vat() != null && salesEntry.getTotal_vat() > 0)
							&& (entity.getTotal_vat() != null && entity.getTotal_vat() == 0)) {
						SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getTotal_vatcst() != null && entity.getTotal_vatcst() != null)
							&& (salesEntry.getTotal_vatcst() > 0 && entity.getTotal_vatcst() > 0)
							&& (salesEntry.getTotal_vatcst() != entity.getTotal_vatcst())) {
						SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)entity.getTotal_vatcst(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_vatcst() != null && salesEntry.getTotal_vatcst() > 0)
							&& (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() == 0)) {
						SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((salesEntry.getTotal_excise() != null && entity.getTotal_excise() != null)
							&& (salesEntry.getTotal_excise() > 0 && entity.getTotal_excise() > 0)
							&& (salesEntry.getTotal_excise() != entity.getTotal_excise())) {
						SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
								salesEntry.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)entity.getTotal_excise(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_excise() != null && salesEntry.getTotal_excise() > 0)
							&& (entity.getTotal_excise() != null && entity.getTotal_excise() == 0)) {
						SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
								salesEntry.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
			else if (((long)entity.getCustomer().getCustomer_id() != (long)salesEntry.getCustomer_id())&&(entity.getRound_off() != salesEntry.getRound_off())) {	
				
				customerService.addcustomertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						entity.getCustomer().getCustomer_id(), (long) 5, (double) 0, (double)entity.getRound_off(),
						(long) 0);
				
				customerService.addcustomertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						salesEntry.getCustomer_id(), (long) 5, (double) 0, (double)salesEntry.getRound_off(), (long) 1);
				
				
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						entity.getSubledger().getSubledger_Id(), (long) 2, (double)entity.getTransaction_value(), (double) 0,
						(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
				
				
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						salesEntry.getSubledger_Id(), (long) 2, (double)salesEntry.getTransaction_value(), (double) 0,
						(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
				
				try {
					if ((salesEntry.getCgst() != null && entity.getCgst() != null)
							&& ((salesEntry.getCgst() > 0 && entity.getCgst() > 0))
							&& (salesEntry.getCgst() != entity.getCgst())) {
						
						SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercgst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)entity.getCgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getCgst() != null && salesEntry.getCgst() > 0)
							&& (entity.getCgst() != null && entity.getCgst() == 0)) {
						
						SubLedger subledgercgst = subLedgerDAO.isExists("Output CGST", entity.getCompany().getCompany_id());
						
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}

					if ((salesEntry.getSgst() != null && entity.getSgst() != null)
							&& (salesEntry.getSgst() > 0 && entity.getSgst() > 0)
							&& (salesEntry.getSgst() != entity.getSgst())) {
						
						SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgersgst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)entity.getSgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
							
						}
					} else if ((salesEntry.getSgst() != null && salesEntry.getSgst() > 0)
							&& (entity.getSgst() != null && entity.getSgst() == 0)) {
						SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
								entity.getCompany().getCompany_id());
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getIgst() != null && entity.getIgst() != null)
							&& (salesEntry.getIgst() > 0 && entity.getIgst() > 0)
							&& (salesEntry.getIgst() != entity.getIgst())) {
						SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
								entity.getCompany().getCompany_id());
						if (subledgerigst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double)entity.getIgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getIgst() != null && salesEntry.getIgst() > 0)
							&& (entity.getIgst() != null && entity.getIgst() == 0)) {
						
						SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
								salesEntry.getCompany().getCompany_id());
						if(subledgerigst!=null)
						{
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getState_compansation_tax() != null && entity.getState_compansation_tax() != null)
							&& (salesEntry.getState_compansation_tax() > 0 && entity.getState_compansation_tax() > 0)
							&& (salesEntry.getState_compansation_tax() != entity.getState_compansation_tax())) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)entity.getState_compansation_tax(),
									(double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getState_compansation_tax() != null
							&& salesEntry.getState_compansation_tax() > 0)
							&& (entity.getState_compansation_tax() != null
									&& entity.getState_compansation_tax() == 0)) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getTotal_vat() != null && entity.getTotal_vat() != null)
							&& (salesEntry.getTotal_vat() > 0 && entity.getTotal_vat() > 0)
							&& (salesEntry.getTotal_vat() != entity.getTotal_vat())) {
						
						SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)entity.getTotal_vat(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_vat() != null && salesEntry.getTotal_vat() > 0)
							&& (entity.getTotal_vat() != null && entity.getTotal_vat() == 0)) {
						SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getTotal_vatcst() != null && entity.getTotal_vatcst() != null)
							&& (salesEntry.getTotal_vatcst() > 0 && entity.getTotal_vatcst() > 0)
							&& (salesEntry.getTotal_vatcst() != entity.getTotal_vatcst())) {
						SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)entity.getTotal_vatcst(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_vatcst() != null && salesEntry.getTotal_vatcst() > 0)
							&& (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() == 0)) {
						SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((salesEntry.getTotal_excise() != null && entity.getTotal_excise() != null)
							&& (salesEntry.getTotal_excise() > 0 && entity.getTotal_excise() > 0)
							&& (salesEntry.getTotal_excise() != entity.getTotal_excise())) {
						SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
								salesEntry.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)entity.getTotal_excise(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_excise() != null && salesEntry.getTotal_excise() > 0)
							&& (entity.getTotal_excise() != null && entity.getTotal_excise() == 0)) {
						SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
								salesEntry.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
			
			else if (((long)entity.getSubledger().getSubledger_Id() != (long)salesEntry.getSubledger_Id())&&(entity.getRound_off() != salesEntry.getRound_off())) {
				
				
				if (entity.getSubledger().getSubledger_Id() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							entity.getSubledger().getSubledger_Id(), (long) 2, (double)entity.getTransaction_value(), (double) 0,
							(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
				}
				if (salesEntry.getSubledger_Id() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							salesEntry.getSubledger_Id(), (long) 2, (double)salesEntry.getTransaction_value(), (double) 0,
							(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
				}
				try {
					if ((salesEntry.getCgst() != null && entity.getCgst() != null)
							&& ((salesEntry.getCgst() > 0 && entity.getCgst() > 0))
							&& (salesEntry.getCgst() != entity.getCgst())) {
						
						SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercgst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)entity.getCgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getCgst() != null && salesEntry.getCgst() > 0)
							&& (entity.getCgst() != null && entity.getCgst() == 0)) {
						
						SubLedger subledgercgst = subLedgerDAO.isExists("Output CGST", entity.getCompany().getCompany_id());
						
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}

					if ((salesEntry.getSgst() != null && entity.getSgst() != null)
							&& (salesEntry.getSgst() > 0 && entity.getSgst() > 0)
							&& (salesEntry.getSgst() != entity.getSgst())) {
						
						SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgersgst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)entity.getSgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
							
						}
					} else if ((salesEntry.getSgst() != null && salesEntry.getSgst() > 0)
							&& (entity.getSgst() != null && entity.getSgst() == 0)) {
						SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
								entity.getCompany().getCompany_id());
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getIgst() != null && entity.getIgst() != null)
							&& (salesEntry.getIgst() > 0 && entity.getIgst() > 0)
							&& (salesEntry.getIgst() != entity.getIgst())) {
						SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
								entity.getCompany().getCompany_id());
						if (subledgerigst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double)entity.getIgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null ,null,null,null,null);
						}
					} else if ((salesEntry.getIgst() != null && salesEntry.getIgst() > 0)
							&& (entity.getIgst() != null && entity.getIgst() == 0)) {
						
						SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
								salesEntry.getCompany().getCompany_id());
						if(subledgerigst!=null)
						{
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getState_compansation_tax() != null && entity.getState_compansation_tax() != null)
							&& (salesEntry.getState_compansation_tax() > 0 && entity.getState_compansation_tax() > 0)
							&& (salesEntry.getState_compansation_tax() != entity.getState_compansation_tax())) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)entity.getState_compansation_tax(),
									(double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getState_compansation_tax() != null
							&& salesEntry.getState_compansation_tax() > 0)
							&& (entity.getState_compansation_tax() != null
									&& entity.getState_compansation_tax() == 0)) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getTotal_vat() != null && entity.getTotal_vat() != null)
							&& (salesEntry.getTotal_vat() > 0 && entity.getTotal_vat() > 0)
							&& (salesEntry.getTotal_vat() != entity.getTotal_vat())) {
						
						SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)entity.getTotal_vat(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_vat() != null && salesEntry.getTotal_vat() > 0)
							&& (entity.getTotal_vat() != null && entity.getTotal_vat() == 0)) {
						SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getTotal_vatcst() != null && entity.getTotal_vatcst() != null)
							&& (salesEntry.getTotal_vatcst() > 0 && entity.getTotal_vatcst() > 0)
							&& (salesEntry.getTotal_vatcst() != entity.getTotal_vatcst())) {
						SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)entity.getTotal_vatcst(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_vatcst() != null && salesEntry.getTotal_vatcst() > 0)
							&& (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() == 0)) {
						SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((salesEntry.getTotal_excise() != null && entity.getTotal_excise() != null)
							&& (salesEntry.getTotal_excise() > 0 && entity.getTotal_excise() > 0)
							&& (salesEntry.getTotal_excise() != entity.getTotal_excise())) {
						SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
								salesEntry.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)entity.getTotal_excise(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_excise() != null && salesEntry.getTotal_excise() > 0)
							&& (entity.getTotal_excise() != null && entity.getTotal_excise() == 0)) {
						SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
								salesEntry.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} 
			else if (((long)entity.getCustomer().getCustomer_id() != (long)salesEntry.getCustomer_id())) {
				
				customerService.addcustomertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						entity.getCustomer().getCustomer_id(), (long) 5, (double) 0, (double)entity.getRound_off(),
						(long) 0);
				
				customerService.addcustomertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						salesEntry.getCustomer_id(), (long) 5, (double) 0, (double)salesEntry.getRound_off(), (long) 1);
				
				
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						entity.getSubledger().getSubledger_Id(), (long) 2, (double)entity.getTransaction_value(), (double) 0,
						(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
				
				
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						salesEntry.getSubledger_Id(), (long) 2, (double)salesEntry.getTransaction_value(), (double) 0,
						(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
				
			} else if (((long)entity.getSubledger().getSubledger_Id() != (long)salesEntry.getSubledger_Id())) {
				if (entity.getSubledger().getSubledger_Id() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							entity.getSubledger().getSubledger_Id(), (long) 2, (double)entity.getTransaction_value(), (double) 0,
							(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
				}

				if (salesEntry.getSubledger_Id() != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							salesEntry.getSubledger_Id(), (long) 2, (double)salesEntry.getTransaction_value(), (double) 0,
							(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
				}
			} else {
				
				
				customerService.addcustomertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						entity.getCustomer().getCustomer_id(), (long) 5, (double) 0, (double)entity.getRound_off(),
						(long) 0);
				
				customerService.addcustomertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						salesEntry.getCustomer_id(), (long) 5, (double) 0, (double)salesEntry.getRound_off(), (long) 1);
				
				
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						entity.getSubledger().getSubledger_Id(), (long) 2, (double)entity.getTransaction_value(), (double) 0,
						(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
				
				
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						salesEntry.getSubledger_Id(), (long) 2, (double)salesEntry.getTransaction_value(), (double) 0,
						(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
				
				try {
					if ((salesEntry.getCgst() != null && entity.getCgst() != null)
							&& ((salesEntry.getCgst() > 0 && entity.getCgst() > 0))
							&& (salesEntry.getCgst() != entity.getCgst())) {
						
						SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercgst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)entity.getCgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getCgst() != null && salesEntry.getCgst() > 0)
							&& (entity.getCgst() != null && entity.getCgst() == 0)) {
						
						SubLedger subledgercgst = subLedgerDAO.isExists("Output CGST", entity.getCompany().getCompany_id());
						
						if (subledgercgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}

					if ((salesEntry.getSgst() != null && entity.getSgst() != null)
							&& (salesEntry.getSgst() > 0 && entity.getSgst() > 0)
							&& (salesEntry.getSgst() != entity.getSgst())) {
						
						SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgersgst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)entity.getSgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
							
						}
					} else if ((salesEntry.getSgst() != null && salesEntry.getSgst() > 0)
							&& (entity.getSgst() != null && entity.getSgst() == 0)) {
						SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
								entity.getCompany().getCompany_id());
						if (subledgersgst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),entity.getCompany().getCompany_id(),
									subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getIgst() != null && entity.getIgst() != null)
							&& (salesEntry.getIgst() > 0 && entity.getIgst() > 0)
							&& (salesEntry.getIgst() != entity.getIgst())) {
						SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
								entity.getCompany().getCompany_id());
						if (subledgerigst != null) {
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double)entity.getIgst(), (double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getIgst() != null && salesEntry.getIgst() > 0)
							&& (entity.getIgst() != null && entity.getIgst() == 0)) {
						
						SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
								salesEntry.getCompany().getCompany_id());
						if(subledgerigst!=null)
						{
						subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
								subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getState_compansation_tax() != null && entity.getState_compansation_tax() != null)
							&& (salesEntry.getState_compansation_tax() > 0 && entity.getState_compansation_tax() > 0)
							&& (salesEntry.getState_compansation_tax() != entity.getState_compansation_tax())) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)entity.getState_compansation_tax(),
									(double) 0, (long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getState_compansation_tax() != null
							&& salesEntry.getState_compansation_tax() > 0)
							&& (entity.getState_compansation_tax() != null
									&& entity.getState_compansation_tax() == 0)) {
						
						SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgercess != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getTotal_vat() != null && entity.getTotal_vat() != null)
							&& (salesEntry.getTotal_vat() > 0 && entity.getTotal_vat() > 0)
							&& (salesEntry.getTotal_vat() != entity.getTotal_vat())) {
						
						SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)entity.getTotal_vat(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_vat() != null && salesEntry.getTotal_vat() > 0)
							&& (entity.getTotal_vat() != null && entity.getTotal_vat() == 0)) {
						SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
								salesEntry.getCompany().getCompany_id());
						
						if (subledgervat != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if ((salesEntry.getTotal_vatcst() != null && entity.getTotal_vatcst() != null)
							&& (salesEntry.getTotal_vatcst() > 0 && entity.getTotal_vatcst() > 0)
							&& (salesEntry.getTotal_vatcst() != entity.getTotal_vatcst())) {
						SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)entity.getTotal_vatcst(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_vatcst() != null && salesEntry.getTotal_vatcst() > 0)
							&& (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() == 0)) {
						SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
								salesEntry.getCompany().getCompany_id());
						if (subledgercst != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if ((salesEntry.getTotal_excise() != null && entity.getTotal_excise() != null)
							&& (salesEntry.getTotal_excise() > 0 && entity.getTotal_excise() > 0)
							&& (salesEntry.getTotal_excise() != entity.getTotal_excise())) {
						SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
								salesEntry.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)entity.getTotal_excise(), (double) 0,
									(long) 0,entity,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(), (double) 0,
									(long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
						}
					} else if ((salesEntry.getTotal_excise() != null && salesEntry.getTotal_excise() > 0)
							&& (entity.getTotal_excise() != null && entity.getTotal_excise() == 0)) {
						SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
								salesEntry.getCompany().getCompany_id());
						if (subledgerexcise != null) {
							subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(),
									(double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
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
			if (salesEntry.getSubledger_Id() != null) {
				subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						salesEntry.getSubledger_Id(), (long) 2, (double)salesEntry.getTransaction_value(), (double) 0, (long) 1,entity,null,null,null,null,null,null,null,null,null,null,null);
			}
		}
			

		salesEntryDAO.update(salesEntry);

	}

	@Override
	public List<SalesEntry> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SalesEntry getById(Long id) throws MyWebException {
		return salesEntryDAO.findOne(id);
	}

	@Override
	public SalesEntry getById(String id) throws MyWebException {
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
	public void remove(SalesEntry entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(SalesEntry entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public Company findCompany(Long user_id) {
		return salesEntryDAO.findCompanyDao(user_id);
	}

	@Override
	public List<Suppliers> findAllSuppliers(Long company_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long saveSalesEntry(SalesEntry salesEntry) {
		List<ProductInformation> informationList = new ArrayList<ProductInformation>();
		Set<Product> products = new HashSet<Product>();
		Product product = new Product();
		salesEntry.setEntry_status(MyAbstractController.ENTRY_PENDING);
		JSONArray jsonArray = new JSONArray(salesEntry.getProductInformationList());
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
			information.setTransaction_amount(jsonObject.getString("transaction_amount"));
			information.setOthers(jsonObject.getString("others"));
			information.setCGST(jsonObject.getString("CGST"));
			information.setSGST(jsonObject.getString("SGST"));
			information.setIGST(jsonObject.getString("IGST"));
			information.setSCT(jsonObject.getString("SCT"));
			information.setDiscount(jsonObject.getString("discount"));
			/*information.setVATCST(jsonObject.getString("VATCST"));
			information.setExcise(jsonObject.getString("Excise"));
			information.setIs_gst(jsonObject.getString("is_gst"));
			;*/
			//vat
			 information.setIs_gst(jsonObject.getString("is_gst"));
			 information.setVAT(jsonObject.getString("VAT"));
			 information.setVATCST(jsonObject.getString("VATCST"));
			 information.setExcise(jsonObject.getString("Excise"));
			informationList.add(information);
		}
		salesEntry.setInformationList(informationList);
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
		salesEntry.setProducts(products);
		try {
			if (salesEntry.getAccounting_year_id() != null && salesEntry.getAccounting_year_id() > 0) {
				salesEntry.setAccountingYear(accountYearDAO.findOne(salesEntry.getAccounting_year_id()));
			}
		} catch (MyWebException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		salesEntry.setCreated_date(new LocalDate());
		salesEntry.setSale_type(salesEntry.getSale_type());

		try {
			if (salesEntry.getSubledger_Id() != null && salesEntry.getSubledger_Id() > 0) {
				salesEntry.setSubledger(subLedgerDAO.findOne(salesEntry.getSubledger_Id()));
			}
			if (salesEntry.getCustomer_id() != null && salesEntry.getCustomer_id() > 0) {
				salesEntry.setCustomer(customerDAO.findOne(salesEntry.getCustomer_id()));
				salesEntry.setCustomer_id(salesEntry.getCustomer().getCustomer_id());
			}
			if(salesEntry.getSale_type()==2)
			{
				if (salesEntry.getBankId() != null) {				
						salesEntry.setBank(bankDao.findOne(salesEntry.getBankId()));
						salesEntry.setBankId(salesEntry.getBank().getBank_id());				
				}
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}

		if (salesEntry.getExport_type() == 0) {
			salesEntry.setExport_type(null);
		}
		
		if (salesEntry.getReceiptId()==null) {
			salesEntry.setAgainst_advance_receipt(false);
		}
		if(salesEntry.getAgainstOpeningbalanceId()!=null && salesEntry.getAgainstOpeningbalanceId().equals((long)1))
		{
			salesEntry.setAgainstOpeningBalnce(true);
		}
		else
		{
			salesEntry.setAgainstOpeningBalnce(false);
		}
		Long id = salesEntryDAO.saveSalesEntry(salesEntry);
		if (salesEntry.getReceiptId() != null) {
			try {
				Receipt receipt = receiptDao.findOne(salesEntry.getReceiptId());	
				double originalAmount= 0.0d;
				if(receipt.getIs_extraAdvanceReceived()!=null && receipt.getIs_extraAdvanceReceived().equals(true) && receipt.getSales_bill_id()!=null)
				{
					double amount = 0.0d;
					
					amount = receipt.getAmount();
					originalAmount = receipt.getAmount();
					List<Receipt> receiptList = receiptDao.getAllReceiptsAgainstAdvanceReceipt(receipt.getReceipt_id());
					for(Receipt receiptAgainstAdvance : receiptList)
					{
						amount=amount-receiptAgainstAdvance.getAmount();
					}
					receipt.setAmount(amount-receipt.getSales_bill_id().getRound_off());
				}
				
				if(receipt.getAmount()>salesEntry.getRound_off())
				{
					Receipt newreceipt =new Receipt();
					newreceipt.setCompany(receipt.getCompany());
					newreceipt.setVoucher_no(commonService.getVoucherNumber("RV", MyAbstractController.VOUCHER_RECEIPT, receipt.getDate(),receipt.getYear_id(), receipt.getCompany().getCompany_id()));						
					newreceipt.setFlag(true);
					newreceipt.setSales_bill_id(salesEntryDAO.findOne(id));
					try {
						if (receipt.getYear_id() != null && receipt.getYear_id() > 0) {
							newreceipt.setAccountingYear(accountYearDAO.findOne(receipt.getYear_id()));
						}						
					} catch (MyWebException e1) {
						e1.printStackTrace();
					}
					newreceipt.setPayment_type(receipt.getPayment_type());
					if(receipt.getPayment_type()!=1)
					{
						newreceipt.setCheque_no(receipt.getCheque_no());
						newreceipt.setCheque_date(receipt.getCheque_date());
						if (receipt.getBank()!=null) {
							newreceipt.setBank(bankDao.findOne(receipt.getBank().getBank_id()));
							}
					}
					double tds=0;
					newreceipt.setLocal_time(new LocalTime());
					newreceipt.setGst_applied(0);					
					newreceipt.setCreated_date(new LocalDate());
					newreceipt.setDate(receipt.getDate());
					newreceipt.setTransaction_value((double) 0);
					newreceipt.setCgst((double)0);
					newreceipt.setSgst((double)0);
					newreceipt.setIgst((double)0);
					newreceipt.setTotal_excise((double)0);
					newreceipt.setTotal_vat((double)0);
					newreceipt.setTotal_vatcst((double)0);
					newreceipt.setState_compansation_tax((double)0);
					newreceipt.setEntry_status(MyAbstractController.ENTRY_NIL);
					newreceipt.setAdvance_payment(true);
					newreceipt.setAmount(salesEntry.getRound_off());
					newreceipt.setAdvreceipt(receipt);
					if(receipt.getTds_paid()==true)
					{
						newreceipt.setTds_paid(true);
						tds=(newreceipt.getAmount()*receipt.getTds_amount())/receipt.getAmount();
						/*tds=(salesEntry.getTransaction_value()*receipt.getCustomer().getTds_rate())/100;*/
						newreceipt.setTds_amount(tds);
						
					}
					else
					{
						newreceipt.setTds_paid(false);
						newreceipt.setTds_amount((double) 0);
					}
					newreceipt.setCustomer(customerDAO.findOne(salesEntry.getCustomer_id()));
					Long receiptid = receiptDao.saveReceiptDaoagainstbill(newreceipt);
					
					if(receipt.getIs_extraAdvanceReceived()==null)
					{
						receipt.setAmount(receipt.getAmount()-salesEntry.getRound_off());
						/*if(receipt.getGst_applied()==1)
						{
						receipt.setTransaction_value(receipt.getTransaction_value()-salesEntry.getRound_off());
						}*/
						receipt.setTds_amount(receipt.getTds_amount()-newreceipt.getTds_amount());
					}
				
					SalesEntry entry = salesEntryDAO.findOne(id);
					entry.setAgainst_advance_receipt(true);
					entry.setEntry_status(MyAbstractController.ENTRY_NIL);
					entry.setAdvreceipt(receiptDao.findOne(receiptid));
					salesEntryDAO.updateSalesEntryThroughExcel(entry);
					
				}
				else if(receipt.getAmount().equals(salesEntry.getRound_off()) || receipt.getAmount()<salesEntry.getRound_off())
				{
					
					if(receipt.getIs_extraAdvanceReceived()!=null && receipt.getIs_extraAdvanceReceived().equals(true) && receipt.getSales_bill_id()!=null)
					{
						Receipt newreceipt =new Receipt();
						newreceipt.setCompany(receipt.getCompany());
						newreceipt.setVoucher_no(commonService.getVoucherNumber("RV", MyAbstractController.VOUCHER_RECEIPT, receipt.getDate(),receipt.getYear_id(), receipt.getCompany().getCompany_id()));						
						newreceipt.setFlag(true);
						newreceipt.setSales_bill_id(salesEntryDAO.findOne(id));
						try {
							if (receipt.getYear_id() != null && receipt.getYear_id() > 0) {
								newreceipt.setAccountingYear(accountYearDAO.findOne(receipt.getYear_id()));
							}						
						} catch (MyWebException e1) {
							e1.printStackTrace();
						}
						newreceipt.setPayment_type(receipt.getPayment_type());
						if(receipt.getPayment_type()!=1)
						{
							newreceipt.setCheque_no(receipt.getCheque_no());
							newreceipt.setCheque_date(receipt.getCheque_date());
							if (receipt.getBank()!=null) {
								newreceipt.setBank(bankDao.findOne(receipt.getBank().getBank_id()));
								}
						}
						double tds=0;
						newreceipt.setLocal_time(new LocalTime());
						newreceipt.setGst_applied(0);					
						newreceipt.setCreated_date(new LocalDate());
						newreceipt.setDate(receipt.getDate());
						newreceipt.setTransaction_value((double) 0);
						newreceipt.setCgst((double)0);
						newreceipt.setSgst((double)0);
						newreceipt.setIgst((double)0);
						newreceipt.setTotal_excise((double)0);
						newreceipt.setTotal_vat((double)0);
						newreceipt.setTotal_vatcst((double)0);
						newreceipt.setState_compansation_tax((double)0);
						newreceipt.setEntry_status(MyAbstractController.ENTRY_NIL);
						newreceipt.setAdvance_payment(true);
						newreceipt.setAmount(salesEntry.getRound_off());
						newreceipt.setAdvreceipt(receipt);
						if(receipt.getTds_paid()==true)
						{
							newreceipt.setTds_paid(true);
							tds=(newreceipt.getAmount()*receipt.getTds_amount())/receipt.getAmount();
							/*tds=(salesEntry.getTransaction_value()*receipt.getCustomer().getTds_rate())/100;*/
							newreceipt.setTds_amount(tds);
							
						}
						else
						{
							newreceipt.setTds_paid(false);
							newreceipt.setTds_amount((double) 0);
						}
						newreceipt.setCustomer(customerDAO.findOne(salesEntry.getCustomer_id()));
						Long receiptid = receiptDao.saveReceiptDaoagainstbill(newreceipt);
						
						if(receipt.getIs_extraAdvanceReceived()==null)
						{
							receipt.setAmount(receipt.getAmount()-salesEntry.getRound_off());
							/*if(receipt.getGst_applied()==1)
							{
							receipt.setTransaction_value(receipt.getTransaction_value()-salesEntry.getRound_off());
							}*/
							receipt.setTds_amount(receipt.getTds_amount()-newreceipt.getTds_amount());
						}
					
						SalesEntry entry = salesEntryDAO.findOne(id);
						entry.setAgainst_advance_receipt(true);
						entry.setEntry_status(MyAbstractController.ENTRY_NIL);
						entry.setAdvreceipt(receiptDao.findOne(receiptid));
						salesEntryDAO.updateSalesEntryThroughExcel(entry);
						receipt.setEntry_status(MyAbstractController.ENTRY_NIL);
					}
					if(receipt.getIs_extraAdvanceReceived()==null)
					{
						
						SalesEntry entry = salesEntryDAO.findOne(id);
						if(receipt.getAmount().equals(salesEntry.getRound_off()))
						{
							entry.setEntry_status(MyAbstractController.ENTRY_NIL);
						}
						if(receipt.getAmount()<salesEntry.getRound_off())
						{
							entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
						}
						entry.setAdvreceipt(receipt);
						entry.setAgainst_advance_receipt(true);
						salesEntryDAO.updateSalesEntryThroughExcel(entry);
						receipt.setSales_bill_id(entry);
						receipt.setEntry_status(MyAbstractController.ENTRY_NIL);
					}
					
				}
				if(receipt.getIs_extraAdvanceReceived()!=null && receipt.getIs_extraAdvanceReceived().equals(true) && receipt.getSales_bill_id()!=null)
				{
					receipt.setAmount(originalAmount);
				}
				receipt.setIs_salegenrated(true);
				receiptDao.updateAdvanceReceipt(receipt);				
			} catch (MyWebException e) {
				e.printStackTrace();
			}
		}

		SalesEntry entry =null;
		try {
			entry = salesEntryDAO.findOne(id);
		} catch (MyWebException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
		if ((salesEntry.getCustomer_id() != null) && (salesEntry.getCustomer_id() != -1) && (salesEntry.getCustomer_id() != -2)) {
			customerService.addcustomertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
					salesEntry.getCustomer_id(), (long) 5, (double) 0, (double)salesEntry.getRound_off(), (long) 1);
		}
		else
		{
			if(salesEntry.getSale_type()==1)
			{
				SubLedger subledgercgst = subLedgerDAO.findOne("Cash In Hand", salesEntry.getCompany().getCompany_id());
				if(subledgercgst!=null)
				{
			 	subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2,(double) 0,(double)salesEntry.getRound_off(), (long) 1,entry,null,null,null,null,null,null,null,null,null,null,null);
			    }
			}
			else if(salesEntry.getSale_type()==2)
			{
				if(salesEntry.getBank()!=null)
				{
					bankService.addbanktransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(), salesEntry.getBank().getBank_id(), (long) 3, (double) 0,(double)salesEntry.getRound_off(),(long) 1);
				}
			}

		}
		
		
		if(salesEntry.getReceiptId() == null) // tds affection is given if and only if we are adding sales entry not against the advance receipt.
		{
		if(salesEntry.getTds_amount()!=null && salesEntry.getTds_amount()>0)
		{
			SubLedger subledgerTds = subLedgerDAO.findOne("TDS Receivable", salesEntry.getCompany().getCompany_id());
			if (subledgerTds != null) {
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)salesEntry.getTds_amount(), (long) 1,entry,null,null,null,null,null,null,null,null,null,null,null);
			}
		}
		}
		
		
		}
	catch (Exception e) {
		e.printStackTrace();
		}
		if (salesEntry.getSubledger_Id() != null) {
			subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
					salesEntry.getSubledger_Id(), (long) 2, (double)salesEntry.getTransaction_value(), (double) 0, (long) 1,entry,null,null,null,null,null,null,null,null,null,null,null);
		}
		try {
			if (salesEntry.getCgst() != null && salesEntry.getCgst() > 0) {
				
				SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST", salesEntry.getCompany().getCompany_id());
				if(subledgercgst!=null)
				{
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double) 0, (long) 1,entry,null,null,null,null,null,null,null,null,null,null,null);
				}
			}

			if (salesEntry.getSgst() != null && salesEntry.getSgst() > 0) {
				SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST", salesEntry.getCompany().getCompany_id());
				if(subledgersgst!=null)
				{
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double) 0, (long) 1,entry,null,null,null,null,null,null,null,null,null,null,null);
				}
			}

			if (salesEntry.getIgst() != null && salesEntry.getIgst() > 0) {
				
				SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST", salesEntry.getCompany().getCompany_id());
				if(subledgerigst!=null)
				{
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double) 0, (long) 1,entry,null,null,null,null,null,null,null,null,null,null,null);
				}
			}

			if (salesEntry.getState_compansation_tax() != null && salesEntry.getState_compansation_tax() > 0) {
				
				SubLedger subledgercess = subLedgerDAO.findOne("Output CESS", salesEntry.getCompany().getCompany_id());
				if(subledgercess!=null)
				{
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(), (double) 0,
						(long) 1,entry,null,null,null,null,null,null,null,null,null,null,null);
				}
			}

			if (salesEntry.getTotal_vat() != null && salesEntry.getTotal_vat() > 0) {
				
				SubLedger subledgervat = subLedgerDAO.findOne("Output VAT", salesEntry.getCompany().getCompany_id());
				if(subledgervat!=null)
				{
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double) 0, (long) 1,entry,null,null,null,null,null,null,null,null,null,null,null);
				}
			}

			if (salesEntry.getTotal_vatcst() != null && salesEntry.getTotal_vatcst() > 0) {
				
				SubLedger subledgercst = subLedgerDAO.findOne("Output CST", salesEntry.getCompany().getCompany_id());
				if(subledgercst!=null)
				{
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double) 0, (long) 1,entry,null,null,null,null,null,null,null,null,null,null,null);
				}
			}

			if (salesEntry.getTotal_excise() != null && salesEntry.getTotal_excise() > 0) {
				
				SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
						salesEntry.getCompany().getCompany_id());
				
				if(subledgerexcise!=null)
				{
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(), (double) 0, (long) 1,entry,null,null,null,null,null,null,null,null,null,null,null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public List<SalesEntry> findAll() {
		return salesEntryDAO.findAll();
	}

	@Override
	public List<SalesEntryProductEntityClass> findAllSalesEntryProductEntityList(Long entryId) {
		return salesEntryDAO.findAllSalesEntryProductEntityList(entryId);
	}

	@Override
	public String deleteSalesEntryProduct(Long SEId, Long sales_id, Long company_id) {
		return salesEntryDAO.deleteSalesEntryProduct(SEId, sales_id, company_id);
	}

	@Override
	public SalesEntry isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CopyOnWriteArrayList<SalesEntry> getReport(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {
		return salesEntryDAO.getReport(Id, from_date, to_date, comId);
	}

	@Override
	public List<SalesEntry> findAllSalesEntryOfCompany(Long CompanyId, Boolean flag) {
		return salesEntryDAO.findAllSalesEntryOfCompany(CompanyId, flag);
	}

	@Override
	public String deleteByIdValue(Long entityId,Boolean isDelete) {
		return salesEntryDAO.deleteByIdValue(entityId,isDelete);
	}
	
	@Override
	public List<BillsReceivableForm> getBillsReceivable(Long customerId, LocalDate fromDate, LocalDate toDate, Long companyId) {
		List<SalesEntry> salesEntries = salesEntryDAO.getBillsReceivable(customerId, fromDate, toDate, companyId);
		List<BillsReceivableForm> billsReceivable = new ArrayList<BillsReceivableForm>();
		
		
		for (SalesEntry salesEntry : salesEntries) {
			
			
			if ((salesEntry.getReceipts().size()>0)||(salesEntry.getCreditNote().size()>0)) {
				double total = 0;
				for (Receipt receipt : salesEntry.getReceipts()) {
					if(receipt.getTds_amount()!=null)
						total += receipt.getAmount()+receipt.getTds_amount(); 
						else
							total += receipt.getAmount();
				}
				for (CreditNote note : salesEntry.getCreditNote()) {
					
					total += note.getRound_off();
		          }
				if (total < salesEntry.getRound_off()) {
					
					if(salesEntry.getCustomer()!=null)
					{
					BillsReceivableForm entry = new BillsReceivableForm();
					entry.setCreated_date(salesEntry.getCreated_date());
					entry.setVoucher_no(salesEntry.getVoucher_no());
					entry.setRound_off(salesEntry.getRound_off() - total);
					entry.setCustomer(salesEntry.getCustomer());
					entry.setParticulars("Sales");
					billsReceivable.add(entry);
					}
					
				}
			} else {
				
				if(salesEntry.getCustomer()!=null)
				{
					
				BillsReceivableForm entry = new BillsReceivableForm();
				entry.setCreated_date(salesEntry.getCreated_date());
				entry.setVoucher_no(salesEntry.getVoucher_no());
				entry.setRound_off(salesEntry.getRound_off());
				entry.setCustomer(salesEntry.getCustomer());
				entry.setParticulars("Sales");
				billsReceivable.add(entry);
				}
			}
		}
		return billsReceivable;
	}

	@Override
	public SalesEntry findOneWithAll(Long salId) {

		return salesEntryDAO.findOneWithAll(salId);
	}

	@Override
	public Long saveSalesEntryThroughExcel(SalesEntry entry) {
		// TODO Auto-generated method stub
		return salesEntryDAO.saveSalesEntryThroughExcel(entry);
	}

	@Override
	public Long saveSalesEntryProductEntityClassThroughExcel(SalesEntryProductEntityClass entry) {
		// TODO Auto-generated method stub
		return salesEntryDAO.saveSalesEntryProductEntityClassThroughExcel(entry);
	}

	@Override
	public void updateSalesEntryThroughExcel(SalesEntry entry) {
		salesEntryDAO.updateSalesEntryThroughExcel(entry);

	}

	@Override
	public void updateSalesEntryProductEntityClassThroughExcel(SalesEntryProductEntityClass entry) {

		salesEntryDAO.updateSalesEntryProductEntityClassThroughExcel(entry);
	}

	@Override
	public List<SalesEntry> findAllSalesEntriesOfCompany(Long CompanyId ,Boolean importFunction) {

		return salesEntryDAO.findAllSalesEntriesOfCompany(CompanyId,importFunction);
	}

	@Override
	public SalesEntryProductEntityClass editproductdetailforSalesEntry(Long entryId) {
		// TODO Auto-generated method stub
		return salesEntryDAO.editproductdetailforSalesEntry(entryId);
	}

	@Override
	public void updateSalesEntry(SalesEntry entry) {
		salesEntryDAO.updateSalesEntry(entry);
		
	}

	@Override
	public void updateSalesEntryProductEntityClass(SalesEntryProductEntityClass entry) {
		salesEntryDAO.updateSalesEntryProductEntityClass(entry);
		
	}

	@Override
	public List<SalesEntry> getDayBookReport(LocalDate from_date,LocalDate to_date, Long companyId) {
	
		return salesEntryDAO.getDayBookReport(from_date, to_date,companyId);
	}

	@Override
	public String activateByIdValue(long id) {
		// TODO Auto-generated method stub
		return salesEntryDAO.activateByIdValue(id);

	}

	@Override
	public List<SalesEntry> findAllactiveSalesEntryOfCompany(long company_id, boolean b) {
		return salesEntryDAO.findAllactiveSalesEntryOfCompany(company_id,b);
	}

	@Override
	public void ChangeStatusofSalesEntryThroughExcel(SalesEntry entry) {
		salesEntryDAO.ChangeStatusofSalesEntryThroughExcel(entry);
		
	}

	@Override
	public List<SalesEntry> getCashBookBankBookReport(LocalDate from_date, LocalDate to_date, Long companyId, Integer type) {
		return salesEntryDAO.getCashBookBankBookReport(from_date, to_date, companyId, type);
	}

	@Override
	public List<SalesEntry> customerSalesHavingGST0(LocalDate from_date, LocalDate to_date, Long companyId
			) {
		// TODO Auto-generated method stub
		return  salesEntryDAO.customerSalesHavingGST0(from_date, to_date, companyId);
	}

	@Override
	public List<SalesEntry> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate from_date, LocalDate to_date,
			Long companyId) {
		// TODO Auto-generated method stub
		return salesEntryDAO.supplierHavingGSTApplicbleAsNOAndExceedingZERO(from_date, to_date, companyId);
	}

	@Override
	public List<SalesEntry> customerSalesWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,
			LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		return salesEntryDAO.customerSalesWithSubledgerTransactionsRs300000AndAbove(from_date, to_date, companyId);
	}

	@Override
	public List<SalesEntry> getSalesForLedgerReport(LocalDate from_date, LocalDate to_date, Long customerId,
			Long companyId) {
		// TODO Auto-generated method stub
		return salesEntryDAO.getSalesForLedgerReport(from_date, to_date, customerId, companyId);
	}

	@Override
	public List<SalesEntry> getCardSalesForLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId,
			Long bank_id) {
		// TODO Auto-generated method stub
		return salesEntryDAO.getCardSalesForLedgerReport(from_date, to_date, companyId, bank_id);
	}

	@Override
	public SalesEntry isExcelVocherNumberExist(String vocherNo, Long companyId) {
		// TODO Auto-generated method stub
		return salesEntryDAO.isExcelVocherNumberExist(vocherNo, companyId);
	}

	@Override
	public List<SalesEntry> findAllSalesEntryOfCompanyForMobile(Long companyId, Boolean flag,Long year_id) {
		// TODO Auto-generated method stub
		return salesEntryDAO.findAllSalesEntryOfCompanyForMobile(companyId, flag,year_id);
	}

	@Override
	public Double getTotalbillsBillsReceivable(Long company_Id) 
	 {
		LocalDate fromDate = null;
		Map<String, Double> billsReceivable1 = new HashMap<String, Double>();
		List<BillsReceivableForm> billsReceivable = new ArrayList<BillsReceivableForm>();
		
		List<Customer> customerList = customerService.findAllCustomersOfCompany(company_Id);
		
		Double totalAmount = 0.0;
	    Double amount=0.0;
	    try {
	    	fromDate=companyDao.findOne(company_Id).getOpeningbalance_date();
			billsReceivable = getBillsReceivable((long)0, fromDate,new LocalDate(),company_Id);
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Customer customer : customerList) {

			for (BillsReceivableForm br : billsReceivable) {

				if (br.getCustomer().getCustomer_id().equals(customer.getCustomer_id())) 
				{
//
					if(billsReceivable1.containsKey(br.getCustomer().getFirm_name()))
					{
						amount = billsReceivable1.get(br.getCustomer().getFirm_name());
						amount += br.getRound_off();
						billsReceivable1.put(br.getCustomer().getFirm_name(), amount);
					}
					else
					{
						double advanceReceiptTotalAmountAgainstCustomer = 0.0 ;
						Double openingBalance = 0.0;
						
						OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSales(fromDate, company_Id, customer.getCustomer_id());
						if(salesopening!=null && salesopening.getDebit_balance()!=null && salesopening.getCredit_balance()!=null)
						{
							openingBalance =(salesopening.getDebit_balance())+(-salesopening.getCredit_balance());
						}
						
						
						Double salesAmount= 0.0;
							 List<SalesEntry> salesaginstopening=getAllOpeningBalanceAgainstSales(customer.getCustomer_id(), company_Id);
							  
							  for(SalesEntry sales:salesaginstopening) {
							  
								  salesAmount=salesAmount+sales.getRound_off();
							  }
					   openingBalance = openingBalance+salesAmount;
						
					   Double receiptAmount= 0.0;
					   Double creditAmount = 0.0;
					   List<Receipt> recceiptaginstopening= receiptDao.getAllOpeningBalanceAgainstReceipt(customer.getCustomer_id(), company_Id);
					   List<CreditNote> creditnoteagainstopening=creditDao.getAllOpeningBalanceAgainstcreditnote(customer.getCustomer_id(), company_Id);
						  
						  for(Receipt receipt:recceiptaginstopening) {
							  receiptAmount=receiptAmount+receipt.getAmount(); 
						  }
						  for(CreditNote creditnote:creditnoteagainstopening) {
							  creditAmount=creditAmount+creditnote.getRound_off(); 
							  }
						  openingBalance = openingBalance-receiptAmount;
						  openingBalance = openingBalance-creditAmount;
						  
						List<Receipt> list = receiptDao.getAllAdvanceReceiptsAgainstCustomer(company_Id, customer.getCustomer_id());
						for(Receipt receipt : list)
						{
							if(receipt.getTds_amount()!=null)
								advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount()+receipt.getTds_amount(); 
								else
									advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount();
						}
						
						billsReceivable1.put(br.getCustomer().getFirm_name(), (br.getRound_off()+(-advanceReceiptTotalAmountAgainstCustomer)+openingBalance));
					}
					
				}
			}
			if(!billsReceivable1.containsKey(customer.getFirm_name()))
			{
				double advanceReceiptTotalAmountAgainstCustomer = 0.0 ;
				List<Receipt> list = receiptDao.getAllAdvanceReceiptsAgainstCustomer(company_Id, customer.getCustomer_id());
				for(Receipt receipt : list)
				{
					if(receipt.getTds_amount()!=null)
						advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount()+receipt.getTds_amount(); 
						else
							advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount();
				}
				
				
				
				OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSales(fromDate, company_Id, customer.getCustomer_id());
				
				if(salesopening!=null && salesopening.getDebit_balance()!=null && salesopening.getCredit_balance()!=null)
				{
					advanceReceiptTotalAmountAgainstCustomer =-advanceReceiptTotalAmountAgainstCustomer+(salesopening.getDebit_balance())+(-salesopening.getCredit_balance());
				}
				
				Double salesAmount= 0.0;
				LocalDate toDate=LocalDate.now();
				 Long salesId;
				  SalesEntry Receipt_saleId;
				  Long ReceiptSalesId;
				  Double saleAmount = 0.0;
				  String voucherNo="";
				  Double receiptAmountForSales= 0.0;
				
					//System.out.println("The from Date dash is " +fromDate);
					//System.out.println("The to Date dash is " +toDate);
				/* List<SalesEntry> salesaginstopening=getAllOpeningBalanceAgainstSales(customer.getCustomer_id(), company_Id);
				  
				  for(SalesEntry sales:salesaginstopening) {
				  
					  salesAmount=salesAmount+sales.getRound_off();
				  }*/ // commented the Bills Receivable not shown on Home Page correctly

				 List<SalesEntry> salesaginstopening=getAllSalesAmount(customer.getCustomer_id(), company_Id, toDate);
				
				
				 //Included the following lines to show correct Bills Receiva
				  List<Receipt> recceiptaginstsales= receiptDao.getReceiptForSales(customer.getCustomer_id(), company_Id,toDate);
					
				  List<CreditNote> creditNoteSales= creditDao.getCreditNoteForSale(company_Id,toDate);
				  
				  for(SalesEntry sales:salesaginstopening) {
					  salesId=sales.getSales_id();
					  saleAmount=sales.getRound_off();
					  voucherNo=sales.getVoucher_no();
					
				  for(Receipt receipt:recceiptaginstsales) {
					
					  Receipt_saleId=receipt.getSales_bill_id();
					 if( receipt.getSales_bill_id()!=null){
						 
					  ReceiptSalesId= Receipt_saleId.getSales_id();
					  
						
					  if (salesId.equals(ReceiptSalesId)==true){
					  receiptAmountForSales=receipt.getAmount(); 
					
					  saleAmount=saleAmount-receiptAmountForSales;
					 
					  }
				  }
				  }
				 
				  for(CreditNote creditnote:creditNoteSales) {
						
					  Receipt_saleId=creditnote.getSales_bill_id();
					 if( creditnote.getSales_bill_id()!=null){
						 
					  ReceiptSalesId= Receipt_saleId.getSales_id();
					 
					  if (salesId.equals(ReceiptSalesId)==true){
					  receiptAmountForSales=creditnote.getRound_off(); 
					
						 
					  saleAmount=saleAmount-receiptAmountForSales;
					 
					  }
				  }
				  }
				  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer+saleAmount;
				  }
				 
			
		   Double receiptAmount= 0.0;
		   Double creditAmount = 0.0;
		   List<Receipt> recceiptaginstopening= receiptDao.getAllOpeningBalanceAgainstReceipt(customer.getCustomer_id(), company_Id);
		   List<CreditNote> creditnoteagainstopening=creditDao.getAllOpeningBalanceAgainstcreditnote(customer.getCustomer_id(), company_Id);  
			  for(Receipt receipt:recceiptaginstopening) {
				  receiptAmount=receiptAmount+receipt.getAmount(); 
			  }
			  for(CreditNote creditnote:creditnoteagainstopening) {
				  creditAmount=creditAmount+creditnote.getRound_off(); 
				  }
			  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer-receiptAmount;
			  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer-creditAmount;	 
				
				if(advanceReceiptTotalAmountAgainstCustomer!=0)
				{
					billsReceivable1.put(customer.getFirm_name(), (advanceReceiptTotalAmountAgainstCustomer));
				}
			}
		}
		
		List<Double> AmountList = new ArrayList<Double>(billsReceivable1.values());
	
		for(Double amount1 :AmountList)
		{
			totalAmount=totalAmount+amount1;
		}
		
		return totalAmount;
	}
	public List<SalesEntry> getAllOpeningBalanceAgainstSales(Long customerid,Long companyId){
		
		return salesEntryDAO.getAllOpeningBalanceAgainstSales( customerid, companyId);
	}
	public List<SalesEntry> getAllSalesAmount(Long customerid,Long companyId,LocalDate toDate){
		
		return salesEntryDAO.getAllSalesAmount( customerid, companyId,toDate);
	}
	@Override
	public List<SalesEntry> findAllDisableSalesEntryOfCompanyAfterImport(Long CompanyId, Boolean flag) {
		// TODO Auto-generated method stub
		return salesEntryDAO.findAllDisableSalesEntryOfCompanyAfterImport(CompanyId, flag);
	}
	@Override
    public List<SalesEntry> findExcelVoucherNumber(long companyid) {
        // TODO Auto-generated method stub
        return salesEntryDAO.findExcelVoucherNumber(companyid);
    }

	@Override
	public List<SalesEntry> salesEntryWithEditedGSTvalues(LocalDate from_date, LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		return salesEntryDAO.salesEntryWithEditedGSTvalues(from_date,to_date,companyId);
	}

}	
	