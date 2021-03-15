/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.entities.Country;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ICountryService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class CountryServiceImpl implements ICountryService{
	
	@Autowired
	private ICountryDAO countryDao;
	
	
	@Override
	public void add(Country entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#update(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void update(Country entity) throws MyWebException {
		countryDao.update(entity);
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#list()
	 */
	@Override
	public List<Country> list() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.Long)
	 */
	@Override
	public Country getById(Long id) throws MyWebException {
		
		return countryDao.findOne(id);
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.String)
	 */
	@Override
	public Country getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#removeById(java.lang.Long)
	 */
	@Override
	public void removeById(Long id) throws MyWebException {
		
		
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
	public void remove(Country entity) throws MyWebException {
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
	public void merge(Country entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.ICountryService#saveCountry(com.fasset.entities.Country)
	 */
	@Override
	public String saveCountry(Country country) {
		Long id= countryDao.saveCountrydao(country);
		if(id!=null){
			return " Country saved successfully";
		}
		else{
			return "Please try again ";
		}
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.ICountryService#findAll()
	 */
	@Override
	public List<Country> findAll() {
		
		return countryDao.findAll();
	}
	@Override
	public List<Country> findAllListing() {
		
		return countryDao.findAllListing();
	}


	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.ICountryService#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = countryDao.deleteByIdValue(entityId);
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public Country isExists(String name) {
		
		return countryDao.isExists(name);
	}

	
	
}
