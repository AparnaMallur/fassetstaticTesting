package com.fasset.form;

import java.util.List;

import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.PurchaseEntryProductEntityClass;

public class PurchaseForm {

	private PurchaseEntry purchaseEntry ;
	
	private List<PurchaseEntryProductEntityClass> suppilerproductList;

	public PurchaseEntry getPurchaseEntry() {
		return purchaseEntry;
	}

	public void setPurchaseEntry(PurchaseEntry purchaseEntry) {
		this.purchaseEntry = purchaseEntry;
	}

	public List<PurchaseEntryProductEntityClass> getSuppilerproductList() {
		return suppilerproductList;
	}

	public void setSuppilerproductList(List<PurchaseEntryProductEntityClass> suppilerproductList) {
		this.suppilerproductList = suppilerproductList;
	}
}
