package com.fasset.form;

import java.util.Set;

import com.fasset.entities.CreditDetails;
import com.fasset.entities.CreditNote;

public class CreditNoteForm {
	
	private CreditNote creditNote;
	private Set<CreditDetails> creditDetails;
	public CreditNote getCreditNote() {
		return creditNote;
	}
	public void setCreditNote(CreditNote creditNote) {
		this.creditNote = creditNote;
	}
	public Set<CreditDetails> getCreditDetails() {
		return creditDetails;
	}
	public void setCreditDetails(Set<CreditDetails> creditDetails) {
		this.creditDetails = creditDetails;
	}
	

}
