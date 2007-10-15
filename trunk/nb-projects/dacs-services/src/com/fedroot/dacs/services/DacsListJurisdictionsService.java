/*
 * DacsListJurisdictionsService.java
 *
 * Created on August 30, 2005, 12:12 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.services;

import fedroot.util.httpclient.NameValueSet;
import com.fedroot.dacs.services.DACS.ServiceName;
import java.util.EnumSet;
import java.util.Set;
import org.apache.commons.httpclient.NameValuePair;

/**
 * wrapper class for DACS dacs_list_jurisdictions service;
 * @author rmorriso
 */
public class DacsListJurisdictionsService extends DacsService {
    public enum args { DACS_VERSION, FORMAT };
    public static final Set<args> mandatory = null;
    
    /**
     * constructor for DacsListJurisdictionsService
     * @param baseuri DACS base uri where dacs_list_jurisdiction service is found
     * @throws java.lang.Exception TODO
     */
    public DacsListJurisdictionsService(String baseuri) {
        super(baseuri + "/" + ServiceName.dacs_list_jurisdictions);
        this.serviceuri = baseuri + "/" + ServiceName.dacs_list_jurisdictions;
        this.nvs = new NameValueSet<args>(); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        // defined in this
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
    }
}
