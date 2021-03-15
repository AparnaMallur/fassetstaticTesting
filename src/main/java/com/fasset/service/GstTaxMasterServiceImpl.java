package com.fasset.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IGstTaxMasterDAO;
import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.entities.Chapter;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.Schedule;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IGstTaxMasterService;

@Transactional
@Service
public class GstTaxMasterServiceImpl implements IGstTaxMasterService{
	
	@Autowired
	private IGstTaxMasterDAO GstTaxDao;
	
	@Autowired IStateDAO stateDao;

	@Override
	public void add(GstTaxMaster entity) throws MyWebException {
			
	}

	@Override
	public void update(GstTaxMaster entity) throws MyWebException {
		GstTaxMaster gst = GstTaxDao.findOne(entity.getTax_id());
		/*gst.setHsc_sac_code(entity.getHsc_sac_code());*/
		gst.setCgst(Float.parseFloat(entity.getCgst1()));
		gst.setIgst(Float.parseFloat(entity.getIgst1()));
		gst.setSgst(Float.parseFloat(entity.getSgst1()));
		gst.setState_comp_cess(Float.parseFloat(entity.getScc()));
		/*gst.setStart_date(entity.getStart_date());*/
	/*	gst.setEnd_date(entity.getEnd_date());*/
		gst.setStatus(entity.getStatus());
		gst.setDescription(entity.getDescription());
		GstTaxDao.update(gst);		
	}

	@Override
	public List<GstTaxMaster> list() {
		return GstTaxDao.findAll();
	}

	@Override
	public List<GstTaxMaster> listsearch(String term) {
		return GstTaxDao.findAllsearch(term);
	}

	@Override
	public GstTaxMaster getById(Long id) throws MyWebException {
		return GstTaxDao.findOne(id);
	}

	@Override
	public GstTaxMaster getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(GstTaxMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(GstTaxMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean isGSTExists(String hsnSacCode,LocalDate startdate, LocalDate enddate) {
		List<GstTaxMaster> gstList = GstTaxDao.findAll();
		Boolean flag = false;
		for(GstTaxMaster gst :gstList){
			if(hsnSacCode.equals(gst.getHsc_sac_code())){
				if((gst.getStart_date().equals(startdate)) && (gst.getEnd_date().equals(enddate)))
				{
				flag = true;
				break;
				}
				else
				{
					flag = false;
				}				
			}
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public GstTaxMaster isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		return GstTaxDao.deleteByIdValue(entityId);
	}

	@Override
	public List<GstTaxMaster> findAllListing() {
		// TODO Auto-generated method stub
		return GstTaxDao.findAllListing();
	}

	@Override
	public GstTaxMaster getHSNbyDate(LocalDate date, String hsn) {
		// TODO Auto-generated method stub
		return GstTaxDao.getHSNbyDate(date,hsn);
	}

	@Override
	public GstTaxMaster getGSTbyHSN(String hsn,LocalDate startdate) {
		// TODO Auto-generated method stub
		return GstTaxDao.getGSTbyHSN(hsn,startdate);
	}

	@Override
	public Chapter getChapter(Integer chapterNo) {
		// TODO Auto-generated method stub
		return GstTaxDao.getChapter(chapterNo);
	}

	@Override
	public Schedule getSchedule(String scheduleName) {
		// TODO Auto-generated method stub
		return GstTaxDao.getSchedule(scheduleName);
	}

	@Override
	public GstTaxMaster getGstforBackdatedEntry(String hsn,LocalDate startdate) {
		// TODO Auto-generated method stub
		return GstTaxDao.getGstforBackdatedEntry(hsn,startdate);
	}

	@Override
	public Integer checkExcelRowNo(String rowno) {
		// TODO Auto-generated method stub
		return GstTaxDao.checkExcelRowNo(rowno);
	}

	@Override
	public List<Chapter> findAllChapters() {
		// TODO Auto-generated method stub
		return GstTaxDao.findAllChapters();
	}

	@Override
	public List<Schedule> findAllSchedules() {
		// TODO Auto-generated method stub
		return GstTaxDao.findAllSchedules();
	}

	@Override
	public String addGstMaster(GstTaxMaster entity)
	{
		String msg = null;
		entity.setCgst(Float.parseFloat(entity.getCgst1()));
		entity.setIgst(Float.parseFloat(entity.getIgst1()));
		entity.setSgst(Float.parseFloat(entity.getSgst1()));
		entity.setState_comp_cess(Float.parseFloat(entity.getScc()));
		entity.setChapter(GstTaxDao.getChapter(entity.getChapter_id()));
		entity.setSchedule(GstTaxDao.getSchedule(entity.getSchedule_id()));
		GstTaxMaster gst1 = GstTaxDao.getGSTbyHSN(entity.getHsc_sac_code(),entity.getStart_date());
		
		if(gst1==null)
		{
			//for back dated entry we are checking entries based on start date ascending
			GstTaxMaster gst2 = GstTaxDao.getGstforBackdatedEntry(entity.getHsc_sac_code(),entity.getStart_date());
			if(gst2!=null)
			{
			
			entity.setEnd_date(gst2.getStart_date().minusDays(1));
			try {
				GstTaxDao.create(entity);
				msg = "GST added successfully";
			} catch (MyWebException e) {
				
				e.printStackTrace();
			}	
			
			}
		}
		
		//Here we are checking our latest GST rate with previous rate in our records whose end date is NULL
		if(gst1!=null && gst1.getSchedule().getScheduleName().equals(entity.getSchedule().getScheduleName()) && gst1.getChapter().getChapterNo().equals(entity.getChapter().getChapterNo()) && gst1.getDescription().equals(entity.getDescription()) && gst1.getCgst().equals(entity.getCgst()) && gst1.getSgst().equals(entity.getSgst()) && gst1.getIgst().equals(entity.getIgst()) && gst1.getState_comp_cess().equals(entity.getState_comp_cess()))
		{
			/*updateList.add(gst.getHsc_sac_code());
			updatecount=updatecount+1;*/
			msg = "A record is already present in database with this values.";
		}
		else
		{
			// if any value change occur in rates or chapter or description we will add new record and the last record will updated (end date will be added)
			if(gst1!=null)
			{
				gst1.setEnd_date(entity.getStart_date().minusDays(1));
				try {
				GstTaxDao.update(gst1);
				GstTaxDao.create(entity);
				msg = "GST added successfully";
				}
				catch (MyWebException e) {
					
					e.printStackTrace();
				}	
			}
			
			// for new records. if database not contain any record.
			GstTaxMaster gst2 = GstTaxDao.getGstforBackdatedEntry(entity.getHsc_sac_code(),entity.getStart_date());
			if(gst1==null && gst2==null)
			{
				
				try {
					GstTaxDao.create(entity);
					msg = "GST added successfully";
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
			}
			
			
			
	    }
		return msg;
	}

	@Override
	public String updateGstMaster(GstTaxMaster entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GstTaxMaster> getHSNSACNoForNonInventory(String term, LocalDate date) {
		// TODO Auto-generated method stub
		return GstTaxDao.getHSNSACNoForNonInventory(term, date);
	}

}