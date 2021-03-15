package com.fasset.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.fasset.controller.validators.BankValidator;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IAccountSubGroupService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IYearEndingService;

/**
 * @author "Vishwajeet"
 *
 */

@Controller
@SessionAttributes("user")
public class BankController extends MyAbstractController {

	@Autowired
	private IBankService bankService;

	@Autowired
	private IAccountSubGroupService accountSubGroupService;

	@Autowired
	private BankValidator bankValidator;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IQuotationService quoteservice;

	@Autowired
	private IClientSubscriptionMasterService subscribeservice;
	
	@Autowired	
	private IYearEndingService yearService;
	
	Boolean importFlag = null; // for maintaining the user on bankList.jsp after delete and view
	Boolean isImport = false;  // is bank saved or updated through excel or through System(Add bank)
	 private List<String> successList = new ArrayList<String>();
	 private List<String> failureList = new ArrayList<String>();
	 private List<String> updateList = new ArrayList<String>();
	 private  String successmsg;
	 private  String failmsg;
	 private  String updatemsg;
	 private  String error;
		
	@RequestMapping(value = "bankList", method = RequestMethod.GET)
	public ModelAndView bankList(@RequestParam(value = "msg", required = false) String msg, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		boolean importfail = false;
		boolean importflag = true;
		importFlag = true;
		long company_id = (long) session.getAttribute("company_id");
		List<Bank> bankList = new ArrayList<Bank>();
		List<Bank> bankListfailure = new ArrayList<Bank>();
		long defaultbankid=companyService.getdefaultbank(company_id);
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
			if (role == ROLE_SUPERUSER) {
				bankList = bankService.findAllListing(true);
				bankListfailure = bankService.findAllListing(false);
			}
			if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				bankList = bankService.findAllListing(true, user.getUser_id());
				bankListfailure = bankService.findAllListing(false, user.getUser_id());
			}
			if (bankListfailure.size() != 0)
				importfail = true;
		} else {
			bankList = bankService.findAllListingBanksOfCompany1(company_id, true);
			bankListfailure = bankService.findAllListingBanksOfCompany1(company_id, false);
			if (bankListfailure.size() != 0)
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
		model.addObject("defaultbankid",defaultbankid);
		model.addObject("importfail", importfail);
		model.addObject("importflag", importflag);
		model.addObject("bankList", bankList);
		model.setViewName("/master/bankList");
		return model;
	}

	@RequestMapping(value = "bankimportfailure", method = RequestMethod.GET)
	public ModelAndView bankimportfailure(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		List<Bank> bankList = new ArrayList<>();
		importFlag = false;
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (role == ROLE_SUPERUSER) {
			bankList = bankService.findAllListing(false);
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			bankList = bankService.findAllListing(false, user.getUser_id());
		} else {
			bankList = bankService.findAllListingBanksOfCompany1(company_id, false);
		}
		model.addObject("bankList", bankList);
		model.setViewName("/master/failureBankList");
		return model;
	}

	@RequestMapping(value = "bank", method = RequestMethod.GET)
	public ModelAndView bank(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		List<Company> companyList = new ArrayList<Company>();
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.list();
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
		}
		model.addObject("companyList", companyList);
		model.addObject("bank", new Bank());
		model.addObject("accountSubGroupList", accountSubGroupService.findSubgroup());
		model.setViewName("master/bank");
		return model;
	}

	@RequestMapping(value = "bank", method = RequestMethod.POST)
	public ModelAndView bank(@ModelAttribute("bank") Bank bank, HttpServletRequest request,
			HttpServletResponse response, BindingResult result) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		
		
		
		if (role == ROLE_SUPERUSER) {
			
			bank.setCompany_id(bank.getCompany_id());
			
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			
			bank.setCompany_id(bank.getCompany_id());
			
		}
		else if(role == ROLE_CLIENT) {
			bank.setCompany_id(company_id);
		}
		
		bankValidator.validate(bank, result);
		
		if (result.hasErrors()) {
			List<Company> companyList = new ArrayList<Company>();
			if (role == ROLE_SUPERUSER) {
				companyList = companyService.list();
			} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				companyList = companyService.getAllCompaniesExe(user.getUser_id());
			}
			model.addObject("companyList", companyList);
			model.addObject("accountSubGroupList", accountSubGroupService.findSubgroup());
			model.setViewName("master/bank");
		} else {
			try {
				if (bank.getBank_id() == null) {
					bank.setBank_approval(APPROVAL_STATUS_PENDING);
					if (role == ROLE_SUPERUSER || role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
						bank.setCompany(companyService.getById(bank.getCompany_id()));
					} else {
						bank.setCompany(user.getCompany());
					}
					bank.setFlag(true);
					bank.setCreated_by(user);
					bankService.add(bank);
					
					 String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            bank.setIp_address(remoteAddr);

					model.addObject("successMsg", "Bank added successfully");
				} else {
					if (role == ROLE_SUPERUSER || role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
						bank.setCompany(companyService.getById(bank.getCompany_id()));
					} else {
						bank.setCompany(user.getCompany());
					}
					bank.setUpdated_by(user);
					Bank oldbank=bankService.findOneView(bank.getBank_id());
					if((oldbank.getBank_approval() == 1) || (oldbank.getBank_approval() == 3))
					{
						if (role == ROLE_EXECUTIVE)
							bank.setBank_approval(2);
						else if((role == ROLE_CLIENT) || (role == ROLE_EMPLOYEE) || (role == ROLE_TRIAL_USER))
							bank.setBank_approval(0);
						else
							bank.setBank_approval(oldbank.getBank_approval());
					}
					else
					{
						bank.setBank_approval(oldbank.getBank_approval());
					}	
					bankService.update(bank);
					session.setAttribute("successMsg", "Bank updated successfully");
				}
			} catch (MyWebException e) {
				e.printStackTrace();
			}
			model.setViewName("redirect:/bankList");
		}
		return model;
	}

	@RequestMapping(value = "viewBank", method = RequestMethod.GET)
	public ModelAndView viewBank(@RequestParam("id") Long id, @RequestParam("flag") Long flag) {
		ModelAndView model = new ModelAndView();
		try {
			Bank bank = bankService.findOneView(id);
			model.addObject("bank", bank);
			model.addObject("flag", flag);
			model.addObject("importFlag", importFlag);
			model.setViewName("master/bankView");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	@RequestMapping(value = "editBank", method = RequestMethod.GET)
	public ModelAndView editBank(@RequestParam("id") Long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		try {
			Bank bank = bankService.findOneView(id);
			List<Company> companyList = new ArrayList<Company>();
			if (role == ROLE_SUPERUSER) {
				companyList = companyService.list();
				bank.setCompany_id(bank.getCompany().getCompany_id());
			} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				companyList = companyService.getAllCompaniesExe(user.getUser_id());
				bank.setCompany_id(bank.getCompany().getCompany_id());
			}
			if(bank.getAccount_sub_group_id()!=null)
			bank.setSubGroupId(bank.getAccount_sub_group_id().getSubgroup_Id());
			model.addObject("companyList", companyList);
			model.addObject("bank", bank);
			model.addObject("accountSubGroupList", accountSubGroupService.findSubgroup());
			model.setViewName("master/bank");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	@RequestMapping(value = "bankPrimaryValidationList", method = RequestMethod.GET)
	public ModelAndView bankPrimaryValidationList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		List<Bank> bankList = new ArrayList<Bank>();

		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}

		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}

		if (role == ROLE_SUPERUSER) {
			bankList = bankService.findByStatus(APPROVAL_STATUS_PENDING);
		} else if (role == ROLE_EXECUTIVE) {
			bankList = bankService.findByStatus(role,APPROVAL_STATUS_PENDING, user.getUser_id());
		}
		model.addObject("bankList", bankList);
		model.addObject("bankbatch", new Bank());
		model.setViewName("/master/bankPrimaryValidationList");
		return model;
	}

	@RequestMapping(value = "bankSecondaryValidationList", method = RequestMethod.GET)
	public ModelAndView bankSecondaryValidationList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		List<Bank> bankList = new ArrayList<Bank>();

		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}

		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}

		if (role == ROLE_SUPERUSER) {
			bankList = bankService.findByStatus(APPROVAL_STATUS_PRIMARY);
		} else if (role == ROLE_MANAGER) {
			bankList = bankService.findByStatus(role,APPROVAL_STATUS_PRIMARY, user.getUser_id());
		}
		model.addObject("bankList", bankList);
		model.addObject("bankbatch", new Bank());
		model.setViewName("/master/bankSecondaryValidationList");
		return model;
	}

	@RequestMapping(value = "rejectBank", method = RequestMethod.GET)
	public ModelAndView rejectCustomer(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("rejectApproval") Boolean rejectApproval) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		
		Long bankId = id;
		String msg = "";
		msg = bankService.updateByApproval(bankId, APPROVAL_STATUS_REJECT);
		yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, null, bankId, null, null, null, (long)APPROVAL_STATUS_REJECT);
		session.setAttribute("successMsg", msg);
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && rejectApproval)) {
			model.setViewName("redirect:/bankPrimaryValidationList");
		} else {
			model.setViewName("redirect:/bankSecondaryValidationList");
		}
		return model;
	}

	@RequestMapping(value = "approveBank", method = RequestMethod.GET)
	public ModelAndView approveBank(@RequestParam("id") long id,
			@RequestParam("primaryApproval") Boolean primaryApproval, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		Long bankId = id;
		String msg = "";
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && primaryApproval == true)) {
			msg = bankService.updateByApproval(bankId, APPROVAL_STATUS_PRIMARY);
			yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, null, bankId, null, (long)APPROVAL_STATUS_PRIMARY, null, null);
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/bankPrimaryValidationList");
		} else {
			msg = bankService.updateByApproval(bankId, APPROVAL_STATUS_SECONDARY);
			yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, null, bankId, null, null, (long)APPROVAL_STATUS_SECONDARY, null);
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/bankSecondaryValidationList");
		}
		return model;
	}

	@RequestMapping(value = "deleteBank", method = RequestMethod.GET)
	public ModelAndView deleteBank(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String msg = "You can't delete bank";
		/*msg = bankService.deleteByIdValue(id);*/
		session.setAttribute("successMsg", msg);
		if(importFlag == true)
		{
			return new ModelAndView("redirect:/bankList");
		}
		else
		{
			return new ModelAndView("redirect:/bankimportfailure");
		}
	}

	
	@RequestMapping(value = "deletefailurBankList", method = RequestMethod.GET)
	public ModelAndView deletefailurBankList(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		//String msg = "You can't delete bank";
		String msg="";
		msg = bankService.deleteByIdValue(id);
		session.setAttribute("successMsg", msg);
		if(importFlag == true)
		{
			return new ModelAndView("redirect:/bankList");
		}
		else
		{
			return new ModelAndView("redirect:/bankimportfailure");
		}
	}

	
	
	
	@RequestMapping(value = { "importExcelBank" }, method = { RequestMethod.POST })
	public ModelAndView importExcelBank(@RequestParam("excelFile") MultipartFile excelfile, HttpServletRequest request,
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
		int m = 0;
		// int duplicate = 0, newQue = 0;
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		long role = (long) session.getAttribute("role");

		User user = new User();
		user = (User) session.getAttribute("user");
		try {
			if (excelfile.getOriginalFilename().endsWith("xls")) {
				int i = 0;
				// Creates a workbook object from the uploaded excelfile
				HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
				// Creates a worksheet object representing the first sheet
				HSSFSheet worksheet = workbook.getSheetAt(0);
				// Reads the data in excel file until last row is encountered
				int rowcount=0;
			while (i <= worksheet.getLastRowNum()) {
				int colcount=0;
					HSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 4; j++) {
						if (row.getCell(j) != null) {
							colcount++;
						}
					}
					if(colcount>=5)
					{
					rowcount++;
					}
				}

				if (isValid) {
					i = 1;
					while (i <= rowcount) {
						// boolean flag = false;
						Bank bank = new Bank();
						// Creates an object for the UserInfo Model
						// Creates an object representing a single row in excel
						HSSFRow row = worksheet.getRow(i++);
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer fvar = 0;
							Integer fvarcount = 1;
							List<AccountSubGroup> accountsubGroupList = accountSubGroupService.findAll();
							try {
								for (AccountSubGroup acsubgroup : accountsubGroupList) {
									String str = "";
									str = acsubgroup.getSubgroup_name().replaceAll(" ", "");									
									if (str.equalsIgnoreCase(row.getCell(4).getStringCellValue().replaceAll(" ", ""))) {
										bank.setSubGroupId(acsubgroup.getSubgroup_Id());
										bank.setAccount_sub_group_id(acsubgroup);
										fvar = 1;
										break;
									} else {
										fvar = 2;
									}

								}
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
												row.getCell(5).getStringCellValue().replaceAll(" ", ""))) {
											bank.setCompany(comp);
											bank.setCompany_id(comp.getCompany_id());
											fvar = 1;
											break;
										} else {
											fvar = 2;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								bank.setCompany(user.getCompany());
								bank.setCompany_id(company_id);
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							bank.setBank_name(row.getCell(0).getStringCellValue());
							String bankn = bank.getBank_name().replace("\"", "").replace("\'", "").replace("-", "");
							bank.setBank_name(bankn);
							bank.setBranch(row.getCell(1).getStringCellValue());
							bank.setAccount_no((long) row.getCell(2).getNumericCellValue());
							bank.setIfsc_no(row.getCell(3).getStringCellValue());
							bank.setCreated_by(user);
							bank.setBank_approval(0);
							bank.setStatus(true);
							if (fvarcount == 1)
							{
								bank.setFlag(true);
								m = 1;
							}
							else if (fvarcount == 0)
							{
								m = m + 2;
								bank.setFlag(false);
							}
							if (fvar != 0) {
								int exist = bankService.isExistsAccount(bank.getAccount_no(), bank.getCompany_id());
								if (exist == 1) {
									Bank bk = bankService.isExists(bank.getAccount_no(), bank.getCompany_id());
									bank.setUpdated_by(user);
									bank.setBank_id(bk.getBank_id());
									
									bankService.update(bank);
									if (fvarcount == 1) {
										m = 1;
										updateList.add(bank.getBank_name());
										updatecount = updatecount + 1;
									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(bank.getBank_name());
										failcount = failcount + 1;
									}
								} else {
									
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									bank.setIp_address(remoteAddr);
									bank.setCreated_by(user);
									bankService.addExcel(bank);
									if (fvarcount == 1) {
										m = 1;
										successList.add(bank.getBank_name());
										successcount = successcount + 1;

									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(bank.getBank_name());
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
				// Creates a workbook object from the uploaded excelfile
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				// Creates a worksheet object representing the first sheet
				XSSFSheet worksheet = workbook.getSheetAt(0);
				// Reads the data in excel file until last row is encountered
				int rowcount=0;
			while (i <= worksheet.getLastRowNum()) {
				int colcount=0;
					XSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 4; j++) {
						if (row.getCell(j) != null) {
							colcount++;
						}
					}
					if(colcount>=5)
					{
					rowcount++;
					}
				}

				if (isValid) {
					i = 1;
					while (i <= rowcount) {
						// boolean flag = false;
						Bank bank = new Bank();
						// Creates an object for the UserInfo Model
						// Creates an object representing a single row in excel
						XSSFRow row = worksheet.getRow(i++);
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Integer fvar = 0;
							Integer fvarcount = 1;
							List<AccountSubGroup> accountsubGroupList = accountSubGroupService.findAll();
							try {
								for (AccountSubGroup acsubgroup : accountsubGroupList) {
									String str = "";
									str = acsubgroup.getSubgroup_name().replaceAll(" ", "");									
									if (str.equalsIgnoreCase(row.getCell(4).getStringCellValue().replaceAll(" ", ""))) {
										bank.setSubGroupId(acsubgroup.getSubgroup_Id());
										bank.setAccount_sub_group_id(acsubgroup);
										fvar = 1;
										break;
									} else {
										fvar = 2;
									}

								}
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
												row.getCell(5).getStringCellValue().replaceAll(" ", ""))) {
											bank.setCompany(comp);
											bank.setCompany_id(comp.getCompany_id());
											fvar = 1;
											break;
										} else {
											fvar = 2;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								bank.setCompany(user.getCompany());
								bank.setCompany_id(company_id);
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							bank.setBank_name(row.getCell(0).getStringCellValue());
							String bankn = bank.getBank_name().replace("\"", "").replace("\'", "").replace("-", "");
							bank.setBank_name(bankn);
							bank.setBranch(row.getCell(1).getStringCellValue());
							bank.setAccount_no((long) row.getCell(2).getNumericCellValue());
							bank.setIfsc_no(row.getCell(3).getStringCellValue());
							bank.setCreated_by(user);
							bank.setBank_approval(0);
							bank.setStatus(true);
							if (fvarcount == 1)
							{
								bank.setFlag(true);
								m = 1;
							}
							else if (fvarcount == 0)
							{
								m = m + 2;
								bank.setFlag(false);
							}
							if (fvar != 0) {
								int exist = bankService.isExistsAccount(bank.getAccount_no(), bank.getCompany_id());
								if (exist == 1) {
									Bank bk = bankService.isExists(bank.getAccount_no(), bank.getCompany_id());
									bank.setUpdated_by(user);
									bank.setBank_id(bk.getBank_id());
									
									bankService.update(bank);
									if (fvarcount == 1) {
										m = 1;
										updateList.add(bank.getBank_name());
										updatecount = updatecount + 1;
									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(bank.getBank_name());
										failcount = failcount + 1;
									}
								} else {
									
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									bank.setIp_address(remoteAddr);
									bank.setCreated_by(user);
									bankService.addExcel(bank);
									if (fvarcount == 1) {
										m = 1;
										successList.add(bank.getBank_name());
										successcount = successcount + 1;

									} else if (fvarcount == 0) {
										m = m + 2;
										failureList.add(bank.getBank_name());
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
		model.setViewName("redirect:/bankList");
		return model;
	}

	@RequestMapping(value = "Batchbank", method = RequestMethod.POST)
	public ModelAndView subledgerBatchApproval(@ModelAttribute("bankbatch") Bank bank, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");
		Boolean flag = true;
		Boolean primaryApproval = null;
		Boolean rejectApproval = null;

		if (bank.getPrimaryApproval() != null) {
			primaryApproval = bank.getPrimaryApproval();
		}
		if (bank.getRejectApproval() != null) {
			rejectApproval = bank.getRejectApproval();
		}

		if (rejectApproval != null) {
			flag = bankService.rejectByBatch(bank.getBankList());	
			for( String bankId : bank.getBankList())
			{
				yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, null, Long.parseLong(bankId), null, null, null, (long)APPROVAL_STATUS_REJECT);
			}
			if (flag) {
				session.setAttribute("successMsg", "Bank Rejected Successfully");
			} else {
				session.setAttribute("errorMsg", "Please select atleast one Bank.");
			}
			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && rejectApproval) {
				model.setViewName("redirect:/bankPrimaryValidationList");
			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !rejectApproval) {
				model.setViewName("redirect:/bankSecondaryValidationList");
			}
		}

		if (primaryApproval != null) {
			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && primaryApproval) {
				flag = bankService.approvedByBatch(bank.getBankList(), primaryApproval);
				
				for( String bankId : bank.getBankList())
				{
					yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, null, Long.parseLong(bankId), null, (long)APPROVAL_STATUS_PRIMARY, null, null);
				}
				if (flag == true) {
					session.setAttribute("successMsg", "Bank Primary Approval Done Successfully");
				} else {
					session.setAttribute("errorMsg", "Please select atleast one Bank.");
				}
				model.setViewName("redirect:/bankPrimaryValidationList");

			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !primaryApproval) {
				flag = bankService.approvedByBatch(bank.getBankList(), primaryApproval);
				for( String bankId : bank.getBankList())
				{
					yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, null, Long.parseLong(bankId), null, null, (long)APPROVAL_STATUS_SECONDARY, null);
				}
				if (flag == true) {
					session.setAttribute("successMsg", "Bank Secondary Approval Done Successfully");
				} else {
					session.setAttribute("errorMsg", "Please select atleast one Bank.");
				}
				model.setViewName("redirect:/bankSecondaryValidationList");
			}
		}
		return model;
	}
}