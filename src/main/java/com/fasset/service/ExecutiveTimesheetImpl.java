package com.fasset.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.generic.IExecutiveTimesheetDAO;
import com.fasset.entities.ExecutiveTimesheet;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.TimesheetForm;
import com.fasset.service.interfaces.IExecutiveTimesheetService;

/**
 * @author vijay ghodake
 *
 * deven infotech pvt ltd.
 */


@Service
@Transactional
public class ExecutiveTimesheetImpl implements IExecutiveTimesheetService{

	@Autowired
	private IExecutiveTimesheetDAO executiveDAO;
	
	@Override
	public void add(ExecutiveTimesheet entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ExecutiveTimesheet entity) throws MyWebException {
		executiveDAO.update(entity);
	}

	@Override
	public List<ExecutiveTimesheet> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecutiveTimesheet getById(Long id) throws MyWebException {
		return executiveDAO.findOne(id);
	}

	@Override
	public ExecutiveTimesheet getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(ExecutiveTimesheet entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(ExecutiveTimesheet entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String saveExecutiveTimesheet(ExecutiveTimesheet executiveTimesheet) {
		Long id= executiveDAO.saveExecutiveTimesheetdao(executiveTimesheet);
		if(id!=null){
			return " Executive timesheet saved successfully";
		}
		else{
			return "Please try again ";
		}
	}

	@Override
	public List<ExecutiveTimesheet> findAll() {
		return executiveDAO.findAll();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = executiveDAO.deleteByIdValue(entityId);
		return msg;
	}

	@Override
	public List<TimesheetForm> findByUserId(Long user_id) {
		List<TimesheetForm> timesheetFormList = new ArrayList<TimesheetForm>();
		List<ExecutiveTimesheet> executiveTimesheets = executiveDAO.findByUserId(user_id);
		for(ExecutiveTimesheet executiveTimesheet : executiveTimesheets) {
			Boolean flag = true;
			if(timesheetFormList.size() > 0) {
				for (TimesheetForm timesheetForm : timesheetFormList) {
					if(timesheetForm.getDate().compareTo(executiveTimesheet.getDate()) == 0) {
						flag = false;
						timesheetForm.getDetails().add(executiveTimesheet);
					}
				}
			}
			if(flag) {
				TimesheetForm timesheetForm = new TimesheetForm();
				timesheetForm.setDate(executiveTimesheet.getDate());
				List<ExecutiveTimesheet> detailsList= new ArrayList<ExecutiveTimesheet>();
				detailsList.add(executiveTimesheet);
				timesheetForm.setDetails(detailsList);
				timesheetFormList.add(timesheetForm);
			}
		}
		return timesheetFormList;
	}

	@Override
	public ExecutiveTimesheet isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimesheetForm findByDate(LocalDate date, Long user_id) {
		List<ExecutiveTimesheet> executiveTimesheets = executiveDAO.findByDate(date, user_id);
		List<ExecutiveTimesheet> detailList = new ArrayList<ExecutiveTimesheet>();
		TimesheetForm timesheetForm = new TimesheetForm();
		timesheetForm.setDate(date);
		if(executiveTimesheets.size() > 0 ) {
			for (ExecutiveTimesheet executiveTimesheet : executiveTimesheets) {
				detailList.add(executiveTimesheet);
			}
		}
		timesheetForm.setDetails(detailList);
		return timesheetForm;
	}

	@Override
	public String deleteByDate(LocalDate date, Long user_id) {
		List<ExecutiveTimesheet> executiveTimesheets = executiveDAO.findByDate(date, user_id);
		try {
			for (ExecutiveTimesheet executiveTimesheet : executiveTimesheets) {
				executiveDAO.deleteByIdValue(executiveTimesheet.getTimesheet_id());
			}			
		}
		catch(Exception e) {
			e.printStackTrace();
			return "Please try again";
		}
		return "Executive timesheet deleted successfully";
	}

	@Override
	public List<TimesheetForm> findAllTimesheetOfExecutive(Long user_id, LocalDate fromdate, LocalDate todate) {
		List<TimesheetForm> timesheetFormList = new ArrayList<TimesheetForm>();
		List<ExecutiveTimesheet> executiveTimesheets = executiveDAO.findTimesheetOfExecutiveByDate(user_id,fromdate,todate);
		for(ExecutiveTimesheet executiveTimesheet : executiveTimesheets) {
			Boolean flag = true;
			if(timesheetFormList.size() > 0) {
				for (TimesheetForm timesheetForm : timesheetFormList) {
					if(timesheetForm.getDate().compareTo(executiveTimesheet.getDate()) == 0) {
						flag = false;
						timesheetForm.getDetails().add(executiveTimesheet);
					}
				}
			}
			if(flag) {
				TimesheetForm timesheetForm = new TimesheetForm();
				timesheetForm.setDate(executiveTimesheet.getDate());
				List<ExecutiveTimesheet> detailsList= new ArrayList<ExecutiveTimesheet>();
				detailsList.add(executiveTimesheet);
				timesheetForm.setDetails(detailsList);
				timesheetFormList.add(timesheetForm);
			}
		}
		return timesheetFormList;
	}

}
