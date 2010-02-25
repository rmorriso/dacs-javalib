/*
 * CredentialsLoaderTest.java
 * Created on February 25, 2010 11:45:04 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.entities;

import fedroot.dacs.DacsTest;
import fedroot.dacs.client.DacsAuthenticateRequest;
import fedroot.dacs.http.DacsClientContext;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
public class CredentialsLoaderTest extends DacsTest {

    CredentialsLoader credentialsLoader;


    public CredentialsLoaderTest(String testName) throws Exception {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DacsClientContext dacsClientContext = new DacsClientContext();
        credentialsLoader = new CredentialsLoader(dacsClientContext, getJurisdiction());
        // authenticate as test user
        DacsAuthenticateRequest dacsAuthenticateRequest = new DacsAuthenticateRequest(getJurisdiction(), "jcarcill", "foozle");
        dacsClientContext.executePostRequest(dacsAuthenticateRequest);
    }

    public void testGetCredentials() {
        try {
            credentialsLoader.load();
            Set<Credential> credentials = credentialsLoader.getCredentials();
            assertEquals(1, credentials.size());
            for (Credential credential : credentials) {
                System.out.println(credential.getName() + credential.getRoles());
            }
        } catch (Exception ex) {
            Logger.getLogger(CredentialsLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
//      signout of an credentials that were created during set up
    }
}
