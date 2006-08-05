/*
 * UserTest.java
 * JUnit based test
 *
 * Created on October 24, 2005, 8:42 AM
 */

package com.fedroot.dacs;

import com.fedroot.dacs.services.DACS;
import com.fedroot.dacs.test.TestConstants;
import junit.framework.*;


/**
 *
 * @author rmorriso
 */
public class UserTest extends TestCase {
    DacsContext dacscontext;
    UserContext testuser;
    Federation testfed;
    Jurisdiction testjur, dssjur, mljur;
    DacsUserAccount dssaccount, mlaccount_1, mlaccount_2, adminaccount;
    Credentials authResult;
    
    public UserTest(String testName)
    throws Exception {
        super(testName);
        dacscontext = new DacsContext();
        testuser = UserContext.getInstance("Rick Morrison");
        testfed = Federation.getInstance(dacscontext, TestConstants.testbaseuri);
        dssjur = testfed.getJurisdictionByName("DSS");
        mljur = testfed.getJurisdictionByName("METALOGIC");
        testjur = testfed.getJurisdictionByName("TEST");
        mlaccount_1 = new DacsUserAccount(testfed, mljur, TestConstants.mlusername_1, "yes");
        mlaccount_2 = new DacsUserAccount(testfed, mljur, TestConstants.mlusername_2, "yes");
        dssaccount = new DacsUserAccount(testfed, dssjur, TestConstants.dssusername_1, "yes");
        adminaccount = new DacsUserAccount(testfed, testjur, TestConstants.testadminusername, "yes");
    }
    
    protected void setUp() throws Exception {
        // signout all credentials for the test user
        testuser.signoutAll();
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        System.out.println("UserTest");
        TestSuite suite = new TestSuite(UserTest.class);
        
        return suite;
    }
    
    /**
     * Test of authenticate method, of class com.fedroot.dacs.UserContext.
     */
    public void testAuthenticate() {
        System.out.println("testAuthenticate");
        try {
            // authenticate some DacsUserAccounts in the test user
            testuser = UserContext.getInstance("testAuthenticate");
            assertTrue(testuser.authenticate(mlaccount_1, TestConstants.mlpassword_1));
            assertTrue(testuser.authenticate(mlaccount_2, TestConstants.mlpassword_1));
            assertTrue(testuser.authenticate(dssaccount, TestConstants.dsspassword_1));
            assertFalse(testuser.authenticate(mlaccount_1, TestConstants.badpassword_1));
            // UserContext should have three credentials now
            assertEquals(3, testuser.getDacsCredentials().size());
            assertEquals(3, testuser.getDacsCredentials(testfed).size());
            assertEquals(2, testuser.getDacsCredentials(mljur).size());
            assertEquals(1, testuser.getDacsCredentials(dssjur).size());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            fail("Exception in testAuthenticate: " + e.getLocalizedMessage());
        }
    }
    
    /**
     * Test of signout method, of class com.fedroot.dacs.UserContext.
     */
    public void testSignout() {
        System.out.println("testSignout (jurisdiction)");
        try {
            testuser = UserContext.getInstance("testSignout");
            assertTrue(testuser.authenticate(mlaccount_1, TestConstants.mlpassword_1));
            assertTrue(testuser.authenticate(mlaccount_2, TestConstants.mlpassword_1));
            assertTrue(testuser.authenticate(dssaccount, TestConstants.dsspassword_1));
            int n = testuser.getDacsCredentials().size();
            assertEquals(3, n);
            // signing out of dssjur should remove exactly one credential
            int n1 = testuser.signout(mljur);
//            assertEquals(1, n1);
            int n2 = testuser.getDacsCredentials().size();
            assertEquals("signout failed to decrement credentials", 1, n2);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            fail("Exception in UserTest testSignout: " + e.getLocalizedMessage());
        }
    }
    
    public void testSignoutAll() {
        System.out.println("testSignoutAll");
        try {
            testuser = UserContext.getInstance("testSignoutAll");
            assertTrue(testuser.authenticate(mlaccount_1, TestConstants.mlpassword_1));
            assertTrue(testuser.authenticate(mlaccount_2, TestConstants.mlpassword_1));
            assertTrue(testuser.authenticate(dssaccount, TestConstants.dsspassword_1));
            assertEquals(3, testuser.getDacsCredentials().size());
            assertEquals("signout failed to decrement credentials", 0, testuser.signoutAll());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            fail("Exception in DacsUserAccount testSignout: " + e.getLocalizedMessage());
        }
    }
}
