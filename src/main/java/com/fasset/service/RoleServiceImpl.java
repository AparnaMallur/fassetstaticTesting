package com.fasset.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IRoleDAO;
import com.fasset.entities.Role;
import com.fasset.entities.RoleMenuMaster;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IRoleService;

@Transactional
@Service
public class RoleServiceImpl implements IRoleService{
	
	@Autowired
	private IRoleDAO roleDao;

	@Override
	public void  add(Role entity)  throws MyWebException {
		roleDao.create(entity);
		
	}

	@Override
	public void update(Role entity) throws MyWebException {
		roleDao.update(entity);
		
	}

	@Override
	public List <Role> list() {
		return roleDao.findAll();
	}

	@Override
	public Role getById(Long id) throws MyWebException {
		return roleDao.findOne(id);
	}

	@Override
	public Role getById(String id) throws MyWebException {
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
	public void remove(Role entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Role entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RoleMenuMaster getRole() {
		return	roleDao.getRole();
		 
	}

	@Override
	public List<Role> getRoleListById(List<Long> roleIds) {
		return roleDao.getRoleListById(roleIds);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public Role isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}