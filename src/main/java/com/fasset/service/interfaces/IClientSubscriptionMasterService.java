package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.form.ClientSubscriptionForm;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IClientSubscriptionMasterService extends IGenericService<ClientSubscriptionMaster>{
	List<ClientSubscriptionForm> getSubscriptionReport();
	public ClientSubscriptionMaster getClientSubscriptionByCompanyId(Long CompanyId);
	Long getquoteofcompany(Long company_id);
}
