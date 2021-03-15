package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.ExecutiveTimesheet;
import com.fasset.form.TimesheetForm;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author vijay ghodake
 *
 * deven infotech pvt ltd.
 */

public interface IExecutiveTimesheetService extends IGenericService<ExecutiveTimesheet>{
	
	String saveExecutiveTimesheet(ExecutiveTimesheet executiveTimesheet);
	List<ExecutiveTimesheet> findAll();
	String deleteByIdValue(Long entityId);
	String deleteByDate(LocalDate date, Long user_id);
	List<TimesheetForm> findByUserId(Long user_id);
	TimesheetForm findByDate(LocalDate date, Long user_id);
	List<TimesheetForm> findAllTimesheetOfExecutive(Long user_id,LocalDate fromdate,LocalDate todate);
}
