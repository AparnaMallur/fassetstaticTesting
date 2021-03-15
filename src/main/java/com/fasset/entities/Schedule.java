package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "schedule")
public class Schedule extends AbstractEntity{
	
	@Id
	@GeneratedValue
	@Column(name = "schedule_id")
	private Long schedule_id;
	
	
	@Column(name = "schedule_name")
	private String scheduleName;
	
	private static final long serialVersionUID = 1L;

	public Long getSchedule_id() {
		return schedule_id;
	}

	public void setSchedule_id(Long schedule_id) {
		this.schedule_id = schedule_id;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	
	
}
