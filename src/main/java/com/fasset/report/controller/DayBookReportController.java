package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.DayBookValidator;
import com.fasset.entities.Company;
import com.fasset.entities.Contra;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.User;
import com.fasset.form.DayBookReportForm;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IContraService;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.IDebitNoteService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;

@Controller
@SessionAttributes("user")
public class DayBookReportController extends MyAbstractController {
	
	@Autowired
	private ICompanyService companyService ;
	
	@Autowired
	private IPaymentService paymentService;
	
	@Autowired
	private IReceiptService receiptService;
	
	
	@Autowired
	private IPurchaseEntryService purchaseEntryService;
	
	@Autowired
	private ISalesEntryService SalesEntryService;
	
	@Autowired	
	private IContraService contraservice;
	
	@Autowired
	private ICreditNoteService creditNoteService;
	
	@Autowired
	private IDebitNoteService debitNoteService;
	
	@Autowired
	private DayBookValidator validator;

	private List<Company> companyList = new ArrayList<Company>();
	private List<Payment> paymentList = new ArrayList<Payment>();
	private List<Receipt> receiptList = new ArrayList<Receipt>();
	private List<PurchaseEntry> purchaseEntryList = new ArrayList<PurchaseEntry>();
	private List<SalesEntry> salesEntryList= new ArrayList<SalesEntry>();
	private List<Contra> contraList = new ArrayList<Contra>();
	private List<CreditNote> creditNoteList = new ArrayList<CreditNote>();
	private List<DebitNote> debitNoteList = new ArrayList<DebitNote>();
	private List<DayBookReportForm> dayBookList = new ArrayList<DayBookReportForm>();
	
	private Company company;
	
