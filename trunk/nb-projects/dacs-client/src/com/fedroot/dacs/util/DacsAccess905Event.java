/*
 * DacsAccess905Event.java
 *
 * Created on December 13, 2005, 10:53 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs.util;

import com.fedroot.dacs.http.DacsGetMethod;
import com.fedroot.dacs.services.DacsNoticeAckService;
import com.fedroot.dacs.services.DacsNoticeAckService.ResponseValue;
import com.fedroot.dacs.services.DacsNoticePresentationService;
import com.fedroot.dacs.xmlbeans.Event905Document;
import com.fedroot.util.httpclient.NameValueSet;
import java.math.BigInteger;

/**
 * wrapper for a DACS 905 (ACK_NEEDED) event
 * @author rmorriso
 */
public class DacsAccess905Event extends DacsAccessEvent{
    private String notice_uris;
    private String resource_uris;
    private String presentation_handler;
    private String ack_handler;
    private BigInteger time;
    private String hmac;
    
    /**
     * Creates a new instance of Dacs902Event
     */
    public DacsAccess905Event(String fed_domain, String jur_dacsuri, Event905Document.Event905 event) {
        super();
        this.fed_domain = fed_domain;
        this.jur_dacsuri = jur_dacsuri;
        this.notice_uris = event.getNoticeUris();
        this.resource_uris = event.getResourceUris();
        this.presentation_handler = event.getPresentationHandler();
        this.ack_handler = event.getAckHandler();
        this.time = event.getTime();
        this.hmac = event.getHmac();
    }
    
    /**
     * getter for this.notice_uris
     * @return 
     */
    public String getNoticeUris() {
        return this.notice_uris;
    }
    
    /**
     * getter for this.resource_uris
     * @return 
     */
    public String getResourceUris() {
        return this.resource_uris;
    }
    
    /**
     * getter for this.time
     * @return 
     */
    public String getTime() {
        return this.time.toString();
    }
    
    /**
     * getter for this.hmac
     * @return 
     */
    public String getHMAC() {
        return this.hmac;
    }
    
    /**
     * getter for this.ack_handler
     * @return 
     */
    public String getAckHandler() {
        return this.ack_handler;
    }
    
    /**
     * getter for this.presentation_handler
     * @return 
     */
    public String getPresentationHandler() {
        return this.presentation_handler;
    }
   
    /**
     * build and return a DacsGetMethod to present the required notices
     * @return 
     */
    public DacsGetMethod getDacsNoticePresentationMethod() {
        // note: for now we ignore the value of event.getPresentationHandler()
        // also need to implement a constructor that uses event.getPresentationHandler(
        // return new DacsNoticePresentationService(this.jur_dacsuri, this.notice_uris, this.resource_uris);
        NameValueSet nvs = DacsNoticePresentationService.getSecureNVS(this.notice_uris, this.resource_uris, this.getTime(), this.hmac);
        return new DacsGetMethod(this.presentation_handler, nvs);
    }
    
    /**
     * build and return a DacsGetMethod to acknowledge notices
     * takes the new time anbd hmac returned in a previous PresentationReply
     * @param response accept or decline
     * @param time 
     * @param hmac 
     * @return 
     */
    public DacsGetMethod getDacsNoticeAckMethod(DacsNoticeAckService.ResponseValue response, String time, String hmac) {
        // note: for now we ignore the value of event.getPresentationHandler()
        // also need to implement a constructor that uses event.getPresentationHandler(
        // return new DacsNoticePresentationService(this.jur_dacsuri, this.notice_uris, this.resource_uris);
        NameValueSet nvs = DacsNoticeAckService.getSecureNVS(response, this.notice_uris, this.resource_uris, time, hmac);
        return new DacsGetMethod(this.ack_handler, nvs);
    }
} 
