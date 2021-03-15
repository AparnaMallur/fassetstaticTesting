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

import com.fasset.controller.validators.AccountSubGroupValidator;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountGroupType;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IAccountGroupService;
import com.fasset.service.interfaces.IAccountSubGroupService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class AccountSubGroupMasterController {
     
	@Autowired
	private IAccountGroupService accountGroupService ;
	
	@Autowired
	private AccountSubGroupValidator validator;
	
	@Autowired
	private IAccountSubGroupService accountSubGroupService ;
	
	@RequestMapping(value = "accountsubgroup", method = RequestMethod.GET)
	public ModelAndView accountSubGroup() {
		ModelAndView model = new ModelAndView();
		model.addObject("accountGroupList", accountGroupService.findAll());
		model.addObject("subgroup", new AccountSubGroup());
		model.setViewName("/master/accountsubgroup");
		return model;
	}
	
	@RequestMapping(value = "saveaccountsubgroup", method = RequestMethod.POST)
	public ModelAndView saveAccountSubGroup(@ModelAttribute("subgroup")AccountSubGroup subgroup, BindingResult result,HttpServletRequest request, HttpServletResponse response) {
		
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		ModelAndView model = new ModelAndView();
		validator.validate(subgroup, result);
		if(result.hasErrors()){
			model.addObject("accountGroupList", accountGroupService.findAll());
			model.setViewName("/master/accountsubgroup");
			return model;
		}
		else{			
			String msg = "";
			try{
				if(subgroup.getSubgroup_Id()!=null){
					if(subgroup.getSubgroup_Id() > 0){
						subgroup.setUpdated_by(user);
						accountSubGroupService.update(subgroup);
						msg = "Account sub group updated successfully";					
					}
				}			
				else{
					subgroup.setCreated_by(user);
					subgroup.setCompany(user.getCompany());
					String remoteAddr="";
					remoteAddr = request.getHeader("X-FORWARDED-FOR");
		            if (remoteAddr == null || "".equals(remoteAddr)) {
		                remoteAddr = request.getRemoteAddr();
		            }
		            subgroup.setIp_address(remoteAddr);
					msg = accountSubGroupService.saveAccountSubGroup(subgroup);
				}			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/accsubgroupList");		
		
		}
	}
	@RequestMapping(value = "accsubgroupList", method = RequestMethod.GET)
	public ModelAndView accSubGroupList(@RequestParam(value = "msg", required = false)String msg, 
			HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("accountSubGroupList", accountSubGroupService.findAllListing());
		model.setViewName("/master/accountsubgroupList");
		return model;
	}
	
	@RequestMapping(value = "viewAccountSubGroup", method = RequestMethod.GET)
	public ModelAndView viewAccountSubGroup(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		AccountSubGroup subgroup = new AccountSubGroup();
		try {
			subgroup = accountSubGroupService.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("type",subgroup.getAccountGroup().getGrouptype());
		model.addObject("subgroup",subgroup);
		model.addObject("group",subgroup.getAccountGroup());
		model.addObject("view","view");
		model.setViewName("/master/accountsubgroupView");
		return model;
	}

	@RequestMapping(value = "editAccountSubGroup", method = RequestMethod.GET)
	public ModelAndView editAccountSubGroup(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		AccountSubGroup subgroup = new AccountSubGroup();
		try {
			if(id > 0){
				subgroup = accountSubGroupService.findOneWithAll(id);
				subgroup.setGroup_Id(subgroup.getAccountGroup().getGroup_Id());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("accountGroupList", accountGroupService.findAll());
		model.addObject("subgroup", subgroup);
		model.setViewName("/master/accountsubgroup");
		return model;
	}
	
	@RequestMapping(value ="deleteAccountSubGroup", method = RequestMethod.GET)
	public ModelAndView AccountSubGroup(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg",  accountSubGroupService.deleteByIdValue(id));
		return new ModelAndView("redirect:/accsubgroupList");
	}
}
