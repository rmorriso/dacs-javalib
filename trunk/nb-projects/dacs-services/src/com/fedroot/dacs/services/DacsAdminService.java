/*
 * DacsAdminService.java
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
 * wrapper for dacs_admin service;
 * ensures that only valid dacs_admin service requests are constructed
 * @author rmorriso
 */
public class DacsAdminService extends DacsService{
    public enum args { DACS_VERSION, FORMAT };
    public static final Set<args> mandatory = null;
    public enum ops { acls, dacs_acls, revocations, users };
    public enum RevocationType { deny, revoke };
    
    /**
     * constructor for DacsAdminService/op
     * @param baseuri DACS base uri where dacs_admin service is found
     * @param op dacs_admin operation to execute (/acls, /users, etc)
     * @throws java.lang.Exception TODO
     */
    public DacsAdminService(String baseuri, ops op) {
        super(baseuri + "/" + ServiceName.dacs_admin + "/" + op.toString()); 
        // create NameValueSet of allowed and required arguments
        this.serviceuri = baseuri + "/" + ServiceName.dacs_admin + "/" + op.toString();
        this.nvs = new NameValueSet<args>();
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
    }
}
