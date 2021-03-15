/**
 * mayur suramwar
 */
package com.fasset.controller;

import java.util.Map;

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

import com.fasset.controller.validators.CityValidator;
import com.fasset.entities.City;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ICityService;
import com.fasset.service.interfaces.ICountryService;
import com.fasset.service.interfaces.IStateService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class CityControllerMVC {

	@Autowired
	private CityValidator cityValidator ;
	
	@Autowired
	private ICityService cityService ;
	
	@Autowired
	private IStateService stateService ;
	
	@Autowired
	private ICountryService countryService ;
	
	@RequestMapping(value = "city", method = RequestMethod.GET)
	public ModelAndView city() {
		ModelAndView model = new ModelAndView();
		model.addObject("stateList", stateService.findAll());
		model.addObject("countryList", countryService.findAll());
		model.addObject("city", new City());
		model.setViewName("/master/city");
		return model;
	}
	
	@RequestMapping(value = "savecity", method = RequestMethod.POST)
	public ModelAndView saveCity(@ModelAttribute("city")City city,
			BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		ModelAndView model = new ModelAndView();
		User user = new User();
		user = (User) session.getAttribute("user");	
		cityValidator.validate(city, result);
		if(result.hasErrors()){
			model.addObject("stateList", stateService.findAll());
			model.addObject("countryList", countryService.findAll());
			model.setViewName("/master/city");
			return model;
		}
		else{
			String msg = "";
			try{
				if(city.getCity_id()!=null){
					if(city.getCity_id() > 0){
						city.setUpdated_by(user);
						cityService.update(city);
						msg = "City updated successfully";						
					}
				}			
				else{
					city.setCreated_by(user);
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            city.setIp_address(remoteAddr);
					msg = cityService.saveCity(city);
				}			
			}
			catch(Exception e){
				e.printStackTrace();
			}	
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/cityList");
		
		}
	}
	
	@RequestMapping(value = "viewCity", method = RequestMethod.GET)
	public ModelAndView viewCity(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		City city = new City();
		try {
			city = cityService.getById(id);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		
		model.addObject("state",city.getState());
		model.addObject("cntry", city.getCountry());
		model.addObject("city",city);
		model.setViewName("/master/cityView");
		return model;
	}
	
	@RequestMapping(value = "cityList", method = RequestMethod.GET)
	public ModelAndView cityList(@RequestParam(value = "msg", required = false)String msg,
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("cityList", cityService.findAllListing());
		model.setViewName("/master/cityList");
		return model;
	}
	
	@RequestMapping(value = "editCity", method = RequestMethod.GET)
	public ModelAndView editState(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		City city = new City();
		try {
			if(id > 0){
				city = cityService.getById(id);
				city.setState_id(city.getState().getState_id());
				city.setCountry_id(city.getCountry().getCountry_id());
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.addObject("stateList", stateService.findAll());
		model.addObject("countryList", countryService.findAll());
		model.addObject("city", city);
		model.setViewName("/master/city");
		return model;
	}
	@RequestMapping(value ="deleteCity", method = RequestMethod.GET)
	public ModelAndView deleteCity(@RequestParam("id") long id,HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg",  cityService.deleteByIdValue(id));
		return new ModelAndView("redirect:/cityList");
	}
}
