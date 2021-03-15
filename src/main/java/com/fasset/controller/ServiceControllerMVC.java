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

import com.fasset.controller.validators.ServiceValidator;
import com.fasset.entities.Country;
import com.fasset.entities.Service;
import com.fasset.entities.ServiceFrequency;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IFrequencyService;
import com.fasset.service.interfaces.IServiceMaster;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class ServiceControllerMVC {
     
	@Autowired
	private ServiceValidator serviceValidator;
	
	@Autowired
	private IServiceMaster serviceMaster ;
	 
	@Autowired
	private IFrequencyService frequencyService ;
	
	@RequestMapping(value = "service", method = RequestMethod.GET)
	public ModelAndView service() {
		ModelAndView model = new ModelAndView();
		Service service = new Service();
		List<ServiceFrequency> frequencyList = frequencyService.findAll();
		model.addObject("frequencyList", frequencyList);
		model.addObject("service", service);
		model.setViewName("/master/service");
		return model;
	}
	
	@RequestMapping(value = "saveservice", method = RequestMethod.POST)
	public ModelAndView saveService(@ModelAttribute("service")Service service, BindingResult result,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = new User();
		user = (User) session.getAttribute("user");
		
		serviceValidator.validate(service, result);
		if(result.hasErrors()){
			model.addObject("frequencyList", frequencyService.findAll());
			model.setViewName("/master/service");
			return model;
		}
		else{
			String msg = "";
			try {
				if(service.getId()!=null) {
				long id = service.getId();
					if(id > 0){
						service.setUpdated_by(user);
						serviceMaster.update(service);
						msg = "Service updated successfully";						
					}
				}
				
				else{
					
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            service.setIp_address(remoteAddr);
					service.setCreated_by(user);
					msg = serviceMaster.saveService(service);
				}
			
			}
			catch(Exception e){
				e.printStackTrace();
			}	
		
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/serviceList");
		}
	}
	@RequestMapping(value = "serviceList", method = RequestMethod.GET)
	public ModelAndView serviceList(@RequestParam(value = "msg", required = false)String msg,HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		List<Service> serviceList = serviceMaster.findAllListing();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("serviceList", serviceList);
		model.setViewName("/master/serviceList");
		return model;
	}
	
	@RequestMapping(value = "viewService", method = RequestMethod.GET)
	public ModelAndView viewService(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		Service service = new Service();
		ServiceFrequency frequency = new ServiceFrequency();
		try {
			service = serviceMaster.getById(id);
			frequency = service.getServiceFrequency();
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.addObject("frequency",frequency);
		model.addObject("service",service);
		model.addObject("view","view");
		model.setViewName("/master/serviceView");
		return model;
	}
	
	@RequestMapping(value = "editService", method = RequestMethod.GET)
	public ModelAndView editService(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		Service service = new Service();
		//System.out.println(id);
		try {
			if(id > 0){
				service = serviceMaster.getById(id);
				if(service.getServiceFrequency()!=null){
					//System.out.println("#####If#####"+service.getServiceFrequency());
					service.setService_frequency(service.getServiceFrequency().getFrequency_id());
				}
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		List<ServiceFrequency> frequencyList = frequencyService.findAll();
		model.addObject("frequencyList", frequencyList);
		model.addObject("service", service);
		model.setViewName("/master/service");
		return model;
	}
	@RequestMapping(value ="deleteService", method = RequestMethod.GET)
	public ModelAndView deleteService(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg", serviceMaster.deleteByIdValue(id));
		return new ModelAndView("redirect:/serviceList");
	}
}
