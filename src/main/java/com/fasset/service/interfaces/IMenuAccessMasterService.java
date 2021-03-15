package com.fasset.service.interfaces;

import java.util.List;
import java.util.Map;

import com.fasset.entities.MenuAccessMaster;
import com.fasset.entities.MenuMaster;
import com.fasset.entities.User;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IMenuAccessMasterService extends IGenericService<MenuAccessMaster>{
	
	String SaveMenuAccessMaster(MenuAccessMaster menuAccessMaster);
	Map<Long,String> getRoleList(Long loginId);
	//List<User> getnameList(Long id);	
	List<MenuAccessMaster> getAccess(Long id ,Long role_Id);
	List<MenuMaster> getMenuList(Long userId, Long roleId);
	List getDynamicMenuAccess(Long loginId, Long role_Id);
	MenuAccessMaster findmenuexist(Long loginId,int menuId, Long role_Id);
	List<MenuAccessMaster> findmenuacccess(Long loginId,int menuId, Long role_Id);
	List<User> getnameList(long id, long role, long company_id);

	
}
