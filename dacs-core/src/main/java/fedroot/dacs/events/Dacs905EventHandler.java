/*
 * Dacs905EventHandler.java
 *
 * Created on December 13, 2005, 10:56 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.events;

import fedroot.dacs.http.DacsClientContext;
import fedroot.dacs.http.DacsGetRequest;



/**
 * interface defining the call-back for all Dacs905EventHandlers
 * @author rmorriso
 */
public interface Dacs905EventHandler {
    /**
     * the logic to handle a DACS 905 (ACK_NEEDED) event
     * @param dacscontext DacsContext to be used during event handling
     * @param dacsget DacsGetMethod that caused the event
     * @param event the event object
     * @return DacsStatus code resulting from event handling
     */
    public abstract int handleEvent (DacsClientContext dacscontext, DacsGetRequest dacsget, DacsAccess905Event event);
}
