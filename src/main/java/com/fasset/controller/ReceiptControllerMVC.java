package com.fasset.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.data.time.Year;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.EditProductValidatorForReceipt;
import com.fasset.controller.validators.ReceiptValidator;
import com.fasset.dao.interfaces.ISalesEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Customer;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.Product;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Service;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ReceiptForm;
import com.fasset.report.controller.ClassToConvertDateForExcell;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IGstTaxMasterService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IProductService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IYearEndingService;

@Controller
@SessionAttributes("user")
public class ReceiptControllerMVC extends MyAbstractController{
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private IAccountingYearService accountingYearService ;
	
	@Autowired
	private ReceiptValidator receiptValidator ;
	
	@Autowired
	private ICustomerService customerService ;
	
	@Autowired
	private ISalesEntryService SalesEntryService;
	
	@Autowired	
	private ICreditNoteService creditService;
	
	@Autowired
	private IReceiptService receiptService ;
	
	@Autowired
	private ISubLedgerService subLedgerService;
	
	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private IBankService bankService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private ISubLedgerDAO subledgerDAO ;
	
	@Autowired
	private IOpeningBalancesService openingbalances;	
	
	@Autowired
	private IGstTaxMasterService gstService;

	@Autowired
	private EditProductValidatorForReceipt editProValidator ;
	
	@Autowired	
	private IYearEndingService yearService;
	
	@Autowired	
	private ISalesEntryDAO salesEntryDao;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IQuotationService quoteservice;

	@Autowired
	private IClientSubscriptionMasterService subscribeservice;
	
	private Long receipt_id ;
	
    private Set<String> successVocharList = new HashSet<String>();
	
	private Set<String> failureVocharList = new HashSet<String>();
	
	Boolean flag = null; // for maintaining the user on receiptList.jsp after delete and view
	
