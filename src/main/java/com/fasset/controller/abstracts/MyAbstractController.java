package com.fasset.controller.abstracts;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class MyAbstractController {

	@Autowired
	protected MessageSource  messages;

	@Value("${db_datePattern}")
	private String defaultPattern;
	
	
	public final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
									 + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public final static String NOT_A =  "N/A";
	
	public final static String SIGNEDUP = "signedUp";
	public final static String SIGNEDUPATT = "signedUpAttorneys";
	public final static String LOGIN = "login";
	public final static String MOBILELOGIN = "mobileLogin";
	public final static String LOGOUT = "logout";
	public final static String LOGOUTMobile = "mobilelogout";
	public final static String FORGOT = "forgotPasswd";
	public final static String HOME = "homePage";
	public final static String MobileHOME = "mobileHomePage";
	public final static String CHANGE_PWD = "changePWD";
	public final static String CHECK_CODE = "checkCode";
	public final static String USER_PROFILE = "userProfile";
	
	public final static String ANYQ = "anyq";
	public final static String BUNDLE = "bundle";
	public final static String CATG_CODE = "catgCode";
	public final static String STRIPE = "stripe";
	public final static String DOWNLOAD = "download";
	public final static String HEAD_TITLE = "headTitle";
	public final static String MESSAGE = "message";
	public final static String NEXT_FORM = "nextForm";
	public final static String ERROR = "error";
	public final static String OK = "ok";
	public final static String CONTINUE_PROC = "commons/continueProc";
	
	public static final int STATUS_INACTIVE = 0;
	public static final int STATUS_PENDING_FOR_APPROVAL = 1;
	public static final int STATUS_TRIAL_LOGIN = 2;
	public static final int STATUS_SUBSCRIBED_USER = 3;
	public static final int STATUS_ACTIVE = 4;
	public static final int STATUS_SUBSCRIPTION_PENDING = 5;
	
	public static final int APPROVAL_STATUS_PENDING = 0;
	public static final int APPROVAL_STATUS_REJECT = 1;
	public static final int APPROVAL_STATUS_PRIMARY = 2;
	public static final int APPROVAL_STATUS_SECONDARY = 3;
	
	public static final Long ROLE_AUDITOR = (long) 1;
	public static final Long ROLE_SUPERUSER = (long) 2;
	public static final Long ROLE_EXECUTIVE = (long) 3;
	public static final Long ROLE_MANAGER = (long) 4;
	public static final Long ROLE_CLIENT = (long) 5;
	public static final Long ROLE_EMPLOYEE = (long) 6;
	public static final Long ROLE_TRIAL_USER = (long) 7;
	
	public static final int PAYMENT_OFFLINE =  1;
	public static final int PAYMENT_RTGS =  2;
	public static final int PAYMENT_ONLINE =  3;
	
	public static final int PAYMENT_TYPE_CASH =  1;
	public static final int PAYMENT_TYPE_CHEQUE =  2;
	public static final int PAYMENT_TYPE_DD =  3;
	public static final int PAYMENT_TYPE_NEFT =  4;
	
	public static final int ENTRY_PENDING =  1;
	public static final int ENTRY_NIL =  2;
	public static final int ENTRY_CANCEL =  3;
	public static final int ENTRY_REVERT =  4;


	public static final int PASSWORD_FORGOT =  1;
	public static final int PASSWORD_CHANGE =  2;
	public static final int PASSWORD_NEW =  3;
	
	public static final int CONTRA_DEPOSITE =  1;
	public static final int CONTRA_WITHDRAW =  2;
	public static final int CONTRA_TRANSFER =  3;
	
	public static final int VOUCHER_SALES_ENTRY = 1;
	public static final int VOUCHER_PURCHASE_ENTRY = 2;
	public static final int VOUCHER_PAYMENT = 3;
	public static final int VOUCHER_RECEIPT = 4;
	public static final int VOUCHER_CREDIT_NOTE = 5;
	public static final int VOUCHER_DEBIT_NOTE = 6;
	public static final int VOUCHER_CONTRA = 7;
	public static final int VOUCHER_MANUAL_JOURNAL = 8;
	public static final int VOUCHER_PAYROLL = 9;
	public static final int DEPRECIATIONAUTOJV = 10;
	public static final int YEAR_AUTOJV = 11;
	public static final int VOUCHER_GSTAJ = 12;
	public static final int AutoJV = 12;


	
	public static final int VOUCHER_GST_AUTOJV = 10;
	
	public static final int DESC_SALES_RETURN = 1;
	public static final int DESC_POST_SALE_DISCOUNT = 2;
	public static final int DESC_DEFICIENCY_IN_SERVICES = 3;
	public static final int DESC_CORRECTION_IN_INVOICES = 4;
	public static final int DESC_CHANGE_IN_POS = 5;
	public static final int DESC_FINALISATION_PROVISIONAL_ASSESSMENTS = 6;
	public static final int DESC_OTHER = 7;
	
	
	//For Year Ending Procedure
	public static final Long ACTIVE_ACCOUNT_YEAR = (long) 1;
	public static final Long DEACTIVE_ACCOUNT_YEAR = (long) 0;
	public static final Long REQUEST_FOR_EDIT_LAST_ACCOUNT_YEAR= (long) 2;
	
	public final static String SuperUserRole = "ROLE_SUPERUSER";
	public final static String ClientRole = "ROLE_CLIENT";
	public final static String Auditor = "ROLE_AUDITOR";
	
	/*public final static String WEBURL = "http://fassetweb.disctesting.in/";
	public final static String APPURL = "http://fassetqa.disctesting.in/";	*/
	
	public final static String WEBURL = "http://www.vdakpo.com/";
	public final static String APPURL = "https://www.fasset.in/fasset/";
	
	public static final Long SUPERUSER_COMPANY = (long) 2;	
	public static final int SIZE_TWO = 2;
	public static final int SIZE_THREE = 3;
	public static final int SIZE_FIVE = 5;
	public static final int SIZE_SIX = 6;
	public static final int SIZE_MINIMUM = 1;
	public static final int SIZE_10 = 10;
	public static final int SIZE_15 = 15;
	public static final int SIZE_20 = 20;
	public static final int SIZE_25 = 25;
	public static final int SIZE_67 = 67;
	public static final int SIZE_80 = 80;
	public static final int SIZE_ONE = 1;
	public static final int SIZE_HUNDRED = 100;
	public static final int SIZE_140 = 140;
	public static final int SIZE_HUNDRED_FIFTY = 150;
	public static final int SIZE_TWO_HUNDRED = 200;
	public static final int SIZE_THOUSAND = 1000;
	public static final int SIZE_FIVE_THOUSAND = 5000;
	public static final int SIZE_THIRTY = 30;
	public static final int SIZE_THIRTY_FIVE = 35;
	public static final int SIZE_82 = 82;
	public static final int SIZE_SEVENTY = 70;
	public static final int SIZE_NINE = 9;
	public static final int SIZE_SEVENTEEN = 17;
	public static final int SIZE_EIGHTEEN = 18;
	public static final int SIZE_TEN = 10;
	public static final int SIZE_12 = 12;
	public static final int SIZE_TWELVE = 12;
	public static final int SIZE_ELEVEN = 11;
	public static final int SIZE_FOUR = 4;
	public static final int SIZE_14 = 14;
	public static final int SIZE_13 = 13;
	public static final int SIZE_FORTY_TWO = 42;
	public static final int SIZE_FIFTY_FOUR = 54;
	public static final int SIZE_THIRTY_FOUR = 34;
	public static final int SIZE_TWENTY_EIGHT = 28;
	public static final int SIZE_THREE_HUNDRED = 300;
	public static final int SIZE_150 = 150;
	public static final int SIZE_FIFTY = 50;
	public static final int SIZE_FORTY_NINE = 49;
	public static final int SIZE_THIRTY_SEVEN = 37;
	public static final int SIZE_FORTY = 40;
	public static final int SIZE_SIXTY = 60;
	public static final int SIZE_FORTY_ONE = 41;
	public static final int SIZE_TWENTY_NINE = 29;
	public static final int SIZE_SIXTY_ONE = 61;
	public static final int SIZE_FORTY_SIX = 46;
	public static final int SIZE_FOURTY = 40;
	public static final int SIZE_TWENTY = 20;
	public static final int SIZE_THIRTY_EIGHT = 38;
	public static final int SIZE_FIVE_HUNDRED = 500;

	protected String getMess(String key) {
		return getMess(key, null);
	}

	protected String getMess(String key, Object[] args) {
		return getMess(key, args, null);
	}

	protected String getMess(String key, Object[] args, Locale locale) {
		String ret = "";
		try {
			ret = messages.getMessage(key, args, locale);
		} catch (Exception e) {
			ret = key;
		}
		return ret;
	}

	protected String getLogin() {
		String userName = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

}