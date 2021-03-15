/**
 * mayur suramwar
 */
package com.fasset.form;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public class SubNature {
	private String subId;
	@Override
	public String toString() {
		return "SubNature [subId=" + subId + ", purpose=" + purpose + "]";
	}
	private String purpose;
	
	public SubNature() {
		super();
	}
	
	public SubNature(String subId, String purpose) {
		super();
		this.subId = subId;
		this.purpose = purpose;
	}
	public String getSubId() {
		return subId;
	}
	public void setSubId(String subId) {
		this.subId = subId;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	
}
