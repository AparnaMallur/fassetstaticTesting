/**Shilpa Joshi ***/
package com.fasset.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.ContraValidator;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.Contra;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Service;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.report.controller.ClassToConvertDateForExcell;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IContraService;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IYearEndingService;

@Controller
@SessionAttributes("user")
public class ContraControllerMVC extends MyAbstractController{

	
	@Autowired	
	private ContraValidator contravalidator;
	
	@Autowired	
	private IContraService contraservice;
	
	@Autowired
	private IAccountingYearService accountingYearService;
	
	@Autowired
	private IBankService bankService;
	
	@Autowired
	private ICommonService commonService;
	
	@Autowired
	private ISubLedgerDAO subledgerDAO ;
	
	@Autowired
	private IOpeningBalancesService openingbalances;
	
	@Autowired
	private ISubLedgerService subledgerService;
	
	@Autowired	
	private IYearEndingService yearService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICompanyService companyService ;
	
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
	
    private List<String> successVocharList = new ArrayList<String>();
	
	private List<String> failureVocharList = new ArrayList<String>();
	
	Boolean flag = null;
	
	@RequestMapping(value = "contraList", method = RequestMethod.GET)
	public ModelAndView contraList(@RequestParam(value = "msg", required = false)String msg,
			HttpServletRequest request,
			HttpServletResponse response) {
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
		boolean importfail=false;
		boolean importflag = false;
		flag=true;
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
	//	List<AccountingYear>  yearList = accountingYearService.findAccountRange(user.getUser_id(), user.getCompany().getYearRange(),company_id);

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
		if(contraservice.findAllContraEntry(company_id,false).size()!=0)
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
		model.addObject("contraList", contraservice.findAllContraEntry(company_id,true));
		model.addObject("importfail", importfail);
		model.addObject("flagFailureList", true);
		model.addObject("yearEndlist", yearEndlist);
		model.addObject("company", company);
		model.setViewName("/transactions/contraList");
		return model;
	}
	
	
	@RequestMapping(value = "viewContra", method = RequestMethod.GET)
	public ModelAndView viewContra(@RequestParam("id") long id) throws MyWebException {
		ModelAndView model = new ModelAndView();
		Contra contra = new Contra();
		try {
			contra = contraservice.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double bankto=0;
		double bankfrom=0;
		if(contra.getType()!=null && contra.getType()==1)
		{
			if(contra.getDeposite_to()!=null && contra.getCompany()!= null && contra.getAccounting_year_id()!=null)
			{
			bankto=openingbalances.getclosingbalance(contra.getCompany().getCompany_id(),contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getDeposite_to().getBank_id(),3);
			}

		}
		if(contra.getType()!=null && contra.getType()==2)
		{
			if(contra.getWithdraw_from()!=null && contra.getCompany()!= null && contra.getAccounting_year_id()!=null)
			{
			bankfrom=openingbalances.getclosingbalance(contra.getCompany().getCompany_id(),contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getWithdraw_from().getBank_id(),3);
			}

		}
		else if(contra.getType()!=null && contra.getType()==3)
		{
			if(contra.getDeposite_to()!=null && contra.getWithdraw_from()!=null && contra.getCompany()!= null && contra.getAccounting_year_id()!=null)
			{
			bankto=openingbalances.getclosingbalance(contra.getCompany().getCompany_id(),contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getDeposite_to().getBank_id(),3);
			bankfrom=openingbalances.getclosingbalance(contra.getCompany().getCompany_id(),contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getWithdraw_from().getBank_id(),3);
			}
		}
		model.addObject("bankto", bankto);
		model.addObject("bankfrom", bankfrom);
		model.addObject("contra",contra);
		model.addObject("flag",flag);
		if(contra.getCreated_by()!=null)
		{
		model.addObject("created_by", userService.getById(contra.getCreated_by()));
		}
		if(contra.getUpdated_by()!=null)
		{
		model.addObject("updated_by", userService.getById(contra.getUpdated_by()));
		}
		model.setViewName("/transactions/contraView");
		return model;
	}
	
	
	@RequestMapping(value ="deleteContra", method = RequestMethod.GET)
	public synchronized ModelAndView deleteContra(@RequestParam("id") long id,HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String msg="";
	    msg = contraservice.deleteByIdValue(id);
	    
	    session.setAttribute("msg", msg);
	    
	    if(flag == true)
		{
			return new ModelAndView("redirect:/contraList");
		}
		else
		{
			return new ModelAndView("redirect:/contraFailure");
		}
	}
	
	
	@RequestMapping(value = "contra", method = RequestMethod.GET)
	public ModelAndView contra(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		Contra contra = new Contra();
		User user = new User();
		user = (User) session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		long company_id=(long)session.getAttribute("company_id");
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		//List<AccountingYear>  yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		 if(AccYear==-1){
				yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
			}else{
				yearList = accountingYearService.findAccountRangeOfCompany(strLong);
			 
			}
		if(yearList.size()!=0)
		{
		model.addObject("bankList", bankService.findAllBanksOfCompany(company_id));
		model.addObject("yearList", yearList);
		model.addObject("contra", contra);
		
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
		
		
		
		model.setViewName("/transactions/contra");
		return model;
		}
		else
		{
			session.setAttribute("msg","Your account is closed");
			return new ModelAndView("redirect:/contraList");
			
		}
	}	
	
