/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;


import java.util.List;
import com.fasset.entities.State;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IStateService extends IGenericService<State> {

	public String saveState(State state);
	public List<State> findAll();
	public String deleteByIdValue(Long entityId);
	List<State> findAllListing();
	State loadStateCode(Long state_code);
	
	
}
