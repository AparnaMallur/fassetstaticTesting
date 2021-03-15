package com.fasset.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.entities.LoginLog;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ILoginLogService;
import com.fasset.dao.interfaces.ILoginLog;
import com.fasset.dao.interfaces.IUserDAO;

@Service
@Transactional
public class LoginLogServiceImpl implements ILoginLogService{

	@Autowired
	private ILoginLog loginDAO;
	
	@Autowired
	private IUserDAO userDAO;
	
	@Override
	public void add(LoginLog entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(LoginLog entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LoginLog> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoginLog getById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoginLog getById(String id) throws MyWebException {
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
	public void remove(LoginLog entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(LoginLog entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LoginLog isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Long user_id, int i, String remoteAddr) {
		
		LoginLog loginlog= new LoginLog();
		loginlog.setType(i);
		loginlog.setUser_id(user_id);
		loginlog.setIp_address(remoteAddr);
		loginlog.setCreated_date(new LocalDateTime());
		loginlog.setLogin_date(new LocalDate());
		try {
			loginlog.setUser(userDAO.findOne(user_id));		
			loginDAO.create(loginlog);
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<LoginLog> Loginlog(Long user_id,LocalDate from_date, LocalDate to_date) {
		// TODO Auto-generated method stub
		return loginDAO.Loginlog(user_id,from_date,to_date);
	}

}
