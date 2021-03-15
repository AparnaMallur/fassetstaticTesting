package com.fasset.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ccavenue.security.Md5;
import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.Company;
import com.fasset.entities.JavaMD5Hash;
import com.fasset.entities.Quotation;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;
import com.fasset.service.interfaces.IClientValidationChecklistService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IMailService;
import com.fasset.utils.MailContent;

@Service
public class MailServiceImpl extends MyAbstractController implements IMailService {

	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private JavaMailSenderImpl mailSenderImpl;
	
	@Autowired
	private  ICompanyService  companyService;
	
	@Autowired
	private IClientValidationChecklistService clientvalidationchecklistService;
	
	@Value("${app.url}")
	private String appUrl;

	@Value("${route.pdf}")
	private String ruta;
	
	@Override
	public void sendMail(String recipient, String subject, String message) {
		System.out.println("Sending mail");
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipient);
		email.setSubject(subject);
		email.setText(message);		
		mailSender.send(email);
	}

	@Override
	public void sendSignupMail(User user) throws MessagingException {
		String body;
		//
		List<ClientValidationChecklist> list=clientvalidationchecklistService.findAll();
		
		if(user.getRole().getRole_id() == MyAbstractController.ROLE_TRIAL_USER){
			body = MailContent.getRegistrationMailContentTrial(user);	
		}
		else{
			body = MailContent.getRegistrationMailContent(user, list);			
		}
				
		MimeMessage message = mailSender.createMimeMessage();
		message.setSubject("Fasset Registration");
		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom(user.getEmail());
		helper.setTo(user.getEmail());
		helper.setText(body, true);
		mailSenderImpl.send(message);
	}
	

	@Override
	public void sendQuotationMail(Quotation quotation,ByteArrayOutputStream baos) throws MessagingException {
		System.out.println("demo1");
		String body = MailContent.getquotationdetails(quotation);
		MimeMessage message = mailSender.createMimeMessage();
		message.setSubject("Fasset Quotation Request");
		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true, "UTF-8");
		System.out.println("quotation.getEmail()"+quotation.getEmail());
		helper.setFrom(quotation.getEmail());
		System.out.println("quotation.getEmail()"+quotation.getEmail());
		helper.setTo(quotation.getEmail());
		helper.setText(body, true);
		System.out.println("demo2");
		File file = new File(getRuta()+"QuotationDetails"+quotation.getQuotation_id()+".pdf");
		/*try {
			System.out.println("demo3");
			//FileOutputStream fos = new FileOutputStream (file); 
		   // baos.writeTo(fos);
		  //  fos.close();
		} catch(IOException ioe) {
			System.out.println("demo4");
			System.out.println("error message"+ioe.getMessage());
		    ioe.printStackTrace();
		}	*/
		System.out.println("demo5");
		//helper.addAttachment("QuotationDetails.pdf", file);
		try{
			System.out.println("the mail id is "+mailSenderImpl.getUsername());
	    mailSenderImpl.send(message);
	    System.out.println("the mail sent "+mailSenderImpl.getUsername());
	    }catch(Exception ex){
	    	System.out.println("the mailexception is "+ ex.getMessage());
	    }
		
	}

	@Override
	public void sendChangedPassMail(User user) throws MessagingException {	
		System.out.println("the appurl is"+appUrl);
		String body = MailContent.getForgotPasswordMailContent(user, appUrl);		
		MimeMessage message = mailSender.createMimeMessage();
		message.setSubject("Reset Password");
		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom(user.getEmail());
		helper.setTo(user.getEmail());
		//helper.setTo("aparna.sublime2018@gmail.com");
		Long roleId = user.getRole().getRole_id();
		if(roleId == MyAbstractController.ROLE_EMPLOYEE || roleId == MyAbstractController.ROLE_MANAGER || roleId == MyAbstractController.ROLE_EXECUTIVE){
			helper.setCc(user.getCompany().getEmail_id());
		}
		helper.setText(body, true);
		mailSenderImpl.send(message);
	}

	@Override
	public void trialPeriodEndReminder(User user, Integer remainingDays) {
				String body = "";
				if (remainingDays > 0) {
					body = "Dear " +  user.getFirst_name() + "," + "<br>Fasset trial period reminder:<br>" + "Your Trial Period ends in next "
							+ remainingDays + " days.Please subscribe to fasset to continue use services.<br><br>" + "<br><div><span style='color: rgb(0, 32, 96);font-size:16px'>With best regards,<br />Team VDAKPO | <a href='https://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a><br/>Email: fassetdeven@gmail.com<br/>Phone:020-65292835,25465860</span></div><br><br>";
				} else {
					body = "Dear " +  user.getFirst_name() + "," + "<br>Fasset trial period reminder:<br>"
							+ "Your trial period is ended for fasset. Please subscribe to fasset to continue use services.<br><br>" 
							+ "<br><div><span style='color: rgb(0, 32, 96);font-size:16px'>With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>\";";
				}
				MimeMessage message = mailSender.createMimeMessage();
				try {
					message.setSubject("Fasset Trial Period");
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MimeMessageHelper helper = null;
				try {
					helper = new MimeMessageHelper(message, true, "UTF-8");
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					helper.setFrom(user.getEmail());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					helper.setTo(user.getEmail());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					helper.setText(body, true);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mailSenderImpl.send(message);
				
	
		
	}

	@Override
	public void paidPeriodEndReminder(User user, Integer remainingDays) {
	
		String body = "";
		if(remainingDays == 0){			
			body = "Dear " + user.getFirst_name() + "," + "<br>Fasset subscription reminder:<br>"
					+ "Your subscription is ended for fasset.Please subscribe to fasset to continue your account<br><br>"
					+ "<br><div><span style='color: rgb(0, 32, 96);font-size:16px'>With best regards,<br />Team VDAKPO | <a href='https://www.fasset.in/fasset/' target='_blank'>www.fasset.in/fasset</a><br/>Email: fassetdeven@gmail.com<br/>Phone:020-65292835,25465860</span></div><br><br>";
		}else {
			body = "Dear " + user.getFirst_name() + "," + "<br>Fasset subscription reminder:<br>" + "Your Paid Period ends in next "
					+ remainingDays + " days.<br><br>" +"<br><div><span style='color: rgb(0, 32, 96);font-size:16px'>With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>\";";
		} 
		
		MimeMessage message = mailSender.createMimeMessage();
		try {
			message.setSubject("Fasset Subscription");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(message, true, "UTF-8");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			helper.setFrom(user.getEmail());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			helper.setTo(user.getEmail());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			helper.setText(body, true);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mailSenderImpl.send(message);
		
		/*sendMail(user.getEmail(), "faseet", body);	*/	
	}

	@Override
	public void sendSubscriptionMail(User user) throws MessagingException {
		
		String body = "";
			body = "Dear " + user.getFirst_name()+","+"<br><br>"
					+" Your subscribed account is approved successfully for fasset."+"<br>"
					+ "Please log into fasset application: https://www.fasset.in/fasset "+"<br>"
					+ "User Email :  " + user.getEmail() +"<br>"
					+ "Password :  " + Md5.decrypt(user.getPassword())+"<br><br>"+"<br><div><span style='color: rgb(0, 32, 96);font-size:16px'>With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>";

			MimeMessage message = mailSender.createMimeMessage();
			try {
				message.setSubject("Fasset Subscription");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MimeMessageHelper helper = null;
			try {
				helper = new MimeMessageHelper(message, true, "UTF-8");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				helper.setFrom(user.getEmail());
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				helper.setTo(user.getEmail());
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				helper.setText(body, true);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mailSenderImpl.send(message);
	}

	@Override
	public void sendMailToKpoExceAndManager(User user) throws MessagingException {
		String body = "";
		body = "Dear " + user.getFirst_name()+","+"<br><br>"
				+ "You are successfully registered with fasset.<br> Please log into fasset : https://www.fasset.in/fasset<br>"
				+ "User Email :  " + user.getEmail() +"<br>"
				+ "Password :  " + Md5.decrypt(user.getPassword()) +"<br>"
				+ "<br><div><span style='color: rgb(0, 32, 96);font-size:16px'>With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>\";";
	
	MimeMessage message = mailSender.createMimeMessage();
	try {
		message.setSubject("VDAKPO Executive Account");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	MimeMessageHelper helper = null;
	try {
		helper = new MimeMessageHelper(message, true, "UTF-8");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setFrom(user.getEmail());
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setTo(user.getEmail());
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setText(body, true);
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	mailSenderImpl.send(message);
		
	}

	@Override
	public void sendMailToEmpolyee(User user) throws MessagingException {
		
		String body = "";
		body = "Dear " + user.getFirst_name() +","+"<br><br>"
				+ "You are successfully registered with fasset.<br>" +"Please log into fasset : https://www.fasset.in/fasset<br>"
				+ "User Email :  " + user.getEmail() +"<br>"
				+ "Password :  " + Md5.decrypt(user.getPassword())+"<br>"
				+ "<br><div><span style='color: rgb(0, 32, 96);font-size:16px'>With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>\";";

	MimeMessage message = mailSender.createMimeMessage();
	try {
		message.setSubject("Employee Registration");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	MimeMessageHelper helper = null;
	try {
		helper = new MimeMessageHelper(message, true, "UTF-8");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setFrom(user.getEmail());
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setTo(user.getEmail());
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setText(body, true);
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	mailSenderImpl.send(message);
	}

	@Override
	public void sendMailToClientAfterAddingCompany(User user) throws MessagingException {
		String body = "";
		body = "Dear " + user.getFirst_name() +","+"<br><br>"
				+ "You are successfully registered with fasset.<br>" +"Please log into fasset : https://www.fasset.in/fasset<br>"
				+ "User Name :  " + user.getEmail() +"<br>"
				+ "Password :  " + Md5.decrypt(user.getPassword()) +"<br><br>"
				+ "<br><div><span style='color: rgb(0, 32, 96);font-size:16px'>With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>\";";
	MimeMessage message = mailSender.createMimeMessage();
	try {
		message.setSubject("Company Registration");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	MimeMessageHelper helper = null;
	try {
		helper = new MimeMessageHelper(message, true, "UTF-8");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		System.out.println("user get mail is  " +user.getEmail());
		helper.setFrom(user.getEmail());
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setTo(user.getEmail());
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setText(body, true);
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("from user "+message.getFrom());
	System.out.println("receipient "+message.getAllRecipients());
	mailSenderImpl.send(message);
	}
	
	
	public MimeMessage Quotationmethod() throws MessagingException{
		MimeMessage message = mailSender.createMimeMessage();
		return message;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	@Override
	public void sendChangedPassSuccessMail(User user) throws MessagingException {
		String body = MailContent.getPassChangeSuccessMailContent(user);
		MimeMessage message = mailSender.createMimeMessage();
		message.setSubject("Fasset password change");
		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom(user.getEmail());
		helper.setTo(user.getEmail());
		Long roleId = user.getRole().getRole_id();
		if(roleId == MyAbstractController.ROLE_EMPLOYEE || roleId == MyAbstractController.ROLE_MANAGER || roleId == MyAbstractController.ROLE_EXECUTIVE){
			helper.setCc(user.getCompany().getEmail_id());
		}
		helper.setText(body, true);
		mailSenderImpl.send(message);
	}

	@Override
	public void sendLastAccountingYearEditInfoMail(YearEnding year) throws MessagingException {
		
		User  user = new User();
		
		for(User clientSuperUser : companyService.getCompanyWithAllUsers(year.getCompany().getCompany_id()).getUser())
		{
			if(clientSuperUser.getRole().getRole_id()==MyAbstractController.ROLE_CLIENT)
			{
				user=clientSuperUser;
			}
		}
		
		String body = "";
		body = "Dear " + user.getFirst_name() +","+"<br><br>"
				+ "You can now use fasset application for adding or editing last year accounting transactions from "+year.getFromDate().toString()+" "+"to"+year.getToDate().toString()+"."+"<br>" +" Please log into fasset : https://www.fasset.in/fasset<br>"
				+ "<br><div><span style='color: rgb(0, 32, 96);font-size:16px'>With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>\";";

	MimeMessage message = mailSender.createMimeMessage();
	try {
		message.setSubject("Edit Last Accounting Year");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	MimeMessageHelper helper = null;
	try {
		helper = new MimeMessageHelper(message, true, "UTF-8");
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setFrom(user.getEmail());
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setTo(user.getEmail());
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		helper.setText(body, true);
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	mailSenderImpl.send(message);
	}

	
	@Override
	public void sendExceptionReportToClientAndExecutive(User client, List<User> executiveList, List<User> managerList,ByteArrayOutputStream baos1,
			ByteArrayOutputStream baos2, ByteArrayOutputStream baos3, ByteArrayOutputStream baos4,
			ByteArrayOutputStream baos5, ByteArrayOutputStream baos6,ByteArrayOutputStream baosBalanceSheet,ByteArrayOutputStream baosProfitAndLossReport,ByteArrayOutputStream baosIAndEReport,ByteArrayOutputStream trailBalance,Company comp,LocalDate date)
	{
		try {
		
		System.out.println("Entering mail service ");
		String body = "";
		body = "Dear " + " Sir/Madam," +","+"<br><br>"
				+ "With reference to your subscription of Fasset Service, please find attached the set of reports for the current period. It contains:  "+"<br>"
				
				+" 1. Trial Balance<br>" 
				+" 2. Balance Sheet<br>"
				+" 3. Profit & Loss A/c<br>"
				+" 4.EXCEPTIONAL CASH PAYMENT REPORT<br>"
				+" 5.PENDING ADVANCE REPORT<br>"
				+" 6. SHORT GST EXCEPTION REPORT<br>"
				+" 7.  EXCESS GST EXCEPTION REPORT<br>"
				+" 8. TDS NON DEDUCTION REPORT<br>"
				+" 9. NEGATIVE CASH REPORT<br>"
				+"If you have any further queries, please get in touch with your Fasset Executive or send us an email on office@fasset.in or call us on 7745080777.";

			
		
		MimeMessage message = mailSender.createMimeMessage();
		message.setSubject("Fasset Reports");
		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom(client.getEmail());
		helper.setTo("paa@abhyankarassociates.com");
		
		helper.setCc("aparna.sublime2018@gmail.com");
		for(User user :executiveList)
		{
			helper.addCc(user.getEmail());
		}
		for(User user :managerList)
		{
			helper.addCc(user.getEmail());
		}
		helper.setText(body, true);
		//String home = System.getProperty("user.home");
		//home=home + "\\FassetTesting\\fasset\\WebContent\\resources\\templates\\New folder\\";
		//System.out.println("Home is " +home);
		String home = System.getProperty("java.io.tmpdir");
		System.out.println("dirPath is " +home);
		//File file1 = new File(home+"EXCEPTIONAL CASH PAYMENT REPORT"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
	
		File file1 = new File(home +"/"+"EXCEPTIONAL CASH PAYMENT REPORT"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
		try {
			FileOutputStream fos = new FileOutputStream (file1); 
			baos1.writeTo(fos);
		    fos.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
		helper.addAttachment("EXCEPTIONAL CASH PAYMENT REPORT.pdf", file1);
		
		File file2 = new File(home +"PENDING ADVANCE REPORT"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
		try {
			FileOutputStream fos = new FileOutputStream (file2); 
			baos2.writeTo(fos);
		    fos.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
		helper.addAttachment("PENDING ADVANCE REPORT.pdf", file2);
		
		File file3 = new File(home+"SHORT GST EXCEPTION REPORT_"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
		try {
			FileOutputStream fos = new FileOutputStream (file3); 
			baos3.writeTo(fos);
		    fos.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
		helper.addAttachment("SHORT GST EXCEPTION REPORT.pdf", file3);
		
		File file4 = new File(home+"EXCESS GST EXCEPTION REPORT"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
		try {
			FileOutputStream fos = new FileOutputStream (file4); 
			baos4.writeTo(fos);
		    fos.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
		helper.addAttachment("EXCESS GST EXCEPTION REPORT.pdf", file4);
		
		File file5 = new File(home +"TDS NON DEDUCTION REPORT"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
		try {
			FileOutputStream fos = new FileOutputStream (file5); 
			baos5.writeTo(fos);
		    fos.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
		helper.addAttachment("TDS NON DEDUCTION REPORT.pdf", file5);
		if(baos6!=null){
		File file6 = new File(home+"NEGATIVE CASH REPORT"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
		try {
			FileOutputStream fos = new FileOutputStream (file6); 
			baos6.writeTo(fos);
		    fos.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
		helper.addAttachment("NEGATIVE CASH REPORT.pdf", file6);
		}
		File file7 = new File(home+"BalanceSheetReport_"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
		try {
			FileOutputStream fos = new FileOutputStream (file7); 
			baosBalanceSheet.writeTo(fos);
		    fos.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
		helper.addAttachment("BalanceSheetReport.pdf", file7);
		
		
		File file8 = new File(home+"ProfitAndLossReport_"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
		try {
			FileOutputStream fos = new FileOutputStream (file8); 
			baosProfitAndLossReport.writeTo(fos);
		    fos.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
		helper.addAttachment("ProfitAndLossReport.pdf", file8);
		
		
		File file9 = new File(home+"IncomeAndExpenditureReport"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
		try {
			FileOutputStream fos = new FileOutputStream (file9); 
			baosIAndEReport.writeTo(fos);
		    fos.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
		helper.addAttachment("IncomeAndExpenditureReport.pdf", file9);
		
		
		File file10 = new File(home+"TrialBalanceReport"+comp.getCompany_name()+"_"+comp.getCompany_id().toString()+".pdf");
		try {
			FileOutputStream fos = new FileOutputStream (file10); 
			trailBalance.writeTo(fos);
		    fos.close();
		} catch(IOException ioe) {
		    ioe.printStackTrace();
		}
		helper.addAttachment("TrialBalanceReport.pdf", file10);
		
	    mailSenderImpl.send(message);
	    System.out.println("mail sent ");
		}
	    catch(Exception ioe) {
		    ioe.printStackTrace();
		}
	   
	}

	
	
}