	@RequestMapping(value = "saveContra", method = RequestMethod.POST)
	public synchronized ModelAndView saveContra(@ModelAttribute("contra")Contra contra, 
			HttpServletRequest request,
			HttpServletResponse response,
			BindingResult result) {
		User user = new User();
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		user = (User) session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		long company_id=user.getCompany().getCompany_id();
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		ModelAndView model = new ModelAndView();		
		contravalidator.validate(contra, result);
		 if(AccYear==-1){
				yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
			}else{
				yearList = accountingYearService.findAccountRangeOfCompany(strLong);
			 
			}
		if(result.hasErrors()){
			model.addObject("bankList", bankService.findAllBanksOfCompany(company_id));
			//model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
			model.addObject("yearList",yearList);
			model.setViewName("/transactions/contra");
			return model;
		}
		else{
			String msg = "";
			try{
				contra.setCompany(user.getCompany());
				contra.setVoucher_no(commonService.getVoucherNumber("CV", VOUCHER_CONTRA,contra.getDate(), contra.getAccountYearId(), user.getCompany().getCompany_id()));				
				if(contra.getTransaction_id()!=null){
					if(contra.getTransaction_id() > 0){
						contra.setCompany(user.getCompany());
						contra.setUpdated_by(user_id);
						contraservice.update(contra);
						msg = "Contra updated successfully";					
					}
				}				
				else{
					contra.setFlag(true);
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            contra.setIp_address(remoteAddr);
			            contra.setCreated_by(user_id);
					msg = contraservice.saveContra(contra);
				}				
			}
			catch(Exception e){
				e.printStackTrace();
			}	
			session.setAttribute("msg", msg);
			return new ModelAndView("redirect:/contraList");
		}
	}
	
	
	
