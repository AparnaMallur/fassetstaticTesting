package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.HorizontalBalanceSheetReportValidator;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.entities.Ledger;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.form.HorizontalBalanceSheetReportForm;
import com.fasset.form.LedgerForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.OpeningBalancesOfSubedgerForm;
import com.fasset.service.interfaces.IAccountGroupService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;

@Controller
@SessionAttributes("user")
public class HorizontalBalanceSheetReportController extends MyAbstractController{
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private HorizontalBalanceSheetReportValidator horizontalBalanceSheetReportValidator;
	
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
	private List<OpeningBalancesForm>subledgerListbeforestartDate = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>bankOpeningBalanceList =new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>supplierOpeningBalanceList=new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>customerOpeningBalanceList= new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>supplierOpeningBalanceBeforeStartDate =new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>customerOpeningBalanceBeforeStartDate = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>bankOpeningBalanceBeforeStartDate =new ArrayList<OpeningBalancesForm>();
	
	private Company company;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long option;
	private List<OpeningBalancesForm>  subledgerList = new ArrayList<OpeningBalancesForm>();	
	
	@RequestMapping(value = "horizontalBalanceSheetReport", method = RequestMethod.GET)
	public ModelAndView horizontalBalanceSheetReport(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");	
		User user = (User)session.getAttribute("user");	
		
		ModelAndView model = new ModelAndView();
		HorizontalBalanceSheetReportForm form = new HorizontalBalanceSheetReportForm();
		
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList",companyList);
			model.setViewName("/report/horizontalBalanceSheetReportForKpo");
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());			
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/horizontalBalanceSheetReportForKpo");
		}  else {
			model.addObject("form", form);
			model.setViewName("/report/horizontalBalanceSheetReport");
		}
		
		return model;
	}
	
	
	@RequestMapping(value = "horizontalBalanceSheetReport", method = RequestMethod.POST)
	public ModelAndView horizontalBalanceSheetReport(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("form") HorizontalBalanceSheetReportForm balanceSheetForm, BindingResult result) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
			
		horizontalBalanceSheetReportValidator.validate(balanceSheetForm, result);
		if(result.hasErrors()){
			model.setViewName("/report/horizontalBalanceSheetReport");
		}
		else {
			subledgerList.clear();
			subledgerListbeforestartDate.clear();
			legderList.clear();
			bankOpeningBalanceList.clear();
			supplierOpeningBalanceList.clear();
			customerOpeningBalanceList.clear();
			bankOpeningBalanceBeforeStartDate.clear();
			supplierOpeningBalanceBeforeStartDate.clear();
			customerOpeningBalanceBeforeStartDate.clear();
			ListForOpeningbalancesOfsubledger.clear();
			ListForOpeningbalancesbeforestartDate.clear();
			
			
			//-------------Calculation for opening balance before start date-----------------
			
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(balanceSheetForm.getFromDate(), company_id))
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
			
		
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplierBeforeFromDate(balanceSheetForm.getFromDate(), company_id))
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
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomerBeforeFromDate(balanceSheetForm.getFromDate(), company_id))
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
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(balanceSheetForm.getFromDate(), balanceSheetForm.getToDate(), company_id,false))
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
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(balanceSheetForm.getFromDate(), balanceSheetForm.getToDate(), company_id,true))
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
		
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBank(balanceSheetForm.getFromDate(), balanceSheetForm.getToDate(), company_id))
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
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplier(balanceSheetForm.getFromDate(), balanceSheetForm.getToDate(), company_id))
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
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomer(balanceSheetForm.getFromDate(), balanceSheetForm.getToDate(), company_id))
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
			fromDate=balanceSheetForm.getFromDate();
			toDate = balanceSheetForm.getToDate();
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
			if(balanceSheetForm.getOption().equals((long)1))
			{
				model.setViewName("/report/horizontalBalanceSheetReportList");
			}
			else
			{
				model.setViewName("/report/verticalBalanceSheetReportList");
			}
			
		}
		return model;
	}
	
	@RequestMapping(value = "horizontalBalanceSheetReportForKpo", method = RequestMethod.POST)
	public ModelAndView horizontalBalanceSheetReportForKpo(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("form") HorizontalBalanceSheetReportForm balanceSheetForm, BindingResult result) {
		ModelAndView model = new ModelAndView();
		horizontalBalanceSheetReportValidator.validate(balanceSheetForm, result);
		if(result.hasErrors()){
			model.addObject("companyList",companyList);
			model.setViewName("/report/horizontalBalanceSheetReportForKpo");
		}
		else {
			subledgerList.clear();
			subledgerListbeforestartDate.clear();
			legderList.clear();
			bankOpeningBalanceList.clear();
			supplierOpeningBalanceList.clear();
			customerOpeningBalanceList.clear();
			bankOpeningBalanceBeforeStartDate.clear();
			supplierOpeningBalanceBeforeStartDate.clear();
			customerOpeningBalanceBeforeStartDate.clear();
			ListForOpeningbalancesOfsubledger.clear();
			ListForOpeningbalancesbeforestartDate.clear();
			
			
			//-------------Calculation for opening balance before start date-----------------
		
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(balanceSheetForm.getFromDate(), balanceSheetForm.getCompanyId()))
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
			
		
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplierBeforeFromDate(balanceSheetForm.getFromDate(), balanceSheetForm.getCompanyId()))
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
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomerBeforeFromDate(balanceSheetForm.getFromDate(), balanceSheetForm.getCompanyId()))
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
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(balanceSheetForm.getFromDate(), balanceSheetForm.getToDate(), balanceSheetForm.getCompanyId(),false))
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
			legderList = ledgerService.findLedgersOnlyOfComapany(balanceSheetForm.getCompanyId());
			
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
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(balanceSheetForm.getFromDate(), balanceSheetForm.getToDate(), balanceSheetForm.getCompanyId(),true))
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
			
		  /* accList = accountGroupService.findAll();
		   legderList = ledgerService.findLedgersOnlyOfComapany(balanceSheetForm.getCompanyId());*/
		   
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
		
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBank(balanceSheetForm.getFromDate(), balanceSheetForm.getToDate(), balanceSheetForm.getCompanyId()))
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
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplier(balanceSheetForm.getFromDate(), balanceSheetForm.getToDate(), balanceSheetForm.getCompanyId()))
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
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomer(balanceSheetForm.getFromDate(), balanceSheetForm.getToDate(), balanceSheetForm.getCompanyId()))
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
				company = companyService.getCompanyWithCompanyStautarType(balanceSheetForm.getCompanyId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fromDate=balanceSheetForm.getFromDate();
			toDate = balanceSheetForm.getToDate();
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date", toDate);
			model.addObject("subOpeningListBeforeStartDate", ListForOpeningbalancesbeforestartDate);
			model.addObject("subOpeningList", ListForOpeningbalancesOfsubledger);
		    model.addObject("bankOpeningBalanceList", bankOpeningBalanceList);
			model.addObject("supplierOpeningBalanceList", supplierOpeningBalanceList);
		
			model.addObject("bankOpeningBalanceBeforeStartDate", bankOpeningBalanceBeforeStartDate);
		    model.addObject("supplierOpeningBalanceBeforeStartDate", supplierOpeningBalanceBeforeStartDate);
			model.addObject("customerOpeningBalanceList", customerOpeningBalanceList);
		    model.addObject("customerOpeningBalanceBeforeStartDate", customerOpeningBalanceBeforeStartDate);
			model.addObject("emptyString", new String("---"));
			model.addObject("group1", "Current Assets");
			model.addObject("subGroup2", "Trade Receivables");
			model.addObject("subGroup3", "Cash & Bank Balances");
			model.addObject("group4", "Current Liabilities");
			model.addObject("subGroup5", "Trade Payables");
			if(balanceSheetForm.getOption().equals((long)1))
			{
				model.setViewName("/report/horizontalBalanceSheetReportList");
			}
			else
			{
				model.setViewName("/report/verticalBalanceSheetReportList");
			}
		}
		return model;
	}
	@RequestMapping(value = "pdfHorizontalBalanceSheetReport", method = RequestMethod.GET)
    public ModelAndView downloadExcel() {
		HorizontalBalanceSheetReportForm form = new HorizontalBalanceSheetReportForm();
    	form.setCompany(company);
    	form.setFromDate(fromDate);
    	form.setToDate(toDate);
    	form.setListForOpeningbalancesOfsubledger(ListForOpeningbalancesOfsubledger);
    	form.setListForOpeningbalancesbeforestartDate(ListForOpeningbalancesbeforestartDate);
    	form.setCustomerOpeningBalanceList(customerOpeningBalanceList);
    	form.setCustomerOpeningBalanceBeforeStartDate(customerOpeningBalanceBeforeStartDate);
    	form.setSupplierOpeningBalanceList(supplierOpeningBalanceList);
    	form.setSupplierOpeningBalanceBeforeStartDate(supplierOpeningBalanceBeforeStartDate);
    	form.setBankOpeningBalanceList(bankOpeningBalanceList);
    	form.setBankOpeningBalanceBeforeStartDate(bankOpeningBalanceBeforeStartDate);
        return new ModelAndView("HorizontalBalanceSheetReportPdfView", "form", form);
    	
    
    }
	
	
	@RequestMapping(value = "pdfVerticalBalanceSheetReport", method = RequestMethod.GET)
    public ModelAndView downloadExcel1() {
		HorizontalBalanceSheetReportForm form = new HorizontalBalanceSheetReportForm();
    	form.setCompany(company);
    	form.setFromDate(fromDate);
    	form.setToDate(toDate);
    	form.setListForOpeningbalancesOfsubledger(ListForOpeningbalancesOfsubledger);
    	form.setListForOpeningbalancesbeforestartDate(ListForOpeningbalancesbeforestartDate);
    	form.setCustomerOpeningBalanceList(customerOpeningBalanceList);
    	form.setCustomerOpeningBalanceBeforeStartDate(customerOpeningBalanceBeforeStartDate);
    	form.setSupplierOpeningBalanceList(supplierOpeningBalanceList);
    	form.setSupplierOpeningBalanceBeforeStartDate(supplierOpeningBalanceBeforeStartDate);
    	form.setBankOpeningBalanceList(bankOpeningBalanceList);
    	form.setBankOpeningBalanceBeforeStartDate(bankOpeningBalanceBeforeStartDate);
        return new ModelAndView("VerticalBalanceSheetReportPdfView", "form", form);
    	
    
    }

	
}
