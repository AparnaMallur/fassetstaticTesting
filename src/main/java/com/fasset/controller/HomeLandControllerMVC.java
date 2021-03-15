package com.fasset.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;

@Controller
public class HomeLandControllerMVC extends MyAbstractController {

	static Logger log = LogManager.getLogger(HomeLandControllerMVC.class.getName());
	
	
	
	@RequestMapping("/")
	public ModelAndView welcomePage(HttpServletRequest request) {		
		ModelAndView model = new ModelAndView();
		model.setViewName("home-landing");
		return model;
	}
	
	@RequestMapping("xyz")
	public ModelAndView welcomePageForMobile(HttpServletRequest request) {		
		ModelAndView model = new ModelAndView();
		model.setViewName("mobilehomelanding");
		return model;
	}
}
