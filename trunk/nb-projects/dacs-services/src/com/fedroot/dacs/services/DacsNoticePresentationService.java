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
import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.services.DACS.ServiceName;
import java.util.EnumSet;
import java.util.Set;
import org.apache.commons.httpclient.NameValuePair;

/**
 * wrapper class for DACS dacs_notices (presentation) service;
 * note that DACS provideds a single program, dacs_notices, that
 * provides both a presentation and an acknowledgment function.
 * @author rmorriso
 */

// note: perhaps separate notice handling from notice presentation?
public class DacsNoticePresentationService extends DacsService{
    public enum args 
    { ACCEPT_LABEL, DECLINE_LABEL, FORMAT, HMAC, NOTICE_URIS, RANDOM, RESOURCE_URIS, TIME };
    public static final Set<args> mandatory = EnumSet.of (args.NOTICE_URIS, args.RESOURCE_URIS);
    
    /**
     * Creates a new instance of DacsNoticePresentationService
     * @param baseuri DACS base uri where dacs_passwd service is found
     * @param notice_uris uris of notices to be presented to user
     * @param resource_uris uris of resources for which acknowledgement of notices
     * is required
     * @throws java.lang.Exception TODO
     */
    public DacsNoticePresentationService(String baseuri, String notice_uris, String resource_uris) {
        super(baseuri + "/" + ServiceName.dacs_notices);
        this.setParams(this.nvs, notice_uris, resource_uris);
    }
    
    /**
     * 
     * @param baseuri DACS base uri where dacs_notice service is found
     * @param notices_uri uris of notices to be presented to user
     * @param resource_uri uris of resources for which acknowledgement of notices
     * is required
     * @param time time returned by DACS_ACS service
     * @param hmac hmac returned by DACS_ACS service
     * @throws java.lang.Exception TODO
     */
    public DacsNoticePresentationService(String baseuri, String notice_uris, String resource_uris, String time, String hmac) {
        super(baseuri + "/" + ServiceName.dacs_notices);
        this.nvs = new NameValueSet<args>(mandatory); 
        this.setSecureParams(this.nvs, notice_uris, resource_uris, time, hmac);
    }
    
    private static void setParams(NameValueSet nvs, String notices_uri, String resource_uri) {
        // create NameValueSet of allowed and mandatory arguments
        nvs.setValue(args.NOTICE_URIS, notices_uri);
        nvs.setValue(args.RESOURCE_URIS, resource_uri);
        nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
    }
    
    private static void setSecureParams(NameValueSet nvs, String notice_uris, String resource_uris, String time, String hmac) {
        // create NameValueSet of allowed and mandatory arguments
        nvs.setValue(args.NOTICE_URIS, notice_uris);
        nvs.setValue(args.RESOURCE_URIS, resource_uris);
        nvs.setValue(args.TIME, time);
        nvs.setValue(args.HMAC, hmac);
        nvs.setValue(DACS.CommonArgs.FORMAT, DACS.ReplyFormat.XMLSCHEMA.toString());
    }
    
    public static NameValueSet getNVS(String notice_uris, String resource_uris) {
        NameValueSet nvs = new NameValueSet<args>(mandatory); 
        setParams(nvs, notice_uris, resource_uris);
        return nvs;
    }
    
    public static NameValueSet getSecureNVS(String notice_uris, String resource_uris, String time, String hmac) {
        NameValueSet nvs = new NameValueSet<args>(mandatory); 
        setSecureParams(nvs, notice_uris, resource_uris, time, hmac);
        return nvs;
    }
}
