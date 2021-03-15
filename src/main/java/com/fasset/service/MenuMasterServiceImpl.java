package com.fasset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IMenuMasterDAO;

import com.fasset.entities.MenuMaster;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IMenuMasterService;


@Service
@Transactional
public class MenuMasterServiceImpl implements IMenuMasterService {

	@Autowired
	private IMenuMasterDAO menuMasterDAO;
	
	@Override
	public void add(MenuMaster entity) throws MyWebException {
	}

	@Override
	public void update(MenuMaster entity) throws MyWebException {
		menuMasterDAO.update(entity);
		
	}

	@Override
	public List<MenuMaster> list() {
		
		return null;
	}

	@Override
	public MenuMaster getById(Long id) throws MyWebException {
		return menuMasterDAO.findOne(id);
	}

	@Override
	public MenuMaster getById(String id) throws MyWebException {
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
	public void remove(MenuMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(MenuMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String saveMenuMaster(MenuMaster menuMaster) {
		Long id=menuMasterDAO.saveMenuMaster(menuMaster);
		
		if(id != null)
		{
			return " Menu saved successfully";
		}
		else
		{
			return "Please try again ";
		}
		
	}

	public List<MenuMaster> findAll(Long id) {
		return menuMasterDAO.findAll(id);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public MenuMaster isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuMaster> getSubMenuList(Long parent_id) {
		
		return menuMasterDAO.getSubMenuList(parent_id);
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		return menuMasterDAO.deleteByIdValue(entityId);
	}

	@Override
	public List<MenuMaster> findAllbyrole(long roleId, long user_Id) {
		return menuMasterDAO.findAllbyrole(roleId,user_Id);

	}

	

	

}
