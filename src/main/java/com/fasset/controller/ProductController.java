package com.fasset.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.ProductValidator;
import com.fasset.entities.Company;

import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.Product;

import com.fasset.entities.TaxMaster;
import com.fasset.entities.UnitOfMeasurement;
import com.fasset.entities.User;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IGstTaxMasterService;
import com.fasset.service.interfaces.IProductService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.ITaxMasterService;
import com.fasset.service.interfaces.IUOMService;
import com.fasset.service.interfaces.IYearEndingService;

@Controller
@SessionAttributes("user")
public class ProductController extends MyAbstractController {

	@Autowired
	private IProductService productService;

	@Autowired
	private IUOMService uomService;

	@Autowired
	ProductValidator productValidator;

	@Autowired
	private ICompanyService companyService;

	@Autowired
	private ITaxMasterService taxMasterService;

	@Autowired
	private IGstTaxMasterService gstService;

	@Autowired
	private IQuotationService quoteservice;

	@Autowired
	private IClientSubscriptionMasterService subscribeservice;

	@Autowired	
	private IYearEndingService yearService;
	
	Boolean importFlag = null; // for maintaining the user on productList.jsp after delete and view
	Boolean isImport = false;  // is product saved or updated through excel or through System(Add Product)
	 private List<String> successList = new ArrayList<String>();
	 private List<String> failureList = new ArrayList<String>();
	 private List<String> updateList = new ArrayList<String>();
	 private  String successmsg;
	 private  String failmsg;
	 private  String updatemsg;
	 private  String error;
	 
	@RequestMapping(value = "productList", method = RequestMethod.GET)
	public ModelAndView productList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		boolean importfail = false;
		boolean importflag = true;
		importFlag = true;
		List<Product> productListFailure = new ArrayList<Product>();
		List<Product> productList = new ArrayList<Product>();
		
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		
		if (session.getAttribute("filemsg") != null) {
			model.addObject("filemsg", session.getAttribute("filemsg"));
			session.removeAttribute("filemsg");
		}

