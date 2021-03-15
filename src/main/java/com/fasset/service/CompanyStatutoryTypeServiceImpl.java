/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.ICompanyStatutoryTypeDAO;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ICompanyStatutoryTypeService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class CompanyStatutoryTypeServiceImpl  implements ICompanyStatutoryTypeService{
    
	@Autowired
	private ICompanyStatutoryTypeDAO typedao;
	
	
	@Override
	public void add(CompanyStatutoryType entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#update(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void update(CompanyStatutoryType entity) throws MyWebException {
		typedao.update(entity);
		
	}
	
	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#list()
	 */
	@Override
	public List<CompanyStatutoryType> list() {
		return typedao.findAll();
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.Long)
	 */
	@Override
	public CompanyStatutoryType getById(Long id) throws MyWebException {
		
		return typedao.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.String)
	 */
	@Override
	public CompanyStatutoryType getById(String id) throws MyWebException {
		
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
	public void remove(CompanyStatutoryType entity) throws MyWebException {
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
	public void merge(CompanyStatutoryType entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.ICompanyStatutoryTypeService#saveCompanyStatutoryType(com.fasset.entities.CompanyStatutoryType)
	 */
	@Override
	public String saveCompanyStatutoryType(CompanyStatutoryType type) {
		
		Long id= typedao.saveCompanyStatutoryTypeDao(type);
		if(id!=null){
			return " Company statutory type saved successfully";
		}
		else{
			return "Please try again ";
		}
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.ICompanyStatutoryTypeService#findAll()
	 */
	@Override
	public List<CompanyStatutoryType> findAll() {
		
		return typedao.findAll();
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.ICompanyStatutoryTypeService#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = typedao.deleteByIdValue(entityId);
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public CompanyStatutoryType isExists(String name) {
		
		return typedao.isExists(name);
	}

	@Override
	public List<CompanyStatutoryType> findAllactive() {
		// TODO Auto-generated method stub
		return typedao.findAllactive();
		}

	@Override
	public List<CompanyStatutoryType> findAllListing() {
		// TODO Auto-generated method stub
		return typedao.findAllListing();
	}	
}
