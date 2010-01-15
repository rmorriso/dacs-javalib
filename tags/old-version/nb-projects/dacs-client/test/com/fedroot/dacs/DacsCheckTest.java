/*
 * DacsCheckTest.java
 * JUnit based test
 *
 * Created on November 25, 2005, 5:34 PM
 */

package com.fedroot.dacs;

import junit.framework.*;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author rmorriso
 */
public class DacsCheckTest extends TestCase {
    
    public DacsCheckTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(DacsCheckTest.class);
        
        return suite;
    }
    
    /**
     * Test of test method, of class com.fedroot.dacs.DacsCheck.
     */
    public void testTest() throws Exception {
        System.out.println("test");
        // set to true for testing on Linux
        if (false) {
            // String username = System.getenv("REMOTE_USER");
            String username = ":bob";
            String resource = "/named-user";
            
            boolean expResult = true;
            boolean result = DacsCheck.test(username, resource);
            assertEquals(expResult, result);
        }
    }
    
}
