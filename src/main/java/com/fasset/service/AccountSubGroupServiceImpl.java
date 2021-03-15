/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IAccountSubGroupDAO;
import com.fasset.dao.interfaces.IAccountGroupDAO;
import com.fasset.entities.AccountSubGroup;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IAccountSubGroupService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class AccountSubGroupServiceImpl implements IAccountSubGroupService{

	@Autowired
	private IAccountSubGroupDAO accountSubGroupDAO;
	
	@Autowired
	private IAccountGroupDAO accountGroupDao;
	
	@Override
	public void add(AccountSubGroup entity) throws MyWebException {
		// TODO Auto-generated method stub		
	}

	@Override
	public void update(AccountSubGroup entity) throws MyWebException {		
		AccountSubGroup subgroup=accountSubGroupDAO.findOne(entity.getSubgroup_Id());
		subgroup.setUpdate_date(new LocalDateTime());
		subgroup.setAccountGroup(accountGroupDao.findOne(entity.getGroup_Id()));
		subgroup.setSubgroup_name(entity.getSubgroup_name());
		subgroup.setStatus(entity.getStatus());
		accountSubGroupDAO.update(subgroup);
	}

	@Override
	public List<AccountSubGroup> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountSubGroup getById(Long id) throws MyWebException {
		return accountSubGroupDAO.findOne(id);
	}

	@Override
	public AccountSubGroup getById(String id) throws MyWebException {
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
	public void remove(AccountSubGroup entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(AccountSubGroup entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String saveAccountSubGroup(AccountSubGroup group) {
		Long id = new Long(0);
		try {
			group.setCreated_date(new LocalDateTime());
			group.setAccountGroup(accountGroupDao.findOne(group.getGroup_Id()));
			id= accountSubGroupDAO.saveAccountSubGroupDao(group);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		
		if(id > 0){
			return " Account sub group saved successfully";
		}
		else{
			return "Please try again ";
		}
	}

	@Override
	public List<AccountSubGroup> findAll() {
		return accountSubGroupDAO.findAll();
	}
	@Override
	public List<AccountSubGroup> findSubgroup() {
		return accountSubGroupDAO.findSubgroup();
	}
	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = accountSubGroupDAO.deleteByIdValue(entityId);
		return msg;
	}

	@Override
	public AccountSubGroup isExists(String name) {
		
		return accountSubGroupDAO.isExists(name);
	}

	@Override
	public List<AccountSubGroup> findAllListing() {
		return accountSubGroupDAO.findAllListing();
	}

	@Override
	public AccountSubGroup findOneWithAll(Long accsId) {
	
		return accountSubGroupDAO.findOneWithAll(accsId);
	}

}
