package com.fasset.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Company;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.entities.VoucherSeries;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.GSTAutoJVForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.PurchaseReportForm;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPayrollAutoJVService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IYearEndingService;

@Controller
@SessionAttributes("user")
public class GSTAutoJVController extends MyAbstractController {

	List<OpeningBalancesForm> bankOpeningBalanceBeforeStartDate = new ArrayList<OpeningBalancesForm>();

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IOpeningBalancesService openingBalancesService;

	@Autowired
	private ISubLedgerDAO subLedgerDAO;

	@Autowired
	private IAccountingYearService accountingYearService;

	@Autowired
	private ISubLedgerService subLedgerService;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IPayrollAutoJVService payroll_service;

	@Autowired
	private IYearEndingService yearService;

	private List<OpeningBalancesForm> subledgerList = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm> subledgerListbeforestartDate = new ArrayList<OpeningBalancesForm>();

	@RequestMapping(value = "gstAutoJvDateSelection", method = RequestMethod.GET)
	public ModelAndView gstAutoJvDateSelection(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		PurchaseReportForm form = new PurchaseReportForm();

		model.addObject("form", form);
		model.setViewName("/transactions/gstAutoJVDateSelection");
		return model;
	}

	@RequestMapping(value = "gstAutoJV", method = RequestMethod.POST)
	public ModelAndView gstAutoJV(@ModelAttribute("form") PurchaseReportForm gstform, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		long user_id = (long) session.getAttribute("user_id");
		long company_id = (long) session.getAttribute("company_id");

		Company company = null;
		company = companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());

		SubLedger outputcgst = subLedgerDAO.findOne("Output CGST", user.getCompany().getCompany_id());

		SubLedger outputsgst = subLedgerDAO.findOne("Output SGST", user.getCompany().getCompany_id());

		SubLedger outputigst = subLedgerDAO.findOne("Output IGST", user.getCompany().getCompany_id());

		SubLedger inputcgst = subLedgerDAO.findOne("Input CGST", user.getCompany().getCompany_id());

		SubLedger inputsgst = subLedgerDAO.findOne("Input SGST", user.getCompany().getCompany_id());

		SubLedger inputigst = subLedgerDAO.findOne("Input IGST", user.getCompany().getCompany_id());

		// SubLedger gstPayble = subLedgerDAO.findOne("GST Payable",
		// user.getCompany().getCompany_id());

		subledgerList.clear();
		subledgerListbeforestartDate.clear();
System.out.println("gstform.getFromDate() " +gstform.getFromDate());
System.out.println("company.getOpeningbalance_date() " +company.getOpeningbalance_date());
System.out.println("gstform.getToDate() " +gstform.getToDate());

