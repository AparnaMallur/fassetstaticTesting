package com.fasset.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.validators.GSTValidator;
import com.fasset.dao.interfaces.IGstTaxMasterDAO;
import com.fasset.entities.Chapter;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.Schedule;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.report.controller.ClassToConvertDateForExcell;
import com.fasset.service.interfaces.IGstTaxMasterService;

@Controller
@SessionAttributes("user")
public class GSTController {	
	@Autowired
	private IGstTaxMasterService gstService;
	
	@Autowired
	private GSTValidator gstValidator;
	
	@Autowired
	private IGstTaxMasterDAO GstTaxDao;
	 
	Boolean isImport = false; 
	 private List<String> successList = new ArrayList<String>();
	 private List<String> failureList = new ArrayList<String>();
	 private List<String> updateList = new ArrayList<String>();
	 private  String successmsg;
	 private  String failmsg;
	 private  String updatemsg;
	 private  String error;
	 
	@RequestMapping(value = "addGST", method = RequestMethod.GET)
	public ModelAndView gstFrom(){
		ModelAndView model = new ModelAndView();
		model.addObject("gst", new GstTaxMaster());
		model.addObject("chapterList", gstService.findAllChapters());
		model.addObject("scheduleList",gstService.findAllSchedules());
		model.setViewName("/master/gstTax");
		return model;
	}
	
