package com.fasset.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasset.entities.abstracts.AbstractEntity;

@Entity
@Table(name = "chapter")
public class Chapter extends AbstractEntity{

	@Id
	@GeneratedValue
	@Column(name = "chapter_id")
	private Long chapter_id;
	
	@Column(name = "chapter_no")
	private Integer chapterNo;
	
	
	private static final long serialVersionUID = 1L;


	public Long getChapter_id() {
		return chapter_id;
	}


	public void setChapter_id(Long chapter_id) {
		this.chapter_id = chapter_id;
	}


	public Integer getChapterNo() {
		return chapterNo;
	}


	public void setChapterNo(Integer chapterNo) {
		this.chapterNo = chapterNo;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
