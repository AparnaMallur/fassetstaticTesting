package com.fasset.dao.interfaces;

import java.util.List;
import java.util.Map;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.MenuAccessMaster;
import com.fasset.entities.User;

public interface IMenuAccessMasterDAO extends IGenericDao<MenuAccessMaster>{
	public Long SaveMenuAccessMaster(MenuAccessMaster menuAccessMaster);
	public Map<Long,String> getRoleList(Long loginId);
	//public List<User> getnameList(Long id);
	public List<MenuAccessMaster> getAccess(Long id ,Long role_Id);
	List getDynamicMenuAccess(Long loginId, Long role_Id);
	MenuAccessMaster findmenuexist(Long loginId,int menuId, Long role_Id);
	List<MenuAccessMaster> findmenuacccess(Long loginId,int menuId, Long role_Id);
	public List<User> getnameList(long id, long role, long company_id);
}
