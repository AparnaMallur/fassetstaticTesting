/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.Ledger;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IAccountSubGroupService extends IGenericService<AccountSubGroup> {

	public String saveAccountSubGroup(AccountSubGroup group);
	public List<AccountSubGroup> findAll();
	public String deleteByIdValue(Long entityId);
	public List<AccountSubGroup> findAllListing();
	List<AccountSubGroup> findSubgroup();
	public AccountSubGroup findOneWithAll(Long accsId);
}
