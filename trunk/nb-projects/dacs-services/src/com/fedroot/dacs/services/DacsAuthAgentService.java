/*
 * DacsAuthAgentService.java
 *
 * Created on October 4, 2005, 12:12 AM
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
public class DacsAuthAgentService extends DacsService{
    public enum local_mode_args { COOKIE_SYNTAX, DACS_BROWSER, DACS_JURISDICTION, DACS_VERSION, FORMAT, OPERATION, USERNAME };
    public static final Set<local_mode_args> local_mode_mandatory = EnumSet.of (local_mode_args.DACS_JURISDICTION, local_mode_args.USERNAME );
    public enum alien_mode_args { ALIEN_FEDERATION, ALIEN_USERNAME, COOKIE_SYNTAX, DACS_BROWSER, DACS_JURISDICTION, DACS_VERSION, FORMAT, OPERATION };
    public static final Set<alien_mode_args> alien_mode_mandatory = EnumSet.of (alien_mode_args.ALIEN_FEDERATION, alien_mode_args.ALIEN_USERNAME, alien_mode_args.DACS_JURISDICTION);
    
    /**
     * constructor for DacsAuthAgentService (local mode);
     * generates new DACS credentials without requiring a password;
     * the username provided is purported to be the DACS
     * username of a user known to the receiving jurisdiction
     * @param baseuri DACS base uri where dacs_current_credentials service is found
     * @param jurisdiction name of jurisdiction in which to generate credentials
     * @param username username for which credentials are to be generated
     * @throws java.lang.Exception TODO
     */
    public DacsAuthAgentService(String baseuri, String jurisdiction, String username) {
        super(baseuri + "/" + ServiceName.dacs_auth_agent);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<local_mode_args>(local_mode_mandatory); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
        this.nvs.setValue(local_mode_args.DACS_JURISDICTION, jurisdiction);
        this.nvs.setValue(local_mode_args.USERNAME, username);
        this.nvs.setValue(local_mode_args.DACS_BROWSER, "1");
    }
    
    /**
     * constructor for DacsAuthAgentService (alien mode)
     * @param baseuri DACS base uri where dacs_current_credentials service is found
     * @param jurisdiction jurisdiction name
     * @param alien_federation name of alien federation
     * @param alien_username username of alien user
     * @throws java.lang.Exception TODO
     */
    public DacsAuthAgentService(String baseuri, String jurisdiction, String alien_federation, String alien_username) {
        super(baseuri + "/" + ServiceName.dacs_auth_agent);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<local_mode_args>(local_mode_mandatory); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
        this.nvs.setValue(alien_mode_args.DACS_JURISDICTION, jurisdiction);
        this.nvs.setValue(alien_mode_args.ALIEN_FEDERATION, alien_federation);
        this.nvs.setValue(alien_mode_args.ALIEN_USERNAME, alien_username);
        this.nvs.setValue(alien_mode_args.DACS_BROWSER, "1");
    }
}
