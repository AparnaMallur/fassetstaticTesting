/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Country;
import com.fasset.entities.Employee;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.State;
import com.fasset.exceptions.MyWebException;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class StateDAOImpl extends AbstractHibernateDao<State> implements IStateDAO{

	
	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IStateDAO#saveStatedao(com.fasset.entities.State)
	 */
	@Override
	public Long saveStatedao(State state) {
		
		Session session = getCurrentSession();
		state.setCreated_date(new LocalDateTime());
		
		Long cntryid = state.getCountry_id();
		Criteria criteria = getCurrentSession().createCriteria(Country.class);
		criteria.add(Restrictions.eq("country_id", cntryid));
		
		Country cntry = (Country) criteria.uniqueResult();
		state.setCntry(cntry);
		Long id = (Long) session.save(state);
		session.flush();
	    session.clear();
		return id;
		
	}
      
	@Override
	public State findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(State.class);
		criteria.add(Restrictions.eq("state_id", id));
		return (State) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<State> findAll() {
		List<State> list =  new ArrayList<State>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT state from State state LEFT JOIN FETCH state.city city");
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((State)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
		}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<State> findAllListing() {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select s.state_id, c.country_name,s.state_name,s.state_code, s.status from state_master s,country_master c where s.country_id=c.country_id order by s.state_id desc");
		return query.list();
		}
	
	@Override
	public void create(State entity) throws MyWebException {
		Session session = getCurrentSession();
		entity.setCreated_date(new LocalDateTime());
		Long cntryid = entity.getCountry_id();
		Country cntry = entity.getCntry();
		cntry.setCountry_id(cntryid);
		entity.setCntry(cntry);
		session.save(entity);	
		session.flush();
	    session.clear();
	}

	@Override
	public void update(State entity) throws MyWebException {
		Session session = getCurrentSession();	
		session.merge(entity);
		session.flush();
	    session.clear();
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IStateDAO#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		String msg="";
		String res="You can't delete state because it is releted to other cities";
		if(res != null)
		{
			msg= "You can't delete state because it is releted to other cities";
		}
		
		else 
		{
		Query query = session.createQuery("delete from State where state_id =:id");
		query.setParameter("id", entityId);
		try{
				query.executeUpdate();		
			}
		catch(Exception e)
		{
			msg= "You can't delete state because it is releted to other cities";
			
		}
		
		}
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public State isExists(String name) {
		Criteria criteria = getCurrentSession().createCriteria(State.class);
		criteria.add(Restrictions.eq("state_name", name));
		return (State) criteria.uniqueResult();
	}

	@Override
	public State loadStateCode(Long state_code) {
		Criteria criteria = getCurrentSession().createCriteria(State.class);
		criteria.add(Restrictions.eq("state_code", state_code));
		return (State) criteria.uniqueResult();
	}
	
}
	
	
	

