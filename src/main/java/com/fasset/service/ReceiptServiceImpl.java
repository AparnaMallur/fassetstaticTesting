package com.fasset.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.IReceiptDAO;
import com.fasset.dao.interfaces.ISalesEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IBankDAO;
import com.fasset.dao.interfaces.ICustomerDAO;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Customer;
import com.fasset.entities.Payment;
import com.fasset.entities.Product;
import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ProductInformation;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IBankService;

@Service
@Transactional
public class ReceiptServiceImpl implements IReceiptService {

	@Autowired
	private IReceiptDAO receiptDAO;

	@Autowired
	private IProductDAO productDao;

	@Autowired
	private IAccountingYearDAO accountYearDAO;

	@Autowired
	private ICustomerDAO customerDao;

	@Autowired
	private ISalesEntryDAO salesEntryDao;
	
	
	@Autowired
	private ISubLedgerDAO subledgerDAO;

	@Autowired
	private IBankDAO bankDao;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private ISubLedgerService subledgerService;

	@Autowired
	private IBankService bankService;

	@Override
	public void add(Receipt entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Receipt rec) throws MyWebException {
		if (rec.getProductInformationList() != null && rec.getProductInformationList() != "") {
			List<ProductInformation> informationList = new ArrayList<ProductInformation>();
			Set<Product> products = new HashSet<Product>();
			Product product = new Product();
			rec.setUpdate_date(new LocalDateTime());
			
			JSONArray jsonArray = new JSONArray(rec.getProductInformationList());
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
				information.setTransaction_amount(jsonObject.getString("transaction_amount"));
				informationList.add(information);
			}
			rec.setInformationList(informationList);
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
			rec.setProducts(products);
		}
		rec.setTds_paid(rec.getTds_paid());
		
		Receipt receipt = receiptDAO.findOneWithAll(rec.getReceipt_id());
		
