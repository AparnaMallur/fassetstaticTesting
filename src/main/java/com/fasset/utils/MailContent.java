package com.fasset.utils;

import java.util.List;

import com.ccavenue.security.Md5;
import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.JavaMD5Hash;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.User;


public class MailContent   {
	
	public MailContent(){
		
	}
	
	public static String getForgotPasswordMailContent(User usr, String appUrl){	
		String strContent="";
		
		String loginUrl = "";		
		
		if(StringUtil.isEmptyOrNullOrNegValue(appUrl) == false){
			appUrl=appUrl.trim();
			loginUrl = appUrl + MyAbstractController.LOGIN;
		}
		strContent=strContent+"Dear " + usr.getFirst_name() +",<br>";
		strContent=strContent+"<br>Your login detail for Fasset is:<br>";
		strContent=strContent+"User Email :  " + usr.getEmail() +"<br>";
				
		String link=appUrl.trim()+"changePWD?email="+usr.getEmail()+"&key="+usr.getEmail_varification_code();
		
		strContent=strContent+"<div><h5><a href='" + link +"'>Click Here to Reset Your Password</a></h5></div></br>";
		strContent=strContent+"<br><br>";
		strContent=strContent+"<div><h5><a href='" + loginUrl +"'>Go to your account</a></h5></div></br>";
	
		
		strContent=strContent+"<div><p>This message contains information that may be confidential. Unless you are the addressee (or authorized to receive emails for the addressee), you may not use, copy or disclose to anyone this message or any information contained in this message. If you have received this message in error, please advise the sender by reply e-mail to help@fasset.com, and delete this message. </p></div>";
		strContent=strContent+"With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>";

		return strContent;
	}
	
	public static String getRegistrationMailContent(User usr,List<ClientValidationChecklist> list){	
		String strContent="";
		strContent=strContent+"Dear " + usr.getFirst_name() +",<br>";
		strContent=strContent+"<br>You are successfully registered for fasset, approval procedure is in process, we will confirm to you over email soon<br>";
		
		strContent=strContent+"<br><b>Please kindly send following documents to VDAKPO</b><br>";
		
		strContent=strContent+"<br><b>Manadatory Documents</b><br>";
		for(ClientValidationChecklist validation : list)
		{
			if(validation.getIs_mandatory()!=null && validation.getIs_mandatory()==true)
			{
				strContent=strContent+"<br>"+validation.getChecklist_name()+"<br>";
			}
		}
		strContent=strContent+"<br><b>Non Manadatory Documents</b><br>";
		for(ClientValidationChecklist validation : list)
		{
			if(validation.getIs_mandatory()!=null && validation.getIs_mandatory()==false)
			{
				strContent=strContent+"<br>"+validation.getChecklist_name()+"<br>";
			}
		}
		strContent=strContent+"<br><div>If you have any questions, we're always here to help you!</div>";
		strContent=strContent+"<div>Contact us: 020-65292835,25465860 or email: fassetdeven@gmail.com</div><br>";	
		strContent=strContent+"With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>";
		
		return strContent;
		
	}
	
	public static String getRegistrationMailContentTrial(User usr){	
		String strContent="";
		strContent=strContent+"Dear " + usr.getFirst_name() +",<br>";
		strContent=strContent+"<br>You are successfully registered for fasset.<br>";		
		
		strContent=strContent+"<br>Please sign into your Fasset account: <a href='https://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a><br>";
		
		strContent=strContent+"User Email :  " + usr.getEmail() +"<br>";
	
		String pass = Md5.decrypt(usr.getPassword());
		strContent=strContent+"Password :  " + pass+"<br><br><br>";

		strContent=strContent+"<br><div>If you have any questions, we're always here to help you!</div>";
		strContent=strContent+"<div>Contact us: 020-65292835,25465860 or email: fassetdeven@gmail.com</div><br>";	
		strContent=strContent+"<br><div><b>Note: All Masters added in trial account will be not available in subscribed account!!!</b></div>";
		strContent=strContent+"With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>";
		
		return strContent;		
	}
	
	
	public static String getquotationdetails(Quotation quotation){	
		String strContent="<span font-size:12px>";				
		strContent=strContent+"Dear " + quotation.getFirst_name() +",<br>";
		strContent=strContent+"<br>Your Fasset account is successfully created. Please enter following quotation no: <b>"+quotation.getQuotation_no()+"</b> and subscribe with us!";
		strContent=strContent+"<br>Please sign into your Fasset account: <a href='https://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a><br>";
    	strContent=strContent+"<br><ul>";
    	for (QuotationDetails details : quotation.getQuotationDetails()) {
	    	strContent=strContent+"<li>"+details.getService_id().getService_name()+" - "+details.getFrequency_id().getFrequency_name()+" : <b>"+details.getAmount()+"</b></li>";
		}
	    /*for(int i=0 ; i < quotation.getQuotationDetails().size() ; i++)
	    {
	    }*/
	    strContent=strContent+"</ul>";
		//strContent=strContent+"<br>Sign into your Fasset account: http://fasset.disclive.in/fasset<br>";
		strContent=strContent+"With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>";
		return strContent;		
	}
	
	
	
	public static String getPassChangeSuccessMailContent(User usr){	
		String strContent="";
		strContent=strContent+"Dear " + usr.getFirst_name() +",<br>";
		strContent=strContent+"<br>Your Fasset password have changed successfully.<br>";
		strContent=strContent+"<br><div>If you have any questions, we're always here to help you!</div>";
		strContent=strContent+"<div>Contact us: 020-65292835,25465860 or email: fassetdeven@gmail.com</div><br>";	
		strContent=strContent+"<br><div><span style='color: rgb(0, 32, 96);font-size:16px'>With best regards,<br />Team VDAKPO | <a href='http://www.fasset.in/fasset/' target='_blank'>www.fasset.in</a>|<a href='http://www.vdakpo.com/' target='_blank'>www.vdakpo.com</a><br/>Email: office@fasset.in<br/>Phone:020-65292835,25465860</span></div><br><br>";
		
		return strContent;
		
	}
}
