/*
 * DacsAccess902Event.java
 *
 * Created on December 13, 2005, 10:53 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs.util;

import com.fedroot.dacs.xmlbeans.Event902Document;

/**
 * wrapper for a DACS 902 (NO_AUTH) event
 * @author rmorriso
 */
public class DacsAccess902Event extends DacsAccessEvent {
    private String message;
    private String handler;
    
    /**
     * Creates a new instance of DacsAccess902Event
     */
    public DacsAccess902Event(String fed_domain, String jur_dacsuri, Event902Document.Event902 event) {
        super();
        this.fed_domain = fed_domain;
        this.jur_dacsuri = jur_dacsuri;
        this.message = event.getMessage();
        this.handler = event.getHandler();
    }
    
    /**
     * return getter for this.message
     * @return message text for the event
     */
    public String getMessage() {
        return this.message;
    }
    
    /**
     * getter for this.handler
     * @return the handler URL string for the event
     */
    public String getHandler() {
        return this.handler;
    }
} 
