/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.AccountGroupType;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IAccountGroupTypeService extends IGenericService<AccountGroupType>{
	public List<AccountGroupType> findAll();
}
