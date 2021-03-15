package com.fasset.report.controller;

import java.util.ArrayList;
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
import com.fasset.controller.validators.BillsPayableReportValidator;
import com.fasset.entities.Company;
import com.fasset.entities.DebitNote;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.form.BillsPayableReportForm;
import com.fasset.form.BillsReceivableForm;
import com.fasset.form.ProfitAndLossReportForm;
import com.fasset.service.DebitNoteServiceImpl;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IDebitNoteService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.ISuplliersService;

@Controller
@SessionAttributes("user")
public class BillsPayableReportController extends MyAbstractController{
	
	@Autowired
	private IPurchaseEntryService purchaseEntryService;
	
	@Autowired
	private ISuplliersService supplierService;
	
	@Autowired
	private BillsPayableReportValidator validator;
	
	@Autowired
	private ICompanyService companyService ;
	
	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IDebitNoteService deibitNote;
	@Autowired
	private IOpeningBalancesService openingbalances;
	
	private List<BillsReceivableForm> billsPayable = new ArrayList<BillsReceivableForm>();
	private List<Company> companyList = new ArrayList<Company>();
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;
	private Map<String, Double> billsPayable1 = new HashMap<String, Double>();
	private Long option;
	@RequestMapping(value = "billsPayableReport", method = RequestMethod.GET)
	public ModelAndView billsPayableReport(HttpServletRequest request, HttpServletResponse response) {
		
		
		HttpSession session = request.getSession(true);
		User user  = (User) session.getAttribute("user");
		Long role = user.getRole().getRole_id();
		ModelAndView model = new ModelAndView();
		BillsPayableReportForm form = new BillsPayableReportForm();
		
		if (role.equals(ROLE_SUPERUSER))
		{
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("companyList", companyList);
			model.addObject("form", form);
			model.setViewName("/report/billsPayableReportForKpo");
		}
		else if(role.equals(ROLE_EXECUTIVE) || role.equals(ROLE_MANAGER)){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("companyList", companyList);
			model.addObject("form", form);
			model.setViewName("/report/billsPayableReportForKpo");
		}
		else {
			List<Suppliers> supplierList = supplierService.findAllSuppliersOfCompany(user.getCompany().getCompany_id());
			model.addObject("supplierList", supplierList);
			model.addObject("form", form);
			model.setViewName("/report/billsPayableReport");
		}
		return model;
	}
	
	@RequestMapping(value = "showBillsPayableReport", method = RequestMethod.POST)
	public ModelAndView showBillsPayableReport(@ModelAttribute("form")BillsPayableReportForm form , 
			BindingResult result, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user  = (User) session.getAttribute("user");
		Long role = user.getRole().getRole_id();		
		long company_id=(long)session.getAttribute("company_id");
		
		Double total = 0.0;
		
		billsPayable1.clear();
		option=form.getSupplierId();
		validator.validate(form, result);
		if(result.hasErrors()){
			if (role == ROLE_SUPERUSER) {
				companyList = companyService.getAllCompaniesOnly();
				model.addObject("companyList", companyList);
				model.addObject("form", form);
				model.setViewName("/report/billsPayableReportForKpo");
			}
			else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
				companyList = companyService.getAllCompaniesExe(user.getUser_id());
				model.addObject("companyList", companyList);
				model.addObject("form", form);
				model.setViewName("/report/billsPayableReportForKpo");
			}
			else {
				List<Suppliers> supplierList = supplierService.findAllSuppliersOfCompany(user.getCompany().getCompany_id());
				model.addObject("supplierList", supplierList);
				model.addObject("form", form);
				model.setViewName("/report/billsPayableReport");
			}
		}
		else{
			
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
			}
			toDate = form.getToDate();
			Long purchaseId;
			Double purchaseAmount= 0.0;
			Long payment_purchaseId;
			String paymentPurchaseId;
			
			
		
