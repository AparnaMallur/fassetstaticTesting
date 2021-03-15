/**
 * mayur suramwar
 */
package com.fasset.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.fasset.controller.validators.CustomerValidator;
import com.fasset.entities.City;
import com.fasset.entities.Company;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.entities.Country;
import com.fasset.entities.Customer;
import com.fasset.entities.Deductee;
import com.fasset.entities.IndustryType;
import com.fasset.entities.Product;
import com.fasset.entities.State;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.service.interfaces.IAccountSubGroupService;
import com.fasset.service.interfaces.ICityService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICompanyStatutoryTypeService;
import com.fasset.service.interfaces.ICountryService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IDeducteeService;
import com.fasset.service.interfaces.IProductService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IStateService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IYearEndingService;
import com.fasset.service.interfaces.IindustryTypeService;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class CustomerControllerMVC extends MyAbstractController {
	@Autowired
	private CustomerValidator customerValidator;

	@Autowired
	private ICompanyStatutoryTypeService companyStatutoryTypeService;

	@Autowired
	private IindustryTypeService industryTypeService;

	@Autowired
	private ICityService cityService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private ICountryService countryService;

	@Autowired
	private IStateService stateService;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private IAccountSubGroupService accountSubGroupService;

	@Autowired
	private IProductService productService;

	@Autowired
	private ISubLedgerService subLedgerService;

	@Autowired
	private IQuotationService quoteservice;

	@Autowired
	private IDeducteeService deducteeService;

	@Autowired
	private IClientSubscriptionMasterService subscribeservice;

	@Autowired	
	private IYearEndingService yearService;
	
	private Company company = new Company();
	/*private IndustryType type = new IndustryType();*/
	private Set<SubLedger> subLedgerList = new HashSet<SubLedger>();
	private List<Product> productList = new ArrayList<Product>();
	private Set<SubLedger> subLedgers = new HashSet<SubLedger>();
	private Set<Product> suppilerproductList = new HashSet<Product>();
	Boolean importFlag = null; // for maintaining the user on customersList.jsp after delete and view
	Boolean isImport = false; // is Customer saved or updated through excel or through System(Add Customer)
	
	 private List<String> successList = new ArrayList<String>();
	 private List<String> failureList = new ArrayList<String>();
	 private List<String> updateList = new ArrayList<String>();
	 private  String successmsg;
	 private  String failmsg;
	 private  String updatemsg;
	 private  String error;
	 
	@RequestMapping(value = "customers", method = RequestMethod.GET)
	public ModelAndView customer(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		Customer customer = new Customer();
		HttpSession session = request.getSession(true);
		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		
		// code for checking gst number
		Boolean gstnumber= false;
		Company companyForGstCheck = companyService.getCompanyById(company_id);
		
		String gst_no = companyForGstCheck.getGst_no();
		
		    if(!gst_no.equals("") &&  gst_no != null) {
		    	
		    	gstnumber = true;
		    }
		     
		    model.addObject("gstnumber", gstnumber);
		
		if (role == ROLE_SUPERUSER) {
			model.addObject("companyList", companyService.list());
			//model.addObject("subLedgerList", subLedgerService.findAllSubLedgerWithLedger());//For getting Income and Discount subledger
			model.addObject("subLedgerList", subLedgerService.findAllSubLedgerWithLedgerForCustSupplier((long) 1, true,true));
			model.addObject("gstnumber", gstnumber);
			productList = productService.list();
		}else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			model.addObject("companyList", companyService.getAllCompaniesExe(user_id));
			//model.addObject("subLedgerList", subLedgerService.findAllSubLedgerWithLedger(user_id)); //For getting Income and Discount subledger
			model.addObject("subLedgerList", subLedgerService.findAllSubLedgerWithLedgerForCustSupplier(user_id, true,false));
			model.addObject("gstnumber", gstnumber);
			productList = productService.list();
			
		}else {
			try {
				company = companyService.getCompanyWithIndustrytype(company_id);
				/*type = company.getIndustry_type();
				model.addObject("subLedgerListindustry", type.getSubLedgers());*/
				//model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(company_id));////For getting Income and Discount subledger
				model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanyForCustomer(company_id));
				productList = productService.findAllProductsOfCompany(company_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		model.addObject("deducteeList", deducteeService.list());
		model.addObject("customer", customer);
		model.addObject("industryTypeList",  industryTypeService.findAll());
		model.addObject("statutoryTypeList", companyStatutoryTypeService.findAll());
		model.addObject("cityList",  cityService.findAll());
		model.addObject("countryList", countryService.findAll());
		model.addObject("stateList", stateService.findAll());
		model.addObject("accountSubGroupList",  accountSubGroupService.findAll());
		model.addObject("industryTypeList",  industryTypeService.findAll());
		model.addObject("productList", productList);
		model.addObject("gstnumber", gstnumber);
		model.setViewName("/master/customers");
		return model;
	}

	@RequestMapping(value = "savecustomers", method = RequestMethod.POST)
	public ModelAndView savecustomers(@ModelAttribute("customer") Customer customer, 
			BindingResult result,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> map) {
		
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");

		customerValidator.validate(customer, result);
		if (result.hasErrors()) {

			if (role == ROLE_SUPERUSER) {
				
				model.addObject("companyList",  companyService.list());
				//model.addObject("subLedgerList",  subLedgerService.findAllSubLedgerWithLedger());//For getting Income and Discount subledger
				model.addObject("subLedgerList",  subLedgerService.findAllSubLedgerWithLedgerForCustSupplier((long) 1, true, true));
				productList = productService.list();
				model.addObject("deducteeList", deducteeService.list());
			}else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				model.addObject("companyList", companyService.getAllCompaniesExe(user_id));
				//model.addObject("subLedgerList", subLedgerService.findAllSubLedgerWithLedger(user_id));//For getting Income and Discount subledger
				model.addObject("subLedgerList", subLedgerService.findAllSubLedgerWithLedgerForCustSupplier(user_id,true,false));
				productList = productService.list();
				model.addObject("deducteeList", deducteeService.list());				
			}else {
				model.addObject("deducteeList", deducteeService.list());
				//model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(company_id));//For getting Income and Discount subledger
				model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanyForCustomer(company_id));
			}
			model.addObject("statutoryTypeList", companyStatutoryTypeService.findAll());
			model.addObject("cityList",  cityService.findAll());
			model.addObject("countryList", countryService.findAll());
			model.addObject("stateList", stateService.findAll());
			model.addObject("accountSubGroupList", accountSubGroupService.findAll());
			model.addObject("productList", productList);
			model.addObject("industryTypeList", industryTypeService.findAll());

			if (customer.getCustomer_id() != null) {
				model.addObject("subLedgers", subLedgers);
				model.addObject("suppilerproductList", suppilerproductList);
			}
			model.setViewName("/master/customers"); 
		} else {
			String msg = "";

			try {
				if (customer.getCustomer_id() != null) {
					if (customer.getCustomer_id() > 0) {
						customer.setUpdated_by(user_id);
						Customer oldcustomer=customerService.findOneWithAll(customer.getCustomer_id());
						if((oldcustomer.getCustomer_approval() == 1) || (oldcustomer.getCustomer_approval() == 3))
						{
							if (role == ROLE_EXECUTIVE)
								customer.setCustomer_approval(2);
							else if((role == ROLE_CLIENT) || (role == ROLE_EMPLOYEE) || (role == ROLE_TRIAL_USER))
								customer.setCustomer_approval(0);
							else
								customer.setCustomer_approval(oldcustomer.getCustomer_approval());
						}
						else
						{
							customer.setCustomer_approval(oldcustomer.getCustomer_approval());
						}	
						
						customerService.update(customer);
						msg = "Customer updated successfully";
					}
				} else {

					customer.setCustomer_approval(APPROVAL_STATUS_PENDING);
					if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
						customer.setCompany(companyService.getById(customer.getCompany_id()));
					} else {
						customer.setCompany(company);
					}
					customer.setFlag(true);
					customer.setCreated_by(user_id);
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            customer.setIp_address(remoteAddr);
					msg = customerService.saveCustomers(customer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/customersList");
		}
		return model;
	}

	@RequestMapping(value = "customersList", method = RequestMethod.GET)
	public ModelAndView customersList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		
		long company_id = (long) session.getAttribute("company_id");
		importFlag = true;
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}

		if (session.getAttribute("filemsg") != null) {
			model.addObject("filemsg", session.getAttribute("filemsg"));
			session.removeAttribute("filemsg");
		}		
		List<Customer> customerList = new ArrayList<>();
		List<Customer> customerListFailure = new ArrayList<>();
		boolean importfail = false;
		boolean importflag = true;

		if (role == ROLE_SUPERUSER || role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			if(role == ROLE_SUPERUSER) {
				customerList = customerService.findAllCustomerListing((long) 1);
				customerListFailure = customerService.findAllCustomerListing((long) 0);				
			}
			if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				customerList = customerService.findAllCustomerListing(true, user.getUser_id());
				customerListFailure = customerService.findAllCustomerListing(false, user.getUser_id());				
			}
			if (customerListFailure.size() != 0)
				importfail = true;
		} else {
			customerList = customerService.findAllCustomerListingOfCompany(company_id, (long) 1);
			customerListFailure = customerService.findAllCustomerListingOfCompany(company_id, (long) 0);
			
			if (customerListFailure.size() != 0)
				importfail = true;
			Long quote_id = subscribeservice.getquoteofcompany(company_id);
			String email = user.getCompany().getEmail_id();
			if (quote_id != 0) {
				importflag = quoteservice.findAllimportDetailsUser(email, quote_id); // for subscribed client if he registered his company with quotation and quotation contains master imports facility.
			} else {
				importflag = quoteservice.findAllimportDetailsUser(email, quote_id);
			}
			/*if(user.getCompany().getCreated_by()!=null)// for company registered without quotation flow. This will be use for master imports as quotation is not present for this company.
			{
				importflag=true;
			}*/
			
		}
		if(isImport==false)
		{	// code for import . here we are refreshing list and massages which are declared as global.
			successList.clear();
			failureList.clear();
			updateList.clear();
			successmsg=null;
			failmsg=null;
			updatemsg=null;
			error=null; 
			// code for import . here we are refreshing list and massages which are declared as global.
		}
		else if(isImport==true)
        {
		// code for import
		  Collections.sort(successList, new Comparator<String>() {
	            public int compare(String string1, String string2) {
	            return string1.trim().toLowerCase().compareTo(string2.trim().toLowerCase());
	            }
	        });
		  Collections.sort(failureList, new Comparator<String>() {
	            public int compare(String string1, String string2) {
	            return string1.trim().toLowerCase().compareTo(string2.trim().toLowerCase());
	            }
	        });
		  Collections.sort(updateList, new Comparator<String>() {
	            public int compare(String string1, String string2) {
	            return string1.trim().toLowerCase().compareTo(string2.trim().toLowerCase());
	            }
	        });
		model.addObject("successList", successList);
		model.addObject("failureList",failureList);
		model.addObject("updateList",updateList);
		model.addObject("successImportmsg", successmsg);
		model.addObject("failmsg",failmsg);
		model.addObject("updatemsg",updatemsg);
		model.addObject("error",error);
		model.addObject("isImport",true);
		// code for import
		isImport=false;
	   }
		
	
		model.addObject("importfail", importfail);
		model.addObject("importflag", importflag);
		model.addObject("customerList", customerList);
		model.setViewName("/master/customerList");
		return model;
	}

	@RequestMapping(value = "customerimportfailure", method = RequestMethod.GET)
	public ModelAndView customerimportfailure(@RequestParam(value = "msg", required = false) String msg,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		long user_id = (long) session.getAttribute("user_id");
		importFlag = false;
		List<Customer> customerList = new ArrayList<>();
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (role == ROLE_SUPERUSER || role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			if(role == ROLE_SUPERUSER) {
				customerList = customerService.findAllCustomerListing((long) 0);			
			}
			if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				customerList = customerService.findAllCustomerListing(false, user_id);
			}
			model.addObject("customerList", customerList);
			model.setViewName("/master/customerList");
		} else {
			customerList = customerService.findAllCustomerListingOfCompany(company_id, (long) 0);
			model.addObject("customerList", customerList);
			model.setViewName("/master/FailureCustomerList");
		}
		return model;
	}

	@RequestMapping(value = "viewCustomer", method = RequestMethod.GET)
	public ModelAndView viewCustomer(@RequestParam("id") long id, @RequestParam("flag") Long flag) {
		ModelAndView model = new ModelAndView();
		model.addObject("customer", customerService.findOneWithAll(id));
		model.addObject("flag", flag);
		model.addObject("importFlag", importFlag);
		model.setViewName("/master/customerView");
		return model;
	}

	@RequestMapping(value = "editCustomers", method = RequestMethod.GET)
	public ModelAndView editCustomers(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		Long customer_id = id;
		HttpSession session = request.getSession(true);
		session.setAttribute("customer_id", customer_id);

		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		List<SubLedger> compsubLedgerList = new ArrayList<>();
		Customer customer = new Customer();
		customer = customerService.findOneWithAll(id);
		if (role == ROLE_SUPERUSER) {
			model.addObject("companyList",  companyService.list());
			//model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(customer.getCompany().getCompany_id()));//For getting Income and Discount subledger
			model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanyForCustomer(customer.getCompany().getCompany_id()));
			productList = productService.findAllProductsOfCompany(customer.getCompany().getCompany_id());
			
		}else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			model.addObject("companyList", companyService.getAllCompaniesExe(user_id));
			//model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(customer.getCompany().getCompany_id())); //For getting Income and Discount subledger
			model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanyForCustomer(customer.getCompany().getCompany_id()));
			productList = productService.findAllProductsOfCompany(customer.getCompany().getCompany_id());
			
			
		} else {
			try {
				company = companyService.getCompanyWithIndustrytype(company_id);
				/*type = company.getIndustry_type();
				model.addObject("subLedgerListindustry", type.getSubLedgers());*/
				productList = productService.findAllProductsOfCompany(company_id);
				model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(company_id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			if (id > 0) {
				

				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					customer.setCompany_id(customer.getCompany().getCompany_id());
				}

				subLedgers = customer.getSubLedgers();
				suppilerproductList = customer.getProduct();

				if (customer.getCompStatType() != null) {
					customer.setCompany_statutory_id(customer.getCompStatType().getCompany_statutory_id());
				}
				if (customer.getCity() != null) {
					customer.setCity_id(customer.getCity().getCity_id());
				}
				if (customer.getCountry() != null) {
					customer.setCountry_id(customer.getCountry().getCountry_id());
				}
				if (customer.getState() != null) {
					customer.setState_id(customer.getState().getState_id());
				}
				if (customer.getIndustryType() != null) {
					customer.setIndustry_id(customer.getIndustryType().getIndustry_id());
				}
				if (customer.getTds_rate() != null) {
					customer.setTds_rate1(customer.getTds_rate().toString());
				}
				if (customer.getCredit_opening_balance() != null) {
					customer.setCredit_opening_balance1(customer.getCredit_opening_balance().toString());
				}
				if (customer.getDebit_opening_balance() != null) {
					customer.setDebit_opening_balance1(customer.getDebit_opening_balance().toString());
				}
				if (customer.getDeductee() != null) {
					customer.setDeductee_id(customer.getDeductee().getDeductee_id());
				}
				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					for (SubLedger subLedger : subLedgers) {
						compsubLedgerList.remove(subLedger);
					}
				}
				else {
					for (SubLedger subLedger : subLedgers) {
						subLedgerList.remove(subLedger);
					}
				}
				for (Product product : suppilerproductList) {
					productList.remove(product);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addObject("deducteeList", deducteeService.list());
		model.addObject("customer", customer);
		model.addObject("statutoryTypeList", companyStatutoryTypeService.findAll());
		model.addObject("industryTypeList",  industryTypeService.findAll());
		model.addObject("cityList", cityService.findAll());
		model.addObject("countryList", countryService.findAll());
		model.addObject("stateList", stateService.findAll());
		model.addObject("productList", productList);
		model.addObject("accountSubGroupList", accountSubGroupService.findAll());
		model.addObject("subLedgers", subLedgers);
		model.addObject("suppilerproductList", suppilerproductList);
		model.setViewName("/master/customers");
		return model;
	}

	@RequestMapping(value = "deleteCustomerSubLedger", method = RequestMethod.GET)
	public ModelAndView deleteCustomerSubLedger(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		Long customer_id = (Long) session.getAttribute("customer_id");
		String msg = "";
		msg = customerService.deleteCustomerSubLedger(customer_id, id);

		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		List<SubLedger> compsubLedgerList = new ArrayList<>();
		Customer customer = new Customer();
		customer = customerService.findOneWithAll(customer_id);
		if (role == ROLE_SUPERUSER) {
		
			model.addObject("companyList", companyService.list());
			
		//	model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(customer.getCompany().getCompany_id()));//get subledger of Income and discount
			model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanyForCustomer(customer.getCompany().getCompany_id()));
			productList = productService.findAllProductsOfCompany(customer.getCompany().getCompany_id());
		}else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			model.addObject("companyList", companyService.getAllCompaniesExe(user_id));
			
			//model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(customer.getCompany().getCompany_id()));//get subledger of Income and discount
			model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanyForCustomer(customer.getCompany().getCompany_id()));
			productList = productService.findAllProductsOfCompany(customer.getCompany().getCompany_id());
			
		}else {
			try {
				company = companyService.getCompanyWithIndustrytype(company_id);
				/*type = company.getIndustry_type();
				model.addObject("subLedgerListindustry", type.getSubLedgers());*/
				productList = productService.findAllProductsOfCompany(company_id);
				model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(company_id));//get subledger of Income and discount
				model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanyForCustomer(company_id));
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	

		try {
			if (id > 0) {
				
				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					if (customer.getCompany().getCompany_id() != null) {
						customer.setCompany_id(customer.getCompany().getCompany_id());
					}
				}

				subLedgers = customer.getSubLedgers();
				suppilerproductList = customer.getProduct();

				if (customer.getCompStatType() != null) {
					customer.setCompany_statutory_id(customer.getCompStatType().getCompany_statutory_id());
				}
				if (customer.getCity() != null) {
					customer.setCity_id(customer.getCity().getCity_id());
				}
				if (customer.getCountry() != null) {
					customer.setCountry_id(customer.getCountry().getCountry_id());
				}
				if (customer.getState() != null) {
					customer.setState_id(customer.getState().getState_id());
				}
				if (customer.getIndustryType() != null) {
					customer.setIndustry_id(customer.getIndustryType().getIndustry_id());
				}

				if (customer.getTds_rate() != null) {
					customer.setTds_rate1(customer.getTds_rate().toString());
				}

				if (customer.getCredit_opening_balance() != null) {
					customer.setCredit_opening_balance1(customer.getCredit_opening_balance().toString());
				}
				if (customer.getDebit_opening_balance() != null) {
					customer.setDebit_opening_balance1(customer.getDebit_opening_balance().toString());
				}
				if (customer.getDeductee() != null) {
					customer.setDeductee_id(customer.getDeductee().getDeductee_id());
				}
				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					for (SubLedger subLedger : subLedgers) {
						compsubLedgerList.remove(subLedger);
					}
				}

				else {

					for (SubLedger subLedger : subLedgers) {
						subLedgerList.remove(subLedger);
					}
				}
				for (Product product : suppilerproductList) {
					productList.remove(product);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("deducteeList", deducteeService.list());
		model.addObject("customer", customer);
		model.addObject("statutoryTypeList", companyStatutoryTypeService.findAll());
		model.addObject("industryTypeList",  industryTypeService.findAll());

		model.addObject("cityList", cityService.findAll());
		model.addObject("countryList", countryService.findAll());
		model.addObject("stateList", stateService.findAll());
		model.addObject("productList", productList);
		model.addObject("accountSubGroupList", accountSubGroupService.findAll());
		model.addObject("subLedgers", subLedgers);
		model.addObject("suppilerproductList", suppilerproductList);
		model.addObject("successMsg", msg);
		model.setViewName("/master/customers");
		return model;
	}

	@RequestMapping(value = "deleteCustomerProduct", method = RequestMethod.GET)
	public ModelAndView deleteCustomerProduct(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		Long customer_id = (Long) session.getAttribute("customer_id");
		String msg = "";
		msg = customerService.deleteCustomerProduct(customer_id, id);

		List<SubLedger> compsubLedgerList = new ArrayList<>();
		Customer customer = new Customer();
		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		customer = customerService.findOneWithAll(customer_id);
		
		if (role == ROLE_SUPERUSER) {
			model.addObject("companyList", companyService.list());
			//model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(customer.getCompany().getCompany_id()));// get Income and discount subledger only
			model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanyForCustomer(customer.getCompany().getCompany_id()));
			productList = productService.findAllProductsOfCompany(customer.getCompany().getCompany_id());
		}else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			
			model.addObject("companyList", companyService.getAllCompaniesExe(user_id));
			//model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(customer.getCompany().getCompany_id()));// get Income and discount subledger only
			
			model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanyForCustomer(customer.getCompany().getCompany_id()));
			productList = productService.findAllProductsOfCompany(customer.getCompany().getCompany_id());
			
		}else {
			try {
				company = companyService.getCompanyWithIndustrytype(company_id);
				/*type = company.getIndustry_type();
				model.addObject("subLedgerListindustry", type.getSubLedgers());*/
				productList = productService.findAllProductsOfCompany(company_id);
				//model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompany(company_id)); // get Income and discount subledger only
				
				model.addObject("subLedgerList", subLedgerService.findAllApprovedByCompanyForCustomer(company_id));
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		
		try {
			if (id > 0) {
				
				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					if (customer.getCompany().getCompany_id() != null) {
						customer.setCompany_id(customer.getCompany().getCompany_id());
					}
				}

				subLedgers = customer.getSubLedgers();
				suppilerproductList = customer.getProduct();

				if (customer.getCompStatType() != null) {
					customer.setCompany_statutory_id(customer.getCompStatType().getCompany_statutory_id());
				}
				if (customer.getCity() != null) {
					customer.setCity_id(customer.getCity().getCity_id());
				}
				if (customer.getCountry() != null) {
					customer.setCountry_id(customer.getCountry().getCountry_id());
				}
				if (customer.getState() != null) {
					customer.setState_id(customer.getState().getState_id());
				}
				if (customer.getIndustryType() != null) {
					customer.setIndustry_id(customer.getIndustryType().getIndustry_id());
				}

				if (customer.getTds_rate() != null) {
					customer.setTds_rate1(customer.getTds_rate().toString());
				}

				if (customer.getCredit_opening_balance() != null) {
					customer.setCredit_opening_balance1(customer.getCredit_opening_balance().toString());
				}
				if (customer.getDebit_opening_balance() != null) {
					customer.setDebit_opening_balance1(customer.getDebit_opening_balance().toString());
				}
				if (customer.getDeductee() != null) {
					customer.setDeductee_id(customer.getDeductee().getDeductee_id());
				}
				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					for (SubLedger subLedger : subLedgers) {
						compsubLedgerList.remove(subLedger);
					}
				}
				else {
					for (SubLedger subLedger : subLedgers) {
						subLedgerList.remove(subLedger);
					}
				}
				for (Product product : suppilerproductList) {
					productList.remove(product);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("customer", customer);
		model.addObject("statutoryTypeList", companyStatutoryTypeService.findAll());
		model.addObject("industryTypeList",  industryTypeService.findAll());
		model.addObject("cityList", cityService.findAll());
		model.addObject("countryList", countryService.findAll());
		model.addObject("stateList", stateService.findAll());
		model.addObject("deducteeList", deducteeService.list());
		model.addObject("productList", productList);
		model.addObject("accountSubGroupList", accountSubGroupService.findAll());
		model.addObject("subLedgers", subLedgers);
		model.addObject("suppilerproductList", suppilerproductList);
		model.addObject("successMsg", msg);
		model.setViewName("/master/customers");
		return model;
	}

	@RequestMapping(value = "customerPrimaryValidationList", method = RequestMethod.GET)
	public ModelAndView customerPrimaryValidation(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		List<Customer> customerList = new ArrayList<Customer>();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}

		if(role == ROLE_SUPERUSER) {
			customerList = customerService.findByStatus(APPROVAL_STATUS_PENDING);		
		}
		if(role == ROLE_EXECUTIVE ) {
			customerList = customerService.findByStatus(role,APPROVAL_STATUS_PENDING, user.getUser_id());
		}
		model.addObject("customerList", customerList);
		model.addObject("customerbatch", new Customer());
		model.setViewName("/master/customerPrimaryValidationList");
		return model;
	}

	@RequestMapping(value = "customerSecondaryValidationList", method = RequestMethod.GET)
	public ModelAndView customerSecondaryValidationList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		List<Customer> customerList = new ArrayList<Customer>();
		User user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}

		if(role == ROLE_SUPERUSER)  {
			customerList = customerService.findByStatus(APPROVAL_STATUS_PRIMARY);		
		}
		if(role == ROLE_MANAGER ) {
			customerList = customerService.findByStatus(role,APPROVAL_STATUS_PRIMARY, user.getUser_id());
		}
		model.addObject("customerList", customerList);
		model.addObject("customerbatch",  new Customer());
		model.setViewName("/master/customerSecondaryValidationList");
		return model;
	}

	@RequestMapping(value = "rejectCustomer", method = RequestMethod.GET)
	public ModelAndView rejectCustomer(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("rejectApproval") Boolean rejectApproval) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		Long customerId = id;
		String msg = "";
		msg = customerService.updateByApproval(customerId, APPROVAL_STATUS_REJECT);
		yearService.saveActivityLogForm(user.getUser_id(), customerId, null, null, null, null, null, null, null, null, (long)APPROVAL_STATUS_REJECT);
		session.setAttribute("successMsg", msg);
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && rejectApproval == true)) {			
			model.setViewName("redirect:/customerPrimaryValidationList");
		} else {
			model.setViewName("redirect:/customerSecondaryValidationList");
		}
		return model;
	}

	@RequestMapping(value = "approveCustomer", method = RequestMethod.GET)
	public ModelAndView approveCustomer(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("primaryApproval") Boolean primaryApproval) {
		ModelAndView model = new ModelAndView();
		Long customerId = id;
		String msg = "";
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && primaryApproval == true)) {
			msg = customerService.updateByApproval(customerId, APPROVAL_STATUS_PRIMARY);
			yearService.saveActivityLogForm(user.getUser_id(), customerId,  null, null, null, null, null, null, (long)APPROVAL_STATUS_PRIMARY, null, null);
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/customerPrimaryValidationList");
		} else {
			msg = customerService.updateByApproval(customerId, APPROVAL_STATUS_SECONDARY);
			yearService.saveActivityLogForm(user.getUser_id(), customerId, null, null, null, null, null, null, null, (long)APPROVAL_STATUS_SECONDARY, null);
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/customerSecondaryValidationList");
		}
		return model;
	}

	@RequestMapping(value = { "importExcelCustomer" }, method = { RequestMethod.POST })
	public ModelAndView importExcelCustomer(@RequestParam("excelFile") MultipartFile excelfile, 
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView();
		boolean isValid = true;
		String success = "Record Imported successfully";
		int successcount = 0;
		String fail = "in failure list, Please refer failure list";
		int failcount = 0;
		String update = "Updated List";
		StringBuffer ErrorMsgs=new StringBuffer();
		boolean Err=false;
		BufferedWriter output = null;
		int updatecount = 0;
		successmsg = "";
	    failmsg = "";
	    updatemsg = "";
	    error = "";
	    isImport=true;
	    successList.clear();
		failureList.clear();
		updateList.clear();
		int m = 0;
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");

		User user = new User();
		user = (User) session.getAttribute("user");
		try {
			if (excelfile.getOriginalFilename().endsWith("xls")) {
				int i = 0;
				isValid = true;
				HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
				HSSFSheet worksheet = workbook.getSheetAt(0);
				int rowcount=0;
				while (i <= worksheet.getLastRowNum()) {
					int colcount=0;
					HSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 24; j++) {
						if (row.getCell(j) != null) {
							colcount++;
						}
					}
					if(colcount>=17)
					{
					rowcount++;
					}
					

				}
				if (isValid) {
					i = 1;
					while (i <= rowcount) {
						Customer customer = new Customer();
						HSSFRow row = worksheet.getRow(i++);
						
						Err=false;
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer fvar = 0;
							Integer fvarcount = 1;
							Set<SubLedger> subLedgers = new HashSet<SubLedger>();
							Set<Product> products = new HashSet<Product>();

							List<CompanyStatutoryType> companyStatutoryList = companyStatutoryTypeService.findAll();
							try {
								for (CompanyStatutoryType companyStatutory : companyStatutoryList) {
									String str = "";
									str = companyStatutory.getCompany_statutory_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(row.getCell(4).getStringCellValue().replaceAll(" ", ""))) {
										fvar = 1;
										customer.setCompany_statutory_id(companyStatutory.getCompany_statutory_id());
										break;
									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" Company Statutory is blank ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							List<IndustryType> industryList = industryTypeService.findAll();
							try {
								for (IndustryType industry : industryList) {
									String str = "";
									str = industry.getIndustry_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(row.getCell(5).getStringCellValue().replaceAll(" ", ""))) {
										fvar = 1;
										customer.setIndustry_id(industry.getIndustry_id());
										break;
									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" Industry Type is blank ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							List<Country> countryList = countryService.findAll();
							try {
								for (Country country : countryList) {
									String str = "";

									str = country.getCountry_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(
											row.getCell(16).getStringCellValue().replaceAll(" ", ""))) {
										fvar = 1;
										customer.setCountry_id(country.getCountry_id());
										break;
									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" Country Name incorrect/does not exist ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							List<State> stateList = stateService.findAll();
							try {
								for (State state : stateList) {
									String str = "";
									str = state.getState_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(
											row.getCell(17).getStringCellValue().replaceAll(" ", ""))) {
										if(state.getCntry().getCountry_id()==customer.getCountry_id())
										{
										fvar = 1;
										customer.setState_id(state.getState_id());
										break;
										}
										else
										{
											fvar = 2;
										}
									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" State Name incorrect/does not exist ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							List<City> cityList = cityService.findAll();
							try {
								for (City city : cityList) {
									String str = "";
									str = city.getCity_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(
											row.getCell(18).getStringCellValue().replaceAll(" ", ""))) {
										if(city.getState().getState_id()==customer.getState_id())
										{
										fvar = 1;
										customer.setCity_id(city.getCity_id());
										break;
										}
										else
										{
											fvar = 2;
										}											
									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" City Name incorrect/does not exist ");}

							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;

							if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
								List<Company> companylist = companyService.findAll();
								try {
									for (Company comp : companylist) {
										String str = "";
										str = comp.getCompany_name().replaceAll(" ", "");
										if (str.equalsIgnoreCase(
												row.getCell(25).getStringCellValue().replaceAll(" ", ""))) {
											customer.setCompany(comp);
											customer.setCompany_id(comp.getCompany_id());
											fvar = 1;
											break;
										} else {
											fvar = 2;

										}
									}
									if(fvar==2){Err=true;ErrorMsgs.append(" Company Name incorrect/does not exist ");}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								customer.setCompany(user.getCompany());
								customer.setCompany_id(company_id);
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							customer.setContact_name(row.getCell(0).getStringCellValue());
								
							try 
							{
								customer.setMobile(row.getCell(1).getStringCellValue());
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							try 
							{
								Double doubleValue = row.getCell(1).getNumericCellValue();
								BigDecimal bd = new BigDecimal(doubleValue.toString());
								long lonVal = bd.longValue();
								String Mobile = Long.toString(lonVal).trim();
								customer.setMobile(Mobile);
								//customer.setMobile(((Integer)((Double)row.getCell(1).getNumericCellValue()).intValue()).toString().trim());
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							customer.setEmail_id(row.getCell(2).getStringCellValue());
							customer.setFirm_name(row.getCell(3).getStringCellValue());
							String company_name = customer.getFirm_name().replace("\"", "").replace("\'", "");
							customer.setFirm_name(company_name);
							if (row.getCell(6, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setLandline_no(row.getCell(6).getStringCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								try 
								{
									customer.setLandline_no(((Integer)((Double)row.getCell(6).getNumericCellValue()).intValue()).toString().trim());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							if (row.getCell(7, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setOwner_pan_no(row.getCell(7).getStringCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
									customer.setOwner_pan_no("0");
								}
								
							}
							if (row.getCell(8, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setAdhaar_no(row.getCell(8).getStringCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								try 
								{
									Double doubleValue = row.getCell(8).getNumericCellValue();
									BigDecimal bd = new BigDecimal(doubleValue.toString());
									long lonVal = bd.longValue();
									String adhar = Long.toString(lonVal).trim();
									customer.setAdhaar_no(adhar);
									//customer.setMobile(Mobile)
									//customer.setAdhaar_no(((Integer)((Double)row.getCell(8).getNumericCellValue()).intValue()).toString().trim());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							boolean gstapply = row.getCell(9).getStringCellValue().matches("Yes");
							if (gstapply == true) {
								customer.setGst_applicable(gstapply);
								if (row.getCell(10, row.CREATE_NULL_AS_BLANK) != null) {
									try 
									{
										customer.setGst_no(row.getCell(10).getStringCellValue());
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
									try 
									{
										customer.setGst_no(((Integer)((Double)row.getCell(10).getNumericCellValue()).intValue()).toString().trim());
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}
							} else {
								customer.setGst_applicable(gstapply);
							}
							try 
							{
								customer.setCompany_pan_no(row.getCell(11).getStringCellValue());
							}
							catch(Exception e)
							{
								e.printStackTrace();
								customer.setCompany_pan_no("0");
							}
							
							
							if (row.getCell(12, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setOther_tax_no(row.getCell(12).getStringCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								try 
								{
									customer.setOther_tax_no(((Integer)((Double)row.getCell(12).getNumericCellValue()).intValue()).toString().trim());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							if (row.getCell(13, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setTan_no(row.getCell(13).getStringCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								try 
								{
									customer.setTan_no(((Integer)((Double)row.getCell(13).getNumericCellValue()).intValue()).toString().trim());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							if (row.getCell(14, row.CREATE_NULL_AS_BLANK) != null) {
								customer.setCurrent_address(row.getCell(14).getStringCellValue());
							}
							if (row.getCell(15, row.CREATE_NULL_AS_BLANK) != null) {
								customer.setPermenant_address(row.getCell(15).getStringCellValue());
							}
							if (row.getCell(19, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setPincode(new Long(row.getCell(19).getStringCellValue()));
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								try 
								{
									customer.setPincode((long) row.getCell(19).getNumericCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							Integer tdsapply;
							boolean tapply = row.getCell(20).getStringCellValue().matches("Yes");
							
							/*
							 * if (tapply == true) { tdsapply = 1; customer.setTds_applicable(tdsapply); if
							 * (row.getCell(22, row.CREATE_NULL_AS_BLANK) != null) { try {
							 * customer.setTds_rate(new Float(row.getCell(22).getStringCellValue())); }
							 * catch(Exception e) { e.printStackTrace(); } try {
							 * customer.setTds_rate((float) row.getCell(22).getNumericCellValue()); }
							 * catch(Exception e) { e.printStackTrace(); } } else {
							 * customer.setTds_rate((float)0); }
							 */
								/*	List<Deductee> deducteeList = deducteeService.list();
								try {
									for (Deductee dec : deducteeList) {
										String str = "";
										str = dec.getDeductee_title().replaceAll(" ", "");
										if (row.getCell(21, row.CREATE_NULL_AS_BLANK) != null) {
											if (str.equalsIgnoreCase(
													row.getCell(21).getStringCellValue().replaceAll(" ", ""))) {
												customer.setDeductee_id(dec.getDeductee_id());
												customer.setDeductee(dec);
												fvar = 1;
												break;
											} else {
												fvar = 2;
											}
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								if ((fvar == 1) && (fvarcount == 1))
									fvarcount = 1;
								else
									fvarcount = 0;
							} else {
								tdsapply = 0;
								customer.setTds_applicable(tdsapply);
								customer.setTds_rate((float)0);
							}
							
*/					
							
							List<Deductee> deducteeList = deducteeService.list();

							if (tapply == true) {
								tdsapply = 1;
								customer.setTds_applicable(tdsapply);

								try {
									

										if (row.getCell(21, row.CREATE_NULL_AS_BLANK) != null) {
											for (Deductee dec : deducteeList) {
												String str = "";
												float rate = 0;
												str = dec.getDeductee_title().replaceAll(" ", "");
												rate = dec.getValue();
											if (str.equalsIgnoreCase(
													row.getCell(21).getStringCellValue().replaceAll(" ", ""))) {
												customer.setDeductee_id(dec.getDeductee_id());
												customer.setDeductee(dec);
												if (row.getCell(22, row.CREATE_NULL_AS_BLANK) != null) {

													try {
														customer.setTds_rate(rate);
													} catch (Exception e) {
														e.printStackTrace();
													}
													try {
														customer.setTds_rate(rate);
													} catch (Exception e) {
														e.printStackTrace();
													}
												} else {
													customer.setTds_rate((float) 0);
												}

												// customer.setTds_rate(rate);
												fvar = 1;
												break;
											} else {
												fvar = 2;
											}
										}
											
									}
										if(fvar==2){Err=true;ErrorMsgs.append(" TDS Type entered incorrect");}
								} catch (Exception e) {
									e.printStackTrace();
								}
								if ((fvar == 1) && (fvarcount == 1))
									fvarcount = 1;
								else
									fvarcount = 0;
							} else {
								tdsapply = 0;
								customer.setTds_applicable(tdsapply);
								customer.setTds_rate((float) 0);
							}

							if (row.getCell(23, row.CREATE_NULL_AS_BLANK) != null) {
								String suball = row.getCell(23).getStringCellValue();
								if (suball != null) {
									String[] sublist = suball.split(",");
									for (String subId : sublist) {
										subId = subId.trim();
										SubLedger sr = subLedgerService.isExists(subId, customer.getCompany_id());
										if (sr != null)
										{
											subLedgers.add(sr);
											fvar = 1;
										}
										else
											fvar = 2;
									}
									if(fvar==2){
										Err=true;
										ErrorMsgs.append(" Subledger is incorrect");
									}
								}
								else{
									fvar=2;
									Err=true;
									ErrorMsgs.append(" Subledger cannot be blank");
								}
							}else
							{
								fvar=2;
								Err=true;
								ErrorMsgs.append(" Subledger cannot be blank");
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							if (row.getCell(24, row.CREATE_NULL_AS_BLANK) != null) {
								String prodall = row.getCell(24).getStringCellValue();
								if (prodall != null) {
									String[] prodlist = prodall.split(",");
									for (String prodId : prodlist) {
										prodId = prodId.trim();
										Product pr = productService.isExists(prodId, customer.getCompany_id());
										if (pr != null)
										{
											products.add(pr);
											fvar = 1;
										}
										else
											fvar = 2;
									}
									if(fvar==2){Err=true;ErrorMsgs.append(" Product incorrect/does not exist");} 
								}else {
									fvar=2;
									Err=true;
									ErrorMsgs.append(" Product cannot be blank");
								}
							}
							else{
								fvar=2;
								Err=true;
								ErrorMsgs.append(" Product cannot be blank");
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;

							customer.setSubLedgers(subLedgers);
							customer.setProduct(products);
							customer.setCustomer_approval(0);
							customer.setStatus(true);
							customer.setCompany(user.getCompany());
							customer.setCompany_id(company_id);
							customer.setCreated_by(user_id);
							if (fvarcount == 1) {
								m = 1;
								customer.setFlag(true);
							} else if (fvarcount == 0) {
								m = m + 2;
								customer.setFlag(false);
							}
							if (fvar != 0) {
								Customer cr = customerService.isExistsCustomer(customer.getCompany_pan_no(),
										customer.getFirm_name(), company_id, customer.getContact_name());
								if (cr != null) {
									
									cr.setUpdated_by(user_id);
									customer.setCustomer_id(cr.getCustomer_id());
									customerService.updateExcel(customer);
									if (fvarcount == 1) {
										m = 1;
										updateList.add(customer.getContact_name());
										updatecount = updatecount + 1;
									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(customer.getContact_name());
										failcount = failcount + 1;
									}
								} else {
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									customer.setIp_address(remoteAddr);
									customer.setCreated_by(user_id);
									customerService.add(customer);
									if (fvarcount == 1) {
										m = 1;
										successList.add(customer.getContact_name());
										successcount = successcount + 1;

									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(customer.getContact_name());
										failcount = failcount + 1;
									}
								}
							}

						}
						if(Err==true){String msg="----row no "+row.getRowNum();System.out.println("row is n"+row.getRowNum());ErrorMsgs.append(msg);ErrorMsgs.append("\n");}
					}
					workbook.close();
				} else {
					System.out.println("no data");
				}
			} else if (excelfile.getOriginalFilename().endsWith("xlsx")) {
				int i = 0;
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
				isValid = true;
				int rowcount=0;
				System.out.println("last rw nm "+ worksheet.getLastRowNum());
				while (i <= worksheet.getLastRowNum()) {
					
					XSSFRow row = worksheet.getRow(i++);
					int colcount=0;
					for (int j = 0; j <= 24; j++) {
						if (row.getCell(j) != null) {
							colcount++;
						}
					}
					if(colcount>=17)
					{
					rowcount++;
					}
					
				}
				System.out.println("rowcont is "+rowcount);
				if (isValid) {
					System.out.println("last rw cnt "+ rowcount);
					i = 1;
					while (i <= rowcount) {
						Customer customer = new Customer();
						XSSFRow row = worksheet.getRow(i++);
						Err=false;
						
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							System.out.println("Row is "+i);
							Integer fvar = 0;
							Integer fvarcount = 1;
							Set<SubLedger> subLedgers = new HashSet<SubLedger>();
							Set<Product> products = new HashSet<Product>();

							List<CompanyStatutoryType> companyStatutoryList = companyStatutoryTypeService.findAll();
							try {
								for (CompanyStatutoryType companyStatutory : companyStatutoryList) {
									String str = "";
									str = companyStatutory.getCompany_statutory_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(row.getCell(4).getStringCellValue().replaceAll(" ", ""))) {
										fvar = 1;
										customer.setCompany_statutory_id(companyStatutory.getCompany_statutory_id());
										break;
									} else {
										fvar = 2;
										
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" Company Statutory is blank ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							System.out.println("fvarcnt 1"+fvarcount);
							List<IndustryType> industryList = industryTypeService.findAll();
							try {
								for (IndustryType industry : industryList) {
									String str = "";
									str = industry.getIndustry_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(row.getCell(5).getStringCellValue().replaceAll(" ", ""))) {
										fvar = 1;
										customer.setIndustry_id(industry.getIndustry_id());
										break;
									} else {
										fvar = 2;
										
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" Industry Type is blank ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							System.out.println("fvarcnt 2"+fvarcount);
							List<Country> countryList = countryService.findAll();
							try {
								for (Country country : countryList) {
									String str = "";

									str = country.getCountry_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(
											row.getCell(16).getStringCellValue().replaceAll(" ", ""))) {
										fvar = 1;
										customer.setCountry_id(country.getCountry_id());
										break;
									} else {
										fvar = 2;
										
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" Country Name incorrect/does not exis ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							System.out.println("fvarcnt 3"+fvarcount);
							List<State> stateList = stateService.findAll();
							try {
								for (State state : stateList) {
									String str = "";
									str = state.getState_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(
											row.getCell(17).getStringCellValue().replaceAll(" ", ""))) {
										if(state.getCntry().getCountry_id()==customer.getCountry_id())
										{
										fvar = 1;
										customer.setState_id(state.getState_id());
										break;
										}
										else
										{
											fvar = 2;
											
										}
									} else {
										fvar = 2;
										
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" State Name incorrect/does not exist ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							System.out.println("fvarcnt 4"+fvarcount);
							List<City> cityList = cityService.findAll();
							try {
								for (City city : cityList) {
									String str = "";
									str = city.getCity_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(
											row.getCell(18).getStringCellValue().replaceAll(" ", ""))) {
										if(city.getState().getState_id()==customer.getState_id())
										{
										fvar = 1;
										customer.setCity_id(city.getCity_id());
										break;
										}
										else
										{
											fvar = 2;
											
										}											
									} else {
										fvar = 2;
										
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" City Name incorrect/does not exist ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							
							if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
								List<Company> companylist = companyService.findAll();
								try {
									for (Company comp : companylist) {
										String str = "";
										str = comp.getCompany_name().replaceAll(" ", "");
										if (str.equalsIgnoreCase(
												row.getCell(25).getStringCellValue().replaceAll(" ", ""))) {
											customer.setCompany(comp);
											customer.setCompany_id(comp.getCompany_id());
											fvar = 1;
											break;
										} else {
											fvar = 2;
											

										}
									}
									if(fvar==2){Err=true;ErrorMsgs.append(" Company Name incorrect/does not exist ");}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								customer.setCompany(user.getCompany());
								customer.setCompany_id(company_id);
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							
							customer.setContact_name(row.getCell(0).getStringCellValue());
								
							try 
							{
								customer.setMobile(row.getCell(1).getStringCellValue());
							}
							catch(Exception e)
							{
								e.printStackTrace();
								
							}
							try 
							{
								
								Double doubleValue = row.getCell(1).getNumericCellValue();
								BigDecimal bd = new BigDecimal(doubleValue.toString());
							
								long lonVal = bd.longValue();
								
								String Mobile = Long.toString(lonVal).trim();
								//customer.setMobile(((Integer)((Double)row.getCell(1).getNumericCellValue()).intValue()).toString().trim());
								//customer.setMobile(((Double) row.getCell(1).getNumericCellValue())).toString().trim()
								//customer.setMobile((((Double) row.getCell(1).getNumericCellValue())).toString().trim());
								//String Mobile=row.getCell(1).getNumericCellValue()+"";
								
								customer.setMobile(Mobile);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							customer.setEmail_id(row.getCell(2).getStringCellValue());
							customer.setFirm_name(row.getCell(3).getStringCellValue());
							String company_name = customer.getFirm_name().replace("\"", "").replace("\'", "");
							customer.setFirm_name(company_name);
							if (row.getCell(6, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setLandline_no(row.getCell(6).getStringCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								try 
								{
									customer.setLandline_no(((Integer)((Double)row.getCell(6).getNumericCellValue()).intValue()).toString().trim());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							if (row.getCell(7, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setOwner_pan_no(row.getCell(7).getStringCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
									customer.setOwner_pan_no("0");
								}
								
							}
							if (row.getCell(8, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setAdhaar_no(row.getCell(8).getStringCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								try 
								{
									Double doubleValue = row.getCell(8).getNumericCellValue();
									System.out.println("The dooble value s" +doubleValue);
									BigDecimal bd = new BigDecimal(doubleValue.toString());
									System.out.println("The bd value s" +bd);
									long lonVal = bd.longValue();
									System.out.println("The lonVal value s" +lonVal);
									String adhar = Long.toString(lonVal).trim();
									System.out.println("The adhar value s" +adhar);
									customer.setAdhaar_no(adhar);
									//customer.setAdhaar_no(((Integer)((Double)row.getCell(8).getNumericCellValue()).intValue()).toString().trim());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							boolean gstapply = row.getCell(9).getStringCellValue().matches("Yes");
							if (gstapply == true) {
								customer.setGst_applicable(gstapply);
								if (row.getCell(10, row.CREATE_NULL_AS_BLANK) != null) {
									try 
									{
										customer.setGst_no(row.getCell(10).getStringCellValue());
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
									try 
									{
										customer.setGst_no(((Integer)((Double)row.getCell(10).getNumericCellValue()).intValue()).toString().trim());
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}
							} else {
								customer.setGst_applicable(gstapply);
							}
							try 
							{
								customer.setCompany_pan_no(row.getCell(11).getStringCellValue());
							}
							catch(Exception e)
							{
								e.printStackTrace();
								customer.setCompany_pan_no("0");
							}
							
							
							if (row.getCell(12, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setOther_tax_no(row.getCell(12).getStringCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								try 
								{
									customer.setOther_tax_no(((Integer)((Double)row.getCell(12).getNumericCellValue()).intValue()).toString().trim());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							if (row.getCell(13, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setTan_no(row.getCell(13).getStringCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								try 
								{
									customer.setTan_no(((Integer)((Double)row.getCell(13).getNumericCellValue()).intValue()).toString().trim());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							if (row.getCell(14, row.CREATE_NULL_AS_BLANK) != null) {
								customer.setCurrent_address(row.getCell(14).getStringCellValue());
							}
							if (row.getCell(15, row.CREATE_NULL_AS_BLANK) != null) {
								customer.setPermenant_address(row.getCell(15).getStringCellValue());
							}
							if (row.getCell(19, row.CREATE_NULL_AS_BLANK) != null) {
								try 
								{
									customer.setPincode(new Long(row.getCell(19).getStringCellValue()));
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								try 
								{
									customer.setPincode((long) row.getCell(19).getNumericCellValue());
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							Integer tdsapply;
							boolean tapply = row.getCell(20).getStringCellValue().matches("Yes");
							
							List<Deductee> deducteeList = deducteeService.list();
							
							if (tapply == true) {
								tdsapply = 1;
								customer.setTds_applicable(tdsapply);
								
								try {
									
										
										if (row.getCell(21, row.CREATE_NULL_AS_BLANK) != null) {
											for (Deductee dec : deducteeList) {
												String str = "";
												float rate=0;
												str = dec.getDeductee_title().replaceAll(" ", "");
												rate=dec.getValue();
											if (str.equalsIgnoreCase(
													row.getCell(21).getStringCellValue().replaceAll(" ", ""))) {
												customer.setDeductee_id(dec.getDeductee_id());
												customer.setDeductee(dec);
												if (row.getCell(22, row.CREATE_NULL_AS_BLANK) != null) {
													
													try 
											     	{
														  customer.setTds_rate(rate);
													} 
													catch (Exception e) {
														e.printStackTrace();
													}
													try 
													{
														customer.setTds_rate(rate);
													}
													catch(Exception e)
													{
														e.printStackTrace();
													}
												}
												else
												{
													customer.setTds_rate((float)0);
												}

													
											
										
												//customer.setTds_rate(rate);
												fvar = 1;
												break;
											} else {
												fvar = 2;
												
											}
										}
											if(fvar==2){Err=true;ErrorMsgs.append(" TDS Type entered incorrect");}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								if ((fvar == 1) && (fvarcount == 1))
									fvarcount = 1;
								else
									fvarcount = 0;
							} 
							else {
								tdsapply = 0;
								customer.setTds_applicable(tdsapply);
								customer.setTds_rate((float)0);
							}
							
							/*
							 * if (row.getCell(22, row.CREATE_NULL_AS_BLANK) != null) {
							 * 
							 * try { customer.setTds_rate(new Float(row.getCell(22).getStringCellValue()));
							 * } catch(Exception e) { e.printStackTrace(); }
							 * 
							 * try { customer.setTds_rate((float) row.getCell(22).getNumericCellValue()); }
							 * catch(Exception e) { e.printStackTrace(); } } else {
							 * customer.setTds_rate((float)0); }
							 */

							
							if (row.getCell(23, row.CREATE_NULL_AS_BLANK) != null) {
								String suball = row.getCell(23).getStringCellValue();
								if (suball != null) {
									String[] sublist = suball.split(",");
									for (String subId : sublist) {
										subId = subId.trim();
										SubLedger sr = subLedgerService.isExists(subId, customer.getCompany_id());
										if (sr != null)
										{
											subLedgers.add(sr);
											fvar = 1;
										}
										else
											fvar = 2;
										
									}
									if(fvar==2){Err=true;ErrorMsgs.append(" Subledger incorrect");}
									
								}
								else {
									fvar=2;
									Err=true;
									ErrorMsgs.append(" Subledger cannot be blank ");
								}
							}else {
								fvar=2;
								Err=true;
								ErrorMsgs.append(" Subledger cannot be blank ");
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							
							if (row.getCell(24, row.CREATE_NULL_AS_BLANK) != null) {
								String prodall = row.getCell(24).getStringCellValue();
								if (prodall != null) {
									String[] prodlist = prodall.split(",");
									for (String prodId : prodlist) {
										prodId = prodId.trim();
										Product pr = productService.isExists(prodId, customer.getCompany_id());
										if (pr != null)
										{
											products.add(pr);
											fvar = 1;
										}
										else
											fvar = 2;
										
									}
									if(fvar==2){ErrorMsgs.insert(0, row.getRowNum());ErrorMsgs.append(" Product incorrect/does not exist ");}
								}else {
									fvar=2;
									ErrorMsgs.insert(0, row.getRowNum());
									ErrorMsgs.append(" Product cannot be blank ");
								}
							}
							else{
								fvar=2;
								Err=true;
								ErrorMsgs.append(" Product cannot be blank ");
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							
							customer.setSubLedgers(subLedgers);
							customer.setProduct(products);
							customer.setCustomer_approval(0);
							customer.setStatus(true);
							customer.setCompany(user.getCompany());
							customer.setCompany_id(company_id);
							customer.setCreated_by(user_id);
							if (fvarcount == 1) {
								m = 1;
								customer.setFlag(true);
							} else if (fvarcount == 0) {
								m = m + 2;
								customer.setFlag(false);
							}
							if (fvar != 0) {
								System.out.println("exist");
								Customer cr = customerService.isExistsCustomer(customer.getCompany_pan_no(),
										customer.getFirm_name(), company_id, customer.getContact_name());
								if (cr != null) {
									
									cr.setUpdated_by(user_id);
									customer.setUpdated_by(user_id);
									customer.setCreated_date(cr.getCreated_date());
									customer.setCreated_by(cr.getCreated_by());
									customer.setCustomer_id(cr.getCustomer_id());
									customerService.updateExcel(customer);
									if (fvarcount == 1) {
										m = 1;
										System.out.println("updated exst");
										updateList.add(customer.getContact_name());
										updatecount = updatecount + 1;
									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(customer.getContact_name());
										failcount = failcount + 1;
									}
								} else {
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									customer.setIp_address(remoteAddr);
									customer.setCreated_by(user_id);
									System.out.println("Mob no is " + customer.getMobile());
									customerService.add(customer);
									if (fvarcount == 1) {
										m = 1;
										successList.add(customer.getContact_name());
										successcount = successcount + 1;

									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(customer.getContact_name());
										failcount = failcount + 1;
									}
								}
							}

						}
						if(Err==true){String msg="----row no "+row.getRowNum();System.out.println("row is n"+row.getRowNum());ErrorMsgs.append(msg);ErrorMsgs.append("\n");}
					}
					workbook.close();
				} else {
					System.out.println("no data");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isValid = false;
		}
		if (successcount != 0) {
			successmsg += "<h5 class='green-lable'>" + successcount + " " + success + "</h5>";
		}
		if (updatecount != 0) {
			updatemsg += "<h5 class='orange-lable'>" + updatecount + " " + update + "</h5>";
		}
		if (failcount != 0) {
			failmsg += "<h5 class='red-lable'>" + failcount + " " + fail + "</h5>";
		}
		if ((successcount == 0) && (updatecount == 0) && (failcount == 0)) {
			error = "0 Record Imported, Please check file format";
		}
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
        } finally {
          if ( output != null ) {
            output.close();
          }
        }
		model.setViewName("redirect:/customersList");
		return model;
	}

	@RequestMapping(value = "deleteCustomers", method = RequestMethod.GET)
	public ModelAndView deleteCustomers(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		String msg = "You can't delete Customer";
		/*msg = customerService.deleteByIdValue(id);*/
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg", msg);
		if(importFlag == true)
		{
			
			return new ModelAndView("redirect:/customersList");
		}
		else
		{
			
			return new ModelAndView("redirect:/customerimportfailure");
		}
	}
	//@RequestMapping(value = "getAdvancePayment", method = RequestMethod.POST, produces =MediaType.APPLICATION_JSON_VALUE )
	@RequestMapping(value = "downloadCustomerMaster" , method =  RequestMethod.POST ,  produces =MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String downloadCustomerMaster(HttpServletRequest request,HttpServletResponse response)throws IOException {
		System.out.println("downloadCustomer");
		
		
		
		HttpSession session = request.getSession(true);
		List<Customer> customerList = new ArrayList<>();
		long company_id = (long) session.getAttribute("company_id");
		
		customerList = customerService.findAllCustomersOnlyOFCompany(company_id);
		
		JSONArray jsonArray = new JSONArray();
		for(Customer entry : customerList) {
			JSONObject jsonObj = new JSONObject();
		//	jsonObj.put("FirstName", entry.getf);
			jsonObj.put("firmname", entry.getFirm_name());
			jsonArray.put(jsonObj);
		}
		

		 return jsonArray.toString();}
	
	@RequestMapping(value = "deletefailureCustomers", method = RequestMethod.GET)
	public ModelAndView deletefailureCustomers(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
//		String msg = "You can't delete Customer";
		String msg="";
		msg = customerService.deleteByIdValue(id);
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg", msg);
		if(importFlag == true)
		{
			
			return new ModelAndView("redirect:/customersList");
		}
		else
		{
			
			return new ModelAndView("redirect:/customerimportfailure");
		}
	}
	
	
	@RequestMapping(value = "batchcustomer", method = RequestMethod.POST)
	public ModelAndView subledgerBatchApproval(@ModelAttribute("customerbatch") Customer customerbatch,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		Boolean flag = true;
		Boolean primaryApproval = null;
		Boolean rejectApproval = null;

		if (customerbatch.getPrimaryApproval() != null) {
			primaryApproval = customerbatch.getPrimaryApproval();
		}
		if (customerbatch.getRejectApproval() != null) {
			rejectApproval = customerbatch.getRejectApproval();
		}

		if (rejectApproval != null) {
			flag = customerService.rejectByBatch(customerbatch.getCustomerList());
			for( String cusId : customerbatch.getCustomerList())
			{
				yearService.saveActivityLogForm(user.getUser_id(), Long.parseLong(cusId), null, null, null, null, null, null, null, null, (long)APPROVAL_STATUS_REJECT);
			}
			if (flag) {
				session.setAttribute("successMsg", "Customer Rejected Successfully");
			} else {
				session.setAttribute("errorMsg", "Please select atleast one Customer.");
			}
			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && rejectApproval) {
				model.setViewName("redirect:/customerPrimaryValidationList");
			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !rejectApproval) {
				model.setViewName("redirect:/customerSecondaryValidationList");
			}
		}

		if (primaryApproval != null) {
			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && primaryApproval) {
				flag = customerService.approvedByBatch(customerbatch.getCustomerList(), primaryApproval);
				for( String cusId : customerbatch.getCustomerList())
				{
					yearService.saveActivityLogForm(user.getUser_id(), Long.parseLong(cusId),  null, null, null, null, null, null, (long)APPROVAL_STATUS_PRIMARY, null, null);
				}
				if (flag) {
					session.setAttribute("successMsg", "Customer Primary Approval Done Successfully");
				} else {
					session.setAttribute("errorMsg", "Please select atleast one Customer.");
				}
				model.setViewName("redirect:/customerPrimaryValidationList");

			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !primaryApproval) {
				flag = customerService.approvedByBatch(customerbatch.getCustomerList(), primaryApproval);
				
				for( String cusId : customerbatch.getCustomerList())
				{
					yearService.saveActivityLogForm(user.getUser_id(), Long.parseLong(cusId), null, null, null, null, null, null, null, (long)APPROVAL_STATUS_SECONDARY, null);
				}
				if (flag) {
					session.setAttribute("successMsg", "Customer Secondary Approval Done Successfully");
				} else {
					session.setAttribute("errorMsg", "Please select atleast one Customer.");
				}
				model.setViewName("redirect:/customerSecondaryValidationList");
			}
		}
		return model;
	}
	
	/* checking gst number for customer through ajax call */
	
	@RequestMapping(value="getGstNumberForCustomer", method=RequestMethod.GET)
	public @ResponseBody Boolean  getGstNumberCustomer(@RequestParam("id")long id ,HttpServletRequest request, HttpServletResponse response)
	{
		
		  HttpSession session = request.getSession(true); long company_id = (long)
		  session.getAttribute("company_id"); 
		  Boolean gstnumber = false; 
		 
		  Customer customer = customerService.findGstNoForCustomerByCompany(company_id, id);
		  
		  String gst_no = customer.getGst_no();
		  
		  if(gst_no != null && !gst_no.equals("")) {
				  
			 gstnumber = true;
				  
				
			 }else  {
				 
				 gstnumber= false;
				 
			 }
		
		return gstnumber;
	
	}
	
	
	
	
}