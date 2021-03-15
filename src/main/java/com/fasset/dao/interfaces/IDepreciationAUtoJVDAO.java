package com.fasset.dao.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.DepreciationSubledgerDetails;
import com.fasset.entities.Ledger;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.State;
import com.fasset.exceptions.MyWebException;



public interface IDepreciationAUtoJVDAO extends  IGenericDao<DepreciationAutoJV>{
	
	
	public List<Ledger> findAllSubledgerForDepreciation() ;
	public Long saveDepreciationAutoJV(DepreciationAutoJV entity);
	public void saveOnedjvdetail(DepreciationSubledgerDetails djv) ;
	public CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationAutoJVReletedToCompany() ;
	CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationAutoJVReletedToCompany(Long company_id);
	public DepreciationAutoJV findOne(Long id) throws MyWebException ;
	
	public String deleteByIdValue(Long entityId);
	public String deleteDetails(Long entityId);
	
	public List<DepreciationSubledgerDetails> getAllDepreciationSubledgerDetails(Long entityId);
	public int getCountByDate(Long companyId, String range, LocalDate date);

}