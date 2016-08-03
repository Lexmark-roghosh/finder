package com.finder.helper;

/**
 * @author roghosh
 *
 */
public class FinderConstants {
	/**
	 * These are the input field names created as constants, which are present within homepage,
	 * i.e - validator.jsp, myproject.jsp
	 */
	public static final String START_PARAM_NAME = "starturl";// this is also used for myproject form name = 'fullSite'
	
	public static final String INDIVIDUAL_GROUP_PARAM_NAME = "groupname";
	
	public static final String INDIVIDUAL_GROUP_LINK_PARAM_NAME = "grouplink";
	
	public static final String PROXY_RADIO_NAME_FOR_FULL = "fullradioInline";
	
	public static final String PROXY_RADIO_NAME_FOR_INDIVIDUAL = "individualradioInline";
	
	public static final String PROXY_YES_RADIO_VALUE_FOR_FULL = "fulloptionYes";
	
	public static final String PROXY_YES_RADIO_VALUE_FOR_INDIVIDUAL = "individualoptionYes";
	
	public static final String PROXY_HOST_NAME_FOR_FULL = "fullproxyHost";
	
	public static final String PROXY_PORT_NAME_FOR_FULL = "fullproxyPort";
	
	public static final String PROXY_HOST_NAME_FOR_INDIVIDUAL = "individualproxyHost";
	
	public static final String PROXY_PORT_NAME_FOR_INDIVIDUAL = "individualproxyPort";
	
	public static final String USER_CHOICE_NAME_FOR_FULL = "fulluserchoice";
	
	public static final String USER_CHOICE_NAME_FOR_INDIVIDUAL = "individualuserchoice";
	
	public static final String USER_CHOICE_WAIT_VALUE_FOR_FULL = "fulloptionwait";
	
	public static final String USER_CHOICE_WAIT_VALUE_FOR_INDIVIDUAL = "individualoptionwait";
	
	public static final String UNIQUE_PREFIX_TO_DISTINGUISH_GROUPLINKS = "@BLf#";
	
	/**
	 *  ----------------------------End of Homepage input fields constants -------------
	 * 
	 *  These are the input field names created as constants, which are present within SignIn / SignUp page,
	 * i.e - 
	 */
	
	public static final String FIRST_NAME_FIELD_NAME = "regFname";
	
	public static final String LAST_NAME_FIELD_NAME = "regLname";
	
	public static final String REGISTRATION_EMAIL_FIELD_NAME = "regEmail";
	
	public static final String REGISTRATION_PASSWORD_FIELD_NAME = "regPassword";
	
	public static final String REGISTRATION_CONFIRM_PASSWORD_FIELD_NAME = "confirmregpassword";
	
	public static final String REGISTRATION_OFFICE_ADDRESS_FIELD_NAME = "ofcaddress";
	
	public static final String REGISTRATION_PIN_CODE_FIELD_NAME = "regPincode";
	
	public static final String REGISTRATION_CONTACT_FIELD_NAME = "regContact";
	
	
	
	public static final String LOGIN_EMAIL_FIELD_NAME = "loginEmail";
	
	public static final String LOGIN_PASSWORD_FIELD_NAME = "loginPassword";
	
	/**
	 * The following string is the name attribute of hidden field for the forms : forgotpassword-form, registration-form
	 */
	
	public static final String HIDDEN_FIELD_NAME = "requestType";
	
	public static final String FORGOTPW_HIDDEN_FIELD_VALUE = "forgot-hidden";
	
	public static final String REG_HIDDEN_FIELD_VALUE = "registration-hidden";
	
	public static final String REOTP_HIDDEN_FIELD_VALUE = "resendotp-hidden";
	/**
	 * These are the input field names created as constants, which are present within SignIn / SignUp page,
	 * i.e -
	 */
	
	public static final String UPDATE_FIRST_NAME_FIELD_NAME = "updateFname";
	
	public static final String UPDATE_LAST_NAME_FIELD_NAME = "updateLname";
	
	public static final String UPDATE_OFFICE_ADDRESS_FIELD_NAME = "updateofcaddress";
	
