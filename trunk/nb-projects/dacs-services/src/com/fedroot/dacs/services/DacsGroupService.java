/*
 * DacsGroupService.java
 *
 * Created on August 30, 2005, 12:12 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.services;

import com.fedroot.util.httpclient.NameValueSet;
import java.util.EnumSet;
import java.util.Set;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import com.fedroot.dacs.services.DACS.ServiceName;

/**
 * wrapper class for DACS dacs_group service;
 * @author rmorriso
 */
public class DacsGroupService extends DacsService{
    public enum args { DACS_VERSION, FORMAT};
    public static final Set<args> mandatory = null;
    public enum ops { acls, dacs_acls, revocations, users };
    
    /**
     * constructor for DacsGroupService;
     * Note: this service has not been well-tested.
     * @param baseuri DACS base uri where dacs_group service is found
     * @param op group operation to be performed
     * @throws java.lang.Exception TODO
     */
    public DacsGroupService(String baseuri, ops op) {
        super(baseuri + "/" + ServiceName.dacs_group + "/" + op.toString());
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>(); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
    }
}
