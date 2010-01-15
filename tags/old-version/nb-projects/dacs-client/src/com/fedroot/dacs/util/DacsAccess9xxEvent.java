/*
 * DacsAccess9xxEvent.java
 *
 * Created on December 13, 2005, 10:53 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs.util;

import com.fedroot.dacs.xmlbeans.Event900Document;
import com.fedroot.dacs.xmlbeans.Event901Document;
import com.fedroot.dacs.xmlbeans.Event903Document;
import com.fedroot.dacs.xmlbeans.Event904Document;
import com.fedroot.dacs.xmlbeans.Event998Document;

/**
 *
 * @author rmorriso
 */
public class DacsAccess9xxEvent extends DacsAccessEvent {
    private String message;
    private String handler;
    
    /**
     * Creates a new instance of DacsAccess902Event
     */
    public DacsAccess9xxEvent(String fed_domain, String jur_dacsuri, Event900Document.Event900 event) {
        super();
        this.fed_domain = fed_domain;
        this.jur_dacsuri = jur_dacsuri;
        this.message = event.getMessage();
        this.handler = event.getHandler();
    }
    
     public DacsAccess9xxEvent(String fed_domain, String jur_dacsuri, Event901Document.Event901 event) {
        super();
        this.fed_domain = fed_domain;
        this.jur_dacsuri = jur_dacsuri;
        this.message = event.getMessage();
        this.handler = event.getHandler();
    }
     
    public DacsAccess9xxEvent(String fed_domain, String jur_dacsuri, Event903Document.Event903 event) {
        super();
        this.fed_domain = fed_domain;
        this.jur_dacsuri = jur_dacsuri;
        this.message = event.getMessage();
        this.handler = event.getHandler();
    }
    public DacsAccess9xxEvent(String fed_domain, String jur_dacsuri, Event904Document.Event904 event) {
        super();
        this.fed_domain = fed_domain;
        this.jur_dacsuri = jur_dacsuri;
        this.message = event.getMessage();
        this.handler = event.getHandler();
    }
    public DacsAccess9xxEvent(String fed_domain, String jur_dacsuri, Event998Document.Event998 event) {
        super();
        this.fed_domain = fed_domain;
        this.jur_dacsuri = jur_dacsuri;
        this.message = event.getMessage();
        this.handler = event.getHandler();
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String getHandler() {
        return this.handler;
    }
} 
