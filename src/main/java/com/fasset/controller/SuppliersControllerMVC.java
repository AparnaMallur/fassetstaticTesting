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
import com.fasset.controller.validators.SupplierValidator;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.City;
import com.fasset.entities.Company;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.entities.Country;
import com.fasset.entities.Deductee;
import com.fasset.entities.IndustryType;
import com.fasset.entities.Product;
import com.fasset.entities.State;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.service.interfaces.IAccountSubGroupService;
import com.fasset.service.interfaces.ICityService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICompanyStatutoryTypeService;
import com.fasset.service.interfaces.ICountryService;
import com.fasset.service.interfaces.IDeducteeService;
import com.fasset.service.interfaces.IProductService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IStateService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.fasset.service.interfaces.IYearEndingService;
import com.fasset.service.interfaces.IindustryTypeService;

@Controller
@SessionAttributes("user")
public class SuppliersControllerMVC extends MyAbstractController {
	@Autowired
	private ICompanyStatutoryTypeService companyStatutoryTypeService;

	@Autowired
	private IindustryTypeService industryTypeService;

	@Autowired
	private ICityService cityService;

	@Autowired
	private ICountryService countryService;

	@Autowired
	private IStateService stateService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private ISuplliersService service;

	@Autowired
	private SupplierValidator supplierValidater;

	@Autowired
	private IProductService productService;

	@Autowired
	private IAccountSubGroupService accountSubGroupService;

	@Autowired
	private ISubLedgerService subLedgerService;

	@Autowired
	private IDeducteeService deducteeService;

	@Autowired
	private IQuotationService quoteservice;

	@Autowired
	private IClientSubscriptionMasterService subscribeservice;

	@Autowired
	private IYearEndingService yearService;

	private Company company = new Company();
	/* private IndustryType type = new IndustryType(); */
	private Set<SubLedger> subLedgerList = new HashSet<SubLedger>();
	private List<Product> productList = new ArrayList<Product>();
	private Set<SubLedger> subLedgers = new HashSet<SubLedger>();
	private Set<Product> suppilerproductList = new HashSet<Product>();
	private List<Deductee> deducteeList = new ArrayList<>();

	Boolean importFlag = null; // for maintaining the user on suppliersList.jsp after delete and view
	Boolean isImport = false; // is Supplier saved or updated through excel or through System(Add Supplier)
	private List<String> successList = new ArrayList<String>();
	private List<String> failureList = new ArrayList<String>();
	private List<String> updateList = new ArrayList<String>();
	private String successmsg;
	private String failmsg;
	private String updatemsg;
	private String error;

