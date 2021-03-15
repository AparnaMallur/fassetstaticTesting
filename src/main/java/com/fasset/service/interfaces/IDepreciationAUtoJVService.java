package com.fasset.service.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.DepreciationSubledgerDetails;
import com.fasset.entities.Ledger;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IDepreciationAUtoJVService extends IGenericService<DepreciationAutoJV>{
	
	
	public List<Ledger> findAllSubledgerForDepreciation() ;
	public void saveDepreciationAutoJV(DepreciationAutoJV entity) ;
	public CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationAutoJVReletedToCompany();
	public CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationAutoJVReletedToCompany(Long company_id);
	public List<DepreciationSubledgerDetails> getAllDepreciationSubledgerDetails(Long entityId);
	public String deleteByIdValue(Long entityId); 
}
