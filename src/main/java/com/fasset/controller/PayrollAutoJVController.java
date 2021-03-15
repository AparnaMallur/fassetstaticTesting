
package com.fasset.controller;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
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
import com.fasset.dao.interfaces.IPayrollAutoJVServiceDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Employee;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PayrollEmployeeDetails;
import com.fasset.entities.PayrollSubledgerDetails;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.entities.VoucherSeries;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.SalesForm;
import com.fasset.report.controller.ClassToConvertDateForExcell;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IEmployeeService;
import com.fasset.service.interfaces.IPayrollAutoJVService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IYearEndingService;

@Controller
@SessionAttributes("user")
public class PayrollAutoJVController extends MyAbstractController {

	@Autowired
	private IPayrollAutoJVService payroll_service;

	@Autowired
	private IEmployeeService employeeService;

	@Autowired
	private IAccountingYearService accountingYearService;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private ICommonService commonService;
	@Autowired
	private ISubLedgerDAO subLedgerDAO;

	@Autowired
	ISubLedgerService subledgerService;
	@Autowired
	private IYearEndingService yearService;
	@Autowired
	private IPayrollAutoJVServiceDAO dao;

	private Long payroll_id;

	@RequestMapping(value = "payrollAutoJV", method = RequestMethod.GET)
	public ModelAndView payrollAutoJV(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		List<AccountingYear>  yearList =new ArrayList<AccountingYear>();
		user = (User) session.getAttribute("user");
		long company_id = (long) session.getAttribute("company_id");
		String yearrange = user.getCompany().getYearRange();
		long AccYear=(long) session.getAttribute("AccountingYear");
		String strLong = Long.toString(AccYear);
		long user_id = (long) session.getAttribute("user_id");
		if(AccYear==-1){
			yearList = accountingYearService.findAccountRange(user_id, yearrange,company_id);
		}else{
			yearList = accountingYearService.findAccountRangeOfCompany(strLong);
		 
		}
		//List<AccountingYear> yearList = accountingYearService.findAccountRange(user_id, yearrange, company_id);
		
		if (yearList.size() != 0) {
			model.addObject("payrollAutoJV", new PayrollAutoJV());
			model.addObject("employeeList", employeeService.findAllEmployeeReletedToCompany(company_id));
			model.addObject("yearList", yearList);
			model.setViewName("/transactions/payrollAutoJV");
			return model;
		} else {
			session.setAttribute("msg", "Your account is closed");
			return new ModelAndView("redirect:/payrollAutoJVList");

		}
   
	}

	@RequestMapping(value = "payrollAutoJV", method = RequestMethod.POST)
	public ModelAndView addPayrollAutoJV(@ModelAttribute("payrollAutoJV") PayrollAutoJV payrollAutoJV,
			HttpServletRequest request, HttpServletResponse response) {

		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long user_id = (long) session.getAttribute("user_id");
		String remoteAddr = "";
		remoteAddr = request.getHeader("X-FORWARDED-FOR");
		if (remoteAddr == null || "".equals(remoteAddr)) {
			remoteAddr = request.getRemoteAddr();
		}
		payrollAutoJV.setIp_address(remoteAddr);
		payrollAutoJV.setCreated_by(user_id);
		payrollAutoJV.setCompany(user.getCompany());
		//payrollAutoJV.setVoucher_no(commonService.getVoucherNumber("AutoJV", AutoJV, payrollAutoJV.getDate(),payrollAutoJV.getAccountYearId(), user.getCompany().getCompany_id()));
		
	//	payrollAutoJV.setVoucher_no(commonService.getVoucherNumberForAUtoJV("AutoJV", payrollAutoJV.getDate(), payrollAutoJV.getAccountYearId(), user.getCompany().getCompany_id()));
		
		// autoJv.setVoucher_no(commonService.getVoucherNumber("AUTOJV",
		// VOUCHER_PAYROLL_AUTOJV, autoJv.getCreated_date(),autoJv.getAccountYearId(),
		// user.getCompany().getCompany_id()));

		PayrollAutoJV autojv = payroll_service.savePayrollAutoJv(payrollAutoJV);
		session.setAttribute("filemsg", "Payroll Auto JV Created Sucessfully");
		session.setAttribute("payroll", autojv);
		return new ModelAndView("redirect:/payrollImport");

	}

