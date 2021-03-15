package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.MenuAccessMaster;
import com.fasset.entities.MenuMaster;

public interface IMenuMasterDAO extends  IGenericDao<MenuMaster> {

 	Long saveMenuMaster(MenuMaster menuMaster);
    public List<MenuMaster> findAll(Long id);
    public List<MenuMaster> getSubMenuList(Long parent_id);
	public String deleteByIdValue(Long entityId);
	List<MenuMaster> findAllbyrole(long roleId, long user_Id);	
}
