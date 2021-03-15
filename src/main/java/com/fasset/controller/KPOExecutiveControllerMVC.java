/**
 * mayur suramwar
 */
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
import com.fasset.controller.validators.KPOExecutiveValidator;
import com.fasset.entities.City;
import com.fasset.entities.Country;
import com.fasset.entities.Role;
import com.fasset.entities.State;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ICityService;
import com.fasset.service.interfaces.ICountryService;
import com.fasset.service.interfaces.IKPOExecutiveService;
import com.fasset.service.interfaces.IRoleService;
import com.fasset.service.interfaces.IStateService;
import com.fasset.service.interfaces.IUserService;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class KPOExecutiveControllerMVC {

	@Autowired
	private ICityService cityService;

	@Autowired
	private ICountryService countryService;

	@Autowired
	private IStateService stateService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private KPOExecutiveValidator validator;

	@Autowired
	private IUserService userService;

	@Autowired
	private IKPOExecutiveService KPOExecutiveService;

	List<Role> roleforsuper = new ArrayList<Role>();

	@RequestMapping(value = "KPOExecutive", method = RequestMethod.GET)
	public ModelAndView KPOExecutive(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		long role_id = (long) session.getAttribute("role");

		List<City> cityList = cityService.findAll();
		List<Country> countryList = countryService.findAll();
		List<State> stateList = stateService.findAll();
		List<Role> role = roleService.list();		
		List<User> managerList = userService.getUserByRole(MyAbstractController.ROLE_MANAGER);
		List<User> adminList = userService.getUserByRole(MyAbstractController.ROLE_SUPERUSER);
		List<Role> roleforsuper = new ArrayList<Role>();
		if (role_id == MyAbstractController.ROLE_SUPERUSER) {
			for (Role newrole : role) {

				switch (newrole.getRole_name()) {
				case "Executive":
					roleforsuper.add(newrole);
					break;

				case "Manager":
					roleforsuper.add(newrole);
					break;
				}
			}
		}

		else {
			for (Role newrole : role) {

				switch (newrole.getRole_name()) {
				case "Executive":
					roleforsuper.add(newrole);
					break;
				}
			}
		}
		model.addObject("cityList", cityList);
		model.addObject("countryList", countryList);
		model.addObject("stateList", stateList);
		model.addObject("roleforsuper", roleforsuper);
		model.addObject("userform", user);
		model.addObject("managerList", managerList);
		model.addObject("adminList", adminList);
		model.setViewName("/master/kpoExecutive");
		return model;
	}

	@RequestMapping(value = "saveKPOExecutive", method = RequestMethod.POST)
	public ModelAndView saveKPOExecutive(@ModelAttribute("userform") User user, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		long role_id = (long) session.getAttribute("role");
		long company_id = (long) session.getAttribute("company_id");
		Long user_id = (long) session.getAttribute("user_id");
		long id = 0;
		ModelAndView model = new ModelAndView();
		validator.validate(user, result);
		if (result.hasErrors()) {
			List<City> cityList = cityService.findAll();
			List<Country> countryList = countryService.findAll();
			List<State> stateList = stateService.findAll();
			List<Role> role = roleService.list();
			List<Role> roleforsuper = new ArrayList<Role>();	
			List<User> managerList = userService.getUserByRole(MyAbstractController.ROLE_MANAGER);
			List<User> adminList = userService.getUserByRole(MyAbstractController.ROLE_SUPERUSER);
			if (role_id == MyAbstractController.ROLE_SUPERUSER) {
				for (Role newrole : role) {

					switch (newrole.getRole_name()) {
					case "Executive":
						roleforsuper.add(newrole);
						break;

					case "Manager":
						roleforsuper.add(newrole);
						break;
					}
				}
			} else {
				for (Role newrole : role) {

					switch (newrole.getRole_name()) {
					case "Executive":
						roleforsuper.add(newrole);
						break;
						
					case "Manager":
						if(user_id == id) {
							roleforsuper.add(newrole);
							break;	
						}
						
					}
				}
			}

			model.addObject("cityList", cityList);
			model.addObject("countryList", countryList);
			model.addObject("stateList", stateList);
			model.addObject("roleforsuper", roleforsuper);
			model.addObject("managerList", managerList);
			model.addObject("adminList", adminList);
			model.setViewName("/master/kpoExecutive");
			return model;
		} else {
			String msg = "";
			try {
				if (user.getUser_id() != null) {
					id = user.getUser_id();
					if (id > 0) {
						user.setUpdated_by(Integer.parseInt(user_id.toString()));
						KPOExecutiveService.update(user);
						msg = "KPO executive/manager updated successfully";
					}
				}

				else {
					user.setCompany_id(company_id);
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            user.setIp_address(remoteAddr);
					user.setCreated_by(Integer.parseInt(user_id.toString()));
					msg = KPOExecutiveService.saveUser(user);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(user_id == id) {
				session.setAttribute("successMsg", "Your profile updated successfully");
				model.setViewName("redirect:/homePage");
			}
			else {
				List<User> userList = userService.list();
				List<User> kpoList = new ArrayList<User>();
				if (role_id == MyAbstractController.ROLE_SUPERUSER) {
					for (User userkpo : userList) {
						if (userkpo.getRole().getRole_id() != null) {
							switch ((userkpo.getRole().getRole_id()).intValue()) {
							case 3:
								kpoList.add(userkpo);
								break;

							case 4:
								kpoList.add(userkpo);
								break;
							}
						}

					}
				} else {
					for (User userkpo : userList) {
						if (userkpo.getRole().getRole_id() != null) {
							switch ((userkpo.getRole().getRole_id()).intValue()) {
							case 3:
								kpoList.add(userkpo);
								break;
							}
						}

					}
				}
				model.addObject("kpoList", kpoList);
				model.addObject("successMsg", msg);			
				model.setViewName("/master/KPOExecutiveList");
			}
			return model;
		}
	}

	@RequestMapping(value = "KPOExecutiveList", method = RequestMethod.GET)
	public ModelAndView KPOExecutiveList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		HttpSession session = request.getSession(true);
		long role_id = (long) session.getAttribute("role");

		List<User> userList = userService.list();
		List<User> kpoList = new ArrayList<User>();

		if (role_id == MyAbstractController.ROLE_SUPERUSER) {
			for (User userkpo : userList) {
				if (userkpo.getRole().getRole_id() != null) {
					switch ((userkpo.getRole().getRole_id()).intValue()) {
					case 3:
						kpoList.add(userkpo);
						break;

					case 4:
						kpoList.add(userkpo);
						break;
					}
				}

			}
		} else {
			for (User userkpo : userList) {
				if (userkpo.getRole().getRole_id() != null) {
					switch ((userkpo.getRole().getRole_id()).intValue()) {
					case 3:
						kpoList.add(userkpo);
						break;
					}
				}

			}
		}
		model.addObject("kpoList", kpoList);
		model.setViewName("/master/KPOExecutiveList");
		return model;
	}

	@RequestMapping(value = "editKPOExecutive", method = RequestMethod.GET)
	public ModelAndView editKPOExecutive(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		long role_id = (long) session.getAttribute("role");
		Long user_id = (long) session.getAttribute("user_id");

		try {
			if (id > 0) {
				user = userService.getById(id);
				user.setCity_id(user.getCity().getCity_id());
				user.setCountry_id(user.getCountry().getCountry_id());
				user.setState_id(user.getState().getState_id());
				user.setRole_id(user.getRole().getRole_id());
				user.setManager(user.getManager_id().getUser_id());
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}

		List<City> cityList = cityService.findAll();
		List<Country> countryList = countryService.findAll();
		List<State> stateList = stateService.findAll();
		List<Role> role = roleService.list();
		List<Role> roleforsuper = new ArrayList<Role>();	
		List<User> managerList = userService.getUserByRole(MyAbstractController.ROLE_MANAGER);
		List<User> adminList = userService.getUserByRole(MyAbstractController.ROLE_SUPERUSER);
		if (role_id == 2) {
			for (Role newrole : role) {

				switch (newrole.getRole_name()) {
				case "Executive":
					roleforsuper.add(newrole);
					break;

				case "Manager":
					roleforsuper.add(newrole);
					break;
				}
			}
		} else {
			for (Role newrole : role) {

				switch (newrole.getRole_name()) {
				case "Executive":
					roleforsuper.add(newrole);
					break;
					
				case "Manager":
					if(user_id == id) {
						roleforsuper.add(newrole);
						break;	
					}
				}
			}
		}
		model.addObject("cityList", cityList);
		model.addObject("countryList", countryList);
		model.addObject("stateList", stateList);
		model.addObject("roleforsuper", roleforsuper);
		model.addObject("userform", user);
		model.addObject("managerList", managerList);
		model.addObject("adminList", adminList);
		model.setViewName("/master/kpoExecutive");
		return model;
	}

	@RequestMapping(value = "viewKPOExecutive", method = RequestMethod.GET)
	public ModelAndView viewKPOExecutive(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		try {
			user = userService.getById(id);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.addObject("user1", user);
		model.addObject("view", "view");
		model.setViewName("/master/KPOExecutiveView");
		return model;
	}

	@RequestMapping(value = "deleteKPOExecutive", method = RequestMethod.GET)
	public ModelAndView deleteKPOExecutive(@RequestParam("id") long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		HttpSession session = request.getSession(true);
		long role_id = (long) session.getAttribute("role");

		List<User> userList = userService.list();
		List<User> kpoList = new ArrayList<User>();

		if (role_id == 2) {
			for (User userkpo : userList) {
				if (userkpo.getRole().getRole_id() != null) {
					switch ((userkpo.getRole().getRole_id()).intValue()) {
					case 3:
						kpoList.add(userkpo);
						break;

					case 4:
						kpoList.add(userkpo);
						break;
					}
				}

			}
		} else {
			for (User userkpo : userList) {
				if (userkpo.getRole().getRole_id() != null) {
					switch ((userkpo.getRole().getRole_id()).intValue()) {
					case 3:
						kpoList.add(userkpo);
						break;
					}
				}

			}
		}
		model.addObject("kpoList", kpoList);
		model.setViewName("/master/KPOExecutiveList");
		return model;
	}
}
