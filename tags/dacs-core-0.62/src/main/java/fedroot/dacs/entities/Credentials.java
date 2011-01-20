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
 * data type for a DACS credentials
 * @author rmorriso
 */
public class Credentials {
    private String federationName;
    private String federationDomain;
    private Set<Credential> credentials;
    private Credential selectedCredential;
    
    /**
     * create new Credential from Credentials XmlBean components
     * @param dacsjur the DacsJurisdiction XmlBean unmarshalled from the DACS dacs_current_credentials service
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

    public boolean hasCredentials() {
        return credentials.size() > 0;
    }

    public void addCredential(Credential credential) {
        credentials.add(credential);
    }

    public Credential getEffectiveCredential() {
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
