package com.fasset.reports;

public class RadioQuestions {
	String txtq, expl, numb;

	public RadioQuestions(String n, String q, String t) {
		setTxtq(q);
		setExpl(t);
		setNumb(n);
	}

	public String getTxtq() {
		return txtq;
	}

	public void setTxtq(String txtq) {
		this.txtq = txtq;
	}

	public String getExpl() {
		return expl;
	}

	public void setExpl(String expl) {
		this.expl = expl;
	}

	public String getNumb() {
		return numb;
	}

	public void setNumb(String numb) {
		this.numb = numb;
	}
}
