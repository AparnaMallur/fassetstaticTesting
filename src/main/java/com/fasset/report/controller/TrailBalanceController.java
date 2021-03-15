package com.fasset.report.controller;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.TrialBalanceReportValidator;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.entities.Ledger;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.form.LedgerForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.OpeningBalancesOfSubedgerForm;
import com.fasset.form.ProfitAndLossReportForm;
import com.fasset.form.TrialBalanceReportForm;
import com.fasset.service.interfaces.IAccountGroupService;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.mysql.jdbc.Connection;

@Controller
@SessionAttributes("user")
public class TrailBalanceController extends MyAbstractController{
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private TrialBalanceReportValidator trialBalanceReportValidator;
	
	@Autowired
	private IAccountingYearService AccountingYearService;
	
	@Autowired
	private IOpeningBalancesService openingBalancesService;
	
	@Autowired
	private ILedgerService ledgerService;
	
	@Autowired
	private IAccountGroupService accountGroupService ;
		
	@Autowired
	private ISubLedgerService subLedgerService;
	
	@Autowired
	private ISuplliersService suplliersService;
	
	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private IBankService bankService;
	
	private List<Company> companyList = new ArrayList<Company>();
	private List<AccountGroup> accList = new ArrayList<AccountGroup>();
	private List<Ledger> legderList = new ArrayList<Ledger>();
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesOfsubledger = new ArrayList<OpeningBalancesOfSubedgerForm>();
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesbeforestartDate = new ArrayList<OpeningBalancesOfSubedgerForm>();
	

	private Company company;
	private LocalDate fromDate;
	private LocalDate toDate;
	private List<OpeningBalancesForm>  subledgerList = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>  subledgerListbeforestartDate = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>   ledgerList =new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>   bankOpeningBalanceList =new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>   supplierOpeningBalanceList  =new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>   customerOpeningBalanceList  = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>   supplierOpeningBalanceBeforeStartDate =new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>   customerOpeningBalanceBeforeStartDate = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>   bankOpeningBalanceBeforeStartDate =new ArrayList<OpeningBalancesForm>();
	
	@RequestMapping(value = "trialBalanceReport", method = RequestMethod.GET)
	public ModelAndView trialBalanceReport(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");	
		User user = (User)session.getAttribute("user");	
		ModelAndView model = new ModelAndView();
		TrialBalanceReportForm trialBalanceReportForm = new TrialBalanceReportForm();
		if (role == ROLE_SUPERUSER) {			
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("trialBalanceReportForm", trialBalanceReportForm);
			model.addObject("companyList",companyList);
			model.setViewName("/report/trialBalanceReportForKpo");
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("trialBalanceReportForm", trialBalanceReportForm);
			model.addObject("companyList",companyList);
			model.setViewName("/report/trialBalanceReportForKpo");
		} else {
			
			model.addObject("trialBalanceReportForm", trialBalanceReportForm);
			model.setViewName("/report/trialBalanceReport");
		}
		
		return model;
	}
	
