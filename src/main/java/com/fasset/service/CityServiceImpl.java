/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.ICityDAO;
import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.entities.City;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ICityService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class CityServiceImpl implements ICityService{

	@Autowired
	private ICityDAO cityDAO ;
	
	@Autowired
	private ICountryDAO countryDao;
	
	@Autowired
	private IStateDAO stateDao;
	
	@Override
	public void add(City entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void update(City entity) throws MyWebException {
		City city = cityDAO.findOne(entity.getCity_id());
		city.setCity_name(entity.getCity_name());
		city.setCountry(countryDao.findOne(entity.getCountry_id()));
		city.setState(stateDao.findOne(entity.getState_id()));
		city.setStatus(entity.getStatus());
		city.setUpdate_date(new LocalDateTime());
		cityDAO.update(city);
	}

	@Override
	public List<City> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public City getById(Long id) throws MyWebException {
		return cityDAO.findOne(id);
	}

	@Override
	public City getById(String id) throws MyWebException {
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
	public void remove(City entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(City entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String saveCity(City city) {
		Long id= cityDAO.saveCitydao(city);
		if(id!=null){
			return " City saved successfully";
		}
		else{
			return "Please try again ";
		}
	}

	@Override
	public List<City> findAll() {
		return cityDAO.findAll();
	}

	@Override
	public List<City> findAllListing() {
		return cityDAO.findAllListing();
	}

	
	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = cityDAO.deleteByIdValue(entityId);
		return msg;
	}

	@Override
	public City isExists(String name) {
		// TODO Auto-generated method stub
		return cityDAO.isExists(name);
	}

	
}