	public static final String UPDATE_PIN_CODE_FIELD_NAME = "updatePincode";
	
	public static final String UPDATE_CONTACT_FIELD_NAME = "updateContact";
	
	public static final String UPDATE_PROXY_HOST_FIELD_NAME = "proxyHost";
	
	public static final String UPDATE_PROXY_PORT_FIELD_NAME = "proxyPort";
	
	public static final String UPDATE_PROXY_STATUS_FIELD_NAME = "proxyStatus";
	
	public static final String UPDATE_CONTINUOUS_BROKEN_FIELD_NAME = "continuousBroken";
	
	public static final String UPDATE_CONTINUOUS_BROKEN_STATUS_FIELD_NAME = "contBrokenStatus";
	
	
	/**
	  *   These are the constants used for database "linkvalidator"
	 */
	public static final String JDBC_DRIVER_CLASS = "com.mysql.jdbc.Driver";
	
	public static final String QUERY_TO_VERIFY_USER_EXISTANCE = "SELECT email_id FROM user_account";
	
	
	/**
	  *   These are the constants used for request status per user
	 */
	
	public static final String REQUEST_IN_PROGRESS = "Processing";
	
	public static final String REQUEST_PENDING = "Pending";
	
	public static final String REQUEST_COMPLETED = "Completed";
	
	public static final String REQUEST_INTERRUPTED = "Interrupted";

	public static final String REQUEST_TYPE_FULL_SITE = "FULL_SITE";
	
	public static final String REQUEST_TYPE_GROUP_LINK = "GROUP_LINK";
	
	/**
	 * Connection timeout constants
	 */
	public static final int CONNECTION_TIMEOUT_TIME = 5000;
	
	public static final int CONTINIOUS_BROKEN_LIMIT = 50;
	
	/**
	 * Crawl limit constants
	 */
	
	public static final int MAX_ALLOWED_CRAWL_LIMIT = 500;
	
	/**
	 * Contants for BrokenValidator class
	 */
	
	public static final int MALFORMED_URL_RESPONSE_CODE = 100;
	
	public static final int NOT_CONNECTED_URL_RESPONSE_CODE = 700;
	
	public static final int DEFAULT_PROXY_PORT = 80;
	
	public static final String HYPERLINK = "TEXT_LINK";
	
	public static final String DOCLINK = "DOC_LINK";
	
	public static final String IMAGELINK = "IMG_LINK";
	
	public static final String CSSLINK = "CSS_LINK";
	
	public static final String JSLINK = "JS_LINK";
	
	public static final String BLANK_ANCHOR_TEXT = "blankAnchor";
	
	public static final String INTERRUPT_BY_USER = "userInterruption";
	
	public static final String INTERRUPT_FOR_BROKEN_LIMIT_REACHED = "brokenLimitInterruption";
	
	public static final String INTERRUPT_FOR_DATABASE = "databaseInterruption";
	
	/**
	 * Constants for email verification email contents
	 */
	
	public static final String VERIFICATION_EMAIL_SUBJECT = "One Time Password to get access to Broken Link Finder";
	
	public static final String VERIFICATION_EMAIL_BODY_BEFORE_OTP = "Thanks for your interest in our site. Please enter " +
						"following OTP at the site.</br><h2>OTP : ";
	
	public static final String VERIFICATION_EMAIL_BODY_AFTER_OTP = "</h2></br>This OTP will be valid for 30 minutes.";
	
	public static final String FORGOTPW_EMAIL_SUBJECT = "Reset Password Information";
	
	public static final String FORGOTPW_EMAIL_BODY_BEFORE_OTP = "Forgot your account password ? Please enter "+
						"following OTP at the site.</br><h3>OTP : ";
	
	public static final String FORGOTPW_EMAIL_BODY_AFTER_OTP = "</h3>";
	
	public static final String REOTP_EMAIL_SUBJECT = "";
	
	public static final String REOTP_EMAIL_BODY_BEFORE_OTP = "";
	
	public static final String REOTP_EMAIL_BODY_AFTER_OTP = "";
	
}
