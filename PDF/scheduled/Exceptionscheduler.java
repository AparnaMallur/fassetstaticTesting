package fasset.PDF.scheduled;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.jfree.data.time.Year;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.Bank;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.entities.Ledger;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PaymentDetails;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.PurchaseEntryProductEntityClass;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.form.ExceptionReport1Form;
import com.fasset.form.ExceptionReport3Form;
import com.fasset.form.ExceptionReport4Form;
import com.fasset.form.ExceptionReport5Form;
import com.fasset.form.ExceptionReport6Form;
import com.fasset.form.LedgerForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.OpeningBalancesOfSubedgerForm;
import com.fasset.pdf.PDFBuilderForBalanceSheetPdfView;
import com.fasset.pdf.PDFBuilderForExceptionReport1PdfView;
import com.fasset.pdf.PDFBuilderForExceptionReport2PdfView;
import com.fasset.pdf.PDFBuilderForExceptionReport3PdfView;
import com.fasset.pdf.PDFBuilderForExceptionReport4PdfView;
import com.fasset.pdf.PDFBuilderForExceptionReport5PdfView;
import com.fasset.pdf.PDFBuilderForExceptionReport6PdfView;
import com.fasset.pdf.PDFBuilderForIncomeAndExpenditurePdfView;
import com.fasset.pdf.PDFBuilderForProfitAndLossPdfView;
import com.fasset.pdf.PDFBuilderForTrialBalancePdfView;
import com.fasset.service.interfaces.IAccountGroupService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IClientAllocationToKpoExecutiveService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IMailService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;

@EnableScheduling
@Component
public class Exceptionscheduler extends MyAbstractController
{
	
	@Autowired
	private IMailService mailService;

	@Autowired
	private ICompanyService companyService;
	
	@Autowired	
	private IClientSubscriptionMasterService SubscriptionService;	
	
	@Autowired
	private IPaymentService paymentService;

	@Autowired
	private IReceiptService receiptService;
	
	@Autowired
	private ISubLedgerDAO subLedgerDAO;
	
	@Autowired
	private IOpeningBalancesService openingBalancesService;

	@Autowired
	private ISalesEntryService salesService;

	@Autowired
	private IPurchaseEntryService purchaseService;
	
	@Autowired
	private ISubLedgerService subLedgerService;
	
	@Autowired
	private IClientAllocationToKpoExecutiveService service ;
	
	@Autowired
	private ILedgerService ledgerService;
	
	@Autowired
	private IAccountGroupService accountGroupService ;
	
	@Autowired
	private ISuplliersService suplliersService;
	
	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private IBankService bankService;
	

