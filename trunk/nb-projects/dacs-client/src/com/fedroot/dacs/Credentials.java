/*
 * Credentials.java
 *
 * Created on August 23, 2005, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;
import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.services.DacsSignoutService;
import com.fedroot.dacs.xmlbeans.CredentialsDocument;
import com.fedroot.dacs.xmlbeans.DacsCurrentCredentialsDocument;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.xmlbeans.XmlObject;

/**
 * wrapper class for DACS credentials;
 * Note: at the moment this class is a bit superfluous. DacsContext is
 * an adequate repository for DACS credentials (cookies). The class will 
 * make more sense when support is added for other forms of credentials (eg X.509
 * certificates).
 * @author rmorriso
 */
public class Credentials {
    Federation federation;
    Jurisdiction jurisdiction;
    String name;
    Roles roles; 
    String cookiename;
    Cookie httpcookie;
    boolean enabled;
    
    /**
     * Creates a new instance of Credentials based on a DACS CredentialsDocument
     * Note: 
     * (1) name clash between com.fedroot.dacs.Credentials and
     * com.fedroot.dacs.xmlbeans.CredentialsDocument.Credentials,
     * (2) DACS CredentialsDocument does not record the federation - it must be determined
     * in context
     * @param federation DACS federation of credentials
     * @param jurisdiction DACS jurisdiction of credentials
     * @param dacscreds DacsCredentials XmlBean from which to build credentials
     * @throws java.lang.Exception TODO
     */
     public Credentials(Federation federation, Jurisdiction jurisdiction, DacsCurrentCredentialsDocument.DacsCurrentCredentials.Credentials dacscreds) 
     throws Exception {
        this.federation = federation;
        this.jurisdiction = jurisdiction;
        this.name = dacscreds.getName().toString();
        if (!dacscreds.getRoles().trim().equals("")) {
            this.roles =  new Roles(dacscreds.getRoles());
        } else {
            this.roles = null;
        }
        this.enabled = true;
     }
     
     public Credentials(Federation federation, DacsCurrentCredentialsDocument.DacsCurrentCredentials.Credentials dacscreds) 
     throws DacsException {
        this.federation = federation;
        this.jurisdiction = federation.getJurisdictionByName(dacscreds.getJurisdiction());
        this.name = dacscreds.getName().toString();
        if (!dacscreds.getRoles().trim().equals("")) {
            this.roles =  new Roles(dacscreds.getRoles());
        } else {
            this.roles = null;
        }
        this.enabled = true;
    }

    /**
     * signout (invalidate) credentials; calls DacsSignoutService on
     * the DACS baseuri, jurisdiction and (user)name
     * @param dacscontext signout of credential using this dacscontext
     * @return true if signout is successful, else false
     * @throws java.lang.Exception throw exception if signout fails
     */
    public boolean signout(DacsContext dacscontext)
    throws Exception {
        int httpstatus;
        DacsSignoutService dacsservice =
                new DacsSignoutService(this.getJurisdiction().getDacsUri(), this.getJurisdiction().getName(), this.getName());
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
                if (dacscurrentcreds.sizeOfCredentialsArray() > 0) {
                    return false;
                } else {
                    this.enabled = false;
                    return true;
                }
            } else {
                throw new Exception("Credentials signout: invalid DacsCurrentCredentialsDocument");
            }
        } else {
            throw new Exception("Credentials signout: returned HTTP status " + httpstatus);
        }
    }
    
    private void nullify() {
        this.enabled = false;
    }
    
    /**
     * test if credentials are enabled/disabled
     * @return true if credential is enabled, else false
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * test credential for membership in a jurisdiction
     * @param jurisdiction jurisdiction to test for membership
     * @return true if credential is in @param jurisdiction, else false
     */
    public boolean inJurisdiction(Jurisdiction jurisdiction) {
        return this.getJurisdiction().getDacsUri().equalsIgnoreCase(jurisdiction.getDacsUri());
    }
    
    /**
     * test credential for membership in a federation
     * @param federation federation to test for membership
     * @return true if credential is in @param federation, else false
     */
    public boolean inFederation(Federation federation) {
        return this.getFederation().getFedadminUri().equalsIgnoreCase(federation.getFedadminUri());
    }

    public Federation getFederation() {
        return federation;
    }

    public Jurisdiction getJurisdiction() {
        return jurisdiction;
    }

    public String getName() {
        return name;
    }
    // TODO: replace use of "::" separator with constant"
    public String getFullName() {
        return federation.getName() + "::" + jurisdiction.getName() + ":" + this.name;
    }

    public Roles getRoles() {
        return roles;
    }
}
