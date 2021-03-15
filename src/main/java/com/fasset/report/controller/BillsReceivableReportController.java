package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.BillsReceivableReportFormValidator;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Customer;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.User;
import com.fasset.form.BillsReceivableForm;
import com.fasset.form.BillsReceivableReportForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.service.CreditNoteServiceImpl;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;

@Controller
@SessionAttributes("user")
public class BillsReceivableReportController extends MyAbstractController {

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private ISalesEntryService salesEntryService;

	@Autowired
	private IReceiptService receiptService;
	@Autowired	
	private ICreditNoteService creditService;
	@Autowired
	private ICompanyService companyService;

	@Autowired
	private BillsReceivableReportFormValidator validator;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IOpeningBalancesService openingbalances;
	
	private List<BillsReceivableForm> billsReceivable = new ArrayList<BillsReceivableForm>();
	private List<Company> companyList = new ArrayList<Company>();
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;

	@RequestMapping(value = "billsReceivableReport", method = RequestMethod.GET)
	public ModelAndView billsReceivableReport(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		Long role = user.getRole().getRole_id();
		ModelAndView model = new ModelAndView();
		BillsReceivableReportForm form = new BillsReceivableReportForm();

		if (role.equals(ROLE_SUPERUSER)) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("companyList", companyList);
			model.addObject("form", form);
			model.setViewName("/report/billsReceivableReportForKpo");
		} else if (role.equals(ROLE_EXECUTIVE) || role.equals(ROLE_MANAGER)) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("companyList", companyList);
			model.addObject("form", form);
			model.setViewName("/report/billsReceivableReportForKpo");
		} else {
			List<Customer> customerList = customerService.findAllCustomersOfCompany(user.getCompany().getCompany_id());
			model.addObject("customerList", customerList);
			model.addObject("form", form);
			model.setViewName("/report/billsReceivableReport");
		}
		return model;
	}

	@RequestMapping(value = "showBillsReceivableReport", method = RequestMethod.POST)
	public ModelAndView showBillsReceivableReport(@ModelAttribute("form") BillsReceivableReportForm form,
			BindingResult result, HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		Long role = user.getRole().getRole_id();
		long company_id = (long) session.getAttribute("company_id");

		Double total = 0.0;
		Map<String, Double> billsReceivable1 = new HashMap<String, Double>();
		 Double receiptAmountForSales= 0.0;
		  Long salesId;
		  SalesEntry Receipt_saleId;
		  Long ReceiptSalesId;
		  Double saleAmount;
		  String voucherNo="";

		validator.validate(form, result);
		if (result.hasErrors()) {
			if (role == ROLE_SUPERUSER) {
				companyList = companyService.getAllCompaniesOnly();
				model.addObject("companyList", companyList);
				model.addObject("form", form);
				model.setViewName("/report/billsReceivableReportForKpo");
			} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				companyList = companyService.getAllCompaniesExe(user.getUser_id());
				model.addObject("companyList", companyList);
				model.addObject("form", form);
				model.setViewName("/report/billsReceivableReportForKpo");
			} else {
				List<Customer> customerList = customerService
						.findAllCustomersOfCompany(user.getCompany().getCompany_id());
				model.addObject("customerList", customerList);
				model.addObject("form", form);
				model.setViewName("/report/billsReceivableReport");
			}
		} else {
			
			if (role == ROLE_SUPERUSER || role == ROLE_EXECUTIVE || role == ROLE_MANAGER) 
			{
			try {
				
				company = companyService.getCompanyWithCompanyStautarType(form.getClientId());
				fromDate=company.getOpeningbalance_date();
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			else 
			{
			
				company=companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());
				fromDate=company.getOpeningbalance_date();
				System.out.println("fromDate"+fromDate);
			}
			toDate = form.getToDate();
			System.out.println("The todate is" + toDate);
			System.out.println("The todate is" + fromDate);
			// client_id & customer_idIncome type
			System.out.println("Cust id bp"+form.getClientId());
			if (form.getClientId() != null && form.getClientId() > 0) 
			{
				System.out.println("1");
				
			 	billsReceivable.clear();
				//billsReceivable = salesEntryService.getBillsReceivable(form.getCustomerId(), fromDate, form.getToDate(),form.getClientId());
				
				if(form.getCustomerId()>0)
				{
					
				Customer customer = customerService.findOneWithAll(form.getCustomerId());
			
					double advanceReceiptTotalAmountAgainstCustomer = 0.0 ;
					Double openingBalance = 0.0;
					
					OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSalesForPeriod(fromDate, form.getClientId(), customer.getCustomer_id(),toDate);
					
					if(salesopening!=null && salesopening.getDebit_balance()!=null && salesopening.getCredit_balance()!=null)
					{
						openingBalance =(salesopening.getDebit_balance())+(-salesopening.getCredit_balance());
					}
					System.out.println("The opening balance is "+openingBalance);
					 
					//Double salesAmount= 0.0;
						 List<SalesEntry> salesaginstopening=salesEntryService.getAllSalesAmount(customer.getCustomer_id(), form.getClientId(),toDate);
							
						//********** commented on 22.10.2019
							// for(SalesEntry sales:salesaginstopening) {
						//  System.out.println("The slaes amt is"+sales.getRound_off());
							//  salesAmount=salesAmount+sales.getRound_off();
						 // }
							//********** commented on 22.10.2019
				  // openingBalance = openingBalance+salesAmount;
						 
						  Double receiptAmount= 0.0;
						  Double creditnoteAmount=0.0;
				   List<Receipt> recceiptaginstopening= receiptService.getAllOpeningBalanceAgainstReceiptForPeriod(customer.getCustomer_id(), form.getClientId(),toDate);
					  List<CreditNote> creditNoteagainstopening=creditService.getAllOpeningBalanceAgainstCreditNoteForPeriod(customer.getCustomer_id(), form.getClientId(),toDate);
					  for(Receipt receipt:recceiptaginstopening) {
						  receiptAmount=receiptAmount+receipt.getAmount(); 
					  }
					  for(CreditNote creditnote:creditNoteagainstopening) {
						  creditnoteAmount=creditnoteAmount+creditnote.getRound_off(); 
					  }
					  openingBalance = openingBalance-receiptAmount;
					  openingBalance = openingBalance-creditnoteAmount;//credit note against opening balance
					  
					  if(openingBalance!=0)
					  {
					  BillsReceivableForm entry = new BillsReceivableForm();
					  entry.setCreated_date(fromDate.plusDays(1));
					  entry.setRound_off(openingBalance);
					  entry.setVoucher_no("Opening Balance");
					  entry.setCustomer(customer);
					  billsReceivable.add(entry);
					  }
					 
					  List<Receipt> recceiptaginstsales= receiptService.getReceiptForSales(customer.getCustomer_id(), form.getClientId(),toDate);
					  List<CreditNote> creditNoteSales= creditService.getCreditNoteForSale(form.getClientId(),toDate);
					  for(SalesEntry sales:salesaginstopening) {
						  salesId=sales.getSales_id();
						  saleAmount=sales.getRound_off();
						  voucherNo=sales.getVoucher_no();
						  System.out.println("BR sales id is " + salesId);
						  LocalDate createdDt=sales.getCreated_date();
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
					  System.out.println("after rect "+saleAmount);
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
					  System.out.println("after CN "+saleAmount);
					  if (!voucherNo.isEmpty()){
						  System.out.println("Sale id and Amount " +salesId + saleAmount);
						  BillsReceivableForm entry = new BillsReceivableForm();
						  entry.setCreated_date(createdDt);
						  entry.setRound_off(saleAmount);
						  entry.setVoucher_no(voucherNo);
						  entry.setCustomer(customer);
						  billsReceivable.add(entry);
					  }
					  }
					List<Receipt> list = receiptService.getAllAdvanceReceiptsAgainstCustomer(form.getClientId(), customer.getCustomer_id());
					for(Receipt receipt : list)
					{
						if(receipt.getTds_amount()!=null)
							advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount()+receipt.getTds_amount(); 
							else
								advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount();
						
						 if(advanceReceiptTotalAmountAgainstCustomer>0)
						  {
						  BillsReceivableForm entry = new BillsReceivableForm();
						  entry.setCreated_date(receipt.getDate());
						  entry.setRound_off(-advanceReceiptTotalAmountAgainstCustomer);
						  entry.setVoucher_no(receipt.getVoucher_no());
						  entry.setCustomer(customer);
						  entry.setParticulars("Advance Pending");
						  billsReceivable.add(entry);
						  }
					}
					
					Collections.sort(billsReceivable, new Comparator<BillsReceivableForm>() {
			            public int compare(BillsReceivableForm form1, BillsReceivableForm form2) {
			                LocalDate date1 = form1.getCreated_date();
			                LocalDate date2 = form2.getCreated_date();
			                return date1.compareTo(date2);
			            }
			        });
				 model.addObject("billsReceivable", billsReceivable);
		      	}
				else
				{
					List<Customer> customerList = customerService.findAllCustomersOfCompany(form.getClientId());
					 List<SalesEntry> billsReceivable=salesEntryService.getAllSalesAmount(form.getCustomerId(), form.getClientId(),toDate);
						//	System.out.println("BR sales list in 2 is"+salesaginstopening.size());
System.out.println("Customer Id is" +form.getCustomerId());
System.out.println("The client is "+form.getClientId());
						 
						  List<Receipt> recceiptaginstsales= receiptService.getReceiptForSales(form.getCustomerId(), form.getClientId(),toDate);
						  List<CreditNote> creditNoteSales= creditService.getCreditNoteForSale(form.getClientId(),toDate);
					System.out.println("admin else");
					 double amount = 0.0 ;
					 double sale_amount=0.0;
						for (Customer customer : customerList) {

							for (SalesEntry br : billsReceivable) {

								if (br.getCustomer().getCustomer_id().equals(customer.getCustomer_id())) 
								{
		//
									System.out.println("admin else if br.getcustomer " +br.getSales_id());
									sale_amount=br.getRound_off();;
									System.out.println("The sale amount is " + sale_amount);
									for(Receipt receipt:recceiptaginstsales) {
										
										  Receipt_saleId=receipt.getSales_bill_id();
										 if( receipt.getSales_bill_id()!=null){
											 
										  ReceiptSalesId= Receipt_saleId.getSales_id();
										 
										  if (br.getSales_id().equals(ReceiptSalesId)==true){
										  receiptAmountForSales=receipt.getAmount(); 
										  System.out.println("Receipt is " +ReceiptSalesId + " "+receiptAmountForSales);
										  sale_amount=sale_amount-receiptAmountForSales;
										 
										  }
									  }
									  }
									for(CreditNote creditnote:creditNoteSales) {
										
										  Receipt_saleId=creditnote.getSales_bill_id();
										 if( creditnote.getSales_bill_id()!=null){
											 
										  ReceiptSalesId= Receipt_saleId.getSales_id();
										 
										  if (br.getSales_id().equals(ReceiptSalesId)==true){
										  receiptAmountForSales=creditnote.getRound_off(); 
										 
										  sale_amount=sale_amount-receiptAmountForSales;
										 
										  }
									  }
									  }
									if(billsReceivable1.containsKey(br.getCustomer().getFirm_name()))
									{
										amount = billsReceivable1.get(br.getCustomer().getFirm_name());
										amount += sale_amount;
										 System.out.println("Sale Amount " +br.getSales_id() + sale_amount);
										billsReceivable1.put(br.getCustomer().getFirm_name(), amount);
									}
									else
									{
										
										double advanceReceiptTotalAmountAgainstCustomer = 0.0 ;
										Double openingBalance = 0.0;
										
										OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSalesForPeriod(fromDate, form.getClientId(), customer.getCustomer_id(),toDate);
										if(salesopening!=null && salesopening.getDebit_balance()!=null && salesopening.getCredit_balance()!=null)
										{
											openingBalance =(salesopening.getDebit_balance())+(-salesopening.getCredit_balance());
										}
										
										
										Double salesAmount= 0.0;
											/* List<SalesEntry> salesaginstopening=salesEntryService.getAllOpeningBalanceAgainstSales(customer.getCustomer_id(), form.getClientId());
											  
											  for(SalesEntry sales:salesaginstopening) {
											  
												  salesAmount=salesAmount+sales.getRound_off();
											  }
									   openingBalance = openingBalance+salesAmount;*/

										
									  
									  
									  
									  
									
									  
									   Double receiptAmount= 0.0;
									   Double creditnoteAmount=0.0; 
									   List<Receipt> recceiptaginstopening= receiptService.getAllOpeningBalanceAgainstReceiptForPeriod(customer.getCustomer_id(), form.getClientId(),toDate);
									   List<CreditNote> creditNoteagainstopening=creditService.getAllOpeningBalanceAgainstCreditNoteForPeriod(customer.getCustomer_id(), form.getClientId(),toDate);
										  for(Receipt receipt:recceiptaginstopening) {
											  receiptAmount=receiptAmount+receipt.getAmount(); 
										  }
										  openingBalance = openingBalance-receiptAmount;
										  for(CreditNote creditnote:creditNoteagainstopening) {
											  creditnoteAmount=creditnoteAmount+creditnote.getRound_off(); 
										  }
										   openingBalance = openingBalance-creditnoteAmount;//credit note against opening balance
										List<Receipt> list = receiptService.getAllAdvanceReceiptsAgainstCustomer(form.getClientId(), customer.getCustomer_id());
										for(Receipt receipt : list)
										{
											if(receipt.getTds_amount()!=null)
												advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount()+receipt.getTds_amount(); 
												else
													advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount();
										}
										 System.out.println("Sale Amount " +br.getSales_id() + sale_amount);
										billsReceivable1.put(br.getCustomer().getFirm_name(), (sale_amount+(-advanceReceiptTotalAmountAgainstCustomer)+openingBalance));
									}
									
								}
							}
							
							if(form.getCustomerId().equals(new Long(-1)))
							{
							if(!billsReceivable1.containsKey(customer.getFirm_name()))
							{
								
								double advanceReceiptTotalAmountAgainstCustomer = 0.0 ;
								List<Receipt> list = receiptService.getAllAdvanceReceiptsAgainstCustomer(form.getClientId(), customer.getCustomer_id());
								for(Receipt receipt : list)
								{
									if(receipt.getTds_amount()!=null)
										advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount()+receipt.getTds_amount(); 
										else
											advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount();
								}
								
								
								
								OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSalesForPeriod(fromDate, form.getClientId(), customer.getCustomer_id(),toDate);
								
								if(salesopening!=null && salesopening.getDebit_balance()!=null && salesopening.getCredit_balance()!=null)
								{
									advanceReceiptTotalAmountAgainstCustomer =-advanceReceiptTotalAmountAgainstCustomer+(salesopening.getDebit_balance())+(-salesopening.getCredit_balance());
								}
								
								//*** commented for Bill receivable issue for Admin Login For All clients
								
								/*Double salesAmount= 0.0;
								 List<SalesEntry> salesaginstopening=salesEntryService.getAllOpeningBalanceAgainstSales(customer.getCustomer_id(), form.getClientId());
								  
								  for(SalesEntry sales:salesaginstopening) {
								  
									  salesAmount=salesAmount+sales.getRound_off();
								  }*/
								
								 
								  
								  
								  
								 // advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer+salesAmount;
							
						   Double receiptAmount= 0.0;
						   Double creditnoteAmount=0.0; 
						   List<Receipt> recceiptaginstopening= receiptService.getAllOpeningBalanceAgainstReceiptForPeriod(customer.getCustomer_id(), form.getClientId(),toDate);
						   List<CreditNote> creditNoteagainstopening=creditService.getAllOpeningBalanceAgainstCreditNoteForPeriod(customer.getCustomer_id(), form.getClientId(),toDate);
							  for(Receipt receipt:recceiptaginstopening) {
								  receiptAmount=receiptAmount+receipt.getAmount(); 
							  }
							  for(CreditNote creditnote:creditNoteagainstopening) {
								  creditnoteAmount=creditnoteAmount+creditnote.getRound_off(); 
							  }
							  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer-receiptAmount;
							  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer-creditnoteAmount;
								
								if(advanceReceiptTotalAmountAgainstCustomer!=0)
								{
									billsReceivable1.put(customer.getFirm_name(), (advanceReceiptTotalAmountAgainstCustomer));
								}
							}
							}
							else 
							{
								if(form.getCustomerId().equals(customer.getCustomer_id()))
								{
								if(!billsReceivable1.containsKey(customer.getFirm_name()))
								{
									double advanceReceiptTotalAmountAgainstCustomer = 0.0 ;
									List<Receipt> list = receiptService.getAllAdvanceReceiptsAgainstCustomer(form.getClientId(), customer.getCustomer_id());
									for(Receipt receipt : list)
									{
										if(receipt.getTds_amount()!=null)
											advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount()+receipt.getTds_amount(); 
											else
												advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount();
									}
									
									
									
									OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSalesForPeriod(fromDate, form.getClientId(), customer.getCustomer_id(),toDate);
									
									if(salesopening!=null && salesopening.getDebit_balance()!=null && salesopening.getCredit_balance()!=null)
									{
										advanceReceiptTotalAmountAgainstCustomer =-advanceReceiptTotalAmountAgainstCustomer+(salesopening.getDebit_balance())+(-salesopening.getCredit_balance());
									}
									
									Double salesAmount= 0.0;
									 List<SalesEntry> salesaginstopening=salesEntryService.getAllOpeningBalanceAgainstSales(customer.getCustomer_id(), form.getClientId());
									  
									  for(SalesEntry sales:salesaginstopening) {
									  
										  salesAmount=salesAmount+sales.getRound_off();
									  }
									  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer+salesAmount;
								
							   Double receiptAmount= 0.0;
							   Double creditnoteAmount=0.0;
							   List<Receipt> recceiptaginstopening= receiptService.getAllOpeningBalanceAgainstReceiptForPeriod(customer.getCustomer_id(), form.getClientId(),toDate);
							   List<CreditNote> creditNoteagainstopening=creditService.getAllOpeningBalanceAgainstCreditNoteForPeriod(customer.getCustomer_id(), form.getClientId(),toDate);
								  for(Receipt receipt:recceiptaginstopening) {
									  receiptAmount=receiptAmount+receipt.getAmount(); 
								  }
								  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer-receiptAmount;
								  for(CreditNote creditnote:creditNoteagainstopening) {
									  creditnoteAmount=creditnoteAmount+creditnote.getRound_off(); 
								  }
								  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer-creditnoteAmount;
									if(advanceReceiptTotalAmountAgainstCustomer!=0)
									{
										billsReceivable1.put(customer.getFirm_name(), (advanceReceiptTotalAmountAgainstCustomer));
									}
									
								}
								}
							}
						}
				}
				 
				
			} else {
				
				System.out.println("2");
				
				billsReceivable.clear();
				//billsReceivable = salesEntryService.getBillsReceivable(form.getCustomerId(), fromDate, form.getToDate(),company_id);

				if(form.getCustomerId()>0)
				{
					System.out.println("Selected customer");
				Customer customer = customerService.findOneWithAll(form.getCustomerId());
			Double Amt =0.0;
					double advanceReceiptTotalAmountAgainstCustomer = 0.0 ;
					Double openingBalance = 0.0;
					
					OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSalesForPeriod(fromDate, company_id, customer.getCustomer_id(),toDate);
					if(salesopening!=null && salesopening.getDebit_balance()!=null && salesopening.getCredit_balance()!=null)
					{
						openingBalance =(salesopening.getDebit_balance())+(-salesopening.getCredit_balance());
					}
					 System.out.println("The opening balance "+openingBalance);
					//*** commented on 22.10.2019 for point Bill receivable has to show all entries***
					Double salesAmount;
						// List<SalesEntry> salesaginstopening=salesEntryService.getAllOpeningBalanceAgainstSales(customer.getCustomer_id(), company_id);
					/*	  
						  for(SalesEntry sales:salesaginstopening) {
						  
							  salesAmount=salesAmount+sales.getRound_off();
						  }
				   openingBalance = openingBalance+salesAmount;*/
				
					
				   Double receiptAmount = 0.0;
				   Double creditnoteAmount=0.0; 
				   List<Receipt> recceiptaginstopening= receiptService.getAllOpeningBalanceAgainstReceiptForPeriod(customer.getCustomer_id(), company_id,toDate);
				   List<CreditNote> creditNoteagainstopening=creditService.getAllOpeningBalanceAgainstCreditNoteForPeriod(customer.getCustomer_id(), company_id,toDate);
				   
					  for(Receipt receipt:recceiptaginstopening) {
						  receiptAmount=receiptAmount+receipt.getAmount(); 
					  }
					  System.out.println("receiptAmount for period is " +receiptAmount);
					  openingBalance = openingBalance-receiptAmount;
					  
					  for(CreditNote creditnote:creditNoteagainstopening) {
						  creditnoteAmount=creditnoteAmount+creditnote.getRound_off(); 
					  }
					   openingBalance = openingBalance-creditnoteAmount;//credit note against opening balance
					  Amt=Amt+openingBalance;
					  System.out.println("The opening balance amount is "+Amt);
					  if(openingBalance!=0)
					  {
					  BillsReceivableForm entry = new BillsReceivableForm();
					  entry.setCreated_date(fromDate.plusDays(1));
					  entry.setRound_off(openingBalance);
					  entry.setVoucher_no("Opening Balance");
					  entry.setCustomer(customer);
					  billsReceivable.add(entry);
					  }
						 List<SalesEntry> salesaginstopening=salesEntryService.getAllSalesAmount(customer.getCustomer_id(), company_id,toDate);
							//	System.out.println("BR sales list in 2 is"+salesaginstopening.size());

							  List<Receipt> recceiptaginstsales= receiptService.getReceiptForSales(customer.getCustomer_id(), company_id,toDate);
							  List<CreditNote> creditNoteSales= creditService.getCreditNoteForSale(company_id,toDate);
							
							  for(SalesEntry sales:salesaginstopening) {
								  salesId=sales.getSales_id();
								  saleAmount=sales.getRound_off();
								  voucherNo=sales.getVoucher_no();
								  LocalDate createdDt=sales.getCreated_date();
									System.out.println("The voucher is "+ voucherNo);
							  for(Receipt receipt:recceiptaginstsales) {
								
								  Receipt_saleId=receipt.getSales_bill_id();
								 if( receipt.getSales_bill_id()!=null){
									 
								  ReceiptSalesId= Receipt_saleId.getSales_id();
								 
								  if (salesId.equals(ReceiptSalesId)==true){
								  receiptAmountForSales=receipt.getAmount(); 
									System.out.println("The Receipt id is "+ receipt.getReceipt_id());
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
						//System.out.println("The sales Amount is "+saleAmount);
							  if (saleAmount!=null){
								  BillsReceivableForm entry = new BillsReceivableForm();
								  entry.setCreated_date(createdDt);
								  entry.setRound_off(saleAmount);
								  entry.setVoucher_no(voucherNo);
								  entry.setCustomer(customer);
								  billsReceivable.add(entry);
							  }
							 
							  }
							  
							 System.out.println("size if BR "+billsReceivable.size());
					List<Receipt> list = receiptService.getAllAdvanceReceiptsAgainstCustomer(company_id, customer.getCustomer_id());
					Double receiptagainstCust=0.0;
					for(Receipt receipt : list)
					{
						if(receipt.getTds_amount()!=null){
							advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount()+receipt.getTds_amount(); receiptagainstCust=receiptagainstCust+receipt.getAmount()+receipt.getTds_amount();}
							else{
								advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount();receiptagainstCust=receiptagainstCust+receipt.getAmount();}
						
						 if(advanceReceiptTotalAmountAgainstCustomer>0)
						  {
						
						  BillsReceivableForm entry = new BillsReceivableForm();
						  entry.setCreated_date(receipt.getDate());
						  entry.setRound_off(-advanceReceiptTotalAmountAgainstCustomer);
						  entry.setVoucher_no(receipt.getVoucher_no());
						  entry.setCustomer(customer);
						  entry.setParticulars("Advance Pending");
						  Amt=Amt+entry.getRound_off();
						  billsReceivable.add(entry);
						  }
					}
				
					Collections.sort(billsReceivable, new Comparator<BillsReceivableForm>() {
			            public int compare(BillsReceivableForm form1, BillsReceivableForm form2) {
			                LocalDate date1 = form1.getCreated_date();
			                LocalDate date2 = form2.getCreated_date();
			                return date1.compareTo(date2);
			            }
			        });
					 System.out.println("size if BR now "+billsReceivable.size());
				 model.addObject("billsReceivable", billsReceivable);
		      	}
				else
				{
					List<Customer> customerList = customerService.findAllCustomersOfCompany(company_id);
					 List<SalesEntry> billsReceivable=salesEntryService.getAllSalesAmount(form.getCustomerId(), company_id,toDate);
						
System.out.println("cl company is" +company_id);
System.out.println("cl customer is "+form.getCustomerId());
					 
					  List<Receipt> recceiptaginstsales= receiptService.getReceiptForSales(form.getCustomerId(), company_id,toDate);
					  List<CreditNote> creditNoteSales= creditService.getCreditNoteForSale(company_id,toDate);
				
					double amount = 0.0 ;
					double sales_amount=0.0;
					for (Customer customer : customerList) {
                 System.out.println("The billreceivable rec " +billsReceivable.size());
						for (SalesEntry br : billsReceivable) {

							if (br.getCustomer().getCustomer_id().equals(customer.getCustomer_id())) 
							{
								sales_amount=br.getRound_off();
								 for(Receipt receipt:recceiptaginstsales) {
										
									  Receipt_saleId=receipt.getSales_bill_id();
									 if( receipt.getSales_bill_id()!=null){
										 
									  ReceiptSalesId= Receipt_saleId.getSales_id();
									 
									  if (br.getSales_id().equals(ReceiptSalesId)==true){
									  receiptAmountForSales=receipt.getAmount(); 
									 
									  sales_amount=sales_amount-receiptAmountForSales;
									
									  }
								  }
								  }
								 
								 for(CreditNote creditnote:creditNoteSales) {
										
									  Receipt_saleId=creditnote.getSales_bill_id();
									 if( creditnote.getSales_bill_id()!=null){
										 
									  ReceiptSalesId= Receipt_saleId.getSales_id();
									 
									  if (br.getSales_id().equals(ReceiptSalesId)==true){
									  receiptAmountForSales=creditnote.getRound_off(); 
									 
									  sales_amount=sales_amount-receiptAmountForSales;
									 
									  }
								  }
								  }
	//
								if(billsReceivable1.containsKey(br.getCustomer().getFirm_name()))
								{
									
									amount = billsReceivable1.get(br.getCustomer().getFirm_name());
									amount += sales_amount;
									billsReceivable1.put(br.getCustomer().getFirm_name(), amount);
								}
								else
								{
									
									double advanceReceiptTotalAmountAgainstCustomer = 0.0 ;
									Double openingBalance = 0.0;
									
									OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSalesForPeriod(fromDate, company_id, customer.getCustomer_id(),toDate);
									if(salesopening!=null && salesopening.getDebit_balance()!=null && salesopening.getCredit_balance()!=null)
									{
										openingBalance =(salesopening.getDebit_balance())+(-salesopening.getCredit_balance());
									}
									
									
									Double salesAmount= 0.0;
									//	 List<SalesEntry> salesaginstopening=salesEntryService.getAllOpeningBalanceAgainstSales(customer.getCustomer_id(), company_id);
											//*** commented on 22.10.2019 for point Bill receivable has to show all entries***
										 /* 
										  for(SalesEntry sales:salesaginstopening) {
										  
											  salesAmount=salesAmount+sales.getRound_off();
										  }
								   openingBalance = openingBalance+salesAmount;*/
											//*** commented on 22.10.2019 for point Bill receivable has to show all entries***
								  

										

									 
									 
									 
										 Double receiptAmount= 0.0;
										 Double creditnoteAmount=0.0; 
								   List<Receipt> recceiptaginstopening= receiptService.getAllOpeningBalanceAgainstReceiptForPeriod(customer.getCustomer_id(), company_id,toDate);
								   List<CreditNote> creditNoteagainstopening=creditService.getAllOpeningBalanceAgainstCreditNoteForPeriod(customer.getCustomer_id(), company_id,toDate);
								   
									  for(Receipt receipt:recceiptaginstopening) {
										  receiptAmount=receiptAmount+receipt.getAmount(); 
									  }
									  for(CreditNote creditnote:creditNoteagainstopening) {
										  creditnoteAmount=creditnoteAmount+creditnote.getRound_off(); 
									  }
									  openingBalance = openingBalance-receiptAmount;
									  openingBalance = openingBalance-creditnoteAmount;//credit note against opening balance
									List<Receipt> list = receiptService.getAllAdvanceReceiptsAgainstCustomer(company_id, customer.getCustomer_id());
									for(Receipt receipt : list)
									{
										if(receipt.getTds_amount()!=null)
											advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount()+receipt.getTds_amount(); 
											else
												advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount();
									}
									
									billsReceivable1.put(br.getCustomer().getFirm_name(), (sales_amount+(-advanceReceiptTotalAmountAgainstCustomer)+openingBalance));
								}
								
							}
						}
						
						if(form.getCustomerId().equals(new Long(-1)))
						{
							
						if(!billsReceivable1.containsKey(customer.getFirm_name()))
						{
							
							double advanceReceiptTotalAmountAgainstCustomer = 0.0 ;
							List<Receipt> list = receiptService.getAllAdvanceReceiptsAgainstCustomer(company_id, customer.getCustomer_id());
							for(Receipt receipt : list)
							{
								if(receipt.getTds_amount()!=null)
									advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount()+receipt.getTds_amount(); 
									else
										advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount();
							}
							
							
							System.out.println("foll code");
							System.out.println("The from Date is " +fromDate);
							System.out.println("The to Date is " +toDate);
							OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSalesForPeriod(fromDate, company_id, customer.getCustomer_id(),toDate);
							
							if(salesopening!=null && salesopening.getDebit_balance()!=null && salesopening.getCredit_balance()!=null)
							{
								advanceReceiptTotalAmountAgainstCustomer =-advanceReceiptTotalAmountAgainstCustomer+(salesopening.getDebit_balance())+(-salesopening.getCredit_balance());
							}
							
							Double salesAmount;
							
							
							
							 
							  
							 /* for(SalesEntry sales:salesaginstopening) {
							  
								  salesAmount=salesAmount+sales.getRound_off();
							  }*/
							
					   Double receiptAmount= 0.0;
					   Double creditnoteAmount=0.0; 
					   List<Receipt> recceiptaginstopening= receiptService.getAllOpeningBalanceAgainstReceiptForPeriod(customer.getCustomer_id(), company_id,toDate);
					   List<CreditNote> creditNoteagainstopening=creditService.getAllOpeningBalanceAgainstCreditNoteForPeriod(customer.getCustomer_id(), company_id,toDate);  
						  for(Receipt receipt:recceiptaginstopening) {
							  receiptAmount=receiptAmount+receipt.getAmount(); 
							  
						  }
						  for(CreditNote creditnote:creditNoteagainstopening) {
							  creditnoteAmount=creditnoteAmount+creditnote.getRound_off(); 
						  }
						  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer-receiptAmount;
						  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer-creditnoteAmount;
						 // System.out.println("adva rece after adv rec" +advanceReceiptTotalAmountAgainstCustomer);
							
							if(advanceReceiptTotalAmountAgainstCustomer!=0)
							{
								
								billsReceivable1.put(customer.getFirm_name(), (advanceReceiptTotalAmountAgainstCustomer));
							}
						}
						}
						else 
						{
						
							if(form.getCustomerId().equals(customer.getCustomer_id()))
							{
							if(!billsReceivable1.containsKey(customer.getFirm_name()))
							{
								
								
								double advanceReceiptTotalAmountAgainstCustomer = 0.0 ;
								List<Receipt> list = receiptService.getAllAdvanceReceiptsAgainstCustomer(company_id, customer.getCustomer_id());
								for(Receipt receipt : list)
								{
									if(receipt.getTds_amount()!=null)
										advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount()+receipt.getTds_amount(); 
										else
											advanceReceiptTotalAmountAgainstCustomer += receipt.getAmount();
								}
								
								
								
								OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSalesForPeriod(fromDate, company_id, customer.getCustomer_id(),toDate);
								
								if(salesopening!=null && salesopening.getDebit_balance()!=null && salesopening.getCredit_balance()!=null)
								{
									advanceReceiptTotalAmountAgainstCustomer =-advanceReceiptTotalAmountAgainstCustomer+(salesopening.getDebit_balance())+(-salesopening.getCredit_balance());
								}
								//*** commented for bill receivable iss
								/*Double salesAmount= 0.0;
								 List<SalesEntry> salesaginstopening=salesEntryService.getAllOpeningBalanceAgainstSales(customer.getCustomer_id(), company_id);
								  
								  for(SalesEntry sales:salesaginstopening) {
								  
									  salesAmount=salesAmount+sales.getRound_off();
								  }
								  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer+salesAmount;
			*/
								Double salesAmount= 0.0;
								
								 
								
								  
								 
						   Double receiptAmount= 0.0;
						   Double creditnoteAmount=0.0; 
						   List<Receipt> recceiptaginstopening= receiptService.getAllOpeningBalanceAgainstReceiptForPeriod(customer.getCustomer_id(), company_id,toDate);
						   List<CreditNote> creditNoteagainstopening=creditService.getAllOpeningBalanceAgainstCreditNoteForPeriod(customer.getCustomer_id(), company_id,toDate);  
							  for(Receipt receipt:recceiptaginstopening) {
								  receiptAmount=receiptAmount+receipt.getAmount(); 
							  }
							  for(CreditNote creditnote:creditNoteagainstopening) {
								  creditnoteAmount=creditnoteAmount+creditnote.getRound_off(); 
							  }
							  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer-receiptAmount;
							  advanceReceiptTotalAmountAgainstCustomer = advanceReceiptTotalAmountAgainstCustomer-creditnoteAmount;
								
								if(advanceReceiptTotalAmountAgainstCustomer!=0)
								{
									billsReceivable1.put(customer.getFirm_name(), (advanceReceiptTotalAmountAgainstCustomer));
								}
							
							}
							}
						}
					}
				}
				
				 
			}
			System.out.println("The option is " +form.getCustomerId());
			model.addObject("option", form.getCustomerId());
			model.addObject("company", company);
			model.addObject("from_date", fromDate);
			model.addObject("to_date", toDate);
		    model.addObject("billsReceivable1", billsReceivable1);
			model.setViewName("/report/billsReceivableReportList");
		}

		return model;
	}

}



