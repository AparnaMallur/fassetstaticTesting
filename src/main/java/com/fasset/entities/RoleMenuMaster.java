package com.fasset.entities;

public class RoleMenuMaster {
	
	
	private Long superUserId;
	private Long clientId;
	private Long auditorId;
	private String superUserName;
	private String clientName;
	private String auditorName;
	
	
	private Long executiveId;
	private String executiveName;
	private Long managerId;
	private String managerName;
	
	private Long employeeId;
	private String employeeName;
	
	
	
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Long getExecutiveId() {
		return executiveId;
	}
	public void setExecutiveId(Long executiveId) {
		this.executiveId = executiveId;
	}
	public String getExecutiveName() {
		return executiveName;
	}
	public void setExecutiveName(String executiveName) {
		this.executiveName = executiveName;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public Long  getSuperUserId () {
		return superUserId;
	}
	public void  setSuperUserId(Long superUserId) {
		this.superUserId = superUserId;
	}
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public Long getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}
	public String getSuperUserName() {
		return superUserName;
	}
	public void setSuperUserName(String superUserName) {
		this.superUserName = superUserName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	
	
	

}
