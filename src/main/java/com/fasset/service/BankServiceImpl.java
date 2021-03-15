package com.fasset.service;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.Year;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IAccountSubGroupDAO;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IBankDAO;
import com.fasset.dao.interfaces.IClientAllocationToKpoExecutiveDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.entities.Bank;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Company;
import com.fasset.entities.Ledger;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IOpeningBalancesService;

@Transactional
@Service
public class BankServiceImpl implements IBankService {
	
	@Autowired
	IClientAllocationToKpoExecutiveDAO clientAllocationToKpoExectiveDAO;

	@Autowired
	private IBankDAO bankDao;

	@Autowired
	private IAccountSubGroupDAO accountSubGroupDao;

	@Autowired
	private ICompanyDAO companyDAO;

	@Autowired
	private IOpeningBalancesService openingbalances;

	@Autowired
	private IAccountingYearDAO yearDAO;
	
	@Autowired
	private IUserDAO userDao;

	@Autowired
	private ICommonService commonservice;
	
	@Autowired
	private ICompanyService companyService;
	
	@Override
	public void add(Bank entity) throws MyWebException {
		entity.setAccount_sub_group_id(accountSubGroupDao.findOne(entity.getSubGroupId()));
		entity.setCreated_date(new LocalDate());
		String bankn = entity.getBank_name().replace("\"", "").replace("\'", "").replace("-", "");
		entity.setBank_name(bankn);
	
		if (bankn.equalsIgnoreCase("OTH")) {
			entity.setBank_name(entity.getOther_bank_name());
		}
		Long id = bankDao.createBank(entity);
		/*if (id != null) {
			Long year_id = yearDAO.findcurrentyear();
			if (entity.getCompany() != null) {
				Long opid = openingbalances.saveOpeningBalances(entity.getCompany().getCompany_id(), year_id, id, (long) 3,  (double)0,  (double)0);
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
	}

	@Override
	public String addExcel(Bank entity) {
		entity.setCreated_date(new LocalDate());
		try {
			Long id = bankDao.createBank(entity);
			/*if (id != null) {
				Long year_id = yearDAO.findcurrentyear();
				if (entity.getCompany() != null) {
					Long opid = openingbalances.saveOpeningBalances(entity.getCompany().getCompany_id(), year_id, id, (long) 3,  (double)0, (double)0);
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
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		return "Bank Saved Successfully!!";
	}

	@Override
	public void update(Bank entity) throws MyWebException {
		Bank bank = bankDao.findOne(entity.getBank_id());
		bank.setAccount_sub_group_id(accountSubGroupDao.findOne(entity.getSubGroupId()));
		bank.setCompany(companyDAO.findOne(bank.getCompany().getCompany_id()));
		bank.setCompany_id(entity.getCompany_id());
		String bankn = entity.getBank_name().replace("\"", "").replace("\'", "").replace("-", "");
		bank.setBank_name(bankn);
		
/*		if ((bank.getBank_approval() == 1) || (bank.getBank_approval() == 3))
*/			
		bank.setBank_approval(entity.getBank_approval());
		bank.setBranch(entity.getBranch());
		bank.setAccount_no(entity.getAccount_no());
		bank.setIfsc_no(entity.getIfsc_no());
		bank.setStatus(entity.getStatus());
		bank.setFlag(true);
		bankDao.update(bank);
	}

	@Override
	public List<Bank> list() {
		return bankDao.findAll();
	}

	@Override
	public Bank getById(Long id) throws MyWebException {
		return bankDao.findOne(id);
	}

