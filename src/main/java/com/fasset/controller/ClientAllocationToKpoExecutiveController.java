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

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.ClientAllocationToKpoExecutiveValidator;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IClientAllocationToKpoExecutiveService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IUserService;

@Controller
@SessionAttributes("user")
public class ClientAllocationToKpoExecutiveController extends MyAbstractController{
	
	@Autowired
	private IClientAllocationToKpoExecutiveService clientAllocationToKpoExecutiveService ;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private ClientAllocationToKpoExecutiveValidator clientAllocationValidator;

	@RequestMapping(value = "clientallocation", method = RequestMethod.GET)
	public ModelAndView clientAllocation(HttpServletRequest request,
			HttpServletResponse response) {
		
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		
		ModelAndView model = new ModelAndView();
		
		if (role == ROLE_SUPERUSER)
		{
			model.addObject("userList", userService.getManagerAndExecutive());
			model.addObject("companyList", companyService.getApprovedCompanies());
		}
        else if(role == ROLE_MANAGER){ 
        	model.addObject("userList", userService.findAllExecutiveOfManager(user.getUser_id()));
        	model.addObject("companyList", clientAllocationToKpoExecutiveService.findAllCompaniesUnderManager(user.getUser_id()));
		}	
		model.addObject("clientAllocationToKpoExecutive", new ClientAllocationToKpoExecutive());
		model.setViewName("/master/clientAllocationToKpoExecutive");
		return model;
		
		
	}
	
	@RequestMapping(value = "clientallocation", method = RequestMethod.POST)
	public ModelAndView clientAllocation(@ModelAttribute ("clientAllocationToKpoExecutive") ClientAllocationToKpoExecutive clientAllocationToKpoExecutive,BindingResult result, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		long role=(long)session.getAttribute("role");
		ModelAndView model = new ModelAndView();
		clientAllocationValidator.validate(clientAllocationToKpoExecutive, result);
		if(result.hasErrors()){
			
			if (role == ROLE_SUPERUSER)
			{
				model.addObject("userList", userService.getManagerAndExecutive());
				model.addObject("companyList", companyService.getApprovedCompanies());
			}
	        else if(role == ROLE_MANAGER){ 
	        	model.addObject("userList", userService.findAllExecutiveOfManager(user.getUser_id()));
	        	model.addObject("companyList", clientAllocationToKpoExecutiveService.findAllCompaniesUnderManager(user.getUser_id()));
			}	
			model.setViewName("/master/clientAllocationToKpoExecutive");
			
		}else{
			String msg = "";
			try
			{
				if(clientAllocationToKpoExecutive.getAllocation_Id()!=null)
				{
					if(clientAllocationToKpoExecutive.getAllocation_Id() > 0) {
						clientAllocationToKpoExecutive.setUpdated_by(user);
						msg = clientAllocationToKpoExecutiveService.updateClientAllocation(clientAllocationToKpoExecutive);
						
					}
				}
				else
				{
					clientAllocationToKpoExecutive.setCreated_by(user);
					
					 String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            clientAllocationToKpoExecutive.setIp_address(remoteAddr);
					msg = clientAllocationToKpoExecutiveService.saveClientAllocationToKpoExecutive(clientAllocationToKpoExecutive);
				}
			}	
			catch(Exception e){
				e.printStackTrace();
			}
			
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/clientAllocationToKpoExecutiveList");
			
		}
		return model;
	}
	
	@RequestMapping(value = "clientAllocationToKpoExecutiveList", method = RequestMethod.GET)
	public ModelAndView clientAllocationToKpoExecutiveList(@RequestParam(value = "msg", required = false)String msg,
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (role == ROLE_SUPERUSER)
		{
		model.addObject("clientAllocationToKpoExecutiveList", clientAllocationToKpoExecutiveService.findAll());
		}
        else if(role == ROLE_MANAGER){ 
        	model.addObject("clientAllocationToKpoExecutiveList", clientAllocationToKpoExecutiveService.findAllCompaniesUnderExecutive(user.getUser_id()));
		}
		model.setViewName("/master/clientAllocationToKpoExecutiveList");
		return model;
	}
	
	@RequestMapping(value = "viewClientAllocation", method = RequestMethod.GET)
	public ModelAndView viewClientAllocation(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		ClientAllocationToKpoExecutive clientAllocationToKpoExecutive = new ClientAllocationToKpoExecutive();
		try {
			clientAllocationToKpoExecutive = clientAllocationToKpoExecutiveService.getById(id);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.addObject("clientAllocationToKpoExecutive",clientAllocationToKpoExecutive);
		model.setViewName("/master/clientAllocationView");
		return model;
	}
	
	@RequestMapping(value = "editClientAllocation", method = RequestMethod.GET)
	public ModelAndView editClientAllocation(@RequestParam("id") long id,HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		ModelAndView model = new ModelAndView();
		ClientAllocationToKpoExecutive clientAllocationToKpoExecutive = new ClientAllocationToKpoExecutive();
		try {
			if(id > 0){
				clientAllocationToKpoExecutive = clientAllocationToKpoExecutiveService.getById(id);
				clientAllocationToKpoExecutive.setCompany_id(clientAllocationToKpoExecutive.getCompany().getCompany_id());
				clientAllocationToKpoExecutive.setUser_id(clientAllocationToKpoExecutive.getUser().getUser_id());
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		if (role == ROLE_SUPERUSER)
		{
			model.addObject("userList", userService.getManagerAndExecutive());
			model.addObject("companyList", companyService.getApprovedCompanies());
		}
        else if(role == ROLE_MANAGER){ 
        	model.addObject("userList", userService.findAllExecutiveOfManager(user.getUser_id()));
        	model.addObject("companyList", companyService.getApprovedCompanies());
		}	
		model.addObject("clientAllocationToKpoExecutive", clientAllocationToKpoExecutive);
		model.setViewName("/master/clientAllocationToKpoExecutive");
		return model;
	}
	
	@RequestMapping(value ="deleteAllocation", method = RequestMethod.GET)
	public ModelAndView deleteAllocation(@RequestParam("id") long id,HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		HttpSession session=request.getSession(true);
	    session.setAttribute("successMsg", clientAllocationToKpoExecutiveService.deleteByIdValue(id));
		model.setViewName("redirect:/clientAllocationToKpoExecutiveList");
		return model;
	}
}
