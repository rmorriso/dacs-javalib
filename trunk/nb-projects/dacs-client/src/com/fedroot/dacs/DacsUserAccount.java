/*
 * DacsUserAccount.java
 *
 * Created on August 23, 2005, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs;

import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.services.DACS;
import com.fedroot.dacs.services.DacsAuthenticateService;
import com.fedroot.dacs.services.DacsPasswordService;
import com.fedroot.dacs.services.DacsSignoutService;
import com.fedroot.dacs.xmlbeans.CommonStatusDocument;
import com.fedroot.dacs.xmlbeans.CredentialsDocument;
import com.fedroot.dacs.xmlbeans.DacsAuthReplyDocument;
import com.fedroot.dacs.xmlbeans.DacsCurrentCredentialsDocument;
import com.fedroot.dacs.xmlbeans.DacsPasswdDocument;
import java.io.IOException;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

/**
 * wrapper class for DACS user account (not credentials)
 * @author rmorriso
 */
public class DacsUserAccount {
    Federation federation;
    Jurisdiction jurisdiction;
    String username;
    boolean enabled=false;
    Credentials credentials;
    
    /**
     * constructor for DacsUserAccount
     * @param federation DACS federation in which to create account
     * @param jurisdiction DACS jurisdiction to associate with this account
     * @param username username to associate with this account
     */
    
    public DacsUserAccount(Federation federation, Jurisdiction jurisdiction, String username) {
        this.federation = federation;
        this.jurisdiction = jurisdiction;
        this.username = username;
        this.enabled = true;
        this.credentials = null;
    }
    
    /**
     * constructor for DacsUserAccount
     * @param federation DACS federation in which to create account
     * @param jurisdiction DACS jurisdiction to associate with this account
     * @param username username to associate with this account
     * @param enabled should account be enabled/disabled
     */
    
    public DacsUserAccount(Federation federation, Jurisdiction jurisdiction, String username, String enabled) {
        this.federation = federation;
        this.jurisdiction = jurisdiction;
        this.username = username;
        if(enabled.equals("yes")) {
            this.enabled = true;
        }
        this.credentials = null;
    }
    
