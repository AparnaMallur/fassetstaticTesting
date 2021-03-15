package com.fasset.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IClientAllocationToKpoExecutiveDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IGstTaxMasterDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.ITaxMasterDAO;
import com.fasset.dao.interfaces.IUnitOfMeasurementDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.Ledger;
import com.fasset.entities.Product;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ViewApprovalsForm;
import com.fasset.service.interfaces.IProductService;

@Transactional
@Service
public class ProductServiceImpl implements IProductService{
	
	@Autowired
	IClientAllocationToKpoExecutiveDAO clientAllocationToKpoExectiveDAO;
	
	@Autowired
	private IProductDAO productDao;
	
	@Autowired
	private IGstTaxMasterDAO gstDao;
	
	@Autowired
	private IUnitOfMeasurementDAO uomDao;

	@Autowired
	private ITaxMasterDAO taxMasterDAO ;
	
	@Autowired
	private ICompanyDAO companyDAO;
	
	@Autowired
	private IUserDAO userDao;
	
	@Override
	public void add(Product entity) throws MyWebException {
		entity.setCreated_date(new LocalDate());
		if(entity.getUnit() != null || entity.getUnit() > 0){
			entity.setUom(uomDao.findOne(entity.getUnit()));
		}		
		if(entity.getTax_id()!=null && entity.getTax_id()>0 ){
			entity.setGst_id(gstDao.findOne(entity.getTax_id()));
		}
		if(entity.getVat_id()!=null && entity.getVat_id()>0){
			entity.setTaxMaster(taxMasterDAO.findOne(entity.getVat_id()));
		}
		String pname = entity.getProduct_name().replace("\"", "").replace("\'", "");
		entity.setProduct_name(pname);
		productDao.create(entity);
	}

