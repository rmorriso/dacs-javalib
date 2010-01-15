/*
 * DacsAuthenticateService.java
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
 * wrapper class for dacs_authenticate service
 * @author rmorriso
 */
public class DacsAuthenticateService extends DacsService{
    public enum args { DACS_BROWSER, DACS_JURISDICTION, DACS_VERSION, FORMAT, PASSWORD, USERNAME };
    public static final Set<args> mandatory = EnumSet.of (args.USERNAME, args.PASSWORD);
    
    /**
     * constructor DacsAuthenticateService
     * @param baseuri DACS base uri where dacs_current_credentials service is found
     * @param jurisdiction jurisdiction name (note that jurisdiction must be the
     * same jurisdiction that is fielding the dacs_authenticate service request)
     * 
     * be the
     * @param username username
     * @param password password for account username
     * @throws java.lang.Exception TODO
     */
    public DacsAuthenticateService(String baseuri, String jurisdiction, String username, String password) {
        super(baseuri + "/" + ServiceName.dacs_authenticate);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>(mandatory); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
        this.nvs.setValue(args.DACS_JURISDICTION, jurisdiction);
        this.nvs.setValue(args.USERNAME, username);
        this.nvs.setValue(args.PASSWORD, password);
        this.nvs.setValue(args.DACS_BROWSER, "1");
    }
}
