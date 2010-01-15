/*
 * FederationTest.java
 * JUnit based test
 *
 * Created on October 23, 2005, 3:01 PM
 */

package com.fedroot.dacs;

import com.fedroot.dacs.test.TestConstants;
import junit.framework.*;


/**
 *
 * @author rmorriso
 */
public class FederationTest extends TestCase {
    DacsContext dacscontext;
    Federation fedfed, mlfed, testfed;
    
    public FederationTest(String testName) 
    throws Exception {
        super(testName);
        dacscontext = new DacsContext();
        // testfed = dacsclient.getFederation(TestConstants.testbaseuri);
        fedfed = Federation.getInstance(dacscontext, TestConstants.fedadminuri);
        mlfed = Federation.getInstance(dacscontext, TestConstants.mlbaseuri);
        testfed = Federation.getInstance(dacscontext, TestConstants.testbaseuri);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        System.out.println("FederationTest");
        TestSuite suite = new TestSuite(FederationTest.class);
        
        return suite;
    }
    
    public void testUniqueness() {
        System.out.println("testUniqueness");
        assertEquals(fedfed,  mlfed);
        assertEquals(testfed,  mlfed);
        assertEquals(fedfed.hashCode(),mlfed.hashCode());
        assertEquals(testfed.hashCode(),mlfed.hashCode());
    }

    /**
     * Test of getJurisdictions method, of class com.fedroot.dacs.Federation.
     */
    public void testGetJurisdictions() {
        System.out.println("testGetJurisdictions");
        
        assertEquals(TestConstants.numTESTjurisdictions, testfed.getJurisdictions().size());
    }

    /**
     * Test of getAuthenticatingJurisdictions method, of class com.fedroot.dacs.Federation.
     */
    public void testGetAuthenticatingJurisdictions() {
        System.out.println("testGetAuthenticatingJurisdictions");
        
        assertEquals(TestConstants.numauthTESTjurisdictions,testfed.getAuthenticatingJurisdictions().size());
    }
    
    /**
     * Test of getJurisdictionByName method, of class com.fedroot.dacs.Federation.
     */
    public void testGetJurisdictionByName() {
        System.out.println("testGetJurisdictionByName");
        
        Jurisdiction testjur = testfed.getJurisdictionByName("TEST");
        assertEquals(TestConstants.testbaseuri, testjur.dacsuri);
    }
    
}
