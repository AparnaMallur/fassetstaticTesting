package com.fasset.reports;

public class CriminalData {
	String page, qstn, part;
	
	String crmn, sntc, date_conv;

	public CriminalData(String page, String qstn, String part, String crmn, String sntc, String date_conv) {
		this.page = page;
		this.qstn = qstn;
		this.part = part;
		this.crmn = crmn;
		this.sntc = sntc;
		this.date_conv = date_conv;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getQstn() {
		return qstn;
	}

	public void setQstn(String qstn) {
		this.qstn = qstn;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getCrmn() {
		return crmn;
	}

	public void setCrmn(String crmn) {
		this.crmn = crmn;
	}

	public String getSntc() {
		return sntc;
	}

	public void setSntc(String sntc) {
		this.sntc = sntc;
	}

	public String getDate_conv() {
		return date_conv;
	}

	public void setDate_conv(String date_conv) {
		this.date_conv = date_conv;
	}
	
	
	
}
