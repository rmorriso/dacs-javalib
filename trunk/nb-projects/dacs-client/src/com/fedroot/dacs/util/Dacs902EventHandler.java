/*
 * Dacs902EventHandler.java
 *
 * Created on December 13, 2005, 10:56 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.util;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.http.DacsGetMethod;

/**
 * interface defining the call-back for all Dacs902EventHandlers
 * @author rmorriso
 */
public interface Dacs902EventHandler {
    /**
     * the logic to handle a DACS 902 (NO_AUTH) event
     * @param dacscontext DacsContext to be used duing event handling
     * @param dacsget DacsGetMethod that caused the event
     * @param event the event object
     * @return DacsStatus code resulting from event handling
     */
    public abstract int handleEvent (DacsContext dacscontext, DacsGetMethod dacsget, DacsAccess902Event event); 
}
