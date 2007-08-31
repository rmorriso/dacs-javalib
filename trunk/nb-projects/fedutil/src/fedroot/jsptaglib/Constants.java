package fedroot.jsptaglib;

/**
 * This class holds Constants that are used in the Registration project.
 * They reference standard commands, jsp page names and file paths.
 *
 * @author Eric Murphy
 *
 */
public class Constants {
    
    /****************************************************************************
     * GLOBAL CONSTANTS
     ***************************************************************************/
    
    /**
     *  The name of the hostUrl attribute set in the servlet context
     *  and the name of the variable set in the Tomcat environment.
     *  The value must be set as a global variable in Tomcat.  This
     *  value is retrieved by using this name.
     */
    // web.xml context-param name for hostURL
    public static final String HOST_URL = "hostUrl";
    
    public static final String ACCESS_CONTAINER = "access/";
    
    public static final String CANCEL = "cancel";
    
    public static final String YES = "yes";
    
    public static final String NO = "no";
    
    public static final String LAST_URL = "lastUrl";
    
    
    
    /****************************************************************************
     * DACS CONSTANTS
     ***************************************************************************/
    // web.xml context-param name for dacs_version
    public static final String DACS_VERSION = "dacs_version";
    
    // web.xml context-param name for  dacs base url parameter in the web.xml
    public static final String DACS_BASE_URL = "dacs_base_url";
    
    // context attribute name for (constructed) dacs signout_url
    public static final String DACS_SIGNOUT_URL = "dacs_signout_url";
    
    // context attribute name for (constructed) dacs authenticate_url
    public static final String DACS_AUTHENTICATE_URL = "dacs_authenticate_url";
    
    // context attribute name for (constructed) dacs current_credentials_url
    public static final String DACS_CURRENT_CREDENTIALS_URL = "dacs_current_credentials_url";
    
    // context attribute name for (constructed) DacsCookieFilter object
    public static final String DACS_COOKIE_FILTER = "dacsCookieFilter";
    
    // web.xml context-param name for  dacs authenticate.cgi
    public static final String DACS_AUTHENTICATE = "dacs_authenticate";
    
    // web.xml context-param name for  dacs signout.cgi
    public static final String DACS_SIGNOUT = "dacs_signout";
    
    // web.xml context-param name for dacs_list_jurisdictions
    public static final String DACS_CURRENT_CREDENTIALS = "dacs_current_credentials";
    
    // web.xml context-param name for dacs jurisdictions
    public static final String DACS_LIST_JURISDICTIONS = "dacs_list_jurisdictions";
    
    // web.xml context-param name for the variable that holds the error code
    public static final String DACS_ERROR_CODE = "DACS_ERROR_CODE";
    
    public static final String DACS_ERROR_URL = "DACS_ERROR_URL";
    
    // version parameter understood by dacs
    public static final String DACS_VERSION_PARAMETER = "DACS_VERSION";
    // version parameter understood by dacs
    public static final String DACS_JURISDICTION_PARAMETER = "DACS_JURISDICTION";
    // version parameter understood by dacs
    public static final String DACS_BROWSER_PARAMETER = "DACS_BROWSER";
    // username parameter understood by dacs
    public static final String DACS_USERNAME_PARAMETER = "USERNAME";
    // password parameter understood by dacs
    public static final String DACS_PASSWORD_PARAMETER = "PASSWORD";
    // version parameter understood by dacs
    public static final String DACS_FORMAT_PARAMETER = "FORMAT";
    // version parameter understood by dacs
    public static final String DACS_FORMAT_VALUE_XML = "XML";
    
    /****************************************************************************
     * CONSTANTS FOR DATABASE CONNECTION POOL
     ***************************************************************************/
    
    /** Name of the parameter in the web.xml for the connection pool tomcat
     * context
     */
    public static final String DB_NAMING_CONTEXT = "dbNameContext";
    
    /** Name of the parameter in the web.xml for the database name */
    public static final String DB_NAME = "dbName";
    
    /****************************************************************************
     * CONSTANTS FOR SENDING EMAIL
     * **************************************************************************/
    
    public static final String FROM_ADDRESS = "fromAddress";
    
    /** Name of the parameter in the web.xml for the email server */
    public static final String EMAIL_CLIENT = "emailClient";
    
    /** Name of the mail session configured in Tomcat */
    public static final String EMAIL_SESSION_NAME = "mail/Session";
    
