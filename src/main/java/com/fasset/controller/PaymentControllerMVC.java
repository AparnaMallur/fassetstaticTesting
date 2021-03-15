package com.fasset.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.fasset.controller.validators.PaymentDetailValidator;
import com.fasset.controller.validators.PaymentValidator;
import com.fasset.dao.interfaces.IPaymentDAO;
import com.fasset.dao.interfaces.IPurchaseEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PaymentDetails;
import com.fasset.entities.Product;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Service;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.UnitOfMeasurement;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.report.controller.ClassToConvertDateForExcell;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.IDebitNoteService;
import com.fasset.service.interfaces.IGstTaxMasterService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IProductService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.fasset.service.interfaces.IUOMService;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IYearEndingService;

@Controller
@SessionAttributes("user")
public class PaymentControllerMVC extends MyAbstractController{
	
	@Autowired
	private IPaymentService paymentService ;
	@Autowired
	private IDebitNoteService debitNoteService;

	@Autowired
	private ICompanyService companyService ;
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private ISuplliersService suplliersService ;
	
	@Autowired
	private IPurchaseEntryService purchaseEntryService;
	
	@Autowired
	private IAccountingYearService accountingYearService ;
	
	@Autowired
	private ISubLedgerService subLedgerService;
	
	@Autowired
	private IBankService bankService;
	
	@Autowired
	private PaymentValidator paymentValidator ;
	
	@Autowired
	private PaymentDetailValidator paymentDetailValidator ;  
	
	@Autowired
	private ICommonService commonService; 
	
	@Autowired
	private IUOMService uomService;
	
	@Autowired
	private ISubLedgerDAO subledgerDAO ;	
	
	@Autowired
	private IPurchaseEntryDAO purchaseEntryDao;
	
	@Autowired
	private IGstTaxMasterService gstService;
	
	@Autowired
	private IPaymentDAO paymentDao;
	
	@Autowired
	private IUserService userService;
	
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
		
	@Autowired
	private IOpeningBalancesService openingbalances;
	
	@Autowired	
	private IYearEndingService yearService;
	
	Boolean flag = null; // for maintaining the user on paymentList.jsp after delete and view
	
	@RequestMapping(value = "paymentList", method = RequestMethod.GET)
	public ModelAndView paymentList(@RequestParam(value = "msg", required = false)String msg,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		User user = new User();
		user = (User) session.getAttribute("user");
		System.out.println("paymentList");
		Company company=null;
		try {
			company = companyService.getById(user.getCompany().getCompany_id());
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView model = new ModelAndView();
		flag = true;
		boolean importfail=false;	
		boolean importflag = false;
		List<YearEnding>  yearEndlist = yearService.findAllYearEnding(company.getCompany_id());
		if((String) session.getAttribute("msg")!=null)
		{
		model.addObject("successMsg", (String) session.getAttribute("msg"));
		session.removeAttribute("msg");
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
			if(year.getYear_range().equalsIgnoreCase(currentyear))
			{
				yearId=year.getYear_id();
			}
		}

		if(paymentService.findAllPaymentOfCompany(company_id, false).size()!=0)
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
		model.addObject("importflag", importflag);
		model.addObject("paymentList", paymentService.findAllPaymentOfCompany(company_id, true));
		model.addObject("importfail",importfail);
		model.addObject("flagFailureList", true);
		model.addObject("yearEndlist", yearEndlist);
		model.addObject("company", company);
		model.setViewName("/transactions/paymentList");
		return model;
	}
	
	
	
	@RequestMapping(value = "payment", method = RequestMethod.GET)
	public ModelAndView payment(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		long user_id=(long)session.getAttribute("user_id");
		long company_id=(long)session.getAttribute("company_id");
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		user = (User) session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		
		//List<AccountingYear>  yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id); commented not to show account year popup
		if(AccYear==-1){
			yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		}else{
			yearList = accountingYearService.findAccountRangeOfCompany(strLong);
		 
		}
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
		if(yearList.size()!=0)
		{
	    Payment payment = new Payment();	
		model.addObject("voucherrange", companyService.getVoucherRange(company_id));
		model.addObject("supplierList", suplliersService.findAllSuppliersOfCompany(company_id));
		model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanywithdefault(company_id));
		model.addObject("yearList",yearList);
		model.addObject("productList", productService.findAllProductsOfCompany(company_id));
		model.addObject("purchaseEntryList", purchaseEntryService.findAllPurchaseEntryOfCompany(company_id, true));
		model.addObject("payment", payment);
		model.addObject("paidtds", 0);
		model.addObject("bankList", bankService.findAllBanksOfCompany(company_id));
		model.addObject("stateId", user.getCompany().getState().getState_id());
		
		
	
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
		
		model.setViewName("/transactions/payment");
		return model;
		}
		else
		{
			session.setAttribute("msg","Your account is closed");
			return new ModelAndView("redirect:/paymentList");
			
		}
		
	}
	
