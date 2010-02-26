/*
 * CredentialsLoaderTest.java
 * Created on February 25, 2010 11:45:04 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.entities;

import fedroot.dacs.client.DacsAuthenticateRequest;
import fedroot.dacs.http.DacsClientContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
public class CredentialsLoaderTest extends TestCase {

    private CredentialsLoader credentialsLoader;

    public CredentialsLoaderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DacsClientContext dacsClientContext = new DacsClientContext();
        FederationLoader federationLoader = new FederationLoader(new DacsClientContext(), "https://fedroot.com/dacs");
        federationLoader.load();
        Federation federation = federationLoader.getFederation();
        Jurisdiction test = federation.getJurisdictionByName("TEST");
        credentialsLoader = new CredentialsLoader(dacsClientContext, test);
        // authenticate as test user
        DacsAuthenticateRequest dacsAuthenticateRequest = new DacsAuthenticateRequest(test, "black", "foozle");
        dacsClientContext.executePostRequest(dacsAuthenticateRequest);
    }

    public void testGetCredentials() {
        try {
            credentialsLoader.load();
            Credentials credentials = credentialsLoader.getCredentials();
            assertEquals(1, credentials.getCredentials().size());
            for (Credential credential : credentials.getCredentials()) {
                System.out.println(credential.getName() + credential.getRoles());
            }
        } catch (Exception ex) {
            Logger.getLogger(CredentialsLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
