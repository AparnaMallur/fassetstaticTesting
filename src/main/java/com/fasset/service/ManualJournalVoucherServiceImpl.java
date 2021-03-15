package com.fasset.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.ICustomerDAO;
import com.fasset.dao.interfaces.IManualJournalVoucherDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.ISupplierDAO;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PayrollEmployeeDetails;
import com.fasset.entities.PayrollSubledgerDetails;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.IncomeAndExpenditureForm.IncomeOrExpense.Details;
import com.fasset.service.interfaces.IManualJournalVoucherService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.fasset.service.interfaces.ICustomerService;
@Service
@Transactional
public class ManualJournalVoucherServiceImpl implements IManualJournalVoucherService{

	@Autowired
	private IManualJournalVoucherDAO journalDao;
	
	@Autowired
	private IAccountingYearDAO accountYearDAO;
	
	@Autowired
	private ISubLedgerDAO SubLedgerDAO;
	@Autowired
	ISubLedgerService subledgerService;
	
	
	@Autowired
	private ISupplierDAO SupplireDAO;
	@Autowired
	ISuplliersService supplierService;
	
	@Autowired
	private ICustomerDAO CustomerDAO;
	@Autowired
	ICustomerService customerService;
	
	@Override
	public void add(ManualJournalVoucher entity) throws MyWebException {
		
	
	}
	
