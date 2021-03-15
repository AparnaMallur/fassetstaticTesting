package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

/**
 * @author vijay ghodake
 *
 * deven infotech pvt ltd.
 */
@Entity
@Table(name = "executive_timesheet")
public class ExecutiveTimesheet extends AbstractEntity{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "timesheet_id", unique = true, nullable = false)
	private Long timesheet_id ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = true, updatable = true)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = true, updatable = true)
	private Company company;
	
	@Transient
	private Long company_id;
	
	@Transient
	private Long service_id;

	@Column(name = "from_date")
	private LocalDate from_date;
	
	@Column(name = "to_date")
	private LocalDate to_date;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_id", referencedColumnName = "id", insertable = true, updatable = true)
	private Service service;

	@Column(name = "ip_address", nullable = true)
	private String ip_address ;
	
	@Column(name = "total_time", nullable = true)
	private String total_time;
	
	@Column(name = "remark", nullable = true, length = MyAbstractController.SIZE_FIVE_HUNDRED)
	private String remark;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "date")
	private LocalDate date;
	
	@Transient
	private String timesheetDetails;

	public Long getTimesheet_id() {
		return timesheet_id;
	}

	public void setTimesheet_id(Long timesheet_id) {
		this.timesheet_id = timesheet_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public Long getService_id() {
		return service_id;
	}

	public void setService_id(Long service_id) {
		this.service_id = service_id;
	}

	public LocalDate getFrom_date() {
		return from_date;
	}

	public void setFrom_date(LocalDate from_date) {
		this.from_date = from_date;
	}

	public LocalDate getTo_date() {
		return to_date;
	}

	public void setTo_date(LocalDate to_date) {
		this.to_date = to_date;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getTotal_time() {
		return total_time;
	}

	public void setTotal_time(String total_time) {
		this.total_time = total_time;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTimesheetDetails() {
		return timesheetDetails;
	}

	public void setTimesheetDetails(String timesheetDetails) {
		this.timesheetDetails = timesheetDetails;
	}
	
	
}
