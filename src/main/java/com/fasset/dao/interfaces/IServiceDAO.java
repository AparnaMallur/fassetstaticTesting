/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Country;
import com.fasset.entities.Service;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IServiceDAO extends IGenericDao<Service>{

	Long saveServiceDao(Service service);
	public String deleteByIdValue(Long entityId);
	List<Service> findAllListing();
}
