/*
 * RolesConfig.java
 *
 * Created on October 24, 2005, 3:45 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;

import java.util.List;

/**
 * container class for a roles configuration section
 * of a DACS jurisdiction configuration
 * @author rmorriso
 */
public class RolesConfig {
    String id;
    List<Directive> directives;
    
    /**
     * constructor
     * @param id configuration section identifier
     * @param directives list of directives for this roles config section
     */
    public RolesConfig(String id, List<Directive> directives) {
        this.id = id;
        this.directives = directives;
    }
    
    /**
     * standard toString operator
     * @return string representation of RolesConfig
     */
    public String toString() {
        String result = "";
        for (Directive dir : this.directives) {
           result = result + ", " + dir.toString();
        }
        if (id == null) {
            return "RolesConfig[]";
        } else {
            return "RolesConfig[" + id + "] " + result;
        }
    }
}