    @RequestMapping(value = "receipt", method = RequestMethod.GET)
	public ModelAndView receipt(HttpServletRequest request, HttpServletResponse response) {
	
    	User user = new User();
 		HttpSession session = request.getSession(true);
 		user = (User)session.getAttribute("user");
 		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		long company_id=(long)session.getAttribute("company_id");
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		System.out.println("AccYear Receipt " +AccYear);
 		ModelAndView model = new ModelAndView();
 		if(AccYear==-1){
			yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		}else{
			yearList = accountingYearService.findAccountRangeOfCompany(strLong);
			
		}
 		//List<AccountingYear>  yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id); commented not to show account popup

 		if(yearList.size()!=0)
		{
	    Receipt receipt = new Receipt();
		model.addObject("voucherrange", companyService.getVoucherRange(company_id));
		model.addObject("yearList", yearList);
		model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
		model.addObject("productList",  productService.findAllProductsOfCompany(company_id));
		model.addObject("salesEntryList",  SalesEntryService.findAllSalesEntriesOfCompany(company_id,false));
		model.addObject("subledgerList", subLedgerService.findAllApprovedByCompanywithdefault(company_id));
		model.addObject("bankList", bankService.findAllBanksOfCompany(user.getCompany().getCompany_id()));
		model.addObject("stateId",user.getCompany().getState().getState_id());
		model.addObject("receipt", receipt);
		model.addObject("paidtds", 0);
		
		
		Long quote_id = subscribeservice.getquoteofcompany(company_id);
		String email = user.getCompany().getEmail_id();
		if (quote_id != 0) {
			if(quoteservice.findAllTransactionimportDetailsUser(email, quote_id))// for subscribed client if he registered his company with quotation and quotation contains master imports facility.)
			{
				List<SalesEntry> disabledSalesEntryList= SalesEntryService.findAllDisableSalesEntryOfCompanyAfterImport(user.getCompany().getCompany_id(), true);
				
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
					 SalesEntryService.deleteByIdValue(sales.getSales_id(),true);
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
		
		model.setViewName("/transactions/receipt");
		
		return model;
		}
		else
		{
			session.setAttribute("msg","Your account is closed");
			return new ModelAndView("redirect:/receiptList");
			
		}
		
	}
    
    
    @RequestMapping(value = "saveReceipt", method = RequestMethod.POST)
    public synchronized ModelAndView saveReceipt(@ModelAttribute("receipt")Receipt receipt, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		User user = new User();		
		Long save_id = receipt.getSave_id();	
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		user = (User)session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");
		long user_id=(long)session.getAttribute("user_id");
		String yearrange = user.getCompany().getYearRange();
		System.out.println("The save");
		receiptValidator.validate(receipt, result);
		ModelAndView model = new ModelAndView();
		
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		if(result.hasErrors()){

		/*String amount=new java.text.DecimalFormat("0").format(receipt.getAmount());
			receipt.setAmount(Double.valueOf(amount));
			
			
 System.out.println(receipt.getAmount());*/
			receipt.setRound_off((double)0);
		    receipt.setCgst((double)0);
		    receipt.setSgst((double)0);
		    receipt.setIgst((double)0);
		    receipt.setTransaction_value((double)0);
		    receipt.setState_compansation_tax((double)0);	    
		    if(AccYear==-1){
				yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
			}else{
				yearList = accountingYearService.findAccountRangeOfCompany(strLong);
			 
			}
			model.addObject("voucherrange", companyService.getVoucherRange(company_id));
		  //  model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
			model.addObject("yearList",yearList);
			model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
			model.addObject("productList", productService.findAllProductsOfCompany(company_id));
			model.addObject("salesEntryList", SalesEntryService.findAllSalesEntriesOfCompany(company_id,false));
			model.addObject("subledgerList", subLedgerService.findAllApprovedByCompanywithdefault(company_id));
			model.addObject("bankList", bankService.findAllBanksOfCompany(user.getCompany().getCompany_id()));
			model.addObject("stateId",user.getCompany().getState().getState_id());
			model.setViewName("/transactions/receipt");			
			return model;
		}
		else{
			String msg = "";
			Long id = null;
			try{
				if(receipt.getReceipt_id()!= null){
					long pid = receipt.getReceipt_id();
					if(pid > 0){
						receipt_id=pid;
						receipt.setCompany(user.getCompany());
						receipt.setUpdated_by(user_id);
						receiptService.update(receipt);
						receipt.setPayment_type(receipt.getPayment_type());
						msg ="Receipt Updated successfully";
					}
				}
				else{
					receipt.setCompany(user.getCompany());
					receipt.setVoucher_no(commonService.getVoucherNumber("RV", VOUCHER_RECEIPT, receipt.getDate(),receipt.getYear_id(), company_id));						
					receipt.setFlag(true);
					
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            receipt.setIp_address(remoteAddr);
					
					receipt.setCreated_by(user_id);
					id = receiptService.saveReceipt(receipt);
					receipt_id=id;
					msg="Receipt Saved Successfully" ;
				}
			}
			
			catch(Exception e){
				e.printStackTrace();
			}
			if(save_id==0){
				Receipt receipt1 = new Receipt();			
				try {
					receipt1= receiptService.findOneWithAll(receipt_id);
							
					if(receipt1.getCustomer() != null){
						receipt1.setCustomer_id(receipt1.getCustomer().getCustomer_id());
					}
					
					if(receipt1.getSales_bill_id()!=null){
						receipt1.setSalesEntryId(receipt1.getSales_bill_id().getSales_id());
					}
					
					if(receipt1.getSubLedger() != null){
						receipt1.setSubledgerId(receipt1.getSubLedger().getSubledger_Id());
					}
					
					if(receipt1.getBank() != null){
						receipt1.setBankId(receipt1.getBank().getBank_id());
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				 if(AccYear==-1){
						yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
					}else{
						yearList = accountingYearService.findAccountRangeOfCompany(strLong);
					 
					}
				model.addObject("voucherrange", companyService.getVoucherRange(company_id));
			   // model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
				model.addObject("yearList",yearList);
				model.addObject("customerproductList", receiptService.findAllReceiptProductEntityList(receipt_id));
				model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
				model.addObject("productList", productService.findAllProductsOfCompany(company_id));
				model.addObject("salesEntryList", SalesEntryService.findAllSalesEntriesOfCompany(company_id,false));
				model.addObject("subledgerList", subLedgerService.findAllApprovedByCompanywithdefault(company_id));
				model.addObject("receipt", receipt1);
				model.addObject("bankList",  bankService.findAllBanksOfCompany(company_id));
				model.addObject("stateId",user.getCompany().getState().getState_id());
				model.addObject("successMsg", msg);
				model.setViewName("/transactions/receipt");	
			}
			else{
				msg="Receipt Saved Successfully";
				session.setAttribute("msg", msg);
				return new ModelAndView("redirect:/receiptList");
				
			}
			return model;
		}
	}
    
    @RequestMapping(value = "receiptList", method = RequestMethod.GET)
	public ModelAndView receiptList(HttpServletRequest request, HttpServletResponse response) {
    	
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		User user = new User();
		user = (User) session.getAttribute("user");
		Company company=null;
		try {
			company = companyService.getById(user.getCompany().getCompany_id());
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flag=true;
		List<YearEnding>  yearEndlist = yearService.findAllYearEnding(company.getCompany_id());
		boolean importfail=false;
		boolean importflag = false;
		
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		
		if((String) session.getAttribute("errorMsg")!=null){
			model.addObject("errorMsg", (String) session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		
		if((String) session.getAttribute("filemsg")!=null) {
			model.addObject("filemsg", ((String) session.getAttribute("filemsg")));
			model.addObject("filemsg1", ((String) session.getAttribute("filemsg1")));
			model.addObject("successMsg", "NA");
			model.addObject("successVocharList", successVocharList);
			model.addObject("failureVocharList", failureVocharList);
			
			session.removeAttribute("filemsg");
			session.removeAttribute("filemsg1");
		}
		
		//code for year
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
			if(year.getYear_range().trim().equalsIgnoreCase(currentyear))
			{
				yearId=year.getYear_id();
			}
		}

		
		if(receiptService.findAllRecepitOfCompany(company_id,false).size()!=0)
		{
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
		

		if (quote_id != 0) {
			if(quoteservice.findAllTransactionimportDetailsUser(email, quote_id))// for subscribed client if he registered his company with quotation and quotation contains master imports facility.)
			{
				List<SalesEntry> disabledSalesEntryList= SalesEntryService.findAllDisableSalesEntryOfCompanyAfterImport(user.getCompany().getCompany_id(), true);
				
				for(SalesEntry sales : disabledSalesEntryList)
				{
					SalesEntryService.deleteByIdValue(sales.getSales_id(),true);
				List<Receipt> receiptList=receiptService.findallreceiptentryofsales(sales.getSales_id());
				 for(int i = 0;i<receiptList.size();i++){	
					 Receipt information= new Receipt();
						information = receiptList.get(i);		
						//receiptService.diactivateByIdValue(information.getReceipt_id(),true);
						 receiptService.deleteByIdValue(information.getReceipt_id());
				 }
				
				 List<CreditNote> creditList=creditService.findBySalesId(sales.getSales_id());
				 for(int i = 0;i<creditList.size();i++){	
					 CreditNote credit= new CreditNote();
						credit = creditList.get(i);		
						creditService.diactivateByIdValue(credit.getCredit_no_id());
				 }
				}
				Quotation quot =null;
				try {
					quot = quoteservice.getById(quote_id);
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//The import button getting disabled as and when there is transaction
				/*for(QuotationDetails details : quot.getQuotationDetails())
				{
					if(details.getService_id().getService_name().equals("Data Migration"))
					{
						System.out.println("Data Migration ReceiptList called");
						quoteservice.saveupdatedservices(details.getQuotation_detail_id(),false);
					
						break;
					}
				}
				*/
				
			}
			
		}
		
		List<Receipt> chweck=receiptService.findAllRecepitOfCompany(company_id,true);

		model.addObject("importflag", importflag);
		model.addObject("receiptList", chweck);
		//model.addObject("receiptListYear",receiptService.findAllRecepitOfCompany(company_id,true,yearId));
		model.addObject("importfail", importfail);
		model.addObject("flagFailureList", true);
		model.addObject("yearEndlist", yearEndlist);
		model.addObject("company", company);
		model.setViewName("/transactions/receiptList");
		return model;
		
	}
    
    @RequestMapping(value = "viewReceipt", method = RequestMethod.GET)
	public ModelAndView viewReceipt(@RequestParam("id") long id) throws MyWebException {
		ModelAndView model = new ModelAndView();
		Receipt receipt = new Receipt();
		
		try {
			receipt = receiptService.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double closingbalance=0;
		if(receipt.getCustomer()!=null && receipt.getCompany()!= null && receipt.getAccountingYear()!=null)
		{
			closingbalance=openingbalances.getclosingbalance(receipt.getCompany().getCompany_id(),receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCustomer().getCustomer_id(),5);
		}
		if(receipt.getSubLedger()!=null && receipt.getCompany()!= null && receipt.getAccountingYear()!=null)
		{
			closingbalance=openingbalances.getclosingbalance(receipt.getCompany().getCompany_id(),receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getSubLedger().getSubledger_Id(),2);
		}		
		model.addObject("closingbalance", closingbalance);
		model.addObject("customerproductList", receiptService.findAllReceiptProductEntityList(id));
		model.addObject("receipt", receipt);
		model.addObject("flag", flag);
		if(receipt.getCreated_by()!=null)
		{
		model.addObject("created_by", userService.getById(receipt.getCreated_by()));
		}
		if(receipt.getUpdated_by()!=null)
		{
		model.addObject("updated_by", userService.getById(receipt.getUpdated_by()));
		}
		model.setViewName("/transactions/receiptView");
		return model;
	}
    
    @RequestMapping(value = "editReceipt", method = RequestMethod.GET)
	public synchronized ModelAndView editReceipt(@RequestParam("id") long id, 
			HttpServletRequest request, 
			HttpServletResponse response) {
    	List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		ModelAndView model = new ModelAndView();
		Receipt receipt = new Receipt();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User)session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");
		receipt_id=id;
		long user_id=(long)session.getAttribute("user_id");
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		String yearrange = user.getCompany().getYearRange();
		receipt = receiptService.findOneWithAll(id);
		Boolean flag = false;
		Boolean flag1 =false;
		
		if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment()==false && receipt.getSales_bill_id()!=null)
        {
			Double total=(double)0;
			SalesEntry salesEntry = receipt.getSales_bill_id();
			for (Receipt receiptPayment : salesEntry.getReceipts()) {
				if(receiptPayment.getTds_amount()!=null)
				total += receiptPayment.getAmount()+receiptPayment.getTds_amount(); 
				else
					total += receiptPayment.getAmount();				
			}
			
			if(salesEntry.getRound_off().equals(total) || total>=salesEntry.getRound_off()) {
				flag=false;
			}
			else
			{
				flag=true;
			}
			
        } 
		else if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment()==true)
		{
			if(receipt.getIs_salegenrated()!=null && receipt.getIs_salegenrated().equals(true))
			{
				flag1=true;
				flag=false;
			}
			else
			{
				flag=true;
			}
		}
		else if(receipt.getFlag()!=null && receipt.getFlag()==false)
		{
			flag=true;
		}
		else if(receipt.getSubLedger()!=null)
		{
			flag=true;
		}
		else if(receipt.getAgainstOpeningBalnce()!=null && receipt.getAgainstOpeningBalnce().equals(true))
		 {
			flag1=false;
			flag=false;
	     }
		
		if(flag==true)
		{
		try {
			if(id > 0){
				
					if(receipt.getCustomer() != null){
						receipt.setCustomer_id(receipt.getCustomer().getCustomer_id());
					}
					else if(receipt.getSubLedger()!=null)
					{
						receipt.setCustomer_id((long)-1);
					}
					if(receipt.getSales_bill_id()!=null){
						receipt.setSalesEntryId(receipt.getSales_bill_id().getSales_id());
					}					
					if(receipt.getSubLedger() != null){
						receipt.setSubledgerId(receipt.getSubLedger().getSubledger_Id());
					}					
					if(receipt.getBank() != null){
						receipt.setBankId(receipt.getBank().getBank_id());
					}	

					Double paidtds=0.0;
					if(receipt.getCustomer() != null && receipt.getSales_bill_id() != null){	
						paidtds=receiptService.findpaidtds(receipt.getSales_bill_id().getSales_id());
					}
					if((String) session.getAttribute("msg")!=null){
						model.addObject("successMsg", (String) session.getAttribute("msg"));
						session.removeAttribute("msg");
					}
					 if(AccYear==-1){
							yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
						}else{
							yearList = accountingYearService.findAccountRangeOfCompany(strLong);
						 
						}
					model.addObject("voucherrange", companyService.getVoucherRange(company_id));
					model.addObject("customerproductList",  receiptService.findAllReceiptProductEntityList(id));
					model.addObject("receipt", receipt);
					model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
					model.addObject("productList", productService.findAllProductsOfCompany(company_id));
					model.addObject("salesEntryList", SalesEntryService.findAllSalesEntriesOfCompany(company_id,false));
					model.addObject("subledgerList", subLedgerService.findAllApprovedByCompanywithdefault(company_id));
					model.addObject("bankList", bankService.findAllBanksOfCompany(user.getCompany().getCompany_id()));
					model.addObject("stateId",user.getCompany().getState().getState_id());
					model.addObject("paidtds", paidtds);
					model.addObject("yearList",yearList);
				    //model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
					model.setViewName("/transactions/receipt");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		else if(flag1==true && flag==false)
		{
			session.setAttribute("msg", "You can't edit receipt voucher because sales voucher is generated for this receipt");
			model.setViewName("redirect:/receiptList");
		}
		else if(receipt.getAgainstOpeningBalnce()!=null && receipt.getAgainstOpeningBalnce().equals(true) && flag1==false && flag==false)
		{
			session.setAttribute("msg", "You can't edit receipt voucher because receipt voucher is generated against opening balance of customer");
			return new ModelAndView("redirect:/receiptList");
		}
		else
		{
			session.setAttribute("msg", "You can't edit receipt voucher because you have completed your total receipt amount against your sales voucher");
			model.setViewName("redirect:/receiptList");
		}
		
		return model;
	}

	@RequestMapping(value ="deleteReceiptProduct", method = RequestMethod.GET)
	public synchronized ModelAndView deleteReceiptProduct(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response) 
	{
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		String msg = " ";
		if(receiptService.findAllReceiptProductEntityList(receipt_id).size()!=1)
		{
			msg= receiptService.deleteReceiptProduct(id,receipt_id,company_id);
		
		}
		else
		{
			msg = "You can't delete this product. You can edit this or you can add your required product first and then delete this product.";
		}
		session.setAttribute("msg", msg);
		return new ModelAndView("redirect:/editReceipt?id="+receipt_id);
		
	}
	
	@RequestMapping(value ="deleteReceipt", method = RequestMethod.GET)
	public synchronized ModelAndView deleteReceipt(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
	
		HttpSession session = request.getSession(true);
		Receipt receipt = new Receipt();
		String msg="";
		receipt = receiptService.findOneWithAll(id);
		
		if(receipt.getIs_salegenrated()!=null && receipt.getIs_salegenrated().equals(true))
		{
			session.setAttribute("msg", "You can't delete receipt voucher because sales voucher is generated for this receipt");
			return new ModelAndView("redirect:/receiptList");
		}
		else if(receipt.getAgainstOpeningBalnce()!=null && receipt.getAgainstOpeningBalnce().equals(true))
		{
			session.setAttribute("msg", "You can't delete receipt voucher because receipt voucher is generated against opening balance of customer");
			return new ModelAndView("redirect:/receiptList");
		}
		else
		{
			msg=receiptService.deleteByIdValue(id);
			session.setAttribute("msg", msg);
			if(flag == true)
			{
				return new ModelAndView("redirect:/receiptList");
			}
			else
			{
				return new ModelAndView("redirect:/receiptFailure");
			}
		}
	}
    
	@RequestMapping(value = "downloadReceipt", method = RequestMethod.GET)
    public ModelAndView downloadReceipt(@RequestParam("id") long id) {
	 
		ReceiptForm form = new ReceiptForm();
		Receipt receipt = new Receipt();

		try {
			receipt = receiptService.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		form.setReceipt(receipt);
		form.setCustomerproductList(receiptService.findAllReceiptProductEntityList(id));

		return new ModelAndView("ReceiptPdfView", "form", form);
    }
	
/*	@RequestMapping(value = "downloadReceiptAccountingVoucher", method = RequestMethod.GET)
    public ModelAndView downloadReceiptAccountingVoucher(@RequestParam("id") long id) {
	 
		ReceiptForm form = new ReceiptForm();
		Receipt receipt = new Receipt();

		try {
			receipt = receiptService.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		form.setReceipt(receipt);
		form.setCustomerproductList(receiptService.findAllReceiptProductEntityList(id));	
		return new ModelAndView("ReceiptAccountingVoucherPdfView", "form", form);
    }*/
	
	@RequestMapping(value = {"importExcelReceipt"}, method = { RequestMethod.POST })
	public ModelAndView importExcelReceipt(@RequestParam("excelFile") MultipartFile excelfile,HttpServletRequest request, HttpServletResponse response) throws IOException{
		boolean isValid = true;
		
		StringBuffer successmsg = new StringBuffer();
		StringBuffer failuremsg = new StringBuffer();
		StringBuffer ErrorMsgs=new StringBuffer();
		String invalidData="";
		boolean Err=false;
		Integer failcount=0;
		Integer sucount=0;
		Integer maincount = 0;
		Integer repetProductafterunsuccesfullattempt = 0;
		
		successVocharList.clear();
		failureVocharList.clear();
		
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		long user_id=(long)session.getAttribute("user_id");
        
		Integer m=0;
		User user = new User();
		user = (User)session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		List<Customer> list =customerService.findAllCustomersOnlyOFCompany(company_id);
		List<SalesEntry> saleslist = SalesEntryService.findAllSalesEntriesOfCompany(company_id,false);
		List<SubLedger> sublist = subLedgerService.findAllSubLedgersOnlyOfCompany(company_id);
		List<Bank> banklist = bankService.findAllBanksOfCompany(company_id);
	    List<AccountingYear> yearlist = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		try {
			
			   if (excelfile.getOriginalFilename().endsWith("xls")) {
	        	System.out.println("file is xls...");
	    			int i = 0;
	    			HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
	    			HSSFSheet worksheet = workbook.getSheetAt(0);
					if(isValid){
						i = 1;
		    			while (i <= worksheet.getLastRowNum()) {
		    				
		    				Receipt entry = new Receipt();
							Receipt_product_details entity = new  Receipt_product_details();
							Customer customer1 = null;
							SubLedger subledger1= null;
							Bank bank1 = null;
							SalesEntry salesEntry1=null;
							AccountingYear year_range1=null;
							Product product1=null;
							HSSFRow row = worksheet.getRow(i++);
							Err=false;
							if(row.getLastCellNum()==0)
							{				
						    	System.out.println("Invalid Data");
							}
							else
							{	
								Double transaction_value = (double)0;
								Double cgst = (double)0;
								Double igst = (double)0 ;
								Double sgst = (double)0;
								Double state_comp_tax = (double)0;
								Double round_off = (double)0;
								Double vat = (double)0;
								Double vatcst = (double)0;
								Double excise = (double)0;
								Double tds = (double)0;
								Double Newtds = (double)0;
								
								
								Integer count=0;
								Integer cnt=0;
								boolean flag = false ;
								
								String salesVoucherNofromExcel="";
								String receiptVoucherNofromExcel="";
								String chequeNofromExcel="";
								
								
								try
								{
									salesVoucherNofromExcel=row.getCell(1).getStringCellValue().trim();
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try
								{
									Double voucherNofromExcel= row.getCell(1).getNumericCellValue();
									Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
									salesVoucherNofromExcel=voucherNofromExcel1.toString().trim();
									
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try
								{
									receiptVoucherNofromExcel=row.getCell(3).getStringCellValue().trim().replaceAll(" ", "");
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try
								{
									Double voucherNofromExcel= row.getCell(3).getNumericCellValue();
									Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
									receiptVoucherNofromExcel=voucherNofromExcel1.toString().trim();
									
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								
								try
								{
									chequeNofromExcel=row.getCell(7).getStringCellValue().trim();
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try
								{
									Double voucherNofromExcel= row.getCell(7).getNumericCellValue();
									Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
									chequeNofromExcel=voucherNofromExcel1.toString().trim();
									
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}   
									
								    Receipt oldentry = receiptService.isExcelVocherNumberExist(receiptVoucherNofromExcel, company_id);
									if(oldentry!=null)
									{
										
										List<Receipt_product_details> prolist = receiptService.findAllReceiptProductEntityList(oldentry.getReceipt_id());
										Boolean mainentryflag = false;
										String finalhsncode="1";
										
										if(prolist!=null && prolist.size()!=0)
										{
										String vocherNo = oldentry.getExcel_voucher_no().trim();
										Long company_Id = oldentry.getCompany().getCompany_id();
										String ProductName = row.getCell(14).getStringCellValue();
										
										if(row.getCell(13)!=null)
										{
										Double hsncode= row.getCell(13).getNumericCellValue();
										Integer hsncode1 = hsncode.intValue();
										finalhsncode=hsncode1.toString().trim();
										}
										
										
										Boolean entryflag = false ;
										
										for(Receipt_product_details sEPC : prolist)
										{
											if(finalhsncode.replace(" ","").trim().equalsIgnoreCase(sEPC.getHSNCode().replace(" ","").trim()) && ProductName.replace(" ","").trim().equalsIgnoreCase(sEPC.getProduct_name().replace(" ","").trim()))
											{
												entryflag=true;
											}
										}
										
										if(company_Id==company_id && vocherNo.replace(" ","").trim().equalsIgnoreCase(receiptVoucherNofromExcel.replace(" ","").trim()) && entryflag==true)
										{
											mainentryflag=true;
										}
										
										}
										else if(prolist.size()==0)
										{
											mainentryflag=true;
										}
										
								  if(mainentryflag==false)
								  {	
										
									    flag = true;
									    
									   			    
										if(oldentry.getGst_applied()==1)
										{
											 if(oldentry.getCustomer()!=null) 
												{
													customer1=oldentry.getCustomer();
												}
										transaction_value=oldentry.getTransaction_value();
										round_off=oldentry.getAmount();
										cgst=oldentry.getCgst();
										igst=oldentry.getIgst();
										sgst= oldentry.getSgst();
										state_comp_tax=oldentry.getState_compansation_tax();
										vat= oldentry.getTotal_vat();
										vatcst=oldentry.getTotal_vatcst();
										excise=oldentry.getTotal_excise();
										tds=oldentry.getTds_amount();
										String ProductName = row.getCell(14).getStringCellValue();
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
													product1=product;
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
										
										
										
										if(row.getCell(15)!=null)
										{
										entity.setQuantity((Double) row.getCell(15).getNumericCellValue());
										}else
										{
											entity.setQuantity((double)0);
										}

										
										if(row.getCell(16)!=null)
										{
											entity.setRate((Double) row.getCell(16).getNumericCellValue());
										}else
										{
											entity.setRate((double)0);
										}
										
										

										if(row.getCell(17)!=null)
										{
											entity.setDiscount((Double) row.getCell(17).getNumericCellValue());
										}else
										{
											entity.setDiscount((double)0);
										}
										
										if(row.getCell(18)!=null)
										{
											entity.setCGST((Double) row.getCell(18).getNumericCellValue());
										}else
										{
											entity.setCGST((double)0);
										}

										
										if(row.getCell(19)!=null)
										{
											entity.setIGST((Double) row.getCell(19).getNumericCellValue());

										}else
										{
											entity.setIGST((double)0);
										}

										if(row.getCell(20)!=null)
										{
											entity.setSGST((Double) row.getCell(20).getNumericCellValue());

										}else
										{
											entity.setSGST((double)0);
										}


										
										if(row.getCell(21)!=null)
										{
											entity.setState_com_tax((Double) row.getCell(21).getNumericCellValue());

										}else
										{
											entity.setState_com_tax((double)0);
										}
										

										if(row.getCell(22)!=null)
										{
											entity.setLabour_charges((Double) row.getCell(22).getNumericCellValue());

										}else
										{
											entity.setLabour_charges((double)0);
										}

										if(row.getCell(23)!=null)
										{
											entity.setFreight((Double) row.getCell(23).getNumericCellValue());

										}else
										{
											entity.setFreight((double)0);
										}
										
										if(row.getCell(24)!=null)
										{
											entity.setOthers((Double) row.getCell(24).getNumericCellValue());

										}else
										{
											entity.setOthers((double)0);
										}

										
										if(row.getCell(25)!=null)
										{
											entity.setUOM(row.getCell(25).getStringCellValue().trim());

										}else
										{
											entity.setUOM("NA");
										}	

										entity.setHSNCode(finalhsncode);

										if(row.getCell(26)!=null)
										{
											entity.setVAT((Double) row.getCell(26).getNumericCellValue());


										}else
										{
											entity.setVAT((double)0);
										}
										
										if(row.getCell(27)!=null)
										{
											entity.setVATCST((Double) row.getCell(27).getNumericCellValue());


										}else
										{
											entity.setVATCST((double)0);
										}
										
										if(row.getCell(28)!=null)
										{
											entity.setExcise((Double) row.getCell(28).getNumericCellValue());


										}else
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
											Double disamount =((round(damount,2))-((((round(damount,2))*(entity.getDiscount())))/100));
											tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFreight()+entity.getOthers();
										}
										else
										{
											tamount = amount;
										}
										transaction_value=transaction_value+round(tamount,2);
										round_off=(round_off+tds)+entity.getCGST()+entity.getIGST()+entity.getSGST()+entity.getState_com_tax()+tamount ;
										
										
										if(product1!=null)
										{

										if ((oldentry.getCustomer() != null)) {
											
											if(oldentry.getAdvance_payment()!=null && oldentry.getAdvance_payment()==true) {
											customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,
													oldentry.getCustomer().getCustomer_id(), (long) 5, ((double)oldentry.getAmount()+tds), (double)0, (long) 0);
											customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id, oldentry.getCustomer().getCustomer_id(),
													(long) 5, ((double)round_off+Newtds), (double)0, (long) 1);
											
											}
											else
											{
												customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,
														oldentry.getCustomer().getCustomer_id(), (long) 5, ((double)oldentry.getAmount()), (double)0, (long) 0);
												customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id, oldentry.getCustomer().getCustomer_id(),
														(long) 5, ((double)round_off), (double)0, (long) 1);
											}
										}  
										
									
										
										if (oldentry.getGst_applied()==1)
										 {
											 if(oldentry.getPayment_type()==1)
											 {
												 if((oldentry.getTransaction_value()!=null) && (oldentry.getTransaction_value()>0) && (transaction_value>0) && (oldentry.getTransaction_value()!=transaction_value))
												 {
													
													if(transaction_value>0)
													 {
														 SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",company_id);
															
															if(subledgercash!=null)
															{
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)(oldentry.getTransaction_value()),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)(transaction_value),(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
															}	
													 }
													
												 }
													
											 }
											 else
											 {
												 if((oldentry.getTransaction_value()!=null) && (oldentry.getTransaction_value()>0)&& (transaction_value>0) && (oldentry.getTransaction_value()!=transaction_value))
												    {
													 if(oldentry.getBank()!=null)
													 {
													 bank1=oldentry.getBank();
													 bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)0,(double)(oldentry.getTransaction_value()),(long) 0);
													 bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)0,(double)(transaction_value),(long) 1);
													 }
												 	}
												 
												 
												
											 }	 
											 
											 if((oldentry.getCgst()!=null)&&(oldentry.getCgst()>0)&&(cgst>0)&&(oldentry.getCgst()!=cgst))
												{
													SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",company_id);
													if(subledgercgst!=null)
													{
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getCgst(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getCgst()!=null)&&(cgst>0) && (oldentry.getCgst()==0))
												{
													SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",company_id);
													if(subledgercgst!=null)
													{
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												if((oldentry.getSgst()!=null)&&(oldentry.getSgst()>0)&&(sgst>0)&&(oldentry.getSgst()!=sgst))
												{
													SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",company_id);
													
													if(subledgersgst!=null)
													{
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getSgst(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getSgst()!=null)&&(sgst>0)&&(oldentry.getSgst()==0))
												{
	                                              SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",company_id);
													
													if(subledgersgst!=null)
													{
												
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
													
												}
												
												if((oldentry.getIgst()!=null) && (oldentry.getIgst()>0) && (igst>0) && (oldentry.getIgst()!=igst))
												{
													SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",company_id);
													
													if(subledgerigst!=null)
													{
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getIgst(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getIgst()!=null)&&(igst>0)&&(oldentry.getIgst()==0))
												{
	                                             SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",company_id);
													
													if(subledgerigst!=null)
													{
												
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if((oldentry.getState_compansation_tax()!=null) && (oldentry.getState_compansation_tax()>0) && (state_comp_tax>0)&&(oldentry.getState_compansation_tax()!=state_comp_tax))
												{
													SubLedger subledgercess = subledgerDAO.findOne("Output CESS",company_id);
													
													if(subledgercess!=null)
													{
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getState_compansation_tax(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getState_compansation_tax()!=null) && (state_comp_tax>0) && (oldentry.getState_compansation_tax()==0))
												{
	                                             SubLedger subledgercess = subledgerDAO.findOne("Output CESS",company_id);
													
													if(subledgercess!=null)
													{
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if((oldentry.getTotal_vat()!=null) && (oldentry.getTotal_vat()>0) && (vat>0)&&(oldentry.getTotal_vat()!=vat))
												{
													SubLedger subledgervat = subledgerDAO.findOne("Output VAT",company_id);
													
													if(subledgervat!=null)
													{
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getTotal_vat(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getTotal_vat()!=null) && (vat>0) && (oldentry.getTotal_vat()==0))
												{
	                                              SubLedger subledgervat = subledgerDAO.findOne("Output VAT",company_id);
													
													if(subledgervat!=null)
													{
			
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if((oldentry.getTotal_vatcst()!=null) && (oldentry.getTotal_vatcst()>0) && (vatcst>0)&&(oldentry.getTotal_vatcst()!=vatcst))
												{
													SubLedger subledgercst = subledgerDAO.findOne("Output CST",company_id);
													
													if(subledgercst!=null)
													{subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getTotal_vatcst(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getTotal_vatcst()!=null) && (vatcst>0) && (oldentry.getTotal_vatcst()==0))
												{
	                                             SubLedger subledgercst = subledgerDAO.findOne("Output CST",company_id);
													
													if(subledgercst!=null)
													{
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if((oldentry.getTotal_excise()!=null) && (oldentry.getTotal_excise()>0) && (excise>0)&&(oldentry.getTotal_excise()!=excise))
												{
													SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",company_id);
													
													if(subledgerexcise!=null)
													{subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getTotal_excise(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
												else if((oldentry.getTotal_excise()!=null) && excise>0 && (oldentry.getTotal_excise()==0))
												{
	                                             SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",company_id);
													
													if(subledgerexcise!=null)
													{
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													}
												}
										 }
										entity.setTransaction_amount(round(tamount,2));
										oldentry.setRound_off(round(round_off,2));
										oldentry.setAmount(round(round_off-tds,2));
										oldentry.setTds_amount(round(tds,2));
										oldentry.setTransaction_value(round(transaction_value,2));
										oldentry.setCgst(round(cgst,2));
										oldentry.setIgst(round(igst,2));
										oldentry.setSgst(round(sgst,2));
										oldentry.setState_compansation_tax(round(state_comp_tax,2));
										oldentry.setTotal_vat(round(vat,2));
										oldentry.setTotal_vatcst(round(vatcst,2));
										oldentry.setTotal_excise(round(excise,2));
										
	                                    receiptService.updateReceiptThroughExcel(oldentry);
										entity.setReceipt_header_id(oldentry.getReceipt_id());
										receiptService.updateReceipt_product_detailsThroughExcel(entity);
										
										 if(repetProductafterunsuccesfullattempt==0)
	                                     {
	                                    	
	                                    	 sucount=sucount+1;
	                                    	 m = 1;
	                                    	successVocharList.add(receiptVoucherNofromExcel);
	                                     }
										 
										}
										else
										{
											/*receiptService.changeStatusOfReceiptThroughExcel(oldentry);
											if(successVocharList!=null && successVocharList.size()>0)
											{
												sucount=sucount-1;
												failcount=failcount+1;
												m = 2;
												successVocharList.remove(receiptVoucherNofromExcel);
												failureVocharList.add(receiptVoucherNofromExcel);
											}*/
											
										}
											
										}
									/*break;*/
								  }
									
								/*  if(mainentryflag==true)
								  {	
									  break;
								  }*/
								  
								}
									
									
									
								
								 if(flag==false)
								{
									
									 if(!failureVocharList.contains(receiptVoucherNofromExcel))
									{
									 Boolean flag1 = false;
									 
									 Receipt oldreceipt = receiptService.isExcelVocherNumberExist(receiptVoucherNofromExcel, company_id);
									 
										 if(oldreceipt !=null && oldreceipt.getExcel_voucher_no()!=null)
										 {
										 String str=oldreceipt.getExcel_voucher_no().trim();
											if(str.replace(" ","").trim().equalsIgnoreCase(receiptVoucherNofromExcel.replace(" ","").trim())) {
												flag1=true;
											  /*  break;*/
											}
										 }
								
									 
									 if(flag1==false)
									 {
										
										 
										 String finalhsncode="1";
										 
										 if(row.getCell(13)!=null)
										 {
										 Double hsncode= row.getCell(13).getNumericCellValue();
										 Integer hsncode1 = hsncode.intValue();
										 finalhsncode=hsncode1.toString().trim();
										 }
										 
									   if(row.getCell(0)==null)
										{
					
										}
										else
										{
											try {
												cnt=0;
												for(Customer customer:list)
												{
													
													String str=customer.getFirm_name().trim();
													if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(0).getStringCellValue().replace(" ","").trim())) {
														entry.setCustomer(customer);
														customer1=customer;
														count=count+1;
														cnt=1;
													    break;
													}
												}
												if(cnt==0){Err=true;ErrorMsgs.append(" Customer name is incorrect ");}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
										entry.setCompany(user.getCompany());
										
										if(row.getCell(1)==null)
										{
										
										}
										else
										{
											cnt=0;
											try {
												for(SalesEntry salesEntry:saleslist)
												{
														String str = salesEntry.getVoucher_no().trim();
														if (str.replace(" ","").trim().equalsIgnoreCase(salesVoucherNofromExcel.replace(" ","").trim())) {
															entry.setSales_bill_id(salesEntry);
															count = count + 1;
															cnt=1;
															salesEntry1=salesEntry;
															break;
												}
												}
												if(cnt==0){Err=true;ErrorMsgs.append("Sales Voucher no is incorrect ");}
												}
												catch (Exception e) {
													e.printStackTrace();
												}
										}
										
										if(row.getCell(2)==null)
										{
											
										}
										else
										{
											try
											{
												cnt=0;
											for(SubLedger subledger:sublist)
											{
													String str = subledger.getSubledger_name().trim();
													if (str.replace(" ","").trim().equalsIgnoreCase(row.getCell(2).getStringCellValue().replace(" ","").trim())) {
														entry.setSubLedger(subledger);
														subledger1 = subledger;
														count = count + 1;
														cnt=1;
														break;
											}
											}
											if(cnt==0){Err=true;ErrorMsgs.append(" Subledger name is incorrect ");}
											}
											catch (Exception e) {
												e.printStackTrace();
											}
										}
										
										
										 String remoteAddr = "";
										 remoteAddr = request.getHeader("X-FORWARDED-FOR");
								            if (remoteAddr == null || "".equals(remoteAddr)) {
								                remoteAddr = request.getRemoteAddr();
								            }
								            entry.setIp_address(remoteAddr);
								            entry.setCreated_by(user_id);
										
										entry.setExcel_voucher_no(receiptVoucherNofromExcel);
										
										if(row.getCell(31)!=null && row.getCell(31).getStringCellValue()!=null)
										{
											entry.setOther_remark(row.getCell(31).getStringCellValue().trim());
										}
										
										entry.setLocal_time(new LocalTime());
										if(row.getCell(5)!=null)
										{
											try {
												String str=row.getCell(5).getDateCellValue().toString();

												entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvert(str)));
								   }
											   catch (Exception e) {
											   e.printStackTrace();
											    }
											try {
												String str=row.getCell(5).getStringCellValue().toString();

												entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvertNew(str)));
								   }
											   catch (Exception e) {
											   e.printStackTrace();
											    }
										
										}
										
										try {
											cnt=0;
											System.out.println("The yearlist is "+yearlist.size());
											for(AccountingYear year_range:yearlist)
											{
												
												String str =year_range.getYear_range().trim();
											
												if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(4).getStringCellValue().replace(" ","").trim())) {
													entry.setAccountingYear(year_range);
													System.out.println("The yearlist is entered");
													count=count+1;
													cnt=1;
													year_range1=year_range;
													if(entry.getDate()!=null)
													{
													entry.setVoucher_no(commonService.getVoucherNumber("RV", VOUCHER_RECEIPT, entry.getDate(),year_range.getYear_id(), company_id));
													}
												    break;
												}
											}
											if(cnt==0){Err=true;ErrorMsgs.append(" Accounting Year is incorrect ");}
										} catch (Exception e) {
											e.printStackTrace();
										}
										
										if(row.getCell(6)!=null)
										{
										if(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("Cash"))
										{
											entry.setPayment_type(1);
										}
										else if(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("Cheque")) {
											entry.setPayment_type(2);
										}else if(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("DD")) {
											
											entry.setPayment_type(3);
										}else if(row.getCell(6)!=null) {
											entry.setPayment_type(4);
										}
										}
										
										if(row.getCell(6)!=null)
										{
										if(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("Cash"))
										{
											
										}
										else
										{
											if(row.getCell(7)!=null)
											{
											entry.setCheque_no(chequeNofromExcel);
											}
											if(row.getCell(8)!=null)
											{
												 try {
														entry.setCheque_date(ClassToConvertDateForExcell.dateConvert(row.getCell(8).getDateCellValue().toString()));

												   }
												   catch (Exception e) {
												    e.printStackTrace();
												    }
											}
											
											if(row.getCell(9)!=null)
											{
												String[] banknameAndAcc = row.getCell(9).getStringCellValue().replace(" ","").trim().split("-");
												String bankname = banknameAndAcc[0];
												String accno = banknameAndAcc[1];
												cnt=0;
											try {
												for(Bank bank:banklist)
												{
													
													String strbankname =bank.getBank_name().trim();
													String straccno = bank.getAccount_no().toString();
													
													
													if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno)) {
														entry.setBank(bank);
														bank1=bank;
														count=count+1;
														cnt=1;
													    break;
													}
												}
												if(cnt==0){Err=true;ErrorMsgs.append(" Bank name is incorrect ");}
											} catch (Exception e) {
												e.printStackTrace();
											}
										    }
										}
										}
										
										

										if(row.getCell(12)!=null)
										{
										if(row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES"))
										{
										entry.setAdvance_payment(true);
										}
										else if(row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO"))
										{
											entry.setAdvance_payment(false);
										}
										}
										
										entry.setCreated_date(new LocalDate());
										
										if((row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("NO")))
										{
											entry.setGst_applied(2);
										
											if(row.getCell(12)!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES"))
											{
											if(row.getCell(29)!=null && row.getCell(29).getStringCellValue().trim().equalsIgnoreCase("YES"))
											{
												
												entry.setTds_paid(true);
												if(row.getCell(30)!=null)
												{
													tds=(Double) row.getCell(30).getNumericCellValue();
													
												}
												entry.setTds_amount(round(tds,2));
												entry.setAmount(round((Double)row.getCell(10).getNumericCellValue()-tds,2));
											}
											
											
											if(row.getCell(29)!=null && row.getCell(29).getStringCellValue().trim().equalsIgnoreCase("NO"))
											{
												
											entry.setTds_paid(false);
											entry.setTds_amount(round(tds,2));
											entry.setAmount(round((Double)row.getCell(10).getNumericCellValue()-tds,2));
											}
											
											/*if(row.getCell(29)==null)
											{
												
												entry.setTds_paid(true);
											if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
											{
												if(customer1.getTds_rate()!=null)
												{
												tds = ((Double)row.getCell(10).getNumericCellValue()*customer1.getTds_rate())/100;
												}
											
											}
											entry.setTds_amount(tds);
											entry.setAmount((Double)row.getCell(10).getNumericCellValue()-tds);
											}*/
										
											}
											
											if(row.getCell(12)!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO") && salesEntry1!=null)
											{
											entry.setAmount(round((Double)row.getCell(10).getNumericCellValue(),2));
											/**IF tds is already cut for sales entry then we are adding receipt against that sales entry then tds value for particular receipt is storing in database for showing tds against this receipt in receipt report & ledger report */
											if(salesEntry1 != null && salesEntry1.getTds_amount()!=null){	
											tds=(entry.getAmount()*salesEntry1.getTds_amount())/salesEntry1.getRound_off();
											}
											entry.setTds_amount(round(tds,2));	
											entry.setTds_paid(false); /**here we are giving tds paid = false because tds is already deducted for sales entry. this tds amount will required for report  receipt report & ledger report .*/
											
											if(entry.getAmount()>salesEntry1.getRound_off())
											{
												entry.setAdvance_payment(true);
												entry.setIs_salegenrated(true);
												entry.setIs_extraAdvanceReceived(true);
												
											}
											else
											{
												entry.setAdvance_payment(false);
											}
											
											
											}
										}
										
									    if((row.getCell(11)!=null)&&((row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("YES")))&&(subledger1==null))
										{
											entry.setGst_applied(1);
											String ProductName = row.getCell(14).getStringCellValue();
											
											for(Customer customer:list)
											{
												Boolean flag2 = false;
												cnt=0;
												if(customer1!=null){
												if(  (customer1.getCustomer_id().equals(customer.getCustomer_id())))
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
														product1=product;
														count=count+1;
														cnt=1;
														flag2=true;
														break;

													}
													}
													else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
													{
														
														String strproductName = product.getProduct_name().trim();
														if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
														entity.setProduct_name(product.getProduct_name());
														entity.setProduct_id(product.getProduct_id());
														flag2=true;
														product1=product;
														count = count+1;
														cnt=1;
														break;
													}
													}
												}
												
												if(flag2==true)
												{
													break;
												}

											}
											catch (Exception e) {
												e.printStackTrace();
											}
											
											}}else{break;}
											
											}
											
											if(cnt==0 && customer1!=null){Err=true;ErrorMsgs.append(" Product name is incorrect ");}
											
											if(row.getCell(15)!=null)
											{
											entity.setQuantity(round((Double) row.getCell(15).getNumericCellValue(),2));
											}else
											{
												entity.setQuantity((double)0);
											}

											
											if(row.getCell(16)!=null)
											{
												entity.setRate(round((Double) row.getCell(16).getNumericCellValue(),2));
											}else
											{
												entity.setRate((double)0);
											}
											
											

											if(row.getCell(17)!=null)
											{
												entity.setDiscount(round((Double) row.getCell(17).getNumericCellValue(),2));
											}else
											{
												entity.setDiscount((double)0);
											}
											
											if(row.getCell(18)!=null)
											{
												entity.setCGST(round((Double) row.getCell(18).getNumericCellValue(),2));
											}else
											{
												entity.setCGST((double)0);
											}

											
											if(row.getCell(19)!=null)
											{
												entity.setIGST(round((Double) row.getCell(19).getNumericCellValue(),2));

											}else
											{
												entity.setIGST((double)0);
											}

											if(row.getCell(20)!=null)
											{
												entity.setSGST(round((Double) row.getCell(20).getNumericCellValue(),2));

											}else
											{
												entity.setSGST((double)0);
											}


											
											if(row.getCell(21)!=null)
											{
												entity.setState_com_tax(round((Double) row.getCell(21).getNumericCellValue(),2));

											}else
											{
												entity.setState_com_tax((double)0);
											}
											

											if(row.getCell(22)!=null)
											{
												entity.setLabour_charges(round((Double) row.getCell(22).getNumericCellValue(),2));

											}else
											{
												entity.setLabour_charges((double)0);
											}

											if(row.getCell(23)!=null)
											{
												entity.setFreight(round((Double) row.getCell(23).getNumericCellValue(),2));

											}else
											{
												entity.setFreight((double)0);
											}
											
											if(row.getCell(24)!=null)
											{
												entity.setOthers(round((Double) row.getCell(24).getNumericCellValue(),2));

											}else
											{
												entity.setOthers((double)0);
											}

											
											if(row.getCell(25)!=null)
											{
												entity.setUOM(row.getCell(25).getStringCellValue().trim());

											}else
											{
												entity.setUOM("NA");
											}	

											entity.setHSNCode(finalhsncode);

											if(row.getCell(26)!=null)
											{
												entity.setVAT(round((Double) row.getCell(26).getNumericCellValue(),2));


											}else
											{
												entity.setVAT((double)0);
											}
											
											if(row.getCell(27)!=null)
											{
												entity.setVATCST(round((Double) row.getCell(27).getNumericCellValue(),2));


											}else
											{
												entity.setVATCST((double)0);
											}
											
											if(row.getCell(28)!=null)
											{
												entity.setExcise(round((Double) row.getCell(28).getNumericCellValue(),2));


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
													Double disamount =((round(damount,2))-((((round(damount,2))*(entity.getDiscount())))/100));
													tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFreight()+entity.getOthers();
												}
												else
												{
													tamount = amount;
												}
											transaction_value=round(tamount,2);
											round_off=cgst+igst+sgst+state_comp_tax+transaction_value ;
											
											if(row.getCell(12)!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES"))
											{
												if(row.getCell(29)!=null && row.getCell(29).getStringCellValue().trim().equalsIgnoreCase("YES"))
												{
													entry.setTds_paid(true);
													/*if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
													{
														if(customer1.getTds_rate()!=null)
														{
														tds = (transaction_value*customer1.getTds_rate())/100;
														}
													
													}*/
													if(row.getCell(30)!=null)
													{
														tds=(Double) row.getCell(30).getNumericCellValue();
														
													}
													entry.setTds_amount(round(tds,2));
													entry.setRound_off(round(round_off,2));
													entry.setAmount(round(round_off,2));
													
												}
												
												if(row.getCell(29)!=null && row.getCell(29).getStringCellValue().trim().equalsIgnoreCase("NO"))
												{
													
													entry.setTds_paid(false);
													entry.setTds_amount(round(tds,2));
													entry.setRound_off(round(round_off,2));
													entry.setAmount(round(round_off,2));
												}
												
												/*if(row.getCell(29)==null)
												{
													
													entry.setTds_paid(true);
													if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
													{
														if(customer1.getTds_rate()!=null)
														{
														tds = (transaction_value*customer1.getTds_rate())/100;
														}
													
													}
													entry.setTds_amount(tds);
													entry.setRound_off(round(round_off-tds,2));
													entry.setAmount(round(round_off-tds,2));
												}*/
												
											}
											else
											{
												entry.setTds_paid(false);
												entry.setTds_amount(round(tds,2));
												entry.setRound_off(round(round_off,2));
												entry.setAmount(round(round_off-tds,2));
											}
									
											entity.setTransaction_amount(round(transaction_value,2));
											entry.setTransaction_value(round(transaction_value,2));
											entry.setCgst(round(cgst,2));
											entry.setSgst(round(sgst,2));
											entry.setIgst(round(igst,2));
											entry.setState_compansation_tax(round(state_comp_tax,2));
											entry.setTotal_vat(round(vat,2));
											entry.setTotal_vatcst(round(vatcst,2));
											entry.setTotal_excise(round(excise,2));
											
										}
									    
									    
									    if((product1!=null) && entry.getAmount() !=null && entry.getAmount()>0&&(customer1!=null)&&(entry.getVoucher_no()!=null)&&(subledger1==null)&&(row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("YES"))&&(row.getCell(12)!=null))
									    {
									    	
											if (((count == 5)&&(customer1!=null && salesEntry1!=null && bank1!=null && year_range1!=null && product1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))||
													((count == 4)&&(customer1!=null && salesEntry1!=null && year_range1!=null && product1!=null && entry.getPayment_type().equals(1)&& row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))
													||((count == 4)&&(customer1!=null && bank1!=null && year_range1!=null && product1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))
													||((count == 3)&&(customer1!=null && year_range1!=null && product1!=null && entry.getPayment_type().equals(1) && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))) 
											{
												entry.setFlag(true);
												sucount=sucount+1;
												successVocharList.add(receiptVoucherNofromExcel);
												repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
												
												Long id1=null;
												
											    entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
												id1 = receiptService.saveReceiptThroughExcel(entry);
						                        entity.setReceipt_header_id(id1);
											    receiptService.saveReceipt_product_detailsThroughExcel(entity);
											    Receipt receipt = receiptService.getById(id1);
											
											try
											{
											if(customer1 != null){
												
												if(entry.getAdvance_payment()!=null && entry.getAdvance_payment()==true) {
												customerService.addcustomertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, customer1.getCustomer_id(), (long) 5, ((double)entry.getAmount()+entry.getTds_amount()),(double)0,(long) 1);
											     }
												else
												{
													customerService.addcustomertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, customer1.getCustomer_id(), (long) 5, ((double)entry.getAmount()),(double)0,(long) 1);
												}
											}
											
											}
											catch(Exception e)
											{
												e.printStackTrace();
											}
											
											try
											{	
											
											if(tds>0 && entry.getTds_paid()!=null && entry.getTds_paid()==true)
											{
												SubLedger subledgerTds = subledgerDAO.findOne("TDS Receivable", company_id);
												if (subledgerTds != null) {
													subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,
															subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)tds, (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
												}
											}
										
											}
											catch(Exception e)
											{
												e.printStackTrace();
											}
											
										    try
											{		
											 if ((row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("YES"))&&(subledger1==null))
											 {
												 if((row.getCell(6)!=null)&&(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("Cash")))
												 {
													 if(transaction_value>0)
													 {
														 SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",company_id);
															
															if(subledgercash!=null)
															{
																subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)(transaction_value),(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
															}	
													 }
														
												 }
												 else
												 {
													 	if((transaction_value>0)&&(bank1!=null))
													 	{
													 		bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)0,(double)(transaction_value),(long) 1);
													 	}
													 	
												 }	
												 if(cgst>0)
													{
														SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",company_id);
														if(subledgercgst!=null)
														{
															subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
														}
													}
													if(sgst>0)
													{
														SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",company_id);
														
														if(subledgersgst!=null)
														{
															subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
														}
													}
													if(igst>0)
													{
														SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",company_id);
														
														if(subledgerigst!=null)
														{
															subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
														}
													}
													if(state_comp_tax>0)
													{
														SubLedger subledgercess = subledgerDAO.findOne("Output CESS",company_id);
														
														if(subledgercess!=null)
														{
															subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
														}
													}
													
													if(vat>0)
													{
														SubLedger subledgervat = subledgerDAO.findOne("Output VAT",company_id);
														
														if(subledgervat!=null)
														{
															subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
														}
													}
													
													if(vatcst>0)
													{
														SubLedger subledgercst = subledgerDAO.findOne("Output CST",company_id);
														
														if(subledgercst!=null)
														{
															subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
														}
													}
													
													if(excise>0)
													{
														SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",company_id);
														
														if(subledgerexcise!=null)
														{
															subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
														}
													}
											 }
											}
											catch(Exception e) {
												
												e.printStackTrace();
											}
												
											}
											else {
												
												if(!successVocharList.contains(receiptVoucherNofromExcel))
												{
													maincount=maincount+1;
													failcount=failcount+1;
							                        failureVocharList.add(receiptVoucherNofromExcel);
							                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
												}
												
											}
											     
										
									    }
									    else if((product1==null)&& entry.getAmount() !=null && entry.getAmount()>0&&(customer1!=null)&&(entry.getVoucher_no()!=null) && (subledger1==null)&&(row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("NO"))&&(row.getCell(12)!=null))   
									    {
									    	
									    	
									    
									    	if (((count == 2)&&(customer1!=null && year_range1!=null && entry.getPayment_type().equals(1) && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))||((count == 4)&&(customer1!=null && salesEntry1!=null && bank1!=null && year_range1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))||
									    		((count == 3)&&(customer1!=null && salesEntry1!=null && year_range1!=null && entry.getPayment_type().equals(1) && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))||
									    		((count == 3)&&(customer1!=null && bank1!=null && year_range1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))
									    			) 
									    	{
												entry.setFlag(true);
												sucount=sucount+1;
												successVocharList.add(receiptVoucherNofromExcel);
												repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
												
												if(row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO"))
												{
												if(entry.getAmount()>salesEntry1.getRound_off())
												{
													 entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
												}
												else
												{
													 entry.setEntry_status(MyAbstractController.ENTRY_NIL);
												}
												}
												if(row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES"))
												{
													entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
												}
												
										    	Long id= receiptService.saveReceiptThroughExcel(entry);
										    	Receipt receipt1 = receiptService.getById(id);
										    	
										    	if(entry.getSales_bill_id() != null){
													SalesEntry salesEntry = salesEntryDao.findOneWithAll(entry.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
													Double total=(double)0;
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
														salesEntryDao.updateSalesEntryThroughExcel(salesEntry);
													} catch (Exception e) {
														e.printStackTrace();
													}

												}
										    try
											{
											if(customer1 != null){
												
												if(entry.getAdvance_payment()!=null && entry.getAdvance_payment()==true) {
												customerService.addcustomertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, customer1.getCustomer_id(), (long) 5, ((double)entry.getAmount()+entry.getTds_amount()),(double)0,(long) 1);
											      }
												else
												{
													customerService.addcustomertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, customer1.getCustomer_id(), (long) 5, ((double)entry.getAmount()),(double)0,(long) 1);
												}
											}
											
											}
											catch(Exception e)
											{
												e.printStackTrace();
											}
										    
										    try
											{	
											
										    if(tds>0 && entry.getTds_paid()!=null && entry.getTds_paid()==true)
											{
												SubLedger subledgerTds = subledgerDAO.findOne("TDS Receivable", company_id);
												if (subledgerTds != null) {
													subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,
															subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)tds, (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
												}
											}
										
											}
											catch(Exception e)
											{
												e.printStackTrace();
											}						
											
											    try
												{	
												if((row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("NO")))
												{
												 if((row.getCell(6)!=null)&&(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("Cash")))
												 {
													
													    SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",company_id);
														if(subledgercash!=null)
														{
														subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)entry.getAmount(),(long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
												
														}
												 }
												 else
													 {
													   if(bank1!=null)
												       {
													   bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)0,(double)entry.getAmount(),(long) 1);
												       }
													   
													 }
											    }
												}
											 catch(Exception e)
											 {
												 e.printStackTrace();
											 }
												
											} 
									    	 else {
													
									    		 if(!successVocharList.contains(receiptVoucherNofromExcel))
													{
														maincount=maincount+1;
														failcount=failcount+1;
								                        failureVocharList.add(receiptVoucherNofromExcel);
								                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
													}
							                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
												}
					    			 }
									    else if(subledger1!=null && row.getCell(10)!=null && (Double)row.getCell(10).getNumericCellValue()!=null && customer1==null && product1==null && entry.getVoucher_no()!=null)
										{
											
											entry.setAmount((Double)row.getCell(10).getNumericCellValue());
											if (((count == 2)&&(subledger1!=null && year_range1!=null))||((count == 3)&&(subledger1!=null && year_range1!=null && bank1!=null)))
											{
												entry.setFlag(true);
												sucount=sucount+1;
												successVocharList.add(receiptVoucherNofromExcel);
												repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
												
												entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
												Long id =receiptService.saveReceiptThroughExcel(entry);
												Receipt receipt1 = receiptService.getById(id);
												
												try
												{
												 if(subledger1 != null && row.getCell(10)!=null){
													 
													 subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledger1.getSubledger_Id(),(long) 2,(double)entry.getAmount(),(double)0,(long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
												 }
												 if (entry.getPayment_type().equals(1)){
														SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand", company_id);
														if (subledgercash != null) {
															subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,
																	subledgercash.getSubledger_Id(), (long) 2, (double) 0, (double)entry.getAmount(), (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);

														}
													} else {
														if(entry.getBank()!=null)
														{
														bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, entry.getBank().getBank_id(),
																(long) 3, (double) 0, (double)entry.getAmount(), (long) 1);
														}
													}
												}
												 catch(Exception e)
													{
														
													}
												
											} 
											else {
												
												if(!successVocharList.contains(receiptVoucherNofromExcel))
												{
													maincount=maincount+1;
													failcount=failcount+1;
							                        failureVocharList.add(receiptVoucherNofromExcel);
							                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
												}
											}
											
										}
										else {
											
											if(!successVocharList.contains(receiptVoucherNofromExcel))
											{
												maincount=maincount+1;
												failcount=failcount+1;
						                        failureVocharList.add(receiptVoucherNofromExcel);
						                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											}
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
		    			
		    			workbook.close(); 
		    			}
	        	} 
	        else if (excelfile.getOriginalFilename().endsWith("xlsx")) {
				int i = 0;
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);	
				if(isValid){
					i = 1;
					
					while (i <= worksheet.getLastRowNum()) {
						Receipt entry = new Receipt();
						Receipt_product_details entity = new  Receipt_product_details();
						Customer customer1 = null;
						SubLedger subledger1= null;
						Bank bank1 = null;
						SalesEntry salesEntry1=null;
						AccountingYear year_range1=null;
						Product product1=null;
						
						
						XSSFRow row = worksheet.getRow(i++);
						Err=false;
						if(row.getLastCellNum()==0)
						{				
					    	System.out.println("Invalid Data");
						}
						else
						{	
							Double transaction_value = (double)0;
							Double cgst = (double)0;
							Double igst = (double)0 ;
							Double sgst = (double)0;
							Double state_comp_tax = (double)0;
							Double round_off = (double)0;
							Double vat = (double)0;
							Double vatcst = (double)0;
							Double excise = (double)0;
							Double tds = (double)0;
							Double Newtds = (double)0;
							boolean paymentType=true;
							
							Integer count=0;
							Integer cnt=0;
							
							boolean flag = false ;
							
							String salesVoucherNofromExcel="";
							String receiptVoucherNofromExcel="";
							String chequeNofromExcel="";
							
							
							
							
							try
							{
								salesVoucherNofromExcel=row.getCell(1).getStringCellValue().trim();
							}
							catch(Exception e)
							{
								//e.printStackTrace();
							}
							
							try
							{
								Double voucherNofromExcel= row.getCell(1).getNumericCellValue();
								Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
								salesVoucherNofromExcel=voucherNofromExcel1.toString().trim();
								
							}
							catch(Exception e)
							{
							//	e.printStackTrace();
							}
							
							try
							{
								receiptVoucherNofromExcel=row.getCell(3).getStringCellValue().trim().replaceAll(" ", "");
							}
							catch(Exception e)
							{
							//	e.printStackTrace();
							}
							
							try
							{
								Double voucherNofromExcel= row.getCell(3).getNumericCellValue();
								Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
								receiptVoucherNofromExcel=voucherNofromExcel1.toString().trim();
								
							}
							catch(Exception e)
							{
								//e.printStackTrace();
							}
							
							
							try
							{
								chequeNofromExcel=row.getCell(7).getStringCellValue().trim();
							}
							catch(Exception e)
							{
								//e.printStackTrace();
							}
							
							try
							{
								Double voucherNofromExcel= row.getCell(7).getNumericCellValue();
								Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
								chequeNofromExcel=voucherNofromExcel1.toString().trim();
								
							}
							catch(Exception e)
							{
								//e.printStackTrace();
							}   
							
							    Receipt oldentry = receiptService.isExcelVocherNumberExist(receiptVoucherNofromExcel, company_id);
								if(oldentry!=null)
								{
									
									List<Receipt_product_details> prolist = receiptService.findAllReceiptProductEntityList(oldentry.getReceipt_id());
									Boolean mainentryflag = false;
									String finalhsncode="1";
									
									if(prolist!=null && prolist.size()!=0)
									{
									String vocherNo = oldentry.getExcel_voucher_no().trim();
									Long company_Id = oldentry.getCompany().getCompany_id();
									String ProductName = row.getCell(14).getStringCellValue();
									
									if(row.getCell(13)!=null)
									{
									Double hsncode= row.getCell(13).getNumericCellValue();
									Integer hsncode1 = hsncode.intValue();
									finalhsncode=hsncode1.toString().trim();
									}
									
									
									Boolean entryflag = false ;
									
									for(Receipt_product_details sEPC : prolist)
									{
										if(finalhsncode.replace(" ","").trim().equalsIgnoreCase(sEPC.getHSNCode().replace(" ","").trim()) && ProductName.replace(" ","").trim().equalsIgnoreCase(sEPC.getProduct_name().replace(" ","").trim()))
										{
											entryflag=true;
										}
									}
									
									if(company_Id==company_id && vocherNo.replace(" ","").trim().equalsIgnoreCase(receiptVoucherNofromExcel.replace(" ","").trim()) && entryflag==true)
									{
										mainentryflag=true;
									}
									
									}
									else if(prolist.size()==0)
									{
										mainentryflag=true;
									}
									
							  if(mainentryflag==false)
							  {	
									
								    flag = true;
								    
								   			    
									if(oldentry.getGst_applied()==1)
									{
										 if(oldentry.getCustomer()!=null) 
											{
												customer1=oldentry.getCustomer();
											}
									transaction_value=oldentry.getTransaction_value();
									round_off=oldentry.getAmount();
									cgst=oldentry.getCgst();
									igst=oldentry.getIgst();
									sgst= oldentry.getSgst();
									state_comp_tax=oldentry.getState_compansation_tax();
									vat= oldentry.getTotal_vat();
									vatcst=oldentry.getTotal_vatcst();
									excise=oldentry.getTotal_excise();
									tds=oldentry.getTds_amount();
									String ProductName = row.getCell(14).getStringCellValue();
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
												product1=product;
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
									
									
									
									if(row.getCell(15)!=null)
									{
									entity.setQuantity((Double) row.getCell(15).getNumericCellValue());
									}else
									{
										entity.setQuantity((double)0);
									}

									
									if(row.getCell(16)!=null)
									{
										entity.setRate((Double) row.getCell(16).getNumericCellValue());
									}else
									{
										entity.setRate((double)0);
									}
									
									

									if(row.getCell(17)!=null)
									{
										entity.setDiscount((Double) row.getCell(17).getNumericCellValue());
									}else
									{
										entity.setDiscount((double)0);
									}
									
									if(row.getCell(18)!=null)
									{
										entity.setCGST((Double) row.getCell(18).getNumericCellValue());
									}else
									{
										entity.setCGST((double)0);
									}

									
									if(row.getCell(19)!=null)
									{
										entity.setIGST((Double) row.getCell(19).getNumericCellValue());

									}else
									{
										entity.setIGST((double)0);
									}

									if(row.getCell(20)!=null)
									{
										entity.setSGST((Double) row.getCell(20).getNumericCellValue());

									}else
									{
										entity.setSGST((double)0);
									}


									
									if(row.getCell(21)!=null)
									{
										entity.setState_com_tax((Double) row.getCell(21).getNumericCellValue());

									}else
									{
										entity.setState_com_tax((double)0);
									}
									

									if(row.getCell(22)!=null)
									{
										entity.setLabour_charges((Double) row.getCell(22).getNumericCellValue());

									}else
									{
										entity.setLabour_charges((double)0);
									}

									if(row.getCell(23)!=null)
									{
										entity.setFreight((Double) row.getCell(23).getNumericCellValue());

									}else
									{
										entity.setFreight((double)0);
									}
									
									if(row.getCell(24)!=null)
									{
										entity.setOthers((Double) row.getCell(24).getNumericCellValue());

									}else
									{
										entity.setOthers((double)0);
									}

									
									if(row.getCell(25)!=null)
									{
										entity.setUOM(row.getCell(25).getStringCellValue().trim());

									}else
									{
										entity.setUOM("NA");
									}	

									entity.setHSNCode(finalhsncode);

									if(row.getCell(26)!=null)
									{
										entity.setVAT((Double) row.getCell(26).getNumericCellValue());


									}else
									{
										entity.setVAT((double)0);
									}
									
									if(row.getCell(27)!=null)
									{
										entity.setVATCST((Double) row.getCell(27).getNumericCellValue());


									}else
									{
										entity.setVATCST((double)0);
									}
									
									if(row.getCell(28)!=null)
									{
										entity.setExcise((Double) row.getCell(28).getNumericCellValue());


									}else
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
										Double disamount =((round(damount,2))-((((round(damount,2))*(entity.getDiscount())))/100));
										tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFreight()+entity.getOthers();
									}
									else
									{
										tamount = amount;
									}
									transaction_value=transaction_value+round(tamount,2);
									round_off=(round_off+tds)+entity.getCGST()+entity.getIGST()+entity.getSGST()+entity.getState_com_tax()+tamount ;
									
									
									if(product1!=null)
									{
												
									if ((oldentry.getCustomer() != null)) {
										
										if(oldentry.getAdvance_payment()!=null && oldentry.getAdvance_payment()==true) {
										customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,
												oldentry.getCustomer().getCustomer_id(), (long) 5, ((double)oldentry.getAmount()+tds), (double)0, (long) 0);
										customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id, oldentry.getCustomer().getCustomer_id(),
												(long) 5, ((double)round_off+Newtds), (double)0, (long) 1);
										
										}
										else
										{
											customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,
													oldentry.getCustomer().getCustomer_id(), (long) 5, ((double)oldentry.getAmount()), (double)0, (long) 0);
											customerService.addcustomertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id, oldentry.getCustomer().getCustomer_id(),
													(long) 5, ((double)round_off), (double)0, (long) 1);
										}
									}  
									
								
									
									if (oldentry.getGst_applied()==1)
									 {
										 if(oldentry.getPayment_type()==1)
										 {
											 if((oldentry.getTransaction_value()!=null) && (oldentry.getTransaction_value()>0) && (transaction_value>0) && (oldentry.getTransaction_value()!=transaction_value))
											 {
												
												if(transaction_value>0)
												 {
													 SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",company_id);
														
														if(subledgercash!=null)
														{
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)(oldentry.getTransaction_value()),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)(transaction_value),(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
														}	
												 }
												
											 }
												
										 }
										 else
										 {
											 if((oldentry.getTransaction_value()!=null) && (oldentry.getTransaction_value()>0)&& (transaction_value>0) && (oldentry.getTransaction_value()!=transaction_value))
											    {
												 if(oldentry.getBank()!=null)
												 {
												 bank1=oldentry.getBank();
												 bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)0,(double)(oldentry.getTransaction_value()),(long) 0);
												 bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)0,(double)(transaction_value),(long) 1);
												 }
											 	}
											 
											 
											
										 }	 
										 
										 if((oldentry.getCgst()!=null)&&(oldentry.getCgst()>0)&&(cgst>0)&&(oldentry.getCgst()!=cgst))
											{
												SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",company_id);
												if(subledgercgst!=null)
												{
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getCgst(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											else if((oldentry.getCgst()!=null)&&(cgst>0) && (oldentry.getCgst()==0))
											{
												SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",company_id);
												if(subledgercgst!=null)
												{
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											if((oldentry.getSgst()!=null)&&(oldentry.getSgst()>0)&&(sgst>0)&&(oldentry.getSgst()!=sgst))
											{
												SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",company_id);
												
												if(subledgersgst!=null)
												{
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getSgst(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											else if((oldentry.getSgst()!=null)&&(sgst>0)&&(oldentry.getSgst()==0))
											{
                                              SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",company_id);
												
												if(subledgersgst!=null)
												{
											
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
												
											}
											
											if((oldentry.getIgst()!=null) && (oldentry.getIgst()>0) && (igst>0) && (oldentry.getIgst()!=igst))
											{
												SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",company_id);
												
												if(subledgerigst!=null)
												{
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getIgst(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											else if((oldentry.getIgst()!=null)&&(igst>0)&&(oldentry.getIgst()==0))
											{
                                             SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",company_id);
												
												if(subledgerigst!=null)
												{
											
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											
											if((oldentry.getState_compansation_tax()!=null) && (oldentry.getState_compansation_tax()>0) && (state_comp_tax>0)&&(oldentry.getState_compansation_tax()!=state_comp_tax))
											{
												SubLedger subledgercess = subledgerDAO.findOne("Output CESS",company_id);
												
												if(subledgercess!=null)
												{
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getState_compansation_tax(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											else if((oldentry.getState_compansation_tax()!=null) && (state_comp_tax>0) && (oldentry.getState_compansation_tax()==0))
											{
                                             SubLedger subledgercess = subledgerDAO.findOne("Output CESS",company_id);
												
												if(subledgercess!=null)
												{
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											
											if((oldentry.getTotal_vat()!=null) && (oldentry.getTotal_vat()>0) && (vat>0)&&(oldentry.getTotal_vat()!=vat))
											{
												SubLedger subledgervat = subledgerDAO.findOne("Output VAT",company_id);
												
												if(subledgervat!=null)
												{
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getTotal_vat(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											else if((oldentry.getTotal_vat()!=null) && (vat>0) && (oldentry.getTotal_vat()==0))
											{
                                              SubLedger subledgervat = subledgerDAO.findOne("Output VAT",company_id);
												
												if(subledgervat!=null)
												{
		
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											
											if((oldentry.getTotal_vatcst()!=null) && (oldentry.getTotal_vatcst()>0) && (vatcst>0)&&(oldentry.getTotal_vatcst()!=vatcst))
											{
												SubLedger subledgercst = subledgerDAO.findOne("Output CST",company_id);
												
												if(subledgercst!=null)
												{subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getTotal_vatcst(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											else if((oldentry.getTotal_vatcst()!=null) && (vatcst>0) && (oldentry.getTotal_vatcst()==0))
											{
                                             SubLedger subledgercst = subledgerDAO.findOne("Output CST",company_id);
												
												if(subledgercst!=null)
												{
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											
											if((oldentry.getTotal_excise()!=null) && (oldentry.getTotal_excise()>0) && (excise>0)&&(oldentry.getTotal_excise()!=excise))
											{
												SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",company_id);
												
												if(subledgerexcise!=null)
												{subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)oldentry.getTotal_excise(),(long) 0,null,oldentry,null,null,null,null,null,null,null,null,null,null);
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
											else if((oldentry.getTotal_excise()!=null) && excise>0 && (oldentry.getTotal_excise()==0))
											{
                                             SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",company_id);
												
												if(subledgerexcise!=null)
												{
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 1,null,oldentry,null,null,null,null,null,null,null,null,null,null);
												}
											}
									 }
									entity.setTransaction_amount(round(tamount,2));
									oldentry.setRound_off(round(round_off,2));
									oldentry.setAmount(round(round_off-tds,2));
									oldentry.setTds_amount(round(tds,2));
									oldentry.setTransaction_value(round(transaction_value,2));
									oldentry.setCgst(round(cgst,2));
									oldentry.setIgst(round(igst,2));
									oldentry.setSgst(round(sgst,2));
									oldentry.setState_compansation_tax(round(state_comp_tax,2));
									oldentry.setTotal_vat(round(vat,2));
									oldentry.setTotal_vatcst(round(vatcst,2));
									oldentry.setTotal_excise(round(excise,2));
									
                                    receiptService.updateReceiptThroughExcel(oldentry);
									entity.setReceipt_header_id(oldentry.getReceipt_id());
									receiptService.updateReceipt_product_detailsThroughExcel(entity);
									
									 if(repetProductafterunsuccesfullattempt==0)
                                     {
                                    	 
                                    	 sucount=sucount+1;
                                    	 m = 1;
                                    	successVocharList.add(receiptVoucherNofromExcel);
                                     }
									 
									}
									else
									{
										/*receiptService.changeStatusOfReceiptThroughExcel(oldentry);
										if(successVocharList!=null && successVocharList.size()>0)
										{
											sucount=sucount-1;
											failcount=failcount+1;
											m = 2;
											successVocharList.remove(receiptVoucherNofromExcel);
											failureVocharList.add(receiptVoucherNofromExcel);
										}*/
										
									}
										
									}
								/*break;*/
							  }
								
							/*  if(mainentryflag==true)
							  {	
								  break;
							  }*/
							  
							}
								
								
								
							
							 if(flag==false)
							{
								
								 if(!failureVocharList.contains(receiptVoucherNofromExcel))
								{
								 Boolean flag1 = false;
								
								 Receipt oldreceipt = receiptService.isExcelVocherNumberExist(receiptVoucherNofromExcel, company_id);
								 
									 if(oldreceipt !=null && oldreceipt.getExcel_voucher_no()!=null)
									 {
									 String str=oldreceipt.getExcel_voucher_no().trim();
										if(str.replace(" ","").trim().equalsIgnoreCase(receiptVoucherNofromExcel.replace(" ","").trim())) {
											flag1=true;
										  /*  break;*/
										}
									 }
							
								 
								 if(flag1==false)
								 {
									
									 
									 String finalhsncode="1";
									 
									 if(row.getCell(13)!=null)
									 {
									 Double hsncode= row.getCell(13).getNumericCellValue();
									 Integer hsncode1 = hsncode.intValue();
									 finalhsncode=hsncode1.toString().trim();
									 }
									 
								   if(row.getCell(0)==null)
									{
				
									}
									else
									{
										try {
											cnt=0;
											for(Customer customer:list)
											{
												
												String str=customer.getFirm_name().trim();
												if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(0).getStringCellValue().replace(" ","").trim())) {
													entry.setCustomer(customer);
													customer1=customer;
													count=count+1;
													cnt=1;
												    break;
												}
											}
											if(cnt==0){Err=true;ErrorMsgs.append(" Customer name is incorrect ");}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									entry.setCompany(user.getCompany());
									
									if(row.getCell(1)==null)
									{
										
									}
									else
									{
										try {
											cnt=0;
											for(SalesEntry salesEntry:saleslist)
											{
													String str = salesEntry.getVoucher_no().trim();
													if (str.replace(" ","").trim().equalsIgnoreCase(salesVoucherNofromExcel.replace(" ","").trim())) {
														entry.setSales_bill_id(salesEntry);
														count = count + 1;
														cnt=1;
														salesEntry1=salesEntry;
														break;
											}
											}
											if(cnt==0){Err=true;ErrorMsgs.append(" Voucher No is incorrect ");}
											}
											catch (Exception e) {
												e.printStackTrace();
											}
									}
									
									if(row.getCell(2)==null)
									{
										
									}
									else
									{
										try
										{
											cnt=0;
										for(SubLedger subledger:sublist)
										{
												String str = subledger.getSubledger_name().trim();
												if (str.replace(" ","").trim().equalsIgnoreCase(row.getCell(2).getStringCellValue().replace(" ","").trim())) {
													entry.setSubLedger(subledger);
													subledger1 = subledger;
													count = count + 1;
													cnt=1;
													break;
										}
										}
										if(cnt==0){Err=true;ErrorMsgs.append(" Subledger name is incorrect ");}
										}
										catch (Exception e) {
											//e.printStackTrace();
										}
									}
									
									
									 String remoteAddr = "";
									 remoteAddr = request.getHeader("X-FORWARDED-FOR");
							            if (remoteAddr == null || "".equals(remoteAddr)) {
							                remoteAddr = request.getRemoteAddr();
							            }
							            entry.setIp_address(remoteAddr);
							            entry.setCreated_by(user_id);
									
									entry.setExcel_voucher_no(receiptVoucherNofromExcel);
									
									if(row.getCell(31)!=null && row.getCell(31).getStringCellValue()!=null)
									{
										entry.setOther_remark(row.getCell(31).getStringCellValue().trim());
									}
									
									entry.setLocal_time(new LocalTime());
									
									if(row.getCell(5)!=null)
									{
										
										try {
											String str=row.getCell(5).getDateCellValue().toString();

											entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvert(str)));
							   }
										   catch (Exception e) {
										   e.printStackTrace();
										    }
										try {
											String str=row.getCell(5).getStringCellValue().toString();

											entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvertNew(str)));
							   }
										   catch (Exception e) {
										  // e.printStackTrace();
										    }
									}
									
									try {
										cnt=0;
										for(AccountingYear year_range:yearlist)
										{
											
											String str =year_range.getYear_range().trim();
											
											if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(4).getStringCellValue().replace(" ","").trim())) {
												entry.setAccountingYear(year_range);
												count=count+1;
												cnt=1;
												year_range1=year_range;
												if(entry.getDate()!=null)
												{
												entry.setVoucher_no(commonService.getVoucherNumber("RV", VOUCHER_RECEIPT, entry.getDate(),year_range.getYear_id(), company_id));
												}
											    break;
											}
										}
										if(cnt==0){Err=true;ErrorMsgs.append(" Accounting Year is incorrect ");}
									} catch (Exception e) {
										//e.printStackTrace();
									}
									
									if(row.getCell(6)!=null)
									{
										String cellval=row.getCell(6).getStringCellValue().trim();
										
										if(cellval.length() <1 )
										{
										
											paymentType=false;
											Err=true;ErrorMsgs.append(" Payment Type cannot be blank ");
										}
									if(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("Cash"))
									{
										entry.setPayment_type(1);
									}
									else if(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("Cheque")) {
										entry.setPayment_type(2);
									}else if(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("DD")) {
										
										entry.setPayment_type(3);
									}else if(row.getCell(6)!=null) {
										entry.setPayment_type(4);
									}
									}else if(row.getCell(6) ==null){
										
										Err=true;ErrorMsgs.append(" Payment Type cannot be blank ");
										paymentType=false;
									}
									
									if(row.getCell(6)!=null)
									{
									if(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("Cash"))
									{
										
									}
									else
									{
										if(row.getCell(7)!=null)
										{
										entry.setCheque_no(chequeNofromExcel);
										}
										if(row.getCell(8)!=null)
										{
											 try {
													entry.setCheque_date(ClassToConvertDateForExcell.dateConvert(row.getCell(8).getDateCellValue().toString()));

											   }
											   catch (Exception e) {
											    e.printStackTrace();
											    }
										}
										if(row.getCell(9)==null){invalidData=invalidData+"\n" + "Bank name is blank"; String[] banknameAndAcc = row.getCell(9).getStringCellValue().replace(" ","").trim().split("-");
										}
										if(row.getCell(9)!=null)
										{
											String[] banknameAndAcc = row.getCell(9).getStringCellValue().replace(" ","").trim().split("-");
											if(banknameAndAcc.length<2){invalidData=invalidData + "\n" +"Bank name is incorrect or blank";}
											String bankname = banknameAndAcc[0];
											String accno = banknameAndAcc[1];
											cnt=0;
											
										try {
											for(Bank bank:banklist)
											{
												
												String strbankname =bank.getBank_name().trim();
												String straccno = bank.getAccount_no().toString();
												
												
												if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno)) {
													entry.setBank(bank);
													bank1=bank;
													count=count+1;
													cnt=1;
												    break;
												}
											}
											if(cnt==0){Err=true;ErrorMsgs.append(" Bank name is incorrect ");}
										} catch (Exception e) {
											//e.printStackTrace();
										}
									    }
									}
									}
									
									

									if(row.getCell(12)!=null)
									{
									if(row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES"))
									{
									entry.setAdvance_payment(true);
									}
									else if(row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO"))
									{
										entry.setAdvance_payment(false);
									}
									}
									
									entry.setCreated_date(new LocalDate());
									
									if((row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("NO")))
									{
										entry.setGst_applied(2);
									
										if(row.getCell(12)!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES"))
										{
										if(row.getCell(28)!=null && row.getCell(28).getStringCellValue().trim().equalsIgnoreCase("YES"))
										{
											
											entry.setTds_paid(true);
											if(row.getCell(30)!=null)
											{
												tds=(Double) row.getCell(30).getNumericCellValue();
												
											}
											entry.setTds_amount(round(tds,2));
											entry.setAmount(round((Double)row.getCell(10).getNumericCellValue()-tds,2));
										}
										
										
										if(row.getCell(28)!=null && row.getCell(28).getStringCellValue().trim().equalsIgnoreCase("NO"))
										{
											
										entry.setTds_paid(false);
										entry.setTds_amount(round(tds,2));
										entry.setAmount(round((Double)row.getCell(10).getNumericCellValue()-tds,2));
										}
										
										/*if(row.getCell(29)==null)
										{
											
											entry.setTds_paid(true);
										if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
										{
											if(customer1.getTds_rate()!=null)
											{
											tds = ((Double)row.getCell(10).getNumericCellValue()*customer1.getTds_rate())/100;
											}
										
										}
										entry.setTds_amount(tds);
										entry.setAmount((Double)row.getCell(10).getNumericCellValue()-tds);
										}*/
									
										}
										
										if(row.getCell(12)!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO") && salesEntry1!=null)
										{
											
										entry.setAmount(round((Double)row.getCell(10).getNumericCellValue(),2));
										/**IF tds is already cut for sales entry then we are adding receipt against that sales entry then tds value for particular receipt is storing in database for showing tds against this receipt in receipt report & ledger report */
										if(salesEntry1 != null && salesEntry1.getTds_amount()!=null){	
										tds=(entry.getAmount()*salesEntry1.getTds_amount())/salesEntry1.getRound_off();
										}
										entry.setTds_amount(round(tds,2));	
										entry.setTds_paid(false); /**here we are giving tds paid = false because tds is already deducted for sales entry. this tds amount will required for report  receipt report & ledger report .*/
										
										if(entry.getAmount()>salesEntry1.getRound_off())
										{
											entry.setAdvance_payment(true);
											entry.setIs_salegenrated(true);
											entry.setIs_extraAdvanceReceived(true);
											
										}
										else
										{
											entry.setAdvance_payment(false);
										}
										
										
										}
									}
									
								    if((row.getCell(11)!=null)&&((row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("YES")))&&(subledger1==null))
									{
										entry.setGst_applied(1);
										String ProductName = row.getCell(14).getStringCellValue();
										cnt=0;
										for(Customer customer:list)
										{
											Boolean flag2 = false;
											if((customer1!=null)){
											if(  (customer1.getCustomer_id().equals(customer.getCustomer_id())))
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
													product1=product;
													count=count+1;
													cnt=1;
													flag2=true;
													break;

												}
												}
												else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
												{
													
													String strproductName = product.getProduct_name().trim();
													if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
													entity.setProduct_name(product.getProduct_name());
													entity.setProduct_id(product.getProduct_id());
													flag2=true;
													product1=product;
													count = count+1;
													cnt=1;
													break;
												}
												}
											}
											
											if(flag2==true)
											{
												break;
											}

										}
										catch (Exception e) {
											//e.printStackTrace();
										}
										
										}}else{break;}
										
										}
										
										if(cnt==0 && customer1!=null){Err=true;ErrorMsgs.append(" product name is incorrect ");}
										
										if(row.getCell(15)!=null)
										{
										entity.setQuantity(round((Double) row.getCell(15).getNumericCellValue(),2));
										}else
										{
											entity.setQuantity((double)0);
										}

										
										if(row.getCell(16)!=null)
										{
											entity.setRate(round((Double) row.getCell(16).getNumericCellValue(),2));
										}else
										{
											entity.setRate((double)0);
										}
										
										

										if(row.getCell(17)!=null)
										{
											entity.setDiscount(round((Double) row.getCell(17).getNumericCellValue(),2));
										}else
										{
											entity.setDiscount((double)0);
										}
										
										if(row.getCell(18)!=null)
										{
											entity.setCGST(round((Double) row.getCell(18).getNumericCellValue(),2));
										}else
										{
											entity.setCGST((double)0);
										}

										
										if(row.getCell(19)!=null)
										{
											entity.setIGST(round((Double) row.getCell(19).getNumericCellValue(),2));

										}else
										{
											entity.setIGST((double)0);
										}

										if(row.getCell(20)!=null)
										{
											entity.setSGST(round((Double) row.getCell(20).getNumericCellValue(),2));

										}else
										{
											entity.setSGST((double)0);
										}


										
										if(row.getCell(21)!=null)
										{
											entity.setState_com_tax(round((Double) row.getCell(21).getNumericCellValue(),2));

										}else
										{
											entity.setState_com_tax((double)0);
										}
										

										if(row.getCell(22)!=null)
										{
											entity.setLabour_charges(round((Double) row.getCell(22).getNumericCellValue(),2));

										}else
										{
											entity.setLabour_charges((double)0);
										}

										if(row.getCell(23)!=null)
										{
											entity.setFreight(round((Double) row.getCell(23).getNumericCellValue(),2));

										}else
										{
											entity.setFreight((double)0);
										}
										
										if(row.getCell(24)!=null)
										{
											entity.setOthers(round((Double) row.getCell(24).getNumericCellValue(),2));

										}else
										{
											entity.setOthers((double)0);
										}

										
										if(row.getCell(25)!=null)
										{
											entity.setUOM(row.getCell(25).getStringCellValue().trim());

										}else
										{
											entity.setUOM("NA");
										}	

										entity.setHSNCode(finalhsncode);

										if(row.getCell(26)!=null)
										{
											entity.setVAT(round((Double) row.getCell(26).getNumericCellValue(),2));


										}else
										{
											entity.setVAT((double)0);
										}
										
										if(row.getCell(27)!=null)
										{
											entity.setVATCST(round((Double) row.getCell(27).getNumericCellValue(),2));


										}else
										{
											entity.setVATCST((double)0);
										}
										
										if(row.getCell(28)!=null)
										{
											entity.setExcise(round((Double) row.getCell(28).getNumericCellValue(),2));


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
											System.out.println("the amount is "+amount);
											if(entity.getDiscount()!=0)
											{
												Double disamount =((round(damount,2))-((((round(damount,2))*(entity.getDiscount())))/100));
												tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFreight()+entity.getOthers();
											}
											else
											{
												tamount = amount;
											}
										transaction_value=round(tamount,2);
										round_off=cgst+igst+sgst+state_comp_tax+transaction_value ;
										
										if(row.getCell(12)!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES"))
										{
											if(row.getCell(29)!=null && row.getCell(29).getStringCellValue().trim().equalsIgnoreCase("YES"))
											{
												entry.setTds_paid(true);
												/*if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
												{
													if(customer1.getTds_rate()!=null)
													{
													tds = (transaction_value*customer1.getTds_rate())/100;
													}
												
												}*/
												if(row.getCell(30)!=null)
												{
													tds=(Double) row.getCell(30).getNumericCellValue();
													
												}
												entry.setTds_amount(round(tds,2));
												entry.setRound_off(round(round_off,2));
												entry.setAmount(round(round_off,2));
												
											}
											
											if(row.getCell(29)!=null && row.getCell(29).getStringCellValue().trim().equalsIgnoreCase("NO"))
											{
												
												entry.setTds_paid(false);
												entry.setTds_amount(round(tds,2));
												entry.setRound_off(round(round_off,2));
												entry.setAmount(round(round_off,2));
											}
											
											/*if(row.getCell(29)==null)
											{
												
												entry.setTds_paid(true);
												if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
												{
													if(customer1.getTds_rate()!=null)
													{
													tds = (transaction_value*customer1.getTds_rate())/100;
													}
												
												}
												entry.setTds_amount(tds);
												entry.setRound_off(round(round_off-tds,2));
												entry.setAmount(round(round_off-tds,2));
											}*/
											
										}
										else
										{
											entry.setTds_paid(false);
											entry.setTds_amount(round(tds,2));
											entry.setRound_off(round(round_off,2));
											entry.setAmount(round(round_off-tds,2));
										}
								
										entity.setTransaction_amount(round(transaction_value,2));
										entry.setTransaction_value(round(transaction_value,2));
										entry.setCgst(round(cgst,2));
										entry.setSgst(round(sgst,2));
										entry.setIgst(round(igst,2));
										entry.setState_compansation_tax(round(state_comp_tax,2));
										entry.setTotal_vat(round(vat,2));
										entry.setTotal_vatcst(round(vatcst,2));
										entry.setTotal_excise(round(excise,2));
										
									}
								   
								  
								    if((product1!=null) && entry.getAmount() !=null && entry.getAmount()>0&&(customer1!=null)&&(entry.getVoucher_no()!=null)&&(subledger1==null)&&(row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("YES"))&&(row.getCell(12)!=null) && (paymentType==true))
								    {
								    	System.out.println("The first if "+count);
								    	System.out.println("The count is "+count);
										if (((count == 5)&&(customer1!=null && salesEntry1!=null && bank1!=null && year_range1!=null && product1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))||
												((count == 4)&&(customer1!=null && salesEntry1!=null && year_range1!=null && product1!=null && entry.getPayment_type().equals(1)&& row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))
												||((count == 4)&&(customer1!=null && bank1!=null && year_range1!=null && product1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))
												||((count == 3)&&(customer1!=null && year_range1!=null && product1!=null && entry.getPayment_type().equals(1) && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))) 
										{
											entry.setFlag(true);
											sucount=sucount+1;
											successVocharList.add(receiptVoucherNofromExcel);
											repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											
											Long id1=null;
											
										    entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
											id1 = receiptService.saveReceiptThroughExcel(entry);
					                        entity.setReceipt_header_id(id1);
										    receiptService.saveReceipt_product_detailsThroughExcel(entity);
										    Receipt receipt = receiptService.getById(id1);
										
										try
										{
										if(customer1 != null){
											
											if(entry.getAdvance_payment()!=null && entry.getAdvance_payment()==true) {
											customerService.addcustomertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, customer1.getCustomer_id(), (long) 5, ((double)entry.getAmount()+entry.getTds_amount()),(double)0,(long) 1);
										     }
											else
											{
												customerService.addcustomertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, customer1.getCustomer_id(), (long) 5, ((double)entry.getAmount()),(double)0,(long) 1);
											}
										}
										
										}
										catch(Exception e)
										{
											//e.printStackTrace();
										}
										
										try
										{	
										
										if(tds>0 && entry.getTds_paid()!=null && entry.getTds_paid()==true)
										{
											SubLedger subledgerTds = subledgerDAO.findOne("TDS Receivable", company_id);
											if (subledgerTds != null) {
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,
														subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)tds, (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
											}
										}
									
										}
										catch(Exception e)
										{
											//e.printStackTrace();
										}
										
									    try
										{		
										 if ((row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("YES"))&&(subledger1==null))
										 {
											 if((row.getCell(6)!=null)&&(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("Cash")))
											 {
												 if(transaction_value>0)
												 {
													 SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",company_id);
														
														if(subledgercash!=null)
														{
															subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)(transaction_value),(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
														}	
												 }
													
											 }
											 else
											 {
												 	if((transaction_value>0)&&(bank1!=null))
												 	{
												 		bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)0,(double)(transaction_value),(long) 1);
												 	}
												 	
											 }	
											 if(cgst>0)
												{
													SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",company_id);
													if(subledgercgst!=null)
													{
														subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
													}
												}
												if(sgst>0)
												{
													SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",company_id);
													
													if(subledgersgst!=null)
													{
														subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
													}
												}
												if(igst>0)
												{
													SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",company_id);
													
													if(subledgerigst!=null)
													{
														subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
													}
												}
												if(state_comp_tax>0)
												{
													SubLedger subledgercess = subledgerDAO.findOne("Output CESS",company_id);
													
													if(subledgercess!=null)
													{
														subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if(vat>0)
												{
													SubLedger subledgervat = subledgerDAO.findOne("Output VAT",company_id);
													
													if(subledgervat!=null)
													{
														subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if(vatcst>0)
												{
													SubLedger subledgercst = subledgerDAO.findOne("Output CST",company_id);
													
													if(subledgercst!=null)
													{
														subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
													}
												}
												
												if(excise>0)
												{
													SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",company_id);
													
													if(subledgerexcise!=null)
													{
														subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
													}
												}
										 }
										}
										catch(Exception e) {
											
											e.printStackTrace();
										}
											
										}
										else {
											
											if(!successVocharList.contains(receiptVoucherNofromExcel))
											{
												maincount=maincount+1;
												failcount=failcount+1;
						                        failureVocharList.add(receiptVoucherNofromExcel);
						                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											}
											
										}
										     
									
								    }
								  
								    
								    
								    else if((product1==null)&& entry.getAmount() !=null && entry.getAmount()>0&&(customer1!=null)&&(entry.getVoucher_no()!=null) && (subledger1==null)&&(row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("NO"))&&(row.getCell(12)!=null) && (paymentType==true))   
								    {
								    	System.out.println("The seciond if "+count);
								    	if (((count == 2)&&(customer1!=null && year_range1!=null && entry.getPayment_type().equals(1) && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))||((count == 4)&&(customer1!=null && salesEntry1!=null && bank1!=null && year_range1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))||
								    		((count == 3)&&(customer1!=null && salesEntry1!=null && year_range1!=null && entry.getPayment_type().equals(1) && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))||
								    		((count == 3)&&(customer1!=null && bank1!=null && year_range1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))
								    			) 
								    	{
											entry.setFlag(true);
											sucount=sucount+1;
											successVocharList.add(receiptVoucherNofromExcel);
											repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											
											if(row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO"))
											{
											if(entry.getAmount()>salesEntry1.getRound_off())
											{
												 entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
											}
											else
											{
												 entry.setEntry_status(MyAbstractController.ENTRY_NIL);
											}
											}
											if(row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES"))
											{
												entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
											}
											
									    	Long id= receiptService.saveReceiptThroughExcel(entry);
									    	Receipt receipt1 = receiptService.getById(id);
									    	
									    	if(entry.getSales_bill_id() != null){
												SalesEntry salesEntry = salesEntryDao.findOneWithAll(entry.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
												Double total=(double)0;
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
													salesEntryDao.updateSalesEntryThroughExcel(salesEntry);
												} catch (Exception e) {
													e.printStackTrace();
												}

											}
									    try
										{
										if(customer1 != null){
											
											if(entry.getAdvance_payment()!=null && entry.getAdvance_payment()==true) {
											customerService.addcustomertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, customer1.getCustomer_id(), (long) 5, ((double)entry.getAmount()+entry.getTds_amount()),(double)0,(long) 1);
										      }
											else
											{
												customerService.addcustomertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, customer1.getCustomer_id(), (long) 5, ((double)entry.getAmount()),(double)0,(long) 1);
											}
										}
										
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
									    
									    try
										{	
										
									    if(tds>0 && entry.getTds_paid()!=null && entry.getTds_paid()==true)
										{
											SubLedger subledgerTds = subledgerDAO.findOne("TDS Receivable", company_id);
											if (subledgerTds != null) {
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,
														subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)tds, (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
											}
										}
									
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}						
										
										    try
											{	
											if((row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("NO")))
											{
											 if((row.getCell(6)!=null)&&(row.getCell(6).getStringCellValue().trim().equalsIgnoreCase("Cash")))
											 {
												
												    SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",company_id);
													if(subledgercash!=null)
													{
													subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)entry.getAmount(),(long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
											
													}
											 }
											 else
												 {
												   if(bank1!=null)
											       {
												   bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)0,(double)entry.getAmount(),(long) 1);
											       }
												   
												 }
										    }
											}
										 catch(Exception e)
										 {
											 e.printStackTrace();
										 }
											
										} 
								    	 else {
												
								    		 if(!successVocharList.contains(receiptVoucherNofromExcel))
												{
													maincount=maincount+1;
													failcount=failcount+1;
							                        failureVocharList.add(receiptVoucherNofromExcel);
							                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
												}
						                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											}
				    			 }
								    else if(subledger1!=null && row.getCell(10)!=null && (Double)row.getCell(10).getNumericCellValue()!=null && customer1==null && product1==null && entry.getVoucher_no()!=null && (paymentType==true))
									{
								    	System.out.println("The third if "+count);
										
										entry.setAmount((Double)row.getCell(10).getNumericCellValue());
										if (((count == 2)&&(subledger1!=null && year_range1!=null))||((count == 3)&&(subledger1!=null && year_range1!=null && bank1!=null)))
										{
											entry.setFlag(true);
											sucount=sucount+1;
											successVocharList.add(receiptVoucherNofromExcel);
											repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											
											entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
											Long id =receiptService.saveReceiptThroughExcel(entry);
											Receipt receipt1 = receiptService.getById(id);
											
											try
											{
											 if(subledger1 != null && row.getCell(10)!=null){
												 
												 subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledger1.getSubledger_Id(),(long) 2,(double)entry.getAmount(),(double)0,(long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);
											 }
											 if (entry.getPayment_type().equals(1)){
													SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand", company_id);
													if (subledgercash != null) {
														subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,
																subledgercash.getSubledger_Id(), (long) 2, (double) 0, (double)entry.getAmount(), (long) 1,null,receipt1,null,null,null,null,null,null,null,null,null,null);

													}
												} else {
													if(entry.getBank()!=null)
													{
													bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, entry.getBank().getBank_id(),
															(long) 3, (double) 0, (double)entry.getAmount(), (long) 1);
													}
												}
											}
											 catch(Exception e)
												{
													
												}
											
										} 
										else {
											
											if(!successVocharList.contains(receiptVoucherNofromExcel))
											{
												maincount=maincount+1;
												failcount=failcount+1;
						                        failureVocharList.add(receiptVoucherNofromExcel);
						                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											}
										}
										
									}
									else {
										
										if(!successVocharList.contains(receiptVoucherNofromExcel))
										{
											maincount=maincount+1;
											failcount=failcount+1;
					                        failureVocharList.add(receiptVoucherNofromExcel);
					                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
										}
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
				return new ModelAndView("redirect:/receiptList");
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
				return new ModelAndView("redirect:/receiptList");
			}
			else
			{
				successmsg.append("You are inserting duplicate records, please check excel file");
				String successmsg1=successmsg.toString();
				session.setAttribute("filemsg", successmsg1);
				return new ModelAndView("redirect:/receiptList");
			}
			
		} else {
			successmsg.append("Please enter required and valid data in columns");

			if(!invalidData.trim().equals("")){
				
				successmsg.append(invalidData);
		}
		
			String successmsg1=successmsg.toString();
			session.setAttribute("filemsg", successmsg1);
			return new ModelAndView("redirect:/receiptList");

		}
	}	
	
	
	
	
	@RequestMapping(value = "receiptFailure", method = RequestMethod.GET)
	public ModelAndView receiptFailure(HttpServletRequest request, HttpServletResponse response) {
		 System.out.println("Failed and set flag false");
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
        flag = false;
        if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
        List<YearEnding>  yearEndlist = yearService.findAllYearEnding(company_id);
		//model.addObject("receiptList", receiptService.findAllRecepitOfCompany(company_id,false,yearId));
		model.addObject("flagFailureList", false);
		model.addObject("yearEndlist", yearEndlist);
		model.setViewName("/transactions/receiptList");
		return model;
		}
	
	

	 @RequestMapping(value ="editproductdetailforReceipt", method = RequestMethod.GET)
		public ModelAndView editproductdetailforReceipt(@RequestParam("id") long id, 
				HttpServletRequest request, 
				HttpServletResponse response){
			HttpSession session = request.getSession(true);
			ModelAndView model = new ModelAndView();
			Receipt_product_details entry = new Receipt_product_details();
			User user = new User();
			GstTaxMaster productgst =new GstTaxMaster();
			user = (User) session.getAttribute("user");
			entry= receiptService.editproductdetailforReceipt(id);
			Receipt receipt= new Receipt();
			receipt= receiptService.findOneWithAll(entry.getReceipt_header_id());

			Product productgst1 = productService.findOneView(entry.getProduct_id());
			if(productgst1.getHsn_san_no()!=null)
			{
				productgst=gstService.getHSNbyDate(receipt.getDate(),productgst1.getHsn_san_no());
			}
			else
			{
				productgst=gstService.getHSNbyDate(receipt.getDate(),entry.getHSNCode());
			}
			model.addObject("entry", entry);
			model.addObject("receipt", receipt);
			model.addObject("productgst", productgst);
			model.addObject("stateId",user.getCompany().getState().getState_id());

			model.setViewName("/transactions/editproductdetailforReceipt");	
			return model;		
		}
	 
	 @RequestMapping(value ="saveproductdetailforReceipt", method = RequestMethod.POST)
		public synchronized ModelAndView saveproductdetailforReceipt(@ModelAttribute("entry")Receipt_product_details entry, 
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
		/*	Double tds=(double)0;*/
			
			
			
			 ModelAndView model = new ModelAndView();
			 editProValidator.validate(entry, result);
				if(result.hasErrors()){

					model.setViewName("/transactions/editproductdetailforReceipt");			
					return model;
				}
				else
				{
					Receipt_product_details receiptdetails = new Receipt_product_details();
					
					receiptdetails= receiptService.editproductdetailforReceipt(entry.getReceipt_detail_id());
					Receipt receipt = new Receipt();
					
					try {			
						receipt= receiptService.findOneWithAll(entry.getReceipt_header_id());
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
			
					if(receipt.getTransaction_value()!=null) 
					{
					transaction_value= receipt.getTransaction_value();
					}
					if(receipt.getCgst()!=null) 
					{
					cgst=receipt.getCgst();
					}
					if(receipt.getIgst()!=null) 
					{
					igst=receipt.getIgst();
					}
					if(receipt.getSgst()!=null) 
					{
					sgst=receipt.getSgst();
					}
					if(receipt.getState_compansation_tax()!=null) 
					{
					state_comp_tax=receipt.getState_compansation_tax();
					}
					
					if(receipt.getAmount()!=null) 
					{
						round_off=receipt.getAmount();
					}
					
				/*	if(receipt.getTds_amount()!=null) 
					{
						tds=receipt.getTds_amount();
					}*/
					
					if(receipt.getTotal_vat()!=null) 
					{
					vat=receipt.getTotal_vat();
					}
					if(receipt.getTotal_vatcst()!=null) 
					{
					vatcst=receipt.getTotal_vatcst();
					}
					if(receipt.getTotal_excise()!=null) 
					{
					excise=receipt.getTotal_excise();
					}
					
					if(receiptdetails.getCGST()!=null)
					{
					CGST=receiptdetails.getCGST();
					}
					
					if(receiptdetails.getIGST()!=null) 
					{
					IGST=receiptdetails.getIGST();
					}
					if(receiptdetails.getSGST()!=null)
					{
					SGST=receiptdetails.getSGST();
					}
					if(receiptdetails.getState_com_tax()!=null)
					{
					state_com_tax=receiptdetails.getState_com_tax();
					}
					if(receiptdetails.getTransaction_amount()!=null)
					{
					transaction_amount=receiptdetails.getTransaction_amount();
					}
					
					if(receiptdetails.getVAT()!=null)
					{
					VAT=receiptdetails.getVAT();
					}
					if(receiptdetails.getVATCST()!=null)
					{
					VATCST=receiptdetails.getVATCST();
					}
					if(receiptdetails.getExcise()!=null) 
					{
					Excise=receiptdetails.getExcise();
					}
					
					
					if(VAT==0 && VATCST==0 && Excise==0)
					{
					cgst=cgst-CGST;
					igst=igst-IGST;
					sgst=sgst-SGST;
					state_comp_tax= state_comp_tax-state_com_tax;
					
					transaction_value=transaction_value-transaction_amount;
					roundamount=CGST+IGST+SGST+state_com_tax+transaction_amount;
					round_off=round_off-roundamount;
					
					cgst=cgst+entry.getCGST();
					igst=igst+entry.getIGST();
					sgst=sgst+entry.getSGST();
					state_comp_tax=state_comp_tax+entry.getState_com_tax();
					amount=(entry.getRate()*entry.getQuantity())+entry.getFreight()+entry.getLabour_charges()+entry.getOthers();
					damount=entry.getRate()*entry.getQuantity();
					
					if(entry.getDiscount()!=null && entry.getDiscount()!=0) {
						disamount=(damount -((damount*entry.getDiscount())/100));
						new_transaction_amount=disamount+entry.getFreight()+entry.getLabour_charges()+entry.getOthers();
					}
					else
					{
						new_transaction_amount=amount;
					}
					
					transaction_value=transaction_value+new_transaction_amount;
					new_round_amount=entry.getCGST()+entry.getIGST()+entry.getSGST()+entry.getState_com_tax()+new_transaction_amount;
					round_off=round_off+new_round_amount;
					
					
					}
					else
					{
						vat=vat-VAT;
						vatcst=vatcst-VATCST;
						excise=excise-Excise;
						//calculate transaction and round off value,
						transaction_value=transaction_value-transaction_amount;
						roundamount=VAT+VATCST+Excise+transaction_amount;
						round_off=round_off-roundamount;
					}
						
					if ((receipt.getAmount() != round_off)) {
						if ((receipt.getCustomer() != null)
								&& (receipt.getAmount() != round_off)) {
							customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,
									receipt.getCustomer().getCustomer_id(), (long) 5, (double)receipt.getAmount(), (double)0, (long) 0);
							customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id, receipt.getCustomer().getCustomer_id(),
									(long) 5,(double)round_off, (double)0, (long) 1);
							
						} 
					}
					
					if(receipt.getGst_applied()!=null && receipt.getPayment_type()!=null)
					{
					if (receipt.getGst_applied()==1)
					 {
						 if(receipt.getPayment_type()==1)
						 {
								if(transaction_value>0)
								 {
									 SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",company_id);
										
										if(subledgercash!=null)
										{
											subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)receipt.getTransaction_value(),(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
											subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)transaction_value,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
										}	
								 }
						 }
						 else
						 {
								 if(receipt.getBank()!=null)
								 {
								 
								 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,receipt.getBank().getBank_id(),(long) 3,(double)0,(double)receipt.getTransaction_value(),(long) 0);
								 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,receipt.getBank().getBank_id(),(long) 3,(double)0,(double)transaction_value,(long) 1);
								 }
							 				
						 }	 
						 
						 SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",company_id);
							if(subledgercgst!=null)
							{
								if(receipt.getCgst()!=null)
								{
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)receipt.getCgst(),(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
								}
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							}
						
							SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",company_id);
							
							if(subledgersgst!=null)
							{
								if(receipt.getSgst()!=null)
								{
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)receipt.getSgst(),(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
								}
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							}
						
							SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",company_id);
							
							if(subledgerigst!=null)
							{
								if(receipt.getIgst()!=null)
								{
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)receipt.getIgst(),(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
								}
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							}
					
							SubLedger subledgercess = subledgerDAO.findOne("Output CESS",company_id);
							
							if(subledgercess!=null)
							{
								if(receipt.getState_compansation_tax()!=null)
								{
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)receipt.getState_compansation_tax(),(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
								}
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							}
						
							SubLedger subledgervat = subledgerDAO.findOne("Output VAT",company_id);
							
							if(subledgervat!=null)
							{
								if(receipt.getTotal_vat()!=null)
								{
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)receipt.getTotal_vat(),(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
								}
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							}
					
							SubLedger subledgercst = subledgerDAO.findOne("Output CST",company_id);
							
							if(subledgercst!=null)
							{
								if(receipt.getTotal_vatcst()!=null)
								{
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)receipt.getTotal_vatcst(),(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
								}
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							}
						
							SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",company_id);
							
							if(subledgerexcise!=null)
							{   
								if(receipt.getTotal_excise()!=null)
								{
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)receipt.getTotal_excise(),(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
								}
								subLedgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							}
					 }
					
				}
					
					receipt.setCgst(round(cgst,2));
					receipt.setIgst(round(igst,2));
					receipt.setSgst(round(sgst,2));
					receipt.setState_compansation_tax(round(state_comp_tax,2));
					
					
					receipt.setTotal_vat(vat);
					receipt.setTotal_vatcst(vatcst);
					receipt.setTotal_excise(excise);
					
					receipt.setTransaction_value(transaction_value);
					receipt.setRound_off(round_off);
					receipt.setAmount(round_off);
					
					receiptService.updateReceipt(receipt);
					receiptdetails.setQuantity(entry.getQuantity());
					receiptdetails.setRate(entry.getRate());
					receiptdetails.setDiscount(entry.getDiscount());
					receiptdetails.setCGST(entry.getCGST());
					receiptdetails.setIGST(entry.getIGST());
					receiptdetails.setSGST(entry.getSGST());
					receiptdetails.setState_com_tax(entry.getState_com_tax());
					receiptdetails.setLabour_charges(entry.getLabour_charges());
					receiptdetails.setFreight(entry.getFreight());
					receiptdetails.setOthers(entry.getOthers());
					receiptdetails.setUOM(entry.getUOM());
					receiptdetails.setVAT(entry.getVAT());
					receiptdetails.setVATCST(entry.getVATCST());
					receiptdetails.setExcise(entry.getExcise());
					receiptdetails.setTransaction_amount(new_transaction_amount);
					receiptService.updateReceipt_product_details(receiptdetails);
					return new ModelAndView("redirect:/editReceipt?id="+receipt.getReceipt_id());
				}
		}
	
	 @RequestMapping(value="getPaidTDS", method=RequestMethod.POST)
		public @ResponseBody Double getPaidTDS(@RequestParam("billid") long billid,HttpServletRequest request, HttpServletResponse response)
		{
		 return receiptService.findpaidtds(billid);		
		}
	 
	 public static Double round(Double d, int decimalPlace) {
	    	DecimalFormat df = new DecimalFormat("#");
			df.setMaximumFractionDigits(decimalPlace);
			return new Double(df.format(d));
	    }
	 

@RequestMapping(value="getOpeningBalanceforreceipt", method=RequestMethod.GET)
		public @ResponseBody Double  getOpeningBalance(@RequestParam("customerid") Long customerid,HttpServletRequest request, HttpServletResponse response)
		{
		 
	Company company=null;
	HttpSession session = request.getSession(true);
	 long company_id=(long)session.getAttribute("company_id");
	 Double debitbalance=0.0;
	 Double creditbalance=0.0;
	 Double total=0.0;
		Double amount=0.0;
		try {
			company = companyService.getCompanyWithCompanyStautarType(company_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalDate date=company.getOpeningbalance_date();
	
		OpeningBalances   receiptopening = openingbalances.isOpeningBalancePresentReceipt(date,company_id,customerid); //subLedgerService.findOne("Cash In Hand", company_id);
	  
		if(receiptopening.getCredit_balance()!=null && receiptopening.getDebit_balance()!=null)
		{
		creditbalance = receiptopening.getCredit_balance();
		debitbalance = receiptopening.getDebit_balance();
		}
		if(debitbalance>0)
		{
		
		 List<Receipt> recceiptaginstopening= receiptService.getAllOpeningBalanceAgainstReceipt(customerid, company_id);
		  
		  for(Receipt receipt:recceiptaginstopening) {
		  amount=amount+receipt.getAmount(); 
		  }
		  List<CreditNote> creditnoteagainstopening=creditService.getAllOpeningBalanceAgainstcreditnote(customerid, company_id);
		  for(CreditNote creditnote:creditnoteagainstopening) {
			  amount=amount+creditnote.getRound_off(); 
			  }
		
		if (creditbalance > 0) {
			total=creditbalance;
		} else {
			total=debitbalance;
		}
		
		}
		 		total = total-amount;
		
		return total;
	}
}