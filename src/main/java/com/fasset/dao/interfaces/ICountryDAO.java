/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Country;



/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ICountryDAO extends IGenericDao<Country>{

	Long saveCountrydao(Country country);
	public String deleteByIdValue(Long entityId);
	List<Country> findAllListing();
}
