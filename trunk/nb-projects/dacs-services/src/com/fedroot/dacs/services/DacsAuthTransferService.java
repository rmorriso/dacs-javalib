/*
 * DacsAuthTransferService.java
 *
 * Created on March 6, 2006, 12:12 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package com.fedroot.dacs.services;

import com.fedroot.util.httpclient.NameValueSet;
import com.fedroot.dacs.services.DACS.ServiceName;
import java.util.EnumSet;
import java.util.Set;
import org.apache.commons.httpclient.NameValuePair;

/**
 * wrapper class for dacs_auth_agent service
 * @author rmorriso
 */
public class DacsAuthTransferService extends DacsService{
    public enum args { 
        DACS_DEBUG, DACS_IDENTITY, FORMAT, INITIAL_FEDERATION, 
        OPERATION, REDIRECT_DEFAULT, TARGET_FEDERATION, 
        TRANSFER_SUCCESS_URL, TRANSFER_ERROR_URL };
    // allowable values of OPERATION:
    public enum ops { EXPORT, IMPORT, PRESENTATION, TOKEN };
    public static final Set<args> mandatory = EnumSet.of (args.OPERATION );
   
    /**
     * constructor for DacsAuthTransferService (PRESENTATION Stage);
     * generates new DACS credentials without requiring a password;
     * the username provided is purported to be the DACS
     * username of a user known to the receiving jurisdiction
     * @param baseuri DACS base uri where dacs_current_credentials service is found
     * @throws java.lang.Exception TODO
     */
    public DacsAuthTransferService(String baseuri, String jurisdiction, String username) {
        super(baseuri + "/" + ServiceName.dacs_auth_transfer);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>(mandatory); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
//        this.nvs.setValue(args.DACS_JURISDICTION, jurisdiction);
//        this.nvs.setValue(args.USERNAME, username);
    }
    
    /**
     * constructor for DacsAuthAgentService (alien mode)
     * @param baseuri DACS base uri where dacs_current_credentials service is found
     * @param jurisdiction jurisdiction name
     * @param alien_federation name of alien federation
     * @param alien_username username of alien user
     * @throws java.lang.Exception TODO
     */
    public DacsAuthTransferService(String baseuri, String jurisdiction, String alien_federation, String alien_username) {
        super(baseuri + "/" + ServiceName.dacs_auth_agent);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>(mandatory); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
//        this.nvs.setValue(args.DACS_JURISDICTION, jurisdiction);
//        this.nvs.setValue(args.ALIEN_USERNAME, alien_username);
//        this.nvs.setValue(args.DACS_BROWSER, "1");
    }
}
