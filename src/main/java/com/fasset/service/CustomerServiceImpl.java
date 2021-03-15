/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.ICityDAO;
import com.fasset.dao.interfaces.IClientAllocationToKpoExecutiveDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.ICompanyStatutoryTypeDAO;
import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.dao.interfaces.ICustomerDAO;
import com.fasset.dao.interfaces.IDeducteeDao;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.ITDSTypeDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.dao.interfaces.IindustryTypeDAO;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.entities.Deductee;
import com.fasset.entities.Ledger;
import com.fasset.entities.Product;
import com.fasset.entities.SubLedger;
import com.fasset.entities.TDS_Type;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.SubNature;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IOpeningBalancesService;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {
	
	@Autowired
	IClientAllocationToKpoExecutiveDAO clientAllocationToKpoExectiveDAO;

	@Autowired
	private ICustomerDAO customerDAO;

	@Autowired
	private ICityDAO cityDAO;

	@Autowired
	private IDeducteeDao deducteeDao;

	@Autowired
	private ICountryDAO countryDao;

	@Autowired
	private IStateDAO stateDAO;

	@Autowired
	private ICompanyStatutoryTypeDAO typedao;

	@Autowired
	private ISubLedgerDAO subLedgerDAO;

	@Autowired
	private IProductDAO productDao;

	@Autowired
	private IindustryTypeDAO industryTypeDAO;

	@Autowired
	private ICompanyDAO companyDAO;

	@Autowired
	private IOpeningBalancesService openingbalances;

	@Autowired
	private IAccountingYearDAO yearDAO;
	
	@Autowired
	private IUserDAO userDao;
	@Autowired
	private ITDSTypeDAO tdsTypeDao;

	@Override
	public void add(Customer entity) throws MyWebException {
		entity.setCreated_date(new LocalDateTime());
		String company_name = entity.getFirm_name().replace("\"", "").replace("\'", "");
		entity.setFirm_name(company_name);
		Long id = customerDAO.createcust(entity);
		/*if (id != null) {
			Long year_id = yearDAO.findcurrentyear();
			if (entity.getCompany() != null) {
				Long opid = openingbalances.saveOpeningBalances(entity.getCompany().getCompany_id(), year_id, id,
						(long) 5, (double) 0, (double) 0);
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
	public void update(Customer entity) throws MyWebException {

		List<SubNature> subPurposeList = new ArrayList<SubNature>();
		Set<SubLedger> subLedgers = new HashSet<SubLedger>();
		Set<Product> products = new HashSet<Product>();
		
		entity.setFlag(true);
	entity.setCustomer_approval(entity.getCustomer_approval());

		if (entity.getSubNatureList() != "" && entity.getSubNatureList() != null) {
			JSONArray jsonArray = new JSONArray(entity.getSubNatureList());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				SubNature subNature = new SubNature();
				subNature.setSubId(jsonObject.getString("subId"));
				subNature.setPurpose(jsonObject.getString("purpose"));
				subPurposeList.add(subNature);
			}

			for (int i = 0; i < subPurposeList.size(); i++) {
				SubNature subNature = new SubNature();
				subNature = subPurposeList.get(i);
				try {
					SubLedger subledger = subLedgerDAO.findOne(Long.parseLong(subNature.getSubId()));
					subLedgers.add(subledger);
				} catch (NumberFormatException | MyWebException e) {
					e.printStackTrace();
				}
			}

		}

		entity.setSubLedgers(subLedgers);
		entity.setSubPurposeList(subPurposeList);

		if (entity.getProductList() != null && entity.getProductList().size() != 0) {
			for (String id : entity.getProductList()) {

				Long pid = Long.parseLong(id);
				try {
					Product product = productDao.findOne(pid);
					products.add(product);
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}
		}

		entity.setProduct(products);
		try {
			entity.setCompStatType(typedao.findOne(entity.getCompany_statutory_id()));
			entity.setCountry(countryDao.findOne(entity.getCountry_id()));
			entity.setState(stateDAO.findOne(entity.getState_id()));
			entity.setCity(cityDAO.findOne(entity.getCity_id()));
			entity.setIndustryType(industryTypeDAO.findOne(entity.getIndustry_id()));
			if(entity.getTds_applicable()==1)
			{
				Deductee deduct =deducteeDao.findOne(entity.getDeductee_id());
				entity.setDeductee(deduct);
				System.out.println("getting update customer "+entity.getDeductee().getDeductee_id());
				entity.setTdstype(tdsTypeDao.findOne(deduct.getTds_type().gettdsType_id()));
				System.out.println("getting update customer "+entity.getTdstype().gettdsType_id());
				
			if ((entity.getTds_rate1() != null) && (entity.getTds_rate1() != "")) {
				entity.setTds_rate(Float.valueOf(entity.getTds_rate1()));
			}
			}
			else
			{
				entity.setDeductee(null);
				entity.setTds_rate((float)0);
			}

			if (entity.getCompany_id() != null && entity.getCompany_id() > 0) {
				entity.setCompany(companyDAO.findOne(entity.getCompany_id()));
			}

		} catch (MyWebException e) {
			e.printStackTrace();
		}
		String company_name = entity.getFirm_name().replace("\"", "").replace("\'", "");
		entity.setFirm_name(company_name);
		
		customerDAO.update(entity);
	}

	@Override
	public List<Customer> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer getById(Long id) throws MyWebException {
		return customerDAO.findOne(id);
	}

	@Override
	public Customer getById(String id) throws MyWebException {
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
	public void remove(Customer entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Customer entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public String saveCustomers(Customer sup) {
		List<SubNature> subPurposeList = new ArrayList<SubNature>();
		Set<SubLedger> subLedgers = new HashSet<SubLedger>();
		Set<Product> products = new HashSet<Product>();
System.out.println("saving customer 2nd nov ");
		sup.setCreated_date(new LocalDateTime());
		if ((sup.getTds_rate1() != null) && (sup.getTds_rate1() != "")) {
			sup.setTds_rate(Float.valueOf(sup.getTds_rate1()));
		}
		else
		{
			sup.setTds_rate((float)0);
		}

		JSONArray jsonArray = new JSONArray(sup.getSubNatureList());
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			SubNature subNature = new SubNature();
			subNature.setSubId(jsonObject.getString("subId"));
			subNature.setPurpose(jsonObject.getString("purpose"));
			subPurposeList.add(subNature);
		}

		sup.setSubPurposeList(subPurposeList);

		for (int i = 0; i < subPurposeList.size(); i++) {
			SubNature subNature = new SubNature();
			subNature = subPurposeList.get(i);
			try {
				SubLedger subledger = subLedgerDAO.findOne(Long.parseLong(subNature.getSubId()));
				subLedgers.add(subledger);
			} catch (NumberFormatException | MyWebException e) {
				e.printStackTrace();
			}
		}
		for (String id : sup.getProductList()) {

			Long pid = Long.parseLong(id);
			try {
				Product product = productDao.findOne(pid);
				products.add(product);
			} catch (MyWebException e) {
				e.printStackTrace();
			}
		}
		sup.setProduct(products);
		sup.setSubLedgers(subLedgers);
		try {
			sup.setCompStatType(typedao.findOne(sup.getCompany_statutory_id()));
			sup.setCountry(countryDao.findOne(sup.getCountry_id()));
			sup.setState(stateDAO.findOne(sup.getState_id()));
			sup.setCity(cityDAO.findOne(sup.getCity_id()));
			sup.setIndustryType(industryTypeDAO.findOne(sup.getIndustry_id()));
			if(sup.getTds_applicable()==1)
			{
				Deductee deduct =deducteeDao.findOne(sup.getDeductee_id());
				sup.setDeductee(deduct);
				sup.setTdstype(tdsTypeDao.findOne(deduct.getTds_type().gettdsType_id()));
				
			if ((sup.getTds_rate1() != null) && (sup.getTds_rate1() != "")) {
				sup.setTds_rate(Float.valueOf(sup.getTds_rate1()));
			}
			}
			else
			{
				sup.setDeductee(null);
				sup.setTds_rate(null);
			}
			/*Deductee deduct = deducteeDao.findOne(sup.getDeductee_id());
			System.out.println("The tds type id is " + deduct.getTds_type().gettdsType_id());
			TDS_Type tdstype=tdsTypeDao.findOne(deduct.getTds_type().gettdsType_id());
			
			sup.setDeductee(deduct);
			sup.setTdstype(tdstype); */
			//sup.setTdsType_id(deduct.getTds_type_id());

		} catch (MyWebException e) {
			e.printStackTrace();
		}
		String company_name = sup.getFirm_name().replace("\"", "").replace("\'", "");
		sup.setFirm_name(company_name);
		Long id = customerDAO.saveCustomersDao(sup);
		if (id != null) {
			/*Long year_id = yearDAO.findcurrentyear();
			Long opid = openingbalances.saveOpeningBalances(sup.getCompany().getCompany_id(), year_id, id, (long) 5, (double) 0, (double) 0);
			if (opid != 0) {
				try {
					sup.setOpeningId(opid);
					sup.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
			return "Customer saved successfully";
		} else {
			return "Please try again";
		}
	}

	@Override
	public List<Customer> findAll() {
		return customerDAO.findAll();
	}

	@Override
	public String deleteCustomerSubLedger(Long cId, Long subId) {
		String msg = customerDAO.deleteCustomerSubLedger(cId, subId);
		return msg;
	}

	@Override
	public String deleteCustomerProduct(Long cId, Long pId) {
		String msg = customerDAO.deleteCustomerProduct(cId, pId);
		return msg;
	}

	@Override
	public List<Customer> findByStatus(int status) {
		return customerDAO.findByStatus(status);
	}

	@Override
	public String updateByApproval(Long customerId, int status) {
		String msg = customerDAO.updateApprovalStatus(customerId, status);
		return msg;
	}

	@Override
	public Customer isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> findAllCustomersOfCompany(Long CompanyId) {
		return customerDAO.findAllCustomersOfCompany(CompanyId);
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		return customerDAO.deleteByIdValue(entityId);
	}

	@Override
	public List<Customer> findAllCustomerListing(Long flag) {
		return customerDAO.findAllCustomerListing(flag);
	}

	@Override
	public List<Customer> findAllCustomerListingOfCompany(Long CompanyId, Long flag) {
		return customerDAO.findAllCustomerListingOfCompany(CompanyId, flag);
	}

	@Override
	public Customer findOneWithAll(Long cusId) {
		return customerDAO.findOneWithAll(cusId);
	}

	@Override
	public void addcustomeropeningbalance(Long company_id, Long sids, Long type, Double creditval, Double debitval) {
		/*Customer customer = new Customer();
		try {
			customer = customerDAO.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}*/
		Long year_id = yearDAO.findcurrentyear();
		if (year_id != 0) {
			/*Long opid = (long) 0;*/
			Long existid = openingbalances.findcustomerbalance(company_id, year_id, sids, (long) 5);
			if (existid == 0) {
				/*opid = */openingbalances.saveOpeningBalances(company_id, year_id, sids, (long) 5, creditval, debitval);
			} else {
				/*opid =*/ openingbalances.updatepeningbalance(existid, creditval, debitval);
			}
			/*if (opid != 0) {
				try {
					customer.setOpeningId(opid);
					customer.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
		}
	}

	@Transactional
	@Override
	public void addcustomertransactionbalance(Long year_id,LocalDate date,Long company_id, Long sids, Long type, Double creditval, Double debitval, Long flag) {
		/*Customer customer = new Customer();
		try {
			customer = customerDAO.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}*/
		//Long year_id = yearDAO.findcurrentyear();
		if (year_id != 0) {
			/*Long opid = (long) 0;*/
			Long existid = openingbalances.findcustomerbalancebydate(company_id, year_id, sids, (long) 5,date);
			System.out.println(existid);
			if (existid == 0) {
				
				
				/*	opid = */openingbalances.saveOpeningBalancesbydate(date,company_id, year_id, sids, (long) 5, creditval, debitval,null,null,null,null,null,null,null,null,null,null,null,null);
			
			} else {
				System.out.println("updateing cust");
				System.out.println("creditval cust "+creditval);
				System.out.println("debit cust "+debitval);
				/*opid = */openingbalances.updateCDbalance(existid, creditval, debitval, flag);
			}
			/*if (opid != 0) {
				try {
					customer.setOpeningId(opid);
					customer.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
		}
	}

	@Override
	public Boolean approvedByBatch(List<String> customerList, Boolean primaryApproval) {
		return customerDAO.approvedByBatch(customerList, primaryApproval);
	}

	@Override
	public Boolean rejectByBatch(List<String> customerList) {
		return customerDAO.rejectByBatch(customerList);
	}

	@Override
	public int isExistsPan(String companypan, String companyname, Long company_id, String email) {
		int opid = customerDAO.isExistsPan(companypan, companyname, company_id, email);
		return opid;
	}

	@Override
	public Customer isExistsCustomer(String companypan, String firm_name, long company_id, String contact_name) {
		return customerDAO.isExistsCustomer(companypan, firm_name, company_id, contact_name);
	}

	@Override
	public List<Customer> findAllCustomersOnlyOFCompany(Long CompanyId) {
		return customerDAO.findAllCustomersOnlyOFCompany(CompanyId);
	}

	@Override
	public void updateExcel(Customer entity) {
		
		entity.setFlag(entity.getFlag());
		entity.setCustomer_approval(0);

		try {
			entity.setCompStatType(typedao.findOne(entity.getCompany_statutory_id()));
			entity.setCountry(countryDao.findOne(entity.getCountry_id()));
			entity.setState(stateDAO.findOne(entity.getState_id()));
			entity.setCity(cityDAO.findOne(entity.getCity_id()));
			entity.setIndustryType(industryTypeDAO.findOne(entity.getIndustry_id()));
		
			//entity.setDeductee(deduct);
			//entity.setTdsType_id(deduct.getTds_type_id());
			if(entity.getTds_applicable()==1)
			{
				Deductee deduct =deducteeDao.findOne(entity.getDeductee_id());
				entity.setDeductee(deduct);
				entity.setTdstype(tdsTypeDao.findOne(deduct.getTds_type().gettdsType_id()));
				
			if ((entity.getTds_rate1() != null) && (entity.getTds_rate1() != "")) {
				entity.setTds_rate(Float.valueOf(entity.getTds_rate1()));
			}
			}
			else
			{
				entity.setDeductee(null);
				entity.setTds_rate(null);
			}
			if (entity.getCompany_id() != null && entity.getCompany_id() > 0) {
				entity.setCompany(companyDAO.findOne(entity.getCompany_id()));
			}

		} catch (MyWebException e) {
			e.printStackTrace();
		}
		try {
			customerDAO.update(entity);
		} catch (MyWebException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Customer> findByStatus(Long role_id,int status, Long userId) {
		
		if(role_id.equals(MyAbstractController.ROLE_EXECUTIVE))
		{
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
		return customerDAO.findByStatus(status, companyIds);
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
			return customerDAO.findByStatus(status, companyIds);
		}
	}

	@Override
	public List<Customer> findAllCustomerListing(Boolean flag, Long userId) {
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
		return customerDAO.findAllCustomerListing(flag, companyIds);
	}

	@Transactional
	@Override
	public void addcustomeropeningbalancenew(Long company_id, Long sids, Long type, Double creditval, Double debitval,
			String date1, Long year) {
		Customer customer = new Customer();
		try {
			customer = customerDAO.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		LocalDate date = new LocalDate(date1);
		//Long year_id = yearDAO.findcurrentyear();
		if (year != 0) {
			Long opid = (long) 0;
			Long existid = openingbalances.findsubledgerbalancebydate(company_id, year, sids, (long) 5,date,null,null,null,null,null,null,null,null,null,null,null,null);
			if (existid == 0) {
				opid = openingbalances.saveOpeningBalancesnew(company_id, year, sids, (long) 5, creditval, debitval,date1);
			} else {
				opid = openingbalances.updatepeningbalancenew(existid, creditval, debitval,date1,year);
			}
			if (opid != 0) {
				try {
					customer.setOpeningId(opid);
					customer.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public List<Customer> findAllOPbalanceofcustomer(long company_id, long flag) {
		// TODO Auto-generated method stub
		return customerDAO.findAllOPbalanceofcustomer(company_id,flag);
	}

	@Override
	public List<Customer> findAllCustomersOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		return customerDAO.findAllCustomersOfCompanyDashboard(companyId);
	}

	@Override
	public List<Customer> findByStatusDashboard(int approvalStatusPending, Long user_id) {
		// TODO Auto-generated method stub
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(user_id);
		List<Long> companyIds = new ArrayList<Long>();
		if(executiveList .isEmpty()){
			companyIds.add((long) 0);
		}
		else
		{			for(ClientAllocationToKpoExecutive com : executiveList ){
				companyIds.add(com.getCompany().getCompany_id());
			}
		}
		return customerDAO.findByStatusDashboard(approvalStatusPending, companyIds);
	}

	@Override
	public List<Customer> findAllDashboard() {
		return customerDAO.findAllDashboard();
	}

	@Override
	public Integer findByStatusCustomerCountDashboard(Long role_id, int approvalStatus, Long user_id) {
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
		return customerDAO.findByStatusCustomerCountDashboard(approvalStatus, companyIds);
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
			return customerDAO.findByStatusCustomerCountDashboard(approvalStatus, companyIds);
		}
	}

	@Override
	public Integer findAllCustomerCountDashboard() {
		// TODO Auto-generated method stub
		return customerDAO.findAllCustomerCountDashboard();
	}

	@Override
	public Integer findAllCustomerCountOfCompanyDashboard(Long CompanyId) {
		// TODO Auto-generated method stub
		return customerDAO.findAllCustomerCountOfCompanyDashboard(CompanyId);
	}

	@Override
	public List<ViewApprovalsForm> viewApprovals(Long role_id, Long userId) {
		
    List<ViewApprovalsForm> approvallist = new ArrayList<ViewApprovalsForm>();	
		
		if(role_id.equals(MyAbstractController.ROLE_EXECUTIVE))
		{
		List<Customer> list = new ArrayList<>();	
		
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
		list = customerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
		if(list.size()>0)
		{
			try {
				User user = userDao.findOne(userId);
				ViewApprovalsForm form = new  ViewApprovalsForm();
				form.setCustomerList(list);
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
				List<Customer> list = new ArrayList<>();	
				
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
				list = customerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
				if(list.size()>0)
				{
					try {
						ViewApprovalsForm form = new  ViewApprovalsForm();
						form.setCustomerList(list);
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
						List<Customer> list = new ArrayList<>();	
						
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
						list = customerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setCustomerList(list);
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
                        List<Customer> list = new ArrayList<>();	
						
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
						list = customerDAO.findByStatus(MyAbstractController.APPROVAL_STATUS_PRIMARY, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setCustomerList(list);
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
	public Customer findGstNoForCustomerByCompany(long companyid, long custid) {
		// TODO Auto-generated method stub
		return customerDAO.findGstNoForCustomerByCompany(companyid, custid);
	}

	@Override
	public List<Customer> findAllCustomersOfCompanyInclPending(Long CompanyId) {
		// TODO Auto-generated method stub
		return customerDAO.findAllCustomersOfCompanyInclPending(CompanyId);
	}

	/*@Override
	public Company getCompanyForGstCheck(Long companyId) {
	
		return null;
	}*/

	
	}

