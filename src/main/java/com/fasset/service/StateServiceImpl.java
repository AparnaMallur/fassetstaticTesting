/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.entities.Country;
import com.fasset.entities.State;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IStateService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class StateServiceImpl implements IStateService{
    
	
	
	@Autowired
	private IStateDAO stateDAO ;
	
	   
		@Autowired
		private ICountryDAO countryDAO ;

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#add(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void add(State entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#update(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void update(State entity) throws MyWebException {
		State state = stateDAO.findOne(entity.getState_id());
		state.setState_name(entity.getState_name());
		state.setCntry(countryDAO.findOne(entity.getCountry_id()));
		state.setUpdate_date(new LocalDateTime());
		state.setStatus(entity.getStatus());
		state.setState_code(entity.getState_code());
		stateDAO.update(state);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#list()
	 */
	@Override
	public List<State> list() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.Long)
	 */
	@Override
	public State getById(Long id) throws MyWebException {
		return stateDAO.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.String)
	 */
	@Override
	public State getById(String id) throws MyWebException {
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
	public void remove(State entity) throws MyWebException {
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
	public void merge(State entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IStateService#saveState(com.fasset.entities.State)
	 */
	@Override
	public String saveState(State state) {
		
		Long id= stateDAO.saveStatedao(state);
		if(id!=null){
			return " State saved successfully";
		}
		else{
			return "Please try again ";
		}
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IStateService#findAll()
	 */
	@Override
	public List<State> findAll() {
		return stateDAO.findAll();
	}
	
	@Override
	public List<State> findAllListing() {
		return stateDAO.findAllListing();
	}


	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IStateService#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = stateDAO.deleteByIdValue(entityId);
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public State isExists(String name) {
		// TODO Auto-generated method stub
		return stateDAO.isExists(name);
	}

	@Override
	public State loadStateCode(Long state_code) {
		return stateDAO.loadStateCode(state_code);
	}
	
}
