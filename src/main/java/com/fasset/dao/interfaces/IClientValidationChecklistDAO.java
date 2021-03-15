/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.IndustryType;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IClientValidationChecklistDAO extends IGenericDao<ClientValidationChecklist>{

	Long saveClientValidationChecklistDao(ClientValidationChecklist validation);
	public String deleteCompany(Long chekId , Long comId);
}
