package com.fasset.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IMenuAccessMasterDAO;
import com.fasset.dao.interfaces.IMenuMasterDAO;
import com.fasset.entities.MenuAccessMaster;
import com.fasset.entities.MenuMaster;
import com.fasset.entities.Role;
import com.fasset.entities.RoleMenuMaster;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IMenuAccessMasterService;

@Transactional
@Service
public class MenuAccessMasterServiceImpl implements IMenuAccessMasterService{

	@Autowired
	private IMenuAccessMasterDAO menuAccessMasterDAO;
	
	@Autowired
	private IMenuMasterDAO menuMasterDAO;
	
	@Override
	public void add(MenuAccessMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public List<MenuAccessMaster> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuAccessMaster getById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuAccessMaster getById(String id) throws MyWebException {
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
	public void remove(MenuAccessMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(MenuAccessMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(MenuAccessMaster menuAccessMaster) throws MyWebException {
		menuAccessMasterDAO.update(menuAccessMaster);		
	}
	@Override
	public String SaveMenuAccessMaster(MenuAccessMaster menuAccessMaster) {
		Long id=menuAccessMasterDAO.SaveMenuAccessMaster(menuAccessMaster);
		if(id>0)
		{
			return "Record Inserted Successfully";
		}
		else
		{
			return "Please Try Again";
		}
		
	}
	

	@Override
	public  Map<Long,String> getRoleList(Long loginId) {
		
		return menuAccessMasterDAO.getRoleList(loginId);
	}

	@Override
	public List<User> getnameList(long id,long role,long company_id) {
		
		return menuAccessMasterDAO.getnameList(id,role,company_id);
	}

	@Override
	public List<MenuAccessMaster> getAccess(Long id , Long role_Id) {
		
		return menuAccessMasterDAO.getAccess(id,role_Id);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public MenuAccessMaster isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuMaster> getMenuList(Long userId, Long roleId) {
		 List<MenuAccessMaster> menuAccessList = menuAccessMasterDAO.getAccess(userId, roleId);
		 List<MenuMaster> menuList = new ArrayList<MenuMaster>();
		 try {
			 for(MenuAccessMaster accessMaster:menuAccessList) {
				MenuMaster menu = menuMasterDAO.findOne(Long.parseLong(Integer.toString(accessMaster.getMenu_Id())));
				menuList.add(menu);			
			 }
		 } catch (Exception e) {
				e.printStackTrace();
			}
		return menuList;
	}

	public List getDynamicMenuAccess(Long loginId,Long role_Id) {
		
		return menuAccessMasterDAO.getDynamicMenuAccess(loginId, role_Id);
	}
	public MenuAccessMaster findmenuexist(Long uid,int mid,Long rid) {
		 return menuAccessMasterDAO.findmenuexist(uid,mid,rid);
	}



	@Override
	public List<MenuAccessMaster> findmenuacccess(Long loginId, int menuId, Long role_Id) {
		// TODO Auto-generated method stub
		 return menuAccessMasterDAO.findmenuacccess(loginId,menuId,role_Id);
	}

}
