package com.fasset.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;

@Controller
@SessionAttributes("user")
public class SubscriptionReportController {
	
	@Autowired
	private IClientSubscriptionMasterService clientSubscriptionMasterService;
	
	@RequestMapping(value ="subscriberreport", method = RequestMethod.GET)
	public ModelAndView showsubsciptionreport( 
			HttpServletRequest request, 
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model.addObject("subscriptionList", clientSubscriptionMasterService.getSubscriptionReport());
		model.setViewName("/master/subscriptionReport");
		return model;
		}
	

}
