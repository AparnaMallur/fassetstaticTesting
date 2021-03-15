/**
 * mayur suramwar
 */
package com.fasset.controller;

import java.util.Set;

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

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.ClientValidationChecklistStatusValidator;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.ClientValidationChecklistStatus;
import com.fasset.entities.Company;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.User;
import com.fasset.form.ClientValidationChecklistForm;
import com.fasset.form.ClientValidationChecklistStatusForm;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.IClientValidationChecklistService;
import com.fasset.service.interfaces.IClientValidationChecklistStatusService;
import com.fasset.service.interfaces.ICompanyService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class ClientValidationChecklistStatusController extends MyAbstractController{

	@Autowired
	private IClientValidationChecklistService service ;
	
	@Autowired
	private IClientValidationChecklistStatusService  statusService ;
	
	@Autowired
	private IAccountingYearService accountingYearService ;
	
	@Autowired
	private ClientValidationChecklistStatusValidator validator;
	
	@Autowired
	private IClientValidationChecklistService checklistService ;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired	
	private IClientSubscriptionMasterService SubscriptionService;	
	
	private Set<QuotationDetails> quotationDetails;
	
	@RequestMapping(value = "editChecklistStatus", method = RequestMethod.GET)
	public ModelAndView checklistStatus(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView();
		ClientValidationChecklistForm form = new ClientValidationChecklistForm();
		HttpSession session = request.getSession(true);
		session.setAttribute("Compid", id);
		Company comp = companyService.getCompanyWithAllUsers(id);
		User client= new User();
		for(User user:comp.getUser())
		{
			if(user.getRole().getRole_id()==5)
			{
				client=user;
			}
		}
		                
		ClientSubscriptionMaster obj= SubscriptionService.getClientSubscriptionByCompanyId(comp.getCompany_id());
		if(obj.getQuotation_id() != null) {
			quotationDetails = obj.getQuotation_id().getQuotationDetails();	
			form.setQuotationDetails(quotationDetails);	
		}
		
		form.setStatus(new ClientValidationChecklistStatus());
		form.setUser(client);
		form.setCompany(comp);
		form.setChecklist(service.findAll());
		form.setYearList(accountingYearService.findAll());
		model.addObject("form", form);
		model.setViewName("/KPO/checklistStatus");
		return model;
	}	
	
	@RequestMapping(value = "checklistStatus", method = RequestMethod.POST)
	public ModelAndView savechecklistStatus(@ModelAttribute("form")ClientValidationChecklistForm form, BindingResult result,HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long user_id=(long)session.getAttribute("user_id");
		Long id= (Long) session.getAttribute("Compid");
		Company comp = companyService.getCompanyWithAllUsers(id);
		
		User client= new User();
		for(User user:comp.getUser())
		{
			if(user.getRole().getRole_id()==5)
			{
				client=user;
			}
		}		
		validator.validate(form, result);
		if(result.hasErrors()){
			ClientValidationChecklistStatus status = new ClientValidationChecklistStatus();    
			status.setEmplimit(form.getStatus().getEmplimit());
			status.setYearRange(form.getStatus().getYearRange());
			status.setFromDate(form.getStatus().getFromDate());
			status.setToDate(form.getStatus().getToDate());
			form.setStatus(status);
			form.setUser(client);
			form.setCompany(comp);
			form.setChecklist(service.findAll());
			form.setYearList(accountingYearService.findAll());
			form.setQuotationDetails(quotationDetails);
			model.addObject("form", form);
			model.setViewName("/KPO/checklistStatus");			
			return model;
		}
		
		
		else{
			ClientValidationChecklistStatusForm obj = new ClientValidationChecklistStatusForm();
			
			if(obj.statuschecklist(form.getStatus(),statusService,checklistService)==true)
			{
			String msg = "";
			try
			{
				ClientValidationChecklistStatus status = form.getStatus();
				status.setCompany(client.getCompany());
				status.setUpdated_by(user_id);
				 String remoteAddr = "";
				 remoteAddr = request.getHeader("X-FORWARDED-FOR");
		            if (remoteAddr == null || "".equals(remoteAddr)) {
		                remoteAddr = request.getRemoteAddr();
		            }
		            status.setIp_address(remoteAddr);
				msg = statusService.saveClientValidationChecklistStatus(status,client);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			model.addObject("compList", companyService.getCompanyByStatus(STATUS_PENDING_FOR_APPROVAL));
			model.addObject("successMsg", msg);
			model.setViewName("/KPO/companyCheckList");			
			return model;
			}
		else
			{
			String msg = "Please Select Manadatory Checklist";
			
			ClientValidationChecklistStatus status = new ClientValidationChecklistStatus();	
			status.setEmplimit(form.getStatus().getEmplimit());
			status.setYearRange(form.getStatus().getYearRange());
			status.setFromDate(form.getStatus().getFromDate());
			status.setToDate(form.getStatus().getToDate());			
			form.setStatus(status);
			form.setUser(client);
			form.setCompany(comp);
			form.setChecklist(service.findAll());
			form.setYearList(accountingYearService.findAll());
			form.setQuotationDetails(quotationDetails);
			model.addObject("form", form);
			model.addObject("successMsg", msg);
			model.setViewName("/KPO/checklistStatus");	
			return model;
			}
		}
	}
	
	@RequestMapping(value ="companyCheckList", method = RequestMethod.GET)
	public ModelAndView companyCheckList() {
		ModelAndView model = new ModelAndView();
		model.addObject("compList", companyService.getCompanyByStatus(STATUS_PENDING_FOR_APPROVAL));
		model.setViewName("/KPO/companyCheckList");
		return model;
	}
	
}
