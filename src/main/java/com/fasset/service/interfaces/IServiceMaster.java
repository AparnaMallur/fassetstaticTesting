/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;


import com.fasset.entities.Service;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IServiceMaster extends IGenericService<Service>{

	public String saveService(Service service);
	public List<Service> findAll();
	public String deleteByIdValue(Long entityId);
	public List<Service> findAllListing();
}
