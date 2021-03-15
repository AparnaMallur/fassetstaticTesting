/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;
import java.util.Set;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IAccountGroupDAO;
import com.fasset.dao.interfaces.IAccountGroupTypeDAO;
import com.fasset.dao.interfaces.ILedgerPostingSideDAO;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountSubGroup;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IAccountGroupService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class AccountGroupServiceImpl implements IAccountGroupService{
    
	@Autowired
	private IAccountGroupDAO accountGroupDAO ;
	
	@Autowired
	private IAccountGroupTypeDAO groupType;
	
	@Autowired
	private ILedgerPostingSideDAO postSide;
	
	@Override
	public void add(AccountGroup entity) throws MyWebException {
		// TODO Auto-generated method stub		
	}

	@Override
	public void update(AccountGroup group) throws MyWebException {
		AccountGroup accountGroup = accountGroupDAO.findOne(group.getGroup_Id());
		accountGroup.setGroup_name(group.getGroup_name());
		accountGroup.setGrouptype(groupType.findOne(group.getAccount_group_id()));
		accountGroup.setPostingSide(postSide.findOne(group.getPosting_id()));
		accountGroup.setStatus(group.getStatus());
		accountGroup.setUpdate_date(new LocalDateTime());
		accountGroupDAO.update(accountGroup);		
	}

	@Override
	public List<AccountGroup> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountGroup getById(Long id) throws MyWebException {
		return accountGroupDAO.findOne(id);
	}

	@Override
	public AccountGroup getById(String id) throws MyWebException {
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
	public void remove(AccountGroup entity) throws MyWebException {
		// TODO Auto-generated method stub		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(AccountGroup entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String saveAccountGroup(AccountGroup group) {
		Long id = new Long(0);
		try {
			group.setCreated_date(new LocalDateTime());
			group.setGrouptype(groupType.findOne(group.getAccount_group_id()));
			group.setPostingSide(postSide.findOne(group.getPosting_id()));
			id= accountGroupDAO.saveAccountGroupDao(group);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		if(id > 0){
			return " Account group saved successfully";
		}
		else{
			return "Please try again ";
		}
	}

	@Override
	public List<AccountGroup> findAll() {
		return accountGroupDAO.findAll();
	}

	@Override
	public List<AccountGroup> findAllListing() {
		return accountGroupDAO.findAllListing();
	}	

	@Override
	public String deleteByIdValue(Long entityId) {
		return accountGroupDAO.deleteByIdValue(entityId);
	}

	@Override
	public AccountGroup isExists(String name) {		
		return accountGroupDAO.isExists(name);
	}

	@Override
	public List<AccountGroup> findByCompanyId(Long companyId) {
		return accountGroupDAO.findByCompanyId(companyId);
	}

	@Override
	public AccountGroup findOneWithAll(Long accId) {
		
		return accountGroupDAO.findOneWithAll(accId);
	}

	@Override
	public List<AccountGroup> getGroupsWithSubGrouplist() {
		return accountGroupDAO.getGroupsWithSubGrouplist();
	}

	@Override
	public List<AccountGroup> findhorizontalAndVerticalBalanceSheetReport(Long comapany_id) {		
		return accountGroupDAO.findhorizontalAndVerticalBalanceSheetReport(comapany_id);
	}

	@Override
	public Set<AccountSubGroup> getSubgroupListForCashFlow(Long groupId) {
		return accountGroupDAO.getSubgroupListForCashFlow(groupId);
	}

	
}
