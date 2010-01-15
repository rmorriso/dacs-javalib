/*
 * Event902DefaultHandler.java
 *
 * Created on December 13, 2005, 3:46 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs.util;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;

/**
 *
 * @author rmorriso
 */
public class Event902DefaultHandler implements Dacs902EventHandler{

    /**
     * Creates a new instance of Event902DefaultHandler
     */
    public Event902DefaultHandler() {

    }
    
    public int handleEvent(DacsContext dacscontext, DacsGetMethod dacsget, DacsAccess902Event event) {
            return DacsStatus.SC_DACS_ACCESS_DENIED;
    }

}
