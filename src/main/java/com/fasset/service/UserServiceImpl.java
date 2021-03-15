package com.fasset.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ccavenue.security.Md5;
import com.fasset.dao.interfaces.ICityDAO;
import com.fasset.dao.interfaces.IClientAllocationToKpoExecutiveDAO;
import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.JavaMD5Hash;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.mobileApp.entity.LoginDetails;
import com.fasset.service.interfaces.IMailService;
import com.fasset.service.interfaces.IUserService;


@Service
@Transactional
public class UserServiceImpl implements IUserService {
	
	@Autowired
	IClientAllocationToKpoExecutiveDAO clientAllocationToKpoExectiveDAO;
	
	@Autowired
	private IUserDAO userDao;

	@Autowired
	private IMailService mailService;

	@Autowired
	private ICityDAO cityDao;
	
	@Autowired
	private IStateDAO stateDao;
	
	@Autowired
	private ICountryDAO countryDao;
	
	@Override
	public void add(User entity) throws MyWebException {
		userDao.create(entity);
	}

	@Override
	public void update(User entity) throws MyWebException {
		User user = userDao.findOne(entity.getUser_id());		
		user.setFirst_name(entity.getFirst_name());
		user.setMiddle_name(entity.getMiddle_name());
		user.setLast_name(entity.getLast_name());
		user.setMobile_no(entity.getMobile_no());
		user.setAdhaar_no(entity.getAdhaar_no());
		user.setPan_no(entity.getPan_no());
		//user.setEmail(entity.getEmail());
		user.setAdhaar_no(entity.getAdhaar_no());
		user.setIs_updated(true);
		user.setPan_no(entity.getPan_no());
		user.setUpdated_date(new LocalDate());
		
		if(entity.getCountry_id() > 0 ){
			user.setCountry(countryDao.findOne(entity.getCountry_id()));
		}
		if(entity.getState_id() > 0){
			user.setState(stateDao.findOne(entity.getState_id()));
		}
		if(entity.getCity_id() > 0){
			user.setCity(cityDao.findOne(entity.getCity_id()));
		}		
	   /*	if(entity.getManager() > 0) {
			user.setManager_id(userDao.findOne(entity.getManager()));
		} */
		
		userDao.update(user);		
	}
	@Override
	public void updatebycomp(User entity) throws MyWebException {
		User user = userDao.findOne(entity.getUser_id());		
		user.setFirst_name(entity.getFirst_name());
		user.setMiddle_name(entity.getMiddle_name());
		user.setLast_name(entity.getLast_name());
		user.setMobile_no(entity.getMobile_no());
		user.setAdhaar_no(entity.getAdhaar_no());
		user.setPan_no(entity.getPan_no());
		user.setAdhaar_no(entity.getAdhaar_no());
		user.setPan_no(entity.getPan_no());
		user.setUpdated_date(new LocalDate());
		user.setAmount(entity.getAmount());
		user.setStatus(entity.getStatus());
		if(entity.getCountry_id() > 0 ){
			user.setCountry(countryDao.findOne(entity.getCountry_id()));
		}
		if(entity.getState_id() > 0){
			user.setState(stateDao.findOne(entity.getState_id()));
		}
		if(entity.getCity_id() > 0){
			user.setCity(cityDao.findOne(entity.getCity_id()));
		}				
		userDao.update(user);		
	}

	@Override
	public List<User> list() {
		return userDao.findAll();
	}

	@Override
	public User getById(Long id) throws MyWebException {
		return userDao.findOne(id);
	}

