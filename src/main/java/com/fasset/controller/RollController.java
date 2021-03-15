package com.fasset.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.Role;
import com.fasset.service.interfaces.IRoleService;

@Controller
@SessionAttributes("user")
public class RollController extends MyAbstractController{
	
	@Autowired
	private IRoleService roleService;

	@RequestMapping(value = "role", method = RequestMethod.GET)
	public ModelAndView role(){
		ModelAndView model = new ModelAndView();
		try{
			List<Role> roleList = roleService.list();
			model.addObject("roleList", roleList);
			model.setViewName("master/roleList");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return model;
	}
}
