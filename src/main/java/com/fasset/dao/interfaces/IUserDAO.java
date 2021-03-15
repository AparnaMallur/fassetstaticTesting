package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.User;
import com.fasset.mobileApp.entity.LoginDetails;

public interface IUserDAO extends IGenericDao<User>{
	User loadUserByUsername(String userName,String password);
	User loadUserByUsername(String userName);
	List<User> getUserByMail(String userName);
	User getUser(Long companyId, Long roleId);
	User getUserByUserName(Long companyId, String userName);
	User isExistsemail(String email);
	Long saveUser(User user);
	Long authenticate(String userName, String password);
	LoginDetails authenticateLoginDetail(String userName,String password);
	void userDeactivate (User user);
	void updateKpoExAndManager(User user);
	List<User> getUserByRole(Long roleId);
	// List<User> getActiveUserByCompany(String userName,String password,Long companyId) ;
	List<User> getUserByRole(List<Long> roleIds);
	List<User> getUserByStatus(Integer statusId, Long roleId);
	List<User> getUserByCompany(Long companyId);
	List<User> getManagerAndExecutive();
	List<User> getActiveUserByCompany(Long companyId);
	List<User> findallemployeeAndAuditorOfAllCompanies();
	Integer getActiveUserByCompanyDashboard(long companyId);
	List<User> findallemployeeAndAuditorOfAllCompaniesexe(List<Long> companyIds);
	List<User> findAllExecutiveOfManager(Long user_id);
}