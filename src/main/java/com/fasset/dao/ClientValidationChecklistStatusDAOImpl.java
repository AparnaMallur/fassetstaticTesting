/**
 * mayur suramwar
 */
package com.fasset.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IClientValidationChecklistStatusDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.ClientValidationChecklistStatus;
import com.fasset.entities.Company;
import com.fasset.entities.IndustryType;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IYearEndingService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Repository
public class ClientValidationChecklistStatusDAOImpl extends AbstractHibernateDao<ClientValidationChecklistStatus> implements IClientValidationChecklistStatusDAO {

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IClientValidationChecklistStatusDAO#saveClientValidationChecklistStatus(com.fasset.entities.ClientValidationChecklistStatus)
	 */
	@Autowired
	private ICompanyDAO dao ;
	
	@Autowired
	private IUserDAO userDAO;
	
	@Override
	public String saveClientValidationChecklistStatus(ClientValidationChecklistStatus status,User Client) {
		    
		    Session session = getCurrentSession();
		try{
			Set<ClientValidationChecklist> newcheckList = status.getNewcheckList();
			
			for(ClientValidationChecklist validation : newcheckList)
			{
				ClientValidationChecklistStatus obj = new ClientValidationChecklistStatus();
				obj.setCreated_date(new LocalDateTime());
				obj.setCompany(status.getCompany());
				obj.setChecklist(validation);
				session.save(obj);
				session.flush();
				session.clear();
			}
			Query query = session.createQuery("update User set status=:status where user_id=:user_id");
			query.setParameter("status",true);
			query.setParameter("user_id",Client.getUser_id());
			query.executeUpdate();
			
			Criteria criteria = session.createCriteria(Company.class);
			criteria.add(Restrictions.eq("company_id", status.getCompany().getCompany_id()));
			Company entity= (Company) criteria.uniqueResult();
			
			entity.setStatus(status.getStatus());
			entity.setEmpLimit(status.getEmplimit());
			entity.setSubscription_from(status.getFromDate());
			entity.setSubscription_to(status.getToDate());
			entity.setYearRange(status.getYearRange());
			entity.setApproved_by(userDAO.findOne(status.getUpdatedby()));
			session.update(entity);
			
			Query query1 = session.createQuery("update ClientSubscriptionMaster set subscription_from=:fromDate, subscription_to=:toDate where company.company_id=:company_id");
			query1.setParameter("fromDate",status.getFromDate());
			query1.setParameter("toDate",status.getToDate());
			query1.setParameter("company_id",status.getCompany().getCompany_id());
			query1.executeUpdate();			
			
			
			}
			catch(Exception e)
			{
				
				return "Client Validation Checklist Not Saved Successfully";
				
			}
		
		return "Client Validation Checklist Saved Successfully";
	}

	
	@Override
	public ClientValidationChecklistStatus findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(ClientValidationChecklistStatus.class);
		criteria.add(Restrictions.eq("checklist_status_id", id));
		return (ClientValidationChecklistStatus) criteria.uniqueResult();
	}
	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IClientValidationChecklistStatusDAO#deleteChecklistStatus(java.lang.Long, java.lang.Long)
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClientValidationChecklistStatus> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from ClientValidationChecklistStatus");
		//query.setParameter("status",true);
		return query.list();
	}
	
	@Override
	public String deleteChecklistStatus(Long cId, Long chId) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("delete from company_client_validation_checklist where checklist_checklist_id =:chId and company_company_id=:cId");
		query.setParameter("chId", chId);
		query.setParameter("cId", cId);
		String msg="";
		try{
		query.executeUpdate();
		msg= "Checklist  Deleted successfully";
		}
		catch(Exception e)
		{
			msg= "You can't delete Checklist";
			
		}
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.IClientValidationChecklistStatusDAO#getManadatoryCheclist()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ClientValidationChecklist> getManadatoryCheclist() {
		
		Session session = getCurrentSession();
		Query query = session.createQuery("from ClientValidationChecklist where is_mandatory=:is_mandatory");
		query.setParameter("is_mandatory",true);
		return query.list();
	}


	/* (non-Javadoc)
	 * @see com.fasset.dao.interfaces.generic.IGenericDao#isExists(java.lang.String)
	 */
	@Override
	public ClientValidationChecklistStatus isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
