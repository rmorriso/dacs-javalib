/*
 * Event905Handler.java
 *
 * Created on December 13, 2005, 3:46 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs.web;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.DacsNotices;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.dacs.services.DACS;
import com.fedroot.dacs.swingutil.DacsNoticePresentationDialog;
import com.fedroot.dacs.util.Dacs905EventHandler;
import com.fedroot.dacs.util.DacsAccess905Event;
import java.awt.Frame;

/**
 *
 * @author rmorriso
 */
public class Event905Handler implements Dacs905EventHandler{
    /** Creates a new instance of Event902Handler */
    public Event905Handler() {

    }
    
    public int handleEvent(DacsContext dacscontext, DacsGetMethod dacsget, DacsAccess905Event event) {
            return DacsStatus.SC_DACS_ACCESS_DENIED;
    }
}
