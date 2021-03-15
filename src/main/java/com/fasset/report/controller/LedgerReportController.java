package com.fasset.report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasset.controller.abstracts.MyAbstractController;

@Controller
@SessionAttributes("user")
public class LedgerReportController extends MyAbstractController {

	/*@Autowired
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
	private IContraService contraservice;
	
	@Autowired	
	private IReceiptService receiptService;
	
	@Autowired	
	private IPaymentService paymentService;
	
	@Autowired
	private IOpeningBalancesService openingBalancesService;
	
	@Autowired
	private ISalesEntryService salesEntryService ;
	
	
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
	private List<LedgerReport> ledgerReport = new ArrayList<LedgerReport>();
    private List<OpeningBalances> subledgerOPBalanceList= new ArrayList<OpeningBalances>();
    private List<Contra> contraList  = new ArrayList<Contra>();
    private List<Receipt> receiptList  = new ArrayList<Receipt>();
    private List<Payment> paymenttList  = new ArrayList<Payment>();
    private List<SalesEntry> salesEntryList= new ArrayList<SalesEntry>();
    private List<OpeningBalancesForm>customerOpenBalanceList = new ArrayList<OpeningBalancesForm>();
    private List<OpeningBalancesForm> supplierOpenBalanceList =new ArrayList<OpeningBalancesForm>();
    private List<OpeningBalancesForm> bankOpenBalanceList =new ArrayList<OpeningBalancesForm>();
    private List<OpeningBalancesForm>  subledgerOpenBalanceList = new ArrayList<OpeningBalancesForm>();
    private  List<List<OpeningBalances>> allsubList = new ArrayList<List<OpeningBalances>>();
    
	@RequestMapping(value = "ledgerReport", method = RequestMethod.GET)
	public ModelAndView ledgerReport(HttpServletRequest request, HttpServletResponse response) {

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
				
				ledgerReport.clear();
				customerOpenBalanceList.clear();
				ledgerReport = ledgerService.getLedgerReport(ledgerReportForm);
				*//**-------------Calculation for opening balance before start date for all customers-----------------*//*
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
				*//**------------Customerlist if client choose for all-----------------*//*
				if(customerId.equals((long)-1))
				{
					List<Customer> customerlist =customerService.findAllCustomersOnlyOFCompany(company_id);
					model.addObject("customerlist",customerlist);
				}
				model.addObject("company",company);
				model.addObject("customerId",customerId);
				model.addObject("option",option);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate",toDate);
				model.addObject("ledgerReport",ledgerReport);
				model.addObject("customerOpenBalanceList",customerOpenBalanceList);
				model.setViewName("/report/ledgerReportList");
			}
			else if(ledgerReportForm.getSupplierId()!=null && ledgerReportForm.getSupplierId()!=0)
			{ 
				
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
				
				ledgerReport.clear();
				supplierOpenBalanceList.clear();
				ledgerReport = ledgerService.getLedgerReport(ledgerReportForm);
				*//**-------------Calculation for opening balance before start date for all suppliers-----------------*//*
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
				*//**------------supplierlist if client choose for all-----------------*//*
				if(supplierId.equals((long)-2))
				{
				List<Suppliers> supplierlist =suplliersService.findAllSuppliersOnlyOfCompany(company_id);
				model.addObject("supplierlist",supplierlist);
				}
				model.addObject("company",company);
				model.addObject("supplierId",supplierId);
				model.addObject("option",option);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate",toDate);
				model.addObject("ledgerReport",ledgerReport);
				model.addObject("supplierOpenBalanceList",supplierOpenBalanceList);
				model.setViewName("/report/ledgerReportList");
			}
			else if(ledgerReportForm.getBankId()!=null && ledgerReportForm.getBankId()!=0)
			{
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
				
				if(bankId.equals((long)-4))
				{
				List<Bank> banklist = bankService.findAllBanksOfCompany(company_id);
				model.addObject("banklist",banklist);
				}
				
			    paymenttList  = paymentService.getPaymentListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), company_id,ledgerReportForm.getBankId());
				receiptList  = receiptService.getReceiptListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), company_id,ledgerReportForm.getBankId());
				contraList = contraservice.getContraListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), company_id,ledgerReportForm.getBankId());
				salesEntryList = salesEntryService.getCardSalesForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), company_id,ledgerReportForm.getBankId());
				model.addObject("bankId",bankId);
				model.addObject("option",option);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate",toDate);
				model.addObject("receiptList",receiptList);
				model.addObject("paymenttList",paymenttList);
				model.addObject("contraList",contraList);
				model.addObject("salesEntryList",salesEntryList);
				model.addObject("bankOpenBalanceList",bankOpenBalanceList);
				model.setViewName("/report/ledgerReportList");
			}
			
			else{
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
					subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getSubledgerId(), company_id);
					model.addObject("subledgerOPBalanceList",subledgerOPBalanceList);
				}
				model.addObject("subledgerOpenBalanceList",subledgerOpenBalanceList);
				model.addObject("option",option);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate",toDate);
				model.addObject("subledgerId",subledgerId);
				model.setViewName("/report/ledgerReportList");
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
			
			if(ledgerReportForm.getCustomerId()!=null && ledgerReportForm.getCustomerId()!=0)
			{
				
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
				
				ledgerReport.clear();
				customerOpenBalanceList.clear();
				ledgerReport = ledgerService.getLedgerReport(ledgerReportForm);
				*//**-------------Calculation for opening balance before start date for all customers-----------------*//*
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
				*//**------------Customerlist if client choose for all-----------------*//*
				if(customerId.equals((long)-1))
				{
					List<Customer> customerlist =customerService.findAllCustomersOnlyOFCompany(ledgerReportForm.getCompanyId());
					model.addObject("customerlist",customerlist);
				}
				model.addObject("company",company);
				model.addObject("customerId",customerId);
				model.addObject("option",option);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate",toDate);
				model.addObject("ledgerReport",ledgerReport);
				model.addObject("customerOpenBalanceList",customerOpenBalanceList);
				model.setViewName("/report/ledgerReportList");
			}
			else if(ledgerReportForm.getSupplierId()!=null && ledgerReportForm.getSupplierId()!=0)
			{
				
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
				
				ledgerReport.clear();
				supplierOpenBalanceList.clear();
				ledgerReport = ledgerService.getLedgerReport(ledgerReportForm);
				*//**-------------Calculation for opening balance before start date for all suppliers-----------------*//*
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
				*//**------------supplierlist if client choose for all-----------------*//*
				if(supplierId.equals((long)-2))
				{
				List<Suppliers> supplierlist =suplliersService.findAllSuppliersOnlyOfCompany(ledgerReportForm.getCompanyId());
				model.addObject("supplierlist",supplierlist);
				}
				model.addObject("company",company);
				model.addObject("supplierId",supplierId);
				model.addObject("option",option);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate",toDate);
				model.addObject("ledgerReport",ledgerReport);
				model.addObject("supplierOpenBalanceList",supplierOpenBalanceList);
				model.setViewName("/report/ledgerReportList");
			}
			else if(ledgerReportForm.getBankId()!=null && ledgerReportForm.getBankId()!=0)
			{
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
				
				if(bankId.equals((long)-4))
				{
				List<Bank> banklist = bankService.findAllBanksOfCompany(ledgerReportForm.getCompanyId());
				model.addObject("banklist",banklist);
				}
				
			    paymenttList  = paymentService.getPaymentListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId(),ledgerReportForm.getBankId());
				receiptList  = receiptService.getReceiptListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId(),ledgerReportForm.getBankId());
				contraList = contraservice.getContraListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId(),ledgerReportForm.getBankId());
				salesEntryList = salesEntryService.getCardSalesForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId(),ledgerReportForm.getBankId());
				model.addObject("bankId",bankId);
				model.addObject("option",option);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate",toDate);
				model.addObject("receiptList",receiptList);
				model.addObject("paymenttList",paymenttList);
				model.addObject("contraList",contraList);
				model.addObject("salesEntryList",salesEntryList);
				model.addObject("bankOpenBalanceList",bankOpenBalanceList);
				model.setViewName("/report/ledgerReportList");
			}
			
			else{
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
				
				if(subledgerId.equals((long)0))
				{  
					allsubList.clear();
					Ledger ledger = ledgerService.findOneWithAll(ledgerId);
					Set<SubLedger> subLedgerList = ledger.getSubLedger();
					model.addObject("subLedgerList",subLedgerList);
					
					for(SubLedger sub : subLedgerList)
					{
						subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), sub.getSubledger_Id(), ledgerReportForm.getCompanyId());
						allsubList.add(subledgerOPBalanceList);
						
					}
					model.addObject("allsubList",allsubList);
				}
				else
				{
					subledgerOPBalanceList	= openingBalancesService.findAllOPbalancesforSubledger(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getSubledgerId(), ledgerReportForm.getCompanyId());
					model.addObject("subledgerOPBalanceList",subledgerOPBalanceList);
				}
				model.addObject("subledgerOpenBalanceList",subledgerOpenBalanceList);
				model.addObject("option",option);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate",toDate);
				model.addObject("subledgerId",subledgerId);
				model.setViewName("/report/ledgerReportList");
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
		
		return subLedgerService.findAllSubLedgerWithRespectToLedger(id);
		
	}
	
	@RequestMapping(value="getledgerList" , method=RequestMethod.POST)
	public  @ResponseBody List<Ledger> getledgerList(@RequestParam("id") Long id)
	{
		
		return ledgerService.findLedgersOnlyOfComapany(id);
		
	}
	
	
	
	
	
	*/

}