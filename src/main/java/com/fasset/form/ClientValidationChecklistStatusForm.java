/**
 * mayur suramwar
 */
package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.ClientValidationChecklistStatus;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IClientValidationChecklistService;
import com.fasset.service.interfaces.IClientValidationChecklistStatusService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public class ClientValidationChecklistStatusForm {

	public Boolean statuschecklist(ClientValidationChecklistStatus status,IClientValidationChecklistStatusService  statusService,IClientValidationChecklistService checklistService)
	{
	
	Boolean statusValidator = true;
	
	List<ClientValidationChecklist> list = statusService.getManadatoryCheclist();
	List<ClientValidationChecklist> newlist = new ArrayList<ClientValidationChecklist>();
	
	if (status.getCheckList()!=null) 
     {
	List<String> checkList = status.getCheckList();
	for(String id :checkList)
	{
		Long cid = Long.parseLong(id);
		try {
			ClientValidationChecklist validation = checklistService.getById(cid);
			if(validation.getIs_mandatory()==true)
			{
			 newlist.add(validation);
			}
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	if(list.size()!=newlist.size())
	{
		statusValidator=false;
	}
   }
	else
	{
		statusValidator=false;
	}
	return statusValidator;
	}
}
