/*
 * DacsPasswordService.java
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
 * wrapper class for DACS dacs_passwd service;
 * provides constructors for setting the password of
 * a DACS user account by an ADMIN _IDENTITY or
 * for an ordinary user to change their own password by
 * providing the current password
 * @author rmorriso
 */
public class DacsPasswordService extends DacsService{
    public enum args { CONFIRM_NEW_PASSWORD, DACS_VERSION, FORMAT, NEW_PASSWORD, OPERATION, PASSWORD, USERNAME };
    public static final Set<args> mandatory = EnumSet.of (args.CONFIRM_NEW_PASSWORD, args.NEW_PASSWORD, args.USERNAME, args.OPERATION);
    public enum ops { SET };
    
    /**
     * constructor for DacsPasswordService (for use by ADMIN_IDENTY)
     * @param baseuri DACS base uri where dacs_passwd service is found
     * @param username DACS username for which password is to be set
     * @param password new password to be set
     * @throws java.lang.Exception TODO
     */
    public DacsPasswordService(String baseuri, String username, String password) {
        super(baseuri + "/" + ServiceName.dacs_passwd);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>(mandatory); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
        this.nvs.setValue(args.USERNAME, username);
        this.nvs.setValue(args.NEW_PASSWORD, password);
        this.nvs.setValue(args.CONFIRM_NEW_PASSWORD, password);
        this.nvs.setValue(args.OPERATION, ops.SET.toString());
    }
    
   /**
     * constructor for DacsPasswordService (for use by ordinary users)
     * @param baseuri DACS base uri where dacs_passwd service is found
     * @param username DACS username for which password is to be set
     * @param oldpassword current password for username
     * @param password new password to be set
     * @throws java.lang.Exception TODO
     */
    public DacsPasswordService(String baseuri, String username, String oldpassword, String password) {
        super(baseuri + "/" + ServiceName.dacs_passwd);
        // create NameValueSet of allowed and required arguments
        this.nvs = new NameValueSet<args>(mandatory); 
        // default Reply Format is XMLSCHEMA; can be changed with setReplyFormat()
        this.nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
        this.nvs.setValue(args.USERNAME, username);
        this.nvs.setValue(args.PASSWORD, oldpassword);
        this.nvs.setValue(args.NEW_PASSWORD, password);
        this.nvs.setValue(args.CONFIRM_NEW_PASSWORD, password);
        this.nvs.setValue(args.OPERATION, ops.SET.toString());
    }
}
