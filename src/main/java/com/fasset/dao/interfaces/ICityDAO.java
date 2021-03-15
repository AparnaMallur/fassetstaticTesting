/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.City;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ICityDAO extends IGenericDao<City>{

	Long saveCitydao(City city);
	public String deleteByIdValue(Long entityId);
	List<City> findAllListing();
}
