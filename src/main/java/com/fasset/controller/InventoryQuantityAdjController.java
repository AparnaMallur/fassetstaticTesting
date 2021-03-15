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
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.validators.InventoryQuantityValidator;
import com.fasset.entities.Company;
import com.fasset.entities.InventoryQuantityAdjustment;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IInventoryQuantityAdjService;
import com.fasset.service.interfaces.IProductService;

@Controller
public class InventoryQuantityAdjController {
	
	@Autowired
	private IInventoryQuantityAdjService inventoryQuantityService;
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private IAccountingYearService accountingYearService;
	
	@Autowired
	private InventoryQuantityValidator inventoryQuantityValidator;
	
	@Autowired
	private ICompanyService companyService;
	
	@RequestMapping(value = "inventoryQuantityList", method = RequestMethod.GET)
	public ModelAndView inventoryQuantityList(@RequestParam(value = "msg", required = false)String msg,
			HttpServletRequest request,
			HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");	
		if((String) session.getAttribute("msg")!=null)
		{
		String massage = (String) session.getAttribute("msg");
		session.removeAttribute("msg");
		model.addObject("successMsg", massage);
		}
		model.addObject("inventoryQuantityList", inventoryQuantityService.findAllInventoryOfCompany(company_id));
		model.setViewName("transactions/inventoryQuantityList");
		return model;
	}
	
	@RequestMapping(value = "inventoryQuantity", method = RequestMethod.GET)
	public ModelAndView inventoryQuantity(HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		User user = new User();		
		HttpSession session = request.getSession(true);
		user = (User)session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");	
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		
		model.addObject("inventoryQuantity", new InventoryQuantityAdjustment());
		model.addObject("productList", productService.findAllProductsOnlyOfCompany(company_id));
		model.addObject("accountingYearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
		model.setViewName("transactions/inventoryQuantity");
		return model;
	}
	
	@RequestMapping(value = "inventoryQuantity", method = RequestMethod.POST)
	public ModelAndView inventoryQuantity(@ModelAttribute("inventoryQuantity")InventoryQuantityAdjustment inventoryQuantity,
			BindingResult result,HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		User user = new User();		
		HttpSession session = request.getSession(true);
		user = (User)session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");	
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		
		Company company = new Company();
		try {
			company = companyService.getById(user.getCompany().getCompany_id());
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		
		inventoryQuantityValidator.validate(inventoryQuantity, result);
		if(result.hasErrors()){
			model.addObject("productList",  productService.findAllProductsOnlyOfCompany(company_id));
			model.addObject("accountingYearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
			model.setViewName("transactions/inventoryQuantity");
			return model;
		}
		else{
			String msg = "";
			try{
				Long id = inventoryQuantity.getInventory_adj_id();
				if(id != null){
					inventoryQuantity.setUpdated_by(user);
					//inventoryQuantity.setValue(inventoryQuantity.getValue());
					inventoryQuantityService.update(inventoryQuantity);
					msg = "Data updated successfully";
				}
				else{
					inventoryQuantity.setCompany(company);
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            inventoryQuantity.setIp_address(remoteAddr);
			            inventoryQuantity.setCreated_by(user);
					inventoryQuantityService.add(inventoryQuantity);
					msg = "Data added successfully";
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			session.setAttribute("msg", msg);
			return new ModelAndView("redirect:/inventoryQuantityList");
		}
	}
	
	@RequestMapping(value = "viewInvQuantity", method = RequestMethod.GET)
	public ModelAndView viewInvQuantity(@RequestParam("id")Long id){
		ModelAndView model = new ModelAndView();
		try {
			model.addObject("inventoryQuantity", inventoryQuantityService.getById(id));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setViewName("transactions/inventoryQuantityView");
		return model;
	}
	
	@RequestMapping(value = "editInvQuantity", method = RequestMethod.GET)
	public ModelAndView editInvQuantity(@RequestParam("id")Long id,HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		User user = new User();		
		HttpSession session = request.getSession(true);
		user = (User)session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");	
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		model.addObject("productList",  productService.findAllProductsOnlyOfCompany(company_id));
		model.addObject("accountingYearList", accountingYearService.findAccountRange(user_id, yearrange,company_id));
		try {
			model.addObject("inventoryQuantity", inventoryQuantityService.getById(id));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setViewName("transactions/inventoryQuantity");
		return model;
	}
	
	
	@RequestMapping(value = "deleteInvQuantity", method = RequestMethod.GET)
	public ModelAndView deleteInvQuantity(@RequestParam("id")Long id,HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession(true);			
		session.setAttribute("msg", inventoryQuantityService.deleteByIdValue(id));
		return new ModelAndView("redirect:/inventoryQuantityList");
	}
	
}
