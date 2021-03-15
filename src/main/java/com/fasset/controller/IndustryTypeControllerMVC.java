package com.fasset.controller;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.fasset.controller.validators.IndustryTypeValidator;
import com.fasset.entities.IndustryType;
import com.fasset.entities.Ledger;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IindustryTypeService;

/**
 * mayur suramwar
 */

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class IndustryTypeControllerMVC {

	@Autowired
	private ILedgerService ledgerService ;
	
	@Autowired
	private IndustryTypeValidator typeValidator ;
	
	@Autowired
	private IindustryTypeService typeService ;
	
	@Autowired
	private ISubLedgerService subLedgerService ;
	
	@RequestMapping(value = "industrytype", method = RequestMethod.GET)
	public ModelAndView industryType(			HttpServletRequest request, 
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		ModelAndView model = new ModelAndView();
		long company_id=(long)session.getAttribute("company_id");	
		model.addObject("ledgerList", ledgerService.findAllLedgersWithSubledger(company_id));
		model.addObject("type", new IndustryType());
		model.setViewName("/master/industrytype");
		return model;
	}
	
	@RequestMapping(value = "saveindustrytype", method = RequestMethod.POST)
	public ModelAndView saveindustryType(@ModelAttribute("type")IndustryType type, BindingResult result,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = new User();
		user = (User) session.getAttribute("user");
		long company_id=(long)session.getAttribute("company_id");	

		typeValidator.validate(type, result);
		if(result.hasErrors()){
			model.addObject("ledgerList", ledgerService.findAllLedgersWithSubledger(company_id));
			model.setViewName("/master/industrytype");
			return model;
		}
		else{
			String msg = "";
			try
			{
			if(type.getIndustry_id()!=null)
			{
			if(type.getIndustry_id() > 0){
				type.setUpdated_by(user);
				typeService.update(type);
				msg = "Industry type updated successfully";
				
			}
			}
			
			else{
				type.setCreated_by(user);
				String remoteAddr = "";
				 remoteAddr = request.getHeader("X-FORWARDED-FOR");
		            if (remoteAddr == null || "".equals(remoteAddr)) {
		                remoteAddr = request.getRemoteAddr();
		            }
		            type.setIp_address(remoteAddr);
				msg = typeService.saveIndustryType(type);
			}
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/industrytypeList");		
		}
	}
	
	@RequestMapping(value = "industrytypeList", method = RequestMethod.GET)
	public ModelAndView industrytypeList(@RequestParam(value = "msg", required = false)String msg,HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("industrytypeList", typeService.findAllListing());
		model.setViewName("/master/industrytypeList");
		return model;
	}
	
	@RequestMapping(value = "viewIndustryType", method = RequestMethod.GET)
	public ModelAndView viewIndustryType(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		IndustryType type = new IndustryType();
		try {
			type = typeService.findOneWithAll(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("subLedgers",type.getSubLedgers());
		model.addObject("type",type);
		model.setViewName("/master/industryTypeView");
		return model;
	}
	@RequestMapping(value = "editIndustryType", method = RequestMethod.GET)
	public ModelAndView editIndustryType(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		Long industryid = id;
		HttpSession session = request.getSession(true);
		session.setAttribute("industryid", industryid);
		
		IndustryType type = new IndustryType();
		Set<SubLedger> subLedgers = new HashSet<SubLedger>();
		List<Ledger> ledgerList = new ArrayList<Ledger>();
		SubLedger subLedger1 = new SubLedger();
		try {
			if(id > 0){
				type = typeService.findOneWithAll(id);
				subLedgers =type.getSubLedgers();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long company_id=(long)session.getAttribute("company_id");	

		List<Ledger> totalledgerList = ledgerService.findAllLedgersWithSubledger(company_id);
		
		for(Ledger ledger : totalledgerList)
		{
			System.out.println(ledger.getLedger_name());
		   Set<SubLedger> subLedgerlist  =  new HashSet<SubLedger>();
			subLedgerlist = ledger.getSubLedger();
			
			System.out.println(subLedgerlist);
			for(SubLedger subLedger : subLedgers)
			{
				System.out.println(subLedger.getSubledger_name());
				try {
					subLedger1 = subLedgerService.getById(subLedger.getSubledger_Id());
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(subLedgerlist.remove(subLedger1));
				
				
			}
			ledger.setSubLedger(subLedgerlist);
			ledgerList.add(ledger);
		}
		
		model.addObject("ledgerList", ledgerList);
		model.addObject("subLedgers", subLedgers);
		model.addObject("type", type);
		model.setViewName("/master/industrytype");
		return model;
	}
	
	
	@RequestMapping(value ="deleteIndustryType", method = RequestMethod.GET)
	public ModelAndView deleteIndustryType(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg",  typeService.deleteByIdValue(id));
		return new ModelAndView("redirect:/industrytypeList");
		
	}
	
	@RequestMapping(value ="deleteIndustrySubLedger", method = RequestMethod.GET)
	public ModelAndView deleteSubLedger(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		Long industryid =(Long)session.getAttribute("industryid");
		String msg="";
	    msg = typeService.deleteSubLedger(industryid, id);
	    
	    IndustryType type = new IndustryType();
		Set<SubLedger> subLedgers = new HashSet<SubLedger>();
		List<Ledger> ledgerList = new ArrayList<Ledger>();
		SubLedger subLedger1 = new SubLedger();
		try {
			if(id > 0){
				type = typeService.findOneWithAll(industryid);
				subLedgers =type.getSubLedgers();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long company_id=(long)session.getAttribute("company_id");	

		List<Ledger> totalledgerList = ledgerService.findAllLedgersWithSubledger(company_id);
		
		for(Ledger ledger : totalledgerList)
		{
			System.out.println(ledger.getLedger_name());
		   Set<SubLedger> subLedgerlist  =  new HashSet<SubLedger>();
			subLedgerlist = ledger.getSubLedger();
			
			System.out.println(subLedgerlist);
			for(SubLedger subLedger : subLedgers)
			{
				System.out.println(subLedger.getSubledger_name());
				try {
					subLedger1 = subLedgerService.getById(subLedger.getSubledger_Id());
				} catch (MyWebException e) {
					
					e.printStackTrace();
				}
				
				System.out.println(subLedgerlist.remove(subLedger1));
				
				
			}
			ledger.setSubLedger(subLedgerlist);
			ledgerList.add(ledger);
		}
		
		model.addObject("ledgerList", ledgerList);
		model.addObject("subLedgers", subLedgers);
		model.addObject("type", type);
		model.addObject("successMsg", msg);
		model.setViewName("/master/industrytype");
		
		return model;
	}
}
