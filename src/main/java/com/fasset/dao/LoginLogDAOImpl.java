package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.ILoginLog;
import com.fasset.entities.Bank;
import com.fasset.entities.LoginLog;
import com.fasset.exceptions.MyWebException;

@Repository
public class LoginLogDAOImpl extends AbstractHibernateDao<LoginLog> implements ILoginLog{

	@Override
	public LoginLog isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void create(LoginLog entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);	
		session.flush();
	    session.clear();
	}

	@Override
	public void update(LoginLog entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);	
		session.flush();
	    session.clear();
	}

	@Override
	public void merge(LoginLog entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(LoginLog entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<LoginLog> Loginlog(Long user_id,LocalDate from_date, LocalDate to_date) {
		List<LoginLog> list = new ArrayList<LoginLog>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT log from LoginLog log WHERE log.user.user_id =:user_id and log.login_date>=:from_date and log.login_date<=:to_date");
		query.setParameter("user_id", user_id);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		while (scrollableResults.next()) {
		list.add((LoginLog)scrollableResults.get()[0]);
		session.evict(scrollableResults.get()[0]);
		}
		session.clear();
		return list;
	}


}
