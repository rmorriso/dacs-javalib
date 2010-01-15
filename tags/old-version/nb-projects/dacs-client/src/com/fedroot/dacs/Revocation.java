/*
 * Revocation.java
 *
 * Created on August 23, 2005, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs;

/**
 * wrapper class for DACS revocation item
 * @author rmorriso
 */
public class Revocation {
    String item;
    String type;
    
    /**
     * Creates a new instance of Credentials based on DACS CredentialsDocument
     * Note: the name clash between com.fedroot.dacs.Credentials and
     * com.fedroot.dacs.xmlbeans.CredentialsDocument.Credentials
     * @param item entity to which revocation applies (eg, user account name, domain name, etc)
     * @param type type of revocation: deny, revoke or comment
     */
    
    public Revocation(String item, String type) {
        this.item = item;
        this.type = type;
        
    }
    
    
    /**
     * standard toString() operator
     * @return string representation of Revocation object
     */
    public String toString() {
        return "Revocation: " + item + ", " + type;
    }
    
}