		if(rec.getAdvance_payment()!=null && rec.getAdvance_payment()==true) {
			
			if(rec.getTds_paid()!=null && rec.getTds_paid().equals(true) && rec.getGst_applied() !=null && rec.getGst_applied().equals(2))
			{
				if(receipt.getTds_amount()!=null && receipt.getAmount()!=null && !(receipt.getTds_amount().equals(rec.getTds_amount())) && !(receipt.getAmount().equals(rec.getAmount()))) {
		rec.setTds_amount(rec.getTds_amount());		
		rec.setAmount(rec.getAmount()-rec.getTds_amount());
				}
				if(receipt.getAmount()!=null && !(receipt.getAmount().equals(rec.getAmount()))) {
					rec.setTds_amount(rec.getTds_amount());		
					rec.setAmount(rec.getAmount()-rec.getTds_amount());
				}
			}
			else if(rec.getTds_paid()!=null && rec.getTds_paid().equals(true) && rec.getGst_applied() !=null && rec.getGst_applied().equals(1))
			{
				rec.setTds_amount(rec.getTds_amount());		
				rec.setAmount(rec.getAmount());
			}	
			else
			{
				rec.setTds_amount((double)0);		
				rec.setAmount(rec.getAmount());
			}
		}
		else 
		{
			/**IF tds is already cut for sales entry then we are adding receipt against that sales entry then tds value for particular receipt is storing in database for showing tds against this receipt in receipt report & ledger report */
			double tds=0;
			if(receipt.getSales_bill_id() != null && receipt.getSales_bill_id().getTds_amount()!=null){	
			tds=(rec.getAmount()*receipt.getSales_bill_id().getTds_amount())/receipt.getSales_bill_id().getRound_off();
			}
			rec.setTds_amount(tds);	
			rec.setAmount(rec.getAmount());
			rec.setTds_paid(false); /**here we are giving tds paid = false because tds is already deducted for sales entry. this tds amount will required for report  receipt report & ledger report .*/
			rec.setAdvance_payment(false);
		}
			
		
		if(rec.getTds_paid()!=null && rec.getTds_paid()==true)
		{
			if(rec.getTds_amount()!=receipt.getTds_amount())
			{
				SubLedger subledgerTds = subledgerDAO.findOne("TDS Receivable", rec.getCompany().getCompany_id());
				if (subledgerTds != null) {
					if(receipt.getTds_amount()!=null)
					{
					subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)receipt.getTds_amount(), (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
					if(rec.getTds_amount()!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),
								subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTds_amount(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}					
				}
			}
		}	
		
		if ((receipt.getCustomer() != null) && (rec.getCustomer_id()!=null && rec.getSubledgerId()!=null) && (rec.getCustomer_id() == -1) && (rec.getSubledgerId() > 0)) {
			rec.setCustomer(null);
			rec.setSales_bill_id(null);
			if (rec.getSubledgerId() != null && rec.getSubledgerId() > 0) {
				rec.setSubLedger(subledgerDAO.findOne(rec.getSubledgerId()));
			}
		} else if ((receipt.getSubLedger() != null) && (rec.getCustomer_id() != null && rec.getCustomer_id() > 0)) {
			if (rec.getCustomer_id() != null && rec.getCustomer_id() > 0) {
				rec.setCustomer(customerDao.findOne(rec.getCustomer_id()));
			}
			if (rec.getSalesEntryId() != null && rec.getSalesEntryId() > 0) {
				rec.setSales_bill_id(salesEntryDao.findOne(rec.getSalesEntryId()));
			}
			rec.setSubLedger(null);
		} else if ((rec.getCustomer_id() != null && rec.getCustomer_id() > 0)) {
			try {
					rec.setCustomer(customerDao.findOne(rec.getCustomer_id()));
			
				if (rec.getSalesEntryId() != null && rec.getSalesEntryId() > 0) {
					rec.setSales_bill_id(salesEntryDao.findOne(rec.getSalesEntryId()));
				}
			} catch (MyWebException e) {
				e.printStackTrace();
			}
		} else if ((rec.getSubledgerId() != null && rec.getSubledgerId() > 0)) {
			try {
					rec.setSubLedger(subledgerDAO.findOne(rec.getSubledgerId()));
			} catch (MyWebException e) {
				e.printStackTrace();
			}
		}
		
		if((rec.getPayment_type()!=null)&&(rec.getPayment_type()!=1))
		{
			if (rec.getBankId() != null && rec.getBankId() > 0) {
				rec.setBank(bankDao.findOne(rec.getBankId()));
			}
		}
		
		if ((receipt.getCustomer() != null && rec.getCustomer_id()!=null && rec.getCustomer_id() > 0)
				&& ((long)receipt.getCustomer().getCustomer_id() != (long)rec.getCustomer_id())
				&& (receipt.getAmount() != rec.getAmount())) {
			
			if(rec.getAdvance_payment()!=null && rec.getAdvance_payment()==true) {
				customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
						receipt.getCustomer().getCustomer_id(), (long) 5, ((double)receipt.getAmount() + (double)receipt.getTds_amount()), (double) 0, (long) 0);
				customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getCustomer_id(),
						(long) 5, ((double)rec.getAmount() + (double)rec.getTds_amount()), (double) 0, (long) 1);
			}
			else
			{
				customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
						receipt.getCustomer().getCustomer_id(), (long) 5, ((double)receipt.getAmount()), (double) 0, (long) 0);
				customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getCustomer_id(),
						(long) 5, ((double)rec.getAmount()), (double) 0, (long) 1);
			}
			
		} else if ((receipt.getSubLedger() != null && rec.getSubledgerId()!=null && rec.getSubledgerId() > 0)
				&& ((long)receipt.getSubLedger().getSubledger_Id() != (long)rec.getSubledgerId())
				&& (receipt.getAmount() != rec.getAmount())) {
			
				subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
						receipt.getSubLedger().getSubledger_Id(), (long) 2, ((double)receipt.getAmount()+ (double)receipt.getTds_amount()), (double) 0, (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
		
				subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getSubledgerId(),
						(long) 2, ((double)rec.getAmount()+ (double)receipt.getTds_amount()), (double) 0, (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
			
		}
		else if ((receipt.getCustomer() != null && rec.getCustomer_id()!=null && rec.getCustomer_id() > 0)
				&& ((long)receipt.getCustomer().getCustomer_id() != (long)rec.getCustomer_id())) {
			
			if(rec.getAdvance_payment()!=null && rec.getAdvance_payment()==true) {
				customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
						receipt.getCustomer().getCustomer_id(), (long) 5, ((double)receipt.getAmount() + (double)receipt.getTds_amount()), (double) 0, (long) 0);
				customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getCustomer_id(),
						(long) 5, ((double)rec.getAmount() + (double)rec.getTds_amount()), (double) 0, (long) 1);
			}
			else
			{
				customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
						receipt.getCustomer().getCustomer_id(), (long) 5, ((double)receipt.getAmount()), (double) 0, (long) 0);
				customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getCustomer_id(),
						(long) 5, ((double)rec.getAmount()), (double) 0, (long) 1);
			}
			
		} else if ((receipt.getSubLedger() != null && rec.getSubledgerId()!=null &&  rec.getSubledgerId() > 0)
				&& ((long)receipt.getSubLedger().getSubledger_Id() != (long)rec.getSubledgerId())) {
			
				subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
						receipt.getSubLedger().getSubledger_Id(), (long) 2, ((double)receipt.getAmount()+ (double)receipt.getTds_amount()), (double) 0, (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
		
				subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getSubledgerId(),
						(long) 2, ((double)rec.getAmount()+ (double)receipt.getTds_amount()), (double) 0, (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
			
		} 
		else if ((receipt.getAmount() != rec.getAmount())) {
			if (((receipt.getCustomer() != null) && (rec.getCustomer_id()!=null && rec.getCustomer_id() > 0)
					&& (receipt.getAmount() != rec.getAmount()))) {
				if(rec.getAdvance_payment()!=null && rec.getAdvance_payment()==true) {
					customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							receipt.getCustomer().getCustomer_id(), (long) 5, ((double)receipt.getAmount() + (double)receipt.getTds_amount()), (double) 0, (long) 0);
					customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getCustomer_id(),
							(long) 5, ((double)rec.getAmount() + (double)rec.getTds_amount()), (double) 0, (long) 1);
				}
				else
				{
					customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							receipt.getCustomer().getCustomer_id(), (long) 5, ((double)receipt.getAmount()), (double) 0, (long) 0);
					customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getCustomer_id(),
							(long) 5, ((double)rec.getAmount()), (double) 0, (long) 1);
				}
			} else if ((receipt.getSubLedger() != null) && (rec.getSubledgerId()!=null && rec.getSubledgerId() > 0)
					&& (receipt.getAmount() != rec.getAmount())) {
				subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
						receipt.getSubLedger().getSubledger_Id(), (long) 2, ((double)receipt.getAmount()+ (double)receipt.getTds_amount()), (double) 0, (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
				subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getSubledgerId(),
						(long) 2, ((double)rec.getAmount()+ (double)receipt.getTds_amount()), (double) 0, (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
			}
			
			if(rec.getGst_applied()!=null && rec.getPayment_type()!=null)
			{
			if (rec.getGst_applied() == 2) {
				if (rec.getPayment_type() == 1) {
					SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand", rec.getCompany().getCompany_id());
					if (subledgercash != null) {
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercash.getSubledger_Id(), (long) 2, (double) 0, (double)receipt.getAmount(), (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						if(rec.getPayment_type()==1)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercash.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getAmount(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
				} else {
					if(receipt.getBank()!=null && rec.getBankId()!=null)
					{
					bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							receipt.getBank().getBank_id(), (long) 3, (double) 0, (double)receipt.getAmount(), (long) 0);
					bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getBankId(), (long) 3,
							(double) 0, (double)rec.getAmount(), (long) 1);
					}
				}
			}
		   }
		}
		
		
		
			
			if(rec.getGst_applied()!=null && rec.getPayment_type()!=null)
			{
			if (rec.getGst_applied() == 1) {
				if (rec.getPayment_type() == 1) {
					SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand", rec.getCompany().getCompany_id());

					if (subledgercash != null) {
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercash.getSubledger_Id(), (long) 2, (double) 0, (double)(receipt.getTransaction_value()),
								(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						if(rec.getPayment_type()==1)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercash.getSubledger_Id(), (long) 2, (double) 0, (double)(rec.getTransaction_value()),
								(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
				}
				else
				{
					 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),rec.getBankId() ,(long) 3,(double) 0,(double)(receipt.getTransaction_value()),(long) 0);
					 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),rec.getBankId() ,(long) 3,(double) 0,(double)(rec.getTransaction_value()),(long) 1);
				}
				
				if ((rec.getCgst() != null && receipt.getCgst() != null)&& (rec.getCgst() != receipt.getCgst())) {
					SubLedger subledgercgst = subledgerDAO.findOne("Output CGST", rec.getCompany().getCompany_id());

					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)receipt.getCgst(), (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getCgst(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				} else if ((rec.getCgst() != null)
						&& (receipt.getCgst() != null && receipt.getCgst() == 0)) {

					SubLedger subledgercgst = subledgerDAO.findOne("Output CGST", rec.getCompany().getCompany_id());

					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getCgst(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				}

				if ((rec.getSgst() != null && receipt.getSgst() != null) && (rec.getSgst() != receipt.getSgst())) {
					SubLedger subledgersgst = subledgerDAO.findOne("Output SGST", rec.getCompany().getCompany_id());

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)receipt.getSgst(), (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getSgst(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				} else if ((rec.getSgst() != null)&& (receipt.getSgst() != null && receipt.getSgst() == 0)) {
					SubLedger subledgersgst = subledgerDAO.findOne("Output SGST", rec.getCompany().getCompany_id());

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getSgst(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				}

				if ((rec.getIgst() != null && receipt.getIgst() != null) && (rec.getIgst() != receipt.getIgst())) {
					SubLedger subledgerigst = subledgerDAO.findOne("Output IGST", rec.getCompany().getCompany_id());

					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)receipt.getIgst(), (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getIgst(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				} else if ((rec.getIgst() != null) && (receipt.getIgst() != null && receipt.getIgst() == 0)) {

					SubLedger subledgerigst = subledgerDAO.findOne("Output IGST", rec.getCompany().getCompany_id());
					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getIgst(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				}

				if ((rec.getState_compansation_tax() != null && receipt.getState_compansation_tax() != null)
						&& (rec.getState_compansation_tax() != receipt.getState_compansation_tax())) {
					SubLedger subledgercess = subledgerDAO.findOne("Output CESS", rec.getCompany().getCompany_id());

					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercess.getSubledger_Id(), (long) 2, (double) 0,
								(double)receipt.getState_compansation_tax(), (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercess.getSubledger_Id(), (long) 2, (double) 0,
								(double)rec.getState_compansation_tax(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}

				} else if ((rec.getState_compansation_tax() != null)
						&& (receipt.getState_compansation_tax() != null
								&& receipt.getState_compansation_tax() == 0)) {

					SubLedger subledgercess = subledgerDAO.findOne("Output CESS", rec.getCompany().getCompany_id());

					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercess.getSubledger_Id(), (long) 2, (double) 0,
								(double)rec.getState_compansation_tax(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				}

				if ((rec.getTotal_vat() != null && receipt.getTotal_vat() != null)
						&& (rec.getTotal_vat() != receipt.getTotal_vat())) {

					SubLedger subledgervat = subledgerDAO.findOne("Output VAT", rec.getCompany().getCompany_id());

					if (subledgervat != null) {
						if(receipt.getTotal_vat()!=null)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)receipt.getTotal_vat(),
								(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
						if(rec.getTotal_vat()!=null)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTotal_vat(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
				} else if ((rec.getTotal_vat() != null)
						&& (rec.getTotal_vat() != null && receipt.getTotal_vat() == 0)) {
					SubLedger subledgervat = subledgerDAO.findOne("Output VAT", rec.getCompany().getCompany_id());
					if (subledgervat != null) {
						if(rec.getTotal_vat()!=null)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTotal_vat(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
				}

				if ((rec.getTotal_vatcst() != null && receipt.getTotal_vatcst() != null)
						&& (rec.getTotal_vatcst() != receipt.getTotal_vatcst())) {
					SubLedger subledgercst = subledgerDAO.findOne("Output CST", rec.getCompany().getCompany_id());
					if (subledgercst != null) {
						if(receipt.getTotal_vatcst()!=null)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)receipt.getTotal_vatcst(),
								(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
						if(rec.getTotal_vatcst()!=null)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTotal_vatcst(),
								(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
				} else if ((rec.getTotal_vatcst() != null)
						&& (receipt.getTotal_vatcst() != null && receipt.getTotal_vatcst() == 0)) {
					SubLedger subledgercst = subledgerDAO.findOne("Output CST", rec.getCompany().getCompany_id());
					if (subledgercst != null) {
						if(rec.getTotal_vatcst()!=null)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTotal_vatcst(),
								(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
				}
				if ((rec.getTotal_excise() != null && receipt.getTotal_excise() != null)
						&& (rec.getTotal_excise() != receipt.getTotal_excise())) {

					SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",
							rec.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						if(receipt.getTotal_excise()!=null)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)receipt.getTotal_excise(),
								(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
						if(rec.getTotal_excise()!=null)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTotal_excise(),
								(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
				} else if ((rec.getTotal_excise() != null)
						&& (receipt.getTotal_excise() != null && receipt.getTotal_excise() == 0)) {

					SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",
							rec.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						if(rec.getTotal_excise()!=null)
						{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
								subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTotal_excise(),
								(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
				}
				
				
			}
			
		   }
		
		
		
	/*	if ((receipt.getCustomer() != null) && (rec.getCustomer_id() == -1) && (rec.getSubledgerId() > 0)) {
			if (receipt.getCustomer().getCustomer_id() != null) {
				customerService.addcustomertransactionbalance(rec.getCompany().getCompany_id(),
						receipt.getCustomer().getCustomer_id(), (long) 5, (double)rec.getAmount(), (double) 0, (long) 0);
			}
			subledgerService.addsubledgertransactionbalance(rec.getCompany().getCompany_id(), rec.getSubledgerId(),
					(long) 2, (double)rec.getAmount(), (double) 0, (long) 1);
		}
		if ((receipt.getSubLedger() != null) && (rec.getCustomer_id() > 0)) {
			if (receipt.getSubLedger() != null) {
				subledgerService.addsubledgertransactionbalance(rec.getCompany().getCompany_id(),
						receipt.getSubLedger().getSubledger_Id(), (long) 2, (double)rec.getAmount(), (double) 0, (long) 0);
			}

			if (rec.getCustomer_id() != null) {
				customerService.addcustomertransactionbalance(rec.getCompany().getCompany_id(), rec.getCustomer_id(),
						(long) 5, (double)rec.getAmount(), (double) 0, (long) 1);
			}
		}*/

		receiptDAO.update(rec);
		
		Receipt receipt1 = receiptDAO.findOneWithAll(rec.getReceipt_id());

		if(receipt1.getSales_bill_id() != null){
			SalesEntry salesEntry = salesEntryDao.findOneWithAll(receipt1.getSales_bill_id().getSales_id());
			double total=(double) 0;
			
			for (Receipt recpt : salesEntry.getReceipts()) {
				if(recpt.getTds_amount()!=null)
				total += recpt.getAmount()+recpt.getTds_amount(); 
				else
					total += recpt.getAmount();				
			}	
			for (CreditNote note : salesEntry.getCreditNote()) {
				total += note.getRound_off();				
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
	}

	@Override
	public List<Receipt> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Receipt getById(Long id) throws MyWebException {
		return receiptDAO.findOne(id);
	}

	@Override
	public Receipt getById(String id) throws MyWebException {
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
	public void remove(Receipt entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Receipt entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public Long saveReceipt(Receipt rec) {
		List<ProductInformation> informationList = new ArrayList<ProductInformation>();
		Set<Product> products = new HashSet<Product>();
		Product product = new Product();
		try {
			if (rec.getYear_id() != null && rec.getYear_id() > 0) {
				rec.setAccountingYear(accountYearDAO.findOne(rec.getYear_id()));
			}
			if (rec.getBankId() > 0) {
				rec.setBank(bankDao.findOne(rec.getBankId()));
			}

		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		if (rec.getGst_applied() == 1) {
			System.out.println("------------1--------------");
			JSONArray jsonArray = new JSONArray(rec.getProductInformationList());
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
				information.setTransaction_amount(jsonObject.getString("transaction_amount"));
				informationList.add(information);
			}
			rec.setInformationList(informationList);

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
			rec.setProducts(products);

		}
		rec.setCreated_date(new LocalDate());

		try {
			if (rec.getCustomer_id() != null && rec.getCustomer_id() > 0) {
				System.out.println("The customer Id for receipt saving is " +rec.getCustomer_id());
				rec.setCustomer(customerDao.findOne(rec.getCustomer_id()));
			}
			if (rec.getSubledgerId() != null && rec.getSubledgerId() > 0) {
				System.out.println("The subledger Id for receipt saving is " +rec.getCustomer_id());
				rec.setSubLedger(subledgerDAO.findOne(rec.getSubledgerId()));
			}
			if (rec.getSalesEntryId() != null && rec.getSalesEntryId() > 0) {
				System.out.println("The salesId Id for receipt saving is " +rec.getCustomer_id());
				rec.setSales_bill_id(salesEntryDao.findOne(rec.getSalesEntryId()));
			}

		} catch (MyWebException e) {
			e.printStackTrace();
		}
		rec.setEntry_status(MyAbstractController.ENTRY_PENDING);
		rec.setTds_paid(rec.getTds_paid());	
		
			
			if(rec.getAdvance_payment()!=null && rec.getAdvance_payment()==true) {
			
				if(rec.getTds_paid()!=null && rec.getTds_paid()==true)
				{
					if(rec.getGst_applied() !=null && rec.getGst_applied().equals(2))
					{
			rec.setTds_amount(rec.getTds_amount());		
			rec.setAmount(rec.getAmount()-rec.getTds_amount());
					}
					else if(rec.getGst_applied() !=null && rec.getGst_applied().equals(1))
					{
						rec.setTds_amount(rec.getTds_amount());		
						rec.setAmount(rec.getAmount());
					}
				}
				else
				{
					rec.setTds_amount((double)0);		
					rec.setAmount(rec.getAmount());
				}
			}
			else 
			{
				
				/**IF tds is already cut for sales entry then we are adding receipt against that sales entry then tds value for particular receipt is storing in database for showing tds against this receipt in receipt report & ledger report */
				double tds=0;
				if(rec.getSales_bill_id() != null && rec.getSales_bill_id().getTds_amount()!=null){	
				tds=(rec.getAmount()*rec.getSales_bill_id().getTds_amount())/rec.getSales_bill_id().getRound_off();
				}
				rec.setTds_amount(tds);	
				rec.setTds_paid(false); /**here we are giving tds paid = false because tds is already deducted for sales entry. this tds amount will required for report  receipt report & ledger report .*/
				rec.setAdvance_payment(false);
			}
			//customer_bill_no
			if(rec.getSalesEntryId().equals((long)-2))
			{
				rec.setAgainstOpeningBalnce(true);
			}
			else
			{
				rec.setAgainstOpeningBalnce(false);
			}
			
		Long id = receiptDAO.saveReceiptDao(rec);		
		System.out.println("rect id is "+ id);
		Receipt receipt1=null;
		try {
			receipt1 = receiptDAO.findOne(id);
		} catch (MyWebException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(rec.getSales_bill_id() != null){
			System.out.println("Sales_bill_id() is not null ");
			SalesEntry salesEntry = salesEntryDao.findOneWithAll(rec.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
			double total=(double) 0;
			for (Receipt receipt : salesEntry.getReceipts()) {
				if(receipt.getTds_amount()!=null)
				total += receipt.getAmount()+receipt.getTds_amount(); 
				else
					total += receipt.getAmount();				
			}
			for (CreditNote note : salesEntry.getCreditNote()) {
					total += note.getRound_off();				
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
		if(rec.getTds_paid()!=null && rec.getTds_paid()==true)
		{
			SubLedger subledgerTds = subledgerDAO.findOne("TDS Receivable", rec.getCompany().getCompany_id());
			if (subledgerTds != null) {
				subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
						subledgerTds.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTds_amount(), (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
			}
		}
		
		if (rec.getCustomer_id() != null && rec.getCustomer_id() > 0) {
			System.out.println("The customer for receipt");
			if(rec.getAdvance_payment()!=null && rec.getAdvance_payment()==true) {
				System.out.println("The getAdvance_payment");
				customerService.addcustomertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getCustomer_id(),
					(long) 5, ((double)rec.getAmount()+(double)rec.getTds_amount()), (double) 0, (long) 1);
			}
			else
			{
				System.out.println("The getAdvance_payment  null");
				customerService.addcustomertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getCustomer_id(),
						(long) 5, ((double)rec.getAmount()), (double) 0, (long) 1);
			}
		}
		
		
		if (rec.getSubledgerId() != null && rec.getSubledgerId() > 0) {
			System.out.println("The subledger");
			subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getSubledgerId(),
					(long) 2, ((double)rec.getAmount()+ (double)rec.getTds_amount()), (double) 0, (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
		}

		if (rec.getGst_applied() == 2) {			
			if (rec.getPayment_type() == 1) {
				SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand", rec.getCompany().getCompany_id());
				if (subledgercash != null) {
					subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							subledgercash.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getAmount(), (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);

				}
			} else {
				bankService.addbanktransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getBank().getBank_id(),
						(long) 3, (double) 0, (double)rec.getAmount(), (long) 1);
			}
		} else if (rec.getGst_applied() == 1) {
			if (rec.getPayment_type() == 1) {

				SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand", rec.getCompany().getCompany_id());

				if (subledgercash != null) {
					subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							subledgercash.getSubledger_Id(), (long) 2, (double) 0, (double)(rec.getTransaction_value()), (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
				}

				
			} else {
				bankService.addbanktransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(), rec.getBank().getBank_id(),
						(long) 3, (double) 0, (double)(rec.getTransaction_value()), (long) 1);
          	}
			
			if (rec.getCgst() != null && rec.getCgst() > 0) {
				SubLedger subledgercgst = subledgerDAO.findOne("Output CGST", rec.getCompany().getCompany_id());

				if (subledgercgst != null) {
					subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getCgst(), (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
				}

			}
			if (rec.getSgst() != null && rec.getSgst() > 0) {
				SubLedger subledgersgst = subledgerDAO.findOne("Output SGST", rec.getCompany().getCompany_id());

				if (subledgersgst != null) {
					subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getSgst(), (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
				}
			}

			if (rec.getIgst() != null && rec.getIgst() > 0) {
				SubLedger subledgerigst = subledgerDAO.findOne("Output IGST", rec.getCompany().getCompany_id());

				if (subledgerigst != null) {
					subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getIgst(), (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
				}
			}

			if (rec.getState_compansation_tax() != null && rec.getState_compansation_tax() > 0) {
				SubLedger subledgercess = subledgerDAO.findOne("Output CESS", rec.getCompany().getCompany_id());

				if (subledgercess != null) {
					subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getState_compansation_tax(),
							(long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
				}
			}

			if (rec.getTotal_vat() != null && rec.getTotal_vat() > 0) {
				SubLedger subledgervat = subledgerDAO.findOne("Output VAT", rec.getCompany().getCompany_id());

				if (subledgervat != null) {
					subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTotal_vat(), (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
				}
			}
			if (rec.getTotal_vatcst() != null && rec.getTotal_vatcst() > 0) {
				SubLedger subledgercst = subledgerDAO.findOne("Output CST", rec.getCompany().getCompany_id());

				if (subledgercst != null) {
					subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTotal_vatcst(), (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
				}
			}
			if (rec.getTotal_excise() != null && rec.getTotal_excise() > 0) {
				SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE", rec.getCompany().getCompany_id());

				if (subledgerexcise != null) {
					subledgerService.addsubledgertransactionbalance(rec.getAccountingYear().getYear_id(),rec.getDate(),rec.getCompany().getCompany_id(),
							subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)rec.getTotal_excise(),
							(long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
				}
			}
		}

		return id;
	}

	@Override
	public List<Receipt> getAllOpeningBalanceAgainstReceipt(Long customerId, Long companyId) {
		
		return receiptDAO.getAllOpeningBalanceAgainstReceipt( customerId, companyId);
	}
	
	@Override
	public List<Receipt> getReceiptForSales(Long customerId, Long companyId,LocalDate toDate) {
		
		return receiptDAO.getReceiptForSales( customerId, companyId,toDate);
	}
	
	@Override
	public List<Receipt> findAll() {
		return receiptDAO.findAll();
	}

	@Override
	public Company findCompany(Long user_id) {
		return receiptDAO.findCompany(user_id);
	}

	@Override
	public List<Customer> findAllCustomer(Long company_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Receipt_product_details> findAllReceiptProductEntityList(Long entryId) {
		return receiptDAO.findAllReceiptProductEntityList(entryId);
	}

	@Override
	public String deleteReceiptProduct(Long REId, Long receipt_id, Long company_id) {
		return receiptDAO.deleteReceiptProduct(REId, receipt_id, company_id);
	}

	@Override
	public Receipt isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CopyOnWriteArrayList<Receipt> getReport(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {
		return receiptDAO.getReport(Id, from_date, to_date, comId);
	}

	@Override
	public List<SalesEntry> findAllCustomerWithDate(LocalDate from_date, LocalDate to_date, Long company_id) {
		return null;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		return receiptDAO.deleteByIdValue(entityId);
	}

	@Override
	public List<Receipt> findAllRecepitOfCompany(Long company_id, Boolean flag) {
		return receiptDAO.findAllRecepitOfCompany(company_id, flag);
	}

	@Override
	public List<Receipt> getAdvancePaymentList(Long customerId, Long companyId, Long yid) {
		/*List<SalesEntry> salesEntryList = salesEntryDao.findAllByCustomers(customerId, companyId);*/
		List<Long> receiptIds = new ArrayList<Long>();
		/*if (salesEntryList != null) {
			for (SalesEntry salesEntry : salesEntryList) {
				if (salesEntry.getReceipts() != null) {
					for (Receipt receipt : salesEntry.getReceipts()) {
						if(receipt.getEntry_status()!=4)
						{
						     receiptIds.add(receipt.getReceipt_id());
						}
					}
				}
			}
		}*/
		return receiptDAO.getAdvanceReceiptList(receiptIds, customerId, companyId,yid);
	}

	@Override
	public List<Receipt> getIncome(Long yearId, Long companyId) {
		List<Receipt> incomes = new ArrayList<Receipt>();
		List<Receipt> receipts = receiptDAO.getIncomeByYearId(yearId, companyId);
		for (Receipt receipt : receipts) {
			boolean isExists = false;
			if (incomes.size() > 0) {
				for (Receipt income : incomes) {
					if (receipt.getSales_bill_id() != null) {
						isExists = true;
						income.setAmount(income.getAmount() + receipt.getRound_off());
					} else if (receipt.getSubLedger() != null) {
						if (income.getSubLedger().getSubledger_Id() == receipt.getSubLedger().getSubledger_Id()) {
							isExists = true;
							income.setAmount(income.getAmount() + receipt.getRound_off());
						}
					}
				}
			} else if (!isExists) {
				incomes.add(receipt);
			}
		}
		return incomes;
	}

	@Override
	public Receipt findOneWithAll(Long rId) {

		return receiptDAO.findOneWithAll(rId);
	}

	@Override
	public Long saveReceiptThroughExcel(Receipt receipt) {

		return receiptDAO.saveReceiptThroughExcel(receipt);
	}

	@Override
	public void saveReceipt_product_detailsThroughExcel(Receipt_product_details entity) {
		receiptDAO.saveReceipt_product_detailsThroughExcel(entity);

	}

	@Override
	public void updateReceiptThroughExcel(Receipt receipt) {
		receiptDAO.updateReceiptThroughExcel(receipt);

	}

	@Override
	public void updateReceipt_product_detailsThroughExcel(Receipt_product_details entity) {
		receiptDAO.updateReceipt_product_detailsThroughExcel(entity);

	}

	@Override
	public List<Receipt> getDayBookReport(LocalDate from_date,LocalDate to_date, Long companyId) {
		return receiptDAO.getDayBookReport(from_date, to_date,companyId);
	}

	@Override
	public List<Receipt> findAllRecepitsOfCompany(Long company_id) {

		return receiptDAO.findAllRecepitsOfCompany(company_id);
	}

	@Override
	public List<Receipt> getCashBookBankBookReport(LocalDate from_date, LocalDate to_date, Long companyId,
			Integer type) {
		return receiptDAO.getCashBookBankBookReport(from_date, to_date, companyId, type);
	}

	@Override
	public Receipt_product_details editproductdetailforReceipt(Long entryId) {
		
		return receiptDAO.editproductdetailforReceipt(entryId);
	}

	@Override
	public void updateReceipt(Receipt entry) {
		receiptDAO.updateReceipt(entry);
		
	}

	@Override
	public void updateReceipt_product_details(Receipt_product_details entry) {
		receiptDAO.updateReceipt_product_details(entry);
		
	}

	@Override
	public double findpaidtds(Long sales_id) {
		// TODO Auto-generated method stub
		return receiptDAO.findpaidtds(sales_id);
	}

	@Override
	public List<Receipt> findallreceiptentryofsales(long id) {
		// TODO Auto-generated method stub
		return receiptDAO.findallreceiptentryofsales(id);
	}

	@Override
	public void diactivateByIdValue(Long entityId,Boolean isDelete) {
		receiptDAO.diactivateByIdValue(entityId,isDelete);	
	}

	@Override
	public void activateByIdValue(Long entityId) {
		receiptDAO.activateByIdValue(entityId);		
	}

	@Override
	public void changeStatusOfReceiptThroughExcel(Receipt receipt) {
		receiptDAO.changeStatusOfReceiptThroughExcel(receipt);
		
	}

	@Override
	public List<Receipt> getReceiptListForLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId,Long bank_id) {
		// TODO Auto-generated method stub
		return receiptDAO.getReceiptListForLedgerReport(from_date, to_date, companyId,bank_id);
	}

	@Override
	public Receipt findOne(Long receipt_id) {
		Receipt receipt=new Receipt();
		// TODO Auto-generated method stub
		try {
			receipt= receiptDAO.findOne(receipt_id);
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return receipt;
	}

	@Override
	public List<Receipt> CashReceiptOfMoreThanRS10000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId) {
		// TODO Auto-generated method stub
		return receiptDAO.CashReceiptOfMoreThanRS10000AndAbove(from_date,to_date,companyId);
	}

	@Override
	public List<Receipt> customerReceiptHavingUnadjustedUnadjusted(LocalDate from_date, LocalDate to_date,
			Long companyId) {
		// TODO Auto-generated method stub
		return receiptDAO.customerReceiptHavingUnadjustedUnadjusted(from_date, to_date, companyId);
	}

	@Override
	public List<Receipt> getReceiptForLedgerReport(LocalDate from_date, LocalDate to_date, Long customerId,
			Long companyId) {
		// TODO Auto-generated method stub
		return receiptDAO.getReceiptForLedgerReport(from_date, to_date, customerId, companyId);
	}

	@Override
	public List<Receipt> customerReceiptHavingGST0(LocalDate from_date, LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		return receiptDAO.customerReceiptHavingGST0(from_date, to_date, companyId);
	}
	@Override
	public List<Receipt> customerReceiptsWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,
			LocalDate to_date, Long companyId) {
		
		return receiptDAO.customerReceiptWithSubledgerTransactionsRs300000AndAbove(from_date, to_date, companyId);
	}
	@Override
	public List<Receipt> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate from_date, LocalDate to_date,
			Long companyId) {
		
		return receiptDAO.supplierHavingGSTApplicbleAsNOAndExceedingZERO(from_date, to_date, companyId);
	}

	@Override
	public Receipt isExcelVocherNumberExist(String vocherNo, Long companyId) {
		// TODO Auto-generated method stub
		return receiptDAO.isExcelVocherNumberExist(vocherNo, companyId);
	}

	@Override
	public List<Receipt> getAllReceiptsAgainstAdvanceReceipt(Long advanceReceiptId) {
		// TODO Auto-generated method stub
		return receiptDAO.getAllReceiptsAgainstAdvanceReceipt(advanceReceiptId);
	}

	@Override
	public List<Receipt> getAllAdvanceReceiptsAgainstCustomer(Long companyId,Long customer_id) {
		// TODO Auto-generated method stub
		return receiptDAO.getAllAdvanceReceiptsAgainstCustomer(companyId,customer_id);
	}

	@Override
	public List<Receipt> getAllOpeningBalanceAgainstReceiptForPeriod(Long customerId, Long companyId,
			LocalDate toDate) {
		// TODO Auto-generated method stub
		return receiptDAO.getAllOpeningBalanceAgainstReceiptForPeriod(customerId, companyId, toDate);
	}



}