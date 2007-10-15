/*
 * DacsSignoutService.java
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
 * wrapper class for DACS dacs_signout service;
 * provides constructors for signout of individual credentials,
 * all credentials for a given DACS jurisdiction, or all
 * credentials for a given DACS federation
 * 
 * @author rmorriso
 */
public class DacsSignoutService extends DacsService{
    public enum args { DACS_JURISDICTION, DACS_USERNAME, DACS_VERSION, FORMAT };
    public static final Set<args> mandatory = null;
    
    /**
     * 
     * constructor for DacsSignoutService (all signout);
     * calls dacs_signout service (under baseuri) invalidating
     * all credentials assigned in the federation
     * @param baseuri DACS base uri where dacs_signout service is found
     * @throws java.lang.Exception TODO
     */
    public DacsSignoutService(String baseuri) {
        super(baseuri + "/" + ServiceName.dacs_signout);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>();
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
    }
    
    /**
     * 
     * constructor for DacsSignoutService (Jurisdiction signout);
     * calls dacs_signout service on jurisdiction, invalidating
     * all credentials in that jurisdiction
     * @param baseuri DACS base uri where dacs_signout service is found
     * @param jurisdiction jurisdiction in which to sigout
     * @throws java.lang.Exception TODO
     */
    public DacsSignoutService(String baseuri, String jurisdiction) {
        super(baseuri + "/" + ServiceName.dacs_signout);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>();
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
        this.nvs.setValue(args.DACS_JURISDICTION, jurisdiction);
    }
    
    /**
     * 
     * constructor for DacsSignoutService (DacsUserAccount signout)
     * calls dacs_signout service on jurisdiction and username, invalidating
     * any credentials for username in that jurisdiction
     * @param baseuri DACS base uri where dacs_signout service is found
     * @param jurisdiction jurisdiction in which to signout
     * @param username username in jurisdiction to signout
     * @throws java.lang.Exception TODO
     */
    public DacsSignoutService(String baseuri, String jurisdiction, String username)
    throws Exception {
        super(baseuri + "/" + ServiceName.dacs_signout);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>();
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
        this.nvs.setValue(args.DACS_JURISDICTION, jurisdiction);
        this.nvs.setValue(args.DACS_USERNAME, username);
    }
}

