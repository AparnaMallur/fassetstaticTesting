package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IServiceDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.dao.interfaces.generic.IExecutiveTimesheetDAO;
import com.fasset.entities.ActivityLog;
import com.fasset.entities.Company;
import com.fasset.entities.ExecutiveTimesheet;
import com.fasset.entities.Service;
import com.fasset.exceptions.MyWebException;

/**
 * @author vijay ghodake
 *
 * deven infotech pvt ltd.
 */
@Repository
public class ExecutiveTimesheetDAOImpl extends AbstractHibernateDao<ExecutiveTimesheet> implements IExecutiveTimesheetDAO{
	
	@Autowired
	private ICompanyDAO companyDAO ;
	
	@Autowired
	private IServiceDAO serviceDAO ;
	

	@Override
	public Long saveExecutiveTimesheetdao(ExecutiveTimesheet executiveTimesheet) {
		Session session = getCurrentSession();
		
		Long id = new Long(0);
		if (executiveTimesheet.getTimesheetDetails() != "") {
			
			JSONArray jsonArray = new JSONArray(executiveTimesheet.getTimesheetDetails());
			for (int i = 0; i < jsonArray.length(); i++) {
				ExecutiveTimesheet timesheet = new ExecutiveTimesheet();
				JSONObject json = jsonArray.getJSONObject(i);
				try {
					timesheet.setCompany(companyDAO.findOne(Long.parseLong(json.getString("companyId"))));
					timesheet.setService(serviceDAO.findOne(Long.parseLong(json.getString("serviceId"))));
					timesheet.setTotal_time(json.getString("totalTime"));
					timesheet.setDate(executiveTimesheet.getDate());
					timesheet.setUser(executiveTimesheet.getUser());
					timesheet.setRemark(json.getString("remark"));
					id = (Long) session.save(timesheet);
					session.flush();
				    session.clear();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}
		}
		
		return id;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
			Session session = getCurrentSession();
			Query query = session.createQuery("delete from ExecutiveTimesheet where timesheet_id =:id");
			query.setParameter("id", entityId);
			String msg="";
			try{
			query.executeUpdate();
			msg= "Timesheet Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete timesheet";
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExecutiveTimesheet> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from ExecutiveTimesheet order by timesheet_id desc");
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExecutiveTimesheet> findByUserId(Long user_id) {
		List<ExecutiveTimesheet> list = new ArrayList<>(); 
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT exe from ExecutiveTimesheet exe WHERE exe.user.user_id =:user_id order by timesheet_id desc");
		query.setParameter("user_id", user_id);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
		   list.add((ExecutiveTimesheet)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		return list;
	}
	
	@Override
	public ExecutiveTimesheet findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(ExecutiveTimesheet.class);
		criteria.add(Restrictions.eq("timesheet_id", id));
		return (ExecutiveTimesheet) criteria.uniqueResult();
	}
	
	@Override
	public void update(ExecutiveTimesheet entity) throws MyWebException {
		Session session = getCurrentSession();
		
		Criteria criteria = getCurrentSession().createCriteria(ExecutiveTimesheet.class);
		criteria.add(Restrictions.eq("timesheet_id", entity.getTimesheet_id()));
		ExecutiveTimesheet executiveTimesheet = (ExecutiveTimesheet) criteria.uniqueResult();
		
		Long companyId = entity.getCompany_id();
		Criteria criteria1 = getCurrentSession().createCriteria(Company.class);
		criteria1.add(Restrictions.eq("company_id", companyId));
		Company company =(Company) criteria1.uniqueResult();
		
		Long serviceId = entity.getService_id();
		Criteria criteria3 = getCurrentSession().createCriteria(Service.class);
		criteria3.add(Restrictions.eq("id", serviceId));
		Service service = (Service) criteria3.uniqueResult();
		
		executiveTimesheet.setCompany(company);
		executiveTimesheet.setRemark(entity.getRemark());
		executiveTimesheet.setService(service);
		executiveTimesheet.setFrom_date(entity.getFrom_date());
		executiveTimesheet.setTo_date(entity.getTo_date());
		executiveTimesheet.setTotal_time(entity.getTotal_time());
		session.merge(executiveTimesheet);
		session.flush();
	    session.clear();
		
	}

	@Override
	public ExecutiveTimesheet isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExecutiveTimesheet> findByDate(LocalDate date, Long user_id) {
		Criteria criteria = getCurrentSession().createCriteria(ExecutiveTimesheet.class);
		criteria.add(Restrictions.eq("date", date));
		criteria.add(Restrictions.eq("user.user_id", user_id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<ExecutiveTimesheet> findTimesheetOfExecutiveByDate(Long user_id, LocalDate fromdate, LocalDate todate) {
		List<ExecutiveTimesheet> list = new ArrayList<>(); 
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT exe from ExecutiveTimesheet exe WHERE exe.user.user_id =:user_id and exe.date>=:from_date and exe.date<=:to_date");
		query.setParameter("user_id", user_id);
		query.setParameter("from_date", fromdate);
		query.setParameter("to_date", todate);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
		   list.add((ExecutiveTimesheet)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		return list;
	}
}
