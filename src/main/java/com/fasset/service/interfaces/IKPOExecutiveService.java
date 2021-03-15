/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.AccountGroup;
import com.fasset.entities.User;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IKPOExecutiveService extends IGenericService<User> {

	public String saveUser(User User);
	public List<User> findAll();
	public String deleteByIdValue(Long entityId);
	public List<User> findAllKPOExecutive();
	
	
	
}
