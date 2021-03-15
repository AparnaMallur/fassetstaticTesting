package com.fasset.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.validators.DeducteeValidator;
import com.fasset.entities.Deductee;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.TDS_Type;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IDeducteeService;
import com.fasset.service.interfaces.ITDSType;
import com.fasset.service.interfaces.IindustryTypeService;

@Controller
@SessionAttributes("user")
public class DeducteeMasterController {
	
	@Autowired
	private IindustryTypeService typeService ;
	
	@Autowired
	private DeducteeValidator validator;
	
	@Autowired
	private IDeducteeService deducteeService;
	
	@Autowired
	private ITDSType TDSTypeService ;
	
	@RequestMapping(value = "deductee", method = RequestMethod.GET)
	public ModelAndView deductee() {
		ModelAndView model = new ModelAndView();
        List<TDS_Type> TDSList=TDSTypeService.findAll();
		
		model.addObject("TDSTypeList", TDSList);
		model.addObject("industrytypeList",  typeService.list());
		model.addObject("tdstypeList",  TDSList);
		model.addObject("deductee", new Deductee());
		model.setViewName("/master/deductee");
		return model;
	}
	
	@RequestMapping(value = "savedeductee", method = RequestMethod.POST)
	public ModelAndView savedeductee(@ModelAttribute("deductee")Deductee deductee,
			BindingResult result, 
			HttpServletRequest request, 
			HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
System.out.println("The deductee title "+deductee.getEffective_date());
		validator.validate(deductee, result);
		if(result.hasErrors()){
		    model.addObject("industrytypeList", typeService.list());
			model.setViewName("/master/deductee");
			return model;
		  }
		else{
			String msg = "";
			try{
				if(deductee.getDeductee_id()!=null){
					
					if(deductee.getDeductee_id() > 0){
						deductee.setUpdated_by(user);
						deducteeService.update(deductee);
						msg = "TDS updated successfully";
					}
				}			
				else{
					deductee.setCreated_by(user);
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            deductee.setIp_address(remoteAddr);
					msg = deducteeService.addDeductee(deductee);						
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}			
			session.setAttribute("msg", msg);
			return new ModelAndView("redirect:/deducteeList");		
		}
	}
	
	@RequestMapping(value ="deducteeList", method = RequestMethod.GET)
	public ModelAndView accountgroupList( HttpServletRequest request,HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if((String) session.getAttribute("msg")!=null)
		{
		String massage = (String) session.getAttribute("msg");
		session.removeAttribute("msg");
		model.addObject("successMsg", massage);
		}
		model.addObject("deducteeList", deducteeService.findAllDeducteeListing());
		model.setViewName("/master/deducteeList");
		return model;
	}
	
	@RequestMapping(value = "viewdeductee", method = RequestMethod.GET)
	public ModelAndView viewdeductee(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		try {
			model.addObject("deductee",deducteeService.getById(id));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setViewName("/master/deducteeView");
		return model;
	}
	
	@RequestMapping(value = "editdeductee", method = RequestMethod.GET)
	public ModelAndView editdeductee(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		Deductee deductee = new Deductee();	
		try {
			if(id > 0){
				deductee = deducteeService.getById(id);
				/*deductee.setIndustry_id(deductee.getIndustryType().getIndustry_id());*/
				deductee.setValue1(deductee.getValue().toString());
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		 List<TDS_Type> TDSList=TDSTypeService.findAll();
			System.out.println("on edit list is "+TDSList.size());
			System.out.println("Date is  "+deductee.getEffective_date());
			
			model.addObject("tdstypeList",  TDSList);
      	model.addObject("industrytypeList", typeService.list());
		model.addObject("deductee", deductee);
		model.setViewName("/master/deductee");
		return model;
	}
	
	@RequestMapping(value ="deletedeductee", method = RequestMethod.GET)
	public ModelAndView deletedeductee(@RequestParam("id") long id,HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String msg="";
	    msg = deducteeService.deleteDeducteeByIdValue(id);
	    
	    session.setAttribute("msg", msg);
		return new ModelAndView("redirect:/deducteeList");
	}
	
	
	

	
}
