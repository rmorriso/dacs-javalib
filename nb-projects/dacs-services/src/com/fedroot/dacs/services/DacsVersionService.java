/*
 * DacsVersionService.java
 *
 * Created on August 30, 2005, 12:12 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.services;

import fedroot.util.httpclient.NameValueSet;
import java.util.EnumSet;
import java.util.Set;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import com.fedroot.dacs.services.DACS.ServiceName;

/**
 * wrapper for dacs_version service;
 * ensures that only valid dacs_admin service requests are constructed
 * @author rmorriso
 */
public class DacsVersionService extends DacsService{
    public enum args { DACS_VERSION, FORMAT};
    public static final Set<args> mandatory = null;
    
    /**
     * constructor for DacsVersionService
     * @param baseuri DACS base uri where dacs_version service is found
     * @throws java.lang.Exception TODO
     */
    public DacsVersionService(String baseuri) {
        super(baseuri + "/" + ServiceName.dacs_version);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>(); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
    }
}
