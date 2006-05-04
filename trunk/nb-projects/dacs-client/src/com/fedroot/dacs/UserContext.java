/*
 * UserContext.java
 *
 * Created on October 26, 2005, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs;

import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.dacs.services.DACS;
import com.fedroot.dacs.services.DacsAuthAgentService;
import com.fedroot.dacs.services.DacsCurrentCredentialsService;
import com.fedroot.dacs.xmlbeans.DacsAuthAgentDocument;
import com.fedroot.dacs.xmlbeans.DacsCurrentCredentialsDocument;
import com.fedroot.dacs.xmlbeans.DacsCurrentCredentialsDocument.DacsCurrentCredentials;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.xmlbeans.XmlObject;

/**
 * a class representing a human or system entity through
 * which DACS interactions are channelled; 
 * A UserContext may hold many UserAccounts in many jurisdictions/federations; 
 * Note: currently only @class DacsUserAccounts are supported, but other
 * account types (eg Linux/Unix, ActiveDirectory/LDAP, custom database, etc)
 * should be possible
 * 
 * @author rmorriso
 */
public class UserContext extends DacsContext {
    // userdb implements a set of unique UserContext objects
    static HashMap<String,UserContext> userdb = new HashMap<String,UserContext>();
    private String name;
    private List<DacsUserAccount> dacsaccounts;
    private List<Credentials> dacscredentials;
    
    /**
     * Creates a new instance of UserContext
     * 
     * @param name user identifier
     */
    
    private UserContext(String name) {
        super();
        this.name = name;
        this.dacsaccounts = new ArrayList<DacsUserAccount>();
        this.dacscredentials = new ArrayList<Credentials>();
    }
    
    /**
     * static "instance" method enforces uniqueness of UserContext instances
     * synchronized to ensure Thread safety
     * @param username unique identifier of this UserContext
     * @return unique UserContext object for the given username
     */
    public static synchronized UserContext getInstance(String username) {
        // if username is present in userdb, then the UserContext
        // has already been created - return it; otherwise create a new
        // UserContext, insert it in the list and return it
        UserContext usercontext = userdb.get(username);
        if (usercontext == null) {
           usercontext = new UserContext(username);
           userdb.put(username, usercontext);
        }
        return usercontext;
    }
   
