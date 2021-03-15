package com.fasset.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.data.time.Year;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.ManualJournalVoucherValidator;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Company;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IManualJournalVoucherService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.fasset.service.interfaces.ICustomerService; 

@Controller
@SessionAttributes("user")
public class ManualJournalVoucherController extends MyAbstractController{

	@Autowired
	private ManualJournalVoucherValidator validator;
	
	@Autowired
	private IManualJournalVoucherService journalService;
	
	@Autowired
	private IAccountingYearService accountingYearService ;
	
	@Autowired
	private ICommonService commonService;

	@Autowired
	ISubLedgerService subledgerService;
	
	@Autowired
	ISuplliersService supplierService;
	
	
	@Autowired
	ICustomerService customerService;
	
	@Autowired
	private ICompanyService companyService ;
	
    private Long mjvdetail_id;
	 
	@RequestMapping(value = "manualJournalVoucher", method = RequestMethod.GET)
	public ModelAndView manualJournalVoucher(HttpServletRequest request, HttpServletResponse response) 
	{
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		Long role = (long) session.getAttribute("role");
		
		
		model.addObject("journalVoucher", new ManualJournalVoucher());
		if (role.equals(ROLE_SUPERUSER) || role.equals(ROLE_EXECUTIVE)|| role.equals(ROLE_MANAGER)) 
		{
			String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018
			String currentyear1 = null;
			String lastyear = null;
			
			Integer year1 = Integer.parseInt(currentyear);
			year1 = year1 - 1;
			String lastYear = year1.toString();
			currentyear1 = lastYear + "-" + currentyear;

			Integer year2 = Integer.parseInt(currentyear);
			year2 = year2 + 1;
			String nextYear = year2.toString();
			lastyear = currentyear + "-" + nextYear;
			
			List<AccountingYear> yearList = new ArrayList<>();
			for(AccountingYear accyear : accountingYearService.findAll())
			{
				if(accyear.getYear_range().equals(currentyear1))
				{
					yearList.add(accyear);
				}
				if(accyear.getYear_range().equals(lastyear))
				{
					yearList.add(accyear);
				}
			}
			model.addObject("yearList", yearList);
		if (role.equals(ROLE_SUPERUSER) ) 
		{
		
			List<Company> companyList = companyService.getAllCompaniesOnly();
		model.addObject("companyList", companyList);
		model.setViewName("/transactions/manualJournalVoucherforKPO");
		
		}
		else if(role.equals(ROLE_EXECUTIVE) || role.equals(ROLE_MANAGER)) {
			List<Company> companyList  = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("companyList", companyList);
			model.setViewName("/transactions/manualJournalVoucherforKPO");
			
		}
		return model;
		}
		else if(role.equals(ROLE_CLIENT)|| role.equals(ROLE_EMPLOYEE))
		{
			String yearrange = user.getCompany().getYearRange();
			List<AccountingYear>  yearList = accountingYearService.findAccountRange(user.getUser_id(), yearrange,user.getCompany().getCompany_id());
			
			if(yearList.size()!=0)
			{
				
			model.addObject("yearList", yearList);
			model.addObject("subledgerList", subledgerService.findAllSubLedgersOnlyOfCompany(user.getCompany().getCompany_id()));
			model.addObject("supplierList",supplierService.findAllSuppliersOnlyOfCompany(user.getCompany().getCompany_id()));
			model.addObject("customerList",customerService.findAllCustomersOnlyOFCompany(user.getCompany().getCompany_id()));
			model.setViewName("/transactions/manualJournalVoucher");
			return model;
			}
			else
			{
				session.setAttribute("msg","Your account is closed");
				return new ModelAndView("redirect:/manualjournalVoucherList");
				
			}
		}
		else
		{
			session.setAttribute("msg","You are not authorised for this transaction.");
			return new ModelAndView("redirect:/manualjournalVoucherList");
		}
		
	}
	@RequestMapping(value = "saveJournalVoucher", method = RequestMethod.POST)
	public ModelAndView saveJournalVoucher(@ModelAttribute("journalVoucher")ManualJournalVoucher journalVoucher,
			BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		User user = new User();
 		user = (User)session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		long user_id=(long)session.getAttribute("user_id");
		Long role = (long) session.getAttribute("role");
		ModelAndView model = new ModelAndView();
		validator.validate(journalVoucher, result);
		if(result.hasErrors()){
			model.addObject("yearList", accountingYearService.findAccountRange(user_id, yearrange,user.getCompany().getCompany_id()));
			model.addObject("subledgerList", subledgerService.findAllSubLedgersOnlyOfCompany(user.getCompany().getCompany_id()));
			model.setViewName("/transactions/manualJournalVoucher");
			return model;
		}
		else{
			String msg = "";
			try{
				if(journalVoucher.getJournal_id()!=null){
					if(journalVoucher.getJournal_id() > 0){
						journalVoucher.setUpdated_by(user);
						journalService.update(journalVoucher);
						
						msg = "Journal voucher updated successfully";						
					}
				}			
				else{
					journalVoucher.setCreated_by(user);
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            journalVoucher.setIp_address(remoteAddr);
			            
			            if (role.equals(ROLE_SUPERUSER) || role.equals(ROLE_EXECUTIVE)|| role.equals(ROLE_MANAGER)) 
			    		{
			            journalVoucher.setCompany(companyService.getById(journalVoucher.getCompany_id()));
			            journalVoucher.setVoucher_no(commonService.getVoucherNumber("JV", VOUCHER_MANUAL_JOURNAL, journalVoucher.getDate(),journalVoucher.getYear_id(), journalVoucher.getCompany_id()));	
			    		}
			            else
			            {
			            	journalVoucher.setCompany(user.getCompany());
			            	journalVoucher.setVoucher_no(commonService.getVoucherNumber("JV", VOUCHER_MANUAL_JOURNAL, journalVoucher.getDate(),journalVoucher.getYear_id(), user.getCompany().getCompany_id()));	
			            }
			            journalVoucher.setEntry_status(MyAbstractController.ENTRY_PENDING);
			            
					journalService.saveMJV(journalVoucher);
					msg = "Journal voucher added successfully";
				}			
			}
			catch(Exception e){
				e.printStackTrace();
			}	
			  session.setAttribute("msg", msg);
			  return new ModelAndView("redirect:/manualjournalVoucherList");
			
		}
	
	}
	
