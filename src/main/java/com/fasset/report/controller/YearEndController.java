package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.data.time.Year;
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
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.City;
import com.fasset.entities.Company;
import com.fasset.entities.Ledger;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.SubLedger;
import com.fasset.entities.YearEndJV;
import com.fasset.entities.YearEndJvSubledgerDetails;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.LedgerForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.OpeningBalancesOfSubedgerForm;
import com.fasset.form.ProfitAndLossReportForm;
import com.fasset.service.interfaces.IAccountGroupService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPayrollAutoJVService;
import com.fasset.service.interfaces.ISubLedgerService;

@Controller
@SessionAttributes("user")
public class YearEndController extends MyAbstractController{
	
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
	private IPayrollAutoJVService payroll_service;
	
	private List<AccountGroup> accList = new ArrayList<AccountGroup>();
	private List<Ledger> legderList = new ArrayList<Ledger>();
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesOfsubledger = new ArrayList<OpeningBalancesOfSubedgerForm>();
	private Company company;
	private LocalDate fromDate;
	private LocalDate toDate;
	private List<OpeningBalancesForm>  subledgerList = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>  subledgerListbeforestartDate = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesbeforestartDate = new ArrayList<OpeningBalancesOfSubedgerForm>();
	
	@RequestMapping(value = "yearEndAutoJV", method = RequestMethod.GET)
	public ModelAndView yearEndAutoJV(HttpServletRequest request,HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		subledgerList.clear();
		legderList.clear();
		subledgerListbeforestartDate.clear();
		ListForOpeningbalancesbeforestartDate.clear();
		ListForOpeningbalancesOfsubledger.clear();

		accList = accountGroupService.findAll();
		legderList = ledgerService.findLedgersOnlyOfComapany(company_id);
		try {
			System.out.println("The company id in yearEndJV"+company_id);
			company = companyService.getCompanyWithCompanyStautarType(company_id);
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018
		String march31stDate=null;
		march31stDate= currentyear+"-"+"03"+"-"+"31";
		LocalDate date = new LocalDate(march31stDate);
		System.out.println("The op start date "+ company.getOpeningbalance_date());
		System.out.println("The  date "+ date);
		System.out.println("The  company id "+ company_id);
		//-------------Calculation for opening balance before start date-----------------
		for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(company.getOpeningbalance_date(), date, company_id,false))
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
					if(sub.getSubledger_Id()==4190){
						System.out.println("OPbalancesforSubledger sub name "+bal);
				System.out.println("OPbalancesforSubledger sub name "+sub.getSubledger_name());
				System.out.println("OPbalancesforSubledger credit bal "+ (double)bal[2]);
				System.out.println("OPbalancesforSubledger debit bal "+ (double)bal[1]);
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
			System.out.println("Openingbalances beforestartDate total credit bal "+ Totalcredit_balance);
			System.out.println("Opening balancesbeforestartDate total debit bal "+ Totaldebit_balance);
			System.out.println("Opening balancesbeforestartDate total ledgerFormList "+ ledgerFormList.size());
			
			sublederform.setTotalcredit_balance(Totalcredit_balance);
			sublederform.setTotaldebit_balance(Totaldebit_balance);
			sublederform.setLedgerformlist(ledgerFormList);
			sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
			ListForOpeningbalancesbeforestartDate.add(sublederform);
			
			
		}
		
		//-------------Calculation for opening balance between start date and end date-----------------
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(company.getOpeningbalance_date(), date, company_id,true))
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
					System.out.println("OPbalances sub name "+sub.getSubledger_name());
					System.out.println("OPbalances credit bal "+ (double)bal[2]);
					System.out.println("OPbalance debit bal "+ (double)bal[1]);
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
			//System.out.println("OpeningbalancesOfsubledger  sublederform"+ sublederform.getLedgerformlist();
			System.out.println("OpeningbalancesOfsubledger total credit bal "+ Totalcredit_balance);
			System.out.println("OpeningbalancesOfsubledger total debit bal "+ Totaldebit_balance);
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
		fromDate=company.getOpeningbalance_date();
		toDate =date; /// new LocalDate();
		for(OpeningBalancesOfSubedgerForm op:ListForOpeningbalancesOfsubledger){
			System.out.println("Subledger name "+op.getTotaldebit_balance());
			System.out.println("Subledger name "+op.getTotalcredit_balance());
		}
		model.addObject("company",company);
		model.addObject("from_date",fromDate);
		model.addObject("to_date", toDate);
		model.addObject("emptyString", new String("---"));
		model.addObject("subOpeningList", ListForOpeningbalancesOfsubledger);
		model.addObject("subOpeningListBeforeStartDate", ListForOpeningbalancesbeforestartDate);
		model.addObject("yearend", new YearEndJV());
		model.setViewName("/report/yearEndAutoJV");
		
