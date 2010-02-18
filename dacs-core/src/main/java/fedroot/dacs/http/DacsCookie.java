/*
 * DacsCookie.java
 * Created on Jan 15, 2010 8:24:49 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.http;

import fedroot.dacs.exceptions.DacsRuntimeException;
import java.util.Date;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 *
 * @author ricmorri
 */
public class DacsCookie extends BasicClientCookie {
    
    /** 
     * Creates a new instance of DacsCookie from a javax.servlet.http.net.Cookie
     */
    public DacsCookie(javax.servlet.http.Cookie cookie) throws DacsRuntimeException {
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
    
    public static boolean isDacsCookie(org.apache.http.cookie.Cookie cookie) {
        return isDacsCookieName(cookie.getName());
    }
    
    public static boolean isDacsCookie(javax.servlet.http.Cookie cookie) {
        return isDacsCookieName(cookie.getName());
    }

    public static boolean isDacsSelectCookie(org.apache.http.cookie.Cookie cookie) {
        return isDacsSelectCookieName(cookie.getName());
    }

    public static boolean isDacsSelectCookie(javax.servlet.http.Cookie cookie) {
        return isDacsSelectCookieName(cookie.getName());
    }

    private static boolean isDacsCookieName(String cookieName) {
        return (cookieName.startsWith("DACS:") && !cookieName.endsWith(":SELECT"));
    }

    private static boolean isDacsSelectCookieName(String cookieName) {
        return (cookieName.startsWith("DACS:") && cookieName.endsWith(":SELECT"));
    }
}
