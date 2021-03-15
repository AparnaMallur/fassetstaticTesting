/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.State;
import com.fasset.entities.Stock;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IStateDAO extends IGenericDao<State>{

	Long saveStatedao(State state);
	public String deleteByIdValue(Long entityId);
	List<State> findAllListing();
	State loadStateCode(Long state_code);
	
	
}
