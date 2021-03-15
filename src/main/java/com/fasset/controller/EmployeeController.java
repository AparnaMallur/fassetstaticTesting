package com.fasset.controller;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.fasset.controller.abstracts.MyAbstractController;

import com.fasset.controller.validators.EmployeeValidator;
import com.fasset.entities.Company;
import com.fasset.entities.Employee;
import com.fasset.entities.User;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IEmployeeService;


@Controller
@SessionAttributes("user")
public class EmployeeController {
	
	

	@Autowired
	IEmployeeService employeeservice;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private EmployeeValidator employeeValidator;
	
	
	@RequestMapping(value="employeeform",method=RequestMethod.GET)
	public ModelAndView getEmployeeForm(HttpServletRequest request, HttpServletResponse response) 
	{
		ModelAndView model= new ModelAndView();
		Employee employee= new Employee(); 
		/*
		 * HttpSession session = request.getSession(true);
		 */
		model.addObject("employee", employee);
		model.setViewName("/master/employeeMaster");
		
		return model;
	}
	
	@RequestMapping(value="saveemployee",method=RequestMethod.POST)
	public ModelAndView employeeFormProcessForSaving(@ModelAttribute("employee")Employee employee, BindingResult result,HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView model= new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id = (long) session.getAttribute("company_id");
		
		Company company=companyService.getCompanyById(company_id);
		employee.setCompany(company);
		System.out.println("The emp doj is "+employee.getDoj());
		employeeValidator.validate(employee, result);
		if(result.hasErrors()) {
			model.setViewName("/master/employeeMaster");
			return model;
		}
		
		String msg = "";
		try {
			
			if(employee.getEmployee_id()!=null)
			{
				if(employee.getEmployee_id()>0) 
				{
					employeeservice.update(employee);
					msg="Employee Updated Successfully";
			    }
			}
			else 
			{	
				employeeservice.saveEmployee(employee);
				msg="Employee Saved Successfully";
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		session.setAttribute("successMsg", msg);
		/* model.addObject("successMsg", msg); */
		model.setViewName("redirect:/allemployee");
		
		return model;
	}

	@RequestMapping(value="deleteemployee", method=RequestMethod.GET)
	public ModelAndView deleteEmployee(@RequestParam("id")Long employee_ID, HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession(true);
		ModelAndView model = new ModelAndView();
System.out.println("deleteing");
		String msg="";
		
		msg=employeeservice.deleteByIdValue(employee_ID);
			
		session.setAttribute("successMsg", msg);
		
		model.setViewName("redirect:/allemployee");
		return model;
	}
	
	@RequestMapping(value = "editemployee", method = RequestMethod.GET)
	public ModelAndView editemployee(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		
		
		ModelAndView model = new ModelAndView();
		Employee employee = employeeservice.getById(id);

		
		model.addObject("employee",employee);
					
		model.setViewName("master/employeeMaster");
		return model;
	}
	
	@RequestMapping(value = "viewemployee", method = RequestMethod.GET)
	public ModelAndView viewEmployee(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		
		Employee employee = employeeservice.getById(id);
		model.addObject("employee",employee);
		model.setViewName("master/employeeMasterView");
		return model;
	}
	
	@RequestMapping(value="allemployee", method=RequestMethod.GET)
	public ModelAndView getAllEmployee(HttpServletRequest request,
			HttpServletResponse response)
	{
		List<Employee> employeeList = new ArrayList<Employee>();
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user= (User)session.getAttribute("user");
		Long role = (Long) session.getAttribute("role");
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
	/*	if (role.equals(com.fasset.controller.abstracts.MyAbstractController.ROLE_SUPERUSER) ) {
			employeeList = employeeservice.findAll();
		
		
		}
		else {*/
			employeeList = employeeservice.findAllEmployeeReletedToCompany(user.getCompany().getCompany_id());	
		
		
		model.addObject("employeeList", employeeList);
		model.setViewName("master/employeeMasterList");
		
		return model;
		
	}

}