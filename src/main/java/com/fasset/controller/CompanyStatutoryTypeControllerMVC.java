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

import com.fasset.controller.validators.CompanyStatutoryTypeValidator;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ICompanyStatutoryTypeService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class CompanyStatutoryTypeControllerMVC {

	@Autowired
	private CompanyStatutoryTypeValidator  typeValidator ;
	
	@Autowired
	private ICompanyStatutoryTypeService  typeService ;
	
	@RequestMapping(value = "companystatutorytype", method = RequestMethod.GET)
	public ModelAndView companyStatutoryType() {
		ModelAndView model = new ModelAndView();
		model.addObject("type", new CompanyStatutoryType());
		model.setViewName("/master/compstattype");
		return model;
	}
	
	@RequestMapping(value = "savecompanystatutorytype", method = RequestMethod.POST)
	public ModelAndView saveCompanyStatutoryType(@ModelAttribute("type")CompanyStatutoryType type, BindingResult result,HttpServletRequest request, 
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		ModelAndView model = new ModelAndView();
		User user = new User();		
		user = (User) session.getAttribute("user");
		typeValidator.validate(type, result);
		if(result.hasErrors()){	
			model.setViewName("/master/compstattype");
			return model;
			
		}
		else{
			String msg = "";
			try
			{
			if(type.getCompany_statutory_id()!=null)
			{
			if(type.getCompany_statutory_id() > 0){
				type.setUpdated_by(user);
				typeService.update(type);
				msg = "Company statutory type updated successfully";
				
			}
			}
			
			else{
				type.setCreated_by(user);
				String remoteAddr = "";
				 remoteAddr = request.getHeader("X-FORWARDED-FOR");
		            if (remoteAddr == null || "".equals(remoteAddr)) {
		                remoteAddr = request.getRemoteAddr();
		            }
		            type.setIp_address(remoteAddr);

				msg = typeService.saveCompanyStatutoryType(type);
			}
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/companystatutorytypeList");		
		}
	}
	
	
	@RequestMapping(value = "companystatutorytypeList", method = RequestMethod.GET)
	public ModelAndView companyStatutoryTypeList(@RequestParam(value = "msg", required = false)String msg,
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("CompStatTypeList", typeService.findAllListing());
		model.setViewName("/master/compstattypeList");
		return model;
	}
	
	@RequestMapping(value = "viewCompanyStatutoryType", method = RequestMethod.GET)
	public ModelAndView viewCompanyStatutoryType(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		try {
			model.addObject("type",typeService.getById(id));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.setViewName("/master/compstattypeView");
		return model;
	}
	
	@RequestMapping(value = "editCompanyStatutoryType", method = RequestMethod.GET)
	public ModelAndView editCompanyStatutoryType(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		try {
			model.addObject("type", typeService.getById(id));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.setViewName("/master/compstattype");
		return model;
	}
	
	@RequestMapping(value ="deleteCompanyStatutoryType", method = RequestMethod.GET)
	public ModelAndView deleteCompanyStatutoryType(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg",  typeService.deleteByIdValue(id));
		return new ModelAndView("redirect:/companystatutorytypeList");
		
		
	}
}
