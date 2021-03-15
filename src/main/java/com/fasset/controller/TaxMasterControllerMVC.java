/**
 * mayur suramwar
 */
package com.fasset.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.validators.TaxMasterValidator;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountGroupType;
import com.fasset.entities.LedgerPostingSide;
import com.fasset.entities.TaxMaster;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ITaxMasterService;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Controller
@SessionAttributes("user")
public class TaxMasterControllerMVC {

	@Autowired
	private TaxMasterValidator validator;

	@Autowired
	private ITaxMasterService iTaxMasterService;

	@RequestMapping(value = "taxmaster", method = RequestMethod.GET)
	public ModelAndView taxmaster() {
		ModelAndView model = new ModelAndView();
		TaxMaster taxMaster = new TaxMaster();

		model.addObject("taxMaster", taxMaster);
		model.setViewName("/master/taxmaster");
		return model;
	}

	@RequestMapping(value = "savetaxMaster", method = RequestMethod.POST)
	public ModelAndView savetaxMaster(@ModelAttribute("taxMaster") TaxMaster taxMaster, BindingResult result,HttpServletRequest request, HttpServletResponse response) {

		ModelAndView model = new ModelAndView();
		User user = new User();
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("user");
		validator.validate(taxMaster, result);
		if (result.hasErrors()) {
		
			model.setViewName("/master/taxmaster");
			return model;
		} else {
			String msg = "";
			try {
				if (taxMaster.getTax_id() != null) {
					long id = taxMaster.getTax_id();
					if (id > 0) {
						taxMaster.setUpdated_by(user);
						iTaxMasterService.update(taxMaster);
						msg = "Tax updated successfully";
					}
				}
				else {
					taxMaster.setCreated_by(user);
					
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            taxMaster.setIp_address(remoteAddr);
					msg = iTaxMasterService.saveTaxMaster(taxMaster);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/taxMasterList");
		}
	}

	@RequestMapping(value = "taxMasterList", method = RequestMethod.GET)
	public ModelAndView taxMasterList(@RequestParam(value = "msg", required = false)String msg,HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addObject("taxMasterList", iTaxMasterService.findAllListing());
		model.setViewName("/master/taxMasterList");
		return model;
	}

	@RequestMapping(value = "editTaxMaster", method = RequestMethod.GET)
	public ModelAndView editTaxMaster(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		TaxMaster taxMaster = new TaxMaster();
		try {
			if (id > 0) {
				taxMaster = iTaxMasterService.getById(id);
				taxMaster.setCst1(taxMaster.getCst().toString());
				taxMaster.setVat1(taxMaster.getVat().toString());
				taxMaster.setExcise1(taxMaster.getExcise().toString());
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}

		model.addObject("taxMaster", taxMaster);
		model.setViewName("/master/taxmaster");
		return model;
	}

	@RequestMapping(value = "viewTaxMaster", method = RequestMethod.GET)
	public ModelAndView viewtaxMaster(@RequestParam("id") long id) {
		ModelAndView model = new ModelAndView();
		TaxMaster taxMaster = new TaxMaster();
		try {
			taxMaster = iTaxMasterService.getById(id);

		} catch (MyWebException e) {
			e.printStackTrace();
		}
		model.addObject("taxMaster", taxMaster);
		model.setViewName("/master/taxMasterView");
		return model;
	}

	@RequestMapping(value = "deleteTaxMaster", method = RequestMethod.GET)
	public ModelAndView deleteTaxMaster(@RequestParam("id") long id,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg",  iTaxMasterService.deleteByIdValue(id));
		return new ModelAndView("redirect:/taxMasterList");
	}
}
