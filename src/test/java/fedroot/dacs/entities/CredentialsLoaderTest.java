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
    private DacsClientContext dacsClientContext;
    private Credentials credentials;

    public CredentialsLoaderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dacsClientContext = new DacsClientContext();
        FederationLoader federationLoader = new FederationLoader("https://fedroot.com/dacs", dacsClientContext);
        Federation federation = federationLoader.getFederation();
        Jurisdiction test = federation.getJurisdictionByName("TEST");
        // authenticate as test user
        DacsAuthenticateRequest dacsAuthenticateRequest = new DacsAuthenticateRequest(test, "black", "foozle");
        dacsClientContext.executePostRequest(dacsAuthenticateRequest);
        // load credentials from dacsClientContext AFTER authentication
        credentialsLoader = new CredentialsLoader(test, dacsClientContext);
        credentials = credentialsLoader.getCredentials();
    }

    public void testGetCredentials() {
        try {
            assertEquals(1, credentials.getCredentials().size());
            Credential black = credentials.getEffectiveCredentials();
            assertEquals("black", black.getName());
            assertEquals(2,black.getRoles().size());
            assertTrue(black.getRoles().contains(new Role("farmer")));
            assertTrue(black.getRoles().contains(new Role("beautiful")));
        } catch (Exception ex) {
            Logger.getLogger(CredentialsLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
