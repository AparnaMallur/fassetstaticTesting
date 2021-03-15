/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;
import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.AccountSubGroup;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IAccountSubGroupDAO extends IGenericDao<AccountSubGroup>{
	
	Long saveAccountSubGroupDao(AccountSubGroup group);
	String deleteByIdValue(Long entityId);
	List<AccountSubGroup> findAllListing();
	List<AccountSubGroup> findSubgroup();
	List<AccountSubGroup> findSubGroupForProfitAndLossReport();
	AccountSubGroup findOneWithAll(Long accsId);
}
