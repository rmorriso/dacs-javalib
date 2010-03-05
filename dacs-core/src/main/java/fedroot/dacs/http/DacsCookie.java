/*
 * DacsCookie.java
 * Created on Jan 15, 2010 8:24:49 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */
package fedroot.dacs.http;

import fedroot.dacs.exceptions.DacsRuntimeException;
import java.util.Date;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 *
 * @author ricmorri
 */
public class DacsCookie extends BasicClientCookie {

    /** 
     * Creates a new instance of DacsCookie from a javax.servlet.http.net.Cookie
     */
    public DacsCookie(String domain, javax.servlet.http.Cookie cookie) throws DacsRuntimeException {
//        super(federationDomain, jcookie.getName(),jcookie.getValue(),"/", jcookie.getMaxAge(),jcookie.getSecure());
        super(cookie.getName(), cookie.getValue());

        if (!isDacsCookie(cookie)) {
            throw new DacsRuntimeException("invalid DACS cookie: " + cookie.getName());
        }
        
        setVersion(1);
        setDomain("." + domain);
        setPath("/");

        if (cookie.getMaxAge() == -1) {
        } else {
            Date expires = new Date();
            expires.setTime(expires.getTime() + cookie.getMaxAge());
            setExpiryDate(expires);
        }

        setSecure(cookie.getSecure());

        // Set attributes EXACTLY as sent by the server
        setAttribute(ClientCookie.VERSION_ATTR, "1");
        setAttribute(ClientCookie.DOMAIN_ATTR, domain);
    }

    public DacsCookie(String domain, String name, String value) throws DacsRuntimeException {
        super(name, value);

        if (!isDacsCookie(this)) {
            throw new DacsRuntimeException("invalid DACS cookie: " + this.getName());
        }

        setVersion(1);
        setDomain("." + domain);
        setPath("/");

        Date expires = new Date();
        expires.setTime(expires.getTime() + 3600);

        setSecure(false);
        // Set attributes EXACTLY as sent by the server
        setAttribute(ClientCookie.VERSION_ATTR, "1");
        setAttribute(ClientCookie.DOMAIN_ATTR, domain);
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
