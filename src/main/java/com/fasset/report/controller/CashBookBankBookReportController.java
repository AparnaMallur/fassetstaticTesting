package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.CashBookBankBookReportValidator;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.Contra;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.CashBookBankBookReportForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.SupplierCustomerLedgerForm;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IContraService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ISubLedgerService;

@Controller
@SessionAttributes("user")
public class CashBookBankBookReportController extends MyAbstractController {

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IPaymentService paymentService;

	@Autowired
	private IReceiptService receiptService;
	
	@Autowired 
	private ISalesEntryService salesEntryService;

	@Autowired
	private CashBookBankBookReportValidator validator;

	@Autowired
	private IBankService bankService;
	
	@Autowired	
	private IContraService contraservice;
    @Autowired
	private IOpeningBalancesService openingBalancesService;
    @Autowired
	private ISubLedgerService subLedgerService ;
    @Autowired
	private ISubLedgerDAO subLedgerDAO;
    
	private List<Company> companyList = new ArrayList<Company>();
	private List<Receipt> receiptList = new ArrayList<Receipt>();
	private List<Contra> contraList = new ArrayList<Contra>();
	private List<SupplierCustomerLedgerForm> dayBookList = new ArrayList<SupplierCustomerLedgerForm>();
	private List<Payment> paymenttList  = new ArrayList<Payment>();
	private List<SalesEntry> salesEntryList= new ArrayList<SalesEntry>();
	private List<OpeningBalancesForm> bankOpenBalanceList =new ArrayList<OpeningBalancesForm>();
	private CopyOnWriteArrayList<OpeningBalances> subledgerOPBalanceList= new CopyOnWriteArrayList<OpeningBalances>();
	private List<OpeningBalancesForm>  subledgerOpenBalanceList = new ArrayList<OpeningBalancesForm>();
	private Long subledgerId =null;
	private Company company;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Integer option;
	private Long bankId = null;
	
	@RequestMapping(value = "cashBookBankBookReport", method = RequestMethod.GET)
	public ModelAndView cashBookBankBookReport(HttpServletRequest request, HttpServletResponse response) {

		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");
		Long role = user.getRole().getRole_id();
		ModelAndView model = new ModelAndView();
		CashBookBankBookReportForm form = new CashBookBankBookReportForm();

		if (role.equals(ROLE_SUPERUSER) || role.equals(ROLE_MANAGER)) {
			companyList = companyService.getApprovedCompanies();
			model.addObject("companyList", companyList);
			model.setViewName("/report/cashBookBankBookReportForKpo");
		}
		else if(role.equals(ROLE_EXECUTIVE)) {
			companyList = companyService.getApprovedCompanies(user.getUser_id());
			model.addObject("companyList", companyList);
			model.setViewName("/report/cashBookBankBookReportForKpo");
		}
		else {
			List<Bank> bankList = bankService.findAllBanksOfCompany(company_id);
			model.addObject("bankList", bankList);
			model.setViewName("/report/cashBookBankBookReport");
		}
		model.addObject("form", form);
		return model;	
	}

