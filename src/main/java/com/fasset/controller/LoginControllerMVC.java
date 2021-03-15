package com.fasset.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rewrite.MapRewritePolicy.Mode;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.ccavenue.security.Md5;
import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.Ask4MailValidator;
import com.fasset.controller.validators.ChangePassValidator;
import com.fasset.controller.validators.PasswordFormValidator;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.City;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Company;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.entities.Country;
import com.fasset.entities.Customer;
import com.fasset.entities.Suppliers;
import com.fasset.entities.Product;
import com.fasset.entities.Quotation;
import com.fasset.entities.Bank;
import com.fasset.entities.Ledger;

import com.fasset.entities.ForgotPasswordLog;
import com.fasset.entities.IndustryType;
import com.fasset.entities.MenuAccessMaster;
import com.fasset.entities.State;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.Ask4Mail;
import com.fasset.form.ChangePassword;
import com.fasset.form.Notification;
import com.fasset.form.SalesReportForm;
import com.fasset.form.UserForm;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IindustryTypeService;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.ICityService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICompanyStatutoryTypeService;
import com.fasset.service.interfaces.ICountryService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.fasset.service.interfaces.IForgotPasswordLogService;
import com.fasset.service.interfaces.IStateService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IProductService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ILoginLogService;
import com.fasset.entities.MenuMaster;
import com.fasset.service.interfaces.IMenuAccessMasterService;