		if (role == ROLE_SUPERUSER || role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			if (role == ROLE_SUPERUSER) {
				productList = productService.findAllListing(true);
				productListFailure = productService.findAllListing(false);
			}
			if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				productList = productService.findAllListing(true, user.getUser_id());
				productListFailure = productService.findAllListing(false, user.getUser_id());
			}
			if (productListFailure.size() != 0)
				importfail = true;
		} else {
			productList = productService.findAllListingProductsOfCompany(company_id, true);
			productListFailure = productService.findAllListingProductsOfCompany(company_id, false);
			if (productListFailure.size() != 0)
				importfail = true;
			Long quote_id = subscribeservice.getquoteofcompany(company_id);
			String email = user.getCompany().getEmail_id();
			if (quote_id != 0) {
				importflag = quoteservice.findAllimportDetailsUser(email, quote_id); // for subscribed client if he registered his company with quotation and quotation contains master imports facility.
			} else {
				importflag = quoteservice.findAllimportDetailsUser(email, quote_id);
			}
		  /*if(user.getCompany().getCreated_by()!=null)// for company registered without quotation flow. This will be use for master imports as quotation is not present for this company.
			{
				importflag=true;
			}*/
		}
		
		if(isImport==false)
		{	// code for import . here we are refreshing list and massages which are declared as global.
			successList.clear();
			failureList.clear();
			updateList.clear();
			successmsg=null;
			failmsg=null;
			updatemsg=null;
			error=null; 
			// code for import . here we are refreshing list and massages which are declared as global.
		}
		else if(isImport==true)
        {
		// code for import
		  Collections.sort(successList, new Comparator<String>() {
	            public int compare(String string1, String string2) {
	            return string1.trim().toLowerCase().compareTo(string2.trim().toLowerCase());
	            }
	        });
		  Collections.sort(failureList, new Comparator<String>() {
	            public int compare(String string1, String string2) {
	            return string1.trim().toLowerCase().compareTo(string2.trim().toLowerCase());
	            }
	        });
		  Collections.sort(updateList, new Comparator<String>() {
	            public int compare(String string1, String string2) {
	            return string1.trim().toLowerCase().compareTo(string2.trim().toLowerCase());
	            }
	        });
		model.addObject("successList", successList);
		model.addObject("failureList",failureList);
		model.addObject("updateList",updateList);
		model.addObject("successImportmsg", successmsg);
		model.addObject("failmsg",failmsg);
		model.addObject("updatemsg",updatemsg);
		model.addObject("error",error);
		model.addObject("isImport",true);
		// code for import
		isImport=false;
	   }
		model.addObject("importfail", importfail);
		model.addObject("importflag", importflag);
		model.addObject("productList", productList);
		model.setViewName("/master/productList");
		return model;
	}
	@RequestMapping(value = "downloadProductMaster" , method =  RequestMethod.POST ,  produces =MediaType.APPLICATION_JSON_VALUE)
	//@RequestMapping(value = { "downloadProductMaster" }, method = { RequestMethod.POST })
	public @ResponseBody String downloadProductMaster(HttpServletRequest request,HttpServletResponse response)throws IOException {
		System.out.println("downloadproduct");
		
		
		HttpSession session = request.getSession(true);
		List<Product> productList = new ArrayList<>();
		long company_id = (long) session.getAttribute("company_id");
		
	
		  System.out.print("company_id "+company_id);
		
		
		productList =  productService.findAllProductsOfCompany(company_id);
		JSONArray jsonArray = new JSONArray();
		for(Product entry : productList) {
			JSONObject jsonObj = new JSONObject();
		//	jsonObj.put("FirstName", entry.getf);
			jsonObj.put("productname", entry.getProduct_name());
			jsonArray.put(jsonObj);
		}
		

		 return jsonArray.toString();
		/*Customer cust=new Customer();
		while (it.hasNext()) 
			cust=(Customer)it.next();
            System.out.print(cust.getFirm_name());*/
	}
		
	
	@RequestMapping(value = "productimportfailure", method = RequestMethod.GET)
	public ModelAndView productimportfailure(@RequestParam(value = "msg", required = false) String msg,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		List<Product> productList = new ArrayList<Product>();
		importFlag = false;
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (role == ROLE_SUPERUSER) {
			productList = productService.findAllListing(false);
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER){
			productList = productService.findAllListing(false, user.getUser_id());
		}
		else {
			productList = productService.findAllListingProductsOfCompany(company_id, false);
		}
		model.addObject("productList", productList);
		model.setViewName("/master/failureProductList");
		return model;
	}

	@RequestMapping(value = "product", method = RequestMethod.GET)
	public ModelAndView product(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		long user_id=(long)session.getAttribute("user_id");
		List<Company> companyList = new ArrayList<Company>();
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.list();
		}
		else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesExe(user_id);			
		}
		List<TaxMaster> taxList = taxMasterService.findAll();
		List<UnitOfMeasurement> uomList = uomService.list();

		model.addObject("companyList", companyList);
		model.addObject("taxList", taxList);
		model.addObject("uomList", uomList);
		model.addObject("product", new Product());
		model.setViewName("/master/product");
		return model;
	}

	@RequestMapping(value = "product", method = RequestMethod.POST)
	public ModelAndView product(@ModelAttribute("product") Product product, BindingResult result, HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> map) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		long user_id=(long)session.getAttribute("user_id");
		productValidator.validate(product, result);
		if (result.hasErrors()) {
			List<Company> companyList = new ArrayList<Company>();
			if (role == ROLE_SUPERUSER) {
				companyList = companyService.list();
			}
			else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
				companyList = companyService.getAllCompaniesExe(user_id);			
			}
			List<TaxMaster> taxList = taxMasterService.findAll();
			List<UnitOfMeasurement> uomList = uomService.list();

			model.addObject("companyList", companyList);
			model.addObject("taxList", taxList);
			model.addObject("uomList", uomList);
			model.setViewName("/master/product");
		} else {
			String msg = "";
			try {
				Long id = product.getProduct_id();
				if (id != null) {
					product.setUpdated_by(user);
					Product oldproduct=productService.findOneView(product.getProduct_id());
					if((oldproduct.getProduct_approval() == 1) || (oldproduct.getProduct_approval() == 3))
					{
						if (role == ROLE_EXECUTIVE)
							product.setProduct_approval(2);
						else if((role == ROLE_CLIENT) || (role == ROLE_EMPLOYEE) || (role == ROLE_TRIAL_USER))
							product.setProduct_approval(0);
						else
							product.setProduct_approval(oldproduct.getProduct_approval());
					}
					else
					{
						product.setProduct_approval(oldproduct.getProduct_approval());
					}	
					productService.update(product);
					msg = "Product updated successfully";

				} else {
					product.setProduct_approval(APPROVAL_STATUS_PENDING);
					if (role == ROLE_SUPERUSER || role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
						product.setCompany(companyService.getById(product.getCompany_id()));
					} else {
						product.setCompany(user.getCompany());
					}
					product.setFlag(true);
					product.setCreated_by(user);
					
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            product.setIp_address(remoteAddr);
					productService.add(product);
					msg = "Product added successfully";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/productList");
		}
		return model;
	}

	@RequestMapping(value = "editProduct", method = RequestMethod.GET)
	public ModelAndView editProduct(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		List<Company> companyList = new ArrayList<Company>();
		Product product = new Product();
		try {
			if (id > 0) {
				product = productService.findOneView(id);
				if (role == ROLE_SUPERUSER) {
					companyList = companyService.list();
					product.setCompany_id(product.getCompany().getCompany_id());
				}
				else if(role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
					companyList = companyService.getAllCompaniesExe(user_id);	
					product.setCompany_id(product.getCompany().getCompany_id());		
				}
				if (product.getTaxMaster() != null) 
					product.setVat_id(product.getTaxMaster().getTax_id());
				
				if (product.getUom() != null)
					product.setUnit(product.getUom().getUom_id());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<UnitOfMeasurement> uomList = uomService.list();
		List<TaxMaster> taxList = taxMasterService.findAll();
		model.addObject("companyList", companyList);
		model.addObject("uomList", uomList);
		model.addObject("taxList", taxList);
		model.addObject("product", product);
		model.setViewName("/master/product");
		return model;

	}

	@RequestMapping(value = "viewProduct", method = RequestMethod.GET)
	public ModelAndView viewProduct(@RequestParam("id") long id, @RequestParam("flag") long flag) {
		ModelAndView model = new ModelAndView();
		Product product = new Product();
		try {
			if (id > 0) {
				product = productService.findOneView(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("product", product);
		model.addObject("flag", flag);
		model.addObject("importFlag", importFlag);
		model.setViewName("/master/productView");
		return model;
	}

	@RequestMapping(value = "productPrimaryValidationList", method = RequestMethod.GET)
	public ModelAndView productPrimaryValidationList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		Product batchproduct = new Product();
		List<Product> productList = new ArrayList<Product>();
		
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}

		if (role == ROLE_SUPERUSER) {
			productList = productService.findByStatus(APPROVAL_STATUS_PENDING);
		}
		else if(role == ROLE_EXECUTIVE) {
			productList = productService.findByStatus(role,APPROVAL_STATUS_PENDING, user_id);	
		}
		model.addObject("productList", productList);
		model.addObject("batchproduct", batchproduct);
		model.setViewName("/master/productPrimaryValidationList");
		return model;
	}

	@RequestMapping(value = "productSecondaryValidationList", method = RequestMethod.GET)
	public ModelAndView productSecondaryValidationList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long user_id = (long) session.getAttribute("user_id");
		long role = (long) session.getAttribute("role");
		Product batchproduct = new Product();
		List<Product> productList = new ArrayList<Product>();
		
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		
		if (session.getAttribute("errorMsg") != null) {
			model.addObject("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		
		if (role == ROLE_SUPERUSER) {
			productList = productService.findByStatus(APPROVAL_STATUS_PRIMARY);
		}
		else if(role == ROLE_MANAGER) {
			productList = productService.findByStatus(role,APPROVAL_STATUS_PRIMARY, user_id);	
		}
		model.addObject("productList", productList);
		model.addObject("batchproduct", batchproduct);
		model.setViewName("/master/productSecondaryValidationList");
		return model;
	}

	@RequestMapping(value = "rejectProduct", method = RequestMethod.GET)
	public ModelAndView rejectProduct(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("rejectApproval") Boolean rejectApproval) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		Long productId = id;
		String msg = "";
		msg = productService.updateByApproval(productId, APPROVAL_STATUS_REJECT);
		yearService.saveActivityLogForm(user.getUser_id(), null, null, productId, null, null, null, null, null, null, (long)APPROVAL_STATUS_REJECT);
		session.setAttribute("successMsg", msg);
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && rejectApproval == true)) {
			model.setViewName("redirect:/productPrimaryValidationList");
		} else {
			model.setViewName("redirect:/productSecondaryValidationList");
		}
		return model;
	}

	@RequestMapping(value = "approveProduct", method = RequestMethod.GET)
	public ModelAndView approveProduct(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("primaryApproval") Boolean primaryApproval) {
		ModelAndView model = new ModelAndView();
		Long productId = id;
		String msg = "";
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		if (role == ROLE_EXECUTIVE || (role == ROLE_SUPERUSER && primaryApproval)) {
			msg = productService.updateByApproval(productId, APPROVAL_STATUS_PRIMARY);
			yearService.saveActivityLogForm(user.getUser_id(), null, null, productId, null, null, null, null, (long)APPROVAL_STATUS_PRIMARY, null, null);
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/productPrimaryValidationList");
		} else {
			msg = productService.updateByApproval(productId, APPROVAL_STATUS_SECONDARY);
			yearService.saveActivityLogForm(user.getUser_id(), null, null, productId, null, null, null, null, null, (long)APPROVAL_STATUS_SECONDARY, null);
			session.setAttribute("successMsg", msg);
			model.setViewName("redirect:/productSecondaryValidationList");
		}
		return model;
	}

	@RequestMapping(value = "deleteProduct", method = RequestMethod.GET)
	public ModelAndView deleteProduct(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String msg = "You can't delete Product";
		/*msg = productService.deleteByIdValue(id);*/
		session.setAttribute("successMsg", msg);
		
		if(importFlag == true)
		{
			
			return new ModelAndView("redirect:/productList");
		}
		else
		{
			return new ModelAndView("redirect:/productimportfailure");
		}
	}


	
	@RequestMapping(value = "deletefailureProductList", method = RequestMethod.GET)
	public ModelAndView deletefailureProductList(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		String msg = "You can't delete Product";
		/*msg = productService.deleteByIdValue(id);*/
		session.setAttribute("successMsg", msg);
		
		if(importFlag == true)
		{
			
			return new ModelAndView("redirect:/productList");
		}
		else
		{
			return new ModelAndView("redirect:/productimportfailure");
		}
	}
	
	
	
	@RequestMapping(value = "importExcelProduct", method = RequestMethod.POST)
	public ModelAndView importExcelLedger(@RequestParam("excelFile") MultipartFile excelfile, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView();
		boolean isValid = true;
		String success = "Record Imported successfully";
		int successcount = 0;
		String fail = "in failure list, Please refer failure list";
		int failcount = 0;
		String update = "Updated List";
		int updatecount = 0;
	    isImport=true;
	    successmsg="";
		failmsg="";
		updatemsg="";
		error=""; 
		successList.clear();
		failureList.clear();
		updateList.clear();
		int m = 0;
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		User user = new User();
		user = (User) session.getAttribute("user");
		long role = (long) session.getAttribute("role");
		List<UnitOfMeasurement> uomList = uomService.findAllListing();
	/*	List<GstTaxMaster> gstList = gstService.list();*/
		List<Company> companylist = companyService.findAll();
		try {
			if (excelfile.getOriginalFilename().endsWith("xls")) {
				System.out.println("file is xls...");

				int i = 0;
				// Creates a workbook object from the uploaded excelfile
				HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
				// Creates a worksheet object representing the first sheet
				HSSFSheet worksheet = workbook.getSheetAt(0);
				// Reads the data in excel file until last row is encountered
				int rowcount=0;
				while (i <= worksheet.getLastRowNum()) {
					int colcount=0;
					HSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 3; j++) {
						if (row.getCell(j) != null) {
							colcount++;
						}
					}
					if(colcount>=4)
					{
					rowcount++;
					}
				}

				if (isValid) {
					i = 1;
					while (i <= rowcount) {
						Product product = new Product();
						HSSFRow row = worksheet.getRow(i++);
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							
							Integer fvar = 0;
							Integer fvarcount = 1;
							// for(int j = 0; j<=3; j++){
							try {
								for (UnitOfMeasurement uom : uomList) {
									String str = "";
									str = uom.getUnit().replaceAll(" ", "");
									System.out.println(str);
									if (str.equalsIgnoreCase(row.getCell(2).getStringCellValue().replaceAll(" ", ""))) {
										product.setUnit(uom.getUom_id());
										product.setUom(uom);
										fvar = 1;
										break;
									} else {
										fvar = 2;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							
							if(row.getCell(3).getStringCellValue().equalsIgnoreCase("GST"))
							{
							try {							
								if(product.getHsn_san_no()==null)
								{
									try
									{
										Double hsncode= row.getCell(4).getNumericCellValue();
										Integer hsncode1 = hsncode.intValue();
										String hsn2=hsncode1.toString().trim();
										GstTaxMaster gst=gstService.getHSNbyDate(new LocalDate(), hsn2);
											//hsn1;
											if (gst!=null) {
												// product.setTax_id(gst.getTax_id());
												product.setGst_id(gst);
												product.setHsn_san_no(hsn2);
												product.setTax_type((long) 1);
												fvar = 1;
												
											} 
											else {
												fvar = 2;
											}
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
									try
									{
									String hsn2=row.getCell(4).getStringCellValue(); 
									GstTaxMaster gst =gstService.getHSNbyDate(new LocalDate(), hsn2);
											//hsn1;
											if (gst!=null) {
												// product.setTax_id(gst.getTax_id());
												product.setGst_id(gst);
												product.setHsn_san_no(hsn2);
												product.setTax_type((long) 1);
												fvar = 1;
												
											} 
											else {
												fvar = 2;
											}
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
									
								}
								else {
									fvar = 2;
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						   }
							else if(row.getCell(3).getStringCellValue().equalsIgnoreCase("VAT"))
							{
								TaxMaster tax = taxMasterService.isExists(row.getCell(5).getStringCellValue().trim());
								if(tax!=null)
								{
										// product.setTax_id(gst.getTax_id());
									    product.setTaxMaster(tax);
										product.setTax_type((long) 2);
										fvar = 1;
								}

								else {
									fvar = 2;
								}
							}
							else {
								fvar = 2;
							}
							
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							
							if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
								try {
									for (Company comp : companylist) {
										String str = "";
										str = comp.getCompany_name().replaceAll(" ", "");
										if (str.equalsIgnoreCase(
												row.getCell(6).getStringCellValue().replaceAll(" ", ""))) {
											product.setCompany(comp);
											product.setCompany_id(comp.getCompany_id());

											fvar = 1;
											break;
										} else {
											fvar = 2;
										}

									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								product.setCompany(user.getCompany());
								product.setCompany_id(company_id);
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							product.setProduct_name(row.getCell(0).getStringCellValue());
							String pname = product.getProduct_name().replace("\"", "").replace("\'", "");
							product.setProduct_name(pname);
							String goods = "Goods";
							String service = "Services";

							if (goods.equalsIgnoreCase(row.getCell(1).getStringCellValue().replaceAll(" ", ""))) {
								product.setType(1);
								fvar = 1;
							} else if (service
									.equalsIgnoreCase(row.getCell(1).getStringCellValue().replaceAll(" ", ""))) {
								product.setType(2);
								fvar = 1;
							} else {
								product.setType(0);
								fvar = 2;
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							System.out.println(product.getType());
							product.setProduct_approval(0);
							product.setCreated_by(user);
							product.setStatus(true);
							// }

							if (fvar != 0) {
								if (fvarcount == 1)
									product.setFlag(true);
								else if (fvarcount == 0)
									product.setFlag(false);
								int exist = productService.isExistsProduct(product.getProduct_name(),
										product.getCompany_id());
								if (exist == 1) {
									Product pr = productService.isExists(product.getProduct_name(),
											product.getCompany_id());
									pr.setUpdated_by(user);
									product.setProduct_id(pr.getProduct_id());
									productService.updateExcel(product);
									if (fvarcount == 1) {
										m = 1;
										product.setFlag(true);
										updateList.add(product.getProduct_name());
										updatecount = updatecount + 1;
									} else if (fvarcount == 0) {
										m = m + 2;
										product.setFlag(false);
										failureList.add(product.getProduct_name());
										failcount = failcount + 1;
									}

								} else {
									if (fvarcount == 1)
										product.setFlag(true);
									else if (fvarcount == 0)
										product.setFlag(false);
									
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									product.setIp_address(remoteAddr);
									product.setCreated_by(user);
									productService.addProductExcel(product);
									if (fvarcount == 1) {
										m = 1;
										product.setFlag(true);
										successList.add(product.getProduct_name());
										successcount = successcount + 1;

									} else if (fvarcount == 0) {
										m = m + 2;
										product.setFlag(false);
										failureList.add(product.getProduct_name());
										failcount = failcount + 1;
									}
								}
							}
							
							
						}
					}
					workbook.close();
				} else {
					System.out.println("no data");
				}

			} else if (excelfile.getOriginalFilename().endsWith("xlsx")) {
				int i = 0;
				int rowcount=0;
				
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
				
			while (i <= worksheet.getLastRowNum()) {
				int colcount=0;
					XSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 3; j++) {
						if (row.getCell(j) != null) {							
						colcount++;
						}						
					}
					if(colcount>=4)
					{
					rowcount++;
					}
				}

				if (isValid) {
					i = 1;
					while (i <= rowcount) {
						Product product = new Product();
						XSSFRow row = worksheet.getRow(i++);
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							
							Integer fvar = 0;
							Integer fvarcount = 1;
							// for(int j = 0; j<=3; j++){
							try {
								for (UnitOfMeasurement uom : uomList) {
									String str = "";
									str = uom.getUnit().replaceAll(" ", "");
									System.out.println(str);
									if (str.equalsIgnoreCase(row.getCell(2).getStringCellValue().replaceAll(" ", ""))) {
										product.setUnit(uom.getUom_id());
										product.setUom(uom);
										fvar = 1;
										break;
									} else {
										fvar = 2;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							
							if(row.getCell(3).getStringCellValue().equalsIgnoreCase("GST"))
							{
							try {							
								if(product.getHsn_san_no()==null)
								{
									try
									{
										Double hsncode= row.getCell(4).getNumericCellValue();
										Integer hsncode1 = hsncode.intValue();
										String hsn2=hsncode1.toString().trim();
										GstTaxMaster gst=gstService.getHSNbyDate(new LocalDate(), hsn2);
											//hsn1;
											if (gst!=null) {
												// product.setTax_id(gst.getTax_id());
												product.setGst_id(gst);
												product.setHsn_san_no(hsn2);
												product.setTax_type((long) 1);
												fvar = 1;
												
											} 
											else {
												fvar = 2;
											}
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
									try
									{
									String hsn2=row.getCell(4).getStringCellValue(); 
									GstTaxMaster gst =gstService.getHSNbyDate(new LocalDate(), hsn2);
											//hsn1;
											if (gst!=null) {
												// product.setTax_id(gst.getTax_id());
												product.setGst_id(gst);
												product.setHsn_san_no(hsn2);
												product.setTax_type((long) 1);
												fvar = 1;
												
											} 
											else {
												fvar = 2;
											}
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
									
								}
								else {
									fvar = 2;
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						   }
							else if(row.getCell(3).getStringCellValue().equalsIgnoreCase("VAT"))
							{
								TaxMaster tax = taxMasterService.isExists(row.getCell(5).getStringCellValue().trim());
								if(tax!=null)
								{
										// product.setTax_id(gst.getTax_id());
									    product.setTaxMaster(tax);
										product.setTax_type((long) 2);
										fvar = 1;
								}

								else {
									fvar = 2;
								}
							}
							else {
								fvar = 2;
							}
							
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							
							if ((role == ROLE_SUPERUSER) || (role == ROLE_EXECUTIVE) || (role == ROLE_MANAGER)) {
								try {
									for (Company comp : companylist) {
										String str = "";
										str = comp.getCompany_name().replaceAll(" ", "");
										if (str.equalsIgnoreCase(
												row.getCell(6).getStringCellValue().replaceAll(" ", ""))) {
											product.setCompany(comp);
											product.setCompany_id(comp.getCompany_id());

											fvar = 1;
											break;
										} else {
											fvar = 2;
										}

									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								product.setCompany(user.getCompany());
								product.setCompany_id(company_id);
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							product.setProduct_name(row.getCell(0).getStringCellValue());
							String pname = product.getProduct_name().replace("\"", "").replace("\'", "");
							product.setProduct_name(pname);
							String goods = "Goods";
							String service = "Services";

							if (goods.equalsIgnoreCase(row.getCell(1).getStringCellValue().replaceAll(" ", ""))) {
								product.setType(1);
								fvar = 1;
							} else if (service
									.equalsIgnoreCase(row.getCell(1).getStringCellValue().replaceAll(" ", ""))) {
								product.setType(2);
								fvar = 1;
							} else {
								product.setType(0);
								fvar = 2;
							}
							if ((fvar == 1) && (fvarcount == 1))
								fvarcount = 1;
							else
								fvarcount = 0;
							System.out.println(product.getType());
							product.setProduct_approval(0);
							product.setCreated_by(user);
							product.setStatus(true);
							// }

							if (fvar != 0) {
								if (fvarcount == 1)
									product.setFlag(true);
								else if (fvarcount == 0)
									product.setFlag(false);
								int exist = productService.isExistsProduct(product.getProduct_name(),
										product.getCompany_id());
								if (exist == 1) {
									Product pr = productService.isExists(product.getProduct_name(),
											product.getCompany_id());
									pr.setUpdated_by(user);
									product.setProduct_id(pr.getProduct_id());
									productService.updateExcel(product);
									if (fvarcount == 1) {
										m = 1;
										product.setFlag(true);
										updateList.add(product.getProduct_name());
										updatecount = updatecount + 1;
									} else if (fvarcount == 0) {
										m = m + 2;
										product.setFlag(false);
										failureList.add(product.getProduct_name());
										failcount = failcount + 1;
									}

								} else {
									if (fvarcount == 1)
										product.setFlag(true);
									else if (fvarcount == 0)
										product.setFlag(false);
									
									String remoteAddr = "";
									remoteAddr = request.getHeader("X-FORWARDED-FOR");
									if (remoteAddr == null || "".equals(remoteAddr)) {
										remoteAddr = request.getRemoteAddr();
									}
									product.setIp_address(remoteAddr);
									product.setCreated_by(user);
									productService.addProductExcel(product);
									if (fvarcount == 1) {
										m = 1;
										product.setFlag(true);
										successList.add(product.getProduct_name());
										successcount = successcount + 1;

									} else if (fvarcount == 0) {
										m = m + 2;
										product.setFlag(false);
										failureList.add(product.getProduct_name());
										failcount = failcount + 1;
									}
								}
							}
							
							
						}
					}
					workbook.close();
				} else {
					System.out.println("no data");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isValid = false;
		}
		if (successcount != 0) {
			successmsg += "<h5 class='green-lable'>" + successcount + " " + success + "</h5>";
		}
		if (updatecount != 0) {
			updatemsg += "<h5 class='orange-lable'>" + updatecount + " " + update + "</h5>";
		}
		if (failcount != 0) {
			failmsg += "<h5 class='red-lable'>" + failcount + " " + fail + "</h5>";
		}
		if ((successcount == 0) && (updatecount == 0) && (failcount == 0)) {
			error = "0 Record Imported, Please check file format";
		}
		model.setViewName("redirect:/productList");
		return model;
	}

	@RequestMapping(value = "Batchproduct", method = RequestMethod.POST)
	public ModelAndView productBatchApproval(@ModelAttribute("batchproduct") Product product,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User)session.getAttribute("user");	
		Boolean flag = true;
		Boolean primaryApproval = null;
		Boolean rejectApproval = null;

		if (product.getPrimaryApproval() != null) {
			primaryApproval = product.getPrimaryApproval();
		}
		if (product.getRejectApproval() != null) {
			rejectApproval = product.getRejectApproval();
		}

		if (rejectApproval != null) {
			flag = productService.rejectByBatch(product.getProductList());
			for( String proId : product.getProductList())
			{
				yearService.saveActivityLogForm(user.getUser_id(), null, null, Long.parseLong(proId), null, null, null, null, null, null, (long)APPROVAL_STATUS_REJECT);
			}
			if (flag) {
				session.setAttribute("successMsg", "Product Rejected Successfully");
			} else {
				session.setAttribute("errorMsg", "Please select atleast one Product.");
			}
			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && rejectApproval) {
				model.setViewName("redirect:/productPrimaryValidationList");
			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !rejectApproval) {
				model.setViewName("redirect:/productSecondaryValidationList");
			}
		}

		if (primaryApproval != null) {
			if ((role == ROLE_EXECUTIVE || role == ROLE_SUPERUSER) && primaryApproval) {
				flag = productService.approvedByBatch(product.getProductList(), primaryApproval);
				for( String proId : product.getProductList())
				{
					yearService.saveActivityLogForm(user.getUser_id(), null, null, Long.parseLong(proId), null, null, null, null, (long)APPROVAL_STATUS_PRIMARY, null, null);
				}
				if (flag == true) {
					session.setAttribute("successMsg", "Product Primary Approval Done Successfully");
				} else {
					session.setAttribute("errorMsg", "Please select atleast one Product.");
				}
				model.setViewName("redirect:/productPrimaryValidationList");

			} else if ((role == ROLE_MANAGER || role == ROLE_SUPERUSER) && !primaryApproval) {
				flag = productService.approvedByBatch(product.getProductList(), primaryApproval);
				for( String proId : product.getProductList())
				{
					yearService.saveActivityLogForm(user.getUser_id(), null, null, Long.parseLong(proId), null, null, null, null, null, (long)APPROVAL_STATUS_SECONDARY, null);
				}
				if (flag == true) {
					session.setAttribute("successMsg", "Product Secondary Approval Done Successfully");
				} else {
					session.setAttribute("errorMsg", "Please select atleast one Product.");
				}
				model.setViewName("redirect:/productSecondaryValidationList");
			}
		}
		return model;
	}

}