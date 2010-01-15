/*
 * DacsUserAccountTest.java
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
public class DacsUserAccountTest extends TestCase {
    DacsContext dacscontext;
    Federation testfed;
    Jurisdiction testjur, dssjur, mljur;
    DacsUserAccount testaccount_1, testaccount_2, adminaccount;
    Credentials authResult;
    
    public DacsUserAccountTest(String testName) 
    throws Exception {
        super(testName);
            dacscontext = new DacsContext();
            testfed = Federation.getInstance(dacscontext, TestConstants.testbaseuri);
            dssjur = testfed.getJurisdictionByName("DSS");
            mljur = testfed.getJurisdictionByName("METALOGIC");
            testjur = testfed.getJurisdictionByName("TEST");
            testaccount_1 = new DacsUserAccount(testfed, testjur, TestConstants.testusername_1, "yes");
            testaccount_2 = new DacsUserAccount(testfed, testjur, TestConstants.testusername_2, "yes");
            adminaccount = new DacsUserAccount(testfed, testjur, TestConstants.testadminusername, "yes");

            authResult = null;
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DacsUserAccountTest.class);
        
        return suite;
    }

    /**
     * Test of authenticate method, of class com.fedroot.dacs.DacsUserAccount.
     */
    public void testAuthenticate() {
        System.out.println("testAuthenticate (success mode)");
        try {
            assertTrue(testaccount_1.authenticate(dacscontext, TestConstants.testpassword_1));
            assertFalse(testaccount_1.authenticate(dacscontext, TestConstants.testpassword_2));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception in testAuthenticate");
        }
    }
    
    /**
     * Test of signout method, of class com.fedroot.dacs.DacsUserAccount.
     */
    public void testSignout() {
        System.out.println("testSignout");
        try {
            // authenticate tastaccount_1
            assertTrue(testaccount_1.authenticate(dacscontext, TestConstants.testpassword_1));
            // there should now be credentials in testaccount_1
            assertNotNull(testaccount_1.getCredentials());
            int n = testjur.currentCredentials(dacscontext).size();
            assertTrue("testaccount_1 signout failed", testaccount_1.signout(dacscontext));
            int n1 = testjur.currentCredentials(dacscontext).size();
            assertEquals("signout failed to decrement credentials", n - 1, n1);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception in testSignout");
        }
    }
    
    /**
     * Test of passwd method, of class com.fedroot.dacs.DacsUserAccount.
     */
    public void testPasswd() {
        System.out.println("testPasswd");
        Credentials authResult = null;
        try {
            // obtain credentials as an ADMIN_IDENTITY
            assertTrue(adminaccount.authenticate(dacscontext, TestConstants.testadminpassword));
            assertTrue(TestConstants.testpassword_1 != TestConstants.testpassword_2);
            // set the starting password for testuser_2
            assertTrue(testaccount_2.passwd(dacscontext, TestConstants.testpassword_1));
            // testuser_2 should be able to authenticate using this password
            assertTrue(testaccount_2.authenticate(dacscontext, TestConstants.testpassword_1));
            // set the new password for testuser_2
            assertTrue(testaccount_2.passwd(dacscontext, TestConstants.testpassword_2));
            // testuser_2 should not be able to authenticate with the original password
            assertFalse(testaccount_2.authenticate(dacscontext, TestConstants.testpassword_1));
            // testuser_2 should be able to authenticate using the new password
            assertTrue(testaccount_2.authenticate(dacscontext, TestConstants.testpassword_2));
            // signout as adminuser
            assertTrue("adminaccount signout failed", adminaccount.signout(dacscontext));
            // re-authenticate testuser_2 with the new password
            assertTrue(testaccount_2.authenticate(dacscontext, TestConstants.testpassword_2));
            // testuser_2 can change his own password (knowing the current password)
            assertTrue(testaccount_2.passwd(dacscontext, TestConstants.testpassword_2, TestConstants.testpassword_1));
            // testuser_2 can authenticate using the new (original) password
            assertTrue(testaccount_2.authenticate(dacscontext, TestConstants.testpassword_1));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception in testPasswd");
        }
    } 
}
