package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasset.controller.abstracts.MyAbstractController;

@Entity
@Table(name = "quotation_details")
public class QuotationDetails {

	@Id
	@GeneratedValue
	@Column(name = "quotation_detail_id")
	private Long quotation_detail_id;
		
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "quotation_id")
	private Quotation quotation_id;
		
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_id")
	private Service service_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "frequency_id")
	private ServiceFrequency frequency_id;
	
	@Column(name = "service_status", nullable = true, length = MyAbstractController.SIZE_THIRTY)
	private Boolean service_status ;
	
	@Column(name = "amount")
	private Float amount;

	public Long getQuotation_detail_id() {
		return quotation_detail_id;
	}

	public void setQuotation_detail_id(Long quotation_detail_id) {
		this.quotation_detail_id = quotation_detail_id;
	}

	public Quotation getQuotation_id() {
		return quotation_id;
	}

	public void setQuotation_id(Quotation quotation_id) {
		this.quotation_id = quotation_id;
	}

	public Service getService_id() {
		return service_id;
	}

	public void setService_id(Service service_id) {
		this.service_id = service_id;
	}

	public ServiceFrequency getFrequency_id() {
		return frequency_id;
	}

	public void setFrequency_id(ServiceFrequency frequency_id) {
		this.frequency_id = frequency_id;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((frequency_id == null) ? 0 : frequency_id.hashCode());
		result = prime * result + ((service_id == null) ? 0 : service_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuotationDetails other = (QuotationDetails) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (frequency_id == null) {
			if (other.frequency_id != null)
				return false;
		} else if (!frequency_id.equals(other.frequency_id))
			return false;
		if (service_id == null) {
			if (other.service_id != null)
				return false;
		} else if (!service_id.equals(other.service_id))
			return false;
		return true;
	}

	public Boolean getService_status() {
		return service_status;		
	}

	public void setService_status(Boolean service_status) {
		this.service_status = service_status;
	}
	
}