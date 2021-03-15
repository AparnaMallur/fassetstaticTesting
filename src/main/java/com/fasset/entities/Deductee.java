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
import org.joda.time.LocalDateTime;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "deductee_master")
public class Deductee extends AbstractEntity{

		/**
		 * 
		 */
		private static final long serialVersionUID = 6417341497743655418L;
	
		@Id
		@GeneratedValue
		@Column(name = "deductee_id", unique = true, nullable = false)
		private Long deductee_id;
	
		@Column(name = "deductee_title", nullable = true, length = MyAbstractController.SIZE_THREE_HUNDRED)
		private String deductee_title;
		
	    

		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "industry_id", referencedColumnName = "industry_id", insertable = true, updatable = true)
		private IndustryType industryType ;
		
	    @ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "tdsType_id", referencedColumnName = "tdsType_id", insertable = true, updatable = true)
		private TDS_Type tds_type;
	    
		@Transient
		private Long industry_id ;
		
		@Transient
		private Long tds_type_id ;
		

		@Transient
		private String value1 ;
		
		@Column(name = "value", nullable = true)
		private Float value;
	
		@Column(name = "from_mobile", nullable = true)
		private Boolean from_mobile;
	
		@Column(name = "status", nullable = true)
		private Boolean status;
	
		@Column(name = "created_date", nullable = true)
		private LocalDateTime created_date;
	
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "created_by")
		private User created_by;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "updated_by")
		private User updated_by;
	
		@Column(name = "update_date", nullable = true)
		private LocalDateTime update_date;
		
		@Column(name = "ip_address", nullable = true)
		private String ip_address ;

		@Column(name = "Effective_date", nullable = true)
		private LocalDate Effective_date;
		
		public Long getDeductee_id() {
			return deductee_id;
		}

		public LocalDate getEffective_date() {
			return Effective_date;
		}

		public void setEffective_date(LocalDate Effective_date) {
			this.Effective_date = Effective_date;
		}

		public void setDeductee_id(Long deductee_id) {
			this.deductee_id = deductee_id;
		}

		public String getDeductee_title() {
			return deductee_title;
		}

		public void setDeductee_title(String deductee_title) {
			this.deductee_title = deductee_title;
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

		public LocalDateTime getCreated_date() {
			return created_date;
		}

		public void setCreated_date(LocalDateTime created_date) {
			this.created_date = created_date;
		}

			public LocalDateTime getUpdate_date() {
			return update_date;
		}

		public void setUpdate_date(LocalDateTime update_date) {
			this.update_date = update_date;
		}

		public IndustryType getIndustryType() {
			return industryType;
		}

		public void setIndustryType(IndustryType industryType) {
			this.industryType = industryType;
		}

		public Long getIndustry_id() {
			return industry_id;
		}

		public void setIndustry_id(Long industry_id) {
			this.industry_id = industry_id;
		}
		public TDS_Type getTds_type() {
			return tds_type;
		}

		public void setTds_type(TDS_Type tds_type) {
			this.tds_type = tds_type;
		}
		public void setCreated_by(User created_by) {
			this.created_by = created_by;
		}

		public void setUpdated_by(User updated_by) {
			this.updated_by = updated_by;
		}

		public String getValue1() {
			return value1;
		}

		public void setValue1(String value1) {
			this.value1 = value1;
		}

		public User getCreated_by() {
			return created_by;
		}

		public User getUpdated_by() {
			return updated_by;
		}

		public Float getValue() {
			return value;
		}

		public void setValue(Float value) {
			this.value = value;
		}

		public String getIp_address() {
			return ip_address;
		}

		public void setIp_address(String ip_address) {
			this.ip_address = ip_address;
		}
		public Long getTds_type_id() {
			return tds_type_id;
		}

		public void setTds_type_id(Long tds_type_id) {
			this.tds_type_id = tds_type_id;
		}
}
