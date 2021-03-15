package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.Chapter;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.Schedule;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IGstTaxMasterService extends IGenericService<GstTaxMaster>{
	Boolean isGSTExists(String hsnSacCode, LocalDate startdate, LocalDate enddate);
	public String deleteByIdValue(Long entityId);
	List<GstTaxMaster> findAllListing();
	List<GstTaxMaster> listsearch(String term);
	GstTaxMaster getHSNbyDate(LocalDate newdate, String hsn);
	GstTaxMaster getGSTbyHSN(String hsn,LocalDate startdate);
	Chapter getChapter(Integer chapterNo);
	Schedule getSchedule(String scheduleName);
	GstTaxMaster getGstforBackdatedEntry(String hsn,LocalDate startdate);
	Integer checkExcelRowNo(String rowno);
	List<Chapter> findAllChapters();
	List<Schedule> findAllSchedules();
	String addGstMaster(GstTaxMaster entity);
	String updateGstMaster(GstTaxMaster entity);
	List<GstTaxMaster> getHSNSACNoForNonInventory(String term,LocalDate date);
}