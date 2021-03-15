package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Customer;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.User;
import com.fasset.form.GSTReport1Form;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IReportService;

@Controller
@SessionAttributes("user")
public class GSTReport1Controller extends MyAbstractController{
	
	@Autowired
	private IReportService reportService;

	@Autowired
	private ICompanyService companyService;
	private List<Company> companyList = new ArrayList<Company>();
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;
	
	GSTReport1Form gstReport1Form = new GSTReport1Form(); 
	
	@RequestMapping(value = "gstReport1Input", method = RequestMethod.GET)
	public ModelAndView gstReport1Input(HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		Long role=(long)session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		GSTReport1Form form= new GSTReport1Form();
		
		if (role.equals(ROLE_SUPERUSER)){
			List<Company> companyList = companyService.getAllCompaniesOnly();
			model.addObject("companyList", companyList);
			model.addObject("form", form);
			model.setViewName("report/gstReport1ForKpo");
		}
		else if(role.equals(ROLE_EXECUTIVE) || role.equals(ROLE_MANAGER)){ 
			 List<Company> companyList  = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("companyList", companyList);
			model.addObject("form", form);
			model.setViewName("/report/gstReport1ForKpo");
		}
		else if(role == ROLE_CLIENT){
			model.addObject("form", form);
			model.setViewName("report/gstReport1");
		}
		return model;
	}
	
	@RequestMapping(value = "gstReport1", method = RequestMethod.POST)
		
	public ModelAndView gstReport1(@ModelAttribute("form")GSTReport1Form form ,HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		Set<Customer>customersForB2blist = new HashSet<>();
		Set<Customer>customersForCdnrlist = new HashSet<>();
		    
		gstReport1Form=reportService.getGSTReport1(form.getFromDate(), form.getToDate(), user.getCompany());
		fromDate=form.getFromDate();
		toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());
		} catch (Exception e) {
			e.printStackTrace();
		}
		gstReport1Form.setCompany(company);
		gstReport1Form.setFromDate(fromDate);
		gstReport1Form.setToDate(toDate);
		
		for(SalesEntry entry : gstReport1Form.getB2bList())
		{
			if(entry.getCustomer()!=null)
			{
				customersForB2blist.add(entry.getCustomer());
			}
			
		}
		
		for(CreditNote entry : gstReport1Form.getCdnrList())
		{
			if(entry.getCustomer()!=null)
			{
				customersForCdnrlist.add(entry.getCustomer());
			}
			
		}
		
		gstReport1Form.setNoOfRecipientsForB2bList(customersForB2blist.size());
		gstReport1Form.setNoOfRecipientsForCdnrList(customersForCdnrlist.size());
		
		model.addObject("company",company);
		model.addObject("from_date",fromDate);
		model.addObject("to_date",toDate);
		model.addObject("gstReport1Form", gstReport1Form);
		model.setViewName("/report/gstReport1List");
		return model;
	}
	
	@RequestMapping(value = "gstReport1forKPO", method = RequestMethod.POST)
	public ModelAndView gstReportforKPO(@ModelAttribute("form")GSTReport1Form form , HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();	
		
		    Set<Customer>customersForB2blist = new HashSet<>();
		    Set<Customer>customersForCdnrlist = new HashSet<>();
		    
			fromDate=form.getFromDate();
			toDate = form.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(form.getClientId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			gstReport1Form=reportService.getGSTReport1(form.getFromDate(),form.getToDate(),company);
			gstReport1Form.setCompany(company);
			gstReport1Form.setFromDate(fromDate);
			gstReport1Form.setToDate(toDate);
			
			for(SalesEntry entry : gstReport1Form.getB2bList())
			{
				if(entry.getCustomer()!=null)
				{
					customersForB2blist.add(entry.getCustomer());
				}
				
			}
			
			for(CreditNote entry : gstReport1Form.getCdnrList())
			{
				if(entry.getCustomer()!=null)
				{
					customersForCdnrlist.add(entry.getCustomer());
				}
				
			}
			
			gstReport1Form.setNoOfRecipientsForB2bList(customersForB2blist.size());
			gstReport1Form.setNoOfRecipientsForCdnrList(customersForCdnrlist.size());
			
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date",toDate);
			model.addObject("gstReport1Form", gstReport1Form);
			model.setViewName("/report/gstReport1List");
		
		return model;
	}
	
	
	@RequestMapping(value = "/downloadGSTReport1Excel", method = RequestMethod.GET)
    public ModelAndView downloadGSTReport1Excel() {
        return new ModelAndView("GSTReport1ExcelView", "gstReport1Form", gstReport1Form);
    }

}
