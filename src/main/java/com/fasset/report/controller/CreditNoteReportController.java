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
import com.fasset.controller.validators.CreditNoteReportFormValidator;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.Customer;
import com.fasset.entities.Ledger;
import com.fasset.entities.User;
import com.fasset.form.CreditNoteReportForm;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICreditNoteService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.ILedgerService;

/**
 * @author "Vijay Ghodake"
 *
 */

@Controller
@SessionAttributes("user")
public class CreditNoteReportController extends MyAbstractController {

	@Autowired
	private ICreditNoteService creditNoteService;
	
	@Autowired
	private CreditNoteReportFormValidator creditNoteReportFormValidator;
	
	@Autowired
	private ICompanyService companyService ;
	
	@Autowired
	private ILedgerService ledgerService;
	
	@Autowired
	private ICustomerService customerService ;	
	
	private List<Customer> customerList = new ArrayList<Customer>();
	private List<CreditNote> creditNoteList = new ArrayList<CreditNote>();
	private List<Ledger> ledgerList = new ArrayList<Ledger>();
	private List<Company> companyList = new ArrayList<Company>();
	private Long option=null;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;
	
	@RequestMapping(value = "creditNoteReport", method = RequestMethod.GET)
	public ModelAndView creditNoteReport(HttpServletRequest request, HttpServletResponse response) {

		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role=(long)session.getAttribute("role");
		long company_id=(long)session.getAttribute("company_id");
		ModelAndView model = new ModelAndView();
		
		CreditNoteReportForm form = new CreditNoteReportForm();

		if (role == ROLE_SUPERUSER) {
			companyList = companyService.getAllCompaniesOnly();			
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/creditNoteReportForKpo");
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());			
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/creditNoteReportForKpo");
		} 
		else {
			try {
				customerList = customerService.findAllCustomersOfCompany(company_id);
				ledgerList = ledgerService.findAllLedgersOfCompany(company_id);

			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addObject("form", form);
			model.addObject("ledgerList", ledgerList);
			model.addObject("customerList", customerList);
			model.setViewName("/report/creditNoteReport");
		}
		return model;
	}

	@RequestMapping(value = "creditNoteReport", method = RequestMethod.POST)
	public ModelAndView creditNoteReport(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("form") CreditNoteReportForm creditNoteReportForm, BindingResult result) {
		ModelAndView model = new ModelAndView();
		
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		
		
		creditNoteReportFormValidator.validate(creditNoteReportForm, result);
		
		if (result.hasErrors()) {
			model.addObject("ledgerList", ledgerList);
			model.addObject("customerList", customerList);
			model.setViewName("/report/creditNoteReport");
			return model;
		} else {
			
			creditNoteList.clear();
			creditNoteList = creditNoteService.getReport(creditNoteReportForm.getCustomerId(),
					creditNoteReportForm.getFromDate(), creditNoteReportForm.getToDate(), user.getCompany().getCompany_id());
			
			option = creditNoteReportForm.getOption();
			fromDate=creditNoteReportForm.getFromDate();
			toDate = creditNoteReportForm.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date",toDate);
			model.addObject("option", option);
			model.addObject("creditNoteList", creditNoteList);
			model.setViewName("/report/creditNoteReportList");
		}
	
		return model;

	}
	
	@RequestMapping(value = "showCreditNoteReportforKpo", method = RequestMethod.POST)
	public ModelAndView showCreditNoteReportforKpo(@ModelAttribute("form") CreditNoteReportForm creditNoteReportForm, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		
		creditNoteReportFormValidator.validate(creditNoteReportForm, result);
		if(result.hasErrors()){
			model.addObject("companyList", companyList);
			model.setViewName("/report/creditNoteReportForKpo");
			return model;
		}else {
			creditNoteList.clear();
			creditNoteList = creditNoteService.getReport(creditNoteReportForm.getCustomerId(),
					creditNoteReportForm.getFromDate(), creditNoteReportForm.getToDate(), creditNoteReportForm.getCompanyId());
		
			option = creditNoteReportForm.getOption();
			fromDate=creditNoteReportForm.getFromDate();
			toDate = creditNoteReportForm.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(creditNoteReportForm.getCompanyId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addObject("company",company);
			model.addObject("from_date",fromDate);
			model.addObject("to_date",toDate);
			model.addObject("option", option);
			model.addObject("creditNoteList", creditNoteList);
			model.setViewName("/report/creditNoteReportList");
		}
			
		
		
		return model;
	}
	


	
	
	/*@RequestMapping(value = "pdfCreditNote", method = RequestMethod.GET)
	@ResponseBody
	public void pdfCreditNote(@RequestParam("id") Long id,
			HttpServletRequest request,HttpServletResponse response) throws MyWebException {
		CreditNote creditNote = new CreditNote();
		try {
			creditNote = creditNoteService.getCompanyWithCompanyStautarType(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String docs = getRuta();
		String docName = "CreditNote" + ".pdf";
		docs += docName;
		printDocument(docs, request,creditNote);
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

	private void printDocument(String docs, HttpServletRequest request, CreditNote creditNote) {
		
		try {
		List<CreditNote> list = new ArrayList<CreditNote>();
		list.add(creditNote);
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