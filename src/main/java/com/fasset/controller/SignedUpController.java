package com.fasset.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ccavenue.security.AesCryptUtil;
import com.fasset.ccavenue.CCAvenueParams;
import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.SignUpValidator;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Company;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.entities.IndustryType;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.UserForm;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICompanyStatutoryTypeService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IRoleService;
import com.fasset.service.interfaces.IindustryTypeService;

@Controller 
public class SignedUpController extends MyAbstractController {
	
	@Value("${ccav.access_code}")
	private String accessCode;
	
	@Value("${ccav.secret_key}")
	private String secretKey;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private ICompanyStatutoryTypeService companyTypeService;
	
	@Autowired
	private IindustryTypeService industryTypeService;
	
	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IQuotationService quotationService;
	
	@Autowired
	private IClientSubscriptionMasterService subscriptionService;
	
	@Autowired
	private SignUpValidator signUpValidator;
	
	@Autowired
	private CCAvenueParams ccAvenueParams;
	
	@Autowired
	private IAccountingYearDAO yearDAO;
	
	/*private UserForm signUpDetailsforUser;
	
	private Company companyforuser;
	
	private User user1;*/
	
	static Logger log = LogManager.getLogger(SignedUpController.class.getName());

	@RequestMapping(value = "signedUp", method = RequestMethod.GET)
	public ModelAndView signeUP(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		Company company = new Company();
		User user = new User();
		UserForm signUpDetails = new UserForm();
		signUpDetails.setUser(user);
		signUpDetails.setCompany(company);
		List<CompanyStatutoryType> companyTypes = companyTypeService.findAllactive();
		List<IndustryType> industryTypes = industryTypeService.findAllactive();
		List<AccountingYear> accountingYear=yearDAO.findAll();
		model.addObject("signedUpForm", signUpDetails);
		model.addObject("companyTypes", companyTypes);
		model.addObject("industryTypes", industryTypes);
		model.addObject("accountingYear", accountingYear);
		model.setViewName("signedup/signedUp");
		return model;
	}
	
