package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.mobileApp.entity.LoginDetails;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IUserService extends IGenericService<User>{
	User loadUserByUsername(String userName,String password);
	User getUser(Long companyId, Long roleId);
	List<User> getUserByMail(String userName);
	User getUserByUserName(Long companyId, String userName);
	Integer authenticate(String userName, String password);
	LoginDetails authenticateLoginDetail(String userName,String password);
	String getRandPassword();
	void addEmployee(User user);
	void updateEmployee(User user);
	void updatebycomp(User entity) throws MyWebException;
	void sendChangedPass(User user);
	void changePassword(User user);
	void userDeactivate (User user);
	List<User> getUserByRole(Long roleId);
	List<User> getUserByRole(List<Long> roleIds);
	List<User> getUserByStatus(Integer statusId, Long roleId);
	List<User> getManagerAndExecutive();
	List<User> getUserByCompany(Long companyId);
	List<User> getActiveUserByCompany(Long company_id);
	List<User> findallemployeeAndAuditorOfAllCompanies();
	Integer getActiveUserByCompanyDashboard(long companyId);
	List<User> findallemployeeAndAuditorOfAllCompaniesexe(Long user_id);
	List<User> findAllExecutiveOfManager(Long user_id);
}