	@RequestMapping(value = "cashBookBankBookReport", method = RequestMethod.POST)
	public ModelAndView cashBookBankBookReport(@ModelAttribute("form") CashBookBankBookReportForm cashbookform,
			BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		validator.validate(cashbookform, result);
		if (result.hasErrors()) {
			model.addObject("form", cashbookform);
			model.setViewName("/report/cashBookBankBookReport");
		} else {
		
			option = cashbookform.getType();
			if(option.equals(new Integer(1)))//cash
			{
				subledgerOPBalanceList.clear();
				subledgerOpenBalanceList.clear();
				subledgerId = subLedgerDAO.findOne("Cash In Hand", company_id).getSubledger_Id();
				

				for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(cashbookform.getFromDate(), cashbookform.getToDate(),company_id,false))
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
				
				subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(cashbookform.getFromDate(), cashbookform.getToDate(),subledgerId,company_id);
				model.addObject("subledgerOPBalanceList",subledgerOPBalanceList);
				try {
					model.addObject("subName",subLedgerService.getById(subledgerId).getSubledger_name());
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model.addObject("subledgerOpenBalanceList",subledgerOpenBalanceList);
				model.addObject("subledgerId",subledgerId);
			}
			else
			{
				bankId = cashbookform.getBankId();
				receiptList.clear();
				paymenttList.clear();
				contraList.clear();
				salesEntryList.clear();
				bankOpenBalanceList.clear();
				dayBookList.clear();
				
				for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(cashbookform.getFromDate(), company_id))
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
				    
				
				paymenttList  = paymentService.getPaymentListForLedgerReport(cashbookform.getFromDate(), cashbookform.getToDate(), company_id,cashbookform.getBankId());
				receiptList  = receiptService.getReceiptListForLedgerReport(cashbookform.getFromDate(), cashbookform.getToDate(), company_id,cashbookform.getBankId());
				contraList = contraservice.getContraListForLedgerReport(cashbookform.getFromDate(), cashbookform.getToDate(), company_id,cashbookform.getBankId());
				salesEntryList = salesEntryService.getCardSalesForLedgerReport(cashbookform.getFromDate(), cashbookform.getToDate(), company_id,cashbookform.getBankId());
					
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
				
				try {
					model.addObject("bankName",bankService.getById(bankId).getBank_name());
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model.addObject("dayBookList", dayBookList);
				model.addObject("bankOpenBalanceList",bankOpenBalanceList);
				model.addObject("bankId",bankId);
			}
			
			fromDate=cashbookform.getFromDate();
			toDate = cashbookform.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(company_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			model.addObject("toDate", toDate);
			model.addObject("fromDate", fromDate);
			model.addObject("company", company);
			model.addObject("option", option);
			model.setViewName("/report/cashBookBankBookReportList");
		}
		return model;
	}

	@RequestMapping(value = "cashBookBankBookReportForKpo", method = RequestMethod.POST)
	public ModelAndView cashBookBankBookReportForKpo(@ModelAttribute("form") CashBookBankBookReportForm cashbookform,
			BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		Long role = user.getRole().getRole_id();
		validator.validate(cashbookform, result);
		if (result.hasErrors()) {
			if (role.equals(ROLE_SUPERUSER) || role.equals(ROLE_MANAGER)) {
				companyList = companyService.getApprovedCompanies();
			}
			else if(role.equals(ROLE_EXECUTIVE)) {
				companyList = companyService.getApprovedCompanies(user.getUser_id());
			}
			model.addObject("form", cashbookform);
			model.addObject("companyList", companyList);
			model.setViewName("/report/cashBookBankBookReportForKpo");
		} else {
		
			option = cashbookform.getType();
			if(option.equals(new Integer(1)))//cash
			{
				subledgerOPBalanceList.clear();
				subledgerOpenBalanceList.clear();
				subledgerId = subLedgerDAO.findOne("Cash In Hand", cashbookform.getCompanyId()).getSubledger_Id();
				

				for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(cashbookform.getFromDate(), cashbookform.getToDate(),cashbookform.getCompanyId(),false))
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
				
				subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(cashbookform.getFromDate(), cashbookform.getToDate(),subledgerId,cashbookform.getCompanyId());
				model.addObject("subledgerOPBalanceList",subledgerOPBalanceList);
				try {
					model.addObject("subName",subLedgerService.getById(subledgerId).getSubledger_name());
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model.addObject("subledgerOpenBalanceList",subledgerOpenBalanceList);
				model.addObject("subledgerId",subledgerId);
			}
			else
			{
				bankId = cashbookform.getBankId();
				receiptList.clear();
				paymenttList.clear();
				contraList.clear();
				salesEntryList.clear();
				bankOpenBalanceList.clear();
				dayBookList.clear();

				for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(cashbookform.getFromDate(), cashbookform.getCompanyId()))
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
				    
				
				paymenttList  = paymentService.getPaymentListForLedgerReport(cashbookform.getFromDate(), cashbookform.getToDate(), cashbookform.getCompanyId(),cashbookform.getBankId());
				receiptList  = receiptService.getReceiptListForLedgerReport(cashbookform.getFromDate(), cashbookform.getToDate(), cashbookform.getCompanyId(),cashbookform.getBankId());
				contraList = contraservice.getContraListForLedgerReport(cashbookform.getFromDate(), cashbookform.getToDate(), cashbookform.getCompanyId(),cashbookform.getBankId());
				salesEntryList = salesEntryService.getCardSalesForLedgerReport(cashbookform.getFromDate(), cashbookform.getToDate(), cashbookform.getCompanyId(),cashbookform.getBankId());
					
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
				
				try {
					model.addObject("bankName",bankService.getById(bankId).getBank_name());
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model.addObject("dayBookList", dayBookList);
				model.addObject("bankOpenBalanceList",bankOpenBalanceList);
				model.addObject("bankId",bankId);
			}
			
			fromDate=cashbookform.getFromDate();
			toDate = cashbookform.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(cashbookform.getCompanyId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			model.addObject("toDate", toDate);
			model.addObject("fromDate", fromDate);
			model.addObject("company", company);
			model.addObject("option", option);
			model.setViewName("/report/cashBookBankBookReportList");
		}
		return model;
	}

	
}