	@Transactional
	@Scheduled(cron = "0 0/1 * * * ?")
	public void sendDailyExceptionReportstoClient() 
	{
		 System.out.println("scheduling under PDF");
		 synchronized(this)
		{
		ByteArrayOutputStream baos1 = null;
		ByteArrayOutputStream baos2 = null;
		ByteArrayOutputStream baos3 = null;
		ByteArrayOutputStream baos4 = null;
		ByteArrayOutputStream baos5 = null;
		ByteArrayOutputStream baos6 = null;
		
		ByteArrayOutputStream baosBalanceSheet = null;
		ByteArrayOutputStream baosProfitAndLossReport = null;
		ByteArrayOutputStream baosIAndEReport = null;
		
		ByteArrayOutputStream trialBalanceReport=null;
		
		 System.out.println("scheduling ");
		List<ByteArrayOutputStream> list = new ArrayList<ByteArrayOutputStream>();
		 
		List<Company> paidCompanies = new ArrayList<>();
		paidCompanies = companyService.getCompanyByStatus(MyAbstractController.STATUS_SUBSCRIBED_USER);
		for (Company company : paidCompanies) {
			
			ClientSubscriptionMaster clientsub = SubscriptionService.getClientSubscriptionByCompanyId(company.getCompany_id());
			
			if(clientsub!=null && clientsub.getQuotation_id()!=null)
			{
				Quotation quot = clientsub.getQuotation_id();
				Set<QuotationDetails> quotationDetails =  quot.getQuotationDetails();
				
				for(QuotationDetails details : quotationDetails)
				{
					if(details.getService_id().getId().equals(new Long(5)) && details.getFrequency_id().getFrequency_id().equals(new Long(1)))
					{
                      Set<User> user = company.getUser();
						
						for (User mailToUser : user) {
							if (mailToUser.getRole()!=null && mailToUser.getRole().getRole_id().equals(new Long(MyAbstractController.ROLE_CLIENT)))
							{
								LocalDate fromDate = new LocalDate().minusDays(1);
								LocalDate toDate = new LocalDate().minusDays(1);
								LocalDate april1stLocaldate =null;
								String currentyear = new Year().toString();
								LocalDate date= new LocalDate();
								String april1stDate=null;
								april1stDate= currentyear+"-"+"04"+"-"+"01";
								
								if(date.isBefore(new LocalDate(april1stDate)) || date.equals(new LocalDate(april1stDate))) 
								{
									Integer year = Integer.parseInt(currentyear);
									year=year-1;
									String lastYear =year.toString();
									String april1stDateOflastyear = lastYear+"-"+"04"+"-"+"01";
									april1stLocaldate = new LocalDate(april1stDateOflastyear);
									
								}
								else if(date.isAfter(new LocalDate(april1stDate)))
								{
								 april1stLocaldate = new LocalDate(april1stDate);
								}
								baos1 = createExceptionReport1(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos2 = createExceptionReport2(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos3 = createExceptionReport3(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos4 = createExceptionReport4(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos5 = createExceptionReport5(company.getCompany_id(),fromDate,toDate,company);
								baos6 = createExceptionReport6(company.getCompany_id(),fromDate,toDate,company);
								list = createProfitAndLossAndIncomeAndExpenditureReport(company.getCompany_id(),fromDate,toDate,company);
								baosBalanceSheet = createBalanceSheetReport(company.getCompany_id(),fromDate,toDate,company);
								baosProfitAndLossReport = list.get(0);
								baosIAndEReport = list.get(1);
								trialBalanceReport = createTrialBalanceReport(company.getCompany_id(),fromDate,toDate,company);
								
								
								List<User> exeManagerList = service.findAllExcecutiveAndManagerOfCompany(company.getCompany_id());
								List<User> executiveList = new ArrayList<>();
								List<User> managerList = new ArrayList<>();
								for(User userexemanager : exeManagerList)
								{
									if(userexemanager.getRole().getRole_id().equals(MyAbstractController.ROLE_EXECUTIVE)) 
									{
										executiveList.add(userexemanager);
									}
									else
									{
										managerList.add(userexemanager);
									}
								}
								mailService.sendExceptionReportToClientAndExecutive(mailToUser, executiveList,managerList, baos1, baos2, baos3, baos4, baos5, baos6, baosBalanceSheet, baosProfitAndLossReport,baosIAndEReport,trialBalanceReport,company, new LocalDate());
							}
						}
					}
				}
				
			}
		}
		
	   }
	}
	
	@Transactional
	@Scheduled(cron = "${email.cronWeekly}")
	public void sendWeeklyExceptionReportstoClient() 
	{
	
		 synchronized(this)
		{
		ByteArrayOutputStream baos1 = null;
		ByteArrayOutputStream baos2 = null;
		ByteArrayOutputStream baos3 = null;
		ByteArrayOutputStream baos4 = null;
		ByteArrayOutputStream baos5 = null;
		ByteArrayOutputStream baos6 = null;
		ByteArrayOutputStream trialBalanceReport=null;
		ByteArrayOutputStream baosBalanceSheet = null;
		ByteArrayOutputStream baosProfitAndLossReport = null;
		ByteArrayOutputStream baosIAndEReport = null;
		List<ByteArrayOutputStream> list = new ArrayList<ByteArrayOutputStream>();
		
		List<Company> paidCompanies = new ArrayList<>();
		paidCompanies = companyService.getCompanyByStatus(MyAbstractController.STATUS_SUBSCRIBED_USER);
		for (Company company : paidCompanies) {
			
			ClientSubscriptionMaster clientsub = SubscriptionService.getClientSubscriptionByCompanyId(company.getCompany_id());
			
			if(clientsub!=null && clientsub.getQuotation_id()!=null)
			{
				Quotation quot = clientsub.getQuotation_id();
				Set<QuotationDetails> quotationDetails =  quot.getQuotationDetails();
				
				for(QuotationDetails details : quotationDetails)
				{
					if(details.getService_id().getId().equals(new Long(5)) && details.getFrequency_id().getFrequency_id().equals(new Long(2)))
					{
                      Set<User> user = company.getUser();
						
						for (User mailToUser : user) {
							if (mailToUser.getRole()!=null && mailToUser.getRole().getRole_id().equals(new Long(MyAbstractController.ROLE_CLIENT)))
							{
								LocalDate fromDate = new LocalDate().minusDays(7);
								LocalDate toDate = new LocalDate().minusDays(1);
								LocalDate april1stLocaldate =null;
								String currentyear = new Year().toString();
								LocalDate date= new LocalDate();
								String april1stDate=null;
								april1stDate= currentyear+"-"+"04"+"-"+"01";
								
								if(date.isBefore(new LocalDate(april1stDate)) || date.equals(new LocalDate(april1stDate))) 
								{
									Integer year = Integer.parseInt(currentyear);
									year=year-1;
									String lastYear =year.toString();
									String april1stDateOflastyear = lastYear+"-"+"04"+"-"+"01";
									april1stLocaldate = new LocalDate(april1stDateOflastyear);
									
								}
								else if(date.isAfter(new LocalDate(april1stDate)))
								{
								 april1stLocaldate = new LocalDate(april1stDate);
								}
								
								baos1 = createExceptionReport1(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos2 = createExceptionReport2(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos3 = createExceptionReport3(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos4 = createExceptionReport4(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos5 = createExceptionReport5(company.getCompany_id(),fromDate,toDate,company);
								baos6 = createExceptionReport6(company.getCompany_id(),fromDate,toDate,company);
								list = createProfitAndLossAndIncomeAndExpenditureReport(company.getCompany_id(),fromDate,toDate,company);
								baosBalanceSheet = createBalanceSheetReport(company.getCompany_id(),fromDate,toDate,company);
								baosProfitAndLossReport = list.get(0);
								baosIAndEReport = list.get(1);
								trialBalanceReport = createTrialBalanceReport(company.getCompany_id(),fromDate,toDate,company);
								List<User> exeManagerList = service.findAllExcecutiveAndManagerOfCompany(company.getCompany_id());
								List<User> executiveList = new ArrayList<>();
								List<User> managerList = new ArrayList<>();
								for(User userexemanager : exeManagerList)
								{
									if(userexemanager.getRole().getRole_id().equals(MyAbstractController.ROLE_EXECUTIVE)) 
									{
										executiveList.add(userexemanager);
									}
									else
									{
										managerList.add(userexemanager);
									}
								}
								mailService.sendExceptionReportToClientAndExecutive(mailToUser, executiveList,managerList, baos1, baos2, baos3, baos4, baos5, baos6, baosBalanceSheet, baosProfitAndLossReport,baosIAndEReport,trialBalanceReport,company, new LocalDate());
							}
						}
					}
				}
				
			}
		}
		
	   }
	}

	@Transactional
	@Scheduled(cron = "${email.cronMonthly}")
	public void sendMonthlyExceptionReportstoClient() {
		
		 synchronized(this)
		{
		ByteArrayOutputStream baos1 = null;
		ByteArrayOutputStream baos2 = null;
		ByteArrayOutputStream baos3 = null;
		ByteArrayOutputStream baos4 = null;
		ByteArrayOutputStream baos5 = null;
		ByteArrayOutputStream baos6 = null;
		ByteArrayOutputStream trialBalanceReport=null;
		ByteArrayOutputStream baosBalanceSheet = null;
		ByteArrayOutputStream baosProfitAndLossReport = null;
		ByteArrayOutputStream baosIAndEReport = null;
		 
		List<ByteArrayOutputStream> list = new ArrayList<ByteArrayOutputStream>();
		
		List<Company> paidCompanies = new ArrayList<>();
		paidCompanies = companyService.getCompanyByStatus(MyAbstractController.STATUS_SUBSCRIBED_USER);
		for (Company company : paidCompanies) {
			
			ClientSubscriptionMaster clientsub = SubscriptionService.getClientSubscriptionByCompanyId(company.getCompany_id());
			
			
			if(clientsub!=null && clientsub.getQuotation_id()!=null)
			{
				Quotation quot = clientsub.getQuotation_id();
				Set<QuotationDetails> quotationDetails =  quot.getQuotationDetails();
				
				for(QuotationDetails details : quotationDetails)
				{
					if(details.getService_id().getId().equals(new Long(5)) && details.getFrequency_id().getFrequency_id().equals(new Long(3)))
					{
                      Set<User> user = company.getUser();
						
						for (User mailToUser : user) {
							if (mailToUser.getRole()!=null && mailToUser.getRole().getRole_id().equals(new Long(MyAbstractController.ROLE_CLIENT)))
							{
								LocalDate fromDate = new LocalDate().minusDays(30);
								LocalDate toDate = new LocalDate().minusDays(1);
								LocalDate april1stLocaldate =null;
								String currentyear = new Year().toString();
								LocalDate date= new LocalDate();
								String april1stDate=null;
								april1stDate= currentyear+"-"+"04"+"-"+"01";
								
								if(date.isBefore(new LocalDate(april1stDate)) || date.equals(new LocalDate(april1stDate))) 
								{
									Integer year = Integer.parseInt(currentyear);
									year=year-1;
									String lastYear =year.toString();
									String april1stDateOflastyear = lastYear+"-"+"04"+"-"+"01";
									april1stLocaldate = new LocalDate(april1stDateOflastyear);
									
								}
								else if(date.isAfter(new LocalDate(april1stDate)))
								{
								 april1stLocaldate = new LocalDate(april1stDate);
								}
								
								baos1 = createExceptionReport1(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos2 = createExceptionReport2(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos3 = createExceptionReport3(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos4 = createExceptionReport4(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos5 = createExceptionReport5(company.getCompany_id(),fromDate,toDate,company);
								baos6 = createExceptionReport6(company.getCompany_id(),fromDate,toDate,company);
								list = createProfitAndLossAndIncomeAndExpenditureReport(company.getCompany_id(),fromDate,toDate,company);
								baosBalanceSheet = createBalanceSheetReport(company.getCompany_id(),fromDate,toDate,company);
								baosProfitAndLossReport = list.get(0);
								baosIAndEReport = list.get(1);
								trialBalanceReport = createTrialBalanceReport(company.getCompany_id(),fromDate,toDate,company);
								List<User> exeManagerList = service.findAllExcecutiveAndManagerOfCompany(company.getCompany_id());
								List<User> executiveList = new ArrayList<>();
								List<User> managerList = new ArrayList<>();
								for(User userexemanager : exeManagerList)
								{
									if(userexemanager.getRole().getRole_id().equals(MyAbstractController.ROLE_EXECUTIVE)) 
									{
										executiveList.add(userexemanager);
									}
									else
									{
										managerList.add(userexemanager);
									}
								}
								mailService.sendExceptionReportToClientAndExecutive(mailToUser, executiveList,managerList, baos1, baos2, baos3, baos4, baos5, baos6, baosBalanceSheet, baosProfitAndLossReport,baosIAndEReport,trialBalanceReport,company, new LocalDate());
							}
						}
					}
				}
				
			}
		}
		
	   }
	}
	@Transactional
	@Scheduled(cron = "${email.cronFortnightly}")
	public void sendFortnightlyExceptionReportstoClient() {
		
		 synchronized(this)
		{
		ByteArrayOutputStream baos1 = null;
		ByteArrayOutputStream baos2 = null;
		ByteArrayOutputStream baos3 = null;
		ByteArrayOutputStream baos4 = null;
		ByteArrayOutputStream baos5 = null;
		ByteArrayOutputStream baos6 = null;
		ByteArrayOutputStream trialBalanceReport=null;
		ByteArrayOutputStream baosBalanceSheet = null;
		ByteArrayOutputStream baosProfitAndLossReport = null;
		ByteArrayOutputStream baosIAndEReport = null;
		 
		List<ByteArrayOutputStream> list = new ArrayList<ByteArrayOutputStream>();
		
		List<Company> paidCompanies = new ArrayList<>();
		paidCompanies = companyService.getCompanyByStatus(MyAbstractController.STATUS_SUBSCRIBED_USER);
		for (Company company : paidCompanies) {
			
			ClientSubscriptionMaster clientsub = SubscriptionService.getClientSubscriptionByCompanyId(company.getCompany_id());
			
			if(clientsub!=null && clientsub.getQuotation_id()!=null)
			{
				Quotation quot = clientsub.getQuotation_id();
				Set<QuotationDetails> quotationDetails =  quot.getQuotationDetails();
				
				for(QuotationDetails details : quotationDetails)
				{
					if(details.getService_id().getId().equals(new Long(5)) && details.getFrequency_id().getFrequency_id().equals(new Long(4)))
					{
                      Set<User> user = company.getUser();
						
						for (User mailToUser : user) {
							if (mailToUser.getRole()!=null && mailToUser.getRole().getRole_id().equals(new Long(MyAbstractController.ROLE_CLIENT)))
							{
								LocalDate fromDate = new LocalDate().minusDays(15);
								LocalDate toDate = new LocalDate().minusDays(1);
								LocalDate april1stLocaldate =null;
								String currentyear = new Year().toString();
								LocalDate date= new LocalDate();
								String april1stDate=null;
								april1stDate= currentyear+"-"+"04"+"-"+"01";
								
								if(date.isBefore(new LocalDate(april1stDate)) || date.equals(new LocalDate(april1stDate))) 
								{
									Integer year = Integer.parseInt(currentyear);
									year=year-1;
									String lastYear =year.toString();
									String april1stDateOflastyear = lastYear+"-"+"04"+"-"+"01";
									april1stLocaldate = new LocalDate(april1stDateOflastyear);
									
								}
								else if(date.isAfter(new LocalDate(april1stDate)))
								{
								 april1stLocaldate = new LocalDate(april1stDate);
								}
								
								baos1 = createExceptionReport1(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos2 = createExceptionReport2(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos3 = createExceptionReport3(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos4 = createExceptionReport4(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos5 = createExceptionReport5(company.getCompany_id(),fromDate,toDate,company);
								baos6 = createExceptionReport6(company.getCompany_id(),fromDate,toDate,company);
								
								list = createProfitAndLossAndIncomeAndExpenditureReport(company.getCompany_id(),fromDate,toDate,company);
								baosBalanceSheet = createBalanceSheetReport(company.getCompany_id(),fromDate,toDate,company);
								baosProfitAndLossReport = list.get(0);
								baosIAndEReport = list.get(1);
								trialBalanceReport = createTrialBalanceReport(company.getCompany_id(),fromDate,toDate,company);
								List<User> exeManagerList = service.findAllExcecutiveAndManagerOfCompany(company.getCompany_id());
								List<User> executiveList = new ArrayList<>();
								List<User> managerList = new ArrayList<>();
								for(User userexemanager : exeManagerList)
								{
									if(userexemanager.getRole().getRole_id().equals(MyAbstractController.ROLE_EXECUTIVE)) 
									{
										executiveList.add(userexemanager);
									}
									else
									{
										managerList.add(userexemanager);
									}
								}
								mailService.sendExceptionReportToClientAndExecutive(mailToUser, executiveList,managerList, baos1, baos2, baos3, baos4, baos5, baos6, baosBalanceSheet, baosProfitAndLossReport,baosIAndEReport,trialBalanceReport,company, new LocalDate());
							}
						}
					}
				}
				
			}
		}
		
	   }
	}
	@Transactional
	@Scheduled(cron = "${email.cronQuarterly}")
	public void sendQuarterlyExceptionReportstoClient() {
		
		 synchronized(this)
		{
		ByteArrayOutputStream baos1 = null;
		ByteArrayOutputStream baos2 = null;
		ByteArrayOutputStream baos3 = null;
		ByteArrayOutputStream baos4 = null;
		ByteArrayOutputStream baos5 = null;
		ByteArrayOutputStream baos6 = null;
		ByteArrayOutputStream baosBalanceSheet = null;
		ByteArrayOutputStream baosProfitAndLossReport = null;
		ByteArrayOutputStream baosIAndEReport = null;
		ByteArrayOutputStream trialBalanceReport=null;
		List<ByteArrayOutputStream> list = new ArrayList<ByteArrayOutputStream>();
		List<Company> paidCompanies = new ArrayList<>();
		paidCompanies = companyService.getCompanyByStatus(MyAbstractController.STATUS_SUBSCRIBED_USER);
		for (Company company : paidCompanies) {
			
			ClientSubscriptionMaster clientsub = SubscriptionService.getClientSubscriptionByCompanyId(company.getCompany_id());
			
			if(clientsub!=null && clientsub.getQuotation_id()!=null)
			{
				Quotation quot = clientsub.getQuotation_id();
				Set<QuotationDetails> quotationDetails =  quot.getQuotationDetails();
				
				for(QuotationDetails details : quotationDetails)
				{
					if(details.getService_id().getId().equals(new Long(5)) && details.getFrequency_id().getFrequency_id().equals(new Long(5)))
					{
                      Set<User> user = company.getUser();
						
						for (User mailToUser : user) {
							if (mailToUser.getRole()!=null && mailToUser.getRole().getRole_id().equals(new Long(MyAbstractController.ROLE_CLIENT)))
							{
								LocalDate fromDate = new LocalDate().minusDays(95);
								LocalDate toDate = new LocalDate().minusDays(1);
								LocalDate april1stLocaldate =null;
								String currentyear = new Year().toString();
								LocalDate date= new LocalDate();
								String april1stDate=null;
								april1stDate= currentyear+"-"+"04"+"-"+"01";
								
								if(date.isBefore(new LocalDate(april1stDate)) || date.equals(new LocalDate(april1stDate))) 
								{
									Integer year = Integer.parseInt(currentyear);
									year=year-1;
									String lastYear =year.toString();
									String april1stDateOflastyear = lastYear+"-"+"04"+"-"+"01";
									april1stLocaldate = new LocalDate(april1stDateOflastyear);
									
								}
								else if(date.isAfter(new LocalDate(april1stDate)))
								{
								 april1stLocaldate = new LocalDate(april1stDate);
								}
								
								baos1 = createExceptionReport1(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos2 = createExceptionReport2(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos3 = createExceptionReport3(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos4 = createExceptionReport4(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos5 = createExceptionReport5(company.getCompany_id(),fromDate,toDate,company);
								baos6 = createExceptionReport6(company.getCompany_id(),fromDate,toDate,company);
								list = createProfitAndLossAndIncomeAndExpenditureReport(company.getCompany_id(),fromDate,toDate,company);
								baosBalanceSheet = createBalanceSheetReport(company.getCompany_id(),fromDate,toDate,company);
								baosProfitAndLossReport = list.get(0);
								baosIAndEReport = list.get(1);
								trialBalanceReport = createTrialBalanceReport(company.getCompany_id(),fromDate,toDate,company);
								List<User> exeManagerList = service.findAllExcecutiveAndManagerOfCompany(company.getCompany_id());
								List<User> executiveList = new ArrayList<>();
								List<User> managerList = new ArrayList<>();
								for(User userexemanager : exeManagerList)
								{
									if(userexemanager.getRole().getRole_id().equals(MyAbstractController.ROLE_EXECUTIVE)) 
									{
										executiveList.add(userexemanager);
									}
									else
									{
										managerList.add(userexemanager);
									}
								}
								mailService.sendExceptionReportToClientAndExecutive(mailToUser, executiveList,managerList, baos1, baos2, baos3, baos4, baos5, baos6, baosBalanceSheet, baosProfitAndLossReport,baosIAndEReport,trialBalanceReport,company, new LocalDate());
							}
						}
					}
				}
				
			}
		}
		
	   }
	}
	@Transactional
	@Scheduled(cron = "${email.cronHalfYearly}")
	public void sendHalfYearlyExceptionReportstoClient() {
		
		 synchronized(this)
		{
		ByteArrayOutputStream baos1 = null;
		ByteArrayOutputStream baos2 = null;
		ByteArrayOutputStream baos3 = null;
		ByteArrayOutputStream baos4 = null;
		ByteArrayOutputStream baos5 = null;
		ByteArrayOutputStream baos6 = null;
		ByteArrayOutputStream baosBalanceSheet = null;
		ByteArrayOutputStream baosProfitAndLossReport = null;
		ByteArrayOutputStream baosIAndEReport = null;
		ByteArrayOutputStream trialBalanceReport=null;
		List<ByteArrayOutputStream> list = new ArrayList<ByteArrayOutputStream>();
		List<Company> paidCompanies = new ArrayList<>();
		paidCompanies = companyService.getCompanyByStatus(MyAbstractController.STATUS_SUBSCRIBED_USER);
		for (Company company : paidCompanies) {
			
			ClientSubscriptionMaster clientsub = SubscriptionService.getClientSubscriptionByCompanyId(company.getCompany_id());
			
			if(clientsub!=null && clientsub.getQuotation_id()!=null)
			{
				Quotation quot = clientsub.getQuotation_id();
				Set<QuotationDetails> quotationDetails =  quot.getQuotationDetails();
				
				for(QuotationDetails details : quotationDetails)
				{
					if(details.getService_id().getId().equals(new Long(5)) && details.getFrequency_id().getFrequency_id().equals(new Long(6)))
					{
                      Set<User> user = company.getUser();
						
						for (User mailToUser : user) {
							if (mailToUser.getRole()!=null && mailToUser.getRole().getRole_id().equals(new Long(MyAbstractController.ROLE_CLIENT)))
							{
								LocalDate fromDate = new LocalDate().minusDays(190);
								LocalDate toDate = new LocalDate().minusDays(1);
								LocalDate april1stLocaldate =null;
								String currentyear = new Year().toString();
								LocalDate date= new LocalDate();
								String april1stDate=null;
								april1stDate= currentyear+"-"+"04"+"-"+"01";
								
								if(date.isBefore(new LocalDate(april1stDate)) || date.equals(new LocalDate(april1stDate))) 
								{
									Integer year = Integer.parseInt(currentyear);
									year=year-1;
									String lastYear =year.toString();
									String april1stDateOflastyear = lastYear+"-"+"04"+"-"+"01";
									april1stLocaldate = new LocalDate(april1stDateOflastyear);
									
								}
								else if(date.isAfter(new LocalDate(april1stDate)))
								{
								 april1stLocaldate = new LocalDate(april1stDate);
								}
								
								baos1 = createExceptionReport1(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos2 = createExceptionReport2(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos3 = createExceptionReport3(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos4 = createExceptionReport4(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos5 = createExceptionReport5(company.getCompany_id(),fromDate,toDate,company);
								baos6 = createExceptionReport6(company.getCompany_id(),fromDate,toDate,company);
								list = createProfitAndLossAndIncomeAndExpenditureReport(company.getCompany_id(),fromDate,toDate,company);
								baosBalanceSheet = createBalanceSheetReport(company.getCompany_id(),fromDate,toDate,company);
								baosProfitAndLossReport = list.get(0);
								baosIAndEReport = list.get(1);
								trialBalanceReport = createTrialBalanceReport(company.getCompany_id(),fromDate,toDate,company);
								List<User> exeManagerList = service.findAllExcecutiveAndManagerOfCompany(company.getCompany_id());
								List<User> executiveList = new ArrayList<>();
								List<User> managerList = new ArrayList<>();
								for(User userexemanager : exeManagerList)
								{
									if(userexemanager.getRole().getRole_id().equals(MyAbstractController.ROLE_EXECUTIVE)) 
									{
										executiveList.add(userexemanager);
									}
									else
									{
										managerList.add(userexemanager);
									}
								}
								mailService.sendExceptionReportToClientAndExecutive(mailToUser, executiveList,managerList, baos1, baos2, baos3, baos4, baos5, baos6, baosBalanceSheet, baosProfitAndLossReport,baosIAndEReport,trialBalanceReport,company, new LocalDate());
							}
						}
					}
				}
				
			}
		}
		
	   }
	}
	@Transactional
	@Scheduled(cron = "${email.cronYearly}")
	public void sendYearlyExceptionReportstoClient() {
		 synchronized(this)
		{
		ByteArrayOutputStream baos1 = null;
		ByteArrayOutputStream baos2 = null;
		ByteArrayOutputStream baos3 = null;
		ByteArrayOutputStream baos4 = null;
		ByteArrayOutputStream baos5 = null;
		ByteArrayOutputStream baos6 = null;
		ByteArrayOutputStream baosBalanceSheet = null;
		ByteArrayOutputStream baosProfitAndLossReport = null;
		ByteArrayOutputStream baosIAndEReport = null;
		ByteArrayOutputStream trialBalanceReport=null;
		List<ByteArrayOutputStream> list = new ArrayList<ByteArrayOutputStream>();
		List<Company> paidCompanies = new ArrayList<>();
		paidCompanies = companyService.getCompanyByStatus(MyAbstractController.STATUS_SUBSCRIBED_USER);
		for (Company company : paidCompanies) {
			
			ClientSubscriptionMaster clientsub = SubscriptionService.getClientSubscriptionByCompanyId(company.getCompany_id());
			
			if(clientsub!=null && clientsub.getQuotation_id()!=null)
			{
				Quotation quot = clientsub.getQuotation_id();
				Set<QuotationDetails> quotationDetails =  quot.getQuotationDetails();
				
				for(QuotationDetails details : quotationDetails)
				{
					if(details.getService_id().getId().equals(new Long(5)) && details.getFrequency_id().getFrequency_id().equals(new Long(7)))
					{
                      Set<User> user = company.getUser();
						
						for (User mailToUser : user) {
							if (mailToUser.getRole()!=null && mailToUser.getRole().getRole_id().equals(new Long(MyAbstractController.ROLE_CLIENT)))
							{
								LocalDate fromDate = new LocalDate().minusDays(365);
								LocalDate toDate = new LocalDate().minusDays(1);
								LocalDate april1stLocaldate =null;
								String currentyear = new Year().toString();
								LocalDate date= new LocalDate();
								String april1stDate=null;
								april1stDate= currentyear+"-"+"04"+"-"+"01";
								
								if(date.isBefore(new LocalDate(april1stDate)) || date.equals(new LocalDate(april1stDate))) 
								{
									Integer year = Integer.parseInt(currentyear);
									year=year-1;
									String lastYear =year.toString();
									String april1stDateOflastyear = lastYear+"-"+"04"+"-"+"01";
									april1stLocaldate = new LocalDate(april1stDateOflastyear);
									
								}
								else if(date.isAfter(new LocalDate(april1stDate)))
								{
								 april1stLocaldate = new LocalDate(april1stDate);
								}
								
								baos1 = createExceptionReport1(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos2 = createExceptionReport2(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos3 = createExceptionReport3(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos4 = createExceptionReport4(company.getCompany_id(),april1stLocaldate,toDate,company);
								baos5 = createExceptionReport5(company.getCompany_id(),fromDate,toDate,company);
								baos6 = createExceptionReport6(company.getCompany_id(),fromDate,toDate,company);
								list = createProfitAndLossAndIncomeAndExpenditureReport(company.getCompany_id(),fromDate,toDate,company);
								baosBalanceSheet = createBalanceSheetReport(company.getCompany_id(),fromDate,toDate,company);
								baosProfitAndLossReport = list.get(0);
								baosIAndEReport = list.get(1);
								trialBalanceReport = createTrialBalanceReport(company.getCompany_id(),fromDate,toDate,company);
								List<User> exeManagerList = service.findAllExcecutiveAndManagerOfCompany(company.getCompany_id());
								List<User> executiveList = new ArrayList<>();
								List<User> managerList = new ArrayList<>();
								for(User userexemanager : exeManagerList)
								{
									if(userexemanager.getRole().getRole_id().equals(MyAbstractController.ROLE_EXECUTIVE)) 
									{
										executiveList.add(userexemanager);
									}
									else
									{
										managerList.add(userexemanager);
									}
								}
								mailService.sendExceptionReportToClientAndExecutive(mailToUser, executiveList,managerList, baos1, baos2, baos3, baos4, baos5, baos6, baosBalanceSheet, baosProfitAndLossReport,baosIAndEReport,trialBalanceReport,company, new LocalDate());
							}
						}
					}
				}
				
			}
		}
		
	   }
		
	}
	/** Cash payment and receipt of more Rs.10000/- and above (For Client) */
	public ByteArrayOutputStream createExceptionReport1(Long companyId,LocalDate fromdate,LocalDate todate,Company company)
	{
		ByteArrayOutputStream baos1 = null;
		
		List<ExceptionReport1Form> exceptionReport1FormsList = new ArrayList<ExceptionReport1Form>();
		
		List<Receipt> receiptList = receiptService.CashReceiptOfMoreThanRS10000AndAbove(fromdate,
				todate, companyId);
		List<Payment> paymentList = paymentService.CashPaymentOfMoreThanRS10000AndAbove(fromdate,
				todate, companyId);
		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport1Form exception1Form = new ExceptionReport1Form();
				if (payment.getDate() != null) {
					exception1Form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					exception1Form.setSuppliers(payment.getSupplier().getCompany_name());
				}
				
				if(payment.getSubLedger()!=null) {
					exception1Form.setSuppliers(payment.getSubLedger().getSubledger_name());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Payment");

				exception1Form.setPaymentType("Cash");
				Double Tds = (double)0;
				Double amount = (double)0;
				if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(true)))
				{
					if(payment.getTds_amount()!=null)
					{
						Tds=Tds+payment.getTds_amount();
					}
					if(payment.getAmount()!=null)
					{
						amount=amount+payment.getAmount();
					}
					
					
				}
				if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(false)))
				{
					if(payment.getAmount()!=null)
					{
						amount=amount+payment.getAmount();
					}
					
				}
				exception1Form.setTotalAmount(amount+Tds);
				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport1FormsList.add(exception1Form);

			}
		}

			for (Receipt receipt : receiptList) {
				if (receipt.getLocal_time() != null) {
					ExceptionReport1Form exception1Form = new ExceptionReport1Form();
					if (receipt.getDate() != null) {
						exception1Form.setDate(receipt.getDate());
					}
					if (receipt.getCustomer() != null) {
						exception1Form.setCustomer(receipt.getCustomer().getFirm_name());
					}
					if(receipt.getSubLedger()!=null) {
						exception1Form.setCustomer(receipt.getSubLedger().getSubledger_name());
					}
					if (receipt.getVoucher_no() != null) {
						exception1Form.setVoucher_Number(receipt.getVoucher_no());
					}
					exception1Form.setVoucher_Type("Receipt");
					exception1Form.setPaymentType("Cash");
					Double Tds = (double)0;
					Double amount = (double)0;
					if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(true)))
					{
						if(receipt.getTds_amount()!=null)
						{
							Tds=Tds+receipt.getTds_amount();
						}
						if(receipt.getAmount()!=null)
						{
							amount=amount+receipt.getAmount();
						}
						
						
					}
					if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(false)))
					{
						if(receipt.getAmount()!=null)
						{
							amount=amount+receipt.getAmount();
						}
						
					}
					exception1Form.setTotalAmount(amount+Tds);
					exception1Form.setLocal_time(receipt.getLocal_time());
					exceptionReport1FormsList.add(exception1Form);

				}

			}

			Collections.sort(exceptionReport1FormsList, new Comparator<ExceptionReport1Form>() {

				@Override
				public int compare(ExceptionReport1Form o1, ExceptionReport1Form o2) {
					LocalDate Date1 = o1.getDate();
					LocalDate Date2 = o2.getDate();
					int sComp = Date2.compareTo(Date1);

					if (sComp != 0) {
						return sComp;
					}

					LocalTime local_time1 = o1.getLocal_time();
					LocalTime local_time2 = o2.getLocal_time();
					return local_time2.compareTo(local_time1);
				}
			});
			PDFBuilderForExceptionReport1PdfView pdfview = new PDFBuilderForExceptionReport1PdfView();
			try {
				baos1= pdfview.GeneratePDf(exceptionReport1FormsList, fromdate, todate, company);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return baos1;
	}
	/**
	 * Cash payment and receipt Unadjusted (For Client)
	 */
	public ByteArrayOutputStream createExceptionReport2(Long companyId,LocalDate fromdate,LocalDate todate,Company company)
	{
		ByteArrayOutputStream baos2 = null;
		List<ExceptionReport3Form> exceptionReport2FormsList = new ArrayList<ExceptionReport3Form>();

		List<Receipt> receiptList = receiptService.customerReceiptHavingUnadjustedUnadjusted(fromdate,
				todate, companyId);
		List<Payment> paymentList = paymentService.supplierPaymentHavingUnadjusted(fromdate, todate,
				companyId);

		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport3Form exception1Form = new ExceptionReport3Form();
				if (payment.getDate() != null) {
					exception1Form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					exception1Form.setSuppliers(payment.getSupplier().getCompany_name());
				}
				

				if(payment.getSubLedger()!=null) {
					exception1Form.setSuppliers(payment.getSubLedger().getSubledger_name());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Payment");

				Double amount = (double) 0;
				Integer days = 0;

				if (payment.getAmount() != null) {
					amount = amount + payment.getAmount();
				}
				exception1Form.setTotalAmount(amount);
				if (payment.getAgingDays() != null) {
					days = days + payment.getAgingDays();

				}
				exception1Form.setNumberOfDays(days);
				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport2FormsList.add(exception1Form);

			}
		}
		for (Receipt receipt : receiptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport3Form exception1Form = new ExceptionReport3Form();
				if (receipt.getDate() != null) {
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getCustomer() != null) {
					exception1Form.setCustomer(receipt.getCustomer().getFirm_name());
				}
				
				if(receipt.getSubLedger()!=null) {
					exception1Form.setCustomer(receipt.getSubLedger().getSubledger_name());
				}
				
				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Receipt");

				Double amount = (double) 0;
				Integer days = 0;
				if (receipt.getAmount() != null) {
					amount = amount + receipt.getAmount();
				}
				exception1Form.setTotalAmount(amount);

				if (receipt.getAgingDays() != null) {
					days = days + receipt.getAgingDays();
				}
				exception1Form.setNumberOfDays(days);
				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport2FormsList.add(exception1Form);

			}

		}

		Collections.sort(exceptionReport2FormsList, new Comparator<ExceptionReport3Form>() {

			@Override
			public int compare(ExceptionReport3Form o1, ExceptionReport3Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});
		
		PDFBuilderForExceptionReport2PdfView pdfview = new PDFBuilderForExceptionReport2PdfView();
		try {
			baos2= pdfview.GeneratePDf(exceptionReport2FormsList, fromdate, todate, company);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos2;
	}
	/** supplierAndCustomerHavingGSTApplicbleAsYesAndTaxZero */
	public ByteArrayOutputStream createExceptionReport3(Long companyId,LocalDate fromdate,LocalDate todate,Company company)
	{
		ByteArrayOutputStream baos3 = null;
		List<ExceptionReport4Form> exceptionReport4FormsList = new ArrayList<ExceptionReport4Form>();
		
		List<SalesEntry> salesList = salesService.customerSalesHavingGST0(fromdate, todate,
				companyId);
		List<PurchaseEntry> purchaseEntryList = purchaseService.supplirPurchaseHavingGST0(fromdate,
				todate, companyId);
		List<Receipt> receiptList = receiptService.customerReceiptHavingGST0(fromdate, todate,
				companyId);
		List<Payment> paymentList = paymentService.supplirPaymentHavingGST0(fromdate, todate,
				companyId);
		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (payment.getDate() != null) {
					exception1Form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					exception1Form.setSuppliers(payment.getSupplier().getCompany_name());
				}
				if(payment.getSubLedger()!=null) {
					exception1Form.setSuppliers(payment.getSubLedger().getSubledger_name());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Payment");
		
				Double Tds = (double)0;
				Double amount = (double)0;
				if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(true)))
				{
					if(payment.getTds_amount()!=null)
					{
						Tds=Tds+payment.getTds_amount();
					}
					if(payment.getAmount()!=null)
					{
						amount=amount+payment.getAmount();
					}
					
					
				}
				if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(false)))
				{
					if(payment.getAmount()!=null)
					{
						amount=amount+payment.getAmount();
					}
					
				}
				exception1Form.setTotalAmount(amount+Tds);

				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);

			}
		}

		for (Receipt receipt : receiptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (receipt.getDate() != null) {
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getCustomer() != null) {
					exception1Form.setCustomer(receipt.getCustomer().getFirm_name());
				}	
				if(receipt.getSubLedger()!=null) {
					exception1Form.setCustomer(receipt.getSubLedger().getSubledger_name());
				}
				
				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Receipt");
				
				Double Tds = (double)0;
				Double amount = (double)0;
				if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(true)))
				{
					if(receipt.getTds_amount()!=null)
					{
						Tds=Tds+receipt.getTds_amount();
					}
					if(receipt.getAmount()!=null)
					{
						amount=amount+receipt.getAmount();
					}
					
					
				}
				if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(false)))
				{
					if(receipt.getAmount()!=null)
					{
						amount=amount+receipt.getAmount();
					}
					
				}
				
				exception1Form.setTotalAmount(amount+Tds);

				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);

			}

		}

		for (SalesEntry sales : salesList) {
			if (sales.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (sales.getCreated_date() != null) {
					exception1Form.setDate(sales.getCreated_date());
				}
				if (sales.getCustomer() != null) {
					exception1Form.setCustomer(sales.getCustomer().getFirm_name());
				}
				if (sales.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(sales.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Sales");
				if (sales.getVoucher_no() != null) {
					exception1Form.setInvoiceNo(sales.getVoucher_no());
				}

				Double amount = (double) 0;

				if (sales.getRound_off() != null) {
					amount = amount + sales.getRound_off();
				}
				exception1Form.setTotalAmount(amount);

				exception1Form.setLocal_time(sales.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);
			}

		}
		for (PurchaseEntry purchase : purchaseEntryList) {
			if (purchase.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (purchase.getCreated_date() != null) {
					exception1Form.setDate(purchase.getCreated_date());
				}
				if (purchase.getSupplier().getCompany_name() != null)

				{
					exception1Form.setCustomer(purchase.getSupplier().getCompany_name());
				}

				if (purchase.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(purchase.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Purchase");
				// supplier_bill_no}
				if (purchase.getSupplier_bill_no() != null) {
					exception1Form.setInvoiceNo(purchase.getSupplier_bill_no());
				}

				Double amount = (double) 0;

				if (purchase.getRound_off() != null) {
					amount = amount + purchase.getRound_off();
				}
				exception1Form.setTotalAmount(amount);
				exception1Form.setLocal_time(purchase.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);
			}

		}
		Collections.sort(exceptionReport4FormsList, new Comparator<ExceptionReport4Form>() {

			@Override
			public int compare(ExceptionReport4Form o1, ExceptionReport4Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});
		
		PDFBuilderForExceptionReport3PdfView pdfview = new PDFBuilderForExceptionReport3PdfView();
		try {
			baos3= pdfview.GeneratePDf(exceptionReport4FormsList, fromdate, todate, company);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos3;
	}
	/** supplierAndCustomerHaviPrevngGSTApplicbleAsNOAndTaxExceedingZero */
	public ByteArrayOutputStream createExceptionReport4(Long companyId,LocalDate fromdate,LocalDate todate,Company company)
	{
		ByteArrayOutputStream baos4 = null;
		List<SalesEntry> salesList = new ArrayList<>();
		List<ExceptionReport5Form> exceptionReport5List = new ArrayList<ExceptionReport5Form>();
		for (SalesEntry entry : salesService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(fromdate,
				todate, companyId)) {
			entry.setProductinfoList(salesService.findAllSalesEntryProductEntityList(entry.getSales_id()));
			salesList.add(entry);
		}
		List<PurchaseEntry> purchaseEntryList = new ArrayList<>();
		
		for (PurchaseEntry entry : purchaseService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(fromdate,
				todate, companyId)) {
			entry.setProductinfoList(purchaseService.findAllPurchaseEntryProductEntityList(entry.getPurchase_id()));
			purchaseEntryList.add(entry);
		}
		List<Receipt> receciptList = new ArrayList<>();
		for(Receipt receipt:receiptService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(fromdate, todate,
				companyId)) {
			receipt.setProductinfoList(receiptService.findAllReceiptProductEntityList(receipt.getReceipt_id()));
			receciptList.add(receipt);
		}
		List<Payment> paymentList = paymentService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(fromdate, todate,companyId);
		
		for(SalesEntry sales :salesList) 
		{
			if(sales.getLocal_time()!=null)
			{
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double)0;
				Double freight= (double)0;
			    Double others= (double)0;
				
				if(sales.getCreated_date()!=null)
				{
					form.setDate(sales.getCreated_date());
				}
				if(sales.getCustomer()!=null)
				{
					form.setCusSupplierName(sales.getCustomer().getFirm_name());
					
				}
				if(sales.getVoucher_no()!=null)
				{
					form.setVoucherNumber(sales.getVoucher_no());
				}
				if(sales.getVoucher_no()!=null)
				{
					form.setInvocieNumber(sales.getVoucher_no());
				}
				for(SalesEntryProductEntityClass info : sales.getProductinfoList())
				{
					labourcharges=labourcharges+info.getLabour_charges();
					freight=freight+info.getFreight();
					others=others+info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				if(sales.getCgst()!=null)
				{
					form.setCgst(sales.getCgst());
					
				}
				form.setVoucherType("Sales");
				if(sales.getSgst()!=null)
				{
					form.setSgst(sales.getSgst());
					
				}
				if(sales.getIgst()!=null)
				{
					form.setIgst(sales.getIgst());
					
				}
				if(sales.getRound_off()!=null)
				{
					form.setInvoiceValue(sales.getRound_off());	
				}
				form.setLocaltime(sales.getLocal_time());
				exceptionReport5List.add(form);
			}
		}
		
		for(PurchaseEntry purchase :purchaseEntryList) 
		{
			if(purchase.getLocal_time()!=null)
			{
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double)0;
				Double freight= (double)0;
			    Double others= (double)0;
				
				if(purchase.getSupplier_bill_date()!=null)
				{
					form.setDate(purchase.getSupplier_bill_date());
				}
				if(purchase.getSupplier()!=null)
				{
					form.setCusSupplierName(purchase.getSupplier().getCompany_name());
					
				}
				if(purchase.getVoucher_no()!=null)
				{
					form.setVoucherNumber(purchase.getVoucher_no());
				}
				if(purchase.getSupplier_bill_no()!=null)
				{
					form.setInvocieNumber(purchase.getSupplier_bill_no());
				}
				for(PurchaseEntryProductEntityClass info : purchase.getProductinfoList())
				{
					labourcharges=labourcharges+info.getLabour_charges();
					freight=freight+info.getFreight();
					others=others+info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				if(purchase.getCgst()!=null)
				{
					form.setCgst(purchase.getCgst());
					
				}
				form.setVoucherType("Purchase");
				if(purchase.getSgst()!=null)
				{
					form.setSgst(purchase.getSgst());
					
				}
				if(purchase.getIgst()!=null)
				{
					form.setIgst(purchase.getIgst());
					
				}
				if(purchase.getRound_off()!=null)
				{
					form.setInvoiceValue(purchase.getRound_off());	
				}
				form.setLocaltime(purchase.getLocal_time());
				exceptionReport5List.add(form);
			}
		}
		
		for(Receipt receipt :receciptList) 
		{
			if(receipt.getLocal_time()!=null)
			{
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double)0;
				Double freight= (double)0;
			    Double others= (double)0;
				
				if(receipt.getDate()!=null)
				{
					form.setDate(receipt.getDate());
				}
				if(receipt.getCustomer()!=null)
				{
					form.setCusSupplierName(receipt.getCustomer().getFirm_name());
					
				}
				if(receipt.getVoucher_no()!=null)
				{
					form.setVoucherNumber(receipt.getVoucher_no());
				}
				if(receipt.getVoucher_no()!=null)
				{
					form.setInvocieNumber(receipt.getVoucher_no());
				}
				for(Receipt_product_details info : receipt.getProductinfoList())
				{
					labourcharges=labourcharges+info.getLabour_charges();
					freight=freight+info.getFreight();
					others=others+info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				form.setVoucherType("Receipt");
				if(receipt.getCgst()!=null)
				{
					form.setCgst(receipt.getCgst());
					
				}
				if(receipt.getSgst()!=null)
				{
					form.setSgst(receipt.getSgst());
					
				}
				if(receipt.getIgst()!=null)
				{
					form.setIgst(receipt.getIgst());
					
				}
				Double Tds = (double)0;
				Double amount = (double)0;
				if(receipt.getTds_amount()!=null)
				{
					Tds=Tds+receipt.getTds_amount();
				}
				if(receipt.getAmount()!=null)
				{
					amount=amount+receipt.getAmount();
				}
				form.setInvoiceValue(Tds+amount);	
		
				form.setLocaltime(receipt.getLocal_time());
				exceptionReport5List.add(form);
			}
		}
		
		for(Payment payment :paymentList) 
		{
			if(payment.getLocal_time()!=null)
			{
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double)0;
				Double freight= (double)0;
			    Double others= (double)0;
				
				if(payment.getDate()!=null)
				{
					form.setDate(payment.getDate());
				}
				if(payment.getSupplier()!=null)
				{
					form.setCusSupplierName(payment.getSupplier().getCompany_name());
					
				}
				if(payment.getVoucher_no()!=null)
				{
					form.setVoucherNumber(payment.getVoucher_no());
				}
				if(payment.getSupplier_bill_no()!=null)
				{
					form.setInvocieNumber(payment.getVoucher_no());
				}
				for(PaymentDetails info : payment.getPaymentDetails())
				{
					labourcharges=labourcharges+info.getLabour_charges();
					freight=freight+info.getFrieght();
					others=others+info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				form.setVoucherType("Payment");
				if(payment.getCGST_head()!=null)
				{
					form.setCgst(payment.getCGST_head());
					
				}
				if(payment.getSGST_head()!=null)
				{
					form.setSgst(payment.getSGST_head());
					
				}
				if(payment.getIGST_head()!=null)
				{
					form.setIgst(payment.getIGST_head());
					
				}
				Double Tds = (double)0;
				Double amount = (double)0;
				if(payment.getTds_amount()!=null)
				{
					Tds=Tds+payment.getTds_amount();
				}
				if(payment.getAmount()!=null)
				{
					amount=amount+payment.getAmount();
				}
				form.setInvoiceValue(Tds+amount);	
				form.setLocaltime(payment.getLocal_time());
				exceptionReport5List.add(form);
			}
		}
		
		Collections.sort(exceptionReport5List, new Comparator<ExceptionReport5Form>() {
	        public int compare(ExceptionReport5Form o1, ExceptionReport5Form o2) {

	        	LocalDate Date1 = o1.getDate();
	        	LocalDate Date2= o2.getDate();
	            int sComp = Date2.compareTo(Date1);

	            if (sComp != 0) {
	               return sComp;
	            } 

	            LocalTime local_time1 = o1.getLocaltime();
	            LocalTime local_time2= o2.getLocaltime();
	            return local_time2.compareTo(local_time1);
	    }});
		
		PDFBuilderForExceptionReport4PdfView pdfview = new PDFBuilderForExceptionReport4PdfView();
		try {
			baos4= pdfview.GeneratePDf(exceptionReport5List, fromdate, todate, company);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos4;
	}
	/**
	 * Supplier with sub-ledgers linked as Legal/ Professional/ Repairs &
	 * Maintenance and the TDS Applicable Flag is "N"; but the total of debit
	 * transactions or total of credit transactions exceed Rs 30000/-
	 */
	public ByteArrayOutputStream createExceptionReport5(Long companyId,LocalDate fromdate,LocalDate todate,Company company)
	{
		ByteArrayOutputStream baos5 = null;
		List<ExceptionReport6Form> exceptionReport6FormsList = new ArrayList<ExceptionReport6Form>();
		List<SalesEntry> salesList = salesService.customerSalesWithSubledgerTransactionsRs300000AndAbove(
				fromdate,todate, companyId);
		List<PurchaseEntry> purchaseEntryList = purchaseService
				.supplierPurchaseWithSubledgerTransactionsRs300000AndAbove(fromdate, todate,
						companyId);
		List<Receipt> receiptList=receiptService.customerReceiptsWithSubledgerTransactionsRs300000AndAbove(fromdate,todate,
				companyId);
		
		List<Payment> paymentList= paymentService.supplierPaymentWithSubledgerTransactionsRs300000AndAbove(fromdate, todate, companyId);
		for (Receipt receipt : receiptList )
		{
			if(receipt.getLocal_time() !=null)
			{
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if(receipt.getDate() !=null)
				{
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}
				if(receipt.getSubLedger()!=null)
				{
					exception1Form.setCustomer("Income");
					exception1Form.setSubledgerName(receipt.getSubLedger().getSubledger_name());
				}
				exception1Form.setVoucher_Type("Receipt");
				Double Tds = (double)0;
				Double amount = (double)0;
				if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(true)))
				{
					if(receipt.getTds_amount()!=null)
					{
						Tds=Tds+receipt.getTds_amount();
					}
					if(receipt.getAmount()!=null)
					{
						amount=amount+receipt.getAmount();
					}
					
					
				}
				if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment().equals(new Boolean(false)))
				{
					if(receipt.getAmount()!=null)
					{
						amount=amount+receipt.getAmount();
					}
					
				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);
				
				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);
				
			}
		}
		for (Payment payment:paymentList )
		 {
			 if (payment.getLocal_time() != null) {
					ExceptionReport6Form exception1Form = new ExceptionReport6Form();
					if (payment.getCreated_date() != null) {
						exception1Form.setDate(payment.getCreated_date());
					}

					if (payment.getVoucher_no() != null) {
						exception1Form.setVoucher_Number(payment.getVoucher_no());
					}
					
					
					
						if(payment.getSubLedger()!=null)
						{
							exception1Form.setSuppliers("Expense");
							exception1Form.setSubledgerName(payment.getSubLedger().getSubledger_name());
						}
						
					exception1Form.setVoucher_Type("Payment");
				
					
					Double Tds = (double)0;
					Double amount = (double)0;
					if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(true)))
					{
						if(payment.getTds_amount()!=null)
						{
							Tds=Tds+payment.getTds_amount();
						}
						if(payment.getAmount()!=null)
						{
							amount=amount+payment.getAmount();
						}
						
						
					}
					if(payment.getAdvance_payment()!=null && payment.getAdvance_payment().equals(new Boolean(false)))
					{
						if(payment.getAmount()!=null)
						{
							amount=amount+payment.getAmount();
						}
						
					}
					exception1Form.setTransactionValue(amount);
					exception1Form.setTDS(0.0d);
					
					exception1Form.setLocal_time(payment.getLocal_time());
					exceptionReport6FormsList.add(exception1Form);
				}

		 }
		

		
		for (SalesEntry sales : salesList) {
			if (sales.getLocal_time() != null) {
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if (sales.getCreated_date() != null) {
					exception1Form.setDate(sales.getCreated_date());
				}
				if (sales.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(sales.getVoucher_no());
				}
				if (sales.getCustomer() != null) {
					exception1Form.setCustomer(sales.getCustomer().getFirm_name());
				}
				
				if(sales.getSubledger()!=null)
				{
					exception1Form.setSubledgerName(sales.getSubledger().getSubledger_name());
				}
				
				exception1Form.setVoucher_Type("Sales");
				

				Double amount = (double) 0;

				if (sales.getTransaction_value() != null) {
					amount = amount + sales.getTransaction_value();
				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);

				exception1Form.setLocal_time(sales.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);
			}

		}
		for (PurchaseEntry purchase : purchaseEntryList) {
			if (purchase.getLocal_time() != null) {
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if (purchase.getCreated_date() != null) {
					exception1Form.setDate(purchase.getCreated_date());
				}

				if (purchase.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(purchase.getVoucher_no());
				}
				
				if (purchase.getSupplier() != null)

				{
					exception1Form.setCustomer(purchase.getSupplier().getCompany_name());
				}
				
					if(purchase.getSubledger()!=null)
					{
						exception1Form.setSubledgerName(purchase.getSubledger().getSubledger_name());
					}
					
				exception1Form.setVoucher_Type("Purchase");
		
				

				Double amount = (double) 0;

				if (purchase.getTransaction_value() != null) {
					amount = amount + purchase.getTransaction_value();
				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);
				
				exception1Form.setLocal_time(purchase.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);
			}

		}
		 
		Collections.sort(exceptionReport6FormsList, new Comparator<ExceptionReport6Form>() {

		

			@Override
			public int compare(ExceptionReport6Form o1, ExceptionReport6Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});
		
		
		PDFBuilderForExceptionReport5PdfView pdfview = new PDFBuilderForExceptionReport5PdfView();
		try {
			baos5= pdfview.GeneratePDf(exceptionReport6FormsList, fromdate, todate, company);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos5;
	}
	/** cash in hand account should technically have debit or zero balance */
	public ByteArrayOutputStream createExceptionReport6(Long companyId,LocalDate fromdate,LocalDate todate,Company company)
	{
		ByteArrayOutputStream baos6 = null;
		List<OpeningBalances> subledgerOPBalanceList = new ArrayList<OpeningBalances>();
		List<OpeningBalancesForm> subledgerOpenBalanceList = new ArrayList<OpeningBalancesForm>();
		SubLedger subledgercash = subLedgerDAO.findOne("Cash In Hand", companyId);

		for (Object bal[] : openingBalancesService.findAllOPbalancesforSubledger(fromdate, todate,
				companyId, false)) {
			if (bal[0] != null && bal[1] != null && bal[2] != null) {
				SubLedger sub = null;
				try {
					sub = subLedgerService.getById((long) bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (sub != null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setSubledgerName(sub.getSubledger_name());
					try {
						form.setSubledger(sub);
					} catch (Exception e) {
						e.printStackTrace();
					}
					form.setSub_id((long) bal[0]);
					form.setCredit_balance((double) bal[2]);
					form.setDebit_balance((double) bal[1]);
					subledgerOpenBalanceList.add(form);
				}
			}
		}

		subledgerOPBalanceList = openingBalancesService.findAllOPbalancesforSubledger(fromdate,
				todate, subledgercash.getSubledger_Id(),companyId);
		
		PDFBuilderForExceptionReport6PdfView pdfview = new PDFBuilderForExceptionReport6PdfView();
		try {
			baos6= pdfview.GeneratePDf(subledgerOPBalanceList,subledgerOpenBalanceList, subledgercash.getSubledger_Id(),fromdate, todate, company);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos6;
	}
	
	
	public ByteArrayOutputStream createBalanceSheetReport(Long companyId,LocalDate fromdate,LocalDate todate,Company company)
	{
		ByteArrayOutputStream baosBalanceSheet = null;
		List<AccountGroup> accList = new ArrayList<AccountGroup>();
		List<Ledger> legderList = new ArrayList<Ledger>();
		List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesOfsubledger = new ArrayList<OpeningBalancesOfSubedgerForm>();
		List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesbeforestartDate = new ArrayList<OpeningBalancesOfSubedgerForm>();
		List<OpeningBalancesForm>subledgerListbeforestartDate = new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>bankOpeningBalanceList =new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>supplierOpeningBalanceList=new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>customerOpeningBalanceList= new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>supplierOpeningBalanceBeforeStartDate =new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>customerOpeningBalanceBeforeStartDate = new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>bankOpeningBalanceBeforeStartDate =new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>  subledgerList = new ArrayList<OpeningBalancesForm>();
		
		//change for Fasset Report  reported by Assem sir
		String currentyear = new Year().toString();
		LocalDate date= new LocalDate();
		String april1stDate=null;
		april1stDate= currentyear+"-"+"04"+"-"+"01";
		
		if(date.isBefore(new LocalDate(april1stDate)) || date.equals(new LocalDate(april1stDate))) 
		{
			Integer year = Integer.parseInt(currentyear);
			year=year-1;
			String lastYear =year.toString();
			String april1stDateOflastyear = lastYear+"-"+"04"+"-"+"01";
			fromdate = new LocalDate(april1stDateOflastyear);
			
		}
		else if(date.isAfter(new LocalDate(april1stDate)))
		{
			fromdate = new LocalDate(april1stDate);
		}
		//=========================================================================================================
		for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(fromdate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Bank bank = null;
				try {
					bank = bankService.getById((long)bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
                form.setBankName(bank.getBank_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				bankOpeningBalanceBeforeStartDate.add(form);
				}
			}
		}
		
		Collections.sort(bankOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String bankName1 = form1.getBankName().trim();
                String bankName2 = form2.getBankName().trim();
                return bankName1.compareTo(bankName2);
            }
        });
		
	
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplierBeforeFromDate(fromdate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Suppliers sup = null;
				try {
					sup = suplliersService.getById((long)bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(sup!=null && sup.getSupplier_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
			    form.setSupplierName(sup.getCompany_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				supplierOpeningBalanceBeforeStartDate.add(form);
				}
			}
		}
		
		Collections.sort(supplierOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String supplierName1 = form1.getSupplierName().trim();
                String supplierName2 = form2.getSupplierName().trim();
                return supplierName1.compareTo(supplierName2);
            }
        });
		
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomerBeforeFromDate(fromdate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Customer cus = null;
				try {
					cus = customerService.getById((long)bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(cus!=null && cus.getCustomer_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) 
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
				form.setCustomerName(cus.getFirm_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				customerOpeningBalanceBeforeStartDate.add(form);
				}
			}
		}
		Collections.sort(customerOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String customerName1 = form1.getCustomerName().trim();
                String customerName2 = form2.getCustomerName().trim();
                return customerName1.compareTo(customerName2);
            }
        });
		
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(fromdate, todate, companyId,false))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{	
				
				SubLedger sub = null;
				try {
					sub = subLedgerService.getById((long)bal[0]);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
				{
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setSubledgerName(sub.getSubledger_name());
					try {
						form.setSubledger(sub);
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				subledgerListbeforestartDate.add(form);
				} 
				
				
			}
		}
		
		accList = accountGroupService.findAll();
		legderList = ledgerService.findLedgersOnlyOfComapany(companyId);
		
		for(AccountGroup group : accList)
		{
			double Totaldebit_balance = 0;
			double Totalcredit_balance = 0;
			
			OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
			List<LedgerForm>  ledgerFormList = new ArrayList<>();
			List<String> AccountSubGroupNameList = new ArrayList<>();
			sublederform.setAccountGroupName(group.getGroup_name());
			sublederform.setGroup(group);
			for(AccountSubGroup subgroup : group.getAccount_sub_group())
			{
				AccountSubGroupNameList.add(subgroup.getSubgroup_name());
				
				
				for(Ledger ledger : legderList)
				{
					
					LedgerForm ledgerform = new LedgerForm();
				    Double ledgerdebit_balance =0.0;
				    Double ledgercredit_balance = 0.0;
					List<OpeningBalancesForm>  openingBalanceList = new ArrayList<>();
					for(OpeningBalancesForm form : subledgerListbeforestartDate)
					{
						if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
						(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
						&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
						{
							
								
							
						
							OpeningBalancesForm formbalance = new OpeningBalancesForm();
							formbalance.setSubledgerName(form.getSubledgerName());
							formbalance.setCredit_balance(form.getCredit_balance());
							formbalance.setDebit_balance(form.getDebit_balance());
							formbalance.setSubledger(form.getSubledger());
							openingBalanceList.add(formbalance);
							ledgerdebit_balance =ledgerdebit_balance+form.getDebit_balance();
							ledgercredit_balance =ledgercredit_balance+form.getCredit_balance();
							Totaldebit_balance = Totaldebit_balance+form.getDebit_balance();
							Totalcredit_balance = Totalcredit_balance+form.getCredit_balance();
							
							
						}
					}
					if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
					{
					ledgerform.setSubgroupName(subgroup.getSubgroup_name());
					ledgerform.setLedgerName(ledger.getLedger_name());
					ledgerform.setLedgercredit_balance(ledgercredit_balance);
					ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
					ledgerform.setSubledgerList(openingBalanceList);
					ledgerFormList.add(ledgerform);
					}
					
				}
				
			}
			
			sublederform.setTotalcredit_balance(Totalcredit_balance);
			sublederform.setTotaldebit_balance(Totaldebit_balance);
			sublederform.setLedgerformlist(ledgerFormList);
			sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
			ListForOpeningbalancesbeforestartDate.add(sublederform);
			
			
		}
		
		//-------------Calculation for opening balance between start date and end date-----------------
		
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(fromdate, todate, companyId,true))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				
				SubLedger sub = null;
				try {
					sub = subLedgerService.getById((long)bal[0]);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))// for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
				{
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setSubledgerName(sub.getSubledger_name());
					try {
						form.setSubledger(sub);
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				subledgerList.add(form);
				} 
			}
		}
		
	   /*accList = accountGroupService.findAll();
	   legderList = ledgerService.findLedgersOnlyOfComapany(company_id);*/
	   
		for(AccountGroup group : accList)
		{
			double Totaldebit_balance = 0;
			double Totalcredit_balance = 0;
			
			OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
			List<LedgerForm>  ledgerFormList = new ArrayList<>();
			List<String> AccountSubGroupNameList = new ArrayList<>();
			sublederform.setAccountGroupName(group.getGroup_name());
			sublederform.setGroup(group);
			for(AccountSubGroup subgroup : group.getAccount_sub_group())
			{
				AccountSubGroupNameList.add(subgroup.getSubgroup_name());
				
				for(Ledger ledger : legderList)
				{
					
					LedgerForm ledgerform = new LedgerForm();
				    Double ledgerdebit_balance =0.0;
				    Double ledgercredit_balance = 0.0;
					List<OpeningBalancesForm>  openingBalanceList = new ArrayList<>();
					for(OpeningBalancesForm form : subledgerList)
					{
						if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
						(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
						&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
						{
							
							OpeningBalancesForm formbalance = new OpeningBalancesForm();
							formbalance.setSubledgerName(form.getSubledgerName());
							formbalance.setCredit_balance(form.getCredit_balance());
							formbalance.setDebit_balance(form.getDebit_balance());
							formbalance.setSubledger(form.getSubledger());
							openingBalanceList.add(formbalance);
							ledgerdebit_balance =ledgerdebit_balance+form.getDebit_balance();
							ledgercredit_balance =ledgercredit_balance+form.getCredit_balance();
							Totaldebit_balance = Totaldebit_balance+form.getDebit_balance();
							Totalcredit_balance = Totalcredit_balance+form.getCredit_balance();
							
							
						}
					}
					if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
					{
					ledgerform.setSubgroupName(subgroup.getSubgroup_name());
					ledgerform.setLedgerName(ledger.getLedger_name());
					ledgerform.setLedgercredit_balance(ledgercredit_balance);
					ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
					ledgerform.setSubledgerList(openingBalanceList);
					ledgerFormList.add(ledgerform);
					}
					
				}
				
			}
			sublederform.setTotalcredit_balance(Totalcredit_balance);
			sublederform.setTotaldebit_balance(Totaldebit_balance);
			sublederform.setLedgerformlist(ledgerFormList);
			sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
			ListForOpeningbalancesOfsubledger.add(sublederform);
		}
	
		for(Object bal[] :openingBalancesService.findAllOPbalancesforBank(fromdate, todate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Bank bank = null;
				try {
					bank = bankService.getById((long)bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
                form.setBankName(bank.getBank_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				bankOpeningBalanceList.add(form);
				}
			}
		}
		
		Collections.sort(bankOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String bankName1 = form1.getBankName().trim();
                String bankName2 = form2.getBankName().trim();
                return bankName1.compareTo(bankName2);
            }
        });
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplier(fromdate, todate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Suppliers sup = null;
				try {
					sup = suplliersService.getById((long)bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(sup!=null && sup.getSupplier_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
			    form.setSupplierName(sup.getCompany_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				supplierOpeningBalanceList.add(form);
				}
			}
		}
		
		Collections.sort(supplierOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String supplierName1 = form1.getSupplierName().trim();
                String supplierName2 = form2.getSupplierName().trim();
                return supplierName1.compareTo(supplierName2);
            }
        });
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomer(fromdate, todate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Customer cus = null;
				try {
					cus = customerService.getById((long)bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(cus!=null && cus.getCustomer_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) 
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
				form.setCustomerName(cus.getFirm_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				customerOpeningBalanceList.add(form);
				}
			}
		}
		
		Collections.sort(customerOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String customerName1 = form1.getCustomerName().trim();
                String customerName2 = form2.getCustomerName().trim();
                return customerName1.compareTo(customerName2);
            }
        });
	
		
		PDFBuilderForBalanceSheetPdfView pdfview = new PDFBuilderForBalanceSheetPdfView();
		try {
			baosBalanceSheet= pdfview.GeneratePDf(ListForOpeningbalancesOfsubledger,ListForOpeningbalancesbeforestartDate,customerOpeningBalanceList,customerOpeningBalanceBeforeStartDate,supplierOpeningBalanceList,supplierOpeningBalanceBeforeStartDate,bankOpeningBalanceList,bankOpeningBalanceBeforeStartDate,fromdate,todate,company);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baosBalanceSheet;
	}
	
	public ByteArrayOutputStream createTrialBalanceReport(Long companyId,LocalDate fromdate,LocalDate todate,Company company)
	{
		ByteArrayOutputStream baostrialBalance = null;
	    List<AccountGroup> accList = new ArrayList<AccountGroup>();
		List<Ledger> legderList = new ArrayList<Ledger>();
		List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesOfsubledger = new ArrayList<OpeningBalancesOfSubedgerForm>();
		List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesbeforestartDate = new ArrayList<OpeningBalancesOfSubedgerForm>();
		List<OpeningBalancesForm>  subledgerList = new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>  subledgerListbeforestartDate = new ArrayList<OpeningBalancesForm>();
	    List<OpeningBalancesForm>   bankOpeningBalanceList =new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>   supplierOpeningBalanceList  =new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>   customerOpeningBalanceList  = new ArrayList<OpeningBalancesForm>();
	    List<OpeningBalancesForm>   supplierOpeningBalanceBeforeStartDate =new ArrayList<OpeningBalancesForm>();
		List<OpeningBalancesForm>   customerOpeningBalanceBeforeStartDate = new ArrayList<OpeningBalancesForm>();
        List<OpeningBalancesForm>   bankOpeningBalanceBeforeStartDate =new ArrayList<OpeningBalancesForm>();
        
      //change for Fasset Report  reported by Assem sir
      		String currentyear = new Year().toString();
      		LocalDate date= new LocalDate();
      		String april1stDate=null;
      		april1stDate= currentyear+"-"+"04"+"-"+"01";
      		
      		if(date.isBefore(new LocalDate(april1stDate)) || date.equals(new LocalDate(april1stDate))) 
      		{
      			Integer year = Integer.parseInt(currentyear);
      			year=year-1;
      			String lastYear =year.toString();
      			String april1stDateOflastyear = lastYear+"-"+"04"+"-"+"01";
      			fromdate = new LocalDate(april1stDateOflastyear);
      			
      		}
      		else if(date.isAfter(new LocalDate(april1stDate)))
      		{
      			fromdate = new LocalDate(april1stDate);
      		}
      		//=========================================================================================================
        for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(fromdate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Bank bank = null;
				try {
					bank = bankService.getById((long)bal[0]);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
                form.setBankName(bank.getBank_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				bankOpeningBalanceBeforeStartDate.add(form);
				}
			}
		}
		
		Collections.sort(bankOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String bankName1 = form1.getBankName().trim();
                String bankName2 = form2.getBankName().trim();
                return bankName1.compareTo(bankName2);
            }
        });
		

		for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplierBeforeFromDate(fromdate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Suppliers sup = null;
				try {
					sup = suplliersService.getById((long)bal[0]);
				//	System.out.println("findAllOPbalancesforSupplierBeforeFromDate  name of Trial Balance   is:"+sup);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(sup!=null && sup.getSupplier_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
			    form.setSupplierName(sup.getCompany_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				supplierOpeningBalanceBeforeStartDate.add(form);
				}
			}
		}
		
		Collections.sort(supplierOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String supplierName1 = form1.getSupplierName().trim();
                String supplierName2 = form2.getSupplierName().trim();
                return supplierName1.compareTo(supplierName2);
            }
        });
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomerBeforeFromDate(fromdate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Customer cus = null;
				try {
					cus = customerService.getById((long)bal[0]);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(cus!=null && cus.getCustomer_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) 
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
				form.setCustomerName(cus.getFirm_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				customerOpeningBalanceBeforeStartDate.add(form);
				}
			}
		}
		Collections.sort(customerOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String customerName1 = form1.getCustomerName().trim();
                String customerName2 = form2.getCustomerName().trim();
                return customerName1.compareTo(customerName2);
            }
        });
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(fromdate, todate, companyId,false))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{	
				
				SubLedger sub = null;
				try {
					sub = subLedgerService.getById((long)bal[0]);
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
				{
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setSubledgerName(sub.getSubledger_name());
					try {
						form.setSubledger(sub);
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				subledgerListbeforestartDate.add(form);
				} 
				
				
			}
		}
		
		accList = accountGroupService.findAll();
		legderList = ledgerService.findLedgersOnlyOfComapany(companyId);
		
		for(AccountGroup group : accList)
		{
			double Totaldebit_balance = 0;
			double Totalcredit_balance = 0;
			
			OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
			List<LedgerForm>  ledgerFormList = new ArrayList<>();
			List<String> AccountSubGroupNameList = new ArrayList<>();
			sublederform.setAccountGroupName(group.getGroup_name());
			sublederform.setGroup(group);
			for(AccountSubGroup subgroup : group.getAccount_sub_group())
			{
				AccountSubGroupNameList.add(subgroup.getSubgroup_name());
				
				
				for(Ledger ledger : legderList)
				{
					
					LedgerForm ledgerform = new LedgerForm();
				    Double ledgerdebit_balance =0.0;
				    Double ledgercredit_balance = 0.0;
					List<OpeningBalancesForm>  openingBalanceList = new ArrayList<>();
					for(OpeningBalancesForm form : subledgerListbeforestartDate)
					{
						if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
						(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
						&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
						{
							OpeningBalancesForm formbalance = new OpeningBalancesForm();
							formbalance.setSubledgerName(form.getSubledgerName());
							formbalance.setCredit_balance(form.getCredit_balance());
							formbalance.setDebit_balance(form.getDebit_balance());
							formbalance.setSubledger(form.getSubledger());
							openingBalanceList.add(formbalance);
							ledgerdebit_balance =ledgerdebit_balance+form.getDebit_balance();
							ledgercredit_balance =ledgercredit_balance+form.getCredit_balance();
							Totaldebit_balance = Totaldebit_balance+form.getDebit_balance();
							Totalcredit_balance = Totalcredit_balance+form.getCredit_balance();
							
							
						}
					}
					if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
					{
					ledgerform.setSubgroupName(subgroup.getSubgroup_name());
					ledgerform.setLedgerName(ledger.getLedger_name());
					ledgerform.setLedgercredit_balance(ledgercredit_balance);
					ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
					ledgerform.setSubledgerList(openingBalanceList);
					ledgerFormList.add(ledgerform);
					}
					
				}
				
			}
			
			sublederform.setTotalcredit_balance(Totalcredit_balance);
			sublederform.setTotaldebit_balance(Totaldebit_balance);
			sublederform.setLedgerformlist(ledgerFormList);
			sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
			ListForOpeningbalancesbeforestartDate.add(sublederform);
			
			
		}
		
		
		
		for(AccountGroup group : accList)
		{
			double Totaldebit_balance = 0;
			double Totalcredit_balance = 0;
			
			OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
			List<LedgerForm>  ledgerFormList = new ArrayList<>();
			List<String> AccountSubGroupNameList = new ArrayList<>();
			sublederform.setAccountGroupName(group.getGroup_name());
			sublederform.setGroup(group);
			for(AccountSubGroup subgroup : group.getAccount_sub_group())
			{
				AccountSubGroupNameList.add(subgroup.getSubgroup_name());
				
				for(Ledger ledger : legderList)
				{
					
					LedgerForm ledgerform = new LedgerForm();
				    Double ledgerdebit_balance =0.0;
				    Double ledgercredit_balance = 0.0;
					List<OpeningBalancesForm>  openingBalanceList = new ArrayList<>();
					for(OpeningBalancesForm form : subledgerList)
					{
						if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
						(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
						&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
						{
							
							OpeningBalancesForm formbalance = new OpeningBalancesForm();
							formbalance.setSubledgerName(form.getSubledgerName());
							formbalance.setCredit_balance(form.getCredit_balance());
							formbalance.setDebit_balance(form.getDebit_balance());
							formbalance.setSubledger(form.getSubledger());
							openingBalanceList.add(formbalance);
							ledgerdebit_balance =ledgerdebit_balance+form.getDebit_balance();
							ledgercredit_balance =ledgercredit_balance+form.getCredit_balance();
							Totaldebit_balance = Totaldebit_balance+form.getDebit_balance();
							Totalcredit_balance = Totalcredit_balance+form.getCredit_balance();
							
							
						}
					}
					if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
					{
					ledgerform.setSubgroupName(subgroup.getSubgroup_name());
					ledgerform.setLedgerName(ledger.getLedger_name());
					ledgerform.setLedgercredit_balance(ledgercredit_balance);
					ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
					ledgerform.setSubledgerList(openingBalanceList);
					ledgerFormList.add(ledgerform);
					}
					
				}
				
			}
			sublederform.setTotalcredit_balance(Totalcredit_balance);
			sublederform.setTotaldebit_balance(Totaldebit_balance);
			sublederform.setLedgerformlist(ledgerFormList);
			sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
			ListForOpeningbalancesOfsubledger.add(sublederform);
		}
		

		for(Object bal[] :openingBalancesService.findAllOPbalancesforBank(fromdate, todate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Bank bank = null;
				try {
					bank = bankService.getById((long)bal[0]);
					
			
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
                form.setBankName(bank.getBank_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				bankOpeningBalanceList.add(form);
				}
			}
		}
		
		Collections.sort(bankOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String bankName1 = form1.getBankName().trim();
                String bankName2 = form2.getBankName().trim();
                return bankName1.compareTo(bankName2);
            }
        });
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforSupplier(fromdate, todate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Suppliers sup = null;
				try {
					sup = suplliersService.getById((long)bal[0]);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(sup!=null && sup.getSupplier_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
			    form.setSupplierName(sup.getCompany_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				supplierOpeningBalanceList.add(form);
				}
			}
		}
		
		Collections.sort(supplierOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String supplierName1 = form1.getSupplierName().trim();
                String supplierName2 = form2.getSupplierName().trim();
                return supplierName1.compareTo(supplierName2);
            }
        });
		
		for(Object bal[] :openingBalancesService.findAllOPbalancesforCustomer(fromdate, todate, companyId))
		{
			if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
			{
				Customer cus = null;
				try {
					cus = customerService.getById((long)bal[0]);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(cus!=null && cus.getCustomer_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) 
				{
				OpeningBalancesForm form = new OpeningBalancesForm();
				form.setCustomerName(cus.getFirm_name());
				form.setCredit_balance((double)bal[2]);
				form.setDebit_balance((double)bal[1]);
				customerOpeningBalanceList.add(form);
				}
			}
		}
		
		Collections.sort(customerOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
                String customerName1 = form1.getCustomerName().trim();
                String customerName2 = form2.getCustomerName().trim();
                return customerName1.compareTo(customerName2);
            }
        });
		PDFBuilderForTrialBalancePdfView pdfview = new PDFBuilderForTrialBalancePdfView();
		try {
			baostrialBalance= pdfview.GeneratePDf(ListForOpeningbalancesbeforestartDate,ListForOpeningbalancesOfsubledger,bankOpeningBalanceList,supplierOpeningBalanceList,customerOpeningBalanceList,bankOpeningBalanceBeforeStartDate,supplierOpeningBalanceBeforeStartDate,customerOpeningBalanceBeforeStartDate,fromdate,todate,company);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return baostrialBalance;
	}
	
	public List<ByteArrayOutputStream> createProfitAndLossAndIncomeAndExpenditureReport(Long companyId,LocalDate fromdate,LocalDate todate,Company company)
	{
		 ByteArrayOutputStream baosProfitAndLossReport = null;
		 ByteArrayOutputStream baosIAndEReport = null;
		 
		 List<ByteArrayOutputStream> list = new ArrayList<ByteArrayOutputStream>();
		 List<AccountGroup> accList = new ArrayList<AccountGroup>();
		 List<Ledger> legderList = new ArrayList<Ledger>();
		 List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesOfsubledger = new ArrayList<OpeningBalancesOfSubedgerForm>();
		 List<OpeningBalancesForm>  subledgerList = new ArrayList<OpeningBalancesForm>();
		 List<OpeningBalancesForm>  subledgerListbeforestartDate = new ArrayList<OpeningBalancesForm>();
		 List<OpeningBalancesOfSubedgerForm> ListForOpeningbalancesbeforestartDate = new ArrayList<OpeningBalancesOfSubedgerForm>();
		
		 accList = accountGroupService.findAll();
		 legderList = ledgerService.findLedgersOnlyOfComapany(companyId);
		 
		//change for Fasset Report  reported by Assem sir
			String currentyear = new Year().toString();
			LocalDate date= new LocalDate();
			String april1stDate=null;
			april1stDate= currentyear+"-"+"04"+"-"+"01";
			
			if(date.isBefore(new LocalDate(april1stDate)) || date.equals(new LocalDate(april1stDate))) 
			{
				Integer year = Integer.parseInt(currentyear);
				year=year-1;
				String lastYear =year.toString();
				String april1stDateOflastyear = lastYear+"-"+"04"+"-"+"01";
				fromdate = new LocalDate(april1stDateOflastyear);
				
			}
			else if(date.isAfter(new LocalDate(april1stDate)))
			{
				fromdate = new LocalDate(april1stDate);
			}
			//=========================================================================================================
		 
		 for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(fromdate, todate, companyId,false))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{	
					
					SubLedger sub = null;
					try {
						sub = subLedgerService.getById((long)bal[0]);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
					{
						OpeningBalancesForm form = new OpeningBalancesForm();
						form.setSubledgerName(sub.getSubledger_name());
						try {
							form.setSubledger(sub);
						} catch (Exception e) {
							e.printStackTrace();
						}
					
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					subledgerListbeforestartDate.add(form);
					} 
					
					
				}
			}
		 
		 for(AccountGroup group : accList)
			{
				double Totaldebit_balance = 0;
				double Totalcredit_balance = 0;
				
				OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
				List<LedgerForm>  ledgerFormList = new ArrayList<>();
				List<String> AccountSubGroupNameList = new ArrayList<>();
				sublederform.setAccountGroupName(group.getGroup_name());
				sublederform.setGroup(group);
				for(AccountSubGroup subgroup : group.getAccount_sub_group())
				{
					AccountSubGroupNameList.add(subgroup.getSubgroup_name());
					
					
					for(Ledger ledger : legderList)
					{
						
						LedgerForm ledgerform = new LedgerForm();
					    Double ledgerdebit_balance =0.0;
					    Double ledgercredit_balance = 0.0;
						List<OpeningBalancesForm>  openingBalanceList = new ArrayList<>();
						for(OpeningBalancesForm form : subledgerListbeforestartDate)
						{
							if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
							(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
							&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
							{
								
									
								
							
								OpeningBalancesForm formbalance = new OpeningBalancesForm();
								formbalance.setSubledgerName(form.getSubledgerName());
								formbalance.setCredit_balance(form.getCredit_balance());
								formbalance.setDebit_balance(form.getDebit_balance());
								formbalance.setSubledger(form.getSubledger());
								openingBalanceList.add(formbalance);
								ledgerdebit_balance =ledgerdebit_balance+form.getDebit_balance();
								ledgercredit_balance =ledgercredit_balance+form.getCredit_balance();
								Totaldebit_balance = Totaldebit_balance+form.getDebit_balance();
								Totalcredit_balance = Totalcredit_balance+form.getCredit_balance();
								
								
							}
						}
						if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
						{
						ledgerform.setSubgroupName(subgroup.getSubgroup_name());
						ledgerform.setLedgerName(ledger.getLedger_name());
						ledgerform.setLedgercredit_balance(ledgercredit_balance);
						ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
						ledgerform.setSubledgerList(openingBalanceList);
						ledgerFormList.add(ledgerform);
						}
						
					}
					
				}
				
				sublederform.setTotalcredit_balance(Totalcredit_balance);
				sublederform.setTotaldebit_balance(Totaldebit_balance);
				sublederform.setLedgerformlist(ledgerFormList);
				sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
				ListForOpeningbalancesbeforestartDate.add(sublederform);
				
				
			}
		 
		 for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(fromdate, todate, companyId,true))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					
					SubLedger sub = null;
					try {
						sub = subLedgerService.getById((long)bal[0]);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					if(sub!=null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))// for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
					{
						OpeningBalancesForm form = new OpeningBalancesForm();
						form.setSubledgerName(sub.getSubledger_name());
						try {
							form.setSubledger(sub);
						} catch (Exception e) {
							e.printStackTrace();
						}
					
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					subledgerList.add(form);
					} 
				}
			}
		 
		 
		 for(AccountGroup group : accList)
			{
				double Totaldebit_balance = 0;
				double Totalcredit_balance = 0;
				
				OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
				List<LedgerForm>  ledgerFormList = new ArrayList<>();
				List<String> AccountSubGroupNameList = new ArrayList<>();
				sublederform.setAccountGroupName(group.getGroup_name());
				sublederform.setGroup(group);
				for(AccountSubGroup subgroup : group.getAccount_sub_group())
				{
					AccountSubGroupNameList.add(subgroup.getSubgroup_name());
					
					for(Ledger ledger : legderList)
					{
						
						LedgerForm ledgerform = new LedgerForm();
					    double ledgerdebit_balance =0;
					    double ledgercredit_balance = 0;
						List<OpeningBalancesForm>  openingBalanceList = new ArrayList<>();
						for(OpeningBalancesForm form : subledgerList)
						{
							if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
							(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
							&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
							{
								
								OpeningBalancesForm formbalance = new OpeningBalancesForm();
								formbalance.setSubledgerName(form.getSubledgerName());
								formbalance.setCredit_balance(form.getCredit_balance());
								formbalance.setDebit_balance(form.getDebit_balance());
								formbalance.setSubledger(form.getSubledger());
								openingBalanceList.add(formbalance);
								ledgerdebit_balance =ledgerdebit_balance+form.getDebit_balance();
								ledgercredit_balance =ledgercredit_balance+form.getCredit_balance();
								Totaldebit_balance = Totaldebit_balance+form.getDebit_balance();
								Totalcredit_balance = Totalcredit_balance+form.getCredit_balance();
								
								
							}
						}
						if(!(ledgerdebit_balance==0 && ledgercredit_balance==0))
						{
						ledgerform.setSubgroupName(subgroup.getSubgroup_name());
						ledgerform.setLedgerName(ledger.getLedger_name());
						ledgerform.setLedgercredit_balance(ledgercredit_balance);
						ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
						ledgerform.setSubledgerList(openingBalanceList);
						ledgerFormList.add(ledgerform);
						}
						
					}
					
				}
				sublederform.setTotalcredit_balance(Totalcredit_balance);
				sublederform.setTotaldebit_balance(Totaldebit_balance);
				sublederform.setLedgerformlist(ledgerFormList);
				sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
				ListForOpeningbalancesOfsubledger.add(sublederform);
			}
		
		 PDFBuilderForProfitAndLossPdfView pdfview = new PDFBuilderForProfitAndLossPdfView();
		try {
			baosProfitAndLossReport= pdfview.GeneratePDf(ListForOpeningbalancesOfsubledger,ListForOpeningbalancesbeforestartDate,fromdate, todate, company);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PDFBuilderForIncomeAndExpenditurePdfView pdfview2 = new PDFBuilderForIncomeAndExpenditurePdfView();
			try {
				baosIAndEReport= pdfview2.GeneratePDf(ListForOpeningbalancesOfsubledger,ListForOpeningbalancesbeforestartDate,fromdate, todate, company);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		list.add(baosProfitAndLossReport);
		list.add(baosIAndEReport);
		return list;
	}
}
