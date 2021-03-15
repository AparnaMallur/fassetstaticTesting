package com.fasset.ccavenue;

import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CCAvenueParams {
	@Value("${merchant_id}")
	private String merchant_id;
	
	@Value("${language}")
	private String language;
	@Value("${currency}")
	private String currency;
	
	private long order_id;	
	private long company_id;
	private String amount;	
	private URL redirect_url;	
	private URL cancel_url;
	
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(long order_id) {
		this.order_id = order_id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public URL getRedirect_url() {
		return redirect_url;
	}
	public void setRedirect_url(URL redirect_url) {
		this.redirect_url = redirect_url;
	}
	public URL getCancel_url() {
		return cancel_url;
	}
	public void setCancel_url(URL cancel_url) {
		this.cancel_url = cancel_url;
	}
	public long getCompany_id() {
		return company_id;
	}
	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}
	
}
