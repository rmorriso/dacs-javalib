/*
 * Event9xxHandler.java
 *
 * Created on December 13, 2005, 3:46 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs.web;

import com.fedroot.dacs.DacsContext;
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.http.DacsStatus;
import com.fedroot.dacs.util.Dacs9xxEventHandler;
import com.fedroot.dacs.util.DacsAccess9xxEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rmorriso
 */
public class Event9xxHandler implements Dacs9xxEventHandler {
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    /** Creates a new instance of Event902Handler */
    public Event9xxHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public int handleEvent(DacsContext dacscontext, DacsGetMethod dacsget, DacsAccess9xxEvent event) {
        String redirecturl = event.getHandler();
        redirecturl = redirecturl + "?DACS_ERROR_URL=https://demo.fedroot.com/metalogic/dacs-web/EchoCookies" ;
        try {
            response.sendRedirect(response.encodeRedirectURL(redirecturl));
        } catch (Exception e) {
            return DacsStatus.SC_INTERNAL_SERVER_ERROR;
        }
        return DacsStatus.SC_MOVED_PERMANENTLY;
    }

}
