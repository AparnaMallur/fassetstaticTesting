/**
 * 
 */
package com.fasset.service;

import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasset.dao.interfaces.IForgotPasswordLogDAO;
import com.fasset.entities.ForgotPasswordLog;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IForgotPasswordLogService;

/**
 * @author "Vishwajeet"
 *
 */
@Service
@Transactional
public class ForgotPasswordLogServiceImpl implements IForgotPasswordLogService{

	@Autowired
	private IForgotPasswordLogDAO forgotPasswordLogDao; 
	
	@Override
	public void add(ForgotPasswordLog entity) throws MyWebException {
		forgotPasswordLogDao.create(entity);
		
	}
	
	@Override
	public void update(ForgotPasswordLog entity) throws MyWebException {
		forgotPasswordLogDao.update(entity);
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#list()
	 */
	@Override
	public List<ForgotPasswordLog> list() {
		return forgotPasswordLogDao.findAll();
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.Long)
	 */
	@Override
	public ForgotPasswordLog getById(Long id) throws MyWebException {		
		return forgotPasswordLogDao.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.String)
	 */
	@Override
	public ForgotPasswordLog getById(String id) throws MyWebException {
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
	public void remove(ForgotPasswordLog entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void merge(ForgotPasswordLog entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ForgotPasswordLog isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalDate getLastPasswordUpdateDate(Long userId) {
		ForgotPasswordLog forgotPasswordLog = forgotPasswordLogDao.getLastPasswordUpdateDate(userId);
		if(forgotPasswordLog != null){
			return forgotPasswordLog.getDate();
		}
		return null;
	}

}
