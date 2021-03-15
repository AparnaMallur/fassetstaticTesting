package com.fasset.form;

import java.util.Set;

import com.fasset.entities.DebitDetails;
import com.fasset.entities.DebitNote;

public class DebitNoteForm {

	private DebitNote debitNote;
	private Set<DebitDetails> debitDetails;
	public DebitNote getDebitNote() {
		return debitNote;
	}
	public void setDebitNote(DebitNote debitNote) {
		this.debitNote = debitNote;
	}
	public Set<DebitDetails> getDebitDetails() {
		return debitDetails;
	}
	public void setDebitDetails(Set<DebitDetails> debitDetails) {
		this.debitDetails = debitDetails;
	}
	
}
