package com.fasset.controller;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.exceptions.MyWebException;
import com.fasset.form.QuotationForm;

import com.fasset.pdf.QuotationPdfView;
import com.fasset.service.interfaces.ICompanyStatutoryTypeService;
import com.fasset.service.interfaces.IFrequencyService;
import com.fasset.service.interfaces.IMailService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IServiceMaster;
import com.fasset.service.interfaces.IYearEndingService;
import com.fasset.service.interfaces.IindustryTypeService;

import com.fasset.controller.validators.QuotationValidator;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Quotation;
import com.fasset.entities.User;

@Controller
@SessionAttributes("user")
public class QuotationControllerMVC extends QuotationPdfView{

	@Autowired	
	private QuotationValidator QuotationValidator;
	
	@Autowired	
	private IQuotationService quoteService;	
	
	
	@Autowired
	private ICompanyStatutoryTypeService companyTypeService;
	
	@Autowired
	private IindustryTypeService industryTypeService;
	
	@Autowired
	private IFrequencyService frequencyService ;
	
	@Autowired
	private IServiceMaster serviceMaster ;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired	
	private IYearEndingService yearService;

	
	private Long QuoatId ;
	
	/////Admin//////
	@RequestMapping(value = "quotationList", method = RequestMethod.GET)
	public ModelAndView quotationList(@RequestParam(value = "msg", required = false)String msg,HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("quoteList", quoteService.findAllListing());
		model.setViewName("/KPO/quoteList");
		return model;
	}
	
