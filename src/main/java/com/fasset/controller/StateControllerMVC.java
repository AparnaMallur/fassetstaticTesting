/**
 * mayur suramwar
 */
package com.fasset.controller;

import java.util.List;
import java.util.Map;

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

import com.fasset.controller.validators.StateValidator;
import com.fasset.entities.Country;
import com.fasset.entities.State;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ICountryService;
import com.fasset.service.interfaces.IStateService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class StateControllerMVC {
    
	@Autowired
	private StateValidator stateValidator ;
	
	@Autowired
	private ICountryService countryService ;
	
	@Autowired
	private IStateService stateService ;
	
	@RequestMapping(value = "state", method = RequestMethod.GET)
	public ModelAndView State() {
		ModelAndView model = new ModelAndView();
		State state = new State();
		List<Country> countryList = countryService.findAll();
		model.addObject("countryList", countryList);
		model.addObject("state", state);
		model.setViewName("/master/state");
		return model;
	}
	
	@RequestMapping(value = "savestate", method = RequestMethod.POST)
	public ModelAndView saveState(@ModelAttribute("state")State state, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		
		stateValidator.validate(state, result);
		HttpSession session = request.getSession(true);
		User user = new User();
		user = (User) session.getAttribute("user");
		ModelAndView model = new ModelAndView();
		if(result.hasErrors()){
			
			model.addObject("countryList", countryService.findAll());
			model.setViewName("/master/state");
			return model;
		}
		else{
			String msg = "";
			try{
				if(state.getState_id()!=null){
					long id = state.getState_id();
					if(id > 0){
						state.setUpdated_by(user);
						stateService.update(state);
						msg = "State updated successfully";						
					}
				}				
				else{
					
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            state.setIp_address(remoteAddr);
					state.setCreated_by(user);
					msg = stateService.saveState(state);
				}			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/stateList");		
		}
	}
	
	@RequestMapping(value = "viewState", method = RequestMethod.GET)
	public ModelAndView viewState(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		State state = new State();
		Country cntry = new Country();
		
		try {
			state = stateService.getById(id);
			cntry = state.getCntry();
			
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.addObject("cntry",cntry);
		model.addObject("state",state);
		model.addObject("view","view");
		model.setViewName("/master/stateView");
		return model;
	}
	
	@RequestMapping(value = "stateList", method = RequestMethod.GET)
	public ModelAndView stateList(@RequestParam(value = "msg", required = false)String msg,HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("stateList", stateService.findAllListing());
		model.setViewName("/master/stateList");
		return model;
	}
	
	@RequestMapping(value = "editState", method = RequestMethod.GET)
	public ModelAndView editState(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		State state = new State();
		try {
			if(id > 0){
				state = stateService.getById(id);
				if(state.getCntry()!=null){
				state.setCountry_id(state.getCntry().getCountry_id());
				}
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		List<Country> countryList = countryService.findAll();
		model.addObject("countryList", countryList);
		model.addObject("state", state);
		model.setViewName("/master/state");
		return model;
	}
	@RequestMapping(value ="deleteState", method = RequestMethod.GET)
	public ModelAndView deleteState(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg",  stateService.deleteByIdValue(id));
		return new ModelAndView("redirect:/stateList");
	}
}
