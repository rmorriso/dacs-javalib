/*
 * AuthConfig.java
 *
 * Created on October 24, 2005, 3:45 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;

import java.util.List;

/**
 * facade for the DACS jurisdiction configuration as returned by 
 * dacs_conf service
 * Note: dacs_conf constructs a merged configuration from the DACS
 * site.conf, the default section of dacs.conf and the jurisdiction
 * section of dacs.conf (using DACS scope rules)
 * @author rmorriso
 */
public class AuthConfig {
    String id;
    List<Directive> directives;
    
    /**
     * Creates a new instance of AuthConfig
     * @param id identifier of the auth clause (eg passwd-local, passwd-unix ...) 
     * @param directives list of auth clause directives
     */
    public AuthConfig(String id, List<Directive> directives) {
        this.id = id;
        this.directives = directives;
    }
    
    /**
     * export AuthConfig to string representation 
     * @return string representation of AuthConfig object
     */
    public String toString() {
        String result = "";
        for (Directive dir : this.directives) {
           result = result + ", " + dir.toString();
        }
        return "AuthConfig[" + id + "] " + result;
    }
}
