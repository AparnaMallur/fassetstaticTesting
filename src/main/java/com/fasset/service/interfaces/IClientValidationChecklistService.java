/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.IndustryType;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IClientValidationChecklistService extends IGenericService<ClientValidationChecklist>{

	public String saveClientValidationChecklist(ClientValidationChecklist validation);
	public List<ClientValidationChecklist> findAll();
	public String deleteCompany(Long chekId, Long comId);
}