	@RequestMapping(value = "editContra", method = RequestMethod.GET)
	public synchronized ModelAndView editContra(@RequestParam("id") long id,
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		user = (User) session.getAttribute("user");
		Contra contra = new Contra();
		long company_id=(long)session.getAttribute("company_id");
		String yearrange = user.getCompany().getYearRange();
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		long user_id=(long)session.getAttribute("user_id");
		try {
			if(id > 0){
				contra = contraservice.findOneWithAll(id);
				if(contra.getDate()!=null)
				{
				contra.setDate(contra.getDate());
				}
				if(contra.getType()!=null && contra.getDeposite_to()!=null && contra.getType() == MyAbstractController.CONTRA_DEPOSITE){
					contra.setDepositeTo(contra.getDeposite_to().getBank_id());
					
				}
				else if(contra.getType()!=null && contra.getWithdraw_from()!=null && contra.getType() == MyAbstractController.CONTRA_WITHDRAW){
					contra.setWithdrawFrom(contra.getWithdraw_from().getBank_id());
					
				}
				else if(contra.getType()!=null && contra.getWithdraw_from()!=null && contra.getDeposite_to()!=null && contra.getType() == MyAbstractController.CONTRA_TRANSFER){
					contra.setDepositeTo(contra.getDeposite_to().getBank_id());
					contra.setWithdrawFrom(contra.getWithdraw_from().getBank_id());					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(AccYear==-1){
			yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		}else{
			yearList = accountingYearService.findAccountRangeOfCompany(strLong);
		 
		}
		model.addObject("yearList",yearList);
		//model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
		model.addObject("bankList", bankService.findAllBanksOfCompany(company_id));
		model.addObject("contra", contra);
		model.setViewName("/transactions/contra");
		return model;
	}
	
	
	
	@RequestMapping(value = {"importExcelContra"}, method = { RequestMethod.POST })
	public ModelAndView importExcelContra(@RequestParam("excelFile") MultipartFile excelfile,HttpServletRequest request, HttpServletResponse response) throws IOException{
		boolean isValid = true;
		
		StringBuffer successmsg = new StringBuffer();
		StringBuffer failuremsg = new StringBuffer();	
	
		Integer failcount=0;
		Integer sucount=0;
		Integer maincount = 0;
		
		successVocharList.clear();
		failureVocharList.clear();
		
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		long user_id=(long)session.getAttribute("user_id");
		
		Integer m=0;
		User user = new User();
		user = (User)session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		
		List<Bank> bankList = bankService.findAllBanksOfCompany(company_id);
		List<AccountingYear> yearlist =accountingYearService.findAccountRange(user_id, yearrange,company_id);
		try {
			   if (excelfile.getOriginalFilename().endsWith("xls")) {
	        	
	    			int i = 0;
	    			HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
	    			HSSFSheet worksheet = workbook.getSheetAt(0);
					
					if(isValid){
						i = 1;
		    			while (i <= worksheet.getLastRowNum()) {
		    				
		    				Contra entry = new Contra();
						
							Bank bankWithdraw = null;
							Bank bankDeposit= null;
							Integer count=0;
							HSSFRow row = worksheet.getRow(i++);
							if(row.getLastCellNum()==0)
							{				
						    	System.out.println("Invalid Data");
							}
							else
							{
								Boolean flag = false;
								String contraVoucherNofromExcel="";
								
								try
								{
									contraVoucherNofromExcel=row.getCell(0).getStringCellValue().trim().replaceAll(" ", "");;
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								
								try
								{
									Double voucherNofromExcel= row.getCell(0).getNumericCellValue();
									Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
									contraVoucherNofromExcel=voucherNofromExcel1.toString().trim();
									
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								Contra oldcontra = contraservice.isExcelVocherNumberExist(contraVoucherNofromExcel, company_id);
									if(oldcontra!=null && oldcontra.getExcel_voucher_no()!=null)
									{
									String str=oldcontra.getExcel_voucher_no().trim();
									if(str.replace(" ","").trim().equalsIgnoreCase(contraVoucherNofromExcel.replace(" ","").trim())) {
										flag=true;
									   /* break;*/
									}
									}
							
								if(flag==false)
								{
									
									if(!failureVocharList.contains(contraVoucherNofromExcel))
									{
									entry.setExcel_voucher_no(contraVoucherNofromExcel);
									
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									entry.setIp_address(remoteAddr);
									entry.setCreated_by(user_id);
									
								if(row.getCell(1)!=null)
								{
									try {
										entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(1).getDateCellValue().toString())));
									   }
									   catch (Exception e) {
									    e.printStackTrace();
									    }
									}
								
								try {
									for(AccountingYear year_range:yearlist)
									{
										
										String str =year_range.getYear_range().trim();
										if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(7).getStringCellValue().replace(" ","").trim())) {
											entry.setAccounting_year_id(year_range);
											count=count+1;
											if(entry.getDate()!=null)
											{
											entry.setVoucher_no(commonService.getVoucherNumber("CV", VOUCHER_CONTRA,entry.getDate(), year_range.getYear_id(), company_id));		
											}
										    break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								
								if(row.getCell(2)!=null)
								{
								if(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Deposit") && row.getCell(4)!=null)
								{
									String[] banknameAndAcc = row.getCell(4).getStringCellValue().replace(" ","").trim().split("-");
									String bankname = banknameAndAcc[0];
									String accno = banknameAndAcc[1];
									
									
									try {
										for(Bank bank:bankList)
										{
											String strbankname =bank.getBank_name().trim();
											String straccno = bank.getAccount_no().toString();
											if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno)) 
											{
												entry.setDeposite_to(bank);
												bankDeposit=bank;
												count=count+1;
												entry.setType(1);
											    break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								else if(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Withdraw")&& row.getCell(3)!=null)
								{
									String[] banknameAndAcc = row.getCell(3).getStringCellValue().replace(" ","").trim().split("-");
									String bankname = banknameAndAcc[0];
									String accno = banknameAndAcc[1];
									
									
									try {
										for(Bank bank:bankList)
										{
											String strbankname =bank.getBank_name().trim();
											String straccno = bank.getAccount_no().toString();
											
											if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno))  {
												entry.setWithdraw_from(bank);
												bankWithdraw=bank;
												count=count+1;
												entry.setType(2);
											    break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								else if(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Transfer") && row.getCell(3)!=null && row.getCell(4)!=null)
								{
									entry.setType(3);
									String[] banknameAndAcc = row.getCell(3).getStringCellValue().replace(" ","").trim().split("-");
									String bankname = banknameAndAcc[0];
									String accno = banknameAndAcc[1];
									
									try {
										for(Bank bank:bankList)
										{
											String strbankname =bank.getBank_name().trim();
											String straccno = bank.getAccount_no().toString();
											
											
											if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno))
											{
												entry.setWithdraw_from(bank);
												bankWithdraw=bank;
												count=count+1;
											    break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									String[] banknameAndAcc1 = row.getCell(4).getStringCellValue().replace(" ","").trim().split("-");
									String bankname1 = banknameAndAcc1[0];
									String accno1 = banknameAndAcc1[1];
									
									try {
										for(Bank bank:bankList)
										{
											String strbankname =bank.getBank_name().trim();
											String straccno = bank.getAccount_no().toString();
											
											
											if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname1) && straccno.replace(" ","").trim().equalsIgnoreCase(accno1))
											{
												entry.setDeposite_to(bank);
												bankDeposit=bank;
												count=count+1;
											    break;
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
								}
								entry.setCompany(user.getCompany());
								entry.setAmount(round((double) row.getCell(5).getNumericCellValue(),2));
								if(row.getCell(6)!=null)
								{
								entry.setOther_remark(row.getCell(6).getStringCellValue().trim());
								}
								
								
								if(entry.getVoucher_no()!=null && entry.getType()!=null)
								{
									if ((count.equals(2) && (entry.getType().equals(1) || entry.getType().equals(2)))|| (count.equals(3) && bankWithdraw!=null && bankDeposit!=null)) {
										entry.setFlag(true);
										sucount=sucount+1;
										successVocharList.add(contraVoucherNofromExcel);
										entry.setLocal_time(new LocalTime());
										Long id  = contraservice.saveContraThroughExcel(entry);
										Contra contra1 = contraservice.getById(id);
										
										if(row.getCell(2)!=null)
										{
										if(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Deposit"))
										{
											SubLedger cashSubledger = subledgerDAO.findOne("Cash In Hand",company_id);
											if(bankDeposit!=null)
											{
											bankService.addbanktransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id,bankDeposit.getBank_id(),(long) 3,(double) 0,(double) row.getCell(5).getNumericCellValue(),(long) 1);
											}
											if(cashSubledger!=null)
											{
											subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id, cashSubledger.getSubledger_Id(), (long) 2, (double) row.getCell(5).getNumericCellValue(), (double) 0,(long) 1,null,null,null,null,null,null,contra1,null,null,null,null,null);
											}
										}
										if(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Withdraw"))
										{
											SubLedger cashSubledger = subledgerDAO.findOne("Cash In Hand",company_id);
											if(bankWithdraw!=null)
											{
											bankService.addbanktransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id,bankWithdraw.getBank_id(),(long) 3,(double) row.getCell(5).getNumericCellValue(),(double) 0,(long) 1);
											}
											if(cashSubledger!=null)
											{
											subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id, cashSubledger.getSubledger_Id(), (long) 2, (double) 0,(double) row.getCell(5).getNumericCellValue(),(long) 1,null,null,null,null,null,null,contra1,null,null,null,null,null);
											}
										}
										 
									if (row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Transfer")) {
		 
										if(bankWithdraw!=null && bankDeposit!=null)
										{
										bankService.addbanktransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id,bankDeposit.getBank_id(),(long) 3,(double) 0,(double) row.getCell(5).getNumericCellValue(),(long) 1);
										bankService.addbanktransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id,bankWithdraw.getBank_id(),(long) 3,(double) row.getCell(5).getNumericCellValue(),(double) 0,(long) 1);
									    }
									}
									}
									} 
									else {
										
										maincount=maincount+1;
										failcount=failcount+1;
				                        failureVocharList.add(contraVoucherNofromExcel);
									}
									
								}
								else {
									
									maincount=maincount+1;
									failcount=failcount+1;
			                        failureVocharList.add(contraVoucherNofromExcel);
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
		    			workbook.close(); 
		    			}
	        	} 
	        else if (excelfile.getOriginalFilename().endsWith("xlsx")) 
	        {
				int i = 0;
				
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
	     		if(isValid){
					i = 1;
					while (i <= worksheet.getLastRowNum()) {
						Contra entry = new Contra();
						Bank bankWithdraw = null;
						Bank bankDeposit= null;
						Integer count=0;
						XSSFRow row = worksheet.getRow(i++);
						if(row.getLastCellNum()==0)
						{				
					    	System.out.println("Invalid Data");
						}
						else
						{
							System.out.println("entered");
							Boolean flag = false;
							String contraVoucherNofromExcel="";
							
							try
							{
								contraVoucherNofromExcel=row.getCell(0).getStringCellValue().trim().replaceAll(" ", "");;
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							try
							{
								Double voucherNofromExcel= row.getCell(0).getNumericCellValue();
								Integer voucherNofromExcel1 = voucherNofromExcel.intValue();
								contraVoucherNofromExcel=voucherNofromExcel1.toString().trim();
								
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							Contra oldcontra = contraservice.isExcelVocherNumberExist(contraVoucherNofromExcel, company_id);
								if(oldcontra!=null && oldcontra.getExcel_voucher_no()!=null)
								{
								String str=oldcontra.getExcel_voucher_no().trim();
								if(str.replace(" ","").trim().equalsIgnoreCase(contraVoucherNofromExcel.replace(" ","").trim())) {
									flag=true;
								   /* break;*/
								}
								}
						
							if(flag==false)
							{
								
								if(!failureVocharList.contains(contraVoucherNofromExcel))
								{
								entry.setExcel_voucher_no(contraVoucherNofromExcel);
								
								String remoteAddr = "";
								remoteAddr = request.getHeader("X-FORWARDED-FOR");
								if (remoteAddr == null || "".equals(remoteAddr)) {
									remoteAddr = request.getRemoteAddr();
								}
								entry.setIp_address(remoteAddr);
								entry.setCreated_by(user_id);
								
							if(row.getCell(1)!=null)
							{
								try {
									entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(1).getDateCellValue().toString())));
								   }
								   catch (Exception e) {
								    e.printStackTrace();
								    }
								}
							
							try {
								for(AccountingYear year_range:yearlist)
								{
									
									String str =year_range.getYear_range().trim();
									if(str.replace(" ","").trim().equalsIgnoreCase(row.getCell(7).getStringCellValue().replace(" ","").trim())) {
										entry.setAccounting_year_id(year_range);
										count=count+1;
										if(entry.getDate()!=null)
										{
										entry.setVoucher_no(commonService.getVoucherNumber("CV", VOUCHER_CONTRA,entry.getDate(), year_range.getYear_id(), company_id));		
										}
									    break;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							
							if(row.getCell(2)!=null)
							{
							if(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Deposit") && row.getCell(4)!=null)
							{
								String[] banknameAndAcc = row.getCell(4).getStringCellValue().replace(" ","").trim().split("-");
								String bankname = banknameAndAcc[0];
								String accno = banknameAndAcc[1];
								
								
								try {
									for(Bank bank:bankList)
									{
										String strbankname =bank.getBank_name().trim();
										String straccno = bank.getAccount_no().toString();
										if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno)) 
										{
											entry.setDeposite_to(bank);
											bankDeposit=bank;
											count=count+1;
											entry.setType(1);
										    break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							else if(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Withdraw")&& row.getCell(3)!=null)
							{
								String[] banknameAndAcc = row.getCell(3).getStringCellValue().replace(" ","").trim().split("-");
								String bankname = banknameAndAcc[0];
								String accno = banknameAndAcc[1];
								
								
								try {
									for(Bank bank:bankList)
									{
										String strbankname =bank.getBank_name().trim();
										String straccno = bank.getAccount_no().toString();
										
										if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno))  {
											entry.setWithdraw_from(bank);
											bankWithdraw=bank;
											count=count+1;
											entry.setType(2);
										    break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							else if(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Transfer") && row.getCell(3)!=null && row.getCell(4)!=null)
							{
								entry.setType(3);
								String[] banknameAndAcc = row.getCell(3).getStringCellValue().replace(" ","").trim().split("-");
								String bankname = banknameAndAcc[0];
								String accno = banknameAndAcc[1];
								
								try {
									for(Bank bank:bankList)
									{
										String strbankname =bank.getBank_name().trim();
										String straccno = bank.getAccount_no().toString();
										
										
										if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname) && straccno.replace(" ","").trim().equalsIgnoreCase(accno))
										{
											entry.setWithdraw_from(bank);
											bankWithdraw=bank;
											count=count+1;
										    break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								String[] banknameAndAcc1 = row.getCell(4).getStringCellValue().replace(" ","").trim().split("-");
								String bankname1 = banknameAndAcc1[0];
								String accno1 = banknameAndAcc1[1];
								
								try {
									for(Bank bank:bankList)
									{
										String strbankname =bank.getBank_name().trim();
										String straccno = bank.getAccount_no().toString();
										
										
										if(strbankname.replace(" ","").trim().equalsIgnoreCase(bankname1) && straccno.replace(" ","").trim().equalsIgnoreCase(accno1))
										{
											entry.setDeposite_to(bank);
											bankDeposit=bank;
											count=count+1;
										    break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
							}
							entry.setCompany(user.getCompany());
							entry.setAmount(round((double) row.getCell(5).getNumericCellValue(),2));
							if(row.getCell(6)!=null)
							{
							entry.setOther_remark(row.getCell(6).getStringCellValue().trim());
							}
							
							
							if(entry.getVoucher_no()!=null && entry.getType()!=null)
							{
								if ((count.equals(2) && (entry.getType().equals(1) || entry.getType().equals(2)))|| (count.equals(3) && bankWithdraw!=null && bankDeposit!=null)) {
									entry.setFlag(true);
									sucount=sucount+1;
									successVocharList.add(contraVoucherNofromExcel);
									entry.setLocal_time(new LocalTime());
									Long id  = contraservice.saveContraThroughExcel(entry);
									Contra contra1 = contraservice.getById(id);
									
									if(row.getCell(2)!=null)
									{
									if(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Deposit"))
									{
										SubLedger cashSubledger = subledgerDAO.findOne("Cash In Hand",company_id);
										if(bankDeposit!=null)
										{
										bankService.addbanktransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id,bankDeposit.getBank_id(),(long) 3,(double) 0,(double) row.getCell(5).getNumericCellValue(),(long) 1);
										}
										if(cashSubledger!=null)
										{
										subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id, cashSubledger.getSubledger_Id(), (long) 2, (double) row.getCell(5).getNumericCellValue(), (double) 0,(long) 1,null,null,null,null,null,null,contra1,null,null,null,null,null);
										}
									}
									if(row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Withdraw"))
									{
										SubLedger cashSubledger = subledgerDAO.findOne("Cash In Hand",company_id);
										if(bankWithdraw!=null)
										{
										bankService.addbanktransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id,bankWithdraw.getBank_id(),(long) 3,(double) row.getCell(5).getNumericCellValue(),(double) 0,(long) 1);
										}
										if(cashSubledger!=null)
										{
										subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id, cashSubledger.getSubledger_Id(), (long) 2, (double) 0,(double) row.getCell(5).getNumericCellValue(),(long) 1,null,null,null,null,null,null,contra1,null,null,null,null,null);
										}
									}
									 
								if (row.getCell(2).getStringCellValue().trim().equalsIgnoreCase("Transfer")) {
	 
									if(bankWithdraw!=null && bankDeposit!=null)
									{
									bankService.addbanktransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id,bankDeposit.getBank_id(),(long) 3,(double) 0,(double) row.getCell(5).getNumericCellValue(),(long) 1);
									bankService.addbanktransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getDate(),company_id,bankWithdraw.getBank_id(),(long) 3,(double) row.getCell(5).getNumericCellValue(),(double) 0,(long) 1);
								    }
								}
								}
								} 
								else {
									
									maincount=maincount+1;
									failcount=failcount+1;
			                        failureVocharList.add(contraVoucherNofromExcel);
								}
								
							}
							else {
								
								maincount=maincount+1;
								failcount=failcount+1;
		                        failureVocharList.add(contraVoucherNofromExcel);
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
					workbook.close();
					}
					
				} 
				else {
					System.out.println("no data");

				}
		}
		 catch (Exception e) {
			e.printStackTrace();
			isValid = false;
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
				return new ModelAndView("redirect:/contraList");
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
				return new ModelAndView("redirect:/contraList");
			}
			else
			{
				successmsg.append("You are inserting duplicate records, please check excel file");
				String successmsg1=successmsg.toString();
				session.setAttribute("filemsg", successmsg1);
				return new ModelAndView("redirect:/contraList");
			}
			
		} else {
			successmsg.append("Please enter required and valid data in columns");
			String successmsg1=successmsg.toString();
			session.setAttribute("filemsg", successmsg1);
			return new ModelAndView("redirect:/contraList");

		}
	}
	
	@RequestMapping(value = "contraFailure", method = RequestMethod.GET)
	public ModelAndView contraFailure(HttpServletRequest request, HttpServletResponse response) {	    
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
		flag=false;
		  if((String) session.getAttribute("msg")!=null){
				model.addObject("successMsg", (String) session.getAttribute("msg"));
				session.removeAttribute("msg");
			}
	        List<YearEnding>  yearEndlist = yearService.findAllYearEnding(company_id);
    	//model.addObject("contraList", contraservice.findAllContraEntry(company_id,false));
    	model.addObject("flagFailureList", false);
    	model.addObject("yearEndlist", yearEndlist);
		model.setViewName("/transactions/contraList");
		return model;
	}
	
	@RequestMapping(value = "downloadContra", method = RequestMethod.GET)
    public ModelAndView downloadContra(@RequestParam("id") long id) throws MyWebException {
		Contra contra = contraservice.findOneWithAll(id);
		return new ModelAndView("ContraPdfView", "contra", contra);
    }
	
	/*@RequestMapping(value = "downloadContraAccountingVoucher", method = RequestMethod.GET)
    public ModelAndView downloadContraAccountingVoucher(@RequestParam("id") long id) throws MyWebException {
		ContraForm form = new ContraForm();
		Contra contra = contraservice.findOneWithAll(id);
		form.setContra(contra);
		return new ModelAndView("ContraAccountingVoucherPdfView", "form", form);
    }*/
	
	 public static Double round(Double d, int decimalPlace) {
	    	DecimalFormat df = new DecimalFormat("#");
			df.setMaximumFractionDigits(decimalPlace);
			return new Double(df.format(d));
	    }
	
}