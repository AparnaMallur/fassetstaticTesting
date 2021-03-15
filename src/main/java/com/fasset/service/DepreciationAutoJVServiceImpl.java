
  package com.fasset.service;

  
  
  
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IDepreciationAUtoJVDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.DepreciationSubledgerDetails;
import com.fasset.entities.Ledger;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.SubLedger;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IDepreciationAUtoJVService;
import com.fasset.service.interfaces.ISubLedgerService;
  @Service
  public class DepreciationAutoJVServiceImpl implements  IDepreciationAUtoJVService {
	  
	  @Autowired
		private IDepreciationAUtoJVDAO dao;
	  @Autowired
		private IAccountingYearDAO accountYearDAO;
	  @Autowired
		private ISubLedgerDAO SubLedgerDAO;
	  @Autowired
		ISubLedgerService subledgerService;
		 

	@Override
	public void add(DepreciationAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(DepreciationAutoJV entity) throws MyWebException {

		DepreciationAutoJV Entry = dao.findOne(entity.getDepreciation_id());
		
		entity.setAccountingYear(accountYearDAO.findOne(Entry.getAccountingYear().getYear_id()));
		//Entry.setAccountingYear((accountYearDAO.findOne(entity.getAccountYearId())));
	//	Entry.getAccountingYear()
		Set<DepreciationSubledgerDetails> depriciationList=new HashSet<DepreciationSubledgerDetails>();	
		List<DepreciationSubledgerDetails>  drmanualdetail= new ArrayList<DepreciationSubledgerDetails>();
		depriciationList=Entry.getDepriciationSubledgerDetails();
		dao.deleteDetails(entity.getDepreciation_id());	
		long company_id=Entry.getCompany().getCompany_id();
		if(entity.getDr_ledgerlist() !=null &&  !entity.getDr_ledgerlist().equals(""))
		{
			JSONArray jsonArray1 = new JSONArray(entity.getDr_ledgerlist());
			for (int i = 0; i < jsonArray1.length(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				DepreciationSubledgerDetails deprijvdetail1 = new DepreciationSubledgerDetails();
				try {
					deprijvdetail1.setSubledger(SubLedgerDAO.findOne((jsonObject1.getLong("subId"))));
				} catch (NumberFormatException | JSONException | MyWebException e) {
					e.getMessage();
					e.printStackTrace();
				}

				deprijvdetail1.setSubLedgerAmount((Double.parseDouble(jsonObject1.getString("subAmount"))));
				
				deprijvdetail1.setDepreciationAutoJV(Entry);
				drmanualdetail.add(deprijvdetail1);
				
	    	}
		}

		
	   for(int i=0;i<drmanualdetail.size();i++)
	    {
	      dao.saveOnedjvdetail(drmanualdetail.get(i));
	    }
	   SubLedger subledgerdep = SubLedgerDAO.findOne("Depreciation", entity.getCompany().getCompany_id());
		if (subledgerdep != null) {
			
			subledgerService.addsubledgertransactionbalance(Entry.getAccountingYear().getYear_id(), Entry.getDate(),
					Entry.getCompany().getCompany_id(),subledgerdep.getSubledger_Id(), (long) 2,(double) 0,
						(double) Entry.getAmount(), (long) 0, null, null, null, null, null, null,
					null, null, null, null, Entry,null);
		} 
	   for (DepreciationSubledgerDetails detail : depriciationList) {
			if (detail.getSubLedgerAmount() != null && detail.getSubLedgerAmount() > 0) {
			
				
				subledgerService.addsubledgertransactionbalance(Entry.getAccountingYear().getYear_id(), Entry.getDate(),
						Entry.getCompany().getCompany_id(), detail.getSubledger().getSubledger_Id(), (long) 2,
						 (double) detail.getSubLedgerAmount(),(double) 0,(long) 0, null, null, null, null, null, null,
						null, null, null, null, Entry,null);
			}
			/*
			 * else {
			 * subledgerService.addsubledgertransactionbalance(Entry.getAccountingYear().
			 * getYear_id(), Entry.getDate(), Entry.getCompany().getCompany_id(),
			 * detail.getSubledger().getSubledger_Id(), (long) 2,(double) 0, (double)
			 * detail.getSubLedgerAmount(), (long) 0, null, null, null, null, null, null,
			 * null, null, null, null, Entry,null); }
			 */
		}
	   
		  
		
		/*
		 * SubLedger depreciationSubledger =
		 * SubLedgerDAO.findOne("Depreciation",company_id);
		 * 
		 * if (depreciationSubledger != null && entity.getAmount()>0) {
		 * subledgerService.addsubledgertransactionbalance(Entry.getAccountingYear().
		 * getYear_id(),Entry.getDate(),company_id,
		 * depreciationSubledger.getSubledger_Id(), (long) 2, (double)
		 * 0,(double)Entry.getAmount(), (long)
		 * 0,null,null,null,null,null,null,null,null,null,null,entity,null); }
		 */		 
		for (DepreciationSubledgerDetails detail : drmanualdetail) {
			if (detail.getSubLedgerAmount() != null && detail.getSubLedgerAmount() > 0) {	
				subledgerService.addsubledgertransactionbalance(Entry.getAccountingYear().getYear_id(), entity.getDate(),
						Entry.getCompany().getCompany_id(), detail.getSubledger().getSubledger_Id(), (long) 2,
							(double) detail.getSubLedgerAmount(), (double) 0,(long) 1, null, null, null, null, null, null,
						null, null, null, null, Entry,null);
			}
			/*
			 * else {
			 * subledgerService.addsubledgertransactionbalance(Entry.getAccountingYear().
			 * getYear_id(), entity.getDate(), Entry.getCompany().getCompany_id(),
			 * detail.getSubledger().getSubledger_Id(), (long) 2,(double) 0, (double)
			 * detail.getSubLedgerAmount(), (long) 1, null, null, null, null, null, null,
			 * null, null, null, null, Entry,null); }
			 */
		}

		SubLedger subledgerdep1 = SubLedgerDAO.findOne("Depreciation", entity.getCompany().getCompany_id());
		if (subledgerdep1 != null) {

			subledgerService.addsubledgertransactionbalance(Entry.getAccountingYear().getYear_id(), Entry.getDate(),
					Entry.getCompany().getCompany_id(), subledgerdep1.getSubledger_Id(), (long) 2, (double) 0,
					(double) entity.getAmount(), (long) 1, null, null, null, null, null, null, null, null, null, null,
					Entry, null);
		}
		 
	   Entry.setDate(entity.getDate());
	   Entry.setAmount(entity.getAmount());
	   dao.update(entity);	
		
	}

	@Override
	public List<DepreciationAutoJV> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DepreciationAutoJV getById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		return dao.findOne(id);
	}

	@Override
	public DepreciationAutoJV getById(String id) throws MyWebException {
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
	public void remove(DepreciationAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(DepreciationAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DepreciationAutoJV isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ledger> findAllSubledgerForDepreciation() {
		// TODO Auto-generated method stub
		return dao.findAllSubledgerForDepreciation();
	}

	public void saveDepreciationAutoJV(DepreciationAutoJV entity)
	{
		Double amount  = 0.0d;
 
		try {
			entity.setAccountingYear(accountYearDAO.findOne(entity.getYear_id()));
		} catch (MyWebException e) {
			
			e.printStackTrace();
		}
		
		
	
		
		Set<DepreciationSubledgerDetails> depriciationList=new HashSet<DepreciationSubledgerDetails>();	
		List<DepreciationSubledgerDetails>  drmanualdetail= new ArrayList<DepreciationSubledgerDetails>();
		
	//	depriciationList=entity.getDepriciationSubledgerDetailsList();
		Long id =  dao.saveDepreciationAutoJV(entity);

		DepreciationAutoJV entry = null;

		try {
			entry = dao.findOne(id);
		} catch (MyWebException e1) {

			e1.printStackTrace();
		}
		SubLedger subledgerdep = SubLedgerDAO.findOne("Depreciation", entity.getCompany().getCompany_id());
		if (subledgerdep != null) {
			
			subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(), entry.getDate(),
					entry.getCompany().getCompany_id(),subledgerdep.getSubledger_Id(), (long) 2,(double) 0,
						(double) entity.getAmount(), (long) 1, null, null, null, null, null, null,
					null, null, null, null, entry,null);
		}
		  
		if(entity.getDr_ledgerlist() !=null &&  !entity.getDr_ledgerlist().equals(""))
		{
			JSONArray jsonArray1 = new JSONArray(entity.getDr_ledgerlist());
			for (int i = 0; i < jsonArray1.length(); i++) {
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				DepreciationSubledgerDetails deprijvdetail1 = new DepreciationSubledgerDetails();
				try {
					deprijvdetail1.setSubledger(SubLedgerDAO.findOne((jsonObject1.getLong("subId"))));
				} catch (NumberFormatException | JSONException | MyWebException e) {
					e.getMessage();
					e.printStackTrace();
				}
				
				//totalsum
				
				
				deprijvdetail1.setSubLedgerAmount((Double.parseDouble(jsonObject1.getString("subAmount"))));
				
				deprijvdetail1.setDepreciationAutoJV(entry);
				depriciationList.add(deprijvdetail1);
				drmanualdetail.add(deprijvdetail1);
				
	    	}
		}
		
		for(int i=0;i<drmanualdetail.size();i++)
		{
			dao.saveOnedjvdetail(drmanualdetail.get(i));
		}
		
		
		for (DepreciationSubledgerDetails detail : depriciationList) {
			if (detail.getSubLedgerAmount() != null && detail.getSubLedgerAmount() > 0) {	
				subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(), entry.getDate(),
						entry.getCompany().getCompany_id(), detail.getSubledger().getSubledger_Id(), (long) 2,
							(double) detail.getSubLedgerAmount(), (double) 0,(long) 1, null, null, null, null, null, null,
						null, null, null, null, entry,null);
			}
			/*
			 * else {
			 * subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().
			 * getYear_id(), entry.getDate(), entry.getCompany().getCompany_id(),
			 * detail.getSubledger().getSubledger_Id(), (long) 2,(double) 0, (double)
			 * detail.getSubLedgerAmount(), (long) 1, null, null, null, null, null, null,
			 * null, null, null, null, entry,null); }
			 */
		}
		 
		 
		try {
			dao.update(entry);
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		entry.setAmount(amount);
		
		 /* if(entity.getDr_ledgerlist() !=null &&
		  !entity.getDr_ledgerlist().equals("")) { JSONArray jsonArray1 = new
		  JSONArray(entity.getDr_ledgerlist()); for (int i = 0; i <
		  jsonArray1.length(); i++) { JSONObject jsonObject1 =
		  jsonArray1.getJSONObject(i); DepreciationSubledgerDetails
		  depriciationSubdetail1 = new DepreciationSubledgerDetails(); try {
		  
		  depriciationSubdetail1.setSubledger(SubLedgerDAO.findOne(Long.parseLong(
		  jsonObject1.getString("drledgerId"))));
		  
		  } catch (NumberFormatException | JSONException | MyWebException e) { // TODO
		  Auto-generated catch block e.printStackTrace(); }
		  depriciationSubdetail1.setSubLedgerAmount(Double.parseDouble(jsonObject1.
		  getString("dramountId")));
		  
		  depriciationSubdetail1.setDepreciationAutoJV(entry);
		  depriciationList.add(depriciationSubdetail1);
		  
		  } }
		 
//		for(int i=0;i<depriciationList.size();i++)
		/*{
			dao.saveOnedjvdetail(depriciationList.get(i));
		}

		for(DepreciationSubledgerDetails detail:depriciationList)
		{
			if(detail.getSubLedgerAmount()!=null && detail.getSubLedgerAmount()>0)
			{
				subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(),entry.getDate(),entry.getCompany().getCompany_id(),
						detail.getSubledger().getSubledger_Id(), (long) 2, (double)detail.getSubLedgerAmount(), (double) 0, (long) 1,null,null,null,null,null,null,null,null,null,null,entry);
			
			}
		}*/
		
	}
	
	@Override
	public CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationAutoJVReletedToCompany() {
		// TODO Auto-generated method stub
		return dao.findAllDepreciationAutoJVReletedToCompany();
	}

	public CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationAutoJVReletedToCompany(Long company_id){
		return dao.findAllDepreciationAutoJVReletedToCompany(company_id);
		
	}

	@Override
	public List<DepreciationSubledgerDetails> getAllDepreciationSubledgerDetails(Long entityId) {
		// TODO Auto-generated method stub
		return dao.getAllDepreciationSubledgerDetails(entityId);
	}

	
	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		try {
			DepreciationAutoJV djv=dao.findOne(entityId);
			//ManualJournalVoucher mjv = journalDao.findOne(entityId);
			Set<DepreciationSubledgerDetails> djvdetails = new HashSet<DepreciationSubledgerDetails>();
			djvdetails=djv.getDepriciationSubledgerDetails();
			
			for (DepreciationSubledgerDetails djvdetail : djvdetails) {
				try {

					if (djvdetail.getSubLedgerAmount() != null && djv != null) {
						subledgerService.addsubledgertransactionbalance(djv.getAccountingYear().getYear_id(),
								djv.getDate(), djv.getCompany().getCompany_id(),
								djvdetail.getSubledger().getSubledger_Id(), (long) 2, (double) 0,
								(double) djvdetail.getSubLedgerAmount(), (long) 0, null, null, null, null, null, null,
								null, null, null, null, djv,null);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			 
			
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		
		return dao.deleteByIdValue(entityId);
	}
  
  }
 