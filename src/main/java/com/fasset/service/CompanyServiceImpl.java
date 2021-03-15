package com.fasset.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

import org.jfree.data.time.Year;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ccavenue.security.Md5;
import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IAccountGroupDAO;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IBankDAO;
import com.fasset.dao.interfaces.ICityDAO;
import com.fasset.dao.interfaces.IClientAllocationToKpoExecutiveDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.ICompanyStatutoryTypeDAO;
import com.fasset.dao.interfaces.ICountryDAO;
import com.fasset.dao.interfaces.IQuotationDAO;
import com.fasset.dao.interfaces.IRoleDAO;
import com.fasset.dao.interfaces.IServiceDAO;
import com.fasset.dao.interfaces.IServiceFrequencyDAO;
import com.fasset.dao.interfaces.IStateDAO;
import com.fasset.dao.interfaces.IUserDAO;
import com.fasset.dao.interfaces.IindustryTypeDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.ClientAllocationToKpoExecutive;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Company;
import com.fasset.entities.CompanyStatutoryType;
import com.fasset.entities.ForgotPasswordLog;
import com.fasset.entities.IndustryType;
import com.fasset.entities.JavaMD5Hash;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.UserForm;
import com.fasset.service.interfaces.IAccountingYearService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IMailService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IYearEndingService;

@Transactional
@Service
public class CompanyServiceImpl extends MyAbstractController implements ICompanyService {	
	
	@Autowired
	IClientAllocationToKpoExecutiveDAO clientAllocationToKpoExectiveDAO;
	
	@Autowired
	private ICompanyDAO companyDao;
	
	@Autowired 
	private IUserDAO userDao;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IindustryTypeDAO industryTypeDao;
	
	@Autowired
	private ICompanyStatutoryTypeDAO companyTypeDao;
	
	@Autowired
	private ICityDAO cityDao;
	
	@Autowired
	private IStateDAO stateDao;
	
	@Autowired
	private ICountryDAO countryDao;
	
	@Autowired
	private IRoleDAO roleDao;
	
	@Autowired
	private IBankDAO bankDao;
	
	@Autowired
	private IAccountingYearDAO accountYearDAO;
	
	@Autowired
	private IYearEndingService yearService ;
	
	@Autowired
	private IQuotationDAO quotationDAO;
	
	@Autowired
	private IServiceDAO serviceDAO ;

    @Autowired
	private IServiceFrequencyDAO frequencyDAO;

    @Autowired	
	private IClientSubscriptionMasterService SubscriptionService;
    @Autowired
	private IAccountingYearService accountingYearService ;
    
	@Override
	public void add(Company entity) throws MyWebException {
		Long id = userDao.saveUser(new User());
		User user = userDao.findOne(id);
		entity.getUser().add(user);
		companyDao.create(entity);		
	}

