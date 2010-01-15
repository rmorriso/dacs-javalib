/*
 * NAT.java
 *
 * Created on September 27, 2005, 10:40 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package com.fedroot.dacs;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Cookie;

/**
 * wrapper class for DACS Notice Acceptance Tokens (NATs)
 * @author rmorriso
 */
public class NAT {
    String name;
    String value;
    String[] noticeUris;
    String[] resourceUris;
    
    /**
     * Creates a new instance of NAT based on a DACS NAT cookie
     * @param cookie a DACS NAT cookie (cookie name of the form "NAT-xxx")
     */
    public NAT(Cookie cookie) 
    throws RuntimeException {
        String name = cookie.getName();
        if (name.startsWith("NAT-")) {
            this.name = name;
            byte[] encodedarray = cookie.getValue().getBytes();
            if (Base64.isArrayByteBase64(encodedarray)) {
                String value = new String(Base64.decodeBase64(encodedarray));
                this.value = value;
                int i = value.indexOf("NoticeURIs=");
                if (i == -1) {
                    throw new RuntimeException("NoticeURIs not present in NAT");
                } else {
                    // skip to notices
                    i += "NoticeURIs=".length() + 1;
                }
                String noticestr = value.substring(i);
                // notice string is terminated by quote
                noticestr = noticestr.substring(0, noticestr.indexOf("\""));
                // add a "," so that split() will work with a single noticeuri
                noticeUris = noticestr.concat(",").split(",");
            } else {
                throw new RuntimeException("NAT value not Base64 encoded");
            }
        } else {
            throw new RuntimeException("invalid NAT cookie: " + name);
        }
    }
        
    public String getNoticeUris() {
        StringBuffer buf = new StringBuffer("NoticeUris: ");
        for (String uri : this.noticeUris) {
            buf.append(uri + "; ");
        }
        return buf.toString();
    }
    
    /**
     * return string representation of NAT
     */
    public String toString() {
        String s = name + ", NoticeURIs[";
        for (String n : noticeUris) {
            s = s.concat(n + ", ");
        }
        return s + "]";
    }
    
}
