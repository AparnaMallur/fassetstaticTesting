/**
 * mayur suramwar
 */
package com.fasset.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.data.time.Year;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.EditProductValidatorForSales;
import com.fasset.controller.validators.SalesEntryValidator;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Customer;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Product;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.entities.Stock;
import com.fasset.entities.SubLedger;
import com.fasset.entities.TaxMaster;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.SalesForm;
import com.fasset.report.controller.ClassToConvertDateForExcell;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IDeducteeService;
import com.fasset.service.interfaces.IGstTaxMasterService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IProductService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.IStockService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ITaxMasterService;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IYearEndingService;
/**
 * @author mayur suramwar
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class SalesEntryController extends MyAbstractController{

	@Autowired
	private IAccountingYearService accountingYearService ;
	
	@Autowired
	private IProductService productService;
	@Autowired
	private IDeducteeService deducteeService;
	
	@Autowired
	private ISalesEntryService entryService ;
	
	@Autowired
	private IBankService bankService;
	
	@Autowired
	private ICustomerService customerService ;
	
	@Autowired
	private IReceiptService receiptService;
	
	@Autowired	
	private ICreditNoteService creditService;
	
	@Autowired
	private SalesEntryValidator validator;
	
	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private ISubLedgerDAO subLedgerDAO;
	
	@Autowired
	ISubLedgerService subledgerService;
	
	@Autowired
	private IStockService stockService;
	
	@Autowired
	private IAccountingYearDAO yearDAO;
	
	@Autowired
	private ITaxMasterService taxservice;
	
	@Autowired
	private IOpeningBalancesService openingbalances;

	@Autowired
	private IGstTaxMasterService gstService;
	
	@Autowired
	private EditProductValidatorForSales editProValidator ;
	
	@Autowired	
	private IYearEndingService yearService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IQuotationService quoteservice;

	@Autowired
	private IClientSubscriptionMasterService subscribeservice;
	
	private Long sales_id =null;
	
    private List<String> successVocharList = new ArrayList<String>();
		
	private List<String> failureVocharList = new ArrayList<String>();
	
	Boolean flag = null; // for maintaining the user on salesEntryList.jsp after delete and view
	
	@RequestMapping(value = "salesEntry", method = RequestMethod.GET)
	public ModelAndView salesEntry(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("executed when added");
        User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		long company_id=(long)session.getAttribute("company_id");
		
		
		ModelAndView model = new ModelAndView();
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		if(AccYear==-1){
			yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		}else{
			yearList = accountingYearService.findAccountRangeOfCompany(strLong);
		 
		}
	//	List<AccountingYear>  yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id); commented not to show account year popup
		
		if(yearList.size()!=0)
		{
			System.out.println("yearList.size()");
		SalesEntry entry = new SalesEntry();
		model.addObject("bankList", bankService.findAllBanksOfCompany(company_id));
		model.addObject("voucherrange", companyService.getVoucherRange(company_id));
		model.addObject("yearList", yearList);
		model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
		model.addObject("productList", productService.findAllProductsOfCompany(company_id));
		model.addObject("subledgerList", subledgerService.findAllSubLedgersOnlyOfCompany(company_id));
		model.addObject("entry", entry);
		model.addObject("stateId",user.getCompany().getState().getState_id());
		
		Company company=null;
		try {
			company = companyService.getById(user.getCompany().getCompany_id());
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addObject("company", company);
		
		Long quote_id = subscribeservice.getquoteofcompany(company_id);
		String email = company.getEmail_id();
		if (quote_id != 0) {
			if(quoteservice.findAllTransactionimportDetailsUser(email, quote_id))// for subscribed client if he registered his company with quotation and quotation contains master imports facility.)
			{
				List<SalesEntry> disabledSalesEntryList= entryService.findAllDisableSalesEntryOfCompanyAfterImport(user.getCompany().getCompany_id(), true);
				
				for(SalesEntry sales : disabledSalesEntryList)
				{
					List<Receipt> receiptList=receiptService.findallreceiptentryofsales(sales.getSales_id());
					 for(int i = 0;i<receiptList.size();i++){	
						 Receipt information= new Receipt();
							information = receiptList.get(i);		
							receiptService.diactivateByIdValue(information.getReceipt_id(),true);
					 }
					 List<CreditNote> creditList=creditService.findBySalesId(sales.getSales_id());
					 for(int i = 0;i<creditList.size();i++){	
						 CreditNote credit= new CreditNote();
							credit = creditList.get(i);		
							creditService.diactivateByIdValue(credit.getCredit_no_id());
					 }
				     entryService.deleteByIdValue(sales.getSales_id(),true);
				
				}
				Quotation quot =null;
				try {
					quot = quoteservice.getById(quote_id);
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				for(QuotationDetails details : quot.getQuotationDetails())
				{
					if(details.getService_id().getService_name().equals("Data Migration"))
					{
						quoteservice.saveupdatedservices(details.getQuotation_detail_id(),false);
					
						break;
					}
				}
				
				
			}
			
		}
		
		 if ((Integer)session.getAttribute("isMobile")!=null) {
			 model.setViewName("/transactions/mobile/viewMobileSalesEntry");
		 }
		 else
		 {
			 model.setViewName("/transactions/salesEntry");
		 }
		
		return model;
		}
		else
		{
			session.setAttribute("msg","Your account is closed");
			return new ModelAndView("redirect:/salesEntryList");
			
		}
		
	}
	
	@RequestMapping(value = "saveSalesEntry", method = RequestMethod.POST)
	public synchronized ModelAndView  saveSalesEntry(@ModelAttribute("entry")SalesEntry newentry, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		
		Long save_id = newentry.getSave_id();
		
		User user = new User();
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		user = (User)session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		long company_id=(long)session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();	
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		Company company=null;
		try {
			company = companyService.getById(user.getCompany().getCompany_id());
			
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addObject("company", company);
		
		validator.validate(newentry, result);
		if(result.hasErrors()){
	
			newentry.setRound_off((double)0);
		    newentry.setCgst((double)0);
		    newentry.setSgst((double)0);
		    newentry.setIgst((double)0);
		    newentry.setTransaction_value((double)0);
		    newentry.setState_compansation_tax((double)0);
		  // newentry.setTotal_vat((double)0);
		  //  newentry.setTotal_vatcst((double)0);
		  //  newentry.setTotal_excise((double)0);
			 if(AccYear==-1){
					yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
				}else{
					yearList = accountingYearService.findAccountRangeOfCompany(strLong);
				 
				}
			model.addObject("bankList", bankService.findAllBanksOfCompany(company_id));
			model.addObject("voucherrange", companyService.getVoucherRange(company_id));
		   // model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
			 model.addObject("yearList",yearList);
			model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
			model.addObject("productList", productService.findAllProductsOfCompany(company_id));
			model.addObject("subledgerList", subledgerService.findAllSubLedgersOnlyOfCompany(company_id));
			model.addObject("stateId",user.getCompany().getState().getState_id());
			
			try {
				company = companyService.getById(user.getCompany().getCompany_id());
				
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addObject("company", company);
			if ((Integer)session.getAttribute("isMobile")!=null) {
				 model.setViewName("/transactions/mobile/viewMobileSalesEntry");
			 }
			 else
			 {
				 model.setViewName("/transactions/salesEntry");
			 }			
			return model;
		}
		else{
			String msg = "";
			Long id = null;
			try{
				if(newentry.getSales_id()!= null)
				{
				long pid = newentry.getSales_id();
				if(pid > 0){
					sales_id=pid;
					newentry.setCompany(user.getCompany());	
					newentry.setUpdated_by(user_id);
					entryService.update(newentry);
					msg ="Sales Entry Saved successfully";
				}
			}
				else{
					newentry.setCreated_date(new LocalDate());
					newentry.setCompany(user.getCompany());	
					newentry.setFlag(true);
					
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            newentry.setIp_address(remoteAddr);
					newentry.setVoucher_no(commonService.getVoucherNumber(newentry.getVoucher_range(), VOUCHER_SALES_ENTRY, newentry.getCreated_date(),newentry.getAccounting_year_id(), company_id));
					newentry.setCreated_by(user_id);					
					id = entryService.saveSalesEntry(newentry);
					sales_id=id;
					msg="Sales Entry Saved Successfully" ;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(save_id==0)
			{
			SalesEntry entry = new SalesEntry();
			try {				
				entry= entryService.findOneWithAll(sales_id);
				if(entry.getCustomer()!=null)
				{
				entry.setCustomer_id(entry.getCustomer().getCustomer_id());
				}
				if(entry.getSubledger()!=null)
				{
				entry.setSubledger_Id(entry.getSubledger().getSubledger_Id());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addObject("bankList", bankService.findAllBanksOfCompany(company_id));
			model.addObject("voucherrange", companyService.getVoucherRange(company_id));
			model.addObject("customerProductList", entryService.findAllSalesEntryProductEntityList(sales_id));
			model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
			model.addObject("productList", productService.findAllProductsOfCompany(company_id));
			model.addObject("subledgerList", subledgerService.findAllSubLedgersOnlyOfCompany(company_id));
			model.addObject("stateId",user.getCompany().getState().getState_id());
			model.addObject("entry", entry);
			model.addObject("successMsg", msg);
			 if ((Integer)session.getAttribute("isMobile")!=null) {
				 model.setViewName("/transactions/mobile/viewMobileSalesEntry");
			 }
			
			 else
			 {
				 model.setViewName("/transactions/salesEntry");
			 }
			}
			else
			{
				    msg="Sales Entry Saved Successfully";
				    session.setAttribute("msg", msg);
				    
				    if ((Integer)session.getAttribute("isMobile")!=null) {
				    	   return new ModelAndView("redirect:/salesEntryList");
					 }
					
					 else
					 {
						   return new ModelAndView("redirect:/salesEntryList");
					 }
				//    return new ModelAndView("redirect:/salesEntryList");
			}
			return model;		
		}	
	}
	
	@RequestMapping(value = "salesEntryList", method = RequestMethod.GET)
	public ModelAndView salesEntryList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		
		boolean importfail=false;
		boolean importflag = false;
		long company_id=(long)session.getAttribute("company_id");
		User user = new User();
		user = (User) session.getAttribute("user");
		Company company=null;
		boolean vchnoflag = false;
		List<SalesEntry> excelvouchrno =
		          entryService.findExcelVoucherNumber(company_id);
		          
		        
		          for (SalesEntry salesEntry : excelvouchrno) {
		          
		          if(salesEntry.getExcel_voucher_no()!=null) {
		          
		          vchnoflag = true;
		            }
		          
		          }
		try {
			company = companyService.getById(user.getCompany().getCompany_id());
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flag = true;
		List<YearEnding>  yearEndlist = yearService.findAllYearEnding(company.getCompany_id());
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		if((String) session.getAttribute("errorMsg")!=null){
			model.addObject("errorMsg", (String) session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		if((String) session.getAttribute("filemsg")!=null)
		{
			model.addObject("filemsg", ((String) session.getAttribute("filemsg")));
			model.addObject("filemsg1", ((String) session.getAttribute("filemsg1")));
			model.addObject("successMsg", "NA");
			model.addObject("successVocharList", successVocharList);
			model.addObject("failureVocharList", failureVocharList);
			session.removeAttribute("filemsg");
			session.removeAttribute("filemsg1");
		}
		List<AccountingYear>  yearList = accountingYearService.findAccountRange(user.getUser_id(), user.getCompany().getYearRange(),company_id);

		String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018
		LocalDate date= new LocalDate();
		String april1stDate=null;
		april1stDate= currentyear+"-"+"04"+"-"+"01";
		LocalDate april1stLocaldate = new LocalDate(april1stDate);
		
		if(date.isBefore(april1stLocaldate)) 
		{
			Integer year = Integer.parseInt(currentyear);
			year=year-1;
			String lastYear =year.toString();
			currentyear=lastYear+"-"+currentyear;
		}
		else if(date.isAfter(april1stLocaldate) || date.equals(april1stLocaldate))
		{
			Integer year = Integer.parseInt(currentyear);
			year=year+1;
			String nextYear =year.toString();
			currentyear=currentyear+"-"+nextYear;
			
		}
		Long yearId= null;
		for(AccountingYear year:yearList)
		{
			if(year.getYear_range().equalsIgnoreCase(currentyear))
			{
				yearId=year.getYear_id();
			}
		}
		List<SalesEntry> entryListFailure = entryService.findAllSalesEntryOfCompany(company_id,false);
		if(entryListFailure.size()!=0){
			importfail=true;
		}
		if(company.getTrial_balance()!=null)
		{
			
				if (company.getTrial_balance() == true)		
					
					model.addObject("opening_flag", "1");
				else
				model.addObject("opening_flag", "0");
		}
		else
			model.addObject("opening_flag", "0");
	
		Long quote_id = subscribeservice.getquoteofcompany(company_id);
		String email = user.getCompany().getEmail_id();
		if (quote_id != 0) {
			importflag = quoteservice.findAllTransactionimportDetailsUser(email, quote_id); // for subscribed client if he registered his company with quotation and quotation contains master imports facility.
		}
		//importflag=true; //
		model.addObject("importflag", importflag);
		model.addObject("importfail", importfail);
		model.addObject("flagFailureList", true);
		model.addObject("yearEndlist", yearEndlist);
		
		
		 if ((Integer)session.getAttribute("isMobile")!=null) {
			 model.addObject("entryList", entryService.findAllSalesEntryOfCompanyForMobile(company_id, true,yearId));
			 model.setViewName("/transactions/mobile/viewMobileSalesEntryList");
		 }
		 else
		 {
			 model.addObject("entryList", entryService.findAllSalesEntryOfCompany(company_id,true
					 ));
			 model.setViewName("/transactions/salesEntryList");
		 }
			
		return model;
	}
	
	

	@RequestMapping(value = "editSalesEntry", method = RequestMethod.GET)
	public synchronized ModelAndView editSalesEntry(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User)session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");
		sales_id=id;
		ModelAndView model = new ModelAndView();
		
		SalesEntry entry = new SalesEntry();
		try {
			
			entry= entryService.findOneWithAll(id);
			if((entry.getReceipts() == null || entry.getReceipts().size() == 0) && (entry.getCreditNote() == null || entry.getCreditNote().size() == 0)) {
				if(entry.getCustomer()!=null)
				{
				entry.setCustomer_id(entry.getCustomer().getCustomer_id());
				}
				if(entry.getSubledger()!=null)
				{
				entry.setSubledger_Id(entry.getSubledger().getSubledger_Id());
				}
				model.addObject("bankList", bankService.findAllBanksOfCompany(company_id));
				model.addObject("voucherrange", companyService.getVoucherRange(company_id));
				model.addObject("customerProductList", entryService.findAllSalesEntryProductEntityList(id));
				model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
				model.addObject("productList", productService.findAllProductsOfCompany(company_id));
				model.addObject("subledgerList", subledgerService.findAllSubLedgersOnlyOfCompany(company_id));
				model.addObject("entry", entry);
				model.addObject("stateId",user.getCompany().getState().getState_id());
				 if ((Integer)session.getAttribute("isMobile")!=null) {
					 model.setViewName("/transactions/mobile/viewMobileSalesEntry");
				 }
				 
				 else
				 {
					 model.setViewName("/transactions/salesEntry");
				 }				
			}
			else if(entry.getReceipts()!=null && entry.getReceipts().size()!= 0) {
				 if ((Integer)session.getAttribute("isMobile")!=null) {
					 session.setAttribute("errorMsg", "You can't edit sales voucher as receipt is already generated for this sales voucher");
						model.setViewName("redirect:/salesEntryList");
			    	 //  return new ModelAndView("redirect:/viewMobileSalesEntryList");
				 }
				
				 else
				 {
					 session.setAttribute("errorMsg", "You can't edit sales voucher as receipt is already generated for this sales voucher");
						model.setViewName("redirect:/salesEntryList");
				 }
				/*
				 * session.setAttribute("errorMsg",
				 * "You can't edit sales voucher as receipt is already generated for this sales voucher"
				 * ); model.setViewName("redirect:/salesEntryList");
				 */
			}
			else if(entry.getCreditNote() != null && entry.getCreditNote().size() != 0) {
				 if ((Integer)session.getAttribute("isMobile")!=null) {
					 session.setAttribute("errorMsg", "You can't edit sales voucher as credit note is already generated for this sales voucher");
								model.setViewName("redirect:/salesEntryList");
			    	 //  return new ModelAndView("redirect:/viewMobileSalesEntryList");
				 }
				
				 else
				 {
					 session.setAttribute("errorMsg", "You can't edit sales voucher as credit note is already generated for this sales voucher");
								model.setViewName("redirect:/salesEntryList");
				 }
				/*
				 * session.setAttribute("errorMsg",
				 * "You can't edit sales voucher as credit note is already generated for this sales voucher"
				 * ); model.setViewName("redirect:/salesEntryList");
				 */
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping(value = "viewSalesEntry", method = RequestMethod.GET)
	public ModelAndView viewSalesEntry(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) throws MyWebException {
		
		ModelAndView model = new ModelAndView();
		SalesEntry entry = new SalesEntry();
		HttpSession session = request.getSession(true);
		try {
			entry = entryService.findOneWithAll(id);
					
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		List<SalesEntry> excelvouchrno = entryService.findExcelVoucherNumber(entry.getCompany().getCompany_id());
        
        String ExcelVoucherNumber = null;
		double closingbalance=0;
		if(entry.getSale_type()==1)
		{
			if(entry.getCompany()!=null && entry.getAccountingYear()!=null && entry.getSubledger()!=null && entry.getCreated_date()!=null)
			{
			closingbalance=openingbalances.getclosingbalance(entry.getCompany().getCompany_id(),entry.getAccountingYear().getYear_id(),entry.getCreated_date(),entry.getSubledger().getSubledger_Id(),2);
			}

		}
		else if(entry.getSale_type()==2)
		{
			if(entry.getCompany()!=null && entry.getAccountingYear()!=null && entry.getBank()!=null && entry.getCreated_date()!=null)
			{
			closingbalance=openingbalances.getclosingbalance(entry.getCompany().getCompany_id(),entry.getAccountingYear().getYear_id(),entry.getCreated_date(),entry.getBank().getBank_id(),3);
			}
		}
		else
		{
			if(entry.getCompany()!=null && entry.getAccountingYear()!=null && entry.getCustomer()!=null && entry.getCreated_date()!=null)
			{
			closingbalance=openingbalances.getclosingbalance(entry.getCompany().getCompany_id(),entry.getAccountingYear().getYear_id(),entry.getCreated_date(),entry.getCustomer().getCustomer_id(),5);
			}
		}
		model.addObject("closingbalance", closingbalance);
		model.addObject("entry", entry);
		model.addObject("ExcelVoucherNumber", ExcelVoucherNumber);
		model.addObject("customerProductList", entryService.findAllSalesEntryProductEntityList(id));
		model.addObject("flag", flag);
		if(entry.getCreated_by()!=null)
		{
		model.addObject("created_by", userService.getById(entry.getCreated_by()));
		}
		if(entry.getUpdated_by()!=null)
		{
		model.addObject("updated_by", userService.getById(entry.getUpdated_by()));
		}
		if((Integer)session.getAttribute("isMobile")!=null)
		{
			model.setViewName("/transactions/mobile/mobileSalesEntryView");
			
			//add view here (viewMobileSalesEntry)
		}
		else
		{
			model.setViewName("/transactions/salesEntryView");
		}
		
		
		
		return model;
	}
	@RequestMapping(value ="deleteSalesEntryProduct", method = RequestMethod.GET)
	public synchronized ModelAndView deleteSalesEntryProduct(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response) 
	{
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User)session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");

		ModelAndView model = new ModelAndView();
		SalesEntry entry = new SalesEntry();
		if(entryService.findAllSalesEntryProductEntityList(sales_id).size()!=1)
		{
		String msg = entryService.deleteSalesEntryProduct(id,sales_id,company_id);
		try {
			entry= entryService.findOneWithAll(sales_id);
			if(entry.getCustomer()!=null)
			{
			entry.setCustomer_id(entry.getCustomer().getCustomer_id());
			}
			if(entry.getSubledger()!=null)
			{
			entry.setSubledger_Id(entry.getSubledger().getSubledger_Id());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		model.addObject("customerProductList", entryService.findAllSalesEntryProductEntityList(sales_id));
		model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
		model.addObject("productList", productService.findAllProductsOfCompany(company_id));
		model.addObject("subledgerList", subledgerService.findAllSubLedgersOnlyOfCompany(company_id));
		model.addObject("stateId",user.getCompany().getState().getState_id());
		model.addObject("entry", entry);
		model.addObject("successMsg", msg);
		if((Integer)session.getAttribute("isMobile")!=null)
		{
			model.setViewName("/transactions/mobile/mobileSalesEntryView");
			
			//add view here (viewMobileSalesEntry)
		}
		else
		{
			model.setViewName("/transactions/salesEntryView");
		}
		
		//model.setViewName("/transactions/salesEntry");	
		return model;
		}
		else
		{
			String msg = "You can't delete this product. You can edit this or you can add your required product first and then delete this product.";
			try {
				entry= entryService.findOneWithAll(sales_id);
				if(entry.getCustomer()!=null)
				{
				entry.setCustomer_id(entry.getCustomer().getCustomer_id());
				}
				if(entry.getSubledger()!=null)
				{
				entry.setSubledger_Id(entry.getSubledger().getSubledger_Id());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
			model.addObject("customerProductList", entryService.findAllSalesEntryProductEntityList(sales_id));
			model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
			model.addObject("productList", productService.findAllProductsOfCompany(company_id));
			model.addObject("subledgerList", subledgerService.findAllSubLedgersOnlyOfCompany(company_id));
			model.addObject("stateId",user.getCompany().getState().getState_id());
			model.addObject("entry", entry);
			model.addObject("successMsg", msg);
			if((Integer)session.getAttribute("isMobile")!=null)
			{
				model.setViewName("/transactions/mobile/mobileSalesEntryView");
				
				//add view here (viewMobileSalesEntry)
			}
			else
			{
				model.setViewName("/transactions/salesEntryView");
			}
			
		//	model.setViewName("/transactions/salesEntry");	
			return model;
		}
		
	}
	
	@RequestMapping(value ="deleteSalesEntry", method = RequestMethod.GET)
	public synchronized ModelAndView deleteSalesEntry(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response) 
	{		
		String msg="";
		HttpSession session = request.getSession(true);
		msg=entryService.deleteByIdValue(id,false);
		List<Receipt> receiptList=receiptService.findallreceiptentryofsales(id);
		 for(int i = 0;i<receiptList.size();i++){	
			 Receipt information= new Receipt();
				information = receiptList.get(i);		
				receiptService.diactivateByIdValue(information.getReceipt_id(),false);
		 }
		 List<CreditNote> creditList=creditService.findBySalesId(id);
		 for(int i = 0;i<creditList.size();i++){	
			 CreditNote credit= new CreditNote();
				credit = creditList.get(i);		
				creditService.diactivateByIdValue(credit.getCredit_no_id());
		 }
		session.setAttribute("msg", msg);
		if(flag == true)
		{
			return new ModelAndView("redirect:/salesEntryList");
		}
		else
		{
			return new ModelAndView("redirect:/salesEntryFailure");
		}
		
	}
	
	
	@RequestMapping(value ="activateSalesEntry", method = RequestMethod.GET)
	public synchronized ModelAndView activateSalesEntry(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response) 
	{		
		String msg="";
		HttpSession session = request.getSession(true);
		msg=entryService.activateByIdValue(id);
		List<Receipt> receiptList=receiptService.findallreceiptentryofsales(id);
		 for(int i = 0;i<receiptList.size();i++){	
			 Receipt information= new Receipt();
				information = receiptList.get(i);		
				receiptService.activateByIdValue(information.getReceipt_id());
		 }
		 List<CreditNote> creditList=creditService.findBySalesId(id);
		 for(int i = 0;i<creditList.size();i++){	
			 CreditNote credit= new CreditNote();
				credit = creditList.get(i);		
				creditService.activateByIdValue(credit.getCredit_no_id());
		 }
		session.setAttribute("msg", msg);
		
		if(flag == true)
		{
			
			 if ((Integer)session.getAttribute("isMobile")!=null) {
				 return new ModelAndView("redirect:/salesEntryList");
			 }
			 else
			 {
				 return new ModelAndView("redirect:/salesEntryList");
			 }
			//return new ModelAndView("redirect:/salesEntryList");
		}
		else
		{
			 if ((Integer)session.getAttribute("isMobile")!=null) {
				 return new ModelAndView("redirect:/salesEntryFailure");
			 }
			 else
			 {
				 return new ModelAndView("redirect:/salesEntryFailure");
			 }
		//	return new ModelAndView("redirect:/salesEntryFailure");
		}
	}
	
	
	@RequestMapping(value = "getAdvanceReceipt", method = RequestMethod.POST, produces =MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody String getAdvanceReceipt(@RequestParam("id") long id,@RequestParam("yid") long yid,
			HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");

		List<Receipt> reciepts = receiptService.getAdvancePaymentList(id, company_id,yid);
		JSONArray jsonArray = new JSONArray();
		for(Receipt receipt : reciepts) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("receipt_id", receipt.getReceipt_id());
			jsonObj.put("date", receipt.getDate());
			jsonObj.put("voucher_no", receipt.getVoucher_no());
			Double amount =(double)0;
			if(receipt.getAmount()!=null && receipt.getTds_paid().equals(true) && receipt.getTds_amount()!=null)
			{
				amount=receipt.getAmount()+receipt.getTds_amount();
			}
			else if(receipt.getAmount()!=null)
			{
				amount=receipt.getAmount();
			}
			
			if(receipt.getIs_extraAdvanceReceived()!=null && receipt.getIs_extraAdvanceReceived().equals(true) && receipt.getSales_bill_id()!=null)
			{
				amount=amount-receipt.getSales_bill_id().getRound_off();
				List<Receipt> receiptList = receiptService.getAllReceiptsAgainstAdvanceReceipt(receipt.getReceipt_id());
				for(Receipt receiptAgainstAdvance : receiptList)
				{
					amount=amount-receiptAgainstAdvance.getAmount();
				}
			}
			
			jsonObj.put("amount", amount);
			jsonArray.put(jsonObj);
		}
		return jsonArray.toString();
	}
	
	@RequestMapping(value = "downloadSales", method = RequestMethod.GET)
    public ModelAndView downloadSales(@RequestParam("id") long id) {
	 
		SalesForm form = new SalesForm();

		SalesEntry entry = new SalesEntry();
		try {
			entry = entryService.findOneWithAll(id);
			//Company company = companyService.getCompanyWithCompanyStautarType(entry.getCompany().getCompany_id());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		form.setSalesEntry(entry);
		form.setCustomerProductList(entryService.findAllSalesEntryProductEntityList(id));

		return new ModelAndView("SalesPdfView", "form", form);
    }
	
	
	/*@RequestMapping(value = "downloadSalesAccountingVoucher", method = RequestMethod.GET)
    public ModelAndView downloadSalesAccountingVoucher(@RequestParam("id") long id) {
	 
		SalesForm form = new SalesForm();

		SalesEntry entry = new SalesEntry();
		try {
			entry = entryService.findOneWithAll(id);
					
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		form.setSalesEntry(entry);
		form.setCustomerProductList(entryService.findAllSalesEntryProductEntityList(id));

		return new ModelAndView("SalesAccountingVoucherPdfView", "form", form);
    }*/
	
		
	@RequestMapping(value = "salesEntryFailure", method = RequestMethod.GET)
	public ModelAndView salesEntryFailure(HttpServletRequest request, HttpServletResponse response) {
	    
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
		flag = false;
		List<YearEnding>  yearEndlist = yearService.findAllYearEnding(company_id);
		
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		//model.addObject("entryList", entryService.findAllSalesEntryOfCompany(company_id,false,true));
		model.addObject("flagFailureList", false);
		model.addObject("yearEndlist", yearEndlist);
		if((Integer)session.getAttribute("isMobile")!=null)
		{
			model.setViewName("/transactions/mobile/viewMobileSalesEntryList");
			
			//add view here (viewMobileSalesEntry)
		}
		else
		{
			model.setViewName("/transactions/salesEntryList");
		}
		
		//		model.setViewName("/transactions/salesEntryList");
		return model;
		}

	   
	   @RequestMapping(value ="editproductdetailforSalesEntry", method = RequestMethod.GET)
		public ModelAndView editproductdetailforSalesEntry(@RequestParam("id") long id, 
				HttpServletRequest request, 
				HttpServletResponse response){
			HttpSession session = request.getSession(true);
			ModelAndView model = new ModelAndView();
			SalesEntryProductEntityClass entry = new SalesEntryProductEntityClass();
			User user = new User();
			GstTaxMaster productgst =new GstTaxMaster();
			TaxMaster productvat= new TaxMaster();
			user = (User) session.getAttribute("user");
			System.out.println("The id of saleEntry is "+id);
			entry= entryService.editproductdetailforSalesEntry(id);
			SalesEntry salesEntry = new SalesEntry();
			salesEntry= entryService.findOneWithAll(entry.getSales_id());
			Product productgst1 = productService.findOneView(entry.getProduct_id());
			if(productgst1.getHsn_san_no()!=null)
			{
			productgst=gstService.getHSNbyDate(salesEntry.getCreated_date(),productgst1.getHsn_san_no());
			}
			else
			{
				productgst=gstService.getHSNbyDate(salesEntry.getCreated_date(),entry.getHSNCode());
			}
			if(productgst1.getTaxMaster()!=null)
			{
				try {
					productvat=taxservice.getById(productgst1.getTaxMaster().getTax_id());
					model.addObject("productvat", productvat);
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			model.addObject("entry", entry);
			model.addObject("salesEntry", salesEntry);

			model.addObject("productgst", productgst);
			model.addObject("stateId",user.getCompany().getState().getState_id());
			if((Integer)session.getAttribute("isMobile")!=null)
			{
				model.setViewName("/transactions/mobile/mobileEditproductdetailforSalesEntry");
				
				//add view here (viewMobileSalesEntry)
			}
			else
			{
				model.setViewName("/transactions/editproductdetailforSalesEntry");
			}
			
			
			//model.setViewName("/transactions/editproductdetailforSalesEntry");	
			return model;		
		}
	   
	   @RequestMapping(value ="saveproductdetailforSalesEntry", method = RequestMethod.POST)
		public synchronized ModelAndView saveproductdetailforSalesEntry(@ModelAttribute("entry")SalesEntryProductEntityClass entry, 
				BindingResult result, 
				HttpServletRequest request, 
				HttpServletResponse response) {
			HttpSession session = request.getSession(true);
			long company_id=(long)session.getAttribute("company_id");			
		    Double transaction_value = (double)0;
			Double cgst = (double)0;
			Double igst = (double)0;
			Double sgst = (double)0;
			Double vat = (double)0;
			Double vatcst = (double)0;
			Double excise = (double)0;
			Double state_comp_tax = (double)0;
			Double round_off = (double)0;
			Double CGST = (double)0;
			Double IGST = (double)0;
			Double SGST = (double)0;
			Double state_com_tax = (double)0;
			Double transaction_amount=(double)0;
			Double roundamount=(double)0;
			Double VAT = (double)0;
			Double VATCST = (double)0;
			Double Excise = (double)0;
			Double new_transaction_amount=(double)0;
			Double new_round_amount=(double)0;
			Double amount = (double)0;
			Double damount = (double)0;
			Double disamount = (double)0;
			Double tdsnew=(double)0;
			Double tdsnew1=(double)0;
			Double tdsrate = (double)0;
			Double tds=(double)0;
			Integer tdsapply=null;
			
			SubLedger subledger1 = new SubLedger();
			Customer customer1 = new Customer();
			 ModelAndView model = new ModelAndView();
			 editProValidator.validate(entry, result);
				if(result.hasErrors()){
					if((Integer)session.getAttribute("isMobile")!=null)
					{
						model.setViewName("/transactions/mobile/mobileEditproductdetailforSalesEntry");
						
						//add view here (viewMobileSalesEntry)
					}
					else
					{
						model.setViewName("/transactions/editproductdetailforSalesEntry");
					}
					
					//model.setViewName("/transactions/editproductdetailforSalesEntry");			
					return model;
				}
				else
				{
					SalesEntryProductEntityClass salesdetails = new SalesEntryProductEntityClass();
					
					salesdetails= entryService.editproductdetailforSalesEntry(entry.getSales_detail_id());
					SalesEntry salesEntry = new SalesEntry();
					
					try {			
						salesEntry= entryService.findOneWithAll(entry.getSales_id());
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
			
					if(salesEntry.getCustomer()!=null)
					{
						customer1=salesEntry.getCustomer();
					    tdsapply=salesEntry.getCustomer().getTds_applicable();
					    if(salesEntry.getCustomer().getTds_rate()!=null)
					    {
				        tdsrate = (double)salesEntry.getCustomer().getTds_rate();
					    }
					}
					if(salesEntry.getSubledger()!=null)
					{
					subledger1=salesEntry.getSubledger();
					}
					
					if(salesEntry.getTransaction_value()!=null)
					{
					transaction_value= salesEntry.getTransaction_value();
					}
					if(salesEntry.getRound_off()!=null)
					{
					round_off=salesEntry.getRound_off();
					}
					if(salesEntry.getCgst()!=null)
					{
					cgst=salesEntry.getCgst();
					}
					if(salesEntry.getIgst()!=null)
					{
					igst=salesEntry.getIgst();
					}
					if(salesEntry.getSgst()!=null)
					{
					sgst=salesEntry.getSgst();
					}
					if(salesEntry.getState_compansation_tax()!=null)
					{
					state_comp_tax=salesEntry.getState_compansation_tax();
					}
					
					if(salesEntry.getTds_amount()!=null)
					{
						tds=salesEntry.getTds_amount();
					}
					if(salesEntry.getTotal_vat()!=null)
					{
					vat=salesEntry.getTotal_vat();
					}
					
					if(salesEntry.getTotal_vatcst()!=null)
					{
					vatcst=salesEntry.getTotal_vatcst();
					}
					if(salesEntry.getTotal_excise()!=null)
					{
					excise=salesEntry.getTotal_excise();
					}
					
					if(salesdetails.getCGST()!=null)
					{
					CGST=salesdetails.getCGST();
					}
					if(salesdetails.getIGST()!=null) {
					IGST=salesdetails.getIGST();
					}
					if(salesdetails.getSGST()!=null)
					{
					SGST=salesdetails.getSGST();
					}
					if(salesdetails.getState_com_tax()!=null)
					{
					state_com_tax=salesdetails.getState_com_tax();
					}
					if(salesdetails.getTransaction_amount()!=null)
					{
					transaction_amount=salesdetails.getTransaction_amount();
					}
					
					if(salesdetails.getVAT()!=null)
					{
					VAT=salesdetails.getVAT();
					}
					if(salesdetails.getVATCST()!=null)
					{
					VATCST=salesdetails.getVATCST();
					}
					if(salesdetails.getExcise()!=null) 
					{
					Excise=salesdetails.getExcise();
					}
					
					if(VAT==0 && VATCST==0 && Excise==0)
					{
					cgst=cgst-CGST;
					igst=igst-IGST;
					sgst=sgst-SGST;
					state_comp_tax= state_comp_tax-state_com_tax;
					
					if(tdsapply!=null && tdsapply==1)
					{
						tdsnew1=(transaction_value*tdsrate)/100;
					}
					
					transaction_value=transaction_value-transaction_amount;
					roundamount=CGST+IGST+SGST+state_com_tax+transaction_amount;
					
					
					round_off=round_off-roundamount+tdsnew1;	
					
					
					
					cgst=cgst+entry.getCGST();
					igst=igst+entry.getIGST();
					sgst=sgst+entry.getSGST();
					state_comp_tax=state_comp_tax+entry.getState_com_tax();
					
					amount=(entry.getRate()*entry.getQuantity())+entry.getFreight()+entry.getLabour_charges()+entry.getOthers();
					
					damount=entry.getRate()*entry.getQuantity();
					
					if(entry.getDiscount()!=null && entry.getDiscount()!=0) {
						disamount=(damount -(entry.getDiscount()));
						new_transaction_amount=disamount+entry.getFreight()+entry.getLabour_charges()+entry.getOthers();
					}
					else
					{
						new_transaction_amount=amount;
					}
					
					transaction_value=transaction_value+new_transaction_amount;
					new_round_amount=entry.getCGST()+entry.getIGST()+entry.getSGST()+entry.getState_com_tax()+new_transaction_amount;
					round_off=round_off+new_round_amount;
					if(tdsapply!=null && tdsapply==1)
					{
						tdsnew=(transaction_value*tdsrate)/100;
					}
					else 
						tdsnew=(double)0;	
					
					
					}
					else
					{
						vat=vat-VAT;
						vatcst=vatcst-VATCST;
						excise=excise-Excise;
						if(tdsapply!=null && tdsapply==1)
						{
							tdsnew1=(transaction_value*tdsrate)/100;
						}
						transaction_value=transaction_value-transaction_amount;
						roundamount=VAT+VATCST+Excise+transaction_amount;
						round_off=round_off-roundamount+tdsnew1;	
						vat=vat+entry.getVAT();
						vatcst=vatcst+entry.getVATCST();
						excise=excise+entry.getExcise();
						
						amount=(entry.getRate()*entry.getQuantity())+entry.getFreight()+entry.getLabour_charges()+entry.getOthers();
						
						damount=entry.getRate()*entry.getQuantity();
						
						if(entry.getDiscount()!=null && entry.getDiscount()!=0) {
							disamount=(damount -(entry.getDiscount()));
							new_transaction_amount=disamount+entry.getFreight()+entry.getLabour_charges()+entry.getOthers();
						}
						else
						{
							new_transaction_amount=amount;
						}
						
						transaction_value=transaction_value+new_transaction_amount;
						new_round_amount=entry.getVAT()+entry.getVATCST()+entry.getExcise()+new_transaction_amount;
						round_off=round_off+new_round_amount;
						if(tdsapply!=null && tdsapply==1)
						{
							tdsnew=(transaction_value*tdsrate)/100;
						}
						else 
							tdsnew=(double)0;	
						
					}
					round_off=round_off-tdsnew;
					
					if(customer1!=null)
					{
					 try {
						 customerService.addcustomertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
								 customer1.getCustomer_id(), (long) 5, (double)0,(double)salesEntry.getRound_off(),
									(long) 0);
						 
						 customerService.addcustomertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
								 customer1.getCustomer_id(), (long) 5, (double)0,(double)round(round_off,2),
									(long) 1);
						 
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
					else
					{
						SubLedger subledgercgst = subLedgerDAO.findOne("Cash In Hand", company_id);
						
						if(subledgercgst!=null)
						{
						subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id, subledgercgst.getSubledger_Id(), (long) 2,(double)0,(double)salesEntry.getRound_off(), (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
						subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id, subledgercgst.getSubledger_Id(), (long) 2,(double)0,(double)round(round_off,2), (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);

						}
					}
					if(subledger1!=null)
					{
							try {
								subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
										subledger1.getSubledger_Id(), (long) 2, (double)salesEntry.getTransaction_value(), (double)0,
										(long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
								
								subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
										subledger1.getSubledger_Id(), (long) 2, (double)transaction_value, (double)0,
										(long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
								
							} catch (Exception e) {
								
								e.printStackTrace();
							}
					}
			
					if(salesEntry.getAgainst_advance_receipt()!=null && salesEntry.getAgainst_advance_receipt()==false)
					{
						SubLedger subledgerTds = subLedgerDAO.findOne("TDS Receivable", salesEntry.getCompany().getCompany_id());
						if (subledgerTds != null) {
							subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)tds, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
							subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
									subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)tdsnew, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
							try
							{
								
								
									SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST",
											company_id);
									if (subledgercgst != null) {
										
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgercgst.getSubledger_Id(), (long) 2, (double)salesEntry.getCgst(), (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
										
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
									}
								
								
								
								
								
									SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
											company_id);
									
									if (subledgersgst != null) {
										
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgersgst.getSubledger_Id(), (long) 2, (double)salesEntry.getSgst(), (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
										
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
										
									}
						
								
								
								
							
								
									SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
											company_id);
									if (subledgerigst != null) {
										
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgerigst.getSubledger_Id(), (long) 2, (double)salesEntry.getIgst(), (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
										
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
							
								}
								
								
								
								
									SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
											company_id);
									
									if (subledgercess != null) {
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgercess.getSubledger_Id(), (long) 2, (double)salesEntry.getState_compansation_tax(),
												(double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax,
												(double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
									}
								
								
									SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
											company_id);
									
									if (subledgervat != null) {
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgervat.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vat(), (double)0,
												(long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
										
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double)0,
												(long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
									}
								
									SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
											company_id);
									if (subledgercst != null) {
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgercst.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_vatcst(), (double)0,
												(long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double)0,
												(long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
									}
								
									SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
											company_id);
									if (subledgerexcise != null) {
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgerexcise.getSubledger_Id(), (long) 2, (double)salesEntry.getTotal_excise(), (double)0,
												(long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
										subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
												subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double)0,
												(long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
									}
								
								
								
								
							}
								
							catch(Exception e)
							{
								e.printStackTrace();
							}
						
							salesEntry.setCgst(round(cgst,2));
							salesEntry.setIgst(round(igst,2));
							salesEntry.setSgst(round(sgst,2));
							salesEntry.setState_compansation_tax(round(state_comp_tax,2));
							salesEntry.setTotal_vat(vat);
							salesEntry.setTotal_vatcst(vatcst);
							salesEntry.setTotal_excise(excise);
							salesEntry.setTransaction_value(round(transaction_value,2));
							salesEntry.setRound_off(round(round_off,2));							
							salesEntry.setTds_amount(tdsnew);		
							entryService.updateSalesEntry(salesEntry);
							/*Double oldqty=salesdetails.getQuantity();//old quantity
							Double newqty = entry.getQuantity();//new quantity	
							Double newRate= entry.getRate();//new rate
							double oldamt=salesdetails.getQuantity()*salesdetails.getRate();//old amount
						    double newamt=entry.getQuantity()*entry.getRate();//new amount	
						    
						    if(!salesdetails.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
							{
							Integer pflag=productService.checktype(company_id,salesdetails.getProduct_id());
						     if(pflag==1)  
						     {
						      Stock stock = null;
						      stock=stockService.isstockexist(company_id,salesdetails.getProduct_id());
								if(stock!=null)
								{
									double totalsubamount = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, salesdetails.getProduct_id(), entry.getQuantity());
									stock.setQuantity((stock.getQuantity()+oldqty)-newqty);
									stock.setAmount(stock.getAmount()+oldamt-totalsubamount);
									stockService.updateStock(stock);
								}
						     }	
							}*/
						     
						     
				    salesdetails.setQuantity(entry.getQuantity());
					salesdetails.setRate(entry.getRate());
					salesdetails.setDiscount(entry.getDiscount());
					salesdetails.setCGST(entry.getCGST());
					salesdetails.setIGST(entry.getIGST());
					salesdetails.setSGST(entry.getSGST());
					salesdetails.setState_com_tax(entry.getState_com_tax());
					salesdetails.setLabour_charges(entry.getLabour_charges());
					salesdetails.setFreight(entry.getFreight());
					salesdetails.setOthers(entry.getOthers());
					salesdetails.setUOM(entry.getUOM());
					salesdetails.setVAT(entry.getVAT());
					salesdetails.setVATCST(entry.getVATCST());
					salesdetails.setExcise(entry.getExcise());
					salesdetails.setTransaction_amount(new_transaction_amount);
					salesdetails.setIs_gst(entry.getIs_gst());
					entryService.updateSalesEntryProductEntityClass(salesdetails);

					 if ((Integer)session.getAttribute("isMobile")!=null) {
						 return new ModelAndView("redirect:/editSalesEntry?id="+salesEntry.getSales_id());
						 }
						 else
						 {
							 return new ModelAndView("redirect:/editSalesEntry?id="+salesEntry.getSales_id());
						 }
						
					//return new ModelAndView("redirect:/editSalesEntry?id="+salesEntry.getSales_id());
				}
		}
	   
	  
		
		@RequestMapping(value = {"importExcelSales"}, method = { RequestMethod.POST })
		public ModelAndView importExcelSales(@RequestParam("excelFile") MultipartFile excelfile,HttpServletRequest request, HttpServletResponse response) throws IOException{
			
			HttpSession session = request.getSession(true);
			long company_id=(long)session.getAttribute("company_id");
			long user_id=(long)session.getAttribute("user_id");
			
			User user = new User();
			user = (User)session.getAttribute("user");
			String yearrange = user.getCompany().getYearRange();
			boolean isValid = true;			
			StringBuffer successmsg = new StringBuffer();
			StringBuffer failuremsg = new StringBuffer();			
			StringBuffer ErrorMsgs=new StringBuffer();
			boolean Err=false;
			Integer failcount=0;
			Integer sucount=0;
			Integer maincount = 0;
			Integer repetProductafterunsuccesfullattempt = 0;		
			successVocharList.clear();
			failureVocharList.clear();			
			
			Long year_id = yearDAO.findcurrentyear();	
			List<Customer> list =customerService.findAllCustomersOnlyOFCompany(company_id);
			List<String> rangelist = companyService.getVoucherRange(company_id);
			List<SubLedger> sublist = subledgerService.findAllSubLedgersOnlyOfCompany(company_id);
			List<Product> productlist = productService.findAllProductsOnlyOfCompany(company_id);
			List<Bank> banklist = bankService.findAllBanksOfCompany(company_id);
			List<AccountingYear> acclist = accountingYearService.findAccountRange(user_id, yearrange,company_id);
			Integer m=0;
			
			try {
				System.out.println("The file name is "+excelfile.getOriginalFilename());
				   if (excelfile.getOriginalFilename().endsWith("xls")) {
		        	System.out.println("file is xls...");
		    			int i = 0;
		    			HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
		    		    HSSFSheet worksheet = workbook.getSheetAt(0);
						if(isValid){
							i = 1;
			    			while (i <= worksheet.getLastRowNum()) {
			    		   Err=false;
									SalesEntry entry = new SalesEntry();
									SalesEntryProductEntityClass entity = new  SalesEntryProductEntityClass();
									Customer customer1 = null;
									SubLedger subledger1=null;
									Product product1= null;
									
									HSSFRow row = worksheet.getRow(i++);	
									//ErrorMsgs.append(System.lineSeparator());
									
									//ErrorMsgs.append(System.lineSeparator());
									////ErrorMsgs.append(row.getRowNum());
									//ErrorMsgs.append(" ");
									//ErrorMsgs.append(row.getCell(0));
									//ErrorMsgs.append(" ");
									
									if(row.getLastCellNum()==0)
									{				
								    	System.out.println("Invalid Data");
									}
									else
									{	
										Double Tds = (double)0;
										Double NewTds = (double)0;
										Double transaction_value = (double)0;
										Double cgst = (double)0;
										Double igst = (double)0 ;
										Double sgst = (double)0;
										Double state_comp_tax = (double)0;
										Double round_off = (double)0;
										Double vat = (double)0;
										Double vatcst = (double)0;
										Double excise = (double)0;
										
										
									
										Integer count=0;
										boolean flag = false ;
										String vocherNofromExcel="";
										String shipingBillNofromExcel="";
										String portCodefromExcel="";
										String vocherRangefromExcel="";
										String ProductName = row.getCell(12).getStringCellValue();
										
										try
										{
											vocherNofromExcel=row.getCell(2).getStringCellValue().trim().replaceAll(" ", "");;
											
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										
										try
										{
											Double voucherNofromExcel= row.getCell(2).getNumericCellValue();
											Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
											vocherNofromExcel=voucherNofromExcel1.toString().trim();
											
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										
										try
										{
											shipingBillNofromExcel=row.getCell(6).getStringCellValue().trim();
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										
										try
										{
											Double shipingBillNfromExcel= row.getCell(6).getNumericCellValue();
											Integer shipingBillNfromExcel1 = shipingBillNfromExcel.intValue();
											shipingBillNofromExcel=shipingBillNfromExcel1.toString().trim();
											
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										
										try
										{
											portCodefromExcel=row.getCell(9).getStringCellValue().trim();
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										
										try
										{
											Double portCodfromExcel= row.getCell(9).getNumericCellValue();
											Integer portCodfromExcel1 = portCodfromExcel.intValue();
											portCodefromExcel=portCodfromExcel1.toString().trim();
											
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										
										
										try
										{
											vocherRangefromExcel=row.getCell(28).getStringCellValue().trim();
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										
										try
										{
											Double vocherrangefromExcel= row.getCell(28).getNumericCellValue();
											Integer vocherRangefromExcel1 = vocherrangefromExcel.intValue();
											vocherRangefromExcel=vocherRangefromExcel1.toString().trim();
											
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										
										    SalesEntry entry1 = entryService.isExcelVocherNumberExist(vocherNofromExcel, company_id);
										
											if(entry1!=null)
											{
												String vocherNo = entry1.getExcel_voucher_no().trim();
												Long company_Id = entry1.getCompany().getCompany_id();
												Double hsncode= row.getCell(11).getNumericCellValue();
												Integer hsncode1 = hsncode.intValue();
												String finalhsncode=hsncode1.toString().trim();
												List<SalesEntryProductEntityClass> prolist = entryService.findAllSalesEntryProductEntityList(entry1.getSales_id());
												Boolean entryflag = false ;
												Boolean mainentryflag = false;
												for(SalesEntryProductEntityClass sEPC : prolist)
												{
													if(finalhsncode.replace(" ","").trim().equalsIgnoreCase(sEPC.getHSNCode().replace(" ","").trim()) && ProductName.replace(" ","").trim().equalsIgnoreCase(sEPC.getProduct_name().replace(" ","").trim()))
													{
														entryflag=true;
													}
												}
												
												if((company_Id.equals(company_id))&&(vocherNo.replace(" ","").trim().equalsIgnoreCase(vocherNofromExcel.replace(" ","").trim()))&&(entryflag==true))
												{
													mainentryflag=true;
												}
												
												if(mainentryflag==false)
												{
												flag = true;
												 
												SalesEntry oldentry = entryService.findOneWithAll(entry1.getSales_id());
												
												if((oldentry.getSale_type()==0))
												{											
														if(oldentry.getCustomer()!=null) 
														{
															customer1=oldentry.getCustomer();
														}
												}
												if(oldentry.getSubledger()!=null)
												{
													subledger1=oldentry.getSubledger();
												}
												
												transaction_value=oldentry.getTransaction_value();
												round_off=oldentry.getRound_off();
												cgst=oldentry.getCgst();
												igst=oldentry.getIgst();
												sgst= oldentry.getSgst();
												state_comp_tax=oldentry.getState_compansation_tax();
												vat= oldentry.getTotal_vat();
												vatcst=oldentry.getTotal_vatcst();
												excise=oldentry.getTotal_excise();
												if(oldentry.getTds_amount()!=null) 
												{
												Tds=oldentry.getTds_amount();
												}
												//credit card sale
												
									if((oldentry.getSale_type()==0))
									{
												for(Customer customer:list)
												{
													Boolean flag1 = false;
													if((customer1!=null) && (customer1.getCustomer_id().equals(customer.getCustomer_id())))
													{
												try {
													for (Product product : customerService.findOneWithAll(customer.getCustomer_id()).getProduct()) {
														
														if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals(""))
														{
														String str = product.getHsn_san_no();
														String strproductName = product.getProduct_name();
														if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
															entity.setProduct_name(product.getProduct_name());
															entity.setProduct_id(product.getProduct_id());
															product1=product;
															entity.setIs_gst(product.getTax_type());
															flag1=true;
															break;

														}
														}
														else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
														{
															
															String strproductName = product.getProduct_name().trim();
															if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
															entity.setProduct_name(product.getProduct_name());
															entity.setProduct_id(product.getProduct_id());
															flag1=true;
															product1=product;
															entity.setIs_gst(product.getTax_type());
															break;
														}
														}
														
													}
													if(flag1==true)
													{
														break;
													}
													}
														catch (Exception e) {
															e.printStackTrace();
														}
														
														}	
													
													
												}
									}
									else
									{
										try {
											
											for (Product product : productlist) {
												
												if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals(""))
												{
													
													
												String str = product.getHsn_san_no().trim();
												String strproductName = product.getProduct_name().trim();
												if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
													
													entity.setProduct_name(product.getProduct_name());
													entity.setProduct_id(product.getProduct_id());
													product1=product;
													entity.setIs_gst(product.getTax_type());
													break;

												}
												}
												else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
												{
													
													String strproductName = product.getProduct_name().trim();
													if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
													entity.setProduct_name(product.getProduct_name());
													entity.setProduct_id(product.getProduct_id());
													product1=product;
													entity.setIs_gst(product.getTax_type());
													break;
												}
												}
											}
										
										}
												catch (Exception e) {
													e.printStackTrace();
												}
									}
												
												if(row.getCell(13)!=null)
												{
												entity.setQuantity((Double) row.getCell(13).getNumericCellValue());
												}else
												{
													entity.setQuantity((double)0);
												}
												
												if(row.getCell(14)!=null)
												{
													entity.setRate(round((Double) row.getCell(14).getNumericCellValue(),2));
												}else
												{
													entity.setRate((double)0);
												}
												
												if(row.getCell(15)!=null)
												{
													entity.setDiscount(round((Double) row.getCell(15).getNumericCellValue(),2));
												}else
												{
													entity.setDiscount((double)0);
												}
												

												if(row.getCell(16)!=null)
												{
													entity.setCGST(round((Double) row.getCell(16).getNumericCellValue(),2));
												}else
												{
													entity.setCGST((double)0);
												}

												if(row.getCell(17)!=null)
												{
													entity.setIGST(round((Double) row.getCell(17).getNumericCellValue(),2));
												}else
												{
													entity.setIGST((double)0);
												}

												if(row.getCell(18)!=null)
												{
													entity.setSGST(round((Double) row.getCell(18).getNumericCellValue(),2));
												}else
												{
													entity.setSGST((double)0);
												}

												if(row.getCell(19)!=null)
												{
													entity.setState_com_tax(round((Double) row.getCell(19).getNumericCellValue(),2));

												}else
												{
													entity.setState_com_tax((double)0);
												}

												if(row.getCell(20)!=null)
												{
													entity.setLabour_charges(round((Double) row.getCell(20).getNumericCellValue(),2));

												}else
												{
													entity.setLabour_charges((double)0);
												}
												if(row.getCell(21)!=null)
												{
													entity.setFreight(round((Double) row.getCell(21).getNumericCellValue(),2));

												}else
												{
													entity.setFreight((double)0);
												}
												
												if(row.getCell(22)!=null)
												{
													entity.setOthers(round((Double) row.getCell(22).getNumericCellValue(),2));

												}else
												{
													entity.setOthers((double)0);
												}
												
												if(row.getCell(23)!=null)
												{
													entity.setUOM(row.getCell(23).getStringCellValue().trim());

												}else
												{
													entity.setUOM("NA");
												}
												
												entity.setHSNCode(finalhsncode);

												  if(row.getCell(24)!=null)
													{
														entity.setVAT(round((Double) row.getCell(24).getNumericCellValue(),2));


													}else
													{
														entity.setVAT((double)0);
													}
													
													if(row.getCell(25)!=null)
													{
														entity.setVATCST(round((Double) row.getCell(25).getNumericCellValue(),2));


													}else
													{
														entity.setVATCST((double)0);
													}
													
													if(row.getCell(26)!=null)
													{
														entity.setExcise(round((Double) row.getCell(26).getNumericCellValue(),2));


													}
													else
													{
														entity.setExcise((double)0);
													}
													
									
													cgst=cgst+entity.getCGST();
													igst=igst+entity.getIGST();
													sgst=sgst+entity.getSGST();
													state_comp_tax= state_comp_tax+entity.getState_com_tax();
													vat=vat+entity.getVAT();
													vatcst=vatcst+entity.getVATCST();
													excise=excise+entity.getExcise();
													
												    Double tamount=(double)0;
													Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFreight()+entity.getOthers());
													Double damount = (entity.getRate()*entity.getQuantity());
													
													
													if(entity.getDiscount()!=0)
													{
														Double disamount =((round(damount,2))-(entity.getDiscount()));
														tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFreight()+entity.getOthers();
													}
													else
													{
														tamount = amount;
													}
												transaction_value=transaction_value+round(tamount,2);
												round_off=round_off+entity.getCGST()+entity.getIGST()+entity.getSGST()+entity.getState_com_tax()+entity.getVAT()+entity.getVATCST()+entity.getExcise()+tamount ;
												
												
												
												if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
												{
													if(customer1.getTds_rate()!=null)
													{
														NewTds = (transaction_value*customer1.getTds_rate())/100;
													}
												
												}
												round_off=round_off+Tds-NewTds;
											
											if(product1!=null)
											{
			                            
											
											 try {
												 if(oldentry.getSale_type()==1)
												 {
													SubLedger subledgercgst = subLedgerDAO.findOne("Cash In Hand", company_id);
													if(subledgercgst!=null)
													{
													subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id, subledgercgst.getSubledger_Id(), (long) 2,(double)0,(double)oldentry.getRound_off(), (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id, subledgercgst.getSubledger_Id(), (long) 2,(double)0,(double)round(round_off,2), (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												 }
												 else if(oldentry.getSale_type()==2)
												 {
													 if(oldentry.getBank()!=null)
													 {
														bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id, oldentry.getBank().getBank_id(), (long) 3, (double)0,(double)oldentry.getRound_off(),(long) 0);
														bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id, oldentry.getBank().getBank_id(), (long) 3, (double)0,(double)round(round_off,2),(long) 1);
													 }
												 }
												 else
												 {
													 if(customer1!=null)
														{
													 customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
															 customer1.getCustomer_id(), (long) 5, (double)0,(double)oldentry.getRound_off(),
																(long) 0);												 
													 customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
															 customer1.getCustomer_id(), (long) 5, (double)0,(double)round(round_off,2),
																(long) 1); 
														}
													   if (Tds>0) {
															
														  
																SubLedger subledgerTds = subLedgerDAO.findOne("TDS Receivable", company_id);
																if (subledgerTds != null) {
																	subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																			subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)Tds, (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
																	subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																			subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)NewTds, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
																}
														
															
														}
													 
												 }											 										 
												} catch (Exception e) {
													
													e.printStackTrace();
												}
											
											
											if(subledger1!=null)
											{
													try {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledger1.getSubledger_Id(), (long) 2, (double)oldentry.getTransaction_value(), (double)0,
																(long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledger1.getSubledger_Id(), (long) 2, (double)transaction_value, (double)0,
																(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														
													} catch (Exception e) {
														
														e.printStackTrace();
													}
											}
											
											
											
													try
													{
														if((oldentry.getCgst()!=null) && (oldentry.getCgst()>0) && (cgst>0) && (oldentry.getCgst()!=cgst))
														{
														
															SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST",
																	company_id);
															if (subledgercgst != null) {
																
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgercgst.getSubledger_Id(), (long) 2, (double)oldentry.getCgst(), (double)0, (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
																
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getCgst()!=null)&&(cgst>0) && (oldentry.getCgst()==0))
														{
															SubLedger subledgercgst = subLedgerDAO.isExists("Output CGST", company_id);
															
															if (subledgercgst != null) {
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double)0,
																		(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														if((oldentry.getSgst()!=null) && (oldentry.getSgst()>0) && (sgst>0) && (oldentry.getSgst()!=sgst))
														{
														
															SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
																	company_id);
															
															if (subledgersgst != null) {
																
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgersgst.getSubledger_Id(), (long) 2, (double)oldentry.getSgst(), (double)0, (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
																
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
																
															}
												
														}
														else if((oldentry.getSgst()!=null)&&(sgst>0)&&(oldentry.getSgst()==0))
														{
															SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
																	company_id);
															if (subledgersgst != null) {
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double)0,
																		(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														if((oldentry.getIgst()!=null) && (oldentry.getIgst()>0) && (igst>0) && (oldentry.getIgst()!=igst))
														{
														
															SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
																	company_id);
															if (subledgerigst != null) {
																
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgerigst.getSubledger_Id(), (long) 2, (double)oldentry.getIgst(), (double)0, (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
																
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													
														}
														}
														else if((oldentry.getIgst()!=null)&&(igst>0)&&(oldentry.getIgst()==0))
														{
															SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
																	company_id);
															if(subledgerigst!=null)
															{
															subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																	subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														if((oldentry.getState_compansation_tax()!=null) && (oldentry.getState_compansation_tax()>0) && (state_comp_tax>0)&&(oldentry.getState_compansation_tax()!=state_comp_tax))
														{
														
															SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
																	company_id);
															
															if (subledgercess != null) {
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgercess.getSubledger_Id(), (long) 2, (double)oldentry.getState_compansation_tax(),
																		(double)0, (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax,
																		(double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getState_compansation_tax()!=null) && (state_comp_tax>0) && (oldentry.getState_compansation_tax()==0))
														{
															SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
																	company_id);
															
															if (subledgercess != null) {
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax,
																		(double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
															
														
														if((oldentry.getTotal_vat()!=null) && (oldentry.getTotal_vat()>0) && (vat>0) && (oldentry.getTotal_vat()!=vat))
														{
														
															SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
																	company_id);
															
															if (subledgervat != null) {
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgervat.getSubledger_Id(), (long) 2, (double)oldentry.getTotal_vat(), (double)0,
																		(long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
																
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double)0,
																		(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getTotal_vat()!=null) && (vat>0) && (oldentry.getTotal_vat()==0))
														{
															SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
																	company_id);
															
															if (subledgervat != null) {
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double)0,
																		(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														if((oldentry.getTotal_vatcst()!=null) && (oldentry.getTotal_vatcst()>0) && (vatcst>0) && (oldentry.getTotal_vatcst()!=vatcst)) 
														{
															SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
																	company_id);
															if (subledgercst != null) {
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgercst.getSubledger_Id(), (long) 2, (double)oldentry.getTotal_vatcst(), (double)0,
																		(long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double)0,
																		(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getTotal_vatcst()!=null) && (vatcst>0) && (oldentry.getTotal_vatcst()==0))
														{
															SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
																	company_id);
															if (subledgercst != null) {
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double)0,
																		(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
															
														if((oldentry.getTotal_excise()!=null) && (oldentry.getTotal_excise()>0) && (excise>0) && (oldentry.getTotal_excise()!=excise))
														{
															SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
																	company_id);
															if (subledgerexcise != null) {
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgerexcise.getSubledger_Id(), (long) 2, (double)oldentry.getTotal_excise(), (double)0,
																		(long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double)0,
																		(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getTotal_excise()!=null) && excise>0 && (oldentry.getTotal_excise()==0))
														{
															SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
																	company_id);
															if (subledgerexcise != null) {
																subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																		subledgerexcise.getSubledger_Id(), (long) 2, (double)excise,
																		(double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														
													}
														
													catch(Exception e)
													{
														e.printStackTrace();
													}
													
													
													entity.setTransaction_amount(round(tamount,2));
													oldentry.setRound_off(round(round_off,2));
													oldentry.setTransaction_value(round(transaction_value,2));
													oldentry.setTds_amount(round(NewTds,2));
													oldentry.setCgst(round(cgst,2));
													oldentry.setIgst(round(igst,2));
													oldentry.setSgst(round(sgst,2));
													oldentry.setState_compansation_tax(round(state_comp_tax,2));
													oldentry.setTotal_vat(round(vat,2));
													oldentry.setTotal_vatcst(round(vatcst,2));
													oldentry.setTotal_excise(round(excise,2));
												
												    entryService.updateSalesEntryThroughExcel(oldentry);
													entity.setSales_id(oldentry.getSales_id());
													entryService.updateSalesEntryProductEntityClassThroughExcel(entity);
													
													
													 //stock
											    		 if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
														{
													     Integer pflag=productService.checktype(company_id,product1.getProduct_id());
													     if(pflag==1)  
													     {
													      Stock stock = null;
													      stock=stockService.isstockexist(company_id,product1.getProduct_id());
															if(stock!=null)
															{
																double amount1 = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(), entity.getQuantity());
																stock.setAmount(stock.getAmount()-amount1);
																stock.setQuantity(stock.getQuantity()-entity.getQuantity());					
																stockService.updateStock(stock);
															}
															
													     }
														}
													  			
			                                         if(repetProductafterunsuccesfullattempt==0)
			                                         {
			                                        	 
			                                        	 sucount=sucount+1;
			                                        	 m = 1;
			                                        	successVocharList.add(vocherNofromExcel);
			                                         }
													
												}
											else
											{
												
												/*entryService.ChangeStatusofSalesEntryThroughExcel(oldentry);
												if(successVocharList!=null && successVocharList.size()>0)
												{
													sucount=sucount-1;
													failcount=failcount+1;
													m = 2;
													successVocharList.remove(vocherNofromExcel);
													failureVocharList.add(vocherNofromExcel);
												}*/
												
													
												
											}	
												/*break;*/
												}
												
												/* if(mainentryflag==true)
												  {	
													  break;
												  }*/
											}
										
										
										
										
											if(flag== false)
											{
												
												if(!failureVocharList.contains(vocherNofromExcel))
												{
												
												 Boolean flagtocheckentry = false;
												 Boolean cashSalesCardSalesFlag =  false;
												 Integer cnt=0;
												 
												 Double hsncode= row.getCell(11).getNumericCellValue();
												 Integer hsncode1 = hsncode.intValue();
												 String finalhsncode=hsncode1.toString().trim();
												 
													String remoteAddr = "";
													remoteAddr = request.getHeader("X-FORWARDED-FOR");
													if (remoteAddr == null || "".equals(remoteAddr)) {
														remoteAddr = request.getRemoteAddr();
													}
													entry.setIp_address(remoteAddr);
													entry.setCreated_by(user_id);
												 
													SalesEntry oldentry = entryService.isExcelVocherNumberExist(vocherNofromExcel, company_id);
												
													if(oldentry !=null && oldentry.getExcel_voucher_no()!=null)
													{
													 String str=oldentry.getExcel_voucher_no().trim();
														if(str.replace(" ","").trim().equalsIgnoreCase(vocherNofromExcel.replace(" ","").trim())) {
															flagtocheckentry=true;
														   /* break;*/
														}
													}
												
												if(flagtocheckentry==false)
												{
													entry.setCompany(user.getCompany());
													
											
													if(row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Cash Sales"))
													{
														entry.setSale_type(1);
														count = count + 1;
														cashSalesCardSalesFlag = true;
														entry.setCustomer(null);
													}
											       	else if(row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Card Sales") && row.getCell(27)!=null)
														{
											       		 cnt=0;
											       		    entry.setSale_type(2);
															entry.setCustomer(null);
															cashSalesCardSalesFlag = true;
															String[] banknameAndAcc = row.getCell(27).getStringCellValue().replace(" ","").trim().split("-");
															String bankname = banknameAndAcc[0];
															String accno = banknameAndAcc[1];
															
															for (Bank bank : banklist) 
															{																
														
																	String strbankname =bank.getBank_name().trim();
																	String straccno = bank.getAccount_no().toString();
																	
																	if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno)) {
																		entry.setBank(bank);
																		entry.setBankId(bank.getBank_id());																		
																		count = count + 1;
																		cnt=1;
																		break;	
																	}
															
															}
															if(cnt==0){
																Err=true;
																ErrorMsgs.append(" Bank is incorrect ");
															}
														}
												   	else
														{
												   		 cnt=0;
												   		    entry.setSale_type(0);
															try {			
																for(Customer customer:list)
																{
																	
																	String str=customer.getFirm_name().trim();
																	if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(0).getStringCellValue().replace(" ","").trim())) {
																		entry.setCustomer(customer);
																		customer1=customer;
																		count=count+1;
																		cnt=1;
																		entry.setSale_type(0);
																	    break;
																	}
																}
															} catch (Exception e) {
																e.printStackTrace();
															}
															if(cnt==0){Err=true;ErrorMsgs.append(" customer First Name does not exists ");}
														}
													
													
												
													
													
												if(row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Cash Sales") || row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Card Sales"))
												{
													 cnt=0;
														for(SubLedger subledger:sublist)
														{
															
															String str =subledger.getSubledger_name().trim();
															if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(3).getStringCellValue().replace(" ","").trim())) {
																entry.setSubledger(subledger);
																subledger1=subledger;
																count=count+1;
																cnt=1;
															    break;
														}
														
												}
														if(cnt==0){Err=true;ErrorMsgs.append(" Income Type incorrect ");}	
												}
												else
												{
														try {
															 cnt=0;
															for(Customer customer:list)
															{
																Boolean flag1 = false;
																
																if((customer1!=null) && (customer1.getCustomer_id().equals(customer.getCustomer_id())))
															  {
																for(SubLedger subledger:customerService.findOneWithAll(customer.getCustomer_id()).getSubLedgers())
																{
																	
																	String str =subledger.getSubledger_name().trim();
																	if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(3).getStringCellValue().replace(" ","").trim())) {
																		entry.setSubledger(subledger);
																		subledger1=subledger;
																		count=count+1;
																		cnt=1;
																		flag1=true;
																	    break;
																	
																    }
																	
																}
																if(flag1==true)
																{
																	break;
																}													
															}												
															}	
															if(cnt==0){Err=true;ErrorMsgs.append(" Income Type incorrect ");}
														}
															catch (Exception e) {
																e.printStackTrace();
															}
												}
												
												
												if(row.getCell(1)!=null)
												{
													try {
														entry.setCreated_date(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(1).getDateCellValue().toString())));
													   }
													   catch (Exception e) {
													    e.printStackTrace();
													    }
													if(entry.getCreated_date()==null){
														Err=true;ErrorMsgs.append("Date format is not correct. Enter date in dd/mm/yyyy format in Invoice Date Column");
														}
												}
												
												try {
													 cnt=0;
													for(AccountingYear year_range: acclist)
													{
														
														String str =year_range.getYear_range().trim();
														if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(10).getStringCellValue().replace(" ","").trim())) {
															entry.setAccountingYear(year_range);
															count=count+1;
															cnt=1;
															if(entry.getCreated_date()!=null)
															{
																for(String range : rangelist)
																{
																	if(range.replace(" ","").trim().equalsIgnoreCase(vocherRangefromExcel.replace(" ","").trim()))
																	{
															     entry.setVoucher_no(commonService.getVoucherNumber(range, VOUCHER_SALES_ENTRY, entry.getCreated_date(),year_range.getYear_id(), company_id));
															          break;
																    }
																}
															}
																
														
														    break;
														}
													}
													if(cnt==0){Err=true;ErrorMsgs.append(" Accounting Year incorrect ");}
												} catch (Exception e) {
													e.printStackTrace();
												}
												
												if(row.getCell(4)!=null)
												{
												entry.setRemark(row.getCell(4).getStringCellValue().trim());
												}
												entry.setLocal_time(new LocalTime());
												entry.setAgainst_advance_receipt(false);
												if(row.getCell(5).getStringCellValue().trim().equalsIgnoreCase("Local"))
												{
													entry.setEntrytype(1);
												}
												else if(row.getCell(5).getStringCellValue().trim().equalsIgnoreCase("Export"))
												{
													entry.setEntrytype(2);
													
													if(row.getCell(6)!=null)
													{
													entry.setShipping_bill_no(shipingBillNofromExcel);
													}

													if(row.getCell(7)!=null)
													{
													if (row.getCell(7).getStringCellValue().trim().equalsIgnoreCase("WPAY")) {
														entry.setExport_type(1);
													} else if (row.getCell(7).getStringCellValue().trim().equalsIgnoreCase("WOPAY")) {
														entry.setExport_type(2);
													}
													}
													
													if(row.getCell(8)!=null)
													{
														 try {
															 entry.setShipping_bill_date(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(8).getDateCellValue().toString())));
														   }
														   catch (Exception e) {
														    e.printStackTrace();
														    }
													
													}
													
													if(row.getCell(9)!=null)
													{
													entry.setPort_code(portCodefromExcel);
													}	
												}
												
												
												entry.setExcel_voucher_no(vocherNofromExcel);
												if(row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Cash Sales") || row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Card Sales"))
												{
													cnt=0;
													try {
														
														for (Product product : productlist) {
															
															if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals(""))
															{
															String str = product.getHsn_san_no().trim();
															String strproductName = product.getProduct_name().trim();
															if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
																entity.setProduct_name(product.getProduct_name());
																entity.setProduct_id(product.getProduct_id());
																product1=product;
																entity.setIs_gst(product.getTax_type());
																count = count + 1;
																cnt=1;
																break;

															}
															}else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
															{
																
																String strproductName = product.getProduct_name().trim();
																if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
																entity.setProduct_name(product.getProduct_name());
																entity.setProduct_id(product.getProduct_id());
																product1=product;
																entity.setIs_gst(product.getTax_type());
																count = count + 1;
																cnt=1;
																break;
															}
															}
														
														}
														
														if(cnt==0){Err=true;ErrorMsgs.append(" Product name or Hsn code incorrect");}

													}
												   catch (Exception e) {
																e.printStackTrace();
															}
												}
												else
												{
													cnt=0;
														for(Customer customer:list)
														{
															Boolean flag1 = false;												
																	if((customer1!=null) && (customer1.getCustomer_id().equals(customer.getCustomer_id())))
																	{
																try {
																	for (Product product : customerService.findOneWithAll(customer.getCustomer_id()).getProduct()) {
																		
																		if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals(""))
																		{
																			String str = product.getHsn_san_no().trim();
																			String strproductName = product.getProduct_name().trim();
																			if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
																			entity.setProduct_name(product.getProduct_name());
																			entity.setProduct_id(product.getProduct_id());
																			flag1=true;
																			count = count + 1;
																			cnt=1;
																			product1=product;
																			entity.setIs_gst(product.getTax_type());
																			break;
					
																		}
																	    }
																		else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
																		{
																			
																			String strproductName = product.getProduct_name().trim();
																			if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
																			entity.setProduct_name(product.getProduct_name());
																			entity.setProduct_id(product.getProduct_id());
																			flag1=true;
																			product1=product;
																			entity.setIs_gst(product.getTax_type());
																			count = count + 1;
																			cnt=1;
																			break;
																		}
																		}
																	}
																	
																	if(flag1==true)
																	{
																		break;
																	}
					
																}
																catch (Exception e) {
																	e.printStackTrace();
																}
																}										
														}
														if(cnt==0){Err=true;ErrorMsgs.append("Product name or Hsn code incorrect");}
												}
												
												if(row.getCell(13)!=null)
												{
												entity.setQuantity(round((Double) row.getCell(13).getNumericCellValue(),2));
												}else
												{
													entity.setQuantity((double)0);
												}
												
												if(row.getCell(14)!=null)
												{
													entity.setRate(round((Double) row.getCell(14).getNumericCellValue(),2));
												}else
												{
													entity.setRate((double)0);
												}
												
												if(row.getCell(15)!=null)
												{
													entity.setDiscount(round((Double) row.getCell(15).getNumericCellValue(),2));
												}else
												{
													entity.setDiscount((double)0);
												}
												

												if(row.getCell(16)!=null)
												{
													entity.setCGST(round((Double) row.getCell(16).getNumericCellValue(),2));
												}else
												{
													entity.setCGST((double)0);
												}

												if(row.getCell(17)!=null)
												{
													entity.setIGST(round((Double) row.getCell(17).getNumericCellValue(),2));
												}else
												{
													entity.setIGST((double)0);
												}

												if(row.getCell(18)!=null)
												{
													entity.setSGST(round((Double) row.getCell(18).getNumericCellValue(),2));
												}else
												{
													entity.setSGST((double)0);
												}

												if(row.getCell(19)!=null)
												{
													entity.setState_com_tax(round((Double) row.getCell(19).getNumericCellValue(),2));

												}else
												{
													entity.setState_com_tax((double)0);
												}

												if(row.getCell(20)!=null)
												{
													entity.setLabour_charges(round((Double) row.getCell(20).getNumericCellValue(),2));

												}else
												{
													entity.setLabour_charges((double)0);
												}
												if(row.getCell(21)!=null)
												{
													entity.setFreight(round((Double) row.getCell(21).getNumericCellValue(),2));

												}else
												{
													entity.setFreight((double)0);
												}
												
												if(row.getCell(22)!=null)
												{
													entity.setOthers(round((Double) row.getCell(22).getNumericCellValue(),2));

												}else
												{
													entity.setOthers((double)0);
												}
												
												if(row.getCell(23)!=null)
												{
													entity.setUOM(row.getCell(23).getStringCellValue().trim());

												}else
												{
													entity.setUOM("NA");
												}
																							
					                            entity.setHSNCode(finalhsncode);

					                            if(row.getCell(24)!=null)
												{
													entity.setVAT(round((Double) row.getCell(24).getNumericCellValue(),2));


												}else
												{
													entity.setVAT((double)0);
												}
												
												if(row.getCell(25)!=null)
												{
													entity.setVATCST(round((Double) row.getCell(25).getNumericCellValue(),2));


												}else
												{
													entity.setVATCST((double)0);
												}
												
												if(row.getCell(26)!=null)
												{
													entity.setExcise(round((Double) row.getCell(26).getNumericCellValue(),2));


												}else
												{
													entity.setExcise((double)0);
												}
												
												
													cgst=entity.getCGST();
													igst=entity.getIGST();
													sgst=entity.getSGST();
													state_comp_tax= entity.getState_com_tax();
												    vat=entity.getVAT();
													vatcst=entity.getVATCST();
													excise=entity.getExcise();
												
												    Double tamount=(double)0;
													Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFreight()+entity.getOthers());
													Double damount = (entity.getRate()*entity.getQuantity());
													
													if(entity.getDiscount()!=0)
													{
														Double disamount =((round(damount,2))-(entity.getDiscount()));
														tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFreight()+entity.getOthers();
													}
													else
													{
														tamount = amount;
													}
													
												transaction_value=round(tamount,2);
												round_off=cgst+igst+sgst+state_comp_tax+vat+vatcst+excise+transaction_value ;
												if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
												{
													if(customer1.getTds_rate()!=null)
													{
														Tds = (transaction_value*customer1.getTds_rate())/100;
													}
												
												}
												round_off = round_off-Tds;
												entity.setTransaction_amount(round(transaction_value,2));
												entry.setRound_off(round(round_off,2));
												entry.setTransaction_value(round(transaction_value,2));
												entry.setCgst(round(cgst,2));
												entry.setSgst(round(sgst,2));
												entry.setIgst(round(igst,2));
												entry.setState_compansation_tax(round(state_comp_tax,2));
												entry.setTotal_vat(round(vat,2));
												entry.setTotal_vatcst(round(vatcst,2));
												entry.setTotal_excise(round(excise,2));
												entry.setTds_amount(round(Tds,2));
												
												if((product1!=null && count.equals(4) && customer1 !=null && entry.getVoucher_no()!=null)||(product1!=null && count.equals(4) && cashSalesCardSalesFlag ==true && entry.getVoucher_no()!=null))
												{	
														entry.setFlag(true);
														sucount=sucount+1;
														successVocharList.add(vocherNofromExcel);
														repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
													
													 //stock
										    		 if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
													{
												     Integer pflag=productService.checktype(company_id,product1.getProduct_id());
												     if(pflag==1)  
												     {
												      Stock stock = null;
												      stock=stockService.isstockexist(company_id,product1.getProduct_id());
														if(stock!=null)
														{
															double amount1 = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(), entity.getQuantity());
															stock.setAmount(stock.getAmount()-amount1);
															stock.setQuantity(stock.getQuantity()-entity.getQuantity());					
															stockService.updateStock(stock);
														}
														
												     }
													}
												     
												     
												    entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
											        Long id1 = entryService.saveSalesEntryThroughExcel(entry);													
													entity.setSales_id(id1);
												    entryService.saveSalesEntryProductEntityClassThroughExcel(entity);
											    
												    SalesEntry sales = entryService.getById(id1);
											    
											    if(entry.getSale_type()==1)
											    {
											    	SubLedger subledgercgst = subLedgerDAO.findOne("Cash In Hand", company_id);
											    	if(subledgercgst!=null)
											    	{
											    	subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id, subledgercgst.getSubledger_Id(), (long) 2,(double)0,(double)round(round_off,2), (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
											    	}
											    }
											    else if(entry.getSale_type()==2)
											    {
											    	if(entry.getBank()!=null)
											    	{
											    	bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getCreated_date(),company_id, entry.getBank().getBank_id(), (long) 3, (double)0,(double)round(round_off,2),(long) 1);
											    	}
											    }
											    else
											    {
														if(customer1!=null)
														{
														try {
															customerService.addcustomertransactionbalance(year_id,entry.getCreated_date(),company_id,
																	customer1.getCustomer_id(), (long) 5, (double)0, (double)round(round_off,2), (long) 1);
														} catch (Exception e) {
															
															e.printStackTrace();
														}
														}
														
														if (Tds>0) {
															SubLedger subledgerTds = subLedgerDAO.findOne("TDS Receivable", company_id);
															if (subledgerTds != null) {
																subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
																		subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)Tds, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
											    }
												
												if(subledger1!=null)
												{
													try {
														subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
																subledger1.getSubledger_Id(), (long) 2, (double)transaction_value, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
														
													} catch (Exception e) {
														
														e.printStackTrace();
													}
												}
													try
													{
														if(cgst>0)
														{
															SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST", company_id);
															if(subledgercgst!=null)
															{
															subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
																	subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														if(sgst>0)
														{
															SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST", company_id);
															if(subledgersgst!=null)
															{
															subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
																	subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														if(igst>0)
														{
															SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST", company_id);
															if(subledgerigst!=null)
															{
															subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
																	subledgerigst.getSubledger_Id(), (long) 2,(double)igst, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														if(state_comp_tax>0)
														{
															SubLedger subledgercess = subLedgerDAO.findOne("Output CESS", company_id);
															if(subledgercess!=null)
															{
															subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
																	subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax, (double)0,
																	(long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														if(vat>0)
														{
															SubLedger subledgervat = subLedgerDAO.findOne("Output VAT", company_id);
															if(subledgervat!=null)
															{
															subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
																	subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														if(vatcst>0)
														{
															SubLedger subledgercst = subLedgerDAO.findOne("Output CST", company_id);
															if(subledgercst!=null)
															{
															subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
																	subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														if(excise>0)
														{
															SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
																	company_id);
															
															if(subledgerexcise!=null)
															{
															subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
																	subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
															}
														}
														
														
													}
													catch(Exception e)
													{
														e.printStackTrace();
													}
													
												}
												else {
													
													maincount=maincount+1;
													failcount=failcount+1;
							                        failureVocharList.add(vocherNofromExcel);
							                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
													
												}
													
												if (maincount==0) {
													m = 1;
												} else {
													m = 2;
												}
												
											  }
												
											   }
											}	
												
												
											}
										if(Err==true){ErrorMsgs.append("----rowno ");ErrorMsgs.append("\n");}			
					                }
			    		     
			    		     
			    			}
			    			workbook.close(); 
			    	}
		        	
		        else if (excelfile.getOriginalFilename().endsWith("xlsx")) {
					int i = 0;
					XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
					XSSFSheet worksheet = workbook.getSheetAt(0);
		         	if(isValid){
						i = 1;
						
						while (i <= worksheet.getLastRowNum()) {
							Err=false;
							SalesEntry entry = new SalesEntry();
							SalesEntryProductEntityClass entity = new  SalesEntryProductEntityClass();
							
							Customer customer1 =null;
							SubLedger subledger1= null ;
							Product product1 = null;
							XSSFRow row = worksheet.getRow(i++);
							//ErrorMsgs.append(System.lineSeparator());
							if(row.getLastCellNum()==0)
							{				
						    	System.out.println("Invalid Data");
							}
							else
							{	
								Double Tds = (double)0;
								Double NewTds = (double)0;
								Double transaction_value = (double)0;
								Double cgst = (double)0;
								Double igst = (double)0 ;
								Double sgst = (double)0;
								Double state_comp_tax = (double)0;
								Double round_off = (double)0;
								Double vat = (double)0;
								Double vatcst = (double)0;
								Double excise = (double)0;
								
								Integer cnt=0;
							
								Integer count=0;
								boolean flag = false ;
								String vocherNofromExcel="";
								String shipingBillNofromExcel="";
								String portCodefromExcel="";
								String vocherRangefromExcel="";
								String ProductName = row.getCell(12).getStringCellValue();
								
								try
								{
									vocherNofromExcel=row.getCell(2).getStringCellValue().trim().replaceAll(" ", "");;
								}
								catch(Exception e)
								{
									//e.printStackTrace();
									//System.out.println("error is " +e.getMessage());
								}
								
								try
								{
									Double voucherNofromExcel= row.getCell(2).getNumericCellValue();
									Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
									vocherNofromExcel=voucherNofromExcel1.toString().trim();
									
								}
								catch(Exception e)
								{
									//e.printStackTrace();
								}
								
								try
								{
									shipingBillNofromExcel=row.getCell(6).getStringCellValue().trim();
								}
								catch(Exception e)
								{
									//e.printStackTrace();
								}
								
								try
								{
									Double shipingBillNfromExcel= row.getCell(6).getNumericCellValue();
									Integer shipingBillNfromExcel1 = shipingBillNfromExcel.intValue();
									shipingBillNofromExcel=shipingBillNfromExcel1.toString().trim();
									
								}
								catch(Exception e)
								{
								//	e.printStackTrace();
								}
								
								try
								{
									portCodefromExcel=row.getCell(9).getStringCellValue().trim();
								}
								catch(Exception e)
								{
									//e.printStackTrace();
								}
								
								try
								{
									Double portCodfromExcel= row.getCell(9).getNumericCellValue();
									Integer portCodfromExcel1 = portCodfromExcel.intValue();
									portCodefromExcel=portCodfromExcel1.toString().trim();
									
								}
								catch(Exception e)
								{
								//	e.printStackTrace();
								}
								
								
								try
								{
									vocherRangefromExcel=row.getCell(28).getStringCellValue().trim();
								}
								catch(Exception e)
								{
									//e.printStackTrace();
								}
								
								try
								{
									Double vocherrangefromExcel= row.getCell(28).getNumericCellValue();
									Integer vocherRangefromExcel1 = vocherrangefromExcel.intValue();
									vocherRangefromExcel=vocherRangefromExcel1.toString().trim();
									
								}
								catch(Exception e)
								{
							//		e.printStackTrace();
								}
								
								    SalesEntry entry1 = entryService.isExcelVocherNumberExist(vocherNofromExcel, company_id);
								
									if(entry1!=null)
									{
										
										String vocherNo = entry1.getExcel_voucher_no().trim();
										Long company_Id = entry1.getCompany().getCompany_id();
										Double hsncode= row.getCell(11).getNumericCellValue();
										Integer hsncode1 = hsncode.intValue();
										String finalhsncode=hsncode1.toString().trim();
										List<SalesEntryProductEntityClass> prolist = entryService.findAllSalesEntryProductEntityList(entry1.getSales_id());
										Boolean entryflag = false ;
										Boolean mainentryflag = false;
										for(SalesEntryProductEntityClass sEPC : prolist)
										{
											if(finalhsncode.replace(" ","").trim().equalsIgnoreCase(sEPC.getHSNCode().replace(" ","").trim()) && ProductName.replace(" ","").trim().equalsIgnoreCase(sEPC.getProduct_name().replace(" ","").trim()))
											{
												entryflag=true;
											}
										}
										
										if((company_Id.equals(company_id))&&(vocherNo.replace(" ","").trim().equalsIgnoreCase(vocherNofromExcel.replace(" ","").trim()))&&(entryflag==true))
										{
											mainentryflag=true;
										}
										
										if(mainentryflag==false)
										{
										flag = true;
										 
										SalesEntry oldentry = entryService.findOneWithAll(entry1.getSales_id());
										
										if((oldentry.getSale_type()==0))
										{											
												if(oldentry.getCustomer()!=null) 
												{
													customer1=oldentry.getCustomer();
												}
										}
										if(oldentry.getSubledger()!=null)
										{
											subledger1=oldentry.getSubledger();
										}
										
										transaction_value=oldentry.getTransaction_value();
										round_off=oldentry.getRound_off();
										cgst=oldentry.getCgst();
										igst=oldentry.getIgst();
										sgst= oldentry.getSgst();
										state_comp_tax=oldentry.getState_compansation_tax();
										vat= oldentry.getTotal_vat();
										vatcst=oldentry.getTotal_vatcst();
										excise=oldentry.getTotal_excise();
										if(oldentry.getTds_amount()!=null) 
										{
										Tds=oldentry.getTds_amount();
										}
										//credit card sale
										
							if((oldentry.getSale_type()==0))
							{
										for(Customer customer:list)
										{
											Boolean flag1 = false;
											if((customer1!=null) && (customer1.getCustomer_id().equals(customer.getCustomer_id())))
											{
										try {
											for (Product product : customerService.findOneWithAll(customer.getCustomer_id()).getProduct()) {
												
												if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals(""))
												{
												String str = product.getHsn_san_no();
												String strproductName = product.getProduct_name();
												if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
													entity.setProduct_name(product.getProduct_name());
													entity.setProduct_id(product.getProduct_id());
													product1=product;
													entity.setIs_gst(product.getTax_type());
													flag1=true;
													break;

												}
												}
												else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
												{
													
													String strproductName = product.getProduct_name().trim();
													if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
													entity.setProduct_name(product.getProduct_name());
													entity.setProduct_id(product.getProduct_id());
													flag1=true;
													product1=product;
													entity.setIs_gst(product.getTax_type());
													break;
												}
												}
												
											}
											if(flag1==true)
											{
												break;
											}
											}
												catch (Exception e) {
													e.printStackTrace();
												}
												
												}	
											
											
										}
							}
							else
							{
								try {
									
									for (Product product : productlist) {
										
										if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals(""))
										{
										String str = product.getHsn_san_no().trim();
										String strproductName = product.getProduct_name().trim();
										if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
											entity.setProduct_name(product.getProduct_name());
											entity.setProduct_id(product.getProduct_id());
											product1=product;
											entity.setIs_gst(product.getTax_type());
											break;

										}
										}
										else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
										{
											
											String strproductName = product.getProduct_name().trim();
											if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
											entity.setProduct_name(product.getProduct_name());
											entity.setProduct_id(product.getProduct_id());
											product1=product;
											entity.setIs_gst(product.getTax_type());
											break;
										}
										}
									}
								
								}
										catch (Exception e) {
											e.printStackTrace();
										}
							}
										
										if(row.getCell(13)!=null)
										{
										entity.setQuantity((Double) row.getCell(13).getNumericCellValue());
										}else
										{
											entity.setQuantity((double)0);
										}
										
										if(row.getCell(14)!=null)
										{
											entity.setRate(round((Double) row.getCell(14).getNumericCellValue(),2));
										}else
										{
											entity.setRate((double)0);
										}
										
										if(row.getCell(15)!=null)
										{
											entity.setDiscount(round((Double) row.getCell(15).getNumericCellValue(),2));
										}else
										{
											entity.setDiscount((double)0);
										}
										

										if(row.getCell(16)!=null)
										{
											entity.setCGST(round((Double) row.getCell(16).getNumericCellValue(),2));
										}else
										{
											entity.setCGST((double)0);
										}

										if(row.getCell(17)!=null)
										{
											entity.setIGST(round((Double) row.getCell(17).getNumericCellValue(),2));
										}else
										{
											entity.setIGST((double)0);
										}

										if(row.getCell(18)!=null)
										{
											entity.setSGST(round((Double) row.getCell(18).getNumericCellValue(),2));
										}else
										{
											entity.setSGST((double)0);
										}

										if(row.getCell(19)!=null)
										{
											entity.setState_com_tax(round((Double) row.getCell(19).getNumericCellValue(),2));

										}else
										{
											entity.setState_com_tax((double)0);
										}

										if(row.getCell(20)!=null)
										{
											entity.setLabour_charges(round((Double) row.getCell(20).getNumericCellValue(),2));

										}else
										{
											entity.setLabour_charges((double)0);
										}
										if(row.getCell(21)!=null)
										{
											entity.setFreight(round((Double) row.getCell(21).getNumericCellValue(),2));

										}else
										{
											entity.setFreight((double)0);
										}
										
										if(row.getCell(22)!=null)
										{
											entity.setOthers(round((Double) row.getCell(22).getNumericCellValue(),2));

										}else
										{
											entity.setOthers((double)0);
										}
										
										if(row.getCell(23)!=null)
										{
											entity.setUOM(row.getCell(23).getStringCellValue().trim());

										}else
										{
											entity.setUOM("NA");
										}
										
										entity.setHSNCode(finalhsncode);

										  if(row.getCell(24)!=null)
											{
												entity.setVAT(round((Double) row.getCell(24).getNumericCellValue(),2));


											}else
											{
												entity.setVAT((double)0);
											}
											
											if(row.getCell(25)!=null)
											{
												entity.setVATCST(round((Double) row.getCell(25).getNumericCellValue(),2));


											}else
											{
												entity.setVATCST((double)0);
											}
											
											if(row.getCell(26)!=null)
											{
												entity.setExcise(round((Double) row.getCell(26).getNumericCellValue(),2));


											}
											else
											{
												entity.setExcise((double)0);
											}
											
							
											cgst=cgst+entity.getCGST();
											igst=igst+entity.getIGST();
											sgst=sgst+entity.getSGST();
											state_comp_tax= state_comp_tax+entity.getState_com_tax();
											vat=vat+entity.getVAT();
											vatcst=vatcst+entity.getVATCST();
											excise=excise+entity.getExcise();
											
										    Double tamount=(double)0;
											Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFreight()+entity.getOthers());
											Double damount = (entity.getRate()*entity.getQuantity());
											
											
											if(entity.getDiscount()!=0)
											{
												Double disamount =((round(damount,2))-(entity.getDiscount()));
												tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFreight()+entity.getOthers();
											}
											else
											{
												tamount = amount;
											}
										transaction_value=transaction_value+round(tamount,2);
										round_off=round_off+entity.getCGST()+entity.getIGST()+entity.getSGST()+entity.getState_com_tax()+entity.getVAT()+entity.getVATCST()+entity.getExcise()+tamount ;
										
										
										
										if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
										{
											if(customer1.getTds_rate()!=null)
											{
												NewTds = (transaction_value*customer1.getTds_rate())/100;
											}
										
										}
										round_off=round_off+Tds-NewTds;
									
									if(product1!=null)
									{
	                            
									
									 try {
										 if(oldentry.getSale_type()==1)
										 {
											SubLedger subledgercgst = subLedgerDAO.findOne("Cash In Hand", company_id);
											if(subledgercgst!=null)
											{
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id, subledgercgst.getSubledger_Id(), (long) 2,(double)0,(double)oldentry.getRound_off(), (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id, subledgercgst.getSubledger_Id(), (long) 2,(double)0,(double)round(round_off,2), (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
											}
										 }
										 else if(oldentry.getSale_type()==2)
										 {
											 if(oldentry.getBank()!=null)
											 {
												bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id, oldentry.getBank().getBank_id(), (long) 3, (double)0,(double)oldentry.getRound_off(),(long) 0);
												bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id, oldentry.getBank().getBank_id(), (long) 3, (double)0,(double)round(round_off,2),(long) 1);
											 }
										 }
										 else
										 {
											 if(customer1!=null)
												{
											 customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
													 customer1.getCustomer_id(), (long) 5, (double)0,(double)oldentry.getRound_off(),
														(long) 0);												 
											 customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
													 customer1.getCustomer_id(), (long) 5, (double)0,(double)round(round_off,2),
														(long) 1); 
												}
											   if (Tds>0) {
													
												  
														SubLedger subledgerTds = subLedgerDAO.findOne("TDS Receivable", company_id);
														if (subledgerTds != null) {
															subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																	subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)Tds, (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
															subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																	subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)NewTds, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														}
												
													
												}
											 
										 }											 										 
										} catch (Exception e) {
											
											e.printStackTrace();
										}
									
									
									if(subledger1!=null)
									{
											try {
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
														subledger1.getSubledger_Id(), (long) 2, (double)oldentry.getTransaction_value(), (double)0,
														(long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
														subledger1.getSubledger_Id(), (long) 2, (double)transaction_value, (double)0,
														(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
												
											} catch (Exception e) {
												
												e.printStackTrace();
											}
									}
									
									
									
											try
											{
												if((oldentry.getCgst()!=null) && (oldentry.getCgst()>0) && (cgst>0) && (oldentry.getCgst()!=cgst))
												{
												
													SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST",
															company_id);
													if (subledgercgst != null) {
														
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgercgst.getSubledger_Id(), (long) 2, (double)oldentry.getCgst(), (double)0, (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getCgst()!=null)&&(cgst>0) && (oldentry.getCgst()==0))
												{
													SubLedger subledgercgst = subLedgerDAO.isExists("Output CGST", company_id);
													
													if (subledgercgst != null) {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double)0,
																(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if((oldentry.getSgst()!=null) && (oldentry.getSgst()>0) && (sgst>0) && (oldentry.getSgst()!=sgst))
												{
												
													SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
															company_id);
													
													if (subledgersgst != null) {
														
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgersgst.getSubledger_Id(), (long) 2, (double)oldentry.getSgst(), (double)0, (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														
													}
										
												}
												else if((oldentry.getSgst()!=null)&&(sgst>0)&&(oldentry.getSgst()==0))
												{
													SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
															company_id);
													if (subledgersgst != null) {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double)0,
																(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if((oldentry.getIgst()!=null) && (oldentry.getIgst()>0) && (igst>0) && (oldentry.getIgst()!=igst))
												{
												
													SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
															company_id);
													if (subledgerigst != null) {
														
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgerigst.getSubledger_Id(), (long) 2, (double)oldentry.getIgst(), (double)0, (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
											
												}
												}
												else if((oldentry.getIgst()!=null)&&(igst>0)&&(oldentry.getIgst()==0))
												{
													SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
															company_id);
													if(subledgerigst!=null)
													{
													subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
															subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if((oldentry.getState_compansation_tax()!=null) && (oldentry.getState_compansation_tax()>0) && (state_comp_tax>0)&&(oldentry.getState_compansation_tax()!=state_comp_tax))
												{
												
													SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
															company_id);
													
													if (subledgercess != null) {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgercess.getSubledger_Id(), (long) 2, (double)oldentry.getState_compansation_tax(),
																(double)0, (long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax,
																(double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getState_compansation_tax()!=null) && (state_comp_tax>0) && (oldentry.getState_compansation_tax()==0))
												{
													SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
															company_id);
													
													if (subledgercess != null) {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax,
																(double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
													
												
												if((oldentry.getTotal_vat()!=null) && (oldentry.getTotal_vat()>0) && (vat>0) && (oldentry.getTotal_vat()!=vat))
												{
												
													SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
															company_id);
													
													if (subledgervat != null) {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgervat.getSubledger_Id(), (long) 2, (double)oldentry.getTotal_vat(), (double)0,
																(long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double)0,
																(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getTotal_vat()!=null) && (vat>0) && (oldentry.getTotal_vat()==0))
												{
													SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
															company_id);
													
													if (subledgervat != null) {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double)0,
																(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if((oldentry.getTotal_vatcst()!=null) && (oldentry.getTotal_vatcst()>0) && (vatcst>0) && (oldentry.getTotal_vatcst()!=vatcst)) 
												{
													SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
															company_id);
													if (subledgercst != null) {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgercst.getSubledger_Id(), (long) 2, (double)oldentry.getTotal_vatcst(), (double)0,
																(long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double)0,
																(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getTotal_vatcst()!=null) && (vatcst>0) && (oldentry.getTotal_vatcst()==0))
												{
													SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
															company_id);
													if (subledgercst != null) {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double)0,
																(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
													
												if((oldentry.getTotal_excise()!=null) && (oldentry.getTotal_excise()>0) && (excise>0) && (oldentry.getTotal_excise()!=excise))
												{
													SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
															company_id);
													if (subledgerexcise != null) {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgerexcise.getSubledger_Id(), (long) 2, (double)oldentry.getTotal_excise(), (double)0,
																(long) 0,oldentry,null,null,null,null,null,null,null,null,null,null,null);
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double)0,
																(long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getTotal_excise()!=null) && excise>0 && (oldentry.getTotal_excise()==0))
												{
													SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
															company_id);
													if (subledgerexcise != null) {
														subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getCreated_date(),company_id,
																subledgerexcise.getSubledger_Id(), (long) 2, (double)excise,
																(double)0, (long) 1,oldentry,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												
											}
												
											catch(Exception e)
											{
												e.printStackTrace();
											}
											
											
											entity.setTransaction_amount(round(tamount,2));
											oldentry.setRound_off(round(round_off,2));
											oldentry.setTransaction_value(round(transaction_value,2));
											oldentry.setTds_amount(round(NewTds,2));
											oldentry.setCgst(round(cgst,2));
											oldentry.setIgst(round(igst,2));
											oldentry.setSgst(round(sgst,2));
											oldentry.setState_compansation_tax(round(state_comp_tax,2));
											oldentry.setTotal_vat(round(vat,2));
											oldentry.setTotal_vatcst(round(vatcst,2));
											oldentry.setTotal_excise(round(excise,2));
										
										    entryService.updateSalesEntryThroughExcel(oldentry);
											entity.setSales_id(oldentry.getSales_id());
											entryService.updateSalesEntryProductEntityClassThroughExcel(entity);
											
											
											 //stock
									    		 if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
												{
											     Integer pflag=productService.checktype(company_id,product1.getProduct_id());
											     if(pflag==1)  
											     {
											      Stock stock = null;
											      stock=stockService.isstockexist(company_id,product1.getProduct_id());
													if(stock!=null)
													{
														double amount1 = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(), entity.getQuantity());
														stock.setAmount(stock.getAmount()-amount1);
														stock.setQuantity(stock.getQuantity()-entity.getQuantity());					
														stockService.updateStock(stock);
													}
													
											     }
												}
											  			
	                                         if(repetProductafterunsuccesfullattempt==0)
	                                         {
	                                        	 
	                                        	 sucount=sucount+1;
	                                        	 m = 1;
	                                        	successVocharList.add(vocherNofromExcel);
	                                         }
											
										}
									else
									{
										
										/*entryService.ChangeStatusofSalesEntryThroughExcel(oldentry);
										if(successVocharList!=null && successVocharList.size()>0)
										{
											sucount=sucount-1;
											failcount=failcount+1;
											m = 2;
											successVocharList.remove(vocherNofromExcel);
											failureVocharList.add(vocherNofromExcel);
										}*/
										
											
										
									}	
										/*break;*/
										}
										
										/* if(mainentryflag==true)
										  {	
											  break;
										  }*/
									}
								
								
								
								
									if(flag== false)
									{
										
										if(!failureVocharList.contains(vocherNofromExcel))
										{
										
										 Boolean flagtocheckentry = false;
										 Boolean cashSalesCardSalesFlag =  false;
										 
										 
										 Double hsncode= row.getCell(11).getNumericCellValue();
										 Integer hsncode1 = hsncode.intValue();
										 String finalhsncode=hsncode1.toString().trim();
										 
											String remoteAddr = "";
											remoteAddr = request.getHeader("X-FORWARDED-FOR");
											if (remoteAddr == null || "".equals(remoteAddr)) {
												remoteAddr = request.getRemoteAddr();
											}
											entry.setIp_address(remoteAddr);
											entry.setCreated_by(user_id);
										 
											SalesEntry oldentry = entryService.isExcelVocherNumberExist(vocherNofromExcel, company_id);
										
											if(oldentry !=null && oldentry.getExcel_voucher_no()!=null)
											{
											 String str=oldentry.getExcel_voucher_no().trim();
												if(str.replace(" ","").trim().equalsIgnoreCase(vocherNofromExcel.replace(" ","").trim())) {
													flagtocheckentry=true;
												   /* break;*/
												}
											}
										
										if(flagtocheckentry==false)
										{
											entry.setCompany(user.getCompany());
											
									
											if(row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Cash Sales"))
											{
												entry.setSale_type(1);
												count = count + 1;
												
												cashSalesCardSalesFlag = true;
												entry.setCustomer(null);
											}
									       	else if(row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Card Sales") && row.getCell(27)!=null)
												{
									       		    cnt=count;
									       		    entry.setSale_type(2);
													entry.setCustomer(null);
													cashSalesCardSalesFlag = true;
													String[] banknameAndAcc = row.getCell(27).getStringCellValue().replace(" ","").trim().split("-");
													String bankname = banknameAndAcc[0];
													String accno = banknameAndAcc[1];
													
													for (Bank bank : banklist) 
													{																
												
															String strbankname =bank.getBank_name().trim();
															String straccno = bank.getAccount_no().toString();
															
															if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno)) {
																entry.setBank(bank);
																entry.setBankId(bank.getBank_id());																		
																count = count + 1;
																
																break;	
															}
													
													}
													if(cnt==count){Err=true;ErrorMsgs.append("Bank name is incorrect");}
												}
										   	else
												{
										   		cnt=count;
										   		    entry.setSale_type(0);
													try {			
														for(Customer customer:list)
														{
															
															String str=customer.getFirm_name().trim();
															if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(0).getStringCellValue().replace(" ","").trim())) {
																entry.setCustomer(customer);
																customer1=customer;
																count=count+1;
																
																entry.setSale_type(0);
															    break;
															}
														}
													} catch (Exception e) {
														e.printStackTrace();
													}
													if(cnt==count){System.out.println("row of err cust "+row.getRowNum());Err=true;ErrorMsgs.append("Customer name is incorrect");}
												}
											
											
										
											
											
										if(row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Cash Sales") || row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Card Sales"))
										{
											cnt=0;
												for(SubLedger subledger:sublist)
												{
													
													String str =subledger.getSubledger_name().trim();
													if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(3).getStringCellValue().replace(" ","").trim())) {
														entry.setSubledger(subledger);
														subledger1=subledger;
														count=count+1;
														cnt=1;
													    break;
												}
												
										}
												if(cnt==0){System.out.println("row of err subledg1 "+row.getRowNum());Err=true;ErrorMsgs.append("Subledger name is incorrect ");}
										}
										else
										{
											cnt=0;
												try {
													for(Customer customer:list)
													{
														Boolean flag1 = false;
														
														if((customer1!=null) && (customer1.getCustomer_id().equals(customer.getCustomer_id())))
													  {
														for(SubLedger subledger:customerService.findOneWithAll(customer.getCustomer_id()).getSubLedgers())
														{
															
															String str =subledger.getSubledger_name().trim();
															if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(3).getStringCellValue().replace(" ","").trim())) {
																entry.setSubledger(subledger);
																subledger1=subledger;
																count=count+1;
																cnt=1;
																System.out.println("getting subled");
																flag1=true;
															    break;
															
														    }
															
														}
														if(flag1==true)
														{
															break;
														}													
													}												
													}												
												}
													catch (Exception e) {
														e.printStackTrace();
													}
												if(cnt==0 && customer1!=null){System.out.println("row of err subledg2"+row.getRowNum());Err=true;ErrorMsgs.append("Subledger name is incorrect");}
										}
										
										
										if(row.getCell(1)!=null)
										{
											try {
												entry.setCreated_date(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(1).getDateCellValue().toString())));
											   }
											   catch (Exception e) {
											    e.printStackTrace();
											    }
											
											if(entry.getCreated_date()==null){
												Err=true;ErrorMsgs.append("Date format is not correct. Enter date in dd/mm/yyyy format in Invoice Date Column");
												}
										
										}
										
										try {
											cnt=0;
											for(AccountingYear year_range: acclist)
											{
												
												String str =year_range.getYear_range().trim();
												if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(10).getStringCellValue().replace(" ","").trim())) {
													entry.setAccountingYear(year_range);
													count=count+1;
													cnt=1;
													if(entry.getCreated_date()!=null)
													{
														for(String range : rangelist)
														{
															if(range.replace(" ","").trim().equalsIgnoreCase(vocherRangefromExcel.replace(" ","").trim()))
															{
													     entry.setVoucher_no(commonService.getVoucherNumber(range, VOUCHER_SALES_ENTRY, entry.getCreated_date(),year_range.getYear_id(), company_id));
													          break;
														    }
														}
													}
													
												
												    break;
												}
											}
											if(cnt==0){Err=true;ErrorMsgs.append("Voucher range is incorrect");}
										} catch (Exception e) {
											e.printStackTrace();
										}
										
										if(row.getCell(4)!=null)
										{
										entry.setRemark(row.getCell(4).getStringCellValue().trim());
										}
										entry.setLocal_time(new LocalTime());
										entry.setAgainst_advance_receipt(false);
										
										if(row.getCell(5).getStringCellValue().trim().equalsIgnoreCase("Local"))
										{
											entry.setEntrytype(1);
											
										}
										else if(row.getCell(5).getStringCellValue().trim().equalsIgnoreCase("Export"))
										{
											
											entry.setEntrytype(2);
											
											if(row.getCell(6)!=null)
											{
											entry.setShipping_bill_no(shipingBillNofromExcel);
											}

											if(row.getCell(7)!=null)
											{
											if (row.getCell(7).getStringCellValue().trim().equalsIgnoreCase("WPAY")) {
												entry.setExport_type(1);
											} else if (row.getCell(7).getStringCellValue().trim().equalsIgnoreCase("WOPAY")) {
												entry.setExport_type(2);
											}
											}
											
											if(row.getCell(8)!=null)
											{
												 try {
													 entry.setShipping_bill_date(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(8).getDateCellValue().toString())));
												   }
												   catch (Exception e) {
												    e.printStackTrace();
												    }
											
											}
											
											if(row.getCell(9)!=null)
											{
											entry.setPort_code(portCodefromExcel);
											}	
										}
										
										
										entry.setExcel_voucher_no(vocherNofromExcel);
										
										if(row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Cash Sales") || row.getCell(0).getStringCellValue().trim().equalsIgnoreCase("Card Sales"))
										{
											cnt=0;
										//	System.out.println("Cash Sales");
											try {
												
												for (Product product : productlist) {
													
													if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals(""))
													{
													String str = product.getHsn_san_no().trim();
													String strproductName = product.getProduct_name().trim();
													if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
														entity.setProduct_name(product.getProduct_name());
														entity.setProduct_id(product.getProduct_id());
														product1=product;
														entity.setIs_gst(product.getTax_type());
														count = count + 1;
														cnt=1;
														break;

													}
													}else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
													{
														
														String strproductName = product.getProduct_name().trim();
														if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
														entity.setProduct_name(product.getProduct_name());
														entity.setProduct_id(product.getProduct_id());
														product1=product;
														entity.setIs_gst(product.getTax_type());
														count = count + 1;
														cnt=1;
														break;
													}
													}
												
												}
												
												if(cnt==0){System.out.println("row error is product1"+row.getRowNum());Err=true;ErrorMsgs.append("Product name is incorrect");}

											}
										   catch (Exception e) {
														e.printStackTrace();
													}
										}
										else
										{
										//	System.out.println("No Cash");
											int prodctr=0;
											cnt=0;
												for(Customer customer:list)
												{
													
													Boolean flag1 = false;												
															if((customer1!=null) && (customer1.getCustomer_id().equals(customer.getCustomer_id())))
															{
																
														try {
															
															for (Product product : customerService.findOneWithAll(customer.getCustomer_id()).getProduct()) {
																
																if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals(""))
																{
																	
																	String str = product.getHsn_san_no().trim();
																	String strproductName = product.getProduct_name().trim();
																	
																	if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
																		
																		entity.setProduct_name(product.getProduct_name());
																	entity.setProduct_id(product.getProduct_id());
																	flag1=true;
																	count = count + 1;
																	cnt=1;
																	prodctr=prodctr+1;
																	
																	product1=product;
																	entity.setIs_gst(product.getTax_type());
																	break;
			
																}
															    }
																else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
																{
																	
																	String strproductName = product.getProduct_name().trim();
																	if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
																	entity.setProduct_name(product.getProduct_name());
																	
																	entity.setProduct_id(product.getProduct_id());
																	flag1=true;
																	product1=product;
																	entity.setIs_gst(product.getTax_type());
																	count = count + 1;
																cnt=1;
																	prodctr=prodctr+1;
																
																	break;
																}
																}
															}
															
															if(flag1==true)
															{
																break;
															}
			
														}
														catch (Exception e) {
															e.printStackTrace();
														}
														}										
												}
												if(cnt==0 && customer1!=null){System.out.println("row error is product2"+row.getRowNum());Err=true;ErrorMsgs.append("Product name is incorrect");}
										}
										
										if(row.getCell(13)!=null)
										{
										entity.setQuantity(round((Double) row.getCell(13).getNumericCellValue(),2));
										}else
										{
											entity.setQuantity((double)0);
										}
										
										if(row.getCell(14)!=null)
										{
											entity.setRate(round((Double) row.getCell(14).getNumericCellValue(),2));
										}else
										{
											entity.setRate((double)0);
										}
										
										if(row.getCell(15)!=null)
										{
											entity.setDiscount(round((Double) row.getCell(15).getNumericCellValue(),2));
										}else
										{
											entity.setDiscount((double)0);
										}
										

										if(row.getCell(16)!=null)
										{
											entity.setCGST(round((Double) row.getCell(16).getNumericCellValue(),2));
										}else
										{
											entity.setCGST((double)0);
										}

										if(row.getCell(17)!=null)
										{
											entity.setIGST(round((Double) row.getCell(17).getNumericCellValue(),2));
										}else
										{
											entity.setIGST((double)0);
										}

										if(row.getCell(18)!=null)
										{
											entity.setSGST(round((Double) row.getCell(18).getNumericCellValue(),2));
										}else
										{
											entity.setSGST((double)0);
										}

										if(row.getCell(19)!=null)
										{
											entity.setState_com_tax(round((Double) row.getCell(19).getNumericCellValue(),2));

										}else
										{
											entity.setState_com_tax((double)0);
										}

										if(row.getCell(20)!=null)
										{
											entity.setLabour_charges(round((Double) row.getCell(20).getNumericCellValue(),2));

										}else
										{
											entity.setLabour_charges((double)0);
										}
										if(row.getCell(21)!=null)
										{
											entity.setFreight(round((Double) row.getCell(21).getNumericCellValue(),2));

										}else
										{
											entity.setFreight((double)0);
										}
										
										if(row.getCell(22)!=null)
										{
											entity.setOthers(round((Double) row.getCell(22).getNumericCellValue(),2));

										}else
										{
											entity.setOthers((double)0);
										}
										
										if(row.getCell(23)!=null)
										{
											entity.setUOM(row.getCell(23).getStringCellValue().trim());

										}else
										{
											entity.setUOM("NA");
										}
																					
			                            entity.setHSNCode(finalhsncode);

			                            if(row.getCell(24)!=null)
										{
											entity.setVAT(round((Double) row.getCell(24).getNumericCellValue(),2));


										}else
										{
											entity.setVAT((double)0);
										}
										
										if(row.getCell(25)!=null)
										{
											entity.setVATCST(round((Double) row.getCell(25).getNumericCellValue(),2));


										}else
										{
											entity.setVATCST((double)0);
										}
										
										if(row.getCell(26)!=null)
										{
											entity.setExcise(round((Double) row.getCell(26).getNumericCellValue(),2));


										}else
										{
											entity.setExcise((double)0);
										}
										
										
											cgst=entity.getCGST();
											igst=entity.getIGST();
											sgst=entity.getSGST();
											state_comp_tax= entity.getState_com_tax();
										    vat=entity.getVAT();
											vatcst=entity.getVATCST();
											excise=entity.getExcise();
										
										    Double tamount=(double)0;
											Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFreight()+entity.getOthers());
											Double damount = (entity.getRate()*entity.getQuantity());
											
											if(entity.getDiscount()!=0)
											{
												Double disamount =((round(damount,2))-(entity.getDiscount()));
												tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFreight()+entity.getOthers();
											}
											else
											{
												tamount = amount;
											}
											
										transaction_value=round(tamount,2);
										round_off=cgst+igst+sgst+state_comp_tax+vat+vatcst+excise+transaction_value ;
										if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
										{
											if(customer1.getTds_rate()!=null)
											{
												Tds = (transaction_value*customer1.getTds_rate())/100;
											}
										
										}
										round_off = round_off-Tds;
										entity.setTransaction_amount(round(transaction_value,2));
										entry.setRound_off(round(round_off,2));
										entry.setTransaction_value(round(transaction_value,2));
										entry.setCgst(round(cgst,2));
										entry.setSgst(round(sgst,2));
										entry.setIgst(round(igst,2));
										entry.setState_compansation_tax(round(state_comp_tax,2));
										entry.setTotal_vat(round(vat,2));
										entry.setTotal_vatcst(round(vatcst,2));
										entry.setTotal_excise(round(excise,2));
										entry.setTds_amount(round(Tds,2));
										
										
										
										
System.out.println("product1 is "+product1);
System.out.println("count is "+count);
System.out.println("customer1 is "+customer1);
System.out.println("entry.getVoucher_no() is "+entry.getVoucher_no());
System.out.println("cashSalesCardSalesFlag is "+cashSalesCardSalesFlag);
if(product1==null){
	Err=true;ErrorMsgs.append("Product name is incorrect");
}
if(customer1==null){
	if(cashSalesCardSalesFlag==false){
		Err=true;ErrorMsgs.append("Set Cash or Card sales is incorrect ");
	}
	if(entry.getVoucher_no()==null){
		Err=true;ErrorMsgs.append("Sales Voucher no is incorrect");
	}
	
}else{
	if(entry.getVoucher_no()==null){
		Err=true;ErrorMsgs.append("Sales Voucher no is incorrect");
	}
}


										
										if((product1!=null && count.equals(4) && customer1 !=null && entry.getVoucher_no()!=null)||(product1!=null && count.equals(4) && cashSalesCardSalesFlag ==true && entry.getVoucher_no()!=null))
										{	
												entry.setFlag(true);
												sucount=sucount+1;
												successVocharList.add(vocherNofromExcel);
												repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											
											 //stock
								    		 if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
											{
										     Integer pflag=productService.checktype(company_id,product1.getProduct_id());
										     if(pflag==1)  
										     {
										      Stock stock = null;
										      stock=stockService.isstockexist(company_id,product1.getProduct_id());
												if(stock!=null)
												{
													double amount1 = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(), entity.getQuantity());
													stock.setAmount(stock.getAmount()-amount1);
													stock.setQuantity(stock.getQuantity()-entity.getQuantity());					
													stockService.updateStock(stock);
												}
												
										     }
											}
										     
										     
										    entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
									        Long id1 = entryService.saveSalesEntryThroughExcel(entry);
									       
											entity.setSales_id(id1);
											
										    entryService.saveSalesEntryProductEntityClassThroughExcel(entity);
									 
										    SalesEntry sales = entryService.getById(id1);
									    
									    if(entry.getSale_type()==1)
									    {
									    	SubLedger subledgercgst = subLedgerDAO.findOne("Cash In Hand", company_id);
									    	if(subledgercgst!=null)
									    	{
									    	subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id, subledgercgst.getSubledger_Id(), (long) 2,(double)0,(double)round(round_off,2), (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
									    	}
									    }
									    else if(entry.getSale_type()==2)
									    {
									    	if(entry.getBank()!=null)
									    	{
									    	bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getCreated_date(),company_id, entry.getBank().getBank_id(), (long) 3, (double)0,(double)round(round_off,2),(long) 1);
									    	}
									    }
									    else
									    {
												if(customer1!=null)
												{
												try {
													customerService.addcustomertransactionbalance(year_id,entry.getCreated_date(),company_id,
															customer1.getCustomer_id(), (long) 5, (double)0, (double)round(round_off,2), (long) 1);
												} catch (Exception e) {
													
													e.printStackTrace();
												}
												}
												
												if (Tds>0) {
													SubLedger subledgerTds = subLedgerDAO.findOne("TDS Receivable", company_id);
													if (subledgerTds != null) {
														subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
																subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)Tds, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
									    }
										
										if(subledger1!=null)
										{
											try {
												subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
														subledger1.getSubledger_Id(), (long) 2, (double)transaction_value, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
												
											} catch (Exception e) {
												
												e.printStackTrace();
											}
										}
											try
											{
												if(cgst>0)
												{
													SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST", company_id);
													if(subledgercgst!=null)
													{
													subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
															subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if(sgst>0)
												{
													SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST", company_id);
													if(subledgersgst!=null)
													{
													subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
															subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if(igst>0)
												{
													SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST", company_id);
													if(subledgerigst!=null)
													{
													subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
															subledgerigst.getSubledger_Id(), (long) 2,(double)igst, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if(state_comp_tax>0)
												{
													SubLedger subledgercess = subLedgerDAO.findOne("Output CESS", company_id);
													if(subledgercess!=null)
													{
													subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
															subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax, (double)0,
															(long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if(vat>0)
												{
													SubLedger subledgervat = subLedgerDAO.findOne("Output VAT", company_id);
													if(subledgervat!=null)
													{
													subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
															subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if(vatcst>0)
												{
													SubLedger subledgercst = subLedgerDAO.findOne("Output CST", company_id);
													if(subledgercst!=null)
													{
													subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
															subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if(excise>0)
												{
													SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
															company_id);
													
													if(subledgerexcise!=null)
													{
													subledgerService.addsubledgertransactionbalance(year_id,entry.getCreated_date(),company_id,
															subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double)0, (long) 1,sales,null,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												
											}
											catch(Exception e)
											{
												e.printStackTrace();
											}
											
										}
										else {
											
											maincount=maincount+1;
											failcount=failcount+1;
					                        failureVocharList.add(vocherNofromExcel);
					                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											
										}
											
										if (maincount==0) {
											m = 1;
										} else {
											m = 2;
										}
										
									  }
										
									   }
									}	
										
										
									}
								if(Err==true){String msg="----row no "+row.getRowNum();System.out.println("row is n"+row.getRowNum());ErrorMsgs.append(msg);ErrorMsgs.append("\n");}		
											
			                }
						System.out.println("Done reading excel");
						workbook.close();
					} 
					}else {
						System.out.println("no data");

					}
				
			} catch (Exception e) {
				e.printStackTrace();
				isValid = false;
			}

			if (isValid) {
				try {
					
					String filePath = request.getServletContext().getRealPath("resources");
					filePath += "/templates/ErrorFile.txt";
					
					System.out.println("File Path is "+filePath);
					File file = new File(   filePath); 
					 
					 FileWriter fw = new FileWriter(file.getAbsoluteFile());
				        BufferedWriter bw = new BufferedWriter(fw);
				        bw.write(ErrorMsgs.toString());
				        bw.close();
				        System.out.println("done");
							        
		        } catch ( Exception e ) {
		            e.printStackTrace();
		        }

	         if (m == 1) {
	        	
	        	 if(((Integer)failureVocharList.size())>0 && ((Integer)successVocharList.size())>0)
	        	 {
					successmsg.append(((Integer)successVocharList.size()).toString());
					successmsg.append(" Records imported successfully");
					successmsg.append(System.lineSeparator());
					failuremsg.append(((Integer)failureVocharList.size()).toString());
					failuremsg.append(" Records Not imported, Please check these records");
					failuremsg.append(System.lineSeparator());
	        	 }
	        	 
	        	 if(((Integer)failureVocharList.size())==0 && ((Integer)successVocharList.size())>0)
	        	 {
	        		    successmsg.append(((Integer)successVocharList.size()).toString());
						successmsg.append(" Records imported successfully");
						successmsg.append(System.lineSeparator());
						failuremsg.append("");
						failuremsg.append(System.lineSeparator());
	        	 }
	        	 
	        	 if(((Integer)failureVocharList.size())>0 && ((Integer)successVocharList.size())==0)
	        	 {
	        		successmsg.append("");
	        		successmsg.append(System.lineSeparator());
					failuremsg.append(((Integer)failureVocharList.size()).toString());
					failuremsg.append(" Records Not imported, Please check these records");
					failuremsg.append(System.lineSeparator());
					
	        	 }	        	 
	        	 if(((Integer)failureVocharList.size())==0 && ((Integer)successVocharList.size())==0)
	        	 {
	        		 successmsg.append("No Records Imported, Please Check Your Excel");
	        		 successmsg.append(System.lineSeparator());
	        		 failuremsg.append("");
	        		 failuremsg.append(System.lineSeparator());
	        	 } 
					String successmsg1=successmsg.toString();
					String failuremsg1=failuremsg.toString();
					session.setAttribute("filemsg", successmsg1);
					session.setAttribute("filemsg1", failuremsg1);
					return new ModelAndView("redirect:/salesEntryList");
				} else if (m == 2){
					
					if(((Integer)failureVocharList.size())>0 && ((Integer)successVocharList.size())>0)
		        	 {
						successmsg.append(((Integer)successVocharList.size()).toString());
						successmsg.append(" Records imported successfully");
						successmsg.append(System.lineSeparator());
						failuremsg.append(((Integer)failureVocharList.size()).toString());
						failuremsg.append(" Records Not imported, Please check these records");
						failuremsg.append(System.lineSeparator());
		        	 }
		        	 
		        	 if(((Integer)failureVocharList.size())==0 && ((Integer)successVocharList.size())>0)
		        	 {
		        		    successmsg.append(((Integer)successVocharList.size()).toString());
							successmsg.append(" Records imported successfully");
							successmsg.append(System.lineSeparator());
							failuremsg.append("");
							failuremsg.append(System.lineSeparator());
		        	 }
		        	 
		        	 if(((Integer)failureVocharList.size())>0 && ((Integer)successVocharList.size())==0)
		        	 {
		        		successmsg.append("");
		        		successmsg.append(System.lineSeparator());
						failuremsg.append(((Integer)failureVocharList.size()).toString());
						failuremsg.append(" Records Not imported, Please check these records");
						failuremsg.append(System.lineSeparator());
						
		        	 }
		        	 
		        	 
		        	 if(((Integer)failureVocharList.size())==0 && ((Integer)successVocharList.size())==0)
		        	 {
		        		 successmsg.append("No Records Imported, Please Check Your Excel");
		        		 successmsg.append(System.lineSeparator());
		        		 failuremsg.append("");
		        		 failuremsg.append(System.lineSeparator());
		        	 }
		        	 
					
					String successmsg1=successmsg.toString();
					String failuremsg1=failuremsg.toString();
					session.setAttribute("filemsg", successmsg1);
					session.setAttribute("filemsg1", failuremsg1);
					return new ModelAndView("redirect:/salesEntryList");
				}
				else
				{
					
					successmsg.append("You are inserting duplicate records, Please check excel file");
					String successmsg1=successmsg.toString();
					session.setAttribute("filemsg", successmsg1);
					return new ModelAndView("redirect:/salesEntryList");
				}
				
			} else {
				successmsg.append("Please enter required and valid data in columns");
				String successmsg1=successmsg.toString();
				session.setAttribute("filemsg", successmsg1);
				return new ModelAndView("redirect:/salesEntryList");

			}
		}	
		
		
		 public static Double round(Double d, int decimalPlace) {
		    	DecimalFormat df = new DecimalFormat("#");
				df.setMaximumFractionDigits(decimalPlace);
				return new Double(df.format(d));
		    }

@RequestMapping(value="getOpeningBalanceforsales", method=RequestMethod.GET)
		public @ResponseBody Double  getOpeningBalance(@RequestParam("customerid") Long customerid,HttpServletRequest request, HttpServletResponse response)
	{
		Company company = null;
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		Double debitbalance = 0.0;
		Double creditbalance = 0.0;
		Double total=0.0;
		Double amount=0.0;

		try {
			company = companyService.getCompanyWithCompanyStautarType(company_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalDate date = company.getOpeningbalance_date();

		OpeningBalances salesopening = openingbalances.isOpeningBalancePresentSales(date, company_id, customerid);
		if(salesopening.getCredit_balance()!=null && salesopening.getDebit_balance()!=null)
		{
		creditbalance = salesopening.getCredit_balance();
		debitbalance = salesopening.getDebit_balance();
		}
	
		
		if(creditbalance>0)
		{
		  List<SalesEntry> salesaginstopening=entryService.getAllOpeningBalanceAgainstSales(customerid, company_id);
		  
		  for(SalesEntry sales:salesaginstopening) {
		  
		  amount=amount+sales.getRound_off()+sales.getTds_amount();
		  }
		 
		/*
		 * List<Receipt> recceiptaginstopening=
		 * receiptService.getAllOpeningBalanceAgainstReceipt(customerid, company_id);
		 * 
		 * for(Receipt receipt:recceiptaginstopening) {
		 * amount=amount+receipt.getAmount(); }
		 */
		
				if (creditbalance > 0) {
					total=creditbalance;
				} else {
					total=debitbalance;
				}
		}
				total=total-amount;
				return total;
		
	}
@RequestMapping(value = "getTDSRateById", method = RequestMethod.GET )
public @ResponseBody Float getTDSRateById(@RequestParam("tdsTypeId") long tdsTypeId,
		HttpServletRequest request,
		HttpServletResponse response){
	
	
	Float tdsrate=deducteeService.getTDSRateByTdsType(tdsTypeId) ;
	System.out.println("rate is  "+tdsrate);
	return  tdsrate;
	
	
}	

}