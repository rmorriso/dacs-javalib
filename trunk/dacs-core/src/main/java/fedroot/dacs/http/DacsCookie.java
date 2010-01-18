/*
 * DacsCookie.java
 *
 * Created on November 1, 2006, 8:43 AM
 *
 * Copyright 2006 Cisco Systems, Inc. All rights reserved. No portion of this
 * software may be reproduced in any form, or by any means, without prior written
 * permission from Cisco Systems, Inc.
 */

package fedroot.dacs.http;

import fedroot.dacs.exceptions.DacsRuntimeException;
import java.util.Date;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 *
 * @author ricmorri
 */
public class DacsCookie extends BasicClientCookie {
    
    /** 
     * Creates a new instance of DacsCookie from a javax.servlet.http.net.Cookie
     */
    public DacsCookie(String federationDomain, javax.servlet.http.Cookie cookie) throws DacsRuntimeException {
//        super(federationDomain, jcookie.getName(),jcookie.getValue(),"/", jcookie.getMaxAge(),jcookie.getSecure());
        super(cookie.getName(), cookie.getValue());

        if(! isDacsCookie(cookie)) {
            throw new DacsRuntimeException("invalid DACS cookie: " + cookie.getName());
        }
        Date expires = new Date();
        expires.setTime(expires.getTime() + cookie.getMaxAge());

        setExpiryDate(expires);
        setSecure(cookie.getSecure());
    }
    
    public static boolean isDacsCookie(Cookie cookie) {
        return cookie.getName().startsWith("DACS:");
    }
    
    public static boolean isDacsCookie(javax.servlet.http.Cookie cookie) {
        return cookie.getName().startsWith("DACS:");
    }

}
