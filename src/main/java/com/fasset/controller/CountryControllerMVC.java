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

import com.fasset.controller.validators.CountryValidator;
import com.fasset.entities.Country;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ICountryService;


/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class CountryControllerMVC {

	@Autowired
	private CountryValidator countryValidator ;
	
	@Autowired
	private ICountryService countryService ;
	
	@RequestMapping(value = "country", method = RequestMethod.GET)
	public ModelAndView country() {
		System.out.println("The country is cled");
		ModelAndView model = new ModelAndView();
		model.addObject("country", new Country());
		model.setViewName("/master/country");
		return model;
	}
	
	
	@RequestMapping(value = "savecountry", method = RequestMethod.POST)
	public ModelAndView saveCountry(@ModelAttribute("country")Country country, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		ModelAndView model = new ModelAndView();
		User user = new User();
		user = (User) session.getAttribute("user");
		countryValidator.validate(country, result);
		if(result.hasErrors()){
			model.setViewName("/master/country");
			return model;
			
		}
		else{
			String msg = "";
			try
			{
			if(country.getCountry_id()!=null)
			{
			if(country.getCountry_id() > 0){
				country.setUpdated_by(user);
				countryService.update(country);
				msg = "Country updated successfully";
				
			}
			}
			
			else{
				country.setCreated_by(user);
				String remoteAddr = "";
				 remoteAddr = request.getHeader("X-FORWARDED-FOR");
		            if (remoteAddr == null || "".equals(remoteAddr)) {
		                remoteAddr = request.getRemoteAddr();
		            }
		            country.setIp_address(remoteAddr);

				msg = countryService.saveCountry(country);
			}
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/countryList");	
			
		}
	}
	
	@RequestMapping(value = "viewCountry", method = RequestMethod.GET)
	public ModelAndView viewCountry(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		try {
			model.addObject("country",countryService.getById(id));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setViewName("/master/countryView");
		return model;
	}
	@RequestMapping(value = "countryList", method = RequestMethod.GET)
	public ModelAndView countryList(@RequestParam(value = "msg", required = false)String msg,
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("countryList", countryService.findAllListing());
		model.setViewName("/master/countryList");
		return model;
	}
	
	@RequestMapping(value = "editCountry", method = RequestMethod.GET)
	public ModelAndView editCountry(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		try {
			model.addObject("country", countryService.getById(id));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setViewName("/master/country");
		return model;
	}
	
	@RequestMapping(value ="deleteCountry", method = RequestMethod.GET)
	public ModelAndView deleteCountry(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg",  countryService.deleteByIdValue(id));
		return new ModelAndView("redirect:/countryList");
	}
	
	
}
