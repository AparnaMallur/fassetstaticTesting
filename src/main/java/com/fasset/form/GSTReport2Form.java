package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import com.fasset.entities.DebitNote;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;

public class GSTReport2Form {

	private Long companyId;
	private Integer month;
	private Long yearId;
	
	private List<PurchaseEntry> getB2BList = new ArrayList<>();
	private List<PurchaseEntry> getB2CLList = new ArrayList<>();
	private List<PurchaseEntry> getB2CSList = new ArrayList<>();
	private List<PurchaseEntry> getExpList = new ArrayList<>();
	private List<DebitNote> cdnrList = new ArrayList<>();
	private List<DebitNote> cdnurList =  new ArrayList<>();
	private List<Payment> getATList =  new ArrayList<>();
	private List<Payment> getATAdjList =  new ArrayList<>();
	private List<HSNReportForm> hsnReportList = new ArrayList<HSNReportForm>();
	private List<ExempListForm> exempList = new ArrayList<ExempListForm>();
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Long getYearId() {
		return yearId;
	}
	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}
	public List<PurchaseEntry> getGetB2BList() {
		return getB2BList;
	}
	public void setGetB2BList(List<PurchaseEntry> getB2BList) {
		this.getB2BList = getB2BList;
	}
	public List<PurchaseEntry> getGetB2CLList() {
		return getB2CLList;
	}
	public void setGetB2CLList(List<PurchaseEntry> getB2CLList) {
		this.getB2CLList = getB2CLList;
	}
	public List<PurchaseEntry> getGetB2CSList() {
		return getB2CSList;
	}
	public void setGetB2CSList(List<PurchaseEntry> getB2CSList) {
		this.getB2CSList = getB2CSList;
	}
	public List<PurchaseEntry> getGetExpList() {
		return getExpList;
	}
	public void setGetExpList(List<PurchaseEntry> getExpList) {
		this.getExpList = getExpList;
	}
	public List<DebitNote> getCdnrList() {
		return cdnrList;
	}
	public void setCdnrList(List<DebitNote> cdnrList) {
		this.cdnrList = cdnrList;
	}
	public List<DebitNote> getCdnurList() {
		return cdnurList;
	}
	public void setCdnurList(List<DebitNote> cdnurList) {
		this.cdnurList = cdnurList;
	}
	public List<Payment> getGetATList() {
		return getATList;
	}
	public void setGetATList(List<Payment> getATList) {
		this.getATList = getATList;
	}
	public List<Payment> getGetATAdjList() {
		return getATAdjList;
	}
	public void setGetATAdjList(List<Payment> getATAdjList) {
		this.getATAdjList = getATAdjList;
	}
	public List<HSNReportForm> getHsnReportList() {
		return hsnReportList;
	}
	public void setHsnReportList(List<HSNReportForm> hsnReportList) {
		this.hsnReportList = hsnReportList;
	}
	public List<ExempListForm> getExempList() {
		return exempList;
	}
	public void setExempList(List<ExempListForm> exempList) {
		this.exempList = exempList;
	}
	
}