	@RequestMapping(value = "suppliers", method = RequestMethod.GET)
	public ModelAndView supplier(HttpServletRequest request, HttpServletResponse response) {
System.out.println("Add new supplier");
		ModelAndView model = new ModelAndView();
		Suppliers supplier = new Suppliers();
		HttpSession session = request.getSession(true);
		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		if (role == ROLE_SUPERUSER) {
			System.out.println("superuser subledger");
			List<Company> companyList = companyService.list();
			model.addObject("companyList", companyList);
			//List<SubLedger> subLedgerList = subLedgerService.findAllSubLedgerWithLedger();//get Expenditure and Discount subledger
			List<SubLedger> subLedgerList = subLedgerService.findAllSubLedgerWithLedgerForCustSupplier((long) 1, false, true);
			model.addObject("subLedgerList", subLedgerList);
			productList = productService.list();
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			System.out.println("Exe or mgr");
			List<Company> companyList = companyService.getAllCompaniesExe(user_id);
			model.addObject("companyList", companyList);
			System.out.println("executive subledger");
			//List<SubLedger> subLedgerList = subLedgerService.findAllSubLedgerWithLedger(user_id); //get Expenditure and Discount subledger
			List<SubLedger> subLedgerList = subLedgerService.findAllSubLedgerWithLedgerForCustSupplier(user_id, false, false);
			model.addObject("subLedgerList", subLedgerList);
			productList = productService.list();

		} else {
			try {
				company = companyService.getCompanyWithIndustrytype(company_id);
				/*
				 * type = company.getIndustry_type(); Set<SubLedger> subLedgerListindustry =
				 * type.getSubLedgers();
				 */
				System.out.println("else subledger");
				List<SubLedger> subLedgerList = subLedgerService.findAllSubLedgerWithLedgerForCustSupplier(user_id, false, false);
				//List<SubLedger> subLedgerList = subLedgerService.findAllApprovedByCompanyForSupplier(company_id);
				productList = productService.findAllProductsOfCompany(company_id);
				/* model.addObject("subLedgerListindustry", subLedgerListindustry); */
				model.addObject("subLedgerList", subLedgerList);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<CompanyStatutoryType> statutoryTypeList = companyStatutoryTypeService.findAll();
		List<City> cityList = cityService.findAll();
		List<Country> countryList = countryService.findAll();
		List<State> stateList = stateService.findAll();
		List<AccountSubGroup> accountSubGroupList = accountSubGroupService.findAll();
		List<IndustryType> industryTypeList = industryTypeService.findAll();
		deducteeList = deducteeService.list();

		model.addObject("deducteeList", deducteeList);
		model.addObject("supplier", supplier);
		model.addObject("statutoryTypeList", statutoryTypeList);
		model.addObject("cityList", cityList);
		model.addObject("countryList", countryList);
		model.addObject("stateList", stateList);
		model.addObject("accountSubGroupList", accountSubGroupList);
		model.addObject("productList", productList);
		model.addObject("industryTypeList", industryTypeList);
		model.setViewName("/master/suppliers");
		return model;
	}
	@RequestMapping(value = "downloadsupplier" , method =  RequestMethod.POST ,  produces =MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String downloadsupplier(HttpServletRequest request,HttpServletResponse response)throws IOException {
		System.out.println("downloadCustomer");
		
		
		
		HttpSession session = request.getSession(true);
		List<Suppliers> supplierList = new ArrayList<>();
		long company_id = (long) session.getAttribute("company_id");
		supplierList=service.findAllSuppliersOnlyOfCompany(company_id);
				
		JSONArray jsonArray = new JSONArray();
		for(Suppliers entry : supplierList) {
			JSONObject jsonObj = new JSONObject();
		//	jsonObj.put("FirstName", entry.getf);
			jsonObj.put("suppliername", entry.getCompany_name());
			jsonArray.put(jsonObj);
		}
		

		 return jsonArray.toString();}
	@RequestMapping(value = "savesuppliers", method = RequestMethod.POST)
	public ModelAndView savesuppliers(@ModelAttribute("supplier") Suppliers supplier, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
		HttpSession session = request.getSession(true);
		ModelAndView model = new ModelAndView();
		long role = (long) session.getAttribute("role");
		long user_id = (long) session.getAttribute("user_id");
		long company_id = (long) session.getAttribute("company_id");
		supplierValidater.validate(supplier, result);
		if (result.hasErrors()) {

			if (role == ROLE_SUPERUSER) {
				List<Company> companyList = companyService.list();
				model.addObject("companyList", companyList);
			//	List<SubLedger> subLedgerList = subLedgerService.findAllSubLedgerWithLedger();//get Expenditure and Discount subledger
				List<SubLedger> subLedgerList = subLedgerService.findAllSubLedgerWithLedgerForCustSupplier((long) 1, false, true);
				model.addObject("subLedgerList", subLedgerList);
				productList = productService.list();
			} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				List<Company> companyList = companyService.getAllCompaniesExe(user_id);
				model.addObject("companyList", companyList);
				//List<SubLedger> subLedgerList = subLedgerService.findAllSubLedgerWithLedger(user_id); //get Expenditure and Discount subledger
				List<SubLedger> subLedgerList = subLedgerService.findAllSubLedgerWithLedgerForCustSupplier(user_id, false, false);
				model.addObject("subLedgerList", subLedgerList);
				productList = productService.list();

			} else {
				company = companyService.getCompanyWithIndustrytype(company_id);
				/*
				 * type = company.getIndustry_type(); Set<SubLedger> subLedgerListindustry =
				 * type.getSubLedgers();
				 */
				//List<SubLedger> subLedgerList = subLedgerService.findAllApprovedByCompany(company_id); //get Expenditure and Discount subledger
				List<SubLedger> subLedgerList = subLedgerService.findAllApprovedByCompanyForSupplier(company_id);
				productList = productService.findAllProductsOfCompany(company_id);
				model.addObject("subLedgerList", subLedgerList);
				/* model.addObject("subLedgerListindustry", subLedgerListindustry); */
			}
			List<CompanyStatutoryType> statutoryTypeList = companyStatutoryTypeService.findAll();
			List<City> cityList = cityService.findAll();
			List<Country> countryList = countryService.findAll();
			List<State> stateList = stateService.findAll();
			List<AccountSubGroup> accountSubGroupList = accountSubGroupService.findAll();
			List<IndustryType> industryTypeList = industryTypeService.findAll();
			List<Deductee> deducteeList = deducteeService.list();
			model.addObject("deducteeList", deducteeList);

			model.addObject("statutoryTypeList", statutoryTypeList);
			model.addObject("cityList", cityList);
			model.addObject("countryList", countryList);
			model.addObject("stateList", stateList);
			model.addObject("productList", productList);
			model.addObject("industryTypeList", industryTypeList);
			model.addObject("accountSubGroupList", accountSubGroupList);

			if (supplier.getSupplier_id() != null) {
				model.addObject("subLedgers", subLedgers);
				model.addObject("suppilerproductList", suppilerproductList);
			}

			model.setViewName("/master/suppliers");

		} else {
			String msg = "";
			try {
				if (supplier.getSupplier_id() != null) {
					long id = supplier.getSupplier_id();
					// long user_type = user.getRole().getRole_id();

					if (id > 0) {
						supplier.setUpdated_by(user_id);
						supplier.setCompany(companyService.getById(supplier.getCompany_id()));
						Suppliers oldsupplier = service.findOneWithAll(supplier.getSupplier_id());
						if ((oldsupplier.getSupplier_approval() == 1) || (oldsupplier.getSupplier_approval() == 3)) {
							if (role == ROLE_EXECUTIVE)
								supplier.setSupplier_approval(2);
							else if ((role == ROLE_CLIENT) || (role == ROLE_EMPLOYEE) || (role == ROLE_TRIAL_USER))
								supplier.setSupplier_approval(0);
							else
								supplier.setSupplier_approval(oldsupplier.getSupplier_approval());
						} else {
							supplier.setSupplier_approval(oldsupplier.getSupplier_approval());
						}
						service.update(supplier);
						msg = "Supplier updated successfully";
					}
				} else {
					supplier.setSupplier_approval(APPROVAL_STATUS_PENDING);
					if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {

						supplier.setCompany(companyService.getById(supplier.getCompany_id()));
					} else {
						supplier.setCompany(company);
					}
					supplier.setFlag(true);
					supplier.setCreated_by(user_id);

					String remoteAddr = "";
					remoteAddr = request.getHeader("X-FORWARDED-FOR");
					if (remoteAddr == null || "".equals(remoteAddr)) {
						remoteAddr = request.getRemoteAddr();
					}
					supplier.setIp_address(remoteAddr);
					msg = service.saveSuppliers(supplier);
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/suppliersList");
		}
		return model;
	}

	@RequestMapping(value = "suppliersList", method = RequestMethod.GET)
	public ModelAndView suppliersList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		List<Suppliers> supplierList = new ArrayList<Suppliers>();
		List<Suppliers> supplierListFailure = new ArrayList<Suppliers>();
		boolean importfail = false;
		boolean importflag = true;
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

		if (role == ROLE_SUPERUSER || role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			if (role == ROLE_SUPERUSER) {
				supplierList = service.findAllSuppliersListing((long) 1);
				supplierListFailure = service.findAllSuppliersListing((long) 0);
			}
			if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				supplierList = service.findAllSuppliersListing(true, user.getUser_id());
				supplierListFailure = service.findAllSuppliersListing(false, user.getUser_id());
			}
			if (supplierListFailure.size() != 0)
				importfail = true;

		} else {
			supplierList = service.findAllSuppliersListingOfCompany(company_id, (long) 1);
			supplierListFailure = service.findAllSuppliersListingOfCompany(company_id, (long) 0);
			if (supplierListFailure.size() != 0)
				importfail = true;
			Long quote_id = subscribeservice.getquoteofcompany(company_id);
			String email = user.getCompany().getEmail_id();
			if (quote_id != 0) {
				importflag = quoteservice.findAllimportDetailsUser(email, quote_id); // for subscribed client if he
																						// registered his company with
																						// quotation and quotation
																						// contains master imports
																						// facility.
			} else {
				importflag = quoteservice.findAllimportDetailsUser(email, quote_id);
			}
			/*
			 * if(user.getCompany().getCreated_by()!=null)// for company registered without
			 * quotation flow. This will be use for master imports as quotation is not
			 * present for this company. { importflag=true; }
			 */
		}
		if (isImport == false) { // code for import . here we are refreshing list and massages which are declared
									// as global.
			successList.clear();
			failureList.clear();
			updateList.clear();
			successmsg = null;
			failmsg = null;
			updatemsg = null;
			error = null;
			// code for import . here we are refreshing list and massages which are declared
			// as global.
		} else if (isImport == true) {
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
			model.addObject("failureList", failureList);
			model.addObject("updateList", updateList);
			model.addObject("successImportmsg", successmsg);
			model.addObject("failmsg", failmsg);
			model.addObject("updatemsg", updatemsg);
			model.addObject("error", error);
			model.addObject("isImport", true);
			// code for import
			isImport = false;
		}
		model.addObject("importfail", importfail);
		model.addObject("importflag", importflag);
		model.addObject("supplierList", supplierList);
		model.setViewName("/master/supplierList");
		return model;
	}

	@RequestMapping(value = "supplierimportfailure", method = RequestMethod.GET)
	public ModelAndView supplierimportfailure(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		List<Suppliers> supplierList = new ArrayList<Suppliers>();
		importFlag = false;
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (role == ROLE_SUPERUSER || role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			if (role == ROLE_SUPERUSER) {
				supplierList = service.findAllSuppliersListing((long) 0);
			}
			if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				supplierList = service.findAllSuppliersListing(false, user.getUser_id());
			}
			model.addObject("supplierList", supplierList);
			model.setViewName("/master/supplierList");
		} else {
			supplierList = service.findAllSuppliersListingOfCompany(company_id, (long) 0);
			model.addObject("supplierList", supplierList);
			model.setViewName("/master/FailureSuppliersList");
		}
		return model;
	}

	@RequestMapping(value = "viewSupplier", method = RequestMethod.GET)
	public ModelAndView viewSupplier(@RequestParam("id") long id, @RequestParam("flag") long flag) {
		ModelAndView model = new ModelAndView();
		Suppliers supplier = new Suppliers();
		try {
			supplier = service.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addObject("supplier", supplier);
		model.addObject("view", "view");
		model.addObject("flag", flag);
		model.addObject("importFlag", importFlag);
		model.setViewName("/master/supplierView");
		return model;
	}

	@RequestMapping(value = "editSuppliers", method = RequestMethod.GET)
	public ModelAndView editSuppliers(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		Long supplierid = id;
		HttpSession session = request.getSession(true);
		session.setAttribute("supplierid", supplierid);
		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		List<SubLedger> compsubLedgerList = new ArrayList<>();
		Suppliers supplier = new Suppliers();
		supplier = service.findOneWithAll(id);

		if (role == ROLE_SUPERUSER) {
			List<Company> companyList = companyService.list();
			model.addObject("companyList", companyList);

			//List<SubLedger> subLedgerList = subLedgerService
			//		.findAllApprovedByCompany(supplier.getCompany().getCompany_id()); //get Expenses and Discount subledger
			List<SubLedger> subLedgerList = subLedgerService
							.findAllApprovedByCompanyForSupplier(supplier.getCompany().getCompany_id());
			model.addObject("subLedgerList", subLedgerList);
			productList = productService.findAllProductsOfCompany(supplier.getCompany().getCompany_id());
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			List<Company> companyList = companyService.getAllCompaniesExe(user_id);
			model.addObject("companyList", companyList);

			//List<SubLedger> subLedgerList = subLedgerService
			//		.findAllApprovedByCompany(supplier.getCompany().getCompany_id()); //get Expenses and Discount subledger
			
			List<SubLedger> subLedgerList = subLedgerService
						.findAllApprovedByCompanyForSupplier(supplier.getCompany().getCompany_id());
			model.addObject("subLedgerList", subLedgerList);
			productList = productService.findAllProductsOfCompany(supplier.getCompany().getCompany_id());

		} else {
			try {

				//List<SubLedger> subLedgerList = subLedgerService.findAllApprovedByCompany(company_id);//get Expenses and Discount subledger
				List<SubLedger> subLedgerList = subLedgerService.findAllApprovedByCompanyForSupplier(company_id);
				productList = productService.findAllProductsOfCompany(company_id);
				/*
				 * company = companyService.getCompanyWithIndustrytype(company_id); type =
				 * company.getIndustry_type();
				 */
				/*
				 * Set<SubLedger> subLedgerListindustry = type.getSubLedgers();
				 * model.addObject("subLedgerListindustry", subLedgerListindustry);
				 */
				model.addObject("subLedgerList", subLedgerList);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		List<CompanyStatutoryType> statutoryTypeList = companyStatutoryTypeService.findAll();
		List<IndustryType> industryTypeList = industryTypeService.findAll();
		List<City> cityList = cityService.findAll();
		List<Country> countryList = countryService.findAll();
		List<State> stateList = stateService.findAll();
		List<AccountSubGroup> accountSubGroupList = accountSubGroupService.findAll();
		deducteeList = deducteeService.list();

		try {
			if (id > 0) {

				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					if (supplier.getCompany().getCompany_id() != null) {
						supplier.setCompany_id(supplier.getCompany().getCompany_id());
					}
				}
				subLedgers = supplier.getSubLedgers();
				suppilerproductList = supplier.getProduct();

				if (supplier.getCompStatType() != null) {
					supplier.setCompany_statutory_id(supplier.getCompStatType().getCompany_statutory_id());
				}
				if (supplier.getCity() != null) {
					supplier.setCity_id(supplier.getCity().getCity_id());
				}
				if (supplier.getCountry() != null) {
					supplier.setCountry_id(supplier.getCountry().getCountry_id());
				}
				if (supplier.getState() != null) {
					supplier.setState_id(supplier.getState().getState_id());
				}
				if (supplier.getIndustryType() != null) {
					supplier.setIndustry_id(supplier.getIndustryType().getIndustry_id());
				}
				if (supplier.getTds_rate() != null) {
					supplier.setTds_rate1(supplier.getTds_rate().toString());
				}
				if (supplier.getCredit_opening_balance() != null) {
					supplier.setCredit_opening_balance1(supplier.getCredit_opening_balance().toString());
				}
				if (supplier.getDebit_opening_balance() != null) {
					supplier.setDebit_opening_balance1(supplier.getDebit_opening_balance().toString());
				}
				if (supplier.getDeductee() != null) {
					supplier.setDeductee_id(supplier.getDeductee().getDeductee_id());
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

		model.addObject("supplier", supplier);
		model.addObject("statutoryTypeList", statutoryTypeList);
		model.addObject("industryTypeList", industryTypeList);
		model.addObject("deducteeList", deducteeList);
		model.addObject("cityList", cityList);
		model.addObject("countryList", countryList);
		model.addObject("stateList", stateList);
		model.addObject("productList", productList);
		model.addObject("accountSubGroupList", accountSubGroupList);
		model.addObject("subLedgers", subLedgers);
		model.addObject("suppilerproductList", suppilerproductList);
		model.setViewName("/master/suppliers");
		return model;
	}

	@RequestMapping(value = "deleteSupplierSubLedger", method = RequestMethod.GET)
	public ModelAndView deleteSupplierSubLedger(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long user_id = (long) session.getAttribute("user_id");
		Long supplierid = (Long) session.getAttribute("supplierid");
		String msg = "";
		msg = service.deleteSupplierSubLedger(supplierid, id);
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		List<SubLedger> compsubLedgerList = new ArrayList<>();
		Suppliers supplier = new Suppliers();
		supplier = service.findOneWithAll(supplierid);
		if (role == ROLE_SUPERUSER) {
			List<Company> companyList = companyService.list();
			model.addObject("companyList", companyList);

			//List<SubLedger> subLedgerList = subLedgerService
				//	.findAllApprovedByCompany(supplier.getCompany().getCompany_id()); //get Expenses and discount subledger
			List<SubLedger> subLedgerList = subLedgerService
					.findAllApprovedByCompanyForSupplier(supplier.getCompany().getCompany_id());
			model.addObject("subLedgerList", subLedgerList);
			productList = productService.findAllProductsOfCompany(supplier.getCompany().getCompany_id());
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			List<Company> companyList = companyService.getAllCompaniesExe(user_id);
			model.addObject("companyList", companyList);

			//List<SubLedger> subLedgerList = subLedgerService
					//.findAllApprovedByCompany(supplier.getCompany().getCompany_id()); //get Expenses and discount subledger
			List<SubLedger> subLedgerList = subLedgerService
					.findAllApprovedByCompanyForSupplier(supplier.getCompany().getCompany_id());
			model.addObject("subLedgerList", subLedgerList);
			productList = productService.findAllProductsOfCompany(supplier.getCompany().getCompany_id());

		} else {
			try {
				//List<SubLedger> subLedgerList = subLedgerService.findAllApprovedByCompany(company_id); //get Expenses and discount subledger
				List<SubLedger> subLedgerList = subLedgerService.findAllApprovedByCompanyForSupplier(company_id);
				productList = productService.findAllProductsOfCompany(company_id);
				/*
				 * company = companyService.getCompanyWithIndustrytype(company_id); type =
				 * company.getIndustry_type(); Set<SubLedger> subLedgerListindustry =
				 * type.getSubLedgers(); model.addObject("subLedgerListindustry",
				 * subLedgerListindustry);
				 */
				model.addObject("subLedgerList", subLedgerList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List<CompanyStatutoryType> statutoryTypeList = companyStatutoryTypeService.findAll();
		List<IndustryType> industryTypeList = industryTypeService.findAll();
		List<City> cityList = cityService.findAll();
		List<Country> countryList = countryService.findAll();
		List<State> stateList = stateService.findAll();
		List<AccountSubGroup> accountSubGroupList = accountSubGroupService.findAll();
		deducteeList = deducteeService.list();

		try {
			if (id > 0) {

				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					if (supplier.getCompany().getCompany_id() != null) {
						supplier.setCompany_id(supplier.getCompany().getCompany_id());
					}
				}
				subLedgers = supplier.getSubLedgers();
				suppilerproductList = supplier.getProduct();

				if (supplier.getCompStatType() != null) {
					supplier.setCompany_statutory_id(supplier.getCompStatType().getCompany_statutory_id());
				}
				if (supplier.getCity() != null) {
					supplier.setCity_id(supplier.getCity().getCity_id());
				}
				if (supplier.getCountry() != null) {
					supplier.setCountry_id(supplier.getCountry().getCountry_id());
				}
				if (supplier.getState() != null) {
					supplier.setState_id(supplier.getState().getState_id());
				}
				if (supplier.getIndustryType() != null) {
					supplier.setIndustry_id(supplier.getIndustryType().getIndustry_id());
				}
				if (supplier.getTds_rate() != null) {
					supplier.setTds_rate1(supplier.getTds_rate().toString());
				}
				if (supplier.getCredit_opening_balance() != null) {
					supplier.setCredit_opening_balance1(supplier.getCredit_opening_balance().toString());
				}
				if (supplier.getDebit_opening_balance() != null) {
					supplier.setDebit_opening_balance1(supplier.getDebit_opening_balance().toString());
				}
				if (supplier.getDeductee() != null) {
					supplier.setDeductee_id(supplier.getDeductee().getDeductee_id());
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

		model.addObject("supplier", supplier);
		model.addObject("statutoryTypeList", statutoryTypeList);
		model.addObject("industryTypeList", industryTypeList);
		model.addObject("deducteeList", deducteeList);
		model.addObject("cityList", cityList);
		model.addObject("countryList", countryList);
		model.addObject("stateList", stateList);
		model.addObject("productList", productList);
		model.addObject("accountSubGroupList", accountSubGroupList);
		model.addObject("subLedgers", subLedgers);
		model.addObject("suppilerproductList", suppilerproductList);
		model.addObject("successMsg", msg);
		model.setViewName("/master/suppliers");
		return model;
	}

	@RequestMapping(value = "deleteSupplierProduct", method = RequestMethod.GET)
	public ModelAndView deleteSupplierProduct(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		Long supplierid = (Long) session.getAttribute("supplierid");

		String msg = "";
		msg = service.deleteSupplierProduct(supplierid, id);
		List<SubLedger> compsubLedgerList = new ArrayList<>();
		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		Suppliers supplier = new Suppliers();
		supplier = service.findOneWithAll(supplierid);
		if (role == ROLE_SUPERUSER) {
			List<Company> companyList = companyService.list();
			model.addObject("companyList", companyList);

		//	List<SubLedger> subLedgerList = subLedgerService
				//	.findAllApprovedByCompany(supplier.getCompany().getCompany_id()); //get Expenses and discount subledger
			
			List<SubLedger> subLedgerList = subLedgerService
					.findAllApprovedByCompanyForSupplier(supplier.getCompany().getCompany_id());
			model.addObject("subLedgerList", subLedgerList);
			productList = productService.findAllProductsOfCompany(supplier.getCompany().getCompany_id());
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			List<Company> companyList = companyService.getAllCompaniesExe(user_id);
			model.addObject("companyList", companyList);

		//	List<SubLedger> subLedgerList = subLedgerService
			//		.findAllApprovedByCompany(supplier.getCompany().getCompany_id()); //get Expenses and discount subledger
			List<SubLedger> subLedgerList = subLedgerService
					.findAllApprovedByCompanyForSupplier(supplier.getCompany().getCompany_id());
			model.addObject("subLedgerList", subLedgerList);
			productList = productService.findAllProductsOfCompany(supplier.getCompany().getCompany_id());

		} else {
			try {
			//	List<SubLedger> subLedgerList = subLedgerService.findAllApprovedByCompany(company_id); //get Expenses and discount subledger
				List<SubLedger> subLedgerList = subLedgerService.findAllApprovedByCompanyForSupplier(company_id);
				productList = productService.findAllProductsOfCompany(company_id);
				/*
				 * company = companyService.getCompanyWithIndustrytype(company_id); type =
				 * company.getIndustry_type(); Set<SubLedger> subLedgerListindustry =
				 * type.getSubLedgers(); model.addObject("subLedgerListindustry",
				 * subLedgerListindustry);
				 */
				model.addObject("subLedgerList", subLedgerList);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		List<CompanyStatutoryType> statutoryTypeList = companyStatutoryTypeService.findAll();
		List<IndustryType> industryTypeList = industryTypeService.findAll();
		List<City> cityList = cityService.findAll();
		List<Country> countryList = countryService.findAll();
		List<State> stateList = stateService.findAll();
		List<AccountSubGroup> accountSubGroupList = accountSubGroupService.findAll();
		deducteeList = deducteeService.list();
		try {
			if (id > 0) {

				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					if (supplier.getCompany().getCompany_id() != null) {
						supplier.setCompany_id(supplier.getCompany().getCompany_id());
					}
				}
				subLedgers = supplier.getSubLedgers();
				suppilerproductList = supplier.getProduct();

				if (supplier.getCompStatType() != null) {
					supplier.setCompany_statutory_id(supplier.getCompStatType().getCompany_statutory_id());
				}
				if (supplier.getCity() != null) {
					supplier.setCity_id(supplier.getCity().getCity_id());
				}
				if (supplier.getCountry() != null) {
					supplier.setCountry_id(supplier.getCountry().getCountry_id());
				}
				if (supplier.getState() != null) {
					supplier.setState_id(supplier.getState().getState_id());
				}
				if (supplier.getIndustryType() != null) {
					supplier.setIndustry_id(supplier.getIndustryType().getIndustry_id());
				}
				if (supplier.getTds_rate() != null) {
					supplier.setTds_rate1(supplier.getTds_rate().toString());
				}
				if (supplier.getCredit_opening_balance() != null) {
					supplier.setCredit_opening_balance1(supplier.getCredit_opening_balance().toString());
				}
				if (supplier.getDebit_opening_balance() != null) {
					supplier.setDebit_opening_balance1(supplier.getDebit_opening_balance().toString());
				}
				if (supplier.getDeductee() != null) {
					supplier.setDeductee_id(supplier.getDeductee().getDeductee_id());
				}
				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					for (SubLedger subLedger : subLedgers) {
						compsubLedgerList.remove(subLedger);
					}
				} else {
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
		model.addObject("supplier", supplier);
		model.addObject("statutoryTypeList", statutoryTypeList);
		model.addObject("industryTypeList", industryTypeList);
		model.addObject("deducteeList", deducteeList);
		model.addObject("cityList", cityList);
		model.addObject("countryList", countryList);
		model.addObject("stateList", stateList);
		model.addObject("productList", productList);
		model.addObject("accountSubGroupList", accountSubGroupList);
		model.addObject("subLedgers", subLedgers);
		model.addObject("suppilerproductList", suppilerproductList);
		model.addObject("successMsg", msg);
		model.setViewName("/master/suppliers");
		return model;
	}

	@RequestMapping(value = "supplierPrimaryValidation", method = RequestMethod.GET)
	public ModelAndView supplierPrimaryValidation(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		Suppliers suppbatch = new Suppliers();
		List<Suppliers> supplierList = new ArrayList<Suppliers>();
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

		if (role == ROLE_SUPERUSER) {
			supplierList = service.findByStatus(APPROVAL_STATUS_PENDING);
		}
		if (role == ROLE_EXECUTIVE) {
			supplierList = service.findByStatus(role, APPROVAL_STATUS_PENDING, user.getUser_id());
		}
		model.addObject("supplierList", supplierList);
		model.addObject("supplierbatch", suppbatch);
		model.setViewName("/master/supplierPrimaryValidation");
		return model;
	}

	@RequestMapping(value = "supplierSecondaryValidation", method = RequestMethod.GET)
	public ModelAndView supplierSecondaryValidation(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		Suppliers suppbatch = new Suppliers();
		List<Suppliers> supplierList = new ArrayList<Suppliers>();
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

		if (role == ROLE_SUPERUSER) {
			supplierList = service.findByStatus(APPROVAL_STATUS_PRIMARY);
		}
		if (role == ROLE_MANAGER) {
			supplierList = service.findByStatus(role, APPROVAL_STATUS_PRIMARY, user.getUser_id());
		}
		model.addObject("supplierList", supplierList);
		model.addObject("supplierbatch", suppbatch);
		model.setViewName("/master/supplierSecondaryValidation");
		return model;
	}

	@RequestMapping(value = "approveSupplier", method = RequestMethod.GET)
	public ModelAndView approveSupplier(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("primaryApproval") Boolean primaryApproval) {
		ModelAndView model = new ModelAndView();
		Long supplierId = id;
		String msg = "";
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");

		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && primaryApproval == true)) {
			msg = service.updateByApproval(supplierId, APPROVAL_STATUS_PRIMARY);
			yearService.saveActivityLogForm(user.getUser_id(), null, supplierId, null, null, null, null, null,
					(long) APPROVAL_STATUS_PRIMARY, null, null);
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/supplierPrimaryValidation");
		} else {
			msg = service.updateByApproval(supplierId, APPROVAL_STATUS_SECONDARY);
			yearService.saveActivityLogForm(user.getUser_id(), null, supplierId, null, null, null, null, null, null,
					(long) APPROVAL_STATUS_SECONDARY, null);
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/supplierSecondaryValidation");
		}
		return model;
	}

	@RequestMapping(value = "rejectSupplier", method = RequestMethod.GET)
	public ModelAndView rejectSupplier(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("rejectApproval") Boolean rejectApproval) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		Long supplierId = id;
		String msg = "";
		msg = service.updateByApproval(supplierId, APPROVAL_STATUS_REJECT);
		yearService.saveActivityLogForm(user.getUser_id(), null, supplierId, null, null, null, null, null, null, null,
				(long) APPROVAL_STATUS_REJECT);
		session.setAttribute("successMsg", msg);
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && rejectApproval)) {
			model.setViewName("redirect:/supplierPrimaryValidation");
		} else {
			model.setViewName("redirect:/supplierSecondaryValidation");
		}
		return model;
	}

	@RequestMapping(value = { "importExcel" }, method = { RequestMethod.POST })
	public ModelAndView importExcel(@RequestParam("excelFile") MultipartFile excelfile, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView();
		boolean isValid = true;
		String success = "Record Imported successfully";
		int successcount = 0;
		String fail = "in failure list, Please refer failure list";
		StringBuffer ErrorMsgs=new StringBuffer();
		boolean Err=false;
		int failcount = 0;
		String update = "Updated List";
		int updatecount = 0;
		successmsg = "";
		failmsg = "";
		updatemsg = "";
		error = "";
		isImport = true;
		successList.clear();
		failureList.clear();
		updateList.clear();
		int m = 0;
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		long user_id = (long) session.getAttribute("user_id");
		try {
			if (excelfile.getOriginalFilename().endsWith("xls")) {
				int i = 0;
				isValid = true;
				HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
				HSSFSheet worksheet = workbook.getSheetAt(0);
				int rowcount = 0;
				while (i <= worksheet.getLastRowNum()) {
					int colcount = 0;
					HSSFRow row = worksheet.getRow(i++);
					
					for (int j = 0; j <= 25; j++) {
						if (row.getCell(j) != null) {
							colcount++;
						}
					}
					if (colcount >= 16) {
						rowcount++;
					}
				}

				if (isValid) {
					i = 1;
					while (i <= rowcount) {
						Suppliers supplier = new Suppliers();
						HSSFRow row = worksheet.getRow(i++);
						//ErrorMsgs.append(System.lineSeparator());
						Err=false;
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer fvar = 0;
							Integer fvarcount = 1;
							Set<SubLedger> subLedgersList = new HashSet<SubLedger>();
							Set<Product> products = new HashSet<Product>();
							List<CompanyStatutoryType> companyStatutoryList = companyStatutoryTypeService.findAll();
							try {
								for (CompanyStatutoryType companyStatutory : companyStatutoryList) {
									String str = "";
									str = companyStatutory.getCompany_statutory_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(row.getCell(4).getStringCellValue().replaceAll(" ", ""))) {
										supplier.setCompany_statutory_id(companyStatutory.getCompany_statutory_id());
										fvar = 1;
										break;
									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" Company Statutory Type incorrect ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							System.out.println(fvar);
							List<IndustryType> industryList = industryTypeService.findAll();
							try {
								for (IndustryType industry : industryList) {
									String str = "";
									str = industry.getIndustry_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(row.getCell(5).getStringCellValue().replaceAll(" ", ""))) {
										supplier.setIndustry_id(industry.getIndustry_id());
										fvar = 1;
										break;

									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" Industry Type incorrect/does not exist ");}
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
										supplier.setCountry_id(country.getCountry_id());
										fvar = 1;
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
							System.out.println(fvar);
							List<State> stateList = stateService.findAll();
							try {
								for (State state : stateList) {
									String str = "";
									str = state.getState_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(
											row.getCell(17).getStringCellValue().replaceAll(" ", ""))) {
										if (state.getCntry().getCountry_id() == supplier.getCountry_id()) {
											supplier.setState_id(state.getState_id());
											fvar = 1;
											break;
										} else {
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
										if (city.getState().getState_id() == supplier.getState_id()) {
											supplier.setCity_id(city.getCity_id());
											fvar = 1;
											break;
										} else {
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
												row.getCell(26).getStringCellValue().replaceAll(" ", ""))) {
											supplier.setCompany(comp);
											supplier.setCompany_id(comp.getCompany_id());
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
								supplier.setCompany(user.getCompany());
								supplier.setCompany_id(company_id);
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							supplier.setContact_name(row.getCell(0).getStringCellValue());

							try {
								supplier.setMobile(row.getCell(1).getStringCellValue());
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								Double doubleValue = row.getCell(1).getNumericCellValue();
								BigDecimal bd = new BigDecimal(doubleValue.toString());
							
								long lonVal = bd.longValue();
								
								String Mobile = Long.toString(lonVal).trim();
								//supplier.setMobile((((Double) row.getCell(1).getNumericCellValue())).toString().trim());
								supplier.setMobile(Mobile);
							} catch (Exception e) {
								e.printStackTrace();
							}
							supplier.setEmail_id(row.getCell(2).getStringCellValue());
							supplier.setCompany_name(row.getCell(3).getStringCellValue());
							String company_name = supplier.getCompany_name().replace("\"", "").replace("\'", "");
							supplier.setCompany_name(company_name);
							if (row.getCell(6, row.CREATE_NULL_AS_BLANK) != null) {

								try {
									supplier.setLandline_no(row.getCell(6).getStringCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
								try {
									supplier.setLandline_no(
											(((Double) row.getCell(6).getNumericCellValue())).toString().trim());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}

							}
							if (row.getCell(7, row.CREATE_NULL_AS_BLANK) != null) {
								try {
									supplier.setOwner_pan_no(row.getCell(7).getStringCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
									supplier.setOwner_pan_no("0");
								}

							}
							if (row.getCell(8, row.CREATE_NULL_AS_BLANK) != null) {
								try {
									supplier.setAdhaar_no(row.getCell(8).getStringCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
								try {
									supplier.setAdhaar_no(
											(((Double) row.getCell(8).getNumericCellValue())).toString().trim());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
							}
							boolean gstapply = row.getCell(9).getStringCellValue().matches("Yes");
							if (gstapply == true) {
								supplier.setGst_applicable(gstapply);
								if (row.getCell(10, row.CREATE_NULL_AS_BLANK) != null) {
									try {
										supplier.setGst_no(row.getCell(10).getStringCellValue());
									} catch (Exception e) {
										/* e.printStackTrace(); */
									}
									try {
										supplier.setGst_no(
												((Integer) ((Double) row.getCell(10).getNumericCellValue()).intValue())
														.toString().trim());
									} catch (Exception e) {
										/* e.printStackTrace(); */
									}

								}
								supplier.setReverse_mecha(row.getCell(20).getStringCellValue());
							} else {
								supplier.setGst_applicable(gstapply);
								supplier.setReverse_mecha("No");
							}

							try {
								supplier.setCompany_pan_no(row.getCell(11).getStringCellValue());
							} catch (Exception e) {
								supplier.setCompany_pan_no("0");
								/* e.printStackTrace(); */
							}

							if (row.getCell(12, row.CREATE_NULL_AS_BLANK) != null) {
								try {
									supplier.setOther_tax_no(row.getCell(12).getStringCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
								try {
									supplier.setOther_tax_no(
											((Integer) ((Double) row.getCell(12).getNumericCellValue()).intValue())
													.toString().trim());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}

							}
							if (row.getCell(13, row.CREATE_NULL_AS_BLANK) != null) {
								try {
									supplier.setTan_no(row.getCell(13).getStringCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
								try {
									supplier.setTan_no(
											((Integer) ((Double) row.getCell(13).getNumericCellValue()).intValue())
													.toString().trim());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}

							}
							if (row.getCell(14, row.CREATE_NULL_AS_BLANK) != null) {
								supplier.setCurrent_address(row.getCell(14).getStringCellValue());
							}
							if (row.getCell(15, row.CREATE_NULL_AS_BLANK) != null) {
								supplier.setPermenant_address(row.getCell(15).getStringCellValue());
							}
							if (row.getCell(19, row.CREATE_NULL_AS_BLANK) != null) {
								try {
									supplier.setPincode(new Long(row.getCell(19).getStringCellValue()));
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
								try {
									supplier.setPincode((long) row.getCell(19).getNumericCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}

							}

							Integer tdsapply;
							boolean tapply = row.getCell(21).getStringCellValue().matches("Yes");

							List<Deductee> deducteeList = deducteeService.list();

							if (tapply == true) {
								tdsapply = 1;
								supplier.setTds_applicable(tdsapply);

								try {
									for (Deductee dec : deducteeList) {
										String str = "";
										float rate = 0;
										str = dec.getDeductee_title().replaceAll(" ", "");
										rate = dec.getValue();

										if (row.getCell(22, row.CREATE_NULL_AS_BLANK) != null) {
											if (str.equalsIgnoreCase(
													row.getCell(22).getStringCellValue().replaceAll(" ", ""))) {
												supplier.setDeductee_id(dec.getDeductee_id());
												supplier.setDeductee(dec);
												if (row.getCell(23, row.CREATE_NULL_AS_BLANK) != null) {

													try {
														supplier.setTds_rate(rate);
													} catch (Exception e) {
														e.printStackTrace();
													}
													try {
														supplier.setTds_rate(rate);
													} catch (Exception e) {
														e.printStackTrace();
													}
												} else {
													supplier.setTds_rate((float) 0);
												}

												// customer.setTds_rate(rate);
												fvar = 1;
												break;
											} else {
												fvar = 2;
											}
										}
									}
									if(fvar==2){Err=true;ErrorMsgs.append(" TDS Type incorrect/does not exist ");}
								} catch (Exception e) {
									e.printStackTrace();
								}
								if ((fvar == 1) && (fvarcount == 1))
									fvarcount = 1;
								else
									fvarcount = 0;
							} else {
								tdsapply = 0;
								supplier.setTds_applicable(tdsapply);
								supplier.setTds_rate((float) 0);
							}

							/*
							 * if (tapply == true) { tdsapply = 1; supplier.setTds_applicable(tdsapply); if
							 * (row.getCell(23, row.CREATE_NULL_AS_BLANK) != null) { try {
							 * supplier.setTds_rate(new Float(row.getCell(23).getStringCellValue())); }
							 * catch(Exception e) { e.printStackTrace(); } try {
							 * supplier.setTds_rate((float) row.getCell(23).getNumericCellValue()); }
							 * catch(Exception e) { e.printStackTrace(); }
							 * 
							 * } else { supplier.setTds_rate((float)0); } List<Deductee> deducteeList =
							 * deducteeService.list(); try { for (Deductee dec : deducteeList) { String str
							 * = ""; str = dec.getDeductee_title().replaceAll(" ", ""); if (row.getCell(22,
							 * row.CREATE_NULL_AS_BLANK) != null) { if (str.equalsIgnoreCase(
							 * row.getCell(22).getStringCellValue().replaceAll(" ", ""))) {
							 * supplier.setDeductee_id(dec.getDeductee_id()); supplier.setDeductee(dec);
							 * fvar = 1; break; } else { fvar = 2; } } } } catch (Exception e) {
							 * e.printStackTrace(); } if ((fvar == 1) && (fvarcount == 1)) fvarcount = 1;
							 * else fvarcount = 0; } else { supplier.setTds_applicable(0);
							 * supplier.setTds_rate((float)0); }
							 */
							supplier.setSupplier_approval(0);
							supplier.setStatus(true);
							supplier.setCreated_by(user_id);
							if (row.getCell(24, row.CREATE_NULL_AS_BLANK) != null) {
								String suball = row.getCell(24).getStringCellValue();
								if (suball != null) {
									String[] sublist = suball.split(",");
									for (String subId : sublist) {
										subId = subId.trim();
										SubLedger sr = subLedgerService.isExists(subId, supplier.getCompany_id());
										if (sr != null) {
											subLedgersList.add(sr);
											fvar = 1;
										} else
											fvar = 2;

									}
									if(fvar==2){Err=true;ErrorMsgs.append(" Subledger Name incorrect/does not exist ");}

								}
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							if (row.getCell(25, row.CREATE_NULL_AS_BLANK) != null) {
								String prodall = row.getCell(25).getStringCellValue();
								if (prodall != null) {
									String[] prodlist = prodall.split(",");
									for (String prodId : prodlist) {
										prodId = prodId.trim();
										Product pr = productService.isExists(prodId, supplier.getCompany_id());
										if (pr != null) {
											products.add(pr);
											fvar = 1;
										} else
											fvar = 2;
									}
									if(fvar==2){Err=true;ErrorMsgs.append(" Product Name incorrect/does not exist ");}
								}
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							supplier.setSubLedgers(subLedgersList);
							supplier.setProduct(products);
							if (fvarcount == 1) {
								m = 1;
								supplier.setFlag(true);
							} else if (fvarcount == 0) {
								m = m + 2;
								supplier.setFlag(false);
							}
							if (fvar != 0) {
								Suppliers sr = service.isExistsSupplier(supplier.getCompany_pan_no(),
										supplier.getCompany_name(), company_id, supplier.getContact_name());
								if (sr != null) {

									sr.setUpdated_by(user_id);
									supplier.setSupplier_id(sr.getSupplier_id());
									service.updateExcel(supplier);
									if (fvarcount == 1) {
										m = 1;
										updateList.add(supplier.getContact_name());
										updatecount = updatecount + 1;
									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(supplier.getContact_name());
										failcount = failcount + 1;
									}
								} else {

									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									supplier.setIp_address(remoteAddr);
									supplier.setCreated_by(user_id);
									service.add(supplier);
									if (fvarcount == 1) {
										m = 1;
										successList.add(supplier.getContact_name());
										successcount = successcount + 1;
									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(supplier.getContact_name());
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
				isValid = true;
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
				int rowcount = 0;
				
				while (i <= worksheet.getLastRowNum()) {
					int colcount = 0;
					XSSFRow row = worksheet.getRow(i++);
					
					for (int j = 0; j <= 25; j++) {
						if (row.getCell(j) != null) {
							
							colcount++;
						}
					}
					if (colcount >= 16) {
						rowcount++;
					}
				}

				if (isValid) {
					i = 1;
					while (i < rowcount) {
						Suppliers supplier = new Suppliers();
						XSSFRow row = worksheet.getRow(i++);
						
						Err=false;
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer fvar = 0;
							Integer fvarcount = 1;
							Set<SubLedger> subLedgersList = new HashSet<SubLedger>();
							Set<Product> products = new HashSet<Product>();
							List<CompanyStatutoryType> companyStatutoryList = companyStatutoryTypeService.findAll();
							try {
								for (CompanyStatutoryType companyStatutory : companyStatutoryList) {
									String str = "";
									str = companyStatutory.getCompany_statutory_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(row.getCell(4).getStringCellValue().replaceAll(" ", ""))) {
										supplier.setCompany_statutory_id(companyStatutory.getCompany_statutory_id());
										fvar = 1;
										break;
									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append("Company Statutory Type incorrect/does not exist ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							
							else
								fvarcount = 0;
							System.out.println("statutory" +fvar +"  " + fvarcount);
							List<IndustryType> industryList = industryTypeService.findAll();
							try {
								for (IndustryType industry : industryList) {
									String str = "";
									str = industry.getIndustry_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(row.getCell(5).getStringCellValue().replaceAll(" ", ""))) {
										supplier.setIndustry_id(industry.getIndustry_id());
										fvar = 1;
										break;

									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append("Industry Type incorrect/does not exist ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							System.out.println("Industry" +fvar +"  " + fvarcount);
							List<Country> countryList = countryService.findAll();
							try {
								for (Country country : countryList) {
									String str = "";
									str = country.getCountry_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(
											row.getCell(16).getStringCellValue().replaceAll(" ", ""))) {
										supplier.setCountry_id(country.getCountry_id());
										fvar = 1;
										break;
									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append("Country Name incorrect/does not exist ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							System.out.println("country" +fvar +"  " + fvarcount);
							List<State> stateList = stateService.findAll();
							try {
								for (State state : stateList) {
									String str = "";
									str = state.getState_name().replaceAll(" ", "");
									if (str.equalsIgnoreCase(
											row.getCell(17).getStringCellValue().replaceAll(" ", ""))) {
										if (state.getCntry().getCountry_id() == supplier.getCountry_id()) {
											supplier.setState_id(state.getState_id());
											fvar = 1;
											break;
										} else {
											fvar = 2;
										}
									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append("State Name incorrect/does not exist ");}
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
										if (city.getState().getState_id() == supplier.getState_id()) {
											supplier.setCity_id(city.getCity_id());
											fvar = 1;
											break;
										} else {
											fvar = 2;
										}
									} else {
										fvar = 2;
									}
								}
								if(fvar==2){Err=true;ErrorMsgs.append("City Name incorrect/does not exist ");}
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
												row.getCell(26).getStringCellValue().replaceAll(" ", ""))) {
											supplier.setCompany(comp);
											supplier.setCompany_id(comp.getCompany_id());
											fvar = 1;
											break;
										} else {
											fvar = 2;
										}

									}
									if(fvar==2){Err=true;ErrorMsgs.append("Company Name incorrect/does not exist ");}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								supplier.setCompany(user.getCompany());
								supplier.setCompany_id(company_id);
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							supplier.setContact_name(row.getCell(0).getStringCellValue());

							try {
								supplier.setMobile(row.getCell(1).getStringCellValue());
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								Double doubleValue = row.getCell(1).getNumericCellValue();
								BigDecimal bd = new BigDecimal(doubleValue.toString());
							
								long lonVal = bd.longValue();
								
								String Mobile = Long.toString(lonVal).trim();
								//supplier.setMobile((((Double) row.getCell(1).getNumericCellValue())).toString().trim());
								supplier.setMobile(Mobile);
							} catch (Exception e) {
								e.printStackTrace();
							}
							supplier.setEmail_id(row.getCell(2).getStringCellValue());
							supplier.setCompany_name(row.getCell(3).getStringCellValue());
							
							String company_name = supplier.getCompany_name().replace("\"", "").replace("\'", "");
							supplier.setCompany_name(company_name);
							if (row.getCell(6, row.CREATE_NULL_AS_BLANK) != null) {

								try {
									supplier.setLandline_no(row.getCell(6).getStringCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
								try {
									supplier.setLandline_no(
											(((Double) row.getCell(6).getNumericCellValue())).toString().trim());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}

							}
							//System.out.println(row.getCell(6).getNumericCellValue());

							if (row.getCell(7, row.CREATE_NULL_AS_BLANK) != null) {
								try {
									supplier.setOwner_pan_no(row.getCell(7).getStringCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
									supplier.setOwner_pan_no("0");
								}

							}
							if (row.getCell(8, row.CREATE_NULL_AS_BLANK) != null) {
								try {
									supplier.setAdhaar_no(row.getCell(8).getStringCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
								try {
									supplier.setAdhaar_no(
											(((Double) row.getCell(8).getNumericCellValue())).toString().trim());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
							}
							//System.out.println(row.getCell(8).getNumericCellValue());

							boolean gstapply = row.getCell(9).getStringCellValue().matches("Yes");
							if (gstapply == true) {
								supplier.setGst_applicable(gstapply);
								if (row.getCell(10, row.CREATE_NULL_AS_BLANK) != null) {
									try {
										supplier.setGst_no(row.getCell(10).getStringCellValue());
									} catch (Exception e) {
										/* e.printStackTrace(); */
									}
									try {
										supplier.setGst_no(
												((Integer) ((Double) row.getCell(10).getNumericCellValue()).intValue())
														.toString().trim());
									} catch (Exception e) {
										/* e.printStackTrace(); */
									}

								}
								supplier.setReverse_mecha(row.getCell(20).getStringCellValue());
							} else {
								supplier.setGst_applicable(gstapply);
								supplier.setReverse_mecha("No");
							}

							try {
								supplier.setCompany_pan_no(row.getCell(11).getStringCellValue());
							} catch (Exception e) {
								supplier.setCompany_pan_no("0");
								/* e.printStackTrace(); */
							}

							if (row.getCell(12, row.CREATE_NULL_AS_BLANK) != null) {
								try {
									supplier.setOther_tax_no(row.getCell(12).getStringCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
								try {
									supplier.setOther_tax_no(
											((Integer) ((Double) row.getCell(12).getNumericCellValue()).intValue())
													.toString().trim());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}

							}
							if (row.getCell(13, row.CREATE_NULL_AS_BLANK) != null) {
								try {
									supplier.setTan_no(row.getCell(13).getStringCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
								try {
									supplier.setTan_no(
											((Integer) ((Double) row.getCell(13).getNumericCellValue()).intValue())
													.toString().trim());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}

							}
							if (row.getCell(14, row.CREATE_NULL_AS_BLANK) != null) {
								supplier.setCurrent_address(row.getCell(14).getStringCellValue());
							}
							if (row.getCell(15, row.CREATE_NULL_AS_BLANK) != null) {
								supplier.setPermenant_address(row.getCell(15).getStringCellValue());
							}
							if (row.getCell(19, row.CREATE_NULL_AS_BLANK) != null) {
								try {
									supplier.setPincode(new Long(row.getCell(19).getStringCellValue()));
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}
								try {
									supplier.setPincode((long) row.getCell(19).getNumericCellValue());
								} catch (Exception e) {
									/* e.printStackTrace(); */
								}

							}

							Integer tdsapply;
							boolean tapply = row.getCell(21).getStringCellValue().matches("Yes");
							
							
							List<Deductee> deducteeList = deducteeService.list();

							if (tapply == true) {
								tdsapply = 1;
								supplier.setTds_applicable(tdsapply);

								try {
									for (Deductee dec : deducteeList) {
										String str = "";
										float rate = 0;
										str = dec.getDeductee_title().replaceAll(" ", "");
										rate = dec.getValue();

										if (row.getCell(22, row.CREATE_NULL_AS_BLANK) != null) {
											if (str.equalsIgnoreCase(
													row.getCell(22).getStringCellValue().replaceAll(" ", ""))) {
												supplier.setDeductee_id(dec.getDeductee_id());
												supplier.setDeductee(dec);
												if (row.getCell(23, row.CREATE_NULL_AS_BLANK) != null) {

													try {
														supplier.setTds_rate(rate);
													} catch (Exception e) {
														e.printStackTrace();
													}
													try {
														supplier.setTds_rate(rate);
													} catch (Exception e) {
														e.printStackTrace();
													}
												} else {
													supplier.setTds_rate((float) 0);
												}

												// customer.setTds_rate(rate);
												fvar = 1;
												break;
											} else {
												fvar = 2;
											}
										}
									}
									if(fvar==2){Err=true;ErrorMsgs.append("TDS Rate incorrect/does not exist ");}
								} catch (Exception e) {
									e.printStackTrace();
								}
								if ((fvar == 1) && (fvarcount == 1))
									fvarcount = 1;
								else
									fvarcount = 0;
							} else {
								tdsapply = 0;
								supplier.setTds_applicable(tdsapply);
								supplier.setTds_rate((float) 0);
							}
							
							
							
							
							
							
							/*
							 * if (tapply == true) { tdsapply = 1; supplier.setTds_applicable(tdsapply); if
							 * (row.getCell(23, row.CREATE_NULL_AS_BLANK) != null) { try {
							 * supplier.setTds_rate(new Float(row.getCell(23).getStringCellValue())); }
							 * catch (Exception e) { e.printStackTrace(); } try {
							 * supplier.setTds_rate((float) row.getCell(23).getNumericCellValue()); } catch
							 * (Exception e) { e.printStackTrace(); }
							 * 
							 * } else { supplier.setTds_rate((float) 0); } List<Deductee> deducteeList =
							 * deducteeService.list(); try { for (Deductee dec : deducteeList) { String str
							 * = ""; str = dec.getDeductee_title().replaceAll(" ", ""); if (row.getCell(22,
							 * row.CREATE_NULL_AS_BLANK) != null) { if (str.equalsIgnoreCase(
							 * row.getCell(22).getStringCellValue().replaceAll(" ", ""))) {
							 * supplier.setDeductee_id(dec.getDeductee_id()); supplier.setDeductee(dec);
							 * fvar = 1; break; } else { fvar = 2; } } } } catch (Exception e) {
							 * e.printStackTrace(); } if ((fvar == 1) && (fvarcount == 1)) fvarcount = 1;
							 * else fvarcount = 0; } else { supplier.setTds_applicable(0);
							 * supplier.setTds_rate((float) 0); }
							 */ 
							
							supplier.setSupplier_approval(0);
							supplier.setStatus(true);
							supplier.setCreated_by(user_id);
							if (row.getCell(24, row.CREATE_NULL_AS_BLANK) != null) {
								String suball = row.getCell(24).getStringCellValue();
								if (suball != null) {
									String[] sublist = suball.split(",");
									for (String subId : sublist) {
										subId = subId.trim();
										SubLedger sr = subLedgerService.isExists(subId, supplier.getCompany_id());
										if (sr != null) {
											subLedgersList.add(sr);
											fvar = 1;
										} else
											fvar = 2;

									}
									if(fvar==2){Err=true;ErrorMsgs.append("Subledger Name incorrect/does not exist ");}
								}
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							if (row.getCell(25, row.CREATE_NULL_AS_BLANK) != null) {
								String prodall = row.getCell(25).getStringCellValue();
								if (prodall != null) {
									String[] prodlist = prodall.split(",");
									for (String prodId : prodlist) {
										prodId = prodId.trim();
										
										Product pr = productService.isExists(prodId, supplier.getCompany_id());
										if (pr != null) {
											products.add(pr);
											fvar = 1;
										} else
											fvar = 2;
									}
									if(fvar==2){Err=true;ErrorMsgs.append("Product Name incorrect/does not exist ");}
								}
							}
							
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							supplier.setSubLedgers(subLedgersList);
							supplier.setProduct(products);
							if (fvarcount == 1) {
								m = 1;
								supplier.setFlag(true);
							} else if (fvarcount == 0) {
								m = m + 2;
								supplier.setFlag(false);
							}
							if (fvar != 0) {
								Suppliers sr = service.isExistsSupplier(supplier.getCompany_pan_no(),
										supplier.getCompany_name(), company_id, supplier.getContact_name());
								if (sr != null) {

									sr.setUpdated_by(user_id);
									supplier.setSupplier_id(sr.getSupplier_id());
									service.updateExcel(supplier);
									if (fvarcount == 1) {
										m = 1;
										updateList.add(supplier.getContact_name());
										updatecount = updatecount + 1;
									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(supplier.getContact_name());
										failcount = failcount + 1;
									}
								} else {

									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									supplier.setIp_address(remoteAddr);
									supplier.setCreated_by(user_id);
									service.add(supplier);
									if (fvarcount == 1) {
										m = 1;
										successList.add(supplier.getContact_name());
										successcount = successcount + 1;
									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(supplier.getContact_name());
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
        }
		model.setViewName("redirect:/suppliersList");
		return model;
	}

	@RequestMapping(value = "deleteSuppliers", method = RequestMethod.GET)
	public ModelAndView deleteSuppliers(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		String msg = "You can't delete Supplier";
		/* msg = service.deleteByIdValue(id); */
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg", msg);

		if (importFlag == true) {

			return new ModelAndView("redirect:/suppliersList");
		} else {

			return new ModelAndView("redirect:/supplierimportfailure");
		}
	}

	@RequestMapping(value = "deleteImportFailureSuppliers", method = RequestMethod.GET)
	public ModelAndView deleteImportFailureSuppliers(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		//String msg = "You can't delete Supplier";
		String msg="";
		msg = service.deleteByIdValue(id); 
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg", msg);

		if (importFlag == true) {

			return new ModelAndView("redirect:/FailureSuppliersList");
		} else {

			return new ModelAndView("redirect:/supplierimportfailure");
		}
	}

	
	@RequestMapping(value = "Batchsupplier", method = RequestMethod.POST)
	public ModelAndView subledgerBatchApproval(@ModelAttribute("supplierbatch") Suppliers supplierbatch,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		Boolean flag = true;
		Boolean primaryApproval = null;
		Boolean rejectApproval = null;

		if (supplierbatch.getPrimaryApproval() != null) {
			primaryApproval = supplierbatch.getPrimaryApproval();
		}
		if (supplierbatch.getRejectApproval() != null) {
			rejectApproval = supplierbatch.getRejectApproval();
		}

		if (rejectApproval != null) {
			flag = service.rejectByBatch(supplierbatch.getSupplierList());
			for (String supId : supplierbatch.getSupplierList()) {
				yearService.saveActivityLogForm(user.getUser_id(), null, Long.parseLong(supId), null, null, null, null,
						null, null, null, (long) APPROVAL_STATUS_REJECT);
			}
			if (flag) {
				session.setAttribute("successMsg", "Supplier Rejected Successfully");
			} else {
				session.setAttribute("errorMsg", "Please select atleast one Supplier.");
			}

			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && rejectApproval) {
				model.setViewName("redirect:/supplierPrimaryValidation");

			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !rejectApproval) {
				model.setViewName("redirect:/supplierSecondaryValidation");

			}
		}

		if (primaryApproval != null) {
			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && primaryApproval) {
				flag = service.approvedByBatch(supplierbatch.getSupplierList(), primaryApproval);
				for (String supId : supplierbatch.getSupplierList()) {
					yearService.saveActivityLogForm(user.getUser_id(), null, Long.parseLong(supId), null, null, null,
							null, null, (long) APPROVAL_STATUS_PRIMARY, null, null);
				}
				if (flag) {
					session.setAttribute("successMsg", "Supplier Primary Approval Done Successfully");
				} else {
					session.setAttribute("errorMsg", "Please select atleast one Supplier.");
				}
				model.setViewName("redirect:/supplierPrimaryValidation");

			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !primaryApproval) {
				flag = service.approvedByBatch(supplierbatch.getSupplierList(), primaryApproval);
				for (String supId : supplierbatch.getSupplierList()) {
					yearService.saveActivityLogForm(user.getUser_id(), null, Long.parseLong(supId), null, null, null,
							null, null, null, (long) APPROVAL_STATUS_SECONDARY, null);
				}
				if (flag == true) {
					session.setAttribute("successMsg", "Supplier Secondary Approval Done Successfully");
				} else {
					session.setAttribute("errorMsg", "Please select atleast one Supplier.");
				}
				model.setViewName("redirect:/supplierSecondaryValidation");
			}
		}
		return model;
	}

}