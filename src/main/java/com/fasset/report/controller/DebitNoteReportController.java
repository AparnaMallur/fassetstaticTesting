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
import com.fasset.controller.validators.DebitNoteReportFormValidator;
import com.fasset.entities.Company;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Ledger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.form.DebitNoteReportForm;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IDebitNoteService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.ISuplliersService;

@Controller
@SessionAttributes("user")
public class DebitNoteReportController extends MyAbstractController {
	
	@Autowired
	private DebitNoteReportFormValidator validator;
	
	@Autowired
	private IDebitNoteService debitNoteService;
	
	@Autowired
	private ISuplliersService suplliersService ;
	
	@Autowired
	private ICompanyService companyService ;
	
	@Autowired
	private ILedgerService ledgerService;
	
	private List<Suppliers> suppliersList = new ArrayList<Suppliers>();
	List<DebitNote> debitNoteList= new ArrayList<DebitNote>();
	private List<Ledger> ledgerList = new ArrayList<Ledger>();
	private List<Company> companyList = new ArrayList<Company>();
	private Long option=null;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;
	
	@RequestMapping(value = "debitNoteReport", method = RequestMethod.GET)
	public ModelAndView debitNoteReport(HttpServletRequest request, HttpServletResponse response) {
		
		
		HttpSession session = request.getSession(true);
		long role=(long)session.getAttribute("role");
		long company_id=(long)session.getAttribute("company_id");	
		User user = (User)session.getAttribute("user");	
		ModelAndView model = new ModelAndView();
		DebitNoteReportForm form = new DebitNoteReportForm();
		
		if (role == ROLE_SUPERUSER){			
			model.addObject("form", form);
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("companyList", companyList);
			model.setViewName("/report/debitNoteReportForKpo");
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());			
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/debitNoteReportForKpo");
		} 
		else{
		suppliersList = suplliersService.findAllSuppliersOfCompany(company_id);
		ledgerList = ledgerService.findAllLedgersOfCompany(company_id);
		model.addObject("form", form);
		model.addObject("ledgerList", ledgerList);
		model.addObject("suppliersList", suppliersList);
		model.setViewName("/report/debitNoteReport");
			}
		return model;
	}
	
	
	@RequestMapping(value = "showDebitNoteReport", method = RequestMethod.POST)
	public ModelAndView showDebitNoteReport(@ModelAttribute("form")DebitNoteReportForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();		
		HttpSession session = request.getSession(true);
		user = (User)session.getAttribute("user");
	
		validator.validate(form, result);
		if(result.hasErrors()){
			model.addObject("ledgerList", ledgerList);
			model.addObject("suppliersList", suppliersList);
			model.setViewName("/report/debitNoteReport");
			return model;
		}
		else{
			debitNoteList.clear();
			debitNoteList= debitNoteService.getReport(form.getSupplierId(), form.getFromDate(),form.getToDate(),user.getCompany().getCompany_id());
			fromDate=form.getFromDate();
			toDate = form.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			option = form.getOption();
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date",toDate);
			model.addObject("option",option);
			model.addObject("debitNoteList", debitNoteList);
			model.setViewName("/report/debitNoteReportList");
		}
		
		return model;
	}
	
	@RequestMapping(value = "showDebitNoteReportforKpo", method = RequestMethod.POST)
	public ModelAndView showDebitNoteReportforKpo(@ModelAttribute("form")DebitNoteReportForm form , BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		
        validator.validate(form, result);
		if(result.hasErrors()){
			model.addObject("companyList", companyList);
			model.setViewName("/report/debitNoteReportForKpo");
			return model;
		}
		else{
			
			debitNoteList.clear();
			debitNoteList= debitNoteService.getReport(form.getSupplierId(), form.getFromDate(),form.getToDate(),form.getCompanyId());
			
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
			option = form.getOption();
			model.addObject("option",option);
			model.addObject("debitNoteList", debitNoteList);
			model.setViewName("/report/debitNoteReportList");
		}
		return model;
		
	}
	
	/*@RequestMapping(value = "pdfDebitNote", method = RequestMethod.GET)
	@ResponseBody
	public void pdfDebitNote(@RequestParam("id") Long id,
			HttpServletRequest request,HttpServletResponse response) throws MyWebException {
		DebitNote debitNote = new DebitNote();
		try {
			debitNote = debitNoteService.getCompanyWithCompanyStautarType(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String docs = getRuta();
		String docName = "DebitNote" + ".pdf";
		docs += docName;
		printDocument(docs, request,debitNote);
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

	private void printDocument(String docs, HttpServletRequest request, DebitNote debitNote) {
		
		try {
			List<DebitNote> list = new ArrayList<DebitNote>();
			list.add(debitNote);
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