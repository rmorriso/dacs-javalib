/*
 * FederationLoaderTest.java
 * Created on Jan 16, 2010 11:45:04 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.entities;

import fedroot.dacs.http.DacsClientContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
public class FederationLoaderTest extends TestCase {

    private FederationLoader federationLoader;
    private Federation federation;
    private Jurisdiction jurisdiction;
    private DacsClientContext dacsClientContext;

    public FederationLoaderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dacsClientContext = new DacsClientContext();
        federationLoader = new FederationLoader("https://fedroot.com/dacs", dacsClientContext);
        federation = federationLoader.getFederation();
        jurisdiction = federation.getJurisdictionByName("TEST");
    }

    public void testGetFederation() {
        try {
            assertEquals("FEDROOT", federation.getFederationName());
            assertEquals(16, federation.getJurisdictions().size());
            Jurisdiction metalogic = federation.getJurisdictionByName("METALOGIC");
            assertNotNull(metalogic);
            assertTrue(metalogic.isAuthenticates());
            Jurisdiction foo = federation.getJurisdictionByName("FOO");
            assertNull(foo);
        } catch (Exception ex) {
            Logger.getLogger(FederationLoaderTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
