/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

import org.jfree.data.time.Year;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IClientValidationChecklistDAO;
import com.fasset.dao.interfaces.IClientValidationChecklistStatusDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.ClientValidationChecklistStatus;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IClientValidationChecklistStatusService;
import com.fasset.service.interfaces.IMailService;
import com.fasset.service.interfaces.IYearEndingService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class ClientValidationChecklistStatusServiceImpl implements IClientValidationChecklistStatusService{

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#add(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Autowired
	private IClientValidationChecklistDAO status ;
	
	@Autowired
	private IClientValidationChecklistStatusDAO dao;
	
	@Autowired
	private IMailService mailService;

	@Autowired
	private IAccountingYearDAO accountYearDAO;
	
	@Autowired
	private IYearEndingService yearService ;
	
	@Autowired
	private ICompanyDAO companyDao;
	
	@Override
	public void add(ClientValidationChecklistStatus entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#update(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void update(ClientValidationChecklistStatus entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#list()
	 */
	@Override
	public List<ClientValidationChecklistStatus> list() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.Long)
	 */
	@Override
	public ClientValidationChecklistStatus getById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.String)
	 */
	@Override
	public ClientValidationChecklistStatus getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#removeById(java.lang.Long)
	 */
	@Override
	public void removeById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#removeById(java.lang.String)
	 */
	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#remove(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void remove(ClientValidationChecklistStatus entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#countRegs()
	 */
	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#merge(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void merge(ClientValidationChecklistStatus entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IClientValidationChecklistStatusService#saveClientValidationChecklistStatus(com.fasset.entities.ClientValidationChecklistStatus)
	 */
	@Override
	public String saveClientValidationChecklistStatus(ClientValidationChecklistStatus type,User Client) {
		List<String> checkList = type.getCheckList();
		Set<ClientValidationChecklist> newcheckList = new HashSet<ClientValidationChecklist>();
		String msg="";
		for(String id :checkList)
		{
			Long cid = Long.parseLong(id);
			try {
				ClientValidationChecklist validation = status.findOne(cid);
				newcheckList.add(validation);
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		type.setNewcheckList(newcheckList);
		msg = dao.saveClientValidationChecklistStatus(type,Client);
		
		if(msg.equalsIgnoreCase("Client Validation Checklist Saved Successfully"))
		{
			String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018
			LocalDate date= new LocalDate();
			String april1stDate=null;
			april1stDate= currentyear+"-"+"04"+"-"+"01";
			LocalDate april1stLocaldate = new LocalDate(april1stDate);
			
			if(date.isBefore(april1stLocaldate)) 
			{
				Integer year = Integer.parseInt(currentyear);
				year=year-1;
				String lastYear =year.toString();
				currentyear=lastYear+"-"+currentyear;
			}
			else if(date.isAfter(april1stLocaldate) || date.equals(april1stLocaldate))
			{
				Integer year = Integer.parseInt(currentyear);
				year=year+1;
				String nextYear =year.toString();
				currentyear=currentyear+"-"+nextYear;
				
			}
			
			
			String[] years=type.getYearRange().split(",");
			List<Long> yearIds = new ArrayList<Long>();
			for (String yId : years) {
				yearIds.add(new Long(yId));
			}
			for(Long id : yearIds)
			{
				
				try {
				AccountingYear accYear = new AccountingYear();
				accYear = accountYearDAO.findOne(id);
				
				/*String[] accYears = accYear.getYear_range().split("-");// 2017-2018 BECOMES TWO STRING AS 2017 AND 2018
				String currentAccountYr = accYears[0];*/
				
				YearEnding year = new YearEnding();
				year.setCompany(Client.getCompany());
				year.setAccountingYear(accYear);
				year.setIsMailSent(false);
				year.setIsApprovedForEditingAccYr(false);
				if(currentyear.equalsIgnoreCase(accYear.getYear_range()))
				{
					year.setYearEndingstatus(MyAbstractController.ACTIVE_ACCOUNT_YEAR);
				}
				else
				{
					year.setYearEndingstatus(MyAbstractController.DEACTIVE_ACCOUNT_YEAR);
					
				}
				yearService.add(year);
				}
			    catch(Exception e)
				{
			    	e.printStackTrace();	
				}
	
			}
			
			try {
				mailService.sendSubscriptionMail(Client);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return msg;
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IClientValidationChecklistStatusService#findAll()
	 */
	@Override
	public List<ClientValidationChecklistStatus> findAll() {
		return dao.findAll();
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IClientValidationChecklistStatusService#deleteChecklistStatus(java.lang.Long, java.lang.Long)
	 */
	@Override
	public String deleteChecklistStatus(Long cId, Long chId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IClientValidationChecklistStatusService#getManadatoryCheclist()
	 */
	@Override
	public List<ClientValidationChecklist> getManadatoryCheclist() {
		return dao.getManadatoryCheclist();
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public ClientValidationChecklistStatus isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
