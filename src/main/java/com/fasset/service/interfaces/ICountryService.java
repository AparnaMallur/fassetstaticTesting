/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;
import com.fasset.entities.Country;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ICountryService extends IGenericService<Country>{

	public String saveCountry(Country country);
	public List<Country> findAll();
	public String deleteByIdValue(Long entityId);
	List<Country> findAllListing();
}
