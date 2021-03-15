package com.fasset.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.ExecutiveTimesheetValidator;
import com.fasset.entities.Company;
import com.fasset.entities.ExecutiveTimesheet;
import com.fasset.entities.User;
import com.fasset.service.interfaces.IClientAllocationToKpoExecutiveService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IExecutiveTimesheetService;
import com.fasset.service.interfaces.IServiceMaster;
import com.fasset.service.interfaces.IUserService;
/**
 * @author Vijay Ghodake
 *
 * deven infotech pvt ltd.
 */

@Controller
@SessionAttributes("user")
public class ExecutiveTimeSheetController extends MyAbstractController{
	
	@Autowired
	private ExecutiveTimesheetValidator executiveTimesheetValidator;

	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private IExecutiveTimesheetService executiveTimesheetService ;
	
	@Autowired
	private IUserService userService ;
	
	@Autowired
	private IServiceMaster serviceService ;
	
	@Autowired
	private IClientAllocationToKpoExecutiveService clientAllocationService;

	@RequestMapping(value ="executivetimesheet", method = RequestMethod.GET)
	public ModelAndView executivetimesheet( 
			HttpServletRequest request, 
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		List<Company> companyList = new ArrayList<Company>();
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.getApprovedCompanies();
		}
		if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getApprovedCompanies(user.getUser_id());
		}

		model.addObject("executiveTimesheet", new ExecutiveTimesheet());
		model.addObject("userList", userService.list());
		model.addObject("serviceList", serviceService.findAll());
		model.addObject("companyList", companyList);
		model.setViewName("/master/executiveTimesheet");
		return model;
	}
	
	@RequestMapping(value = "saveexecutivetimesheet", method = RequestMethod.POST)
	public ModelAndView saveexecutivetimesheet(@ModelAttribute("executiveTimesheet")ExecutiveTimesheet executiveTimesheet, 
			BindingResult result, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		ModelAndView model = new ModelAndView();
		executiveTimesheetValidator.validate(executiveTimesheet, result);
		if(result.hasErrors()){
			if (role == ROLE_SUPERUSER) {
				model.addObject("companyList", companyService.getApprovedCompanies());
			}
			if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				model.addObject("companyList", companyService.getApprovedCompanies(user.getUser_id()));
			}
			
			model.addObject("userList", userService.list());
			model.addObject("serviceList", serviceService.findAll());
			model.setViewName("/master/executiveTimesheet");
		}
		else{
			String msg = "";
			try{
				if(executiveTimesheet.getTimesheet_id()!=null){					
					executiveTimesheet.setUser(user);
					executiveTimesheetService.update(executiveTimesheet);
					msg = "Timesheet updated successfully";
				}			
				else{
					executiveTimesheet.setUser(user);
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            executiveTimesheet.setIp_address(remoteAddr);
					msg = executiveTimesheetService.saveExecutiveTimesheet(executiveTimesheet);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/executiveTimesheetList");
		}
		return model;
	}
	
	@RequestMapping(value = "executiveTimesheetList", method = RequestMethod.GET)
	public ModelAndView executiveTimesheetList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		if(session.getAttribute("successMsg") != null){
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if(session.getAttribute("errorMsg") != null){
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		model.addObject("executiveTimesheetList", executiveTimesheetService.findByUserId(user.getUser_id()));
		model.setViewName("/master/executiveTimesheetList");
		return model;
	}
	
	
	
	@RequestMapping(value = "viewExecutiveTimesheet", method = RequestMethod.GET)
	public ModelAndView viewService(@RequestParam("date") String date,
			HttpServletRequest request, 
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();		
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		model.addObject("executiveTimesheet",executiveTimesheetService.findByDate(new LocalDate(date), user.getUser_id()));
		model.setViewName("/master/executiveTimesheetView");
		return model;
	}
	
	@RequestMapping(value = "editExecutiveTimesheet", method = RequestMethod.GET)
	public ModelAndView editExecutiveTimesheet(@RequestParam("date") String date, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		model.addObject("serviceList", serviceService.findAll());
		model.addObject("companyList", clientAllocationService.findCompanyByExecutiveId(user.getUser_id()));
		model.addObject("executiveTimesheet", executiveTimesheetService.findByDate(new LocalDate(date), user.getUser_id()));
		model.setViewName("/master/executiveTimesheet");
		return model;
	}
	
	@RequestMapping(value = "deleteExecutiveTimesheet", method = RequestMethod.GET)
	public ModelAndView deleteExecutiveTimesheet(@RequestParam(value = "id", required = false) long id, 
			@RequestParam(value = "date", required = false) LocalDate date,
			HttpServletRequest request, 
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		String msg="";
		if(id > 0) {
			msg=executiveTimesheetService.deleteByIdValue(id);			
		}
		if(date != null) {
			msg=executiveTimesheetService.deleteByDate(date, user.getUser_id());	
		}
		session.setAttribute("successMsg", msg);
		model.setViewName("redirect:/executiveTimesheetList");
		return model;
	}
}
