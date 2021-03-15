package com.fasset.reports;

public class ExplQuestions {
	String page, qstn, part, expl;

	public ExplQuestions(String page, String qstn, String part, String expl) {
		setExpl(expl);
		setPart(part);
		setQstn(qstn);
		setPage(page);
	}

	public String getExpl() {
		return expl;
	}

	public void setExpl(String expl) {
		this.expl = expl;
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
}