		for (Object bal[] : openingBalancesService.findAllOPbalancesforSubledger(company.getOpeningbalance_date(),
				gstform.getFromDate(), company_id, false)) {
			System.out.println("findallopbalanforsubledger");
			if (bal[0] != null && bal[1] != null && bal[2] != null) {

				SubLedger sub = null;
				try {
					sub = subLedgerService.getById((long) bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (sub != null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) {
					if (sub.getSubledger_Id().equals(outputcgst.getSubledger_Id())
							|| sub.getSubledger_Id().equals(outputsgst.getSubledger_Id())
							|| sub.getSubledger_Id().equals(outputigst.getSubledger_Id())
							|| sub.getSubledger_Id().equals(inputcgst.getSubledger_Id())
							|| sub.getSubledger_Id().equals(inputsgst.getSubledger_Id())
							|| sub.getSubledger_Id().equals(inputigst.getSubledger_Id())) {
						OpeningBalancesForm form = new OpeningBalancesForm();
						form.setSubledgerName(sub.getSubledger_name());
						try {
							form.setSubledger(sub);
						} catch (Exception e) {
							e.printStackTrace();
						}

						form.setCredit_balance((double) bal[2]);
						form.setDebit_balance((double) bal[1]);
						subledgerListbeforestartDate.add(form);
					}

				}
			}
		}

		for (Object bal[] : openingBalancesService.findAllOPbalancesforSubledger(gstform.getFromDate(),
				gstform.getToDate(), company_id, true)) {
			if (bal[0] != null && bal[1] != null && bal[2] != null) {

				SubLedger sub = null;
				try {
					sub = subLedgerService.getById((long) bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (sub != null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) {
					if (sub.getSubledger_Id().equals(outputcgst.getSubledger_Id())
							|| sub.getSubledger_Id().equals(outputsgst.getSubledger_Id())
							|| sub.getSubledger_Id().equals(outputigst.getSubledger_Id())
							|| sub.getSubledger_Id().equals(inputcgst.getSubledger_Id())
							|| sub.getSubledger_Id().equals(inputsgst.getSubledger_Id())
							|| sub.getSubledger_Id().equals(inputigst.getSubledger_Id())) {
						OpeningBalancesForm form = new OpeningBalancesForm();
						form.setSubledgerName(sub.getSubledger_name());
						try {
							form.setSubledger(sub);
						} catch (Exception e) {
							e.printStackTrace();
						}

						form.setCredit_balance((double) bal[2]);
						form.setDebit_balance((double) bal[1]);
						subledgerList.add(form);
					}

				}
			}
		}
		GSTAutoJVForm form = new GSTAutoJVForm();

		Double outputCgstClsoingBalance = 0.0;
		Double outputSgstClsoingBalance = 0.0;
		Double outputIgstClsoingBalance = 0.0;
		Double inputCgstClsoingBalance = 0.0;
		Double inputSgstClsoingBalance = 0.0;
		Double inputIgstClsoingBalance = 0.0;

		for (OpeningBalancesForm openingform : subledgerListbeforestartDate) {
			System.out.println("openingform");
			if (form.getOutputCgst().equals(openingform.getSubledgerName())) {

				outputCgstClsoingBalance = outputCgstClsoingBalance + openingform.getCredit_balance()
						- openingform.getDebit_balance();
			}
			if (form.getOutputSgst().equals(openingform.getSubledgerName())) {

				outputSgstClsoingBalance = outputSgstClsoingBalance + openingform.getCredit_balance()
						- openingform.getDebit_balance();
			}
			if (form.getOutputIgst().equals(openingform.getSubledgerName())) {

				outputIgstClsoingBalance = outputIgstClsoingBalance + openingform.getCredit_balance()
						- openingform.getDebit_balance();
			}
			if (form.getInputCgst().equals(openingform.getSubledgerName())) {

				inputCgstClsoingBalance = inputCgstClsoingBalance + openingform.getDebit_balance()
						- openingform.getCredit_balance();
			}
			if (form.getInputSgst().equals(openingform.getSubledgerName())) {
				inputSgstClsoingBalance = inputSgstClsoingBalance + openingform.getDebit_balance()
						- openingform.getCredit_balance();

			}
			if (form.getInputIgst().equals(openingform.getSubledgerName())) {
				inputIgstClsoingBalance = inputIgstClsoingBalance + openingform.getDebit_balance()
						- openingform.getCredit_balance();

			}
		}

		for (OpeningBalancesForm openingform : subledgerList) {
			if (form.getOutputCgst().equals(openingform.getSubledgerName())) {
				System.out.println("1 subledgerList ");
				outputCgstClsoingBalance = outputCgstClsoingBalance + openingform.getCredit_balance()
						- openingform.getDebit_balance();

			}
			if (form.getOutputSgst().equals(openingform.getSubledgerName())) {
				System.out.println("2 subledgerList ");
				outputSgstClsoingBalance = outputSgstClsoingBalance + openingform.getCredit_balance()
						- openingform.getDebit_balance();
			}
			if (form.getOutputIgst().equals(openingform.getSubledgerName())) {
				System.out.println("3 subledgerList ");
				outputIgstClsoingBalance = outputIgstClsoingBalance + openingform.getCredit_balance()
						- openingform.getDebit_balance();
			}
			if (form.getInputCgst().equals(openingform.getSubledgerName())) {
				System.out.println("4 subledgerList ");
				inputCgstClsoingBalance = inputCgstClsoingBalance + openingform.getDebit_balance()
						- openingform.getCredit_balance();
			}
			if (form.getInputSgst().equals(openingform.getSubledgerName())) {
				System.out.println("5 subledgerList ");
				inputSgstClsoingBalance = inputSgstClsoingBalance + openingform.getDebit_balance()
						- openingform.getCredit_balance();

			}
			if (form.getInputIgst().equals(openingform.getSubledgerName())) {
				System.out.println("6 subledgerList ");
				inputIgstClsoingBalance = inputIgstClsoingBalance + openingform.getDebit_balance()
						- openingform.getCredit_balance();

			}
		}

		form.setOutputCgstClsoingBalance(outputCgstClsoingBalance);
		form.setOutputSgstClsoingBalance(outputSgstClsoingBalance);
		form.setOutputIgstClsoingBalance(outputIgstClsoingBalance);
		form.setInputCgstClsoingBalance(inputCgstClsoingBalance);
		form.setInputSgstClsoingBalance(inputSgstClsoingBalance);
		form.setInputIgstClsoingBalance(inputIgstClsoingBalance);

		double totalIgstCrBalance = 0.0;
		double totalCgstCrbalance = 0.0;
		double totalSgstCrBalance = 0.0;
		double totalIgstDrBalance = 0.0;
		double totalCgstDrBalance = 0.0;
		double totalSgstDrBalance = 0.0;

		List<AccountingYear> yearList = accountingYearService.findAccountRange(user_id, yearrange, company_id);
		AccountingYear year = yearList.get(0);
		GSTAutoJV autoJv = new GSTAutoJV();
		autoJv.setCreated_date(gstform.getTransactionDate());
		
		
		VoucherSeries series = new VoucherSeries();
		series.setVoucher_no(commonService.getVoucherNumberForAUtoJV(gstform.getTransactionDate(), user.getCompany().getCompany_id()));
		series.setVoucher_date(autoJv.getCreated_date());
		series.setCompany(user.getCompany());
		autoJv.setVoucherSeries(series);
	//	autoJv.setVoucher_no(commonService.getVoucherNumber("AutoJV", AutoJV, autoJv.getCreated_date(),autoJv.getAccountYearId(), user.getCompany().getCompany_id()));
		//autoJv.setVoucher_no(commonService.getVoucherNumberForAUtoJV("AutoJV", autoJv.getCreated_date(), autoJv.getAccountYearId(), user.getCompany().getCompany_id()));
		// autoJv.setVoucher_no(commonService.getVoucherNumber("GSTJV",
		// VOUCHER_GST_AUTOJV, new LocalDate(),year.getYear_id(),
		// user.getCompany().getCompany_id()));
		autoJv.setAccountingYear(year);
		autoJv.setCompany(user.getCompany());
		
		System.out.println("outputCgstClsoingBalance  " +outputCgstClsoingBalance);
		if ((outputCgstClsoingBalance + outputSgstClsoingBalance + outputIgstClsoingBalance <= 0)) {

			session.setAttribute("successMsg", "No GST to pay ");

			return new ModelAndView("redirect:/gstAutoJVList");
		} else {
			if (inputIgstClsoingBalance + inputSgstClsoingBalance + inputIgstClsoingBalance == 0)
			// if(outputCgstClsoingBalance+outputSgstClsoingBalance+outputIgstClsoingBalance==0)
			{

				session.setAttribute("successMsg", "Please pay GST using payment voucher.");

				return new ModelAndView("redirect:/gstAutoJVList");

			} else {
				autoJv.setBalanceCase(2);
				autoJv.setInitialOutputIgstBalance(outputIgstClsoingBalance);
				autoJv.setInitialOutputCgstbalance(outputCgstClsoingBalance);
				autoJv.setInitialOutputSgstBalance(outputSgstClsoingBalance);

				totalIgstDrBalance = outputIgstClsoingBalance;
				totalCgstDrBalance = outputCgstClsoingBalance;
				totalSgstDrBalance = outputSgstClsoingBalance;

				autoJv.setOutputIgstBalance((double) 0);
				autoJv.setOutputCgstbalance((double) 0);
				autoJv.setOutputSgstBalance((double) 0);

				autoJv.setInitialInputIgstBalance(inputIgstClsoingBalance);
				autoJv.setInitialInputCgstBalance(inputCgstClsoingBalance);
				autoJv.setInitialInputSgstBalance(inputSgstClsoingBalance);

				autoJv.setInputIgstBalance(inputIgstClsoingBalance);
				autoJv.setInputCgstBalance(inputCgstClsoingBalance);
				autoJv.setInputSgstBalance(inputSgstClsoingBalance);

				if (inputIgstClsoingBalance >= outputIgstClsoingBalance) {
					inputIgstClsoingBalance = inputIgstClsoingBalance - (outputIgstClsoingBalance);
					totalIgstCrBalance = totalIgstCrBalance + outputIgstClsoingBalance;
					autoJv.setInputIgstBalance(inputIgstClsoingBalance);
					// need to change this
					if (inputIgstClsoingBalance >= (outputCgstClsoingBalance + outputSgstClsoingBalance)) {
						inputIgstClsoingBalance = inputIgstClsoingBalance
								- (outputCgstClsoingBalance + outputSgstClsoingBalance);
						totalIgstCrBalance = totalIgstCrBalance + outputCgstClsoingBalance + outputSgstClsoingBalance;
						autoJv.setInputIgstBalance(inputIgstClsoingBalance);
						autoJv.setInputCgstBalance(inputCgstClsoingBalance);
						autoJv.setInputSgstBalance(inputSgstClsoingBalance);

					} else {
						inputIgstClsoingBalance = (inputIgstClsoingBalance / 2); // 2500
						outputCgstClsoingBalance = outputCgstClsoingBalance - inputIgstClsoingBalance;
						outputSgstClsoingBalance = outputSgstClsoingBalance - inputIgstClsoingBalance;
						inputIgstClsoingBalance = inputIgstClsoingBalance
								- (outputCgstClsoingBalance + outputSgstClsoingBalance);
						autoJv.setInputIgstBalance((double) 0);
						totalIgstCrBalance = autoJv.getInitialInputIgstBalance();

						if (inputCgstClsoingBalance >= outputCgstClsoingBalance) {
							inputCgstClsoingBalance = inputCgstClsoingBalance - outputCgstClsoingBalance;

							totalCgstCrbalance = totalCgstCrbalance + outputCgstClsoingBalance;

							autoJv.setInputCgstBalance(inputCgstClsoingBalance);

						} else {
							outputCgstClsoingBalance = outputCgstClsoingBalance - inputCgstClsoingBalance;
							totalCgstDrBalance = totalCgstDrBalance - outputCgstClsoingBalance;
							autoJv.setInputCgstBalance((double) 0);
							autoJv.setOutputCgstbalance((double) outputCgstClsoingBalance);
							// autoJv.setOutputSgstBalance((double)0);
							totalCgstCrbalance = autoJv.getInitialInputCgstBalance();
						}

						if (inputSgstClsoingBalance >= outputSgstClsoingBalance) {
							inputSgstClsoingBalance = inputSgstClsoingBalance - outputSgstClsoingBalance;
							totalSgstCrBalance = totalSgstCrBalance + outputSgstClsoingBalance;
							autoJv.setInputSgstBalance(inputSgstClsoingBalance);
						} else {
							outputSgstClsoingBalance = outputSgstClsoingBalance - inputSgstClsoingBalance;
							totalSgstDrBalance = totalSgstDrBalance - outputSgstClsoingBalance;
							autoJv.setInputSgstBalance((double) 0);
							autoJv.setOutputSgstBalance((double) outputSgstClsoingBalance);
							totalSgstCrBalance = autoJv.getInitialInputSgstBalance();
						}
						/*
						 * if(inputCgstClsoingBalance>=outputSgstClsoingBalance) {
						 * inputCgstClsoingBalance=inputCgstClsoingBalance-outputSgstClsoingBalance;
						 * totalCgstCrbalance=totalCgstCrbalance+outputSgstClsoingBalance;
						 * autoJv.setInputCgstBalance(inputCgstClsoingBalance);
						 * 
						 * }
						 */
						/*
						 * else {
						 * outputIgstClsoingBalance=outputIgstClsoingBalance-inputCgstClsoingBalance;
						 * autoJv.setInputCgstBalance((double)0);
						 * totalCgstCrbalance=autoJv.getInitialInputCgstBalance(); }
						 */

					}
					/*
					 * else {
					 * outputCgstClsoingBalance=outputCgstClsoingBalance-inputCgstClsoingBalance;
					 * autoJv.setInputCgstBalance((double)0);
					 * totalCgstCrbalance=autoJv.getInitialInputCgstBalance();
					 * if(inputSgstClsoingBalance>=outputCgstClsoingBalance) {
					 * inputSgstClsoingBalance=inputSgstClsoingBalance-outputCgstClsoingBalance;
					 * totalSgstCrBalance=totalSgstCrBalance+outputCgstClsoingBalance;
					 * autoJv.setInputSgstBalance(inputSgstClsoingBalance);
					 * if(inputSgstClsoingBalance>=outputSgstClsoingBalance) {
					 * inputSgstClsoingBalance=inputSgstClsoingBalance-outputSgstClsoingBalance;
					 * totalSgstCrBalance=totalSgstCrBalance+outputSgstClsoingBalance;
					 * autoJv.setInputSgstBalance(inputSgstClsoingBalance); } else {
					 * outputSgstClsoingBalance=outputSgstClsoingBalance-inputSgstClsoingBalance;
					 * autoJv.setInputSgstBalance((double)0);
					 * totalSgstCrBalance=autoJv.getInitialInputSgstBalance();
					 * autoJv.setGstPaybleBalance(outputSgstClsoingBalance); } } else {
					 * outputCgstClsoingBalance=outputCgstClsoingBalance-inputSgstClsoingBalance;
					 * autoJv.setInputSgstBalance((double)0);
					 * totalSgstCrBalance=autoJv.getInitialInputSgstBalance();
					 * autoJv.setGstPaybleBalance(outputCgstClsoingBalance+outputSgstClsoingBalance)
					 * ;
					 * 
					 * } }
					 */
				} else {
					outputIgstClsoingBalance = outputIgstClsoingBalance - inputIgstClsoingBalance;
					autoJv.setInputIgstBalance((double) 0);
					totalIgstDrBalance=totalIgstDrBalance-outputIgstClsoingBalance;
					
					//this is to show output balance of IGST
					autoJv.setOutputIgstBalance(outputIgstClsoingBalance);
					
					totalIgstCrBalance = autoJv.getInitialInputIgstBalance();
					if (inputCgstClsoingBalance >= outputCgstClsoingBalance) {
						inputCgstClsoingBalance = inputCgstClsoingBalance - outputCgstClsoingBalance;
						totalCgstCrbalance = totalCgstCrbalance + outputCgstClsoingBalance;
						autoJv.setInputCgstBalance(inputCgstClsoingBalance);
						
						
						if (inputSgstClsoingBalance >= outputSgstClsoingBalance) {
							inputSgstClsoingBalance = inputSgstClsoingBalance - outputSgstClsoingBalance;
							totalSgstCrBalance = totalSgstCrBalance + outputSgstClsoingBalance;
							autoJv.setInputSgstBalance(inputSgstClsoingBalance);
						}
						else
						{
							outputSgstClsoingBalance=outputSgstClsoingBalance-	inputSgstClsoingBalance  ;
							totalSgstDrBalance=totalSgstDrBalance+outputSgstClsoingBalance;
							autoJv.setOutputSgstBalance(outputSgstClsoingBalance);
							autoJv.setInputSgstBalance((double)0);
						}
						
						if (outputIgstClsoingBalance > 0) {
							totalIgstDrBalance=totalIgstDrBalance+outputIgstClsoingBalance;
							outputIgstClsoingBalance = (outputIgstClsoingBalance / 2);
							inputCgstClsoingBalance = inputCgstClsoingBalance - outputIgstClsoingBalance;
							totalCgstCrbalance = totalCgstCrbalance + outputIgstClsoingBalance;
							autoJv.setInputCgstBalance(inputCgstClsoingBalance);
							inputSgstClsoingBalance = inputSgstClsoingBalance - outputIgstClsoingBalance;
							totalSgstCrBalance = totalSgstCrBalance + outputIgstClsoingBalance;
							autoJv.setInputSgstBalance(inputSgstClsoingBalance);
							autoJv.setOutputIgstBalance((double)0);
							
						}

					}
					else
					{
						outputCgstClsoingBalance=outputCgstClsoingBalance-	inputCgstClsoingBalance  ;
						totalCgstDrBalance=totalCgstDrBalance-outputCgstClsoingBalance;
						autoJv.setOutputCgstbalance(outputCgstClsoingBalance);
						totalCgstCrbalance=autoJv.getInitialInputCgstBalance();
						autoJv.setInputCgstBalance((double)0);
						
						if (inputSgstClsoingBalance >= outputSgstClsoingBalance) {
							inputSgstClsoingBalance = inputSgstClsoingBalance - outputSgstClsoingBalance;
							totalSgstCrBalance = totalSgstCrBalance + outputSgstClsoingBalance;
							autoJv.setInputSgstBalance(inputSgstClsoingBalance);
						}
						else
						{
							outputSgstClsoingBalance=outputSgstClsoingBalance-	inputSgstClsoingBalance  ;
							totalSgstDrBalance=totalSgstDrBalance-outputSgstClsoingBalance;
							autoJv.setOutputSgstBalance(outputSgstClsoingBalance);
							totalSgstCrBalance=autoJv.getInitialInputSgstBalance();
							autoJv.setInputSgstBalance((double)0);
						}
						
						
					}

					

					
					// this block is for deduct o/p igst value from i/p cgst and i/p sgst 
					

					/*
					 * if(inputIgstClsoingBalance>=(outputCgstClsoingBalance+
					 * outputSgstClsoingBalance)) {
					 * inputIgstClsoingBalance=inputIgstClsoingBalance-(outputCgstClsoingBalance+
					 * outputSgstClsoingBalance);
					 * totalIgstCrBalance=totalIgstCrBalance+outputCgstClsoingBalance+
					 * outputSgstClsoingBalance;
					 * autoJv.setInputIgstBalance(inputIgstClsoingBalance);
					 * autoJv.setInputCgstBalance(inputCgstClsoingBalance);
					 * autoJv.setInputSgstBalance(inputSgstClsoingBalance);
					 * 
					 * }
					 */

					/*
					 * if(inputCgstClsoingBalance>=(outputIgstClsoingBalance/2)) {
					 * inputCgstClsoingBalance=inputCgstClsoingBalance-(outputIgstClsoingBalance/2);
					 * //inputCgstClsoingBalance=inputCgstClsoingBalance-outputIgstClsoingBalance;
					 * totalCgstCrbalance=totalCgstCrbalance+(outputIgstClsoingBalance/2);
					 * autoJv.setInputCgstBalance(inputCgstClsoingBalance);
					 * 
					 * }
					 * 
					 * else {
					 * outputIgstClsoingBalance=outputIgstClsoingBalance-inputCgstClsoingBalance;
					 * 
					 * outputCgstClsoingBalance=outputCgstClsoingBalance-inputCgstClsoingBalance;
					 * autoJv.setInputCgstBalance((double)0);
					 * totalCgstCrbalance=autoJv.getInitialInputCgstBalance(); }
					 */
					/*
					 * if(inputCgstClsoingBalance>=outputIgstClsoingBalance) {
					 * inputCgstClsoingBalance=inputCgstClsoingBalance-outputIgstClsoingBalance;
					 * totalCgstCrbalance=totalCgstCrbalance+outputIgstClsoingBalance;
					 * autoJv.setInputCgstBalance(inputCgstClsoingBalance);
					 * 
					 * if(inputCgstClsoingBalance>=outputCgstClsoingBalance) {
					 * inputCgstClsoingBalance=inputCgstClsoingBalance-outputCgstClsoingBalance;
					 * totalCgstCrbalance=totalCgstCrbalance+outputCgstClsoingBalance;
					 * autoJv.setInputCgstBalance(inputCgstClsoingBalance);
					 * 
					 * if(inputCgstClsoingBalance>=outputSgstClsoingBalance) {
					 * inputCgstClsoingBalance=inputCgstClsoingBalance-outputSgstClsoingBalance;
					 * totalCgstCrbalance=totalCgstCrbalance+outputSgstClsoingBalance;
					 * autoJv.setInputCgstBalance(inputCgstClsoingBalance); } else {
					 * outputSgstClsoingBalance=outputSgstClsoingBalance-inputCgstClsoingBalance;
					 * autoJv.setInputCgstBalance((double)0);
					 * totalCgstCrbalance=autoJv.getInitialInputCgstBalance();
					 * 
					 * if(inputSgstClsoingBalance>=outputSgstClsoingBalance) {
					 * inputSgstClsoingBalance=inputSgstClsoingBalance-outputSgstClsoingBalance;
					 * totalSgstCrBalance=totalSgstCrBalance+outputSgstClsoingBalance;
					 * autoJv.setInputSgstBalance(inputSgstClsoingBalance); } else {
					 * outputSgstClsoingBalance=outputSgstClsoingBalance-inputSgstClsoingBalance;
					 * autoJv.setInputSgstBalance((double)0);
					 * totalSgstCrBalance=autoJv.getInitialInputSgstBalance();
					 * autoJv.setGstPaybleBalance(outputSgstClsoingBalance); } } } else {
					 * outputCgstClsoingBalance=outputCgstClsoingBalance-inputCgstClsoingBalance;
					 * autoJv.setInputCgstBalance((double)0);
					 * totalCgstCrbalance=autoJv.getInitialInputCgstBalance();
					 * 
					 * if(inputSgstClsoingBalance>=outputCgstClsoingBalance) {
					 * inputSgstClsoingBalance=inputSgstClsoingBalance-outputCgstClsoingBalance;
					 * totalSgstCrBalance=totalSgstCrBalance+outputCgstClsoingBalance;
					 * autoJv.setInputSgstBalance(inputSgstClsoingBalance);
					 * 
					 * if(inputSgstClsoingBalance>=outputSgstClsoingBalance) {
					 * inputSgstClsoingBalance=inputSgstClsoingBalance-outputSgstClsoingBalance;
					 * totalSgstCrBalance=totalSgstCrBalance+outputSgstClsoingBalance;
					 * autoJv.setInputSgstBalance(inputSgstClsoingBalance); } else {
					 * outputSgstClsoingBalance=outputSgstClsoingBalance-inputSgstClsoingBalance;
					 * autoJv.setInputSgstBalance((double)0);
					 * totalSgstCrBalance=autoJv.getInitialInputSgstBalance();
					 * autoJv.setGstPaybleBalance(outputSgstClsoingBalance); }
					 * 
					 * 
					 * } else {
					 * outputCgstClsoingBalance=outputCgstClsoingBalance-inputSgstClsoingBalance;
					 * autoJv.setInputSgstBalance((double)0);
					 * totalSgstCrBalance=autoJv.getInitialInputSgstBalance();
					 * autoJv.setGstPaybleBalance(outputCgstClsoingBalance+outputSgstClsoingBalance)
					 * ;
					 * 
					 * } }
					 * 
					 * }
					 */
					/*
					 * else { outputIgstClsoingBalance = outputIgstClsoingBalance -
					 * inputCgstClsoingBalance; autoJv.setInputCgstBalance((double) 0);
					 * totalCgstCrbalance = autoJv.getInitialInputCgstBalance();
					 * 
					 * if (inputSgstClsoingBalance >= outputIgstClsoingBalance) {
					 * inputSgstClsoingBalance = inputSgstClsoingBalance - outputIgstClsoingBalance;
					 * totalSgstCrBalance = totalSgstCrBalance + outputIgstClsoingBalance;
					 * autoJv.setInputSgstBalance(inputSgstClsoingBalance);
					 * 
					 * if (inputSgstClsoingBalance >= outputCgstClsoingBalance) {
					 * 
					 * inputSgstClsoingBalance = inputSgstClsoingBalance - outputCgstClsoingBalance;
					 * totalSgstCrBalance = totalSgstCrBalance + outputCgstClsoingBalance;
					 * autoJv.setInputSgstBalance(inputSgstClsoingBalance);
					 * 
					 * if (inputSgstClsoingBalance >= outputSgstClsoingBalance) {
					 * inputSgstClsoingBalance = inputSgstClsoingBalance - outputSgstClsoingBalance;
					 * totalSgstCrBalance = totalSgstCrBalance + outputSgstClsoingBalance;
					 * autoJv.setInputSgstBalance(inputSgstClsoingBalance); } else {
					 * outputSgstClsoingBalance = outputSgstClsoingBalance -
					 * inputSgstClsoingBalance; autoJv.setInputSgstBalance((double) 0);
					 * totalSgstCrBalance = autoJv.getInitialInputSgstBalance(); //
					 * autoJv.setGstPaybleBalance(outputSgstClsoingBalance); } } else {
					 * outputCgstClsoingBalance = outputCgstClsoingBalance -
					 * inputSgstClsoingBalance; autoJv.setInputSgstBalance((double) 0);
					 * totalSgstCrBalance = autoJv.getInitialInputSgstBalance(); //
					 * autoJv.setGstPaybleBalance(outputCgstClsoingBalance+outputSgstClsoingBalance)
					 * ;
					 * 
					 * }
					 * 
					 * } else { outputIgstClsoingBalance = outputIgstClsoingBalance -
					 * inputSgstClsoingBalance; autoJv.setInputIgstBalance((double) 0);
					 * totalSgstCrBalance = autoJv.getInitialInputSgstBalance(); //
					 * autoJv.setGstPaybleBalance(outputCgstClsoingBalance+outputSgstClsoingBalance+
					 * outputIgstClsoingBalance); }
					 * 
					 * }
					 */

				}

			}

			autoJv.setTotalInputIgstDrBalance(totalIgstCrBalance);
			autoJv.setTotalInputCgstDrBalance(totalCgstCrbalance);
			autoJv.setTotalInputSgstDrBalance(totalSgstCrBalance);

			autoJv.setTotalOutputIgstCrBalance(totalIgstDrBalance);
			autoJv.setTotalOutputCgstCrbalance(totalCgstDrBalance);
			autoJv.setTotalOutputSgstCrBalance(totalSgstDrBalance);
			// autoJv.setTotalgstPaybleDrBalance(autoJv.getGstPaybleBalance());

			session.setAttribute("autoJv", autoJv);
			model.setViewName("/transactions/gstAutoJV");
			return model;
		}

	}

	@RequestMapping(value = "submitGstAutoJV", method = RequestMethod.GET)
	public ModelAndView submitGstAutoJV(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		long company_id = (long) session.getAttribute("company_id");
System.out.println("GST SAved");
		GSTAutoJV autoJv = (GSTAutoJV) session.getAttribute("autoJv");

		SubLedger outputcgst = subLedgerDAO.findOne("Output CGST", user.getCompany().getCompany_id());

		SubLedger outputsgst = subLedgerDAO.findOne("Output SGST", user.getCompany().getCompany_id());

		SubLedger outputigst = subLedgerDAO.findOne("Output IGST", user.getCompany().getCompany_id());

		SubLedger inputcgst = subLedgerDAO.findOne("Input CGST", user.getCompany().getCompany_id());

		SubLedger inputsgst = subLedgerDAO.findOne("Input SGST", user.getCompany().getCompany_id());

		SubLedger inputigst = subLedgerDAO.findOne("Input IGST", user.getCompany().getCompany_id());

		// SubLedger gstpayble = subLedgerDAO.findOne("GST Payable",
		// user.getCompany().getCompany_id());

		Long id = payroll_service.saveGstAutoJv(autoJv);
		GSTAutoJV entity = payroll_service.findGstAutojv(id);

		if (outputcgst != null && autoJv.getTotalOutputCgstCrbalance() > 0) {
			subLedgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),
					entity.getCreated_date(), company_id, outputcgst.getSubledger_Id(), (long) 2, (double) 0,
					(double) entity.getTotalOutputCgstCrbalance(), (long) 1, null, null, null, null, null, null, null,
					null, null, entity, null, null);
		}

		if (inputcgst != null && autoJv.getTotalInputCgstDrBalance() > 0) {
			subLedgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),
					entity.getCreated_date(), company_id, inputcgst.getSubledger_Id(), (long) 2,
					(double) entity.getTotalInputCgstDrBalance(), (double) 0, (long) 1, null, null, null, null, null,
					null, null, null, null, entity, null, null);
		}

