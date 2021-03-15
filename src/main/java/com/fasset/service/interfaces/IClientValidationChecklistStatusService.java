/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.ClientValidationChecklistStatus;
import com.fasset.entities.IndustryType;
import com.fasset.entities.User;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IClientValidationChecklistStatusService extends IGenericService<ClientValidationChecklistStatus>{

	public String saveClientValidationChecklistStatus(ClientValidationChecklistStatus type,User Client);
	public List<ClientValidationChecklistStatus> findAll();
	public String deleteChecklistStatus(Long cId, Long chId);
	public List<ClientValidationChecklist> getManadatoryCheclist();
	
}
