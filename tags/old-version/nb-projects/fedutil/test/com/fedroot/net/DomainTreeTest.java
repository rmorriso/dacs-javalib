/*
 * DomainTreeTest.java
 * JUnit based test
 *
 * Created on December 14, 2005, 9:56 PM
 */

package com.fedroot.net;

import fedroot.net.DomainTree;
import junit.framework.*;
import java.util.HashMap;

/**
 *
 * @author rmorriso
 */
public class DomainTreeTest extends TestCase {
    DomainTree domaintree;
    
    public DomainTreeTest(String testName) {
        super(testName);
        domaintree = new DomainTree();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DomainTreeTest.class);
        
        return suite;
    }

    /**
     * Test of insert method, of class com.fedroot.net.DomainTree.
     */
    public void testInsert() {
        System.out.println("insert");
        
        domaintree.insert("fedroot.com", "age", new Integer(50));
        assertEquals(new Integer(50), domaintree.get("fedroot.com", "age"));
        domaintree.insert("demo.fedroot.com", "color", "blue");
        assertNull(domaintree.get("fedroot.com", "color"));
        assertEquals(domaintree.get("demo.fedroot.com", "color"), "blue");
    }  
    
    /**
     * Test of search method, of class com.fedroot.net.DomainTree.
     */
    public void testSearch() {
        System.out.println("search");
        // search should find "most specific" value of size
        domaintree.insert("demo.fedroot.com", "size", "large");
        domaintree.insert("fedroot.com", "size", "medium");
        assertEquals("large", domaintree.search("demo.fedroot.com", "size"));
        domaintree.insert("demo.fedroot.com", "size", "huge");
        // check that new values are set correctly (large => huge)
        assertEquals("huge", domaintree.search("demo.fedroot.com", "size"));
        assertNull(domaintree.search(".com", "size"));
        assertNull(domaintree.search("demo.fedroot.com", "location"));
    }  
}
