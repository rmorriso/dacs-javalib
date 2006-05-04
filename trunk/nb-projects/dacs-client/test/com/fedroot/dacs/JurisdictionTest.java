/*
 * JurisdictionTest.java
 * JUnit based test
 *
 * Created on October 21, 2005, 7:11 PM
 */

package com.fedroot.dacs;

import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.services.DACS;
import com.fedroot.dacs.test.TestConstants;
import java.util.List;
import junit.framework.*;


/**
 *
 * @author rmorriso
 */
public class JurisdictionTest extends TestCase {
    DacsContext dacscontext;
    Federation testfed;
    Jurisdiction testjur, testjur1, dssjur, mljur;
    DacsUserAccount testaccount_1, testaccount_2, dssaccount, mlaccount, adminaccount;
    Credentials authResult;

    
    public JurisdictionTest(String testName)
    throws Exception {
            super(testName);
            dacscontext = new DacsContext();
            testfed = Federation.getInstance(dacscontext, TestConstants.testbaseuri);
            dssjur = testfed.getJurisdictionByName("DSS");
            mljur = testfed.getJurisdictionByName("METALOGIC");
            testjur = testfed.getJurisdictionByName("TEST");
            testjur1 = testfed.getJurisdictionByName("TEST");
            testaccount_1 = new DacsUserAccount(testfed, testjur, TestConstants.testusername_1, "yes");
            testaccount_2 = new DacsUserAccount(testfed, testjur, TestConstants.testusername_2, "yes");
            dssaccount = new DacsUserAccount(testfed, dssjur, TestConstants.dssusername_1, "yes");
            mlaccount = new DacsUserAccount(testfed, dssjur, TestConstants.mlusername_1, "yes");
            adminaccount = new DacsUserAccount(testfed, testjur, TestConstants.testadminusername, "yes");

            authResult = null;
    }

    protected void setUp() throws Exception {
        // signout out of ALL credentials in the federation
        int n = testjur.signoutFederation(dacscontext);
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        System.out.println("JurisdictionTest");
        TestSuite suite = new TestSuite(JurisdictionTest.class);
        
        return suite;
    }

     public void testUniqueness() {
        System.out.println("testUniqueness");
        assertEquals(testjur,  testjur1);
        assertEquals(testjur.hashCode(),testjur1.hashCode());
    }
    /**
     * Test of currentCredentials method, of class com.fedroot.dacs.Jurisdiction.
     */
    public void testCurrentCredentials() {
        Credentials authResult = null;
        List<Credentials> credsResult;
        System.out.println("testCurrentCredentials");
        
        try {
            // authenticate as testaccount_1
            assertTrue(testaccount_1.authenticate(dacscontext, TestConstants.testpassword_1));
            // authenticate as dssaccountname_1
            assertTrue(dssaccount.authenticate(dacscontext, TestConstants.dsspassword_1));
            credsResult = dssjur.currentCredentials(dacscontext);
            assertEquals(2, credsResult.size());
            assertEquals(2,dacscontext.getDacsCookies().size());
            dacscontext.dumpDacsCookies(System.out);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            fail("Exception in dacscontext.currentCredentials: " + e.getLocalizedMessage());
        }
    }
    
    /**
     * Test of signout method, of class com.fedroot.dacs.Jurisdiction.
     */
    public void testSignout() {
        System.out.println("testSignout");
        try {
            int n = dssjur.currentCredentials(dacscontext).size();
            assertTrue(n == 0);
            // authenticate once in each of the 3 jurisdictions TEST, DSS, METALOGIC
            assertTrue(testaccount_1.authenticate(dacscontext, TestConstants.testpassword_1));
            assertTrue(dssaccount.authenticate(dacscontext, TestConstants.dsspassword_1));
            assertTrue(mlaccount.authenticate(dacscontext, TestConstants.mlpassword_1));
            // n is the count of credentials in the federation to which testjur,
            // dssjur and mljur belong
            n = testjur.currentCredentials(dacscontext).size();
            assertTrue(n == 3);
            // signout from testjur should only remove one credential (obtained through
            // testaccount_1) so testjur.signout() should return 2
            int n1 = testjur.signout(dacscontext);
            assertEquals(n - 1, n1);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            fail("Exception in dacscontext.testSignout: " + e.getLocalizedMessage());
        }
    }
    
