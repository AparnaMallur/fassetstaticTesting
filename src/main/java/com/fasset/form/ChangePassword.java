package com.fasset.form;

public class ChangePassword {
	private String userName;
	private String oldPass;
	private String pass;
	private String confPass;	
	private Long company_id;
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getConfPass() {
		return confPass;
	}
	public void setConfPass(String confPass) {
		this.confPass = confPass;
	}
	public String getOldPass() {
		return oldPass;
	}
	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

}
