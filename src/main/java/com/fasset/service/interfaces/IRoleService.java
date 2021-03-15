package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.Role;
import com.fasset.entities.RoleMenuMaster;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IRoleService extends IGenericService<Role>{
	
	RoleMenuMaster getRole() ;
	List<Role> getRoleListById(List<Long> roleIds);	

}