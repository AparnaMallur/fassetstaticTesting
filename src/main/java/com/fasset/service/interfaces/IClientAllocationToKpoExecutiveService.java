package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Company;
import com.fasset.entities.User;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IClientAllocationToKpoExecutiveService extends IGenericService<ClientAllocationToKpoExecutive> {
	String saveClientAllocationToKpoExecutive(ClientAllocationToKpoExecutive clientAllocationToKpoExecutive);
	public List<ClientAllocationToKpoExecutive> findAll();
	public String deleteByIdValue(Long entityId);
	public List<Company> findCompanyByExecutiveId(Long user_id);
	public List<ClientAllocationToKpoExecutive> findAllCompaniesUnderExecutive(Long user_id);
	public String updateClientAllocation(ClientAllocationToKpoExecutive entity);
	
	public List<Company> findAllCompaniesUnderManager(Long user_id);
	public List<User> findAllExcecutiveAndManagerOfCompany(Long company_id);
	
}
