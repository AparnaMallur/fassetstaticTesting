package com.fasset.dao.interfaces.generic;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.ExecutiveTimesheet;

/**
 * @author vijay ghodake
 *
 * deven infotech pvt ltd.
 */

public interface IExecutiveTimesheetDAO extends IGenericDao<ExecutiveTimesheet>{
	
	Long saveExecutiveTimesheetdao(ExecutiveTimesheet executiveTimesheet);
	String deleteByIdValue(Long entityId);
	List<ExecutiveTimesheet> findByUserId(Long user_id);
	List<ExecutiveTimesheet> findByDate(LocalDate date, Long user_id);
	List<ExecutiveTimesheet> findTimesheetOfExecutiveByDate(Long user_id,LocalDate fromdate,LocalDate todate);

}