	@RequestMapping(value = "signedUp", method = RequestMethod.POST)
	public synchronized ModelAndView signeUP(@ModelAttribute("signedUpForm") UserForm signUpDetails,
			BindingResult result,HttpServletRequest request, HttpServletResponse response) throws MyWebException {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		//signUpDetails.get
		System.out.println("acc year sign up "+signUpDetails.getCompany().getYearRangeId());
		if(signUpDetails.getCompanyId() != null && signUpDetails.getCompanyId() > 0) {
			signUpDetails.setCompany(companyService.getById(signUpDetails.getCompanyId()));
		}
		signUpValidator.validate(signUpDetails, result);
		
		if(result.hasErrors()){
			if((signUpDetails.getCompanyId() == null) || (signUpDetails.getCompanyId() == 0)) {
				List<CompanyStatutoryType> companyTypes = companyTypeService.list();
				List<IndustryType> industryTypes = industryTypeService.list();
				List<AccountingYear> accountingYear=yearDAO.findAll();
				model.addObject("signedUpForm", signUpDetails);
				model.addObject("companyTypes", companyTypes);
				model.addObject("industryTypes", industryTypes);
				model.addObject("accountingYear", accountingYear);
				model.setViewName("signedup/signedUp");			
			}
			else {
				model.setViewName("/signedup/subscribe");
			}
			return model;
		}
		
		else{
			try {
				Company company = signUpDetails.getCompany();
				User user = new User();
				if(signUpDetails.getCompanyId() == null || signUpDetails.getCompanyId() == 0) {
					company.setCreated_date(new LocalDate());
					user = signUpDetails.getUser();				
					user.setIs_updated(false);					
				}
				if(signUpDetails.getSignUpType() == STATUS_TRIAL_LOGIN){					
					company.setStatus(STATUS_TRIAL_LOGIN);
					company.setIndustryTypeId(signUpDetails.getIndustryTypeId());
					company.setCompanyTypeId(signUpDetails.getCompanyTypeId());
					//Long year_id = yearDAO.findcurrentyear(); Accept Year Range from User
					//company.setYearRange(year_id.toString());Accept Year Range from User
					
					company.setYearRange(signUpDetails.getCompany().getYearRangeId().toString());
					user.setStatus(true);
					user.setRole(roleService.getById(ROLE_TRIAL_USER));
					companyService.saveSignUpDetails(company, user);
					model.addObject("message", "Thank You. Please check your email for login details.");
					model.addObject("headTitle", "Registration Successful");
					model.addObject("nextForm", "login");
					model.setViewName("/commons/continueProc");
				}
				if(signUpDetails.getSignUpType() == STATUS_PENDING_FOR_APPROVAL){
					
					Quotation quotation = quotationService.getQuotation(signUpDetails.getCompany().getEmail_id(), signUpDetails.getQuotationNo());
					session.setAttribute("signUpDetails", signUpDetails);
					session.setAttribute("company", company);
					session.setAttribute("user1", user);
					session.setAttribute("quotationDetails", quotation);

					Float totalAmount = new Float(0);
					for (QuotationDetails quotDetail : quotation.getQuotationDetails()) {
						totalAmount += quotDetail.getAmount();
					}
					quotation.setCompanyId(signUpDetails.getCompanyId());
					quotation.setAmount(totalAmount);
					quotation.setPaymentType(signUpDetails.getPaymentType());
					model.addObject("quotation", quotation);
					model.setViewName("/signedup/quotationDetails");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return model;
		}
	}
	
	@RequestMapping(value = "subscribe", method = RequestMethod.GET)
	public synchronized ModelAndView subscribe(@RequestParam("cId")Long cId,
			HttpServletRequest request, 
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		UserForm signUpDetails = new UserForm();
		signUpDetails.setCompanyId(cId);
		signUpDetails.setSignUpType(STATUS_PENDING_FOR_APPROVAL);
		model.addObject("signedUpForm", signUpDetails);
		model.setViewName("/signedup/subscribe");
		return model;
	}
	
	@RequestMapping(value = "proceedNext", method = RequestMethod.POST)
	public synchronized ModelAndView proceedNext(@ModelAttribute("quotation")Quotation quotation,
			HttpServletRequest request, 
			HttpServletResponse response) throws MyWebException {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		UserForm signUpDetails=(UserForm) session.getAttribute("signUpDetails");
		Company company = (Company) session.getAttribute("company");
		User user= (User) session.getAttribute("user1");
		Quotation quotationDetails = (Quotation) session.getAttribute("quotationDetails");
		session.removeAttribute("signUpDetails");
		session.removeAttribute("company");
		session.removeAttribute("user1");
		session.removeAttribute("quotationDetails");
		
		Long companyId  =null;
		if(signUpDetails.getPaymentType() != MyAbstractController.PAYMENT_ONLINE && (signUpDetails.getCompanyId() == null || signUpDetails.getCompanyId() == 0)){
			company.setStatus(STATUS_INACTIVE);
			user.setStatus(false);
			user.setRole(roleService.getById(ROLE_CLIENT));						
			user.setFirst_name(quotationDetails.getFirst_name());
			user.setLast_name(quotationDetails.getLast_name());
			user.setMobile_no(quotationDetails.getMobile_no());
			user.setEmail(quotationDetails.getEmail());
			user.setAmount(quotation.getAmount().toString());
			company.setCompany_name(quotationDetails.getCompany_name());
			company.setIndustry_type(quotationDetails.getIndustrytype());
			company.setCompany_statutory_type(quotationDetails.getCompanystatutorytype());
			companyId = companyService.saveSignUpDetails(company, user);
			
		}
		ClientSubscriptionMaster clientSubscriptionMaster = new ClientSubscriptionMaster();
		clientSubscriptionMaster.setQuotationId(quotation.getQuotation_id());
		clientSubscriptionMaster.setSubscription_amount(quotation.getAmount());
		clientSubscriptionMaster.setPayment_mode(quotation.getPaymentType());
		clientSubscriptionMaster.setStatus(true);
		
		subscriptionService.add(clientSubscriptionMaster);
		Company company1 = companyService.getById(companyId);
		company1.setStatus(STATUS_PENDING_FOR_APPROVAL);
		companyService.updateCompany(company1);
		model.addObject("message", "You are successfully registered for fasset, approval procedure is in process, we will confirm to you over email soon");
		model.addObject("headTitle", "Registration Successful");
		model.addObject("nextForm", "login");
		model.setViewName("/commons/continueProc");
		return model;
	}
	@RequestMapping(value = "ccavenuepayment", method = RequestMethod.POST)
	public synchronized ModelAndView ccavenuepayment(@ModelAttribute("quotation")Quotation quotation,
			HttpServletRequest request, 
			HttpServletResponse response) throws MyWebException {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		
		ccAvenueParams.setAmount(quotation.getAmount().toString());
		ccAvenueParams.setOrder_id(quotation.getQuotation_id());
		try {
			ccAvenueParams.setCancel_url(new URL(MyAbstractController.APPURL+"/ccavResponseHandler"));
			ccAvenueParams.setRedirect_url(new URL(MyAbstractController.APPURL+"/ccavResponseHandler"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		model.addObject("user", user);
		model.addObject("ccAvenueParams", ccAvenueParams);
		model.setViewName("/signedup/confirmPayment");
		return model;			
	}
	@RequestMapping(value = "ccavRequestHandler", method = RequestMethod.POST)
	public synchronized ModelAndView ccavRequestHandler(HttpServletRequest request, 
			HttpServletResponse response) throws MyWebException {
		ModelAndView model = new ModelAndView();
		Enumeration<?> enumeration=request.getParameterNames();
		String ccaRequest="", pname="", pvalue="";
		while(enumeration.hasMoreElements()){
			pname = ""+enumeration.nextElement();
			pvalue = request.getParameter(pname);
			System.out.println("pname = "+pname+" pvalue = "+pvalue);
			ccaRequest = ccaRequest + pname + "=" + pvalue + "&";
		}
		AesCryptUtil aesUtil=new AesCryptUtil(secretKey);
		String encRequest = aesUtil.encrypt(ccaRequest);
		System.out.println("encrypted request : "+encRequest);
		model.addObject("encRequest", encRequest);
		System.out.println("accessCode=["+accessCode +"]");
		model.addObject("accessCode", accessCode);
		model.setViewName("/signedup/ccavRequestHandler");
		return model;
	}
	@RequestMapping(value = "ccavResponseHandler", method = RequestMethod.POST)
	public synchronized ModelAndView ccavResponseHandler(HttpServletRequest request, 
			HttpServletResponse response) throws MyWebException {
		ModelAndView model = new ModelAndView();
		
		String encResp= request.getParameter("encResp");
		System.out.println("secretKey=["+secretKey+"]");
		AesCryptUtil aesUtil=new AesCryptUtil(secretKey);
		String decResp = aesUtil.decrypt(encResp);
		StringTokenizer tokenizer = new StringTokenizer(decResp, "&");
		Hashtable<String, Object> hs=new Hashtable<String, Object>();
		String pair=null, pname=null, pvalue=null;
		while (tokenizer.hasMoreTokens()) {
			pair = (String)tokenizer.nextToken();
			if(pair!=null){
				StringTokenizer strTok=new StringTokenizer(pair, "=");
				pname=""; pvalue="";
				if(strTok.hasMoreTokens()) {
					pname=(String)strTok.nextToken();
					if(strTok.hasMoreTokens())
						pvalue=(String)strTok.nextToken();
					hs.put(pname, pvalue);
				}
			}
		}
		
		ClientSubscriptionMaster clientSubscriptionMaster = new ClientSubscriptionMaster();
		clientSubscriptionMaster.setQuotationId(new Long(hs.get("order_id").toString()));
		clientSubscriptionMaster.setSubscription_amount(new Float(hs.get("amount").toString()));
		clientSubscriptionMaster.setPayment_mode(MyAbstractController.PAYMENT_ONLINE);
		if(hs.get("order_status").equals("Success")) {
			clientSubscriptionMaster.setStatus(true);
			if(hs.get("company_id") == null) {
				Company company = new Company();
				User user = new User();
				
				Quotation quotation = quotationService.getById(new Long(hs.get("order_id").toString()));
				company.setStatus(STATUS_PENDING_FOR_APPROVAL);
				user.setStatus(false);
				user.setRole(roleService.getById(ROLE_CLIENT));						
				user.setFirst_name(quotation.getFirst_name());
				user.setLast_name(quotation.getLast_name());
				user.setIs_updated(false);
				company.setCompany_name(quotation.getCompany_name());
				company.setEmail_id(quotation.getEmail());
				company.setMobile(quotation.getMobile_no());
				company.setIndustry_type(quotation.getIndustrytype());
				company.setCompany_statutory_type(quotation.getCompanystatutorytype());
				companyService.saveSignUpDetails(company, user);

				model.addObject("message", "You are successfully registered for fasset, approval procedure is in process, we will confirm to you over email soon");
				model.addObject("headTitle", "Registration Successful");
			}
			else {
				model.addObject("message", "You are successfully subscribed for fasset, approval procedure is in process, we will confirm to you over email soon");
				model.addObject("headTitle", "Subscription Successful");				
			}
		}
		else {
			clientSubscriptionMaster.setStatus(false);
			if(hs.get("company_id") == null) {
				model.addObject("message", "Your Subscribition yet pending. Please sign up again and subscribe with us for confirm your account.\n Failure Reason : "+hs.get("failure_message"));
				model.addObject("headTitle", "Registration Failed");
			}
			else {
				model.addObject("message", "Your Subscribition yet pending. Please subscribe with us for confirm your account.\n Failure Reason : "+hs.get("failure_message"));
				model.addObject("headTitle", "Subscription Failed");				
			}
		}
		
		subscriptionService.add(clientSubscriptionMaster);
		
		model.addObject("nextForm", "login");
		model.setViewName("/commons/continueProc");
		return model;
	}

}