/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.Country;
import com.fasset.entities.VoucherSeries;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IAccountingYearDAO extends IGenericDao<AccountingYear>{

	Long saveAccountingYearDao(AccountingYear year);
	public List<AccountingYear> findAccountRange(Long userId,String yearId,Long Comapny_id);
	public List<AccountingYear> findAccountRangeOfYearId(Long userId,Long yearId,Long Comapny_id);
	public String deleteByIdValue(Long entityId);
	public int findactiveyear(Long yid);
	public List<AccountingYear> findAccountRangeOfCompany(String yearId);
	public AccountingYear findAccountRange(Long YearId);
	List<AccountingYear> findAllListing();
	Long findcurrentyear();
	
	
	//public VoucherSeries findOneVoucherForAutoJV(String id) throws MyWebException;

}
