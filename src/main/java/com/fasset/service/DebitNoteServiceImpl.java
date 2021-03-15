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
import com.fasset.dao.interfaces.IDebitNoteDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.IPurchaseEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.ISupplierDAO;
import com.fasset.dao.interfaces.IUnitOfMeasurementDAO;
import com.fasset.entities.CreditDetails;
import com.fasset.entities.DebitDetails;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Payment;
import com.fasset.entities.Product;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Stock;
import com.fasset.entities.SubLedger;
import com.fasset.entities.UnitOfMeasurement;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IDebitNoteService;
import com.fasset.service.interfaces.IStockService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;

@Service
@Transactional
public class DebitNoteServiceImpl implements IDebitNoteService {

	@Autowired
	private IDebitNoteDAO debitNoteDao;

	@Autowired
	private ISupplierDAO supplierDao;

	@Autowired
	private IPurchaseEntryDAO purchaseEntryDao;

	@Autowired
	private IAccountingYearDAO accountingYearDao;

	@Autowired
	private IProductDAO productDao;

	@Autowired
	private IUnitOfMeasurementDAO uomDao;

	@Autowired
	private ISubLedgerDAO subledgerDao;

	@Autowired
	ISuplliersService supplierService;

	@Autowired
	ISubLedgerService subledgerService;

	@Autowired
	IBankService bankService;

	@Autowired
	private IStockService stockService;
	
