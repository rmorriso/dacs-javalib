/*
 * Credentials.java
 *
 * Created on February 25, 2010, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.entities;

import java.util.HashSet;
import java.util.Set;


/**
 * data type for wrapping a set of DACS credentials, and recording the selected
 * credential
 * @author rmorriso
 */
public class Credentials {
    private String federationName;
    private String federationDomain;
    private Set<Credential> credentials;
    private Credential selectedCredential;
    
    /**
     * create new Credentials wrapper from DACS Credentials XmlBean components
     * @param federationName the DACS federation name
     * @param federationDomain the DACS federation domain
     */
    public Credentials(String federationName, String federationDomain) {
        this.federationName = federationName;
        this.federationDomain = federationDomain;
        this.credentials = new HashSet<Credential>();
    }

    /**
     * @return the federationName
     */
    public String getFederationName() {
        return federationName;
    }

    /**
     * @return the federationDomain
     */
    public String getFederationDomain() {
        return federationDomain;
    }

    public Set<Credential> getCredentials() {
        return credentials;
    }

    public boolean hasCredential(Jurisdiction jurisdiction, String username) {
        for (Credential credential : credentials) {
            if (credential.getJurisdictionName().equals(jurisdiction.getJName()) && credential.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasCredentials() {
        return credentials.size() > 0;
    }

    public void addCredential(Credential credential) {
        credentials.add(credential);
    }

    /**
     * get the selected Credential; the selected Credential is the designated
     * member of the credentials set that should be used
     * @return
     */
    public Credential getSelectedCredential() {
        return selectedCredential;
    }

    /**
     * get the effective credential;
     * the effective credential is the selected credential, when multiple
     * credentials are supported; otherwise the effective credential is the singleton
     * member of the credentials list. It is an error to have multiple credentials
     * without a selected credential
     * @return
     */
    public Credential getEffectiveCredentials() {
        if (selectedCredential != null) {
            return selectedCredential;
        } else {
            for (Credential credential : credentials) {
                return credential;
            }
        }
        return null;
    }
}
