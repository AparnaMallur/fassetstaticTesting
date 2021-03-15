/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import javax.mail.MessagingException;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ccavenue.security.Md5;
import com.fasset.dao.interfaces.ICityDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.dao.interfaces.IKPOExecutiveDAO;
import com.fasset.dao.interfaces.IRoleDAO;
import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.entities.JavaMD5Hash;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IKPOExecutiveService;
import com.fasset.service.interfaces.IMailService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class KPOExecutiveServiceImpl implements IKPOExecutiveService{

	
	@Autowired
	private IUserDAO UserDAO;
	
	@Autowired
	private ICityDAO cityDAO ;
	
	@Autowired
	private ICountryDAO countryDao;
	
	@Autowired
	private IStateDAO stateDAO ;
	
	@Autowired
	private IRoleDAO roleDAO ;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private IKPOExecutiveDAO KPOExecutiveDAO ;
	
	@Autowired
	private ICompanyDAO companyDAO ;
	
	@Override
	public void add(User entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(User entity) throws MyWebException {
		User user = UserDAO.findOne(entity.getUser_id());
		
		user.setUpdated_date(new LocalDate());
		user.setUpdated_by(entity.getUpdated_by());
		try{
			user.setFirst_name(entity.getFirst_name());
			user.setMiddle_name(entity.getMiddle_name());
			user.setLast_name(entity.getLast_name());
			user.setMobile_no(entity.getMobile_no());
			user.setEmail(entity.getEmail());
			user.setAdhaar_no(entity.getAdhaar_no());
			user.setPan_no(entity.getPan_no());
			user.setStatus(entity.getStatus());
			user.setCountry(countryDao.findOne(entity.getCountry_id()));
			user.setState(stateDAO.findOne(entity.getState_id()));
			user.setCity(cityDAO.findOne(entity.getCity_id()));
			user.setRole(roleDAO.findOne(entity.getRole_id()));
			user.setManager_id(UserDAO.findOne(entity.getManager()));
		}
		catch (MyWebException e) {
			e.printStackTrace();
		}
		UserDAO.updateKpoExAndManager(user);
	}

	@Override
	public List<User> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getById(Long id) throws MyWebException {
		
		return UserDAO.findOne(id);
	}

	@Override
	public User getById(String id) throws MyWebException {
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
	public String saveUser(User User) {
		String msg = null;
		User.setCreated_date(new LocalDate());
		User.setPassword(User.getPassword());
		User.setPassword(Md5.md5(User.getPassword()));
		try {
			User.setCountry(countryDao.findOne(User.getCountry_id()));
			User.setState(stateDAO.findOne(User.getState_id()));
			User.setCity(cityDAO.findOne(User.getCity_id()));
			User.setRole(roleDAO.findOne(User.getRole_id()));
			User.setCompany(companyDAO.findOne(User.getCompany_id()));
			User.setManager_id(UserDAO.findOne(User.getManager()));
			User.setIs_updated(true);
			User.setStatus(true);
			
			
			UserDAO.create(User);
			
			mailService.sendMailToKpoExceAndManager(User);
		} catch (MyWebException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		Long id = User.getRole_id();
		if(id==3)
		{
			msg = "KPO Executive added successfully" ;
			
		}
		else
		{
			msg = "KPO Manager added successfully";
		}
		return msg;
	}

	@Override
	public List<User> findAll() {
		return UserDAO.findAll();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = countryDao.deleteByIdValue(entityId);
		return msg;
	}

	@Override
	public List<User> findAllKPOExecutive() {
		List<User> list = KPOExecutiveDAO.findAllKPOExecutive();
		return list;
	}

	@Override
	public User isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