	@Override
	public String addProductExcel(Product entity) {		
		try {
			productDao.create(entity);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		return "Product Saved Successfully";
	}

	
	@Override
	public void update(Product entity) throws MyWebException {
		Product product = productDao.findOne(entity.getProduct_id());
		String pname = entity.getProduct_name().replace("\"", "").replace("\'", "");
		product.setProduct_name(pname);
		product.setHsn_san_no(entity.getHsn_san_no());
		product.setType(entity.getType());
		product.setSales_rate(entity.getSales_rate());
		product.setStatus(entity.getStatus());
		product.setUpdated_date(new LocalDate());
		product.setTax_type(entity.getTax_type());
		product.setFlag(true);
/*		if((product.getProduct_approval()==1) || (product.getProduct_approval()==3))
*/			
		product.setProduct_approval(entity.getProduct_approval());
		if(entity.getUnit() != null || entity.getUnit() > 0){
			product.setUom(uomDao.findOne(entity.getUnit()));
		}		
		if(entity.getTax_id()!=null && entity.getTax_id()>0 ){
			product.setGst_id(gstDao.findOne(entity.getTax_id()));
		}
		if(entity.getVat_id()!=null && entity.getVat_id()>0){
			product.setTaxMaster(taxMasterDAO.findOne(entity.getVat_id()));
		}
		
		productDao.update(product);
	}

	@Override
	public void updateExcel(Product entity) throws MyWebException {
		Product product = productDao.findOne(entity.getProduct_id());
		product.setProduct_name(entity.getProduct_name());
		
		if(entity.getHsn_san_no()!=null)
		{
		product.setHsn_san_no(entity.getHsn_san_no());
		}
		
		product.setType(entity.getType());
		product.setSales_rate(entity.getSales_rate());
		product.setStatus(entity.getStatus());
		product.setProduct_approval(0);
		if(entity.getGst_id()!=null)
		{
		product.setGst_id(entity.getGst_id());
		}
		if(entity.getTaxMaster()!=null)
		{
			product.setTaxMaster(entity.getTaxMaster());
		}
		product.setUpdated_date(new LocalDate());
		product.setTax_type(entity.getTax_type());
		product.setFlag(entity.getFlag());		
		productDao.update(product);
	}
	
	@Override
	public List<Product> list() {
		return productDao.findAll();
	}

	@Override
	public Product getById(Long id){
		Product product = new Product();
		try{
			product =productDao.findOne(id);
		}
		catch(Exception e ){
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public Product getById(String id) throws MyWebException {
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
	public void remove(Product entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Product entity) throws MyWebException {
		// TODO Auto-generated method stub		
	}

	@Override
	public List<Product> findByStatus(int status) {
		return productDao.findByStatus(status);
	}

	@Override
	public String updateByApproval(Long productId, int status) {
		String msg = productDao.updateByApproval(productId, status);
		return msg;
	}

	@Override
	public Product isExists(String name) {		
		return productDao.isExists(name);
	}

	@Override
	public Product isExists(String name,Long company_id) {		
		return productDao.isExists(name,company_id);
	}
	
	@Override
	public List<Product> findAllProductsOfCompany(Long CompanyId) {
		return productDao.findAllProductsOfCompany(CompanyId);
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		return productDao.deleteByIdValue(entityId);
	}

	@Override
	public List<Product> findAllListing(Boolean flag) {
		return productDao.findAllListing(flag);
	}

	@Override
	public List<Product> findAllListingProductsOfCompany(long company_id, Boolean flag) {
		return productDao.findAllListingProductsOfCompany(company_id,flag);
	}

	@Override
	public Product findOneView(Long productId) {
		return productDao.findOneView(productId);
	}
	
	@Override
	public void setdefaultdata(long company_id, User user) {
		
		int exid=productDao.isExistsProduct("Non-Inventory",company_id);
		
		if(exid==0)
		{
			Product product =  new Product();
			product.setProduct_name("Non-Inventory");
			product.setType(2);
			product.setTax_type((long) 1);
			product.setStatus(true);
			product.setCreated_date(new LocalDate());
			product.setFlag(true);
			product.setProduct_approval(3);
			product.setAllocated(true);
			product.setCreated_by(user);
			try {
				product.setUom(uomDao.findOne((long) 43));
				product.setCompany(companyDAO.findOne(company_id));
				productDao.create(product);
			} catch (MyWebException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	@Override
	public Boolean approvedByBatch(List<String> productList,Boolean primaryApproval) {
		return productDao.approvedByBatch(productList,primaryApproval);
	}

	@Override
	public Boolean rejectByBatch(List<String> productList) {
		
		return productDao.rejectByBatch(productList);
	}

	@Override
	public int isExistsProduct(String product_name, Long company_id) {
		int exid=productDao.isExistsProduct(product_name,company_id);
		return exid;
	}

	@Override
	public Integer checktype(long company_id, Long product_id) {
		return productDao.checktype(company_id, product_id);
	}

	@Override
	public List<Product> findByStatus(Long role_id,int status, Long userId) {
		
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
		return productDao.findByStatus(status, companyIds);
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
			return productDao.findByStatus(status, companyIds);
		}
	}

	@Override
	public List<Product> findAllListing(Boolean flag, Long userId) {
		
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
		return productDao.findAllListing(flag, companyIds);
	}

	@Override
	public List<Product> findAllProductsOnlyOfCompany(Long CompanyId) {
		// TODO Auto-generated method stub
		return productDao.findAllProductsOnlyOfCompany(CompanyId);
	}

	@Override
	public Integer findAllProductsOfCompanyDashboard(long companyId) {
		// TODO Auto-generated method stub
		return productDao.findAllProductsOfCompanyDashboard(companyId);
	}

	@Override
	public Integer findByStatusDashboard(Long role_id,int Approvalstatus, Long user_id) {
		// TODO Auto-generated method stub//
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
		return productDao.findByStatusDashboard(Approvalstatus, companyIds);
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
			return productDao.findByStatusDashboard(Approvalstatus, companyIds);
		}
	}

	@Override
	public Integer findAllDashboard() {
		// TODO Auto-generated method stub
		return productDao.findAllDashboard();
	}

	@Override
	public List<ViewApprovalsForm> viewApprovals(Long role_id, Long userId) {
		
      List<ViewApprovalsForm> approvallist = new ArrayList<ViewApprovalsForm>();	
		
		if(role_id.equals(MyAbstractController.ROLE_EXECUTIVE))
		{
		List<Product> list = new ArrayList<>();	
		
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
		list = productDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
		if(list.size()>0)
		{
			try {
				User user = userDao.findOne(userId);
				ViewApprovalsForm form = new  ViewApprovalsForm();
				form.setProductList(list);
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
				List<Product> list = new ArrayList<>();	
				
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
				list = productDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
				if(list.size()>0)
				{
					try {
						ViewApprovalsForm form = new  ViewApprovalsForm();
						form.setProductList(list);
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
						List<Product> list = new ArrayList<>();	
						
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
						list = productDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PENDING, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setProductList(list);
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
                        List<Product> list = new ArrayList<>();	
						
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
						list = productDao.findByStatus(MyAbstractController.APPROVAL_STATUS_PRIMARY, companyIds);
						if(list.size()>0)
						{
							try {
								ViewApprovalsForm form = new  ViewApprovalsForm();
								form.setProductList(list);
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


