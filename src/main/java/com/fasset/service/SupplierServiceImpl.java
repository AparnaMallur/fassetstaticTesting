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
import com.fasset.dao.interfaces.ICompanyStatutoryTypeDAO;
import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.dao.interfaces.IDeducteeDao;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.ISupplierDAO;
import com.fasset.dao.interfaces.ITDSTypeDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.dao.interfaces.IindustryTypeDAO;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Deductee;
import com.fasset.entities.Ledger;
import com.fasset.entities.Product;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.SubNature;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.ISuplliersService;

@Service
@Transactional
public class SupplierServiceImpl implements ISuplliersService {
	
	@Autowired
	IClientAllocationToKpoExecutiveDAO clientAllocationToKpoExectiveDAO;

	@Autowired
	private ISupplierDAO supplierDao;

	@Autowired
	private ICityDAO cityDAO;

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
	private IDeducteeDao deducteeDao;
	
	@Autowired
	private ITDSTypeDAO tdsTypeDao;

	@Autowired
	private IOpeningBalancesService openingbalances;

	@Autowired
	private IAccountingYearDAO yearDAO;
	
	@Autowired
	private IUserDAO userDao;

	@Transactional
	@Override
	public void add(Suppliers entity) throws MyWebException {
		entity.setCreated_date(new LocalDateTime());
		Long id = supplierDao.createsup(entity);
		/*if (id != null) {
			Long year_id = yearDAO.findcurrentyear();
			if (entity.getCompany() != null) {
				Long opid = openingbalances.saveOpeningBalances(entity.getCompany().getCompany_id(), year_id, id, (long) 4, (double) 0, (double) 0);
				if (opid != 0) {
					entity.setOpeningId(opid);
					try {
						entity.setOpeningbalances(openingbalances.getById(opid));
					} catch (MyWebException e) {
						e.printStackTrace();
					}
				}
			}
		}*/
	}

