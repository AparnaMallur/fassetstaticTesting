
package com.fasset.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.DepreciationAutoJVValidator;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Company;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.DepreciationSubledgerDetails;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.entities.VoucherSeries;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IDepreciationAUtoJVService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IYearEndingService;



@Controller
@SessionAttributes("user")
public class DepreciationAutoJVController extends MyAbstractController {
	
	@Autowired
	private ISubLedgerService subledgerService;

	@Autowired
	private IAccountingYearService accountingYearService ;
		
	@Autowired
	private IDepreciationAUtoJVService depriciationService;

	@Autowired
	private IYearEndingService yearService;
	@Autowired
	private DepreciationAutoJVValidator validator;
	@Autowired
	private ICompanyService companyService;

	
	@Autowired
	private ICommonService commonService;
	
	@RequestMapping(value = "depreciationform", method = RequestMethod.GET)
	public ModelAndView getdepreciationForm(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session=request.getSession();
		DepreciationAutoJV depreciationAutoJV = new DepreciationAutoJV();
		User user= (User)session.getAttribute("user");
		session.removeAttribute("successMsg");
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
		if(yearList.size()!=0)
		{
		model.addObject("yearList", yearList);
		model.addObject("subledgerList", subledgerService.findAllSubLedgersForDepreciation(user.getCompany().getCompany_id()));
		model.addObject("subledgerDepriciaiton", "Depreciation");
		model.addObject("dAutoJV", depreciationAutoJV);
		model.setViewName("/transactions/depreciationAutoJV");
		return model;
		}
		else
		{
			session.setAttribute("msg","Your account is closed");
			return new ModelAndView("redirect:/depreciationAutoJVList");
			
		}
	}

