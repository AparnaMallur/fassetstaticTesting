package com.fasset.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IClientAllocationToKpoExecutiveDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Company;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IClientAllocationToKpoExecutiveService;

@Transactional
@Service
public class ClientAllocationToKpoExecutiveServiceImpl implements IClientAllocationToKpoExecutiveService{
	
	@Autowired
	IClientAllocationToKpoExecutiveDAO clientAllocationToKpoExectiveDAO;
	
	@Autowired
	IUserDAO userDAO;
	
	@Autowired
	ICompanyDAO companyDAO;

	@Override
	public void add(ClientAllocationToKpoExecutive entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ClientAllocationToKpoExecutive entity) throws MyWebException {
		
	}

	@Override
	public List<ClientAllocationToKpoExecutive> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientAllocationToKpoExecutive getById(Long id) throws MyWebException {
		return clientAllocationToKpoExectiveDAO.findOne(id);
	}

	@Override
	public ClientAllocationToKpoExecutive getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(ClientAllocationToKpoExecutive entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(ClientAllocationToKpoExecutive entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ClientAllocationToKpoExecutive isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveClientAllocationToKpoExecutive(ClientAllocationToKpoExecutive clientAllocationToKpoExecutive) {
		String msg = "";
		User user=null;
		try {
			user = userDAO.findOne(clientAllocationToKpoExecutive.getUser_id());
		} catch (MyWebException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(user!=null)
		{
			if(user.getRole().getRole_id().equals(MyAbstractController.ROLE_MANAGER))
			{
				int count = clientAllocationToKpoExectiveDAO.isCompanyAllocated(clientAllocationToKpoExecutive.getUser_id(), clientAllocationToKpoExecutive.getCompany_id());
				
				if(count==0)
				{
				try {
					clientAllocationToKpoExecutive.setUser(user);
					clientAllocationToKpoExecutive.setCompany(companyDAO.findOne(clientAllocationToKpoExecutive.getCompany_id()));
					long id = clientAllocationToKpoExectiveDAO.saveClientAllocationToKpoExecutive(clientAllocationToKpoExecutive);
					if (id > 0) {
						msg = "Allocated succesfully";
					} 
				} catch (Exception e) {
				e.printStackTrace();
			      }
				}
				else
				{
					msg = "The company is already allocated to the user.";
				}
				
			}
			else if(user.getRole().getRole_id().equals(MyAbstractController.ROLE_EXECUTIVE)) 
			{
				int count = clientAllocationToKpoExectiveDAO.isCompanyAllocated(clientAllocationToKpoExecutive.getUser_id(), clientAllocationToKpoExecutive.getCompany_id());
				
				if(count==0)
				{
				try {
					
					
					int count1 = clientAllocationToKpoExectiveDAO.isCompanyAllocated(user.getManager_id().getUser_id(), clientAllocationToKpoExecutive.getCompany_id());
					
					
					if(count1==0)
					{
				ClientAllocationToKpoExecutive client = new ClientAllocationToKpoExecutive();
				client.setCreated_by(clientAllocationToKpoExecutive.getCreated_by());
				client.setIp_address(clientAllocationToKpoExecutive.getIp_address());
				clientAllocationToKpoExecutive.setUser(user.getManager_id());
				clientAllocationToKpoExecutive.setCompany(companyDAO.findOne(clientAllocationToKpoExecutive.getCompany_id()));
		        clientAllocationToKpoExectiveDAO.saveClientAllocationToKpoExecutive(clientAllocationToKpoExecutive);
				
					}
				
					clientAllocationToKpoExecutive.setUser(user);
					clientAllocationToKpoExecutive.setCompany(companyDAO.findOne(clientAllocationToKpoExecutive.getCompany_id()));
					long id = clientAllocationToKpoExectiveDAO.saveClientAllocationToKpoExecutive(clientAllocationToKpoExecutive);
					if (id > 0) {
						msg = "Allocated succesfully";
					} else {
						msg = "Getting problem while allocating";
					}
				} catch (Exception e) {
				e.printStackTrace();
			      }
				}
				else
				{
					msg = "The company is already allocated to the user.";
				}
			}
			
		}
		
		return msg;
	}

	@Override
	public List<ClientAllocationToKpoExecutive> findAll() {
		return clientAllocationToKpoExectiveDAO.findAll();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = clientAllocationToKpoExectiveDAO.deleteByIdValue(entityId);
		return msg;
	}

	@Override
	public List<Company> findCompanyByExecutiveId(Long user_id) {
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(user_id);
		List<Company> companyList = new ArrayList<Company>();
		if(executiveList != null){
			for(ClientAllocationToKpoExecutive com : executiveList ){
				companyList.add(com.getCompany());
			}
		}
		
		return companyList;
	}

	@Override
	public List<ClientAllocationToKpoExecutive> findAllCompaniesUnderExecutive(Long user_id) {
		// TODO Auto-generated method stub
		return clientAllocationToKpoExectiveDAO.findAllCompaniesUnderExecutive(user_id);
	}

	@Override
	public String updateClientAllocation(ClientAllocationToKpoExecutive clientAllocationToKpoExecutive) {
		
		User user=null;
		try {
			user = userDAO.findOne(clientAllocationToKpoExecutive.getUser_id());
		} catch (MyWebException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String msg=null;
		if(user.getRole().getRole_id().equals(MyAbstractController.ROLE_MANAGER))
		{
			int count = clientAllocationToKpoExectiveDAO.isCompanyAllocated(clientAllocationToKpoExecutive.getUser_id(), clientAllocationToKpoExecutive.getCompany_id());
			
			if(count==0)
			{
			try {
				ClientAllocationToKpoExecutive clientAllocationToKpoExecutive1 = clientAllocationToKpoExectiveDAO.findOne(clientAllocationToKpoExecutive.getAllocation_Id());
				
				clientAllocationToKpoExecutive1.setUser(userDAO.findOne(clientAllocationToKpoExecutive.getUser_id()));
				clientAllocationToKpoExecutive1.setCompany(companyDAO.findOne(clientAllocationToKpoExecutive.getCompany_id()));
				clientAllocationToKpoExecutive1.setUpdated_by(clientAllocationToKpoExecutive.getUpdated_by());
				
					clientAllocationToKpoExectiveDAO.update(clientAllocationToKpoExecutive1);
					msg =  "Allocation updated successfully";
			} catch (Exception e) {
			e.printStackTrace();
		      }
			}
			else
			{
				msg = "The company is already allocated to the user.";
			}
			
		}
		else if(user.getRole().getRole_id().equals(MyAbstractController.ROLE_EXECUTIVE)) 
		{
			int count = clientAllocationToKpoExectiveDAO.isCompanyAllocated(clientAllocationToKpoExecutive.getUser_id(), clientAllocationToKpoExecutive.getCompany_id());
			
			if(count==0)
			{
			try {
				int count1 = clientAllocationToKpoExectiveDAO.isCompanyAllocated(user.getManager_id().getUser_id(), clientAllocationToKpoExecutive.getCompany_id());
				
				
				if(count1==0)
				{
			ClientAllocationToKpoExecutive client = new ClientAllocationToKpoExecutive();
			client.setCreated_by(clientAllocationToKpoExecutive.getCreated_by());
			client.setIp_address(clientAllocationToKpoExecutive.getIp_address());
			clientAllocationToKpoExecutive.setUser(user.getManager_id());
			clientAllocationToKpoExecutive.setCompany(companyDAO.findOne(clientAllocationToKpoExecutive.getCompany_id()));
	        clientAllocationToKpoExectiveDAO.saveClientAllocationToKpoExecutive(clientAllocationToKpoExecutive);
			
				}
			
                ClientAllocationToKpoExecutive clientAllocationToKpoExecutive1 = clientAllocationToKpoExectiveDAO.findOne(clientAllocationToKpoExecutive.getAllocation_Id());
				
				clientAllocationToKpoExecutive1.setUser(userDAO.findOne(clientAllocationToKpoExecutive.getUser_id()));
				clientAllocationToKpoExecutive1.setCompany(companyDAO.findOne(clientAllocationToKpoExecutive.getCompany_id()));
				clientAllocationToKpoExecutive1.setUpdated_by(clientAllocationToKpoExecutive.getUpdated_by());
				
					clientAllocationToKpoExectiveDAO.update(clientAllocationToKpoExecutive1);
					msg =  "Allocation updated successfully";
			} catch (Exception e) {
			e.printStackTrace();
		      }
			}
			else
			{
				msg = "The company is already allocated to the user.";
			}
		}
		return msg ;
}

	@Override
	public List<Company> findAllCompaniesUnderManager(Long user_id) {
		// TODO Auto-generated method stub
		return clientAllocationToKpoExectiveDAO.findAllCompaniesUnderManager(user_id);
	}

	@Override
	public List<User> findAllExcecutiveAndManagerOfCompany(Long company_id) {
		// TODO Auto-generated method stub
		return clientAllocationToKpoExectiveDAO.findAllExcecutiveAndManagerOfCompany(company_id);
	}
}
