package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Company;
import com.fasset.entities.User;

public interface IClientAllocationToKpoExecutiveDAO extends IGenericDao<ClientAllocationToKpoExecutive> {
	Long saveClientAllocationToKpoExecutive(ClientAllocationToKpoExecutive clientAllocationToKpoExecutive);
	public String deleteByIdValue(Long entityId);
	public List<ClientAllocationToKpoExecutive> findCompanyByExecutiveId(Long user_id);
	public List<ClientAllocationToKpoExecutive> findAllCompaniesUnderExecutive(Long user_id);
	public int isCompanyAllocated(Long user_id,Long company_id);
	public List<User> findAllExcecutiveAndManagerOfCompany(Long company_id);
	public List<Company> findAllCompaniesUnderManager(Long user_id);
	
}
