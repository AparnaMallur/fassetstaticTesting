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

import com.fasset.controller.validators.AccountGroupValidator;
import com.fasset.entities.AccountGroup;

import com.fasset.entities.User;
import com.fasset.service.interfaces.IAccountGroupService;
import com.fasset.service.interfaces.IAccountGroupTypeService;
import com.fasset.service.interfaces.ILedgerPostingSideService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */

// Account group related controller

//controller for account

//controller
@Controller
@SessionAttributes("user")
public class AccountGroupMasterController {

	@Autowired
	private IAccountGroupTypeService accountGpTypeService;
	
	@Autowired
	private ILedgerPostingSideService  ledgerPostingSideService;
	
	@Autowired
	private AccountGroupValidator validator;
	
	@Autowired
	private IAccountGroupService  accGrpService ;
	
	@RequestMapping(value = "accountgroup", method = RequestMethod.GET)
	public ModelAndView accountgroup() {
		ModelAndView model = new ModelAndView();
		model.addObject("accgptypeList", accountGpTypeService.findAll());
		model.addObject("ledPostingSideList", ledgerPostingSideService.findAll());
		model.addObject("group", new AccountGroup());
		model.setViewName("/master/accountgroup");
		return model;
	}
		
	@RequestMapping(value = "saveaccountgroup", method = RequestMethod.POST)
	public ModelAndView saveaccountgroup(@ModelAttribute("group")AccountGroup group,  
			BindingResult result,
			HttpServletRequest request, 
			HttpServletResponse response) {
		User user = new User();
		HttpSession session = request.getSession(true);
		ModelAndView model = new ModelAndView();
		user = (User) session.getAttribute("user");
		validator.validate(group, result);
		if(result.hasErrors()){
			model.addObject("accgptypeList", accountGpTypeService.findAll());
			model.addObject("ledPostingSideList", ledgerPostingSideService.findAll());
			model.setViewName("/master/accountgroup");
			return model;
		  }
		else{
			String msg = "";
			try{
				if(group.getGroup_Id()!=null){
					if(group.getGroup_Id()> 0){
						group.setUpdated_by(user);
						accGrpService.update(group);
						msg = "Account group updated successfully";
					}
				}			
				else{
					group.setCreated_by(user);
					group.setCompany(user.getCompany());
					 String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            group.setIp_address(remoteAddr);
					msg = accGrpService.saveAccountGroup(group);				
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}	
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/accgroupList");		
		}
	}
	
	@RequestMapping(value ="accgroupList", method = RequestMethod.GET)
	public ModelAndView accountgroupList(@RequestParam(value = "msg", required = false)String msg,
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("accountGpList", accGrpService.findAllListing());
		model.setViewName("/master/accountGpList");
		return model;
	}
	
	@RequestMapping(value = "viewAccountGroup", method = RequestMethod.GET)
	public ModelAndView viewAccountGroup(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		AccountGroup group = new AccountGroup();
		
		try {
			group = accGrpService.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("group",group);
		model.addObject("gptype",group.getGrouptype());
		model.addObject("pside", group.getPostingSide());
		model.setViewName("/master/accountgroupView");
		return model;
	}
	@RequestMapping(value = "editAccountGroup", method = RequestMethod.GET)
	public ModelAndView editAccountGroup(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		AccountGroup group = new AccountGroup();
		try {
			if(id > 0){
				group = accGrpService.findOneWithAll(id);
				group.setAccount_group_id(group.getGrouptype().getAccount_group_id());
				group.setPosting_id(group.getPostingSide().getPosting_id());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		model.addObject("accgptypeList", accountGpTypeService.findAll());
		model.addObject("ledPostingSideList", ledgerPostingSideService.findAll());
		model.addObject("group", group);
		model.setViewName("/master/accountgroup");
		return model;
	}
	@RequestMapping(value ="deleteAccountGroup", method = RequestMethod.GET)
	public ModelAndView deleteAccountGroup(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg",  accGrpService.deleteByIdValue(id));
		return new ModelAndView("redirect:/accgroupList");
	}
}