	////////View///////
	@RequestMapping(value = "viewQuotation", method = RequestMethod.GET)
	public ModelAndView viewQuotation(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		Quotation quotation = new Quotation();
		HttpSession session = request.getSession(true);
		
		if((String) session.getAttribute("msg")!=null)
		{
		model.addObject("successMsg", (String) session.getAttribute("msg"));
		session.removeAttribute("msg");
		}
		
		try {
			quotation = quoteService.getById(id);	
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		
		
		model.addObject("quotation",quotation);
		model.addObject("type",quotation.getCompanystatutorytype());
		model.addObject("industryType",quotation.getIndustrytype());
		model.addObject("quoteDetails",quoteService.findAllDetailsList(id));
		model.addObject("view","view");
		model.setViewName("/KPO/quoteView");
		return model;
	}
	
	
	////Save////
	@RequestMapping(value = "saveQuotation", method = RequestMethod.POST)
	public ModelAndView saveQuotation(@ModelAttribute("quotation")Quotation quotation, BindingResult result,HttpServletRequest request, HttpServletResponse response) throws MyWebException {
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");	
		ModelAndView model = new ModelAndView();
		Long save_id = quotation.getSave_id();
		
		QuotationValidator.validate(quotation, result);
		if(result.hasErrors()){
			
			String msg ="Please select service, frequency and amount for quotation details";
			
			model.addObject("quotation",quoteService.getById(quotation.getQuotation_id()));
			model.addObject("successMsg",msg);
			model.addObject("frequencyList",frequencyService.findAll());
			model.addObject("serviceList",serviceMaster.findAll());
			model.addObject("quoteDetails",quoteService.findAllDetailsList(quotation.getQuotation_id()));
			model.addObject("companyTypeList",  companyTypeService.findAll());
			model.addObject("industryTypeList", industryTypeService.findAll());
			model.setViewName("/KPO/quotation");		
			return model;
		}
		else{
			String msg = "";
			if(save_id==0)
			{
			try
			{
			if(quotation.getQuotation_id()!=null)
			{
			long id = quotation.getQuotation_id();
				if(id > 0){
					msg =quoteService.updateQuatationDetails(quotation);
									
				}
			}				
				else{
					quoteService.savequote(quotation);
				}			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		
			model.addObject("quotation",quoteService.getById(quotation.getQuotation_id()));
			model.addObject("frequencyList",frequencyService.findAll());
			model.addObject("serviceList",serviceMaster.findAll());
			model.addObject("quoteDetails",quoteService.findAllDetailsList(quotation.getQuotation_id()));
			model.addObject("companyTypeList",  companyTypeService.findAll());
			model.addObject("industryTypeList", industryTypeService.findAll());
			model.addObject("successMsg", msg);
			model.setViewName("/KPO/quotation");
			}
			else
			{
				msg="Quotation updated successfully";
				quoteService.updateQuatationDetailsAfterSaveAndBack(quotation);
				if(quotation.getQuotation_id()!=null)
				{
				yearService.saveActivityLogForm(user.getUser_id(), null, null, null, null, null, null, quotation.getQuotation_id(), null, null, null);
				}
				session.setAttribute("successMsg", msg);
				return new ModelAndView ("redirect:/quotationList");
				
				
			}
					
			return model;
		}
	}
	
	
	/////Edit////	
	@RequestMapping(value = "editQuotation", method = RequestMethod.GET)
	public ModelAndView editQuotation(@RequestParam("id") long id ,HttpServletRequest request, HttpServletResponse response) throws MyWebException {
		ModelAndView model = new ModelAndView();
		QuoatId=id;
		
		model.addObject("quotation",quoteService.getById(id));
		model.addObject("frequencyList",frequencyService.findAll());
		model.addObject("serviceList",serviceMaster.findAll());
		model.addObject("quoteDetails",quoteService.findAllDetailsList(id));
		model.addObject("companyTypeList",  companyTypeService.findAll());
		model.addObject("industryTypeList", industryTypeService.findAll());
		model.setViewName("/KPO/quotation");
		return model;
	}
	
	
	/////Delete////////////
	@RequestMapping(value ="deleteQuotation", method = RequestMethod.GET)
	public ModelAndView deleteQuotation(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg",  quoteService.deleteByIdValue(id));
		return new ModelAndView("redirect:/quotationList");
	}
	
	@RequestMapping(value ="deleteQuotationDetails", method = RequestMethod.GET)
	public ModelAndView deleteQuotationDetails(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) throws MyWebException {
		ModelAndView model = new ModelAndView();
		
	    model.addObject("successMsg", quoteService.deleteQuotationDetails(id));
		model.addObject("quotation",quoteService.getById(QuoatId));
		model.addObject("frequencyList",frequencyService.findAll());
		model.addObject("serviceList", serviceMaster.findAll());
		model.addObject("quoteDetails",quoteService.findAllDetailsList(QuoatId));
		model.addObject("companyTypeList", companyTypeService.findAll());
		model.addObject("industryTypeList", industryTypeService.findAll());
		model.setViewName("/KPO/quotation");	
		return model;
	}
	@RequestMapping(value = "importList", method = RequestMethod.GET)
	public ModelAndView importList(@RequestParam(value = "msg", required = false)String msg) {
		ModelAndView model = new ModelAndView();
		if(msg != null){
			model.addObject("successMsg", msg);
		}
		model.addObject("importList", quoteService.findAllimportDetailsList());
		model.setViewName("/KPO/importList");
		return model;
	}
	@RequestMapping(value="saveservicestatus", method=RequestMethod.POST)
	public @ResponseBody String saveservicestatus(@RequestBody Boolean status,HttpServletRequest request,@RequestParam("Id") long Id)
	{
		quoteService.saveupdatedservices(Id,status);
		return "Status Added Succesfully";		
	}
	@RequestMapping(value = "transactionimportList", method = RequestMethod.GET)
	public ModelAndView transactionimportList(@RequestParam(value = "msg", required = false)String msg) {
		ModelAndView model = new ModelAndView();
		if(msg != null){
			model.addObject("successMsg", msg);
		}
		model.addObject("importList", quoteService.findAllTransactioimportDetailsList());
		model.setViewName("/KPO/transactionImportList");
		return model;
	}
	/*@RequestMapping(value="savetransactionservicestatus", method=RequestMethod.POST)
	public @ResponseBody String savetransactionservicestatus(@RequestBody Boolean status,HttpServletRequest request,@RequestParam("Id") long Id)
	{
		quoteService.saveupdatedservices(Id,status);
		return "Status Added Succesfully";		
	}*/
	

	@RequestMapping(value = "sendmailQuotation", method = RequestMethod.GET)
	public ModelAndView sendmailQuotation(@RequestParam("id") long quote_id,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		
		QuotationForm form = new QuotationForm();
		Quotation quotation = new Quotation();		
		try {
			System.out.println("quote_id"+quote_id);
			if(quote_id > 0){
				quotation = quoteService.getById(quote_id);
				
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		form.setQuotation(quotation);
		form.setQuoteDetails(quoteService.findAllDetailsList(quote_id));
		ByteArrayOutputStream baos = createTemporaryOutputStream();
		QuotationControllerMVC obj = new QuotationControllerMVC();
		
		try {
			baos=obj.GeneratePDf(new HashMap<String, Object>(),request,response,form);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		 try {
			 
			mailService.sendQuotationMail(quotation, baos);
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}
		 session.setAttribute("msg", "Quotation details sent to the "+quotation.getEmail());
		 return new ModelAndView("redirect:/viewQuotation?id="+quote_id);
	 
		
	}
/////Admin//////
	@RequestMapping(value = "showQuotationWithoutRegistration", method = RequestMethod.GET)
	public ModelAndView showQuotationWithoutRegistration(@RequestParam(value = "msg", required = false)String msg) {
		ModelAndView model = new ModelAndView();
		CopyOnWriteArrayList<Quotation>list =quoteService.findAllQuotations();
		List<ClientSubscriptionMaster>listall=quoteService.findAllSubscriptionList();
		for (ClientSubscriptionMaster clientmaster : listall) {
		
			if(clientmaster.getQuotation_id()!=null)
			{
				for(Quotation quotation:list)
				{
					if(clientmaster.getQuotation_id().getQuotation_id().equals(quotation.getQuotation_id()))
					{
				    list.remove(quotation);
					}
				}
			}
		}
		model.addObject("quoteList",list );
		model.setViewName("/KPO/showQuotationWithoutRegistrationList");
		return model;
	}
}


