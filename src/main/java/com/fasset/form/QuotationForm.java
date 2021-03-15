package com.fasset.form;

import java.util.List;

import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;

public class QuotationForm {

	private List<QuotationDetails> quoteDetails;
	private Quotation quotation;
	public List<QuotationDetails> getQuoteDetails() {
		return quoteDetails;
	}
	public void setQuoteDetails(List<QuotationDetails> quoteDetails) {
		this.quoteDetails = quoteDetails;
	}
	public Quotation getQuotation() {
		return quotation;
	}
	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}
}