	@Override
	public void update(Company entity) throws MyWebException {
		
		System.out.println("The updationg of company");
		Company company = companyDao.findOne(entity.getCompany_id());
		company.setLandline_no(entity.getLandline_no());
		company.setCompany_name(entity.getCompany_name());
		company.setCurrent_address(entity.getCurrent_address());
		company.setPermenant_address(entity.getPermenant_address());
		company.setPan_no(entity.getPan_no());
		company.setPincode(entity.getPincode());
		company.setRegistration_no(entity.getRegistration_no());
		company.setRcm(entity.getRcm());
		company.setBusiness_nature(entity.getBusiness_nature());
		company.setGst_no(entity.getGst_no());
		company.setPte_no(entity.getPte_no());
		company.setPtr_no(entity.getPtr_no());
		company.setEway_bill_no(entity.getEway_bill_no());
		company.setIec_no(entity.getIec_no());
		company.setOther_tax_1(entity.getOther_tax_1());
		company.setOther_tax_2(entity.getOther_tax_2());
		company.setEmpLimit(entity.getEmpLimit());
		if(entity.getLogo()!=null && entity.getLogo()!=""){
			company.setLogo(entity.getLogo());
		}
		if(entity.getMobile()!=null && entity.getMobile()!=""){
			company.setMobile(entity.getMobile());
		}
		
		company.setVoucher_range(entity.getVoucher_range());
		company.setUpdated_date(new LocalDate());
		if(entity.getCompanyTypeId()!=null && entity.getCompanyTypeId() > 0 ){
			company.setCompany_statutory_type(companyTypeDao.findOne(entity.getCompanyTypeId()));
		}
		if(entity.getIndustryTypeId()!=null && entity.getIndustryTypeId() > 0){
			company.setIndustry_type(industryTypeDao.findOne(entity.getIndustryTypeId()));
		}
		if(entity.getCountryId() > 0 ){
			company.setCountry(countryDao.findOne(entity.getCountryId()));
		}
		if(entity.getStateId() > 0){
			company.setState(stateDao.findOne(entity.getStateId()));
		}
		if(entity.getCityId() > 0){
			company.setCity(cityDao.findOne(entity.getCityId()));
		}
		if(entity.getBankId() !=null && entity.getBankId() > 0){
			company.setBank(bankDao.findOne(entity.getBankId()));
		}
		/*if(entity.getYearRangeId() > 0){
			company.setYear_range(accountingYearDao.findOne(entity.getYearRangeId()));
		}*/
		companyDao.update(company);		
	}
	@Override
	public void updatebycomp(Company entity,String amount) throws MyWebException {
		
		Company company = companyDao.findOne(entity.getCompany_id());
		company.setLandline_no(entity.getLandline_no());
		company.setCompany_name(entity.getCompany_name());
		company.setCurrent_address(entity.getCurrent_address());
		company.setPermenant_address(entity.getPermenant_address());
		company.setPan_no(entity.getPan_no());
		company.setPincode(entity.getPincode());
		company.setRegistration_no(entity.getRegistration_no());
		company.setRcm(entity.getRcm());
		company.setBusiness_nature(entity.getBusiness_nature());
		company.setGst_no(entity.getGst_no());
		company.setPte_no(entity.getPte_no());
		company.setPtr_no(entity.getPtr_no());
		company.setEway_bill_no(entity.getEway_bill_no());
		company.setIec_no(entity.getIec_no());
		company.setOther_tax_1(entity.getOther_tax_1());
		company.setOther_tax_2(entity.getOther_tax_2());
		company.setStatus(entity.getStatus());
		company.setEmpLimit(entity.getEmpLimit());
		company.setSubscription_from(entity.getSubscription_from());
		company.setSubscription_to(entity.getSubscription_to());
		company.setEmail_id(entity.getEmail_id());
		//company.setYearRange(entity.getYearRange());
		
		String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018
		LocalDate date= new LocalDate();
		String april1stDate=null;
		april1stDate= currentyear+"-"+"04"+"-"+"01";
		LocalDate april1stLocaldate = new LocalDate(april1stDate);
		
		if(date.isBefore(april1stLocaldate)) 
		{
			Integer year = Integer.parseInt(currentyear);
			year=year-1;
			String lastYear =year.toString();
			currentyear=lastYear+"-"+currentyear;
		}
		else if(date.isAfter(april1stLocaldate) || date.equals(april1stLocaldate))
		{
			Integer year = Integer.parseInt(currentyear);
			year=year+1;
			String nextYear =year.toString();
			currentyear=currentyear+"-"+nextYear;
			
		}
		StringBuilder YearRange=new StringBuilder();
		List<Long> yearIds = new ArrayList<Long>();
		if(entity.getYearRange()!=null){
			System.out.println("year range is "+entity.getYearRange());
		 String[] yearsNew=entity.getYearRange().split(",");
		 for (String yId : yearsNew) {
				System.out.println("The new years "+ yId);
				if((yearIds.contains(new Long(yId)))!=true){
					System.out.println("Adding new");
					YearRange.append((yId+","));
				yearIds.add(new Long(yId));}
			}
		}
      /*String[] years=company.getYearRange().split(",");
		
		for (String yId : years) {
			System.out.println("The old id" +yId);
			YearRange.append((yId+","));
			yearIds.add(new Long(yId));
		}*/
		
	for(Long id : yearIds)
		{
		System.out.println("YID"+id);
			AccountingYear accYear = new AccountingYear();
			accYear = accountYearDAO.findOne(id);
			
			if(yearService.findYearEnd(accYear.getYear_id(), entity.getCompany_id())==null)
			{
				YearEnding year = new YearEnding();
				year.setCompany(companyDao.findOne(entity.getCompany_id()));
				year.setAccountingYear(accYear);
				year.setIsMailSent(false);
				year.setIsApprovedForEditingAccYr(false);
				if(currentyear.equalsIgnoreCase(accYear.getYear_range()))
				{
					year.setYearEndingstatus(MyAbstractController.ACTIVE_ACCOUNT_YEAR);
				}
				else
				{
					year.setYearEndingstatus(MyAbstractController.DEACTIVE_ACCOUNT_YEAR);
				}
			
				
				yearService.add(year);
			}
			
		}
	System.out.println("The range is " +YearRange);
	String AccYearRange=YearRange.substring(0, YearRange.length() - 1);
		company.setYearRange(AccYearRange);
		if(entity.getLogo()!=null && entity.getLogo()!=""){
			company.setLogo(entity.getLogo());
		}
		if(entity.getMobile()!=null && entity.getMobile()!=""){
			company.setMobile(entity.getMobile());
		}
		
		company.setVoucher_range(entity.getVoucher_range());
		company.setUpdated_date(new LocalDate());
		if(entity.getCompanyTypeId() > 0){
			company.setCompany_statutory_type(companyTypeDao.findOne(entity.getCompanyTypeId()));
		}
		if(entity.getIndustryTypeId() > 0){
			company.setIndustry_type(industryTypeDao.findOne(entity.getIndustryTypeId()));
		}
		if(entity.getCountryId() > 0 ){
			company.setCountry(countryDao.findOne(entity.getCountryId()));
		}
		if(entity.getStateId() > 0){
			company.setState(stateDao.findOne(entity.getStateId()));
		}
		if(entity.getCityId() > 0){
			company.setCity(cityDao.findOne(entity.getCityId()));
		}
		if(entity.getBankId() > 0){
			company.setBank(bankDao.findOne(entity.getBankId()));
		}
		Integer count =0;
		Set<QuotationDetails> quotationDetails = new HashSet<QuotationDetails>();
		Set<QuotationDetails> newquotationDetails = new HashSet<QuotationDetails>();
		Float totalamount=0.0f;
		
		if(amount!=null || amount!=("0") || amount!=("")  )
		{
			totalamount = new Float(amount);
		}
		
		ClientSubscriptionMaster obj= SubscriptionService.getClientSubscriptionByCompanyId(entity.getCompany_id());
		if(obj!=null)
		{
		if(company.getStatus()==2)
		{
				companyDao.update(company);		
		}
		else
		{
		obj.setSubscription_amount(totalamount);
		obj.setSubscription_from(entity.getSubscription_from());
		obj.setSubscription_to(entity.getSubscription_to());
		SubscriptionService.update(obj);
		
		}
		Quotation quot = quotationDAO.findOne(obj.getQuotation_id().getQuotation_id());
		quotationDetails = quot.getQuotationDetails();
		count = quotationDetails.size();
		
		if(entity.getServiceFreq()!=null && !entity.getServiceFreq().equals(""))
		{
		JSONArray jsonArray = new JSONArray(entity.getServiceFreq());
		totalamount = totalamount/((float)jsonArray.length()+(float)count);
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			QuotationDetails details = new QuotationDetails();
			/*details.setAmount(totalamount);*/
			details.setAmount(0.0f);
			details.setService_status(true);
			details.setService_id(serviceDAO.findOne(Long.parseLong(jsonObject.getString("serviceId"))));
			details.setFrequency_id(frequencyDAO.findOne(Long.parseLong(jsonObject.getString("frequencyId"))));
			details.setQuotation_id(quot);
			newquotationDetails.add(details);
		}
		}
		else
		{
			totalamount = totalamount/(float)count;
		}
		
		for(QuotationDetails details :quotationDetails)
		{
			/*details.setAmount(totalamount);*/
			details.setAmount(0.0f);
			details.setQuotation_id(quot);
			newquotationDetails.add(details);
		}
		quot.setQuotationDetails(newquotationDetails);
		quotationDAO.update(quot);
		companyDao.update(company);		
		}
	}
	@Override
	public List<Company> list() {
		return companyDao.findAll();
	}
	@Override
	public List<Company> FindAlllist()
	{
		return companyDao.FindAlllist();
	}
	@Override
	public List<Company> FindAlllistexe(Long user_id)
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
		return companyDao.FindAlllistexe(companyIds);
	}
	@Override
	public Company getById(Long id) throws MyWebException {
		return companyDao.findOne(id);
	}

	@Override
	public Company getById(String id) throws MyWebException {
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
	public void remove(Company entity) throws MyWebException {
		// TODO Auto-generated method stub		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Company entity) throws MyWebException {
		// TODO Auto-generated method stub		
	}

	@Override
	public Long saveSignUpDetails(Company company, User user) {
		Long companyId = null;
		try {
			
			if(company.getCompany_statutory_type() == null) {
				if(company.getCompanyTypeId() != null){
					CompanyStatutoryType companyType = companyTypeDao.findOne(company.getCompanyTypeId());
					company.setCompany_statutory_type(companyType);
				}
			}
			if(company.getIndustry_type() == null) {
				if(company.getIndustryTypeId() != null){
					IndustryType industryType = industryTypeDao.findOne(company.getIndustryTypeId());
					company.setIndustry_type(industryType);
				}
			}
			if(company.getStatus()==MyAbstractController.STATUS_TRIAL_LOGIN)
			{
				company.setSubscription_from(new LocalDate());
				company.setSubscription_to(new LocalDate().plusDays(7));
				company.setCreated_date(new LocalDate());
				company.setIstrialClient(true);
				companyId = companyDao.saveCompany(company);
			}
			else
			{
				company.setCreated_date(new LocalDate());
				companyId = companyDao.saveCompany(company);
			}
			
			user.setCompany(companyDao.findOne(companyId));
			user.setCreated_date(new LocalDate());
			user.setEmail(company.getEmail_id());
			user.setMobile_no(company.getMobile());
			user.setPassword(Md5.md5(userService.getRandPassword()));
			
			
			ForgotPasswordLog forgotPasswordLog = new ForgotPasswordLog();
			forgotPasswordLog.setDate(new LocalDate());
			forgotPasswordLog.setType(MyAbstractController.PASSWORD_NEW);
			forgotPasswordLog.setUser(user);
			Set<ForgotPasswordLog> forgotPasswordLogSet = new HashSet<ForgotPasswordLog>();
			forgotPasswordLogSet.add(forgotPasswordLog);
			user.setForgotPassword(forgotPasswordLogSet);
			userDao.create(user);
			if(user.getRole().getRole_id().equals(MyAbstractController.ROLE_TRIAL_USER))
			{
				List<AccountingYear>acclist = accountYearDAO.findAll();
				AccountingYear CurrentAccyear = null;
				/*String currentyear = new Year().toString();
				LocalDate date= new LocalDate();
				String april1stDate=null;
				april1stDate= currentyear+"-"+"04"+"-"+"01";
				LocalDate april1stLocaldate = new LocalDate(april1stDate);
				
				if(date.isBefore(april1stLocaldate)) 
				{
					Integer year = Integer.parseInt(currentyear);
					year=year-1;
					String lastYear =year.toString();
					currentyear=lastYear+"-"+currentyear;
				}
				else if(date.isAfter(april1stLocaldate) || date.equals(april1stLocaldate))
				{
					Integer year = Integer.parseInt(currentyear);
					year=year+1;
					String nextYear =year.toString();
					currentyear=currentyear+"-"+nextYear;
					
				}*/
				for(AccountingYear year : acclist)
				{
				 /*   //CURRENT YEAR LIKE 2018
					String[] accYears = year.getYear_range().split("-");// 2017-2018 BECOMES TWO STRING AS 2017 AND 2018
					String currentAccountYr = accYears[0];*/
					
					//if(currentyear.equalsIgnoreCase(year.getYear_range()))
					if(company.getYearRangeId()==(year.getYear_id()))
					{
						CurrentAccyear=year;
						break;
					}
				}
				
				if(CurrentAccyear!=null)
				{
				YearEnding yearend = new YearEnding();
				yearend.setCompany(companyDao.findOne(companyId));
				yearend.setAccountingYear(CurrentAccyear);
				yearend.setIsMailSent(false);
				yearend.setIsApprovedForEditingAccYr(false);
				yearend.setYearEndingstatus(MyAbstractController.ACTIVE_ACCOUNT_YEAR);
				yearService.add(yearend);
				}
				else
				{
					for(AccountingYear year : acclist)
					{
						YearEnding yearend = new YearEnding();
						yearend.setCompany(companyDao.findOne(companyId));
						yearend.setAccountingYear(year);
						yearend.setIsMailSent(false);
						yearend.setIsApprovedForEditingAccYr(false);
						yearend.setYearEndingstatus(MyAbstractController.ACTIVE_ACCOUNT_YEAR);
						yearService.add(yearend);
						break;
					}
					
				}
				
			}
			mailService.sendSignupMail(user);
				
			
		} catch (MyWebException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return companyId;
	}

	@Override
	public Company isExists(String name) {
		return null;
	}

	@Override
	public List<Company> findAll() {
		return companyDao.findAll();
	}

	@Override
	public void addCompany(UserForm userForm)throws MyWebException {
		Company company = userForm.getCompany();
		User user = userForm.getUser();
		User user1 = new User();
		User user2 = new User();
		Set<User> userList = new HashSet<User>();
		company.setCreated_date(new LocalDate());
		company.setEmail_id(user.getEmail());
		company.setMobile(user.getMobile_no());
		if(company.getCompanyTypeId() > 0){
			company.setCompany_statutory_type(companyTypeDao.findOne(company.getCompanyTypeId()));
		}
		if(company.getIndustryTypeId() > 0){
			company.setIndustry_type(industryTypeDao.findOne(company.getIndustryTypeId()));
		}
		if(company.getCountryId() > 0 ){
			company.setCountry(countryDao.findOne(company.getCountryId()));
			user.setCountry_id(company.getCountryId());
			user.setCountry(countryDao.findOne(company.getCountryId()));
		}
		if(company.getStateId() > 0){
			company.setState(stateDao.findOne(company.getStateId()));
			user.setState_id(company.getStateId());
			user.setState(stateDao.findOne(company.getStateId()));
		}
		if(company.getCityId() > 0){
			company.setCity(cityDao.findOne(company.getCityId()));
			user.setCity_id(company.getCityId());
			user.setCity(cityDao.findOne(company.getCityId()));
		}
		
		user.setCreated_date(new LocalDate());
		/*
		 * user1 = userService.getUser(user.getUser_id(), ROLE_CLIENT); user2 =
		 * userService.getUser(user.getUser_id(), ROLE_TRIAL_USER); if(user1== null) {
		 * 
		 * user= user2; } else { user =user1; }
		 */
		user.setRole(roleDao.findOne(MyAbstractController.ROLE_CLIENT));
		//user.setRole(roleDao.findOne(user.getRole().getRole_id()));
		user.setCompany(company);
		
		if((company.getStatus()==MyAbstractController.STATUS_INACTIVE)||(company.getStatus()==MyAbstractController.STATUS_PENDING_FOR_APPROVAL))
		{
			user.setStatus(false);
		}
		else
		{
			user.setStatus(true);
		}
		user.setIs_updated(false);
		
		user.setPassword(Md5.md5(user.getPassword()));
		userList.add(user);
		company.setUser(userList);
		
		// quotation details
		
		String quotationNo = null;
		Long quid = quotationDAO.getLastQuotationId();
		
		String[] diviededDate = new LocalDate().toString().split("-");
		String currentMonth = diviededDate[1];
		String lastString = "00"+quid.toString();
		
		char[]cuurentyeararray = new Year().toString().toCharArray(); 
		StringBuilder cuurentyear3 = new StringBuilder();
		cuurentyear3.append(cuurentyeararray[2]);
		cuurentyear3.append(cuurentyeararray[3]);
		Integer start1=Integer.parseInt(cuurentyear3.toString());
		Integer start2 = start1+1;
		
		quotationNo=start1.toString()+start2.toString()+currentMonth+lastString;
		
		Quotation quot = new Quotation();
		quot.setCompany_name(company.getCompany_name());
		quot.setEmail(user.getEmail());
		quot.setFirst_name(user.getFirst_name());
		quot.setLast_name(user.getLast_name());
		quot.setMobile_no(user.getMobile_no());
		quot.setStatus(true);
		quot.setCompany_statutory_id(company.getCompanyTypeId());
		quot.setIndustry_id(company.getIndustryTypeId());
		quot.setFlag(true);
		quot.setQuotation_no(quotationNo);
		System.out.println("freq is "+company.getServiceFreq());
		JSONArray jsonArray = new JSONArray(company.getServiceFreq());
		Float amount = new Float(user.getAmount());
		amount = amount/(float)jsonArray.length();
		Set<QuotationDetails> quotationDetails = new HashSet<>();
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			QuotationDetails details = new QuotationDetails();
			/*details.setAmount(amount);*/
			details.setAmount(0.0f);
			details.setService_status(true);
			details.setService_id(serviceDAO.findOne(Long.parseLong(jsonObject.getString("serviceId"))));
			details.setFrequency_id(frequencyDAO.findOne(Long.parseLong(jsonObject.getString("frequencyId"))));
			details.setQuotation_id(quot);
			quotationDetails.add(details);
		}
		quot.setQuotationDetails(quotationDetails);
		Long qid  = quotationDAO.savequote(quot);
		Quotation quot1=quotationDAO.findOne(qid);
		
		ClientSubscriptionMaster clientSubscriptionMaster = new ClientSubscriptionMaster();
		clientSubscriptionMaster.setSubscription_amount(new Float(user.getAmount()));
		clientSubscriptionMaster.setPayment_mode(MyAbstractController.PAYMENT_OFFLINE);
		clientSubscriptionMaster.setCompany(company);
		clientSubscriptionMaster.setCreated_date(new LocalDate());
		clientSubscriptionMaster.setStatus(true);
		clientSubscriptionMaster.setSubscription_from(company.getSubscription_from());
		clientSubscriptionMaster.setSubscription_to(company.getSubscription_to());
		clientSubscriptionMaster.setQuotation_id(quot1);
		
		Set<ClientSubscriptionMaster> sets = new HashSet<ClientSubscriptionMaster>();
		sets.add(clientSubscriptionMaster);
		company.setClientSubscriptionMasters(sets);
		
		long comapny_id = companyDao.createComapny(company);
		System.out.println("The newly created co id is"+comapny_id);
		String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018
		LocalDate date= new LocalDate();
		String april1stDate=null;
		april1stDate= currentyear+"-"+"04"+"-"+"01";
		LocalDate april1stLocaldate = new LocalDate(april1stDate);
		
		if(date.isBefore(april1stLocaldate)) 
		{
			Integer year = Integer.parseInt(currentyear);
			year=year-1;
			String lastYear =year.toString();
			currentyear=lastYear+"-"+currentyear;
		}
		else if(date.isAfter(april1stLocaldate) || date.equals(april1stLocaldate))
		{
			Integer year = Integer.parseInt(currentyear);
			year=year+1;
			String nextYear =year.toString();
			currentyear=currentyear+"-"+nextYear;
			
		}
		
		String[] years=company.getYearRange().split(",");
		List<Long> yearIds = new ArrayList<Long>();
		for (String yId : years) {
			yearIds.add(new Long(yId));
		}
		for(Long id : yearIds)
		{
			AccountingYear accYear = new AccountingYear();
			accYear = accountYearDAO.findOne(id);
			/*String[] accYears = accYear.getYear_range().split("-");// 2017-2018 BECOMES TWO STRING AS 2017 AND 2018
			String currentAccountYr = accYears[0];*/
			YearEnding year = new YearEnding();
			year.setCompany(companyDao.findOne(comapny_id));
			year.setAccountingYear(accYear);
			year.setIsMailSent(false);
			year.setIsApprovedForEditingAccYr(false);
			if(currentyear.equalsIgnoreCase(accYear.getYear_range()))
			{
				year.setYearEndingstatus(MyAbstractController.ACTIVE_ACCOUNT_YEAR);
			}
			else
			{
				year.setYearEndingstatus(MyAbstractController.DEACTIVE_ACCOUNT_YEAR);
			}
			yearService.add(year);
		}
		
		try {
			mailService.sendMailToClientAfterAddingCompany(user);
		} catch (MessagingException e) {
			e.printStackTrace();
		}catch(Exception m){
			
		}
		
	}

	@Override
	public List<Company> getCompanyByStatus(Integer status) {
		return companyDao.getCompanyByStatus(status);
	}

	@Override
	public void companyDeactivate(Company company) {
		try {
			companyDao.update(company);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Company> getApprovedCompanies() {
		return companyDao.getApprovedCompanies();
	}

	@Override
	public Company getCompanyWithAllUsers(Long companyId) {		
		return companyDao.getCompanyWithAllUsers(companyId);
	}

	@Override
	public Company getCompanyWithIndustrytype(Long companyId) {
		return companyDao.getCompanyWithIndustrytype(companyId);
	}

	@Override
	public List<Company> getAllCompaniesWithLedgerlist() {
		return companyDao.getAllCompaniesWithLedgerlist();
	}

	@Override
	public Company findOneWithAll(Long compId) {		
		return companyDao.findOneWithAll(compId);
	}

	@Override
	public List<Company> getAllCompaniesWithLedgerAndCustomerlist() {		
		return companyDao.getAllCompaniesWithLedgerAndCustomerlist();
	}

	@Override
	public List<Company> getAllCompaniesWithLedgerAndSuppilerlist() {
		return companyDao.getAllCompaniesWithLedgerAndSuppilerlist();
	}

	@Override
	public List<String> getVoucherRange(Long companyId) {
		Company comp;
		List<String> range = null;
		try {
			comp = companyDao.findOne(companyId);
			System.out.println(comp.getVoucher_range());
			range=companyDao.getVoucherRange(companyId, comp.getVoucher_range());
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		
		return range;
	}

	@Override
	public Boolean setDefaultBank(Long bankId, Long compId,Long flag) {
		try {
			Company company = companyDao.findOne(compId);
			if(flag==1)
				company.setBank(null);
			else
			company.setBank(bankDao.findOne(bankId));
			companyDao.update(company);
		} catch (MyWebException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Boolean setTrialBalance(Long compId,LocalDate openingBalancedate,Long YearId) {
		try {
			Company company = companyDao.findOne(compId);
			
			AccountingYear yearRange = accountingYearService.findAccountRange(YearId);
			company.setTrial_balance(true);
			company.setOpeningbalance_date(openingBalancedate);
			company.setYear_range(yearRange);
			companyDao.update(company);
		} catch (MyWebException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	@Override
	public List<Company> getAllCompaniesWithAccountGrouplist() {
		return companyDao.getAllCompaniesWithAccountGrouplist();
	}

	@Override
	public String deleteByIdValue(long id) {
		String msg = companyDao.deleteByIdValue(id);
		return msg;
	}

	@Override
	public int isExistscompanyname(String company_name) {
		int exid=companyDao.isExistscompanyname(company_name);
		return exid;
	}

	@Override
	public List<Company> getAllCompaniesWithProducts() {
		return companyDao.getAllCompaniesWithProducts();
	}

	@Override
	public List<Company> getAllCompaniesOnly() {
		return companyDao.getAllCompaniesOnly();
	}

	@Override
	public List<Company> getAllCompaniesExe(Long userId) {
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(userId);
		List<Company> companies = new ArrayList<Company>();
		if(executiveList .isEmpty()){
			companies.add(null);
		}
		else
		{	
			for(ClientAllocationToKpoExecutive com : executiveList ){
				companies.add(com.getCompany());
			}
		}
		 Collections.sort(companies, new Comparator<Company>() {
	            public int compare(Company company1, Company company2) {
	            return company1.getCompany_name().trim().toLowerCase().compareTo(company2.getCompany_name().trim().trim().toLowerCase());
	            }
	        });
		return companies;
	}

	@Override
	public List<Company> getAllCompaniesWithLedgerlist(Long userId) {
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(userId);
		List<Long> companies = new ArrayList<Long>();
		if(executiveList .isEmpty()){
			companies.add((long) 0);
		}
		else
		{	
			for(ClientAllocationToKpoExecutive com : executiveList ){
				companies.add(com.getCompany().getCompany_id());
			}
		}
		return companyDao.getAllCompaniesWithLedgerlist(companies);
	}

	@Override
	public List<Company> getApprovedCompanies(Long userId) {
		List<ClientAllocationToKpoExecutive> executiveList = clientAllocationToKpoExectiveDAO.findCompanyByExecutiveId(userId);
		List<Long> companies = new ArrayList<Long>();
		if(executiveList .isEmpty()){
			companies.add((long) 0);
		}
		else
		{	
			for(ClientAllocationToKpoExecutive com : executiveList ){
				companies.add(com.getCompany().getCompany_id());
			}
		}
		return companyDao.getApprovedCompanies(companies);
	}

	@Override
	public Company getCompanyWithCompanyStautarType(Long companyId) {
		// TODO Auto-generated method stub
		return companyDao.getCompanyWithCompanyStautarType(companyId);
	}

	@Override
	public long getdefaultbank(long company_id) {
		long bid=0;
		try {
			Company company = companyDao.findOne(company_id);
			if(company.getBank()!=null)
				bid=company.getBank().getBank_id();			
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		System.out.println("Default value is "+bid);
		return bid;
	}

	@Override
	public int ismailsent(Long company_id, LocalDate date) {
		// TODO Auto-generated method stub
		return companyDao.ismailsent(company_id,date);
	}

	@Override
	public void updatemailsent(Long company_id, LocalDate date) {
		companyDao.updatemailsent(company_id,date);		
	}

	@Override
	public List<Company> FindAllInactiveCompanies(Long role, Long user_id) {
		// TODO Auto-generated method stub
		return companyDao.FindAllInactiveCompanies(role,user_id);
	}

	@Override
	public void updateCompany(Company company) {
		// TODO Auto-generated method stub
		try {
			companyDao.update(company);
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Company getCompanyById(Long companyId) {
		// TODO Auto-generated method stub
		return companyDao.getCompanyById(companyId);
	}
	
}