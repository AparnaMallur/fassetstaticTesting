package com.fasset.controller;

import java.util.List;

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

import com.fasset.controller.validators.MenuMasterValidator;
import com.fasset.entities.MenuMaster;
import com.fasset.entities.RoleMenuMaster;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IMenuMasterService;
import com.fasset.service.interfaces.IRoleService;


@Controller
@SessionAttributes("user")
public class MenuMasterController {

		@Autowired
		private MenuMasterValidator menuMasterValidator;
		@Autowired
		private IMenuMasterService MenuMasterService;
		
		@Autowired
		private IRoleService RoleService;
		

	@RequestMapping(value = "menuMaster", method = RequestMethod.GET)
	public ModelAndView MasterMenu(HttpServletRequest request)
	{
		HttpSession session=request.getSession();
		User user = (User) session.getAttribute("user");
		Long id=user.getUser_id();
		long role_id=(long)session.getAttribute("role");		

		RoleMenuMaster roleMenuMaster = new RoleMenuMaster();
		ModelAndView model = new ModelAndView();
		MenuMaster menumaster = new MenuMaster();
		roleMenuMaster  = RoleService.getRole();
		List<MenuMaster> menuMasterList=MenuMasterService.findAll(role_id);
		model.addObject("menumaster", menumaster);
		//model.addObject("roleMenuMaster",roleMenuMaster);
		model.addObject("menuMasterList",menuMasterList);
		model.setViewName("/master/menuMaster");
		return model;
	}

	@RequestMapping(value="MenuList", method=RequestMethod.GET)
	public ModelAndView MasaterMenuList(HttpServletRequest request)
	{
		HttpSession session=request.getSession();
		User user = (User) session.getAttribute("user");
		Long id=(long)session.getAttribute("role");
		ModelAndView model = new ModelAndView();
		List<MenuMaster> menuMasterList=MenuMasterService.findAll(id);
		///System.out.println(menuMasterList.size()+"    "+id);
		model.addObject("menuMasterList",menuMasterList);
		model.setViewName("/master/menuMasterList");
		return model;
		
	}
	
	
	@RequestMapping(value = "saveMenuMaster", method = RequestMethod.POST)
	public  ModelAndView SaveMenuMaster(@ModelAttribute("menumaster")MenuMaster menuMaster,
			BindingResult result,HttpServletRequest request)
	{
		
		HttpSession session=request.getSession();
		User user = (User) session.getAttribute("user");
		Long loginId=(long)session.getAttribute("role");
		
		ModelAndView model = new ModelAndView();
		List<MenuMaster> menuMasterList=MenuMasterService.findAll(loginId);

		menuMasterValidator.validate(menuMaster, result);
		if(result.hasErrors()){
			System.out.println("Inside result.hasErrors===========:>"+menuMaster);
			model.addObject("menuMasterList",menuMasterList);
			model.setViewName("/master/menuMaster");			
			return model;
		}
		else{
			String msg="";
			Long parentId;
			parentId = menuMaster.getParent();
			try {
				if(parentId > 0) {
					menuMaster.setParent_id(MenuMasterService.getById(parentId));
				}
				if(menuMaster.getMenu_id()!=null)
				{
					Long id=menuMaster.getMenu_id();
					if(id>0)
					{
						System.out.println("Client Auditor======:>"+menuMaster.getAuditor()+""+menuMaster.getClient());
						/*if(menuMaster.getAuditor()==1)
						{
							menuMaster.setClient(0);
						}
						
						if(menuMaster.getClient()==1)
						{
							menuMaster.setAuditor(0);
						}*/
						
							MenuMasterService.update(menuMaster);
						
						msg = "Menu updated successfully";
					}
				}
				else
				{
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            menuMaster.setIp_address(remoteAddr);
					
					msg=MenuMasterService.saveMenuMaster(menuMaster);
					
				}
				
				model.addObject("menuMasterList",menuMasterList);
				model.setViewName("/master/menuMasterList");
				model.addObject("successMsg", msg);
				model.setViewName("/master/menuMasterList");
			}
			catch (MyWebException e) {
				e.printStackTrace();
			}
			return model;
		}
	}
	@RequestMapping(value="viewMenu", method=RequestMethod.GET)
	public ModelAndView viewMenu(@RequestParam("id") long id)
	{
		ModelAndView model=new ModelAndView();
		MenuMaster menuMaster=new MenuMaster();
		try {
			menuMaster=MenuMasterService.getById(id);
			
		} catch  (MyWebException  e) {
			
			e.printStackTrace();
		}
		model.addObject("menuMaster",menuMaster);
		model.addObject("view","view");
		model.setViewName("/master/menuView");
		return model;
		
	}
	
	@RequestMapping (value="editMenu",method=RequestMethod.GET)
	public ModelAndView editMenu(@RequestParam("id") long id, 
			HttpServletRequest request,
			HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		long role_id=(long)session.getAttribute("role");
		ModelAndView model=new ModelAndView();
		MenuMaster menumaster=new MenuMaster();
		try {
			if(id>0)
			{
				menumaster=MenuMasterService.getById(id);
				if(menumaster.getParent_id() != null) {
					menumaster.setParent(menumaster.getParent_id().getMenu_id());
				}
				
			}
			
			
		} catch (MyWebException e) {
			
			e.printStackTrace();
		}
		List<MenuMaster> menuMasterList=MenuMasterService.findAll(role_id);
		model.addObject("menumaster", menumaster);
		model.addObject("menuMasterList",menuMasterList);
		model.setViewName("/master/menuMaster");
		return model;
		
	}

	@RequestMapping (value="deleteMenu",method=RequestMethod.GET)
	public ModelAndView deleteMenu(@RequestParam("id") long id, 
			HttpServletRequest request,
			HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		long role_id=(long)session.getAttribute("role");		
		ModelAndView model=new ModelAndView();
		String msg="";
		msg=MenuMasterService.deleteByIdValue(id);
		List<MenuMaster> menuMasterList=MenuMasterService.findAll(role_id);
		///System.out.println(menuMasterList.size()+"    "+id);
		model.addObject("menuMasterList",menuMasterList);
		model.setViewName("/master/menuMasterList");			
		return model;
		
	}
}