	@RequestMapping(value = "gstList", method = RequestMethod.GET)
	public ModelAndView gstList(@RequestParam(value = "msg", required = false)String msg,HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("successMsg") != null) {
			model.addObject("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if(isImport==false)
		{	// code for import . here we are refreshing list and massages which are declared as global.
			successList.clear();
			failureList.clear();
			updateList.clear();
			successmsg=null;
			failmsg=null;
			updatemsg=null;
			error=null; 
			// code for import . here we are refreshing list and massages which are declared as global.
		}
		else if(isImport==true)
        {
		model.addObject("successList", successList);
		model.addObject("failureList",failureList);
		model.addObject("updateList",updateList);
		model.addObject("successImportmsg", successmsg);
		model.addObject("failmsg",failmsg);
		model.addObject("updatemsg",updatemsg);
		model.addObject("error",error);
		model.addObject("isImport",true);
		// code for import
		isImport=false;
	   }
		model.addObject("gstList",  gstService.findAllListing());
		model.setViewName("/master/gstTaxList");
		return model;
	}
	
	@RequestMapping(value = "saveGST", method = RequestMethod.POST)
	public ModelAndView saveGST(@ModelAttribute("gst") GstTaxMaster gst, BindingResult result,HttpServletRequest request, HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long user_id=(long)session.getAttribute("user_id");
System.out.println("The gst start date "+gst.getStart_date());
		gstValidator.validate(gst, result);
		if(result.hasErrors()){
			model.addObject("error", true);
			model.addObject("chapterList", gstService.findAllChapters());
			model.addObject("scheduleList",gstService.findAllSchedules());
			model.setViewName("/master/gstTax");
			return model;
		}
		else{
			String msg = "";
			try {
				if(gst.getTax_id() != null){
					long id = gst.getTax_id();
					if(id>0)
					{
					gst.setUpdated_by(user_id);
					gst.setStart_date(gst.getStart_date());
					 gstService.update(gst);
					 msg = "GST updated successfully";
					 //model.addObject("successMsg", "GST updated successfully");
					}
				}
				else{
					
					gst.setCreated_by(user_id);
					String remoteAddr = "";
					 remoteAddr = request.getHeader("X-FORWARDED-FOR");
			            if (remoteAddr == null || "".equals(remoteAddr)) {
			                remoteAddr = request.getRemoteAddr();
			            }
			            gst.setIp_address(remoteAddr);
					gstService.addGstMaster(gst);
					msg = "GST added successfully";
					//model.addObject("successMsg", "GST added successfully");
				}
			} catch (MyWebException e) {
				e.printStackTrace();
			}
			session.setAttribute("successMsg", msg);
			return new ModelAndView("redirect:/gstList");		
		}
	}
	
	@RequestMapping(value = "editGST", method = RequestMethod.GET)
	public ModelAndView editGST(@RequestParam("id") Long id){
		ModelAndView model = new ModelAndView();
		try {
			GstTaxMaster gst = gstService.getById(id);
			gst.setCgst1(gst.getCgst().toString());
			gst.setSgst1(gst.getSgst().toString());
			gst.setIgst1(gst.getIgst().toString());
			gst.setScc(gst.getState_comp_cess().toString());
			if (gst.getChapter() != null) {
				gst.setChapterNo(gst.getChapter().getChapterNo());
			}
			if (gst.getSchedule() != null) {
				gst.setScheduleName(gst.getSchedule().getScheduleName());
			}
		
			model.addObject("gst", gst);
			model.addObject("chapterList", gstService.findAllChapters());
			model.addObject("scheduleList",gstService.findAllSchedules());
			model.setViewName("/master/gstTax");
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		return model;
	}
	

	@RequestMapping(value = "viewGstAutoJV", method = RequestMethod.GET)
	public ModelAndView viewGstAutoJV(@RequestParam("id") Long id){
		ModelAndView model = new ModelAndView();
		try {
			GstTaxMaster gst = gstService.getById(id);
			model.addObject("gst", gst);
			model.setViewName("/master/gstTaxView");
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping(value = "getHSNSACNo", method = RequestMethod.GET)
	public @ResponseBody List<GstTaxMaster> getHSNSACNo(@RequestParam(value="term")String term){
		List<GstTaxMaster> gstAllList = gstService.listsearch(term);
	 	List<GstTaxMaster> gstMatchList=new ArrayList<GstTaxMaster>();
	 	for(GstTaxMaster gst:gstAllList){
	 		if(gst.getChapter()!=null && gst.getSchedule()!=null)
	 		{
	 			gst.setChapterNo(gst.getChapter().getChapterNo());
	 			gst.setScheduleName(gst.getSchedule().getScheduleName());
	 			gstMatchList.add(gst);	
	 		}
	 					
	 	}	 	
		return gstMatchList;
	}
	
	@RequestMapping(value = "getHSNSACNoForNonInventory", method = RequestMethod.GET)
	public @ResponseBody List<GstTaxMaster> getHSNSACNoForNonInventory(@RequestParam(value="term")String term,@RequestParam(value="date")String date){
		LocalDate newdate=new LocalDate(date);
		List<GstTaxMaster> gstAllList = gstService.getHSNSACNoForNonInventory(term,newdate);
	 	List<GstTaxMaster> gstMatchList=new ArrayList<GstTaxMaster>();
	 	for(GstTaxMaster gst:gstAllList){
	 		if(gst.getChapter()!=null && gst.getSchedule()!=null)
	 		{
	 			gst.setChapterNo(gst.getChapter().getChapterNo());
	 			gst.setScheduleName(gst.getSchedule().getScheduleName());
	 			gstMatchList.add(gst);	
	 		}
	 					
	 	}	 	
		return gstMatchList;
	}
	
	@RequestMapping(value = "getHSNSACByDate", method = RequestMethod.GET)
	public @ResponseBody GstTaxMaster getHSNSACByDate(@RequestParam(value="date")String date,@RequestParam(value="hsn")String hsn){
		
		LocalDate newdate=new LocalDate(date);
		return gstService.getHSNbyDate(newdate,hsn);
	}
	@RequestMapping(value = "deleteGST", method = RequestMethod.GET)
	public ModelAndView deleteGST(@RequestParam("id") Long id,HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession(true);
		session.setAttribute("successMsg", gstService.deleteByIdValue(id));
		return new ModelAndView("redirect:/gstList");
	}
	//Record Imported, Please check file format
	@RequestMapping(value = {"importExcelGST" }, method = { RequestMethod.POST })
	public ModelAndView importExcelLedger(@RequestParam("excelFile") MultipartFile excelfile, 
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView();
		boolean isValid = true;
	
		String success = "Record Imported Successfully";
		int successcount = 0;
		String chapter="Chapter is blank"; //  added for GSTR import issue
		String schedule="Schedule is blank"; //  added for GSTR import issue
		String DateIsBlank="Please enter start date in DD/MM/YYYY format for below HSN numbers.";
		String fail = "";
		int failcount = 0;
		String update = "These are the HSN numbers for which rates are not changed and already present in system";
		int updatecount = 0;
		successmsg = "";
	    failmsg = "";
	    updatemsg = "";
	    error = "";
	    isImport=true;
	    successList.clear();
		failureList.clear();
		updateList.clear();
		HttpSession session = request.getSession(true);
		
		User user = new User();
		user = (User) session.getAttribute("user");
		try {
			if (excelfile.getOriginalFilename().endsWith("xls")) {
				int i = 0;
				HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
				HSSFSheet worksheet = workbook.getSheetAt(0);
				int rowcount=0;
				while (i <= worksheet.getLastRowNum()) {
					int colcount=0;
					HSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 2; j++) {
						if (row.getCell(j) != null) {
							colcount++;
						}
					}
					if(colcount>=3)
					{
					rowcount++;
					}
				}

				if (isValid) {
					i = 1;
					while (i <= rowcount) {
						GstTaxMaster gst = new GstTaxMaster();
						HSSFRow row = worksheet.getRow(i++);
						
						if (row.getLastCellNum() == 0) {
							
						} else {
							
							Boolean flag = false;
					    	try
							{
					    		
								Double hsncode= row.getCell(0).getNumericCellValue();
								Integer hsncode1 = hsncode.intValue();
								String hsn =hsncode1.toString().trim();
								gst.setHsc_sac_code(hsn);
							}
							catch(Exception e)
							{
								/*e.printStackTrace();*/
								
							}
							try
							{
								
								String hsn=row.getCell(0).getStringCellValue(); 
								gst.setHsc_sac_code(hsn);
							}
							catch(Exception e)
							{
								/*e.printStackTrace();*/
								
							}
					
							try
							{
								
								Double rowno= row.getCell(9).getNumericCellValue();
								Integer rowno1 = rowno.intValue();
								String excel_row_no=rowno1.toString().trim();
								gst.setExcel_row_no(excel_row_no);
							}
							catch(Exception e)
							{
								/*e.printStackTrace();*/
								
							}
							try
							{
								
								String excel_row_no=row.getCell(9).getStringCellValue(); 
								gst.setExcel_row_no(excel_row_no);
							}
							catch(Exception e)
							{
								/*e.printStackTrace();*/
							
							}
							
							if(gstService.checkExcelRowNo(gst.getExcel_row_no()).equals(0))
							{
                            if(row.getCell(1)!=null)
                            {
                            	try
    							{
							   gst.setDescription(row.getCell(1).getStringCellValue());
    							}
                            	catch(Exception e)
                            	{
                            		
                            	}
							
                            	try
    							{
							   gst.setDescription(new Double(row.getCell(1).getNumericCellValue()).toString());
    							}
                            	catch(Exception e)
                            	{
                            		
                            	}
                            }
                            else
                            {
                            	gst.setDescription("");
                            }
							
							try
							{
							String dt=	row.getCell(8).getDateCellValue().toString();
							
							
							dt=ClassToConvertDateForExcell.dateConvert(dt);
							
							LocalDate Dat= new  LocalDate(dt);
							
								gst.setStart_date(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(8).getDateCellValue().toString())));
								
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
						   if(row.getCell(5)!=null)
	                        {
							   try
								{
							 gst.setCgst((float) row.getCell(5).getNumericCellValue());
								}
							   catch(Exception e)
                           	   {
								   gst.setCgst((float)0);
                               }
	                        }
							 else
							 {
								 gst.setCgst((float)0);
							 }
						   
						   if(row.getCell(4)!=null)
	                       {
								
								 try
									{
									 gst.setIgst((float) row.getCell(4).getNumericCellValue());
									}
								   catch(Exception e)
	                           	   {
									   gst.setIgst((float)0);
	                               }
	                       }
							 else
							 {
								 gst.setIgst((float)0);
							 }
						   
						   if(row.getCell(6)!=null)
	                       {
							  
							   try
								{
								   gst.setSgst((float) row.getCell(6).getNumericCellValue());
								}
							   catch(Exception e)
                          	   {
								   gst.setSgst((float)0);
                              }
	                       }
							 else
							 {
								 gst.setSgst((float)0);
							 }
							
						   if(row.getCell(7)!=null)
	                       {
							  
							   try
								{
								   gst.setState_comp_cess((float) row.getCell(7).getNumericCellValue());
								}
							   catch(Exception e)
                         	   {
								   gst.setState_comp_cess((float)0);
                             }
	                       }
							 else
							 {
								 gst.setState_comp_cess((float)0);
							 }
						
							gst.setStatus(true);
							
							if(row.getCell(2)!=null)
		                       {
								 try
									{
									 
								gst.setChapter(gstService.getChapter((int) row.getCell(2).getNumericCellValue()));
									}
								 catch(Exception e)
								 {
									 
								 }
		                       }
								 else
								 {
									 gst.setChapter(null);
								 }
							
							if(row.getCell(3)!=null)
		                       {
								try
								{
								gst.setSchedule(gstService.getSchedule(row.getCell(3).getStringCellValue()));
								}
								 catch(Exception e)
								 {
									 
								 }
		                       }
								 else
								 {
									 gst.setSchedule(null);
								 }
							
							String remoteAddr = "";
							 remoteAddr = request.getHeader("X-FORWARDED-FOR");
					            if (remoteAddr == null || "".equals(remoteAddr)) {
					                remoteAddr = request.getRemoteAddr();
					            }
					            gst.setIp_address(remoteAddr);
					          //|| gst.getSchedule()==null || gst.getChapter()==null commented so that user is shown exactly what field is blank
								//new code
							if(gst.getStart_date()==null )
							{
								flag=true;
								fail=DateIsBlank;
							}else if(gst.getSchedule()==null){
								flag=true;
								fail=schedule;
							}else if(gst.getChapter()==null){
								flag=true;
								fail=chapter;
							}
							//end new code
							if(flag==true)
							{
								failureList.add(gst.getHsc_sac_code());
								failcount=failcount+1;
							}
							else
							{
								//we are checking entry with end date null which is our latest GST rate.
								GstTaxMaster gst1 = GstTaxDao.getGSTbyHSN(gst.getHsc_sac_code(),gst.getStart_date());
								
								if(gst1==null)
								{
									//for back dated entry we are checking entries based on start date ascending
									GstTaxMaster gst2 = gstService.getGstforBackdatedEntry(gst.getHsc_sac_code(),gst.getStart_date());
									if(gst2!=null)
									{
									gst.setCreated_by(user.getUser_id());
									gst.setEnd_date(gst2.getStart_date().minusDays(1));
									GstTaxDao.create(gst);	
									successList.add(gst.getHsc_sac_code());
									successcount=successcount+1;
									}
								}
								
								//Here we are checking our latest GST rate with previous rate in our records whose end date is NULL
								if(gst1!=null && gst1.getSchedule().getScheduleName().equals(gst.getSchedule().getScheduleName()) && gst1.getChapter().getChapterNo().equals(gst.getChapter().getChapterNo()) && gst1.getDescription().equals(gst.getDescription()) && gst1.getCgst().equals(gst.getCgst()) && gst1.getSgst().equals(gst.getSgst()) && gst1.getIgst().equals(gst.getIgst()) && gst1.getState_comp_cess().equals(gst.getState_comp_cess()))
								{
									//if it is same then it should be added in to updateList
									updateList.add(gst.getHsc_sac_code());
									updatecount=updatecount+1;
									
								}
								else
								{
									// if any value change occur in rates or chapter or description we will add new record and the last record will updated (end date will be added)
									if(gst1!=null)
									{
										gst1.setEnd_date(gst.getStart_date().minusDays(1));
										GstTaxDao.update(gst1);
										gst.setCreated_by(user.getUser_id());
										GstTaxDao.create(gst);	
										successList.add(gst.getHsc_sac_code());
										successcount=successcount+1;
									}
									
									// for new records. if database not contain any record.
									GstTaxMaster gst2 = gstService.getGstforBackdatedEntry(gst.getHsc_sac_code(),gst.getStart_date());
									if(gst1==null && gst2==null)
									{
										gst.setCreated_by(user.getUser_id());
										GstTaxDao.create(gst);	
										successList.add(gst.getHsc_sac_code());
										successcount=successcount+1;
									}
									
									
									
							    }
							}
						   }
			     		}
					}
					workbook.close();
				} else {
					
				}
			} else if (excelfile.getOriginalFilename().endsWith("xlsx")) {
				int i = 0;
				isValid = true;
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
			
				int rowcount=0;
				while (i <= worksheet.getLastRowNum()) {
					int colcount=0;
					XSSFRow row = worksheet.getRow(i++);
					for (int j = 0; j <= 2; j++) {
						if (row.getCell(j) != null) {
							colcount++;
						}
					}
					if(colcount>=3)
					{
					rowcount++;
					}
				}

				if (isValid) {
					i = 1;
					while (i < rowcount) {
						GstTaxMaster gst = new GstTaxMaster();
						XSSFRow row = worksheet.getRow(i++);
						
						if (row.getLastCellNum() == 0) {
							System.out.println("Invalid Data");
						} else {
							System.out.println("Gst import");
							Boolean flag = false;
					    	try
							{
					    		
					    		Double hsncode= row.getCell(0).getNumericCellValue();
								Integer hsncode1 = hsncode.intValue();
								String hsn =hsncode1.toString().trim();
								gst.setHsc_sac_code(hsn);
							}
							catch(Exception e)
							{
								/*e.printStackTrace();*/
								
							}
							try
							{
								
								String hsn=row.getCell(0).getStringCellValue(); 
								
								gst.setHsc_sac_code(hsn);
							}
							catch(Exception e)
							{
								/*e.printStackTrace();*/
								
							}
					
							try
							{
								Double rowno= row.getCell(9).getNumericCellValue();
								Integer rowno1 = rowno.intValue();
								String excel_row_no=rowno1.toString().trim();
								gst.setExcel_row_no(excel_row_no);
							}
							catch(Exception e)
							{
								/*e.printStackTrace();*/
								
							}
							try
							{
								String excel_row_no=row.getCell(9).getStringCellValue(); 
								gst.setExcel_row_no(excel_row_no);
							}
							catch(Exception e)
							{
								/*e.printStackTrace();*/
								
							}
							
							if(gstService.checkExcelRowNo(gst.getExcel_row_no()).equals(0))
							{
								
                            if(row.getCell(1)!=null)
                            {
                            	try
    							{
							   gst.setDescription(row.getCell(1).getStringCellValue());
    							}
                            	catch(Exception e)
                            	{
                            		
                            	}
							
                            	try
    							{
							   gst.setDescription(new Double(row.getCell(1).getNumericCellValue()).toString());
    							}
                            	catch(Exception e)
                            	{
                            		
                            	}
                            }
                            else
                            {
                            	gst.setDescription("");
                            }
							
							try
							{
								String dt=	row.getCell(8).getDateCellValue().toString();
								
								
								dt=ClassToConvertDateForExcell.dateConvert(dt);
							
								LocalDate Dat= new  LocalDate(dt);
								
								gst.setStart_date(new LocalDate(ClassToConvertDateForExcell.dateConvert(row.getCell(8).getDateCellValue().toString())));
								
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
						   if(row.getCell(5)!=null)
	                        {
							   try
								{
							 gst.setCgst((float) row.getCell(5).getNumericCellValue());
								}
							   catch(Exception e)
                           	   {
								   gst.setCgst((float)0);
                               }
	                        }
							 else
							 {
								 gst.setCgst((float)0);
							 }
						   
						   if(row.getCell(4)!=null)
	                       {
								
								 try
									{
									 gst.setIgst((float) row.getCell(4).getNumericCellValue());
									}
								   catch(Exception e)
	                           	   {
									   gst.setIgst((float)0);
	                               }
	                       }
							 else
							 {
								 gst.setIgst((float)0);
							 }
						   
						   if(row.getCell(6)!=null)
	                       {
							  
							   try
								{
								   gst.setSgst((float) row.getCell(6).getNumericCellValue());
								}
							   catch(Exception e)
                          	   {
								   gst.setSgst((float)0);
                              }
	                       }
							 else
							 {
								 gst.setSgst((float)0);
							 }
							
						   if(row.getCell(7)!=null)
	                       {
							  
							   try
								{
								   gst.setState_comp_cess((float) row.getCell(7).getNumericCellValue());
								}
							   catch(Exception e)
                         	   {
								   gst.setState_comp_cess((float)0);
                             }
	                       }
							 else
							 {
								 gst.setState_comp_cess((float)0);
							 }
						
							gst.setStatus(true);
							
							if(row.getCell(2)!=null)
		                       {
								 try
									{
								gst.setChapter(gstService.getChapter((int) row.getCell(2).getNumericCellValue()));
									}
								 catch(Exception e)
								 {
									
								 }
		                       }
								 else
								 {
									 gst.setChapter(null);
								 }
							
							if(row.getCell(3)!=null)
		                       {
								try
								{
								gst.setSchedule(gstService.getSchedule(row.getCell(3).getStringCellValue()));
								}
								 catch(Exception e)
								 {
									
								 }
		                       }
								 else
								 {
									
									 gst.setSchedule(null);
								 }
							
							String remoteAddr = "";
							 remoteAddr = request.getHeader("X-FORWARDED-FOR");
					            if (remoteAddr == null || "".equals(remoteAddr)) {
					                remoteAddr = request.getRemoteAddr();
					            }
					            gst.setIp_address(remoteAddr);
					            //|| gst.getSchedule()==null || gst.getChapter()==null commented so that user is shown exactly what field is blank
							//new code 
					            if(gst.getStart_date()==null )
							{
								flag=true;
								fail=DateIsBlank;
							}else if(gst.getSchedule()==null){
								flag=true;
								fail=schedule;
							}else if(gst.getChapter()==null){
								flag=true;
								fail=chapter;
							}
							//end new code
							if(flag==true)
							{
								failureList.add(gst.getHsc_sac_code());
								failcount=failcount+1;
								
							}
							else
							{
								
								//we are checking entry with end date null which is our latest GST rate.
								GstTaxMaster gst1 = GstTaxDao.getGSTbyHSN(gst.getHsc_sac_code(),gst.getStart_date());
								
								if(gst1==null)
								{
									//for back dated entry we are checking entries based on start date ascending
									GstTaxMaster gst2 = gstService.getGstforBackdatedEntry(gst.getHsc_sac_code(),gst.getStart_date());
									if(gst2!=null)
									{
									gst.setCreated_by(user.getUser_id());
									gst.setEnd_date(gst2.getStart_date().minusDays(1));
									GstTaxDao.create(gst);	
									successList.add(gst.getHsc_sac_code());
									successcount=successcount+1;
									}
								}
								
								//Here we are checking our latest GST rate with previous rate in our records whose end date is NULL
								if(gst1!=null && gst1.getSchedule().getScheduleName().equals(gst.getSchedule().getScheduleName()) && gst1.getChapter().getChapterNo().equals(gst.getChapter().getChapterNo()) && gst1.getDescription().equals(gst.getDescription()) && gst1.getCgst().equals(gst.getCgst()) && gst1.getSgst().equals(gst.getSgst()) && gst1.getIgst().equals(gst.getIgst()) && gst1.getState_comp_cess().equals(gst.getState_comp_cess()))
								{
									//if it is same then it should be added in to updateList
									updateList.add(gst.getHsc_sac_code());
									updatecount=updatecount+1;
								}
								else
								{
									// if any value change occur in rates or chapter or description we will add new record and the last record will updated (end date will be added)
									if(gst1!=null)
									{
										gst1.setEnd_date(gst.getStart_date().minusDays(1));
										GstTaxDao.update(gst1);
										gst.setCreated_by(user.getUser_id());
										GstTaxDao.create(gst);	
										successList.add(gst.getHsc_sac_code());
										successcount=successcount+1;
									}
									
									// for new records. if database not contain any record.
									GstTaxMaster gst2 = gstService.getGstforBackdatedEntry(gst.getHsc_sac_code(),gst.getStart_date());
									if(gst1==null && gst2==null)
									{
										gst.setCreated_by(user.getUser_id());
										GstTaxDao.create(gst);	
										successList.add(gst.getHsc_sac_code());
										successcount=successcount+1;
									}
									
									
									
							    }
							}
						   }
			     		}
					}
					workbook.close();
				} else {
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isValid = false;
		}
		//Please enter start date in DD/MM/YYYY format for below HSN numbers.
		if (successcount != 0) {
			successmsg += "<h5 class='green-lable'>" + successcount + " " + success + "</h5>";
		}
		if (updatecount != 0) {
			updatemsg += "<h5 class='orange-lable'>" + updatecount + " " + update + "</h5>";
		}
		if (failcount != 0) {
			failmsg += "<h5 class='red-lable'>" + fail + "</h5>";
		}
		if ((successcount == 0) && (updatecount == 0) && (failcount == 0)) {
			error = "0 Record Imported, Please check file format";
		}
		model.setViewName("redirect:/gstList");
		return model;
	}
}