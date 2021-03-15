package com.fasset.service;

import java.math.BigDecimal;
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
import com.fasset.dao.interfaces.ICreditNoteDAO;
import com.fasset.dao.interfaces.ICustomerDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.ISalesEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.IUnitOfMeasurementDAO;
import com.fasset.entities.CreditDetails;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Product;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Stock;
import com.fasset.entities.SubLedger;
import com.fasset.entities.UnitOfMeasurement;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IStockService;
import com.fasset.service.interfaces.ISubLedgerService;

@Service
@Transactional
public class CreditNoteServiceImpl implements ICreditNoteService {

	@Autowired
	private ICreditNoteDAO creditNoteDao;

	@Autowired
	private ICustomerDAO customerDao;

	@Autowired
	private ISalesEntryDAO salesEntryDao;

	@Autowired
	private IAccountingYearDAO accountingYearDao;

	@Autowired
	private IProductDAO productDao;

	@Autowired
	private IUnitOfMeasurementDAO uomDao;

	@Autowired
	private ISubLedgerDAO subledgerDao;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private ISubLedgerService subledgerService;
	
	@Autowired
	private IStockService stockService;

	@Override
	public void add(CreditNote entity) throws MyWebException {
		Set<CreditDetails> creditDetailsList = new HashSet<CreditDetails>();
		try {
			entity.setAccountYearId(entity.getAccountYearId());
			entity.setDate(new LocalDate(entity.getDateString()));
			entity.setCustomer(customerDao.findOne(entity.getCustomerId()));
			entity.setSales_bill_id(salesEntryDao.findOne(entity.getSalesEntryId()));
			entity.setAccounting_year_id(accountingYearDao.findOne(entity.getAccountYearId()));
			entity.setSubledger(subledgerDao.findOne(entity.getSubledgerId()));
			entity.setCreated_date(new LocalDate());
			entity.setEntry_status(MyAbstractController.ENTRY_PENDING);
			if (entity.getCreditNoteDetails() != "") {
				JSONArray jsonArray = new JSONArray(entity.getCreditNoteDetails());

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					CreditDetails creditDetails = new CreditDetails();
					creditDetails.setCgst(Double.parseDouble(json.getString("cgst")));
					creditDetails.setIgst(Double.parseDouble(json.getString("igst")));
					creditDetails.setSgst(Double.parseDouble(json.getString("sgst")));
					creditDetails.setState_com_tax(Double.parseDouble(json.getString("state_com_tax")));
					creditDetails.setVAT(Double.parseDouble(json.getString("VAT")));
					creditDetails.setVATCST(Double.parseDouble(json.getString("VATCST")));
					creditDetails.setExcise(Double.parseDouble(json.getString("Excise")));
					creditDetails.setIs_gst(Long.parseLong(json.getString("is_gst")));
					creditDetails.setDiscount(Double.parseDouble(json.getString("discount")));
					creditDetails.setFrieght(Double.parseDouble(json.getString("freight")));
					creditDetails.setLabour_charges(Double.parseDouble(json.getString("labour_charges")));
					creditDetails.setOthers(Double.parseDouble(json.getString("others")));
					creditDetails.setQuantity(Double.parseDouble(json.getString("quantity")));
					creditDetails.setRate(Double.parseDouble(json.getString("rate")));
					creditDetails.setTransaction_amount(Double.parseDouble(json.getString("transactionAmount")));
					Product product = productDao.findOne(Long.parseLong(json.getString("productId")));
					creditDetails.setProduct_id(product);
					UnitOfMeasurement uom = uomDao.findOne(Long.parseLong(json.getString("uomId")));
					creditDetails.setUom_id(uom);
					creditDetails.setCredit_id(entity);
					//Stock
					if(entity.getDescription() != 2 && entity.getDescription() != 3) {
						
						 if(!product.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
					     {
						 Integer pflag=productDao.checktype(entity.getCompany().getCompany_id(),Long.parseLong(json.getString("productId")));
					     if(pflag==1)  
					     {
						      Stock stock = null;
						      stock=stockService.isstockexist(entity.getCompany().getCompany_id(),Long.parseLong(json.getString("productId")));
						      double amt=Double.parseDouble(json.getString("quantity"))*Double.parseDouble(json.getString("rate"));						     
						      if(stock!=null)
								{
						    	    stock.setAmount(stock.getAmount()+amt);
									stock.setQuantity(stock.getQuantity()+Double.parseDouble(json.getString("quantity")));					
									stockService.updateStock(stock);
									stockService.addStockInfoOfProduct(stock.getStock_id(), entity.getCompany().getCompany_id(), Long.parseLong(json.getString("productId")), Double.parseDouble(json.getString("quantity")), Double.parseDouble(json.getString("rate")));
								}
					     }	
					     }
					}
					creditDetailsList.add(creditDetails);
				}
				entity.setCreditDetails(creditDetailsList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setLocal_time(new LocalTime());
		entity.setEntry_status(MyAbstractController.ENTRY_PENDING);
		System.out.println("The sales id is "+entity.getSalesEntryId());
		if(entity.getSalesEntryId().equals((long)-2))
		{
			entity.setAgainstOpeningBalnce(true);
		}
		else
		{
			entity.setAgainstOpeningBalnce(false);
		}
		Long id = creditNoteDao.saveCreditNoteTroughExcel(entity);
		CreditNote note = creditNoteDao.findOne(id);
		
		if(note.getSales_bill_id() != null){
			SalesEntry salesEntry = salesEntryDao.findOneWithAll(note.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
			Double total=(double) 0;
			for (CreditNote note1 : salesEntry.getCreditNote()) {
					total += note1.getRound_off();				
			}
		
			if(salesEntry.getRound_off().equals(total)) {
				salesEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
			}
			else if(total>=salesEntry.getRound_off())
			{
				salesEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
			}
			else{
				salesEntry.setEntry_status(MyAbstractController.ENTRY_PENDING);			
			}
			try {
				salesEntryDao.update(salesEntry);
			} catch (MyWebException e) {
				e.printStackTrace();
			}

		}
		
		
		if(entity.getDescription() != 2 && entity.getDescription() != 3) {
			if (entity.getCustomerId() != null && entity.getCustomerId() > 0) {
				customerService.addcustomertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getCustomerId(),(long) 5, (double)entity.getRound_off(), (double) 0, (long) 1);
			}			
			if(entity.getSubledgerId() != null && entity.getSubledgerId() > 0) {
				subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(),entity.getSubledgerId(), (long) 2, (double) 0, (double)entity.getTransaction_value_head(), (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
			}
			
			if (entity.getTds_amount() != null && entity.getTds_amount() != 0) {
				SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", entity.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)entity.getTds_amount(), (double) 0, (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
			}
			
		}else {		
			if (entity.getCustomerId() != null && entity.getCustomerId() > 0) {
				customerService.addcustomertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getCustomerId(),(long) 5, (double)entity.getRound_off(), (double) 0, (long) 1);
			}			
			if(entity.getSubledgerId() != null && entity.getSubledgerId() > 0) {
				subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(),entity.getSubledgerId(), (long) 2, (double) 0, (double)entity.getRound_off(), (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
			}			
		}
	/*	SalesEntry entry = salesEntryDao.findOne(entity.getSalesEntryId());

		if (entry.getReceipts() != null && entry.getReceipts().size() > 0) {

			for (Receipt receipt : entry.getReceipts()) {
				if (receipt.getGst_applied() == 2) {
					
					 * if (receipt.getPayment_type() == 1) { 
					 * SubLedger subledgercash = subledgerDao.findOne("Cash In hand", entity.getCompany().getCompany_id()); 
					 * if(subledgercash != null) {
					 * subledgerService.addsubledgertransactionbalance(entity.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, entity.getRound_off(), (double) 0, (long) 1);
					 * 
					 * } } else {
					 * bankService.addbanktransactionbalance(entity.getCompany().getCompany_id(),
					 * receipt.getBank().getBank_id(), (long) 3,entity.getRound_off(),(Double) 0,
					 * (long) 1); }
					 
				}
				if (receipt.getGst_applied() == 1) {
					if (receipt.getPayment_type() == 1) {
						
						 * SubLedger subledgercash = subledgerDao.findOne("Cash In hand", entity.getCompany().getCompany_id());
						 * 
						 * if (subledgercash != null) {
						 * subledgerService.addsubledgertransactionbalance(entity.getCompany().getCompany_id(), subledgercash.getSubledger_Id(), (long) 2, entity.getTransaction_value_head(), (double) 0, (long) 1); }
						 
					}
					else {						
						 bankService.addbanktransactionbalance(entity.getCompany().getCompany_id(), receipt.getBank().getBank_id(), (long) 3,entity.getRound_off(),(Double) 0, (long) 1);
					}
							
				}
           	}
		}*/
		if (creditDetailsList != null && creditDetailsList.size() > 0) {
		
			if (entity.getCGST_head() != null && entity.getCGST_head() > 0) {
				SubLedger subledgercgst = subledgerDao.findOne("Output CGST", entity.getCompany().getCompany_id());
	
				if (subledgercgst != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getCGST_head(), (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
				}
	
			}
			if (entity.getSGST_head() != null && entity.getSGST_head() > 0) {
				SubLedger subledgersgst = subledgerDao.findOne("Output SGST", entity.getCompany().getCompany_id());
	
				if (subledgersgst != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getSGST_head(), (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
				}
			}
	
			if (entity.getIGST_head() != null && entity.getIGST_head() > 0) {
				SubLedger subledgerigst = subledgerDao.findOne("Output IGST", entity.getCompany().getCompany_id());
	
				if (subledgerigst != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getIGST_head(), (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
				}
			}
	
			if (entity.getSCT_head() != null && entity.getSCT_head() > 0) {
				SubLedger subledgercess = subledgerDao.findOne("Output CESS", entity.getCompany().getCompany_id());
	
				if (subledgercess != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getSCT_head(), (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
				}
			}
	
			if (entity.getTotal_vat() != null && entity.getTotal_vat() > 0) {
				SubLedger subledgervat = subledgerDao.findOne("Output VAT", entity.getCompany().getCompany_id());
	
				if (subledgervat != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_vat(), (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
				}
			}
			if (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() > 0) {
				SubLedger subledgercst = subledgerDao.findOne("Output CST", entity.getCompany().getCompany_id());
	
				if (subledgercst != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_vatcst(), (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
				}
			}
			if (entity.getTotal_excise() != null && entity.getTotal_excise() > 0) {
				SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", entity.getCompany().getCompany_id());
	
				if (subledgerexcise != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTotal_excise(), (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
				}
			}
		}
	}

	@Override
	public void update(CreditNote entity) throws MyWebException {
		Boolean flag = false;

		CreditNote creditNote = new CreditNote();
		CreditNote preCreditNote = new CreditNote();
		try {
			creditNote = creditNoteDao.findOneView(entity.getCredit_no_id());
			creditNote.setFlag(true);

			if(creditNote.getSubledger()!=null)
			{
			preCreditNote.setSubledger(creditNote.getSubledger());
			}
			if(creditNote.getCustomer()!=null)
			{
			preCreditNote.setCustomer(creditNote.getCustomer());
			}
			if(creditNote.getSales_bill_id()!=null)
			{
			preCreditNote.setSales_bill_id(creditNote.getSales_bill_id());
			}
			if(creditNote.getDescription()!=null)
			{
			preCreditNote.setDescription(creditNote.getDescription());
			}
		
			preCreditNote.setCompany(creditNote.getCompany());
			if(creditNote.getDate()!=null)
			{
			preCreditNote.setDate(creditNote.getDate());
			}
			if(creditNote.getAccounting_year_id()!=null)
			{
			preCreditNote.setAccounting_year_id(accountingYearDao.findOne(creditNote.getAccounting_year_id().getYear_id()));
			}
			
			if(creditNote.getRound_off()!=null)
			{
			preCreditNote.setRound_off(creditNote.getRound_off());
			}
			if (creditNote.getTransaction_value_head() != null)
				preCreditNote.setTransaction_value_head(creditNote.getTransaction_value_head());
			
			if (creditNote.getTds_amount() != null)
				preCreditNote.setTds_amount(creditNote.getTds_amount());
				
			if (creditNote.getCGST_head() != null)
				preCreditNote.setCGST_head(creditNote.getCGST_head());
			
			if (creditNote.getSGST_head() != null)
				preCreditNote.setSGST_head(creditNote.getSGST_head());
			
			if (creditNote.getIGST_head() != null)
				preCreditNote.setIGST_head(creditNote.getIGST_head());
			
			if (creditNote.getSCT_head() != null)
				preCreditNote.setSCT_head(creditNote.getSCT_head());
			
			if (creditNote.getTotal_vat() != null)
				preCreditNote.setTotal_vat(creditNote.getTotal_vat());
			
			if (creditNote.getTotal_vatcst() != null)
				preCreditNote.setTotal_vatcst(creditNote.getTotal_vatcst());
			
			if (creditNote.getTotal_excise() != null)
				preCreditNote.setTotal_excise(creditNote.getTotal_excise());
			
			if(creditNote.getCreditDetails() != null || creditNote.getCreditDetails().size() > 0)
				preCreditNote.setCreditDetails(creditNote.getCreditDetails());

			try
			{
			if (entity.getCreditNoteDetails() !=null && entity.getCreditNoteDetails() != "") {
				JSONArray jsonArray = new JSONArray(entity.getCreditNoteDetails());
				Set<CreditDetails> creditDetailsList = new HashSet<CreditDetails>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					CreditDetails creditDetails = new CreditDetails();
					if (json.getInt("id") == 0) {
						flag = true;
						creditDetails.setCgst(Double.parseDouble(Double.toString(json.getDouble("cgst"))));
						creditDetails.setIgst(Double.parseDouble(Double.toString(json.getDouble("igst"))));
						creditDetails.setSgst(Double.parseDouble(Double.toString(json.getDouble("sgst"))));
						creditDetails.setState_com_tax(Double.parseDouble(Double.toString(json.getDouble("state_com_tax"))));
						creditDetails.setVAT(Double.parseDouble(Double.toString(json.getDouble("VAT"))));
						creditDetails.setVATCST(Double.parseDouble(Double.toString(json.getDouble("VATCST"))));
						creditDetails.setExcise(Double.parseDouble(Double.toString(json.getDouble("Excise"))));
						creditDetails.setIs_gst(Long.parseLong(Integer.toString(json.getInt("is_gst"))));
						creditDetails.setDiscount(Double.parseDouble(Double.toString(json.getDouble("discount"))));
						creditDetails.setFrieght(Double.parseDouble(Double.toString(json.getDouble("freight"))));
						creditDetails.setLabour_charges(Double.parseDouble(Double.toString(json.getDouble("labour_charges"))));
						creditDetails.setOthers(Double.parseDouble(Double.toString(json.getDouble("others"))));
						creditDetails.setQuantity(json.getDouble("quantity"));
						creditDetails.setRate(Double.parseDouble(Double.toString(json.getDouble("rate"))));
						creditDetails.setTransaction_amount(Double.parseDouble(json.getString("transactionAmount")));
						Product product = productDao.findOne(Long.parseLong(Integer.toString(json.getInt("productId"))));
						creditDetails.setProduct_id(product);
						UnitOfMeasurement uom = uomDao.findOne(Long.parseLong(Integer.toString(json.getInt("uomId"))));
						creditDetails.setUom_id(uom);
						creditDetails.setCredit_id(creditNote);
						if(entity.getDescription() != 2 && entity.getDescription() != 3) {
							
							if(!product.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
						     {
							 Integer pflag=productDao.checktype(creditNote.getCompany().getCompany_id(),Long.parseLong(json.getString("productId")));
						     if(pflag==1)  
						     {
							      Stock stock = null;
							      stock=stockService.isstockexist(creditNote.getCompany().getCompany_id(),Long.parseLong(json.getString("productId")));
							      double amt=Double.parseDouble(json.getString("quantity"))*Double.parseDouble(json.getString("rate"));						     
							      if(stock!=null)
									{
							    	    stock.setAmount(stock.getAmount()+amt);
										stock.setQuantity(stock.getQuantity()+Double.parseDouble(json.getString("quantity")));					
										stockService.updateStock(stock);
										stockService.addStockInfoOfProduct(stock.getStock_id(), creditNote.getCompany().getCompany_id(), Long.parseLong(json.getString("productId")), Double.parseDouble(json.getString("quantity")), Double.parseDouble(json.getString("rate")));
									}
						     }	
						     }		
						}
						creditDetailsList.add(creditDetails);
						
					}
					else {
						creditDetails = creditNoteDao.getCreditDetailsById(Long.parseLong(Integer.toString(json.getInt("id"))));

						creditDetails.setCgst(Double.parseDouble(Double.toString(json.getDouble("cgst"))));
						creditDetails.setIgst(Double.parseDouble(Double.toString(json.getDouble("igst"))));
						creditDetails.setSgst(Double.parseDouble(Double.toString(json.getDouble("sgst"))));
						creditDetails.setState_com_tax(Double.parseDouble(Double.toString(json.getDouble("state_com_tax"))));
						creditDetails.setVAT(Double.parseDouble(Double.toString(json.getDouble("VAT"))));
						creditDetails.setVATCST(Double.parseDouble(Double.toString(json.getDouble("VATCST"))));
						creditDetails.setExcise(Double.parseDouble(Double.toString(json.getDouble("Excise"))));
						creditDetails.setIs_gst(Long.parseLong(Integer.toString(json.getInt("is_gst"))));
						creditDetails.setDiscount(Double.parseDouble(Double.toString(json.getDouble("discount"))));
						creditDetails.setFrieght(Double.parseDouble(Double.toString(json.getDouble("freight"))));
						creditDetails.setLabour_charges(Double.parseDouble(Double.toString(json.getDouble("labour_charges"))));
						creditDetails.setOthers(Double.parseDouble(Double.toString(json.getDouble("others"))));
						creditDetails.setQuantity(json.getDouble("quantity"));
						creditDetails.setRate(Double.parseDouble(Double.toString(json.getDouble("rate"))));
						
						creditNoteDao.updateCreditDetail(creditDetails);
					}
				}
				if (flag)
					creditNote.setCreditDetails(creditDetailsList);
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			if(entity.getRound_off()!=null)
			{
			creditNote.setRound_off(entity.getRound_off());
			}
			if (entity.getTransaction_value_head() != null)
			{
			creditNote.setTransaction_value_head(entity.getTransaction_value_head());
			}
			if (entity.getTds_amount() != null)
			{
			creditNote.setTds_amount(entity.getTds_amount());
			}
			if(entity.getCGST_head()!=null)
			{
			creditNote.setCGST_head(entity.getCGST_head());
			}
		
			if(entity.getSGST_head()!=null)
			{
			creditNote.setSGST_head(entity.getSGST_head());
			}
			if(entity.getIGST_head()!=null)
			{
			creditNote.setIGST_head(entity.getIGST_head());
			}
			if(entity.getSCT_head()!=null)
			{
			creditNote.setSCT_head(entity.getSCT_head());
			}
			if(entity.getTotal_vat()!=null)
			{
			creditNote.setTotal_vat(entity.getTotal_vat());
			}
			if(entity.getTotal_vatcst()!=null)
			{
			creditNote.setTotal_vatcst(entity.getTotal_vatcst());
			}
			if(entity.getTotal_excise()!=null)
			{
			creditNote.setTotal_excise(entity.getTotal_excise());
			}
			if(entity.getCustomerId()!=null)
			{
			creditNote.setCustomer(customerDao.findOne(entity.getCustomerId()));
			}
			if(entity.getSubledgerId()!=null)
			{
			creditNote.setSubledger(subledgerDao.findOne(entity.getSubledgerId()));
			}
			if(entity.getSalesEntryId()!=null)
			{
			creditNote.setSales_bill_id(salesEntryDao.findOne(entity.getSalesEntryId()));
			}
			if(entity.getDateString()!=null)
			{
			creditNote.setDate(new LocalDate(entity.getDateString()));
			}
			if(creditNote.getAccounting_year_id()!=null)
			{
			creditNote.setAccounting_year_id(accountingYearDao.findOne(creditNote.getAccounting_year_id().getYear_id()));
			}
			if(creditNote.getStatus()!=null)
			{
			creditNote.setStatus(entity.getStatus());
			}
			creditNote.setUpdated_date(new LocalDate());
			if(creditNote.getDescription()!=null)
			{
			creditNote.setDescription(entity.getDescription());
			}
			if(entity.getDescription()==7)
			creditNote.setRemark(entity.getRemark());
			if(entity.getUpdated_by()!=null)
			{
				creditNote.setUpdated_by(entity.getUpdated_by());
			}
			creditNoteDao.update(creditNote);
			CreditNote note = creditNoteDao.findOne(creditNote.getCredit_no_id());
			
			if(note.getSales_bill_id() != null){
				SalesEntry salesEntry = salesEntryDao.findOneWithAll(note.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
				Double total=(double) 0;
				for (CreditNote note1 : salesEntry.getCreditNote()) {
						total += note1.getRound_off();				
				}
			
				if(salesEntry.getRound_off().equals(total)) {
					salesEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
				}
				else if(total>=salesEntry.getRound_off())
				{
					salesEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
				}
				else{
					salesEntry.setEntry_status(MyAbstractController.ENTRY_PENDING);			
				}
				try {
					salesEntryDao.update(salesEntry);
				} catch (MyWebException e) {
					e.printStackTrace();
				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try
		{
		
		if(preCreditNote.getDescription() != 2 && preCreditNote.getDescription() != 3) {
			if (preCreditNote.getCustomer()!=null) {
				customerService.addcustomertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),preCreditNote.getCompany().getCompany_id(), preCreditNote.getCustomer().getCustomer_id(),(long) 5, (double)preCreditNote.getRound_off(), (double) 0, (long) 0);
			}			
			if(preCreditNote.getSubledger()!=null) {
				subledgerService.addsubledgertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),preCreditNote.getCompany().getCompany_id(),preCreditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)preCreditNote.getTransaction_value_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}	
			if (preCreditNote.getTds_amount() != null && preCreditNote.getTds_amount() != 0) {
				SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", preCreditNote.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),preCreditNote.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)preCreditNote.getTds_amount(), (double) 0, (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		}else {		
			if (preCreditNote.getCustomer()!=null) {
				customerService.addcustomertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),creditNote.getCompany().getCompany_id(), preCreditNote.getCustomer().getCustomer_id(),(long) 5, (double)preCreditNote.getRound_off(), (double) 0, (long) 0);
			}			
			if(preCreditNote.getSubledger()!=null) {
				subledgerService.addsubledgertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),entity.getCompany().getCompany_id(),preCreditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)preCreditNote.getRound_off(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}			
		}
		
		if(creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
			if (creditNote.getCustomer()!=null) {
				customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)creditNote.getRound_off(), (double) 0, (long) 1);
			}			
			if(creditNote.getSubledger()!=null) {
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTransaction_value_head(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}	
			if (creditNote.getTds_amount() != null && creditNote.getTds_amount() != 0) {
				SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", creditNote.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)creditNote.getTds_amount(), (double) 0, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		}else {		
			if (creditNote.getCustomer()!=null) {
				customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)creditNote.getRound_off(), (double) 0, (long) 1);
			}			
			if(creditNote.getSubledgerId() != null && creditNote.getSubledgerId() > 0) {
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getRound_off(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}			
		}
		
		if((preCreditNote.getDescription() != 2 && preCreditNote.getDescription() != 3) || (creditNote.getDescription() != 2 && creditNote.getDescription() != 3)) {
			if(preCreditNote.getDescription() != 2 && preCreditNote.getDescription() != 3) {
				if (preCreditNote.getCGST_head() != null && preCreditNote.getCGST_head() > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Output CGST", preCreditNote.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),preCreditNote.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)preCreditNote.getCGST_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (preCreditNote.getSGST_head() != null && preCreditNote.getSGST_head() > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Output SGST", preCreditNote.getCompany().getCompany_id());

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),preCreditNote.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)preCreditNote.getSGST_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (preCreditNote.getIGST_head() != null && preCreditNote.getIGST_head() > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Output IGST", preCreditNote.getCompany().getCompany_id());
					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),preCreditNote.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)preCreditNote.getIGST_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (preCreditNote.getSCT_head() != null && preCreditNote.getSCT_head() > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Output CESS", preCreditNote.getCompany().getCompany_id());
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),preCreditNote.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double) 0,(double) preCreditNote.getSCT_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (preCreditNote.getTotal_vat() != null && preCreditNote.getTotal_vat() > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Output VAT", preCreditNote.getCompany().getCompany_id());
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),preCreditNote.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)preCreditNote.getTotal_vat(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (preCreditNote.getTotal_vatcst() != null && preCreditNote.getTotal_vatcst() > 0) {

					SubLedger subledgercst = subledgerDao.findOne("Output CST", preCreditNote.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),preCreditNote.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)preCreditNote.getTotal_vatcst(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (preCreditNote.getTotal_excise() != null && preCreditNote.getTotal_excise() > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", preCreditNote.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(preCreditNote.getAccounting_year_id().getYear_id(),preCreditNote.getDate(),preCreditNote.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)preCreditNote.getTotal_excise(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
			}
			
			if(creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
				if (creditNote.getCGST_head() != null && creditNote.getCGST_head() > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Output CGST", creditNote.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getCGST_head(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getSGST_head() != null && creditNote.getSGST_head() > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Output SGST", creditNote.getCompany().getCompany_id());

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getSGST_head(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getIGST_head() != null && creditNote.getIGST_head() > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Output IGST", creditNote.getCompany().getCompany_id());
					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getIGST_head(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getSCT_head() != null && creditNote.getSCT_head() > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Output CESS", creditNote.getCompany().getCompany_id());
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getSCT_head(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getTotal_vat() != null && creditNote.getTotal_vat() > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Output VAT", creditNote.getCompany().getCompany_id());
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_vat(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getTotal_vatcst() != null && creditNote.getTotal_vatcst() > 0) {

					SubLedger subledgercst = subledgerDao.findOne("Output CST", creditNote.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_vatcst(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getTotal_excise() != null && creditNote.getTotal_excise() > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", creditNote.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_excise(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
			}
			
		}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public List<CreditNote> list() {
		return creditNoteDao.findAll();
	}

	@Override
	public CreditNote getById(Long id) throws MyWebException {
		return creditNoteDao.findOne(id);
	}

	@Override
	public CreditNote getById(String id) throws MyWebException {
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
	public void remove(CreditNote entity) throws MyWebException {
		// TODO Auto-generated method stub
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(CreditNote entity) throws MyWebException {
		// TODO Auto-generated method stub
	}

	@Override
	public CreditNote isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CreditNote> getReport(Long id, LocalDate from_date, LocalDate to_date, Long companyId) {
		return creditNoteDao.getReport(id, from_date, to_date, companyId);
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		try {
			CreditNote creditNote = creditNoteDao.findOneView(entityId);
			
			if(creditNote.getDescription() !=null && creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
				if (creditNote.getCustomer()!=null && creditNote.getRound_off()!=null) {
					customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)creditNote.getRound_off(), (double) 0, (long) 0);
				}			
				if(creditNote.getSubledger()!=null && creditNote.getTransaction_value_head()!=null) {
					subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTransaction_value_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				}
				if (creditNote.getTds_amount() != null && creditNote.getTds_amount() != 0) {
					SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", creditNote.getCompany().getCompany_id());
					subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)creditNote.getTds_amount(), (double) 0, (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				}
			}else {		
				if (creditNote.getCustomer()!=null && creditNote.getRound_off()!=null) {
					customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)creditNote.getRound_off(), (double) 0, (long) 0);
				}			
				if(creditNote.getSubledger()!=null && creditNote.getRound_off()!=null) {
					subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getRound_off(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				}			
			}

			/*SalesEntry entry = salesEntryDao.findOne(creditNote.getSales_bill_id().getSales_id());*/
			
			if (creditNote.getCreditDetails() != null && creditNote.getCreditDetails().size() > 0) {
				if (creditNote.getCGST_head() != null && creditNote.getCGST_head() > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Output CGST", creditNote.getCompany().getCompany_id());

					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getCGST_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}

				}
				if (creditNote.getSGST_head() != null && creditNote.getSGST_head() > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Output SGST", creditNote.getCompany().getCompany_id());

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getSGST_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}

				if (creditNote.getIGST_head() != null && creditNote.getIGST_head() > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Output IGST", creditNote.getCompany().getCompany_id());

					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getIGST_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}

				if (creditNote.getSCT_head() != null && creditNote.getSCT_head() > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Output CESS",
							creditNote.getCompany().getCompany_id());

					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getSCT_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}

				if (creditNote.getTotal_vat() != null && creditNote.getTotal_vat() > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Output VAT", creditNote.getCompany().getCompany_id());

					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_vat(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getTotal_vatcst() != null && creditNote.getTotal_vatcst() > 0) {
					SubLedger subledgercst = subledgerDao.findOne("Output CST", creditNote.getCompany().getCompany_id());

					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_vatcst(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getTotal_excise() != null && creditNote.getTotal_excise() > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", creditNote.getCompany().getCompany_id());

					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_excise(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
			}
			for (CreditDetails creditDetails : creditNote.getCreditDetails()) {
				if(creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
					
					 if(!creditDetails.getProduct_id().getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
					 {
					 Integer pflag=productDao.checktype(creditNote.getCompany().getCompany_id(),creditDetails.getProduct_id().getProduct_id());
				     if(pflag==1)  
				     {
					      Stock stock = null;
					      stock=stockService.isstockexist(creditNote.getCompany().getCompany_id(),creditDetails.getProduct_id().getProduct_id());
					      if(stock!=null)
							{
					    	    double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), creditNote.getCompany().getCompany_id(), creditDetails.getProduct_id().getProduct_id(), (Double)creditDetails.getQuantity());
					    	    stock.setAmount(stock.getAmount()-amount);
								stock.setQuantity(stock.getQuantity()-creditDetails.getQuantity());					
								stockService.updateStock(stock);
							}
							
				     }	
				   }
				     
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return creditNoteDao.deleteByIdValue(entityId);
	}

	@Override
	public List<CreditNote> findAllCreditNoteOfCompany(Long CompanyId, Boolean flag) {
		return creditNoteDao.findAllCreditNoteOfCompany(CompanyId, flag);
	}

	@Override
	public Long deleteByCebitNoteDetailsId(Long entityId, Long companyId) {
		CreditDetails creditDetails = creditNoteDao.getCreditDetailsById(entityId);
		Long creditNoteId = creditDetails.getCredit_id().getCredit_no_id();
		CreditNote creditNote = creditNoteDao.findOneView(creditNoteId);
		
		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double state_comp_tax = (double)0;
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double excise = (double)0;
		Double transaction_value = (double)0;
		Double round_off = (double)0;
		Double tdsforoneproduct = (double)0;
		Double tds = (double)0;
		Double preCGST = (double)0;
		Double preIGST = (double)0;
		Double preSGST = (double)0;
		Double preSCT = (double)0;
		Double preVAT = (double)0;
		Double preVATCST = (double)0;
		Double preExcise = (double)0;
		Double pretransaction_amount=(double)0;
		Double preRound_off = (double)0;
		
		// Set previous creditNote detail values
		if(creditDetails.getCgst()!=null){
			preCGST=creditDetails.getCgst();
		}
		if(creditDetails.getIgst()!=null){
			preIGST=creditDetails.getIgst();
		}
		if(creditDetails.getSgst()!=null){
			preSGST=creditDetails.getSgst();
		}
		if(creditDetails.getState_com_tax()!=null){
			preSCT=creditDetails.getState_com_tax();
		}
		if(creditDetails.getVAT()!=null){
			preVAT=creditDetails.getVAT();
		}
		if(creditDetails.getVATCST()!=null){
			preVATCST=creditDetails.getVATCST();
		}
		if(creditDetails.getExcise()!=null) {
			preExcise=creditDetails.getExcise();
		}
		if(creditDetails.getTransaction_amount()!=null){
			pretransaction_amount=creditDetails.getTransaction_amount();
		}
			
		// Set creditNote values
		if(creditNote.getTransaction_value_head()!=null) {
			transaction_value= creditNote.getTransaction_value_head();
		}
		if(creditNote.getCGST_head()!=null){
			cgst=creditNote.getCGST_head();
		}
		if(creditNote.getIGST_head()!=null) {
			igst=creditNote.getIGST_head();
		}
		if(creditNote.getSGST_head()!=null){
			sgst=creditNote.getSGST_head();
		}
		if(creditNote.getSCT_head()!=null){
			state_comp_tax=creditNote.getSCT_head();
		}
		if(creditNote.getTotal_vat()!=null){
			vat=creditNote.getTotal_vat();
		}
		if(creditNote.getTotal_vatcst()!=null){
			vatcst=creditNote.getTotal_vatcst();
		}
		if(creditNote.getTotal_excise()!=null) {
			excise=creditNote.getTotal_excise();
		}
		if(creditNote.getRound_off()!=null){
			round_off=creditNote.getRound_off();
		}
		if(creditNote.getTds_amount()!=null){
			tds=creditNote.getTds_amount();
		}
		
		cgst = cgst-preCGST;
		igst = igst-preIGST;
		sgst = sgst-preSGST;
		state_comp_tax = state_comp_tax - preSCT;
		vat = vat - preVAT;
		vatcst = vatcst - preVATCST;
		excise = excise - preExcise;
		transaction_value=transaction_value - pretransaction_amount;
		
		if(creditNote.getCustomer()!=null && creditNote.getCustomer().getTds_applicable()!=null)
		{
			if(creditNote.getCustomer().getTds_applicable().equals(1) && creditNote.getCustomer().getTds_rate()!=null)
			{
				tdsforoneproduct = (pretransaction_amount*creditNote.getCustomer().getTds_rate())/100;
			}
		}
		
		preRound_off = pretransaction_amount + preCGST + preIGST + preSGST + preSCT + preVAT + preVATCST + preExcise-tdsforoneproduct;
		tds=tds-tdsforoneproduct;
		round_off = round_off - preRound_off;
		
		
		if (creditNote.getCustomer() != null) {
			customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),companyId, creditNote.getCustomer().getCustomer_id(), (long) 5, (double)preRound_off, (double) 0, (long) 0);
		}
		
		if(creditNote.getSubledger() != null) {
			subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),companyId,creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)pretransaction_amount,(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
		}
		
		if (tdsforoneproduct!= 0) {
			SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", creditNote.getCompany().getCompany_id());
			subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)tdsforoneproduct, (double) 0, (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
		}
		
		if(preCGST!=null && preCGST>0){
			SubLedger subledgercgst = subledgerDao.findOne("Output CGST",companyId);
			
			if(subledgercgst!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),companyId,subledgercgst.getSubledger_Id(),(long) 2,(double) 0, (double)preCGST, (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		}
		if(preSGST!=null && preSGST>0){
			SubLedger subledgersgst = subledgerDao.findOne("Output SGST",companyId);
			
			if(subledgersgst!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),companyId,subledgersgst.getSubledger_Id(),(long) 2,(double) 0,(double)preSGST,(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		}
		if(preIGST!=null && preIGST>0){
			SubLedger subledgerigst = subledgerDao.findOne("Output IGST",companyId);
			
			if(subledgerigst!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),companyId,subledgerigst.getSubledger_Id(),(long) 2,(double) 0,(double)preIGST,(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		}
		if(preSCT != null && preSCT > 0){
			SubLedger subledgercess = subledgerDao.findOne("Output CESS",companyId);
			
			if(subledgercess!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),companyId,subledgercess.getSubledger_Id(),(long) 2,(double) 0,(double)preSCT,(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}	
		}
		if(preVAT!=null && preVAT>0){
			SubLedger subledgervat = subledgerDao.findOne("Output VAT",companyId);
			
			if(subledgervat!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),companyId,subledgervat.getSubledger_Id(),(long) 2,(double) 0,(double)preVAT,(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		}
		if(preVATCST!=null && preVATCST>0){
			SubLedger subledgercst = subledgerDao.findOne("Output CST",companyId);
			
			if(subledgercst!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),companyId,subledgercst.getSubledger_Id(),(long) 2,(double) 0,(double)preVATCST,(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		}
		if(preExcise!=null && preExcise>0){
			SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE",companyId);
			
			if(subledgerexcise!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),companyId,subledgerexcise.getSubledger_Id(),(long) 2,(double) 0,(double)preExcise,(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		}
		if(creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
			
			 if(!creditDetails.getProduct_id().getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
			 {
			 Integer pflag=productDao.checktype(creditNote.getCompany().getCompany_id(),creditDetails.getProduct_id().getProduct_id());
		     if(pflag==1)  
		     {
			      Stock stock = null;
			      stock=stockService.isstockexist(creditNote.getCompany().getCompany_id(),creditDetails.getProduct_id().getProduct_id());
			      if(stock!=null)
					{
			    	    double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), creditNote.getCompany().getCompany_id(), creditDetails.getProduct_id().getProduct_id(), (Double)creditDetails.getQuantity());
			    	    stock.setAmount(stock.getAmount()-amount);
						stock.setQuantity(stock.getQuantity()-creditDetails.getQuantity());					
						stockService.updateStock(stock);
					}
					
		     }	
		   }
					
		}
		try {			
			creditNote.setCGST_head(round(cgst,2));
			creditNote.setIGST_head(round(igst,2));
			creditNote.setSGST_head(round(sgst,2));
			creditNote.setSCT_head(round(state_comp_tax,2));
			creditNote.setTotal_vat(vat);
			creditNote.setTotal_vatcst(vatcst);
			creditNote.setTotal_excise(excise);
			creditNote.setTransaction_value_head(transaction_value);
			creditNote.setRound_off(round_off);
			creditNote.setTds_amount(tds);
			creditNoteDao.update(creditNote);
			creditNoteDao.deleteByCebitNoteDetailsId(entityId);
			
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		return creditNoteId;
	}

	@Override
	public CreditNote findOneView(Long creditNoteId) {
		return creditNoteDao.findOneView(creditNoteId);
	}

	@Override
	public List<CreditNote> getReportAgainstSubledger(Long id, LocalDate from_date, LocalDate to_date, Long companyId) {
		return creditNoteDao.getReportAgainstSubledger(id, from_date, to_date, companyId);
	}

	@Override
	public List<CreditNote> getDayBookReport(LocalDate from_date,LocalDate to_date, Long companyId) {
		return creditNoteDao.getDayBookReport(from_date, to_date,companyId);
	}

	@Override
	public List<CreditNote> findAllCreditNotesOfCompany(Long CompanyId) {

		return creditNoteDao.findAllCreditNotesOfCompany(CompanyId);
	}

	@Override
	public void updateCreditNoteThroughExcel(CreditNote note) {
		creditNoteDao.updateCreditNoteThroughExcel(note);

	}

	@Override
	public void updateCreditDetailsThroughExcel(Long year_id, LocalDate date,CreditDetails creditDetails, Long credit_id, Long companyId) {
		creditNoteDao.updateCreditDetailsThroughExcel(creditDetails, credit_id);
	}

	@Override
	public Long saveCreditNoteThroughExcel(CreditNote entity) 
	{
		return creditNoteDao.saveCreditNoteThroughExcel(entity);
	}
	
	public static Double round(Double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public void changeStatusOfCreditNoteThroughExcel(CreditNote note) {
		creditNoteDao.changeStatusOfCreditNoteThroughExcel(note);
		
	}

	@Override
	public CreditDetails getCreditDetails(Long id) {
		return creditNoteDao.getCreditDetailsById(id);
	}

	@Override
	public void updateCreditDetails(CreditDetails creditDetails, long company_id) {
		
		
		CreditDetails crDetails = creditNoteDao.getCreditDetailsById(creditDetails.getCredit_detail_id());
		CreditNote creditNote= creditNoteDao.findOneView(crDetails.getCredit_id().getCredit_no_id());

		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double state_comp_tax = (double)0;
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double excise = (double)0;
		Double transaction_value = (double)0;
		Double round_off = (double)0;
		Double pretds  = (double)0;
		Double newtds  = (double)0;
		Double tds  = (double)0;
		Double preCGST = (double)0, newCGST = (double)0;
		Double preIGST = (double)0, newIGST = (double)0;
		Double preSGST = (double)0, newSGST = (double)0;
		Double preSCT = (double)0, newSCT = (double)0;
		Double preVAT = (double)0, newVAT = (double)0;
		Double preVATCST = (double)0, newVATCST = (double)0;
		Double preExcise = (double)0, newExcise = (double)0;
		Double pretransaction_amount=(double)0, newtransaction_amount=(double)0;
		Double preRound_off = (double)0, newRound_off = (double)0;
	
		// Set previous creditNote detail values
		if(crDetails.getCgst()!=null){
			preCGST=crDetails.getCgst();
		}
		if(crDetails.getIgst()!=null){
			preIGST=crDetails.getIgst();
		}
		if(crDetails.getSgst()!=null){
			preSGST=crDetails.getSgst();
		}
		if(crDetails.getState_com_tax()!=null){
			preSCT=crDetails.getState_com_tax();
		}
		if(crDetails.getTransaction_amount()!=null){
			pretransaction_amount=crDetails.getTransaction_amount();
		}
		if(crDetails.getVAT()!=null){
			preVAT=crDetails.getVAT();
		}
		if(crDetails.getVATCST()!=null){
			preVATCST=crDetails.getVATCST();
		}
		if(crDetails.getExcise()!=null) {
			preExcise=crDetails.getExcise();
		}
		
		// Set new credit detail values
		if(creditDetails.getCgst()!=null){
			newCGST=creditDetails.getCgst();
		}
		if(creditDetails.getIgst()!=null){
			newIGST=creditDetails.getIgst();
		}
		if(creditDetails.getSgst()!=null){
			newSGST=creditDetails.getSgst();
		}
		if(creditDetails.getState_com_tax()!=null){
			newSCT=creditDetails.getState_com_tax();
		}
		if(creditDetails.getTransaction_amount()!=null){
			newtransaction_amount=creditDetails.getTransaction_amount();
		}
		if(creditDetails.getVAT()!=null){
			newVAT=creditDetails.getVAT();
		}
		if(creditDetails.getVATCST()!=null){
			newVATCST=creditDetails.getVATCST();
		}
		if(creditDetails.getExcise()!=null) {
			newExcise=creditDetails.getExcise();
		}
		
		
		// Set credit note values
		if(creditNote.getTransaction_value_head()!=null) {
			transaction_value= creditNote.getTransaction_value_head();
		}
		if(creditNote.getTds_amount()!=null)
		{
			tds=creditNote.getTds_amount();
		}
		
		if(creditNote.getCGST_head()!=null){
			cgst=creditNote.getCGST_head();
		}
		if(creditNote.getIGST_head()!=null) {
			igst=creditNote.getIGST_head();
		}
		if(creditNote.getSGST_head()!=null){
			sgst=creditNote.getSGST_head();
		}
		if(creditNote.getSCT_head()!=null){
			state_comp_tax=creditNote.getSCT_head();
		}
		if(creditNote.getRound_off()!=null){
			round_off=creditNote.getRound_off();
		}
		if(creditNote.getTotal_vat()!=null){
			vat=creditNote.getTotal_vat();
		}
		if(creditNote.getTotal_vatcst()!=null){
			vatcst=creditNote.getTotal_vatcst();
		}
		if(creditNote.getTotal_excise()!=null) {
			excise=creditNote.getTotal_excise();
		}
		if(preVAT==0 && preVATCST==0 && preExcise==0)
		{
		cgst = cgst-preCGST + newCGST;
		igst = igst-preIGST + newIGST;
		sgst = sgst-preSGST + newSGST;
		state_comp_tax = state_comp_tax - preSCT + newSCT;
		}
		else
		{
		vat = vat - preVAT + newVAT;
		vatcst = vatcst - preVATCST + newVATCST;
		excise = excise - preExcise + newExcise;
		}
		transaction_value=transaction_value - pretransaction_amount + newtransaction_amount;
		
		if(creditNote.getCustomer()!=null && creditNote.getCustomer().getTds_applicable()!=null)
		{
			if(creditNote.getCustomer().getTds_applicable().equals(1) && creditNote.getCustomer().getTds_rate()!=null)
			{
				pretds = (pretransaction_amount*creditNote.getCustomer().getTds_rate())/100;
			}
		}
		if(creditNote.getCustomer()!=null && creditNote.getCustomer().getTds_applicable()!=null)
		{
			if(creditNote.getCustomer().getTds_applicable().equals(1) && creditNote.getCustomer().getTds_rate()!=null)
			{
				newtds = (newtransaction_amount*creditNote.getCustomer().getTds_rate())/100;
			}
		}
	
		tds=tds-pretds+newtds;

		preRound_off = pretransaction_amount + preCGST + preIGST + preSGST + preSCT + preVAT + preVATCST + preExcise-pretds;
		newRound_off = newtransaction_amount + newCGST + newIGST + newSGST + newSCT + newVAT + newVATCST + newExcise-newtds;
		
		round_off = round_off - preRound_off + newRound_off;
		if(creditNote.getDescription() !=null && creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {			
					if(creditNote.getTransaction_value_head()!=null && creditNote.getTransaction_value_head() != transaction_value) {
						if (creditNote.getCustomer()!=null && creditNote.getRound_off()!=null) {
							customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)preRound_off, (double) 0, (long) 0);
							customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)newRound_off, (double) 0, (long) 1);
						}
						
						if (creditNote.getTds_amount() != null && creditNote.getTds_amount() != 0) {
							SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", creditNote.getCompany().getCompany_id());
							subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)pretds, (double) 0, (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
						}


			            if (creditNote.getTds_amount() != null && creditNote.getTds_amount() != 0) {
							SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", creditNote.getCompany().getCompany_id());
							subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)newtds, (double) 0, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
						}
						
						if(creditNote.getSubledger()!=null && creditNote.getTransaction_value_head()!=null) {
							subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)pretransaction_amount, (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)newtransaction_amount, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
						}			
					}
			}
			/*else
			{
				if(creditNote.getRound_off() !=null && creditNote.getRound_off() != newRound_off) {
					if (creditNote.getCustomer()!=null && creditNote.getRound_off()!=null) {
						customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)creditNote.getRound_off(), (double) 0, (long) 0);
						customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)round_off, (double) 0, (long) 1);
					}			
					if(creditNote.getSubledger()!=null  && creditNote.getRound_off()!=null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getRound_off(), (long) 0,null,null,null,null,creditNote,null,null);
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)round_off, (long) 1,null,null,null,null,creditNote,null,null);
					}			
				}
			}*/
					
		
			SubLedger subledgercgst = subledgerDao.findOne("Output CGST",company_id);
			if(subledgercgst!=null){
				
				if(creditNote.getCGST_head()!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double) 0,(double)creditNote.getCGST_head(),(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				}
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double) 0,(double)cgst,(long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		
			 SubLedger subledgersgst = subledgerDao.findOne("Output SGST",company_id);
				
			 if(subledgersgst!=null){
				 if(creditNote.getSGST_head()!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double) 0,(double)creditNote.getSGST_head(),(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double) 0,(double)sgst,(long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			 }
	
			 SubLedger subledgerigst = subledgerDao.findOne("Output IGST",company_id);
			
			 if(subledgerigst!=null){
				 if(creditNote.getIGST_head()!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double) 0,(double)creditNote.getIGST_head(),(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double) 0,(double)igst,(long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				
			 }
		
			SubLedger subledgercess = subledgerDao.findOne("Output CESS",company_id);
			
			if(subledgercess!=null){
				 if(creditNote.getSCT_head()!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double) 0,(double)creditNote.getSCT_head(),(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double) 0,(double)state_comp_tax,(long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
	
			SubLedger subledgervat = subledgerDao.findOne("Output VAT",company_id);
			
			if(subledgervat!=null){
				
				 if(creditNote.getTotal_vat()!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double) 0,(double)creditNote.getTotal_vat(),(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double) 0,(double)vat,(long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		
			SubLedger subledgercst = subledgerDao.findOne("Output CST",company_id);
			
			if(subledgercst!=null){
				 if(creditNote.getTotal_vatcst()!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double) 0,(double)creditNote.getTotal_vatcst(),(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double) 0,(double)vatcst,(long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		
			SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE",company_id);
			
			if(subledgerexcise!=null){
				 if(creditNote.getTotal_excise()!=null){
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double) 0,(double)creditNote.getTotal_excise(),(long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double) 0,(double)excise,(long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
			}
		
		if(creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
			/* Integer pflag=productDao.checktype(creditNote.getCompany().getCompany_id(),creditDetails.getProductId());
		     if(pflag==1)  
		     {
			      Stock stock = null;
			      Double stupdate;
			      stupdate=crDetails.getQuantity()-creditDetails.getQuantity();
			      stock=stockService.isstockexist(creditNote.getCompany().getCompany_id(),creditDetails.getProductId());
			      double amt=creditDetails.getQuantity()*creditDetails.getRate();
			      if(stock==null)
					{
						Stock stockdata = new Stock();
						stockdata.setCompany_id(creditNote.getCompany().getCompany_id());
						stockdata.setProduct_id(creditDetails.getProductId());
						stockdata.setQuantity(stupdate);
						stockdata.setAmount(amt);
						stockService.saveStock(stockdata);
					}
					else
					{					
						stock.setAmount(amt);
						stock.setQuantity((stock.getQuantity()-crDetails.getQuantity())+creditDetails.getQuantity());					
						stockService.updateStock(stock);
					}
		     }*/		
		}
		try {
			crDetails.setCgst(newCGST);
			crDetails.setIgst(newIGST);
			crDetails.setSgst(newSGST);
			crDetails.setState_com_tax(newSCT);
			crDetails.setVAT(newVAT);
			crDetails.setVATCST(newVATCST);
			crDetails.setExcise(newExcise);
			crDetails.setDiscount(creditDetails.getDiscount());
			crDetails.setFrieght(creditDetails.getFrieght());
			crDetails.setLabour_charges(creditDetails.getLabour_charges());
			crDetails.setOthers(creditDetails.getOthers());
			crDetails.setQuantity(creditDetails.getQuantity());
			crDetails.setRate(creditDetails.getRate());
			crDetails.setTransaction_amount(creditDetails.getTransaction_amount());
			creditNoteDao.updateCreditDetail(crDetails);
			
			creditNote.setCGST_head(round(cgst,2));
			creditNote.setIGST_head(round(igst,2));
			creditNote.setSGST_head(round(sgst,2));
			creditNote.setSCT_head(round(state_comp_tax,2));
			creditNote.setTotal_vat(vat);
			creditNote.setTotal_vatcst(vatcst);
			creditNote.setTotal_excise(excise);
			creditNote.setTransaction_value_head(transaction_value);
			creditNote.setRound_off(round_off);
			creditNote.setTds_amount(tds);
			creditNoteDao.update(creditNote);
			
			CreditNote note = creditNoteDao.findOne(creditNote.getCredit_no_id());
			
			if(note.getSales_bill_id() != null){
				SalesEntry salesEntry = salesEntryDao.findOneWithAll(note.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
				Double total=(double) 0;
				for (CreditNote note1 : salesEntry.getCreditNote()) {
						total += note1.getRound_off();				
				}
			
				if(salesEntry.getRound_off().equals(total)) {
					salesEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
				}
				else if(total>=salesEntry.getRound_off())
				{
					salesEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
				}
				else{
					salesEntry.setEntry_status(MyAbstractController.ENTRY_PENDING);			
				}
				try {
					salesEntryDao.update(salesEntry);
				} catch (MyWebException e) {
					e.printStackTrace();
				}

			}
			
			
		} catch (MyWebException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<CreditNote> findBySalesId(long id) {
		
		// TODO Auto-generated method stub
		return creditNoteDao.findBySalesId(id);
	}

	@Override
	public void diactivateByIdValue(Long entityId) {
		try {
			CreditNote creditNote = creditNoteDao.findOneView(entityId);

			if(creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
				if (creditNote.getCustomer()!=null) {
					customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)creditNote.getRound_off(), (double) 0, (long) 0);
				}			
				if(creditNote.getSubledger()!=null) {
					subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTransaction_value_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				}			
			}else {		
				if (creditNote.getCustomer()!=null) {
					customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)creditNote.getRound_off(), (double) 0, (long) 0);
				}			
				if(creditNote.getSubledger()!=null) {
					subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getRound_off(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				}			
			}
			
			if (creditNote.getCreditDetails() != null && creditNote.getCreditDetails().size() > 0) {
				if (creditNote.getCGST_head() != null && creditNote.getCGST_head() > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Output CGST", creditNote.getCompany().getCompany_id());

					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getCGST_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}

				}
				if (creditNote.getSGST_head() != null && creditNote.getSGST_head() > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Output SGST", creditNote.getCompany().getCompany_id());

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getSGST_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}

				if (creditNote.getIGST_head() != null && creditNote.getIGST_head() > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Output IGST", creditNote.getCompany().getCompany_id());

					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getIGST_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}

				if (creditNote.getSCT_head() != null && creditNote.getSCT_head() > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Output CESS",
							creditNote.getCompany().getCompany_id());

					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getSCT_head(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}

				if (creditNote.getTotal_vat() != null && creditNote.getTotal_vat() > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Output VAT", creditNote.getCompany().getCompany_id());

					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_vat(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getTotal_vatcst() != null && creditNote.getTotal_vatcst() > 0) {
					SubLedger subledgercst = subledgerDao.findOne("Output CST", creditNote.getCompany().getCompany_id());

					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_vatcst(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getTotal_excise() != null && creditNote.getTotal_excise() > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", creditNote.getCompany().getCompany_id());

					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_excise(), (long) 0,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
			}
			for (CreditDetails creditDetails : creditNote.getCreditDetails()) {
				if(creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
					
					 if(!creditDetails.getProduct_id().getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
					 {
					 Integer pflag=productDao.checktype(creditNote.getCompany().getCompany_id(),creditDetails.getProduct_id().getProduct_id());
				     if(pflag==1)  
				     {
					      Stock stock = null;
					      stock=stockService.isstockexist(creditNote.getCompany().getCompany_id(),creditDetails.getProduct_id().getProduct_id());
					      if(stock!=null)
							{
					    	    double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), creditNote.getCompany().getCompany_id(), creditDetails.getProduct_id().getProduct_id(), (Double)creditDetails.getQuantity());
					    	    stock.setAmount(stock.getAmount()-amount);
								stock.setQuantity(stock.getQuantity()-creditDetails.getQuantity());					
								stockService.updateStock(stock);
							}
							
				     }	
				   }
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		creditNoteDao.diactivateByIdValue(entityId);
	}

	@Override
	public void activateByIdValue(Long entityId) {
		try {
			CreditNote creditNote = creditNoteDao.findOneView(entityId);

			if(creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
				if (creditNote.getCustomer()!=null) {
					customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)creditNote.getRound_off(), (double) 0, (long) 1);
				}			
				if(creditNote.getSubledger()!=null) {
					subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTransaction_value_head(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				}			
			}else {		
				if (creditNote.getCustomer()!=null) {
					customerService.addcustomertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), creditNote.getCustomer().getCustomer_id(),(long) 5, (double)creditNote.getRound_off(), (double) 0, (long) 1);
				}			
				if(creditNote.getSubledger()!=null) {
					subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(),creditNote.getSubledger().getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getRound_off(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
				}			
			}

			/*SalesEntry entry = salesEntryDao.findOne(creditNote.getSales_bill_id().getSales_id());*/
			
			if (creditNote.getCreditDetails() != null && creditNote.getCreditDetails().size() > 0) {
				if (creditNote.getCGST_head() != null && creditNote.getCGST_head() > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Output CGST", creditNote.getCompany().getCompany_id());

					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getCGST_head(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}

				}
				if (creditNote.getSGST_head() != null && creditNote.getSGST_head() > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Output SGST", creditNote.getCompany().getCompany_id());

					if (subledgersgst != null) {
					}
				}

				if (creditNote.getIGST_head() != null && creditNote.getIGST_head() > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Output IGST", creditNote.getCompany().getCompany_id());

					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getIGST_head(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}

				if (creditNote.getSCT_head() != null && creditNote.getSCT_head() > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Output CESS",
							creditNote.getCompany().getCompany_id());

					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getSCT_head(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}

				if (creditNote.getTotal_vat() != null && creditNote.getTotal_vat() > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Output VAT", creditNote.getCompany().getCompany_id());

					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_vat(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getTotal_vatcst() != null && creditNote.getTotal_vatcst() > 0) {
					SubLedger subledgercst = subledgerDao.findOne("Output CST", creditNote.getCompany().getCompany_id());

					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_vatcst(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
				if (creditNote.getTotal_excise() != null && creditNote.getTotal_excise() > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", creditNote.getCompany().getCompany_id());

					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)creditNote.getTotal_excise(), (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
					}
				}
			}
			for (CreditDetails creditDetails : creditNote.getCreditDetails()) {
				if(creditNote.getDescription() != 2 && creditNote.getDescription() != 3) {
				
					 if(!creditDetails.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
				     {
				        Integer pflag=productDao.checktype(creditNote.getCompany().getCompany_id(),creditDetails.getProduct_id().getProduct_id());
					     if(pflag==1)
					     {
					      Stock stock = null;
					      stock=stockService.isstockexist(creditNote.getCompany().getCompany_id(),creditDetails.getProduct_id().getProduct_id());
					      double amt=creditDetails.getQuantity()*creditDetails.getRate();					     
					      if(stock!=null)
							{
					    	    stock.setAmount(stock.getAmount()+amt);
								stock.setQuantity(stock.getQuantity()+(Double)creditDetails.getQuantity());					
								stockService.updateStock(stock);
								stockService.addStockInfoOfProduct(stock.getStock_id(), creditNote.getCompany().getCompany_id(), creditDetails.getProduct_id().getProduct_id(), (Double)creditDetails.getQuantity(), creditDetails.getRate());
					       }
				        }	
				    }		
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		creditNoteDao.activateByIdValue(entityId);
		
	}

	@Override
	public List<CreditNote> getCreditNoteForLedgerReport(LocalDate from_date, LocalDate to_date, Long customerId,
			Long companyId) {
		// TODO Auto-generated method stub
		return creditNoteDao.getCreditNoteForLedgerReport(from_date, to_date, customerId, companyId);
	}

	@Override
	public CreditNote isExcelVocherNumberExist(String vocherNo, Long companyId) {
		// TODO Auto-generated method stub
		return creditNoteDao.isExcelVocherNumberExist(vocherNo, companyId);
	}

	@Override
	public List<CreditNote> getCreditNoteForSale(Long companyId,LocalDate toDate) {
		// TODO Auto-generated method stub
		return creditNoteDao.getCreditNoteForSale(companyId,toDate);
	}

	@Override
	public List<CreditNote> getAllOpeningBalanceAgainstcreditnote(Long customerId, Long companyId) {
		// TODO Auto-generated method stub
		return creditNoteDao.getAllOpeningBalanceAgainstcreditnote( customerId, companyId);
	}

	@Override
	public List<CreditNote> getAllOpeningBalanceAgainstCreditNoteForPeriod(Long customerId, Long companyId,
			LocalDate toDate) {
		return creditNoteDao.getAllOpeningBalanceAgainstCreditNoteForPeriod(customerId, companyId, toDate);
	}
}