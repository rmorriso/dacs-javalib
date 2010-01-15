/*
 * Notice.java
 *
 * Created on December 16, 2005, 10:40 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;

import com.fedroot.dacs.xmlbeans.NoticeDocument;
import org.apache.commons.codec.binary.Base64;

/**
 * wrapper class for DACS Notice Acceptance Tokens (NATs)
 * @author rmorriso
 */
public class Notice {
    String notice_uri;
    String base64content;
    String content;
    
    
    /**
     * Creates a new instance of Notice from XML notice
     * 
     * @param notice_uri a uri referencing the notice content)
     * @param base64content the Base 64 encoded content of the notice
     */
    public Notice(String notice_uri, String base64content) {
        this.notice_uri = notice_uri;
        this.base64content = base64content;
        byte[] encodedarray = base64content.getBytes();
        if (Base64.isArrayByteBase64(encodedarray)) {
            this.content = new String(Base64.decodeBase64(encodedarray));
        }
    }
    
    /**
     * return string representation of Notice
     */
    public String getContent() {
        return this.content;
    }
    
}
