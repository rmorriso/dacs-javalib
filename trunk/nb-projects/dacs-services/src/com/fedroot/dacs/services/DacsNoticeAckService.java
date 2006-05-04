/*
 * DacsNoticesPresentationService.java
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
 * wrapper class for DACS dacs_notices (acknowledgement) service;
 * this is the insecure variant without time/hmac parameters required
 * to enforce a secure workflow
 * note that DACS provides a single program, dacs_notices, that
 * provides both a presentation and an acknowledgment function.
 * @author rmorriso
 */

public class DacsNoticeAckService extends DacsService{
    public enum args 
    { ACCEPT_LABEL, DECLINE_LABEL, FORMAT, HMAC, NOTICE_URIS, RANDOM, RESPONSE, RESOURCE_URIS, TIME };
    public static final Set<args> mandatory = EnumSet.of (args.NOTICE_URIS, args.RESPONSE, args.RESOURCE_URIS);
    public enum ResponseValue
    { accepted, declined };
    
    /**
     * constructor for DacsNoticesAckService;
     * @param baseuri DACS base uri where dacs_passwd service is found
     * @param response users response (accepted or declined)
     * @param notice_uris uris of notices to be presented to user
     * @param resource_uris uris of resources for which acknowledgement of notices
     * is required
     * @throws java.lang.Exception 
     */
    public DacsNoticeAckService(String baseuri, ResponseValue response, String notice_uris, String resource_uris) {
        super(baseuri + "/" + ServiceName.dacs_notices);
        this.nvs = new NameValueSet<args>(mandatory); 
        setParams(this.nvs, response, notice_uris, resource_uris);
    }
    
    /**
     * constructor for DacsNoticesAckService;
     * this is the secure variant with time/hmac parameters required
     * to enforce a secure workflow
     * @param baseuri DACS base uri where dacs_passwd service is found
     * @param response user response (accepted or declined)
     * @param notice_uris uris of notices to be presented to user
     * @param resource_uris uris of resources for which acknowledgement of notices
     * is required
     * @param time time returned by DACS_ACS service
     * @param hmac hmac returned by DACS_ACS service
     * @throws java.lang.Exception 
     */
    public DacsNoticeAckService(String baseuri, ResponseValue response, String notice_uris, String resource_uris, String time, String hmac) {
        super(baseuri + "/" + ServiceName.dacs_notices);
        this.nvs = new NameValueSet<args>(mandatory); 
        setSecureParams(this.nvs, response, notice_uris, resource_uris, time, hmac);
    }
    
     private static void setParams(NameValueSet nvs, ResponseValue response, String notices_uri, String resource_uri) {
        // create NameValueSet of allowed and mandatory arguments
        nvs.setValue(args.RESPONSE, response.toString());
        nvs.setValue(args.NOTICE_URIS, notices_uri);
        nvs.setValue(args.RESOURCE_URIS, resource_uri);
        nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
    }
    
    private static void setSecureParams(NameValueSet nvs, ResponseValue response, String notice_uris, String resource_uris, String time, String hmac) {
        // create NameValueSet of allowed and mandatory arguments
        nvs.setValue(args.RESPONSE, response.toString());
        nvs.setValue(args.NOTICE_URIS, notice_uris);
        nvs.setValue(args.RESOURCE_URIS, resource_uris);
        nvs.setValue(args.TIME, time);
        nvs.setValue(args.HMAC, hmac);
        nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
    }
    
    /**
     * 
     * @param response 
     * @param notice_uris 
     * @param resource_uris 
     * @return 
     */
    public static NameValueSet getNVS(ResponseValue response, String notice_uris, String resource_uris) {
        NameValueSet nvs = new NameValueSet<args>(mandatory); 
        setParams(nvs, response, notice_uris, resource_uris);
        return nvs;
    }
    
    /**
     * 
     * @param response 
     * @param notice_uris 
     * @param resource_uris 
     * @param time 
     * @param hmac 
     * @return 
     */
    public static NameValueSet getSecureNVS(ResponseValue response, String notice_uris, String resource_uris, String time, String hmac) {
        NameValueSet nvs = new NameValueSet<args>(mandatory); 
        setSecureParams(nvs, response, notice_uris, resource_uris, time, hmac);
        return nvs;
    }
}
