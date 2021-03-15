package  fasset.PDF.scheduled;;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


import org.jfree.data.time.Year;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Company;
import com.fasset.entities.Payment;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.Receipt;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ExceptionReport1Form;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IMailService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IYearEndingService;

@EnableScheduling
@Component
public class EmailNotification extends MyAbstractController {

	@Autowired
	private IMailService mailService;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICompanyService companyService;
	
	@Autowired	
	private IYearEndingService yearService;
	
	@Autowired	
	private IClientSubscriptionMasterService SubscriptionService;	
	
	@Autowired
	private IPaymentService paymentService;

	@Autowired
	private IReceiptService receiptService;
	
	List<Company> trialCompanies = new ArrayList<>();
	List<Company> paidCompanies = new ArrayList<>();
	
	/*@Transactional
	@Scheduled(fixedDelay = 43200000)
	public void doTask1() {
		
		
		 synchronized(this)
		 {
		 trialCompanies.clear();
		 paidCompanies.clear();
	     trialCompanies = companyService.getCompanyByStatus(STATUS_TRIAL_LOGIN);
	     paidCompanies = companyService.getCompanyByStatus(STATUS_SUBSCRIBED_USER);
		 LocalDate date=new LocalDate();

		if (trialCompanies != null && trialCompanies.size()>0) {
			
			
			for (Company company : trialCompanies) {
			   
				try 
				{
				
				if(company.getSubscription_to() != null) {
					
					int remainingDays = Days.daysBetween(date,company.getSubscription_to()).getDays();
					
					if (remainingDays == 2 || remainingDays == 7 || remainingDays == 10 || remainingDays == 12 || remainingDays == 0) {
						Set<User> user = company.getUser();
						if (remainingDays == 0) {
							
							int mailsent=companyService.ismailsent(company.getCompany_id(),date);
							if(mailsent==0)
							{
								 
								
							for (User newuuser : user) {
		
								mailService.trialPeriodEndReminder(newuuser, remainingDays);	
								newuuser.setStatus(false);
								userService.userDeactivate(newuuser);
								
							}
							company.setStatus(STATUS_INACTIVE);
							companyService.companyDeactivate(company);
							
							
							}
							
						}
						else {
							for (User mailToUser : user) {
								if (mailToUser.getRole()!=null && mailToUser.getRole().getRole_id() == ROLE_CLIENT) {
									int mailsent=companyService.ismailsent(company.getCompany_id(),date);
									if(mailsent==0)
									{		
									
									mailService.trialPeriodEndReminder(mailToUser, remainingDays);	
									companyService.updatemailsent(company.getCompany_id(),date);
									
									}
									break;
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
		
		if ( paidCompanies!= null &&  paidCompanies.size()>0) {
			
			
			for (Company company : paidCompanies) {
				
				try {
				
				
				if(company.getSubscription_to() != null) {
					int remainingDays = Days.daysBetween(date,company.getSubscription_to()).getDays();
					if (remainingDays == 2 || remainingDays == 7 || remainingDays == 10 || remainingDays == 30 || remainingDays == 0) {
						Set<User> user = company.getUser();
						if (remainingDays == 0) {
							
							int mailsent=companyService.ismailsent(company.getCompany_id(),date);
							if(mailsent==0)
							{
								
							for (User newuuser : user) {	
								mailService.paidPeriodEndReminder(newuuser, remainingDays);
								newuuser.setStatus(false);
								userService.userDeactivate(newuuser);
								
							}
							company.setStatus(STATUS_INACTIVE);
							companyService.companyDeactivate(company);
							
							}
							
						}
						else {
							for (User mailToUser : user) {
								if (mailToUser.getRole()!=null && mailToUser.getRole().getRole_id() == ROLE_CLIENT) {
									int mailsent=companyService.ismailsent(company.getCompany_id(),date);
									if(mailsent==0)
									{			
									
									mailService.paidPeriodEndReminder(mailToUser, remainingDays);
									companyService.updatemailsent(company.getCompany_id(),date);
								
									}
									break;
								}
							}
					    }
					}
				}
				
			   }
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}	
		
	   }
		
	}*/	
	
	
	@Transactional
	@Scheduled(fixedDelay = 43200000)
	public void closeAccountYearOn31stMarch() {
		
		 List<YearEnding> list = yearService.findAllYearEnding();
		 String stringDate = new LocalDate().toString();
		 String[] splits = stringDate.split("-");
		 String currentyear = new Year().toString();
		 synchronized(this)
		 {
	    if(splits[1].equalsIgnoreCase("04")&&splits[2].equalsIgnoreCase("01"))
	    {
		for(YearEnding year : list)
		{
			if(year.getAccountingYear()!=null)
			{
//			AccountingYear accYear = year.getAccountingYear();
//			String[] accYears = accYear.getYear_range().split("-");// 2017-2018 BECOMES TWO STRING AS 2017 AND 2018
//			String currentAccountYr = accYears[1];
//			if(currentyear.equalsIgnoreCase(currentAccountYr))
//			{
//			year.setYearEndingstatus((long)0);
//			try {
//				yearService.update(year);
//			} catch (MyWebException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			}
			}
			if(year.getAccountingYear()!=null)
			{
			AccountingYear accYear = year.getAccountingYear();
			String[] accYears = accYear.getYear_range().split("-");// 2017-2018 BECOMES TWO STRING AS 2017 AND 2018
			String currentAccountYr = accYears[0];
			if(currentyear.equalsIgnoreCase(currentAccountYr))
			{
			year.setYearEndingstatus((long)1);
			try {
				yearService.update(year);
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			}
			
		}
	    }
	     }
		
	}	
	
	/*@Transactional
	@Scheduled(fixedDelay = 43200000)
	public void closeAccountYearOnPerticularDateProvidedByVDAKPO() {
		
		 List<YearEnding> list = yearService.findAllYearEnding();
		 synchronized(this)
		 {
		for(YearEnding year : list)
		{
			if(year!=null && year.getToDate()!=null && year.getToDate().equals(new LocalDate()))
			{
			year.setYearEndingstatus(MyAbstractController.DEACTIVE_ACCOUNT_YEAR);
			try {
				yearService.update(year);
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	      }
	     
		
	}*/	
	
}