    /**
     * authenticate a DacsUserAccount with respect to this usercontext;
     * calls account's authenticate method with the given password
     * 
     * @param account the account to associate with this user
     * @param password password for @param account
     * @return true if authentication is successful, else false
     * @throws java.lang.Exception TODO
     */
    public boolean authenticate(DacsUserAccount account, String password) 
    throws Exception {
        if (account.authenticate(this, password)) {
            this.dacsaccounts.add(account);
            this.dacscredentials.add(account.getCredentials());
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * get a DacsUserAccount without authentication (DANGEROUS! this is like /bin/su)
     * PRECONDITION: this UserContext must contain credentials that have been
     * granted access to dacs_auth_agent
     * @param username name of account
     * @throws DacsException TODO
     * @return authenticated DacsUserAccount
     */
    public String makeDacsCredentials(Jurisdiction jurisdiction, String username)
    throws DacsException, Exception {
        String cookiename = null;
        DacsAuthAgentService dacsservice =
                new DacsAuthAgentService(jurisdiction.dacsuri, jurisdiction.name, username);
        DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
        int dacsstatus = this.executeCheckFailMethod(dacsget, DACS.ReplyFormat.XMLSCHEMA);
        switch (dacsstatus) {
            case DacsStatus.SC_DACS_ACCESS_GRANTED: //check mode
            case DacsStatus.SC_OK: // normal mode
            // Parse dacsget response
            XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
            dacsget.releaseConnection();
            // Check that it is an instance of the DacsAuthAgentDocument
            if(expectedXmlObject instanceof DacsAuthAgentDocument){
                DacsAuthAgentDocument dacsauthagentdoc = (DacsAuthAgentDocument) expectedXmlObject;
                if (dacsauthagentdoc.getDacsAuthAgent().isSetCommonStatus()) {
                    throw new DacsException("DacsAuthAgentService: " + dacsauthagentdoc.getDacsAuthAgent().getCommonStatus().getMessage());
                } else {
                    DacsCurrentCredentials dacscredentials =
                            dacsauthagentdoc.getDacsAuthAgent().getDacsCurrentCredentials();
                    if (dacscredentials.sizeOfCredentialsArray() > 0) {
                        cookiename = dacscredentials.getCredentialsArray(0).getCookieName();
                    }
                }
            } else {
                throw new DacsException("DacsAuthAgent service returned invalid DacsAuthAgentDocument. Please report this as a bug.");
            }
            break;
            case DacsStatus.SC_DACS_ACCESS_DENIED: // check mode
            case DacsStatus.SC_DACS_ACCESS_ERROR:
            default:
                System.out.println(DacsStatus.getStatusText(dacsstatus));
                throw new DacsException("DacsAuthAgentService returned: " + DacsStatus.getStatusText(dacsstatus) + dacsstatus);
        }
        return cookiename;
    }
  
    /**
     * signout (invalidate) usercontext with respect to @param account; 
     * @return true if signout of account was successful, else false
     * @param account to signout
     * @throws java.lang.Exception TODO
     */
    
    public boolean signout(DacsUserAccount account)
    throws Exception {
        return account.signout(this);
    }
    
    /**
     * signout all DacsUserAccounts in @param jurisdiction in this UserContext's
     * DacsContext (other UserContext's may be using the same DacsAccount in other
     * DacsContexts)
     * 
     * @param jurisdiction Jurisdiction from which to signout DacsUserAccounts
     * @return count of DacsUserAccounts that were signed out
     * @throws java.lang.Exception TODO
     */
    public int signout(Jurisdiction jurisdiction)
    throws Exception {
        int n = 0;
        for (DacsUserAccount account : this.dacsaccounts) {
            if (account.getJurisdiction().getName().equals(jurisdiction.getName())) {
                if (account.signout(this)) {
                    n = n++;
                }
            }
        }
        return n;
    }
      
    /**
     * signout (invalidate) user's credentials in @param federation; 
     * calls DacsSignoutService on each credential in dacscredentials 
     * in the given federation
     * @param federation federation in which to signout all credentials
     * @return true if signout is successful, else false
     * @throws java.lang.Exception TODO
     */ 
    public int signout(Federation federation)
    throws Exception {
        int n = 0;
        for (Credentials cred : this.dacscredentials) {
            if (cred.getJurisdiction().getFederation().getName().equals(federation.getName())) {
                if (cred.signout(this)) {
                    n = n++;
                }
            }
        }
        return n;
    } 
    
    /**
     * signout all credentials for this user
     * 
     * @return true on success, false on failure
     * @deprecated to be replaced by method that does one dacs_signout
     * request per federation in which the UserContext has credentials
     * @throws java.lang.Exception TODO
     */
    public int signoutAll()
    throws Exception {
        int n = 0;
        for (Credentials cred : this.dacscredentials) {
            if (cred.signout(this)) {
                n = n++;
            }
        }
        return n;
    }
    
    /**
     * getter for name instance variable
     * @return user name (string)
     */
    public String getName() {
        return name;
    }
    
    public List<Credentials> getDacsCredentials(Federation federation)
    throws DacsException {
        try {
            List<Credentials> newcredentials = new ArrayList<Credentials>();
            int httpstatus;
            DacsCurrentCredentialsService dacsservice =
                    new DacsCurrentCredentialsService(federation.fedadmin_uri);
            DacsGetMethod dacsget = dacsservice.getDacsGetMethod();
            httpstatus = this.executeMethod(dacsget);
            if (httpstatus == HttpStatus.SC_OK) {
                XmlObject expectedXmlObject = XmlObject.Factory.parse(dacsget.getResponseBodyAsStream());
                dacsget.releaseConnection();
                // Check that it is an instance of the DacsAuthReplyDocument
                if(expectedXmlObject instanceof DacsCurrentCredentialsDocument){
                    DacsCurrentCredentialsDocument dacsdoc = (DacsCurrentCredentialsDocument)expectedXmlObject;
                    DacsCurrentCredentialsDocument.DacsCurrentCredentials dacscreds = dacsdoc.getDacsCurrentCredentials();
                    if (dacscreds.isSetCommonStatus()) {
                        throw new DacsException("getDacsCredentials: DacsCurrentCredentialsService request returned error status");
                    } else {
                        List<DacsCurrentCredentialsDocument.DacsCurrentCredentials.Credentials> credentials = dacscreds.getCredentialsList();
                        for (DacsCurrentCredentialsDocument.DacsCurrentCredentials.Credentials foo : credentials) {
                            newcredentials.add(new Credentials(federation, foo));
                            
                        }
                    }
                }
                return newcredentials;
            } else {
                throw new DacsException("getDacsCredentials: DacsCurrentCredentialsService request returned HTTP status: " + httpstatus);
            }
        } catch (Exception ex) {
            throw new DacsException("getDacsCredentials: exception in DacsCurrentCredentialsService request");
        }
    }
   
    /**
     * getter for dacscredentials instance variable
     * @return all credentials for this user
     */
    public List<Credentials> getDacsCredentials() {
        List<Credentials> enabledcreds = new ArrayList<Credentials>();
        for (Credentials cred : dacscredentials) {
            if (cred.isEnabled()) {
                enabledcreds.add(cred);
            }
        }
        return enabledcreds;
    }
    
     /** 
      * filtered getter for dacscredentials; returns 
      * all credentials in @param jurisdiction
      * @param jurisdiction jurisdiction to filter on
      * @return credentials for user in @param jurisdiction
      */
     public List<Credentials> getDacsCredentials(Jurisdiction jurisdiction) {
        List<Credentials> enabledcreds = new ArrayList<Credentials>();
        for (Credentials cred : dacscredentials) {
            if (cred.isEnabled() && cred.inJurisdiction(jurisdiction)) {
                enabledcreds.add(cred);
            }
        }
        return enabledcreds;
    }
     
    /**
     * add DacsUserAccount to user's list of accounts
     * @param account to be added
     */
    public void addDacsUserAccount(DacsUserAccount account) {
        dacsaccounts.add(account);
    }
    
    public String toString() {
        return name;
    }
}
