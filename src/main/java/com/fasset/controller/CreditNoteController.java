package com.fasset.controller;

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
import com.fasset.controller.validators.CreditDetailValidator;
import com.fasset.controller.validators.CreditNoteValidator;
import com.fasset.dao.interfaces.ICreditNoteDAO;
import com.fasset.dao.interfaces.ISalesEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.CreditDetails;
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
import com.fasset.entities.Service;
import com.fasset.entities.Stock;
import com.fasset.entities.SubLedger;
import com.fasset.entities.TaxMaster;
import com.fasset.entities.UnitOfMeasurement;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.report.controller.ClassToConvertDateForExcell;
import com.fasset.service.interfaces.IAccountingYearService;
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
import com.fasset.service.interfaces.IStockService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ITaxMasterService;
import com.fasset.service.interfaces.IUOMService;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IYearEndingService;

@Controller
@SessionAttributes("user")
public class CreditNoteController extends MyAbstractController {
	
	@Autowired
	private ICompanyService companyService ;

	@Autowired
	private ICreditNoteService creditNoteService;

	@Autowired
	private ICustomerService customerService;

	
	@Autowired
	private IAccountingYearService accountingYearService;

	@Autowired
	private CreditNoteValidator creditNoteValidator;

	@Autowired
	private ISalesEntryService entryService;

	@Autowired
	private IProductService productService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IUOMService uomService;
	
	@Autowired
	private IOpeningBalancesService openingbalances;
	
	@Autowired
	private ISubLedgerDAO subledgerDao;
	
	@Autowired
	private ITaxMasterService taxservice;
	
	@Autowired
	private IGstTaxMasterService gstService;
	
	@Autowired
	private IStockService stockService;
	
	@Autowired
	private ISubLedgerService subLedgerService;
	
	@Autowired
	private CreditDetailValidator creditDetailValidator;
	
	@Autowired	
	private IYearEndingService yearService;

	@Autowired
	private ICreditNoteDAO creditNoteDao;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ISalesEntryDAO salesEntryDao;	
	
	@Autowired
	private IQuotationService quoteservice;

	@Autowired
	private ISalesEntryService SalesEntryService;
	@Autowired	
	private ICreditNoteService creditService;
	@Autowired
	private IReceiptService receiptService ;
	
	@Autowired
	private IClientSubscriptionMasterService subscribeservice;
	
	private Set<String> successVocharList = new HashSet<String>();

	private Set<String> failureVocharList = new HashSet<String>();

	Boolean flag = null;
	
