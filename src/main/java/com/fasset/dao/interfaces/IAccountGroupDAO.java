/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;
import java.util.Set;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountSubGroup;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IAccountGroupDAO extends IGenericDao<AccountGroup>{

	Long saveAccountGroupDao(AccountGroup group);
	public String deleteByIdValue(Long entityId);
	List<AccountGroup> findByCompanyId (Long companyId);
	List<AccountGroup> findAllListing();
	public AccountGroup findOneWithAll(Long accId);
	List<AccountGroup> getGroupsWithSubGrouplist();
	public List<AccountGroup> findhorizontalAndVerticalBalanceSheetReport(Long comapany_id);
	public Set<AccountSubGroup> getSubgroupListForCashFlow(Long groupId);
	
}
