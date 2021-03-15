/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IAccountingYearService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class AccountingYearServiceImpl implements IAccountingYearService{

	@Autowired
	private IAccountingYearDAO accountingYearDAO ;
	
	@Override
	public void add(AccountingYear entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#update(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void update(AccountingYear entity) throws MyWebException {
		
		accountingYearDAO.update(entity);
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#list()
	 */
	@Override
	public List<AccountingYear> list() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.Long)
	 */
	@Override
	public AccountingYear getById(Long id) throws MyWebException {
		return accountingYearDAO.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.String)
	 */
	@Override
	public AccountingYear getById(String id) throws MyWebException {
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
	public void remove(AccountingYear entity) throws MyWebException {
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
	public void merge(AccountingYear entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IAccountingYearService#saveAccountingYear(com.fasset.entities.AccountingYear)
	 */
	@Override
	public String saveAccountingYear(AccountingYear year) {
		Long id= accountingYearDAO.saveAccountingYearDao(year);
		if(id!=null){
			return "Accounting Year saved successfully";
		}
		else{
			return "Please try again";
		}
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IAccountingYearService#findAll()
	 */
	@Override
	public List<AccountingYear> findAll() {
		return accountingYearDAO.findAll();
	}
	
	public List<AccountingYear> findAllListing() {
		return accountingYearDAO.findAllListing();
	}

	
	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.IAccountingYearService#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = accountingYearDAO.deleteByIdValue(entityId);
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public AccountingYear isExists(String name) {
		// TODO Auto-generated method stub
		return accountingYearDAO.isExists(name);
	}

	@Override
	public List<AccountingYear> findAccountRange(Long userId, String yearId,Long Comapny_id) {
		// TODO Auto-generated method stub
		return accountingYearDAO.findAccountRange(userId,yearId,Comapny_id);

	}

	@Override
	public int findactiveyear(Long yid) {
		// TODO Auto-generated method stub
		return accountingYearDAO.findactiveyear(yid);
	}

	@Override
	public List<AccountingYear> findAccountRangeOfCompany(String yearId) {
		// TODO Auto-generated method stub
		return accountingYearDAO.findAccountRangeOfCompany(yearId);
	}

	@Override
	public Long findcurrentyear() {
		// TODO Auto-generated method stub
		return accountingYearDAO.findcurrentyear();
	}

	@Override
	public List<AccountingYear> findAccountRangeOfYearId(Long userId, Long yearId, Long Comapny_id) {
		return accountingYearDAO.findAccountRangeOfYearId(userId,yearId,Comapny_id);
	}

	@Override
	public AccountingYear findAccountRange(Long YearId) {
		// TODO Auto-generated method stub
		return accountingYearDAO.findAccountRange(YearId);
	}

}
