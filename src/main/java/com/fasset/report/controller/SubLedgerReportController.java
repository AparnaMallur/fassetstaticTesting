package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.LedgerReportValidator;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.Contra;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Customer;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Ledger;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.entities.YearEndJvSubledgerDetails;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.LedgerReportForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.SupplierCustomerLedgerForm;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IContraService;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IDebitNoteService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IManualJournalVoucherService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.fasset.service.interfaces.IYearEndingService;

@Controller
@SessionAttributes("user")
public class SubLedgerReportController extends MyAbstractController{

	@Autowired
	private ILedgerService ledgerService;
	
	@Autowired
	private LedgerReportValidator ledgerReportValidator;
	
	@Autowired
	private ICompanyService companyService ;
	
	@Autowired
	private ICustomerService customerService ;
	
	@Autowired
	private IBankService bankService;
	
	@Autowired
	private ISuplliersService suplliersService;

	@Autowired
	private ISubLedgerService subLedgerService ;
	
	@Autowired
	private ISalesEntryService SalesEntryService;
	
	@Autowired	
	private IContraService contraservice;
	
	@Autowired	
	private IReceiptService receiptService;
	
	@Autowired	
	private IPaymentService paymentService;
	
	@Autowired
	private IOpeningBalancesService openingBalancesService;
	
	@Autowired
	private ICreditNoteService creditNoteService;
	
	@Autowired
	private IPurchaseEntryService purchaseEntryService;
	@Autowired
	private IDebitNoteService debitNoteservice;
	@Autowired
	private ISalesEntryService salesEntryService ;
	@Autowired
	private IManualJournalVoucherService journalService;
	@Autowired
	private IYearEndingService yearEndJournal;
	
	private Long customerId = null;
	private Long supplierId = null;
	private Long bankId = null;
	private Long option = null;
	private Long ledgerId =null;
	private Long subledgerId =null;
	private LocalDate fromDate=null;
	private LocalDate toDate=null;
	private Company company;
	
	private List<Ledger> ledgerList = new ArrayList<Ledger>();
	private List<Company> companyList = new ArrayList<Company>();
	
    private CopyOnWriteArrayList<OpeningBalances> subledgerOPBalanceList= new CopyOnWriteArrayList<OpeningBalances>();
    private List<Contra> contraList  = new ArrayList<Contra>();
    private List<Receipt> receiptList  = new ArrayList<Receipt>();
    private List<Payment> paymenttList  = new ArrayList<Payment>();
    private List<PurchaseEntry> purchaseEntryList = new ArrayList<PurchaseEntry>();
	private List<SalesEntry> salesEntryList= new ArrayList<SalesEntry>();
	private List<CreditNote> creditNoteList = new ArrayList<CreditNote>();
	private List<DebitNote> debitNoteList = new ArrayList<DebitNote>();
	
    private List<OpeningBalancesForm>customerOpenBalanceList = new ArrayList<OpeningBalancesForm>();
    private List<OpeningBalancesForm> supplierOpenBalanceList =new ArrayList<OpeningBalancesForm>();
    private List<OpeningBalancesForm> bankOpenBalanceList =new ArrayList<OpeningBalancesForm>();
    private List<OpeningBalancesForm>  subledgerOpenBalanceList = new ArrayList<OpeningBalancesForm>();
    private  List<List<OpeningBalances>> allsubList = new ArrayList<List<OpeningBalances>>();
    private List<SupplierCustomerLedgerForm> dayBookList = new ArrayList<SupplierCustomerLedgerForm>();
	    