    /** Right stop for email layout of columnar data */
    public static final int RIGHT_STOP = 20;
    
    /****************************************************************************
     * CONSTANTS FOR REGISTRATION SERVLET
     ****************************************************************************
     *
     * Name of the parameter that specifies doPost 'action'.
     * <pre>
     * example URL:
     *
     * http://someServer.com/registrationServlet?command=jurisdictionList
     *
     * or in a .jsp file:
     *
     * input type="hidden" name="%=Constants.COMMAND%"
     *                       value="%=Constants.BEGIN_REGISTRATION%"
     * </pre>
     */
    public static final String COMMAND = "command";
    
    public static final String JURISDICTION_TYPE = "jurisdictionType";
    
    /** REGISTRATION COMMANDS **************************************************/
    
    /** Command value indicating a valid Jurisdiction has been captured. */
    public static final String JURISDICTION_CAPTURED = "jurCaptured";
    
    /**
     * Command value used for determining if a user has already registered
     * for a specific Jurisdiction, or whether they have previously registered
     * for a different Jurisdiction.
     */
    public static final String CHECK_REGISTRATION = "checkReg";
    
    /** Command value that processes the completed regBean*/
    public static final String PROCESS_PROFILE_FORM = "processProfile";
    
    public static final String REGISTRATION_STATUS  = "regStatus";
    public static final String REG_ACCEPT  = "regAccept";
    public static final String REG_LOCAL   = "regLocal";
    public static final String REG_REMOTE = "regRemote";
    public static final String REG_DECLINE = "regDecline";
    public static final String REG_PROCCESS_PASSWORD = "proccessPassword";
    public static final String REG_CHECK_PASSWORD  = "checkPasswords";
    
    /** attributes set in the session or request */
    public static final String PASSWORD1  = "password1";
    public static final String PASSWORD2  = "password2";
    public static final String TID = "TID";
    
    /** If a user recieves an email and then submits a malformed URL from it
     * (copy and paste error), this is the URL they have submitted.
     */
    public static final String BAD_URL = "badUrl";
    
    /***************************************************************************
     * CONSTANTS FOR LOGIN PROCESS
     ***************************************************************************/
    
    /** Command for calling remote authentication in
     *   {@link AuthenticationServer.doPost(HttpServletRequest, HttpServletResponse)}*/
    public static final String REMOTE = "remote";
    
    /** Command for calling local authentication in
     *   {@link AuthenticationServer.doPost(HttpServletRequest, HttpServletResponse)}*/
    public static final String LOCAL = "local";
    
    /** Attribute for username passed from login.jsp */
    public static final String USERNAME = "username";
    
    /** Attribute for password from login.jsp */
    public static final String PASSWORD = "password";
    
    /** Attribute for the Jurisdiction title from login.jsp */
    public static final String JURISDICTION = "jurisdiction";
    
    /** Parameter name for the original url to redirect to if the authentication
     * process is successful
     */
    public static final String ORIGINAL_REFERER = "origRefer";
    
    /** Parameter for failed login message displayed in login.jsp */
    public static final String LOGIN_FAIL = "loginFail";
    
    /**
     * Parameter to indicate the preferred page to be displayed if the
     * authentication credentials are successfully validated.
     */
    public static final String LOGIN_SUCCESS_PAGE = "login_success_page";
    /**
     * Parameter to indicate the preferred page to be displayed if the
     * authentication credentials are successfully invalidated.
     */
    public static final String LOGIN_FAILURE_PAGE = "login_failure_page";
    
    
    /***************************************************************************
     * MODIFY ACCOUNT CONSTANTS
     **************************************************************************/
    
    /** A list of a users accounts */
    public static final String ACCOUNT_LIST = "accountList";
    
    /** Command to return to the page that was sending the request */
    public static final String RETURN = "return";
    
    /** Command to enter a new password in user's account record. */
    public static final String FORGOT_CHANGE_PASSWORD = "forgotChangePassword";
    
    /** sends new password to user. A
     * {@link org.nfis.access.beans.ForgotPasswordBean} must
     * be set in session to execute this command */
    public static final String FORGOT_PASSWORD = "forgot_password";
    
    /** Discover if user is locally or remotely authenticated and then forward
     *  to modifyLocalPassword.jsp or modifyRemotePassword.jsp
     */
    public static final String LOCAL_OR_REMOTE_MODIFY_PASSWORD ="localOrRemote";
    
    
    
