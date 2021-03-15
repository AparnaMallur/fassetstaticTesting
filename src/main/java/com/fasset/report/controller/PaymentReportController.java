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
import com.fasset.controller.validators.PaymentReportValidator;
import com.fasset.entities.Company;
import com.fasset.entities.Payment;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.PaymentReportForm;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IPaymentService;

@Controller
@SessionAttributes("user")
public class PaymentReportController extends MyAbstractController {

	@Autowired
	private PaymentReportValidator validator;

	@Autowired
	private IPaymentService paymentService;
	
	@Autowired
	private ICompanyService companyService ;
	
	private List<Payment> paymentList = new ArrayList<Payment>();
	private List<Company> companyList = new ArrayList<Company>();
	
	private Long option = null;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;
	
	@RequestMapping(value = "paymentReport", method = RequestMethod.GET)
	public ModelAndView paymentReport(HttpServletRequest request, HttpServletResponse response) {

		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = user.getRole().getRole_id();
		ModelAndView model = new ModelAndView();
		PaymentReportForm form = new PaymentReportForm();
		
		if (role == ROLE_SUPERUSER){
			companyList = companyService.getAllCompaniesOnly();			
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/paymentReportForKpo");
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/paymentReportForKpo");
		}
		else {
			model.addObject("form", form);
			model.setViewName("/report/paymentReport");
		}
		return model;
	}

	@RequestMapping(value = "showPaymentReport", method = RequestMethod.POST)
	public ModelAndView showPaymentReport(@ModelAttribute("form") PaymentReportForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		
		HttpSession session = request.getSession(true);
		
		long company_id=(long)session.getAttribute("company_id");

		validator.validate(form, result);
		if (result.hasErrors()) {
			model.setViewName("/report/paymentReport");
		} else {
			paymentList.clear();
			paymentList= paymentService.getReport(form.getSubledgerId(), form.getFromDate(),form.getToDate(),company_id);
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
			model.addObject("paymentList", paymentList);
			model.setViewName("/report/paymentReportList");
		}
		return model;
	}

	@RequestMapping(value = "showPaymentReportForKpo", method = RequestMethod.POST)
	public ModelAndView showPaymentReportForKpo(@ModelAttribute("form") PaymentReportForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		validator.validate(form, result);
		if (result.hasErrors()) {
			
			model.addObject("companyList", companyList);
			model.setViewName("/report/paymentReportForKpo");
		} else {
			paymentList.clear();
			paymentList = paymentService.getReport(form.getSubledgerId(), form.getFromDate(),form.getToDate(),form.getCompanyId());
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
			model.addObject("option",option);
			model.addObject("subledger_id",form.getSubledgerId());
			model.addObject("paymentList", paymentList);
			model.setViewName("/report/paymentReportList");
		}
		
		return model;

	}

/*	@RequestMapping(value = "viewPaymentReport", method = RequestMethod.GET)
	public ModelAndView viewPaymentReport(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		Payment payment = new Payment();
		User user = (User) request.getSession().getAttribute("user");

		List<Suppliers> supplierList = suplliersService.findAllSuppliersOfCompany(user.getCompany().getCompany_id());
		List<Product> productList = productService.list();
		List<PurchaseEntry> purchaseEntryList = purchaseEntryService.findAll();
		Set<PaymentDetails> paymentDetailsList = new HashSet<PaymentDetails>();
		try {
			if (id > 0) {
				payment = paymentService.findOneWithAll(id);
				payment.setSupplierId(payment.getSupplier().getSupplier_id());
				paymentDetailsList = payment.getPaymentDetails();
				if (paymentDetailsList != null) {
					model.addObject("paymentDetailsList", paymentDetailsList);
				}
				payment.setDateString(payment.getDate().toString());
				payment.setChequeDateString(payment.getCheque_date().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addObject("payment", payment);
		model.addObject("supplierList", supplierList);
		model.addObject("productList", productList);
		model.addObject("purchaseEntryList", purchaseEntryList);
		model.setViewName("/report/paymentReportView");
		return model;
	}

	@RequestMapping(value = "paymentReportList", method = RequestMethod.GET)
	public ModelAndView paymentReportList(HttpServletRequest request, HttpServletResponse response) {
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		ModelAndView model = new ModelAndView();
		Long role = user.getRole().getRole_id();
		// List<Payment> paymentList= new ArrayList<Payment>();

		if (role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER || role == ROLE_MANAGER) {
			LocalDate fromDateOfKPO = (LocalDate) session.getAttribute("fromDateOfKPO");
			LocalDate toDateOfKPO = (LocalDate) session.getAttribute("toDateOfKPO");
			Long SuppilerIdOfKPO = (Long) session.getAttribute("SuppilerIdOfKPO");
			paymentList = paymentService.getReport(SuppilerIdOfKPO, fromDateOfKPO, toDateOfKPO,
					user.getCompany().getCompany_id());

		} else {
			LocalDate fromDateOfClient = (LocalDate) session.getAttribute("fromDateOfClient");
			LocalDate toDateOfClient = (LocalDate) session.getAttribute("toDateOfClient");
			Long SuppilerIdOfClient = (Long) session.getAttribute("SuppilerIdOfClient");
			paymentList = paymentService.getReport(SuppilerIdOfClient, fromDateOfClient, toDateOfClient,
					user.getCompany().getCompany_id());

		}
		model.addObject("paymentList", paymentList);
		model.setViewName("/report/paymentReportList");
		return model;

	}*/

	/*@RequestMapping(value = "pdfPayment", method = RequestMethod.GET)
	@ResponseBody
	public void pdfPayment(@RequestParam("id") Long id, HttpServletRequest request, HttpServletResponse response)
			throws MyWebException {
		Payment payment = new Payment();
		try {
			payment = paymentService.getCompanyWithCompanyStautarType(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String docs = getRuta();
		String docName = "Payment" + ".pdf";
		docs += docName;
		printDocument(docs, request, payment);
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

	private void printDocument(String docs, HttpServletRequest request, Payment payment) {

		try {
			List<Payment> list = new ArrayList<Payment>();
			list.add(payment);
			Locale locale = RequestContextUtils.getLocale(request);
			final DefaultResourceLoader loader = new DefaultResourceLoader();
			Resource resource = loader.getResource("classpath:logo-top.png");
			File file = resource.getFile();
			String path = file.getAbsoluteFile().getParentFile().getAbsolutePath();
			String imgPath = path.substring(0, path.lastIndexOf(getAppName())) + getAppName() + File.separator
					+ "resources" + File.separator + "images" + File.separator + File.separator;

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
		} catch (Exception e) {

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