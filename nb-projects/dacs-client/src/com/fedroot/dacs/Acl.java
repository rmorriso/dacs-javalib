/*
 * Acl.java
 *
 * Created on August 23, 2005, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs;

/**
 * facade for a DACS ACL (Access Control List)
 * @author rmorriso
 */
public class Acl {
    String name;
    boolean enabled=false;
    
    /**
     * constructor for Acl
     * @param aclname name of the ACL (corresponds to DACS ACL filename);
     * we aren't supposed to know that DACS filenames are
     * constructed by prepending "acl-" to the ACL name
     * @param enabled whether the ACL is enabled for access control decisions;
     * we aren't supposed to know that DACS disables an ACL
     * by prepending "disabled-" to an ACL filename
     */
    
    public Acl(String aclname, String enabled) {
        name = aclname;
        if(enabled.equalsIgnoreCase("yes")) {
            this.enabled = true;
        }
    }
    
    
    /**
     * standard toString() operator
     * @return string representation of Acl object
     */
    public String toString() {
        return "Credentials: " + name; //  + ", " + roles.toString();
    }
    
}
