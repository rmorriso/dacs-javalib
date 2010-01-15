/*
 * Directive.java
 *
 * Created on October 24, 2005, 3:44 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;

/**
 * Directive class
 * @author rmorriso
 */
public class Directive {
    String name;
    String value;
    
    /**
     * Creates a new instance of Directive; a directive has a name and a value
     * @param name Directive name
     * @param value Directive value
     */
    public Directive(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    /**
     * standard toString() operator
     * @return string representation of Directive
     */
    public String toString() {
        return this.name + " = " + this.value;
    }
}
