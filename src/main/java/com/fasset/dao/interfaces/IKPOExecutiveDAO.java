/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.Country;
import com.fasset.entities.User;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IKPOExecutiveDAO extends IGenericDao<User>{

	public List<User> findAllKPOExecutive();
	
}
