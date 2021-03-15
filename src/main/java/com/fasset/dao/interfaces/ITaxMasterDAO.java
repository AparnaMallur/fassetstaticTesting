/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Country;
import com.fasset.entities.TaxMaster;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ITaxMasterDAO extends IGenericDao<TaxMaster>{

	Long saveTaxMasterDao(TaxMaster taxMaster);
	public String deleteByIdValue(Long entityId);
	List<TaxMaster> findAllListing();
	
}
