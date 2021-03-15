package com.fasset.reports;

import java.util.ArrayList;

public class DataReport {
	
	private String npag, part, nqst, name, path;
	private ArrayList<Object> data;

	public DataReport() {
		data = new ArrayList<>();
	}

	public String getNpag() {
		return npag;
	}

	public void setNpag(String npag) {
		this.npag = npag;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getNqst() {
		return nqst;
	}

	public void setNqst(String nqst) {
		this.nqst = nqst;
	}

	public ArrayList<Object> getData() {
		return data;
	}

	public void setData(ArrayList<Object> data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