	@Override
	public Bank getById(String id) throws MyWebException {
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
	public void remove(Bank entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Bank entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Bank> findByStatus(int status) {
		return bankDao.findByStatus(status);
	}

	@Override
	public String updateByApproval(Long bankId, int status) {
		String msg = bankDao.updateApprovalStatus(bankId, status);
		return msg;
	}

	@Override
	public Bank isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bank> findAllBanksOfCompany(Long CompanyId) {
		return bankDao.findAllBanksOfCompany(CompanyId);
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = bankDao.deleteByIdValue(entityId);
		return msg;
	}

	@Override
	public List<Bank> findAllListing(Boolean flag) {
		return bankDao.findAllListing(flag);
	}
	
	@Override
	public List<Bank> findAllListingBanksOfCompany(Long CompanyId, Boolean flag) {
		return bankDao.findAllListingBanksOfCompany(CompanyId, flag);
	}

	@Override
	public Bank findOneView(Long id) {
		return bankDao.findOneView(id);
	}

	@Transactional
	@Override
	public void addbankopeningbalance(Long company_id, Long sids, Long type, Double creditval, Double debitval) {
		/*Bank bank = new Bank();
		try {
			bank = bankDao.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}*/
		Long year_id = yearDAO.findcurrentyear();
		if (year_id != 0) {
			/*Long opid = (long) 0;*/
			Long existid = openingbalances.findbankbalance(company_id, year_id, sids, (long) 3);
			if (existid == 0) {
			/*	opid =*/ openingbalances.saveOpeningBalances(company_id, year_id, sids, (long) 3, creditval, debitval);
			} else {
			/*	opid = */openingbalances.updatepeningbalance(existid, creditval, debitval);
			}
		/*	if (opid != 0) {
				try {
					bank.setOpeningId(opid);
					bank.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
		}
	}

	@Transactional
	@Override
	public void addbanktransactionbalance(Long year_id, LocalDate date,Long company_id, Long sids, Long type, Double creditval, Double debitval, Long flag) {
	/*	Bank bank = new Bank();
		try {
			bank = bankDao.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}*/
		//Long year_id = yearDAO.findcurrentyear();
		
		if (year_id != 0) {
			/*Long opid = (long) 0;*/
			Long existid = openingbalances.findbankbalancebydate(company_id, year_id, sids, (long) 3,date);
			if (existid == 0) {
				
				/*opid = */openingbalances.saveOpeningBalancesbydate(date,company_id, year_id, sids, (long) 3, creditval, debitval,null,null,null,null,null,null,null,null,null,null,null,null);
			} else {
				
				/*opid = */openingbalances.updateCDbalance(existid, creditval, debitval, flag);
			}
			/*if (opid != 0) {
				try {
					bank.setOpeningId(opid);
					bank.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
		}
	}

	@Override
	public Boolean approvedByBatch(List<String> bankList, Boolean primaryApproval) {
		return bankDao.approvedByBatch(bankList, primaryApproval);
	}

	@Override
	public Boolean rejectByBatch(List<String> bankList) {
		return bankDao.rejectByBatch(bankList);
	}

	@Override
	public int isExistsAccount(Long account_no, Long company_id) {
		int opid = bankDao.isExistsAccount(account_no, company_id);
		return opid;
	}

	@Override
	public Bank isExists(Long account_no, Long company_id) {
		return bankDao.isExists(account_no, company_id);
	}

	@Override
	public List<Bank> findByStatus(Long role_id,int status, Long userId) {
		
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
		return bankDao.findByStatus(status, companyIds);
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
			return bankDao.findByStatus(status, companyIds);
		}
	}

	@Override
	public List<Bank> findAllListing(Boolean flag, Long userId) {
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
		return bankDao.findAllListing(flag, companyIds);
	}

	@Transactional
	@Override
	public void addbankopeningbalancenew(Long company_id, Long sids, Long type, Double creditval, Double debitval,
			String date1, Long year) {
		Bank bank = new Bank();
		try {
			bank = bankDao.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		LocalDate date=new LocalDate(date1);
		//Long year_id = yearDAO.findcurrentyear();
		if (year!=0) {
			Long opid = (long) 0;
			Long existid = openingbalances.findsubledgerbalancebydate(company_id, year, sids, (long) 3,date,null,null,null,null,null,null,null,null,null,null,null,null);
			if (existid == 0) {
				opid = openingbalances.saveOpeningBalancesnew(company_id, year, sids, (long) 3, creditval, debitval,date1);
			} else {
				opid = openingbalances.updatepeningbalancenew(existid, creditval, debitval,date1,year);
			}
			if (opid != 0) {
				try {
					bank.setOpeningId(opid);
					bank.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public List<Bank> findAllOPbalanceofbank(long company_id, long flag) {
		// TODO Auto-generated method stub
		return bankDao.findAllOPbalanceofbank(company_id,flag);
	}

	@Override
	public Integer findAllBanksOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		return bankDao.findAllBanksOfCompanyDashboard(companyId);
	}

	@Override
	public Integer findByStatusDashboard(Long role_Id, int approvalStatus, Long user_id) {
		
		if(role_Id.equals(MyAbstractController.ROLE_EXECUTIVE))
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
		return bankDao.findByStatusDashboard(approvalStatus, companyIds);
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
			return bankDao.findByStatusDashboard(approvalStatus, companyIds);
		}
	}

	@Override
	public Integer findAllDashboard() {
		return bankDao.findAllDashboard();
	}

	@Override
	public List<Bank> findAllListingBanksOfCompany1(Long CompanyId, Boolean flag) {
		return bankDao.findAllListingBanksOfCompany1(CompanyId, flag);
	}

	@Override
	public List<Bank> findAllListing1(boolean flag) {
		// TODO Auto-generated method stub
		return bankDao.findAllListing1(flag);
	}

	@Override
	public List<ViewApprovalsForm> viewApprovals(Long role_id, Long userId) {
List<ViewApprovalsForm> approvallist = new ArrayList<ViewApprovalsForm>();	
		
		if(role_id.equals(MyAbstractController.ROLE_EXECUTIVE))
		{
		List<Bank> list = new ArrayList<>();	
		
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
		list = bankDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
		if(list.size()>0)
		{
			try {
				User user = userDao.findOne(userId);
				ViewApprovalsForm form = new  ViewApprovalsForm();
				form.setBankList(list);
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
				List<Bank> list = new ArrayList<>();	
				
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
				list = bankDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
				if(list.size()>0)
				{
					try {
						ViewApprovalsForm form = new  ViewApprovalsForm();
						form.setBankList(list);
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
						List<Bank> list = new ArrayList<>();	
						
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
						list = bankDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setBankList(list);
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
                        List<Bank> list = new ArrayList<>();	
						
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
						list = bankDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PRIMARY, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setBankList(list);
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
	public List<Bank> findAllListing2(boolean flag, Long company_id) {
		// TODO Auto-generated method stub
		return bankDao.findAllListing2(flag, company_id);
	}

	@Override
	public Double getClosingBalanceOfAllbanks(Long company_Id) {
		
		Double credit_amount=0.0;
		Double debit_amount=0.0;
		
		Double openingcredit_amount=0.0;
		Double openingdebit_amount=0.0;
		
		Double closing_amount=0.0;
		Double openingBalance =0.0;
		Company comp  = null;
		try {
			comp= companyService.getCompanyWithCompanyStautarType(company_Id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Object bal[] :openingbalances.findAllOPbalancesforBank(comp.getOpeningbalance_date(), comp.getOpeningbalance_date(), company_Id))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Bank bank = null;
				try {
					bank = bankDao.findOne((long)bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
					openingcredit_amount=openingcredit_amount+(double)bal[2];
					openingdebit_amount=openingdebit_amount+(double)bal[1];
				}
			}
		}
		
		openingBalance = openingBalance+(openingdebit_amount-openingcredit_amount);
		
		for(Object bal[] :openingbalances.findAllOPbalancesforBank(comp.getOpeningbalance_date(), new LocalDate(), company_Id))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Bank bank = null;
				try {
					bank = bankDao.findOne((long)bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
					credit_amount=credit_amount+(double)bal[2];
					debit_amount=debit_amount+(double)bal[1];
				}
			}
		}
		debit_amount=debit_amount-openingBalance;
		closing_amount=debit_amount-credit_amount+openingBalance;
		return closing_amount;
	}

	
	
}