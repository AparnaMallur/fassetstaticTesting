/**
 * mayur suramwar
 */
package com.fasset.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.fasset.controller.validators.LedgerValidator;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountGroupType;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.Company;
import com.fasset.entities.IndustryType;
import com.fasset.entities.Ledger;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.service.interfaces.IAccountSubGroupService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.fasset.service.interfaces.IYearEndingService;

/**
 * @author Vijay ghodake
 *
 *         deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class LedgerMasterControllerMVC extends MyAbstractController {

	@Autowired
	private IAccountSubGroupService accountSubGroupService;

	@Autowired
	private LedgerValidator ledgerValidator;

	@Autowired
	private ILedgerService ledgerService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IQuotationService quoteservice;

	@Autowired
	private IClientSubscriptionMasterService subscribeservice;

	@Autowired
	private IBankService bankService;

	@Autowired
	ISubLedgerService subledgerService;
	@Autowired
	private ICustomerService customerService;

	@Autowired
	private ISuplliersService supplierService;

	@Autowired	
	private IYearEndingService yearService;
	
	Boolean importFlag = null;  // for maintaining the user on ledgerList.jsp after delete and view
	Boolean isImport = false;  // is ledger saved or updated through excel or through System(Add ledger)
	 private List<String> successList = new ArrayList<String>();
	 private List<String> failureList = new ArrayList<String>();
	 private List<String> updateList = new ArrayList<String>();
	 private  String successmsg;
	 private  String failmsg;
	 private  String updatemsg;
	 private  String error;
	
	@RequestMapping(value = "ledger", method = RequestMethod.GET)
	public ModelAndView ledger(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		List<Company> companyList = new ArrayList<Company>();
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.list();
		}
		if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
		}
		Ledger ledger = new Ledger();
		List<AccountSubGroup> accountSubGroupList = accountSubGroupService.findAll();
		model.addObject("companyList", companyList);
		model.addObject("accountSubGroupList", accountSubGroupList);
		model.addObject("ledger", ledger);
		model.setViewName("/master/ledger");
		return model;
	}

	@RequestMapping(value = "saveledger", method = RequestMethod.POST)
	public ModelAndView saveLedger(@ModelAttribute("ledger") Ledger ledger, 
			BindingResult result, 
			HttpServletRequest request,
			HttpServletResponse response) {

		User user = new User();
		ModelAndView model = new ModelAndView(); 
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");

		ledgerValidator.validate(ledger, result);
		if (result.hasErrors()) {
			List<Company> companyList = new ArrayList<Company>();
			if (role == ROLE_SUPERUSER) {
				companyList = companyService.list();
			}
			if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				companyList = companyService.getAllCompaniesExe(user.getUser_id());
			}
			List<AccountSubGroup> accountSubGroupList = accountSubGroupService.findAll();
			model.addObject("companyList", companyList);
			model.addObject("accountSubGroupList", accountSubGroupList);
			model.setViewName("/master/ledger");
		} else {
			String msg = "";
			try {
				if (ledger.getLedger_id() != null) {
					long id = ledger.getLedger_id();
					if (id > 0) {
						
						Ledger oldledger = ledgerService.findOne(ledger.getLedger_id());
						if((oldledger.getLedger_approval() == 1) || (oldledger.getLedger_approval() == 3))
						{
							if (role == ROLE_EXECUTIVE)
								ledger.setLedger_approval(2);
							else if((role == ROLE_CLIENT) || (role == ROLE_EMPLOYEE) || (role == ROLE_TRIAL_USER))
								ledger.setLedger_approval(0);
							else
								ledger.setLedger_approval(oldledger.getLedger_approval());
						}
						else
						{
							ledger.setLedger_approval(oldledger.getLedger_approval());
						}			
						ledger.setFlag(true);
						ledger.setUpdated_by(user);
						ledgerService.update(ledger);
						msg = "Ledger updated successfully";
					}
				} else {
					ledger.setLedger_approval(APPROVAL_STATUS_PENDING);
					if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
						ledger.setCompany(companyService.getById(ledger.getCompany_id()));
					} else {
						ledger.setCompany_id(company_id);
						ledger.setCompany(companyService.getById(company_id));
					}
					ledger.setFlag(true);
					ledger.setCreated_by(user);
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            ledger.setIp_address(remoteAddr);
					msg = ledgerService.saveLedger(ledger);
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/ledgerList");
		}
		return model;
	}

	@RequestMapping(value = "ledgerList", method = RequestMethod.GET)
	public ModelAndView ledgerList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		user = (User) session.getAttribute("user");
		long company_id = (long) session.getAttribute("company_id");
		boolean importflag = true;
		List<Ledger> ledgerList = new ArrayList<Ledger>();
		List<Ledger> ledgerListfail = new ArrayList<Ledger>();
		boolean importfail = false;
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

		if ((role == ROLE_SUPERUSER)) {
			ledgerList = ledgerService.findAllListing((long) 1);
			ledgerListfail = ledgerService.findAllListing((long) 0);
			if (ledgerListfail.size() != 0)
				importfail = true;
			
		} else if ((role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
			ledgerList = ledgerService.findAllListingExe(true, user.getUser_id());
			ledgerListfail = ledgerService.findAllListingExe(false, user.getUser_id());
			if (ledgerListfail.size() != 0)
				importfail = true;
		} else {
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
			ledgerList = ledgerService.findAllListingLedgersOfCompany(company_id, (long) 1);
			ledgerListfail = ledgerService.findAllListingLedgersOfCompany(company_id, (long) 0);
			if (ledgerListfail.size() != 0)
				importfail = true;
			Company company = companyService.getCompanyWithIndustrytype(company_id);
			IndustryType type = company.getIndustry_type();
			Set<SubLedger> ledgerListIndustry = type.getSubLedgers();
			model.addObject("ledgerListIndustry", ledgerListIndustry);
			 
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
		model.addObject("ledgerList", ledgerList);
		model.setViewName("/master/ledgerList");
		return model;
	}
	 
	@RequestMapping(value = "ledgerimportfailure", method = RequestMethod.GET)
	public ModelAndView ledgerimportfailure(@RequestParam(value = "msg", required = false) String msg,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		importFlag = false;
		List<Ledger> ledgerList = new ArrayList<Ledger>();
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (role == ROLE_SUPERUSER) {
			ledgerList = ledgerService.findAllListing((long) 0);
		} 
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			ledgerList = ledgerService.findAllListingExe(false, user.getUser_id());
		}
		else {
			ledgerList = ledgerService.findAllListingLedgersOfCompany(company_id, (long) 0);
		}
		model.addObject("ledgerList", ledgerList);
		model.setViewName("/master/ledgerList");
		return model;
	}

	@RequestMapping(value = "viewLedger", method = RequestMethod.GET)
	public ModelAndView viewLedger(@RequestParam("id") long id, @RequestParam("flag") long flag) {
		ModelAndView model = new ModelAndView();
		Ledger ledger = new Ledger();
		AccountSubGroup subgroup = new AccountSubGroup();
		AccountGroup group = new AccountGroup();
		AccountGroupType type = new AccountGroupType();
		try {
			ledger = ledgerService.findOneWithAll(id);
			subgroup = ledger.getAccsubgroup();
			group = subgroup.getAccountGroup();
			type = group.getGrouptype();

		} catch (Exception e) {
			e.printStackTrace();
		}
	
		model.addObject("ledger", ledger);
		model.addObject("subgroup", subgroup);
		model.addObject("group", group);
		model.addObject("type", type);
		model.addObject("flag", flag);
		model.addObject("importFlag", importFlag);
		model.addObject("view", "view");
		model.setViewName("/master/ledgerView");
		return model;
	}

	@RequestMapping(value = "editLedger", method = RequestMethod.GET)
	public ModelAndView editLedger(@RequestParam("id") long id, 
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		Ledger ledger = new Ledger();
		try {
			if (id > 0) {
				ledger = ledgerService.findOneWithAll(id);
				
				if (ledger.getFlag() != null) {
					ledger.setFlag(ledger.getFlag());
				}
				if (ledger.getCredit_opening_balance() != null) {
					ledger.setCredit_opening_balance1(ledger.getCredit_opening_balance().toString());
				}
				if (ledger.getDebit_opening_balance() != null) {
					ledger.setDebit_opening_balance1(ledger.getDebit_opening_balance().toString());
				}
				if (role == ROLE_SUPERUSER) {
					if (ledger.getCompany() != null)
						ledger.setCompany_id(ledger.getCompany().getCompany_id());
					List<Company> companyList = companyService.list();
					model.addObject("companyList", companyList);
				}
				if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
					if (ledger.getCompany() != null)
						ledger.setCompany_id(ledger.getCompany().getCompany_id());
					List<Company> companyList = companyService.getAllCompaniesExe(user.getUser_id());
					model.addObject("companyList", companyList);
				}
				if (ledger.getAccsubgroup() != null)
					ledger.setSubgroup_Id(ledger.getAccsubgroup().getSubgroup_Id());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<AccountSubGroup> accountSubGroupList = accountSubGroupService.findAll();
		model.addObject("accountSubGroupList", accountSubGroupList);
		model.addObject("ledger", ledger);
		model.setViewName("/master/ledger");
		return model;
	}

	@RequestMapping(value = "ledgerPrimaryValidationList", method = RequestMethod.GET)
	public ModelAndView ledgerPrimaryValidationList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		Ledger batchledger = new Ledger();
		List<Ledger> ledgerList =  new ArrayList<Ledger>();
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
			ledgerList = ledgerService.findByStatus(APPROVAL_STATUS_PENDING);				
		}
		if(role == ROLE_EXECUTIVE ) {
			ledgerList = ledgerService.findByStatus(role,APPROVAL_STATUS_PENDING, user.getUser_id());
		}
		model.addObject("ledgerList", ledgerList);
		model.addObject("batchledger", batchledger);
		model.setViewName("/master/ledgerPrimaryValidationList");
		return model;
	}

	@RequestMapping(value = "ledgerSecondaryValidationList", method = RequestMethod.GET)
	public ModelAndView ledgerSecondaryValidationList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		Ledger batchledger = new Ledger();
		List<Ledger> ledgerList =  new ArrayList<Ledger>();
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
			ledgerList = ledgerService.findByStatus(APPROVAL_STATUS_PRIMARY);				
		}
		if(role == ROLE_MANAGER) {
			ledgerList = ledgerService.findByStatus(role,APPROVAL_STATUS_PRIMARY, user.getUser_id());
		}
		model.addObject("ledgerList", ledgerList);
		model.addObject("batchledger", batchledger);
		model.setViewName("/master/ledgerSecondaryValidationList");
		return model;
	}

	@RequestMapping(value = "rejectLedger", method = RequestMethod.GET)
	public ModelAndView rejectLedger(@RequestParam("id") long id,
			@RequestParam("rejectApproval") Boolean rejectApproval, 
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		Long ledgerId = id;
		String msg = "";
		msg = ledgerService.updateByApproval(ledgerId, APPROVAL_STATUS_REJECT);
		yearService.saveActivityLogForm(user.getUser_id(), null, null, null, ledgerId, null, null, null, null, null, (long)APPROVAL_STATUS_REJECT);
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && rejectApproval)) {
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/ledgerPrimaryValidationList");
		} else {
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/ledgerSecondaryValidationList");
		}
		return model;
	}

	@RequestMapping(value = "approveLedger", method = RequestMethod.GET)
	public ModelAndView approveLedger(@RequestParam("id") long id,
			@RequestParam("primaryApproval") Boolean primaryApproval, 
			HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		Long ledgerId = id;
		String msg = "";
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");	
		
		long role = (long) session.getAttribute("role");
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && primaryApproval)) {
			msg = ledgerService.updateByApproval(ledgerId, APPROVAL_STATUS_PRIMARY);
			yearService.saveActivityLogForm(user.getUser_id(), null, null, null, ledgerId, null, null, null, (long)APPROVAL_STATUS_PRIMARY, null, null);
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/ledgerPrimaryValidationList");
		}
		else {
			msg = ledgerService.updateByApproval(ledgerId, APPROVAL_STATUS_SECONDARY);
			yearService.saveActivityLogForm(user.getUser_id(), null, null, null, ledgerId, null, null, null, null, (long)APPROVAL_STATUS_SECONDARY, null);
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/ledgerSecondaryValidationList");
		}
		return model;
	}

	@RequestMapping(value = "deleteLedger", method = RequestMethod.GET)
	public ModelAndView deleteLedger(@RequestParam("id") long id, 
			HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String msg = "You can't delete ledger";
		session.setAttribute("successMsg", msg);
		/*msg = ledgerService.deleteByIdValue(id);		*/
	
		if(importFlag == true)
		{	
			return new ModelAndView("redirect:/ledgerList");
		}
		else
		{
			return new ModelAndView("redirect:/ledgerimportfailure");
		}
	}

	@RequestMapping(value = { "importExcelLedger" }, method = { RequestMethod.POST })
	public ModelAndView importExcelLedger(@RequestParam("excelFile") MultipartFile excelfile, 
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView();
		boolean isValid = true;
		String success = "Record Imported successfully";
		int successcount = 0;
		String fail = "in failure list, Please refer failure list";
		int failcount = 0;
		String update = "Updated List";
		int updatecount = 0;
		successmsg = "";
	    failmsg = "";
	    updatemsg = "";
	    error = "";
	    isImport=true;
	    successList.clear();
		failureList.clear();
		updateList.clear();
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		User user = new User();
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		int m = 0;
		try {
			if (excelfile.getOriginalFilename().endsWith("xls")) {
				int i = 0;
				// Creates a workbook object from the uploaded excelfile
				HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
				// Creates a worksheet object representing the first sheet
				HSSFSheet worksheet = workbook.getSheetAt(0);
				int rowcount=0;
				// Reads the data in excel file until last row is encountered
				while (i <= worksheet.getLastRowNum()) {
					int colcount=0;
					HSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 2; j++) {
						if (row.getCell(j) != null) {
							colcount++;
						}
					}
					if(colcount>=3)
					{
					rowcount++;
					}
				}

				if (isValid) {
					i = 1;
					while (i <= rowcount) {
						Ledger ledger = new Ledger();
						HSSFRow row = worksheet.getRow(i++);
						
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer fvar = 0;
							List<AccountSubGroup> accountsubGroupList = accountSubGroupService.findAll();
							try {
								for (AccountSubGroup acsubgroup : accountsubGroupList) {
									String str = "";
									str = acsubgroup.getSubgroup_name().replaceAll(" ", "");
									
									if (str.equalsIgnoreCase(row.getCell(1).getStringCellValue().replaceAll(" ", ""))) {
										ledger.setSubgroup_Id(acsubgroup.getSubgroup_Id());
										ledger.setAccsubgroup(acsubgroup);
										ledger.setFlag(true);
										fvar = 1;
										break;
									} else {
										ledger.setFlag(false);
										fvar = 2;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
								List<Company> companylist = companyService.findAll();
								try {
									for (Company comp : companylist) {
										String str = "";
										str = comp.getCompany_name().replaceAll(" ", "");
										if (str.equalsIgnoreCase(
												row.getCell(3).getStringCellValue().replaceAll(" ", ""))) {
											ledger.setCompany(comp);
											ledger.setCompany_id(comp.getCompany_id());
											fvar = 1;
											ledger.setFlag(true);
											break;
										} else {
											if(fvar == 1)
												fvar=1;
											else
												fvar=2;
											ledger.setFlag(false);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								ledger.setCompany(user.getCompany());
								ledger.setCompany_id(company_id);
							}
							boolean assub = row.getCell(2).getStringCellValue().matches("Yes");
							if (assub == true) {
								if (fvar == 1)
									ledger.setAs_subledger(true);
								else
									ledger.setAs_subledger(false);
							} else {
								ledger.setAs_subledger(false);
							}
							ledger.setLedger_name(row.getCell(0).getStringCellValue());
							String subname = ledger.getLedger_name().replace("\"", "").replace("\'", "");
							ledger.setLedger_name(subname);
							
							ledger.setLedger_approval(0);
							ledger.setCreated_by(user);
							ledger.setStatus(true);

							if (fvar != 0) {
								int exist = ledgerService.isExistsledger(ledger.getLedger_name(),
										ledger.getCompany_id());
								if (exist == 1) {
									Ledger lg = ledgerService.isExists(ledger.getLedger_name(), ledger.getCompany_id());
									ledger.setUpdated_by(user);
									ledgerService.updateExcel(ledger,lg.getLedger_id());
									if (fvar == 1) {
										m = 1;
										if(lg.getAllocated()==null)
										{
										updateList.add(ledger.getLedger_name());
										updatecount = updatecount + 1;
										}
										
									} else if (fvar == 2) {
										m = m + 2;
										failureList.add(ledger.getLedger_name());
								    	failcount = failcount + 1;
									}
								} else {
									
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									ledger.setIp_address(remoteAddr);
									ledger.setCreated_by(user);
									ledgerService.add(ledger);
									if (fvar == 1) {
										m = 1;
										successList.add(ledger.getLedger_name());
										successcount = successcount + 1;
									} else if (fvar == 2) {
										m = m + 2;
										failureList.add(ledger.getLedger_name());
										failcount = failcount + 1;
									}
								}
							}
						}
					}
					workbook.close();
				} else {
					System.out.println("no data");
				}
			} else if (excelfile.getOriginalFilename().endsWith("xlsx")) {
				int i = 0;
				isValid = true;
				// Creates a workbook object from the uploaded excelfile
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				// Creates a worksheet object representing the first sheet
				XSSFSheet worksheet = workbook.getSheetAt(0);
				// Reads the data in excel file until last row is encountered
				int rowcount=0;
				while (i <= worksheet.getLastRowNum()) {
					int colcount=0;
					XSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 2; j++) {
						if (row.getCell(j) != null) {
							colcount++;
						}
					}
					if(colcount>=3)
					{
					rowcount++;
					}
				}

				if (isValid) {
					i = 1;
					while (i < rowcount) {
						Ledger ledger = new Ledger();
						XSSFRow row = worksheet.getRow(i++);
						
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer fvar = 0;
							List<AccountSubGroup> accountsubGroupList = accountSubGroupService.findAll();
							try {
								for (AccountSubGroup acsubgroup : accountsubGroupList) {
									String str = "";
									str = acsubgroup.getSubgroup_name().replaceAll(" ", "");
									
									if (str.equalsIgnoreCase(row.getCell(1).getStringCellValue().replaceAll(" ", ""))) {
										ledger.setSubgroup_Id(acsubgroup.getSubgroup_Id());
										ledger.setAccsubgroup(acsubgroup);
										ledger.setFlag(true);
										fvar = 1;
										break;
									} else {
										ledger.setFlag(false);
										fvar = 2;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
								List<Company> companylist = companyService.findAll();
								try {
									for (Company comp : companylist) {
										String str = "";
										str = comp.getCompany_name().replaceAll(" ", "");
										if (str.equalsIgnoreCase(
												row.getCell(3).getStringCellValue().replaceAll(" ", ""))) {
											ledger.setCompany(comp);
											ledger.setCompany_id(comp.getCompany_id());
											fvar = 1;
											ledger.setFlag(true);
											break;
										} else {
											if(fvar == 1)
												fvar=1;
											else
												fvar=2;
											ledger.setFlag(false);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								ledger.setCompany(user.getCompany());
								ledger.setCompany_id(company_id);
							}
							boolean assub = row.getCell(2).getStringCellValue().matches("Yes");
							if (assub == true) {
								if (fvar == 1)
									ledger.setAs_subledger(true);
								else
									ledger.setAs_subledger(false);
							} else {
								ledger.setAs_subledger(false);
							}
							ledger.setLedger_name(row.getCell(0).getStringCellValue());
							String subname = ledger.getLedger_name().replace("\"", "").replace("\'", "");
							ledger.setLedger_name(subname);
							ledger.setLedger_approval(0);
							ledger.setCreated_by(user);
							ledger.setStatus(true);

							if (fvar != 0) {
								int exist = ledgerService.isExistsledger(ledger.getLedger_name(),
										ledger.getCompany_id());
								if (exist == 1) {
									Ledger lg = ledgerService.isExists(ledger.getLedger_name(), ledger.getCompany_id());
									ledger.setUpdated_by(user);
									ledgerService.updateExcel(ledger,lg.getLedger_id());
									if (fvar == 1) {
										m = 1;
										if(lg.getAllocated()==null)
										{
										updateList.add(ledger.getLedger_name());
										updatecount = updatecount + 1;
										}
									} else if (fvar == 2) {
										m = m + 2;
										failureList.add(ledger.getLedger_name());
								    	failcount = failcount + 1;
									}
								} else {
									
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									ledger.setIp_address(remoteAddr);
									ledger.setCreated_by(user);
									ledgerService.add(ledger);
									if (fvar == 1) {
										m = 1;
										successList.add(ledger.getLedger_name());
										successcount = successcount + 1;
									} else if (fvar == 2) {
										m = m + 2;
										failureList.add(ledger.getLedger_name());
										failcount = failcount + 1;
									}
								}
							}
						}
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
		model.setViewName("redirect:/ledgerList");
		return model;
	}
	

	@RequestMapping(value = "saveopningbalance", method = RequestMethod.POST)
	public @ResponseBody String saveopningbalance(@RequestParam("credit") String credit,
			@RequestParam("debit") String debit, 
			@RequestParam("type") Long type, 
			@RequestParam("id") String id,
			@RequestParam("date") String date,
			@RequestParam("year") Long year,
			@RequestParam("client_company_id") Long client_company_id,
			HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("Editing Ob by year "+ year);
		System.out.println("Editing Ob by type "+ type);
		LocalDate date1= new LocalDate(date).minusDays(1);
		date = date1.toString();
		
		Long company_id = client_company_id;
		if (type == 1) {
			JSONArray creditArray = new JSONArray(credit);
			JSONArray debitArray = new JSONArray(debit);
			JSONArray subidd = new JSONArray(id);
			for (int i = 0; i < subidd.length(); i++) {
				JSONObject siddata = subidd.getJSONObject(i);
				JSONObject creditdata = creditArray.getJSONObject(i);
				JSONObject debitdata = debitArray.getJSONObject(i);
				Long sids = siddata.getLong("subid");
				System.out.println("Sid is 1 "+sids);
				Double creditval = creditdata.getDouble("credit");
				Double debitval =  debitdata.getDouble("debit");
				subledgerService.addsubledgeropeningbalancenew(company_id, sids, type, creditval, debitval,date,year);
			}
		}
		/*
		 * else if (type == 2) { JSONArray creditArray = new JSONArray(credit);
		 * JSONArray debitArray = new JSONArray(debit); JSONArray subidd = new
		 * JSONArray(id); for (int i = 0; i < subidd.length(); i++) { JSONObject siddata
		 * = subidd.getJSONObject(i); JSONObject creditdata =
		 * creditArray.getJSONObject(i); JSONObject debitdata =
		 * debitArray.getJSONObject(i); Long sids = siddata.getLong("subid"); Float
		 * creditval = (float) creditdata.getDouble("credit"); Float debitval = (float)
		 * debitdata.getDouble("debit");
		 * subledgerService.createsubledgerwithopening(company_id, sids, type,
		 * creditval, debitval); } }
		 */
		else if (type == 3) {
			JSONArray creditArray = new JSONArray(credit);
			JSONArray debitArray = new JSONArray(debit);
			JSONArray subidd = new JSONArray(id);
			for (int i = 0; i < subidd.length(); i++) {
				JSONObject siddata = subidd.getJSONObject(i);
				JSONObject creditdata = creditArray.getJSONObject(i);
				JSONObject debitdata = debitArray.getJSONObject(i);
				Long sids = siddata.getLong("subid");
				Double creditval =  creditdata.getDouble("credit");
				Double debitval =  debitdata.getDouble("debit");
				bankService.addbankopeningbalancenew(company_id, sids, type, creditval, debitval,date,year);
			}
		} else if (type == 4) {
			JSONArray creditArray = new JSONArray(credit);
			JSONArray debitArray = new JSONArray(debit);
			JSONArray subidd = new JSONArray(id);
			for (int i = 0; i < subidd.length(); i++) {
				JSONObject siddata = subidd.getJSONObject(i);
				JSONObject creditdata = creditArray.getJSONObject(i);
				JSONObject debitdata = debitArray.getJSONObject(i);
				Long sids = siddata.getLong("subid");
				Double creditval =  creditdata.getDouble("credit");
				Double debitval =  debitdata.getDouble("debit");
				supplierService.addsupplieropeningbalancenew(company_id, sids, type, creditval, debitval,date,year);
			}
		} else if (type == 5) {
			JSONArray creditArray = new JSONArray(credit);
			JSONArray debitArray = new JSONArray(debit);
			JSONArray subidd = new JSONArray(id);
			for (int i = 0; i < subidd.length(); i++) {
				JSONObject siddata = subidd.getJSONObject(i);
				JSONObject creditdata = creditArray.getJSONObject(i);
				JSONObject debitdata = debitArray.getJSONObject(i);
				Long sids = siddata.getLong("subid");
				Double creditval =  creditdata.getDouble("credit");
				Double debitval =  debitdata.getDouble("debit");
				customerService.addcustomeropeningbalancenew(company_id, sids, type, creditval, debitval,date,year);
			}
		}
		companyService.setTrialBalance(company_id,date1,year);
		return "Opening Balances Added Successfully!!!";
		/*
		 * String type=""; String[] myJsonData = request.getParameterValues("json[]");
		 * for (int i=0; i < myJsonData.length; ++i) { type=myJsonData[i]; } return
		 * type;
		 */
	}

	@RequestMapping(value = "Batchledger", method = RequestMethod.POST)
	public ModelAndView subledgerBatchApproval(@ModelAttribute("batchledger") Ledger ledger, 
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");
		
		Boolean flag = true;
		Boolean primaryApproval = null;
		Boolean rejectApproval = null;

		if (ledger.getPrimaryApproval() != null) {
			primaryApproval = ledger.getPrimaryApproval();
		}
		if (ledger.getRejectApproval() != null) {
			rejectApproval = ledger.getRejectApproval();
		}

		if (rejectApproval != null) {
			flag = ledgerService.rejectByBatch(ledger.getLedgerList());
			for( String ledgerId : ledger.getLedgerList())
			{
				yearService.saveActivityLogForm(user.getUser_id(), null, null, null, Long.parseLong(ledgerId), null, null, null, null, null, (long)APPROVAL_STATUS_REJECT);
			}
			if (flag == true) {
				session.setAttribute("successMsg", "Ledger Rejected Successfully");
			
			} else {
				session.setAttribute("errorMsg", "Please select atleast one Ledger.");
			
			}
			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && rejectApproval) {
				model.setViewName("redirect:/ledgerPrimaryValidationList");
			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !rejectApproval) {
				model.setViewName("redirect:/ledgerSecondaryValidationList");
			}
			/*model.addObject("batchledger", batchledger);
			if ((role == ROLE_EXECUTIVE && rejectApproval == true) || (role == ROLE_SUPERUSER && rejectApproval == true)) {
				List<Ledger> ledgerList = ledgerService.findByStatus(APPROVAL_STATUS_PENDING);
				model.addObject("ledgerList", ledgerList);
				model.setViewName("/master/ledgerPrimaryValidationList");
			} else if ((role == ROLE_MANAGER && rejectApproval == false) || (role == ROLE_SUPERUSER && rejectApproval == false)) {
				List<Ledger> ledgerList = ledgerService.findByStatus(APPROVAL_STATUS_PRIMARY);
				model.addObject("ledgerList", ledgerList);
				model.setViewName("/master/ledgerSecondaryValidationList");
			}*/
		}

		if (primaryApproval != null) {

			if ((role == ROLE_EXECUTIVE && primaryApproval == true) || (role == ROLE_SUPERUSER && primaryApproval == true)) {
				flag = ledgerService.approvedByBatch(ledger.getLedgerList(), primaryApproval);
				
				for( String ledgerId : ledger.getLedgerList())
				{
					yearService.saveActivityLogForm(user.getUser_id(), null, null, null, Long.parseLong(ledgerId), null, null, null, (long)APPROVAL_STATUS_PRIMARY, null, null);
				}
				if (flag == true) {
					session.setAttribute("successMsg", "Ledger Primary Approval Done Successfully");
					
				} else {
					session.setAttribute("errorMsg", "Please select atleast one Ledger.");
					
				}
				model.setViewName("redirect:/ledgerPrimaryValidationList");
				/*List<Ledger> ledgerList = ledgerService.findByStatus(APPROVAL_STATUS_PENDING);
				model.addObject("ledgerList", ledgerList);
				// model.addObject("successMsg", msg);
				model.addObject("batchledger", batchledger);
				model.setViewName("/master/ledgerPrimaryValidationList");*/

			} else if ((role == ROLE_MANAGER && primaryApproval == false) || (role == ROLE_SUPERUSER && primaryApproval == false)) {
				flag = ledgerService.approvedByBatch(ledger.getLedgerList(), primaryApproval);
				for( String ledgerId : ledger.getLedgerList())
				{
					yearService.saveActivityLogForm(user.getUser_id(), null, null, null, Long.parseLong(ledgerId), null, null, null, null, (long)APPROVAL_STATUS_SECONDARY, null);
				}
				if (flag == true) {
					session.setAttribute("successMsg", "Ledger Secondary Approval Done Successfully");
					
				} else {
					session.setAttribute("errorMsg", "Please select atleast one Ledger.");
					
				}
				/*List<Ledger> ledgerList = ledgerService.findByStatus(APPROVAL_STATUS_PRIMARY);
				model.addObject("ledgerList", ledgerList);
				// model.addObject("successMsg", msg);
				model.addObject("batchledger", batchledger);
				model.setViewName("/master/ledgerSecondaryValidationList");*/
				model.setViewName("redirect:/ledgerSecondaryValidationList");
			}
		}
		return model;
	}
}
