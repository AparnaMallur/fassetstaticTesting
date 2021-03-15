/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IAccountSubGroupDAO;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IClientAllocationToKpoExecutiveDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.ILedgerDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Ledger;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IOpeningBalancesService;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Service
@Transactional
public class LedgerServiceImpl implements ILedgerService {
	
	@Autowired
	IClientAllocationToKpoExecutiveDAO clientAllocationToKpoExectiveDAO;

	@Autowired
	private ILedgerDAO ledgerDAO;

	@Autowired
	private ISubLedgerDAO subLedgerDao;

	@Autowired
	private IAccountSubGroupDAO SubGroupDAO;

	@Autowired
	private ICompanyDAO companyDAO;

	@Autowired
	private IOpeningBalancesService openingbalances;

	@Autowired
	private IAccountingYearDAO yearDAO;
	
	@Autowired
	private IUserDAO userDao;
	
	@Override
	public void add(Ledger entity) throws MyWebException {
		Long id = ledgerDAO.saveLedgerDao(entity);
		if (id != null) {
			Long year_id = yearDAO.findcurrentyear();
			/*if (year_id != 0) {
				if (entity.getCompany() != null) {
					Long opid = openingbalances.saveOpeningBalances(entity.getCompany_id(), year_id, id, (long) 1, (double) 0, (double) 0);
					if (opid != 0) {
						try {
							entity.setOpeningId(opid);
							entity.setOpeningbalances(openingbalances.getById(opid));
						} catch (MyWebException e) {
							e.printStackTrace();
						}
					}
				}
			}*/
			if (entity.getAs_subledger()) {
				
				SubLedger sub = subLedgerDao.isExists(entity.getLedger_name(), entity.getCompany_id());
				if(sub==null)
				{
				SubLedger subLedger = new SubLedger();
				subLedger.setSubledger_name(entity.getLedger_name());
				subLedger.setStatus(entity.getStatus());
				subLedger.setSubledger_approval(0);
				subLedger.setCreated_date(new LocalDate());
				subLedger.setCompany(companyDAO.findOne(entity.getCompany_id()));
				subLedger.setLedger(ledgerDAO.findOne(id));
				subLedger.setFlag(true);
				subLedger.setCreated_by(entity.getCreated_by());
				Long sid = subLedgerDao.createSubLedger(subLedger);
				if (sid != null) {

					/*if (year_id != 0) {
						Long opid = openingbalances.saveOpeningBalances(entity.getCompany_id(), year_id, sid, (long) 2, (double) 0, (double) 0);
						if (opid != 0) {
							try {
								subLedger.setOpeningId(opid);
								subLedger.setOpeningbalances(openingbalances.getById(opid));
							} catch (MyWebException e) {
								e.printStackTrace();
							}
						}
					}*/
				}
				
			   }
			}
		}
	}