	    @RequestMapping(value = "ledgerReport", method = RequestMethod.GET)
		public ModelAndView ledgerReport(HttpServletRequest request, HttpServletResponse response) {
	    	System.out.println("The Leger List for saleget");
			HttpSession session = request.getSession(true);
			long role=(long)session.getAttribute("role");
			long company_id=(long)session.getAttribute("company_id");	
			User user = (User)session.getAttribute("user");	
			ModelAndView model = new ModelAndView();
			LedgerReportForm ledgerReportForm = new LedgerReportForm();
			
			if (role == ROLE_SUPERUSER){
				companyList = companyService.getAllCompaniesOnly();
				
				model.addObject("ledgerReportForm", ledgerReportForm);
				model.addObject("companyList", companyList);
				model.setViewName("/report/ledgerReportForKpo");
			}
			else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
				companyList = companyService.getAllCompaniesExe(user.getUser_id());	
				model.addObject("ledgerReportForm", ledgerReportForm);
				model.addObject("companyList", companyList);
				model.setViewName("/report/ledgerReportForKpo");
			} 
			else
			{
				List<Bank> bankList = bankService.findAllBanksOfCompany(company_id);
				List<Customer> customerList = customerService.findAllCustomersOfCompany(company_id);
				List<Suppliers> suppliersList = suplliersService.findAllSuppliersOfCompany(company_id);
				ledgerList = ledgerService.findAllLedgersOfCompany(company_id);
				
				model.addObject("ledgerReportForm", ledgerReportForm);
				model.addObject("ledgerList", ledgerList);
				model.addObject("bankList", bankList);
				model.addObject("customerList", customerList);
				model.addObject("suppliersList", suppliersList);
				model.setViewName("/report/ledgerReport");
			}
			return model;
		}
	    
	    @RequestMapping(value = "ledgerReport", method = RequestMethod.POST)
		public ModelAndView ledgerReport(HttpServletRequest request, HttpServletResponse response,
				@ModelAttribute("ledgerReportForm") LedgerReportForm ledgerReportForm, BindingResult result) {
			ModelAndView model = new ModelAndView();

			HttpSession session = request.getSession(true);
			long company_id=(long)session.getAttribute("company_id");
			
			ledgerReportValidator.validate(ledgerReportForm, result);
			System.out.println("The Leger List for sale");
			if(result.hasErrors()){
				
				List<Bank> bankList = bankService.findAllBanksOfCompany(company_id);
				List<Customer> customerList = customerService.findAllCustomersOfCompany(company_id);
				List<Suppliers> suppliersList = suplliersService.findAllSuppliersOfCompany(company_id);
				
				
				model.addObject("bankList", bankList);
				model.addObject("customerList", customerList);
				model.addObject("suppliersList", suppliersList);
				model.addObject("ledgerList", ledgerList);
				model.setViewName("/report/ledgerReport");
			}
			else {
				
				ledgerReportForm.setCompanyId(company_id);
				if(ledgerReportForm.getCustomerId()!=null && ledgerReportForm.getCustomerId()!=0)
				{
					System.out.println("The customer id not null");
					
					customerId = ledgerReportForm.getCustomerId();
					option = ledgerReportForm.getReportAgainst();
					fromDate=ledgerReportForm.getFromDate();
					toDate=ledgerReportForm.getToDate();
					try {
						company = companyService.getCompanyWithCompanyStautarType(company_id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ledgerReportForm.setLedgerId((long)0);
					ledgerReportForm.setSubledgerId((long)0);
					customerOpenBalanceList.clear();
					receiptList.clear();
					salesEntryList.clear();
					creditNoteList.clear();
					dayBookList.clear();
					
					/**-------------Calculation for opening balance before start date for all customers-----------------*/
					for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomerBeforeFromDate(fromDate,company_id))
					{
						if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
						{
							Customer cus = null;
							try {
								cus = customerService.getById((long)bal[0]);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if(cus!=null && cus.getCustomer_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) 
							{
							OpeningBalancesForm form = new OpeningBalancesForm();
							form.setCustomer_id((long)bal[0]);
							form.setCredit_balance((double)bal[2]);
							form.setDebit_balance((double)bal[1]);
							customerOpenBalanceList.add(form);
							}
						}
					}
					
					salesEntryList = SalesEntryService.getSalesForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCustomerId(), company_id);
					receiptList = receiptService.getReceiptForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCustomerId(), company_id);
					creditNoteList = creditNoteService.getCreditNoteForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCustomerId(), company_id);
					
					for(SalesEntry sales :salesEntryList) 
					{
						if(sales.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(sales.getCreated_date()!=null)
							{
								form.setDate(sales.getCreated_date());
							}
							if(sales.getVoucher_no()!=null)
							{
								form.setVoucher_Number(sales.getVoucher_no());
							}
							if(sales.getSubledger()!=null)
							{
								form.setParticulars(sales.getSubledger().getSubledger_name());
							}
							if(sales.getCustomer()!=null)
							{
								form.setCustomer(sales.getCustomer());
							}
							form.setVoucher_Type("Sales");
							Double amount = (double)0;
							
							if(sales.getRound_off()!=null)
							{
								amount=amount+sales.getRound_off();
							}
							form.setDebit(amount);
							
							form.setLocal_time(sales.getLocal_time());
							form.setType(4);
						dayBookList.add(form);
						}
					}
					for(Receipt receipt :receiptList) 
					{
						if(receipt.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(receipt.getDate()!=null)
							{
								form.setDate(receipt.getDate());
							}
							if(receipt.getVoucher_no()!=null)
							{
								form.setVoucher_Number(receipt.getVoucher_no());
							}
							if(receipt.getPayment_type()!=null)
							{
								if(receipt.getPayment_type().equals(1))
								{
								form.setParticulars("cash");
								}
								else
								{
									if(receipt.getBank()!=null)
									{
										form.setParticulars(receipt.getBank().getBank_name()+"--"+receipt.getBank().getAccount_no());
									}
								}
							}
							if(receipt.getCustomer()!=null)
							{
								form.setCustomer(receipt.getCustomer());
								
							}
							form.setVoucher_Type("Receipt");
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
								form.setCredit(amount+Tds);
							
							}
							if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(false)))
							{
								if(receipt.getAmount()!=null)
								{
									amount=amount+receipt.getAmount();
								}
								form.setCredit(amount);
							}
						
							form.setLocal_time(receipt.getLocal_time());
							form.setType(2);
							dayBookList.add(form);
						}
					}
					for(CreditNote creditNote :creditNoteList) 
					{
						if(creditNote.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(creditNote.getDate()!=null)
							{
								form.setDate(creditNote.getDate());
							}
							if(creditNote.getVoucher_no()!=null)
							{
								form.setVoucher_Number(creditNote.getVoucher_no());
							}
							if(creditNote.getSubledger()!=null)
							{
								form.setParticulars(creditNote.getSubledger().getSubledger_name());
							
							}
							if(creditNote.getCustomer()!=null)
							{
								form.setCustomer(creditNote.getCustomer());
							}
							form.setVoucher_Type("Credit Note");
							Double amount = (double)0;
							
							if(creditNote.getRound_off()!=null)
							{
								amount=amount+creditNote.getRound_off();
							}
							form.setCredit(amount);
					
							form.setLocal_time(creditNote.getLocal_time());
							form.setType(5);
						dayBookList.add(form);
						}
					}
					Collections.sort(dayBookList, new Comparator<SupplierCustomerLedgerForm>() {
				        public int compare(SupplierCustomerLedgerForm o1, SupplierCustomerLedgerForm o2) {

				        	LocalDate Date1 = o1.getDate();
				        	LocalDate Date2= o2.getDate();
				            int sComp = Date1.compareTo(Date2);

				            if (sComp != 0) {
				               return sComp;
				            } 

				            LocalTime local_time1 = o1.getLocal_time();
				            LocalTime local_time2= o2.getLocal_time();
				            return local_time1.compareTo(local_time2);
				    }});
					/**------------Customerlist if client choose for all-----------------*/
					if(customerId.equals((long)-1))
					{
						List<Customer> customerlist =customerService.findAllCustomersOnlyOFCompany(company_id);
						model.addObject("customerlist",customerlist);
					}
					else
					{
						try {
							model.addObject("customerName",customerService.getById(customerId).getFirm_name());
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
					model.addObject("company",company);
					model.addObject("customerId",customerId);
					model.addObject("option",option);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate",toDate);
					model.addObject("dayBookList", dayBookList);
					model.addObject("customerOpenBalanceList",customerOpenBalanceList);
					model.setViewName("/report/customer-ledgerReportList");
				}
				else if(ledgerReportForm.getSupplierId()!=null && ledgerReportForm.getSupplierId()!=0)
				{
					System.out.println("The SupplierId id not null");
					supplierId = ledgerReportForm.getSupplierId();
					option = ledgerReportForm.getReportAgainst();
					fromDate=ledgerReportForm.getFromDate();	
					toDate=ledgerReportForm.getToDate();
					try {
						company = companyService.getCompanyWithCompanyStautarType(company_id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ledgerReportForm.setLedgerId((long)0);
					ledgerReportForm.setSubledgerId((long)0);
					
					
					supplierOpenBalanceList.clear();
					purchaseEntryList.clear();
					paymenttList.clear();
					debitNoteList.clear();
					dayBookList.clear();
					
					/**-------------Calculation for opening balance before start date for all suppliers-----------------*/
					for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplierBeforeFromDate(fromDate, company_id))
					{
						if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
						{
							Suppliers sup = null;
							try {
								sup = suplliersService.getById((long)bal[0]);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(sup!=null && sup.getSupplier_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
							{
							OpeningBalancesForm form = new OpeningBalancesForm();
						    form.setSupplier_id((long)bal[0]);
							form.setCredit_balance((double)bal[2]);
							form.setDebit_balance((double)bal[1]);
							supplierOpenBalanceList.add(form);
							}
						}
					}
					purchaseEntryList = purchaseEntryService.getPurchaseForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getSupplierId(), company_id);
					paymenttList = paymentService.getPaymentForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getSupplierId(), company_id);
					debitNoteList = debitNoteservice.getDebitNoteForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getSupplierId(), company_id);
					
					for(PurchaseEntry purchase :purchaseEntryList) 
					{
						if(purchase.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(purchase.getSupplier_bill_date()!=null)
							{
								form.setDate(purchase.getSupplier_bill_date());
							}
							if(purchase.getVoucher_no()!=null)
							{
								form.setVoucher_Number(purchase.getVoucher_no());
							}
							if(purchase.getSubledger()!=null)
							{
								form.setParticulars(purchase.getSubledger().getSubledger_name());
							}
							if(purchase.getSupplier()!=null)
							{
								form.setSupplier(purchase.getSupplier());
							
								
							}
							form.setVoucher_Type("Purchase");
							Double amount = (double)0;
							
							if(purchase.getRound_off()!=null)
							{
								amount=amount+purchase.getRound_off();
							}
							System.out.println("The ledger purchase amount" +amount);
							form.setCredit(amount);
							form.setLocal_time(purchase.getLocal_time());
							form.setType(3);
						dayBookList.add(form);
						}
					}
					
					for(Payment payment :paymenttList) 
					{
						if(payment.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(payment.getDate()!=null)
							{
								form.setDate(payment.getDate());
							}
							if(payment.getVoucher_no()!=null)
							{
								form.setVoucher_Number(payment.getVoucher_no());
							}
							if(payment.getPayment_type()!=null)
							{
								if(payment.getPayment_type().equals(1))
								{
								form.setParticulars("cash");
								}
								else
								{
									if(payment.getBank()!=null)
									{
										form.setParticulars(payment.getBank().getBank_name()+"--"+payment.getBank().getAccount_no());
									}
								}
							}
							if(payment.getSupplier()!=null)
							{
								form.setSupplier(payment.getSupplier());
								
								
							}
							form.setVoucher_Type("Payment");
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
								form.setDebit(amount+Tds);
								
							
							}
							if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(false)))
							{
								if(payment.getAmount()!=null)
								{
									amount=amount+payment.getAmount();
								}
								form.setDebit(amount);
							}
							System.out.println("The ledger payment amount" +amount);
						
							form.setLocal_time(payment.getLocal_time());
							form.setType(1);
							dayBookList.add(form);
						}
					}
					for(DebitNote debitNote :debitNoteList) 
					{
						if(debitNote.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(debitNote.getDate()!=null)
							{
								form.setDate(debitNote.getDate());
							}
							if(debitNote.getVoucher_no()!=null)
							{
								form.setVoucher_Number(debitNote.getVoucher_no());
							}
							if(debitNote.getSubledger()!=null)
							{
								form.setParticulars(debitNote.getSubledger().getSubledger_name());
							
							}
							if(debitNote.getSupplier()!=null)
							{
								form.setSupplier(debitNote.getSupplier());
								
							}
							form.setVoucher_Type("Debit Note");
							Double amount = (double)0;
							
							if(debitNote.getRound_off()!=null)
							{
								amount=amount+debitNote.getRound_off();
							}
							form.setDebit(amount);
							System.out.println("The ledger debit amount" +amount);
					
							form.setLocal_time(debitNote.getLocal_time());
							form.setType(6);
						    dayBookList.add(form);
						}
					}
					
					Collections.sort(dayBookList, new Comparator<SupplierCustomerLedgerForm>() {
				        public int compare(SupplierCustomerLedgerForm o1, SupplierCustomerLedgerForm o2) {

				        	LocalDate Date1 = o1.getDate();
				        	LocalDate Date2= o2.getDate();
				            int sComp = Date1.compareTo(Date2);

				            if (sComp != 0) {
				               return sComp;
				            } 

				            LocalTime local_time1 = o1.getLocal_time();
				            LocalTime local_time2= o2.getLocal_time();
				            return local_time1.compareTo(local_time2);
				    }});
					/**------------supplierlist if client choose for all-----------------*/
					if(supplierId.equals((long)-2))
					{
					List<Suppliers> supplierlist =suplliersService.findAllSuppliersOnlyOfCompany(company_id);
					model.addObject("supplierlist",supplierlist);
					}
					else
					{
						try {
							model.addObject("supplierName",suplliersService.getById(supplierId).getCompany_name());
						} catch (MyWebException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					model.addObject("company",company);
					model.addObject("supplierId",supplierId);
					model.addObject("option",option);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate",toDate);
					model.addObject("dayBookList", dayBookList);
					model.addObject("supplierOpenBalanceList",supplierOpenBalanceList);
					model.setViewName("/report/supplier-ledgerReportList");
				}
				else if(ledgerReportForm.getBankId()!=null && ledgerReportForm.getBankId()!=0)
				{
					System.out.println("The bank id not null");
					bankId = ledgerReportForm.getBankId();
					option = ledgerReportForm.getReportAgainst();
					fromDate=ledgerReportForm.getFromDate();	
					toDate=ledgerReportForm.getToDate();
					try {
						company = companyService.getCompanyWithCompanyStautarType(company_id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ledgerReportForm.setLedgerId((long)0);
					ledgerReportForm.setSubledgerId((long)0);
					
					receiptList.clear();
					paymenttList.clear();
					contraList.clear();
					salesEntryList.clear();
					bankOpenBalanceList.clear();
					dayBookList.clear();
					
					for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(ledgerReportForm.getFromDate(), company_id))
					{
						if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
						{
							Bank bank = null;
							try {
								bank = bankService.getById((long)bal[0]);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
							{
							OpeningBalancesForm form = new OpeningBalancesForm();
		                    form.setBank_id((long)bal[0]);
							form.setCredit_balance((double)bal[2]);
							form.setDebit_balance((double)bal[1]);
							bankOpenBalanceList.add(form);
							}
						}
					}
					    
					
					paymenttList  = paymentService.getPaymentListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), company_id,ledgerReportForm.getBankId());
					receiptList  = receiptService.getReceiptListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), company_id,ledgerReportForm.getBankId());
					contraList = contraservice.getContraListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), company_id,ledgerReportForm.getBankId());
					salesEntryList = salesEntryService.getCardSalesForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), company_id,ledgerReportForm.getBankId());
						
					for(SalesEntry sales :salesEntryList) 
					{
						if(sales.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(sales.getCreated_date()!=null)
							{
								form.setDate(sales.getCreated_date());
							}
							if(sales.getVoucher_no()!=null)
							{
								form.setVoucher_Number(sales.getVoucher_no());
							}
							if(sales.getSubledger()!=null)
							{
								form.setParticulars(sales.getSubledger().getSubledger_name());
							}
							if(sales.getBank()!=null)
							{
								form.setSuppCustName("Card Sales -"+sales.getBank().getBank_name()/*+"-"+sales.getBank().getAccount_no()*/);
								form.setBank(sales.getBank());
							}
							form.setVoucher_Type("Sales");
							Double amount = (double)0;
							
							if(sales.getRound_off()!=null)
							{
								amount=amount+sales.getRound_off();
							}
							form.setDebit(amount);
							form.setLocal_time(sales.getLocal_time());
							form.setType(4);
						dayBookList.add(form);
						}
					}
					
					for(Contra contra :contraList) 
					{
						if(contra.getLocal_time()!=null)
						{
							if((contra.getDeposite_to()!=null)||(contra.getWithdraw_from()!=null))
							{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(contra.getDate()!=null)
							{
								form.setDate(contra.getDate());
							}
							if(contra.getVoucher_no()!=null)
							{
								form.setVoucher_Number(contra.getVoucher_no());
							}
							if(contra.getType().equals(2))
							{
								form.setParticulars(contra.getWithdraw_from().getBank_name());
								form.setBank(contra.getWithdraw_from());
							}
							else if(contra.getType().equals(1))
							{
								form.setParticulars(contra.getDeposite_to().getBank_name());
								form.setBank(contra.getDeposite_to());
							}
							else
							{
								form.setWithdraw_from(contra.getWithdraw_from());
								form.setDeposite_to(contra.getDeposite_to());
							}
								
							if(contra.getType().equals(2))
							{
								form.setVoucher_Type("Contra-Withdraw");
								form.setContratType(contra.getType());
							}
							else if(contra.getType().equals(1))
							{
								form.setVoucher_Type("Contra-Deposit");
								form.setContratType(contra.getType());
							}
							else
							{
								form.setVoucher_Type("Contra-Transfer");
								form.setContratType(contra.getType());
							}
						
							Double amount = (double)0;
							
							if(contra.getType().equals(2))
							{
								if(contra.getAmount()!=null)
								{
									amount=amount+contra.getAmount();
								}
								form.setCredit(amount);
								
							}
							else if(contra.getType().equals(1))
							{
								if(contra.getAmount()!=null)
								{
									amount=amount+contra.getAmount();
								}
								form.setDebit(amount);
								
							}
							else
							{
								
								if(contra.getAmount()!=null)
								{
									amount=amount+contra.getAmount();
								}
								form.setTransferAmount(amount);
								
							}
					
							form.setLocal_time(contra.getLocal_time());
							form.setType(7);
						dayBookList.add(form);
						   }
						}
					}
					
					for(Receipt receipt :receiptList) 
					{
						if(receipt.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(receipt.getDate()!=null)
							{
								form.setDate(receipt.getDate());
							}
							if(receipt.getVoucher_no()!=null)
							{
								form.setVoucher_Number(receipt.getVoucher_no());
							}
							if(receipt.getBank()!=null)
							{
								form.setParticulars(receipt.getBank().getBank_name()/*+"-"+receipt.getBank().getAccount_no()*/);
								form.setBank(receipt.getBank());
							}
							if(receipt.getCustomer()!=null)
							{
								form.setSuppCustName(receipt.getCustomer().getFirm_name());
								
							}
							else if(receipt.getSubLedger()!=null) 
							{
								form.setSuppCustName(receipt.getSubLedger().getSubledger_name());
							}
							form.setVoucher_Type("Receipt");
							Double Tds = (double)0;
							Double Tax = (double)0;
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
								
								if(receipt.getCgst()!=null)
								{
									Tax=Tax+receipt.getCgst();
								}
								if(receipt.getSgst()!=null)
								{
									Tax=Tax+receipt.getSgst();
								}
								if(receipt.getIgst()!=null)
								{
									Tax=Tax+receipt.getIgst();
								}
								if(receipt.getState_compansation_tax()!=null)
								{
									Tax=Tax+receipt.getState_compansation_tax();
								}
								
								form.setDebit(amount-Tax);
							
							}
							if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(false)))
							{
								if(receipt.getAmount()!=null)
								{
									amount=amount+receipt.getAmount();
								}
								if(receipt.getCgst()!=null)
								{
									Tax=Tax+receipt.getCgst();
								}
								if(receipt.getSgst()!=null)
								{
									Tax=Tax+receipt.getSgst();
								}
								if(receipt.getIgst()!=null)
								{
									Tax=Tax+receipt.getIgst();
								}
								if(receipt.getState_compansation_tax()!=null)
								{
									Tax=Tax+receipt.getState_compansation_tax();
								}
								
								form.setDebit(amount-Tax);
							}
						
							form.setLocal_time(receipt.getLocal_time());
							form.setType(2);
							dayBookList.add(form);
						}
					}
					
					for(Payment payment :paymenttList) 
					{
						if(payment.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(payment.getDate()!=null)
							{
								form.setDate(payment.getDate());
							}
							if(payment.getVoucher_no()!=null)
							{
								form.setVoucher_Number(payment.getVoucher_no());
							}
							if(payment.getBank()!=null)
							{
								form.setParticulars(payment.getBank().getBank_name()/*+"--"+payment.getBank().getAccount_no()*/);
								form.setBank(payment.getBank());
							}
							if(payment.getSupplier()!=null)
							{
								form.setSuppCustName(payment.getSupplier().getCompany_name());
								
							}
							else if(payment.getSubLedger()!=null) 
							{
								form.setSuppCustName(payment.getSubLedger().getSubledger_name());
							}
							form.setVoucher_Type("Payment");
							Double Tds = (double)0;
							Double Tax = (double)0;
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
								if(payment.getCGST_head()!=null)
								{
									Tax=Tax+payment.getCGST_head();
								}
								if(payment.getSGST_head()!=null)
								{
									Tax=Tax+payment.getSGST_head();
								}
								if(payment.getIGST_head()!=null)
								{
									Tax=Tax+payment.getIGST_head();
								}
								if(payment.getSCT_head()!=null)
								{
									Tax=Tax+payment.getSCT_head();
								}
								form.setCredit(amount-Tax);
								
								
							
							}
							if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(false)))
							{
								if(payment.getAmount()!=null)
								{
									amount=amount+payment.getAmount();
								}
								if(payment.getCGST_head()!=null)
								{
									Tax=Tax+payment.getCGST_head();
								}
								if(payment.getSGST_head()!=null)
								{
									Tax=Tax+payment.getSGST_head();
								}
								if(payment.getIGST_head()!=null)
								{
									Tax=Tax+payment.getIGST_head();
								}
								if(payment.getSCT_head()!=null)
								{
									Tax=Tax+payment.getSCT_head();
								}
								form.setCredit(amount-Tax);
							}
						
							form.setLocal_time(payment.getLocal_time());
							form.setType(1);
							dayBookList.add(form);
						}
					}
					
					
					Collections.sort(dayBookList, new Comparator<SupplierCustomerLedgerForm>() {
				        public int compare(SupplierCustomerLedgerForm o1, SupplierCustomerLedgerForm o2) {

				        	LocalDate Date1 = o1.getDate();
				        	LocalDate Date2= o2.getDate();
				            int sComp = Date1.compareTo(Date2);

				            if (sComp != 0) {
				               return sComp;
				            } 

				            LocalTime local_time1 = o1.getLocal_time();
				            LocalTime local_time2= o2.getLocal_time();
				            return local_time1.compareTo(local_time2);
				    }});
					
					if(bankId.equals((long)-4))
					{
					List<Bank> banklist = bankService.findAllBanksOfCompany(company_id);
					model.addObject("banklist",banklist);
					}
					else
					{
						try {
							model.addObject("bankName",bankService.getById(bankId).getBank_name());
						} catch (MyWebException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
					model.addObject("company",company);
					model.addObject("bankId",bankId);
					model.addObject("option",option);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate",toDate);
					model.addObject("dayBookList", dayBookList);
					model.addObject("bankOpenBalanceList",bankOpenBalanceList);
					model.setViewName("/report/bank-ledgerReportList");
				}
				
				else{
					System.out.println("ledgerReportForm.getReportAgainst()");
					option = ledgerReportForm.getReportAgainst();
					ledgerId = ledgerReportForm.getLedgerId();
					fromDate=ledgerReportForm.getFromDate();	
					toDate=ledgerReportForm.getToDate();
					try {
						company = companyService.getCompanyWithCompanyStautarType(company_id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					subledgerOPBalanceList.clear();
					subledgerOpenBalanceList.clear();
					ledgerList.clear();
					model.addObject("company",company);
					subledgerId = ledgerReportForm.getSubledgerId();
					
					for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(),company_id,false))
					{
						if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
						{
						
							SubLedger sub = null;
							try {
								sub = subLedgerService.getById((long)bal[0]);
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
							{
								OpeningBalancesForm form = new OpeningBalancesForm();
								form.setSubledgerName(sub.getSubledger_name());
								try {
									form.setSubledger(sub);
								} catch (Exception e) {
									e.printStackTrace();
								}
							form.setSub_id((long)bal[0]);
							form.setCredit_balance((double)bal[2]);
							form.setDebit_balance((double)bal[1]);
							subledgerOpenBalanceList.add(form);
							} 
						
						}
					}
					
					if(ledgerId!=0)
					{
					if(subledgerId.equals((long)0))
					{  
						allsubList.clear();
						Ledger ledger = ledgerService.findOneWithAll(ledgerId);
						Set<SubLedger> subLedgerList = ledger.getSubLedger();
						model.addObject("subLedgerList",subLedgerList);
						
						for(SubLedger sub : subLedgerList)
						{
							subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), sub.getSubledger_Id(), company_id);
							allsubList.add(subledgerOPBalanceList);
							
						}
						model.addObject("allsubList",allsubList);
					}
					else
					{
						subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getSubledgerId(),company_id);
						model.addObject("subledgerOPBalanceList",subledgerOPBalanceList);
						try {
							model.addObject("subName",subLedgerService.getById(subledgerId).getSubledger_name());
						} catch (MyWebException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					}
					else
					{
						allsubList.clear();
						ledgerList = ledgerService.findAllLedgersOfCompany(company_id);
						Set<SubLedger> subLedgerList = new HashSet<>();
						for(Ledger ledger : ledgerList)
						{
							for(SubLedger sub : ledger.getSubLedger())
							{
								subLedgerList.add(sub);
							}
						}
						model.addObject("subLedgerList",subLedgerList);
						for(SubLedger sub : subLedgerList)
						{
							subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), sub.getSubledger_Id(), company_id);
							allsubList.add(subledgerOPBalanceList);
							
						}
						model.addObject("allsubList",allsubList);
						
					}
					model.addObject("subledgerOpenBalanceList",subledgerOpenBalanceList);
					model.addObject("option",option);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate",toDate);
					model.addObject("subledgerId",subledgerId);
					model.setViewName("/report/subledger-ledgerReportList");
					}
			}
			return model;
		}
	 
	    @RequestMapping(value = "ledgerReportForKpo", method = RequestMethod.POST)
		public ModelAndView ledgerReportForKpo(HttpServletRequest request, HttpServletResponse response,
				@ModelAttribute("ledgerReportForm") LedgerReportForm ledgerReportForm, BindingResult result) {
			ModelAndView model = new ModelAndView();
			
			ledgerReportValidator.validate(ledgerReportForm, result);
			if(result.hasErrors()){
				model.addObject("companyList", companyList);
				model.setViewName("/report/ledgerReportForKpo");
			}
            else {
				
				ledgerReportForm.setCompanyId(ledgerReportForm.getCompanyId());
				if(ledgerReportForm.getCustomerId()!=null && ledgerReportForm.getCustomerId()!=0)
				{
					System.out.println("customerledger rpo");
					customerId = ledgerReportForm.getCustomerId();
					option = ledgerReportForm.getReportAgainst();
					fromDate=ledgerReportForm.getFromDate();
					toDate=ledgerReportForm.getToDate();
					try {
						company = companyService.getCompanyWithCompanyStautarType(ledgerReportForm.getCompanyId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ledgerReportForm.setLedgerId((long)0);
					ledgerReportForm.setSubledgerId((long)0);
					customerOpenBalanceList.clear();
					receiptList.clear();
					salesEntryList.clear();
					creditNoteList.clear();
					dayBookList.clear();
					
					/**-------------Calculation for opening balance before start date for all customers-----------------*/
					for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomerBeforeFromDate(fromDate,ledgerReportForm.getCompanyId()))
					{
						if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
						{
							Customer cus = null;
							try {
								cus = customerService.getById((long)bal[0]);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if(cus!=null && cus.getCustomer_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) 
							{
							OpeningBalancesForm form = new OpeningBalancesForm();
							form.setCustomer_id((long)bal[0]);
							form.setCredit_balance((double)bal[2]);
							form.setDebit_balance((double)bal[1]);
							customerOpenBalanceList.add(form);
							}
						}
					}
					
					salesEntryList = SalesEntryService.getSalesForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCustomerId(), ledgerReportForm.getCompanyId());
					receiptList = receiptService.getReceiptForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCustomerId(), ledgerReportForm.getCompanyId());
					creditNoteList = creditNoteService.getCreditNoteForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCustomerId(), ledgerReportForm.getCompanyId());
					
					for(SalesEntry sales :salesEntryList) 
					{
						if(sales.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(sales.getCreated_date()!=null)
							{
								form.setDate(sales.getCreated_date());
							}
							if(sales.getVoucher_no()!=null)
							{
								form.setVoucher_Number(sales.getVoucher_no());
							}
							if(sales.getSubledger()!=null)
							{
								form.setParticulars(sales.getSubledger().getSubledger_name());
							}
							if(sales.getCustomer()!=null)
							{
								form.setCustomer(sales.getCustomer());
							}
							form.setVoucher_Type("Sales");
							Double amount = (double)0;
							
							if(sales.getRound_off()!=null)
							{
								amount=amount+sales.getRound_off();
							}
							form.setDebit(amount);
							
							form.setLocal_time(sales.getLocal_time());
							form.setType(4);
						dayBookList.add(form);
						}
					}
					for(Receipt receipt :receiptList) 
					{
						if(receipt.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(receipt.getDate()!=null)
							{
								form.setDate(receipt.getDate());
							}
							if(receipt.getVoucher_no()!=null)
							{
								form.setVoucher_Number(receipt.getVoucher_no());
							}
							if(receipt.getPayment_type()!=null)
							{
								if(receipt.getPayment_type().equals(1))
								{
								form.setParticulars("cash");
								}
								else
								{
									if(receipt.getBank()!=null)
									{
										form.setParticulars(receipt.getBank().getBank_name()+"--"+receipt.getBank().getAccount_no());
									}
								}
							}
							if(receipt.getCustomer()!=null)
							{
								form.setCustomer(receipt.getCustomer());
								
							}
							form.setVoucher_Type("Receipt");
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
								form.setCredit(amount+Tds);
							
							}
							if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(false)))
							{
								if(receipt.getAmount()!=null)
								{
									amount=amount+receipt.getAmount();
								}
								form.setCredit(amount);
							}
						
							form.setLocal_time(receipt.getLocal_time());
							form.setType(2);
							dayBookList.add(form);
						}
					}
					for(CreditNote creditNote :creditNoteList) 
					{
						if(creditNote.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(creditNote.getDate()!=null)
							{
								form.setDate(creditNote.getDate());
							}
							if(creditNote.getVoucher_no()!=null)
							{
								form.setVoucher_Number(creditNote.getVoucher_no());
							}
							if(creditNote.getSubledger()!=null)
							{
								form.setParticulars(creditNote.getSubledger().getSubledger_name());
							
							}
							if(creditNote.getCustomer()!=null)
							{
								form.setCustomer(creditNote.getCustomer());
							}
							form.setVoucher_Type("Credit Note");
							Double amount = (double)0;
							
							if(creditNote.getRound_off()!=null)
							{
								amount=amount+creditNote.getRound_off();
							}
							form.setCredit(amount);
					
							form.setLocal_time(creditNote.getLocal_time());
							form.setType(5);
						dayBookList.add(form);
						}
					}
					Collections.sort(dayBookList, new Comparator<SupplierCustomerLedgerForm>() {
				        public int compare(SupplierCustomerLedgerForm o1, SupplierCustomerLedgerForm o2) {

				        	LocalDate Date1 = o1.getDate();
				        	LocalDate Date2= o2.getDate();
				            int sComp = Date1.compareTo(Date2);

				            if (sComp != 0) {
				               return sComp;
				            } 

				            LocalTime local_time1 = o1.getLocal_time();
				            LocalTime local_time2= o2.getLocal_time();
				            return local_time1.compareTo(local_time2);
				    }});
					/**------------Customerlist if client choose for all-----------------*/
					if(customerId.equals((long)-1))
					{
						List<Customer> customerlist =customerService.findAllCustomersOnlyOFCompany(ledgerReportForm.getCompanyId());
						model.addObject("customerlist",customerlist);
					}
					else
					{
						try {
							model.addObject("customerName",customerService.getById(customerId).getFirm_name());
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
					model.addObject("company",company);
					model.addObject("customerId",customerId);
					model.addObject("option",option);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate",toDate);
					model.addObject("dayBookList", dayBookList);
					model.addObject("customerOpenBalanceList",customerOpenBalanceList);
					model.setViewName("/report/customer-ledgerReportList");
				}
				else if(ledgerReportForm.getSupplierId()!=null && ledgerReportForm.getSupplierId()!=0)
				{
					System.out.println("The supplier id not null");
					supplierId = ledgerReportForm.getSupplierId();
					option = ledgerReportForm.getReportAgainst();
					fromDate=ledgerReportForm.getFromDate();	
					toDate=ledgerReportForm.getToDate();
					try {
						company = companyService.getCompanyWithCompanyStautarType(ledgerReportForm.getCompanyId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ledgerReportForm.setLedgerId((long)0);
					ledgerReportForm.setSubledgerId((long)0);
					
					
					supplierOpenBalanceList.clear();
					purchaseEntryList.clear();
					paymenttList.clear();
					debitNoteList.clear();
					dayBookList.clear();
					
					/**-------------Calculation for opening balance before start date for all suppliers-----------------*/
					for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplierBeforeFromDate(fromDate, ledgerReportForm.getCompanyId()))
					{
						if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
						{
							Suppliers sup = null;
							try {
								sup = suplliersService.getById((long)bal[0]);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(sup!=null && sup.getSupplier_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
							{
							OpeningBalancesForm form = new OpeningBalancesForm();
						    form.setSupplier_id((long)bal[0]);
							form.setCredit_balance((double)bal[2]);
							form.setDebit_balance((double)bal[1]);
							supplierOpenBalanceList.add(form);
							}
						}
					}
					
					purchaseEntryList = purchaseEntryService.getPurchaseForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getSupplierId(), ledgerReportForm.getCompanyId());
					paymenttList = paymentService.getPaymentForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getSupplierId(), ledgerReportForm.getCompanyId());
					debitNoteList = debitNoteservice.getDebitNoteForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getSupplierId(), ledgerReportForm.getCompanyId());
					System.out.println("purchaseEntryList " +purchaseEntryList.size());
					for(PurchaseEntry purchase :purchaseEntryList) 
					{
						if(purchase.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(purchase.getSupplier_bill_date()!=null)
							{
								form.setDate(purchase.getSupplier_bill_date());
							}
							if(purchase.getVoucher_no()!=null)
							{
								form.setVoucher_Number(purchase.getVoucher_no());
							}
							if(purchase.getSubledger()!=null)
							{
								form.setParticulars(purchase.getSubledger().getSubledger_name());
							}
							if(purchase.getSupplier()!=null)
							{
								form.setSupplier(purchase.getSupplier());
							
								
							}
							form.setVoucher_Type("Purchase");
							Double amount = (double)0;
							
							if(purchase.getRound_off()!=null)
							{
								amount=amount+purchase.getRound_off();
							}
							form.setCredit(amount);
							form.setLocal_time(purchase.getLocal_time());
							form.setType(3);
						dayBookList.add(form);
						}
					}
					
					for(Payment payment :paymenttList) 
					{
						if(payment.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(payment.getDate()!=null)
							{
								form.setDate(payment.getDate());
							}
							if(payment.getVoucher_no()!=null)
							{
								form.setVoucher_Number(payment.getVoucher_no());
							}
							if(payment.getPayment_type()!=null)
							{
								if(payment.getPayment_type().equals(1))
								{
								form.setParticulars("cash");
								}
								else
								{
									if(payment.getBank()!=null)
									{
										form.setParticulars(payment.getBank().getBank_name()+"--"+payment.getBank().getAccount_no());
									}
								}
							}
							if(payment.getSupplier()!=null)
							{
								form.setSupplier(payment.getSupplier());
								
								
							}
							form.setVoucher_Type("Payment");
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
								form.setDebit(amount+Tds);
								
							
							}
							if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(false)))
							{
								if(payment.getAmount()!=null)
								{
									amount=amount+payment.getAmount();
								}
								form.setDebit(amount);
							}
						
							form.setLocal_time(payment.getLocal_time());
							form.setType(1);
							dayBookList.add(form);
						}
					}
					for(DebitNote debitNote :debitNoteList) 
					{
						if(debitNote.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(debitNote.getDate()!=null)
							{
								form.setDate(debitNote.getDate());
							}
							if(debitNote.getVoucher_no()!=null)
							{
								form.setVoucher_Number(debitNote.getVoucher_no());
							}
							if(debitNote.getSubledger()!=null)
							{
								form.setParticulars(debitNote.getSubledger().getSubledger_name());
							
							}
							if(debitNote.getSupplier()!=null)
							{
								form.setSupplier(debitNote.getSupplier());
								
							}
							form.setVoucher_Type("Debit Note");
							Double amount = (double)0;
							
							if(debitNote.getRound_off()!=null)
							{
								amount=amount+debitNote.getRound_off();
							}
							form.setDebit(amount);
						
					
							form.setLocal_time(debitNote.getLocal_time());
							form.setType(6);
						    dayBookList.add(form);
						}
					}
					
					Collections.sort(dayBookList, new Comparator<SupplierCustomerLedgerForm>() {
				        public int compare(SupplierCustomerLedgerForm o1, SupplierCustomerLedgerForm o2) {

				        	LocalDate Date1 = o1.getDate();
				        	LocalDate Date2= o2.getDate();
				            int sComp = Date1.compareTo(Date2);

				            if (sComp != 0) {
				               return sComp;
				            } 

				            LocalTime local_time1 = o1.getLocal_time();
				            LocalTime local_time2= o2.getLocal_time();
				            return local_time1.compareTo(local_time2);
				    }});
					/**------------supplierlist if client choose for all-----------------*/
					if(supplierId.equals((long)-2))
					{
					List<Suppliers> supplierlist =suplliersService.findAllSuppliersOnlyOfCompany(ledgerReportForm.getCompanyId());
					model.addObject("supplierlist",supplierlist);
					}
					else
					{
						try {
							model.addObject("supplierName",suplliersService.getById(supplierId).getCompany_name());
						} catch (MyWebException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					model.addObject("company",company);
					model.addObject("supplierId",supplierId);
					model.addObject("option",option);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate",toDate);
					model.addObject("dayBookList", dayBookList);
					model.addObject("supplierOpenBalanceList",supplierOpenBalanceList);
					model.setViewName("/report/supplier-ledgerReportList");
				}
				else if(ledgerReportForm.getBankId()!=null && ledgerReportForm.getBankId()!=0)
				{
					System.out.println("The Bank id not null");
					bankId = ledgerReportForm.getBankId();
					option = ledgerReportForm.getReportAgainst();
					fromDate=ledgerReportForm.getFromDate();	
					toDate=ledgerReportForm.getToDate();
					try {
						company = companyService.getCompanyWithCompanyStautarType(ledgerReportForm.getCompanyId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ledgerReportForm.setLedgerId((long)0);
					ledgerReportForm.setSubledgerId((long)0);
					
					receiptList.clear();
					paymenttList.clear();
					contraList.clear();
					salesEntryList.clear();
					bankOpenBalanceList.clear();
					dayBookList.clear();
					
					for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(ledgerReportForm.getFromDate(), ledgerReportForm.getCompanyId()))
					{
						if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
						{
							Bank bank = null;
							try {
								bank = bankService.getById((long)bal[0]);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
							{
							OpeningBalancesForm form = new OpeningBalancesForm();
		                    form.setBank_id((long)bal[0]);
							form.setCredit_balance((double)bal[2]);
							form.setDebit_balance((double)bal[1]);
							bankOpenBalanceList.add(form);
							}
						}
					}
				
				    paymenttList  = paymentService.getPaymentListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId(),ledgerReportForm.getBankId());
					receiptList  = receiptService.getReceiptListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId(),ledgerReportForm.getBankId());
					contraList = contraservice.getContraListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId(),ledgerReportForm.getBankId());
					salesEntryList = salesEntryService.getCardSalesForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId(),ledgerReportForm.getBankId());
					
					for(SalesEntry sales :salesEntryList) 
					{
						if(sales.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(sales.getCreated_date()!=null)
							{
								form.setDate(sales.getCreated_date());
							}
							if(sales.getVoucher_no()!=null)
							{
								form.setVoucher_Number(sales.getVoucher_no());
							}
							if(sales.getSubledger()!=null)
							{
								form.setParticulars(sales.getSubledger().getSubledger_name());
							}
							if(sales.getBank()!=null)
							{
								form.setSuppCustName("Card Sales -"+sales.getBank().getBank_name()/*+"-"+sales.getBank().getAccount_no()*/);
								form.setBank(sales.getBank());
							}
							form.setVoucher_Type("Sales");
							Double amount = (double)0;
							
							if(sales.getRound_off()!=null)
							{
								amount=amount+sales.getRound_off();
							}
							form.setDebit(amount);
							form.setLocal_time(sales.getLocal_time());
							form.setType(4);
						dayBookList.add(form);
						}
					}
					
					for(Contra contra :contraList) 
					{
						if(contra.getLocal_time()!=null)
						{
							if((contra.getDeposite_to()!=null)||(contra.getWithdraw_from()!=null))
							{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(contra.getDate()!=null)
							{
								form.setDate(contra.getDate());
							}
							if(contra.getVoucher_no()!=null)
							{
								form.setVoucher_Number(contra.getVoucher_no());
							}
							if(contra.getType().equals(2))
							{
								form.setParticulars(contra.getWithdraw_from().getBank_name());
								form.setBank(contra.getWithdraw_from());
							}
							else if(contra.getType().equals(1))
							{
								form.setParticulars(contra.getDeposite_to().getBank_name());
								form.setBank(contra.getDeposite_to());
							}
							else
							{
								form.setWithdraw_from(contra.getWithdraw_from());
								form.setDeposite_to(contra.getDeposite_to());
							}
								
							if(contra.getType().equals(2))
							{
								form.setVoucher_Type("Contra-Withdraw");
								form.setContratType(contra.getType());
							}
							else if(contra.getType().equals(1))
							{
								form.setVoucher_Type("Contra-Deposit");
								form.setContratType(contra.getType());
							}
							else
							{
								form.setVoucher_Type("Contra-Transfer");
								form.setContratType(contra.getType());
							}
						
							Double amount = (double)0;
							
							if(contra.getType().equals(2))
							{
								if(contra.getAmount()!=null)
								{
									amount=amount+contra.getAmount();
								}
								form.setCredit(amount);
								
							}
							else if(contra.getType().equals(1))
							{
								if(contra.getAmount()!=null)
								{
									amount=amount+contra.getAmount();
								}
								form.setDebit(amount);
								
							}
							else
							{
								
								if(contra.getAmount()!=null)
								{
									amount=amount+contra.getAmount();
								}
								form.setTransferAmount(amount);
								
							}
					
							form.setLocal_time(contra.getLocal_time());
							form.setType(7);
						dayBookList.add(form);
						   }
						}
					}
					
					for(Receipt receipt :receiptList) 
					{
						if(receipt.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(receipt.getDate()!=null)
							{
								form.setDate(receipt.getDate());
							}
							if(receipt.getVoucher_no()!=null)
							{
								form.setVoucher_Number(receipt.getVoucher_no());
							}
							if(receipt.getBank()!=null)
							{
								form.setParticulars(receipt.getBank().getBank_name()/*+"-"+receipt.getBank().getAccount_no()*/);
								form.setBank(receipt.getBank());
							}
							if(receipt.getCustomer()!=null)
							{
								form.setSuppCustName(receipt.getCustomer().getFirm_name());
								
							}
							else if(receipt.getSubLedger()!=null) 
							{
								form.setSuppCustName(receipt.getSubLedger().getSubledger_name());
							}
							form.setVoucher_Type("Receipt");
							Double Tds = (double)0;
							Double Tax = (double)0;
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
								
								if(receipt.getCgst()!=null)
								{
									Tax=Tax+receipt.getCgst();
								}
								if(receipt.getSgst()!=null)
								{
									Tax=Tax+receipt.getSgst();
								}
								if(receipt.getIgst()!=null)
								{
									Tax=Tax+receipt.getIgst();
								}
								if(receipt.getState_compansation_tax()!=null)
								{
									Tax=Tax+receipt.getState_compansation_tax();
								}
								
								form.setDebit(amount-Tax);
							
							}
							if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(false)))
							{
								if(receipt.getAmount()!=null)
								{
									amount=amount+receipt.getAmount();
								}
								if(receipt.getCgst()!=null)
								{
									Tax=Tax+receipt.getCgst();
								}
								if(receipt.getSgst()!=null)
								{
									Tax=Tax+receipt.getSgst();
								}
								if(receipt.getIgst()!=null)
								{
									Tax=Tax+receipt.getIgst();
								}
								if(receipt.getState_compansation_tax()!=null)
								{
									Tax=Tax+receipt.getState_compansation_tax();
								}
								
								form.setDebit(amount-Tax);
							}
						
							form.setLocal_time(receipt.getLocal_time());
							form.setType(2);
							dayBookList.add(form);
						}
					}
					
					for(Payment payment :paymenttList) 
					{
						if(payment.getLocal_time()!=null)
						{
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							if(payment.getDate()!=null)
							{
								form.setDate(payment.getDate());
							}
							if(payment.getVoucher_no()!=null)
							{
								form.setVoucher_Number(payment.getVoucher_no());
							}
							if(payment.getBank()!=null)
							{
								form.setParticulars(payment.getBank().getBank_name()/*+"--"+payment.getBank().getAccount_no()*/);
								form.setBank(payment.getBank());
							}
							if(payment.getSupplier()!=null)
							{
								form.setSuppCustName(payment.getSupplier().getCompany_name());
								
							}
							else if(payment.getSubLedger()!=null) 
							{
								form.setSuppCustName(payment.getSubLedger().getSubledger_name());
							}
							form.setVoucher_Type("Payment");
							Double Tds = (double)0;
							Double Tax = (double)0;
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
								if(payment.getCGST_head()!=null)
								{
									Tax=Tax+payment.getCGST_head();
								}
								if(payment.getSGST_head()!=null)
								{
									Tax=Tax+payment.getSGST_head();
								}
								if(payment.getIGST_head()!=null)
								{
									Tax=Tax+payment.getIGST_head();
								}
								if(payment.getSCT_head()!=null)
								{
									Tax=Tax+payment.getSCT_head();
								}
								form.setCredit(amount-Tax);
								
								
							
							}
							if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(false)))
							{
								if(payment.getAmount()!=null)
								{
									amount=amount+payment.getAmount();
								}
								if(payment.getCGST_head()!=null)
								{
									Tax=Tax+payment.getCGST_head();
								}
								if(payment.getSGST_head()!=null)
								{
									Tax=Tax+payment.getSGST_head();
								}
								if(payment.getIGST_head()!=null)
								{
									Tax=Tax+payment.getIGST_head();
								}
								if(payment.getSCT_head()!=null)
								{
									Tax=Tax+payment.getSCT_head();
								}
								form.setCredit(amount-Tax);
							}
						
							form.setLocal_time(payment.getLocal_time());
							form.setType(1);
							dayBookList.add(form);
						}
					}
					
					
					Collections.sort(dayBookList, new Comparator<SupplierCustomerLedgerForm>() {
				        public int compare(SupplierCustomerLedgerForm o1, SupplierCustomerLedgerForm o2) {

				        	LocalDate Date1 = o1.getDate();
				        	LocalDate Date2= o2.getDate();
				            int sComp = Date1.compareTo(Date2);

				            if (sComp != 0) {
				               return sComp;
				            } 

				            LocalTime local_time1 = o1.getLocal_time();
				            LocalTime local_time2= o2.getLocal_time();
				            return local_time1.compareTo(local_time2);
				    }});
					
					if(bankId.equals((long)-4))
					{
					List<Bank> banklist = bankService.findAllBanksOfCompany(ledgerReportForm.getCompanyId());
					model.addObject("banklist",banklist);
					}
					else
					{
						try {
							model.addObject("bankName",bankService.getById(bankId).getBank_name());
						} catch (MyWebException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					model.addObject("bankId",bankId);
					model.addObject("option",option);
					model.addObject("fromDate",fromDate);
					model.addObject("company",company);
					model.addObject("toDate",toDate);
					model.addObject("dayBookList", dayBookList);
					
					model.addObject("bankOpenBalanceList",bankOpenBalanceList);
					model.setViewName("/report/bank-ledgerReportList");
				}
				
				else{
					System.out.println("ledgerReportForm.getReportAgainst() kpo");
					option = ledgerReportForm.getReportAgainst();
					ledgerId = ledgerReportForm.getLedgerId();
					fromDate=ledgerReportForm.getFromDate();	
					toDate=ledgerReportForm.getToDate();
					try {
						company = companyService.getCompanyWithCompanyStautarType(ledgerReportForm.getCompanyId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					subledgerOPBalanceList.clear();
					subledgerOpenBalanceList.clear();
					ledgerList.clear();
					model.addObject("company",company);
					subledgerId = ledgerReportForm.getSubledgerId();
					
					for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(),ledgerReportForm.getCompanyId(),false))
					{
						if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
						{
							SubLedger sub = null;
							try {
								sub = subLedgerService.getById((long)bal[0]);
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
							{
								OpeningBalancesForm form = new OpeningBalancesForm();
								
								form.setSubledgerName(sub.getSubledger_name());
								try {
									form.setSubledger(sub);
								} catch (Exception e) {
									e.printStackTrace();
								}
							form.setSub_id((long)bal[0]);
							form.setCredit_balance((double)bal[2]);
							form.setDebit_balance((double)bal[1]);
							subledgerOpenBalanceList.add(form);
							} 
						}
					}
					
					if(ledgerId!=0)
					{
						System.out.println("ledgerId!=0");
					if(subledgerId.equals((long)0))
					{
						System.out.println("The subledger is zero");
						allsubList.clear();
						System.out.println("subledgerId.equals((long)0");
						Ledger ledger = ledgerService.findOneWithAll(ledgerId);
						Set<SubLedger> subLedgerList = ledger.getSubLedger();
						model.addObject("subLedgerList",subLedgerList);
						
						
						for(SubLedger sub : subLedgerList)
						{
							CopyOnWriteArrayList<OpeningBalances> subledgerOPBalanceList1= new CopyOnWriteArrayList<OpeningBalances>();
							subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), sub.getSubledger_Id(), ledgerReportForm.getCompanyId());
							
							for(OpeningBalances balance : subledgerOPBalanceList)
							{
								
								
								if(balance.getMjv()!=null && balance.getCredit_balance()>0)
								{
									List<ManualJVDetails>  drmanualdetail= new ArrayList<ManualJVDetails>();
									for(ManualJVDetails detail: journalService.getAllMJVDetails(balance.getMjv().getJournal_id()))
							        {
							            if(detail.getDramount()!=null && detail.getDramount()>0)
							            {
							                drmanualdetail.add(detail);
							            }
							           
							        }
									Collections.sort(drmanualdetail, new Comparator<ManualJVDetails>() {
							            public int compare(ManualJVDetails o1, ManualJVDetails o2) {
							                Long detailid1 = o1.getDetailjv_id();
							                Long detailid2= o2.getDetailjv_id();
							                return detailid1.compareTo(detailid2);
							              
							        }});
									balance.setSublegderdr(drmanualdetail.get(0).getSubledgerdr().getSubledger_name());
									subledgerOPBalanceList1.add(balance);
								}
								else if(balance.getMjv()!=null && balance.getDebit_balance()>0)
								{
									List<ManualJVDetails> crmanualdetail= new ArrayList<ManualJVDetails>();
									
									for(ManualJVDetails detail: journalService.getAllMJVDetails(balance.getMjv().getJournal_id()))
							        {
							            if(detail.getCramount()!=null && detail.getCramount()>0)
							            {
							            	crmanualdetail.add(detail);
							            }
							           
							        }
									Collections.sort(crmanualdetail, new Comparator<ManualJVDetails>() {
							            public int compare(ManualJVDetails o1, ManualJVDetails o2) {
							                Long detailid1 = o1.getDetailjv_id();
							                Long detailid2= o2.getDetailjv_id();
							                return detailid1.compareTo(detailid2);
							              
							        }});
									balance.setSublegderCr(crmanualdetail.get(0).getSubledgercr().getSubledger_name());
									subledgerOPBalanceList1.add(balance);
								}
								else
								{
									subledgerOPBalanceList1.add(balance);
								}
								
							}
								
							allsubList.add(subledgerOPBalanceList1);
							
						}
						model.addObject("allsubList",allsubList);
					}
					else
					{
						CopyOnWriteArrayList<OpeningBalances> subledgerOPBalanceList1= new CopyOnWriteArrayList<OpeningBalances>();
						subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getSubledgerId(), ledgerReportForm.getCompanyId());
						System.out.println("Ledger for Subledger " +subledgerOPBalanceList.size());
						System.out.println("!subledgerId.equals((long)0 " + ledgerReportForm.getSubledgerId());
						
						for(OpeningBalances balance : subledgerOPBalanceList)
						{
						//	System.out.println("The ledger id is"+ balance.getLedger().getLedger_id());
						
							System.out.println("The ledger id is"+ balance.getOpening_id());
							if(balance.getMjv()!=null && balance.getCredit_balance()>0)
							{
								List<ManualJVDetails>  drmanualdetail= new ArrayList<ManualJVDetails>();
								for(ManualJVDetails detail: journalService.getAllMJVDetails(balance.getMjv().getJournal_id()))
						        {
						            if(detail.getDramount()!=null && detail.getDramount()>0)
						            {
						                drmanualdetail.add(detail);
						            }
						           
						        }
								Collections.sort(drmanualdetail, new Comparator<ManualJVDetails>() {
						            public int compare(ManualJVDetails o1, ManualJVDetails o2) {
						                Long detailid1 = o1.getDetailjv_id();
						                Long detailid2= o2.getDetailjv_id();
						                return detailid1.compareTo(detailid2);
						              
						        }});
								
								balance.setSublegderdr(drmanualdetail.get(0).getSubledgerdr().getSubledger_name());
								subledgerOPBalanceList1.add(balance);
							}
							else if(balance.getMjv()!=null && balance.getDebit_balance()>0)
							{
								List<ManualJVDetails> crmanualdetail= new ArrayList<ManualJVDetails>();
								
								for(ManualJVDetails detail: journalService.getAllMJVDetails(balance.getMjv().getJournal_id()))
						        {
						            if(detail.getCramount()!=null && detail.getCramount()>0)
						            {
						            	crmanualdetail.add(detail);
						            }
						           
						        }
								Collections.sort(crmanualdetail, new Comparator<ManualJVDetails>() {
						            public int compare(ManualJVDetails o1, ManualJVDetails o2) {
						                Long detailid1 = o1.getDetailjv_id();
						                Long detailid2= o2.getDetailjv_id();
						                return detailid1.compareTo(detailid2);
						              
						        }});
								
								balance.setSublegderCr(crmanualdetail.get(0).getSubledgercr().getSubledger_name());
								subledgerOPBalanceList1.add(balance);
							}
							else
							{
								
								
								subledgerOPBalanceList1.add(balance);
							}
						}
						model.addObject("subledgerOPBalanceList",subledgerOPBalanceList1);
						try {
							model.addObject("subName",subLedgerService.getById(subledgerId).getSubledger_name());
						} catch (MyWebException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					}
					else
					{
						allsubList.clear();
						System.out.println("ledgerId!=0 else");
						ledgerList = ledgerService.findAllLedgersOfCompany(ledgerReportForm.getCompanyId());
						Set<SubLedger> subLedgerList = new HashSet<>();
						for(Ledger ledger : ledgerList)
						{
							for(SubLedger sub : ledger.getSubLedger())
							{
								subLedgerList.add(sub);
							}
						}
						model.addObject("subLedgerList",subLedgerList);
						for(SubLedger sub : subLedgerList)
						{
							CopyOnWriteArrayList<OpeningBalances> subledgerOPBalanceList1= new CopyOnWriteArrayList<OpeningBalances>();
							subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), sub.getSubledger_Id(), ledgerReportForm.getCompanyId());
							
							for(OpeningBalances balance : subledgerOPBalanceList)
							{
								if(balance.getMjv()!=null && balance.getCredit_balance()>0)
								{
									List<ManualJVDetails>  drmanualdetail= new ArrayList<ManualJVDetails>();
									for(ManualJVDetails detail: journalService.getAllMJVDetails(balance.getMjv().getJournal_id()))
							        {
							            if(detail.getDramount()!=null && detail.getDramount()>0)
							            {
							                drmanualdetail.add(detail);
							            }
							           
							        }
									Collections.sort(drmanualdetail, new Comparator<ManualJVDetails>() {
							            public int compare(ManualJVDetails o1, ManualJVDetails o2) {
							                Long detailid1 = o1.getDetailjv_id();
							                Long detailid2= o2.getDetailjv_id();
							                return detailid1.compareTo(detailid2);
							              
							        }});
									if(drmanualdetail.get(0).getSubledgerdr()!=null){
										balance.setSublegderdr(drmanualdetail.get(0).getSubledgerdr().getSubledger_name());
									}else if(drmanualdetail.get(0).getSupplierdr()!=null){
										balance.setSublegderdr(drmanualdetail.get(0).getSupplierdr().getCompany_name());
									}else if(drmanualdetail.get(0).getCustomerdr()!=null){
										balance.setSublegderdr(drmanualdetail.get(0).getCustomerdr().getFirm_name());
									}
									
									subledgerOPBalanceList1.add(balance);
								}
								else if(balance.getMjv()!=null && balance.getDebit_balance()>0)
								{
									List<ManualJVDetails> crmanualdetail= new ArrayList<ManualJVDetails>();
									
									for(ManualJVDetails detail: journalService.getAllMJVDetails(balance.getMjv().getJournal_id()))
							        {
							            if(detail.getCramount()!=null && detail.getCramount()>0)
							            {
							            	crmanualdetail.add(detail);
							            }
							           
							        }
									Collections.sort(crmanualdetail, new Comparator<ManualJVDetails>() {
							            public int compare(ManualJVDetails o1, ManualJVDetails o2) {
							                Long detailid1 = o1.getDetailjv_id();
							                Long detailid2= o2.getDetailjv_id();
							                return detailid1.compareTo(detailid2);
							              
							        }});
									if(crmanualdetail.get(0).getSubledgerdr()!=null){
										balance.setSublegderdr(crmanualdetail.get(0).getSubledgerdr().getSubledger_name());
									}else if(crmanualdetail.get(0).getSupplierdr()!=null){
										balance.setSublegderdr(crmanualdetail.get(0).getSupplierdr().getCompany_name());
									}else if(crmanualdetail.get(0).getCustomerdr()!=null){
										balance.setSublegderdr(crmanualdetail.get(0).getCustomerdr().getFirm_name());
									}
									//balance.setSublegderCr(crmanualdetail.get(0).getSubledgercr().getSubledger_name());
									subledgerOPBalanceList1.add(balance);
								}
								else
								{
									System.out.println("The voucher type is " +balance.getSubLedger().getSubledger_name() + balance.getCredit_balance() + balance.getDebit_balance());
									subledgerOPBalanceList1.add(balance);
								}
								
							}
								
							allsubList.add(subledgerOPBalanceList1);
							
							/*allsubList.add(subledgerOPBalanceList);*/
							
						}
						model.addObject("allsubList",allsubList);
						
					}
					
					
					model.addObject("subledgerOpenBalanceList",subledgerOpenBalanceList);
					model.addObject("option",option);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate",toDate);
					model.addObject("subledgerId",subledgerId);
					model.setViewName("/report/subledger-ledgerReportList");
					}
			}
			
			return model;	
		}
		@RequestMapping(value="getBankList" , method=RequestMethod.POST)
		public  @ResponseBody List<Bank> getBankList(@RequestParam("id") Long id)
		{
			
			return bankService.findAllBanksOfCompany(id);
			
		}
		
		@RequestMapping(value="getcustomerList" , method=RequestMethod.POST)
		public  @ResponseBody List<Customer> getcustomerList(@RequestParam("id") Long id)
		{
			
			List<Customer> list = new ArrayList<>();
			 list = customerService.findAllCustomersOnlyOFCompany(id);
			
			return list;
			
		}
	
		@RequestMapping(value="getsuppilerList" , method=RequestMethod.POST)
		public  @ResponseBody List<Suppliers> getsuppilerList(@RequestParam("id") Long id)
		{
			
			return suplliersService.findAllSuppliersOnlyOfCompany(id);
			
		}
		@RequestMapping(value="getSubLedgerList" , method=RequestMethod.POST)
		public  @ResponseBody Set<SubLedger> getSubLedgerList(@RequestParam("id") Long id)
		{
			if(id!=0)
			{
			return subLedgerService.findAllSubLedgerWithRespectToLedger(id);
			}
			else
			{
				return null;
			}
			
		}
		
		@RequestMapping(value="getledgerList" , method=RequestMethod.POST)
		public  @ResponseBody List<Ledger> getledgerList(@RequestParam("id") Long id)
		{
			
			return ledgerService.findLedgersOnlyOfComapany(id);
			
		}
		
		
		
}