	@Transactional
	@Override
	public void update(Suppliers entity) throws MyWebException {

		List<SubNature> subPurposeList = new ArrayList<SubNature>();
		Set<SubLedger> subLedgers = new HashSet<SubLedger>();
		Set<Product> products = new HashSet<Product>();
		entity.setUpdate_date(new LocalDateTime());
		
		entity.setSupplier_approval(entity.getSupplier_approval());
		entity.setFlag(true);
		String reverse_mecha = entity.getReverse_mecha();
		String gst_no = entity.getGst_no();
		String suppiler_category = null;
		switch (reverse_mecha) {
		case "Yes":
			suppiler_category = "Notified service goods";
			break;
		case "No":
			if (gst_no == "NA") {
				suppiler_category = "URD";
			} else {
				suppiler_category = "None";
			}
			break;
		}
		entity.setSuppiler_category(suppiler_category);

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
				
				Deductee deduct=deducteeDao.findOne(entity.getDeductee_id());
				System.out.println("tds Apply supp"+deduct.getDeductee_id());
				System.out.println("tds Apply supp"+deduct.getTds_type().gettdsType_id());
			entity.setDeductee(deduct);
			entity.setTdstype(tdsTypeDao.findOne(deduct.getTds_type().gettdsType_id()));
			
				if ((entity.getTds_rate1() != null) && (entity.getTds_rate1() != "")) {
					entity.setTds_rate(Float.valueOf(entity.getTds_rate1()));
				}
			}
			else
			{
				entity.setDeductee(null);
				entity.setTds_rate((float)0);
			}
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		String company_name = entity.getCompany_name().replace("\"", "").replace("\'", "");
		entity.setCompany_name(company_name);
		supplierDao.update(entity);

	}

	@Override
	public void updateExcel(Suppliers entity) {
		entity.setUpdate_date(new LocalDateTime());
		if ((entity.getTds_rate1() != null) && (entity.getTds_rate1() != "")) {
			entity.setTds_rate(Float.valueOf(entity.getTds_rate1()));
		}

		String reverse_mecha = entity.getReverse_mecha();
		String gst_no = entity.getGst_no();
		String suppiler_category = null;
		switch (reverse_mecha) {
		case "Yes":
			suppiler_category = "Notified service goods";
			break;
		case "No":
			if (gst_no == "NA") {
				suppiler_category = "URD";
			} else {
				suppiler_category = "None";

			}
			break;
		}
		entity.setSuppiler_category(suppiler_category);
		entity.setSupplier_approval(0);
		try {
			entity.setCompStatType(typedao.findOne(entity.getCompany_statutory_id()));
			entity.setCountry(countryDao.findOne(entity.getCountry_id()));
			entity.setState(stateDAO.findOne(entity.getState_id()));
			entity.setCity(cityDAO.findOne(entity.getCity_id()));
			entity.setIndustryType(industryTypeDAO.findOne(entity.getIndustry_id()));
			Deductee deduct =deducteeDao.findOne(entity.getDeductee_id());
			entity.setDeductee(deduct);
			entity.setTdstype(tdsTypeDao.findOne(deduct.getTds_type().gettdsType_id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		try {
			supplierDao.update(entity);
		} catch (MyWebException e) {
			e.printStackTrace();
		}

	}

	@Transactional
	@Override
	public List<Suppliers> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public Suppliers getById(Long id) throws MyWebException {
		return supplierDao.findOne(id);
	}

	@Override
	public Suppliers getById(String id) throws MyWebException {
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
	public void remove(Suppliers entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Suppliers entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public String saveSuppliers(Suppliers sup) {
		List<SubNature> subPurposeList = new ArrayList<SubNature>();
		Set<SubLedger> subLedgers = new HashSet<SubLedger>();
		Set<Product> products = new HashSet<Product>();

		sup.setCreated_date(new LocalDateTime());
		if ((sup.getTds_rate1() != null) && (sup.getTds_rate1() != "")) {
			sup.setTds_rate(Float.valueOf(sup.getTds_rate1()));
		}
		else
		{
			sup.setTds_rate((float)0);
		}

		String reverse_mecha = sup.getReverse_mecha();
		String gst_no = sup.getGst_no();
		String suppiler_category = null;
		switch (reverse_mecha) {
		case "Yes":
			suppiler_category = "Notified service goods";
			break;
		case "No":
			if (gst_no == "NA") {
				suppiler_category = "URD";
			} else {
				suppiler_category = "None";
			}
			break;
		}
		sup.setSuppiler_category(suppiler_category);

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
			
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		String company_name = sup.getCompany_name().replace("\"", "").replace("\'", "");
		sup.setCompany_name(company_name);
		
		Long id = supplierDao.saveSuppliersdao(sup);
		if (id != null) {
			/*Long year_id = yearDAO.findcurrentyear();
			Long opid = openingbalances.saveOpeningBalances(sup.getCompany().getCompany_id(), year_id, id, (long) 4, (double) 0, (double) 0);
			if (opid != 0) {
				sup.setOpeningId(opid);
				try {
					sup.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
			return " Supplier saved successfully";
		} else {
			return "Please try again ";
		}
	}

	@Transactional
	@Override
	public List<Suppliers> findAll() {
		return supplierDao.findAll();
	}

	@Transactional
	@Override
	public String deleteSupplierSubLedger(Long suId, Long subId) {

		String msg = supplierDao.deleteSupplierSubLedger(suId, subId);
		return msg;
	}

	@Transactional
	@Override
	public String deleteSupplierProduct(Long suId, Long pId) {

		String msg = supplierDao.deleteSupplierProduct(suId, pId);
		return msg;
	}

	@Transactional
	@Override
	public List<Suppliers> findByStatus(int status) {
		return supplierDao.findByStatus(status);
	}

	@Transactional
	@Override
	public String updateByApproval(Long supplierId, int status) {
		String msg = supplierDao.updateApprovalStatus(supplierId, status);
		return msg;
	}

	@Override
	public Suppliers isExists(String name) {
		return null;
	}

	@Transactional
	@Override
	public List<Suppliers> findAllSuppliersOfCompany(Long CompanyId) {
		return supplierDao.findAllSuppliersOfCompany(CompanyId);
	}

	@Transactional
	@Override
	public String deleteByIdValue(Long entityId) {
		return supplierDao.deleteByIdValue(entityId);
	}

	@Transactional
	@Override
	public List<Suppliers> findAllSuppliersListing(Long flag) {
		return supplierDao.findAllSuppliersListing(flag);
	}

	@Transactional
	@Override
	public List<Suppliers> findAllSuppliersListingOfCompany(Long CompanyId, Long flag) {
		return supplierDao.findAllSuppliersListingOfCompany(CompanyId, flag);
	}

	@Transactional
	@Override
	public Suppliers findOneWithAll(Long supId) {
		return supplierDao.findOneWithAll(supId);
	}

	@Transactional
	@Override
	public void addsupplieropeningbalance(Long company_id, Long sids, Long type, Double creditval, Double debitval) {
		Suppliers supplier = new Suppliers();
		try {
			supplier = supplierDao.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		Long year_id = yearDAO.findcurrentyear();
		if (year_id != 0) {
			Long opid = (long) 0;
			Long existid = openingbalances.findsupplierbalance(company_id, year_id, sids, (long) 4);
			if (existid == 0) {
				opid = openingbalances.saveOpeningBalances(company_id, year_id, sids, (long) 4, creditval, debitval);
			} else {
				opid = openingbalances.updatepeningbalance(existid, creditval, debitval);
			}
			if (opid != 0) {
				try {
					supplier.setOpeningId(opid);
					supplier.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Transactional
	@Override
	public void addsuppliertrsansactionbalance(Long year_id,LocalDate date,Long company_id, Long sids, Long type, Double creditval, Double debitval,
			Long flag) {
	/*	Suppliers supplier = new Suppliers();
		try {
			supplier = supplierDao.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}*/
		//Long year_id = yearDAO.findcurrentyear();
		if (year_id != 0) {
		/*	Long opid = (long) 0;*/
			
			Long existid = openingbalances.findsupplierbalancebydate(company_id, year_id, sids, (long) 4,date);
			
			if (existid == 0) {
			/*	opid =*/ openingbalances.saveOpeningBalancesbydate(date,company_id, year_id, sids, (long) 4, creditval, debitval,null,null,null,null,null,null,null,null,null,null,null,null);
			} else {
			/*	opid =*/ openingbalances.updateCDbalance(existid, creditval, debitval, flag);
			}
			/*if (opid != 0) {
				try {
					supplier.setOpeningId(opid);
					supplier.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}*/
		}
	}

	@Override
	public int isExistsPan(String companypan, String companyname, Long company_id, String email) {
		int opid = supplierDao.isExistsPan(companypan, companyname, company_id, email);
		return opid;
	}

	@Override
	public Boolean approvedByBatch(List<String> supplierList, Boolean primaryApproval) {
		return supplierDao.approvedByBatch(supplierList, primaryApproval);
	}

	@Override
	public Boolean rejectByBatch(List<String> supplierList) {
		return supplierDao.rejectByBatch(supplierList);
	}

	@Override
	public Suppliers isExistsSupplier(String owner_pan_no, String company_name, long company_id, String supplier_name) {
		return supplierDao.isExistsSupplier(owner_pan_no, company_name, company_id, supplier_name);
	}

	@Override
	public List<Suppliers> findAllSuppliersOnlyOfCompany(Long CompanyId) {
		return supplierDao.findAllSuppliersOnlyOfCompany(CompanyId);
	}

	@Override
	public List<Suppliers> findByStatus(Long role_id,int status, Long userId) {
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
		return supplierDao.findByStatus(status, companyIds);
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
			return supplierDao.findByStatus(status, companyIds);
		}
	}

	@Override
	public List<Suppliers> findAllSuppliersListing(Boolean flag, Long userId) {
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
		return supplierDao.findAllSuppliersListing(flag, companyIds);
	}

	@Transactional
	@Override
	public void addsupplieropeningbalancenew(Long company_id, Long sids, Long type, Double creditval, Double debitval,
			String date1, Long year) {
		Suppliers supplier = new Suppliers();
		try {
			supplier = supplierDao.findOne(sids);
		} catch (MyWebException e1) {
			e1.printStackTrace();
		}
		LocalDate date= new LocalDate(date1);
		//Long year = yearDAO.findcurrentyear();
		if (year != 0) {
			Long opid = (long) 0;
			Long existid = openingbalances.findsubledgerbalancebydate(company_id, year, sids, (long) 4,date,null,null,null,null,null,null,null,null,null,null,null,null);
			if (existid == 0) {
				opid = openingbalances.saveOpeningBalancesnew(company_id, year, sids, (long) 4, creditval, debitval,date1);
			} else {
				opid = openingbalances.updatepeningbalancenew(existid, creditval, debitval,date1,year);
			}
			if (opid != 0) {
				try {
					supplier.setOpeningId(opid);
					supplier.setOpeningbalances(openingbalances.getById(opid));
				} catch (MyWebException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	@Override
	public List<Suppliers> findAllOPbalanceofsupplier(long company_id, long flag) {
		// TODO Auto-generated method stub
		return supplierDao.findAllOPbalanceofsupplier(company_id,flag);
	}

	@Override
	public List<Suppliers> findAllSuppliersOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		return supplierDao.findAllSuppliersOfCompanyDashboard(companyId);
	}

	@Override
	public List<Suppliers> findByStatusDashboard(int approvalStatusPending, Long user_id) {
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
		return supplierDao.findByStatusDashboard(approvalStatusPending, companyIds);
	}

	@Override
	public List<Suppliers> findAllDashboard() {
		// TODO Auto-generated method stub
		return supplierDao.findAllDashboard();
	}

	@Override
	public Integer findByStatusSupplierCountDashboard(Long role_id, int approvalStatus, Long user_id) {
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
		return supplierDao.findByStatusSuppliersCountDashboard(approvalStatus, companyIds);
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
			return supplierDao.findByStatusSuppliersCountDashboard(approvalStatus, companyIds);
		}
	}

	@Override
	public Integer findAllSupplierCountDashboard() {
		// TODO Auto-generated method stub
		return supplierDao.findAllSuppliersCountDashboard();
	}

	@Override
	public Integer findAllSupplierCountOfCompanyDashboard(Long CompanyId) {
		// TODO Auto-generated method stub
		return supplierDao.findAllSuppliersCountOfCompanyDashboard(CompanyId);
	}

	@Override
	public List<ViewApprovalsForm> viewApprovals(Long role_id, Long userId) {
List<ViewApprovalsForm> approvallist = new ArrayList<ViewApprovalsForm>();	
		
		if(role_id.equals(MyAbstractController.ROLE_EXECUTIVE))
		{
		List<Suppliers> list = new ArrayList<>();	
		
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
		list = supplierDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
		if(list.size()>0)
		{
			try {
				User user = userDao.findOne(userId);
				ViewApprovalsForm form = new  ViewApprovalsForm();
				form.setSupplierList(list);
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
				List<Suppliers> list = new ArrayList<>();	
				
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
				list = supplierDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
				if(list.size()>0)
				{
					try {
						ViewApprovalsForm form = new  ViewApprovalsForm();
						form.setSupplierList(list);
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
						List<Suppliers> list = new ArrayList<>();	
						
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
						list = supplierDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setSupplierList(list);
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
                        List<Suppliers> list = new ArrayList<>();	
						
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
						list = supplierDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PRIMARY, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setSupplierList(list);
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