		if (outputsgst != null && autoJv.getTotalOutputSgstCrBalance() > 0) {
			subLedgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),
					entity.getCreated_date(), company_id, outputsgst.getSubledger_Id(), (long) 2, (double) 0,
					(double) entity.getTotalOutputSgstCrBalance(), (long) 1, null, null, null, null, null, null, null,
					null, null, entity, null, null);
		}

		if (inputsgst != null && autoJv.getTotalInputSgstDrBalance() > 0) {
			subLedgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),
					entity.getCreated_date(), company_id, inputsgst.getSubledger_Id(), (long) 2,
					(double) entity.getTotalInputSgstDrBalance(), (double) 0, (long) 1, null, null, null, null, null,
					null, null, null, null, entity, null, null);
		}

		if (outputigst != null && autoJv.getTotalOutputIgstCrBalance() > 0) {
			subLedgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),
					entity.getCreated_date(), company_id, outputigst.getSubledger_Id(), (long) 2, (double) 0,
					(double) entity.getTotalOutputIgstCrBalance(), (long) 1, null, null, null, null, null, null, null,
					null, null, entity, null, null);
		}

		if (inputigst != null && autoJv.getTotalInputIgstDrBalance() > 0) {
			subLedgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),
					entity.getCreated_date(), company_id, inputigst.getSubledger_Id(), (long) 2,
					(double) entity.getTotalInputIgstDrBalance(), (double) 0, (long) 1, null, null, null, null, null,
					null, null, null, null, entity, null, null);
		}

		/*
		 * if(autoJv.getBalanceCase().equals(2)) { if (gstpayble != null &&
		 * autoJv.getTotalgstPaybleDrBalance() !=null &&
		 * autoJv.getTotalgstPaybleDrBalance()>0) {
		 * subLedgerService.addsubledgertransactionbalance(entity.getAccountingYear().
		 * getYear_id(),entity.getCreated_date(),company_id,
		 * gstpayble.getSubledger_Id(), (long) 2,(double)
		 * entity.getTotalgstPaybleDrBalance(),(double) 0, (long)
		 * 1,null,null,null,null,null,null,null,null,null,entity,null,null); } } else {
		 * if (gstpayble != null && autoJv.getTotalgstPaybleDrBalance() !=null &&
		 * autoJv.getTotalgstPaybleDrBalance()>0) {
		 * subLedgerService.addsubledgertransactionbalance(entity.getAccountingYear().
		 * getYear_id(),entity.getCreated_date(),company_id,
		 * gstpayble.getSubledger_Id(), (long) 2,(double) 0,(double)
		 * entity.getTotalgstPaybleDrBalance(), (long)
		 * 1,null,null,null,null,null,null,null,null,null,entity,null,null); } }
		 */
		session.removeAttribute("autoJv");
		session.setAttribute("successMsg", "Gst Auto JV Saved Sucessfully.");

		return new ModelAndView("redirect:/gstAutoJVList");

	}

	@RequestMapping(value = "gstAutoJVList", method = RequestMethod.GET)
	public ModelAndView gstAutoJVList(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		List<YearEnding> yearEndlist = yearService.findAllYearEnding(company_id);

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

		if ((String) session.getAttribute("successMsg") != null) {
			String successMsg = (String) session.getAttribute("successMsg");
			session.removeAttribute("successMsg");

			model.addObject("successMsg", successMsg);
		}

		model.addObject("gstAutoJvList", payroll_service.getAllGstAutoJVReletedToCompany(company_id));
		model.addObject("yearEndlist", yearEndlist);
		model.setViewName("/transactions/gstAutoJVList");
		return model;
	}

	@RequestMapping(value = "deleteGSTAutoJV", method = RequestMethod.GET)
	public ModelAndView deleteGSTAutoJV(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		String msg = payroll_service.deleteGSTAutoJVByIdValue(id, company_id);
		session.setAttribute("successMsg", msg);
		return new ModelAndView("redirect:/gstAutoJVList");
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "viewGstAutoJV1", method = RequestMethod.GET)
	public ModelAndView viewGstAutoJV1(@RequestParam("id") long id) {

		ModelAndView model = new ModelAndView();
		GSTAutoJV autoJv = new GSTAutoJV();
		autoJv = payroll_service.getGstAutoJvById(id);

		model.addObject("autoJv", autoJv);
		// model.addObject("payrollEmployeeList",
		// payroll_service.findAllPayrollEmployeeList(id));
		model.setViewName("/transactions/gstlAutoJVView");
		return model;
	}
}