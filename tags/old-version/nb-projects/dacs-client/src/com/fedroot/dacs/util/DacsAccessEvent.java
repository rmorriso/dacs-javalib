/*
 * DacsAccessEvent.java
 *
 * Created on December 13, 2005, 12:48 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs.util;

/**
 * abstract superclass to be extended by specific DACS events
 * @author rmorriso
 */
public abstract class DacsAccessEvent {
    protected String fed_domain;
    protected String jur_dacsuri;
    
    public String getFedDomain() {
        return this.fed_domain;
    }
    
    public String getJurUri() {
        return this.jur_dacsuri;
    }
}