	@RequestMapping(value = "payment", method = RequestMethod.POST)
	public synchronized ModelAndView payment(@ModelAttribute("payment")Payment payment, 
			BindingResult result, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		Company company = new Company();
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		paymentValidator.validate(payment, result);
		System.out.println("3");
		User user = (User) session.getAttribute("user");	
		long company_id=user.getCompany().getCompany_id();
		long user_id=(long)session.getAttribute("user_id");
		String yearrange = user.getCompany().getYearRange();
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		double closingbalance=0;
		if(payment.getSupplier()!=null && payment.getCompany()!= null && payment.getAccountingYear()!=null)
		{
			closingbalance=openingbalances.getclosingbalance(payment.getCompany().getCompany_id(),payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getSupplier().getSupplier_id(),4);
		}
		else if(payment.getSubLedger()!=null && payment.getCompany()!= null && payment.getAccountingYear()!=null)
		{
			closingbalance=openingbalances.getclosingbalance(payment.getCompany().getCompany_id(),payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getSubLedger().getSubledger_Id(),2);
		}		
		

		try {
			company = companyService.getById(company_id);
		} catch (MyWebException e) {			
			e.printStackTrace();
		}
		//List<AccountingYear>  yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);

		String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018
		LocalDate date1= new LocalDate();
		String april1stDate=null;
		april1stDate= currentyear+"-"+"04"+"-"+"01";
		LocalDate april1stLocaldate = new LocalDate(april1stDate);
		
		if(date1.isBefore(april1stLocaldate)) 
		{
			Integer year = Integer.parseInt(currentyear);
			year=year-1;
			String lastYear =year.toString();
			currentyear=lastYear+"-"+currentyear;
		}
		else if(date1.isAfter(april1stLocaldate) || date1.equals(april1stLocaldate))
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
		}*/
		if(result.hasErrors()){
			if(AccYear==-1){
				yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
			}else{
				yearList = accountingYearService.findAccountRangeOfCompany(strLong);
			 
			}
			//model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
			model.addObject("yearList",yearList);
			model.addObject("voucherrange", companyService.getVoucherRange(company_id));
		    model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanywithdefault(company_id));
			model.addObject("supplierList", suplliersService.findAllSuppliersOfCompany(company_id));
			model.addObject("productList", productService.findAllProductsOfCompany(company_id));
			model.addObject("bankList", bankService.findAllBanksOfCompany(company_id));
			model.addObject("purchaseEntryList", purchaseEntryService.findAllPurchaseEntryOfCompany(company_id, true));
			model.addObject("stateId",user.getCompany().getState().getState_id());
			model.addObject("closingbalance", closingbalance);
			model.setViewName("/transactions/payment");	
			return model;
		}
		else{
			String msg = "";			
			try{			
				if(payment.getPayment_id()!= null){		
					
					String[] date=payment.getDateString().split("-");					
					String newdate=date[2]+"-"+date[1]+"-"+date[0];
					payment.setDate(new LocalDate(newdate));
					payment.setUpdated_by(user_id);
					
					paymentService.update(payment);
					msg = "Payment updated successfully";
				}
				else {			
					payment.setCompany(company);
					payment.setFlag(true);
					String[] date=payment.getDateString().split("-");					
					String newdate=date[2]+"-"+date[1]+"-"+date[0];
					payment.setDate(new LocalDate(newdate));
					payment.setVoucher_no(commonService.getVoucherNumber("PV", VOUCHER_PAYMENT, payment.getDate(),payment.getAccountYearId(), user.getCompany().getCompany_id()));	
					
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            payment.setIp_address(remoteAddr);
			            payment.setCreated_by(user_id);
					paymentService.add(payment);
					msg = "Payment added successfully";
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}		
			session.setAttribute("msg", msg);
			return new ModelAndView("redirect:/paymentList");
		}
	}
	
	@RequestMapping(value = "viewPayment", method = RequestMethod.GET)
	public ModelAndView viewPayment(@RequestParam("id") long id,
			HttpServletRequest request, 
			HttpServletResponse response) throws MyWebException {
		
		ModelAndView model = new ModelAndView();
		Payment payment = new Payment();
		try {
			if(id > 0){
				payment = paymentService.findOneWithAll(id);				
				if(payment.getPaymentDetails() != null){
					model.addObject("paymentDetailsList", payment.getPaymentDetails());
				}
				if((payment.getPayment_type()!=null)&&(payment.getPayment_type() != MyAbstractController.PAYMENT_TYPE_CASH)){
					if(payment.getCheque_date()!=null)
					{
					payment.setChequeDateString(payment.getCheque_date().toString());
					}
				}
				if(payment.getDate()!=null)
				{
				payment.setDateString(payment.getDate().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		double closingbalance=0;
		if(payment.getSupplier()!=null && payment.getCompany()!= null && payment.getAccountingYear()!=null)
		{
			closingbalance=openingbalances.getclosingbalance(payment.getCompany().getCompany_id(),payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getSupplier().getSupplier_id(),4);
		}
		else if(payment.getSubLedger()!=null && payment.getCompany()!= null && payment.getAccountingYear()!=null)
		{
			closingbalance=openingbalances.getclosingbalance(payment.getCompany().getCompany_id(),payment.getAccountingYear().getYear_id(),payment.getDate(),payment.getSubLedger().getSubledger_Id(),2);
		}		
		model.addObject("closingbalance", closingbalance);
		model.addObject("payment", payment);
		model.addObject("flag", flag);
		if(payment.getCreated_by()!=null)
		{
		model.addObject("created_by", userService.getById(payment.getCreated_by()));
		}
		if(payment.getUpdated_by()!=null)
		{
		model.addObject("updated_by", userService.getById(payment.getUpdated_by()));
		}
		model.setViewName("/transactions/paymentView");
		return model;
	}
	
	@RequestMapping(value = "editPayment", method = RequestMethod.GET)
	public synchronized ModelAndView editPayment(@RequestParam("id") long id,
			HttpServletRequest request, 
			HttpServletResponse response) {
		System.out.println("2");
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		Payment payment = new Payment();
		User user = (User) request.getSession(true).getAttribute("user");		  
		long company_id=user.getCompany().getCompany_id();
		long user_id=(long)session.getAttribute("user_id");
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		String yearrange = user.getCompany().getYearRange();
		payment = paymentService.findOneWithAll(id);
		Boolean flag = false;
		Boolean flag1 =false;
		if(payment.getAdvance_payment()!=null && payment.getAdvance_payment()==false && payment.getSupplier_bill_no()!=null)
        {
			Double paymentAmount =(double)0;
			PurchaseEntry entry = payment.getSupplier_bill_no();
			
			for (Payment paymentagainstPurchase : entry.getPayments()) {
					paymentAmount += paymentagainstPurchase.getAmount();
			}
			
			if(entry.getRound_off().equals(paymentAmount) || paymentAmount>=entry.getRound_off()) {
				flag=false;
			}
			else
			{
				flag=true;
			}
			
        } 
		else if(payment.getAdvance_payment()!=null && payment.getAdvance_payment()==true)
		{
			if(payment.getIs_purchasegenrated()!=null && payment.getIs_purchasegenrated().equals(true))
			{
				flag1=true;
				flag=false;
			}
			else
			{
				flag=true;
			}
			
		}
		else if(payment.getFlag()!=null && payment.getFlag()==false)
		{
			flag=true;
		}
		else if(payment.getSubLedger()!=null)
		{
			flag=true;
		}
		else if(payment.getAgainstOpeningBalnce()!=null && payment.getAgainstOpeningBalnce().equals(true))
		 {
			flag1=false;
			flag=false;
	     }
		
		
		if(flag==true)
		{
		try {
				if(payment.getDate()!=null)
				{
				payment.setDateString(payment.getDate().toString());
				}
				
				if(payment.getSupplier() != null){
					payment.setSupplierId(payment.getSupplier().getSupplier_id());
					if(payment.getSupplier_bill_no() != null)
					{
						payment.setPurchaseEntryId(payment.getSupplier_bill_no().getPurchase_id());
					Double paidtds=paymentService.findpaidtds(payment.getSupplier_bill_no().getPurchase_id());
					model.addObject("paidtds", paidtds);
					}
					
				}
				else{
					payment.setSupplierId((long) -1);
				}
				
				if(payment.getSubLedger() != null){
					payment.setSubLedgerId(payment.getSubLedger().getSubledger_Id());
					
				}
			
				if(payment.getBank() != null){
					payment.setBankId(payment.getBank().getBank_id());
				}
				
				if((payment.getPayment_type()!=null)&&(payment.getPayment_type() == PAYMENT_TYPE_CHEQUE || payment.getPayment_type() == PAYMENT_TYPE_DD || payment.getPayment_type() == MyAbstractController.PAYMENT_TYPE_NEFT)){
					
					if(payment.getCheque_date()!=null)
					{
						
					payment.setChequeDateString(payment.getCheque_date().toString());
					}
				}
				if(payment.getPaymentDetails() != null && payment.getPaymentDetails().size() > 0){
					model.addObject("paymentDetailsList", payment.getPaymentDetails());
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//List<AccountingYear>  yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);

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
		}*/
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		if(AccYear==-1){
			yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		}else{
			yearList = accountingYearService.findAccountRangeOfCompany(strLong);
		 
		}
		//model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
		model.addObject("yearList",yearList);
		model.addObject("voucherrange", companyService.getVoucherRange(company_id));
	    model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanywithdefault(company_id));
		model.addObject("supplierList", suplliersService.findAllSuppliersOfCompany(company_id));
		model.addObject("productList", productService.findAllProductsOfCompany(company_id));
		model.addObject("bankList", bankService.findAllBanksOfCompany(company_id));
		model.addObject("purchaseEntryList", purchaseEntryService.findAllPurchaseEntryOfCompany(company_id, true));
		model.addObject("stateId",user.getCompany().getState().getState_id());
		model.addObject("payment",payment);
		model.setViewName("/transactions/payment");
		
	   }
		else if(flag1==true && flag==false) {
			
			session.setAttribute("msg", "You can't edit payment voucher because purchase voucher is generated for this payment");
			model.setViewName("redirect:/paymentList");
		}
		else if(payment.getAgainstOpeningBalnce()!=null && flag1==false && flag==false)
		{
			session.setAttribute("msg", "You can't edit payment voucher because payment voucher is generated against opening balance of supplier");
			return new ModelAndView("redirect:/paymentList");
		}
		else
		{
			session.setAttribute("msg", "You can't edit payment voucher because you have completed your total payment against your purchase voucher");
			model.setViewName("redirect:/paymentList");
		}
		return model;
	}	
	@RequestMapping(value="deletePayment", method = RequestMethod.GET)
	public synchronized ModelAndView deletePayment(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		Payment payment = new Payment();
		payment = paymentService.findOneWithAll(id);
		String msg="";
		
		
		if(payment.getIs_purchasegenrated()!=null && payment.getIs_purchasegenrated().equals(true))
		{
			session.setAttribute("msg", "You can't delete payment voucher because purchase voucher is generated for this payment");
			return new ModelAndView("redirect:/paymentList");
		}
		else if(payment.getAgainstOpeningBalnce()!=null && payment.getAgainstOpeningBalnce().equals(true))
		{
			session.setAttribute("msg", "You can't delete payment voucher because payment voucher is generated against opening balance of supplier");
			return new ModelAndView("redirect:/paymentList");
		}
		else
		{
			msg=paymentService.deleteByIdValue(id);
			session.setAttribute("msg", msg);
			if(flag == true)
			{
				return new ModelAndView("redirect:/paymentList");
			}
			else
			{
				return new ModelAndView("redirect:/paymentFailure");
			}
			
		}
	}
	@RequestMapping(value = "downloadPayment", method = RequestMethod.GET)
    public ModelAndView downloadReceipt(@RequestParam("id") long id) {	 
		System.out.println("Download excel");
		Payment payment = paymentService.findOneWithAll(id);
		return new ModelAndView("PaymentPdfView", "payment", payment);
    }	
	/*@RequestMapping(value = "downloadPaymentAccountingVoucher", method = RequestMethod.GET)
    public ModelAndView downloadPaymentAccountingVoucher(@RequestParam("id") long id) {	 
		PaymentForm form = new PaymentForm();
		Payment payment = paymentService.findOneWithAll(id);
		form.setPayment(payment);
		return new ModelAndView("PaymentAccountingVoucherPdfView", "form", form);
    }	*/
	@RequestMapping(value = {"importExcelPayment"}, method = { RequestMethod.POST })
	public ModelAndView importExcelPayment(@RequestParam("excelFile") MultipartFile excelfile,HttpServletRequest request, HttpServletResponse response) throws IOException{
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
		
		List<Suppliers> list =suplliersService.findAllSuppliersOnlyOfCompany(company_id);
		List<PurchaseEntry> purchaselist =purchaseEntryService.findAllPurchaseEntriesOfCompany(company_id,false);
		List<SubLedger> sublist =subLedgerService.findAllApprovedByCompanywithdefault(company_id);
		List<AccountingYear> yearlist =accountingYearService.findAccountRange(user_id, yearrange,company_id);
		List<Bank> banklist =bankService.findAllBanksOfCompany(company_id);
		List<UnitOfMeasurement> uomlist =uomService.findAllListing();
		
		try {
			   if (excelfile.getOriginalFilename().endsWith("xls")) {
	        	
	    			int i = 0;
	    			HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
	    			HSSFSheet worksheet = workbook.getSheetAt(0);
	    		
					if(isValid){
						i = 1;
		    			while (i <= worksheet.getLastRowNum()) {

		    				Payment entry = new Payment();
							PaymentDetails entity = new  PaymentDetails();
							Suppliers supplier1 =null;
							PurchaseEntry purchseEntry1=null;
							SubLedger subledger1= null;
							Bank bank1 = null;
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
								Double tds = (double)0;
								Double Newtds = (double)0;
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
								boolean flag = false ;
								
								String purchaseVoucherNofromExcel="";
								String paymentVoucherNofromExcel="";
								String chequeNofromExcel="";
							
								try
								{
									purchaseVoucherNofromExcel=row.getCell(1).getStringCellValue().trim().replaceAll(" ", "");;
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try
								{
									Double voucherNofromExcel= row.getCell(1).getNumericCellValue();
									Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
									purchaseVoucherNofromExcel=voucherNofromExcel1.toString().trim();
									
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try
								{
									paymentVoucherNofromExcel=row.getCell(3).getStringCellValue().trim().replaceAll(" ", "");;
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try
								{
									Double voucherNofromExcel= row.getCell(3).getNumericCellValue();
									Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
									paymentVoucherNofromExcel=voucherNofromExcel1.toString().trim();
									
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
								
								Payment oldentry = paymentService.isExcelVocherNumberExist(paymentVoucherNofromExcel, company_id);	
									
									if(oldentry!=null)
									{
										
										Set<PaymentDetails> paymentDetails = oldentry.getPaymentDetails();
										Boolean mainentryflag = false;
										String finalhsncode ="1";
										
										if(paymentDetails!=null && paymentDetails.size()!=0)
										{
										String vocherNo = oldentry.getExcel_voucher_no().trim();
										Long company_Id = oldentry.getCompany().getCompany_id();
										String ProductName = row.getCell(14).getStringCellValue().trim();
										
										if(row.getCell(13)!=null)
										{
										Double hsncode= row.getCell(13).getNumericCellValue();
										Integer hsncode1 = hsncode.intValue();
										finalhsncode=hsncode1.toString().trim();
										}
										Boolean entryflag = false ;
										
										for(PaymentDetails pD : paymentDetails)
										{
											if(finalhsncode.replace(" ","").trim().equalsIgnoreCase(pD.getHSNCode().replace(" ","").trim()) && ProductName.replace(" ","").trim().equalsIgnoreCase(pD.getProduct_name().replace(" ","").trim()))
											{
												entryflag=true;
											}
										}
										
										if((company_Id==company_id)&&(vocherNo.replace(" ","").trim().equalsIgnoreCase(paymentVoucherNofromExcel.replace(" ","").trim()))&&(entryflag==true))
										{
											mainentryflag=true;
										}
										}
										else
										{
											mainentryflag=true;
										}
										
								  if(mainentryflag==false)
								  {	
										flag = true;
										
											
											if(oldentry.getGst_applied()==true)
											{
												if(oldentry.getSupplier()!=null)
												{
												supplier1=oldentry.getSupplier();
												}
												
												transaction_value=oldentry.getTransaction_value_head();
												round_off=oldentry.getRound_off();
												cgst=oldentry.getCGST_head();
												igst=oldentry.getIGST_head();
												sgst= oldentry.getSGST_head();
												state_comp_tax=oldentry.getSCT_head();
												vat= oldentry.getTotal_vat();
												vatcst=oldentry.getTotal_vatcst();
												excise=oldentry.getTotal_excise();
												tds = oldentry.getTds_amount();
												String ProductName = row.getCell(14).getStringCellValue().trim();
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
															entity.setProduct_id(product);
															entity.setProduct_name(product.getProduct_name());
															entity.setHSNCode(finalhsncode);
															flag1=true;
															product1=product;
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
												entity.setQuantity((int) row.getCell(15).getNumericCellValue());
												}else
												{
													entity.setQuantity((int) 0);
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
													entity.setCgst((Double) row.getCell(18).getNumericCellValue());
												}else
												{
													entity.setCgst((double)0);
												}

												
												if(row.getCell(19)!=null)
												{
													entity.setIgst((Double) row.getCell(19).getNumericCellValue());

												}else
												{
													entity.setIgst((double)0);
												}

												if(row.getCell(20)!=null)
												{
													entity.setSgst((Double) row.getCell(20).getNumericCellValue());

												}else
												{
													entity.setSgst((double)0);
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
													entity.setFrieght((Double) row.getCell(23).getNumericCellValue());

												}else
												{
													entity.setFrieght((double)0);
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
												try {
													for(UnitOfMeasurement uom:uomlist)
													{
														
														String str=uom.getUnit().trim();
														if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(25).getStringCellValue().replace(" ","").trim())) {
															entity.setUom_id(uom);
														    break;
														}
													}
												}
												catch(Exception e) {
													e.printStackTrace();
												}
												}
												
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

												
												cgst=cgst+entity.getCgst();
												igst=igst+entity.getIgst();
												sgst=sgst+entity.getSgst();
												state_comp_tax= state_comp_tax+entity.getState_com_tax();
												vat=vat+entity.getVAT();
												vatcst=vatcst+entity.getVATCST();
												excise=excise+entity.getExcise();
												
												    Double tamount=(double)0;
													Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers());
													Double damount = (entity.getRate()*entity.getQuantity());
													
													
													if(entity.getDiscount()!=0)
													{
														Double disamount =((round(damount,2))-((((round(damount,2))*(entity.getDiscount())))/100));
														tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers();
													}
													else
													{
														tamount = amount;
													}
												transaction_value=transaction_value+round(tamount,2);
												round_off=(round_off+tds)+entity.getCgst()+entity.getIgst()+entity.getSgst()+entity.getState_com_tax()+tamount ;
											
												if(oldentry.getAdvance_payment()!=null && oldentry.getAdvance_payment()==true && oldentry.getTds_paid()==true)
												{
												
													if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
													{
														if(supplier1.getTds_rate()!=null)
														{
														Newtds = (transaction_value*supplier1.getTds_rate())/100;
														}
														
														round_off=round_off-Newtds;
													}
														
												}
												else
												{
													oldentry.setTds_paid(false);
													oldentry.setTds_amount(Newtds);
												}
												
												
												
												if(product1!=null)
												{
												
												if ((oldentry.getSupplier() != null)) {
													suplliersService.addsuppliertrsansactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,
															oldentry.getSupplier().getSupplier_id(), (long) 4, (double)0, ((double)oldentry.getRound_off()+tds),
															(long) 0);
													suplliersService.addsuppliertrsansactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,
															oldentry.getSupplier().getSupplier_id(), (long) 4, (double)0, ((double)round_off+Newtds), (long) 1);
												} 
												
												
												if(oldentry.getAdvance_payment()!=null && oldentry.getAdvance_payment()==true)
												{
												if(oldentry.getTds_amount()!=null && oldentry.getTds_paid() !=null && oldentry.getTds_paid()==true)
												{
													  SubLedger subledgerTds = subledgerDAO.findOne("TDS Payable", company_id);
													  if (subledgerTds != null) {
														  subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id, subledgerTds.getSubledger_Id(), (long) 2,  (double)tds, (double)0, (long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														  subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id, subledgerTds.getSubledger_Id(), (long) 2,  (double)Newtds, (double)0, (long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
													  }
												}
												}
												
												
												if (oldentry.getGst_applied()==true)
												 {
													 if(oldentry.getPayment_type()==1)
													 {
														 if((oldentry.getTransaction_value_head()!=null) && (oldentry.getTransaction_value_head()>0) && (transaction_value>0) && (oldentry.getTransaction_value_head()!=transaction_value))
														 {
															
															if(transaction_value>0)
															 {
																 SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",company_id);
																	
																	if(subledgercash!=null)
																	{
																		subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)(oldentry.getTransaction_value_head()),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																		subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)(transaction_value),(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																	}	
															 }
															
														 }
															
													 }
													 else
													 {
														 if((oldentry.getTransaction_value_head()!=null) && (oldentry.getTransaction_value_head()>0)&& (transaction_value>0) && (oldentry.getTransaction_value_head()!=transaction_value))
														    {
															 if(oldentry.getBank()!=null)
															 {
															 bank1=oldentry.getBank();
															 bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)(oldentry.getTransaction_value_head()),(double)0,(long) 0);
															 bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)(transaction_value),(double)0,(long) 1);
															 }
														 	}
														 
														 
														
													 }	
													 if((oldentry.getCGST_head()!=null)&&(oldentry.getCGST_head()>0)&&(cgst>0)&&(oldentry.getCGST_head()!=cgst))
														{
															SubLedger subledgercgst = subledgerDAO.findOne("Input CGST",company_id);
															if(subledgercgst!=null)
															{
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)oldentry.getCGST_head(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)cgst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getCGST_head()!=null)&&(cgst>0) && (oldentry.getCGST_head()==0))
														{
															SubLedger subledgercgst = subledgerDAO.findOne("Input CGST",company_id);
															if(subledgercgst!=null)
															{
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)cgst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														if((oldentry.getSGST_head()!=null)&&(oldentry.getSGST_head()>0)&&(sgst>0)&&(oldentry.getSGST_head()!=sgst))
														{
															SubLedger subledgersgst = subledgerDAO.findOne("Input SGST",company_id);
															
															if(subledgersgst!=null)
															{
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)oldentry.getSGST_head(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)sgst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getSGST_head()!=null)&&(sgst>0)&&(oldentry.getSGST_head()==0))
														{
		                                                     SubLedger subledgersgst = subledgerDAO.findOne("Input SGST",company_id);
															
															if(subledgersgst!=null)
															{
														
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)sgst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
															
														}
														
														if((oldentry.getIGST_head()!=null) && (oldentry.getIGST_head()>0) && (igst>0) && (oldentry.getIGST_head()!=igst))
														{
															SubLedger subledgerigst = subledgerDAO.findOne("Input IGST",company_id);
															
															if(subledgerigst!=null)
															{
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)oldentry.getIGST_head(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)igst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getIGST_head()!=null)&&(igst>0)&&(oldentry.getIGST_head()==0))
														{
		                                                    SubLedger subledgerigst = subledgerDAO.findOne("Input IGST",company_id);
															
															if(subledgerigst!=null)
															{
														
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)igst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														
														if((oldentry.getSCT_head()!=null) && (oldentry.getSCT_head()>0) && (state_comp_tax>0)&&(oldentry.getSCT_head()!=state_comp_tax))
														{
															SubLedger subledgercess = subledgerDAO.findOne("Input CESS",company_id);
															
															if(subledgercess!=null)
															{
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)oldentry.getSCT_head(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)state_comp_tax,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getSCT_head()!=null) && (state_comp_tax>0) && (oldentry.getSCT_head()==0))
														{
		                                                    SubLedger subledgercess = subledgerDAO.findOne("Input CESS",company_id);
															
															if(subledgercess!=null)
															{
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)state_comp_tax,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														
														if((oldentry.getTotal_vat()!=null) && (oldentry.getTotal_vat()>0) && (vat>0)&&(oldentry.getTotal_vat()!=vat))
														{
															SubLedger subledgervat = subledgerDAO.findOne("Input VAT",company_id);
															
															if(subledgervat!=null)
															{
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)oldentry.getTotal_vat(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)vat,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getTotal_vat()!=null) && (vat>0) && (oldentry.getTotal_vat()==0))
														{
		                                                     SubLedger subledgervat = subledgerDAO.findOne("Input VAT",company_id);
															
															if(subledgervat!=null)
															{
					
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)vat,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														
														if((oldentry.getTotal_vatcst()!=null) && (oldentry.getTotal_vatcst()>0) && (vatcst>0)&&(oldentry.getTotal_vatcst()!=vatcst))
														{
															SubLedger subledgercst = subledgerDAO.findOne("Input CST",company_id);
															
															if(subledgercst!=null)
															{subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)oldentry.getTotal_vatcst(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)vatcst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getTotal_vatcst()!=null) && (vatcst>0) && (oldentry.getTotal_vatcst()==0))
														{
		                                                    SubLedger subledgercst = subledgerDAO.findOne("Input CST",company_id);
															
															if(subledgercst!=null)
															{
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)vatcst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														
														if((oldentry.getTotal_excise()!=null) && (oldentry.getTotal_excise()>0) && (excise>0)&&(oldentry.getTotal_excise()!=excise))
														{
															SubLedger subledgerexcise = subledgerDAO.findOne("Input EXCISE",company_id);
															
															if(subledgerexcise!=null)
															{subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)oldentry.getTotal_excise(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)excise,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
														else if((oldentry.getTotal_excise()!=null) && excise>0 && (oldentry.getTotal_excise()==0))
														{
		                                                    SubLedger subledgerexcise = subledgerDAO.findOne("Input EXCISE",company_id);
															
															if(subledgerexcise!=null)
															{
																subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)excise,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															}
														}
												 }
												
												entity.setTransaction_amount(round(tamount,2));
												oldentry.setRound_off(round(round_off,2));
												oldentry.setAmount(round(round_off,2));
												oldentry.setTds_amount(round(Newtds,2));
												oldentry.setTransaction_value_head(round(transaction_value,2));
												oldentry.setCGST_head(round(cgst,2));
												oldentry.setSGST_head(round(sgst,2));
												oldentry.setIGST_head(round(igst,2));
												oldentry.setSCT_head(round(state_comp_tax,2));
												oldentry.setTotal_vat(round(vat,2));
												oldentry.setTotal_vatcst(round(vatcst,2));
												oldentry.setTotal_excise(round(excise,2));
												
												
												paymentService.updatePaymentThroughExcel(oldentry);
												paymentService.updatePaymentDetailsThroughExcel(entity, oldentry.getPayment_id());
												 if(repetProductafterunsuccesfullattempt==0)
		                                         {
		                                        	 
		                                        	 sucount=sucount+1;
		                                        	 m = 1;
		                                        	successVocharList.add(paymentVoucherNofromExcel);
		                                         }
												 
															
												}
												else
												{
												/*	paymentService.cahngeStatusOfPaymentThroughExcel(oldentry);
													if(successVocharList!=null && successVocharList.size()>0)
													{
														sucount=sucount-1;
														failcount=failcount+1;
														m = 2;
														successVocharList.remove(paymentVoucherNofromExcel);
														failureVocharList.add(paymentVoucherNofromExcel);
													}*/
												}
												
											}
										
										
										/*break;*/
									   }
								  
								 /* if(mainentryflag==true)
								  {	
									  break;
								  }*/
								  
									}
								
								
							if(flag==false)
							{
								if(!failureVocharList.contains(paymentVoucherNofromExcel))
								{
								 Boolean flag1 = false;
								 String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									entry.setIp_address(remoteAddr);
									entry.setCreated_by(user_id);
									Payment oldpayment = paymentService.isExcelVocherNumberExist(paymentVoucherNofromExcel, company_id);	
								
									if(oldpayment!=null && oldpayment.getExcel_voucher_no()!=null)
									{
									String str=oldpayment.getExcel_voucher_no().trim();
									if(str.replace(" ","").trim().equalsIgnoreCase(paymentVoucherNofromExcel.replace(" ","").trim())) {
										flag1=true;
									   /* break;*/
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
								}
								entry.setCompany(user.getCompany());
								
								if(row.getCell(1)==null)
								{
								
								}
								else
								{
									try {
										cnt=0;
										for(PurchaseEntry purchseEntry:purchaselist)
										{
												String str = purchseEntry.getSupplier_bill_no().replace(" ","").trim();
												if (str.replace(" ","").trim().equalsIgnoreCase(purchaseVoucherNofromExcel.replace(" ","").trim())) {
													entry.setSupplier_bill_no(purchseEntry);
													count = count + 1;
													cnt=1;
													purchseEntry1=purchseEntry;
													break;
										}
										}
										if(cnt==0){Err=true;ErrorMsgs.append(" Purchase voucher no is incorrect ");}
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
								
								
								
								entry.setLocal_time(new LocalTime());
								entry.setExcel_voucher_no(paymentVoucherNofromExcel);
								
								if(row.getCell(5)!=null)
								{
									
										
										 try {
											 String str=row.getCell(5).getDateCellValue().toString();
											
												entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvert(str)));

										   }
										   catch (Exception e) {
										    e.printStackTrace();
											  // System.out.println("datecell error");
										    }
										 try {
											 String str=row.getCell(5).getStringCellValue().toString().trim();
											 
												
													entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvertNew(str)));

											   }
											   catch (Exception e) {
											   e.printStackTrace();
												//   System.out.println("text error");
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
											entry.setVoucher_no(commonService.getVoucherNumber("PV", VOUCHER_PAYMENT, entry.getDate(),year_range.getYear_id(), company_id));	
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
								}else if(row.getCell(6)!=null){
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
										entry.setCheque_dd_no(chequeNofromExcel);
										}
										if(row.getCell(8)!=null)
										{
											  try {
													entry.setCheque_date(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(8).getDateCellValue().toString())));

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
											
										try {
											for(Bank bank:banklist)
											{
												
												String strbankname =bank.getBank_name().trim();
												String straccno = bank.getAccount_no().toString();
												
												
												if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno)) {
													entry.setBank(bank);
													bank1=bank;
													count=count+1;
												    break;
												}
											}
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
								if(row.getCell(31)!=null)
								{
								entry.setOther_remark(row.getCell(31).getStringCellValue().trim());
								}
								
								if((row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("NO")))
								{
									entry.setGst_applied(false);
								
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
										
										entry.setTds_paid(true);
									if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
									{
										if(supplier1.getTds_rate()!=null)
										{
										tds = ((Double)row.getCell(10).getNumericCellValue()*supplier1.getTds_rate())/100;
										}
									
									}
									entry.setTds_amount(round(tds,2));
									entry.setAmount(round((Double)row.getCell(10).getNumericCellValue()-tds,2));
									}
									
									if(row.getCell(29)==null)
									{
										
										entry.setTds_paid(true);
									if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
									{
										if(supplier1.getTds_rate()!=null)
										{
										tds = ((Double)row.getCell(10).getNumericCellValue()*supplier1.getTds_rate())/100;
										}
									
									}
									entry.setTds_amount(round(tds,2));
									entry.setAmount(round((Double)row.getCell(10).getNumericCellValue()-tds,2));
									}
								
									}
									
									if(row.getCell(12)!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO") && purchseEntry1!=null)
									{
										/**IF tds is already cut for purchase entry then we are adding payment against that purchase entry then tds value for particular payment is storing in database for showing tds against this payment in payment report & ledger report */
										Double tdsforPayment=0.0;
										entry.setAmount(round((Double)row.getCell(10).getNumericCellValue(),2));
										if(purchseEntry1.getTds_amount()!=null){	
										tds=(entry.getAmount()*purchseEntry1.getTds_amount())/purchseEntry1.getRound_off();
										}
										entry.setTds_amount(round(tdsforPayment,2));	
										entry.setTds_paid(false); /**here we are giving tds paid = false because tds is already deducted for purchase entry. this tds amount will required for report  payment report & ledger report .*/
										if(entry.getAmount()>purchseEntry1.getRound_off())
										{
											entry.setAdvance_payment(true);
											entry.setIs_purchasegenrated(true);
											entry.setIs_extraAdvanceReceived(true);
											
										}
										else
										{
											entry.setAdvance_payment(false);
										}
										
									}
								}
								
								if((row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("YES"))&&(subledger1==null))
								{
									entry.setGst_applied(true);
									String ProductName = row.getCell(14).getStringCellValue().trim();
									cnt=0;
									for(Suppliers supplier:list)
									{
										Boolean flag2 = false;
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
											    entity.setProduct_id(product);
											    entity.setProduct_name(product.getProduct_name());
											    entity.setHSNCode(finalhsncode);
												flag2=true;
												product1=product;
												count=count+1;
												cnt=1;
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
									
									} }else {break;}
									
									}
									
									if(cnt==0){Err=true;ErrorMsgs.append(" Product name is incorrect ");}
								if(row.getCell(15)!=null)
								{
								entity.setQuantity((int) row.getCell(15).getNumericCellValue());
								}else
								{
									entity.setQuantity((int) 0);
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
									entity.setCgst((Double) row.getCell(18).getNumericCellValue());
								}else
								{
									entity.setCgst((double)0);
								}

								
								if(row.getCell(19)!=null)
								{
									entity.setIgst((Double) row.getCell(19).getNumericCellValue());

								}else
								{
									entity.setIgst((double)0);
								}

								if(row.getCell(20)!=null)
								{
									entity.setSgst((Double) row.getCell(20).getNumericCellValue());

								}else
								{
									entity.setSgst((double)0);
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
									entity.setFrieght((Double) row.getCell(23).getNumericCellValue());

								}else
								{
									entity.setFrieght((double)0);
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
								try {
									for(UnitOfMeasurement uom:uomlist)
									{
										
										String str=uom.getUnit().trim();
										if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(25).getStringCellValue().replace(" ","").trim())) {
											entity.setUom_id(uom);
										    break;
										}
									}
								}
								catch(Exception e) {
									e.printStackTrace();
								}
								}
								
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

								
								cgst=entity.getCgst();
								igst=entity.getIgst();
								sgst=entity.getSgst();
								state_comp_tax= entity.getState_com_tax();
							    vat=entity.getVAT();
								vatcst=entity.getVATCST();
								excise=entity.getExcise();
							
								Double tamount=(double)0;
								Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers());
								Double damount = (entity.getRate()*entity.getQuantity());
								
								if(entity.getDiscount()!=0)
								{
									Double disamount =((round(damount,2))-((((round(damount,2))*(entity.getDiscount())))/100));
									tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers();
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
										if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
										{
											if(supplier1.getTds_rate()!=null)
											{
											tds = (transaction_value*supplier1.getTds_rate())/100;
											}
										
										}
										entry.setTds_amount(round(tds,2));
										entry.setRound_off(round(round_off,2));
										entry.setAmount(round(round_off,2));
										
									}
									
									if(row.getCell(29)!=null && row.getCell(29).getStringCellValue().trim().equalsIgnoreCase("NO"))
									{
										
										entry.setTds_paid(true);
										if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
										{
											if(supplier1.getTds_rate()!=null)
											{
											tds = (transaction_value*supplier1.getTds_rate())/100;
											}
										
										}
										entry.setTds_amount(round(tds,2));
										entry.setRound_off(round(round_off,2));
										entry.setAmount(round(round_off,2));
									}
									
									if(row.getCell(29)==null)
									{
										
										entry.setTds_paid(true);
										if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
										{
											if(supplier1.getTds_rate()!=null)
											{
											tds = (transaction_value*supplier1.getTds_rate())/100;
											}
										
										}
										entry.setTds_amount(round(tds,2));
										entry.setRound_off(round(round_off,2));
										entry.setAmount(round(round_off,2));
									}
									
								}
								else
								{
									entry.setTds_paid(false);
									entry.setTds_amount(round(tds,2));
									entry.setRound_off(round(round_off-tds,2));
									entry.setAmount(round(round_off-tds,2));
								}
							
								entity.setTransaction_amount(round(transaction_value,2));
								entry.setTransaction_value_head(round(transaction_value,2));
								entry.setTds_amount(round(tds,2));
								entry.setCGST_head(round(cgst,2));
								entry.setSGST_head(round(sgst,2));
								entry.setIGST_head(round(igst,2));
								entry.setSCT_head(round(state_comp_tax,2));
								entry.setTotal_vat(round(vat,2));
								entry.setTotal_vatcst(round(vatcst,2));
								entry.setTotal_excise(round(excise,2));
								}
								
								
								 if((product1!=null && entry.getAmount() !=null  && entry.getAmount()>0)&&(supplier1!=null)&&(entry.getVoucher_no()!=null)&&(subledger1==null)&&(row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("YES"))&&(row.getCell(12)!=null))
								    {
										if (((count == 5)&&(supplier1!=null && purchseEntry1!=null && bank1!=null && year_range1!=null && product1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))||
												((count == 4)&&(supplier1!=null && purchseEntry1!=null && year_range1!=null && product1!=null && entry.getPayment_type().equals(1)&& row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))
												||((count == 4)&&(supplier1!=null && bank1!=null && year_range1!=null && product1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))
												||((count == 3)&&(supplier1!=null && year_range1!=null && product1!=null && entry.getPayment_type().equals(1) &&row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))) 
								  {
									entry.setFlag(true);
									sucount=sucount+1;
									successVocharList.add(paymentVoucherNofromExcel);
									repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
									
									Long id1=null;
								    entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
									id1 = paymentService.savePaymentThroughExcel(entry);
									paymentService.savePaymentDetailsThroughExcel(entity, id1);
									Payment payment = paymentService.getById(id1);
								
								
								if(supplier1!=null)
								{
									try
									{
										suplliersService.addsuppliertrsansactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, supplier1.getSupplier_id(),
												(long) 4, (double)0, ((double)entry.getRound_off()+entry.getTds_amount()), (long) 1);
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}
								
								if(tds>0 && entry.getTds_paid()==true)
								{
									SubLedger subledgertds = subledgerDAO.findOne("TDS Payable", company_id);
								   if(subledgertds!=null)
							       {
									subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)entry.getTds_amount(), (double)0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
								   }
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
													subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)(transaction_value),(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
												}	
										 }
											
									 }
									 else
									 {
										 	if((transaction_value>0)&&(bank1!=null))
										 	{
										 		bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)(transaction_value),(double)0,(long) 1);
										 	}
										 	
									 }	 
									   
									   
									   if(cgst>0)
										{
											SubLedger subledgercgst = subledgerDAO.findOne("Input CGST",company_id);
											if(subledgercgst!=null)
											{
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)cgst,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
											}
										}
										if(sgst>0)
										{
											SubLedger subledgersgst = subledgerDAO.findOne("Input SGST",company_id);
											
											if(subledgersgst!=null)
											{
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)sgst,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
											}
										}
										if(igst>0)
										{
											SubLedger subledgerigst = subledgerDAO.findOne("Input IGST",company_id);
											
											if(subledgerigst!=null)
											{
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)igst,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
											}
										}
										if(state_comp_tax>0)
										{
											SubLedger subledgercess = subledgerDAO.findOne("Input CESS",company_id);
											
											if(subledgercess!=null)
											{
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)state_comp_tax,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
											}
										}
										
										if(vat>0)
										{
											SubLedger subledgervat = subledgerDAO.findOne("Input VAT",company_id);
											
											if(subledgervat!=null)
											{
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)vat,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
											}
										}
										
										if(vatcst>0)
										{
											SubLedger subledgercst = subledgerDAO.findOne("Input CST",company_id);
											
											if(subledgercst!=null)
											{
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)vatcst,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
											}
										}
										
										if(excise>0)
										{
											SubLedger subledgerexcise = subledgerDAO.findOne("Input EXCISE",company_id);
											
											if(subledgerexcise!=null)
											{
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)excise,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
											}
										}
								 }
								}
								catch(Exception e) {
									
									e.printStackTrace();
								}
									
								  } else {
										
									  if(!successVocharList.contains(paymentVoucherNofromExcel))
										{
				                        	maincount=maincount+1;
											failcount=failcount+1;
											
					                        failureVocharList.add(paymentVoucherNofromExcel);
					                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
										}
										
									}
								
					     		    
								
								    }
								 else if((product1==null && entry.getAmount() !=null && entry.getAmount()>0)&&(supplier1!=null)&&(entry.getVoucher_no()!=null) && (subledger1==null)&&(row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("NO"))&&(row.getCell(12)!=null))   
								    {
								    	if (((count == 2)&&(supplier1!=null && year_range1!=null && entry.getPayment_type().equals(1) && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))||
								    	    ((count == 3)&&(supplier1!=null && bank1!=null && year_range1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))||
								    		((count == 4)&&(supplier1!=null && purchseEntry1!=null && bank1!=null && year_range1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))||
								    		((count == 3)&&(supplier1!=null && purchseEntry1!=null && year_range1!=null && entry.getPayment_type().equals(1)&& row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))
								    		) 
								   {
									
									entry.setFlag(true);
									sucount=sucount+1;
									successVocharList.add(paymentVoucherNofromExcel);
									repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
									//Client wants the record to be editable always 
									/*if(row.getCell(12).getStringCellValue().equalsIgnoreCase("NO"))
									{
									if(entry.getAmount()>purchseEntry1.getRound_off())
									{
										 entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
									}
									else
									{
										 entry.setEntry_status(MyAbstractController.ENTRY_NIL);
									}
									}
									if(row.getCell(12).getStringCellValue().equalsIgnoreCase("YES"))
									{*/
										entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
									//}
									  Long id= paymentService.savePaymentThroughExcel(entry);
									   Payment payment1 = paymentService.getById(id);
									   
									   if(entry.getSupplier_bill_no() != null){				
											PurchaseEntry purchaseEntry = purchaseEntryDao.findOneWithAll(entry.getSupplier_bill_no().getPurchase_id());
											Double total =(double)0;
											for (Payment payment : purchaseEntry.getPayments()) {
												if(payment.getTds_amount()!= null && payment.getTds_paid()!=null && payment.getTds_paid()==true)
												total += payment.getAmount()+payment.getTds_amount(); 
												else
													total += payment.getAmount();
											}
											for (DebitNote note : purchaseEntry.getDebitNote()) {
												total += note.getRound_off();
										    }
											if(purchaseEntry.getRound_off().equals(total)) {
												purchaseEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
											}
											else if(total>=purchaseEntry.getRound_off())
											{
												purchaseEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
											}
											else{
												purchaseEntry.setEntry_status(MyAbstractController.ENTRY_PENDING);			
											}
											purchaseEntryDao.updatePurchaseEntry(purchaseEntry);
										}
									   
								 	if(supplier1!=null)
									{
										try
										{
											if(entry.getAdvance_payment()!=null && entry.getAdvance_payment()==true) {
											suplliersService.addsuppliertrsansactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, supplier1.getSupplier_id(),
													(long) 4, (double)0, (double)entry.getAmount()+entry.getTds_amount(), (long) 1);
									     	}
											else
											{
												suplliersService.addsuppliertrsansactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, supplier1.getSupplier_id(),
														(long) 4, (double)0, (double)entry.getAmount(), (long) 1);
											}
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
									}
								 	
								 	if(tds>0 && entry.getTds_paid()==true)
									{
										SubLedger subledgertds = subledgerDAO.findOne("TDS Payable", company_id);
									   if(subledgertds!=null)
								       {
										subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)tds, (double)0, (long) 1,null,null,null,payment1,null,null,null,null,null,null,null,null);
									   }
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
													System.out.println("subledger service");
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)entry.getAmount(),(double)0,(long) 1,null,null,null,payment1,null,null,null,null,null,null,null,null);
										
												}
										 }
										 else
											 {
											   if(bank1!=null)
										       {
											   bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)entry.getAmount(),(double)0,(long) 1);
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
											
								    		  if(!successVocharList.contains(paymentVoucherNofromExcel))
												{
						                        	maincount=maincount+1;
													failcount=failcount+1;
							                        failureVocharList.add(paymentVoucherNofromExcel);
							                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
												}
											
										}
							   
								
					      }
								else if(subledger1!=null && row.getCell(10)!=null && (Double)row.getCell(10).getNumericCellValue()!=null && supplier1==null && product1==null && entry.getVoucher_no()!=null)
								{
									
									entry.setAmount((Double)row.getCell(10).getNumericCellValue());
									entry.setTds_amount(0.0);
									if (((count == 2)&&(subledger1!=null && year_range1!=null))||((count == 3)&&(subledger1!=null && year_range1!=null && bank1!=null))) 
									{
										
										entry.setFlag(true);
										sucount=sucount+1;
										successVocharList.add(paymentVoucherNofromExcel);
										repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
										
										entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
										Long id = paymentService.savePaymentThroughExcel(entry);
										Payment payment1 = paymentService.getById(id);
										try
										{
										 if(subledger1 != null && row.getCell(10)!=null ){
											 
											 subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledger1.getSubledger_Id(),(long) 2,(double)0,(double)entry.getAmount(),(long) 1,null,null,null,payment1,null,null,null,null,null,null,null,null);
										 }
										 if (entry.getGst_applied().equals(false)) {
												if (entry.getPayment_type().equals(MyAbstractController.PAYMENT_TYPE_CASH)) {
													SubLedger subledgercgst = subledgerDAO.findOne("Cash In Hand", company_id);
													subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, subledgercgst.getSubledger_Id(), (long) 2, (double)entry.getAmount(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
												} 
												else if(entry.getBank()!=null){
													bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, entry.getBank().getBank_id(), (long) 3, (double)entry.getAmount(), (double) 0, (long) 1);
												}
											} 
										}
										 catch(Exception e)
											{
												e.printStackTrace();
											}
										
									} 
									else {
										
										  if(!successVocharList.contains(paymentVoucherNofromExcel))
											{
					                        	maincount=maincount+1;
												failcount=failcount+1;
						                        failureVocharList.add(paymentVoucherNofromExcel);
						                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											}
										
									}
									
									
									
								}
								else {
									
									
			                        if(!successVocharList.contains(paymentVoucherNofromExcel))
									{
			                        	maincount=maincount+1;
										failcount=failcount+1;
				                        failureVocharList.add(paymentVoucherNofromExcel);
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
						
						Payment entry = new Payment();
						PaymentDetails entity = new  PaymentDetails();
						
						Suppliers supplier1 =null;
						PurchaseEntry purchseEntry1=null;
						SubLedger subledger1= null;
						Bank bank1 = null;
						AccountingYear year_range1=null;
						Product product1=null;
						XSSFRow row = worksheet.getRow(i++);
						Err=false;
						;
						if(row.getLastCellNum()==0)
						{				
					    	System.out.println("Invalid Data");
						}
						else
						{	
							Double tds = (double)0;
							Double Newtds = (double)0;
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
							boolean flag = false ;
							boolean paymentType=true;
							String purchaseVoucherNofromExcel="";
							String paymentVoucherNofromExcel="";
							String chequeNofromExcel="";
							
						
							try
							{
								purchaseVoucherNofromExcel=row.getCell(1).getStringCellValue().trim().replaceAll(" ", "");;
								
							}
							catch(Exception e)
							{
								
							//	e.printStackTrace(); 
							}
							
							try
							{
								Double voucherNofromExcel= row.getCell(1).getNumericCellValue();
								Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
								purchaseVoucherNofromExcel=voucherNofromExcel1.toString().trim();
								
							}
							catch(Exception e)
							{
								//e.printStackTrace();
							
							}
							
							try
							{
								paymentVoucherNofromExcel=row.getCell(3).getStringCellValue().trim().replaceAll(" ", "");;
							}
							catch(Exception e)
							{
								//e.printStackTrace(); 
							}
							
							try
							{
								Double voucherNofromExcel= row.getCell(3).getNumericCellValue();
								Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
								paymentVoucherNofromExcel=voucherNofromExcel1.toString().trim();
								
								
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
							//	e.printStackTrace(); 
							}
							
							Payment oldentry = paymentService.isExcelVocherNumberExist(paymentVoucherNofromExcel, company_id);	
							
								if(oldentry!=null)
								{
									
									
									Set<PaymentDetails> paymentDetails = oldentry.getPaymentDetails();
									Boolean mainentryflag = false;
									String finalhsncode ="1";
									
									if(paymentDetails!=null && paymentDetails.size()!=0)
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
									
									for(PaymentDetails pD : paymentDetails)
									{
										if(finalhsncode.replace(" ","").trim().equalsIgnoreCase(pD.getHSNCode().replace(" ","").trim()) && ProductName.replace(" ","").trim().equalsIgnoreCase(pD.getProduct_name().replace(" ","").trim()))
										{
											entryflag=true;
										}
									}
									
									
									
									
									if((company_Id==company_id)&&(vocherNo.replace(" ","").trim().equalsIgnoreCase(paymentVoucherNofromExcel.replace(" ","").trim()))&&(entryflag==true))
									{
										mainentryflag=true;
									}
									}
									else
									{
										mainentryflag=true;
									}
									
							  if(mainentryflag==false)
							  {	
									flag = true;
									
										
										if(oldentry.getGst_applied()==true)
										{
											if(oldentry.getSupplier()!=null)
											{
											supplier1=oldentry.getSupplier();
											}
											
											transaction_value=oldentry.getTransaction_value_head();
											round_off=oldentry.getRound_off();
											cgst=oldentry.getCGST_head();
											igst=oldentry.getIGST_head();
											sgst= oldentry.getSGST_head();
											state_comp_tax=oldentry.getSCT_head();
											vat= oldentry.getTotal_vat();
											vatcst=oldentry.getTotal_vatcst();
											excise=oldentry.getTotal_excise();
											tds = oldentry.getTds_amount();
											String ProductName = row.getCell(14).getStringCellValue();
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
														entity.setProduct_id(product);
														entity.setProduct_name(product.getProduct_name());
														entity.setHSNCode(finalhsncode);
														flag1=true;
														product1=product;
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
											entity.setQuantity((int) row.getCell(15).getNumericCellValue());
											}else
											{
												entity.setQuantity((int) 0);
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
												entity.setCgst((Double) row.getCell(18).getNumericCellValue());
											}else
											{
												entity.setCgst((double)0);
											}

											
											if(row.getCell(19)!=null)
											{
												entity.setIgst((Double) row.getCell(19).getNumericCellValue());

											}else
											{
												entity.setIgst((double)0);
											}

											if(row.getCell(20)!=null)
											{
												entity.setSgst((Double) row.getCell(20).getNumericCellValue());

											}else
											{
												entity.setSgst((double)0);
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
												entity.setFrieght((Double) row.getCell(23).getNumericCellValue());

											}else
											{
												entity.setFrieght((double)0);
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
											try {
												for(UnitOfMeasurement uom:uomlist)
												{
													
													String str=uom.getUnit().trim();
													if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(25).getStringCellValue().replace(" ","").trim())) {
														entity.setUom_id(uom);
													    break;
													}
												}
											}
											catch(Exception e) {
												e.printStackTrace();
											}
											}
											
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

											
											cgst=cgst+entity.getCgst();
											igst=igst+entity.getIgst();
											sgst=sgst+entity.getSgst();
											state_comp_tax= state_comp_tax+entity.getState_com_tax();
											vat=vat+entity.getVAT();
											vatcst=vatcst+entity.getVATCST();
											excise=excise+entity.getExcise();
											
											    Double tamount=(double)0;
												Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers());
												Double damount = (entity.getRate()*entity.getQuantity());
												
												
												if(entity.getDiscount()!=0)
												{
													Double disamount =((round(damount,2))-((((round(damount,2))*(entity.getDiscount())))/100));
													tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers();
												}
												else
												{
													tamount = amount;
												}
											transaction_value=transaction_value+round(tamount,2);
											round_off=(round_off+tds)+entity.getCgst()+entity.getIgst()+entity.getSgst()+entity.getState_com_tax()+tamount ;
										
											if(oldentry.getAdvance_payment()!=null && oldentry.getAdvance_payment()==true && oldentry.getTds_paid()==true)
											{
											
												if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
												{
													if(supplier1.getTds_rate()!=null)
													{
													Newtds = (transaction_value*supplier1.getTds_rate())/100;
													}
													
													round_off=round_off-Newtds;
												}
													
											}
											else
											{
												oldentry.setTds_paid(false);
												oldentry.setTds_amount(Newtds);
											}
											
											
											
											if(product1!=null)
											{
											
											if ((oldentry.getSupplier() != null)) {
												suplliersService.addsuppliertrsansactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,
														oldentry.getSupplier().getSupplier_id(), (long) 4, (double)0, ((double)oldentry.getRound_off()+tds),
														(long) 0);
												suplliersService.addsuppliertrsansactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,
														oldentry.getSupplier().getSupplier_id(), (long) 4, (double)0, ((double)round_off+Newtds), (long) 1);
											} 
											
											
											if(oldentry.getAdvance_payment()!=null && oldentry.getAdvance_payment()==true)
											{
											if(oldentry.getTds_amount()!=null && oldentry.getTds_paid() !=null && oldentry.getTds_paid()==true)
											{
												  SubLedger subledgerTds = subledgerDAO.findOne("TDS Payable", company_id);
												  if (subledgerTds != null) {
													  subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id, subledgerTds.getSubledger_Id(), (long) 2,  (double)tds, (double)0, (long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
													  subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id, subledgerTds.getSubledger_Id(), (long) 2,  (double)Newtds, (double)0, (long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
												  }
											}
											}
											
											
											if (oldentry.getGst_applied()==true)
											 {
												 if(oldentry.getPayment_type()==1)
												 {
													 if((oldentry.getTransaction_value_head()!=null) && (oldentry.getTransaction_value_head()>0) && (transaction_value>0) && (oldentry.getTransaction_value_head()!=transaction_value))
													 {
														
														if(transaction_value>0)
														 {
															 SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",company_id);
																
																if(subledgercash!=null)
																{
																	subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)(oldentry.getTransaction_value_head()),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																	subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)(transaction_value),(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
																}	
														 }
														
													 }
														
												 }
												 else
												 {
													 if((oldentry.getTransaction_value_head()!=null) && (oldentry.getTransaction_value_head()>0)&& (transaction_value>0) && (oldentry.getTransaction_value_head()!=transaction_value))
													    {
														 if(oldentry.getBank()!=null)
														 {
														 bank1=oldentry.getBank();
														 bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)(oldentry.getTransaction_value_head()),(double)0,(long) 0);
														 bankService.addbanktransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)(transaction_value),(double)0,(long) 1);
														 }
													 	}
													 
													 
													
												 }	
												 if((oldentry.getCGST_head()!=null)&&(oldentry.getCGST_head()>0)&&(cgst>0)&&(oldentry.getCGST_head()!=cgst))
													{
														SubLedger subledgercgst = subledgerDAO.findOne("Input CGST",company_id);
														if(subledgercgst!=null)
														{
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)oldentry.getCGST_head(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)cgst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getCGST_head()!=null)&&(cgst>0) && (oldentry.getCGST_head()==0))
													{
														SubLedger subledgercgst = subledgerDAO.findOne("Input CGST",company_id);
														if(subledgercgst!=null)
														{
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)cgst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													if((oldentry.getSGST_head()!=null)&&(oldentry.getSGST_head()>0)&&(sgst>0)&&(oldentry.getSGST_head()!=sgst))
													{
														SubLedger subledgersgst = subledgerDAO.findOne("Input SGST",company_id);
														
														if(subledgersgst!=null)
														{
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)oldentry.getSGST_head(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)sgst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getSGST_head()!=null)&&(sgst>0)&&(oldentry.getSGST_head()==0))
													{
	                                                     SubLedger subledgersgst = subledgerDAO.findOne("Input SGST",company_id);
														
														if(subledgersgst!=null)
														{
													
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)sgst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
														
													}
													
													if((oldentry.getIGST_head()!=null) && (oldentry.getIGST_head()>0) && (igst>0) && (oldentry.getIGST_head()!=igst))
													{
														SubLedger subledgerigst = subledgerDAO.findOne("Input IGST",company_id);
														
														if(subledgerigst!=null)
														{
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)oldentry.getIGST_head(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)igst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getIGST_head()!=null)&&(igst>0)&&(oldentry.getIGST_head()==0))
													{
	                                                    SubLedger subledgerigst = subledgerDAO.findOne("Input IGST",company_id);
														
														if(subledgerigst!=null)
														{
													
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)igst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													
													if((oldentry.getSCT_head()!=null) && (oldentry.getSCT_head()>0) && (state_comp_tax>0)&&(oldentry.getSCT_head()!=state_comp_tax))
													{
														SubLedger subledgercess = subledgerDAO.findOne("Input CESS",company_id);
														
														if(subledgercess!=null)
														{
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)oldentry.getSCT_head(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)state_comp_tax,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getSCT_head()!=null) && (state_comp_tax>0) && (oldentry.getSCT_head()==0))
													{
	                                                    SubLedger subledgercess = subledgerDAO.findOne("Input CESS",company_id);
														
														if(subledgercess!=null)
														{
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)state_comp_tax,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													
													if((oldentry.getTotal_vat()!=null) && (oldentry.getTotal_vat()>0) && (vat>0)&&(oldentry.getTotal_vat()!=vat))
													{
														SubLedger subledgervat = subledgerDAO.findOne("Input VAT",company_id);
														
														if(subledgervat!=null)
														{
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)oldentry.getTotal_vat(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)vat,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getTotal_vat()!=null) && (vat>0) && (oldentry.getTotal_vat()==0))
													{
	                                                     SubLedger subledgervat = subledgerDAO.findOne("Input VAT",company_id);
														
														if(subledgervat!=null)
														{
				
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)vat,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													
													if((oldentry.getTotal_vatcst()!=null) && (oldentry.getTotal_vatcst()>0) && (vatcst>0)&&(oldentry.getTotal_vatcst()!=vatcst))
													{
														SubLedger subledgercst = subledgerDAO.findOne("Input CST",company_id);
														
														if(subledgercst!=null)
														{subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)oldentry.getTotal_vatcst(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)vatcst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getTotal_vatcst()!=null) && (vatcst>0) && (oldentry.getTotal_vatcst()==0))
													{
	                                                    SubLedger subledgercst = subledgerDAO.findOne("Input CST",company_id);
														
														if(subledgercst!=null)
														{
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)vatcst,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													
													if((oldentry.getTotal_excise()!=null) && (oldentry.getTotal_excise()>0) && (excise>0)&&(oldentry.getTotal_excise()!=excise))
													{
														SubLedger subledgerexcise = subledgerDAO.findOne("Input EXCISE",company_id);
														
														if(subledgerexcise!=null)
														{subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)oldentry.getTotal_excise(),(double)0,(long) 0,null,null,null,oldentry,null,null,null,null,null,null,null,null);
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)excise,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
													else if((oldentry.getTotal_excise()!=null) && excise>0 && (oldentry.getTotal_excise()==0))
													{
	                                                    SubLedger subledgerexcise = subledgerDAO.findOne("Input EXCISE",company_id);
														
														if(subledgerexcise!=null)
														{
															subLedgerService.addsubledgertransactionbalance(oldentry.getAccountingYear().getYear_id(),oldentry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)excise,(double)0,(long) 1,null,null,null,oldentry,null,null,null,null,null,null,null,null);
														}
													}
											 }
											
											entity.setTransaction_amount(round(tamount,2));
											oldentry.setRound_off(round(round_off,2));
											oldentry.setAmount(round(round_off,2));
											oldentry.setTds_amount(round(Newtds,2));
											oldentry.setTransaction_value_head(round(transaction_value,2));
											oldentry.setCGST_head(round(cgst,2));
											oldentry.setSGST_head(round(sgst,2));
											oldentry.setIGST_head(round(igst,2));
											oldentry.setSCT_head(round(state_comp_tax,2));
											oldentry.setTotal_vat(round(vat,2));
											oldentry.setTotal_vatcst(round(vatcst,2));
											oldentry.setTotal_excise(round(excise,2));
											
											
											paymentService.updatePaymentThroughExcel(oldentry);
											paymentService.updatePaymentDetailsThroughExcel(entity, oldentry.getPayment_id());
											 if(repetProductafterunsuccesfullattempt==0)
	                                         {
	                                        	 
	                                        	 sucount=sucount+1;
	                                        	 m = 1;
	                                        	successVocharList.add(paymentVoucherNofromExcel);
	                                         }
											 
														
											}
											else
											{
											/*	paymentService.cahngeStatusOfPaymentThroughExcel(oldentry);
												if(successVocharList!=null && successVocharList.size()>0)
												{
													sucount=sucount-1;
													failcount=failcount+1;
													m = 2;
													successVocharList.remove(paymentVoucherNofromExcel);
													failureVocharList.add(paymentVoucherNofromExcel);
												}*/
											}
											
										}
									
									
									/*break;*/
								   }
							  
							 /* if(mainentryflag==true)
							  {	
								  break;
							  }*/
							  
								}
							
							
						if(flag==false)
						{
							
							if(!failureVocharList.contains(paymentVoucherNofromExcel))
							{
								
							 Boolean flag1 = false;
							 String remoteAddr = "";
								remoteAddr = request.getHeader("X-FORWARDED-FOR");
								if (remoteAddr == null || "".equals(remoteAddr)) {
									remoteAddr = request.getRemoteAddr();
								}
								entry.setIp_address(remoteAddr);
								entry.setCreated_by(user_id);
								Payment oldpayment = paymentService.isExcelVocherNumberExist(paymentVoucherNofromExcel, company_id);	
							
								if(oldpayment!=null && oldpayment.getExcel_voucher_no()!=null)
								{
								String str=oldpayment.getExcel_voucher_no().trim();
								if(str.replace(" ","").trim().equalsIgnoreCase(paymentVoucherNofromExcel.replace(" ","").trim())) {
									flag1=true;
								   /* break;*/
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
							}
							entry.setCompany(user.getCompany());
							
							if(row.getCell(1)==null)
							{
							
							}
							else
							{
								try {
									cnt=0;
									for(PurchaseEntry purchseEntry:purchaselist)
									{
										
											String str = purchseEntry.getSupplier_bill_no().replace(" ","").trim();
											if (str.replace(" ","").trim().equalsIgnoreCase(purchaseVoucherNofromExcel.replace(" ","").trim())) {
												entry.setSupplier_bill_no(purchseEntry);
												
												count = count + 1;
												cnt=1;
												purchseEntry1=purchseEntry;
												break;
									}
									}
									if(cnt==0){Err=true;ErrorMsgs.append(" Supplier Bill no is incorrect ");}
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
							
							
							
							entry.setLocal_time(new LocalTime());
							entry.setExcel_voucher_no(paymentVoucherNofromExcel);
							
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
									// if(row.getCell(5).ce)
									 String str=row.getCell(5).getStringCellValue().toString();
									 
										
											entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvertNew(str)));

									   }
									   catch (Exception e) {
									   e.printStackTrace();
										   System.out.println("text error");
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
											
										entry.setVoucher_no(commonService.getVoucherNumber("PV", VOUCHER_PAYMENT, entry.getDate(),year_range.getYear_id(), company_id));	
										}
									    break;
									}
								}
								if(cnt==0){Err=true;ErrorMsgs.append(" Accounting Year is incorrect ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
							if(row.getCell(6)!=null )
							{
								String cellval=row.getCell(6).getStringCellValue().trim();
								System.out.println("not null row 6 is" + row.getCell(6).getStringCellValue());
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
							}else if(row.getCell(6)!=null){
								
								entry.setPayment_type(4);
							}
							}else if(row.getCell(6) ==null){
								
								Err=true;ErrorMsgs.append(" Payment Type cannot be blank ");
								paymentType=false;
							}
							} catch (Exception e) {
								e.printStackTrace();
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
									entry.setCheque_dd_no(chequeNofromExcel);
									}
									if(row.getCell(8)!=null)
									{
										  try {
												entry.setCheque_date(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(8).getDateCellValue().toString())));

										   }
										   catch (Exception e) {
										    e.printStackTrace();
										    }
									}
									if(row.getCell(9)==null){invalidData=invalidData+"\n" + "Bank name is blank"; String[] banknameAndAcc = row.getCell(9).getStringCellValue().replace(" ","").trim().split("-");
									}
									if(row.getCell(9)!=null )
									{
										
										
										String[] banknameAndAcc = row.getCell(9).getStringCellValue().replace(" ","").trim().split("-");
										if(banknameAndAcc.length<2){invalidData=invalidData + "\n" +"Bank name is incorrect or blank";}
										System.out.println ("banknameandacc is " +banknameAndAcc.length);
										String bankname = banknameAndAcc[0];
										String accno = banknameAndAcc[1];
										
									try {
										cnt=0;
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
							if(row.getCell(31)!=null)
							{
							entry.setOther_remark(row.getCell(31).getStringCellValue().trim());
							}
							
							if((row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("NO")))
							{
								entry.setGst_applied(false);
							
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
									
									entry.setTds_paid(true);
								if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
								{
									if(supplier1.getTds_rate()!=null)
									{
									tds = ((Double)row.getCell(10).getNumericCellValue()*supplier1.getTds_rate())/100;
									}
								
								}
								
								entry.setTds_amount(round(tds,2));
								entry.setAmount(round((Double)row.getCell(10).getNumericCellValue()-tds,2));
								}
								
								if(row.getCell(29)==null)
								{
									
									entry.setTds_paid(true);
								if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
								{
									if(supplier1.getTds_rate()!=null)
									{
									tds = ((Double)row.getCell(10).getNumericCellValue()*supplier1.getTds_rate())/100;
									}
								
								}
								entry.setTds_amount(round(tds,2));
								entry.setAmount(round((Double)row.getCell(10).getNumericCellValue()-tds,2));
								}
							
								}
								
								if(row.getCell(12)!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO") && purchseEntry1!=null)
								{
									/**IF tds is already cut for purchase entry then we are adding payment against that purchase entry then tds value for particular payment is storing in database for showing tds against this payment in payment report & ledger report */
									Double tdsforPayment=0.0;
									
									entry.setAmount(round((Double)row.getCell(10).getNumericCellValue(),2));
									if(purchseEntry1.getTds_amount()!=null){	
									tds=(entry.getAmount()*purchseEntry1.getTds_amount())/purchseEntry1.getRound_off();
									}
									entry.setTds_amount(round(tdsforPayment,2));	
									entry.setTds_paid(false); /**here we are giving tds paid = false because tds is already deducted for purchase entry. this tds amount will required for report  payment report & ledger report .*/
									if(entry.getAmount()>purchseEntry1.getRound_off())
									{
										entry.setAdvance_payment(true);
										entry.setIs_purchasegenrated(true);
										entry.setIs_extraAdvanceReceived(true);
										
									}
									else
									{
										entry.setAdvance_payment(false);
									}
									
								}
							}
							
							if((row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("YES"))&&(subledger1==null))
							{
								entry.setGst_applied(true);
								String ProductName = row.getCell(14).getStringCellValue();
								
								for(Suppliers supplier:list)
								{
									Boolean flag2 = false;
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
										    entity.setProduct_id(product);
										    entity.setProduct_name(product.getProduct_name());
										    entity.setHSNCode(finalhsncode);
											flag2=true;
											product1=product;
											count=count+1;
											cnt=1;
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
								if(cnt==0 && product1!=null){Err=true;ErrorMsgs.append(" Product name is incorrect ");}
									
							if(row.getCell(15)!=null)
							{
							entity.setQuantity((int) row.getCell(15).getNumericCellValue());
							}else
							{
								entity.setQuantity((int) 0);
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
								entity.setCgst((Double) row.getCell(18).getNumericCellValue());
							}else
							{
								entity.setCgst((double)0);
							}

							
							if(row.getCell(19)!=null)
							{
								entity.setIgst((Double) row.getCell(19).getNumericCellValue());

							}else
							{
								entity.setIgst((double)0);
							}

							if(row.getCell(20)!=null)
							{
								entity.setSgst((Double) row.getCell(20).getNumericCellValue());

							}else
							{
								entity.setSgst((double)0);
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
								entity.setFrieght((Double) row.getCell(23).getNumericCellValue());

							}else
							{
								entity.setFrieght((double)0);
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
							try {
								for(UnitOfMeasurement uom:uomlist)
								{
									
									String str=uom.getUnit().trim();
									if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(25).getStringCellValue().replace(" ","").trim())) {
										entity.setUom_id(uom);
									    break;
									}
								}
							}
							catch(Exception e) {
								e.printStackTrace();
							}
							}
							
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

							
							cgst=entity.getCgst();
							igst=entity.getIgst();
							sgst=entity.getSgst();
							state_comp_tax= entity.getState_com_tax();
						    vat=entity.getVAT();
							vatcst=entity.getVATCST();
							excise=entity.getExcise();
						
							Double tamount=(double)0;
							Double amount = ((entity.getRate()*entity.getQuantity())+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers());
							Double damount = (entity.getRate()*entity.getQuantity());
							
							if(entity.getDiscount()!=0)
							{
								Double disamount =((round(damount,2))-((((round(damount,2))*(entity.getDiscount())))/100));
								tamount=(round(disamount,2))+entity.getLabour_charges()+entity.getFrieght()+entity.getOthers();
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
									if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
									{
										if(supplier1.getTds_rate()!=null)
										{
										tds = (transaction_value*supplier1.getTds_rate())/100;
										}
									
									}
									entry.setTds_amount(round(tds,2));
									entry.setRound_off(round(round_off,2));
									entry.setAmount(round(round_off,2));
									
								}
								
								if(row.getCell(29)!=null && row.getCell(29).getStringCellValue().trim().equalsIgnoreCase("NO"))
								{
									
									entry.setTds_paid(true);
									if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
									{
										if(supplier1.getTds_rate()!=null)
										{
										tds = (transaction_value*supplier1.getTds_rate())/100;
										}
									
									}
									entry.setTds_amount(round(tds,2));
									entry.setRound_off(round(round_off,2));
									entry.setAmount(round(round_off,2));
								}
								
								if(row.getCell(29)==null)
								{
									
									entry.setTds_paid(true);
									if(supplier1!=null && (supplier1.getTds_applicable()!=null && supplier1.getTds_applicable()==1))
									{
										if(supplier1.getTds_rate()!=null)
										{
										tds = (transaction_value*supplier1.getTds_rate())/100;
										}
									
									}
									entry.setTds_amount(round(tds,2));
									entry.setRound_off(round(round_off,2));
									entry.setAmount(round(round_off,2));
								}
								
							}
							else
							{
								entry.setTds_paid(false);
								entry.setTds_amount(round(tds,2));
								entry.setRound_off(round(round_off-tds,2));
								entry.setAmount(round(round_off-tds,2));
							}
						
							entity.setTransaction_amount(round(transaction_value,2));
							entry.setTransaction_value_head(round(transaction_value,2));
							entry.setTds_amount(round(tds,2));
							entry.setCGST_head(round(cgst,2));
							entry.setSGST_head(round(sgst,2));
							entry.setIGST_head(round(igst,2));
							entry.setSCT_head(round(state_comp_tax,2));
							entry.setTotal_vat(round(vat,2));
							entry.setTotal_vatcst(round(vatcst,2));
							entry.setTotal_excise(round(excise,2));
							}
							
							
							
							
							 if((product1!=null && entry.getAmount() !=null  && entry.getAmount()>0)&&(supplier1!=null)&&(entry.getVoucher_no()!=null)&&(subledger1==null)&&(row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("YES"))&&(row.getCell(12)!=null) &&(paymentType==true) )
							    {
								
									if (((count == 5)&&(supplier1!=null && purchseEntry1!=null && bank1!=null && year_range1!=null && product1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))||
											((count == 4)&&(supplier1!=null && purchseEntry1!=null && year_range1!=null && product1!=null && entry.getPayment_type().equals(1)&& row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))
											||((count == 4)&&(supplier1!=null && bank1!=null && year_range1!=null && product1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))
											||((count == 3)&&(supplier1!=null && year_range1!=null && product1!=null && entry.getPayment_type().equals(1) &&row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))) 
							  {
									
								entry.setFlag(true);
								sucount=sucount+1;
								successVocharList.add(paymentVoucherNofromExcel);
								repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
								
								Long id1=null;
							    entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
								id1 = paymentService.savePaymentThroughExcel(entry);
								paymentService.savePaymentDetailsThroughExcel(entity, id1);
								Payment payment = paymentService.getById(id1);
							
							
							if(supplier1!=null)
							{
								try
								{
									suplliersService.addsuppliertrsansactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, supplier1.getSupplier_id(),
											(long) 4, (double)0, ((double)entry.getRound_off()+entry.getTds_amount()), (long) 1);
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							
							if(tds>0 && entry.getTds_paid()==true)
							{
								SubLedger subledgertds = subledgerDAO.findOne("TDS Payable", company_id);
							   if(subledgertds!=null)
						       {
								subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)entry.getTds_amount(), (double)0, (long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
							   }
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
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)(transaction_value),(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
											}	
									 }
										
								 }
								 else
								 {
									 	if((transaction_value>0)&&(bank1!=null))
									 	{
									 		bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)(transaction_value),(double)0,(long) 1);
									 	}
									 	
								 }	 
								   
								   
								   if(cgst>0)
									{
										SubLedger subledgercgst = subledgerDAO.findOne("Input CGST",company_id);
										if(subledgercgst!=null)
										{
											subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)cgst,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
										}
									}
									if(sgst>0)
									{
										SubLedger subledgersgst = subledgerDAO.findOne("Input SGST",company_id);
										
										if(subledgersgst!=null)
										{
											subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)sgst,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
										}
									}
									if(igst>0)
									{
										SubLedger subledgerigst = subledgerDAO.findOne("Input IGST",company_id);
										
										if(subledgerigst!=null)
										{
											subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)igst,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
										}
									}
									if(state_comp_tax>0)
									{
										SubLedger subledgercess = subledgerDAO.findOne("Input CESS",company_id);
										
										if(subledgercess!=null)
										{
											subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)state_comp_tax,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
										}
									}
									
									if(vat>0)
									{
										SubLedger subledgervat = subledgerDAO.findOne("Input VAT",company_id);
										
										if(subledgervat!=null)
										{
											subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)vat,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
										}
									}
									
									if(vatcst>0)
									{
										SubLedger subledgercst = subledgerDAO.findOne("Input CST",company_id);
										
										if(subledgercst!=null)
										{
											subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)vatcst,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
										}
									}
									
									if(excise>0)
									{
										SubLedger subledgerexcise = subledgerDAO.findOne("Input EXCISE",company_id);
										
										if(subledgerexcise!=null)
										{
											subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)excise,(double)0,(long) 1,null,null,null,payment,null,null,null,null,null,null,null,null);
										}
									}
							 }
							}
							catch(Exception e) {
								
								e.printStackTrace();
							}
								
							  } else {
									
								  if(!successVocharList.contains(paymentVoucherNofromExcel))
									{
									 
			                        	maincount=maincount+1;
										failcount=failcount+1;
				                        failureVocharList.add(paymentVoucherNofromExcel);
				                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
									}
									
								}
							
				     		    
							
							    }
							 else if((product1==null && entry.getAmount() !=null && entry.getAmount()>0)&&(supplier1!=null)&&(entry.getVoucher_no()!=null) && (subledger1==null)&&(row.getCell(11)!=null)&&(row.getCell(11).getStringCellValue().trim().equalsIgnoreCase("NO"))&&(row.getCell(12)!=null) &&(paymentType==true))   
							    {
								
							    	if (((count == 2)&&(supplier1!=null && year_range1!=null && entry.getPayment_type().equals(1) && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))||
							    	    ((count == 3)&&(supplier1!=null && bank1!=null && year_range1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("YES")))||
							    		((count == 4)&&(supplier1!=null && purchseEntry1!=null && bank1!=null && year_range1!=null && row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))||
							    		((count == 3)&&(supplier1!=null && purchseEntry1!=null && year_range1!=null && entry.getPayment_type().equals(1)&& row.getCell(12).getStringCellValue().trim().equalsIgnoreCase("NO")))
							    		) 
							   {
							    		
								entry.setFlag(true);
								sucount=sucount+1;
								successVocharList.add(paymentVoucherNofromExcel);
								repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
								//Client wants the record to be editable always 
								/*if(row.getCell(12).getStringCellValue().equalsIgnoreCase("NO"))
								{
								if(entry.getAmount()>purchseEntry1.getRound_off())
								{
									 entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
								}
								else
								{
									 entry.setEntry_status(MyAbstractController.ENTRY_NIL);
								}
								}
								if(row.getCell(12).getStringCellValue().equalsIgnoreCase("YES"))
								{*/
									entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
								//}
									
								  Long id= paymentService.savePaymentThroughExcel(entry);
								   Payment payment1 = paymentService.getById(id);
								   
								   if(entry.getSupplier_bill_no() != null){				
										PurchaseEntry purchaseEntry = purchaseEntryDao.findOneWithAll(entry.getSupplier_bill_no().getPurchase_id());
										Double total =(double)0;
										for (Payment payment : purchaseEntry.getPayments()) {
											if(payment.getTds_amount()!= null && payment.getTds_paid()!=null && payment.getTds_paid()==true)
											total += payment.getAmount()+payment.getTds_amount(); 
											else
												total += payment.getAmount();
										}
										for (DebitNote note : purchaseEntry.getDebitNote()) {
											total += note.getRound_off();
									    }
										if(purchaseEntry.getRound_off().equals(total)) {
											purchaseEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
										}
										else if(total>=purchaseEntry.getRound_off())
										{
											purchaseEntry.setEntry_status(MyAbstractController.ENTRY_NIL);
										}
										else{
											purchaseEntry.setEntry_status(MyAbstractController.ENTRY_PENDING);			
										}
										purchaseEntryDao.updatePurchaseEntry(purchaseEntry);
									}
								   
							 	if(supplier1!=null)
								{
							 		
									try
									{
										if(entry.getAdvance_payment()!=null && entry.getAdvance_payment()==true) {
										suplliersService.addsuppliertrsansactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, supplier1.getSupplier_id(),
												(long) 4, (double)0, (double)entry.getAmount()+entry.getTds_amount(), (long) 1);
								     	}
										else
										{
											suplliersService.addsuppliertrsansactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, supplier1.getSupplier_id(),
													(long) 4, (double)0, (double)entry.getAmount(), (long) 1);
										}
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}
							 	
							 	if(tds>0 && entry.getTds_paid()==true)
								{
									SubLedger subledgertds = subledgerDAO.findOne("TDS Payable", company_id);
								   if(subledgertds!=null)
							       {
									subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, subledgertds.getSubledger_Id(), (long) 2, (double)tds, (double)0, (long) 1,null,null,null,payment1,null,null,null,null,null,null,null,null);
								   }
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
											
											subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)entry.getAmount(),(double)0,(long) 1,null,null,null,payment1,null,null,null,null,null,null,null,null);
									
											}
									 }
									 else
										 {
										   if(bank1!=null)
									       {
										   bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,bank1.getBank_id(),(long) 3,(double)entry.getAmount(),(double)0,(long) 1);
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
										
							    		  if(!successVocharList.contains(paymentVoucherNofromExcel))
											{
							    			  
					                        	maincount=maincount+1;
												failcount=failcount+1;
						                        failureVocharList.add(paymentVoucherNofromExcel);
						                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
											}
										
									}
						   
							
				      }
							else if(subledger1!=null && row.getCell(10)!=null && (Double)row.getCell(10).getNumericCellValue()!=null && supplier1==null && product1==null && entry.getVoucher_no()!=null && paymentType==true)
							{
								
								entry.setAmount((Double)row.getCell(10).getNumericCellValue());
								entry.setTds_amount(0.0);
								
								if (((count == 2)&&(subledger1!=null && year_range1!=null))||((count == 3)&&(subledger1!=null && year_range1!=null && bank1!=null))) 
								{
									
									entry.setFlag(true);
									sucount=sucount+1;
									successVocharList.add(paymentVoucherNofromExcel);
									repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
									
									entry.setEntry_status(MyAbstractController.ENTRY_PENDING);
									Long id = paymentService.savePaymentThroughExcel(entry);
									Payment payment1 = paymentService.getById(id);
									try
									{
									 if(subledger1 != null && row.getCell(10)!=null ){
										 
										 subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id,subledger1.getSubledger_Id(),(long) 2,(double)0,(double)entry.getAmount(),(long) 1,null,null,null,payment1,null,null,null,null,null,null,null,null);
									 }
									 if (entry.getGst_applied().equals(false)) {
											if (entry.getPayment_type().equals(MyAbstractController.PAYMENT_TYPE_CASH)) {
												SubLedger subledgercgst = subledgerDAO.findOne("Cash In Hand", company_id);
												subLedgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, subledgercgst.getSubledger_Id(), (long) 2, (double)entry.getAmount(), (double) 0, (long) 1,null,null,null,entry,null,null,null,null,null,null,null,null);
											} 
											else if(entry.getBank()!=null){
												bankService.addbanktransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),company_id, entry.getBank().getBank_id(), (long) 3, (double)entry.getAmount(), (double) 0, (long) 1);
											}
										} 
									}
									 catch(Exception e)
										{
											e.printStackTrace();
										}
									
								} 
								else {
									
									  if(!successVocharList.contains(paymentVoucherNofromExcel))
										{
										  
				                        	maincount=maincount+1;
											failcount=failcount+1;
					                        failureVocharList.add(paymentVoucherNofromExcel);
					                        repetProductafterunsuccesfullattempt=repetProductafterunsuccesfullattempt+1;
										}
									
								}
								
								
								
							}
							else {
								
								
		                        if(!successVocharList.contains(paymentVoucherNofromExcel))
								{
		                        	
		                        	maincount=maincount+1;
									failcount=failcount+1;
			                        failureVocharList.add(paymentVoucherNofromExcel);
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

try {
	
	String filePath = request.getServletContext().getRealPath("resources");
	filePath += "/templates/ErrorFile.txt";
	
	System.out.println("File Path is "+filePath +ErrorMsgs.toString() );
	File file = new File(   filePath); 
	 
	 FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(ErrorMsgs.toString());
        bw.close();
        System.out.println("done");
			        
} catch ( Exception e ) {
    e.printStackTrace();
}
		if (isValid) {

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
				return new ModelAndView("redirect:/paymentList");
				
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
				return new ModelAndView("redirect:/paymentList");
			}
			else
			{
				successmsg.append("You are inserting duplicate records, please check excel file");
				String successmsg1=successmsg.toString();
				session.setAttribute("filemsg", successmsg1);
				return new ModelAndView("redirect:/paymentList");
			}
			
		} else {
			successmsg.append("Please enter required and valid data in columns");
			
			
				if(!invalidData.trim().equals("")){
					
					successmsg.append(invalidData);
			}
			
			 String successmsg1=successmsg.toString();
			session.setAttribute("filemsg", successmsg1);
			return new ModelAndView("redirect:/paymentList");

		}
	}	
	
	
	@RequestMapping(value = "paymentFailure", method = RequestMethod.GET)
	public ModelAndView paymentFailure(HttpServletRequest request, HttpServletResponse response) {
	    
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
		flag = false;
		List<YearEnding>  yearEndlist = yearService.findAllYearEnding(company_id);
		
		 if((String) session.getAttribute("msg")!=null){
				model.addObject("successMsg", (String) session.getAttribute("msg"));
				session.removeAttribute("msg");
			}
		 
		//model.addObject("paymentList", paymentService.findAllPaymentsOfCompany(company_id, false));
		model.addObject("flagFailureList", false);
		model.addObject("yearEndlist", yearEndlist);
		model.setViewName("/transactions/paymentList");
		return model;
		}
	
	
	 
	 @RequestMapping(value ="editproductdetailforPayment", method = RequestMethod.GET)
	 public ModelAndView editproductdetailforPayment(@RequestParam("id") long id, 
				HttpServletRequest request, 
				HttpServletResponse response){

		 ModelAndView model = new ModelAndView();
		 HttpSession session = request.getSession(true);
		 GstTaxMaster productgst =new GstTaxMaster();
		 try {
			 long company_id=(long)session.getAttribute("company_id");
			 Company company = companyService.getById(company_id);
			 PaymentDetails paymentDetails = new PaymentDetails();			
			 paymentDetails= paymentService.getPaymentDetailById(id);
			 Payment payment = paymentService.findOneWithAll(paymentDetails.getPayment_id().getPayment_id());
			 paymentDetails.setPaymentId(paymentDetails.getPayment_id().getPayment_id());
			 paymentDetails.setProductId(paymentDetails.getProduct_id().getProduct_id());
			 
			 if(paymentDetails.getProduct_id().getHsn_san_no()!=null)
				{
				 productgst=gstService.getHSNbyDate(payment.getDate(),paymentDetails.getProduct_id().getHsn_san_no());
				}
			 else
				{
					productgst=gstService.getHSNbyDate(payment.getDate(),paymentDetails.getHSNCode());
				}
			 model.addObject("paymentDetails", paymentDetails);
			 model.addObject("productgst", productgst);
			 model.addObject("compState", company.getState().getState_id());
			 model.addObject("supState", payment.getSupplier().getState().getState_id());
			 model.addObject("payment", payment);
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
		 model.setViewName("/transactions/editproductdetailforPayment");
		 return model;		
	 }
	 
	 @RequestMapping(value ="saveproductdetailforPayment", method = RequestMethod.POST)
	 public synchronized ModelAndView saveproductdetailforPayment(@ModelAttribute("paymentDetails")PaymentDetails paymentDetails, 
				BindingResult result, 
				HttpServletRequest request, 
				HttpServletResponse response){
	System.out.println("1");
		ModelAndView model = new ModelAndView();
		try {
			paymentDetails.setProduct_id(productService.getById(paymentDetails.getProductId()));
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		paymentDetailValidator.validate(paymentDetails, result);
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		try {
			if(result.hasErrors()) {
				Company company = companyService.getById(company_id);
				Payment payment = paymentService.findOneWithAll(paymentDetails.getPaymentId());
			
				model.addObject("supState", payment.getSupplier().getState().getState_id());
				model.setViewName("/transactions/editproductdetailforPayment");
			}
			else {
				paymentService.updatePaymentDetail(paymentDetails, company_id);
				model.setViewName("redirect:/editPayment?id="+paymentDetails.getPaymentId());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return model;		
	 }
	 
	 @RequestMapping(value ="deleteproductdetailforPayment", method = RequestMethod.GET)
	 public synchronized ModelAndView deleteproductdetailforPayment(@RequestParam("id") long id, 
				HttpServletRequest request, 
				HttpServletResponse response){
		 ModelAndView model = new ModelAndView();
		 HttpSession session = request.getSession(true);
		 long company_id=(long)session.getAttribute("company_id");
		 
		 PaymentDetails payDetails = paymentDao.getPaymentDetailById(id);
		 Long paymentId = payDetails.getPayment_id().getPayment_id();
		 String msg = " ";
	     Payment payment = paymentService.findOneWithAll(paymentId);
	     if(payment.getPaymentDetails().size()!=1)
	     {
		 paymentService.deletePaymentDetail(id, company_id);
		 msg = "Product deleted successfully";
	     }
	     else
	     {
	    	 msg = "You can't delete this product. You can edit this or you can add your required product first and then delete this product.";
	    	
	     }
	     session.setAttribute("msg", msg);
		 model.setViewName("redirect:/editPayment?id=" + paymentId);
		 return model;		
	 }	 
	 
	 @RequestMapping(value="getPaidTDSPayment", method=RequestMethod.POST)
		public @ResponseBody Double getPaidTDSPayment(@RequestParam("billid") long billid,HttpServletRequest request, HttpServletResponse response)
		{
		 return paymentService.findpaidtds(billid);		
		}
	 
	 public static Double round(Double d, int decimalPlace) {
	    	DecimalFormat df = new DecimalFormat("#");
			df.setMaximumFractionDigits(decimalPlace);
			return new Double(df.format(d));
	    }

@RequestMapping(value="getOpeningBalanceforpayment", method=RequestMethod.GET)
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
		
		OpeningBalances paymentopening = openingbalances.isOpeningBalancePresentPayment(date, company_id, supplierid); // subLedgerService.findOne("Cash
		if(paymentopening!=null){
		if(paymentopening.getCredit_balance()!=null && paymentopening.getDebit_balance()!=null)
		{
		creditbalance = paymentopening.getCredit_balance();
		debitbalance = paymentopening.getDebit_balance();
		}
		}
		if(creditbalance>0)
		{
		List<Payment> paymentaginstopening=	paymentService.getAllOpeningBalanceAgainstPayment(supplierid, company_id);
		List<DebitNote> debitnoteagainstopening=debitNoteService.getAllOpeningBalanceAgainstDebitNote(supplierid, company_id);
		for(Payment payment:paymentaginstopening)
		{
			amount=amount+payment.getAmount();
		}
		for(DebitNote payment:debitnoteagainstopening)
		{
			amount=amount+payment.getRound_off();
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
	
}