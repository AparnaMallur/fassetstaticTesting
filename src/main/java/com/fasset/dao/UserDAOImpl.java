package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.ccavenue.security.Md5;
import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.JavaMD5Hash;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.mobileApp.entity.LoginDetails;

@Repository
public class UserDAOImpl extends AbstractHibernateDao<User> implements IUserDAO{
	@Override
	public User findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("user_id", id));
		criteria.setFetchMode("manager_id", FetchMode.JOIN);
		return (User) criteria.uniqueResult();
	}

	@Override
	public User findOne(Criteria crt) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findOne(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAll() {
		List<User> list = new ArrayList<User>();
		Session  session = getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("user_id"));
		list = criteria.list();
		session.clear();
		return list;
	}

	@Override
	public void create(User entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDate());
		session.save(entity);	
		session.flush();
	    session.clear();
	}

	@Override
	public void update(User entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
	    session.clear();
		
	}

	@Override
	public void merge(User entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(User entity) throws MyWebException {
		Session session = getCurrentSession();
		session.delete(entity);
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
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public User loadUserByUsername(String userName,String password) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("email", userName));
		criteria.add(Restrictions.eq("password", Md5.md5(password)));
		return (User) criteria.uniqueResult();
	}

    /* public List<User> getAllStudent() {
		
		Session session;
		return (List<User>) session.getCurrentSession().createQuery("from Student").list();
	}*/
	
	@Override
	public Long saveUser(User user) {
		Session session = getCurrentSession();
		user.setCreated_date(new LocalDate());
		Long id  = (Long) session.save(user);
		session.flush();
	    session.clear();
		return id;
	}
	
	@Override
	public LoginDetails authenticateLoginDetail(String userName,String password)
	{
		Session session = getCurrentSession();
		
		Criteria criteria=session.createCriteria(User.class);
		criteria.add(Restrictions.eq("email", userName));
		criteria.add(Restrictions.eq("password", Md5.md5(password)));
	    User user=(User)criteria.uniqueResult();
	    LoginDetails details = new LoginDetails();
System.out.println("The database hit is " );
		  if(user!=null)
		  {
			  details.setSuccessMsg("success");
			  details.setResponseMassage("User loged successfully.");
			  details.setFirstName(user.getFirst_name());
			  System.out.println(user.getFirst_name() );
			  details.setLastName(user.getLast_name());
			  
		  }
		  else
		  {
			  details.setSuccessMsg("failure");
			  details.setResponseMassage("Username or password is invalid.");
			 
		  }
		  return details;  
	}
	
	@Override
	public Long authenticate(String userName, String password) {
		
		Session session = getCurrentSession();
		Query query = session.createQuery("select user_id from User where email=:email and password=:password");
		query.setParameter("email", userName);
		query.setParameter("password", password);
		
		if(query.uniqueResult()!=null){
			Query queryUser = session.createQuery("select user_id from User where email=:email and password=:password and status=:status");
			queryUser.setParameter("email", userName);
			queryUser.setParameter("password", password);
			queryUser.setParameter("status", true);
			if(queryUser.uniqueResult()==null){
				return (long) 0;
			}
			
			
			Query query1 = session.createQuery("select company.company_id from User where email=:email and password=:password");
			query1.setParameter("email", userName);
			query1.setParameter("password", password);
			Long company_id=(Long)query1.uniqueResult();
		System.out.println("The company id is jan2021 "+company_id);
			Query querycomp = session.createQuery("from Company where company_id =:company_id and (status!=0 and status!=1)");
			querycomp.setParameter("company_id", company_id);
			if(querycomp.list()==null || querycomp.list().isEmpty()) {
				return (long) -2;
			}
			
			Query querycomp1 = session.createQuery("from Company where company_id =:company_id and status=5");
			querycomp1.setParameter("company_id", company_id);			
			if((querycomp1.list()!=null) && (!querycomp1.list().isEmpty())) {
				return (long) -3;
			}
			else {
				return (Long)query.uniqueResult();
			}
		}
		else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserByRole(Long roleId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from User where role.role_id=:roleId");
		query.setParameter("roleId", roleId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserByStatus(Integer statusId, Long roleId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from User where status=:statusId and role.role_id=:roleId");
		query.setParameter("statusId", statusId);
		query.setParameter("roleId", roleId);
		return query.list();
	}
	
	@Override
	public User isExists(String mobileNo) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("mobile_no", mobileNo));
		return (User) criteria.uniqueResult();
	}
	
	@Override
	public User isExistsemail(String email) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("email", email));
		criteria.setMaxResults(1);
		return (User) criteria.uniqueResult();
	}

	@Override
	public void userDeactivate(User user) {
		Session session = getCurrentSession();
		Query query = session.createQuery("update User set status =:status where user_id =:userId");
		query.setParameter("userId", user.getUser_id());
		query.setParameter("status", user.getStatus());
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserByCompany(Long companyId) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select u.user_id, c.company_name,u.first_name,u.middle_name,u.last_name,u.current_address,u.email,u.mobile_no,r.role_name,u.status from user u, role r,company c where u.role_id=r.role_id and u.company_id=c.company_id and u.company_id=:company_id order by u.user_id desc");
		query.setParameter("company_id", companyId);
		return query.list();	
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getActiveUserByCompany(Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.ne("role.role_id", MyAbstractController.ROLE_CLIENT));
		criteria.add(Restrictions.eq("status", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	/*@SuppressWarnings("unchecked")
	@Override
	public List<User> getActiveUserByCompany(String userName,String password,Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("email", userName));
		criteria.add(Restrictions.eq("password",Md5.md5(password)));
		criteria.add(Restrictions.ne("role.role_id", MyAbstractController.ROLE_CLIENT));
		criteria.add(Restrictions.eq("status", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
		
	}*/

	@Override
	public User getUser(Long companyId, Long roleId) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("role.role_id", roleId));
		return (User) criteria.uniqueResult();
	}

	@Override
	public void updateKpoExAndManager(User entity) {
		Session session = getCurrentSession();
		/*Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("user_id", entity.getUser_id()));
		User user = (User) criteria.uniqueResult();
		user.setFirst_name(entity.getFirst_name());
		user.setMiddle_name(entity.getMiddle_name());
		user.setLast_name(entity.getLast_name());
		user.setRole(entity.getRole());
		user.setMobile_no(entity.getMobile_no());
		user.setEmail(entity.getEmail());
		user.setAdhaar_no(entity.getAdhaar_no());
		user.setPan_no(entity.getPan_no());
		user.setCountry(entity.getCountry());
		user.setState(entity.getState());
		user.setCity(entity.getCity());
		user.setStatus(entity.getStatus());*/
		session.update(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getManagerAndExecutive() {
		List<Long> roleIds = new ArrayList<Long>();
		Long role_executive = MyAbstractController.ROLE_EXECUTIVE; 
		Long role_manager = MyAbstractController.ROLE_MANAGER;
		roleIds.add(role_executive);
		roleIds.add(role_manager);
		
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.in("role.role_id", roleIds));
		criteria.addOrder(Order.asc("first_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<User> getUserByRole(List<Long> roleIds) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.in("role.role_id", roleIds));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findallemployeeAndAuditorOfAllCompanies() {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select u.user_id, c.company_name,u.first_name,u.middle_name,u.last_name,u.current_address,u.email,u.mobile_no,r.role_name,u.status from user u, role r,company c where u.role_id=r.role_id and u.company_id=c.company_id  order by u.user_id desc");
		return query.list();
	}

	@Override
	public Integer getActiveUserByCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.ne("role.role_id", MyAbstractController.ROLE_CLIENT));
		criteria.add(Restrictions.eq("status", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list().size();
	}

	@Override
	public List<User> findallemployeeAndAuditorOfAllCompaniesexe(List<Long> companyIds) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select u.user_id, c.company_name,u.first_name,u.middle_name,u.last_name,u.current_address,u.email,u.mobile_no,r.role_name,u.status from user u, role r,company c where u.role_id=r.role_id and u.company_id=c.company_id and u.company_id in(:company_id) order by u.user_id desc");
		query.setParameterList("company_id", companyIds);
		return query.list();
	}

	@Override
	public List<User> findAllExecutiveOfManager(Long user_id) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("manager_id.user_id", user_id));
		criteria.addOrder(Order.asc("first_name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public User loadUserByUsername(String userName) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("email", userName));
		criteria.setMaxResults(1);
		return (User) criteria.uniqueResult();
	}

	@Override
	public List<User> getUserByMail(String userName) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("email", userName));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public User getUserByUserName(Long companyId, String userName) {
		// TODO Auto-generated method stub
		System.out.println("email in userdao  is "+userName);
		System.out.println("companyid in userdao  is "+companyId);
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("email", userName));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		
		return (User) criteria.uniqueResult();
	}

	
}