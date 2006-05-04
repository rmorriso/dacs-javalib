/*
 * Event902Handler.java
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
import com.fedroot.dacs.util.DacsAccess902Event;
import com.fedroot.dacs.util.Dacs902EventHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.URI;

/**
 *
 * @author rmorriso
 */
public class Event902Handler implements Dacs902EventHandler{
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    /** Creates a new instance of Event902Handler */
    public Event902Handler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public int handleEvent(DacsContext dacscontext, DacsGetMethod dacsget, DacsAccess902Event event) {
        String redirecturl = event.getHandler();
        try {
            String orig_refer = dacsget.getURI().toString();
            redirecturl = redirecturl + "?DACS_ERROR_URL=https://demo.fedroot.com/metalogic/dacs-web/Test902Handler" ;
            //redirecturl = redirecturl + "?DACS_ERROR_URL=" + orig_refer;         
            response.sendRedirect(response.encodeRedirectURL(redirecturl));
        } catch (Exception e) {
            return DacsStatus.SC_INTERNAL_SERVER_ERROR;
        }
        return DacsStatus.SC_MOVED_PERMANENTLY;
    }
    
}
