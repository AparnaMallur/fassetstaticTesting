package com.fasset.report.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.GSTReport2Validator;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Company;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.User;
import com.fasset.form.ExempListForm;
import com.fasset.form.GSTReport2Form;
import com.fasset.form.HSNReportForm;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IDebitNoteService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IPurchaseEntryService;

@Controller
@SessionAttributes("user")
public class GSTReport2Controller extends MyAbstractController{

	@Autowired
	private IPurchaseEntryService prchaseEntryService;
	
	@Autowired
	private IDebitNoteService debitNoteService ;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IAccountingYearService AccountingYearService;
	
	@Autowired
	private IPaymentService paymentService;
	
	@Autowired
	private GSTReport2Validator validator;
	
	private List<Company> companyList = new ArrayList<Company>();
	private List<PurchaseEntry> getB2BList = new ArrayList<PurchaseEntry>();
	private List<PurchaseEntry> getB2CLList = new ArrayList<PurchaseEntry>();
	private List<PurchaseEntry> getB2CSList = new ArrayList<PurchaseEntry>();
	private List<PurchaseEntry> getExpList = new ArrayList<PurchaseEntry>();
	private List<DebitNote> cdnrList = new ArrayList<DebitNote>();
	private List<DebitNote> cdnurList =  new ArrayList<DebitNote>();
	private List<Payment> getATList =  new ArrayList<Payment>();
	private List<Payment> getATAdjList =  new ArrayList<Payment>();
	private List<HSNReportForm> hsnReportList = new ArrayList<HSNReportForm>();	
	private List<ExempListForm> exempList = new ArrayList<ExempListForm>();
    
	@RequestMapping(value = "gstReport2Input", method = RequestMethod.GET)
	public ModelAndView gstReport2Input(HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		long user_id=(long)session.getAttribute("user_id");
		
		GSTReport2Form form = new GSTReport2Form();
		
		if (role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList",companyList);
			model.setViewName("/report/gstReport2ForKpo");
		} else {			
			String yearrange = user.getCompany().getYearRange();
			List<AccountingYear> accountingYear = AccountingYearService.findAccountRange(user_id, yearrange, user.getCompany().getCompany_id());
			model.addObject("form", form);
			model.addObject("accountingYear",accountingYear);
			model.setViewName("/report/gstReport2");
		}
		
		return model;
	}
	
	@RequestMapping(value = "gstReport2Input", method = RequestMethod.POST)
	public ModelAndView gstReport2Input(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("form") GSTReport2Form form, BindingResult result) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		User user = (User) session.getAttribute("user");
		long user_id=(long)session.getAttribute("user_id");
		
		Company company = new Company();
		try {
			company = companyService.findOneWithAll(company_id);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		validator.validate(form, result);
		
		if(result.hasErrors()){
			String yearrange = user.getCompany().getYearRange();
			List<AccountingYear> accountingYear = AccountingYearService.findAccountRange(user_id, yearrange, user.getCompany().getCompany_id());
			model.addObject("accountingYear",accountingYear);
			model.setViewName("/report/gstReport2");
		}
		else {
			
			getB2BList.clear();
			getB2CLList.clear();
			getB2CSList.clear();
			getExpList.clear();
			cdnrList.clear();
			cdnurList.clear();
			getATList.clear();
			getATAdjList.clear();
			hsnReportList.clear();
			exempList.clear();
			
			getB2BList= prchaseEntryService.getB2BList(form.getMonth(), form.getYearId(), company_id);
			getB2CLList=prchaseEntryService.getB2CLList(form.getMonth(), form.getYearId(), company_id, company.getState().getState_id());
			getB2CSList=prchaseEntryService.getB2CSList(form.getMonth(), form.getYearId(), company_id, company.getState().getState_id());
			getExpList=prchaseEntryService.getExpList(form.getMonth(), form.getYearId(), company_id, company.getCountry().getCountry_id());
			cdnrList= debitNoteService.cdnrList(form.getMonth(), form.getYearId(), company_id);
			cdnurList=debitNoteService.cdnurList(form.getMonth(), form.getYearId(), company_id);
			getATList= paymentService.getATList(form.getMonth(), form.getYearId(), company_id);
		    getATAdjList=paymentService.getATAdjList(form.getMonth(), form.getYearId(), company_id);
		    hsnReportList=prchaseEntryService.getHsnList(form.getMonth(), form.getYearId(), company_id);
		    exempList = prchaseEntryService.getExempList(form.getMonth(), form.getYearId(), company_id);
			
			model.addObject("getB2BList", getB2BList);
			model.addObject("getB2CLList", getB2CLList);
			model.addObject("getB2CSList", getB2CSList);
			model.addObject("getExpList", getExpList);
			model.addObject("cdnrList", cdnrList);
			model.addObject("cdnurList", cdnurList);
			model.addObject("getATList", getATList);
			model.addObject("getATAdjList", getATAdjList);
			model.addObject("hsnReportList", hsnReportList);
			model.addObject("exempList", exempList);
			model.setViewName("/report/gstReport2List");
		}
		return model;
	}
	
