package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Role;
import com.fasset.entities.RoleMenuMaster;

public interface IRoleDAO extends IGenericDao<Role>{

	RoleMenuMaster getRole() ;
	List<Role> getRoleListById(List<Long> roleIds);
	
}