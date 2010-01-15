package com.fedroot.util.httpclient;
/*
 * NameValueSetTest.java
 *
 * Created on August 26, 2005, 11:51 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
import fedroot.util.httpclient.NameValueSet;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;
import junit.framework.*;
import org.apache.commons.httpclient.NameValuePair;


/**
 *
 * @author rmorriso
 */
public class NameValueSetTest extends TestCase {
    enum test {color, age, gender};
    Set<test> mandatory = EnumSet.of (test.color, test.age);
    NameValueSet<test> nvs;
    NameValueSet<String> nvsNoMandatory;
    
    public NameValueSetTest(String testName) {
        super(testName);
    }
    
    protected void setUp() {
        try {
        nvsNoMandatory = new NameValueSet<String>();
        nvs = new NameValueSet<test>(mandatory);
        // color and age are mandatory, gender is optional
        nvs.setValue(test.color, "red"); 
        nvs.setValue(test.age, "3");
        nvsNoMandatory.setValue("Key1", "Value1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(NameValueSetTest.class);
        
        return suite;
    }
    
    
    /**
     * Test of setValue method, of class com.fedroot.dacs.NameValueSet.
     */
    public void testSetValue() {
        System.out.println("testSetValue");
        
        try {
            assertEquals("red",nvs.getValue(test.color));
            nvs.setValue(test.color,"blue");
            assertEquals("blue",nvs.getValue(test.color));
        } catch (Exception e) {
            fail("exception thrown resetting mandatory value");
        }
        
        System.out.println("testSetValue (NoMandatory)");
        try {
            if (nvsNoMandatory == null) {
                System.out.println("nvsNoMandatory is NULL");
            }
            System.out.println("Key1 Value: " + nvsNoMandatory.getValue("Key1"));
            assertEquals("Value1",nvsNoMandatory.getValue("Key1"));
            nvsNoMandatory.setValue("Key2", "Value2");
            System.out.println("Key1 Value: " + nvsNoMandatory.getValue("Key1"));
            assertEquals("Value2",nvsNoMandatory.getValue("Key2"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
    
    /**
     * Test of remove method, of class com.fedroot.dacs.NameValueSet.
     */
    public void testRemove() {
        System.out.println("testRemove (remove mandatory should throw exception)");
        try {
            nvs.remove(test.color);
            fail("attempt to remove mandatory color should have thrown exception");
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            nvs.setValue(test.gender,"F");
            // should contain color, age and gender now
            NameValuePair[] nvp = nvs.getNVPArray();
            assertEquals(3,nvp.length);
            nvs.remove(test.gender);
            // should contain just color, age now
           nvp = nvs.getNVPArray();
            assertEquals(2,nvp.length);
        } catch (Exception e) {
            fail("Unexpected exception was thrown: ");
        }
        System.out.println("testRemove (No Mandatory)");
        try {
            // remove Key1
            nvsNoMandatory.remove("Key1");
            // now try to get it
            nvsNoMandatory.getValue("Key1");
            fail("attempt to get non-existent key should throw exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
    
    /**
     * Test of getNVPArray method, of class com.fedroot.dacs.NameValueSet.
     */
    public void testGetNVPArray() {

        System.out.println("testGetNVPArray (remove option value)");
        NameValueSet<test> nvs = new NameValueSet<test>(mandatory);
        // color and age are mandatory, gender is optional
        nvs.setValue(test.color, "red"); 
        nvs.setValue(test.age, "3");
        try {
            // only color and age have been set and (optional) gender are set
            assertNotNull(nvs);
            nvs.setValue(test.gender,"F");
            NameValuePair[] nvp = nvs.getNVPArray();
            assertEquals(3,nvp.length);
            System.out.println("testGetNVPArray(1)");
            nvs.dumpNameValueSet();
            // now remove gender
            nvs.setValue(test.gender,null);
            nvp = nvs.getNVPArray();
            assertEquals(2,nvp.length);
        } catch (Exception e) {
            e.printStackTrace();
            fail("getNVPArray threw exception");
        }
        try {
            // unset mandatory name color
            nvs.setValue(test.color, null);
            assertNull(nvs.getValue(test.color));
            System.out.println("testGetNVPArray (remove mandatory value)");
            nvs.dumpNameValueSet();
            // getNVPArray() should throw exception
            NameValuePair[] nvp = nvs.getNVPArray();
            fail("required name exception was not thrown");

        } catch (Exception e) {
            assertTrue(true);
        }
        
        try {
            // reset mandatory name color
            nvs.setValue(test.color, "green");
            assertEquals("green", nvs.getValue(test.color));
            // set optional name gender
            nvs.setValue(test.gender,"F");
            NameValuePair[] nvp = nvs.getNVPArray();
            // should contain color, age and gender now
            assertEquals(3,nvp.length);
            System.out.println("testGetNVPArray(3)");
            nvs.dumpNameValueSet();
        } catch (Exception e) {
            fail("getNVPArray threw exception");
        }
        
        System.out.println("testGetNVPArray (No Mandatory)");
        try {
         nvsNoMandatory.setValue("Key2","Value2");
         NameValuePair[] nvp = nvsNoMandatory.getNVPArray();
         // should contain Key1, Key2 now
         assertEquals(2,nvp.length);
         nvsNoMandatory.dumpNameValueSet();
        } catch (Exception e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
}
