/*
 * TestMe.java
 *
 * Created on October 24, 2005, 1:37 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
/**
 *
 * @author rmorriso
 */
public class TestMe {
    
    /** Creates a new instance of TestMe */
    public TestMe() {
    }
    
    public static void main(String args[]) {
        try {
            // String username = System.getenv("REMOTE_USER");
            String username = ":bob";
            String resource = "/named-user";
            
            boolean expResult = true;
            boolean result = DacsCheck.test(username, resource);
        } catch (Exception e) {
            System.out.println("unexpected exception thrown: " + e.getMessage());
        }
    }
}

