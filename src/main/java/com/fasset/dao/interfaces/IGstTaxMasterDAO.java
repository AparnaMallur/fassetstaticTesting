package com.fasset.dao.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Chapter;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.Schedule;

public interface IGstTaxMasterDAO extends IGenericDao<GstTaxMaster>{
	public String deleteByIdValue(Long entityId);

	public List<GstTaxMaster> findAllListing();

	List<GstTaxMaster> findAllsearch(String term);

	public GstTaxMaster getHSNbyDate(LocalDate date, String hsn);
	GstTaxMaster getGSTbyHSN(String hsn,LocalDate startdate);
	Chapter getChapter(Integer chapterNo);
	Schedule getSchedule(String scheduleName);
	GstTaxMaster getGstforBackdatedEntry(String hsn,LocalDate startdate);
	Integer checkExcelRowNo(String rowno);
	List<Chapter> findAllChapters();
	List<Schedule> findAllSchedules();
	Chapter getChapter(Long chapterid);
	Schedule getSchedule(Long scheduleid);
	List<GstTaxMaster> getHSNSACNoForNonInventory(String term,LocalDate date);
	
}