			if(form.getClientId() != null && form.getClientId() > 0) {
			
				
				billsPayable.clear();
				
				
				if(form.getSupplierId()>0)
				{
					
					Suppliers sup = supplierService.findOneWithAll(form.getSupplierId());
					
					double advancePaymentTotalAmountAgainstSupplier = 0.0 ;
					Double openingBalance = 0.0;
					
					OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(fromDate,form.getClientId(), sup.getSupplier_id()); 
					
					if(purchaseopening!=null && purchaseopening.getDebit_balance()!=null && purchaseopening.getCredit_balance()!=null)
					{
						openingBalance =(-purchaseopening.getDebit_balance())+purchaseopening.getCredit_balance();
					}
						
					List<PurchaseEntry> purchaseaginstopening =	purchaseEntryService.getAllOpeningBalanceAgainstPurchase(sup.getSupplier_id(), form.getClientId());
					List<PurchaseEntry> purchaseForperiod=purchaseEntryService.getPurchaseForLedgerReport1(toDate, form.getSupplierId(), form.getClientId());
					List<Payment> paymentagainstPurchase=paymentService.getPaymentForLedgerReport1( toDate, form.getSupplierId(), form.getClientId());
					List<DebitNote> debitNoteforPurchase =deibitNote.getDebitNoteForLedgerReport(fromDate, toDate, form.getSupplierId(), form.getClientId());
									/*for(PurchaseEntry purchase:purchaseaginstopening) 
					{
						purchaseAmount=purchaseAmount+purchase.getRound_off();
					}
					openingBalance=openingBalance+purchaseAmount;*/
				
					
					List<Payment> paymentaginstopening=	paymentService.getAllOpeningBalanceAgainstPaymentForPeriod(sup.getSupplier_id(), form.getClientId(),toDate);
					List<DebitNote> debitnoteaginstopening=	deibitNote.getAllOpeningBalanceAgainstDebitNoteForPeriod(sup.getSupplier_id(), form.getClientId(),toDate);
					for(Payment payment:paymentaginstopening)
					{
						openingBalance=openingBalance-payment.getAmount();
					}
					for(DebitNote debitnote:debitnoteaginstopening)
					{
						openingBalance=openingBalance-debitnote.getRound_off();
					}
					if(openingBalance!=0)
					{
					  BillsReceivableForm entry = new BillsReceivableForm();
					  entry.setCreated_date(fromDate.plusDays(1));
					  entry.setRound_off(openingBalance);
					  entry.setVoucher_no("Opening Balance");
					  entry.setSupplier(sup);
					  billsPayable.add(entry);
					}
					 for(PurchaseEntry purchase:purchaseForperiod) 
					{
					purchaseId=	purchase.getPurchase_id();	
					
					purchaseAmount=purchase.getRound_off();
					System.out.println("The bp1 purchase id" +purchaseId +" "+ purchaseAmount);
					for(Payment payment:paymentagainstPurchase){
						if(payment.getSupplier_bill_no()!=null){
							payment_purchaseId=payment.getSupplier_bill_no().getPurchase_id();
						
							//payment_purchaseId=Long.parseLong(paymentPurchaseId);
						if(purchaseId.equals(payment_purchaseId)==true){
							if(payment.getTds_amount()!= null && payment.getTds_paid()!=null && payment.getTds_paid()==true)
								
							{
								purchaseAmount=purchaseAmount-(payment.getAmount()+payment.getTds_amount());
							}else{
								purchaseAmount=purchaseAmount-payment.getAmount();
							}
							System.out.println("The bp1 purchase after payment" +purchaseAmount);
						}
						}
					}
					for(DebitNote debitnote:debitNoteforPurchase){
						if(debitnote.getPurchase_bill_id()!=null){
							payment_purchaseId=debitnote.getPurchase_bill_id().getPurchase_id();
							if(purchaseId.equals(payment_purchaseId)==true){
								purchaseAmount=purchaseAmount-debitnote.getRound_off();
								System.out.println("The bp1 purchase after debit" + purchaseAmount);
							}
						}
					}
					if(purchaseAmount!=0){
						BillsReceivableForm entry = new BillsReceivableForm();
						  entry.setCreated_date(purchase.getSupplier_bill_date());
						  entry.setRound_off(purchaseAmount);
						  entry.setVoucher_no(purchase.getVoucher_no());
						  entry.setSupplier(sup);
						  billsPayable.add(entry);
					}
					}
					
					List<Payment> paymentlist = paymentService.getAllAdvancePaymentsAgainstSupplierForPeriod(sup.getSupplier_id(), form.getClientId(),toDate);
					
					for(Payment payment : paymentlist)
					{
						advancePaymentTotalAmountAgainstSupplier=0.0;
						if(payment.getTds_amount()!=null)
							advancePaymentTotalAmountAgainstSupplier += payment.getAmount()+payment.getTds_amount(); 
							else
								advancePaymentTotalAmountAgainstSupplier += payment.getAmount();
						
						if(advancePaymentTotalAmountAgainstSupplier>0)
						{
						  BillsReceivableForm entry = new BillsReceivableForm();
						  entry.setCreated_date(payment.getDate());
						  entry.setRound_off(-advancePaymentTotalAmountAgainstSupplier);
						  entry.setVoucher_no(payment.getVoucher_no());
						  entry.setParticulars("Advance Pending");
						  entry.setSupplier(sup);
						  billsPayable.add(entry);
						}
					}
					
					/*
					 * Collections.sort(billsPayable, new Comparator<BillsReceivableForm>() { public
					 * int compare(BillsReceivableForm form1, BillsReceivableForm form2) { LocalDate
					 * date1 = form1.getCreated_date(); LocalDate date2 = form2.getCreated_date();
					 * return date1.compareTo(date2); } });
					 */
				 model.addObject("billsPayable", billsPayable);
					
				}
				else
				{
					List<Suppliers> suppliersList= supplierService.findAllSuppliersOfCompany(form.getClientId());
					List<PurchaseEntry> billsPayable=purchaseEntryService.getPurchaseForLedgerReport1( toDate, form.getSupplierId(), form.getClientId());
					List<Payment> paymentagainstPurchase=paymentService.getPaymentForLedgerReport1( toDate, form.getSupplierId(), form.getClientId());
					List<DebitNote> debitNoteforPurchase =deibitNote.getDebitNoteForLedgerReport(fromDate, toDate, form.getSupplierId(), form.getClientId());
					
					System.out.println("paymentrecs" +paymentagainstPurchase.size());
				//billsPayable= purchaseEntryService.getBillsPayableForPurchase(form.getSupplierId(), fromDate, form.getToDate(),form.getClientId());
					String companyName;
					double amount = 0.0 ;
					double billAmt=0.0;
					for (Suppliers supplier : suppliersList) {
						
						for (PurchaseEntry br : billsPayable) {
							
							if (br.getSupplier().getSupplier_id().equals(supplier.getSupplier_id())) {
                            
								companyName = br.getSupplier().getCompany_name();
								
								billAmt=br.getRound_off();
								
								for(Payment payment:paymentagainstPurchase){
									System.out.println("1");
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
										payment_purchaseId=debitnote.getPurchase_bill_id().getPurchase_id();
										if(br.getPurchase_id().equals(payment_purchaseId)==true){
											
											billAmt=billAmt-debitnote.getRound_off();
											
										}
									}
									
								}
								//System.out.println("The purchase amount is "+billAmt);
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
									
									
									OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(fromDate,form.getClientId(), supplier.getSupplier_id()); 
									
									
									if(purchaseopening!=null && purchaseopening.getDebit_balance()!=null && purchaseopening.getCredit_balance()!=null)
									{
										openingBalance =(-purchaseopening.getDebit_balance())+purchaseopening.getCredit_balance();
									}
								
									
									List<PurchaseEntry> purchaseaginstopening =	purchaseEntryService.getAllOpeningBalanceAgainstPurchase(supplier.getSupplier_id(), form.getClientId());
									for(PurchaseEntry purchase:purchaseaginstopening) 
									{
										purchaseAmount=purchaseAmount+purchase.getRound_off();
									}
									openingBalance=openingBalance+purchaseAmount;
									
										
								
									
									List<Payment> paymentaginstopening=	paymentService.getAllOpeningBalanceAgainstPaymentForPeriod(supplier.getSupplier_id(), form.getClientId(),toDate);
									List<DebitNote> debitnoteaginstopening=	deibitNote.getAllOpeningBalanceAgainstDebitNoteForPeriod(supplier.getSupplier_id(), form.getClientId(),toDate);
									for(Payment payment:paymentaginstopening)
									{
										openingBalance=openingBalance-payment.getAmount();
										
									}
									for(DebitNote debitnote:debitnoteaginstopening)
									{
										openingBalance=openingBalance-debitnote.getRound_off();
									}
									List<Payment> paymentlist = paymentService.getAllAdvancePaymentsAgainstSupplierForPeriod(supplier.getSupplier_id(), form.getClientId(),toDate);
									
									for(Payment payment : paymentlist)
									{
										
										if(payment.getTds_amount()!=null)
											advancePaymentTotalAmountAgainstSupplier += payment.getAmount()+payment.getTds_amount(); 
											else
												advancePaymentTotalAmountAgainstSupplier += payment.getAmount();
									}
									
									
									billsPayable1.put(companyName, (billAmt+(-advancePaymentTotalAmountAgainstSupplier)+openingBalance));
									
								}
							}
						}
						
						if(form.getSupplierId().equals(new Long(0)))
						{
							
						if(!billsPayable1.containsKey(supplier.getCompany_name()))
						{
							
							double advancePaymentTotalAmountAgainstSupplier = 0.0 ;
							List<Payment> paymentlist = paymentService.getAllAdvancePaymentsAgainstSupplier(supplier.getSupplier_id(), form.getClientId());
							
							for(Payment payment : paymentlist)
							{
								if(payment.getTds_amount()!=null)
									advancePaymentTotalAmountAgainstSupplier += payment.getAmount()+payment.getTds_amount(); 
									else
										advancePaymentTotalAmountAgainstSupplier += payment.getAmount();
							}
							
							advancePaymentTotalAmountAgainstSupplier=-advancePaymentTotalAmountAgainstSupplier;
							
							OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(fromDate, form.getClientId(), supplier.getSupplier_id()); 
						
							if(purchaseopening!=null && purchaseopening.getDebit_balance()!=null && purchaseopening.getCredit_balance()!=null)
							{
								advancePaymentTotalAmountAgainstSupplier =advancePaymentTotalAmountAgainstSupplier+(-purchaseopening.getDebit_balance())+purchaseopening.getCredit_balance();
							}
							
							
							
							
							List<PurchaseEntry> purchaseaginstopening =	purchaseEntryService.getAllOpeningBalanceAgainstPurchase(supplier.getSupplier_id(), form.getClientId());
							for(PurchaseEntry purchase:purchaseaginstopening) 
							{
								purchaseAmount=purchaseAmount+purchase.getRound_off();
							}
							advancePaymentTotalAmountAgainstSupplier=advancePaymentTotalAmountAgainstSupplier+purchaseAmount;
							
							Double paymentAmount= 0.0;
							List<Payment> paymentaginstopening=	paymentService.getAllOpeningBalanceAgainstPayment(supplier.getSupplier_id(), form.getClientId());
							List<DebitNote> debitnoteaginstopening=	deibitNote.getAllOpeningBalanceAgainstDebitNote(supplier.getSupplier_id(), form.getClientId());
							for(Payment payment:paymentaginstopening)
							{
								paymentAmount=paymentAmount+payment.getAmount();
							}
							for(DebitNote debitnote:debitnoteaginstopening)
							{
								paymentAmount=paymentAmount+debitnote.getRound_off();
							}
							advancePaymentTotalAmountAgainstSupplier=advancePaymentTotalAmountAgainstSupplier-paymentAmount;
							
							if(advancePaymentTotalAmountAgainstSupplier!=0)
							{
								billsPayable1.put(supplier.getCompany_name(), (advancePaymentTotalAmountAgainstSupplier));
							}
							
						}
					
						}
						else
						{
							if(supplier.getSupplier_id().equals(form.getSupplierId()))
							{
							if(!billsPayable1.containsKey(supplier.getCompany_name()))
							{
								double advancePaymentTotalAmountAgainstSupplier = 0.0 ;
								List<Payment> paymentlist = paymentService.getAllAdvancePaymentsAgainstSupplier(supplier.getSupplier_id(), form.getClientId());
								
								for(Payment payment : paymentlist)
								{
									if(payment.getTds_amount()!=null)
										advancePaymentTotalAmountAgainstSupplier += payment.getAmount()+payment.getTds_amount(); 
										else
											advancePaymentTotalAmountAgainstSupplier += payment.getAmount();
								}
								
								advancePaymentTotalAmountAgainstSupplier=-advancePaymentTotalAmountAgainstSupplier;
								
								OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(fromDate, form.getClientId(), supplier.getSupplier_id()); 
							
								if(purchaseopening!=null && purchaseopening.getDebit_balance()!=null && purchaseopening.getCredit_balance()!=null)
								{
									advancePaymentTotalAmountAgainstSupplier =advancePaymentTotalAmountAgainstSupplier+(-purchaseopening.getDebit_balance())+purchaseopening.getCredit_balance();
								}
								
								
								
								
								List<PurchaseEntry> purchaseaginstopening =	purchaseEntryService.getAllOpeningBalanceAgainstPurchase(supplier.getSupplier_id(), form.getClientId());
								for(PurchaseEntry purchase:purchaseaginstopening) 
								{
									purchaseAmount=purchaseAmount+purchase.getRound_off();
								}
								advancePaymentTotalAmountAgainstSupplier=advancePaymentTotalAmountAgainstSupplier+purchaseAmount;
								
								Double paymentAmount= 0.0;
								List<Payment> paymentaginstopening=	paymentService.getAllOpeningBalanceAgainstPayment(supplier.getSupplier_id(), form.getClientId());
								List<DebitNote> debitnoteaginstopening=	deibitNote.getAllOpeningBalanceAgainstDebitNote(supplier.getSupplier_id(), form.getClientId());
								
								for(Payment payment:paymentaginstopening)
								{
									paymentAmount=paymentAmount+payment.getAmount();
								}
								for(DebitNote debitnote:debitnoteaginstopening)
								{
									paymentAmount=paymentAmount+debitnote.getRound_off();
								}
								advancePaymentTotalAmountAgainstSupplier=advancePaymentTotalAmountAgainstSupplier-paymentAmount;
								
								if(advancePaymentTotalAmountAgainstSupplier!=0)
								{
									billsPayable1.put(supplier.getCompany_name(), (advancePaymentTotalAmountAgainstSupplier));
								}
						
								
							}
							}
						}
					}
				}
				
				
			}
				
			else {
			
				
				
				billsPayable.clear();
			
				
				if(form.getSupplierId()>0)
				{
					
                    Suppliers sup = supplierService.findOneWithAll(form.getSupplierId());
					
					double advancePaymentTotalAmountAgainstSupplier = 0.0 ;
					Double openingBalance = 0.0;
					
					OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(fromDate,company_id, sup.getSupplier_id()); 
					
					if(purchaseopening!=null && purchaseopening.getDebit_balance()!=null && purchaseopening.getCredit_balance()!=null)
					{
						
						openingBalance =(-purchaseopening.getDebit_balance())+purchaseopening.getCredit_balance();
					}
						
					List<PurchaseEntry> purchaseaginstopening =	purchaseEntryService.getAllOpeningBalanceAgainstPurchase(sup.getSupplier_id(), company_id);
					/*for(PurchaseEntry purchase:purchaseaginstopening) 
					{
						purchaseAmount=purchaseAmount+purchase.getRound_off();
					}
					openingBalance=openingBalance+purchaseAmount;
				*/
					//List<PurchaseEntry> purchaseaginstopening =	purchaseEntryService.getAllOpeningBalanceAgainstPurchase(sup.getSupplier_id(), form.getClientId());
					List<PurchaseEntry> purchaseForperiod=purchaseEntryService.getPurchaseForLedgerReport1( toDate, form.getSupplierId(), company_id);
					List<Payment> paymentagainstPurchase=paymentService.getPaymentForLedgerReport1( toDate, form.getSupplierId(), company_id);
					List<DebitNote> debitNoteforPurchase =deibitNote.getDebitNoteForLedgerReport(fromDate, toDate, form.getSupplierId(), company_id);
					
					
					//List<Payment> paymentaginstopening1=	paymentService.getAllOpeningBalanceAgainstPayment(sup.getSupplier_id(), company_id);
					List<Payment> paymentaginstopening=	paymentService.getAllOpeningBalanceAgainstPaymentForPeriod(sup.getSupplier_id(), company_id,toDate);
					List<DebitNote> debitnoteaginstopening=	deibitNote.getAllOpeningBalanceAgainstDebitNoteForPeriod(sup.getSupplier_id(), company_id,toDate);
					
					for(Payment payment:paymentaginstopening)
					{
						
						openingBalance=openingBalance-payment.getAmount();
					}
					for(DebitNote debitnote:debitnoteaginstopening)
					{
						openingBalance=openingBalance-debitnote.getRound_off();
					}
					if(openingBalance!=0)
					{
					  BillsReceivableForm entry = new BillsReceivableForm();
					  entry.setCreated_date(fromDate.plusDays(1));
					  entry.setRound_off(openingBalance);
					  entry.setVoucher_no("Opening Balance");
					  entry.setSupplier(sup);
					  billsPayable.add(entry);
					}
					
					
					double purcAmt=0.0;
					double dN=0.0;
					 for(PurchaseEntry purchase:purchaseForperiod) 
					{
					purchaseId=	purchase.getPurchase_id();	
					
					purchaseAmount=purchase.getRound_off();
					purcAmt=purcAmt+purchaseAmount;
					for(Payment payment:paymentagainstPurchase){
						if(payment.getSupplier_bill_no()!=null){
							payment_purchaseId=payment.getSupplier_bill_no().getPurchase_id();
							
							//payment_purchaseId=Long.parseLong(paymentPurchaseId);
						if(purchaseId.equals(payment_purchaseId)==true){
							if(payment.getTds_amount()!= null && payment.getTds_paid()!=null && payment.getTds_paid()==true)
								
							{
								purchaseAmount=purchaseAmount-(payment.getAmount()+payment.getTds_amount());
							}else{
								purchaseAmount=purchaseAmount-payment.getAmount();
							}
							
							
						}
						}
					}
					for(DebitNote debitnote:debitNoteforPurchase){
						if(debitnote.getPurchase_bill_id()!=null){
							payment_purchaseId=debitnote.getPurchase_bill_id().getPurchase_id();
							if(purchaseId.equals(payment_purchaseId)==true){
								
								purchaseAmount=purchaseAmount-debitnote.getRound_off();
							}
						}
					}
					if(purchaseAmount!=0){
						
						BillsReceivableForm entry = new BillsReceivableForm();
						  entry.setCreated_date(purchase.getSupplier_bill_date());
						  entry.setRound_off(purchaseAmount);
						  entry.setVoucher_no(purchase.getVoucher_no());
						  entry.setSupplier(sup);
						  billsPayable.add(entry);
					}
					}
					List<Payment> paymentlist = paymentService.getAllAdvancePaymentsAgainstSupplierForPeriod(sup.getSupplier_id(), company_id,toDate);
					
					for(Payment payment : paymentlist)
					{
						advancePaymentTotalAmountAgainstSupplier=0.0;
						if(payment.getTds_amount()!=null)
							advancePaymentTotalAmountAgainstSupplier += payment.getAmount()+payment.getTds_amount(); 
							else
								advancePaymentTotalAmountAgainstSupplier += payment.getAmount();
						
						if(advancePaymentTotalAmountAgainstSupplier>0)
						{
						  BillsReceivableForm entry = new BillsReceivableForm();
						  entry.setCreated_date(payment.getDate());
						  entry.setRound_off(-advancePaymentTotalAmountAgainstSupplier);
						  entry.setVoucher_no(payment.getVoucher_no());
						  entry.setParticulars("Advance Pending");
						  entry.setSupplier(sup);
						  billsPayable.add(entry);
						}
					}
					
					/*
					 * Collections.sort(billsPayable, new Comparator<BillsReceivableForm>() { public
					 * int compare(BillsReceivableForm form1, BillsReceivableForm form2) { LocalDate
					 * date1 = form1.getCreated_date(); LocalDate date2 = form2.getCreated_date();
					 * return date1.compareTo(date2); } });
					 */
				 model.addObject("billsPayable", billsPayable);
				}
				else
				{
					List<Suppliers> suppliersList= supplierService.findAllSuppliersOfCompany(company_id);
					//billsPayable= purchaseEntryService.getBillsPayableForPurchase(form.getSupplierId(), fromDate, form.getToDate(),company_id);
					List<PurchaseEntry> billsPayable=purchaseEntryService.getPurchaseForLedgerReport1( toDate, form.getSupplierId(), company_id);
					List<Payment> paymentagainstPurchase=paymentService.getPaymentForLedgerReport1( toDate, form.getSupplierId(), company_id);
					List<DebitNote> debitNoteforPurchase =deibitNote.getDebitNoteForLedgerReport(fromDate, toDate, form.getSupplierId(), company_id);
					;
					Double billAmt=0.0;
					// new changes for cloup data
					String companyName;
					double amount = 0.0 ;
					
					for (Suppliers supplier : suppliersList) {
						
						for (PurchaseEntry br : billsPayable) {
                        
							if (br.getSupplier().getSupplier_id().equals(supplier.getSupplier_id())) {
								 
								companyName = br.getSupplier().getCompany_name();
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
										payment_purchaseId=debitnote.getPurchase_bill_id().getPurchase_id();
										if(br.getPurchase_id().equals(payment_purchaseId)==true){
										
											billAmt=billAmt-debitnote.getRound_off();
										}
									}
								}
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
									
									
									OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(fromDate,company_id, supplier.getSupplier_id()); 
									
									
									if(purchaseopening!=null && purchaseopening.getDebit_balance()!=null && purchaseopening.getCredit_balance()!=null)
									{
										openingBalance =(-purchaseopening.getDebit_balance())+purchaseopening.getCredit_balance();
									}
								
									
										
									
								
									List<Payment> paymentaginstopening=	paymentService.getAllOpeningBalanceAgainstPaymentForPeriod(supplier.getSupplier_id(), company_id,toDate);
									List<DebitNote> debitnoteaginstopening=	deibitNote.getAllOpeningBalanceAgainstDebitNoteForPeriod(supplier.getSupplier_id(), company_id,toDate);

									for(Payment payment:paymentaginstopening)
									{
										openingBalance=openingBalance-payment.getAmount();
									}
									for(DebitNote debitnote:debitnoteaginstopening)
									{
										openingBalance=openingBalance-debitnote.getRound_off();
									}
									List<Payment> paymentlist = paymentService.getAllAdvancePaymentsAgainstSupplier(supplier.getSupplier_id(), company_id);
									
									for(Payment payment : paymentlist)
									{
									
										if(payment.getTds_amount()!=null)
											
											advancePaymentTotalAmountAgainstSupplier += payment.getAmount()+payment.getTds_amount(); 
											else
												advancePaymentTotalAmountAgainstSupplier += payment.getAmount();
									}
									
									
									billsPayable1.put(companyName, (billAmt+(-advancePaymentTotalAmountAgainstSupplier)+openingBalance));
									
								}
							}
						}
						if(form.getSupplierId().equals(new Long(0)))
						{
						
						if(!billsPayable1.containsKey(supplier.getCompany_name()))
						{
							
							
							double advancePaymentTotalAmountAgainstSupplier = 0.0 ;
							double openingBalance =0.0;
							List<Payment> paymentlist = paymentService.getAllAdvancePaymentsAgainstSupplier(supplier.getSupplier_id(), company_id);
							
							for(Payment payment : paymentlist)
							{
								if(payment.getTds_amount()!=null)
									advancePaymentTotalAmountAgainstSupplier += payment.getAmount()+payment.getTds_amount(); 
									else
										advancePaymentTotalAmountAgainstSupplier += payment.getAmount();
							}
							
							advancePaymentTotalAmountAgainstSupplier =-advancePaymentTotalAmountAgainstSupplier;
							
							OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(fromDate, company_id, supplier.getSupplier_id()); 
						
							if(purchaseopening!=null && purchaseopening.getDebit_balance()!=null && purchaseopening.getCredit_balance()!=null)
							{
								openingBalance =(-purchaseopening.getDebit_balance())+purchaseopening.getCredit_balance();
							}
							
							
							
							
							List<PurchaseEntry> purchaseaginstopening =	purchaseEntryService.getAllOpeningBalanceAgainstPurchase(supplier.getSupplier_id(), company_id);
							for(PurchaseEntry purchase:purchaseaginstopening) 
							{
								purchaseAmount=purchaseAmount+purchase.getRound_off();
							}
							advancePaymentTotalAmountAgainstSupplier=advancePaymentTotalAmountAgainstSupplier+purchaseAmount;
							
							
							List<Payment> paymentaginstopening=	paymentService.getAllOpeningBalanceAgainstPayment(supplier.getSupplier_id(), company_id);
							List<DebitNote> debitnoteaginstopening=	deibitNote.getAllOpeningBalanceAgainstDebitNote(supplier.getSupplier_id(), company_id);
							for(Payment payment:paymentaginstopening)
							{
								openingBalance=openingBalance-payment.getAmount();
							}
							for(DebitNote debitnote:debitnoteaginstopening)
							{
								openingBalance=openingBalance-debitnote.getRound_off();
							}
							advancePaymentTotalAmountAgainstSupplier=advancePaymentTotalAmountAgainstSupplier+openingBalance;
							
							if(advancePaymentTotalAmountAgainstSupplier!=0)
							{
								billsPayable1.put(supplier.getCompany_name(), (advancePaymentTotalAmountAgainstSupplier));
							}
							
						}
					
						}
						else
						{
							
							if(supplier.getSupplier_id().equals(form.getSupplierId()))
							{
								
							if(!billsPayable1.containsKey(supplier.getCompany_name()))
							{
								
								double advancePaymentTotalAmountAgainstSupplier = 0.0 ;
								double openingBalance=0.0;
								List<Payment> paymentlist = paymentService.getAllAdvancePaymentsAgainstSupplier(supplier.getSupplier_id(), company_id);
								
								for(Payment payment : paymentlist)
								{
									if(payment.getTds_amount()!=null)
										advancePaymentTotalAmountAgainstSupplier += payment.getAmount()+payment.getTds_amount(); 
										else
											advancePaymentTotalAmountAgainstSupplier += payment.getAmount();
								}
								
								advancePaymentTotalAmountAgainstSupplier =-advancePaymentTotalAmountAgainstSupplier;
								
								OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(fromDate, company_id, supplier.getSupplier_id()); 
							
								if(purchaseopening!=null && purchaseopening.getDebit_balance()!=null && purchaseopening.getCredit_balance()!=null)
								{
									openingBalance =(-purchaseopening.getDebit_balance())+purchaseopening.getCredit_balance();;
								}
								
								
								
							
								List<PurchaseEntry> purchaseaginstopening =	purchaseEntryService.getAllOpeningBalanceAgainstPurchase(supplier.getSupplier_id(), company_id);
								for(PurchaseEntry purchase:purchaseaginstopening) 
								{
									
									purchaseAmount=purchaseAmount+purchase.getRound_off();
								}
								advancePaymentTotalAmountAgainstSupplier=advancePaymentTotalAmountAgainstSupplier+purchaseAmount;
								
								
								List<Payment> paymentaginstopening=	paymentService.getAllOpeningBalanceAgainstPayment(supplier.getSupplier_id(), company_id);
								List<DebitNote> debitnoteaginstopening=	deibitNote.getAllOpeningBalanceAgainstDebitNote(supplier.getSupplier_id(), company_id);
								for(Payment payment:paymentaginstopening)
								{
									
									openingBalance=openingBalance-payment.getAmount();
								}
								for(DebitNote debitnote:debitnoteaginstopening)
								{
									openingBalance=openingBalance-debitnote.getRound_off();
								}
								advancePaymentTotalAmountAgainstSupplier=advancePaymentTotalAmountAgainstSupplier+openingBalance;
								
								if(advancePaymentTotalAmountAgainstSupplier!=0)
								{
									billsPayable1.put(supplier.getCompany_name(), (advancePaymentTotalAmountAgainstSupplier));
								}
						
								
							}
							}
						}
					}
				}
				
		}
			
			model.addObject("option", form.getSupplierId());
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date",toDate);	
			model.addObject("billsReceivable1", billsPayable1);
			model.setViewName("/report/billsPayableReportList");
		}
		return model;
	}
	

	@RequestMapping(value = "pdfBillsPayableReport", method = RequestMethod.GET)
    public ModelAndView downloadExcel() 
	{
		BillsPayableReportForm form = new BillsPayableReportForm();
		form.setBillsPayable(billsPayable);
		form.setCompany(company);
		form.setFromDate(fromDate);
    	form.setToDate(toDate);
    	form.setOption(option);
	    return new ModelAndView("BillsPayableReport", "form", form);
    
    }
		
	
}
