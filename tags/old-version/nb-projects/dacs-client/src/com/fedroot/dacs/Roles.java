/*
 * Roles.java
 *
 * Created on October 26, 2005, 11:51 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;

/**
 * DACS roles (may be associated with a DacsUserAccount)
 * TODO: fixme this is incomplete - currently being called with roles string
 * needs to be split on role separator character ':' and made into a list of
 * roles
 * @author rmorriso
 */
public class Roles {
    String name;
    
    /**
     * constructor
     * @param name name of Role
     */
    public Roles(String name) {
        this.name = name;
    }
    
    /**
     * getter for role name
     * @return role name
     */
    public String getName() {
        return name;
    }
    
    public String toString() {
        return name;
    }
    
}
