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
import com.fasset.controller.validators.EditProductValidator;
import com.fasset.controller.validators.PurchaseEntryValidator;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.Product;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.PurchaseEntryProductEntityClass;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Service;
import com.fasset.entities.Stock;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.TaxMaster;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.PurchaseForm;
import com.fasset.report.controller.ClassToConvertDateForExcell;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.IDebitNoteService;
import com.fasset.service.interfaces.IDeducteeService;
import com.fasset.service.interfaces.IGstTaxMasterService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IProductService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.IStockService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.fasset.service.interfaces.ITDSType;
import com.fasset.service.interfaces.ITaxMasterService;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IYearEndingService;
/**
 * 
 * 
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class PurchaseEntryControllerMVC extends MyAbstractController{    	
	
	@Autowired
	private IPurchaseEntryService purEntryService ;
	
	@Autowired
	private PurchaseEntryValidator entryValidator ;
	
	@Autowired
	private ISuplliersService suplliersService ;
	@Autowired
	private IDeducteeService deducteeService ;
	
	@Autowired
	ISubLedgerService subledgerService;
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private IPaymentService paymentService;
	
	@Autowired
	private IAccountingYearService accountingYearService;
	
	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private ISubLedgerDAO subLedgerDAO;
	
	@Autowired
	private EditProductValidator editProValidator ;
	
	@Autowired
	private IStockService stockService;
	
	@Autowired
	private ITaxMasterService taxservice;
	
	@Autowired
	private IOpeningBalancesService openingbalances;
	
	@Autowired
	private IDebitNoteService debitService;
	@Autowired
	private IGstTaxMasterService gstService;
	@Autowired
	private IYearEndingService yearService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IQuotationService quoteservice;

	@Autowired
	private IClientSubscriptionMasterService subscribeservice;
	
	@Autowired
	private ISalesEntryService SalesEntryService;
	
	@Autowired	
	private ICreditNoteService creditService;
	
	@Autowired
	private IReceiptService receiptService ;
	
	private Long purchase_id =null;
	Boolean flag = null; // for maintaining the user on purchaseEntryList.jsp after delete and view
	
    private List<String> successVocharList = new ArrayList<String>();
		
	private List<String> failureVocharList = new ArrayList<String>();
	
	@RequestMapping(value = "purchaseEntry", method = RequestMethod.GET)
	public ModelAndView purchaseEntry(HttpServletRequest request, HttpServletResponse response) {		
        User user = new User();
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		user = (User) session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		long company_id=(long)session.getAttribute("company_id");
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		ModelAndView model = new ModelAndView();
		if(AccYear==-1){
			yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		}else{
			yearList = accountingYearService.findAccountRangeOfCompany(strLong);
		 
		}
		System.out.println("The session acc year id is" +session.getAttribute("AccountingYear"));
		System.out.println("The yearrange id is" +yearrange);
		System.out.println("The company_id id is" +company_id);
		System.out.println("The yearlist size is" +yearList.size());
		if(yearList.size()!=0)
		{
		model.addObject("voucherrange", companyService.getVoucherRange(company_id));
		model.addObject("yearList", yearList);
		model.addObject("supplierList", suplliersService.findAllSuppliersOfCompany(company_id));
		
		model.addObject("productList", productService.findAllProductsOfCompany(company_id));
		model.addObject("entry", new PurchaseEntry());
		model.addObject("stateId",user.getCompany().getState().getState_id());
		
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
		 if ((Integer)session.getAttribute("isMobile")!=null) {
			 model.setViewName("/transactions/mobile/mobileViewForPurchase");
		 }
		 else
		 {
			 model.setViewName("/transactions/purchaseEntry");
		 }
		
		
		return model;
		}
		else
		{
			session.setAttribute("msg","Your account is closed");
			return new ModelAndView("redirect:/purchaseEntryList");
			
		}
		
	}
	
	@RequestMapping(value = "savePurchaseEntry", method = RequestMethod.POST)
	public synchronized ModelAndView savePurchaseEntry(@ModelAttribute("entry")PurchaseEntry newentry, 
			BindingResult result, 
			HttpServletRequest request, 
			HttpServletResponse response) {	
		Long save_id = newentry.getSave_id();
		
		User user = new User();
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		user = (User)session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		long company_id=(long)session.getAttribute("company_id");
System.out.println("The year range selected is " +yearrange);
		ModelAndView model = new ModelAndView();
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		newentry.setSupplier_bill_date(new LocalDate(newentry.getSupplier_bill_date()));
	    entryValidator.validate(newentry, result);		
		if(result.hasErrors()){
		    newentry.setRound_off((double)0);
		    newentry.setCgst((double)0);
		    newentry.setSgst((double)0);
		    newentry.setIgst((double)0);
		    newentry.setTransaction_value((double)0);
		    newentry.setState_compansation_tax((double)0);	
		    if(AccYear==-1){
				yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
			}else{
				yearList = accountingYearService.findAccountRangeOfCompany(strLong);
			 
			}
			model.addObject("voucherrange", companyService.getVoucherRange(company_id));		    
		    model.addObject("stateId",user.getCompany().getState().getState_id());
		  //  model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
		    model.addObject("yearList",yearList);
		    model.addObject("supplierList", suplliersService.findAllSuppliersOfCompany(company_id));
			model.addObject("productList", productService.findAllProductsOfCompany(company_id));
			model.setViewName("/transactions/purchaseEntry");			
			return model;
		}
		else{
			String msg = "";
			Long id = null;
			try{
				if(newentry.getPurchase_id()!= null){
					long pid = newentry.getPurchase_id();
					if(pid > 0){
						purchase_id=pid;
						newentry.setCompany(user.getCompany());
						newentry.setUpdated_by(user_id);
						purEntryService.update(newentry);
						msg ="Purchase Entry Updated successfully";
					}
				}
				else{
					newentry.setCompany(user.getCompany());
					newentry.setFlag(true);
					
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            newentry.setIp_address(remoteAddr);
			        newentry.setCreated_by(user_id);
					newentry.setVoucher_no(commonService.getVoucherNumber("PE", VOUCHER_PURCHASE_ENTRY, newentry.getSupplier_bill_date(),newentry.getAccounting_year_id(), user.getCompany().getCompany_id()));					//newentry.setVoucher_no(commonService.getVoucherNumber("PE", VOUCHER_PURCHASE_ENTRY, newentry.getSupplier_bill_date(),newentry.getAccounting_year_id(), user.getCompany().getCompany_id()));
					System.out.println("The voucher No is " +newentry.getVoucher_no());
					id = purEntryService.savePurchaseEntry(newentry);
					purchase_id=id;
					msg="Purchase Entry Saved Successfully" ;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(save_id==0){
				PurchaseEntry entry = new PurchaseEntry();
				try {				
					
					entry= purEntryService.findOneWithAll(purchase_id);
					
					if(entry.getSupplier()!=null)
					{
					entry.setSupplier_id(entry.getSupplier().getSupplier_id());
					}
					if(entry.getSubledger()!=null)
					{
					entry.setSubledger_Id(entry.getSubledger().getSubledger_Id());	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			
				 if(AccYear==-1){
						yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
					}else{
						yearList = accountingYearService.findAccountRangeOfCompany(strLong);
					 
					}
				model.addObject("voucherrange", companyService.getVoucherRange(company_id));
				model.addObject("suppilerproductList", purEntryService.findAllPurchaseEntryProductEntityList(purchase_id));
				model.addObject("supplierList", suplliersService.findAllSuppliersOfCompany(company_id));
				model.addObject("productList", productService.findAllProductsOfCompany(company_id));
				model.addObject("entry", entry);
				model.addObject("successMsg", msg);
				model.addObject("yearList",yearList);
				//model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
				model.addObject("stateId",user.getCompany().getState().getState_id());
				model.setViewName("/transactions/purchaseEntry");			
			}
			else{   
				msg="Purchase Entry Saved successfully";
				session.setAttribute("msg", msg);
				return new ModelAndView("redirect:/purchaseEntryList");
			}
			return model;
		}
	}
	
	@RequestMapping(value = "purchaseEntryList", method = RequestMethod.GET)
	public ModelAndView purchaseEntryList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		boolean importfail=false;
		boolean importflag = false;
		User user = new User();
		user = (User) session.getAttribute("user");
		Company company=null;
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
		if((String) session.getAttribute("filemsg")!=null){
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
		if(purEntryService.findAllPurchaseEntryOfCompany(company_id,false).size()!=0){
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
		model.addObject("importflag", importflag);
		model.addObject("entryList", purEntryService.findAllPurchaseEntryOfCompany(company_id,true));
		model.addObject("importfail", importfail);
		model.addObject("flagFailureList", true);
		model.addObject("yearEndlist", yearEndlist);
		model.addObject("company", company);
		

		 if ((Integer)session.getAttribute("isMobile")!=null) {
			 model.addObject("entryList", purEntryService.findAllPurchaseEntryOfCompanyForMobile(company_id, true,yearId));
			 model.setViewName("/transactions/mobile/mobilePurchaseEntryList");
		 }
		 else
		 {
			 model.addObject("entryList", purEntryService.findAllPurchaseEntryOfCompany(company_id,true));
				model.setViewName("/transactions/purchaseEntryList");
		 }
			
		
		
		return model;
	}
	
	@RequestMapping(value = "editPurchaseEntry", method = RequestMethod.GET)
	public synchronized ModelAndView editPurchaseEntry(@RequestParam("id") long id,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		User user = new User();
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		user = (User)session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();		
		PurchaseEntry entry = new PurchaseEntry();
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		purchase_id=id;
		
		try {			
			entry= purEntryService.findOneWithAll(id);
			if((entry.getPayments() == null || entry.getPayments().size() == 0)&&(entry.getDebitNote() == null || entry.getDebitNote().size() == 0)) {
				if(entry.getSupplier()!=null)
				{
				entry.setSupplier_id(entry.getSupplier().getSupplier_id());
				}
				if(entry.getSubledger()!=null)
				{
				entry.setSubledger_Id(entry.getSubledger().getSubledger_Id());
				}
				 if(AccYear==-1){
						yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
					}else{
						yearList = accountingYearService.findAccountRangeOfCompany(strLong);
					 
					}
				model.addObject("voucherrange", companyService.getVoucherRange(company_id));
				model.addObject("suppilerproductList", purEntryService.findAllPurchaseEntryProductEntityList(id));
				model.addObject("supplierList", suplliersService.findAllSuppliersOfCompany(company_id));
				model.addObject("productList", productService.findAllProductsOfCompany(company_id));
				model.addObject("entry", entry);
				//model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
				model.addObject("yearList", yearList);
				model.addObject("stateId",user.getCompany().getState().getState_id());
				model.setViewName("/transactions/purchaseEntry");				
			}
			else if(entry.getPayments() != null && entry.getPayments().size() != 0){
				session.setAttribute("errorMsg", "You can't edit purchase voucher as payment is already generated for this purchase voucher");
				model.setViewName("redirect:/purchaseEntryList");
			}
			else if(entry.getDebitNote() != null && entry.getDebitNote().size() != 0){
				session.setAttribute("errorMsg", "You can't edit purchase voucher as debit note is already generated for this purchase voucher");
				model.setViewName("redirect:/purchaseEntryList");
			}
			
		} catch (Exception e) {			
			e.printStackTrace();
		}			
		return model;
	}
	
	@RequestMapping(value = "viewPurchaseEntry", method = RequestMethod.GET)
	public ModelAndView viewPurchaseEntry(@RequestParam("id") long id,
			HttpServletRequest request, 
			HttpServletResponse response) throws MyWebException {
		
		ModelAndView model = new ModelAndView();
		PurchaseEntry entry = new PurchaseEntry();
		
		try {
			entry = purEntryService.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double closingbalance=0;
		if(entry.getSupplier()!=null)
		{
			if(entry.getCompany()!=null && entry.getAccountingYear()!=null && entry.getSupplier()!=null && entry.getSupplier_bill_date()!=null)
			{
			closingbalance=openingbalances.getclosingbalance(entry.getCompany().getCompany_id(),entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),entry.getSupplier().getSupplier_id(),4);
			}

		}
		else
		{
			if(entry.getCompany()!=null && entry.getAccountingYear()!=null && entry.getSubledger()!=null && entry.getSupplier_bill_date()!=null)
			{
			closingbalance=openingbalances.getclosingbalance(entry.getCompany().getCompany_id(),entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),entry.getSubledger().getSubledger_Id(),2);

			}
		}
		model.addObject("closingbalance", closingbalance);
		model.addObject("entry", entry);
		model.addObject("flag", flag);
		if(entry.getCreated_by()!=null)
		{
		model.addObject("created_by", userService.getById(entry.getCreated_by()));
		}
		if(entry.getUpdated_by()!=null)
		{
		model.addObject("updated_by", userService.getById(entry.getUpdated_by()));
		}
		model.addObject("suppilerproductList", purEntryService.findAllPurchaseEntryProductEntityList(id));
		model.setViewName("/transactions/purchaseEntryView");
		return model;
	}
	
	@RequestMapping(value ="deletePurchaseEntryProduct", method = RequestMethod.GET)
	public synchronized ModelAndView deletePurchaseEntryProduct(@RequestParam("id") long id, 
			HttpServletRequest request, 
			HttpServletResponse response){
		
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User)session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");
		
		ModelAndView model = new ModelAndView();
			
		PurchaseEntry entry = new PurchaseEntry();
		
		if(purEntryService.findAllPurchaseEntryProductEntityList(purchase_id).size()!=1)
		{
			String msg = purEntryService.deletePurchaseEntryProduct(id,purchase_id,company_id);	
			
			try {			
				entry= purEntryService.findOneWithAll(purchase_id);
				if(entry.getSupplier()!=null)
				{
				entry.setSupplier_id(entry.getSupplier().getSupplier_id());
				}
				if(entry.getSubledger()!=null)
				{
				entry.setSubledger_Id(entry.getSubledger().getSubledger_Id());	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			model.addObject("suppilerproductList", purEntryService.findAllPurchaseEntryProductEntityList(purchase_id));
			model.addObject("supplierList", suplliersService.findAllSuppliersOfCompany(company_id));
			model.addObject("productList", productService.findAllProductsOfCompany(company_id));
			model.addObject("entry", entry);
			model.addObject("successMsg", msg);
			model.addObject("stateId",user.getCompany().getState().getState_id());
			model.setViewName("/transactions/purchaseEntry");	
			return model;
		}
		else 
		{
			/*purEntryService.deleteByIdValue(purchase_id);
			session.setAttribute("msg", "Product as well as Purchase Entry Deleted Successfully");
			model.setViewName("redirect:/purchaseEntryList");
			return model;*/
			String msg = "You can't delete this product. You can edit this or you can add your required product first and then delete this product.";
			try {			
				entry= purEntryService.findOneWithAll(purchase_id);
				if(entry.getSupplier()!=null)
				{
				entry.setSupplier_id(entry.getSupplier().getSupplier_id());
				}
				if(entry.getSubledger()!=null)
				{
				entry.setSubledger_Id(entry.getSubledger().getSubledger_Id());	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			model.addObject("suppilerproductList", purEntryService.findAllPurchaseEntryProductEntityList(purchase_id));
			model.addObject("supplierList", suplliersService.findAllSuppliersOfCompany(company_id));
			model.addObject("productList", productService.findAllProductsOfCompany(company_id));
			model.addObject("entry", entry);
			model.addObject("successMsg", msg);
			model.addObject("stateId",user.getCompany().getState().getState_id());
			model.setViewName("/transactions/purchaseEntry");	
			return model;
		}
		
		
			
	}
	
	@RequestMapping(value = "deletePurchaseEntry", method = RequestMethod.GET)
	public synchronized ModelAndView deletePurchaseEntry(@RequestParam("id") long id,
			HttpServletRequest request, 
			HttpServletResponse response) {

		Boolean advance_payment =  false;
		HttpSession session = request.getSession(true);
		String msg = "";
		List<Payment> paymentList=paymentService.findallpaymententryofsales(id);
		for(Payment payment:paymentList)
		{
		if(payment.getAdvance_payment()!=null && payment.getAdvance_payment()==true)
		{
			advance_payment=true;
		}
		}
		if(advance_payment==false)
		{
		 for(int i = 0;i<paymentList.size();i++){	
			 Payment information= new Payment();
				information = paymentList.get(i);		
				paymentService.deletePayment(information.getPayment_id());
		 }

		 List<DebitNote> debitList=debitService.findByPurchaseId(id);
		 for(int i = 0;i<debitList.size();i++){	
			 DebitNote debit= new DebitNote();
				debit = debitList.get(i);		
				debitService.deleteByIdValue(debit.getDebit_no_id());
		 }
		 msg = purEntryService.deleteByIdValue(id);		
		}
		else
		{
			msg= "You can't delete purchase entry ";
		}
		session.setAttribute("msg", msg);
		
		if(flag == true)
		{
			return new ModelAndView("redirect:/purchaseEntryList");
		}
		else
		{
			return new ModelAndView("redirect:/purchaseEntryFailure");
		}
	}
	
	
	@RequestMapping(value = "getAdvancePayment", method = RequestMethod.POST, produces =MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody String getAdvancePayment(@RequestParam("id") long id,@RequestParam("yid") long yid,
			HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");

		List<Payment> payments = paymentService.getAdvancePaymentList(id, company_id,yid);
		JSONArray jsonArray = new JSONArray();
		for(Payment entry : payments) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("payment_id", entry.getPayment_id());
			jsonObj.put("date", entry.getDate());
			jsonObj.put("voucher_no", entry.getVoucher_no());
			Double amount =(double)0;
			if(entry.getAmount()!=null && entry.getTds_paid().equals(true) && entry.getTds_amount()!=null)
			{
				amount=entry.getAmount()+entry.getTds_amount();
			}
			else if(entry.getAmount()!=null)
			{
				amount=entry.getAmount();
			}
			
			if(entry.getIs_extraAdvanceReceived()!=null && entry.getIs_extraAdvanceReceived().equals(true) && entry.getSupplier_bill_no()!=null)
			{
				amount=amount-entry.getSupplier_bill_no().getRound_off();
				List<Payment> paymentList = paymentService.getAllPaymentsAgainstAdvancePayment(entry.getPayment_id());
				for(Payment paymentAgainstAdvance : paymentList)
				{
					amount=amount-paymentAgainstAdvance.getAmount();
				}
			}
			if(entry.getAgainstOpeningBalnce()!=null & entry.getSupplier_bill_no()!=null)
			{
				amount=amount-entry.getSupplier_bill_no().getRound_off();
				List<Payment> paymentList = paymentService.getAllPaymentsAgainstAdvancePayment(entry.getPayment_id());
				for(Payment paymentAgainstAdvance : paymentList)
				{
					amount=amount-paymentAgainstAdvance.getAmount();
				}
			}
				
				
			jsonObj.put("amount", amount);
			jsonArray.put(jsonObj);
		}
		return jsonArray.toString();
	}
	
	
	@RequestMapping(value = "downloadPurchase", method = RequestMethod.GET)
    public ModelAndView downloadPurchase(@RequestParam("id") long id) {
	 
		PurchaseForm form = new PurchaseForm();

		PurchaseEntry entry = new PurchaseEntry();
		try {
			entry = purEntryService.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		form.setPurchaseEntry(entry);
		form.setSuppilerproductList(purEntryService.findAllPurchaseEntryProductEntityList(id));
		return new ModelAndView("PurchasePdfView", "form", form);
    }
	
	/*@RequestMapping(value = "downloadPurchaseAccountingVoucher", method = RequestMethod.GET)
    public ModelAndView downloadPurchaseAccountingVoucher(@RequestParam("id") long id) {
	 
		PurchaseForm form = new PurchaseForm();

		PurchaseEntry entry = new PurchaseEntry();
		try {
			entry = purEntryService.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		form.setPurchaseEntry(entry);
		form.setSuppilerproductList(purEntryService.findAllPurchaseEntryProductEntityList(id));
		return new ModelAndView("PurchaseAccountingVoucherPdfView", "form", form);
    }*/
	
	
	@RequestMapping(value = {"importExcelPurchase"}, method = { RequestMethod.POST })
	public ModelAndView importExcelPurchase(@RequestParam("excelFile") MultipartFile excelfile,HttpServletRequest request, HttpServletResponse response) throws IOException{
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
		
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		long user_id=(long)session.getAttribute("user_id");
		User user = new User();
		user = (User)session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		
		List<Suppliers> list =suplliersService.findAllSuppliersOnlyOfCompany(company_id);
		List<AccountingYear> acclist = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		
		Integer m=0;
		
		try {
			   if (excelfile.getOriginalFilename().endsWith("xls")) {
	        	System.out.println("file is xls...");
	    			int i = 0;
	    			HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
	    			HSSFSheet worksheet = workbook.getSheetAt(0);
					if(isValid){
						i = 1;
		    			while (i <= worksheet.getLastRowNum()) {
		    				
		    				
		    				PurchaseEntry entry = new PurchaseEntry();
		    				PurchaseEntryProductEntityClass entity = new  PurchaseEntryProductEntityClass();
		    				
		    				Suppliers supplier1= null;
		    				SubLedger subledger1=null;
		    				Product product1=null;
		    			
		    				HSSFRow row = worksheet.getRow(i++);
		    				Err=false;
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
								Integer cnt=0;
								Boolean flag = false ;
								
								String vocherNofromExcel="";
								String shipingBillNofromExcel="";
								String portCodefromExcel="";
								String suppilerBillNofromExcel="";
								String ProductName = row.getCell(13).getStringCellValue();
								
								try
								{
									vocherNofromExcel=row.getCell(10).getStringCellValue().trim().replaceAll(" ", "");;
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try
								{
									Double voucherNofromExcel= row.getCell(10).getNumericCellValue();
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
									suppilerBillNofromExcel=row.getCell(2).getStringCellValue().trim();
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try
								{
									Double suppilerBillNfromExcel= row.getCell(2).getNumericCellValue();
									Integer suppilerBillNfromExcel1 = suppilerBillNfromExcel.intValue();
									suppilerBillNofromExcel=suppilerBillNfromExcel1.toString().trim();
									
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								
								PurchaseEntry entry1 = purEntryService.isExcelVocherNumberExist(vocherNofromExcel, company_id);	
								if(entry1!=null)
								{
									String vocherNo = entry1.getExcel_voucher_no().trim();
									Long company_Id = entry1.getCompany().getCompany_id();
									Double hsncode= row.getCell(12).getNumericCellValue();
									Integer hsncode1 = hsncode.intValue();
									String finalhsncode=hsncode1.toString().trim();
									List<PurchaseEntryProductEntityClass> prolist = purEntryService.findAllPurchaseEntryProductEntityList(entry1.getPurchase_id());
									Boolean entryflag = false ;
									Boolean mainentryflag = false;
									for(PurchaseEntryProductEntityClass pEPC : prolist)
									{
										if(finalhsncode.replace(" ","").trim().equalsIgnoreCase(pEPC.getHSNCode().replace(" ","").trim()) && ProductName.replace(" ","").trim().equalsIgnoreCase(pEPC.getProduct_name().replace(" ","").trim()))
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
										
									flag=true;								
									
									
									PurchaseEntry oldentry = purEntryService.findOneWithAll(entry1.getPurchase_id());
									
									if(oldentry.getSupplier()!=null)
									{
									supplier1=oldentry.getSupplier();
									}
									if(oldentry.getSubledger()!=null) {
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
									Tds=oldentry.getTds_amount();
									
									for(Suppliers supplier:list)
									{
										Boolean flag1 = false;
										if((supplier1!=null) && (supplier1.getSupplier_id().equals(supplier.getSupplier_id())))
										{
									try {
										for (Product product : suplliersService.findOneWithAll(supplier.getSupplier_id()).getProduct()) {
											
											if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals(""))
											{
												String str = product.getHsn_san_no().trim();
												String strproductName = product.getProduct_name().trim();
												if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
												entity.setProduct_name(product.getProduct_name());
												entity.setProduct_id(product.getProduct_id());
												flag1=true;
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
									
									if(row.getCell(14)!=null)
									{
									entity.setQuantity(round((Double) row.getCell(14).getNumericCellValue(),2));
									}else
									{
										entity.setQuantity((double)0);
									}

									
									if(row.getCell(15)!=null)
									{
										entity.setRate(round((Double) row.getCell(15).getNumericCellValue(),2));
									}else
									{
										entity.setRate((double)0);
									}
									
									

									if(row.getCell(16)!=null)
									{
										entity.setDiscount(round((Double) row.getCell(16).getNumericCellValue(),2));
									}else
									{
										entity.setDiscount((double)0);
									}
									
									if(row.getCell(17)!=null)
									{
										entity.setCGST(round((Double) row.getCell(17).getNumericCellValue(),2));
									}else
									{
										entity.setCGST((double)0);
									}

									
									if(row.getCell(18)!=null)
									{
										entity.setIGST(round((Double) row.getCell(18).getNumericCellValue(),2));

									}else
									{
										entity.setIGST((double)0);
									}

									if(row.getCell(19)!=null)
									{
										entity.setSGST(round((Double) row.getCell(19).getNumericCellValue(),2));

									}else
									{
										entity.setSGST((double)0);
									}


									
									if(row.getCell(20)!=null)
									{
										entity.setState_com_tax(round((Double) row.getCell(20).getNumericCellValue(),2));

									}else
									{
										entity.setState_com_tax((double)0);
									}
									

									if(row.getCell(21)!=null)
									{
										entity.setLabour_charges(round((Double) row.getCell(21).getNumericCellValue(),2));

									}else
									{
										entity.setLabour_charges((double)0);
									}

									if(row.getCell(22)!=null)
									{
										entity.setFreight(round((Double) row.getCell(22).getNumericCellValue(),2));

									}else
									{
										entity.setFreight((double)0);
									}
									
									if(row.getCell(23)!=null)
									{
										entity.setOthers(round((Double) row.getCell(23).getNumericCellValue(),2));

									}else
									{
										entity.setOthers((double)0);
									}

									
									if(row.getCell(24)!=null)
									{
										entity.setUOM(row.getCell(24).getStringCellValue().trim());

									}else
									{
										entity.setUOM("NA");
									}	

									entity.setHSNCode(finalhsncode);

									if(row.getCell(25)!=null)
									{
										entity.setVAT(round((Double) row.getCell(25).getNumericCellValue(),2));


									}else
									{
										entity.setVAT((double)0);
									}
									
									if(row.getCell(26)!=null)
									{
										entity.setVATCST(round((Double) row.getCell(26).getNumericCellValue(),2));


									}else
									{
										entity.setVATCST((double)0);
									}
									
									if(row.getCell(27)!=null)
									{
										entity.setExcise(round((Double) row.getCell(27).getNumericCellValue(),2));


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
										Double disamount =((round(damount,2))-(entity.getDiscount()));
										tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFreight()+entity.getOthers();
									}
									else
									{
										tamount = amount;
									}
									
									transaction_value=transaction_value+round(tamount,2);
									round_off=round_off+entity.getCGST()+entity.getIGST()+entity.getSGST()+entity.getState_com_tax()+entity.getVAT()+entity.getVATCST()+entity.getExcise()+tamount;
									
					         		if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
									{
										if(supplier1.getTds_rate()!=null)
										{
											NewTds = (transaction_value*supplier1.getTds_rate())/100;
										}
									
									}
									
									round_off =round_off+Tds-NewTds;
								  if(product1!=null)
							     {
									
								if(supplier1!=null)
								{
	                                try {
	                                	suplliersService.addsuppliertrsansactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
	                                			supplier1.getSupplier_id(), (long) 4, (double)oldentry.getRound_off(), (double)0,
	                							(long) 0);
	                                	
	                                	suplliersService.addsuppliertrsansactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
	                                			supplier1.getSupplier_id(), (long) 4, (double)round(round_off,2), (double)0, (long) 1);
									} catch (Exception e) {
										
										e.printStackTrace();
									}
									
								}
								
								
								if (Tds>0) {
									SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", company_id);
									if(subledgertds!=null)
									{
									subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)Tds, (double)0, (long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
									subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)NewTds, (double)0, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
									}
								}
								
								if(subledger1!=null)
								{
									try {
										subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
												subledger1.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getTransaction_value(),
												(long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										
										subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
												subledger1.getSubledger_Id(), (long) 2, (double)0, (double)transaction_value, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											
											}
										catch (Exception e) {
											e.printStackTrace();
										}
								}
								
								
								
									try{
										
										
										if((oldentry.getCgst()!=null) && (oldentry.getCgst()>0) && (cgst>0) && (oldentry.getCgst()!=cgst))
										{
										
											SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",
													company_id);
											
											if (subledgercgst != null) {
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getCgst(), (long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)cgst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										
										}
										}
										else if((oldentry.getCgst()!=null)&&(cgst>0) && (oldentry.getCgst()==0))
										{
											SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",
													company_id);
											
											if (subledgercgst != null) {
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)cgst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
										
										}
										
										
										if((oldentry.getSgst()!=null) && (oldentry.getSgst()>0) && (sgst>0) && (oldentry.getSgst()!=sgst))
										{
										
											SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
													company_id);
											
											if (subledgersgst != null) {
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getSgst(), (long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)sgst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
										
										}
										else if((oldentry.getSgst()!=null)&&(sgst>0)&&(oldentry.getSgst()==0))
										{
											SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
													company_id);
											
											if (subledgersgst != null) {
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)sgst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
										}
										
										if((oldentry.getIgst()!=null) && (oldentry.getIgst()>0) && (igst>0) && (oldentry.getIgst()!=igst))
										{
										
											SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
													company_id);
											
											if (subledgerigst != null) {
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgerigst.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getIgst(), (long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgerigst.getSubledger_Id(), (long) 2, (double)0, (double)igst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
										
										}
										else if((oldentry.getIgst()!=null)&&(igst>0)&&(oldentry.getIgst()==0))
										{
											SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
													company_id);
											
											if (subledgerigst != null) {
							            		subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgerigst.getSubledger_Id(), (long) 2, (double)0, (double)igst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
										}
										
										if((oldentry.getState_compansation_tax()!=null) && (oldentry.getState_compansation_tax()>0) && (state_comp_tax>0)&&(oldentry.getState_compansation_tax()!=state_comp_tax))
										{
											SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
													company_id);
											
											if (subledgercess != null) {
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgercess.getSubledger_Id(), (long) 2, (double)0,
														(double)oldentry.getState_compansation_tax(), (long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgercess.getSubledger_Id(), (long) 2, (double)0,
														(double)state_comp_tax, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
										}
										else if((oldentry.getState_compansation_tax()!=null) && (state_comp_tax>0) && (oldentry.getState_compansation_tax()==0))
										{
											SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
													company_id);
											
											if (subledgercess != null) {
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgercess.getSubledger_Id(), (long) 2, (double)0,
														(double)state_comp_tax, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
										}
										
										if((oldentry.getTotal_vat()!=null) && (oldentry.getTotal_vat()>0) && (vat>0) && (oldentry.getTotal_vat()!=vat))
										{
										
											SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", company_id);
											if (subledgervat != null) {
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgervat.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getTotal_vat(),
														(long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgervat.getSubledger_Id(), (long) 2, (double)0, (double)vat,
														(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
										
										}
										else if((oldentry.getTotal_vat()!=null) && (vat>0) && (oldentry.getTotal_vat()==0))
										{
											SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", company_id);
											if (subledgervat != null) {
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgervat.getSubledger_Id(), (long) 2, (double)0, (double)vat,
														(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
										}
										
										
										if((oldentry.getTotal_vatcst()!=null) && (oldentry.getTotal_vatcst()>0) && (vatcst>0) && (oldentry.getTotal_vatcst()!=vatcst)) 
										{
										
											SubLedger subledgercst = subLedgerDAO.findOne("Input CST", company_id);
											
											if (subledgercst != null) {
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getTotal_vatcst(),
														(long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)vatcst,
														(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
										
										}
										else if((oldentry.getTotal_vatcst()!=null) && (vatcst>0) && (oldentry.getTotal_vatcst()==0))
										{
	                                            SubLedger subledgercst = subLedgerDAO.findOne("Input CST", company_id);
											
											if (subledgercst != null) {
												
													   subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)vatcst,
														(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
											
										}
										
										if((oldentry.getTotal_excise()!=null) && (oldentry.getTotal_excise()>0) && (excise>0) && (oldentry.getTotal_excise()!=excise))
										{
										
											SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
													company_id);
											
											if (subledgerexcise != null) {
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgerexcise.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getTotal_excise(),
														(long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
												
												subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgerexcise.getSubledger_Id(), (long) 2, (double)0, (double)excise,
														(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											}
										
										}
										else if((oldentry.getTotal_excise()!=null) && excise>0 && (oldentry.getTotal_excise()==0))
										{
											SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
													company_id);
											
											if (subledgerexcise != null) {
												
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
														subledgerexcise.getSubledger_Id(), (long) 2, (double)0, (double)excise,
														(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
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
								
										
								    purEntryService.updatePurchaseEntryThroughExcel(oldentry);
									entity.setPurchase_id(oldentry.getPurchase_id());
									purEntryService.updatePurchaseEntryProductEntityClassThroughExcel(entity);
									
									 //stock
									if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
									{
									Integer pflag=productService.checktype(company_id,product1.getProduct_id());
								     if(pflag==1)  
								     {
								      Stock stock = null;
								      stock=stockService.isstockexist(company_id,product1.getProduct_id());
								      double amt=entity.getQuantity()*entity.getRate();
										if(stock==null)
										{
											Stock stockdata = new Stock();
											stockdata.setCompany_id(company_id);
											stockdata.setProduct_id(product1.getProduct_id());
											stockdata.setQuantity(entity.getQuantity());
											stockdata.setAmount(amt);
											Long id = stockService.saveStock(stockdata);
											stockService.addStockInfoOfProduct(id, company_id, product1.getProduct_id(), entity.getQuantity(), entity.getRate());
										}
										else
										{					
											stock.setAmount(stock.getAmount()+amt);
											stock.setQuantity(stock.getQuantity()+entity.getQuantity());					
											stockService.updateStock(stock);
											stockService.addStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(), entity.getQuantity(), entity.getRate());
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
									 /* purEntryService.ChangeStatusOfPurchaseEntryThroughExcel(oldentry);
									  
									  if(successVocharList!=null && successVocharList.size()>0)
										{
											sucount=sucount-1;
											failcount=failcount+1;
											m = 2;
											successVocharList.remove(vocherNofromExcel);
											failureVocharList.add(vocherNofromExcel);
										}*/
								  }
									/*
									break;*/
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
									
									 Double hsncode= row.getCell(12).getNumericCellValue();
									 Integer hsncode1 = hsncode.intValue();
									 String finalhsncode=hsncode1.toString().trim();
									 
									 String remoteAddr = "";
									 remoteAddr = request.getHeader("X-FORWARDED-FOR");
							            if (remoteAddr == null || "".equals(remoteAddr)) {
							                remoteAddr = request.getRemoteAddr();
							            }
							            entry.setIp_address(remoteAddr);
							            entry.setCreated_by(user_id);	
							            
							          PurchaseEntry oldentry = purEntryService.isExcelVocherNumberExist(vocherNofromExcel, company_id);	
										if(oldentry!=null && oldentry.getExcel_voucher_no()!=null)
										{
										 String str=oldentry.getExcel_voucher_no().trim();
											if(str.replace(" ","").trim().equalsIgnoreCase(vocherNofromExcel.replace(" ","").trim())) {
												flagtocheckentry=true;
											   /* break;*/
											}
										}
								
									if(flagtocheckentry==false)
									{
									
									try {
										cnt=0;
											for(Suppliers supplier:list)
											{
												
												String str=supplier.getCompany_name().trim();
												if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(0).getStringCellValue().replace(" ","").trim())) {
													entry.setSupplier(supplier);
													supplier1=supplier;
													count=count+1;
													cnt=1;
												    break;
												}
											}
											if(cnt==0){Err=true;ErrorMsgs.append(" Supplier name is incorrect ");}
										} catch (Exception e) {
											e.printStackTrace();
										}
									entry.setCompany(user.getCompany());
									
									for(Suppliers supplier:list)
									{
										Boolean flag1 = false;
										cnt=0;
										if(supplier1!=null){
										if( (supplier1.getSupplier_id().equals(supplier.getSupplier_id())))
										{
										for(SubLedger subledger:suplliersService.findOneWithAll(supplier.getSupplier_id()).getSubLedgers())
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
									    }}else{break;}
									}
									if(cnt==0 && supplier1!=null){Err=true;ErrorMsgs.append(" Subledger not correct");}
									
									if(row.getCell(1)!=null)
									{
										try {
											entry.setSupplier_bill_date(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(1).getDateCellValue().toString())));

										   }
										   catch (Exception e) {
										    e.printStackTrace();
										    }
										
										if(entry.getSupplier_bill_date()==null){
											Err=true;ErrorMsgs.append("Date format is not correct. Enter date in dd/mm/yyyy format in Supplier Bill Date Column");
											}
									}
									
									try {
										cnt=0;
										for(AccountingYear year_range:acclist)
										{
											
											String str =year_range.getYear_range().trim();
											if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(11).getStringCellValue().replace(" ","").trim())) {
												entry.setAccountingYear(year_range);
												if(entry.getSupplier_bill_date()!=null)
												{
												entry.setVoucher_no(commonService.getVoucherNumber("PE", VOUCHER_PURCHASE_ENTRY, entry.getSupplier_bill_date(),year_range.getYear_id(), company_id));
												}
												count=count+1;
												cnt=1;
											    break;
											}
										}
										if(cnt==0 ){Err=true;ErrorMsgs.append(" Accounting Year range is incorrect");}
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									
									if(row.getCell(2)!=null)
									{
									entry.setSupplier_bill_no(suppilerBillNofromExcel);
									}
									entry.setLocal_time(new LocalTime());
									if(row.getCell(4)!=null)
									{
									entry.setRemark(row.getCell(4).getStringCellValue().trim());
									}
									
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
									entry.setCreated_date(new LocalDate());
									entry.setAgainst_advance_Payment(false);
									for(Suppliers supplier:list)
									{
										Boolean flag1 = false;
										cnt=0;
										if((supplier1!=null)){
										if(  (supplier1.getSupplier_id().equals(supplier.getSupplier_id())))
										{
									try {
										for (Product product : suplliersService.findOneWithAll(supplier.getSupplier_id()).getProduct()) {
											
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
												count = count + 1;
												cnt=1;
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
										}else{break;}
									}
									if(cnt==0 && supplier1!=null){Err=true;ErrorMsgs.append(" Product not correct");}
									if(row.getCell(14)!=null)
									{
									entity.setQuantity((Double) row.getCell(14).getNumericCellValue());
									}else
									{
										entity.setQuantity((double)0);
									}

									
									if(row.getCell(15)!=null)
									{
										entity.setRate((Double) row.getCell(15).getNumericCellValue());
									}else
									{
										entity.setRate((double)0);
									}
									
									

									if(row.getCell(16)!=null)
									{
										entity.setDiscount((Double) row.getCell(16).getNumericCellValue());
									}else
									{
										entity.setDiscount((double)0);
									}
									
									if(row.getCell(17)!=null)
									{
										entity.setCGST((Double) row.getCell(17).getNumericCellValue());
									}else
									{
										entity.setCGST((double)0);
									}

									
									if(row.getCell(18)!=null)
									{
										entity.setIGST((Double) row.getCell(18).getNumericCellValue());

									}else
									{
										entity.setIGST((double)0);
									}

									if(row.getCell(19)!=null)
									{
										entity.setSGST((Double) row.getCell(19).getNumericCellValue());

									}else
									{
										entity.setSGST((double)0);
									}


									
									if(row.getCell(20)!=null)
									{
										entity.setState_com_tax((Double) row.getCell(20).getNumericCellValue());

									}else
									{
										entity.setState_com_tax((double)0);
									}
									

									if(row.getCell(21)!=null)
									{
										entity.setLabour_charges((Double) row.getCell(21).getNumericCellValue());

									}else
									{
										entity.setLabour_charges((double)0);
									}

									if(row.getCell(22)!=null)
									{
										entity.setFreight((Double) row.getCell(22).getNumericCellValue());

									}else
									{
										entity.setFreight((double)0);
									}
									
									if(row.getCell(23)!=null)
									{
										entity.setOthers((Double) row.getCell(23).getNumericCellValue());

									}else
									{
										entity.setOthers((double)0);
									}

									
									if(row.getCell(24)!=null)
									{
										entity.setUOM(row.getCell(24).getStringCellValue().trim());

									}else
									{
										entity.setUOM("NA");
									}	

									entity.setHSNCode(finalhsncode);

									if(row.getCell(25)!=null)
									{
										entity.setVAT((Double) row.getCell(25).getNumericCellValue());


									}else
									{
										entity.setVAT((double)0);
									}
									
									if(row.getCell(26)!=null)
									{
										entity.setVATCST((Double) row.getCell(26).getNumericCellValue());


									}else
									{
										entity.setVATCST((double)0);
									}
									
									if(row.getCell(27)!=null)
									{
										entity.setExcise((Double) row.getCell(27).getNumericCellValue());


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
									
										if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
										{
											if(supplier1.getTds_rate()!=null)
											{
												Tds = (transaction_value*supplier1.getTds_rate())/100;
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
								
								  if(product1!=null && count.equals(4) && supplier1!=null && entry.getVoucher_no()!=null)
								  {
									
										entry.setFlag(true);
										sucount=sucount+1;
										successVocharList.add(vocherNofromExcel);
										repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
										
						
									entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
									Long id1 = purEntryService.savePurchaseEntryThroughExcel(entry);
									entity.setPurchase_id(id1);
									purEntryService.savePurchaseEntryProductEntityClassThroughExcel(entity);
									
									PurchaseEntry purchase = purEntryService.getById(id1);
									 //stock
									if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
									{
									Integer pflag=productService.checktype(company_id,product1.getProduct_id());
								     if(pflag==1)  
								     {
								      Stock stock = null;
								      stock=stockService.isstockexist(company_id,product1.getProduct_id());
								      double amt=entity.getQuantity()*entity.getRate();
										if(stock==null)
										{
											Stock stockdata = new Stock();
											stockdata.setCompany_id(company_id);
											stockdata.setProduct_id(product1.getProduct_id());
											stockdata.setQuantity(entity.getQuantity());
											stockdata.setAmount(amt);
											Long id = stockService.saveStock(stockdata);
											stockService.addStockInfoOfProduct(id, company_id, product1.getProduct_id(), entity.getQuantity(), entity.getRate());
										}
										else
										{					
											stock.setAmount(stock.getAmount()+amt);
											stock.setQuantity(stock.getQuantity()+entity.getQuantity());					
											stockService.updateStock(stock);
											stockService.addStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(), entity.getQuantity(), entity.getRate());
										}
								     }  	
									
							        }
								     
									
									if(supplier1!=null)
									{
									try {
										suplliersService.addsuppliertrsansactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
												supplier1.getSupplier_id(), (long) 4, (double)round(round_off,2), (double)0, (long) 1);
									} catch (Exception e) {
										
										e.printStackTrace();
									}
									}
									
									try {
											if(subledger1!=null)
											{
												subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id, subledger1.getSubledger_Id(),
														(long) 2, (double)0, (double)transaction_value, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
											
											}
									    }
										catch (Exception e) {
											e.printStackTrace();
										}
								
									
									
									try{
										
										
										if (Tds>0) {
											SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", company_id);
											if(subledgertds!=null)
											{
											subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)Tds, (double)0, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
										    }
										}
										if(cgst>0)
										{
											SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST", company_id);
											if(subledgercgst!=null)
											{
											subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
													subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)cgst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
											}
										}
										
										if(sgst>0)
										{
											SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST", company_id);
											if(subledgersgst!=null)
											{
											subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
													subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)sgst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
											}
										}
										
										if(igst>0)
										{
											SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST", company_id);
											if(subledgerigst!=null)
											{
											subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
													subledgerigst.getSubledger_Id(), (long) 2, (double)0,(double)igst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
											}
										}
										
										if(state_comp_tax>0)
										{
											SubLedger subledgercess = subLedgerDAO.findOne("Input CESS", company_id);
											if(subledgercess!=null)
											{
											subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
													subledgercess.getSubledger_Id(), (long) 2, (double)0, (double)state_comp_tax,
													(long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
											}
										}
										if(vat>0)
										{
											SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", company_id);
											if(subledgervat!=null)
											{
											subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
													subledgervat.getSubledger_Id(), (long) 2, (double)0,(double)vat, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
											}
										}
										if(vatcst>0)
										{
											SubLedger subledgercst = subLedgerDAO.findOne("Input CST", company_id);
											if(subledgercst!=null)
											{
											subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
													subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)vatcst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
											}
										}
										if(excise>0)
										{
											SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE", company_id);
											if(subledgerexcise!=null)
											{
											subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
													subledgerexcise.getSubledger_Id(), (long) 2, (double)0,(double) excise, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
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
	    				PurchaseEntry entry = new PurchaseEntry();
	    				PurchaseEntryProductEntityClass entity = new  PurchaseEntryProductEntityClass();
	    				
	    				Suppliers supplier1= null;
	    				SubLedger subledger1=null;
	    				Product product1=null;
						XSSFRow row = worksheet.getRow(i++);
						Err=false;
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
							Integer cnt=0;
							Boolean flag = false ;
							
							String vocherNofromExcel="";
							String shipingBillNofromExcel="";
							String portCodefromExcel="";
							String suppilerBillNofromExcel="";
							String ProductName = row.getCell(13).getStringCellValue();
							
							try
							{
								vocherNofromExcel=row.getCell(10).getStringCellValue().trim().replaceAll(" ", "");;
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							try
							{
								Double voucherNofromExcel= row.getCell(10).getNumericCellValue();
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
								suppilerBillNofromExcel=row.getCell(2).getStringCellValue().trim();
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							try
							{
								Double suppilerBillNfromExcel= row.getCell(2).getNumericCellValue();
								Integer suppilerBillNfromExcel1 = suppilerBillNfromExcel.intValue();
								suppilerBillNofromExcel=suppilerBillNfromExcel1.toString().trim();
								
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							
							PurchaseEntry entry1 = purEntryService.isExcelVocherNumberExist(vocherNofromExcel, company_id);	
							if(entry1!=null)
							{
								String vocherNo = entry1.getExcel_voucher_no().trim();
								Long company_Id = entry1.getCompany().getCompany_id();
								Double hsncode= row.getCell(12).getNumericCellValue();
								Integer hsncode1 = hsncode.intValue();
								String finalhsncode=hsncode1.toString().trim();
								List<PurchaseEntryProductEntityClass> prolist = purEntryService.findAllPurchaseEntryProductEntityList(entry1.getPurchase_id());
								Boolean entryflag = false ;
								Boolean mainentryflag = false;
								for(PurchaseEntryProductEntityClass pEPC : prolist)
								{
									if(finalhsncode.replace(" ","").trim().equalsIgnoreCase(pEPC.getHSNCode().replace(" ","").trim()) && ProductName.replace(" ","").trim().equalsIgnoreCase(pEPC.getProduct_name().replace(" ","").trim()))
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
									
								flag=true;								
								
								
								PurchaseEntry oldentry = purEntryService.findOneWithAll(entry1.getPurchase_id());
								
								if(oldentry.getSupplier()!=null)
								{
								supplier1=oldentry.getSupplier();
								}
								if(oldentry.getSubledger()!=null) {
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
								Tds=oldentry.getTds_amount();
								
								for(Suppliers supplier:list)
								{
									Boolean flag1 = false;
									if((supplier1!=null) && (supplier1.getSupplier_id().equals(supplier.getSupplier_id())))
									{
								try {
									for (Product product : suplliersService.findOneWithAll(supplier.getSupplier_id()).getProduct()) {
										
										if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals(""))
										{
											String str = product.getHsn_san_no().trim();
											String strproductName = product.getProduct_name().trim();
											if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
											entity.setProduct_name(product.getProduct_name());
											entity.setProduct_id(product.getProduct_id());
											flag1=true;
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
								
								if(row.getCell(14)!=null)
								{
								entity.setQuantity(round((Double) row.getCell(14).getNumericCellValue(),2));
								}else
								{
									entity.setQuantity((double)0);
								}

								
								if(row.getCell(15)!=null)
								{
									entity.setRate(round((Double) row.getCell(15).getNumericCellValue(),2));
								}else
								{
									entity.setRate((double)0);
								}
								
								

								if(row.getCell(16)!=null)
								{
									entity.setDiscount(round((Double) row.getCell(16).getNumericCellValue(),2));
								}else
								{
									entity.setDiscount((double)0);
								}
								
								if(row.getCell(17)!=null)
								{
									entity.setCGST(round((Double) row.getCell(17).getNumericCellValue(),2));
								}else
								{
									entity.setCGST((double)0);
								}

								
								if(row.getCell(18)!=null)
								{
									entity.setIGST(round((Double) row.getCell(18).getNumericCellValue(),2));

								}else
								{
									entity.setIGST((double)0);
								}

								if(row.getCell(19)!=null)
								{
									entity.setSGST(round((Double) row.getCell(19).getNumericCellValue(),2));

								}else
								{
									entity.setSGST((double)0);
								}


								
								if(row.getCell(20)!=null)
								{
									entity.setState_com_tax(round((Double) row.getCell(20).getNumericCellValue(),2));

								}else
								{
									entity.setState_com_tax((double)0);
								}
								

								if(row.getCell(21)!=null)
								{
									entity.setLabour_charges(round((Double) row.getCell(21).getNumericCellValue(),2));

								}else
								{
									entity.setLabour_charges((double)0);
								}

								if(row.getCell(22)!=null)
								{
									entity.setFreight(round((Double) row.getCell(22).getNumericCellValue(),2));

								}else
								{
									entity.setFreight((double)0);
								}
								
								if(row.getCell(23)!=null)
								{
									entity.setOthers(round((Double) row.getCell(23).getNumericCellValue(),2));

								}else
								{
									entity.setOthers((double)0);
								}

								
								if(row.getCell(24)!=null)
								{
									entity.setUOM(row.getCell(24).getStringCellValue().trim());

								}else
								{
									entity.setUOM("NA");
								}	

								entity.setHSNCode(finalhsncode);

								if(row.getCell(25)!=null)
								{
									entity.setVAT(round((Double) row.getCell(25).getNumericCellValue(),2));


								}else
								{
									entity.setVAT((double)0);
								}
								
								if(row.getCell(26)!=null)
								{
									entity.setVATCST(round((Double) row.getCell(26).getNumericCellValue(),2));


								}else
								{
									entity.setVATCST((double)0);
								}
								
								if(row.getCell(27)!=null)
								{
									entity.setExcise(round((Double) row.getCell(27).getNumericCellValue(),2));


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
									Double disamount =((round(damount,2))-(entity.getDiscount()));
									tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFreight()+entity.getOthers();
								}
								else
								{
									tamount = amount;
								}
								
								transaction_value=transaction_value+round(tamount,2);
								round_off=round_off+entity.getCGST()+entity.getIGST()+entity.getSGST()+entity.getState_com_tax()+entity.getVAT()+entity.getVATCST()+entity.getExcise()+tamount;
								
				         		if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
								{
									if(supplier1.getTds_rate()!=null)
									{
										NewTds = (transaction_value*supplier1.getTds_rate())/100;
									}
								
								}
								
								round_off =round_off+Tds-NewTds;
							  if(product1!=null)
						     {
								
							if(supplier1!=null)
							{
                                try {
                                	suplliersService.addsuppliertrsansactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
                                			supplier1.getSupplier_id(), (long) 4, (double)oldentry.getRound_off(), (double)0,
                							(long) 0);
                                	
                                	suplliersService.addsuppliertrsansactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
                                			supplier1.getSupplier_id(), (long) 4, (double)round(round_off,2), (double)0, (long) 1);
								} catch (Exception e) {
									
									e.printStackTrace();
								}
								
							}
							
							
							if (Tds>0) {
								SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", company_id);
								if(subledgertds!=null)
								{
								subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)Tds, (double)0, (long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
								subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)NewTds, (double)0, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
								}
							}
							
							if(subledger1!=null)
							{
								try {
									subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
											subledger1.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getTransaction_value(),
											(long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
									
									subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
											subledger1.getSubledger_Id(), (long) 2, (double)0, (double)transaction_value, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										
										}
									catch (Exception e) {
										e.printStackTrace();
									}
							}
							
							
							
								try{
									
									
									if((oldentry.getCgst()!=null) && (oldentry.getCgst()>0) && (cgst>0) && (oldentry.getCgst()!=cgst))
									{
									
										SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",
												company_id);
										
										if (subledgercgst != null) {
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getCgst(), (long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)cgst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
									
									}
									}
									else if((oldentry.getCgst()!=null)&&(cgst>0) && (oldentry.getCgst()==0))
									{
										SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",
												company_id);
										
										if (subledgercgst != null) {
										subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)cgst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
									}
									
									}
									
									
									if((oldentry.getSgst()!=null) && (oldentry.getSgst()>0) && (sgst>0) && (oldentry.getSgst()!=sgst))
									{
									
										SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
												company_id);
										
										if (subledgersgst != null) {
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getSgst(), (long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)sgst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
									
									}
									else if((oldentry.getSgst()!=null)&&(sgst>0)&&(oldentry.getSgst()==0))
									{
										SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
												company_id);
										
										if (subledgersgst != null) {
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)sgst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
									}
									
									if((oldentry.getIgst()!=null) && (oldentry.getIgst()>0) && (igst>0) && (oldentry.getIgst()!=igst))
									{
									
										SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
												company_id);
										
										if (subledgerigst != null) {
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgerigst.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getIgst(), (long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgerigst.getSubledger_Id(), (long) 2, (double)0, (double)igst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
									
									}
									else if((oldentry.getIgst()!=null)&&(igst>0)&&(oldentry.getIgst()==0))
									{
										SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
												company_id);
										
										if (subledgerigst != null) {
						            		subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgerigst.getSubledger_Id(), (long) 2, (double)0, (double)igst, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
									}
									
									if((oldentry.getState_compansation_tax()!=null) && (oldentry.getState_compansation_tax()>0) && (state_comp_tax>0)&&(oldentry.getState_compansation_tax()!=state_comp_tax))
									{
										SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
												company_id);
										
										if (subledgercess != null) {
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgercess.getSubledger_Id(), (long) 2, (double)0,
													(double)oldentry.getState_compansation_tax(), (long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgercess.getSubledger_Id(), (long) 2, (double)0,
													(double)state_comp_tax, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
									}
									else if((oldentry.getState_compansation_tax()!=null) && (state_comp_tax>0) && (oldentry.getState_compansation_tax()==0))
									{
										SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
												company_id);
										
										if (subledgercess != null) {
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgercess.getSubledger_Id(), (long) 2, (double)0,
													(double)state_comp_tax, (long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
									}
									
									if((oldentry.getTotal_vat()!=null) && (oldentry.getTotal_vat()>0) && (vat>0) && (oldentry.getTotal_vat()!=vat))
									{
									
										SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", company_id);
										if (subledgervat != null) {
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgervat.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getTotal_vat(),
													(long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgervat.getSubledger_Id(), (long) 2, (double)0, (double)vat,
													(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
									
									}
									else if((oldentry.getTotal_vat()!=null) && (vat>0) && (oldentry.getTotal_vat()==0))
									{
										SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", company_id);
										if (subledgervat != null) {
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgervat.getSubledger_Id(), (long) 2, (double)0, (double)vat,
													(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
									}
									
									
									if((oldentry.getTotal_vatcst()!=null) && (oldentry.getTotal_vatcst()>0) && (vatcst>0) && (oldentry.getTotal_vatcst()!=vatcst)) 
									{
									
										SubLedger subledgercst = subLedgerDAO.findOne("Input CST", company_id);
										
										if (subledgercst != null) {
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getTotal_vatcst(),
													(long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)vatcst,
													(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
									
									}
									else if((oldentry.getTotal_vatcst()!=null) && (vatcst>0) && (oldentry.getTotal_vatcst()==0))
									{
                                            SubLedger subledgercst = subLedgerDAO.findOne("Input CST", company_id);
										
										if (subledgercst != null) {
											
												   subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)vatcst,
													(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
										
									}
									
									if((oldentry.getTotal_excise()!=null) && (oldentry.getTotal_excise()>0) && (excise>0) && (oldentry.getTotal_excise()!=excise))
									{
									
										SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
												company_id);
										
										if (subledgerexcise != null) {
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgerexcise.getSubledger_Id(), (long) 2, (double)0, (double)oldentry.getTotal_excise(),
													(long) 0,null,null,oldentry,null,null,null,null,null,null,null,null,null);
											
											subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgerexcise.getSubledger_Id(), (long) 2, (double)0, (double)excise,
													(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
										}
									
									}
									else if((oldentry.getTotal_excise()!=null) && excise>0 && (oldentry.getTotal_excise()==0))
									{
										SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
												company_id);
										
										if (subledgerexcise != null) {
											
										subledgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getSupplier_bill_date(),company_id,
													subledgerexcise.getSubledger_Id(), (long) 2, (double)0, (double)excise,
													(long) 1,null,null,oldentry,null,null,null,null,null,null,null,null,null);
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
							
									
							    purEntryService.updatePurchaseEntryThroughExcel(oldentry);
								entity.setPurchase_id(oldentry.getPurchase_id());
								purEntryService.updatePurchaseEntryProductEntityClassThroughExcel(entity);
								
								 //stock
								if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
								{
								Integer pflag=productService.checktype(company_id,product1.getProduct_id());
							     if(pflag==1)  
							     {
							      Stock stock = null;
							      stock=stockService.isstockexist(company_id,product1.getProduct_id());
							      double amt=entity.getQuantity()*entity.getRate();
									if(stock==null)
									{
										Stock stockdata = new Stock();
										stockdata.setCompany_id(company_id);
										stockdata.setProduct_id(product1.getProduct_id());
										stockdata.setQuantity(entity.getQuantity());
										stockdata.setAmount(amt);
										Long id = stockService.saveStock(stockdata);
										stockService.addStockInfoOfProduct(id, company_id, product1.getProduct_id(), entity.getQuantity(), entity.getRate());
									}
									else
									{					
										stock.setAmount(stock.getAmount()+amt);
										stock.setQuantity(stock.getQuantity()+entity.getQuantity());					
										stockService.updateStock(stock);
										stockService.addStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(), entity.getQuantity(), entity.getRate());
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
								 /* purEntryService.ChangeStatusOfPurchaseEntryThroughExcel(oldentry);
								  
								  if(successVocharList!=null && successVocharList.size()>0)
									{
										sucount=sucount-1;
										failcount=failcount+1;
										m = 2;
										successVocharList.remove(vocherNofromExcel);
										failureVocharList.add(vocherNofromExcel);
									}*/
							  }
								/*
								break;*/
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
								
								 Double hsncode= row.getCell(12).getNumericCellValue();
								 Integer hsncode1 = hsncode.intValue();
								 String finalhsncode=hsncode1.toString().trim();
								 
								 String remoteAddr = "";
								 remoteAddr = request.getHeader("X-FORWARDED-FOR");
						            if (remoteAddr == null || "".equals(remoteAddr)) {
						                remoteAddr = request.getRemoteAddr();
						            }
						            entry.setIp_address(remoteAddr);
						            entry.setCreated_by(user_id);	
						            
						          PurchaseEntry oldentry = purEntryService.isExcelVocherNumberExist(vocherNofromExcel, company_id);	
									if(oldentry!=null && oldentry.getExcel_voucher_no()!=null)
									{
									 String str=oldentry.getExcel_voucher_no().trim();
										if(str.replace(" ","").trim().equalsIgnoreCase(vocherNofromExcel.replace(" ","").trim())) {
											flagtocheckentry=true;
										   /* break;*/
										}
									}
							
								if(flagtocheckentry==false)
								{
								
								try {
									cnt=0;
										for(Suppliers supplier:list)
										{
											
											String str=supplier.getCompany_name().trim();
											if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(0).getStringCellValue().replace(" ","").trim())) {
												entry.setSupplier(supplier);
												supplier1=supplier;
												count=count+1;
												cnt=1;
											    break;
											}
										}
										if(cnt==0){Err=true;ErrorMsgs.append(" supplier is not correct ");}
									} catch (Exception e) {
										e.printStackTrace();
									}
								entry.setCompany(user.getCompany());
								
								for(Suppliers supplier:list)
								{
									Boolean flag1 = false;
									cnt=0;
									if((supplier1!=null) ){
									if( (supplier1.getSupplier_id().equals(supplier.getSupplier_id())))
									{
									for(SubLedger subledger:suplliersService.findOneWithAll(supplier.getSupplier_id()).getSubLedgers())
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
								    }}else{break;}
								}
								
								if(cnt==0 && supplier1!=null ){Err=true;ErrorMsgs.append(" subledger is not correct ");}
								if(row.getCell(1)!=null)
								{
									try {
										entry.setSupplier_bill_date(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(1).getDateCellValue().toString())));

									   }
									   catch (Exception e) {
									    e.printStackTrace();
									    }
									if(entry.getSupplier_bill_date()==null){
										Err=true;ErrorMsgs.append("Date format is not correct. Enter date in dd/mm/yyyy format in Supplier Bill Date Column");
										}
								}
								
								try {
									cnt=0;
									for(AccountingYear year_range:acclist)
									{
										
										String str =year_range.getYear_range().trim();
										if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(11).getStringCellValue().replace(" ","").trim())) {
											entry.setAccountingYear(year_range);
											if(entry.getSupplier_bill_date()!=null)
											{
											entry.setVoucher_no(commonService.getVoucherNumber("PE", VOUCHER_PURCHASE_ENTRY, entry.getSupplier_bill_date(),year_range.getYear_id(), company_id));
											}
											count=count+1;
											cnt=1;
										    break;
										}
									}
									if(cnt==0){Err=true;ErrorMsgs.append(" Accounting  is not correct");}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								
								if(row.getCell(2)!=null)
								{
								entry.setSupplier_bill_no(suppilerBillNofromExcel);
								}
								entry.setLocal_time(new LocalTime());
								if(row.getCell(4)!=null)
								{
								entry.setRemark(row.getCell(4).getStringCellValue().trim());
								}
								
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
								entry.setCreated_date(new LocalDate());
								entry.setAgainst_advance_Payment(false);
								for(Suppliers supplier:list)
								{
									Boolean flag1 = false;
									cnt=1;
									if((supplier1!=null)){
									if(  (supplier1.getSupplier_id().equals(supplier.getSupplier_id())))
									{
								try {
									for (Product product : suplliersService.findOneWithAll(supplier.getSupplier_id()).getProduct()) {
										
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
											count = count + 1;
											cnt=1;
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
								
								} }else{break;}
								}
								
								if(cnt==0 && supplier1!=null ){Err=true;ErrorMsgs.append(" Product is not correct ");}
								if(row.getCell(14)!=null)
								{
								entity.setQuantity((Double) row.getCell(14).getNumericCellValue());
								}else
								{
									entity.setQuantity((double)0);
								}

								
								if(row.getCell(15)!=null)
								{
									entity.setRate((Double) row.getCell(15).getNumericCellValue());
								}else
								{
									entity.setRate((double)0);
								}
								
								

								if(row.getCell(16)!=null)
								{
									entity.setDiscount((Double) row.getCell(16).getNumericCellValue());
								}else
								{
									entity.setDiscount((double)0);
								}
								
								if(row.getCell(17)!=null)
								{
									entity.setCGST((Double) row.getCell(17).getNumericCellValue());
								}else
								{
									entity.setCGST((double)0);
								}

								
								if(row.getCell(18)!=null)
								{
									entity.setIGST((Double) row.getCell(18).getNumericCellValue());

								}else
								{
									entity.setIGST((double)0);
								}

								if(row.getCell(19)!=null)
								{
									entity.setSGST((Double) row.getCell(19).getNumericCellValue());

								}else
								{
									entity.setSGST((double)0);
								}


								
								if(row.getCell(20)!=null)
								{
									entity.setState_com_tax((Double) row.getCell(20).getNumericCellValue());

								}else
								{
									entity.setState_com_tax((double)0);
								}
								

								if(row.getCell(21)!=null)
								{
									entity.setLabour_charges((Double) row.getCell(21).getNumericCellValue());

								}else
								{
									entity.setLabour_charges((double)0);
								}

								if(row.getCell(22)!=null)
								{
									entity.setFreight((Double) row.getCell(22).getNumericCellValue());

								}else
								{
									entity.setFreight((double)0);
								}
								
								if(row.getCell(23)!=null)
								{
									entity.setOthers((Double) row.getCell(23).getNumericCellValue());

								}else
								{
									entity.setOthers((double)0);
								}

								
								if(row.getCell(24)!=null)
								{
									entity.setUOM(row.getCell(24).getStringCellValue().trim());

								}else
								{
									entity.setUOM("NA");
								}	

								entity.setHSNCode(finalhsncode);

								if(row.getCell(25)!=null)
								{
									entity.setVAT((Double) row.getCell(25).getNumericCellValue());


								}else
								{
									entity.setVAT((double)0);
								}
								
								if(row.getCell(26)!=null)
								{
									entity.setVATCST((Double) row.getCell(26).getNumericCellValue());


								}else
								{
									entity.setVATCST((double)0);
								}
								
								if(row.getCell(27)!=null)
								{
									entity.setExcise((Double) row.getCell(27).getNumericCellValue());


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
								
									if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
									{
										if(supplier1.getTds_rate()!=null)
										{
											Tds = (transaction_value*supplier1.getTds_rate())/100;
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
							
							  if(product1!=null && count.equals(4) && supplier1!=null && entry.getVoucher_no()!=null)
							  {
								
									entry.setFlag(true);
									sucount=sucount+1;
									successVocharList.add(vocherNofromExcel);
									repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
									
					
								entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
								Long id1 = purEntryService.savePurchaseEntryThroughExcel(entry);
								entity.setPurchase_id(id1);
								purEntryService.savePurchaseEntryProductEntityClassThroughExcel(entity);
								
								PurchaseEntry purchase = purEntryService.getById(id1);
								 //stock
								if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
								{
								Integer pflag=productService.checktype(company_id,product1.getProduct_id());
							     if(pflag==1)  
							     {
							      Stock stock = null;
							      stock=stockService.isstockexist(company_id,product1.getProduct_id());
							      double amt=entity.getQuantity()*entity.getRate();
									if(stock==null)
									{
										Stock stockdata = new Stock();
										stockdata.setCompany_id(company_id);
										stockdata.setProduct_id(product1.getProduct_id());
										stockdata.setQuantity(entity.getQuantity());
										stockdata.setAmount(amt);
										Long id = stockService.saveStock(stockdata);
										stockService.addStockInfoOfProduct(id, company_id, product1.getProduct_id(), entity.getQuantity(), entity.getRate());
									}
									else
									{					
										stock.setAmount(stock.getAmount()+amt);
										stock.setQuantity(stock.getQuantity()+entity.getQuantity());					
										stockService.updateStock(stock);
										stockService.addStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(), entity.getQuantity(), entity.getRate());
									}
							     }  	
								
						        }
							     
								
								if(supplier1!=null)
								{
								try {
									suplliersService.addsuppliertrsansactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
											supplier1.getSupplier_id(), (long) 4, (double)round(round_off,2), (double)0, (long) 1);
								} catch (Exception e) {
									
									e.printStackTrace();
								}
								}
								
								try {
										if(subledger1!=null)
										{
											subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id, subledger1.getSubledger_Id(),
													(long) 2, (double)0, (double)transaction_value, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
										
										}
								    }
									catch (Exception e) {
										e.printStackTrace();
									}
							
								
								
								try{
									
									
									if (Tds>0) {
										SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", company_id);
										if(subledgertds!=null)
										{
										subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)Tds, (double)0, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
									    }
									}
									if(cgst>0)
									{
										SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST", company_id);
										if(subledgercgst!=null)
										{
										subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
												subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)cgst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
										}
									}
									
									if(sgst>0)
									{
										SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST", company_id);
										if(subledgersgst!=null)
										{
										subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
												subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)sgst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
										}
									}
									
									if(igst>0)
									{
										SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST", company_id);
										if(subledgerigst!=null)
										{
										subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
												subledgerigst.getSubledger_Id(), (long) 2, (double)0,(double)igst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
										}
									}
									
									if(state_comp_tax>0)
									{
										SubLedger subledgercess = subLedgerDAO.findOne("Input CESS", company_id);
										if(subledgercess!=null)
										{
										subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
												subledgercess.getSubledger_Id(), (long) 2, (double)0, (double)state_comp_tax,
												(long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
										}
									}
									if(vat>0)
									{
										SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", company_id);
										if(subledgervat!=null)
										{
										subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
												subledgervat.getSubledger_Id(), (long) 2, (double)0,(double)vat, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
										}
									}
									if(vatcst>0)
									{
										SubLedger subledgercst = subLedgerDAO.findOne("Input CST", company_id);
										if(subledgercst!=null)
										{
										subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
												subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)vatcst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
										}
									}
									if(excise>0)
									{
										SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE", company_id);
										if(subledgerexcise!=null)
										{
										subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getSupplier_bill_date(),company_id,
												subledgerexcise.getSubledger_Id(), (long) 2, (double)0,(double) excise, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
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
					return new ModelAndView("redirect:/purchaseEntryList");
				}
			    else if (m == 2){
					
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
					return new ModelAndView("redirect:/purchaseEntryList");
				}
			else
			{
				successmsg.append("You are inserting duplicate records, please check excel file");
				String successmsg1=successmsg.toString();
				session.setAttribute("filemsg", successmsg1);
				return new ModelAndView("redirect:/purchaseEntryList");
			}
			
		} else {
			successmsg.append("Please enter required and valid data in columns");
			String successmsg1=successmsg.toString();
			session.setAttribute("filemsg", successmsg1);
			return new ModelAndView("redirect:/purchaseEntryList");

		}
	}	
	
	@RequestMapping(value = "purchaseEntryFailure", method = RequestMethod.GET)
	public ModelAndView purchaseEntryFailure(HttpServletRequest request, HttpServletResponse response) {
	    
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
        flag = false;
        List<YearEnding>  yearEndlist = yearService.findAllYearEnding(company_id);
        
        if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
        
	//	model.addObject("entryList", purEntryService.findAllPurchaseEntryOfCompany(company_id, false,yearId));
		model.addObject("flagFailureList", false);
		model.addObject("yearEndlist", yearEndlist);
		model.setViewName("/transactions/purchaseEntryList");
		return model;
		}
		
	

	 @RequestMapping(value ="editproductdetailforPurchaseEntry", method = RequestMethod.GET)
		public ModelAndView editproductdetailforPurchaseEntry(@RequestParam("id") long id, 
				HttpServletRequest request, 
				HttpServletResponse response){
			HttpSession session = request.getSession(true);
			ModelAndView model = new ModelAndView();
			PurchaseEntryProductEntityClass entry = new PurchaseEntryProductEntityClass();
			User user = new User();
			TaxMaster productvat= new TaxMaster();
			user = (User) session.getAttribute("user");
			GstTaxMaster productgst =new GstTaxMaster();
			entry= purEntryService.editproductdetailforPurchaseEntry(id);
			PurchaseEntry PEentry = new PurchaseEntry();
			PEentry= purEntryService.findOneWithAll(entry.getPurchase_id());
			Product productgst1 = productService.findOneView(entry.getProduct_id());
			
			if(productgst1.getHsn_san_no()!=null)
			{
				productgst=gstService.getHSNbyDate(PEentry.getSupplier_bill_date(),productgst1.getHsn_san_no());
			}
			else
			{
				productgst=gstService.getHSNbyDate(PEentry.getSupplier_bill_date(),entry.getHSNCode());
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
			model.addObject("PEentry", PEentry);
			model.addObject("entry", entry);
			model.addObject("productgst", productgst);
			model.addObject("stateId",user.getCompany().getState().getState_id());
			model.setViewName("/transactions/editproductdetailforPurchaseEntry");	
			return model;		
		}
	 
	 
	 @RequestMapping(value ="saveproductdetailforPurchaseEntry", method = RequestMethod.POST)
		public synchronized ModelAndView saveproductdetailforPurchaseEntry(@ModelAttribute("entry")PurchaseEntryProductEntityClass entry, 
				BindingResult result, 
				HttpServletRequest request, 
				HttpServletResponse response) {
			
		 
			HttpSession session = request.getSession(true);
			long company_id=(long)session.getAttribute("company_id");
			
		    Double transaction_value = (double)0;
			Double cgst = (double)0;
			Double tds=(double)0;
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
			Suppliers supplier1 = new Suppliers();
			SubLedger subledger1 = new SubLedger();
			
			 ModelAndView model = new ModelAndView();
			 
			 editProValidator.validate(entry, result);
				if(result.hasErrors()){

					model.setViewName("/transactions/editproductdetailforPurchaseEntry");			
					return model;
				}
				else
				{
					PurchaseEntryProductEntityClass purchasedetails = new PurchaseEntryProductEntityClass();
					
					purchasedetails= purEntryService.editproductdetailforPurchaseEntry(entry.getPurchase_detail_id());
					PurchaseEntry purchase = new PurchaseEntry();
					
					try {			
						purchase=purEntryService.findOneWithAll(entry.getPurchase_id());
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					Integer tdsapply=purchase.getSupplier().getTds_applicable();
					
					if(purchase.getSupplier().getTds_rate()!=null)
					{
						tdsrate=(double)purchase.getSupplier().getTds_rate();
					}
					if(purchase.getSupplier()!=null)
					{
				    supplier1=purchase.getSupplier();
					}
					if(purchase.getSubledger()!=null)
					{
					subledger1=purchase.getSubledger();
					}
					
					if(purchase.getTransaction_value()!=null)
					{
					transaction_value= purchase.getTransaction_value();
					}
					if(purchase.getRound_off()!=null)
					{
					round_off=purchase.getRound_off();
					}
					if(purchase.getTds_amount()!=null)
					{
						tds=purchase.getTds_amount();
					}
					if(purchase.getCgst()!=null)
					{
					cgst=purchase.getCgst();
					}
					if(purchase.getIgst()!=null)
					{
					igst=purchase.getIgst();
					}
					if(purchase.getSgst()!=null)
					{
					sgst=purchase.getSgst();
					}
					if(purchase.getState_compansation_tax()!=null)
					{
					state_comp_tax=purchase.getState_compansation_tax();
					}
					
					if(purchase.getTotal_vat()!=null)
					{
					vat=purchase.getTotal_vat();
					}
					
					if(purchase.getTotal_vatcst()!=null)
					{
					vatcst=purchase.getTotal_vatcst();
					}
					if(purchase.getTotal_excise()!=null)
					{
					excise=purchase.getTotal_excise();
					}
					
					if(purchasedetails.getCGST()!=null)
					{
					CGST=purchasedetails.getCGST();
					}
					if(purchasedetails.getIGST()!=null) {
					IGST=purchasedetails.getIGST();
					}
					if(purchasedetails.getSGST()!=null)
					{
					SGST=purchasedetails.getSGST();
					}
					if(purchasedetails.getState_com_tax()!=null)
					{
					state_com_tax=purchasedetails.getState_com_tax();
					}
					if(purchasedetails.getTransaction_amount()!=null)
					{
					transaction_amount=purchasedetails.getTransaction_amount();
					}
					
					if(purchasedetails.getVAT()!=null)
					{
					VAT=purchasedetails.getVAT();
					}
					if(purchasedetails.getVATCST()!=null)
					{
					VATCST=purchasedetails.getVATCST();
					}
					if(purchasedetails.getExcise()!=null) 
					{
					Excise=purchasedetails.getExcise();
					}
					
					if(VAT==0 && VATCST==0 && Excise==0)
					{
					cgst=cgst-CGST;
					igst=igst-IGST;
					sgst=sgst-SGST;
					state_comp_tax= state_comp_tax-state_com_tax;
					if(tdsapply==1)
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
					
					if(entry.getDiscount()!=null && entry.getDiscount()!=0) 
					{
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
					if(tdsapply==1)
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
						
						if(tdsapply==1)
						{
							tdsnew1=(transaction_value*tdsrate)/100;
						}
						transaction_value=transaction_value-transaction_amount;
						roundamount=VAT+VATCST+Excise+transaction_amount;
						
						round_off=round_off-roundamount+tdsnew1;
						
						
						vat=vat+entry.getVAT();
						vatcst =vatcst +entry.getVATCST();
						excise =excise+entry.getExcise();
						
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
						if(tdsapply==1)
						{
							tdsnew=(transaction_value*tdsrate)/100;
						}
						else 
							tdsnew=(double)0;
						
					}
					
					round_off=round_off-tdsnew;
					
					if(supplier1!=null)
					{
                        try {
                        	suplliersService.addsuppliertrsansactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
                        			supplier1.getSupplier_id(), (long) 4, (double)purchase.getRound_off(), (double)0,
        							(long) 0);
                        	
                        	suplliersService.addsuppliertrsansactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
                        			supplier1.getSupplier_id(), (long) 4, (double)round(round_off,2), (double)0, (long) 1);
						} catch (Exception e) {
							
							e.printStackTrace();
						}
						
					}
					
					if(subledger1!=null)
					{
						try {
							subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
									subledger1.getSubledger_Id(), (long) 2, (double)0, (double)purchase.getTransaction_value(),
									(long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
							
							subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
									subledger1.getSubledger_Id(), (long) 2, (double)0, (double)transaction_value, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
								
								}
							catch (Exception e) {
								e.printStackTrace();
							}
					}
					try {
							if(purchase.getAgainst_advance_Payment()!=null && purchase.getAgainst_advance_Payment()==false)
							{
								SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", purchase.getCompany().getCompany_id());
								subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)tds, (double)0, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
								subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)tdsnew, (double)0, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
							}							
														
								SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST",
										company_id);								
								if (subledgercgst != null) {
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)purchase.getCgst(), (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
									
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)cgst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
							
							    }
							
							
								SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST",
										company_id);
								
								if (subledgersgst != null) {
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)purchase.getSgst(), (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
									
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)sgst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
								}
							
						
							
								SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST",
										company_id);
								
								if (subledgerigst != null) {
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgerigst.getSubledger_Id(), (long) 2, (double)0, (double)purchase.getIgst(), (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
									
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgerigst.getSubledger_Id(), (long) 2, (double)0, (double)igst, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
								}
							
							
								SubLedger subledgercess = subLedgerDAO.findOne("Input CESS",
										company_id);
								
								if (subledgercess != null) {
									
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgercess.getSubledger_Id(), (long) 2, (double)0,
											(double)purchase.getState_compansation_tax(), (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
									
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgercess.getSubledger_Id(), (long) 2, (double)0,
											(double)state_comp_tax, (long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
								}
							
							
								SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", company_id);
								if (subledgervat != null) {
									
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgervat.getSubledger_Id(), (long) 2, (double)0, (double)purchase.getTotal_vat(),
											(long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
									
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgervat.getSubledger_Id(), (long) 2, (double)0, (double)vat,
											(long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
								}
							
							
							
								SubLedger subledgercst = subLedgerDAO.findOne("Input CST", company_id);
								
								if (subledgercst != null) {
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)purchase.getTotal_vatcst(),
											(long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
									
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)vatcst,
											(long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
								}
							
						
							
								SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE",
										company_id);
								
								if (subledgerexcise != null) {
									
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgerexcise.getSubledger_Id(), (long) 2, (double)0, (double)purchase.getTotal_excise(),
											(long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
									
									subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),company_id,
											subledgerexcise.getSubledger_Id(), (long) 2, (double)0, (double)excise,
											(long) 1,null,null,purchase,null,null,null,null,null,null,null,null,null);
								}
							
							
							
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
						
						purchase.setCgst(round(cgst,2));
						purchase.setIgst(round(igst,2));
						purchase.setSgst(round(sgst,2));
						purchase.setState_compansation_tax(round(state_comp_tax,2));
						purchase.setTotal_vat(vat);
						purchase.setTotal_vatcst(vatcst);
						purchase.setTotal_excise(excise);
						purchase.setTransaction_value(round(transaction_value,2));
						purchase.setRound_off(round(round_off,2));
						purchase.setTds_amount(tdsnew);
						purEntryService.updatePurchaseEntry(purchase);
					/*Double oldqty=purchasedetails.getQuantity();//old quantity
					Double newqty = entry.getQuantity();//new quantity	
					Double newRate= entry.getRate();//new rate
					double oldamt=purchasedetails.getQuantity()*purchasedetails.getRate();//old amount
				    double newmt=entry.getQuantity()*entry.getRate();//new amount	
					purEntryService.updatePurchaseEntry(purchase);
					if(!purchasedetails.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
					{
						Integer pflag=productService.checktype(company_id,purchasedetails.getProduct_id());
					     if(pflag==1)  
					     {
					      Stock stock = null;
					      stock=stockService.isstockexist(company_id,purchasedetails.getProduct_id());
					      if(stock!=null)
					      {
				       		stock.setQuantity((stock.getQuantity()-oldqty)+newqty);
								stock.setAmount(stock.getAmount()-oldamt+newmt);
								stockService.updateStock(stock);
								stockService.addStockInfoOfProduct(stock.getStock_id(), company_id, purchasedetails.getProduct_id(), newqty,newRate );
					      }
						}
				    }	*/	
					
					
					purchasedetails.setQuantity(entry.getQuantity());
					purchasedetails.setRate(entry.getRate());
					purchasedetails.setDiscount(entry.getDiscount());
					purchasedetails.setCGST(entry.getCGST());
					purchasedetails.setIGST(entry.getIGST());
					purchasedetails.setSGST(entry.getSGST());
					purchasedetails.setState_com_tax(entry.getState_com_tax());
					purchasedetails.setLabour_charges(entry.getLabour_charges());
					purchasedetails.setFreight(entry.getFreight());
					purchasedetails.setOthers(entry.getOthers());
					purchasedetails.setUOM(entry.getUOM());
					purchasedetails.setVAT(entry.getVAT());
					purchasedetails.setVATCST(entry.getVATCST());
					purchasedetails.setExcise(entry.getExcise());
					purchasedetails.setTransaction_amount(new_transaction_amount);
					purchasedetails.setIs_gst(entry.getIs_gst());
					purEntryService.updatePurchaseEntryProductEntityClass(purchasedetails);
					return new ModelAndView("redirect:/editPurchaseEntry?id="+purchase.getPurchase_id());
				}
		}
	 
	 public static Double round(Double d, int decimalPlace) {
	    	DecimalFormat df = new DecimalFormat("#");
			df.setMaximumFractionDigits(decimalPlace);
			return new Double(df.format(d));
	    }

@RequestMapping(value="getOpeningBalanceforpurchase", method=RequestMethod.GET)
		public @ResponseBody Double  getOpeningBalance(@RequestParam("supplierid") Long supplierid,HttpServletRequest request, HttpServletResponse response)
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

		OpeningBalances purchaseopening = openingbalances.isOpeningBalancePresentPurchase(date, company_id, supplierid); 
																														
		if(purchaseopening.getCredit_balance()!=null && purchaseopening.getDebit_balance()!=null)
		{
		creditbalance = purchaseopening.getCredit_balance();
		debitbalance = purchaseopening.getDebit_balance();
		}
		if(debitbalance>0)
		{
		
		List<PurchaseEntry> purchaseaginstopening=	purEntryService.getAllOpeningBalanceAgainstPurchase(supplierid, company_id);
		for(PurchaseEntry purchase:purchaseaginstopening) {
			
			amount=amount+purchase.getRound_off()+purchase.getTds_amount();
		}
		
		
				if (creditbalance > 0) {
					total=creditbalance;
				} else {
					total=debitbalance;
				}
		}
				total=total-amount;
				
				return total;
		
	}



@RequestMapping(value = "getTDSRate", method = RequestMethod.GET )
public @ResponseBody Float getTDSRate(@RequestParam(value="effectiveDate") String effectiveDate,@RequestParam(value="tdsTypeId") long tdsTypeId,
		HttpServletRequest request,
		HttpServletResponse response){
	LocalDate newdate=new LocalDate(effectiveDate);
	System.out.println("getTdsrate "+tdsTypeId);
	Float tdsrate=deducteeService.getTDSRate(newdate, tdsTypeId);
	System.out.println("rate is  "+tdsrate);
	return  tdsrate;
	
}

}