	@Override
	public void update(ManualJournalVoucher entity) throws MyWebException {
		ManualJournalVoucher voucher = journalDao.findOne(entity.getJournal_id());
		Double amount  = 0.0d;
		/*voucher.setDate(entity.getDate());*/
		voucher.setRemark(entity.getRemark());
		voucher.setAccounting_year_id(accountYearDAO.findOne(entity.getYear_id()));
		voucher.setUpdate_date(new LocalDate());
		Set<ManualJVDetails> mjvdetails = new HashSet<ManualJVDetails>();
		Set<ManualJVDetails> newmjvdetails = new HashSet<ManualJVDetails>();
		List<ManualJVDetails>  crmanualdetail= new ArrayList<ManualJVDetails>();
		List<ManualJVDetails>  drmanualdetail= new ArrayList<ManualJVDetails>();
		
		mjvdetails=voucher.getMjvdetails();

		for(ManualJVDetails details:mjvdetails) 
		{
			journalDao.deleteByMJVIdValue(details.getDetailjv_id());
		}
		
		if(entity.getCr_ledgerlist() !=null &&  !entity.getCr_ledgerlist().equals(""))
		{
			JSONArray jsonArray = new JSONArray(entity.getCr_ledgerlist());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
			
					ManualJVDetails mvjdetail = new ManualJVDetails();
					
					if(jsonObject.get("type").equals("sub")){
						
						try {
							mvjdetail.setSubledgercr(SubLedgerDAO.findOne(Long.parseLong(jsonObject.getString("crledgerId"))));
						} catch (NumberFormatException | JSONException | MyWebException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}else if(jsonObject.get("type").equals("sup")){
							
						
							try {
								mvjdetail.setSuppliercr(SupplireDAO.findOne(Long.parseLong(jsonObject.getString("crledgerId"))));
							} catch (NumberFormatException | JSONException | MyWebException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}else if(jsonObject.get("type").equals("cus")){
								
								try {
									mvjdetail.setCustomercr(CustomerDAO.findOne(Long.parseLong(jsonObject.getString("crledgerId"))));
								} catch (NumberFormatException | JSONException | MyWebException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}
					/*try {
						mvjdetail.setSubledgercr(SubLedgerDAO.findOne(Long.parseLong(jsonObject.getString("crledgerId"))));
					} catch (NumberFormatException | JSONException | MyWebException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					mvjdetail.setCramount(Double.parseDouble(jsonObject.getString("cramountId")));
					mvjdetail.setDramount(0.0d);
					mvjdetail.setManualjournalvoucher(voucher);
					newmjvdetails.add(mvjdetail);
					crmanualdetail.add(mvjdetail);
				
			}
		}
		
		
		if(entity.getDr_ledgerlist() !=null &&  !entity.getDr_ledgerlist().equals(""))
		{
			JSONArray jsonArray1 = new JSONArray(entity.getDr_ledgerlist());
			for (int i = 0; i < jsonArray1.length(); i++) {
				
				JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
				
				ManualJVDetails mvjdetail1 = new ManualJVDetails();
				/*try {
					mvjdetail1.setSubledgerdr(SubLedgerDAO.findOne(Long.parseLong(jsonObject1.getString("drledgerId"))));
				} catch (NumberFormatException | JSONException | MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				if(jsonObject1.get("type").equals("sub")){
					
				try {
					mvjdetail1.setSubledgerdr(SubLedgerDAO.findOne(Long.parseLong(jsonObject1.getString("drledgerId"))));
				} catch (NumberFormatException | JSONException | MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}else if(jsonObject1.get("type").equals("sup")){
					
					try {
						mvjdetail1.setSupplierdr(SupplireDAO.findOne(Long.parseLong(jsonObject1.getString("drledgerId"))));
					} catch (NumberFormatException | JSONException | MyWebException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}else if(jsonObject1.get("type").equals("cus")){
						
						try {
							mvjdetail1.setCustomerdr(CustomerDAO.findOne(Long.parseLong(jsonObject1.getString("drledgerId"))));
						} catch (NumberFormatException | JSONException | MyWebException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
				mvjdetail1.setDramount(Double.parseDouble(jsonObject1.getString("dramountId")));
				mvjdetail1.setCramount(0.0d);
				mvjdetail1.setManualjournalvoucher(voucher);
				newmjvdetails.add(mvjdetail1);
				drmanualdetail.add(mvjdetail1);
				
	    	}
		}
		
		for(int i=0;i<drmanualdetail.size();i++)
		{
			journalDao.saveOnemjvdetail(drmanualdetail.get(i));
		}
		
		for(int i=0;i<crmanualdetail.size();i++)
		{
			journalDao.saveOnemjvdetail(crmanualdetail.get(i));
		}
		
		for(ManualJVDetails details:newmjvdetails) 
		{
			if(details.getCramount()!=null)
			{
				amount=amount+details.getCramount();
			}
			
		}
	
		voucher.setAmount(amount);
		voucher.setUpdated_by(entity.getUpdated_by());
		journalDao.update(voucher);
		
		for(ManualJVDetails detail:mjvdetails)
		{
		/*if(detail.getCramount()!=null && detail.getCramount()>0)
		{
			subledgerService.addsubledgertransactionbalance(voucher.getAccounting_year_id().getYear_id(),voucher.getCreated_date(),voucher.getCompany().getCompany_id(),
					detail.getSubledgercr().getSubledger_Id(), (long) 2, (double)detail.getCramount(), (double) 0,
				(long) 0,null,null,null,null,null,null,null,voucher,null,null,null,null);
		}
		else 
		{
			subledgerService.addsubledgertransactionbalance(voucher.getAccounting_year_id().getYear_id(),voucher.getCreated_date(),voucher.getCompany().getCompany_id(),
					detail.getSubledgerdr().getSubledger_Id(), (long) 2,  (double) 0,(double)detail.getDramount(),
				(long) 0,null,null,null,null,null,null,null,voucher,null,null,null,null);
		}*/
			
			
			if(detail.getSubledgercr()!=null){
				
				subledgerService.addsubledgertransactionbalance(voucher.getAccounting_year_id().getYear_id(),voucher.getCreated_date(),voucher.getCompany().getCompany_id(),
						detail.getSubledgercr().getSubledger_Id(), (long) 2, (double)detail.getCramount(), (double) 0, (long) 0,null,null,null,null,null,null,null,voucher,null,null,null,null);
				
			}else if (detail.getSubledgerdr()!=null){
				
				subledgerService.addsubledgertransactionbalance(voucher.getAccounting_year_id().getYear_id(),voucher.getCreated_date(),voucher.getCompany().getCompany_id(),
						detail.getSubledgerdr().getSubledger_Id(), (long) 2, (double) 0,(double)detail.getDramount(),  (long) 0,null,null,null,null,null,null,null,voucher,null,null,null,null);
				
			}else if(detail.getSuppliercr()!=null){
				
			//	supplierService.addsuppliertrsansactionbalance(entry.getAccounting_year_id().getYear_id(), entry.getCreated_date(), entry.getCompany().getCompany_id(), detail.getSuppliercr().getSupplier_id(),(long) 2, (double) 0,(double)detail.getDramount(), (long) 1,null,null,null,null,null,null,null,entry,null,null,null,null);
				supplierService.addsuppliertrsansactionbalance(voucher.getAccounting_year_id().getYear_id(), voucher.getCreated_date(), voucher.getCompany().getCompany_id(), detail.getSuppliercr().getSupplier_id(), (long) 4,  (double)detail.getCramount(),(double) 0, (long) 0);
			
				
			}else if(detail.getSupplierdr()!=null){
				
				supplierService.addsuppliertrsansactionbalance(voucher.getAccounting_year_id().getYear_id(), voucher.getCreated_date(), voucher.getCompany().getCompany_id(), detail.getSupplierdr().getSupplier_id(), (long) 4, (double) 0, (double)detail.getDramount(), (long) 0);
				
			}else if(detail.getCustomerdr()!=null){
				
				customerService.addcustomertransactionbalance(voucher.getAccounting_year_id().getYear_id(), voucher.getCreated_date(), voucher.getCompany().getCompany_id(), detail.getCustomerdr().getCustomer_id(), (long) 5, (double) 0,(double)detail.getDramount(), (long) 0);
			}else if (detail.getCustomercr()!=null){
				
				customerService.addcustomertransactionbalance(voucher.getAccounting_year_id().getYear_id(), voucher.getCreated_date(), voucher.getCompany().getCompany_id(), detail.getCustomerdr().getCustomer_id(), (long) 5,(double)detail.getCramount(), (double) 0, (long) 0);
			}
		}
		
		
		for(ManualJVDetails detail:newmjvdetails)
		{
			/*if(detail.getCramount()!=null && detail.getCramount()>0)
			{
				subledgerService.addsubledgertransactionbalance(voucher.getAccounting_year_id().getYear_id(),voucher.getCreated_date(),voucher.getCompany().getCompany_id(),
						detail.getSubledgercr().getSubledger_Id(), (long) 2, (double)detail.getCramount(), (double) 0, (long) 1,null,null,null,null,null,null,null,voucher,null,null,null,null);
			
			}
			else
			{
				subledgerService.addsubledgertransactionbalance(voucher.getAccounting_year_id().getYear_id(),voucher.getCreated_date(),voucher.getCompany().getCompany_id(),
						detail.getSubledgerdr().getSubledger_Id(), (long) 2, (double) 0,(double)detail.getDramount(),  (long) 1,null,null,null,null,null,null,null,voucher,null,null,null,null);
			
			}*/
			
			if(detail.getSubledgercr()!=null){
				
				subledgerService.addsubledgertransactionbalance(voucher.getAccounting_year_id().getYear_id(),voucher.getCreated_date(),voucher.getCompany().getCompany_id(),
						detail.getSubledgercr().getSubledger_Id(), (long) 2, (double)detail.getCramount(), (double) 0, (long) 1,null,null,null,null,null,null,null,voucher,null,null,null,null);
				
			}else if (detail.getSubledgerdr()!=null){
				
				subledgerService.addsubledgertransactionbalance(voucher.getAccounting_year_id().getYear_id(),voucher.getCreated_date(),voucher.getCompany().getCompany_id(),
						detail.getSubledgerdr().getSubledger_Id(), (long) 2, (double) 0,(double)detail.getDramount(),  (long) 1,null,null,null,null,null,null,null,voucher,null,null,null,null);
				
			}else if(detail.getSuppliercr()!=null){
				
			//	supplierService.addsuppliertrsansactionbalance(entry.getAccounting_year_id().getYear_id(), entry.getCreated_date(), entry.getCompany().getCompany_id(), detail.getSuppliercr().getSupplier_id(),(long) 2, (double) 0,(double)detail.getDramount(), (long) 1,null,null,null,null,null,null,null,entry,null,null,null,null);
				supplierService.addsuppliertrsansactionbalance(voucher.getAccounting_year_id().getYear_id(), voucher.getCreated_date(), voucher.getCompany().getCompany_id(), detail.getSuppliercr().getSupplier_id(), (long) 4,  (double)detail.getCramount(),(double) 0, (long) 1);
			
				
			}else if(detail.getSupplierdr()!=null){
				
				supplierService.addsuppliertrsansactionbalance(voucher.getAccounting_year_id().getYear_id(), voucher.getCreated_date(), voucher.getCompany().getCompany_id(), detail.getSupplierdr().getSupplier_id(), (long) 4, (double) 0, (double)detail.getDramount(), (long) 1);
				
			}else if(detail.getCustomerdr()!=null){
				
				customerService.addcustomertransactionbalance(voucher.getAccounting_year_id().getYear_id(), voucher.getCreated_date(), voucher.getCompany().getCompany_id(), detail.getCustomerdr().getCustomer_id(), (long) 5, (double) 0,(double)detail.getDramount(), (long) 1);
			}else if (detail.getCustomercr()!=null){
				
				customerService.addcustomertransactionbalance(voucher.getAccounting_year_id().getYear_id(), voucher.getCreated_date(), voucher.getCompany().getCompany_id(), detail.getCustomerdr().getCustomer_id(), (long) 5,(double)detail.getCramount(), (double) 0, (long) 1);
			}
		}
	}

	@Override
	public List<ManualJournalVoucher> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManualJournalVoucher getById(Long id) throws MyWebException {
		
		return journalDao.findOne(id);
	}

	@Override
	public List<ManualJVDetails> getAllMJVDetails(Long entityId) {
		return journalDao.getAllMJVDetails(entityId);
	}
	
	@Override
	public ManualJournalVoucher getById(String id) throws MyWebException {
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
	public void remove(ManualJournalVoucher entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(ManualJournalVoucher entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ManualJournalVoucher isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate) {
		// TODO Auto-generated method stub
		return journalDao.findAllManualJournalVoucherReletedToCompany(company_id,fromDate,toDate);
	}



	@Override
	public String deleteByIdValue(Long entityId) {
		
		try {
			ManualJournalVoucher mjv = journalDao.findOne(entityId);
			Set<ManualJVDetails> mjvdetails = new HashSet<ManualJVDetails>();
			mjvdetails=mjv.getMjvdetails();
			for(ManualJVDetails mjvdetail:mjvdetails)
			{
				try {
					/*
					if(mjvdetail.getSubledgercr()!=null && mjv!=null)
					{
						subledgerService.addsubledgertransactionbalance(mjv.getAccounting_year_id().getYear_id(),mjv.getCreated_date(),mjv.getCompany().getCompany_id(),
							mjvdetail.getSubledgercr().getSubledger_Id(), (long) 2, (double)mjvdetail.getCramount(), (double) 0,
							(long) 0,null,null,null,null,null,null,null,mjv,null,null,null,null);
					}
				
					else if(mjvdetail.getSubledgerdr()!=null  && mjv!=null)
					{
						subledgerService.addsubledgertransactionbalance(mjv.getAccounting_year_id().getYear_id(),mjv.getCreated_date(),mjv.getCompany().getCompany_id(),
							mjvdetail.getSubledgerdr().getSubledger_Id(), (long) 2,  (double) 0,(double)mjvdetail.getDramount(),
							(long) 0,null,null,null,null,null,null,null,mjv,null,null,null,null);
					}
					*/
					
					if(mjvdetail.getSubledgercr()!=null){
						
						subledgerService.addsubledgertransactionbalance(mjv.getAccounting_year_id().getYear_id(),mjv.getCreated_date(),mjv.getCompany().getCompany_id(),
								mjvdetail.getSubledgercr().getSubledger_Id(), (long) 2, (double)mjvdetail.getCramount(), (double) 0, (long) 0,null,null,null,null,null,null,null,mjv,null,null,null,null);
						
					}else if (mjvdetail.getSubledgerdr()!=null){
						
						subledgerService.addsubledgertransactionbalance(mjv.getAccounting_year_id().getYear_id(),mjv.getCreated_date(),mjv.getCompany().getCompany_id(),
								mjvdetail.getSubledgerdr().getSubledger_Id(), (long) 2, (double) 0,(double)mjvdetail.getDramount(),  (long) 0,null,null,null,null,null,null,null,mjv,null,null,null,null);
						
					}else if(mjvdetail.getSuppliercr()!=null){
						
					//	supplierService.addsuppliertrsansactionbalance(entry.getAccounting_year_id().getYear_id(), entry.getCreated_date(), entry.getCompany().getCompany_id(), detail.getSuppliercr().getSupplier_id(),(long) 2, (double) 0,(double)detail.getDramount(), (long) 1,null,null,null,null,null,null,null,entry,null,null,null,null);
						supplierService.addsuppliertrsansactionbalance(mjv.getAccounting_year_id().getYear_id(), mjv.getCreated_date(), mjv.getCompany().getCompany_id(), mjvdetail.getSuppliercr().getSupplier_id(), (long) 4,  (double)mjvdetail.getCramount(),(double) 0, (long) 0);
					
						
					}else if(mjvdetail.getSupplierdr()!=null){
						
						supplierService.addsuppliertrsansactionbalance(mjv.getAccounting_year_id().getYear_id(), mjv.getCreated_date(), mjv.getCompany().getCompany_id(), mjvdetail.getSupplierdr().getSupplier_id(), (long) 4, (double) 0, (double)mjvdetail.getDramount(), (long) 0);
						
					}else if(mjvdetail.getCustomerdr()!=null){
						
						customerService.addcustomertransactionbalance(mjv.getAccounting_year_id().getYear_id(), mjv.getCreated_date(), mjv.getCompany().getCompany_id(), mjvdetail.getCustomerdr().getCustomer_id(), (long) 5, (double) 0,(double)mjvdetail.getDramount(), (long) 0);
					}else if (mjvdetail.getCustomercr()!=null){
						
						customerService.addcustomertransactionbalance(mjv.getAccounting_year_id().getYear_id(), mjv.getCreated_date(), mjv.getCompany().getCompany_id(), mjvdetail.getCustomerdr().getCustomer_id(), (long) 5,(double)mjvdetail.getCramount(), (double) 0, (long) 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			 
			
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		return journalDao.deleteByIdValue(entityId);
	}
	@Override
	public String deleteByMJVIdValue(Long entityId) {
		// TODO Auto-generated method stub
		try {
			ManualJVDetails mjvdetail= journalDao.findOnemjvdetail(entityId);
			ManualJournalVoucher mjv=mjvdetail.getManualjournalvoucher();
			if(mjvdetail.getSubledgercr()!=null && mjv!=null)
			{
				subledgerService.addsubledgertransactionbalance(mjv.getAccounting_year_id().getYear_id(),mjv.getCreated_date(),mjv.getCompany().getCompany_id(),
					mjvdetail.getSubledgercr().getSubledger_Id(), (long) 2, (double)mjvdetail.getCramount(), (double) 0,
					(long) 0,null,null,null,null,null,null,null,mjv,null,null,null,null);
			}
		
			else if(mjvdetail.getSubledgerdr()!=null  && mjv!=null)
			{
				subledgerService.addsubledgertransactionbalance(mjv.getAccounting_year_id().getYear_id(),mjv.getCreated_date(),mjv.getCompany().getCompany_id(),
					mjvdetail.getSubledgerdr().getSubledger_Id(), (long) 2,  (double) 0,(double)mjvdetail.getDramount(),
					(long) 0,null,null,null,null,null,null,null,mjv,null,null,null,null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return journalDao.deleteByMJVIdValue(entityId);
				
	}
	

	@Override
	public void saveMJV(ManualJournalVoucher entity) 
	{
		
		
		Double amount  = 0.0d;
		try {
			entity.setAccounting_year_id(accountYearDAO.findOne(entity.getYear_id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		entity.setCreated_date(entity.getDate());
		Set<ManualJVDetails> mvjdetails = new HashSet<ManualJVDetails>();
		List<ManualJVDetails>  crmanualdetail= new ArrayList<ManualJVDetails>();
		List<ManualJVDetails>  drmanualdetail= new ArrayList<ManualJVDetails>();
	Long id = journalDao.saveMJV(entity);

    ManualJournalVoucher entry =null;

	try {
		entry = journalDao.findOne(id);
	} catch (MyWebException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	if(entity.getCr_ledgerlist() !=null &&  !entity.getCr_ledgerlist().equals(""))
	{
		
		JSONArray jsonArray = new JSONArray(entity.getCr_ledgerlist());
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ManualJVDetails mvjdetail = new ManualJVDetails();
			
			/*try {
				mvjdetail.setSubledgercr(SubLedgerDAO.findOne(Long.parseLong(jsonObject.getString("crledgerId"))));
			} catch (NumberFormatException | JSONException | MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			if(jsonObject.get("type").equals("sub")){
				
				try {
					mvjdetail.setSubledgercr(SubLedgerDAO.findOne(Long.parseLong(jsonObject.getString("crledgerId"))));
				} catch (NumberFormatException | JSONException | MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}else if(jsonObject.get("type").equals("sup")){
					
				
					try {
						mvjdetail.setSuppliercr(SupplireDAO.findOne(Long.parseLong(jsonObject.getString("crledgerId"))));
					} catch (NumberFormatException | JSONException | MyWebException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}else if(jsonObject.get("type").equals("cus")){
						
						try {
							mvjdetail.setCustomercr(CustomerDAO.findOne(Long.parseLong(jsonObject.getString("crledgerId"))));
						} catch (NumberFormatException | JSONException | MyWebException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
			mvjdetail.setCramount(Double.parseDouble(jsonObject.getString("cramountId")));
			amount=amount+Double.parseDouble(jsonObject.getString("cramountId"));
			mvjdetail.setDramount(0.0d);
			mvjdetail.setManualjournalvoucher(entry);
			mvjdetails.add(mvjdetail);	
			crmanualdetail.add(mvjdetail);
		}
	}
	
	
	if(entity.getDr_ledgerlist() !=null &&  !entity.getDr_ledgerlist().equals(""))
	{
		
		JSONArray jsonArray1 = new JSONArray(entity.getDr_ledgerlist());
		for (int i = 0; i < jsonArray1.length(); i++) {
			JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
			ManualJVDetails mvjdetail1 = new ManualJVDetails();
			
			if(jsonObject1.get("type").equals("sub")){
				
			try {
				mvjdetail1.setSubledgerdr(SubLedgerDAO.findOne(Long.parseLong(jsonObject1.getString("drledgerId"))));
			} catch (NumberFormatException | JSONException | MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}else if(jsonObject1.get("type").equals("sup")){
				
				try {
					mvjdetail1.setSupplierdr(SupplireDAO.findOne(Long.parseLong(jsonObject1.getString("drledgerId"))));
				} catch (NumberFormatException | JSONException | MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}else if(jsonObject1.get("type").equals("cus")){
					
					try {
						mvjdetail1.setCustomerdr(CustomerDAO.findOne(Long.parseLong(jsonObject1.getString("drledgerId"))));
					} catch (NumberFormatException | JSONException | MyWebException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
			mvjdetail1.setDramount(Double.parseDouble(jsonObject1.getString("dramountId")));
			mvjdetail1.setCramount(0.0d);
			mvjdetail1.setManualjournalvoucher(entry);
			mvjdetails.add(mvjdetail1);
			drmanualdetail.add(mvjdetail1);
    	}
	}

	
	for(int i=0;i<drmanualdetail.size();i++)
	{
		journalDao.saveOnemjvdetail(drmanualdetail.get(i));
	}
	
	for(int i=0;i<crmanualdetail.size();i++)
	{
		journalDao.saveOnemjvdetail(crmanualdetail.get(i));
	}

		
	for(ManualJVDetails detail:mvjdetails)
	{
		if(detail.getSubledgercr()!=null){
			
			subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getCreated_date(),entry.getCompany().getCompany_id(),
					detail.getSubledgercr().getSubledger_Id(), (long) 2, (double)detail.getCramount(), (double) 0, (long) 1,null,null,null,null,null,null,null,entry,null,null,null,null);
			
		}else if (detail.getSubledgerdr()!=null){
			
			subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getCreated_date(),entry.getCompany().getCompany_id(),
					detail.getSubledgerdr().getSubledger_Id(), (long) 2, (double) 0,(double)detail.getDramount(),  (long) 1,null,null,null,null,null,null,null,entry,null,null,null,null);
			
		}else if(detail.getSuppliercr()!=null){
			
		//	supplierService.addsuppliertrsansactionbalance(entry.getAccounting_year_id().getYear_id(), entry.getCreated_date(), entry.getCompany().getCompany_id(), detail.getSuppliercr().getSupplier_id(),(long) 2, (double) 0,(double)detail.getDramount(), (long) 1,null,null,null,null,null,null,null,entry,null,null,null,null);
			supplierService.addsuppliertrsansactionbalance(entry.getAccounting_year_id().getYear_id(), entry.getCreated_date(), entry.getCompany().getCompany_id(), detail.getSuppliercr().getSupplier_id(), (long) 4,  (double)detail.getCramount(),(double) 0, (long) 1);
		
			
		}else if(detail.getSupplierdr()!=null){
			
			supplierService.addsuppliertrsansactionbalance(entry.getAccounting_year_id().getYear_id(), entry.getCreated_date(), entry.getCompany().getCompany_id(), detail.getSupplierdr().getSupplier_id(), (long) 4, (double) 0, (double)detail.getDramount(), (long) 1);
			
		}else if(detail.getCustomerdr()!=null){
			
			customerService.addcustomertransactionbalance(entry.getAccounting_year_id().getYear_id(), entry.getCreated_date(), entry.getCompany().getCompany_id(), detail.getCustomerdr().getCustomer_id(), (long) 5, (double) 0,(double)detail.getDramount(), (long) 1);
		}else if (detail.getCustomercr()!=null){
			System.out.println(entry.getAccounting_year_id().getYear_id());
			System.out.println(entry.getCreated_date());
			System.out.println(entry.getCompany().getCompany_id());
			System.out.println(detail.getCustomerdr().getCustomer_id());
			System.out.println(detail.getCramount());
			
			customerService.addcustomertransactionbalance(entry.getAccounting_year_id().getYear_id(), entry.getCreated_date(), entry.getCompany().getCompany_id(), detail.getCustomerdr().getCustomer_id(), (long) 5,(double)detail.getCramount(), (double) 0, (long) 1);
		}
		/*if(detail.getCramount()!=null && detail.getCramount()>0  )
		{
			subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getCreated_date(),entry.getCompany().getCompany_id(),
					detail.getSubledgercr().getSubledger_Id(), (long) 2, (double)detail.getCramount(), (double) 0, (long) 1,null,null,null,null,null,null,null,entry,null,null,null,null);
		
		}
		else
		{
			subledgerService.addsubledgertransactionbalance(entry.getAccounting_year_id().getYear_id(),entry.getCreated_date(),entry.getCompany().getCompany_id(),
					detail.getSubledgerdr().getSubledger_Id(), (long) 2, (double) 0,(double)detail.getDramount(),  (long) 1,null,null,null,null,null,null,null,entry,null,null,null,null);
		
		}*/
	}
	
	entry.setAmount(amount);
	try {
		journalDao.update(entry);
	} catch (MyWebException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}

	@Override
	public CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany() {
		// TODO Auto-generated method stub
		return journalDao.findAllManualJournalVoucherReletedToCompany();
	}


	@Override
	public CopyOnWriteArrayList<ManualJournalVoucher> findAllManualJournalVoucherReletedToCompany(Long company_id) {
		// TODO Auto-generated method stub
		return journalDao.findAllManualJournalVoucherReletedToCompany(company_id);
	}

	@Override
	public CopyOnWriteArrayList<DepreciationAutoJV> findAllDepreciationJournalVoucherReletedToCompany(Long company_id,
			LocalDate fromDate, LocalDate toDate) {
		// TODO Auto-generated method stub
		return journalDao.findAllDepreciationJournalVoucherReletedToCompany(company_id,fromDate,toDate);
	}

	@Override
	public CopyOnWriteArrayList<GSTAutoJV> findAllGstJournalVoucherReletedToCompany(Long company_id, LocalDate fromDate,
			LocalDate toDate) {
		// TODO Auto-generated method stub
		return journalDao.findAllGstJournalVoucherReletedToCompany(company_id,fromDate,toDate);
	}
	@Override
	public CopyOnWriteArrayList<PayrollAutoJV> findAllPayrollJournalVoucherReletedToCompany(Long company_id,LocalDate fromDate,LocalDate toDate) {
		// TODO Auto-generated method stub
		return journalDao.findAllPayrollJournalVoucherReletedToCompany(company_id,fromDate,toDate);
	}
	
}