	@Override
	public void add(DebitNote entity) throws MyWebException {
		Set<DebitDetails> debitDetailsList = new HashSet<DebitDetails>();
		try {
			entity.setDate(new LocalDate(entity.getDateString()));
			entity.setSupplier(supplierDao.findOne(entity.getSupplierId()));
			entity.setSubledger(subledgerDao.findOne(entity.getSubledgerId()));
			entity.setPurchase_bill_id(purchaseEntryDao.findOne(entity.getPurchaseEntryId()));
			entity.setYearId(entity.getYearId());
			entity.setAccounting_year_id(accountingYearDao.findOne(entity.getYearId()));
			entity.setCreated_date(new LocalDate());
			entity.setEntry_status(MyAbstractController.ENTRY_PENDING);
			if (entity.getDebitNoteDetails() != "") {
				JSONArray jsonArray = new JSONArray(entity.getDebitNoteDetails());

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					DebitDetails debitDetails = new DebitDetails();
					debitDetails.setCgst(Double.parseDouble(json.getString("cgst")));
					debitDetails.setIgst(Double.parseDouble(json.getString("igst")));
					debitDetails.setSgst(Double.parseDouble(json.getString("sgst")));
					debitDetails.setState_com_tax(Double.parseDouble(json.getString("state_com_tax")));
					debitDetails.setVAT(Double.parseDouble(json.getString("VAT")));
					debitDetails.setVATCST(Double.parseDouble(json.getString("VATCST")));
					debitDetails.setExcise(Double.parseDouble(json.getString("Excise")));
					debitDetails.setIs_gst(Long.parseLong(json.getString("is_gst")));
					debitDetails.setDiscount(Double.parseDouble(json.getString("discount")));
					debitDetails.setFrieght(Double.parseDouble(json.getString("freight")));
					debitDetails.setLabour_charges(Double.parseDouble(json.getString("labour_charges")));
					debitDetails.setOthers(Double.parseDouble(json.getString("others")));
					debitDetails.setQuantity(Double.parseDouble(json.getString("quantity")));
					debitDetails.setRate(Double.parseDouble(json.getString("rate")));
					debitDetails.setTransaction_amount(Double.parseDouble(json.getString("transactionAmount")));
					Product product = productDao.findOne(Long.parseLong(json.getString("productId")));
					debitDetails.setProduct_id(product);
					UnitOfMeasurement uom = uomDao.findOne(Long.parseLong(json.getString("uomId")));
					debitDetails.setUom_id(uom);
					debitDetails.setDebit_id(entity);
					//Stock
					if(entity.getDescription() != 2 && entity.getDescription() != 3) 
					{
				    	 if(!product.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
					    {
						 Integer pflag=productDao.checktype(entity.getCompany().getCompany_id(),Long.parseLong(json.getString("productId")));
					     if(pflag==1)  
					     {
						      Stock stock = null;
						      stock=stockService.isstockexist(entity.getCompany().getCompany_id(),Long.parseLong(json.getString("productId")));					     
						      if(stock!=null)
								{
						    	    double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), entity.getCompany().getCompany_id(), Long.parseLong(json.getString("productId")), Double.parseDouble(json.getString("quantity")));
						    	    stock.setAmount(stock.getAmount()-amount);
									stock.setQuantity(stock.getQuantity()-Double.parseDouble(json.getString("quantity")));					
									stockService.updateStock(stock);
								}
					     }	
					    }
					}
					debitDetailsList.add(debitDetails);
				}
				entity.setDebitDetails(debitDetailsList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(entity.getPurchaseEntryId().equals((long)-2))
		{
			entity.setAgainstOpeningBalnce(true);
		}
		else
		{
			entity.setAgainstOpeningBalnce(false);
		}
		entity.setLocal_time(new LocalTime());
		entity.setEntry_status(MyAbstractController.ENTRY_PENDING);
		Long id = debitNoteDao.saveDebitNoteThroughExcel(entity);	
		DebitNote note = debitNoteDao.findOne(id);
		
		if(note.getPurchase_bill_id() != null){				
			PurchaseEntry purchaseEntry = purchaseEntryDao.findOneWithAll(note.getPurchase_bill_id().getPurchase_id());
			Double total =(double) 0;
			for (DebitNote note1 : purchaseEntry.getDebitNote()) {
					total += note1.getRound_off();
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
		
		if(entity.getDescription() != 2 && entity.getDescription() != 3) {
			if (entity.getSupplierId() != null && entity.getSupplierId() > 0) {
				supplierService.addsuppliertrsansactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getSupplierId(), (long) 4, (double) 0, (double)entity.getRound_off(), (long) 1);
			}
			if(entity.getSubledgerId() != null && entity.getSubledgerId() > 0) {
				subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getSubledgerId(), (long) 2, (double)entity.getTransaction_value_head(), (double) 0, (long) 1,null,null,null,null,null,note,null,null,null,null,null,null);
			}
			if(entity.getTds_amount()!=null && entity.getTds_amount()>0)
			{
				SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", entity.getCompany().getCompany_id());
				if (subledgerTds != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(),
							subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)entity.getTds_amount(), (long) 1,null,null,null,null,null,note,null,null,null,null,null,null);
				}
			}
			
		}else {
			if (entity.getSupplierId() != null && entity.getSupplierId() > 0) {
				supplierService.addsuppliertrsansactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getSupplierId(), (long) 4, (double) 0, (double)entity.getRound_off(), (long) 1);
			}
			if(entity.getSubledgerId() != null && entity.getSubledgerId() > 0) {
				subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), entity.getSubledgerId(), (long) 2, (double)entity.getRound_off(), (double) 0, (long) 1,null,null,null,null,null,note,null,null,null,null,null,null);
			}
			
		}

		
		if (debitDetailsList != null && debitDetailsList.size() > 0) {
			if (entity.getCGST_head() != null && entity.getCGST_head() > 0) {
				SubLedger subledgercgst = subledgerDao.findOne("Input CGST", entity.getCompany().getCompany_id());
				if (subledgercgst != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)entity.getCGST_head(), (double) 0, (long) 1,null,null,null,null,null,note,null,null,null,null,null,null);
				}
			}
			if (entity.getSGST_head() != null && entity.getSGST_head() > 0) {
				SubLedger subledgersgst = subledgerDao.findOne("Input SGST", entity.getCompany().getCompany_id());

				if (subledgersgst != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)entity.getSGST_head(), (double) 0, (long) 1,null,null,null,null,null,note,null,null,null,null,null,null);
				}
			}
			if (entity.getIGST_head() != null && entity.getIGST_head() > 0) {
				SubLedger subledgerigst = subledgerDao.findOne("Input IGST", entity.getCompany().getCompany_id());
				if (subledgerigst != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)entity.getIGST_head(), (double) 0, (long) 1,null,null,null,null,null,note,null,null,null,null,null,null);
				}
			}
			if (entity.getSCT_head() != null && entity.getSCT_head() > 0) {
				SubLedger subledgercess = subledgerDao.findOne("Input CESS", entity.getCompany().getCompany_id());
				if (subledgercess != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)entity.getSCT_head(), (double) 0, (long) 1,null,null,null,null,null,note,null,null,null,null,null,null);
				}
			}
			if (entity.getTotal_vat() != null && entity.getTotal_vat() > 0) {
				SubLedger subledgervat = subledgerDao.findOne("Input VAT", entity.getCompany().getCompany_id());
				if (subledgervat != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)entity.getTotal_vat(), (double) 0, (long) 1,null,null,null,null,null,note,null,null,null,null,null,null);
				}
			}
			if (entity.getTotal_vatcst() != null && entity.getTotal_vatcst() > 0) {

				SubLedger subledgercst = subledgerDao.findOne("Input CST", entity.getCompany().getCompany_id());
				if (subledgercst != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)entity.getTotal_vatcst(), (double) 0, (long) 1,null,null,null,null,null,note,null,null,null,null,null,null);
				}
			}
			if (entity.getTotal_excise() != null && entity.getTotal_excise() > 0) {
				SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", entity.getCompany().getCompany_id());
				if (subledgerexcise != null) {
					subledgerService.addsubledgertransactionbalance(entity.getAccounting_year_id().getYear_id(),entity.getDate(),entity.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)entity.getTotal_excise(), (double) 0, (long) 1,null,null,null,null,null,note,null,null,null,null,null,null);
				}
			}
		}
		
		/*PurchaseEntry entry = purchaseEntryDao.findOne(entity.getPurchaseEntryId());

		if (entry.getPayments() != null) {
			for (Payment payment : entry.getPayments()) {
				if (payment.getGst_applied() == false) {
					
					 * if (payment.getPayment_type() == 1) { SubLedger subledgercgst =
					 * subledgerDao.findOne("Cash In hand", entity.getCompany().getCompany_id());
					 * subledgerService.addsubledgertransactionbalance(entity.getCompany().
					 * getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (Double)
					 * 0,entity.getRound_off(), (long) 1); } else {
					 * 
					 * bankService.addbanktransactionbalance(entity.getCompany().getCompany_id(),
					 * payment.getBank().getBank_id(), (long) 3,(Double) 0 , entity.getRound_off(),
					 * (long) 1); }
					 
				}

				if (payment.getGst_applied() == true) {
					if (payment.getPayment_type() == 1) {
						
						 * SubLedger subledgercash = subledgerDao.findOne("Cash In hand",
						 * entity.getCompany().getCompany_id());
						 * subledgerService.addsubledgertransactionbalance(entity.getCompany().
						 * getCompany_id(), subledgercash.getSubledger_Id(), (long) 2,(Double) 0 ,
						 * entity.getRound_off(), (long) 1);
						 

						

					} else {
						
						 * bankService.addbanktransactionbalance(entity.getCompany().getCompany_id(),
						 * payment.getBank().getBank_id(), (long) 3,(Double) 0 ,
						 * entity.getTransaction_value_head(), (long) 1);
						 
					}
				}
			}
		}*/
	}

	@Override
	public void update(DebitNote entity) throws MyWebException {
		Boolean flag = false;

		DebitNote debitNote = new DebitNote();
		DebitNote preDebitNote = new DebitNote();
		try {
			debitNote = debitNoteDao.findOneView(entity.getDebit_no_id());
			debitNote.setFlag(true);
			
			if(debitNote.getSupplier()!=null)
			{
			preDebitNote.setSupplier(debitNote.getSupplier());
			}
			if(debitNote.getSubledger()!=null)
			{
			preDebitNote.setSubledger(debitNote.getSubledger());
			}
			if(debitNote.getPurchase_bill_id()!=null)
			{
			preDebitNote.setPurchase_bill_id(debitNote.getPurchase_bill_id());
			}
			
			if(debitNote.getDescription()!=null)
			{
			preDebitNote.setDescription(debitNote.getDescription());
			}
			if(debitNote.getCompany()!=null)
			{
			preDebitNote.setCompany(debitNote.getCompany());
			}
			if(debitNote.getDate()!=null)
			{
			preDebitNote.setDate(debitNote.getDate());
			}
			if(debitNote.getAccounting_year_id()!=null)
			{
			preDebitNote.setAccounting_year_id(accountingYearDao.findOne(debitNote.getAccounting_year_id().getYear_id()));
			}
			if(debitNote.getRound_off()!=null)
			{
			preDebitNote.setRound_off(debitNote.getRound_off());
			}
			if (debitNote.getTransaction_value_head() != null)
				preDebitNote.setTransaction_value_head(debitNote.getTransaction_value_head());
			
			if (debitNote.getTds_amount() != null)
				preDebitNote.setTds_amount(debitNote.getTds_amount());
			
			if (debitNote.getCGST_head() != null)
				preDebitNote.setCGST_head(debitNote.getCGST_head());
			
			if (debitNote.getSGST_head() != null)
				preDebitNote.setSGST_head(debitNote.getSGST_head());
			
			if (debitNote.getIGST_head() != null)
				preDebitNote.setIGST_head(debitNote.getIGST_head());
			
			if (debitNote.getSCT_head() != null)
				preDebitNote.setSCT_head(debitNote.getSCT_head());
			
			if (debitNote.getTotal_vat() != null)
				preDebitNote.setTotal_vat(debitNote.getTotal_vat());
			
			if (debitNote.getTotal_vatcst() != null)
				preDebitNote.setTotal_vatcst(debitNote.getTotal_vatcst());
			
			if (debitNote.getTotal_excise() != null)
				preDebitNote.setTotal_excise(debitNote.getTotal_excise());
			
			if(debitNote.getDebitDetails() != null || debitNote.getDebitDetails().size() > 0)
				preDebitNote.setDebitDetails(debitNote.getDebitDetails());

			if (entity.getDebitNoteDetails()!=null && entity.getDebitNoteDetails() != "") {
				JSONArray jsonArray = new JSONArray(entity.getDebitNoteDetails());
				Set<DebitDetails> debitDetailsList = new HashSet<DebitDetails>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					DebitDetails debitDetails = new DebitDetails();
					if (json.getInt("id") == 0) {
						flag = true;
						debitDetails.setCgst(Double.parseDouble(Double.toString(json.getDouble("cgst"))));
						debitDetails.setIgst(Double.parseDouble(Double.toString(json.getDouble("igst"))));
						debitDetails.setSgst(Double.parseDouble(Double.toString(json.getDouble("sgst"))));
						debitDetails.setState_com_tax(Double.parseDouble(Double.toString(json.getDouble("state_com_tax"))));
						debitDetails.setVAT(Double.parseDouble(Double.toString(json.getDouble("VAT"))));
						debitDetails.setVATCST(Double.parseDouble(Double.toString(json.getDouble("VATCST"))));
						debitDetails.setExcise(Double.parseDouble(Double.toString(json.getDouble("Excise"))));
						debitDetails.setIs_gst(Long.parseLong(Integer.toString(json.getInt("is_gst"))));
						debitDetails.setDiscount(Double.parseDouble(Double.toString(json.getDouble("discount"))));
						debitDetails.setFrieght(Double.parseDouble(Double.toString(json.getDouble("freight"))));
						debitDetails.setLabour_charges(Double.parseDouble(Double.toString(json.getDouble("labour_charges"))));
						debitDetails.setOthers(Double.parseDouble(Double.toString(json.getDouble("others"))));
						debitDetails.setQuantity(Double.parseDouble(Double.toString(json.getDouble("quantity"))));
						debitDetails.setRate(Double.parseDouble(Double.toString(json.getDouble("rate"))));
						debitDetails.setTransaction_amount(Double.parseDouble(json.getString("transactionAmount")));
						Product product = productDao.findOne(Long.parseLong(Integer.toString(json.getInt("productId"))));
						debitDetails.setProduct_id(product);
						UnitOfMeasurement uom = uomDao.findOne(Long.parseLong(Integer.toString(json.getInt("uomId"))));
						debitDetails.setUom_id(uom);
						debitDetails.setDebit_id(debitNote);
						//Stock
						if(entity.getDescription() != 2 && entity.getDescription() != 3) 
						{
							 if(!product.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
							    {
								 Integer pflag=productDao.checktype(debitNote.getCompany().getCompany_id(),Long.parseLong(json.getString("productId")));
							     if(pflag==1)  
							     {
								      Stock stock = null;
								      stock=stockService.isstockexist(debitNote.getCompany().getCompany_id(),Long.parseLong(json.getString("productId")));					     
								      if(stock!=null)
										{
								    	    double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), debitNote.getCompany().getCompany_id(), Long.parseLong(json.getString("productId")), Double.parseDouble(json.getString("quantity")));
								    	    stock.setAmount(stock.getAmount()-amount);
											stock.setQuantity(stock.getQuantity()-Double.parseDouble(json.getString("quantity")));					
											stockService.updateStock(stock);
										}
							     }	
							    }	
						}
						debitDetailsList.add(debitDetails);
					}
					else {
						debitDetails = debitNoteDao.getDebitDetailsById(Long.parseLong(Integer.toString(json.getInt("id"))));

						debitDetails.setCgst(Double.parseDouble(Double.toString(json.getDouble("cgst"))));
						debitDetails.setIgst(Double.parseDouble(Double.toString(json.getDouble("igst"))));
						debitDetails.setSgst(Double.parseDouble(Double.toString(json.getDouble("sgst"))));
						debitDetails.setState_com_tax(Double.parseDouble(Double.toString(json.getDouble("state_com_tax"))));
						debitDetails.setVAT(Double.parseDouble(Double.toString(json.getDouble("VAT"))));
						debitDetails.setVATCST(Double.parseDouble(Double.toString(json.getDouble("VATCST"))));
						debitDetails.setExcise(Double.parseDouble(Double.toString(json.getDouble("Excise"))));
						debitDetails.setIs_gst(Long.parseLong(Integer.toString(json.getInt("is_gst"))));
						debitDetails.setDiscount(Double.parseDouble(Double.toString(json.getDouble("discount"))));
						debitDetails.setFrieght(Double.parseDouble(Double.toString(json.getDouble("freight"))));
						debitDetails.setLabour_charges(Double.parseDouble(Double.toString(json.getDouble("labour_charges"))));
						debitDetails.setOthers(Double.parseDouble(Double.toString(json.getDouble("others"))));
						debitDetails.setQuantity(Double.parseDouble(Double.toString(json.getDouble("quantity"))));
						debitDetails.setRate(Double.parseDouble(Double.toString(json.getDouble("rate"))));
						
						debitNoteDao.updateDebitDetail(debitDetails);
					}
				}
				if (flag)
					debitNote.setDebitDetails(debitDetailsList);
			}
		
			if(entity.getRound_off()!=null)
			{
				debitNote.setRound_off(entity.getRound_off());
			}
			if (entity.getTransaction_value_head() != null)
				debitNote.setTransaction_value_head(entity.getTransaction_value_head());
			
			if (entity.getTds_amount() != null)
			{
				debitNote.setTds_amount(entity.getTds_amount());
			}
			
			if(entity.getCGST_head()!=null)
			{
			debitNote.setCGST_head(entity.getCGST_head());
			}
			if(entity.getSGST_head()!=null)
			{
			debitNote.setSGST_head(entity.getSGST_head());
			}
			if(entity.getIGST_head()!=null)
			{
			debitNote.setIGST_head(entity.getIGST_head());
			}
			if(entity.getSCT_head()!=null)
			{
			debitNote.setSCT_head(entity.getSCT_head());
			}
			if(entity.getTotal_vat()!=null)
			{
			debitNote.setTotal_vat(entity.getTotal_vat());
			}
			if(entity.getTotal_vatcst()!=null)
			{
			debitNote.setTotal_vatcst(entity.getTotal_vatcst());
			}
			if(entity.getTotal_excise()!=null)
			{
			debitNote.setTotal_excise(entity.getTotal_excise());
			}
			
			debitNote.setSupplier(supplierDao.findOne(entity.getSupplierId()));
			debitNote.setSubledger(subledgerDao.findOne(entity.getSubledgerId()));
			debitNote.setPurchase_bill_id(purchaseEntryDao.findOne(entity.getPurchaseEntryId()));
			debitNote.setDate(new LocalDate(entity.getDateString()));
			if(debitNote.getAccounting_year_id()!=null)
			{
			debitNote.setAccounting_year_id(accountingYearDao.findOne(debitNote.getAccounting_year_id().getYear_id()));
			}
			debitNote.setStatus(entity.getStatus());
			debitNote.setUpdated_date(new LocalDate());
			if(entity.getDescription()==7)
			debitNote.setRemark(entity.getRemark());
			if(entity.getUpdated_by()!=null)
			{
				debitNote.setUpdated_by(entity.getUpdated_by());
			}
			debitNoteDao.update(debitNote);
			DebitNote note = debitNoteDao.findOne(debitNote.getDebit_no_id());
			
			if(note.getPurchase_bill_id() != null){				
				PurchaseEntry purchaseEntry = purchaseEntryDao.findOneWithAll(note.getPurchase_bill_id().getPurchase_id());
				Double total =(double) 0;
				for (DebitNote note1 : purchaseEntry.getDebitNote()) {
						total += note1.getRound_off();
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(preDebitNote.getDescription()!=null && preDebitNote.getDescription() != 2 && preDebitNote.getDescription() != 3) {
			if(preDebitNote.getSupplier()!=null && preDebitNote.getRound_off()!=null)
			{
			supplierService.addsuppliertrsansactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), preDebitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double) preDebitNote.getRound_off(), (long) 0);
			}
			
			if(preDebitNote.getTds_amount()!=null && preDebitNote.getTds_amount()>0)
			{
				SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", preDebitNote.getCompany().getCompany_id());
				if (subledgerTds != null) {
					subledgerService.addsubledgertransactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(),
							subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)preDebitNote.getTds_amount(), (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				}
			}
			
			if(preDebitNote.getSubledger()!=null && preDebitNote.getTransaction_value_head()!=null)
			{
			subledgerService.addsubledgertransactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), preDebitNote.getSubledger().getSubledger_Id(), (long) 2, (double) preDebitNote.getTransaction_value_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}else {
			
			if(preDebitNote.getSupplier()!=null && preDebitNote.getRound_off()!=null)
			{
			supplierService.addsuppliertrsansactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), preDebitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double) preDebitNote.getRound_off(), (long) 0);
			}
			if(preDebitNote.getSubledger()!=null && preDebitNote.getRound_off()!=null)
			{
			subledgerService.addsubledgertransactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), preDebitNote.getSubledger().getSubledger_Id(), (long) 2, (double) preDebitNote.getRound_off(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}
		
		if(debitNote.getDescription()!=null && debitNote.getDescription() != 2 && debitNote.getDescription() != 3) {
			
			if(debitNote.getSupplier()!=null && debitNote.getRound_off()!=null)
			{
			supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double) debitNote.getRound_off(), (long) 1);
			}
			if(debitNote.getTds_amount()!=null && debitNote.getTds_amount()>0)
			{
				SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", debitNote.getCompany().getCompany_id());
				if (subledgerTds != null) {
					subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(),
							subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)debitNote.getTds_amount(), (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				}
			}
			if(debitNote.getSubledger()!=null && debitNote.getTransaction_value_head()!=null)
			{
			subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSubledger().getSubledger_Id(), (long) 2, (double) debitNote.getTransaction_value_head(), (double) 0, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}else {
			if(debitNote.getSupplier()!=null && debitNote.getRound_off()!=null)
			{
			supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double) debitNote.getRound_off(), (long) 1);
			}
			if(debitNote.getSubledger()!=null && debitNote.getRound_off()!=null)
			{
			subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSubledger().getSubledger_Id(), (long) 2, (double) debitNote.getRound_off(), (double) 0, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}
		
		
		
		if((preDebitNote.getDescription() !=null && preDebitNote.getDescription() != 2 && preDebitNote.getDescription() != 3) || (debitNote.getDescription()!=null && debitNote.getDescription() != 2 && debitNote.getDescription() != 3)) {
			if(preDebitNote.getDescription() != 2 && preDebitNote.getDescription() != 3) {
				if (preDebitNote.getCGST_head() != null && preDebitNote.getCGST_head() > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Input CGST", preDebitNote.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double) preDebitNote.getCGST_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (preDebitNote.getSGST_head() != null && preDebitNote.getSGST_head() > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Input SGST", preDebitNote.getCompany().getCompany_id());

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double) preDebitNote.getSGST_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (preDebitNote.getIGST_head() != null && preDebitNote.getIGST_head() > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Input IGST", preDebitNote.getCompany().getCompany_id());
					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double) preDebitNote.getIGST_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (preDebitNote.getSCT_head() != null && preDebitNote.getSCT_head() > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Input CESS", preDebitNote.getCompany().getCompany_id());
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double) preDebitNote.getSCT_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (preDebitNote.getTotal_vat() != null && preDebitNote.getTotal_vat() > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Input VAT", preDebitNote.getCompany().getCompany_id());
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double) preDebitNote.getTotal_vat(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (preDebitNote.getTotal_vatcst() != null && preDebitNote.getTotal_vatcst() > 0) {

					SubLedger subledgercst = subledgerDao.findOne("Input CST", preDebitNote.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double) preDebitNote.getTotal_vatcst(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (preDebitNote.getTotal_excise() != null && preDebitNote.getTotal_excise() > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", preDebitNote.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(preDebitNote.getAccounting_year_id().getYear_id(),preDebitNote.getDate(),preDebitNote.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double) preDebitNote.getTotal_excise(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
			}
			
			if(debitNote.getDescription() != 2 && debitNote.getDescription() != 3) {
				if (debitNote.getCGST_head() != null && debitNote.getCGST_head() > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Input CGST", debitNote.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double) debitNote.getCGST_head(), (double) 0, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getSGST_head() != null && debitNote.getSGST_head() > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Input SGST", debitNote.getCompany().getCompany_id());

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)debitNote.getSGST_head(), (double) 0, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getIGST_head() != null && debitNote.getIGST_head() > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Input IGST", debitNote.getCompany().getCompany_id());
					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)debitNote.getIGST_head(), (double) 0, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getSCT_head() != null && debitNote.getSCT_head() > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Input CESS", debitNote.getCompany().getCompany_id());
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)debitNote.getSCT_head(), (double) 0, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getTotal_vat() != null && debitNote.getTotal_vat() > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Input VAT", debitNote.getCompany().getCompany_id());
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)debitNote.getTotal_vat(), (double) 0, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getTotal_vatcst() != null && debitNote.getTotal_vatcst() > 0) {

					SubLedger subledgercst = subledgerDao.findOne("Input CST", debitNote.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)debitNote.getTotal_vatcst(), (double) 0, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getTotal_excise() != null && debitNote.getTotal_excise() > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", debitNote.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)debitNote.getTotal_excise(), (double) 0, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
			}
		}
		
	}

	@Override
	public List<DebitNote> list() {
		return debitNoteDao.findAll();
	}

	@Override
	public DebitNote getById(Long id) throws MyWebException {
		return debitNoteDao.findOne(id);
	}

	@Override
	public DebitNote getById(String id) throws MyWebException {
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
	public void remove(DebitNote debitNote) throws MyWebException {
		// TODO Auto-generated method stub
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(DebitNote entity) throws MyWebException {
		// TODO Auto-generated method stub
	}

	@Override
	public DebitNote isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DebitNote> getReport(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {
		return debitNoteDao.getReport(Id, from_date, to_date, comId);
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		try {
			DebitNote debitNote = debitNoteDao.findOneView(entityId);

			if((debitNote.getDescription() !=null && debitNote.getDescription() != 2 && debitNote.getDescription() != 3)) {
				if(debitNote.getSupplier()!=null && debitNote.getRound_off()!=null)
				{
				supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double) debitNote.getRound_off(), (long) 0);
				}
				if(debitNote.getTds_amount()!=null && debitNote.getTds_amount()>0)
				{
					SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", debitNote.getCompany().getCompany_id());
					if (subledgerTds != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(),
								subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)debitNote.getTds_amount(), (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if(debitNote.getSubledger()!=null && debitNote.getTransaction_value_head()!=null)
				{
					subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSubledger().getSubledger_Id(), (long) 2, (double) debitNote.getTransaction_value_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				}
			}else {
				
				if(debitNote.getSupplier()!=null && debitNote.getRound_off()!=null )
				{
				supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double) debitNote.getRound_off(), (long) 0);
				}
				if(debitNote.getSubledger()!=null && debitNote.getRound_off()!=null)
				{
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSubledger().getSubledger_Id(), (long) 2, (double) debitNote.getRound_off(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				}
			}
	
			if (debitNote.getDebitDetails() != null && debitNote.getDebitDetails().size() > 0) {
				if (debitNote.getCGST_head() != null && debitNote.getCGST_head() > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Input CGST", debitNote.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)debitNote.getCGST_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getSGST_head() != null && debitNote.getSGST_head() > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Input SGST", debitNote.getCompany().getCompany_id());

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)debitNote.getSGST_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getIGST_head() != null && debitNote.getIGST_head() > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Input IGST", debitNote.getCompany().getCompany_id());
					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)debitNote.getIGST_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getSCT_head() != null && debitNote.getSCT_head() > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Input CESS", debitNote.getCompany().getCompany_id());
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)debitNote.getSCT_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getTotal_vat() != null && debitNote.getTotal_vat() > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Input VAT", debitNote.getCompany().getCompany_id());
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)debitNote.getTotal_vat(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getTotal_vatcst() != null && debitNote.getTotal_vatcst() > 0) {
					SubLedger subledgercst = subledgerDao.findOne("Input CST", debitNote.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)debitNote.getTotal_vatcst(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getTotal_excise() != null && debitNote.getTotal_excise() > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", debitNote.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)debitNote.getTotal_excise(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
			}
			
			//Stock
			for (DebitDetails debitDetails : debitNote.getDebitDetails()) {
				if(debitNote.getDescription() != 2 && debitNote.getDescription() != 3) 
				{
				   if(!debitDetails.getProduct_id().getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
				   {
					 Integer pflag=productDao.checktype(debitNote.getCompany().getCompany_id(),debitDetails.getProduct_id().getProduct_id());
				     if(pflag==1)  
				     {
					      Stock stock = null;
					      stock=stockService.isstockexist(debitNote.getCompany().getCompany_id(),debitDetails.getProduct_id().getProduct_id());
					      double amt=debitDetails.getQuantity()*debitDetails.getRate();
					      if(stock!=null)
							{
					    	    stock.setAmount(stock.getAmount()+amt);
								stock.setQuantity(stock.getQuantity()+debitDetails.getQuantity());					
								stockService.updateStock(stock);
								stockService.addStockInfoOfProduct(stock.getStock_id(), debitNote.getCompany().getCompany_id(), debitDetails.getProduct_id().getProduct_id(), (double)debitDetails.getQuantity(), debitDetails.getRate());
							}
				     }	
				   }
				}		
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return debitNoteDao.deleteByIdValue(entityId);
	}

	@Override
	public List<DebitNote> findAllDebitNoteOfCompany(Long CompanyId) {
		return debitNoteDao.findAllDebitNoteOfCompany(CompanyId);
	}

	@Override
	public List<DebitNote> cdnrList(Integer month, Long yearId, Long companyId) {
		return debitNoteDao.cdnrList(month, yearId, companyId);
	}

	@Override
	public List<DebitNote> cdnurList(Integer month, Long yearId, Long companyId) {
		return debitNoteDao.cdnurList(month, yearId, companyId);
	}

	@Override
	public Long deleteByDebitNoteDetailsId(Long entityId, Long companyId) {
		DebitDetails debitDetails = debitNoteDao.getDebitDetailsById(entityId);
		Long debitNoteId = debitDetails.getDebit_id().getDebit_no_id();
		DebitNote debitNote = debitNoteDao.findOneView(debitNoteId);
		
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
		
		// Set previous debitNote detail values
		if(debitDetails.getCgst()!=null){
			preCGST=debitDetails.getCgst();
		}
		if(debitDetails.getIgst()!=null){
			preIGST=debitDetails.getIgst();
		}
		if(debitDetails.getSgst()!=null){
			preSGST=debitDetails.getSgst();
		}
		if(debitDetails.getState_com_tax()!=null){
			preSCT=debitDetails.getState_com_tax();
		}
		if(debitDetails.getVAT()!=null){
			preVAT=debitDetails.getVAT();
		}
		if(debitDetails.getVATCST()!=null){
			preVATCST=debitDetails.getVATCST();
		}
		if(debitDetails.getExcise()!=null) {
			preExcise=debitDetails.getExcise();
		}
		if(debitDetails.getTransaction_amount()!=null){
			pretransaction_amount=debitDetails.getTransaction_amount();
		}
			
		// Set debitNote values
		if(debitNote.getTransaction_value_head()!=null) {
			transaction_value= debitNote.getTransaction_value_head();
		}
		if(debitNote.getCGST_head()!=null){
			cgst=debitNote.getCGST_head();
		}
		if(debitNote.getIGST_head()!=null) {
			igst=debitNote.getIGST_head();
		}
		if(debitNote.getSGST_head()!=null){
			sgst=debitNote.getSGST_head();
		}
		if(debitNote.getSCT_head()!=null){
			state_comp_tax=debitNote.getSCT_head();
		}
		if(debitNote.getTotal_vat()!=null){
			vat=debitNote.getTotal_vat();
		}
		if(debitNote.getTotal_vatcst()!=null){
			vatcst=debitNote.getTotal_vatcst();
		}
		if(debitNote.getTotal_excise()!=null) {
			excise=debitNote.getTotal_excise();
		}
		if(debitNote.getRound_off()!=null){
			round_off=debitNote.getRound_off();
		}
		if(debitNote.getTds_amount()!=null){
			tds=debitNote.getTds_amount();
		}
		
		cgst = cgst-preCGST;
		igst = igst-preIGST;
		sgst = sgst-preSGST;
		state_comp_tax = state_comp_tax - preSCT;
		vat = vat - preVAT;
		vatcst = vatcst - preVATCST;
		excise = excise - preExcise;
		transaction_value=transaction_value - pretransaction_amount;
		
		if(debitNote.getSupplier()!=null && debitNote.getSupplier().getTds_applicable()!=null)
		{
			if(debitNote.getSupplier().getTds_applicable().equals(1) && debitNote.getSupplier().getTds_rate()!=null)
			{
				tdsforoneproduct = (pretransaction_amount*debitNote.getSupplier().getTds_rate())/100;
			}
		}
		
		preRound_off = pretransaction_amount + preCGST + preIGST + preSGST + preSCT + preVAT + preVATCST + preExcise-tdsforoneproduct;
		tds=tds-tdsforoneproduct;
		round_off = round_off - preRound_off;
		
		if (debitNote.getSupplier() != null) {
			supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),companyId, debitNote.getSupplier().getSupplier_id(), (long) 5, (double) 0, (double)preRound_off, (long) 0);
		}
		
		if (tdsforoneproduct!= 0) 
		{
			SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", debitNote.getCompany().getCompany_id());
			if (subledgerTds != null) {
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(),
						subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)tdsforoneproduct, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}
		
		if(debitNote.getSubledger() != null) {
			subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),companyId,debitNote.getSubledger().getSubledger_Id(),(long) 2,(double)pretransaction_amount,(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
		}
		
		if(preCGST!=null && preCGST>0){
			SubLedger subledgercgst = subledgerDao.findOne("Input CGST",companyId);
			
			if(subledgercgst!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),companyId,subledgercgst.getSubledger_Id(),(long) 2,(double)preCGST,(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}
		if(preSGST!=null && preSGST>0){
			SubLedger subledgersgst = subledgerDao.findOne("Input SGST",companyId);
			
			if(subledgersgst!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),companyId,subledgersgst.getSubledger_Id(),(long) 2,(double)preSGST,(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}
		if(preIGST!=null && preIGST>0){
			SubLedger subledgerigst = subledgerDao.findOne("Input IGST",companyId);
			
			if(subledgerigst!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),companyId,subledgerigst.getSubledger_Id(),(long) 2,(double)preIGST,(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}
		if(preSCT != null && preSCT > 0){
			SubLedger subledgercess = subledgerDao.findOne("Input CESS",companyId);
			
			if(subledgercess!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),companyId,subledgercess.getSubledger_Id(),(long) 2,(double)preSCT,(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}	
		}
		if(preVAT!=null && preVAT>0){
			SubLedger subledgervat = subledgerDao.findOne("Input VAT",companyId);
			
			if(subledgervat!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),companyId,subledgervat.getSubledger_Id(),(long) 2,(double)preVAT,(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}
		if(preVATCST!=null && preVATCST>0){
			SubLedger subledgercst = subledgerDao.findOne("Input CST",companyId);
			
			if(subledgercst!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),companyId,subledgercst.getSubledger_Id(),(long) 2,(double)preVATCST,(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}
		if(preExcise!=null && preExcise>0){
			SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE",companyId);
			
			if(subledgerexcise!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),companyId,subledgerexcise.getSubledger_Id(),(long) 2,(double)preExcise,(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		}
		//Stock
			if(debitNote.getDescription() != 2 && debitNote.getDescription() != 3) 
			{
				
				 if(!debitDetails.getProduct_id().getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
				   {
					 Integer pflag=productDao.checktype(debitNote.getCompany().getCompany_id(),debitDetails.getProduct_id().getProduct_id());
				     if(pflag==1)  
				     {
					      Stock stock = null;
					      stock=stockService.isstockexist(debitNote.getCompany().getCompany_id(),debitDetails.getProduct_id().getProduct_id());
					      double amt=debitDetails.getQuantity()*debitDetails.getRate();
					      if(stock!=null)
							{
					    	    stock.setAmount(stock.getAmount()+amt);
								stock.setQuantity(stock.getQuantity()+debitDetails.getQuantity());					
								stockService.updateStock(stock);
								stockService.addStockInfoOfProduct(stock.getStock_id(), debitNote.getCompany().getCompany_id(), debitDetails.getProduct_id().getProduct_id(), (double)debitDetails.getQuantity(), debitDetails.getRate());
							}
				     }	
				   }
			}	
		
		try {			
			debitNote.setCGST_head(round(cgst,2));
			debitNote.setIGST_head(round(igst,2));
			debitNote.setSGST_head(round(sgst,2));
			debitNote.setSCT_head(round(state_comp_tax,2));
			debitNote.setTotal_vat(vat);
			debitNote.setTotal_vatcst(vatcst);
			debitNote.setTotal_excise(excise);
			debitNote.setTransaction_value_head(transaction_value);
			debitNote.setRound_off(round_off);
			debitNote.setTds_amount(tds);
			debitNoteDao.update(debitNote);
			debitNoteDao.deleteByDebitNoteDetailsId(entityId);
			
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		return debitNoteId;
		
	}

	@Override
	public DebitNote findOneView(Long id) {
		return debitNoteDao.findOneView(id);
	}

	@Override
	public List<DebitNote> getReportAgainstSubledger(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {
		return debitNoteDao.getReportAgainstSubledger(Id, from_date, to_date, comId);
	}

	@Override
	public void saveDebitNote(DebitNote note) {
		debitNoteDao.saveDebitNote(note);
	}

	@Override
	public List<DebitNote> getDayBookReport(LocalDate from_date,LocalDate to_date, Long companyId) {
		
		return debitNoteDao.getDayBookReport(from_date, to_date,companyId);
	}

	@Override
	public void updateDebitNoteThroughExcel(DebitNote note) {
		debitNoteDao.updateDebitNoteThroughExcel(note);
	}

	@Override
	public void updateDebitDetailsThroughExcel(Long year_id, LocalDate date,DebitDetails debitDetails, Long debit_id, Long companyId) {
		debitNoteDao.updateDebitDetailsThroughExcel(debitDetails, debit_id);
	}

	@Override
	public Long saveDebitNoteThroughExcel(DebitNote entity) {
		return debitNoteDao.saveDebitNoteThroughExcel(entity);
	}

	@Override
	public List<DebitNote> findAllDebitNotesOfCompany(Long CompanyId, Boolean flag) {
		return debitNoteDao.findAllDebitNotesOfCompany(CompanyId, flag);
	}
	
	public static Double round(Double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public List<DebitNote> findAllDebitNotesOnlyOfCompany(Long CompanyId) {
		return debitNoteDao.findAllDebitNotesOnlyOfCompany(CompanyId);
	}

	@Override
	public void changeStatusOfDebitNoteThroughExcel(DebitNote note) {
		debitNoteDao.changeStatusOfDebitNoteThroughExcel(note);
		
	}

	@Override
	public DebitDetails getDebitDetails(Long id) {
		return debitNoteDao.getDebitDetailsById(id);
	}

	@Override
	public void updateDebitDetails(DebitDetails debitDetails, long company_id) {
		DebitDetails crDetails = debitNoteDao.getDebitDetailsById(debitDetails.getDebit_detail_id());
		DebitNote debitNote= debitNoteDao.findOneView(crDetails.getDebit_id().getDebit_no_id());
		
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
	
		// Set previous debitNote detail values
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
		
		// Set new debit detail values
		if(debitDetails.getCgst()!=null){
			newCGST=debitDetails.getCgst();
		}
		if(debitDetails.getIgst()!=null){
			newIGST=debitDetails.getIgst();
		}
		if(debitDetails.getSgst()!=null){
			newSGST=debitDetails.getSgst();
		}
		if(debitDetails.getState_com_tax()!=null){
			newSCT=debitDetails.getState_com_tax();
		}
		if(debitDetails.getTransaction_amount()!=null){
			newtransaction_amount=debitDetails.getTransaction_amount();
		}
		if(debitDetails.getVAT()!=null){
			newVAT=debitDetails.getVAT();
		}
		if(debitDetails.getVATCST()!=null){
			newVATCST=debitDetails.getVATCST();
		}
		if(debitDetails.getExcise()!=null) {
			newExcise=debitDetails.getExcise();
		}
		
		
		// Set debit note values
		if(debitNote.getTransaction_value_head()!=null) {
			transaction_value= debitNote.getTransaction_value_head();
		}
		if(debitNote.getTds_amount()!=null)
		{
			tds=debitNote.getTds_amount();
		}
		if(debitNote.getCGST_head()!=null){
			cgst=debitNote.getCGST_head();
		}
		if(debitNote.getIGST_head()!=null) {
			igst=debitNote.getIGST_head();
		}
		if(debitNote.getSGST_head()!=null){
			sgst=debitNote.getSGST_head();
		}
		if(debitNote.getSCT_head()!=null){
			state_comp_tax=debitNote.getSCT_head();
		}
		if(debitNote.getRound_off()!=null){
			round_off=debitNote.getRound_off();
		}
		if(debitNote.getTotal_vat()!=null){
			vat=debitNote.getTotal_vat();
		}
		if(debitNote.getTotal_vatcst()!=null){
			vatcst=debitNote.getTotal_vatcst();
		}
		if(debitNote.getTotal_excise()!=null) {
			excise=debitNote.getTotal_excise();
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
		
		if(debitNote.getSupplier()!=null  && debitNote.getSupplier().getTds_applicable()!=null)
		{
			if(debitNote.getSupplier().getTds_applicable().equals(1) && debitNote.getSupplier().getTds_rate()!=null)
			{
				pretds = (pretransaction_amount*debitNote.getSupplier().getTds_rate())/100;
			}
		}
		if(debitNote.getSupplier()!=null  && debitNote.getSupplier().getTds_applicable()!=null)
		{
			if(debitNote.getSupplier().getTds_applicable().equals(1) && debitNote.getSupplier().getTds_rate()!=null)
			{
				newtds = (newtransaction_amount*debitNote.getSupplier().getTds_rate())/100;
			}
		}
		
		tds=tds-pretds+newtds;
		
		preRound_off = pretransaction_amount + preCGST + preIGST + preSGST + preSCT + preVAT + preVATCST + preExcise-pretds;
		newRound_off = newtransaction_amount + newCGST + newIGST + newSGST + newSCT + newVAT + newVATCST + newExcise-newtds;
		
		round_off = round_off - preRound_off + newRound_off;
		
		if(debitNote.getDescription() != 2 && debitNote.getDescription() != 3) {	
				if(debitNote.getTransaction_value_head() != transaction_value) {
					if (debitNote.getSupplier()!=null && debitNote.getRound_off()!=null) {
						supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double)preRound_off, (long) 0);
						supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double)newRound_off, (long) 1);
						if(debitNote.getTds_amount()!=null && debitNote.getTds_amount()>0)
						{
							SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", debitNote.getCompany().getCompany_id());
							if (subledgerTds != null) {
								subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(),
										subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)pretds, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
							}
						}
						if(debitNote.getTds_amount()!=null && debitNote.getTds_amount()>0)
						{
							SubLedger subledgerTds = subledgerDao.findOne("TDS Payable", debitNote.getCompany().getCompany_id());
							if (subledgerTds != null) {
								subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(),
										subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)newtds, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
							}
						}
						
					}			
					if(debitNote.getSubledger()!=null && debitNote.getTransaction_value_head()!=null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(),debitNote.getSubledger().getSubledger_Id(), (long) 2, (double)pretransaction_amount, (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(),debitNote.getSubledger().getSubledger_Id(), (long) 2, (double)newtransaction_amount, (double) 0, (long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}			
				}
			}
	/*	else
		{
			if(debitNote.getRound_off() != newRound_off) {
				if (debitNote.getSupplier()!=null && debitNote.getRound_off()!=null) {
					supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double)debitNote.getRound_off(), (long) 0);
					supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double)round_off, (long) 1);
				}			
				if(debitNote.getSubledger()!=null && debitNote.getTransaction_value_head()!=null) {
					subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(),debitNote.getSubledger().getSubledger_Id(), (long) 2, (double)debitNote.getRound_off(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null);
					subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(),debitNote.getSubledger().getSubledger_Id(), (long) 2, (double)round_off, (double) 0, (long) 1,null,null,null,null,null,debitNote,null);
				}			
			}
		}*/
		
		
			SubLedger subledgercgst = subledgerDao.findOne("Input CGST",company_id);
			if(subledgercgst!=null){
				if(debitNote.getCGST_head()!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)debitNote.getCGST_head(),(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				}
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)cgst,(double) 0,(long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		
			 SubLedger subledgersgst = subledgerDao.findOne("Input SGST",company_id);
				
			 if(subledgersgst!=null){
				 if(debitNote.getSGST_head()!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)debitNote.getSGST_head(),(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)sgst,(double) 0,(long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			 }
		
			 SubLedger subledgerigst = subledgerDao.findOne("Input IGST",company_id);
			
			 if(subledgerigst!=null){
				 if(debitNote.getIGST_head()!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)debitNote.getIGST_head(),(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)igst,(double) 0,(long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			 }
		
			SubLedger subledgercess = subledgerDao.findOne("Input CESS",company_id);
			
			if(subledgercess!=null){
				 if(debitNote.getSCT_head()!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)debitNote.getSCT_head(),(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)state_comp_tax,(double) 0,(long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
	
			SubLedger subledgervat = subledgerDao.findOne("Input VAT",company_id);
			
			if(subledgervat!=null){
				 if(debitNote.getTotal_vat()!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)debitNote.getTotal_vat(),(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)vat,(double) 0,(long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		
			SubLedger subledgercst = subledgerDao.findOne("Input CST",company_id);
			
			if(subledgercst!=null){
				 if(debitNote.getTotal_vatcst()!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)debitNote.getTotal_vatcst(),(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)vatcst,(double) 0,(long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
	
			SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE",company_id);
			
			if(subledgerexcise!=null){
				 if(debitNote.getTotal_excise()!=null){
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)debitNote.getTotal_excise(),(double) 0,(long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				 }
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)excise,(double) 0,(long) 1,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			}
		
		//Stock
		if(debitNote.getDescription() != 2 && debitNote.getDescription() != 3) 
		{
			/* Integer pflag=productDao.checktype(debitNote.getCompany().getCompany_id(),debitDetails.getProductId());
		     if(pflag==1)  
		     {
			      Stock stock = new Stock();
			      Double stupdate;
			      stupdate=crDetails.getQuantity()-debitDetails.getQuantity();
			      stock=stockService.isstockexist(debitNote.getCompany().getCompany_id(),debitDetails.getProductId());
			      double amt=debitDetails.getQuantity()*debitDetails.getRate();
			      if(stock==null)
					{
						Stock stockdata = new Stock();
						stockdata.setCompany_id(debitNote.getCompany().getCompany_id());
						stockdata.setProduct_id(debitDetails.getProductId());
						stockdata.setQuantity((Double)debitDetails.getQuantity());
						stockdata.setAmount(amt);
						stockService.saveStock(stockdata);
					}
					else
					{					
						stock.setAmount(amt);
						stock.setQuantity((stock.getQuantity()+crDetails.getQuantity())-debitDetails.getQuantity());					
						stockService.updateStock(stock);
					}
		     }		*/
		}
		try {
			crDetails.setCgst(newCGST);
			crDetails.setIgst(newIGST);
			crDetails.setSgst(newSGST);
			crDetails.setState_com_tax(newSCT);
			crDetails.setVAT(newVAT);
			crDetails.setVATCST(newVATCST);
			crDetails.setExcise(newExcise);
			crDetails.setDiscount(debitDetails.getDiscount());
			crDetails.setFrieght(debitDetails.getFrieght());
			crDetails.setLabour_charges(debitDetails.getLabour_charges());
			crDetails.setOthers(debitDetails.getOthers());
			crDetails.setQuantity(debitDetails.getQuantity());
			crDetails.setRate(debitDetails.getRate());
			crDetails.setTransaction_amount(debitDetails.getTransaction_amount());
			debitNoteDao.updateDebitDetail(crDetails);
			
			debitNote.setCGST_head(round(cgst,2));
			debitNote.setIGST_head(round(igst,2));
			debitNote.setSGST_head(round(sgst,2));
			debitNote.setSCT_head(round(state_comp_tax,2));
			debitNote.setTotal_vat(vat);
			debitNote.setTotal_vatcst(vatcst);
			debitNote.setTotal_excise(excise);
			debitNote.setTransaction_value_head(transaction_value);
			debitNote.setRound_off(round_off);
			debitNote.setTds_amount(tds);
			debitNoteDao.update(debitNote);
			
            DebitNote note = debitNoteDao.findOne(debitNote.getDebit_no_id());
			
			if(note.getPurchase_bill_id() != null){				
				PurchaseEntry purchaseEntry = purchaseEntryDao.findOneWithAll(note.getPurchase_bill_id().getPurchase_id());
				Double total =(double) 0;
				for (DebitNote note1 : purchaseEntry.getDebitNote()) {
						total += note1.getRound_off();
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
		} catch (MyWebException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<DebitNote> findByPurchaseId(long id) {
		// TODO Auto-generated method stub
		return debitNoteDao.findByPurchaseId(id);
	}

	@Override
	public void diactivateByIdValue(Long entityId) {
		try {
			DebitNote debitNote = debitNoteDao.findOneView(entityId);

			if((debitNote.getDescription() !=null && debitNote.getDescription() != 2 && debitNote.getDescription() != 3)) {
				if(debitNote.getSupplier()!=null && debitNote.getRound_off()!=null)
				{
				supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double) debitNote.getRound_off(), (long) 0);
				}
				if(debitNote.getSubledger()!=null && debitNote.getTransaction_value_head()!=null)
				{
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSubledger().getSubledger_Id(), (long) 2, (double) debitNote.getTransaction_value_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
				}
			}else {
				if(debitNote.getSupplier()!=null && debitNote.getRound_off()!=null)
				{
				supplierService.addsuppliertrsansactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSupplier().getSupplier_id(),(long) 5, (double) 0, (double) debitNote.getRound_off(), (long) 0);
				}
				if(debitNote.getSubledger()!=null && debitNote.getRound_off()!=null)
				{
				subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), debitNote.getSubledger().getSubledger_Id(), (long) 2, (double) debitNote.getRound_off(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
			    }
			}
	
			if (debitNote.getDebitDetails() != null && debitNote.getDebitDetails().size() > 0) {
				if (debitNote.getCGST_head() != null && debitNote.getCGST_head() > 0) {
					SubLedger subledgercgst = subledgerDao.findOne("Input CGST", debitNote.getCompany().getCompany_id());
					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (double)debitNote.getCGST_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getSGST_head() != null && debitNote.getSGST_head() > 0) {
					SubLedger subledgersgst = subledgerDao.findOne("Input SGST", debitNote.getCompany().getCompany_id());

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgersgst.getSubledger_Id(), (long) 2, (double)debitNote.getSGST_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getIGST_head() != null && debitNote.getIGST_head() > 0) {
					SubLedger subledgerigst = subledgerDao.findOne("Input IGST", debitNote.getCompany().getCompany_id());
					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgerigst.getSubledger_Id(), (long) 2, (double)debitNote.getIGST_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getSCT_head() != null && debitNote.getSCT_head() > 0) {
					SubLedger subledgercess = subledgerDao.findOne("Input CESS", debitNote.getCompany().getCompany_id());
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgercess.getSubledger_Id(), (long) 2, (double)debitNote.getSCT_head(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getTotal_vat() != null && debitNote.getTotal_vat() > 0) {
					SubLedger subledgervat = subledgerDao.findOne("Input VAT", debitNote.getCompany().getCompany_id());
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgervat.getSubledger_Id(), (long) 2, (double)debitNote.getTotal_vat(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getTotal_vatcst() != null && debitNote.getTotal_vatcst() > 0) {
					SubLedger subledgercst = subledgerDao.findOne("Input CST", debitNote.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgercst.getSubledger_Id(), (long) 2, (double)debitNote.getTotal_vatcst(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
				if (debitNote.getTotal_excise() != null && debitNote.getTotal_excise() > 0) {
					SubLedger subledgerexcise = subledgerDao.findOne("Input EXCISE", debitNote.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(debitNote.getAccounting_year_id().getYear_id(),debitNote.getDate(),debitNote.getCompany().getCompany_id(), subledgerexcise.getSubledger_Id(), (long) 2, (double)debitNote.getTotal_excise(), (double) 0, (long) 0,null,null,null,null,null,debitNote,null,null,null,null,null,null);
					}
				}
			}
			
			/*PurchaseEntry entry = purchaseEntryDao.findOne(debitNote.getPurchaseEntryId());*/
	
			/*if (entry.getPayments() != null) {
				for (Payment payment : entry.getPayments()) {
					if (payment.getGst_applied() == false) {
						
						 * if (payment.getPayment_type() == 1) { SubLedger subledgercgst =
						 * subledgerDao.findOne("Cash In hand", debitNote.getCompany().getCompany_id());
						 * subledgerService.addsubledgertransactionbalance(debitNote.getCompany().
						 * getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2, (Double)
						 * 0,debitNote.getRound_off(), (long) 1); } else {
						 * 
						 * bankService.addbanktransactionbalance(debitNote.getCompany().getCompany_id(),
						 * payment.getBank().getBank_id(), (long) 3,(Double) 0 , debitNote.getRound_off(),
						 * (long) 1); }
						 
					}
	
					if (payment.getGst_applied() == true) {
						if (payment.getPayment_type() == 1) {
							
							 * SubLedger subledgercash = subledgerDao.findOne("Cash In hand",
							 * debitNote.getCompany().getCompany_id());
							 * subledgerService.addsubledgertransactionbalance(debitNote.getCompany().
							 * getCompany_id(), subledgercash.getSubledger_Id(), (long) 2,(Double) 0 ,
							 * debitNote.getRound_off(), (long) 1);
							 

						} else {

							
							 * bankService.addbanktransactionbalance(debitNote.getCompany().getCompany_id(),
							 * payment.getBank().getBank_id(), (long) 3,(Double) 0 ,
							 * debitNote.getTransaction_value_head(), (long) 1);
							 	
						}
					}
				}
			}*/
			//Stock
			for (DebitDetails debitDetails : debitNote.getDebitDetails()) {
				if(debitNote.getDescription() != 2 && debitNote.getDescription() != 3) 
				{
					 Integer pflag=productDao.checktype(debitNote.getCompany().getCompany_id(),debitDetails.getProduct_id().getProduct_id());
				     if(pflag==1)  
				     {
					      Stock stock = new Stock();
					      stock=stockService.isstockexist(debitNote.getCompany().getCompany_id(),debitDetails.getProduct_id().getProduct_id());
					      double amt=debitDetails.getQuantity()*debitDetails.getRate();
					      if(stock==null)
							{
								Stock stockdata = new Stock();
								stockdata.setAmount(amt);
								stockdata.setCompany_id(debitNote.getCompany().getCompany_id());
								stockdata.setProduct_id(debitDetails.getProduct_id().getProduct_id());
								stockdata.setQuantity((double)debitDetails.getQuantity());
								stockService.saveStock(stockdata);
							}
							else
							{					
								stock.setAmount(amt);
								stock.setQuantity(stock.getQuantity()+debitDetails.getQuantity());					
								stockService.updateStock(stock);
							}
				     }		
				}		
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		debitNoteDao.diactivateByIdValue(entityId);
	}

	@Override
	public List<DebitNote> getDebitNoteForLedgerReport(LocalDate from_date, LocalDate to_date, Long suppilerId,
			Long companyId) {
		// TODO Auto-generated method stub
		return debitNoteDao.getDebitNoteForLedgerReport(from_date, to_date, suppilerId, companyId);
	}

	@Override
	public DebitNote isExcelVocherNumberExist(String vocherNo, Long companyId) {
		// TODO Auto-generated method stub
		return debitNoteDao.isExcelVocherNumberExist(vocherNo, companyId);
	}

	@Override
	public List<DebitNote> getAllOpeningBalanceAgainstDebitNote(Long supplierId, Long companyId) {
		// TODO Auto-generated method stub
		return debitNoteDao.getAllOpeningBalanceAgainstDebitNote(supplierId, companyId);
	}

	@Override
	public List<DebitNote> getAllOpeningBalanceAgainstDebitNoteForPeriod(Long supplierId, Long companyId,
			LocalDate todate) {
		// TODO Auto-generated method stub
		return debitNoteDao.getAllOpeningBalanceAgainstDebitNoteForPeriod(supplierId, companyId, todate);
	}

}