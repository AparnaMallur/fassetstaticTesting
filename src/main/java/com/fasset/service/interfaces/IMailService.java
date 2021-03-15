package com.fasset.service.interfaces;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.mail.MessagingException;

import org.joda.time.LocalDate;

import com.fasset.entities.Company;
import com.fasset.entities.Quotation;
import com.fasset.entities.User;
import com.fasset.entities.YearEnding;


public interface IMailService {
	void sendMail(String recipient, String subject, String message);
	void sendSignupMail(User user) throws MessagingException;
	void sendChangedPassMail(User user) throws MessagingException;
	void sendChangedPassSuccessMail(User user) throws MessagingException;
	void trialPeriodEndReminder(User user,Integer remainingDays);
	void paidPeriodEndReminder(User user,Integer remainingDays);
	//void sendQuotationMail(Quotation quotation,ByteArrayOutputStream baos) throws MessagingException;
	void sendSubscriptionMail(User user) throws MessagingException;
	void sendMailToKpoExceAndManager(User user) throws MessagingException;
	void sendMailToEmpolyee(User user) throws MessagingException;
	void sendMailToClientAfterAddingCompany(User user) throws MessagingException;
	void sendQuotationMail(Quotation quotation,ByteArrayOutputStream baos) throws MessagingException;
	void sendLastAccountingYearEditInfoMail(YearEnding year) throws MessagingException;
	
	void sendExceptionReportToClientAndExecutive(User client,List<User> executiveList,List<User> managerList,ByteArrayOutputStream baos1,ByteArrayOutputStream baos2,ByteArrayOutputStream baos3,ByteArrayOutputStream baos4,ByteArrayOutputStream baos5,ByteArrayOutputStream baos6,ByteArrayOutputStream baosBalanceSheet,ByteArrayOutputStream baosProfitAndLossReport,ByteArrayOutputStream baosIAndEReport,ByteArrayOutputStream trailBalance , Company comp,LocalDate date);
	
}