        /**
     * Test of signout method, of class com.fedroot.dacs.Jurisdiction.
     */
    public void testSignoutFederation() {
        System.out.println("testSignoutFederation");
        try {
            int n = dssjur.currentCredentials(dacscontext).size();
            assertTrue(n == 0);
            // authenticate a few DacsUserAccounts
            assertTrue(testaccount_1.authenticate(dacscontext, TestConstants.testpassword_1));
            assertTrue(dssaccount.authenticate(dacscontext, TestConstants.dsspassword_1));
            assertTrue(mlaccount.authenticate(dacscontext, TestConstants.mlpassword_1));
            n = testjur.currentCredentials(dacscontext).size();
            assertTrue(n == 3);
            // there should be no credentials after sign-out from federation
            n = testjur.signoutFederation(dacscontext);
            assertEquals(0, n);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            fail("Exception in dacscontext.testSignoutFederation: " + e.getLocalizedMessage());
        }
    }
    
    /**
     * Test of loadAcls method, of class com.fedroot.dacs.Jurisdiction.
     */
    public void testGetAcls() {
        Credentials authResult = null;
        List<Acl> aclsResult;
        System.out.println("testGetAcls");
        
        try {
            // obtain credentials as a DACS_ADMIN_IDENTITY (TEST jurisdiction)
            assertTrue(adminaccount.authenticate(dacscontext, TestConstants.testadminpassword));
            aclsResult = testjur.loadAcls(dacscontext);
            assertEquals(TestConstants.numTESTacls, aclsResult.size());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            fail("Exception in dacscontext.testGetAcls: " + e.getLocalizedMessage());
        }
    }
        
    /**
     * Test of loadConfiguration method, of class com.fedroot.dacs.Jurisdiction.
     */
    public void testLoadConfiguration() {
        System.out.println("testLoadRevocations");
        try {
            // obtain credentials as a DACS_ADMIN_DENTITY
            assertTrue(adminaccount.authenticate(dacscontext, TestConstants.testadminpassword));
            JurisdictionConfiguration config = testjur.getConfiguration(dacscontext);
            config.dumpConfiguration(System.out);
        } catch (DacsException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail("unexpected exception thrown: " + e.getMessage());
        } 
    }
    /**
     * Test of loadRevocations method, of class com.fedroot.dacs.Jurisdiction.
     */
    public void testLoadRevocations() {
        System.out.println("testLoadRevocations");
        try {
            // obtain credentials as a DACS_ADMIN_DENTITY
            assertTrue(adminaccount.authenticate(dacscontext, TestConstants.testadminpassword));
            testjur.loadRevocations(dacscontext);
            assertEquals(TestConstants.numTESTrevocations, testjur.sizeOfRevocations());
        } catch (Exception e) {
            fail("exception thrown");
        }
    }
    
    
    /**
     * Test of getGetUsers method, of class com.fedroot.dacs.Jurisdiction.
     */
    public void testGetUsers() {
        Credentials authResult = null;
        List<DacsUserAccount> usersResult;
        System.out.println("testGetUsers");
        
        try {
            // obtain credentials as a DACS_ADMIN_DENTITY
            assertTrue(adminaccount.authenticate(dacscontext, TestConstants.testadminpassword));
            usersResult = testjur.getDacsUserAccounts(dacscontext);
            assertEquals(TestConstants.numTESTusers, usersResult.size());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            fail("Exception in dacscontext.testGetAcls: " + e.getLocalizedMessage());
        }
    }
    
    /**
     * Test of getGetAuthenticatedUser method, of class com.fedroot.dacs.Jurisdiction.
     */
    public void testGetAuthenticatedUser() {
        Credentials authResult = null;
        DacsUserAccount user;
        System.out.println("testGetAuthenticatedUser");
        
        try {
            // obtain credentials as a DACS_ADMIN_DENTITY
            assertTrue(adminaccount.authenticate(dacscontext, TestConstants.testadminpassword));
            // get credentials count before getting authenticated user
            int n = testjur.currentCredentials(dacscontext).size();
            // get an authenticated user "foo"
            user = testjur.getAuthenticatedUser(dacscontext, "foo");
            assertNotNull(user);
            assertEquals(testjur.getFederation().getName(), user.getFederation().getName());
            assertEquals(testjur.getName(), user.getJurisdiction().getName());
            assertNotNull("DACS cookie not found in dacscontext", 
                    dacscontext.getCookieByName(
                        DACS.makeCookieName(user.getFederation().getName(), user.getJurisdiction().getName(), "foo")));
            int n1 = testjur.currentCredentials(dacscontext).size();
            assertEquals("failed to increment number of credentials", n + 1, n1);
        } catch (Exception e) {
            fail("Exception in testGetAuthenticatedUser: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
