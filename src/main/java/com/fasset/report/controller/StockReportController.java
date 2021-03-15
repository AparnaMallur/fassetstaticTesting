package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.StockReportValidator;
import com.fasset.entities.Company;
import com.fasset.entities.Product;
import com.fasset.entities.User;
import com.fasset.form.StockReportForm;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IProductService;
import com.fasset.service.interfaces.IStockService;

@Controller
@SessionAttributes("user")
public class StockReportController extends MyAbstractController{
	
	@Autowired
	private IProductService productService;

	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private StockReportValidator stockReportValidator;
	
	@Autowired
	private IStockService stockService;
	
	private List<Product> productList = new ArrayList<Product>();
	private List<Company> compList = new ArrayList<Company>();
	private List<StockReportForm> stockList = new ArrayList<StockReportForm>();
	private Company company;
	
	@RequestMapping(value = "stockReport", method = RequestMethod.GET)
	public ModelAndView stockReport(HttpServletRequest request, HttpServletResponse response){
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role=(long)session.getAttribute("role");
		ModelAndView model = new ModelAndView();
		StockReportForm stockReportForm = new StockReportForm();
			
		if (role == ROLE_SUPERUSER) {
			compList = companyService.getAllCompaniesOnly();
			model.addObject("stockReportForm", stockReportForm);
			model.addObject("compList", compList);
			model.setViewName("/report/stockReportForKpo");
		} 
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){ 
			compList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("stockReportForm", stockReportForm);
			model.addObject("compList", compList);
			model.setViewName("/report/stockReportForKpo");
		}else {
			productList = productService.findAllProductsOfCompany(user.getCompany().getCompany_id());
			model.addObject("stockReportForm", stockReportForm);
			model.addObject("productList", productList);
			model.setViewName("/report/stockReport");
		}		
		return model;
	}
	
	@RequestMapping(value = "stockReport", method = RequestMethod.POST)
	public ModelAndView stockReport(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("stockReportForm") StockReportForm stockReportForm, BindingResult result) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		stockReportValidator.validate(stockReportForm, result);
		if (result.hasErrors()) {
			model.addObject("productList", productList);
			model.setViewName("/report/stockReport");
			return model;
		} else {
			stockList.clear();
			stockList = stockService.getStockDetails(company_id, stockReportForm.getProductId());
			try {
				company = companyService.getCompanyWithCompanyStautarType(company_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		model.addObject("company", company);
		model.addObject("stockList", stockList);
		model.setViewName("/report/stockReportList");
		return model;

	}
	
	@RequestMapping(value = "stockReportforKpo", method = RequestMethod.POST)
	public ModelAndView stockReportforKpo(@ModelAttribute("stockReportForm") StockReportForm stockReportForm, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		stockReportValidator.validate(stockReportForm, result);
		if(result.hasErrors()){
			model.addObject("compList", compList);
			model.setViewName("/report/stockReportForKpo");
		}
		
		else
		{
			stockList.clear();
			stockList = stockService.getStockDetails(stockReportForm.getClientId(), stockReportForm.getProductId());
			try {
				company = companyService.getCompanyWithCompanyStautarType(stockReportForm.getClientId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addObject("company", company);

			model.addObject("stockList", stockList);
			model.setViewName("/report/stockReportList");
		}
		
		return model;
	}
	@RequestMapping(value="getproductListForStockReport" , method=RequestMethod.POST)
	public  @ResponseBody List<Product> getproductListForStockReport(@RequestParam("id") Long id)
	{
		
		List<Product> list = new ArrayList<>();
		for(Product pro : productService.findAllProductsOfCompany(id))
		{
			if(pro.getType()!=null && pro.getType()!=2) {
				list.add(pro);
			}
		}
		return list;
		
	}
}
