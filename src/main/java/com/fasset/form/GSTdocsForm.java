package com.fasset.form;

public class GSTdocsForm {

	private String natureOfdocument;
	private String srnofrom;
	private String srnoto;
	private Integer totalNumber;
	private Integer totalCancel;
	
	
	public String getNatureOfdocument() {
		return natureOfdocument;
	}
	public void setNatureOfdocument(String natureOfdocument) {
		this.natureOfdocument = natureOfdocument;
	}
	public String getSrnofrom() {
		return srnofrom;
	}
	public void setSrnofrom(String srnofrom) {
		this.srnofrom = srnofrom;
	}
	public String getSrnoto() {
		return srnoto;
	}
	public void setSrnoto(String srnoto) {
		this.srnoto = srnoto;
	}
	public Integer getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}
	public Integer getTotalCancel() {
		return totalCancel;
	}
	public void setTotalCancel(Integer totalCancel) {
		this.totalCancel = totalCancel;
	}
}
