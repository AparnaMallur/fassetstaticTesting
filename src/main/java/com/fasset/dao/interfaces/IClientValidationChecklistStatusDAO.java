/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;
import java.util.Set;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.ClientValidationChecklistStatus;
import com.fasset.entities.IndustryType;
import com.fasset.entities.User;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IClientValidationChecklistStatusDAO extends IGenericDao<ClientValidationChecklistStatus>{

	public String saveClientValidationChecklistStatus(ClientValidationChecklistStatus status,User Client);
	public String deleteChecklistStatus(Long cId, Long chId);
	public List<ClientValidationChecklist> getManadatoryCheclist();
}
