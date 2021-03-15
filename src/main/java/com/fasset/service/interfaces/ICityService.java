/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.City;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ICityService extends IGenericService<City>{

	public String saveCity(City city);
	public List<City> findAll();
	public String deleteByIdValue(Long entityId);
	List<City> findAllListing();
}