    public static final String MODIFY_ACCOUNT = "modify";
    
    /**
     * Attribute identifying that the user has input the wrong
     * password when trying to modify a profile.
     */
    public static final String WRONG_PASSWORD = "wrongPassword";
    
    /** replaces old password with new one. An
     * {@link org.nfis.access.beans.PasswordBean} must be set in session to
     * execute this command */
    public static final String MODIFY_PASSWORD = "modifyPassword";
    
    /** Command that begins the modify profile path */
    public static final String BEGIN_MODIFY_PROFILE = "begin_modify_password";
    
    /** Command when the modify_profile.jsp is ready to be submitted */
    public static final String MODIFY_PROFILE_REVIEW = "modifyProfileReview";
    
    /** Command to actually initiate the modify profile action */
    public static final String MODIFY_PROFILE = "modifyProfile";
    
    /** Used to signify we are returning to the modify_profile.jsp page */
    public static final String BACK_TO_MODIFY_PROFILE = "backToModifyProfile";
    
    /** Used to reference the original email address in order to track if it
     * has been changed.
     */
    public static final String MODIFY_PROFILE_EMAIL = "mpEmail";
    
    /***************************************************************************
     * PATHS TO SERVLETS AND JSP
     ***************************************************************************/
    
    // can't start a name with a number
    public static final String _403_ERROR_JSP = "403_error.jsp";
    
    public static final String ERROR_HANDLER = "error_handler.jsp";
    
    public static final String AUTH_SERVICE = "local_dacs_auth";
    
    public static final String AUTH_SERVLET = "auth";
    
    public static final String CONFIRMATION_JSP = "confirmation.jsp";
    
    public static final String CONFIRMATION_SIGNOUT = "confirmation_signout.jsp";
    /** DSS documentation */
    public static final String DACS_DOCS = "http://www.dss.bc.ca/dacs-docs/index.php3";
    
    public static final String JAVAX_SERVLET_ERROR_STATUS_CODE = "javax.servlet.error.status_code";
    
    public static final String EMAIL_LINK_ERROR = "email_link_error.jsp";
    
    public static final String ERROR_JSP = "error.jsp";
    
    public static final String ERROR_INIT = "error_init.jsp";
    
    public static final String FORGOT_CHANGE_PASSWORD_JSP = "forgotChangePassword.jsp";
    
    public static final String FORGOT_CHANGE_PASSWORD_SUCCESS_JSP = "forgotChangePasswordSuccess.jsp";
    
    public static final String FORGOT_PASSWORD_DECLINE_JSP = "forgotPasswordDecline.jsp";
    
    public static final String FORGOT_PASSWORD_JSP = "forgot_password.jsp";
    
    public static final String FORGOT_PASSWORD_SUCCESS_JSP = "forgotPasswordSuccess.jsp";
    
    public static final String FORGOT_REMOTE_PASSWORD = "forgotRemotePassword.jsp";
    
    public static final String LOGIN_JSP = "login.jsp";
    
    public static final String LOGIN_SUCCESS_JSP  = "login_success.jsp";
    
    public static final String LOGIN_FAILURE_JSP  = "login.jsp";
    
    public static final String MODIFY_LOCAL_PASSWORD = "modifyLocalPassword.jsp";
    
    public static final String MODIFY_PASSWORD_JSP = "modify_password.jsp";
    
    public static final String MODIFY_PROFILE_DECLINE_JSP = "modify_profile_decline.jsp";
    
    public static final String MODIFY_PROFILE_JSP = "modify_profile.jsp";
    
    public static final String MODIFY_PROFILE_FORWARD_JSP = "modify_profile_forward.jsp";
    
    public static final String MODIFY_PROFILE_INTRO_JSP = "modify_profile_intro.jsp";
    
    public static final String MODIFY_PROFILE_REVIEW_JSP = "modify_profile_review.jsp";
    
    public static final String MODIFY_PROFILE_SUBMITTED_JSP = "modify_profile_submitted.jsp";
    
    public static final String MODIFY_PROFILE_SUCCESS_JSP = "modify_profile_success.jsp";
    
    public static final String MOFIDY_PROFILE_PENDING_JSP = "modify_profile_pending.jsp";
    
    public static final String MODIFY_REMOTE_PASSWORD = "modifyRemotePassword.jsp";
    
    public static final String MODIFY_PASSWORD_SUCCESS = "passwordSuccess.jsp";
    