	@Override
	public User getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeById(Long id) throws MyWebException {
		User user = userDao.findOne(id);
		userDao.delete(user);
	}

	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub

	}
	@Override
	public LoginDetails authenticateLoginDetail(String userName, String password) {
		return userDao.authenticateLoginDetail(userName,password);
	}
	@Override
	public void remove(User entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(User entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public User loadUserByUsername(String userName,String password) {
		if(password.trim().isEmpty()){
			return userDao.loadUserByUsername(userName);
		}else{
		return userDao.loadUserByUsername(userName,password);}
	}

	@Override
	public Integer authenticate(String userName, String password) {
		
		Long userId = userDao.authenticate(userName, Md5.md5(password));
		
		if (userId == null) {
			
			User user=userDao.isExistsemail(userName);
			if(user==null){
				return 0;//User is not registered with fasset
			}
			else{
				return -1;//password is incorrect
			}
		}
		else if(userId > 0) {
			
			return 1;
		}
		else
			return Integer.parseInt(userId.toString());
	}

	@Override
	public void sendChangedPass(User user) {
		String pass = getRandPassword();
		user.setEmail_varification_code(pass);
		try {
			userDao.update(user);
			mailService.sendChangedPassMail(user);
		}catch (MyWebException e) {
			e.printStackTrace();
		}catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	public static int getRandInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	@Override
	public String getRandPassword() {
		char[] charArray = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		int[] intArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String pass = "Fasset";
		for (int i = 1; i < 9; i++) {
			int j = i;
			if (j % 2 == 0) {
				int rand = getRandInt(0, 25);
				pass += charArray[rand];
			} else {
				int rand = getRandInt(0, 9);
				pass += intArray[rand];
			}
		}

		return pass;
	}

	@Override
	public List<User> getUserByRole(Long roleId) {
		return userDao.getUserByRole(roleId);
	}

	@Override
	public List<User> getUserByStatus(Integer statusId, Long roleId) {
		return userDao.getUserByStatus(statusId, roleId);
	}
	
	@Override
	public User isExists(String mobileNo) {
		return userDao.isExists(mobileNo);
	}

	@Override
	public void changePassword(User user) {
		try {
			userDao.update(user);
			mailService.sendChangedPassSuccessMail(user);
		} catch (MyWebException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void userDeactivate(User user) {
		userDao.userDeactivate(user);
	}

	@Override
	public List<User> getUserByCompany(Long companyId) {
		return userDao.getUserByCompany(companyId);
	}


	@Override
	public List<User> findallemployeeAndAuditorOfAllCompaniesexe(Long user_id) {
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(user_id);
		List<Long> companyIds = new ArrayList<Long>();
		if(executiveList .isEmpty()){
			companyIds.add((long) 0);
		}
		else
		{	
			for(ClientAllocationToKpoExecutive com : executiveList ){
				companyIds.add(com.getCompany().getCompany_id());
			}
		}
		return userDao.findallemployeeAndAuditorOfAllCompaniesexe(companyIds);

	}
	@Override
	public User getUser(Long companyId, Long roleId) {
		return userDao.getUser(companyId, roleId);
	}

	@Override
	public List<User> getManagerAndExecutive() {
		return userDao.getManagerAndExecutive();
	}

	@Override
	public void addEmployee(User user) {
		try{
			if(user.getCountry_id() > 0 ){
				user.setCountry(countryDao.findOne(user.getCountry_id()));
			}
			if(user.getState_id() > 0){
				user.setState(stateDao.findOne(user.getState_id()));
			}
			if(user.getCity_id() > 0){
				user.setCity(cityDao.findOne(user.getCity_id()));
			}		
			
			user.setPassword(user.getPassword());
			user.setPassword(Md5.md5(user.getPassword()));
			userDao.create(user);
			mailService.sendMailToEmpolyee(user);
		} catch (MyWebException | MessagingException e) {
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public void updateEmployee(User entity) {
		User user = new User();
		try {
			user = userDao.findOne(entity.getUser_id());
			user.setFirst_name(entity.getFirst_name());
			user.setMiddle_name(entity.getMiddle_name());
			user.setLast_name(entity.getLast_name());
			user.setMobile_no(entity.getMobile_no());
			user.setAdhaar_no(entity.getAdhaar_no());
			user.setPan_no(entity.getPan_no());
			user.setEmail(entity.getEmail());
			user.setPan_no(entity.getPan_no());
			user.setCurrent_address(entity.getCurrent_address());
			user.setPermenant_address(entity.getPermenant_address());
			user.setPin_code(entity.getPin_code());
			user.setJoinDate(entity.getJoinDate());
			user.setUpdated_date(new LocalDate());
			user.setStatus(entity.getStatus());
			user.setRole(entity.getRole());
			
			if(entity.getCountry_id() > 0 ){
				user.setCountry(countryDao.findOne(entity.getCountry_id()));
			}
			if(entity.getState_id() > 0){
				user.setState(stateDao.findOne(entity.getState_id()));
			}
			if(entity.getCity_id() > 0){
				user.setCity(cityDao.findOne(entity.getCity_id()));
			}
			
			userDao.update(user);	
		} catch (MyWebException e) {
			e.printStackTrace();
		}		
		
	}

	@Override
	public List<User> getUserByRole(List<Long> roleIds) {		
		return userDao.getUserByRole(roleIds);
	}

	@Override
	public List<User> getActiveUserByCompany(Long company_id) {
		// TODO Auto-generated method stub
		return userDao.getActiveUserByCompany(company_id);
	}

	@Override
	public List<User> findallemployeeAndAuditorOfAllCompanies() {
		// TODO Auto-generated method stub
		return userDao.findallemployeeAndAuditorOfAllCompanies();
	}

	@Override
	public Integer getActiveUserByCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		return userDao.getActiveUserByCompanyDashboard(companyId);
	}

	@Override
	public List<User> findAllExecutiveOfManager(Long user_id) {
		// TODO Auto-generated method stub
		return userDao.findAllExecutiveOfManager(user_id);
	}

	@Override
	public List<User> getUserByMail(String userName) {
		// TODO Auto-generated method stub
		return userDao.getUserByMail(userName);
	}

	@Override
	public User getUserByUserName(Long companyId, String userName) {
		// TODO Auto-generated method stub
		return userDao.getUserByUserName(companyId, userName);
	}

	
}