package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import com.fasset.controller.validators.ReceiptReportFormValidator;
import com.fasset.entities.Company;
import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.BillsPayableReportForm;
import com.fasset.form.ReceiptReportForm;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IReceiptService;

@Controller
@SessionAttributes("user")
public class ReceiptReportController extends MyAbstractController{
	
	@Autowired
	private ReceiptReportFormValidator validator;
	
	@Autowired
	private IReceiptService receiptService;
	
	@Autowired
	private ICompanyService companyService ;	
	
	private List<Company> companyList = new ArrayList<Company>();
	private List<Receipt> receiptList= new ArrayList<Receipt>();
	
	
	
	private Long option=null;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;
	
	@RequestMapping(value = "receiptReport", method = RequestMethod.GET)
	public ModelAndView receiptReport(HttpServletRequest request, HttpServletResponse response) {
			
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");	
		User user = (User)session.getAttribute("user");	
		ModelAndView model = new ModelAndView();
		ReceiptReportForm form = new ReceiptReportForm();
		
		if (role == ROLE_SUPERUSER)
		{
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/receiptReportForKpo");
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/receiptReportForKpo");
		}
		else {
			model.addObject("form", form);
		    model.setViewName("/report/receiptReport");
		}
		return model;
	}
	
	@RequestMapping(value = "showReceiptReport", method = RequestMethod.POST)
	public ModelAndView showReceiptReport(@ModelAttribute("form")ReceiptReportForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");

		validator.validate(form, result);
		if(result.hasErrors()){
			model.setViewName("/report/receiptReport");
		}
		else
		{
			receiptList.clear();
			
			for(Receipt rec : receiptService.getReport(form.getSubledgerId(), form.getFromDate(),form.getToDate(),company_id))
			{
				rec.setProductinfoList(receiptService.findAllReceiptProductEntityList(rec.getReceipt_id()));
				receiptList.add(rec);
				
			}
			System.out.println("receiptList " +receiptList.size());
			
			option = form.getOption();
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
			model.addObject("option",option);
			model.addObject("subledger_id",form.getSubledgerId());
			model.addObject("receiptList", receiptList);
			
			model.setViewName("/report/receiptReportList");
		}
		
		return model;
	}
	
	@RequestMapping(value = "showReceiptReportforKpo", method = RequestMethod.POST)
	public ModelAndView showReceiptReportforKpo(@ModelAttribute("form")ReceiptReportForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		System.out.println("rect repo admin "+form.getCompanyId());
		validator.validate(form, result);
		if(result.hasErrors()){
			model.addObject("companyList", companyList);
			model.setViewName("/report/receiptReportForKpo");
		}
		else
		{
			option = form.getOption();
			fromDate=form.getFromDate();
			toDate = form.getToDate();
			receiptList.clear();
			
			for(Receipt rec : receiptService.getReport(form.getSubledgerId(), form.getFromDate(),form.getToDate(),form.getCompanyId()))
			{
				rec.setProductinfoList(receiptService.findAllReceiptProductEntityList(rec.getReceipt_id()));
				receiptList.add(rec);
				
			}
			try {
				company = companyService.getCompanyWithCompanyStautarType(form.getCompanyId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date",toDate);
			model.addObject("option",option);
			model.addObject("subledger_id",form.getSubledgerId());
			model.addObject("receiptList", receiptList);
			
			model.setViewName("/report/receiptReportList");
		}
		return model;
		
	}
	
	@RequestMapping(value = "pdfReceiptReport", method = RequestMethod.GET)
    public ModelAndView downloadExcel() 
	{
		ReceiptReportForm form = new ReceiptReportForm();
		form.setReceiptList(receiptList);
		form.setCompany(company);
		form.setFromDate(fromDate);
		form.setToDate(toDate);
		form.setOption(option);
		return new ModelAndView("ReceiptReport", "form", form);
   
    }
	
	
	
	
	
	
	  /*  @RequestMapping(value = "pdfReceipt", method = RequestMethod.GET)
		@ResponseBody
		public void pdfReceipt(@RequestParam("id") Long id,
				HttpServletRequest request,HttpServletResponse response) throws MyWebException {
	    	Receipt receipt = new Receipt();
			try {
				receipt = receiptService.getCompanyWithCompanyStautarType(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String docs = getRuta();
			String docName = "Receipt" + ".pdf";
			docs += docName;
			printDocument(docs, request,receipt);
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

		private void printDocument(String docs, HttpServletRequest request,Receipt receipt ) {
			
			try {
			List<Receipt> list = new ArrayList<Receipt>();
			list.add(receipt);
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