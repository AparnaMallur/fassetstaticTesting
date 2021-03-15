package com.fasset.service.interfaces;

import com.fasset.entities.MenuMaster;
import com.fasset.service.interfaces.generic.IGenericService;
import java.util.*;
public interface IMenuMasterService extends IGenericService<MenuMaster>{
	public String saveMenuMaster(MenuMaster menuMaster);
	public List<MenuMaster> findAll(Long id);
	public List<MenuMaster> getSubMenuList(Long parent_id);
	public String deleteByIdValue(Long entityId);
	public List<MenuMaster> findAllbyrole(long roleId, long user_Id);
}
