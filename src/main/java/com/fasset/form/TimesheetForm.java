package com.fasset.form;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.ExecutiveTimesheet;

public class TimesheetForm {
	private LocalDate date;
	private List<ExecutiveTimesheet> details;
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<ExecutiveTimesheet> getDetails() {
		return details;
	}

	public void setDetails(List<ExecutiveTimesheet> details) {
		this.details = details;
	}
}
