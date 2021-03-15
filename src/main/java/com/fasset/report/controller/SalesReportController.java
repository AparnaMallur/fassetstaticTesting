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
import com.fasset.controller.validators.SalesReportValidator;
import com.fasset.entities.Company;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.SalesReportForm;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ISalesEntryService;

@Controller
@SessionAttributes("user")
public class SalesReportController extends MyAbstractController{

	@Autowired
	private ICompanyService companyService ;
	
	@Autowired
	private SalesReportValidator validator ;
	
	@Autowired
	private ISalesEntryService salesEntryService ;
	
	private List<Company> companyList = new ArrayList<Company>();
	private List<SalesEntry> salesEntryList= new ArrayList<SalesEntry>();
	private Integer option = null;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;
	
	@RequestMapping(value = "salesReport", method = RequestMethod.GET)
	public ModelAndView salesReport(HttpServletRequest request, HttpServletResponse response) {
	
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");	
		User user = (User)session.getAttribute("user");	
		ModelAndView model = new ModelAndView();
		SalesReportForm form = new SalesReportForm();
		
		if (role == ROLE_SUPERUSER)
		{
			companyList = companyService.getAllCompaniesOnly();			
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/salesReportForKpo");
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());		
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/salesReportForKpo");
		}
		else{
			model.addObject("form", form);
			model.setViewName("/report/salesReport");
		}
		return model;
	}
	
	
	@RequestMapping(value = "showSalesReport", method = RequestMethod.POST)
	public ModelAndView showSalesReport(@ModelAttribute("form")SalesReportForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		validator.validate(form, result);
		if(result.hasErrors()){
			model.setViewName("/report/salesReport");
		}
		else
		{
			fromDate=form.getFromDate();
			toDate = form.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(company_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			salesEntryList.clear();
			for(SalesEntry entry : salesEntryService.getReport(form.getSubledgerId(), form.getFromDate(),form.getToDate(),company_id))
			{
				entry.setProductinfoList(salesEntryService.findAllSalesEntryProductEntityList(entry.getSales_id()));
				salesEntryList.add(entry);
			}
			option = form.getOption();
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date",toDate);
			model.addObject("salesEntryList", salesEntryList);
			model.addObject("option", option);
			model.setViewName("/report/salesReportList");
		}
		
		return model;
	}
	
	@RequestMapping(value = "showSalesReportforKpo", method = RequestMethod.POST)
	public ModelAndView showSalesReportforKpo(@ModelAttribute("form")SalesReportForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();	
		validator.validate(form, result);
		if(result.hasErrors()){
			model.addObject("companyList", companyList);
			model.setViewName("/report/salesReportForKpo");
		}
		else
		{
			salesEntryList.clear();
			for(SalesEntry entry : salesEntryService.getReport(form.getSubledgerId(), form.getFromDate(),form.getToDate(),form.getCompanyId()))
			{
				entry.setProductinfoList(salesEntryService.findAllSalesEntryProductEntityList(entry.getSales_id()));
				salesEntryList.add(entry);
			}
			option = form.getOption();
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
			model.addObject("salesEntryList", salesEntryList);
			model.setViewName("/report/salesReportList");
		}
		return model;
		 
	}
	
	    @RequestMapping(value = "pdfSalesEntry", method = RequestMethod.GET)
	    public ModelAndView downloadExcel() {
	    	SalesReportForm form = new SalesReportForm();
	    	form.setOption(option);
	    	form.setSalesEntryList(salesEntryList);
	    	form.setCompany(company);
			form.setFromDate(fromDate);
			form.setToDate(toDate);
	    	return new ModelAndView("SalesEntryPdfView", "form", form);
	    }
	    
	    
	   /* @RequestMapping(value = "pdfSalesEntry", method = RequestMethod.GET)
		@ResponseBody
		public void pdfSalesEntry(@RequestParam("id") Long id,
				HttpServletRequest request,HttpServletResponse response) throws MyWebException {
        	SalesEntry entry = new SalesEntry();
			try {
				entry = salesEntryService.getCompanyWithCompanyStautarType(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String docs = getRuta();
			String docName = "SalesEntry" + ".pdf";
			docs += docName;
			printDocument(docs, request,entry);
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

		private void printDocument(String docs, HttpServletRequest request,SalesEntry entry) {
			
			try {
			List<SalesEntry> list = new ArrayList<SalesEntry>();
			list.add(entry);
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
			final JRBeanCollectionDataSource datosReport = new JRBeanCollectionDataSource(list);
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