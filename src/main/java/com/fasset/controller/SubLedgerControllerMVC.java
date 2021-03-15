/**
 * mayur suramwar
 */
package com.fasset.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
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
import com.fasset.controller.validators.SubLedgerValidator;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountGroupType;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.entities.IndustryType;
import com.fasset.entities.Ledger;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.BankBalanceForm;
import com.fasset.form.CustomerBalanceForm;
import com.fasset.form.SubledgerBalanceForm;
import com.fasset.form.SubledgerExportForm;
import com.fasset.form.SupplierBalanceForm;
import com.fasset.report.controller.ClassToConvertDateForExcell;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IMailService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.fasset.service.interfaces.IYearEndingService;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class SubLedgerControllerMVC extends MyAbstractController {
	
	@Autowired
	private IOpeningBalancesService openingBalances;
	 
	@Autowired
	private ILedgerService ledgerService;

	@Autowired
	private SubLedgerValidator subLedgerValidator;

	@Autowired
	private ISubLedgerService subLedgerService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IQuotationService quoteservice;

	@Autowired
	private IClientSubscriptionMasterService subscribeservice;

	@Autowired
	private IBankService bankService;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private ISuplliersService supplierService;

	@Autowired
	private IAccountingYearService accountingYearService ;
	
	
	@Autowired	
	private IYearEndingService yearService;
	
	Boolean importFlag = null;  // for maintaining the user on subledgerList.jsp after delete and view
	Boolean isImport = false;   // is subledger saved or updated through excel or through System(Add subledger)
	private List<Ledger> ledgerList = new ArrayList<Ledger>();
	private List<SubLedger> subledgerList;
	private List<Bank> bankList;
	private List<Suppliers> supplierList;
	private List<Customer> customerList;
	private Set<SubLedger> subLedgerListIndustry = new HashSet<SubLedger>();
	Company company = new Company();
	
	 private List<String> successList = new ArrayList<String>();
	 private List<String> failureList = new ArrayList<String>();
	 private List<String> updateList = new ArrayList<String>();
	 private  String successmsg;
	 private  String failmsg;
	 private  String updatemsg;
	 private  String error;
	 
	@RequestMapping(value = "subledger", method = RequestMethod.GET)
	public ModelAndView subLedger(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		SubLedger subledger = new SubLedger();
		User user = (User) session.getAttribute("user");
		model.addObject("subledger", subledger);

		if (role == ROLE_SUPERUSER || role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			List<Company> companyList = new ArrayList<Company>();
			if(role == ROLE_SUPERUSER) {
				companyList = companyService.getApprovedCompanies();
			}
			if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				companyList = companyService.getApprovedCompanies(user.getUser_id());
			}
			model.addObject("companyList", companyList);
			model.setViewName("/master/subledgerForKpo");
		} else {
			ledgerList = ledgerService.findAllLedgersOfCompany(company_id);
			model.addObject("ledgerList", ledgerList);
			model.setViewName("/master/subledger");
		}
		return model;
	}

	@RequestMapping(value = "savesubledger", method = RequestMethod.POST)
	public ModelAndView saveSubLedger(@ModelAttribute("subledger") SubLedger subledger, 
			BindingResult result,
			HttpServletRequest request,
			HttpServletResponse response) {
System.out.println("saving subledger");
		User user = new User();
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");

		subLedgerValidator.validate(subledger, result);
		if (result.hasErrors()) {
			if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
				List<Company> companyList = new ArrayList<Company>();
				if(role == ROLE_SUPERUSER) {
					companyList = companyService.getApprovedCompanies();
				}
				if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
					companyList = companyService.getApprovedCompanies(user.getUser_id());
				}
				model.addObject("companyList", companyList);
				model.setViewName("/master/subledgerForKpo");
			} else {
				model.addObject("ledgerList", ledgerList);
				model.setViewName("/master/subledger");
			}
		} else {
			String msg = "";
			try {
				if (subledger.getSubledger_Id() != null) {
					long id = subledger.getSubledger_Id();
					if (id > 0) {
						subledger.setUpdated_by(user);
						SubLedger oldsubledger=subLedgerService.findOneWithAll(subledger.getSubledger_Id());
						if((oldsubledger.getSubledger_approval() == 1) || (oldsubledger.getSubledger_approval() == 3))
						{
							if (role == ROLE_EXECUTIVE)
								subledger.setSubledger_approval(2);
							else if((role == ROLE_CLIENT) || (role == ROLE_EMPLOYEE) || (role == ROLE_TRIAL_USER))
								subledger.setSubledger_approval(0);
							else
								subledger.setSubledger_approval(oldsubledger.getSubledger_approval());
						}
						else
						{
							subledger.setSubledger_approval(oldsubledger.getSubledger_approval());
						}	
						subLedgerService.update(subledger);
						msg = "Sub Ledger updated successfully";
					}
				}

				else {
					subledger.setCreated_by(user);
					subledger.setFlag(true);
					subledger.setSubledger_approval(APPROVAL_STATUS_PENDING);
					if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
						subledger.setCompany(companyService.getById(subledger.getCompany_id()));
					}
					else {
						subledger.setCompany_id(company_id);
						subledger.setCompany(companyService.getById(company_id));
					}
					msg = subLedgerService.saveSubLedger(subledger);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/subledgerList");
		}
		return model;
	}

	@RequestMapping(value = "subledgerList", method = RequestMethod.GET)
	public ModelAndView subLedgerList(@RequestParam(value = "msg", required = false) String msg,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		System.out.println("The SubLEDGER");
		boolean importfail = false;
		boolean importflag = true;
		User user = new User();
		user = (User) session.getAttribute("user");
		long company_id = (long) session.getAttribute("company_id");
		List<SubLedger> subledgerList = new ArrayList<>();
		List<SubLedger> subledgerListfail = new ArrayList<>();
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
		
		if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
			
			if(role == ROLE_SUPERUSER) {
				subledgerList = subLedgerService.findAllSubLedgersListing((long) 1);
				subledgerListfail = subLedgerService.findAllSubLedgersListing((long) 0);
			}
			if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				subledgerList = subLedgerService.findAllSubLedgersListing(true, user.getUser_id());
				subledgerListfail = subLedgerService.findAllSubLedgersListing(false, user.getUser_id());
			}
			
			if (subledgerListfail.size() != 0)
				importfail = true;
			
		} else {
			
			subledgerList = subLedgerService.findAllSubLedgersListingOfCompany1(company_id, (long) 1);
			subledgerListfail = subLedgerService.findAllSubLedgersListingOfCompany1(company_id, (long) 0);
			Company company = companyService.getCompanyWithIndustrytype(company_id);
			IndustryType type = company.getIndustry_type();
			Set<SubLedger> subLedgerListIndustry = type.getSubLedgers();
			if (subledgerListfail.size() != 0)
				importfail = true;
			Long quote_id = subscribeservice.getquoteofcompany(company_id);
			String email = user.getCompany().getEmail_id();
			if (quote_id != 0) {
				importflag = quoteservice.findAllimportDetailsUser(email, quote_id); // for subscribed client if he registered his company with quotation and quotation contains master imports facility.
			} else {
				importflag = quoteservice.findAllimportDetailsUser(email, quote_id);
			}
		/*	if(user.getCompany().getCreated_by()!=null)// for company registered without quotation flow. This will be use for master imports as quotation is not present for this company.
			{
				importflag=true;
			}*/
			
			model.addObject("subLedgerListIndustry", subLedgerListIndustry);

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
		model.addObject("importflag", importflag);
		model.addObject("importfail", importfail);
		model.addObject("subledgerList", subledgerList);
		model.setViewName("/master/subledgerList");
		return model;
	}
	@RequestMapping(value = "downloadsubledger" , method =  RequestMethod.POST ,  produces =MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String downloadsubledger(HttpServletRequest request,HttpServletResponse response)throws IOException {
		System.out.println("downloadCustomer");
		
		
		
		HttpSession session = request.getSession(true);
		List<SubLedger> subledgerList = new ArrayList<>();
		long company_id = (long) session.getAttribute("company_id");
		subledgerList=subLedgerService.findAllSubLedgersOnlyOfCompany(company_id);
				
		JSONArray jsonArray = new JSONArray();
		for(SubLedger entry : subledgerList) {
			JSONObject jsonObj = new JSONObject();
		//	jsonObj.put("FirstName", entry.getf);
			jsonObj.put("subledgername", entry.getSubledger_name());
			jsonArray.put(jsonObj);
		}
		

		 return jsonArray.toString();}
	@RequestMapping(value = "subledgerimportfailure", method = RequestMethod.GET)
	public ModelAndView subledgerimportfailure(@RequestParam(value = "msg", required = false) String msg,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		User user = (User) session.getAttribute("user");
		List<SubLedger> subledgerList = new ArrayList<>();
		importFlag = false;
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if(role == ROLE_SUPERUSER) {
			subledgerList = subLedgerService.findAllSubLedgersListing((long) 0);
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			subledgerList = subLedgerService.findAllSubLedgersListing(false, user.getUser_id());
		}
		else {
			subledgerList = subLedgerService.findAllSubLedgersListingOfCompany1(company_id, (long) 0);
		}
		model.addObject("subledgerList", subledgerList);
		model.setViewName("/master/failuresubledgerList");
		return model;
	}

	@RequestMapping(value = "viewSubLedger", method = RequestMethod.GET)
	public ModelAndView viewSubLedger(@RequestParam("id") long id, @RequestParam("flag") long flag) {
		ModelAndView model = new ModelAndView();
		SubLedger subledger = new SubLedger();
		Ledger ledger = new Ledger();
		AccountSubGroup subgroup = new AccountSubGroup();
		AccountGroup group = new AccountGroup();
		AccountGroupType type = new AccountGroupType();
		try {
			subledger = subLedgerService.findOneWithAll(id);
			//subledger.getOpeningbalances().setDebit_balance(new java.text.DecimalFormat("0").format(subledger.getOpeningbalances().getDebit_balance()));
			ledger = subledger.getLedger();
			subgroup = ledger.getAccsubgroup();
			group = subgroup.getAccountGroup();
			type = group.getGrouptype();
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("type", type);
		model.addObject("subgroup", subgroup);
		model.addObject("group", group);
		model.addObject("ledger", ledger);
		model.addObject("subledger", subledger);
		model.addObject("flag", flag);
		model.addObject("importFlag", importFlag);
		model.setViewName("/master/subLedgerView");
		return model;
	}

	@RequestMapping(value = "editSubLedger", method = RequestMethod.GET)
	public ModelAndView editSubLedger(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		User user = (User) session.getAttribute("user");
		SubLedger subledger = new SubLedger();

		try {
			if (id > 0) {
				subledger = subLedgerService.findOneWithAll(id);
				subledger.setSetDefault(subledger.getSetDefault());
				if (subledger.getCredit_opening_balance() != null) {
					subledger.setCredit_opening_balance1(subledger.getCredit_opening_balance().toString());
				}

				if (subledger.getDebit_opening_balance() != null) {
					subledger.setDebit_opening_balance1(subledger.getDebit_opening_balance().toString());
				}

				if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
					subledger.setCompany_id(subledger.getCompany().getCompany_id());
					if (subledger.getLedger() != null)
						subledger.setLedger_id(subledger.getLedger().getLedger_id());
					List<Company> companyList = new ArrayList<Company>();
					if(role == ROLE_SUPERUSER) {
						companyList = companyService.getApprovedCompanies();
					}
					if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
						companyList = companyService.getApprovedCompanies(user.getUser_id());
					}
					model.addObject("companyList", companyList);
					model.addObject("subledger", subledger);
					model.setViewName("/master/subledgerForKpo");
				} else {
					if (subledger.getLedger() != null)
						subledger.setLedger_id(subledger.getLedger().getLedger_id());
					ledgerList = ledgerService.findAllLedgersOfCompany(company_id);
					model.addObject("ledgerList", ledgerList);
					model.addObject("subledger", subledger);
					model.setViewName("/master/subledger");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	@RequestMapping(value = "deletesubledger", method = RequestMethod.GET)
	public ModelAndView deleteSubledger(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String msg = "You can't delete subledger";
		/*msg = subLedgerService.deleteByIdValue(id);*/
		session.setAttribute("successMsg", msg);
		if(importFlag == true)
		{	
			return new ModelAndView("redirect:/subledgerList");
		}
		else
		{
			return new ModelAndView("redirect:/subledgerimportfailure");
		}
	}

	
	@RequestMapping(value = "deletefailuresubledger", method = RequestMethod.GET)
	public ModelAndView deletefailureSubledger(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		//String msg = "You can't delete subledger";
		String msg="";
		msg = subLedgerService.deleteByIdValue(id);
		session.setAttribute("successMsg", msg);
		if(importFlag == true)
		{	
			return new ModelAndView("redirect:/subledgerList");
		}
		else
		{
			return new ModelAndView("redirect:/subledgerimportfailure");
		}
	}

	
	
	@RequestMapping(value = "subLedgerPrimaryValidationList", method = RequestMethod.GET)
	public ModelAndView subLedgerPrimaryValidationList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		SubLedger batchsubledger = new SubLedger();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		List<SubLedger> subledgerList = new ArrayList<SubLedger>();
		
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		
		if(role == ROLE_SUPERUSER) {
			subledgerList = subLedgerService.findByStatus(APPROVAL_STATUS_PENDING);			
		}
		if(role == ROLE_EXECUTIVE ) {
			subledgerList = subLedgerService.findByStatus(role,APPROVAL_STATUS_PENDING, user.getUser_id());
		}
		model.addObject("subledgerList", subledgerList);
		model.addObject("batchsubledger", batchsubledger);
		model.setViewName("/master/subLedgerPrimaryValidationList");
		return model;
	}

	@RequestMapping(value = "subLedgerSecondaryValidationList", method = RequestMethod.GET)
	public ModelAndView subLedgerSecondaryValidationList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		SubLedger batchsubledger = new SubLedger();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		List<SubLedger> subledgerList = new ArrayList<SubLedger>();
		
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		
		if(role == ROLE_SUPERUSER) {
			subledgerList = subLedgerService.findByStatus(APPROVAL_STATUS_PRIMARY);			
		}
		if(role == ROLE_MANAGER) {
			subledgerList = subLedgerService.findByStatus(role,APPROVAL_STATUS_PRIMARY, user.getUser_id());
		}
		model.addObject("subledgerList", subledgerList);
		model.addObject("batchsubledger", batchsubledger);
		model.setViewName("/master/subLedgerSecondaryValidationList");
		return model;
	}

	@RequestMapping(value = "approveSubLedger", method = RequestMethod.GET)
	public ModelAndView approveSubLedger(@RequestParam("id") long id,
			@RequestParam("primaryApproval") Boolean primaryApproval, 
			HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		Long subId = id;
		String msg = "";
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		SubLedger sub =null;
		try {
			sub = subLedgerService.getById(id);
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long role = (long) session.getAttribute("role");
		if(sub!=null && sub.getLedger().getLedger_approval().equals(APPROVAL_STATUS_SECONDARY))
		{
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && primaryApproval)) {
			msg = subLedgerService.updateByApproval(subId, APPROVAL_STATUS_PRIMARY);
			yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, subId, null, null, (long)APPROVAL_STATUS_PRIMARY, null,null );
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/subLedgerPrimaryValidationList");
		} else {
			msg = subLedgerService.updateByApproval(subId, APPROVAL_STATUS_SECONDARY);
			yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, subId, null, null, null, (long)APPROVAL_STATUS_SECONDARY,null );
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/subLedgerSecondaryValidationList");
		}
		}
		else
		{
			if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && primaryApproval)) {
				msg="Ledger is not approved for this subledger. Please approve the ledger";
				session.setAttribute("successMsg", msg);
				model.setViewName("redirect:/subLedgerPrimaryValidationList");
			}
			else
			{
				msg="Ledger is not approved for this subledger. Please approve the ledger";
				session.setAttribute("successMsg", msg);
				model.setViewName("redirect:/subLedgerSecondaryValidationList");
			}
		}
		return model;
	}

	@RequestMapping(value = "rejectSubLedger", method = RequestMethod.GET)
	public ModelAndView rejectSubLedger(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("rejectApproval") Boolean rejectApproval) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");
		Long subId = id;
		String msg = "";
		msg = subLedgerService.updateByApproval(subId, APPROVAL_STATUS_REJECT);
		yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, subId, null, null, null, null, (long)APPROVAL_STATUS_REJECT);
		session.setAttribute("successMsg", msg);
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && rejectApproval == true)) {
			model.setViewName("redirect:/subLedgerPrimaryValidationList");
		} else {
			model.setViewName("redirect:/subLedgerSecondaryValidationList");
		}
		return model;
	}

	@RequestMapping(value = { "importExcelSubLedger" }, method = { RequestMethod.POST })
	public ModelAndView importExcelLedger(@RequestParam("excelFile") MultipartFile excelfile,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean isValid = true;
		ModelAndView model = new ModelAndView();
		String success = "Record Imported successfully";
		int successcount = 0;
		String fail = "in failure list, Please refer failure list";
		int failcount = 0;
		String update = "Updated List";
		StringBuffer ErrorMsgs=new StringBuffer();
		boolean Err=false;
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
		long role = (long) session.getAttribute("role");

		User user = new User();
		user = (User) session.getAttribute("user");
		try {
			if (excelfile.getOriginalFilename().endsWith("xls")) {
				System.out.println("file is xls...");
				int i = 0;
				// Creates a workbook object from the uploaded excelfile
				HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
				// Creates a worksheet object representing the first sheet
				HSSFSheet worksheet = workbook.getSheetAt(0);
				// Reads the data in excel file until last row is encountered

				while (i <= worksheet.getLastRowNum()) {
					HSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 1; j++) {
						if (row.getCell(j).getCellType() == Cell.CELL_TYPE_BLANK) {
							isValid = false;
							break;
						}
					}

				}

				if (isValid) {
					i = 1;
					while (i <= worksheet.getLastRowNum()) {
						//boolean flag = false;
						SubLedger subledger = new SubLedger();

						// Creates an object for the UserInfo Model
						// Creates an object representing a single row in excel
						HSSFRow row = worksheet.getRow(i++);
						Err=false;
						
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer fvar = 0;
							// for(int j = 0; j<=3; j++){
							List<Ledger> ledgerList = ledgerService.findAllLedgersOfCompany(company_id);
							try {
								for (Ledger ledger : ledgerList) {
									String str = "";
									str = ledger.getLedger_name().replaceAll(" ", "");
									if (str.trim().equalsIgnoreCase(row.getCell(1).getStringCellValue().replaceAll(" ", "").trim())) {
										subledger.setLedger_id(ledger.getLedger_id());
										subledger.setLedger(ledger);
										subledger.setFlag(true);
										fvar = 1;
										break;
									} else {
										subledger.setFlag(false);
										fvar = 2;
									}

									// System.out.println("test");
									// System.out.println(acsubgroup.getSubgroup_Id());
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" Ledger Name incorrect/does not exist ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
								List<Company> companylist = companyService.findAll();
								try {
									for (Company comp : companylist) {
										String str = "";
										str = comp.getCompany_name().replaceAll(" ", "");
										if (str.trim().equalsIgnoreCase( row.getCell(2).getStringCellValue().replaceAll(" ", "").trim())) {
											subledger.setCompany(comp);
											subledger.setCompany_id(comp.getCompany_id());
											fvar = 1;
											subledger.setFlag(true);
											break;
										} else {
											fvar = 2;
											subledger.setFlag(false);

										}
									}
									if(fvar==2){Err=true;ErrorMsgs.append(" Company Name incorrect/does not exist ");}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								subledger.setCompany(user.getCompany());
								subledger.setCompany_id(company_id);
							}
							subledger.setSubledger_name(row.getCell(0).getStringCellValue());
							String subname = subledger.getSubledger_name().replace("\"", "").replace("\'", "");
							subledger.setSubledger_name(subname);
							
							subledger.setSubledger_approval(0);
							subledger.setCreated_by(user);
							subledger.setStatus(true);
							// }
							/*
							 * if(fvar==1) m=1; else if(fvar==2) m=m+2;
							 */
							if (fvar != 0) {
								int exist = subLedgerService.isExistssubledger(subledger.getSubledger_name(), subledger.getCompany_id());
								if (exist == 1) {
									SubLedger sl = subLedgerService.isExists(subledger.getSubledger_name(), subledger.getCompany_id());
									subledger.setUpdated_by(user);
									subledger.setSubledger_Id(sl.getSubledger_Id());
									if(sl.getAllocated()!=null)
									{
									subledger.setAllocated(sl.getAllocated());
									}
									subLedgerService.updateExcelSubLedger(subledger);
									if (fvar == 1) {
										m = 1;
										if(sl.getAllocated()==null)
										{
										updateList.add(subledger.getSubledger_name());
										updatecount = updatecount + 1;
										}
										
									} else if (fvar == 2) {
										m = m + 2;
										failureList.add(subledger.getSubledger_name());
										failcount = failcount + 1;
									}
								} else {
									
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									subledger.setIp_address(remoteAddr);
									subledger.setCreated_by(user);
									subLedgerService.saveExcelSubLedger(subledger);
									if (fvar == 1) {
										m = 1;
										successList.add(subledger.getSubledger_name());
										successcount = successcount + 1;

									} else if (fvar == 2) {
										m = m + 2;
										failureList.add(subledger.getSubledger_name());
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
				// Creates a workbook object from the uploaded excelfile
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				// Creates a worksheet object representing the first sheet
				XSSFSheet worksheet = workbook.getSheetAt(0);
				// Reads the data in excel file until last row is encountered

				while (i <= worksheet.getLastRowNum()) {
					XSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 1; j++) {
						if (row.getCell(j).getCellType() == Cell.CELL_TYPE_BLANK) {
							isValid = false;
							break;
						}
					}

				}

				if (isValid) {
					i = 1;
					while (i <= worksheet.getLastRowNum()) {
						//boolean flag = false;
						SubLedger subledger = new SubLedger();
						// Creates an object for the UserInfo Model
						// Creates an object representing a single row in excel
						XSSFRow row = worksheet.getRow(i++);
						Err=false;
						
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer fvar = 0;
							// for(int j = 0; j<=3; j++){
							List<Ledger> ledgerList = ledgerService.findAllLedgersOfCompany(company_id);
							try {
								for (Ledger ledger : ledgerList) {
									String str = "";
									str = ledger.getLedger_name().replaceAll(" ", "");
									System.out.println("Led " +str);
									if (str.trim().equalsIgnoreCase(row.getCell(1).getStringCellValue().replaceAll(" ", "").trim())) {
										subledger.setLedger_id(ledger.getLedger_id());
										subledger.setLedger(ledger);
										subledger.setFlag(true);
										fvar = 1;
										break;
									} else {
										subledger.setFlag(false);
										fvar = 2;
									}

									 
								}
								if(fvar==2){Err=true;ErrorMsgs.append(" Ledger Name incorrect/does not exist ");}
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
								List<Company> companylist = companyService.findAll();
								System.out.println("Rold");
								try {
									for (Company comp : companylist) {
										String str = "";
										str = comp.getCompany_name().replaceAll(" ", "");
										if (str.trim().equalsIgnoreCase( row.getCell(2).getStringCellValue().replaceAll(" ", "").trim())) {
											subledger.setCompany(comp);
											subledger.setCompany_id(comp.getCompany_id());
											fvar = 1;
											subledger.setFlag(true);
											break;
										} else {
											fvar = 2;
											subledger.setFlag(false);

										}
									}
                if(fvar==2){Err=true;ErrorMsgs.append(" Company name incorrect");}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								subledger.setCompany(user.getCompany());
								subledger.setCompany_id(company_id);
							}
							subledger.setSubledger_name(row.getCell(0).getStringCellValue());
							String subname = subledger.getSubledger_name().replace("\"", "").replace("\'", "");
							subledger.setSubledger_name(subname);
							subledger.setSubledger_approval(0);
							subledger.setCreated_by(user);
							subledger.setStatus(true);
							// }
							/*
							 * if(fvar==1) m=1; else if(fvar==2) m=m+2;
							 */
							System.out.println("test1");
							 System.out.println(fvar);
							if (fvar != 0) {
								int exist = subLedgerService.isExistssubledger(subledger.getSubledger_name(), subledger.getCompany_id());
								if (exist == 1) {
									
									SubLedger sl = subLedgerService.isExists(subledger.getSubledger_name(), subledger.getCompany_id());
									subledger.setUpdated_by(user);
									subledger.setSubledger_Id(sl.getSubledger_Id());
									if(sl.getAllocated()!=null)
									{
									subledger.setAllocated(sl.getAllocated());
									}
									subLedgerService.updateExcelSubLedger(subledger);
								
									if (fvar == 1) {
										m = 1;
										if(sl.getAllocated()==null)
										{
										updateList.add(subledger.getSubledger_name());
										updatecount = updatecount + 1;
										}
									} else if (fvar == 2) {
										m = m + 2;
										failureList.add(subledger.getSubledger_name());
										failcount = failcount + 1;
									}
								} else {
									
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									subledger.setIp_address(remoteAddr);
									subledger.setCreated_by(user);
									subLedgerService.saveExcelSubLedger(subledger);
									if (fvar == 1) {
										m = 1;
										successList.add(subledger.getSubledger_name());
										successcount = successcount + 1;

									} else if (fvar == 2) {
										m = m + 2;
										failureList.add(subledger.getSubledger_name());
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
		
		model.setViewName("redirect:/subledgerList");
		return model;
	}

	@RequestMapping(value = "subledgerOpeningList", method = RequestMethod.GET)
	public ModelAndView subledgerOpeningList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		boolean importfail = false;
		boolean importflag = true;
		User user = new User();
		user = (User) session.getAttribute("user");
		long company_id = (long) session.getAttribute("company_id");
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		List<SubLedger> subledgerListfail = new ArrayList<>();
		System.out.println("Editing Client Balance ");
		// for start of new account year and request for editing last yearr account
		 Boolean startNewYrFlag = false;
		 String stringDate = new LocalDate().toString();
		 String[] splits = stringDate.split("-");
		 if(splits[1].trim().equalsIgnoreCase("04")&&splits[2].trim().equalsIgnoreCase("01"))
		 {
			 startNewYrFlag=true;
		 }
		 //
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
	
			subledgerList = subLedgerService.findAllSubLedgersOnlyOfCompany(company_id);
			
			subledgerListfail = subLedgerService.findAllSubLedgersListingOfCompany(company_id, (long) 0);
			supplierList = supplierService.findAllSuppliersOnlyOfCompany(company_id);
			company = companyService.getCompanyWithIndustrytype(company_id);
			 String[] years=company.getYearRange().split(",");
				List<Long> yearIds = new ArrayList<Long>();
				for (String yId : years) {
					System.out.println("TheYId is " +yId);
					yearIds.add(new Long(yId));
				}
				Collections.sort(yearIds);

				
			Long YearId=	yearIds.get(0);
			if (subledgerListfail.size() != 0)
				importfail = true;
			Long quote_id = subscribeservice.getquoteofcompany(company_id);
			String email = user.getCompany().getEmail_id();
			if (quote_id != 0) {
				importflag = quoteservice.findAllimportDetailsUser(email, quote_id);
			} else {
				importflag = quoteservice.findAllimportDetailsUser(email, quote_id);
			}
			bankList = bankService.findAllBanksOfCompany(company_id);
			customerList = customerService.findAllCustomersOnlyOFCompany(company_id);
			Long year_id = accountingYearService.findcurrentyear();

			if (company.getTrial_balance() == null)
			{
				model.addObject("opening_flag", "0");
				List<OpeningBalances>  subledgerListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  bankListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  supplierListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  customerListOP = new ArrayList<OpeningBalances>();
				customerListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 5,year_id,(long)1);
				supplierListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 4,year_id,(long)1);
				bankListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 3,year_id,(long)1);
				subledgerListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 2,year_id,(long)1);
				model.addObject("customerListOP", customerListOP);
				model.addObject("supplierListOP", supplierListOP);
				model.addObject("bankListOP", bankListOP);
				model.addObject("subledgerListOP", subledgerListOP);
			}
			else if (company.getTrial_balance() == false)
			{
				model.addObject("opening_flag", "0");
				List<OpeningBalances>  subledgerListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  bankListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  supplierListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  customerListOP = new ArrayList<OpeningBalances>();
				customerListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 5,year_id,(long)1);
				supplierListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 4,year_id,(long)1);
				bankListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 3,year_id,(long)1);
				subledgerListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 2,year_id,(long)1);
				model.addObject("customerListOP", customerListOP);
				model.addObject("supplierListOP", supplierListOP);
				model.addObject("bankListOP", bankListOP);
				model.addObject("subledgerListOP", subledgerListOP);
			}
			else if (company.getTrial_balance() == true)
			{
				model.addObject("opening_flag", "1");		
				List<OpeningBalances>  subledgerListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  bankListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  supplierListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  customerListOP = new ArrayList<OpeningBalances>();
				customerListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 5,year_id,(long)2);
				supplierListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 4,year_id,(long)2);
				bankListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 3,year_id,(long)2);
				subledgerListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 2,year_id,(long)2);
				model.addObject("customerListOP", customerListOP);
				model.addObject("supplierListOP", supplierListOP);
				model.addObject("bankListOP", bankListOP);
				model.addObject("subledgerListOP", subledgerListOP);
			}
			System.out.println("testingOB");
			
			model.addObject("customerList", customerList);
			model.addObject("supplierList", supplierList);
			model.addObject("bankList", bankList);
			model.addObject("subledgerList", subledgerList);
		//model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
			model.addObject("yearList",accountingYearService.findAccountRangeOfYearId(user_id, YearId, company_id));
			model.addObject("importflag", importflag);
		model.addObject("importfail", importfail);
		model.addObject("clientflag", 0);
		model.addObject("startNewYrFlag", startNewYrFlag);
		model.setViewName("/master/subledger_opening_balance");
		return model;
	}
	@RequestMapping(value = "editclientopeningbalances", method = RequestMethod.GET)
	public ModelAndView editclientopeningbalances(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = new User();
		user = (User) session.getAttribute("user");
		
		
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		List<Company> companyList = new ArrayList<Company>();
		if(role == ROLE_SUPERUSER) {
			companyList = companyService.getApprovedCompanies();
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getApprovedCompanies(user.getUser_id());
		}
		model.addObject("companyList", companyList);
		model.addObject("yearList", accountingYearService.findAll());
		model.setViewName("/master/client_subledger_opening_balance");
		return model;
	} 
	@RequestMapping(value = "saveclientsubledgerOpeningList", method = RequestMethod.POST)
	public ModelAndView savesubledgerOpeningList(HttpServletRequest request, HttpServletResponse response,@RequestParam("company_id") long cid) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = new User();
		user = (User) session.getAttribute("user");
		long company_id = cid;
		company = companyService.getCompanyWithIndustrytype(company_id);
		String yearrange =user.getCompany().getYearRange(); 
		 String[] years = null;
		// Long YearId;
			List<Long> yearIds = new ArrayList<Long>();
			Long YearId = null;
			System.out.println("First element is ");
		//System.out.println("First element is "+yearIds.get(0));
		//System.out.println("Last element is "+yearIds.get(yearIds.size()-1));
		//System.out.println("Istrial balance " +user.getCompany().getTrial_balance());
		//System.out.println("Year Range is " +yearrange);
		long user_id=(long)session.getAttribute("user_id");
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		
		Long year_id = accountingYearService.findcurrentyear();

		if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
			/*if(role == ROLE_SUPERUSER) {*/
			
				subledgerList = subLedgerService.findAllSubLedgersOnlyOfCompany(company_id);
				bankList = bankService.findAllBanksOfCompany(company_id);
				supplierList = supplierService.findAllSuppliersOnlyOfCompany(company_id);
				customerList = customerService.findAllCustomersOnlyOFCompany(company_id);
				List<OpeningBalances>  subledgerListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  bankListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  supplierListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  customerListOP = new ArrayList<OpeningBalances>();
				customerListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 5,year_id,(long)1);
				supplierListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 4,year_id,(long)1);
				bankListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 3,year_id,(long)1);
				subledgerListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 2,year_id,(long)1);
				model.addObject("customerListOP", customerListOP);
				model.addObject("supplierListOP", supplierListOP);
				model.addObject("bankListOP", bankListOP);
				model.addObject("subledgerListOP", subledgerListOP);
			/*}*/
		/*	if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				subledgerList = subLedgerService.findAllSubLedgersOnlyOfCompany(company_id);
				bankList = bankService.findAllBanksOfCompany(company_id);
				supplierList = supplierService.findAllSuppliersOnlyOfCompany(company_id);
				customerList = customerService.findAllCustomersOnlyOFCompany(company_id);
				List<OpeningBalances>  subledgerListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  bankListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  supplierListOP = new ArrayList<OpeningBalances>();
				List<OpeningBalances>  customerListOP = new ArrayList<OpeningBalances>();
				customerListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 5,year_id,(long)1);
				supplierListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 4,year_id,(long)1);
				bankListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 3,year_id,(long)1);
				subledgerListOP = openingBalances.findAllOPbalancesofdata(company_id, (long) 2,year_id,(long)1);
				model.addObject("customerListOP", customerListOP);
				model.addObject("supplierListOP", supplierListOP);
				model.addObject("bankListOP", bankListOP);
				model.addObject("subledgerListOP", subledgerListOP);
			}	*/		
				if(company.getTrial_balance()==null){
					System.out.println("null");
					years=company.getYearRange().split(",");
					model.addObject("openingbalance_date", "0");
				}else if (company.getTrial_balance() == false){
					System.out.println("false");
					years=company.getYearRange().split(",");
					model.addObject("openingbalance_date", "0");
				}
				else if (company.getTrial_balance() == true)
				{
					LocalDate date1=company.getOpeningbalance_date();
					//lastdate =date.minusDays(1);
					if(company.getYear_range()!=null){
					YearId=company.getYear_range().getYear_id();}else{
					years=company.getYearRange().split(",");}
					date1=date1.plusDays(1);
					System.out.println("date1 " +date1);
					String dt=date1.getMonthOfYear() + "/" +date1.getDayOfMonth()+"/" + date1.getYear();
					System.out.println("true " +company.getOpeningbalance_date() + "  " + dt);
					model.addObject("openingbalance_date", dt);
				}
				if(years!=null && years.length>0){
				for (String yId : years) {
					System.out.println("TheYId is " +yId);
					yearIds.add(new Long(yId));
				}
				Collections.sort(yearIds);

				System.out.println(yearIds);
				 YearId=	yearIds.get(0);
				 }
			System.out.println("editOBtodayadmin");
			model.addObject("opening_flag", "0");
			model.addObject("customerList", customerList);
			model.addObject("supplierList", supplierList);
			model.addObject("bankList", bankList);
			model.addObject("subledgerList", subledgerList);
		}
		System.out.println("editOBtoday");
		model.addObject("clientflag", 1);
		model.addObject("yearList",accountingYearService.findAccountRangeOfYearId(user_id,YearId , company_id));
		//Only First Year of Subscription Accounting Year Range to be shown
		//model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
		
		model.setViewName("/master/subledger_opening_balance");
		return model;
	}
	@RequestMapping(value = "Batchsubledger", method = RequestMethod.POST)
	public ModelAndView subledgerBatchApproval(@ModelAttribute("batchsubledger") SubLedger subLedger,
			HttpServletRequest request, 
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");
		Boolean flag = true;
		Boolean primaryApproval = null;
		Boolean rejectApproval = null;

		if (subLedger.getPrimaryApproval() != null) {
			primaryApproval = subLedger.getPrimaryApproval();
		}
		if (subLedger.getRejectApproval() != null) {
			rejectApproval = subLedger.getRejectApproval();
		}

		if (rejectApproval != null) {
			flag = subLedgerService.rejectByBatch(subLedger.getSubledgerList());
			for( String subId : subLedger.getSubledgerList())
			{
				yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, Long.parseLong(subId), null, null, null, null, (long)APPROVAL_STATUS_REJECT);
			}
			if (flag == true) {
				session.setAttribute("successMsg", "SubLedger Rejected Successfully");
			} else {
				session.setAttribute("errorMsg", "Please select atleast one SubLedger.");
			}
			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && rejectApproval) {
				model.setViewName("redirect:/subLedgerPrimaryValidationList");
			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !rejectApproval) {
				model.setViewName("redirect:/subLedgerSecondaryValidationList");
			}
		}

		if (primaryApproval != null) {
			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && primaryApproval) {
				flag = subLedgerService.approvedByBatch(subLedger.getSubledgerList(), primaryApproval);
				for( String subId : subLedger.getSubledgerList())
				{
					yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, Long.parseLong(subId), null, null, (long)APPROVAL_STATUS_PRIMARY, null,null );
				}
				if (flag == true) {
					session.setAttribute("successMsg", "SubLedger Primary Approval Done Successfully");
				} else {
					session.setAttribute("errorMsg", "Please select atleast one SubLedger.");
				}
				model.setViewName("redirect:/subLedgerPrimaryValidationList");

			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !primaryApproval) {
				flag = subLedgerService.approvedByBatch(subLedger.getSubledgerList(), primaryApproval);
				for( String subId : subLedger.getSubledgerList())
				{
					yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, Long.parseLong(subId), null, null, null, (long)APPROVAL_STATUS_SECONDARY,null );
				}
				if (flag == true) {
					session.setAttribute("successMsg", "SubLedger Secondary Approval Done Successfully");
				} else {
					session.setAttribute("errorMsg", "Please select atleast one SubLedger.");
				}
				model.setViewName("redirect:/subLedgerSecondaryValidationList");

			}
		}
		return model;
	}

	@RequestMapping(value = "exportOpeningBalances", method = RequestMethod.GET)
	public ModelAndView exportOpeningBalances(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		subledgerList = subLedgerService.findAllSubLedgersOnlyOfCompany(user.getCompany().getCompany_id());
		supplierList = supplierService.findAllSuppliersOnlyOfCompany(user.getCompany().getCompany_id());
		bankList = bankService.findAllBanksOfCompany(user.getCompany().getCompany_id());
		customerList = customerService.findAllCustomersOnlyOFCompany(user.getCompany().getCompany_id());
		SubledgerExportForm form = new SubledgerExportForm();
		form.setBankList(bankList);
		form.setCustomerList(customerList);
		form.setSubledgerList(subledgerList);
		form.setSupplierList(supplierList);

		if (company != null) {
			if (company.getTrial_balance() == null) {
				form.setOpening_flag(0);
				form.setSubLedgerListIndustry(subLedgerListIndustry);
			} else if (company.getTrial_balance() == false) {
				form.setOpening_flag(0);
				form.setSubLedgerListIndustry(subLedgerListIndustry);
			} else if (company.getTrial_balance() == true)
				form.setOpening_flag(1);

		}
		return new ModelAndView("OpeningBalancesExcelView", "form", form);

	}

	@RequestMapping(value = { "importOpeningBalancesExcel" }, method = { RequestMethod.POST })
	public ModelAndView importOpeningBalancesExcel(@RequestParam("excelFile") MultipartFile excelfile, @RequestParam("importDate") String importDate,@RequestParam("importRange") String importRange,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView();
		boolean isValid = true;
		String msg = "";
		int m = 0;
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		
		User user = new User();
		user = (User)session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		
		String str1 = "";
		String str2 = "";
		double total_credit = (float) 0;
		double total_debit = (float) 0;

		AccountingYear year = null;
		LocalDate date = null;
		LocalDate lastdate = null;
		String dateString = null;
		
		System.out.println("The date is "+ importDate);
		List<SubledgerBalanceForm> subldgerlist = new ArrayList<SubledgerBalanceForm>();
		List<BankBalanceForm> banklist = new ArrayList<BankBalanceForm>();
		List<SupplierBalanceForm> supplierlist = new ArrayList<SupplierBalanceForm>();
		List<CustomerBalanceForm> customerlist = new ArrayList<CustomerBalanceForm>();
		List<AccountingYear> yearlist = accountingYearService.findAccountRange(user.getUser_id(), yearrange,company_id);
		List<SubLedger> sublist =  subLedgerService.findAllSubLedgersOnlyOfCompany(company_id);
		List<Bank> listOfbank = bankService.findAllBanksOfCompany(company_id);
		List<Suppliers> suplist = supplierService.findAllSuppliersOnlyOfCompany(company_id);
		List<Customer> cuslist = customerService.findAllCustomersOnlyOFCompany(company_id);

		Integer mainCount = 0;
		
		try {
			if (excelfile.getOriginalFilename().endsWith("xls")) {
				int i = 0;
				HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
				HSSFSheet worksheet = workbook.getSheetAt(0);

				if (isValid) {
					i = 1;
					while (i <= worksheet.getLastRowNum()) {
						HSSFRow row = worksheet.getRow(i++);
						
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer count = 0;
							
							if (row.getCell(16) != null && row.getCell(16).getStringCellValue().trim().isEmpty()==false) {
								
								try {
									for(AccountingYear year_range:yearlist)
									{
										
										String str =year_range.getYear_range().trim();
										//if(str.trim().equalsIgnoreCase(row.getCell(16).getStringCellValue().trim())) {
										if(str.trim().equalsIgnoreCase(importRange.trim())) {
											year=year_range;
										
										    break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
                           /*if (row.getCell(17) != null && row.getCell(17).getStringCellValue().isEmpty()==false) {
								
                            
							}*/
							//if(row.getCell(17) != null && row.getCell(17).getDateCellValue()!=null)
							if(importDate!=null || importDate!="")
							{
							System.out.println("importdaate not null");
							//date = new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(17).getDateCellValue().toString()));
							//	date = new LocalDate(ClassToConvertDateForExcell.dateConvert(importDate));
							date=LocalDate.parse(importDate);
							System.out.println("date is "+date);
								lastdate =date.minusDays(1);
							/*date= new LocalDate();
							String[] diviededDate = date.toString().split("-");
							String yearofDate = diviededDate[0];
						    dateString= yearofDate+"-"+"03"+"-"+"31";*/
							dateString = lastdate.toString();
							
							}
							if(year!=null && dateString!=null)
							{
							if (row.getCell(1) == null || row.getCell(1).getStringCellValue().trim().isEmpty()==true) {
								count = count + 1;
							} else {
								try {
									for (SubLedger subLedger : sublist) {
										String str = null;
										str = subLedger.getSubledger_name().trim();

										if (str.trim().equalsIgnoreCase(row.getCell(1).getStringCellValue().trim())) {

											SubledgerBalanceForm form = new SubledgerBalanceForm();
											form.setSubledger(subLedger);
									
											form.setCreditBalance(row.getCell(2).getNumericCellValue());
											form.setDebitBalance(row.getCell(3).getNumericCellValue());
											subldgerlist.add(form);
											total_credit = total_credit + (row.getCell(2).getNumericCellValue());
											total_debit = total_debit + (row.getCell(3).getNumericCellValue());
											count = count + 1;
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							}

							if (row.getCell(4) == null || row.getCell(4).getStringCellValue().trim().isEmpty()==true) {
								count = count + 1;
							} else {
								try {
									for (Bank bank : listOfbank) {
										String bankName = null;
										String bankAccBranchName = null;
										bankName = bank.getBank_name().trim();
										bankAccBranchName = bank.getBranch()+"/"+bank.getAccount_no();
										if (bankName.trim().equalsIgnoreCase(row.getCell(4).getStringCellValue().trim()) && bankAccBranchName.trim().equalsIgnoreCase(row.getCell(5).getStringCellValue().trim())) {

											BankBalanceForm form = new BankBalanceForm();
											form.setBank(bank);
											form.setCreditBalance(row.getCell(6).getNumericCellValue());
											form.setDebitBalance(row.getCell(7).getNumericCellValue());
											banklist.add(form);
											total_credit = total_credit + (row.getCell(6).getNumericCellValue());
											total_debit = total_debit + (row.getCell(7).getNumericCellValue());
											count = count + 1;
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							if (row.getCell(8) == null ||  row.getCell(8).getStringCellValue().trim().isEmpty()==true) {
								count = count + 1;
							} else {
								try {
									for (Suppliers supplier : suplist) {
										String strCompanyName = null;
										strCompanyName = supplier.getCompany_name().trim();
										String strcontactName = null;
										strcontactName = supplier.getContact_name().trim();
										if (strCompanyName.trim().equalsIgnoreCase(row.getCell(8).getStringCellValue().trim()) && strcontactName.trim().equalsIgnoreCase(row.getCell(9).getStringCellValue().trim())) {
											SupplierBalanceForm form = new SupplierBalanceForm();
											form.setSupplier(supplier);
											form.setCreditBalance(row.getCell(10).getNumericCellValue());
											form.setDebitBalance(row.getCell(11).getNumericCellValue());
											supplierlist.add(form);
											total_credit = total_credit + (row.getCell(10).getNumericCellValue());
											total_debit = total_debit + (row.getCell(11).getNumericCellValue());
											count = count + 1;
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							if (row.getCell(12) == null || row.getCell(12).getStringCellValue().trim().isEmpty()==true) {
								count = count + 1;
							} else {
								try {
									for (Customer customer : cuslist) {
										
										String strCompanyName = null;
										strCompanyName = customer.getFirm_name().trim();
										String strcontactName = null;
										strcontactName = customer.getContact_name().trim();
										if (strCompanyName.trim().equalsIgnoreCase(row.getCell(12).getStringCellValue().trim()) && strcontactName.trim().equalsIgnoreCase(row.getCell(13).getStringCellValue().trim())) {
											CustomerBalanceForm form = new CustomerBalanceForm();
											form.setCustomer(customer);
											form.setCreditBalance(row.getCell(14).getNumericCellValue());
											form.setDebitBalance(row.getCell(15).getNumericCellValue());
											customerlist.add(form);
											total_credit = total_credit + (row.getCell(14).getNumericCellValue());
											total_debit = total_debit + (row.getCell(15).getNumericCellValue());
											count = count + 1;
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						
							if (count == 4) {

							} else {
								mainCount = mainCount + 1;
							}

						     }
							else
							{
								mainCount = 1;
								m=m+1;
							}
						}
					}
					workbook.close();

					if (mainCount == 0) {
						str1 += Math.round(total_credit);
						str2 += Math.round(total_debit);
						if (str1.equals(str2)) {
							m = 1;
							if (subldgerlist != null && subldgerlist.size() > 0) {
								for (SubledgerBalanceForm subform : subldgerlist) {
									try {
										subLedgerService.addsubledgeropeningbalancenew(company_id, subform.getSubledger().getSubledger_Id(), (long) 1, subform.getCreditBalance(), subform.getDebitBalance(),dateString,year.getYear_id());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							}
							if (banklist != null && banklist.size() > 0) {
								for (BankBalanceForm bankform : banklist) {
									try {
										bankService.addbankopeningbalancenew(company_id, bankform.getBank().getBank_id(), (long) 3, bankform.getCreditBalance(), bankform.getDebitBalance(),dateString,year.getYear_id());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							if (supplierlist != null && supplierlist.size() > 0) {
								for (SupplierBalanceForm supform : supplierlist) {
									try {
										supplierService.addsupplieropeningbalancenew(company_id, supform.getSupplier().getSupplier_id(), (long) 4, supform.getCreditBalance(), supform.getDebitBalance(),dateString,year.getYear_id());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							if (customerlist != null && customerlist.size() > 0) {
								for (CustomerBalanceForm cusform : customerlist) {
									try {
										customerService.addcustomeropeningbalancenew(company_id, cusform.getCustomer().getCustomer_id(), (long) 5, cusform.getCreditBalance(), cusform.getDebitBalance(),dateString,year.getYear_id());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							companyService.setTrialBalance(company_id,lastdate,year.getYear_id());

						} else {
							m = 2;
						}
					}

				} else {
					System.out.println("no data");
				}
				
			} else if (excelfile.getOriginalFilename().endsWith("xlsx")) {
				int i = 0;
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);

				if (isValid) {
					i = 1;

					while (i <= worksheet.getLastRowNum()) {
						XSSFRow row = worksheet.getRow(i++);
						System.out.println(" name "+row.getCell(0));
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer count = 0;
							
							if (row.getCell(16) != null && row.getCell(16).getStringCellValue().trim().isEmpty()==false) {
								
								try {
									for(AccountingYear year_range:yearlist)
									{
										
										String str =year_range.getYear_range().trim();
										//if(str.trim().equalsIgnoreCase(row.getCell(16).getStringCellValue().trim())) {
										if(str.trim().equalsIgnoreCase(importRange.trim())) {
											year=year_range;
											
										    break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
                           /*if (row.getCell(17) != null && row.getCell(17).getStringCellValue().isEmpty()==false) {
								
                            Select Accounting Year
							}*/
							//if(row.getCell(17) != null && row.getCell(17).getDateCellValue()!=null)
							if(importDate!=null || importDate!="" )
							{
								
							//date = new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(17).getDateCellValue().toString()));
							//	date = new LocalDate(ClassToConvertDateForExcell.dateConvert(importDate));
								System.out.println("importdaate not null");
								//date = new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(17).getDateCellValue().toString()));
								//	date = new LocalDate(ClassToConvertDateForExcell.dateConvert(importDate));
								date=LocalDate.parse(importDate);
								System.out.println("date is "+date);
									
								lastdate =date.minusDays(1);
							/*date= new LocalDate();
							String[] diviededDate = date.toString().split("-");
							String yearofDate = diviededDate[0];
						    dateString= yearofDate+"-"+"03"+"-"+"31";*/
							dateString = lastdate.toString();
							
							}
							
							if(year!=null && dateString!=null)
							{
							if (row.getCell(1) == null || row.getCell(1).getStringCellValue().trim().isEmpty()==true) {
								count = count + 1;
								
							} else {
								try {
									for (SubLedger subLedger : sublist) {
										String str = null;
										str = subLedger.getSubledger_name().trim();

										if (str.trim().equalsIgnoreCase(row.getCell(1).getStringCellValue().trim())) {

											SubledgerBalanceForm form = new SubledgerBalanceForm();
											form.setSubledger(subLedger);
									
											form.setCreditBalance(row.getCell(2).getNumericCellValue());
											form.setDebitBalance(row.getCell(3).getNumericCellValue());
											subldgerlist.add(form);
											total_credit = total_credit + (row.getCell(2).getNumericCellValue());
											total_debit = total_debit + (row.getCell(3).getNumericCellValue());
											count = count + 1;
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
							System.out.println("The subledger count "+count);
							if (row.getCell(4) == null || row.getCell(4).getStringCellValue().trim().isEmpty()==true) {
								count = count + 1;
								
							} else {
								try {
									for (Bank bank : listOfbank) {
										String bankName = null;
										String bankAccBranchName = null;
										bankName = bank.getBank_name().trim();
										bankAccBranchName = bank.getBranch()+"/"+bank.getAccount_no();
										if (bankName.trim().equalsIgnoreCase(row.getCell(4).getStringCellValue().trim()) && bankAccBranchName.trim().equalsIgnoreCase(row.getCell(5).getStringCellValue().trim())) {
											System.out.println("row "+ i + " bank is same");
											BankBalanceForm form = new BankBalanceForm();
											form.setBank(bank);
											form.setCreditBalance(row.getCell(6).getNumericCellValue());
											form.setDebitBalance(row.getCell(7).getNumericCellValue());
											banklist.add(form);
											total_credit = total_credit + (row.getCell(6).getNumericCellValue());
											total_debit = total_debit + (row.getCell(7).getNumericCellValue());
											count = count + 1;
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							System.out.println("The Bank count "+count);
							if (row.getCell(8) == null ||  row.getCell(8).getStringCellValue().trim().isEmpty()==true) {
								count = count + 1;
								System.out.println("row "+ i + " cell 8 is null");
							} else {
								try {
									for (Suppliers supplier : suplist) {
										String strCompanyName = null;
										strCompanyName = supplier.getCompany_name().trim();
										String strcontactName = null;
										strcontactName = supplier.getContact_name().trim();
										if (strCompanyName.trim().equalsIgnoreCase(row.getCell(8).getStringCellValue().trim()) && strcontactName.trim().equalsIgnoreCase(row.getCell(9).getStringCellValue().trim())) {
											SupplierBalanceForm form = new SupplierBalanceForm();
											form.setSupplier(supplier);
											System.out.println("row "+ i + " supplier is same");
											form.setCreditBalance(row.getCell(10).getNumericCellValue());
											form.setDebitBalance(row.getCell(11).getNumericCellValue());
											supplierlist.add(form);
											total_credit = total_credit + (row.getCell(10).getNumericCellValue());
											total_debit = total_debit + (row.getCell(11).getNumericCellValue());
											count = count + 1;
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
							System.out.println("The supplier count "+count);
							if (row.getCell(12) == null || row.getCell(12).getStringCellValue().trim().isEmpty()==true) {
								count = count + 1;
								System.out.println("row "+ i + " cell 12 is null");
							} else {
								try {
									for (Customer customer : cuslist) {
										
										String strCompanyName = null;
										strCompanyName = customer.getFirm_name().trim();
										String strcontactName = null;
										strcontactName = customer.getContact_name().trim();
										if (strCompanyName.trim().equalsIgnoreCase(row.getCell(12).getStringCellValue().trim()) && strcontactName.trim().equalsIgnoreCase(row.getCell(13).getStringCellValue().trim())) {
											CustomerBalanceForm form = new CustomerBalanceForm();
											form.setCustomer(customer);
											System.out.println("row "+ i + " cust same");
											form.setCreditBalance(row.getCell(14).getNumericCellValue());
											form.setDebitBalance(row.getCell(15).getNumericCellValue());
											customerlist.add(form);
											total_credit = total_credit + (row.getCell(14).getNumericCellValue());
											total_debit = total_debit + (row.getCell(15).getNumericCellValue());
											count = count + 1;
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							System.out.println("The row is "+i);
						System.out.println("The count is "+count);
							if (count == 4) {

							} else {
								mainCount = mainCount + 1;
							}

						     }
							else
							{
								mainCount = 1;
								m=m+1;
							}
						}
					}
					workbook.close();

					if (mainCount == 0) {
						str1 += Math.round(total_credit);
						str2 += Math.round(total_debit);
						System.out.println("Tota credit bank"+total_credit);
						System.out.println("Tota credit bank"+total_debit);
						if (str1.equals(str2)) {
							m = 1;
							if (subldgerlist != null && subldgerlist.size() > 0) {
								for (SubledgerBalanceForm subform : subldgerlist) {
									try {
										subLedgerService.addsubledgeropeningbalancenew(company_id, subform.getSubledger().getSubledger_Id(), (long) 1, subform.getCreditBalance(), subform.getDebitBalance(),dateString,year.getYear_id());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							}
							if (banklist != null && banklist.size() > 0) {
								for (BankBalanceForm bankform : banklist) {
									try {
										bankService.addbankopeningbalancenew(company_id, bankform.getBank().getBank_id(), (long) 3, bankform.getCreditBalance(), bankform.getDebitBalance(),dateString,year.getYear_id());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							if (supplierlist != null && supplierlist.size() > 0) {
								for (SupplierBalanceForm supform : supplierlist) {
									try {
										supplierService.addsupplieropeningbalancenew(company_id, supform.getSupplier().getSupplier_id(), (long) 4, supform.getCreditBalance(), supform.getDebitBalance(),dateString,year.getYear_id());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							if (customerlist != null && customerlist.size() > 0) {
								for (CustomerBalanceForm cusform : customerlist) {
									try {
										customerService.addcustomeropeningbalancenew(company_id, cusform.getCustomer().getCustomer_id(), (long) 5, cusform.getCreditBalance(), cusform.getDebitBalance(),dateString,year.getYear_id());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							companyService.setTrialBalance(company_id,lastdate,year.getYear_id());

						} else {
							m = 2;
						}
					}

				} else {
					System.out.println("no data");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isValid = false;
		}

		if(mainCount == 1 && year==null && dateString==null)
		{
			msg="Please Add Date and Accounting Year in Excel File";
		}
		if(mainCount == 1 && year!=null && dateString==null)
		{
			msg="Please Add Date in Excel File";
		}
		if(mainCount == 1 && year==null && dateString!=null)
		{
			msg="Please Add Accounting Year in Excel File";
		}
		if (mainCount == 0 && m == 2) {
			msg = "Opening Balances Not Saved ! Credit and Debit Balance Mismatch!. Credit Balance is " + str1 + " and Debit Balance is " + str2;
		}
		if (m == 0) {
			msg ="Please Enter Required and Valid Data In Columns";
		}
		if (m == 1 && mainCount == 0) {
			msg = "Opening Balances Saved Successfully";
		}
		session.setAttribute("successMsg", msg);
		model.setViewName("redirect:/subledgerOpeningList");
		return model;
	}
	
}