@Controller
@SessionAttributes("user")
public class LoginControllerMVC extends MyAbstractController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private ISuplliersService supplierService;

	@Autowired
	private IProductService productService;

	@Autowired
	private IBankService bankService;

	@Autowired
	private ILoginLogService loginService;

	@Autowired
	private ILedgerService ledgerService;

	@Autowired
	private IMenuAccessMasterService menuAccessMasterService;

	@Autowired
	private ICountryService countryService;

	@Autowired
	private IStateService stateService;

	@Autowired
	private ICityService cityService;

	@Autowired
	private ICompanyStatutoryTypeService companyTypeService;

	@Autowired
	private IindustryTypeService industryTypeService;

	@Autowired
	private IForgotPasswordLogService forgotPasswordLogService;

	@Autowired
	private Ask4MailValidator ask4MailValidator;

	@Autowired
	private PasswordFormValidator passwordFormValidator;

	@Autowired
	private ChangePassValidator changePasswordValidator;

	@Autowired
	private IAccountingYearService accountingYearService;

	@Autowired
	private IQuotationService quoteService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private ISubLedgerService subLedgerService;

	@Autowired
	private IPurchaseEntryService purEntryService;
	@Autowired
	private ISalesEntryService entryService;

	static Logger log = LogManager.getLogger(LoginControllerMVC.class.getName());
	public Long idlg;

	public List<Notification> notifications = new ArrayList<Notification>();

	List<ViewApprovalsForm> approvalList = new ArrayList<>();

	@Transactional
	@RequestMapping(value = HOME, method = RequestMethod.GET)
	public ModelAndView homePage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession();
		long role = (long) session.getAttribute("role");
		String setYear = null;
		long CompanyId = (long) session.getAttribute("company_id");
		User user = (User) session.getAttribute("user");
		System.out.println("The User Id is 21Apr" +user.getUser_id());
		
		if ((String) session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", (String) session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}

		if (role == ROLE_SUPERUSER) {
			Integer productlist = productService.findAllDashboard();
			Integer bankList = bankService.findAllDashboard();
			Integer ledgerList = ledgerService.findAllDashboard();
			Integer subledgerCount = subLedgerService.findAllDashboard();
			Integer supplierCount = supplierService.findAllSupplierCountDashboard();
			Integer customerCount = customerService.findAllCustomerCountDashboard();
			
			CopyOnWriteArrayList<Quotation> list = quoteService.findAllQuotations();
			List<ClientSubscriptionMaster> listall = quoteService.findAllSubscriptionList();
			for (ClientSubscriptionMaster clientmaster : listall) {

				if (clientmaster.getQuotation_id() != null) {
					for (Quotation quotation : list) {
						if (clientmaster.getQuotation_id().getQuotation_id().equals(quotation.getQuotation_id())) {
							list.remove(quotation);
						}
					}
				}
			}

			/* Integer quoteList=quoteService.findAllDashboard(); */

			model.addObject("quoteList", list.size());
			model.addObject("subledgerCount", subledgerCount);
			model.addObject("supplierCount", supplierCount);
			model.addObject("customerCount", customerCount);
			model.addObject("ledgerList", ledgerList);
			model.addObject("bankList", bankList);
			model.addObject("productlist", productlist);
		} else if (role == ROLE_EXECUTIVE) {
			Integer productlist = productService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer bankList = bankService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
			Integer ledgerList = ledgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
			Integer subledgerCount = subLedgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer supplierCount = supplierService.findByStatusSupplierCountDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer customerCount = customerService.findByStatusCustomerCountDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());

			model.addObject("ledgerList", ledgerList);
			model.addObject("bankList", bankList);
			model.addObject("productlist", productlist);
			model.addObject("subledgerCount", subledgerCount);
			model.addObject("customerCount", customerCount);
			model.addObject("supplierCount", supplierCount);
		} else if (role == ROLE_MANAGER) {
			Integer productlist = productService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer bankList = bankService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
			Integer ledgerList = ledgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
			Integer subledgerCount = subLedgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer supplierCount = supplierService.findByStatusSupplierCountDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer customerCount = customerService.findByStatusCustomerCountDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());

			model.addObject("ledgerList", ledgerList);
			model.addObject("bankList", bankList);
			model.addObject("productlist", productlist);
			model.addObject("subledgerCount", subledgerCount);
			model.addObject("supplierCount", supplierCount);
			model.addObject("customerCount", customerCount);
		} else {
			Integer productlist = productService.findAllProductsOfCompanyDashboard(CompanyId);
			Integer bankList = bankService.findAllBanksOfCompanyDashboard(CompanyId);
			Integer ledgerList = ledgerService.findAllLedgersOfCompanyDashboard(CompanyId);
			/* Integer empList = userService.getActiveUserByCompanyDashboard(CompanyId); */
			Integer subledgerCount = subLedgerService.findAllSubLedgerOfCompanyDashboard(CompanyId);
			Integer supplierCount = supplierService.findAllSupplierCountOfCompanyDashboard(CompanyId);
			Integer customerCount = customerService.findAllCustomerCountOfCompanyDashboard(CompanyId);

			/* model.addObject("empList", empList); */
			model.addObject("closingBalanceOfCashInHand", subLedgerService.getClosingBalanceOfCashInHand(CompanyId));
			model.addObject("closingBalanceOfAllBanks", bankService.getClosingBalanceOfAllbanks(CompanyId));
			model.addObject("totalBillsReceivable", entryService.getTotalbillsBillsReceivable(CompanyId));
			model.addObject("totalBillsPayable", purEntryService.getTotalBillsPayable(CompanyId));
			model.addObject("ledgerList", ledgerList);
			model.addObject("bankList", bankList);
			model.addObject("productlist", productlist);
			model.addObject("subledgerCount", subledgerCount);
			model.addObject("supplierCount", supplierCount);
			model.addObject("customerCount", customerCount);
		}
		List<AccountingYear>  yearList = accountingYearService.findAccountRange(user.getUser_id(), user.getCompany().getYearRange(),CompanyId);
		
		
		
		
		for(AccountingYear a :yearList){
			System.out.println("accounting year id is " +a.getYear_range());
		}
		if(null == session.getAttribute("SetYear")){  
			
			 setYear="NA";
			}else{setYear="true";}
		model.addObject("yearList", yearList);
		model.addObject("setYear",setYear);
		model.addObject("approvalList", approvalList);
		if (session.getAttribute("approval") != null) {
			model.addObject("approval", (Integer) session.getAttribute("approval"));
			session.removeAttribute("approval");
		}
		
		model.setViewName("/homePage");
		return model;
	}
	
	

	@Transactional
	@RequestMapping(value = MobileHOME, method = RequestMethod.GET)
	public ModelAndView homePageForMobile(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession();
		long role = (long) session.getAttribute("role");
		long CompanyId = (long) session.getAttribute("company_id");
		User user = (User) session.getAttribute("user");
		if ((String) session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", (String) session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}

		if (role == ROLE_SUPERUSER) {
			Integer productlist = productService.findAllDashboard();
			Integer bankList = bankService.findAllDashboard();
			Integer ledgerList = ledgerService.findAllDashboard();
			Integer subledgerCount = subLedgerService.findAllDashboard();
			Integer supplierCount = supplierService.findAllSupplierCountDashboard();
			Integer customerCount = customerService.findAllCustomerCountDashboard();
			CopyOnWriteArrayList<Quotation> list = quoteService.findAllQuotations();
			List<ClientSubscriptionMaster> listall = quoteService.findAllSubscriptionList();
			for (ClientSubscriptionMaster clientmaster : listall) {

				if (clientmaster.getQuotation_id() != null) {
					for (Quotation quotation : list) {
						if (clientmaster.getQuotation_id().getQuotation_id().equals(quotation.getQuotation_id())) {
							list.remove(quotation);
						}
					}
				}
			}

			/* Integer quoteList=quoteService.findAllDashboard(); */

			model.addObject("quoteList", list.size());
			model.addObject("subledgerCount", subledgerCount);
			model.addObject("supplierCount", supplierCount);
			model.addObject("customerCount", customerCount);
			model.addObject("ledgerList", ledgerList);
			model.addObject("bankList", bankList);
			model.addObject("productlist", productlist);
		} else if (role == ROLE_EXECUTIVE) {
			Integer productlist = productService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer bankList = bankService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
			Integer ledgerList = ledgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
			Integer subledgerCount = subLedgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer supplierCount = supplierService.findByStatusSupplierCountDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer customerCount = customerService.findByStatusCustomerCountDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());

			model.addObject("ledgerList", ledgerList);
			model.addObject("bankList", bankList);
			model.addObject("productlist", productlist);
			model.addObject("subledgerCount", subledgerCount);
			model.addObject("customerCount", customerCount);
			model.addObject("supplierCount", supplierCount);
		} else if (role == ROLE_MANAGER) {
			Integer productlist = productService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer bankList = bankService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
			Integer ledgerList = ledgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
			Integer subledgerCount = subLedgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer supplierCount = supplierService.findByStatusSupplierCountDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());
			Integer customerCount = customerService.findByStatusCustomerCountDashboard(role, APPROVAL_STATUS_PENDING,
					user.getUser_id());

			model.addObject("ledgerList", ledgerList);
			model.addObject("bankList", bankList);
			model.addObject("productlist", productlist);
			model.addObject("subledgerCount", subledgerCount);
			model.addObject("supplierCount", supplierCount);
			model.addObject("customerCount", customerCount);
		} else {
			Integer productlist = productService.findAllProductsOfCompanyDashboard(CompanyId);
			Integer bankList = bankService.findAllBanksOfCompanyDashboard(CompanyId);
			Integer ledgerList = ledgerService.findAllLedgersOfCompanyDashboard(CompanyId);
			/* Integer empList = userService.getActiveUserByCompanyDashboard(CompanyId); */
			Integer subledgerCount = subLedgerService.findAllSubLedgerOfCompanyDashboard(CompanyId);
			Integer supplierCount = supplierService.findAllSupplierCountOfCompanyDashboard(CompanyId);
			Integer customerCount = customerService.findAllCustomerCountOfCompanyDashboard(CompanyId);

			/* model.addObject("empList", empList); */
			model.addObject("closingBalanceOfCashInHand", subLedgerService.getClosingBalanceOfCashInHand(CompanyId));
			model.addObject("closingBalanceOfAllBanks", bankService.getClosingBalanceOfAllbanks(CompanyId));
			model.addObject("totalBillsReceivable", entryService.getTotalbillsBillsReceivable(CompanyId));
			model.addObject("totalBillsPayable", purEntryService.getTotalBillsPayable(CompanyId));
			model.addObject("ledgerList", ledgerList);
			model.addObject("bankList", bankList);
			model.addObject("productlist", productlist);
			model.addObject("subledgerCount", subledgerCount);
			model.addObject("supplierCount", supplierCount);
			model.addObject("customerCount", customerCount);
		}
		model.addObject("approvalList", approvalList);
		if (session.getAttribute("approval") != null) {
			model.addObject("approval", (Integer) session.getAttribute("approval"));
			session.removeAttribute("approval");
		}
		model.setViewName("/mobilehomepage");
		return model;
	}
	
	
	
	
	
	
	

	@RequestMapping(value = LOGIN, method = RequestMethod.GET)
	public ModelAndView loginPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model.setViewName("/home-landing");
		return model;
	}
	
	@RequestMapping(value = MOBILELOGIN, method = RequestMethod.GET)
	public ModelAndView loginPageforMobile(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model.setViewName("/mobilehomelanding");
		return model;
	}

	@RequestMapping(value = LOGIN, method = RequestMethod.POST)
	public ModelAndView loginPage(@RequestParam("userName") String userName, @RequestParam("password") String password,
			@RequestParam(name = "isMobile", required = false) Integer isMobile, HttpServletRequest request,
			HttpServletResponse response, Model model1) {
		ModelAndView model = new ModelAndView();
		HttpSession session1 = request.getSession(true);
		String setYear=null;
		session1.invalidate();
		if (model1.containsAttribute("user")) {
			model1.asMap().remove("user");
		}
		HttpSession session = request.getSession(true);
		
		if (isMobile != null) {
			session.setAttribute("isMobile", isMobile);
		}
		
		if ((userName == null || userName == "") && (password == null || password == "")) {
			model.addObject("nologin", "Email id is required");
			model.addObject("nopasswd", "Password required");
			
			if (isMobile != null && isMobile.equals(1)) {
				model.setViewName("/mobilehomelanding");
			} else {
				model.setViewName("/home-landing");
			}
			
		} else if (userName == null || userName == "") {
			model.addObject("nologin", "Email id is required");
			model.addObject("passwordval", password);
			if (isMobile != null && isMobile.equals(1)) {
				model.setViewName("/mobilehomelanding");
			} else {
				model.setViewName("/home-landing");
			}
		} else if (!userName.matches(EMAIL_PATTERN)) {
			model.addObject("nologin", "Enter valid email id");
			model.addObject("passwordval", password);
			if (isMobile != null && isMobile.equals(1)) {
				model.setViewName("/mobilehomelanding");
			} else {
				model.setViewName("/home-landing");
			}
		} else if (password == null || password == "") {
			model.addObject("userval", userName);
			model.addObject("nopasswd", "Password required");
			if (isMobile != null && isMobile.equals(1)) {
				model.setViewName("/mobilehomelanding");
			} else {
				model.setViewName("/home-landing");
			}
		} else {
			
			Integer flag = userService.authenticate(userName, password);
			if (flag == 1) {
				String remoteAddr = "";
				remoteAddr = request.getHeader("X-FORWARDED-FOR");
				if (remoteAddr == null || "".equals(remoteAddr)) {
					remoteAddr = request.getRemoteAddr();
				}
				
				User user = userService.loadUserByUsername(userName,password);
				loginService.create(user.getUser_id(), 1, remoteAddr);
				Company company = user.getCompany();
				List<AccountingYear> yearlist = new ArrayList<AccountingYear>();
				company = companyService.findOneWithAll(company.getCompany_id()); // for company statutary type and
																					// industry type
				session.setAttribute("user", user);
				session.setAttribute("is_updated", user.getIs_updated());
				session.setAttribute("app_url", "${app.logourl}");
				session.setAttribute("user_id", user.getUser_id());
				// for log in throgh mobile
				// isMobile=1;
				
				
				String yearId = company.getYearRange();
				if (yearId != null) {
					String[] years = yearId.split(",");
					List<Long> yearIds = new ArrayList<Long>();
					for (String yId : years) {
						yearIds.add(new Long(yId));
					}
					for (Long id : yearIds) {
						try {
							yearlist.add(accountingYearService.getById(id));
						} catch (MyWebException e) {
							e.printStackTrace();
						}
					}
				}
				if (company.getBank() != null)
					session.setAttribute("compbank_id", company.getBank().getBank_id());
				else {
					session.setAttribute("compbank_id", 0);
					notifications.removeAll(notifications);
					//notifications.add(new Notification("Set Default Bank",
						//	"You have not set default bank yet, please set the default bank", "bankList"));
					//session.setAttribute("notifications", notifications);
				}

				session.setAttribute("company_id", user.getCompany().getCompany_id());
				session.setAttribute("company_name", user.getCompany().getCompany_name());
				session.setAttribute("company_logo", company.getLogo());
				session.setAttribute("role", user.getRole().getRole_id());
				session.setAttribute("rolename", user.getRole().getRole_name());
				session.setAttribute("username", (user.getFirst_name() + " " + user.getLast_name()));
				List<AccountingYear>  yearList = accountingYearService.findAccountRange(user.getUser_id(), user.getCompany().getYearRange(),user.getCompany().getCompany_id());
				//List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
				System.out.println("yearList "+yearList.size());
				for(AccountingYear a :yearList){
					System.out.println("accounting year id is " +a.getYear_range());
				}
				if(yearList.size()==1){
					System.out.println("set yearList ");
					session.setAttribute("AccountingYear",(long)-1);
				}
				if(null == session.getAttribute("SetYear")){  
					
					 setYear="NA";
					}else{setYear="true";}
				model.addObject("yearList", yearList);
				model.addObject("setYear",setYear);
				
				if (!user.getIs_updated()) {
					List<Country> countryList = countryService.findAll();
					List<State> stateList = stateService.findAll();
					List<City> cityList = cityService.findAll();
					List<CompanyStatutoryType> companyTypeList = companyTypeService.findAll();
					List<IndustryType> industryTypeList = industryTypeService.findAll();
					List<AccountingYear> yearRangeList = accountingYearService.findAll();
					UserForm userform = new UserForm();
					userform.setUser(user);
					company.setCompanyTypeId(company.getCompany_statutory_type().getCompany_statutory_id());
					company.setIndustryTypeId(company.getIndustry_type().getIndustry_id());
					if (company.getCountry() != null)
						company.setCountryId(company.getCountry().getCountry_id());
					if (company.getState() != null)
						company.setStateId(company.getState().getState_id());
					if (company.getCity() != null)
						company.setCityId(company.getCity().getCity_id());
					userform.setCompany(company);
					model.addObject("yearRangeList", yearRangeList);
					model.addObject("companyTypeList", companyTypeList);
					model.addObject("industryTypeList", industryTypeList);
					model.addObject("countryList", countryList);
					model.addObject("stateList", stateList);
					model.addObject("cityList", cityList);
					model.addObject("userform", userform);
					model.addObject("user", user);
					model.setViewName("master/user");
					model.addObject("yearlist", yearlist);
				} else {
					long role = (long) session.getAttribute("role");
					long CompanyId = (long) session.getAttribute("company_id");
					if (role == ROLE_SUPERUSER) {
					
						Integer productlist = productService.findAllDashboard();
						Integer bankList = bankService.findAllDashboard();
						Integer ledgerList = ledgerService.findAllDashboard();
						/* Integer quoteList=quoteService.findAllDashboard(); */
						Integer subledgerCount = subLedgerService.findAllDashboard();
						Integer supplierCount = supplierService.findAllSupplierCountDashboard();
						Integer customerCount = customerService.findAllCustomerCountDashboard();
						CopyOnWriteArrayList<Quotation> list = quoteService.findAllQuotations();
						List<ClientSubscriptionMaster> listall = quoteService.findAllSubscriptionList();
						for (ClientSubscriptionMaster clientmaster : listall) {

							if (clientmaster.getQuotation_id() != null) {
								for (Quotation quotation : list) {
									if (clientmaster.getQuotation_id().getQuotation_id()
											.equals(quotation.getQuotation_id())) {
										list.remove(quotation);
									}
								}
							}
						}

						model.addObject("ledgerList", ledgerList);
						model.addObject("bankList", bankList);
						model.addObject("quoteList", list.size());
						model.addObject("productlist", productlist);
						model.addObject("subledgerCount", subledgerCount);
						model.addObject("supplierCount", supplierCount);
						model.addObject("customerCount", customerCount);
					} else if (role == ROLE_EXECUTIVE) {
						Integer productlist = productService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
								user.getUser_id());
						Integer bankList = bankService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
								user.getUser_id());
						Integer ledgerList = ledgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
								user.getUser_id());
						Integer subledgerCount = subLedgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
								user.getUser_id());
						Integer supplierCount = supplierService.findByStatusSupplierCountDashboard(role,
								APPROVAL_STATUS_PENDING, user.getUser_id());
						Integer customerCount = customerService.findByStatusCustomerCountDashboard(role,
								APPROVAL_STATUS_PENDING, user.getUser_id());

						model.addObject("ledgerList", ledgerList);
						model.addObject("bankList", bankList);
						model.addObject("productlist", productlist);
						model.addObject("subledgerCount", subledgerCount);
						model.addObject("supplierCount", supplierCount);
						model.addObject("customerCount", customerCount);
					} else if (role == ROLE_MANAGER) {
						Integer productlist = productService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
								user.getUser_id());
						Integer bankList = bankService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
								user.getUser_id());
						Integer ledgerList = ledgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
								user.getUser_id());
						Integer subledgerCount = subLedgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING,
								user.getUser_id());
						Integer supplierCount = supplierService.findByStatusSupplierCountDashboard(role,
								APPROVAL_STATUS_PENDING, user.getUser_id());
						Integer customerCount = customerService.findByStatusCustomerCountDashboard(role,
								APPROVAL_STATUS_PENDING, user.getUser_id());

						model.addObject("ledgerList", ledgerList);
						model.addObject("bankList", bankList);
						model.addObject("productlist", productlist);
						model.addObject("subledgerCount", subledgerCount);
						model.addObject("supplierCount", supplierCount);
						model.addObject("customerCount", customerCount);
					} else {

						Integer productlist = productService.findAllProductsOfCompanyDashboard(CompanyId);
						Integer bankList = bankService.findAllBanksOfCompanyDashboard(CompanyId);
						Integer ledgerList = ledgerService.findAllLedgersOfCompanyDashboard(CompanyId);
						/* Integer empList = userService.getActiveUserByCompanyDashboard(CompanyId); */
						Integer subledgerCount = subLedgerService.findAllSubLedgerOfCompanyDashboard(CompanyId);
						Integer supplierCount = supplierService.findAllSupplierCountOfCompanyDashboard(CompanyId);
						Integer customerCount = customerService.findAllCustomerCountOfCompanyDashboard(CompanyId);

						model.addObject("closingBalanceOfCashInHand",
								subLedgerService.getClosingBalanceOfCashInHand(CompanyId));
						model.addObject("closingBalanceOfAllBanks", bankService.getClosingBalanceOfAllbanks(CompanyId));
						model.addObject("totalBillsReceivable", entryService.getTotalbillsBillsReceivable(CompanyId));
						model.addObject("totalBillsPayable", purEntryService.getTotalBillsPayable(CompanyId));
						model.addObject("ledgerList", ledgerList);
						model.addObject("bankList", bankList);
						/* model.addObject("empList", empList); */
						model.addObject("productlist", productlist);
						model.addObject("subledgerCount", subledgerCount);
						model.addObject("supplierCount", supplierCount);
						model.addObject("customerCount", customerCount);
					}
					if (user.getRole().getRole_id() == ROLE_EMPLOYEE) {
						LocalDate lastDate = forgotPasswordLogService.getLastPasswordUpdateDate(user.getUser_id());
						
						LocalDate today = new LocalDate();
						if (lastDate != null) {
							if (today.compareTo(lastDate.plusDays(27)) >= 0) {
								if (today.compareTo(lastDate.plusDays(30)) >= 0) {
									model.addObject("expired", "expired");
								} else {
									model.addObject("reminder", "reminder");
								}
							}
						}
					}
					
					if (isMobile != null && isMobile.equals(1)) {
						model.setViewName("/mobilehomepage");
					} else {
						model.setViewName("/homePage");
					}
					

				}
			} else if (flag == -2) {
				model.addObject("passwordval", password);
				model.addObject("error", "Company is not yet registered with fasset!");
				model.addObject("nologin", "Company is not yet registered with fasset!");
				if (isMobile != null && isMobile.equals(1)) {
					model.setViewName("/mobilehomelanding");
				} else {
					model.setViewName("/home-landing");
				}
			} else if (flag == -3) {
				User user = userService.loadUserByUsername(userName,password);
				if (user.getRole().getRole_id() == ROLE_CLIENT) {
					model.addObject("passwordval", password);
					model.addObject("error", "Subscription for Fasset has expired!<br><a href = \"subscribe?cId="
							+ user.getCompany().getCompany_id() + "\">Click here</a> for subscripe again");
					model.addObject("nologin", "Subscription for Fasset has expired!");
				} else if (user.getRole().getRole_id() == ROLE_TRIAL_USER) {
					model.addObject("passwordval", password);
					model.addObject("error",
							"Subscription for Fasset has expired, please signup for using our service!");
					model.addObject("nologin", "");
				} else if (user.getRole().getRole_id() == ROLE_EMPLOYEE
						|| user.getRole().getRole_id() == ROLE_AUDITOR) {
					model.addObject("passwordval", password);
					model.addObject("error", "Subscription for Fasset has expired!");
					model.addObject("nologin", "Subscription for Fasset has expired!");
				}
				if (isMobile != null && isMobile.equals(1)) {
					model.setViewName("/mobilehomelanding");
				} else {
					model.setViewName("/home-landing");
				}
			} else if (flag == 0) {
				model.addObject("passwordval", password);
				model.addObject("error", "User is not registered with fasset!");
				model.addObject("nologin", "User is not registered with fasset!");
				if (isMobile != null && isMobile.equals(1)) {
					model.setViewName("/mobilehomelanding");
				} else {
					model.setViewName("/home-landing");
				}
			} else {
				model.addObject("userval", userName);
				model.addObject("error", "Please insert correct password!");
				model.addObject("nopasswd", "Password Is Incorrect");
				if (isMobile != null && isMobile.equals(1)) {
					model.setViewName("/mobilehomelanding");
				} else {
					model.setViewName("/home-landing");
				}
			}
		}
		return model;
	}
	@RequestMapping(value="setAccountingYear", method=RequestMethod.GET)
	public @ResponseBody Boolean  setAccountingYear(@RequestParam("id")long id ,HttpServletRequest request, HttpServletResponse response)
	{
		
		 System.out.println("id is "+id);
		  Boolean success = true; 
		  HttpSession session = request.getSession(true);
		  session.setAttribute("AccountingYear",id);
		session.setAttribute("SetYear", true);
		return success;
	
	}
	@Transactional
	@RequestMapping(value = LOGOUT, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView logoutPage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "expiredPassword", required = false) Integer expiredPassword, Model model) {
		ModelAndView model1 = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("user_id") != null) {
			long user_Id = (long) session.getAttribute("user_id");
			String remoteAddr = "";
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
			loginService.create(user_Id, 2, remoteAddr);
			session.invalidate();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
			if (expiredPassword != null) {
				Ask4Mail mail = new Ask4Mail();
				model1.addObject("email", mail);
				model1.setViewName("/login/forgotPasswd");
			} else {
				if (model.containsAttribute("user")) {
					model.asMap().remove("user");
				}
				model1.setViewName("/home-landing");

				// model1.setViewName("/home-landing");
			}
		} else {
			model1.setViewName("/home-landing");

			// model1.setViewName("/home-landing");
		}
		return model1;
	}

	@Transactional
	@RequestMapping(value = LOGOUTMobile, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView logoutPageMobile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "expiredPassword", required = false) Integer expiredPassword, Model model) {
		ModelAndView model1 = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("user_id") != null) {
			long user_Id = (long) session.getAttribute("user_id");
			String remoteAddr = "";
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
			loginService.create(user_Id, 2, remoteAddr);
			session.invalidate();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
			if (expiredPassword != null) {
				Ask4Mail mail = new Ask4Mail();
				model1.addObject("email", mail);
				model1.setViewName("/login/forgotPasswd");
			} else {
				if (model.containsAttribute("user")) {
					model.asMap().remove("user");
				}

				model1.setViewName("/mobilehomelanding");
			}
		} else {
			model1.setViewName("/mobilehomelanding");
		}
		return model1;
	}

	@RequestMapping(value = FORGOT, method = RequestMethod.GET)
	public ModelAndView forgotPass(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Login Reset");
		ModelAndView model = new ModelAndView();
		Ask4Mail mail = new Ask4Mail();
		model.addObject("email", mail);
		model.setViewName("/login/forgotPasswd");
		return model;
	}
	
	@RequestMapping(value = FORGOT, method = RequestMethod.POST)
	public String sendLink(@ModelAttribute("email") Ask4Mail mail, HttpServletRequest request,
			HttpServletResponse response, BindingResult result, ModelMap model) {

		ask4MailValidator.validate(mail, result);

		if (result.hasErrors()) {
			return "/login/forgotPasswd";
		} else {
			try {
				User sf = userService.loadUserByUsername(mail.getLogin(),"");
				System.out.println("Forgot passwd");
				userService.sendChangedPass(sf);
				model.addAttribute("message", "Please check your email for reseting password.");
				model.addAttribute("headTitle", "Reset Password");
				model.addAttribute("nextForm", "login");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "/commons/continueProc";
	}

	@RequestMapping(value = "changePassword", method = RequestMethod.GET)
	public ModelAndView changePassword(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		ModelAndView model = new ModelAndView();
		model.addObject("changePass", new ChangePassword());

		if ((Integer) session.getAttribute("isMobile") != null) {
			model.setViewName("/login/mobileChangePassword");

			// add view here (viewMobileSalesEntry)
		} else {
			model.setViewName("/login/changePassword");
		}
		// model.setViewName("/login/changePassword");
		return model;
	}

	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	public ModelAndView changePassword(@ModelAttribute("changePass") ChangePassword changePass,
			HttpServletRequest request, HttpServletResponse response, BindingResult result) {

		HttpSession session = request.getSession();
		Long userId = (Long) session.getAttribute("user_id");

		User user = new User();
		try {
			user = userService.getById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView model = new ModelAndView();
		changePass.setUserName(user.getEmail());
		changePasswordValidator.validate(changePass, result);
		if (result.hasErrors()) {
			if ((Integer) session.getAttribute("isMobile") != null) {
				model.setViewName("/login/mobileChangePassword");

				// add view here (viewMobileSalesEntry)
			} else {
				model.setViewName("/login/changePassword");
			}
			// model.setViewName("/login/mobileChangePassword");

			// model.setViewName("/login/changePassword");
			return model;
		} else {

			user.setPassword(Md5.md5(changePass.getPass()));
			ForgotPasswordLog forgotPasswordLog = new ForgotPasswordLog();
			forgotPasswordLog.setDate(new LocalDate());
			forgotPasswordLog.setType(PASSWORD_CHANGE);
			forgotPasswordLog.setUser(user);
			Set<ForgotPasswordLog> forgotPasswordLogSet = new HashSet<ForgotPasswordLog>();
			forgotPasswordLogSet.add(forgotPasswordLog);
			user.setForgotPassword(forgotPasswordLogSet);
			userService.changePassword(user);
			/* model.setViewName("/homePage"); */

			if ((Integer) session.getAttribute("isMobile") != null) {
				return new ModelAndView("redirect:/homePage");

				// add view here (viewMobileSalesEntry)
			} else {
				return new ModelAndView("redirect:/homePage");
			}

			// return new ModelAndView("redirect:/homePage");
		}

	}

	@RequestMapping(value = CHANGE_PWD, method = RequestMethod.GET)
	public ModelAndView changePWD(@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "key", required = true) String genKey) {
		ModelAndView model = new ModelAndView();
		try {
			System.out.println("change pwd 1 " +email);
			
			User sf = userService.loadUserByUsername(email,"");
			if ((null != sf) && (sf.getEmail_varification_code().equals(genKey))) {
				ChangePassword pwd = new ChangePassword();
				pwd.setUserName(email);
				 List<User> users=userService.getUserByMail(email);
				
				model.addObject("changePass", pwd);
				model.addObject("companyList", users);
				model.setViewName("login/changePWD");
			} else {
				model.addObject(HEAD_TITLE, getMess("P3.title.error"));
				model.addObject(MESSAGE, getMess("P2.erro.mess.1"));
				model.addObject(NEXT_FORM, SIGNEDUP);
				model.setViewName(CONTINUE_PROC);
			}
		} catch (Exception e) {
			model.addObject(HEAD_TITLE, getMess("P3.title.error"));
			model.addObject(MESSAGE, "");
			model.addObject(NEXT_FORM, SIGNEDUP);
			model.setViewName(CONTINUE_PROC);
		}

		return model;
	}

	@RequestMapping(value = CHANGE_PWD, method = RequestMethod.POST)
	public ModelAndView changePWD(@ModelAttribute("changePass") ChangePassword changePass, HttpServletRequest request,
			HttpServletResponse response, BindingResult result) {

		ModelAndView model = new ModelAndView();
		passwordFormValidator.validate(changePass, result);
		System.out.println("change pwd 2");
		System.out.println("change pwd 2 company id "+changePass.getCompany_id());
		if (result.hasErrors()) {
			List<User> users=userService.getUserByMail(changePass.getUserName());
			model.addObject("companyList", users);
			model.setViewName("login/changePWD");
			return model;
		} else {
			try {
			//	User user = userService.loadUserByUsername(changePass.getUserName(),changePass.getPass());
				User user=userService.getUserByUserName(changePass.getCompany_id(), changePass.getUserName());
				
				user.setPassword(Md5.md5(changePass.getPass()));
				ForgotPasswordLog forgotPasswordLog = new ForgotPasswordLog();
				forgotPasswordLog.setDate(new LocalDate());
				forgotPasswordLog.setType(PASSWORD_FORGOT);
				forgotPasswordLog.setUser(user);
				Set<ForgotPasswordLog> forgotPasswordLogSet = new HashSet<ForgotPasswordLog>();
				forgotPasswordLogSet.add(forgotPasswordLog);
				user.setForgotPassword(forgotPasswordLogSet);
				
				userService.changePassword(user);
				model.addObject(HEAD_TITLE, getMess("P3.change"));
				model.addObject(MESSAGE, getMess("P3.change.mess"));
				model.addObject(NEXT_FORM, LOGIN);
				model.setViewName(CONTINUE_PROC);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return model;
		}
	}

	@RequestMapping(value = "getmenu", method = RequestMethod.GET)
	public @ResponseBody List<MenuMaster> getmenu(@RequestParam(value = "role") long role, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		System.out.println("getting menu");
		List<MenuMaster> menuList = menuAccessMasterService.getMenuList(user.getUser_id(), role);
		return menuList;
	}

	@RequestMapping(value = "getActionAccess", method = RequestMethod.POST)
	public @ResponseBody List<MenuAccessMaster> getActionAccess(@RequestParam("menuid") int menuid,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		long user_Id = (long) session.getAttribute("user_id");
		long role_Id = (long) session.getAttribute("role");
		List<MenuAccessMaster> menuList = menuAccessMasterService.findmenuacccess(user_Id, menuid, role_Id);
		if (session.getAttribute("menuList") == null) {
			session.setAttribute("menuList", menuList);
		}
		return menuList;

	}
	
	/////////////// For SuperUser dashboard

	// Inactive clients list
	@RequestMapping(value = "inactiveClientList", method = RequestMethod.GET)
	public ModelAndView inactiveClientList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");

		if (role == ROLE_SUPERUSER) {
			model.addObject("inactiveClientList", companyService.FindAllInactiveCompanies(role, user.getUser_id()));
		} else if (role == ROLE_MANAGER) {

			model.addObject("inactiveClientList", companyService.FindAllInactiveCompanies(role, user.getUser_id()));
		} else if (role == ROLE_EXECUTIVE) {
			model.addObject("inactiveClientList", companyService.FindAllInactiveCompanies(role, user.getUser_id()));
		}
		model.setViewName("KPO/inactiveClientList");
		return model;
	}

	@RequestMapping(value = "viewApprovals", method = RequestMethod.GET)
	public ModelAndView viewApprovals(@RequestParam("flag") Long flag, HttpServletRequest request,
			HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		approvalList.clear();

		if (role == ROLE_SUPERUSER) {
			if (flag.equals((long) 1)) {
				approvalList = ledgerService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 1);

			}
			if (flag.equals((long) 2)) {
				approvalList = subLedgerService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 2);

			}
			if (flag.equals((long) 3)) {
				approvalList = productService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 3);

			}
			if (flag.equals((long) 4)) {
				approvalList = customerService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 4);

			}
			if (flag.equals((long) 5)) {
				approvalList = supplierService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 5);

			}
			if (flag.equals((long) 6)) {
				approvalList = bankService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 6);

			}
		}
		if (role == ROLE_MANAGER) {

			if (flag.equals((long) 1)) {
				approvalList = ledgerService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 1);

			}
			if (flag.equals((long) 2)) {
				approvalList = subLedgerService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 2);

			}
			if (flag.equals((long) 3)) {
				approvalList = productService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 3);

			}
			if (flag.equals((long) 4)) {
				approvalList = customerService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 4);

			}
			if (flag.equals((long) 5)) {
				approvalList = supplierService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 5);

			}
			if (flag.equals((long) 6)) {
				approvalList = bankService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 6);

			}
		}
		if (role == ROLE_EXECUTIVE) {
			if (flag.equals((long) 1)) {
				approvalList = ledgerService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 1);

			}
			if (flag.equals((long) 2)) {
				approvalList = subLedgerService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 2);

			}
			if (flag.equals((long) 3)) {
				approvalList = productService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 3);

			}
			if (flag.equals((long) 4)) {
				approvalList = customerService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 4);

			}
			if (flag.equals((long) 5)) {
				approvalList = supplierService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 5);

			}
			if (flag.equals((long) 6)) {
				approvalList = bankService.viewApprovals(role, user.getUser_id());
				session.setAttribute("approval", (int) 6);

			}
		}
		if ((Integer) session.getAttribute("isMobile") != null) {
			return new ModelAndView("redirect:/homePage");

			// add view here (viewMobileSalesEntry)
		} else {
			return new ModelAndView("redirect:/homePage");
		}
		// return new ModelAndView("redirect:/homePage");
	}

	/// for Mobile Home PAge 23april
	@RequestMapping(value = "mobilehomepage", method = RequestMethod.GET)
	public ModelAndView mobilehomepage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		model.setViewName("/transactions/mobile/mobilehomepage");
		return model;
	}

	/// for Mobile Home PAge 22april
	@RequestMapping(value = "mobilehomelandingpage", method = RequestMethod.GET)
	public ModelAndView mobilehomelandingpage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		model.setViewName("/transactions/mobile/mobilehomelanding");
		return model;
	}

}
