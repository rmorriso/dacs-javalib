/*
 * Event905DefaultHandler.java
 *
 * Created on December 13, 2005, 3:46 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.events;

import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsGetRequest;
import fedroot.dacs.http.DacsStatus;

/**
 *
 * @author rmorriso
 */
public class Event905DefaultHandler implements Dacs905EventHandler{
    /** Creates a new instance of Event902Handler */
    public Event905DefaultHandler() {

    }
    
    @Override
    public int handleEvent(DacsClientContext dacscontext, DacsGetRequest dacsget, DacsAccess905Event event) {
            return DacsStatus.SC_DACS_ACCESS_DENIED;
    }
}
