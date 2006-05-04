/*
 * DacsCurrentCredentialsService.java
 *
 * Created on August 30, 2005, 12:12 AM
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
 * wrapper class for dacs_current_credentials service
 * @author rmorriso
 */
public class DacsCurrentCredentialsService extends DacsService{
    public enum args { DACS_JURISDICTION, DACS_VERSION, FORMAT, USERNAME};
    public static final Set<args> mandatory = null;
    
    /**
     * constructor for DacsCurrent CredentialsService;
     * this is a varargs constructor which can be called with only
     * a jurisdiction name or with a jurisdiction name and a username
     * @param baseuri DACS base uri where dacs_current_credentials service is found
     * @param jurisdiction_username varargs parameter for jurisdiction and unsername
     * @throws java.lang.Exception TODO
     */
    
    public DacsCurrentCredentialsService(String baseuri, String... jurisdiction_username) {
        super(baseuri + "/" + ServiceName.dacs_current_credentials);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>(); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
        if (jurisdiction_username.length > 0) {
            this.nvs.setValue(args.DACS_JURISDICTION, jurisdiction_username[0]);
        }
        if (jurisdiction_username.length > 1) {       
            this.nvs.setValue(args.USERNAME, jurisdiction_username[1]);
        }
    }
}
