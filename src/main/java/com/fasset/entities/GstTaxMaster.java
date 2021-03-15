package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.LocalDate;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "gst_tax_master")
public class GstTaxMaster extends AbstractEntity{	
	@Id
	@GeneratedValue
	@Column(name = "tax_id", unique = true, nullable = false)
    private Long tax_id ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "chapter_id")
	@JsonIgnore
	private Chapter chapter ;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "schedule_id")
	@JsonIgnore
	private Schedule schedule ;
	
	@Column(name = "tax_name")
	private String tax_name;
	
	@Column(name = "hsc_sac_code")
	private String hsc_sac_code;
	
	@Column(name = "sgst")
	private Float sgst;
	
	@Transient
	private String sgst1;
	
	@Column(name = "cgst")
	private Float cgst;
	
	@Transient
	private String cgst1;
	
	@Column(name = "igst")
	private Float igst;
	
	@Transient
	private String igst1;
	
	@Column(name = "state_comp_cess")
	private Float state_comp_cess;
	
	@Transient
	private String scc;
	
	@Column(name = "from_mobile")
	private Boolean from_mobile;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "flag")
	private Boolean flag;
	
	@Column(name = "updated_date")
	private LocalDate updated_date;	

	@Column(name = "created_date")
	private LocalDate created_date; 
	  
	@Column(name = "start_date", nullable = true)
	private LocalDate start_date ;
 
	@Column(name = "end_date", nullable = true)
	private LocalDate end_date ;	
 	
	@Column(name = "created_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Long created_by;

	@Column(name = "updated_by", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Long updated_by;
	
	@Column(name = "description", length = 1000)
	private String description;
	
	@Column(name = "ip_address", nullable = true)
	private String ip_address ;
	
	@Column(name = "excel_row_no")
	private String excel_row_no ;
	
	@Transient
	private Long chapter_id;
	
	@Transient
	private Long schedule_id;
	
	@Transient
	private Integer chapterNo;
	
	@Transient
	private String scheduleName;
	
	public Integer getChapterNo() {
		return chapterNo;
	}

	public void setChapterNo(Integer chapterNo) {
		this.chapterNo = chapterNo;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public Long getChapter_id() {
		return chapter_id;
	}

	public void setChapter_id(Long chapter_id) {
		this.chapter_id = chapter_id;
	}

	public Long getSchedule_id() {
		return schedule_id;
	}

	public void setSchedule_id(Long schedule_id) {
		this.schedule_id = schedule_id;
	}

	public Long getTax_id() {
		return tax_id;
	}

	public void setTax_id(Long tax_id) {
		this.tax_id = tax_id;
	}

	public String getTax_name() {
		return tax_name;
	}

	public void setTax_name(String tax_name) {
		this.tax_name = tax_name;
	}

	public String getHsc_sac_code() {
		return hsc_sac_code;
	}

	public void setHsc_sac_code(String hsc_sac_code) {
		this.hsc_sac_code = hsc_sac_code;
	}

	public Float getSgst() {
		return sgst;
	}

	public void setSgst(Float sgst) {
		this.sgst = sgst;
	}

	public Float getCgst() {
		return cgst;
	}

	public void setCgst(Float cgst) {
		this.cgst = cgst;
	}

	public Float getIgst() {
		return igst;
	}

	public void setIgst(Float igst) {
		this.igst = igst;
	}

	public Float getState_comp_cess() {
		return state_comp_cess;
	}

	public void setState_comp_cess(Float state_comp_cess) {
		this.state_comp_cess = state_comp_cess;
	}

	public Boolean getFrom_mobile() {
		return from_mobile;
	}

	public void setFrom_mobile(Boolean from_mobile) {
		this.from_mobile = from_mobile;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
	}	

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}	

	public String getSgst1() {
		return sgst1;
	}

	public void setSgst1(String sgst1) {
		this.sgst1 = sgst1;
	}

	public String getCgst1() {
		return cgst1;
	}

	public void setCgst1(String cgst1) {
		this.cgst1 = cgst1;
	}

	public String getIgst1() {
		return igst1;
	}

	public void setIgst1(String igst1) {
		this.igst1 = igst1;
	}

	public String getScc() {
		return scc;
	}

	public void setScc(String scc) {
		this.scc = scc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Long created_by) {
		this.created_by = created_by;
	}

	public Long getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Long updated_by) {
		this.updated_by = updated_by;
	}

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	public LocalDate getEnd_date() {
		return end_date;
	}

	public void setEnd_date(LocalDate end_date) {
		this.end_date = end_date;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public String getExcel_row_no() {
		return excel_row_no;
	}

	public void setExcel_row_no(String excel_row_no) {
		this.excel_row_no = excel_row_no;
	}
	
}