	@RequestMapping(value = "dayBookReport", method = RequestMethod.GET)
	public ModelAndView dayBookReport(HttpServletRequest request, HttpServletResponse response) {

		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		Long role = user.getRole().getRole_id();

		ModelAndView model = new ModelAndView();
		DayBookReportForm form = new DayBookReportForm();
		
		if (role.equals(ROLE_SUPERUSER))
		{
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/dayBookReportForKpo");
		}
		else if(role.equals(ROLE_EXECUTIVE) || role.equals(ROLE_MANAGER)){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());			
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/dayBookReportForKpo");
		} 
		else {
			model.addObject("form", form);
			model.setViewName("/report/dayBookReport");
		}
		return model;
	}
	
	@RequestMapping(value = "dayBookReport", method = RequestMethod.POST)
	public ModelAndView dayBookReport(@ModelAttribute("form")DayBookReportForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
	
		HttpSession session = request.getSession(true);
	
		long company_id=(long)session.getAttribute("company_id");
		validator.validate(form, result);
		if(result.hasErrors()){
			model.addObject("form", form);
			model.setViewName("/report/dayBookReport");
		}
		else
		{
			paymentList.clear();
			receiptList.clear();
			purchaseEntryList.clear();
			salesEntryList.clear();
			contraList.clear();
			creditNoteList.clear();
			debitNoteList.clear();
			dayBookList.clear();
			
			paymentList =paymentService.getDayBookReport(form.getFromDate(),form.getToDate(),company_id);
			receiptList =receiptService.getDayBookReport(form.getFromDate(),form.getToDate(),company_id);
			purchaseEntryList=purchaseEntryService.getDayBookReport(form.getFromDate(),form.getToDate(),company_id);
			salesEntryList=SalesEntryService.getDayBookReport(form.getFromDate(),form.getToDate(),company_id);
			contraList=contraservice.getDayBookReport(form.getFromDate(),form.getToDate(),company_id);
			creditNoteList=creditNoteService.getDayBookReport(form.getFromDate(),form.getToDate(),company_id);
			debitNoteList= debitNoteService.getDayBookReport(form.getFromDate(),form.getToDate(),company_id);
			try {
				company = companyService.getCompanyWithCompanyStautarType(company_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(Payment payment :paymentList) 
			{
				if(payment.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(payment.getDate()!=null)
				{
				dayBookform.setDate(payment.getDate());
				}
				if(payment.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(payment.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Payment");
				if(payment.getSupplier()!=null)
				{
					dayBookform.setParticulars(payment.getSupplier().getCompany_name());
				}
				if(payment.getSubLedger()!=null)
				{
					dayBookform.setParticulars(payment.getSubLedger().getSubledger_name());
				    dayBookform.setSubLedger(payment.getSubLedger());
				}
				Double Tds = (double)0;
				Double amount = (double)0;
				if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(true)))
				{
					if(payment.getTds_amount()!=null)
					{
						Tds=Tds+payment.getTds_amount();
					}
					if(payment.getAmount()!=null)
					{
						amount=amount+payment.getAmount();
					}
					if(dayBookform.getSubLedger()==null)
				   {
					dayBookform.setCredit(amount);
				   }
					else if(dayBookform.getSubLedger()!=null)
					{
						dayBookform.setDebit(amount);
					}
					
				}
				if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(false)))
				{
					if(payment.getAmount()!=null)
					{
						amount=amount+payment.getAmount();
					}
						if(dayBookform.getSubLedger()==null)
					   {
						dayBookform.setCredit(amount);
					   }
						else if(dayBookform.getSubLedger()!=null)
						{
							dayBookform.setDebit(amount);
						}
				}
				dayBookform.setLocal_time(payment.getLocal_time());
				dayBookform.setType(1);
				dayBookList.add(dayBookform);
				}
				
			}
			
			
			for(Receipt receipt :receiptList) 
			{
				if(receipt.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(receipt.getDate()!=null)
				{
				dayBookform.setDate(receipt.getDate());
				}
				if(receipt.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(receipt.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Receipt");
				if(receipt.getCustomer()!=null)
				{
					dayBookform.setParticulars(receipt.getCustomer().getFirm_name());
				}
				if(receipt.getSubLedger()!=null)
				{
					dayBookform.setParticulars(receipt.getSubLedger().getSubledger_name());
					dayBookform.setSubLedger(receipt.getSubLedger());
				}
				Double Tds = (double)0;
				Double amount = (double)0;
				if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(true)))
				{
					if(receipt.getTds_amount()!=null)
					{
						Tds=Tds+receipt.getTds_amount();
					}
					if(receipt.getAmount()!=null)
					{
						amount=amount+receipt.getAmount();
					}
					if(receipt.getSubLedger()==null)
					{
						dayBookform.setDebit(amount);
					}
					else if(receipt.getSubLedger()!=null)
					{
						dayBookform.setCredit(amount);
					}
					
					
					
				}
				if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(false)))
				{
					if(receipt.getAmount()!=null)
					{
						amount=amount+receipt.getAmount();
					}
					if(receipt.getSubLedger()==null)
					{
						dayBookform.setDebit(amount);
					}
					else if(receipt.getSubLedger()!=null)
					{
						dayBookform.setCredit(amount);
					}
				}
				dayBookform.setLocal_time(receipt.getLocal_time());
				dayBookform.setType(2);
				dayBookList.add(dayBookform);
				}
				
			}
			for(PurchaseEntry purchase :purchaseEntryList) 
			{
				if(purchase.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(purchase.getSupplier_bill_date()!=null)
				{
				dayBookform.setDate(purchase.getSupplier_bill_date());
				}
				if(purchase.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(purchase.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Purchase");
				if(purchase.getSupplier()!=null)
				{
					dayBookform.setParticulars(purchase.getSupplier().getCompany_name());
				}
				Double amount = (double)0;
				
					if(purchase.getTransaction_value()!=null)
					{
						amount=amount+purchase.getTransaction_value();
					}
					dayBookform.setDebit(amount);
				dayBookform.setLocal_time(purchase.getLocal_time());
				dayBookform.setType(3);
				dayBookList.add(dayBookform);
				}
				
			}
			
			for(SalesEntry sales :salesEntryList) 
			{
				if(sales.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(sales.getCreated_date()!=null)
				{
				dayBookform.setDate(sales.getCreated_date());
				}
				if(sales.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(sales.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Sales");
				if(sales.getSale_type()!=null && sales.getSale_type().equals(new Integer(1)))
				{
					dayBookform.setParticulars("Cash Sales");
				}
				else if(sales.getSale_type()!=null && sales.getSale_type().equals(new Integer(2)))
				{
					if(sales.getBank()!=null)
					{
					dayBookform.setParticulars("Card Sales"+"-"+" "+sales.getBank().getBank_name()+"-"+" "+sales.getBank().getAccount_no());
					}
					else
					{
						dayBookform.setParticulars("Card Sales");
					}
				}
				else if(sales.getCustomer()!=null)
				{
					dayBookform.setParticulars(sales.getCustomer().getFirm_name());
				}
				Double amount = (double)0;
				
					if(sales.getTransaction_value()!=null)
					{
						amount=amount+sales.getTransaction_value();
					}
					dayBookform.setCredit(amount);
				dayBookform.setLocal_time(sales.getLocal_time());
				dayBookform.setType(4);
				dayBookList.add(dayBookform);
				}
				
			}
			for(CreditNote creditNote :creditNoteList) 
			{
				if(creditNote.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(creditNote.getDate()!=null)
				{
				dayBookform.setDate(creditNote.getDate());
				}
				if(creditNote.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(creditNote.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Credit Note");
				if(creditNote.getCustomer()!=null)
				{
					dayBookform.setParticulars(creditNote.getCustomer().getFirm_name());
				}
				
				Double amount = (double)0;
				
					if(creditNote.getTds_amount()!=null)
					{
						amount=amount+creditNote.getRound_off()+creditNote.getTds_amount();
					}
					else
					{
						amount=amount+creditNote.getRound_off();
					}
					dayBookform.setCredit(amount);
			
				dayBookform.setLocal_time(creditNote.getLocal_time());
				dayBookform.setType(5);
				dayBookList.add(dayBookform);
				}
				
			}
			
			for(DebitNote debitNote :debitNoteList) 
			{
				if(debitNote.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(debitNote.getDate()!=null)
				{
				dayBookform.setDate(debitNote.getDate());
				}
				if(debitNote.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(debitNote.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Debit Note");
				if(debitNote.getSupplier()!=null)
				{
					dayBookform.setParticulars(debitNote.getSupplier().getCompany_name());
				}
				
				
				Double amount = (double)0;
			
				if(debitNote.getTds_amount()!=null)
					{
						amount=amount+debitNote.getRound_off()+debitNote.getTds_amount();
					}
				else
				{
					amount=amount+debitNote.getRound_off();
				}
					dayBookform.setDebit(amount);
				
				dayBookform.setLocal_time(debitNote.getLocal_time());
				dayBookform.setType(6);
				dayBookList.add(dayBookform);
				}
				
			}
			
			for(Contra contra :contraList) 
			{
				if(contra.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(contra.getDate()!=null)
				{
				dayBookform.setDate(contra.getDate());
				}
				if(contra.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(contra.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Contra");
				if(contra.getType().equals(new Integer(1)))
				{
					dayBookform.setParticulars("Deposit");
					dayBookform.setDeposit_to("Deposited to "+contra.getDeposite_to().getBank_name());
				}
				if(contra.getType().equals(new Integer(2)))
				{
					dayBookform.setParticulars("Withdraw");
					dayBookform.setWithdraw_from("Withdraw from "+contra.getWithdraw_from().getBank_name());
				}
				if(contra.getType().equals(new Integer(3)))
				{
					dayBookform.setParticulars("Transfer");
					dayBookform.setDeposit_to("Deposited to "+contra.getDeposite_to().getBank_name());
					dayBookform.setWithdraw_from("Withdraw from "+contra.getWithdraw_from().getBank_name());
				}
				Double amount = (double)0;
			
				if(contra.getType().equals(new Integer(1)))
				{
				if(contra.getAmount()!=null)
					{
						amount=amount+contra.getAmount();
					}
				dayBookform.setCredit(amount);
			
				}
				if(contra.getType().equals(new Integer(2)))
				{
				if(contra.getAmount()!=null)
					{
						amount=amount+contra.getAmount();
					}
				dayBookform.setDebit(amount);
				}
				if(contra.getType().equals(new Integer(3)))
				{
				if(contra.getAmount()!=null)
					{
						amount=amount+contra.getAmount();
					}
					dayBookform.setCredit(amount);
					dayBookform.setDebit(amount);
				}
				
				dayBookform.setLocal_time(contra.getLocal_time());
				dayBookform.setType(7);
				dayBookList.add(dayBookform);
				

			
			}
			}
			
			Collections.sort(dayBookList, new Comparator<DayBookReportForm>() {
		        public int compare(DayBookReportForm o1, DayBookReportForm o2) {

		        	LocalDate Date1 = o1.getDate();
		        	LocalDate Date2= o2.getDate();
		            int sComp = Date2.compareTo(Date1);

		            if (sComp != 0) {
		               return sComp;
		            } 

		            LocalTime local_time1 = o1.getLocal_time();
		            LocalTime local_time2= o2.getLocal_time();
		            return local_time2.compareTo(local_time1);
		    }});
			
			model.addObject("company", company);
			model.addObject("dayBookList", dayBookList);
			model.setViewName("/report/dayBookReportList");
		}
		return model;
	}
	
	@RequestMapping(value = "dayBookReportForKpo", method = RequestMethod.POST)
	public ModelAndView dayBookReportForKpo(@ModelAttribute("form")DayBookReportForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		
		validator.validate(form, result);
		if(result.hasErrors()){
			companyList = companyService.getApprovedCompanies();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/dayBookReportForKpo");
		}
		else
		{
			paymentList.clear();
			receiptList.clear();
			purchaseEntryList.clear();
			salesEntryList.clear();
			contraList.clear();
			creditNoteList.clear();
			debitNoteList.clear();
			dayBookList.clear();
			
			paymentList =paymentService.getDayBookReport(form.getFromDate(),form.getToDate(),form.getCompanyId());
			receiptList =receiptService.getDayBookReport(form.getFromDate(),form.getToDate(),form.getCompanyId());
			purchaseEntryList=purchaseEntryService.getDayBookReport(form.getFromDate(),form.getToDate(),form.getCompanyId());
			salesEntryList=SalesEntryService.getDayBookReport(form.getFromDate(),form.getToDate(),form.getCompanyId());
			contraList=contraservice.getDayBookReport(form.getFromDate(),form.getToDate(),form.getCompanyId());
			creditNoteList=creditNoteService.getDayBookReport(form.getFromDate(),form.getToDate(),form.getCompanyId());
			debitNoteList= debitNoteService.getDayBookReport(form.getFromDate(),form.getToDate(),form.getCompanyId());
			try {
				company = companyService.getCompanyWithCompanyStautarType(form.getCompanyId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(Payment payment :paymentList) 
			{
				if(payment.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(payment.getDate()!=null)
				{
				dayBookform.setDate(payment.getDate());
				}
				if(payment.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(payment.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Payment");
				if(payment.getSupplier()!=null)
				{
					dayBookform.setParticulars(payment.getSupplier().getCompany_name());
				}
				if(payment.getSubLedger()!=null)
				{
					dayBookform.setParticulars(payment.getSubLedger().getSubledger_name());
                    dayBookform.setSubLedger(payment.getSubLedger());
				}
				Double Tds = (double)0;
				Double amount = (double)0;
				if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(true)))
				{
					if(payment.getTds_amount()!=null)
					{
						Tds=Tds+payment.getTds_amount();
					}
					if(payment.getAmount()!=null)
					{
						amount=amount+payment.getAmount();
					}
						if(dayBookform.getSubLedger()==null)
					   {
						dayBookform.setCredit(amount);
					   }
						else if(dayBookform.getSubLedger()!=null)
						{
							dayBookform.setDebit(amount);
						}
					
				}
				if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(false)))
				{
					if(payment.getAmount()!=null)
					{
						amount=amount+payment.getAmount();
					}
					if(dayBookform.getSubLedger()==null)
					   {
						dayBookform.setCredit(amount);
					   }
						else if(dayBookform.getSubLedger()!=null)
						{
							dayBookform.setDebit(amount);
						}
				}
				dayBookform.setLocal_time(payment.getLocal_time());
				dayBookform.setType(1);
				dayBookList.add(dayBookform);
				}
				
			}
			
			
			for(Receipt receipt :receiptList) 
			{
				if(receipt.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(receipt.getDate()!=null)
				{
				dayBookform.setDate(receipt.getDate());
				}
				if(receipt.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(receipt.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Receipt");
				if(receipt.getCustomer()!=null)
				{
					dayBookform.setParticulars(receipt.getCustomer().getFirm_name());
				}
				if(receipt.getSubLedger()!=null)
				{
					dayBookform.setParticulars(receipt.getSubLedger().getSubledger_name());
					dayBookform.setSubLedger(receipt.getSubLedger());
				}
				Double Tds = (double)0;
				Double amount = (double)0;
				if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(true)))
				{
					if(receipt.getTds_amount()!=null)
					{
						Tds=Tds+receipt.getTds_amount();
					}
					if(receipt.getAmount()!=null)
					{
						amount=amount+receipt.getAmount();
					}
					if(receipt.getSubLedger()==null)
					{
						dayBookform.setDebit(amount);
					}
					else if(receipt.getSubLedger()!=null)
					{
						dayBookform.setCredit(amount);
					}
					
				}
				if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(false)))
				{
					if(receipt.getAmount()!=null)
					{
						amount=amount+receipt.getAmount();
					}
					if(receipt.getSubLedger()==null)
					{
						dayBookform.setDebit(amount);
					}
					else if(receipt.getSubLedger()!=null)
					{
						dayBookform.setCredit(amount);
					}
				}
				dayBookform.setLocal_time(receipt.getLocal_time());
				dayBookform.setType(2);
				dayBookList.add(dayBookform);
				}
				
			}
			for(PurchaseEntry purchase :purchaseEntryList) 
			{
				if(purchase.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(purchase.getSupplier_bill_date()!=null)
				{
				dayBookform.setDate(purchase.getSupplier_bill_date());
				}
				if(purchase.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(purchase.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Purchase");
				if(purchase.getSupplier()!=null)
				{
					dayBookform.setParticulars(purchase.getSupplier().getCompany_name());
				}
				Double amount = (double)0;
				
					if(purchase.getTransaction_value()!=null)
					{
						amount=amount+purchase.getTransaction_value();
					}
					dayBookform.setDebit(amount);
				dayBookform.setLocal_time(purchase.getLocal_time());
				dayBookform.setType(3);
				dayBookList.add(dayBookform);
				}
				
			}
			
			for(SalesEntry sales :salesEntryList) 
			{
				if(sales.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(sales.getCreated_date()!=null)
				{
				dayBookform.setDate(sales.getCreated_date());
				}
				if(sales.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(sales.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Sales");
				if(sales.getSale_type()!=null && sales.getSale_type().equals(new Integer(1)))
				{
					dayBookform.setParticulars("Cash Sales");
				}
				else if(sales.getSale_type()!=null && sales.getSale_type().equals(new Integer(2)))
				{
					if(sales.getBank()!=null)
					{
					dayBookform.setParticulars("Card Sales"+"-"+" "+sales.getBank().getBank_name()+"-"+" "+sales.getBank().getAccount_no());
					}
					else
					{
						dayBookform.setParticulars("Card Sales");
					}
				}
				else if(sales.getCustomer()!=null)
				{
					dayBookform.setParticulars(sales.getCustomer().getFirm_name());
				}
				Double amount = (double)0;
				
					if(sales.getTransaction_value()!=null)
					{
						amount=amount+sales.getTransaction_value();
					}
					dayBookform.setCredit(amount);
				dayBookform.setLocal_time(sales.getLocal_time());
				dayBookform.setType(4);
				dayBookList.add(dayBookform);
				}
				
			}
			for(CreditNote creditNote :creditNoteList) 
			{
				if(creditNote.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(creditNote.getDate()!=null)
				{
				dayBookform.setDate(creditNote.getDate());
				}
				if(creditNote.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(creditNote.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Credit Note");
				if(creditNote.getCustomer()!=null)
				{
					dayBookform.setParticulars(creditNote.getCustomer().getFirm_name());
				}
				
				Double amount = (double)0;
				
				if(creditNote.getTds_amount()!=null)
				{
					amount=amount+creditNote.getRound_off()+creditNote.getTds_amount();
				}
				else
				{
					amount=amount+creditNote.getRound_off();
				}
				dayBookform.setCredit(amount);
			
				dayBookform.setLocal_time(creditNote.getLocal_time());
				dayBookform.setType(5);
				dayBookList.add(dayBookform);
				}
				
			}
			
			for(DebitNote debitNote :debitNoteList) 
			{
				if(debitNote.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(debitNote.getDate()!=null)
				{
				dayBookform.setDate(debitNote.getDate());
				}
				if(debitNote.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(debitNote.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Debit Note");
				if(debitNote.getSupplier()!=null)
				{
					dayBookform.setParticulars(debitNote.getSupplier().getCompany_name());
				}
				
				
				Double amount = (double)0;
				
				if(debitNote.getTds_amount()!=null)
					{
						amount=amount+debitNote.getRound_off()+debitNote.getTds_amount();
					}
				else
				{
					amount=amount+debitNote.getRound_off();
				}
					dayBookform.setDebit(amount);
				
				dayBookform.setLocal_time(debitNote.getLocal_time());
				dayBookform.setType(6);
				dayBookList.add(dayBookform);
				}
				
			}
			
			for(Contra contra :contraList) 
			{
				if(contra.getLocal_time()!=null)
				{
				DayBookReportForm dayBookform = new DayBookReportForm();
				if(contra.getDate()!=null)
				{
				dayBookform.setDate(contra.getDate());
				}
				if(contra.getVoucher_no()!=null)
				{
					dayBookform.setVoucher_Number(contra.getVoucher_no());
				}
				dayBookform.setVoucher_Type("Contra");
				if(contra.getType().equals(new Integer(1)))
				{
					dayBookform.setParticulars("Deposit");
					dayBookform.setDeposit_to("Deposited to "+contra.getDeposite_to().getBank_name());
				}
				if(contra.getType().equals(new Integer(2)))
				{
					dayBookform.setParticulars("Withdraw");
					dayBookform.setWithdraw_from("Withdraw from "+contra.getWithdraw_from().getBank_name());
				}
				if(contra.getType().equals(new Integer(3)))
				{
					dayBookform.setParticulars("Transfer");
					dayBookform.setDeposit_to("Deposited to "+contra.getDeposite_to().getBank_name());
					dayBookform.setWithdraw_from("Withdraw from "+contra.getWithdraw_from().getBank_name());
				}
				Double amount = (double)0;
			
				if(contra.getType().equals(new Integer(1)))
				{
				if(contra.getAmount()!=null)
					{
						amount=amount+contra.getAmount();
					}
				dayBookform.setCredit(amount);
				}
				if(contra.getType().equals(new Integer(2)))
				{
				if(contra.getAmount()!=null)
					{
						amount=amount+contra.getAmount();
					}
				dayBookform.setDebit(amount);
					
				}
				if(contra.getType().equals(new Integer(3)))
				{
				if(contra.getAmount()!=null)
					{
						amount=amount+contra.getAmount();
					}
					dayBookform.setCredit(amount);
					dayBookform.setDebit(amount);
				}
				
				dayBookform.setLocal_time(contra.getLocal_time());
				dayBookform.setType(7);
				dayBookList.add(dayBookform);
				

				
			
			}
			}
			
			Collections.sort(dayBookList, new Comparator<DayBookReportForm>() {
		        public int compare(DayBookReportForm o1, DayBookReportForm o2) {

		        	LocalDate Date1 = o1.getDate();
		        	LocalDate Date2= o2.getDate();
		            int sComp = Date2.compareTo(Date1);

		            if (sComp != 0) {
		               return sComp;
		            } 

		            LocalTime local_time1 = o1.getLocal_time();
		            LocalTime local_time2= o2.getLocal_time();
		            return local_time2.compareTo(local_time1);
		    }});
			
			model.addObject("company", company);
			model.addObject("dayBookList", dayBookList);
			model.setViewName("/report/dayBookReportList");
		}
		return model;
	}
	
	
	
}
