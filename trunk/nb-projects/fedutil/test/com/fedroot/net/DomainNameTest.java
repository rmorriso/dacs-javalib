/*
 * DomainNameTest.java
 * JUnit based test
 *
 * Created on December 15, 2005, 12:01 AM
 */

package com.fedroot.net;

import junit.framework.*;

/**
 *
 * @author rmorriso
 */
public class DomainNameTest extends TestCase {
    
    public DomainNameTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DomainNameTest.class);
        
        return suite;
    }

    /**
     * Test of head method, of class com.fedroot.net.DomainName.
     */
    public void testHead() {
        System.out.println("head");
        assertEquals("demo", DomainName.head("demo.fedroot.com"));
        assertNull(DomainName.head(".com"));
        assertNull(DomainName.head("com"));
    }


    
    /**
     * Test of tail method, of class com.fedroot.net.DomainTree.
     */
    public void testTail() {
        System.out.println("tail");
        assertEquals("fedroot.com", DomainName.tail("demo.fedroot.com"));
        assertEquals("com", DomainName.tail(".com"));
        assertNull(DomainName.tail("com"));
    }  
    
}
