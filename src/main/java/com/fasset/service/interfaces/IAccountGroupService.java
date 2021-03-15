/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;
import java.util.Set;

import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.Company;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IAccountGroupService extends IGenericService<AccountGroup> {

	public String saveAccountGroup(AccountGroup group);
	public List<AccountGroup> findAll();
	public String deleteByIdValue(Long entityId);
	public List<AccountGroup> findByCompanyId (Long companyId);
	List<AccountGroup> findAllListing();
	public AccountGroup findOneWithAll(Long accId);
	List<AccountGroup> getGroupsWithSubGrouplist();
	public List<AccountGroup> findhorizontalAndVerticalBalanceSheetReport(Long comapany_id);
	public Set<AccountSubGroup> getSubgroupListForCashFlow(Long groupId);
	
	
}