	@Override
	@Transactional
	public void update(Ledger entity) throws MyWebException {
		Ledger ledger = null;
		Long year_id = yearDAO.findcurrentyear();
		try {
			ledger = ledgerDAO.findOne(entity.getLedger_id());
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		
		ledger.setUpdate_date(new LocalDate());
		ledger.setLedger_approval(entity.getLedger_approval());
		ledger.setFlag(entity.getFlag());
		if (ledger.getCompany() != null)
			try {
				ledger.setCompany(companyDAO.findOne(ledger.getCompany().getCompany_id()));
			} catch (MyWebException e) {
				e.printStackTrace();
			}
		else if (entity.getCompany_id() != null)
			try {
				ledger.setCompany(companyDAO.findOne(entity.getCompany_id()));
			} catch (MyWebException e) {
				e.printStackTrace();
			}
	
	
		/*if (ledger.getAs_subledger() != null) {
			
			if(ledger.getAs_subledger()==true && entity.getAs_subledger()==true)
			{
				SubLedger sub = subLedgerDao.isExists(ledger.getLedger_name(), entity.getCompany_id());
				if(sub!=null)
				{
				
					sub.setSubledger_name(entity.getLedger_name());
					sub.setStatus(entity.getStatus());
					sub.setCreated_date(new LocalDate());
					sub.setCompany(ledger.getCompany());
					try {
						sub.setLedger(ledgerDAO.findOne(entity.getLedger_id()));
					} catch (MyWebException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sub.setFlag(true);
					sub.setUpdate_date(new LocalDate());
					sub.setUpdated_by(entity.getUpdated_by());
					sub.setSubledger_approval(0);
					subLedgerDao.updateSubledger(sub);
				}
			}
			
			
			if(ledger.getAs_subledger()==false && entity.getAs_subledger()==true)
			{
				try
				{
				SubLedger sub = subLedgerDao.isExists(entity.getLedger_name(), entity.getCompany_id());
				if(sub==null)
				{
				SubLedger subLedger = new SubLedger();
				subLedger.setSubledger_name(entity.getLedger_name());
				subLedger.setStatus(entity.getStatus());
				subLedger.setSubledger_approval(0);
				subLedger.setCreated_date(new LocalDate());
				subLedger.setCompany(companyDAO.findOne(entity.getCompany_id()));
				subLedger.setLedger(ledgerDAO.findOne(entity.getLedger_id()));
				subLedger.setFlag(true);
				subLedger.setCreated_by(entity.getCreated_by());
				Long sid = subLedgerDao.createSubLedger(subLedger);
				if (sid != null) {

					if (year_id != 0) {
						Long opid = openingbalances.saveOpeningBalances(entity.getCompany_id(), year_id, sid, (long) 2, (double) 0, (double) 0);
						if (opid != 0) {
							try {
								subLedger.setOpeningId(opid);
								subLedger.setOpeningbalances(openingbalances.getById(opid));
							} catch (MyWebException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
			   }
			   }
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
		}
		*/
		ledger.setStatus(entity.getStatus());
		ledger.setAs_subledger(entity.getAs_subledger());
		
		String subname = entity.getLedger_name().replace("\"", "").replace("\'", "");
		ledger.setLedger_name(subname);
		try {
			ledger.setAccsubgroup(SubGroupDAO.findOne(entity.getSubgroup_Id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		try {
			ledgerDAO.update(ledger);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Ledger> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ledger getById(Long id) throws MyWebException {
		return ledgerDAO.findOne(id);
	}

	@Override
	public Ledger getById(String id) throws MyWebException {
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
	public void remove(Ledger entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Ledger entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public String saveLedger(Ledger ledger) {
		String msg = "";
		try {
			
			String subname = ledger.getLedger_name().replace("\"", "").replace("\'", "");
			ledger.setLedger_name(subname);
			
			ledger.setAccsubgroup(SubGroupDAO.findOne(ledger.getSubgroup_Id()));
			ledger.setCompany(companyDAO.findOne(ledger.getCompany_id()));
			ledger.setCreated_date(new LocalDate());
			Long id = ledgerDAO.saveLedgerDao(ledger);
			Long year_id = yearDAO.findcurrentyear();
			/*if (id != null) {
				if (year_id != 0) {
					Long opid = openingbalances.saveOpeningBalances(ledger.getCompany_id(), year_id, id, (long) 1, (double) 0, (double) 0);
					if (opid != 0) {
						try {
							ledger.setOpeningId(opid);
							ledger.setOpeningbalances(openingbalances.getById(opid));
						} catch (MyWebException e) {
							e.printStackTrace();
						}
					}
				}
			}*/
			if (ledger.getAs_subledger()) {
				SubLedger sub = subLedgerDao.isExists(ledger.getLedger_name(), ledger.getCompany_id());
				if(sub==null)
				{
				SubLedger subLedger = new SubLedger();
				subLedger.setSubledger_name(ledger.getLedger_name());
				subLedger.setStatus(ledger.getStatus());
				subLedger.setSubledger_approval(0);
				subLedger.setCreated_date(new LocalDate());
				subLedger.setCompany(companyDAO.findOne(ledger.getCompany_id()));
				subLedger.setLedger(ledgerDAO.findOne(id));
				subLedger.setFlag(true);
				subLedger.setCreated_by(ledger.getCreated_by());
				Long sid = subLedgerDao.createSubLedger(subLedger);
				if (sid != null) {

					/*if (year_id != 0) {
						Long opid = openingbalances.saveOpeningBalances(ledger.getCompany_id(), year_id, sid, (long) 2, (double) 0, (double) 0);
						if (opid != 0) {
							try {
								subLedger.setOpeningId(opid);
								subLedger.setOpeningbalances(openingbalances.getById(opid));
							} catch (MyWebException e) {
								e.printStackTrace();
							}
						}
					}*/
				}
				
			   }
			}
			if (id != null) {

				msg = " Ledger saved successfully";
			} else {
				msg = "Please try again ";
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public List<Ledger> findAll() {
		return ledgerDAO.findAll();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = ledgerDAO.deleteByIdValue(entityId);
		return msg;
	}

	@Override
	public List<Ledger> findByStatus(int status) {
		return ledgerDAO.findByStatus(status);
	}

	@Override
	public String updateByApproval(Long ledgerId, int status) {
		return ledgerDAO.updateApprovalStatus(ledgerId, status);
	}

	@Override
	public Ledger isExists(String name) {
		return ledgerDAO.isExists(name);
	}

	@Override
	public Ledger isExists(String name, Long company_id) {
		return ledgerDAO.isExists(name, company_id);
	}

	@Override
	public List<Ledger> findAllLedgersOfCompany(Long CompanyId) {
		return ledgerDAO.findAllLedgersOfCompany(CompanyId);
	}

	@Override
	public List<Ledger> findAllListing(Long flag) {
		return ledgerDAO.findAllListing(flag);
	}

	@Override
	public List<Ledger> findAllListingLedgersOfCompany(Long CompanyId, Long flag) {
		return ledgerDAO.findAllListingLedgersOfCompany(CompanyId, flag);
	}

	@Override
	public List<Ledger> findAllLedgersWithSubledger(Long company_id) {
		return ledgerDAO.findAllLedgersWithSubledger(company_id);
	}

	@Override
	public Ledger findOneWithAll(Long leId) {
		return ledgerDAO.findOneWithAll(leId);
	}

	@Transactional
	@Override
	public Ledger addledger(Long companyId, Ledger ledger) {
		Long id = ledgerDAO.isExistsforcompany(ledger.getLedger_name(), companyId);
		Ledger newledger = new Ledger();
		if (id == 0) {
			newledger.setLedger_name(ledger.getLedger_name());
			newledger.setLedger_approval(3);
			newledger.setFlag(true);
			newledger.setStatus(true);
			newledger.setAllocated(true);
			newledger.setCreated_date(new LocalDate());
			newledger.setCreated_by(ledger.getCreated_by());
			newledger.setAs_subledger(false);
			try {
				newledger.setAccsubgroup(SubGroupDAO.findOne(ledger.getAccsubgroup().getSubgroup_Id()));
				newledger.setCompany(companyDAO.findOne(companyId));
				Long lid = ledgerDAO.saveLedgerDao(newledger);
				if (lid != null) {
					Long year_id = yearDAO.findcurrentyear();
					if (year_id != 0) {
						Long opid = openingbalances.saveOpeningBalances(companyId, year_id, lid, (long) 1, (double) 0, (double) 0);
						if (opid != 0) {
							try {
								newledger.setOpeningId(opid);
								newledger.setOpeningbalances(openingbalances.getById(opid));
							} catch (MyWebException e) {
								e.printStackTrace();
							}
						}
					}
				}
			} catch (MyWebException e) {
				e.printStackTrace();
			}
		} else {
			newledger = ledgerDAO.findOneWithAll(id);
		}
		return newledger;

	}

	@Override
	public Ledger addledgerwithopening(Long companyId, Ledger ledger) {
		Long id = ledgerDAO.isExistsforcompany(ledger.getLedger_name(), companyId);
		System.out.println(id);
		Ledger newledger = new Ledger();
		if (id == 0) {
			newledger.setLedger_name(ledger.getLedger_name());
			newledger.setLedger_approval(3);
			newledger.setFlag(true);
			newledger.setStatus(true);
			newledger.setAllocated(true);
			newledger.setCreated_date(new LocalDate());
			newledger.setCreated_by(ledger.getCreated_by());
			newledger.setAs_subledger(false);
			try {
				newledger.setAccsubgroup(SubGroupDAO.findOne(ledger.getAccsubgroup().getSubgroup_Id()));
				newledger.setCompany(companyDAO.findOne(companyId));
				Long lid = ledgerDAO.saveLedgerDao(newledger);
				if (lid != null) {
					Long year_id = yearDAO.findcurrentyear();
					if (year_id != 0) {
						Long opid = openingbalances.saveOpeningBalances(companyId, year_id, lid, (long) 1, (double) 0, (double) 0);
						if (opid != 0) {
							try {
								newledger.setOpeningId(opid);
								newledger.setOpeningbalances(openingbalances.getById(opid));
							} catch (MyWebException e) {
								e.printStackTrace();
							}
						}
					}
				}
			} catch (MyWebException e) {
				e.printStackTrace();
			}
		} else {
			newledger = ledgerDAO.findOneWithAll(id);
		}
		return newledger;

	}

	@Transactional
	@Override
	public void addledgeropeningbalance(Long company_id, Long sids, Long type, Double creditval, Double debitval) {
	/*	Ledger ledger = new Ledger();
		try {
			ledger = ledgerDAO.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}*/
		Long year_id = yearDAO.findcurrentyear();
		if (year_id != 0) {
			/*Long opid = (long) 0;*/
			Long existid = openingbalances.findsubledgerbalance(company_id, year_id, sids, (long) 1);
			if (existid == 0) {
			/*	opid = */openingbalances.saveOpeningBalances(company_id, year_id, sids, (long) 1, creditval, debitval);
			} else {
			/*	opid =*/ openingbalances.updatepeningbalance(existid, creditval, debitval);
			}
			/*if (opid != 0) {
				try {
					ledger.setOpeningId(opid);
					ledger.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
		}
	}

	@Transactional
	@Override
	public void addledgertransactionbalance(Long year_id, LocalDate date, Long company_id, Long sids, Long type, Double creditval, Double debitval,
			Long flag) {
		/*Ledger ledger = new Ledger();
		try {
			ledger = ledgerDAO.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}*/
		//Long year_id = yearDAO.findcurrentyear();
		if (year_id != 0) {
			/*Long opid = (long) 0;*/
			Long existid = openingbalances.findsubledgerbalancebydate(company_id, year_id, sids, (long) 1,date,null,null,null,null,null,null,null,null,null,null,null,null);
			if (existid == 0) {
				Long opid = openingbalances.saveOpeningBalancesbydate(date,company_id, year_id, sids, (long) 1, creditval, debitval,null,null,null,null,null,null,null,null,null,null,null,null);
			System.out.println("Ledger id is "+opid);
			} else {
				/*opid =*/ openingbalances.updateCDbalance(existid, creditval, debitval, flag);
			}
			/*if (opid != 0) {
				try {
					ledger.setOpeningId(opid);
					ledger.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
		}
	}

	@Override
	public Boolean approvedByBatch(List<String> ledgerList, Boolean primaryApproval) {
		return ledgerDAO.approvedByBatch(ledgerList, primaryApproval);
	}

	@Override
	public Boolean rejectByBatch(List<String> ledgerList) {
		return ledgerDAO.rejectByBatch(ledgerList);
	}

	@Override
	public int isExistsledger(String ledger_name, Long company_id) {
		int opid = ledgerDAO.isExistsledger(ledger_name, company_id);
		return opid;
	}

	@Override
	public void updateExcel(Ledger entity,Long id) {
		Ledger ledger = null;
		Long year_id = yearDAO.findcurrentyear();
		try {
			ledger = ledgerDAO.findOne(id);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		
		ledger.setUpdate_date(new LocalDate());
		ledger.setLedger_approval(0);
		ledger.setFlag(entity.getFlag());
		if (ledger.getCompany() != null)
			try {
				ledger.setCompany(companyDAO.findOne(ledger.getCompany().getCompany_id()));
			} catch (MyWebException e) {
				e.printStackTrace();
			}
		else if (entity.getCompany_id() != null)
			try {
				ledger.setCompany(companyDAO.findOne(entity.getCompany_id()));
			} catch (MyWebException e) {
				e.printStackTrace();
			}
	
	
	/*	if (ledger.getAs_subledger() != null) {
			
			if(ledger.getAs_subledger()==true && entity.getAs_subledger()==true)
			{
				SubLedger sub = subLedgerDao.isExists(ledger.getLedger_name(), entity.getCompany_id());
				if(sub!=null)
				{
				
					sub.setSubledger_name(entity.getLedger_name());
					sub.setStatus(entity.getStatus());
					sub.setCreated_date(new LocalDate());
					sub.setCompany(ledger.getCompany());
					try {
						sub.setLedger(ledgerDAO.findOne(entity.getLedger_id()));
					} catch (MyWebException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sub.setFlag(true);
					sub.setUpdate_date(new LocalDate());
					sub.setUpdated_by(entity.getUpdated_by());
					sub.setSubledger_approval(0);
					subLedgerDao.updateSubledger(sub);
				}
			}
			
			
			if(ledger.getAs_subledger()==false && entity.getAs_subledger()==true)
			{
				try
				{
				SubLedger sub = subLedgerDao.isExists(entity.getLedger_name(), entity.getCompany_id());
				if(sub==null)
				{
				SubLedger subLedger = new SubLedger();
				subLedger.setSubledger_name(entity.getLedger_name());
				subLedger.setStatus(entity.getStatus());
				subLedger.setSubledger_approval(0);
				subLedger.setCreated_date(new LocalDate());
				subLedger.setCompany(companyDAO.findOne(entity.getCompany_id()));
				subLedger.setLedger(ledgerDAO.findOne(entity.getLedger_id()));
				subLedger.setFlag(true);
				subLedger.setCreated_by(entity.getCreated_by());
				Long sid = subLedgerDao.createSubLedger(subLedger);
				if (sid != null) {

					if (year_id != 0) {
						Long opid = openingbalances.saveOpeningBalances(entity.getCompany_id(), year_id, sid, (long) 2, (double) 0, (double) 0);
						if (opid != 0) {
							try {
								subLedger.setOpeningId(opid);
								subLedger.setOpeningbalances(openingbalances.getById(opid));
							} catch (MyWebException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
			   }
			   }
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
		}*/
		
		ledger.setStatus(entity.getStatus());
		ledger.setAs_subledger(entity.getAs_subledger());
		
		
		ledger.setLedger_name(entity.getLedger_name());
		
		try {
			ledger.setAccsubgroup(SubGroupDAO.findOne(entity.getSubgroup_Id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		try {
			if(ledger.getAllocated()==null)
			{
			ledgerDAO.update(ledger);
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Ledger> findAllLedgersWithSubledger() {
		return ledgerDAO.findAllLedgersWithSubledger();
	}

	@Override
	public List<Ledger> findLedgersOnlyOfComapany(Long company_id) {
		return ledgerDAO.findLedgersOnlyOfComapany(company_id);
	}

	@Override
	public List<Ledger> findAllListingExe(Boolean flag, Long userId) {
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(userId);
		List<Long> companyIds = new ArrayList<Long>();
		if(executiveList .isEmpty()){
			companyIds.add((long) 0);
		}
		else
		{			for(ClientAllocationToKpoExecutive com : executiveList ){
				companyIds.add(com.getCompany().getCompany_id());
			}
		}
		return ledgerDAO.findAllListingExe(flag, companyIds);
	}

	@Override
	public List<Ledger> findByStatus(Long role_id,int status, Long userId) {
		
		if(role_id.equals(MyAbstractController.ROLE_EXECUTIVE))
		{
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(userId);
		List<Long> companyIds = new ArrayList<Long>();
		if(executiveList .isEmpty()){
			companyIds.add((long) 0);
		}
		else
		{	
			for(ClientAllocationToKpoExecutive com : executiveList ){
				companyIds.add(com.getCompany().getCompany_id());
			}
		}
		return ledgerDAO.findByStatus(status, companyIds);
		}
		else
		{
			List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findAllCompaniesUnderExecutive(userId);
			List<Long> companyIds = new ArrayList<Long>();
			if(executiveList .isEmpty()){
				companyIds.add((long) 0);
			}
			else
			{	
				for(ClientAllocationToKpoExecutive com : executiveList ){
					companyIds.add(com.getCompany().getCompany_id());
				}
			}
			return ledgerDAO.findByStatus(status, companyIds);
		}
	}

	@Override
	public Ledger findOne(Long ledger_id) throws MyWebException {
		// TODO Auto-generated method stub
		return ledgerDAO.findOne(ledger_id);
	}

	@Transactional
	@Override
	public void addledgeropeningbalancenew(Long company_id, Long sids, long l, Double creditval, Double debitval,
			Long year, String date) {
		Ledger ledger = new Ledger();
		try {
			ledger = ledgerDAO.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		//Long year_id = yearDAO.findcurrentyear();
		if (year != 0) {
			/*Long opid = (long) 0;
			Long existid = openingbalances.findsubledgerbalance(company_id, year, sids, (long) 1);
			if (existid == 0) {
				opid = openingbalances.saveOpeningBalancesnew(company_id, year, sids, (long) 1, creditval, debitval,date);
			} else {
				opid = openingbalances.updatepeningbalancenew(existid, creditval, debitval,date,year);
			}
			if (opid != 0) {
				try {
					ledger.setOpeningId(opid);
					ledger.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
			LocalDate date1= new LocalDate(date);
			Long opid = (long) 0;
			Long existid = openingbalances.findsubledgerbalancebydate(company_id, year, sids, (long) 1,date1,null,null,null,null,null,null,null,null,null,null,null,null);
			
			if (existid == 0) {
				opid = openingbalances.saveOpeningBalancesnew(company_id, year, sids, (long) 1, creditval, debitval,date);
			} else {
				opid = openingbalances.updatepeningbalancenew(existid, creditval, debitval,date,year);
			}
			if (opid != 0) {
				try {
					ledger.setOpeningId(opid);
					ledger.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	/*@Override
	public List<LedgerReport> getLedgerReportforOpeningBalance(LedgerReportForm ledgerReportForm) {
	
		List<Payment> advancepayments = new ArrayList<>();
	    List<Receipt>  advancereceipts = new ArrayList<>();
	    Set<Payment> payments = new HashSet<>();
		Set<Receipt> receipts = new HashSet<>();
		
		advancepayments.clear();
		advancereceipts.clear();
		payments.clear();
		receipts.clear();
		
		List<LedgerReport> ledgerReports = ledgerDAO.getLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId(),ledgerReportForm.getCustomerId(),ledgerReportForm.getSupplierId());
		
		for (LedgerReport ledgerReport : ledgerReports) {
			if (ledgerReport.getType().equals("Purchase")) {
				
				if(ledgerReport.getPrimary_id()==null)// for advance payment with purchase id = null
				{
					advancepayments = paymentDao.getAdvancePaymentListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId());
					for(Payment pay :advancepayments)
					{
						if(pay.getSupplier_bill_no()==null)
						{
						payments.add(pay);
						}
					}
					ledgerReport.setPayments(payments);
				}
				else
				{
				ledgerReport.setDebitNotes(purchaseEntryDao.findOneWithAll(Long.parseLong(ledgerReport.getPrimary_id())).getDebitNote());
				try {
					payments = purchaseEntryDao.findOneWithAll(Long.parseLong(ledgerReport.getPrimary_id())).getPayments();
					
					if(advancepayments.size()==0)// for advance payment with with purchase id = null
					{
						advancepayments = paymentDao.getAdvancePaymentListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId());
						for(Payment pay :advancepayments)
						{
							if(pay.getSupplier_bill_no()==null)
							{
							payments.add(pay);
							}
						}
					}
					ledgerReport.setPayments(payments);
					ledgerReport.setSupplier(supplierSevice.getById(Long.parseLong(ledgerReport.getCustomer_name_id())));
				
					
				} catch (NumberFormatException | MyWebException e) {
					e.printStackTrace();
				}
				}
			} else if (ledgerReport.getType().equals("Sales")) {
				
				if(ledgerReport.getPrimary_id()==null)// for advance receipt with sales_id = null
				{
					advancereceipts = receiptDao.getAdvanceReceiptListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId());
					for(Receipt rec : advancereceipts)
					{
						if(rec.getSales_bill_id()==null)
						{
							receipts.add(rec);
						}
					}
					ledgerReport.setReceipts(receipts);
				}
				else
				{
				ledgerReport.setCreditNotes(salesEntryDao.findOneWithAll(Long.parseLong(ledgerReport.getPrimary_id())).getCreditNote());
				try {
					
					receipts = salesEntryDao.findOneWithAll(Long.parseLong(ledgerReport.getPrimary_id())).getReceipts();
					
					if(advancereceipts.size()==0)// for advance receipt with sales_id = null
					{
						advancereceipts = receiptDao.getAdvanceReceiptListForLedgerReport(ledgerReportForm.getFromDate(), ledgerReportForm.getToDate(), ledgerReportForm.getCompanyId());
						for(Receipt rec : advancereceipts)
						{
							if(rec.getSales_bill_id()==null)
							{
								receipts.add(rec);
							}
						}
					}
					ledgerReport.setReceipts(receipts);
					
					if (ledgerReport.getCustomer_name_id().equalsIgnoreCase("0")) {

					} else {
						ledgerReport.setCustomer(customerService.getById(Long.parseLong(ledgerReport.getCustomer_name_id())));
					}
					
					if(ledgerReport.getBank_id()!=null)
					{
						ledgerReport.setBank(bankDao.findOne(ledgerReport.getBank_id()));
					}
					
				} catch (NumberFormatException | MyWebException e) {
					e.printStackTrace();
				}
				}
			}
		}
		return ledgerReports;
	}*/

	@Override
	public Integer findAllLedgersOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		return ledgerDAO.findAllLedgersOfCompanyDashboard(companyId);
	}

	@Override
	public Integer findByStatusDashboard(Long role_id,int approvalStatus, Long user_id) {
		
		if(role_id.equals(MyAbstractController.ROLE_EXECUTIVE))
		{
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(user_id);
		List<Long> companyIds = new ArrayList<Long>();
		if(executiveList .isEmpty()){
			companyIds.add((long) 0);
		}
		else
		{	
			for(ClientAllocationToKpoExecutive com : executiveList ){
				companyIds.add(com.getCompany().getCompany_id());
			}
		}
		return ledgerDAO.findByStatusDashboard(approvalStatus, companyIds);
		}
		else
		{
			List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findAllCompaniesUnderExecutive(user_id);
			List<Long> companyIds = new ArrayList<Long>();
			if(executiveList .isEmpty()){
				companyIds.add((long) 0);
			}
			else
			{	
				for(ClientAllocationToKpoExecutive com : executiveList ){
					companyIds.add(com.getCompany().getCompany_id());
				}
			}
			return ledgerDAO.findByStatusDashboard(approvalStatus, companyIds);
		}
	}

	@Override
	public Integer findAllDashboard() {
		// TODO Auto-generated method stub
		return ledgerDAO.findAllDashboard();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ViewApprovalsForm> viewApprovals(Long role_id,Long userId) {
		
		List<ViewApprovalsForm> approvallist = new ArrayList<ViewApprovalsForm>();	
		
		if(role_id.equals(MyAbstractController.ROLE_EXECUTIVE))
		{
		List<Ledger> list = new ArrayList<>();	
		
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(userId);
		List<Long> companyIds = new ArrayList<Long>();
		if(executiveList .isEmpty()){
			companyIds.add((long) 0);
		}
		else
		{	
			for(ClientAllocationToKpoExecutive com : executiveList ){
				companyIds.add(com.getCompany().getCompany_id());
			}
		}
		list = ledgerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
		if(list.size()>0)
		{
			try {
				User user = userDao.findOne(userId);
				ViewApprovalsForm form = new  ViewApprovalsForm();
				form.setLedgerList(list);
				form.setFirst_name(user.getFirst_name());
				form.setLast_name(user.getLast_name());
				approvallist.add(form);
			} catch (MyWebException e) {
				e.printStackTrace();
			}
			
		}
		}
		else if(role_id.equals(MyAbstractController.ROLE_MANAGER))
		{
			List<User> userlist = userDao.findAllExecutiveOfManager(userId);
			if(userlist.size()>0)
			{
			for(User user :userlist)
			{
				List<Ledger> list = new ArrayList<>();	
				
				List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(user.getUser_id());
				List<Long> companyIds = new ArrayList<Long>();
				if(executiveList .isEmpty()){
					companyIds.add((long) 0);
				}
				else
				{	
					for(ClientAllocationToKpoExecutive com : executiveList ){
						companyIds.add(com.getCompany().getCompany_id());
					}
				}
				list = ledgerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
				if(list.size()>0)
				{
					try {
						ViewApprovalsForm form = new  ViewApprovalsForm();
						form.setLedgerList(list);
						form.setFirst_name(user.getFirst_name());
						form.setLast_name(user.getLast_name());
						approvallist.add(form);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			}
		}
		else if(role_id.equals(MyAbstractController.ROLE_SUPERUSER))
		{
			List<User> kpoList = userDao.getManagerAndExecutive();
			
			if(kpoList.size()>0)
			{
				for(User user : kpoList)
				{
					if(user.getRole()!=null && user.getRole().getRole_id().equals(MyAbstractController.ROLE_EXECUTIVE))
					{
						List<Ledger> list = new ArrayList<>();	
						
						List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(user.getUser_id());
						List<Long> companyIds = new ArrayList<Long>();
						if(executiveList .isEmpty()){
							companyIds.add((long) 0);
						}
						else
						{	
							for(ClientAllocationToKpoExecutive com : executiveList ){
								companyIds.add(com.getCompany().getCompany_id());
							}
						}
						list = ledgerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setLedgerList(list);
								form.setFirst_name(user.getFirst_name());
								form.setLast_name(user.getLast_name());
								approvallist.add(form);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}
					else if(user.getRole()!=null && user.getRole().getRole_id().equals(MyAbstractController.ROLE_MANAGER))
					{
                        List<Ledger> list = new ArrayList<>();	
						
						List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(user.getUser_id());
						List<Long> companyIds = new ArrayList<Long>();
						if(executiveList .isEmpty()){
							companyIds.add((long) 0);
						}
						else
						{	
							for(ClientAllocationToKpoExecutive com : executiveList ){
								companyIds.add(com.getCompany().getCompany_id());
							}
						}
						list = ledgerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PRIMARY, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setLedgerList(list);
								form.setFirst_name(user.getFirst_name());
								form.setLast_name(user.getLast_name());
								approvallist.add(form);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}
				}
			}
		}
		
		return approvallist;
	}
}
