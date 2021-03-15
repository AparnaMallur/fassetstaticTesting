package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.PurchaseReportFormValidator;
import com.fasset.entities.Company;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.PurchaseReportForm;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IPurchaseEntryService;

@Controller
@SessionAttributes("user")
public class PurchaseReportController extends MyAbstractController {
	
	@Autowired
	private PurchaseReportFormValidator  validator ;
	
	@Autowired
	private IPurchaseEntryService purchaseEntryService ;
	
	@Autowired
	private ICompanyService companyService ;	

	private List<Company> companyList = new ArrayList<Company>();
	private List<PurchaseEntry> purchaseEntryList= new ArrayList<PurchaseEntry>();
	private Long option = null;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;
	
	
	@RequestMapping(value = "purchaseReport", method = RequestMethod.GET)
	public ModelAndView purchaseReport(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");	
		User user = (User)session.getAttribute("user");	
		
		ModelAndView model = new ModelAndView();
		PurchaseReportForm form = new PurchaseReportForm();
		
		if (role == ROLE_SUPERUSER)
		{
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/purchaseReportForKpo");
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/purchaseReportForKpo");
		}
		else{
		
		model.addObject("form", form);
		model.setViewName("/report/purchaseReport");
			}
		return model;
	}
	
	@RequestMapping(value = "showPurchaseReport", method = RequestMethod.POST)
	public ModelAndView showPurchaseReport(@ModelAttribute("form")PurchaseReportForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		
		validator.validate(form, result);
		if(result.hasErrors()){
			model.setViewName("/report/purchaseReport");
		}
		else
		{
			purchaseEntryList.clear();
			for(PurchaseEntry entry : purchaseEntryService.getReport(form.getSubledgerId(), form.getFromDate(),form.getToDate(),company_id))
			{
				entry.setProductinfoList(purchaseEntryService.findAllPurchaseEntryProductEntityList(entry.getPurchase_id()));
				purchaseEntryList.add(entry);
			}
			option=form.getOption();
			fromDate=form.getFromDate();
			toDate = form.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(company_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date",toDate);
			model.addObject("option", option);
			model.addObject("purchaseEntryList", purchaseEntryList);
			model.setViewName("/report/purchaseEntryReportList");
		}
		
		return model;
	}
	
	
	@RequestMapping(value = "showPurchaseReportForKpo", method = RequestMethod.POST)
	public ModelAndView showPurchaseReportForKpo(@ModelAttribute("form")PurchaseReportForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		validator.validate(form, result);
		if(result.hasErrors()){
			model.addObject("companyList", companyList);
			model.setViewName("/report/purchaseReportForKpo");
		}
		else
		{
			purchaseEntryList.clear();
			for(PurchaseEntry entry : purchaseEntryService.getReport(form.getSubledgerId(), form.getFromDate(),form.getToDate(),form.getCompanyId()))
			{
				entry.setProductinfoList(purchaseEntryService.findAllPurchaseEntryProductEntityList(entry.getPurchase_id()));
				purchaseEntryList.add(entry);
			}
			option=form.getOption();
			fromDate=form.getFromDate();
			toDate = form.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(form.getCompanyId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date",toDate);
			model.addObject("option", option);
			model.addObject("purchaseEntryList", purchaseEntryList);
			model.setViewName("/report/purchaseEntryReportList");
		}
		return model;
		
	}
	
/*	@RequestMapping(value = "pdfPurchaseEntry", method = RequestMethod.GET)
	@ResponseBody
	public void pdfPayment(HttpServletRequest request,HttpServletResponse response) throws MyWebException {
		
		String docs = getRuta();
		String docName = "PurchaseEntry" + ".pdf";
		docs += docName;
		printDocument(docs, request);
		FileSystemResource fp = new FileSystemResource(docs);
		InputStream is;
		try {
			is = new FileInputStream(fp.getFile());
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=" + fp.getFilename());
			IOUtils.copy(is, response.getOutputStream());
			is.close();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printDocument(String docs, HttpServletRequest request) {
		
		try {
		Locale locale = RequestContextUtils.getLocale(request);
		final DefaultResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader.getResource("classpath:logo-top.png");
		File file = resource.getFile();
		String path = file.getAbsoluteFile().getParentFile().getAbsolutePath();
		String imgPath = path.substring(0, path.lastIndexOf(getAppName())) + getAppName() + File.separator
				+ "resources" + File.separator + "images" + File.separator+ File.separator;

		final File fl = new ClassPathResource(
				"com/fasset/reports/whats_next_" + "I130" + "_" + locale.getLanguage() + ".jrxml").getFile();
		path = fl.getAbsoluteFile().getParentFile().getAbsolutePath() + File.separator;
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("imgPath", imgPath);
		parameters.put("SUBREPORT_DIR", path);

		final JasperDesign jd = JRXmlLoader.load(fl);
		final JRBeanCollectionDataSource datosReport = new JRBeanCollectionDataSource(purchaseEntryList);
		final JasperReport report = JasperCompileManager.compileReport(jd);
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, datosReport);
		
		JasperExportManager.exportReportToPdfFile(print, docs);
	}
		catch (Exception e) {
		
			e.printStackTrace();
	}
		
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}*/
}