	//deleteDepriciationAutoJV
	@RequestMapping(value = "deleteDepriciationAutoJV", method = RequestMethod.GET)
	public ModelAndView deleteDepriciationAutoJV(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) 
	{		
		HttpSession session=request.getSession(true);
		session.setAttribute("msg",depriciationService.deleteByIdValue(id));
		return new ModelAndView("redirect:/depreciationAutoJVList");
		
				
	}
	@RequestMapping(value = "depreciationAutoJVList", method = RequestMethod.GET)
	public ModelAndView depreciationAutoJVList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		List<YearEnding> yearEndlist = yearService.findAllYearEnding(company_id);
		session.removeAttribute("successMsg");
		Company company = null;
		try {
			company = companyService.getById(company_id);
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (company.getTrial_balance() != null) {
			if (company.getTrial_balance() == true)
				model.addObject("opening_flag", "1");
			else
				model.addObject("opening_flag", "0");
		} else
			model.addObject("opening_flag", "0");
		if((String) session.getAttribute("msg")!=null){
			model.addObject("successMsg", (String) session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
			model.addObject("depreciationAutoJVList", depriciationService.findAllDepreciationAutoJVReletedToCompany(company_id));
			model.addObject("yearEndlist", yearEndlist);
		
		model.setViewName("/transactions/depreciationAutoJVList");
		return model;
	}
	
	
	@RequestMapping(value = "saveDepreciationAutoJV", method = RequestMethod.POST)
	public ModelAndView saveDepreciationAutoJV( HttpServletRequest request, HttpServletResponse response,@ModelAttribute("dAutoJV") DepreciationAutoJV 
			depreciationAutoJV,BindingResult result) {
	
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		String yearrange = user.getCompany().getYearRange();
		long company_id=(long)session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
        String msg="";
        depreciationAutoJV.setAmount1((long)1);
        String remoteAddr = "";
		 remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
            depreciationAutoJV.setIp_address(remoteAddr);
            depreciationAutoJV.setCreated_by(user);  		  
		List<AccountingYear>  yearList = accountingYearService.findAccountRange(user.getUser_id(), yearrange,company_id);
		VoucherSeries series = new VoucherSeries();
		series.setVoucher_no(commonService.getVoucherNumberForAUtoJV(depreciationAutoJV.getDate(), user.getCompany().getCompany_id()));
		series.setVoucher_date(depreciationAutoJV.getDate());
		series.setCompany(user.getCompany());
		depreciationAutoJV.setVoucherSeries(series);
		//depreciationAutoJV.setVoucher_no(commonService.getVoucherNumber("DEPRECIATIONAUTOJV", DEPRECIATIONAUTOJV, depreciationAutoJV.getDate(),depreciationAutoJV.getAccountYearId(), user.getCompany().getCompany_id()));
		depreciationAutoJV.setCompany(user.getCompany());
			if (depreciationAutoJV.getDepreciation_id() != null && depreciationAutoJV.getDepreciation_id() > 0) {
				try {
					depriciationService.update(depreciationAutoJV);
					msg = "Depreciation Voucher Updated Successfully";
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				depriciationService.saveDepreciationAutoJV(depreciationAutoJV);
				msg = " Depreciation Auto JV saved Succesfully";
			}
		session.setAttribute("successMsg", msg);
		model.addObject("yearList", yearList);
		model.addObject("subledgerList", subledgerService.findAllSubLedgersForDepreciation(user.getCompany().getCompany_id()));
		model.setViewName("redirect:/depreciationAutoJVList");
		return model;	
	}
//	/viewDepreciationJournal
	@RequestMapping(value = "viewDepreciationJournal", method = RequestMethod.GET)
	public ModelAndView viewDepreciationJournal(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		List<DepreciationSubledgerDetails>  depreciationdetail= new ArrayList<DepreciationSubledgerDetails>();
		DepreciationAutoJV voucher =null;
		Double depreciaitonAmount=0.0;
		try {
			voucher = depriciationService.getById(id);
					//journalService.getById(id);
			model.addObject("journalVoucher", voucher);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		for(DepreciationSubledgerDetails deprisubdetail: depriciationService.getAllDepreciationSubledgerDetails(id))
		{
			depreciaitonAmount=deprisubdetail.getDepreciationAutoJV().getAmount();
			if(deprisubdetail.getSubledger()!=null && deprisubdetail.getSubLedgerAmount()>0)
			{
				depreciationdetail.add(deprisubdetail);
			}
			
		}
		Collections.sort(depreciationdetail, new Comparator<DepreciationSubledgerDetails>() {
	        public int compare(DepreciationSubledgerDetails o1, DepreciationSubledgerDetails o2) {

	        	Long detailid1 = o1.getDepreciationsubledgerdetails_id();
	        	Long detailid2= o2.getDepreciationsubledgerdetails_id();
	        	return detailid1.compareTo(detailid2);

	          
	    }});
		
		if(voucher.getCreated_by()!=null)
		{
		model.addObject("created_by", voucher.getCreated_by());
		}
		
		model.addObject("djvDetailList", depriciationService.getAllDepreciationSubledgerDetails(id));
		model.addObject("depreciationdetail", depreciationdetail);
		model.addObject("depreciaitonAmount", depreciaitonAmount);
		
		model.setViewName("/transactions/depreciationAutoJVView");
		return model;
	}
	
	@RequestMapping(value = "updateDepreciationJournal", method = RequestMethod.GET)
	public ModelAndView updateDepreciationJournal(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response ) {
		ModelAndView model = new ModelAndView();
		DepreciationAutoJV voucher =new DepreciationAutoJV();
		HttpSession session=request.getSession();
		User user= (User)session.getAttribute("user");
		session.removeAttribute("successMsg");
		String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018
		String currentyear1 = null;
		String lastyear = null;
		
		Integer year1 = Integer.parseInt(currentyear);
		year1 = year1 - 1;
		String lastYear = year1.toString();
		currentyear1 = lastYear + "/" + currentyear;

		Integer year2 = Integer.parseInt(currentyear);
		year2 = year2 + 1;
		String nextYear = year2.toString();
		lastyear = currentyear + "/" + nextYear;
			
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
		try 
			{
				voucher = depriciationService.getById(id);
				model.addObject("dAutoJV", voucher);
			} 
		catch (MyWebException e)
			{
				e.printStackTrace();
			}
		Set<DepreciationSubledgerDetails> depriciationSubledgerDetails = voucher.getDepriciationSubledgerDetails();
		Set<SubLedger> list = new HashSet<SubLedger>();
		for(DepreciationSubledgerDetails details : depriciationSubledgerDetails)
		{
			SubLedger sub = new SubLedger();
			sub.setSubledger_Id(details.getSubledger().getSubledger_Id());
			sub.setSubledger_name(details.getSubledger().getSubledger_name());
			sub.setDepSubLedgerAmount(details.getSubLedgerAmount());
			list.add(sub);
		}
		model.addObject("yearList", yearList);
		model.addObject("subledgerList", list);
		model.addObject("subledgerDepriciaiton", "Depreciation");
		model.setViewName("/transactions/depreciationAutoJV");
		return model;		
	}

}