	@RequestMapping(value = "creditNoteList", method = RequestMethod.GET)
	public ModelAndView creditNoteList(@RequestParam(value = "msg", required = false) String msg,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		boolean importfail = false;
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
		List<YearEnding>  yearEndlist = yearService.findAllYearEnding(company.getCompany_id());
		flag = true;
		if ((String) session.getAttribute("msg") != null) {
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}

		if ((String) session.getAttribute("filemsg") != null) {
			model.addObject("filemsg", ((String) session.getAttribute("filemsg")));
			model.addObject("filemsg1", ((String) session.getAttribute("filemsg1")));
			model.addObject("successMsg", "NA");
			model.addObject("successVocharList", successVocharList);
			model.addObject("failureVocharList", failureVocharList);
			

			session.removeAttribute("filemsg");
			session.removeAttribute("filemsg1");
		}

		//code for year
		//List<AccountingYear>  yearList = accountingYearService.findAccountRange(user.getUser_id(), user.getCompany().getYearRange(),company_id);

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
		/*for(AccountingYear year:yearList)
		{
			if(year.getYear_range().equalsIgnoreCase(currentyear))
			{
			}
		}
*/
		
		//List<CreditNote> creditNoteList = creditNoteService.findAllCreditNoteOfCompany(company_id, true);
		if (creditNoteService.findAllCreditNoteOfCompany(company_id, false).size() != 0) {
			importfail = true;
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
		model.addObject("creditNoteList", creditNoteService.findAllCreditNoteOfCompany(company_id, true));
		model.addObject("importfail", importfail);
		model.addObject("flagFailureList", true);
		model.addObject("yearEndlist", yearEndlist);
		model.addObject("company", company);
		
		
		model.setViewName("/transactions/creditNoteList");
		return model;
	}

	@RequestMapping(value = "creditNote", method = RequestMethod.GET)
	public ModelAndView creditNote(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long company_id = (long) session.getAttribute("company_id");
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		String yearrange = user.getCompany().getYearRange();
		long user_id = (long) session.getAttribute("user_id");
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		System.out.println("str "+strLong);
		//List<AccountingYear>  yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		System.out.println("yearList "+yearList.size());
		 if(AccYear==-1){
				yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
			}else{
				yearList = accountingYearService.findAccountRangeOfCompany(strLong);
			 
			}
		if(yearList.size()!=0)
		{
		model.addObject("creditNote", new CreditNote());
		model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
		model.addObject("entryList", entryService.findAllactiveSalesEntryOfCompany(company_id, true));
		model.addObject("subLedgerList",  subLedgerService.findAllSubLedgersOnlyOfCompany(company_id));
		model.addObject("stateId", user.getCompany().getState().getState_id());
		model.addObject("yearList",  yearList);
		model.addObject("products", productService.findAllProductsOfCompany(company_id));
		
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
		model.setViewName("/transactions/creditNote");
		return model;
		}
		else
		{
			session.setAttribute("msg","Your account is closed");
			return new ModelAndView("redirect:/creditNoteList");
			
		}	
		
	   }

	@RequestMapping(value = "creditNote", method = RequestMethod.POST)
	public synchronized ModelAndView creditNote(@ModelAttribute("creditNote") CreditNote creditNote, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long company_id = (long) session.getAttribute("company_id");
		String yearrange = user.getCompany().getYearRange();
		long user_id = (long) session.getAttribute("user_id");
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		Long id = new Long(0);
		creditNoteValidator.validate(creditNote, result);
		if (result.hasErrors()) {
			if(AccYear==-1){
				yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
			}else{
				yearList = accountingYearService.findAccountRangeOfCompany(strLong);
			 
			}
			model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
			model.addObject("stateId", user.getCompany().getState().getState_id());
			model.addObject("entryList", entryService.findAllactiveSalesEntryOfCompany(company_id, true));
			//model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
			model.addObject("yearList",yearList);
			model.addObject("products", productService.findAllProductsOfCompany(company_id));
			model.addObject("subLedgerList", subLedgerService.findAllSubLedgersOnlyOfCompany(company_id));
			model.setViewName("/transactions/creditNote");
			return model;
		} else {
			String msg = "";
			try {
				id = creditNote.getCredit_no_id();

				if (id == null) {
					creditNote.setCompany(user.getCompany());
					creditNote.setDate(new LocalDate(creditNote.getDateString()));
					creditNote.setVoucher_no(commonService.getVoucherNumber("CN", VOUCHER_CREDIT_NOTE,
					creditNote.getDate(), creditNote.getAccountYearId(), user.getCompany().getCompany_id()));
					creditNote.setFlag(true);
					if(creditNote.getTds_amount()==null){
						System.out.println("tds null");
						creditNote.setTds_amount(0.0);
					}
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            creditNote.setIp_address(remoteAddr);
			            creditNote.setCreated_by(user_id);
					creditNoteService.add(creditNote);
					msg = "Credit Note added successfully";
				} else {
		            creditNote.setUpdated_by(user_id);
					creditNoteService.update(creditNote);
					msg = "Credit Note updated successfully";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			session.setAttribute("msg", msg);
			return new ModelAndView("redirect:/creditNoteList");
		}

	}

	@RequestMapping(value = "viewCreditNote", method = RequestMethod.GET)
	public ModelAndView viewCreditNote(@RequestParam("id") Long id) throws MyWebException {
		ModelAndView model = new ModelAndView();
		CreditNote creditNote = new CreditNote();
		double closingbalance = (double)0;
		try {
			if (id > 0) {
				creditNote = creditNoteService.findOneView(id);
			
				if (creditNote.getCreditDetails() != null && creditNote.getCreditDetails().size()>0) {
					model.addObject("creditDetailsList", creditNote.getCreditDetails());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(creditNote.getCustomer()!=null)
		{
			closingbalance =openingbalances.getclosingbalance(creditNote.getCompany().getCompany_id(),creditNote.getAccounting_year_id().getYear_id(),creditNote.getDate(),creditNote.getCustomer().getCustomer_id(),5);
		}
		
		model.addObject("closingbalance", closingbalance);
		model.addObject("creditNote", creditNote);
		model.addObject("flag", flag);
		if(creditNote.getCreated_by()!=null)
		{
		model.addObject("created_by", userService.getById(creditNote.getCreated_by()));
		}
		if(creditNote.getUpdated_by()!=null)
		{
		model.addObject("updated_by", userService.getById(creditNote.getUpdated_by()));
		}
		model.setViewName("/transactions/creditNoteView");
		return model;
	}

	@RequestMapping(value = "editCreditNote", method = RequestMethod.GET)
	public synchronized ModelAndView editCreditNote(@RequestParam("id") Long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		User user = (User) session.getAttribute("user");
		long company_id = (long) session.getAttribute("company_id");
		CreditNote creditNote = new CreditNote();
		
		creditNote = creditNoteService.findOneView(id);
		SalesEntry entry = creditNote.getSales_bill_id();
		
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		if(new Double((creditNote.getRound_off()+creditNote.getTds_amount())).equals(new Double(entry.getRound_off()+entry.getTds_amount())))
		{
			session.setAttribute("msg", "You can't edit credit note as you have already return your all products for Sales Entry.");
			return new ModelAndView("redirect:/creditNoteList");
		}
		else if((entry.getReceipts().size()==0))
		{
		try {
			
			if(creditNote.getAccounting_year_id()!=null)
			{
			creditNote.setAccountYearId(creditNote.getAccounting_year_id().getYear_id());
			}
			if(creditNote.getDate()!=null)
			{
			creditNote.setDateString(creditNote.getDate().toString());
			}
			if(creditNote.getCustomer()!=null)
			{
			creditNote.setCustomerId(creditNote.getCustomer().getCustomer_id());
			}
			if(creditNote.getCustomer()!=null)
			{
			creditNote.setCustomer(creditNote.getCustomer());
			}
			if(creditNote.getSales_bill_id()!=null)
			{
			creditNote.setSalesEntryId(creditNote.getSales_bill_id().getSales_id());
			}
			if(creditNote.getSubledger()!=null)
			{
			creditNote.setSubledgerId(creditNote.getSubledger().getSubledger_Id());
			}
			if (creditNote.getCreditDetails() != null && creditNote.getCreditDetails().size()>0) {
				model.addObject("creditDetailsList", creditNote.getCreditDetails());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		 
		model.addObject("creditNote", creditNote);
		model.addObject("customerList", customerService.findAllCustomersOfCompanyInclPending(company_id));
		model.addObject("subLedgerList", subLedgerService.findAllSubLedgersOnlyOfCompany(company_id));
		model.addObject("stateId", user.getCompany().getState().getState_id());
		model.addObject("entryList", entryService.findAllactiveSalesEntryOfCompany(company_id, true));
		model.addObject("yearList", accountingYearService.findAll());
		model.addObject("products", productService.findAllProductsOfCompany(company_id));
		model.setViewName("/transactions/creditNote");
	}else
		{
			session.setAttribute("msg", "You can't edit credit voucher as receipt voucher is already generated.");
			model.setViewName("redirect:/creditNoteList");
		}
		return model;
	}

	@RequestMapping(value = "deleteCreditNote", method = RequestMethod.GET)
	public synchronized ModelAndView deleteCreditNote(@RequestParam("id") Long id, HttpServletRequest request,
			HttpServletResponse response) {
System.out.println("The credit note id is "+id);
		HttpSession session = request.getSession(true);
		CreditNote creditNote = new CreditNote();
		creditNote = creditNoteService.findOneView(id);
		SalesEntry entry = creditNote.getSales_bill_id();
		
		if(new Double((creditNote.getRound_off()+creditNote.getTds_amount())).equals(new Double(entry.getRound_off()+entry.getTds_amount())))
		{
			session.setAttribute("msg", "You can't delete credit note as you have already return your all products for Sales Entry.");
			return new ModelAndView("redirect:/creditNoteList");
		}
		else if((entry.getReceipts().size()==0))
		{
            session.setAttribute("msg",  creditNoteService.deleteByIdValue(id));
			
			if(flag == true)
			{
				return new ModelAndView("redirect:/creditNoteList");
			}
			else
			{
				return new ModelAndView("redirect:/creditNoteFailure");
			}
			
		}
		else
		{
			session.setAttribute("msg", "You can't edit credit voucher as receipt voucher is already generated.");
			return new ModelAndView("redirect:/creditNoteList");
		}
	}

	@RequestMapping(value = "getSalesProductDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getSalesProductDetails(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
System.out.println("getsalesproddetls");
		List<SalesEntryProductEntityClass> productDetails = entryService.findAllSalesEntryProductEntityList(id);
		JSONArray jsonArray = new JSONArray();
		for (SalesEntryProductEntityClass entry : productDetails) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("product_id", entry.getProduct_id());
			jsonObj.put("product_name", entry.getProduct_name());
			jsonObj.put("quantity", entry.getQuantity());
			jsonObj.put("rate", entry.getRate());
			jsonObj.put("discount", entry.getDiscount());
			jsonObj.put("labour_charges", entry.getLabour_charges());
			jsonObj.put("freight", entry.getFreight());
			jsonObj.put("others", entry.getOthers());
			jsonObj.put("CGST", entry.getCGST());
			jsonObj.put("SGST", entry.getSGST());
			jsonObj.put("IGST", entry.getIGST());
			jsonObj.put("state_com_tax", entry.getState_com_tax());
			jsonArray.put(jsonObj);
		}

		return jsonArray.toString();
	}
	 
	@RequestMapping(value ="editCreditDetails", method = RequestMethod.GET)
	public ModelAndView editCreditDetails(@RequestParam("id") long id, 
				HttpServletRequest request, 
				HttpServletResponse response){

		 ModelAndView model = new ModelAndView();
		 HttpSession session = request.getSession(true);
		 GstTaxMaster productgst =new GstTaxMaster();
		 TaxMaster productvat= new TaxMaster();
		 try {
			 long company_id=(long)session.getAttribute("company_id");
			 Company company = companyService.getById(company_id);
			 CreditDetails creditDetails = creditNoteService.getCreditDetails(id);			
			 CreditNote creditNote = creditNoteService.findOneView(creditDetails.getCredit_id().getCredit_no_id());
			 if(creditDetails.getCredit_id()!=null)
			 {
			 creditDetails.setCreditId(creditDetails.getCredit_id().getCredit_no_id());
			 }
			 if(creditDetails.getProduct_id()!=null)
			 {
			 creditDetails.setProductId(creditDetails.getProduct_id().getProduct_id());
			 }
			 if(creditNote.getCustomer()!=null)
			 {
			 creditNote.setCustomer(creditNote.getCustomer());
			 }
			 
			 if(creditNote.getSales_bill_id()!=null && creditDetails.getProduct_id()!=null && creditDetails.getProduct_id().getHsn_san_no()!=null)
			 {
			 productgst=gstService.getHSNbyDate(creditNote.getSales_bill_id().getCreated_date(),creditDetails.getProduct_id().getHsn_san_no());
			 }
			 else if(creditNote.getSales_bill_id()!=null && creditDetails.getProduct_id()!=null && creditDetails.getProduct_id().getHsn_san_no()==null)
				{
				    SalesEntry salesentry = null;
					if(creditNote.getSales_bill_id()!=null)
					{
						salesentry = creditNote.getSales_bill_id();
					}
				 
					if(salesentry!=null)
					{
						
						for(SalesEntryProductEntityClass salesentity : entryService.findAllSalesEntryProductEntityList(salesentry.getSales_id()))
						{
							
							if(creditDetails.getProduct_id()!=null && creditDetails.getProduct_id().getProduct_id().equals(salesentity.getProduct_id()))
							{
								productgst= gstService.getHSNbyDate(salesentry.getCreated_date(),salesentity.getHSNCode());
								
							}
							
						}
					}
					
				}
			 
			 if(creditDetails.getProduct_id().getTaxMaster()!=null)
				{
					try {
						productvat=taxservice.getById(creditDetails.getProduct_id().getTaxMaster().getTax_id());
						model.addObject("productvat", productvat);
					} catch (MyWebException e) {
						e.printStackTrace();
					}
				}
			 
			 model.addObject("productgst", productgst);
			 model.addObject("creditDetails", creditDetails);
			 model.addObject("compState", company.getState().getState_id());
			 if(creditNote.getCustomer()!=null)
			 {
			 model.addObject("custState", creditNote.getCustomer().getState().getState_id());
			 model.addObject("customer", creditNote.getCustomer());
			 }
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
		 model.setViewName("/transactions/editCreditDetails");
		 return model;		
	 }
	 
	 @RequestMapping(value ="saveCreditDetails", method = RequestMethod.POST)
	 public synchronized ModelAndView saveCreditDetails(@ModelAttribute("creditDetails")CreditDetails creditDetails, 
				BindingResult result, 
				HttpServletRequest request, 
				HttpServletResponse response){

		ModelAndView model = new ModelAndView();
		try {
			creditDetails.setProduct_id(productService.getById(creditDetails.getProductId()));
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		creditDetailValidator.validate(creditDetails, result);
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		try {
			if(result.hasErrors()) {
				/*Company company = companyService.getById(company_id);
				 CreditNote creditNote = creditNoteService.findOneView(creditDetails.getCredit_id().getCredit_no_id());
				 model.addObject("compState", company.getState().getState_id());
				 if(creditNote.getCustomer()!=null)
				 {
				 model.addObject("custState", creditNote.getCustomer().getState().getState_id());
				 model.addObject("customer", creditNote.getCustomer());
				 }*/
				GstTaxMaster productgst =new GstTaxMaster();
				TaxMaster productvat= new TaxMaster();
				 try {
					 Company company = companyService.getById(company_id);
					 CreditDetails details = creditNoteService.getCreditDetails(creditDetails.getCredit_detail_id());			
					 CreditNote creditNote = creditNoteService.findOneView(details.getCredit_id().getCredit_no_id());
					 if(details.getCredit_id()!=null)
					 {
						 details.setCreditId(details.getCredit_id().getCredit_no_id());
					 }
					 if(details.getProduct_id()!=null)
					 {
						 details.setProductId(details.getProduct_id().getProduct_id());
					 }
					 if(creditNote.getCustomer()!=null)
					 {
					 creditNote.setCustomer(creditNote.getCustomer());
					 }
					 
					 if(creditNote.getSales_bill_id()!=null && creditDetails.getProduct_id()!=null && creditDetails.getProduct_id().getHsn_san_no()!=null)
					 {
					 productgst=gstService.getHSNbyDate(creditNote.getSales_bill_id().getCreated_date(),creditDetails.getProduct_id().getHsn_san_no());
					 }
					 else if(creditNote.getSales_bill_id()!=null && creditDetails.getProduct_id()!=null && creditDetails.getProduct_id().getHsn_san_no()==null)
						{
							productgst=gstService.getHSNbyDate(creditNote.getSales_bill_id().getCreated_date(),creditDetails.getHSNCode());
						}
					 if(details.getProduct_id().getTaxMaster()!=null)
						{
							try {
								productvat=taxservice.getById(details.getProduct_id().getTaxMaster().getTax_id());
								model.addObject("productvat", productvat);
							} catch (MyWebException e) {
								e.printStackTrace();
							}
						}
					 model.addObject("productgst", productgst);
					 model.addObject("creditDetails", details);
					 model.addObject("compState", company.getState().getState_id());
					 if(creditNote.getCustomer()!=null)
					 {
					 model.addObject("custState", creditNote.getCustomer().getState().getState_id());
					 model.addObject("customer", creditNote.getCustomer());
					 }
				 }
				 catch(Exception e) {
					 e.printStackTrace();
				 }
				 model.setViewName("/transactions/editCreditDetails");
			}
			else {
				creditNoteService.updateCreditDetails(creditDetails, company_id);
				model.setViewName("redirect:/editCreditNote?id="+creditDetails.getCreditId());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return model;		
	 }

	@RequestMapping(value = "deleteCreditDetails", method = RequestMethod.GET)
	public synchronized ModelAndView deleteCreditDetails(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		CreditDetails creditDetails = creditNoteDao.getCreditDetailsById(id);
		Long creditNoteId = creditDetails.getCredit_id().getCredit_no_id();
		CreditNote creditNote = creditNoteDao.findOneView(creditNoteId);
		if(creditNote.getCreditDetails().size()!=1)
		{
		 creditNoteService.deleteByCebitNoteDetailsId(id, company_id);
		}
		else
		{
			 String msg = "You can't delete this product. You can edit this or you can add your required product first and then delete this product.";
	    	 session.setAttribute("msg", msg);
		}
		model.setViewName("redirect:/editCreditNote?id=" + creditNoteId);
		return model;

	}

	@RequestMapping(value = "downloadCreditNote", method = RequestMethod.GET)
	public ModelAndView downloadCreditNote(@RequestParam("id") long id) {
		return new ModelAndView("CreditNotePdfView", "creditNote", creditNoteService.findOneView(id));
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = { "importExcelCredit" }, method = { RequestMethod.POST })
	public ModelAndView importExcelCredit(@RequestParam("excelFile") MultipartFile excelfile,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		boolean isValid = true;

		StringBuffer successmsg = new StringBuffer();
		StringBuffer failuremsg = new StringBuffer();

		String succeescount = null;
		String failuarecount = null;

		Integer failcount = 0;
		Integer sucount = 0;
		Integer maincount = 0;
		Integer repetProductafterunsuccesfullattempt = 0;

		successVocharList.clear();
		failureVocharList.clear();

		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		long user_id = (long) session.getAttribute("user_id");
		User user = new User();
		user = (User) session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		List<Customer> list = customerService.findAllCustomersOnlyOFCompany(company_id);
		List<SubLedger> subLedgerList = subLedgerService.findAllSubLedgersOnlyOfCompany(company_id);
		List<UnitOfMeasurement> uomList = uomService.findAllListing();
		List<SalesEntry> saleslist = entryService.findAllSalesEntriesOfCompany(company_id,false);
		List<AccountingYear> yearlist =accountingYearService.findAccountRange(user_id, yearrange,company_id);
		Integer m = 0;
		

		try {
			if (excelfile.getOriginalFilename().endsWith("xls")) {
				System.out.println("file is xls...");
				int i = 0;
				HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
				HSSFSheet worksheet = workbook.getSheetAt(0);
				if (isValid) {
					i = 1;
					while (i <= worksheet.getLastRowNum()) {
						CreditNote note = new CreditNote();
						CreditDetails entity = new CreditDetails();
						Customer customer1 = null;
						SubLedger subledger1 = null;
						Bank bank1 = null;
						SalesEntry salesEntry1 = null;
						AccountingYear year_range1 = null;
						Product product1 = null;
						
						HSSFRow row = worksheet.getRow(i++);
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
						    double transaction_value = (double) 0;
						    double cgst = (double) 0;
						    double igst = (double) 0;
						    double sgst = (double) 0;
						    double state_comp_tax = (double) 0;
						    double round_off = (double) 0;
						    double vat = (double) 0;
						    double vatcst = (double) 0;
						    double excise = (double) 0;
						    double tds = (double) 0;
						    double NewTds = (double)0;
							int count = 0;
							
							double VAT = (double) 0;
							double VATCST = (double) 0;
							double Excise = (double) 0;
							
							boolean flag = false;

							String creditVocherNofromExcel = "";
							String salesVocherNofromExcel = "";

							try {
								creditVocherNofromExcel = row.getCell(2).getStringCellValue().trim().replaceAll(" ", "");;
							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								Double voucherNofromExcel = row.getCell(2).getNumericCellValue();
								Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
								creditVocherNofromExcel = voucherNofromExcel1.toString().trim();

							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								salesVocherNofromExcel = row.getCell(4).getStringCellValue().trim();
							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								Double shipingBillNfromExcel = row.getCell(4).getNumericCellValue();
								Integer shipingBillNfromExcel1 = shipingBillNfromExcel.intValue();
								salesVocherNofromExcel = shipingBillNfromExcel1.toString().trim();

							} catch (Exception e) {
								e.printStackTrace();
							}

							CreditNote oldentry = creditNoteService.isExcelVocherNumberExist(creditVocherNofromExcel, company_id);
					    		if (oldentry!=null) 
					    		{
									Set<CreditDetails> creditDetailsList = oldentry.getCreditDetails();
									Boolean mainentryflag = false;
									String finalhsncode = "1";
									
									if (creditDetailsList != null && creditDetailsList.size() != 0) {
										String vocherNo = oldentry.getExcel_voucher_no().trim();
										Long company_Id = oldentry.getCompany().getCompany_id();
										String ProductName = row.getCell(8).getStringCellValue();
										
										if (row.getCell(7) != null) {
											Double hsncode = row.getCell(7).getNumericCellValue();
											Integer hsncode1 = hsncode.intValue();
											finalhsncode = hsncode1.toString().trim();
										}
										
										Boolean entryflag = false;

										for (CreditDetails cD : creditDetailsList) {
											if (finalhsncode.replace(" ","").trim().equalsIgnoreCase(cD.getHSNCode().replace(" ","").trim()) && ProductName.replace(" ","").trim().equalsIgnoreCase(cD.getProduct_name().replace(" ","").trim())) {
												entryflag = true;
											}
										}

										if (company_Id.equals(company_id)
												&& vocherNo.replace(" ","").trim().equalsIgnoreCase(creditVocherNofromExcel.replace(" ","").trim())
												&& entryflag == true) {
											mainentryflag = true;
										}
									} else if (creditDetailsList.size() == 0) {
										mainentryflag = true;
									}

									if (mainentryflag == false) {
										flag = true;
										if(oldentry.getDescription()!=null && (oldentry.getDescription()==1||oldentry.getDescription()==4||oldentry.getDescription()==5||oldentry.getDescription()==6||oldentry.getDescription()==7))
										 {
											transaction_value = oldentry.getTransaction_value_head();
											round_off = oldentry.getRound_off();
											cgst = oldentry.getCGST_head();
											igst = oldentry.getIGST_head();
											sgst = oldentry.getSGST_head();
											state_comp_tax = oldentry.getSCT_head();
											vat = oldentry.getTotal_vat();
											vatcst = oldentry.getTotal_vatcst();
											excise = oldentry.getTotal_excise();
											tds = oldentry.getTds_amount();
											String ProductName = row.getCell(8).getStringCellValue();
											
											if (oldentry.getCustomer() != null) {
												customer1 = oldentry.getCustomer();
											}
											if (oldentry.getSubledger() != null) {
												subledger1 = oldentry.getSubledger();
											}

											for (Customer customer : list) {
												Boolean flag1 = false;
												if ((customer1 != null)
														&& (customer1.getCustomer_id().equals(customer.getCustomer_id()))) {
													try {
														for (Product product : customerService
																.findOneWithAll(customer.getCustomer_id())
																.getProduct()) {

															if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals("")) {
																String str = product.getHsn_san_no().trim();
																String strproductName = product.getProduct_name().trim();
																if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
																	 entity.setProduct_id(product);
																	 entity.setProduct_name(product.getProduct_name());
																     entity.setHSNCode(finalhsncode);
																	product1 = product;
																	entity.setIs_gst(product.getTax_type());
																	flag1 = true;
																	break;

																}
															}
															else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
															{
																
																String strproductName = product.getProduct_name().trim();
																if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
															    entity.setProduct_id(product);
															    entity.setProduct_name(product.getProduct_name());
																entity.setHSNCode(finalhsncode);
																flag1=true;
																product1=product;
																entity.setIs_gst(product.getTax_type());
																break;
															}
															}
														}

														if (flag1 == true) {
															break;
														}

													} catch (Exception e) {
														e.printStackTrace();
													}

												}

											}

											if (row.getCell(9) != null) {
												entity.setQuantity((double) row.getCell(9).getNumericCellValue());
											} else {
												entity.setQuantity((double) 0);
											}

											if (row.getCell(10) != null) {
												entity.setRate((double) row.getCell(10).getNumericCellValue());
											} else {
												entity.setRate((double) 0);
											}

											if (row.getCell(11) != null) {
												entity.setDiscount((double) row.getCell(11).getNumericCellValue());
											} else {
												entity.setDiscount((double) 0);
											}

											if (row.getCell(12) != null) {
												entity.setCgst((double) row.getCell(12).getNumericCellValue());
											} else {
												entity.setCgst((double) 0);
											}

											if (row.getCell(13) != null) {
												entity.setIgst((double) row.getCell(13).getNumericCellValue());

											} else {
												entity.setIgst((double) 0);
											}

											if (row.getCell(14) != null) {
												entity.setSgst((double) row.getCell(14).getNumericCellValue());

											} else {
												entity.setSgst((double) 0);
											}

											if (row.getCell(15) != null) {
												entity.setState_com_tax((double) row.getCell(15).getNumericCellValue());

											} else {
												entity.setState_com_tax((double) 0);
											}

											if (row.getCell(16) != null) {
												entity.setLabour_charges((double) row.getCell(16).getNumericCellValue());

											} else {
												entity.setLabour_charges((double) 0);
											}

											if (row.getCell(17) != null) {
												entity.setFrieght((double) row.getCell(17).getNumericCellValue());

											} else {
												entity.setFrieght((double) 0);
											}

											if (row.getCell(18) != null) {
												entity.setOthers((double) row.getCell(18).getNumericCellValue());

											} else {
												entity.setOthers((double) 0);
											}

											if (row.getCell(19) != null) {
												try {
													for (UnitOfMeasurement uom : uomList) {
														String str = uom.getUnit().trim();
														if (str.replace(" ","").trim().equalsIgnoreCase(
																row.getCell(19).getStringCellValue().replace(" ","").trim())) {
															entity.setUom_id(uom);
															break;
														}
													}
												} catch (Exception e) {
													e.printStackTrace();
												}
											}

											if (row.getCell(20) != null) {
												entity.setVAT((double) row.getCell(20).getNumericCellValue());

											} else {
												entity.setVAT((double) 0);
											}

											if (row.getCell(21) != null) {
												entity.setVATCST((double) row.getCell(21).getNumericCellValue());

											} else {
												entity.setVATCST((double) 0);
											}

											if (row.getCell(22) != null) {
												entity.setExcise((double) row.getCell(22).getNumericCellValue());

											} else {
												entity.setExcise((double) 0);
											}

											cgst=cgst+entity.getCgst();
											igst=igst+entity.getIgst();
											sgst=sgst+entity.getSgst();
											state_comp_tax= state_comp_tax+entity.getState_com_tax();
											vat=vat+entity.getVAT();
											vatcst=vatcst+entity.getVATCST();
											excise=excise+entity.getExcise();

											    Double tamount = (double) 0;
												Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers());
												Double damount = (entity.getRate()*entity.getQuantity());
												
												
												if(entity.getDiscount()!=0)
												{
													Double disamount =((round(damount,2))-(entity.getDiscount()));
													tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers();
												}
												else
												{
													tamount = amount;
												}
												
												transaction_value = transaction_value + round(tamount, 2);
												round_off = round_off + entity.getCgst() + entity.getIgst()
														+ entity.getSgst() + entity.getState_com_tax() + entity.getVAT()+entity.getVATCST()+entity.getExcise()+tamount;
										
												if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
												{
													if(customer1.getTds_rate()!=null)
													{
														NewTds = (transaction_value*customer1.getTds_rate())/100;
													}
												
												}
												round_off=round_off+tds-NewTds;

											if (product1 != null) {
												
												
												if (customer1!=null && oldentry.getRound_off()!=null) {
													customerService.addcustomertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, customer1.getCustomer_id(),(long) 5, (double)oldentry.getRound_off(), (double) 0, (long) 0);
													customerService.addcustomertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, customer1.getCustomer_id(),(long) 5, (double)round_off, (double) 0, (long) 1);
													
													if (tds!= 0) {
														SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", company_id);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)tds, (double) 0, (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
													}
													
													if (NewTds != 0) {
														SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", company_id);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)NewTds, (double) 0, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
													}
												}			
												if(subledger1 != null && oldentry.getTransaction_value_head()!=null ) {
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id,subledger1.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getTransaction_value_head(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id,subledger1.getSubledger_Id(), (long) 2, (double) 0, (double)transaction_value, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
												}		
												
												
													if (oldentry.getCGST_head()!=null && cgst > 0) {
														SubLedger subledgercgst = subledgerDao.findOne("Output CGST", company_id);
											
														if (subledgercgst != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getCGST_head(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)cgst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
											
													}
													else if((oldentry.getCGST_head()!=null)&&(cgst>0) && (oldentry.getCGST_head()==0))
													{
														SubLedger subledgercgst = subledgerDao.findOne("Output CGST", company_id);
														
														if (subledgercgst != null) {
															
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)cgst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													
													if (oldentry.getSGST_head()!=null && sgst > 0) {
														SubLedger subledgersgst = subledgerDao.findOne("Output SGST", company_id);
											
														if (subledgersgst != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getSGST_head(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sgst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getSGST_head()!=null)&&(sgst>0)&&(oldentry.getSGST_head()==0))
													{
														SubLedger subledgersgst = subledgerDao.findOne("Output SGST", company_id);
														
														if (subledgersgst != null) {
															
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sgst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
											
													if (oldentry.getIGST_head()!=null && igst > 0) {
														SubLedger subledgerigst = subledgerDao.findOne("Output IGST", company_id);
											
														if (subledgerigst != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getIGST_head(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)igst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													  else if((oldentry.getIGST_head()!=null)&&(igst>0)&&(oldentry.getIGST_head()==0))
														{
														  SubLedger subledgerigst = subledgerDao.findOne("Output IGST", company_id);
															
															if (subledgerigst != null) {
																
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)igst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															}
														
														}
													
											
													if (oldentry.getSCT_head()!=null && state_comp_tax > 0) {
														SubLedger subledgercess = subledgerDao.findOne("Output CESS", company_id);
											
														if (subledgercess != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getSCT_head(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)state_comp_tax, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getSCT_head()!=null) && (state_comp_tax>0) && (oldentry.getSCT_head()==0))
													{
														SubLedger subledgercess = subledgerDao.findOne("Output CESS", company_id);
														
														if (subledgercess != null) {
															
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)state_comp_tax, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													
													}
											
													if (oldentry.getTotal_vat()!=null && vat > 0) {
														SubLedger subledgervat = subledgerDao.findOne("Output VAT", company_id);
											
														if (subledgervat != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getTotal_vat(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)vat, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getTotal_vat()!=null) && (vat>0) && (oldentry.getTotal_vat()==0))
													{
														SubLedger subledgervat = subledgerDao.findOne("Output VAT", company_id);
														
														if (subledgervat != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)vat, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													
													if (oldentry.getTotal_vatcst()!=null && vatcst > 0) {
														SubLedger subledgercst = subledgerDao.findOne("Output CST", company_id);
											
														if (subledgercst != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getTotal_vatcst(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)vatcst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getTotal_vatcst()!=null) && (vatcst>0) && (oldentry.getTotal_vatcst()==0))
													{
														SubLedger subledgercst = subledgerDao.findOne("Output CST", company_id);
														
														if (subledgercst != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)vatcst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													if (oldentry.getTotal_excise()!=null && excise > 0) {
														SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", company_id);
											
														if (subledgerexcise != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getTotal_excise(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)excise, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getTotal_excise()!=null) && excise>0 && (oldentry.getTotal_excise()==0))
													{
														SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", company_id);
														
														if (subledgerexcise != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)excise, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													
													
													entity.setTransaction_amount(round(tamount, 2));
													oldentry.setRound_off(round(round_off, 2));
													oldentry.setTransaction_value_head(round(transaction_value,2));
													oldentry.setTds_amount(NewTds);
													oldentry.setCGST_head(round(cgst, 2));
													oldentry.setIGST_head(round(igst, 2));
													oldentry.setSGST_head(round(sgst, 2));
													oldentry.setSCT_head(round(state_comp_tax, 2));
													oldentry.setTotal_vat(round(vat, 2));
													oldentry.setTotal_vatcst(round(vatcst, 2));
													oldentry.setTotal_excise(round(excise, 2));
												
													creditNoteService.updateCreditNoteThroughExcel(oldentry);
													creditNoteService.updateCreditDetailsThroughExcel(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),entity,
															oldentry.getCredit_no_id(), company_id);
													
													
													CreditNote creditNote  = creditNoteService.getById(oldentry.getCredit_no_id());
													
													if(creditNote.getSales_bill_id() != null){
														SalesEntry salesEntry = salesEntryDao.findOneWithAll(creditNote.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
														double total=(double) 0;
														for (CreditNote note1 : salesEntry.getCreditNote()) {
																total += note1.getRound_off();				
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
													
													// stock
													 if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
													 {
													 Integer pflag=productService.checktype(company_id,product1.getProduct_id());
												     if(pflag==1)  
												     {
													      Stock stock = null;
													      stock=stockService.isstockexist(company_id,product1.getProduct_id());
													      if(stock!=null)
															{
													    	    double amount1 = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(),(double)entity.getQuantity());
													    	    stock.setAmount(stock.getAmount()-amount1);
																stock.setQuantity(stock.getQuantity()-entity.getQuantity());					
																stockService.updateStock(stock);
															}
															
												     }	
												   }

												if (repetProductafterunsuccesfullattempt == 0) {
													sucount = sucount + 1;
													m = 1;
													successVocharList.add(creditVocherNofromExcel);
												}
											} else {

												/*creditNoteService.changeStatusOfCreditNoteThroughExcel(oldentry);
												if (successVocharList != null && successVocharList.size() > 0) {
													sucount = sucount - 1;
													failcount = failcount + 1;
													m = 2;
													successVocharList.remove(creditVocherNofromExcel);
													failureVocharList.add(creditVocherNofromExcel);
												}*/

											}

										}
										/*break;*/
									}

									/*if (mainentryflag == true) {
										break;
									}*/
								}
							
							if (flag == false) {
								
								if(!failureVocharList.contains(creditVocherNofromExcel))
								{
								Boolean flag1 = false;
								String remoteAddr = "";
								remoteAddr = request.getHeader("X-FORWARDED-FOR");
								if (remoteAddr == null || "".equals(remoteAddr)) {
									remoteAddr = request.getRemoteAddr();
								}
								note.setIp_address(remoteAddr);
								note.setCreated_by(user_id);
								CreditNote oldnote = creditNoteService.isExcelVocherNumberExist(creditVocherNofromExcel, company_id);
									if(oldnote!=null && oldnote.getExcel_voucher_no()!=null)
									{
									String str = oldnote.getExcel_voucher_no().trim();
									if (str.replace(" ","").trim().equalsIgnoreCase(creditVocherNofromExcel.replace(" ","").trim())) {
										flag1 = true;
										/*break;*/
									}
									}
						

								if (flag1 == false) {

									String finalhsncode = "1";

									if (row.getCell(7) != null) {
										Double hsncode = row.getCell(7).getNumericCellValue();
										Integer hsncode1 = hsncode.intValue();
										finalhsncode = hsncode1.toString().trim();
									}

									try {
										for (Customer customer : list) {

											String str = customer.getFirm_name().trim();
											if (str.replace(" ","").trim().equalsIgnoreCase(row.getCell(0).getStringCellValue().replace(" ","").trim())) {
												note.setCustomer(customer);
												customer1 = customer;
												count = count + 1;
												break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

									try {
										for (SubLedger sub : subLedgerList) {

											String str = sub.getSubledger_name().trim();
											if (str.replace(" ","").trim().equalsIgnoreCase(row.getCell(23).getStringCellValue().replace(" ","").trim())) {
												note.setSubledger(sub);
												subledger1 = sub;
												count = count + 1;
												break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									note.setCompany(user.getCompany());

									try {
										for (SalesEntry salesEntry : saleslist) {
											String str = salesEntry.getVoucher_no().trim();
											if (str.replace(" ","").trim().equalsIgnoreCase(salesVocherNofromExcel.replace(" ","").trim())) {
												note.setSales_bill_id(salesEntry);
												count = count + 1;
												salesEntry1 = salesEntry;
												break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									note.setLocal_time(new LocalTime());
									note.setExcel_voucher_no(creditVocherNofromExcel);
									
									if (row.getCell(1) != null) {
										 try {
											 note.setDate(new LocalDate(ClassToConvertDateForExcell
														.dateConvert(row.getCell(1).getDateCellValue().toString())));
										   }
										   catch (Exception e) {
										    e.printStackTrace();
										    }
										
									}
									
									try {
										for (AccountingYear year_range : yearlist) {

											String str = year_range.getYear_range().trim();
											if (str.replace(" ","").trim().equalsIgnoreCase(row.getCell(5).getStringCellValue().trim().replace(" ","").trim())) {
												note.setAccounting_year_id(year_range);
												count = count + 1;
												year_range1 = year_range;
												if(note.getDate()!=null)
												{
												note.setVoucher_no(commonService.getVoucherNumber("CN", VOUCHER_CREDIT_NOTE,
														note.getDate(), year_range.getYear_id(), user.getCompany().getCompany_id()));
												}
												break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}


									if (row.getCell(3) != null) {
										if (row.getCell(3).getStringCellValue().equalsIgnoreCase("Sales return")) {
											note.setDescription(1);
										} else if (row.getCell(3).getStringCellValue()
												.equalsIgnoreCase("Post sale discount")) {
											note.setDescription(2);
										} else if (row.getCell(3).getStringCellValue()
												.equalsIgnoreCase("Deficiency in services")) {
											note.setDescription(3);
										} else if (row.getCell(3).getStringCellValue()
												.equalsIgnoreCase("Correction in invoices")) {
											note.setDescription(4);
										} else if (row.getCell(3).getStringCellValue()
												.equalsIgnoreCase("Change in POS")) {
											note.setDescription(5);
										} else if (row.getCell(3).getStringCellValue()
												.equalsIgnoreCase("Finalization of provisional assessment")) {
											note.setDescription(6);
										} else if (row.getCell(3).getStringCellValue().equalsIgnoreCase("Other")) {
											note.setDescription(7);
										}
									}

									if(note.getDescription()!=null && (note.getDescription()==1||note.getDescription()==4||note.getDescription()==5||note.getDescription()==6||note.getDescription()==7))
									{

										String ProductName = row.getCell(8).getStringCellValue();
										
										for (Customer customer : list) {
											Boolean flag2 = false;
											if ((customer1 != null)
													&& (customer1.getCustomer_id().equals(customer.getCustomer_id()))) {
												try {
													for (Product product : customerService.findOneWithAll(customer.getCustomer_id()).getProduct()) {

														if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals("")){
															String str = product.getHsn_san_no().trim();
															String strProductName = product.getProduct_name().trim();
															if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strProductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
																 entity.setProduct_id(product);
																 entity.setProduct_name(product.getProduct_name());
																 entity.setHSNCode(finalhsncode);
																product1 = product;
																entity.setIs_gst(product.getTax_type());
																count = count+1;
																flag2 = true;
																break;

															}
														}
														else if(product.getHsn_san_no()!=null && product.getHsn_san_no().equals(""))
														{
															
															String strproductName = product.getProduct_name().trim();
															if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
														    entity.setProduct_id(product);
														    entity.setProduct_name(product.getProduct_name());
															entity.setHSNCode(finalhsncode);
															flag2=true;
															product1=product;
															entity.setIs_gst(product.getTax_type());
															count = count+1;
															break;
														}
														}
													}

													if (flag2 == true) {
														break;
													}

												} catch (Exception e) {
													e.printStackTrace();
												}

											}

										}

										if (row.getCell(9) != null) {
											entity.setQuantity((double) row.getCell(9).getNumericCellValue());
										} else {
											entity.setQuantity((double) 0);
										}

										if (row.getCell(10) != null) {
											entity.setRate((double) row.getCell(10).getNumericCellValue());
										} else {
											entity.setRate((double) 0);
										}

										if (row.getCell(11) != null) {
											entity.setDiscount((double) row.getCell(11).getNumericCellValue());
										} else {
											entity.setDiscount((double) 0);
										}

										if (row.getCell(12) != null) {
											entity.setCgst((double) row.getCell(12).getNumericCellValue());
										} else {
											entity.setCgst((double) 0);
										}

										if (row.getCell(13) != null) {
											entity.setIgst((double) row.getCell(13).getNumericCellValue());

										} else {
											entity.setIgst((double) 0);
										}

										if (row.getCell(14) != null) {
											entity.setSgst((double) row.getCell(14).getNumericCellValue());

										} else {
											entity.setSgst((double) 0);
										}

										if (row.getCell(15) != null) {
											entity.setState_com_tax((double) row.getCell(15).getNumericCellValue());

										} else {
											entity.setState_com_tax((double) 0);
										}

										if (row.getCell(16) != null) {
											entity.setLabour_charges((double) row.getCell(16).getNumericCellValue());

										} else {
											entity.setLabour_charges((double) 0);
										}

										if (row.getCell(17) != null) {
											entity.setFrieght((double) row.getCell(17).getNumericCellValue());

										} else {
											entity.setFrieght((double) 0);
										}

										if (row.getCell(18) != null) {
											entity.setOthers((double) row.getCell(18).getNumericCellValue());

										} else {
											entity.setOthers((double) 0);
										}

										if (row.getCell(19) != null) {
											try {
												for (UnitOfMeasurement uom : uomList) {
													String str = uom.getUnit().trim();
													if (str.replace(" ","").trim().equalsIgnoreCase(row.getCell(19).getStringCellValue().replace(" ","").trim())) {
														entity.setUom_id(uom);
														break;
													}
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

										if (row.getCell(20) != null) {
											entity.setVAT((double) row.getCell(20).getNumericCellValue());

										} else {
											entity.setVAT((double) 0);
										}

										if (row.getCell(21) != null) {
											entity.setVATCST((double) row.getCell(21).getNumericCellValue());

										} else {
											entity.setVATCST((double) 0);
										}

										if (row.getCell(22) != null) {
											entity.setExcise((double) row.getCell(22).getNumericCellValue());

										} else {
											entity.setExcise((double) 0);
										}
										cgst=entity.getCgst();
										igst=entity.getIgst();
										sgst=entity.getSgst();
										state_comp_tax= entity.getState_com_tax();
									    vat=entity.getVAT();
										vatcst=entity.getVATCST();
										excise=entity.getExcise();

										    Double tamount = (double) 0;
										    Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers());
											Double damount = (entity.getRate()*entity.getQuantity());
											
											if(entity.getDiscount()!=0)
											{
												Double disamount =((round(damount,2))-(entity.getDiscount()));
												tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers();
											}
											else
											{
												tamount = amount;
											}

											transaction_value = round(tamount, 2);
											
											
											if(customer1!=null && customer1.getTds_applicable()!=null)
											{
												if(customer1.getTds_applicable().equals(1) && customer1.getTds_rate()!=null)
												{
													tds = (transaction_value*customer1.getTds_rate())/100;
												}
											}
											round_off = cgst + igst + sgst + state_comp_tax + vat+vatcst+excise+ transaction_value-tds;
                                           

										entity.setTransaction_amount(round(tamount, 2));
										note.setRound_off(round(round_off, 2));
										note.setTds_amount(tds);
										note.setTransaction_value_head(round(transaction_value, 2));
										note.setCGST_head(round(cgst, 2));
										note.setIGST_head(round(igst, 2));
										note.setSGST_head(round(sgst, 2));
										note.setSCT_head(round(state_comp_tax, 2));
										note.setTotal_vat(round(vat, 2));
										note.setTotal_vatcst(round(vatcst, 2));
										note.setTotal_excise(round(excise, 2));

									} else {
										note.setRound_off(round((double) row.getCell(6).getNumericCellValue(), 2));
									}

									if ((product1 != null && customer1!=null && note.getVoucher_no()!=null) && (note.getDescription()!=null && (note.getDescription()==1||note.getDescription()==4||note.getDescription()==5||note.getDescription()==6||note.getDescription()==7))) 
									{

										if (count == 5)
										{

											note.setFlag(true);
											sucount = sucount + 1;
											successVocharList.add(creditVocherNofromExcel);
											repetProductafterunsuccesfullattempt = repetProductafterunsuccesfullattempt + 1;

											note.setEntry_status(MyAbstractController.ENTRY_PENDING);
											Long id = creditNoteService.saveCreditNoteThroughExcel(note);
											creditNoteService.updateCreditDetailsThroughExcel(note.getAccounting_year_id().getYear_id(),note.getDate(),entity, id, company_id);

											CreditNote creditNote  = creditNoteService.getById(id);
											
											if(creditNote.getSales_bill_id() != null){
												SalesEntry salesEntry = salesEntryDao.findOneWithAll(creditNote.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
												double total=(double) 0;
												for (CreditNote note1 : salesEntry.getCreditNote()) {
														total += note1.getRound_off();				
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
											// stock
											 if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
											 {
											 Integer pflag=productService.checktype(company_id,product1.getProduct_id());
										     if(pflag==1)  
										     {
											      Stock stock = null;
											      stock=stockService.isstockexist(company_id,product1.getProduct_id());
											      if(stock!=null)
													{
											    	    double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(),(double)entity.getQuantity());
											    	    stock.setAmount(stock.getAmount()-amount);
														stock.setQuantity(stock.getQuantity()-entity.getQuantity());					
														stockService.updateStock(stock);
													}
													
										     }	
										   }
											
											if (customer1!=null) {
												customerService.addcustomertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, customer1.getCustomer_id(),(long) 5, (double)round_off, (double) 0, (long) 1);
											}			
											if(subledger1 != null) {
												subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id,subledger1.getSubledger_Id(), (long) 2, (double) 0, (double)transaction_value, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
											}		
											if (note.getTds_amount() != null && note.getTds_amount() != 0) {
												SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", company_id);
												subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)note.getTds_amount(), (double) 0, (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
											}
											
												if (cgst > 0) {
													SubLedger subledgercgst = subledgerDao.findOne("Output CGST", company_id);
										
													if (subledgercgst != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)cgst, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
										
												}
												if (sgst > 0) {
													SubLedger subledgersgst = subledgerDao.findOne("Output SGST", company_id);
										
													if (subledgersgst != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sgst, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
										
												if (igst > 0) {
													SubLedger subledgerigst = subledgerDao.findOne("Output IGST", company_id);
										
													if (subledgerigst != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)igst, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
										
												if (state_comp_tax > 0) {
													SubLedger subledgercess = subledgerDao.findOne("Output CESS", company_id);
										
													if (subledgercess != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)state_comp_tax, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
										
												if (vat > 0) {
													SubLedger subledgervat = subledgerDao.findOne("Output VAT", company_id);
										
													if (subledgervat != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)vat, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
												if (vatcst > 0) {
													SubLedger subledgercst = subledgerDao.findOne("Output CST", company_id);
										
													if (subledgercst != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)vatcst, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
												if ( excise > 0) {
													SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", company_id);
										
													if (subledgerexcise != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)excise, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
										} 
										else {
											
											if(!successVocharList.contains(creditVocherNofromExcel))
											{
												maincount = maincount + 1;
												failcount = failcount + 1;
												failureVocharList.add(creditVocherNofromExcel);
												repetProductafterunsuccesfullattempt = repetProductafterunsuccesfullattempt + 1;
											}
										}

									}
									else if ((product1 == null && note.getVoucher_no()!=null)&&(note.getDescription()!=null && (note.getDescription()==2||note.getDescription()==3))) 
									{
										if (count == 4) {

											note.setFlag(true);
											sucount = sucount + 1;
											successVocharList.add(creditVocherNofromExcel);
											repetProductafterunsuccesfullattempt = repetProductafterunsuccesfullattempt + 1;

											note.setEntry_status(MyAbstractController.ENTRY_PENDING);
											Long id = creditNoteService.saveCreditNoteThroughExcel(note);
											CreditNote creditNote  = creditNoteService.getById(id);
											
											if(creditNote.getSales_bill_id() != null){
												SalesEntry salesEntry = salesEntryDao.findOneWithAll(creditNote.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
												double total=(double) 0;
												for (CreditNote note1 : salesEntry.getCreditNote()) {
														total += note1.getRound_off();				
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
											
											if (customer1!=null) {
												customerService.addcustomertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, customer1.getCustomer_id(),(long) 5, (double)round_off, (double) 0, (long) 1);
											}			
											if(subledger1 != null) {
												subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id,subledger1.getSubledger_Id(), (long) 2, (double) 0, (double)round_off, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
											}
										} 
										else {
											
											if(!successVocharList.contains(creditVocherNofromExcel))
											{
												maincount = maincount + 1;
												failcount = failcount + 1;
												failureVocharList.add(creditVocherNofromExcel);
												repetProductafterunsuccesfullattempt = repetProductafterunsuccesfullattempt + 1;
											}
										}
											
									}
									else {
										
										if(!successVocharList.contains(creditVocherNofromExcel))
										{
											maincount = maincount + 1;
											failcount = failcount + 1;
											failureVocharList.add(creditVocherNofromExcel);
											repetProductafterunsuccesfullattempt = repetProductafterunsuccesfullattempt + 1;
										}
										
									}

									if (maincount == 0) {
										m = 1;
									} else {
										m = 2;
									}
								}
							   }
							}
						}
					}
					workbook.close();
				}
			} else if (excelfile.getOriginalFilename().endsWith("xlsx")) {
				int i = 0;
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
				if (isValid) {
					i = 1;
					while (i <= worksheet.getLastRowNum()) {
						CreditNote note = new CreditNote();
						CreditDetails entity = new CreditDetails();
						Customer customer1 = null;
						SubLedger subledger1 = null;
						Bank bank1 = null;
						SalesEntry salesEntry1 = null;
						AccountingYear year_range1 = null;
						Product product1 = null;
						
						XSSFRow row = worksheet.getRow(i++);

						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
						    double transaction_value = (double) 0;
						    double cgst = (double) 0;
						    double igst = (double) 0;
						    double sgst = (double) 0;
						    double state_comp_tax = (double) 0;
						    double round_off = (double) 0;
						    double vat = (double) 0;
						    double vatcst = (double) 0;
						    double excise = (double) 0;
						    double tds = (double) 0;
						    double NewTds = (double)0;
							int count = 0;
							
							double VAT = (double) 0;
							double VATCST = (double) 0;
							double Excise = (double) 0;
							
							boolean flag = false;

							String creditVocherNofromExcel = "";
							String salesVocherNofromExcel = "";

							try {
								creditVocherNofromExcel = row.getCell(2).getStringCellValue().trim().replaceAll(" ", "");;
							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								Double voucherNofromExcel = row.getCell(2).getNumericCellValue();
								Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
								creditVocherNofromExcel = voucherNofromExcel1.toString().trim();

							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								salesVocherNofromExcel = row.getCell(4).getStringCellValue().trim();
							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								Double shipingBillNfromExcel = row.getCell(4).getNumericCellValue();
								Integer shipingBillNfromExcel1 = shipingBillNfromExcel.intValue();
								salesVocherNofromExcel = shipingBillNfromExcel1.toString().trim();

							} catch (Exception e) {
								e.printStackTrace();
							}

							CreditNote oldentry = creditNoteService.isExcelVocherNumberExist(creditVocherNofromExcel, company_id);
					    		if (oldentry!=null) 
					    		{
									Set<CreditDetails> creditDetailsList = oldentry.getCreditDetails();
									Boolean mainentryflag = false;
									String finalhsncode = "1";
									
									if (creditDetailsList != null && creditDetailsList.size() != 0) {
										String vocherNo = oldentry.getExcel_voucher_no().trim();
										Long company_Id = oldentry.getCompany().getCompany_id();
										String ProductName = row.getCell(8).getStringCellValue();
										
										if (row.getCell(7) != null) {
											Double hsncode = row.getCell(7).getNumericCellValue();
											Integer hsncode1 = hsncode.intValue();
											finalhsncode = hsncode1.toString().trim();
										}
										
										Boolean entryflag = false;

										for (CreditDetails cD : creditDetailsList) {
											if (finalhsncode.replace(" ","").trim().equalsIgnoreCase(cD.getHSNCode().replace(" ","").trim()) && ProductName.replace(" ","").trim().equalsIgnoreCase(cD.getProduct_name().replace(" ","").trim())) {
												entryflag = true;
											}
										}

										if (company_Id.equals(company_id)
												&& vocherNo.replace(" ","").trim().equalsIgnoreCase(creditVocherNofromExcel.replace(" ","").trim())
												&& entryflag == true) {
											mainentryflag = true;
										}
									} else if (creditDetailsList.size() == 0) {
										mainentryflag = true;
									}

									if (mainentryflag == false) {
										flag = true;
										if(oldentry.getDescription()!=null && (oldentry.getDescription()==1||oldentry.getDescription()==4||oldentry.getDescription()==5||oldentry.getDescription()==6||oldentry.getDescription()==7))
										 {
											transaction_value = oldentry.getTransaction_value_head();
											round_off = oldentry.getRound_off();
											cgst = oldentry.getCGST_head();
											igst = oldentry.getIGST_head();
											sgst = oldentry.getSGST_head();
											state_comp_tax = oldentry.getSCT_head();
											vat = oldentry.getTotal_vat();
											vatcst = oldentry.getTotal_vatcst();
											excise = oldentry.getTotal_excise();
											tds = oldentry.getTds_amount();
											String ProductName = row.getCell(8).getStringCellValue();
											
											if (oldentry.getCustomer() != null) {
												customer1 = oldentry.getCustomer();
											}
											if (oldentry.getSubledger() != null) {
												subledger1 = oldentry.getSubledger();
											}

											for (Customer customer : list) {
												Boolean flag1 = false;
												if ((customer1 != null)
														&& (customer1.getCustomer_id().equals(customer.getCustomer_id()))) {
													try {
														for (Product product : customerService
																.findOneWithAll(customer.getCustomer_id())
																.getProduct()) {

															if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals("")) {
																String str = product.getHsn_san_no().trim();
																String strproductName = product.getProduct_name().trim();
																if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
																	 entity.setProduct_id(product);
																	 entity.setProduct_name(product.getProduct_name());
																     entity.setHSNCode(finalhsncode);
																	product1 = product;
																	entity.setIs_gst(product.getTax_type());
																	flag1 = true;
																	break;

																}
															}
															else if(product.getHsn_san_no()==null || product.getHsn_san_no().equals(""))
															{
																
																String strproductName = product.getProduct_name().trim();
																if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
															    entity.setProduct_id(product);
															    entity.setProduct_name(product.getProduct_name());
																entity.setHSNCode(finalhsncode);
																flag1=true;
																product1=product;
																entity.setIs_gst(product.getTax_type());
																break;
															}
															}
														}

														if (flag1 == true) {
															break;
														}

													} catch (Exception e) {
														e.printStackTrace();
													}

												}

											}

											if (row.getCell(9) != null) {
												entity.setQuantity((double) row.getCell(9).getNumericCellValue());
											} else {
												entity.setQuantity((double) 0);
											}

											if (row.getCell(10) != null) {
												entity.setRate((double) row.getCell(10).getNumericCellValue());
											} else {
												entity.setRate((double) 0);
											}

											if (row.getCell(11) != null) {
												entity.setDiscount((double) row.getCell(11).getNumericCellValue());
											} else {
												entity.setDiscount((double) 0);
											}

											if (row.getCell(12) != null) {
												entity.setCgst((double) row.getCell(12).getNumericCellValue());
											} else {
												entity.setCgst((double) 0);
											}

											if (row.getCell(13) != null) {
												entity.setIgst((double) row.getCell(13).getNumericCellValue());

											} else {
												entity.setIgst((double) 0);
											}

											if (row.getCell(14) != null) {
												entity.setSgst((double) row.getCell(14).getNumericCellValue());

											} else {
												entity.setSgst((double) 0);
											}

											if (row.getCell(15) != null) {
												entity.setState_com_tax((double) row.getCell(15).getNumericCellValue());

											} else {
												entity.setState_com_tax((double) 0);
											}

											if (row.getCell(16) != null) {
												entity.setLabour_charges((double) row.getCell(16).getNumericCellValue());

											} else {
												entity.setLabour_charges((double) 0);
											}

											if (row.getCell(17) != null) {
												entity.setFrieght((double) row.getCell(17).getNumericCellValue());

											} else {
												entity.setFrieght((double) 0);
											}

											if (row.getCell(18) != null) {
												entity.setOthers((double) row.getCell(18).getNumericCellValue());

											} else {
												entity.setOthers((double) 0);
											}

											if (row.getCell(19) != null) {
												try {
													for (UnitOfMeasurement uom : uomList) {
														String str = uom.getUnit().trim();
														if (str.replace(" ","").trim().equalsIgnoreCase(
																row.getCell(19).getStringCellValue().replace(" ","").trim())) {
															entity.setUom_id(uom);
															break;
														}
													}
												} catch (Exception e) {
													e.printStackTrace();
												}
											}

											if (row.getCell(20) != null) {
												entity.setVAT((double) row.getCell(20).getNumericCellValue());

											} else {
												entity.setVAT((double) 0);
											}

											if (row.getCell(21) != null) {
												entity.setVATCST((double) row.getCell(21).getNumericCellValue());

											} else {
												entity.setVATCST((double) 0);
											}

											if (row.getCell(22) != null) {
												entity.setExcise((double) row.getCell(22).getNumericCellValue());

											} else {
												entity.setExcise((double) 0);
											}

											cgst=cgst+entity.getCgst();
											igst=igst+entity.getIgst();
											sgst=sgst+entity.getSgst();
											state_comp_tax= state_comp_tax+entity.getState_com_tax();
											vat=vat+entity.getVAT();
											vatcst=vatcst+entity.getVATCST();
											excise=excise+entity.getExcise();

											    Double tamount = (double) 0;
												Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers());
												Double damount = (entity.getRate()*entity.getQuantity());
												
												
												if(entity.getDiscount()!=0)
												{
													Double disamount =((round(damount,2))-(entity.getDiscount()));
													tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers();
												}
												else
												{
													tamount = amount;
												}
												
												transaction_value = transaction_value + round(tamount, 2);
												round_off = round_off + entity.getCgst() + entity.getIgst()
														+ entity.getSgst() + entity.getState_com_tax() + entity.getVAT()+entity.getVATCST()+entity.getExcise()+tamount;
										
												if(customer1!=null && (customer1.getTds_applicable()!=null && customer1.getTds_applicable()==1))
												{
													if(customer1.getTds_rate()!=null)
													{
														NewTds = (transaction_value*customer1.getTds_rate())/100;
													}
												
												}
												round_off=round_off+tds-NewTds;

											if (product1 != null) {
												
												
												if (customer1!=null && oldentry.getRound_off()!=null) {
													customerService.addcustomertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, customer1.getCustomer_id(),(long) 5, (double)oldentry.getRound_off(), (double) 0, (long) 0);
													customerService.addcustomertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, customer1.getCustomer_id(),(long) 5, (double)round_off, (double) 0, (long) 1);
													
													if (tds!= 0) {
														SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", company_id);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)tds, (double) 0, (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
													}
													
													if (NewTds != 0) {
														SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", company_id);
														subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)NewTds, (double) 0, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
													}
												}			
												if(subledger1 != null && oldentry.getTransaction_value_head()!=null ) {
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id,subledger1.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getTransaction_value_head(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
													subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id,subledger1.getSubledger_Id(), (long) 2, (double) 0, (double)transaction_value, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
												}		
												
												
													if (oldentry.getCGST_head()!=null && cgst > 0) {
														SubLedger subledgercgst = subledgerDao.findOne("Output CGST", company_id);
											
														if (subledgercgst != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getCGST_head(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)cgst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
											
													}
													else if((oldentry.getCGST_head()!=null)&&(cgst>0) && (oldentry.getCGST_head()==0))
													{
														SubLedger subledgercgst = subledgerDao.findOne("Output CGST", company_id);
														
														if (subledgercgst != null) {
															
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)cgst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													
													if (oldentry.getSGST_head()!=null && sgst > 0) {
														SubLedger subledgersgst = subledgerDao.findOne("Output SGST", company_id);
											
														if (subledgersgst != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getSGST_head(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sgst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getSGST_head()!=null)&&(sgst>0)&&(oldentry.getSGST_head()==0))
													{
														SubLedger subledgersgst = subledgerDao.findOne("Output SGST", company_id);
														
														if (subledgersgst != null) {
															
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sgst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
											
													if (oldentry.getIGST_head()!=null && igst > 0) {
														SubLedger subledgerigst = subledgerDao.findOne("Output IGST", company_id);
											
														if (subledgerigst != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getIGST_head(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)igst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													  else if((oldentry.getIGST_head()!=null)&&(igst>0)&&(oldentry.getIGST_head()==0))
														{
														  SubLedger subledgerigst = subledgerDao.findOne("Output IGST", company_id);
															
															if (subledgerigst != null) {
																
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)igst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															}
														
														}
													
											
													if (oldentry.getSCT_head()!=null && state_comp_tax > 0) {
														SubLedger subledgercess = subledgerDao.findOne("Output CESS", company_id);
											
														if (subledgercess != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getSCT_head(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)state_comp_tax, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getSCT_head()!=null) && (state_comp_tax>0) && (oldentry.getSCT_head()==0))
													{
														SubLedger subledgercess = subledgerDao.findOne("Output CESS", company_id);
														
														if (subledgercess != null) {
															
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)state_comp_tax, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													
													}
											
													if (oldentry.getTotal_vat()!=null && vat > 0) {
														SubLedger subledgervat = subledgerDao.findOne("Output VAT", company_id);
											
														if (subledgervat != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getTotal_vat(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)vat, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getTotal_vat()!=null) && (vat>0) && (oldentry.getTotal_vat()==0))
													{
														SubLedger subledgervat = subledgerDao.findOne("Output VAT", company_id);
														
														if (subledgervat != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)vat, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													
													if (oldentry.getTotal_vatcst()!=null && vatcst > 0) {
														SubLedger subledgercst = subledgerDao.findOne("Output CST", company_id);
											
														if (subledgercst != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getTotal_vatcst(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)vatcst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getTotal_vatcst()!=null) && (vatcst>0) && (oldentry.getTotal_vatcst()==0))
													{
														SubLedger subledgercst = subledgerDao.findOne("Output CST", company_id);
														
														if (subledgercst != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)vatcst, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													if (oldentry.getTotal_excise()!=null && excise > 0) {
														SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", company_id);
											
														if (subledgerexcise != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)oldentry.getTotal_excise(), (long) 0,null,null,null,null,oldentry,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)excise, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getTotal_excise()!=null) && excise>0 && (oldentry.getTotal_excise()==0))
													{
														SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", company_id);
														
														if (subledgerexcise != null) {
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),company_id, subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)excise, (long) 1,null,null,null,null,oldentry,null,null,null,null,null,null,null);
														}
													}
													
													
													entity.setTransaction_amount(round(tamount, 2));
													oldentry.setRound_off(round(round_off, 2));
													oldentry.setTransaction_value_head(round(transaction_value,2));
													oldentry.setTds_amount(NewTds);
													oldentry.setCGST_head(round(cgst, 2));
													oldentry.setIGST_head(round(igst, 2));
													oldentry.setSGST_head(round(sgst, 2));
													oldentry.setSCT_head(round(state_comp_tax, 2));
													oldentry.setTotal_vat(round(vat, 2));
													oldentry.setTotal_vatcst(round(vatcst, 2));
													oldentry.setTotal_excise(round(excise, 2));
												
													creditNoteService.updateCreditNoteThroughExcel(oldentry);
													creditNoteService.updateCreditDetailsThroughExcel(oldentry.getAccounting_year_id().getYear_id(),oldentry.getDate(),entity,
															oldentry.getCredit_no_id(), company_id);
													
													
													CreditNote creditNote  = creditNoteService.getById(oldentry.getCredit_no_id());
													
													if(creditNote.getSales_bill_id() != null){
														SalesEntry salesEntry = salesEntryDao.findOneWithAll(creditNote.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
														double total=(double) 0;
														for (CreditNote note1 : salesEntry.getCreditNote()) {
																total += note1.getRound_off();				
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
													
													// stock
													 if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
													 {
													 Integer pflag=productService.checktype(company_id,product1.getProduct_id());
												     if(pflag==1)  
												     {
													      Stock stock = null;
													      stock=stockService.isstockexist(company_id,product1.getProduct_id());
													      if(stock!=null)
															{
													    	    double amount1 = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(),(double)entity.getQuantity());
													    	    stock.setAmount(stock.getAmount()-amount1);
																stock.setQuantity(stock.getQuantity()-entity.getQuantity());					
																stockService.updateStock(stock);
															}
															
												     }	
												   }

												if (repetProductafterunsuccesfullattempt == 0) {
													sucount = sucount + 1;
													m = 1;
													successVocharList.add(creditVocherNofromExcel);
												}
											} else {

												/*creditNoteService.changeStatusOfCreditNoteThroughExcel(oldentry);
												if (successVocharList != null && successVocharList.size() > 0) {
													sucount = sucount - 1;
													failcount = failcount + 1;
													m = 2;
													successVocharList.remove(creditVocherNofromExcel);
													failureVocharList.add(creditVocherNofromExcel);
												}*/

											}

										}
										/*break;*/
									}

									/*if (mainentryflag == true) {
										break;
									}*/
								}
							
							if (flag == false) {
								
								if(!failureVocharList.contains(creditVocherNofromExcel))
								{
								Boolean flag1 = false;
								String remoteAddr = "";
								remoteAddr = request.getHeader("X-FORWARDED-FOR");
								if (remoteAddr == null || "".equals(remoteAddr)) {
									remoteAddr = request.getRemoteAddr();
								}
								note.setIp_address(remoteAddr);
								note.setCreated_by(user_id);
								CreditNote oldnote = creditNoteService.isExcelVocherNumberExist(creditVocherNofromExcel, company_id);
									if(oldnote!=null && oldnote.getExcel_voucher_no()!=null)
									{
									String str = oldnote.getExcel_voucher_no().trim();
									if (str.replace(" ","").trim().equalsIgnoreCase(creditVocherNofromExcel.replace(" ","").trim())) {
										flag1 = true;
										/*break;*/
									}
									}
						

								if (flag1 == false) {

									String finalhsncode = "1";

									if (row.getCell(7) != null) {
										Double hsncode = row.getCell(7).getNumericCellValue();
										Integer hsncode1 = hsncode.intValue();
										finalhsncode = hsncode1.toString().trim();
									}

									try {
										for (Customer customer : list) {

											String str = customer.getFirm_name().trim();
											if (str.replace(" ","").trim().equalsIgnoreCase(row.getCell(0).getStringCellValue().replace(" ","").trim())) {
												note.setCustomer(customer);
												customer1 = customer;
												count = count + 1;
												break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

									try {
										for (SubLedger sub : subLedgerList) {

											String str = sub.getSubledger_name().trim();
											if (str.replace(" ","").trim().equalsIgnoreCase(row.getCell(23).getStringCellValue().replace(" ","").trim())) {
												note.setSubledger(sub);
												subledger1 = sub;
												count = count + 1;
												break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									note.setCompany(user.getCompany());

									try {
										for (SalesEntry salesEntry : saleslist) {
											String str = salesEntry.getVoucher_no().trim();
											if (str.replace(" ","").trim().equalsIgnoreCase(salesVocherNofromExcel.replace(" ","").trim())) {
												note.setSales_bill_id(salesEntry);
												count = count + 1;
												salesEntry1 = salesEntry;
												break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									note.setLocal_time(new LocalTime());
									note.setExcel_voucher_no(creditVocherNofromExcel);
									
									if (row.getCell(1) != null) {
										 try {
											 note.setDate(new LocalDate(ClassToConvertDateForExcell
														.dateConvert(row.getCell(1).getDateCellValue().toString())));
										   }
										   catch (Exception e) {
										    e.printStackTrace();
										    }
										
									}
									
									try {
										for (AccountingYear year_range : yearlist) {

											String str = year_range.getYear_range().trim();
											if (str.replace(" ","").trim().equalsIgnoreCase(row.getCell(5).getStringCellValue().trim().replace(" ","").trim())) {
												note.setAccounting_year_id(year_range);
												count = count + 1;
												year_range1 = year_range;
												if(note.getDate()!=null)
												{
												note.setVoucher_no(commonService.getVoucherNumber("CN", VOUCHER_CREDIT_NOTE,
														note.getDate(), year_range.getYear_id(), user.getCompany().getCompany_id()));
												}
												break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}


									if (row.getCell(3) != null) {
										if (row.getCell(3).getStringCellValue().equalsIgnoreCase("Sales return")) {
											note.setDescription(1);
										} else if (row.getCell(3).getStringCellValue()
												.equalsIgnoreCase("Post sale discount")) {
											note.setDescription(2);
										} else if (row.getCell(3).getStringCellValue()
												.equalsIgnoreCase("Deficiency in services")) {
											note.setDescription(3);
										} else if (row.getCell(3).getStringCellValue()
												.equalsIgnoreCase("Correction in invoices")) {
											note.setDescription(4);
										} else if (row.getCell(3).getStringCellValue()
												.equalsIgnoreCase("Change in POS")) {
											note.setDescription(5);
										} else if (row.getCell(3).getStringCellValue()
												.equalsIgnoreCase("Finalization of provisional assessment")) {
											note.setDescription(6);
										} else if (row.getCell(3).getStringCellValue().equalsIgnoreCase("Other")) {
											note.setDescription(7);
										}
									}

									if(note.getDescription()!=null && (note.getDescription()==1||note.getDescription()==4||note.getDescription()==5||note.getDescription()==6||note.getDescription()==7))
									{

										String ProductName = row.getCell(8).getStringCellValue();
										
										for (Customer customer : list) {
											Boolean flag2 = false;
											if ((customer1 != null)
													&& (customer1.getCustomer_id().equals(customer.getCustomer_id()))) {
												try {
													for (Product product : customerService.findOneWithAll(customer.getCustomer_id()).getProduct()) {

														if(product.getHsn_san_no()!=null && !product.getHsn_san_no().equals("")){
															String str = product.getHsn_san_no().trim();
															String strProductName = product.getProduct_name().trim();
															if (str.replace(" ","").trim().equalsIgnoreCase(finalhsncode.replace(" ","").trim()) && strProductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
																 entity.setProduct_id(product);
																 entity.setProduct_name(product.getProduct_name());
																 entity.setHSNCode(finalhsncode);
																product1 = product;
																entity.setIs_gst(product.getTax_type());
																count = count+1;
																flag2 = true;
																break;

															}
														}
														else if(product.getHsn_san_no()!=null && product.getHsn_san_no().equals(""))
														{
															
															String strproductName = product.getProduct_name().trim();
															if (strproductName.replace(" ","").trim().equalsIgnoreCase(ProductName.replace(" ","").trim())) {
														    entity.setProduct_id(product);
														    entity.setProduct_name(product.getProduct_name());
															entity.setHSNCode(finalhsncode);
															flag2=true;
															product1=product;
															entity.setIs_gst(product.getTax_type());
															count = count+1;
															break;
														}
														}
													}

													if (flag2 == true) {
														break;
													}

												} catch (Exception e) {
													e.printStackTrace();
												}

											}

										}

										if (row.getCell(9) != null) {
											entity.setQuantity((double) row.getCell(9).getNumericCellValue());
										} else {
											entity.setQuantity((double) 0);
										}

										if (row.getCell(10) != null) {
											entity.setRate((double) row.getCell(10).getNumericCellValue());
										} else {
											entity.setRate((double) 0);
										}

										if (row.getCell(11) != null) {
											entity.setDiscount((double) row.getCell(11).getNumericCellValue());
										} else {
											entity.setDiscount((double) 0);
										}

										if (row.getCell(12) != null) {
											entity.setCgst((double) row.getCell(12).getNumericCellValue());
										} else {
											entity.setCgst((double) 0);
										}

										if (row.getCell(13) != null) {
											entity.setIgst((double) row.getCell(13).getNumericCellValue());

										} else {
											entity.setIgst((double) 0);
										}

										if (row.getCell(14) != null) {
											entity.setSgst((double) row.getCell(14).getNumericCellValue());

										} else {
											entity.setSgst((double) 0);
										}

										if (row.getCell(15) != null) {
											entity.setState_com_tax((double) row.getCell(15).getNumericCellValue());

										} else {
											entity.setState_com_tax((double) 0);
										}

										if (row.getCell(16) != null) {
											entity.setLabour_charges((double) row.getCell(16).getNumericCellValue());

										} else {
											entity.setLabour_charges((double) 0);
										}

										if (row.getCell(17) != null) {
											entity.setFrieght((double) row.getCell(17).getNumericCellValue());

										} else {
											entity.setFrieght((double) 0);
										}

										if (row.getCell(18) != null) {
											entity.setOthers((double) row.getCell(18).getNumericCellValue());

										} else {
											entity.setOthers((double) 0);
										}

										if (row.getCell(19) != null) {
											try {
												for (UnitOfMeasurement uom : uomList) {
													String str = uom.getUnit().trim();
													if (str.replace(" ","").trim().equalsIgnoreCase(row.getCell(19).getStringCellValue().replace(" ","").trim())) {
														entity.setUom_id(uom);
														break;
													}
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

										if (row.getCell(20) != null) {
											entity.setVAT((double) row.getCell(20).getNumericCellValue());

										} else {
											entity.setVAT((double) 0);
										}

										if (row.getCell(21) != null) {
											entity.setVATCST((double) row.getCell(21).getNumericCellValue());

										} else {
											entity.setVATCST((double) 0);
										}

										if (row.getCell(22) != null) {
											entity.setExcise((double) row.getCell(22).getNumericCellValue());

										} else {
											entity.setExcise((double) 0);
										}
										cgst=entity.getCgst();
										igst=entity.getIgst();
										sgst=entity.getSgst();
										state_comp_tax= entity.getState_com_tax();
									    vat=entity.getVAT();
										vatcst=entity.getVATCST();
										excise=entity.getExcise();

										    Double tamount = (double) 0;
										    Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers());
											Double damount = (entity.getRate()*entity.getQuantity());
											
											if(entity.getDiscount()!=0)
											{
												Double disamount =((round(damount,2))-(entity.getDiscount()));
												tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers();
											}
											else
											{
												tamount = amount;
											}

											transaction_value = round(tamount, 2);
											
											
											if(customer1!=null && customer1.getTds_applicable()!=null)
											{
												if(customer1.getTds_applicable().equals(1) && customer1.getTds_rate()!=null)
												{
													tds = (transaction_value*customer1.getTds_rate())/100;
												}
											}
											round_off = cgst + igst + sgst + state_comp_tax + vat+vatcst+excise+ transaction_value-tds;
                                           

										entity.setTransaction_amount(round(tamount, 2));
										note.setRound_off(round(round_off, 2));
										note.setTds_amount(tds);
										note.setTransaction_value_head(round(transaction_value, 2));
										note.setCGST_head(round(cgst, 2));
										note.setIGST_head(round(igst, 2));
										note.setSGST_head(round(sgst, 2));
										note.setSCT_head(round(state_comp_tax, 2));
										note.setTotal_vat(round(vat, 2));
										note.setTotal_vatcst(round(vatcst, 2));
										note.setTotal_excise(round(excise, 2));

									} else {
										note.setRound_off(round((double) row.getCell(6).getNumericCellValue(), 2));
									}

									if ((product1 != null && customer1!=null && note.getVoucher_no()!=null) && (note.getDescription()!=null && (note.getDescription()==1||note.getDescription()==4||note.getDescription()==5||note.getDescription()==6||note.getDescription()==7))) 
									{

										if (count == 5)
										{

											note.setFlag(true);
											sucount = sucount + 1;
											successVocharList.add(creditVocherNofromExcel);
											repetProductafterunsuccesfullattempt = repetProductafterunsuccesfullattempt + 1;

											note.setEntry_status(MyAbstractController.ENTRY_PENDING);
											Long id = creditNoteService.saveCreditNoteThroughExcel(note);
											creditNoteService.updateCreditDetailsThroughExcel(note.getAccounting_year_id().getYear_id(),note.getDate(),entity, id, company_id);

											CreditNote creditNote  = creditNoteService.getById(id);
											
											if(creditNote.getSales_bill_id() != null){
												SalesEntry salesEntry = salesEntryDao.findOneWithAll(creditNote.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
												double total=(double) 0;
												for (CreditNote note1 : salesEntry.getCreditNote()) {
														total += note1.getRound_off();				
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
											// stock
											 if(!product1.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
											 {
											 Integer pflag=productService.checktype(company_id,product1.getProduct_id());
										     if(pflag==1)  
										     {
											      Stock stock = null;
											      stock=stockService.isstockexist(company_id,product1.getProduct_id());
											      if(stock!=null)
													{
											    	    double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, product1.getProduct_id(),(double)entity.getQuantity());
											    	    stock.setAmount(stock.getAmount()-amount);
														stock.setQuantity(stock.getQuantity()-entity.getQuantity());					
														stockService.updateStock(stock);
													}
													
										     }	
										   }
											
											if (customer1!=null) {
												customerService.addcustomertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, customer1.getCustomer_id(),(long) 5, (double)round_off, (double) 0, (long) 1);
											}			
											if(subledger1 != null) {
												subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id,subledger1.getSubledger_Id(), (long) 2, (double) 0, (double)transaction_value, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
											}		
											if (note.getTds_amount() != null && note.getTds_amount() != 0) {
												SubLedger subledgertds = subledgerDao.findOne("TDS Receivable", company_id);
												subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)note.getTds_amount(), (double) 0, (long) 1,null,null,null,null,note,null,null,null,null,null,null,null);
											}
											
												if (cgst > 0) {
													SubLedger subledgercgst = subledgerDao.findOne("Output CGST", company_id);
										
													if (subledgercgst != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgercgst.getSubledger_Id(), (long) 2, (double) 0, (double)cgst, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
										
												}
												if (sgst > 0) {
													SubLedger subledgersgst = subledgerDao.findOne("Output SGST", company_id);
										
													if (subledgersgst != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgersgst.getSubledger_Id(), (long) 2, (double) 0, (double)sgst, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
										
												if (igst > 0) {
													SubLedger subledgerigst = subledgerDao.findOne("Output IGST", company_id);
										
													if (subledgerigst != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgerigst.getSubledger_Id(), (long) 2, (double) 0, (double)igst, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
										
												if (state_comp_tax > 0) {
													SubLedger subledgercess = subledgerDao.findOne("Output CESS", company_id);
										
													if (subledgercess != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgercess.getSubledger_Id(), (long) 2, (double) 0, (double)state_comp_tax, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
										
												if (vat > 0) {
													SubLedger subledgervat = subledgerDao.findOne("Output VAT", company_id);
										
													if (subledgervat != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgervat.getSubledger_Id(), (long) 2, (double) 0, (double)vat, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
												if (vatcst > 0) {
													SubLedger subledgercst = subledgerDao.findOne("Output CST", company_id);
										
													if (subledgercst != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgercst.getSubledger_Id(), (long) 2, (double) 0, (double)vatcst, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
												if ( excise > 0) {
													SubLedger subledgerexcise = subledgerDao.findOne("Output EXCISE", company_id);
										
													if (subledgerexcise != null) {
														subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, subledgerexcise.getSubledger_Id(), (long) 2, (double) 0, (double)excise, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
													}
												}
										} 
										else {
											
											if(!successVocharList.contains(creditVocherNofromExcel))
											{
												maincount = maincount + 1;
												failcount = failcount + 1;
												failureVocharList.add(creditVocherNofromExcel);
												repetProductafterunsuccesfullattempt = repetProductafterunsuccesfullattempt + 1;
											}
										}

									}
									else if ((product1 == null && note.getVoucher_no()!=null)&&(note.getDescription()!=null && (note.getDescription()==2||note.getDescription()==3))) 
									{
										if (count == 4) {

											note.setFlag(true);
											sucount = sucount + 1;
											successVocharList.add(creditVocherNofromExcel);
											repetProductafterunsuccesfullattempt = repetProductafterunsuccesfullattempt + 1;

											note.setEntry_status(MyAbstractController.ENTRY_PENDING);
											Long id = creditNoteService.saveCreditNoteThroughExcel(note);
											CreditNote creditNote  = creditNoteService.getById(id);
											
											if(creditNote.getSales_bill_id() != null){
												SalesEntry salesEntry = salesEntryDao.findOneWithAll(creditNote.getSales_bill_id().getSales_id());/** we got all previous receipts and current receipt with current sales entry as we are getting receipts eagerly from sales entry*/
												double total=(double) 0;
												for (CreditNote note1 : salesEntry.getCreditNote()) {
														total += note1.getRound_off();				
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
											
											if (customer1!=null) {
												customerService.addcustomertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id, customer1.getCustomer_id(),(long) 5, (double)round_off, (double) 0, (long) 1);
											}			
											if(subledger1 != null) {
												subLedgerService.addsubledgertransactionbalance(note.getAccounting_year_id().getYear_id(),note.getDate(),company_id,subledger1.getSubledger_Id(), (long) 2, (double) 0, (double)round_off, (long) 1,null,null,null,null,creditNote,null,null,null,null,null,null,null);
											}
										} 
										else {
											
											if(!successVocharList.contains(creditVocherNofromExcel))
											{
												maincount = maincount + 1;
												failcount = failcount + 1;
												failureVocharList.add(creditVocherNofromExcel);
												repetProductafterunsuccesfullattempt = repetProductafterunsuccesfullattempt + 1;
											}
										}
											
									}
									else {
										
										if(!successVocharList.contains(creditVocherNofromExcel))
										{
											maincount = maincount + 1;
											failcount = failcount + 1;
											failureVocharList.add(creditVocherNofromExcel);
											repetProductafterunsuccesfullattempt = repetProductafterunsuccesfullattempt + 1;
										}
										
									}

									if (maincount == 0) {
										m = 1;
									} else {
										m = 2;
									}
								}
							   }
							}
						}
					}
					workbook.close();
				}
			} else {
				System.out.println("no data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			isValid = false;
		}
		if (isValid) {
			if (m == 1) {

				if (((Integer) failureVocharList.size()) > 0 && ((Integer) successVocharList.size()) > 0) {
					successmsg.append(((Integer) successVocharList.size()).toString());
					successmsg.append(" Records imported successfully");
					successmsg.append(System.lineSeparator());
					failuremsg.append(((Integer) failureVocharList.size()).toString());
					failuremsg.append(" Records Not imported, Please check these records");
					failuremsg.append(System.lineSeparator());
				}

				if (((Integer) failureVocharList.size()) == 0 && ((Integer) successVocharList.size()) > 0) {
					successmsg.append(((Integer) successVocharList.size()).toString());
					successmsg.append(" Records imported successfully");
					successmsg.append(System.lineSeparator());
					failuremsg.append("");
					failuremsg.append(System.lineSeparator());
				}

				if (((Integer) failureVocharList.size()) > 0 && ((Integer) successVocharList.size()) == 0) {
					successmsg.append("");
					successmsg.append(System.lineSeparator());
					failuremsg.append(((Integer) failureVocharList.size()).toString());
					failuremsg.append(" Records Not imported, Please check these records");
					failuremsg.append(System.lineSeparator());

				}

				if (((Integer) failureVocharList.size()) == 0 && ((Integer) successVocharList.size()) == 0) {
					successmsg.append("No Records Imported, Please Check Your Excel");
					successmsg.append(System.lineSeparator());
					failuremsg.append("");
					failuremsg.append(System.lineSeparator());
				}
				String successmsg1 = successmsg.toString();
				String failuremsg1 = failuremsg.toString();
				session.setAttribute("filemsg", successmsg1);
				session.setAttribute("filemsg1", failuremsg1);
				return new ModelAndView("redirect:/creditNoteList");
			} else if (m == 2) {
				if (((Integer) failureVocharList.size()) > 0 && ((Integer) successVocharList.size()) > 0) {
					successmsg.append(((Integer) successVocharList.size()).toString());
					successmsg.append(" Records imported successfully");
					successmsg.append(System.lineSeparator());
					failuremsg.append(((Integer) failureVocharList.size()).toString());
					failuremsg.append(" Records Not imported, Please check these records");
					failuremsg.append(System.lineSeparator());
				}

				if (((Integer) failureVocharList.size()) == 0 && ((Integer) successVocharList.size()) > 0) {
					successmsg.append(((Integer) successVocharList.size()).toString());
					successmsg.append(" Records imported successfully");
					successmsg.append(System.lineSeparator());
					failuremsg.append("");
					failuremsg.append(System.lineSeparator());
				}

				if (((Integer) failureVocharList.size()) > 0 && ((Integer) successVocharList.size()) == 0) {
					successmsg.append("");
					successmsg.append(System.lineSeparator());
					failuremsg.append(((Integer) failureVocharList.size()).toString());
					failuremsg.append(" Records Not imported, Please check these records");
					failuremsg.append(System.lineSeparator());

				}

				if (((Integer) failureVocharList.size()) == 0 && ((Integer) successVocharList.size()) == 0) {
					successmsg.append("No Records Imported, Please Check Your Excel");
					successmsg.append(System.lineSeparator());
					failuremsg.append("");
					failuremsg.append(System.lineSeparator());
				}
				String successmsg1 = successmsg.toString();
				String failuremsg1 = failuremsg.toString();
				session.setAttribute("filemsg", successmsg1);
				session.setAttribute("filemsg1", failuremsg1);
				return new ModelAndView("redirect:/creditNoteList");
			} else {
				successmsg.append("You are inserting duplicate records, please check excel file");
				String successmsg1 = successmsg.toString();
				session.setAttribute("filemsg", successmsg1);
				return new ModelAndView("redirect:/creditNoteList");
			}

		} else {
			successmsg.append("Please enter required and valid data in columns");
			String successmsg1 = successmsg.toString();
			session.setAttribute("filemsg", successmsg1);
			return new ModelAndView("redirect:/creditNoteList");
		}
	}

	@RequestMapping(value = "creditNoteFailure", method = RequestMethod.GET)
	public ModelAndView creditNoteFailure(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
		flag = false;
		 List<YearEnding>  yearEndlist = yearService.findAllYearEnding(company_id);
		  if((String) session.getAttribute("msg")!=null){
				model.addObject("successMsg", (String) session.getAttribute("msg"));
				session.removeAttribute("msg");
			}
		//model.addObject("creditNoteList", creditNoteService.findAllCreditNoteOfCompany(company_id, false));
		model.addObject("flagFailureList", false);
		model.addObject("yearEndlist", yearEndlist);
		model.setViewName("/transactions/creditNoteList");
		return model;
	}

	 public static Double round(Double d, int decimalPlace) {
	    	DecimalFormat df = new DecimalFormat("#");
			df.setMaximumFractionDigits(decimalPlace);
			return new Double(df.format(d));
	    }

	
	 @RequestMapping(value="getOpeningBalanceforcreditnote", method=RequestMethod.GET)
		public @ResponseBody Double  getOpeningBalance(@RequestParam("customerid") Long customerid,HttpServletRequest request, HttpServletResponse response)
		{
		 System.out.println("ob for cn " +customerid);
	Company company=null;
	HttpSession session = request.getSession(true);
	 long company_id=(long)session.getAttribute("company_id");
	 System.out.println("The company id is "+company_id);
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