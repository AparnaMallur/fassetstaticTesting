/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IServiceDAO;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IServiceMaster;


/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class ServiceMasterImpl implements IServiceMaster{
    
	@Autowired
	private IServiceDAO serviceDAO ;

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#add(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void add(com.fasset.entities.Service entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#update(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void update(com.fasset.entities.Service entity) throws MyWebException {
		serviceDAO.update(entity);
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#list()
	 */
	@Override
	public List<com.fasset.entities.Service> list() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.Long)
	 */
	@Override
	public com.fasset.entities.Service getById(Long id) throws MyWebException {
		return serviceDAO.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.String)
	 */
	@Override
	public com.fasset.entities.Service getById(String id) throws MyWebException {
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
	public void remove(com.fasset.entities.Service entity) throws MyWebException {
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
	public void merge(com.fasset.entities.Service entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IServiceMaster#saveService(com.fasset.entities.Service)
	 */
	@Override
	public String saveService(com.fasset.entities.Service service) {
		Long id= serviceDAO.saveServiceDao(service);
		if(id!=null){
			return " Service name, Service charge saved successfully";
		}
		else{
			return "Please try again ";
		}
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IServiceMaster#findAll()
	 */
	@Override
	public List<com.fasset.entities.Service> findAll() {
		return serviceDAO.findAll();
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IServiceMaster#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = serviceDAO.deleteByIdValue(entityId);
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public com.fasset.entities.Service isExists(String name) {
		
		return serviceDAO.isExists(name);
	}

	@Override
	public List<com.fasset.entities.Service> findAllListing() {
		// TODO Auto-generated method stub
		return serviceDAO.findAllListing();
	}
	
	
	

}
