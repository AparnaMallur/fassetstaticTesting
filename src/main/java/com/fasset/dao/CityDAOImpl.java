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
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.ICityDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.City;
import com.fasset.entities.Country;
import com.fasset.entities.State;
import com.fasset.exceptions.MyWebException;


/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class CityDAOImpl extends AbstractHibernateDao<City> implements ICityDAO{

	@Override
	public Long saveCitydao(City city) {
		
		Session session = getCurrentSession();
		city.setCreated_date(new LocalDateTime());
		Long stateid = city.getState_id();
		Criteria criteria = getCurrentSession().createCriteria(State.class);
		criteria.add(Restrictions.eq("state_id", stateid));
		State state =(State) criteria.uniqueResult();
		
		Long cid = city.getCountry_id();
		Criteria criteria2 = getCurrentSession().createCriteria(Country.class);
		criteria2.add(Restrictions.eq("country_id", cid));
		Country country =(Country) criteria2.uniqueResult();
		
		city.setStatus(city.getStatus());
	     city.setState(state);
	     city.setCountry(country);
		Long id = (Long) session.save(city);
		session.flush();
		session.clear();
		return id;
	}
	
	@Override
	public City findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(City.class);
		criteria.add(Restrictions.eq("city_id", id));
		return (City) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<City> findAll() {
		List<City> list =  new ArrayList<City>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT city from City city");
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((City)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<City> findAllListing() {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select t.city_id, c.country_name,s.state_name,s.state_code,t.city_name, t.status from state_master s,country_master c, city_master t where t.state_id=s.state_id and t.country_id=c.country_id order by t.city_id desc");
		return query.list();
	}
	
	@Override
	public void create(City entity) throws MyWebException {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
		session.clear();
	}

	@Override
	public void update(City entity) throws MyWebException {
		Session session = getCurrentSession();
		session.merge(entity);
		session.flush();
		session.clear();
		
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		Session session = getCurrentSession();
		String msg="";
		//Query res=select c.
		//String res="select c.*,cm.*,cp.*,u.*,sm.* from city_master c INNER JOIN customer_master cm ON c.city_id = cm.city_id INNER JOIN supplier_master sm ON c.city_id = sm.city_id INNER JOIN company cp ON c.city_id = cp.city INNER JOIN user u ON c.city_id = u.city_id where c.city_id =:city_id";
		String res="You can't delete City City is used foe somewhere";
		if(res != null)
		{
			msg= "You Can't Delete City";
		}
		
		else 
		{ 
		Query query =session.createQuery("delete from City where city_id =:id");
		query.setParameter("id", entityId); query.executeUpdate(); 
		msg="City Deleted successfully"; }
		 
		return msg;
	}

	@Override
	public City isExists(String name) {		
		Criteria criteria = getCurrentSession().createCriteria(City.class);
		criteria.add(Restrictions.eq("city_name", name));
		return (City) criteria.uniqueResult();
	}
	
}


