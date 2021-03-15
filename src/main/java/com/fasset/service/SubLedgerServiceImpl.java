/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.PurchaseEntryDAOImpl;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IClientAllocationToKpoExecutiveDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.ILedgerDAO;
import com.fasset.dao.interfaces.IPurchaseEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.dao.interfaces.IindustryTypeDAO;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Company;
import com.fasset.entities.Contra;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.Ledger;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.Payment;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.entities.YearEndJV;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.ISubLedgerService;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Service
@Transactional
public class SubLedgerServiceImpl implements ISubLedgerService {
	
	@Autowired
	IClientAllocationToKpoExecutiveDAO clientAllocationToKpoExectiveDAO;

	@Autowired
	private ISubLedgerDAO subLedgerDAO;
	@Autowired
	private IPurchaseEntryService p;

	@Autowired
	private ICompanyDAO companyDAO;

	@Autowired
	private ILedgerDAO ledgerDAO;

	@Autowired
	private ILedgerService ledgerService;

	@Autowired
	private IOpeningBalancesService openingbalances;
	

	@Autowired
	private IAccountingYearDAO yearDAO;

	@Autowired
	private IindustryTypeDAO industryTypeDao;
	
	@Autowired
	private IUserDAO userDao;
	
	@Autowired
	private ICommonService commonservice;

