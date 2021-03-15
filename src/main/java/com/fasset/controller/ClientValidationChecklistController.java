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

import com.fasset.controller.validators.ClientValidationChecklistValidator;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IClientValidationChecklistService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class ClientValidationChecklistController {

	@Autowired
	private IClientValidationChecklistService service ;
	
	@Autowired
	private ClientValidationChecklistValidator validator ;
	
	@RequestMapping(value = "clientValidation", method = RequestMethod.GET)
	public ModelAndView clientValidationChecklist() {
		ModelAndView model = new ModelAndView();
		model.addObject("validation", new ClientValidationChecklist());
		model.setViewName("/KPO/clientValidation");
		return model;
	}
	
	@RequestMapping(value = "saveclientValidation", method = RequestMethod.POST)
	public ModelAndView saveclientValidationChecklist(@ModelAttribute("validation")ClientValidationChecklist validation, BindingResult result,HttpServletRequest request, HttpServletResponse response) {
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		ModelAndView model = new ModelAndView();
		validator.validate(validation, result);
		if(result.hasErrors()){	
			model.setViewName("/KPO/clientValidation");
			return model;
		}
		else{
			String msg = "";
			try
			{
			if(validation.getChecklist_id()!=null)
			{
			if(validation.getChecklist_id() > 0){
				validation.setUpdated_by(user.getUser_id());
				service.update(validation);
				msg = "Client Validation Checklist updated successfully";
				
			  }
			}
			
			else{
				validation.setCreated_by(user.getUser_id());
				String remoteAddr = "";
				 remoteAddr = request.getHeader("X-FORWARDED-FOR");
		            if (remoteAddr == null || "".equals(remoteAddr)) {
		                remoteAddr = request.getRemoteAddr();
		            }
		            validation.setIp_address(remoteAddr);
				msg = service.saveClientValidationChecklist(validation);
			}
			
			}
			catch(Exception e){
				e.printStackTrace();
			}		
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/checklist");	
		}
	}
	
	@RequestMapping(value ="checklist", method = RequestMethod.GET)
	public ModelAndView checklist(@RequestParam(value = "msg", required = false)String msg,
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("checklist", service.findAll());
		model.setViewName("/KPO/ValidationChecklist");
		return model;
	}
	
	@RequestMapping(value = "editChecklist", method = RequestMethod.GET)
	public ModelAndView editChecklist(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		try {
			model.addObject("validation", service.getById(id));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setViewName("/KPO/clientValidation");
		return model;
	}
	
	@RequestMapping(value = "viewChecklist", method = RequestMethod.GET)
	public ModelAndView viewChecklist(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		try {
			model.addObject("validation",service.getById(id));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setViewName("/KPO/checklistView");
		return model;
	}
}
