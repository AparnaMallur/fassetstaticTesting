package com.fasset.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
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

import com.ccavenue.security.Md5;
import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.UserFormValidator;
import com.fasset.controller.validators.UserValidator;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Bank;
import com.fasset.entities.City;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Company;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.entities.Country;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Customer;
import com.fasset.entities.IndustryType;
import com.fasset.entities.JavaMD5Hash;
import com.fasset.entities.Ledger;
import com.fasset.entities.Product;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.Receipt;
import com.fasset.entities.Role;
import com.fasset.entities.State;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.UserForm;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ICityService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICompanyStatutoryTypeService;
import com.fasset.service.interfaces.ICountryService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IForgotPasswordLogService;
import com.fasset.service.interfaces.IFrequencyService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IRoleService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.IServiceMaster;
import com.fasset.service.interfaces.IStateService;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IindustryTypeService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.fasset.service.interfaces.IProductService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.IQuotationService;

//user Controller
@Controller
@SessionAttributes("user")
public class UserController extends MyAbstractController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private ICountryService countryService;

	@Autowired
	private IStateService stateService;
	
	@Autowired
	private IServiceMaster ServiceMaster;
	
	@Autowired
	private IFrequencyService frequencyService;
	
	@Autowired
	private ICityService cityService;

	@Autowired
	private ISuplliersService supplierService;

	@Autowired
	private IQuotationService quoteService;

	@Autowired
	private ILedgerService ledgerService;

	@Autowired
	private ICompanyStatutoryTypeService companyTypeService;

	@Autowired
	private IindustryTypeService industryTypeService;

	@Autowired
	private IForgotPasswordLogService forgotPasswordLogService;

	@Autowired
	private IAccountingYearService accountingYearService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IBankService bankService;

	@Autowired
	private UserFormValidator userFormValidator;

	@Autowired
	private UserValidator UserValidator;

	@Autowired
	private ISubLedgerService subLedgerService;

	@Autowired
	private IProductService productService;

	@Autowired	
	private IClientSubscriptionMasterService SubscriptionService;	
	
	@Autowired
	private IPurchaseEntryService purEntryService ;
    @Autowired
	private ISalesEntryService entryService ;

    private Long company_id;
     
	@RequestMapping(value = "companyList", method = RequestMethod.GET)
	public ModelAndView companyList(@RequestParam(value = "msg", required = false) String msg,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		Long role = (Long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		
		if (role.equals(ROLE_SUPERUSER) ) {
			model.addObject("companyList", companyService.FindAlllist());			
		}
		else if ((role.equals(ROLE_EXECUTIVE)) || (role.equals(ROLE_MANAGER))) {
			model.addObject("companyList", companyService.FindAlllistexe(user.getUser_id()));	
		}
		else
		{
			model.addObject("companyList", null);
		}
		model.setViewName("master/companyList");
		return model;
	}

	@RequestMapping(value = "viewCompany", method = RequestMethod.GET)
	public ModelAndView viewCompany(@RequestParam("id") Long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		ClientSubscriptionMaster obj= SubscriptionService.getClientSubscriptionByCompanyId(id);
		if(obj!=null)
		{
		if(obj.getQuotation_id() != null) {
			model.addObject("quotationDetails", obj.getQuotation_id().getQuotationDetails());
		}
		}
		Company comp = companyService.findOneWithAll(id);
		model.addObject("company",comp );
		model.addObject("yearRangeList", accountingYearService.findAccountRange(null, comp.getYearRange(),id));
		model.setViewName("master/companyView");
		return model;
	}

	@RequestMapping(value = "company", method = RequestMethod.GET)
	public ModelAndView company(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		Company company = new Company();
user.setUser_id(null);
		
		//user.setUser_id((long) '0');
		UserForm userform = new UserForm();
		userform.setCompany(company);
		
		userform.setUser(user);
		model.addObject("companyTypeList", companyTypeService.findAll());
		model.addObject("industryTypeList", industryTypeService.findAll());
		model.addObject("countryList", countryService.findAll());
		model.addObject("stateList", stateService.findAll());
		model.addObject("serviceList", ServiceMaster.findAll());
		model.addObject("frequencyServiceList", frequencyService.findAll());
		model.addObject("cityList", cityService.findAll());
		model.addObject("yearRangeList", accountingYearService.findAll());
		model.addObject("quotationDetails", new HashSet<QuotationDetails>());
		model.addObject("userform", userform);
		
		model.setViewName("master/company");
		return model;
	}

	@RequestMapping(value = "editCompany", method = RequestMethod.GET)
	public ModelAndView editCompany(@RequestParam("id") Long id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		ModelAndView model = new ModelAndView();
		User user = new User();
		User user1 = new User();
		User user2 = new User();
		User user3 = new User();
		Company company = new Company();
		company_id=id;
		System.out.println("Edting");
		try {
			user1 = userService.getUser(id, ROLE_CLIENT);
			user2 = userService.getUser(id, ROLE_TRIAL_USER);
			user3 = userService.getUser(id, ROLE_SUPERUSER);
			if(user1== null && user3==null)
			{
				user= user2;
			}
			else if(user2== null && user3==null)
			{
				user =user1;
			}
			else
			{
				user=user3;
				user.setAmount("0");
			}
			
			company = companyService.findOneWithAll(id);
			if (company.getCompany_statutory_type() != null) {
				company.setCompanyTypeId(company.getCompany_statutory_type().getCompany_statutory_id());
			}
			if (company.getIndustry_type() != null) {
				company.setIndustryTypeId(company.getIndustry_type().getIndustry_id());
			}
			if (company.getCity() != null) {
				company.setCityId(company.getCity().getCity_id());
			}
			if (company.getState() != null) {
				company.setStateId(company.getState().getState_id());
			}
			if (company.getCountry() != null) {
				company.setCountryId(company.getCountry().getCountry_id());
			}
			if (company.getYearRange() != null) {
				System.out.println("Range "+company.getYearRange());
				company.setYearRange(company.getYearRange());
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		
		ClientSubscriptionMaster obj= SubscriptionService.getClientSubscriptionByCompanyId(id);
		if(obj!=null)
		{
		if(obj.getQuotation_id() != null) {
			model.addObject("quotationDetails", obj.getQuotation_id().getQuotationDetails());
			
		}
		}
		user.setPassword(Md5.decrypt(user.getPassword()));
		
	//	user.setAmount(user.getAmount());
		
		UserForm userform = new UserForm();
		userform.setUser(user);
		userform.setCompany(company);
		model.addObject("companyTypeList", companyTypeService.findAll());
		model.addObject("industryTypeList", industryTypeService.findAll());
		model.addObject("countryList", countryService.findAll());
		model.addObject("stateList", stateService.findAll());
		model.addObject("cityList", cityService.findAll());
		model.addObject("serviceList", ServiceMaster.findAll());
		model.addObject("frequencyServiceList", frequencyService.findAll());
		model.addObject("yearRangeList",  accountingYearService.findAll());
		model.addObject("userform", userform);
		model.addObject("bankList",  bankService.findAllBanksOfCompany(id));
		model.setViewName("master/company");
		return model;
	}
	
	/*	HttpSession session = request.getSession(true);
		ModelAndView model = new ModelAndView();
		User user = new User();
		Company company = new Company();
		company_id=id;
		try {
			user = userService.getUser(id, ROLE_CLIENT);
			company = companyService.findOneWithAll(id);
			if (company.getCompany_statutory_type() != null) {
				company.setCompanyTypeId(company.getCompany_statutory_type().getCompany_statutory_id());
			}
			if (company.getIndustry_type() != null) {
				company.setIndustryTypeId(company.getIndustry_type().getIndustry_id());
			}
			if (company.getCity() != null) {
				company.setCityId(company.getCity().getCity_id());
			}
			if (company.getState() != null) {
				company.setStateId(company.getState().getState_id());
			}
			if (company.getCountry() != null) {
				company.setCountryId(company.getCountry().getCountry_id());
			}
			if (company.getYear_range() != null) {
				company.setYearRangeId(company.getYear_range().getYear_id());
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			user = userService.getUser(id, ROLE_TRIAL_USER);
			company = companyService.findOneWithAll(id);
			if (company.getCompany_statutory_type() != null) {
				company.setCompanyTypeId(company.getCompany_statutory_type().getCompany_statutory_id());
			}
			if (company.getIndustry_type() != null) {
				company.setIndustryTypeId(company.getIndustry_type().getIndustry_id());
			}
			if (company.getCity() != null) {
				company.setCityId(company.getCity().getCity_id());
			}
			if (company.getState() != null) {
				company.setStateId(company.getState().getState_id());
			}
			if (company.getCountry() != null) {
				company.setCountryId(company.getCountry().getCountry_id());
			}
			if (company.getYear_range() != null) {
				company.setYearRangeId(company.getYear_range().getYear_id());
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		if(user.getRole().getRole_id()!=MyAbstractController.ROLE_TRIAL_USER)
		{
		ClientSubscriptionMaster obj= SubscriptionService.getClientSubscriptionByCompanyId(id);
		
		if(obj.getQuotation_id() != null ) {
			model.addObject("quotationDetails", obj.getQuotation_id().getQuotationDetails());
		}
		}
		user.setPassword(Md5.decrypt(user.getPassword()));
		UserForm userform = new UserForm();
		userform.setUser(user);
		userform.setCompany(company);
		model.addObject("companyTypeList", companyTypeService.findAll());
		model.addObject("industryTypeList", industryTypeService.findAll());
		model.addObject("countryList", countryService.findAll());
		model.addObject("stateList", stateService.findAll());
		model.addObject("cityList", cityService.findAll());
		model.addObject("serviceList", ServiceMaster.findAll());
		model.addObject("frequencyServiceList", frequencyService.findAll());
		model.addObject("yearRangeList",  accountingYearService.findAll());
		model.addObject("userform", userform);
		model.addObject("bankList",  bankService.findAllBanksOfCompany(id));
		model.setViewName("master/company");
		return model;
	}*/

	@RequestMapping(value ="deleteServicedetail", method = RequestMethod.GET)
	public synchronized ModelAndView deleteService(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response) 
	{
		HttpSession session = request.getSession(true);
		String msg="";
		try
		{
			msg=quoteService.deleteService(id);
		
		session.setAttribute("msg", msg);
		}
		catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", msg);
		}
		 return new ModelAndView("redirect:/editCompany?id="+company_id);
	}
	
	@RequestMapping(value="saveCompany", method = RequestMethod.POST)
	public ModelAndView saveCompany(@ModelAttribute("userform") UserForm userForm, @RequestParam("logo") MultipartFile logo,
			HttpServletRequest request, HttpServletResponse response, BindingResult result) {
		System.out.println("The saving of comp 21Apr");
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		ModelAndView model = new ModelAndView();
		
		userFormValidator.validate(userForm, result);
		String filePath = "";
		String msg = "";
		try {
			filePath = request.getServletContext().getRealPath("resources");
			filePath += "/images/logo/";
			System.out.println("The logo path is "+filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (result.hasErrors()) {
			ClientSubscriptionMaster obj= SubscriptionService.getClientSubscriptionByCompanyId(userForm.getCompany().getCompany_id());
			if(obj!=null && obj.getQuotation_id() != null) {
				model.addObject("quotationDetails", obj.getQuotation_id().getQuotationDetails());
				
			}
			model.addObject("yearRangeList", accountingYearService.findAll());
			model.addObject("companyTypeList", companyTypeService.findAll());
			model.addObject("industryTypeList",  industryTypeService.findAll());
			model.addObject("serviceList", ServiceMaster.findAll());
			model.addObject("frequencyServiceList", frequencyService.findAll());
			model.addObject("countryList", countryService.findAll());
			model.addObject("stateList", stateService.findAll());
			model.addObject("cityList", cityService.findAll());
			model.addObject("bankList", bankService.findAllBanksOfCompany(userForm.getCompany().getCompany_id()));
			model.setViewName("/master/company");
		} else {
		
			try {
				byte[] bytes = logo.getBytes();
				System.out.println(bytes.length);
				if (bytes.length > 0) {
					Path path = Paths.get(filePath + logo.getOriginalFilename());
					
					Files.write(path, bytes);
					userForm.getCompany().setLogo(logo.getOriginalFilename());
				}

				if ((userForm.getUser().getUser_id() == null || userForm.getUser().getUser_id() ==0) && userForm.getCompany().getCompany_id() == null) {
					userForm.getCompany().setCreated_by(user);// for company registered without quotation flow. This will be use for master imports as quotation is not present for this company.
					System.out.println("userId" +userForm.getUser().getUser_id())	;
					companyService.addCompany(userForm);
					msg = "User added successfully";
				} else {
					userForm.getUser().setCity_id(userForm.getCompany().getCityId());
					userForm.getUser().setState_id(userForm.getCompany().getStateId());
					userForm.getUser().setCountry_id(userForm.getCompany().getCountryId());
					
					if((userForm.getCompany().getStatus()==MyAbstractController.STATUS_INACTIVE)||(userForm.getCompany().getStatus()==MyAbstractController.STATUS_PENDING_FOR_APPROVAL))
					{
						userForm.getUser().setStatus(false);
					}
					else
					{
						userForm.getUser().setStatus(true);
					}
					
					userService.updatebycomp(userForm.getUser());
					Company company = new Company();
					company = userForm.getCompany();
					company.setMobile(userForm.getUser().getMobile_no());
					//userForm.getCompany().setUpdated_by(user);
					companyService.updatebycomp(company,userForm.getUser().getAmount());
					msg = "User updated successfully";
				}
			} catch (MyWebException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/companyList");
		}
		
		return model;
	}
	
	
	

	@RequestMapping(value = "employeeList", method = RequestMethod.GET)
	public ModelAndView employeeList(@RequestParam(value = "msg", required = false) String msg,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		Long role = (Long) session.getAttribute("role");
		
		if ((String) session.getAttribute("msg") != null) {
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}

		if ((String) session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", (String) session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		List<User> userList = null;
		
		System.out.println("Role is "+role);
		if ((role == ROLE_CLIENT) || (role == ROLE_EMPLOYEE) || (role == ROLE_AUDITOR) || (role == ROLE_TRIAL_USER)) {
			userList = userService.getUserByCompany(user.getCompany().getCompany_id());
		}
		else if (role == ROLE_SUPERUSER) {
			userList = userService.findallemployeeAndAuditorOfAllCompanies();
		}
		else if ((role == ROLE_EXECUTIVE) || (role ==ROLE_MANAGER)) {
			userList = userService.findallemployeeAndAuditorOfAllCompaniesexe(user.getUser_id());
		}
		if (msg != null) {
			model.addObject("successMsg", msg);
		}
		model.addObject("employeeList", userList);
		model.setViewName("master/employeeList");
		return model;
	}
	

	
	@RequestMapping(value = "employee", method = RequestMethod.GET)
	public ModelAndView employee(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		List<Role> rolelist = new ArrayList<Role>();	
		List<Role> role = roleService.list();
	    boolean acount=false;
	    int emp=0;
	    
	    
	    if (user.getRole().getRole_id().equals(ROLE_SUPERUSER)) {
			List<Company> companyList = companyService.getAllCompaniesOnly();
			for (Role newrole : role) {
				switch (newrole.getRole_name()) {
				case "Auditor":
					rolelist.add(newrole);
					break;				
				case "Employee":
					rolelist.add(newrole);
					break;			
				}
			}
			
			model.addObject("employee", new User());
			model.addObject("acount", acount);
			model.addObject("cityList", cityService.findAll());
			model.addObject("rolelist", rolelist);
			model.addObject("countryList", countryService.findAll());
			model.addObject("stateList", stateService.findAll());
			model.addObject("employee", new User());
			model.addObject("companyList", companyList);
			model.setViewName("master/employee");
		}
		else
		{
		List<User> userList = userService.getActiveUserByCompany(user.getCompany().getCompany_id());
		for (User auditor : userList) {
			
			String aname=auditor.getRole().getRole_name();
			if(aname.equalsIgnoreCase("Auditor"))
			{
				acount=true;				
			}
			else
			{
				emp=emp+1;
			}		
		}
		for (Role newrole : role) {
			switch (newrole.getRole_name()) {
			case "Auditor":
				rolelist.add(newrole);
				break;				
			case "Employee":
				rolelist.add(newrole);
				break;			
			}
		}

		if ((emp < (user.getCompany().getEmpLimit()))) {
			model.addObject("acount", acount);
			model.addObject("cityList", cityService.findAll());
			model.addObject("rolelist", rolelist);
			model.addObject("countryList", countryService.findAll());
			model.addObject("stateList", stateService.findAll());
			model.addObject("employee", new User());
			model.setViewName("master/employee");

		} else {		
			session.setAttribute("errorMsg", "You cant add one more user.");
			model.setViewName("redirect:/employeeList");
		}
		}
		return model;
	}

	
	@RequestMapping(value = "saveEmployee", method = RequestMethod.POST)
	public ModelAndView saveEmployee(@ModelAttribute("employee") User employee, HttpServletRequest request,
			HttpServletResponse response, BindingResult result, Map<String, Object> map) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		long CompanyId=user.getCompany().getCompany_id();
		
		long id = 0;
		String msg = "";
		List<Role> rolelist = new ArrayList<Role>();	
		List<Role> role = roleService.list();
	    boolean acount=false;
	    int emp=0;
	    employee.setCompany_id(CompanyId);
	    System.out.println("user getting saved "+employee.getJoinDate());
	    
		if (user.getRole().getRole_id().equals(ROLE_SUPERUSER)) {
			
			UserValidator.validate(employee, result);
			if (result.hasErrors()) {
				for (Role newrole : role) {
						switch (newrole.getRole_name()) 
						{
							case "Auditor":rolelist.add(newrole);
							break;				
							case "Employee":
							rolelist.add(newrole);
							break;			
						}
				}
				model.addObject("acount", false);
				model.addObject("cityList", cityService.findAll());
				model.addObject("rolelist", rolelist);
				model.addObject("countryList", countryService.findAll());
				model.addObject("stateList", stateService.findAll());
				model.addObject("companyList", companyService.getAllCompaniesOnly());
				model.setViewName("master/employee");
			}
			else
			{
				Company comp = new Company();
				List<User> userList = userService.getActiveUserByCompany(employee.getCompany_id());
				for (User auditor : userList) {
					String aname=auditor.getRole().getRole_name();
					if(aname.equalsIgnoreCase("Auditor"))
					{
						acount=true;				
					}
					else
					{
						emp=emp+1;
					}
				
				}
				
				try {
					comp = companyService.getById(employee.getCompany_id());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				if ((emp > (comp.getEmpLimit()))) {
					
					session.setAttribute("errorMsg", "You cant add one more user as employee limit is exceed for given company.");
					model.setViewName("redirect:/employeeList");	

				} else if(acount==true && employee.getRole_id().equals(ROLE_AUDITOR)){
				
					session.setAttribute("errorMsg", "You cant add one more auditor as auditor is alredy added for given company.");
					model.setViewName("redirect:/employeeList");
				}
				else
				{
					try {
						if (employee.getUser_id() == null) {
							if(employee.getRole_id()!=null)
							{
							employee.setRole(roleService.getById(employee.getRole_id()));
							}
							else
							{
								employee.setRole(roleService.getById(MyAbstractController.ROLE_EMPLOYEE));
							}
							employee.setIs_updated(true);
							employee.setCompany(comp);
							userService.addEmployee(employee);
							msg = "User added successfully";
						} else {
							id = employee.getUser_id();
							
							if(employee.getRole_id()!=null)
							{
							employee.setRole(roleService.getById(employee.getRole_id()));
							}
							else
							{
								employee.setRole(roleService.getById(MyAbstractController.ROLE_EMPLOYEE));
							}
							userService.updateEmployee(employee);
							msg = "User updated successfully";
						}
					} catch (MyWebException e) {
						e.printStackTrace();
					}
				
						session.setAttribute("msg", msg);
						model.setViewName("redirect:/employeeList");				
					
				}
			}
		}
	    
		else
		{
		List<User> userList = userService.getActiveUserByCompany(user.getCompany().getCompany_id());
		for (User auditor : userList) {
			String aname=auditor.getRole().getRole_name();
			if(aname.equalsIgnoreCase("Auditor"))
			{
				acount=true;				
			}
			else
			{
				emp=emp+1;
			}
		
		}
		for (Role newrole : role) {
			switch (newrole.getRole_name()) {
			case "Auditor":
				rolelist.add(newrole);
				break;				
			case "Employee":
				rolelist.add(newrole);
				break;			
			}
		}

		UserValidator.validate(employee, result);
		if (result.hasErrors()) {
			List<City> cityList = cityService.findAll();
			List<Country> countryList = countryService.findAll();
			List<State> stateList = stateService.findAll();
			model.addObject("cityList", cityList);
			model.addObject("countryList", countryList);
			model.addObject("stateList", stateList);
			model.addObject("rolelist", rolelist);
			model.addObject("acount", acount);
			model.setViewName("master/employee");
		} else {
			try {
				if (employee.getUser_id() == null) {
					
					if(employee.getRole_id()!=null)
					{
					employee.setRole(roleService.getById(employee.getRole_id()));
					}
					else
					{
						employee.setRole(roleService.getById(MyAbstractController.ROLE_EMPLOYEE));
					}
					employee.setIs_updated(true);
					employee.setCompany(user.getCompany());
					userService.addEmployee(employee);
					msg = "User added successfully";
				} else {
					id = employee.getUser_id();
					if(employee.getRole_id()!=null)
					{
					employee.setRole(roleService.getById(employee.getRole_id()));
					}
					else
					{
						employee.setRole(roleService.getById(MyAbstractController.ROLE_EMPLOYEE));
					}
					userService.updateEmployee(employee);
					msg = "User updated successfully";
				}
			} catch (MyWebException e) {
				e.printStackTrace();
			}
			if(id == user.getUser_id()){
				session.setAttribute("successMsg", "Your profile updated successfully");
				model.setViewName("redirect:/homePage");
			}
			else {
				session.setAttribute("msg", msg);
				model.setViewName("redirect:/employeeList");				
			}
		}
		}
		return model;
	}
	@RequestMapping(value = "editEmployee", method = RequestMethod.GET)
	public ModelAndView editEmployee(@RequestParam("id") Long id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		User user1 = (User) session.getAttribute("user");
		ModelAndView model = new ModelAndView();
		User user = new User();
		
		List<City> cityList = cityService.findAll();
		List<Country> countryList = countryService.findAll();
		List<State> stateList = stateService.findAll();
		List<Role> rolelist = new ArrayList<Role>();	
		List<Role> role = roleService.list();
	    boolean acount=false;
	    int emp=0;
	    
	    
		try {
			user = userService.getById(id);
			if (user.getRole() != null) {
				user.setRole_id(user.getRole().getRole_id());
			}
			if (user.getCity() != null) {
				user.setCity_id(user.getCity().getCity_id());
			}
			if (user.getState() != null) {
				user.setState_id(user.getState().getState_id());
			}
			if (user.getCountry() != null) {
				user.setCountry_id(user.getCountry().getCountry_id());
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		
		 if (user1.getRole().getRole_id().equals(ROLE_SUPERUSER)) {
			List<Company> companyList = companyService.getAllCompaniesOnly();
			for (Role newrole : role) {
				switch (newrole.getRole_name()) {
				case "Auditor":
					rolelist.add(newrole);
					break;				
				case "Employee":
					rolelist.add(newrole);
					break;			
				}
			}
			user.setCompany_id(user.getCompany().getCompany_id());
			model.addObject("acount", false);
			model.addObject("cityList", cityList);
			model.addObject("rolelist", rolelist);
			model.addObject("countryList", countryList);
			model.addObject("stateList", stateList);
			model.addObject("employee", user);
			model.addObject("companyList", companyList);
			model.setViewName("master/employee");
		}
		else
		{
		List<User> userList = userService.getActiveUserByCompany(user.getCompany().getCompany_id());
		for (User auditor : userList) {
			String aname=auditor.getRole().getRole_name();
			if(aname.equalsIgnoreCase("Auditor"))
			{
				if(id!=auditor.getUser_id())
				{
				acount=true;	
				}
			}
			else
			{
				emp=emp+1;
			}		
		}
		for (Role newrole : role) {
			switch (newrole.getRole_name()) {
			case "Auditor":
				rolelist.add(newrole);
				break;				
			case "Employee":
				rolelist.add(newrole);
				break;			
			}
		}
		model.addObject("rolelist", rolelist);
		model.addObject("acount", acount);
		model.addObject("cityList", cityList);
		model.addObject("countryList", countryList);
		model.addObject("stateList", stateList);
		model.addObject("employee", user);
		model.setViewName("master/employee");
		}
		return model;
	}

	@RequestMapping(value = "viewEmployee", method = RequestMethod.GET)
	public ModelAndView viewEmployee(@RequestParam("id") Long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		try {
			user = userService.getById(id);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.addObject("employee", user);
		model.setViewName("master/employeeView");
		return model;
	}

	
	
	@RequestMapping(value = "deleteEmployee", method = RequestMethod.GET)
	public ModelAndView deleteEmployee(@RequestParam("id") Long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		try {
			userService.removeById(id);
			session.setAttribute("msg", "Employee deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", "Employee cannot be deleted");
		}
		model.setViewName("redirect:/employeeList");
		return model;
	}

	@RequestMapping(value = "viewUser", method = RequestMethod.GET)
	public ModelAndView viewUser(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		Long role = (Long) session.getAttribute("role");
		Long userId = (Long) session.getAttribute("user_id");
		User user = new User();
		Company company = new Company();
		List<AccountingYear> yearlist = new ArrayList<AccountingYear>();
		try {
			user = userService.getById(userId);
			company = companyService.findOneWithAll(user.getCompany().getCompany_id());
			if(role == ROLE_SUPERUSER) {
				UserForm userform = new UserForm();
				userform.setUser(user);
				userform.setCompany(company);
				model.addObject("userform", userform);
				model.setViewName("master/userView");
				
			} 
			else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				if(role == ROLE_MANAGER) {
					model.addObject("isManager",true);
				}
				model.addObject("isProfile",true);
				model.addObject("user1", user);
				model.setViewName("master/KPOExecutiveView");				
			}
			else if((role == ROLE_EMPLOYEE) || (role == ROLE_AUDITOR)) {
				model.addObject("isProfile",true);
				model.addObject("employee", user);
				model.setViewName("master/employeeView");				
			}
			else {
			//if (role == ROLE_CLIENT) {
				
				String yearId = company.getYearRange();
				if(yearId!=null)
				{
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
				
				UserForm userform = new UserForm();
				userform.setUser(user);
				userform.setCompany(company);
				model.addObject("yearlist", yearlist);
				model.addObject("userform", userform);
				model.setViewName("master/userView");
			}

		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		return model;
	}

	@RequestMapping(value = USER_PROFILE, method = RequestMethod.GET)
	public ModelAndView userProfile(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		Long userId = (Long) session.getAttribute("user_id");
		Long role = (Long) session.getAttribute("role");
		User user = new User();
		Company company = new Company();
		List<AccountingYear> yearlist = new ArrayList<AccountingYear>();

		try {
			user = userService.getById(userId);
			company = companyService.findOneWithAll(user.getCompany().getCompany_id());

			if (role == ROLE_CLIENT) {
				String yearId = company.getYearRange();
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
			if (company.getCompany_statutory_type() != null) {
				company.setCompanyTypeId(company.getCompany_statutory_type().getCompany_statutory_id());
			}
			if (company.getIndustry_type() != null) {
				company.setIndustryTypeId(company.getIndustry_type().getIndustry_id());
			}
			if (company.getCity() != null) {
				company.setCityId(company.getCity().getCity_id());
			}
			if (company.getState() != null) {
				company.setStateId(company.getState().getState_id());
			}
			if (company.getCountry() != null) {
				company.setCountryId(company.getCountry().getCountry_id());
			}
			if (company.getBank() != null) {
				company.setBankId(company.getBank().getBank_id());
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		UserForm userform = new UserForm();
		userform.setUser(user);
		userform.setCompany(company);

		List<Country> countryList = countryService.findAll();
		List<State> stateList = stateService.findAll();
		List<City> cityList = cityService.findAll();
		List<CompanyStatutoryType> companyTypeList = companyTypeService.findAll();
		List<IndustryType> industryTypeList = industryTypeService.findAll();
		List<Bank> bankList = bankService.findAllBanksOfCompany(user.getCompany().getCompany_id());

		model.addObject("companyTypeList", companyTypeList);
		model.addObject("industryTypeList", industryTypeList);
		model.addObject("countryList", countryList);
		model.addObject("stateList", stateList);
		model.addObject("cityList", cityList);
		model.addObject("yearlist", yearlist);
		model.addObject("bankList", bankList);

		model.addObject("userform", userform);
		model.setViewName("master/user");
		return model;
	}

	@RequestMapping(value = USER_PROFILE, method = RequestMethod.POST)
	public ModelAndView userProfile(@ModelAttribute("userform") UserForm userForm,
			@RequestParam("logo") MultipartFile logo, HttpServletRequest request, HttpServletResponse response,
			BindingResult result) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession();
		long company_id = (long) session.getAttribute("company_id");
		User user = (User) session.getAttribute("user");
		Long role = (Long) session.getAttribute("role");
		userForm.setRole_id(role);

		userFormValidator.validate(userForm, result);

		String filePath = request.getServletContext().getRealPath("resources");
		filePath += "/images/logo/";
		List<AccountingYear> yearlist = new ArrayList<AccountingYear>();
		if (result.hasErrors()) {

			try {
				Company company = new Company();
				company = companyService.findOneWithAll(company_id);
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
					model.addObject("yearlist", yearlist);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<Country> countryList = countryService.findAll();
			List<State> stateList = stateService.findAll();
			List<City> cityList = cityService.findAll();
			List<CompanyStatutoryType> companyTypeList = companyTypeService.findAll();
			List<IndustryType> industryTypeList = industryTypeService.findAll();
			List<AccountingYear> yearRangeList = accountingYearService.findAll();
			List<Bank> bankList = bankService.findAllBanksOfCompany(user.getCompany().getCompany_id());

			model.addObject("yearRangeList", yearRangeList);
			model.addObject("companyTypeList", companyTypeList);
			model.addObject("industryTypeList", industryTypeList);
			model.addObject("countryList", countryList);
			model.addObject("stateList", stateList);
			model.addObject("cityList", cityList);
			model.addObject("bankList", bankList);
			model.setViewName("master/user");
			return model;
		} else {

			try {
				byte[] bytes = logo.getBytes();
				if (bytes.length > 0) {
					Path path = Paths.get(filePath + logo.getOriginalFilename());
					Files.write(path, bytes);
					userForm.getCompany().setLogo(logo.getOriginalFilename());
					session.setAttribute("company_logo", logo.getOriginalFilename());
				}
				System.out.println(user.getIs_updated());
				if ((user.getIs_updated() == null) || (user.getIs_updated() != true)) {
					if ((role == ROLE_CLIENT) || (role == ROLE_TRIAL_USER)) {
						subLedgerService.setdefaultdata(company_id);
						subLedgerService.setindustrydata(company_id);
						productService.setdefaultdata(company_id, user);
						session.setAttribute("is_updated", true);
						user.setIs_updated(true);
						session.setAttribute("user",user);						
					}
				}
				System.out.println(user.getIs_updated());
				userForm.getUser().setCity_id(userForm.getCompany().getCityId());
				userForm.getUser().setState_id(userForm.getCompany().getStateId());
				userForm.getUser().setCountry_id(userForm.getCompany().getCountryId());
				userService.update(userForm.getUser());
				companyService.update(userForm.getCompany());
				session.setAttribute("company_name", userForm.getCompany().getCompany_name());

			} catch (MyWebException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}		
			if (role == ROLE_EMPLOYEE) {
				LocalDate lastDate = forgotPasswordLogService.getLastPasswordUpdateDate(userForm.getUser().getUser_id());
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
			if (role == ROLE_SUPERUSER) {
		
				Integer productlist = productService.findAllDashboard();			
				Integer bankList=bankService.findAllDashboard();
				Integer ledgerList=ledgerService.findAllDashboard();
				/*Integer quoteList=quoteService.findAllDashboard();*/
				Integer subledgerCount = subLedgerService.findAllDashboard();
				Integer supplierCount = supplierService.findAllSupplierCountDashboard();
				Integer customerCount = customerService.findAllCustomerCountDashboard();
				
				CopyOnWriteArrayList<Quotation>list =quoteService.findAllQuotations();
				List<ClientSubscriptionMaster>listall=quoteService.findAllSubscriptionList();
				for (ClientSubscriptionMaster clientmaster : listall) {
				
					if(clientmaster.getQuotation_id()!=null)
					{
						for(Quotation quotation:list)
						{
							if(clientmaster.getQuotation_id().getQuotation_id().equals(quotation.getQuotation_id()))
							{
						    list.remove(quotation);
							}
						}
					}
				}
				
				
				model.addObject("quoteList", list.size());
				model.addObject("subledgerCount", subledgerCount);
				model.addObject("supplierCount", supplierCount);
				model.addObject("customerCount", customerCount);
				model.addObject("ledgerList", ledgerList);
				model.addObject("bankList", bankList);
				model.addObject("productlist", productlist);
			}
			else if (role == ROLE_EXECUTIVE) {
				Integer productlist = productService.findByStatusDashboard(role,APPROVAL_STATUS_PENDING,user.getUser_id());
				Integer bankList=bankService.findByStatusDashboard(role,APPROVAL_STATUS_PENDING,user.getUser_id());
				Integer ledgerList=ledgerService.findByStatusDashboard(role,APPROVAL_STATUS_PENDING,user.getUser_id());
				Integer subledgerCount= subLedgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
				Integer supplierCount = supplierService.findByStatusSupplierCountDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
				Integer customerCount = customerService.findByStatusCustomerCountDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
				
			
			
				model.addObject("ledgerList", ledgerList);
				model.addObject("bankList", bankList);
				model.addObject("productlist", productlist);
				model.addObject("subledgerCount", subledgerCount);
				model.addObject("customerCount", customerCount);
				model.addObject("supplierCount", supplierCount);
			}
			else if (role == ROLE_MANAGER) {
				Integer productlist = productService.findByStatusDashboard(role,APPROVAL_STATUS_PENDING,user.getUser_id());
				Integer bankList=bankService.findByStatusDashboard(role,APPROVAL_STATUS_PENDING,user.getUser_id());
				Integer ledgerList=ledgerService.findByStatusDashboard(role,APPROVAL_STATUS_PENDING,user.getUser_id());
				Integer subledgerCount= subLedgerService.findByStatusDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
				Integer supplierCount  = supplierService.findByStatusSupplierCountDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
				Integer customerCount = customerService.findByStatusCustomerCountDashboard(role, APPROVAL_STATUS_PENDING, user.getUser_id());
						
				
			
				model.addObject("ledgerList", ledgerList);
				model.addObject("bankList", bankList);
				model.addObject("productlist", productlist);
				model.addObject("subledgerCount", subledgerCount);
				model.addObject("supplierCount", supplierCount);
				model.addObject("customerCount", customerCount);
			}
			else
			{
				Integer productlist = productService.findAllProductsOfCompanyDashboard(company_id);
				Integer bankList=bankService.findAllBanksOfCompanyDashboard(company_id);
				Integer ledgerList=ledgerService.findAllLedgersOfCompanyDashboard(company_id);
				/*Integer empList = userService.getActiveUserByCompanyDashboard(company_id);*/
				Integer subledgerCount = subLedgerService.findAllSubLedgerOfCompanyDashboard(company_id);
				Integer supplierCount = supplierService.findAllSupplierCountOfCompanyDashboard(company_id);
				Integer customerCount = customerService.findAllCustomerCountOfCompanyDashboard(company_id);
			
				/*model.addObject("empList", empList);*/
				model.addObject("closingBalanceOfCashInHand", subLedgerService.getClosingBalanceOfCashInHand(company_id));
				model.addObject("closingBalanceOfAllBanks", bankService.getClosingBalanceOfAllbanks(company_id));
				model.addObject("totalBillsReceivable", entryService.getTotalbillsBillsReceivable(company_id));
				model.addObject("totalBillsPayable", purEntryService.getTotalBillsPayable(company_id));
				model.addObject("ledgerList", ledgerList);
				model.addObject("bankList", bankList);
				model.addObject("productlist", productlist);
				model.addObject("subledgerCount", subledgerCount);
				model.addObject("supplierCount", supplierCount);
				model.addObject("customerCount", customerCount);
			}
			model.setViewName("/homePage");
			return model;
		}
	}

	@RequestMapping(value = "setDefaultBank", method = RequestMethod.POST)
	public @ResponseBody Boolean setDefaultBank(@RequestParam("bankId") Long bankId, @RequestParam("flag") Long flag,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		return companyService.setDefaultBank(bankId, user.getCompany().getCompany_id(), flag);
	}

	@RequestMapping(value = "deleteCompany", method = RequestMethod.GET)
	public ModelAndView deleteTaxMaster(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		String msg = "";
		HttpSession session = request.getSession(true);
		Long role = (Long) session.getAttribute("role");
		msg = companyService.deleteByIdValue(id);
		if (role == ROLE_SUPERUSER) {
			List<Company> companyList = new ArrayList<Company>();
			companyList = companyService.list();
			model.addObject("companyList", companyList);
		}
		model.addObject("successMsg", msg);
		model.setViewName("master/companyList");
		return model;
	}
}