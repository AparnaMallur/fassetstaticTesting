package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Company;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.DepreciationSubledgerDetails;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PayrollSubledgerDetails;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.User;
import com.fasset.form.BillsPayableReportForm;
import com.fasset.form.JournalRegisterDetails;
import com.fasset.form.SubledgerDetailsForm;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IManualJournalVoucherService;

@Controller
@SessionAttributes("user")
public class ManualJVReportController extends MyAbstractController {
	@Autowired
	private ICompanyService companyService;
	@Autowired
	private IManualJournalVoucherService journalService;

	private List<Company> companyList = new ArrayList<Company>();
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;

	@RequestMapping(value = "manualJVReport", method = RequestMethod.GET)
	public ModelAndView manualJVReport(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		Long role = user.getRole().getRole_id();
		ModelAndView model = new ModelAndView();
		BillsPayableReportForm form = new BillsPayableReportForm();

		if (role.equals(ROLE_SUPERUSER)) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("companyList", companyList);
			model.addObject("form", form);
			model.setViewName("/report/manualJVReportForKpo");
		} else if (role.equals(ROLE_EXECUTIVE) || role.equals(ROLE_MANAGER)) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("companyList", companyList);
			model.addObject("form", form);
			model.setViewName("/report/manualJVReportForKpo");
		} else {
			model.addObject("form", form);
			model.setViewName("/report/manualJVReport");
		}
		return model;
	}



	@RequestMapping(value = "showManualJournalReportforKPO", method = RequestMethod.POST)
	public ModelAndView showManualJournalReportforKPO(@ModelAttribute("form") BillsPayableReportForm form,
			BindingResult result, HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		fromDate = form.getFromDate();
		toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(form.getClientId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ManualJournalVoucher> mJVlist = new ArrayList<>();
		List<PayrollAutoJV> pJVlist = new ArrayList<>();
		
		List<JournalRegisterDetails>journalRegisterList= new ArrayList<JournalRegisterDetails>();
		CopyOnWriteArrayList<ManualJournalVoucher> mjvlist = journalService
				.findAllManualJournalVoucherReletedToCompany(form.getClientId(),fromDate,toDate);
		CopyOnWriteArrayList<DepreciationAutoJV> djvlist = journalService
				.findAllDepreciationJournalVoucherReletedToCompany(form.getClientId(),fromDate,toDate);
		CopyOnWriteArrayList<GSTAutoJV> gstjvlist = journalService
				.findAllGstJournalVoucherReletedToCompany(form.getClientId(),fromDate,toDate);
		CopyOnWriteArrayList<PayrollAutoJV> payrolljvlist = journalService
				.findAllPayrollJournalVoucherReletedToCompany(form.getClientId(),fromDate,toDate);
		for(ManualJournalVoucher mjv :mjvlist)
		{
			JournalRegisterDetails jrdetails=new JournalRegisterDetails();
			jrdetails.setDate(mjv.getDate());
			jrdetails.setVoucherType("ManualJV");
			jrdetails.setVoucherNumber(mjv.getVoucher_no());
			List<SubledgerDetailsForm>subList = new ArrayList<SubledgerDetailsForm>();
			for(ManualJVDetails details: mjv.getMjvdetails())
			{
				SubledgerDetailsForm subform= new SubledgerDetailsForm();
			/*	if(details.getSubledgercr()!=null)
				{
					subform.setCreditBalance(details.getCramount());
					subform.setSubledgerName(details.getSubledgercr().getSubledger_name());
				}
				else
				{
					subform.setDebitBalance(details.getDramount());
					subform.setSubledgerName(details.getSubledgerdr().getSubledger_name());
				}
				*/
				
				if(details.getSubledgercr()!=null)
				{
					subform.setCreditBalance(details.getCramount());
					subform.setSubledgerName(details.getSubledgercr().getSubledger_name());
				}
				else if(details.getSubledgerdr()!=null)
				{
					subform.setDebitBalance(details.getDramount());
					subform.setSubledgerName(details.getSubledgerdr().getSubledger_name());
				}else if(details.getSuppliercr()!=null){
					subform.setCreditBalance(details.getCramount());
					subform.setSubledgerName(details.getSuppliercr().getCompany_name());
				}else if(details.getSupplierdr()!=null){
					subform.setDebitBalance(details.getDramount());
					subform.setSubledgerName(details.getSupplierdr().getCompany_name());
				}else if(details.getCustomercr()!=null){
					subform.setCreditBalance(details.getCramount());
					subform.setSubledgerName(details.getCustomercr().getFirm_name());
				}else if(details.getCustomerdr()!=null){
					subform.setDebitBalance(details.getDramount());
					subform.setSubledgerName(details.getCustomerdr().getFirm_name());
				}
				subList.add(subform);
			}
			jrdetails.setSubList(subList);
			journalRegisterList.add(jrdetails);
		}
		for(DepreciationAutoJV djv : djvlist)
		{
			JournalRegisterDetails jrdetails=new JournalRegisterDetails();
			jrdetails.setDate(djv.getDate());
			jrdetails.setVoucherType("DepreciationJV");
			jrdetails.setVoucherNumber(djv.getVoucherSeries().getVoucher_no());
			List<SubledgerDetailsForm>subList = new ArrayList<SubledgerDetailsForm>();
			for(DepreciationSubledgerDetails dtails : djv.getDepriciationSubledgerDetails())
			{
				SubledgerDetailsForm subform= new SubledgerDetailsForm();
				subform.setSubledgerName(dtails.getSubledger().getSubledger_name());
				subform.setDebitBalance(dtails.getSubLedgerAmount());
				subList.add(subform);
			}
			jrdetails.setSubList(subList);
			journalRegisterList.add(jrdetails);
		}
		for(GSTAutoJV gjv : gstjvlist)
		{
			JournalRegisterDetails jrdetails=new JournalRegisterDetails();
			jrdetails.setDate(gjv.getCreated_date());
			jrdetails.setVoucherType("GSTJV");
			jrdetails.setVoucherNumber(gjv.getVoucherSeries().getVoucher_no());
			List<SubledgerDetailsForm>subList = new ArrayList<SubledgerDetailsForm>();
			SubledgerDetailsForm form1 = new SubledgerDetailsForm();
			SubledgerDetailsForm form2 = new SubledgerDetailsForm();
			SubledgerDetailsForm form3 = new SubledgerDetailsForm();
			SubledgerDetailsForm form4 = new SubledgerDetailsForm();
			SubledgerDetailsForm form5 = new SubledgerDetailsForm();
			SubledgerDetailsForm form6 = new SubledgerDetailsForm();
			form1.setSubledgerName("INPUT IGST");
			form1.setCreditBalance(gjv.getInputIgstBalance());
			form2.setSubledgerName("INPUT CGST");
			form2.setCreditBalance(gjv.getInputCgstBalance());
			form3.setSubledgerName("INPUT SGST");
			form3.setCreditBalance(gjv.getInputSgstBalance());
			
			form4.setSubledgerName("OUTPUT IGST");
			form4.setDebitBalance(gjv.getOutputIgstBalance());
			form5.setSubledgerName("OUTPUT CGST");
			form5.setDebitBalance(gjv.getOutputCgstbalance());
			form6.setSubledgerName("OUTPUT SGST");
			form6.setDebitBalance(gjv.getOutputSgstBalance());
			subList.add(form1);
			subList.add(form2);
			subList.add(form3);
			subList.add(form4);
			subList.add(form5);
			subList.add(form6);
			jrdetails.setSubList(subList);
			journalRegisterList.add(jrdetails);
		}
		for(PayrollAutoJV pjv : payrolljvlist)
		{
			JournalRegisterDetails jrdetails=new JournalRegisterDetails();
			jrdetails.setDate(pjv.getDate());
			jrdetails.setVoucherType("PayrollJV");
			jrdetails.setVoucherNumber(pjv.getVoucherSeries().getVoucher_no());
			List<SubledgerDetailsForm>subList = new ArrayList<SubledgerDetailsForm>();
			for(PayrollSubledgerDetails details : pjv.getPayrollSubledgerDetails())
			{
				SubledgerDetailsForm subform = new SubledgerDetailsForm();
				if(details.getCrAmount()!=null && details.getCrAmount()!=0)
				{
					subform.setCreditBalance(details.getCrAmount());
				}
				else
				{
					subform.setDebitBalance(details.getDrAmount());
				}
				subform.setSubledgerName(details.getSubledgerName());
				subList.add(subform);
			}
			jrdetails.setSubList(subList);
			journalRegisterList.add(jrdetails);
				
		}
		Collections.sort(journalRegisterList, new Comparator<JournalRegisterDetails>() {
	        public int compare(JournalRegisterDetails o1, JournalRegisterDetails o2) {

	        	LocalDate Date1 = o1.getDate();
	        	LocalDate Date2= o2.getDate();
	            return Date1.compareTo(Date2);
	    }});
		 
		/*
		 * CopyOnWriteArrayList<ManualJournalVoucher> list = journalService
		 * .findAllManualJournalVoucherReletedToCompany(form.getClientId(),fromDate,
		 * toDate); for (ManualJournalVoucher mjv : list) {
		 * mjv.setDetailList(journalService.getAllMJVDetails(mjv.getJournal_id()));
		 * mJVlist.add(mjv); }
		 */
		
		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.addObject("journalRegisterList", journalRegisterList);
		model.setViewName("/report/manualJVReportList");
		

		return model;
	}

	@RequestMapping(value = "showManualJournalReport", method = RequestMethod.POST)
	public ModelAndView showManualJournalReport(@ModelAttribute("form") BillsPayableReportForm form,
			BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
		fromDate = form.getFromDate();
		toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(company_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<JournalRegisterDetails>journalRegisterList= new ArrayList<JournalRegisterDetails>();
		CopyOnWriteArrayList<ManualJournalVoucher> mjvlist = journalService
				.findAllManualJournalVoucherReletedToCompany(company_id,fromDate,toDate);
		CopyOnWriteArrayList<DepreciationAutoJV> djvlist = journalService
				.findAllDepreciationJournalVoucherReletedToCompany(company_id,fromDate,toDate);
		CopyOnWriteArrayList<GSTAutoJV> gstjvlist = journalService
				.findAllGstJournalVoucherReletedToCompany(company_id,fromDate,toDate);
		CopyOnWriteArrayList<PayrollAutoJV> payrolljvlist = journalService
				.findAllPayrollJournalVoucherReletedToCompany(company_id,fromDate,toDate);
		for(ManualJournalVoucher mjv :mjvlist)
		{
			JournalRegisterDetails jrdetails=new JournalRegisterDetails();
			jrdetails.setDate(mjv.getDate());
			jrdetails.setVoucherType("ManualJV");
			jrdetails.setVoucherNumber(mjv.getVoucher_no());
			List<SubledgerDetailsForm>subList = new ArrayList<SubledgerDetailsForm>();
			for(ManualJVDetails details: mjv.getMjvdetails())
			{
				SubledgerDetailsForm subform= new SubledgerDetailsForm();
				if(details.getSubledgercr()!=null)
				{
					subform.setCreditBalance(details.getCramount());
					subform.setSubledgerName(details.getSubledgercr().getSubledger_name());
				}
				else if(details.getSubledgerdr()!=null)
				{
					subform.setDebitBalance(details.getDramount());
					subform.setSubledgerName(details.getSubledgerdr().getSubledger_name());
				}else if(details.getSuppliercr()!=null){
					subform.setDebitBalance(details.getCramount());
					subform.setSubledgerName(details.getSuppliercr().getCompany_name());
				}else if(details.getSupplierdr()!=null){
					subform.setDebitBalance(details.getDramount());
					subform.setSubledgerName(details.getSupplierdr().getCompany_name());
				}else if(details.getCustomercr()!=null){
					subform.setDebitBalance(details.getCramount());
					subform.setSubledgerName(details.getCustomercr().getFirm_name());
				}else if(details.getCustomerdr()!=null){
					subform.setDebitBalance(details.getDramount());
					subform.setSubledgerName(details.getCustomerdr().getFirm_name());
				}
				subList.add(subform);
			}
			jrdetails.setSubList(subList);
			journalRegisterList.add(jrdetails);
		}
		for(DepreciationAutoJV djv : djvlist)
		{
			JournalRegisterDetails jrdetails=new JournalRegisterDetails();
			jrdetails.setDate(djv.getDate());
			jrdetails.setVoucherType("DepreciationJV");
			jrdetails.setVoucherNumber(djv.getVoucherSeries().getVoucher_no());
			List<SubledgerDetailsForm>subList = new ArrayList<SubledgerDetailsForm>();
			for(DepreciationSubledgerDetails dtails : djv.getDepriciationSubledgerDetails())
			{
				SubledgerDetailsForm subform= new SubledgerDetailsForm();
				subform.setSubledgerName(dtails.getSubledger().getSubledger_name());
				subform.setDebitBalance(dtails.getSubLedgerAmount());
				subList.add(subform);
			}
			jrdetails.setCreditBalance( djv.getAmount());
			jrdetails.setSubList(subList);
			journalRegisterList.add(jrdetails);
		}
		for(GSTAutoJV gjv : gstjvlist)
		{
			JournalRegisterDetails jrdetails=new JournalRegisterDetails();
			jrdetails.setDate(gjv.getCreated_date());
			jrdetails.setVoucherType("GSTJV");
			jrdetails.setVoucherNumber(gjv.getVoucherSeries().getVoucher_no());
			List<SubledgerDetailsForm>subList = new ArrayList<SubledgerDetailsForm>();
			SubledgerDetailsForm form1 = new SubledgerDetailsForm();
			SubledgerDetailsForm form2 = new SubledgerDetailsForm();
			SubledgerDetailsForm form3 = new SubledgerDetailsForm();
			SubledgerDetailsForm form4 = new SubledgerDetailsForm();
			SubledgerDetailsForm form5 = new SubledgerDetailsForm();
			SubledgerDetailsForm form6 = new SubledgerDetailsForm();
			form1.setSubledgerName("INPUT IGST");
			form1.setCreditBalance(gjv.getInputIgstBalance());
			form2.setSubledgerName("INPUT CGST");
			form2.setCreditBalance(gjv.getInputCgstBalance());
			form3.setSubledgerName("INPUT SGST");
			form3.setCreditBalance(gjv.getInputSgstBalance());
			
			form4.setSubledgerName("OUTPUT IGST");
			form4.setDebitBalance(gjv.getOutputIgstBalance());
			form5.setSubledgerName("OUTPUT CGST");
			form5.setDebitBalance(gjv.getOutputCgstbalance());
			form6.setSubledgerName("OUTPUT SGST");
			form6.setDebitBalance(gjv.getOutputSgstBalance());
			subList.add(form1);
			subList.add(form2);
			subList.add(form3);
			subList.add(form4);
			subList.add(form5);
			subList.add(form6);
			jrdetails.setSubList(subList);
			journalRegisterList.add(jrdetails);
		}
		for(PayrollAutoJV pjv : payrolljvlist)
		{
			JournalRegisterDetails jrdetails=new JournalRegisterDetails();
			jrdetails.setDate(pjv.getDate());
			jrdetails.setVoucherType("PayrollJV");
			jrdetails.setVoucherNumber(pjv.getVoucherSeries().getVoucher_no());
			List<SubledgerDetailsForm>subList = new ArrayList<SubledgerDetailsForm>();
			for(PayrollSubledgerDetails details : pjv.getPayrollSubledgerDetails())
			{
				SubledgerDetailsForm subform = new SubledgerDetailsForm();
				if(details.getCrAmount()!=null && details.getCrAmount()!=0)
				{
					subform.setCreditBalance(details.getCrAmount());
				}
				else
				{
					subform.setDebitBalance(details.getDrAmount());
				}
				subform.setSubledgerName(details.getSubledgerName());
				subList.add(subform);
			}
			jrdetails.setSubList(subList);
			journalRegisterList.add(jrdetails);
				
		}
		Collections.sort(journalRegisterList, new Comparator<JournalRegisterDetails>() {
	        public int compare(JournalRegisterDetails o1, JournalRegisterDetails o2) {

	        	LocalDate Date1 = o1.getDate();
	        	LocalDate Date2= o2.getDate();
	            return Date1.compareTo(Date2);
	    }});
		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.addObject("journalRegisterList", journalRegisterList);
		model.setViewName("/report/manualJVReportList");
		return model;
	}

}