	@RequestMapping(value = "manualjournalVoucherList", method = RequestMethod.GET)
	public ModelAndView manualjournalVoucherList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		Long role = (long) session.getAttribute("role");
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		if (role.equals(ROLE_SUPERUSER) || role.equals(ROLE_EXECUTIVE)|| role.equals(ROLE_MANAGER)) 
		{
			model.addObject("manualjournalVoucherList", journalService.findAllManualJournalVoucherReletedToCompany());
		}
		else
		{
			
			model.addObject("manualjournalVoucherList", journalService.findAllManualJournalVoucherReletedToCompany(company_id));
		}
		
		model.setViewName("/transactions/manualjournalVoucherList");
		return model;
	}
	
	
	@RequestMapping(value = "editJournal", method = RequestMethod.GET)
	public ModelAndView editJournal(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response ) {
		HttpSession session = request.getSession(true);
		User user = (User)session.getAttribute("user");
		Long role = (long) session.getAttribute("role");
		ModelAndView model = new ModelAndView();
		List<ManualJVDetails>  crmanualdetail= new ArrayList<ManualJVDetails>();
		List<ManualJVDetails>  drmanualdetail= new ArrayList<ManualJVDetails>();
		ManualJournalVoucher journal = new ManualJournalVoucher();
		
		mjvdetail_id=id;
		if(id > 0){
			try {
				journal = journalService.getById(id);
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(journal.getAccounting_year_id()!=null)
			{
			journal.setYear_id(journal.getAccounting_year_id().getYear_id());
			}
			
		}
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		for(ManualJVDetails detail: journalService.getAllMJVDetails(id))
		{
			if(detail.getDramount()!=null && detail.getDramount()>0)
			{
				drmanualdetail.add(detail);
			}
			else
			{
				crmanualdetail.add(detail);
			}
		}
		Collections.sort(drmanualdetail, new Comparator<ManualJVDetails>() {
	        public int compare(ManualJVDetails o1, ManualJVDetails o2) {

	        	Long detailid1 = o1.getDetailjv_id();
	        	Long detailid2= o2.getDetailjv_id();
	        	return detailid1.compareTo(detailid2);

	          
	    }});
		Collections.sort(crmanualdetail, new Comparator<ManualJVDetails>() {
	        public int compare(ManualJVDetails o1, ManualJVDetails o2) {

	        	Long detailid1 = o1.getDetailjv_id();
	        	Long detailid2= o2.getDetailjv_id();
	        	return detailid1.compareTo(detailid2);

	          
	    }});
		
		model.addObject("mjvDetailList", journalService.getAllMJVDetails(id));
		model.addObject("drmanualdetail", drmanualdetail);
		model.addObject("crmanualdetail", crmanualdetail);
		model.addObject("journalVoucher", journal);
		
		if (role.equals(ROLE_SUPERUSER) || role.equals(ROLE_EXECUTIVE)|| role.equals(ROLE_MANAGER)) 
		{
			if(journal.getCompany()!=null)
			{
				journal.setCompany_id(journal.getCompany().getCompany_id());
			}
			String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018
			String currentyear1 = null;
			String lastyear = null;
			
			Integer year1 = Integer.parseInt(currentyear);
			year1 = year1 - 1;
			String lastYear = year1.toString();
			currentyear1 = lastYear + "-" + currentyear;

			Integer year2 = Integer.parseInt(currentyear);
			year2 = year2 + 1;
			String nextYear = year2.toString();
			lastyear = currentyear + "-" + nextYear;
				
			List<AccountingYear> yearList = new ArrayList<>();
			for(AccountingYear accyear : accountingYearService.findAll())
			{
				if(accyear.getYear_range().equals(currentyear1))
				{
					yearList.add(accyear);
				}
				if(accyear.getYear_range().equals(lastyear))
				{
					yearList.add(accyear);
				}
			}
			model.addObject("yearList", yearList);
		if (role.equals(ROLE_SUPERUSER)) 
		{
		List<Company> companyList = companyService.getAllCompaniesOnly();
		model.addObject("companyList", companyList);
		model.setViewName("/transactions/manualJournalVoucherforKPO");
		
		}
		else if(role.equals(ROLE_EXECUTIVE) || role.equals(ROLE_MANAGER)) {
			List<Company> companyList  = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("companyList", companyList);
			model.setViewName("/transactions/manualJournalVoucherforKPO");
			
		}
		return model;
		}
		else if(role.equals(ROLE_CLIENT)|| role.equals(ROLE_EMPLOYEE))
		{
			String yearrange = user.getCompany().getYearRange();
			List<AccountingYear>  yearList = accountingYearService.findAccountRange(user.getUser_id(), yearrange,user.getCompany().getCompany_id());
			
			if(yearList.size()!=0)
			{
			model.addObject("yearList", yearList);
			model.addObject("subledgerList", subledgerService.findAllSubLedgersOnlyOfCompany(user.getCompany().getCompany_id()));
			model.setViewName("/transactions/manualJournalVoucher");
			
			}
			return model;
		}
		else
		{
			session.setAttribute("msg","You are not authorised for this transaction.");
			return new ModelAndView("redirect:/manualjournalVoucherList");
		}
	
	}
	
	@RequestMapping(value = "viewJournal", method = RequestMethod.GET)
	public ModelAndView viewJournal(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		List<ManualJVDetails>  crmanualdetail= new ArrayList<ManualJVDetails>();
		List<ManualJVDetails>  drmanualdetail= new ArrayList<ManualJVDetails>();
		ManualJournalVoucher voucher =null;
		try {
			voucher = journalService.getById(id);
			model.addObject("journalVoucher", voucher);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		for(ManualJVDetails detail: journalService.getAllMJVDetails(id))
		{
			if(detail.getDramount()!=null && detail.getDramount()>0)
			{
				drmanualdetail.add(detail);
			}
			else
			{
				crmanualdetail.add(detail);
			}
		}
		Collections.sort(drmanualdetail, new Comparator<ManualJVDetails>() {
	        public int compare(ManualJVDetails o1, ManualJVDetails o2) {

	        	Long detailid1 = o1.getDetailjv_id();
	        	Long detailid2= o2.getDetailjv_id();
	        	return detailid1.compareTo(detailid2);

	          
	    }});
		Collections.sort(crmanualdetail, new Comparator<ManualJVDetails>() {
	        public int compare(ManualJVDetails o1, ManualJVDetails o2) {

	        	Long detailid1 = o1.getDetailjv_id();
	        	Long detailid2= o2.getDetailjv_id();
	        	return detailid1.compareTo(detailid2);

	          
	    }});
		if(voucher.getCreated_by()!=null)
		{
		model.addObject("created_by", voucher.getCreated_by());
		}
		if(voucher.getUpdated_by()!=null)
		{
		model.addObject("updated_by",voucher.getUpdated_by());
		}
		model.addObject("mjvDetailList", journalService.getAllMJVDetails(id));
		model.addObject("drmanualdetail", drmanualdetail);
		model.addObject("crmanualdetail", crmanualdetail);
		model.setViewName("/transactions/manualJournalVoucherView");
		return model;
	}
	
	@RequestMapping(value ="deleteJournal", method = RequestMethod.GET)
	public ModelAndView deleteJournal(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response) 
	{		
		
		HttpSession session = request.getSession(true);
		session.setAttribute("msg", journalService.deleteByIdValue(id));
		return new ModelAndView("redirect:/manualjournalVoucherList");
	}
	

	@RequestMapping(value ="deleteMVJDetail", method = RequestMethod.GET)
	public ModelAndView deleteMJVJournal(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response) 
	{		
		
		HttpSession session = request.getSession(true);
		session.setAttribute("msg", journalService.deleteByMJVIdValue(id));
		 return new ModelAndView("redirect:/editJournal?id="+mjvdetail_id);
				
	}
	@RequestMapping(value="getSubledgers" , method=RequestMethod.POST)
	public  @ResponseBody List<SubLedger> getSubLedgerList(@RequestParam("id") Long id)
	{
		
		return subledgerService.findAllSubLedgersOnlyOfCompany(id);
		
	}
}