package com.fasset.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasset.dao.interfaces.IUnitOfMeasurementDAO;
import com.fasset.entities.UnitOfMeasurement;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IUOMService;

@Transactional
@Service
public class UOMServiceImpl implements IUOMService{
	
	@Autowired
	private IUnitOfMeasurementDAO uomDao;

	@Override
	public void add(UnitOfMeasurement entity) throws MyWebException {
		uomDao.create(entity);
	}

	@Override
	public void update(UnitOfMeasurement entity) throws MyWebException {
		uomDao.update(entity);
	}

	@Override
	public List<UnitOfMeasurement> list() {
		return uomDao.findAll();
	}

	@Override
	public UnitOfMeasurement getById(Long id) throws MyWebException {
		return uomDao.findOne(id);
	}

	@Override
	public UnitOfMeasurement getById(String id) throws MyWebException {
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
	public void remove(UnitOfMeasurement entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(UnitOfMeasurement entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public UnitOfMeasurement isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		return uomDao.deleteByIdValue(entityId);
	}

	@Override
	public List<UnitOfMeasurement> findAllListing() {
		// TODO Auto-generated method stub
		return uomDao.findAllListing();
	}

}
