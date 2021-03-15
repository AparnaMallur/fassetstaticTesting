/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.AccountingYear;
import com.fasset.entities.Country;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IAccountingYearService extends IGenericService<AccountingYear>{
	

	public String saveAccountingYear(AccountingYear year);
	public List<AccountingYear> findAll();
	public List<AccountingYear> findAccountRange(Long userId,String yearId,Long Comapny_id);
	public String deleteByIdValue(Long entityId);
	public int findactiveyear(Long yid);
	public List<AccountingYear> findAccountRangeOfCompany(String yearId);
	List<AccountingYear> findAllListing();
	public Long findcurrentyear();
	public List<AccountingYear> findAccountRangeOfYearId(Long userId,Long yearId,Long Comapny_id);
	public AccountingYear findAccountRange(Long YearId);
}
