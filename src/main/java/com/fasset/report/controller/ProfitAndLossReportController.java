package com.fasset.report.controller;

import java.util.ArrayList;
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
import com.fasset.controller.validators.VerticalProfitAndLossReportValidator;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.Company;
import com.fasset.entities.Ledger;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.form.LedgerForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.OpeningBalancesOfSubedgerForm;
import com.fasset.form.ProfitAndLossReportForm;
import com.fasset.service.interfaces.IAccountGroupService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.ISubLedgerService;


@Controller
@SessionAttributes("user")
public class ProfitAndLossReportController extends MyAbstractController{
	
	@Autowired
	private ICompanyService companyService;

	@Autowired
	private IOpeningBalancesService openingBalancesService;
	
	@Autowired
	private ILedgerService ledgerService;
	
	@Autowired
	private IAccountGroupService accountGroupService ;
		
	@Autowired
	private ISubLedgerService subLedgerService;
	
	@Autowired
	private VerticalProfitAndLossReportValidator validator;
	
	private List<Company> companyList = new ArrayList<Company>();
	private List<AccountGroup> accList = new ArrayList<AccountGroup>();
	private List<Ledger> legderList = new ArrayList<Ledger>();
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesOfsubledger = new ArrayList<OpeningBalancesOfSubedgerForm>();
	private Company company;
	private LocalDate fromDate;
	private LocalDate toDate;
	private List<OpeningBalancesForm>  subledgerList = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>  subledgerListbeforestartDate = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesbeforestartDate = new ArrayList<OpeningBalancesOfSubedgerForm>();
	
	@RequestMapping(value = "profitAndLossReport", method = RequestMethod.GET)
	public ModelAndView profitAndLossReport(HttpServletRequest request, HttpServletResponse response) {

	
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");	
		User user = (User)session.getAttribute("user");	
		
		ModelAndView model = new ModelAndView();
		ProfitAndLossReportForm form = new ProfitAndLossReportForm();
		
		if (role == ROLE_SUPERUSER || role == ROLE_MANAGER) {
			//companyList = companyService.list();
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList",companyList);
			model.setViewName("/report/profitAndLossReportForKpo");
		}
		else if(role == ROLE_EXECUTIVE){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList",companyList);
			model.setViewName("/report/profitAndLossReportForKpo");
		} else {
			model.addObject("form", form);
			model.setViewName("/report/profitAndLossReport");
		}
		
		return model;
	}
	@RequestMapping(value = "profitAndLossReport", method = RequestMethod.POST)
	public ModelAndView profitAndLossReport(@ModelAttribute("form") ProfitAndLossReportForm profitAndLossForm,
			HttpServletRequest request, 
			HttpServletResponse response,
	 		BindingResult result) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
			System.out.println("P and L report "+company_id);
		validator.validate(profitAndLossForm, result);
		if(result.hasErrors()){
			model.setViewName("/report/profitAndLossReport");
		}
		else {
			subledgerList.clear();
			legderList.clear();
			subledgerListbeforestartDate.clear();
			ListForOpeningbalancesbeforestartDate.clear();
			ListForOpeningbalancesOfsubledger.clear();

			accList = accountGroupService.findAll();
			legderList = ledgerService.findLedgersOnlyOfComapany(company_id);
			
			//-------------Calculation for opening balance before start date-----------------
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(profitAndLossForm.getFromDate(), profitAndLossForm.getToDate(), company_id,false))
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
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(profitAndLossForm.getFromDate(), profitAndLossForm.getToDate(), company_id,true))
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
					    double ledgerdebit_balance =0;
					    double ledgercredit_balance = 0;
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
						if(!(ledgerdebit_balance==0 && ledgercredit_balance==0))
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
		
			
			try {
				company = companyService.getCompanyWithCompanyStautarType(company_id);
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			fromDate=profitAndLossForm.getFromDate();
			toDate = profitAndLossForm.getToDate();
			
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date", toDate);
			model.addObject("emptyString", new String("---"));
			model.addObject("subOpeningList", ListForOpeningbalancesOfsubledger);
			model.addObject("subOpeningListBeforeStartDate", ListForOpeningbalancesbeforestartDate);
			if(profitAndLossForm.getOption().equals((long)1))
			{
				model.setViewName("/report/horizontalprofitAndLossReportList");
			}
			else
			{
				model.setViewName("/report/verticalprofitAndLossReportList");
			}
		}
		return model;
	}
	
	
	@RequestMapping(value = "profitAndLossReportForKpo", method = RequestMethod.POST)
	public ModelAndView profitAndLossReportForKpo(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("form") ProfitAndLossReportForm profitAndLossForm, BindingResult result) {
		ModelAndView model = new ModelAndView();
		System.out.println("P and L report");
		validator.validate(profitAndLossForm, result);
		if(result.hasErrors()){
			model.addObject("companyList",companyList);
			model.setViewName("/report/profitAndLossReportForKpo");
		}
		else {
			subledgerList.clear();
			legderList.clear();
			subledgerListbeforestartDate.clear();
			ListForOpeningbalancesbeforestartDate.clear();
			ListForOpeningbalancesOfsubledger.clear();
			
			accList = accountGroupService.findAll();
			legderList = ledgerService.findLedgersOnlyOfComapany(profitAndLossForm.getCompanyId());
			
			//-------------Calculation for opening balance before start date-----------------
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(profitAndLossForm.getFromDate(), profitAndLossForm.getToDate(), profitAndLossForm.getCompanyId(),false))
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
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(profitAndLossForm.getFromDate(), profitAndLossForm.getToDate(), profitAndLossForm.getCompanyId(),true))
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
					    double ledgerdebit_balance =0;
					    double ledgercredit_balance = 0;
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
						if(!(ledgerdebit_balance==0 && ledgercredit_balance==0))
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
		
			
			try {
				company = companyService.getCompanyWithCompanyStautarType(profitAndLossForm.getCompanyId());
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			fromDate=profitAndLossForm.getFromDate();
			toDate = profitAndLossForm.getToDate();
			
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date", toDate);
			model.addObject("emptyString", new String("---"));
			model.addObject("subOpeningList", ListForOpeningbalancesOfsubledger);
			model.addObject("subOpeningListBeforeStartDate", ListForOpeningbalancesbeforestartDate);
			if(profitAndLossForm.getOption().equals((long)1))
			{
				model.setViewName("/report/horizontalprofitAndLossReportList");
			}
			else
			{
				model.setViewName("/report/verticalprofitAndLossReportList");
			}
		}
		return model;
	}
	
	@RequestMapping(value = "pdfProfitAndLoss", method = RequestMethod.GET)
    public ModelAndView downloadExcel() {
		System.out.println("PANdLPdf");
		ProfitAndLossReportForm form = new ProfitAndLossReportForm();
		form.setListForOpeningbalancesOfsubledger(ListForOpeningbalancesOfsubledger);
		form.setCompany(company);
    	form.setFromDate(fromDate);
    	form.setToDate(toDate);
    	form.setListForOpeningbalancesbeforestartDate(ListForOpeningbalancesbeforestartDate);
	    return new ModelAndView("HorizontalProfitAndLossPdfView", "form", form);
    
    }
	
}
