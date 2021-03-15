package com.fasset.form;

import java.io.Serializable;

public class Notification  implements Serializable{
	private String header;
	private String discription;
	private String url;
	
	public Notification() {
		super();
	}

	public Notification(String header, String discription, String url) {
		super();
		this.header = header;
		this.discription = discription;
		this.url = url;
	}

	public String getHeader() {
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getDiscription() {
		return discription;
	}
	
	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
