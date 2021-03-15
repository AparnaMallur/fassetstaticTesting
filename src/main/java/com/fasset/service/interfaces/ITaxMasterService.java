/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.Country;
import com.fasset.entities.TaxMaster;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ITaxMasterService extends IGenericService<TaxMaster>{
	
	public String saveTaxMaster(TaxMaster taxMaster);
	public List<TaxMaster> findAll();
	public String deleteByIdValue(Long entityId);
	List<TaxMaster> findAllListing();

}
