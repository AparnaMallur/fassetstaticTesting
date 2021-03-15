package com.fasset.controller.validators;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasset.entities.GstTaxMaster;
import com.fasset.service.interfaces.IGstTaxMasterService;

@Component
public class GSTValidator extends MyValidator implements Validator{
	@Autowired
	private IGstTaxMasterService gstService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return GstTaxMaster.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		GstTaxMaster gst = (GstTaxMaster) target;
		Long id = gst.getTax_id();
		String hsnSac = gst.getHsc_sac_code();
		String igst = gst.getIgst1();
		String cgst = gst.getCgst1();
		String sgst = gst.getSgst1();
		String sct = gst.getScc();
		LocalDate startdate=gst.getStart_date();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cgst1","error.cgst1", "CGST number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hsc_sac_code","error.hsc_sac_code", "HSC/SAC number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sgst1","error.sgst1", "SGST number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "scc","error.scc", "State Compensation Tax is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "igst1","error.igst1", "IGST number is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description","error.description", "Description is required");
		if(startdate==null){
			errors.rejectValue("start_date","error.start_date", "Start Date cannot be blank");
		}
		
		if(hsnSac != null && hsnSac != ""){
			if(!hsnSac.matches("\\d{2,8}")){
				errors.rejectValue("hsc_sac_code","error.hsc_sac_code", "Enter valid HSN/SAC number");
			}
		}
		
		if(igst != null && igst != ""){
			if (!igst.matches("^([+-]?\\d*\\.?\\d*)$")) {
				errors.rejectValue("igst1","error.igst1", "Enter valid IGST");
			}
		}
		
	if(cgst != null && cgst != ""){
		if (!cgst.matches("^([+-]?\\d*\\.?\\d*)$")) {
				errors.rejectValue("cgst1","error.cgst1", "Enter valid CGST");
			}
		}
		
		if(sgst != null && sgst != ""){
			if (!sgst.matches("^([+-]?\\d*\\.?\\d*)$")) {
				errors.rejectValue("sgst1","error.sgst1", "Enter valid SGST");
			}
		}
		
		if(gst.getChapter_id()!=null && gst.getChapter_id()==0 ){
			errors.rejectValue("chapter_id","error.chapter_id", "Select chapter");
		}
		if(gst.getSchedule_id()!=null && gst.getSchedule_id()==0 ){
			errors.rejectValue("schedule_id","error.schedule_id", "Select schedule");
		}
		
	}

	
}
