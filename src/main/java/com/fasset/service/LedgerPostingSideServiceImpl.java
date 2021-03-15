/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.ILedgerPostingSideDAO;
import com.fasset.entities.LedgerPostingSide;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ILedgerPostingSideService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class LedgerPostingSideServiceImpl implements ILedgerPostingSideService{

	@Autowired
	private ILedgerPostingSideDAO dao;
	
	@Override
	public void add(LedgerPostingSide entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#update(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void update(LedgerPostingSide entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#list()
	 */
	@Override
	public List<LedgerPostingSide> list() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.Long)
	 */
	@Override
	public LedgerPostingSide getById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.String)
	 */
	@Override
	public LedgerPostingSide getById(String id) throws MyWebException {
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
	public void remove(LedgerPostingSide entity) throws MyWebException {
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
	public void merge(LedgerPostingSide entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.ILedgerPostingSideService#findAll()
	 */
	@Override
	public List<LedgerPostingSide> findAll() {
		return dao.findAll();
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public LedgerPostingSide isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
