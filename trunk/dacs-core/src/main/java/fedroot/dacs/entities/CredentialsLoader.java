/*
 * CredentialsLoader.java
 * Created on Jan 16, 2010 5:48:01 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.entities;

import com.fedroot.dacs.DacsCurrentCredentials;
import fedroot.dacs.client.DacsCurrentCredentialsRequest;
import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
public class CredentialsLoader extends WebServiceEntityLoader {

    private Credentials credentials;
    private DacsCurrentCredentialsRequest dacsCurrentCredentialsRequest;

    public CredentialsLoader(Jurisdiction jurisdiction, DacsClientContext dacsClientContext) throws DacsException {
        super(new DacsCurrentCredentialsRequest(jurisdiction));
        DacsCurrentCredentials dacsCurrentCredentials = (DacsCurrentCredentials) load(dacsClientContext);
        credentials = new Credentials(dacsCurrentCredentials.getFederationName(), dacsCurrentCredentials.getFederationDomain());
        for (DacsCurrentCredentials.Credentials credential : dacsCurrentCredentials.getCredentials()) {
            credentials.addCredential(new Credential(credential.getFederation(), credential.getJurisdiction(), credential.getName(), credential.getRoles(), credential.getAuthStyle(), credential.getCookieName()));
        }
    }

    /**
     * Returns the DACS current credentials
     *
     * @return the federation
     */
    public Credentials getCredentials() {
        return this.credentials;
    }
}
