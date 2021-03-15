/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IClientValidationChecklistDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IindustryTypeDAO;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.Company;
import com.fasset.entities.SubLedger;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IClientValidationChecklistService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class ClientValidationChecklistServiceImpl implements IClientValidationChecklistService{

	@Autowired
	private IClientValidationChecklistDAO dao;
	
	/*@Autowired
	private ICompanyDAO companyDAO ;*/
	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#add(com.fasset.entities.abstracts.AbstractEntity)
	 */
	
	
	
	@Override
	public String saveClientValidationChecklist(ClientValidationChecklist validation) {
		
	/*	validation.setCreated_date(new LocalDateTime());
		List<String> compList = validation.getCompanyList();
		Set<Company> companyList = new HashSet<Company>();
		for(String id :compList)
		{
			Long cid = Long.parseLong(id);
			try {
				Company company = companyDAO.findOne(cid);
				companyList.add(company);
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		validation.setCompany(companyList);*/
		
		Long id= dao.saveClientValidationChecklistDao(validation);
		if(id!=null){
			return " Client Validation Checklist saved successfully";
		}
		else{
			return "Please try again ";
		}
	}
	@Override
	public void add(ClientValidationChecklist entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#update(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void update(ClientValidationChecklist entity) throws MyWebException {
		
		dao.update(entity);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#list()
	 */
	@Override
	public List<ClientValidationChecklist> list() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.Long)
	 */
	@Override
	public ClientValidationChecklist getById(Long id) throws MyWebException {
		return dao.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.String)
	 */
	@Override
	public ClientValidationChecklist getById(String id) throws MyWebException {
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
	public void remove(ClientValidationChecklist entity) throws MyWebException {
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
	public void merge(ClientValidationChecklist entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IClientValidationChecklistService#saveClientValidationChecklist(com.fasset.entities.ClientValidationChecklist)
	 */
	

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IClientValidationChecklistService#findAll()
	 */
	@Override
	public List<ClientValidationChecklist> findAll() {
		
		return dao.findAll();
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IClientValidationChecklistService#deleteCompany(java.lang.Long, java.lang.Long)
	 */
	@Override
	public String deleteCompany(Long chekId, Long comId) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public ClientValidationChecklist isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