	@RequestMapping(value = "gstReport2InputForKpo", method = RequestMethod.POST)
	public ModelAndView gstReport2InputForKpo(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("form") GSTReport2Form form, BindingResult result) {
		ModelAndView model = new ModelAndView();
		Company company = new Company();
		
		try {
			company = companyService.findOneWithAll(form.getCompanyId());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		validator.validate(form, result);
		if(result.hasErrors()){
			model.addObject("companyList",companyList);
			model.setViewName("/report/gstReport2ForKpo");
		}
		else {
			
			getB2BList.clear();
			getB2CLList.clear();
			getB2CSList.clear();
			getExpList.clear();
			cdnrList.clear();
			cdnurList.clear();
			getATList.clear();
			getATAdjList.clear();
			hsnReportList.clear();
			exempList.clear();
			
			getB2BList= prchaseEntryService.getB2BList(form.getMonth(), form.getYearId(), form.getCompanyId());
			getB2CLList=prchaseEntryService.getB2CLList(form.getMonth(), form.getYearId(), form.getCompanyId(), company.getState().getState_id());
			getB2CSList=prchaseEntryService.getB2CSList(form.getMonth(), form.getYearId(), form.getCompanyId(), company.getState().getState_id());
			getExpList=prchaseEntryService.getExpList(form.getMonth(), form.getYearId(), form.getCompanyId(), company.getCountry().getCountry_id());
			cdnrList= debitNoteService.cdnrList(form.getMonth(), form.getYearId(), form.getCompanyId());
			cdnurList=debitNoteService.cdnurList(form.getMonth(), form.getYearId(), form.getCompanyId());
			getATList= paymentService.getATList(form.getMonth(), form.getYearId(), form.getCompanyId());
			getATAdjList=paymentService.getATAdjList(form.getMonth(), form.getYearId(), form.getCompanyId());
		    hsnReportList=prchaseEntryService.getHsnList(form.getMonth(), form.getYearId(), form.getCompanyId()); 
		    exempList = prchaseEntryService.getExempList(form.getMonth(), form.getYearId(), form.getCompanyId());
			
			model.addObject("getB2BList", getB2BList);
			model.addObject("getB2CLList", getB2CLList);
			model.addObject("getB2CSList", getB2CSList);
			model.addObject("getExpList", getExpList);
			model.addObject("cdnrList", cdnrList);
			model.addObject("cdnurList", cdnurList);
			model.addObject("getATList", getATList);
			model.addObject("getATAdjList", getATAdjList);
			model.addObject("hsnReportList", hsnReportList);
			model.addObject("exempList", exempList);
			
			model.setViewName("/report/gstReport2List");
		}
		return model;
	}
	
	@RequestMapping(value = "downloadGSTReport2Excel", method = RequestMethod.GET)
    public ModelAndView downloadGSTReport2Excel() {
		GSTReport2Form form = new GSTReport2Form();
		form.setGetB2BList(getB2BList);
		form.setGetB2CLList(getB2CLList);
		form.setGetB2CSList(getB2CSList);
		form.setGetExpList(getExpList);
		form.setCdnurList(cdnurList);
		form.setCdnrList(cdnrList);
		form.setGetATList(getATList);
		form.setGetATAdjList(getATAdjList);
		form.setHsnReportList(hsnReportList);
		form.setExempList(exempList);
        return new ModelAndView("GSTReport2ExcelView", "form", form);
    }
		
}	






		
	