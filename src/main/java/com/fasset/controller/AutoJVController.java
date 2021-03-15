package com.fasset.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;

@Controller
@SessionAttributes("user")
public class AutoJVController extends MyAbstractController 
{
	@RequestMapping(value = "autoJVList", method = RequestMethod.GET)
	public ModelAndView autoJVList(HttpServletRequest request, HttpServletResponse response) 
	{

		ModelAndView model = new ModelAndView();
		model.setViewName("/transactions/autoJVList");
		return model;
	}
}