	@Autowired
	private ICompanyService companyService;
	
	
	@Override
	public void add(SubLedger entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(SubLedger entity) throws MyWebException {
		entity.setSetDefault(entity.getSetDefault());
		entity.setUpdate_date(new LocalDate());
		entity.setCompany(companyDAO.findOne(entity.getCompany_id()));
		String subname = entity.getSubledger_name().replace("\"", "").replace("\'", "");
		entity.setSubledger_name(subname);
		subLedgerDAO.update(entity);
	}

	@Override
	public List<SubLedger> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubLedger getById(Long id) throws MyWebException {
		return subLedgerDAO.findOne(id);
	}

	@Override
	public SubLedger getById(String id) throws MyWebException {
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
	public void remove(SubLedger entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(SubLedger entity) throws MyWebException {
		// TODO Auto-generated method stub
	}

	@Override
	public String saveSubLedger(SubLedger subLedger) {
		String subname = subLedger.getSubledger_name().replace("\"", "").replace("\'", "");
		subLedger.setSubledger_name(subname);
		
		Long id = subLedgerDAO.saveSubLedgerDao(subLedger);

		if (id != null) {
			/*Long year_id = yearDAO.findcurrentyear();
			if (year_id != 0) {
				Long opid = openingbalances.saveOpeningBalances(subLedger.getCompany_id(), year_id, id, (long) 2,
						(double) 0, (double) 0);
				if (opid != 0) {
					try {
						subLedger.setOpeningId(opid);
						subLedger.setOpeningbalances(openingbalances.getById(opid));
					} catch (MyWebException e) {
						e.printStackTrace();
					}
				}
			}*/
			return " Sub Ledger saved successfully";
		} else {
			return "Please try again ";
		}
	}

	@Transactional
	@Override
	public String saveExcelSubLedger(SubLedger subLedger) {
		Long id = subLedgerDAO.saveSubLedgerDao(subLedger);

		if (id != null) {
			/*Long year_id = yearDAO.findcurrentyear();
			if (year_id != 0) {
				if (subLedger.getCompany() != null) {
					Long opid = openingbalances.saveOpeningBalances(subLedger.getCompany_id(), year_id, id, (long) 2, (double) 0, (double) 0);
					if (opid != 0) {
						try {
							subLedger.setOpeningId(opid);
							subLedger.setOpeningbalances(openingbalances.getById(opid));
						} catch (MyWebException e) {
							e.printStackTrace();
						}
					}
				}
			}*/

			return " Sub Ledger saved successfully";
		} else {
			return "Please try again ";
		}
	}

	@Override
	public List<SubLedger> findAll() {
		return subLedgerDAO.findAll();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = subLedgerDAO.deleteByIdValue(entityId);
		return msg;
	}

	@Override
	public SubLedger isExists(String name) {
		return subLedgerDAO.isExists(name);
	}

	@Override
	public SubLedger isExists(String name, Long company_id) {
		return subLedgerDAO.isExists(name, company_id);
	}

	@Override
	public List<SubLedger> getSubledgerByGroup(String groupName, Long companyId) {
		return subLedgerDAO.getSubledgerByGroup(groupName, companyId);
	}

	@Override
	public List<SubLedger> findByStatus(int status) {
		return subLedgerDAO.findByStatus(status);
	}

	@Override
	public String updateByApproval(Long subLedgerId, int status) {
		return subLedgerDAO.updateApprovalStatus(subLedgerId, status);
	}

	@Override
	public List<SubLedger> findAllSubLedgersListing(Long flag) {
		return subLedgerDAO.findAllSubLedgersListing(flag);
	}

	@Override
	public List<SubLedger> findAllSubLedgersListingOfCompany(Long CompanyId, Long flag) {
		return subLedgerDAO.findAllSubLedgersListingOfCompany(CompanyId, flag);
	}

	@Override
	public List<SubLedger> findAllApprovedByCompany(Long CompanyId) {
		return subLedgerDAO.findAllSubLedgersOfCompany(CompanyId);
	}

	@Override
	public List<SubLedger> findAllApprovedByCompanywithdefault(Long CompanyId) {
		return subLedgerDAO.findAllSubLedgersOfCompanywithdefault(CompanyId);
	}

	@Override
	public List<SubLedger> findAllSubLedgerWithLedger() {
		return subLedgerDAO.findAllSubLedgerWithLedger();
	}

	@Override
	public SubLedger findOneWithAll(Long subId) {
		return subLedgerDAO.findOneWithAll(subId);
	}

	public void setdefaultdata(Long CompanyId) {
		List<SubLedger> sblist = new ArrayList<SubLedger>();
		sblist = subLedgerDAO.setdefaultdata();
		for (int i = 0; i < sblist.size(); i++) {
			SubLedger information = new SubLedger();
			SubLedger newsubledger = new SubLedger();

			information = sblist.get(i);
			Ledger ledger = new Ledger();
			Long id1 = ledgerDAO.isExistsforcompany(information.getLedger().getLedger_name(), CompanyId);
			
			if(id1.equals((long)0))
			{
				ledger = ledgerService.addledger(CompanyId, information.getLedger());
			}
			else
			{
				try {
					ledger = ledgerService.getById(id1);
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			newsubledger.setSubledger_name(information.getSubledger_name());
			newsubledger.setLedger_id(ledger.getLedger_id());
			newsubledger.setFlag(true);
			newsubledger.setStatus(true);
			newsubledger.setSubledger_approval(3);
			newsubledger.setCreated_date(new LocalDate());
			newsubledger.setCreated_by(information.getCreated_by());
			newsubledger.setAllocated(true);
			newsubledger.setSetDefault(true);
			try {
				newsubledger.setCompany(companyDAO.findOne(CompanyId));
				newsubledger.setLedger(ledger);
			} catch (MyWebException e) {
				e.printStackTrace();
			}
			newsubledger.setCompany_id(CompanyId);
			
			SubLedger subledger = subLedgerDAO.findOne(information.getSubledger_name(),CompanyId);
			if(subledger==null)
			{
				try {
					Long id = subLedgerDAO.saveSubLedgerDao(newsubledger);
					if (id != 0) {
						Long year_id = yearDAO.findcurrentyear();
						if (year_id != 0) {
							Long opid = openingbalances.saveOpeningBalances(newsubledger.getCompany_id(), year_id, id, (long) 2, (double) 0, (double) 0);
							if (opid != 0) {
								newsubledger.setOpeningId(opid);
								newsubledger.setOpeningbalances(openingbalances.getById(opid));
							}
						}
					}

				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}
			

		}

	}

	@Transactional
	public void createsubledgerwithopening(Long CompanyId, Long subledgerid, Long type, Double credit, Double debit) {
		SubLedger newsubledger = new SubLedger();
		SubLedger infosubledger = new SubLedger();

		Ledger ledger = new Ledger();
		try {
			infosubledger = subLedgerDAO.findOne(subledgerid);
			ledger = ledgerDAO.findOne(infosubledger.getLedger().getLedger_id());
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		ledger = ledgerService.addledgerwithopening(CompanyId, ledger);
		newsubledger.setSubledger_name(infosubledger.getSubledger_name());
		newsubledger.setLedger_id(ledger.getLedger_id());
		newsubledger.setFlag(true);
		newsubledger.setStatus(true);
		newsubledger.setSubledger_approval(3);
		newsubledger.setCreated_date(new LocalDate());
		newsubledger.setCreated_by(infosubledger.getCreated_by());
		newsubledger.setAllocated(true);
		try {
			newsubledger.setCompany(companyDAO.findOne(CompanyId));
			newsubledger.setLedger(ledgerDAO.findOne(newsubledger.getLedger_id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		newsubledger.setCompany_id(CompanyId);
		try {
			Long id = subLedgerDAO.saveSubLedgerDao(newsubledger);
			if (id != 0) {
				Long year_id = yearDAO.findcurrentyear();
				if (year_id != 0) {
					Long opid = openingbalances.saveOpeningBalances(CompanyId, year_id, id, type, credit, debit);
					if (opid != 0) {
						newsubledger.setOpeningId(opid);
						newsubledger.setOpeningbalances(openingbalances.getById(opid));
					}
				}
			}

		} catch (MyWebException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	@Override
	public void addsubledgeropeningbalance(Long company_id, Long sids, Long type, Double creditval, Double debitval) {
		SubLedger subledger = new SubLedger();
		Ledger ledger = new Ledger();
		try {
			subledger = subLedgerDAO.findOne(sids);
			ledger = subledger.getLedger();

		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		Long year_id = yearDAO.findcurrentyear();
		if (year_id != 0) {
			/*Long opid = (long) 0;*/
			Long existid = openingbalances.findsubledgerbalance(company_id, year_id, sids, (long) 2);
			if (existid == 0) {
				/*opid =*/ openingbalances.saveOpeningBalances(company_id, year_id, sids, (long) 2, creditval, debitval);
			} else {
				/*opid =*/ openingbalances.updatepeningbalance(existid, creditval, debitval);
			}
		/*	if (opid != 0) {
				try {
					subledger.setOpeningId(opid);
					subledger.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
			ledgerService.addledgeropeningbalance(company_id, ledger.getLedger_id(), (long) 1, creditval, debitval);
		}
	}

	@Transactional
	@Override
	public void addsubledgertransactionbalance(Long year_id,LocalDate date,Long company_id, Long sids, Long type, Double creditval, Double debitval, Long flag,SalesEntry sales,Receipt receipt,PurchaseEntry purchase,Payment payment,CreditNote credit,DebitNote debit,Contra contra,ManualJournalVoucher mjv,PayrollAutoJV payAutoJv,GSTAutoJV gstAutoJV,DepreciationAutoJV depriAutoJV,YearEndJV yearEndJV) {
		SubLedger subledger = new SubLedger();
		Ledger ledger = new Ledger();
		if (ledger != null) 
		{
		try {
			
			subledger = subLedgerDAO.findOne(sids);
			ledger = subledger.getLedger();

		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		}
		//Long year_id = yearDAO.findcurrentyear();
		if (year_id != 0) {
			
			/*Long opid = (long) 0;*/
			Long existid = openingbalances.findsubledgerbalancebydate(company_id, year_id, sids, (long) 2,date,sales,receipt,purchase,payment,credit,debit,contra,mjv,payAutoJv,gstAutoJV,depriAutoJV,yearEndJV);
			
			if (existid == 0) {
				
							try {
					if(purchase!=null){
						
						
					PurchaseEntry a=p.getById(purchase.getPurchase_id())    ;
					System.out.println("The purchaseId still "+a.getPurchase_id());}
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
							System.out.println("saving op balance for cash" );
							if(receipt!=null){System.out.println("saving op balance for cash"  +receipt.getReceipt_id());}
				Long opid = openingbalances.saveOpeningBalancesbydate(date,company_id, year_id, sids, (long) 2, creditval, debitval,sales,receipt,purchase,payment,credit,debit,contra,mjv,payAutoJv,gstAutoJV,depriAutoJV,yearEndJV);
			System.out.println("subledgerId id is "+opid);
			} else {
				System.out.println("updateing op balance for cash");
				System.out.println("creditval is "+creditval);
				System.out.println("debit is "+debitval);
				/*opid = */openingbalances.updateCDbalance(existid, creditval, debitval, flag);
			}
		/*	if (opid != 0) {
				try {
					subledger.setOpeningId(opid);
					subledger.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
			ledgerService.addledgertransactionbalance(year_id,date,company_id, ledger.getLedger_id(), (long) 1, creditval, debitval, flag);
		}
	}

	@Override
	public Boolean approvedByBatch(List<String> subledgerList, Boolean primaryApproval) {
		return subLedgerDAO.approvedByBatch(subledgerList, primaryApproval);
	}

	@Override
	public Boolean rejectByBatch(List<String> subledgerList) {
		return subLedgerDAO.rejectByBatch(subledgerList);
	}

	@Override
	public int isExistssubledger(String subledger_name, Long company_id) {
		int opid = subLedgerDAO.isExistssubledger(subledger_name, company_id);
		return opid;
	}

	@Override
	public void setindustrydata(long CompanyId) {
		List<SubLedger> sblist = new ArrayList<SubLedger>();

		try {
			Company comp = companyDAO.findOne(CompanyId);
			Set<SubLedger> subLedgers = industryTypeDao.findOneWithAll(comp.getIndustry_type().getIndustry_id()).getSubLedgers();

			sblist = new ArrayList<SubLedger>(subLedgers);

		} catch (MyWebException e2) {
			e2.printStackTrace();
		}

		System.out.println(sblist);
		for (int i = 0; i < sblist.size(); i++) {
			SubLedger information = new SubLedger();
			SubLedger newsubledger = new SubLedger();

			information = sblist.get(i);
			Ledger ledger = new Ledger();
			Long id1 = ledgerDAO.isExistsforcompany(information.getLedger().getLedger_name(), CompanyId);
			
			if(id1.equals((long)0))
			{
				ledger = ledgerService.addledger(CompanyId, information.getLedger());
			}
			else
			{
				try {
					ledger = ledgerService.getById(id1);
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			newsubledger.setSubledger_name(information.getSubledger_name());
			newsubledger.setLedger_id(ledger.getLedger_id());
			newsubledger.setFlag(true);
			newsubledger.setStatus(true);
			newsubledger.setSubledger_approval(3);
			newsubledger.setCreated_date(new LocalDate());
			newsubledger.setCreated_by(information.getCreated_by());
			newsubledger.setAllocated(true);
			try {
				newsubledger.setCompany(companyDAO.findOne(CompanyId));
				newsubledger.setLedger(ledger);
			} catch (MyWebException e) {
				e.printStackTrace();
			}
			newsubledger.setCompany_id(CompanyId);
			
			SubLedger subledger = subLedgerDAO.findOne(information.getSubledger_name(),CompanyId);
			if(subledger==null)
			{
			try {
				Long id = subLedgerDAO.saveSubLedgerDao(newsubledger);
				if (id != 0) {
					Long year_id = yearDAO.findcurrentyear();
					if (year_id != 0) {
						Long opid = openingbalances.saveOpeningBalances(newsubledger.getCompany_id(), year_id, id, (long) 2, (double) 0, (double) 0);
						if (opid != 0) {
							newsubledger.setOpeningId(opid);
							newsubledger.setOpeningbalances(openingbalances.getById(opid));
						}
					}
				}

			} catch (MyWebException e) {
				e.printStackTrace();
			}
			
			}
			
			
			
		}

	}

	@Override
	public Set<SubLedger> findAllSubLedgerWithRespectToLedger(Long ledgerId) {
		return subLedgerDAO.findAllSubLedgerWithRespectToLedger(ledgerId);
	}

	@Override
	public List<SubLedger> findAllSubLedgersListing(Boolean flag, Long userId) {
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
		return subLedgerDAO.findAllSubLedgersListing(flag, companyIds);
	}

	@Override
	public List<SubLedger> findByStatus(Long role_id,int status, Long userId) {
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
		return subLedgerDAO.findByStatus(status, companyIds);
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
			return subLedgerDAO.findByStatus(status, companyIds);
		}
	}

	@Override
	public List<SubLedger> findAllSubLedgerWithLedger(Long userId) {
		System.out.println("The user Id is " + userId);
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(userId);
		List<Long> companyIds = new ArrayList<Long>();
		if(executiveList != null){
			for(ClientAllocationToKpoExecutive com : executiveList ){
				companyIds.add(com.getCompany().getCompany_id());
			}
		}
		return subLedgerDAO.findAllSubLedgerWithLedger(companyIds);
	}

	@Override
	public List<SubLedger> findAllSubLedgersOnlyOfCompany(Long CompanyId) {
		
		return subLedgerDAO.findAllSubLedgersOnlyOfCompany(CompanyId);
	}
	
	@Transactional
	@Override
	public void addsubledgeropeningbalancenew(Long company_id, Long sids, Long type, Double creditval, Double debitval,String date1, Long year) {
		SubLedger subledger = new SubLedger();
		Ledger ledger = new Ledger();
		
		try {
			subledger = subLedgerDAO.findOne(sids);
			ledger = ledgerDAO.findOne(subledger.getLedger().getLedger_id());

		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		LocalDate date= new LocalDate(date1);
		//Long year_id = yearDAO.findcurrentyear();
		if (year != 0) {
			Long opid = (long) 0;
			Long existid = openingbalances.findsubledgerbalancebydate(company_id, year, sids, (long) 2,date,null,null,null,null,null,null,null,null,null,null,null,null);
			
			if (existid == 0) {
				opid = openingbalances.saveOpeningBalancesnew(company_id, year, sids, (long) 2, creditval, debitval,date1);
			} else {
				opid = openingbalances.updatepeningbalancenew(existid, creditval, debitval,date1,year);
			}
			if (opid != 0) {
				try {
					subledger.setOpeningId(opid);
					subledger.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}
			ledgerService.addledgeropeningbalancenew(company_id, ledger.getLedger_id(), (long) 1, creditval, debitval,year,date1);
		}
	}

	@Override
	public List<SubLedger> findAllOPbalanceofsubledger(long company_id, long flag) {
		// TODO Auto-generated method stub
		return subLedgerDAO.findAllOPbalanceofsubledger(company_id,flag);
	}

	@Override
	public List<SubLedger> findAllSubLedgersListingOfCompany1(Long CompanyId, Long flag) {
		return subLedgerDAO.findAllSubLedgersListingOfCompany1(CompanyId, flag);
	}

	@Override
	public void updateExcelSubLedger(SubLedger subledger) {
		subledger.setSetDefault(subledger.getSetDefault());
		subledger.setUpdate_date(new LocalDate());
		subledger.setFlag(subledger.getFlag());
		try {
			subledger.setCompany(companyDAO.findOne(subledger.getCompany_id()));
			if(subledger.getAllocated()==null)
			{
			subLedgerDAO.updateExcelSubLedger(subledger);	
			}

		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Integer findByStatusDashboard(Long role_id, int approvalStatus, Long user_id) {
	
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
		return subLedgerDAO.findByStatusDashboard(approvalStatus, companyIds);
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
			return subLedgerDAO.findByStatusDashboard(approvalStatus, companyIds);
		}
	}

	@Override
	public Integer findAllDashboard() {
		
		return subLedgerDAO.findAllDashboard();
	}

	@Override
	public Integer findAllSubLedgerOfCompanyDashboard(Long CompanyId) {
		
		return subLedgerDAO.findAllSubLedgerOfCompanyDashboard(CompanyId);
	}

	@Override
	public List<ViewApprovalsForm> viewApprovals(Long role_id, Long userId) {
		
       List<ViewApprovalsForm> approvallist = new ArrayList<ViewApprovalsForm>();	
		
		if(role_id.equals(MyAbstractController.ROLE_EXECUTIVE))
		{
		List<SubLedger> list = new ArrayList<>();	
		
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
		list = subLedgerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
		if(list.size()>0)
		{
			try {
				User user = userDao.findOne(userId);
				ViewApprovalsForm form = new  ViewApprovalsForm();
				form.setSubledgerList(list);
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
				List<SubLedger> list = new ArrayList<>();	
				
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
				list = subLedgerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
				if(list.size()>0)
				{
					try {
						ViewApprovalsForm form = new  ViewApprovalsForm();
						form.setSubledgerList(list);
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
						List<SubLedger> list = new ArrayList<>();	
						
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
						list = subLedgerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setSubledgerList(list);
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
                        List<SubLedger> list = new ArrayList<>();	
						
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
						list = subLedgerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PRIMARY, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setSubledgerList(list);
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

	@Override
	public Double getClosingBalanceOfCashInHand(Long company_Id) {
		Double openingcredit_amount=0.0;
		Double openingdebit_amount=0.0;
		Double credit_amount=0.0;
		Double debit_amount=0.0;
		Double closing_amount=0.0;	
		Double openingBalance =0.0;
		//company_Id=(long) 64;
		//System.out.println("111" + "  "+ company_Id);
		SubLedger cashinhand = subLedgerDAO.findOne("Cash In Hand", company_Id);
		
		Company comp  = null;
		try {
			comp= companyService.getCompanyWithCompanyStautarType(company_Id);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
		for(Object bal[] :openingbalances.findAllOPbalancesforSubLedger(comp.getOpeningbalance_date(), comp.getOpeningbalance_date(), company_Id))
		{
			//System.out.println("enter First opbal ok");
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				
				try {
					
					cashinhand = subLedgerDAO.findOne(cashinhand.getSubledger_Id());
					
				} catch (MyWebException e1) {
					e1.printStackTrace();
				}
				if(cashinhand!=null && cashinhand.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				
				{
					
					openingcredit_amount=openingcredit_amount+(double)bal[2];
					openingdebit_amount=openingdebit_amount+(double)bal[1];
				}
			}
		}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		openingBalance = openingBalance+(openingdebit_amount-openingcredit_amount);
		
	try{
		for (Object bal[] : openingbalances.findAllOPbalancesforSubledger(comp.getOpeningbalance_date(), new LocalDate(),company_Id, true)) 
		{
		
			if (cashinhand != null) {
				if (bal[0] != null && bal[1] != null && bal[2] != null
						&& cashinhand.getSubledger_Id().equals(new Long((long) bal[0]))) {
					
					if (cashinhand != null && cashinhand.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
						credit_amount = (double) bal[2];
						debit_amount = (double) bal[1];
						closing_amount = debit_amount - credit_amount;
					}

				}
			}
		}}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		debit_amount=debit_amount-openingBalance;
		closing_amount=debit_amount-credit_amount+openingBalance;
		return closing_amount;
	}
	
	@Override
	public Set<SubLedger> findAllSubLedgersForDepreciation(Long company_id) {
		// TODO Auto-generated method stub
		return subLedgerDAO.findAllSubLedgersForDepreciation(company_id);
	}
	
	public Ledger subLedgerOfNameDepreciaition(Long company_id)
	{
		 return subLedgerDAO.subLedgerOfNameDepreciaition(company_id);
	}

	@Override
	public List<SubLedger> findAllSubLedgersofSuppliers(Long CompanyId) {
		// TODO Auto-generated method stub
		return subLedgerDAO.findAllSubLedgersofSuppliers(CompanyId);
	}

	@Override
	public List<SubLedger> findAllApprovedByCompanyForCustomer(Long CompanyId) {
		// TODO Auto-generated method stub
		return subLedgerDAO.findAllApprovedByCompanyForCustomer(CompanyId);
	}

	@Override
	public List<SubLedger> findAllApprovedByCompanyForSupplier(Long CompanyId) {
		// TODO Auto-generated method stub
		return subLedgerDAO.findAllApprovedByCompanyForSupplier(CompanyId);
	}

	@Override
	public List<SubLedger> findAllSubLedgerWithLedgerForCustSupplier(Long userId ,boolean IsCustomer,boolean IsSuperUser) {
		// TODO Auto-generated method stub
		List<Long> companyIds = new ArrayList<Long>();
		List<SubLedger> list = new ArrayList<>();
		long roleId = 0;
		long companyId = 0;
		if(!IsSuperUser){
			try{
				System.out.println("user Id is " + userId);
			User usr=userDao.findOne(userId);
			roleId=usr.getRole().getRole_id();
			companyId=usr.getCompany().getCompany_id();
			System.out.println("Role Id is " + roleId);
			System.out.println("company Id is " + companyId);
			} catch (MyWebException e) {
				e.printStackTrace();
			}
			if(roleId==MyAbstractController.ROLE_EXECUTIVE || roleId==MyAbstractController.ROLE_MANAGER){
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(userId);
		
		if(executiveList != null){
			for(ClientAllocationToKpoExecutive com : executiveList ){
				System.out.println("cmp alloc "+com.getCompany().getCompany_id());
				companyIds.add(com.getCompany().getCompany_id());
				//yearIds.add(new Long(yId));
			}
		}
		System.out.println("the compid list is "+companyIds.size());
		 System.out.println(companyIds.isEmpty()); 
			}else{
				companyIds.add(companyId);
			}
			}
		if(IsCustomer){
			list= subLedgerDAO.findAllSubLedgerWithLedgerForCust(companyIds, IsSuperUser);}
		else{
			list= subLedgerDAO.findAllSubLedgerWithLedgerForSuppl(companyIds, IsSuperUser);
		}
	return list;
	}
	}

