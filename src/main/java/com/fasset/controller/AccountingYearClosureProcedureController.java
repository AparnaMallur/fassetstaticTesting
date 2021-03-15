package com.fasset.controller;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.data.time.Year;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.EditingLastYearAccountForm;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IMailService;
import com.fasset.service.interfaces.IYearEndingService;

//controller 1
@Controller
@SessionAttributes("user")
public class AccountingYearClosureProcedureController extends MyAbstractController{
	
	@Autowired	
	private IYearEndingService yearService;
	
	@Autowired	
	private IMailService mailSrvice;
	
	@Autowired
	private ICompanyService companyService ;
	
	@RequestMapping(value = "requestForEditingLastYearAccount", method = RequestMethod.GET)
	public ModelAndView requestForEditingLastYearAccount(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model.addObject("form", new EditingLastYearAccountForm());
		model.setViewName("/master/requestForEditingLastYearAccount");
		return model;
	}
	
	
	@RequestMapping(value = "sendRequestForEditingLastYearAccount", method = RequestMethod.POST)
	public ModelAndView sendRequestForEditingLastYearAccount(@ModelAttribute("form")EditingLastYearAccountForm form,HttpServletRequest request, HttpServletResponse response) {
			HttpSession session = request.getSession(true);
			long company_id=(long)session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
		List<YearEnding> list = yearService.findAllYearEnding(company_id);
		String currentyear = new Year().toString();
		String msg = "";
		Boolean flag = false;
		for(YearEnding year : list)
		{
			AccountingYear accYear = year.getAccountingYear();
			String[] accYears = accYear.getYear_range().split("-");// 2017-2018 BECOMES TWO STRING AS 2017 AND 2018
			String currentAccountYr = accYears[1];//
			if(currentyear.equalsIgnoreCase(currentAccountYr) && year.getYearEndingstatus().equals((long)0) && year.getIsMailSent()==false)
			{
				year.setFromDate(form.getFromDate());
				year.setToDate(form.getToDate());
				year.setIsMailSent(true);
				try {
					yearService.update(year);
					msg = "Your request is sent to VDAKPO. Please wait for approval. You will get approval mail soon.";
				} catch (MyWebException e) {
					e.printStackTrace();
				}
				flag=true;
			}
		}
		if(flag==false)
		{
		msg = "You have alredy requested for this.";
		}
		if(msg != null){
			model.addObject("successMsg", msg);
		}
		model.addObject("form", new EditingLastYearAccountForm());
		model.setViewName("/master/requestForEditingLastYearAccount");
		return model;
	}
	
	// Account Year Closure Procedure (Companies which are requested for editing of last transaction year.) For VDAKPO SuperUser
	@RequestMapping(value = "approvalForEditingLastYear", method = RequestMethod.GET)
	public ModelAndView ApprovalForEditingLastYear() {
		ModelAndView model = new ModelAndView();
		List<YearEnding> yearList = new ArrayList<>();
		List<YearEnding> list = yearService.findAllYearEnding();
		String currentyear = new Year().toString();
		
		for(YearEnding year :list)
		{
			AccountingYear accYear = year.getAccountingYear();
			String[] accYears = accYear.getYear_range().split("-");// 2017-2018 BECOMES TWO STRING AS 2017 AND 2018
			String currentAccountYr = accYears[1];//
			
			if(currentyear.equalsIgnoreCase(currentAccountYr)&& year.getIsMailSent()==true && year.getIsApprovedForEditingAccYr()==false)
			{
				yearList.add(year);
			}
		}
		model.addObject("yearList", yearList);
		model.setViewName("/master/approvalForEditingLastYear");
		return model;
	}
	
	
	@RequestMapping(value = "approvalOfCompanyForEditingLastYear", method = RequestMethod.GET)
	public ModelAndView approvalOfCompanyForEditingLastYear(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		
		try {
			YearEnding year = yearService.getById(id);
			year.setYearEndingstatus((long)1);
			year.setIsApprovedForEditingAccYr(true);
			yearService.update(year);
			try {
				mailSrvice.sendLastAccountingYearEditInfoMail(year);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			model.addObject("successMsg", "Account is active now. Can use up to "+year.getToDate().toString()+"."+" "+" A mail is also sent to given user regarding this.");
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		
		List<YearEnding> yearList = new ArrayList<>();
		List<YearEnding> list = yearService.findAllYearEnding();
		String currentyear = new Year().toString();
		
		for(YearEnding year :list)
		{
			AccountingYear accYear = year.getAccountingYear();
			String[] accYears = accYear.getYear_range().split("-");// 2017-2018 BECOMES TWO STRING AS 2017 AND 2018
			String currentAccountYr = accYears[1];
			
			if(currentyear.equalsIgnoreCase(currentAccountYr)&& year.getIsMailSent()==true && year.getIsApprovedForEditingAccYr()==false)
			{
				yearList.add(year);
			}
		}
		model.addObject("yearList", yearList);
		model.setViewName("/master/approvalForEditingLastYear");
		return model;
	}
	
	
	@RequestMapping(value = "subscribedClientList", method = RequestMethod.GET)
	public ModelAndView subscribedClientList(
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");	
		User user = (User)session.getAttribute("user");	
		
		if (role == ROLE_SUPERUSER)
		{
		model.addObject("subscribedClientList", companyService.FindAllInactiveCompanies(role,user.getUser_id()));
		}
       else if(role == ROLE_MANAGER){ 
			
    	   model.addObject("subscribedClientList", companyService.FindAllInactiveCompanies(role,user.getUser_id()));
		}
		else if(role == ROLE_EXECUTIVE){ 
			model.addObject("subscribedClientList", companyService.FindAllInactiveCompanies(role,user.getUser_id()));
		}
		model.setViewName("KPO/subscribedClientList");
		return model;
	}
	
}