    public static final String NFIS_EMAIL= "support@nfis.org";
    
    public static final String NFIS_URL = "https://public.secure.nfis.org/access/";
    
    public static final String NO_ACCOUNT_JSP = "no_account.jsp";
    
    public static final String REG_SERVLET_PATH = "reg";
    
    public static final String REGISTRATION = "registration.jsp";
    
    public static final String REGISTRATION_GET_JURISDICTION = "registration_get_jurisdiction.jsp";
    
    public static final String REGISTRATION_REMOTE_LOGIN_JSP = "registration_remote_login.jsp";
    
    public static final String REGISTRATION_REMOTE_LOGIN_SUCCESS_JSP = "registration_remote_login_success.jsp";
    
    public static final String REGISTRATION_GET_EMAIL = "registration_get_email.jsp";
    
    public static final String REGISTRATION_1 = "registration_1.jsp";
    
    public static final String REGISTRATION_GET_PROFILE = "registration_get_profile.jsp";
    
    public static final String REGISTRATION_EMAIL_SENT = "registration_email_sent.jsp";
    
    public static final String REGISTRATION_GET_PASSWORD = "registration_get_password.jsp";
    
    public static final String REGISTRATION_DECLINE = "registration_decline.jsp";
    
    public static final String REGISTRATION_COMPLETE = "registration_complete.jsp";
    
    public static final String REGISTRATION_ALREADY_REGISTERED  = "registration_already_registered.jsp";
    
    public static final String REGISTRATION_PENDING = "registration_pending.jsp";
    
    public static final String ROLES_SERVICE = "roles_service";
    
    public static final String SIGNOUT = "signout.jsp";
    
    public static final String SIGNOUT2 = "signout2.jsp";
    
    public static final String SIGNOUT_SERVLET = "signoutServlet.java";
    
    public static final String USER_INFO_SERVICE = "user_info_service";
    
    public static final String USER_NAME_CHANGED = "userNameChanged";
    
    public static final String WHY_REGISTER = "why_register.jsp";
    
    /***************************************************************************
     * NAMES OF SESSION OR REQUEST ATTRIBUTES
     ***************************************************************************/
    
    /** The name of a DACS Cookie when set as an attribute */
    public static final String DACS_COOKIE = "dacsCookie";
    
    /** The name of an {@link Exception} when it is set as an attribute.*/
    public static final String ERROR = "error";
    
    /** The name of the ForgotPassword bean when it is set as an attribute */
    public static final String FORGOT_PASSWORD_BEAN = "forgotPasswordBean";
    
    /** The name of the login bean when it is set as an attribute */
    public static final String LOGIN_BEAN = "loginBean";
    
    /** The name a PasswordBean when set as an attribute */
    public static final String PASSWORD_BEAN = "passwordBean";
    
    /** The name of the registration bean when it is set as an attribute */
    public static final String REG_BEAN = "regBean";
    
    /** The date an entry was made in the temp_registration table */
    public static final String TEMP_ENTRY_DATE = "tempDate";
    
    /** Attribute name for the RegistrtaionServlet's local jurisdictionList*/
    public static final String LOCAL_JURISDICTION_LIST  = "localJurisdictionList";
    public static final String REMOTE_JURISDICTION_LIST  = "remoteJurisdictionList";
    public static final String ALL_JURISDICTION_LIST  = "allJurisdictionList";
    
    /***************************************************************************
     * LANGUAGE CONSTANTS
     ***************************************************************************/
    
    /** Name of request parameter when changing the session language */
    public static final String LANGUAGE = "language";
    
    /**
     * Attribute name for the English Properties file
     * AND
     * request parameter when specifying English as the session language */
    public static final String ENGLISH = "en";
    
    
    /**
     * Canada's country code as per ISO 3166 Codes
     */
    public static final String CA = "CA";
    
    /**
     * Attribute name for the English Properties file
     * AND
     * request parameter when specifying Frech as the session language
     * */
    public static final String FRENCH = "fr";
    
    /** Attribute name for the Properties file set in the users session */
    public static final String TEXT = "text";
    
    /** Name of page that is passed into header.jsp */
    public static final String PAGE_NAME = "pageName";
    
    /** Name of form which allows you to change language in the header.jsp */
    public static final String LANGUAGE_FORM="language_form";
    
    /** if parameter set in the jsp include for header.jsp the language button
     * will not be shown
     */
    public static final String NO_LANG_BUTTON = "noLangButton";
    
}
