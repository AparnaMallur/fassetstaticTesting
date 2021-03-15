package com.fasset.form;

import java.util.List;

import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;

public class ReceiptForm {

	private Receipt receipt ;
	private List<Receipt_product_details> customerproductList ;
	private Double closingbalance ;
	
	public Receipt getReceipt() {
		return receipt;
	}
	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}
	public List<Receipt_product_details> getCustomerproductList() {
		return customerproductList;
	}
	public void setCustomerproductList(List<Receipt_product_details> customerproductList) {
		this.customerproductList = customerproductList;
	}
	public Double getClosingbalance() {
		return closingbalance;
	}
	public void setClosingbalance(Double closingbalance) {
		this.closingbalance = closingbalance;
	}
}
