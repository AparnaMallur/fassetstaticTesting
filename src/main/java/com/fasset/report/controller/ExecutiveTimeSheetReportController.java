package com.fasset.report.controller;

import java.util.Collections;
import java.util.Comparator;
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
import com.fasset.entities.User;
import com.fasset.form.DateForm;
import com.fasset.service.interfaces.IExecutiveTimesheetService;
import com.fasset.service.interfaces.IUserService;

@Controller
@SessionAttributes("user")
public class ExecutiveTimeSheetReportController extends MyAbstractController{

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IExecutiveTimesheetService executiveTimesheetService ;
	
	
		@RequestMapping(value = "executiveTimeSheetReport", method = RequestMethod.GET)
		public ModelAndView activityLogReport(HttpServletRequest request, HttpServletResponse response) {
		
			HttpSession session = request.getSession(true);
			long role=(long)session.getAttribute("role");	
			User user = (User)session.getAttribute("user");	
			ModelAndView model = new ModelAndView();
			DateForm form = new DateForm();
			
			if (role == ROLE_SUPERUSER)
			{
			
				List<User> kpoList = userService.getManagerAndExecutive();		
			    kpoList.add(user);
			    Collections.sort(kpoList, new Comparator<User>() {
		            public int compare(User user1, User user2) {
		            return user1.getFirst_name().trim().toLowerCase().compareTo(user2.getFirst_name().trim().toLowerCase());
		            }
		        });
				model.addObject("form", form);
				model.addObject("kpoList", kpoList);
				
			}
			else if(role == ROLE_MANAGER){ 
				
				List<User> kpoList = userService.findAllExecutiveOfManager(user.getUser_id());
				kpoList.add(user);
				  Collections.sort(kpoList, new Comparator<User>() {
			            public int compare(User user1, User user2) {
			            return user1.getFirst_name().trim().toLowerCase().compareTo(user2.getFirst_name().trim().toLowerCase());
			            }
			        });
				model.addObject("form", form);
				model.addObject("kpoList", kpoList);
				
			}
			else if(role == ROLE_EXECUTIVE){ 
				model.addObject("form", form);
				
			}
			model.setViewName("/report/executiveTimeSheetForKpo");
			return model;
		}
		
		@RequestMapping(value = "executiveTimeSheetReport", method = RequestMethod.POST)
		public ModelAndView showSalesReport(@ModelAttribute("form")DateForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
			ModelAndView model = new ModelAndView();
			HttpSession session = request.getSession(true);
			long role=(long)session.getAttribute("role");
			User user = (User)session.getAttribute("user");	
			if (role == ROLE_SUPERUSER || role == ROLE_MANAGER)
			{
				model.addObject("executiveTimesheetList", executiveTimesheetService.findAllTimesheetOfExecutive(form.getUserId(),form.getFromDate(),form.getToDate()));
			}
			else if(role == ROLE_EXECUTIVE){ 
				model.addObject("executiveTimesheetList", executiveTimesheetService.findAllTimesheetOfExecutive(user.getUser_id(),form.getFromDate(),form.getToDate()));
			}
			model.setViewName("/report/executiveTimeSheetReportList");
			return model;
		}
}
