/*
 * DacsAccess9xxEvent.java
 *
 * Created on December 13, 2005, 10:53 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.events;

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
    public DacsAccess9xxEvent(String fed_domain, String jur_dacsuri, String message, String handler) {
        super();
        this.fed_domain = fed_domain;
        this.jur_dacsuri = jur_dacsuri;
        this.message = message;
        this.handler = handler;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String getHandler() {
        return this.handler;
    }
} 