	@RequestMapping(value = "payrollAutoJVList", method = RequestMethod.GET)
	public ModelAndView payrollAutoJVList(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		List<YearEnding> yearEndlist = yearService.findAllYearEnding(company_id);
		String successMsg = (String) session.getAttribute("filemsg");
		session.removeAttribute("filemsg");
		Company company = null;
		try {
			company = companyService.getById(company_id);
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (company.getTrial_balance() != null) {
			if (company.getTrial_balance() == true)
				model.addObject("opening_flag", "1");
			else
				model.addObject("opening_flag", "0");
		} else
			model.addObject("opening_flag", "0");

		model.addObject("successMsg", successMsg);  
		model.addObject("company_id", company_id);
		model.addObject("yearEndlist", yearEndlist);
		model.addObject("payrollList", payroll_service.getAllPayrollJVReletedToCompany(company_id));
		model.addObject("yearEndlist", yearEndlist);
		model.setViewName("/transactions/payrollAutoJVList");
		return model;
	}

	@RequestMapping(value = { "importExcelPayroll" }, method = { RequestMethod.POST })
	public ModelAndView importExcelPayroll(@RequestParam("excelFile") MultipartFile excelfile,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		boolean isValid = true;
		Boolean isAllemployeeExist = true;
		Boolean isWorkingDay=true;
		String employeeCode = "";
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		long user_id = (long) session.getAttribute("user_id");
		StringBuffer successmsg = new StringBuffer();
		StringBuffer ErrorMsgs=new StringBuffer();
		BufferedWriter output = null;
		Integer m = 0;
		User user = new User();
		user = (User) session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		System.out.println("yr range "+yearrange);
		System.out.println("company_id "+company_id);
		List<AccountingYear> yearlist = accountingYearService.findAccountRange(user_id, yearrange, company_id);
		try {
			if (excelfile.getOriginalFilename().endsWith("xls")) {
				System.out.println("file is xls...");
				int i = 0;
				HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
				HSSFSheet worksheet = workbook.getSheetAt(0);

				if (isValid) {
					i = 1;
					PayrollAutoJV entry = new PayrollAutoJV();

					Set<PayrollEmployeeDetails> payrollEmployeeDetails = new HashSet<PayrollEmployeeDetails>();
					/* PayrollAutoJV entry = new PayrollAutoJV(); */

					List<Employee> employeeList = employeeService.findAllEmployeeReletedToCompany(company_id);

					while (i <= worksheet.getLastRowNum()) {

						PayrollEmployeeDetails details = new PayrollEmployeeDetails();
						Employee employee1 = null;

						HSSFRow row = worksheet.getRow(i++);
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							Boolean flag = false;
							String employeecode = "";
							try {
								employeecode = row.getCell(0).getStringCellValue().trim().replaceAll(" ", "");
								;
							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								Double employeecodefromExcel = row.getCell(0).getNumericCellValue();
								Integer employeecodefromExcel1 = employeecodefromExcel.intValue();
								employeecode = employeecodefromExcel1.toString().trim();

							} catch (Exception e) {
								e.printStackTrace();
							}

							if (entry.getIp_address() != null) {
								String remoteAddr = "";
								remoteAddr = request.getHeader("X-FORWARDED-FOR");
								if (remoteAddr == null || "".equals(remoteAddr)) {
									remoteAddr = request.getRemoteAddr();
								}
								entry.setIp_address(remoteAddr);
								entry.setCreated_by(user_id);

								if (row.getCell(1) != null) {
									try {
										
										entry.setDate(new LocalDate(ClassToConvertDateForExcell
												.dateConvert(row.getCell(21).getDateCellValue().toString())));
									} catch (Exception e) {
										e.printStackTrace();
									}
									try {
										entry.setDate(new LocalDate(ClassToConvertDateForExcell
												.dateConvert(row.getCell(21).getStringCellValue().toString())));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

								entry.setCompany(user.getCompany());
								if (row.getCell(6) != null) {
									if (row.getCell(22) != null) {
									entry.setOther_remark(row.getCell(22).getStringCellValue().trim());
								}
								}
							}

							for (Employee employee : employeeList) {
								if (employee.getCode().equals(employeecode)) {
									employee1 = employee;
									break;
								}
							}
							if (employee1 != null && isAllemployeeExist == true) {
								System.out.println("code is "+employee1.getCode());
								details.setCode(employee1.getCode());
								details.setName(employee1.getName());
								details.setPan(employee1.getPan());
							} else {
								System.out.println(" no code is "+employee1.getCode());
								isAllemployeeExist = false;
								employeeCode = employeecode;
							}

						}
					}
					workbook.close();
				}
			} else if (excelfile.getOriginalFilename().endsWith("xlsx")) {
				int i = 0;
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
				if (isValid) {
					i = 1;
					PayrollAutoJV entry = new PayrollAutoJV();
					DecimalFormat twoDForm = new DecimalFormat("#.##");
					Double totalCramount = 0.0;
					Double totalDramount = 0.0;
					double totalBasicSalary = 0.0;
					double totalDA = 0.0;
					double totalconveyanceAllowance = 0.0;
					double totalotherAllowances = 0.0;
					double totalgrossSalary = 0.0;
					double totalpfEmployeeContribution = 0.0;
					double totalESICEmployeeContribution = 0.0;
					double totalprofessionTax = 0.0;
					double totalLWF = 0.0;
					double totalTDS = 0.0;
					double totalotherDeductions = 0.0;
					double totaladvanceAdjustment = 0.0;
					double allTotalDeductions = 0.0;
					double totalnetSalary = 0.0;
					double totalpfEmployerContribution = 0.0;
					double totalESICEmployerContribution = 0.0;
					double totalPFAdminCharges = 0.0;
					double salaryandWagesDrAmount = 0.0;
					double ePFPayableCrAmount = 0.0;
					double eSICPayableCrAmount = 0.0;
					double pTPayableCrAmount = 0.0;
					double lWFPayableCrAmount = 0.0;
					double tDS192BPayableCrAmount = 0.0;
					double salaryPayableCrAmount = 0.0;
					double providentFundDrAmount = 0.0;
					double administrativeExpenseCrAmount = 0.0;
					double eSICDrAmount = 0.0;

					double otherSalaryDeductionsCramount=0;
					double salaryAdvanceCramount=0;

					Set<PayrollEmployeeDetails> payrollEmployeeDetailsList = new HashSet<PayrollEmployeeDetails>();

					List<Employee> employeeList = employeeService.findAllEmployeeReletedToCompany(company_id);
					while (i <= worksheet.getLastRowNum()) {
						PayrollEmployeeDetails details = new PayrollEmployeeDetails();
						Employee employee1 = null;
						isAllemployeeExist=true;
						XSSFRow row = worksheet.getRow(i++);
						
						ErrorMsgs.append(System.lineSeparator());
						ErrorMsgs.append(row.getRowNum());
						ErrorMsgs.append(" ");
						ErrorMsgs.append(row.getCell(1));
						ErrorMsgs.append(" ");
						if ((row.getLastCellNum() == 0 )) {
							System.out.println("Invalid Data");
						} else {

							String employeecode = "";
							try {
								employeecode = row.getCell(0).getStringCellValue().trim().replaceAll(" ", "");
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							try {
								Double employeecodefromExcel = row.getCell(0).getNumericCellValue();
								Integer employeecodefromExcel1 = employeecodefromExcel.intValue();
								employeecode = employeecodefromExcel1.toString().trim();
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if (entry.getIp_address() == null) {
								System.out.println("Ip addr");
								String remoteAddr = "";
								remoteAddr = request.getHeader("X-FORWARDED-FOR");
								//System.out.println("remoteaddr1 "+remoteAddr);
								if (remoteAddr == null || "".equals(remoteAddr)) {
								//	System.out.println("remote addr null");
									remoteAddr = request.getRemoteAddr();
									//System.out.println("remoteaddr " +remoteAddr);
								}
								entry.setIp_address(remoteAddr);}
								entry.setCreated_by(user_id);
								
								if (row.getCell(21) != null ) {
									if(row.getCell(21).getCellType()!=Cell.CELL_TYPE_BLANK){
									System.out.println(row.getCell(21).getCellType());
																	try {
										 System.out.println(row.getCell(21).getDateCellValue());
											entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(21).getDateCellValue().toString())));
											

									   }
									   catch (Exception e) {
									    e.printStackTrace();
										  // System.out.println("datecell error");
									    }
																	try {
																		 System.out.println(row.getCell(21).getDateCellValue());
																			entry.setDate(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(21).getStringCellValue().toString())));
																			

																	   }
																	   catch (Exception e) {
																	    e.printStackTrace();
																		  // System.out.println("datecell error");
																	    }
								}
						}
								
								entry.setCompany(user.getCompany());
								if (row.getCell(6) != null) {
									
									
									if(row.getCell(22)!=null) {
									entry.setOther_remark(row.getCell(22).getStringCellValue().trim());
								}
								}
								try {
									for (AccountingYear year_range : yearlist) {
//System.out.println("yearrange");
										String str = year_range.getYear_range().trim();
										if (str.replace(" ", "").trim().equalsIgnoreCase(
												row.getCell(23).getStringCellValue().replace(" ", "").trim())) {
											entry.setAccounting_year_id(year_range);
											
											if (entry.getDate() != null) {
												
												entry.setVoucher_no(
														commonService.getVoucherNumber("AUTOJV",VOUCHER_PAYROLL,
																entry.getDate(), year_range.getYear_id(), company_id));
											}
											if (entry.getDate() != null) 
											{
												if(row.getCell(24)!=null)
													{
														int day=(int) row.getCell(24).getNumericCellValue();
														if(day < 32 && day > 27)
														{
															entry.setDays((long)row.getCell(24).getNumericCellValue());
														}
														else {
																isWorkingDay = false;
																System.out.println("i is "+i);
																if(row.getRowNum()==1){
																ErrorMsgs.append("The number of days of first row employee should be greater than 27");}else{ErrorMsgs.append("Do not enter Date and AccountingYear  ");}
														}
													}
											}
											
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							
						
							for (Employee employee : employeeList) {
								
								if (employee.getCode().equals(employeecode)) {
									
									employee1 = employee;
									break;
								}
							}

							if (employee1 != null && isAllemployeeExist.equals(true) && row.getCell(3) != null
									&& (Double) row.getCell(3).getNumericCellValue() != 0) {
								details.setCode(employee1.getCode());
								details.setName(employee1.getName());
								details.setPan(employee1.getPan());

								int wday=(int) row.getCell(3).getNumericCellValue();
								if(wday < 32 && wday > 0)
								{
									details.setTotaldays((int) row.getCell(3).getNumericCellValue());
								}
								else {
									ErrorMsgs.append("Total working days is blank or 0 ");
									isAllemployeeExist = false;
									employeeCode = employeecode;
								}
								
								if (row.getCell(4) != null) {
									details.setBasicSalary((Double) row.getCell(4).getNumericCellValue());
									//totalBasicSalary = totalBasicSalary + (Double) row.getCell(4).getNumericCellValue();
									totalBasicSalary = totalBasicSalary + Double.valueOf(twoDForm.format(row.getCell(4).getNumericCellValue()));
									System.out.println("total sal is "+totalBasicSalary);
								} else {
									details.setBasicSalary((double) 0);
								}
								if (row.getCell(5) != null) {
									details.setDA((Double) row.getCell(5).getNumericCellValue());
									//totalDA = totalDA + (Double) row.getCell(5).getNumericCellValue();
									totalDA = totalDA + Double.valueOf(twoDForm.format(row.getCell(5).getNumericCellValue()));
								} else {
									details.setDA((double) 0);
								}
								if (row.getCell(6) != null) {
									details.setConveyanceAllowance((Double) row.getCell(6).getNumericCellValue());
									//totalconveyanceAllowance = totalconveyanceAllowance
									//		+ (Double) row.getCell(6).getNumericCellValue();
									totalconveyanceAllowance = totalconveyanceAllowance
													+ Double.valueOf(twoDForm.format( row.getCell(6).getNumericCellValue()));
								} else {
									details.setConveyanceAllowance((double) 0);
								}

								if (row.getCell(7) != null) {
									details.setOtherAllowances((Double) row.getCell(7).getNumericCellValue());
								//	totalotherAllowances = totalotherAllowances
									//		+ (Double) row.getCell(7).getNumericCellValue();
									totalotherAllowances = totalotherAllowances
											+ Double.valueOf(twoDForm.format( row.getCell(7).getNumericCellValue()));
								} else {
									details.setOtherAllowances((double) 0);
								}

								if (row.getCell(8) != null) {
									details.setGrossSalary((Double) row.getCell(8).getNumericCellValue());
									//totalgrossSalary = totalgrossSalary + (Double) row.getCell(8).getNumericCellValue();
									totalgrossSalary = totalgrossSalary + Double.valueOf(twoDForm.format(row.getCell(8).getNumericCellValue()));
								//	Double.valueOf(twoDForm.format(salaryandWagesDrAmount));
								} else {
									details.setGrossSalary((double) 0);
								}
								if (row.getCell(9) != null) {
									details.setPfEmployeeContribution((Double) row.getCell(9).getNumericCellValue());
								//	totalpfEmployeeContribution = totalpfEmployeeContribution
									//		+ (Double) row.getCell(9).getNumericCellValue();
									totalpfEmployeeContribution = totalpfEmployeeContribution
											+  Double.valueOf(twoDForm.format( row.getCell(9).getNumericCellValue()));
								} else {
									details.setPfEmployeeContribution((double) 0);
								}
								if (row.getCell(10) != null) {
									details.seteSICEmployeeContribution((Double) row.getCell(10).getNumericCellValue());
									//totalESICEmployeeContribution = totalESICEmployeeContribution
									//		+ (Double) row.getCell(10).getNumericCellValue();
									
									totalESICEmployeeContribution = totalESICEmployeeContribution
											+ Double.valueOf(twoDForm.format( row.getCell(10).getNumericCellValue()));
								} else {
									details.seteSICEmployeeContribution((double) 0);
								}
								if (row.getCell(11) != null) {
									details.setProfessionTax((Double) row.getCell(11).getNumericCellValue());
								//	totalprofessionTax = totalprofessionTax
									//		+ (Double) row.getCell(11).getNumericCellValue();
									
									totalprofessionTax = totalprofessionTax
											+ Double.valueOf(twoDForm.format( row.getCell(11).getNumericCellValue()));
								} else {
									details.setProfessionTax((double) 0);
								}
								if (row.getCell(12) != null) {
									details.setlWF((Double) row.getCell(12).getNumericCellValue());
									//totalLWF = totalLWF + (Double) row.getCell(12).getNumericCellValue();
									
									totalLWF = totalLWF + Double.valueOf(twoDForm.format( row.getCell(12).getNumericCellValue()));
								} else {
									details.setlWF((double) 0);
								}

								if (row.getCell(13) != null) {
									details.settDS((Double) row.getCell(13).getNumericCellValue());
									//totalTDS = totalTDS + (Double) row.getCell(13).getNumericCellValue();
									totalTDS = totalTDS +  Double.valueOf(twoDForm.format( row.getCell(13).getNumericCellValue()));
								} else {
									details.settDS((double) 0);
								}
								if (row.getCell(14) != null) {
									details.setOtherDeductions((Double) row.getCell(14).getNumericCellValue());
									//totalotherDeductions = totalotherDeductions
									//		+ (Double) row.getCell(14).getNumericCellValue();
									
									totalotherDeductions = totalotherDeductions
											+  Double.valueOf(twoDForm.format( row.getCell(14).getNumericCellValue()));
								} else {
									details.setOtherDeductions((double) 0);
								}
								if (row.getCell(15) != null) {
									details.setAdvanceAdjustment((Double) row.getCell(15).getNumericCellValue());
								//	totaladvanceAdjustment = totaladvanceAdjustment
									//		+ (Double) row.getCell(15).getNumericCellValue();
									
									totaladvanceAdjustment = totaladvanceAdjustment
											+ Double.valueOf(twoDForm.format( row.getCell(15).getNumericCellValue()));
								} else {
									details.setAdvanceAdjustment((double) 0);
								}

								if (row.getCell(16) != null) {
									details.setTotalDeductions((Double) row.getCell(16).getNumericCellValue());
									//allTotalDeductions = allTotalDeductions
									//		+ (Double) row.getCell(16).getNumericCellValue();
									
									allTotalDeductions = allTotalDeductions
											+ Double.valueOf(twoDForm.format( row.getCell(16).getNumericCellValue()));
								} else {
									details.setTotalDeductions((double) 0);
								}
								if (row.getCell(17) != null) {
									details.setNetSalary((Double) row.getCell(17).getNumericCellValue());
									//totalnetSalary = totalnetSalary + (Double) row.getCell(17).getNumericCellValue();
									totalnetSalary = totalnetSalary + Double.valueOf(twoDForm.format( row.getCell(17).getNumericCellValue()));
								} else {
									details.setNetSalary((double) 0);
								}
								if (row.getCell(18) != null) {
									details.setPfEmployerContribution((Double) row.getCell(18).getNumericCellValue());
									//totalpfEmployerContribution = totalpfEmployerContribution
									//		+ (Double) row.getCell(18).getNumericCellValue();
									
									totalpfEmployerContribution = totalpfEmployerContribution
											+ Double.valueOf(twoDForm.format( row.getCell(18).getNumericCellValue()));
								} else {
									details.setPfEmployerContribution((double) 0);
								}
								if (row.getCell(19) != null) {
									details.seteSICEmployerContribution((Double) row.getCell(19).getNumericCellValue());
									//totalESICEmployerContribution = totalESICEmployerContribution
									//		+ (Double) row.getCell(19).getNumericCellValue();
									
									totalESICEmployerContribution = totalESICEmployerContribution
											+ Double.valueOf(twoDForm.format( row.getCell(19).getNumericCellValue()));
								} else {
									details.seteSICEmployerContribution((double) 0);
								}
								if (row.getCell(20) != null) {
									details.setpFAdminCharges((Double) row.getCell(20).getNumericCellValue());
									//totalPFAdminCharges = totalPFAdminCharges
									//		+ (Double) row.getCell(20).getNumericCellValue();
									
									totalPFAdminCharges = totalPFAdminCharges
											+ Double.valueOf(twoDForm.format( row.getCell(20).getNumericCellValue()));
								} else {
									details.setpFAdminCharges((double) 0);
								}
								details.setPayrollAutoJV(entry);
								payrollEmployeeDetailsList.add(details);
							} 
							else {
								ErrorMsgs.append("Either employee not correct or Total working days blank ");
								isAllemployeeExist = false;
								employeeCode = employeecode;
							}

						}

					}
				//	DecimalFormat twoDForm = new DecimalFormat("#.##");
					salaryandWagesDrAmount = totalgrossSalary;
					System.out.println("salaryandWagesDrAmount "+salaryandWagesDrAmount);
					salaryandWagesDrAmount=Double.valueOf(twoDForm.format(salaryandWagesDrAmount));
					System.out.println("formatted "+salaryandWagesDrAmount);
					ePFPayableCrAmount = totalpfEmployeeContribution + totalpfEmployerContribution
							+ totalPFAdminCharges;
					ePFPayableCrAmount=Double.valueOf(twoDForm.format(ePFPayableCrAmount));
					eSICPayableCrAmount = totalESICEmployeeContribution + totalESICEmployerContribution;
					eSICPayableCrAmount=Double.valueOf(twoDForm.format(eSICPayableCrAmount));
					pTPayableCrAmount = totalprofessionTax;
					pTPayableCrAmount=Double.valueOf(twoDForm.format(pTPayableCrAmount));
					lWFPayableCrAmount = totalLWF;
					lWFPayableCrAmount=Double.valueOf(twoDForm.format(lWFPayableCrAmount));
					tDS192BPayableCrAmount = totalTDS;
					tDS192BPayableCrAmount=Double.valueOf(twoDForm.format(tDS192BPayableCrAmount));
					salaryPayableCrAmount = totalnetSalary;
					salaryPayableCrAmount=Double.valueOf(twoDForm.format(salaryPayableCrAmount));
					providentFundDrAmount = totalpfEmployerContribution;
					providentFundDrAmount=Double.valueOf(twoDForm.format(providentFundDrAmount));
					administrativeExpenseCrAmount = totalPFAdminCharges;
					administrativeExpenseCrAmount=Double.valueOf(twoDForm.format(administrativeExpenseCrAmount));
					eSICDrAmount = totalESICEmployerContribution;
					eSICDrAmount=Double.valueOf(twoDForm.format(eSICDrAmount));
					otherSalaryDeductionsCramount=totalotherDeductions;
					otherSalaryDeductionsCramount=Double.valueOf(twoDForm.format(otherSalaryDeductionsCramount));
							salaryAdvanceCramount=totaladvanceAdjustment;
							salaryAdvanceCramount=Double.valueOf(twoDForm.format(salaryAdvanceCramount));

					totalCramount = ePFPayableCrAmount + eSICPayableCrAmount + pTPayableCrAmount + lWFPayableCrAmount
							+ tDS192BPayableCrAmount + salaryPayableCrAmount+otherSalaryDeductionsCramount+salaryAdvanceCramount;
					totalDramount = salaryandWagesDrAmount + providentFundDrAmount + eSICDrAmount
							+ administrativeExpenseCrAmount;
System.out.println("Tota cred "+totalCramount);
System.out.println("Tota debit "+totalDramount);
					Set<PayrollSubledgerDetails> payrollSubledgerDetailsList = new HashSet<PayrollSubledgerDetails>();
					PayrollSubledgerDetails subDetails1 = new PayrollSubledgerDetails();
					subDetails1.setSubledgerName("Salary and Wages");
					subDetails1.setDrAmount(salaryandWagesDrAmount);
					subDetails1.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails1);

					PayrollSubledgerDetails subDetails2 = new PayrollSubledgerDetails();
					subDetails2.setSubledgerName("PF Payable");
					subDetails2.setCrAmount(ePFPayableCrAmount);
					subDetails2.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails2);

					PayrollSubledgerDetails subDetails3 = new PayrollSubledgerDetails();
					subDetails3.setSubledgerName("ESIC Payable");
					subDetails3.setCrAmount(eSICPayableCrAmount);
					subDetails3.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails3);

					
					PayrollSubledgerDetails subDetails4 = new PayrollSubledgerDetails();
					subDetails4.setSubledgerName("PT Payable");
					subDetails4.setCrAmount(pTPayableCrAmount);
					subDetails4.setPayrollAutoJV(entry);	
					payrollSubledgerDetailsList.add(subDetails4);

					PayrollSubledgerDetails subDetails5 = new PayrollSubledgerDetails();
					subDetails5.setSubledgerName("LWF Payable");
					subDetails5.setCrAmount(lWFPayableCrAmount);
					subDetails5.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails5);

					PayrollSubledgerDetails subDetails6 = new PayrollSubledgerDetails();
					subDetails6.setSubledgerName("TDS 192B Payable");
					subDetails6.setCrAmount(tDS192BPayableCrAmount);
					subDetails6.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails6);

					PayrollSubledgerDetails subDetails7 = new PayrollSubledgerDetails();
					subDetails7.setSubledgerName("Salary Payable");
					subDetails7.setCrAmount(salaryPayableCrAmount);
					subDetails7.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails7);

