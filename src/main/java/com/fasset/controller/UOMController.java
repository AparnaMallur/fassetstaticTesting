package com.fasset.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.validators.UOMValidator;
import com.fasset.entities.UnitOfMeasurement;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IUOMService;

/**
 * @author "Vishwajeet"
 *
 */

@Controller
@SessionAttributes("user")
public class UOMController {
	
	@Autowired
	private IUOMService uomService;
	
	@Autowired
	private UOMValidator oumValidator;
	
	@RequestMapping(value = "uomList", method = RequestMethod.GET)
	public ModelAndView uomList(@RequestParam(value = "msg", required = false)String msg,HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("uomList", uomService.findAllListing());
		model.setViewName("master/uomList");
		return model;
	}
	
	@RequestMapping(value = "uom", method = RequestMethod.GET)
	public ModelAndView uom(){
		ModelAndView model = new ModelAndView();
		model.addObject("uom", new UnitOfMeasurement());
		model.setViewName("master/uom");
		return model;
	}
	
	@RequestMapping(value = "uom", method = RequestMethod.POST)
	public ModelAndView uom(@ModelAttribute("uom") UnitOfMeasurement uom, BindingResult result,HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User)session.getAttribute("user");
		oumValidator.validate(uom, result);
		if(result.hasErrors()){
			model.setViewName("/master/uom");
			return model;
		}
		else{
			String msg = "";
			try{
				if(uom.getUom_id() == null){
					uom.setCreated_by(user);
					
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            uom.setIp_address(remoteAddr);
					uomService.add(uom);
					msg = "Unit of measurement added successfully";
					
				}
				else{
					uom.setUpdated_by(user);
					uomService.update(uom);
					msg = "Unit of measurement updated successfully";
					
				}
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/uomList");		
			
		}
	}
	
	@RequestMapping(value = "viewUOM", method = RequestMethod.GET)
	public ModelAndView viewUOM(@RequestParam("id")Long id){
		ModelAndView model = new ModelAndView();
		try {
			model.addObject("uom", uomService.getById(id));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.setViewName("master/uomView");
		return model;
	}
	
	@RequestMapping(value = "editUOM", method = RequestMethod.GET)
	public ModelAndView editUOM(@RequestParam("id")Long id){
		ModelAndView model = new ModelAndView();
		try {
			model.addObject("uom", uomService.getById(id));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.setViewName("master/uom");
		return model;
	}
	
	@RequestMapping(value = "deleteUOM", method = RequestMethod.GET)
	public ModelAndView deleteUOM(@RequestParam("id")Long id,HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg",  uomService.deleteByIdValue(id));
		return new ModelAndView("redirect:/uomList");
	}
	
}
