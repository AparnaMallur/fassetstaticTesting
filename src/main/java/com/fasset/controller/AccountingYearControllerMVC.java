/**
 * mayur suramwar
 */
package com.fasset.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.validators.AccountingYearValidator;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IAccountingYearService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class AccountingYearControllerMVC {

	@Autowired
	private AccountingYearValidator accountingYearValidator ;
	
	@Autowired
	private IAccountingYearService accountingYearService ;
	
	@RequestMapping(value = "accountingYear", method = RequestMethod.GET)
	public ModelAndView accountingYear() {
		ModelAndView model = new ModelAndView();
		model.addObject("year", new AccountingYear());
		model.setViewName("/master/accountingYear");
		return model;
	}
	
	@RequestMapping(value = "saveAccountingYear", method = RequestMethod.POST)
	public ModelAndView saveAccountingYear(@ModelAttribute("year")AccountingYear year, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		accountingYearValidator.validate(year, result);
		if(result.hasErrors()){
			model.setViewName("/master/accountingYear");			
			return model;
		}
		else{
			String msg = "";
			try
			{
				if(year.getYear_id()!=null)
				{
				if(year.getYear_id() > 0){
					year.setUpdated_by(user);
					accountingYearService.update(year);
					msg = "Accounting Year updated successfully";					
				}
			}			
			else{
				year.setCreated_by(user);
				
				String remoteAddr = "";
				 remoteAddr = request.getHeader("X-FORWARDED-FOR");
		            if (remoteAddr == null || "".equals(remoteAddr)) {
		                remoteAddr = request.getRemoteAddr();
		            }
		         year.setIp_address(remoteAddr);
				msg = accountingYearService.saveAccountingYear(year);
			}
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			model.addObject("yearList", accountingYearService.findAllListing());
			model.addObject("successMsg", msg);
			model.setViewName("/master/accountingYearList");			
			return model;
		}
	}
	
	@RequestMapping(value = "accountingYearList", method = RequestMethod.GET)
	public ModelAndView accountingYearList() {
		ModelAndView model = new ModelAndView();
		model.addObject("yearList", accountingYearService.findAllListing());
		model.setViewName("/master/accountingYearList");
		return model;
	}
	
	@RequestMapping(value = "editAccountingYear", method = RequestMethod.GET)
	public ModelAndView editAccountingYear(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		AccountingYear year = new AccountingYear();
		try {
			if(id > 0){
				year = accountingYearService.getById(id);
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}		
		model.addObject("year", year);
		model.setViewName("/master/accountingYear");
		return model;
	}
	
	@RequestMapping(value ="deleteAccountingYear", method = RequestMethod.GET)
	public ModelAndView deleteAccountingYear(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		String msg="";
	    msg = accountingYearService.deleteByIdValue(id);
		model.addObject("yearList", accountingYearService.findAllListing());
		model.addObject("successMsg", msg);
		model.setViewName("/master/accountingYearList");
		return model;
	}
	
////////View///////
@RequestMapping(value = "viewAccountingYear", method = RequestMethod.GET)
public ModelAndView viewAccountingYear(@RequestParam("id") long id) {
	ModelAndView model = new ModelAndView();
	try {
		model.addObject("year", accountingYearService.getById(id));
	} catch (MyWebException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	model.setViewName("/master/accountingYearView");
	return model;
}
}