					PayrollSubledgerDetails subDetails8 = new PayrollSubledgerDetails();
					subDetails8.setSubledgerName("Provident Fund");
					subDetails8.setDrAmount(providentFundDrAmount);
					subDetails8.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails8);

					PayrollSubledgerDetails subDetails9 = new PayrollSubledgerDetails();
					subDetails9.setSubledgerName("PF administrative charges");
					subDetails9.setDrAmount(administrativeExpenseCrAmount);
					subDetails9.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails9);

					PayrollSubledgerDetails subDetails10 = new PayrollSubledgerDetails();
					subDetails10.setSubledgerName("ESIC");
					subDetails10.setDrAmount(eSICDrAmount);
					subDetails10.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails10);
					
					PayrollSubledgerDetails subDetails11 = new PayrollSubledgerDetails();
					subDetails11.setSubledgerName("Other Salary Deductions");
					subDetails11.setCrAmount(otherSalaryDeductionsCramount);
					subDetails11.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails11);

					PayrollSubledgerDetails subDetails12 = new PayrollSubledgerDetails();
					subDetails12.setSubledgerName("Salary Advance");
					subDetails12.setCrAmount(salaryAdvanceCramount);
					subDetails12.setPayrollAutoJV(entry);
					payrollSubledgerDetailsList.add(subDetails12);

					
					entry.setPayrollEmployeeDetails(payrollEmployeeDetailsList);
					entry.setPayrollSubledgerDetails(payrollSubledgerDetailsList);
					try {
						
						String filePath = request.getServletContext().getRealPath("resources");
						filePath += "/templates/ErrorFile.txt";
						String home = System.getProperty("user.home");
						home =home +"\\Downloads\\" + "ErrorFile.txt";
						//File file = new File(home, "mytext.txt");
						//File file = new File(   "D:\\program.txt");  
						System.out.println("File Path is "+filePath);
						File file = new File(   filePath); 
						System.out.println("Home is " +home);
						 
						 FileWriter fw = new FileWriter(file.getAbsoluteFile());
					        BufferedWriter bw = new BufferedWriter(fw);
					        bw.write(ErrorMsgs.toString());
					        bw.close();
					        System.out.println("done");
						/*if(file.exists()){file.delete();}
						
					        System.out.println("creating file");
					        
					        if(file.createNewFile()) {
					        	output = new BufferedWriter(new FileWriter(file));
					            output.write(ErrorMsgs.toString());
					        System.out.println("Succesfully created file");
					        } else{
					        System.out.println("Failed to create file");
					        }*/
					        
			        } catch ( Exception e ) {
			            e.printStackTrace();
			        } finally {
			          if ( output != null ) {
			            output.close();
			          }
			        }
					if (isAllemployeeExist.equals(false)) {
						successmsg.append(
								"Please enter valid employee data you are entering wrong employee data " + employeeCode);
						String successmsg1 = successmsg.toString();
						session.setAttribute("filemsg", successmsg1);
						
					}else if(isWorkingDay.equals(false))
					{
						successmsg.append("Please Enter Correct Working Days of Month");
						String successmsg1 = successmsg.toString();
						session.setAttribute("filemsg", successmsg1);
					}
					else if (isAllemployeeExist.equals(true) && !totalCramount.equals(totalDramount)) {
						System.out.println("Error added");
						successmsg.append("Total Cr Amount is not equal to Dr amount");
						ErrorMsgs.append("Total Cr Amount is not equal to Dr amount ");
						String successmsg1 = successmsg.toString();
						session.setAttribute("filemsg", successmsg1);
					}
					else if (isAllemployeeExist.equals(true) && totalCramount.equals(totalDramount)) {
						System.out.println("Created Sucessfully");
						successmsg.append("Payroll Auto JV Created Sucessfully");
						String successmsg1 = successmsg.toString();
						session.setAttribute("filemsg", successmsg1);
						session.setAttribute("payroll", entry);
					}
					workbook.close();
				}

			} else {
				System.out.println("no data");

			}
		} catch (Exception e) {
			e.printStackTrace();
			isValid = false;
		}

		return new ModelAndView("redirect:/payrollImport");
	}

	@RequestMapping(value = "deletePayroll", method = RequestMethod.GET)
	public ModelAndView deletePayroll(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		String msg = payroll_service.deleteByIdValue(id);
		HttpSession session = request.getSession(true);

		session.setAttribute("filemsg", msg);
		return new ModelAndView("redirect:/payrollAutoJVList");
	}

	@RequestMapping(value = "editpayrollAutoJV", method = RequestMethod.GET)
	public ModelAndView editpayrollAutoJV(@RequestParam(name="id",required = false) Long id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		ModelAndView model = new ModelAndView();
		List<PayrollEmployeeDetails> payrollemployeedetail = new ArrayList<PayrollEmployeeDetails>();
		List<PayrollSubledgerDetails> payrollSubledgerDetails = new ArrayList<PayrollSubledgerDetails>();
		PayrollAutoJV payrollAutoJV = new PayrollAutoJV();
		long company_id = user.getCompany().getCompany_id();

		if(id!=null && id!=0)
		{
		payroll_id = id;
		if (id > 0) {
			try {
				payrollAutoJV = payroll_service.findOneWithAll(payroll_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (payrollAutoJV.getAccounting_year_id() != null) {
				payrollAutoJV.setAccountYearId(payrollAutoJV.getAccounting_year_id().getYear_id());
			}

		}

		if ((String) session.getAttribute("msg") != null) {
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}

		for (PayrollEmployeeDetails detail : payroll_service.findAllPayrollEmployeeList(id)) {
			payrollemployeedetail.add(detail);
		}
		model.addObject("employeeList", employeeService.findAllEmployeeReletedToCompany(company_id));
		model.addObject("payrollemployeedetail", payrollemployeedetail);
		model.addObject("payrollAutoJV", payrollAutoJV);
		// model.addObject("payrollEmployeeList",
		// payroll_service.findAllPayrollEmployeeList(id));
		}
		else
		{
			payrollAutoJV  = (PayrollAutoJV) session.getAttribute("payroll");
			if ((String) session.getAttribute("msg") != null) {
				model.addObject("successMsg", (String) session.getAttribute("msg"));
				session.removeAttribute("msg");
			}
			for (PayrollEmployeeDetails detail : payrollAutoJV.getPayrollEmployeeDetails()) {
				payrollemployeedetail.add(detail);
			}
			model.addObject("employeeList", employeeService.findAllEmployeeReletedToCompany(company_id));
			model.addObject("payrollemployeedetail", payrollemployeedetail);
			model.addObject("payrollAutoJV", payrollAutoJV);
		}
		model.setViewName("/transactions/editpayrollAutoJV");
		return model;

	}

	@RequestMapping(value = "editEmployeeForPayroll", method = RequestMethod.GET)
	public ModelAndView editEmployeeForPayroll(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		ModelAndView model = new ModelAndView();
		PayrollEmployeeDetails entry = new PayrollEmployeeDetails();
		PayrollAutoJV payrollEntry = new PayrollAutoJV();
		User user = new User();
		user = (User) session.getAttribute("user");
		entry = payroll_service.editEmployeeForPayroll(id);

		payrollEntry= payroll_service.findOneWithAll(entry.getPayrollAutoJV().getPayroll_id());
	//	payrollEntry.setEmployeeDetails(entry.getPayrollAutoJV().getEmployeeDetails());
		model.addObject("entry", entry);
		model.addObject("payrollEntry", payrollEntry);
		model.setViewName("/transactions/editEmployeeForPayroll");
		return model;
	}

	@RequestMapping(value = "saveEmployeeForPayroll", method = RequestMethod.POST)
	public synchronized ModelAndView saveEmployeeForPayroll(@ModelAttribute("entry") PayrollEmployeeDetails entry,BindingResult result, 
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
	    Double pfEmployeeContribution = (double)0;
	    Double eSICEmployeeContribution = (double)0;
	    Double professionTax = (double)0;
	    Double lWF = (double)0;
	    Double tDS = (double)0;
	    Double otherDeductions = (double)0;
	    Double advanceAdjustment = (double)0;
	    Double pfEmployerContribution = (double)0;
	    Double eSICEmployerContribution = (double)0;
	    Double pFAdminCharges = (double)0;
	    Double pfEmployeeContributionnew = (double)0;
	    Double eSICEmployeeContributionnew = (double)0;
	    Double professionTaxnew = (double)0;
	    Double lWFnew = (double)0;
	    Double tDSnew = (double)0;
	    Double otherDeductionsnew = (double)0;
	    Double advanceAdjustmentnew = (double)0;
	    Double pfEmployerContributionnew = (double)0;
	    Double eSICEmployerContributionnew = (double)0;
	    Double pFAdminChargesnew = (double)0;




		PayrollEmployeeDetails payEmpDetails = new PayrollEmployeeDetails();
		payEmpDetails = payroll_service.editEmployeeForPayroll(entry.getEmployeeDetails_id());
		PayrollAutoJV payrollAutoJV = new PayrollAutoJV();
		try {
			payrollAutoJV = payroll_service.findOneWithAll(payroll_id);
			
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		
		if(payEmpDetails.getPfEmployeeContribution()!=null)
		{
			pfEmployeeContribution= payEmpDetails.getPfEmployeeContribution();
		}
		if(payEmpDetails.geteSICEmployeeContribution()!=null)
		{
			eSICEmployeeContribution= payEmpDetails.geteSICEmployeeContribution();
		}
				
		if(payEmpDetails.getProfessionTax()!=null)
		{
			professionTax= payEmpDetails.getProfessionTax();
		}
		
		if(payEmpDetails.getlWF()!=null)
		{
			lWF= payEmpDetails.getlWF();
		}
		if(payEmpDetails.gettDS()!=null)
		{
			tDS= payEmpDetails.gettDS();
		}
		if(payEmpDetails.getOtherDeductions()!=null)
		{
			otherDeductions= payEmpDetails.getOtherDeductions();
		}
		if(payEmpDetails.getAdvanceAdjustment()!=null)
		{
			advanceAdjustment= payEmpDetails.getAdvanceAdjustment();
		}
		if(payEmpDetails.getPfEmployerContribution()!=null)
		{
			pfEmployerContribution= payEmpDetails.getPfEmployerContribution();
		}
		
		if(payEmpDetails.geteSICEmployerContribution()!=null)
		{
			eSICEmployerContribution= payEmpDetails.geteSICEmployerContribution();
		}
		
		if(payEmpDetails.getpFAdminCharges()!=null)
		{
			pFAdminCharges= payEmpDetails.getpFAdminCharges();
		}
		return null;
		
		//payroll_service.updateSalesEntry(pa);
		

	}

	@RequestMapping(value = "payrollImport", method = RequestMethod.GET)
	public ModelAndView payrollImport(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);

		if ((String) session.getAttribute("filemsg") != null) {
			String msg = (String) session.getAttribute("filemsg");

			if (!msg.equals("Payroll Auto JV Created Sucessfully")) {
				session.removeAttribute("payroll");
				model.addObject("success", 0);

			} else {
				PayrollAutoJV autojv = (PayrollAutoJV) session.getAttribute("payroll");
				session.removeAttribute("filemsg");
				model.addObject("payroll", autojv);
				model.addObject("success", 1);
			}

		}
		model.setViewName("/transactions/payrollImport");
		return model;
	}

	@RequestMapping(value = "savePayrollImport", method = RequestMethod.GET)
	public ModelAndView savePayrollImport(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		PayrollAutoJV autojv = (PayrollAutoJV) session.getAttribute("payroll");

		if(autojv.getPayroll_id()==null)
		{
			VoucherSeries series = new VoucherSeries();
			series.setVoucher_no(commonService.getVoucherNumberForAUtoJV(autojv.getDate(), autojv.getCompany().getCompany_id()));
			series.setVoucher_date(autojv.getDate());
			series.setCompany(autojv.getCompany());
			autojv.setVoucherSeries(series);
			
		Long id = dao.savePayrollAutoJv(autojv);
		try {
			//PayrollAutoJV entry = dao.findOne(id);
			PayrollAutoJV entry= (PayrollAutoJV) session.getAttribute("payroll");
			Set<PayrollSubledgerDetails> payrollSubledgerDetailsList = entry.getPayrollSubledgerDetails();

			for (PayrollSubledgerDetails details : payrollSubledgerDetailsList) {
				if (details.getSubledgerName().equals("Salary and Wages")) {
					SubLedger salryAndWages = subLedgerDAO.findOne("Salary and Wages",
							autojv.getCompany().getCompany_id());

//					try {
//						salryAndWages = subLedgerDAO.findOne(salryAndWages.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (salryAndWages != null) {
						subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), salryAndWages.getSubledger_Id(),
								(long) 2, (double) 0, details.getDrAmount(), (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}

				if (details.getSubledgerName().equals("ESIC")) {
					SubLedger esic = subLedgerDAO.findOne("ESIC", autojv.getCompany().getCompany_id());
//
//					try {
//						esic = subLedgerDAO.findOne(esic.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (esic != null) {
						subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), esic.getSubledger_Id(), (long) 2,
								(double) 0, details.getDrAmount(), (long) 1, null, null, null, null, null, null, null,
								null, entry, null, null,null);
					}
				}

				if (details.getSubledgerName().equals("PF Payable")) {
					SubLedger epfPayable = subLedgerDAO.findOne("PF Payable", autojv.getCompany().getCompany_id());

//					try {
//						epfPayable = subLedgerDAO.findOne(epfPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (epfPayable != null) {
						subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), epfPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("ESIC Payable")) {
					SubLedger esicPayable = subLedgerDAO.findOne("ESIC Payable", autojv.getCompany().getCompany_id());

					if (esicPayable != null) {
//						try {
//							esicPayable = subLedgerDAO.findOne(esicPayable.getSubledger_Id());
//						} catch (MyWebException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						if (esicPayable != null) {
							subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
									entry.getDate(), autojv.getCompany().getCompany_id(), esicPayable.getSubledger_Id(),
									(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
									null, null, null, entry, null, null,null);
						}

					}
				}
				if (details.getSubledgerName().equals("PT Payable")) {
					SubLedger ptPayable = subLedgerDAO.findOne("PT Payable", autojv.getCompany().getCompany_id());

//					try {
//						ptPayable = subLedgerDAO.findOne(ptPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (ptPayable != null) {
						subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), ptPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("Other Salary Deductions")) {
					SubLedger otherSalaryDeductions = subLedgerDAO.findOne("Other Salary Deductions", autojv.getCompany().getCompany_id());

					if (otherSalaryDeductions != null) {
//						try {
//							esicPayable = subLedgerDAO.findOne(esicPayable.getSubledger_Id());
//						} catch (MyWebException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						if (otherSalaryDeductions != null) {
							subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
									entry.getDate(), autojv.getCompany().getCompany_id(), otherSalaryDeductions.getSubledger_Id(),
									(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
									null, null, null, entry, null, null,null);
						}

					}
				}
				if (details.getSubledgerName().equals("Salary Advance")) {
					SubLedger salaryAdvance = subLedgerDAO.findOne("Salary Advance", autojv.getCompany().getCompany_id());

					if (salaryAdvance != null) {
//						try {
//							esicPayable = subLedgerDAO.findOne(esicPayable.getSubledger_Id());
//						} catch (MyWebException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						if (salaryAdvance != null) {
							subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
									entry.getDate(), autojv.getCompany().getCompany_id(), salaryAdvance.getSubledger_Id(),
									(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
									null, null, null, entry, null, null,null);
						}

					}
				}
				if (details.getSubledgerName().equals("LWF Payable")) {
					SubLedger lwfPayable = subLedgerDAO.findOne("LWF Payable", autojv.getCompany().getCompany_id());
//
//					try {
//						lwfPayable = subLedgerDAO.findOne(lwfPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (lwfPayable != null) {
						subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), lwfPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("TDS 192B Payable")) {
					SubLedger tds192btPayable = subLedgerDAO.findOne("TDS 192B Payable",
							autojv.getCompany().getCompany_id());
//
//					try {
//						tds192btPayable = subLedgerDAO.findOne(tds192btPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (tds192btPayable != null) {
						subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), tds192btPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("Salary Payable")) {
					SubLedger salaryPayable = subLedgerDAO.findOne("Salary Payable",
							autojv.getCompany().getCompany_id());
//
//					try {
//						salaryPayable = subLedgerDAO.findOne(salaryPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (salaryPayable != null) {
						subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), salaryPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("Provident Fund")) {
					SubLedger providentfund = subLedgerDAO.findOne("Provident Fund",
							autojv.getCompany().getCompany_id());

//					try {
//						providentfund = subLedgerDAO.findOne(providentfund.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (providentfund != null) {
						subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), providentfund.getSubledger_Id(),
								(long) 2, (double) 0, details.getDrAmount(), (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("PF administrative charges")) {
					SubLedger administrativeexpense = subLedgerDAO.findOne("PF administrative charges",
							autojv.getCompany().getCompany_id());

//					try {
//						administrativeexpense = subLedgerDAO.findOne(administrativeexpense.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (administrativeexpense != null) {
						subledgerService.addsubledgertransactionbalance(autojv.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(),
								administrativeexpense.getSubledger_Id(), (long) 2, (double) 0, details.getDrAmount(),
								(long) 1, null, null, null, null, null, null, null, null, entry, null, null,null);
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.setAttribute("filemsg", "Payroll Auto JV Created Sucessfully");
	}
		else
		{
			PayrollAutoJV entry = null;
			try {
				entry = dao.findOne(autojv.getPayroll_id());
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			entry.setOther_remark(autojv.getOther_remark());
			//delete details by payrollid
		
			/*
			 * for(PayrollEmployeeDetails details: oldpayrollEmployeeDetails) {
			 * details.getEmployee(). }
			 */
			Set<PayrollSubledgerDetails> oldpayrollSubledgerDetailsList = entry.getPayrollSubledgerDetails();
			
			for (PayrollSubledgerDetails details : oldpayrollSubledgerDetailsList) {
				if (details.getSubledgerName().equals("Salary and Wages")) {
					SubLedger salryAndWages = subLedgerDAO.findOne("Salary and Wages",
							autojv.getCompany().getCompany_id());

//					try {
//						salryAndWages = subLedgerDAO.findOne(salryAndWages.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (salryAndWages != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), salryAndWages.getSubledger_Id(),
								(long) 2, (double) 0, details.getDrAmount(), (long) 0, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}

				if (details.getSubledgerName().equals("ESIC")) {
					SubLedger esic = subLedgerDAO.findOne("ESIC", autojv.getCompany().getCompany_id());
//
//					try {
//						esic = subLedgerDAO.findOne(esic.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (esic != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), esic.getSubledger_Id(), (long) 2,
								(double) 0, details.getDrAmount(), (long) 0, null, null, null, null, null, null, null,
								null, entry, null, null,null);
					}
				}

				if (details.getSubledgerName().equals("PF Payable")) {
					SubLedger epfPayable = subLedgerDAO.findOne("PF Payable", autojv.getCompany().getCompany_id());

//					try {
//						epfPayable = subLedgerDAO.findOne(epfPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (epfPayable != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), epfPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 0, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("ESIC Payable")) {
					SubLedger esicPayable = subLedgerDAO.findOne("ESIC Payable", autojv.getCompany().getCompany_id());

					if (esicPayable != null) {
//						try {
//							esicPayable = subLedgerDAO.findOne(esicPayable.getSubledger_Id());
//						} catch (MyWebException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						if (esicPayable != null) {
							subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
									entry.getDate(), autojv.getCompany().getCompany_id(), esicPayable.getSubledger_Id(),
									(long) 2, details.getCrAmount(), (double) 0, (long) 0, null, null, null, null, null,
									null, null, null, entry, null, null,null);
						}

					}
				}
				if (details.getSubledgerName().equals("Other Salary Deductions")) {
					SubLedger otherSalaryDeductions = subLedgerDAO.findOne("Other Salary Deductions", autojv.getCompany().getCompany_id());

					if (otherSalaryDeductions != null) {
//						try {
//							esicPayable = subLedgerDAO.findOne(esicPayable.getSubledger_Id());
//						} catch (MyWebException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						if (otherSalaryDeductions != null) {
							subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
									entry.getDate(), autojv.getCompany().getCompany_id(), otherSalaryDeductions.getSubledger_Id(),
									(long) 2, details.getCrAmount(), (double) 0, (long) 0, null, null, null, null, null,
									null, null, null, entry, null, null,null);
						}

					}
				}
				if (details.getSubledgerName().equals("Salary Advance")) {
					SubLedger salaryAdvance = subLedgerDAO.findOne("Salary Advance", autojv.getCompany().getCompany_id());

					if (salaryAdvance != null) {
//						try {
//							esicPayable = subLedgerDAO.findOne(esicPayable.getSubledger_Id());
//						} catch (MyWebException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						if (salaryAdvance != null) {
							subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
									entry.getDate(), autojv.getCompany().getCompany_id(), salaryAdvance.getSubledger_Id(),
									(long) 2, details.getCrAmount(), (double) 0, (long) 0, null, null, null, null, null,
									null, null, null, entry, null, null,null);
						}

					}
				}
				if (details.getSubledgerName().equals("PT Payable")) {
					SubLedger ptPayable = subLedgerDAO.findOne("PT Payable", autojv.getCompany().getCompany_id());

//					try {
//						ptPayable = subLedgerDAO.findOne(ptPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (ptPayable != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), ptPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 0, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("LWF Payable")) {
					SubLedger lwfPayable = subLedgerDAO.findOne("LWF Payable", autojv.getCompany().getCompany_id());
//
//					try {
//						lwfPayable = subLedgerDAO.findOne(lwfPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (lwfPayable != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), lwfPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 0, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("TDS 192B Payable")) {
					SubLedger tds192btPayable = subLedgerDAO.findOne("TDS 192B Payable",
							autojv.getCompany().getCompany_id());
//
//					try {
//						tds192btPayable = subLedgerDAO.findOne(tds192btPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (tds192btPayable != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), tds192btPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 0, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("Salary Payable")) {
					SubLedger salaryPayable = subLedgerDAO.findOne("Salary Payable",
							autojv.getCompany().getCompany_id());
//
//					try {
//						salaryPayable = subLedgerDAO.findOne(salaryPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (salaryPayable != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), salaryPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 0, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("Provident Fund")) {
					SubLedger providentfund = subLedgerDAO.findOne("Provident Fund",
							autojv.getCompany().getCompany_id());

//					try {
//						providentfund = subLedgerDAO.findOne(providentfund.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (providentfund != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(), providentfund.getSubledger_Id(),
								(long) 2, (double) 0, details.getDrAmount(), (long) 0, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("PF administrative charges")) {
					SubLedger administrativeexpense = subLedgerDAO.findOne("PF administrative charges",
							autojv.getCompany().getCompany_id());

//					try {
//						administrativeexpense = subLedgerDAO.findOne(administrativeexpense.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (administrativeexpense != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								entry.getDate(), autojv.getCompany().getCompany_id(),
								administrativeexpense.getSubledger_Id(), (long) 2, (double) 0, details.getDrAmount(),
								(long) 0, null, null, null, null, null, null, null, null, entry, null, null,null);
					}
				}
			}
			dao.deleteDetails(entry.getPayroll_id());	
			Set<PayrollSubledgerDetails> payrollSubledgerDetailsList = autojv.getPayrollSubledgerDetails();

			for (PayrollSubledgerDetails details : payrollSubledgerDetailsList) {
				if (details.getSubledgerName().equals("Salary and Wages")) {
					SubLedger salryAndWages = subLedgerDAO.findOne("Salary and Wages",
							autojv.getCompany().getCompany_id());

//					try {
//						salryAndWages = subLedgerDAO.findOne(salryAndWages.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (salryAndWages != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								autojv.getDate(), autojv.getCompany().getCompany_id(), salryAndWages.getSubledger_Id(),
								(long) 2, (double) 0, details.getDrAmount(), (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}

				if (details.getSubledgerName().equals("ESIC")) {
					SubLedger esic = subLedgerDAO.findOne("ESIC", autojv.getCompany().getCompany_id());
//
//					try {
//						esic = subLedgerDAO.findOne(esic.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (esic != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								autojv.getDate(), autojv.getCompany().getCompany_id(), esic.getSubledger_Id(), (long) 2,
								(double) 0, details.getDrAmount(), (long) 1, null, null, null, null, null, null, null,
								null, entry, null, null,null);
					}
				}

				if (details.getSubledgerName().equals("PF Payable")) {
					SubLedger epfPayable = subLedgerDAO.findOne("PF Payable", autojv.getCompany().getCompany_id());

//					try {
//						epfPayable = subLedgerDAO.findOne(epfPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (epfPayable != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								autojv.getDate(), autojv.getCompany().getCompany_id(), epfPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("ESIC Payable")) {
					SubLedger esicPayable = subLedgerDAO.findOne("ESIC Payable", autojv.getCompany().getCompany_id());

					if (esicPayable != null) {
//						try {
//							esicPayable = subLedgerDAO.findOne(esicPayable.getSubledger_Id());
//						} catch (MyWebException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						if (esicPayable != null) {
							subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
									autojv.getDate(), autojv.getCompany().getCompany_id(), esicPayable.getSubledger_Id(),
									(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
									null, null, null, entry, null, null,null);
						}

					}
				}
				if (details.getSubledgerName().equals("Other Salary Deductions")) {
					SubLedger otherSalaryDeductions = subLedgerDAO.findOne("Other Salary Deductions", autojv.getCompany().getCompany_id());

					if (otherSalaryDeductions != null) {
//						try {
//							esicPayable = subLedgerDAO.findOne(esicPayable.getSubledger_Id());
//						} catch (MyWebException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						if (otherSalaryDeductions != null) {
							subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
									entry.getDate(), autojv.getCompany().getCompany_id(), otherSalaryDeductions.getSubledger_Id(),
									(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
									null, null, null, entry, null, null,null);
						}

					}
				}
				if (details.getSubledgerName().equals("Salary Advance")) {
					SubLedger salaryAdvance = subLedgerDAO.findOne("Salary Advance", autojv.getCompany().getCompany_id());

					if (salaryAdvance != null) {
//						try {
//							esicPayable = subLedgerDAO.findOne(esicPayable.getSubledger_Id());
//						} catch (MyWebException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						if (salaryAdvance != null) {
							subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
									entry.getDate(), autojv.getCompany().getCompany_id(), salaryAdvance.getSubledger_Id(),
									(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
									null, null, null, entry, null, null,null);
						}

					}
				}
				if (details.getSubledgerName().equals("PT Payable")) {
					SubLedger ptPayable = subLedgerDAO.findOne("PT Payable", autojv.getCompany().getCompany_id());

//					try {
//						ptPayable = subLedgerDAO.findOne(ptPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (ptPayable != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								autojv.getDate(), autojv.getCompany().getCompany_id(), ptPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("LWF Payable")) {
					SubLedger lwfPayable = subLedgerDAO.findOne("LWF Payable", autojv.getCompany().getCompany_id());
//
//					try {
//						lwfPayable = subLedgerDAO.findOne(lwfPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (lwfPayable != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								autojv.getDate(), autojv.getCompany().getCompany_id(), lwfPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("TDS 192B Payable")) {
					SubLedger tds192btPayable = subLedgerDAO.findOne("TDS 192B Payable",
							autojv.getCompany().getCompany_id());
//
//					try {
//						tds192btPayable = subLedgerDAO.findOne(tds192btPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (tds192btPayable != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								autojv.getDate(), autojv.getCompany().getCompany_id(), tds192btPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("Salary Payable")) {
					SubLedger salaryPayable = subLedgerDAO.findOne("Salary Payable",
							autojv.getCompany().getCompany_id());
//
//					try {
//						salaryPayable = subLedgerDAO.findOne(salaryPayable.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (salaryPayable != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								autojv.getDate(), autojv.getCompany().getCompany_id(), salaryPayable.getSubledger_Id(),
								(long) 2, details.getCrAmount(), (double) 0, (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("Provident Fund")) {
					SubLedger providentfund = subLedgerDAO.findOne("Provident Fund",
							autojv.getCompany().getCompany_id());

//					try {
//						providentfund = subLedgerDAO.findOne(providentfund.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (providentfund != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								autojv.getDate(), autojv.getCompany().getCompany_id(), providentfund.getSubledger_Id(),
								(long) 2, (double) 0, details.getDrAmount(), (long) 1, null, null, null, null, null,
								null, null, null, entry, null, null,null);
					}
				}
				if (details.getSubledgerName().equals("PF administrative charges")) {
					SubLedger administrativeexpense = subLedgerDAO.findOne("PF administrative charges",
							autojv.getCompany().getCompany_id());

//					try {
//						administrativeexpense = subLedgerDAO.findOne(administrativeexpense.getSubledger_Id());
//					} catch (MyWebException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					if (administrativeexpense != null) {
						subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),
								autojv.getDate(), autojv.getCompany().getCompany_id(),
								administrativeexpense.getSubledger_Id(), (long) 2, (double) 0, details.getDrAmount(),
								(long) 1, null, null, null, null, null, null, null, null, entry, null, null,null);
					}
				}
			}
			entry.setDate(autojv.getDate());
			entry.setDays(autojv.getDays());
			entry.setPayrollEmployeeDetails(null);
			entry.setPayrollSubledgerDetails(null);
			PayrollAutoJV entry1 = null;
			try {
				payroll_service.update(entry);
				entry1 = dao.findOne(autojv.getPayroll_id());
				entry1.setPayrollEmployeeDetails(autojv.getPayrollEmployeeDetails());
				entry1.setPayrollSubledgerDetails(autojv.getPayrollSubledgerDetails());
				payroll_service.update(entry1);
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			session.setAttribute("filemsg", "Payroll Auto JV Updated Sucessfully");
		}
		
		session.removeAttribute("payroll");
		return new ModelAndView("redirect:/payrollAutoJVList");
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "viewPayroll", method = RequestMethod.GET)
	public ModelAndView viewPayroll(@RequestParam("id") long id) {
System.out.println("Thepayroll id is " +id);
		ModelAndView model = new ModelAndView();
		Double totalDR = 0.00;
		Double totalCR = 0.00;
		PayrollAutoJV payrollAutoJV = new PayrollAutoJV();

		try {
			payrollAutoJV = payroll_service.getById(id);

		} catch (MyWebException e) {

			e.printStackTrace();
		}
		model.addObject("payrollAutoJV", payrollAutoJV);
		model.addObject("payrollEmployeeList", payroll_service.findAllPayrollEmployeeList(id));
		model.setViewName("/transactions/payrollAutoJVView");
		return model;
	}

//downloadPayrollPdf

	@RequestMapping(value = "downloadPayrollPdf", method = RequestMethod.GET)
	public ModelAndView downloadPayrollPdf(@RequestParam("id") long id) {

		PayrollAutoJV entry = new PayrollAutoJV();
		try {
			entry = payroll_service.findOneView(id);
			// Company company =
			// companyService.getCompanyWithCompanyStautarType(entry.getCompany().getCompany_id());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("PayrollAutoJVPdfView", "payrollAutoJV", payroll_service.findOneView(id));
	}

	public static Double round(Double d, int decimalPlace) {
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(decimalPlace);
		return new Double(df.format(d));
	}

}