		return model;
	}
	
	
	@RequestMapping(value = "saveYearEndAutoJV", method = RequestMethod.GET)
    public ModelAndView saveYearEndAutoJV() {
System.out.println("saving year end auto jv");
System.out.println(ListForOpeningbalancesbeforestartDate.size());
		ProfitAndLossReportForm form = new ProfitAndLossReportForm();
		form.setListForOpeningbalancesOfsubledger(ListForOpeningbalancesOfsubledger);
		form.setCompany(company);
    	form.setFromDate(fromDate);
    	form.setToDate(toDate);
    	form.setListForOpeningbalancesbeforestartDate(ListForOpeningbalancesbeforestartDate);
    	payroll_service.saveYearEndAutoJV(form);
    	return new ModelAndView("redirect:/yearEndAutoJVList");
    }
	
	@RequestMapping(value = "yearEndAutoJVList", method = RequestMethod.GET)
	public ModelAndView yearEndAutoJVList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		Long role = (long) session.getAttribute("role");
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
	if (role.equals(ROLE_SUPERUSER) || role.equals(ROLE_EXECUTIVE)|| role.equals(ROLE_MANAGER)) 
	{
		model.addObject("yearEndAutoJVList",payroll_service.findAllYearEndVoucherReletedToCompany() );
		}
	else
		{
		model.addObject("yearEndAutoJVList",payroll_service.findAllYearEndVoucherReletedToCompany() );
		}
		
		model.setViewName("/report/yearEndAutoJVList");
		return model;
	}

	@RequestMapping(value = "viewYearEndJournal", method = RequestMethod.GET)
	public ModelAndView viewYearEndJournal(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		List<YearEndJvSubledgerDetails>  crmanualdetail= new ArrayList<YearEndJvSubledgerDetails>();
		List<YearEndJvSubledgerDetails>  drmanualdetail= new ArrayList<YearEndJvSubledgerDetails>();
		YearEndJV  voucher =null;
		try {
			voucher	= payroll_service.getYearEndJVById(id);
		
			model.addObject("yearendjournalVoucher", voucher);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		for(YearEndJvSubledgerDetails detail: payroll_service.getAllYearEndJvSubledgerDetails(id))
		{
			
			if((detail.getSubLedgerAmount()!=null && detail.getSubLedgerAmount()>0) && (detail.getCrSide()==(1) ))
			{
				drmanualdetail.add(detail);
			}
			
			else
			{
				
				crmanualdetail.add(detail);
				
			}

		}
//	
//		Collections.sort(drmanualdetail, new Comparator<YearEndJvSubledgerDetails>() {
//	        public int compare(YearEndJvSubledgerDetails o1, YearEndJvSubledgerDetails o2) {
//
//	        	Long detailid1 = o1.getYearEndJvSubledgerDetails_id();
//	        	Long detailid2= o2.getYearEndJvSubledgerDetails_id();
//	        	return detailid1.compareTo(detailid2);
//
//	          
//	    }});
//		Collections.sort(crmanualdetail, new Comparator<YearEndJvSubledgerDetails>() {
//	        public int compare(YearEndJvSubledgerDetails o1, YearEndJvSubledgerDetails o2) {
//
//	        	Long detailid1 = o1.getYearEndJvSubledgerDetails_id();
//	        	Long detailid2= o2.getYearEndJvSubledgerDetails_id();
//	        	return detailid1.compareTo(detailid2);
//
//	          
//	    }});
		if(voucher.getCreated_by()!=null)
		{
		model.addObject("created_by", voucher.getCreated_by());
		}
		
		model.addObject("mjvDetailList", payroll_service.getAllYearEndJvSubledgerDetails(id));
		model.addObject("drmanualdetail", drmanualdetail);
		model.addObject("crmanualdetail", crmanualdetail);
		model.setViewName("/report/yearendJournalVoucherView");
		return model;
	}
	@RequestMapping(value ="deleteyearendJournal", method = RequestMethod.GET)
	public ModelAndView deleteyearendJournal(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response) 
	{		
		
		HttpSession session = request.getSession(true);
		session.setAttribute("msg", payroll_service.deleteByYearEndJVIdValue(id));
		return new ModelAndView("redirect:/yearEndAutoJVList");
	}
	
	
	
}