    /**
     * authenticate by username/password (wrapper for DACS service dacs_authenticate)
     * @return Credentials object for username
     * @param dacscontext authenticate with respect to this dacscontext
     * @param password password for username
     * @throws java.lang.Exception TODO
     */
    public boolean authenticate(DacsContext dacscontext, String password)
    throws Exception {
        int httpstatus;
        DacsAuthenticateService dacsservice = 
             new DacsAuthenticateService(this.jurisdiction.getDacsUri(), this.jurisdiction.getName(), this.username, password);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        httpstatus = dacscontext.executeMethod(dacsget);
        if (httpstatus == HttpStatus.SC_OK) {
            XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
            dacsget.releaseConnection();
            // Check that it is an instance of the DacsAuthReplyDocument
            if(expectedXmlObject instanceof DacsAuthReplyDocument){
                DacsAuthReplyDocument dacsAuthReplyDocument = (DacsAuthReplyDocument)expectedXmlObject;
                DacsAuthReplyDocument.DacsAuthReply dacsauthreply = dacsAuthReplyDocument.getDacsAuthReply();
                if (dacsauthreply.isSetCommonStatus()) {
                    // common_error should use context and code instead of embedding the code in message
                    if (dacsauthreply.getCommonStatus().getMessage().startsWith("800")) {
                        return false;
                    } else {
                        throw new Exception("User authenticate: exception in DacsAuthenticateService request");
                    }
                }
                if (dacsauthreply.isSetDacsCurrentCredentials()) {
                    DacsCurrentCredentialsDocument.DacsCurrentCredentials dacscurrentcreds =
                            dacsauthreply.getDacsCurrentCredentials();
                    DacsCurrentCredentialsDocument.DacsCurrentCredentials.Credentials dacscreds =
                            dacscurrentcreds.getCredentialsArray(0);
                    String federation =
                            dacscurrentcreds.getFederationName().toString();
                    Cookie cookie =
                            dacscontext.getCookieByName(DACS.makeCookieName(this.federation.getName(), this.jurisdiction.getName(), this.username));
                    this.credentials =
                            new Credentials(this.federation, this.jurisdiction, dacscreds);
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new Exception("User authenticate: invalid DacsAuthReply");
            }
        } else {
            throw new Exception("User authenticate: returned HTTP status " + httpstatus);
        }
    }
   
    /**
     * set password on this DacsUserAccount;
     * calls DacsPasswordService with new password for account
     * PRECONDITION: dacscontext contains credentials for an ADMIN_IDENTITY
     * @return true if signout is successful, else false
     * @param dacscontext execute dacs_passwd service in this context
     * @param password to be set
     * @throws java.lang.Exception TODO
     */
    public boolean passwd(DacsContext dacscontext, String password)
    throws Exception {
        int httpstatus;
        DacsPasswordService dacsservice =
             new DacsPasswordService(this.jurisdiction.getDacsUri(), this.username, password);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        httpstatus = dacscontext.executeMethod(dacsget);
        if (httpstatus == HttpStatus.SC_OK) { 
            // Parse xmlstring from httpget response
            XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
            dacsget.releaseConnection();
            // Check that it is an instance of the DacsPasswdDocument
            if(expectedXmlObject instanceof DacsPasswdDocument){
                DacsPasswdDocument dacsdoc = (DacsPasswdDocument) expectedXmlObject;
                DacsPasswdDocument.DacsPasswd dacspasswd = dacsdoc.getDacsPasswd();
                if (dacspasswd.getCommonStatus().getCode() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else if (expectedXmlObject instanceof CommonStatusDocument) {
                CommonStatusDocument dacsdoc = (CommonStatusDocument) expectedXmlObject;
                throw new Exception("User passwd: error return status; " + dacsdoc.getCommonStatus().getMessage());
            } else {
                throw new Exception("User passwd: error in reply from server");
            }
        } else {
            throw new Exception("User passwd: returned HTTP status " + httpstatus);
        }
    }
    
    /**
     * change password on this DacsUserAccount;
     * calls DacsPasswordService with current and new password for account;
     * this DacsUserAccount, use this variant to allow unprivileged
     * users to change the password on their own accounts.
     * @return true if password change was successful, else false
     * @param dacscontext execute dacs_passwd service in this context
     * @param oldpassword current password for this account - 
     * only a DACS ADMIN_IDENTITY may set a password without supplying the current password
     * @param password new password to be set for this account
     * @throws java.lang.Exception TODO
     */
    public boolean passwd(DacsContext dacscontext, String oldpassword, String password)
    throws Exception {
        int httpstatus;
        DacsPasswordService dacsservice =
                new DacsPasswordService(this.jurisdiction.getDacsUri(), this.username, oldpassword, password);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        httpstatus = dacscontext.executeMethod(dacsget);
        if (httpstatus == HttpStatus.SC_OK) { 
            // Parse xmlstring from httpget response
            XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
            dacsget.releaseConnection();
            // Check that it is an instance of the DacsPasswdDocument
            if(expectedXmlObject instanceof DacsPasswdDocument){
                DacsPasswdDocument dacsdoc = (DacsPasswdDocument) expectedXmlObject;
                DacsPasswdDocument.DacsPasswd dacspasswd = dacsdoc.getDacsPasswd();
                // common_status code = 0 means success...
                if (dacspasswd.getCommonStatus().getCode() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new Exception("Error in DacsPasswdDocument");
            }
        } else {
            throw new Exception("DacsUserAccount passwd: returned HTTP status " + httpstatus);
        }
    }
    
    /**
     * signout (invalidate) credentials associated with this account
     * (no other credentials should be affected); 
     * calls DacsSignoutService on this account's jurisdiction and username
     * @return true if signout was successful
     * Note: CredentialsDocument reply contains credentials that remain after signout -
     * a better check of success would be to check for the presence of credentials for
     * this account before signout and then confirm that they are absent after signout.
     * @param dacscontext invalidate credentials with respect to this this dacscontext
     * @throws java.lang.Exception TODO
     */
    public boolean signout(DacsContext dacscontext)
    throws Exception {
        int httpstatus;
        DacsSignoutService dacsservice =
           new DacsSignoutService(this.jurisdiction.getDacsUri(), this.jurisdiction.getName(), this.username);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        httpstatus = dacscontext.executeMethod(dacsget);
        if (httpstatus == HttpStatus.SC_OK) {
            // Parse xmlstring from httpget response
            XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
            dacsget.releaseConnection();
            // Check that it is an instance of the DacsCurrentCredentialsDocument
            if(expectedXmlObject instanceof DacsCurrentCredentialsDocument){
                DacsCurrentCredentialsDocument dacsdoc = (DacsCurrentCredentialsDocument)expectedXmlObject;
                DacsCurrentCredentialsDocument.DacsCurrentCredentials dacscurrentcreds =
                            dacsdoc.getDacsCurrentCredentials();
                return true;
            } else {
                throw new Exception("Error in DacsCurrentCredentialsDocument");
            }
        } else {
            throw new Exception("DacsUserAccount signout: returned HTTP status " + httpstatus);
        }
    }
    
    /**
     * get DACS federation for this user account
     * @return Federation associated with this DacsUserAccount
     */
    public Federation getFederation() {
        return federation;
    }
    
    /**
     * get DACS jurisdiction for this user account
     * @return Jurisdiction associated with this DacsUserAccount
     */
    public Jurisdiction getJurisdiction() {
        return jurisdiction;
    }
    
    /**
     * get name of this user account
     * @return name of DacsUserAccount
     */
    public String getName() {
        return username;
    }
    
    /**
     * get list of DACS credentials that have been obtained for this user account
     * @return Credentials associated with this DacsUserAccount
     */
    public Credentials getCredentials() {
        return this.credentials;
    }
    
    /**
     * convert to a string representation
     * @return string representation of DacsUserAccount object
     */
    public String toString() {
        return "User: " + jurisdiction.name + ":" + username; //  + ", " + roles.toString();
    }
    
}