	@RequestMapping(value = "trialBalanceReport", method = RequestMethod.POST)
	public ModelAndView trialBalanceReport(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("trialBalanceReportForm") TrialBalanceReportForm trialBalanceReportForm, BindingResult result) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		trialBalanceReportValidator.validate(trialBalanceReportForm, result);
		if(result.hasErrors()){
			model.setViewName("/report/trialBalanceReport");
		}
		else
		{
			subledgerList.clear();
			subledgerListbeforestartDate.clear();
			ledgerList.clear();
			bankOpeningBalanceList.clear();
			supplierOpeningBalanceList.clear();
			customerOpeningBalanceList.clear();
			bankOpeningBalanceBeforeStartDate.clear();
			supplierOpeningBalanceBeforeStartDate.clear();
			customerOpeningBalanceBeforeStartDate.clear();
			ListForOpeningbalancesOfsubledger.clear();
			ListForOpeningbalancesbeforestartDate.clear();
			
			
			//-------------Calculation for opening balance before start date-----------------
			
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(trialBalanceReportForm.getFromDate(), company_id))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Bank bank = null;
					try {
						bank = bankService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
                    form.setBankName(bank.getBank_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					bankOpeningBalanceBeforeStartDate.add(form);
					}
				}
			}
			
			Collections.sort(bankOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String bankName1 = form1.getBankName().trim();
	                String bankName2 = form2.getBankName().trim();
	                return bankName1.compareTo(bankName2);
	            }
	        });
			
		
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplierBeforeFromDate(trialBalanceReportForm.getFromDate(), company_id))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Suppliers sup = null;
					try {
						sup = suplliersService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(sup!=null && sup.getSupplier_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
				    form.setSupplierName(sup.getCompany_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					supplierOpeningBalanceBeforeStartDate.add(form);
					}
				}
			}
			
			Collections.sort(supplierOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String supplierName1 = form1.getSupplierName().trim();
	                String supplierName2 = form2.getSupplierName().trim();
	                return supplierName1.compareTo(supplierName2);
	            }
	        });
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomerBeforeFromDate(trialBalanceReportForm.getFromDate(), company_id))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Customer cus = null;
					try {
						cus = customerService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(cus!=null && cus.getCustomer_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) 
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setCustomerName(cus.getFirm_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					customerOpeningBalanceBeforeStartDate.add(form);
					}
				}
			}
			Collections.sort(customerOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String customerName1 = form1.getCustomerName().trim();
	                String customerName2 = form2.getCustomerName().trim();
	                return customerName1.compareTo(customerName2);
	            }
	        });
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getToDate(), company_id,false))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{	
					
					SubLedger sub = null;
					try {
						sub = subLedgerService.getById((long)bal[0]);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
					{
						OpeningBalancesForm form = new OpeningBalancesForm();
						form.setSubledgerName(sub.getSubledger_name());
						try {
							form.setSubledger(sub);
						} catch (Exception e) {
							e.printStackTrace();
						}
					
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					subledgerListbeforestartDate.add(form);
					} 
					
					
				}
			}
			
			accList = accountGroupService.findAll();
			legderList = ledgerService.findLedgersOnlyOfComapany(company_id);
			
			for(AccountGroup group : accList)
			{
				double Totaldebit_balance = 0;
				double Totalcredit_balance = 0;
				
				OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
				List<LedgerForm>  ledgerFormList = new ArrayList<>();
				List<String> AccountSubGroupNameList = new ArrayList<>();
				sublederform.setAccountGroupName(group.getGroup_name());
				sublederform.setGroup(group);
				for(AccountSubGroup subgroup : group.getAccount_sub_group())
				{
					AccountSubGroupNameList.add(subgroup.getSubgroup_name());
					
					
					for(Ledger ledger : legderList)
					{
						
						LedgerForm ledgerform = new LedgerForm();
					    Double ledgerdebit_balance =0.0;
					    Double ledgercredit_balance = 0.0;
						List<OpeningBalancesForm>  openingBalanceList = new ArrayList<>();
						for(OpeningBalancesForm form : subledgerListbeforestartDate)
						{
							if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
							(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
							&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
							{
								
									
								
							
								OpeningBalancesForm formbalance = new OpeningBalancesForm();
								formbalance.setSubledgerName(form.getSubledgerName());
								formbalance.setCredit_balance(form.getCredit_balance());
								formbalance.setDebit_balance(form.getDebit_balance());
								formbalance.setSubledger(form.getSubledger());
								openingBalanceList.add(formbalance);
								ledgerdebit_balance =ledgerdebit_balance+form.getDebit_balance();
								ledgercredit_balance =ledgercredit_balance+form.getCredit_balance();
								Totaldebit_balance = Totaldebit_balance+form.getDebit_balance();
								Totalcredit_balance = Totalcredit_balance+form.getCredit_balance();
								
								
							}
						}
						if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
						{
						ledgerform.setSubgroupName(subgroup.getSubgroup_name());
						ledgerform.setLedgerName(ledger.getLedger_name());
						ledgerform.setLedgercredit_balance(ledgercredit_balance);
						ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
						ledgerform.setSubledgerList(openingBalanceList);
						ledgerFormList.add(ledgerform);
						}
						
					}
					
				}
				
				sublederform.setTotalcredit_balance(Totalcredit_balance);
				sublederform.setTotaldebit_balance(Totaldebit_balance);
				sublederform.setLedgerformlist(ledgerFormList);
				sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
				ListForOpeningbalancesbeforestartDate.add(sublederform);
				
				
			}
			
			//-------------Calculation for opening balance between start date and end date-----------------
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getToDate(), company_id,true))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					
					SubLedger sub = null;
					try {
						sub = subLedgerService.getById((long)bal[0]);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))// for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
					{
						OpeningBalancesForm form = new OpeningBalancesForm();
						form.setSubledgerName(sub.getSubledger_name());
						try {
							form.setSubledger(sub);
						} catch (Exception e) {
							e.printStackTrace();
						}
					
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					subledgerList.add(form);
					} 
				}
			}
			
		   /*accList = accountGroupService.findAll();
		   legderList = ledgerService.findLedgersOnlyOfComapany(company_id);*/
		   
			for(AccountGroup group : accList)
			{
				double Totaldebit_balance = 0;
				double Totalcredit_balance = 0;
				
				OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
				List<LedgerForm>  ledgerFormList = new ArrayList<>();
				List<String> AccountSubGroupNameList = new ArrayList<>();
				sublederform.setAccountGroupName(group.getGroup_name());
				sublederform.setGroup(group);
				for(AccountSubGroup subgroup : group.getAccount_sub_group())
				{
					AccountSubGroupNameList.add(subgroup.getSubgroup_name());
					
					for(Ledger ledger : legderList)
					{
						
						LedgerForm ledgerform = new LedgerForm();
					    Double ledgerdebit_balance =0.0;
					    Double ledgercredit_balance = 0.0;
						List<OpeningBalancesForm>  openingBalanceList = new ArrayList<>();
						for(OpeningBalancesForm form : subledgerList)
						{
							if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
							(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
							&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
							{
								
								OpeningBalancesForm formbalance = new OpeningBalancesForm();
								formbalance.setSubledgerName(form.getSubledgerName());
								formbalance.setCredit_balance(form.getCredit_balance());
								formbalance.setDebit_balance(form.getDebit_balance());
								formbalance.setSubledger(form.getSubledger());
								openingBalanceList.add(formbalance);
								ledgerdebit_balance =ledgerdebit_balance+form.getDebit_balance();
								ledgercredit_balance =ledgercredit_balance+form.getCredit_balance();
								Totaldebit_balance = Totaldebit_balance+form.getDebit_balance();
								Totalcredit_balance = Totalcredit_balance+form.getCredit_balance();
								
								
							}
						}
						if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
						{
						ledgerform.setSubgroupName(subgroup.getSubgroup_name());
						ledgerform.setLedgerName(ledger.getLedger_name());
						ledgerform.setLedgercredit_balance(ledgercredit_balance);
						ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
						ledgerform.setSubledgerList(openingBalanceList);
						ledgerFormList.add(ledgerform);
						}
						
					}
					
				}
				sublederform.setTotalcredit_balance(Totalcredit_balance);
				sublederform.setTotaldebit_balance(Totaldebit_balance);
				sublederform.setLedgerformlist(ledgerFormList);
				sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
				ListForOpeningbalancesOfsubledger.add(sublederform);
			}
		
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBank(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getToDate(), company_id))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Bank bank = null;
					try {
						bank = bankService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
                    form.setBankName(bank.getBank_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					bankOpeningBalanceList.add(form);
					}
				}
			}
			
			Collections.sort(bankOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String bankName1 = form1.getBankName().trim();
	                String bankName2 = form2.getBankName().trim();
	                return bankName1.compareTo(bankName2);
	            }
	        });
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplier(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getToDate(), company_id))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Suppliers sup = null;
					try {
						sup = suplliersService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(sup!=null && sup.getSupplier_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
				    form.setSupplierName(sup.getCompany_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					supplierOpeningBalanceList.add(form);
					}
				}
			}
			
			Collections.sort(supplierOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String supplierName1 = form1.getSupplierName().trim();
	                String supplierName2 = form2.getSupplierName().trim();
	                return supplierName1.compareTo(supplierName2);
	            }
	        });
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomer(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getToDate(), company_id))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Customer cus = null;
					try {
						cus = customerService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(cus!=null && cus.getCustomer_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) 
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setCustomerName(cus.getFirm_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					customerOpeningBalanceList.add(form);
					}
				}
			}
			
			Collections.sort(customerOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String customerName1 = form1.getCustomerName().trim();
	                String customerName2 = form2.getCustomerName().trim();
	                return customerName1.compareTo(customerName2);
	            }
	        });
			
			try {
				company = companyService.getCompanyWithCompanyStautarType(company_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fromDate=trialBalanceReportForm.getFromDate();
			toDate = trialBalanceReportForm.getToDate();
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date", toDate);
			model.addObject("subOpeningListBeforeStartDate", ListForOpeningbalancesbeforestartDate);
			model.addObject("subOpeningList", ListForOpeningbalancesOfsubledger);
		    model.addObject("bankOpeningBalanceList", bankOpeningBalanceList);
			model.addObject("supplierOpeningBalanceList", supplierOpeningBalanceList);
			model.addObject("customerOpeningBalanceList", customerOpeningBalanceList);
			model.addObject("bankOpeningBalanceBeforeStartDate", bankOpeningBalanceBeforeStartDate);
		    model.addObject("supplierOpeningBalanceBeforeStartDate", supplierOpeningBalanceBeforeStartDate);
		    model.addObject("customerOpeningBalanceBeforeStartDate", customerOpeningBalanceBeforeStartDate);
			model.addObject("emptyString", new String("---"));
			model.addObject("group1", "Current Assets");
			model.addObject("subGroup2", "Trade Receivables");
			model.addObject("subGroup3", "Cash & Bank Balances");
			model.addObject("group4", "Current Liabilities");
			model.addObject("subGroup5", "Trade Payables");
			model.setViewName("/report/trialBalanceReportList");
		}
		return model;
	}
	
	@RequestMapping(value = "trialBalanceReportForKpo", method = RequestMethod.POST)
	public ModelAndView trialBalanceReportForKpo(@ModelAttribute("trialBalanceReportForm") TrialBalanceReportForm trialBalanceReportForm, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		
		trialBalanceReportValidator.validate(trialBalanceReportForm, result);
		if(result.hasErrors()){
			model.addObject("companyList",companyList);
			model.setViewName("/report/trialBalanceReportForKpo");
		}
		
		else
		{
			subledgerList.clear();
			subledgerListbeforestartDate.clear();
			ledgerList.clear();
			bankOpeningBalanceList.clear();
			supplierOpeningBalanceList.clear();
			customerOpeningBalanceList.clear();
			bankOpeningBalanceBeforeStartDate.clear();
			supplierOpeningBalanceBeforeStartDate.clear();
			customerOpeningBalanceBeforeStartDate.clear();
			ListForOpeningbalancesOfsubledger.clear();
			ListForOpeningbalancesbeforestartDate.clear();
			
			List<OpeningBalancesForm> Allbank = new ArrayList<>();
			List<OpeningBalancesForm> Allcustomer = new ArrayList<>();
			List<OpeningBalancesForm> Allsupplier = new ArrayList<>();
			//-------------Calculation for opening balance before start date-----------------
		
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getCompanyId()))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Bank bank = null;
					try {
						bank = bankService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
                    form.setBankName(bank.getBank_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					bankOpeningBalanceBeforeStartDate.add(form);
					Allbank.add(form);
					}
				}
			}
			
			Collections.sort(bankOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String bankName1 = form1.getBankName().trim();
	                String bankName2 = form2.getBankName().trim();
	                return bankName1.compareTo(bankName2);
	            }
	        });
			
		
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplierBeforeFromDate(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getCompanyId()))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Suppliers sup = null;
					try {
						sup = suplliersService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(sup!=null && sup.getSupplier_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
				    form.setSupplierName(sup.getCompany_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					supplierOpeningBalanceBeforeStartDate.add(form);
					Allsupplier.add(form);
					}
				}
			}
			
			Collections.sort(supplierOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String supplierName1 = form1.getSupplierName().trim();
	                String supplierName2 = form2.getSupplierName().trim();
	                return supplierName1.compareTo(supplierName2);
	            }
	        });
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomerBeforeFromDate(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getCompanyId()))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Customer cus = null;
					try {
						cus = customerService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(cus!=null && cus.getCustomer_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) 
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setCustomerName(cus.getFirm_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					customerOpeningBalanceBeforeStartDate.add(form);
					Allcustomer.add(form);
					}
				}
			}
			Collections.sort(customerOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String customerName1 = form1.getCustomerName().trim();
	                String customerName2 = form2.getCustomerName().trim();
	                return customerName1.compareTo(customerName2);
	            }
	        });
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getToDate(), trialBalanceReportForm.getCompanyId(),false))
			{
				
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{	
					
					SubLedger sub = null;
					try {
						sub = subLedgerService.getById((long)bal[0]);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
					{
						OpeningBalancesForm form = new OpeningBalancesForm();
						form.setSubledgerName(sub.getSubledger_name());
						try {
							form.setSubledger(sub);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					subledgerListbeforestartDate.add(form);
					} 
					
					
				}
			}
			
			accList = accountGroupService.findAll();
			legderList = ledgerService.findLedgersOnlyOfComapany(trialBalanceReportForm.getCompanyId());
			List<LedgerForm> AllledgerFormList = new ArrayList<>();
			for(AccountGroup group : accList)
			{
				double Totaldebit_balance = 0;
				double Totalcredit_balance = 0;
				
				OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
				List<LedgerForm>  ledgerFormList = new ArrayList<>();
				List<String> AccountSubGroupNameList = new ArrayList<>();
				sublederform.setAccountGroupName(group.getGroup_name());
				sublederform.setGroup(group);
				List<AccountSubGroup> sortedList = new ArrayList<>(group.getAccount_sub_group());
				Collections.sort(sortedList, new Comparator<AccountSubGroup>() {
		            public int compare(AccountSubGroup form1, AccountSubGroup form2) {
		                String bankName1 = form1.getSubgroup_name();
		                String bankName2 = form2.getSubgroup_name();
		                return bankName1.compareTo(bankName2);
		            }
		        });
				
				for(AccountSubGroup subgroup : sortedList)
				{
					AccountSubGroupNameList.add(subgroup.getSubgroup_name());
					
					
					for(Ledger ledger : legderList)
					{
						
						LedgerForm ledgerform = new LedgerForm();
						LedgerForm ledgerform1 = new LedgerForm();
					    Double ledgerdebit_balance =0.0;
					    Double ledgercredit_balance = 0.0;
						List<OpeningBalancesForm>  openingBalanceList = new ArrayList<>();
						for(OpeningBalancesForm form : subledgerListbeforestartDate)
						{
							if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
							(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
							&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
							{
								
								OpeningBalancesForm formbalance = new OpeningBalancesForm();
								
								formbalance.setSubledgerName(form.getSubledgerName());
								formbalance.setCredit_balance(form.getCredit_balance());
								formbalance.setDebit_balance(form.getDebit_balance());
								formbalance.setSubledger(form.getSubledger());
								openingBalanceList.add(formbalance);
								ledgerdebit_balance =ledgerdebit_balance+form.getDebit_balance();
								ledgercredit_balance =ledgercredit_balance+form.getCredit_balance();
								Totaldebit_balance = Totaldebit_balance+form.getDebit_balance();
								Totalcredit_balance = Totalcredit_balance+form.getCredit_balance();
								
								
							}
						}
						if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
						{
						ledgerform.setSubgroupName(subgroup.getSubgroup_name());
						
						ledgerform.setLedgerName(ledger.getLedger_name());
						ledgerform.setLedgercredit_balance(ledgercredit_balance);
						ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
						ledgerform.setSubledgerList(openingBalanceList);
						ledgerFormList.add(ledgerform);
                        ledgerform1.setSubgroupName(subgroup.getSubgroup_name());
						
						ledgerform1.setLedgerName(ledger.getLedger_name());
						ledgerform1.setLedgercredit_balance(0.0);
						ledgerform1.setLedgerdebit_balance(0.0);
						AllledgerFormList.add(ledgerform1);
						}
						
					}
					
				}
				
				sublederform.setTotalcredit_balance(Totalcredit_balance);
				sublederform.setTotaldebit_balance(Totaldebit_balance);
				sublederform.setLedgerformlist(ledgerFormList);
				sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
				ListForOpeningbalancesbeforestartDate.add(sublederform);
				
				
			}
			
			//-------------Calculation for opening balance between start date and end date-----------------
			
			System.out.println("Calculation for opening balance between start date and end date");
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getToDate(), trialBalanceReportForm.getCompanyId(),true))
			{
				
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					
					SubLedger sub = null;
					try {
						sub = subLedgerService.getById((long)bal[0]);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))// for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
					{
						OpeningBalancesForm form = new OpeningBalancesForm();
						
						form.setSubledgerName(sub.getSubledger_name());
						try {
							form.setSubledger(sub);
						} catch (Exception e) {
							e.printStackTrace();
						}
					
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					subledgerList.add(form);
					} 
				}
			}
			boolean isPresent=false;
		  /* accList = accountGroupService.findAll();
		   legderList = ledgerService.findLedgersOnlyOfComapany(trialBalanceReportForm.getCompanyId());*/
			
			for(AccountGroup group : accList)
			{
				double Totaldebit_balance = 0;
				double Totalcredit_balance = 0;
				
				OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
				List<LedgerForm>  ledgerFormList = new ArrayList<>();
				List<String> AccountSubGroupNameList = new ArrayList<>();
				sublederform.setAccountGroupName(group.getGroup_name());
				sublederform.setGroup(group);
				List<AccountSubGroup> sortedList = new ArrayList<>(group.getAccount_sub_group());
				Collections.sort(sortedList, new Comparator<AccountSubGroup>() {
		            public int compare(AccountSubGroup form1, AccountSubGroup form2) {
		                String bankName1 = form1.getSubgroup_name();
		                String bankName2 = form2.getSubgroup_name();
		                return bankName1.compareTo(bankName2);
		            }
		        });
				
				for(AccountSubGroup subgroup : sortedList)
				{
					AccountSubGroupNameList.add(subgroup.getSubgroup_name());
					
					for(Ledger ledger : legderList)
					{
						
						LedgerForm ledgerform = new LedgerForm();
						LedgerForm ledgerform1 = new LedgerForm();
					    Double ledgerdebit_balance =0.0;
					    Double ledgercredit_balance = 0.0;
						List<OpeningBalancesForm>  openingBalanceList = new ArrayList<>();
						for(OpeningBalancesForm form : subledgerList)
						{
							if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
							(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
							&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
							{
								
								OpeningBalancesForm formbalance = new OpeningBalancesForm();
								formbalance.setSubledgerName(form.getSubledgerName());
								formbalance.setCredit_balance(form.getCredit_balance());
								formbalance.setDebit_balance(form.getDebit_balance());
								formbalance.setSubledger(form.getSubledger());
								openingBalanceList.add(formbalance);
								ledgerdebit_balance =ledgerdebit_balance+form.getDebit_balance();
								ledgercredit_balance =ledgercredit_balance+form.getCredit_balance();
								Totaldebit_balance = Totaldebit_balance+form.getDebit_balance();
								Totalcredit_balance = Totalcredit_balance+form.getCredit_balance();
								
								
							}
						}
						if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
						{
						ledgerform.setSubgroupName(subgroup.getSubgroup_name());
						ledgerform.setLedgerName(ledger.getLedger_name());
					
						
						ledgerform.setLedgercredit_balance(ledgercredit_balance);
						ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
						ledgerform.setSubledgerList(openingBalanceList);
						ledgerFormList.add(ledgerform);
						
						ledgerform1.setLedgerName(ledger.getLedger_name());
						ledgerform1.setLedgercredit_balance(0.0);
						ledgerform1.setLedgerdebit_balance(0.0);
						isPresent=false;
						
						for(LedgerForm l:AllledgerFormList){
							if(l.getLedgerName().equalsIgnoreCase(ledgerform1.getLedgerName())){
								isPresent=true ;
										break;
							}
						}
						if(isPresent==false){
							AllledgerFormList.add(ledgerform1);
						}
						
						}
						
					}
					
				}
				sublederform.setTotalcredit_balance(Totalcredit_balance);
				sublederform.setTotaldebit_balance(Totaldebit_balance);
				sublederform.setLedgerformlist(ledgerFormList);
				sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
				ListForOpeningbalancesOfsubledger.add(sublederform);
			}
		
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBank(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getToDate(), trialBalanceReportForm.getCompanyId()))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Bank bank = null;
					try {
						bank = bankService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
                    form.setBankName(bank.getBank_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					isPresent=false;
					for(OpeningBalancesForm b:Allbank){
						if(b.getBankName().equalsIgnoreCase(bank.getBank_name())){
							isPresent=true;
							break;
						}
					}
					
					if(isPresent==false){
						Allbank.add(form);
					}
					bankOpeningBalanceList.add(form);
					}
				}
			}
			
			Collections.sort(bankOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String bankName1 = form1.getBankName().trim();
	                String bankName2 = form2.getBankName().trim();
	                return bankName1.compareTo(bankName2);
	            }
	        });
			
			Collections.sort(Allbank, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String bankName1 = form1.getBankName().trim();
	                String bankName2 = form2.getBankName().trim();
	                return bankName1.compareTo(bankName2);
	            }
	        });
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplier(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getToDate(), trialBalanceReportForm.getCompanyId()))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Suppliers sup = null;
					try {
						sup = suplliersService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(sup!=null && sup.getSupplier_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
				    form.setSupplierName(sup.getCompany_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					isPresent=false;
					for(OpeningBalancesForm b:Allsupplier){
						if(b.getSupplierName().equalsIgnoreCase(sup.getCompany_name())){
							isPresent=true;
							break;
						}
					}
					
					if(isPresent==false){
						Allsupplier.add(form);
					}
					supplierOpeningBalanceList.add(form);
					}
				}
			}
			
			Collections.sort(supplierOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String supplierName1 = form1.getSupplierName().trim();
	                String supplierName2 = form2.getSupplierName().trim();
	                return supplierName1.compareTo(supplierName2);
	            }
	        });
			
			
			Collections.sort(Allsupplier, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String supplierName1 = form1.getSupplierName().trim();
	                String supplierName2 = form2.getSupplierName().trim();
	                return supplierName1.compareTo(supplierName2);
	            }
	        });
			for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomer(trialBalanceReportForm.getFromDate(), trialBalanceReportForm.getToDate(), trialBalanceReportForm.getCompanyId()))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Customer cus = null;
					try {
						cus = customerService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(cus!=null && cus.getCustomer_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) 
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setCustomerName(cus.getFirm_name());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					isPresent=false;
					for(OpeningBalancesForm b:Allcustomer){
						if(b.getCustomerName().equalsIgnoreCase(cus.getFirm_name())){
							isPresent=true;
							break;
						}
					}
					
					if(isPresent==false){
						Allcustomer.add(form);
					}
					customerOpeningBalanceList.add(form);
					}
				}
			}
			 
			Collections.sort(AllledgerFormList, new Comparator<LedgerForm>() {
	            public int compare(LedgerForm form1, LedgerForm form2) {
	                String ledgerName1 = form1.getLedgerName().trim();
	                String ledgerName2 = form2.getLedgerName().trim();
	                return ledgerName1.compareTo(ledgerName2);
	            }
	        });
			
			try {
				company = companyService.getCompanyWithCompanyStautarType(trialBalanceReportForm.getCompanyId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(LedgerForm l:AllledgerFormList){
				System.out.println("All Ledger "+l.getLedgerName());
			}
			for(OpeningBalancesForm form : bankOpeningBalanceBeforeStartDate){
				
				System.out.println("bank before startdate"+form.getBankName() );
				
				
				
			}
for(OpeningBalancesForm form : bankOpeningBalanceList){
				
				System.out.println("bank curr period"+form.getBankName() );
				
				
				
			}
for(OpeningBalancesForm form : supplierOpeningBalanceBeforeStartDate){
	
	System.out.println("supp bd period"+form.getSupplierName() );
	
	
	
}
for(OpeningBalancesForm form : supplierOpeningBalanceList){
	
	System.out.println("supp curr period"+form.getSupplierName() );
	
	
	
}

for(OpeningBalancesForm form : customerOpeningBalanceBeforeStartDate){
	
	System.out.println("cus bd period"+form.getCustomerName() );
	
	
	
}
for(OpeningBalancesForm form : customerOpeningBalanceList){
	
	System.out.println("cus curr period"+form.getCustomerName() );
	
	
	
}
			for(OpeningBalancesOfSubedgerForm form : ListForOpeningbalancesbeforestartDate){
				if (form.getGroup().getPostingSide().getPosting_id()==3){
				System.out.println("obalancebeforedate.accountGroupName"+form.getAccountGroupName() );
				for(String o:form.getAccountSubGroupNameList()){
					System.out.println("account sub group is " + o);
				}
				for(LedgerForm l:form.getLedgerformlist()){
					System.out.println("ledger Name in beforedate is "+l.getLedgerName() );
				}
				}
			}
				for(OpeningBalancesOfSubedgerForm form1 : ListForOpeningbalancesOfsubledger){
					System.out.println("obalance.accountGroupName"+form1.getAccountGroupName());
					if (form1.getGroup().getPostingSide().getPosting_id()==3){
					System.out.println("obalance.accountGroupName"+form1.getAccountGroupName());
					for(LedgerForm l:form1.getLedgerformlist()){
						System.out.println("ledger Name in obalance is "+l.getLedgerName());
					}
					}
				}
			Collections.sort(customerOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String customerName1 = form1.getCustomerName().trim();
	                String customerName2 = form2.getCustomerName().trim();
	                return customerName1.compareTo(customerName2);
	            }
	        });
			
			Collections.sort(Allcustomer, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String customerName1 = form1.getCustomerName().trim();
	                String customerName2 = form2.getCustomerName().trim();
	                return customerName1.compareTo(customerName2);
	            }
	        });
			fromDate=trialBalanceReportForm.getFromDate();
			toDate = trialBalanceReportForm.getToDate();
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date", toDate);
			model.addObject("AllledgerFormList", AllledgerFormList);
			model.addObject("Allsupplier", Allsupplier);
			model.addObject("Allcustomer", Allcustomer);
			model.addObject("Allbank", Allbank);
			model.addObject("subOpeningListBeforeStartDate", ListForOpeningbalancesbeforestartDate);
			model.addObject("subOpeningList", ListForOpeningbalancesOfsubledger);
		    model.addObject("bankOpeningBalanceList", bankOpeningBalanceList);
			model.addObject("supplierOpeningBalanceList", supplierOpeningBalanceList);
			model.addObject("customerOpeningBalanceList", customerOpeningBalanceList);
			model.addObject("bankOpeningBalanceBeforeStartDate", bankOpeningBalanceBeforeStartDate);
		    model.addObject("supplierOpeningBalanceBeforeStartDate", supplierOpeningBalanceBeforeStartDate);
		    model.addObject("customerOpeningBalanceBeforeStartDate", customerOpeningBalanceBeforeStartDate);
			model.addObject("emptyString", new String("---"));
			model.addObject("group1", "Current Assets");
			model.addObject("subGroup2", "Trade Receivables");
			model.addObject("subGroup3", "Cash & Bank Balances");
			model.addObject("group4", "Current Liabilities");
			model.addObject("subGroup5", "Trade Payables");
			model.setViewName("/report/trialBalanceReportList");
		}
		
		return model;
	}
	
	
	
  /*  @RequestMapping(value = "pdfTrialBalanceReport", method = RequestMethod.GET)
    public ModelAndView pdfTrialBalanceReport() {
	 
    	TrialBalanceReportForm form = new TrialBalanceReportForm();
    	
    	form.setCompany(company);
    	form.setFromDate(fromDate);
    	form.setToDate(toDate);
    	form.setCustomerList(customerList);
    	form.setSupplierList(supplierList);
    	form.setBankList(bankList);
    	form.setListForOpeningbalancesbeforestartDate(ListForOpeningbalancesbeforestartDate);
    	form.setListForOpeningbalancesOfsubledger(ListForOpeningbalancesOfsubledger);
     return new ModelAndView("TrailBalancePdfView", "form", form);
    }*/
	 
	@RequestMapping(value="getYearList" , method=RequestMethod.POST)
	public  @ResponseBody List<AccountingYear> getYearList(@RequestParam("id") long id)
	{
		
		Long user_id= null;
		Company company= new Company();
		try{
		company= companyService.findOneWithAll(id);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		for(User user : company.getUser())
		{
			if(user.getRole().getRole_id()==ROLE_CLIENT)
			{
				user_id=user.getUser_id();
			}
		}
		String yearrange = company.getYearRange();
		List<AccountingYear> accountingYear = AccountingYearService.findAccountRange(user_id, yearrange,company.getCompany_id());
		return accountingYear;
		
	}


	@RequestMapping(value = "pdfTrialBalanceReport", method = RequestMethod.GET)
    public ModelAndView downloadExcel() 
	{
		
		TrialBalanceReportForm form = new TrialBalanceReportForm();
    	
    	form.setCompany(company);
    	form.setFromDate(fromDate);
    	form.setToDate(toDate);
    	form.setBankList(bankOpeningBalanceList);
    	form.setBankList(bankOpeningBalanceBeforeStartDate);
    	form.setListForOpeningbalancesbeforestartDate(ListForOpeningbalancesbeforestartDate);
    	form.setListForOpeningbalancesOfsubledger(ListForOpeningbalancesOfsubledger);
    	form.setSupplierList(supplierOpeningBalanceBeforeStartDate);
    	form.setSupplierList(supplierOpeningBalanceList);
    	form.setCustomerList(customerOpeningBalanceBeforeStartDate);
    	form.setCustomerList(customerOpeningBalanceList);

    	return new ModelAndView("PDFBuilderForTrialBalance", "form", form);
     
    }
	

	
	
}