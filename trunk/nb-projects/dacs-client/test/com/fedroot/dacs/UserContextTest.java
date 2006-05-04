/*
 * UserContextTest.java
 * JUnit based test
 *
 * Created on August 23, 2005, 3:49 PM
 */

package com.fedroot.dacs;

import com.fedroot.dacs.exceptions.DacsException;
import com.fedroot.dacs.services.DACS;
import junit.framework.*;
import com.fedroot.dacs.test.TestConstants;
import org.apache.commons.httpclient.Cookie;

/**
 *
 * @author rmorriso
 */
public class UserContextTest extends TestCase {
    UserContext usercontext;
    Federation testfed;
    Jurisdiction testjur, openidjur;
    
    public UserContextTest(String testName) 
    throws Exception {
        super(testName);
        usercontext = UserContext.getInstance("test");
        testfed = Federation.getInstance(usercontext, TestConstants.fedadminuri);
        openidjur = testfed.getJurisdictionByName("OPENID");
        testjur = testfed.getJurisdictionByName("TEST");
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        System.out.println("UserContextTest");
        TestSuite suite = new TestSuite(UserContextTest.class);
        return suite;
    }
    

    /**
     * Test of makeCredentials method, of class com.fedroot.dacs.UserContext.
     */
    public void testMakeCredentials() {
        System.out.println("testMakeCredentials");
        try {
            String cookiename = usercontext.makeDacsCredentials(openidjur,"rmorriso.myopenid.com");
            Cookie cookie = usercontext.getCookieByName(cookiename);
            assertNotNull(cookie);
            assertEquals(DACS.makeCookieName(testfed.name,openidjur.name,"rmorriso.myopenid.com"), cookiename);
        } catch (DacsException e) {
            fail("makeDacsCredentials threw unexpected exception: " + e.getMessage());
        } catch (Exception e) {
            fail("makeDacsCredentials threw unexpected exception: " + e.getMessage());
        }
        try {
            String cookiename = usercontext.makeDacsCredentials(testjur,"rmorriso.myopenid.com");
            fail("makeDacsCredentials failed to throw exception ");
        } catch (DacsException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("makeDacsCredentials threw unexpected exception: " + e.getMessage());
        }
    }
}
