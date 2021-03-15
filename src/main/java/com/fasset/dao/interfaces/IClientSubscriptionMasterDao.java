package com.fasset.dao.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Company;

public interface IClientSubscriptionMasterDao extends IGenericDao<ClientSubscriptionMaster> {
	public ClientSubscriptionMaster getClientSubscriptionByCompanyId(Long CompanyId);
	Long getquoteofcompany(Long company_id);
	List<ClientSubscriptionMaster> getSubscriptionReport();
	public ClientSubscriptionMaster getClientSubscriptionByQuotationId(Long quotationId);
}
