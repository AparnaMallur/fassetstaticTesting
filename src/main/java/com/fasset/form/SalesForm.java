package com.fasset.form;

import java.util.List;

import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;

public class SalesForm {

private	SalesEntry salesEntry;

private List<SalesEntryProductEntityClass> customerProductList ;

public SalesEntry getSalesEntry() {
	return salesEntry;
}

public void setSalesEntry(SalesEntry salesEntry) {
	this.salesEntry = salesEntry;
}

public List<SalesEntryProductEntityClass> getCustomerProductList() {
	return customerProductList;
}

public void setCustomerProductList(List<SalesEntryProductEntityClass> customerProductList) {
	this.customerProductList = customerProductList;
}
	
}
