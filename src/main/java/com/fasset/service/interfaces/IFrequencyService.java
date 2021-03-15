/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;
import com.fasset.entities.ServiceFrequency;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IFrequencyService extends IGenericService<ServiceFrequency> {
	
	public List<ServiceFrequency> findAll();
}
